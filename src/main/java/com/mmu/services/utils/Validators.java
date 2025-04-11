package com.mmu.services.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validators {

	static final String regex = "^(?!(?:(?:\\x22?\\x5C[\\x00-\\x7E]\\x22?)|(?:\\x22?[^\\x5C\\x22]\\x22?)){255,})(?!(?:(?:\\x22?\\x5C[\\x00-\\x7E]\\x22?)|(?:\\x22?[^\\x5C\\x22]\\x22?)){65,}@)(?:(?:[\\x21\\x23-\\x27\\x2A\\x2B\\x2D\\x2F-\\x39\\x3D\\x3F\\x5E-\\x7E]+)|(?:\\x22(?:[\\x01-\\x08\\x0B\\x0C\\x0E-\\x1F\\x21\\x23-\\x5B\\x5D-\\x7F]|(?:\\x5C[\\x00-\\x7F]))*\\x22))(?:\\.(?:(?:[\\x21\\x23-\\x27\\x2A\\x2B\\x2D\\x2F-\\x39\\x3D\\x3F\\x5E-\\x7E]+)|(?:\\x22(?:[\\x01-\\x08\\x0B\\x0C\\x0E-\\x1F\\x21\\x23-\\x5B\\x5D-\\x7F]|(?:\\x5C[\\x00-\\x7F]))*\\x22)))*@(?:(?:(?!.*[^.]{64,})(?:(?:(?:xn--)?[a-z0-9]+(?:-[a-z0-9]+)*\\.){1,126}){1,}(?:(?:[a-z][a-z0-9]*)|(?:(?:xn--)[a-z0-9]+))(?:-[a-z0-9]+)*)|(?:\\[(?:(?:IPv6:(?:(?:[a-f0-9]{1,4}(?::[a-f0-9]{1,4}){7})|(?:(?!(?:.*[a-f0-9][:\\]]){7,})(?:[a-f0-9]{1,4}(?::[a-f0-9]{1,4}){0,5})?::(?:[a-f0-9]{1,4}(?::[a-f0-9]{1,4}){0,5})?)))|(?:(?:IPv6:(?:(?:[a-f0-9]{1,4}(?::[a-f0-9]{1,4}){5}:)|(?:(?!(?:.*[a-f0-9]:){5,})(?:[a-f0-9]{1,4}(?::[a-f0-9]{1,4}){0,3})?::(?:[a-f0-9]{1,4}(?::[a-f0-9]{1,4}){0,3}:)?)))?(?:(?:25[0-5])|(?:2[0-4][0-9])|(?:1[0-9]{2})|(?:[1-9]?[0-9]))(?:\\.(?:(?:25[0-5])|(?:2[0-4][0-9])|(?:1[0-9]{2})|(?:[1-9]?[0-9]))){3}))\\]))$";
	public static final Pattern EMAIL_PATTERN = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
	public static final Pattern VALID_PHONE_NUMBER_REGEX = Pattern.compile("^[7-9][0-9]{9}$", Pattern.CASE_INSENSITIVE);
	private static final Pattern VALID_PASSWORD_REGEX = Pattern
			.compile("^.*(?=.{8,})(?=..*[0-9])(?=.*[a-z])(?=.*[-_@#$%^&+=]).*$");
	private static final Pattern VALID_PINCODE_REGEX = Pattern.compile("^[1-9][0-9]{5}$");

	public static boolean emailValidate(String email) {
		Matcher matcher = EMAIL_PATTERN.matcher(email);
		if (!matcher.matches())
			return false;
		else
			return true;
	}

	public static boolean phoneValidate(String num) {
		Matcher matcher = VALID_PHONE_NUMBER_REGEX.matcher(num);
		if (!matcher.matches())
			return false;
		else
			return true;
	}

	public static boolean passwordValidate(String pwd) {
		System.out.println(pwd);
		Matcher matcher = VALID_PASSWORD_REGEX.matcher(pwd);
		if (!matcher.matches())
			return false;
		else
			return true;
	}
	
	public static boolean pincodeValidate(String pincode) {
		System.out.println(pincode);
		Matcher matcher = VALID_PINCODE_REGEX.matcher(pincode);
		if (!matcher.matches())
			return false;
		else
			return true;

	}

	public static Date getDateTime() {
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		Date today = null;
		try {
			today = dateFormat.parse(dateFormat.format(new Date()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return today;
	}
	
	public static String getDateTimeString() {
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String today = null;
		today = dateFormat.format(new Date());
		return today;
	}

	public static boolean dateValidate(String format, String date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
	try {	
		dateFormat.parse(date);
		return true;
	} catch (Exception e) {
		e.printStackTrace();
		return false;
	}
	}

}
