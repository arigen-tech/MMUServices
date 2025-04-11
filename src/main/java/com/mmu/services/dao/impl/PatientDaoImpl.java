package com.mmu.services.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mmu.services.dao.PatientDao;
import com.mmu.services.entity.Patient;
@Repository
@Transactional
public class PatientDaoImpl extends GenericDaoImpl<Patient, Long> implements PatientDao{

}
