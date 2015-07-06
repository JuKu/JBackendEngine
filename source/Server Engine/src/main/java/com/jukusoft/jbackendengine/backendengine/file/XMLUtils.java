package com.jukusoft.jbackendengine.backendengine.file;

import org.w3c.dom.Document;

/**
 * Created by Justin on 29.06.2015.
 */
public class XMLUtils {

    public static String getContent (Document doc, String tagName) {
        //http://www.mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/
        return doc.getElementsByTagName(tagName).item(0).getTextContent();
    }

}
