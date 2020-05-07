package webviewselenium.bookProperties;

/**
 * This class indicates which properties each book must have.
 */
public class BookProperties {
	private String bookTitle;
	private String commitName;
	private String browserName;
	private String branchName;
	private String serverName;
	private String date;
	private String avgTime;
	private String pathToImage;
	private String numberOfPages;

	public static final class Builder {
		private String bookTitle;
		private String commitName;
		private String browserName = "Chrome";
		private String branchName;
		private String serverName;
		private String date = "0.0.0 01.01.2000";
		private String avgTime = "0";
		private String pathToImage;
		private String numberOfPages = "0";

		public Builder bookTitle(String bookTitle) {
			this.bookTitle = bookTitle;
			return this;
		}

		public Builder commitName(String commitName) {
			this.commitName = commitName;
			return this;
		}

		public Builder branchName(String branchName) {
			this.branchName = branchName;
			return this;
		}

		public Builder serverName(String serverName) {
			this.serverName = serverName;
			return this;
		}

		public Builder date(String date) {
			this.date = date;
			return this;
		}

		public Builder avgTime(String avgTime) {
			this.avgTime = avgTime;
			return this;
		}

		public Builder pathToImage(String pathToImage) {
			this.pathToImage = pathToImage;
			return this;
		}

		public Builder numberOfPages(String numberOfPages) {
			this.numberOfPages = numberOfPages;
			return this;
		}

		public BookProperties build() {
			if (bookTitle.isEmpty())
				throw new IllegalStateException("bookTitle cannot be empty");
			if (serverName.isEmpty())
				throw new IllegalStateException("serverName cannot be empty");
			if (pathToImage.isEmpty())
				throw new IllegalStateException("pathToImage cannot be empty!");

			BookProperties bookProperties = new BookProperties();
			bookProperties.bookTitle = this.bookTitle;
			bookProperties.commitName = this.commitName;
			bookProperties.browserName = this.browserName;
			bookProperties.branchName = this.branchName;
			bookProperties.serverName = this.serverName;
			bookProperties.date = this.date;
			bookProperties.avgTime = this.avgTime;
			bookProperties.pathToImage = this.pathToImage;
			bookProperties.numberOfPages = this.numberOfPages;

			return bookProperties;
		}
	}

	public String getBookTitle() {
		return bookTitle;
	}

	public String getCommitName() {
		return commitName;
	}

	public String getBrowserName() {
		return browserName;
	}

	public String getBranchName() {
		return branchName;
	}

	public String getServerName() {
		return serverName;
	}

	public String getDate() {
		return date;
	}

	public String getAvgTime() {
		return avgTime;
	}

	public String getPathToImage() {
		return pathToImage;
	}

	public String getNumberOfPages() {
		return numberOfPages;
	}
}
