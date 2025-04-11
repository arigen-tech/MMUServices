package com.mmu.services.dao.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mmu.services.dao.ApprovalProcessDao;
import com.mmu.services.dao.MedicalExamDAO;
import com.mmu.services.dao.SystemAdminDao;
import com.mmu.services.entity.AnmApmApproval;
import com.mmu.services.entity.FundAllocationHd;
import com.mmu.services.entity.MasMedicalExamReport;
import com.mmu.services.entity.StoreInternalIndentM;
import com.mmu.services.entity.Users;
import com.mmu.services.entity.VendorInvoicePayment;
import com.mmu.services.hibernateutils.GetHibernateUtils;
import com.mmu.services.utils.HMSUtil;

@Repository
@Transactional
public class ApprovalProcessDaoImpl implements ApprovalProcessDao {

	@Autowired
	GetHibernateUtils getHibernateUtils;
	@Autowired
	SystemAdminDao systemAdminDao;
	@Autowired
	MedicalExamDAO medicalExamDAO;
	

	
	@Override
	public Long saveOrUpdateANMEntryDetails(HashMap<String, Object> jsondata) {
		 Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		 Transaction t = session.beginTransaction();
		 Long anmApmId = null;
		 Date date = new Date();
	     SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
	     String str = formatter.format(date);
	     Date today=HMSUtil.convertStringTypeDateToDateType(str);
		 try {
		
			 if(jsondata.get("anmApmId").equals(""))
	         {
				 AnmApmApproval anmApmApproval=new AnmApmApproval();
				 anmApmApproval.setCityId(Long.parseLong(jsondata.get("cityId").toString()));
				 anmApmApproval.setMmuId(Long.parseLong(jsondata.get("mmuId").toString()));
				 anmApmApproval.setTotalNoPatients(Long.parseLong(jsondata.get("totalNoPatients").toString()));
				 anmApmApproval.setPatientNoLabTest(Long.parseLong(jsondata.get("patientNoLabTest").toString()));
				 anmApmApproval.setPatientNoMedicineDispensed(Long.parseLong(jsondata.get("patientNoMedicineDispensed").toString()));
				 anmApmApproval.setPatientNoLabourRegistredDepartment(Long.parseLong(jsondata.get("patientNoLabourRegistredDepartment").toString()));
				 anmApmApproval.setPatientNoApplyLabourRegistration(Long.parseLong(jsondata.get("patientNoApplyLabourRegistration").toString()));
				 anmApmApproval.setLastChgBy(Long.parseLong(jsondata.get("lastChgBy").toString()));
				 anmApmApproval.setStatus(jsondata.get("status").toString());
				 //anmApmApproval.setPatientNoMedicineDispensed(Long.parseLong(jsondata.get("patientNoMedicineDispensed").toString()));
				 Date dateOfEntry=HMSUtil.convertStringTypeDateToDateType(jsondata.get("dateOfEntry").toString());
				 anmApmApproval.setOpdDate(dateOfEntry);
				 anmApmApproval.setDateOfEntry(today);
				
				 anmApmId =Long.parseLong(session.save(anmApmApproval).toString());
	          }
			 else
			 {
				 AnmApmApproval anmApmApproval=new AnmApmApproval();
				 anmApmId =Long.parseLong(jsondata.get("anmApmId").toString());
				 Long anmApmIdPKey=Long.parseLong(jsondata.get("anmApmId").toString());
				 anmApmApproval.setAnmApmId(anmApmIdPKey);
				 anmApmApproval.setCityId(Long.parseLong(jsondata.get("cityId").toString()));
				 anmApmApproval.setMmuId(Long.parseLong(jsondata.get("mmuId").toString()));
				 anmApmApproval.setTotalNoPatients(Long.parseLong(jsondata.get("totalNoPatients").toString()));
				 anmApmApproval.setPatientNoLabTest(Long.parseLong(jsondata.get("patientNoLabTest").toString()));
				 anmApmApproval.setPatientNoMedicineDispensed(Long.parseLong(jsondata.get("patientNoMedicineDispensed").toString()));
				 anmApmApproval.setPatientNoLabourRegistredDepartment(Long.parseLong(jsondata.get("patientNoLabourRegistredDepartment").toString()));
				 anmApmApproval.setPatientNoApplyLabourRegistration(Long.parseLong(jsondata.get("patientNoApplyLabourRegistration").toString()));
				 anmApmApproval.setLastChgBy(Long.parseLong(jsondata.get("lastChgBy").toString()));
				 anmApmApproval.setStatus(jsondata.get("status").toString());
				 if(jsondata.containsKey("action"))
				 {
					 anmApmApproval.setStatus("a"); 
					 anmApmApproval.setRemarks(jsondata.get("remarks").toString());
				 }
				 //anmApmApproval.setPatientNoMedicineDispensed(Long.parseLong(jsondata.get("patientNoMedicineDispensed").toString()));
				 Date dateOfEntry=HMSUtil.convertStringTypeDateToDateType(jsondata.get("dateOfEntry").toString());
				 anmApmApproval.setOpdDate(dateOfEntry);
				 anmApmApproval.setDateOfEntry(today);
				
				 session.update(anmApmApproval);
				 
			 }
		
				/*String captureVendorBillDetailId=jsondata.get("captureVendorBillDetailId").toString();
			
				String penaltyAmount=jsondata.get("penaltyAmount").toString();
				String finalAmount=jsondata.get("finalAmount").toString();
				String lastApprovalMsg=jsondata.get("lastApprovalMsg").toString();
				String qryStringHd  ="update CaptureVendorBillDetail u set u.lastApprovalStatus='"+lastApprovalMsg+"',u.penaltyAmount='"+penaltyAmount+"',u.finalAmount='"+finalAmount+"',u.status='C',u.paymentStatus='C'  where u.captureVendorBillDetailId='"+captureVendorBillDetailId+"' " ;
				Query queryHd = session.createQuery(qryStringHd);
		        int countHd = queryHd.executeUpdate();
				System.out.println(countHd + "Records CaptureVendorBillDetail Updated.");*/
				t.commit();	

			
		 }catch (Exception e) {
			e.printStackTrace();
			t.rollback();
			System.out.println("Exception Message Print ::" + e.toString());
			return 0L;
			
		 }finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		 return anmApmId;
	}



