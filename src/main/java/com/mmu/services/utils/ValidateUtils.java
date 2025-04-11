package com.mmu.services.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

public class ValidateUtils {

	public static JSONObject getmaster_detail_validate(HashMap<String, Object> master_detail) {
		JSONObject jsonvalidator = new JSONObject();
		if (!master_detail.containsKey("terminalid") || master_detail.get("terminalid") == null
				|| master_detail.get("terminalid").toString().trim().equalsIgnoreCase("")) {
			jsonvalidator.put("status", "0");
			jsonvalidator.put("msg", "terminalid is not contain in json");
			return jsonvalidator;

		} else if (!master_detail.containsKey("date1") || master_detail.get("date1") == null
				|| master_detail.get("date1").toString().trim().equalsIgnoreCase("")) {
			jsonvalidator.put("status", "0");
			jsonvalidator.put("msg", "date1 is not contain in json");
			return jsonvalidator;
		} else if (!master_detail.containsKey("date2") || master_detail.get("date2") == null
				|| master_detail.get("date2").toString().trim().equalsIgnoreCase("")) {
			jsonvalidator.put("status", "0");
			jsonvalidator.put("msg", "date2 is not contain in json");
			return jsonvalidator;
		}
		return null;
	}

	// public static JSONObject getCrm_addDetails_validate(Object data) {
	// HashMap<String, String> jsonData = (HashMap<String, String>) data;
	// return function(jsonData, new String[] { "cmobile", "cname", "status",
	// "email", "doorno", "pincode", "gstin",
	// "gstinstate", "state", "country", "terminal_id", "shop_id", "admin_id" });
	// }

	public static JSONObject getCrm_addDetails_validate(Object data) {
		HashMap<String, String> jsonData = (HashMap<String, String>) data;
		return functionNew(jsonData, new String[] { "cmobile", "cname" });
	}

	public static JSONObject addProductValidation(Object data) {
		HashMap<String, String> jsonData = (HashMap<String, String>) data;
		return function(jsonData, new String[] { "Product_Id", "shop_id", "admin_id" });

	}

	public static JSONObject addAdvancePayValidation(Object data) {
		HashMap<String, String> jsonData = (HashMap<String, String>) data;
		return function(jsonData, new String[] { "admin_id", "terminal_id", "cust_name", "mob_no", "shop_id",
				"total_amount", "adv_amount", "adv_amount_date" });
	}

	public static JSONObject addInventoryValidation(Object data) {
		HashMap<String, String> jsonData = (HashMap<String, String>) data;
		return function(jsonData,
				new String[] { "Product_Id", "terminal_id", "discountstatus", "Category_Id", "Product_Name", "Rate",
						"tax_active", "Low_quantity", "Available_Quantity", "Description", "Image_Name",
						"Category_Name", "Brand", "shop_id" });

	}

	private static JSONObject function(HashMap<String, String> jsonData, String[] strings) {
		for (int i = 0; i < strings.length; i++) {
			boolean b = jsonData.containsKey(strings[i]) && jsonData.get(strings[i]) != null
					&& !(jsonData.get(strings[i]).toString().equals(""));
			if (b == false) {
				return ErrorDiscriptions.getReturnMsg(
						"jsondata is not contain" + " " + strings[i] + " " + "as a  key or value or it is null", "0");
			}
			if (strings[i].equals("cmobile") && jsonData.get(strings[i]).length() != 10) {

				return ErrorDiscriptions.getReturnMsg(" " + strings[i] + " " + "value must contain 10 digits.", "0");
			}
		}
		return ErrorDiscriptions.getReturnMsg("validated data found.", "1");
	}

	private static JSONObject functionNew(HashMap<String, String> jsonData, String[] strings) {
		List<String> list = new ArrayList<String>();
		List<String> list2 = new ArrayList<String>();
		String str = "";

		for (int i = 0; i < strings.length; i++) {

			if (!(jsonData.containsKey(strings[i]))) {
				list.add(strings[i]);
			} else if (jsonData.get(strings[i]) == null || jsonData.get(strings[i]).equals("")) {
				list2.add(strings[i]);
			}

		}
		if (list.size() > 0) {
			str = "not contain as key" + list;
		}
		if (list2.size() > 0) {
			str = str.concat("value is null or empty for the keys" + list2);
		}
		if (list.size() > 0 || list2.size() > 0) {
			return ErrorDiscriptions.getReturnMsg(str, "0");
		} else {
			return ErrorDiscriptions.getReturnMsg("jsondata is validated.", "1");
		}
	}

