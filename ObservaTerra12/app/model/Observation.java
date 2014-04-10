package model;
public class Observation {

	private Area area;
	private Indicator indicator;
	private Measure measure;
	private Time time;
	private Provider provider;
	private Submission submission;

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public Indicator getIndicator() {
		return indicator;
	}

	public void setIndicator(Indicator indicator) {
		this.indicator = indicator;
	}

	public Measure getMeasure() {
		return measure;
	}

	public void setMeasure(Measure measure) {
		this.measure = measure;
	}

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	public Provider getProvider() {
		return provider;
	}

	public void setProvider(Provider provider) {
		this.provider = provider;
	}

	public Submission getSubmission() {
		return submission;
	}

	public void setSubmission(Submission submission) {
		this.submission = submission;
	}
}