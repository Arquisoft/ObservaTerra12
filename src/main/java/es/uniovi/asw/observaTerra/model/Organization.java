package es.uniovi.asw.observaTerra.model;
import java.util.List;

public class Organization {
	
	private String name;
	private String cifc;
	private Country country;
	private List<Organization> organizations;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getCifc() {
		return cifc;
	}

	public void setCifc(String cifc) {
		this.cifc = cifc;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public void addArea(Organization organization) {
		organizations.add(organization);
	}

	public void removeArea(Organization organization) {
		organizations.remove(organization);
	}
}