	public static JSONObject getPaymentHeaderVali(HashMap<String, Object> single) {
		return getJsonKeyNullAndBlankCheckfunction(single, new String[] { "payment_list" });
	}

	private static JSONObject getJsonKeyNullAndBlankCheckfunction(HashMap<String, Object> jsonData, String[] strings) {
		for (int i = 0; i < strings.length; i++) {
			boolean b = jsonData.containsKey(strings[i]) && jsonData.get(strings[i]) != null
					&& !(jsonData.get(strings[i]).toString().equals(""));
			if (b == false) {
				return ErrorDiscriptions.getReturnMsg(
						"jsondata is not contain" + " " + strings[i] + " " + "as a  key or it is null", "0");
			}
		}
		return ErrorDiscriptions.getReturnMsg("validated data found.", "1");
	}

	private static JSONObject getJsonKeyNullAndBlankCheck(HashMap<String, String> jsonData, String[] strings) {
		for (int i = 0; i < strings.length; i++) {
			boolean b = jsonData.containsKey(strings[i]) && jsonData.get(strings[i]) != null
					&& !(jsonData.get(strings[i]).toString().equals(""));
			if (b == false) {
				return ErrorDiscriptions.getReturnMsg(
						"jsondata is not contain" + " " + strings[i] + " " + "as a  key or value or it is null", "0");
			}
		}
		return ErrorDiscriptions.getReturnMsg("validated data found.", "1");
	}

	public static JSONObject get_valid_shop_value(HashMap<String, String> jsondata) {
		return function(jsondata, new String[] { "shop_id", "terminal_id" });
	}

	// all the new methods make by santosh bharti

	public static JSONObject getAddrequsitionVali(HashMap<String, Object> single) {
		return getJsonKeyNullAndBlankCheckfunction(single, new String[] { "admin_id", "requsition_date",
				"from_terminal_Id", "requsition_no", "to_shopid", "from_shopid", "to_shopid", "itms" });
	}

	public static JSONObject getPoReciveVali(HashMap<String, Object> single) {
		return getJsonKeyNullAndBlankCheckfunction(single,
				new String[] { "admin_id", "po_no", "porecivestatus", "total_tax", "total_po_amount", "items" });
	}

	public static JSONObject validatePoItemsRecive(HashMap<String, Object> payload) {
		return getJsonKeyNullAndBlankCheckfunction(payload,
				new String[] { "product_id", "item_recive_qty", "item_recive_qty_units", "item_mrp",
						"item_purchase_price", "item_sales_price", "item_tax", "total_item_tax", "total_amount",
						"item_recive_status" });
	}

	public static JSONObject getSalesEodByAdminVali(HashMap<String, Object> single) {
		return getJsonKeyNullAndBlankCheckfunction(single, new String[] { "admin_id", "from_date", "to_date" });
	}

	public static JSONObject validateReqItems(HashMap<String, Object> payload) {
		return getJsonKeyNullAndBlankCheckfunction(payload,
				new String[] { "product_id", "product_name", "requried_qty", "requried_product_units" });
	}

	public static JSONObject getrequsitionVali(HashMap<String, Object> jsonData) {
		return getJsonKeyNullAndBlankCheckfunction(jsonData,
				new String[] { "reko_status", "to_shop_id", "admin_id", "fromdate", "todate" });
	}

	public static JSONObject getAllRekoVali(HashMap<String, Object> jsonData) {
		return getJsonKeyNullAndBlankCheckfunction(jsonData,
				new String[] { "to_shop_or_admin_id", "admin_id", "fromdate", "todate" });
	}

	public static JSONObject getrequsitionFromShopVali(HashMap<String, Object> jsonData) {
		return getJsonKeyNullAndBlankCheckfunction(jsonData,
				new String[] { "reko_status", "from_shop_id", "admin_id", "fromdate", "todate" });
	}

	public static JSONObject stockTransfarToShopVali(HashMap<String, Object> single) {
		return getJsonKeyNullAndBlankCheckfunction(single,
				new String[] { "requsition_no", "admin_id", "allocated_by", "allocated_by_id", "items" });
	}

	public static JSONObject getLodedRekoVali(HashMap<String, Object> single) {
		return getJsonKeyNullAndBlankCheckfunction(single, new String[] { "shop_id", "admin_id" });
	}

	//
	public static JSONObject stockTransfarToShopItemsVali(HashMap<String, Object> payload) {
		return getJsonKeyNullAndBlankCheckfunction(payload,
				new String[] { "product_id", "release_qty", "release_product_units", "requried_release_status" });
	}

