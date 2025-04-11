package com.mmu.services.utils;

import java.net.URL;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.springframework.web.bind.ServletRequestUtils;

import com.mmu.services.entity.MasEmployee;
import com.mmu.services.utils.HMSUtil;






public class HMSUtil extends ServletRequestUtils {
	
 public static int calculateAgeNoOfYear(Date dob ) {
		 
		 Calendar lCal = Calendar.getInstance();
		    lCal.setTime(dob);
		    int yr=lCal.get(Calendar.YEAR);
		    int mn=lCal.get(Calendar.MONTH) + 1;
		    int dt=lCal.get(Calendar.DATE);
		    LocalDate today = LocalDate.now(); //Today's date
		LocalDate birthday = LocalDate.of(yr,mn,dt) ; //Birth date
		Period p = Period.between(birthday, today); // Period
		return p.getYears();
	 }
 
 public static String getFullName(MasEmployee emp) {
	 String patientName = "";
		if (emp.getEmployeeName() != null) {
			patientName = emp.getEmployeeName();
		}

		/*if (emp.getMiddleName() != null) {
			patientName = patientName + " "
					+ emp.getMiddleName();
		}
		if (emp.getLastName() != null) {
			patientName = patientName + " "
					+ emp.getLastName();
	 
			}*/
		return patientName;
 	}
 
 public static String getProperties(String fileName, String propName){
		String propertyValue = null;
		try{
			URL resourcePath = Thread.currentThread().getContextClassLoader()
					.getResource(fileName);
			Properties properties= new Properties();
			properties.load(resourcePath.openStream());
			propertyValue = properties.getProperty(propName);
		}catch(Exception e){e.printStackTrace();}
		return propertyValue;
	}
 
 public static String convertDateToStringFormat(Date date, String format){
     String dateFormat="";
     SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
     if(date != null) {
    	 dateFormat=simpleDateFormat.format(date);    
     }    
     return dateFormat;	

}
 
