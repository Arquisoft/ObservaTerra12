package model;

import java.util.List;

public class Organization {

	// No se puede inicializar,
	private Long idOrganization = null;

	private String nombre = "";
	private Country country = null;
	private String tipoOrganizacion = null;
	private List<Organization> organizations = null;

	public Long getIdOrganization() {
		return idOrganization;
	}

	@Override
	public String toString() {
		return "Organization [idOrganization=" + idOrganization + ", nombre="
				+ nombre + ", country=" + country + ", tipoOrganizacion="
				+ tipoOrganizacion + "]";
	}

	public Organization(String nombre, Country country, String tipoOrganizacion) {
		super();
		this.nombre = nombre;
		this.country = country;
		this.tipoOrganizacion = tipoOrganizacion;
	}

	public Organization() {
		super();
	}

	public void setIdOrganization(Long idOrganization) {
		this.idOrganization = idOrganization;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
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
				+ ((idOrganization == null) ? 0 : idOrganization.hashCode());
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
		if (idOrganization == null) {
			if (other.idOrganization != null)
				return false;
		} else if (!idOrganization.equals(other.idOrganization))
			return false;
		return true;
	}
}