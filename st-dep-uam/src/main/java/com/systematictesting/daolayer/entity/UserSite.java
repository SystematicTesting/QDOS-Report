package com.systematictesting.daolayer.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "userSites")
public class UserSite {
	
	public interface FIELDS {
		String ID = "id";
		String EMAIL = "email";
		String SITE_NAME = "siteName";
		String DESCRIPTION = "description";
		String STATUS = "status";
		String SHARE_STATUS = "shareStatus";
		String LAST_MODIFIED_TIME = "lastmodifiedtime";
		String CREATE_TIME = "createtime";
	}
	
	@Id
	private String id;
	
	@Indexed
	private String email;
	@Indexed
	private String siteName;
	private String description;
	private String status;
	private String shareStatus;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getShareStatus() {
		return shareStatus;
	}
	public void setShareStatus(String shareStatus) {
		this.shareStatus = shareStatus;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((createtime == null) ? 0 : createtime.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((lastmodifiedtime == null) ? 0 : lastmodifiedtime.hashCode());
		result = prime * result + ((shareStatus == null) ? 0 : shareStatus.hashCode());
		result = prime * result + ((siteName == null) ? 0 : siteName.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
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
		UserSite other = (UserSite) obj;
		if (createtime == null) {
			if (other.createtime != null)
				return false;
		} else if (!createtime.equals(other.createtime))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
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
		if (shareStatus == null) {
			if (other.shareStatus != null)
				return false;
		} else if (!shareStatus.equals(other.shareStatus))
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
		return true;
	}
	@Override
	public String toString() {
		return "UserSite [id=" + id + ", email=" + email + ", siteName=" + siteName + ", description=" + description
				+ ", status=" + status + ", shareStatus=" + shareStatus + ", lastmodifiedtime=" + lastmodifiedtime
				+ ", createtime=" + createtime + "]";
	}
}
