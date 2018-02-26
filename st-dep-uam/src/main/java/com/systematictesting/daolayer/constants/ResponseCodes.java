/**
 * Copyright (c) Mar 9, 2015 Sharad Tech Ltd. (www.sharadtech.com) to Present.
 * All rights reserved. 
 */
package com.systematictesting.daolayer.constants;

public interface ResponseCodes {

	interface SUCCESS {
		public static int code = 200;
		public static String message = "SUCCESS";
	}
	
	interface DB_CONNECTION_FAILURE {
		public static int code = 100;
		public static String message = "DB Connection Failure Detected.";
	}
	
	interface ACCOUNT_ACTIVATION_LINK_INVALID {
		public static int code = 405;
		public static String message = "Account activation link is not valid. Please contact site administrator.";
	}
	
	interface INVALID_ACTIVE_API_KEY {
		public static int code = 406;
		public static String message = "Invalid ACTIVE API Key Detected. Please login again.";
	}
	
	interface EMAIL_NOT_VALID {
		public static int code = 407;
		public static String message = "Invalid Email Detected. Please login again.";
	}
	
	interface INVALID_PASSWORD {
		public static int code = 408;
		public static String message = "Invalid Password Detected. Please try again.";
	}
	
	interface SESSION_NOT_VALID {
		public static int code = 409;
		public static String message = "User Session is not valid. Please try again.";
	}
	
	interface SHARING_INVITATION_FAILED {
		public static int code = 410;
		public static String message = "Acceptance of Sharing Invitation is failed. Please try again.";
	}
	
	interface INTERNAL_ERROR {
		public static int code = 500;
		public static String message = "Internal Server Error Detected.";
	}
	
	interface DUPLICATE_EMAIL {
		public static int code = 300;
		public static String message = "Email Address already registered.";
	}
	
	interface LOGIN_FAILED {
		public static int code = 401;
		public static String message = "Login Failed.";
	}
	
	interface USER_DEACTIVATED {
		public static int code = 402;
		public static String message = "User has been deactivated. Please contact site administrator.";
	}
	
	interface NEW_USER_DETECTED {
		public static int code = 403;
		public static String message = "User account has not been activated. Please check your email and activate the account.";
	}
	
	interface SITE_NAME_NOT_ACTIVE {
		public static int code = 411;
		public static String message = "Sitename not active.";
	}
	
	interface USER_SITE_ALREADY_PRESENT {
		public static int code = 412;
		public static String message = "User site already present in DB.";
	}
	
	interface MISSING_MANDATORY_API_PARAMS {
		public static int code = 413;
		public static String message = "Missing Mandatory REST API Params.";
	}
	
	interface LICENSE_NOT_VALID {
		public static int code = 414;
		public static String message = "License Key not valid. Please consult site adminstrator or contact at license@systematictesting.com";
	}
	
	interface INVALID_UPLOAD_POST_REQUEST {
		public static int code = 415;
		public static String message = "Invalid upload post request detected. Upload POST request must be in multipart content.";
	}
	
	interface NO_CATALOG_SUITES_AVAILABLE {
		public static int code = 416;
		public static String message = "No Catalog Suites available. Please upload some test suites in Catalog Manager.";
	}
	
	interface USER_SITE_NOT_PRESENT {
		public static int code = 417;
		public static String message = "User site not present in DB.";
	}
	
	interface FILE_NOT_FOUND {
		public static int code = 418;
		public static String message = "File not found on server. Please connect with Site Administrator.";
	}
}
