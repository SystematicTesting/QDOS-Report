/**
 * Copyright (c) Mar 8, 2015 Sharad Tech Ltd. (www.sharadtech.com) to Present.
 * All rights reserved. 
 */
package com.systematictesting.daolayer.constants;

public interface Status {

	interface USER_STATUS {
		String NEW = "NEW";
		String ACTIVE = "ACTIVE";
		String INACTIVE = "INACTIVE";
	}
	
	interface SITE_STATUS {
		String ACTIVE = "ACTIVE";
		String DELETED = "DELETED";
	}
	
	interface SHARE_STATUS {
		String COMPANY = "company";
		String PUBLIC = "public";
		String PRIVATE = "private";
	}
}
