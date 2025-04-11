package com.mmu.services.dao;

import org.springframework.stereotype.Repository;

import com.mmu.services.entity.Patient;
@Repository
public interface PatientDao extends GenericDao<Patient, Long> {

}
