package parser;

public abstract class AbstractParser implements Parser {

	private String url;
	private String countryTag;
	private String indicatorTag;
	private String measureTag;
	private String timeTag;
	private String providerTag;
	private String submissionTag;
	
	@Override
	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public void setTags(String countryTag, String indicatorTag,
			String measureTag, String timeTag, String providerTag,
			String submissionTag) {
		this.countryTag = countryTag;
		this.indicatorTag = indicatorTag;
		this.measureTag = measureTag;
		this.timeTag = timeTag;
		this.providerTag = providerTag;
		this.submissionTag = submissionTag;
	}

}
