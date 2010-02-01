package model;

import java.io.Serializable;
import java.util.Date;

public class Metadata  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -9223001260141880240L;
	private String author;
	private String dateCreated;
	private String lastChangeDate;
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

	public String getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getLastChangeDate() {
		return lastChangeDate;
	}

	public void setLastChangeDate(String lastChangeDate) {
		this.lastChangeDate = lastChangeDate;
	}
	
	
	
}
