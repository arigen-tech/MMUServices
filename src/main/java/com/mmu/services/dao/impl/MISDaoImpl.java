package com.mmu.services.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mmu.services.dao.MISDao;
import com.mmu.services.hibernateutils.GetHibernateUtils;
import com.mmu.services.hibernateutils.GetHibernateUtilsMis;
import com.mmu.services.utils.HMSUtil;

import antlr.StringUtils;


@Repository
public class MISDaoImpl implements MISDao {
	String databaseScema = HMSUtil.getProperties("adt.properties", "currentSchema").trim();
	
	@Autowired
	GetHibernateUtils getHibernateUtils;
	
	/*@Autowired
	GetHibernateUtilsMis getHibernateUtilsMis;*/
		
	@Override
	public Map<String, Object> getDaiDidiClinicData(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> map = new HashMap<>();
				
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();	
		JSONObject jsonObject = new JSONObject();
		session.doWork(new Work() {
			@Override
			public void execute(java.sql.Connection connection) throws SQLException {
				int cityId =0;
				String campDate="";
				JSONArray jsonArray1 = new JSONArray();				
				
				if(jsondata.containsKey("cityId")) {
					  cityId = Integer.parseInt(jsondata.get("cityId").toString());
					}
				if(jsondata.containsKey("campDate")) {
					  campDate = jsondata.get("campDate").toString();
					}
							
				String queryString = "SELECT asp_dai_clinic_register(?,?)";				
				PreparedStatement    stmt = connection.prepareCall(queryString);
				
				stmt.setInt(2, cityId);
				
				java.util.Date camp_Date = HMSUtil.convertStringDateToUtilDateForDatabase(campDate);
				java.sql.Date sql_campDate = new java.sql.Date(camp_Date.getTime());
					
				try {
					stmt.setDate(1, sql_campDate);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
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
						jsonObject.put("daiDidiClinic_data", jsonArray1);
					}
				}
				
					
			}
		});
		jsonObject.put("campDate", jsondata.get("campDate").toString());				
		map.put("daiDidiClinic_data", jsonObject);
		return map;
	}
	
	
	@Override
	public Map<String, Object> getLabourBeneficiaryData(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> map = new HashMap<>();
				
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();	
		JSONObject jsonObject = new JSONObject();
		session.doWork(new Work() {
			@Override
			public void execute(java.sql.Connection connection) throws SQLException {
				int districtId =0;
				String campDate="";
				JSONArray jsonArray1 = new JSONArray();				
				
				if(jsondata.containsKey("districtId")) {
					districtId = Integer.parseInt(jsondata.get("districtId").toString());
					}
				if(jsondata.containsKey("campDate")) {
					  campDate = jsondata.get("campDate").toString();
					}
							
				String queryString = "SELECT asp_labour_register(?,?)";				
				PreparedStatement    stmt = connection.prepareCall(queryString);
				
				stmt.setInt(2, districtId);
				
				java.util.Date camp_Date = HMSUtil.convertStringDateToUtilDateForDatabase(campDate);
				java.sql.Date sql_campDate = new java.sql.Date(camp_Date.getTime());
					
				try {
					stmt.setDate(1, sql_campDate);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
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
						jsonObject.put("labourBeneficiary_data", jsonArray1);
					}
				}
				 					
			}
		});
		jsonObject.put("campDate", jsondata.get("campDate").toString());	
		map.put("labourBeneficiary_data", jsonObject);
		return map;
	}
	
	
	@Override
	public Map<String, Object> getMMSSYInfoData(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> map = new HashMap<>();
				
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();	
		JSONObject jsonObject = new JSONObject();
		session.doWork(new Work() {
			@Override
			public void execute(java.sql.Connection connection) throws SQLException {
				int districtId =0;
				String campDate="";
				JSONArray jsonArray1 = new JSONArray();				
				
				if(jsondata.containsKey("districtId")) {
					districtId = Integer.parseInt(jsondata.get("districtId").toString());
					}
				if(jsondata.containsKey("campDate")) {
					  campDate = jsondata.get("campDate").toString();
					}
							
				String queryString = "SELECT asp_mmssy_info_register(?,?)";				
				PreparedStatement    stmt = connection.prepareCall(queryString);
				
				stmt.setInt(2, districtId);
				
				java.util.Date camp_Date = HMSUtil.convertStringDateToUtilDateForDatabase(campDate);
				java.sql.Date sql_campDate = new java.sql.Date(camp_Date.getTime());
					
				try {
					stmt.setDate(1, sql_campDate);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
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
						jsonObject.put("mmssyInfo_data", jsonArray1);
					}
				}
				
					
			}
		});
		jsonObject.put("campDate", jsondata.get("campDate").toString());			
		map.put("mmssyInfo_data", jsonObject);
		return map;
	}
	
	@Override
	public Map<String, Object> getDaiDidiClinicData2(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> map = new HashMap<>();
				
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();	
		JSONObject jsonObject = new JSONObject();
		session.doWork(new Work() {
			@Override
			public void execute(java.sql.Connection connection) throws SQLException {
				int cityId =0;
				String fromDate="";
				String toDate="";
				JSONArray jsonArray1 = new JSONArray();				
				
				if(jsondata.containsKey("cityId")) {
					  cityId = Integer.parseInt(jsondata.get("cityId").toString());
					}
				if(jsondata.containsKey("fromDate")) {
					  fromDate = jsondata.get("fromDate").toString();
					}
				if(jsondata.containsKey("toDate")) {
					  toDate = jsondata.get("toDate").toString();
					}
							
				String queryString = "SELECT asp_dai_clinic_register_range(?,?,?)";				
				PreparedStatement    stmt = connection.prepareCall(queryString);
				
				stmt.setInt(3, cityId);
				
				java.util.Date frmDate = HMSUtil.convertStringDateToUtilDateForDatabase(fromDate);
				java.sql.Date sql_frmDate = new java.sql.Date(frmDate.getTime());
				
				java.util.Date to_Date = HMSUtil.convertStringDateToUtilDateForDatabase(toDate);
				java.sql.Date sql_toDate = new java.sql.Date(to_Date.getTime());
					
				try {
					stmt.setDate(1, sql_frmDate);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					stmt.setDate(2, sql_toDate);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
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
						jsonObject.put("daiDidiClinic_data", jsonArray1);
					}
				}
				
					
			}
		});
		jsonObject.put("fromDate", jsondata.get("fromDate").toString());
		jsonObject.put("toDate", jsondata.get("toDate").toString());	
		map.put("daiDidiClinic_data", jsonObject);
		return map;
	}

	@Override
	public Map<String, Object> getLabourBeneficiaryData2(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> map = new HashMap<>();
				
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();	
		JSONObject jsonObject = new JSONObject();
		session.doWork(new Work() {
			@Override
			public void execute(java.sql.Connection connection) throws SQLException {
				int districtId =0;
				String fromDate="";
				String toDate="";
				JSONArray jsonArray1 = new JSONArray();				
				
				if(jsondata.containsKey("districtId")) {
					districtId = Integer.parseInt(jsondata.get("districtId").toString());
					}
				if(jsondata.containsKey("fromDate")) {
					  fromDate = jsondata.get("fromDate").toString();
					}
				if(jsondata.containsKey("toDate")) {
					  toDate = jsondata.get("toDate").toString();
					}
							
				String queryString = "SELECT asp_labour_register_range(?,?,?)";				
				PreparedStatement    stmt = connection.prepareCall(queryString);
				
				stmt.setInt(3, districtId);
				
				java.util.Date frmDate = HMSUtil.convertStringDateToUtilDateForDatabase(fromDate);
				java.sql.Date sql_frmDate = new java.sql.Date(frmDate.getTime());
				
				java.util.Date to_Date = HMSUtil.convertStringDateToUtilDateForDatabase(toDate);
				java.sql.Date sql_toDate = new java.sql.Date(to_Date.getTime());
					
				try {
					stmt.setDate(1, sql_frmDate);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					stmt.setDate(2, sql_toDate);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
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
						jsonObject.put("labourBeneficiary_data", jsonArray1);
					}
				}
				
					
			}
		});
		jsonObject.put("fromDate", jsondata.get("fromDate").toString());	
		map.put("labourBeneficiary_data", jsonObject);
		return map;
	}

	@Override
	public Map<String, Object> getMMSSYInfoData2(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> map = new HashMap<>();
				
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();	
		JSONObject jsonObject = new JSONObject();
		session.doWork(new Work() {
			@Override
			public void execute(java.sql.Connection connection) throws SQLException {
				int districtId =0;
				String fromDate="";
				String toDate="";
				JSONArray jsonArray1 = new JSONArray();				
				
				if(jsondata.containsKey("districtId")) {
					districtId = Integer.parseInt(jsondata.get("districtId").toString());
					}
				if(jsondata.containsKey("fromDate")) {
					  fromDate = jsondata.get("fromDate").toString();
					}
				if(jsondata.containsKey("toDate")) {
					  toDate = jsondata.get("toDate").toString();
					}
							
				String queryString = "SELECT asp_mmssy_info_register_range(?,?,?)";				
				PreparedStatement    stmt = connection.prepareCall(queryString);
				
				stmt.setInt(3, districtId);
				
				java.util.Date frmDate = HMSUtil.convertStringDateToUtilDateForDatabase(fromDate);
				java.sql.Date sql_frmDate = new java.sql.Date(frmDate.getTime());
				
				java.util.Date to_Date = HMSUtil.convertStringDateToUtilDateForDatabase(toDate);
				java.sql.Date sql_toDate = new java.sql.Date(to_Date.getTime());
					
				try {
					stmt.setDate(1, sql_frmDate);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					stmt.setDate(2, sql_toDate);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
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
						jsonObject.put("mmssyInfo_data", jsonArray1);
					}
				}
				
					
			}
		});
		jsonObject.put("fromDate", jsondata.get("fromDate").toString());
		jsonObject.put("toDate", jsondata.get("toDate").toString());			
		map.put("mmssyInfo_data", jsonObject);
		return map;
	}

	@Override
	public Map<String, Object> aiAuditReport(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> map = new HashMap<>();
				
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();	
		JSONObject jsonObject = new JSONObject();
		session.doWork(new Work() {
			@Override
			public void execute(java.sql.Connection connection) throws SQLException {
				int mmuId =0;
				String fromDate="";
				String toDate="";
				JSONArray jsonArray1 = new JSONArray();				
				
				if(jsondata.containsKey("mmuId")) {
					mmuId = Integer.parseInt(jsondata.get("mmuId").toString());
					}
				if(jsondata.containsKey("fromDate")) {
					  fromDate = jsondata.get("fromDate").toString();
					}
				if(jsondata.containsKey("toDate")) {
					  toDate = jsondata.get("toDate").toString();
					}
							
				String queryString = "SELECT asp_audit_register(?,?,?)";				
				PreparedStatement    stmt = connection.prepareCall(queryString);
				
				stmt.setInt(3, mmuId);
				
				java.util.Date frmDate = HMSUtil.convertStringDateToUtilDateForDatabase(fromDate);
				java.sql.Date sql_frmDate = new java.sql.Date(frmDate.getTime());
				
				java.util.Date to_Date = HMSUtil.convertStringDateToUtilDateForDatabase(toDate);
				java.sql.Date sql_toDate = new java.sql.Date(to_Date.getTime());
					
				try {
					stmt.setDate(1, sql_frmDate);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					stmt.setDate(2, sql_toDate);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
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
						jsonObject.put("aiAuditReport_data", jsonArray1);
					}
				}
				
					
			}
		});
		jsonObject.put("fromDate", jsondata.get("fromDate").toString());
		jsonObject.put("toDate", jsondata.get("toDate").toString());
		jsonObject.put("mmuName", jsondata.get("mmuName").toString());
		map.put("aiAuditReport_data", jsonObject);
		return map;
	}

	@Override
	public Map<String, Object> getdlReportData(HashMap<String, Object> jsondata, HttpServletRequest request,
											   HttpServletResponse response) {
		Map<String,Object> map = new HashMap<>();

		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		JSONObject jsonObject = new JSONObject();
		session.doWork(new Work() {
			@Override
			public void execute(java.sql.Connection connection) throws SQLException {
				int p_mmu_id  =0;
				int p_city_id  =0;
				int p_cluster_id  =0;
				String asondate ="";
				JSONArray jsonArray1 = new JSONArray();

				if(jsondata.containsKey("clusterId")) {
					p_cluster_id = Integer.parseInt(jsondata.get("clusterId").toString());
				}
				if(jsondata.containsKey("mmuId")) {
					p_mmu_id = Integer.parseInt(jsondata.get("mmuId").toString());
				}
				if(jsondata.containsKey("cityId")) {
					p_city_id = Integer.parseInt(jsondata.get("cityId").toString());
				}
				if(jsondata.containsKey("asondate")) {
					asondate = jsondata.get("asondate").toString();
				}

				String queryString = "SELECT edl_report(?,?,?)";
				PreparedStatement    stmt = connection.prepareCall(queryString);

				stmt.setInt(1, p_cluster_id);
				stmt.setInt(2, p_city_id);
				stmt.setInt(3, p_mmu_id);
				

				java.util.Date asondate_Date = HMSUtil.convertStringDateToUtilDateForDatabase(asondate);
				java.sql.Date sql_asondate = new java.sql.Date(asondate_Date.getTime());

				try {
					//stmt.setDate(4, sql_asondate);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

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
						jsonObject.put("edlReport_data", jsonArray1);
					}
				}


			}
		});
		jsonObject.put("asondate", jsondata.get("asondate").toString());
		map.put("edlReport_data", jsonObject);
		return map;
	}

	@Override
	public Map<String, Object> getLabReportData(HashMap<String, Object> jsondata, HttpServletRequest request,
												HttpServletResponse response) {
		Map<String,Object> map = new HashMap<>();

		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		JSONObject jsonObject = new JSONObject();
		session.doWork(new Work() {
			@Override
			public void execute(java.sql.Connection connection) throws SQLException {
				int p_mmu_id  =0;
				int p_city_id  =0;
				String fromDate="";
				String toDate="";
				int p_cluster_id  =0;
				JSONArray jsonArray1 = new JSONArray();

				if(jsondata.containsKey("clusterId")) {
					p_cluster_id = Integer.parseInt(jsondata.get("clusterId").toString());
				}
				if(jsondata.containsKey("mmuId")) {
					p_mmu_id = Integer.parseInt(jsondata.get("mmuId").toString());
				}
				if(jsondata.containsKey("cityId")) {
					p_city_id = Integer.parseInt(jsondata.get("cityId").toString());
				}
				if(jsondata.containsKey("fromDate")) {
					fromDate = jsondata.get("fromDate").toString();
				}
				if(jsondata.containsKey("toDate")) {
					toDate = jsondata.get("toDate").toString();
				}

				String queryString = "SELECT lab_report(?,?,?,?,?)";
				PreparedStatement    stmt = connection.prepareCall(queryString);
				stmt.setInt(1, p_cluster_id);
				stmt.setInt(2, p_city_id);
				stmt.setInt(3, p_mmu_id);
				

				java.util.Date frmDate = HMSUtil.convertStringDateToUtilDateForDatabase(fromDate);
				java.sql.Date sql_frmDate = new java.sql.Date(frmDate.getTime());

				java.util.Date to_Date = HMSUtil.convertStringDateToUtilDateForDatabase(toDate);
				java.sql.Date sql_toDate = new java.sql.Date(to_Date.getTime());

				try {
					stmt.setDate(4, sql_frmDate);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					stmt.setDate(5, sql_toDate);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

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
						jsonObject.put("labReport_data", jsonArray1);
					}
				}


			}
		});
		jsonObject.put("fromDate", jsondata.get("fromDate").toString());
		jsonObject.put("toDate", jsondata.get("toDate").toString());
		map.put("labReport_data", jsonObject);
		return map;
	}

	@Override
	public Map<String, Object> getmedicineIssueReportData(HashMap<String, Object> jsondata, HttpServletRequest request,
														  HttpServletResponse response) {
		Map<String,Object> map = new HashMap<>();

		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		JSONObject jsonObject = new JSONObject();
		session.doWork(new Work() {
			@Override
			public void execute(java.sql.Connection connection) throws SQLException {
				int p_mmu_id  =0;
				int p_district_id   =0;
				int p_cityId=0;
				String fromDate="";
				String toDate="";
				JSONArray jsonArray1 = new JSONArray();

				if(jsondata.containsKey("mmuId")) {
					p_mmu_id = Integer.parseInt(jsondata.get("mmuId").toString());
				}
				if(jsondata.containsKey("districtId")) {
					p_district_id  = Integer.parseInt(jsondata.get("districtId").toString());
				}
				if(jsondata.containsKey("cityId")) {
					p_cityId = Integer.parseInt(jsondata.get("cityId").toString());
				}
				if(jsondata.containsKey("fromDate")) {
					fromDate = jsondata.get("fromDate").toString();
				}
				if(jsondata.containsKey("toDate")) {
					toDate = jsondata.get("toDate").toString();
				}

				String queryString = "SELECT medicine_issue_report(?,?,?,?,?)";
				PreparedStatement    stmt = connection.prepareCall(queryString);

				
				stmt.setInt(1, p_district_id );
				stmt.setInt(2, p_cityId );
				stmt.setInt(3, p_mmu_id);
				
				java.util.Date frmDate = HMSUtil.convertStringDateToUtilDateForDatabase(fromDate);
				java.sql.Date sql_frmDate = new java.sql.Date(frmDate.getTime());

				java.util.Date to_Date = HMSUtil.convertStringDateToUtilDateForDatabase(toDate);
				java.sql.Date sql_toDate = new java.sql.Date(to_Date.getTime());

				try {
					stmt.setDate(4, sql_frmDate);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					stmt.setDate(5, sql_toDate);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

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
						jsonObject.put("medicineIssueReport_data", jsonArray1);
					}
				}


			}
		});
		jsonObject.put("fromDate", jsondata.get("fromDate").toString());
		jsonObject.put("toDate", jsondata.get("toDate").toString());
		map.put("medicineIssueReport_data", jsonObject);
		return map;
	}

	@Override
	public Map<String, Object> edlReportCityWise(HashMap<String, Object> jsondata, HttpServletRequest request,
												 HttpServletResponse response) {
		Map<String,Object> map = new HashMap<>();

		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		JSONObject jsonObject = new JSONObject();
		session.doWork(new Work() {
			@Override
			public void execute(java.sql.Connection connection) throws SQLException {
				int p_city_id  =0;
				int p_cluster_id  =0;
				int p_mmu_Id=0;
				String asondate ="";
				JSONArray jsonArray1 = new JSONArray();

				if(jsondata.containsKey("clusterId")) {
					p_cluster_id = Integer.parseInt(jsondata.get("clusterId").toString());
				}

				if(jsondata.containsKey("cityId")) {
					p_city_id = Integer.parseInt(jsondata.get("cityId").toString());
				}
				if(jsondata.containsKey("mmuId")) {
					p_mmu_Id = Integer.parseInt(jsondata.get("mmuId").toString());
				}

				if(jsondata.containsKey("asondate")) {
					asondate = jsondata.get("asondate").toString();
				}

				String queryString = "SELECT edl_report_summary(?,?)";
				PreparedStatement    stmt = connection.prepareCall(queryString);

				stmt.setInt(1, p_cluster_id);
				stmt.setInt(2, p_city_id);
				//stmt.setInt(3, p_mmu_Id);

				java.util.Date asondate_Date = HMSUtil.convertStringDateToUtilDateForDatabase(asondate);
				java.sql.Date sql_asondate = new java.sql.Date(asondate_Date.getTime());

				try {
					//stmt.setDate(4, sql_asondate);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

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
						jsonObject.put("edlReportCityWise_data", jsonArray1);
					}
				}


			}
		});
		jsonObject.put("asondate", jsondata.get("asondate").toString());
		map.put("edlReportCityWise_data", jsonObject);
		return map;
	}


	public Map<String, Object> getAuditOpdRegister(HashMap<String, Object> jsondata, HttpServletRequest request, HttpServletResponse response) {
		Map<String,Object> map = new HashMap<>();

		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		JSONObject jsonObject = new JSONObject();
		session.doWork(new Work() {
			@Override
			public void execute(java.sql.Connection connection) throws SQLException {
				/*String fromDate=map.get("fromDate").toString();
				String toDate=map.get("toDate").toString();
				int mmu_id=Integer.parseInt(map.get("mmu_id").toString());
				int gender_id=Integer.parseInt(map.get("gender_id").toString());
				int icdId=Integer.parseInt(map.get("icdId").toString());
				int user_id=Integer.parseInt(map.get("User_id").toString());
				String Level_of_user=map.get("Level_of_user").toString();
				int fromAge=Integer.parseInt(map.get("fromAge").toString());
				int toAge=Integer.parseInt(map.get("toAge").toString());*/
				
				String from_Date  ="";
				String to_Date  ="";
				int mmu_id=0;
				int gender_id=0;
				int icd_Id=0;
				int user_id=0;
				String Level_of_user="";
				int from_Age=0;
				int to_Age=0;
				String referral="";
				
				JSONArray jsonArray1 = new JSONArray();

				if(jsondata.containsKey("fromDate")) {
					from_Date = jsondata.get("fromDate").toString();
				}

				if(jsondata.containsKey("toDate")) {
					to_Date =jsondata.get("toDate").toString();
				}
				if(jsondata.containsKey("mmu_id")) {
					mmu_id = Integer.parseInt(jsondata.get("mmu_id").toString());
				}

				if(jsondata.containsKey("gender_id")) {
					gender_id = Integer.parseInt(jsondata.get("gender_id").toString());
				}
				if(jsondata.containsKey("icdId")) {
					icd_Id = Integer.parseInt(jsondata.get("icdId").toString());
				}
				if(jsondata.containsKey("User_id")) {
					user_id = Integer.parseInt(jsondata.get("User_id").toString());
				}
				if(jsondata.containsKey("Level_of_user")) {
					Level_of_user = jsondata.get("Level_of_user").toString();
				}
				if(jsondata.containsKey("from_Age")) {
					from_Age = Integer.parseInt(jsondata.get("from_Age").toString());
				}
				if(jsondata.containsKey("to_Age")) {
					to_Age = Integer.parseInt(jsondata.get("to_Age").toString());
				}
				if(jsondata.containsKey("referral")) {
					referral = jsondata.get("referral").toString();
				}

				String queryString = "SELECT asp_mmu_opd_register(?,?,?,?,?,?,?,?,?,?)";
				PreparedStatement    stmt = connection.prepareCall(queryString);
				
				java.util.Date asondate_Date = HMSUtil.convertStringDateToUtilDateForDatabase(from_Date);
				java.sql.Date sql_asondatefrom_Date = new java.sql.Date(asondate_Date.getTime());
				
				java.util.Date asondate_Date1 = HMSUtil.convertStringDateToUtilDateForDatabase(to_Date);
				java.sql.Date sql_asondateto_Date = new java.sql.Date(asondate_Date1.getTime());
				stmt.setDate(1, sql_asondatefrom_Date);
				stmt.setDate(2, sql_asondateto_Date);
				stmt.setInt(3, gender_id);
				stmt.setInt(4, mmu_id);
				stmt.setInt(5, icd_Id);
				stmt.setInt(6, from_Age);
				stmt.setInt(7, to_Age);
				stmt.setString(8, Level_of_user);
				stmt.setInt(9, user_id);
				stmt.setString(10, referral);
				
				//java.util.Date asondate_Date = HMSUtil.convertStringDateToUtilDateForDatabase(asondate);
				//java.sql.Date sql_asondate = new java.sql.Date(asondate_Date.getTime());

				try {
					//stmt.setDate(4, sql_asondate);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

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
						jsonObject.put("opdRegsiter_data", jsonArray1);
					}
				}


			}
		});
		//jsonObject.put("asondate", jsondata.get("asondate").toString());
		map.put("opdRegsiter_data", jsonObject);
		return map;
	  }
	

