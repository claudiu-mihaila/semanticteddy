package model;

import java.util.Date;

public class Metadata {
	private String author;
	private Date created;
	//pentru inceput...sa nu folosim ultimele doua proprietati..
	private Date lastChange;
	private String lastChangeBy;
	
	public Metadata(){}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getLastChange() {
		return lastChange;
	}

	public void setLastChange(Date lastChange) {
		this.lastChange = lastChange;
	}

	public String getLastChangeBy() {
		return lastChangeBy;
	}

	public void setLastChangeBy(String lastChangeBy) {
		this.lastChangeBy = lastChangeBy;
	}
	
	
}
