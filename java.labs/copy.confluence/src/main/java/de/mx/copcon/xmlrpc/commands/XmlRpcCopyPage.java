/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mx.copcon.xmlrpc.commands;

import de.mx.copcon.InformationCallback;
import de.mx.copcon.xmlrpc.ContentProcessor;
import de.mx.copcon.Command;
import de.mx.copcon.xmlrpc.RpcCommandException;
import de.mx.copcon.CommandFactory;
import de.mx.copcon.XmlRpcException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marxma
 */
public class XmlRpcCopyPage implements Command {

    private final static String SQL_GETMETADATA = "select * from OS_PROPERTYENTRY where ENTITY_ID = ?";
    private final static String SQL_GETMETADATA1 = "select * from OS_PROPERTYENTRY w"
            + "here ENTITY_ID = ? and ENTITY_KEY = ? and entity_name = ?";
    private final static String SQL_INSERTMETADATA = "insert into OS_PROPERTYENTRY "
            + "(entity_name, entity_id, entity_key, key_type, boolean_val, "
            + "double_val, string_val, text_val, long_val, int_val, date_val) "
            + "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";
    private final static String SQL_UPDATEMETADATA = "update OS_PROPERTYENTRY set text_val = "
            + "(select text_val from OS_PROPERTYENTRY where ENTITY_KEY = ? "
            + "and entity_id=?) where entity_id = ? and entity_key like '~metadata.%'";
    private final static String SQL_GETPAGECONTENT = "select BODYCONTENT.CONTENTID, body from BODYCONTENT "
            + "join CONTENT on (BODYCONTENT.CONTENTID = CONTENT.CONTENTID) "
            + "where CONTENT.TITLE = ?";
    
    /**
     * error codes
     */
    public final static int PAGE_NOT_EXIST = 1;
    private final String srcServer;
    private final String srcSpace;
    private final String srcPage;
    private final String destServer;
    private final String destSpace;
    private final String destPage;
    private final List<ContentProcessor> processor;
    private final boolean recursive;
    private EnumSet<CopyEnum> flags;

    /**
     *
     * @param srcToken
     * @param srcSpace
     * @param srcParentPage
     * @param srcPage
     * @param destToken
     * @param destSpace
     * @param destParentPage
     * @param destPage
     */
    public XmlRpcCopyPage(
            final String srcServer, 
            final String srcSpace, 
            final String srcPage, 
            final String destServer, 
            final String destSpace, 
            final String destPage, 
            final boolean recursive, 
            EnumSet<CopyEnum> flags, 
            final List<ContentProcessor> processor) {
        this.srcServer = srcServer;
        this.srcSpace = srcSpace;
        this.srcPage = srcPage;
        this.destServer = destServer;
        this.destSpace = destSpace;
        this.destPage = destPage;
        this.processor = processor;
        this.recursive = recursive;  
        this.flags = flags;
    }

    /**
     *
     * @throws RpcCommandException when a page not exist.
     */
    @Override
    public void run(InformationCallback info) {
        CommandFactory xmlrpc = CommandFactory.INSTANCE;
        if (recursive) {
            copyStructure(xmlrpc, srcSpace, srcPage, destSpace, destPage, info);
        } else {
            if (info != null) {
                info.setInformation(String.format("Copy to Page %s : %s", destSpace, destPage));
            }
            copyPage(xmlrpc, srcSpace, srcPage, destSpace, destPage, info);
        }
    }

