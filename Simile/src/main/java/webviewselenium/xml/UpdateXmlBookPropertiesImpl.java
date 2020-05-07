package webviewselenium.xml;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import webviewselenium.database.BookDatabaseValues;

public class UpdateXmlBookPropertiesImpl implements UpdateXmlBookProperties {

	@Override
	public void updateAverageTimeValue(String pathToXmlFile, int measuredTime, int currentValue, String bookName, String commitName) {

		int updatedValue = (int) ((currentValue + measuredTime) / 2);
		
		try {
			File xmlFile = new File(pathToXmlFile);
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(xmlFile);
			document.getDocumentElement().normalize();

			// the name of the root element
			NodeList nodeList = document.getElementsByTagName("ScanInfo");

			for (int i = 0; i < nodeList.getLength(); i++) {
				Node theNode = nodeList.item(i);

				if (theNode.getNodeType() == Node.ELEMENT_NODE) {
					Element theElement = (Element) theNode;

					if (theElement.getElementsByTagName(BookDatabaseValues.bookName).item(0).getTextContent().trim().equals(bookName.trim())) {
							//&& !theElement.getElementsByTagName("Commit").item(0).getTextContent().equals(ConstantXMLScanValues.commitName)) {
						theElement.getElementsByTagName(BookDatabaseValues.averageTime).item(0).setTextContent(Integer.toString(updatedValue));
					}
				}
			}
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(pathToXmlFile);
			transformer.transform(source, result);
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

	}

}
