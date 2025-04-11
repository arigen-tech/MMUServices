package com.mmu.services.dao.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaQuery;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mmu.services.dao.DispensaryDao;
import com.mmu.services.entity.MasCity;
import com.mmu.services.entity.MasDepartment;
import com.mmu.services.entity.MasHospital;
import com.mmu.services.entity.MasItemType;
import com.mmu.services.entity.MasStoreItem;
import com.mmu.services.entity.MasStoreSupplier;
import com.mmu.services.entity.MasStoreSupplierNew;
import com.mmu.services.entity.MasStoreSupplierType;
import com.mmu.services.entity.MasZone;
import com.mmu.services.entity.Patient;
import com.mmu.services.entity.PatientPrescriptionDt;
import com.mmu.services.entity.PatientPrescriptionHd;
import com.mmu.services.entity.StoreCoInternalIndentM;
import com.mmu.services.entity.StoreCoInternalIndentT;
import com.mmu.services.entity.StoreDoInternalIndentM;
import com.mmu.services.entity.StoreDoInternalIndentT;
import com.mmu.services.entity.StoreGrnM;
import com.mmu.services.entity.StoreInternalIndentM;
import com.mmu.services.entity.StoreInternalIndentT;
import com.mmu.services.entity.StoreIssueM;
import com.mmu.services.entity.StoreIssueT;
import com.mmu.services.entity.StoreItemBatchStock;
import com.mmu.services.entity.Users;
import com.mmu.services.hibernateutils.GetHibernateUtils;
import com.mmu.services.service.impl.DispensaryServiceImpl;
import com.mmu.services.utils.CommonUtil;
import com.mmu.services.utils.HMSUtil;

@Repository
@Transactional
public class DispensaryDaoImpl implements DispensaryDao {

	@Autowired
	GetHibernateUtils getHibernateUtils;

	@Autowired
	DispensaryServiceImpl dispenceryService;

	// -------------------------Start Create
	// indent(Anita)-----------------------------//