 public static String convertDateToStringFormat1(LocalDate visitDate, String format){
     String dateFormat="";
     SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
     if(visitDate != null) {
    	 dateFormat=simpleDateFormat.format(visitDate);    
     }    
     return dateFormat;	

}
 
 
 
 
 public static Date getNextDate(Date inputDate) {
	 
	String strDate= convertDateToStringFormat(inputDate, "yyyy-MM-dd");
	Date returnDate=null;
	try {
		returnDate = new SimpleDateFormat("yyyy-MM-dd").parse(strDate);
		returnDate.setHours(0);
		returnDate.setMinutes(0);
		returnDate.setSeconds(0);
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 final Calendar calendar = Calendar.getInstance();
	 calendar.setTime(returnDate);
	 calendar.add(Calendar.DAY_OF_YEAR, 1);
	 return calendar.getTime();
	}
 
 
 
 public static Date dateFormatteryyyymmdd(String stringDate) throws Exception {
		SimpleDateFormat dateFormatterYYYYMMDD = new SimpleDateFormat(
				"yyyy-MM-dd");
		try {
			return (dateFormatterYYYYMMDD.parse(stringDate));
		} catch (ParseException e) {
			e.printStackTrace();
			throw e;			
		}
		
	}
 
 
	public static Date convertStringDateToUtilDate(String date, String format) {
		Date utilDate = null;

		SimpleDateFormat df = new SimpleDateFormat(format);
		if (date != null) {
			try {
				utilDate = df.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		return utilDate;
	}
	
	
	public static Date convertStringDateToUtilDateForDatabase(String date) {
		Date utilDate = null;
		String format="";
		if(date.trim().length()>10) {
			 format="dd/MM/yyyy HH:mm:ss";
		}else {
			 format="dd/MM/yyyy";
		}
		
		SimpleDateFormat df = new SimpleDateFormat(format);
		if (date != null) {
			try {
				utilDate = df.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		return utilDate;
	}
	
	public static Date convertStringDateToUtilDateForDatabaseForExcel(String date) {
		Date utilDate = null;
		String format="";
		if(date.trim().length()>10) {
			 format="dd/MM/yyyy HH:mm:ss";
		}else {
			 format="dd/MM/yy";
		}
		
		SimpleDateFormat df = new SimpleDateFormat(format);
		if (date != null) {
			try {
				utilDate = df.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		return utilDate;
	}
	
	
	
	public static String convertNullToEmptyString(Object obj) {
		return (obj == null) ? "" : obj.toString();
	}
	
	public static String convertStringNullToEmptyString(String stringNull) {
		return (stringNull.equals("null")) ? "" : stringNull;
	}
	
	public static long calculateTotalMinutes(String startTime, String endTime) {

		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		Date d1;
		Date d2;
		long totalMinutes=0;
		try {
			d1 = format.parse(startTime);
			d2 = format.parse(endTime);
			totalMinutes = (d2.getTime() - d1.getTime())/(60 * 1000); 
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		return totalMinutes;
	}
	 public static String convertStringFormat(String date) {
			Date orderDateTime = null;

			SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
			if (date != null) {
				try {
					orderDateTime = inputFormat.parse(date);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			return outputFormat.format(orderDateTime);
		}
	
	
	public static String addingMinutes(String startTime, int timeInterval) {

		 SimpleDateFormat df = new SimpleDateFormat("HH:mm");
		 Date d;
		 String newTime="";
		try {
			d = df.parse(startTime);
			 Calendar cal = Calendar.getInstance();
			 cal.setTime(d);
			 cal.add(Calendar.MINUTE, timeInterval);
			 newTime = df.format(cal.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		 return newTime;
	}
	
	
	 public static String getDateWithoutTime(Date date) {
		    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		    return sdf.format(date);
		  }
	
	 
	 public static String changeDateToddMMyyyy(Date dbDate) {
		 String strDate = dbDate.toString();
		 String strNewDate = "", year = "", dt = "", month = "";
		 year = strDate.substring(0, 4);
		 month = strDate.substring(5, 7);
		 dt = strDate.substring(8, 10);
		 strNewDate = (dt + "/" + month + "/" + year);
		 return strNewDate;
		}
	 
	 public static void main(String[] args) {
		
		 System.out.println( getPreviousDate(new Date()));
	 }
	 
	 
	 public static Date convertStringTypeDateToDateType(String date) {
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
	 
	 
	public static String calculateAgeYearOrMonthOrDays(Date birthDate) {
		// get todays date
		Calendar now = Calendar.getInstance();
		// get a calendar representing their birth date

		Calendar cal = Calendar.getInstance();
		cal.setTime(birthDate);

		// calculate age as the difference in years.
		@SuppressWarnings("unused")
		int calculatedDays, calculatedMonth, calculatedYear;
		int currentDays = now.get(Calendar.DATE);
		int birthDays = cal.get(Calendar.DATE);
		int currentMonth = now.get(Calendar.MONTH);
		int birthMonth = cal.get(Calendar.MONTH);
		int currentYear = now.get(Calendar.YEAR);
		int birthYear = cal.get(Calendar.YEAR);

		if (currentDays < birthDays) {
			currentDays = currentDays + 30;
			calculatedDays = currentDays - birthDays;
			currentMonth = currentMonth - 1;
		} else {
			calculatedDays = currentDays - birthDays;
		}

		if (currentMonth < birthMonth) {
			currentMonth = currentMonth + 12;
			calculatedMonth = currentMonth - birthMonth;
			currentYear = currentYear - 1;
		} else {
			calculatedMonth = currentMonth - birthMonth;
		}

		int age = currentYear - birthYear;
		String patientAge = "";

		if (age == 0 && calculatedMonth != 0 && calculatedDays != 0) {
			patientAge = calculatedMonth + " Months ";
		} else if (age == 0 && calculatedMonth == 0 && calculatedDays != 0) {
			patientAge = calculatedDays + "  Days";
		} else if (age == 0 && calculatedMonth != 0 && calculatedDays == 0) {
			patientAge = calculatedMonth + " Months ";
		} else if (age == 0 && calculatedMonth == 0 && calculatedDays == 0) {
			patientAge = "1 Days";
		} else {
			patientAge = age + " Years ";
		}
		return patientAge;
	}
	 
	
public static String calculateAge(Date birthDate) {
		
		int years = 0;
	    int months = 0;
	    int days = 0;

	    //create calendar object for birth day
	    Calendar birthDay = Calendar.getInstance();
	    birthDay.setTimeInMillis(birthDate.getTime());

	    //create calendar object for current day
	    long currentTime = System.currentTimeMillis();
	    Calendar now = Calendar.getInstance();
	    now.setTimeInMillis(currentTime);

	    //Get difference between years
	    years = now.get(Calendar.YEAR) - birthDay.get(Calendar.YEAR);
	    int currMonth = now.get(Calendar.MONTH) + 1;
	    int birthMonth = birthDay.get(Calendar.MONTH) + 1;

	    //Get difference between months
	    months = currMonth - birthMonth;

	    //if month difference is in negative then reduce years by one 
	    //and calculate the number of months.
	    if (months < 0)
	    {
	       years--;
	       months = 12 - birthMonth + currMonth;
	       if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE))
	          months--;
	    } else if (months == 0 && now.get(Calendar.DATE) < birthDay.get(Calendar.DATE))
	    {
	       years--;
	       months = 11;
	    }

	    //Calculate the days
	    if (now.get(Calendar.DATE) > birthDay.get(Calendar.DATE))
	       days = now.get(Calendar.DATE) - birthDay.get(Calendar.DATE);
	    else if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE))
	    {
	       int today = now.get(Calendar.DAY_OF_MONTH);
	       now.add(Calendar.MONTH, -1);
	       days = now.getActualMaximum(Calendar.DAY_OF_MONTH) - birthDay.get(Calendar.DAY_OF_MONTH) + today;
	    } 
	    else
	    {
	       days = 0;
	       if (months == 12)
	       {
	          years++;
	          months = 0;
	       }
	    }
	    
	    if(years !=0)
	    {
	    	return years + " Years";
	    }
	    else if (years == 0 && months !=0 )
	    {
	    	return months + " months";
	    }
	    return days + " Days";
	    
	   // return years + " Years, " + months + " months, " + days + " Days";
	    //return years + " Years";
	}

	 public static String convertDateAndTimeToUSformatOnlyDate(Date dat) {
		  String date = "";
		  try {
			  SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy");
			  if (dat != null) {
				  date = sdf.format(dat);
			  }
		  } catch (Exception e) {
		  }
		  return date;
	  }
	
	/*
	 * public static final String[] tensNames = { "", " ten", " twenty", " thirty",
	 * " forty", " fifty", " sixty", " seventy", " eighty", " ninety" }; public
	 * static final String[] numNames = { "", " one", " two", " three", " four",
	 * " five", " six", " seven", " eight", " nine", " ten", " eleven", " twelve",
	 * " thirteen", " fourteen", " fifteen", " sixteen", " seventeen", " eighteen",
	 * " nineteen" };
	 * 
	 * 
	 * 
	 * public static String convertLessThanOneThousand(int paramInt) { String str;
	 * if (paramInt % 100 < 20) { str = numNames[(paramInt % 100)]; paramInt /= 100;
	 * } else { str = numNames[(paramInt % 10)]; paramInt /= 10;
	 * 
	 * str = tensNames[(paramInt % 10)] + str; paramInt /= 10; } if (paramInt == 0)
	 * return str; return numNames[paramInt] + " hundred" + str; }
	 * 
	 */
	
	 public static final String[] units;
	    public static final String[] tens;
	    
	    
	    static {
	        units = new String[] { "", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen" };
	        tens = new String[] { "", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety" };
	    }
	    
	    public static String convert(final int n) {
	        if (n < 0) {
	            return "Minus " + convert(-n);
	        }
	        if (n < 20) {
	            return units[n];
	        }
	        if (n < 100) {
	            return tens[n / 10] + ((n % 10 != 0) ? " " : "") + units[n % 10];
	        }
	        if (n < 1000) {
	            return units[n / 100] + " Hundred" + ((n % 100 != 0) ? " " : "") + convert(n % 100);
	        }
	        if (n < 100000) {
	            return convert(n / 1000) + " Thousand" + ((n % 10000 != 0) ? " " : "") + convert(n % 1000);
	        }
	        if (n < 10000000) {
	            return convert(n / 100000) + " Lakh" + ((n % 100000 != 0) ? " " : "") + convert(n % 100000);
	        }
	        return convert(n / 10000000) + " Crore" + ((n % 10000000 != 0) ? " " : "") + convert(n % 10000000);
	    }

		public static String convertTimeinHHMMfromLong(long timestamp) {
			DateFormat format = new SimpleDateFormat( "HH:mm" );
			String time = format.format( timestamp );
			return time;
		}
		
		public static long getNoOfDaysFromPreviousDateToCurrentDate(Date previousDate) {
			long noOfDays = 0;
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			//current date in dd/MM/yyyy
			Date todayDate = new Date();
	    	String currentdate = dateFormat.format(todayDate);
	    	//prevous date in dd/MM/yyyy
	    	String pastDate = dateFormat.format(previousDate);

	    	try {
	    	    Date date1 = dateFormat.parse(currentdate);
	    	    Date date2 = dateFormat.parse(pastDate);
	    	    long diff = date1.getTime() - date2.getTime();
	    	    noOfDays =  + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
	    	    System.out.println ("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
	    	} catch (Exception e) {
	    	    e.printStackTrace();
	    	}
	    	
			return noOfDays;
		}
		
		public static Date getDateBeforeDays(int day) {
			
			 Calendar cal = Calendar.getInstance();
			 cal.add(Calendar.DATE, day);
			 cal.set(Calendar.HOUR_OF_DAY,0);
			 cal.set(Calendar.MINUTE,0);
			 Date date = cal.getTime();
			 return date;
			 
		}
		public static String getDateBetweenTwoDate(Date dateMedicalExamFrom, Date dateOFBirthTo){

			 String toDate = HMSUtil.convertDateToStringFormat(
					 dateMedicalExamFrom, "dd/MM/yyyy");
			 String date1 = HMSUtil.convertDateToStringFormat(
					 dateOFBirthTo, "dd/MM/yyyy");
			
			 
			 	//String date="12/02/2021";
				//String date1="04/02/1988";
				String [] dateA=toDate.split("/");
				String [] dateB=date1.split("/");
				
				LocalDate firstDate = LocalDate.of(Integer.parseInt(dateA[2].trim().toString()), Integer.parseInt(dateA[1].trim().toString()), Integer.parseInt(dateA[0].trim().toString()));
				LocalDate secondDate = LocalDate.of(Integer.parseInt(dateB[2].trim().toString()), Integer.parseInt(dateB[1].trim().toString()), Integer.parseInt(dateB[0].trim().toString()));
				Period diff = Period.between(secondDate, firstDate);
				//String yearsnow= String.valueOf(diff.getYears())+" years"+", "+String.valueOf(diff.getMonths())+" months, "+ String.valueOf(diff.getDays()+" days");
				String yearsnow= String.valueOf(diff.getYears())+" years";
				return yearsnow;	

		}
		


		 public static String getTotalServiceDateBetweenTwoDate(Date dateMedicalExamFrom, Date docOrDoa){

			 String toDate = HMSUtil.convertDateToStringFormat(
					 dateMedicalExamFrom, "dd/MM/yyyy");
			 String date1 = HMSUtil.convertDateToStringFormat(
					 docOrDoa, "dd/MM/yyyy");
			
			 
			 	//String date="12/02/2021";
				//String date1="04/02/1988";
				String [] dateA=toDate.split("/");
				String [] dateB=date1.split("/");
				
				LocalDate firstDate = LocalDate.of(Integer.parseInt(dateA[2].trim().toString()), Integer.parseInt(dateA[1].trim().toString()), Integer.parseInt(dateA[0].trim().toString()));
				LocalDate secondDate = LocalDate.of(Integer.parseInt(dateB[2].trim().toString()), Integer.parseInt(dateB[1].trim().toString()), Integer.parseInt(dateB[0].trim().toString()));
				Period diff = Period.between(secondDate, firstDate);
				//String yearsnow= String.valueOf(diff.getYears())+" years"+", "+String.valueOf(diff.getMonths())+" months, "+ String.valueOf(diff.getDays()+" days");
				String yearsnow= String.valueOf(diff.getYears())+" years";
				return yearsnow;	

		}	
		 
		 public static Long[] convertFromStringToLongArray(String[] hospitals) {
				Long [] result = new Long[hospitals.length];
				for (int i = 0; i < hospitals.length; i++)
					if(hospitals[i]!=null && !hospitals[i].equalsIgnoreCase("null"))
				     result[i] = Long.parseLong(hospitals[i]);
				  return result;
			}
		 
		 public static Boolean checkIfNull(Object obj) {
			 if(obj == null) {
				 return true;
			 }
			 return false;
		 }
		 
		 public static Map<String,Date> getMinAndMaxDateAgainstInputDate(String date){
			 SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/YYYY");
			 Map<String,Date> map = new HashMap<>();
			 try {
				Date minDate = formatter.parse(date);
				Date maxDate = new Date(minDate.getTime() + TimeUnit.DAYS.toMillis(1));
				map.put("minDate", minDate);
				map.put("maxDate", minDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 return map;
			 
		 }
		 
		 public static Time convertStringTimeIntoSQLTime(String time) {
			 try {
					SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
					long ms;
					ms = sdf.parse(time).getTime();
					Time t = new Time(ms);
					return t;
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 return null;
			 
		 }
		 
		public static Time getCurrentSQLTime() {
			long now = System.currentTimeMillis();
			Time sqlTime = new Time(now);
			return sqlTime;
		}
		
		public static java.sql.Timestamp getCurrentTimeStamp() {
			Calendar calendar = Calendar.getInstance();
			java.sql.Timestamp timestamp = new java.sql.Timestamp(calendar.getTime().getTime());
			return timestamp;
		}
		
		 public static String convertUtilDateToddMMyyyy(Date date){
		     String dateFormat="";
		     SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		     if(date != null) {
		    	 dateFormat=simpleDateFormat.format(date);    
		     }    
		     return dateFormat;	

		}
		 
		 public static Date subtractYearFromDate(Date date) {
			 
			 return null;
		 }
		 
		 public static String convertStringNullToObjectNull(String input) {
			 if(input.equals("null")) {
				 return null;
			 }else {
				 return input;
			 }
		 }
		 
		 public static Date getDOBFromAge(int age) {
			 Date dob = new Date();
			 int year = dob.getYear();
			 dob.setYear(year-age);
			 System.out.println(dob);
			 return dob;
		 }
		 
		 public static Date getTodayFormattedDate() {
			 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			 Date dateWithoutTime = null;
			 try {
				dateWithoutTime = sdf.parse(sdf.format(new Date()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			 return dateWithoutTime;
		 }
		
		 public static Date getPreviousDate(Date today) {
			 
				String strDate= convertDateToStringFormat(today, "yyyy-MM-dd");
				Date returnDate=null;
				try {
					returnDate = new SimpleDateFormat("yyyy-MM-dd").parse(strDate);
					returnDate.setHours(0);
					returnDate.setMinutes(0);
					returnDate.setSeconds(0);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 final Calendar calendar = Calendar.getInstance();
				 calendar.setTime(returnDate);
				 calendar.add(Calendar.DAY_OF_YEAR, -1);
				 return calendar.getTime();
			}

		 public static Date getDOBFromAge(int age, String ageFlag) {
			 Date dob = new Date();
			 if(ageFlag.equalsIgnoreCase("Y")) {
				 int year = dob.getYear();
				 dob.setYear(year-age);
			 }else if(ageFlag.equalsIgnoreCase("M")) {
				 int month = dob.getMonth();
				 dob.setMonth(month-age);
			 }else if(ageFlag.equalsIgnoreCase("D")) {
				 int days = dob.getDate();
				 dob.setDate(days-age);
			 }
			 System.out.println(dob);
			 return dob;
		 }
		 
		 public static String calculateAgeFromDOB(Date dob) {
			 
			 Calendar lCal = Calendar.getInstance();
			    lCal.setTime(dob);
			    int yr=lCal.get(Calendar.YEAR);
			    int mn=lCal.get(Calendar.MONTH) + 1;
			    int dt=lCal.get(Calendar.DATE);
			    LocalDate today = LocalDate.now(); //Today's date
			LocalDate birthday = LocalDate.of(yr,mn,dt) ; //Birth date
			Period p = Period.between(birthday, today); // Period
			int year = p.getYears();
			int month = p.getMonths();
			int days = p.getDays();
			String calculatedAge = "";
			if(year != 0){
				calculatedAge =  year+" Yrs";
				return calculatedAge;
			}
			if(month != 0){
				calculatedAge = month+" Mths";
				return calculatedAge;
			}
			if(days != 0){
				calculatedAge = days+" Days";
				return calculatedAge;
			}
			return calculatedAge;
		 }

public static String converStringDateFormatToStringDate(String dateValue,String oldDateFormat,String newDateFormate) {
	
	 SimpleDateFormat oldFormat = new SimpleDateFormat(oldDateFormat);
	 Date date = null;
	try {
		date = oldFormat.parse(dateValue);
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		SimpleDateFormat sdf = new SimpleDateFormat(newDateFormate);
	 String date11= sdf.format(date);
	return date11;
}
public static String getReplaceString(String replaceValue) {
	
	 String stringReplace=replaceValue.replaceAll("[\\[\\]]", "");
	 return  stringReplace.replaceAll("^\"|\"$", "");
		
	}
}


