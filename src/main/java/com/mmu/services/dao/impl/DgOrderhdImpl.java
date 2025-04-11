package com.mmu.services.dao.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mmu.services.dao.DgOrderdtDao;
import com.mmu.services.dao.DgOrderhdDao;
import com.mmu.services.dao.DgResultEntryDtDao;
import com.mmu.services.dao.DgResultEntryHdDao;
import com.mmu.services.dao.PatientDocumentDetailDao;
import com.mmu.services.dao.ReferralPatientDtDao;
import com.mmu.services.dao.ReferralPatientHdDao;
import com.mmu.services.dao.SystemAdminDao;
import com.mmu.services.entity.DgMasInvestigation;
import com.mmu.services.entity.DgOrderdt;
import com.mmu.services.entity.DgOrderhd;
import com.mmu.services.entity.DgResultEntryDt;
import com.mmu.services.entity.DgResultEntryHd;
import com.mmu.services.entity.DischargeIcdCode;
import com.mmu.services.entity.DiverOrderDet;
import com.mmu.services.entity.FwcObjDetail;
import com.mmu.services.entity.MasIcd;
import com.mmu.services.entity.MasIdealWeight;
import com.mmu.services.entity.MasStoreItem;
import com.mmu.services.entity.OpdDisposalDetail;

import com.mmu.services.entity.OpdPatientDetail;
import com.mmu.services.entity.OpdTemplate;
import com.mmu.services.entity.OpdTemplateInvestigation;
import com.mmu.services.entity.OpdTemplateTreatment;
import com.mmu.services.entity.Patient;
import com.mmu.services.entity.PatientDocumentDetail;
import com.mmu.services.entity.PatientImmunizationHistory;
import com.mmu.services.entity.PatientImpantHistory;
import com.mmu.services.entity.PatientMedicalCat;
import com.mmu.services.entity.PatientPrescriptionDt;
import com.mmu.services.entity.PatientPrescriptionHd;
import com.mmu.services.entity.PatientSymptom;
import com.mmu.services.entity.ProcedureDt;
import com.mmu.services.entity.ProcedureHd;
import com.mmu.services.entity.ReferralPatientDt;
import com.mmu.services.entity.ReferralPatientHd;
import com.mmu.services.entity.Visit;
import com.mmu.services.hibernateutils.GetHibernateUtils;
import com.mmu.services.service.impl.OpdServiceImpl;
import com.mmu.services.utils.HMSUtil;
import com.mmu.services.utils.ProjectUtils;

@Repository
@Transactional
public class DgOrderhdImpl extends GenericDaoImpl<DgOrderhd, Long> implements DgOrderhdDao {
	String databaseScema = HMSUtil.getProperties("adt.properties", "currentSchema").trim();
	final String DG_ORDER_DT=databaseScema+"."+"DG_ORDER_DT";
	final String DG_ORDER_HD=databaseScema+"."+"DG_ORDER_HD";
	final String DG_MAS_INVESTIGATION=databaseScema+"."+"DG_MAS_INVESTIGATION";
	
	
	final String  PATIENT_PRESCRIPTION_HD=databaseScema+"."+"PATIENT_PRESCRIPTION_HD";
	final String   PATIENT_PRESCRIPTION_DT=databaseScema+"."+"PATIENT_PRESCRIPTION_DT";
	final String   MAS_STORE_ITEM=databaseScema+"."+"MAS_STORE_ITEM";
	final String   MAS_STORE_UNIT=databaseScema+"."+"MAS_STORE_UNIT";
	final String   MAS_FREQUENCY=databaseScema+"."+"MAS_FREQUENCY";
	final String  REFERRAL_PATIENT_DT=databaseScema+"."+"REFERRAL_PATIENT_DT";
	final String  REFERRAL_PATIENT_HD=databaseScema+"."+"REFERRAL_PATIENT_HD";
	final String   MAS_EMPANELLED_HOSPITAL=databaseScema+"."+"MAS_EMPANELLED_HOSPITAL";
	final String   MAS_ICD=databaseScema+"."+"MAS_ICD";
	final String   MAS_DEPARTMENT=databaseScema+"."+"MAS_DEPARTMENT";
	final String   PROCEDURE_DT=databaseScema+"."+"PROCEDURE_DT";
	final String   PROCEDURE_HD=databaseScema+"."+"PROCEDURE_HD";
	final String   MAS_NURSING_CARE=databaseScema+"."+"MAS_NURSING_CARE";
	
	@Autowired
	GetHibernateUtils getHibernateUtils;
	 
	 @Autowired
	 DgResultEntryDtDao dgResultEntryDtDao;
	 @Autowired
	 DgOrderdtDao dgOrderdtDao;
	 @Autowired
	 ReferralPatientDtDao referralPatientDtDao;
	 @Autowired
	 ReferralPatientHdDao referralPatientHdDao;	 
	 
	 @Autowired
	 PatientDocumentDetailDao patientDocumentDetailDao;
	 
	 @Autowired
	 SystemAdminDao systemAdminDao;
	 @Autowired
	 DgResultEntryHdDao dgResultEntryHdDao;

	 @Override
	public List<Object[]> getDgMasInvestigations(Long visitId,Long opdPatientDetailId) {
		List<DgMasInvestigation>  listDgMasInvestigation =null;
		Transaction transation=null;
		 List<Object[]> listObject=null;
		try {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		transation=session.beginTransaction();
		 StringBuilder sbQuery = new StringBuilder();
		 
		    sbQuery.append(" select dgmas.INVESTIGATION_ID,dgmas.INVESTIGATION_NAME,ohd.ORDERHD_ID,odt.ORDER_DATE, ohd.VISIT_ID,");
		    sbQuery.append( "ohd.DEPARTMENT_ID,ohd.MMU_ID,odt.ORDERDT_ID,odt.ORDER_STATUS from " +DG_ORDER_HD+ " ohd ");
		    sbQuery.append(" join "+DG_ORDER_DT+ " odt on  ohd.orderhd_id=odt.orderhd_id join "+DG_MAS_INVESTIGATION); 
		    sbQuery.append(" dgmas on dgmas.INVESTIGATION_ID=odt.INVESTIGATION_ID and dgmas.status='y'  "); 
		    sbQuery.append(" where ohd.VISIT_ID =:visitId");
		    Query query = session.createSQLQuery(sbQuery.toString());

		    query.setParameter("visitId", visitId);
		    
		     listObject = query.list();
		transation.commit();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		
		}
		return listObject;
	}

	@Override
	public List<Object[]> getTreatementDetail(Long visitId,Long opdPatientDetailId) {
		Transaction transation=null;
		 List<Object[]> listObject=null;
		try {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		transation=session.beginTransaction();
		 StringBuilder sbQuery = new StringBuilder();
		    sbQuery.append(" select msi.nomenclature,msi.ITEM_ID,mf.FREQUENCY_ID,mf.FREQUENCY_NAME,mf.FREQUENCY_CODE," );
		    sbQuery.append("ppdt.NO_OF_DAYS,ppdt.dosage,pphd.PRESCRIPTION_HD_ID,ppdt.PRESCRIPTION_DT_ID,ppdt.total,ppdt.instruction,ppdt.STORE_STOCK, msu.STORE_UNIT_NAME,msi.PVMS_NO,msi.ITEM_TYPE_ID,msi.DISP_UNIT_ID, msi.ITEM_CLASS_ID,ppdt.status from ");
		    sbQuery.append( PATIENT_PRESCRIPTION_HD+ " pphd join " +PATIENT_PRESCRIPTION_DT+ " ppdt on pphd.PRESCRIPTION_HD_ID=ppdt.PRESCRIPTION_HD_ID "); 
		    sbQuery.append(" left outer join " +MAS_STORE_ITEM+ " msi on msi.ITEM_ID =ppdt.ITEM_ID left outer  join " +MAS_STORE_UNIT+ " msu on msu.STORE_UNIT_ID=msi.DISP_UNIT_ID ");
		    sbQuery.append(" left outer  join " +MAS_FREQUENCY+" mf on mf.FREQUENCY_ID=ppdt.FREQUENCY_ID");
		    sbQuery.append(" where pphd.VISIT_ID =:visitId ");
		    if(opdPatientDetailId!=null && opdPatientDetailId!=0)
			    sbQuery.append(" and pphd.OPD_PATIENT_DETAILS_ID =:opdPatientDetailId ");
			    Query query = session.createSQLQuery(sbQuery.toString());

			    query.setParameter("visitId", visitId);
			    if(opdPatientDetailId!=null && opdPatientDetailId!=0)
			    query.setParameter("opdPatientDetailId", opdPatientDetailId);
		    
		     listObject = query.list();
		
		transation.commit();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		
		}
		return listObject;
	}

