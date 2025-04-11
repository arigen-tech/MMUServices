package com.mmu.services.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mmu.services.entity.DgResultEntryHd;
@Repository
public interface DgResultEntryHdDao extends GenericDao<DgResultEntryHd, Long> {

	List<DgResultEntryHd> findByOrderIdAndVisitId(Long visitId, Long orderHdId);

	List<DgResultEntryHd> findByOrderIdAndPatientId(Long patientId, Long orderHdId);

	Long saveOrUpdateDgResultEntryHdInv(DgResultEntryHd dgResultEntryHd);

	DgResultEntryHd getDgResultEntryHdByInvestigationIdAndOrderhdId(Long subChargecodeId, Long mainChargecodeId,
			Long dgOrderHdId);
	
}