public Map<String, Object> getAttendanceRegister(HashMap<String, Object> jsondata, HttpServletRequest request, HttpServletResponse response) {
	Map<String,Object> map = new HashMap<>();

	Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	JSONObject jsonObject = new JSONObject();
	session.doWork(new Work() {
		@Override
		public void execute(java.sql.Connection connection) throws SQLException {
			
			String from_Date  ="";
			String to_Date  ="";
			int mmu_id=0;
			String Level_of_user="";
			int user_id=0;
			
			
			JSONArray jsonArray1 = new JSONArray();

			if(jsondata.containsKey("fromDate")) {
				from_Date = jsondata.get("fromDate").toString();
			}

			if(jsondata.containsKey("toDate")) {
				to_Date =jsondata.get("toDate").toString();
			}
			if(jsondata.containsKey("mmu_id")) {
				mmu_id = Integer.parseInt(jsondata.get("mmu_id").toString());
			}

			
			if(jsondata.containsKey("User_id")) {
				user_id = Integer.parseInt(jsondata.get("User_id").toString());
			}
			if(jsondata.containsKey("Level_of_user")) {
				Level_of_user = jsondata.get("Level_of_user").toString();
			}
			

			String queryString = "SELECT asp_mmu_attendance_register(?,?,?,?,?)";
			PreparedStatement    stmt = connection.prepareCall(queryString);
			
			java.util.Date asondate_Date = HMSUtil.convertStringDateToUtilDateForDatabase(from_Date);
			java.sql.Date sql_asondatefrom_Date = new java.sql.Date(asondate_Date.getTime());
			
			java.util.Date asondate_Date1 = HMSUtil.convertStringDateToUtilDateForDatabase(to_Date);
			java.sql.Date sql_asondateto_Date = new java.sql.Date(asondate_Date1.getTime());
			stmt.setDate(1, sql_asondatefrom_Date);
			stmt.setDate(2, sql_asondateto_Date);
			stmt.setInt(3, mmu_id);
			stmt.setString(4, Level_of_user);
			stmt.setInt(5, user_id);
			
			//java.util.Date asondate_Date = HMSUtil.convertStringDateToUtilDateForDatabase(asondate);
			//java.sql.Date sql_asondate = new java.sql.Date(asondate_Date.getTime());

			try {
				//stmt.setDate(4, sql_asondate);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

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
					jsonObject.put("opdRegsiter_data", jsonArray1);
				}
			}


		}
	});
	//jsonObject.put("asondate", jsondata.get("asondate").toString());
	map.put("opdRegsiter_data", jsonObject);
	return map;
  }

