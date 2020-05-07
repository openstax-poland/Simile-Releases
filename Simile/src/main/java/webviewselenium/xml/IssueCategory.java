package webviewselenium.xml;

public interface IssueCategory {
	
	/**
	 * Returns relative path to XML file of desired category.
	 * 
	 * @param pathToReportDirectory path to report directory
	 * @param categoryName name of the desired category
	 * @return relative path to desired category directory
	 */
	public String getPathOfDesiredCategoryDirectory(String pathToReportDirectory, String categoryName);
	
	/**
	 * Returns relative path to XML file of desired category.
	 * 
	 * @param pathToReportDirectory path to report directory
	 * @param categoryName name of the desired category
	 * @return relative path to XML file of desired category
	 */
	public String getXmlPathOfDesiredCategory(String pathToReportDirectory, String categoryName);
	
	/**
	 * Creates XML for issue category. 
	 * 
	 * @param pathToCategoryXml path to XML file of desired category
	 * @param categoryName name of the desired category
	 * @param categoryDescription description of the desired category
	 */
	public void createIssueCategory(String pathToCategoryXml, String categoryName, String categoryDescription);

}
