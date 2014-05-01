package parser;

import java.io.File;
import java.util.List;

import model.Indicator;
import model.Observation;
import model.Provider;
import model.Submission;

public abstract class AbstractParser implements Parser {

	File file;
	String keySearch;
	Provider provider;
	Submission submission;
	Indicator indicator;
	List<Observation> observations;
	String countryTag;
	String indicatorTag;
	String measureTag;
	String timeTag;

	@Override
	public void setKeySearch(String keySearch) {
		this.keySearch = keySearch;
	}

	@Override
	public void setProvider(Provider provider) {
		this.provider = provider;
	}

	@Override
	public void setSubmission(Submission submission) {
		this.submission = submission;
	}

	@Override
	public void setIndicator(Indicator indicator) {
		this.indicator = indicator;
	}

	@Override
	public void setFile(File file) {
		this.file = file;
	}

	@Override
	public void setTags(String countryTag, String indicatorTag,
			String measureTag, String timeTag, String providerTag,
			String submissionTag) {
		this.countryTag = countryTag;
		this.indicatorTag = indicatorTag;
		this.measureTag = measureTag;
		this.timeTag = timeTag;
	}

}
