package parser;

public interface Parser {

	void setUrl(String url);

	void setTags(String countryTag, String indicatorTag, String measureTag,
			String timeTag, String providerTag, String submissionTag);

	void execute();
}
