package parser;

import java.io.File;
import java.util.List;

import model.Indicator;
import model.Observation;
import model.Provider;
import model.Submission;

public interface Parser {

	void setFile(File file);

	void setTags(String countryTag, String indicatorTag, String measureTag,
			String timeTag);

	List<Observation> getParsedObservations();

	void setIndicator(Indicator indicator);

	void setKeySearch(String keySearch);

	void setProvider(Provider provider);

	void setSubmission(Submission submission);
}
