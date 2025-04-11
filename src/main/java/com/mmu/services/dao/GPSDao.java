package com.mmu.services.dao;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mmu.services.entity.MasDistrict;
import com.mmu.services.entity.Users;

@Repository
public interface GPSDao {

	Map<String, Object> getAllDistricts(HashMap<String, Object> jsondata);

	List<MasDistrict> getDistrictList(Map<String, Object> requestData);

	List<Object[]> getCampDetails(LocalDate today, Long mmuId);

	List<Users> getUserDetails(String mmu);

	Map<String, Object> getCampInfoAllDistrict(HashMap<String, Object> jsondata);

}
