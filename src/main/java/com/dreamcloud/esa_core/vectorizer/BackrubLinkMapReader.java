package com.dreamcloud.esa_core.vectorizer;

import com.dreamcloud.esa_core.xml.BZipFileTools;
import com.dreamcloud.esa_core.xml.XmlReadingHandler;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;

public class BackrubLinkMapReader extends XmlReadingHandler {
    private final SAXParserFactory saxFactory;
    private final MultiValuedMap<Integer, Integer> linkMap = new HashSetValuedHashMap<>();

    public BackrubLinkMapReader() {
        saxFactory = SAXParserFactory.newInstance();
        saxFactory.setNamespaceAware(true);
        saxFactory.setValidating(false);
        saxFactory.setXIncludeAware(true);
    }

    public void parse(File inputFile) throws ParserConfigurationException, SAXException, IOException {
        SAXParser saxParser = saxFactory.newSAXParser();
        Reader reader = BZipFileTools.getFileReader(inputFile);
        InputSource is = new InputSource(reader);
        is.setEncoding("UTF-8");
        saxParser.parse(is, this);
        reader.close();
    }

    protected void handleDocument(Map<String, String> xmlFields) {
        Integer articleId = Integer.parseInt(xmlFields.get("id"));
        String incomingIds = xmlFields.get("incomingLinks");
        String[] incomingLinkIds = incomingIds.split(",");
        for (String incomingLinkId: incomingLinkIds) {
            linkMap.put(articleId, Integer.parseInt(incomingLinkId));
        }
    }

    public MultiValuedMap<Integer, Integer> getLinkMap() {
        return linkMap;
    }
}
