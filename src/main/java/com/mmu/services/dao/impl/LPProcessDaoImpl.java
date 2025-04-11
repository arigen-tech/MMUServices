package com.mmu.services.dao.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mmu.services.dao.DispensaryDao;
import com.mmu.services.dao.LPProcessDao;
import com.mmu.services.entity.FundUtilization;
import com.mmu.services.entity.MasDepartment;
import com.mmu.services.entity.MasEmployee;
import com.mmu.services.entity.MasHospital;
import com.mmu.services.entity.MasStoreFinancial;
import com.mmu.services.entity.MasStoreItem;
import com.mmu.services.entity.MasStoreLpType;
import com.mmu.services.entity.MasStoreLpc;
import com.mmu.services.entity.MasStoreSupplier;
import com.mmu.services.entity.MasStoreUnit;
import com.mmu.services.entity.StoreBudgetaryM;
import com.mmu.services.entity.StoreBudgetaryT;
import com.mmu.services.entity.StorePoM;
import com.mmu.services.entity.StorePoT;
import com.mmu.services.entity.StoreQuotationM;
import com.mmu.services.entity.StoreQuotationT;
import com.mmu.services.entity.StoreSoM;
import com.mmu.services.entity.StoreSoT;
import com.mmu.services.entity.TempDirectReceivingForBackLp;
import com.mmu.services.entity.Users;
import com.mmu.services.hibernateutils.GetHibernateUtils;
import com.mmu.services.service.impl.DispensaryServiceImpl;
import com.mmu.services.utils.HMSUtil;

@Repository
@Transactional
public class LPProcessDaoImpl implements LPProcessDao {

	@Autowired
	GetHibernateUtils getHibernateUtils;

	@Autowired
	DispensaryServiceImpl dispenceryService;

	@Autowired
	DispensaryDao dispensaryDao;