	 public Map<String,Object> getDgOrderDtByOrderHdIdAndInvestigationId(List<String> orderhdIds, List<String> investigationIds ){
		Session session=null;
		List<String>listDgMasInvestigation=null;
		List<DgOrderdt>listDgOrderDt=null;
		Map<String,Object>hashMapDgMasInvestigation=null;
		 try {
			 session= getHibernateUtils.getHibernateUtlis().OpenSession();
			 Criteria criteria=session.createCriteria(DgOrderdt.class).add(Restrictions.in("ORDERHD_ID", orderhdIds)).add(Restrictions.in("INVESTIGATION_ID", investigationIds))
			 .setProjection(
			            Projections.projectionList().add(Projections.property("INVESTIGATION_ID"))) ;
		 listDgMasInvestigation=criteria.list();
		 
		 Criteria masCriteria=session.createCriteria(DgOrderdt.class).add(Restrictions.in("ORDERHD_ID", orderhdIds)).add(Restrictions.in("INVESTIGATION_ID", investigationIds));
		 listDgOrderDt=criteria.list();
		 
		 hashMapDgMasInvestigation=new HashMap<>();
		 hashMapDgMasInvestigation.put("listDgMasInvestigation", listDgMasInvestigation);
		 hashMapDgMasInvestigation.put("listDgOrderDt", listDgOrderDt);
		 } 
		 catch(Exception e) {
			 e.printStackTrace();
		 }finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
		 }
		 return hashMapDgMasInvestigation;
	 }

	@Override
	public DgOrderdt getDgOrderDtByDgOrderdtId(Long dgOrderDtId) {
		DgOrderdt dgOrderdt=null;
		//try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			
			dgOrderdt=(DgOrderdt) session.get(DgOrderdt.class, dgOrderDtId);
		/*
		 * } catch(Exception e) { e.printStackTrace(); } finally {
		 * //getHibernateUtils.getHibernateUtlis().CloseConnection(); }
		 */
		return dgOrderdt;
	}

	@Override
	public Long saveOrUpdateDgOrderdt(DgOrderdt dgOrderDt) {
		Session session=null;
		Long dgOrderDtId=null;
		//Transaction tx=null;
		//try{
			session= getHibernateUtils.getHibernateUtlis().OpenSession();
			//tx=session.beginTransaction();
			session.saveOrUpdate(dgOrderDt);
			dgOrderDtId=dgOrderDt.getOrderdtId();
			session.flush();
	        session.clear();
			//tx.commit();
			
		/*
		 * } catch(Exception e) { e.printStackTrace(); } finally {
		 * 
		 * //getHibernateUtils.getHibernateUtlis().CloseConnection();
		 * 
		 * }
		 */
		return dgOrderDtId;
	}
	
	@Override
	public Long saveOrUpdateDgOrderHd(DgOrderhd dgOrderhd) {
		Session session=null;
		Long dgOrderhdId=null;
		//Transaction tx=null;
		//try{
			session= getHibernateUtils.getHibernateUtlis().OpenSession();
			//tx=session.beginTransaction();
			session.saveOrUpdate(dgOrderhd);
			dgOrderhdId=dgOrderhd.getOrderhdId();
			session.flush();
	        session.clear();
			//tx.commit();
			
		//}
	/*
	 * catch(Exception e) { e.printStackTrace(); } finally {
	 * 
	 * 
	 * }
	 */
		return dgOrderhdId;
	}
	
	@Override
	public Long saveOrUpdatePatient(Patient patient) {
		Session session=null;
		Long patientId=null;
		Transaction tx=null;
		try{
			session= getHibernateUtils.getHibernateUtlis().OpenSession();
			tx=session.beginTransaction();
			session.saveOrUpdate(patient);
			patientId=patient.getPatientId();
			session.flush();
	        session.clear();
			tx.commit();
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			
			}
		return patientId;
	}
	/*@Override
	@Transactional
	public Long updatePatientHistory(OpdPatientHistory opdPatientHistory) {
		Session session=null;
		Long patientHistoryId=null;
		//Transaction tx=null;
		 
			session= getHibernateUtils.getHibernateUtlis().OpenSession();
			//tx=session.beginTransaction();
			session.saveOrUpdate(opdPatientHistory);
			patientHistoryId=opdPatientHistory.getOpdPatientHistoryId();
			session.flush();
	        session.clear();
			//tx.commit();
			
		 
		return patientHistoryId;
	}*/

	@Override
	public Long updateOpdPatientDetail(OpdPatientDetail opdPatientDetail) {
		Long opdPatientDetailId=null;
		Session session=null;
		Transaction tx=null;
		 
			session= getHibernateUtils.getHibernateUtlis().OpenSession();
			session.saveOrUpdate(opdPatientDetail);
			opdPatientDetailId=opdPatientDetail.getOpdPatientDetailsId();
			session.flush();
	        session.clear();
		 
		return opdPatientDetailId;
	}

	
