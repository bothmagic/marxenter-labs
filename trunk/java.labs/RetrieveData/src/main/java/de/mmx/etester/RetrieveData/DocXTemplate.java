/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mmx.etester.RetrieveData;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBElement;
import org.docx4j.XmlUtils;
import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.wml.ContentAccessor;
import org.docx4j.wml.P;
import org.docx4j.wml.Text;

/**
 * Templategenerator.
 * @author marxma
 */
public class DocXTemplate {

    private WordprocessingMLPackage wpp;
    private Map<String, List<Object>> templates = new HashMap<String, List<Object>>();
    private Map<String, List<String>> templateVars = new HashMap<String, List<String>>();
    public DocXTemplate(File template) {
        try {
            wpp = WordprocessingMLPackage.load(template);
        } catch (Docx4JException ex) {
            Logger.getLogger(DocXTemplate.class.getName()).log(Level.SEVERE, null, ex);
        }

        findTemplates();
    }

    public List<String> getTemplates() {
        return new ArrayList<String>(templates.keySet());
    }
    
    public List<String> getTemplateVars(String templatePart) {
        return Collections.unmodifiableList(templateVars.get(templatePart));
    }

    private void findTemplates() {
        List<Object> templateObjects = null;
        String lastTemplateId = null;
        for (Object content : wpp.getMainDocumentPart().getContent()) {
            if (content instanceof P) {
                String text = String.valueOf(content).trim();
                if (text.indexOf("{ET:") >= 0) {
                    if (lastTemplateId == null) {
                        lastTemplateId = text;
                        templateObjects = new ArrayList<Object>();
                        continue;
                    } else if (lastTemplateId.equals(text)) {
                        templates.put(lastTemplateId, templateObjects);
                        lastTemplateId = null;
                        templateObjects = null;
                        continue;
                    }
                }
            }
            
            if (lastTemplateId != null) {
                templateObjects.add(content);
               
                List<Object> vars = getAllElementFromObject(content, Text.class);
                List<String> templateVar;
                
                if (this.templateVars.containsKey(lastTemplateId)) {
                    templateVar = this.templateVars.get(lastTemplateId);
                } else {
                    templateVar = new ArrayList<String>();
                    this.templateVars.put(lastTemplateId, templateVar);
                }
                
                for (Object obj: vars) {
                     Text textElement = (Text) obj;
                     if (textElement.getValue().trim().matches("\\{.*\\}")
                             && !templateVar.contains(textElement.getValue().trim())) {
                         templateVar.add(textElement.getValue().trim());
                     }
                }
                
            }
        }
    }

    void replace(String key, List<Map<String, Object>> valueMapList) {


        List<Object> templateObjects = templates.get(key);
        List<Object> replacedObjects = new ArrayList<Object>();

        for (Map<String, Object> values : valueMapList) {

            for (Object tmplObj : templateObjects) {

                if (tmplObj instanceof JAXBElement) {
                    tmplObj = ((JAXBElement<?>) tmplObj).getValue();
                }

                if (tmplObj instanceof ContentAccessor) {
                    ContentAccessor p = (ContentAccessor) tmplObj;
                    p = XmlUtils.deepCopy(p);
                    replaceVars(p, values);
                    replacedObjects.add(p);
                }
            }
        }

        String lastTemplateId = null;
        int start = -1;
        int end = 0;
        P firstP = null;
        for (Object content : wpp.getMainDocumentPart().getContent()) {
            if (content instanceof P) {
                String text = String.valueOf(content).trim();
                if (text.indexOf(key) >= 0) {
                    if (lastTemplateId == null && start < 0) {
                        start = wpp.getMainDocumentPart().getContent().indexOf(content);
                        firstP = (P) content;
                    } else if (start > 0) {
                        end = wpp.getMainDocumentPart().getContent().indexOf(content);
                    }
                }
            }
        }
        ContentAccessor parent = (ContentAccessor) firstP.getParent();

        for (int i = start; i < end - 1; i++) {
            wpp.getMainDocumentPart().getContent().remove(i);
        }

        ((ContentAccessor) parent).getContent().addAll(start, replacedObjects);



    }

    private void replaceVars(ContentAccessor ca, Map<String, Object> values) {
        List<Object> vars = getAllElementFromObject(ca, Text.class);
        for (Object text : vars) {
            Text textElement = (Text) text;

            if (!textElement.getValue().equals("{Attachment}") &&
                    values.containsKey(textElement.getValue()) && values.get(textElement.getValue()) != null) {
                
                textElement.setValue(String.valueOf(values.get(textElement.getValue())));
            }
        }
        
        vars = getAllElementFromObject(ca, P.class);
        for (Object p : vars) {
            
            if (String.valueOf(p).trim().indexOf("{Attachment}")>=0) {
                List<String> att = (List<String>) values.get("attachments");

                for (String file : att) {
                    try {
                        BinaryPartAbstractImage image =
                                BinaryPartAbstractImage.createImagePart(wpp, new File(file));
                        Inline inline = image.createImageInline( file, file, 
    			0, 1, false);
        
                        org.docx4j.wml.ObjectFactory factory = new org.docx4j.wml.ObjectFactory();        
                        org.docx4j.wml.R  run = factory.createR();		
                        ((P)p).getContent().add(run);        
                        org.docx4j.wml.Drawing drawing = factory.createDrawing();		
                        run.getContent().add(drawing);		
                        drawing.getAnchorOrInline().add(inline);
		

                    } catch (Exception ex) {
                        Logger.getLogger(DocXTemplate.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
                
                ((P)p).getContent().remove(0);
            }

        }
    }
    
    /**
     * helper to get all content objects per type.
     * @param obj
     * @param toSearch
     * @return 
     */
    private List<Object> getAllElementFromObject(Object obj, Class<?> toSearch) {
        List<Object> result = new ArrayList<Object>();
        if (obj instanceof JAXBElement) {
            obj = ((JAXBElement<?>) obj).getValue();
        }

        if (obj.getClass().equals(toSearch)) {
            result.add(obj);
        } else if (obj instanceof ContentAccessor) {
            List<?> children = ((ContentAccessor) obj).getContent();
            for (Object child : children) {
                result.addAll(getAllElementFromObject(child, toSearch));
            }

        }
        return result;
    }

    void save(File output) throws Docx4JException {
        wpp.save(output);
    }

    private Object getItem(Object obj) {
        if (obj instanceof JAXBElement) {
            obj = ((JAXBElement<?>) obj).getValue();
        }
        return obj;
    }
}
