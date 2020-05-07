package webviewselenium.xml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import webviewselenium.constans.ConstantBookInfoSize;

public class ScannedBooksInfoImpl implements ScannedBooksInfo {

	private String rootElementName = "ScanInfo";
	private static int index = 0;
	Map<String, Map<String, String>> allBooksInfo = new LinkedHashMap<>();

	ConstantBookInfoSize bookInfoSize = new ConstantBookInfoSize();

	@Override
	public Map<String, String> readAllBooksInfo(String pathToXmlFile) {
		try {
			File xmlFile = new File(pathToXmlFile);
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(xmlFile);
			document.getDocumentElement().normalize();

			NodeList nodeList = document.getElementsByTagName(rootElementName);

			Map<String, String> allScannedBooks = new LinkedHashMap<>();

			for (int i = 0; i < nodeList.getLength(); i++) {
				Node theNode = nodeList.item(i);

				if (theNode.getNodeType() == Node.ELEMENT_NODE) {
					Element theElement = (Element) theNode;

					String title = theElement.getElementsByTagName("Title").item(0).getTextContent();
					String commit = theElement.getElementsByTagName("Commit").item(0).getTextContent();
					String browser = "Chrome";
					String branch = theElement.getElementsByTagName("Branch").item(0).getTextContent();
					String server = theElement.getElementsByTagName("Server").item(0).getTextContent();
					String data = theElement.getElementsByTagName("Date").item(0).getTextContent();
					String avgTime = theElement.getElementsByTagName("AverageTime").item(0).getTextContent();
					String pathToImage = theElement.getElementsByTagName("Path").item(0).getTextContent();
					//allScannedBooks.put("title", theElement.getElementsByTagName("Title").item(0).getTextContent());
					//allScannedBooks.put("commit", theElement.getElementsByTagName("Commit").item(0).getTextContent());
					//allScannedBooks.put("path", theElement.getElementsByTagName("Path").item(0).getTextContent());
					//System.out.println("allScannedBooks: " + allScannedBooks);
					String result
							= String.format("%" + bookInfoSize.getTitleNameSize() + "s", title)
							+ String.format("%" + bookInfoSize.getCommitNameSize() + "s", commit)
							+ String.format("%" + bookInfoSize.getBrowserNameSize() + "s", browser)
							+ String.format("%" + bookInfoSize.getBranchNameSize() + "s", branch)
							+ String.format("%" + bookInfoSize.getServerNameSize() + "s", server)
							+ String.format("%" + bookInfoSize.getDataSize() + "s", data.substring(8, data.length()))
							+ String.format("%" + bookInfoSize.getAverageTimeSize() + "s", avgTime)
							+ String.format("%" + bookInfoSize.getPathSize() + "s", pathToImage);
					String indexValue = Integer.toString(index);
					allScannedBooks.put(theElement.getElementsByTagName("Title").item(0).getTextContent() + indexValue, result);
					/*BookProperties bp = new BookProperties(
							theElement.getElementsByTagName(ConstantXMLScanDBScanValues.bookName).item(0).getTextContent().trim(),
							theElement.getElementsByTagName(ConstantXMLScanDBScanValues.commitName).item(0).getTextContent().trim(),
							"Chrome",
							theElement.getElementsByTagName(ConstantXMLScanDBScanValues.branchName).item(0).getTextContent().trim(),
							theElement.getElementsByTagName(ConstantXMLScanDBScanValues.serverName).item(0).getTextContent().trim(),
							theElement.getElementsByTagName(ConstantXMLScanDBScanValues.dateName).item(0).getTextContent().trim(),
							theElement.getElementsByTagName(ConstantXMLScanDBScanValues.averageTime).item(0).getTextContent().trim(),
							theElement.getElementsByTagName(ConstantXMLScanDBScanValues.pathName).item(0).getTextContent().trim()
					);*/
					//allScannedBooks.put(theElement.getElementsByTagName("Title").item(0).getTextContent() + indexValue, bp.getPrettyName());
					index++;
				}
			}
			return allScannedBooks;

		} catch (IOException | NumberFormatException | ParserConfigurationException | DOMException | SAXException ex) {
			Logger.getLogger(ScannedBooksInfoImpl.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	public List<String> readMatchedBooksInfo(String pathToXmlFile, String bookName, String localSourceName, String sourceName) {
		try {
			File xmlFile = new File(pathToXmlFile);
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(xmlFile);
			document.getDocumentElement().normalize();

			// the name of the root element
			NodeList nodeList = document.getElementsByTagName("ScanInfo");

			List<String> books = new ArrayList<>();

			for (int i = 0; i < nodeList.getLength(); i++) {
				Node theNode = nodeList.item(i);

				if (theNode.getNodeType() == Node.ELEMENT_NODE) {
					Element theElement = (Element) theNode;

					if (theElement.getElementsByTagName("Title").item(0).getTextContent().trim().equals(bookName.trim())
							&& !theElement.getElementsByTagName("Path").item(0).getTextContent().trim().equals(localSourceName.trim())) {
						String title = theElement.getElementsByTagName("Title").item(0).getTextContent();
						String commit = theElement.getElementsByTagName("Commit").item(0).getTextContent();
						String browser = "Chrome";
						String branch = theElement.getElementsByTagName("Branch").item(0).getTextContent();
						String server = theElement.getElementsByTagName("Server").item(0).getTextContent();
						String data = theElement.getElementsByTagName("Date").item(0).getTextContent();
						String avgTime = theElement.getElementsByTagName("AverageTime").item(0).getTextContent();
						String pathToImage = theElement.getElementsByTagName("Path").item(0).getTextContent();

						String result
								= String.format("%" + bookInfoSize.getTitleNameSize() + "s", title)
								+ String.format("%" + bookInfoSize.getCommitNameSize() + "s", commit)
								+ String.format("%" + bookInfoSize.getBrowserNameSize() + "s", browser)
								+ String.format("%" + bookInfoSize.getBranchNameSize() + "s", branch)
								+ String.format("%" + bookInfoSize.getServerNameSize() + "s", server)
								+ String.format("%" + bookInfoSize.getDataSize() + "s", data)
								+ String.format("%" + bookInfoSize.getAverageTimeSize() + "s", avgTime)
								+ String.format("%" + bookInfoSize.getPathSize() + "s", pathToImage);

						/*BookProperties bp = new BookProperties(
								theElement.getElementsByTagName(ConstantXMLScanDBScanValues.bookName).item(0).getTextContent().trim(),
								theElement.getElementsByTagName(ConstantXMLScanDBScanValues.commitName).item(0).getTextContent().trim(),
								"Chrome",
								theElement.getElementsByTagName(ConstantXMLScanDBScanValues.branchName).item(0).getTextContent().trim(),
								theElement.getElementsByTagName(ConstantXMLScanDBScanValues.serverName).item(0).getTextContent().trim(),
								theElement.getElementsByTagName(ConstantXMLScanDBScanValues.dateName).item(0).getTextContent().trim(),
								theElement.getElementsByTagName(ConstantXMLScanDBScanValues.averageTime).item(0).getTextContent().trim(),
								theElement.getElementsByTagName(ConstantXMLScanDBScanValues.pathName).item(0).getTextContent().trim()
						);

						System.out.println(bp.getPrettyName());*/
						//books.add(bp.getPrettyName());
						books.add(result);
					}
				}
			}
			return books;

		} catch (IOException | NumberFormatException | ParserConfigurationException | DOMException | SAXException ex) {
			Logger.getLogger(ScannedBooksInfoImpl.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}
}