/*	@Override
	public List<OpdPatientHistory> getPatientHistoryList(Long visitId) {
		List<OpdPatientHistory>opdPatientHistoryList=null;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			opdPatientHistoryList =session.createCriteria(OpdPatientHistory.class).add(Restrictions.eq("visitId", visitId)).list();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return opdPatientHistoryList;
	}*/

	@Override
	public PatientPrescriptionDt getMasStoreItemByPatientPrecriptionDtId(Long patientPrecriptionDtId) {
		PatientPrescriptionDt patientPrescriptionDt=null;
		//try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			
			patientPrescriptionDt=(PatientPrescriptionDt) session.get(PatientPrescriptionDt.class, patientPrecriptionDtId);
		/*
		 * } catch(Exception e) { e.printStackTrace(); } finally {
		 * //getHibernateUtils.getHibernateUtlis().CloseConnection(); }
		 */
		return patientPrescriptionDt;
		
	}
	
	@Override
	public Long saveOrUpdatePatientPrecriptionDt(PatientPrescriptionDt patientPrescriptionDt) {
		Session session=null;
		Long patientPrecriptiondtId=null;
		//Transaction tx=null;
		//try{
			session= getHibernateUtils.getHibernateUtlis().OpenSession();
			//tx=session.beginTransaction();
			session.saveOrUpdate(patientPrescriptionDt);
			patientPrecriptiondtId=patientPrescriptionDt.getPrescriptionDtId();
			session.flush();
	        session.clear();
			//tx.commit();
			
		/*
		 * } catch(Exception e) { e.printStackTrace(); } finally {
		 * //getHibernateUtils.getHibernateUtlis().CloseConnection();
		 * 
		 * }
		 */
		return patientPrecriptiondtId;
	}

	@Override
	public List<Object[]> getReferralPatientDtList(Long opdPatientDetailId) {
		 List<Object[]>listReferralPatientDt=null;
		 try {
			 Session session=getHibernateUtils.getHibernateUtlis().OpenSession();
			 StringBuilder sbQuery = new StringBuilder();
			 
			    sbQuery.append(" select masEmpHos.EMPANELLED_HOSPITAL_ID,masEmpHos.EMPANELLED_HOSPITAL_NAME,masDepart.DEPARTMENT_ID, " );
			    sbQuery.append(" masDepart.DEPARTMENT_NAME,masIcd.ICD_ID,masIcd.ICD_NAME,refdt.instruction,refdt.REFREAL_DT_ID,refhd.REFREAL_HD_ID,refdt.EXT_DEPARTMENT,masIcd.ICD_CODE,refhd.REFERRAL_NOTE,refdt.FINAL_NOTE,refhd.REFERRAL_INI_DATE,refdt.INT_DEPARTMENT_ID,refhd.INT_HOSPITAL_ID,refhd.DOCTOR_NOTE from ");
			    sbQuery.append( REFERRAL_PATIENT_DT+ " refdt  join " +REFERRAL_PATIENT_HD+ " refhd on refhd.REFREAL_HD_ID=refdt.REFREAL_HD_ID "); 
			    sbQuery.append(" left join "+MAS_EMPANELLED_HOSPITAL+ " masEmpHos on masEmpHos.EMPANELLED_HOSPITAL_ID=refhd.EXT_HOSPITAL_ID left join "+MAS_ICD+ " masIcd on masIcd.ICD_ID=refdt.DIAGNOSIS_ID ");
			    sbQuery.append(" left join "+MAS_DEPARTMENT+" masDepart on masDepart.DEPARTMENT_ID =refdt.INT_DEPARTMENT_ID ");
			    sbQuery.append(" where refhd.OPD_PATIENT_DETAILS_ID =:OPD_PATIENT_DETAILS_ID  ORDER BY  refdt.REFREAL_HD_ID Asc");
			    Query query = session.createSQLQuery(sbQuery.toString());

			    query.setParameter("OPD_PATIENT_DETAILS_ID", opdPatientDetailId);

			    listReferralPatientDt = query.list();
		 }
		 catch(Exception e) {
			 e.printStackTrace();
		 }
		 finally {
			 getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		 
		 return listReferralPatientDt;
	}
	

	
	@Override
	public ReferralPatientHd getPatientReferalHdByExtHospitalId(Long extHospitalId,Long opdPatientDetailId,String referalFlagValue) {
		ReferralPatientHd referralPatientHd=null;
		List<ReferralPatientHd>listReferralPatientHd=null;
		//Transaction tx=null;
		//try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			//tx=session.beginTransaction();
			Criterion cr1=null;
			if(StringUtils.isNotEmpty(referalFlagValue)&& referalFlagValue.equalsIgnoreCase("I")) {
				cr1=Restrictions.eq("intHospitalId", extHospitalId);
			}
			else {
				cr1=Restrictions.eq("extHospitalId", extHospitalId);
			}
			Criteria criteria = session.createCriteria(ReferralPatientHd.class).
					add(cr1)
					.add(Restrictions.eq("opdPatientDetailsId", opdPatientDetailId)) ;
			listReferralPatientHd=criteria.list();
			
			if(CollectionUtils.isNotEmpty(listReferralPatientHd)) {
				referralPatientHd=listReferralPatientHd.get(0);
			}
			// tx.commit();
		/*
		 * } catch(Exception e) { e.printStackTrace(); } finally {
		 * getHibernateUtils.getHibernateUtlis().CloseConnection(); }
		 */
		return referralPatientHd;
	}
	
	@Override
	public Long saveOrUpdateReferalDt(ReferralPatientDt referralPatientDt) {
		Session session=null;
		Long patientReferalDtId=null;
		/*
		 * Transaction tx=null; try{
		 */
			session= getHibernateUtils.getHibernateUtlis().OpenSession();
		//	tx=session.beginTransaction();
			session.saveOrUpdate(referralPatientDt);
			patientReferalDtId=referralPatientDt.getRefrealDtId();
			session.flush();
	        session.clear();
			//tx.commit();
			
		/*
		 * } catch(Exception e) { e.printStackTrace(); } finally {
		 * getHibernateUtils.getHibernateUtlis().CloseConnection();
		 * 
		 * }
		 */return patientReferalDtId;
	}
	
	@Override
	public Long saveOrUpdateReferalHd(ReferralPatientHd referralPatientHd) {
		Session session=null;
		Long patientReferalHdId=null;
		//Transaction tx=null;
		//try{
			session= getHibernateUtils.getHibernateUtlis().OpenSession();
			//tx=session.beginTransaction();
			session.saveOrUpdate(referralPatientHd);
			patientReferalHdId=referralPatientHd.getRefrealHdId();
			session.flush();
	        session.clear();
			//tx.commit();
			
		/*
		 * } catch(Exception e) { e.printStackTrace(); } finally {
		 * getHibernateUtils.getHibernateUtlis().CloseConnection();
		 * 
		 * }
		 */
		return patientReferalHdId;
	}
	@Transactional
	public String  deleteInvestigatRow(Long dgOrderDt, String flag) {
		Session session = null;
		String status="";
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Object object = null;
			if (flag.equalsIgnoreCase("1")) {
				object = session.get(DgOrderdt.class, dgOrderDt);
				DgOrderdt dgOrderdt = (DgOrderdt) object;
			
				if (dgOrderdt != null) {
					//session.delete(dgOrderdt);
					DgOrderhd dgOrderHd=dgOrderdt.getDgOrderHd();
					
					status=deleteObject("DgOrderdt","orderdtId",  dgOrderdt.getOrderdtId());
					
					List<Long>listDgOrderdt=getDgOrderDtByDgOrderHtId(dgOrderHd.getOrderhdId());
					
					if(CollectionUtils.isEmpty(listDgOrderdt)  && dgOrderHd!=null) {
						status=deleteObject("DgOrderhd","orderhdId",  dgOrderHd.getOrderhdId());
					}
				}
				
				
			}
			
			if (flag.equalsIgnoreCase("2")) {
				object = session.get(PatientPrescriptionDt.class, dgOrderDt);
				PatientPrescriptionDt patientPrescriptionDt = (PatientPrescriptionDt) object;
				if (patientPrescriptionDt != null) {
					//session.delete(patientPrescriptionDt);
					Long  patientPrescriptionHdId=patientPrescriptionDt.getPrescriptionHdId();
					status=deleteObject("PatientPrescriptionDt","prescriptionDtId",  patientPrescriptionDt.getPrescriptionDtId());
					
					List<Long>listDgOrderdt= getPatientPrescriptionHdId(patientPrescriptionHdId) ;
					if(CollectionUtils.isEmpty(listDgOrderdt) && patientPrescriptionHdId!=null) {
						status=deleteObject("PatientPrescriptionHd","prescriptionHdId", patientPrescriptionHdId);
					}
				}
			}
			
			if (flag.equalsIgnoreCase("3")) {
				object = session.get(ReferralPatientDt.class, dgOrderDt);
				
				ReferralPatientDt referralPatientDt = (ReferralPatientDt) object;

				Long referalPatientHd = referralPatientDt.getRefrealHdId();
				
				if (referralPatientDt != null) {
					//session.delete(referralPatientDt);
					ReferralPatientHd referralPatientHd= referralPatientDt.getReferralPatientHd(); 
					status=deleteObject("ReferralPatientDt","refrealDtId",  referralPatientDt.getRefrealDtId()); 
					List<Long>listReferalPatient=getReferralPatientHdId(referralPatientHd.getRefrealHdId());
					if(CollectionUtils.isEmpty(listReferalPatient) && referralPatientHd!=null) {
						status=deleteObject("ReferralPatientHd","refrealHdId",  referralPatientHd.getRefrealHdId());
					}
					//ReferralPatientHd referralPatientHd=getReferralPatientHdByRereralHdId(referalPatientHd);

					//session.delete(referralPatientHd);
					//status =deleteObject("ReferralPatientHd","refrealHdId",  referralPatientHd.getRefrealHdId());

				}
			}
			
			///////////////////////delete procedure////////////////////////////
			if (flag.equalsIgnoreCase("5")) {
				object = session.get(ProcedureDt.class, dgOrderDt);
				ProcedureDt procedureDt = (ProcedureDt) object;
				if (procedureDt != null) {
					Long  procedureHdId=procedureDt.getProcedureHdId();
					status=deleteObject("ProcedureDt","procedureDtId",  procedureDt.getProcedureDtId());
					
					List<Long>listProcedureDtId= getProcedureDtIdList(procedureHdId) ;
					if(CollectionUtils.isEmpty(listProcedureDtId) && procedureHdId!=null) {
						status=deleteObject("ProcedureHd","procedureHdId", procedureHdId);
					}
				}
			}
			
			
///////////////////////delete RmsId relate value////////////////////////////
	if (flag.equalsIgnoreCase("10")) {
		Criterion cr1=Restrictions.eq("ridcId", dgOrderDt);
		List<DgResultEntryDt>listDgResultEntryDt= dgResultEntryDtDao.findByCriteria(cr1);
		Long dgOrderHdId=null;
		Long dgResultHdId=null;
		List<Long>listDgOrderdt=new ArrayList<>();
		List<Long>listRidcId=new ArrayList<>();
		if(CollectionUtils.isNotEmpty(listDgResultEntryDt)) {
			if(listDgResultEntryDt.get(0).getDgResultEntryHd()!=null&& listDgResultEntryDt.get(0).getDgResultEntryHd().getDgOrderhd()!=null 
					&&listDgResultEntryDt.get(0).getDgResultEntryHd().getDgOrderhd().getOrderhdId()!=null) {
				
				dgOrderHdId=listDgResultEntryDt.get(0).getDgResultEntryHd().getDgOrderhd().getOrderhdId();
				
				dgResultHdId=listDgResultEntryDt.get(0).getDgResultEntryHd().getResultEntryId();
			
				for(DgResultEntryDt dgResultEntryDt:listDgResultEntryDt) {
				if(dgResultEntryDt.getOrderDtId()!=null)
				listDgOrderdt.add(dgResultEntryDt.getOrderDtId());
				if(dgResultEntryDt.getResultEntryDetailId()!=null) {
					listRidcId.add(dgResultEntryDt.getRidcId());
				}
			}
				deleteAllCommonRecord("DgResultEntryDt","ridcId",listRidcId,session);  
				deleteAllCommonRecord("DgOrderdt","orderdtId",listDgOrderdt,session); 
				int statuss=0;
				Criterion cr11=Restrictions.eq("resultEntryId", dgResultHdId);
				List<DgResultEntryDt>listDgResultEntryDtV=dgResultEntryDtDao.findByCriteria(cr11);
					if(CollectionUtils.isEmpty(listDgResultEntryDtV)) {
						List<Long>dgResultHdList=new ArrayList<>();
						
						 cr11=Restrictions.eq("orderHdId", dgOrderHdId);
						List<DgResultEntryHd>listDgResultEntryHd=dgResultEntryHdDao.findByCriteria(cr11);
						if(CollectionUtils.isNotEmpty(listDgResultEntryHd))
						for(DgResultEntryHd dgResultEntryHd:listDgResultEntryHd) {
							if(dgResultEntryHd.getResultEntryId()!=null)
								dgResultHdList.add(dgResultEntryHd.getResultEntryId());
							 
						}
						
						//dgResultHdList.add(dgResultHdId);
						statuss=deleteAllCommonRecord("DgResultEntryHd","resultEntryId",dgResultHdList,session); 
						
						
						
						Criterion cr111=Restrictions.eq("orderhdId", dgOrderHdId);
						List<DgOrderdt>listDgOrderdtV=dgOrderdtDao.findByCriteria(cr111);
						if(CollectionUtils.isEmpty(listDgOrderdtV)) {
							List<Long>dgOrderHdList=new ArrayList<>();
							dgOrderHdList.add(dgOrderHdId);
							statuss=deleteAllCommonRecord("DgOrderhd","orderhdId",dgOrderHdList,session);
						}
					}
					  status=""+statuss;
			}
		}			
		 
	}
	
/////////////////////// delete RmsId  Referal relate value////////////////////////////
		//	if (flag.equalsIgnoreCase("11")) {}	
			///////////////////////////////////////////////////////////////////////////////////////////////////////
			
			if (flag.equalsIgnoreCase("15")) {
				Criterion cr1 = Restrictions.eq("ridcId", dgOrderDt);
				List<PatientDocumentDetail> listPatientDocumentDetail = patientDocumentDetailDao.findByCriteria(cr1);
				PatientDocumentDetail patientDocumentDetail=null;				
				if (CollectionUtils.isNotEmpty(listPatientDocumentDetail)) {
					patientDocumentDetail = listPatientDocumentDetail.get(0);
					patientDocumentDetailDao.remove(patientDocumentDetail);
					int statuss = 0;
					status = "" + statuss;
				}
			}	
			
			

			if (flag.equalsIgnoreCase("600")) {
				List<Long> listOpdDisposalDetail = new ArrayList<>();
				listOpdDisposalDetail.add(dgOrderDt);
				int statuss = 0;
				statuss = deleteAllCommonRecord("OpdDisposalDetail", "disposalDetailsId", listOpdDisposalDetail,
						session);
					status = "" + statuss;
			}	
			
			
			if (flag.equalsIgnoreCase("601")) {
				object = session.get(DgOrderdt.class, dgOrderDt);
				DgOrderdt dgOrderdt = (DgOrderdt) object;
			
				if (dgOrderdt != null) {
					session.delete(dgOrderdt);
					DgOrderhd dgOrderHd=dgOrderdt.getDgOrderHd();
					
					status=deleteObject("DgOrderdt","orderdtId",  dgOrderdt.getOrderdtId());
					
					List<Long>listDgOrderdt=getDgOrderDtByDgOrderHtId(dgOrderHd.getOrderhdId());
					
					if(CollectionUtils.isEmpty(listDgOrderdt)  && dgOrderHd!=null) {
						status=deleteObject("DgOrderhd","orderhdId",  dgOrderHd.getOrderhdId());
					}
				}
				
				
			}
			
			////////////////////////////////////////////////////////////////////////////////////
			if (flag.equalsIgnoreCase("701")) {
				object = session.get(OpdTemplateInvestigation.class, dgOrderDt);
				OpdTemplateInvestigation opdTemplateInvestigation = (OpdTemplateInvestigation) object;
				
				if (opdTemplateInvestigation != null) {
					//session.delete(opdTemplateInvestigation);
					status=deleteObject("OpdTemplateInvestigation","templateInvestigationId",  dgOrderDt);
					Long templateId=opdTemplateInvestigation.getTemplateId();
					List<Long>listDgOrderdt=getOpdTemplate(templateId);
					
					if(CollectionUtils.isEmpty(listDgOrderdt) &&  templateId!=null) {
						status=deleteObject("OpdTemplate","templateId",  templateId);
					}
				}
				
				
			}
            ////////////////////////////////////////////////////////////////////////////////////
			if (flag.equalsIgnoreCase("702")) {
				object = session.get(OpdTemplateTreatment.class, dgOrderDt);
				OpdTemplateTreatment opdTemplateTreatment = (OpdTemplateTreatment) object;

				if (opdTemplateTreatment != null) {
               //session.delete(opdTemplateInvestigation);
					status = deleteObject("OpdTemplateTreatment", "treatmentTemplateId", dgOrderDt);
					Long templateId = opdTemplateTreatment.getTemplateId();
					List<Long> listDgOrderdt = getOpdTemplate(templateId);

					if (CollectionUtils.isEmpty(listDgOrderdt) && templateId != null) {
						status = deleteObject("OpdTemplate", "templateId", templateId);
					}
				}

			}
	
					
			
			/////////////////////////////////////////////////////////////////////////////////////////////////

			///////////////////////////////////Family History/////////////////////////////////////////////////////////

			if (flag.equalsIgnoreCase("f1001")) {
				List<Long> listOfFamilyId = new ArrayList<>();
				listOfFamilyId.add(dgOrderDt);
				int statuss = 0;
				statuss = deleteAllCommonRecord("FamilyDetail", "familyId",listOfFamilyId,
						session);
					status = "" + statuss;
			}	
			
			//////////////////////////////////////opdTemplate/////////////////////////////////////////////////////////
			
			
			if (flag.equalsIgnoreCase("i1005")) {
				object = session.get(OpdTemplate.class, dgOrderDt);
				OpdTemplate opdTemplate = (OpdTemplate) object;

				if (opdTemplate != null) {
					 
					Long templateId = opdTemplate.getTemplateId();
					List<Long> listOpdTemplateInvestigation = getOpdTemplate(templateId);
					int statuss = 0;
					if (CollectionUtils.isNotEmpty(listOpdTemplateInvestigation) && templateId != null) {
						statuss = deleteAllCommonRecord("OpdTemplateInvestigation", "templateId", listOpdTemplateInvestigation,
								session);
						
					}
					if(statuss!=0) {
						List<Long>opdTemplateList=new ArrayList<>();
						opdTemplateList.add(templateId);
						statuss = deleteAllCommonRecord("OpdTemplate", "templateId", opdTemplateList,
								session);
					}
				}
				
			}
			if (flag.equalsIgnoreCase("t1006")) {
				object = session.get(OpdTemplate.class, dgOrderDt);
				OpdTemplate opdTemplate = (OpdTemplate) object;

				if (opdTemplate != null) {
					 
					Long templateId = opdTemplate.getTemplateId();
					List<Long> listOpdTemplateTreatment = getOpdTemplateTreatment(templateId);
					int statuss = 0;
					if (CollectionUtils.isNotEmpty(listOpdTemplateTreatment) && templateId != null) {
						statuss = deleteAllCommonRecord("OpdTemplateTreatment", "templateId", listOpdTemplateTreatment,
								session);
						
					}
					if(statuss!=0) {
						List<Long>opdTemplateList=new ArrayList<>();
						opdTemplateList.add(templateId);
						statuss = deleteAllCommonRecord("OpdTemplate", "templateId", opdTemplateList,
								session);
					}
				}
				
			}
			
			if (flag.equalsIgnoreCase("a1010")) {
				
				List<Long> listOfPatientAllergyId = new ArrayList<>();
				listOfPatientAllergyId.add(dgOrderDt);
				int statuss = 0;
				statuss = deleteAllCommonRecord("PatientAllergyDetail", "allergyId",listOfPatientAllergyId,
						session);
					status = "" + statuss;
			}
			
			if (flag.equalsIgnoreCase("Im1005")) {
				
				List<Long> listOfPatientAllergyId = new ArrayList<>();
				listOfPatientAllergyId.add(dgOrderDt);
				int statuss = 0;
				statuss = deleteAllCommonRecord("PatientImmunizationHistory", "immunizationId",listOfPatientAllergyId,
						session);
					status = "" + statuss;
			}
			
			/////////////////////////////////Patient service Detail/////////////////////////////////////////////////
				if (flag.equalsIgnoreCase("servi006")) {
				
				List<Long> listOfPatientAllergyId = new ArrayList<>();
				listOfPatientAllergyId.add(dgOrderDt);
				int statuss = 0;
				statuss = deleteAllCommonRecord("PatientServicesDetail", "serviceDetailsId",listOfPatientAllergyId,
						session);
					status = "" + statuss;
			}
			
			/////////////////////////////////////////////////////////////////////////////
				if (flag.equalsIgnoreCase("diseasi007")) {
					
					List<Long> listOfPatientAllergyId = new ArrayList<>();
					listOfPatientAllergyId.add(dgOrderDt);
					int statuss = 0;
					statuss = deleteAllCommonRecord("PatientDiseaseInfo", "diseaseInfoId",listOfPatientAllergyId,
							session);
						status = "" + statuss;
				}
				
				/////////////////////////////////////////////////////////////////////////
				if (flag.equalsIgnoreCase("beforeNewWarmi008")) {
					
					List<Long> listOfPatientAllergyId = new ArrayList<>();
					listOfPatientAllergyId.add(dgOrderDt);
					int statuss = 0;
					statuss = deleteAllCommonRecord("PatientDiseaseInfo", "diseaseInfoId",listOfPatientAllergyId,
							session);
						status = "" + statuss;
				}
				
				/////////////////////////////////////////////////////////////////////////
				if (flag.equalsIgnoreCase("40")) {
					
					List<Long> listOfPatientDiagnosisId = new ArrayList<>();
					listOfPatientDiagnosisId.add(dgOrderDt);
					int statuss = 0;
					statuss = deleteAllCommonRecord("DischargeIcdCode", "dischargeIcdCodeId",listOfPatientDiagnosisId,
							session);
						status = "" + statuss;
				}
				
				
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();

		}
		return status;
	}
	@Transactional
	public int deleteAllCommonRecord(String tableName,String columnName,List<Long>columnValue,Session session) {
		String hql = "delete from " + tableName + " WHERE " + columnName + " IN (:columnValue)";
		Query query = null;
		
			query = session.createQuery(hql);
			query.setParameterList("columnValue", columnValue);
			return query.executeUpdate();

	}
	
	
	
	
	
	public ReferralPatientHd getReferralPatientHdByRereralHdId(Long referalPatientHd) {
		ReferralPatientHd referralPatientHd=null;
		try{
			Session session= getHibernateUtils.getHibernateUtlis().OpenSession();
			referralPatientHd=(ReferralPatientHd) session.createCriteria(ReferralPatientHd.class).add(Restrictions.eq("refrealHdId", referalPatientHd)).uniqueResult();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		 finally {
			// getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return referralPatientHd;
	}

	@Override
	public Long saveOrUpdatePatientPrescriptionHd(PatientPrescriptionHd patientPrescriptionHd) {
		 	Session session=null;
			Long patientPrecriptionHdId=null;
			//Transaction tx=null;
			//try{
				session= getHibernateUtils.getHibernateUtlis().OpenSession();
				//tx=session.beginTransaction();
				session.saveOrUpdate(patientPrescriptionHd);
				patientPrecriptionHdId=patientPrescriptionHd.getPrescriptionHdId();
				session.flush();
		        session.clear();
				//tx.commit();
				
		/*
		 * } catch(Exception e) { e.printStackTrace(); } finally {
		 * //getHibernateUtils.getHibernateUtlis().CloseConnection();
		 * 
		 * }
		 */
			return patientPrecriptionHdId;
	}
	@Override
	public DgOrderhd getDgOrderhdByDgOrderhdId(Long dgOrderhdId) {
		DgOrderhd dgOrderhd=null;
		//try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			
			dgOrderhd=(DgOrderhd) session.get(DgOrderhd.class, dgOrderhdId);
		/*
		 * } catch(Exception e) { e.printStackTrace(); } finally {
		 * //getHibernateUtils.getHibernateUtlis().CloseConnection(); }
		 */
		return dgOrderhd;
	}

 
public String deleteObject(String  className,String columnName,Long columnValue) {
 String status="";
		try {

			String hql = "delete from " + className + " WHERE " + columnName + "=" + columnValue;
			Query query = null;
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			query = session.createQuery(hql);
			query.executeUpdate();
			status = ""+columnValue;
			session.clear();
			session.flush();
		} catch(Exception e) {
	  status="error"+000;
		e.printStackTrace();
	}
		/*finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}*/
	 return  status;
}
public PatientPrescriptionHd getPatientPrecriptionHdByVisitId(Long visitId) {
	PatientPrescriptionHd patientPrescriptionHd=null;
	List<PatientPrescriptionHd>listPatientPrescriptionHd=null;
	//try{
		Session session= getHibernateUtils.getHibernateUtlis().OpenSession();
		listPatientPrescriptionHd=  session.createCriteria(PatientPrescriptionHd.class).add(Restrictions.eq("visitId", visitId))
				.add(Restrictions.eq("status", "P")).list();
		if(CollectionUtils.isNotEmpty(listPatientPrescriptionHd)) {
			patientPrescriptionHd=listPatientPrescriptionHd.get(0);
		}
		/*
		 * } catch(Exception e) { e.printStackTrace(); } finally {
		 * //getHibernateUtils.getHibernateUtlis().CloseConnection(); }
		 */	 
	return patientPrescriptionHd;
}
@Override
public DgOrderhd getDgOrderHdByVisitId(Long visitId,Date orderDate) {
	DgOrderhd dgOrderhd=null;
	List<DgOrderhd>listDgOrderhd=null;
	//try{
		Session session= getHibernateUtils.getHibernateUtlis().OpenSession();
		listDgOrderhd=  session.createCriteria(DgOrderhd.class).add(Restrictions.eq("visitId", visitId)).add(Restrictions.eq("orderDate", orderDate)).list() ;
		if(CollectionUtils.isNotEmpty(listDgOrderhd)) {
			dgOrderhd=listDgOrderhd.get(0);
		}
		
		/*
		 * } catch(Exception e) { e.printStackTrace(); } finally { //
		 * getHibernateUtils.getHibernateUtlis().CloseConnection(); }
		 */
	return dgOrderhd;
}

@Override
public List<Object[]> getProcedureDtList(Long opdPatientDetailId,Long visitId) {
	 List<Object[]>listProcedureDt=null;
	 try {
		 Session session=getHibernateUtils.getHibernateUtlis().OpenSession();
		 StringBuilder sbQuery = new StringBuilder();
		 
		 sbQuery.append(" SELECT NURSING_NAME , MAS_FREQUENCY.FREQUENCY_NAME ,PROCEDURE_DT.NO_OF_DAYS ,'' As NURSING_REMARK,MAX(MAS_NURSING_CARE.NURSING_ID) as NURSING_ID,");
		 sbQuery.append(" PROCEDURE_DT.PROCEDURE_ID,PROCEDURE_HD.PROCEDURE_HD_ID,MAS_FREQUENCY.FREQUENCY_ID");
		 sbQuery.append(" ,PROCEDURE_HD.PROCEDURE_TYPE,MAX(PROCEDURE_DT.PROCEDURE_DT_ID)PROCEDURE_DT_ID,PROCEDURE_DT.remarks,PROCEDURE_DT.status FROM "+PROCEDURE_DT );
		 sbQuery.append(" LEFT OUTER JOIN "+PROCEDURE_HD+ " on PROCEDURE_DT.PROCEDURE_HD_ID=PROCEDURE_HD.PROCEDURE_HD_ID ");
		 sbQuery.append(" LEFT OUTER JOIN "+MAS_NURSING_CARE+ " on PROCEDURE_DT.PROCEDURE_ID=MAS_NURSING_CARE.NURSING_ID ");
		 sbQuery.append(" LEFT OUTER JOIN "+MAS_FREQUENCY+ " on MAS_FREQUENCY.FREQUENCY_ID=PROCEDURE_DT.FREQUENCY_ID ");
		 sbQuery.append(" WHERE PROCEDURE_HD.VISIT_ID=:visitId  and PROCEDURE_HD.OPD_PATIENT_DETAILS_ID=:opdPatientDetailId ");
		 
		 sbQuery.append(" GROUP BY NURSING_NAME , MAS_FREQUENCY.FREQUENCY_NAME ,PROCEDURE_DT.NO_OF_DAYS,");
		 sbQuery.append(" PROCEDURE_DT.PROCEDURE_ID,PROCEDURE_HD.PROCEDURE_HD_ID,MAS_FREQUENCY.FREQUENCY_ID ");
		 sbQuery.append(" ,PROCEDURE_HD.PROCEDURE_TYPE,PROCEDURE_DT.remarks,PROCEDURE_DT.status ");
		 
		 SQLQuery  query = session.createSQLQuery(sbQuery.toString());
		   query.addScalar("NURSING_NAME", new StringType());
		   query .addScalar("FREQUENCY_NAME", new StringType());
		   query.addScalar("NO_OF_DAYS", new LongType());
		    query.addScalar("NURSING_REMARK", new StringType());
		    query.addScalar("NURSING_ID", new LongType());
		    
		    query.addScalar("PROCEDURE_ID", new LongType());
		    query.addScalar("PROCEDURE_HD_ID", new LongType());
		    query.addScalar("FREQUENCY_ID", new LongType());
		    query.addScalar("PROCEDURE_TYPE", new StringType());
		    query.addScalar("PROCEDURE_DT_ID", new LongType());
		    query.addScalar("remarks", new StringType());
		    query.addScalar("status", new StringType());
		    query.setParameter("visitId", visitId);
		    query.setParameter("opdPatientDetailId", opdPatientDetailId);
		    
		    listProcedureDt = query.list();
	 }
	 catch(HibernateException e) {
		 e.printStackTrace();
	 }
	 finally {
		 getHibernateUtils.getHibernateUtlis().CloseConnection();
	 }
	 return listProcedureDt;
}


/*@Override
public String deleteObesityMark(OpdObesityHd opdObesityHd) {
	Transaction trans=null;
	Session session=null;
	
	try {
		session=getHibernateUtils.getHibernateUtlis().OpenSession();
	}
	catch(Exception e) {
		e.printStackTrace();
	}
	return "";
}*/

@Override
public String updateAndInsertDischargeICDCode(String [] icdCodeArray,Long visitId,Long patientId,Long opdPatientDetailId,String userId) {
	Session session=null;
	 
	String status="";
	 
		session=getHibernateUtils.getHibernateUtlis().OpenSession();
	 
		DischargeIcdCode dischargeIcdCode=null;
		if(icdCodeArray!=null && icdCodeArray.length>0) {
		for(String icg:icdCodeArray) {
			dischargeIcdCode=getDischargeIcdCodeByVisitAndPatientOrOpdPD(Long.parseLong(icg.trim()),visitId,patientId,opdPatientDetailId);
			if(dischargeIcdCode!=null) {
				dischargeIcdCode.setIcdId(Long.parseLong(icg.trim()));
			}
			else {
				dischargeIcdCode=new DischargeIcdCode();
				dischargeIcdCode.setIcdId(Long.parseLong(icg.trim()));
				dischargeIcdCode.setPatientId(patientId);
				dischargeIcdCode.setVisitId(visitId);
				dischargeIcdCode.setOpdPatientDetailsId(opdPatientDetailId);
			}
			Date date=new Date();
			dischargeIcdCode.setLastChgDate(new Timestamp(date.getTime()));
			if(StringUtils.isNotEmpty(userId)) {
				dischargeIcdCode.setLastChgBy(Long.parseLong(userId));
			}
			session.saveOrUpdate(dischargeIcdCode);
		}
		}
		status="success";
	 
	return status;
}


public DischargeIcdCode getDischargeIcdCodeByVisitAndPatientOrOpdPD(Long icdId, Long visitId,Long patientId,Long opdPatientDetailId) {
	Session session=null;
	DischargeIcdCode dischargeIcdCode=null;
	 
		session=getHibernateUtils.getHibernateUtlis().OpenSession();
		List<DischargeIcdCode> listDischargeIcdCode=session.createCriteria(DischargeIcdCode.class).add(Restrictions.eq("icdId", icdId))
				.add(Restrictions.eq("visitId", visitId)).add(Restrictions.eq("patientId", patientId)).add(Restrictions.eq("opdPatientDetailsId", opdPatientDetailId)).list();
		if(CollectionUtils.isNotEmpty(listDischargeIcdCode)) {
			dischargeIcdCode=listDischargeIcdCode.get(0);
		}
	return dischargeIcdCode;
	
}
public PatientSymptom getPatientSymByVisitAndPatientOrOpdPD(Long symptomId, Long visitId,Long patientId,Long opdPatientDetailId) {
	Session session=null;
	PatientSymptom dischargeIcdCode=null;
	 
		session=getHibernateUtils.getHibernateUtlis().OpenSession();
		List<PatientSymptom> listDischargeIcdCode=session.createCriteria(PatientSymptom.class).add(Restrictions.eq("symptomId", symptomId))
				.add(Restrictions.eq("visitId", visitId)).add(Restrictions.eq("patientId", patientId)).add(Restrictions.eq("opdPatientDetailId", opdPatientDetailId)).list();
		if(CollectionUtils.isNotEmpty(listDischargeIcdCode)) {
			dischargeIcdCode=listDischargeIcdCode.get(0);
		}
	return dischargeIcdCode;
	
}
@SuppressWarnings("unchecked")
@Override
public Map<String, Object> getMasIcdByVisitPatAndOpdPD(Long visitId,HashMap<String, String> jsonData) {
	Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	Map<String, Object> map = new HashMap<>();
	List<Object[]> searchList = null;
	int count = 0;
	
			
	try {	
		//String Query ="SELECT  VT.VISIT_DATE,ICD_DIAGNOSIS,patient_symptoms,VT.VISIT_ID,MD.DEPARTMENT_NAME,U.USER_NAME,MU.MMU_NAME FROM  visit VT LEFT OUTER JOIN  opd_Patient_Details OPD ON OPD.VISIT_ID=VT.VISIT_ID LEFT OUTER JOIN  mas_Department MD ON MD.DEPARTMENT_ID=VT.DEPARTMENT_ID LEFT OUTER JOIN  users U ON OPD.DOCTOR_ID=U.USER_ID LEFT OUTER JOIN  MAS_MMU MU ON MU.MMU_ID=VT.MMU_ID WHERE   VT.PATIENT_ID='"+patientId+"' AND Upper(VT.VISIT_STATUS)='C' ORDER BY VISIT_DATE DESC";
		String Query ="select di.discharge_icd_code_id,di.icd_id,mi.icd_name,mi.icd_code,mi.communicable_flag,mi.infectious_flag from discharge_icd_code di LEFT OUTER JOIN  mas_icd mi ON mi.icd_id=di.icd_id where visit_id='"+visitId+"' ";
		
	System.out.println(Query);
	if (Query != null) 
	{
		searchList = session.createSQLQuery(Query).list();
		
	} 
	else
	{
		System.out.println("No Record Found");
	}
	Query query = session.createSQLQuery(Query.toString());
	count = searchList.size();
     
 	 int listCount = searchList.size();
    
     searchList = query.list();

		map.put("count", count);
		map.put("list", searchList);
	}catch(Exception ex) {
		ex.printStackTrace();
	}finally {
		getHibernateUtils.getHibernateUtlis().CloseConnection();
	}
	return map;	
}

@Override
public String deleteChangeIcdCode(Long icdId,Long visitId,Long opdPatientDetailId,Long patient) {
	String status="";
	try {
		DischargeIcdCode dischargeIcdCode =getDischargeIcdCodeByVisitAndPatientOrOpdPD(icdId, visitId,patient,opdPatientDetailId);
		 if(dischargeIcdCode!=null) {
		deleteObject("DischargeIcdCode","dischargeIcdCodeId",dischargeIcdCode.getDischargeIcdCodeId());	
		status="succsess";
		 }
		 }
	catch(Exception e) {
		status="fail";
		e.printStackTrace();
	}
	finally {
		getHibernateUtils.getHibernateUtlis().CloseConnection();
	}
	return status;
}





@Override
	public String deleteForReferalTypeNo(List<ReferralPatientDt> listReferralPatientDt,
			List<ReferralPatientHd> listReferralPatientHd) {
		Session session = null;
		Transaction tx = null;
		String status = "";
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			for (ReferralPatientDt referralPatientDt : listReferralPatientDt) {
				session.delete(referralPatientDt);
			}
			for (ReferralPatientHd referralPatientHd : listReferralPatientHd) {
				session.delete(referralPatientHd);
			}
			
			status = "success";
			 

		} catch (Exception e) {
			status = "fail";
			e.printStackTrace();
		} finally {
			if(session!=null) {
				session.flush();
			}
			if(tx!=null)
			{
				tx.commit();
			}
			getHibernateUtils.getHibernateUtlis().CloseConnection();

		}
		return status;

	}

@Override
public Map<String, Object> getPatientReferalHdByVisitIdAndOpdPdAndPatient(Long patientId,
		Long opdPatientDetailId) {
	 Map<String,Object>mapObject=new HashMap<>();
	 Session session=null;
	 ReferralPatientDt referralPatientDt=null; 
	 List<ReferralPatientHd>listReferralPatientHd=null;
	 List<ReferralPatientDt>listReferralPatientDt=null;
	 List<Long>listRefer =null;
	 try
	 {
		 session=getHibernateUtils.getHibernateUtlis().OpenSession();
		 listReferralPatientHd= session.createCriteria(ReferralPatientHd.class).add(Restrictions.eq("opdPatientDetailsId", opdPatientDetailId))
				 .add(Restrictions.eq("patientId", patientId)).list();
		 if(CollectionUtils.isNotEmpty(listReferralPatientHd)) {
			 listRefer=new ArrayList<Long>();
		 for(ReferralPatientHd referralPatientHd :listReferralPatientHd) {
			 listRefer.add(referralPatientHd.getRefrealHdId());
		 }
		 
		 listReferralPatientDt =session.createCriteria(ReferralPatientDt.class).add(Restrictions.in("refrealHdId", listRefer)).list();
		 }
		 
		 mapObject.put("listReferralPatientHd",listReferralPatientHd);
		 mapObject.put("listReferralPatientDt",listReferralPatientDt);
	 }
	 catch(Exception e) {
		 e.printStackTrace();
	 }
	 return mapObject;
}


 
public List<Long> getDgOrderDtByDgOrderHtId(Long dgOrderHtId) {
	List<Long>	listDhgOrderDt=null;
	try {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		
		 Criteria cr=session.createCriteria(DgOrderdt.class).add(Restrictions.eq("orderhdId", dgOrderHtId));
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("orderdtId").as("orderdtId"));
			cr.setProjection(projectionList);
			listDhgOrderDt=cr.list();
				 
	}
	catch(Exception e) {
		e.printStackTrace();
	}
	 
	return listDhgOrderDt;
}

