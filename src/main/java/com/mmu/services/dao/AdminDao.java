package com.mmu.services.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mmu.services.entity.MasCity;
import com.mmu.services.entity.MasDepartment;
import com.mmu.services.entity.MasDoctorMapping;

@Repository
public interface AdminDao {

	public Map<String, Object> getDepartmentList(HashMap<String, Object> map);

	public List<MasDoctorMapping> getDoctorList(HashMap<String, Object> jsondata);

	public Map<String, Object> getDoctorRoaster(HashMap<String, Object> jsondata);

	public String submitDepartmentRoaster(List<String[]> allrowdata, String changeDate, String changeTime,
			Long changeBy, Long dept_id, Long hostpitalID);

	List<MasDepartment> getDepartmentListBasedOnDepartmentType(HashMap<String, Object> map);

	public List<MasCity> getAllCity(HashMap<String, Object> map);

}
