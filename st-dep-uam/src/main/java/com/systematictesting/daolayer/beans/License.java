package com.systematictesting.daolayer.beans;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.systematictesting.daolayer.entity.User;

public class License {

	public static final String DELIMITER = ",";
	public static final String EXPIRY_DATE_FORMAT = "dd/MM/yyyy";
	public static final String NOT_APPLICABLE = "Not Applicable";
	public static final String ENTERPRISE = "Enterprise";
	public static final String STANDARD = "Standard";
	public static final String FEATURE_KEY = "feature";
	
	private static final Logger logger = LoggerFactory.getLogger(License.class);
	private static final String SEPARATOR = "=";
	private static final String HMAC_DELIMITER = "~";
	private static final String CRYPTO_ALOG = "HmacMD5";
	private static final byte[] key_bytes = new byte[] { -10,-95,-120,58,-54,34,98,-34,-42,-2,100,-87};
	
	private String licenseKey;
	private Long expiryDate;
	private List<String> featureList = new ArrayList<String>();
	private String expiryDateString;
	
	public interface FIELDS {
		String LICENSE_KEY = "licenseKey";
		String EXPIRY_DATE_STRING = "expiryDateString";
		String EXPIRY_DATE = "expiryDate";
		String FEATURE_LIST = "featureList";
	}
	
	public String getLicenseKey() {
		return licenseKey;
	}
	public void setLicenseKey(String licenseKey) {
		this.licenseKey = licenseKey;
	}
	public Long getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(Long expiryDate) {
		this.expiryDate = expiryDate;
	}
	public List<String> getFeatureList() {
		return featureList;
	}
	public String getExpiryDateString() {
		return expiryDateString;
	}
	public void setExpiryDateString(String expiryDateString) {
		this.expiryDateString = expiryDateString;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((expiryDate == null) ? 0 : expiryDate.hashCode());
		result = prime * result + ((expiryDateString == null) ? 0 : expiryDateString.hashCode());
		result = prime * result + ((featureList == null) ? 0 : featureList.hashCode());
		result = prime * result + ((licenseKey == null) ? 0 : licenseKey.hashCode());
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
		License other = (License) obj;
		if (expiryDate == null) {
			if (other.expiryDate != null)
				return false;
		} else if (!expiryDate.equals(other.expiryDate))
			return false;
		if (expiryDateString == null) {
			if (other.expiryDateString != null)
				return false;
		} else if (!expiryDateString.equals(other.expiryDateString))
			return false;
		if (featureList == null) {
			if (other.featureList != null)
				return false;
		} else if (!featureList.equals(other.featureList))
			return false;
		if (licenseKey == null) {
			if (other.licenseKey != null)
				return false;
		} else if (!licenseKey.equals(other.licenseKey))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "License [licenseKey=" + licenseKey + ", expiryDate=" + expiryDate + ", featureList=" + featureList
				+ ", expiryDateString=" + expiryDateString + "]";
	}
	
	public boolean validateLicense(String email){
		logger.debug("VALIDATING LICENSE KEY : "+this.licenseKey);
		boolean result = false;
		StringBuilder hash_source = new StringBuilder();
		hash_source.append(FIELDS.EXPIRY_DATE).append(SEPARATOR).append(this.expiryDate).append(HMAC_DELIMITER);
		hash_source.append(User.FIELDS.EMAIL).append(SEPARATOR).append(email.toLowerCase().trim()).append(HMAC_DELIMITER);
		hash_source.append(FIELDS.FEATURE_LIST).append(SEPARATOR);
		for (String feature : featureList){
			hash_source.append(feature.trim()).append(DELIMITER);
		}
		hash_source.append(HMAC_DELIMITER);
		
		try {
			Mac hmac = Mac.getInstance(CRYPTO_ALOG);
			SecretKeySpec secret_key = new SecretKeySpec(key_bytes, CRYPTO_ALOG);
			hmac.init(secret_key);
			byte[] hmac_bytes = hmac.doFinal(hash_source.substring(0, hash_source.length() - 1).toString().getBytes());
			String token = String.format("%0" + (2 * hmac.getMacLength()) + "x", new BigInteger(1, hmac_bytes));
			
			if (token.equals(licenseKey)){
				logger.info("LICENSE KEY : "+this.licenseKey+" : is 100% valid.");
				result=true;
			} else {
				logger.warn("LICENSE KEY : "+this.licenseKey+" : is not valid.");
				logger.debug("LICENSE TOKEN : "+token);
				logger.warn("Here are some more details : \nLICENSE KEY : "+this.licenseKey+"\nFEATURE LIST="+this.featureList+"\nEMAIL="+email.toLowerCase().trim()+"\nEXPIRY_DATE_STRING="+this.expiryDateString+"\nEXPIRY_DATE="+this.expiryDate);
			}
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("No such algorithm detected.");
		} catch (InvalidKeyException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Invalid Key Detected");
		}
		
		return result;
	}
	
	private static String generateLicense(String features, String email, String dateOfExpiry) {
		try {
			DateFormat format = new SimpleDateFormat(License.EXPIRY_DATE_FORMAT);
			Date expiryDate = format.parse(dateOfExpiry);

			StringBuilder hash_source = new StringBuilder();
			hash_source.append(FIELDS.EXPIRY_DATE).append(SEPARATOR).append(expiryDate.getTime()).append(HMAC_DELIMITER);
			hash_source.append(User.FIELDS.EMAIL).append(SEPARATOR).append(email).append(HMAC_DELIMITER);
			hash_source.append(FIELDS.FEATURE_LIST).append(SEPARATOR);
			String[] featureList = features.split(DELIMITER);
			for (String feature : featureList) {
				hash_source.append(feature.trim()).append(DELIMITER);
			}
			hash_source.append(HMAC_DELIMITER);
			Mac hmac = Mac.getInstance(CRYPTO_ALOG);
			SecretKeySpec secret_key = new SecretKeySpec(key_bytes, CRYPTO_ALOG);
			hmac.init(secret_key);
			byte[] hmac_bytes = hmac.doFinal(hash_source.substring(0, hash_source.length() - 1).toString().getBytes());
			String token = String.format("%0" + (2 * hmac.getMacLength()) + "x", new BigInteger(1, hmac_bytes));
			return token;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("No such algorithm detected.");
		} catch (InvalidKeyException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Invalid Key Detected");
		} catch (ParseException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Expiry Date can't be parsed.");
		}
	}
	
	public static void main(String[] args) {
		Feature feature = new Feature();
		String features = feature.getAutomationCoverage()+DELIMITER+feature.getMoreVersionHistory()+DELIMITER+feature.getSiteManager();
		String email = "sharad.kumar@shell.com";
		String dateOfExpiry = "01/01/2016";
		System.out.println("FEATURE LIST = "+features);
		System.out.println("LICENSE KEY = "+generateLicense(features, email, dateOfExpiry));
	}
	
}
