package webviewselenium.constans;

import java.util.ArrayList;
import java.util.List;

/**
 * Class contains all information about available supported servers.
 *
 * Please note that if you add new supported server you should:
 * 	1. Add new server as a private static property.
 * 	2. Add getter method to the created property.
 * 	3. Add the created property to the getAvailableServers() method as additional field in the list.
 *
 */
public class ConstantServerNames {
	private static List<String> availableServers = new ArrayList<>();
	private static String serverKatalyst = "https://katalyst01.cnx.org/";
	private static String serverStagingCNX = "https://staging.cnx.org/";
	private static String serverStagingOpenStax = "https://staging.openstax.org/";
	private static String serverOpenStax = "https://openstax.org/";
	private static String serverRelease53 = "https://release-53.sandbox.openstax.org/";
	private static String serverRexWebHeroku = "https://rex-web.herokuapp.com/";
	private static String serverRexWebStagingHeroku = "https://rex-web-staging.herokuapp.com/";
	private static String serverRexWebIssue947 = "https://rex-web-issue-947-np39vpknfjij.herokuapp.com/";
	private static String sourcePdf = "PDF";

	/**
	 * @return list of all available supported servers
	 */
	public static List<String> getAvailableServers() {
		availableServers.clear();
		availableServers.add(serverKatalyst);
		availableServers.add(serverStagingCNX);
		availableServers.add(serverOpenStax);
		availableServers.add(serverStagingOpenStax);
		availableServers.add(serverRelease53);
		availableServers.add(serverRexWebHeroku);
		availableServers.add(serverRexWebStagingHeroku);
		availableServers.add(serverRexWebIssue947);
		availableServers.add(sourcePdf);
		return availableServers;
	}
}