	public static JSONObject getAddStockMainJsonvalidate(HashMap<String, Object> single) {
		return getJsonKeyNullAndBlankCheckfunction(single, new String[] { "stocklist" });
	}

	public static JSONObject getAddStockJsonNullAndBlankValidation(HashMap<String, String> jsondata) {
		return getJsonKeyNullAndBlankCheck(jsondata,
				new String[] { "Product_Id", "Product_Name", "qty", "product_price", "product_units" });// "Available_Quantity",
	}

	public static JSONObject getCatListByAdmin(HashMap<String, Object> single) {
		return getJsonKeyNullAndBlankCheckfunction(single, new String[] { "admin_id" });
	}

	public static JSONObject getSubCatListByAdminforShopStockMaster(HashMap<String, Object> single) {
		return getJsonKeyNullAndBlankCheckfunction(single, new String[] { "admin_id", "shop_id", "cat_id" });
	}

	public static JSONObject getSubCatListByAdmin(HashMap<String, Object> single) {
		return getJsonKeyNullAndBlankCheckfunction(single, new String[] { "admin_id", "cat_id" });
	}

	public static JSONObject getCatByStockvalidation(HashMap<String, Object> single) {
		return getJsonKeyNullAndBlankCheckfunction(single, new String[] { "admin_id", "product_cat_id" });
	}

	public static JSONObject getSubCatByShopMasterStockvalidation(HashMap<String, Object> single) {
		return getJsonKeyNullAndBlankCheckfunction(single,
				new String[] { "admin_id", "shop_id", "product_cat_id", "sub_cat_id" });
	}

	public static JSONObject getSubCatByStockvalidation(HashMap<String, Object> single) {
		return getJsonKeyNullAndBlankCheckfunction(single, new String[] { "admin_id", "product_cat_id", "sub_cat_id" });
	}

	public static JSONObject getproductIdByStockvalidation(HashMap<String, Object> single) {
		return getJsonKeyNullAndBlankCheckfunction(single, new String[] { "admin_id", "product_id" });
	}

	public static JSONObject getwhStockLogs(HashMap<String, Object> single) {
		return getJsonKeyNullAndBlankCheckfunction(single, new String[] { "admin_id", "from_date", "to_date" });
	}

	public static JSONObject getReciveRekoVali(HashMap<String, Object> single) {
		return getJsonKeyNullAndBlankCheckfunction(single, new String[] { "reko_recive_date", "admin_id",
				"requsition_no", "accept_reject_status", "reko_recive_by", "reko_recive_by_id", "itms" });
	}

	public static JSONObject getReciveRekoItemVali(HashMap<String, Object> single) {
		return getJsonKeyNullAndBlankCheckfunction(single, new String[] { "product_recived_status", "product_id",
				"requried_qty", "requried_product_units", "release_qty", "release_product_units" });
	}

	public static JSONObject getSubCatByItemsVali(HashMap<String, String> single) {
		return function(single, new String[] { "adminorshopid", "cat_id", "sub_cat_id", "from_date", "to_date" });
	}

	public static JSONObject testshop(Object jsonData) {
		HashMap<String, String> hashMap = (HashMap<String, String>) jsonData;
		return functionNew(hashMap, new String[] { "admin_id", "shop_id", "productentrydate" });
	}

	public static JSONObject testUpdateStock(HashMap<String, String> map) {
		return functionNew(map,
				new String[] { "brand", "category_name", "category_id", "sub_category_name", "sub_category_id",
						"product_name", "product_id", "p_description", "tax_active", "tax_rate", "hsn", "mrp", "units",
						"product_barcode" });

	}

	public static JSONObject getpayreport(Object jsondata) {
		HashMap single = (HashMap) jsondata;
		return functionNew(single, new String[] { "orderGenerated_by", "orderGenerated_by_id", "fromdate", "todate" });
	}

	public static JSONObject vehicleRegisteration(HashMap<String, String> jsondata) {
		return   functionNew(jsondata, new String[] {"vehicle_no", "vehicle_name", "driver_name", "driver_email","driver_mobile","driver_aadhaar","admin_id"});
	}
	
	
	
	
	
	public static JSONObject adminValidate(HashMap<String, String> jsondata) {
		return functionNew(jsondata, new String[] {"admin_id"});
		}

	public static JSONObject damageReport(Object jsonData) {
		HashMap<String,String> single = (HashMap) jsonData;
		return functionNew(single, new String[] {"request_type","Shop_id_receiver","email_id_receiver","stock_transfer_id","date_current","from_shop_id","from_terminal_id","admin_id"});		
	}

