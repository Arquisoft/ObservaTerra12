package model;
import java.util.Date;


public class Submission {
	
	private Long idSubmission = null;
	
	private Date date = null;
	private User user = null;
	
	public Long getIdSubmission() {
		return idSubmission;
	}

	public void setIdSubmission(Long idSubmission) {
		this.idSubmission = idSubmission;
	}

	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((idSubmission == null) ? 0 : idSubmission.hashCode());
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
		Submission other = (Submission) obj;
		if (idSubmission == null) {
			if (other.idSubmission != null)
				return false;
		} else if (!idSubmission.equals(other.idSubmission))
			return false;
		return true;
	}
}