@Override
public Map<String, Object> getAttendanceInOutTime(HashMap<String, Object> jsondata, HttpServletRequest request, HttpServletResponse response) {
	Map<String,Object> map = new HashMap<>();

	Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	JSONObject jsonObject = new JSONObject();
	session.doWork(new Work() {
		@Override
		public void execute(java.sql.Connection connection) throws SQLException {
			
			String from_Date  ="";
			String to_Date  ="";
			int mmu_id=0;
			String Level_of_user="";
			int user_id=0;
			
			
			JSONArray jsonArray1 = new JSONArray();

			if(jsondata.containsKey("fromDate")) {
				from_Date = jsondata.get("fromDate").toString();
			}

			if(jsondata.containsKey("toDate")) {
				to_Date =jsondata.get("toDate").toString();
			}
			if(jsondata.containsKey("mmu_id")) {
				mmu_id = Integer.parseInt(jsondata.get("mmu_id").toString());
			}

			
			if(jsondata.containsKey("User_id")) {
				user_id = Integer.parseInt(jsondata.get("User_id").toString());
			}
			if(jsondata.containsKey("Level_of_user")) {
				Level_of_user = jsondata.get("Level_of_user").toString();
			}
			

			String queryString = "SELECT asp_mmu_attendance_in_out_time(?,?,?,?,?)";
			PreparedStatement    stmt = connection.prepareCall(queryString);
			
			java.util.Date asondate_Date = HMSUtil.convertStringDateToUtilDateForDatabase(from_Date);
			java.sql.Date sql_asondatefrom_Date = new java.sql.Date(asondate_Date.getTime());
			
			java.util.Date asondate_Date1 = HMSUtil.convertStringDateToUtilDateForDatabase(to_Date);
			java.sql.Date sql_asondateto_Date = new java.sql.Date(asondate_Date1.getTime());
			stmt.setDate(1, sql_asondatefrom_Date);
			stmt.setDate(2, sql_asondateto_Date);
			stmt.setInt(3, mmu_id);
			stmt.setString(4, Level_of_user);
			stmt.setInt(5, user_id);
			
			//java.util.Date asondate_Date = HMSUtil.convertStringDateToUtilDateForDatabase(asondate);
			//java.sql.Date sql_asondate = new java.sql.Date(asondate_Date.getTime());

			try {
				//stmt.setDate(4, sql_asondate);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

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
					jsonObject.put("opdRegsiter_data", jsonArray1);
				}
			}


		}
	});
	//jsonObject.put("asondate", jsondata.get("asondate").toString());
	map.put("opdRegsiter_data", jsonObject);
	return map;
  }

