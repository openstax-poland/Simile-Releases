package webviewselenium.xml;

public interface UpdateXmlBookProperties {

	/**
	 * This method allows to update value of average scan time.
	 * 
	 * @param pathToXmlFile pathToXMLFile relative path to XML file
	 * @param measuredTime value of the latest scan time
	 * @param currentValue value of the average scan time
     * @param bookName title of the scanned book
     * @param commitName commit name of the scanned book
	 */
	public void updateAverageTimeValue(String pathToXmlFile, int measuredTime, int currentValue, String bookName, String commitName);
	
}
