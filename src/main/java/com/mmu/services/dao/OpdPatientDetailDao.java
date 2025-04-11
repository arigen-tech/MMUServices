package com.mmu.services.dao;

import org.springframework.stereotype.Repository;

import com.mmu.services.entity.MasAdministrativeSex;
import com.mmu.services.entity.MasEmployee;
import com.mmu.services.entity.MasMaritalStatus;
import com.mmu.services.entity.MasMedicalCategory;
import com.mmu.services.entity.MasReligion;
import com.mmu.services.entity.MasState;
import com.mmu.services.entity.MasTrade;
import com.mmu.services.entity.MasUnit;
import com.mmu.services.entity.OpdPatientDetail;
import com.mmu.services.entity.Patient;


@Repository
public interface OpdPatientDetailDao {
	
	public OpdPatientDetail getOpdPatientDetails(Long visitId);
	
	public OpdPatientDetail getOpdPatientDetailsByOpdPatientDetailId(Long opdPatientDetailId);
	public Patient getPatientByPatientId(Long patientId);
	public MasMaritalStatus getMasMaritalStatusByMaritalId(Long MaritalId,String masMaritalStatus);
	
	public MasAdministrativeSex getMasAdministrativeSexByGender(String gender);
	public MasMedicalCategory getMasMedicalCategoryByCategory(String category);
	
	//public OpdPatientHistory getOpdPatientHistoryByVisitId(Long visitId);
	public MasEmployee getMasEmployeeByFirstName(String empname);
	public MasReligion getMasReligionByReligion(String religion);
	public MasUnit getMasUnitByUnitId(String unitName);
	public MasState getMasStateByStateName(String stateName);
	public MasTrade getMasTradeByTradeName(String masTradeName);

	
	
	
	
	
}