    private void copyPage(CommandFactory xmlrpc, String srcSpace, String srcPage, String destSpace, String destPage, InformationCallback info) {
        Map<String, String> srcPageInfo = null;
        Map<String, String> destPageInfo = null;
        String content = null;
        String pageId = null;
        
        try {
            srcPageInfo = xmlrpc.getPage(srcServer, srcSpace, srcPage, CommandFactory.API_VERSION_1);
            content = srcPageInfo.get("content");
            pageId = srcPageInfo.get("id");
        } catch (XmlRpcException ex) {
            Connection srcCon = xmlrpc.getConnection(srcServer);
            try {
                PreparedStatement getcontent = srcCon.prepareCall(SQL_GETPAGECONTENT);
                getcontent.setString(1, srcPage);
                ResultSet resset = getcontent.executeQuery();
                if (resset.next()) {
                    pageId = String.valueOf(resset.getLong("CONTENTID"));
                    content = resset.getString("BODY");
                    content = content.replaceAll("\\x0b", "");
                    content = content.replaceAll("\\x0c", "");
                }
            } catch (SQLException ex1) {
                if (info != null) {
                    info.addError(String.format("could not copy page %s : %s -> %s : %s", srcSpace, srcPage, destSpace, destPage), ex1);
                }

                return;
            }
        }

       
        try {
            destPageInfo = xmlrpc.getPage(destServer, destSpace, destPage, CommandFactory.API_VERSION_2);
        } catch (XmlRpcException ex) {
            if (info != null) {
                info.addError(String.format("could not copy page %s : %s -> %s : %s", srcSpace, srcPage, destSpace, destPage), ex);
            }
            return;
        }
        if (flags.contains(CopyEnum.WITHCONTENT)) {
            copyContent(xmlrpc, destSpace, destPage, content, destPageInfo, info);
        }
        
        if (flags.contains(CopyEnum.WITHATTACHMENT)) {
            copyAttachments(xmlrpc, pageId, info, srcSpace, srcPage, destSpace, destPage, destPageInfo);
        }
        
        if (flags.contains(CopyEnum.WITHMETADATA)) {
            try {
                //copy metadata
                copyMetadata(xmlrpc, pageId, destPageInfo.get("id"));
            } catch (SQLException ex) {
                if (info != null) {
                    info.addError(String.format("could not copy metadata from page %s : %s -> %s : %s", srcSpace, srcPage, destSpace, destPage), ex);
                }
            }
        }
        
        if (flags.contains(CopyEnum.WITHLABEL)) {
            copyLabels(xmlrpc, pageId, destPageInfo.get("id"), info);
        }
        
        if (flags.contains(CopyEnum.WITHCOMMENT)) {
            copyComments(xmlrpc, pageId, destPageInfo.get("id"), info);
        }
    }

    private void copyStructure(CommandFactory xmlrpc, String srcSpace, String srcPage, String destSpace, String destPage, InformationCallback info) {

        if (info != null) {
            info.setInformation(String.format("Create Pagestructure for %s : %s", destSpace, destPage));
        }
        XmlRpcLoadStructure loadStructure = new XmlRpcLoadStructure(srcServer, srcSpace, srcPage);
        loadStructure.run(info);
        int sumPageCount = loadStructure.getPageStructure().size();
        if (info != null) {
            info.setInformation(String.format("Create Pagestructure for %s [%d pages found]",
                    destPage, sumPageCount));
        }
        XmlRpcSaveStructure saveStructure = new XmlRpcSaveStructure(loadStructure.getPageStructure(),
                destServer, destSpace, destPage);

        saveStructure.run(info);
        int pageCount = 0;
        copyPage(xmlrpc, srcSpace, srcPage, destSpace, destPage, info);
        for (String path : loadStructure.getPageStructure()) {

            String copyPage = path.substring(path.lastIndexOf("/") + 1);
            if (info != null) {
                info.setInformation(String.format("Copy to Page %s : %s [%d/%d]", destSpace, destPage, pageCount, sumPageCount));
            }
            copyPage(xmlrpc, srcSpace, copyPage, destSpace, copyPage, info);

            pageCount++;



        }



    }

