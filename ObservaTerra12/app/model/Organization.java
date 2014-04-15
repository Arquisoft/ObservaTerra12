package model;

import java.util.List;

public class Organization {

	private String nombre;
	private Long idOrganizacion;
	private Country country;
	private String tipoOrganizacion;
	private List<Organization> organizations;

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Long getIdOrganizacion() {
		return idOrganizacion;
	}

	public void setIdOrganizacion(Long idOrganizacion) {
		this.idOrganizacion = idOrganizacion;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public List<Organization> getOrganizations() {
		return organizations;
	}

	public void setOrganizations(List<Organization> organizations) {
		this.organizations = organizations;
	}

	public String getTipoOrganizacion() {
		return tipoOrganizacion;
	}

	public void setTipoOrganizacion(String tipoOrganizacion) {
		this.tipoOrganizacion = tipoOrganizacion;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((idOrganizacion == null) ? 0 : idOrganizacion.hashCode());
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		result = prime
				* result
				+ ((tipoOrganizacion == null) ? 0 : tipoOrganizacion.hashCode());
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
		Organization other = (Organization) obj;
		if (idOrganizacion == null) {
			if (other.idOrganizacion != null)
				return false;
		} else if (!idOrganizacion.equals(other.idOrganizacion))
			return false;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		if (tipoOrganizacion == null) {
			if (other.tipoOrganizacion != null)
				return false;
		} else if (!tipoOrganizacion.equals(other.tipoOrganizacion))
			return false;
		return true;
	}

	
}