public List<Long> getPatientPrescriptionHdId(Long patientPrescriptionHdId) {
	List<Long>	listPatientPrescriptionDt=null;
	try {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		
		Criteria cr=session.createCriteria(PatientPrescriptionDt.class).add(Restrictions.eq("prescriptionHdId", patientPrescriptionHdId));
				ProjectionList projectionList = Projections.projectionList();
				projectionList.add(Projections.property("prescriptionDtId").as("prescriptionDtId"));
				cr.setProjection(projectionList);
				listPatientPrescriptionDt=cr.list();
	}
	catch(Exception e) {
		e.printStackTrace();
	}
	 
	return listPatientPrescriptionDt;
}


public List<Long> getReferralPatientHdId(Long referralPatientHdId) {
	List<Long>	listReferralPatientDt=null;
	try {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		
		Criteria cr=session.createCriteria(ReferralPatientDt.class).add(Restrictions.eq("refrealHdId", referralPatientHdId));
				ProjectionList projectionList = Projections.projectionList();
				projectionList.add(Projections.property("refrealDtId").as("refrealDtId"));
				cr.setProjection(projectionList);
				listReferralPatientDt=cr.list();
	}
	catch(Exception e) {
		e.printStackTrace();
	}
	 
	return listReferralPatientDt;
}



