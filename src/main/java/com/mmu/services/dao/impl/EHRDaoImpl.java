package com.mmu.services.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.jdbc.Work;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mmu.services.dao.EHRDao;
import com.mmu.services.entity.Visit;
import com.mmu.services.hibernateutils.GetHibernateUtils;
import com.mmu.services.utils.HMSUtil;


@Repository
public class EHRDaoImpl implements EHRDao {
	String databaseScema = HMSUtil.getProperties("adt.properties", "currentSchema").trim();
	@Autowired
	GetHibernateUtils getHibernateUtils;	

	@Override
	public Map<String, Object> getPatientSummary(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> map = new HashMap<>();
		JSONObject object = new JSONObject();
		JSONArray jsArray1 = new JSONArray();
		JSONArray jsArray2 = new JSONArray();
		JSONArray jsArray3 = new JSONArray();
		JSONArray jsArray4 = new JSONArray();
		JSONArray jsArray5 = new JSONArray();
		JSONArray jsArray6 = new JSONArray();
		JSONArray jsArray7 = new JSONArray();
		JSONArray jsArray8 = new JSONArray();
		JSONArray jsArray9 = new JSONArray();
		JSONArray jsArray10 = new JSONArray();
		JSONArray jsArray11 = new JSONArray();
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();	
		
		session.doWork(new Work() {
			@Override
			public void execute(java.sql.Connection connection) throws SQLException {
				int patientId = Integer.parseInt(jsondata.get("patient_id").toString());
				int userId = Integer.parseInt(jsondata.get("user_id").toString());
				
				String queryString = ("SELECT * from "+databaseScema+".ASP_EHR_PATIENT_DATA(?,?)");				
				PreparedStatement    stmt = connection.prepareCall(queryString);
				stmt.setInt(1, patientId);
				stmt.setInt(2, userId);
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
							jsArray1.put(jsonObj);
						}
						object.put("ref_cur1", jsArray1);
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
							jsArray2.put(jsonObj);
						}
						object.put("ref_cur2", jsArray2);
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
							jsArray3.put(jsonObj);
						}
						object.put("ref_cur3", jsArray3);
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
							jsArray4.put(jsonObj);
						}
						object.put("ref_cursor4", jsArray4);
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
							jsArray5.put(jsonObj);
						}
						object.put("ref_cursor5", jsArray5);
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
							jsArray6.put(jsonObj);
						}
						object.put("ref_cursor6", jsArray6);
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
							jsArray7.put(jsonObj);
						}
						object.put("ref_cursor7", jsArray7);
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
							jsArray8.put(jsonObj);
						}
						object.put("ref_cursor8", jsArray8);
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
							jsArray9.put(jsonObj);
						}
						object.put("ref_cursor9", jsArray9);
					}
				}
				
				if (rs.next()) {
					Object o = rs.getObject(1);
					if (o instanceof ResultSet) {
						ResultSet rs10 = (ResultSet) o;
						while (rs10.next()) {
							int columnCount = rs10.getMetaData().getColumnCount();
							JSONObject jsonObj = new JSONObject();
							for (int i = 0; i < columnCount; i++) {
								jsonObj.put(rs10.getMetaData().getColumnLabel(i + 1), HMSUtil.convertNullToEmptyString(rs10.getObject(i + 1)));
							}
							jsArray10.put(jsonObj);
						}
						object.put("ref_cursor10", jsArray10);
					}
				}
				
								
				if (rs.next()) {
					Object o = rs.getObject(1);
					if (o instanceof ResultSet) {
						ResultSet rs11 = (ResultSet) o;
						while (rs11.next()) {
							int columnCount = rs11.getMetaData().getColumnCount();
							JSONObject jsonObj = new JSONObject();
							for (int i = 0; i < columnCount; i++) {
								jsonObj.put(rs11.getMetaData().getColumnLabel(i + 1), HMSUtil.convertNullToEmptyString(rs11.getObject(i + 1)));
							}
							jsArray11.put(jsonObj);
						}
						object.put("ref_cursor11", jsArray11);
					}
				}
								
			}
		});
		map.put("patient_summary", object);
		return map;
	}
	
	@Override
	public Map<String, Object> getVisitSummary(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> map = new HashMap<>();
		
		//int userId = 2;
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();	
		JSONObject jsonObject = new JSONObject();
		session.doWork(new Work() {
			@Override
			public void execute(java.sql.Connection connection) throws SQLException {
				int visitId =0;
				JSONArray jsonArray1 = new JSONArray();
				JSONArray jsonArray2 = new JSONArray();
				JSONArray jsonArray3 = new JSONArray();
				JSONArray jsonArray4 = new JSONArray();
				JSONArray jsonArray5 = new JSONArray();
				JSONArray jsonArray6 = new JSONArray();
				JSONArray jsonArray7 = new JSONArray();
				JSONArray jsonArray8 = new JSONArray();				
				int patientId = Integer.parseInt(jsondata.get("patient_id").toString());
				if(jsondata.containsKey("visit_id")) {
				  visitId = Integer.parseInt(jsondata.get("visit_id").toString());
				}
				String queryString = ("SELECT * from "+databaseScema+".ASP_EHR_VISIT_DATA(?,?)");				
				PreparedStatement    stmt = connection.prepareCall(queryString);
				stmt.setInt(1, patientId);
				stmt.setInt(2, visitId);
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
						jsonObject.put("encounter_details", jsonArray1);
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
						jsonObject.put("plan_details", jsonArray2);
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
							jsonArray3.put(jsonObj);
						}
						jsonObject.put("current_medication", jsonArray3);
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
						jsonObject.put("nursing_care", jsonArray4);
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
						jsonObject.put("recommended_advice", jsonArray5);
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
						jsonObject.put("implant_history", jsonArray6);
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
							jsonArray7.put(jsonObj);
						}
						jsonObject.put("disposal", jsonArray7);
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
						jsonObject.put("immunization_history", jsonArray8);
					}
				}
			
				
			}
		});
						
		map.put("visit_summary", jsonObject);
		return map;
	}

	public List getPatientTotalVisit(HashMap<String, Object> jsondata) {
		Long patientId = Long.parseLong(jsondata.get("patient_id").toString());
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();	
		try {
			List list = session.createCriteria(Visit.class)
			.add(Restrictions.eq("patientId", patientId))
			.add(Restrictions.eq("visitStatus", "c").ignoreCase())
			.setProjection(Projections.property("visitId").as("visitId"))
			.addOrder(Order.desc("visitDate"))
			.list();
			return list;
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

}
