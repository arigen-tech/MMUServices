package com.mmu.services.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mmu.services.hibernateutils.GetHibernateUtils;
import com.mmu.services.utils.HMSUtil;

@Repository
public class FundInvoiceDashBoardDaoImpl {

	@Autowired
	GetHibernateUtils getHibernateUtils;
	
	public Map<String, Object> getFundInvoiceUpssWise(HashMap<String, Object> jsondata, HttpServletResponse response) {
		Map<String,Object> map = new HashMap<>();
		
	
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();	
		JSONObject jsonObject = new JSONObject();
		try {
		session.doWork(new Work() {
			@Override
			public void execute(java.sql.Connection connection) throws SQLException {
				
				int mmuId=0;
				String toDate="";
				String fromDate="";
				String mmuOrCity="";
				String flagType="FM";
				String fundType= null;
				String phase=null;
				String distIdVal=null;
				String levelOfUser=null;
				JSONArray jsonArray1 = new JSONArray();				
				JSONArray jsonArray2 = new JSONArray();	
				
				
				if(jsondata.containsKey("mmuCity")) {
					mmuOrCity = jsondata.get("mmuCity").toString();
					}
				if(jsondata.containsKey("upss_id")) {
					mmuId = Integer.valueOf(jsondata.get("upss_id").toString());
					}
				if(jsondata.containsKey("toDate")) {
					toDate = jsondata.get("toDate").toString();
					}
				if(jsondata.containsKey("fundType")) {
					fundType = jsondata.get("fundType").toString();
					}
				java.util.Date tDate = HMSUtil.convertStringDateToUtilDateForDatabase(toDate);
				java.sql.Date sql_toDate = new java.sql.Date(tDate.getTime());
				if(jsondata.containsKey("fromDate")) {
					fromDate = jsondata.get("fromDate").toString();
					}
				if(jsondata.containsKey("flagType")) {
					flagType = jsondata.get("flagType").toString();
				}
				if(jsondata.containsKey("phase")) {
					phase = jsondata.get("phase").toString();
					if(phase.equalsIgnoreCase("")) {
						phase="0";
					}
				}
				if(jsondata.containsKey("flagType")) {
					flagType = jsondata.get("flagType").toString();
				}
				if(jsondata.containsKey("distIdVal")) {
					distIdVal = jsondata.get("distIdVal").toString();
				}
				if(jsondata.containsKey("levelOfUser")) {
					levelOfUser = jsondata.get("levelOfUser").toString();
				}
				java.util.Date frmDate = HMSUtil.convertStringDateToUtilDateForDatabase(fromDate);
				java.sql.Date sql_frmDate = new java.sql.Date(frmDate.getTime());
				String queryString = "SELECT asp_invoice_fund_dashboard(?,?,?,?,?,?,?,?,?)";				
				PreparedStatement    stmt = connection.prepareCall(queryString);
				
				stmt.setDate(1, sql_frmDate);
				stmt.setDate(2,sql_toDate );
				stmt.setString(3,mmuOrCity);
				stmt.setString(4,flagType);
				stmt.setInt(5, mmuId);
				stmt.setString(6,fundType);
				stmt.setString(7, phase);
				stmt.setString(8, distIdVal);
				stmt.setString(9, levelOfUser);
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
						jsonObject.put("fundInvoiceDataInfo", jsonArray1);
					}
				  }
			
			    }
		   });
		}
		finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
			map.put("fundInvoice_info", jsonObject);
			return map;
		}
	
}
