package com.systematictesting.daolayer.entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Catalog")
public class Catalog {

	public interface FIELDS {
		String ID = "id";
		String EMAIL = "email";
		String VERSION_NUMBER = "versionNumber";
		String VERSION_NAME_PREFIX = "C";
		String SITE_NAME = "siteName";
		String CATALOG_SUITES = "catalogSuites";
		String STATUS = "status";
		String LAST_MODIFIED_TIME = "lastmodifiedtime";
		String CREATE_TIME = "createtime";
	}
	
	@Id
	private String id;
	
	@Indexed
	private String email;
	@Indexed
	private String siteName;
	@Indexed
	private Long versionNumber;
	
	private List<SingleTestSuite> catalogSuites = new ArrayList<SingleTestSuite>();
	private String status;
	private Long lastmodifiedtime;
	private Long createtime;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public Long getVersionNumber() {
		return versionNumber;
	}
	public void setVersionNumber(Long versionNumber) {
		this.versionNumber = versionNumber;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Long getLastmodifiedtime() {
		return lastmodifiedtime;
	}
	public void setLastmodifiedtime(Long lastmodifiedtime) {
		this.lastmodifiedtime = lastmodifiedtime;
	}
	public Long getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Long createtime) {
		this.createtime = createtime;
	}
	public List<SingleTestSuite> getCatalogSuites() {
		return catalogSuites;
	}
	public void setCatalogSuites(List<SingleTestSuite> catalogSuites) {
		this.catalogSuites = catalogSuites;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((catalogSuites == null) ? 0 : catalogSuites.hashCode());
		result = prime * result + ((createtime == null) ? 0 : createtime.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((lastmodifiedtime == null) ? 0 : lastmodifiedtime.hashCode());
		result = prime * result + ((siteName == null) ? 0 : siteName.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((versionNumber == null) ? 0 : versionNumber.hashCode());
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
		Catalog other = (Catalog) obj;
		if (catalogSuites == null) {
			if (other.catalogSuites != null)
				return false;
		} else if (!catalogSuites.equals(other.catalogSuites))
			return false;
		if (createtime == null) {
			if (other.createtime != null)
				return false;
		} else if (!createtime.equals(other.createtime))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (lastmodifiedtime == null) {
			if (other.lastmodifiedtime != null)
				return false;
		} else if (!lastmodifiedtime.equals(other.lastmodifiedtime))
			return false;
		if (siteName == null) {
			if (other.siteName != null)
				return false;
		} else if (!siteName.equals(other.siteName))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (versionNumber == null) {
			if (other.versionNumber != null)
				return false;
		} else if (!versionNumber.equals(other.versionNumber))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Catalog [id=" + id + ", email=" + email + ", siteName=" + siteName + ", versionNumber=" + versionNumber + ", catalogSuites=" + catalogSuites + ", status=" + status + ", lastmodifiedtime=" + lastmodifiedtime + ", createtime=" + createtime + "]";
	}
}
