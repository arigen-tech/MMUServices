package com.mmu.services.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mmu.services.entity.MasDepartment;

@Repository
public interface WardDao {

	List<MasDepartment> getWardDepartment(Map<String, Object> inputJson);

}
