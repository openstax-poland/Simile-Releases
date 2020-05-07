package webviewselenium.xml;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import webviewselenium.constans.SharedConstants;
import webviewselenium.database.BookDatabaseValues;

public class IssueCategoryImpl implements IssueCategory {

	@Override
	public String getPathOfDesiredCategoryDirectory(String pathToReportDirectory, String categoryName) {
		return pathToReportDirectory + File.separator + categoryName + File.separator;
	}

	@Override
	public String getXmlPathOfDesiredCategory(String pathToReportDirectory, String categoryName) {
		return getPathOfDesiredCategoryDirectory(pathToReportDirectory, categoryName)
				+ SharedConstants.nameOfCategoryInfoXmlFile + ".xml";
	}

	@Override
	public void createIssueCategory(String pathToCategoryXml, String categoryName, String categoryDescription) {
		try {
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            
            Document document = documentBuilder.newDocument();
            Element rootElement = document.createElement(BookDatabaseValues.categoryRootElement);
            document.appendChild(rootElement);
            
            Element name = document.createElement(BookDatabaseValues.categoryName);
            name.appendChild(document.createTextNode(categoryName));
            rootElement.appendChild(name);
            
            Element description = document.createElement(BookDatabaseValues.categoryDescription);
            description.appendChild(document.createTextNode(categoryDescription));
            rootElement.appendChild(description);
            
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(pathToCategoryXml);
            
            transformer.transform(source, result);
		} catch (ParserConfigurationException exc) {
            exc.printStackTrace();
        } catch (TransformerConfigurationException exc) {
            exc.printStackTrace();
        } catch (TransformerException exc) {
            exc.printStackTrace();
        }  
	}

}