	@Override
	public Map<String, List<AnmApmApproval>> getAnmOpdOfflineData(JSONObject jsondata) {
        Map<String, List<AnmApmApproval>> map = new HashMap<>();
        List<AnmApmApproval> anmOpdOfflineDetails = new ArrayList<>();
        List totalMatches  =new ArrayList<>();
        int pageNo=0;
        int pageSize = 5;
        Date fromDate;
        Date toDate;
        Criterion c1 = null;
        /*if(jsondata.has("fromDate"))
        {	
         fromDate = (String) jsondata.get("fromDate");
		 toDate = (String) jsondata.get("toDate");
        }*/
        try {
            Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
            Criteria criteria = session.createCriteria(AnmApmApproval.class);
            if(jsondata.has("PN") && jsondata.get("PN").toString() !=null)
                pageNo = Integer.parseInt(jsondata.get("PN").toString());

            if (jsondata.has("creatdBy") && jsondata.get("creatdBy").equals("anm")) {
                //criteria.add(Restrictions.eq("status","S"));
            	criteria.add(Restrictions.or(Restrictions.eq("status", "s"), Restrictions.eq("status", "c"),Restrictions.eq("status", "a")));
                
            }
            if (jsondata.has("creatdBy") && jsondata.get("creatdBy").equals("apm")) {
                //criteria.add(Restrictions.eq("status","S"));
            	criteria.add(Restrictions.or(Restrictions.eq("status", "c")));
                
            }
            if (jsondata.has("cityId") && !jsondata.get("cityId").toString().trim().isEmpty()) {
                criteria.add(Restrictions.eq("cityId",Long.parseLong(jsondata.get("cityId").toString())));
            	//criteria.add(Restrictions.or(Restrictions.eq("status", "C"), Restrictions.eq("status", "A")));
                
            }
            if (jsondata.has("mmuId") && !jsondata.get("mmuId").toString().trim().isEmpty()) {
                criteria.add(Restrictions.eq("mmuId",Long.parseLong(jsondata.get("mmuId").toString())));
            	//criteria.add(Restrictions.or(Restrictions.eq("status", "C"), Restrictions.eq("status", "A")));
                
            }
            if (jsondata.has("anmApmId") && !jsondata.get("anmApmId").toString().trim().isEmpty()) {
                criteria.add(Restrictions.eq("anmApmId",Long.parseLong(jsondata.get("anmApmId").toString())));
            	//criteria.add(Restrictions.or(Restrictions.eq("status", "C"), Restrictions.eq("status", "A")));
                
            }
           
            if (jsondata.has("fromDate")
                    && !jsondata.get("fromDate").toString().trim().isEmpty()
                    && jsondata.has("toDate")
                    && !jsondata.get("toDate").toString().trim().isEmpty()
            ) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                 fromDate = formatter.parse(jsondata.getString("fromDate"));
                 toDate = formatter.parse(jsondata.getString("toDate"));
                criteria.add(Restrictions.ge("opdDate", fromDate));
                criteria.add(Restrictions.le("opdDate", toDate));
            }
            if (jsondata.has("fromDate") && !jsondata.get("fromDate").toString().trim().isEmpty()) 
            {
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                 fromDate = formatter.parse(jsondata.getString("fromDate"));
                 criteria.add(Restrictions.ge("opdDate", fromDate));
               
            }
           
          
           
            criteria.addOrder(Order.desc("anmApmId"));
            totalMatches = criteria.list();
            if (pageNo > 0) {
                criteria.setFirstResult((pageSize) * (pageNo - 1));
                criteria.setMaxResults(pageSize);
            }
            anmOpdOfflineDetails = criteria.list();


            map.put("anmOpdOfflineDetails", anmOpdOfflineDetails);
            map.put("totalMatches", totalMatches);
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            getHibernateUtils.getHibernateUtlis().CloseConnection();
        }
        return map;
    }
	
	
	/*
	 * @Override public Map<String, Object> getIndentList(HashMap<String, Object>
	 * requestData) { // TODO Auto-generated method stub Map<String, Object> map =
	 * new HashMap<String, Object>(); List totalMatches = new ArrayList();
	 * List<StoreInternalIndentM> storeInternalIndentMList = new
	 * ArrayList<StoreInternalIndentM>(); List<Object> StoreInternalIndentM= new
	 * ArrayList<Object>(); List<MasMedicalExamReport>
	 * listMasMedicalExamReportInbox=null; List<MasMedicalExamReport>
	 * listMasMedicalExamReportPendingAndReject=null;
	 * 
	 * List<MasMedicalExamReport> listMBMasMedicalExamReportInbox=null;
	 * List<MasMedicalExamReport> listMBMasMedicalExamReportPendingAndReject=null;
	 * 
	 * JSONObject json = new JSONObject(requestData); Long hospitalId =
	 * Long.parseLong(json.get("hospitalId").toString()); Long departmentId =
	 * Long.parseLong(json.get("departmentId").toString()); Long userId =
	 * Long.parseLong(json.get("userId").toString());
	 * 
	 * List<MasMedicalExamReport> listReject=null;
	 * 
	 * Criteria cr=null; Criteria crInbox1=null; Criteria crPendingAndReject2=null;
	 * Criteria cr3=null; Criterion co1=null; Criterion co2=null; Criterion
	 * co3=null; Criterion co4=null; Criterion co5=null; Criterion co6=null;
	 * Criterion co7=null; Criterion co8=null; Criterion co9=null; Users users=null;
	 * List<Long>listMasDesi= null; Criteria crMBInbox1=null; Criteria
	 * crMBPendingAndReject2=null;
	 * 
	 * Criteria onlyRejected=null;
	 * List<MasMedicalExamReport>listMasMedExamReportForDigiRejAndApproved=null;
	 * 
	 * try {
	 * 
	 * users=systemAdminDao.getUsersByUserIdAndHospitalId(userId,hospitalId);
	 * 
	 * Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * Transaction tx=session.beginTransaction();
	 * 
	 * if(users!=null) { if(users.getDesignationId()!=null ) { String []
	 * desigArray=users.getDesignationId().split(","); if(desigArray!=null &&
	 * desigArray.length>0) { listMasDesi= new ArrayList<>(); for(String
	 * ss:desigArray) { listMasDesi.add(Long.parseLong(ss.trim())); } } } }
	 * 
	 * cr =
	 * session.createCriteria(StoreInternalIndentM.class).createAlias("masHospital",
	 * "mh").createAlias("department1", "md"); cr
	 * =cr.add(Restrictions.eq("mh.hospitalId",
	 * hospitalId)).add(Restrictions.eq("md.departmentId", departmentId));
	 * cr.addOrder(Order.desc("demandDate"));
	 * 
	 * if(json.get("flag").toString().equalsIgnoreCase("Y")) {
	 * cr=cr.add(Restrictions.eq("status", "Y").ignoreCase()); } else
	 * if(json.get("flag").toString().equalsIgnoreCase("YN")) { Criterion cr1=
	 * Restrictions.eq("status", "Y").ignoreCase(); Criterion cr2=
	 * Restrictions.eq("status", "N").ignoreCase(); LogicalExpression orExp =
	 * Restrictions.or(cr1, cr2); cr.add( orExp ); }
	 * 
	 * if(CollectionUtils.isNotEmpty(listMasDesi) && hospitalId!=null) { crInbox1 =
	 * session.createCriteria(MasMedicalExamReport.class).createAlias(
	 * "visit.masAppointmentType", "masAppointmentType"); co1
	 * =Restrictions.eq("forwardUnitId", hospitalId); co2=Restrictions.eq("status",
	 * "af"); Criterion co33=Restrictions.in("fowardedDesignationId",listMasDesi);
	 * //Criterion co34=Restrictions.eq("masAppointmentType.appointmentTypeCode",
	 * "ME").ignoreCase(); crInbox1.add(co33); //crInbox1.add(co34);
	 * LogicalExpression orExp1 = Restrictions.and(co1,co2); crInbox1.add(orExp1); }
	 * 
	 * crPendingAndReject2 =
	 * session.createCriteria(MasMedicalExamReport.class).createAlias(
	 * "visit.masAppointmentType",
	 * "masAppointmentType");//.add(Restrictions.eq("hospitalId", hospitalId));
	 * Criterion co88=Restrictions.eq("lastChgBy", userId);
	 * co5=Restrictions.eq("status", "pe"); co6=Restrictions.eq("status", "rj");
	 * co4=Restrictions.eq("status", "ac"); co3=Restrictions.eq("status", "af");
	 * Disjunction orExp3 = Restrictions.or(co5,co6,co4,co3);
	 * crPendingAndReject2.add(orExp3); crPendingAndReject2.add(co88);
	 * 
	 * co7=Restrictions.and(Restrictions.eq("approvedBy",
	 * "MO"),Restrictions.eq("hospitalId", hospitalId));
	 * co8=Restrictions.and(Restrictions.eq("approvedBy",
	 * "RMO"),Restrictions.eq("rmoHospitalId", hospitalId));
	 * co9=Restrictions.and(Restrictions.eq("approvedBy",
	 * "PRMo"),Restrictions.eq("pdmsHospitalId", hospitalId));
	 * 
	 * Disjunction orExp31 = Restrictions.or(co7,co8,co9);
	 * crPendingAndReject2.add(orExp31);
	 * 
	 * 
	 * /////////////////////////////////////////////////////////Reject only modify
	 * by client////////////////////////////////////////////////////////////
	 * 
	 * onlyRejected =
	 * session.createCriteria(MasMedicalExamReport.class).createAlias(
	 * "visit.masAppointmentType",
	 * "masAppointmentType");//.add(Restrictions.eq("hospitalId", hospitalId));
	 * Criterion co61=Restrictions.eq("status", "rj"); onlyRejected.add(co61);
	 * 
	 * co7=Restrictions.and(Restrictions.eq("approvedBy",
	 * "MO"),Restrictions.eq("hospitalId", hospitalId));
	 * co8=Restrictions.and(Restrictions.eq("approvedBy",
	 * "RMO"),Restrictions.eq("rmoHospitalId", hospitalId));
	 * co9=Restrictions.or(Restrictions.and(Restrictions.eq("approvedBy",
	 * "PRMo"),Restrictions.eq("rmoHospitalId", hospitalId))
	 * ,Restrictions.and(Restrictions.eq("approvedBy",
	 * "PRMo"),Restrictions.eq("pdmsHospitalId", hospitalId)));
	 * 
	 * Disjunction orExp311 = Restrictions.or(co7,co8,co9);
	 * onlyRejected.add(orExp311); listReject=onlyRejected.list();
	 * /////////////////////////////////////////////////////////////////////////////
	 * ///////////////////////////////////////////
	 * 
	 * ////////////////////////// used for Digitization Approved and Reject
	 * ///////////////////////////////////////
	 * 
	 * Criteria criteriaForDigi= session. createCriteria(MasMedicalExamReport.class)
	 * .createAlias("visit", "visit") .createAlias("visit.patient",
	 * "patient").createAlias("visit.masAppointmentType", "masAppointmentType");
	 * Criterion crd3=
	 * Restrictions.or(Restrictions.eq("masAppointmentType.appointmentTypeCode",
	 * "ME").ignoreCase(),Restrictions.eq("masAppointmentType.appointmentTypeCode",
	 * "MB").ignoreCase()); Criterion crd4=null; Criterion crd5=null;
	 * crd4=Restrictions.or(Restrictions.eq("status", "ar")
	 * ,Restrictions.eq("status", "vr"),Restrictions.eq("status", "ea"));
	 * 
	 * crd5=Restrictions.or(Restrictions.eq("status", "ev")
	 * ,Restrictions.eq("status", "ea"));
	 * 
	 * criteriaForDigi.add(crd3).add(crd4);
	 * listMasMedExamReportForDigiRejAndApproved=criteriaForDigi.list();
	 * 
	 * //////////////////////////////////////////End of Digitization for Approved
	 * and
	 * Reject///////////////////////////////////////////////////////////////////////
	 * //////////////////
	 * 
	 * if(!cr.list().isEmpty() && cr.list().size()>0) {
	 * 
	 * totalMatches=cr.list(); storeInternalIndentMList= cr.list(); } try {
	 * if(crInbox1!=null && !crInbox1.list().isEmpty() && crInbox1.list().size()>0)
	 * {
	 * 
	 * listMasMedicalExamReportInbox= crInbox1.list(); }
	 * 
	 * 
	 * if(!crMBInbox1.list().isEmpty() && crMBInbox1.list().size()>0) {
	 * 
	 * listMBMasMedicalExamReportInbox= crMBInbox1.list(); }
	 * 
	 * } catch(Exception e) { e.printStackTrace(); }
	 * 
	 * if(crPendingAndReject2!=null && !crPendingAndReject2.list().isEmpty() &&
	 * crPendingAndReject2.list().size()>0) {
	 * 
	 * listMasMedicalExamReportPendingAndReject= crPendingAndReject2.list(); }
	 * 
	 * 
	 * if(!crMBPendingAndReject2.list().isEmpty() &&
	 * crMBPendingAndReject2.list().size()>0) {
	 * 
	 * listMBMasMedicalExamReportPendingAndReject= crMBPendingAndReject2.list(); }
	 * 
	 * 
	 * tx.commit(); map.put("storeInternalIndentMList", storeInternalIndentMList);
	 * map.put("totalMatches", totalMatches.size());
	 * 
	 * map.put("listMasMedicalExamReportInbox", listMasMedicalExamReportInbox);
	 * 
	 * map.put("listMasMedicalExamReportPendingAndReject",
	 * listMasMedicalExamReportPendingAndReject);
	 * 
	 * 
	 * map.put("listMBMasMedicalExamReportInbox", listMBMasMedicalExamReportInbox);
	 * 
	 * map.put("listMBMasMedicalExamReportPendingAndReject",
	 * listMBMasMedicalExamReportPendingAndReject);
	 * 
	 * map.put("listMasMedExamReportForDigiRejAndApproved",
	 * listMasMedExamReportForDigiRejAndApproved);
	 * 
	 * map.put("listOfReject", listReject); } catch (Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.getMessage();
	 * e.printStackTrace(); } finally {
	 * 
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); }
	 * 
	 * 
	 * 
	 * return map; }
	 */

}
