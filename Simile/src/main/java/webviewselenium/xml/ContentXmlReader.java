package webviewselenium.xml;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * Class contains methods that allows to read from the XML files.
 */
public class ContentXmlReader {
    private String path;
    private String nodeName;

    public ContentXmlReader(String path, String nodeName) {
        this.path = path;
        this.nodeName = nodeName;
    }

    public NodeList readXmlContent() {
        try {
            return getXmlNodeList();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return null;
    }

    private NodeList getXmlNodeList() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(new File(path));
        document.getDocumentElement().normalize();

        return document.getElementsByTagName(nodeName);
    }
}
