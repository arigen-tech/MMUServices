package com.mmu.services.dao.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.ListUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mmu.services.dao.LPProcessDao;
import com.mmu.services.dao.StoresDAO;
import com.mmu.services.entity.MasDepartment;
import com.mmu.services.entity.MasHospital;
import com.mmu.services.entity.MasItemClass;
import com.mmu.services.entity.MasItemType;
import com.mmu.services.entity.MasMMU;
import com.mmu.services.entity.MasStoreFinancial;
import com.mmu.services.entity.MasStoreGroup;
import com.mmu.services.entity.MasStoreItem;
import com.mmu.services.entity.MasStoreSection;
import com.mmu.services.entity.MasStoreSupplier;
import com.mmu.services.entity.MasStoreSupplierType;
import com.mmu.services.entity.MasStoreUnit;
import com.mmu.services.entity.StoreBalanceM;
import com.mmu.services.entity.StoreBalanceT;
import com.mmu.services.entity.StoreBudgetaryM;
import com.mmu.services.entity.StoreBudgetaryT;
import com.mmu.services.entity.StoreCoInternalIndentM;
import com.mmu.services.entity.StoreCoInternalIndentT;
import com.mmu.services.entity.StoreDoInternalIndentM;
import com.mmu.services.entity.StoreDoInternalIndentT;
import com.mmu.services.entity.StoreGrnM;
import com.mmu.services.entity.StoreGrnT;
import com.mmu.services.entity.StoreInternalIndentM;
import com.mmu.services.entity.StoreInternalIndentT;
import com.mmu.services.entity.StoreIssueM;
import com.mmu.services.entity.StoreIssueT;
import com.mmu.services.entity.StoreItemBatchStock;
import com.mmu.services.entity.StorePoM;
import com.mmu.services.entity.StorePoT;
import com.mmu.services.entity.StoreQuotationM;
import com.mmu.services.entity.StoreQuotationT;
import com.mmu.services.entity.StoreSoM;
import com.mmu.services.entity.StoreSoT;
import com.mmu.services.entity.StoreStockTakingM;
import com.mmu.services.entity.StoreStockTakingT;
import com.mmu.services.entity.TempDirectReceivingForBackLp;
import com.mmu.services.entity.Users;
import com.mmu.services.hibernateutils.GetHibernateUtils;
import com.mmu.services.utils.HMSUtil;

@Repository
@Transactional
public class StoresDAOImpl implements StoresDAO {

	@Autowired
	GetHibernateUtils getHibernateUtils;

	@Autowired
	LPProcessDao lpProcessDoa;

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getStoreItemList(long itemGroupId, long itemTypeId, long itemSectionId, long itemCategoryId,
			long itemClassId, String pvmsNo, String nomenclturId) {

		List<Object[]> itemList = new ArrayList<Object[]>();
		List<Object[]> itemListEq = new ArrayList<Object[]>();
		List<Object[]> itemListilike = new ArrayList<Object[]>();
		List<Object[]> removalItemList = new ArrayList<Object[]>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();

			Criteria cr = session.createCriteria(MasStoreItem.class, "masStoreItem")
					.add(Restrictions.eq("status", "y").ignoreCase())
					.add(Restrictions.eq("edl", "y").ignoreCase());
			cr = cr.createAlias("masStoreItem.masStoreUnit1", "mh");

			if (!pvmsNo.isEmpty()) {
				pvmsNo = "%" + pvmsNo + "%";
				cr = cr.add(Restrictions.ilike("pvmsNo", pvmsNo));
			}

			if (!nomenclturId.isEmpty()) {
				nomenclturId = nomenclturId + "%";
				cr = cr.add(Restrictions.ilike("nomenclature", nomenclturId));
			}

			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("masStoreItem.itemId").as("itemId"));
			projectionList.add(Projections.property("masStoreItem.pvmsNo").as("pvmsNo"));
			projectionList.add(Projections.property("masStoreItem.nomenclature").as("nomenclature"));
			projectionList.add(Projections.property("mh.storeUnitId").as("itemUnitId"));
			projectionList.add(Projections.property("mh.storeUnitName").as("itemUnitName"));
			projectionList.add(Projections.property("masStoreItem.inactiveForEntry").as("inactiveForEntry"));
			cr.setProjection(projectionList);

			cr.setFirstResult(0);
			cr.setMaxResults(10);

			itemListEq = cr.list();
			itemList = itemListEq;

