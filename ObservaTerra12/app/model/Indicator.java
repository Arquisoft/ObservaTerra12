package model;

public class Indicator {

	private Long idIndicator = null;
	private String nombre = "";

	public Long getIdIndicator() {
		return idIndicator;
	}

	public void setIdIndicator(Long idIndicator) {
		this.idIndicator = idIndicator;
	}

	public String getNombre() {
		return nombre;
	}

	public Indicator(String nombre) {
		super();
		this.nombre = nombre;
	}

	@Override
	public String toString() {
		return "Indicator [idIndicator=" + idIndicator + ", nombre=" + nombre
				+ "]";
	}

	public Indicator() {

	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((idIndicator == null) ? 0 : idIndicator.hashCode());
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
		Indicator other = (Indicator) obj;
		if (idIndicator == null) {
			if (other.idIndicator != null)
				return false;
		} else if (!idIndicator.equals(other.idIndicator))
			return false;
		return true;
	}
}