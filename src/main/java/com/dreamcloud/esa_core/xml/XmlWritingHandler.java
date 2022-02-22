package com.dreamcloud.esa_core.xml;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.File;
import java.io.IOException;

abstract public class XmlWritingHandler extends XmlReadingHandler implements AutoCloseable {
    private XMLStreamWriter xmlWriter;

    public XmlWritingHandler() {
    }

    public void open(File outputFile) throws IOException, XMLStreamException {
        this.xmlWriter = BZipFileTools.getXmlWriter(outputFile);
    }

    public void writeDocumentBegin(String openTag) throws XMLStreamException {
        xmlWriter.writeStartDocument();
        xmlWriter.writeStartElement(openTag);
    }

    public void writeDocumentEnd() throws XMLStreamException {
        xmlWriter.writeEndElement();
        xmlWriter.writeEndDocument();
    }

    public void writeStartElement(String tagName) throws XMLStreamException {
        xmlWriter.writeStartElement(tagName);
    }

    public void writeEndElement() throws XMLStreamException {
        xmlWriter.writeEndElement();
    }

    public void writeElement(String tagName, String content) throws XMLStreamException {
        xmlWriter.writeStartElement(tagName);
        xmlWriter.writeCharacters(content);
        xmlWriter.writeEndElement();
    }

    public void close() throws Exception {
        if (xmlWriter != null) {
            xmlWriter.flush();
            xmlWriter.close();
            xmlWriter = null;
        }
        super.close();
    }
}
