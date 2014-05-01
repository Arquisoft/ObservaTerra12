package model;

public class Country extends Area {

	public Country(String name) {
		super(name);

	}

	public Country() {
		super();
	}

	@Override
	public String toString() {
		return "Country [getName()=" + getName() + ", getIdArea()="
				+ getIdArea() + "]";
	}

}