package com.mmu.services.dao;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.mmu.services.entity.FamilyDetail;

@Repository
public interface FamilyDetailsDAO {

	List<FamilyDetail> getFamilyDetailsByVisitId(HashMap<String, Object> jsondata);

	FamilyDetail getFamilyDetailsByFamilyId(Long familyId);

}
