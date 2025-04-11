package com.mmu.services.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mmu.services.dao.WardDao;
import com.mmu.services.entity.MasDepartment;
import com.mmu.services.service.WardService;
import com.mmu.services.utils.HMSUtil;

@Service
public class WardServiceImpl implements WardService {

	@Autowired
	WardDao wardDao;
	
	@Override
	public Map<String, Object> getWardDepartment(Map<String, Object> inputJson) {
		try {
			List<MasDepartment> list = wardDao.getWardDepartment(inputJson);
			
			Map<String, Object> data = new HashMap<>();
			List<Map<Long, Object>> departmentList = new ArrayList<>();
			
			if (list.isEmpty()) {
				data.put("status", 0);
				data.put("doctorList", departmentList);
			} else {
				for (MasDepartment masDepartment : list) {
					Map<Long, Object> output = new HashMap<>();
					output.put(masDepartment.getDepartmentId(), HMSUtil.convertNullToEmptyString(masDepartment.getDepartmentName()));
					departmentList.add(output);
				}
				data.put("status", 1);
				data.put("departmentList", departmentList);
			}
			return data;
		} catch (Exception e) {
			e.printStackTrace();
		}
	return null;
	}
	}
