package model;

import java.util.List;

public class Area {

	private Long idArea = 0L;
	
	private String name = "";
	private List<Area> areas = null;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addArea(Area area) {
		areas.add(area);
	}

	public void removeArea(Area area) {
		areas.remove(area);
	}

	public Long getIdArea() {
		return idArea;
	}

	public void setIdArea(Long idArea) {
		this.idArea = idArea;
	}

	public List<Area> getAreas() {
		return areas;
	}

	public void setAreas(List<Area> areas) {
		this.areas = areas;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idArea == null) ? 0 : idArea.hashCode());
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
		Area other = (Area) obj;
		if (idArea == null) {
			if (other.idArea != null)
				return false;
		} else if (!idArea.equals(other.idArea))
			return false;
		return true;
	}
}