	public static JSONObject damagedata(HashMap<String, String> hashMaps) {
		return functionNew(hashMaps, new String[] {"product_name","product_id","received_qty","return_qty"});		
	
	}

	public static JSONObject getShopByShopInvoice(Object JSONObject) {
		HashMap<String,String> single = (HashMap) JSONObject;
		return functionNew(single, new String[] {"adminid","date","date2"});		
	}

	public static JSONObject getReturnProduct(Object jsonData) {	
		HashMap<String,String> single = (HashMap) jsonData;
	return functionNew(single, new String[] {"adminid","date1","date2"});
	}


//////////////////// Check Null Validation Method for json date: 14/01/2019//////////////////////
	
public static JSONObject addVitalPreConsulataionDetailsvalidation(HashMap<String, Object> single) {
	return getJsonKeyNullAndBlankCheckfunction2(single, new String[] {"patientId","visitId"});
}

public static JSONObject addAuthorityDetails(HashMap<String, Object> single) {
	return getJsonKeyNullAndBlankCheckfunction2(single, new String[] {"authorityId","remarks","captureVendorBillDetailId","actionId",
			"authorityOrderNo","authorityRoleName","authorityName"});
}

public static JSONObject addANMEntryDetails(HashMap<String, Object> single) {
	return getJsonKeyNullAndBlankCheckfunction2(single, new String[] {"cityId","mmuId","patientNoApplyLabourRegistration","patientNoLabTest",
			"patientNoLabourRegistredDepartment","patientNoMedicineDispensed","totalNoPatients"});
}

public static JSONObject checkIdealWeight(HashMap<String, String> single) {
	return getJsonKeyNullAndBlankCheckfunction3(single, new String[] {"height","age"});
}

public static JSONObject checkPatientVisit(HashMap<String, String> single) {
	return getJsonKeyNullAndBlankCheckfunction3(single, new String[] {"visitId"});
}
public static JSONObject checkPatientRecord(HashMap<String, String> single) {
	return getJsonKeyNullAndBlankCheckfunction3(single, new String[] {"patientId"});
}

public static JSONObject checkEmployee(HashMap<String, String> single) {
	return getJsonKeyNullAndBlankCheckfunction3(single, new String[] {"employeeId"});
}

public static JSONObject checkPatientVisitId(HashMap<String, Object> single) {
	return getJsonKeyNullAndBlankCheckfunctionHMS(single, new String[] {"visitId"});
}

public static JSONObject checkPatientId(HashMap<String, Object> single) {
	return getJsonKeyNullAndBlankCheckfunctionHMS(single, new String[] {"patientId"});
}

private static JSONObject getJsonKeyNullAndBlankCheckfunction2(HashMap<String, Object> jsonData, String[] strings) {
	for (int i = 0; i < strings.length; i++) {
		boolean b = jsonData.containsKey(strings[i]) && jsonData.get(strings[i]) != null
				&& !(jsonData.get(strings[i]).toString().equals(""));
		if (b == false) {
			return ErrorDiscriptions.getReturnMsg(
					"jsondata is not contain" + " " + strings[i] + " " + "as a  key or it is null", "0");
		}
	}
	return ErrorDiscriptions.getReturnMsg("validated data found.", "1");
}

private static JSONObject getJsonKeyNullAndBlankCheckfunction3(HashMap<String, String> jsonData, String[] strings) {
	for (int i = 0; i < strings.length; i++) {
		boolean b = jsonData.containsKey(strings[i]) && jsonData.get(strings[i]) != null
				&& !(jsonData.get(strings[i]).toString().equals(""));
		if (b == false) {
			return ErrorDiscriptions.getReturnMsg(
					"jsondata is not contain" + " " + strings[i] + " " + "as a  key or it is null", "0");
		}
	}
	return ErrorDiscriptions.getReturnMsg("validated data found.", "1");
}

private static JSONObject getJsonKeyNullAndBlankCheckfunctionHMS(HashMap<String, Object> jsonData, String[] strings) {
	for (int i = 0; i < strings.length; i++) {
		boolean b = jsonData.containsKey(strings[i]) && jsonData.get(strings[i]) != null
				&& !(jsonData.get(strings[i]).toString().equals(""));
		if (b == false) {
			return ErrorDiscriptions.getReturnMsg(
					"jsondata is not contain" + " " + strings[i] + " " + "as a  key or it is null", "0");
		}
	}
	return ErrorDiscriptions.getReturnMsg("validated data found.", "1");
}
}


