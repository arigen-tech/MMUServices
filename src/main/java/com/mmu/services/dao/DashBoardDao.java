package com.mmu.services.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import com.mmu.services.entity.StoreInternalIndentM;
import com.mmu.services.entity.StoreInternalIndentT;

@Repository
public interface DashBoardDao {

	public Map<String, Object> getDashBoardData(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	Map<String, Object> getHomePageData(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	//Map<String, List<StoreInternalIndentM>> getAllStoreInternalIndentM(JSONObject jsondata);

	Map<String, List<StoreInternalIndentT>> getAllStoreInternalIndentT(JSONObject jsondata);

}