    private void copyMetadata(CommandFactory cmd, String srcPageId, String destPageId) throws SQLException {
        //System.out.println("copy metadata "+srcPageId+","+destPageId);
        Connection srcCon = cmd.getConnection(srcServer);
        Connection destCon = cmd.getConnection(destServer);
        Long destPage = Long.valueOf(destPageId);
        PreparedStatement srcmetadata = srcCon.prepareStatement(SQL_GETMETADATA);
        srcmetadata.setLong(1, Long.valueOf(srcPageId));

        ResultSet metadataset = srcmetadata.executeQuery();
        int maxMetaData = 0;
        PreparedStatement insertLiveTempStatement = destCon.prepareStatement(SQL_INSERTMETADATA);
        
        while (metadataset.next()) {
            //System.out.println("copy metadata");
            PreparedStatement getdestmetadata = destCon.prepareStatement(SQL_GETMETADATA1);
            getdestmetadata.setLong(1, destPage);
            getdestmetadata.setString(2, metadataset.getString("entity_key"));
            getdestmetadata.setString(3, metadataset.getString("entity_name"));

            String entity_key = metadataset.getString("entity_key");

            if (entity_key.contains("~metadata.")) {
                int value = Integer.valueOf(entity_key.substring(entity_key.lastIndexOf(".") + 1));
                if (value > maxMetaData) {
                    insertLiveTempStatement.setString(1, metadataset.getString("entity_name"));
                    insertLiveTempStatement.setLong(2, destPage);
                    insertLiveTempStatement.setString(3, metadataset.getString("entity_key"));
                    insertLiveTempStatement.setLong(4, metadataset.getLong("key_type"));
                    insertLiveTempStatement.setBoolean(5, metadataset.getBoolean("boolean_val"));
                    insertLiveTempStatement.setDouble(6, metadataset.getDouble("double_val"));
                    insertLiveTempStatement.setString(7, metadataset.getString("string_val"));
                    insertLiveTempStatement.setString(8, metadataset.getString("text_val"));
                    insertLiveTempStatement.setLong(9, metadataset.getLong("long_val"));
                    insertLiveTempStatement.setInt(10, metadataset.getInt("int_val"));
                    insertLiveTempStatement.setDate(11, metadataset.getDate("date_val"));
                }
                maxMetaData = value;
            } else {

                ResultSet destmetadatares = getdestmetadata.executeQuery();

                if (!destmetadatares.next()) {
                    PreparedStatement insertStatement = destCon.prepareStatement(SQL_INSERTMETADATA);
                    insertStatement.setString(1, metadataset.getString("entity_name"));
                    insertStatement.setLong(2, destPage);
                    insertStatement.setString(3, metadataset.getString("entity_key"));
                    insertStatement.setLong(4, metadataset.getLong("key_type"));
                    insertStatement.setBoolean(5, metadataset.getBoolean("boolean_val"));
                    insertStatement.setDouble(6, metadataset.getDouble("double_val"));
                    insertStatement.setString(7, metadataset.getString("string_val"));
                    insertStatement.setString(8, metadataset.getString("text_val"));
                    insertStatement.setLong(9, metadataset.getLong("long_val"));
                    insertStatement.setInt(10, metadataset.getInt("int_val"));
                    insertStatement.setDate(11, metadataset.getDate("date_val"));
                    if (insertStatement.executeUpdate() != 1) {
                        System.err.println("could not insert metadata");
                    }
                    insertStatement.close();
                }
                getdestmetadata.close();
            }

        }
        if (maxMetaData > 0) {
            try {
                Map<String, String> page = cmd.getPage(destServer, destPageId, CommandFactory.API_VERSION_2);
                insertLiveTempStatement.setString(3, "~metadata."+page.get("version"));
                if (insertLiveTempStatement.executeUpdate() == 0) {
                    System.err.println("could not execute insert statement");
                }
            } catch (XmlRpcException ex) {
                Logger.getLogger(XmlRpcCopyPage.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        metadataset.close();
        srcmetadata.close();

    }

    private void copyContent(CommandFactory xmlrpc, String destSpace, String destPage, String content, Map<String, String> destPageInfo, InformationCallback info) {
        if (processor != null && !processor.isEmpty()) {

            for (ContentProcessor p : processor) {
                content = p.process(content);
            }
        }
        destPageInfo.put("content", content);
        try {
            xmlrpc.execute(destServer, "storePage", Arrays.asList(destPageInfo), CommandFactory.API_VERSION_1);
        } catch (XmlRpcException ex) {
            if (info != null) {
                info.addError(String.format("could not copy page %s : %s -> %s : %s", srcSpace, srcPage, destSpace, destPage), ex);
            }
        }
    }

    private void copyAttachments(CommandFactory xmlrpc, String pageId, InformationCallback info, String srcSpace, String srcPage, String destSpace, String destPage, Map<String, String> destPageInfo) {
        Object[] attInfoList = new Object[0];
        try {
            // copy attachment

            attInfoList = (Object[]) xmlrpc.execute(srcServer, "getAttachments",
                    Arrays.asList(new Object[]{pageId}), CommandFactory.API_VERSION_1);
        } catch (XmlRpcException ex) {
            //Logger.getLogger(XmlRpcCopyPage.class.getName()).log(Level.SEVERE, null, ex);
            if (info != null) {
                info.addError(String.format("could not copy page attachments %s : %s -> %s : %s", srcSpace, srcPage, destSpace, destPage), ex);
            }

        }
    
        for (Object att : attInfoList) {
            Map<String, String> attInfo = (Map<String, String>) att;
            try {

                byte[] attData = (byte[]) xmlrpc.execute(srcServer, "getAttachmentData",
                        Arrays.asList(new Object[]{pageId, attInfo.get("fileName"), "0"}), CommandFactory.API_VERSION_1);
                xmlrpc.execute(destServer, "addAttachment",
                        Arrays.asList(new Object[]{destPageInfo.get("id"), attInfo, attData}), CommandFactory.API_VERSION_2);
            } catch (XmlRpcException ex) {
                if (info != null) {
                    info.addError(String.format("could not copy page attachment %s %s : %s -> %s : %s",
                            attInfo.get("fileName"), srcSpace, srcPage, destSpace, destPage), ex);
                }
            }

        }
    }

    private void copyLabels(CommandFactory xmlrpc, String pageId, String destPageId, InformationCallback info) {
        try {
            Object[] labels = (Object[]) xmlrpc.execute(srcServer, "getLabelsById", Arrays.asList(new Object[] {pageId}), CommandFactory.API_VERSION_1);
            System.out.println("found labels " + labels.length);
            for (Object l : labels) {
                Map<String, String> label = (Map<String, String>)l;
                xmlrpc.execute(destServer, 
                        "addLabelByName", 
                        Arrays.asList(new Object[] {label.get("name"), destPageId}), 
                        CommandFactory.API_VERSION_2);
            }
        } catch (XmlRpcException ex) {
            ex.printStackTrace();
            if (info != null) {
                info.addError(String.format("could not copy page labels %s : %s -> %s : %s", srcSpace, srcPage, destSpace, destPage), ex);
            }
        }
        
    }

    private void copyComments(CommandFactory xmlrpc, String pageId, String destPageId, InformationCallback info) {
        try {
            Object[] comments = (Object[]) xmlrpc.execute(srcServer, "getComments", Arrays.asList(new Object[] {pageId}), CommandFactory.API_VERSION_1);
            System.out.println("found comments " + comments.length);
            for (Object c : comments) {
                Map<String, String> comment = (Map<String, String>)c;
                comment.put("pageId", destPageId);
                comment.remove("id");
                comment.remove("url");
                xmlrpc.execute(destServer, 
                        "addComment", 
                        Arrays.asList(new Object[] {comment}), 
                        CommandFactory.API_VERSION_2);
            }
        } catch (XmlRpcException ex) {
            ex.printStackTrace();
            if (info != null) {
                info.addError(String.format("could not copy page comments %s : %s -> %s : %s", srcSpace, srcPage, destSpace, destPage), ex);
            }
        }
    }
}
