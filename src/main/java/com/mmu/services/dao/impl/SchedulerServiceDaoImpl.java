package com.mmu.services.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.jdbc.Work;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mmu.services.dao.SchedulerServiceDao;
import com.mmu.services.entity.Attendance;
import com.mmu.services.entity.MasCamp;
import com.mmu.services.entity.MasMMU;
import com.mmu.services.hibernateutils.GetHibernateUtils;
import com.mmu.services.utils.HMSUtil;


@Repository
@Transactional
public class SchedulerServiceDaoImpl implements SchedulerServiceDao {
	
	@Autowired
	GetHibernateUtils getHibernateUtils;
	
	@Override
	public Map<String, Object> getMMUListForPoll(HashMap<String,Object> json ) {

		Map<String, Object> map = new HashMap<String, Object>();
		List<MasMMU> list = new ArrayList<>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasMMU.class).add(Restrictions.eq("status", "Y").ignoreCase());

			
			list = criteria.addOrder(Order.asc("mmuId")).list();
			map.put("mmu", list);
			return map;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return map;
	
	}

	@Override
	public Map<String, Object> savePollInfo(Map<String, Object> requestData) {


		Map<String, Object> response = new HashMap<>();
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Transaction tx = session.beginTransaction();
		Criteria cr = null;
		List<Attendance> list = new ArrayList<>();
		try {

			
			Long mmuId = Long.parseLong(String.valueOf(requestData.get("mmuId")));
			int good = Integer.parseInt(String.valueOf(requestData.get("good")));
			int average = Integer.parseInt(String.valueOf(requestData.get("average")));
			int bad = Integer.parseInt(String.valueOf(requestData.get("bad")));
			int total = Integer.parseInt(String.valueOf(requestData.get("total")));
			String pollDate = String.valueOf(requestData.get("pollDate"));
			
			String lastChgDate = HMSUtil.getCurrentTimeStamp().toString();
			
			
			Query insertSql = session.createSQLQuery("insert into mmu_feedback_data(mmu_id,map_date,good,average,bad,last_chg_date,total) " + 
					"values("+mmuId+",'"+pollDate+"',"+good+","+average+","+bad+",'"+lastChgDate+"',"+total+"); ");
			//System.out.println("insertSql--> "+insertSql.toString());
			insertSql.executeUpdate();
			
			response.put("status", true);
			response.put("msg", "Success");		
			
			tx.commit();
			return response;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		response.put("status", false);
		response.put("msg", "Failed");
		return response;
	
	
	}

	@Override
	public Map<String, Object> getCampDetailsForMMU(HashMap<String, Object> jsondata) {


		Map<String, Object> map = new HashMap<String, Object>();
		List<MasCamp> list = new ArrayList<>();
		try {
			//Long mmuId = Long.parseLong(String.valueOf(jsondata.get("mmuId")));
			LocalDate today = LocalDate.now();
			
			//System.out.println("localTime1--- "+HMSUtil.getCurrentSQLTime());
			
			Date campDate = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasCamp.class)
					.createAlias("masMMU", "mmu")					
					//.add(Restrictions.eq("mmu.mmuId", mmuId))
					.add(Restrictions.eq("campDate", campDate))
					.add(Restrictions.eq("weeklyOff", "Camp"))
					.add(Restrictions.le("startTime", HMSUtil.getCurrentSQLTime()))
					//.add(Restrictions.le("endTime", HMSUtil.getCurrentSQLTime()))
					.addOrder(Order.asc("mmu.mmuId"));

			
			list = criteria.list();
			//System.out.println("list mmu size--> "+list.size());
			map.put("mmu", list);
			return map;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return map;
	
	
	}

	@Override
	public Map<String, Object> saveVehicleLocation(Map<String, Object> requestData) {



		Map<String, Object> response = new HashMap<>();
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Transaction tx = session.beginTransaction();
		Criteria cr = null;
		List<Attendance> list = new ArrayList<>();
		try {

			String schedulerDate = String.valueOf(requestData.get("schedulerDate"));
			Long mmuId = Long.parseLong(String.valueOf(requestData.get("mmuId")));
			Long campId = Long.parseLong(String.valueOf(requestData.get("campId")));
			Double campLatitudes = Double.parseDouble(String.valueOf(requestData.get("campLatitudes")));
			Double campLongitudes = Double.parseDouble(String.valueOf(requestData.get("campLongitudes")));
			
			Double mmuLattitude = Double.parseDouble(String.valueOf(requestData.get("mmuLattitude")));
			Double mmuLongitude = Double.parseDouble(String.valueOf(requestData.get("mmuLongitude")));
			Double distance = Double.parseDouble(String.valueOf(requestData.get("distance")));
			
			String lastChgDate = HMSUtil.getCurrentTimeStamp().toString();
			
			Query insertSql = null;
			
			if(campId > 0) {
			   insertSql = session.
			  createSQLQuery("insert into mmu_audit_camptime(scheduler_date,camp_id,mmu_id,camp_latitudes,camp_longitudes,mmu_latitudes,mmu_longitudes,distance,last_chg_date) "
			  + "values('"+schedulerDate+"',"+campId+","+mmuId+","+campLatitudes+","+
			  campLongitudes+","+mmuLattitude+","+mmuLongitude+","+distance+
			  ",'"+lastChgDate+"'); ");
			 
			} else {
			 insertSql = session.createSQLQuery("insert into mmu_audit_camptime(scheduler_date,mmu_id,camp_latitudes,camp_longitudes,mmu_latitudes,mmu_longitudes,distance,last_chg_date) " + 
					"values('"+schedulerDate+"',"+mmuId+","+campLatitudes+","+campLongitudes+","+mmuLattitude+","+mmuLongitude+","+distance+",'"+lastChgDate+"'); ");
			
			}
			//System.out.println("insertSql--> "+insertSql.toString());
			insertSql.executeUpdate();
			
			response.put("status", true);
			response.put("msg", "Success");		
			
			tx.commit();
			return response;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		response.put("status", false);
		response.put("msg", "Failed");
		return response;
	
	
	
	}

	@Override
	public Map<String, Object> getFollowupMsgData() {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Map<String, Object> map = new HashMap<>();
		List<Object[]> searchList = null;
		int count = 0;

		try {
			
			String Query = "select op.followup_date,ps.mobile_number,op.follow_up_location,op.follow_up_landnarks,op.patient_id,op.opd_patient_details_id,ps.patient_name from opd_patient_details op" + 
					" left outer join patient ps on ps.patient_id=op.patient_id" + 
					" where op.followup_date=CURRENT_DATE+1 and (followup_msg_status='N' or followup_msg_status is null);";

			System.out.println(Query);
			if (Query != null) {
				searchList = session.createSQLQuery(Query).list();

			} else {
				System.out.println("No Record Found");
			}
			Query query = session.createSQLQuery(Query.toString());
			count = searchList.size();

			int listCount = searchList.size();

			searchList = query.list();

			map.put("count", count);
			map.put("list", searchList);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}
	
	@Override
	public String updateFollowUpMsgStatus(Long opdPatientDetailsId) {
		try {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Transaction t = session.beginTransaction();
		if(opdPatientDetailsId!=null)
		{	
		String qryStringHd  ="update OpdPatientDetail u set u.followupMsgStatus='Y' where u.opdPatientDetailsId='"+opdPatientDetailsId+"' " ;
		Query queryHd = session.createQuery(qryStringHd);
        int countHd = queryHd.executeUpdate();
		System.out.println(countHd + "Users Records MMU Updated.");
		t.commit();
		}
		}catch (Exception e) {
			e.printStackTrace();
			return "500";
			// TODO: handle exception
		}
		return "statusUpdated";
	}

	@Override
	public Map<String, Object> getMmedicineRolInfo(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> map = new HashMap<>();
		
		//int userId = 2;
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();	
		JSONObject jsonObject = new JSONObject();
		session.doWork(new Work() {
			@Override
			public void execute(java.sql.Connection connection) throws SQLException {
				
				int mmuId=1;
				JSONArray jsonArray1 = new JSONArray();				
				JSONArray jsonArray2 = new JSONArray();	
				
				/*
				
				if(jsondata.containsKey("mmuId")) {
					mmuId = Integer.parseInt(jsondata.get("mmuId").toString());
					}*/
				
				String queryString = "SELECT asp_medicine_rol_info()";				
				PreparedStatement    stmt = connection.prepareCall(queryString);
				//stmt.setInt(1, mmuId);
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
						jsonObject.put("rol_medicine", jsonArray1);
					}
				  }
												
			    }
		   });
			
				map.put("rolMedicineInfo", jsonObject);
				return map;
		}

	@Override
	public String auditUploadData(HashMap<String, String> jsondata, HttpServletResponse response) {
		Map<String,Object> map = new HashMap<>();
	    Session session = getHibernateUtils.getHibernateUtlis().OpenSession();	
		JSONObject jsonObject = new JSONObject();
		try {
		session.doWork(new Work() {
			@Override
			public void execute(java.sql.Connection connection) throws SQLException {
				
			
				String queryString = "SELECT asp_audit_upload()";				
				PreparedStatement    stmt = connection.prepareCall(queryString);
				JSONArray jsonArray1 = new JSONArray();		
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
				return "success";
		}

}
