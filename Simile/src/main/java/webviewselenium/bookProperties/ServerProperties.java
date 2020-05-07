package webviewselenium.bookProperties;

import webviewselenium.database.BookDatabaseValues;

public class ServerProperties {
	private String serverName;
	private String serverUrl;
	private String serverXmlElementName;

	public ServerProperties(String serverName) {
		this.serverName = serverName;
		this.serverUrl = BookDatabaseValues.getServerUrlByServerName(serverName);
		this.serverXmlElementName = BookDatabaseValues.getServerXmlElementNameByServerName(serverName);
	}

	public String getServerName() {
		return serverName;
	}

	public String getServerUrl() {
		return serverUrl;
	}

	public String getServerXmlElementName() {
		return serverXmlElementName;
	}
}
