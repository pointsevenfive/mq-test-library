package com.pointsevenfive.esb.test;

import com.pointsevenfive.esb.util.ResourceHelper;
import com.pointsevenfive.esb.util.Validation;
import org.junit.Test;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ValidationTest {

    private String xml = ResourceHelper.getResource("msgs/demo.xml");

    @Test
    public void xpathValidationTest() throws IOException, SAXException, ParserConfigurationException, XPathExpressionException {
        Validation validation = new Validation(xml);
        NodeList nodes = validation.getNodes("//root/message/address/number");
        assertThat(nodes.item(0).getFirstChild().getNodeValue(), is("10"));
    }

    @Test
    public void printXpaths() throws IOException, SAXException, ParserConfigurationException {
        Validation validation = new Validation(xml);
        Map<String, String> paths = validation.getPaths();
        assertThat(paths.get("//root/message/address/number"), is("10"));
    }
}