			if (itemListEq.size() >= 0 && itemListEq.size() < 10) {
				if (!nomenclturId.isEmpty()) {

					Criteria crilike = session.createCriteria(MasStoreItem.class, "masStoreItem")
							.add(Restrictions.eq("status", "y").ignoreCase())
							.add(Restrictions.eq("edl", "y").ignoreCase());
					crilike = crilike.createAlias("masStoreItem.masStoreUnit1", "mh");

					nomenclturId = "%" + nomenclturId;
					crilike = crilike.add(Restrictions.ilike("nomenclature", nomenclturId));

					ProjectionList projectionListilike = Projections.projectionList();
					projectionListilike.add(Projections.property("masStoreItem.itemId").as("itemId"));
					projectionListilike.add(Projections.property("masStoreItem.pvmsNo").as("pvmsNo"));
					projectionListilike.add(Projections.property("masStoreItem.nomenclature").as("nomenclature"));
					projectionListilike.add(Projections.property("mh.storeUnitId").as("itemUnitId"));
					projectionListilike.add(Projections.property("mh.storeUnitName").as("itemUnitName"));
					projectionListilike.add(Projections.property("masStoreItem.inactiveForEntry").as("inactiveForEntry"));
					crilike.setProjection(projectionListilike);

					crilike.setFirstResult(0);
					crilike.setMaxResults(10);

					itemListilike = crilike.list();

					for (int i = 0; i < itemListilike.size(); i++) {
						for (int j = 0; j < itemListEq.size(); j++) {
							if ((itemListilike.get(i)[0]).equals(itemListEq.get(j)[0])) {
								// itemListilike.remove(itemListilike.get(i));
								removalItemList.add(itemListilike.get(i));
							}
						}
					}
					itemListilike.removeAll(removalItemList);
					itemList = ListUtils.union(itemListEq, itemListilike);
					if (itemList.size() > 10) {
						itemList = itemList.subList(0, 10);
					}
				}

			}

		} catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return itemList;

	}

	@Override
	public boolean submitOpeningBalance(JSONObject formData) {
		boolean flag = false;

		JSONArray itemId = formData.getJSONArray("item");
		JSONArray batchNumber = formData.getJSONArray("batchNumber");
		JSONArray domDate = formData.getJSONArray("domDate");
		JSONArray doeDate = formData.getJSONArray("doeDate");
		JSONArray quantity = formData.getJSONArray("quantity");
		JSONArray unitRate = formData.getJSONArray("unitRate");
		JSONArray amount = formData.getJSONArray("amount");
		JSONArray supplierTypeId = formData.getJSONArray("vendorTypeId");
		JSONArray supplierId = formData.getJSONArray("vendorNameId");

		JSONArray departmentArr = formData.getJSONArray("department");
		JSONArray enterbyArr = formData.getJSONArray("userId");
		//String letterName=formData.get("uploadFile").toString();
		JSONArray invoiceN = formData.getJSONArray("invoiceNo");
		//JSONArray fileN = formData.getJSONArray("uploadFile");

		long departmentId = departmentArr.getLong(0);
		long enterbyId = enterbyArr.getLong(0);
		
		String invoiceNo=invoiceN.getString(0);
		String letterName=formData.get("uploadFile").toString();
        
		JSONArray BEDateArr = formData.getJSONArray("BEDate");
		String BEDate = BEDateArr.getString(0);

		/*
		 * JSONArray counterArr = formData.getJSONArray("count"); long counter =
		 * counterArr.getLong(0);
		 */

		JSONArray mmuArr = formData.getJSONArray("mmuId");
		String mmuId = mmuArr.getString(0);
		
		JSONArray cityIdArr = formData.getJSONArray("cityId");
		String cityId = cityIdArr.getString(0);
		
		JSONArray distIdArr = formData.getJSONArray("districtId");
		String districtId = distIdArr.getString(0);

		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Transaction tx = session.beginTransaction();
		try {
			StoreBalanceM balanceM = new StoreBalanceM();

			balanceM.setBalanceDate(HMSUtil.convertStringDateToUtilDateForDatabase(BEDate));

			if(mmuId != null && !mmuId.equals("") && !mmuId.equals("null") && !mmuId.equals("0")) {
				MasMMU masMMU = new MasMMU();
				masMMU.setMmuId(Long.parseLong(mmuId));
				balanceM.setMmuId(Long.parseLong(mmuId));
			}
			
			if(cityId != null && !cityId.equals("") && !cityId.equals("null") && !cityId.equals("0")) {
				balanceM.setCityId(Long.parseLong(cityId));
			}
			
			if(districtId != null && !districtId.equals("") && !districtId.equals("null") && !districtId.equals("0")) {
				balanceM.setDistrictId(Long.parseLong(districtId));
			}

			MasDepartment department = new MasDepartment();
			department.setDepartmentId(departmentId);
			balanceM.setMasDepartment(department);

			Users user = new Users();
			user.setUserId(enterbyId);
			balanceM.setUser(user);

			Users submitBy = new Users();
			submitBy.setUserId(enterbyId);
			balanceM.setSubmittedBy(submitBy);

			balanceM.setLastChgDate(new Timestamp(new Date().getTime()));

			balanceM.setStatus("O");
            balanceM.setInvoiceNo(invoiceNo);
            balanceM.setFileName(letterName);
			long balanceMID = (long) session.save(balanceM);

			if (balanceMID != 0) {

				for (int i = 0; i < itemId.length(); i++) {

					StoreBalanceT balanceT = new StoreBalanceT();

					StoreBalanceM balM = new StoreBalanceM();
					balM.setId(balanceMID);
					balanceT.setStoreBalanceM(balM);

					balanceT.setQty(quantity.getLong(i));
					if (!unitRate.get(i).toString().isEmpty()) {
						balanceT.setUnitRate(new BigDecimal(unitRate.get(i).toString()));
					}
					
					if (supplierTypeId.get(i) != null && !supplierTypeId.get(i).equals("")) {
						balanceT.setSupplierTypeId(Long.parseLong(supplierTypeId.get(i).toString()));
					}
					
					if (supplierId.get(i) != null && !supplierId.get(i).equals("")) {
						balanceT.setSupplierId(Long.parseLong(supplierId.get(i).toString()));
					}

					balanceT.setBatchNo(batchNumber.get(i).toString());
					balanceT.setExpiryDate(HMSUtil.convertStringDateToUtilDateForDatabase(doeDate.get(i).toString()));
					balanceT.setManufactureDate(
							HMSUtil.convertStringDateToUtilDateForDatabase(domDate.get(i).toString()));

					MasStoreItem item = new MasStoreItem();
					item.setItemId(itemId.getLong(i));
					balanceT.setMasStoreItem(item);

					Users lastChgBy = new Users();
					lastChgBy.setUserId(enterbyId);
					balanceT.setUser(lastChgBy);

					if (!amount.get(i).toString().isEmpty()) {
						balanceT.setTotalAmount(new BigDecimal(amount.get(i).toString()));
					}
					
					session.save(balanceT);
				}
			}
			tx.commit();
			flag = true;
		} catch (Exception e) {
			tx.rollback();
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return flag;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MasStoreGroup> getGroupList() {
		List<MasStoreGroup> masGroupList = new ArrayList<MasStoreGroup>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			masGroupList = session.createCriteria(MasStoreGroup.class).add(Restrictions.eq("status", "y").ignoreCase())
					.addOrder(Order.asc("groupName")).list();
		} catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return masGroupList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MasItemType> getItemTypeList() {
		List<MasItemType> masItemTypeList = new ArrayList<MasItemType>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			masItemTypeList = session.createCriteria(MasItemType.class).add(Restrictions.eq("status", "y").ignoreCase())
					.addOrder(Order.asc("itemTypeName")).list();
		} catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return masItemTypeList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MasItemClass> getItemClassList() {
		List<MasItemClass> masItemClassList = new ArrayList<MasItemClass>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			masItemClassList = session.createCriteria(MasItemClass.class)
					.add(Restrictions.eq("status", "y").ignoreCase()).addOrder(Order.asc("itemClassName")).list();
		} catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return masItemClassList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MasStoreSection> getStoreSectionList() {
		List<MasStoreSection> masStoreSectionList = new ArrayList<MasStoreSection>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			masStoreSectionList = session.createCriteria(MasStoreSection.class)
					.add(Restrictions.eq("status", "y").ignoreCase()).addOrder(Order.asc("sectionName")).list();
		} catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return masStoreSectionList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getOpeningBalanceWaitingList(String fromDate, String toDate, String mmuId, String cityId, String districtId,
			long departmentId, int pageNo) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<StoreBalanceM> waitingList = new ArrayList<StoreBalanceM>();
		List<Integer> totalMatches = new ArrayList<Integer>();
		int pageSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
		try {

			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria cr = session.createCriteria(StoreBalanceM.class);

			if (!fromDate.isEmpty()) {
				cr = cr.add(Restrictions.ge("balanceDate", HMSUtil.convertStringDateToUtilDateForDatabase(fromDate)));
			}
			if (!toDate.isEmpty()) {
				cr = cr.add(Restrictions.le("balanceDate", HMSUtil.convertStringDateToUtilDateForDatabase(toDate)));
			}

			cr = cr.add(Restrictions.eq("status", "O").ignoreCase())
					.add(Restrictions.eq("masDepartment.departmentId", departmentId));
					
			if(mmuId != null && !mmuId.equals("") && !mmuId.equals("null") && !mmuId.equals("0")) {
				cr = cr.add(Restrictions.eq("mmuId", Long.parseLong(mmuId)));
			}
			if(cityId != null && !cityId.equals("") && !cityId.equals("null") && !cityId.equals("0")) {
				cr = cr.add(Restrictions.eq("cityId", Long.parseLong(cityId)));
			}
			if(districtId != null && !districtId.equals("") && !districtId.equals("null") && !districtId.equals("0")) {
				cr = cr.add(Restrictions.eq("districtId", Long.parseLong(districtId)));
			}
			cr = cr.addOrder(Order.asc("id"));

			totalMatches = cr.list();
			cr.setFirstResult((pageSize) * (pageNo - 1));
			cr.setMaxResults(pageSize);

			waitingList = cr.list();

		} catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		map.put("totalMatches", totalMatches);
		map.put("waitingList", waitingList);
		return map;

	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getOpeningBalanceDetailsForApproval(long balanceM_headerId) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<StoreBalanceM> balanceMDetails = new ArrayList<StoreBalanceM>();
		List<StoreBalanceT> balanceTDetails = new ArrayList<StoreBalanceT>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();

			balanceMDetails = session.createCriteria(StoreBalanceM.class).add(Restrictions.eq("id", balanceM_headerId))
					.list();

			balanceTDetails = session.createCriteria(StoreBalanceT.class)
					.add(Restrictions.eq("storeBalanceM.id", balanceM_headerId)).list();

			map.put("balanceMDetails", balanceMDetails);
			map.put("balanceTDetails", balanceTDetails);

		} catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean submitOpeningBalanceDataForApproval(JSONObject formData) {
		boolean flag = false;

		JSONArray itemId = formData.getJSONArray("itemId");
		JSONArray batchNumber = formData.getJSONArray("batchNumber");
		JSONArray domDate = formData.getJSONArray("dom");
		JSONArray doeDate = formData.getJSONArray("doe");
		JSONArray quantity = formData.getJSONArray("qty");
		JSONArray unitRate = formData.getJSONArray("unitRate");
		JSONArray totalAmount = formData.getJSONArray("totalAmount");
		JSONArray supplierId = formData.getJSONArray("supplierId");
		JSONArray supplierTypeId = formData.getJSONArray("supplierTypeId");

		JSONArray mmuArr = formData.getJSONArray("mmuId");
		String mmuId = mmuArr.getString(0);
		
		JSONArray cityIdArr = formData.getJSONArray("cityId");
		String cityId = cityIdArr.getString(0);
		
		JSONArray districtIdArr = formData.getJSONArray("districtId");
		String districtId = districtIdArr.getString(0);

		JSONArray departmentArr = formData.getJSONArray("departmentId");
		long departmentId = departmentArr.getLong(0);

		JSONArray balanceMArr = formData.getJSONArray("balanceMId");
		long balanceMId = balanceMArr.getLong(0);

		JSONArray userArr = formData.getJSONArray("userId");
		long userId = userArr.getLong(0);

		JSONArray statusArr = formData.getJSONArray("actionId");
		String approvalStatus = statusArr.getString(0);

		JSONArray remarkArr = formData.getJSONArray("remarkId");
		String remark = remarkArr.getString(0);

		String tableRowId = "";
		String array_RowId[];

		JSONArray tableRowIdArr = formData.getJSONArray("hiddenValueCharge");

		tableRowId = tableRowIdArr.getString(0);
		array_RowId = tableRowId.split(",");

		Date currentDate = new Date();

		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Transaction tx = session.beginTransaction();
		try {
			StoreBalanceM balanceM = (StoreBalanceM) session.get(StoreBalanceM.class, balanceMId);

			if (approvalStatus.equalsIgnoreCase("A")) {

				balanceM.setStatus(approvalStatus);

				Users lastChgBy = new Users();
				lastChgBy.setUserId(userId);
				balanceM.setUser(lastChgBy);

				Users approvedBy = new Users();
				approvedBy.setUserId(userId);
				balanceM.setApprovedBy(approvedBy);

				balanceM.setRemarks(remark);

				balanceM.setLastChgDate(new Timestamp(new Date().getTime()));

				session.update(balanceM);

				if (tableRowId != "" && !tableRowId.isEmpty()) {
					for (int i = 0; i < array_RowId.length; i++) {
						StoreBalanceT balanceT = null;
						List<StoreBalanceT> balanceTList = new ArrayList<StoreBalanceT>();
						balanceTList = session.createCriteria(StoreBalanceT.class)
								.add(Restrictions.eq("id", Long.parseLong(array_RowId[i])))
								.add(Restrictions.eq("storeBalanceM.id", balanceMId)).list();

						if (balanceTList.size() > 0) {

							// this section will update the record
							balanceT = balanceTList.get(0);
							balanceT.setQty(quantity.getLong(i));
							if (!unitRate.get(i).toString().isEmpty()) {
								balanceT.setUnitRate(new BigDecimal(unitRate.get(i).toString()));
							}
							balanceT.setBatchNo(batchNumber.get(i).toString());
							if (!domDate.get(i).toString().isEmpty()) {
								balanceT.setManufactureDate(
										HMSUtil.convertStringDateToUtilDateForDatabase(domDate.get(i).toString()));
							}
							balanceT.setExpiryDate(
									HMSUtil.convertStringDateToUtilDateForDatabase(doeDate.get(i).toString()));

							balanceT.setUser(lastChgBy);

							if (!totalAmount.get(i).toString().isEmpty()) {
								balanceT.setTotalAmount(new BigDecimal(totalAmount.get(i).toString()));
							}
							if (!supplierId.get(i).toString().isEmpty()) {
								balanceT.setSupplierId(Long.parseLong(supplierId.get(i).toString()));
							}
							if (!supplierTypeId.get(i).toString().isEmpty()) {
								balanceT.setSupplierTypeId(Long.parseLong(supplierTypeId.get(i).toString()));
							}

							session.update(balanceT);

							if (!batchNumber.getString(i).isEmpty() && !doeDate.getString(i).isEmpty()
									&& quantity.getLong(i) > 0) {
								StoreItemBatchStock storeItemBacthStock = new StoreItemBatchStock();

								Criteria cr = session.createCriteria(StoreItemBatchStock.class)
										.add(Restrictions.eq("masDepartment.departmentId", departmentId))
										.add(Restrictions.eq("masStoreItem.itemId", itemId.getLong(i)))
										.add(Restrictions.eq("batchNo", batchNumber.getString(i)))
										.add(Restrictions.eq("expiryDate",HMSUtil.convertStringDateToUtilDateForDatabase(doeDate.getString(i))));


								if(mmuId != null && !mmuId.equals("") && !mmuId.equals("null") && !mmuId.equals("0")) {
									cr = cr.add(Restrictions.eq("mmuId", Long.parseLong(mmuId)));
								}
								if(cityId != null && !cityId.equals("") && !cityId.equals("null") && !cityId.equals("0")) {
									cr = cr.add(Restrictions.eq("cityId", Long.parseLong(cityId)));
								}
								if(districtId != null && !districtId.equals("") && !districtId.equals("null") && !districtId.equals("0")) {
									cr = cr.add(Restrictions.eq("districtId", Long.parseLong(districtId)));
								}
								List<StoreItemBatchStock> storeItemBatchList	=	cr.list();
								
								if (!storeItemBatchList.isEmpty() && storeItemBatchList.size() > 0) {
									storeItemBacthStock = storeItemBatchList.get(0);

									long closingBalanceQty = storeItemBatchList.get(0).getClosingStock() != null
											? storeItemBatchList.get(0).getClosingStock()
											: 0;
									closingBalanceQty = closingBalanceQty + quantity.getLong(i);
									storeItemBacthStock.setClosingStock(closingBalanceQty);

									/*
									 * long receivedQty =
									 * storeItemBatchList.get(0).getReceivedQty()!=null?storeItemBatchList.get(0).
									 * getReceivedQty():0; receivedQty = receivedQty+quantity.getLong(i);
									 * storeItemBacthStock.setReceivedQty(receivedQty);
									 */

									BigDecimal opBalanceAmount = storeItemBatchList.get(0)
											.getOpeningBalanceAmount() != null
													? storeItemBatchList.get(0).getOpeningBalanceAmount()
													: new BigDecimal("0");
									if (!totalAmount.get(i).toString().isEmpty()) {
										opBalanceAmount = opBalanceAmount
												.add(new BigDecimal(totalAmount.get(i).toString()));
									}
									storeItemBacthStock.setOpeningBalanceAmount(opBalanceAmount);
									
									if (!supplierId.get(i).toString().isEmpty()) {
										storeItemBacthStock.setSupplierId(Long.parseLong(supplierId.get(i).toString()));
									}
									if (!supplierTypeId.get(i).toString().isEmpty()) {
										storeItemBacthStock.setSupplierTypeId(Long.parseLong(supplierTypeId.get(i).toString()));
									}
									if(mmuId != null && !mmuId.equals("") && !mmuId.equals("null") && !mmuId.equals("0")) {
										storeItemBacthStock.setMmuId(Long.parseLong(mmuId));
									}
									if(cityId != null && !cityId.equals("") && !cityId.equals("null") && !cityId.equals("0")) {
										storeItemBacthStock.setCityId(Long.parseLong(cityId));
									}
									if(districtId != null && !districtId.equals("") && !districtId.equals("null") && !districtId.equals("0")) {
										storeItemBacthStock.setDistrictId(Long.parseLong(districtId)); 
									}
									
									long openingBalanceQty = storeItemBatchList.get(0).getOpeningBalanceQty() != null
											? storeItemBatchList.get(0).getOpeningBalanceQty()
											: 0;
									openingBalanceQty = openingBalanceQty + quantity.getLong(i);
									storeItemBacthStock.setOpeningBalanceQty(openingBalanceQty);

									storeItemBacthStock.setLastChgDate(new Timestamp(new Date().getTime()));
									storeItemBacthStock.setUser(lastChgBy);

									session.update(storeItemBacthStock);

								} else {

									MasStoreItem item = new MasStoreItem();
									item.setItemId(itemId.getLong(i)); // item.setItemId(Long.parseLong(itemId.get(i).toString()));
									storeItemBacthStock.setMasStoreItem(item);

									storeItemBacthStock.setBatchNo(batchNumber.get(i).toString());
									if (!supplierId.get(i).toString().isEmpty()) {
										storeItemBacthStock.setSupplierId(Long.parseLong(supplierId.get(i).toString()));
									}
									if (!supplierTypeId.get(i).toString().isEmpty()) {
										storeItemBacthStock.setSupplierTypeId(Long.parseLong(supplierTypeId.get(i).toString()));
									}

									if(mmuId != null && !mmuId.equals("") && !mmuId.equals("null") && !mmuId.equals("0")) {
										storeItemBacthStock.setMmuId(Long.parseLong(mmuId));
									}
									if(cityId != null && !cityId.equals("") && !cityId.equals("null") && !cityId.equals("0")) {
										storeItemBacthStock.setCityId(Long.parseLong(cityId));
									}
									if(districtId != null && !districtId.equals("") && !districtId.equals("null") && !districtId.equals("0")) {
										storeItemBacthStock.setDistrictId(Long.parseLong(districtId)); 
									}
									
									storeItemBacthStock.setOpeningBalanceQty(quantity.getLong(i));
									storeItemBacthStock.setClosingStock(quantity.getLong(i));

									if (!unitRate.getString(i).isEmpty()) {
										storeItemBacthStock.setMrp(new BigDecimal(unitRate.getString(i)));
									}
									if (!totalAmount.getString(i).isEmpty()) {
										storeItemBacthStock.setCostPrice(new BigDecimal(totalAmount.getString(i)));
										storeItemBacthStock
												.setOpeningBalanceAmount(new BigDecimal(totalAmount.get(i).toString()));
									}

									if (!domDate.get(i).toString().isEmpty()) {
										storeItemBacthStock.setManufactureDate(HMSUtil
												.convertStringDateToUtilDateForDatabase(domDate.get(i).toString()));
									}

									storeItemBacthStock.setExpiryDate(
											HMSUtil.convertStringDateToUtilDateForDatabase(doeDate.get(i).toString()));

									storeItemBacthStock.setOpeningBalanceDate(currentDate);

									storeItemBacthStock.setLastChgDate(new Timestamp(new Date().getTime()));

									MasDepartment dept = new MasDepartment();
									dept.setDepartmentId(departmentId);
									storeItemBacthStock.setMasDepartment(dept);

									storeItemBacthStock.setUser(lastChgBy);

									session.save(storeItemBacthStock);
								}
							}

						} else {
							// This section will insert new record in store_balance_t
							balanceT = new StoreBalanceT();

							StoreBalanceM balM = new StoreBalanceM();
							balM.setId(balanceMId);
							balanceT.setStoreBalanceM(balM);
							balanceT.setQty(quantity.getLong(i));

							if (!unitRate.get(i).toString().isEmpty()) {
								balanceT.setUnitRate(new BigDecimal(unitRate.get(i).toString()));
							}

							balanceT.setBatchNo(batchNumber.get(i).toString());
							balanceT.setExpiryDate(
									HMSUtil.convertStringDateToUtilDateForDatabase(doeDate.get(i).toString()));
							balanceT.setManufactureDate(
									HMSUtil.convertStringDateToUtilDateForDatabase(domDate.get(i).toString()));

							MasStoreItem item = new MasStoreItem();
							item.setItemId(itemId.getLong(i));
							balanceT.setMasStoreItem(item);
							balanceT.setUser(lastChgBy);

							if (!totalAmount.getString(i).isEmpty()) {
								balanceT.setTotalAmount(new BigDecimal(totalAmount.get(i).toString()));
							}
							if (!supplierId.get(i).toString().isEmpty()) {
								balanceT.setSupplierId(Long.parseLong(supplierId.get(i).toString()));
							}
							if (!supplierId.get(i).toString().isEmpty()) {
								balanceT.setSupplierTypeId(Long.parseLong(supplierTypeId.get(i).toString()));
							}
							session.save(balanceT);

							if (!batchNumber.getString(i).isEmpty() && !doeDate.getString(i).isEmpty()
									&& quantity.getLong(i) > 0) {
								StoreItemBatchStock storeItemBacthStock = new StoreItemBatchStock();

								Criteria cr = session.createCriteria(StoreItemBatchStock.class)
										.add(Restrictions.eq("masDepartment.departmentId", departmentId))
										.add(Restrictions.eq("masStoreItem.itemId", itemId.getLong(i)))
										.add(Restrictions.eq("batchNo", batchNumber.getString(i)))
										.add(Restrictions.eq("expiryDate",HMSUtil.convertStringDateToUtilDateForDatabase(doeDate.getString(i))));
								
								if(mmuId != null && !mmuId.equals("") && !mmuId.equals("null") && !mmuId.equals("0")) {
									cr = cr.add(Restrictions.eq("mmuId", Long.parseLong(mmuId)));
								}
								if(cityId != null && !cityId.equals("") && !cityId.equals("null") && !cityId.equals("0")) {
									cr = cr.add(Restrictions.eq("cityId", Long.parseLong(cityId)));
								}
								if(districtId != null && !districtId.equals("") && !districtId.equals("null") && !districtId.equals("0")) {
									cr = cr.add(Restrictions.eq("districtId", Long.parseLong(districtId)));
								}
								
								List<StoreItemBatchStock> storeItemBatchList =	cr.list();
								if (!storeItemBatchList.isEmpty() && storeItemBatchList.size() > 0) {
									storeItemBacthStock = storeItemBatchList.get(0);

									long closingBalanceQty = storeItemBatchList.get(0).getClosingStock() != null
											? storeItemBatchList.get(0).getClosingStock()
											: 0;
									closingBalanceQty = closingBalanceQty + quantity.getLong(i);
									storeItemBacthStock.setClosingStock(closingBalanceQty);

									/*
									 * long receivedQty =
									 * storeItemBatchList.get(0).getReceivedQty()!=null?storeItemBatchList.get(0).
									 * getReceivedQty():0; receivedQty = receivedQty+quantity.getLong(i);
									 * storeItemBacthStock.setReceivedQty(receivedQty);
									 */
									
									if(mmuId != null && !mmuId.equals("") && !mmuId.equals("null") && !mmuId.equals("0")) {
										storeItemBacthStock.setMmuId(Long.parseLong(mmuId));
									}
									if(cityId != null && !cityId.equals("") && !cityId.equals("null") && !cityId.equals("0")) {
										storeItemBacthStock.setCityId(Long.parseLong(cityId));
									}
									if(districtId != null && !districtId.equals("") && !districtId.equals("null") && !districtId.equals("0")) {
										storeItemBacthStock.setDistrictId(Long.parseLong(districtId)); 
									}
									
									BigDecimal opBalanceAmount = storeItemBatchList.get(0)
											.getOpeningBalanceAmount() != null
													? storeItemBatchList.get(0).getOpeningBalanceAmount()
													: new BigDecimal("0");
									opBalanceAmount = opBalanceAmount
											.add(new BigDecimal(totalAmount.get(i).toString()));
									storeItemBacthStock.setOpeningBalanceAmount(opBalanceAmount);
									
									if (!supplierId.get(i).toString().isEmpty()) {
										storeItemBacthStock.setSupplierId(Long.parseLong(supplierId.get(i).toString()));
									}
									if (!supplierId.get(i).toString().isEmpty()) {
										storeItemBacthStock.setSupplierTypeId(Long.parseLong(supplierTypeId.get(i).toString()));
									}

									long openingBalanceQty = storeItemBatchList.get(0).getOpeningBalanceQty() != null
											? storeItemBatchList.get(0).getOpeningBalanceQty()
											: 0;
									openingBalanceQty = openingBalanceQty + quantity.getLong(i);
									storeItemBacthStock.setOpeningBalanceQty(openingBalanceQty);

									storeItemBacthStock.setLastChgDate(new Timestamp(new Date().getTime()));
									storeItemBacthStock.setUser(lastChgBy);
									session.update(storeItemBacthStock);

								} else {

									MasStoreItem itemNewAdded = new MasStoreItem();
									itemNewAdded.setItemId(itemId.getLong(i)); // item.setItemId(Long.parseLong(itemId.get(i).toString()));
									storeItemBacthStock.setMasStoreItem(itemNewAdded);

									storeItemBacthStock.setBatchNo(batchNumber.get(i).toString());

									MasHospital masHospital = new MasHospital();

									storeItemBacthStock.setOpeningBalanceQty(quantity.getLong(i));
									storeItemBacthStock.setClosingStock(quantity.getLong(i));
									
									if (!supplierId.get(i).toString().isEmpty()) {
										storeItemBacthStock.setSupplierId(Long.parseLong(supplierId.get(i).toString()));
									}
									if (!supplierId.get(i).toString().isEmpty()) {
										storeItemBacthStock.setSupplierTypeId(Long.parseLong(supplierTypeId.get(i).toString()));
									}
									
									if(mmuId != null && !mmuId.equals("") && !mmuId.equals("null") && !mmuId.equals("0")) {
										storeItemBacthStock.setMmuId(Long.parseLong(mmuId));
									}
									if(cityId != null && !cityId.equals("") && !cityId.equals("null") && !cityId.equals("0")) {
										storeItemBacthStock.setCityId(Long.parseLong(cityId));
									}
									if(districtId != null && !districtId.equals("") && !districtId.equals("null") && !districtId.equals("0")) {
										storeItemBacthStock.setDistrictId(Long.parseLong(districtId)); 
									}

									if (!unitRate.getString(i).isEmpty()) {
										storeItemBacthStock.setMrp(new BigDecimal(unitRate.getString(i)));
									}
									if (!totalAmount.getString(i).isEmpty()) {
										storeItemBacthStock.setCostPrice(new BigDecimal(totalAmount.getString(i)));
										storeItemBacthStock
												.setOpeningBalanceAmount(new BigDecimal(totalAmount.get(i).toString()));
									}

									if (!domDate.get(i).toString().isEmpty()) {
										storeItemBacthStock.setManufactureDate(HMSUtil
												.convertStringDateToUtilDateForDatabase(domDate.get(i).toString()));
									}

									storeItemBacthStock.setExpiryDate(
											HMSUtil.convertStringDateToUtilDateForDatabase(doeDate.get(i).toString()));

									storeItemBacthStock.setOpeningBalanceDate(currentDate);

									storeItemBacthStock.setLastChgDate(new Timestamp(new Date().getTime()));

									MasDepartment dept = new MasDepartment();
									dept.setDepartmentId(departmentId);
									storeItemBacthStock.setMasDepartment(dept);

									storeItemBacthStock.setUser(lastChgBy);

									session.save(storeItemBacthStock);
								}
							}
						}

					}

				}

			} else if (approvalStatus.equalsIgnoreCase("R")) {
				// when case of Rejected
				balanceM.setStatus(approvalStatus);

				Users lastChgBy = new Users();
				lastChgBy.setUserId(userId);
				balanceM.setUser(lastChgBy);

				Users approvedBy = new Users();
				approvedBy.setUserId(userId);
				balanceM.setApprovedBy(approvedBy);

				balanceM.setRemarks(remark);

				balanceM.setLastChgDate(new Timestamp(new Date().getTime()));
				session.update(balanceM);

			}

			tx.commit();
			flag = true;
		} catch (Exception e) {
			flag = false;
			tx.rollback();
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return flag;
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public List<Object[]> generateStockStatusReport(String radioSelectValue, long itemId, long groupId, long sectionId,
			long mmuId, long deptId) {

		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		List<Object[]> objectList = new ArrayList<Object[]>();
		String qry = "";
		String query = "";

		try {

			if (groupId != 0) {
				query = query + " and mas_store_item.group_id = :group";
			}

			if (sectionId != 0) {
				query = query + " and mas_store_item.section_id = :section";
			}
			if (itemId != 0) {
				query = query + " and mas_store_item.item_id = :item";
			}
 
			
			query = query + " and store_item_batch_stock.CLOSING_STOCK>0";

			if (radioSelectValue.equalsIgnoreCase("D")) {
				qry = "select  sum(coalesce(store_item_batch_stock.received_qty,0)) as batch_stock_received_qty,"
						+ "sum(coalesce(store_item_batch_stock.issue_qty,0)) as batch_stock_issue_qty,"
						+ "sum(coalesce(store_item_batch_stock.opening_balance_qty,0)) as batch_stock_opning_balnce_qty,"
						+ "sum(store_item_batch_stock.CLOSING_STOCK) as balance_qty,"
						+ "mas_store_item.pvms_no,mas_store_item.nomenclature,store_item_batch_stock.batch_no,store_item_batch_stock.expiry_date, "
						+ "mas_store_unit.store_unit_name,store_item_batch_stock.manufacture_date,"
						+ "mas_supplier.supplier_name,msst.supplier_Type_Name"
						+ " from mas_store_item mas_store_item "
						+ "inner join store_item_batch_stock store_item_batch_stock on mas_store_item.item_id = store_item_batch_stock.item_id"
						+ " left outer join mas_store_unit mas_store_unit on mas_store_item.item_unit_id=mas_store_unit.store_unit_id "
						+ " left outer join MAS_STORE_SUPPLIER mas_supplier on mas_supplier.SUPPLIER_ID=store_item_batch_stock.supplier_id "
						+ " left outer join MAS_STORE_SUPPLIER_TYPE msst on msst.SUPPLIER_TYPE_ID=store_item_batch_stock.supplier_type_id "																						// by
						+ " where store_item_batch_stock.department_id ='" + deptId + "'" + query;
						 
						if(mmuId!=0) {
						qry=qry	+ " and store_item_batch_stock.mmu_id = '" + mmuId + "' ";
						}
						qry=qry	+ " and store_item_batch_stock.expiry_date > current_date "
						+ " group by store_item_batch_stock.item_id,mas_store_item.pvms_no,mas_store_item.nomenclature, "
						+ " store_item_batch_stock.batch_no,store_item_batch_stock.expiry_date,mas_store_unit.store_unit_name,store_item_batch_stock.manufacture_date, mas_supplier.supplier_name,msst.supplier_Type_Name "
						+ " order by mas_store_item.nomenclature";
			} else {
				qry = "select  sum(coalesce(store_item_batch_stock.received_qty,0)) as batch_stock_received_qty, "
						+ "sum(coalesce(store_item_batch_stock.issue_qty,0)) as batch_stock_issue_qty, "
						+ "sum(coalesce(store_item_batch_stock.opening_balance_qty,0)) as batch_stock_opning_balnce_qty, "
						+ "sum(store_item_batch_stock.CLOSING_STOCK) as balance_qty, "
						+ "mas_store_item.pvms_no as mas_store_item_pvms_no,mas_store_item.nomenclature as mas_store_item_nomenclature, "
						+ "mas_store_unit.store_unit_name, " + "mas_hospital.hospital_name " // added by rahul
						+ " from mas_store_item mas_store_item "
						+ "inner join store_item_batch_stock store_item_batch_stock on mas_store_item.item_id = store_item_batch_stock.item_id "
						+ " left outer join mas_store_unit mas_store_unit on mas_store_item.item_unit_id=mas_store_unit.store_unit_id "
						+ " left outer join mas_hospital mas_hospital on mas_hospital.hospital_id=store_item_batch_stock.hospital_id " // added
					 
						+ "where store_item_batch_stock.department_id ='" + deptId + "'" + query;
						if(mmuId!=0) {
							qry=qry	+ " and store_item_batch_stock.mmu_id = '" + mmuId + "' ";
						}
						qry=qry	+ " and store_item_batch_stock.expiry_date > current_date "
						+ " group by mas_store_item.pvms_no,mas_store_item.nomenclature,mas_store_unit.store_unit_name,  "
						+ " mas_hospital.hospital_name " // added by rahul
						+ " order by  mas_store_item.nomenclature";
			}

			Query queryHiber = (Query) session.createSQLQuery(qry);

			if (groupId != 0) {
				queryHiber = queryHiber.setParameter("group", groupId);
			}

			if (sectionId != 0) {
				queryHiber = queryHiber.setParameter("section", sectionId);
			}

			if (itemId != 0) {
				queryHiber = queryHiber.setParameter("item", itemId);
			}

			/*
			 * if (hospitalList.length>0){ queryHiber = queryHiber.setParameter("hospital",
			 * hospitalList); }
			 */

			objectList = (List<Object[]>) queryHiber.list();

		} catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return objectList;
	}
	
	@SuppressWarnings({ "unchecked" })
	@Override
	public List<Object[]> generateStockStatusReportCo(String radioSelectValue, long itemId, long groupId, long sectionId,
			long cityId, long deptId) {

		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		System.out.println("calling*********************************************");
		List<Object[]> objectList = new ArrayList<Object[]>();
		String qry = "";
		String query = "";

		try {

			if (groupId != 0) {
				query = query + " and mas_store_item.group_id = :group";
			}

			if (sectionId != 0) {
				query = query + " and mas_store_item.section_id = :section";
			}
			if (itemId != 0) {
				query = query + " and mas_store_item.item_id = :item";
			}

			query = query + " and store_item_batch_stock.CLOSING_STOCK>0";

			if (radioSelectValue.equalsIgnoreCase("D")) {
				qry = "select  sum(coalesce(store_item_batch_stock.received_qty,0)) as batch_stock_received_qty,"
						+ "sum(coalesce(store_item_batch_stock.issue_qty,0)) as batch_stock_issue_qty,"
						+ "sum(coalesce(store_item_batch_stock.opening_balance_qty,0)) as batch_stock_opning_balnce_qty,"
						+ "sum(store_item_batch_stock.CLOSING_STOCK) as balance_qty,"
						+ "mas_store_item.pvms_no,mas_store_item.nomenclature,store_item_batch_stock.batch_no,store_item_batch_stock.expiry_date, "
						+ "mas_store_unit.store_unit_name,store_item_batch_stock.manufacture_date,"
						+ "mas_supplier.supplier_name,msst.supplier_Type_Name"
						+ " from mas_store_item mas_store_item "
						+ "inner join store_item_batch_stock store_item_batch_stock on mas_store_item.item_id = store_item_batch_stock.item_id"
						+ " left outer join mas_store_unit mas_store_unit on mas_store_item.item_unit_id=mas_store_unit.store_unit_id "
						+ " left outer join MAS_STORE_SUPPLIER mas_supplier on mas_supplier.SUPPLIER_ID=store_item_batch_stock.supplier_id "
						+ " left outer join MAS_STORE_SUPPLIER_TYPE msst on msst.SUPPLIER_TYPE_ID=store_item_batch_stock.supplier_type_id "
						+ " where store_item_batch_stock.department_id ='" + deptId + "'" + query
						+ " and store_item_batch_stock.city_id = '" + cityId + "' "
						+ " and store_item_batch_stock.expiry_date > current_date "
						+ " group by store_item_batch_stock.item_id,mas_store_item.pvms_no,mas_store_item.nomenclature, "
						+ " store_item_batch_stock.batch_no,store_item_batch_stock.expiry_date,mas_store_unit.store_unit_name,store_item_batch_stock.manufacture_date,  "
						+ " mas_supplier.supplier_name,msst.supplier_Type_Name  "
						+ " order by mas_store_item.nomenclature";
			} else {
				qry = "select  sum(coalesce(store_item_batch_stock.received_qty,0)) as batch_stock_received_qty, "
						+ "sum(coalesce(store_item_batch_stock.issue_qty,0)) as batch_stock_issue_qty, "
						+ "sum(coalesce(store_item_batch_stock.opening_balance_qty,0)) as batch_stock_opning_balnce_qty, "
						+ "sum(store_item_batch_stock.CLOSING_STOCK) as balance_qty, "
						+ "mas_store_item.pvms_no as mas_store_item_pvms_no,mas_store_item.nomenclature as mas_store_item_nomenclature, "
						+ "mas_store_unit.store_unit_name "
						+ " from mas_store_item mas_store_item "
						+ "inner join store_item_batch_stock store_item_batch_stock on mas_store_item.item_id = store_item_batch_stock.item_id "
						+ " left outer join mas_store_unit mas_store_unit on mas_store_item.item_unit_id=mas_store_unit.store_unit_id "
						+ "where store_item_batch_stock.department_id ='" + deptId + "'" + query
						+ " and store_item_batch_stock.city_id = '" + cityId + "' "
						+ " and store_item_batch_stock.expiry_date > current_date "
						+ " group by mas_store_item.pvms_no,mas_store_item.nomenclature,mas_store_unit.store_unit_name  "
						+ " order by mas_store_item.nomenclature";
			}

			Query queryHiber = (Query) session.createSQLQuery(qry);

			if (groupId != 0) {
				queryHiber = queryHiber.setParameter("group", groupId);
			}

			if (sectionId != 0) {
				queryHiber = queryHiber.setParameter("section", sectionId);
			}

			if (itemId != 0) {
				queryHiber = queryHiber.setParameter("item", itemId);
			}

			/*
			 * if (hospitalList.length>0){ queryHiber = queryHiber.setParameter("hospital",
			 * hospitalList); }
			 */

			objectList = (List<Object[]>) queryHiber.list();

		} catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return objectList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getReceivingIndentWaitingList(String fromDate, String toDate, long mmuId,
			String indentNo, long departmentId, int pageNo) {

		List<Integer> totalMatches = new ArrayList<Integer>();
		List<StoreIssueM> waitingListData = new ArrayList<StoreIssueM>();
		Map<String, Object> map = new HashMap<String, Object>();
		int pageSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();

			Criteria cr = session.createCriteria(StoreIssueM.class).createAlias("storeInternalIndentM", "m1")
				.add(Restrictions.eq("m1.mmuId", mmuId))
				.add(Restrictions.isNull("status"));
			// storeInternalIndentM
			cr = cr.add(Restrictions.or(Restrictions.eq("m1.status", "I").ignoreCase(), Restrictions.eq("m1.status", "P").ignoreCase()));
			cr = cr.addOrder(Order.desc("issueDate"));
			totalMatches = cr.list();
			cr.setFirstResult((pageSize) * (pageNo - 1));
			cr.setMaxResults(pageSize);

			waitingListData = cr.list();
		} catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		map.put("waitingListData", waitingListData);
		map.put("totalMatches", totalMatches);
		return map;

	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> receivingIndentInInventory(long internalM_headerId, long storeIssueMId) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<StoreInternalIndentM> internalMList = new ArrayList<StoreInternalIndentM>();
		List<StoreIssueM> issueMList = new ArrayList<StoreIssueM>();
		List<StoreIssueT> issueTList = new ArrayList<StoreIssueT>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();

			internalMList = session.createCriteria(StoreInternalIndentM.class)
					.add(Restrictions.eq("id", internalM_headerId)).list();

			issueMList = session.createCriteria(StoreIssueM.class)
					.add(Restrictions.eq("id", storeIssueMId))
					.add(Restrictions.eq("storeInternalIndentM.id", internalM_headerId)).list();


			issueTList = session.createCriteria(StoreIssueT.class).add(Restrictions.eq("storeIssueM.id", storeIssueMId))
						.list();

			map.put("internalMList", internalMList);
			map.put("issueTList", issueTList);
			map.put("issueMList", issueMList);

		} catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean addToStockIssuedIndent(JSONObject formData) {
		boolean flag = false;

		JSONArray itemId = formData.getJSONArray("itemId");
		JSONArray issueTId = formData.getJSONArray("issueTId");
		JSONArray batchNumber = formData.getJSONArray("batchNumber");
		JSONArray doeDate = formData.getJSONArray("doe");
		JSONArray domDate = formData.getJSONArray("dom");
		JSONArray qtyReceived = formData.getJSONArray("qtyReceived");
		JSONArray indentTId = formData.getJSONArray("indentTId");
		JSONArray stockId = formData.getJSONArray("stockId");

		/*
		 * JSONArray issueTId = formData.getJSONArray("issueTId"); JSONArray qtyDemand =
		 * formData.getJSONArray("qtyDemand"); JSONArray qtyIssued =
		 * formData.getJSONArray("qtyIssued");
		 */

		JSONArray userArr = formData.getJSONArray("userId");
		long userId = userArr.getLong(0);

		JSONArray indentMIArr = formData.getJSONArray("indentMId");
		long indentMId = indentMIArr.getLong(0);

		JSONArray mmu = formData.getJSONArray("mmuId");
		long mmuId = mmu.getLong(0);
		
		JSONArray storeIssueMArr = formData.getJSONArray("storeIssueMId");
		long storeIssueMId = storeIssueMArr.getLong(0);

		Date currentDate = HMSUtil
				.convertStringDateToUtilDateForDatabase(formData.getJSONArray("currentDate").getString(0));

		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Transaction tx = session.beginTransaction();
		try {
			for (int i = 0; i < itemId.length(); i++) {
				StoreItemBatchStock stock = null;
				if (!batchNumber.getString(i).isEmpty() && !doeDate.getString(i).isEmpty()
						&& qtyReceived.getLong(i) > 0) {
					List<StoreItemBatchStock> storeItemBatchList = session.createCriteria(StoreItemBatchStock.class)
							.add(Restrictions.eq("mmuId", mmuId))
							.add(Restrictions.eq("masStoreItem.itemId", itemId.getLong(i)))
							.add(Restrictions.eq("batchNo", batchNumber.getString(i))).add(Restrictions.eq("expiryDate",
									HMSUtil.convertStringDateToUtilDateForDatabase(doeDate.getString(i))))
							.list();

					if (!storeItemBatchList.isEmpty() && storeItemBatchList.size() > 0) {
						stock = storeItemBatchList.get(0);
						long closingBalanceQty = storeItemBatchList.get(0).getClosingStock() != null
								? storeItemBatchList.get(0).getClosingStock()
								: 0;
						closingBalanceQty = closingBalanceQty + qtyReceived.getLong(i);
						stock.setClosingStock(closingBalanceQty);

						long receivedQty = storeItemBatchList.get(0).getReceivedQty() != null
								? storeItemBatchList.get(0).getReceivedQty()
								: 0;
						receivedQty = receivedQty + qtyReceived.getLong(i);
						stock.setReceivedQty(receivedQty);

						stock.setLastChgDate(new Timestamp(new Date().getTime()));
						Users lastChg = new Users();
						lastChg.setUserId(userId);
						stock.setUser(lastChg);
						stock.setMmuId(mmuId);
						MasDepartment dept = new MasDepartment();
						long departmentId = Long.parseLong(HMSUtil.getProperties("adt.properties", "PHARMACY_DEPT_ID").trim());
						//long departmentId = 2051;
						dept.setDepartmentId(departmentId);
						stock.setMasDepartment(dept);
						
						session.update(stock);
					} else {
						stock = new StoreItemBatchStock();
						BigDecimal unitRate = null;
						BigDecimal totalAmount = null;
						stock.setMmuId(mmuId);
						/*
						 * StoreItemBatchStock storeStock =
						 * (StoreItemBatchStock)session.get(StoreItemBatchStock.class,
						 * stockId.getLong(i)); if(storeStock!=null && storeStock.getMrp()!=null) {
						 * unitRate = storeStock.getMrp(); totalAmount =
						 * unitRate.multiply(BigDecimal.valueOf(qtyReceived.getLong(i)));
						 * stock.setMrp(unitRate); stock.setCostPrice(totalAmount); }
						 */
							
						StoreItemBatchStock storeStock =
								  (StoreItemBatchStock)session.get(StoreItemBatchStock.class,
								  stockId.getLong(i)); if(storeStock!=null && storeStock.getMrp()!=null) {
								  unitRate = storeStock.getMrp(); 
								  //totalAmount =unitRate.multiply(BigDecimal.valueOf(qtyReceived.getLong(i)));
								  stock.setMrp(unitRate); 
								  //stock.setCostPrice(totalAmount); 
								  }
								  
						
						MasStoreItem item = new MasStoreItem();
						item.setItemId(itemId.getLong(i)); // item.setItemId(Long.parseLong(itemId.get(i).toString()));
						stock.setMasStoreItem(item);

						stock.setBatchNo(batchNumber.get(i).toString());

						stock.setClosingStock(qtyReceived.getLong(i));
						stock.setReceivedQty(qtyReceived.getLong(i));

						if (domDate.get(i) != null && !domDate.get(i).toString().isEmpty()) {
							stock.setManufactureDate(
									HMSUtil.convertStringDateToUtilDateForDatabase(domDate.get(i).toString()));
						}

						stock.setExpiryDate(HMSUtil.convertStringDateToUtilDateForDatabase(doeDate.get(i).toString()));

						stock.setLastChgDate(new Timestamp(new Date().getTime()));

						/*
						 * MasDepartment dept = new MasDepartment(); dept.setDepartmentId(departmentId);
						 * stock.setMasDepartment(dept);
						 */
						MasDepartment dept = new MasDepartment();
						long departmentId = Long.parseLong(HMSUtil.getProperties("adt.properties", "PHARMACY_DEPT_ID").trim());
						//long departmentId = 2051;
						dept.setDepartmentId(departmentId);
						stock.setMasDepartment(dept);
						
						Users lastChg = new Users();
						lastChg.setUserId(userId);
						stock.setUser(lastChg);

						session.save(stock);

					}

				}

				long itemQtyReceived = (long) session.createCriteria(StoreIssueT.class)
						.add(Restrictions.eq("storeInternalIndentT.id", indentTId.getLong(i)))
						.setProjection(Projections.sum("qtyIssued")).uniqueResult();

				/*
				 * List<StoreInternalIndentT> tList = session.createCriteria(StoreIssueT.class)
				 * .createAlias("storeInternalIndentT", "t").add(Restrictions.eq("t.id",
				 * indentTId.getLong(i))) .list();
				 * 
				 * long itemQtyReceived = 0; if (!tList.isEmpty() && tList.size() > 0) { for
				 * (StoreInternalIndentT st : tList) { itemQtyReceived = itemQtyReceived +
				 * st.getQtyRequest(); } }
				 */
				
				StoreIssueT sIssueT = (StoreIssueT) session.get(StoreIssueT.class, issueTId.getLong(i));
				if(sIssueT != null) {
					sIssueT.setQtyReceived(qtyReceived.getLong(i)); 
					sIssueT.setReceivedBy(userId);
					sIssueT.setReceivedDate(new Date());
					session.update(sIssueT);
				}

				StoreInternalIndentT internalIndentT = (StoreInternalIndentT) session.get(StoreInternalIndentT.class,
						indentTId.getLong(i));
				if (internalIndentT != null) {
					internalIndentT.setAvailableStock(
							(internalIndentT.getAvailableStock() != null ? internalIndentT.getAvailableStock() : 0)
									+ itemQtyReceived);
					internalIndentT.setQtyReceived(itemQtyReceived);
					session.save(internalIndentT);
				}

			}
			
			StoreIssueM storeIssueM = (StoreIssueM) session.get(StoreIssueM.class, storeIssueMId);
			if(storeIssueM != null) {
				storeIssueM.setStatus("R");
				session.update(storeIssueM);
			}

			StoreInternalIndentM internalIndentM = (StoreInternalIndentM) session.get(StoreInternalIndentM.class,
					indentMId);
			if (internalIndentM != null) {
				if(internalIndentM.getStatus().equalsIgnoreCase("I")) {
					List<StoreIssueM> storeMList = session.createCriteria(StoreIssueM.class)
							.add(Restrictions.ne("id", storeIssueMId))
							.add(Restrictions.eq("indentMId", indentMId))
							.add(Restrictions.isNull("status")).list();
					
					if(storeMList.isEmpty()) {
						internalIndentM.setStatus("O"); 
						internalIndentM.setReceivedDate(new Timestamp(currentDate.getTime()));
						
					}
				}
				session.update(internalIndentM);
			}

			tx.commit();
			flag = true;

		} catch (Exception e) {
			indentMId = 0;
			tx.rollback();
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return flag;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MasStoreItem> getStoreItemListForAutoComplete(String pvmsNo, String nomenclature, String itemId) {
		List<MasStoreItem> itemList = new ArrayList<MasStoreItem>();
		List<MasStoreItem> itemListEq = new ArrayList<MasStoreItem>();
		List<MasStoreItem> itemListilike = new ArrayList<MasStoreItem>();
		List<MasStoreItem> removalItemList = new ArrayList<MasStoreItem>();

		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();

		Criteria cr = session.createCriteria(MasStoreItem.class)
				.add(Restrictions.eq("status", "y").ignoreCase());
				//.add(Restrictions.eq("edl", "y").ignoreCase());
				
		try {
			if (!pvmsNo.isEmpty()) {
				pvmsNo = "%" + pvmsNo + "%";
				cr = cr.add(Restrictions.ilike("pvmsNo", pvmsNo));
			}

			if (!nomenclature.isEmpty()) {
				nomenclature = nomenclature + "%";
				cr = cr.add(Restrictions.ilike("nomenclature", nomenclature));
			}

			if (!itemId.isEmpty()) {
				cr = cr.add(Restrictions.eq("itemId", Long.parseLong(itemId)));
			}

			cr.setFirstResult(0);
			cr.setMaxResults(10);

			itemListEq = cr.list();
			itemList = itemListEq;

			if (itemListEq.size() >= 0 && itemListEq.size() < 10) {
				if (!nomenclature.isEmpty()) {

					Criteria crilike = session.createCriteria(MasStoreItem.class)
							.add(Restrictions.eq("status", "y").ignoreCase());
							//.add(Restrictions.eq("edl", "y").ignoreCase());  //for drug code search

					nomenclature = "%" + nomenclature;
					crilike = crilike.add(Restrictions.ilike("nomenclature", nomenclature));

					crilike.setFirstResult(0);
					crilike.setMaxResults(10);

					itemListilike = crilike.list();

					for (int i = 0; i < itemListilike.size(); i++) {
						for (int j = 0; j < itemListEq.size(); j++) {
							if ((itemListilike.get(i).getItemId()).equals(itemListEq.get(j).getItemId())) {
								// itemListilike.remove(itemListilike.get(i));
								removalItemList.add(itemListilike.get(i));
							}
						}
					}
					itemListilike.removeAll(removalItemList);
					itemList = ListUtils.union(itemListEq, itemListilike);
					if (itemList.size() > 10) {
						itemList = ListUtils.union(itemListEq, itemListilike).subList(0, 10);
					}
				}

			}

		} catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return itemList;

		/*
		 * List<MasStoreItem> itemList = new ArrayList<MasStoreItem>(); Session session
		 * = getHibernateUtils.getHibernateUtlis().OpenSession(); Criteria cr =
		 * session.createCriteria(MasStoreItem.class) .add(Restrictions.eq("status",
		 * "y").ignoreCase());
		 * 
		 * try { if (!pvmsNo.isEmpty()) { pvmsNo = "%"+pvmsNo+"%"; cr =
		 * cr.add(Restrictions.ilike("pvmsNo", pvmsNo)); }
		 * 
		 * if (!nomenclature.isEmpty()) { nomenclature = "%"+nomenclature+"%"; cr =
		 * cr.add(Restrictions.ilike("nomenclature", nomenclature)); }
		 * 
		 * if (!itemId.isEmpty()) { cr = cr.add(Restrictions.eq("itemId",
		 * Long.parseLong(itemId))); }
		 * 
		 * cr =cr.setFirstResult(0); cr=cr.setMaxResults(10);
		 * 
		 * itemList = cr.list(); }catch(Exception e) {
		 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.printStackTrace();
		 * }finally { getHibernateUtils.getHibernateUtlis().CloseConnection(); } return
		 * itemList;
		 */
	}

	@Override
	public boolean updatePhysicalStockTaking(JSONObject jObject) {
		boolean flag = false;

		JSONArray stockIdList = jObject.getJSONArray("stockId");
		JSONArray itemId = jObject.getJSONArray("item");
		JSONArray batchNumber = jObject.getJSONArray("batchNumber");
		JSONArray doeDate = jObject.getJSONArray("doeDate");
		JSONArray computedStock = jObject.getJSONArray("computedStock");
		JSONArray physicalStock = jObject.getJSONArray("physicalStock");
		JSONArray remark = jObject.getJSONArray("remark");
		JSONArray deficient = jObject.getJSONArray("deficient");
		JSONArray surplus = jObject.getJSONArray("surplus");

		JSONArray mmuArr = jObject.getJSONArray("mmuId");
		String mmuId = mmuArr.getString(0);
		
		JSONArray cityIdArr = jObject.getJSONArray("cityId");
		String cityId = cityIdArr.getString(0);
		
		JSONArray distIdArr = jObject.getJSONArray("districtId");
		String districtId = distIdArr.getString(0);

		JSONArray userArr = jObject.getJSONArray("userId");
		long userId = userArr.getLong(0);

		JSONArray departmentArr = jObject.getJSONArray("department");
		long departmentId = departmentArr.getLong(0);

		String txtReason = "";
		if (!jObject.getJSONArray("txtReason").toString().isEmpty()) {
			JSONArray txtReasonArr = jObject.getJSONArray("txtReason");
			txtReason = txtReasonArr.getString(0);
		}

		Timestamp currentDate = new Timestamp(new Date().getTime());

		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Transaction tx = session.beginTransaction();

		try {

			StoreStockTakingM stockTakingM = new StoreStockTakingM();
			stockTakingM.setPhysicalDate(currentDate);
			stockTakingM.setLastChangedDate(currentDate);

			if(mmuId != null && !mmuId.equals("") && !mmuId.equals("null") && !mmuId.equals("0")) {
				stockTakingM.setMmuId(Long.parseLong(mmuId));
			}
			
			if(cityId != null && !cityId.equals("") && !cityId.equals("null") && !cityId.equals("0")) {
				stockTakingM.setCityId(Long.parseLong(cityId));
			}
			
			if(districtId != null && !districtId.equals("") && !districtId.equals("null") && !districtId.equals("0")) {
				stockTakingM.setDistrictId(Long.parseLong(districtId));
			}
			

			MasDepartment department = new MasDepartment();
			department.setDepartmentId(departmentId);
			stockTakingM.setMasDepartment(department);

			Users user = new Users();
			user.setUserId(userId);
			stockTakingM.setLastChgBy(user);

			stockTakingM.setSubmittedBy(user);

			stockTakingM.setStatus("O");

			stockTakingM.setReason(txtReason);

			long stockTakingMId = (long) session.save(stockTakingM);

			for (int i = 0; i < stockIdList.length(); i++) {

				StoreStockTakingT stockTakingT = new StoreStockTakingT();
				StoreStockTakingM m = new StoreStockTakingM();
				m.setTakingMId(stockTakingMId);
				stockTakingT.setStoreStockTakingM(m);

				stockTakingT.setBatchNo(batchNumber.getString(i));
				stockTakingT.setComputedStock(new BigDecimal(computedStock.getString(i)));
				stockTakingT.setStoreStockService(new BigDecimal(physicalStock.getString(i)));

				stockTakingT.setExpiryDate(HMSUtil.convertStringDateToUtilDateForDatabase(doeDate.get(i).toString()));

				if (!surplus.getString(i).isEmpty()) {
					stockTakingT.setStockSurplus(new BigDecimal(surplus.getString(i)));
				}

				if (!deficient.getString(i).isEmpty()) {
					stockTakingT.setStockDeficient(new BigDecimal(deficient.getString(i)));
				}

				stockTakingT.setRemarks(remark.getString(i));

				MasStoreItem item = new MasStoreItem();
				item.setItemId(itemId.getLong(i));
				stockTakingT.setMasStoreItem(item);

				StoreItemBatchStock stock = new StoreItemBatchStock();
				stock.setStockId(stockIdList.getLong(i));
				stockTakingT.setStoreItemBatchStock(stock);

				session.save(stockTakingT);
			}

			tx.commit();
			flag = true;

		} catch (Exception e) {
			flag = false;
			tx.rollback();
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return flag;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> stockTakingWaitingListData(String fromDate, String toDate, String mmuId, String cityId, String districtId, long departmentId,
			int pageNo) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<StoreStockTakingM> waitingList = new ArrayList<StoreStockTakingM>();
		List<Integer> totalMatches = new ArrayList<Integer>();
		int pageSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria cr = session.createCriteria(StoreStockTakingM.class);

			if (!fromDate.isEmpty()) {
				cr = cr.add(Restrictions.ge("physicalDate", HMSUtil.convertStringDateToUtilDateForDatabase(fromDate)));
			}

			Date toEndDate = null;
			if (!toDate.isEmpty()) {
				toEndDate = HMSUtil.getNextDate(HMSUtil.convertStringTypeDateToDateType(toDate));
				cr = cr.add(Restrictions.le("physicalDate", toEndDate));
			}

			cr = cr.add(Restrictions.eq("status", "O").ignoreCase());
			
			if(mmuId != null && !mmuId.equals("") && !mmuId.equals("null") && !mmuId.equals("0")) {
				cr = cr.add(Restrictions.eq("mmuId", Long.parseLong(mmuId)));
			}
			
			if(cityId != null && !cityId.equals("") && !cityId.equals("null") && !cityId.equals("0")) {
				cr = cr.add(Restrictions.eq("cityId", Long.parseLong(cityId)));
			}
			
			if(districtId != null && !districtId.equals("") && !districtId.equals("null") && !districtId.equals("0")) {
				cr = cr.add(Restrictions.eq("districtId", Long.parseLong(districtId)));
			}
			
			cr = cr.add(Restrictions.eq("masDepartment.departmentId", departmentId)).addOrder(Order.asc("id"));

			totalMatches = cr.list();

			cr.setFirstResult((pageSize) * (pageNo - 1));
			cr.setMaxResults(pageSize);

			waitingList = cr.list();

		} catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		map.put("totalMatches", totalMatches);
		map.put("waitingList", waitingList);
		return map;

	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> stockTakingDetailsForApproval(long takingM_headerId) {

		Map<String, Object> map = new HashMap<String, Object>();
		List<StoreStockTakingM> takingMDetails = new ArrayList<StoreStockTakingM>();
		List<StoreStockTakingT> takingTDetails = new ArrayList<StoreStockTakingT>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();

			takingMDetails = session.createCriteria(StoreStockTakingM.class)
					.add(Restrictions.eq("takingMId", takingM_headerId)).list();

			takingTDetails = session.createCriteria(StoreStockTakingT.class)
					.add(Restrictions.eq("storeStockTakingM.takingMId", takingM_headerId)).list();

			map.put("takingMDetails", takingMDetails);
			map.put("takingTDetails", takingTDetails);

		} catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return map;

	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean submitStockingTakingDataForApproval(JSONObject jObject) {
		boolean flag = false;

		JSONArray itemId = jObject.getJSONArray("itemId");
		JSONArray stockId = jObject.getJSONArray("stockId");

		// JSONArray stockTakingTId = jObject.getJSONArray("stockTakingTId");

		JSONArray physicalStock = jObject.getJSONArray("physicalStock");
		JSONArray computedStock = jObject.getJSONArray("computedStock");
		JSONArray batchNumber = jObject.getJSONArray("batchNumber");
		JSONArray doeDate = jObject.getJSONArray("doe");

		JSONArray surplus = jObject.getJSONArray("surplus");
		JSONArray deficient = jObject.getJSONArray("deficient");
		JSONArray remarks = jObject.getJSONArray("remarks");

		/*
		 * JSONArray hospitalArr = jObject.getJSONArray("hospitalId"); long hospitalId =
		 * hospitalArr.getLong(0);
		 * 
		 * JSONArray departmentArr = jObject.getJSONArray("departmentId"); long
		 * departmentId = departmentArr.getLong(0);
		 */
		JSONArray takingMArr = jObject.getJSONArray("takingMId");
		long takingMId = takingMArr.getLong(0);

		JSONArray userArr = jObject.getJSONArray("userId");
		long userId = userArr.getLong(0);

		JSONArray statusArr = jObject.getJSONArray("actionId");
		String approvalStatus = statusArr.getString(0);

		JSONArray reasonStockTakingArr = jObject.getJSONArray("reasonStockTaking");
		String reasonStockTaking = reasonStockTakingArr.getString(0);

		JSONArray finalRemarksArr = jObject.getJSONArray("finalRemarks");
		String finalRemarks = finalRemarksArr.getString(0);

		String tableRowId = "";
		String array_RowId[];

		JSONArray tableRowIdArr = jObject.getJSONArray("hiddenValueCharge");

		tableRowId = tableRowIdArr.getString(0);
		array_RowId = tableRowId.split(",");

		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Transaction tx = session.beginTransaction();
		try {
			StoreStockTakingM stockTakingM = (StoreStockTakingM) session.get(StoreStockTakingM.class, takingMId);

			if (approvalStatus.equalsIgnoreCase("A")) {

				stockTakingM.setStatus(approvalStatus);

				Users approvedBy = new Users();
				approvedBy.setUserId(userId);
				stockTakingM.setApprovedBy(approvedBy);
				stockTakingM.setLastChgBy(approvedBy);
				stockTakingM.setReason(reasonStockTaking);
				stockTakingM.setLastChangedDate(new Timestamp(new Date().getTime()));
				session.update(stockTakingM);

				if (tableRowId != "" && !tableRowId.isEmpty()) {
					for (int i = 0; i < array_RowId.length; i++) {
						StoreStockTakingT stockTakingT = null;
						List<StoreStockTakingT> stockTakingTList = new ArrayList<StoreStockTakingT>();

						stockTakingT = (StoreStockTakingT) session.get(StoreStockTakingT.class,
								Long.parseLong(array_RowId[i]));

						stockTakingTList = session.createCriteria(StoreStockTakingT.class)
								.add(Restrictions.eq("takingTId", Long.parseLong(array_RowId[i])))
								.add(Restrictions.eq("storeStockTakingM.takingMId", takingMId)).list();

						if (stockTakingTList.size() > 0) {

							stockTakingT = stockTakingTList.get(0);

							stockTakingT.setStoreStockService(new BigDecimal(physicalStock.getString(i)));
							if (!surplus.getString(i).isEmpty()) {
								stockTakingT.setStockSurplus(new BigDecimal(surplus.getString(i)));
							}
							if (!deficient.getString(i).isEmpty()) {
								stockTakingT.setStockDeficient(new BigDecimal(deficient.getString(i)));
							}
							session.update(stockTakingT);

							if (!stockId.getString(i).isEmpty()) {
								StoreItemBatchStock storeItemBacthStock = null;
								storeItemBacthStock = (StoreItemBatchStock) session.get(StoreItemBatchStock.class,
										Long.parseLong(stockId.getString(i)));
								if (storeItemBacthStock != null) {
									long closingBalanceQty = storeItemBacthStock.getClosingStock() != null
											? storeItemBacthStock.getClosingStock()
											: 0;
									if (!surplus.getString(i).isEmpty()) {
										if (storeItemBacthStock.getStockSurplus() != null) {
											storeItemBacthStock.setStockSurplus(storeItemBacthStock.getStockSurplus()
													.add(new BigDecimal(surplus.getString(i))));
										} else {
											storeItemBacthStock.setStockSurplus(new BigDecimal(surplus.getString(i)));
										}
										storeItemBacthStock.setClosingStock(
												closingBalanceQty + (Long.parseLong(surplus.getString(i))));
										/*
										 * storeItemBacthStock.setClosingStock(Long.parseLong(physicalStock.getString(i)
										 * ));
										 */

									} else if (!deficient.getString(i).isEmpty()) {
										if (storeItemBacthStock.getStockDeficient() != null) {
											storeItemBacthStock.setStockDeficient(storeItemBacthStock
													.getStockDeficient().add(new BigDecimal(deficient.getString(i))));
										} else {
											storeItemBacthStock
													.setStockDeficient(new BigDecimal(deficient.getString(i)));
										}
										storeItemBacthStock.setClosingStock(
												closingBalanceQty - (Long.parseLong(deficient.getString(i))));
										/*
										 * storeItemBacthStock.setClosingStock(Long.parseLong(physicalStock.getString(i)
										 * ));
										 */
									}
									storeItemBacthStock.setLastChgDate(new Timestamp(new Date().getTime()));
									storeItemBacthStock.setUser(approvedBy);
									session.update(storeItemBacthStock);

								}
							}

						} else {
							stockTakingT = new StoreStockTakingT();

							StoreStockTakingM m = new StoreStockTakingM();
							m.setTakingMId(takingMId);
							stockTakingT.setStoreStockTakingM(m);

							stockTakingT.setBatchNo(batchNumber.getString(i));
							stockTakingT.setComputedStock(new BigDecimal(computedStock.getString(i)));
							stockTakingT.setStoreStockService(new BigDecimal(physicalStock.getString(i)));

							stockTakingT.setExpiryDate(
									HMSUtil.convertStringDateToUtilDateForDatabase(doeDate.get(i).toString()));

							if (!surplus.getString(i).isEmpty()) {
								stockTakingT.setStockSurplus(new BigDecimal(surplus.getString(i)));
							}

							if (!deficient.getString(i).isEmpty()) {
								stockTakingT.setStockDeficient(new BigDecimal(deficient.getString(i)));
							}

							stockTakingT.setRemarks(remarks.getString(i));

							MasStoreItem item = new MasStoreItem();
							item.setItemId(itemId.getLong(i));
							stockTakingT.setMasStoreItem(item);

							StoreItemBatchStock stock = new StoreItemBatchStock();
							stock.setStockId(stockId.getLong(i));
							stockTakingT.setStoreItemBatchStock(stock);

							session.save(stockTakingT);

							if (!stockId.getString(i).isEmpty()) {
								StoreItemBatchStock storeItemBacthStock = null;
								storeItemBacthStock = (StoreItemBatchStock) session.get(StoreItemBatchStock.class,
										Long.parseLong(stockId.getString(i)));
								if (storeItemBacthStock != null) {
									long closingBalanceQty = storeItemBacthStock.getClosingStock() != null
											? storeItemBacthStock.getClosingStock()
											: 0;

									if (!surplus.getString(i).isEmpty()) {
										if (storeItemBacthStock.getStockSurplus() != null) {
											storeItemBacthStock.setStockSurplus(storeItemBacthStock.getStockSurplus()
													.add(new BigDecimal(surplus.getString(i))));
										} else {
											storeItemBacthStock.setStockSurplus(new BigDecimal(surplus.getString(i)));
										}
										storeItemBacthStock.setClosingStock(
												closingBalanceQty + Long.parseLong(surplus.getString(i)));

									} else if (!deficient.getString(i).isEmpty()) {
										if (storeItemBacthStock.getStockDeficient() != null) {
											storeItemBacthStock.setStockDeficient(storeItemBacthStock
													.getStockDeficient().add(new BigDecimal(deficient.getString(i))));
										} else {
											storeItemBacthStock
													.setStockDeficient(new BigDecimal(deficient.getString(i)));
										}
										storeItemBacthStock.setClosingStock(
												closingBalanceQty - Long.parseLong(deficient.getString(i)));
									}
									storeItemBacthStock.setLastChgDate(new Timestamp(new Date().getTime()));
									storeItemBacthStock.setUser(approvedBy);
									session.update(storeItemBacthStock);
								}
							}
						}

					}
				}
			} else if (approvalStatus.equalsIgnoreCase("R")) {
				// Rejected case
				stockTakingM.setStatus(approvalStatus);

				Users approvedBy = new Users();
				approvedBy.setUserId(userId);
				stockTakingM.setApprovedBy(approvedBy);
				stockTakingM.setLastChgBy(approvedBy);

				stockTakingM.setReason(finalRemarks);
				stockTakingM.setLastChangedDate(new Timestamp(new Date().getTime()));
				session.update(stockTakingM);
			}

			tx.commit();
			flag = true;
		} catch (Exception e) {
			flag = false;
			tx.rollback();
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return flag;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StoreSoM> getDataForCreateSupplyOrder(long hospitalId, long departmentId) {
		List<StoreSoM> soMList = new ArrayList<StoreSoM>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();

			soMList = session.createCriteria(StoreSoM.class)
					.add(Restrictions.eq("masDepartment.departmentId", departmentId))
					.add(Restrictions.eq("masHospital.hospitalId", hospitalId))
					.add(Restrictions.eq("status", 'A').ignoreCase()).list();
		} catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return soMList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getSupplyOrderSanctionData(long soMId) {

		Map<String, Object> map = new HashMap<String, Object>();
		List<StoreSoM> storeSoMData = new ArrayList<StoreSoM>();
		List<StoreSoT> storeSoTData = new ArrayList<StoreSoT>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();

			storeSoMData = session.createCriteria(StoreSoM.class).add(Restrictions.eq("soMId", soMId)).list();

			storeSoTData = session.createCriteria(StoreSoT.class).add(Restrictions.eq("storeSoM.soMId", soMId)).list();

			map.put("storeSoMData", storeSoMData);
			map.put("storeSoTData", storeSoTData);

		} catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return map;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MasStoreSupplierType> getsupplierTypeList() {
		List<MasStoreSupplierType> supplierTypeList = new ArrayList<MasStoreSupplierType>();
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		try {
			supplierTypeList = session.createCriteria(MasStoreSupplierType.class)
					.add(Restrictions.eq("status", "y").ignoreCase()).list();

		} catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return supplierTypeList;
	}

	@Override
	public boolean saveSupplyOrder(JSONObject jObject) {
		boolean flag = false;

		JSONArray requestTypeArr = jObject.getJSONArray("requestType");
		String requestTypeStatus = requestTypeArr.getString(0);

		JSONArray vendorArr = jObject.getJSONArray("vendorId");
		long vendorId = vendorArr.getLong(0);

		JSONArray storeSoMArr = jObject.getJSONArray("storeSoMId");
		long storeSoMId = storeSoMArr.getLong(0);

		JSONArray quotationMArr = jObject.getJSONArray("quotationMId");
		long quotationMId = quotationMArr.getLong(0);

		/*
		 * JSONArray quotationArr = jObject.getJSONArray("quotationNo"); String
		 * quotationNo = quotationArr.getString(0);
		 */

		// SanctionId and StoreSoMId are same
		/*
		 * JSONArray sanctionArr = jObject.getJSONArray("sanctionId"); long sanctionId =
		 * sanctionArr.getLong(0);
		 */

		JSONArray finYearArr = jObject.getJSONArray("yearId");
		long yearId = finYearArr.getLong(0);

		JSONArray stockistArr = jObject.getJSONArray("stockistId");
		long stockistId = stockistArr.getLong(0);

		JSONArray soDateArr = jObject.getJSONArray("soDate");
		String soDate = soDateArr.getString(0);

		JSONArray deliveryDateArr = jObject.getJSONArray("deliveryDate");
		String deliveryDate = deliveryDateArr.getString(0);

		JSONArray rfp_noArr = jObject.getJSONArray("rfp_no");
		String rfp_no = rfp_noArr.getString(0);

		JSONArray departmentArr = jObject.getJSONArray("departmentId");
		long departmentId = departmentArr.getLong(0);

		JSONArray hospitaltArr = jObject.getJSONArray("hospitalId");
		long hospitalId = hospitaltArr.getLong(0);

		JSONArray enterbyArr = jObject.getJSONArray("userId");
		long enterbyId = enterbyArr.getLong(0);

		JSONArray taxDetailsArr = jObject.getJSONArray("taxDetails");
		String taxDetails = taxDetailsArr.getString(0);

		JSONArray paymentTermArr = jObject.getJSONArray("paymentTerm");
		String paymentTerm = paymentTermArr.getString(0);

		JSONArray deliveryScheduleArr = jObject.getJSONArray("deliverySchedule");
		String deliverySchedule = deliveryScheduleArr.getString(0);

		JSONArray itemId = jObject.getJSONArray("itemId");
		JSONArray stockSoT = jObject.getJSONArray("stockSoT");
		JSONArray qtyRequired = jObject.getJSONArray("qty");
		JSONArray unitRate = jObject.getJSONArray("unitRate");
		JSONArray amtValue = jObject.getJSONArray("amtValue");
		JSONArray quotationT = jObject.getJSONArray("quotationTId");

		String lpTypeFlag = "";

		Timestamp currentDate = new Timestamp(new Date().getTime());

		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Transaction tx = session.beginTransaction();
		try {
			StorePoM poM = new StorePoM();

			StoreSoM soM = null;
			soM = getSanctionOrderObjectFromId(storeSoMId);
			if (soM != null) {
				lpTypeFlag = soM.getLpTypeFlag() != null ? soM.getLpTypeFlag() : "";
				poM.setLpTypeFlag(lpTypeFlag);
			} else {
				soM = new StoreSoM();
				soM.setSoMId(storeSoMId);
			}
			poM.setStoreSoM(soM);

			StoreQuotationM quotationM = new StoreQuotationM();
			quotationM.setQuotationMId(quotationMId);
			poM.setStoreQuotationM(quotationM);

			poM.setRfpNo(rfp_no);

			poM.setPoDate(HMSUtil.convertStringDateToUtilDateForDatabase(soDate));
			poM.setDeliveryDate(HMSUtil.convertStringDateToUtilDateForDatabase(deliveryDate));

			MasStoreSupplier supplier = new MasStoreSupplier();
			supplier.setSupplierId(vendorId);
			poM.setMasStoreSupplier(supplier);

			MasStoreSupplierType supplierType = new MasStoreSupplierType();
			supplierType.setSupplierTypeId(stockistId);
			poM.setMasStoreSupplierType(supplierType);

			poM.setTaxTerm(taxDetails);
			poM.setDeliverySchedule(deliverySchedule);
			poM.setPayTerms(paymentTerm);

			poM.setStatus(requestTypeStatus);

			MasDepartment department = new MasDepartment();
			department.setDepartmentId(departmentId);
			poM.setMasDepartment(department);

			MasStoreFinancial year = new MasStoreFinancial();
			year.setFinancialId(yearId);
			poM.setMasStoreFinancial(year);

			MasHospital hospital = new MasHospital();
			hospital.setHospitalId(hospitalId);
			poM.setMasHospital(hospital);

			Users lastChgBy = new Users();
			lastChgBy.setUserId(enterbyId);
			poM.setLastChgBy(lastChgBy);

			poM.setLastChgDate(currentDate);

			long storePoMId = (long) session.save(poM);

			for (int i = 0; i < qtyRequired.length(); i++) {
				if (qtyRequired.getLong(i) != 0) {
					StorePoT poT = new StorePoT();

					MasStoreItem item = new MasStoreItem();
					item.setItemId(itemId.getLong(i));
					poT.setMasStoreItem(item);

					StorePoM storePoM = new StorePoM();
					storePoM.setPoMId(storePoMId);
					poT.setStorePoM(storePoM);

					StoreSoT storeSoT = new StoreSoT();
					storeSoT.setSoTId(stockSoT.getLong(i));
					poT.setStoreSoT(storeSoT);

					StoreQuotationT quotationTId = new StoreQuotationT();
					quotationTId.setQuotationTId(quotationT.getLong(i));
					poT.setStoreQuatationDetails(quotationTId);

					poT.setQuantityOrdered(qtyRequired.getLong(i));
					poT.setAmount(new BigDecimal(amtValue.getString(i)));
					poT.setUnitRate(new BigDecimal(unitRate.getString(i)));
					session.save(poT);
				}
			}

			StoreSoM soMStatus = (StoreSoM) session.get(StoreSoM.class, storeSoMId);
			soMStatus.setStatus("C");
			Users lastchgBy = new Users();
			lastchgBy.setUserId(enterbyId);
			soMStatus.setLastChgBy(lastchgBy);
			session.update(soMStatus);

			tx.commit();
			flag = true;
		} catch (Exception e) {
			tx.rollback();
			;
			flag = false;
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return flag;
	}

	private StoreSoM getSanctionOrderObjectFromId(long storeSoMId) {
		StoreSoM storeSoM = null;
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		storeSoM = (StoreSoM) session.get(StoreSoM.class, storeSoMId);
		return storeSoM;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MasStoreSupplier> getsupplierList(long hospitalId) {
		List<MasStoreSupplier> masStoreSupplierList = new ArrayList<MasStoreSupplier>();
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		try {
			masStoreSupplierList = session.createCriteria(MasStoreSupplier.class)
					.add(Restrictions.eq("masHospital.hospitalId", hospitalId))
					.add(Restrictions.eq("status", "y").ignoreCase()).addOrder(Order.asc("supplierName")).list();

		} catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return masStoreSupplierList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getDataForSupplyOrderList(long hospitalId, long departmentId, long vendorId,
			int pageNo) {
		List<StorePoM> poMList = new ArrayList<StorePoM>();
		Map<String, Object> map = new HashMap<String, Object>();
		List<Integer> totalMatches = new ArrayList<Integer>();
		int pageSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
		try {
			// status e=save and status s= submit
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria cr = session.createCriteria(StorePoM.class)
					.add(Restrictions.eq("masDepartment.departmentId", departmentId))
					.add(Restrictions.eq("masHospital.hospitalId", hospitalId)).addOrder(Order.asc("poDate"));

			/*
			 * .add(Restrictions.or(Restrictions.eq("status", "e").ignoreCase(),
			 * Restrictions.eq("status", "s").ignoreCase()));
			 */

			if (vendorId != 0) {
				cr = cr.add(Restrictions.eq("masStoreSupplier.supplierId", vendorId));
			}
			totalMatches = cr.list();
			cr.setFirstResult((pageSize) * (pageNo - 1));
			cr.setMaxResults(pageSize);
			poMList = cr.list();
		} catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		map.put("poMList", poMList);
		map.put("totalMatches", totalMatches);
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> supplyOrderWaitingListForPendingApproval(long hospitalId, long departmentId,
			long vendorId, int pageNo) {
		List<StorePoM> poMList = new ArrayList<StorePoM>();
		Map<String, Object> map = new HashMap<String, Object>();
		List<Integer> totalMatches = new ArrayList<Integer>();
		int pageSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
		try {
			// status s= submit
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria cr = session.createCriteria(StorePoM.class)
					.add(Restrictions.eq("masDepartment.departmentId", departmentId))
					.add(Restrictions.eq("masHospital.hospitalId", hospitalId))
					.add(Restrictions.eq("status", "s").ignoreCase());

			if (vendorId != 0) {
				cr = cr.add(Restrictions.eq("masStoreSupplier.supplierId", vendorId));
			}
			totalMatches = cr.list();
			cr.setFirstResult((pageSize) * (pageNo - 1));
			cr.setMaxResults(pageSize);
			poMList = cr.list();
		} catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		map.put("poMList", poMList);
		map.put("totalMatches", totalMatches);
		return map;
	}

	@Override
	public boolean saveOrSubmitSupplyOrderForApproval(JSONObject jObject) {
		boolean flag = false;
		/*
		 * JSONArray requestTypeArr = jObject.getJSONArray("requestType"); String
		 * requestTypeStatus = requestTypeArr.getString(0);
		 * 
		 * JSONArray taxDetailsArr = jObject.getJSONArray("taxDetails"); String
		 * taxDetails = taxDetailsArr.getString(0);
		 * 
		 * JSONArray paymentTermArr = jObject.getJSONArray("paymentTerm"); String
		 * paymentTerm = paymentTermArr.getString(0);
		 * 
		 * JSONArray deliveryScheduleArr = jObject.getJSONArray("deliverySchedule");
		 * String deliverySchedule = deliveryScheduleArr.getString(0);
		 * 
		 * 
		 * JSONArray enterbyArr = jObject.getJSONArray("userId"); long enterbyId =
		 * enterbyArr.getLong(0);
		 * 
		 * JSONArray poMIdArr = jObject.getJSONArray("poMId"); long poMId =
		 * poMIdArr.getLong(0);
		 * 
		 * JSONArray itemId = jObject.getJSONArray("itemId");
		 * 
		 * 
		 * String actionId=""; if(jObject.has("actionId")) { JSONArray actionArr =
		 * jObject.getJSONArray("actionId"); actionId = actionArr.getString(0); }
		 * 
		 * String remarkId=""; if(jObject.has("remarkId")) { JSONArray remarkArr =
		 * jObject.getJSONArray("remarkId"); remarkId = remarkArr.getString(0); }
		 * 
		 * 
		 * 
		 * 
		 * Timestamp currentDate = new Timestamp(new Date().getTime());
		 * 
		 * Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		 * Transaction tx = session.beginTransaction(); try {
		 * 
		 * StorePoM storePoM = (StorePoM) session.get(StorePoM.class, poMId);
		 * if(requestTypeStatus.equalsIgnoreCase("e")) { // save
		 * storePoM.setTaxTerm(taxDetails);
		 * storePoM.setDeliverySchedule(deliverySchedule);
		 * storePoM.setPayTerms(paymentTerm);
		 * 
		 * Users lastchgBy = new Users(); lastchgBy.setUserId(enterbyId);
		 * storePoM.setLastChgBy(lastchgBy);
		 * 
		 * storePoM.setLastChgDate(currentDate);
		 * 
		 * session.update(storePoM);
		 * 
		 * }else { // submit storePoM.setTaxTerm(taxDetails);
		 * storePoM.setDeliverySchedule(deliverySchedule);
		 * storePoM.setPayTerms(paymentTerm);
		 * 
		 * Users lastchgBy = new Users(); lastchgBy.setUserId(enterbyId);
		 * storePoM.setLastChgBy(lastchgBy);
		 * 
		 * if (!actionId.isEmpty() && !actionId.equalsIgnoreCase("0")) {
		 * storePoM.setRemarks(remarkId); storePoM.setStatus(actionId);
		 * storePoM.setApprovedBy(lastchgBy); storePoM.setApprovedDate(currentDate);
		 * 
		 * if (actionId.equalsIgnoreCase("R")) {
		 * removeDataFromTempTableForBackLp(session,storePoM.getStoreQuotationM().
		 * getStoreBudgetaryM()); } if(actionId.equalsIgnoreCase("A")) { for(int
		 * i=0;i<itemId.length();i++) { long tempId =
		 * lpProcessDoa.getTempIdFromBudgetarty(storePoM.getStoreQuotationM().
		 * getStoreBudgetaryM(), Long.parseLong(itemId.getString(i).trim()));
		 * lpProcessDoa.updateTempTableStatus(session, tempId, "supply");
		 * 
		 * }
		 * 
		 * } }else { storePoM.setStatus("s"); } storePoM.setLastChgDate(currentDate);
		 * session.update(storePoM); } tx.commit(); flag=true; }catch(Exception e) {
		 * tx.rollback();; flag=false;
		 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.printStackTrace();
		 * }finally { getHibernateUtils.getHibernateUtlis().CloseConnection(); }
		 */

		return flag;

	}

	@SuppressWarnings("unchecked")
	private void removeDataFromTempTableForBackLp(Session session, StoreBudgetaryM storeBudgetaryM) {
		List<StoreBudgetaryT> StoreBudgetaryTList = new ArrayList<StoreBudgetaryT>();
		try {
			StoreBudgetaryTList = (List<StoreBudgetaryT>) session.createCriteria(StoreBudgetaryT.class)
					.add(Restrictions.eq("storeBudgetaryM.budgetaryMId", storeBudgetaryM.getBudgetaryMId()))
					.add(Restrictions.isNotNull("tempDirectReceivingForBackLp")).list();

			if (StoreBudgetaryTList.size() > 0) {
				for (StoreBudgetaryT budgetaryTObject : StoreBudgetaryTList) {
					TempDirectReceivingForBackLp tempBackObject = null;
					tempBackObject = (TempDirectReceivingForBackLp) session.get(TempDirectReceivingForBackLp.class,
							budgetaryTObject.getTempDirectReceivingForBackLp().getTempId());
					tempBackObject.setStatus(null);
					tempBackObject.setBudgetaryStatus(null);
					tempBackObject.setQuotationStatus(null);
					tempBackObject.setSanctionOrderStatus(null);
					tempBackObject.setSupplyOrderStatus(null);

					session.update(tempBackObject);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getRVWaitingListAgainstSo(long hospitalId, long departmentId, long vendorId,
			String fromDate, String toDate, String soNo, int pageNo) {
		List<StorePoM> poMList = new ArrayList<StorePoM>();
		List<Integer> totalMatches = new ArrayList<Integer>();
		Map<String, Object> map = new HashMap<String, Object>();
		int pageSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria cr = session.createCriteria(StorePoM.class)
					.add(Restrictions.eq("masDepartment.departmentId", departmentId))
					.add(Restrictions.eq("masHospital.hospitalId", hospitalId))
					.add(Restrictions.or(Restrictions.isNull("receivedStatus"),
							Restrictions.eq("receivedStatus", "p").ignoreCase()))
					.add(Restrictions.eq("status", 'A').ignoreCase());

			if (vendorId != 0) {
				cr = cr.add(Restrictions.eq("masStoreSupplier.supplierId", vendorId));
			}
			if (!fromDate.isEmpty()) {
				cr = cr.add(Restrictions.ge("poDate", HMSUtil.convertStringDateToUtilDateForDatabase(fromDate)));
			}
			if (!toDate.isEmpty()) {
				cr = cr.add(Restrictions.le("poDate", HMSUtil.convertStringDateToUtilDateForDatabase(toDate)));
			}

			if (!soNo.isEmpty()) {
				cr = cr.add(Restrictions.eq("poNumber", soNo));
			}
			totalMatches = cr.list();
			cr.setFirstResult((pageSize) * (pageNo - 1));
			cr.setMaxResults(pageSize);
			poMList = cr.list();

		} catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		map.put("poMList", poMList);
		map.put("totalMatches", totalMatches);
		return map;

	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> supplyOrderDetailsForApproval(long poM_headerId) {
		// Create RV Against SO Details
		Map<String, Object> map = new HashMap<String, Object>();
		List<StoreDoInternalIndentM> poMDetails = new ArrayList<StoreDoInternalIndentM>();
		List<StoreDoInternalIndentT> poTDetails = new ArrayList<StoreDoInternalIndentT>();
		List<TempDirectReceivingForBackLp> tempObjectList = new ArrayList<TempDirectReceivingForBackLp>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();

			StoreDoInternalIndentM indentM = (StoreDoInternalIndentM) session.get(StoreDoInternalIndentM.class, poM_headerId);
			poMDetails.add(indentM);

			poTDetails = session.createCriteria(StoreDoInternalIndentT.class).add(Restrictions.eq("storeDoMId", poM_headerId))
					.add(Restrictions.or(Restrictions.isNull("receiveStatus"),
							Restrictions.eq("receiveStatus", "p").ignoreCase()))
					.list();

			if (poMDetails.size() > 0 && poTDetails.size() > 0) {
				/*if (poMDetails.get(0).getLpTypeFlag() != null
						
						&& poMDetails.get(0).getLpTypeFlag().equalsIgnoreCase("B")) {*/
					Collection<StoreDoInternalIndentT> entities = poTDetails;
					List<MasStoreItem> masStoreItemsList = entities.stream().map(StoreDoInternalIndentT::getMasStoreItem)
							.collect(Collectors.toList());

					/*tempObjectList = session.createCriteria(StoreQuotationT.class)
							.add(Restrictions.eq("storeQuotationM", poMDetails.get(0).getStoreQuotationM()))
							.add(Restrictions.in("masStoreItem", masStoreItemsList))
							.setProjection(Projections.property("tempDirectReceivingForBackLp")).list();*/

				//}
			}

			map.put("poMDetails", poMDetails);
			map.put("poTDetails", poTDetails);

		} catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return map;
	}

	@Override
	public long submitRvAgainstSupplyOrder(JSONObject jObject) {
		long grnMId = 0;
		JSONArray requestTypeArr = jObject.getJSONArray("requestType");
		String requestTypeStatus = requestTypeArr.getString(0);

		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Transaction tx = session.beginTransaction();
		try {
			grnMId = saveOrUpdateSToreGrnForSO(session, jObject, requestTypeStatus);
			if (grnMId > 0) {
				tx.commit();
			}
		} catch (Exception e) {
			tx.rollback();
			;
			grnMId = -1;
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return grnMId;
	}

	private long saveOrUpdateSToreGrnForSO(Session session, JSONObject jObject, String requestTypeStatus) {
		long grnMId = 0;
		long returnGrnMId = 0;
		boolean stockFlag = false;
		try {
			JSONArray rvDateArr = jObject.getJSONArray("rvDate");
			String rvDate = rvDateArr.getString(0);

			/*
			 * JSONArray lpTypeFlagArr = jObject.getJSONArray("lpTypeFlag"); String
			 * lpTypeFlagValue = lpTypeFlagArr.getString(0);
			 */

			/*
			 * JSONArray invDateArr = jObject.getJSONArray("invDate"); String invDate =
			 * invDateArr.getString(0);
			 * 
			 * JSONArray invNoMArr = jObject.getJSONArray("invNo"); String invNo =
			 * invNoMArr.getString(0);
			 * 
			 * JSONArray invAmtArr = jObject.getJSONArray("invAmt"); String invAmt =
			 * invAmtArr.getString(0);
			 * 
			 * JSONArray departmentArr = jObject.getJSONArray("departmentId"); long
			 * departmentId = departmentArr.getLong(0);
			 */

			/*
			 * JSONArray hospitaltArr = jObject.getJSONArray("hospitalId"); long hospitalId
			 * = hospitaltArr.getLong(0);
			 */

			JSONArray enterbyArr = jObject.getJSONArray("userIdval");
			long enterbyId = enterbyArr.getLong(0);

			JSONArray poMIdArr = jObject.getJSONArray("poMId");
			long poMId = poMIdArr.getLong(0);

			JSONArray vendorNameArr = jObject.getJSONArray("vendorNameId");
			long vendorNameId = vendorNameArr.getLong(0);

			// GRN_T TABLE

			JSONArray itemId = jObject.getJSONArray("itemId");
			JSONArray poTId = jObject.getJSONArray("poTId");
			JSONArray batchNo = jObject.getJSONArray("batchNo");
			JSONArray doe = jObject.getJSONArray("doe");
			JSONArray dom = jObject.getJSONArray("dom");
			JSONArray soQty = jObject.getJSONArray("soQty");
			JSONArray suppliedQty = jObject.getJSONArray("suppliedQty");
			JSONArray receivedQty = jObject.getJSONArray("receivedQty");
			JSONArray shortQty = jObject.getJSONArray("short");
			JSONArray overQty = jObject.getJSONArray("over");
			JSONArray unitRate = jObject.getJSONArray("unitRate");
			JSONArray totalAmt = jObject.getJSONArray("totalAmt");
			JSONArray manufacturerId = jObject.getJSONArray("manufacturerId");
			/*
			 * JSONArray amtAfterTax = jObject.getJSONArray("amtAfterTax"); JSONArray
			 * taxableValue = jObject.getJSONArray("taxableValue");
			 */
			/*
			 * JSONArray cgstPercent = jObject.getJSONArray("cgstPercent"); JSONArray
			 * cgstAmt = jObject.getJSONArray("cgstAmt"); JSONArray sgstPercent =
			 * jObject.getJSONArray("sgstPercent"); JSONArray sgstAmt =
			 * jObject.getJSONArray("sgstAmt"); JSONArray totalValue =
			 * jObject.getJSONArray("totalValue");
			 * 
			 * JSONArray chemicalCompo = jObject.getJSONArray("chemicalCompo");
			 */

			/* JSONArray detailsRemark = jObject.getJSONArray("detailsRemark"); */

			Timestamp currentDate = new Timestamp(new Date().getTime());

			String tableRowId = "";
			String array_RowId[];

			JSONArray tableRowIdArr = jObject.getJSONArray("hiddenValueCharge");
			JSONArray districtIdArr = jObject.getJSONArray("districtId");
			Long districtId = Long.parseLong(districtIdArr.getString(0));
			System.out.println("districtId********************************"+districtId);

			tableRowId = tableRowIdArr.getString(0);
			array_RowId = tableRowId.split(",");

			StoreGrnM storeGrnM = new StoreGrnM();
			if (requestTypeStatus.equalsIgnoreCase("P")) {
				// Partial Receiving
				storeGrnM = new StoreGrnM();
				storeGrnM.setReceiveType("L"); // LP process receiving
				storeGrnM.setGrnDate(new Timestamp(HMSUtil.convertStringDateToUtilDateForDatabase(rvDate).getTime()));
				//storeGrnM.setInvoiceNo(invNo);
				//storeGrnM.setInvoiceDate(HMSUtil.convertStringDateToUtilDateForDatabase(invDate));
				//storeGrnM.setInvoiceAmount(new BigDecimal(invAmt));
				storeGrnM.setStatus(requestTypeStatus);

				Users lastChgBy = new Users();
				lastChgBy.setUserId(enterbyId);
				storeGrnM.setLastChgBy(lastChgBy);
				storeGrnM.setCreatedBy(lastChgBy);

				storeGrnM.setLastChgDate(currentDate);

				/*
				 * StorePoM poM = new StorePoM(); poM.setPoMId(poMId);
				 */
				storeGrnM.setStoreDoHdId(poMId);

				MasStoreSupplier supplier = new MasStoreSupplier();
				supplier.setSupplierId(vendorNameId);
				storeGrnM.setMasStoreSupplier(supplier);

				/*
				 * if (!lpTypeFlagValue.isEmpty() && lpTypeFlagValue.equalsIgnoreCase("B")) {
				 * storeGrnM.setLpTypeFlag("B"); }
				 */

				long storeGrmId = (long) session.save(storeGrnM);
				returnGrnMId = storeGrmId;
				for (int i = 0; i < array_RowId.length; i++) {
					long qtyReceived = 0;
					if (!receivedQty.getString(i).isEmpty() && Long.parseLong(receivedQty.getString(i)) > 0) {

						qtyReceived = Long.parseLong(receivedQty.getString(i));

						StoreGrnT storeGrnT = new StoreGrnT();

						StoreGrnM grnM = new StoreGrnM();
						grnM.setGrnMId(storeGrmId);
						storeGrnT.setStoreGrnM(grnM);

						MasStoreItem storeItem = new MasStoreItem();
						storeItem.setItemId(itemId.getLong(i));
						storeGrnT.setMasStoreItem(storeItem);

						StoreDoInternalIndentT storePoT = new StoreDoInternalIndentT();
						storePoT.setId(poTId.getLong(i));
						storeGrnT.setStoreDoDtId(poTId.getLong(i));

						storeGrnT.setBatchNo(batchNo.getString(i));
						if(manufacturerId.getString(i) != null && !manufacturerId.getString(i).equals("") && !manufacturerId.getString(i).equals("null")) {
							storeGrnT.setVendorNameId(manufacturerId.getLong(i));
						}
						if (!dom.getString(i).isEmpty()) {
							storeGrnT.setManufacturerDate(
									HMSUtil.convertStringDateToUtilDateForDatabase(dom.getString(i)));
						}
						if (!doe.getString(i).isEmpty()) {
							storeGrnT.setExpiryDate(HMSUtil.convertStringDateToUtilDateForDatabase(doe.getString(i)));
						}

						if (!soQty.getString(i).isEmpty()) {
							storeGrnT.setSupplyOrderQty(soQty.getLong(i));
						}

						if (!suppliedQty.get(i).toString().isEmpty()) {
							storeGrnT.setSuppliedQty(Long.parseLong(suppliedQty.get(i).toString()));
						}

						storeGrnT.setReceivedQty(qtyReceived);

						if (!shortQty.get(i).toString().isEmpty()) {
							storeGrnT.setShortQty(Long.parseLong(shortQty.get(i).toString()));
						}

						if (!overQty.get(i).toString().isEmpty()) {
							storeGrnT.setOverQty(Long.parseLong(overQty.get(i).toString()));
						}

						if (!unitRate.getString(i).isEmpty()) {
							storeGrnT.setUnitRate(new BigDecimal(unitRate.getString(i)));
						}

						if (!totalAmt.get(i).toString().isEmpty()) {
							storeGrnT.setTotalAmount(new BigDecimal(totalAmt.getString(i)));
						}
						/*
						 * if(!amtAfterTax.getString(i).isEmpty()) { storeGrnT.setAmountAfterTax(new
						 * BigDecimal(amtAfterTax.getString(i))); }
						 * if(!taxableValue.getString(i).isEmpty()) { storeGrnT.setTaxableValue(new
						 * BigDecimal(taxableValue.getString(i))); }
						 */

						/*
						 * if (!cgstPercent.getString(i).isEmpty()) { storeGrnT.setCgstPerct(new
						 * BigDecimal(cgstPercent.getString(i))); }
						 * 
						 * if (!cgstAmt.getString(i).isEmpty()) { storeGrnT.setCgstRate(new
						 * BigDecimal(cgstAmt.getString(i))); }
						 * 
						 * if (!sgstPercent.getString(i).isEmpty()) { storeGrnT.setSgstPerct(new
						 * BigDecimal(sgstPercent.getString(i))); }
						 * 
						 * if (!sgstAmt.getString(i).isEmpty()) { storeGrnT.setSgstRate(new
						 * BigDecimal(sgstAmt.getString(i))); }
						 * 
						 * if (!totalValue.getString(i).isEmpty()) { storeGrnT.setTotalValue(new
						 * BigDecimal(totalValue.getString(i))); }
						 * storeGrnT.setChemicalComposittion(chemicalCompo.getString(i));
						 */

						/* storeGrnT.setRemark(detailsRemark.getString(i)); */

						session.save(storeGrnT);

						StoreDoInternalIndentT storePoTObj = (StoreDoInternalIndentT) session.get(StoreDoInternalIndentT.class, poTId.getLong(i));
						long receiviedQtyPre = storePoTObj.getQtyReceived() != null
								? storePoTObj.getQtyReceived(): 0;
						storePoTObj.setQtyReceived(Long.sum(receiviedQtyPre, qtyReceived));
						storePoTObj.setReceiveStatus("P");
						session.update(storePoTObj);

						/*
						 * if (!lpTypeFlagValue.isEmpty() && lpTypeFlagValue.equalsIgnoreCase("B")) { //
						 * No need to update stock. It is already update in direct receiving. stockFlag
						 * = true; } else { stockFlag = updateStoreStockForSupplyOrderDO(i, session,
						 * itemId.getLong(i), batchNo.getString(i), doe.getString(i), dom.getString(i),
						 * qtyReceived, unitRate.getString(i), totalAmt.getString(i), districtId,
						 * enterbyId); }
						 */
						
						stockFlag = updateStoreStockForSupplyOrderDO(i, session, itemId.getLong(i),
								batchNo.getString(i), doe.getString(i), dom.getString(i), qtyReceived,
								unitRate.getString(i), totalAmt.getString(i), districtId, enterbyId);

					}
				}
				StoreDoInternalIndentM storePoMObj = (StoreDoInternalIndentM) session.get(StoreDoInternalIndentM.class, poMId);
				storePoMObj.setStatus(requestTypeStatus);
				session.update(storePoMObj);

			} else {
				// Final Receiving
				storeGrnM = new StoreGrnM();
				//storeGrnM.setReceiveType("L"); // LP process receiving
				storeGrnM.setGrnDate(new Timestamp(HMSUtil.convertStringDateToUtilDateForDatabase(rvDate).getTime()));
				//storeGrnM.setInvoiceNo(invNo);
				//storeGrnM.setInvoiceDate(HMSUtil.convertStringDateToUtilDateForDatabase(invDate));
				//storeGrnM.setInvoiceAmount(new BigDecimal(invAmt));
				storeGrnM.setStatus(requestTypeStatus);

				Users lastChgBy = new Users();
				lastChgBy.setUserId(enterbyId);
				storeGrnM.setLastChgBy(lastChgBy);
				storeGrnM.setCreatedBy(lastChgBy);

				storeGrnM.setLastChgDate(currentDate);

				/*
				 * StorePoM poM = new StorePoM(); poM.setPoMId(poMId);
				 */
				storeGrnM.setStoreDoHdId(poMId);

				/*
				 * MasHospital hospital = new MasHospital(); hospital.setHospitalId(hospitalId);
				 * storeGrnM.setMasHospital(hospital);
				 */

				MasStoreSupplier supplier = new MasStoreSupplier();
				supplier.setSupplierId(vendorNameId);
				storeGrnM.setMasStoreSupplier(supplier);

				/*
				 * MasDepartment department = new MasDepartment();
				 * department.setDepartmentId(departmentId);
				 * storeGrnM.setMasDepartment(department);
				 * 
				 * if (!lpTypeFlagValue.isEmpty() && lpTypeFlagValue.equalsIgnoreCase("B")) {
				 * storeGrnM.setLpTypeFlag("B"); }
				 */

				long storeGrmId = (long) session.save(storeGrnM);
				returnGrnMId = storeGrmId;
				for (int i = 0; i < array_RowId.length; i++) {

					long qtyReceived = 0;
					if (!receivedQty.getString(i).isEmpty() && Long.parseLong(receivedQty.getString(i)) > 0) {
						qtyReceived = Long.parseLong(receivedQty.getString(i));
					}
					StoreGrnT storeGrnT = new StoreGrnT();

					StoreGrnM grnM = new StoreGrnM();
					grnM.setGrnMId(storeGrmId);
					storeGrnT.setStoreGrnM(grnM);

					MasStoreItem storeItem = new MasStoreItem();
					storeItem.setItemId(itemId.getLong(i));
					storeGrnT.setMasStoreItem(storeItem);
					System.out.println("manu************************* "+manufacturerId);
					if(manufacturerId.getString(i) != null && !manufacturerId.getString(i).equals("") && !manufacturerId.getString(i).equals("null")) {
						storeGrnT.setVendorNameId(manufacturerId.getLong(i));
					}
					
					/*
					 * StoreDoInternalIndentT storePoT = new StoreDoInternalIndentT();
					 * storePoT.setPoTId(poTId.getLong(i));
					 */
					storeGrnT.setStoreDoDtId(poTId.getLong(i));

					if (!batchNo.getString(i).isEmpty()) {
						storeGrnT.setBatchNo(batchNo.getString(i));
					}

					if (!dom.getString(i).isEmpty()) {
						storeGrnT.setManufacturerDate(HMSUtil.convertStringDateToUtilDateForDatabase(dom.getString(i)));
					}

					if (!doe.getString(i).isEmpty()) {
						storeGrnT.setExpiryDate(HMSUtil.convertStringDateToUtilDateForDatabase(doe.getString(i)));
					}
					if (!soQty.getString(i).isEmpty()) {

						storeGrnT.setSupplyOrderQty(soQty.getLong(i));
					}

					if (!suppliedQty.get(i).toString().isEmpty()) {
						storeGrnT.setSuppliedQty(Long.parseLong(suppliedQty.get(i).toString()));
					}

					storeGrnT.setReceivedQty(qtyReceived);

					if (!shortQty.get(i).toString().isEmpty()) {
						storeGrnT.setShortQty(Long.parseLong(shortQty.get(i).toString()));
					}

					if (!overQty.get(i).toString().isEmpty()) {
						storeGrnT.setOverQty(Long.parseLong(overQty.get(i).toString()));
					}
					if (!unitRate.getString(i).isEmpty()) {
						storeGrnT.setUnitRate(new BigDecimal(unitRate.getString(i)));
					}

					if (!totalAmt.get(i).toString().isEmpty()) {
						storeGrnT.setTotalAmount(new BigDecimal(totalAmt.getString(i)));
					}
					/*
					 * if(!amtAfterTax.getString(i).isEmpty()) { storeGrnT.setAmountAfterTax(new
					 * BigDecimal(amtAfterTax.getString(i))); }
					 * if(!taxableValue.getString(i).isEmpty()) { storeGrnT.setTaxableValue(new
					 * BigDecimal(taxableValue.getString(i))); }
					 */
					/*
					 * if (!cgstPercent.getString(i).isEmpty()) { storeGrnT.setCgstPerct(new
					 * BigDecimal(cgstPercent.getString(i))); }
					 * 
					 * if (!cgstAmt.getString(i).isEmpty()) { storeGrnT.setCgstRate(new
					 * BigDecimal(cgstAmt.getString(i))); } if (!sgstPercent.getString(i).isEmpty())
					 * { storeGrnT.setSgstPerct(new BigDecimal(sgstPercent.getString(i))); }
					 * 
					 * if (!sgstAmt.getString(i).isEmpty()) { storeGrnT.setSgstRate(new
					 * BigDecimal(sgstAmt.getString(i))); }
					 * 
					 * if (!totalValue.getString(i).isEmpty()) { storeGrnT.setTotalValue(new
					 * BigDecimal(totalValue.getString(i))); }
					 * storeGrnT.setChemicalComposittion(chemicalCompo.getString(i));
					 */

					/* storeGrnT.setRemark(detailsRemark.getString(i)); */

					session.save(storeGrnT);

					StoreDoInternalIndentT storePoTObj = (StoreDoInternalIndentT) session.get(StoreDoInternalIndentT.class, poTId.getLong(i));
					long receiviedQtyPre = storePoTObj.getQtyReceived() != null ? storePoTObj.getQtyReceived(): 0;
					storePoTObj.setQtyReceived(Long.sum(receiviedQtyPre, qtyReceived));
					storePoTObj.setReceiveStatus("F"); 
					session.update(storePoTObj);

					/*
					 * if (!lpTypeFlagValue.isEmpty() && lpTypeFlagValue.equalsIgnoreCase("B")) { //
					 * No need to update stock. It is already update in direct receiving. stockFlag
					 * = true; } else { stockFlag = updateStoreStockForSupplyOrder(i, session,
					 * itemId.getLong(i), batchNo.getString(i), doe.getString(i), dom.getString(i),
					 * qtyReceived, unitRate.getString(i), totalAmt.getString(i), hospitalId,
					 * departmentId, enterbyId); }
					 */
					stockFlag = updateStoreStockForSupplyOrderDO(i, session, itemId.getLong(i), batchNo.getString(i),
							doe.getString(i), dom.getString(i), qtyReceived, unitRate.getString(i),
							totalAmt.getString(i), districtId, enterbyId);

				}

				StoreDoInternalIndentM storePoMObj = (StoreDoInternalIndentM) session.get(StoreDoInternalIndentM.class, poMId);
				//storePoMObj.setReceivedStatus(requestTypeStatus);
				storePoMObj.setStatus("C"); // complete
				session.update(storePoMObj);

			}
			if (stockFlag) {
				grnMId = returnGrnMId;
			}
		} catch (Exception e) {
			grnMId = -1;
			e.printStackTrace();
		}
		return grnMId;
	}

	@SuppressWarnings("unchecked")
	private boolean updateStoreStockForSupplyOrder(int i, Session session, long itemId, String batchNumber,
			String doeDate, String domDate, long qtyReceived, String unitRate, String totalAmount, long hospitalId,
			long departmentId, long userId) {
		boolean flag = false;
		try {
			StoreItemBatchStock stock = null;
			if (!batchNumber.isEmpty() && !doeDate.isEmpty() && qtyReceived > 0) {
				List<StoreItemBatchStock> storeItemBatchList = session.createCriteria(StoreItemBatchStock.class)
						.add(Restrictions.eq("masHospital.hospitalId", hospitalId))
						.add(Restrictions.eq("masDepartment.departmentId", departmentId))
						.add(Restrictions.eq("masStoreItem.itemId", itemId))
						.add(Restrictions.eq("batchNo", batchNumber))
						.add(Restrictions.eq("expiryDate", HMSUtil.convertStringDateToUtilDateForDatabase(doeDate)))
						.list();

				if (storeItemBatchList.size() > 0) {
					stock = storeItemBatchList.get(0);

					long closingBalanceQty = storeItemBatchList.get(0).getClosingStock() != null
							? storeItemBatchList.get(0).getClosingStock()
							: 0;
					long previousReceiveQty = storeItemBatchList.get(0).getReceivedQty() != null
							? storeItemBatchList.get(0).getReceivedQty()
							: 0;

					closingBalanceQty = closingBalanceQty + qtyReceived;
					stock.setClosingStock(closingBalanceQty);

					qtyReceived = previousReceiveQty + qtyReceived;
					stock.setReceivedQty(qtyReceived);

					stock.setLastChgDate(new Timestamp(new Date().getTime()));

					Users lastchgBy = new Users();
					lastchgBy.setUserId(userId);
					stock.setUser(lastchgBy);
					session.update(stock);
				} else {
					stock = new StoreItemBatchStock();

					MasStoreItem item = new MasStoreItem();
					item.setItemId(itemId);
					stock.setMasStoreItem(item);

					stock.setBatchNo(batchNumber);

					MasHospital masHospital = new MasHospital();
					masHospital.setHospitalId(hospitalId);
					stock.setMasHospital(masHospital);

					stock.setClosingStock(qtyReceived);
					stock.setReceivedQty(qtyReceived);
					stock.setMrp(new BigDecimal(unitRate));
					stock.setCostPrice(new BigDecimal((totalAmount)));

					stock.setManufactureDate(HMSUtil.convertStringDateToUtilDateForDatabase(domDate));
					stock.setExpiryDate(HMSUtil.convertStringDateToUtilDateForDatabase(doeDate));

					stock.setLastChgDate(new Timestamp(new Date().getTime()));

					MasDepartment dept = new MasDepartment();
					dept.setDepartmentId(departmentId);
					stock.setMasDepartment(dept);

					Users lastChg = new Users();
					lastChg.setUserId(userId);
					stock.setUser(lastChg);

					session.save(stock);
				}
			}
			flag = true;
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}
	
	@SuppressWarnings("unchecked")
	private boolean updateStoreStockForSupplyOrderDO(int i, Session session, long itemId, String batchNumber,
			String doeDate, String domDate, long qtyReceived, String unitRate, String totalAmount, long districtId,
			long userId) {
		boolean flag = false;
		try {
			StoreItemBatchStock stock = null;
			if (!batchNumber.isEmpty() && !doeDate.isEmpty() && qtyReceived > 0) {
				List<StoreItemBatchStock> storeItemBatchList = session.createCriteria(StoreItemBatchStock.class)
						.add(Restrictions.eq("districtId", districtId))
						.add(Restrictions.eq("masStoreItem.itemId", itemId))
						.add(Restrictions.eq("batchNo", batchNumber))
						.add(Restrictions.eq("expiryDate", HMSUtil.convertStringDateToUtilDateForDatabase(doeDate)))
						.list();

				if (storeItemBatchList.size() > 0) {
					stock = storeItemBatchList.get(0);

					long closingBalanceQty = storeItemBatchList.get(0).getClosingStock() != null
							? storeItemBatchList.get(0).getClosingStock()
							: 0;
					long previousReceiveQty = storeItemBatchList.get(0).getReceivedQty() != null
							? storeItemBatchList.get(0).getReceivedQty()
							: 0;

					closingBalanceQty = closingBalanceQty + qtyReceived;
					stock.setClosingStock(closingBalanceQty);

					qtyReceived = previousReceiveQty + qtyReceived;
					stock.setReceivedQty(qtyReceived);

					stock.setLastChgDate(new Timestamp(new Date().getTime()));

					Users lastchgBy = new Users();
					lastchgBy.setUserId(userId);
					stock.setUser(lastchgBy);
					stock.setDistrictId(districtId); 
					
					session.update(stock);
				} else {
					stock = new StoreItemBatchStock();

					MasStoreItem item = new MasStoreItem();
					item.setItemId(itemId);
					stock.setMasStoreItem(item);

					stock.setBatchNo(batchNumber);

					stock.setClosingStock(qtyReceived);
					stock.setReceivedQty(qtyReceived);
					if(unitRate != null && !unitRate.equals("")) {
						stock.setMrp(new BigDecimal(unitRate));
					}
					if(totalAmount != null && !totalAmount.equals("")) {
						stock.setCostPrice(new BigDecimal((totalAmount)));
					}
					

					stock.setManufactureDate(HMSUtil.convertStringDateToUtilDateForDatabase(domDate));
					stock.setExpiryDate(HMSUtil.convertStringDateToUtilDateForDatabase(doeDate));

					stock.setLastChgDate(new Timestamp(new Date().getTime()));

					MasDepartment dept = new MasDepartment();
					long departmentId = Long.parseLong(HMSUtil.getProperties("adt.properties", "PHARMACY_DEPT_ID").trim());
					dept.setDepartmentId(departmentId);
					stock.setMasDepartment(dept);

					Users lastChg = new Users();
					lastChg.setUserId(userId);
					stock.setUser(lastChg);
					stock.setDistrictId(districtId); 
					session.save(stock);
				}
			}
			flag = true;
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}

	@Override
	public boolean submitDirectReceivingSo(JSONObject jObject) {
		List<Boolean> flagList = new ArrayList<Boolean>();
		boolean submitFlag = false;
		boolean stockFlag = false;
		long itemTypeNivId = 0;
		;

		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Transaction tx = session.beginTransaction();
		try {

			JSONArray rvDateArr = jObject.getJSONArray("recevingDate");
			String rvDate = rvDateArr.getString(0);

			JSONArray vendorArr = jObject.getJSONArray("vendorId");
			long vendorId = vendorArr.getLong(0);

			JSONArray userArr = jObject.getJSONArray("userId");
			long userId = userArr.getLong(0);

			JSONArray departmentArr = jObject.getJSONArray("departmentId");
			long departmentId = departmentArr.getLong(0);

			JSONArray hospitaltArr = jObject.getJSONArray("hospitalId");
			long hospitalId = hospitaltArr.getLong(0);

			JSONArray itemId = jObject.getJSONArray("itemId");
			JSONArray batchNumber = jObject.getJSONArray("batchNumber");
			JSONArray domDate = jObject.getJSONArray("domDate");
			JSONArray doeDate = jObject.getJSONArray("doeDate");
			JSONArray receivedQty = jObject.getJSONArray("quantity");

			JSONArray nipItemId = jObject.getJSONArray("nipItemId");
			JSONArray nipBatchNumber = jObject.getJSONArray("nipBatchNumber");

			JSONArray nipDomDate = jObject.getJSONArray("nipDomDate");
			JSONArray nipDoeDate = jObject.getJSONArray("nipDoeDate");
			JSONArray nipReceivedQty = jObject.getJSONArray("nipQuantity");

			JSONArray nipNewItem = jObject.getJSONArray("newNip");
			JSONArray nipau = jObject.getJSONArray("auNip");

			Timestamp currentDate = new Timestamp(new Date().getTime());

			for (int i = 0; i < itemId.length(); i++) {
				if (!receivedQty.getString(i).isEmpty()) {
					String amount = "0";
					String unitRate = "0";

					TempDirectReceivingForBackLp tempDirRecObject = new TempDirectReceivingForBackLp();

					MasStoreItem storeItem = new MasStoreItem();
					storeItem.setItemId(itemId.getLong(i));
					tempDirRecObject.setMasStoreItem(storeItem);

					tempDirRecObject.setBatchNo(batchNumber.getString(i));

					tempDirRecObject.setDom(HMSUtil.convertStringDateToUtilDateForDatabase(domDate.getString(i)));
					tempDirRecObject.setDoe(HMSUtil.convertStringDateToUtilDateForDatabase(doeDate.getString(i)));

					tempDirRecObject.setReceivedQty(Long.parseLong(receivedQty.getString(i)));

					tempDirRecObject.setAmount(new BigDecimal(amount));

					tempDirRecObject.setReceivingDate(
							new Timestamp(HMSUtil.convertStringDateToUtilDateForDatabase(rvDate).getTime()));

					Users user = new Users();
					user.setUserId(userId);
					tempDirRecObject.setReceivedBy(user);
					tempDirRecObject.setLastChgBy(user);

					MasStoreSupplier supplier = new MasStoreSupplier();
					supplier.setSupplierId(vendorId);
					tempDirRecObject.setMasStoreSupplier(supplier);

					MasHospital hospital = new MasHospital();
					hospital.setHospitalId(hospitalId);
					tempDirRecObject.setMasHospital(hospital);

					MasDepartment department = new MasDepartment();
					department.setDepartmentId(departmentId);
					tempDirRecObject.setMasDepartment(department);

					session.save(tempDirRecObject);

					stockFlag = updateStoreStockForSupplyOrder(i, session, itemId.getLong(i), batchNumber.getString(i),
							doeDate.getString(i), domDate.getString(i), Long.parseLong(receivedQty.getString(i)),
							unitRate, amount, hospitalId, departmentId, userId);

					flagList.add(stockFlag);
				}
			}

			for (int i = 0; i < nipItemId.length(); i++) {
				if (!nipItemId.getString(i).isEmpty()) {
					if (!nipReceivedQty.getString(i).isEmpty()) {
						String unitRate = "0";
						String amount = "0";

						TempDirectReceivingForBackLp tempDirRecObjectNip = new TempDirectReceivingForBackLp();

						MasStoreItem storeItem = new MasStoreItem();
						storeItem.setItemId(nipItemId.getLong(i));
						tempDirRecObjectNip.setMasStoreItem(storeItem);

						tempDirRecObjectNip.setBatchNo(nipBatchNumber.getString(i));

						tempDirRecObjectNip
								.setDom(HMSUtil.convertStringDateToUtilDateForDatabase(nipDomDate.getString(i)));
						tempDirRecObjectNip
								.setDoe(HMSUtil.convertStringDateToUtilDateForDatabase(nipDoeDate.getString(i)));

						tempDirRecObjectNip.setReceivedQty(Long.parseLong(nipReceivedQty.getString(i)));

						tempDirRecObjectNip.setAmount(new BigDecimal(amount));

						tempDirRecObjectNip.setReceivingDate(
								new Timestamp(HMSUtil.convertStringDateToUtilDateForDatabase(rvDate).getTime()));

						Users user = new Users();
						user.setUserId(userId);
						tempDirRecObjectNip.setReceivedBy(user);
						tempDirRecObjectNip.setLastChgBy(user);

						MasStoreSupplier supplier = new MasStoreSupplier();
						supplier.setSupplierId(vendorId);
						tempDirRecObjectNip.setMasStoreSupplier(supplier);

						MasHospital hospital = new MasHospital();
						hospital.setHospitalId(hospitalId);
						tempDirRecObjectNip.setMasHospital(hospital);

						MasDepartment department = new MasDepartment();
						department.setDepartmentId(departmentId);
						tempDirRecObjectNip.setMasDepartment(department);

						session.save(tempDirRecObjectNip);

						stockFlag = updateStoreStockForSupplyOrder(i, session, Long.parseLong(nipItemId.getString(i)),
								nipBatchNumber.getString(i), nipDoeDate.getString(i), nipDomDate.getString(i),
								Long.parseLong(nipReceivedQty.getString(i)), unitRate, amount, hospitalId, departmentId,
								userId);

						flagList.add(stockFlag);
					}
				} else {
					// Adding Niv item into mas_store_item
					if ((!nipNewItem.getString(i).isEmpty())) {
						MasStoreItem msit = new MasStoreItem();
						msit.setHospitalId(hospitalId);
						msit.setLastChgBy(userId);
						msit.setNomenclature(nipNewItem.getString(i));
						String itemTypeCodeNiv = HMSUtil.getProperties("adt.properties", "itemTypeCodeNIP").trim();
						if (itemTypeCodeNiv != null && itemTypeCodeNiv != "") {
							MasItemType mty = getItemTypeIdForNiv(itemTypeCodeNiv);
							itemTypeNivId = mty.getItemTypeId();
						}
						if (itemTypeNivId != 0) {
							msit.setItemTypeId(itemTypeNivId);
						}
						msit.setDispUnitId(Long.parseLong(nipau.getString(i)));
						msit.setItemUnitId(Long.parseLong(nipau.getString(i)));
						msit.setLastChgDate(currentDate);
						msit.setStatus("y");

						long masStoreNivitemId = Long.parseLong(session.save(msit).toString());

						if (masStoreNivitemId != 0) {
							String unitRate = "0";
							String amount = "0";

							TempDirectReceivingForBackLp tempDirRecObjectNip = new TempDirectReceivingForBackLp();

							MasStoreItem storeItem = new MasStoreItem();
							storeItem.setItemId(masStoreNivitemId);
							tempDirRecObjectNip.setMasStoreItem(storeItem);

							tempDirRecObjectNip.setBatchNo(nipBatchNumber.getString(i));

							tempDirRecObjectNip
									.setDom(HMSUtil.convertStringDateToUtilDateForDatabase(nipDomDate.getString(i)));
							tempDirRecObjectNip
									.setDoe(HMSUtil.convertStringDateToUtilDateForDatabase(nipDoeDate.getString(i)));

							tempDirRecObjectNip.setReceivedQty(Long.parseLong(nipReceivedQty.getString(i)));

							tempDirRecObjectNip.setAmount(new BigDecimal(amount));

							tempDirRecObjectNip.setReceivingDate(
									new Timestamp(HMSUtil.convertStringDateToUtilDateForDatabase(rvDate).getTime()));

							Users user = new Users();
							user.setUserId(userId);
							tempDirRecObjectNip.setReceivedBy(user);
							tempDirRecObjectNip.setLastChgBy(user);

							MasStoreSupplier supplier = new MasStoreSupplier();
							supplier.setSupplierId(vendorId);
							tempDirRecObjectNip.setMasStoreSupplier(supplier);

							MasHospital hospital = new MasHospital();
							hospital.setHospitalId(hospitalId);
							tempDirRecObjectNip.setMasHospital(hospital);

							MasDepartment department = new MasDepartment();
							department.setDepartmentId(departmentId);
							tempDirRecObjectNip.setMasDepartment(department);

							session.save(tempDirRecObjectNip);

							stockFlag = updateStoreStockForSupplyOrder(i, session, masStoreNivitemId,
									nipBatchNumber.getString(i), nipDoeDate.getString(i), nipDomDate.getString(i),
									Long.parseLong(nipReceivedQty.getString(i)), unitRate, amount, hospitalId,
									departmentId, userId);

							flagList.add(stockFlag);
						}
					}

				}
			}

			if (!flagList.contains(false)) {
				submitFlag = true;
				tx.commit();
			}
		} catch (Exception e) {
			tx.rollback();
			;
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return submitFlag;
	}

	/*
	 * @Override public long submitDirectReceivingSo(JSONObject jObject) { long
	 * grnMId=0; long returnGrnMId=0; boolean stockFlag =false; long
	 * itemTypeNivId=0;
	 * 
	 * Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * Transaction tx = session.beginTransaction(); try {
	 * 
	 * JSONArray rvDateArr = jObject.getJSONArray("recevingDate"); String rvDate =
	 * rvDateArr.getString(0);
	 * 
	 * JSONArray vendorArr = jObject.getJSONArray("vendorId"); long vendorId =
	 * vendorArr.getLong(0);
	 * 
	 * 
	 * JSONArray userArr = jObject.getJSONArray("userId"); long userId =
	 * userArr.getLong(0);
	 * 
	 * JSONArray departmentArr = jObject.getJSONArray("departmentId"); long
	 * departmentId = departmentArr.getLong(0);
	 * 
	 * JSONArray hospitaltArr = jObject.getJSONArray("hospitalId"); long hospitalId
	 * = hospitaltArr.getLong(0);
	 * 
	 * JSONArray itemId = jObject.getJSONArray("itemId"); JSONArray batchNumber =
	 * jObject.getJSONArray("batchNumber"); JSONArray domDate =
	 * jObject.getJSONArray("domDate"); JSONArray doeDate =
	 * jObject.getJSONArray("doeDate"); JSONArray receivedQty =
	 * jObject.getJSONArray("quantity");
	 * 
	 * 
	 * 
	 * JSONArray nipItemId = jObject.getJSONArray("nipItemId"); JSONArray
	 * nipBatchNumber = jObject.getJSONArray("nipBatchNumber");
	 * 
	 * JSONArray nipDomDate = jObject.getJSONArray("nipDomDate"); JSONArray
	 * nipDoeDate = jObject.getJSONArray("nipDoeDate"); JSONArray nipReceivedQty =
	 * jObject.getJSONArray("nipQuantity");
	 * 
	 * JSONArray nipNewItem = jObject.getJSONArray("newNip"); JSONArray nipau =
	 * jObject.getJSONArray("auNip");
	 * 
	 * 
	 * Timestamp currentDate = new Timestamp(new Date().getTime());
	 * 
	 * StoreGrnM storeGrnM = new StoreGrnM(); storeGrnM = new StoreGrnM();
	 * storeGrnM.setReceiveType("D"); // direct receiving storeGrnM.setGrnDate(new
	 * Timestamp(HMSUtil.convertStringDateToUtilDateForDatabase(rvDate).getTime()));
	 * 
	 * storeGrnM.setStatus("D");
	 * 
	 * Users lastChgBy = new Users(); lastChgBy.setUserId(userId);
	 * storeGrnM.setLastChgBy(lastChgBy); storeGrnM.setCreatedBy(lastChgBy);
	 * 
	 * storeGrnM.setLastChgDate(currentDate);
	 * 
	 * MasStoreSupplier supplier = new MasStoreSupplier();
	 * supplier.setSupplierId(vendorId); storeGrnM.setMasStoreSupplier(supplier);
	 * 
	 * MasHospital hospital = new MasHospital(); hospital.setHospitalId(hospitalId);
	 * storeGrnM.setMasHospital(hospital);
	 * 
	 * MasDepartment department = new MasDepartment();
	 * department.setDepartmentId(departmentId);
	 * storeGrnM.setMasDepartment(department);
	 * 
	 * grnMId = (long) session.save(storeGrnM);
	 * 
	 * for (int i = 0; i < itemId.length(); i++) { long qtyReceived=0; if
	 * (!receivedQty.getString(i).isEmpty()) { // Need to discuss as UI has not
	 * these option String unitRate="0"; String totalAmt="0";
	 * 
	 * StoreGrnT storeGrnT = new StoreGrnT();
	 * 
	 * StoreGrnM grnM = new StoreGrnM(); grnM.setGrnMId(grnMId);
	 * storeGrnT.setStoreGrnM(grnM);
	 * 
	 * MasStoreItem storeItem = new MasStoreItem();
	 * storeItem.setItemId(itemId.getLong(i)); storeGrnT.setMasStoreItem(storeItem);
	 * 
	 * storeGrnT.setUnitRate(new BigDecimal(unitRate));
	 * 
	 * storeGrnT.setBatchNo(batchNumber.getString(i));
	 * 
	 * storeGrnT.setManufacturerDate(HMSUtil.convertStringDateToUtilDateForDatabase(
	 * domDate.getString(i)));
	 * storeGrnT.setExpiryDate(HMSUtil.convertStringDateToUtilDateForDatabase(
	 * doeDate.getString(i)));
	 * 
	 * qtyReceived= Long.parseLong(receivedQty.getString(i));
	 * storeGrnT.setReceivedQty(qtyReceived);
	 * 
	 * session.save(storeGrnT);
	 * 
	 * stockFlag = updateStoreStockForSupplyOrder(i, session, itemId.getLong(i),
	 * batchNumber.getString(i), doeDate.getString(i), domDate.getString(i),
	 * receivedQty.getLong(i), unitRate, totalAmt, hospitalId, departmentId,
	 * userId); }
	 * 
	 * }
	 * 
	 * for (int i = 0; i < nipItemId.length(); i++) {
	 * if(!nipItemId.getString(i).isEmpty()) { long nipQtyReceived=0;
	 * if(!nipReceivedQty.getString(i).isEmpty()) { String unitRate="0"; String
	 * totalAmt="0";
	 * 
	 * StoreGrnT storeGrnT = new StoreGrnT();
	 * 
	 * StoreGrnM grnM = new StoreGrnM(); grnM.setGrnMId(grnMId);
	 * storeGrnT.setStoreGrnM(grnM);
	 * 
	 * MasStoreItem storeItem = new MasStoreItem();
	 * storeItem.setItemId(Long.parseLong(nipItemId.getString(i)));
	 * storeGrnT.setMasStoreItem(storeItem);
	 * 
	 * storeGrnT.setUnitRate(new BigDecimal(unitRate));
	 * 
	 * storeGrnT.setBatchNo(nipBatchNumber.getString(i));
	 * 
	 * storeGrnT.setManufacturerDate(HMSUtil.convertStringDateToUtilDateForDatabase(
	 * nipDomDate.getString(i)));
	 * storeGrnT.setExpiryDate(HMSUtil.convertStringDateToUtilDateForDatabase(
	 * nipDoeDate.getString(i)));
	 * 
	 * nipQtyReceived= Long.parseLong(nipReceivedQty.getString(i));
	 * storeGrnT.setReceivedQty(nipQtyReceived);
	 * 
	 * session.save(storeGrnT);
	 * 
	 * stockFlag = updateStoreStockForSupplyOrder(i, session,
	 * Long.parseLong(nipItemId.getString(i)), nipBatchNumber.getString(i),
	 * nipDoeDate.getString(i), nipDomDate.getString(i), nipQtyReceived, unitRate,
	 * totalAmt, hospitalId, departmentId, userId);
	 * 
	 * } }else { // Adding Niv item into mas_store_item
	 * if((!nipNewItem.getString(i).isEmpty())) { MasStoreItem msit = new
	 * MasStoreItem(); msit.setHospitalId(hospitalId); msit.setLastChgBy(userId);
	 * msit.setNomenclature(nipNewItem.getString(i)); String
	 * itemTypeCodeNiv=HMSUtil.getProperties("adt.properties",
	 * "itemTypeCodeNIP").trim(); if(itemTypeCodeNiv!=null && itemTypeCodeNiv!="") {
	 * MasItemType mty = getItemTypeIdForNiv(itemTypeCodeNiv);
	 * itemTypeNivId=mty.getItemTypeId(); } if(itemTypeNivId!=0) {
	 * msit.setItemTypeId(itemTypeNivId); }
	 * msit.setDispUnitId(Long.parseLong(nipau.getString(i)));
	 * msit.setItemUnitId(Long.parseLong(nipau.getString(i)));
	 * msit.setLastChgDate(currentDate); msit.setStatus("y");
	 * 
	 * long masStoreNivitemId = Long.parseLong(session.save(msit).toString());
	 * 
	 * if(masStoreNivitemId!=0) { long nipQtyReceived=0; String unitRate="0"; String
	 * totalAmt="0";
	 * 
	 * StoreGrnT storeGrnT = new StoreGrnT();
	 * 
	 * StoreGrnM grnM = new StoreGrnM(); grnM.setGrnMId(grnMId);
	 * storeGrnT.setStoreGrnM(grnM);
	 * 
	 * MasStoreItem storeItem = new MasStoreItem();
	 * storeItem.setItemId(masStoreNivitemId); storeGrnT.setMasStoreItem(storeItem);
	 * 
	 * storeGrnT.setUnitRate(new BigDecimal(unitRate));
	 * 
	 * storeGrnT.setBatchNo(nipBatchNumber.getString(i));
	 * 
	 * storeGrnT.setManufacturerDate(HMSUtil.convertStringDateToUtilDateForDatabase(
	 * nipDomDate.getString(i)));
	 * storeGrnT.setExpiryDate(HMSUtil.convertStringDateToUtilDateForDatabase(
	 * nipDoeDate.getString(i)));
	 * 
	 * nipQtyReceived= Long.parseLong(nipReceivedQty.getString(i));
	 * storeGrnT.setReceivedQty(nipQtyReceived);
	 * 
	 * session.save(storeGrnT);
	 * 
	 * stockFlag = updateStoreStockForSupplyOrder(i, session, masStoreNivitemId,
	 * nipBatchNumber.getString(i), nipDoeDate.getString(i),
	 * nipDomDate.getString(i), nipQtyReceived, unitRate, totalAmt, hospitalId,
	 * departmentId, userId);
	 * 
	 * 
	 * } }
	 * 
	 * } }
	 * 
	 * if(stockFlag){ tx.commit(); returnGrnMId = grnMId; } } catch(Exception e) {
	 * tx.rollback();; returnGrnMId=0;
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.printStackTrace();
	 * }finally { getHibernateUtils.getHibernateUtlis().CloseConnection(); }
	 * 
	 * return returnGrnMId; }
	 * 
	 */

	private MasItemType getItemTypeIdForNiv(String itemTypeCodeNiv) {
		Session session = null;
		MasItemType itemTypeIdNiv = null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			itemTypeIdNiv = (MasItemType) session.createCriteria(MasItemType.class)
					.add(Restrictions.eq("itemTypeCode", itemTypeCodeNiv)).uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return itemTypeIdNiv;

	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean deleteRowFromDatabase(long headerMId, long detailTId, String dataFlag) {
		boolean flag = false;
		List<StoreBalanceT> balanceTList = new ArrayList<StoreBalanceT>();
		List<StoreStockTakingT> takingTList = new ArrayList<StoreStockTakingT>();
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Transaction tx = session.beginTransaction();
		try {

			if (detailTId != 0 && dataFlag.equalsIgnoreCase("openingBalanceEntryApproval")) {
				balanceTList = session.createCriteria(StoreBalanceT.class).add(Restrictions.eq("id", detailTId))
						.add(Restrictions.eq("storeBalanceM.id", headerMId)).list();

				if (balanceTList.size() > 0) {
					session.delete(balanceTList.get(0));
					tx.commit();
					flag = true;
				} else {
					flag = false;
				}
			}

			if (detailTId != 0 && dataFlag.equalsIgnoreCase("stockTakingApproval")) {
				takingTList = session.createCriteria(StoreStockTakingT.class)
						.add(Restrictions.eq("takingTId", detailTId))
						.add(Restrictions.eq("storeStockTakingM.takingMId", headerMId)).list();

				if (takingTList.size() > 0) {
					session.delete(takingTList.get(0));
					tx.commit();
					flag = true;
				} else {
					flag = false;
				}
			}
		} catch (Exception e) {
			tx.rollback();
			flag = false;
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		}
		return flag;

	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getPvmsItemForExcel(String requestData) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<MasStoreItem> masStoreItemList = new ArrayList<MasStoreItem>();
		String pvms = null;
		String nomenclature = null;
		String auName = "";
		long item_id = 0;

		List<Long> itemIdList = new ArrayList<Long>();
		List<String> pvmsNoList = new ArrayList<String>();
		List<String> itemNameList = new ArrayList<String>();
		List<String> itemUnitList = new ArrayList<String>();

		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();

		masStoreItemList = session.createCriteria(MasStoreItem.class).add(Restrictions.eq("status", "y")).list();
		if (masStoreItemList.size() > 0) {
			for (MasStoreItem masStoreItem : masStoreItemList) {
				try {
					pvms = masStoreItem.getPvmsNo();
					nomenclature = masStoreItem.getNomenclature();
					auName = masStoreItem.getMasStoreUnit1().getStoreUnitName();
					item_id = masStoreItem.getItemId();

				} catch (Exception e) {
					pvms = "";
					nomenclature = "";
					item_id = 0;
					auName = "";
				}

				itemIdList.add(item_id);
				pvmsNoList.add(pvms);
				itemNameList.add(nomenclature);
				itemUnitList.add(auName);
			}
			map.put("flag", "Data");
		} else {
			map.put("flag", "NoData");
		}
		map.put("itemIdList", itemIdList);
		map.put("pvmsNoList", pvmsNoList);
		map.put("itemNameList", itemNameList);
		map.put("itemUnitList", itemUnitList);

		return map;
	}

	@Override
	public Map<String, Object> importPVMSItemFromExcel(String requestData) {
		boolean rowInsert = false;
		Map<String, Object> map = new HashMap<String, Object>();

		JSONObject responseData = new JSONObject(requestData);

		long mmuId = responseData.getLong("mmuId");
		long userId = responseData.getLong("userId");
		long departmentId = responseData.getLong("departmentId");

		// Re-assigning value in above reference variable
		responseData = (JSONObject) responseData.get("data");

		JSONArray itemIdList = responseData.getJSONArray("itemIdList");

		JSONArray batch1 = responseData.getJSONArray("batch1");
		JSONArray expiryDate1 = responseData.getJSONArray("expiryDate1");
		JSONArray qty1 = responseData.getJSONArray("qty1");

		JSONArray batch2 = responseData.getJSONArray("batch2");
		JSONArray expiryDate2 = responseData.getJSONArray("expiryDate2");
		JSONArray qty2 = responseData.getJSONArray("qty2");

		JSONArray batch3 = responseData.getJSONArray("batch3");
		JSONArray expiryDate3 = responseData.getJSONArray("expiryDate3");
		JSONArray qty3 = responseData.getJSONArray("qty3");

		JSONArray batch4 = responseData.getJSONArray("batch4");
		JSONArray expiryDate4 = responseData.getJSONArray("expiryDate4");
		JSONArray qty4 = responseData.getJSONArray("qty2");

		JSONArray batch5 = responseData.getJSONArray("batch5");
		JSONArray expiryDate5 = responseData.getJSONArray("expiryDate5");
		JSONArray qty5 = responseData.getJSONArray("expiryDate5");

		JSONArray batch6 = responseData.getJSONArray("batch6");
		JSONArray expiryDate6 = responseData.getJSONArray("expiryDate6");
		JSONArray qty6 = responseData.getJSONArray("expiryDate6");

		JSONArray batch7 = responseData.getJSONArray("batch7");
		JSONArray expiryDate7 = responseData.getJSONArray("expiryDate7");
		JSONArray qty7 = responseData.getJSONArray("expiryDate7");

		JSONArray batch8 = responseData.getJSONArray("batch8");
		JSONArray expiryDate8 = responseData.getJSONArray("expiryDate8");
		JSONArray qty8 = responseData.getJSONArray("expiryDate8");

		JSONArray batch9 = responseData.getJSONArray("batch9");
		JSONArray expiryDate9 = responseData.getJSONArray("expiryDate9");
		JSONArray qty9 = responseData.getJSONArray("expiryDate9");

		JSONArray batch10 = responseData.getJSONArray("batch10");
		JSONArray expiryDate10 = responseData.getJSONArray("expiryDate10");
		JSONArray qty10 = responseData.getJSONArray("expiryDate10");

		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Transaction tx = session.beginTransaction();
		try {

			StoreBalanceM balanceM = new StoreBalanceM();
			balanceM.setBalanceDate(new Date());
			balanceM.setMmuId(mmuId);

			MasDepartment department = new MasDepartment();
			department.setDepartmentId(departmentId);
			balanceM.setMasDepartment(department);

			Users user = new Users();
			user.setUserId(userId);
			balanceM.setUser(user);

			balanceM.setLastChgDate(new Timestamp(new Date().getTime()));

			balanceM.setStatus("o");

			long balanceMId = (long) session.save(balanceM);

			if (balanceMId != 0) {
				for (int i = 0; i < itemIdList.length(); i++) {
					if (!batch1.get(i).toString().equals("") && !expiryDate1.get(i).toString().equals("")
							&& !qty1.get(i).toString().equals("")) {
						updateStoreStockDataFromExcel(session, mmuId, departmentId, userId, balanceMId,
								batch1.get(i).toString(), expiryDate1.get(i).toString(), qty1.getLong(i),
								itemIdList.getLong(i));
						rowInsert = true;
					}

					if (!batch2.get(i).toString().equals("") && !expiryDate2.get(i).toString().equals("")
							&& !qty2.get(i).toString().equals("")) {
						updateStoreStockDataFromExcel(session, mmuId, departmentId, userId, balanceMId,
								batch2.get(i).toString(), expiryDate2.get(i).toString(), qty2.getLong(i),
								itemIdList.getLong(i));
						rowInsert = true;
					}

					if (!batch3.get(i).toString().equals("") && !expiryDate3.get(i).toString().equals("")
							&& !qty3.get(i).toString().equals("")) {
						updateStoreStockDataFromExcel(session, mmuId, departmentId, userId, balanceMId,
								batch3.get(i).toString(), expiryDate3.get(i).toString(), qty3.getLong(i),
								itemIdList.getLong(i));
						rowInsert = true;
					}
					if (!batch4.get(i).toString().equals("") && !expiryDate4.get(i).toString().equals("")
							&& !qty4.get(i).toString().equals("")) {
						updateStoreStockDataFromExcel(session, mmuId, departmentId, userId, balanceMId,
								batch4.get(i).toString(), expiryDate4.get(i).toString(), qty4.getLong(i),
								itemIdList.getLong(i));
						rowInsert = true;
					}
					if (!batch5.get(i).toString().equals("") && !expiryDate5.get(i).toString().equals("")
							&& !qty5.get(i).toString().equals("")) {
						updateStoreStockDataFromExcel(session, mmuId, departmentId, userId, balanceMId,
								batch5.get(i).toString(), expiryDate5.get(i).toString(), qty5.getLong(i),
								itemIdList.getLong(i));
						rowInsert = true;
					}
					if (!batch6.get(i).toString().equals("") && !expiryDate6.get(i).toString().equals("")
							&& !qty6.get(i).toString().equals("")) {
						updateStoreStockDataFromExcel(session, mmuId, departmentId, userId, balanceMId,
								batch6.get(i).toString(), expiryDate6.get(i).toString(), qty6.getLong(i),
								itemIdList.getLong(i));
						rowInsert = true;
					}
					if (!batch7.get(i).toString().equals("") && !expiryDate7.get(i).toString().equals("")
							&& !qty7.get(i).toString().equals("")) {
						updateStoreStockDataFromExcel(session, mmuId, departmentId, userId, balanceMId,
								batch7.get(i).toString(), expiryDate7.get(i).toString(), qty7.getLong(i),
								itemIdList.getLong(i));
						rowInsert = true;
					}
					if (!batch8.get(i).toString().equals("") && !expiryDate8.get(i).toString().equals("")
							&& !qty8.get(i).toString().equals("")) {
						updateStoreStockDataFromExcel(session, mmuId, departmentId, userId, balanceMId,
								batch8.get(i).toString(), expiryDate8.get(i).toString(), qty8.getLong(i),
								itemIdList.getLong(i));
						rowInsert = true;
					}
					if (!batch9.get(i).toString().equals("") && !expiryDate9.get(i).toString().equals("")
							&& !qty9.get(i).toString().equals("")) {
						updateStoreStockDataFromExcel(session, mmuId, departmentId, userId, balanceMId,
								batch9.get(i).toString(), expiryDate9.get(i).toString(), qty9.getLong(i),
								itemIdList.getLong(i));
						rowInsert = true;
					}

					if (!batch10.get(i).toString().equals("") && !expiryDate10.get(i).toString().equals("")
							&& !qty10.get(i).toString().equals("")) {
						updateStoreStockDataFromExcel(session, mmuId, departmentId, userId, balanceMId,
								batch10.get(i).toString(), expiryDate10.get(i).toString(), qty10.getLong(i),
								itemIdList.getLong(i));
						rowInsert = true;
					}
				}
			}
			if (rowInsert) {
				tx.commit();
				map.put("msg", "Opening Balance submitted successfully.");
				map.put("status", "1.");
			} else {
				map.put("msg", "No Data in excel to import.");
				map.put("status", "1.");
			}
		} catch (Exception e) {
			tx.rollback();
			map.put("status", "0");
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	private void updateStoreStockDataFromExcel(Session session, Long mmuId, Long departmentId, long userId,
			long balanceMId, String batchNumber, String expiryDate, long qty, long itemId) {

		StoreBalanceT balanceT = new StoreBalanceT();

		StoreBalanceM balM = new StoreBalanceM();
		balM.setId(balanceMId);
		balanceT.setStoreBalanceM(balM);

		balanceT.setQty(qty);
		// balanceT.setUnitRate(new BigDecimal(unitRate.get(i).toString()));
		balanceT.setBatchNo(batchNumber);
		balanceT.setExpiryDate(HMSUtil.convertStringDateToUtilDateForDatabaseForExcel(expiryDate));
		// balanceT.setManufactureDate(HMSUtil.convertStringDateToUtilDateForDatabase(domDate.get(i).toString()));

		MasStoreItem item = new MasStoreItem();
		item.setItemId(itemId);
		balanceT.setMasStoreItem(item);

		Users lastChgBy = new Users();
		lastChgBy.setUserId(userId);
		balanceT.setUser(lastChgBy);

		// balanceT.setTotalAmount(new BigDecimal(totalAmount.get(i).toString()));

		session.save(balanceT);
	}

	@Override
	public Map<String, Object> importPvmsdataReceivedFromiAushadhi(String requestData) {
		boolean rowInsert = false;
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject responseData = new JSONObject(requestData);
		long grnMId = 0;

		long hospitalId = responseData.getLong("hospitalId");
		long userId = responseData.getLong("userId");
		long departmentId = responseData.getLong("departmentId");
		String receivingNo = responseData.get("receivingNo").toString();
		String rvDate = responseData.get("rvDate").toString();

		// Re-assigning value in above reference variable
		responseData = (JSONObject) responseData.get("data");

		JSONArray pvmsNoList = responseData.getJSONArray("pvmsNoList");
		JSONArray qtyReceived = responseData.getJSONArray("qtyReceived");
		JSONArray batchList = responseData.getJSONArray("batchList");
		JSONArray expiryDateList = responseData.getJSONArray("expiryDateList");

		/*
		 * JSONArray qtyDemanded = responseData.getJSONArray("qtyDemanded"); JSONArray
		 * qtyIssued = responseData.getJSONArray("qtyIssued"); JSONArray remarksList =
		 * responseData.getJSONArray("remarksList");
		 */

		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Transaction tx = session.beginTransaction();
		try {

			StoreGrnM storeGrnM = new StoreGrnM();
			storeGrnM = new StoreGrnM();
			storeGrnM.setReceiveType("I"); // iAushadhi receiving
			storeGrnM.setGrnDate(new Timestamp(HMSUtil.convertStringDateToUtilDateForDatabase(rvDate).getTime()));
			storeGrnM.setInvoiceNo(receivingNo);

			storeGrnM.setStatus("I");

			Users lastChgBy = new Users();
			lastChgBy.setUserId(userId);
			storeGrnM.setLastChgBy(lastChgBy);
			storeGrnM.setCreatedBy(lastChgBy);

			Timestamp currentDate = new Timestamp(new Date().getTime());
			storeGrnM.setLastChgDate(currentDate);

			MasHospital hospital = new MasHospital();
			hospital.setHospitalId(hospitalId);
			storeGrnM.setMasHospital(hospital);

			MasDepartment department = new MasDepartment();
			department.setDepartmentId(departmentId);
			storeGrnM.setMasDepartment(department);

			grnMId = (long) session.save(storeGrnM);

			if (grnMId != 0) {
				for (int i = 0; i < pvmsNoList.length(); i++) {
					System.out.println(pvmsNoList.get(i));
					MasStoreItem item = (MasStoreItem) session.createCriteria(MasStoreItem.class)
							.add(Restrictions.eq("pvmsNo", pvmsNoList.get(i))).uniqueResult();

					if (!batchList.get(i).toString().equals("") && !expiryDateList.get(i).toString().equals("")
							&& !qtyReceived.get(i).toString().equals("") && item != null) {
						updateStoreStockDataFromExceliAushadhi(session, hospitalId, departmentId, userId, grnMId,
								batchList.get(i).toString(), expiryDateList.get(i).toString(), qtyReceived.getLong(i),
								item.getItemId());
						rowInsert = true;
					} else {

					}
				}
			}
			if (rowInsert) {
				tx.commit();
				map.put("msg", "Data imported successfully.");
				map.put("status", "1.");
			} else {
				map.put("msg", "No Data in excel to import.");
				map.put("status", "1.");
			}
		} catch (Exception e) {
			tx.rollback();
			map.put("status", "0");
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;

	}

	@SuppressWarnings("unchecked")
	private void updateStoreStockDataFromExceliAushadhi(Session session, Long hospitalId, Long departmentId,
			long userId, long grnMId, String batchNumber, String expiryDate, long qty, long itemId) {

		StoreGrnT storeGrnT = new StoreGrnT();

		StoreGrnM grnM = new StoreGrnM();
		grnM.setGrnMId(grnMId);
		storeGrnT.setStoreGrnM(grnM);

		MasStoreItem storeItem = new MasStoreItem();
		storeItem.setItemId(itemId);
		storeGrnT.setMasStoreItem(storeItem);

		storeGrnT.setUnitRate(new BigDecimal("0"));

		storeGrnT.setBatchNo(batchNumber);

		// storeGrnT.setManufacturerDate(HMSUtil.convertStringDateToUtilDateForDatabase(domDate.getString(i)));
		storeGrnT.setExpiryDate(HMSUtil.convertStringDateToUtilDateForDatabase(expiryDate));

		storeGrnT.setReceivedQty((qty));

		session.save(storeGrnT);

		if (!batchNumber.isEmpty() && !expiryDate.isEmpty() && qty > 0) {
			StoreItemBatchStock storeItemBacthStock = new StoreItemBatchStock();

			List<StoreItemBatchStock> storeItemBatchList = session.createCriteria(StoreItemBatchStock.class)
					.add(Restrictions.eq("masHospital.hospitalId", hospitalId))
					.add(Restrictions.eq("masDepartment.departmentId", departmentId))
					.add(Restrictions.eq("masStoreItem.itemId", itemId)).add(Restrictions.eq("batchNo", batchNumber))
					.add(Restrictions.eq("expiryDate", HMSUtil.convertStringDateToUtilDateForDatabase(expiryDate)))
					.list();
			if (!storeItemBatchList.isEmpty() && storeItemBatchList.size() > 0) {
				storeItemBacthStock = storeItemBatchList.get(0);

				long closingBalanceQty = storeItemBatchList.get(0).getClosingStock() != null
						? storeItemBatchList.get(0).getClosingStock()
						: 0;
				closingBalanceQty = closingBalanceQty + qty;
				storeItemBacthStock.setClosingStock(closingBalanceQty);

				long openingBalanceQty = storeItemBatchList.get(0).getOpeningBalanceQty() != null
						? storeItemBatchList.get(0).getOpeningBalanceQty()
						: 0;
				openingBalanceQty = openingBalanceQty + qty;
				storeItemBacthStock.setOpeningBalanceQty(openingBalanceQty);

				storeItemBacthStock.setLastChgDate(new Timestamp(new Date().getTime()));
				session.update(storeItemBacthStock);

			} else {

				MasStoreItem itemNewAdded = new MasStoreItem();
				itemNewAdded.setItemId(itemId);
				storeItemBacthStock.setMasStoreItem(itemNewAdded);

				storeItemBacthStock.setBatchNo(batchNumber);

				MasHospital masHospital = new MasHospital();
				masHospital.setHospitalId(hospitalId);
				storeItemBacthStock.setMasHospital(masHospital);

				// storeItemBacthStock.setOpeningBalanceQty(qty);
				storeItemBacthStock.setClosingStock(qty);

				storeItemBacthStock.setExpiryDate(HMSUtil.convertStringDateToUtilDateForDatabase(expiryDate));

				storeItemBacthStock.setOpeningBalanceDate(new Date());
				storeItemBacthStock.setLastChgDate(new Timestamp(new Date().getTime()));

				MasDepartment dept = new MasDepartment();
				dept.setDepartmentId(departmentId);
				storeItemBacthStock.setMasDepartment(dept);

				Users lastChg = new Users();
				lastChg.setUserId(userId);
				storeItemBacthStock.setUser(lastChg);

				session.save(storeItemBacthStock);
			}
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MasStoreUnit> getAuNameListForStore() {

		List<MasStoreUnit> auNameList = new ArrayList<MasStoreUnit>();
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		try {
			auNameList = session.createCriteria(MasStoreUnit.class).add(Restrictions.eq("status", "y").ignoreCase())
					.addOrder(Order.asc("storeUnitName")).list();

		} catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return auNameList;

	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getItemListForbackDateBudgetary(String fromDate, String toDate, long hospitalId,
			long departmentId, long supplierId, int pageNo) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<TempDirectReceivingForBackLp> backDateItemsList = new ArrayList<TempDirectReceivingForBackLp>();
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		try {
			Criteria criteria = session.createCriteria(TempDirectReceivingForBackLp.class)
					.add(Restrictions.eq("masHospital.hospitalId", hospitalId))
					.add(Restrictions.eq("masDepartment.departmentId", departmentId))
					.add(Restrictions.isNull("status"));

			if (supplierId != 0) {
				criteria = criteria.add(Restrictions.eq("masStoreSupplier.supplierId", supplierId));
			}
			if (!fromDate.isEmpty()) {
				criteria = criteria.add(
						Restrictions.ge("receivingDate", HMSUtil.convertStringDateToUtilDateForDatabase(fromDate)));
			}
			if (!toDate.isEmpty()) {
				criteria = criteria
						.add(Restrictions.le("receivingDate", HMSUtil.convertStringDateToUtilDateForDatabase(toDate)));
			}

			backDateItemsList = criteria.list();

		} catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		map.put("backDateItemsList", backDateItemsList);
		return map;
	}

	/*
	 * @SuppressWarnings("unchecked")
	 * 
	 * @Override public Map<String, Object> getItemListForbackDateBudgetary(String
	 * fromDate, String toDate, long hospitalId, long departmentId,long supplierId,
	 * int pageNo) { Map<String, Object> map = new HashMap<String, Object>();
	 * List<StoreGrnM> grnMList= new ArrayList<StoreGrnM>(); List<StoreGrnT>
	 * grnTList= new ArrayList<StoreGrnT>(); Session session =
	 * getHibernateUtils.getHibernateUtlis().OpenSession(); try { Criteria criteria
	 * = session.createCriteria(StoreGrnM.class).add(Restrictions.eq(
	 * "masHospital.hospitalId", hospitalId))
	 * .add(Restrictions.eq("masDepartment.departmentId", departmentId))
	 * .add(Restrictions.eq("status", "D").ignoreCase()); if (supplierId != 0) {
	 * criteria = criteria.add(Restrictions.eq("masStoreSupplier.supplierId",
	 * supplierId)); } if (!fromDate.isEmpty()) { criteria =
	 * criteria.add(Restrictions.ge("grnDate",
	 * HMSUtil.convertStringDateToUtilDateForDatabase(fromDate))); } if
	 * (!toDate.isEmpty()) { criteria = criteria.add(Restrictions.le("grnDate",
	 * HMSUtil.convertStringDateToUtilDateForDatabase(toDate))); }
	 * 
	 * grnMList = criteria.list();
	 * 
	 * if(grnMList.size()>0) { Collection<StoreGrnM> entities = grnMList; List<Long>
	 * ids = entities.stream()
	 * .map(StoreGrnM::getGrnMId).collect(Collectors.toList());
	 * 
	 * grnTList= session.createCriteria(StoreGrnT.class).add(Restrictions.in(
	 * "storeGrnM.grnMId", ids))
	 * .add(Restrictions.isNull("budgetaryStatusForBackDate")).list();
	 * 
	 * }
	 * 
	 * }catch(Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.printStackTrace();
	 * }finally { getHibernateUtils.getHibernateUtlis().CloseConnection(); }
	 * map.put("grnTList", grnTList); return map; }
	 * 
	 */

	@Override
	public long createBudgetaryForBackDateLP(JSONObject jObject) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Transaction tx = session.beginTransaction();
		long returnId = 0;
		long budgetaryMId = 0;
		long budgetaryTId = 0;
		boolean flag = false;
		try {
			long hospitalId = jObject.getLong("hospitalId");
			long userId = jObject.getLong("userId");
			long departmentId = jObject.getLong("departmentId");
			long apxCost = jObject.getLong("apxCost");

			String reqDate = jObject.getString("reqDate");

			JSONArray tempIds = jObject.getJSONArray("tempIds");
			JSONArray amountIds = jObject.getJSONArray("amountIds");

			StoreBudgetaryM storeBudgetaryM = new StoreBudgetaryM();

			MasHospital masHospital = new MasHospital();
			masHospital.setHospitalId(hospitalId);
			storeBudgetaryM.setMasHospital(masHospital);

			MasDepartment masDepartment = new MasDepartment();
			masDepartment.setDepartmentId(departmentId);
			storeBudgetaryM.setMasDepartment(masDepartment);

			Users users = new Users();
			users.setUserId(userId);
			storeBudgetaryM.setReqBY(users);

			storeBudgetaryM.setStatus("P");// p=pending
			storeBudgetaryM.setLpTypeFlag("B");// B=BackLP
			storeBudgetaryM.setApproxCost(new BigDecimal(apxCost));
			storeBudgetaryM
					.setReqDate(new Timestamp(HMSUtil.convertStringDateToUtilDateForDatabase(reqDate).getTime()));

			budgetaryMId = (long) session.save(storeBudgetaryM);

			if (budgetaryMId != 0) {
				for (int i = 0; i < tempIds.length(); i++) {
					TempDirectReceivingForBackLp tempBackLpObject = null;
					long itemId = 0;
					long receivedQty = 0;
					flag = false;

					StoreBudgetaryT storeBudgetaryT = new StoreBudgetaryT();

					tempBackLpObject = (TempDirectReceivingForBackLp) session.get(TempDirectReceivingForBackLp.class,
							tempIds.getLong(i));
					if (tempBackLpObject != null) {
						itemId = tempBackLpObject.getMasStoreItem().getItemId();
						receivedQty = tempBackLpObject.getReceivedQty();

						storeBudgetaryT.setStoreBudgetaryM(storeBudgetaryM);

						MasStoreItem mst = new MasStoreItem();
						mst.setItemId(itemId);
						storeBudgetaryT.setMasStoreItem(mst);
						storeBudgetaryT.setQtyRequried(receivedQty);

						storeBudgetaryT.setTempDirectReceivingForBackLp(tempBackLpObject);
						storeBudgetaryT.setQtyRequried(receivedQty);
						budgetaryTId = (long) session.save(storeBudgetaryT);
						flag = true;

						if (budgetaryTId != 0) {
							tempBackLpObject.setStatus("Y");
							tempBackLpObject.setBudgetaryStatus("Y");
							tempBackLpObject.setAmount(new BigDecimal(amountIds.getString(i)));
							session.update(tempBackLpObject);
						}
					}
				}

			}
			if (flag) {
				tx.commit();
				returnId = budgetaryMId;
			}
		} catch (Exception e) {
			returnId = 0;
			tx.rollback();
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return returnId;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getIssuedByName(Long storeIssueIndentMId) {
		String name = "";
		List<StoreIssueM> issueMList = new ArrayList<StoreIssueM>();
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		try {
			issueMList = session.createCriteria(StoreIssueM.class)
					.add(Restrictions.eq("storeInternalIndentM.id", storeIssueIndentMId)).list();
			// name = issueMList.get(0).getUserIssuedBy().getFirstName();

		} catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		}
		return name;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getReceivingIndentWaitingListCo(String fromDate, String toDate, long cityId,
			String indentNo, long departmentId, int pageNo) {

		List<Integer> totalMatches = new ArrayList<Integer>();
		List<StoreIssueM> waitingListData = new ArrayList<StoreIssueM>();
		Map<String, Object> map = new HashMap<String, Object>();
		int pageSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();

			Criteria cr = session.createCriteria(StoreIssueM.class).createAlias("storeCoInternalIndentM", "m1")
				.add(Restrictions.eq("m1.cityId", cityId))
				.add(Restrictions.isNull("status"));
			cr = cr.add(Restrictions.or(Restrictions.eq("m1.status", "I").ignoreCase(), Restrictions.eq("m1.status", "P").ignoreCase()));
			cr = cr.addOrder(Order.desc("issueDate"));
			
			totalMatches = cr.list();
			cr.setFirstResult((pageSize) * (pageNo - 1));
			cr.setMaxResults(pageSize);

			waitingListData = cr.list();
		} catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		map.put("waitingListData", waitingListData);
		map.put("totalMatches", totalMatches);
		return map;

	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> receivingIndentInInventoryCo(long internalM_headerId, long storeIssueMId) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<StoreCoInternalIndentM> internalMList = new ArrayList<StoreCoInternalIndentM>();
		List<StoreIssueM> issueMList = new ArrayList<StoreIssueM>();
		List<StoreIssueT> issueTList = new ArrayList<StoreIssueT>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();

			internalMList = session.createCriteria(StoreCoInternalIndentM.class)
					.add(Restrictions.eq("id", internalM_headerId)).list();

			issueMList = session.createCriteria(StoreIssueM.class)
					.add(Restrictions.eq("id", storeIssueMId))
					.add(Restrictions.eq("storeCoInternalIndentM.id", internalM_headerId)).list();


				issueTList = session.createCriteria(StoreIssueT.class).add(Restrictions.eq("storeIssueM.id", storeIssueMId))
						.list();

			map.put("internalMList", internalMList);
			map.put("issueTList", issueTList);
			map.put("issueMList", issueMList);

		} catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean addToStockIssuedIndentCo(JSONObject formData) {
		boolean flag = false;

		JSONArray itemId = formData.getJSONArray("itemId");
		JSONArray issueTId = formData.getJSONArray("issueTId");
		JSONArray batchNumber = formData.getJSONArray("batchNumber");
		JSONArray doeDate = formData.getJSONArray("doe");
		JSONArray domDate = formData.getJSONArray("dom");
		JSONArray qtyReceived = formData.getJSONArray("qtyReceived");
		JSONArray qtyReceived2 = formData.getJSONArray("qtyReceived");
		JSONArray indentTId = formData.getJSONArray("indentTId");
		JSONArray stockId = formData.getJSONArray("stockId");

		/*
		 * JSONArray issueTId = formData.getJSONArray("issueTId"); JSONArray qtyDemand =
		 * formData.getJSONArray("qtyDemand"); JSONArray qtyIssued =
		 * formData.getJSONArray("qtyIssued");
		 */

		JSONArray userArr = formData.getJSONArray("userId");
		long userId = userArr.getLong(0);

		JSONArray indentMIArr = formData.getJSONArray("indentMId");
		long indentMId = indentMIArr.getLong(0);

		JSONArray city = formData.getJSONArray("cityId");
		long cityId = city.getLong(0);
		
		JSONArray storeIssueMArr = formData.getJSONArray("storeIssueMId");
		long storeIssueMId = storeIssueMArr.getLong(0);

		Date currentDate = HMSUtil
				.convertStringDateToUtilDateForDatabase(formData.getJSONArray("currentDate").getString(0));

		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Transaction tx = session.beginTransaction();
		try {
			for (int i = 0; i < itemId.length(); i++) {
				StoreItemBatchStock stock = null;
				if (!batchNumber.getString(i).isEmpty() && !doeDate.getString(i).isEmpty()
						&& qtyReceived.getLong(i) > 0) {
					List<StoreItemBatchStock> storeItemBatchList = session.createCriteria(StoreItemBatchStock.class)
							.add(Restrictions.eq("cityId", cityId))
							.add(Restrictions.eq("masStoreItem.itemId", itemId.getLong(i)))
							.add(Restrictions.eq("batchNo", batchNumber.getString(i))).add(Restrictions.eq("expiryDate",
									HMSUtil.convertStringDateToUtilDateForDatabase(doeDate.getString(i))))
							.list();

					if (!storeItemBatchList.isEmpty() && storeItemBatchList.size() > 0) {
						stock = storeItemBatchList.get(0);
						long closingBalanceQty = storeItemBatchList.get(0).getClosingStock() != null
								? storeItemBatchList.get(0).getClosingStock()
								: 0;
						closingBalanceQty = closingBalanceQty + qtyReceived.getLong(i);
						stock.setClosingStock(closingBalanceQty);

						long receivedQty = storeItemBatchList.get(0).getReceivedQty() != null
								? storeItemBatchList.get(0).getReceivedQty()
								: 0;
						receivedQty = receivedQty + qtyReceived.getLong(i);
						stock.setReceivedQty(receivedQty);

						stock.setLastChgDate(new Timestamp(new Date().getTime()));
						Users lastChg = new Users();
						lastChg.setUserId(userId);
						stock.setUser(lastChg);
						stock.setCityId(cityId);
						MasDepartment dept = new MasDepartment();
						long departmentId = Long.parseLong(HMSUtil.getProperties("adt.properties", "PHARMACY_DEPT_ID").trim());
						//long departmentId = 2051;
						dept.setDepartmentId(departmentId);
						stock.setMasDepartment(dept);
						session.update(stock);
					} else {
						stock = new StoreItemBatchStock();
						BigDecimal unitRate = null;
						BigDecimal totalAmount = null;
						stock.setCityId(cityId);
						/*
						 * StoreItemBatchStock storeStock =
						 * (StoreItemBatchStock)session.get(StoreItemBatchStock.class,
						 * stockId.getLong(i)); if(storeStock!=null && storeStock.getMrp()!=null) {
						 * unitRate = storeStock.getMrp(); totalAmount =
						 * unitRate.multiply(BigDecimal.valueOf(qtyReceived.getLong(i)));
						 * stock.setMrp(unitRate); stock.setCostPrice(totalAmount); }
						 */

						MasStoreItem item = new MasStoreItem();
						item.setItemId(itemId.getLong(i)); // item.setItemId(Long.parseLong(itemId.get(i).toString()));
						stock.setMasStoreItem(item);

						stock.setBatchNo(batchNumber.get(i).toString());

						stock.setClosingStock(qtyReceived.getLong(i));
						stock.setReceivedQty(qtyReceived.getLong(i));

						if (domDate.get(i) != null && !domDate.get(i).toString().isEmpty()) {
							stock.setManufactureDate(
									HMSUtil.convertStringDateToUtilDateForDatabase(domDate.get(i).toString()));
						}

						stock.setExpiryDate(HMSUtil.convertStringDateToUtilDateForDatabase(doeDate.get(i).toString()));

						stock.setLastChgDate(new Timestamp(new Date().getTime()));

						/*
						 * MasDepartment dept = new MasDepartment(); dept.setDepartmentId(departmentId);
						 * stock.setMasDepartment(dept);
						 */

						Users lastChg = new Users();
						lastChg.setUserId(userId);
						stock.setUser(lastChg);
						MasDepartment dept = new MasDepartment();
						long departmentId = Long.parseLong(HMSUtil.getProperties("adt.properties", "PHARMACY_DEPT_ID").trim());
						dept.setDepartmentId(departmentId);
						stock.setMasDepartment(dept);
						session.save(stock);

					}

				}

				long itemQtyReceived = (long) session.createCriteria(StoreIssueT.class)
						.add(Restrictions.eq("storeCoInternalIndentT.id", indentTId.getLong(i)))
						.setProjection(Projections.sum("qtyRequest")).uniqueResult();

				/*
				 * List<StoreInternalIndentT> tList = session.createCriteria(StoreIssueT.class)
				 * .createAlias("storeInternalIndentT", "t").add(Restrictions.eq("t.id",
				 * indentTId.getLong(i))) .list();
				 * 
				 * long itemQtyReceived = 0; if (!tList.isEmpty() && tList.size() > 0) { for
				 * (StoreInternalIndentT st : tList) { itemQtyReceived = itemQtyReceived +
				 * st.getQtyRequest(); } }
				 */
				StoreIssueT sIssueT = (StoreIssueT) session.get(StoreIssueT.class, issueTId.getLong(i));
				if(sIssueT != null) {
					sIssueT.setQtyReceived(qtyReceived2.getLong(i)); 
					sIssueT.setReceivedBy(userId);
					sIssueT.setReceivedDate(new Date());
					session.update(sIssueT);
				}
				
				StoreCoInternalIndentT internalIndentT = (StoreCoInternalIndentT) session.get(StoreCoInternalIndentT.class,
						indentTId.getLong(i));
				if (internalIndentT != null) {
					internalIndentT.setAvailableStock(
							(internalIndentT.getAvailableStock() != null ? internalIndentT.getAvailableStock() : 0)
									+ itemQtyReceived);
					if(internalIndentT.getQtyReceived() == null) {
						internalIndentT.setQtyReceived(itemQtyReceived);
					}else {
						internalIndentT.setQtyReceived(internalIndentT.getQtyReceived() + itemQtyReceived);
					}
					
					session.save(internalIndentT);
				}

			}
			
			StoreIssueM storeIssueM = (StoreIssueM) session.get(StoreIssueM.class, storeIssueMId);
			if(storeIssueM != null) {
				storeIssueM.setStatus("R");
				session.update(storeIssueM);
			}

			StoreCoInternalIndentM internalIndentM = (StoreCoInternalIndentM) session.get(StoreCoInternalIndentM.class,
					indentMId);
			if (internalIndentM != null) {
				String status = internalIndentM.getStatus();
				if(status.equalsIgnoreCase("I")) {
					List<StoreIssueM> storeMList = session.createCriteria(StoreIssueM.class)
							.add(Restrictions.ne("id", storeIssueMId))
							.add(Restrictions.eq("indentCoMId", indentMId))
							.add(Restrictions.isNull("status")).list();
					
					if(storeMList.isEmpty()) {
						internalIndentM.setStatus("R"); 
						internalIndentM.setReceivedDate(new Timestamp(currentDate.getTime()));
					}
				}
				
				session.update(internalIndentM);
			}

			tx.commit();
			flag = true;

		} catch (Exception e) {
			indentMId = 0;
			tx.rollback();
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return flag;

	}

	@Override
	public List<Object[]> generateStockStatusReportDo(String radioSelectValue, long itemId, long groupId,
			long sectionId, long districtId, long deptId) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		System.out.println("calling*********************************************");
		List<Object[]> objectList = new ArrayList<Object[]>();
		String qry = "";
		String query = "";

		try {

			if (groupId != 0) {
				query = query + " and mas_store_item.group_id = :group";
			}

			if (sectionId != 0) {
				query = query + " and mas_store_item.section_id = :section";
			}
			if (itemId != 0) {
				query = query + " and mas_store_item.item_id = :item";
			}

			query = query + " and store_item_batch_stock.CLOSING_STOCK>0";

			if (radioSelectValue.equalsIgnoreCase("D")) {
				qry = "select  sum(coalesce(store_item_batch_stock.received_qty,0)) as batch_stock_received_qty,"
						+ "sum(coalesce(store_item_batch_stock.issue_qty,0)) as batch_stock_issue_qty,"
						+ "sum(coalesce(store_item_batch_stock.opening_balance_qty,0)) as batch_stock_opning_balnce_qty,"
						+ "sum(store_item_batch_stock.CLOSING_STOCK) as balance_qty,"
						+ "mas_store_item.pvms_no,mas_store_item.nomenclature,store_item_batch_stock.batch_no,store_item_batch_stock.expiry_date, "
						+ "mas_store_unit.store_unit_name,store_item_batch_stock.manufacture_date,"
						+ "mas_supplier.supplier_name,msst.supplier_Type_Name"
						+ " from mas_store_item mas_store_item "
						+ "inner join store_item_batch_stock store_item_batch_stock on mas_store_item.item_id = store_item_batch_stock.item_id"
						+ " left outer join mas_store_unit mas_store_unit on mas_store_item.item_unit_id=mas_store_unit.store_unit_id "
						+ " left outer join MAS_STORE_SUPPLIER mas_supplier on mas_supplier.SUPPLIER_ID=store_item_batch_stock.supplier_id "
						+ " left outer join MAS_STORE_SUPPLIER_TYPE msst on msst.SUPPLIER_TYPE_ID=store_item_batch_stock.supplier_type_id "
						+ " where store_item_batch_stock.department_id ='" + deptId + "'" + query
						+ " and store_item_batch_stock.district_id = '" + districtId + "' "
						+ " group by store_item_batch_stock.item_id,mas_store_item.pvms_no,mas_store_item.nomenclature, "
						+ " store_item_batch_stock.batch_no,store_item_batch_stock.expiry_date,mas_store_unit.store_unit_name,store_item_batch_stock.manufacture_date, "
						+ " mas_supplier.supplier_name,msst.supplier_Type_Name  "
						+ " order by  mas_store_item.pvms_no,mas_store_item.nomenclature";
			} else {
				qry = "select  sum(coalesce(store_item_batch_stock.received_qty,0)) as batch_stock_received_qty, "
						+ "sum(coalesce(store_item_batch_stock.issue_qty,0)) as batch_stock_issue_qty, "
						+ "sum(coalesce(store_item_batch_stock.opening_balance_qty,0)) as batch_stock_opning_balnce_qty, "
						+ "sum(store_item_batch_stock.CLOSING_STOCK) as balance_qty, "
						+ "mas_store_item.pvms_no as mas_store_item_pvms_no,mas_store_item.nomenclature as mas_store_item_nomenclature, "
						+ "mas_store_unit.store_unit_name "
						+ " from mas_store_item mas_store_item "
						+ "inner join store_item_batch_stock store_item_batch_stock on mas_store_item.item_id = store_item_batch_stock.item_id "
						+ " left outer join mas_store_unit mas_store_unit on mas_store_item.item_unit_id=mas_store_unit.store_unit_id "
						+ "where store_item_batch_stock.department_id ='" + deptId + "'" + query
						+ " and store_item_batch_stock.district_id = '" + districtId + "' "
						+ " group by mas_store_item.pvms_no,mas_store_item.nomenclature,mas_store_unit.store_unit_name  "
						+ " order by  mas_store_item_pvms_no,mas_store_item.nomenclature";
			}

			Query queryHiber = (Query) session.createSQLQuery(qry);

			if (groupId != 0) {
				queryHiber = queryHiber.setParameter("group", groupId);
			}

			if (sectionId != 0) {
				queryHiber = queryHiber.setParameter("section", sectionId);
			}

			if (itemId != 0) {
				queryHiber = queryHiber.setParameter("item", itemId);
			}

			/*
			 * if (hospitalList.length>0){ queryHiber = queryHiber.setParameter("hospital",
			 * hospitalList); }
			 */

			objectList = (List<Object[]>) queryHiber.list();

		} catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return objectList;
	}

}
