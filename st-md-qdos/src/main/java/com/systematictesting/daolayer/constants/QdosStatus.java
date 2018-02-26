package com.systematictesting.daolayer.constants;

import com.systematictesting.daolayer.constants.Status;

public interface QdosStatus extends Status{

	interface REAL_TIME_STATUS {
		String NEW = "NEW";
		String FINISHED = "FINISHED";
		String ABORTED = "ABORTED";
		String RUNNING = "RUNNING";
	}
	
	interface TEST_CASE_STATUS {
		String PASS = "PASS";
		String FAIL = "FAIL";
		String ABORTED = "ABORTED";
		String MANUAL = "MANUAL";
	}
	
}
