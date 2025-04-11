package com.mmu.services.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import com.mmu.services.entity.MasDepartment;
import com.mmu.services.entity.MasDepartmentType;
import com.mmu.services.hibernateutils.GetHibernateUtils;



public class CommonUtil {
	
	@Autowired
	static GetHibernateUtils getHibernateUtils;	

	private static final int NIP001 = 0;

	public static String generateRandomToken() {
		final String CHAR_LIST = "NIP0001";
		int St=NIP001;
		StringBuffer randStr = new StringBuffer(St);
		SecureRandom secureRandom = new SecureRandom();
		for (int i = 0; i < St; i++)
			randStr.append(CHAR_LIST.charAt(secureRandom.nextInt(CHAR_LIST.length())));
		return randStr.toString();
	}

	public static String generateHashValue(String password) {
		String passwordToHash = "password";
		String generatedPassword = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(passwordToHash.getBytes());
			byte[] bytes = md.digest();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			generatedPassword = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return generatedPassword;
	}

	public static String getDeviceType(String user_agent) {

		if (user_agent.indexOf("Windows") != -1 )
			return "WEB_WIN";
		else if (user_agent.indexOf("Macintosh") != -1 )
			return "WEB_iOS";
		else if (user_agent.indexOf("Mobile") != -1 && user_agent.indexOf("iPhone") != -1)
			return "MOB_iOS";
		else if (user_agent.indexOf("Mobile") != -1 && user_agent.indexOf("Android") != -1)
			return "MOB_ARD";
		else if (user_agent.indexOf("iOS") != -1 )
			return "iOS_APP";
		else if (user_agent.indexOf("Android") != -1 )
			return "ADR_APP";
		else if (user_agent.indexOf("Postman") != -1 )
			return "WEB_WIN";
		else
			return "undefined";
	}
	public static String getOtp() {
		return String.format("%04d", new Random().nextInt(9999));
	}

	 public static String getDate() {
	 return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	 }

	public static boolean validateTime(Date logintime, long timeout) {
		long login=logintime.getTime();
		long today=Validators.getDateTime().getTime();
		if(timeout==0)return true;
		else if(today-login>timeout)return false;
		else return true;
	}
	
	public static Date convertStringTypeDateToDateType(String date) {
		Date orderDateTime = null;

		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		if (date != null) {

			try {
				orderDateTime = df.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}

		}

		return orderDateTime;
	}
	
	public static Date convertStringDate(String date) {
		Date orderDateTime = null;

		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		if (date != null) {
			try {
				orderDateTime = df.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		return orderDateTime;
	}
	

	
	public static Long getDepartmentIdAgainstCode(org.hibernate.Session session,String code) {
		Long departmentId = null;
		MasDepartment masDepartment = (MasDepartment) session.createCriteria(MasDepartment.class)
				.add(Restrictions.eq("departmentCode", code).ignoreCase()).uniqueResult();
		if(masDepartment != null) {
			departmentId = masDepartment.getDepartmentId();
		}
		return departmentId;		
	}
	
	public static Long getDepartmentTypeIdAgainstCode(org.hibernate.Session session,String code) {
		Long departmentTypeId = null;
		MasDepartmentType masDepartmentType = (MasDepartmentType) session.createCriteria(MasDepartmentType.class)
				.add(Restrictions.eq("departmentTypeCode", code).ignoreCase()).uniqueResult();
		if(masDepartmentType != null) {
			departmentTypeId = masDepartmentType.getDepartmentTypeId();
		}
		return departmentTypeId;		
	}

}
