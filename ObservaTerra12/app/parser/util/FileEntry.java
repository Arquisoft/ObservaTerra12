package parser.util;

public class FileEntry {

	private String url;
	private String formatTag;
	private String areaTag;
	private String indicatorTag;
	private String measureTag;
	private String timeTag;
	private String providerTag;
	private String submissionTag;
	
	public FileEntry(String url, String formatTag, String areaTag,
			String indicatorTag, String measureTag, String timeTag,
			String providerTag, String submissionTag) {
		this.url = url;
		this.formatTag = formatTag;
		this.areaTag = areaTag;
		this.indicatorTag = indicatorTag;
		this.measureTag = measureTag;
		this.timeTag = timeTag;
		this.providerTag = providerTag;
		this.submissionTag = submissionTag;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getFormatTag() {
		return formatTag;
	}

	public void setFormatTag(String formatTag) {
		this.formatTag = formatTag;
	}

	public String getAreaTag() {
		return areaTag;
	}

	public void setAreaTag(String areaTag) {
		this.areaTag = areaTag;
	}

	public String getIndicatorTag() {
		return indicatorTag;
	}

	public void setIndicatorTag(String indicatorTag) {
		this.indicatorTag = indicatorTag;
	}

	public String getMeasureTag() {
		return measureTag;
	}

	public void setMeasureTag(String measureTag) {
		this.measureTag = measureTag;
	}

	public String getTimeTag() {
		return timeTag;
	}

	public void setTimeTag(String timeTag) {
		this.timeTag = timeTag;
	}

	public String getProviderTag() {
		return providerTag;
	}

	public void setProviderTag(String providerTag) {
		this.providerTag = providerTag;
	}

	public String getSubmissionTag() {
		return submissionTag;
	}

	public void setSubmissionTag(String submissionTag) {
		this.submissionTag = submissionTag;
	}
	
}
