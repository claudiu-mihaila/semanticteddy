package model;

import java.util.Date;

public class Metadata {
	private String author;
	private Date dateCreated;
	private Date lastChangeDate;
	private String lastChangeBy;
	
	public Metadata(){}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getLastChangeBy() {
		return lastChangeBy;
	}

	public void setLastChangeBy(String lastChangeBy) {
		this.lastChangeBy = lastChangeBy;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getLastChangeDate() {
		return lastChangeDate;
	}

	public void setLastChangeDate(Date lastChangeDate) {
		this.lastChangeDate = lastChangeDate;
	}
	
}