@Override
public ProcedureDt getProcedureDtByProcedureDtId(Long procedureDtId) {
	ProcedureDt procedureDt=null;

	//try {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();

		procedureDt=(ProcedureDt) session.createCriteria(ProcedureDt.class)
				.add(Restrictions.eq("procedureDtId", procedureDtId)).uniqueResult();
	/*}
	catch(Exception e) {
		e.printStackTrace();
	}
	finally {
		getHibernateUtils.getHibernateUtlis().CloseConnection();
	}*/
	return procedureDt;
}

@Override
public ProcedureHd getProcedureHdByProcedureHdId(Long procedureHdId) {
	ProcedureHd procedureHd=null;

	try {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();

		procedureHd=(ProcedureHd) session.createCriteria(ProcedureHd.class)
				.add(Restrictions.eq("procedureHdId", procedureHdId)).uniqueResult();
	}
	catch(Exception e) {
		e.printStackTrace();
	}
	finally {
		//getHibernateUtils.getHibernateUtlis().CloseConnection();
	}
	return procedureHd;
}

@Override
public ProcedureHd getProcedureHdByVisitIdAndType(Long visitId,String procedureType) {
	List<ProcedureHd> listProcedureHd=null;
	ProcedureHd procedureHd=null;
	//try {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();

		listProcedureHd= session.createCriteria(ProcedureHd.class)
				.add(Restrictions.eq("visitId", visitId)).add(Restrictions.eq("procedureType", procedureType)).list(); 
		if(CollectionUtils.isNotEmpty(listProcedureHd)) {
			procedureHd=	listProcedureHd.get(0);
		}
		/*
		 * } catch(Exception e) { e.printStackTrace(); } finally {
		 * getHibernateUtils.getHibernateUtlis().CloseConnection(); }
		 */
	return procedureHd;
}

