package webviewselenium.bookScan;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import webviewselenium.constans.SharedConstants;

public class FolderManager {

    static int MaxNumberFromFolderNames;
    private static final String pathToMainScanDirectory = SharedConstants.nameOfBookScanDirectory;
    private static final String BooksAvailableForScan = SharedConstants.fullNameOfFileThatContainsBooksAvailableForScan;

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static String NewScanFolderSetup(String BookName, String BookID,String Pages, String Date, String Commit, String Branch,String Server, boolean createDirectory) throws URISyntaxException, ParserConfigurationException, TransformerConfigurationException, TransformerException {
    	
    	
        File folder = new File(pathToMainScanDirectory);
        File[] listOfFiles = folder.listFiles();
        List<Integer> FolderNumbers = new ArrayList<Integer>();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isDirectory()) {
                if (isNumeric(listOfFiles[i].getName())) {
                    FolderNumbers.add(Integer.parseInt(listOfFiles[i].getName()));
                }
            }
        }
        
    	if(createDirectory) {        
	        if (FolderNumbers.size() == 0) {
	            new File(pathToMainScanDirectory + File.separator + "1").mkdirs();
	            MaxNumberFromFolderNames = 1;
	        } else {
	
	            Collections.sort(FolderNumbers);
	
	            MaxNumberFromFolderNames = FolderNumbers.get(FolderNumbers.size() - 1);
	            MaxNumberFromFolderNames++;
	            new File(pathToMainScanDirectory + File.separator + MaxNumberFromFolderNames).mkdirs();
	        }
	        //XML INFO CREATION
	        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	
	        // root elements
	        Document doc = docBuilder.newDocument();
	        Element rootElement = doc.createElement("ScanInfo");
	        doc.appendChild(rootElement);
	
	        // staff elements
	        Element staff = doc.createElement("Book");
	        rootElement.appendChild(staff);
	
	        // set attribute to staff element
	        Attr attr = doc.createAttribute("Name");
	        attr.setValue(BookName);
	        staff.setAttributeNode(attr);
	
	        // shorten way
	        // staff.setAttribute("id", "1");
	        // firstname elements
	        Element title = doc.createElement("Title");
	        title.appendChild(doc.createTextNode(BookName));
	        staff.appendChild(title);
	
	        Element firstname = doc.createElement("ID");
	        firstname.appendChild(doc.createTextNode(BookID));
	        staff.appendChild(firstname);
	        
	        Element server = doc.createElement("Server");
	        server.appendChild(doc.createTextNode(Server));
	        staff.appendChild(server);
	        
	        Element pages = doc.createElement("Pages");
	        pages.appendChild(doc.createTextNode(Pages));
	        staff.appendChild(pages);
	
	        Element CommitElement = doc.createElement("Commit");
	        CommitElement.appendChild(doc.createTextNode(Commit));
	        staff.appendChild(CommitElement);
	
	        Element BranchElement = doc.createElement("Branch");
	        BranchElement.appendChild(doc.createTextNode(Branch));
	        staff.appendChild(BranchElement);
	
	        // lastname elements
	        Element lastname = doc.createElement("Date");
	        lastname.appendChild(doc.createTextNode(Date));
	        staff.appendChild(lastname);
	
	        Element averageTime = doc.createElement("AverageTime");
	        averageTime.appendChild(doc.createTextNode("0"));
	        staff.appendChild(averageTime);
	        
	        Element path = doc.createElement("Path");
	        path.appendChild(doc.createTextNode(pathToMainScanDirectory + File.separator + MaxNumberFromFolderNames));
	        staff.appendChild(path);
	
	        // write the content into xml file
	        TransformerFactory transformerFactory = TransformerFactory.newInstance();
	        Transformer transformer = transformerFactory.newTransformer();
	        DOMSource source = new DOMSource(doc);
	
	        if (listOfFiles.length == 0) {
	            int MaxNumberFromFolderNames = 1;
	            StreamResult result = new StreamResult(new File(pathToMainScanDirectory + File.separator + MaxNumberFromFolderNames + File.separator + "info.xml"));
	            transformer.transform(source, result);
	            String res = pathToMainScanDirectory + File.separator + MaxNumberFromFolderNames;
	            return res;
	        } else {
	            Collections.sort(FolderNumbers);
	
	            int MaxNumberFromFolderNames;
	            if(FolderNumbers.size() == 0) {
	            	MaxNumberFromFolderNames = 1;
	            } else {
	            	MaxNumberFromFolderNames = FolderNumbers.get(FolderNumbers.size() - 1) + 1;
	            }
	            
	            StreamResult result = new StreamResult(new File(pathToMainScanDirectory + File.separator + MaxNumberFromFolderNames + File.separator + "info.xml"));
	            transformer.transform(source, result);
	            String res = pathToMainScanDirectory + File.separator + MaxNumberFromFolderNames;
	            return res;
	        }
    	} else {
    		 Collections.sort(FolderNumbers);
    		 MaxNumberFromFolderNames = FolderNumbers.get(FolderNumbers.size() - 1) + 1;
    		 return pathToMainScanDirectory + File.separator + MaxNumberFromFolderNames;
    	}
    }
    public static void updateScanTime(long time,String bookName) throws SAXException{
             try {

            File fXmlFile = new File(BooksAvailableForScan);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = (Document) dBuilder.parse(fXmlFile);
            List<String> BookNames = new ArrayList<String>();
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("Book");

            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String BookName = eElement.getAttribute("Name");

                    if(BookName.equals(bookName)){
                    long timeget  = Long.parseLong(eElement.getElementsByTagName("AverageScanTime").item(0).getTextContent());
                    String tempp = String.valueOf((timeget+time)/2);
                    eElement.getElementsByTagName("AverageScanTime").item(0).setTextContent(tempp);
                 
                    }

                }
            }

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(BooksAvailableForScan));
            transformer.transform(source, result);

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
     public static long getScanTime(String bookName) throws SAXException, ParserConfigurationException, IOException{
         

            File fXmlFile = new File(BooksAvailableForScan);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = (Document) dBuilder.parse(fXmlFile);
            List<String> BookNames = new ArrayList<String>();
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("Book");

            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String BookName = eElement.getAttribute("Name");

                    if(BookName.equals(bookName)){
                    long timeget  = Long.parseLong(eElement.getElementsByTagName("FirstPageURL").item(0).getTextContent());
                    return timeget;
                 
                    }

                }
            }

       return 0;
    }
        
    }


