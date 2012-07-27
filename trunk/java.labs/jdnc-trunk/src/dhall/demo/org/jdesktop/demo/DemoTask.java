package org.jdesktop.demo;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.MatchingTask;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.selectors.FileSelector;
import org.jdesktop.demo.swingx.SwingXDemo;

/**
 * DemoTask.java
 *
 *
 * Created: Mon Sep  5 20:57:52 2005
 *
 * @author <a href="mailto:dave@dolphin.hallsquared.org">David A. Hall</a>
 * @version 1.0
 */
public class DemoTask extends MatchingTask {
    private List<FileSet> filesets = new ArrayList<FileSet>();
    private File dir;

    public void setDir (File dir) {
        this.dir = dir;
    }

    public void addFileset(FileSet set) {
        filesets.add(set);
    }
    
    public void execute() throws BuildException {
        
        if (dir == null && filesets.size() == 0) {
            throw new BuildException("no files sepecified");
        }

        if (dir != null) {
            FileSet implfs = getImplicitFileSet();
            implfs.setDir(dir);
            processFileSet(implfs);
        }
        
        for(FileSet fs : filesets) {
            processFileSet(fs);
        }

        String[] args = new String[] { "-d", buffer.toString() };
        SwingXDemo.main(args);
    }

    private StringBuffer buffer = new StringBuffer();
    
    private void processFileSet(FileSet fs) {
        DirectoryScanner ds = fs.getDirectoryScanner(getProject());
        for (String filename : ds.getIncludedFiles()) {
            if (filename.endsWith(".java")) {
                filename = filename.substring(0, filename.length() - 5);
            }
            else if (filename.endsWith(".class")) {
                filename = filename.substring(0, filename.length() - 6);
            }
            filename = filename.replaceAll(System.getProperty("file.separator"),".");
            log("will include " +filename, getProject().MSG_VERBOSE);
            if (buffer.length() > 0)
                buffer.append(',');
            buffer.append(filename);
        }
    }
}

