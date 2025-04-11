package com.mmu.services.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mmu.services.dao.ReferralPatientDtDao;
import com.mmu.services.entity.ReferralPatientDt;

@Repository
@Transactional
public class ReferralPatientDtDaoImpl extends GenericDaoImpl<ReferralPatientDt, Long> implements ReferralPatientDtDao {

}
