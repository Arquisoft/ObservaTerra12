package model;

public class Measure {

	private String value;
	private String unit;
	private Long idMedida;

	public String getValue() {
		return value;
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

	public Long getIdMedida() {
		return idMedida;
	}

	public void setIdMedida(Long idMedida) {
		this.idMedida = idMedida;
	}

}