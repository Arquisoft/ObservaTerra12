package model;

import java.util.List;

public class Area {

	private Long id_area;
	private String name;
	private List<Area> areas;

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

	public Long getId_area() {
		return id_area;
	}

	public void setId_area(Long id_area) {
		this.id_area = id_area;
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
		result = prime * result + ((id_area == null) ? 0 : id_area.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		if (id_area == null) {
			if (other.id_area != null)
				return false;
		} else if (!id_area.equals(other.id_area))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	
}