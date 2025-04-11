package com.mmu.services.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mmu.services.dao.ReferralPatientHdDao;
import com.mmu.services.entity.ReferralPatientHd;

@Repository
@Transactional
public class ReferralPatientHdDaoImpl extends GenericDaoImpl<ReferralPatientHd, Long> implements ReferralPatientHdDao{

}
