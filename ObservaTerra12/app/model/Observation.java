package model;
public class Observation {
	
	private Long idObservation = 0L;	

	private Area area = null;
	private Indicator indicator = null;
	private Measure measure = null;
	private Time time = null;
	private Provider provider = null;
	private Submission submission = null;

	public Long getIdObservation() {
		return idObservation;
	}

	public void setIdObservation(Long idObservation) {
		this.idObservation = idObservation;
	}

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((idObservation == null) ? 0 : idObservation.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Observation other = (Observation) obj;
		if (idObservation == null) {
			if (other.idObservation != null)
				return false;
		} else if (!idObservation.equals(other.idObservation))
			return false;
		return true;
	}
}