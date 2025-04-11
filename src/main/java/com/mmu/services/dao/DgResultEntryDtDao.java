package com.mmu.services.dao;

import org.springframework.stereotype.Repository;

import com.mmu.services.entity.DgResultEntryDt;
@Repository
public interface DgResultEntryDtDao extends GenericDao<DgResultEntryDt, Long> {

	Long saveOrUpdateDgResultEntryDtInv(DgResultEntryDt dgResultEntryDt);

}
