package model;

public class Measure {

	private Long idMeasure = null;

	private String value = "";
	private String unit = "";

	public Long getIdMeasure() {
		return idMeasure;
	}

	public void setIdMeasure(Long idMeasure) {
		this.idMeasure = idMeasure;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "Measure [idMeasure=" + idMeasure + ", value=" + value
				+ ", unit=" + unit + "]";
	}

	public Measure(String value, String unit) {
		super();
		this.value = value;
		this.unit = unit;
	}

	public Measure() {
		super();
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((idMeasure == null) ? 0 : idMeasure.hashCode());
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
		Measure other = (Measure) obj;
		if (idMeasure == null) {
			if (other.idMeasure != null)
				return false;
		} else if (!idMeasure.equals(other.idMeasure))
			return false;
		return true;
	}
}