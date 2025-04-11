package com.mmu.services.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mmu.services.dao.PatientDocumentDetailDao;
import com.mmu.services.entity.PatientDocumentDetail;
@Repository
@Transactional
public class PatientDocumentDetailDaoImpl extends GenericDaoImpl<PatientDocumentDetail, Long> implements PatientDocumentDetailDao{

}
