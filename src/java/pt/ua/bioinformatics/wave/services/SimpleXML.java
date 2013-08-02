package pt.ua.bioinformatics.wave.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author pedrolopes
 * @version 1.2, 2010-12-17
 */
public class SimpleXML {

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder;
    Document doc;

    public SimpleXML(String file) {
        try {
            factory.setNamespaceAware(false);
            builder = factory.newDocumentBuilder();
            doc = (Document) builder.parse(file);
        } catch (SAXException ex) {
            Logger.getLogger(SimpleXML.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SimpleXML.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(SimpleXML.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public SimpleXML(InputStream is) {
        try {
            factory.setNamespaceAware(false);
            builder = factory.newDocumentBuilder();
            doc = builder.parse(is);
        } catch (SAXException ex) {
            //Logger.getLogger(SimpleXML.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("[SimpleXML] unable to load XML\n\t" + ex.toString());
        } catch (IOException ex) {
            //Logger.getLogger(SimpleXML.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("[SimpleXML] unable to load XML\n\t" + ex.toString());
        } catch (ParserConfigurationException ex) {
            //Logger.getLogger(SimpleXML.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("[SimpleXML] unable to load XML\n\t" + ex.toString());
        }
    }

    public String selectNode(String xquery) throws XPathExpressionException {
        XPathFactory xp_factory = XPathFactory.newInstance();
        XPath xpath;
        xpath = xp_factory.newXPath();

        XPathExpression expr = xpath.compile(xquery);

        Object result = expr.evaluate(doc, XPathConstants.NODESET);

        NodeList nodes = (NodeList) result;
        Node v = nodes.item(0).getFirstChild();

        return v.getNodeValue();
    }

    public NodeList selectNodes(String xquery) throws XPathExpressionException {
        XPathFactory xp_factory = XPathFactory.newInstance();
        XPath xpath;
        xpath = xp_factory.newXPath();

        XPathExpression expr = xpath.compile(xquery);

        return (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
    }
}