@Override
public Long saveOrUpdateProcedureHd(ProcedureHd procedureHd) {
	Session session=null;
	Long procedureHdId=null;
	Transaction tx=null;
	//try{
		session= getHibernateUtils.getHibernateUtlis().OpenSession();
		//tx=session.beginTransaction();
		session.saveOrUpdate(procedureHd);
		procedureHdId=procedureHd.getProcedureHdId();
		session.flush();
        session.clear();
		//tx.commit();
		
		/*
		 * } catch(Exception e) { e.printStackTrace(); } finally {
		 * getHibernateUtils.getHibernateUtlis().CloseConnection();
		 * 
		 * }
		 */
	return procedureHdId;
}


@Override
public Long saveOrUpdateProcedureDd(ProcedureDt procedureDt) {
	Session session=null;
	Long procedureDtId=null;
	//Transaction tx=null;
	//try{
		session= getHibernateUtils.getHibernateUtlis().OpenSession();
		//tx=session.beginTransaction();
		session.saveOrUpdate(procedureDt);
		procedureDtId=procedureDt.getProcedureDtId();
		session.flush();
        session.clear();
		//tx.commit();
		
		/*
		 * } catch(Exception e) { e.printStackTrace(); } finally {
		 * getHibernateUtils.getHibernateUtlis().CloseConnection();
		 * 
		 * }
		 */
	return procedureDtId;
}

 
@Override
public PatientPrescriptionHd getPatientPrecriptionHdByPPHdId(Long patientPreciptionHdId) {
	PatientPrescriptionHd patientPrescriptionHd=null;
	List<PatientPrescriptionHd>listPatientPrescriptionHd=null;
	//try{
		Session session= getHibernateUtils.getHibernateUtlis().OpenSession();
		listPatientPrescriptionHd=  session.createCriteria(PatientPrescriptionHd.class).add(Restrictions.eq("prescriptionHdId", patientPreciptionHdId)).list();
		if(CollectionUtils.isNotEmpty(listPatientPrescriptionHd)) {
			patientPrescriptionHd=listPatientPrescriptionHd.get(0);
		}
		/*
		 * } catch(Exception e) { e.printStackTrace(); } finally { //
		 * getHibernateUtils.getHibernateUtlis().CloseConnection(); }
		 */
	 
	return patientPrescriptionHd;
}

public List<Long> getProcedureDtIdList(Long procedureHdId) {
	List<Long>	listprocedureDt=null;
	try {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		
		Criteria cr=session.createCriteria(ProcedureDt.class).add(Restrictions.eq("procedureHdId", procedureHdId));
				ProjectionList projectionList = Projections.projectionList();
				projectionList.add(Projections.property("procedureDtId").as("procedureDtId"));
				cr.setProjection(projectionList);
				listprocedureDt=cr.list();
	}
	catch(Exception e) {
		e.printStackTrace();
	}
	 
	return listprocedureDt;
}


@Override
public Map<String,Object> getTreatementDetailByPatientId(Long patientId,Integer pageNo,Long departmentId) {
	Transaction transation=null;
	 List<Object[]> listObject=null;
	 Map<String,Object>map=new HashMap<>();
	 int count=0;
	try {
		int pagingSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
	Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	transation=session.beginTransaction();
	/* StringBuilder sbQuery = new StringBuilder();
	    sbQuery.append(" select msi.nomenclature,msi.ITEM_ID,mf.FREQUENCY_ID,mf.FREQUENCY_NAME,mf.FREQUENCY_CODE," );
	    sbQuery.append("ppdt.NO_OF_DAYS,ppdt.dosage,pphd.PRESCRIPTION_HD_ID,ppdt.PRESCRIPTION_DT_ID,ppdt.DISP_STOCK,ppdt.total,"
	    		+ "ppdt.instruction,ppdt.STORE_STOCK, msu.STORE_UNIT_NAME,msi.PVMS_NO,pphd.OTHER_TREATMENT,ppdt.ITEM_STOP_BY,ppdt.ITEM_STOP_DATE,ppdt.ITEM_STOP_STATUS from ");
	    sbQuery.append(" PATIENT_PRESCRIPTION_HD pphd join PATIENT_PRESCRIPTION_DT ppdt on pphd.PRESCRIPTION_HD_ID=ppdt.PRESCRIPTION_HD_ID "); 
	    sbQuery.append(" join MAS_STORE_ITEM msi on msi.ITEM_ID =ppdt.ITEM_ID join MAS_STORE_UNIT msu on msu.STORE_UNIT_ID=msi.DISP_UNIT_ID ");
	    sbQuery.append("  join MAS_FREQUENCY mf on mf.FREQUENCY_ID=ppdt.FREQUENCY_ID");
	    sbQuery.append(" where pphd.PATIENT_ID =:PATIENT_ID ");
	    */
	Integer countDepa=getPatientDepartmentDiff(  patientId,  departmentId,  session);
	 Calendar cal = Calendar.getInstance();
	  
	 Date date = new Date();
	 cal.setTime(date);	 
	 
//		Date from = cal.getTime();
	 Date from = null;
		if(countDepa==2) {
			cal.add(Calendar.DATE, -1);
			
			 cal.set(Calendar.HOUR_OF_DAY, 23);
				cal.set(Calendar.MINUTE,59);
			from = cal.getTime();
	    	 System.out.println("date is before"+from);
	    	
		}
		else {
			 cal.set(Calendar.HOUR_OF_DAY, 23);
				cal.set(Calendar.MINUTE,59);
	    	  from = cal.getTime();
		}
		 
		 cal.add(Calendar.DATE, -90);
		 cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			
		 Date to = cal.getTime();
		 
	    Criteria cri=session.createCriteria(PatientPrescriptionDt.class,"patientPrescriptionDt").createAlias("patientPrescriptionHd", "patientPrescriptionHd")
	    		.createAlias("masStoreItem", "masStoreItem").createAlias("masStoreItem.masStoreUnit", "masStoreUnit")
	    		.createAlias("masFrequency", "masFrequency").createAlias("patientPrescriptionHd.visit", "visit")
	    		.createAlias("visit.masDepartment", "masDepartment").createAlias("visit.patient", "patient");
	    cri.add(Restrictions.eq("patientPrescriptionHd.patientId", patientId))
	    // .add(Restrictions.between("patientPrescriptionHd.prescriptionDate", from, to));
	    .add(Restrictions.ge("patientPrescriptionHd.prescriptionDate",to))
	    .add(Restrictions.le("patientPrescriptionHd.prescriptionDate",from));
	    ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("masStoreItem.nomenclature").as("nomenclature"))
		.add(Projections.property("masStoreItem.itemId").as("itemId"))
		.add(Projections.property("masFrequency.frequencyId").as("frequencyId"))
		
		.add(Projections.property("masFrequency.frequencyName").as("frequencyName"))
		.add(Projections.property("masFrequency.frequencyCode").as("frequencyCode"))
		
		.add(Projections.property("patientPrescriptionDt.noOfDays").as("noOfDays"))
		.add(Projections.property("patientPrescriptionDt.dosage").as("dosage"))
		
		
		.add(Projections.property("patientPrescriptionHd.prescriptionHdId").as("prescriptionHdId"))
		.add(Projections.property("prescriptionDtId").as("prescriptionDtId"))
		
		.add(Projections.property("patientPrescriptionDt.dispStock").as("dispStock"))
		.add(Projections.property("patientPrescriptionDt.total").as("total"))
		
		.add(Projections.property("patientPrescriptionDt.instruction").as("instruction"))
		.add(Projections.property("patientPrescriptionDt.storeStock").as("storeStock"))
		
		.add(Projections.property("masStoreUnit.storeUnitName").as("storeUnitName"))
		.add(Projections.property("masStoreItem.pvmsNo").as("pvmsNo"))
		
		//.add(Projections.property("patientPrescriptionHd.otherTreatment").as("otherTreatment"))
		.add(Projections.property("patientPrescriptionDt.itemStopBy").as("itemStopBy"))
		
		.add(Projections.property("patientPrescriptionDt.itemStopDate").as("itemStopDate"))
		.add(Projections.property("patientPrescriptionDt.itemStopStatus").as("itemStopStatus"))
		
		.add(Projections.property("patientPrescriptionHd.prescriptionDate").as("prescriptionDate"))
		.add(Projections.property("masDepartment.departmentName").as("departmentName"))
				.add(Projections.property("patientPrescriptionHd.doctorId").as("doctorId"))
				.add(Projections.property("masStoreItem.itemClassId").as("itemClassId"));
			//	.add(Projections.property("patientPrescriptionHd.doctorIds").as("lastName"));
		
		cri.setProjection(projectionList);
	     listObject = cri.list();
			count = listObject.size();
			cri = cri.setFirstResult(pagingSize * (pageNo - 1));
			cri = cri.setMaxResults(pagingSize);
			listObject = cri.list();
			map.put("count", count);
			map.put("list", listObject);
	     
	
	transation.commit();
	}
	catch(Exception e) {
		e.printStackTrace();
	}
	finally {
	getHibernateUtils.getHibernateUtlis().CloseConnection();
	
	}
	return map;
}

