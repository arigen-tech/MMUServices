package com.mmu.services.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

public class JavaUtils {
	
	public static String getReplaceString(String replaceValue) {
		return replaceValue.replaceAll("[\\[\\]]", ",");
		}
	public static String getReplaceString1(String replaceValue) {
		return replaceValue.replaceAll("[\\[\\]]", "");
		}
	
	public static String replaceStringWithDoubleQuotes(String stringValue) {
		stringValue= stringValue.replaceAll("[\\[\\]]", "");
			return  stringValue.replaceAll("\"", "");
		}
	public static final String[] getSplitString(String value) {
			final String regex = "-";
		
		return value.split(regex);
	}
	
	public static final String calculateAgefromDob(Date dateOfBirth) {
		System.out.println("dob :: "+dateOfBirth);
		
		  Calendar c = Calendar.getInstance();
		  c.setTime(dateOfBirth);
		  int year = c.get(Calendar.YEAR);
		  int month = c.get(Calendar.MONTH) + 1;
		  int date = c.get(Calendar.DATE);
		  LocalDate l1 = LocalDate.of(year, month, date);
		  LocalDate now1 = LocalDate.now();
		  Period diff1 = Period.between(l1, now1);
		  //int age = diff1.getYears();
		  String age = diff1.getYears() + " Years";
		  System.out.println("age:" + diff1.getYears() + "Years");
	   
			return  age;  
	}
	
	
	
	public static final String getDateFromDateAndTime(Date date) {
		
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
	    return sdf.format(date);
	}
	
	public static final String getTimeFromDateAndTime(String datetime) {
		
		String time="";
		try {
		if(StringUtils.isNotEmpty(datetime) && StringUtils.isNotBlank(datetime)) {
			String[] sss = datetime.split("\\s");//split with space
			
			String[] sss1 = sss[1].split("\\."); // split with dot(.)
			
			//time = sss[1].substring(0, 8)+" "+sss[2];
			//time = sss1[0]+" "+sss[2];
			time = sss1[0];
		}else {
			System.out.println("date and time is not found");
		}
		}catch(Exception e) {
			e.printStackTrace(System.out);
		}
		
		return time;
		
	}
	
/*public static final String getTimeFromDateAndTime1(Date datetime) {
		
		String time="";
		try {
		if(StringUtils.isNotEmpty(datetime) && StringUtils.isNotBlank(datetime)) {
			String[] sss = datetime.split("\\s");//split with space
			
			String[] sss1 = sss[1].split("\\."); // split with dot(.)
			
			//time = sss[1].substring(0, 8)+" "+sss[2];
			//time = sss1[0]+" "+sss[2];
			time = sss1[0];
		}else {
			System.out.println("date and time is not found");
		}
		}catch(Exception e) {
			e.printStackTrace(System.out);
		}
		
		return time;
		
	}
	*/
}