@Override
public Map<String, Object> getLabRegister(HashMap<String, Object> jsondata, HttpServletRequest request,
		HttpServletResponse response) {
	Map<String,Object> map = new HashMap<>();

	Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	JSONObject jsonObject = new JSONObject();
	session.doWork(new Work() {
		@Override
		public void execute(java.sql.Connection connection) throws SQLException {
			String from_Date  ="";
			String to_Date  ="";
			int mmu_id=0;
			int gender_id=0;
			int investigation_Id=0;
			int user_id=0;
			String Level_of_user="";
			int from_Age=0;
			int to_Age=0;
			
			JSONArray jsonArray1 = new JSONArray();

			if(jsondata.containsKey("fromDate")) {
				from_Date = jsondata.get("fromDate").toString();
			}

			if(jsondata.containsKey("toDate")) {
				to_Date =jsondata.get("toDate").toString();
			}
			if(jsondata.containsKey("mmu_id")&& org.apache.commons.lang.StringUtils.isNotBlank(jsondata.get("mmu_id").toString())) {
				mmu_id = Integer.parseInt(jsondata.get("mmu_id").toString());
			}else {
				mmu_id=0;
			}

			if(jsondata.containsKey("gender_id") && org.apache.commons.lang.StringUtils.isNotBlank(jsondata.get("gender_id").toString())) {
				gender_id = Integer.parseInt(jsondata.get("gender_id").toString());
			}else {
				gender_id=0;
			}
			if(jsondata.containsKey("investigationId") && org.apache.commons.lang.StringUtils.isNotBlank(jsondata.get("investigationId").toString()))  {
				investigation_Id = Integer.parseInt(jsondata.get("investigationId").toString());
			}else {
				investigation_Id=0;
			}
			if(jsondata.containsKey("User_id") && org.apache.commons.lang.StringUtils.isNotBlank(jsondata.get("User_id").toString()))  {
				user_id = Integer.parseInt(jsondata.get("User_id").toString());
			}else {
				user_id=0;
			}
			if(jsondata.containsKey("Level_of_user")) {
				Level_of_user = jsondata.get("Level_of_user").toString();
			}
			if(jsondata.containsKey("fromAge")&& org.apache.commons.lang.StringUtils.isNotBlank(jsondata.get("fromAge").toString())) {
				from_Age = Integer.parseInt(jsondata.get("fromAge").toString());
			}else {
				from_Age=0;
			}
			if(jsondata.containsKey("toAge") && org.apache.commons.lang.StringUtils.isNotBlank(jsondata.get("toAge").toString())) {
				to_Age = Integer.parseInt(jsondata.get("toAge").toString());
			}else {
				to_Age=0;
			}

			String queryString = "SELECT asp_mmu_lab_register(?,?,?,?,?,?,?,?,?)";
			PreparedStatement    stmt = connection.prepareCall(queryString);
			
			java.util.Date asondate_Date = HMSUtil.convertStringDateToUtilDateForDatabase(from_Date);
			java.sql.Date sql_asondatefrom_Date = new java.sql.Date(asondate_Date.getTime());
			
			java.util.Date asondate_Date1 = HMSUtil.convertStringDateToUtilDateForDatabase(to_Date);
			java.sql.Date sql_asondateto_Date = new java.sql.Date(asondate_Date1.getTime());
			stmt.setDate(1, sql_asondatefrom_Date);
			stmt.setDate(2, sql_asondateto_Date);
			stmt.setInt(3, gender_id);
			stmt.setInt(4, investigation_Id);
			stmt.setInt(5, mmu_id);			
			stmt.setInt(6, from_Age);
			stmt.setInt(7, to_Age);
			stmt.setString(8, Level_of_user);
			stmt.setInt(9, user_id);
			
			//java.util.Date asondate_Date = HMSUtil.convertStringDateToUtilDateForDatabase(asondate);
			//java.sql.Date sql_asondate = new java.sql.Date(asondate_Date.getTime());

			try {
				//stmt.setDate(4, sql_asondate);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

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
					jsonObject.put("opdRegsiter_data", jsonArray1);
				}
			}


		}
	});
	//jsonObject.put("asondate", jsondata.get("asondate").toString());
	map.put("opdRegsiter_data", jsonObject);
	return map;
  }
}