	/*
	 * public long saveLPBudgetary(StoreBudgetaryM storeBudgetaryM) { // TODO
	 * Auto-generated method stub long budId =0; try { Session session =
	 * getHibernateUtils.getHibernateUtlis().OpenSession(); Criteria criteria =
	 * session.createCriteria(StoreBudgetaryM.class); Transaction tx =
	 * session.beginTransaction(); Serializable id = session.save(storeBudgetaryM);
	 * tx.commit(); session.flush(); session.clear(); if (id != null) { budId =
	 * (long)id; } else { budId = 0; } } catch (Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.getMessage();
	 * e.printStackTrace(); }finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return budId; }
	 * 
	 * @Override public long savestoreBudgetaryT(StoreBudgetaryT storeBudgetaryT) {
	 * // TODO Auto-generated method stub long budId =0; try { Session session =
	 * getHibernateUtils.getHibernateUtlis().OpenSession(); Criteria criteria =
	 * session.createCriteria(StoreBudgetaryT.class); Transaction tx =
	 * session.beginTransaction(); Serializable id = session.save(storeBudgetaryT);
	 * tx.commit(); session.flush(); session.clear(); if (id != null) { budId =
	 * (long)id; } else { budId = 0; } } catch (Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.getMessage();
	 * e.printStackTrace(); }finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return budId; }
	 */
	/*
	 * @Override public Map<String, Object> getBudgetrayApprovalList(long pageNo,
	 * HashMap<String, String> requestData) { // TODO Auto-generated method stub
	 * Map<String, Object> map = new HashMap<String, Object>(); int pageSize =
	 * Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
	 * int pageNo1= 0; List totalMatches = new ArrayList(); if
	 * (requestData.get("PN") != null) pageNo1 =
	 * Integer.parseInt(requestData.get("PN").toString()); List<StoreBudgetaryM>
	 * storeBudgetaryMList = new ArrayList<StoreBudgetaryM>(); JSONObject json = new
	 * JSONObject(requestData);
	 * 
	 * Criteria cr=null;
	 * 
	 * String status=json.get("flag").toString(); long hospitalId =
	 * Long.parseLong(json.getString("hospitalId").toString()); long departmentId =
	 * Long.parseLong(json.getString("departmentId").toString());
	 * 
	 * try { Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * cr = session.createCriteria(StoreBudgetaryM.class).createAlias("masHospital",
	 * "mh").createAlias("masDepartment", "md"); cr
	 * =cr.add(Restrictions.eq("mh.hospitalId",
	 * hospitalId)).add(Restrictions.eq("md.departmentId", departmentId));;
	 * if(!status.equalsIgnoreCase("ALL")) cr.addOrder(Order.desc("reqDate")); else
	 * cr.addOrder(Order.desc("reqNo")); if(status.equalsIgnoreCase("WL")) {
	 * cr.add(Restrictions.or( Restrictions.eq("status", "W").ignoreCase(),
	 * Restrictions.eq("status", "L").ignoreCase(), Restrictions.eq("status",
	 * "R").ignoreCase()));
	 * 
	 * }else if(status.equalsIgnoreCase("LA")) { cr.add(Restrictions.or(
	 * Restrictions.eq("status", "L").ignoreCase(), Restrictions.eq("status",
	 * "N").ignoreCase(), Restrictions.eq("status", "K").ignoreCase(),
	 * Restrictions.eq("status", "A").ignoreCase()));
	 * 
	 * }else if(!status.equalsIgnoreCase("ALL")) cr=cr.add(Restrictions.eq("status",
	 * status).ignoreCase());
	 * 
	 * if(!cr.list().isEmpty() && cr.list().size()>0) {
	 * 
	 * totalMatches=cr.list(); cr.setFirstResult((pageSize) * (pageNo1 - 1));
	 * cr.setMaxResults(pageSize); storeBudgetaryMList= cr.list(); } } catch
	 * (Exception e) { getHibernateUtils.getHibernateUtlis().CloseConnection();
	 * e.getMessage(); e.printStackTrace(); } finally {
	 * 
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); }
	 * map.put("storeBudgetaryMList", storeBudgetaryMList); map.put("totalMatches",
	 * totalMatches.size()); return map; }
	 * 
	 * @Override public Map<String, Object> getIndentDetails(HashMap<String, String>
	 * jsondata) { // TODO Auto-generated method stub Map<String, Object> map = new
	 * HashMap<String, Object>(); Criteria cr=null; List<StoreBudgetaryT>
	 * storeBudgetaryTList=null; JSONObject json = new JSONObject(jsondata); long
	 * budgetaryId = Long.parseLong(json.getString("budgetaryId")); try { Session
	 * session = getHibernateUtils.getHibernateUtlis().OpenSession(); cr =
	 * session.createCriteria(StoreBudgetaryT.class).createAlias("storeBudgetaryM",
	 * "sm"); cr =cr.add(Restrictions.eq("sm.budgetaryMId", budgetaryId));
	 * cr.addOrder(Order.asc("budgetaryTId")); if(!cr.list().isEmpty() &&
	 * cr.list().size()>0) {
	 * 
	 * storeBudgetaryTList = cr.list(); } } catch (Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.getMessage();
	 * e.printStackTrace(); }finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); }
	 * map.put("storeBudgetaryTList", storeBudgetaryTList); map.put("status","1");
	 * return map;
	 * 
	 * }
	 * 
	 * @Override public StoreBudgetaryM getStoreBudgetaryM(long budMId) { // TODO
	 * Auto-generated method stub StoreBudgetaryM storeBudgetaryM =null;
	 * List<StoreBudgetaryM> storeBudgetaryMList =new ArrayList<StoreBudgetaryM>();
	 * try { Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * storeBudgetaryMList =
	 * session.createCriteria(StoreBudgetaryM.class).add(Restrictions.eq(
	 * "budgetaryMId",budMId)).list();
	 * 
	 * if(storeBudgetaryMList.size()>0) { storeBudgetaryM
	 * =storeBudgetaryMList.get(0); } }catch(Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.printStackTrace();
	 * }finally { getHibernateUtils.getHibernateUtlis().CloseConnection(); }
	 * 
	 * return storeBudgetaryM; }
	 * 
	 * @Override public MasStoreLpType getmasStoreLpType(Long lpTypeId) { // TODO
	 * Auto-generated method stub MasStoreLpType masStoreLpType=null;
	 * List<MasStoreLpType> masStoreLpTypeList =new ArrayList<MasStoreLpType>(); try
	 * { Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * masStoreLpTypeList =
	 * session.createCriteria(MasStoreLpType.class).add(Restrictions.eq("lpTypeId",
	 * lpTypeId)).list();
	 * 
	 * if(masStoreLpTypeList.size()>0) { masStoreLpType =masStoreLpTypeList.get(0);
	 * } }catch(Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.printStackTrace();
	 * }finally { getHibernateUtils.getHibernateUtlis().CloseConnection(); }
	 * 
	 * return masStoreLpType; }
	 * 
	 * @Override public void updateStoreBudgetaryM(StoreBudgetaryM storeBudgetaryM)
	 * { // TODO Auto-generated method stub
	 * 
	 * try { Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * Criteria criteria = session.createCriteria(StoreBudgetaryM.class);
	 * Transaction tx = session.beginTransaction(); session.update(storeBudgetaryM);
	 * tx.commit();
	 * 
	 * } catch (Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.getMessage();
	 * e.printStackTrace(); }finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } }
	 * 
	 * 
	 * @Override public StoreBudgetaryT getStoreBudgetaryT(long budTId) { // TODO
	 * Auto-generated method stub
	 * 
	 * StoreBudgetaryT storeBudgetaryT=null; List<StoreBudgetaryT>
	 * storeBudgetaryTList =new ArrayList<StoreBudgetaryT>(); try { Session session
	 * = getHibernateUtils.getHibernateUtlis().OpenSession(); storeBudgetaryTList =
	 * session.createCriteria(StoreBudgetaryT.class).add(Restrictions.eq(
	 * "budgetaryTId",budTId)).list();
	 * 
	 * if(storeBudgetaryTList.size()>0) { storeBudgetaryT
	 * =storeBudgetaryTList.get(0); } }catch(Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.printStackTrace();
	 * }finally { getHibernateUtils.getHibernateUtlis().CloseConnection(); } return
	 * storeBudgetaryT; }
	 * 
	 * @Override public long saveOrUpdateStoreBudgetaryT(StoreBudgetaryT
	 * storeBudgetaryT) { // TODO Auto-generated method stub long budTId =0; try {
	 * Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * Criteria criteria = session.createCriteria(StoreBudgetaryT.class);
	 * Transaction tx = session.beginTransaction();
	 * session.saveOrUpdate(storeBudgetaryT); tx.commit(); budTId=1; } catch
	 * (Exception e) { getHibernateUtils.getHibernateUtlis().CloseConnection();
	 * e.getMessage(); e.printStackTrace(); }finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return budTId; }
	 * 
	 * @Override public int deleteIndentItem(long budTId) { // TODO Auto-generated
	 * method stub int status=0; Transaction tx=null; List<StoreBudgetaryT>
	 * storeBudgetaryTList=null; try {
	 * 
	 * 
	 * Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * tx=session.beginTransaction();
	 * 
	 * Serializable id = new Long(budTId); StoreBudgetaryT persistentInstance =
	 * (StoreBudgetaryT) session.load(StoreBudgetaryT.class, id); if
	 * (persistentInstance != null) { session.delete(persistentInstance);
	 * tx.commit(); status=1; session.clear(); session.flush();
	 * 
	 * } //session.delete(storeInternalIndentT);
	 * 
	 * } catch (Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.getMessage();
	 * e.printStackTrace(); }finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return status; }
	 * 
	 * @SuppressWarnings("unchecked")
	 * 
	 * @Override public List<MasStoreSupplier> getVendorList(long hospitalId) { //
	 * TODO Auto-generated method stub List<MasStoreSupplier> masStoreSupplierList =
	 * new ArrayList<MasStoreSupplier>(); Session session =
	 * getHibernateUtils.getHibernateUtlis().OpenSession(); try {
	 * masStoreSupplierList = session.createCriteria(MasStoreSupplier.class)
	 * .createAlias("masHospital", "mh") .add(Restrictions.eq("mh.hospitalId",
	 * hospitalId)) .add(Restrictions.eq("status", "Y").ignoreCase())
	 * .addOrder(Order.asc("supplierName")).list(); }catch (Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.getMessage();
	 * e.printStackTrace(); }finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return
	 * masStoreSupplierList; }
	 * 
	 * @Override public MasStoreSupplier getMasStoreSupplier(long supplierId) { //
	 * TODO Auto-generated method stub MasStoreSupplier masStoreSupplier =null;
	 * List<MasStoreSupplier> masStoreSupplierList =new
	 * ArrayList<MasStoreSupplier>(); try { Session session =
	 * getHibernateUtils.getHibernateUtlis().OpenSession(); masStoreSupplierList =
	 * session.createCriteria(MasStoreSupplier.class).add(Restrictions.eq(
	 * "supplierId",supplierId)).list();
	 * 
	 * if(masStoreSupplierList.size()>0) { masStoreSupplier
	 * =masStoreSupplierList.get(0); } }catch(Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.printStackTrace();
	 * }finally { getHibernateUtils.getHibernateUtlis().CloseConnection(); }
	 * 
	 * return masStoreSupplier; }
	 * 
	 * @Override public long saveStoreQuotationM(StoreQuotationM storeQuotationM) {
	 * // TODO Auto-generated method stub long quotationMId =0; try { Session
	 * session = getHibernateUtils.getHibernateUtlis().OpenSession(); Criteria
	 * criteria = session.createCriteria(StoreQuotationM.class); Transaction tx =
	 * session.beginTransaction(); Serializable id = session.save(storeQuotationM);
	 * tx.commit(); session.flush(); session.clear(); if (id != null) { quotationMId
	 * = (long)id; } else { quotationMId = 0; } } catch (Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.getMessage();
	 * e.printStackTrace(); }finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return
	 * quotationMId; }
	 * 
	 * @Override public long saveStoreQuotationT(StoreQuotationT storeQuotationT) {
	 * // TODO Auto-generated method stub long quotationTId =0; try { Session
	 * session = getHibernateUtils.getHibernateUtlis().OpenSession(); Criteria
	 * criteria = session.createCriteria(StoreQuotationT.class); Transaction tx =
	 * session.beginTransaction(); Serializable id = session.save(storeQuotationT);
	 * tx.commit(); session.flush(); session.clear(); if (id != null) { quotationTId
	 * = (long)id; } else { quotationTId = 0; } } catch (Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.getMessage();
	 * e.printStackTrace(); }finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return
	 * quotationTId; }
	 * 
	 * @Override public Map<String, Object> getQuotationApprovalList(long pageNo,
	 * HashMap<String, String> requestData) { // TODO Auto-generated method stub
	 * Map<String, Object> map = new HashMap<String, Object>(); int pageSize =
	 * Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
	 * int pageNo1= 0; List totalMatches = new ArrayList(); if
	 * (requestData.get("PN") != null) pageNo1 =
	 * Integer.parseInt(requestData.get("PN").toString()); List<StoreQuotationM>
	 * storeQuotationMList = new ArrayList<StoreQuotationM>(); JSONObject json = new
	 * JSONObject(requestData);
	 * 
	 * Criteria cr=null; Date indentStratDate=null;
	 * 
	 * String status=json.get("flag").toString(); long hospitalId =
	 * Long.parseLong(json.getString("hospitalId").toString()); long departmentId =
	 * Long.parseLong(json.getString("departmentId").toString()); try { Session
	 * session = getHibernateUtils.getHibernateUtlis().OpenSession(); cr =
	 * session.createCriteria(StoreQuotationM.class) .createAlias("masHospital",
	 * "mh").createAlias("masDepartment", "md"); cr
	 * =cr.add(Restrictions.eq("mh.hospitalId",
	 * hospitalId)).add(Restrictions.eq("md.departmentId", departmentId))
	 * .add(Restrictions.eq("status",status).ignoreCase())
	 * .setProjection(Projections.projectionList()
	 * .add(Projections.groupProperty("storeBudgetaryM.budgetaryMId"))
	 * .add(Projections.groupProperty("quotationDate"),"quotationDate")
	 * .add(Projections.groupProperty("createdBy.userId"))
	 * .add(Projections.min("totalCost")));
	 * cr.addOrder(Order.desc("quotationDate"));
	 * 
	 * if(!cr.list().isEmpty() && cr.list().size()>0) {
	 * 
	 * totalMatches=cr.list(); cr.setFirstResult((pageSize) * (pageNo1 - 1));
	 * cr.setMaxResults(pageSize); storeQuotationMList= cr.list(); } } catch
	 * (Exception e) { getHibernateUtils.getHibernateUtlis().CloseConnection();
	 * e.getMessage(); e.printStackTrace(); } finally {
	 * 
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); }
	 * map.put("storeQuotationMList", storeQuotationMList); map.put("totalMatches",
	 * totalMatches.size()); return map; }
	 * 
	 * @Override public MasEmployee getNameByServiceNo(String serviceNo) { // TODO
	 * Auto-generated method stub List<MasEmployee> masEmployeeList =new
	 * ArrayList<MasEmployee>(); MasEmployee masEmployee=null; try {
	 * 
	 * Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * masEmployeeList =
	 * session.createCriteria(MasEmployee.class).add(Restrictions.eq("serviceNo",
	 * serviceNo).ignoreCase()).list();
	 * 
	 * if(masEmployeeList.size()>0) { masEmployee =masEmployeeList.get(0); } } catch
	 * (Exception e) { getHibernateUtils.getHibernateUtlis().CloseConnection();
	 * e.getMessage(); e.printStackTrace(); }finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return
	 * masEmployee; }
	 * 
	 * @Override public long saveMasStoreLpc(MasStoreLpc masStoreLpc) { // TODO
	 * Auto-generated method stub long masStoreLpcId =0; try { Session session =
	 * getHibernateUtils.getHibernateUtlis().OpenSession(); Transaction tx =
	 * session.beginTransaction(); Serializable id = session.save(masStoreLpc);
	 * tx.commit(); session.flush(); session.clear(); if (id != null) {
	 * masStoreLpcId = (long)id; } else { masStoreLpcId = 0; } } catch (Exception e)
	 * { getHibernateUtils.getHibernateUtlis().CloseConnection(); e.getMessage();
	 * e.printStackTrace(); }finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return
	 * masStoreLpcId; }
	 * 
	 * @Override public Map<String, Object> getCtmMemberList(long pageNo,
	 * HashMap<String, String> requestData,long hospitalId) { // TODO Auto-generated
	 * method stub Map<String, Object> map = new HashMap<String, Object>(); int
	 * pageSize = Integer.parseInt(HMSUtil.getProperties("adt.properties",
	 * "pageSize").trim()); int pageNo1= 0; List totalMatches = new ArrayList(); if
	 * (requestData.get("PN") != null) pageNo1 =
	 * Integer.parseInt(requestData.get("PN").toString()); List<MasStoreLpc>
	 * masStoreLpcList = new ArrayList<MasStoreLpc>(); JSONObject json = new
	 * JSONObject(requestData);
	 * 
	 * Criteria cr=null;
	 * 
	 * try { Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * cr = session.createCriteria(MasStoreLpc.class).createAlias("masHospital",
	 * "mh"); cr=cr.add(Restrictions.eq("mh.hospitalId", hospitalId));
	 * if(!cr.list().isEmpty() && cr.list().size()>0) {
	 * 
	 * totalMatches=cr.list(); cr.setFirstResult((pageSize) * (pageNo1 - 1));
	 * cr.setMaxResults(pageSize); masStoreLpcList= cr.list(); }
	 * 
	 * } catch (Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.getMessage();
	 * e.printStackTrace(); } finally {
	 * 
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); }
	 * map.put("masStoreLpcList", masStoreLpcList); map.put("totalMatches",
	 * totalMatches.size()); return map; }
	 * 
	 * @Override public MasEmployee getMasEmployee(Long memberId) { // TODO
	 * Auto-generated method stub MasEmployee masEmployee =null; List<MasEmployee>
	 * masEmployeeList =new ArrayList<MasEmployee>(); try { Session session =
	 * getHibernateUtils.getHibernateUtlis().OpenSession(); masEmployeeList =
	 * session.createCriteria(MasEmployee.class).add(Restrictions.eq("employeeId",
	 * memberId)).list();
	 * 
	 * if(masEmployeeList.size()>0) { masEmployee =masEmployeeList.get(0); }
	 * 
	 * }catch(Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.printStackTrace();
	 * }finally { getHibernateUtils.getHibernateUtlis().CloseConnection(); }
	 * 
	 * return masEmployee; }
	 * 
	 * @Override public MasStoreLpc getMasStoreLpc(Long lpcId) { // TODO
	 * Auto-generated method stub MasStoreLpc masStoreLpc =null; List<MasStoreLpc>
	 * masStoreLpcList =new ArrayList<MasStoreLpc>(); try { Session session =
	 * getHibernateUtils.getHibernateUtlis().OpenSession(); masStoreLpcList =
	 * session.createCriteria(MasStoreLpc.class).add(Restrictions.eq("lpcId",lpcId))
	 * .list();
	 * 
	 * if(masStoreLpcList.size()>0) { masStoreLpc =masStoreLpcList.get(0); }
	 * }catch(Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.printStackTrace();
	 * }finally { getHibernateUtils.getHibernateUtlis().CloseConnection(); }
	 * 
	 * return masStoreLpc; }
	 * 
	 * @Override public String checkCommitteeExist(Date fromDate, Date toDate,long
	 * hospitalId) { // TODO Auto-generated method stub String status ="0";
	 * MasStoreLpc masStoreLpc =null; List<MasStoreLpc> masStoreLpcList =new
	 * ArrayList<MasStoreLpc>(); try { Session session =
	 * getHibernateUtils.getHibernateUtlis().OpenSession(); Criterion
	 * c1=Restrictions.le("fromDate", fromDate); Criterion
	 * c2=Restrictions.ge("toDate", fromDate); LogicalExpression
	 * le1=Restrictions.and(c1, c2); Criteria criteria =
	 * session.createCriteria(MasStoreLpc.class).createAlias("masHospital", "mh");
	 * //masStoreLpcList=criteria.add(Restrictions.le("fromDate", fromDate))
	 * //.add(Restrictions.ge("toDate", fromDate))
	 * 
	 * masStoreLpcList=criteria.add(Restrictions.eq("mh.hospitalId", hospitalId))
	 * .add(Restrictions.eq("status", "Y").ignoreCase()).add(le1) .list();
	 * if(masStoreLpcList.size()==0) { Criteria criteria2 =
	 * session.createCriteria(MasStoreLpc.class).createAlias("masHospital", "mh");
	 * masStoreLpcList=criteria2.add(Restrictions.eq("mh.hospitalId", hospitalId))
	 * .add(Restrictions.le("fromDate", toDate)) .add(Restrictions.ge("toDate",
	 * toDate)) .add(Restrictions.eq("status", "Y").ignoreCase()).list(); }
	 * if(masStoreLpcList.size()>0) { status="1"; } }catch(Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); status="-1";
	 * e.printStackTrace(); }finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); }
	 * 
	 * return status; }
	 * 
	 * @Override public long inactivateCommittee(MasStoreLpc masStoreLpc) { // TODO
	 * Auto-generated method stub long lpcId =0; try { Session session =
	 * getHibernateUtils.getHibernateUtlis().OpenSession(); Transaction tx =
	 * session.beginTransaction(); session.update(masStoreLpc); tx.commit();
	 * lpcId=1; } catch (Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.getMessage();
	 * lpcId=0; e.printStackTrace(); }finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return lpcId; }
	 * 
	 * @SuppressWarnings("unchecked")
	 * 
	 * @Override public Map<String, Object> getItemList(HashMap<String, String>
	 * payload) {
	 * 
	 * Map<String, Object> map = new HashMap<String, Object>();
	 * List<StoreQuotationT> storeQuotationTList = new ArrayList<StoreQuotationT>();
	 * List<StoreQuotationM> storeQuotationMList = new ArrayList<StoreQuotationM>();
	 * long id = Long.parseLong(payload.get("budgetaryId")); try {
	 * 
	 * Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * storeQuotationTList = session.createCriteria(StoreQuotationT.class)
	 * .createAlias("storeQuotationM", "sqm") .createAlias("sqm.storeBudgetaryM",
	 * "sbm") .add(Restrictions.eq("sbm.budgetaryMId", id))
	 * .add(Restrictions.eq("sqm.status", "Y").ignoreCase())
	 * .addOrder(Order.asc("masStoreSupplier")).list();
	 * 
	 * storeQuotationMList =
	 * session.createCriteria(StoreQuotationM.class).createAlias("storeBudgetaryM",
	 * "sbm") .add(Restrictions.eq("sbm.budgetaryMId", id))
	 * .add(Restrictions.eq("status", "Y").ignoreCase())
	 * .addOrder(Order.asc("masStoreSupplier")).list();
	 * 
	 * 
	 * } catch (Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.getMessage();
	 * e.printStackTrace(); }finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); }
	 * map.put("storeQuotationTList", storeQuotationTList);
	 * map.put("storeQuotationMList", storeQuotationMList); return map; }
	 * 
	 * @Override public MasStoreLpc getCommitteeMembers(Date quotationDate,long
	 * hospitalId) { // TODO Auto-generated method stub List<MasStoreLpc>
	 * masStoreLpcList =new ArrayList<MasStoreLpc>(); MasStoreLpc masStoreLpc=null;
	 * try {
	 * 
	 * Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * masStoreLpcList =
	 * session.createCriteria(MasStoreLpc.class).createAlias("masHospital", "mh")
	 * .add(Restrictions.le("fromDate", quotationDate))
	 * .add(Restrictions.ge("toDate", quotationDate))
	 * .add(Restrictions.eq("mh.hospitalId", hospitalId))
	 * .add(Restrictions.eq("status", "Y").ignoreCase()).list();
	 * if(masStoreLpcList.size()>0) { masStoreLpc =masStoreLpcList.get(0); } } catch
	 * (Exception e) { getHibernateUtils.getHibernateUtlis().CloseConnection();
	 * e.getMessage(); e.printStackTrace(); }finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return
	 * masStoreLpc; }
	 * 
	 * @SuppressWarnings("unchecked")
	 * 
	 * @Override public Map<String, Object> getStoreQuotationM(long budId) { // TODO
	 * Auto-generated method stub Map<String, Object> map = new HashMap<String,
	 * Object>(); List<StoreQuotationM> storeQuotationMList = new
	 * ArrayList<StoreQuotationM>(); try {
	 * 
	 * Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * 
	 * storeQuotationMList =
	 * session.createCriteria(StoreQuotationM.class).createAlias("storeBudgetaryM",
	 * "sbm") .add(Restrictions.eq("sbm.budgetaryMId", budId))
	 * .add(Restrictions.eq("status", "Y").ignoreCase())
	 * .addOrder(Order.asc("masStoreSupplier")).list();
	 * 
	 * 
	 * } catch (Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.getMessage();
	 * e.printStackTrace(); }finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); }
	 * map.put("storeQuotationMList", storeQuotationMList); return map; }
	 * 
	 * @Override public int updateStoreQuotationM(StoreQuotationM storeQuotationM) {
	 * // TODO Auto-generated method stub int flag=0; try { Session session =
	 * getHibernateUtils.getHibernateUtlis().OpenSession(); Transaction tx =
	 * session.beginTransaction(); session.update(storeQuotationM); tx.commit();
	 * flag=1; } catch (Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.getMessage();
	 * flag=0; e.printStackTrace(); }finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return flag; }
	 * 
	 * @Override public Map<String, Object> getL1ItemList(HashMap<String, String>
	 * jsondata) { // TODO Auto-generated method stub Map<String, Object> map = new
	 * HashMap<String, Object>(); Criteria cr=null; List<StoreQuotationT>
	 * storeQuotationTList=null; JSONObject json = new JSONObject(jsondata); long
	 * storeQMId = Long.parseLong(json.getString("storeQMId")); try { Session
	 * session = getHibernateUtils.getHibernateUtlis().OpenSession(); cr =
	 * session.createCriteria(StoreQuotationT.class).createAlias("storeQuotationM",
	 * "sqm"); cr =cr.add(Restrictions.eq("sqm.quotationMId", storeQMId));
	 * if(!cr.list().isEmpty() && cr.list().size()>0) {
	 * 
	 * storeQuotationTList = cr.list(); } } catch (Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.getMessage();
	 * e.printStackTrace(); }finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); }
	 * map.put("storeQuotationTList", storeQuotationTList); map.put("status","1");
	 * return map; }
	 * 
	 * @Override public long saveStoreSoM(StoreSoM storeSoM) { // TODO
	 * Auto-generated method stub long storeSoMId =0; try { Session session =
	 * getHibernateUtils.getHibernateUtlis().OpenSession(); Criteria criteria =
	 * session.createCriteria(StoreSoM.class); Transaction tx =
	 * session.beginTransaction(); //Serializable id = session.save(storeSoM);
	 * session.saveOrUpdate(storeSoM); storeSoMId = storeSoM.getSoMId();
	 * tx.commit(); session.flush(); session.clear();
	 * 
	 * if (id != null) { storeSoMId = (long)id; } else { storeSoMId = 0; }
	 * 
	 * } catch (Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.getMessage();
	 * e.printStackTrace(); }finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return storeSoMId;
	 * }
	 * 
	 * @Override public long saveStoreSoT(StoreSoT storeSoT) { // TODO
	 * Auto-generated method stub long storeSoTId =0; try { Session session =
	 * getHibernateUtils.getHibernateUtlis().OpenSession(); Criteria criteria =
	 * session.createCriteria(StoreSoT.class); Transaction tx =
	 * session.beginTransaction(); Serializable id = session.save(storeSoT);
	 * tx.commit(); session.flush(); session.clear(); if (id != null) { storeSoTId =
	 * (long)id; } else { storeSoTId = 0; } } catch (Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.getMessage();
	 * e.printStackTrace(); }finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return storeSoTId;
	 * }
	 * 
	 * @Override public Map<String, Object> getSanctionApprovalList(long pageNo,
	 * HashMap<String, String> requestData) { // TODO Auto-generated method stub
	 * Map<String, Object> map = new HashMap<String, Object>(); int pageSize =
	 * Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
	 * int pageNo1= 0; List totalMatches = new ArrayList(); if
	 * (requestData.get("PN") != null) pageNo1 =
	 * Integer.parseInt(requestData.get("PN").toString()); List<StoreSoM>
	 * storeSoMList = new ArrayList<StoreSoM>(); JSONObject json = new
	 * JSONObject(requestData);
	 * 
	 * Criteria cr=null;
	 * 
	 * String status=json.get("flag").toString(); long hospitalId =
	 * Long.parseLong(json.getString("hospitalId").toString()); long departmentId =
	 * Long.parseLong(json.getString("departmentId").toString());
	 * 
	 * try { Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * cr = session.createCriteria(StoreSoM.class).createAlias("masHospital",
	 * "mh").createAlias("masDepartment", "md"); cr
	 * =cr.add(Restrictions.eq("mh.hospitalId",
	 * hospitalId)).add(Restrictions.eq("md.departmentId", departmentId));
	 * cr.addOrder(Order.desc("orderDate")); if(!status.equalsIgnoreCase("ALL"))
	 * cr=cr.add(Restrictions.eq("status", status).ignoreCase());
	 * 
	 * if(!cr.list().isEmpty() && cr.list().size()>0) {
	 * 
	 * totalMatches=cr.list(); cr.setFirstResult((pageSize) * (pageNo1 - 1));
	 * cr.setMaxResults(pageSize); storeSoMList= cr.list(); } } catch (Exception e)
	 * { getHibernateUtils.getHibernateUtlis().CloseConnection(); e.getMessage();
	 * e.printStackTrace(); } finally {
	 * 
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); }
	 * map.put("storeSoMList", storeSoMList); map.put("totalMatches",
	 * totalMatches.size()); return map; }
	 * 
	 * @Override public Map<String, Object> getSanctionData(HashMap<String, String>
	 * jsondata) { // TODO Auto-generated method stub
	 * 
	 * Map<String, Object> map = new HashMap<String, Object>(); Criteria cr=null;
	 * List<StoreSoM> storeSoMList=null; JSONObject json = new JSONObject(jsondata);
	 * long storeSoMId = Long.parseLong(json.getString("storeSoMId")); try { Session
	 * session = getHibernateUtils.getHibernateUtlis().OpenSession(); cr =
	 * session.createCriteria(StoreSoM.class).add(Restrictions.eq("soMId",
	 * storeSoMId)); if(!cr.list().isEmpty() && cr.list().size()>0) {
	 * 
	 * storeSoMList = cr.list(); } } catch (Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.getMessage();
	 * e.printStackTrace(); }finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); }
	 * map.put("storeSoMList", storeSoMList); map.put("status","1"); return map;
	 * 
	 * }
	 * 
	 * @Override public StoreSoM getStoreSoM(long storeSoMId) { // TODO
	 * Auto-generated method stub StoreSoM storeSoM=null; List<StoreSoM>
	 * storeSoMList =new ArrayList<StoreSoM>(); try { Session session =
	 * getHibernateUtils.getHibernateUtlis().OpenSession(); storeSoMList =
	 * session.createCriteria(StoreSoM.class).add(Restrictions.eq("soMId",storeSoMId
	 * )).list();
	 * 
	 * if(storeSoMList.size()>0) { storeSoM =storeSoMList.get(0); } }catch(Exception
	 * e) { getHibernateUtils.getHibernateUtlis().CloseConnection();
	 * e.printStackTrace(); }finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return storeSoM; }
	 * 
	 * @Override public int updateStoreSoM(StoreSoM storeSoM) { // TODO
	 * Auto-generated method stub int flag=0; try { Session session =
	 * getHibernateUtils.getHibernateUtlis().OpenSession(); Transaction tx =
	 * session.beginTransaction(); session.update(storeSoM); tx.commit(); flag=1; }
	 * catch (Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.getMessage();
	 * flag=0; e.printStackTrace(); }finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return flag; }
	 * 
	 * @SuppressWarnings("unchecked")
	 * 
	 * @Override public StorePoT getLpRate(String itemId,String hospitalId,String
	 * departmentId) {
	 * 
	 * Map<String, Object> map = new HashMap<String, Object>();
	 * List<StorePoT>listStorePoT=null; StorePoT storePoT=null; int flag=0; try {
	 * Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * Transaction tx = session.beginTransaction();
	 * 
	 * Criteria criteria =
	 * session.createCriteria(StorePoT.class,"st").createAlias("storePoM", "spm")
	 * .add(Restrictions.eq("masStoreItem.itemId", Long.parseLong(itemId)))
	 * .add(Restrictions.eq("spm.masHospital.hospitalId",
	 * Long.parseLong(hospitalId)))
	 * .add(Restrictions.eq("spm.masDepartment.departmentId",
	 * Long.parseLong(departmentId)));
	 * criteria=criteria.addOrder(Order.desc("poTId")).setFirstResult(0).
	 * setMaxResults(1);
	 * 
	 * listStorePoT =criteria.list(); if(listStorePoT.size()>0) { storePoT
	 * =listStorePoT.get(0); }
	 * 
	 * tx.commit();
	 * 
	 * } catch (Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.getMessage();
	 * e.printStackTrace(); }
	 * 
	 * return storePoT; }
	 * 
	 * @Override public MasStoreLpType getMasStoreLpType(long approxCost) { // TODO
	 * Auto-generated method stub MasStoreLpType masStoreLpType=null;
	 * List<MasStoreLpType> masStoreLpTypeList =new ArrayList<MasStoreLpType>(); try
	 * { Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * Criteria cr = session.createCriteria(MasStoreLpType.class); Criterion
	 * cr1=Restrictions.ge("tolpAmount", approxCost); Criterion
	 * cr2=Restrictions.le("fromlpAmount", approxCost); LogicalExpression
	 * lg=Restrictions.and(cr1, cr2); .add(Restrictions.gt("fromlpAmount",
	 * approxCost)) .add(Restrictions.le("tolpAmount", approxCost))
	 * masStoreLpTypeList=cr.add(lg) .list();
	 * 
	 * if(masStoreLpTypeList.size()>0) { masStoreLpType =masStoreLpTypeList.get(0);
	 * } }catch(Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.printStackTrace();
	 * }finally { getHibernateUtils.getHibernateUtlis().CloseConnection(); }
	 * 
	 * return masStoreLpType; }
	 * 
	 * @Override public List<MasStoreFinancial> financialYearList() { // TODO
	 * Auto-generated method stub List<MasStoreFinancial> masStoreFinancialList =
	 * new ArrayList<MasStoreFinancial>(); Session session =
	 * getHibernateUtils.getHibernateUtlis().OpenSession(); try {
	 * masStoreFinancialList = session.createCriteria(MasStoreFinancial.class)
	 * .addOrder(Order.asc("financialYear")).list(); }catch (Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.getMessage();
	 * e.printStackTrace(); }finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return
	 * masStoreFinancialList; }
	 * 
	 * @Override public long saveFundUtilization(FundUtilization fundUtilization) {
	 * // TODO Auto-generated method stub long fuId =0; try { Session session =
	 * getHibernateUtils.getHibernateUtlis().OpenSession(); Criteria criteria =
	 * session.createCriteria(FundUtilization.class); Transaction tx =
	 * session.beginTransaction(); Serializable id = session.save(fundUtilization);
	 * tx.commit(); session.flush(); session.clear(); if (id != null) { fuId =
	 * (long)id; } else { fuId = 0; } } catch (Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.getMessage();
	 * e.printStackTrace(); }finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return fuId; }
	 * 
	 * @Override public Map<String, Object> fundUtilizationList(long pageNo,
	 * HashMap<String, String> requestData,long hospitalId, long departmentId) { //
	 * TODO Auto-generated method stub Map<String, Object> map = new HashMap<String,
	 * Object>(); int pageSize =
	 * Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
	 * int pageNo1= 0; List totalMatches = new ArrayList(); if
	 * (requestData.get("PN") != null) pageNo1 =
	 * Integer.parseInt(requestData.get("PN").toString()); List<FundUtilization>
	 * fundUtilizationList = new ArrayList<FundUtilization>(); JSONObject json = new
	 * JSONObject(requestData);
	 * 
	 * Criteria cr=null;
	 * 
	 * try { Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * cr = session.createCriteria(FundUtilization.class)
	 * .createAlias("masHospital", "mh").createAlias("masDepartment", "md");
	 * cr=cr.add(Restrictions.eq("mh.hospitalId",
	 * hospitalId)).add(Restrictions.eq("md.departmentId", departmentId));
	 * if(!cr.list().isEmpty() && cr.list().size()>0) {
	 * 
	 * totalMatches=cr.list(); cr.setFirstResult((pageSize) * (pageNo1 - 1));
	 * cr.setMaxResults(pageSize); fundUtilizationList= cr.list(); }
	 * 
	 * } catch (Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.getMessage();
	 * e.printStackTrace(); } finally {
	 * 
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); }
	 * map.put("fundUtilizationList", fundUtilizationList); map.put("totalMatches",
	 * totalMatches.size()); return map; }
	 * 
	 * @SuppressWarnings("unchecked")
	 * 
	 * @Override public String checkAuthorityNoExist(String authorityNo, long
	 * hospitalId, long departmentId) { // TODO Auto-generated method stub String
	 * status ="0"; FundUtilization fundUtilization =null; List<FundUtilization>
	 * FundUtilizationList =new ArrayList<FundUtilization>(); try { Session session
	 * = getHibernateUtils.getHibernateUtlis().OpenSession(); Criteria criteria =
	 * session.createCriteria(FundUtilization.class) .createAlias("masHospital",
	 * "mh").createAlias("masDepartment", "md");
	 * FundUtilizationList=criteria.add(Restrictions.eq("mh.hospitalId",
	 * hospitalId)) .add(Restrictions.eq("md.departmentId", departmentId))
	 * .add(Restrictions.eq("authorityNo", authorityNo).ignoreCase()) .list();
	 * 
	 * if(FundUtilizationList.size()>0) { status="1"; } }catch(Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); status="-1";
	 * e.printStackTrace(); }finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); }
	 * 
	 * return status; }
	 * 
	 * @SuppressWarnings("unchecked")
	 * 
	 * @Override public Map<String, Object> getFundUtilizationInShownYear(long
	 * hospitalId, long departmentId, long yearId,int pageNo) { List<HashMap<String,
	 * Object>> utilizedFundData = new ArrayList<HashMap<String, Object>>();
	 * Map<String, Object> map = new HashMap<String, Object>(); List<Integer>
	 * totalMatches = new ArrayList<Integer>(); List<StorePoM> poMDataList = new
	 * ArrayList<StorePoM>(); int pageSize =
	 * Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
	 * BigDecimal sum = null; try { Session session =
	 * getHibernateUtils.getHibernateUtlis().OpenSession(); Criteria cr =
	 * session.createCriteria(StorePoM.class)
	 * .add(Restrictions.eq("masHospital.hospitalId", hospitalId))
	 * .add(Restrictions.eq("masDepartment.departmentId", departmentId))
	 * .add(Restrictions.eq("masStoreFinancial.financialId", yearId));
	 * 
	 * 
	 * totalMatches = cr.list(); cr.setFirstResult((pageSize) * (pageNo - 1));
	 * cr.setMaxResults(pageSize); poMDataList = cr.list();
	 * 
	 * if (poMDataList.size() > 0) { for (StorePoM poMData : poMDataList) {
	 * 
	 * sum = (BigDecimal)
	 * session.createCriteria(StorePoT.class).setProjection(Projections.sum("amount"
	 * )) .add(Restrictions.eq("storePoM.poMId",
	 * poMData.getPoMId())).uniqueResult();
	 * 
	 * HashMap<String, Object> poMFundMap = new HashMap<String, Object>();
	 * poMFundMap.put("poMId", poMData.getPoMId()); poMFundMap.put("poNumber",
	 * poMData.getPoNumber()); poMFundMap.put("poDate",
	 * HMSUtil.changeDateToddMMyyyy(poMData.getPoDate())); poMFundMap.put("sum",
	 * sum); utilizedFundData.add(poMFundMap); } map.put("utilizedFundData",
	 * utilizedFundData); map.put("count", totalMatches.size()); map.put("status",
	 * 1);
	 * 
	 * } else { map.put("utilizedFundData", utilizedFundData); map.put("count",
	 * totalMatches.size()); map.put("status", 0); }
	 * 
	 * } catch (Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.printStackTrace();
	 * } finally { getHibernateUtils.getHibernateUtlis().CloseConnection(); } return
	 * map; }
	 * 
	 * @SuppressWarnings("unused")
	 * 
	 * @Override public long saveBudgetary(HashMap<String, Object> jsondata) { //
	 * TODO Auto-generated method stub StoreBudgetaryM storeBudgetaryM = new
	 * StoreBudgetaryM(); MasHospital masHospital = null; MasDepartment
	 * masDepartment = null; Transaction tx =null; long budId=0; long budTId=0;
	 * 
	 * 
	 * if(!jsondata.get("lpDate").toString().isEmpty()) { String lpDate =
	 * jsondata.get("lpDate").toString(); String lpDate1 =
	 * dispenceryService.getReplaceString(lpDate); Date lpDate2 =
	 * HMSUtil.convertStringDateToUtilDate(lpDate1, "dd/MM/yyyy");
	 * storeBudgetaryM.setReqDate(new Timestamp(lpDate2.getTime())); }
	 * 
	 * if (jsondata.get("departmentId") != null) { String departmentId =
	 * jsondata.get("departmentId").toString(); masDepartment =
	 * dispensaryDao.getMasDepartment(Long.parseLong(departmentId)); } if
	 * (jsondata.get("hospitalId") != null) { String hospitalId =
	 * jsondata.get("hospitalId").toString(); masHospital =
	 * dispensaryDao.gettoMasHospital(Long.parseLong(hospitalId)); }
	 * 
	 * String approxCost = jsondata.get("approxCost").toString(); String approxCost1
	 * = dispenceryService.getReplaceString(approxCost); String[] approxCostValue =
	 * approxCost1.split(",");
	 * 
	 * 
	 * String nomenclature = jsondata.get("nomenclature").toString(); String
	 * nomenclature1 = dispenceryService.getReplaceString(nomenclature); String[]
	 * nomenclatureValue = nomenclature1.split(",");
	 * 
	 * String requiredQty = jsondata.get("requiredQty").toString(); String
	 * requiredQty1 = dispenceryService.getReplaceString(requiredQty); String[]
	 * requiredQtyValue = requiredQty1.split(","); int itemLength =
	 * requiredQtyValue.length;
	 * 
	 * String accountingUnit1 = jsondata.get("accountingUnit1").toString(); String
	 * accountingUnit = dispenceryService.getReplaceString(accountingUnit1);
	 * String[] accountingUnitValue = accountingUnit.split(",");
	 * 
	 * String lpunitrate = jsondata.get("lpunitrate").toString(); String lpunitrate1
	 * = dispenceryService.getReplaceString(lpunitrate); String[] lpunitrateValue =
	 * lpunitrate1.split(",");
	 * 
	 * String itemIdNom = jsondata.get("itemIdNom").toString(); String itemIdNom1 =
	 * dispenceryService.getReplaceString(itemIdNom); String[] itemIdNomValue =
	 * itemIdNom1.split(",");
	 * 
	 * String pvmsNo1 = jsondata.get("pvmsNo1").toString(); String pvmsNo =
	 * dispenceryService.getReplaceString(pvmsNo1); String[] pvmsNo1Value =
	 * pvmsNo.split(",");
	 * 
	 * Users users=null; MasStoreItem masStoreItem=null;
	 * 
	 * //save data in table if(jsondata.get("userId")!=null) { String userId =
	 * jsondata.get("userId").toString();
	 * users=dispensaryDao.getUser(Long.parseLong(userId)); } Session session1 =
	 * getHibernateUtils.getHibernateUtlis().OpenSession(); tx =
	 * session1.beginTransaction(); try{ storeBudgetaryM.setReqBY(users);
	 * storeBudgetaryM.setMasDepartment(masDepartment);
	 * storeBudgetaryM.setMasHospital(masHospital);
	 * storeBudgetaryM.setStatus("P");//p=pending storeBudgetaryM.setApproxCost(new
	 * BigDecimal(approxCostValue[0])); if(session1 == null) {
	 * System.out.println("session is null"); } budId = (long)
	 * session1.save(storeBudgetaryM); // long budgetaryMId=
	 * lPProcessDao.saveLPBudgetary(storeBudgetaryM);
	 * 
	 * for (int i = 0; i < itemLength; i++) { StoreBudgetaryT storeBudgetaryT=new
	 * StoreBudgetaryT(); MasStoreItem masStoreItem1=new MasStoreItem();
	 * if(requiredQtyValue[i]!=null && !requiredQtyValue[i].isEmpty())
	 * storeBudgetaryT.setQtyRequried(Long.parseLong(requiredQtyValue[i].trim()));
	 * 
	 * 
	 * 
	 * if(itemIdNomValue[i]!=null && !itemIdNomValue[i].isEmpty()) {
	 * masStoreItem1.setItemId(Long.parseLong(itemIdNomValue[i].trim()));
	 * //masStoreItem=dispensaryDao.getMasStoreItem(Long.parseLong(itemIdNomValue[i]
	 * .trim())); storeBudgetaryT.setMasStoreItem(masStoreItem1); }
	 * if(lpunitrateValue[i]!=null && !lpunitrateValue[i].isEmpty())
	 * storeBudgetaryT.setLastLpRate(new BigDecimal(lpunitrateValue[i].trim()));
	 * 
	 * 
	 * storeBudgetaryT.setStoreBudgetaryM(storeBudgetaryM);//need to chk budTId =
	 * (long) session1.save(storeBudgetaryT); //long budgetaryMId=
	 * lPProcessDao.savestoreBudgetaryT(storeBudgetaryT); } tx.commit();
	 * session1.flush(); session1.clear(); }
	 * 
	 * catch(Exception e) { if (tx != null) { try { tx.rollback(); budId=-1; }
	 * catch(Exception re) { budId=-1; re.printStackTrace(); } } budId=-1;
	 * e.printStackTrace(); }
	 * 
	 * finally {
	 * 
	 * getHibernateUtils.getHibernateUtlis().CloseConnection();
	 * 
	 * }
	 * 
	 * return budId; }
	 * 
	 * @Override public StoreSoM getSanctionData(long storeQMId) { // TODO
	 * Auto-generated method stub StoreSoM storeSoM =null; List<StoreSoM>
	 * storeSoMList =new ArrayList<StoreSoM>(); try { Session session =
	 * getHibernateUtils.getHibernateUtlis().OpenSession(); storeSoMList =
	 * session.createCriteria(StoreSoM.class).createAlias("storeQuotationM", "sq")
	 * .add(Restrictions.eq("sq.quotationMId",storeQMId)).list();
	 * 
	 * if(storeSoMList.size()>0) { storeSoM =storeSoMList.get(0); } }catch(Exception
	 * e) { getHibernateUtils.getHibernateUtlis().CloseConnection();
	 * e.printStackTrace(); }finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); }
	 * 
	 * return storeSoM; }
	 * 
	 * @SuppressWarnings("unused")
	 * 
	 * @Override public long saveBudgetaryQuotation(HashMap<String, Object>
	 * jsondata) { // TODO Auto-generated method stub JSONObject json = new
	 * JSONObject(jsondata); Date qutotationDate=null; String lpTypeFlag="";
	 * Transaction tx =null; if (jsondata != null) {
	 * if(!jsondata.get("reqDate").toString().isEmpty()) { String reqDate =
	 * jsondata.get("reqDate").toString(); String reqDate1 =
	 * dispenceryService.getReplaceString(reqDate); qutotationDate =
	 * HMSUtil.convertStringDateToUtilDate(reqDate1, "dd/MM/yyyy");
	 * 
	 * } MasHospital masHospital = null; MasDepartment masDepartment = null;
	 * 
	 * if (jsondata.get("departmentId") != null) { String departmentId =
	 * jsondata.get("departmentId").toString(); masDepartment =
	 * dispensaryDao.getMasDepartment(Long.parseLong(departmentId)); } if
	 * (jsondata.get("hospitalId") != null) { String hospitalId =
	 * jsondata.get("hospitalId").toString(); masHospital =
	 * dispensaryDao.gettoMasHospital(Long.parseLong(hospitalId)); }
	 * 
	 * 
	 * String reqNo = jsondata.get("reqNo").toString(); String reqNo1 =
	 * dispenceryService.getReplaceString(reqNo); String[] reqNoValue =
	 * reqNo1.split(",");
	 * 
	 * 
	 * 
	 * String quotqtionNo = jsondata.get("quotqtionNo").toString(); String
	 * quotqtionNo1 = dispenceryService.getReplaceString(quotqtionNo); String[]
	 * quotqtionNoValue = quotqtionNo1.split(",");
	 * 
	 * 
	 * String vendor = jsondata.get("vendors").toString(); String vendor1 =
	 * dispenceryService.getReplaceString(vendor); String[] vendorValue =
	 * vendor1.split(","); int vendorLength = vendorValue.length;
	 * 
	 * //ridc id code String[] ridcIdValue=null; if
	 * (!jsondata.get("ridcId").toString().isEmpty() && jsondata.get("ridcId") !=
	 * null) { String ridcId = jsondata.get("ridcId").toString(); ridcIdValue =
	 * ridcId.split(","); int ridcIdLength = ridcIdValue.length; } //End Ridc code
	 * 
	 * String budgetaryMId = jsondata.get("budgetaryMId1").toString(); String
	 * budgetaryMId1 = dispenceryService.getReplaceString(budgetaryMId); String[]
	 * budgetaryMId1Value = budgetaryMId1.split(",");
	 * 
	 * String lowvendor = jsondata.get("vendor").toString(); String lowvendor1 =
	 * dispenceryService.getReplaceString(lowvendor); String[] lowvendorValue =
	 * lowvendor1.split(",");
	 * 
	 * 
	 * String btnvalue = jsondata.get("btnvalue").toString(); String btnvalue1 =
	 * dispenceryService.getReplaceString(btnvalue); String[] btnValue =
	 * btnvalue1.split(","); String butnValue=""; butnValue=btnValue[0];
	 * 
	 * 
	 * String unitRate = jsondata.get("unitRate").toString(); String unitRate1 =
	 * dispenceryService.getReplaceString(unitRate); String[] unitRateValue =
	 * unitRate1.split(",");
	 * 
	 * String totalCost = jsondata.get("totalCost").toString(); String totalCost1 =
	 * dispenceryService.getReplaceString(totalCost); String[] totalCostValue =
	 * totalCost1.split(",");
	 * 
	 * String totalCostQ = jsondata.get("totalCostQ").toString(); String totalCostQ1
	 * = dispenceryService.getReplaceString(totalCostQ); String[] totalCostQValue =
	 * totalCostQ1.split(",");
	 * 
	 * 
	 * String nomenclature = jsondata.get("nomenclature").toString(); String
	 * nomenclature1 = dispenceryService.getReplaceString(nomenclature); String[]
	 * nomenclatureValue = nomenclature1.split(",");
	 * 
	 * String requiredQty = jsondata.get("requiredQty").toString(); String
	 * requiredQty1 = dispenceryService.getReplaceString(requiredQty); String[]
	 * requiredQtyValue = requiredQty1.split(","); int itemLength =
	 * requiredQtyValue.length;
	 * 
	 * String accountingUnit1 = jsondata.get("accountingUnit1").toString(); String
	 * accountingUnit = dispenceryService.getReplaceString(accountingUnit1);
	 * String[] accountingUnitValue = accountingUnit.split(",");
	 * 
	 * String lpunitrate = jsondata.get("lpunitrate").toString(); String lpunitrate1
	 * = dispenceryService.getReplaceString(lpunitrate); String[] lpunitrateValue =
	 * lpunitrate1.split(",");
	 * 
	 * String itemIdNom = jsondata.get("itemIdNom").toString(); String itemIdNom1 =
	 * dispenceryService.getReplaceString(itemIdNom); String[] itemIdNomValue =
	 * itemIdNom1.split(",");
	 * 
	 * String pvmsNo1 = jsondata.get("pvmsNo1").toString(); String pvmsNo =
	 * dispenceryService.getReplaceString(pvmsNo1); String[] pvmsNo1Value =
	 * pvmsNo.split(",");
	 * 
	 * Users users=null; //MasStoreItem masStoreItem=null;
	 * 
	 * //save data in table if(jsondata.get("userId")!=null) { String userId =
	 * jsondata.get("userId").toString();
	 * users=dispensaryDao.getUser(Long.parseLong(userId)); }
	 * 
	 * StoreBudgetaryM
	 * storeBudgetaryM=getStoreBudgetaryM(Long.parseLong(budgetaryMId1Value[0]));
	 * 
	 * if(storeBudgetaryM.getLpTypeFlag()!=null &&
	 * storeBudgetaryM.getLpTypeFlag().equalsIgnoreCase("B")) { lpTypeFlag="B"; }
	 * 
	 * //update status in StoreBudgetaryM if(butnValue.equalsIgnoreCase("Save"))
	 * storeBudgetaryM.setStatus("N");//n=save,y=submit else
	 * if(butnValue.equalsIgnoreCase("Submit")) storeBudgetaryM.setStatus("Y");
	 * //code for LP cash and carry long lpCode =
	 * Long.parseLong(HMSUtil.getProperties("adt.properties",
	 * "LP_CASH_AND_CARRY_CODE").trim()); long quotationMId=0; long quotMId=0; long
	 * min=0;
	 * 
	 * Session session1 = getHibernateUtils.getHibernateUtlis().OpenSession(); tx =
	 * session1.beginTransaction(); try{
	 * 
	 * if(storeBudgetaryM.getMasStoreLpType().getLpCode()==lpCode)
	 * storeBudgetaryM.setStatus("M"); //end
	 * ///lPProcessDao.updateStoreBudgetaryM(storeBudgetaryM);
	 * session1.update(storeBudgetaryM);
	 * 
	 * if(totalCostQValue[0]!=null && !totalCostQValue[0].isEmpty()) {
	 * min=Long.parseLong(totalCostQValue[0]); } int n=0; for (int i = 0; i <
	 * vendorLength; i++) { StoreQuotationM storeQuotationM = new StoreQuotationM();
	 * MasStoreSupplier masStoreSupplier=new MasStoreSupplier();
	 * storeQuotationM.setQuotationDate(new Timestamp(qutotationDate.getTime()));
	 * storeQuotationM.setCreatedDate(new Timestamp(qutotationDate.getTime()));
	 * storeQuotationM.setCreatedBy(users);
	 * storeQuotationM.setMasDepartment(masDepartment);
	 * storeQuotationM.setMasHospital(masHospital);
	 * storeQuotationM.setStoreBudgetaryM(storeBudgetaryM);
	 * 
	 * if(!lpTypeFlag.isEmpty() && lpTypeFlag.equalsIgnoreCase("B")) {
	 * storeQuotationM.setLpTypeFlag("B"); // need to modify quotation date and
	 * created date }else { // need to modify quotation date and created date }
	 * 
	 * if(butnValue.equalsIgnoreCase("Save"))
	 * 
	 * storeQuotationM.setStatus("N");//n=save,y=submit else
	 * if(butnValue.equalsIgnoreCase("Submit"))
	 * 
	 * storeQuotationM.setStatus("Y");//n=save,y=submit
	 * 
	 * if(quotqtionNoValue[i]!=null && !quotqtionNoValue[i].isEmpty())
	 * storeQuotationM.setQuotationNo(quotqtionNoValue[i]);
	 * 
	 * if(vendorValue[i]!=null && !vendorValue[i].isEmpty()) {
	 * masStoreSupplier.setSupplierId(Long.parseLong(vendorValue[i].trim()));
	 * //masStoreSupplier=lPProcessDao.getMasStoreSupplier(Long.parseLong(
	 * vendorValue[i].trim()));
	 * storeQuotationM.setMasStoreSupplier(masStoreSupplier); //lp cash and carry
	 * if(storeBudgetaryM.getMasStoreLpType().getLpCode()==lpCode) {
	 * if(Long.parseLong(vendorValue[i].trim())==Long.parseLong(lowvendorValue[0]))
	 * { storeQuotationM.setL1Status("Y"); } else storeQuotationM.setL1Status("N");
	 * storeQuotationM.setStatus("M"); storeQuotationM.setApprovedBy(users);
	 * storeQuotationM.setApprovedDate(new Timestamp(new Date().getTime())); //end }
	 * }
	 * 
	 * if(totalCostQValue[i]!=null && !totalCostQValue[i].isEmpty())
	 * storeQuotationM.setTotalCost(new BigDecimal(totalCostQValue[i].trim()));
	 * //save ridcid if (!jsondata.get("ridcId").toString().isEmpty() &&
	 * jsondata.get("ridcId") != null) {
	 * 
	 * if(ridcIdValue[i]!=null && !ridcIdValue[i].isEmpty()) {
	 * storeQuotationM.setRidcId(Long.parseLong(ridcIdValue[i])); } } //end ridcid
	 * 
	 * quotationMId= (long) session1.save(storeQuotationM);
	 * if(totalCostQValue[i]!=null && !totalCostQValue[i].isEmpty()) { if(min >=
	 * Long.parseLong(totalCostQValue[i].trim())) {
	 * min=Long.parseLong(totalCostQValue[i].trim()); quotMId=quotationMId; } } n=i;
	 * for (int j = 0; j < itemLength;j++) { StoreQuotationT storeQuotationT = new
	 * StoreQuotationT(); MasStoreItem masStoreItem= new MasStoreItem();
	 * if(totalCostValue[n]!=null && !totalCostValue[n].isEmpty())
	 * storeQuotationT.setTotalCost(new BigDecimal(totalCostValue[n].trim()));
	 * storeQuotationT.setStoreQuotationM(storeQuotationM);
	 * storeQuotationT.setMasStoreSupplier(masStoreSupplier);
	 * 
	 * if(itemIdNomValue[j]!=null && !itemIdNomValue[j].isEmpty()) {
	 * masStoreItem.setItemId(Long.parseLong(itemIdNomValue[j].trim()));
	 * //masStoreItem=dispensaryDao.getMasStoreItem(Long.parseLong(itemIdNomValue[j]
	 * .trim())); storeQuotationT.setMasStoreItem(masStoreItem); }
	 * if(unitRateValue[n]!=null && !unitRateValue[n].isEmpty())
	 * storeQuotationT.setUnitRate(new BigDecimal(unitRateValue[n].trim()));
	 * 
	 * if(requiredQtyValue[j]!=null && !requiredQtyValue[j].isEmpty())
	 * storeQuotationT.setQtyRequried(Long.parseLong(requiredQtyValue[j].trim()));
	 * 
	 * if(!lpTypeFlag.isEmpty() && lpTypeFlag.equalsIgnoreCase("B")) { long tempId =
	 * getTempIdFromBudgetarty(storeBudgetaryM,Long.parseLong(itemIdNomValue[j].trim
	 * ())); TempDirectReceivingForBackLp tempObjectBackLp = new
	 * TempDirectReceivingForBackLp(); tempObjectBackLp.setTempId(tempId);
	 * storeQuotationT.setTempDirectReceivingForBackLp(tempObjectBackLp);
	 * 
	 * updateTempTableStatus(session1,tempId,"quotation"); }
	 * 
	 * //long quotationTId= lPProcessDao.saveStoreQuotationT(storeQuotationT); long
	 * quotationTId= (long) session1.save(storeQuotationT); n=n+vendorLength; } }
	 * tx.commit(); session1.flush(); session1.clear(); }
	 * 
	 * catch (Exception e) { if (tx != null) { try { tx.rollback(); quotMId = -1; }
	 * catch (Exception re) { quotMId = -1; re.printStackTrace(); } } quotMId = -1;
	 * e.printStackTrace(); }
	 * 
	 * finally {
	 * 
	 * getHibernateUtils.getHibernateUtlis().CloseConnection();
	 * 
	 * }
	 * 
	 * return quotMId; } return -1; }
	 * 
	 * @Override public void updateTempTableStatus(Session sessionP, long tempId,
	 * String value) { TempDirectReceivingForBackLp tempTblObject=null; Session
	 * session=null; if(sessionP!=null) { session = sessionP; }else { session =
	 * getHibernateUtils.getHibernateUtlis().OpenSession(); } tempTblObject =
	 * (TempDirectReceivingForBackLp)
	 * session.get(TempDirectReceivingForBackLp.class, tempId);
	 * if(tempTblObject!=null) { if(value.equalsIgnoreCase("quotation")) {
	 * tempTblObject.setQuotationStatus("Y"); }
	 * if(value.equalsIgnoreCase("sanction")) {
	 * tempTblObject.setSanctionOrderStatus("Y"); }
	 * 
	 * if(value.equalsIgnoreCase("supply")) {
	 * tempTblObject.setSupplyOrderStatus("Y"); }
	 * 
	 * if(value.equalsIgnoreCase("grn")) { tempTblObject.setGrnStatus("Y"); }
	 * 
	 * session.update(tempTblObject); }
	 * 
	 * }
	 * 
	 * @SuppressWarnings("unchecked")
	 * 
	 * @Override public long getTempIdFromBudgetarty(StoreBudgetaryM
	 * storeBudgetaryM, long itemId) { List<StoreBudgetaryT> budList = new
	 * ArrayList<StoreBudgetaryT>(); long tempId = 0; Session session =
	 * getHibernateUtils.getHibernateUtlis().OpenSession(); try { budList =
	 * session.createCriteria(StoreBudgetaryT.class)
	 * .add(Restrictions.eq("storeBudgetaryM", storeBudgetaryM))
	 * .add(Restrictions.eq("masStoreItem1.itemId", itemId)).list();
	 * 
	 * tempId = budList.get(0).getTempDirectReceivingForBackLp().getTempId(); }
	 * catch (Exception e) { e.printStackTrace(); }
	 * 
	 * return tempId; }
	 * 
	 * @SuppressWarnings("static-access")
	 * 
	 * @Override public long submitSanctionOrder(HashMap<String, Object> jsondata) {
	 * // TODO Auto-generated method stub JSONObject jsonObj = new JSONObject();
	 * JSONObject json = new JSONObject(jsondata); Transaction tx =null; long
	 * storeSoMId=0; MasHospital masHospital = null; MasDepartment masDepartment =
	 * null; String reqNo1 =""; String quotationNo1=""; String fileNo1=""; String
	 * orderDate=""; String vendorName1=""; String orderPurpose=""; String
	 * quantumItem1=""; String orderValue=""; String refAuthority=""; String
	 * issuedUnder=""; String payingAgency1=""; String bookedBudget=""; String
	 * videNoteNumber=""; String uoNumber1=""; String commSanction1=""; String
	 * commWithSanction=""; String l1Cost1=""; String l1CostWord1=""; String
	 * vendorId1=""; String budgetaryId1=""; String[] storeQuotationTIdValue=null;
	 * String btnvalue=""; String btnvalue1=""; long sanctionId=0; if (jsondata !=
	 * null) {
	 * 
	 * String ridcId=""; if(!jsondata.get("ridcId").toString().isEmpty()) { ridcId =
	 * jsondata.get("ridcId").toString();
	 * 
	 * } if(!jsondata.get("submit").toString().isEmpty()) { btnvalue =
	 * jsondata.get("submit").toString(); btnvalue1 =
	 * dispenceryService.getReplaceString(btnvalue); }
	 * 
	 * 
	 * if(!jsondata.get("reqNo").toString().isEmpty()) { String reqNo =
	 * jsondata.get("reqNo").toString(); reqNo1 =
	 * dispenceryService.getReplaceString(reqNo); }
	 * if(!jsondata.get("quotationNo").toString().isEmpty()) { String quotationNo =
	 * jsondata.get("quotationNo").toString(); quotationNo1 =
	 * dispenceryService.getReplaceString(quotationNo); }
	 * if(!jsondata.get("fileNo").toString().isEmpty()) { String fileNo =
	 * jsondata.get("fileNo").toString(); fileNo1 =
	 * dispenceryService.getReplaceString(fileNo); }
	 * if(!jsondata.get("date").toString().isEmpty()) { String date =
	 * jsondata.get("date").toString(); orderDate =
	 * dispenceryService.getReplaceString(date); }
	 * if(!jsondata.get("sanctionPurpose").toString().isEmpty()) { String
	 * sanctionPurpose = jsondata.get("sanctionPurpose").toString(); orderPurpose =
	 * dispenceryService.getReplaceString(sanctionPurpose); }
	 * 
	 * if(!jsondata.get("vendorName").toString().isEmpty()) { String vendorName =
	 * jsondata.get("vendorName").toString(); vendorName1 =
	 * dispenceryService.getReplaceString(vendorName); }
	 * 
	 * if(!jsondata.get("vendorId").toString().isEmpty()) { String vendorId =
	 * jsondata.get("vendorId").toString(); vendorId1=
	 * dispenceryService.getReplaceString(vendorId);
	 * 
	 * } if(!jsondata.get("quantumItem").toString().isEmpty()) { String quantumItem
	 * = jsondata.get("quantumItem").toString(); quantumItem1 =
	 * dispenceryService.getReplaceString(quantumItem); }
	 * 
	 * if(!jsondata.get("valueOfSanction").toString().isEmpty()) { String
	 * valueOfSanction = jsondata.get("valueOfSanction").toString(); orderValue =
	 * dispenceryService.getReplaceString(valueOfSanction); }
	 * if(!jsondata.get("authorityLetter").toString().isEmpty()) { String
	 * authorityLetter = jsondata.get("authorityLetter").toString(); refAuthority =
	 * dispenceryService.getReplaceString(authorityLetter); }
	 * if(!jsondata.get("ifa").toString().isEmpty()) { String ifa =
	 * jsondata.get("ifa").toString(); issuedUnder =
	 * dispenceryService.getReplaceString(ifa); }
	 * if(!jsondata.get("payingAgency").toString().isEmpty()) { String payingAgency
	 * = jsondata.get("payingAgency").toString(); payingAgency1 =
	 * dispenceryService.getReplaceString(payingAgency); }
	 * if(!jsondata.get("expenditure").toString().isEmpty()) { String expenditure =
	 * jsondata.get("expenditure").toString(); bookedBudget =
	 * dispenceryService.getReplaceString(expenditure); }
	 * if(!jsondata.get("videNote").toString().isEmpty()) { String videNote =
	 * jsondata.get("videNote").toString(); videNoteNumber =
	 * dispenceryService.getReplaceString(videNote); }
	 * if(!jsondata.get("uoNumber").toString().isEmpty()) { String uoNumber =
	 * jsondata.get("uoNumber").toString(); uoNumber1 =
	 * dispenceryService.getReplaceString(uoNumber); }
	 * 
	 * if(!jsondata.get("commSanction").toString().isEmpty()) { String commSanction
	 * = jsondata.get("commSanction").toString(); commWithSanction =
	 * dispenceryService.getReplaceString(commSanction); }
	 * if(!jsondata.get("cda").toString().isEmpty()) { String cda =
	 * jsondata.get("cda").toString(); commSanction1 =
	 * dispenceryService.getReplaceString(cda); }
	 * if(!jsondata.get("l1Cost").toString().isEmpty()) { String l1Cost =
	 * jsondata.get("l1Cost").toString(); l1Cost1 =
	 * dispenceryService.getReplaceString(l1Cost); }
	 * 
	 * if(!jsondata.get("l1CostWord").toString().isEmpty()) { String l1CostWord =
	 * jsondata.get("l1CostWord").toString(); l1CostWord1 =
	 * dispenceryService.getReplaceString(l1CostWord); }
	 * 
	 * 
	 * if(!jsondata.get("budgetaryId").toString().isEmpty()) { String budgetaryId =
	 * jsondata.get("budgetaryId").toString(); budgetaryId1 =
	 * dispenceryService.getReplaceString(budgetaryId); }
	 * 
	 * if (jsondata.get("departmentId") != null) { String departmentId =
	 * jsondata.get("departmentId").toString(); masDepartment =
	 * dispensaryDao.getMasDepartment(Long.parseLong(departmentId)); } if
	 * (jsondata.get("hospitalId") != null) { String hospitalId =
	 * jsondata.get("hospitalId").toString(); masHospital =
	 * dispensaryDao.gettoMasHospital(Long.parseLong(hospitalId)); } if
	 * (jsondata.get("storeQuotationTId") != null) { String storeQuotationTId =
	 * jsondata.get("storeQuotationTId").toString(); String storeQuotationTId1 =
	 * dispenceryService.getReplaceString(storeQuotationTId); storeQuotationTIdValue
	 * = storeQuotationTId1.split(","); }
	 * 
	 * StoreQuotationM storeQuotationObject=null; String storeQuotationM =
	 * jsondata.get("storeQuotationM").toString(); String storeQuotationM1 =
	 * dispenceryService.getReplaceString(storeQuotationM); String[]
	 * storeQuotationM1Value = storeQuotationM1.split(","); Long storeQuotM =
	 * Long.parseLong(storeQuotationM1Value[0]); storeQuotationObject =
	 * getStoreQuotationObjectFromId(storeQuotM);
	 * 
	 * String nomenclature = jsondata.get("nomenclature").toString(); String
	 * nomenclature1 = dispenceryService.getReplaceString(nomenclature); String[]
	 * nomenclatureValue = nomenclature1.split(",");
	 * 
	 * String requiredQty = jsondata.get("requiredQty").toString(); String
	 * requiredQty1 = dispenceryService.getReplaceString(requiredQty); String[]
	 * requiredQtyValue = requiredQty1.split(","); int itemLength =
	 * requiredQtyValue.length;
	 * 
	 * String accountingUnit1 = jsondata.get("accountingUnit1").toString(); String
	 * accountingUnit = dispenceryService.getReplaceString(accountingUnit1);
	 * String[] accountingUnitValue = accountingUnit.split(",");
	 * 
	 * String unitrate = jsondata.get("unitrate").toString(); String unitrate1 =
	 * dispenceryService.getReplaceString(unitrate); String[] unitrateValue =
	 * unitrate1.split(",");
	 * 
	 * String itemIdNom = jsondata.get("itemIdNom").toString(); String itemIdNom1 =
	 * dispenceryService.getReplaceString(itemIdNom); String[] itemIdNomValue =
	 * itemIdNom1.split(",");
	 * 
	 * String totalValue = jsondata.get("totalValue").toString(); String totalValue1
	 * = dispenceryService.getReplaceString(totalValue); String[] totalCostValue =
	 * totalValue1.split(",");
	 * 
	 * 
	 * //save data in table Users users=new Users(); //save data in table
	 * if(jsondata.get("userId")!=null) { String userId =
	 * jsondata.get("userId").toString();
	 * //users=dispensaryDao.getUser(Long.parseLong(userId));
	 * users.setUserId(Long.parseLong(userId)); } MasStoreSupplier
	 * masStoreSupplier=new MasStoreSupplier(); //save data in table
	 * 
	 * if(!jsondata.get("vendorId").toString().isEmpty()) { String vId =
	 * jsondata.get("vendorId").toString(); String vendorId =
	 * dispenceryService.getReplaceString(vId);
	 * masStoreSupplier.setSupplierId(Long.parseLong(vendorId)); }
	 * 
	 * 
	 * StoreSoM storeSoM=new StoreSoM();
	 * if(!jsondata.get("sanctionId").toString().isEmpty()) { String sanctionid =
	 * jsondata.get("sanctionId").toString(); String sanctionId1 =
	 * dispenceryService.getReplaceString(sanctionid);
	 * sanctionId=Long.parseLong(sanctionId1); if(sanctionId!=0)
	 * storeSoM=getStoreSoM(sanctionId); } StoreBudgetaryM
	 * storeBudgetaryM=getStoreBudgetaryM(Long.parseLong(budgetaryId1)); Session
	 * session1 = getHibernateUtils.getHibernateUtlis().OpenSession(); tx =
	 * session1.beginTransaction(); try{
	 * 
	 * storeSoM.setRfpNo(reqNo1); storeSoM.setMasDepartment(masDepartment);
	 * storeSoM.setMasHospital(masHospital);
	 * if(btnvalue1.equalsIgnoreCase("Submit")) storeSoM.setStatus("Y");//y=submit
	 * sanction else storeSoM.setStatus("N");//y=save sanction
	 * storeSoM.setQuatationNo(quotationNo1); storeSoM.setFileNo(fileNo1);
	 * storeSoM.setOrderDate(HMSUtil.convertStringDateToUtilDate(orderDate,
	 * "dd/MM/yyyy")); storeSoM.setOrderPurpose(orderPurpose);
	 * storeSoM.setQuatationOfItem(quantumItem1);
	 * storeSoM.setOrderValue(orderValue);
	 * storeSoM.setRefOfGovtAuthority(refAuthority);
	 * storeSoM.setIssuedUnder(issuedUnder);
	 * storeSoM.setPayingAgency(payingAgency1);
	 * storeSoM.setBookedBudget(bookedBudget);
	 * storeSoM.setVideNoteNumber(videNoteNumber); storeSoM.setUoNo(uoNumber1);
	 * storeSoM.setCommWithSanc(commWithSanction);
	 * storeSoM.setSancOverruling(commSanction1); storeSoM.setTotalAmt(new
	 * BigDecimal(l1Cost1));
	 * 
	 * storeSoM.setTotalAmtInWords(HMSUtil.convert(Integer.parseInt(l1Cost1)));
	 * storeSoM.setLastChgDate(new Timestamp(new Date().getTime()));
	 * storeSoM.setLastChgBy(users); storeSoM.setCreatedBy(users);
	 * storeSoM.setMasStoreSupplier(masStoreSupplier);
	 * if(storeQuotationObject!=null) {
	 * storeSoM.setStoreQuotationM(storeQuotationObject); }else {
	 * storeQuotationObject=new StoreQuotationM();
	 * storeQuotationObject.setQuotationMId(storeQuotM);
	 * storeSoM.setStoreQuotationM(storeQuotationObject); }
	 * 
	 * 
	 * // for lpTypeType Check if (storeQuotationObject != null &&
	 * storeQuotationObject.getLpTypeFlag() != null &&
	 * storeQuotationObject.getLpTypeFlag().equalsIgnoreCase("B")) {
	 * storeSoM.setLpTypeFlag(storeQuotationObject.getLpTypeFlag()); }
	 * 
	 * if(!ridcId.equalsIgnoreCase("")) storeSoM.setRidcId(Long.parseLong(ridcId));
	 * else if(!commSanction1.equalsIgnoreCase("Yes")) storeSoM.setRidcId(null);
	 * session1.saveOrUpdate(storeSoM); storeSoMId=storeSoM.getSoMId();
	 * if(sanctionId==0) { for (int i = 0; i < itemLength; i++) { StoreSoT
	 * storeSoT=new StoreSoT(); MasStoreItem masStoreItem=new MasStoreItem();
	 * MasStoreUnit masStoreUnit=new MasStoreUnit() ; if(requiredQtyValue[i]!=null
	 * && !requiredQtyValue[i].isEmpty()) storeSoT.setItemQty(new
	 * BigDecimal(requiredQtyValue[i].trim()));
	 * 
	 * if(itemIdNomValue[i]!=null && !itemIdNomValue[i].isEmpty()) {
	 * //masStoreItem=dispensaryDao.getMasStoreItem(Long.parseLong(itemIdNomValue[i]
	 * .trim())); List<MasStoreItem> MasStoreItemOldList =new
	 * ArrayList<MasStoreItem>(); MasStoreItemOldList =
	 * session1.createCriteria(MasStoreItem.class).add(Restrictions.eq("itemId",Long
	 * .parseLong(itemIdNomValue[i].trim()))).list();
	 * 
	 * if(MasStoreItemOldList.size()>0) { masStoreItem =MasStoreItemOldList.get(0);
	 * } masStoreItem.setItemId(Long.parseLong(itemIdNomValue[i].trim()));
	 * storeSoT.setMasStoreItem(masStoreItem);
	 * 
	 * } if(unitrateValue[i]!=null && !unitrateValue[i].isEmpty())
	 * storeSoT.setUnitRate(new BigDecimal(unitrateValue[i].trim()));
	 * 
	 * if(totalCostValue[i]!=null && !totalCostValue[i].isEmpty())
	 * storeSoT.setItemValue(new BigDecimal(totalCostValue[i].trim()));
	 * 
	 * masStoreUnit.setStoreUnitId(masStoreItem.getItemUnitId());
	 * 
	 * storeSoT.setMasStoreUnit(masStoreUnit);
	 * 
	 * storeSoT.setStoreSoM(storeSoM);//need to chk
	 * 
	 * if(storeQuotationTIdValue[i]!=null && !storeQuotationTIdValue[i].isEmpty()) {
	 * StoreQuotationT storeQuotationT=new StoreQuotationT();
	 * storeQuotationT.setQuotationTId(Long.parseLong(storeQuotationTIdValue[i].trim
	 * ())); storeSoT.setStoreQuotationT(storeQuotationT);
	 * 
	 * } long storeSoTId=(long) session1.save(storeSoT); } } if (storeSoMId != 0 &&
	 * btnvalue1.equalsIgnoreCase("Submit")) { //StoreBudgetaryM
	 * storeBudgetaryM=lPProcessDao.getStoreBudgetaryM(Long.parseLong(budgetaryId1))
	 * ; List<StoreQuotationM> storeQuotationMlist =
	 * storeBudgetaryM.getStoreQuotationMs(); for(StoreQuotationM storeQuotnM1 :
	 * storeQuotationMlist) { storeQuotnM1.setStatus("S");//s=for Sanction created
	 * session1.update(storeQuotnM1);
	 * 
	 * } } tx.commit(); session1.flush(); session1.clear(); }
	 * 
	 * catch(Exception e) { if (tx != null) { try { tx.rollback(); storeSoMId=-1; }
	 * catch(Exception re) { storeSoMId=-1; re.printStackTrace(); } } storeSoMId=-1;
	 * e.printStackTrace(); }
	 * 
	 * finally {
	 * 
	 * getHibernateUtils.getHibernateUtlis().CloseConnection();
	 * 
	 * }
	 * 
	 * return storeSoMId; } return -1; }
	 * 
	 * private StoreQuotationM getStoreQuotationObjectFromId(Long storeQuotM) {
	 * StoreQuotationM storeQuotationObj = null; Session session =
	 * getHibernateUtils.getHibernateUtlis().OpenSession(); try { storeQuotationObj
	 * = (StoreQuotationM) session.get(StoreQuotationM.class, storeQuotM); }catch
	 * (Exception e) { getHibernateUtils.getHibernateUtlis().CloseConnection();
	 * e.printStackTrace(); } return storeQuotationObj; }
	 * 
	 * @Override public int deleteQuotation(StoreQuotationM storeQuotationM) { //
	 * TODO Auto-generated method stub int status=0; Transaction tx=null; try {
	 * Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * tx=session.beginTransaction(); if (storeQuotationM != null) {
	 * session.delete(storeQuotationM); tx.commit(); status=1; session.clear();
	 * session.flush();
	 * 
	 * }
	 * 
	 * } catch (Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.getMessage();
	 * e.printStackTrace(); }finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return status; }
	 * 
	 * 
	 * @SuppressWarnings("unchecked")
	 * 
	 * @Override public void removeDataFromTempTableForBackLp(StoreBudgetaryM
	 * storeBudgetaryM) { List<StoreBudgetaryT> StoreBudgetaryTList = new
	 * ArrayList<StoreBudgetaryT>(); Transaction tx=null; try { Session session =
	 * getHibernateUtils.getHibernateUtlis().OpenSession(); StoreBudgetaryTList =
	 * (List<StoreBudgetaryT>) session.createCriteria(StoreBudgetaryT.class)
	 * .add(Restrictions.eq("storeBudgetaryM.budgetaryMId",
	 * storeBudgetaryM.getBudgetaryMId()))
	 * .add(Restrictions.isNotNull("tempDirectReceivingForBackLp")).list();
	 * 
	 * if(StoreBudgetaryTList.size()>0) { tx=session.beginTransaction();
	 * for(StoreBudgetaryT budgetaryTObject : StoreBudgetaryTList) {
	 * TempDirectReceivingForBackLp tempBackObject=null; tempBackObject =
	 * (TempDirectReceivingForBackLp)
	 * session.get(TempDirectReceivingForBackLp.class,
	 * budgetaryTObject.getTempDirectReceivingForBackLp().getTempId());
	 * tempBackObject.setStatus(null); tempBackObject.setBudgetaryStatus(null);
	 * tempBackObject.setQuotationStatus(null);
	 * tempBackObject.setSanctionOrderStatus(null);
	 * tempBackObject.setSupplyOrderStatus(null);
	 * 
	 * session.update(tempBackObject); } tx.commit(); }
	 * 
	 * }catch (Exception e) { tx.rollback(); e.printStackTrace(); }
	 * 
	 * }
	 */
}
