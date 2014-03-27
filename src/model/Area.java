package model;
import java.util.List;

public class Area {
	
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
}