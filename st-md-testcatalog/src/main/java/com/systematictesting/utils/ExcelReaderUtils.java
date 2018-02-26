package com.systematictesting.utils;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReaderUtils {
	
	private static final Logger log = LoggerFactory.getLogger(ExcelReaderUtils.class);
	private static final String EMPTY_STRING = "";
	
	public static int getRowCount(XSSFWorkbook xlsWorkbook, String sheetName) {
		log.debug("Fetching Total Rows from Sheet :  " +  sheetName);
		int index = xlsWorkbook.getSheetIndex(sheetName);
		if (index == -1){
			log.debug("Returning Total Rows are : 0");
			return 0;
		}
		else {
			XSSFSheet xlsSheet = xlsWorkbook.getSheetAt(index);
			int totalRows = xlsSheet.getLastRowNum() + 1;
			log.debug("Returning Total Rows are :  " + totalRows);
			return totalRows;
		}
	}
	
	public static String getCellDataOnMasterSheet(XSSFWorkbook xlsWorkbook, String sheetName, String columnName, int rowNumber) {
		log.debug("Getting Cell Data from Sheet Name : "+sheetName+", Column Name : "+columnName+" and Row Number : "+rowNumber);
		int index = xlsWorkbook.getSheetIndex(sheetName);
		if (rowNumber <= 0 || index == -1) {
			log.debug("Returning Cell Data :  " + EMPTY_STRING);
			return EMPTY_STRING;
		}

		XSSFSheet sheet = xlsWorkbook.getSheetAt(index);
		int columnRowIndexNumber = 3;
		int columnIndexNumber = fetchColumnIndexUsingColumnName(columnName, sheet, columnRowIndexNumber);
		if (columnIndexNumber == -1) {
			log.debug("Returning Cell Data :  " + EMPTY_STRING);
			return EMPTY_STRING;
		}

		return fetchCellData(columnIndexNumber, rowNumber, sheet);
	}
	
	public static String getCellData(XSSFWorkbook xlsWorkbook, String sheetName, String columnName, int rowNumber) {
		log.debug("Getting Cell Data from Sheet Name : "+sheetName+", Column Name : "+columnName+" and Row Number : "+rowNumber);
		int index = xlsWorkbook.getSheetIndex(sheetName);
		if (rowNumber <= 0 || index == -1) {
			log.debug("Returning Cell Data :  " + EMPTY_STRING);
			return EMPTY_STRING;
		}

		XSSFSheet sheet = xlsWorkbook.getSheetAt(index);
		int columnRowIndexNumber = 0;
		int columnIndexNumber = fetchColumnIndexUsingColumnName(columnName, sheet,columnRowIndexNumber);
		if (columnIndexNumber == -1) {
			log.debug("Returning Cell Data :  " + EMPTY_STRING);
			return EMPTY_STRING;
		}

		return fetchCellData(columnIndexNumber, rowNumber, sheet);
	}

	private static int fetchColumnIndexUsingColumnName(String columnName, XSSFSheet sheet, int columnRowIndex) {
		log.debug("Fetching Column Index Number for Column Name : "+columnName+", in Sheet Name : "+sheet.getSheetName());
		XSSFRow row = sheet.getRow(columnRowIndex);
		log.debug("Total cells in a ROW : "+row.getLastCellNum());
		int columnIndexNumber = -1;
		for (int i = 0; i < row.getLastCellNum(); i++) {
			if (row.getCell(i).getStringCellValue().trim().equals(columnName.trim())) {
				columnIndexNumber = i;
				break;
			}
		}
		log.debug("Returning Column Index number :  " + columnIndexNumber);
		return columnIndexNumber;
	}

	public static String getCellData(XSSFWorkbook xlsWorkbook, String sheetName, int columnIndexNumber, int rowNumber) {
		log.debug("Getting Cell Data from Sheet Name : "+sheetName+", Column Index Number : "+columnIndexNumber+" and Row Number : "+rowNumber);
		int index = xlsWorkbook.getSheetIndex(sheetName);
		if (rowNumber <= 0 || index == -1 || columnIndexNumber == -1) {
			log.debug("Returning Cell Data :  " + EMPTY_STRING);
			return EMPTY_STRING;
		}
		XSSFSheet sheet = xlsWorkbook.getSheetAt(index);

		return fetchCellData(columnIndexNumber, rowNumber, sheet);
	}

	private static String fetchCellData(int columnIndexNumber, int rowNumber, XSSFSheet sheet) {
		log.debug("Fetching Cell Data from Sheet Name : "+sheet.getSheetName()+", Column Index Number : "+columnIndexNumber+" and Row Number : "+rowNumber);
		XSSFRow row;
		row = sheet.getRow(rowNumber - 1);
		if (row == null) {
			log.debug("Returning Cell Data :  " + EMPTY_STRING);
			return EMPTY_STRING;
		}

		XSSFCell cell = row.getCell(columnIndexNumber);
		if (cell == null) {
			log.debug("Returning Cell Data :  " + EMPTY_STRING);
			return EMPTY_STRING;
		} else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
			log.debug("Returning Cell Data :  " + cell.getStringCellValue());
			return cell.getStringCellValue();
		} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC || cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
			String cellText = String.valueOf(cell.getNumericCellValue());
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				double d = cell.getNumericCellValue();
				Calendar cal = Calendar.getInstance();
				cal.setTime(HSSFDateUtil.getJavaDate(d));
				cellText = (String.valueOf(cal.get(Calendar.YEAR))).substring(2);
				// format in form of M/D/YY
				cellText = cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH) + 1 + "/" + cellText;
				log.debug("Returning Cell Data :  " + cellText);
				return cellText;
			} else {
				log.debug("Returning Cell Data :  " + cellText);
				return cellText;
			}

		} else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
			log.debug("Returning Cell Data :  " + String.valueOf(cell.getBooleanCellValue()));
			return String.valueOf(cell.getBooleanCellValue());
		} else {
			log.debug("Returning Cell Data :  " + EMPTY_STRING);
			return EMPTY_STRING;
		}
	}
}
