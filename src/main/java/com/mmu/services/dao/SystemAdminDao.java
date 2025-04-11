package com.mmu.services.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.mmu.services.entity.MasDesignation;
import com.mmu.services.entity.MasDesignationMapping;
import com.mmu.services.entity.MasEmployee;
import com.mmu.services.entity.MasHospital;
import com.mmu.services.entity.MasRank;
import com.mmu.services.entity.MasRole;
import com.mmu.services.entity.MasUnit;
import com.mmu.services.entity.OpdDisposalDetail;
import com.mmu.services.entity.Patient;
import com.mmu.services.entity.Users;


@Service
public interface SystemAdminDao {
	 List<MasHospital> getMasHospitalListForAdmin() ;
	  List<MasEmployee> getMasEmployeeForAdmin(String serviceNo) ;
	  List<MasDesignation> getAllMasDesigation() ;
	List<Users> getUserByServiceNoAndHospital(String serviceNo, Long hospitalId);
	Long saveOrUsers(Users users);
	Map<String,Object>   getAllUsers(Integer pageNo,String serviceNumber);
	Users getUserbyUserId(Long userId);
	Long activateDeActivatUser(Users users, String status);
	MasDesignationMapping getMassDesibyDesiId(Long designationId, Long unitId);
	Long saveAndUpdateMasDesignation(MasDesignationMapping masDesignationMapping);

	Map<String,Object>  getAllMassDesignation(Integer pageNo);
	 List<MasDesignationMapping> getMassDesignationIdMasId(Long parseLong);
	MasDesignationMapping getMassDesiByMassDesignationMappingId(Long masDesgiId);
	List<MasDesignationMapping> getMassDesiByUnitId(Long unitId);
	String getMasDesignationByDesignationId(String masDesigtionId);
	List<MasRole> getMasRole(HashMap<String, Object> jsondata);
	Map<String,Object>  getAllUsers(Long unitId,Long userId,Integer pageNo,String serviceNumber);
	String getMasRoleByRoleId(String masRoles);
	Users getUsersByUserId(Long userId);
	MasRole getMasRoleOfRoleName();
	List<MasEmployee> getMasEmployeeForAdmin(String serviceNo, MasHospital masHospital,String flag);
	MasDesignationMapping getMasDesignationMappingForUnitId(Long unitId);
	String getMasDesignationByDesignationIdNotAvial(String masDesigtionId, String masDesignationOfUser);
	String getMasRoleByRoleIdForMapping(String masRoles);
	List<MasHospital> getMasHospitalByUnitId(Long unitId);
	List<MasRole> getMasRole();
	MasUnit getMasHospitalByHospitalId(Long hospitalId);
	List<MasEmployee> getAllServiceByUnitId(HashMap<String, Object> jsondata);
	Users getUsersByUserIdAndHospitalId(Long userId, Long hospital);
	MasRank getRankByRankCode(String rankCode);
	List<Object[]> getUnitListForAdminByUnitode(HashMap<String, Object> jsondata);
	List<OpdDisposalDetail> getOpdDisposalDetailList(Long visitId);
	OpdDisposalDetail getOpdDisposalDetailObj(Long disposalDetailsId);
	List<OpdDisposalDetail> getOpdDisposalDetailListForVisitId(Long visitID, Long patientId, Long disposalId);
	void saveOrUpdateOpdDisposalDetail(OpdDisposalDetail opdDisposalDetail);
	void saveOrUpdateOpdDisposalDetailForOpd(OpdDisposalDetail opdDisposalDetail);
	List<MasHospital> getMasHospitalByUnitCode(String unitCode);
	//sList<Patient> getPatientForAdmin(String serviceNo);
}
