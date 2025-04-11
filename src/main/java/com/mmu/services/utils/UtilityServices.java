package com.mmu.services.utils;

import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import net.sf.json.JSONObject;

public class UtilityServices {
	
	 protected static Properties js_messagepropertiesEn = new Properties();
	  protected static Properties messagepropertiesEn = new Properties();
	  protected static Properties properties = new Properties();
	  public static void resourceFileLoder() {
		  
		  try {
		      Resource resource = new ClassPathResource("/js_messages_en.properties");
		      js_messagepropertiesEn = PropertiesLoaderUtils.loadProperties(resource);
		    } catch (Exception e) {
		    //  logger.error(e.getMessage(), e);
		    
		    }
		  try {
		      Resource resource = new ClassPathResource("/messages_en.properties");
		      messagepropertiesEn = PropertiesLoaderUtils.loadProperties(resource);
		    } catch (Exception e) {
		      //logger.error(e.getMessage(), e);
		    }
		  
		  try {
		      Resource resource = new ClassPathResource("/resource.properties");
		      properties = PropertiesLoaderUtils.loadProperties(resource);
		    } catch (Exception e) {
		      //logger.error(e.getMessage(), e);
		    }

	  }
	  
	  public static JSONObject getJSLocaleJSON() {
		    
		      jsonOutput = getLocaleValuesJSON(null);
		    
		    return jsonOutput;
		  }
	  static JSONObject jsonOutput = new JSONObject();
	  
	  public static JSONObject getLocaleValuesJSON(String locale) {
		    JSONObject jsonOutput = new JSONObject();
		    resourceFileLoder();
		   if (locale == null || !locale.equals("")) {
		      locale = getValueOfPropByKey("locale");
		    }
		    try {
		       if (locale.equalsIgnoreCase("en")) {
		        jsonOutput.putAll(js_messagepropertiesEn);
		      }
		     

		    } catch (Exception e) {
		      //logger.error(e.getMessage(), e);
		    }

		    return jsonOutput;
		  }
	  
	  public static String getValueOfPropByKey(String key) {
		    String value = "";
		    resourceFileLoder();
		    try {
		      value = properties.getProperty(key.trim());
		    } catch (Exception e) {
		    //  logger.error(e.getMessage(), e);
		    }
		    return value;
		  }
	  public static String getLocaleValuePropByKey(String key, String locale) {
		    String value = "";
		      try {
		       if (locale.equalsIgnoreCase("en")) {
		          value = messagepropertiesEn.getProperty(key.trim());
		        }  
		      } catch (Exception e) {
		        //logger.error(e.getMessage(), e);
		      }
		     
		    return value;
		  }
	  
	 
}