	@SuppressWarnings("unchecked")
	@Override
	public long savestoreInternalIndentT(StoreInternalIndentT storeInternalIndentT) {
		// TODO Auto-generated method stub
		long indentId = 0;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(StoreInternalIndentT.class);
			Transaction tx = session.beginTransaction();
			Serializable id = session.save(storeInternalIndentT);
			tx.commit();
			session.flush();
			session.clear();
			if (id != null) {
				indentId = (long) id;
			} else {
				indentId = 0;
			}
		} catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.getMessage();
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return indentId;
	}

	@Override
	public MasStoreItem getMasStoreItem(long itemId) {
		// TODO Auto-generated method stub
		MasStoreItem masStoreItemOld = null;
		List<MasStoreItem> MasStoreItemOldList = new ArrayList<MasStoreItem>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			MasStoreItemOldList = session.createCriteria(MasStoreItem.class).add(Restrictions.eq("itemId", itemId))
					.list();

			if (MasStoreItemOldList.size() > 0) {
				masStoreItemOld = MasStoreItemOldList.get(0);
			}
		} catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return masStoreItemOld;
	}

	@Override
	public MasHospital gettoMasHospital(long hospitalId) {
		// TODO Auto-generated method stub
		MasHospital masHospital = null;
		List<MasHospital> MasHospitalList = new ArrayList<MasHospital>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			MasHospitalList = session.createCriteria(MasHospital.class).add(Restrictions.eq("hospitalId", hospitalId))
					.list();

			if (MasHospitalList.size() > 0) {
				masHospital = MasHospitalList.get(0);
			}
		} catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return masHospital;
	}

	@Override
	public Users getUser(long userId) {
		// TODO Auto-generated method stub
		Users users = null;
		List<Users> UsersList = new ArrayList<Users>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			UsersList = session.createCriteria(Users.class).add(Restrictions.eq("userId", userId)).list();

			if (UsersList.size() > 0) {
				users = UsersList.get(0);
			}
		} catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return users;
	}

	@Override
	public long savestoreInternalIndentM(StoreInternalIndentM storeInternalIndentM) {
		// TODO Auto-generated method stub
		long indentId = 0;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(StoreInternalIndentM.class);
			Transaction tx = session.beginTransaction();
			Serializable id = session.save(storeInternalIndentM);
			tx.commit();
			session.flush();
			session.clear();
			if (id != null) {
				indentId = (long) id;
			} else {
				indentId = 0;
			}
		} catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.getMessage();
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return indentId;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<String, Object> getIndentList(long pageNo, HashMap<String, String> requestData) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		int pageSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
		int pageNo1 = 0;
		List totalMatches = new ArrayList();
		if (requestData.get("PN") != null)
			pageNo1 = Integer.parseInt(requestData.get("PN").toString());
		List<StoreInternalIndentM> storeInternalIndentMList = new ArrayList<StoreInternalIndentM>();
		JSONObject json = new JSONObject(requestData);
		long mmuId = Long.parseLong(json.getString("mmuId").toString());
		long departmentId = Long.parseLong(json.getString("departmentId").toString());

		Criteria cr = null;
		Date indentStratDate = null;
		Date indentEndDate = null;
		Date finalIndentEndDate = null;
		String startDate = "";
		String endDate = "";
		if (json.has("fromDate")) {
			startDate = json.getString("fromDate");
			endDate = json.getString("toDate");
			if ((!startDate.isEmpty() && startDate != null) && (!endDate.isEmpty() && endDate != null)) {
				indentStratDate = HMSUtil.convertStringDateToUtilDate(startDate, "dd/MM/yyyy");
				indentEndDate = HMSUtil.convertStringDateToUtilDate(endDate, "dd/MM/yyyy");

				Calendar cal = Calendar.getInstance();
				cal.setTime(indentEndDate);
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 59);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				// Date from = cal.getTime();
				cal.set(Calendar.HOUR_OF_DAY, 23);
				finalIndentEndDate = cal.getTime();

			}
		}

		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			cr = session.createCriteria(StoreInternalIndentM.class);
			cr = cr.add(Restrictions.eq("mmuId", mmuId));
			/*
			 * if (json.has("hospitalFlag")) { if (!json.getString("hospitalFlag").isEmpty()
			 * && json.getString("hospitalFlag").equalsIgnoreCase("F")) { cr =
			 * cr.add(Restrictions.eq("mmuId", mmuId)); } else { cr =
			 * cr.add(Restrictions.or(Restrictions.eq("mmuId", mmuId),
			 * Restrictions.eq("fwcParentMasHospital.hospitalId", hospitalId))); } }else{ cr
			 * = cr.add(Restrictions.eq("mmuId", mmuId)); }
			 */

			// cr = cr.add(Restrictions.eq("departmentId", departmentId));

			cr.addOrder(Order.desc("demandDate"));
			if (json.get("flag").toString().equalsIgnoreCase("Y")) {

				cr = cr.add(Restrictions.eq("status", "Y").ignoreCase());
			} else if (json.get("flag").toString().equalsIgnoreCase("YN")) {

				Criterion cr1 = Restrictions.eq("status", "Y").ignoreCase();
				Criterion cr2 = Restrictions.eq("status", "N").ignoreCase();
				LogicalExpression orExp = Restrictions.or(cr1, cr2);
				cr.add(orExp);
			} else if (json.get("flag").toString().equalsIgnoreCase("A")) {
				cr = cr.add(Restrictions.eq("status", "A").ignoreCase());
			} else if (json.get("flag").toString().equalsIgnoreCase("R")) {
				cr = cr.add(Restrictions.eq("status", "R").ignoreCase());
			}
			if ((!startDate.isEmpty() && startDate != null) && (!endDate.isEmpty() && endDate != null)) {
				cr = cr.add(Restrictions.ge("demandDate", indentStratDate))
						.add(Restrictions.lt("demandDate", finalIndentEndDate));
			}

			if (!cr.list().isEmpty() && cr.list().size() > 0) {

				totalMatches = cr.list();
				cr.setFirstResult((pageSize) * (pageNo1 - 1));
				cr.setMaxResults(pageSize);
				storeInternalIndentMList = cr.list();
			}
		} catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.getMessage();
			e.printStackTrace();
		} finally {

			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		map.put("storeInternalIndentMList", storeInternalIndentMList);
		map.put("totalMatches", totalMatches.size());
		return map;
	}

	@Override
	public Map<String, Object> getIndentDetails(HashMap<String, String> jsondata) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		Criteria cr = null;
		List<StoreInternalIndentT> storeInternalIndentTList = null;
		JSONObject json = new JSONObject(jsondata);
		long indentid = Long.parseLong(json.getString("indentId"));
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			cr = session.createCriteria(StoreInternalIndentT.class).createAlias("storeInternalIndentM1", "sm");
			cr = cr.add(Restrictions.eq("sm.id", indentid));
			cr.addOrder(Order.asc("id"));
			if (!cr.list().isEmpty() && cr.list().size() > 0) {

				storeInternalIndentTList = cr.list();
			}
		} catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.getMessage();
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		map.put("storeInternalIndentTList", storeInternalIndentTList);
		map.put("status", "1");
		return map;

	}
	
	@Override
	public Map<String, Object> getIndentDetailsCo(HashMap<String, String> jsondata) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		Criteria cr = null;
		List<StoreInternalIndentT> storeInternalIndentTList = null;
		JSONObject json = new JSONObject(jsondata);
		long indentid = Long.parseLong(json.getString("indentId"));
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			cr = session.createCriteria(StoreInternalIndentT.class).createAlias("storeInternalIndentM1", "sm");
			cr = cr.add(Restrictions.eq("sm.id", indentid));
			cr.addOrder(Order.asc("id"));
			if (!cr.list().isEmpty() && cr.list().size() > 0) {

				storeInternalIndentTList = cr.list();
			}
		} catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.getMessage();
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		map.put("storeInternalIndentTList", storeInternalIndentTList);
		map.put("status", "1");
		return map;

	}

	@Override
	public int deleteIndentItem(long indentId) {
		// TODO Auto-generated method stub
		int status = 0;
		Transaction tx = null;
		List<StoreInternalIndentT> storeInternalIndentTList = null;
		try {

			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			/*
			 * Criteria criteria = session.createCriteria(StoreInternalIndentT.class);
			 * criteria =criteria.add(Restrictions.eq("id", indentId));
			 * storeInternalIndentTList=criteria.list(); StoreInternalIndentT
			 * storeInternalIndentT = storeInternalIndentTList.get(0);
			 */
			Serializable id = new Long(indentId);
			StoreInternalIndentT persistentInstance = (StoreInternalIndentT) session.load(StoreInternalIndentT.class,
					id);
			if (persistentInstance != null) {
				session.delete(persistentInstance);
				tx.commit();
				status = 1;
				session.clear();
				session.flush();

			}
			// session.delete(storeInternalIndentT);

		} catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.getMessage();
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return status;
	}

	@Override
	public StoreInternalIndentT getStoreInternalIndentT(long indentId) {
		// TODO Auto-generated method stub
		StoreInternalIndentT storeInternalIndentT = null;
		List<StoreInternalIndentT> storeInternalIndentTList = new ArrayList<StoreInternalIndentT>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			storeInternalIndentTList = session.createCriteria(StoreInternalIndentT.class)
					.add(Restrictions.eq("id", indentId)).list();

			if (storeInternalIndentTList.size() > 0) {
				storeInternalIndentT = storeInternalIndentTList.get(0);
			}
		} catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return storeInternalIndentT;
	}

	@Override
	public long saveOrUpdatSstoreInternalIndentT(StoreInternalIndentT storeInternalIndentT) {
		// TODO Auto-generated method stub
		long indentId = 0;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(StoreInternalIndentM.class);
			Transaction tx = session.beginTransaction();
			session.saveOrUpdate(storeInternalIndentT);
			tx.commit();
			indentId = 1;
		} catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.getMessage();
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return indentId;
	}

	@Override
	public StoreInternalIndentM getStoreInternalIndentM(long indentmId) {
		// TODO Auto-generated method stub
		StoreInternalIndentM storeInternalIndentM = null;
		List<StoreInternalIndentM> storeInternalIndentMList = new ArrayList<StoreInternalIndentM>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			storeInternalIndentMList = session.createCriteria(StoreInternalIndentM.class)
					.add(Restrictions.eq("id", indentmId)).list();

			if (storeInternalIndentMList.size() > 0) {
				storeInternalIndentM = storeInternalIndentMList.get(0);
			}
		} catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return storeInternalIndentM;
	}

	@Override
	public void updateStoreInternalIndentM(StoreInternalIndentM storeInternalIndentM) {
		// TODO Auto-generated method stub
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(StoreInternalIndentM.class);
			Transaction tx = session.beginTransaction();
			session.update(storeInternalIndentM);
			tx.commit();

		} catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.getMessage();
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

	}

	@Override
	public MasDepartment getMasDepartment(long deptId) {
		// TODO Auto-generated method stub
		MasDepartment masDepartment = null;
		List<MasDepartment> masDepartmentList = new ArrayList<MasDepartment>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			masDepartmentList = session.createCriteria(MasDepartment.class).add(Restrictions.eq("departmentId", deptId))
					.list();

			if (masDepartmentList.size() > 0) {
				masDepartment = masDepartmentList.get(0);
			}
		} catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return masDepartment;
	}

	// code by Deepak
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getPendingPrescriptionList(Map<String, Object> jsondata) {
		Map<String, Object> map = new HashMap<>();
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		try {
			long mmuId = Long.parseLong(String.valueOf(jsondata.get("mmuId")));
			int pageNo = Integer.parseInt(String.valueOf(jsondata.get("pageNo")));
			int pagingSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
			int count = 0;
			String mobileNo = String.valueOf(jsondata.get("mobileNo"));
			String patientName = String.valueOf(jsondata.get("patientName"));

			Criteria criteria = session.createCriteria(PatientPrescriptionHd.class, "phd")
					.add(Restrictions.eq("mmuId", mmuId)).add(Restrictions.eq("status", "P").ignoreCase())
					.addOrder(Order.desc("prescriptionDate"));

			criteria = criteria.createAlias("phd.patient", "patient");
			if (mobileNo != null && !mobileNo.equals("") && !mobileNo.equals("null")) {

				criteria = criteria.add(Restrictions.eq("patient.mobileNumber", mobileNo.trim()));

			}
			if (patientName != null && !patientName.equals("") && !patientName.equals("null")) {

				String pName = "%" + patientName + "%";
				criteria = criteria.add(Restrictions.like("patient.patientName", pName).ignoreCase());

			}
			if (((patientName.equals("") || patientName.equals("null")) && (mobileNo.equals("") || mobileNo.equals("null")))) {

			
				Date date = new Date();
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
		 		cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				Date from = cal.getTime();
		 		cal.set(Calendar.HOUR_OF_DAY, 23);
				cal.set(Calendar.MINUTE,59);
		 		Date to = cal.getTime();
				criteria = criteria.add(Restrictions.between("prescriptionDate", from,to) );
			}
			List<PatientPrescriptionHd> list = criteria.list();
			count = list.size();

			criteria = criteria.setFirstResult(pagingSize * (pageNo - 1));
			criteria = criteria.setMaxResults(pagingSize);
			list = criteria.list();

			map.put("count", count);
			map.put("prescriptionList", list);
			return map;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getPrescriptionDetail(Map<String, Object> jsondata) {
		Map<String, Object> map = new HashMap<>();
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		// JSONObject jsondata = new JSONObject(data);
		try {
			long headerId = Integer.parseInt(String.valueOf(jsondata.get("id")));
			System.out.println("headerId " + headerId);
			List<PatientPrescriptionDt> detailList = session.createCriteria(PatientPrescriptionDt.class)
					.add(Restrictions.eq("prescriptionHdId", headerId)).add(Restrictions.eq("status", "P").ignoreCase())
					.list();

			System.out.println("detailList " + detailList.size());
			// map.put("headerDetail", headerDetail);
			map.put("detailList", detailList);
			return map;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getBatchDetail(Map<String, Object> jsondata) {
		Map<String, Object> map = new HashMap<>();
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		try {
			long itemId = Long.parseLong(String.valueOf(jsondata.get("item_id")));
			String mmuId = String.valueOf(jsondata.get("mmuId"));
			String cityId = String.valueOf(jsondata.get("cityId"));
			String districtId = String.valueOf(jsondata.get("districtId"));
			long departmentId = Long.parseLong(String.valueOf(jsondata.get("department_id")));
			int expiryDays = 0;

			if (!jsondata.containsKey("stockAdjustFlag")) { // this key comes in case of stock adjustment
				expiryDays = Integer.parseInt(HMSUtil.getProperties("adt.properties", "EXPIRY_DAYS"));
			}


			Date date = new Date();
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.add(Calendar.DATE, expiryDays);
			date = c.getTime();
			Criteria cr = session.createCriteria(StoreItemBatchStock.class)
					.createAlias("masStoreItem", "itm").add(Restrictions.eq("itm.itemId", itemId));
			
			if(mmuId != null && !mmuId.equals("") && !mmuId.equals("null") && !mmuId.equals("0")) {
				cr = cr.add(Restrictions.eq("mmuId", Long.parseLong(mmuId)));
			}
			
			if(cityId != null && !cityId.equals("") && !cityId.equals("null") && !cityId.equals("0")) {
				cr = cr.add(Restrictions.eq("cityId", Long.parseLong(cityId)));
			}
			
			if(districtId != null && !districtId.equals("") && !districtId.equals("null") && !districtId.equals("0")) {
				cr = cr.add(Restrictions.eq("districtId", Long.parseLong(districtId)));
			}
			
			cr = cr.createAlias("masDepartment", "md").add(Restrictions.eq("md.departmentId", departmentId))
					.add(Restrictions.gt("closingStock", new Long(0))).add(Restrictions.gt("expiryDate", date))
					.addOrder(Order.asc("expiryDate"));
			
			List<StoreItemBatchStock> dispensaryList = cr.list();

			map.put("dispensaryList", dispensaryList);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getPrescriptionHeader(Map<String, Object> jsondata) {
		Map<String, Object> map = new HashMap<>();
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		try {
			long headerId = Integer.parseInt(String.valueOf(jsondata.get("id")));
			List<PatientPrescriptionHd> headerDetail = session.createCriteria(PatientPrescriptionHd.class)
					.add(Restrictions.eq("prescriptionHdId", headerId)).list();

			map.put("headerDetail", headerDetail);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String issueMedicineFromDispensary(Map<String, Object> jsondata) {
		String msg = "";
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Transaction tx = session.beginTransaction();
		try {

			Calendar calendar = Calendar.getInstance();
			java.sql.Timestamp ourJavaTimestampObject = new java.sql.Timestamp(calendar.getTime().getTime());
			List<Map<String, Object>> listOfData = (List<Map<String, Object>>) jsondata.get("dtData");
			long mmuId = Long.parseLong(String.valueOf(jsondata.get("mmuId")));
			long departmentId = Long.parseLong(String.valueOf(jsondata.get("department_id")));
			long lastChangeBy = Long.parseLong(String.valueOf(jsondata.get("user_id")));
			long header_id = Long.parseLong(String.valueOf(jsondata.get("header_id")));
			boolean storeIssueMFlag = false;
			for (Map<String, Object> map : listOfData) {
				String checkStockID = String.valueOf(map.get("stock_id"));
				if (checkStockID != null && !checkStockID.equals("") && !checkStockID.equals("null")) {
					storeIssueMFlag = true;
				}
			}
			List<StoreIssueM> storeIssueMList = (List<StoreIssueM>) session.createCriteria(StoreIssueM.class)
					.add(Restrictions.eq("prescriptionId", header_id)).list();
			long issueMid = 0;
			if (storeIssueMFlag) {
				if (storeIssueMList.isEmpty()) {
					StoreIssueM storeIssueM = new StoreIssueM();
					storeIssueM.setIssueType("P");
					storeIssueM.setIssueDate(new Date());
					storeIssueM.setMmuId(mmuId);
					storeIssueM.setDepartmentId(departmentId);
					storeIssueM.setPrescriptionId(header_id);
					storeIssueM.setLastChgDate(ourJavaTimestampObject);
					storeIssueM.setLastChgBy(lastChangeBy);
					storeIssueM.setIssuedBy(lastChangeBy);
					storeIssueM.setIssueDate(ourJavaTimestampObject);
					issueMid = (long) session.save(storeIssueM);
				} else {
					for (StoreIssueM storeIssueM : storeIssueMList) {
						issueMid = storeIssueM.getId();
					}
				}
			}
			System.out.println("issueMid " + issueMid);
			/*
			 * tx.commit(); tx = session.beginTransaction();
			 */
			// boolean statusFlag = false;
			for (Map<String, Object> map : listOfData) {
				// header_id = Long.parseLong(String.valueOf(map.get("header_id")));
				long itemId = Long.parseLong(String.valueOf(map.get("item_id")));
				long detailId = Long.parseLong(String.valueOf(map.get("detail_id")));
				String checkStockID = String.valueOf(map.get("stock_id"));
				// String nisFlag = String.valueOf(map.get("nisFlag"));
				long stockId = 0;
				boolean flag = true;
				Date expiryDate = null;
				if (checkStockID == null || checkStockID.equals("") || checkStockID.equals("null")) {
					stockId = 0;
					flag = false;
				} else {
					stockId = Long.parseLong(checkStockID);
					expiryDate = HMSUtil.convertStringTypeDateToDateType(String.valueOf(map.get("date_of_expiry")));
				}
				if (checkStockID == null || checkStockID.equals("") || checkStockID.equals("null")) {
					PatientPrescriptionDt pDt = (PatientPrescriptionDt) session.get(PatientPrescriptionDt.class,
							detailId);
					if (pDt != null) {
						/*
						 * if(nisFlag.equals("N")) { pDt.setStatus("W"); pDt.setQtyIssued(new Long(0));
						 * pDt.setLastChgDate(ourJavaTimestampObject); session.save(pDt); statusFlag =
						 * true; }else { pDt.setStatus("C"); pDt.setQtyIssued(new Long(0));
						 * pDt.setLastChgDate(ourJavaTimestampObject); session.save(pDt); }
						 */
						pDt.setStatus("C");
						pDt.setQtyIssued(new Long(0));
						pDt.setLastChgDate(ourJavaTimestampObject);
						session.save(pDt);

					}
					continue;
				}
				String dosage = String.valueOf(map.get("dosage"));
				long no_of_days = Long.parseLong(String.valueOf(map.get("no_of_days")));
				long frequencyId = Long.parseLong(String.valueOf(map.get("frequency_id")));
				long qtyPrescribed = Long.parseLong(String.valueOf(map.get("qty_prescribed")));
				// long stockId = Long.parseLong(String.valueOf(map.get("stock_id")));
				String batchNo = String.valueOf(map.get("batch_no"));
				long issuedQty = Long.parseLong(String.valueOf(map.get("qty_issued")));

				// long storeStock = Long.parseLong(String.valueOf(map.get("store_stock")));
				long prescribedQty = Long.parseLong(String.valueOf(map.get("qty_prescribed")));
				// Date expiryDate =
				// HMSUtil.convertStringTypeDateToDateType(String.valueOf(map.get("date_of_expiry")));

				int expiryDays = Integer.parseInt(HMSUtil.getProperties("adt.properties", "EXPIRY_DAYS"));
				long dispStock = 0;
				long batchStock = 0;
				Date date = new Date();
				Calendar c = Calendar.getInstance();
				c.setTime(date);
				c.add(Calendar.DATE, expiryDays);
				date = c.getTime();
				String sql = "select CLOSING_STOCK, STOCK_ID from STORE_ITEM_BATCH_STOCK where ITEM_ID =:item_id and mmu_id =:mmuId and DEPARTMENT_ID =:dept_id and CLOSING_STOCK > 0 and EXPIRY_DATE >:exp_date";
				Query query = session.createSQLQuery(sql);
				query.setLong("item_id", itemId);
				query.setLong("mmuId", mmuId);
				// query.setLong("batchId", stockId);
				query.setLong("dept_id", departmentId);
				query.setDate("exp_date", date);
				List<Object[]> result = query.list();
				for (Object[] obj : result) {
					if (obj[0] != null) {
						dispStock += ((Double) obj[0]).longValue();
					}
					if (stockId == ((BigInteger) obj[1]).longValue()) {
						if (obj[1] != null) {
							batchStock = ((Double) obj[0]).longValue();
						}
					}

				}
				if (batchStock >= issuedQty) {
					dispStock = dispStock - issuedQty;
					batchStock = batchStock - issuedQty;
				} else {
					msg = "No Enough Stock in Selected Batch";
				}
				long dtId = 0;
				long quantityBalance = prescribedQty - issuedQty;
				if (detailId != 0) {

					long issuedQt = 0;
					for (Map<String, Object> data : listOfData) {
						long item_id = Long.parseLong(String.valueOf(data.get("item_id")));
						if (itemId == item_id) {
							issuedQt = issuedQt + Long.parseLong(String.valueOf(data.get("qty_issued")));
						}
					}

					PatientPrescriptionDt pDt = (PatientPrescriptionDt) session.get(PatientPrescriptionDt.class,
							detailId);
					if (pDt != null) {
						pDt.setQtyIssued(issuedQt);
						pDt.setBatchExpiryDate(expiryDate);
						// pDt.setDispStock(dispStock);
						// pDt.setStoreStock(storeStock);
						pDt.setQtyBalance(quantityBalance);
						/*
						 * if(nisFlag.equals("N")) { if (issuedQty == prescribedQty) {
						 * pDt.setStatus("C"); } else if (issuedQty < prescribedQty) {
						 * pDt.setStatus("W"); statusFlag = true; }
						 * 
						 * }else if(nisFlag.equals("Y")){ pDt.setStatus("C"); //pDt.setNisFlag("Y"); }
						 */

						pDt.setStatus("C");
						pDt.setLastChgDate(ourJavaTimestampObject);
						pDt.setIssuedDate(ourJavaTimestampObject);
						pDt.setIssuedBy(lastChangeBy);
						session.update(pDt);
					}

				}

				StoreItemBatchStock bStock = (StoreItemBatchStock) session.get(StoreItemBatchStock.class, stockId);
				if(bStock.getIssueQty() != null) {
					bStock.setIssueQty(bStock.getIssueQty() + issuedQty);
				}else {
					bStock.setIssueQty(issuedQty);
				}
				
				bStock.setClosingStock(batchStock);
				bStock.setLastChgDate(ourJavaTimestampObject);
				session.update(bStock);

				StoreIssueT storeIssueT = new StoreIssueT();
				storeIssueT.setQtyIssued(issuedQty);
				StoreIssueM issueM = new StoreIssueM();
				issueM.setId(issueMid);
				storeIssueT.setStoreIssueM(issueM);
				storeIssueT.setItemId(itemId);
				if (flag) {
					storeIssueT.setBatchStockId(stockId);
					storeIssueT.setExpiryDate(expiryDate);
					storeIssueT.setBatchNo(batchNo);
				}
				storeIssueT.setQtyRequest(qtyPrescribed);
				PatientPrescriptionDt pDt = new PatientPrescriptionDt();
				pDt.setPrescriptionDtId(detailId);
				storeIssueT.setPatientPrescriptionDt(pDt);
				storeIssueT.setIssueDate(ourJavaTimestampObject);
				Users user = new Users();
				user.setUserId(lastChangeBy);
				storeIssueT.setUsers(user);
				session.save(storeIssueT);
			}

			List<PatientPrescriptionDt> patientList = session.createCriteria(PatientPrescriptionDt.class)
					.add(Restrictions.eq("prescriptionHdId", header_id))
					.add(Restrictions.eq("status", "P").ignoreCase()).list();
			if (patientList.isEmpty()) {
				PatientPrescriptionHd pHd = (PatientPrescriptionHd) session.get(PatientPrescriptionHd.class, header_id);
				if (pHd != null) {
					/*
					 * if(statusFlag) { pHd.setStatus("W"); //pHd.setIssuedBy(lastChangeBy); }else {
					 * pHd.setStatus("I"); //pHd.setIssuedBy(lastChangeBy); }
					 */
					pHd.setStatus("I");
					pHd.setDepartmentId(departmentId);
					pHd.setLastChgDate(ourJavaTimestampObject);
					pHd.setIssuedBy(lastChangeBy);
					session.update(pHd);
				}
			}

			tx.commit();
			msg = "success";
			return msg;
		} catch (Exception ex) {
			tx.rollback();
			ex.printStackTrace();
			msg = "Error while issuing Medicine";
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			tx = null;
		}
		return msg;
	}

	@Override
	public Map<String, Object> getPartialWaitingList(Map<String, Object> jsondata) {
		Map<String, Object> map = new HashMap<>();
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		try {
			long hospitalId = Long.parseLong(String.valueOf(jsondata.get("hospitalId")));
			int pageNo = Integer.parseInt(String.valueOf(jsondata.get("pageNo")));
			int pagingSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
			int count = 0;
			String mobileNo = String.valueOf(jsondata.get("mobileNo"));
			String patientName = String.valueOf(jsondata.get("patientName"));
			String serviceNo = String.valueOf(jsondata.get("serviceNo"));

			Criteria criteria = session.createCriteria(PatientPrescriptionHd.class, "phd")
					.add(Restrictions.eq("hospitalId", hospitalId)).add(Restrictions.eq("status", "W").ignoreCase())
					.addOrder(Order.desc("prescriptionDate"));

			criteria = criteria.createAlias("phd.patient", "patient");
			if (mobileNo != null && !mobileNo.equals("") && !mobileNo.equals("null")) {

				criteria = criteria.add(Restrictions.eq("patient.mobileNumber", mobileNo.trim()));

			}
			if (patientName != null && !patientName.equals("") && !patientName.equals("null")) {

				String pName = "%" + patientName + "%";
				criteria = criteria.add(Restrictions.like("patient.patientName", pName).ignoreCase());

			}
			if (serviceNo != null && !serviceNo.equals("") && !serviceNo.equals("null")) {

				criteria = criteria.add(Restrictions.eq("patient.serviceNo", serviceNo).ignoreCase());

			}

			List<PatientPrescriptionHd> list = criteria.list();
			count = list.size();

			criteria = criteria.setFirstResult(pagingSize * (pageNo - 1));
			criteria = criteria.setMaxResults(pagingSize);
			list = criteria.list();

			map.put("count", count);
			map.put("prescriptionList", list);
			return map;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getPartialIssueHeader(Map<String, Object> jsondata) {
		Map<String, Object> map = new HashMap<>();
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		try {
			long headerId = Integer.parseInt(String.valueOf(jsondata.get("id")));
			List<PatientPrescriptionHd> headerDetail = session.createCriteria(PatientPrescriptionHd.class)
					.add(Restrictions.eq("prescriptionHdId", headerId)).list();

			map.put("headerDetail", headerDetail);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getPartialIssueDetails(Map<String, Object> jsondata) {
		Map<String, Object> map = new HashMap<>();
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		try {
			long headerId = Integer.parseInt(String.valueOf(jsondata.get("id")));

			List<PatientPrescriptionDt> detailList = session.createCriteria(PatientPrescriptionDt.class)
					.add(Restrictions.eq("prescriptionHdId", headerId)).add(Restrictions.eq("status", "W")).list();

			System.out.println("detailList " + detailList.size());
			// map.put("headerDetail", headerDetail);
			map.put("detailList", detailList);
			return map;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String partialIssueMedicineFromDispensary(Map<String, Object> jsondata) {
		String msg = "";
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Transaction tx = session.beginTransaction();
		try {

			Calendar calendar = Calendar.getInstance();
			java.sql.Timestamp ourJavaTimestampObject = new java.sql.Timestamp(calendar.getTime().getTime());
			List<Map<String, Object>> listOfData = (List<Map<String, Object>>) jsondata.get("dtData");
			long hospitalId = Long.parseLong(String.valueOf(jsondata.get("hospital_id")));
			long departmentId = Long.parseLong(String.valueOf(jsondata.get("department_id")));
			long lastChangeBy = Long.parseLong(String.valueOf(jsondata.get("user_id")));
			long header_id = Long.parseLong(String.valueOf(jsondata.get("header_id")));
			boolean storeIssueMFlag = false;
			for (Map<String, Object> map : listOfData) {
				String checkStockID = String.valueOf(map.get("stock_id"));
				if (checkStockID != null && !checkStockID.equals("") && !checkStockID.equals("null")) {
					storeIssueMFlag = true;
				}
			}

			List<StoreIssueM> storeIssueMList = (List<StoreIssueM>) session.createCriteria(StoreIssueM.class)
					.add(Restrictions.eq("prescriptionId", header_id)).list();
			long issueMid = 0;
			if (storeIssueMFlag) {
				if (storeIssueMList.isEmpty()) {
					StoreIssueM storeIssueM = new StoreIssueM();
					storeIssueM.setIssueType("P");
					storeIssueM.setIssueDate(new Date());
					storeIssueM.setHospitalId(hospitalId);
					storeIssueM.setDepartmentId(departmentId);
					storeIssueM.setPrescriptionId(header_id);
					storeIssueM.setLastChgDate(ourJavaTimestampObject);
					storeIssueM.setLastChgBy(lastChangeBy);
					storeIssueM.setIssuedBy(lastChangeBy);
					storeIssueM.setIssueDate(ourJavaTimestampObject);
					issueMid = (long) session.save(storeIssueM);
				} else {
					for (StoreIssueM storeIssueM : storeIssueMList) {
						issueMid = storeIssueM.getId();
					}
				}
			}
			System.out.println("issueMid " + issueMid);
			/*
			 * tx.commit(); tx = session.beginTransaction();
			 */
			boolean statusFlag = false;
			for (Map<String, Object> map : listOfData) {
				// header_id = Long.parseLong(String.valueOf(map.get("header_id")));
				long itemId = Long.parseLong(String.valueOf(map.get("item_id")));
				long detailId = Long.parseLong(String.valueOf(map.get("detail_id")));
				String nisFlag = String.valueOf(map.get("nisFlag"));
				String checkStockID = String.valueOf(map.get("stock_id"));
				if (checkStockID == null || checkStockID.equals("") || checkStockID.equals("null")) {
					PatientPrescriptionDt pDt = (PatientPrescriptionDt) session.get(PatientPrescriptionDt.class,
							detailId);
					if (pDt != null) {
						if (nisFlag.equals("N")) {
							pDt.setStatus("C");
							pDt.setQtyIssued(new Long(0));
							pDt.setLastChgDate(ourJavaTimestampObject);
							session.save(pDt);
							statusFlag = true;
						} else {
							pDt.setStatus("C");
							pDt.setQtyIssued(new Long(0));
							// pDt.setNisFlag("Y");
							pDt.setLastChgDate(ourJavaTimestampObject);
							session.save(pDt);
							// statusFlag = false;
						}
					}
					continue;
				}

				long stockId = 0;
				boolean flag = true;
				Date expiryDate = null;
				// String checkStockID = String.valueOf(map.get("stock_id"));
				if (checkStockID == null || checkStockID.equals("") || checkStockID.equals("null")) {
					stockId = 0;
					flag = false;
				} else {
					stockId = Long.parseLong(checkStockID);
					expiryDate = HMSUtil.convertStringTypeDateToDateType(String.valueOf(map.get("date_of_expiry")));
				}

				String dosage = String.valueOf(map.get("dosage"));
				long no_of_days = Long.parseLong(String.valueOf(map.get("no_of_days")));
				long frequencyId = Long.parseLong(String.valueOf(map.get("frequency_id")));
				long qtyPrescribed = Long.parseLong(String.valueOf(map.get("qty_prescribed")));
				// long stockId = Long.parseLong(String.valueOf(map.get("stock_id")));
				String batchNo = String.valueOf(map.get("batch_no"));
				long issuedQty = Long.parseLong(String.valueOf(map.get("qty_issued")));

				// long storeStock = Long.parseLong(String.valueOf(map.get("store_stock")));
				long prescribedQty = Long.parseLong(String.valueOf(map.get("qty_prescribed")));

				// Date expiryDate =
				// HMSUtil.convertStringTypeDateToDateType(String.valueOf(map.get("date_of_expiry")));
				/*
				 * if(issuedQty == 0) { PatientPrescriptionDt pDt = (PatientPrescriptionDt)
				 * session.get(PatientPrescriptionDt.class, detailId); if(pDt != null) {
				 * pDt.setStatus("W"); session.save(pDt); } continue; }
				 */
				// check value in db
				int expiryDays = Integer.parseInt(HMSUtil.getProperties("adt.properties", "EXPIRY_DAYS"));
				long dispStock = 0;
				long batchStock = 0;
				Date date = new Date();
				Calendar c = Calendar.getInstance();
				c.setTime(date);
				c.add(Calendar.DATE, expiryDays);
				date = c.getTime();
				String sql = "select CLOSING_STOCK, STOCK_ID from STORE_ITEM_BATCH_STOCK where ITEM_ID =:item_id and hospital_id =:hospital_id and DEPARTMENT_ID =:dept_id and CLOSING_STOCK > 0 and EXPIRY_DATE >:exp_date";
				Query query = session.createSQLQuery(sql);
				query.setLong("item_id", itemId);
				query.setLong("hospital_id", hospitalId);
				// query.setLong("batchId", stockId);
				query.setLong("dept_id", departmentId);
				query.setDate("exp_date", date);
				List<Object[]> result = query.list();
				for (Object[] obj : result) {
					if (obj[0] != null) {
						dispStock += ((BigDecimal) obj[0]).longValue();
					}
					if (stockId == ((BigDecimal) obj[1]).longValue()) {
						if (obj[1] != null) {
							batchStock = ((BigDecimal) obj[0]).longValue();
						}
					}

				}
				if (batchStock >= issuedQty) {
					dispStock = dispStock - issuedQty;
					batchStock = batchStock - issuedQty;
				} else {
					msg = "No Enough Stock in Selected Batch";
				}
				long dtId = 0;
				long quantityBalance = prescribedQty - issuedQty;
				if (detailId != 0) {
					long issuedQt = 0;
					for (Map<String, Object> data : listOfData) {
						long item_id = Long.parseLong(String.valueOf(data.get("item_id")));
						if (itemId == item_id) {
							issuedQt = issuedQt + Long.parseLong(String.valueOf(data.get("qty_issued")));
						}
					}
					PatientPrescriptionDt pDt = (PatientPrescriptionDt) session.get(PatientPrescriptionDt.class,
							detailId);
					if (pDt != null) {
						if (pDt.getQtyIssued() != null) {
							issuedQt = issuedQt + pDt.getQtyIssued();
						}
						pDt.setQtyIssued(issuedQt);
						pDt.setBatchExpiryDate(expiryDate);
						// pDt.setDispStock(dispStock);
						// pDt.setStoreStock(storeStock);
						pDt.setQtyBalance(quantityBalance);
						if (nisFlag.equals("N")) {
							if (issuedQty == prescribedQty) {
								pDt.setStatus("C");
							} else if (issuedQty < prescribedQty) {
								pDt.setStatus("C");
								statusFlag = true;
							}
						} else if (nisFlag.equals("Y")) {
							pDt.setStatus("C");
							// pDt.setNisFlag("Y");
						}

						// pDt.setIssuedDate(ourJavaTimestampObject);
						pDt.setLastChgDate(ourJavaTimestampObject);
						session.update(pDt);
					}

				}

				StoreItemBatchStock bStock = (StoreItemBatchStock) session.get(StoreItemBatchStock.class, stockId);
				bStock.setIssueQty(issuedQty);
				bStock.setClosingStock(batchStock);
				bStock.setLastChgDate(ourJavaTimestampObject);
				session.update(bStock);

				StoreIssueT storeIssueT = new StoreIssueT();
				storeIssueT.setQtyIssued(issuedQty);
				StoreIssueM issueM = new StoreIssueM();
				issueM.setId(issueMid);
				storeIssueT.setStoreIssueM(issueM);
				storeIssueT.setItemId(itemId);
				if (flag) {
					storeIssueT.setBatchStockId(stockId);
					storeIssueT.setExpiryDate(expiryDate);
					storeIssueT.setBatchNo(batchNo);
				}
				storeIssueT.setIssueDate(ourJavaTimestampObject);
				Users user = new Users();
				user.setUserId(lastChangeBy);
				storeIssueT.setUsers(user);
				session.save(storeIssueT);
			}

			List<PatientPrescriptionDt> patientList = session.createCriteria(PatientPrescriptionDt.class)
					.add(Restrictions.eq("prescriptionHdId", header_id))
					.add(Restrictions.eq("status", "P").ignoreCase()).list();
			if (patientList.isEmpty()) {
				PatientPrescriptionHd pHd = (PatientPrescriptionHd) session.get(PatientPrescriptionHd.class, header_id);
				if (pHd != null) {
					if (statusFlag) {
						pHd.setStatus("I");
						// pHd.setIssuedBy(lastChangeBy);
					} else {
						pHd.setStatus("I");
						// pHd.setIssuedBy(lastChangeBy);
					}

					pHd.setLastChgDate(ourJavaTimestampObject);
					session.update(pHd);
				}
			}

			tx.commit();
			msg = "success";
			return msg;
		} catch (Exception ex) {
			tx.rollback();
			ex.printStackTrace();
			msg = "Error while issuing Medicine";
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			tx = null;
		}
		return msg;
	}

	// end by Deepak
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> indentIssueWaitingList(Map<String, Object> jsondata) {
		Map<String, Object> map = new HashMap<String, Object>();
		int pageSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
		int pageNo1 = 0;
		int count = 0;
		if (jsondata.get("pageNo") != null)
			pageNo1 = Integer.parseInt(jsondata.get("pageNo").toString());
		List<StoreInternalIndentM> storeInternalIndentMList = new ArrayList<StoreInternalIndentM>();
		JSONObject json = new JSONObject(jsondata);
		Long cityId = Long.parseLong(String.valueOf(jsondata.get("cityId")));
		Criteria cr = null;

		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			cr = session.createCriteria(StoreInternalIndentM.class);
			cr = cr.add(Restrictions.eq("cityId", cityId));
			cr = cr.add(Restrictions.or(Restrictions.eq("status", "C").ignoreCase(), Restrictions.eq("status", "P").ignoreCase()));
			cr.addOrder(Order.desc("demandDate"));
			count = cr.list().size();

			cr.setFirstResult((pageSize) * (pageNo1 - 1));
			cr.setMaxResults(pageSize);
			storeInternalIndentMList = cr.list();

			map.put("storeInternalIndentMList", storeInternalIndentMList);
			map.put("count", count);

			return map;
		} catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
		} finally {

			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		map.put("storeInternalIndentMList", storeInternalIndentMList);
		map.put("count", 0);
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getIndentIssueHeader(Map<String, Object> jsondata) {
		Map<String, Object> map = new HashMap<>();
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		try {
			long headerId = Long.parseLong(String.valueOf(jsondata.get("id")));

			List<StoreInternalIndentM> headerList = (List<StoreInternalIndentM>) session
					.createCriteria(StoreInternalIndentM.class).add(Restrictions.eq("id", headerId)).list();

			map.put("headerList", headerList);
			return map;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getIndentIssueDetails(Map<String, Object> jsondata) {
		Map<String, Object> map = new HashMap<>();
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		try {
			long headerId = Long.parseLong(String.valueOf(jsondata.get("id")));

			List<StoreInternalIndentT> detailList = (List<StoreInternalIndentT>) session
					.createCriteria(StoreInternalIndentT.class).createAlias("storeInternalIndentM1", "hd")
					.add(Restrictions.eq("hd.id", headerId)).list();
			map.put("detailList", detailList);
			return map;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String,Object> indentIssue(Map<String, Object> jsondata) {
		String msg = "";
		Map<String,Object> response = new HashMap<String, Object>();
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Transaction tx = session.beginTransaction();
		try {

			Calendar calendar = Calendar.getInstance();
			java.sql.Timestamp ourJavaTimestampObject = new java.sql.Timestamp(calendar.getTime().getTime());
			List<Map<String, Object>> listOfData = (List<Map<String, Object>>) jsondata.get("dtData");
			long cityId = Long.parseLong(String.valueOf(jsondata.get("cityId")));
			long lastChangeBy = Long.parseLong(String.valueOf(jsondata.get("user_id")));
			long header_id = Long.parseLong(String.valueOf(jsondata.get("header_id")));
			String issueFlag = String.valueOf(jsondata.get("flag"));
			Long mmuId = null;
			StoreInternalIndentM sim = (StoreInternalIndentM) session.get(StoreInternalIndentM.class, header_id);
			if (sim != null) {
				mmuId = sim.getMmuId();
			}

			/*
			 * List<StoreIssueM> storeIssueMList = (List<StoreIssueM>)
			 * session.createCriteria(StoreIssueM.class) .add(Restrictions.eq("indentMId",
			 * header_id)).list();
			 */
			long issueMid = 0;
			StoreIssueM storeIssueM = new StoreIssueM();
			storeIssueM.setIssueType("I");
			storeIssueM.setIssueDate(new Date());
			storeIssueM.setIndentMId(header_id);
			storeIssueM.setLastChgDate(ourJavaTimestampObject);
			storeIssueM.setLastChgBy(lastChangeBy);
			storeIssueM.setIssuedBy(lastChangeBy);
			storeIssueM.setIssueDate(ourJavaTimestampObject);
			storeIssueM.setMmuId(mmuId);
			issueMid = (long) session.save(storeIssueM);
			
			/*
			 * if (storeIssueMList.isEmpty()) { StoreIssueM storeIssueM = new StoreIssueM();
			 * storeIssueM.setIssueType("I"); storeIssueM.setIssueDate(new Date());
			 * storeIssueM.setIndentMId(header_id);
			 * storeIssueM.setLastChgDate(ourJavaTimestampObject);
			 * storeIssueM.setLastChgBy(lastChangeBy);
			 * storeIssueM.setIssuedBy(lastChangeBy);
			 * storeIssueM.setIssueDate(ourJavaTimestampObject);
			 * storeIssueM.setMmuId(mmuId); issueMid = (long) session.save(storeIssueM); }
			 * else { for (StoreIssueM storeIssueM : storeIssueMList) { issueMid =
			 * storeIssueM.getId(); } }
			 */
			System.out.println("issueMid " + issueMid);
			/*
			 * tx.commit(); tx = session.beginTransaction();
			 */
			for (Map<String, Object> map : listOfData) {
				// header_id = Long.parseLong(String.valueOf(map.get("header_id")));
				long itemId = Long.parseLong(String.valueOf(map.get("item_id")));
				long detailId = Long.parseLong(String.valueOf(map.get("detail_id")));
				long qtyDemanded = (long) Double.parseDouble(String.valueOf(map.get("qty_demanded")));
				Long issuedQty = (long) Double.parseDouble(String.valueOf(map.get("qty_issued")));
				System.out.println("issue quantity " + issuedQty);
				// long balanceAfterIssue =
				// Long.parseLong(String.valueOf(map.get("balance_after_issue")));
				boolean flag = true;
				Date expiryDate = null;
				long stockId = 0;
				String checkStockID = String.valueOf(map.get("stock_id"));
				if (checkStockID == null || checkStockID.equals("") || checkStockID.equals("null")) {
					stockId = 0;
					flag = false;
				} else {
					stockId = Long.parseLong(checkStockID);
					expiryDate = HMSUtil.convertStringTypeDateToDateType(String.valueOf(map.get("date_of_expiry")));
				}

				/*
				 * String checkStockID = String.valueOf(map.get("stock_id")); if(checkStockID ==
				 * null || checkStockID.equals("") || checkStockID.equals("null")) { continue; }
				 */
				String batchNo = String.valueOf(map.get("batch_no"));
				// Date manufacturingDate =
				// HMSUtil.convertStringTypeDateToDateType(String.valueOf(map.get("date_of_manufacturing")));

				// long bStock = Long.parseLong(String.valueOf(map.get("batch_stock")));
				// long availableStock = Long.parseLong(String.valueOf(map.get("disp_stock")));

				int expiryDays = Integer.parseInt(HMSUtil.getProperties("adt.properties", "EXPIRY_DAYS"));
				long dispStock = 0;
				long batchStock = 0;
				Date date = new Date();
				Calendar c = Calendar.getInstance();
				c.setTime(date);
				c.add(Calendar.DATE, expiryDays);
				date = c.getTime();
				// checking exact batch and total batch stock at runtime
				String sql = "select CLOSING_STOCK, STOCK_ID,mrp,batch_no from STORE_ITEM_BATCH_STOCK where ITEM_ID =:item_id and city_id =:cityId and CLOSING_STOCK > 0 and EXPIRY_DATE >:exp_date";
				Query query = session.createSQLQuery(sql);
				query.setLong("item_id", itemId);
				query.setLong("cityId", cityId);
				query.setDate("exp_date", date);
				List<Object[]> result = query.list();
				Double costPriceMrp=0.0;
				for (Object[] obj : result) {
					if (obj[0] != null) {
						dispStock += ((Double) obj[0]).longValue();
					}
					if (stockId == ((BigInteger) obj[1]).longValue()) {
						if (obj[1] != null) {
							batchStock = ((Double) obj[0]).longValue();
						}
					}
					///////////////////////Added by kumar Avinash//////////////////////////////////
					 if(obj[2] != null) {
					  if(obj[3]!=null && obj[3].toString().equalsIgnoreCase(batchNo))
						 costPriceMrp=((Double) obj[2]).doubleValue();
					 }
					 /////////////////////////////////////////////////////////
				}
				if (batchStock >= issuedQty) {
					dispStock = dispStock - issuedQty;
					batchStock = batchStock - issuedQty;
				} else {
					msg = "No Enough Stock in  Selected Batch";
				}
				///////////////////////Added by kumar Avinash//////////////////////////////////
				
				 
				//////////////////////////////////////////
				long dtId = 0;
				// saving or updating record on the basis of detail id
				// long quantityBalance = qtyDemanded - issuedQty;
				if (detailId != 0) {
					/*
					 * long issuedQt = 0; for (Map<String, Object> data : listOfData) { long item_id
					 * = Long.parseLong(String.valueOf(data.get("item_id"))); if (itemId == item_id)
					 * { issuedQt = issuedQt + (long)
					 * Double.parseDouble(String.valueOf(data.get("qty_issued"))); } }
					 */

					StoreInternalIndentT sIt = (StoreInternalIndentT) session.get(StoreInternalIndentT.class, detailId);
					if (sIt != null) {
						// sIt.setAvailableStock(dispStock);
						if(sIt.getQtyIssued() == null) {
							sIt.setQtyIssued(issuedQty);
							
							
						}else {
							sIt.setQtyIssued(sIt.getQtyIssued() + issuedQty);
						}
						////////////////////////Kumar Avinash///////////////////////
						//if(issueFlag.equalsIgnoreCase("f")) {
							 //finalValueCostPrize=costPriceMrp*issuedQty;
						if(sIt.getTotalCost()==null) {
							 sIt.setTotalCost(costPriceMrp* issuedQty);
						}
						else {
							//if(issueFlag.equalsIgnoreCase("f"))
							 sIt.setTotalCost(sIt.getTotalCost()+(costPriceMrp* issuedQty));
							 
						}
				 			
						//}
						/////////////////////////////////////////////
						session.update(sIt);
					}

				}
				
				Double costForIssueQty=0.0;
				// updating the stock
				StoreItemBatchStock bStock = (StoreItemBatchStock) session.get(StoreItemBatchStock.class, stockId);
				if (bStock != null) {
					if(bStock.getIssueQty() != null) {
						bStock.setIssueQty(bStock.getIssueQty() + issuedQty);
						if(bStock.getMrp()!=null)
							costForIssueQty= (Double.parseDouble(issuedQty.toString()))* (Double.parseDouble(bStock.getMrp().toString()));
							 
					}else {
						bStock.setIssueQty(issuedQty);
						costForIssueQty= (issuedQty)* (costPriceMrp);
					}
					
					bStock.setClosingStock(batchStock);
					bStock.setLastChgDate(ourJavaTimestampObject);
					session.update(bStock);
				}

				// saving record into transaction table
				StoreIssueT storeIssueT = new StoreIssueT();
				storeIssueT.setQtyIssued(issuedQty);
				StoreIssueM issueM = new StoreIssueM();
				issueM.setId(issueMid);
				storeIssueT.setStoreIssueM(issueM);
				storeIssueT.setItemId(itemId);
				if (flag) {
					storeIssueT.setBatchStockId(stockId);
					storeIssueT.setExpiryDate(expiryDate);
					storeIssueT.setBatchNo(batchNo);
				}
				// storeIssueT.setQtyRequest(qtyDemanded);
				storeIssueT.setQtyRequest(qtyDemanded);
				StoreInternalIndentT sit = new StoreInternalIndentT();
				sit.setId(detailId);
				System.out.println("detailId " + detailId);
				storeIssueT.setStoreInternalIndentT(sit);
				storeIssueT.setIssueDate(ourJavaTimestampObject);
				Users user = new Users();
				user.setUserId(lastChangeBy);
				storeIssueT.setUsers(user);
				////////////////////added by kumar avinash///////////////////////////////
				storeIssueT.setTotalCost(costForIssueQty);
				////////////////////////////////////////////////
				session.save(storeIssueT);
			}
			StoreInternalIndentM simM = (StoreInternalIndentM) session.get(StoreInternalIndentM.class, header_id);
			if (simM != null) {
				if(issueFlag.equalsIgnoreCase("P")) {
					simM.setStatus("P");
					simM.setCoFlag("P");
				}else {
					simM.setStatus("I");
					simM.setCoFlag("I");
				}
				
				session.save(simM);
			}
			
			tx.commit();
			
			/*
			 * StoreIssueM issueM = (StoreIssueM) session.get(StoreIssueM.class, issueMid);
			 * if(issueM != null) { response.put("issueNo", issueM.getIssueNo()); }
			 */
			
			msg = "success";
			response.put("msg", msg);
			response.put("issueMId", issueMid);
			
			return response;
		} catch (Exception ex) {
			tx.rollback();
			ex.printStackTrace();
			msg = "Error while issuing Medicine";
			response.put("msg", msg);
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			tx = null;
		}
		return response;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getIssuNoAndIndentNo(Map<String, Object> jsondata) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Map<String, Object> map = new HashMap<>();
		try {
			long cityId = Long.parseLong(String.valueOf(jsondata.get("cityId")));
			long departmentId = Long.parseLong(String.valueOf(jsondata.get("departmentId")));
			Date fDate=HMSUtil.convertStringDateToUtilDateForDatabase(jsondata.get("fromDate").toString());
			Date tDate=HMSUtil.convertStringDateToUtilDateForDatabase(jsondata.get("toDate").toString());
			List<StoreIssueM> list = session.createCriteria(StoreIssueM.class)
					.createAlias("storeInternalIndentM", "indentM")
					.add(Restrictions.eq("indentM.cityId", cityId))
					//.add(Restrictions.eq("departmentId", departmentId))
					.add(Restrictions.between("issueDate", fDate, tDate))
					.add(Restrictions.eq("issueType", "I").ignoreCase())
					.addOrder(Order.desc("id")).list();

			map.put("list", list);
			return map;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@Override
	public Map<String, Object> getDrugExpiryList(Map<String, Object> jsondata) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Map<String, Object> map = new HashMap<>();
		try {
			String mmuId = String.valueOf(jsondata.get("mmuId"));
			String cityId = String.valueOf(jsondata.get("cityId"));
			String districtId = String.valueOf(jsondata.get("districtId"));
			long departmentId = Long.parseLong(String.valueOf(jsondata.get("departmentId")));
			String pvmsNo = String.valueOf(jsondata.get("pvmsNo"));
			String nomenclature = String.valueOf(jsondata.get("nomenclature"));
			int pageNo = Integer.parseInt(String.valueOf(jsondata.get("pageNo")));
			int pagingSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
			int count = 0;
			Date fromDate = null;
			if (jsondata.get("from_date") != null && !jsondata.get("from_date").equals("")) {
				fromDate = HMSUtil.convertStringTypeDateToDateType(String.valueOf(jsondata.get("from_date")));
			}
			Date toDate = null;
			if (jsondata.get("to_date") != null && !jsondata.get("to_date").equals("")) {
				toDate = HMSUtil.convertStringTypeDateToDateType(String.valueOf(jsondata.get("to_date")));
			}
			boolean flag = true;
			Criteria criteria = session.createCriteria(StoreItemBatchStock.class).createAlias("masDepartment", "md");
			
			if(mmuId != null && !mmuId.equals("") && !mmuId.equals("null") && !mmuId.equals("0")) {
				criteria = criteria.add(Restrictions.eq("mmuId", Long.parseLong(mmuId)));
			}
			
			if(cityId != null && !cityId.equals("") && !cityId.equals("null") && !cityId.equals("0")) {
				criteria = criteria.add(Restrictions.eq("cityId", Long.parseLong(cityId)));
			}
			
			if(districtId != null && !districtId.equals("") && !districtId.equals("null") && !districtId.equals("0")) {
				criteria = criteria.add(Restrictions.eq("districtId", Long.parseLong(districtId)));
			}
							
			criteria = criteria.add(Restrictions.eq("md.departmentId", departmentId))
					.add(Restrictions.gt("closingStock", new Long(0))).addOrder(Order.asc("expiryDate"));

			if (jsondata.get("from_date") != null && !jsondata.get("from_date").equals("")) {
				criteria = criteria.add(Restrictions.ge("expiryDate", fromDate));
				flag = false;
			}

			if (jsondata.get("to_date") != null && !jsondata.get("to_date").equals("")) {
				criteria = criteria.add(Restrictions.le("expiryDate", toDate));
				flag = false;
			}
			if ((nomenclature != null && !nomenclature.equals("") && !nomenclature.equals("null"))
					|| (pvmsNo != null && !pvmsNo.equals("") && !pvmsNo.equals("null"))) {
				criteria = criteria.createAlias("masStoreItem", "msi");
			}

			if (pvmsNo != null && !pvmsNo.equals("") && !pvmsNo.equals("null")) {

				criteria = criteria.add(Restrictions.eq("msi.pvmsNo", pvmsNo));
			}

			if (nomenclature != null && !nomenclature.equals("") && !nomenclature.equals("null")) {
				String nomen = "%" + nomenclature + "%";
				criteria = criteria.add(Restrictions.like("msi.nomenclature", nomen).ignoreCase());
			}

			if (flag) {
				criteria = criteria.add(Restrictions.lt("expiryDate", new Date()));
			}

			List<StoreItemBatchStock> list = criteria.list();
			count = list.size();

			criteria = criteria.setFirstResult(pagingSize * (pageNo - 1));
			criteria = criteria.setMaxResults(pagingSize);
			list = criteria.list();

			map.put("count", count);
			map.put("list", list);

			return map;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getROLDataList(Map<String, Object> jsondata) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Map<String, Object> map = new HashMap<>();
		try {
			long hospitalId = Long.parseLong(String.valueOf(jsondata.get("hospitalId")));
			long departmentId = Long.parseLong(String.valueOf(jsondata.get("departmentId")));
			int pageNo = Integer.parseInt(String.valueOf(jsondata.get("pageNo")));
			int pagingSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
			int count = 0;

			int expiryDays = Integer.parseInt(HMSUtil.getProperties("adt.properties", "EXPIRY_DAYS"));
			Date date = new Date();
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.add(Calendar.DATE, expiryDays);
			date = c.getTime();
			String sql = "";
			// long dispensaryId = Long.parseLong(HMSUtil.getProperties("adt.properties",
			// "DISPENSARY_DEPARTMENT_ID").trim());
			String code = HMSUtil.getProperties("adt.properties", "DISPENSARY_DEPARTMENT_CODE");
			long dispensaryId = CommonUtil.getDepartmentIdAgainstCode(session, code);

			// long storeId = Long.parseLong(HMSUtil.getProperties("adt.properties",
			// "STORE_DEPARTMENT_ID").trim());
			String code2 = HMSUtil.getProperties("adt.properties", "STORE_DEPARTMENT_CODE");
			long storeId = CommonUtil.getDepartmentIdAgainstCode(session, code2);
			if (departmentId == dispensaryId) {
				sql = "SELECT  pvms_no,mas.NOMENCLATURE,msu.store_unit_name,CLOSING_STOCK,rol_d from mas_store_item mas,\r\n"
						+ "(select  msi.item_id, sum(Bat.CLOSING_STOCK) as CLOSING_STOCK from    mas_store_item msi  \r\n"
						+ "left outer join STORE_ITEM_BATCH_STOCK Bat on msi.item_id=Bat.item_id where\r\n"
						+ "Bat.hospital_id=:hId and Bat.department_id=:dId  group by msi.item_id)aks,mas_store_unit msu  \r\n"
						+ "where aks.item_id=mas.item_id and mas.item_unit_id = msu.store_unit_id and mas.rol_d >= aks.CLOSING_STOCK";
			} else if (departmentId == storeId) {
				sql = "SELECT  pvms_no,mas.NOMENCLATURE,msu.store_unit_name,CLOSING_STOCK,rol_s from mas_store_item mas,\r\n"
						+ "(select  msi.item_id, sum(Bat.CLOSING_STOCK) as CLOSING_STOCK from    mas_store_item msi  \r\n"
						+ "left outer join STORE_ITEM_BATCH_STOCK Bat on msi.item_id=Bat.item_id where\r\n"
						+ "Bat.hospital_id=:hId and Bat.department_id=:dId  group by msi.item_id)aks,mas_store_unit msu  \r\n"
						+ "where aks.item_id=mas.item_id and mas.item_unit_id = msu.store_unit_id and mas.rol_s >= aks.CLOSING_STOCK";
			}

			Query query = session.createSQLQuery(sql).setParameter("hId", hospitalId).setParameter("dId", departmentId);

			List<Object[]> list = query.list();
			count = list.size();
			System.out.println("count is " + count);

			query = query.setFirstResult(pagingSize * (pageNo - 1));
			query = query.setMaxResults(pagingSize);
			list = query.list();

			map.put("count", count);
			map.put("list", list);

			return map;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@Override
	public Map<String, Object> getVisitListAndPrescriptionId(Map<String, Object> jsondata) {

		List<PatientPrescriptionHd> list = null;
		// long visitId = Long.parseLong((String) jsonData.get("visitId"));
		long patientId = Long.parseLong((String) jsondata.get("patientId"));
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria cr = session.createCriteria(PatientPrescriptionHd.class)
					.add(Restrictions.eq("patientId", patientId)).add(Restrictions.or(
							Restrictions.eq("status", "W").ignoreCase(), Restrictions.eq("status", "I").ignoreCase()));
			list = cr.list();
			map.put("list", list);
			getHibernateUtils.getHibernateUtlis().CloseConnection();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	@Override
	public String getPrescriptionId(long visitId) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		String prescriptionId = "";
		try {
			PatientPrescriptionHd patientPrescription = (PatientPrescriptionHd) session
					.createCriteria(PatientPrescriptionHd.class).add(Restrictions.eq("visitId", visitId))
					.uniqueResult();

			if (patientPrescription != null) {
				prescriptionId = String.valueOf(patientPrescription.getPrescriptionHdId());
			}
			return prescriptionId;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return prescriptionId;
	}

	@Override
	public Map<String, Object> getNisRegisterData(Map<String, Object> jsondata) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Map<String, Object> map = new HashMap<>();
		try {

			String hospitalId = String.valueOf(jsondata.get("hospitalId"));
			System.out.println("hospitalId32: " + hospitalId);

			int pageNo = Integer.parseInt(String.valueOf(jsondata.get("pageNo")));
			int pagingSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
			String itemTypeCodePVMS = HMSUtil.getProperties("adt.properties", "itemTypeCodePvms").trim();
			int count = 0;
			long itemTypeId = 0;
			MasItemType masItemType = (MasItemType) session.createCriteria(MasItemType.class)
					.add(Restrictions.eq("itemTypeCode", itemTypeCodePVMS)).uniqueResult();
			if (masItemType != null) {
				itemTypeId = masItemType.getItemTypeId();
				System.out.println("item type id is " + itemTypeId);
			}

			String sql = "SELECT MAS_STORE_ITEM.PVMS_NO, MAS_STORE_ITEM.NOMENCLATURE, sum((PATIENT_PRESCRIPTION_DT.TOTAL - NVL(PATIENT_PRESCRIPTION_DT.QTY_ISSUED,0))) AS QUANTITY, MAS_HOSPITAL.HOSPITAL_NAME"
					+ " from PATIENT_PRESCRIPTION_DT "
					+ " LEFT OUTER JOIN MAS_STORE_ITEM on PATIENT_PRESCRIPTION_DT.ITEM_ID=MAS_STORE_ITEM.ITEM_ID "
					+ " LEFT OUTER JOIN PATIENT_PRESCRIPTION_HD on PATIENT_PRESCRIPTION_DT.PRESCRIPTION_HD_ID=PATIENT_PRESCRIPTION_HD.PRESCRIPTION_HD_ID "
					+ " LEFT OUTER JOIN VISIT ON PATIENT_PRESCRIPTION_HD.VISIT_ID = VISIT.VISIT_ID "
					+ " LEFT OUTER JOIN MAS_DEPARTMENT ON MAS_DEPARTMENT.DEPARTMENT_ID = VISIT.DEPARTMENT_ID "
					+ " LEFT OUTER JOIN MAS_HOSPITAL on PATIENT_PRESCRIPTION_HD.HOSPITAL_ID=MAS_HOSPITAL.HOSPITAL_ID "
					+ " LEFT OUTER JOIN MAS_ITEM_TYPE on MAS_STORE_ITEM.ITEM_TYPE_ID =MAS_ITEM_TYPE.ITEM_TYPE_ID where "
					+ " VISIT.HOSPITAL_ID in (" + hospitalId
					+ ") AND PATIENT_PRESCRIPTION_HD.NIS_NO is not null AND PATIENT_PRESCRIPTION_DT.NIS_FLAG = 'Y' and MAS_ITEM_TYPE.ITEM_TYPE_ID = :itemTypeId";

			if ((jsondata.get("fromDate")) != null && !(jsondata.get("fromDate")).equals("")) {
				sql += " and PATIENT_PRESCRIPTION_HD.PRESCRIPTION_DATE > :fromDate ";
			}
			if ((jsondata.get("toDate")) != null && !(jsondata.get("toDate")).equals("")) {
				sql += "and PATIENT_PRESCRIPTION_HD.PRESCRIPTION_DATE < :toDate ";
			}

			sql += " group by MAS_STORE_ITEM.PVMS_NO, MAS_STORE_ITEM.NOMENCLATURE,MAS_HOSPITAL.HOSPITAL_NAME "
					+ "order by MAS_STORE_ITEM.NOMENCLATURE";

			System.out.println("query " + sql);
			Query sqlQuery = session.createSQLQuery(sql);

			// sqlQuery.setParameter("hospitalId", hospitalId);
			sqlQuery.setParameter("itemTypeId", itemTypeId);

			Date fromDate = null;
			if ((jsondata.get("fromDate")) != null && !(jsondata.get("fromDate")).equals("")) {
				fromDate = HMSUtil.convertStringTypeDateToDateType(String.valueOf(jsondata.get("fromDate")));
				sqlQuery.setParameter("fromDate", fromDate);
			}
			Date toDate = null;
			if ((jsondata.get("toDate")) != null && !(jsondata.get("toDate")).equals("")) {
				toDate = HMSUtil.convertStringTypeDateToDateType(String.valueOf(jsondata.get("toDate")));
				Calendar c = Calendar.getInstance();
				c.setTime(toDate);
				c.add(Calendar.DATE, 1);
				Date tDate = c.getTime();
				sqlQuery.setParameter("toDate", tDate);
			}

			List<Object[]> list = sqlQuery.list();

			count = list.size();
			System.out.println("list size is " + count);

			sqlQuery = sqlQuery.setFirstResult(pagingSize * (pageNo - 1));
			sqlQuery = sqlQuery.setMaxResults(pagingSize);
			list = sqlQuery.list();

			map.put("count", count);
			map.put("list", list);
			return map;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getDailyIssueSummaryData(Map<String, Object> jsondata) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Map<String, Object> map = new HashMap<>();
		try {
			long hospitalId = Long.parseLong(String.valueOf(jsondata.get("hospitalId")));
			long departmentId = Long.parseLong(String.valueOf(jsondata.get("departmentId")));
			String serviceNo = String.valueOf(jsondata.get("serviceNo"));
			String patientName = String.valueOf(jsondata.get("patientName"));
			String mobileNo = String.valueOf(jsondata.get("mobileNo"));
			int pageNo = Integer.parseInt(String.valueOf(jsondata.get("pageNo")));
			int pagingSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
			int count = 0;

			Criteria criteria = session.createCriteria(StoreIssueT.class).createAlias("storeIssueM", "sim")
					.add(Restrictions.eq("sim.hospitalId", hospitalId))
					.add(Restrictions.eq("sim.departmentId", departmentId))
					.add(Restrictions.isNull("storeInternalIndentT"));

			Date fromDate = null;
			if ((jsondata.get("fromDate")) != null && !(jsondata.get("fromDate")).equals("")) {
				fromDate = HMSUtil.convertStringTypeDateToDateType(String.valueOf(jsondata.get("fromDate")));
				criteria = criteria.add(Restrictions.ge("sim.issueDate", fromDate));
			}
			Date toDate = null;
			if ((jsondata.get("toDate")) != null && !(jsondata.get("toDate")).equals("")) {
				toDate = HMSUtil.convertStringTypeDateToDateType(String.valueOf(jsondata.get("toDate")));
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(toDate);
				calendar.add(Calendar.DAY_OF_YEAR, 1);
				toDate = calendar.getTime();
				criteria = criteria.add(Restrictions.le("sim.issueDate", toDate));
			}
			if ((!serviceNo.equals("") && !serviceNo.equals("null"))
					|| (!patientName.equals("") && !patientName.equals("null"))
					|| (!mobileNo.equals("") && !mobileNo.equals("null"))) {
				criteria = criteria.createAlias("sim.patientPrescriptionHd", "pHd");
				criteria = criteria.createAlias("pHd.patient", "patient");
			}
			if (serviceNo != null && !serviceNo.equals("") && !serviceNo.equals("null")) {
				criteria = criteria.add(Restrictions.eq("patient.serviceNo", serviceNo).ignoreCase());
			}
			if (patientName != null && !patientName.equals("") && !patientName.equals("null")) {
				String pName = "%" + patientName + "%";
				criteria = criteria.add(Restrictions.like("patient.patientName", pName).ignoreCase());
			}
			if (mobileNo != null && !mobileNo.equals("") && !mobileNo.equals("null")) {
				criteria = criteria.add(Restrictions.eq("patient.mobileNumber", mobileNo));
			}

			List<StoreIssueT> list = criteria.list();
			count = list.size();
			System.out.println("count " + count);
			criteria = criteria.setFirstResult(pagingSize * (pageNo - 1));
			criteria = criteria.setMaxResults(pagingSize);
			list = criteria.list();

			map.put("count", count);
			map.put("list", list);

			return map;

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@Override
	public Map<String, Object> getDepartmentIdAgainstCode(Map<String, Object> payload) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Map<String, Object> map = new HashMap<>();
		try {
			String code = String.valueOf(payload.get("code"));
			MasDepartment masDepartment = (MasDepartment) session.createCriteria(MasDepartment.class)
					.add(Restrictions.eq("departmentCode", code).ignoreCase()).uniqueResult();
			String departmentId = "";
			if (masDepartment != null) {
				departmentId = String.valueOf(masDepartment.getDepartmentId());
			}
			map.put("departmentId", departmentId);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@Override
	public Long getItemTypeId(Map<String, Object> payload) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		long id = 0;
		String itemTypeCodePVMS = String.valueOf(HMSUtil.getProperties("adt.properties", "itemTypeCodePvms").trim());
		try {
			MasItemType masItemType = (MasItemType) session.createCriteria(MasItemType.class)
					.add(Restrictions.eq("itemTypeCode", itemTypeCodePVMS)).uniqueResult();

			if (masItemType != null) {
				id = masItemType.getItemTypeId();
			}
			return id;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return null;
	}

	@Override
	public long submitDispenceryIndent(HashMap<String, Object> jsondata) {
		// TODO Auto-generated method stub
		JSONObject json = new JSONObject(jsondata);
		Transaction tx = null;
		StoreInternalIndentM storeInternalIndentM = new StoreInternalIndentM();

		String btnvalue = "";
		String btnvalue1 = "";
		long indentMId = 0;
		if (jsondata != null) {
			if (!jsondata.get("indentDate").toString().isEmpty()) {
				String indentDate = jsondata.get("indentDate").toString();
				String indentDate1 = dispenceryService.getReplaceString(indentDate);
				Date indentDate2 = HMSUtil.convertStringDateToUtilDate(indentDate1, "dd/MM/yyyy");
				storeInternalIndentM.setDemandDate(new Timestamp(indentDate2.getTime()));
			}
			if (json.has("save")) {
				btnvalue = jsondata.get("save").toString();
				btnvalue1 = dispenceryService.getReplaceString(btnvalue);
			}
			if (json.has("submit")) {
				btnvalue = jsondata.get("submit").toString();
				btnvalue1 = dispenceryService.getReplaceString(btnvalue);
			}
			// String departmentId = jsondata.get("departmentId").toString();
			// String departmentId1 = dispenceryService.getReplaceString(departmentId);
			// long store = Long.parseLong(departmentId1);

			String nomenclature = jsondata.get("nomenclature").toString();
			String nomenclature1 = dispenceryService.getReplaceString(nomenclature);
			String[] nomenclatureValue = nomenclature1.split(",");

			String requiredQty = jsondata.get("requiredQty").toString();
			String requiredQty1 = dispenceryService.getReplaceString(requiredQty);
			String[] requiredQtyValue = requiredQty1.split(",");
			int itemLength = requiredQtyValue.length;

			String accountingUnit1 = jsondata.get("accountingUnit1").toString();
			String accountingUnit = dispenceryService.getReplaceString(accountingUnit1);
			String[] accountingUnitValue = accountingUnit.split(",");

			String availableStock = jsondata.get("availableStock").toString();
			String availableStock1 = dispenceryService.getReplaceString(availableStock);
			String[] availableStockValue = availableStock1.split(",");

			String stockInDispencery = jsondata.get("stockInDispencery").toString();
			String stockInDispencery1 = dispenceryService.getReplaceString(stockInDispencery);
			String[] stockInDispenceryValue = stockInDispencery1.split(",");

			// String stockInStore = jsondata.get("stockInStore").toString();
			// String stockInStore1 = dispenceryService.getReplaceString(stockInStore);
			// String[] stockInStoreValue = stockInStore1.split(",");

			String remarks1 = jsondata.get("remarks1").toString();
			String remarks = dispenceryService.getReplaceString(remarks1);
			String[] remarks1Value = remarks.split(",");

			String itemIdNom = jsondata.get("itemIdNom").toString();
			String itemIdNom1 = dispenceryService.getReplaceString(itemIdNom);
			String[] itemIdNomValue = itemIdNom1.split(",");

			String pvmsNo1 = jsondata.get("pvmsNo1").toString();
			String pvmsNo = dispenceryService.getReplaceString(pvmsNo1);
			String[] pvmsNo1Value = pvmsNo.split(",");

			Users users = null;
			String dept1 = jsondata.get("loginDepartmentId").toString();// come from session
			long dept = Long.parseLong(dept1);
			System.out.println("department id is ***************************************** " + dept);

			// MasHospital masHospital = null;
			MasDepartment masDepartment = null;
			/*
			 * MasDepartment masStore = null;
			 * 
			 * masDepartment = getMasDepartment(dept); masStore = getMasDepartment(store);
			 */
			// save data in table
			if (jsondata.get("userId") != null) {
				String userId = jsondata.get("userId").toString();
				users = getUser(Long.parseLong(userId));
			}
			/*
			 * if (jsondata.get("hospitalId") != null) { String hospitalId =
			 * jsondata.get("hospitalId").toString(); masHospital =
			 * gettoMasHospital(Long.parseLong(hospitalId)); }
			 */
			
			//Long cityId = Long.parseLong(String.valueOf(jsondata.get("cityId")));
			Long mmuId = Long.parseLong(String.valueOf(jsondata.get("mmuId")));

			Date date = new Date();
			Session session1 = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session1.beginTransaction();
			try {
				storeInternalIndentM.setLastChgDate(new Timestamp(date.getTime()));

				// storeInternalIndentM.setMasHospital(masHospital);
				// dept
				/*
				 * MasDepartment masDepatment = new MasDepartment();
				 * masDepatment.setDepartmentId(dept);
				 */
				storeInternalIndentM.setDepartmentId(dept);
				storeInternalIndentM.setMmuId(mmuId);
				storeInternalIndentM.setDepartment1(masDepartment);// need to change
				if (btnvalue1.equalsIgnoreCase("save"))
					storeInternalIndentM.setStatus("N");// n=save,y=submit,a=approve,r=reject
				if (btnvalue1.equalsIgnoreCase("submit"))
					storeInternalIndentM.setStatus("Y");
				// storeInternalIndentM.setDepartment2(masStore);
				storeInternalIndentM.setUser1(users);
				storeInternalIndentM.setUser2(users);
				storeInternalIndentM.setUsers4(users);
				//storeInternalIndentM.setCityId(cityId);
				
				/////////////////////////////Modify by Avinash//////////////////////
				if(jsondata.get("cittyIdVal")!=null && jsondata.get("cittyIdVal")!="0") {
					//String cityIdVallll=jsondata.get("cittyIdVal").toString();
				String cittyIdVal = dispenceryService.getReplaceString(jsondata.get("cittyIdVal").toString());
				if(cittyIdVal!=null && !cittyIdVal.equalsIgnoreCase("0")) {
				Long cityIdUpdate = Long.parseLong(cittyIdVal.trim());
					
				storeInternalIndentM.setCityId(cityIdUpdate);
				}
				}
				///////////////////////////////////////////////////////
				indentMId = (long) session1.save(storeInternalIndentM);

				for (int i = 0; i < itemLength; i++) {
					StoreInternalIndentT storeInternalIndentT = new StoreInternalIndentT();
					MasStoreItem masStoreItem = new MasStoreItem();
					if (requiredQtyValue[i] != null && !requiredQtyValue[i].isEmpty())
						storeInternalIndentT.setQtyRequest(Long.parseLong(requiredQtyValue[i].trim()));

					if (remarks1Value[i] != null && !remarks1Value[i].isEmpty())
						storeInternalIndentT.setReasonForDemand(remarks1Value[i].trim());

					if (itemIdNomValue[i] != null && !itemIdNomValue[i].isEmpty()) {
						// masStoreItem =
						// dispensaryDao.getMasStoreItem(Long.parseLong(itemIdNomValue[i].trim()));
						masStoreItem.setItemId(Long.parseLong(itemIdNomValue[i].trim()));
						storeInternalIndentT.setMasStoreItem(masStoreItem);
					}
					if (availableStockValue[i] != null && !availableStockValue[i].trim().isEmpty()) {
						storeInternalIndentT.setAvailableStock(Long.parseLong(availableStockValue[i].trim()));
					}
					if (stockInDispenceryValue[i] != null && !stockInDispenceryValue[i].trim().isEmpty()) {
						storeInternalIndentT.setDispStock(Long.parseLong(stockInDispenceryValue[i].trim()));
					}
					/*
					 * if (stockInStoreValue[i] != null && !stockInStoreValue[i].trim().isEmpty()) {
					 * storeInternalIndentT.setStoresStock(Long.parseLong(stockInStoreValue[i].trim(
					 * ))); }
					 */
					storeInternalIndentT.setDepartment1(masDepartment);// need to chk
					// storeInternalIndentT.setd
					storeInternalIndentT.setStoreInternalIndentM1(storeInternalIndentM);// need to chk
					long indentId = (long) session1.save(storeInternalIndentT);
				}

				tx.commit();
				session1.flush();
				session1.clear();
			} catch (Exception e) {
				if (tx != null) {
					try {
						tx.rollback();
						indentMId = -1;
					} catch (Exception re) {
						indentMId = -1;
						re.printStackTrace();
					}
				}
				indentMId = -1;
				e.printStackTrace();
			}

			finally {

				getHibernateUtils.getHibernateUtlis().CloseConnection();

			}

			return indentMId;
		}
		return -1;
	}

	@Override
	public Map<String, Object> getRegisteredPatientList(Map<String, Object> payload) {
		Map<String, Object> map = new HashMap<>();
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		try {
			int pageNo = Integer.parseInt(String.valueOf(payload.get("pageNo")));
			int pagingSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
			int count = 0;
			String mobileNo = String.valueOf(payload.get("mobileNo"));
			String patientName = String.valueOf(payload.get("patientName"));

			Criteria criteria = session.createCriteria(Patient.class).createAlias("masAdministrativeSex","masAdministrativeSex").add(Restrictions.isNull("patientType"))
					.addOrder(Order.asc("patientName"));

			if (mobileNo != null && !mobileNo.equals("") && !mobileNo.equals("null")) {

				criteria = criteria.add(Restrictions.eq("mobileNumber", mobileNo.trim()));

			}
			if (patientName != null && !patientName.equals("") && !patientName.equals("null")) {

				String pName = "%" + patientName + "%";
				criteria = criteria.add(Restrictions.like("patientName", pName).ignoreCase());

			}
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("patientId").as("patientId"));
			projectionList.add(Projections.property("patientName").as("patientName"));
			projectionList.add(Projections.property("dateOfBirth").as("dateOfBirth"));
			
			projectionList.add(Projections.property("mobileNumber").as("mobileNumber"));
			projectionList.add(Projections.property("masAdministrativeSex.administrativeSexName").as("administrativeSexName"));
			criteria.setProjection(projectionList);
			List<Object[]> list = criteria.list();
			count = list.size();

			criteria = criteria.setFirstResult(pagingSize * (pageNo - 1));
			criteria = criteria.setMaxResults(pagingSize);
			list = criteria.list();

			map.put("count", count);
			map.put("list", list);
			return map;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@Override
	public Map<String, Object> getRegisteredPatientDetail(Map<String, Object> payload) {
		Map<String, Object> map = new HashMap<>();
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		try {

			long patientId = Long.parseLong(String.valueOf(payload.get("id")));

			Patient patient = (Patient) session.createCriteria(Patient.class)
					.add(Restrictions.eq("patientId", patientId)).uniqueResult();

			map.put("patient", patient);
			return map;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@Override
	public Map<String, Object> updatePatientInformation(Map<String, Object> requestData) {
		Map<String, Object> response = new HashMap<>();
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Transaction tx = session.beginTransaction();
		try {
			Long patientId = Long.parseLong(String.valueOf(requestData.get("patientId")));

			Patient patient = (Patient) session.get(Patient.class, patientId);
			if (requestData.get("identificationTypeId") != null
					&& !String.valueOf(requestData.get("identificationTypeId")).equals("")
					&& !String.valueOf(requestData.get("identificationTypeId")).equals("null")) {
				Long identificationTypeId = Long.parseLong(String.valueOf(requestData.get("identificationTypeId")));
				patient.setIdentificationTypeId(identificationTypeId);
			}
			if (requestData.get("districtId") != null && !String.valueOf(requestData.get("districtId")).equals("")
					&& !String.valueOf(requestData.get("districtId")).equals("null")) {
				Long districtId = Long.parseLong(String.valueOf(requestData.get("districtId")));
				patient.setDistrictId(districtId);

			}
			if (requestData.get("cityId") != null && !String.valueOf(requestData.get("cityId")).equals("")
					&& !String.valueOf(requestData.get("cityId")).equals("null")) {
				Long cityId = Long.parseLong(String.valueOf(requestData.get("cityId")));
				patient.setCityId(cityId);
			}

			if (requestData.get("typeOfPatient") != null && !String.valueOf(requestData.get("typeOfPatient")).equals("")
					&& !String.valueOf(requestData.get("typeOfPatient")).equals("null")) {
				String patientType = String.valueOf(requestData.get("typeOfPatient"));
				patient.setPatientType(patientType);
			}

			if (requestData.get("isFormSubmitted") != null
					&& !String.valueOf(requestData.get("isFormSubmitted")).equals("")
					&& !String.valueOf(requestData.get("isFormSubmitted")).equals("null")) {
				String isFormSubmitted = String.valueOf(requestData.get("isFormSubmitted"));
				patient.setFormSubmitted(isFormSubmitted);
			}

			if (requestData.get("isLabourRegistered") != null
					&& !String.valueOf(requestData.get("isLabourRegistered")).equals("")
					&& !String.valueOf(requestData.get("isLabourRegistered")).equals("null")) {
				String isLabourRegistered = String.valueOf(requestData.get("isLabourRegistered"));
				patient.setLaborRegistered(isLabourRegistered);
			}

			if (requestData.get("relationId") != null && !String.valueOf(requestData.get("relationId")).equals("")
					&& !String.valueOf(requestData.get("relationId")).equals("null")) {
				Long relationId = Long.parseLong(String.valueOf(requestData.get("relationId")));
				patient.setRelationId(relationId);
			}

			if (requestData.get("occupation") != null && !String.valueOf(requestData.get("occupation")).equals("")
					&& !String.valueOf(requestData.get("occupation")).equals("null")) {
				String occupation = String.valueOf(requestData.get("occupation"));
				patient.setOccuption(occupation);
			}

			if (requestData.get("registrationNo") != null
					&& !String.valueOf(requestData.get("registrationNo")).equals("")
					&& !String.valueOf(requestData.get("registrationNo")).equals("null")) {
				String registrationNo = String.valueOf(requestData.get("registrationNo"));
				patient.setRegNo(registrationNo);
			}

			if (requestData.get("identificationNo") != null
					&& !String.valueOf(requestData.get("identificationNo")).equals("")
					&& !String.valueOf(requestData.get("identificationNo")).equals("null")) {
				String identificationNo = String.valueOf(requestData.get("identificationNo"));
				patient.setIdentificationNo(identificationNo);
			}

			if (requestData.get("address") != null && !String.valueOf(requestData.get("address")).equals("")
					&& !String.valueOf(requestData.get("address")).equals("null")) {
				String address = String.valueOf(requestData.get("address"));
				patient.setAddress(address);
			}

			if (requestData.get("pincode") != null && !String.valueOf(requestData.get("pincode")).equals("")
					&& !String.valueOf(requestData.get("pincode")).equals("null")) {
				Long pincode = Long.parseLong(String.valueOf(requestData.get("pincode")));
				patient.setPincode(pincode.intValue());
			}

			if (requestData.get("typeOfLabour") != null
					&& !String.valueOf(requestData.get("typeOfLabour")).equals("")) {
				Long labourId = Long.parseLong(String.valueOf(requestData.get("typeOfLabour")));
				patient.setLabourId(labourId);
			}

			if (requestData.get("bloodGroupId") != null && !String.valueOf(requestData.get("bloodGroupId")).equals("")
					&& !String.valueOf(requestData.get("bloodGroupId")).equals("null")) {
				Long bloodGroupId = Long.parseLong(String.valueOf(requestData.get("bloodGroupId")));
				patient.setBloodGroupId(bloodGroupId);
			}

			if (requestData.get("wardId") != null && !String.valueOf(requestData.get("wardId")).equals("")
					&& !String.valueOf(requestData.get("wardId")).equals("null")) {
				Long wardId = Long.parseLong(String.valueOf(requestData.get("wardId")));
				patient.setWardId(wardId);
			}

			if (requestData.get("castId") != null && !String.valueOf(requestData.get("castId")).equals("")
					&& !String.valueOf(requestData.get("castId")).equals("null")) {
				Long castId = Long.parseLong(String.valueOf(requestData.get("castId")));
				patient.setCastId(castId);
			}

			session.save(patient);
			response.put("status", true);
			response.put("msg", "Patient Information is updated.");

			tx.commit();
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		response.put("status", false);
		response.put("msg", "Something went wrong");
		return response;
	}

	@Override
	public Map<String, Object> getPendingListForAuditor(long pageNo, HashMap<String, String> requestData) {
		Map<String, Object> map = new HashMap<String, Object>();
		int pageSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
		int pageNo1 = 0;
		List totalMatches = new ArrayList();
		if (requestData.get("PN") != null)
			pageNo1 = Integer.parseInt(requestData.get("PN").toString());
		List<StoreInternalIndentM> storeInternalIndentMList = new ArrayList<StoreInternalIndentM>();
		JSONObject json = new JSONObject(requestData);

		long departmentId = Long.parseLong(json.getString("departmentId").toString());

		Criteria cr = null;
		Date indentStratDate = null;
		Date indentEndDate = null;
		Date finalIndentEndDate = null;
		String startDate = "";
		String endDate = "";
		if (json.has("fromDate")) {
			startDate = json.getString("fromDate");
			endDate = json.getString("toDate");
			if ((!startDate.isEmpty() && startDate != null) && (!endDate.isEmpty() && endDate != null)) {
				indentStratDate = HMSUtil.convertStringDateToUtilDate(startDate, "dd/MM/yyyy");
				indentEndDate = HMSUtil.convertStringDateToUtilDate(endDate, "dd/MM/yyyy");

				Calendar cal = Calendar.getInstance();
				cal.setTime(indentEndDate);
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 59);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				// Date from = cal.getTime();
				cal.set(Calendar.HOUR_OF_DAY, 23);
				finalIndentEndDate = cal.getTime();

			}
		}

		try {
			long mmuId = Long.parseLong(json.getString("mmuId").toString());
			
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			cr = session.createCriteria(StoreInternalIndentM.class);
			cr = cr.add(Restrictions.eq("mmuId", mmuId));

			cr = cr.add(Restrictions.eq("departmentId", departmentId));
			cr = cr.add(Restrictions.eq("status", "A").ignoreCase());
			cr.addOrder(Order.desc("demandDate"));

			if ((!startDate.isEmpty() && startDate != null) && (!endDate.isEmpty() && endDate != null)) {
				cr = cr.add(Restrictions.ge("demandDate", indentStratDate))
						.add(Restrictions.lt("demandDate", finalIndentEndDate));
			}

			if (!cr.list().isEmpty() && cr.list().size() > 0) {

				totalMatches = cr.list();
				cr.setFirstResult((pageSize) * (pageNo1 - 1));
				cr.setMaxResults(pageSize);
				storeInternalIndentMList = cr.list();
			}
		} catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.getMessage();
			e.printStackTrace();
		} finally {

			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		map.put("storeInternalIndentMList", storeInternalIndentMList);
		map.put("totalMatches", totalMatches.size());
		return map;
	}

	@Override
	public Map<String, Object> getPendingListForCO(long pageNo, HashMap<String, String> requestData) {
		Map<String, Object> map = new HashMap<String, Object>();
		int pageSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
		int pageNo1 = 0;
		List totalMatches = new ArrayList();
		if (requestData.get("PN") != null)
			pageNo1 = Integer.parseInt(requestData.get("PN").toString());
		List<StoreInternalIndentM> storeInternalIndentMList = new ArrayList<StoreInternalIndentM>();
		JSONObject json = new JSONObject(requestData);
		long cityId = Long.parseLong(json.getString("cityId").toString());
		long departmentId = Long.parseLong(json.getString("departmentId").toString());

		Criteria cr = null;
		Date indentStratDate = null;
		Date indentEndDate = null;
		Date finalIndentEndDate = null;
		String startDate = "";
		String endDate = "";
		if (json.has("fromDate")) {
			startDate = json.getString("fromDate");
			endDate = json.getString("toDate");
			if ((!startDate.isEmpty() && startDate != null) && (!endDate.isEmpty() && endDate != null)) {
				indentStratDate = HMSUtil.convertStringDateToUtilDate(startDate, "dd/MM/yyyy");
				indentEndDate = HMSUtil.convertStringDateToUtilDate(endDate, "dd/MM/yyyy");

				Calendar cal = Calendar.getInstance();
				cal.setTime(indentEndDate);
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 59);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				// Date from = cal.getTime();
				cal.set(Calendar.HOUR_OF_DAY, 23);
				finalIndentEndDate = cal.getTime();

			}
		}

		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			cr = session.createCriteria(StoreInternalIndentM.class).add(Restrictions.eq("cityId", cityId));
			if (json.getString("mmuId") != null && !json.getString("mmuId").equals("null")
					&& !json.getString("mmuId").equals("null")) {
				long mmuId = Long.parseLong(json.getString("mmuId").toString());
				cr = cr.add(Restrictions.eq("mmuId", mmuId));
			}
			// cr = cr.add(Restrictions.eq("mmuId", mmuId));
			cr = cr.add(Restrictions.eq("departmentId", departmentId));
			cr = cr.add(Restrictions.eq("status", "U").ignoreCase());
			cr.addOrder(Order.desc("demandDate"));

			if ((!startDate.isEmpty() && startDate != null) && (!endDate.isEmpty() && endDate != null)) {
				cr = cr.add(Restrictions.ge("demandDate", indentStratDate))
						.add(Restrictions.lt("demandDate", finalIndentEndDate));
			}

			if (!cr.list().isEmpty() && cr.list().size() > 0) {

				totalMatches = cr.list();
				cr.setFirstResult((pageSize) * (pageNo1 - 1));
				cr.setMaxResults(pageSize);
				storeInternalIndentMList = cr.list();
			}
		} catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.getMessage();
			e.printStackTrace();
		} finally {

			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		map.put("storeInternalIndentMList", storeInternalIndentMList);
		map.put("totalMatches", totalMatches.size());
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> displayItemListCO(Map<String, Object> requestData) {
		Map<String, Object> map = new HashMap<>();
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		try {
			Long cityId = Long.parseLong(String.valueOf(requestData.get("cityId")));

			Criteria criteria = session.createCriteria(StoreInternalIndentT.class)
					.createAlias("storeInternalIndentM1", "m1").createAlias("masStoreItem", "msi")
					.createAlias("msi.masStoreUnit1", "unit").add(Restrictions.eq("m1.status", "C"))
					.add(Restrictions.eq("m1.cityId", cityId)).add(Restrictions.isNull("forwardedFlag"));

			ProjectionList projectionList = Projections.projectionList();
//			/masStoreItem
			projectionList.add(Projections.property("msi.itemId"));
			projectionList.add(Projections.property("msi.nomenclature"));
			projectionList.add(Projections.property("unit.storeUnitName"));
			// projectionList.add(Projections.property("availableStock"));
			projectionList.add(Projections.sum("qtyRequest"));
			projectionList.add(Projections.sum("availableStock"));
			projectionList.add(Projections.sum("approvedQty"));
			projectionList.add(Projections.property("msi.pvmsNo"));
			projectionList.add(Projections.groupProperty("msi.itemId"));
			projectionList.add(Projections.groupProperty("msi.nomenclature"));
			projectionList.add(Projections.groupProperty("unit.storeUnitName"));
			projectionList.add(Projections.groupProperty("msi.pvmsNo"));
			criteria.setProjection(projectionList);

			List list = criteria.list();// 9

			map.put("list", list);
			return map;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> forwardToDisctrict(Map<String, Object> requestData) {
		Map<String, Object> map = new HashMap<>();
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Transaction tx = session.beginTransaction();
		try {

			Calendar calendar = Calendar.getInstance();
			java.sql.Timestamp ourJavaTimestampObject = new java.sql.Timestamp(calendar.getTime().getTime());

			Long userId = Long.parseLong(String.valueOf(requestData.get("userId")));
			List<Map<String, Object>> list = (List<Map<String, Object>>) requestData.get("list");
			long cityId = Long.parseLong(String.valueOf(list.get(0).get("cityId")));
			
			MasCity masCity = (MasCity) session.get(MasCity.class, cityId);
			Long districtId = null;
			if (masCity != null) {
				districtId = masCity.getMasDistrict().getDistrictId();
			}

			StoreCoInternalIndentM storeCoM = new StoreCoInternalIndentM();
			storeCoM.setCityId(cityId);
			storeCoM.setDemandDate(ourJavaTimestampObject);
			storeCoM.setUserId(userId);
			storeCoM.setLastChgDate(ourJavaTimestampObject);
			storeCoM.setDistrictId(districtId);
			storeCoM.setUserId(userId);
			storeCoM.setLastChgDate(new Timestamp(new Date().getTime()));
			storeCoM.setCoId(userId);
			storeCoM.setCoDate(new Date());
			Long storeCoMId = Long.parseLong(session.save(storeCoM).toString());
			
			
			for (Map<String, Object> item : list) {
				String availableQty = String.valueOf(item.get("availableQty"));
				String requiredQty = String.valueOf(item.get("requiredQty"));
				Long itemId = Long.parseLong(String.valueOf(item.get("itemId")));
				//Long cityId = Long.parseLong(String.valueOf(item.get("cityId")));

				// set forwarded flag to Y
				List<StoreInternalIndentT> storeTList = session.createCriteria(StoreInternalIndentT.class)
						.createAlias("masStoreItem", "msi").add(Restrictions.eq("msi.itemId", itemId))
						.createAlias("storeInternalIndentM1", "storeM").add(Restrictions.eq("storeM.status", "C"))
						.add(Restrictions.eq("storeM.cityId", cityId)).list();

				StoreCoInternalIndentT storeCoT = new StoreCoInternalIndentT();
				storeCoT.setAvailableStock(Long.parseLong(availableQty));
				storeCoT.setItemId(itemId);
				storeCoT.setQtyRequest(Long.parseLong(requiredQty));
				storeCoT.setStoreCoMId(storeCoMId);

				Long storeCoDtId = Long.parseLong(String.valueOf(session.save(storeCoT)));

				for (StoreInternalIndentT storeT : storeTList) {
					storeT.setForwardedFlag("Y");
					storeT.setStoreCoDtId(storeCoDtId);
					session.save(storeT);
				}
			}

			tx.commit();
			map.put("status", true);
			map.put("msg", "Item forwarded to District");
			return map;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		map.put("status", false);
		map.put("msg", "Something went wrong");
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getPendingListForDO(long pageNo, HashMap<String, String> requestData) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> responseList = new ArrayList<Map<String, Object>>();
		int pageSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
		long districtId = Long.parseLong(String.valueOf(requestData.get("districtId")));
		int pageNo1 = 0;
		int count = 0;
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		try {

			Criteria cr = session.createCriteria(StoreCoInternalIndentM.class)
					.add(Restrictions.eq("districtId", districtId))
					.add(Restrictions.isNull("status"))
					.addOrder(Order.desc("demandDate"));

			List<StoreCoInternalIndentM> list = cr.list();
			count = list.size();

			cr.setFirstResult((pageSize) * (pageNo1 - 1));
			cr.setMaxResults(pageSize);
			list = cr.list();

			map.put("list", list);
			map.put("count", count);
			return map;
		} catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		map.put("list", responseList);
		map.put("count", count);
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> indentValidationDO(HashMap<String, String> payload) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> responseList = new ArrayList<Map<String, Object>>();
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		try {
			Long storeCoMId = Long.parseLong(String.valueOf(payload.get("indentCoMId")));

			List<StoreCoInternalIndentT> indentCoTList = session.createCriteria(StoreCoInternalIndentT.class)
					.add(Restrictions.eq("storeCoMId", storeCoMId)).list();

			map.put("list", indentCoTList);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@Override
	public Map<String, Object> updateIndentDispenceryByDO(HashMap<String, Object> jsondata) {
		Map<String, Object> ResponseMap = new HashMap<String, Object>();
		List<Map<String, Object>> responseList = new ArrayList<Map<String, Object>>();
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Transaction tx = session.beginTransaction();
		try {

			Long storeCoMId = Long.parseLong(String.valueOf(jsondata.get("storeCoMId")));
			Long userId = Long.parseLong(String.valueOf(jsondata.get("userId")));
			String remarks = String.valueOf(jsondata.get("remarks"));
			List<Map<String, Object>> list = (List<Map<String, Object>>) jsondata.get("dtList");

			StoreCoInternalIndentM storeCoInternalIndentM = (StoreCoInternalIndentM) session
					.get(StoreCoInternalIndentM.class, storeCoMId);
			storeCoInternalIndentM.setStatus("A");
			storeCoInternalIndentM.setDoId(userId);
			storeCoInternalIndentM.setDoDate(new Date());
			storeCoInternalIndentM.setUserId(userId);
			storeCoInternalIndentM.setLastChgDate(new Timestamp(new Date().getTime()));
			storeCoInternalIndentM.setRemarks(remarks);

			for (Map<String, Object> map : list) {
				Long dtId = Long.parseLong(String.valueOf(map.get("dtId")));
				Long approvedQty = Long.parseLong(String.valueOf(map.get("approvedQty")));

				StoreCoInternalIndentT storeCoInternalIndentT = (StoreCoInternalIndentT) session
						.get(StoreCoInternalIndentT.class, dtId);
				storeCoInternalIndentT.setApprovedQty(approvedQty);
				session.save(storeCoInternalIndentT);
			}

			ResponseMap.put("status", true);
			ResponseMap.put("msg", "Indent approved");
			tx.commit();
			return ResponseMap;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		ResponseMap.put("status", false);
		ResponseMap.put("msg", "Something went wrong");
		return ResponseMap;
	}

	public static String getReplaceString(String replaceValue) {
		return replaceValue.replaceAll("[\\[\\]]", "");
	}

	@Override
	public Map<String, Object> displayItemListDO(Map<String, Object> requestData) {
		Map<String, Object> map = new HashMap<>();
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		try {

			Long districtId = Long.parseLong(String.valueOf(requestData.get("districtId")));

			Criteria criteria = session.createCriteria(StoreCoInternalIndentT.class)
					.createAlias("storeCoInternalIndentM1", "m1").add(Restrictions.eq("m1.districtId", districtId))
					.createAlias("masStoreItem", "msi").createAlias("msi.masStoreUnit1", "unit")
					.add(Restrictions.eq("m1.status", "A")).add(Restrictions.isNull("forwardedFlag"));

			ProjectionList projectionList = Projections.projectionList();
//			/masStoreItem
			projectionList.add(Projections.property("msi.itemId"));
			projectionList.add(Projections.property("msi.nomenclature"));
			projectionList.add(Projections.property("unit.storeUnitName"));
			// projectionList.add(Projections.property("availableStock"));
			projectionList.add(Projections.sum("qtyRequest"));
			projectionList.add(Projections.sum("availableStock"));
			projectionList.add(Projections.sum("approvedQty"));
			projectionList.add(Projections.property("msi.pvmsNo"));
			projectionList.add(Projections.groupProperty("msi.itemId"));
			projectionList.add(Projections.groupProperty("msi.nomenclature"));
			projectionList.add(Projections.groupProperty("unit.storeUnitName"));
			projectionList.add(Projections.groupProperty("msi.pvmsNo"));
			criteria.setProjection(projectionList);

			List list = criteria.list();

			map.put("list", list);
			return map;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> mmuWiseIndentDetail(Map<String, Object> requestData) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> responseList = new ArrayList<Map<String, Object>>();
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		try {
			Long itemId = Long.parseLong(String.valueOf(requestData.get("itemId")));

			List<StoreInternalIndentT> indentCoTList = session.createCriteria(StoreInternalIndentT.class)
					.createAlias("storeInternalIndentM1", "storeCoM").add(Restrictions.eq("storeCoM.status", "C"))
					.add(Restrictions.isNull("forwardedFlag")).createAlias("masStoreItem", "msi")
					.add(Restrictions.eq("msi.itemId", itemId)).list();

			map.put("list", indentCoTList);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> submitDoItemsAndGeneratePo(Map<String, Object> requestData) {
		Map<String, Object> map = new HashMap<>();
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Transaction tx = session.beginTransaction();
		try {

			Calendar calendar = Calendar.getInstance();
			java.sql.Timestamp ourJavaTimestampObject = new java.sql.Timestamp(calendar.getTime().getTime());

			Long userId = Long.parseLong(String.valueOf(requestData.get("userId")));
			List<Map<String, Object>> list = (List<Map<String, Object>>) requestData.get("list");
			// Date poDate =
			// HMSUtil.convertStringTypeDateToDateType(String.valueOf(requestData.get("poDate")));
			Long vendorTypeId = Long.parseLong(String.valueOf(requestData.get("vendorTypeId")));
			Long vendorNameId = Long.parseLong(String.valueOf(requestData.get("vendorNameId")));
			Long districtId = Long.parseLong(String.valueOf(requestData.get("districtId")));

			String rvNotes = String.valueOf(requestData.get("rvNotes"));
			String indentFlag = String.valueOf(requestData.get("indentFlag"));

			StoreDoInternalIndentM storeDoM = new StoreDoInternalIndentM();
			storeDoM.setUserId(userId);
			storeDoM.setLastChgDate(ourJavaTimestampObject);
			storeDoM.setDhmoId(userId);
			storeDoM.setPoDate(new Timestamp(new Date().getTime()));
			storeDoM.setVendorNewId(vendorNameId);
			storeDoM.setVendorTypeId(vendorTypeId);
			storeDoM.setDistrictId(districtId);
			if (requestData.get("rvNotes") != null && !rvNotes.equals("")
					&& !rvNotes.equals("null")) {
				storeDoM.setRvNotes(rvNotes);
			}
			
			if(indentFlag.equalsIgnoreCase("direct")) {
				storeDoM.setTypeOfPo("Direct PO");
			}else {
				storeDoM.setTypeOfPo("Through Indent");
			}
			
			Long storeDoMId = Long.parseLong(String.valueOf(session.save(storeDoM)));

			for (Map<String, Object> item : list) {
				String availableQty = String.valueOf(item.get("availableQty"));
				String requiredQty = String.valueOf(item.get("requiredQty"));
				Long itemId = Long.parseLong(String.valueOf(item.get("itemId")));
				Long approvedQty = Long.parseLong(String.valueOf(item.get("approvedQty")));
				Long poQty = Long.parseLong(String.valueOf(item.get("poQty")));
				//Long unitRate = Long.parseLong(String.valueOf(item.get("unitRate")));
				// set forwarded flag to Y
				List<StoreCoInternalIndentT> storeTList = session.createCriteria(StoreCoInternalIndentT.class)
						.createAlias("masStoreItem", "msi").add(Restrictions.eq("msi.itemId", itemId))
						.createAlias("storeCoInternalIndentM1", "storeM").add(Restrictions.eq("storeM.status", "A"))
						.add(Restrictions.isNull("forwardedFlag")).list();

				StoreDoInternalIndentT storeDoT = new StoreDoInternalIndentT();
				if(!indentFlag.equalsIgnoreCase("direct")) {
					storeDoT.setAvailableStock(Long.parseLong(availableQty));
				}
				
				storeDoT.setItemId(itemId);
				storeDoT.setQtyRequest(Long.parseLong(requiredQty));
				storeDoT.setApprovedQty(approvedQty);
				storeDoT.setStoreDoMId(storeDoMId);
				storeDoT.setPoQty(poQty);
				if(item.get("unitRate") != null && !item.get("unitRate").equals("") && !item.get("unitRate").equals("null")) {
					Double unitRate = Double.parseDouble(String.valueOf(item.get("unitRate")));
					storeDoT.setUnitRate(unitRate);
				}
				Long storeDoDtId = Long.parseLong(String.valueOf(session.save(storeDoT)));

				for (StoreCoInternalIndentT storeT : storeTList) {
					storeT.setForwardedFlag("Y");
					storeT.setStoreDoDtId(storeDoDtId);
					session.save(storeT);
				}
			}

			tx.commit();
			map.put("status", true);
			map.put("msg", "PO created successfully");
			return map;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		map.put("status", false);
		map.put("msg", "Something went wrong");
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getCityWiseIndentList(Map<String, Object> requestData) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> responseList = new ArrayList<Map<String, Object>>();
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		try {
			Long itemId = Long.parseLong(String.valueOf(requestData.get("itemId")));
			Long districtId = Long.parseLong(String.valueOf(requestData.get("districtId")));

			List<StoreCoInternalIndentT> indentCoTList = session.createCriteria(StoreCoInternalIndentT.class)
					.createAlias("storeCoInternalIndentM1", "storeCoM").add(Restrictions.eq("storeCoM.status", "A"))
					.add(Restrictions.isNull("forwardedFlag")).createAlias("masStoreItem", "msi")
					.add(Restrictions.eq("msi.itemId", itemId)).add(Restrictions.eq("storeCoM.districtId", districtId))
					.list();

			map.put("list", indentCoTList);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getMasSupplierList(Map<String, Object> requestData) {
		Map<String, Object> map = new HashMap<String, Object>();
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		try {
			
			

			Criteria cr = session.createCriteria(MasStoreSupplier.class)
					.addOrder(Order.asc("supplierName"));
			if(requestData.get("id") != null && !String.valueOf(requestData.get("id")).equals("") && !String.valueOf(requestData.get("id")).equals("null")) {
				Long id = Long.parseLong(String.valueOf(requestData.get("id")));
				MasStoreSupplierType msst = new MasStoreSupplierType();
				msst.setSupplierTypeId(id); 
				cr = cr.createAlias("masStoreSupplierType", "msst");
				cr = cr.add(Restrictions.eq("msst.supplierTypeId", id));
			}
			cr.add(Restrictions.eq("status", "y").ignoreCase());
			
			List<MasStoreSupplier> supplierList = cr.list();
			map.put("list", supplierList);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getMasSupplierListNew(Map<String, Object> requestData) {
		Map<String, Object> map = new HashMap<String, Object>();
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		List<MasStoreSupplierNew> supplierList = new ArrayList<MasStoreSupplierNew>();
		try {

			Criteria cr = session.createCriteria(MasStoreSupplierNew.class);
			
			if(requestData.get("supplierTypeId") != null && !String.valueOf(requestData.get("supplierTypeId")).equals("") && !String.valueOf(requestData.get("supplierTypeId")).equals("null")) {
				cr = cr.add(Restrictions.eq("supplierTypeId", Long.parseLong(String.valueOf(requestData.get("supplierTypeId")))));
			}
			
			cr = cr.addOrder(Order.asc("supplierName"));
			supplierList = cr.list();

			map.put("list", supplierList);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getMasSupplierTypeList(Map<String, Object> requestData) {
		Map<String, Object> map = new HashMap<String, Object>();
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		try {

			List<MasStoreSupplierType> storeSupplierTypeList = session.createCriteria(MasStoreSupplierType.class)
					.addOrder(Order.asc("supplierTypeName"))
					.list();

			map.put("list", storeSupplierTypeList);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@Override
	public Map<String, Object> getRvWaitingList(Map<String, Object> payload) {
		Map<String, Object> map = new HashMap<>();
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		try {
			int pageNo = Integer.parseInt(String.valueOf(payload.get("pageNo")));
			int pagingSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
			long districtId = Long.parseLong(String.valueOf(payload.get("districtId")));
			int count = 0;

			Criteria criteria = session.createCriteria(StoreDoInternalIndentM.class)
					.add(Restrictions.eq("districtId", districtId))
					.add(Restrictions.or(Restrictions.isNull("status"), Restrictions.eq("status", "P").ignoreCase()))
					.addOrder(Order.desc("poDate"));

			List<StoreDoInternalIndentM> list = criteria.list();
			count = list.size();

			criteria = criteria.setFirstResult(pagingSize * (pageNo - 1));
			criteria = criteria.setMaxResults(pagingSize);
			list = criteria.list();

			map.put("count", count);
			map.put("list", list);
			return map;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> rvDetail(HashMap<String, String> payload) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> responseList = new ArrayList<Map<String, Object>>();
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		try {
			Long id = Long.parseLong(String.valueOf(payload.get("id")));

			StoreDoInternalIndentM indentM = (StoreDoInternalIndentM) session.get(StoreDoInternalIndentM.class, id);

			List<StoreDoInternalIndentT> list = session.createCriteria(StoreDoInternalIndentT.class)
					.add(Restrictions.eq("storeDoMId", id))
					
					.list();

			map.put("hdList", indentM);
			map.put("list", list);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@Override
	public Map<String, Object> indentIssueWaitingListForDO(Map<String, Object> payload) {
		Map<String, Object> map = new HashMap<String, Object>();
		int pageSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
		int pageNo1 = 0;
		int count = 0;
		if (payload.get("PN") != null)
			pageNo1 = Integer.parseInt(payload.get("PN").toString());

		Long districtId = Long.parseLong(String.valueOf(payload.get("districtId")));
		List<StoreCoInternalIndentM> storeCoInternalIndentMList = new ArrayList<StoreCoInternalIndentM>();
		Criteria cr = null;

		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			cr = session.createCriteria(StoreCoInternalIndentM.class).add(Restrictions.eq("districtId", districtId));
			cr = cr.add(Restrictions.or(Restrictions.eq("status", "A"), Restrictions.eq("status", "P")));
			cr.addOrder(Order.desc("demandDate"));
			count = cr.list().size();

			cr.setFirstResult((pageSize) * (pageNo1 - 1));
			cr.setMaxResults(pageSize);
			storeCoInternalIndentMList = cr.list();

			map.put("list", storeCoInternalIndentMList);
			map.put("count", count);

			return map;
		} catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
		} finally {

			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		map.put("list", storeCoInternalIndentMList);
		map.put("count", 0);
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String submitRvDetailAgainstPo(Map<String, Object> jsondata) {
		String msg = "";
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Transaction tx = session.beginTransaction();
		try {

			Calendar calendar = Calendar.getInstance();
			java.sql.Timestamp ourJavaTimestampObject = new java.sql.Timestamp(calendar.getTime().getTime());
			List<Map<String, Object>> listOfData = (List<Map<String, Object>>) jsondata.get("list");
			long hdId = Long.parseLong(String.valueOf(jsondata.get("hdId")));
			long userId = Long.parseLong(String.valueOf(jsondata.get("userId")));
			Long districtId = null;
			Long vendorTypeId = null;
			Long vendorNameId = null;
			StoreDoInternalIndentM sim = (StoreDoInternalIndentM) session.get(StoreDoInternalIndentM.class, hdId);
			if (sim != null) {
				districtId = sim.getDistrictId();
				vendorTypeId = sim.getVendorTypeId();
				vendorNameId = sim.getVendorNewId();
				sim.setStatus("I");
				session.save(sim);
			}

			List<StoreGrnM> storeGrnMList = (List<StoreGrnM>) session.createCriteria(StoreGrnM.class)
					.add(Restrictions.eq("store_do_hd_id", hdId)).list();
			long issueMid = 0;
			if (storeGrnMList.isEmpty()) {
				StoreGrnM storeGrnM = new StoreGrnM();
				Users createdByUser = new Users();
				createdByUser.setUserId(userId);
				storeGrnM.setCreatedBy(createdByUser);
				storeGrnM.setDistrictId(districtId);
				storeGrnM.setStoreDoHdId(hdId);
				storeGrnM.setLastChgDate(ourJavaTimestampObject);
				storeGrnM.setLastChgBy(createdByUser);
				MasStoreSupplier mss = new MasStoreSupplier();
				mss.setSupplierId(vendorNameId);
				MasStoreSupplierType msst = new MasStoreSupplierType();
				msst.setSupplierTypeId(vendorTypeId);
				storeGrnM.setMasStoreSupplier(mss);
				issueMid = (long) session.save(storeGrnM);
			} else {
				for (StoreGrnM storeGrnM : storeGrnMList) {
					issueMid = storeGrnM.getGrnMId();
				}
			}
			System.out.println("issueMid " + issueMid);
			/*
			 * tx.commit(); tx = session.beginTransaction();
			 */
			for (Map<String, Object> map : listOfData) {
				// header_id = Long.parseLong(String.valueOf(map.get("header_id")));
				long itemId = Long.parseLong(String.valueOf(map.get("itemId")));
				long detailId = Long.parseLong(String.valueOf(map.get("dtId")));
				long requiredQty = Long.parseLong(String.valueOf(map.get("requiredQty")));
				long approvedQty = Long.parseLong(String.valueOf(map.get("approvedQty")));
				long poQty = Long.parseLong(String.valueOf(map.get("poQty")));
				long rvQty = Long.parseLong(String.valueOf(map.get("rvQty")));
				String batch = String.valueOf(map.get("batch"));
				Date domDate = HMSUtil.convertStringTypeDateToDateType(String.valueOf(map.get("dom")));
				Date doeDate = HMSUtil.convertStringTypeDateToDateType(String.valueOf(map.get("doe")));
				long unitRate = Long.parseLong(String.valueOf(map.get("unitRate")));

				StoreItemBatchStock stock = null;
				List<StoreItemBatchStock> storeItemBatchList = session.createCriteria(StoreItemBatchStock.class)
						.add(Restrictions.eq("districtId", districtId))
						.add(Restrictions.eq("masStoreItem.itemId", itemId)).add(Restrictions.eq("batchNo", batch))
						.list();
				Long stockId = null;
				/*
				 * if (!storeItemBatchList.isEmpty() && storeItemBatchList.size() > 0) { stock =
				 * storeItemBatchList.get(0); long closingBalanceQty =
				 * storeItemBatchList.get(0).getClosingStock() != null ?
				 * storeItemBatchList.get(0).getClosingStock() : 0; closingBalanceQty =
				 * closingBalanceQty + rvQty; stock.setClosingStock(closingBalanceQty);
				 * 
				 * long receivedQty = storeItemBatchList.get(0).getReceivedQty() != null ?
				 * storeItemBatchList.get(0).getReceivedQty() : 0; //receivedQty = receivedQty +
				 * stock; stock.setReceivedQty(receivedQty);
				 * 
				 * stock.setLastChgDate(new Timestamp(new Date().getTime())); Users lastChg =
				 * new Users(); lastChg.setUserId(userId); stock.setUser(lastChg); stock.setma
				 * MasDepartment dept = new MasDepartment(); long departmentId = 2051;
				 * dept.setDepartmentId(departmentId); stock.setMasDepartment(dept);
				 * session.update(stock); } else { stock = new StoreItemBatchStock(); BigDecimal
				 * unitRate = null; BigDecimal totalAmount = null; stock.setMmuId(mmuId);
				 * 
				 * StoreItemBatchStock storeStock =
				 * (StoreItemBatchStock)session.get(StoreItemBatchStock.class,
				 * stockId.getLong(i)); if(storeStock!=null && storeStock.getMrp()!=null) {
				 * unitRate = storeStock.getMrp(); totalAmount =
				 * unitRate.multiply(BigDecimal.valueOf(qtyReceived.getLong(i)));
				 * stock.setMrp(unitRate); stock.setCostPrice(totalAmount); }
				 * 
				 * 
				 * MasStoreItem item = new MasStoreItem(); item.setItemId(itemId.getLong(i)); //
				 * item.setItemId(Long.parseLong(itemId.get(i).toString()));
				 * stock.setMasStoreItem(item);
				 * 
				 * stock.setBatchNo(batchNumber.get(i).toString());
				 * 
				 * stock.setClosingStock(qtyReceived.getLong(i));
				 * stock.setReceivedQty(qtyReceived.getLong(i));
				 * 
				 * if (domDate.get(i) != null && !domDate.get(i).toString().isEmpty()) {
				 * stock.setManufactureDate(
				 * HMSUtil.convertStringDateToUtilDateForDatabase(domDate.get(i).toString())); }
				 * 
				 * stock.setExpiryDate(HMSUtil.convertStringDateToUtilDateForDatabase(doeDate.
				 * get(i).toString()));
				 * 
				 * stock.setLastChgDate(new Timestamp(new Date().getTime()));
				 * 
				 * 
				 * MasDepartment dept = new MasDepartment(); dept.setDepartmentId(departmentId);
				 * stock.setMasDepartment(dept);
				 * 
				 * 
				 * Users lastChg = new Users(); lastChg.setUserId(userId);
				 * stock.setUser(lastChg);
				 * 
				 * session.save(stock);
				 * 
				 * }
				 */

				// updating the stock
				/*
				 * StoreItemBatchStock bStock = (StoreItemBatchStock)
				 * session.get(StoreItemBatchStock.class, stockId); if (bStock != null) {
				 * bStock.setIssueQty(issuedQty); bStock.setClosingStock(batchStock);
				 * bStock.setLastChgDate(ourJavaTimestampObject); session.update(bStock); }
				 */

				// saving record into transaction table
				/*StoreIssueT storeIssueT = new StoreIssueT();
				storeIssueT.setQtyIssued(issuedQty);
				StoreIssueM issueM = new StoreIssueM();
				issueM.setId(issueMid);
				storeIssueT.setStoreIssueM(issueM);
				storeIssueT.setItemId(itemId);
				if (flag) {
					storeIssueT.setBatchStockId(stockId);
					storeIssueT.setExpiryDate(expiryDate);
					storeIssueT.setBatchNo(batchNo);
				}
				// storeIssueT.setQtyRequest(qtyDemanded);
				storeIssueT.setQtyRequest(qtyDemanded);
				StoreInternalIndentT sit = new StoreInternalIndentT();
				sit.setId(detailId);
				System.out.println("detailId " + detailId);
				storeIssueT.setStoreInternalIndentT(sit);
				storeIssueT.setIssueDate(ourJavaTimestampObject);
				Users user = new Users();
				user.setUserId(lastChangeBy);
				storeIssueT.setUsers(user);
				session.save(storeIssueT);
			}*/
			/*StoreInternalIndentM simM = (StoreInternalIndentM) session.get(StoreInternalIndentM.class, header_id);
			if (simM != null) {
				simM.setStatus("I");
				session.save(simM);
			}
			tx.commit();
			msg = "success";
			return msg;*/
			}
		} catch (Exception ex) {
			tx.rollback();
			ex.printStackTrace();
			msg = "Error while issuing Medicine";
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			tx = null;
		}
		return msg;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getIndentIssueHeaderDo(Map<String, Object> jsondata) {
		Map<String, Object> map = new HashMap<>();
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		try {
			long headerId = Long.parseLong(String.valueOf(jsondata.get("id")));

			List<StoreCoInternalIndentM> headerList = (List<StoreCoInternalIndentM>) session
					.createCriteria(StoreCoInternalIndentM.class).add(Restrictions.eq("id", headerId)).list();

			map.put("headerList", headerList);
			return map;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getIndentIssueDetailsDo(Map<String, Object> jsondata) {
		Map<String, Object> map = new HashMap<>();
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		try {
			long headerId = Long.parseLong(String.valueOf(jsondata.get("id")));

			List<StoreCoInternalIndentT> detailList = (List<StoreCoInternalIndentT>) session
					.createCriteria(StoreCoInternalIndentT.class).createAlias("storeCoInternalIndentM1", "hd")
					.add(Restrictions.eq("hd.id", headerId)).list();
			map.put("detailList", detailList);
			return map;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String indentIssueDo(Map<String, Object> jsondata) {
		String msg = "";
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Transaction tx = session.beginTransaction();
		try {

			Calendar calendar = Calendar.getInstance();
			java.sql.Timestamp ourJavaTimestampObject = new java.sql.Timestamp(calendar.getTime().getTime());
			List<Map<String, Object>> listOfData = (List<Map<String, Object>>) jsondata.get("dtData");
			long districtId = Long.parseLong(String.valueOf(jsondata.get("districtId")));
			long lastChangeBy = Long.parseLong(String.valueOf(jsondata.get("user_id")));
			long header_id = Long.parseLong(String.valueOf(jsondata.get("header_id")));
			String issueFlag = String.valueOf(jsondata.get("flag"));
			Long cityId = null;
			StoreCoInternalIndentM sim = (StoreCoInternalIndentM) session.get(StoreCoInternalIndentM.class, header_id);
			if (sim != null) {
				cityId = sim.getCityId();
			}

			List<StoreIssueM> storeIssueMList = (List<StoreIssueM>) session.createCriteria(StoreIssueM.class)
					.add(Restrictions.eq("indentMId", header_id)).list();
			long issueMid = 0;
			StoreIssueM storeIssueM = new StoreIssueM();
			storeIssueM.setIssueType("I");
			storeIssueM.setIssueDate(new Date());
			storeIssueM.setIndentCoMId(header_id);
			storeIssueM.setLastChgDate(ourJavaTimestampObject);
			storeIssueM.setLastChgBy(lastChangeBy);
			storeIssueM.setIssuedBy(lastChangeBy);
			storeIssueM.setIssueDate(ourJavaTimestampObject);
			storeIssueM.setCityId(cityId); 
			issueMid = (long) session.save(storeIssueM);
			
			/*
			 * if (storeIssueMList.isEmpty()) { StoreIssueM storeIssueM = new StoreIssueM();
			 * storeIssueM.setIssueType("I"); storeIssueM.setIssueDate(new Date());
			 * storeIssueM.setIndentCoMId(header_id);
			 * storeIssueM.setLastChgDate(ourJavaTimestampObject);
			 * storeIssueM.setLastChgBy(lastChangeBy);
			 * storeIssueM.setIssuedBy(lastChangeBy);
			 * storeIssueM.setIssueDate(ourJavaTimestampObject);
			 * storeIssueM.setCityId(cityId); issueMid = (long) session.save(storeIssueM); }
			 * else { for (StoreIssueM storeIssueM : storeIssueMList) { issueMid =
			 * storeIssueM.getId(); } }
			 */
			System.out.println("issueMid " + issueMid);
			/*
			 * tx.commit(); tx = session.beginTransaction();
			 */
			for (Map<String, Object> map : listOfData) {
				// header_id = Long.parseLong(String.valueOf(map.get("header_id")));
				long itemId = Long.parseLong(String.valueOf(map.get("item_id")));
				long detailId = Long.parseLong(String.valueOf(map.get("detail_id")));
				long qtyDemanded = (long) Double.parseDouble(String.valueOf(map.get("qty_demanded")));
				long issuedQty = (long) Double.parseDouble(String.valueOf(map.get("qty_issued")));
				System.out.println("issue quantity " + issuedQty);
				// long balanceAfterIssue =
				// Long.parseLong(String.valueOf(map.get("balance_after_issue")));
				boolean flag = true;
				Date expiryDate = null;
				long stockId = 0;
				String checkStockID = String.valueOf(map.get("stock_id"));
				if (checkStockID == null || checkStockID.equals("") || checkStockID.equals("null")) {
					stockId = 0;
					flag = false;
				} else {
					stockId = Long.parseLong(checkStockID);
					expiryDate = HMSUtil.convertStringTypeDateToDateType(String.valueOf(map.get("date_of_expiry")));
				}

				/*
				 * String checkStockID = String.valueOf(map.get("stock_id")); if(checkStockID ==
				 * null || checkStockID.equals("") || checkStockID.equals("null")) { continue; }
				 */
				String batchNo = String.valueOf(map.get("batch_no"));
				// Date manufacturingDate =
				// HMSUtil.convertStringTypeDateToDateType(String.valueOf(map.get("date_of_manufacturing")));

				// long bStock = Long.parseLong(String.valueOf(map.get("batch_stock")));
				// long availableStock = Long.parseLong(String.valueOf(map.get("disp_stock")));

				int expiryDays = Integer.parseInt(HMSUtil.getProperties("adt.properties", "EXPIRY_DAYS"));
				long dispStock = 0;
				long batchStock = 0;
				Date date = new Date();
				Calendar c = Calendar.getInstance();
				c.setTime(date);
				c.add(Calendar.DATE, expiryDays);
				date = c.getTime();
				// checking exact batch and total batch stock at runtime
				String sql = "select CLOSING_STOCK, STOCK_ID from STORE_ITEM_BATCH_STOCK where ITEM_ID =:item_id and district_id =:districtId and CLOSING_STOCK > 0 and EXPIRY_DATE >:exp_date";
				Query query = session.createSQLQuery(sql);
				query.setLong("item_id", itemId);
				query.setLong("districtId", districtId);
				query.setDate("exp_date", date);
				List<Object[]> result = query.list();
				for (Object[] obj : result) {
					if (obj[0] != null) {
						dispStock += ((Double) obj[0]).longValue();
					}
					if (stockId == ((BigInteger) obj[1]).longValue()) {
						if (obj[1] != null) {
							batchStock = ((Double) obj[0]).longValue();
						}
					}

				}
				if (batchStock >= issuedQty) {
					dispStock = dispStock - issuedQty;
					batchStock = batchStock - issuedQty;
				} else {
					msg = "No Enough Stock in Selected Batch";
				}
				long dtId = 0;
				// saving or updating record on the basis of detail id
				// long quantityBalance = qtyDemanded - issuedQty;
				if (detailId != 0) {
					/*
					 * long issuedQt = 0; for (Map<String, Object> data : listOfData) { long item_id
					 * = Long.parseLong(String.valueOf(data.get("item_id"))); if (itemId == item_id)
					 * { issuedQt = issuedQt + (long)
					 * Double.parseDouble(String.valueOf(data.get("qty_issued"))); } }
					 */
					
					StoreCoInternalIndentT sIt = (StoreCoInternalIndentT) session.get(StoreCoInternalIndentT.class, detailId);
					if (sIt != null) {
						// sIt.setAvailableStock(dispStock);
						if(sIt.getQtyIssued() == null) {
							sIt.setQtyIssued(issuedQty);
						}else {
							sIt.setQtyIssued(sIt.getQtyIssued() + issuedQty);
						}
						
						session.update(sIt);
					}

				}
				
				
				// updating the stock
				StoreItemBatchStock bStock = (StoreItemBatchStock) session.get(StoreItemBatchStock.class, stockId);
				if (bStock != null) {
					
					if(bStock.getIssueQty() != null) {
						bStock.setIssueQty(bStock.getIssueQty() + issuedQty);
						
					}else {
						bStock.setIssueQty(issuedQty);
					}
					
					
					bStock.setClosingStock(batchStock);
					bStock.setLastChgDate(ourJavaTimestampObject);
					session.update(bStock);
				}

				// saving record into transaction table
				StoreIssueT storeIssueT = new StoreIssueT();
				storeIssueT.setQtyIssued(issuedQty);
				StoreIssueM issueM = new StoreIssueM();
				issueM.setId(issueMid);
				storeIssueT.setStoreIssueM(issueM);
				storeIssueT.setItemId(itemId);
				if (flag) {
					storeIssueT.setBatchStockId(stockId);
					storeIssueT.setExpiryDate(expiryDate);
					storeIssueT.setBatchNo(batchNo);
				}
				// storeIssueT.setQtyRequest(qtyDemanded);
				storeIssueT.setQtyRequest(qtyDemanded);
				/*
				 * StoreInternalIndentT sit = new StoreInternalIndentT(); sit.setId(detailId);
				 */
				System.out.println("detailId " + detailId);
				storeIssueT.setStoreCoTId(detailId);
				storeIssueT.setIssueDate(ourJavaTimestampObject);
				Users user = new Users();
				user.setUserId(lastChangeBy);
				storeIssueT.setUsers(user);
			 	session.save(storeIssueT);
			}
			StoreCoInternalIndentM simM = (StoreCoInternalIndentM) session.get(StoreCoInternalIndentM.class, header_id);
			if (simM != null) {
				if(issueFlag.equalsIgnoreCase("P")) {
					simM.setStatus("P");
					System.out.println("co status changed to P");
				}else if(issueFlag.equalsIgnoreCase("F")){
					simM.setStatus("I");
				}
				
				session.save(simM);
			}
			System.out.println("******************************data saved************************");
			tx.commit();
			msg = "success";
			return msg;
		} catch (Exception ex) {
			tx.rollback();
			ex.printStackTrace();
			msg = "Error while issuing Medicine";
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			tx = null;
		}
		return msg;
	}

	@Override
	public Map<String, Object> getIndentListCo(long pageNo, HashMap<String, String> requestData) {
		Map<String, Object> map = new HashMap<String, Object>();
		int pageSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
		int pageNo1 = 0;
		List totalMatches = new ArrayList();
		if (requestData.get("PN") != null)
			pageNo1 = Integer.parseInt(requestData.get("PN").toString());
		List<StoreInternalIndentM> storeInternalIndentMList = new ArrayList<StoreInternalIndentM>();
		JSONObject json = new JSONObject(requestData);
		long cityId = Long.parseLong(json.getString("cityId").toString());
		long departmentId = Long.parseLong(json.getString("departmentId").toString());

		Criteria cr = null;
		Date indentStratDate = null;
		Date indentEndDate = null;
		Date finalIndentEndDate = null;
		String startDate = "";
		String endDate = "";
		if (json.has("fromDate")) {
			startDate = json.getString("fromDate");
			endDate = json.getString("toDate");
			if ((!startDate.isEmpty() && startDate != null) && (!endDate.isEmpty() && endDate != null)) {
				indentStratDate = HMSUtil.convertStringDateToUtilDate(startDate, "dd/MM/yyyy");
				indentEndDate = HMSUtil.convertStringDateToUtilDate(endDate, "dd/MM/yyyy");

				Calendar cal = Calendar.getInstance();
				cal.setTime(indentEndDate);
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 59);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				// Date from = cal.getTime();
				cal.set(Calendar.HOUR_OF_DAY, 23);
				finalIndentEndDate = cal.getTime();

			}
		}

		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			cr = session.createCriteria(StoreInternalIndentM.class);
			cr = cr.add(Restrictions.eq("cityId", cityId));
			/*
			 * if (json.has("hospitalFlag")) { if (!json.getString("hospitalFlag").isEmpty()
			 * && json.getString("hospitalFlag").equalsIgnoreCase("F")) { cr =
			 * cr.add(Restrictions.eq("mmuId", mmuId)); } else { cr =
			 * cr.add(Restrictions.or(Restrictions.eq("mmuId", mmuId),
			 * Restrictions.eq("fwcParentMasHospital.hospitalId", hospitalId))); } }else{ cr
			 * = cr.add(Restrictions.eq("mmuId", mmuId)); }
			 */

			// cr = cr.add(Restrictions.eq("departmentId", departmentId));

			cr.addOrder(Order.desc("demandDate"));
			if (json.get("flag").toString().equalsIgnoreCase("Y")) {

				cr = cr.add(Restrictions.eq("status", "Y").ignoreCase());
			} else if (json.get("flag").toString().equalsIgnoreCase("YN")) {

				Criterion cr1 = Restrictions.eq("status", "Y").ignoreCase();
				Criterion cr2 = Restrictions.eq("status", "N").ignoreCase();
				LogicalExpression orExp = Restrictions.or(cr1, cr2);
				cr.add(orExp);
			} else if (json.get("flag").toString().equalsIgnoreCase("A")) {
				cr = cr.add(Restrictions.eq("status", "A").ignoreCase());
			} else if (json.get("flag").toString().equalsIgnoreCase("R")) {
				cr = cr.add(Restrictions.eq("status", "R").ignoreCase());
			}
			if ((!startDate.isEmpty() && startDate != null) && (!endDate.isEmpty() && endDate != null)) {
				cr = cr.add(Restrictions.ge("demandDate", indentStratDate))
						.add(Restrictions.lt("demandDate", finalIndentEndDate));
			}

			if (!cr.list().isEmpty() && cr.list().size() > 0) {

				totalMatches = cr.list();
				cr.setFirstResult((pageSize) * (pageNo1 - 1));
				cr.setMaxResults(pageSize);
				storeInternalIndentMList = cr.list();
			}
		} catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.getMessage();
			e.printStackTrace();
		} finally {

			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		map.put("storeInternalIndentMList", storeInternalIndentMList);
		map.put("totalMatches", totalMatches.size());
		return map;
	}

	@Override
	public Map<String, Object> getIndentListDo(long pageNo, HashMap<String, String> requestData) {
		Map<String, Object> map = new HashMap<String, Object>();
		int pageSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
		int pageNo1 = 0;
		List totalMatches = new ArrayList();
		if (requestData.get("PN") != null)
			pageNo1 = Integer.parseInt(requestData.get("PN").toString());
		List<StoreDoInternalIndentM> storeInternalIndentMList = new ArrayList<StoreDoInternalIndentM>();
		JSONObject json = new JSONObject(requestData);
		long districtId = Long.parseLong(json.getString("districtId").toString());
		long departmentId = Long.parseLong(json.getString("departmentId").toString());

		Criteria cr = null;
		Date indentStratDate = null;
		Date indentEndDate = null;
		Date finalIndentEndDate = null;
		String startDate = "";
		String endDate = "";
		if (json.has("fromDate")) {
			startDate = json.getString("fromDate");
			endDate = json.getString("toDate");
			if ((!startDate.isEmpty() && startDate != null) && (!endDate.isEmpty() && endDate != null)) {
				indentStratDate = HMSUtil.convertStringDateToUtilDate(startDate, "dd/MM/yyyy");
				indentEndDate = HMSUtil.convertStringDateToUtilDate(endDate, "dd/MM/yyyy");

				Calendar cal = Calendar.getInstance();
				cal.setTime(indentEndDate);
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 59);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				// Date from = cal.getTime();
				cal.set(Calendar.HOUR_OF_DAY, 23);
				finalIndentEndDate = cal.getTime();

			}
		}

		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			cr = session.createCriteria(StoreDoInternalIndentM.class);
			cr = cr.add(Restrictions.eq("districtId", districtId));

			cr.addOrder(Order.desc("demandDate"));
			
			if ((!startDate.isEmpty() && startDate != null) && (!endDate.isEmpty() && endDate != null)) {
				cr = cr.add(Restrictions.ge("demandDate", indentStratDate))
						.add(Restrictions.lt("demandDate", finalIndentEndDate));
			}

			if (!cr.list().isEmpty() && cr.list().size() > 0) {

				totalMatches = cr.list();
				cr.setFirstResult((pageSize) * (pageNo1 - 1));
				cr.setMaxResults(pageSize);
				storeInternalIndentMList = cr.list();
			}
		} catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.getMessage();
			e.printStackTrace();
		} finally {

			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		map.put("storeInternalIndentMList", storeInternalIndentMList);
		map.put("totalMatches", totalMatches.size());
		return map;
	}

	@Override
	public Map<String, Object> getDrugList(Map<String, Object> payload) {
		Map<String, Object> map = new HashMap<String, Object>();

		String mmuId = String.valueOf(payload.get("mmuId"));
		String cityId = String.valueOf(payload.get("cityId"));
		String districtId = String.valueOf(payload.get("districtId"));
		int expiryDays = 0;
		if (!payload.containsKey("stockAdjustFlag")) { // this key comes in case of stock adjustment
			expiryDays = Integer.parseInt(HMSUtil.getProperties("adt.properties", "EXPIRY_DAYS"));
		}
		
		Date date = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, expiryDays);
		date = c.getTime();
		
		List<StoreItemBatchStock> storeItemBatchList = new ArrayList<StoreItemBatchStock>();
		Criteria cr = null;

		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			cr = session.createCriteria(StoreItemBatchStock.class);
			if(!mmuId.equals("0") && !mmuId.equals("") && mmuId != null && !mmuId.equals("null")) {
				cr = cr.add(Restrictions.eq("mmuId", Long.parseLong(mmuId)));
			}else if(!cityId.equals("0") && !cityId.equals("") && cityId != null && !cityId.equals("null")) {
				cr = cr.add(Restrictions.eq("cityId", Long.parseLong(cityId)));
			}else if(!districtId.equals("0") && !districtId.equals("") && districtId != null && !districtId.equals("null")) {
				cr = cr.add(Restrictions.eq("districtId", Long.parseLong(districtId)));
			}
			cr = cr.add(Restrictions.gt("closingStock", new Long(0))).add(Restrictions.gt("expiryDate", date));
			cr = cr.createAlias("masStoreItem", "masStoreItem")
					.addOrder(Order.asc("masStoreItem.nomenclature"));
			storeItemBatchList = cr.list();

			map.put("list", storeItemBatchList);

			return map;
		} catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
		} finally {

			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		map.put("list", storeItemBatchList);
		return map;
	}

	@Override
	public String updateUnitRate(Map<String, Object> payload) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Transaction tx = session.beginTransaction();
		String msg = "";
		try {
			
			List<Map<String,Object>> list = (List<Map<String, Object>>) payload.get("list");
			for(Map<String,Object> map : list) {
				long stockId = Long.parseLong(String.valueOf(map.get("stockId")));
				BigDecimal unitRate = new BigDecimal(map.get("unitRate").toString());
				
				StoreItemBatchStock sibs = (StoreItemBatchStock) session.get(StoreItemBatchStock.class, stockId);
				if(sibs != null) {
					sibs.setMrp(unitRate);
					session.save(sibs);
				}
			}
			
			tx.commit();
			msg = "Unit Rate updated successfully";
			return msg;
		} catch (Exception ex) {
			tx.rollback();
			ex.printStackTrace();
			msg = "Error while updating unit rate";
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			tx = null;
		}
		return msg;
	}
	
	@Override
	public Map<String, Object> getIndentDetailsForTracking(HashMap<String, String> jsondata) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		Criteria cr = null;
		List<StoreInternalIndentT> storeInternalIndentTList = null;
		JSONObject json = new JSONObject(jsondata);
		long indentid = Long.parseLong(json.getString("indentId"));
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			cr = session.createCriteria(StoreInternalIndentT.class).createAlias("storeInternalIndentM1", "sm");
			cr = cr.add(Restrictions.eq("sm.id", indentid));
			cr.addOrder(Order.asc("id"));
			if (!cr.list().isEmpty() && cr.list().size() > 0) {

				storeInternalIndentTList = cr.list();
			}
		} catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.getMessage();
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		map.put("storeInternalIndentTList", storeInternalIndentTList);
		map.put("status", "1");
		return map;

	}

	@Override
	public Map<String, Object> getIndentDetailsForTrackingCo(HashMap<String, String> jsondata) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<MasStoreSupplierType> getMasStoreSupplierType(){
		List<MasStoreSupplierType> masStoreSupplierTypeList = new ArrayList<>();
		try {
		
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasStoreSupplierType.class);
			masStoreSupplierTypeList = criteria.list();
		}catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return masStoreSupplierTypeList;
	}
	
	public List<MasStoreSupplierNew> getMasStoreSupplierNew(){
		List<MasStoreSupplierNew> masStoreSuppliernewList = new ArrayList<>();
		try {
		
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasStoreSupplierNew.class);
			masStoreSuppliernewList = criteria.list();
		}catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return masStoreSuppliernewList;
	}
	public List<MasStoreSupplierNew> getMasStoreSupplierNew(Long id,Long districtId){
		List<MasStoreSupplierNew> masStoreSuppliernewList = new ArrayList<>();
		try {
		
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasStoreSupplierNew.class);
			criteria = criteria.add(Restrictions.eq("supplierTypeId", id));
			if(id==2) {
				criteria = criteria.add(Restrictions.eq("upssId", districtId));
			}
			masStoreSuppliernewList = criteria.list();
		}catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return masStoreSuppliernewList;
	}

}
