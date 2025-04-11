package com.mmu.services.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mmu.services.entity.DgOrderdt;
import com.mmu.services.entity.DiverOrderDet;
@Repository
public interface DgOrderdtDao extends GenericDao<DgOrderdt, Long>{

	 

	Long saveOrUpdateDgOrderdtInv(DgOrderdt dgOrderdt);

	DgOrderdt getDgOrderdtByInvestigationIdAndOrderhdId(Long investigationId, Long orderhdId);

	DgOrderdt getDgOrderdtByOrderDtId(Long orderDtId);

	List<DgOrderdt> getDgOrderdtByDiverId(Long orderDetId);

	List<DiverOrderDet> getDiverDgOrderHdtByVisitIdForDiver(Long visitId, Long patientId);

	List<DgOrderdt> getDgOrderHdtByVisitIdForDiver(Long visitId, Long patientId);

	DiverOrderDet getDiverDgOrderdtByOrderDtId(Long diverOrderDtId);



}
