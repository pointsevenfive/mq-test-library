package com.pointsevenfive.esb.util;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Validation {

    DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    Map<String, String> paths = new HashMap<>();
    Document doc;

    public Validation(String xml) throws ParserConfigurationException, IOException, SAXException {
        doc = builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
        this.clean(doc, "/");
    }

    public NodeList getNodes(String xPath) throws XPathExpressionException {
        XPath path = XPathFactory.newInstance().newXPath();
        return (NodeList) path.compile(xPath).evaluate(doc, XPathConstants.NODESET);
    }

    private void clean(Node node, String base) {
        NodeList children = node.getChildNodes();
        for (int i = children.getLength() - 1; i >= 0; i--) {
            Node child = children.item(i);
            short nodeType = child.getNodeType();
            if (nodeType == Node.ELEMENT_NODE) {
                String path = String.format("%s/%s", base, child.getNodeName());
                clean(child, path);
            } else if (nodeType == Node.TEXT_NODE) {
                String trimmedNodeVal = child.getNodeValue().trim();
                if (trimmedNodeVal.length() == 0) {
                    node.removeChild(child);
                } else {
                    child.setNodeValue(trimmedNodeVal);
                    paths.put(base, child.getNodeValue());
                }
            } else if (nodeType == Node.COMMENT_NODE) {
                node.removeChild(child);
            }
        }
    }

    public Map<String, String> getPaths() {
        return paths;
    }
}
