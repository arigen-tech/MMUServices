package com.mmu.services.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.mmu.services.entity.DgOrderdt;
import com.mmu.services.entity.DgOrderhd;
import com.mmu.services.entity.DiverOrderDet;
import com.mmu.services.entity.FwcObjDetail;
import com.mmu.services.entity.MasStoreItem;
import com.mmu.services.entity.OpdPatientDetail;
import com.mmu.services.entity.Patient;
import com.mmu.services.entity.PatientImpantHistory;
import com.mmu.services.entity.PatientMedicalCat;
import com.mmu.services.entity.PatientPrescriptionDt;
import com.mmu.services.entity.PatientPrescriptionHd;
import com.mmu.services.entity.ProcedureDt;
import com.mmu.services.entity.ProcedureHd;
import com.mmu.services.entity.ReferralPatientDt;
import com.mmu.services.entity.ReferralPatientHd;
@Service
@Repository
public interface DgOrderhdDao extends GenericDao<DgOrderhd, Long> {
	
	public List<Object[]> getDgMasInvestigations(Long visitId,Long opdPatientDetailId);
	public List<Object[]> getTreatementDetail(Long visitId,Long opdPatientDetailId);
	public Map<String,Object> getDgOrderDtByOrderHdIdAndInvestigationId(List<String> orderDtIds, List<String> investigationIds );
	public DgOrderdt getDgOrderDtByDgOrderdtId(Long dgOrderDtId);
	public Long saveOrUpdateDgOrderdt(DgOrderdt dgOrderDt);
	public Long saveOrUpdateDgOrderHd(DgOrderhd dgOrderhd);
	
	public Long saveOrUpdatePatient(Patient patient);
	//public Long updatePatientHistory(OpdPatientHistory patient);
	public Long updateOpdPatientDetail(OpdPatientDetail opdPatientDetail) ;
	//public List<OpdPatientHistory>getPatientHistoryList(Long visitId);
	public PatientPrescriptionDt getMasStoreItemByPatientPrecriptionDtId(Long itemId);
	public Long saveOrUpdatePatientPrescriptionHd(PatientPrescriptionHd patientPrescriptionHd);
	
	public Long saveOrUpdatePatientPrecriptionDt(PatientPrescriptionDt patientPrescriptionDt);
	public List<Object[]>getReferralPatientDtList(Long opdPatientDetailId);
	public ReferralPatientDt getReferralPatientDtByReferaldtId(Long referalDtId);
	
	public ReferralPatientHd getPatientReferalHdByExtHospitalId(Long extHospitalId,Long opdPatientDetailId,String referFlagValue) ;
	public Long saveOrUpdateReferalDt(ReferralPatientDt referralPatientDt);
	public Long saveOrUpdateReferalHd(ReferralPatientHd referralPatientHd);
	public String deleteInvestigatRow(Long dgOrderDtId,String flag);
	public DgOrderhd getDgOrderhdByDgOrderhdId(Long dgOrderhdId);
	public String deleteObject(String  className,String columnName,Long columnValue);
	public PatientPrescriptionHd getPatientPrecriptionHdByVisitId(Long visitId);
	
	public DgOrderhd getDgOrderHdByVisitId(Long visitId, Date orderDate);
	
	public List<Object[]>getProcedureDtList(Long opdPatientDetailId,Long visitId);
		//public String deleteObesityMark(OpdObesityHd pdObesityHd);
	
	public  String updateAndInsertDischargeICDCode(String [] icdCodeArray,Long visitId,Long patientId,Long opdPatientDetailId,String userId);
	public Map<String, Object> getMasIcdByVisitPatAndOpdPD(Long patientId,HashMap<String, String> jsondata);
	public String deleteChangeIcdCode(Long  dischargeIcdCodeId,Long visitId,Long opdPatientDetailId,Long patientId);
	
	public String deleteForReferalTypeNo(List<ReferralPatientDt> listReferralPatientDt,
			List<ReferralPatientHd> listReferralPatientHd);
	public Map<String,Object> getPatientReferalHdByVisitIdAndOpdPdAndPatient(Long patientId,Long opdPatientDetailId) ;
	public ProcedureHd getProcedureHdByProcedureHdId(Long procedureHdId);
	public ProcedureDt getProcedureDtByProcedureDtId(Long procedureDtId);
	ProcedureHd getProcedureHdByVisitIdAndType(Long visitId,String procedureType);
	public Long saveOrUpdateProcedureHd(ProcedureHd procedureHd) ;
	public Long saveOrUpdateProcedureDd(ProcedureDt procedureDt);
	public PatientPrescriptionHd getPatientPrecriptionHdByPPHdId(Long patientPreciptionHdId);
	public Map<String,Object> getTreatementDetailByPatientId(Long patientId,Integer pageNo,Long departmentId) ;
	List<PatientPrescriptionDt> getPatientPrecriptionDtIdByPatientId(List<Long> patientPrecriptionDtIds);
	List<MasStoreItem> getMasStoreItemByItemName(String masStoreName);
	Long updateMasStoreItem(MasStoreItem masStoreItem);
	List<PatientImpantHistory> getPatientHistoryImpByPatientId(Long patientId);
	PatientImpantHistory getPatientImpantHistoryByPatientImplantHistoryId(Long patientImpantId);
	public Long saveOrUpdatePatientImplantHis(PatientImpantHistory patientImpantHistory);
	DgOrderhd getDgOrderHdByHospitalIdAndPatient(Long hospitalId, Long patientId);
	Long saveOrUpdateDgOrderHdInv(DgOrderhd dgOrderhd);
	DgOrderhd getDgOrderHdByOrderHdId(Long orderHdId);
	DgOrderhd getDgOrderHdByHospitalIdAndPatientAndVisitId(Long hospitalId, Long patientId, Long visitId);
	List<DgOrderdt> getDgOrderDtByOrderHdId(Long orderHdId);
	Integer getDgResultEntryDtByOrderDtId(Long orderdtId);
	List<DgOrderhd> getDgOrderHdtByVisitId(Long visitId,String flag);
	Long saveOrUpdateFwcObjDetail(FwcObjDetail fwcObjDetail);
	List<FwcObjDetail>  getOpdPatientDetail(Long opdPatientDetailId);
	List<PatientMedicalCat> getPatientMedicalCatByVisit(Long visitId);

	
	 
	String updateObject(String className, String columnSetValue, String columnWhereValue, Set<Long> columnValue,
			Session session);
	String getOrderNumberForVisit(Long visitId);
	 
	DgOrderhd getDgOrderHdByHospitalIdAndPatientAndVisitIdMe(Long hospitalId, Long patientId, Long visitId, Date from,
			Date to);
	MasStoreItem getMasStoreItemByItemId(Long itemId);
	Long saveOrUpdateMasStoreItem(MasStoreItem masStoreItem);
	public void deleteDivenSection(List<Long> dgOrderdtList);
	Long saveOrUpdateDiverDgOrderdt(DiverOrderDet dgOrderDt);
	String updateAndInsertpatientSympotnsValue(String[] patientSympotnsValueArray, Long visitId, Long patientId,
			Long opdPatientDetailId, String userId,Long mmuId);
		
	}

