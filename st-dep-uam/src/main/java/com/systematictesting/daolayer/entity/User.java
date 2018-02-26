/**
 * Copyright (c) Mar 7, 2015 Sharad Tech Ltd. (www.sharadtech.com) to Present.
 * All rights reserved. 
 */
package com.systematictesting.daolayer.entity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.systematictesting.daolayer.beans.EmailSiteCombo;
import com.systematictesting.daolayer.beans.License;
import com.systematictesting.daolayer.constants.UserConstants;

@Document(collection = "users")
public class User {

	public interface FIELDS {
		String ID = "id";
		String EMAIL = "email";
		String PASSWORD = "password";
		String FIRST_NAME = "firstname";
		String LAST_NAME = "lastname";
		String ADDRESS = "address";
		String POSTCODE = "postcode";
		String COMPANY_NAME = "companyname";
		String TERMS_AND_CONDITIONS = "termsAndConditions";
		String CITY = "city";
		String COUNTRY = "country";
		String LAST_MODIFIED_TIME = "lastmodifiedtime";
		String CREATE_TIME = "createtime";
		String STATUS = "status";
		String DEFAULT_API_KEY = "defaultAPIkey";
		String ACTIVE_API_KEY = "activeAPIkey";
		String ALL_SAVED_API_KEYS = "allsavedAPIkeys";
		String EMAIL_SUBSCRIPTIONS = "emailSubscriptions";
		String SHARED_SUBSCRIBED_SITES = "sharedSubscribedSites";
		String LICENSE = "license";
	}
	
	@Id
	private String id;

	@Indexed(unique = true)
	private String email;
	private String password;
	private String firstname;
	private String lastname;
	private String address;
	private String postcode;
	private String companyname;
	private String termsAndConditions;
	private String city;
	private String country;

	private Long lastmodifiedtime;
	private Long createtime;
	private String status;
	private String defaultAPIkey;
	private String activeAPIkey;
	private License license;

	private Map<String, String> allsavedAPIkeys = new HashMap<String, String>();
	private Map<String, List<String>> emailSubscriptions = new HashMap<String, List<String>>();
	private List<EmailSiteCombo> sharedSubscribedSites = new ArrayList<EmailSiteCombo>();

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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getCompanyname() {
		return companyname;
	}

