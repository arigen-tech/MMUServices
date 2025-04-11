package com.mmu.services.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mmu.services.dao.DgSampleCollectionDtDao;
import com.mmu.services.entity.DgSampleCollectionDt;
@Repository
@Transactional
public class DgSampleCollectionDtDaoImpl extends GenericDaoImpl<DgSampleCollectionDt, Long>  implements DgSampleCollectionDtDao{

}
