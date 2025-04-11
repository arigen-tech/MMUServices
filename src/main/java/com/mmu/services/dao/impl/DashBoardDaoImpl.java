package com.mmu.services.dao.impl;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.jdbc.Work;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mmu.services.dao.DashBoardDao;
import com.mmu.services.entity.MasStoreFinancial;
import com.mmu.services.entity.StoreInternalIndentM;
import com.mmu.services.entity.StoreInternalIndentT;
import com.mmu.services.hibernateutils.GetHibernateUtils;
import com.mmu.services.hibernateutils.GetHibernateUtilsMis;
import com.mmu.services.utils.HMSUtil;


@Repository
public class DashBoardDaoImpl implements DashBoardDao {
	String databaseScema = HMSUtil.getProperties("adt.properties", "currentSchema").trim();
	
	@Autowired
	GetHibernateUtils getHibernateUtils;
	
	@Autowired
	GetHibernateUtilsMis getHibernateUtilsNormal;
		
	@Override
	public Map<String, Object> getDashBoardData(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> map = new HashMap<>();
		
		//int userId = 2;
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();	
		JSONObject jsonObject = new JSONObject();
		session.doWork(new Work() {
			@Override
			public void execute(java.sql.Connection connection) throws SQLException {
				int districtId =0;
				int cityId =0;
				int mmuId=0;
				int p_clusterid=0;
				String fromDate="";
				String toDate="";
				JSONArray jsonArray1 = new JSONArray();				
				JSONArray jsonArray2 = new JSONArray();	
				JSONArray jsonArray3A = new JSONArray();	
				JSONArray jsonArray3B = new JSONArray();
				JSONArray jsonArray4 = new JSONArray();	
				JSONArray jsonArray4A = new JSONArray();	
				JSONArray jsonArray4B = new JSONArray();	
				JSONArray jsonArray4C = new JSONArray();	
				JSONArray jsonArray4D = new JSONArray();	
				JSONArray jsonArray4E = new JSONArray();	
				
				JSONArray jsonArray5 = new JSONArray();
				JSONArray jsonArray6 = new JSONArray();
				
				JSONArray jsonArray7A = new JSONArray();
				JSONArray jsonArray7B = new JSONArray();
				JSONArray jsonArray8 = new JSONArray();
				JSONArray jsonArray9 = new JSONArray();
				
				if(jsondata.containsKey("districtId")) {
				  districtId = Integer.parseInt(jsondata.get("districtId").toString());
				}
				
				if(jsondata.containsKey("cityId")) {
					  cityId = Integer.parseInt(jsondata.get("cityId").toString());
					}
				if(jsondata.containsKey("fromDate")) {
					  fromDate = jsondata.get("fromDate").toString();
					}
				if(jsondata.containsKey("toDate")) {
					  toDate = jsondata.get("toDate").toString();
					}				
				if(jsondata.containsKey("mmuId")) {
					mmuId = Integer.parseInt(jsondata.get("mmuId").toString());
					}
				/*
				 * if(jsondata.containsKey("clusterId")) { p_clusterid =
				 * Integer.parseInt(jsondata.get("clusterId").toString()); }
				 */
				String queryString = "SELECT asp_mmu_dashboard_1(?,?,?,?,?)";
				PreparedStatement    stmt = connection.prepareCall(queryString);
				stmt.setInt(1, districtId);
				stmt.setInt(2, cityId);	
				java.util.Date frmDate = HMSUtil.convertStringDateToUtilDateForDatabase(fromDate);
				java.sql.Date sql_frmDate = new java.sql.Date(frmDate.getTime());
				
				java.util.Date to_Date = HMSUtil.convertStringDateToUtilDateForDatabase(toDate);
				java.sql.Date sql_toDate = new java.sql.Date(to_Date.getTime());				
				try {
					stmt.setDate(3,  sql_frmDate);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				};
				try {
					stmt.setDate(4, sql_toDate);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				stmt.setInt(5, mmuId);
				//stmt.setInt(6, p_clusterid);
				
				connection.setAutoCommit(false);
				ResultSet rs = stmt.executeQuery();
				
				if (rs.next()) {
					Object o = rs.getObject(1);
					if (o instanceof ResultSet) {
						ResultSet rs1 = (ResultSet) o;
						while (rs1.next()) {
							int columnCount = rs1.getMetaData().getColumnCount();
							JSONObject jsonObj = new JSONObject();
							for (int i = 0; i < columnCount; i++) {
								jsonObj.put(rs1.getMetaData().getColumnLabel(i + 1), HMSUtil.convertNullToEmptyString(rs1.getObject(i + 1)));
							}
							jsonArray1.put(jsonObj);
						}
						jsonObject.put("dashboard_data", jsonArray1);
					}
				}
				
				
				if (rs.next()) {
					Object o = rs.getObject(1);
					if (o instanceof ResultSet) {
						ResultSet rs2 = (ResultSet) o;
						while (rs2.next()) {
							int columnCount = rs2.getMetaData().getColumnCount();
							JSONObject jsonObj = new JSONObject();
							for (int i = 0; i < columnCount; i++) {
								jsonObj.put(rs2.getMetaData().getColumnLabel(i + 1), HMSUtil.convertNullToEmptyString(rs2.getObject(i + 1)));
							}
							jsonArray2.put(jsonObj);
						}
						jsonObject.put("piechart_data", jsonArray2);
					}
				}
				
				if (rs.next()) {
					Object o = rs.getObject(1);
					if (o instanceof ResultSet) {
						ResultSet rs3A = (ResultSet) o;
						while (rs3A.next()) {
							int columnCount = rs3A.getMetaData().getColumnCount();
							JSONObject jsonObj = new JSONObject();
							for (int i = 0; i < columnCount; i++) {
								jsonObj.put(rs3A.getMetaData().getColumnLabel(i + 1), HMSUtil.convertNullToEmptyString(rs3A.getObject(i + 1)));
							}
							jsonArray3A.put(jsonObj);
						}
						jsonObject.put("opdStatistics_city_data_total", jsonArray3A);
					}
				}
				
				if (rs.next()) {
					Object o = rs.getObject(1);
					if (o instanceof ResultSet) {
						ResultSet rs3B = (ResultSet) o;
						while (rs3B.next()) {
							int columnCount = rs3B.getMetaData().getColumnCount();
							JSONObject jsonObj = new JSONObject();
							for (int i = 0; i < columnCount; i++) {
								jsonObj.put(rs3B.getMetaData().getColumnLabel(i + 1), HMSUtil.convertNullToEmptyString(rs3B.getObject(i + 1)));
							}
							jsonArray3B.put(jsonObj);
						}
						jsonObject.put("opdStatistics_city_data_ddc", jsonArray3B);
					}
				}
				
				if (rs.next()) {
					Object o = rs.getObject(1);
					if (o instanceof ResultSet) {
						ResultSet rs4 = (ResultSet) o;
						while (rs4.next()) {
							int columnCount = rs4.getMetaData().getColumnCount();
							JSONObject jsonObj = new JSONObject();
							for (int i = 0; i < columnCount; i++) {
								jsonObj.put(rs4.getMetaData().getColumnLabel(i + 1), HMSUtil.convertNullToEmptyString(rs4.getObject(i + 1)));
							}
							jsonArray4.put(jsonObj);
						}
						jsonObject.put("topDistricts_data", jsonArray4);
					}
				}
				
				if (rs.next()) {
					Object o = rs.getObject(1);
					if (o instanceof ResultSet) {
						ResultSet rs4A = (ResultSet) o;
						while (rs4A.next()) {
							int columnCount = rs4A.getMetaData().getColumnCount();
							JSONObject jsonObj = new JSONObject();
							for (int i = 0; i < columnCount; i++) {
								jsonObj.put(rs4A.getMetaData().getColumnLabel(i + 1), HMSUtil.convertNullToEmptyString(rs4A.getObject(i + 1)));
							}
							jsonArray4A.put(jsonObj);
						}
						jsonObject.put("topDistricts_data_reg", jsonArray4A);
					}
				}
				
				if (rs.next()) {
					Object o = rs.getObject(1);
					if (o instanceof ResultSet) {
						ResultSet rs4B = (ResultSet) o;
						while (rs4B.next()) {
							int columnCount = rs4B.getMetaData().getColumnCount();
							JSONObject jsonObj = new JSONObject();
							for (int i = 0; i < columnCount; i++) {
								jsonObj.put(rs4B.getMetaData().getColumnLabel(i + 1), HMSUtil.convertNullToEmptyString(rs4B.getObject(i + 1)));
							}
							jsonArray4B.put(jsonObj);
						}
						jsonObject.put("topDistricts_data_apt", jsonArray4B);
					}
				}
				
				
				if (rs.next()) {
					Object o = rs.getObject(1);
					if (o instanceof ResultSet) {
						ResultSet rs4C = (ResultSet) o;
						while (rs4C.next()) {
							int columnCount = rs4C.getMetaData().getColumnCount();
							JSONObject jsonObj = new JSONObject();
							for (int i = 0; i < columnCount; i++) {
								jsonObj.put(rs4C.getMetaData().getColumnLabel(i + 1), HMSUtil.convertNullToEmptyString(rs4C.getObject(i + 1)));
							}
							jsonArray4C.put(jsonObj);
						}
						jsonObject.put("topDistricts_data_lab", jsonArray4C);
					}
				}
				
				if (rs.next()) {
					Object o = rs.getObject(1);
					if (o instanceof ResultSet) {
						ResultSet rs4D = (ResultSet) o;
						while (rs4D.next()) {
							int columnCount = rs4D.getMetaData().getColumnCount();
							JSONObject jsonObj = new JSONObject();
							for (int i = 0; i < columnCount; i++) {
								jsonObj.put(rs4D.getMetaData().getColumnLabel(i + 1), HMSUtil.convertNullToEmptyString(rs4D.getObject(i + 1)));
							}
							jsonArray4D.put(jsonObj);
						}
						jsonObject.put("topDistricts_data_dipsn", jsonArray4D);
					}
				}
				
				
				if (rs.next()) {
					Object o = rs.getObject(1);
					if (o instanceof ResultSet) {
						ResultSet rs4E = (ResultSet) o;
						while (rs4E.next()) {
							int columnCount = rs4E.getMetaData().getColumnCount();
							JSONObject jsonObj = new JSONObject();
							for (int i = 0; i < columnCount; i++) {
								jsonObj.put(rs4E.getMetaData().getColumnLabel(i + 1), HMSUtil.convertNullToEmptyString(rs4E.getObject(i + 1)));
							}
							jsonArray4E.put(jsonObj);
						}
						jsonObject.put("topDistricts_data_medstock", jsonArray4E);
					}
				}
				
				
				if (rs.next()) {
					Object o = rs.getObject(1);
					if (o instanceof ResultSet) {
						ResultSet rs5 = (ResultSet) o;
						while (rs5.next()) {
							int columnCount = rs5.getMetaData().getColumnCount();
							JSONObject jsonObj = new JSONObject();
							for (int i = 0; i < columnCount; i++) {
								jsonObj.put(rs5.getMetaData().getColumnLabel(i + 1), HMSUtil.convertNullToEmptyString(rs5.getObject(i + 1)));
							}
							jsonArray5.put(jsonObj);
						}
						jsonObject.put("signsSymptoms_data", jsonArray5);
					}
				}
				
				if (rs.next()) {
					Object o = rs.getObject(1);
					if (o instanceof ResultSet) {
						ResultSet rs6 = (ResultSet) o;
						while (rs6.next()) {
							int columnCount = rs6.getMetaData().getColumnCount();
							JSONObject jsonObj = new JSONObject();
							for (int i = 0; i < columnCount; i++) {
								jsonObj.put(rs6.getMetaData().getColumnLabel(i + 1), HMSUtil.convertNullToEmptyString(rs6.getObject(i + 1)));
							}
							jsonArray6.put(jsonObj);
						}
						jsonObject.put("feedbackPie_data", jsonArray6);
					}
				}
				
				if (rs.next()) {							
					
					Object o = rs.getObject(1);
					if (o instanceof ResultSet) {
						ResultSet rs7A = (ResultSet) o;
						while (rs7A.next()) {
							int columnCount = rs7A.getMetaData().getColumnCount();
							JSONObject jsonObj = new JSONObject();
							for (int i = 0; i < columnCount; i++) {
								jsonObj.put(rs7A.getMetaData().getColumnLabel(i + 1), HMSUtil.convertNullToEmptyString(rs7A.getObject(i + 1)));
							}
							jsonArray7A.put(jsonObj);
						}
						jsonObject.put("opdstatistics_state_data_total", jsonArray7A);
					}
				}
				
				if (rs.next()) {							
					
					Object o = rs.getObject(1);
					if (o instanceof ResultSet) {
						ResultSet rs7B = (ResultSet) o;
						while (rs7B.next()) {
							int columnCount = rs7B.getMetaData().getColumnCount();
							JSONObject jsonObj = new JSONObject();
							for (int i = 0; i < columnCount; i++) {
								jsonObj.put(rs7B.getMetaData().getColumnLabel(i + 1), HMSUtil.convertNullToEmptyString(rs7B.getObject(i + 1)));
							}
							jsonArray7B.put(jsonObj);
						}
						jsonObject.put("opdstatistics_state_data_ddc", jsonArray7B);
					}
				}
				
				if (rs.next()) {
					Object o = rs.getObject(1);
					if (o instanceof ResultSet) {
						ResultSet rs8 = (ResultSet) o;
						while (rs8.next()) {
							int columnCount = rs8.getMetaData().getColumnCount();
							JSONObject jsonObj = new JSONObject();
							for (int i = 0; i < columnCount; i++) {
								jsonObj.put(rs8.getMetaData().getColumnLabel(i + 1), HMSUtil.convertNullToEmptyString(rs8.getObject(i + 1)));
							}
							jsonArray8.put(jsonObj);
						}
						jsonObject.put("diagnosis_data", jsonArray8);
					}
				}
				
				if (rs.next()) {
					Object o = rs.getObject(1);
					if (o instanceof ResultSet) {
						ResultSet rs9 = (ResultSet) o;
						while (rs9.next()) {
							int columnCount = rs9.getMetaData().getColumnCount();
							JSONObject jsonObj = new JSONObject();
							for (int i = 0; i < columnCount; i++) {
								jsonObj.put(rs9.getMetaData().getColumnLabel(i + 1), HMSUtil.convertNullToEmptyString(rs9.getObject(i + 1)));
							}
							jsonArray9.put(jsonObj);
						}
						jsonObject.put("communicable_data", jsonArray9);
					}
				}
				
		
			}
		});
		getHibernateUtils.getHibernateUtlis().CloseConnection();				
		map.put("dashboard_data", jsonObject);
		return map;
	}	
	
	@Override
	public Map<String, Object> getHomePageData(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> map = new HashMap<>();
		
		Session session = getHibernateUtilsNormal.getHibernateUtlisMiss().OpenSession();	
		JSONObject jsonObj1 = new JSONObject();	
		session.doWork(new Work() {
			@Override
			public void execute(java.sql.Connection connection) throws SQLException {
				
				JSONArray jsonArray1 = new JSONArray();				
				JSONArray jsonArray2 = new JSONArray();	
				JSONArray jsonArray3 = new JSONArray();				
				String queryString = "SELECT asp_mmu_det()";				
				PreparedStatement    stmt = connection.prepareCall(queryString);
				connection.setAutoCommit(false);
				ResultSet rs = stmt.executeQuery();
				
				if (rs.next()) {
					Object o = rs.getObject(1);
					if (o instanceof ResultSet) {
						ResultSet rs1 = (ResultSet) o;
						while (rs1.next()) {
							int columnCount = rs1.getMetaData().getColumnCount();
							JSONObject jsonObj = new JSONObject();
							for (int i = 0; i < columnCount; i++) {
								jsonObj.put(rs1.getMetaData().getColumnLabel(i + 1), HMSUtil.convertNullToEmptyString(rs1.getObject(i + 1)));
							}
							jsonArray1.put(jsonObj);
						}
					}
				}
				
				if (rs.next()) {
					Object o = rs.getObject(1);
					if (o instanceof ResultSet) {
						ResultSet rs2 = (ResultSet) o;
						while (rs2.next()) {
							int columnCount = rs2.getMetaData().getColumnCount();
							JSONObject jsonObj = new JSONObject();
							for (int i = 0; i < columnCount; i++) {
								jsonObj.put(rs2.getMetaData().getColumnLabel(i + 1), HMSUtil.convertNullToEmptyString(rs2.getObject(i + 1)));
							}
							jsonArray2.put(jsonObj);
						}
						
					}
				}
				
				if (rs.next()) {
					Object o = rs.getObject(1);
					if (o instanceof ResultSet) {
						ResultSet rs3 = (ResultSet) o;
						while (rs3.next()) {
							int columnCount = rs3.getMetaData().getColumnCount();
							JSONObject jsonObj = new JSONObject();
							for (int i = 0; i < columnCount; i++) {
								jsonObj.put(rs3.getMetaData().getColumnLabel(i + 1), HMSUtil.convertNullToEmptyString(rs3.getObject(i + 1)));
							}
							jsonArray2.put(jsonObj);
						}
						
					}
				}
				
				if (rs.next()) {
					Object o = rs.getObject(1);
					if (o instanceof ResultSet) {
						ResultSet rs4 = (ResultSet) o;
						while (rs4.next()) {
							int columnCount = rs4.getMetaData().getColumnCount();
							JSONObject jsonObj = new JSONObject();
							for (int i = 0; i < columnCount; i++) {
								jsonObj.put(rs4.getMetaData().getColumnLabel(i + 1), HMSUtil.convertNullToEmptyString(rs4.getObject(i + 1)));
							}
							jsonArray2.put(jsonObj);
						}
						
					}
				}
				
				if (rs.next()) {
					Object o = rs.getObject(1);
					if (o instanceof ResultSet) {
						ResultSet rs5 = (ResultSet) o;
						while (rs5.next()) {
							int columnCount = rs5.getMetaData().getColumnCount();
							JSONObject jsonObj = new JSONObject();
							for (int i = 0; i < columnCount; i++) {
								jsonObj.put(rs5.getMetaData().getColumnLabel(i + 1), HMSUtil.convertNullToEmptyString(rs5.getObject(i + 1)));
							}
							jsonArray2.put(jsonObj);
						}
						
					}
				}
				
				if (rs.next()) {
					Object o = rs.getObject(1);
					if (o instanceof ResultSet) {
						ResultSet rs6 = (ResultSet) o;
						while (rs6.next()) {
							int columnCount = rs6.getMetaData().getColumnCount();
							JSONObject jsonObj = new JSONObject();
							for (int i = 0; i < columnCount; i++) {
								jsonObj.put(rs6.getMetaData().getColumnLabel(i + 1), HMSUtil.convertNullToEmptyString(rs6.getObject(i + 1)));
							}
							jsonArray2.put(jsonObj);
						}
						
					}
				}
				
				if (rs.next()) {
					Object o = rs.getObject(1);
					if (o instanceof ResultSet) {
						ResultSet rs7 = (ResultSet) o;
						while (rs7.next()) {
							int columnCount = rs7.getMetaData().getColumnCount();
							JSONObject jsonObj = new JSONObject();
							for (int i = 0; i < columnCount; i++) {
								jsonObj.put(rs7.getMetaData().getColumnLabel(i + 1), HMSUtil.convertNullToEmptyString(rs7.getObject(i + 1)));
							}
							jsonArray2.put(jsonObj);
						}
						
					}
				}
				
				if (rs.next()) {
					Object o = rs.getObject(1);
					if (o instanceof ResultSet) {
						ResultSet rs8 = (ResultSet) o;
						while (rs8.next()) {
							int columnCount = rs8.getMetaData().getColumnCount();
							JSONObject jsonObj = new JSONObject();
							for (int i = 0; i < columnCount; i++) {
								jsonObj.put(rs8.getMetaData().getColumnLabel(i + 1), HMSUtil.convertNullToEmptyString(rs8.getObject(i + 1)));
							}
							jsonArray2.put(jsonObj);
						}
						
					}
					jsonObj1.put("staticData", jsonArray2);
				}
				
				if (rs.next()) {
					Object o = rs.getObject(1);
					if (o instanceof ResultSet) {
						ResultSet rs9 = (ResultSet) o;
						while (rs9.next()) {
							int columnCount = rs9.getMetaData().getColumnCount();
							JSONObject jsonObj = new JSONObject();
							for (int i = 0; i < columnCount; i++) {
								jsonObj.put(rs9.getMetaData().getColumnLabel(i + 1), HMSUtil.convertNullToEmptyString(rs9.getObject(i + 1)));
							
							}
							jsonArray3.put(jsonObj);
						}
						
					}
				}
							
				jsonObj1.put("campLocation_data", jsonArray1);
				jsonObj1.put("comdisease_data", jsonArray3);
				
			}
		});
		getHibernateUtils.getHibernateUtlis().CloseConnection();				
		map.put("homepage_data", jsonObj1);
		return map;
	}

	public Map<String, Object> getPandemicZoneData(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> map = new HashMap<>();
		
		//int userId = 2;
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();	
		JSONObject jsonObject = new JSONObject();
		session.doWork(new Work() {
			@Override
			public void execute(java.sql.Connection connection) throws SQLException {
				
				int mmuId=1;
				String toDate="";
				String fromDate="";
				JSONArray jsonArray1 = new JSONArray();				
				JSONArray jsonArray2 = new JSONArray();	
				
				
				
				if(jsondata.containsKey("mmuId")) {
					mmuId = 0;
					}
				if(jsondata.containsKey("toDate")) {
					toDate = jsondata.get("toDate").toString();
					}
				java.util.Date tDate = HMSUtil.convertStringDateToUtilDateForDatabase(toDate);
				java.sql.Date sql_toDate = new java.sql.Date(tDate.getTime());
				if(jsondata.containsKey("fromDate")) {
					fromDate = jsondata.get("fromDate").toString();
					
					}
				java.util.Date frmDate = HMSUtil.convertStringDateToUtilDateForDatabase(fromDate);
				java.sql.Date sql_frmDate = new java.sql.Date(frmDate.getTime());
				String queryString = "SELECT asp_pandemic_cases_info(?,?,?)";				
				PreparedStatement    stmt = connection.prepareCall(queryString);
				stmt.setInt(1, mmuId);
				stmt.setDate(2, sql_frmDate);
				stmt.setDate(3,sql_toDate );
				connection.setAutoCommit(false);
				ResultSet rs = stmt.executeQuery();
				
				if (rs.next()) {
					Object o = rs.getObject(1);
					if (o instanceof ResultSet) {
						ResultSet rs1 = (ResultSet) o;
						while (rs1.next()) {
							int columnCount = rs1.getMetaData().getColumnCount();
							JSONObject jsonObj = new JSONObject();
							for (int i = 0; i < columnCount; i++) {
								jsonObj.put(rs1.getMetaData().getColumnLabel(i + 1), HMSUtil.convertNullToEmptyString(rs1.getObject(i + 1)));
							}
							jsonArray1.put(jsonObj);
						}
						jsonObject.put("pandemicCasesInfo", jsonArray1);
					}
				  }
			
			    }
		   });
				getHibernateUtils.getHibernateUtlis().CloseConnection();
				map.put("pandemic_info", jsonObject);
				return map;
		}

	public Map<String, Object> getInvoiceData(HashMap<String, Object> jsondata,HttpServletResponse response) {
		Map<String,Object> map = new HashMap<>();
		
	
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();	
		JSONObject jsonObject = new JSONObject();
		session.doWork(new Work() {
			@Override
			public void execute(java.sql.Connection connection) throws SQLException {
				
				int mmuId=1;
				String toDate="";
				String fromDate="";
				String mmuOrCity="";
				String phase=null;
				String distIdVal=null;
				String levelOfUser=null;
				JSONArray jsonArray1 = new JSONArray();				
				JSONArray jsonArray2 = new JSONArray();	
				
				
				if(jsondata.containsKey("mmuCity")) {
					mmuOrCity = jsondata.get("mmuCity").toString();
					}
				if(jsondata.containsKey("mmuId")) {
					mmuId = 0;
					}
				if(jsondata.containsKey("toDate")) {
					toDate = jsondata.get("toDate").toString();
					}
				java.util.Date tDate = HMSUtil.convertStringDateToUtilDateForDatabase(toDate);
				java.sql.Date sql_toDate = new java.sql.Date(tDate.getTime());
				if(jsondata.containsKey("fromDate")) {
					fromDate = jsondata.get("fromDate").toString();
					}
				if(jsondata.containsKey("phase")) {
					phase = jsondata.get("phase").toString();
					if(phase.equalsIgnoreCase("")) {
						phase="0";
					}
				}
				if(jsondata.containsKey("distIdVal")) {
					distIdVal = jsondata.get("distIdVal").toString();
				}
				if(jsondata.containsKey("levelOfUser")) {
					levelOfUser = jsondata.get("levelOfUser").toString();
				}
				java.util.Date frmDate = HMSUtil.convertStringDateToUtilDateForDatabase(fromDate);
				java.sql.Date sql_frmDate = new java.sql.Date(frmDate.getTime());
				String queryString = "SELECT asp_invoice_dashboard(?,?,?,?,?,?)";				
				PreparedStatement    stmt = connection.prepareCall(queryString);
				
				stmt.setDate(1, sql_frmDate);
				stmt.setDate(2,sql_toDate );
				stmt.setString(3,mmuOrCity);
	    		stmt.setString(4,distIdVal);
				stmt.setString(5,levelOfUser);
				stmt.setString(6,phase);
				connection.setAutoCommit(false);
				ResultSet rs = stmt.executeQuery();
				
				if (rs.next()) {
					Object o = rs.getObject(1);
					if (o instanceof ResultSet) {
						ResultSet rs1 = (ResultSet) o;
						while (rs1.next()) {
							int columnCount = rs1.getMetaData().getColumnCount();
							JSONObject jsonObj = new JSONObject();
							for (int i = 0; i < columnCount; i++) {
								jsonObj.put(rs1.getMetaData().getColumnLabel(i + 1), HMSUtil.convertNullToEmptyString(rs1.getObject(i + 1)));
							}
							jsonArray1.put(jsonObj);
						}
						jsonObject.put("invoiceDataInfo", jsonArray1);
					}
				  }
			
			    }
		   });
				getHibernateUtils.getHibernateUtlis().CloseConnection();
				map.put("pandemic_info", jsonObject);
				return map;
		}
	public Map<String, Object> getMedicineInvoiceData(HashMap<String, Object> jsondata, HttpServletResponse response) {
		Map<String,Object> map = new HashMap<>();
		
	
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();	
		JSONObject jsonObject = new JSONObject();
		session.doWork(new Work() {
			@Override
			public void execute(java.sql.Connection connection) throws SQLException {
				
				int mmuId=1;
				String toDate="";
				String fromDate="";
				String mmuOrCity="";
				String flagType = "T";
				String phase="";
				JSONArray jsonArray1 = new JSONArray();				
				
				
				if(jsondata.containsKey("mmuCity")) {
					mmuOrCity = jsondata.get("mmuCity").toString();
					}
				if(jsondata.containsKey("upss_id")) {
					mmuId = Integer.valueOf(jsondata.get("upss_id").toString());
					}
				if(jsondata.containsKey("toDate")) {
					toDate = jsondata.get("toDate").toString();
					}
				if(jsondata.containsKey("flagType")) {
					flagType = jsondata.get("flagType").toString();
					}
				java.util.Date tDate = HMSUtil.convertStringDateToUtilDateForDatabase(toDate);
				java.sql.Date sql_toDate = new java.sql.Date(tDate.getTime());
				if(jsondata.containsKey("fromDate")) {
					fromDate = jsondata.get("fromDate").toString();
					}
				if(jsondata.containsKey("phase")) {
					phase = jsondata.get("phase").toString();
					if(phase.equalsIgnoreCase("")) {
						phase="0";
					}
				}
				java.util.Date frmDate = HMSUtil.convertStringDateToUtilDateForDatabase(fromDate);
				java.sql.Date sql_frmDate = new java.sql.Date(frmDate.getTime());
				String queryString = "SELECT asp_medical_invoice_dashboard(?,?,?,?,?)";				
				PreparedStatement    stmt = connection.prepareCall(queryString);
				
				stmt.setDate(1, sql_frmDate);
				stmt.setDate(2,sql_toDate );
				stmt.setString(3,mmuOrCity);
				stmt.setInt(4,mmuId);
				stmt.setString(5,phase);
				
				connection.setAutoCommit(false);
				ResultSet rs = stmt.executeQuery();
				
				if (rs.next()) {
					Object o = rs.getObject(1);
					if (o instanceof ResultSet) {
						ResultSet rs1 = (ResultSet) o;
						while (rs1.next()) {
							int columnCount = rs1.getMetaData().getColumnCount();
							JSONObject jsonObj = new JSONObject();
							for (int i = 0; i < columnCount; i++) {
								jsonObj.put(rs1.getMetaData().getColumnLabel(i + 1), HMSUtil.convertNullToEmptyString(rs1.getObject(i + 1)));
							}
							jsonArray1.put(jsonObj);
						}
						jsonObject.put("invoiceDataInfo", jsonArray1);
					}
				  }
			
			    }
		   });
				getHibernateUtils.getHibernateUtlis().CloseConnection();
				map.put("pandemic_info", jsonObject);
				return map;
		}
	public Map<String, Object> getUpssInvoiceData(HashMap<String, Object> jsondata, HttpServletResponse response) {
		Map<String,Object> map = new HashMap<>();
		
	
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();	
		JSONObject jsonObject = new JSONObject();
		session.doWork(new Work() {
			@Override
			public void execute(java.sql.Connection connection) throws SQLException {
				
				int mmuId=1;
				String toDate="";
				String fromDate="";
				String mmuOrCity="";
				String flagType = "T";
				String phase=null;
				String distIdVal=null;
				String levelOfUser=null;
				JSONArray jsonArray1 = new JSONArray();				
				
				
				if(jsondata.containsKey("mmuCity")) {
					mmuOrCity = jsondata.get("mmuCity").toString();
					}
				if(jsondata.containsKey("upss_id")) {
					mmuId = Integer.valueOf(jsondata.get("upss_id").toString());
					}
				if(jsondata.containsKey("toDate")) {
					toDate = jsondata.get("toDate").toString();
					}
				if(jsondata.containsKey("flagType")) {
					flagType = jsondata.get("flagType").toString();
					}
				java.util.Date tDate = HMSUtil.convertStringDateToUtilDateForDatabase(toDate);
				java.sql.Date sql_toDate = new java.sql.Date(tDate.getTime());
				if(jsondata.containsKey("fromDate")) {
					fromDate = jsondata.get("fromDate").toString();
					}
				if(jsondata.containsKey("phase")) {
					phase = jsondata.get("phase").toString();
					if(phase.equalsIgnoreCase("")) {
						phase="0";
					}
				}
				if(jsondata.containsKey("distIdVal")) {
					distIdVal = jsondata.get("distIdVal").toString();
				}
				if(jsondata.containsKey("levelOfUser")) {
					levelOfUser = jsondata.get("levelOfUser").toString();
				}
				java.util.Date frmDate = HMSUtil.convertStringDateToUtilDateForDatabase(fromDate);
				java.sql.Date sql_frmDate = new java.sql.Date(frmDate.getTime());
				String queryString = "SELECT asp_invoice_dashboard_detail(?,?,?,?,?,?,?,?)";				
				PreparedStatement    stmt = connection.prepareCall(queryString);
				
				stmt.setDate(1, sql_frmDate);
				stmt.setDate(2,sql_toDate );
				stmt.setString(3,mmuOrCity);
				stmt.setString(4,flagType);
				stmt.setInt(5,mmuId);
				//stmt.setString(6,phase);
				stmt.setString(6,distIdVal);
				stmt.setString(7,levelOfUser);
				stmt.setString(8,phase);
				connection.setAutoCommit(false);
				ResultSet rs = stmt.executeQuery();
				
				if (rs.next()) {
					Object o = rs.getObject(1);
					if (o instanceof ResultSet) {
						ResultSet rs1 = (ResultSet) o;
						while (rs1.next()) {
							int columnCount = rs1.getMetaData().getColumnCount();
							JSONObject jsonObj = new JSONObject();
							for (int i = 0; i < columnCount; i++) {
								jsonObj.put(rs1.getMetaData().getColumnLabel(i + 1), HMSUtil.convertNullToEmptyString(rs1.getObject(i + 1)));
							}
							jsonArray1.put(jsonObj);
						}
						jsonObject.put("invoiceDataInfo", jsonArray1);
					}
				  }
			
			    }
		   });
				getHibernateUtils.getHibernateUtlis().CloseConnection();
				map.put("upss_data_info", jsonObject);
				return map;
		}
	
	public Map<String, Object> getAuthorityWiseStatus(HashMap<String, Object> jsondata, HttpServletResponse response) {
		Map<String,Object> map = new HashMap<>();
		
	
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();	
		JSONObject jsonObject = new JSONObject();
		session.doWork(new Work() {
			@Override
			public void execute(java.sql.Connection connection) throws SQLException {
				
				int mmuId=1;
				String toDate="";
				String fromDate="";
				String mmuOrCity="";
				String flagType = "T";
				JSONArray jsonArray1 = new JSONArray();	
				String authId = "";
				
				
				if(jsondata.containsKey("authId")) {
					authId = jsondata.get("authId").toString();
				}
		
				String queryString = "SELECT asp_Authority_wise_status(?)";				
				PreparedStatement    stmt = connection.prepareCall(queryString);
				
				stmt.setInt(1, Integer.valueOf(authId));
				
				connection.setAutoCommit(false);
				ResultSet rs = stmt.executeQuery();
				
				if (rs.next()) {
					Object o = rs.getObject(1);
					if (o instanceof ResultSet) {
						ResultSet rs1 = (ResultSet) o;
						while (rs1.next()) {
							int columnCount = rs1.getMetaData().getColumnCount();
							JSONObject jsonObj = new JSONObject();
							for (int i = 0; i < columnCount; i++) {
								jsonObj.put(rs1.getMetaData().getColumnLabel(i + 1), HMSUtil.convertNullToEmptyString(rs1.getObject(i + 1)));
							}
							jsonArray1.put(jsonObj);
						}
						jsonObject.put("authorityWiseStatus", jsonArray1);
					}
				  }
			
			    }
		   });
				getHibernateUtils.getHibernateUtlis().CloseConnection();
				map.put("authority_wise_status", jsonObject);
				return map;
		}
	
	public Map<String, Object> getAuditDashBoardData(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
Map<String,Object> map = new HashMap<>();
		
		//int userId = 2;
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();	
		JSONObject jsonObject = new JSONObject();
		session.doWork(new Work() {
			@Override
			public void execute(java.sql.Connection connection) throws SQLException {
				int districtId =0;
				int cityId =0;
				int mmuId=0;
				int p_clusterid=0;
				String fromDate="";
				String toDate="";
				JSONArray jsonArray1 = new JSONArray();				
				JSONArray jsonArray2 = new JSONArray();	
				JSONArray jsonArray3A = new JSONArray();	
				JSONArray jsonArray3B = new JSONArray();
				JSONArray jsonArray4 = new JSONArray();	
				JSONArray jsonArray4A = new JSONArray();	
				JSONArray jsonArray4B = new JSONArray();	
				JSONArray jsonArray4C = new JSONArray();	
				JSONArray jsonArray4D = new JSONArray();	
				JSONArray jsonArray4E = new JSONArray();	
				
				JSONArray jsonArray5 = new JSONArray();
				JSONArray jsonArray6 = new JSONArray();
				
				JSONArray jsonArray7A = new JSONArray();
				JSONArray jsonArray7B = new JSONArray();
				JSONArray jsonArray8 = new JSONArray();
				JSONArray jsonArray9 = new JSONArray();
				
				if(jsondata.containsKey("districtId")) {
				  districtId = Integer.parseInt(jsondata.get("districtId").toString());
				}
				
				if(jsondata.containsKey("cityId")) {
					  cityId = Integer.parseInt(jsondata.get("cityId").toString());
					}
								
				if(jsondata.containsKey("mmuId")) {
					mmuId = Integer.parseInt(jsondata.get("mmuId").toString());
					}
				if(jsondata.containsKey("fromDate")) {
					  fromDate = jsondata.get("fromDate").toString();
					}
				if(jsondata.containsKey("toDate")) {
					  toDate = jsondata.get("toDate").toString();
					}
				/*
				 * if(jsondata.containsKey("clusterId")) { p_clusterid =
				 * Integer.parseInt(jsondata.get("clusterId").toString()); }
				 */
				String queryString = "SELECT asp_mmu_audit_dashboard(?,?,?,?,?)";
				PreparedStatement    stmt = connection.prepareCall(queryString);
				stmt.setInt(1, districtId);
				stmt.setInt(2, cityId);
				stmt.setInt(3, mmuId);
				java.util.Date frmDate = HMSUtil.convertStringDateToUtilDateForDatabase(fromDate);
				java.sql.Date sql_frmDate = new java.sql.Date(frmDate.getTime());
				
				java.util.Date to_Date = HMSUtil.convertStringDateToUtilDateForDatabase(toDate);
				java.sql.Date sql_toDate = new java.sql.Date(to_Date.getTime());				
				try {
					stmt.setDate(4,  sql_frmDate);
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				};
				try {
					stmt.setDate(5, sql_toDate);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//stmt.setInt(6, p_clusterid);
				
				connection.setAutoCommit(false);
				ResultSet rs = stmt.executeQuery();
				
				if (rs.next()) {
					
					Object o = rs.getObject(1);
					
					if (o instanceof ResultSet) {
						ResultSet rs1 = (ResultSet) o;
						while (rs1.next()) {
							int columnCount = rs1.getMetaData().getColumnCount();
							JSONObject jsonObj = new JSONObject();
							for (int i = 0; i < columnCount; i++) {
								jsonObj.put(rs1.getMetaData().getColumnLabel(i + 1), HMSUtil.convertNullToEmptyString(rs1.getObject(i + 1)));
							}
							jsonArray1.put(jsonObj);
						}
						System.out.println("Object:"+jsonArray1);
						jsonObject.put("dashboard_data", jsonArray1);
					}
				}
				
				
				if (rs.next()) {
					Object o = rs.getObject(1);
					if (o instanceof ResultSet) {
						ResultSet rs2 = (ResultSet) o;
						while (rs2.next()) {
							int columnCount = rs2.getMetaData().getColumnCount();
							JSONObject jsonObj = new JSONObject();
							for (int i = 0; i < columnCount; i++) {
								jsonObj.put(rs2.getMetaData().getColumnLabel(i + 1), HMSUtil.convertNullToEmptyString(rs2.getObject(i + 1)));
							}
							jsonArray2.put(jsonObj);
						}
						System.out.println("Object2:"+jsonArray2);
						jsonObject.put("piechart_data", jsonArray2);
					}
				}
				
				if (rs.next()) {
					Object o = rs.getObject(1);
					if (o instanceof ResultSet) {
						ResultSet rs3A = (ResultSet) o;
						while (rs3A.next()) {
							int columnCount = rs3A.getMetaData().getColumnCount();
							JSONObject jsonObj = new JSONObject();
							for (int i = 0; i < columnCount; i++) {
								jsonObj.put(rs3A.getMetaData().getColumnLabel(i + 1), HMSUtil.convertNullToEmptyString(rs3A.getObject(i + 1)));
							}
							jsonArray3A.put(jsonObj);
						}
						System.out.println("Object3A:"+jsonArray3A);
						jsonObject.put("opdStatistics_city_data_total", jsonArray3A);
					}
				}
				
				if (rs.next()) {
					Object o = rs.getObject(1);
					if (o instanceof ResultSet) {
						ResultSet rs3B = (ResultSet) o;
						while (rs3B.next()) {
							int columnCount = rs3B.getMetaData().getColumnCount();
							JSONObject jsonObj = new JSONObject();
							for (int i = 0; i < columnCount; i++) {
								jsonObj.put(rs3B.getMetaData().getColumnLabel(i + 1), HMSUtil.convertNullToEmptyString(rs3B.getObject(i + 1)));
							}
							jsonArray3B.put(jsonObj);
						}
						System.out.println("Object3B:"+jsonArray3B);
						jsonObject.put("opdStatistics_city_data_ddc", jsonArray3B);
					}
				}
				
				if (rs.next()) {
					Object o = rs.getObject(1);
					if (o instanceof ResultSet) {
						ResultSet rs4 = (ResultSet) o;
						while (rs4.next()) {
							int columnCount = rs4.getMetaData().getColumnCount();
							JSONObject jsonObj = new JSONObject();
							for (int i = 0; i < columnCount; i++) {
								jsonObj.put(rs4.getMetaData().getColumnLabel(i + 1), HMSUtil.convertNullToEmptyString(rs4.getObject(i + 1)));
							}
							jsonArray4.put(jsonObj);
						}
						System.out.println("Object4:"+jsonArray4);
						jsonObject.put("topDistricts_data", jsonArray4);
					}
				}
				
				if (rs.next()) {
					Object o = rs.getObject(1);
					if (o instanceof ResultSet) {
						ResultSet rs4A = (ResultSet) o;
						while (rs4A.next()) {
							int columnCount = rs4A.getMetaData().getColumnCount();
							JSONObject jsonObj = new JSONObject();
							for (int i = 0; i < columnCount; i++) {
								jsonObj.put(rs4A.getMetaData().getColumnLabel(i + 1), HMSUtil.convertNullToEmptyString(rs4A.getObject(i + 1)));
							}
							jsonArray4A.put(jsonObj);
						}
						System.out.println("Object4A:"+jsonArray4A);
						jsonObject.put("topDistricts_data_reg", jsonArray4A);
					}
				}
				
				if (rs.next()) {
					Object o = rs.getObject(1);
					if (o instanceof ResultSet) {
						ResultSet rs4B = (ResultSet) o;
						while (rs4B.next()) {
							int columnCount = rs4B.getMetaData().getColumnCount();
							JSONObject jsonObj = new JSONObject();
							for (int i = 0; i < columnCount; i++) {
								jsonObj.put(rs4B.getMetaData().getColumnLabel(i + 1), HMSUtil.convertNullToEmptyString(rs4B.getObject(i + 1)));
							}
							jsonArray4B.put(jsonObj);
						}
						System.out.println("Object4B:"+jsonArray4B);
						jsonObject.put("topDistricts_data_apt", jsonArray4B);
					}
				}
				
				
				if (rs.next()) {
					Object o = rs.getObject(1);
					if (o instanceof ResultSet) {
						ResultSet rs4C = (ResultSet) o;
						while (rs4C.next()) {
							int columnCount = rs4C.getMetaData().getColumnCount();
							JSONObject jsonObj = new JSONObject();
							for (int i = 0; i < columnCount; i++) {
								jsonObj.put(rs4C.getMetaData().getColumnLabel(i + 1), HMSUtil.convertNullToEmptyString(rs4C.getObject(i + 1)));
							}
							jsonArray4C.put(jsonObj);
						}
						jsonObject.put("topDistricts_data_lab", jsonArray4C);
					}
				}
				
				if (rs.next()) {
					Object o = rs.getObject(1);
					if (o instanceof ResultSet) {
						ResultSet rs4D = (ResultSet) o;
						while (rs4D.next()) {
							int columnCount = rs4D.getMetaData().getColumnCount();
							JSONObject jsonObj = new JSONObject();
							for (int i = 0; i < columnCount; i++) {
								jsonObj.put(rs4D.getMetaData().getColumnLabel(i + 1), HMSUtil.convertNullToEmptyString(rs4D.getObject(i + 1)));
							}
							jsonArray4D.put(jsonObj);
						}
						jsonObject.put("topDistricts_data_dipsn", jsonArray4D);
					}
				}
				
				
				if (rs.next()) {
					Object o = rs.getObject(1);
					if (o instanceof ResultSet) {
						ResultSet rs4E = (ResultSet) o;
						while (rs4E.next()) {
							int columnCount = rs4E.getMetaData().getColumnCount();
							JSONObject jsonObj = new JSONObject();
							for (int i = 0; i < columnCount; i++) {
								jsonObj.put(rs4E.getMetaData().getColumnLabel(i + 1), HMSUtil.convertNullToEmptyString(rs4E.getObject(i + 1)));
							}
							jsonArray4E.put(jsonObj);
						}
						jsonObject.put("topDistricts_data_medstock", jsonArray4E);
					}
				}
				
				
				if (rs.next()) {
					Object o = rs.getObject(1);
					if (o instanceof ResultSet) {
						ResultSet rs5 = (ResultSet) o;
						while (rs5.next()) {
							int columnCount = rs5.getMetaData().getColumnCount();
							JSONObject jsonObj = new JSONObject();
							for (int i = 0; i < columnCount; i++) {
								jsonObj.put(rs5.getMetaData().getColumnLabel(i + 1), HMSUtil.convertNullToEmptyString(rs5.getObject(i + 1)));
							}
							jsonArray5.put(jsonObj);
						}
						jsonObject.put("signsSymptoms_data", jsonArray5);
					}
				}
				
				if (rs.next()) {
					Object o = rs.getObject(1);
					if (o instanceof ResultSet) {
						ResultSet rs6 = (ResultSet) o;
						while (rs6.next()) {
							int columnCount = rs6.getMetaData().getColumnCount();
							JSONObject jsonObj = new JSONObject();
							for (int i = 0; i < columnCount; i++) {
								jsonObj.put(rs6.getMetaData().getColumnLabel(i + 1), HMSUtil.convertNullToEmptyString(rs6.getObject(i + 1)));
							}
							jsonArray6.put(jsonObj);
						}
						jsonObject.put("feedbackPie_data", jsonArray6);
					}
				}
				
				if (rs.next()) {							
					
					Object o = rs.getObject(1);
					if (o instanceof ResultSet) {
						ResultSet rs7A = (ResultSet) o;
						while (rs7A.next()) {
							int columnCount = rs7A.getMetaData().getColumnCount();
							JSONObject jsonObj = new JSONObject();
							for (int i = 0; i < columnCount; i++) {
								jsonObj.put(rs7A.getMetaData().getColumnLabel(i + 1), HMSUtil.convertNullToEmptyString(rs7A.getObject(i + 1)));
							}
							jsonArray7A.put(jsonObj);
						}
						jsonObject.put("opdstatistics_state_data_total", jsonArray7A);
					}
				}
				
				if (rs.next()) {							
					
					Object o = rs.getObject(1);
					if (o instanceof ResultSet) {
						ResultSet rs7B = (ResultSet) o;
						while (rs7B.next()) {
							int columnCount = rs7B.getMetaData().getColumnCount();
							JSONObject jsonObj = new JSONObject();
							for (int i = 0; i < columnCount; i++) {
								jsonObj.put(rs7B.getMetaData().getColumnLabel(i + 1), HMSUtil.convertNullToEmptyString(rs7B.getObject(i + 1)));
							}
							jsonArray7B.put(jsonObj);
						}
						jsonObject.put("opdstatistics_state_data_ddc", jsonArray7B);
					}
				}
				
				if (rs.next()) {
					Object o = rs.getObject(1);
					if (o instanceof ResultSet) {
						ResultSet rs8 = (ResultSet) o;
						while (rs8.next()) {
							int columnCount = rs8.getMetaData().getColumnCount();
							JSONObject jsonObj = new JSONObject();
							for (int i = 0; i < columnCount; i++) {
								jsonObj.put(rs8.getMetaData().getColumnLabel(i + 1), HMSUtil.convertNullToEmptyString(rs8.getObject(i + 1)));
							}
							jsonArray8.put(jsonObj);
						}
						jsonObject.put("diagnosis_data", jsonArray8);
					}
				}
				
				if (rs.next()) {
					Object o = rs.getObject(1);
					if (o instanceof ResultSet) {
						ResultSet rs9 = (ResultSet) o;
						while (rs9.next()) {
							int columnCount = rs9.getMetaData().getColumnCount();
							JSONObject jsonObj = new JSONObject();
							for (int i = 0; i < columnCount; i++) {
								jsonObj.put(rs9.getMetaData().getColumnLabel(i + 1), HMSUtil.convertNullToEmptyString(rs9.getObject(i + 1)));
							}
							jsonArray9.put(jsonObj);
						}
						jsonObject.put("communicable_data", jsonArray9);
					}
				}
				
		
			}
		});
		getHibernateUtils.getHibernateUtlis().CloseConnection();				
		map.put("dashboard_data", jsonObject);
		return map;
	}



	public Map<String, Object> getAuditDashBoardAuditorData(HashMap<String, Object> jsondata,
			HttpServletRequest request, HttpServletResponse response) {
	     Map<String,Object> map = new HashMap<>();
			
			//int userId = 2;
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();	
			JSONObject jsonObject = new JSONObject();
			session.doWork(new Work() {
				@Override
				public void execute(java.sql.Connection connection) throws SQLException {
					int auditorId =0;
					String fromDateWithTime="";
					String pstime ="";
					String petime ="";
					String startdate="";
					String endDate="";
					JSONArray jsonArray1 = new JSONArray();				
					JSONArray jsonArray2 = new JSONArray();	
					JSONArray jsonArray3A = new JSONArray();	
					JSONArray jsonArray3B = new JSONArray();
					JSONArray jsonArray4 = new JSONArray();	
					JSONArray jsonArray4A = new JSONArray();	
					JSONArray jsonArray4B = new JSONArray();	
					JSONArray jsonArray4C = new JSONArray();	
					JSONArray jsonArray4D = new JSONArray();	
					JSONArray jsonArray4E = new JSONArray();	
					
					JSONArray jsonArray5 = new JSONArray();
					JSONArray jsonArray6 = new JSONArray();
					
					JSONArray jsonArray7A = new JSONArray();
					JSONArray jsonArray7B = new JSONArray();
					JSONArray jsonArray8 = new JSONArray();
					JSONArray jsonArray9 = new JSONArray();
					
					if(jsondata.containsKey("auditorId")) {
						auditorId = Integer.parseInt(jsondata.get("auditorId").toString());
					}
					
					
     				if(jsondata.containsKey("starttime")) {
     					pstime = jsondata.get("starttime").toString();
     				}
     				if(jsondata.containsKey("endtime")) {
     					petime = jsondata.get("endtime").toString();
     				}
					if(jsondata.containsKey("startDate")) {
						startdate = jsondata.get("startDate").toString();
						System.out.println("startdate"+startdate);
						}
					
					if(jsondata.containsKey("endDate")) {
						  endDate = jsondata.get("endDate").toString();
							System.out.println("endDate"+endDate);
						}
					/*
					 * if(jsondata.containsKey("clusterId")) { p_clusterid =
					 * Integer.parseInt(jsondata.get("clusterId").toString()); }
					 */
					System.out.println("auditorId" + auditorId+"starttime"+pstime+ "endtime"+petime + "startDate" + startdate+"endDate" + endDate);
					String queryString = "SELECT asp_mmu_audit_status_dashboard(?,?,?,?,?)";
					PreparedStatement    stmt = connection.prepareCall(queryString);
					stmt.setInt(1, auditorId);
					/*java.util.Date frmDate = HMSUtil.convertStringDateToUtilDateForDatabase(fromDateWithTime);
					java.sql.Date sql_frmDate = new java.sql.Date(frmDate.getTime());
					try {
						stmt.setDate(2,  sql_frmDate);
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					};*/
					stmt.setString(2, pstime);
					
					
					stmt.setString(3, petime);
					
					java.util.Date frmDate1 = HMSUtil.convertStringDateToUtilDateForDatabase(startdate);
					java.sql.Date sql_frmDate1 = new java.sql.Date(frmDate1.getTime());
					
					java.util.Date to_Date1 = HMSUtil.convertStringDateToUtilDateForDatabase(endDate);
					java.sql.Date sql_toDate1 = new java.sql.Date(to_Date1.getTime());				
					try {
						stmt.setDate(4,  sql_frmDate1);
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					};
					try {
						stmt.setDate(5, sql_toDate1);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					
					//connection
					connection.setAutoCommit(false);
					ResultSet rs = stmt.executeQuery();
					
					if (rs.next()) {
						
						Object o = rs.getObject(1);
						
						if (o instanceof ResultSet) {
							ResultSet rs1 = (ResultSet) o;
							while (rs1.next()) {
								int columnCount = rs1.getMetaData().getColumnCount();
								JSONObject jsonObj = new JSONObject();
								for (int i = 0; i < columnCount; i++) {
									jsonObj.put(rs1.getMetaData().getColumnLabel(i + 1), HMSUtil.convertNullToEmptyString(rs1.getObject(i + 1)));
								}
								jsonArray1.put(jsonObj);
							}
							System.out.println("Object:"+jsonArray1);
							jsonObject.put("dashboard_auditor_data", jsonArray1);
						}
					}
					
					
					
					
					
					if (rs.next()) {
						Object o = rs.getObject(1);
						if (o instanceof ResultSet) {
							ResultSet rs2 = (ResultSet) o;
							while (rs2.next()) {
								int columnCount = rs2.getMetaData().getColumnCount();
								JSONObject jsonObj = new JSONObject();
								for (int i = 0; i < columnCount; i++) {
									jsonObj.put(rs2.getMetaData().getColumnLabel(i + 1), HMSUtil.convertNullToEmptyString(rs2.getObject(i + 1)));
								}
								jsonArray2.put(jsonObj);
							}
							System.out.println("Object2:"+jsonArray2);
							jsonObject.put("bar_data", jsonArray2);
							jsonObject.put("communicable_data", jsonArray9);
						}
					}
					
			
				}
			});
			getHibernateUtils.getHibernateUtlis().CloseConnection();				
			map.put("dashboard_auditor_data", jsonObject);
			return map;
	}
	public Map<String, Object> getMMUMecineInvoiceData(HashMap<String, Object> jsondata, HttpServletResponse response) {
		Map<String,Object> map = new HashMap<>();
		
	
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();	
		JSONObject jsonObject = new JSONObject();
		try {
		session.doWork(new Work() {
			@Override
			public void execute(java.sql.Connection connection) throws SQLException {
				
				int districtId=0;
				int cityId = 0;
				String toDate="";
				String fromDate="";
				String mmuOrCity="";
				
				JSONArray jsonArray1 = new JSONArray();				
				
				
				if(jsondata.containsKey("mmuCity")) {
					mmuOrCity = jsondata.get("mmuCity").toString();
					}
				if(jsondata.containsKey("upss_id")) {
					districtId = Integer.valueOf(jsondata.get("upss_id").toString());
					}
				if(jsondata.containsKey("toDate")) {
					toDate = jsondata.get("toDate").toString();
				}
				if(mmuOrCity.equalsIgnoreCase("U")) {
					cityId=0;
				}else {
					cityId=districtId;
					districtId=0;
				}
			
				java.util.Date tDate = HMSUtil.convertStringDateToUtilDateForDatabase(toDate);
				java.sql.Date sql_toDate = new java.sql.Date(tDate.getTime());
				if(jsondata.containsKey("fromDate")) {
					fromDate = jsondata.get("fromDate").toString();
					}
				java.util.Date frmDate = HMSUtil.convertStringDateToUtilDateForDatabase(fromDate);
				java.sql.Date sql_frmDate = new java.sql.Date(frmDate.getTime());
				String queryString = "SELECT asp_invoice_track(?,?,?,?)";				
				PreparedStatement    stmt = connection.prepareCall(queryString);
				
				stmt.setDate(1, sql_frmDate);
				stmt.setDate(2,sql_toDate );
				stmt.setInt(3,districtId);
				stmt.setInt(4,cityId);
				
				connection.setAutoCommit(false);
				ResultSet rs = stmt.executeQuery();
				
				if (rs.next()) {
					Object o = rs.getObject(1);
					if (o instanceof ResultSet) {
						ResultSet rs1 = (ResultSet) o;
						while (rs1.next()) {
							int columnCount = rs1.getMetaData().getColumnCount();
							JSONObject jsonObj = new JSONObject();
							for (int i = 0; i < columnCount; i++) {
								jsonObj.put(rs1.getMetaData().getColumnLabel(i + 1), HMSUtil.convertNullToEmptyString(rs1.getObject(i + 1)));
							}
							jsonArray1.put(jsonObj);
						}
						jsonObject.put("mmuInvoiceDataInfo", jsonArray1);
					}
				  }
			
			    }
		   });
		}
		catch(Exception exce) {
			exce.printStackTrace();
		}
		finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
				
				map.put("upss_data_info", jsonObject);
				return map;
		}
	
	
	 
	@Override
	public Map<String, List<StoreInternalIndentT>> getAllStoreInternalIndentT(JSONObject jsondata) {
		Map<String, List<StoreInternalIndentT>> map = new HashMap<String, List<StoreInternalIndentT>>();
		List<StoreInternalIndentT> masHeadList = new ArrayList<StoreInternalIndentT>();
		List totalMatches  =new ArrayList<>();
		int pageNo=1;
		int pageSize = 5;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(StoreInternalIndentT.class).createAlias("storeInternalIndentM1", "storeInternalIndentM1") ;
			 if (jsondata.has("mmuId"))
			 {
				 Long mmuId= Long.parseLong(jsondata.get("mmuId").toString());
				 criteria = criteria.add(Restrictions.eq("storeInternalIndentM1.mmuId", mmuId));
			 }
			 
			   
			 Date fromDate =null;
				Date toDate =null;
				 if(jsondata.has("fromDate"))
			     {		 
								 
					 	fromDate = HMSUtil.convertStringTypeDateToDateType(String.valueOf(jsondata.get("fromDate")));
					 	toDate = HMSUtil.convertStringTypeDateToDateType(String.valueOf(jsondata.get("toDate")));
						Calendar c = Calendar.getInstance();
						c.setTime(fromDate);
						//c.add(Calendar.DATE, 6);
						c.setTime(toDate);
						toDate = c.getTime();
						 criteria = criteria.add(Restrictions.between("storeInternalIndentM1.demandDate", fromDate, toDate));
			     }
				

			 
				 
						
			  
			 
			if(!jsondata.get("PN").toString().equals("0")) {
			 pageNo = Integer.parseInt(jsondata.get("PN").toString());
					
			String searchApprovingAuthorityName="";
				 /*if (jsondata.has("searchFinancialYear"))
				 {
					 searchApprovingAuthorityName =  "%"+jsondata.get("searchFinancialYear").toString()+"%";
					  if( 
							  !jsondata.get("searchFinancialYear").toString().trim().equalsIgnoreCase("")) {
							criteria.add(Restrictions.ilike("financialYear",  searchApprovingAuthorityName));
							
						}
				 }
				  criteria.addOrder(Order.asc("financialId"));
					*/ 
			  	criteria.addOrder(Order.desc("storeInternalIndentM1.demandDate"));
				 totalMatches = criteria.list();				 
				 criteria.setFirstResult((pageSize) * (pageNo - 1));
				 criteria.setMaxResults(pageSize);
				 masHeadList = criteria.list();
			}
			
			 if(jsondata.get("PN").toString().equals("0")) {
				// criteria.addOrder(Order.asc("financialId"));
				 //criteria.add(Restrictions.eq("status", "y").ignoreCase());
				 criteria.addOrder(Order.desc("storeInternalIndentM1.demandDate"));
				 totalMatches = criteria.list();				 
				 masHeadList = criteria.list();
			 }
		 
		map.put("masFinancialYearList", masHeadList);
		map.put("totalMatches", totalMatches);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}
	

}
