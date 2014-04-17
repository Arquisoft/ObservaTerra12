package model;

import java.util.Date;

public class Time {

	private Date startDate;
	private Date endDate;
	private Long idTiempo;

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Long getIdTiempo() {
		return idTiempo;
	}

	public void setIdTiempo(Long idTiempo) {
		this.idTiempo = idTiempo;
	}

}