@Transactional
public Integer getPatientDepartmentDiff(Long patientId,Long departmentId,Session session) {
	Integer count=0;
	Criteria cr=null;
	cr=session.createCriteria(Visit.class) .add(Restrictions.eq("patientId", patientId));
	ProjectionList projectionList=Projections.projectionList();
	projectionList.add(Projections.property("departmentId"));
	cr.setProjection(projectionList);
	List<Long>deptList=cr.list();
	if(CollectionUtils.isNotEmpty(deptList)) {
        Predicate<Long> btf = n -> n != departmentId;
        List<Long>newList=new ArrayList<>();
		deptList.stream().filter(btf).forEach(e->newList.add(e));
		if(CollectionUtils.isNotEmpty(newList)) {
			count= 1;	
		}
		else {
			count= 2;
		}
	}
	return count;
}
@SuppressWarnings("unchecked")
@Override
public List<PatientPrescriptionDt> getPatientPrecriptionDtIdByPatientId(List<Long> patientPrecriptionDtIds) {
	List<PatientPrescriptionDt> listPatientPrescriptionDt=null;
	try {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		
		listPatientPrescriptionDt=  session.createCriteria(PatientPrescriptionDt.class)
				.add(Restrictions.in("prescriptionDtId", patientPrecriptionDtIds)).list();
	}
	catch(Exception e) {
		e.printStackTrace();
	}
	finally {
		//getHibernateUtils.getHibernateUtlis().CloseConnection();
	}
	return listPatientPrescriptionDt;
	
}

@Override
public List<MasStoreItem> getMasStoreItemByItemName(String masStoreName) {
	List<MasStoreItem> listMasStoreItem=null;
	//try {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		
		listMasStoreItem=session.createCriteria(MasStoreItem.class).add(Restrictions.eq("nomenclature", masStoreName).ignoreCase()).list();
		/*
		 * } catch(Exception e) { e.printStackTrace(); } finally {
		 * //getHibernateUtils.getHibernateUtlis().CloseConnection(); }
		 */
	return listMasStoreItem;
	
}


@Override
public Long updateMasStoreItem(MasStoreItem masStoreItem) {
	Session session=null;
	Long itemId=null;
	//Transaction tx=null;
	//try{
		session= getHibernateUtils.getHibernateUtlis().OpenSession();
		//tx=session.beginTransaction();
		session.saveOrUpdate(masStoreItem);
		itemId=masStoreItem.getItemId();
		session.flush();
        session.clear();
		//tx.commit();
		
		/*
		 * } catch(Exception e) { e.printStackTrace(); } finally {
		 * 
		 * //getHibernateUtils.getHibernateUtlis().CloseConnection();
		 * 
		 * }
		 */
	return itemId;
}
@Override
public List<PatientImpantHistory> getPatientHistoryImpByPatientId(Long patientId) {
	List<PatientImpantHistory>listPatientImpantHistory=null;
	try{
		Session session= getHibernateUtils.getHibernateUtlis().OpenSession();
		listPatientImpantHistory=  session.createCriteria(PatientImpantHistory.class).add(Restrictions.eq("patientId", patientId)).list();
		 
	}
	catch(Exception e) {
		e.printStackTrace();
	}
	finally {
		getHibernateUtils.getHibernateUtlis().CloseConnection();
	}
	 
	return listPatientImpantHistory;
}


@Override
public PatientImpantHistory getPatientImpantHistoryByPatientImplantHistoryId(Long patientImpantId) {
	PatientImpantHistory patientImpantHistory=null;

	//try {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();

		patientImpantHistory=(PatientImpantHistory) session.createCriteria(PatientImpantHistory.class)
				.add(Restrictions.eq("patientImpantId", patientImpantId)).uniqueResult();
		/*
		 * } catch(Exception e) { e.printStackTrace(); } finally {
		 * //getHibernateUtils.getHibernateUtlis().CloseConnection(); }
		 */
	return patientImpantHistory;
}



@Override
public Long saveOrUpdatePatientImplantHis(PatientImpantHistory patientImpantHistory) {
	Session session=null;
	Long patientImpantId=null;
	//Transaction tx=null;
	//try{
		session= getHibernateUtils.getHibernateUtlis().OpenSession();
		//tx=session.beginTransaction();
		session.saveOrUpdate(patientImpantHistory);
		patientImpantId=patientImpantHistory.getPatientImpantId();
		session.flush();
        session.clear();
		//tx.commit();
		
		/*
		 * } catch(Exception e) { e.printStackTrace(); } finally {
		 * 
		 * //getHibernateUtils.getHibernateUtlis().CloseConnection();
		 * 
		 * }
		 */
	return patientImpantId;
}
 
@Override
@Transactional
public DgOrderhd getDgOrderHdByHospitalIdAndPatient(Long hospitalId,Long patientId) {
	DgOrderhd dgOrderhd=null;
	List<DgOrderhd>listDgOrderhd=null;
	Session session=null;
	session= getHibernateUtils.getHibernateUtlis().OpenSession();
	try{
		Criteria crq1=session.createCriteria(DgOrderhd.class);
		Criterion cr1=Restrictions.eq("hospitalId", hospitalId);
		Criterion cr2=Restrictions.eq("patientId", patientId);
		Criterion cr3=Restrictions.isNull("visitId");
		
		crq1.add(cr1).add(cr2).add(cr3);
		//listDgOrderhd= findByCriteria(cr1,cr2,cr3);
		listDgOrderhd=crq1.list();
		if(CollectionUtils.isNotEmpty(listDgOrderhd)) {
			dgOrderhd=listDgOrderhd.get(0);
		}
		
	}
	catch(Exception e) {
		e.printStackTrace();
	}
	  
	return dgOrderhd;
}
@Override
@Transactional
public DgOrderhd getDgOrderHdByHospitalIdAndPatientAndVisitId(Long hospitalId,Long patientId,Long visitId) {
	DgOrderhd dgOrderhd=null;
	List<DgOrderhd>listDgOrderhd=null;
	Session session=null;
	session= getHibernateUtils.getHibernateUtlis().OpenSession();
	try{
		Criteria crq1=session.createCriteria(DgOrderhd.class);
		Criterion cr1=Restrictions.eq("hospitalId", hospitalId);
		Criterion cr2=Restrictions.eq("patientId", patientId);
		Criterion cr3=Restrictions.eq("visitId",visitId);
		
		crq1.add(cr1).add(cr2).add(cr3);
		//listDgOrderhd= findByCriteria(cr1,cr2,cr3);
		listDgOrderhd=crq1.list();
		if(CollectionUtils.isNotEmpty(listDgOrderhd)) {
			dgOrderhd=listDgOrderhd.get(0);
		}
		
	}
	catch(Exception e) {
		e.printStackTrace();
	}
	  
	return dgOrderhd;
}

@Override
public Long saveOrUpdateDgOrderHdInv(DgOrderhd dgOrderhd) {
	Session session=null;
	Long dgOrderhdId=null;
	try{
		session= getHibernateUtils.getHibernateUtlis().OpenSession();
		session.saveOrUpdate(dgOrderhd);
		dgOrderhdId=dgOrderhd.getOrderhdId();
		session.flush();
        session.clear();
	}
	catch(Exception e) {
		e.printStackTrace();
	}
	 
	return dgOrderhdId;
}




@Override
@Transactional
public DgOrderhd getDgOrderHdByOrderHdId(Long orderHdId ) {
	DgOrderhd dgOrderhd=null;
	List<DgOrderhd>listDgOrderhd=null;
	Session session=null;
	try{
		session= getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria cr11=session.createCriteria(DgOrderhd.class);
		Criterion cr1=Restrictions.eq("orderhdId", orderHdId);
		cr11.add(cr1) ;
		listDgOrderhd=cr11.list();
		if(CollectionUtils.isNotEmpty(listDgOrderhd)) {
			dgOrderhd=listDgOrderhd.get(0);
		}
		
	}
	catch(Exception e) {
		e.printStackTrace();
	}
	  
	return dgOrderhd;
}


@Override
@Transactional
public List<DgOrderdt> getDgOrderDtByOrderHdId(Long orderHdId ) {
	DgOrderhd dgOrderhd=null;
	List<DgOrderdt>listDgOrderdt=null;
	Session session=null;
	try{
		session= getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria cr11=session.createCriteria(DgOrderdt.class);
		Criterion cr1=Restrictions.eq("orderhdId", orderHdId);
		Criterion cr12=Restrictions.eq("orderStatus", "P");
		cr11.add(cr1) ;
		cr11.add(cr12) ;
		listDgOrderdt=cr11.list();
		 
		
	}
	catch(Exception e) {
		e.printStackTrace();
	}
	  
	return listDgOrderdt;
}


