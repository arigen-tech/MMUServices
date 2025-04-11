package com.mmu.services.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mmu.services.dao.VisitDao;
import com.mmu.services.entity.Visit;
@Repository
@Transactional
public class VisitDaoImpl extends GenericDaoImpl<Visit, Long>  implements VisitDao {
	 
}
