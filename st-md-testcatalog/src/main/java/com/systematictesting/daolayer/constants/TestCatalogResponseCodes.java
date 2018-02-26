package com.systematictesting.daolayer.constants;

public interface TestCatalogResponseCodes extends ResponseCodes {

	interface EXCEL_SHEET_DOES_NOT_MEET_STANDARD {
		public static int code = 504;
		public static String message = "Excel sheet does not meet V1 Standards.";
		public static String SUITE_NAME_MISSING = "Suite Name is not present in MasterSheet.";
		public static String TEST_SUITE_DATE_MISSING = "Test Suite date is not present in MasterSheet.";
		public static String MANDATORY_COLUMNS_MISSING_IN_MASTER_SHEET = "Mandatory columns are missing in MasterSheet.";
		public static String NO_TEST_CASE_IN_TEST_SUITE = "No test cases present in MasterSheet.";
		public static String AUTOMATED_TEST_CASE_SHEETS_ARE_MISSING = "Automated test cases sheets are missing in MasterSheet.";
		public static String KEY_VALUE_PAIRS_SHEET_MISSING = "KeyValuePairs sheet is missing in MasterSheet.";
		public static String STANDARD_HEADER_MISSING_IN_TESTCASE_SHEET = "Test Case sheet missing Standard V1 Header.";
	}
	
	interface INVALID_EXCEL_FILE_DETECTED {
		public static int code = 505;
		public static String message = "Invalid excel file detected.";
	}
	
	interface TESTSUITE_NAME_ALREADY_PRESENT {
		public static int code = 506;
		public static String message = "Supplied Test Suite name already present in Database against then requested site.";
	}
	
	interface INVALID_TEST_SUITE {
		public static int code = 507;
		public static String message = "Invalid Test Suite Name Detected.";
	}
}