@Override
@Transactional
public Integer getDgResultEntryDtByOrderDtId(Long orderdtId ) {
	DgResultEntryDt dgResultEntryDt=null;
	List<DgResultEntryDt>listDgResultEntryDt=null;
	Session session=null;
	Integer countForResult=0;
	try{
		session= getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria cr11=session.createCriteria(DgResultEntryDt.class);
		Criterion cr1=Restrictions.eq("orderDtId", orderdtId);
		cr11.add(cr1) ;
		listDgResultEntryDt=cr11.list();
		if(CollectionUtils.isNotEmpty(listDgResultEntryDt)) {
			 for(DgResultEntryDt dgResultEntryDt1:listDgResultEntryDt) {
				 if(dgResultEntryDt1!=null && StringUtils.isNotEmpty(dgResultEntryDt1.getResult())) {
				 countForResult++;
			 }
			 }
				 
		}
		
	}
	catch(Exception e) {
		e.printStackTrace();
	}
	  
	return countForResult;
}

 
@Override
@Transactional
public List<DgOrderhd> getDgOrderHdtByVisitId(Long visitId ,String flage) {
	List<DgOrderhd>listDgOrderhd=null;
	Session session=null;
	try{
		session= getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria cr11=session.createCriteria(DgOrderhd.class);
		Criterion cr1=Restrictions.eq("visitId", visitId);
		Criterion cr12=null;
		if(StringUtils.isNotEmpty(flage))
		  cr12=Restrictions.eq("orderStatus", "C");
		cr11.add(cr1) ;
		if(cr12!=null)
		cr11.add(cr12);
		listDgOrderhd=cr11.list();
		 
	}
	catch(Exception e) {
		e.printStackTrace();
	}
	  
	return listDgOrderhd;
}




@Override
@Transactional
public Long saveOrUpdateFwcObjDetail(FwcObjDetail fwcObjDetail) {
	Session session=null;
	Long dgOrderhdId=null;
	try{
		session= getHibernateUtils.getHibernateUtlis().OpenSession();
		session.saveOrUpdate(fwcObjDetail);
		 
		session.flush();
        session.clear();
	}
	catch(Exception e) {
		e.printStackTrace();
	}
	 
	return dgOrderhdId;
}


@Override
@Transactional
public List<FwcObjDetail> getOpdPatientDetail(Long opdPatientDetailId) {
	List<FwcObjDetail> listFwcObjdetails=null;
	try {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		listFwcObjdetails =  session.createCriteria(FwcObjDetail.class).add(Restrictions.eq("opdPatientDetailsId", opdPatientDetailId)). list();
	}
	catch(Exception e) {
		e.printStackTrace();
	}
	finally {
		 
	}
	return listFwcObjdetails;
}

@Override
@Transactional
public List<PatientMedicalCat> getPatientMedicalCatByVisit(Long visitId ) {
	List<PatientMedicalCat>listPatientMedicalCat=null;
	Session session=null;
	try{
		session= getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria cr11=session.createCriteria(PatientMedicalCat.class);
		Criterion cr1=Restrictions.eq("visitId", visitId);
		Criterion cr12=null;
		 
		  cr12=Restrictions.eq("pMedFitFlag", "F");
		cr11.add(cr1) ;
		if(cr12!=null)
		cr11.add(cr12);
		listPatientMedicalCat=cr11.list();
		 
	}
	catch(Exception e) {
		e.printStackTrace();
	}
	  
	return listPatientMedicalCat;
}


@Transactional
public List<Long> getOpdTemplate(Long templateId) {
	List<Long>	listgetOpdTemplatet=null;
	try {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		
		 Criteria cr=session.createCriteria(OpdTemplateInvestigation.class).add(Restrictions.eq("templateId", templateId));
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("templateId").as("templateId"));
			cr.setProjection(projectionList);
			listgetOpdTemplatet=cr.list();
				 
	}
	catch(Exception e) {
		e.printStackTrace();
	}
	 
	return listgetOpdTemplatet;
}

@Transactional
public List<Long> getOpdTemplateTreatment(Long templateId) {
	List<Long>	listgetOpdTemplatet=null;
	try {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		
		 Criteria cr=session.createCriteria(OpdTemplateTreatment.class).add(Restrictions.eq("templateId", templateId));
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("templateId").as("templateId"));
			cr.setProjection(projectionList);
			listgetOpdTemplatet=cr.list();
				 
	}
	catch(Exception e) {
		e.printStackTrace();
	}
	 
	return listgetOpdTemplatet;
}
 

 
@Override
public String updateObject(String className,String columnSetValue,String columnWhereValue,Set<Long> columnValue,Session session) {
	 String status="";
			 	String stempId="";
				 if(CollectionUtils.isNotEmpty(columnValue)) {
				 for(Long l:columnValue) {
					 if(StringUtils.isEmpty(stempId)) {
						 stempId=""+l;
					 }
					 else {
						 stempId+=","+l;
					 }
				 }
				 }
				String hql = " update from "+className+" set "+columnSetValue+" WHERE "+columnWhereValue+" in("+stempId+")";
				Query query = null;
				query = session.createQuery(hql);
				query.executeUpdate();
				status = ""+columnValue;
				session.clear();
				session.flush();
			
		 return  status;
	}



@Transactional
@Override
public String getOrderNumberForVisit(Long visitId) {
	List<String>	listOrderNumber=null;
	String orderNumberVal="";
	try {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		
		 Criteria cr=session.createCriteria(DgOrderhd.class).add(Restrictions.eq("visitId", visitId));
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("orderNo").as("orderNo"));
			cr.setProjection(projectionList);
			listOrderNumber=cr.list();
	if(CollectionUtils.isNotEmpty(listOrderNumber)) {
		orderNumberVal=listOrderNumber.get(0);
		}			 
	}
	catch(Exception e) {
		e.printStackTrace();
	}
	 
	return orderNumberVal;
}
@Override
public MasStoreItem getMasStoreItemByItemId(Long itemId) {
	MasStoreItem msitDispUpdate =null;
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria dispUnitId = session.createCriteria(MasStoreItem.class);
		dispUnitId.add(Restrictions.eq("itemId", itemId));
		  msitDispUpdate = (MasStoreItem) dispUnitId.uniqueResult();	
		
	return msitDispUpdate;
	
}

@Override
public Long saveOrUpdateMasStoreItem(MasStoreItem masStoreItem) {
	 	Session session=null;
		Long dispUnitId=null;
			session= getHibernateUtils.getHibernateUtlis().OpenSession();
			session.saveOrUpdate(masStoreItem);
			dispUnitId=masStoreItem.getDispUnitId();
			session.flush();
	        session.clear();
			
	 
		return dispUnitId;
}

@Override
public void deleteDivenSection(List<Long> dgOrderdtList) {
	Session session=null;
		session= getHibernateUtils.getHibernateUtlis().OpenSession();
		deleteAllCommonRecord("DgOrderdt","orderdtId",dgOrderdtList,session); 
}

@Override
public Long saveOrUpdateDiverDgOrderdt(DiverOrderDet dgOrderDt) {
	Session session=null;
	Long dgOrderDtId=null;
	//Transaction tx=null;
	//try{
		session= getHibernateUtils.getHibernateUtlis().OpenSession();
		//tx=session.beginTransaction();
		session.saveOrUpdate(dgOrderDt);
		dgOrderDtId=dgOrderDt.getOrderDetId();
		//session.flush();
        //session.clear();
		//tx.commit();
		
	/*
	 * } catch(Exception e) { e.printStackTrace(); } finally {
	 * 
	 * //getHibernateUtils.getHibernateUtlis().CloseConnection();
	 * 
	 * }
	 */
	return dgOrderDtId;
}

@Override
@Transactional
public DgOrderhd getDgOrderHdByHospitalIdAndPatientAndVisitIdMe(Long hospitalId,Long patientId,Long visitId,Date from,Date to) {
	DgOrderhd dgOrderhd=null;
	List<DgOrderhd>listDgOrderhd=null;
	Session session=null;
	session= getHibernateUtils.getHibernateUtlis().OpenSession();
	try{
		Criteria crq1=session.createCriteria(DgOrderhd.class);
		Criterion cr1=Restrictions.eq("hospitalId", hospitalId);
		Criterion cr2=Restrictions.eq("patientId", patientId);
		Criterion cr3=Restrictions.eq("visitId",visitId);
		Criterion cr4=Restrictions.between("orderDate", from, to);
		crq1.add(cr1).add(cr2).add(cr3).add(cr4);
		 
		//listDgOrderhd= findByCriteria(cr1,cr2,cr3);
		listDgOrderhd=crq1.list();
		if(CollectionUtils.isNotEmpty(listDgOrderhd)) {
			dgOrderhd=listDgOrderhd.get(0);
		}
		
	}
	catch(Exception e) {
		e.printStackTrace();
	}
	  
	return dgOrderhd;
}

@Override
public ReferralPatientDt getReferralPatientDtByReferaldtId(Long referalDtId) {
	ReferralPatientDt referralPatientDt=null;
	 
	//try {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();

		referralPatientDt=(ReferralPatientDt) session.createCriteria(ReferralPatientDt.class)
				.add(Restrictions.eq("refrealDtId", referalDtId)).uniqueResult();
	/*
	 * } catch(Exception e) { e.printStackTrace(); } finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); }
	 */
	return referralPatientDt;
}


@Override
public String updateAndInsertpatientSympotnsValue(String [] patientSympotnsValueArray,Long visitId,Long patientId,Long opdPatientDetailId,String userId,Long mmuId) {
	Session session=null;
	 
	String status="";
	 
		session=getHibernateUtils.getHibernateUtlis().OpenSession();
	 
		PatientSymptom patientSymp=null;
		if(patientSympotnsValueArray!=null && patientSympotnsValueArray.length>0) {
		for(String icg:patientSympotnsValueArray) {
			patientSymp=getPatientSymByVisitAndPatientOrOpdPD(Long.parseLong(icg.trim()),visitId,patientId,opdPatientDetailId);
			if(patientSymp!=null) {
				patientSymp.setSymptomId(Long.parseLong(icg.trim()));
			}
			else {
				patientSymp=new PatientSymptom();
				patientSymp.setSymptomId(Long.parseLong(icg.trim()));
				patientSymp.setPatientId(patientId);
				patientSymp.setVisitId(visitId);
				patientSymp.setMmuId(mmuId);
				//patientSymp.setOpdPatientDetailsId(opdPatientDetailId);
			}
			Date date=new Date();
			patientSymp.setLastChgDate(new Timestamp(date.getTime()));
			if(StringUtils.isNotEmpty(userId)) {
				patientSymp.setLastChgBy(Long.parseLong(userId));
			}
			session.saveOrUpdate(patientSymp);
		}
		}
		status="success";
	 
	return status;
}



}