	public void setCompanyname(String companyname) {
		this.companyname = companyname;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDefaultAPIkey() {
		return defaultAPIkey;
	}

	public void setDefaultAPIkey(String defaultAPIkey) {
		this.defaultAPIkey = defaultAPIkey;
	}

	public String getActiveAPIkey() {
		return activeAPIkey;
	}

	public void setActiveAPIkey(String activeAPIkey) {
		this.activeAPIkey = activeAPIkey;
	}

	public String getTermsAndConditions() {
		return termsAndConditions;
	}

	public void setTermsAndConditions(String termsAndConditions) {
		this.termsAndConditions = termsAndConditions;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Map<String, String> getAllsavedAPIkeys() {
		return allsavedAPIkeys;
	}

	public String getAllsavedAPIkeysForCookie() {
		StringBuilder sb = new StringBuilder();
		Iterator<String> itr = allsavedAPIkeys.keySet().iterator();
		int index = 1;
		while (itr.hasNext()) {
			String key = itr.next();
			String value = allsavedAPIkeys.get(key);
			if (index == 1) {
				sb.append(key).append(UserConstants.KEY_EMAIL_SEPARATOR).append(value);
			} else {
				sb.append(UserConstants.API_KEY_SEPARATOR).append(key).append(UserConstants.KEY_EMAIL_SEPARATOR)
						.append(value);
			}
			index++;
		}
		return sb.toString();
	}

	public void setAllsavedAPIkeys(Map<String, String> allsavedAPIkeys) {
		this.allsavedAPIkeys = allsavedAPIkeys;
	}

	public Map<String, List<String>> getEmailSubscriptions() {
		return emailSubscriptions;
	}

	public void setEmailSubscriptions(Map<String, List<String>> emailSubscriptions) {
		this.emailSubscriptions = emailSubscriptions;
	}

	public List<EmailSiteCombo> getSharedSubscribedSites() {
		return sharedSubscribedSites;
	}

	public void setSharedSubscribedSites(List<EmailSiteCombo> sharedSubscribedSites) {
		this.sharedSubscribedSites = sharedSubscribedSites;
	}

	public License getLicense() {
		return license;
	}

	public void setLicense(License license) {
		this.license = license;
	}
	
	public boolean isLicenseValid() {
		if (this.license!=null){
			Long expiryTime = this.license.getExpiryDate();
			if (expiryTime!=null){
				Calendar expiryDate = Calendar.getInstance();    
				expiryDate.setTime(new Date(expiryTime));
				expiryDate.add(Calendar.DATE, 1);
				Long todayTime = System.currentTimeMillis();
				if (expiryDate.getTimeInMillis()>=todayTime){
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((activeAPIkey == null) ? 0 : activeAPIkey.hashCode());
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((allsavedAPIkeys == null) ? 0 : allsavedAPIkeys.hashCode());
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((companyname == null) ? 0 : companyname.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((createtime == null) ? 0 : createtime.hashCode());
		result = prime * result + ((defaultAPIkey == null) ? 0 : defaultAPIkey.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((emailSubscriptions == null) ? 0 : emailSubscriptions.hashCode());
		result = prime * result + ((firstname == null) ? 0 : firstname.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((lastmodifiedtime == null) ? 0 : lastmodifiedtime.hashCode());
		result = prime * result + ((lastname == null) ? 0 : lastname.hashCode());
		result = prime * result + ((license == null) ? 0 : license.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((postcode == null) ? 0 : postcode.hashCode());
		result = prime * result + ((sharedSubscribedSites == null) ? 0 : sharedSubscribedSites.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((termsAndConditions == null) ? 0 : termsAndConditions.hashCode());
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
		User other = (User) obj;
		if (activeAPIkey == null) {
			if (other.activeAPIkey != null)
				return false;
		} else if (!activeAPIkey.equals(other.activeAPIkey))
			return false;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (allsavedAPIkeys == null) {
			if (other.allsavedAPIkeys != null)
				return false;
		} else if (!allsavedAPIkeys.equals(other.allsavedAPIkeys))
			return false;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (companyname == null) {
			if (other.companyname != null)
				return false;
		} else if (!companyname.equals(other.companyname))
			return false;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (createtime == null) {
			if (other.createtime != null)
				return false;
		} else if (!createtime.equals(other.createtime))
			return false;
		if (defaultAPIkey == null) {
			if (other.defaultAPIkey != null)
				return false;
		} else if (!defaultAPIkey.equals(other.defaultAPIkey))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (emailSubscriptions == null) {
			if (other.emailSubscriptions != null)
				return false;
		} else if (!emailSubscriptions.equals(other.emailSubscriptions))
			return false;
		if (firstname == null) {
			if (other.firstname != null)
				return false;
		} else if (!firstname.equals(other.firstname))
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
		if (lastname == null) {
			if (other.lastname != null)
				return false;
		} else if (!lastname.equals(other.lastname))
			return false;
		if (license == null) {
			if (other.license != null)
				return false;
		} else if (!license.equals(other.license))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (postcode == null) {
			if (other.postcode != null)
				return false;
		} else if (!postcode.equals(other.postcode))
			return false;
		if (sharedSubscribedSites == null) {
			if (other.sharedSubscribedSites != null)
				return false;
		} else if (!sharedSubscribedSites.equals(other.sharedSubscribedSites))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (termsAndConditions == null) {
			if (other.termsAndConditions != null)
				return false;
		} else if (!termsAndConditions.equals(other.termsAndConditions))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", email=" + email + ", password=" + password + ", firstname=" + firstname
				+ ", lastname=" + lastname + ", address=" + address + ", postcode=" + postcode + ", companyname="
				+ companyname + ", termsAndConditions=" + termsAndConditions + ", city=" + city + ", country=" + country
				+ ", lastmodifiedtime=" + lastmodifiedtime + ", createtime=" + createtime + ", status=" + status
				+ ", defaultAPIkey=" + defaultAPIkey + ", activeAPIkey=" + activeAPIkey + ", license=" + license
				+ ", allsavedAPIkeys=" + allsavedAPIkeys + ", emailSubscriptions=" + emailSubscriptions
				+ ", sharedSubscribedSites=" + sharedSubscribedSites + "]";
	}

}
