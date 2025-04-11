package com.mmu.services.dao.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mmu.services.dao.AdminDao;
import com.mmu.services.entity.CityMmuMapping;
import com.mmu.services.entity.DoctorRoaster;
import com.mmu.services.entity.MasAppointmentSession;
import com.mmu.services.entity.MasCity;
import com.mmu.services.entity.MasDepartment;
import com.mmu.services.entity.MasDoctorMapping;
import com.mmu.services.entity.MasHospital;
import com.mmu.services.entity.MasMMU;
import com.mmu.services.entity.Users;
import com.mmu.services.entity.Visit;
import com.mmu.services.hibernateutils.GetHibernateUtils;
import com.mmu.services.utils.CommonUtil;
import com.mmu.services.utils.HMSUtil;

@Repository
public class AdminDaoImpl implements AdminDao {
	
	@Autowired
	GetHibernateUtils getHibernateUtils;	

	@SuppressWarnings("unchecked")
	@Override
	public Map<String,Object> getDepartmentList(HashMap<String, Object> map) {
		Map<String,Object> data = new HashMap<>();
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();	
		String hID = (String)map.get("hospitalID");
		Long hospitalId = Long.parseLong(hID);	
		String flag = "";
		if(map.containsKey("flag")) {
			flag = (String) map.get("flag");
		}
		String departmentTypeCode = HMSUtil.getProperties("adt.properties", "DEPARTMENT_TYPE_CODE").trim();
		Long departmentTypeId = CommonUtil.getDepartmentTypeIdAgainstCode(session, departmentTypeCode);
		System.out.println("departmentTypeId "+departmentTypeId);
		List<MasDepartment> departmentList = new ArrayList<>();
		List<Object []> objectList = new ArrayList<>();
		if(flag.equalsIgnoreCase("roaster")) {

			departmentList=session.createCriteria(MasAppointmentSession.class)
					.add(Restrictions.eq("Status","y").ignoreCase())
					.createAlias("masDepartment", "md")
					.add(Restrictions.eq("md.status","y").ignoreCase())
					.createAlias("masHospital", "hospital")
					.add(Restrictions.eq("hospital.hospitalId", hospitalId))
					.setProjection(Projections.distinct(Projections.property("masDepartment")))
					.addOrder(Order.asc("masDepartment"))
					.list();
			
			data.put("list", departmentList);
		}else {
			departmentList = session.createCriteria(MasDepartment.class)	
					.add(Restrictions.eq("status", "Y").ignoreCase())
					.createAlias("masDepartmentType", "dt")
					.add(Restrictions.eq("dt.departmentTypeId",departmentTypeId))
					.addOrder(Order.asc("departmentName"))
					.list();
			
			data.put("list", departmentList);
		}
		System.out.println("size is "+departmentList.size());
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return data;
			
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MasDoctorMapping> getDoctorList(HashMap<String, Object> jsondata) {
		String deptID = (String) jsondata.get("departmentID");
		Long dpid = Long.parseLong(deptID);
		System.out.println("department id is "+dpid);
		List<MasDoctorMapping> list = new ArrayList<MasDoctorMapping> ();
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		list = session.createCriteria(MasDoctorMapping.class)
				.createAlias("departmentID", "dt")
				.add(Restrictions.eq("dt.departmentTypeId", dpid)).list();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getDoctorRoaster(HashMap<String, Object> map) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Map<String, Object> jsonResponse = new HashMap<>();
		try {
		System.out.println(map.toString());
		
		HashMap<String, Object> roasterData = new HashMap<>();
		String deptId = (String) map.get("departmentID");
		long hospitalId = Long.parseLong(map.get("hospital_id")+"");
		long dpid = Long.parseLong(deptId);
		//SimpleDateFormat formatter = new SimpleDateFormat("yy-MM-dd");
		//SimpleDateFormat formatter = new SimpleDateFormat("yy-MM-dd");
		Date fromDate =null;
		Date toDate =null;
		 if(map.get("fromDate")!=null)
	     {		 
						 
			 	fromDate = HMSUtil.convertStringTypeDateToDateType(String.valueOf(map.get("fromDate")));							 
				Calendar c = Calendar.getInstance();
				c.setTime(fromDate);
				c.add(Calendar.DATE, 6); 
				toDate = c.getTime();
	     }
		 
		System.out.println("dpid "+dpid+" fromdate"+fromDate+" todate "+toDate);
		List<DoctorRoaster> list = new ArrayList<>();
		Criteria c=session.createCriteria(DoctorRoaster.class,"dr")
				.createAlias("dr.masHospital", "mh")
				.createAlias("dr.masDepartment", "md")
				.add(Restrictions.eq("mh.hospitalId", hospitalId))				
				.add(Restrictions.between("roasterDate", fromDate, toDate))				
				.add(Restrictions.eq("md.departmentId", dpid));
				list=c.list();	 
				
		List<MasAppointmentSession> checkboxConfigurationDetail = new ArrayList<>();
		checkboxConfigurationDetail = session.createCriteria(MasAppointmentSession.class,"mas")
				.createAlias("masAppointmentType", "mat")
				.createAlias("mas.masHospital","mh")
				.add(Restrictions.eq("mh.hospitalId", hospitalId))
				.add(Restrictions.eq("masDepartment.departmentId", dpid))
				.addOrder(Order.asc("mat.appointmentTypeName"))
				.list();
		
		List<MasAppointmentSession> appointmentSessionList = new ArrayList<>();
		appointmentSessionList = session.createCriteria(MasAppointmentSession.class,"mas")
				.createAlias("masAppointmentType", "mat")
				.createAlias("mas.masHospital","mh")
				.add(Restrictions.eq("mh.hospitalId", hospitalId))
				.add(Restrictions.eq("masDepartment.departmentId", dpid))
				.add(Restrictions.eq("Status", "y").ignoreCase())
				.addOrder(Order.asc("mat.appointmentTypeName"))
				.list();
		
		jsonResponse.put("doctorRoasterDetail", list);
		jsonResponse.put("checkboxConfigurationDetail", checkboxConfigurationDetail);
		jsonResponse.put("appointmentSessionList",appointmentSessionList);
		return jsonResponse;
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return jsonResponse;
		
	}
	

	@Override
	public String submitDepartmentRoaster(List<String[]> allrowdata, String changeDate, String changeTime, Long changeBy, Long dept_id, Long hostpitalID) {
		
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();		
		Transaction tx = session.beginTransaction();
		try {
		String result = "";
		//java.util.Date currentDate =  CommonUtil.convertStringTypeDateToDateType(changeDate);
		MasDepartment md = new MasDepartment();
		md.setDepartmentId(dept_id);
		MasHospital mh = new MasHospital();
		mh.setHospitalId(hostpitalID);
		/*MasEmployee me = new MasEmployee();
		me.setEmployeeId(1l);*/
		Users users = new Users();
		users.setUserId(changeBy);
		DoctorRoaster dr = null;
		System.out.println("dept_id "+dept_id+" changeBy "+changeBy+" doctorId");
		boolean flag = false;
		for(int i=0;i<allrowdata.size();i++) {
			
			String[] rowdata = allrowdata.get(i);
			long id = 0;
			java.util.Date roasterDate = null;
			String roasterValue = null;	
				if (rowdata.length > 0) {
					if(rowdata[0].equals("")) {
						rowdata[0] = "0";
					}
					DateTimeFormatter sdf = DateTimeFormatter.ofPattern("dd/MM/yyyy");	
					id = Long.parseLong(rowdata[0]);
					//roasterDate = CommonUtil.convertStringDateToSQLDate(rowdata[1]);
					roasterDate = HMSUtil.convertStringTypeDateToDateType(rowdata[1]);
					//LocalDate roastLdate = new java.sql.Date(roasterDate.getTime()).toLocalDate();					
					LocalDate roasterDates = LocalDate.parse(rowdata[1], sdf);
					System.out.println("local roaster date is "+roasterDates);
					
					//today Local date
					LocalDate todayLocalDate = LocalDate.now();				
					String todayDate = sdf.format(todayLocalDate);
					LocalDate tDate = LocalDate.parse(todayDate, sdf);
					System.out.println("today date is "+tDate);
					
					
					if(roasterDates.isBefore(tDate)) {  
						System.out.println("roaster date is small");
						continue;
					}else {
						System.out.println("roaster date is larger");
					}
					
					roasterValue = rowdata[2];
					System.out.println("id " + id+" roasterDate "+roasterDate+" roasterValue "+roasterValue);
					
					// Below method will cancel the visit if doctor is not available on roster date
					 checkVisitForCancellationDoctorNotAvailable(session,tx,hostpitalID,dept_id,roasterDates,roasterValue);
					
				} 
			
					System.out.println("id is "+id);
					dr = (DoctorRoaster) session.get(DoctorRoaster.class, id);	
					if(dr!=null) {
						
						dr.setRoasterDate(roasterDate);
						dr.setRoasterValue(roasterValue);
						Calendar calendar = Calendar.getInstance();
						java.sql.Timestamp ourJavaTimestampObject = new java.sql.Timestamp(calendar.getTime().getTime());
						dr.setLastChgDate(ourJavaTimestampObject);
						dr.setUser(users);
						dr.setMasDepartment(md);
						dr.setMasHospital(mh);	
						//dr.setMasEmployee(me);						
						
						session.update(dr);	
					}else {
						dr = new DoctorRoaster();						
						dr.setRoasterDate(roasterDate);
						dr.setRoasterValue(roasterValue);
						Calendar calendar = Calendar.getInstance();
						java.sql.Timestamp ourJavaTimestampObject = new java.sql.Timestamp(calendar.getTime().getTime());						
						dr.setLastChgDate(ourJavaTimestampObject);
						dr.setUser(users);
						dr.setMasDepartment(md);
						dr.setMasHospital(mh);		
						session.save(dr);	
					}
				
					/*md.setDepartmentId(dept_id);
					//mh.setHospitalID(hostpitalID);
					dr.setRoasterDate(roasterDate);
					dr.setRoasterValue(roasterValue);
					dr.setChgDate(roasterDate);
					dr.setChgTime(changeTime);
					dr.setUser(users);
					dr.setMasDepartment(md);
					dr.setMasEmployee(me);
					dr.setHospitalId(hostpitalID);		
					session.saveOrUpdate(dr);			*/		
					flag = true;
			}
		if(flag == false) {
			return "Roster could not be saved for previous date";
		}
		
		tx.commit();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return "Roster created Successfully";
		
	}catch(Exception ex) {
		tx.rollback();
		ex.printStackTrace();
		return "Error";
	}
	}	
	
	@SuppressWarnings("unchecked")
	private void checkVisitForCancellationDoctorNotAvailable(Session session, Transaction tx,Long hostpitalID, Long dept_id, LocalDate roasterDate,
			String roasterValue) {

		List<Visit> exisitingVisit = new ArrayList<Visit>();
		Date visitStartDate = Date.from(roasterDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		Date visitEndDate = HMSUtil.getNextDate(visitStartDate);

		String[] arrayValues = roasterValue.split(",");
		String appTypeAndValue = "";
		for (int i = 0; i < arrayValues.length; i++) {
			appTypeAndValue = arrayValues[i];
			String[] arr = appTypeAndValue.split("@");
			String rosterStatus = arr[0];
			long appointmentTypeId = Long.parseLong(arr[1]);
			if (rosterStatus.equalsIgnoreCase("n")) {
				exisitingVisit = session.createCriteria(Visit.class)
						.add(Restrictions.eq("masHospital.hospitalId", hostpitalID))
						.add(Restrictions.eq("masDepartment.departmentId", dept_id))
						.add(Restrictions.eq("masAppointmentType.appointmentTypeId", appointmentTypeId))
						.add(Restrictions.eq("visitStatus", 'w').ignoreCase())
						.add(Restrictions.ge("visitDate", visitStartDate))
						.add(Restrictions.lt("visitDate", visitEndDate)).list();

				if (exisitingVisit.size() > 0) {
					for (Visit visit : exisitingVisit) {
						Visit v = (Visit) session.get(Visit.class, visit.getVisitId());
						v.setVisitStatus("n");
						//v.setCancelStatus("f");
						v.setTokenNo(null);
					}
				}

			}
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MasDepartment> getDepartmentListBasedOnDepartmentType(HashMap<String, Object> map) {
		String departmentType = String.valueOf(map.get("departmentType"));
		String mmuId = String.valueOf(map.get("mmuId"));
		System.out.println("mmuId"+mmuId);
		List<MasDepartment> list = new ArrayList<>();
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		try {
			String departmentTypeCode = HMSUtil.getProperties("adt.properties", "DEPARTMENT_TYPE_CODE").trim();
			Criteria criteria = session.createCriteria(MasDepartment.class)
					.createAlias("masDepartmentType", "dt")
					.add(Restrictions.eq("dt.departmentTypeCode",departmentTypeCode))
					.add(Restrictions.eq("status", "Y").ignoreCase()).addOrder(Order.asc("departmentName"));

			list = criteria.list();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return list;		
		}

	@Override
	public List<MasCity> getAllCity(HashMap<String, Object> map) {
		String mmuId = String.valueOf(map.get("mmuId"));
		//System.out.println("mmuId"+mmuId);
		List<MasCity> list = new ArrayList<>();
		List<CityMmuMapping> listCityMmuMapping = new ArrayList<>();
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		try {
			 if(mmuId!=null && !mmuId.equalsIgnoreCase("null")) {
			Criteria criteria = session.createCriteria(CityMmuMapping.class)
					.createAlias("masCity", "mc")
					.add(Restrictions.eq("mmuId",Long.parseLong(mmuId)))
					.add(Restrictions.eq("mc.indentCity", "y").ignoreCase())
					.add(Restrictions.eq("status", "a").ignoreCase())
					.addOrder(Order.asc("mc.cityName"));

			listCityMmuMapping = criteria.list();
			if(listCityMmuMapping.size()!=0) {
				
				for(CityMmuMapping cityMmuMapping:listCityMmuMapping) {
					list.add(cityMmuMapping.getMasCity());	
				}
			}
			if(list!=null && list.size()>1) {
			
				MasMMU masMMUObje=(MasMMU) session.createCriteria(MasMMU.class).add(Restrictions.eq("mmuId",Long.parseLong(mmuId))).uniqueResult();
				criteria = session.createCriteria(MasCity.class)
						.createAlias("masDistrict", "md")
						.add(Restrictions.eq("md.districtId",masMMUObje.getDistrictId()))
								.add(Restrictions.eq("indentCity","y").ignoreCase())
								.add(Restrictions.eq("status", "y").ignoreCase())
								.addOrder(Order.asc("cityName"));
						 
				list = criteria.list();
	
			}
			
			
			
			if(list.size()==0) {
				  criteria = session.createCriteria(CityMmuMapping.class)
						.createAlias("masCity", "mc")
						.add(Restrictions.eq("mmuId",Long.parseLong(mmuId)))
						.add(Restrictions.eq("status", "a").ignoreCase())
						.addOrder(Order.asc("mc.cityName"));
						 
				  listCityMmuMapping = criteria.list();
				  if(listCityMmuMapping.size()!=0) {
						
						for(CityMmuMapping cityMmuMapping:listCityMmuMapping) {
							list.add(cityMmuMapping.getMasCity());	
						}
					}
				if(list.size()>0) {
					
					MasCity masCity=list.get(0);
					
					 criteria = session.createCriteria(MasCity.class)
								.createAlias("masDistrict", "md")
								.add(Restrictions.eq("md.districtId",masCity.getMasDistrict().getDistrictId()))
										.add(Restrictions.eq("indentCity","y").ignoreCase())
										.add(Restrictions.eq("status", "y").ignoreCase())
										.addOrder(Order.asc("cityName"));
								 
						list = criteria.list();
				}
			}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return list;
	}

}
