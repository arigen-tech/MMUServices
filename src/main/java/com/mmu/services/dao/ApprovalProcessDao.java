package com.mmu.services.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import com.mmu.services.entity.AnmApmApproval;
import com.mmu.services.entity.VendorInvoicePayment;

@Repository
public interface ApprovalProcessDao {

	Long saveOrUpdateANMEntryDetails(HashMap<String, Object> jsondata);

	Map<String, List<AnmApmApproval>> getAnmOpdOfflineData(JSONObject jsondata);

	/* Map<String, Object> getIndentList(HashMap<String, Object> payload); */

}
