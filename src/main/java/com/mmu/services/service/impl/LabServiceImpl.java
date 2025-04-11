package com.mmu.services.service.impl;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mmu.services.dao.LabDao;
import com.mmu.services.dao.OpdMasterDao;
import com.mmu.services.dao.impl.OpdMasterDaoImpl;
import com.mmu.services.entity.DgFixedValue;
import com.mmu.services.entity.DgMasInvestigation;
import com.mmu.services.entity.DgNormalValue;
import com.mmu.services.entity.DgOrderdt;
import com.mmu.services.entity.DgOrderhd;
import com.mmu.services.entity.DgResultEntryDt;
import com.mmu.services.entity.DgResultEntryHd;
import com.mmu.services.entity.DgSampleCollectionDt;
import com.mmu.services.entity.DgSampleCollectionHd;
import com.mmu.services.entity.DgSubMasInvestigation;
import com.mmu.services.entity.MasCamp;
import com.mmu.services.entity.MasIdentificationType;
import com.mmu.services.entity.MasMainChargecode;
import com.mmu.services.entity.Patient;
import com.mmu.services.hibernateutils.GetHibernateUtils;
import com.mmu.services.service.LabService;
import com.mmu.services.utils.HMSUtil;
import com.mmu.services.utils.JavaUtils;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

@Service("labService")
public class LabServiceImpl implements LabService {

	@Autowired
	LabDao labDao;

	@Autowired
	GetHibernateUtils getHibernateUtils;

	@Autowired
	OpdMasterDao opdMasterDao;

	public static String getReplaceString(String replaceValue) {
		return replaceValue.replaceAll("[\\[\\]]", "");
	}

	// onload
	@SuppressWarnings("unchecked")
	@Override
	public String getPendingSampleCollectionWaitingListGrid(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		Map<String, Object> mapObject = labDao.getPendingSampleCollectionWaitingListGrid(jsonObject);
		int count = (int) mapObject.get("count");

		List list = new ArrayList();
		String patientId = "";
		String serviceno = "";
		String patientname = "";
		Long relationId = null;
		String labMark = "";
		String headerId = "";
		Date orderdate;
		String orderdate1 = "";
		String empname = "";
		String departmentName = "";
		String relationName = "";
		String mobileNumber = "";
		Date dateOfBirth;
		String age = "";
		String gender = "";
		Long departmentId = null;
		String orderNo = "";

		if (mapObject != null) {
			List<Object[]> dgOrderhdList = (List<Object[]>) mapObject.get("dgOrderhdsList");
			if (CollectionUtils.isNotEmpty(dgOrderhdList)) {
				for (Object row : dgOrderhdList) {
					Map<String, Object> mapdata = new LinkedHashMap<String, Object>();

					Object[] rows = (Object[]) row;
					if (rows[0] != null) {
						// patientId = rows[0].toString();
						mapdata.put("patientId", rows[0].toString());
					}

					if (rows[1] != null) {
						// patientname = rows[2].toString();
						mapdata.put("patientname", rows[1].toString());
					}

					if (rows[2] != null) {
						// patientname = rows[2].toString();
						mapdata.put("headerId", rows[2].toString());
					}

					if (rows[3] != null) {
						orderdate = (Date) rows[3];
						orderdate1 = HMSUtil.convertDateToStringFormat(orderdate, "dd/MM/yyyy");
						mapdata.put("orderdate", orderdate1);
					} else {
						mapdata.put("orderdate", "");
					}

					if (rows[4] != null) {
						departmentName = rows[5].toString();
						mapdata.put("departmentName", rows[4].toString());
					} else {
						mapdata.put("departmentName", "");
					}

					if (rows[5] != null) {
						// mobileNumber = rows[9].toString();
						mapdata.put("mobileNumber", rows[5].toString());
					} else {
						mapdata.put("mobileNumber", "");
					}

					//System.out.println("dob="+rows[6]);
					if (rows[6] != null) {
						
						
						//pt.put("ageFull", ageFull);
						dateOfBirth = (Date) rows[6]; 
						
						String ageFull = HMSUtil.calculateAge(dateOfBirth);					
						
						
						if (rows[6] != null) {

							if (dateOfBirth != null) {
								
								mapdata.put("age", ageFull);
							} else {
								mapdata.put("age", "");
							}
						}

					} else {
						mapdata.put("age", "");
					}

					if (rows[7] != null) {
						// gender = rows[11].toString();
						mapdata.put("gender", rows[7].toString());
					} else {
						mapdata.put("gender", "");
					}

					if (rows[8] != null) {
						mapdata.put("orderNo", Long.parseLong(StringUtils.trim(rows[8].toString())));
					} else {
						mapdata.put("orderNo", 0);
					}

					// mapdata.put("patientId", patientId);
					// mapdata.put("serviceno", serviceno);
					// mapdata.put("patientname", patientname);
					mapdata.put("labMark", labMark);
					// mapdata.put("headerId", headerId);
					// mapdata.put("orderdate", orderdate1);
					// mapdata.put("empname", empname);
					// mapdata.put("departmentName", departmentName);
					mapdata.put("departmentId", departmentId);
					// mapdata.put("relationName", relationName);
					// mapdata.put("mobileNumber", mobileNumber);
					// mapdata.put("age", age);
					// mapdata.put("gender", gender);
					// mapdata.put("orderNo", orderNo);

					list.add(mapdata);

				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("status", 1);
					json.put("count", count);
					json.put("msg", "Record Found Successfully");
				} else {
					json.put("data", list);
					json.put("status", "0");
					json.put("count", 0);
					json.put("msg", "Record Not Found");
				}
			} else if (CollectionUtils.isEmpty(dgOrderhdList)) {
				json.put("status", 0);
				json.put("count", list.size());
				json.put("msg", "No Record Found");
			}

		} else {
			json.put("error", "key does not exist");
		}

		return json.toString();
	}

	/**
	 * @Description : Method getPendingSampleCollection(), Waiting List of Pending
	 *              SampleCollection
	 * @param: JSONObject jsonObject
	 * @param: HttpServletRequest request, HttpServletResponse response
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String getPendingSampleCollection(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		Map<String, List<?>> sampleCollectionDetailMap = null;

		if (jsonObject != null) {
			try {

				sampleCollectionDetailMap = labDao.getPendingSampleCollection(jsonObject);

				if (sampleCollectionDetailMap != null) {
					if (sampleCollectionDetailMap.containsKey("dgOrderhdsList")) {
						List<Object[]> dgOrderhdList = (List<Object[]>) sampleCollectionDetailMap.get("dgOrderhdsList");

						@SuppressWarnings("rawtypes")
						List list = new ArrayList();
						String patientId = "";
						String serviceno = "";
						String patientname = "";
						Long relationId = null;
						String labMark = "";
						String headerId = "";
						Date orderdate;
						String orderdate1 = "";
						String empname = "";
						String departmentName = "";
						String relationName = "";
						String mobileNumber = "";
						Date dateOfBirth;
						String age = "";
						String gender = "";
						Long departmentId = null;
						Long orderNo = null;

						if (CollectionUtils.isNotEmpty(dgOrderhdList)) {

							for (Object row : dgOrderhdList) {
								Map<String, Object> mapdata = new LinkedHashMap<String, Object>();

								Object[] rows = (Object[]) row;
								if (rows[0] != null) {
									patientId = rows[0].toString();
								}
								if (rows[1] != null) {
									serviceno = rows[1].toString();
								}
								if (rows[2] != null) {
									patientname = rows[2].toString();
								}
								if (rows[3] != null) {
									relationId = Long.parseLong(rows[3].toString());
								}
								if (rows[4] != null) {
									labMark = rows[4].toString();
								}
								if (rows[5] != null) {
									headerId = rows[5].toString();
								}
								if (rows[6] != null) {
									orderdate = (Date) rows[6];
									orderdate1 = HMSUtil.convertDateToStringFormat(orderdate, "dd/MM/yyyy");
								}
								if (rows[7] != null) {
									empname = rows[7].toString();
								}
								if (rows[8] != null) {
									departmentName = rows[8].toString();
								}

								if (rows[9] != null) {
									mobileNumber = rows[9].toString();
								}
								if (rows[10] != null) {
									
									
									//pt.put("ageFull", ageFull);
									dateOfBirth = (Date) rows[10]; 
									
									String ageFull = HMSUtil.calculateAge(dateOfBirth);					
									
									
									if (rows[10] != null) {

										if (dateOfBirth != null) {
											
											mapdata.put("age", ageFull);
										} else {
											mapdata.put("age", "");
										}
									}

								} else {
									mapdata.put("age", "");
								}
								if (rows[11] != null) {
									gender = rows[11].toString();
								}

								if (rows[12] != null) {
									orderNo = Long.parseLong(rows[12].toString());
								}
								if (rows[13] != null) {
									relationName = rows[13].toString();
								}

								mapdata.put("patientId", patientId);
								mapdata.put("serviceno", serviceno);
								mapdata.put("patientname", patientname);
								mapdata.put("labMark", labMark);
								mapdata.put("headerId", headerId);
								mapdata.put("orderdate", orderdate1);
								mapdata.put("empname", empname);
								mapdata.put("departmentName", departmentName);
								mapdata.put("departmentId", departmentId);
								mapdata.put("relationName", relationName);
								mapdata.put("mobileNumber", mobileNumber);
								mapdata.put("age", age);
								mapdata.put("gender", gender);
								mapdata.put("orderNo", orderNo);
								mapdata.put("relationId", relationId);

								list.add(mapdata);

							}

							if (list != null && list.size() > 0) {
								json.put("data", list);
								json.put("status", 1);
								json.put("count", list.size());
								json.put("msg", "Record Found Successfully");
							} else {
								json.put("data", list);
								json.put("status", "0");
								json.put("count", list.size());
								json.put("msg", "No Record Found");
							}
						} else if (CollectionUtils.isEmpty(dgOrderhdList)) {
							json.put("status", 0);
							json.put("count", list.size());
							json.put("msg", "No Record Found");
						}

					}
				} else {
					json.put("error", "key does not exist");
				}

			} catch (Exception e) {
				e.printStackTrace(System.out);
			}
		} else {
			json.put("status", "jsonObjectNull");
			json.put("msg", "jsonObject is null");

		}
		return json.toString();
	}

	/**
	 * @Description: getPendingSampleCollectionDetails(), details of the pending
	 *               sample collection service No wise.
	 * @param: JSONObject jsonObject
	 * @param: HttpServletRequest request, HttpServletResponse response
	 * @return: json.toString()
	 */
	@Override
	public String getPendingSampleCollectionDetails(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		Map<String, List<?>> sampleCollDetailsMap = null;
		List<?> sampleCollectionDtlsList = null;
		List<Object> list = new ArrayList<Object>();
		List<Object> investigationlists = new ArrayList<Object>();
		try {
			sampleCollDetailsMap = labDao.getPendingSampleCollectionDetails(jsonObject);
			List<?> investigationList = labDao.getInvestigationListdgOrderdtwise(jsonObject);

			String orderhdId = "";
			Date orderdate1;
			String lastChgDate = "";
			String departmentName = "";
			String serviceNo = "";
			Long patientId = null;
			String patientName = "";
			Long relationId = null;
			String relationName = "";
			String employeeName = "";
			Date dateOfBirth;
			String gender = "";
			String mobileNumber = "";
			String labMark = "";
			String orderdate = "";
			String age = "";
			String createdBy = "";
			String currentTime = "";
			String departmentId = "";
			String orderStatus = "";

			String orderdtId = "";
			String orderhdIdforInv = "";
			String investigationId = "";
			String investigationName = "";
			String sampleCode = "";
			Long sampleId = null;
			String sampleDesc = "";
			String subChargeCodeName = "";
			String labMarkForInv = "";
			String subChargeCodeId = "";
			String orderNo = "";
			Long collectionId = null;
			String collectionName = null;
			String rankName = null;
			String lastChgBy = null;

			if (sampleCollDetailsMap != null) {
				if (sampleCollDetailsMap.containsKey("pendingSampleCollectionDtlsLists")) {
					sampleCollectionDtlsList = sampleCollDetailsMap.get("pendingSampleCollectionDtlsLists");
					if (CollectionUtils.isNotEmpty(sampleCollectionDtlsList)) {
						for (Iterator<?> iterator = sampleCollectionDtlsList.iterator(); iterator.hasNext();) {
							Map<String, Object> map = new LinkedHashMap<String, Object>();
							Object[] row = (Object[]) iterator.next();
							if (row[0] != null) {
								orderhdId = row[0].toString();
							}
							if (row[1] != null) {
								orderdate1 = (Date) row[1];
								orderdate = HMSUtil.convertDateToStringFormat(orderdate1, "dd/MM/yyyy");
							}
							if (row[2] != null) {

								lastChgDate = JavaUtils.getTimeFromDateAndTime(row[2].toString());
							}
							if (row[3] != null) {
								departmentName = row[3].toString();
							}

							if (row[4] != null) {
								patientId = Long.parseLong(row[4].toString());
							}
							if (row[5] != null) {
								patientName = row[5].toString();
							}

							if (row[6] != null) {
								
								
								//pt.put("ageFull", ageFull);
								dateOfBirth = (Date) row[6]; 
								
								String ageFull = HMSUtil.calculateAge(dateOfBirth);					
								
								
								if (row[6] != null) {

									if (dateOfBirth != null) {
										
										map.put("age", ageFull);
									} else {
										map.put("age", "");
									}
								}

							} else {
								map.put("age", "");
							}
							if (row[7] != null) {
								gender = row[7].toString();
							}
							if (row[8] != null) {
								mobileNumber = row[8].toString();
							}

							if (row[9] != null) {
								departmentId = row[9].toString();
							}
							if (row[10] != null) {
								orderStatus = row[10].toString();
							}
							if (row[11] != null) {
								orderNo = row[11].toString();
							}

							if (row[12] != null) {
								lastChgBy = row[12].toString();
							}

							/* SAMPLE DETAILS */
							LocalDate currentDate1 = LocalDate.now();
							DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
							String currentDate = formatter.format(currentDate1);
							String currTime = LocalTime.now().toString();
							String[] currTime1 = currTime.split("\\.");
							currentTime = currTime1[0];

							map.put("orderhdId", orderhdId);
							map.put("orderdate", orderdate);
							map.put("lastChgDate", lastChgDate);
							map.put("departmentName", departmentName);
							map.put("departmentId", departmentId);
							map.put("serviceNo", serviceNo);
							map.put("patientId", patientId);
							map.put("patientName", patientName);
							map.put("relationId", relationId);
							map.put("relationName", relationName);
							map.put("employeeName", employeeName);
							
							map.put("gender", gender);
							map.put("mobileNumber", mobileNumber);
							map.put("labMark", labMark);
							map.put("currentDate", currentDate);
							map.put("currentTime", currentTime);
							map.put("createdBy", createdBy);
							map.put("orderStatus", orderStatus);
							map.put("orderNo", orderNo);
							map.put("rankName", rankName);
							map.put("lastChgBy", lastChgBy);
							list.add(map);

						}

						if (investigationList != null && investigationList.size() > 0) {
							for (Iterator<?> iterator = investigationList.iterator(); iterator.hasNext();) {
								Map<String, Object> invMap = new HashMap<String, Object>();

								Object[] row = (Object[]) iterator.next();
								if (row[0] != null) {
									orderdtId = row[0].toString();
								}
								if (row[1] != null) {
									orderhdIdforInv = row[1].toString();
								}
								if (row[2] != null) {
									investigationId = row[2].toString();
								}
								if (row[3] != null) {
									investigationName = row[3].toString();
								}
								if (row[4] != null) {
									sampleId = Long.parseLong(row[4].toString());
								}

								if (row[5] != null) {
									sampleCode = row[5].toString();
								}
								if (row[6] != null) {
									sampleDesc = row[6].toString();
								}
								if (row[7] != null) {
									subChargeCodeName = row[7].toString();
								}
								if (row[8] != null) {
									labMarkForInv = row[8].toString();
								}
								if (row[9] != null) {
									subChargeCodeId = row[9].toString();
								}

								if (row[10] != null) {
									collectionId = Long.parseLong(row[10].toString());
								}

								if (row[11] != null) {
									collectionName = row[11].toString();
								}

								invMap.put("orderdtId", orderdtId);
								invMap.put("orderhdIdforInv", orderhdIdforInv);
								invMap.put("investigationId", investigationId);
								invMap.put("investigationName", investigationName);
								invMap.put("sampleId", sampleId);
								invMap.put("sampleCode", sampleCode);
								invMap.put("sampleDesc", sampleDesc);
								invMap.put("subChargeCodeName", subChargeCodeName);
								invMap.put("labMarkForInv", labMarkForInv);
								invMap.put("subChargeCodeId", subChargeCodeId);
								invMap.put("collectionId", collectionId);
								invMap.put("collectionName", collectionName);

								investigationlists.add(invMap);

							}

							// List<MasSubChargecode> subChargecodesList =
							// labDao.getAllSubChargeCode(investigationList);
							// not in use
							//List<DgOrderdt> orderdtsList = labDao.getAllInvestigationFromDgOrderDt(orderhdId);

							if (list != null && list.size() > 0
									|| investigationlists != null && investigationlists.size() > 0) {
								json.put("data1", list);
								json.put("data2", investigationlists);
								json.put("count1", list.size());
								json.put("count2", investigationlists.size());
								json.put("status", 1);
								json.put("msg", "Record get successfull");

							}

						} else {
							json.put("stauts", 0);
							json.put("invcount", investigationlists.size());
							json.put("msg", "No Record Found");
						}

					} else {
						json.put("stauts", 0);
						json.put("msg", "No Record Found");
					}

				} else {
					json.put("msg", "Key Does Not Contains");
				}

			}

		} catch (Exception e) {
			e.printStackTrace(System.out);
		}

		return json.toString();
	}

	/**
	 * @Description: Method: submitSampleCollectionDetails() submit the sample
	 *               collection Details.
	 * @param: JSONObject jsonObject
	 * @param: HttpServletRequest request, HttpServletResponse response
	 * @return: json.toString()
	 */
	@Override
	@Transactional
	public String submitSampleCollectionDetails(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {

		JSONObject json = new JSONObject();
		Map<String,Object>datamap= new HashMap<String,Object>();
		DgSampleCollectionHd sampleCollectionHd = null;
		Long orderhdId = null;
		
		if (jsonObject != null) {

			String orderhdIds = JavaUtils.getReplaceString1(jsonObject.get("orderhdId").toString());
			orderhdIds = orderhdIds.replaceAll("\"", "");
			orderhdId = Long.parseLong(orderhdIds);
			
			//System.out.println("orderhdId="+orderhdId);
			
		}
		
		 sampleCollectionHd = labDao.getSampleCollectionHdId(orderhdId);
		
		Long visitId = labDao.getVisitIdFromDgOrderHd(orderhdId);
				
				
		datamap =labDao.submitSampleCollectionDetailsAll(jsonObject,sampleCollectionHd,visitId);
			

			if (datamap != null) 
			{
				json.put("status", 1);
				json.put("msg", "Record Added Successfully");
			} else {
				json.put("status", 0);
				json.put("msg", "Record Not Added");
			}
		

		return json.toString();
	}

	/**
	 * @Description: Method:submitSampleCollectionHeader1() submit Sample Collection
	 *               Header Object wise.
	 * @param sampleCollectionHd Object.
	 * @return
	 */
	private Long submitSampleCollectionHeader1(DgSampleCollectionHd sampleCollectionHd) {

		Long sampCollHdId = null;
		Session session = null;
		Transaction transaction = null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			transaction = session.beginTransaction();
			// sampCollHdId = (Long)session.save(sampleCollectionHd);

			session.saveOrUpdate(sampleCollectionHd);
			sampCollHdId = sampleCollectionHd.getSampleCollectionHdId();
			// transaction.commit();
			session.flush();
			session.clear();

		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return sampCollHdId;
	}

	/******************************************************
	 * pending Sample Validate List
	 *****************************************************/

	/**
	 * @Description: pendingSampleValidateList() pending Sample Validation List
	 * @param: JSONObject jsonObject
	 * @param: HttpServletRequest request,HttpServletResponse response
	 */
	// onload
	@Override
	public String getPendingSampleValidateListGrid(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {

		JSONObject json = new JSONObject();
		List<Object> list = new ArrayList<Object>();

		/*
		 * List<DgSampleCollectionDt> dgSampleCollectionDtsList =
		 * labDao.getPendingSampleValidateListGrid();
		 */
		Map<String, Object> responseData = labDao.getPendingSampleValidateListGrid(jsonObject);
		List<DgSampleCollectionDt> dgSampleCollectionDtsList = (List<DgSampleCollectionDt>) responseData.get("list");
		int count = (int) responseData.get("count");

		if (dgSampleCollectionDtsList != null && dgSampleCollectionDtsList.size() > 0) {
			for (Iterator<?> iterator = dgSampleCollectionDtsList.iterator(); iterator.hasNext();) {
				Map<String, Object> map = new HashMap<String, Object>();
				DgSampleCollectionDt dgSampleCollectionDt = new DgSampleCollectionDt();
				dgSampleCollectionDt = (DgSampleCollectionDt) iterator.next();

				long subchargeCodeId = dgSampleCollectionDt.getSubcharge();
				long sampleCollectionHeaderId = dgSampleCollectionDt.getSampleCollectionHdId();
				//DgSampleCollectionDt dt = labDao.getDgSampleCollectionDt(subchargeCodeId, sampleCollectionHeaderId);
				DgSampleCollectionDt dt = labDao.getDgSampleCollectionDt(sampleCollectionHeaderId);
				map.put("subchargeCodeId",
						dt.getMasSubChargecode().getSubChargecodeId() != null
								? dt.getMasSubChargecode().getSubChargecodeId()
								: "");
				map.put("subchargeCodeName",
						dt.getMasSubChargecode() != null && dt.getMasSubChargecode().getSubChargecodeName() != null
								? dt.getMasSubChargecode().getSubChargecodeName()
								: "");

				map.put("sampleCollectionHeaderId",
						dt.getSampleCollectionHdId() != null ? dt.getSampleCollectionHdId() : "");

				// map.put("sampleCollectionDtId",
				// dgSampleCollectionDt.getSampleCollectionDtId()!=0 ?
				// dgSampleCollectionDt.getSampleCollectionDtId():"");

				map.put("sampleDateTime",
						dt.getDgSampleCollectionHd().getCollectionDate() != null ? HMSUtil.convertDateToStringFormat(
								dt.getDgSampleCollectionHd().getCollectionDate(), "dd/MM/yyyy") : "");

				map.put("orderNumber",
						dt.getDgSampleCollectionHd() != null && dt.getDgSampleCollectionHd().getDgOrderHd() != null
								&& dt.getDgSampleCollectionHd().getDgOrderHd().getOrderNo() != null
										? dt.getDgSampleCollectionHd().getDgOrderHd().getOrderNo()
										: "");

				// map.put("serviceNo", dt.getDgSampleCollectionHd().getDgOrderHd()!=null &&
				// dt.getDgSampleCollectionHd().getDgOrderHd().getVisit()!=null &&
				// dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getPatient()!=null &&
				// dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getPatient().getServiceNo()!=null
				// ?
				// dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getPatient().getServiceNo():"");

				// map.put("employeeName", dt.getDgSampleCollectionHd().getDgOrderHd()!=null &&
				// dt.getDgSampleCollectionHd().getDgOrderHd().getVisit()!=null &&
				// dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getPatient()!=null &&
				// dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getPatient().getEmployeeName()!=null
				// ?
				// dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getPatient().getEmployeeName():"");

				map.put("patientName", dt.getDgSampleCollectionHd().getDgOrderHd() != null
						&& dt.getDgSampleCollectionHd().getDgOrderHd().getVisit() != null
						&& dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getPatient() != null
						&& dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getPatient().getPatientName() != null
								? dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getPatient().getPatientName()
								: "");

				// map.put("relation", dt.getDgSampleCollectionHd().getDgOrderHd()!=null &&
				// dt.getDgSampleCollectionHd().getDgOrderHd().getVisit()!=null &&
				// dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getPatient()!=null &&
				// dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getPatient().getMasRelation()!=null
				// &&
				// dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getPatient().getMasRelation().getRelationId()!=null
				// ?
				// dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getPatient().getMasRelation().getRelationName():"");

				// map.put("age", dt.getDgSampleCollectionHd().getDgOrderHd()!=null &&
				// dt.getDgSampleCollectionHd().getDgOrderHd().getVisit()!=null &&
				// dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getPatient()!=null &&
				// dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getPatient().getDateOfBirth()!=null?
				// JavaUtils.calculateAgefromDob(dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getPatient().getDateOfBirth()):""
				// );
				if (dt.getDgSampleCollectionHd().getDgOrderHd() != null
						&& dt.getDgSampleCollectionHd().getDgOrderHd().getVisit() != null
						&& dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getPatient() != null
						&& dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getPatient()
								.getDateOfBirth() != null) {
					Date date = dt.getDgSampleCollectionHd().getDgOrderHd().getPatient().getDateOfBirth();
					Date visitDate = null;
					if (dt.getDgSampleCollectionHd().getDgOrderHd().getVisit() != null
							&& dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getVisitDate() != null) {
						visitDate = dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getVisitDate();
					}
					if (visitDate != null && date != null) {
						map.put("age", HMSUtil.getDateBetweenTwoDate(visitDate, date));
					} else {
						map.put("age", "");
					}
				} else {
					map.put("age", "");
				}
				map.put("gender",
						dt.getDgSampleCollectionHd().getDgOrderHd() != null
								&& dt.getDgSampleCollectionHd().getDgOrderHd().getVisit() != null
								&& dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getPatient() != null
								&& dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getPatient()
										.getMasAdministrativeSex().getAdministrativeSexName() != null
												? dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getPatient()
														.getMasAdministrativeSex().getAdministrativeSexName()
												: "");

				// map.put("departmentName", dt.getDgSampleCollectionHd().getDgOrderHd()!=null
				// && dt.getDgSampleCollectionHd().getDgOrderHd().getVisit()!=null &&
				// dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getMasDepartment()!=null
				// &&
				// dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getMasDepartment().getDepartmentName()!=null
				// ?
				// dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getMasDepartment().getDepartmentName():"");
				map.put("departmentName", dt.getDgSampleCollectionHd().getDgOrderHd() != null
						&& dt.getDgSampleCollectionHd().getDgOrderHd() != null
						&& dt.getDgSampleCollectionHd().getDgOrderHd().getMasDepartment() != null
						&& dt.getDgSampleCollectionHd().getDgOrderHd().getMasDepartment().getDepartmentName() != null
								? dt.getDgSampleCollectionHd().getDgOrderHd().getMasDepartment().getDepartmentName()
								: "");
				map.put("departmentId",
						dt.getDgSampleCollectionHd() != null && dt.getDgSampleCollectionHd().getDepartmentId() != null
								? dt.getDgSampleCollectionHd().getDepartmentId()
								: "");

				map.put("visitId",
						dt.getDgSampleCollectionHd().getVisitId() != null ? dt.getDgSampleCollectionHd().getVisitId()
								: "");

				map.put("investigationType",
						dt.getDgOrderdt() != null && dt.getDgOrderdt().getDgMasInvestigations() != null
								&& dt.getDgOrderdt().getDgMasInvestigations().getInvestigationType() != null
										? dt.getDgOrderdt().getDgMasInvestigations().getInvestigationType()
										: "");

				map.put("investigationId",
						dt.getDgOrderdt() != null && dt.getDgOrderdt().getDgMasInvestigations() != null
								&& dt.getDgOrderdt().getDgMasInvestigations().getInvestigationId() != null
										? dt.getDgOrderdt().getDgMasInvestigations().getInvestigationId()
										: "");

				map.put("investigationName",
						dt.getDgOrderdt() != null && dt.getDgOrderdt().getDgMasInvestigations() != null
								&& dt.getDgOrderdt().getDgMasInvestigations().getInvestigationName() != null
										? dt.getDgOrderdt().getDgMasInvestigations().getInvestigationName()
										: "");

				list.add(map);
			}

			if (list != null && list.size() > 0) {
				json.put("data", list);
				json.put("msg", "Record Fetch Successfully");
				json.put("status", 1);
				json.put("count", count);
			} else {
				json.put("data", list);
				json.put("msg", "No Record Found");
				json.put("status", 0);
				json.put("count", 0);
			}
		} else {
			json.put("data", dgSampleCollectionDtsList);
			json.put("msg", "No Record Found");
			json.put("status", 0);
			json.put("count", 0);
		}
		return json.toString();
	}

	@Override
	public String getPendingSampleValidateList(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsonObject != null) {

			List<Object> list = new ArrayList<Object>();

			List<DgSampleCollectionDt> dgSampleCollectionDtsList = labDao.getPendingSampleValidateList(jsonObject);

			if (dgSampleCollectionDtsList != null && dgSampleCollectionDtsList.size() > 0) {
				for (Iterator<?> iterator = dgSampleCollectionDtsList.iterator(); iterator.hasNext();) {
					Map<String, Object> map = new HashMap<String, Object>();
					DgSampleCollectionDt dgSampleCollectionDt = new DgSampleCollectionDt();
					dgSampleCollectionDt = (DgSampleCollectionDt) iterator.next();

					map.put("sampleCollectionHeaderId",
							dgSampleCollectionDt.getSampleCollectionHdId() != null
									? dgSampleCollectionDt.getSampleCollectionHdId()
									: "");

					if (dgSampleCollectionDt.getSampleCollectionDtId() != 0) {
						map.put("sampleCollectionDtId",
								dgSampleCollectionDt.getSampleCollectionDtId() != 0
										? dgSampleCollectionDt.getSampleCollectionDtId()
										: "");
					} else {
						map.put("sampleCollectionDtId", "");
					}

					if (dgSampleCollectionDt.getDgSampleCollectionHd().getCollectionDate() != null) {
						String sampleDateTime = HMSUtil.convertDateToStringFormat(
								dgSampleCollectionDt.getDgSampleCollectionHd().getCollectionDate(), "dd/MM/yyyy");
						map.put("sampleDateTime", sampleDateTime);
					} else {
						map.put("sampleDateTime", "");
					}

					if (dgSampleCollectionDt.getDiagNo() != null) {
						map.put("orderNumber", dgSampleCollectionDt.getDiagNo());
					} else {
						map.put("orderNumber", "");
					}

					/*
					 * if(dgSampleCollectionDt.getDgSampleCollectionHd().getDgOrderHd()!=null &&
					 * dgSampleCollectionDt.getDgSampleCollectionHd().getDgOrderHd().getVisit()!=
					 * null &&
					 * dgSampleCollectionDt.getDgSampleCollectionHd().getDgOrderHd().getVisit().
					 * getPatient()!=null &&
					 * dgSampleCollectionDt.getDgSampleCollectionHd().getDgOrderHd().getVisit().
					 * getPatient().getServiceNo()!=null) { map.put("serviceNo",
					 * dgSampleCollectionDt.getDgSampleCollectionHd().getDgOrderHd().getVisit().
					 * getPatient().getServiceNo()); }else { map.put("serviceNo", ""); }
					 */

					/*
					 * if(dgSampleCollectionDt.getDgSampleCollectionHd().getDgOrderHd()!=null &&
					 * dgSampleCollectionDt.getDgSampleCollectionHd().getDgOrderHd().getVisit()!=
					 * null &&
					 * dgSampleCollectionDt.getDgSampleCollectionHd().getDgOrderHd().getVisit().
					 * getPatient()!=null &&
					 * dgSampleCollectionDt.getDgSampleCollectionHd().getDgOrderHd().getVisit().
					 * getPatient().getEmployeeName()!=null) { map.put("employeeName",
					 * dgSampleCollectionDt.getDgSampleCollectionHd().getDgOrderHd().getVisit().
					 * getPatient().getEmployeeName()); }else { map.put("employeeName", ""); }
					 */

					if (dgSampleCollectionDt.getDgSampleCollectionHd().getDgOrderHd() != null
							&& dgSampleCollectionDt.getDgSampleCollectionHd().getDgOrderHd().getVisit() != null
							&& dgSampleCollectionDt.getDgSampleCollectionHd().getDgOrderHd().getVisit()
									.getPatient() != null
							&& dgSampleCollectionDt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getPatient()
									.getPatientName() != null) {
						map.put("patientName", dgSampleCollectionDt.getDgSampleCollectionHd().getDgOrderHd().getVisit()
								.getPatient().getPatientName());
					} else {
						map.put("patientName", "");
					}

					/*
					 * if(dgSampleCollectionDt.getDgSampleCollectionHd().getDgOrderHd()!=null &&
					 * dgSampleCollectionDt.getDgSampleCollectionHd().getDgOrderHd().getVisit()!=
					 * null &&
					 * dgSampleCollectionDt.getDgSampleCollectionHd().getDgOrderHd().getVisit().
					 * getPatient()!=null &&
					 * dgSampleCollectionDt.getDgSampleCollectionHd().getDgOrderHd().getVisit().
					 * getPatient().getMasRelation()!=null &&
					 * dgSampleCollectionDt.getDgSampleCollectionHd().getDgOrderHd().getVisit().
					 * getPatient().getMasRelation().getRelationId()!=null) { map.put("relation",
					 * dgSampleCollectionDt.getDgSampleCollectionHd().getDgOrderHd().getVisit().
					 * getPatient().getMasRelation().getRelationName()); }else { map.put("relation",
					 * ""); }
					 */
					if (dgSampleCollectionDt.getDgSampleCollectionHd().getDgOrderHd() != null
							&& dgSampleCollectionDt.getDgSampleCollectionHd().getDgOrderHd().getVisit() != null
							&& dgSampleCollectionDt.getDgSampleCollectionHd().getDgOrderHd().getVisit()
									.getPatient() != null
							&& dgSampleCollectionDt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getPatient()
									.getDateOfBirth() != null) {
						Date dateOfBirth = dgSampleCollectionDt.getDgSampleCollectionHd().getDgOrderHd().getVisit()
								.getPatient().getDateOfBirth();
						// String age = JavaUtils.calculateAgefromDob(dateOfBirth);
						// map.put("age", age);
						Date visitDate = null;
						if (dgSampleCollectionDt.getDgSampleCollectionHd().getDgOrderHd().getVisit() != null
								&& dgSampleCollectionDt.getDgSampleCollectionHd().getDgOrderHd().getVisit()
										.getVisitDate() != null) {
							visitDate = dgSampleCollectionDt.getDgSampleCollectionHd().getDgOrderHd().getVisit()
									.getVisitDate();
						}
						if (visitDate != null && dateOfBirth != null) {
							map.put("age", HMSUtil.getDateBetweenTwoDate(visitDate, dateOfBirth));
						} else {
							map.put("age", "");
						}

					} else {
						map.put("age", "");
					}
					if (dgSampleCollectionDt.getDgSampleCollectionHd().getDgOrderHd() != null
							&& dgSampleCollectionDt.getDgSampleCollectionHd().getDgOrderHd().getVisit() != null
							&& dgSampleCollectionDt.getDgSampleCollectionHd().getDgOrderHd().getVisit()
									.getPatient() != null
							&& dgSampleCollectionDt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getPatient()
									.getMasAdministrativeSex().getAdministrativeSexName() != null) {
						map.put("gender", dgSampleCollectionDt.getDgSampleCollectionHd().getDgOrderHd().getVisit()
								.getPatient().getMasAdministrativeSex().getAdministrativeSexName());
					} else {
						map.put("gender", "");
					}

					/*
					 * if(dgSampleCollectionDt.getDgSampleCollectionHd().getDgOrderHd()!=null &&
					 * dgSampleCollectionDt.getDgSampleCollectionHd().getDgOrderHd().getVisit()!=
					 * null &&
					 * dgSampleCollectionDt.getDgSampleCollectionHd().getDgOrderHd().getVisit().
					 * getMasDepartment()!=null &&
					 * dgSampleCollectionDt.getDgSampleCollectionHd().getDgOrderHd().getVisit().
					 * getMasDepartment().getDepartmentName()!=null) { map.put("departmentName",
					 * dgSampleCollectionDt.getDgSampleCollectionHd().getDgOrderHd().getVisit().
					 * getMasDepartment().getDepartmentName()); }else { map.put("departmentName",
					 * ""); }
					 */
					if (dgSampleCollectionDt.getDgSampleCollectionHd().getDgOrderHd() != null
							&& dgSampleCollectionDt.getDgSampleCollectionHd().getDgOrderHd().getMasDepartment() != null
							&& dgSampleCollectionDt.getDgSampleCollectionHd().getDgOrderHd().getMasDepartment()
									.getDepartmentName() != null) {
						map.put("departmentName", dgSampleCollectionDt.getDgSampleCollectionHd().getDgOrderHd()
								.getMasDepartment().getDepartmentName());
					} else {
						map.put("departmentName", "");
					}

					if (dgSampleCollectionDt.getDgSampleCollectionHd() != null
							&& dgSampleCollectionDt.getDgSampleCollectionHd().getDepartmentId() != null) {
						map.put("departmentId", dgSampleCollectionDt.getDgSampleCollectionHd().getDepartmentId());
					} else {
						map.put("departmentId", null);
					}
					if (dgSampleCollectionDt.getSubcharge() != null) {
						map.put("subchargeCodeId", dgSampleCollectionDt.getSubcharge());
					} else {
						map.put("subchargeCodeId", "");
					}
					if (dgSampleCollectionDt.getMasSubChargecode().getSubChargecodeName() != null) {
						map.put("subchargeCodeName", dgSampleCollectionDt.getMasSubChargecode().getSubChargecodeName());
					} else {
						map.put("subchargeCodeName", "");
					}

					if (dgSampleCollectionDt.getDgSampleCollectionHd().getVisitId() != null) {
						map.put("visitId", dgSampleCollectionDt.getDgSampleCollectionHd().getVisitId());
					} else {
						map.put("visitId", "");
					}

					if (dgSampleCollectionDt.getDgOrderdt() != null
							&& dgSampleCollectionDt.getDgOrderdt().getDgMasInvestigations() != null
							&& dgSampleCollectionDt.getDgOrderdt().getDgMasInvestigations()
									.getInvestigationType() != null) {
						map.put("investigationType",
								dgSampleCollectionDt.getDgOrderdt().getDgMasInvestigations().getInvestigationType());
					} else {
						map.put("investigationType", "");
					}

					if (dgSampleCollectionDt.getDgOrderdt() != null
							&& dgSampleCollectionDt.getDgOrderdt().getDgMasInvestigations() != null
							&& dgSampleCollectionDt.getDgOrderdt().getDgMasInvestigations()
									.getInvestigationId() != null) {
						map.put("investigationId",
								dgSampleCollectionDt.getDgOrderdt().getDgMasInvestigations().getInvestigationId());
					} else {
						map.put("investigationId", "");
					}

					if (dgSampleCollectionDt.getDgOrderdt() != null
							&& dgSampleCollectionDt.getDgOrderdt().getDgMasInvestigations() != null
							&& dgSampleCollectionDt.getDgOrderdt().getDgMasInvestigations()
									.getInvestigationName() != null) {
						map.put("investigationName",
								dgSampleCollectionDt.getDgOrderdt().getDgMasInvestigations().getInvestigationName());
					} else {
						map.put("investigationName", "");
					}

					list.add(map);
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("msg", "Record Fetch Successfully");
					json.put("status", 1);
					json.put("count", list.size());
				} else {
					json.put("data", list);
					json.put("msg", "No Record Found");
					json.put("status", 0);
					json.put("count", list.size());
				}
			} else {
				json.put("data", dgSampleCollectionDtsList);
				json.put("msg", "No Record Found");
				json.put("status", 0);
				json.put("count", dgSampleCollectionDtsList.size());
			}

		} else {
			json.put("msg", "Invalid Input Parameters");
			json.put("status", 2);
		}
		return json.toString();
	}

	/**********************************************************
	 * Pending Sample Validate Details
	 *****************************************************/

	/**
	 * @Description: Method: getPendingSampleValidateDetails(),get Details of
	 *               Pending Sample Validation
	 * @param: JSONObject jsonObject
	 * @param: HttpServletRequest request, HttpServletResponse response
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public String getPendingSampleValidateDetails(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<DgSampleCollectionHd> dgSampleCollectionHds = null;
		List<DgSampleCollectionDt> dgSampleCollectionDts = null;
		Map<String, Object> map = new HashMap<String, Object>();

		List list = new ArrayList();
		List list1 = new ArrayList();
		// List list2 = new ArrayList();

		if (jsonObject != null) {
			Map<String, Object> mapobject = labDao.getPendingSampleValidateDetails(jsonObject);
			List<DgSampleCollectionDt> dgSampleCollectionDtsValidateSampleList = labDao
					.getPendingSampleValidateDetailsForSampleValidate(jsonObject);

			if (mapobject.get("dgSampleCollectionDtsList") != null) {
				dgSampleCollectionDts = (List<DgSampleCollectionDt>) mapobject.get("dgSampleCollectionDtsList");
				for (Iterator<?> iterator = dgSampleCollectionDts.iterator(); iterator.hasNext();) {
					DgSampleCollectionDt dt = new DgSampleCollectionDt();
					dt = (DgSampleCollectionDt) iterator.next();

					if (dt.getSampleCollectionHdId() != null) {
						map.put("sampleCollectionHdId", dt.getSampleCollectionHdId());
					} else {
						map.put("sampleCollectionHdId", "");
					}
					if (dt.getDgSampleCollectionHd().getCollectionDate() != null) {
						String sampleDateTime = HMSUtil.convertDateToStringFormat(
								dt.getDgSampleCollectionHd().getCollectionDate(), "dd/MM/yyyy");
						map.put("sampleDateTime", sampleDateTime);
					} else {
						map.put("sampleDateTime", "");
					}

					if (dt.getDgSampleCollectionHd().getCollectionDate() != null
							&& dt.getDgSampleCollectionHd().getDgOrderHd() != null
							&& dt.getDgSampleCollectionHd().getDgOrderHd().getLastChgDate() != null) {
						// map.put("orderTime",
						// JavaUtils.getTimeFromDateAndTime(dt.getDgSampleCollectionHd().getCollectionDate().toString()));
						// map.put("orderTime",
						// HMSUtil.convertStringTypeDateToDateType(dt.getDgSampleCollectionHd().getCollectionDate().toString()));
						map.put("orderTime", JavaUtils.getTimeFromDateAndTime(
								dt.getDgSampleCollectionHd().getDgOrderHd().getLastChgDate().toString()));

					} else {
						map.put("orderTime", "");
					}

					if (dt.getDgSampleCollectionHd() != null && dt.getDgSampleCollectionHd().getDgOrderHd() != null
							&& dt.getDgSampleCollectionHd().getDgOrderHd().getOrderNo() != null) {
						map.put("orderNumber", dt.getDgSampleCollectionHd().getDgOrderHd().getOrderNo());
					} else {
						map.put("orderNumber", "");
					}
					/*
					 * if (dt.getDgSampleCollectionHd().getDgOrderHd() != null &&
					 * dt.getDgSampleCollectionHd().getDgOrderHd().getVisit() != null &&
					 * dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getPatient() != null
					 * && dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getPatient()
					 * .getServiceNo() != null) { map.put("serviceNo",
					 * dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getPatient().
					 * getServiceNo()); } else { map.put("serviceNo", ""); } if
					 * (dt.getDgSampleCollectionHd().getDgOrderHd() != null &&
					 * dt.getDgSampleCollectionHd().getDgOrderHd().getVisit() != null &&
					 * dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getPatient() != null
					 * && dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getPatient()
					 * .getEmployeeName() != null) { map.put("employeeName",
					 * dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getPatient().
					 * getEmployeeName()); } else { map.put("employeeName", ""); }
					 */
					if (dt.getDgSampleCollectionHd().getDgOrderHd() != null
							&& dt.getDgSampleCollectionHd().getDgOrderHd().getVisit() != null
							&& dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getPatient() != null
							&& dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getPatient()
									.getPatientName() != null) {
						map.put("patientName",
								dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getPatient().getPatientName());
					} else {
						map.put("patientName", "");
					}

					/*
					 * if (dt.getDgSampleCollectionHd().getDgOrderHd() != null &&
					 * dt.getDgSampleCollectionHd().getDgOrderHd().getVisit() != null &&
					 * dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getPatient() != null
					 * && dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getPatient()
					 * .getMasRelation() != null &&
					 * dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getPatient().
					 * getMasRelation() .getRelationId() != null) { map.put("relationName",
					 * dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getPatient()
					 * .getMasRelation().getRelationName()); } else { map.put("relationName", ""); }
					 */

					if (dt.getDgSampleCollectionHd().getDgOrderHd() != null
							&& dt.getDgSampleCollectionHd().getDgOrderHd().getVisit() != null
							&& dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getPatient() != null
							&& dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getPatient()
									.getDateOfBirth() != null) {
						Date dateOfBirth = dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getPatient()
								.getDateOfBirth();
						// String age = JavaUtils.calculateAgefromDob(dateOfBirth);
						// map.put("age", age);
						Date visitDate = null;
						if (dt.getDgSampleCollectionHd().getDgOrderHd().getVisit() != null
								&& dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getVisitDate() != null) {
							visitDate = dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getVisitDate();
						}
						if (visitDate != null && dateOfBirth != null) {
							map.put("age", HMSUtil.getDateBetweenTwoDate(visitDate, dateOfBirth));
						} else {
							map.put("age", "");
						}

					} else {
						map.put("age", "");
					}
					if (dt.getDgSampleCollectionHd().getDgOrderHd() != null
							&& dt.getDgSampleCollectionHd().getDgOrderHd().getVisit() != null
							&& dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getPatient() != null
							&& dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getPatient()
									.getMasAdministrativeSex() != null
							&& dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getPatient()
									.getMasAdministrativeSex().getAdministrativeSexName() != null) {
						map.put("gender", dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getPatient()
								.getMasAdministrativeSex().getAdministrativeSexName());
					} else {
						map.put("gender", "");
					}

					if (dt.getDgSampleCollectionHd().getDgOrderHd() != null
							&& dt.getDgSampleCollectionHd().getDgOrderHd().getVisit() != null
							&& dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getMasDepartment() != null
							&& dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getMasDepartment()
									.getDepartmentName() != null) {
						map.put("departmentName", dt.getDgSampleCollectionHd().getDgOrderHd().getVisit()
								.getMasDepartment().getDepartmentName());
					} else {
						map.put("departmentName", "");
					}

					if (dt.getDgSampleCollectionHd().getPatient() != null
							&& dt.getDgSampleCollectionHd().getPatient().getPatientId() != null) {
						map.put("patientId", dt.getDgSampleCollectionHd().getPatient().getPatientId());
					} else {
						map.put("patientId", null);
					}

					if (dt.getDgSampleCollectionHd().getDgOrderHd() != null
							&& dt.getDgSampleCollectionHd().getDgOrderHd().getVisit() != null
							&& dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getMasDepartment() != null
							&& dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getMasDepartment()
									.getDepartmentId() != 0) {
						map.put("departmentId", dt.getDgSampleCollectionHd().getDgOrderHd().getVisit()
								.getMasDepartment().getDepartmentId());
					} else {
						map.put("departmentId", null);
					}
					if (dt.getDgSampleCollectionHd().getDgOrderHd().getOrderDate() != null) {
						map.put("orderDate", HMSUtil.convertDateToStringFormat(
								dt.getDgSampleCollectionHd().getDgOrderHd().getOrderDate(), "dd/MM/yyyy"));
					} else {
						map.put("orderDate", "");
					}

					if (dt.getDgSampleCollectionHd().getVisitId() != 0) {
						map.put("visitId", dt.getDgSampleCollectionHd().getVisitId());
					} else {
						map.put("visitId", null);
					}

					if (dt.getSubcharge() != null) {
						map.put("subchargeCodeId", dt.getSubcharge());
					} else {
						map.put("subchargeCodeId", null);
					}

					if (dt.getMasSubChargecode().getSubChargecodeName() != null) {
						map.put("subchargeCodeName", dt.getMasSubChargecode().getSubChargecodeName());
					} else {
						map.put("subchargeCodeName", null);
					}

					/*
					 * if (dt.getDgSampleCollectionHd() != null &&
					 * dt.getDgSampleCollectionHd().getPatient() != null &&
					 * dt.getDgSampleCollectionHd().getPatient().getMasRank() != null &&
					 * dt.getDgSampleCollectionHd().getPatient().getMasRank().getRankName() != null)
					 * { map.put("rankName",
					 * dt.getDgSampleCollectionHd().getPatient().getMasRank().getRankName()); } else
					 * { map.put("rankName", null); } if (dt.getDgSampleCollectionHd() != null &&
					 * dt.getDgSampleCollectionHd().getPatient() != null &&
					 * dt.getDgSampleCollectionHd().getPatient().getUhidNo() != null) {
					 * map.put("regNo", dt.getDgSampleCollectionHd().getPatient().getUhidNo()); }
					 * else { map.put("regNo", ""); }
					 */
					/*
					 * if (dt.getDgSampleCollectionHd() != null &&
					 * dt.getDgSampleCollectionHd().getDgOrderHd() != null &&
					 * dt.getDgSampleCollectionHd().getDgOrderHd().getUsers().getFirstName() !=
					 * null) { map.put("validatedBy",
					 * dt.getDgSampleCollectionHd().getDgOrderHd().getUsers().getFirstName()); }
					 * else { map.put("validatedBy", ""); }
					 */

					/* SAMPLE DETAILS */
					LocalDate currentDate1 = LocalDate.now();
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
					String currentDate = formatter.format(currentDate1);
					String currTime = LocalTime.now().toString();
					String[] currTime1 = currTime.split("\\.");
					String currentTime = currTime1[0];

					map.put("currentDate", currentDate);
					map.put("currentTime", currentTime);

				}

				list.add(map);
			}

			/******************************************
			 * Investigation List of Pending Sample Validation
			 **************************************/

			/**
			 * @Description : Investigation List of Pending Sample Validation Details
			 * @param:jsonObject
			 * @return
			 */
			if (dgSampleCollectionDtsValidateSampleList != null && dgSampleCollectionDtsValidateSampleList.size() > 0) {

				for (DgSampleCollectionDt dt : dgSampleCollectionDtsValidateSampleList) {
					Map<String, Object> map1 = new HashMap<String, Object>();

					if (dt.getSampleCollectionDtId() != 0) {
						map1.put("sampleCollectionDtId", dt.getSampleCollectionDtId());
					} else {
						map1.put("sampleCollectionDtId", "");
					}

					if (dt.getDgSampleCollectionHd() != null && dt.getDgSampleCollectionHd().getDgOrderHd() != null
							&& dt.getDgSampleCollectionHd().getDgOrderHd().getOrderNo() != null) {
						map1.put("digNo", dt.getDgSampleCollectionHd().getDgOrderHd().getOrderNo());
					} else {
						map1.put("digNo", "");
					}

					if (dt.getInvestigationId() != null) {
						map1.put("investigationId", dt.getInvestigationId());
					} else {
						map1.put("investigationId", "");
					}

					if (dt.getDgMasInvestigation() != null
							&& dt.getDgMasInvestigation().getInvestigationName() != null) {
						map1.put("investigationName", dt.getDgMasInvestigation().getInvestigationName());
					} else {
						map1.put("investigationName", "");
					}

					if (dt.getSampleId() != null) {
						map1.put("sampleId", dt.getSampleId());
					} else {
						map1.put("sampleId", "");
					}

					if (dt.getMasSample() != null && dt.getMasSample().getSampleDescription() != null) {
						map1.put("sampleDesc", dt.getMasSample().getSampleDescription());
					} else {
						map1.put("sampleDesc", "");
					}

					if (dt.getSampleCollDatetime() != null) {
						map1.put("sampleCollDate",
								HMSUtil.convertDateToStringFormat(dt.getSampleCollDatetime(), "dd/MM/yyyy"));
					} else {
						map1.put("sampleCollDate", "");
					}

					if (dt.getSubcharge() != null) {
						map1.put("subchargeCodeId", dt.getSubcharge());
					} else {
						map1.put("subchargeCodeId", null);
					}

					if (dt.getMasSubChargecode() != null && dt.getMasSubChargecode().getSubChargecodeName() != null) {
						map1.put("subchargeCodeName", dt.getMasSubChargecode().getSubChargecodeName());
					} else {
						map1.put("subchargeCodeName", null);
					}

					if (dt.getReason() != null) {
						map1.put("reason", dt.getReason());
					} else {
						map1.put("reason", null);
					}

					list1.add(map1);
				}

			}

			json.put("data", list);
			json.put("data1", list1);
			// json.put("list2", list2);
			json.put("count", list.size());
			json.put("count1", list1.size());
			json.put("msg", "Record get Successfully");

		} else {
			json.put("status", 0);
			json.put("msg", "Invalid Input");
		}

		return json.toString();
	}

	/********************************************
	 * submit Sample Validation Details
	 *******************************************************/

	/**
	 * @Description: Method: submitSampleValidationDetails(), Submit Sample
	 *               Validation Details
	 * @param: JSONObject jsonObject
	 * @param:HttpServletRequest request, HttpServletResponse response
	 * @return
	 */
	@Override
	public String submitSampleValidationDetails(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		String[] reasonArr = null;
		String[] additionalRemarksArr = null;
		if (jsonObject != null) {

			Long subchargeCodeId = null;
			Long sampleCollectionHdId = null;
			String acceptedChecks = "";
			String rejectedChecks = "";
			String[] acceptedChecksArray = null;
			String[] rejectedChecksArray = null;
			String[] investIDArray = null;
			String[] a_subchargeCodeIdArr = null;
			String[] r_subchargeCodeIdArr = null;

			if (jsonObject.has("acceptedCheckBox")) {
				if (jsonObject.get("acceptedCheckBox").toString() != null) {
					acceptedChecks = JavaUtils.getReplaceString1(jsonObject.get("acceptedCheckBox").toString());
					acceptedChecks = acceptedChecks.replaceAll("\"", "");
					acceptedChecksArray = acceptedChecks.split(",");
				}
			}

			if (jsonObject.has("rejectedCheckBox")) {
				if (jsonObject.get("rejectedCheckBox").toString() != null) {
					rejectedChecks = JavaUtils.getReplaceString1(jsonObject.get("rejectedCheckBox").toString());
					rejectedChecks = rejectedChecks.replaceAll("\"", "");
					rejectedChecksArray = rejectedChecks.split(",");
				}
			}

			if (jsonObject.get("sampleCollectionHdId").toString() != null) {
				String sampleCollectionHdIds = JavaUtils
						.getReplaceString1(jsonObject.get("sampleCollectionHdId").toString());
				sampleCollectionHdIds = sampleCollectionHdIds.replaceAll("\"", "");
				sampleCollectionHdId = Long.parseLong(sampleCollectionHdIds);
			}

			if (jsonObject.has("subchargeCodeIdArray")) {
				if (jsonObject.get("subchargeCodeIdArray").toString() != null) {
					String subchargeCodeIds = jsonObject.get("subchargeCodeIdArray").toString();
					subchargeCodeIds = JavaUtils.getReplaceString1(subchargeCodeIds);
					subchargeCodeIds = subchargeCodeIds.replaceAll("\"", "");
					a_subchargeCodeIdArr = subchargeCodeIds.split(",");
					// subchargeCodeId = Long.parseLong(subchargeCodeIdArr[0]);
				}
			}

			if (jsonObject.has("r_subchargeCodeIdArray")) {
				if (jsonObject.get("r_subchargeCodeIdArray").toString() != null) {
					String subchargeCodeIds = jsonObject.get("r_subchargeCodeIdArray").toString();
					subchargeCodeIds = JavaUtils.getReplaceString1(subchargeCodeIds);
					subchargeCodeIds = subchargeCodeIds.replaceAll("\"", "");
					r_subchargeCodeIdArr = subchargeCodeIds.split(",");
					// subchargeCodeId = Long.parseLong(subchargeCodeIdArr[0]);
				}
			}

			if (jsonObject.has("reason")) {
				if (jsonObject.get("reason").toString() != null) {
					String reason = jsonObject.get("reason").toString();
					reason = JavaUtils.getReplaceString1(reason);
					reason = reason.replaceAll("\"", "");
					reasonArr = reason.split(",");
				}
			}

			if (jsonObject.has("additionalRemarks")) {
				if (jsonObject.get("additionalRemarks").toString() != null) {
					String additionalRemarks = jsonObject.get("additionalRemarks").toString();
					additionalRemarks = JavaUtils.getReplaceString1(additionalRemarks);
					additionalRemarks = additionalRemarks.replaceAll("\"", "");
					additionalRemarksArr = additionalRemarks.split(",");

				}
			}

			/*
			 * if(jsonObject.has("investID")) {
			 * if(jsonObject.get("investID").toString()!=null) { String
			 * investIDs=jsonObject.get("investID").toString(); investIDs =
			 * JavaUtils.getReplaceString1(investIDs); investIDs =
			 * investIDs.replaceAll("\"", ""); investIDArray = investIDs.split(",");
			 * 
			 * } }
			 */

			String[] acceptedInvestigations = null;
			if (jsonObject.has("investigationsArray")) {
				if (jsonObject.get("investigationsArray").toString() != null) {
					String investIDs = jsonObject.get("investigationsArray").toString();
					investIDs = JavaUtils.getReplaceString1(investIDs);
					investIDs = investIDs.replaceAll("\"", "");
					acceptedInvestigations = investIDs.split(",");

				}
			}

			String[] rejectedInvestigations = null;
			if (jsonObject.has("r_investigationsArray")) {
				if (jsonObject.get("r_investigationsArray").toString() != null) {
					String rejInvestIDs = jsonObject.get("r_investigationsArray").toString();
					rejInvestIDs = JavaUtils.getReplaceString1(rejInvestIDs);
					rejInvestIDs = rejInvestIDs.replaceAll("\"", "");
					rejectedInvestigations = rejInvestIDs.split(",");

				}
			}

			// investigationsArray
			// r_investigationsArray

			String acceptflag = "";
			if (acceptedChecksArray.length > 0 && !acceptedChecksArray[0].equalsIgnoreCase("")) {
				for (int i = 0; i < acceptedChecksArray.length; i++) {
					if (acceptedChecksArray[i].equalsIgnoreCase("y")) {
						if (sampleCollectionHdId != null) {
							//System.out.println("ainvestIDArray :: " + acceptedInvestigations[i]);
							acceptflag = "A";
							// update
							List<DgSampleCollectionDt> sampleCollectionDtIds = labDao
									.getSampleCollectionDtsIdsubChrgCode(sampleCollectionHdId,
											Long.parseLong(a_subchargeCodeIdArr[i]),
											Long.parseLong(acceptedInvestigations[i]));
							if (!sampleCollectionDtIds.isEmpty()) {
								boolean status = labDao.updateDgSampleCollectionDt(sampleCollectionDtIds, reasonArr,
										additionalRemarksArr, acceptflag);

								// List<DgSampleCollectionDt> dgresultEntrydtIdList =
								// labDao.getDgSampleCollectiondtIdForUpdates(sampleCollectionHdId);

								/*
								 * if(status==true) { Integer count=0; for(int
								 * i=0;i<dgresultEntrydtIdList.size();i++) {
								 * if(dgresultEntrydtIdList.get(i).getOrderStatus().equalsIgnoreCase("P")) {
								 * count+=1; } }
								 * 
								 * if(count==0) { boolean
								 * uDgSampleCollectionHdStatus=labDao.updateStatusDgSampleCollectionHd(
								 * sampleCollectionHdId); //updateOrderStatusDgOrderHd1(orderhdId);
								 * if(uDgSampleCollectionHdStatus==true) { json.put("msg",
								 * "DgOrderHd Status has been updated"); json.put("status", "uDgOrderHdStatus");
								 * } }else { //not update dgorderHd } }
								 */
							}
						}
					}
				}

			}
			if (rejectedChecksArray.length > 0 && !rejectedChecksArray[0].equalsIgnoreCase("")) {
				for (int j = 0; j < rejectedChecksArray.length; j++) {
					if (rejectedChecksArray[j].equalsIgnoreCase("y")) {
						if (sampleCollectionHdId != null) {
							//System.out.println("rinvestIDArray :: " + rejectedInvestigations[j]);
							acceptflag = "R";
							// update
							List<DgSampleCollectionDt> sampleCollectionDtIds = labDao
									.getSampleCollectionDtsIdsubChrgCode(sampleCollectionHdId,
											Long.parseLong(r_subchargeCodeIdArr[j]),
											Long.parseLong(rejectedInvestigations[j]));
							if (!sampleCollectionDtIds.isEmpty()) {
								boolean status = labDao.updateDgSampleCollectionDt(sampleCollectionDtIds, reasonArr,
										additionalRemarksArr, acceptflag);

							}
						}
					}
				}
			}

		} else {
			json.put("msg", "Invalid Input");
			json.put("status", 0);
		}

		return json.toString();
	}

	private Long submitDgResultEntryHeader(DgResultEntryHd dgResultEntryHd) {
		Long resultEntryHdId = null;
		Session session = null;
		Transaction transaction = null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			transaction = session.beginTransaction();
			session.saveOrUpdate(dgResultEntryHd);
			resultEntryHdId = dgResultEntryHd.getResultEntryId();

			session.flush();
			session.clear();

		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return resultEntryHdId;
	}

	/************************************************************
	 * Result Entry
	 *********************************************************/
	// onload waiting list
	@Override
	public String getResultEntryWaitingListGrid(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		List list = new ArrayList();
		JSONObject json = new JSONObject();
		/*
		 * List<DgSampleCollectionDt> dtList =
		 * labDao.getResultEntryWaitingListGrid(jsonObject);
		 */

		Map<String, Object> responseData = labDao.getResultEntryWaitingListGrid(jsonObject);
		List<DgSampleCollectionDt> dtList = (List<DgSampleCollectionDt>) responseData.get("list");
		int count = (int) responseData.get("count");
		if (CollectionUtils.isNotEmpty(dtList)) {
			for (DgSampleCollectionDt dt : dtList) {
				Map<String, Object> map = new HashMap<String, Object>();
				//long subChargeCodeId = dt.getSubcharge();
				long hdId = dt.getSampleCollectionHdId();
				DgSampleCollectionDt dgSampleCollectionDt = labDao.getDgSampleCollectionDt(hdId);

				String collectedDate = HMSUtil.convertDateToStringFormat(dgSampleCollectionDt.getSampleCollDatetime(),
						"dd/MM/yyyy");

				if (collectedDate != null) {
					map.put("collectedDate", collectedDate);
				} else {
					map.put("collectedDate", "");
				}

				String collectedTime = JavaUtils
						.getTimeFromDateAndTime(dgSampleCollectionDt.getLastChgDate().toString());
				if (collectedTime != null) {
					map.put("collectedTime", collectedTime);
				} else {
					map.put("collectedTime", "");
				}

				/*
				 * if (dgSampleCollectionDt.getDgSampleCollectionHd() != null &&
				 * dgSampleCollectionDt.getDgSampleCollectionHd().getPatient() != null &&
				 * dgSampleCollectionDt.getDgSampleCollectionHd().getPatient().getServiceNo() !=
				 * null) { map.put("serviceNo",
				 * dgSampleCollectionDt.getDgSampleCollectionHd().getPatient().getServiceNo());
				 * } else { map.put("serviceNo", ""); }
				 */

				if (dgSampleCollectionDt.getDgSampleCollectionHd() != null
						&& dgSampleCollectionDt.getDgSampleCollectionHd().getVisitId() != null) {
					map.put("visitId", dgSampleCollectionDt.getDgSampleCollectionHd().getVisitId());
				} else {
					map.put("visitId", "");
				}

				if (dgSampleCollectionDt.getDgSampleCollectionHd() != null
						&& dgSampleCollectionDt.getDgSampleCollectionHd().getDepartmentId() != null) {
					map.put("departmentId", dgSampleCollectionDt.getDgSampleCollectionHd().getDepartmentId());
				} else {
					map.put("departmentId", "");
				}

				/*
				 * if (dgSampleCollectionDt.getDgSampleCollectionHd() != null &&
				 * dgSampleCollectionDt.getDgSampleCollectionHd().getDgOrderHd() != null &&
				 * dgSampleCollectionDt.getDgSampleCollectionHd().getDgOrderHd().
				 * getMasDepartment() != null &&
				 * dgSampleCollectionDt.getDgSampleCollectionHd().getDgOrderHd().
				 * getMasDepartment() .getDepartmentName() != null) { map.put("departmentName",
				 * dgSampleCollectionDt.getDgSampleCollectionHd().getDgOrderHd()
				 * .getMasDepartment().getDepartmentName()); } else { map.put("departmentName",
				 * ""); }
				 */

				Date collectedDate1 = dgSampleCollectionDt.getLastChgDate();

				if (dgSampleCollectionDt.getDgSampleCollectionHd().getPatient().getDateOfBirth() != null) {

					Date dateOfBirth = dgSampleCollectionDt.getDgSampleCollectionHd().getPatient().getDateOfBirth();
					if (collectedDate != null && dateOfBirth != null)

					{
						map.put("age", HMSUtil.calculateAge(dateOfBirth));

					}

					else {
						map.put("age", "");
					}

				}
				
				

				if (dgSampleCollectionDt.getDgSampleCollectionHd() != null
						&& dgSampleCollectionDt.getDgSampleCollectionHd().getSampleCollectionHdId() != null) {
					map.put("sampleCollectionHdId",
							dgSampleCollectionDt.getDgSampleCollectionHd().getSampleCollectionHdId());
				} else {
					map.put("sampleCollectionHdId", "");
				}

				if (dgSampleCollectionDt.getDgSampleCollectionHd() != null
						&& dgSampleCollectionDt.getDgSampleCollectionHd().getPatientId() != null) {
					map.put("patientId", dgSampleCollectionDt.getDgSampleCollectionHd().getPatientId());
				} else {
					map.put("patientId", "");
				}

				if (dgSampleCollectionDt.getDgSampleCollectionHd() != null
						&& dgSampleCollectionDt.getDgSampleCollectionHd().getPatient() != null) {
					map.put("mobileNo", dgSampleCollectionDt.getDgSampleCollectionHd().getPatient().getMobileNumber());
				} else {
					map.put("mobileNo", "");
				}
				
				if (dgSampleCollectionDt.getDgSampleCollectionHd() != null
						&& dgSampleCollectionDt.getDgSampleCollectionHd().getUser() != null) {
					map.put("CollectedBy", dgSampleCollectionDt.getDgSampleCollectionHd().getUser().getUserName());
				} else {
					map.put("CollectedBy", "");
				}

				if (dgSampleCollectionDt.getDgSampleCollectionHd() != null
						&& dgSampleCollectionDt.getDgSampleCollectionHd().getPatient() != null && dgSampleCollectionDt
								.getDgSampleCollectionHd().getPatient().getMasAdministrativeSex() != null) {
					map.put("gender", dgSampleCollectionDt.getDgSampleCollectionHd().getPatient()
							.getMasAdministrativeSex().getAdministrativeSexName());
				} else {
					map.put("gender", "");
				}

				if (dgSampleCollectionDt.getDgSampleCollectionHd() != null
						&& dgSampleCollectionDt.getDgSampleCollectionHd().getPatient() != null
						&& dgSampleCollectionDt.getDgSampleCollectionHd().getPatientId() != null) {
					map.put("patientName",
							dgSampleCollectionDt.getDgSampleCollectionHd().getPatient().getPatientName());
				} else {
					map.put("patientName", "");
				}

				/*
				 * if (dgSampleCollectionDt.getDgSampleCollectionHd() != null &&
				 * dgSampleCollectionDt.getDgSampleCollectionHd().getPatient() != null &&
				 * dgSampleCollectionDt.getDgSampleCollectionHd().getPatient().getMasRelation()
				 * != null &&
				 * dgSampleCollectionDt.getDgSampleCollectionHd().getPatient().getMasRelation()
				 * .getRelationName() != null) { map.put("relationName",
				 * dgSampleCollectionDt.getDgSampleCollectionHd().getPatient().getMasRelation()
				 * .getRelationName()); } else { map.put("relationName", ""); }
				 */
				/*
				 * map.put("subchargeCodeId", dgSampleCollectionDt.getSubcharge() != null ?
				 * dgSampleCollectionDt.getSubcharge() : "");
				 * 
				 * map.put("subChargecodeName",
				 * dgSampleCollectionDt.getMasSubChargecode().getSubChargecodeName() != null ?
				 * dgSampleCollectionDt.getMasSubChargecode().getSubChargecodeName() : "");
				 */

				map.put("sampleCollectionDtId",
						dgSampleCollectionDt.getSampleCollectionDtId() != null
								? dgSampleCollectionDt.getSampleCollectionDtId()
								: "");

				map.put("diagNo",
						dgSampleCollectionDt.getDgSampleCollectionHd() != null
								&& dgSampleCollectionDt.getDgSampleCollectionHd().getDgOrderHd() != null
								&& dgSampleCollectionDt.getDgSampleCollectionHd().getDgOrderHd().getOrderNo() != null
										? dgSampleCollectionDt.getDgSampleCollectionHd().getDgOrderHd().getOrderNo()
										: "");
				/*
				 * if (dgSampleCollectionDt.getDgMasInvestigation() != null &&
				 * dgSampleCollectionDt.getDgMasInvestigation().getInvestigationType() != null)
				 * { map.put("investigationType",
				 * dgSampleCollectionDt.getDgMasInvestigation().getInvestigationType()); } else
				 * { map.put("investigationType", ""); }
				 */
				list.add(map);
			}
			if (list != null && !list.isEmpty()) {
				json.put("data", list);
				json.put("msg", "Record Fetch Successfully");
				json.put("count", count);
				json.put("status", 1);
			} else {
				json.put("data", list);
				json.put("count", 0);
				json.put("msg", "No Record Found");
				json.put("status", 0);
			}
		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}

		return json.toString();
	}

	/**
	 * @Description: Method:getResultEntryWaitingList(). Waiting List of Result
	 *               Entry
	 * @param JSONObject         jsonObject
	 * @param HttpServletRequest request, HttpServletResponse response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String getResultEntryWaitingList(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List list = new ArrayList();
		if (jsonObject != null) {

			List<DgSampleCollectionDt> dgSampleCollectionDts = labDao.getResultEntryWaitingList(jsonObject);

			if (dgSampleCollectionDts != null && !dgSampleCollectionDts.isEmpty()) {

				for (DgSampleCollectionDt dt : dgSampleCollectionDts) {
					Map<String, Object> map = new HashMap<String, Object>();

					String collectedDate = HMSUtil
							.convertDateToStringFormat(dt.getDgSampleCollectionHd().getCollectionDate(), "dd/MM/yyyy");

					if (collectedDate != null) {
						map.put("collectedDate", collectedDate);
					} else {
						map.put("collectedDate", "");
					}

					String collectedTime = JavaUtils
							.getTimeFromDateAndTime(dt.getDgSampleCollectionHd().getLastChgDate().toString());
					if (collectedTime != null) {
						map.put("collectedTime", collectedTime);
					} else {
						map.put("collectedTime", "");
					}

					/*
					 * if (dt.getDgSampleCollectionHd() != null &&
					 * dt.getDgSampleCollectionHd().getPatient() != null &&
					 * dt.getDgSampleCollectionHd().getPatient().getServiceNo() != null) {
					 * map.put("serviceNo",
					 * dt.getDgSampleCollectionHd().getPatient().getServiceNo()); } else {
					 * map.put("serviceNo", ""); }
					 */

					if (dt.getDgSampleCollectionHd() != null && dt.getDgSampleCollectionHd().getVisitId() != null) {
						map.put("visitId", dt.getDgSampleCollectionHd().getVisitId());
					} else {
						map.put("visitId", "");
					}

					if (dt.getDgSampleCollectionHd() != null
							&& dt.getDgSampleCollectionHd().getDepartmentId() != null) {
						map.put("departmentId", dt.getDgSampleCollectionHd().getDepartmentId());
					} else {
						map.put("departmentId", "");
					}

					if (dt.getDgSampleCollectionHd() != null && dt.getDgSampleCollectionHd().getDgOrderHd() != null
							&& dt.getDgSampleCollectionHd().getDgOrderHd().getMasDepartment() != null
							&& dt.getDgSampleCollectionHd().getDgOrderHd().getMasDepartment()
									.getDepartmentName() != null) {
						map.put("departmentName",
								dt.getDgSampleCollectionHd().getDgOrderHd().getMasDepartment().getDepartmentName());
					} else {
						map.put("departmentName", "");
					}

					if (dt.getDgSampleCollectionHd() != null
							&& dt.getDgSampleCollectionHd().getSampleCollectionHdId() != null) {
						map.put("sampleCollectionHdId", dt.getDgSampleCollectionHd().getSampleCollectionHdId());
					} else {
						map.put("sampleCollectionHdId", "");
					}

					if (dt.getDgSampleCollectionHd() != null && dt.getDgSampleCollectionHd().getPatientId() != null) {
						map.put("patientId", dt.getDgSampleCollectionHd().getPatientId());
					} else {
						map.put("patientId", "");
					}
					if (dt.getDgSampleCollectionHd() != null && dt.getDgSampleCollectionHd().getPatient() != null
							&& dt.getDgSampleCollectionHd().getPatientId() != null) {
						map.put("patientName", dt.getDgSampleCollectionHd().getPatient().getPatientName());
					} else {
						map.put("patientName", "");
					}

					/*
					 * if (dt.getDgSampleCollectionHd() != null &&
					 * dt.getDgSampleCollectionHd().getPatient() != null &&
					 * dt.getDgSampleCollectionHd().getPatient().getMasRelation() != null &&
					 * dt.getDgSampleCollectionHd().getPatient().getMasRelation().getRelationName()
					 * != null) { map.put("relationName",
					 * dt.getDgSampleCollectionHd().getPatient().getMasRelation().getRelationName())
					 * ; } else { map.put("relationName", ""); }
					 */

					map.put("subchargeCodeId", dt.getSubcharge() != null ? dt.getSubcharge() : "");

					map.put("subChargecodeName",
							dt.getMasSubChargecode().getSubChargecodeName() != null
									? dt.getMasSubChargecode().getSubChargecodeName()
									: "");

					map.put("sampleCollectionDtId",
							dt.getSampleCollectionDtId() != null ? dt.getSampleCollectionDtId() : "");

					map.put("diagNo", dt.getDiagNo() != null ? dt.getDiagNo() : "");

					if (dt.getDgOrderdt() != null && dt.getDgOrderdt().getDgMasInvestigations() != null
							&& dt.getDgOrderdt().getDgMasInvestigations().getInvestigationType() != null) {
						map.put("investigationType", dt.getDgOrderdt().getDgMasInvestigations().getInvestigationType());
					} else {
						map.put("investigationType", "");
					}

					list.add(map);
				}
				// list.add(map);

				if (list != null && !list.isEmpty()) {
					json.put("data", list);
					json.put("msg", "Record Fetch Successfully");
					json.put("count", list.size());
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", list.size());
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}
			} else {
				json.put("data", dgSampleCollectionDts);
				// json.put("count", dgSampleCollectionDts.size());
				json.put("msg", "No Record Found");
				json.put("status", 0);
			}

		} else {
			json.put("msg", "Invalid Input");
			json.put("status", 0);
		}
		return json.toString();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public String getResultEntryDetails(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		String maxNormalValue = "";
		String minNormalValue = "";
		String normalRange = "";
		List<DgSampleCollectionDt> dgSampCollDtsInvList = null;
		List<DgSubMasInvestigation> subInvestigationList = null;
		List<DgFixedValue> fixedValueList = null;
		Map<String, Object> mapFixedValue = null;
		List<DgNormalValue> normalValList = null;
		List globalList = new ArrayList();
		List detailedList = new ArrayList();
		if (jsonObject != null) {
			Long sampleCollectionHdId = Long.parseLong(jsonObject.get("sampleCollectionHdId").toString());
			//Long subchargeCodeId = Long.parseLong(jsonObject.get("subchargeCodeId").toString());
			// String investigationType = jsonObject.get("investigationType").toString();

			//List<DgSampleCollectionDt> dgSampleCollectionDtsList = labDao.getResultEntryDetails(sampleCollectionHdId,subchargeCodeId);
			
			List<DgSampleCollectionDt> dgSampleCollectionDtsList = labDao.getResultEntryDetails(sampleCollectionHdId);
			// Map<String, Object> mapdata =
			// labDao.getAllInvestigations(sampleCollectionHdId, subchargeCodeId,
			// investigationType);

			//Map<String, Object> mapdata = labDao.getAllInvestigations(sampleCollectionHdId, subchargeCodeId);
			Map<String, Object> mapdata = labDao.getAllInvestigations(sampleCollectionHdId);
			if (CollectionUtils.isNotEmpty(dgSampleCollectionDtsList)) {
				try {
					for (DgSampleCollectionDt dt : dgSampleCollectionDtsList) {
						Map<String, Object> map = new HashMap<String, Object>();
						if (dt.getDgSampleCollectionHd() != null
								&& dt.getDgSampleCollectionHd().getCollectionDate() != null) {
							map.put("collectionDate", HMSUtil.convertDateToStringFormat(
									dt.getDgSampleCollectionHd().getCollectionDate(), "dd/MM/yyyy"));
						} else {
							map.put("collectionDate", "");
						}

						if (dt.getDgSampleCollectionHd().getLastChgDate() != null) {
							map.put("lastChgDate",
									HMSUtil.getDateWithoutTime(dt.getDgSampleCollectionHd().getLastChgDate()));
						} else {
							map.put("lastChgDate", "");
						}

						if (dt.getDgSampleCollectionHd().getPatientId() != null) {
							map.put("patientId", dt.getDgSampleCollectionHd().getPatientId());
						} else {
							map.put("patientId", "");
						}
						if (dt.getDgSampleCollectionHd().getPatient() != null
								&& dt.getDgSampleCollectionHd().getPatient().getPatientName() != null) {
							map.put("patientName", dt.getDgSampleCollectionHd().getPatient().getPatientName());
						} else {
							map.put("patientName", "");
						}
						/*
						 * if (dt.getDgSampleCollectionHd().getPatient() != null &&
						 * dt.getDgSampleCollectionHd().getPatient().getMasrelation() != null &&
						 * dt.getDgSampleCollectionHd().getPatient().getMasrelation().getRelationId() !=
						 * null) { map.put("relationId",
						 * dt.getDgSampleCollectionHd().getPatient().getMasrelation().getRelationId());
						 * } else { map.put("relationId", ""); }
						 */

						/*
						 * if (dt.getDgSampleCollectionHd().getPatient() != null &&
						 * dt.getDgSampleCollectionHd().getPatient().getMasrelation() != null &&
						 * dt.getDgSampleCollectionHd().getPatient().getMasrelation() .getRelationName()
						 * != null) { map.put("relationName",
						 * dt.getDgSampleCollectionHd().getPatient().getMasrelation().getRelationName())
						 * ; } else { map.put("relationName", ""); }
						 */

						if (dt.getDgSampleCollectionHd() != null && dt.getDgSampleCollectionHd().getDgOrderHd() != null
								&& dt.getDgSampleCollectionHd().getDgOrderHd().getOrderNo() != null) {
							map.put("diagNo", dt.getDgSampleCollectionHd().getDgOrderHd().getOrderNo());
						} else {
							map.put("diagNo", "");
						}

						if (dt.getDgSampleCollectionHd().getPatient().getDateOfBirth() != null) {
							Date dateOfBirth = dt.getDgSampleCollectionHd().getPatient().getDateOfBirth();
							// String age = JavaUtils.calculateAgefromDob(dateOfBirth);
							if (dateOfBirth != null) {
								map.put("dateOfBirth", dateOfBirth);
							} else {
								map.put("dateOfBirth", "");
							}

							Date visitDate = null;
							if (dt.getDgSampleCollectionHd().getDgOrderHd().getVisit() != null
									&& dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getVisitDate() != null) {
								visitDate = dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getVisitDate();
							}
							if (  dateOfBirth != null) {
								map.put("age", HMSUtil.calculateAge(dateOfBirth));
							} else {
								map.put("age", "");
							}
							// map.put("age", age);
						} else {
							map.put("age", "");
						}

						if (dt.getDgSampleCollectionHd().getPatient() != null
								&& dt.getDgSampleCollectionHd().getPatient().getMasAdministrativeSex() != null
								&& dt.getDgSampleCollectionHd().getPatient().getMasAdministrativeSex()
										.getAdministrativeSexName() != null) {
							map.put("gender", dt.getDgSampleCollectionHd().getPatient().getMasAdministrativeSex()
									.getAdministrativeSexName());
						} else {
							map.put("gender", "");
						}

						if (dt.getDgSampleCollectionHd().getPatient() != null
								&& dt.getDgSampleCollectionHd().getPatient().getAdministrativeSexId() != null) {
							map.put("genderId", dt.getDgSampleCollectionHd().getPatient().getAdministrativeSexId());
						} else {
							map.put("genderId", "");
						}

						
						  if (dt.getDgSampleCollectionHd().getPatient() != null &&
						  dt.getDgSampleCollectionHd().getPatient().getMobileNumber() != null) {
						  map.put("mobileNo",
						  dt.getDgSampleCollectionHd().getPatient().getMobileNumber()); } else {
						  map.put("mobileNo", ""); }
						 

						if (dt.getDgSampleCollectionHd().getVisitId() != null) {
							map.put("visitId", dt.getDgSampleCollectionHd().getVisitId());
						} else {
							map.put("visitId", "");
						}

						if (dt.getDgSampleCollectionHd().getDepartmentId() != null) {
							map.put("departmentId", dt.getDgSampleCollectionHd().getDepartmentId());
						} else {
							map.put("departmentId", "");
						}

						if (dt.getDgSampleCollectionHd().getMmuId() != null) {
							map.put("hospitalId", dt.getDgSampleCollectionHd().getMmuId());
						} else {
							map.put("hospitalId", "");
						}

						if (dt.getDgSampleCollectionHd() != null && dt.getDgSampleCollectionHd().getDgOrderHd() != null
								&& dt.getDgSampleCollectionHd().getDgOrderHd().getMasDepartment() != null
								&& dt.getDgSampleCollectionHd().getDgOrderHd().getMasDepartment()
										.getDepartmentName() != null) {
							map.put("departmentName",
									dt.getDgSampleCollectionHd().getDgOrderHd().getMasDepartment().getDepartmentName());
						} else {
							map.put("departmentName", "");
						}

						/*
						 * if (dt.getDgSampleCollectionHd() != null &&
						 * dt.getDgSampleCollectionHd().getPatient() != null &&
						 * dt.getDgSampleCollectionHd().getPatient().getMasRank() != null &&
						 * dt.getDgSampleCollectionHd().getPatient().getMasRank().getRankName() != null)
						 * { map.put("rankName",
						 * dt.getDgSampleCollectionHd().getPatient().getMasRank().getRankName()); } else
						 * { map.put("rankName", ""); }
						 * 
						 * if (dt.getDgSampleCollectionHd() != null &&
						 * dt.getDgSampleCollectionHd().getDgOrderHd() != null &&
						 * dt.getDgSampleCollectionHd().getDgOrderHd().getUsers() != null &&
						 * dt.getDgSampleCollectionHd().getDgOrderHd().getUsers().getFirstName() !=
						 * null) { map.put("enteredBy",
						 * dt.getDgSampleCollectionHd().getDgOrderHd().getUsers().getFirstName()); }
						 * else { map.put("enteredBy", ""); }
						 */

						if (dt.getDgSampleCollectionHd() != null && dt.getDgSampleCollectionHd().getDgOrderHd() != null
								&& dt.getDgSampleCollectionHd().getDgOrderHd().getUsers() != null
								&& dt.getDgSampleCollectionHd().getDgOrderHd().getUsers().getUserId() != null) {
							map.put("userId", dt.getDgSampleCollectionHd().getDgOrderHd().getUsers().getUserId());
						} else {
							map.put("userId", "");
						}

						if (dt.getDgSampleCollectionHd().getSampleCollectionHdId() != null) {
							map.put("sampleCollectionHdId", dt.getDgSampleCollectionHd().getSampleCollectionHdId());
						} else {
							map.put("sampleCollectionHdId", "");
						}

						if (dt.getSampleCollectionDtId() != null) {
							map.put("sampleCollectionDtId", dt.getSampleCollectionDtId());
						} else {
							map.put("sampleCollectionDtId", "");
						}

						if (dt.getDgSampleCollectionHd().getOrderhdId() != null) {
							map.put("orderHdId", dt.getDgSampleCollectionHd().getOrderhdId());
						} else {
							map.put("orderHdId", "");
						}

						if (dt.getDgMasInvestigation().getInvestigationName() != null) {
							map.put("parentInvestigationName", dt.getDgMasInvestigation().getInvestigationName());
						} else {
							map.put("parentInvestigationName", "");
						}

						if (dt.getDgMasInvestigation().getInvestigationId() != null) {
							map.put("parentInvestigationId", dt.getDgMasInvestigation().getInvestigationId());
						} else {
							map.put("parentInvestigationId", "");
						}

						if (dt.getMaincharge() != null) {
							map.put("mainChargeCodeId", dt.getMaincharge());
						} else {
							map.put("mainChargeCodeId", "");
						}
						if (dt.getSubcharge() != null) {
							map.put("subChargeCodeId", dt.getSubcharge());
						} else {
							map.put("subChargeCodeId", "");
						}

						if (dt.getDgMasInvestigation() != null
								&& dt.getDgMasInvestigation().getInvestigationType() != null) {
							map.put("pInvestigationType", dt.getDgMasInvestigation().getInvestigationType());
						} else {
							map.put("pInvestigationType", "");
						}

						/*
						 * if (dt.getDgSampleCollectionHd() != null &&
						 * dt.getDgSampleCollectionHd().getPatient() != null &&
						 * dt.getDgSampleCollectionHd().getPatient().getEmployeeName() != null) {
						 * map.put("employeeName",
						 * dt.getDgSampleCollectionHd().getPatient().getEmployeeName()); } else {
						 * map.put("employeeName", ""); }
						 */

						if (dt.getOrderdtId() != null) {
							map.put("orderdtId", dt.getOrderdtId());
						} else {
							map.put("orderdtId", "");
						}

						// result entry details.
						LocalDate currentDate1 = LocalDate.now();
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
						String currentDate = formatter.format(currentDate1);
						String currTime = LocalTime.now().toString();
						String[] currTime1 = currTime.split("\\.");
						String currentTime = currTime1[0];

						map.put("currentDate", currentDate);
						map.put("currentTime", currentTime);

						detailedList.add(map);

					}

				} catch (Exception e) {
					e.printStackTrace();
					return "{\"status\":\"0\",\"msg\":\"Something went wrong}";
				}

			} else {
				json.put("msg", "No Record Found");
				json.put("count", dgSampleCollectionDtsList.size());
			}
			List dataSingleParameterList = new ArrayList();
			List multiSubInvestigationList = new ArrayList();
			List fixedValues = new ArrayList();
			List norValList = new ArrayList();
			List dataTemplateParameterList = new ArrayList();
			List<DgSampleCollectionDt> invTypeTempList = new ArrayList<DgSampleCollectionDt>();

			if (mapdata != null) {
				dgSampCollDtsInvList = (List<DgSampleCollectionDt>) mapdata.get("dgSampCollDtsInvList");
				subInvestigationList = (List<DgSubMasInvestigation>) mapdata.get("subInvestigationList");
				fixedValueList = (List<DgFixedValue>) mapdata.get("fixedValueList");
				mapFixedValue = (Map<String, Object>) mapdata.get("mapFixedValue");
				normalValList = (List<DgNormalValue>) mapdata.get("normalValList");
				invTypeTempList = (List<DgSampleCollectionDt>) mapdata.get("invTypeTempList");

				if (CollectionUtils.isNotEmpty(dgSampCollDtsInvList)) {
					for (DgSampleCollectionDt dt : dgSampCollDtsInvList) {
						Map<String, Object> map1 = new HashMap<String, Object>();
						if (dt.getSampleCollectionDtId() != null) {
							map1.put("sampleCollectionDtId", dt.getSampleCollectionDtId());
						} else {
							map1.put("sampleCollectionDtId", "");
						}

						if (dt.getSampleCollectionHdId() != null) {
							map1.put("sampleCollectionHdId", dt.getSampleCollectionHdId());
						} else {
							map1.put("sampleCollectionHdId", "");
						}

						if (dt.getInvestigationId() != null) {
							map1.put("investigationId", dt.getInvestigationId());
						} else {
							map1.put("investigationId", "");
						}

						if (dt.getDgMasInvestigation() != null
								&& dt.getDgMasInvestigation().getInvestigationName() != null) {
							map1.put("investigationName", dt.getDgMasInvestigation().getInvestigationName());
						} else {
							map1.put("investigationName", "");
						}

						if (dt.getSampleId() != null) {
							map1.put("sampleId", dt.getSampleId());
						} else {
							map1.put("sampleId", "");
						}

						if (dt.getDgMasInvestigation().getMasSample().getSampleDescription() != null) {
							map1.put("sampleDescription",
									dt.getDgMasInvestigation().getMasSample().getSampleDescription());
						} else {
							map1.put("sampleDescription", "");
						}

						if (dt.getDgMasInvestigation() != null && dt.getDgMasInvestigation().getMasUOM() != null
								&& dt.getDgMasInvestigation().getMasUOM().getUOMName() != null) {
							map1.put("uomName", dt.getDgMasInvestigation().getMasUOM().getUOMName());
						} else {
							map1.put("uomName", "");
						}

						if (dt.getDgMasInvestigation() != null && dt.getDgMasInvestigation().getMasUOM() != null
								&& dt.getDgMasInvestigation().getMasUOM().getUOMName() != null) {
							map1.put("uomId", dt.getDgMasInvestigation().getMasUOM().getUOMId());
						} else {
							map1.put("uomId", "");
						}

						if (dt.getDgMasInvestigation().getInvestigationType() != null) {
							map1.put("investigationType", dt.getDgMasInvestigation().getInvestigationType());
						} else {
							map1.put("investigationType", "");
						}

						if (dt.getDgMasInvestigation().getMinNormalValue() != null) {
							minNormalValue = dt.getDgMasInvestigation().getMinNormalValue();
							map1.put("minNormalValue", minNormalValue);
						} else {
							map1.put("minNormalValue", "");
						}

						if (dt.getDgMasInvestigation().getMaxNormalValue() != null) {
							maxNormalValue = dt.getDgMasInvestigation().getMaxNormalValue();
							map1.put("maxNormalValue", maxNormalValue);
						} else {
							map1.put("maxNormalValue", "");
						}

						normalRange = minNormalValue.concat("-").concat(maxNormalValue);
						map1.put("normalRange", normalRange);

						if (dt.getDgSampleCollectionHd() != null && dt.getDgSampleCollectionHd().getDgOrderHd() != null
								&& dt.getDgSampleCollectionHd().getDgOrderHd().getOrderNo() != null) {
							map1.put("diagNo", dt.getDgSampleCollectionHd().getDgOrderHd().getOrderNo());
						} else {
							map1.put("diagNo", "");
						}

						if (dt.getMaincharge() != null) {
							map1.put("mainchargeCodeId", dt.getMaincharge());
						} else {
							map1.put("mainchargeCodeId", "");
						}

						if (dt.getSubcharge() != null) {
							map1.put("subChargeCodeId", dt.getSubcharge());
						} else {
							map1.put("subChargeCodeId", "");
						}

						if (dt.getOrderdtId() != null) {
							map1.put("orderdtId", dt.getOrderdtId());
						} else {
							map1.put("orderdtId", "");
						}

						dataSingleParameterList.add(map1);
					}
				}

				if (CollectionUtils.isNotEmpty(subInvestigationList)) {
					for (DgSubMasInvestigation submInv : subInvestigationList) {
						Map<String, Object> map2 = new HashMap<String, Object>();
						if (submInv.getInvestigationId() != null) {
							map2.put("investigationId", submInv.getInvestigationId());
						} else {
							map2.put("investigationId", "");
						}

						if (submInv.getSubInvestigationId() != null) {
							map2.put("subInvestigationId", submInv.getSubInvestigationId());
						} else {
							map2.put("subInvestigationId", "");
						}

						if (submInv.getDgMasInvestigation() != null
								&& submInv.getDgMasInvestigation().getInvestigationName() != null) {
							map2.put("investigationName", submInv.getDgMasInvestigation().getInvestigationName());
						} else {
							map2.put("investigationName", "");
						}

						if (submInv.getSubInvestigationName() != null) {
							map2.put("subInvestigationName", submInv.getSubInvestigationName());
						} else {
							map2.put("subInvestigationName", "");
						}

						if (submInv.getResultType() != null) {
							map2.put("resultType", submInv.getResultType());
						} else {
							map2.put("resultType", "");
						}

						if (submInv.getComparisonType() != null) {
							map2.put("comparisonType", submInv.getComparisonType());
						} else {
							map2.put("comparisonType", "");
						}

						if (submInv.getDgMasInvestigation() != null
								&& submInv.getDgMasInvestigation().getInvestigationType() != null) {
							map2.put("investigationType", submInv.getDgMasInvestigation().getInvestigationType());
						} else {
							map2.put("investigationType", "");
						}

						if (submInv.getDgMasInvestigation() != null
								&& submInv.getDgMasInvestigation().getSubChargecodeId() != null) {
							map2.put("subChargeCode", submInv.getDgMasInvestigation().getSubChargecodeId());
						} else {
							map2.put("subChargeCode", "");
						}

						if (submInv.getDgMasInvestigation() != null
								&& submInv.getDgMasInvestigation().getMainChargecodeId() != null) {
							map2.put("mainChargeCode", submInv.getDgMasInvestigation().getMainChargecodeId());
						} else {
							map2.put("mainChargeCode", "");
						}

						if (submInv.getDgMasInvestigation() != null
								&& submInv.getDgMasInvestigation().getSampleId() != null) {
							map2.put("sampleId", submInv.getDgMasInvestigation().getSampleId());
						} else {
							map2.put("sampleId", "");
						}

						if (submInv.getDgMasInvestigation() != null
								&& submInv.getDgMasInvestigation().getMasSample() != null
								&& submInv.getDgMasInvestigation().getMasSample().getSampleDescription() != null) {
							map2.put("sampleDescription",
									submInv.getDgMasInvestigation().getMasSample().getSampleDescription());
						} else {
							map2.put("sampleDescription", "");
						}

						if (submInv.getMasUOM() != null && submInv.getMasUOM().getUOMId() != null) {
							map2.put("uomId", submInv.getMasUOM().getUOMId());
						} else {
							map2.put("uomId", "");
						}

						if (submInv.getMasUOM() != null && submInv.getMasUOM().getUOMName() != null) {
							map2.put("uomName", submInv.getMasUOM().getUOMName());
						} else {
							map2.put("uomName", "");
						}

						if (submInv.getDgFixedValues() != null) {
							List<DgFixedValue> dgFixedValues = submInv.getDgFixedValues();
							Map<Object, Object> dgfixedValueMap = new HashMap<Object, Object>();
							for (DgFixedValue dgfixedValue : dgFixedValues) {
								dgfixedValueMap.put(dgfixedValue.getFixedValue().trim(),
										dgfixedValue.getFixedId() + "@@" + dgfixedValue.getFixedValue().trim());
							}
							map2.put("dgFixedValue", dgfixedValueMap);
						} else {
							map2.put("dgFixedValue", "");
						}

						multiSubInvestigationList.add(map2);
					}
				}

				if (CollectionUtils.isNotEmpty(fixedValueList)) {
					for (DgFixedValue fixedValue : fixedValueList) {
						Map<String, Object> map3 = new HashMap<String, Object>();
						if (fixedValue.getFixedId() != null) {
							map3.put("fixedId", fixedValue.getFixedId());
						} else {
							map3.put("fixedId", "");
						}
						if (fixedValue.getFixedValue() != null) {
							map3.put("fixedValue", fixedValue.getFixedValue());
						} else {
							map3.put("fixedValue", "");
						}

						if (fixedValue.getSubInvestigationId() != null) {
							map3.put("subInvestigationId", fixedValue.getSubInvestigationId());
						} else {
							map3.put("subInvestigationId", "");
						}
						fixedValues.add(map3);
					}

				}

				if (!CollectionUtils.isEmpty(normalValList)) {
					for (DgNormalValue normalValue : normalValList) {
						Map<String, Object> mapNormalVal = new HashMap<String, Object>();

						if (normalValue.getSex() != null) {
							mapNormalVal.put("gender", normalValue.getSex());
						} else {
							mapNormalVal.put("gender", "");
						}

						if (normalValue.getNormalId() != null) {
							mapNormalVal.put("normalId", normalValue.getNormalId());
						} else {
							mapNormalVal.put("normalId", "");
						}

						if (normalValue.getFromAge() != null) {
							mapNormalVal.put("fromAge", normalValue.getFromAge());
						} else {
							mapNormalVal.put("fromAge", "");
						}

						if (normalValue.getToAge() != null) {
							mapNormalVal.put("toAge", normalValue.getToAge());
						} else {
							mapNormalVal.put("toAge", "");
						}

						if (normalValue.getMinNormalValue() != null) {
							mapNormalVal.put("minNormalValue", normalValue.getMinNormalValue());
						} else {
							mapNormalVal.put("minNormalValue", "");
						}

						if (normalValue.getMaxNormalValue() != null) {
							mapNormalVal.put("maxNormalValue", normalValue.getMaxNormalValue());
						} else {
							mapNormalVal.put("maxNormalValue", "");
						}

						if (normalValue.getNormalValue() != null) {
							mapNormalVal.put("normalValue", normalValue.getNormalValue());
						} else {
							mapNormalVal.put("normalValue", "");
						}

						if (normalValue.getSubInvestigationId() != null) {
							mapNormalVal.put("subInvestigationId", normalValue.getSubInvestigationId());
						} else {
							mapNormalVal.put("subInvestigationId", "");
						}

						String range = normalValue.getMinNormalValue() + "-" + normalValue.getMaxNormalValue();
						mapNormalVal.put("range", range);
						norValList.add(mapNormalVal);
					}
				}

				if (CollectionUtils.isNotEmpty(invTypeTempList)) {
					for (DgSampleCollectionDt dt : invTypeTempList) {
						Map<String, Object> invTypeTempmap = new HashMap<String, Object>();
						invTypeTempmap.put("diagNo",
								dt.getDgSampleCollectionHd() != null
										&& dt.getDgSampleCollectionHd().getOrderId() != null
												? dt.getDgSampleCollectionHd().getOrderId()
												: "");
						invTypeTempmap.put("investigationId",
								dt.getInvestigationId() != null ? dt.getInvestigationId() : "");
						invTypeTempmap.put("investigationName",
								dt.getDgMasInvestigation() != null
										&& dt.getDgMasInvestigation().getInvestigationName() != null
												? dt.getDgMasInvestigation().getInvestigationName().trim()
												: "");
						invTypeTempmap.put("sampleCollectionDtId",
								dt.getSampleCollectionDtId() != null ? dt.getSampleCollectionDtId() : "");
						invTypeTempmap.put("subChargeCode", dt.getSubcharge() != null ? dt.getSubcharge() : "");
						invTypeTempmap.put("subChargeName",
								dt.getMasSubChargecode() != null
										&& dt.getMasSubChargecode().getSubChargecodeName() != null
												? dt.getMasSubChargecode().getSubChargecodeName()
												: "");
						invTypeTempmap.put("mainChargeCode", dt.getMaincharge() != null ? dt.getMaincharge() : "");
						invTypeTempmap.put("sampleId", dt.getSampleId() != null ? dt.getSampleId() : "");
						invTypeTempmap.put("sampleDescription",
								dt.getDgMasInvestigation() != null && dt.getDgMasInvestigation().getMasSample() != null
										&& dt.getDgMasInvestigation().getMasSample().getSampleDescription() != null
												? dt.getDgMasInvestigation().getMasSample().getSampleDescription()
												: "");
						invTypeTempmap.put("uomId",
								dt.getDgMasInvestigation() != null && dt.getDgMasInvestigation().getUomId() != null
										? dt.getDgMasInvestigation().getUomId()
										: "");
						invTypeTempmap.put("uomName",
								dt.getDgMasInvestigation() != null && dt.getDgMasInvestigation().getMasUOM() != null
										&& dt.getDgMasInvestigation().getMasUOM().getUOMName() != null
												? dt.getDgMasInvestigation().getMasUOM().getUOMName()
												: "");
						invTypeTempmap.put("normalRange", "");
						invTypeTempmap.put("orderDtId", dt.getOrderdtId() != null ? dt.getOrderdtId() : "");
						invTypeTempmap.put("sampleCollectionHdId",
								dt.getSampleCollectionHdId() != null ? dt.getSampleCollectionHdId() : "");
						invTypeTempmap.put("investigationType",
								dt.getDgMasInvestigation() != null
										&& dt.getDgMasInvestigation().getInvestigationType() != null
												? dt.getDgMasInvestigation().getInvestigationType().trim()
												: "");
						dataTemplateParameterList.add(invTypeTempmap);
					}
				}
			}

			Map<String, Object> globalMap = new HashMap<String, Object>();
			globalMap.put("data", detailedList);
			globalMap.put("dataSingleParameter", dataSingleParameterList);
			globalMap.put("subInvestigationList", multiSubInvestigationList);
			globalMap.put("mapFixedValue", mapFixedValue);
			globalMap.put("fixedValues", fixedValues);
			globalMap.put("norValList", norValList);
			globalMap.put("dataTemplateParameter", dataTemplateParameterList);
			globalMap.put("count", detailedList.size());
			globalMap.put("count1", multiSubInvestigationList.size());
			globalMap.put("msg", "Record Fetch Successfully");
			globalMap.put("status", 1);

			globalList.add(globalMap);

			if (globalList != null && !globalList.isEmpty()) {
				json.put("globalList", globalList);
			}

		} else {
			json.put("msg", "Invalid Input");
			json.put("status", 0);

		}
		return json.toString();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String submitResultEntryDetails(JSONObject payload, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		Session session = null;

		Transaction tx = null;
		session = getHibernateUtils.getHibernateUtlis().OpenSession();
		tx = session.beginTransaction();
		String suserId = payload.get("userId").toString();
		String scampId = payload.get("campId").toString();
		
		String mmuName =payload.get("mmuName").toString();
		String cityName =payload.get("cityName").toString();
		
		
		Long nUserId = Long.parseLong(suserId);
		Long ncampId = Long.parseLong(scampId);
		MasCamp masCamp=null;
		if(StringUtils.isEmpty(mmuName) && StringUtils.isEmpty(cityName))
		masCamp=labDao.getMasCampFromMMUId(ncampId,session);
		if(masCamp!=null) {
			if(masCamp.getMasMMU()!=null)
				mmuName=masCamp.getMasMMU().getMmuName();
			if(masCamp.getMasCity()!=null)
				cityName=masCamp.getMasCity().getCityName();
		}
		String[] sampleCollectionHdIdsArray = null;
		String[] diagNoValue = null;
		String[] parentInvIdValue = null;
		Long visitId = null;
		String[] patId = null;
		String[] deptId = null;
		String[] sampleCollectionDtIdsArray = null;
		// String [] parentInvestIdsArray = null;
		String[] subInvestigationIdsArray = null;
		// String [] diagNosArray = null;
		String[] resultsIdsArray = null;
		String[] samplesIdsArray = null;
		String[] fixedIdsArray = null;
		String[] remarkIdsArray = null;
		String[] mainchargeCodeIdsArray = null;
		String[] subchargeCodeIdsArray = null;
		String[] investigationTypesArray = null;
		String[] orderHdIdsArray = null;
		String[] normalIdsArray = null;
		String[] resultsArray = null;
		String[] resultValue = null;
		Long resultEnterHdId = null;
		Long resultEnterdtId = null;
		
		String[] patientNameVal = null;
		String[] orderDateVal = null;
		String[] campNameVal = null;
		String[] mobileNoVal=null;
		String patientName=null;
		String orderDate=null;
		String campName=null;
		String mobileNo=null;
	    if (payload.has("patientName")) {
	    	 patientName = payload.get("patientName").toString();
	    	patientName = JavaUtils.replaceStringWithDoubleQuotes(patientName);
	    	patientNameVal = patientName.split(",");
		}
	    if (payload.has("date")) {
	    	orderDate = payload.get("date").toString();
	    	orderDate = JavaUtils.replaceStringWithDoubleQuotes(orderDate);
	    	orderDateVal = orderDate.split(",");
		}
	    if (payload.has("campName")) {
	    	campName = payload.get("campName").toString();
	    	campName = JavaUtils.replaceStringWithDoubleQuotes(campName);
	    	campNameVal = campName.split(",");
		}
	    if (payload.has("mobileNo")) {
	    	mobileNo = payload.get("mobileNo").toString();
	    
	    	mobileNo = JavaUtils.replaceStringWithDoubleQuotes(mobileNo);
	    	mobileNoVal = mobileNo.split(",");
	    	 
		}
	    
	    String orderHdIdR="";
	    if (payload.has("orderHdId")) {
	    	orderHdIdR = payload.get("orderHdId").toString();
	    	orderHdIdR = JavaUtils.replaceStringWithDoubleQuotes(orderHdIdR);
	    	 
		}
	
	    try {
			//final String uri = "https://2factor.in/API/R1/?module=TRANS_SMS&apikey=5cdc6365-22b5-11ec-a13b-0200cd936042&to="+mobileNo+"&from=CGMSSY&templatename=Apartment&var1="+followUpLocationForSend+"&var2="+followUpLandmarksForSend+"&var3="+followUpDateForSend+"";
			//final String url="https://www.cgmmssy.in/report/generateLabHistoryReport?oderHdId="+orderHdIdR+"";
	    	String urlForMessage = HMSUtil.getProperties("adt.properties", "urlForMessage").trim();
	    	
	    	String url=urlForMessage+"?orderHdId="+orderHdIdR;
	    	StringBuilder sb = new StringBuilder();
			 String sEmailURL = url;
			// sb.append(getFormatTemp(sEmailURL));
		     
			//sb.append("<a href="+url+">Click</a>");
			 sb.append(url);
			//final String uri = "https://2factor.in/API/R1/?module=TRANS_SMS&apikey=5cdc6365-22b5-11ec-a13b-0200cd936042&to="+mobileNo+"&from=CGMSSY&templatename=LinkMMU&var1="+URLEncoder.encode(patientName,"UTF8")+"&var2="+URLEncoder.encode(mmuName,"UTF8")+""+URLEncoder.encode(cityName,"UTF8")+"&var3="+URLEncoder.encode(sb.toString(),"UTF8")+"";
		    
	    	//final String uri = "https://2factor.in/API/R1/?module=TRANS_SMS&apikey=5cdc6365-22b5-11ec-a13b-0200cd936042&to="+mobileNo+"&from=CGMSSY&templatename=Report6&var1="+patientName+"&var2="+mmuName+""+cityName+"&var3="+URLEncoder.encode(sb.toString(),"UTF8")+"";
		      final String uri = "https://2factor.in/API/R1/?module=TRANS_SMS&apikey=5cdc6365-22b5-11ec-a13b-0200cd936042&to="+mobileNo+"&from=CGMMSY&templatename=LinkMMU-New&var1="+patientName+"&var2="+mmuName+"&var3="+sEmailURL+"&shortenurl=1";
			 //final String uri = "https://2factor.in/API/R1/?module=TRANS_SMS&apikey=5cdc6365-22b5-11ec-a13b-0200cd936042&to="+mobileNo+"&from=CGMSSY&templatename=Report6&var1="+patientName+"&var2="+orderDate+"&var3="+campName+"";	
			 HttpResponse<String> resp = Unirest.post(uri).asString();
			//System.out.println(resp.getBody());
			System.out.println("Lab Message send successfully");
			if(resp.getBody()!=null)
			{
				boolean status=resp.getBody().contains("Status");
				if(status==true)
				{
					System.out.println("Message delivered succssfully");
				}
			    
			}
			//return resp.getBody();
			} catch (Exception e) {
				e.printStackTrace();
			}
	    
		List<Long> resultEnterdtId1 = new ArrayList<>();
		Long sampleCollectionHdId = null;
		
		try {
			

			DgResultEntryHd dgResultEntryHd = null;
			DgResultEntryDt dgResultEntryDt = null;
			DgResultEntryHd dgResultEntryHdObject = null;
			String[] investigationTypeValues = null;
			String[] pInvestTypeValues = null;

			if (payload.has("investigationType")) {
				String investigationTypes = payload.get("investigationType").toString();
				investigationTypes = JavaUtils.replaceStringWithDoubleQuotes(investigationTypes);
				investigationTypeValues = investigationTypes.split(",");
			}

			String diagNo = payload.get("diagNo").toString();
			diagNo = JavaUtils.replaceStringWithDoubleQuotes(diagNo);
			diagNoValue = diagNo.split(",");

			if (payload.has("parentInvId")) {
				String parentInvId = payload.get("parentInvId").toString();
				parentInvId = JavaUtils.replaceStringWithDoubleQuotes(parentInvId);
				parentInvIdValue = parentInvId.split(",");
			}

			if (payload.has("result")) {
				String result = payload.get("result").toString();
				result = JavaUtils.replaceStringWithDoubleQuotes(result);
				resultValue = result.split(",");
			} else if (payload.has("resultId")) {
				String result = String.valueOf(payload.get("resultId"));
				result = JavaUtils.replaceStringWithDoubleQuotes(result);
				resultValue = result.split(",");
			}

			String samplecollectionHdId = "";
			if (StringUtils.isNotBlank(payload.get("sampleCollectionHdId").toString())) {
				String sampleCollectionHdIds = payload.get("sampleCollectionHdId").toString();
				sampleCollectionHdIds = JavaUtils.replaceStringWithDoubleQuotes(sampleCollectionHdIds);
				sampleCollectionHdIdsArray = sampleCollectionHdIds.split(",");
				for (int j = 0; j < sampleCollectionHdIdsArray.length; j++) {
					String[] sampleCollectionHeaderIds = sampleCollectionHdIdsArray[j].split("##");
					samplecollectionHdId = sampleCollectionHeaderIds[0];
				}
			}

			if (StringUtils.isNotBlank(payload.get("patientId").toString())) {
				String patientId = payload.get("patientId").toString();
				patientId = JavaUtils.replaceStringWithDoubleQuotes(patientId);
				patId = patientId.split(",");
			}

			if (StringUtils.isNotBlank(payload.get("departmentId").toString())) {
				String departmentId = payload.get("departmentId").toString();
				departmentId = JavaUtils.replaceStringWithDoubleQuotes(departmentId);
				deptId = departmentId.split(",");
			}

			if (StringUtils.isNotBlank(payload.get("sampleCollectionDtId").toString())) {
				String sampleCollectionDtIds = payload.get("sampleCollectionDtId").toString();
				sampleCollectionDtIds = JavaUtils.replaceStringWithDoubleQuotes(sampleCollectionDtIds);
				sampleCollectionDtIdsArray = sampleCollectionDtIds.split(",");
			}

			if (payload.has("subInvestigationIds")) {
				if (StringUtils.isNotBlank(payload.get("subInvestigationIds").toString())) {
					String subInvestigationIds = payload.get("subInvestigationIds").toString();
					subInvestigationIds = JavaUtils.replaceStringWithDoubleQuotes(subInvestigationIds);
					subInvestigationIdsArray = subInvestigationIds.split(",");
				}
			}
			/*
			 * if(payload.has("resultId")) {
			 * if(StringUtils.isNotBlank(payload.get("resultId").toString())) { String
			 * resultIds = payload.get("resultId").toString(); resultIds =
			 * JavaUtils.replaceStringWithDoubleQuotes(resultIds); resultsIdsArray =
			 * resultIds.split(","); } }
			 */

			if (StringUtils.isNotBlank(payload.get("sampleIds").toString())) {
				String samplesIds = payload.get("sampleIds").toString();
				samplesIds = JavaUtils.replaceStringWithDoubleQuotes(samplesIds);
				samplesIdsArray = samplesIds.split(",");
			}

			if (payload.has("fixedIds")) {
				if (StringUtils.isNotBlank(payload.get("fixedIds").toString())) {
					String fixedIds = payload.get("fixedIds").toString();
					fixedIds = JavaUtils.replaceStringWithDoubleQuotes(fixedIds);
					fixedIdsArray = fixedIds.split(",");
				}
			}
			if (payload.has("normalIds")) {
				if (StringUtils.isNotBlank(payload.get("normalIds").toString())) {
					String normalIds = payload.get("normalIds").toString();
					normalIds = JavaUtils.replaceStringWithDoubleQuotes(normalIds);
					normalIdsArray = normalIds.split(",");
				}
			}
			// if(StringUtils.isNotBlank(payload.get("remarks").toString())) {
			String remarkIds = payload.get("remarks").toString();
			remarkIds = JavaUtils.replaceStringWithDoubleQuotes(remarkIds);
			remarkIdsArray = remarkIds.split(",");
			// }

			if (StringUtils.isNotBlank(payload.get("mainChargeCodeId").toString())) {
				String mainchargeCodeIds = payload.get("mainChargeCodeId").toString();
				mainchargeCodeIds = JavaUtils.replaceStringWithDoubleQuotes(mainchargeCodeIds);
				mainchargeCodeIdsArray = mainchargeCodeIds.split(",");
			}

			if (StringUtils.isNotBlank(payload.get("subChargeCodeId").toString())) {
				String subchargeCodeIds = payload.get("subChargeCodeId").toString();
				subchargeCodeIds = JavaUtils.replaceStringWithDoubleQuotes(subchargeCodeIds);
				subchargeCodeIdsArray = subchargeCodeIds.split(",");
			}

			/*
			 * if(StringUtils.isNotBlank(payload.get("investigationType").toString())) {
			 * String investigationTypes = payload.get("investigationType").toString();
			 * investigationTypes =
			 * JavaUtils.replaceStringWithDoubleQuotes(investigationTypes);
			 * investigationTypesArray = investigationTypes.split(","); }
			 */

			if (payload.has("mInvestTypes")) {
				if (StringUtils.isNotBlank(payload.get("mInvestTypes").toString())) {
					String investigationTypes = payload.get("mInvestTypes").toString();
					investigationTypes = JavaUtils.replaceStringWithDoubleQuotes(investigationTypes);
					investigationTypesArray = investigationTypes.split(",");
				}
			}

			if (StringUtils.isNotBlank(payload.get("orderHdId").toString())) {
				String orderHdIds = payload.get("orderHdId").toString();
				orderHdIds = JavaUtils.replaceStringWithDoubleQuotes(orderHdIds);
				orderHdIdsArray = orderHdIds.split(",");
			}

			if (payload.has("result")) {
				if (StringUtils.isNotBlank(payload.get("result").toString())) {
					String results = payload.get("result").toString();
					results = JavaUtils.replaceStringWithDoubleQuotes(results);
					resultsArray = results.split(",");
				}
			} else if (payload.has("resultId")) {
				if (StringUtils.isNotBlank(payload.get("resultId").toString())) {
					String results = payload.get("resultId").toString();
					results = JavaUtils.replaceStringWithDoubleQuotes(results);
					resultsArray = results.split(",");
				}
			}

			String[] InvestigationIdValueParentv = null;
			if (payload.has("InvestigationIdValueParent")) {
				if (StringUtils.isNotBlank(payload.get("InvestigationIdValueParent").toString())) {
					String InvestigationIdValueParent = payload.get("InvestigationIdValueParent").toString();
					InvestigationIdValueParent = JavaUtils.replaceStringWithDoubleQuotes(InvestigationIdValueParent);
					InvestigationIdValueParentv = InvestigationIdValueParent.split(",");
				}
			}

			String[] normalRangeValue = null;
			if (payload.has("normalRange")) {
				if (StringUtils.isNotBlank(payload.get("normalRange").toString())) {
					String normalRange = payload.get("normalRange").toString();
					normalRange = JavaUtils.replaceStringWithDoubleQuotes(normalRange);
					normalRangeValue = normalRange.split(",");
				}
			}

			String[] fixedIdsValue = null;
			if (payload.has("fixedIds")) {
				if (StringUtils.isNotBlank(payload.get("fixedIds").toString())) {
					String fixedIds = payload.get("fixedIds").toString();
					fixedIds = JavaUtils.replaceStringWithDoubleQuotes(fixedIds);
					fixedIdsValue = fixedIds.split(",");
				}
			}

			String[] userIdValue = null;
			if (payload.has("userId")) {
				if (StringUtils.isNotBlank(payload.get("userId").toString())) {
					String userId = payload.get("userId").toString();
					userId = JavaUtils.replaceStringWithDoubleQuotes(userId);
					userIdValue = userId.split(",");
				}
			}

			String[] uomIdsValue = null;
			if (payload.has("uomIds")) {
				if (StringUtils.isNotBlank(payload.get("uomIds").toString())) {
					String uomIds = payload.get("uomIds").toString();
					uomIds = JavaUtils.replaceStringWithDoubleQuotes(uomIds);
					uomIdsValue = uomIds.split(",");
				}
			}

			String[] orderdtIdValue = null;
			if (payload.has("orderdtId")) {
				if (StringUtils.isNotBlank(payload.get("orderdtId").toString())) {
					String orderdtId = payload.get("orderdtId").toString();
					orderdtId = JavaUtils.replaceStringWithDoubleQuotes(orderdtId);
					orderdtIdValue = orderdtId.split(",");
				}
			}
			String[] dgOrderdtIdNewss = null;
			if (payload.has("dgOrderdtIdNew")) {
				if (StringUtils.isNotBlank(payload.get("dgOrderdtIdNew").toString())) {
					String dgOrderdtIdNews = payload.get("dgOrderdtIdNew").toString();
					dgOrderdtIdNews = JavaUtils.replaceStringWithDoubleQuotes(dgOrderdtIdNews);
					dgOrderdtIdNewss = dgOrderdtIdNews.split(",");
				}
			}

			String[] subchargeCodeIds = null;
			if (payload.has("subchargeCodeId")) {
				if (StringUtils.isNotBlank(payload.get("subchargeCodeId").toString())) {
					String subchargeCodeId = payload.get("subchargeCodeId").toString();
					subchargeCodeId = JavaUtils.replaceStringWithDoubleQuotes(subchargeCodeId);
					subchargeCodeIds = subchargeCodeId.split(",");
				}
			}

			String[] mainchargecodeIds = null;
			if (payload.has("mainchargecodeId")) {
				if (StringUtils.isNotBlank(payload.get("mainchargecodeId").toString())) {
					String mainchargecodeId = payload.get("mainchargecodeId").toString();
					mainchargecodeId = JavaUtils.replaceStringWithDoubleQuotes(mainchargecodeId);
					mainchargecodeIds = mainchargecodeId.split(",");
				}
			}

			String[] sInvestTypesArray = null;
			if (payload.has("sInvestTypes")) {
				if (StringUtils.isNotBlank(payload.get("sInvestTypes").toString())) {
					String singInvestTypes = payload.get("sInvestTypes").toString();
					singInvestTypes = JavaUtils.replaceStringWithDoubleQuotes(singInvestTypes);
					sInvestTypesArray = singInvestTypes.split(",");
				}
			}

			String[] mInvestTypesArray = null;
			if (payload.has("mInvestTypes")) {
				if (StringUtils.isNotBlank(payload.get("mInvestTypes").toString())) {
					String multiInvestTypes = payload.get("mInvestTypes").toString();
					multiInvestTypes = JavaUtils.replaceStringWithDoubleQuotes(multiInvestTypes);
					mInvestTypesArray = multiInvestTypes.split(",");
				}
			}

			String[] tInvestTypesArray = null;
			if (payload.has("tInvestTypes")) {
				if (StringUtils.isNotBlank(payload.get("tInvestTypes").toString())) {
					String tempInvestTypes = payload.get("tInvestTypes").toString();
					tempInvestTypes = JavaUtils.replaceStringWithDoubleQuotes(tempInvestTypes);
					tInvestTypesArray = tempInvestTypes.split(",");
				}
			}

			String[] templateResultArray = null;
			if (payload.has("resultInvs")) {
				if (StringUtils.isNotBlank(payload.get("resultInvs").toString())) {
					String tempResultInvs = payload.get("resultInvs").toString();
					tempResultInvs = JavaUtils.replaceStringWithDoubleQuotes(tempResultInvs);
					templateResultArray = tempResultInvs.split(",");
				}
			}

			String[] hospitalIdArray = null;
			if (payload.has("hospitalIdd")) {
				if (StringUtils.isNotBlank(payload.get("hospitalIdd").toString())) {
					String hospitalIds = payload.get("hospitalIdd").toString();
					hospitalIds = JavaUtils.replaceStringWithDoubleQuotes(hospitalIds);
					hospitalIdArray = hospitalIds.split(",");
				}
			}

			// validate result range

			List subInvestigationList = new ArrayList();
			for (int j = 0; j < subInvestigationIdsArray.length; j++) {
				String[] subInvestigationId = subInvestigationIdsArray[j].trim().split("##");
				subInvestigationList.add(subInvestigationId[0].toString());
			}
			String[] subInvestigationIdsArray1 = (String[]) subInvestigationList
					.toArray(new String[subInvestigationList.size()]);
			Map<Long, Object> resultRangeMap = labDao.validateResultEnteredRange(subInvestigationIdsArray1, resultValue,
					normalRangeValue);

			List<Long> resultHDList1 = new ArrayList<Long>();
			Integer counter = 1;
			Integer conunt = 1;
			String finalValue = "";
			Long orderHdId = null;
			HashMap<String, HashMap<String, String>> mapInfo = new HashMap<>();

			HashMap<String, String> mapSampleDetail = new HashMap<>();
			Set<Integer> uniqueInvestigation = new HashSet<>();
			for (int i = 0; i < subInvestigationIdsArray.length; i++) {
				String InvestigatonId = "";
				String subInvestigationId = "";
				String investigationType = "";
				if (StringUtils.isNotEmpty(diagNoValue[i].toString()) && !diagNoValue[i].equalsIgnoreCase("0")) {
					finalValue += diagNoValue[i].trim();

					if (i < subInvestigationIdsArray.length && !subInvestigationIdsArray[i].equalsIgnoreCase("0")
							&& StringUtils.isNotBlank(subInvestigationIdsArray[i])) {
						InvestigatonId = subInvestigationIdsArray[i].trim();
						String[] subInvestigationIdArr = InvestigatonId.split("##");
						subInvestigationId = subInvestigationIdArr[0].toString();
						investigationType = subInvestigationIdArr[1].toString();
						finalValue += "," + subInvestigationId + "&&&" + investigationType;
					} else {
						finalValue += "," + "0";
					}

					String sampleCollectionHd = "";
					if (i < sampleCollectionHdIdsArray.length && !sampleCollectionHdIdsArray[i].equalsIgnoreCase("0")
							&& StringUtils.isNotBlank(sampleCollectionHdIdsArray[i])) {
						String sampleCollectionHdIds = sampleCollectionHdIdsArray[i].trim();
						String[] sampleCollectionHdArray = sampleCollectionHdIds.split("##");
						sampleCollectionHd = sampleCollectionHdArray[0].toString();
						sampleCollectionHdArray[1].toString();
						finalValue += "," + sampleCollectionHd;
					} else {
						finalValue += "," + "0";
					}

					if (i < orderHdIdsArray.length && !orderHdIdsArray[i].equalsIgnoreCase("0")
							&& StringUtils.isNotBlank(orderHdIdsArray[i])) {
						finalValue += "," + orderHdIdsArray[i].trim();
						orderHdId = Long.parseLong(orderHdIdsArray[i].trim());
					} else {
						finalValue += "," + "0";
					}

					if (payload.has("mInvestTypes")) {
						if (i < investigationTypesArray.length && !investigationTypesArray[i].equalsIgnoreCase("0")
								&& StringUtils.isNotBlank(investigationTypesArray[i])) {
							finalValue += "," + investigationTypesArray[i].trim();
						} else {
							finalValue += "," + "0";
						}
					} else {
						finalValue += "," + "0";
					}

					if (i < subchargeCodeIdsArray.length && !subchargeCodeIdsArray[i].equalsIgnoreCase("0")
							&& StringUtils.isNotBlank(subchargeCodeIdsArray[i])) {
						finalValue += "," + subchargeCodeIdsArray[i].trim();
					} else {
						finalValue += "," + "0";
					}

					if (i < mainchargeCodeIdsArray.length && !mainchargeCodeIdsArray[i].equalsIgnoreCase("0")
							&& StringUtils.isNotBlank(mainchargeCodeIdsArray[i])) {
						finalValue += "," + mainchargeCodeIdsArray[i].trim();
					} else {
						finalValue += "," + "0";
					}

					if (i < remarkIdsArray.length && !remarkIdsArray[i].equalsIgnoreCase("0")
							&& StringUtils.isNotBlank(remarkIdsArray[i])) {
						finalValue += "," + remarkIdsArray[i].trim();
					} else {
						finalValue += "," + "0";
					}

					if (payload.has("normalIds")) {
						if (i < normalIdsArray.length && !normalIdsArray[i].equalsIgnoreCase("0")
								&& StringUtils.isNotBlank(normalIdsArray[i])) {
							finalValue += "," + normalIdsArray[i].trim();
						} else {
							finalValue += "," + "0";
						}
					} else {
						finalValue += "," + "0";
					}

					if (payload.has("fixedIds")) {
						if (i < fixedIdsArray.length && !fixedIdsArray[i].equalsIgnoreCase("0")
								&& StringUtils.isNotBlank(fixedIdsArray[i])) {
							finalValue += "," + fixedIdsArray[i].trim();
						} else {
							finalValue += "," + "0";
						}
					} else {
						finalValue += "," + "0";
					}

					if (i < samplesIdsArray.length && !samplesIdsArray[i].equalsIgnoreCase("0")
							&& StringUtils.isNotBlank(samplesIdsArray[i])) {
						finalValue += "," + samplesIdsArray[i].trim();
					} else {
						finalValue += "," + "0";
					}

					/*
					 * if (i<resultsIdsArray.length && !resultsIdsArray[i].equalsIgnoreCase("0") &&
					 * StringUtils.isNotBlank(resultsIdsArray[i])) { finalValue += "," +
					 * resultsIdsArray[i].trim(); } else { finalValue += "," + "0"; }
					 */

					if (payload.has("parentInvId")) {
						if (i < parentInvIdValue.length && !parentInvIdValue[0].equalsIgnoreCase("0")
								&& StringUtils.isNotBlank(parentInvIdValue[0])) {
							uniqueInvestigation.add(Integer.parseInt(parentInvIdValue[i].trim()));
							finalValue += "," + parentInvIdValue[i].trim();
						} else {
							finalValue += "," + "0";
						}
					} else {
						finalValue += "," + "0";
					}

					String sampleCollectionDtId = "";
					if (i < sampleCollectionDtIdsArray.length && !sampleCollectionDtIdsArray[i].equalsIgnoreCase("0")
							&& StringUtils.isNotBlank(sampleCollectionDtIdsArray[i])) {
						String sampleCollectionDtIds = sampleCollectionDtIdsArray[i].trim();
						String[] sampleCollectionDetalisId = sampleCollectionDtIds.split("##");
						sampleCollectionDtId = sampleCollectionDetalisId[0].toString();
						sampleCollectionDetalisId[1].toString();
						finalValue += "," + sampleCollectionDtId.trim();
					} else {
						finalValue += "," + "0";
					}

					if (i < deptId.length && !deptId[i].equalsIgnoreCase("0") && StringUtils.isNotBlank(deptId[i])) {
						finalValue += "," + deptId[i].trim();
					} else {
						finalValue += "," + "0";
					}

					if (i < patId.length && !patId[i].equalsIgnoreCase("0") && StringUtils.isNotBlank(patId[i])) {
						finalValue += "," + patId[i].trim();
					} else {
						finalValue += "," + "0";
					}

					if (i < resultsArray.length && !resultsArray[i].equalsIgnoreCase("0")
							&& StringUtils.isNotBlank(resultsArray[i])) {
						finalValue += "," + resultsArray[i].trim();
					} else {
						finalValue += "," + "0";
					}
					String parentInvestigationId = "";
					if (payload.has("InvestigationIdValueParent")) {
						if (i < InvestigationIdValueParentv.length
								&& !InvestigationIdValueParentv[i].equalsIgnoreCase("0")
								&& StringUtils.isNotBlank(InvestigationIdValueParentv[i])) {
							String InvestigationIdValueParentInv = InvestigationIdValueParentv[i].trim();
							String[] stringParenentInvestigation = InvestigationIdValueParentInv.split("##");
							parentInvestigationId = stringParenentInvestigation[0].toString();
							stringParenentInvestigation[1].toString();
							finalValue += "," + parentInvestigationId.trim();
						} else {
							finalValue += "," + "0";
						}
					} else {
						finalValue += "," + "0";
					}

					if (payload.has("normalRange")) {
						if (i < normalRangeValue.length && !normalRangeValue[i].equalsIgnoreCase("0")
								&& StringUtils.isNotBlank(normalRangeValue[i])) {
							finalValue += "," + normalRangeValue[i].trim();
						} else {
							finalValue += "," + "0";
						}
					} else {
						finalValue += "," + "0";
					}

					if (payload.has("fixedIds")) {
						if (i < fixedIdsValue.length && !fixedIdsValue[i].equalsIgnoreCase("0")
								&& StringUtils.isNotBlank(fixedIdsValue[i])) {
							finalValue += "," + fixedIdsValue[i].trim();
						} else {
							finalValue += "," + "0";
						}
					} else {
						finalValue += "," + "0";
					}

					if (payload.has("userId")) {
						if (i < userIdValue.length && !userIdValue[i].equalsIgnoreCase("0")
								&& StringUtils.isNotBlank(userIdValue[i])) {
							finalValue += "," + userIdValue[i].trim();
						} else {
							finalValue += "," + "0";
						}
					} else {
						finalValue += "," + "0";
					}

					if (payload.has("uomIds")) {
						if (i < uomIdsValue.length && !uomIdsValue[i].equalsIgnoreCase("0")
								&& StringUtils.isNotBlank(uomIdsValue[i])) {
							finalValue += "," + uomIdsValue[i].trim();
						} else {
							finalValue += "," + "0";
						}
					} else {
						finalValue += "," + "0";
					}

					if (payload.has("orderdtId")) {
						if (i < orderdtIdValue.length && !orderdtIdValue[i].equalsIgnoreCase("0")
								&& StringUtils.isNotBlank(orderdtIdValue[i])) {
							finalValue += "," + orderdtIdValue[i].trim();
						} else {
							finalValue += "," + "0";
						}
					} else {
						finalValue += "," + "0";
					}

					String investigationId = "";
					String investigationTypes = "";
					if (i < subInvestigationIdsArray.length && !subInvestigationIdsArray[i].equalsIgnoreCase("0")
							&& StringUtils.isNotBlank(subInvestigationIdsArray[i])) {
						String investigations = subInvestigationIdsArray[i].toString();
						String[] investigationArray = investigations.split("##");
						for (int j = 0; j < investigationArray.length; j++) {
							investigationId = investigationArray[0].toString();
							investigationTypes = investigationArray[1].toString();
						}
						if (resultRangeMap.containsKey(Long.parseLong(investigationId))) {
							String rangeVal = (String) resultRangeMap.get(Long.parseLong(investigationId));
							finalValue += "," + rangeVal;
						} else {
							finalValue += "," + "0";
						}

					} else {
						finalValue += "," + "0";
					}

					String dgOrderdtId = "";
					String dgOrderInvType = "";
					String dgOrederParentInvId = "";

					if (payload.has("dgOrderdtIdNew")) {
						if (i < dgOrderdtIdNewss.length && !dgOrderdtIdNewss[i].equalsIgnoreCase("0")
								&& StringUtils.isNotBlank(dgOrderdtIdNewss[i])) {
							String dgorderdt = dgOrderdtIdNewss[i].trim();
							String[] stringDgOrderdt = dgorderdt.split("##");
							dgOrderdtId = stringDgOrderdt[0];
							dgOrderInvType = stringDgOrderdt[1];
							dgOrederParentInvId = stringDgOrderdt[2];
							finalValue += "," + dgOrderdtId + "&&" + dgOrderInvType + "&&" + dgOrederParentInvId;
						} else {
							finalValue += "," + "0";
						}
					} else {
						finalValue += "," + "0";
					}

					if (payload.has("subchargeCodeId")) {
						if (i < subchargeCodeIds.length && !subchargeCodeIds[i].equalsIgnoreCase("0")
								&& StringUtils.isNotBlank(subchargeCodeIds[i])) {
							finalValue += "," + subchargeCodeIds[i].trim();
						} else {
							finalValue += "," + "0";
						}
					} else {
						finalValue += "," + "0";
					}

					if (payload.has("mainchargecodeId")) {
						if (i < mainchargecodeIds.length && !mainchargecodeIds[i].equalsIgnoreCase("0")
								&& StringUtils.isNotBlank(mainchargecodeIds[i])) {
							finalValue += "," + mainchargecodeIds[i].trim();
						} else {
							finalValue += "," + "0";
						}
					} else {
						finalValue += "," + "0";
					}

					if (payload.has("sInvestTypes")) {
						if (i < sInvestTypesArray.length && !sInvestTypesArray[i].equalsIgnoreCase("0")
								&& StringUtils.isNotBlank(sInvestTypesArray[i])) {
							finalValue += "," + sInvestTypesArray[i].trim();
						} else {
							finalValue += "," + "0";
						}
					} else {
						finalValue += "," + "0";
					}

					if (payload.has("mInvestTypes")) {
						if (i < mInvestTypesArray.length && !mInvestTypesArray[i].equalsIgnoreCase("0")
								&& StringUtils.isNotBlank(mInvestTypesArray[i])) {
							finalValue += "," + mInvestTypesArray[i].trim();
						} else {
							finalValue += "," + "0";
						}
					} else {
						finalValue += "," + "0";
					}

					if (payload.has("tInvestTypes")) {
						if (i < tInvestTypesArray.length && !tInvestTypesArray[i].equalsIgnoreCase("0")
								&& StringUtils.isNotBlank(tInvestTypesArray[i])) {
							finalValue += "," + tInvestTypesArray[i].trim();
						} else {
							finalValue += "," + "0";
						}
					} else {
						finalValue += "," + "0";
					}

					if (payload.has("resultInvs")) {
						if (i < templateResultArray.length && !templateResultArray[i].equalsIgnoreCase("0")
								&& StringUtils.isNotBlank(templateResultArray[i])) {
							finalValue += "," + templateResultArray[i].trim();
						} else {
							finalValue += "," + "0";
						}
					} else {
						finalValue += "," + "0";
					}

					if (payload.has("hospitalIdd")) {
						if (i < hospitalIdArray.length && !hospitalIdArray[i].equalsIgnoreCase("0")
								&& StringUtils.isNotBlank(hospitalIdArray[i])) {
							finalValue += "," + hospitalIdArray[i].trim();
						} else {
							finalValue += "," + "0";
						}
					} else {
						finalValue += "," + "0";
					}

					// put all child object
					mapSampleDetail.put(subInvestigationIdsArray[i].trim() + "@#" + counter, finalValue);

					finalValue = "";
					counter++;
				}
			}

			counter = 1;
			conunt = 1;
           long doctor_id=0;
			List<Long> dtIdList = new ArrayList<Long>();
			List<Object> invList = new ArrayList<Object>();
			List<DgSampleCollectionDt> dgSampCollDtlist = labDao.getDgSampCollDtIdFromDgSamCollDts(
					Long.parseLong(samplecollectionHdId), Long.parseLong(subchargeCodeIdsArray[0]), session);
			if (dgSampCollDtlist != null && !dgSampCollDtlist.isEmpty()) {
				for (DgSampleCollectionDt dt : dgSampCollDtlist) {
					invList.add(dt.getInvestigationId());
					dtIdList.add(dt.getOrderdtId());
					
					doctor_id= dt.getDgSampleCollectionHd().getDgOrderHd().getDoctorId();
					
				}
			}
			//System.out.println("subInvestigationIdsArray.length="+subInvestigationIdsArray.length);
			
			 
			for (int i = 0; i < subInvestigationIdsArray.length; i++) {

				if (mapSampleDetail.containsKey(subInvestigationIdsArray[i].trim() + "@#" + counter)) {
					String finalResultValue = mapSampleDetail.get(subInvestigationIdsArray[i].trim() + "@#" + counter);
					
					//System.out.println("finalResultValue="+finalResultValue);
					String[] values = finalResultValue.split(",");
					
					//System.out.println("result="+values[15]);
					
					
					//System.out.println(values[15]=="0");
					
					  if(!(values[15].equalsIgnoreCase("0")))
					  { 
						  visitId = labDao.getVisitIdFromDgSampleCollectionHeader(Long.parseLong(samplecollectionHdId),
									session);
							List<DgMasInvestigation> testOrderNo = labDao.getTestOrderNoFromDgMasInvestigation(invList,
									session);

							// dgResultEntryHd =
							// labDao.getDgResultEntryHd(Long.parseLong(sampleCollectionHdIdsArray[0]));
							// dgResultEntryHd = labDao.getResultEntryObject(Long.parseLong(values[16]),
							// orderHdId, session);

							dgResultEntryHd = labDao.getResultEntryObject(Long.parseLong(values[24]),
									Long.parseLong(values[25]), orderHdId, session);
							if (dgResultEntryHd != null) {
								resultEnterHdId = dgResultEntryHd.getResultEntryId();
							} else {
								dgResultEntryHd = new DgResultEntryHd();
								sampleCollectionHdId = Long.parseLong(values[2].toString());
								dgResultEntryHd.setSampleCollectionHeaderId(Long.parseLong(values[2].toString()));
								dgResultEntryHd.setResultNo(values[0]);
								Timestamp resultDate = new Timestamp(System.currentTimeMillis());
								dgResultEntryHd.setResultDate(resultDate);
								dgResultEntryHd.setVerified("V");
								dgResultEntryHd.setResultStatus("C");// P
								dgResultEntryHd.setCampId(ncampId);
								
								
								if (testOrderNo != null && testOrderNo.get(0).getTestOrderNo() != null
										&& !testOrderNo.isEmpty()) {
									dgResultEntryHd.setTestOrderNo(testOrderNo.get(0).getTestOrderNo());
								} else {
									dgResultEntryHd.setTestOrderNo(null);
								}
								if (values[30] != null && !values[30].equalsIgnoreCase("")) {
									dgResultEntryHd.setMmuId((Long.parseLong(values[30])));
								} else {
									dgResultEntryHd.setMmuId(null);
								}

								if (values[25] != null && !values[25].equalsIgnoreCase("0")) {
									dgResultEntryHd.setMainChargecodeId(Long.parseLong(values[25]));
								} else if (values[6] != null && !values[6].equalsIgnoreCase("0")) {
									dgResultEntryHd.setMainChargecodeId(Long.parseLong(values[6]));
								} else {
									dgResultEntryHd.setMainChargecodeId(null);
								}
								if (values[24] != null && !values[24].equalsIgnoreCase("0")) {
									dgResultEntryHd.setSubChargecodeId(Long.parseLong(values[24]));
								} else {
									dgResultEntryHd.setSubChargecodeId(null);
								}
								if (visitId != null) {
									dgResultEntryHd.setVisitId(visitId);
								} else {
									dgResultEntryHd.setVisitId(null);
								}
								/*
								 * if(values[16]!=null && !values[16].equalsIgnoreCase("0")) {
								 * dgResultEntryHd.setInvestigationId(Long.parseLong(values[16])); }else {
								 * dgResultEntryHd.setInvestigationId(null); }
								 */
								dgResultEntryHd.setOrderHdId(orderHdId);
								if (patId[0] != null && !patId[0].equalsIgnoreCase("0")) {
									dgResultEntryHd.setPatientId(Long.parseLong(patId[0].toString()));
								} else {
									dgResultEntryHd.setPatientId(null);
								}
								if (deptId[0] != null && !deptId[0].equalsIgnoreCase("0"))
									dgResultEntryHd.setDepartmentId(Long.parseLong(deptId[0]));

								Timestamp verifiedOn = new Timestamp(System.currentTimeMillis());
								dgResultEntryHd.setVerifiedOn(verifiedOn);
								if (payload.has("userId") && nUserId !=0) {
									dgResultEntryHd.setLastChgBy(nUserId);
									dgResultEntryHd.setCreatedBy(nUserId);
									dgResultEntryHd.setResultVerifiedBy(doctor_id);
									// dgResultEntryHd.setResultVerifiedBy(Long.parseLong(values[19]));
								} else {
									dgResultEntryHd.setLastChgBy(null);
									dgResultEntryHd.setCreatedBy(null);
									dgResultEntryHd.setResultVerifiedBy(null);
									// dgResultEntryHd.setResultVerifiedBy(null);
								}

								resultEnterHdId = labDao.submitDgResultEntry(dgResultEntryHd, session);
								// resultHDList1.add(resultEnterHdId);
							}
							resultHDList1.add(resultEnterHdId);

							// insert into details
							dgResultEntryDt = new DgResultEntryDt();
							dgResultEntryDt.setResultEntryId(resultEnterHdId);
							Timestamp lastchgdt = new Timestamp(System.currentTimeMillis());
							dgResultEntryDt.setLastChgDate(lastchgdt);
							if (!values[16].equalsIgnoreCase("0")) {
								dgResultEntryDt.setInvestigationId(Long.parseLong(values[16]));
							}
							dgResultEntryDt.setSampleCollectionDetailsId(Long.parseLong(values[12]));

							if (!values[1].equalsIgnoreCase("0")) {
								String[] investIdVal = values[1].split("&&&");
								if (investIdVal[1].equalsIgnoreCase("m")) {
									dgResultEntryDt.setSubInvestigationId(Long.parseLong(investIdVal[0]));
									dgResultEntryDt.setResultType(investIdVal[1]);

									if (values[15] != null && !values[15].equalsIgnoreCase("0") && values[15].contains("@@")) {
										String[] resulttt = values[15].split("@@");
										if (resulttt[1] != null && resulttt[1] != "")
											dgResultEntryDt.setResult(resulttt[1]);
									} else if (values[15] != null && !values[15].equalsIgnoreCase("0")
											&& !values[15].contains("@@")) {
										dgResultEntryDt.setResult(values[15]);
									} else {
										dgResultEntryDt.setResult("");
									}

								} else if (investIdVal[1].equalsIgnoreCase("s")) {
									dgResultEntryDt.setInvestigationId(Long.parseLong(investIdVal[0]));
									dgResultEntryDt.setResultType(investIdVal[1]);
									if (values[15] != null && !values[15].equalsIgnoreCase("0")) {
										dgResultEntryDt.setResult(values[15]);
									} else {
										dgResultEntryDt.setResult("");
									}

								} else if (investIdVal[1].equalsIgnoreCase("t")) {
									dgResultEntryDt.setInvestigationId(Long.parseLong(investIdVal[0]));
									dgResultEntryDt.setResultType(investIdVal[1]);
									if (values[15] != null && !values[15].equalsIgnoreCase("0")) {
										dgResultEntryDt.setResult(values[15]);
									} else {
										dgResultEntryDt.setResult("");
									}

								}

							} else {
								dgResultEntryDt.setSubInvestigationId(null);
							}
							if (!values[6].equalsIgnoreCase("0")) {
								dgResultEntryDt.setChargeCodeId(null);
							} else {
								dgResultEntryDt.setChargeCodeId(null);
							}
							if (values[9] != null && !values[9].equalsIgnoreCase("0")) {
								dgResultEntryDt.setUomId(Long.parseLong(values[9]));
							} else if (values[20] != null && !values[20].equalsIgnoreCase("0")) {
								dgResultEntryDt.setUomId(Long.parseLong(values[20]));
							} else {
								dgResultEntryDt.setUomId(null);
							}

							if (values[10] != null && !values[10].equalsIgnoreCase("0")) {
								dgResultEntryDt.setSampleId(Long.parseLong(values[10]));
							} else {
								dgResultEntryDt.setSampleId(null);
							}

							dgResultEntryDt.setResultDetailStatus("E");// P

							if (!values[7].equalsIgnoreCase("0")) {
								dgResultEntryDt.setRemarks(values[7]);
							} else {
								dgResultEntryDt.setRemarks("");
							}
							dgResultEntryDt.setValidated("V");
							dgResultEntryDt.setResultDetailStatus("C");
							
							/*
							 * if(values[15]!=null && !values[15].equalsIgnoreCase("0") &&
							 * values[15].contains("@@")) { String [] resulttt=values[15].split("@@");
							 * if(resulttt[1]!=null && resulttt[1]!="")
							 * dgResultEntryDt.setResult(resulttt[1]); }
							 * 
							 * else if(values[15]!=null && !values[15].equalsIgnoreCase("0")) {
							 * 
							 * dgResultEntryDt.setResult(values[15]); } else {
							 * dgResultEntryDt.setResult(""); }
							 */

							if (values[17] != null && !values[17].equalsIgnoreCase("0")) {
								dgResultEntryDt.setRangeValue(values[17]);
							} else {
								dgResultEntryDt.setRangeValue("");
							}

							if (values[21] != null && !values[21].equalsIgnoreCase("0")) {
								dgResultEntryDt.setOrderDtId(Long.parseLong(values[21]));
							} else {
								dgResultEntryDt.setOrderDtId(null);
							}

							if (values[22] != null && !values[22].equalsIgnoreCase("0")) {
								dgResultEntryDt.setRangeStatus(values[22]);
							} else {
								dgResultEntryDt.setRangeStatus("");
							}

							if (values[23] != null && !values[23].equalsIgnoreCase("0")) {
								String[] dgOrderdtValue = values[23].split("&&");
								if (dgOrderdtValue[1].equalsIgnoreCase("m")) {
									dgResultEntryDt.setOrderDtId(Long.parseLong(dgOrderdtValue[0]));

								} else if (dgOrderdtValue[1].equalsIgnoreCase("s")) {
									dgResultEntryDt.setOrderDtId(Long.parseLong(dgOrderdtValue[0]));

								} else if (dgOrderdtValue[1].equalsIgnoreCase("t")) {
									dgResultEntryDt.setOrderDtId(Long.parseLong(dgOrderdtValue[0]));
								}

							} else {
								dgResultEntryDt.setOrderDtId(null);
							}

							/*
							 * if(values[29]!=null && !values[29].equalsIgnoreCase("0")) {
							 * if(values[28].equalsIgnoreCase("t")) { dgResultEntryDt.setResult(values[29]);
							 * }else { dgResultEntryDt.setResult(null); }
							 * 
							 * }else { dgResultEntryDt.setResult(null); }
							 */

							resultEnterdtId = labDao.submitDgResultEntryDt(dgResultEntryDt, session);
							resultEnterdtId1.add(resultEnterdtId);
					  }
					 

					

				}

				counter += 1;
			}

			// tx.commit();

			// update into tables
			 
			if (resultEnterdtId != null) {
				List<Long> dgSampleCollectionDtIDs = new ArrayList<Long>();
				List<Object[]> dgsamplecolldtIds = labDao.getDgSamCollDtIdFromDgResultEntDts(resultHDList1, session);
				for (Object[] rows : dgsamplecolldtIds) {
					dgSampleCollectionDtIDs.add(Long.parseLong(rows[0].toString()));
				}

				// update dgsamplecollection details
				boolean updateflag = labDao.updateDgSampleCollectionDtForResultEntry(dgSampleCollectionDtIDs, session);
				// get headerId from dgsamplecollectionhd

				List<Long> hdIdList = new ArrayList<Long>();
				List<Object[]> hdIdObj = labDao.getHeadeIdFrmDgSampleCollectionDt(dgSampleCollectionDtIDs, session);
				for (Object[] rows : hdIdObj) {
					hdIdList.add(Long.parseLong(rows[0].toString()));
				}

				List<DgSampleCollectionDt> samplecollDtList = labDao.getDgSampleCollectiondtIdForUpdates(hdIdList,
						session);

				if (updateflag == true) {
					Integer count = 0;
					if (samplecollDtList != null)
						for (int k = 0; k < samplecollDtList.size(); k++) {
							if (samplecollDtList.get(k).getOrderStatus().equalsIgnoreCase("C")) {
								count += 1;
							}
						}
					if (count == 0) {
						boolean uDgSampleCollDtOrderHdStatus = labDao.updateOrderStatusDgSampleCollectionHd(hdIdList,
								session);
						if (uDgSampleCollDtOrderHdStatus == true) {
							json.put("msg", "Sample Collection Header Status has been updated");
							json.put("status", "uDgOrderHdStatus");
						}
					} else {
						// error
						json.put("msg", "Result Updated in Sample Collection Details");
						json.put("status", 0);
					}
				}
			} else {
				json.put("msg", "Record not submitted");
				json.put("status", 0);
			}

			tx.commit();
			json.put("msg", "Record Successfully submitted");
			json.put("status", 1);
		} catch (Exception e) {
			json.put("status", 0);
			json.put("msg", "Record not submitted");
			tx.rollback();
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return json.toString();
	}

	/*************************************************************
	 * Result Validation
	 *******************************************************/
	@SuppressWarnings("rawtypes")
	@Override
	public String getResultValidationWaitingListGrid(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		String resulttime = "";
		JSONObject json = new JSONObject();
		List listObj = new ArrayList();
		if (jsonObject != null) {
			/*
			 * List<DgResultEntryHd> dgResultEntryHdList =
			 * labDao.getResultValidationWaitingListGrid(jsonObject);
			 */

			Map<String, Object> responseData = labDao.getResultValidationWaitingListGrid(jsonObject);
			List<DgResultEntryHd> dgResultEntryHdList = (List<DgResultEntryHd>) responseData.get("list");
			int count = (int) responseData.get("count");

			if (dgResultEntryHdList != null && !dgResultEntryHdList.isEmpty()) {
				for (DgResultEntryHd hd : dgResultEntryHdList) {
					Map<String, Object> map = new HashMap<String, Object>();

					String resultDate = HMSUtil.convertDateToStringFormat(hd.getResultDate(), "dd/MM/yyyy");
					if (resultDate != null) {
						map.put("resultDate", resultDate);
					} else {
						map.put("resultDate", "");
					}
					if (hd.getResultDate() != null) {
						resulttime = JavaUtils.getTimeFromDateAndTime(hd.getResultDate().toString());
					}

					if (resulttime != null) {
						map.put("resulttime", resulttime);
					} else {
						map.put("resulttime", "");
					}

					if (hd.getPatient() != null && hd.getPatient().getPatientName() != null) {
						map.put("patientName", hd.getPatient().getPatientName());
					} else {
						map.put("patient", "");
					}

					/*
					 * if(hd.getPatient()!=null && hd.getPatient().getMasRelation()!=null &&
					 * hd.getPatient().getMasRelation().getRelationId()!=null) {
					 * map.put("relationId", hd.getPatient().getMasRelation().getRelationId());
					 * }else { map.put("relationId", ""); }
					 */

					/*
					 * if(hd.getPatient()!=null && hd.getPatient().getMasRelation()!=null &&
					 * hd.getPatient().getMasRelation().getRelationName()!=null) {
					 * map.put("relationName", hd.getPatient().getMasRelation().getRelationName());
					 * }else { map.put("relationName", ""); }
					 * 
					 * if(hd.getPatient()!=null && hd.getPatient().getServiceNo()!=null) {
					 * map.put("serviceNo", hd.getPatient().getServiceNo()); }else {
					 * map.put("serviceNo", ""); }
					 */

					if (hd.getDgSampleCollectionHd() != null && hd.getDgSampleCollectionHd().getVisitId() != null) {
						map.put("visitId", hd.getDgSampleCollectionHd().getVisitId());
					} else {
						map.put("visitId", "");
					}

					if (hd.getDepartmentId() != null) {
						map.put("departmentId", hd.getDepartmentId());
					} else {
						map.put("departmentId", "");
					}

					if (hd.getDgSampleCollectionHd() != null && hd.getDgSampleCollectionHd().getDgOrderHd() != null
							&& hd.getDgSampleCollectionHd().getDgOrderHd().getMasDepartment() != null
							&& hd.getDgSampleCollectionHd().getDgOrderHd().getMasDepartment()
									.getDepartmentName() != null) {
						map.put("departmentName",
								hd.getDgSampleCollectionHd().getDgOrderHd().getMasDepartment().getDepartmentName());
					} else {
						map.put("departmentName", "");
					}

					if (hd.getResultEntryId() != null) {
						map.put("resultEntHdId", hd.getResultEntryId());
					} else {
						map.put("resultEntHdId", "");
					}

					if (hd.getSampleCollectionHeaderId() != null) {
						map.put("sampleCollectionHdId", hd.getSampleCollectionHeaderId());
					} else {
						map.put("sampleCollectionHdId", "");
					}

					if (hd.getPatient() != null && hd.getPatient().getPatientId() != null) {
						map.put("patientId", hd.getPatient().getPatientId());
					} else {
						map.put("patientId", "");
					}
					if (hd.getPatient() != null && hd.getPatient().getMobileNumber() != null) {
						map.put("mobileNumber", hd.getPatient().getMobileNumber());
					} else {
						map.put("mobileNumber", "");
					}
					if (hd.getSubChargecodeId() != null) {
						map.put("chargeCodeId", hd.getSubChargecodeId());
					} else {
						map.put("chargeCodeId", "");
					}
					/*
					 * if(hd.getDgMasInvestigation()!=null &&
					 * hd.getDgMasInvestigation().getMasSubChargecode()!=null &&
					 * hd.getMasSubChargecode().getSubChargecodeName()!=null) {
					 * map.put("subChargecodeName",
					 * hd.getMasSubChargecode().getSubChargecodeName()); }else {
					 * map.put("subChargecodeName", ""); }
					 */

					if (hd.getMasSubChargecode() != null && hd.getMasSubChargecode().getSubChargecodeName() != null) {
						map.put("subChargecodeName", hd.getMasSubChargecode().getSubChargecodeName());
					} else {
						map.put("subChargecodeName", "");
					}

					if (hd.getResultStatus() != null) {
						map.put("resultDetailStatus", hd.getResultStatus());
					} else {
						map.put("resultDetailStatus", "");
					}

					if (hd.getDgSampleCollectionHd() != null && hd.getDgSampleCollectionHd().getOrderId() != null) {
						map.put("diagNo", hd.getDgSampleCollectionHd().getOrderId());
					} else {
						map.put("diagNo", "");
					}

					listObj.add(map);
				}
				if (listObj != null && !listObj.isEmpty()) {
					json.put("data", listObj);
					json.put("count", count);
					json.put("status", 1);
					json.put("msg", "Record Fetch Successfully");
				} else {
					json.put("data", listObj);
					json.put("count", 0);
					json.put("status", 0);
					json.put("msg", "Record Fetch Successfully");
				}
			} else {
				json.put("msg", "No Record Found");
				json.put("status", 0);
				json.put("count", 0);
			}

		}
		return json.toString();
	}

	@Override
	public String getResultValidationWaitingList(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();

		return json.toString();
	}

	//// @ result validation details start

	@SuppressWarnings("unchecked")
	@Override
	public String getResultValidationDetails(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		// resultEntryDetailId
		JSONObject json = new JSONObject();
		List list = new ArrayList();
		List<DgSampleCollectionDt> dgSampCollDtsInvList = null;
		List<DgFixedValue> fixedValueList = null;
		Map<String, Object> mapFixedValue = null;
		List<DgNormalValue> normalValList = null;
		List globalListResultValidation = new ArrayList();

		if (jsonObject != null) {
			Long resultEntryHdId = Long.parseLong(jsonObject.get("resultEntryHdId").toString());
			Map<String, Object> mapObject = labDao.getResultValidationDetails(resultEntryHdId);

			if (mapObject != null && mapObject.size() > 0) {
				List<DgResultEntryHd> dgResultEntryHds = (List<DgResultEntryHd>) mapObject.get("dgResultEntryHdList");
				List<DgResultEntryDt> dgResultEntryDts = (List<DgResultEntryDt>) mapObject
						.get("dgResultEntryDtInvList");
				List<DgMasInvestigation> parInvestigationList = (List<DgMasInvestigation>) mapObject
						.get("parentInvestigationList");
				List<DgResultEntryDt> singleParameterList = (List<DgResultEntryDt>) mapObject
						.get("singleParameterList");
				List<DgResultEntryDt> templateParameterList = (List<DgResultEntryDt>) mapObject
						.get("templateParameterList");

				if (CollectionUtils.isNotEmpty(dgResultEntryHds) && dgResultEntryHds != null) {
					for (DgResultEntryHd hd : dgResultEntryHds) {
						Map<String, Object> mapDetails = new HashMap<String, Object>();
						if (hd.getResultEntryId() != null) {
							mapDetails.put("resultEntryHdId", hd.getResultEntryId());
						} else {
							mapDetails.put("resultEntryHdId", "");
						}

						if (hd.getDepartmentId() != null) {
							mapDetails.put("departmentId", hd.getDepartmentId());
						} else {
							mapDetails.put("departmentId", "");
						}

						if (hd.getVisit() != null && hd.getVisit().getMasDepartment() != null
								&& hd.getVisit().getMasDepartment().getDepartmentName() != null) {
							mapDetails.put("departmentName", hd.getVisit().getMasDepartment().getDepartmentName());
						} else {
							mapDetails.put("departmentName", "");
						}

						if (hd.getResultDate() != null) {
							mapDetails.put("resultDate",
									HMSUtil.convertDateToStringFormat(hd.getResultDate(), "dd/MM/yyyy"));
						} else {
							mapDetails.put("resultDate", "");
						}

						if (hd.getResultDate() != null) {
							mapDetails.put("resultTime",
									JavaUtils.getTimeFromDateAndTime(hd.getResultDate().toString()));
						} else {
							mapDetails.put("resultTime", "");
						}

						if (hd.getPatientId() != null) {
							mapDetails.put("patientId", hd.getPatientId());
						} else {
							mapDetails.put("patientId", "");
						}
						if (hd.getPatient() != null && hd.getPatient().getPatientName() != null) {
							mapDetails.put("patientName", hd.getPatient().getPatientName());
						} else {
							mapDetails.put("patientName", "");
						}
						/*
						 * if (hd.getPatient() != null && hd.getPatient().getMasrelation() != null &&
						 * hd.getPatient().getMasrelation().getRelationId() != null) {
						 * mapDetails.put("relationId",
						 * hd.getPatient().getMasrelation().getRelationId()); } else {
						 * mapDetails.put("relationId", ""); }
						 * 
						 * if (hd.getPatient() != null && hd.getPatient().getMasrelation() != null &&
						 * hd.getPatient().getMasrelation().getRelationName() != null) {
						 * mapDetails.put("relationName",
						 * hd.getPatient().getMasrelation().getRelationName()); } else {
						 * mapDetails.put("relationName", ""); }
						 * 
						 * if (hd.getPatient() != null && hd.getPatient().getEmployeeName() != null) {
						 * mapDetails.put("employeeName", hd.getPatient().getEmployeeName()); } else {
						 * mapDetails.put("employeeName", ""); }
						 */

						if (hd.getPatient().getDateOfBirth() != null) {
							Date dateOfBirth = hd.getPatient().getDateOfBirth();
							// String age = JavaUtils.calculateAgefromDob(dateOfBirth);
							Date visitDate = null;
							if (hd.getVisit() != null && hd.getVisit().getVisitDate() != null) {
								visitDate = hd.getVisit().getVisitDate();
							}
							if (visitDate != null && dateOfBirth != null) {
								mapDetails.put("age", HMSUtil.getDateBetweenTwoDate(visitDate, dateOfBirth));
							} else {
								mapDetails.put("age", "");
							}
							// mapDetails.put("age", age);
						} else {
							mapDetails.put("age", "");
						}

						if (hd.getPatient() != null && hd.getPatient().getMasAdministrativeSex() != null
								&& hd.getPatient().getMasAdministrativeSex().getAdministrativeSexName() != null) {
							mapDetails.put("gender",
									hd.getPatient().getMasAdministrativeSex().getAdministrativeSexName());
						} else {
							mapDetails.put("gender", "");
						}

						/*
						 * if (hd.getPatient() != null && hd.getPatient().getServiceNo() != null) {
						 * mapDetails.put("serviceNo", hd.getPatient().getServiceNo()); } else {
						 * mapDetails.put("serviceNo", ""); }
						 */

						if (hd.getVisitId() != null) {
							mapDetails.put("visitId", hd.getVisitId());
						} else {
							mapDetails.put("visitId", "");
						}

						if (hd.getDgSampleCollectionHd() != null && hd.getDgSampleCollectionHd().getDgOrderHd() != null
								&& hd.getDgSampleCollectionHd().getDgOrderHd().getUsers().getLoginName() != null) {
							mapDetails.put("validatedBy",
									hd.getDgSampleCollectionHd().getDgOrderHd().getUsers().getLoginName());
						} else {
							mapDetails.put("validatedBy", "");
						}

						if (hd.getDgSampleCollectionHd() != null && hd.getDgSampleCollectionHd().getDgOrderHd() != null
								&& hd.getDgSampleCollectionHd().getDgOrderHd().getUsers().getLoginName() != null) {
							mapDetails.put("resultEnteredBy",
									hd.getDgSampleCollectionHd().getDgOrderHd().getUsers().getLoginName());
						} else {
							mapDetails.put("resultEnteredBy", "");
						}

						if (hd.getSampleCollectionHeaderId() != null) {
							mapDetails.put("sampleCollectionHdId", hd.getSampleCollectionHeaderId());
						} else {
							mapDetails.put("sampleCollectionHdId", "");
						}

						if (hd.getOrderHdId() != null) {
							mapDetails.put("orderHdId", hd.getOrderHdId());
						} else {
							mapDetails.put("orderHdId", "");
						}

						/* SAMPLE DETAILS */
						LocalDate currentDate1 = LocalDate.now();
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
						String currentDate = formatter.format(currentDate1);
						String currTime = LocalTime.now().toString();
						String[] currTime1 = currTime.split("\\.");
						String currentTime = currTime1[0];

						mapDetails.put("currentDate", currentDate);
						mapDetails.put("currentTime", currentTime);

						list.add(mapDetails);
					}
				}

				List investigationDetailList = new ArrayList();
				if (CollectionUtils.isNotEmpty(dgResultEntryDts) && dgResultEntryDts != null) {
					for (DgResultEntryDt dgResultEntryDt : dgResultEntryDts) {
						Map<String, Object> mapInvestigationDetails = new HashMap<String, Object>();
						if (dgResultEntryDt.getInvestigationId() != null) {
							mapInvestigationDetails.put("investigationId", dgResultEntryDt.getInvestigationId());
						} else {
							mapInvestigationDetails.put("investigationId", "");
						}

						if (dgResultEntryDt.getSubInvestigationId() != null) {
							mapInvestigationDetails.put("subInvestigationId", dgResultEntryDt.getSubInvestigationId());
						} else {
							mapInvestigationDetails.put("subInvestigationId", "");
						}

						if (dgResultEntryDt.getDgMasInvestigation() != null
								&& dgResultEntryDt.getDgMasInvestigation().getInvestigationName() != null) {
							mapInvestigationDetails.put("investigationName",
									dgResultEntryDt.getDgMasInvestigation().getInvestigationName());
						} else {
							mapInvestigationDetails.put("investigationName", "");
						}

						if (dgResultEntryDt.getDgSubMasInvestigation() != null
								&& dgResultEntryDt.getDgSubMasInvestigation().getSubInvestigationName() != null) {
							mapInvestigationDetails.put("subInvestigationName",
									dgResultEntryDt.getDgSubMasInvestigation().getSubInvestigationName());
						} else {
							mapInvestigationDetails.put("subInvestigationName", "");
						}

						if (dgResultEntryDt.getDgMasInvestigation() != null
								&& dgResultEntryDt.getDgMasInvestigation().getMasSample() != null
								&& dgResultEntryDt.getDgMasInvestigation().getMasSample().getSampleId() != null) {
							mapInvestigationDetails.put("sampleId",
									dgResultEntryDt.getDgMasInvestigation().getMasSample().getSampleId());
						} else {
							mapInvestigationDetails.put("sampleId", "");
						}

						if (dgResultEntryDt.getDgMasInvestigation() != null
								&& dgResultEntryDt.getDgMasInvestigation().getMasSample() != null && dgResultEntryDt
										.getDgMasInvestigation().getMasSample().getSampleDescription() != null) {
							mapInvestigationDetails.put("sampleDescription",
									dgResultEntryDt.getDgMasInvestigation().getMasSample().getSampleDescription());
						} else {
							mapInvestigationDetails.put("sampleDescription", "");
						}

						if (dgResultEntryDt.getDgMasInvestigation() != null
								&& dgResultEntryDt.getDgMasInvestigation().getMasUOM() != null
								&& dgResultEntryDt.getDgMasInvestigation().getMasUOM().getUOMId() != null) {
							mapInvestigationDetails.put("uomId",
									dgResultEntryDt.getDgMasInvestigation().getMasUOM().getUOMId());
						} else {
							mapInvestigationDetails.put("uomId", "");
						}

						if (dgResultEntryDt.getDgMasInvestigation() != null
								&& dgResultEntryDt.getDgMasInvestigation().getMasUOM() != null
								&& dgResultEntryDt.getDgMasInvestigation().getMasUOM().getUOMName() != null) {
							mapInvestigationDetails.put("uomName",
									dgResultEntryDt.getDgMasInvestigation().getMasUOM().getUOMName());
						} else {
							mapInvestigationDetails.put("uomName", "");
						}

						if (dgResultEntryDt.getResult() != null) {
							mapInvestigationDetails.put("result", dgResultEntryDt.getResult());
						} else {
							mapInvestigationDetails.put("result", "");
						}

						if (dgResultEntryDt.getResult() != null) {
							mapInvestigationDetails.put("range", dgResultEntryDt.getRangeValue());
						} else {
							mapInvestigationDetails.put("range", "");
						}

						if (dgResultEntryDt.getResult() != null) {
							mapInvestigationDetails.put("rangeStatus", dgResultEntryDt.getRangeStatus());
						} else {
							mapInvestigationDetails.put("rangeStatus", "");
						}
						if (dgResultEntryDt.getResultEntryId() != null) {
							mapInvestigationDetails.put("resultEntryHdId", dgResultEntryDt.getResultEntryId());
						} else {
							mapInvestigationDetails.put("resultEntryHdId", "");
						}

						if (dgResultEntryDt.getResultEntryDetailId() != null) {
							mapInvestigationDetails.put("resultEntryDtId", dgResultEntryDt.getResultEntryDetailId());
						} else {
							mapInvestigationDetails.put("resultEntryDtId", "");
						}
						if (dgResultEntryDt.getResultType() != null) {
							mapInvestigationDetails.put("resultType", dgResultEntryDt.getResultType());
						} else {
							mapInvestigationDetails.put("resultType", "");
						}

						investigationDetailList.add(mapInvestigationDetails);
					}

				}

				List subInvestigationList = new ArrayList();
				if (CollectionUtils.isNotEmpty(dgResultEntryDts) && dgResultEntryDts != null) {
					for (DgResultEntryDt dgResultEntryDt : dgResultEntryDts) {
						Map<String, Object> mapSubInvestigationDetails = new HashMap<String, Object>();
						if (dgResultEntryDt.getResultType().equalsIgnoreCase("m")) {

							mapSubInvestigationDetails.put("subInvestigationId",
									dgResultEntryDt.getDgSubMasInvestigation().getSubInvestigationId());
							mapSubInvestigationDetails.put("subInvestigationName",
									dgResultEntryDt.getDgSubMasInvestigation().getSubInvestigationName());
							mapSubInvestigationDetails.put("investigtionType",
									dgResultEntryDt.getDgMasInvestigation().getInvestigationType());
							mapSubInvestigationDetails.put("investigatonId",
									dgResultEntryDt.getDgMasInvestigation().getInvestigationId());
							mapSubInvestigationDetails.put("investigatonName",
									dgResultEntryDt.getDgMasInvestigation().getInvestigationName());
							mapSubInvestigationDetails.put("subchargeCodeId",
									dgResultEntryDt.getDgSubMasInvestigation().getSubChargecodeId());

							mapSubInvestigationDetails.put("result",
									dgResultEntryDt.getResult() != null ? dgResultEntryDt.getResult() : "");
							mapSubInvestigationDetails.put("resultType",
									dgResultEntryDt.getResultType() != null ? dgResultEntryDt.getResultType() : "");

							mapSubInvestigationDetails.put("rangeValue",
									dgResultEntryDt.getRangeValue() != null ? dgResultEntryDt.getRangeValue() : "");
							mapSubInvestigationDetails.put("rangeStatus",
									dgResultEntryDt.getRangeStatus() != null ? dgResultEntryDt.getRangeStatus() : "");
							mapSubInvestigationDetails.put("uomId",
									dgResultEntryDt.getDgSubMasInvestigation() != null
											&& dgResultEntryDt.getDgSubMasInvestigation().getMasUOM() != null
											&& dgResultEntryDt.getDgSubMasInvestigation().getMasUOM().getUOMId() != null
													? dgResultEntryDt.getDgSubMasInvestigation().getMasUOM().getUOMId()
													: "");
							mapSubInvestigationDetails.put("uomName",
									dgResultEntryDt.getDgSubMasInvestigation().getMasUOM() != null && dgResultEntryDt
											.getDgSubMasInvestigation().getMasUOM().getUOMName() != null
													? dgResultEntryDt.getDgSubMasInvestigation().getMasUOM()
															.getUOMName()
													: "");

							mapSubInvestigationDetails.put("remarks",
									dgResultEntryDt.getRemarks() != null ? dgResultEntryDt.getRemarks() : "");
							mapSubInvestigationDetails.put("sampleDescription",
									dgResultEntryDt.getDgMasInvestigation() != null
											&& dgResultEntryDt.getDgMasInvestigation().getMasSample() != null
											&& dgResultEntryDt.getDgMasInvestigation().getMasSample()
													.getSampleDescription() != null
															? dgResultEntryDt.getDgMasInvestigation().getMasSample()
																	.getSampleDescription()
															: "");
							mapSubInvestigationDetails.put("sampleId", dgResultEntryDt.getDgMasInvestigation() != null
									&& dgResultEntryDt.getDgMasInvestigation().getMasSample() != null
									&& dgResultEntryDt.getDgMasInvestigation().getMasSample().getSampleId() != null
											? dgResultEntryDt.getDgMasInvestigation().getMasSample().getSampleId()
											: "");

							mapSubInvestigationDetails.put("resultEntryDtId",
									dgResultEntryDt.getResultEntryDetailId() != null
											? dgResultEntryDt.getResultEntryDetailId()
											: "");
							mapSubInvestigationDetails.put("resultEntryHdId",
									dgResultEntryDt.getResultEntryId() != null ? dgResultEntryDt.getResultEntryId()
											: "");

							subInvestigationList.add(mapSubInvestigationDetails);
						}
					}
				}
				List parentInvestigationList = new ArrayList();
				if (CollectionUtils.isNotEmpty(parInvestigationList) && parInvestigationList != null) {
					for (DgMasInvestigation dgMasInvestigation : parInvestigationList) {
						Map<Object, Object> mapSubInvestigationParent = new HashMap<Object, Object>();

						mapSubInvestigationParent.put("investigtionId",
								dgMasInvestigation.getInvestigationId() != null
										? dgMasInvestigation.getInvestigationId()
										: "");
						mapSubInvestigationParent.put("investigtionName",
								dgMasInvestigation.getInvestigationName() != null
										? dgMasInvestigation.getInvestigationName()
										: "");
						mapSubInvestigationParent.put("investigtionType",
								dgMasInvestigation.getInvestigationType() != null
										? dgMasInvestigation.getInvestigationType()
										: "");

						parentInvestigationList.add(mapSubInvestigationParent);
					}
				}
				// single parameter
				List templateParaList = new ArrayList();
				List singleParaList = new ArrayList();
				if (CollectionUtils.isNotEmpty(singleParameterList) && singleParameterList != null) {
					for (DgResultEntryDt dgResultEntryDt : singleParameterList) {
						Map<String, Object> mapSinglePara = new HashMap<String, Object>();
						mapSinglePara.put("investigationId",
								dgResultEntryDt.getInvestigationId() != null ? dgResultEntryDt.getInvestigationId()
										: "");
						mapSinglePara.put("subInvestigationId",
								dgResultEntryDt.getSubInvestigationId() != null
										? dgResultEntryDt.getSubInvestigationId()
										: "");
						mapSinglePara.put("investigationName",
								dgResultEntryDt.getDgMasInvestigation() != null
										&& dgResultEntryDt.getDgMasInvestigation().getInvestigationName() != null
												? dgResultEntryDt.getDgMasInvestigation().getInvestigationName()
												: "");
						mapSinglePara.put("subInvestigationName",
								dgResultEntryDt.getDgSubMasInvestigation() != null
										&& dgResultEntryDt.getDgSubMasInvestigation().getSubInvestigationName() != null
												? dgResultEntryDt.getDgSubMasInvestigation().getSubInvestigationName()
												: "");
						mapSinglePara.put("sampleId",
								dgResultEntryDt.getDgMasInvestigation() != null
										&& dgResultEntryDt.getDgMasInvestigation().getMasSample() != null
										&& dgResultEntryDt.getDgMasInvestigation().getMasSample().getSampleId() != null
												? dgResultEntryDt.getDgMasInvestigation().getMasSample().getSampleId()
												: "");

						mapSinglePara.put("sampleDescription", dgResultEntryDt.getDgMasInvestigation() != null
								&& dgResultEntryDt.getDgMasInvestigation().getMasSample() != null
								&& dgResultEntryDt.getDgMasInvestigation().getMasSample().getSampleDescription() != null
										? dgResultEntryDt.getDgMasInvestigation().getMasSample().getSampleDescription()
										: "");

						mapSinglePara.put("uomId",
								dgResultEntryDt.getDgMasInvestigation() != null
										&& dgResultEntryDt.getDgMasInvestigation().getMasUOM() != null
										&& dgResultEntryDt.getDgMasInvestigation().getMasUOM().getUOMId() != null
												? dgResultEntryDt.getDgMasInvestigation().getMasUOM().getUOMId()
												: "");

						mapSinglePara.put("uomName",
								dgResultEntryDt.getDgMasInvestigation() != null
										&& dgResultEntryDt.getDgMasInvestigation().getMasUOM() != null
										&& dgResultEntryDt.getDgMasInvestigation().getMasUOM().getUOMName() != null
												? dgResultEntryDt.getDgMasInvestigation().getMasUOM().getUOMName()
												: "");
						mapSinglePara.put("result",
								dgResultEntryDt.getResult() != null ? dgResultEntryDt.getResult() : "");
						mapSinglePara.put("rangeValue",
								dgResultEntryDt.getRangeValue() != null ? dgResultEntryDt.getRangeValue() : "");
						mapSinglePara.put("range",
								dgResultEntryDt.getResult() != null ? dgResultEntryDt.getRangeValue() : "");
						mapSinglePara.put("rangeStatus",
								dgResultEntryDt.getRangeStatus() != null ? dgResultEntryDt.getRangeStatus() : "");
						mapSinglePara.put("resultType",
								dgResultEntryDt.getResultType() != null ? dgResultEntryDt.getResultType() : "");
						mapSinglePara.put("remarks",
								dgResultEntryDt.getRemarks() != null ? dgResultEntryDt.getRemarks() : "");

						mapSinglePara.put("resultEntryHdId",
								dgResultEntryDt.getResultEntryId() != null ? dgResultEntryDt.getResultEntryId() : "");
						mapSinglePara.put("resultEntryDtId",
								dgResultEntryDt.getResultEntryDetailId() != null
										? dgResultEntryDt.getResultEntryDetailId()
										: "");

						singleParaList.add(mapSinglePara);
					}
				}

				if (CollectionUtils.isNotEmpty(templateParameterList) && templateParameterList != null) {
					for (DgResultEntryDt dgResultEntryDt : templateParameterList) {
						Map<String, Object> mapTemplatePara = new HashMap<String, Object>();

						mapTemplatePara.put("investigationId",
								dgResultEntryDt.getInvestigationId() != null ? dgResultEntryDt.getInvestigationId()
										: "");
						mapTemplatePara.put("subInvestigationId",
								dgResultEntryDt.getSubInvestigationId() != null
										? dgResultEntryDt.getSubInvestigationId()
										: "");
						mapTemplatePara.put("investigationName",
								dgResultEntryDt.getDgMasInvestigation() != null
										&& dgResultEntryDt.getDgMasInvestigation().getInvestigationName() != null
												? dgResultEntryDt.getDgMasInvestigation().getInvestigationName()
												: "");
						mapTemplatePara.put("subInvestigationName",
								dgResultEntryDt.getDgSubMasInvestigation() != null
										&& dgResultEntryDt.getDgSubMasInvestigation().getSubInvestigationName() != null
												? dgResultEntryDt.getDgSubMasInvestigation().getSubInvestigationName()
												: "");
						mapTemplatePara.put("sampleId",
								dgResultEntryDt.getDgMasInvestigation() != null
										&& dgResultEntryDt.getDgMasInvestigation().getMasSample() != null
										&& dgResultEntryDt.getDgMasInvestigation().getMasSample().getSampleId() != null
												? dgResultEntryDt.getDgMasInvestigation().getMasSample().getSampleId()
												: "");

						mapTemplatePara.put("sampleDescription", dgResultEntryDt.getDgMasInvestigation() != null
								&& dgResultEntryDt.getDgMasInvestigation().getMasSample() != null
								&& dgResultEntryDt.getDgMasInvestigation().getMasSample().getSampleDescription() != null
										? dgResultEntryDt.getDgMasInvestigation().getMasSample().getSampleDescription()
										: "");

						mapTemplatePara.put("uomId",
								dgResultEntryDt.getDgMasInvestigation() != null
										&& dgResultEntryDt.getDgMasInvestigation().getMasUOM() != null
										&& dgResultEntryDt.getDgMasInvestigation().getMasUOM().getUOMId() != null
												? dgResultEntryDt.getDgMasInvestigation().getMasUOM().getUOMId()
												: "");

						mapTemplatePara.put("uomName",
								dgResultEntryDt.getDgMasInvestigation() != null
										&& dgResultEntryDt.getDgMasInvestigation().getMasUOM() != null
										&& dgResultEntryDt.getDgMasInvestigation().getMasUOM().getUOMName() != null
												? dgResultEntryDt.getDgMasInvestigation().getMasUOM().getUOMName()
												: "");
						mapTemplatePara.put("result",
								dgResultEntryDt.getResult() != null ? dgResultEntryDt.getResult() : "");
						mapTemplatePara.put("range",
								dgResultEntryDt.getResult() != null ? dgResultEntryDt.getRangeValue() : "");
						mapTemplatePara.put("rangeStatus",
								dgResultEntryDt.getResult() != null ? dgResultEntryDt.getRangeStatus() : "");

						mapTemplatePara.put("resultType",
								dgResultEntryDt.getResultType() != null ? dgResultEntryDt.getResultType() : "");
						mapTemplatePara.put("rangeValue",
								dgResultEntryDt.getRangeValue() != null ? dgResultEntryDt.getRangeValue() : "");
						mapTemplatePara.put("remarks",
								dgResultEntryDt.getRemarks() != null ? dgResultEntryDt.getRemarks() : "");

						mapTemplatePara.put("resultEntryHdId",
								dgResultEntryDt.getResultEntryId() != null ? dgResultEntryDt.getResultEntryId() : "");
						mapTemplatePara.put("resultEntryDtId",
								dgResultEntryDt.getResultEntryDetailId() != null
										? dgResultEntryDt.getResultEntryDetailId()
										: "");

						templateParaList.add(mapTemplatePara);
					}
				}
				// template parameter

				Map<String, Object> globalMap = new HashMap<String, Object>();
				// globalListResultValidation

				globalMap.put("data", list);
				globalMap.put("investigationDetailList", investigationDetailList);
				globalMap.put("subInvestigationList", subInvestigationList);
				globalMap.put("parentInvestigationList", parentInvestigationList);
				globalMap.put("singleParameter", singleParaList);
				globalMap.put("templateParameter", templateParaList);
				globalMap.put("count", list.size());
				globalMap.put("msg", "Record Fetch Successfully");
				globalMap.put("status", 1);

				globalListResultValidation.add(globalMap);

				if (globalListResultValidation != null && !globalListResultValidation.isEmpty()) {
					json.put("globalListResultValidation", globalListResultValidation);
				}

			} else {
				json.put("msg", "No Record Found");
				json.put("status", 0);
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}
		return json.toString();
	}

	//// @ result validation details end.

	@Override
	public String submitResultValidationDetails(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsonObject != null) {

			String[] checkboxValuesArray = null;
			String[] subInvestigationIdArray = null;
			String[] resultsValueArray = null;

			if (jsonObject.has("checkedChkBoxArray")) {
				String checkboxValues = jsonObject.get("checkedChkBoxArray").toString();
				checkboxValues = JavaUtils.replaceStringWithDoubleQuotes(checkboxValues);
				checkboxValuesArray = checkboxValues.split(",");
			}

			if (jsonObject.has("subInvestigationIdsArray")) {
				String subInvestigationIds = jsonObject.get("subInvestigationIdsArray").toString();
				subInvestigationIds = JavaUtils.replaceStringWithDoubleQuotes(subInvestigationIds);
				subInvestigationIdArray = subInvestigationIds.split(",");
			}

			if (jsonObject.has("resultsArray")) {
				String results = jsonObject.get("resultsArray").toString();
				results = JavaUtils.replaceStringWithDoubleQuotes(results);
				resultsValueArray = results.split(",");
			}

			String[] normalRangeArrayValues = null;
			if (jsonObject.has("normalIdsArray")) {
				String normalIds = jsonObject.get("normalIdsArray").toString();
				normalIds = JavaUtils.replaceStringWithDoubleQuotes(normalIds);
				normalRangeArrayValues = normalIds.split(",");
			}

			String[] resultHdIdsArrayValues = null;
			if (jsonObject.has("resultHdIdsArray")) {
				String resultHdIds = jsonObject.get("resultHdIdsArray").toString();
				resultHdIds = JavaUtils.replaceStringWithDoubleQuotes(resultHdIds);
				resultHdIdsArrayValues = resultHdIds.split(",");
			}

			//
			String[] uomIdsArrayValues = null;
			if (jsonObject.has("uomIdsArray")) {
				String uomIds = jsonObject.get("uomIdsArray").toString();
				uomIds = JavaUtils.replaceStringWithDoubleQuotes(uomIds);
				uomIdsArrayValues = uomIds.split(",");
			}

			String[] SampleIdArrayValues = null;
			if (jsonObject.has("SampleIdArray")) {
				String SampleIds = jsonObject.get("SampleIdArray").toString();
				SampleIds = JavaUtils.replaceStringWithDoubleQuotes(SampleIds);
				SampleIdArrayValues = SampleIds.split(",");
			}

			String[] remarksArrayValues = null;
			if (jsonObject.has("remarksArray")) {
				String remarks = jsonObject.get("remarksArray").toString();
				remarks = JavaUtils.replaceStringWithDoubleQuotes(remarks);
				remarksArrayValues = remarks.split(",");
			}

			Long resultEntryHdId = null;
			String[] headerIdArray = null;
			if (jsonObject.has("resulEntHdId")) {
				String resulEntHdId = jsonObject.get("resulEntHdId").toString();
				resulEntHdId = JavaUtils.replaceStringWithDoubleQuotes(resulEntHdId);
				headerIdArray = resulEntHdId.split(",");
				resultEntryHdId = Long.parseLong(headerIdArray[0].toString());
			}

			Long userIdVal = null;
			String[] userIdArray = null;
			if (jsonObject.has("userId")) {
				String userId = jsonObject.get("userId").toString();
				userId = JavaUtils.replaceStringWithDoubleQuotes(userId);
				userIdArray = userId.split(",");
				userIdVal = Long.parseLong(userIdArray[0].toString());
			}

			String[] parentInvIdsArrayValues = null;
			if (jsonObject.has("parentInvIdsArray")) {
				String parentInvIds = jsonObject.get("parentInvIdsArray").toString();
				parentInvIds = JavaUtils.replaceStringWithDoubleQuotes(parentInvIds);
				parentInvIdsArrayValues = parentInvIds.split(",");
			}

			String[] resultEntDtIdsArrayValues = null;
			if (jsonObject.has("resultDtIdsArray")) {
				String resultDtIds = jsonObject.get("resultDtIdsArray").toString();
				resultDtIds = JavaUtils.replaceStringWithDoubleQuotes(resultDtIds);
				resultEntDtIdsArrayValues = resultDtIds.split(",");
			}

			String[] rangeStatusArrayValues = null;
			if (jsonObject.has("rangeStatusArray")) {
				String rangeStatus = jsonObject.get("rangeStatusArray").toString();
				rangeStatus = JavaUtils.replaceStringWithDoubleQuotes(rangeStatus);
				rangeStatusArrayValues = rangeStatus.split(",");
			}

			List<Long> subInvIds = null;
			List<String> remarksList = new ArrayList<String>();

			if (checkboxValuesArray.length >= 0) {
				subInvIds = new ArrayList<Long>();
				for (int i = 0; i < checkboxValuesArray.length; i++) {

					if (i < subInvestigationIdArray.length) {
						subInvIds.add(Long.parseLong(subInvestigationIdArray[i].toString()));

					}

				}
			}
			List<Long> dtIds = null;
			List<DgResultEntryDt> resultEntrydtList = null;
			if (subInvIds == null) {
				dtIds = new ArrayList<Long>();
				if (jsonObject.has("InvestigationIdValueParent")) {
					String InvestigationIdValueParentf = jsonObject.get("InvestigationIdValueParent").toString();
					InvestigationIdValueParentf = JavaUtils.replaceStringWithDoubleQuotes(InvestigationIdValueParentf);
					String[] InvestigationIdValueParentfArray = InvestigationIdValueParentf.split(",");
					for (int i = 0; i < InvestigationIdValueParentfArray.length; i++) {

						if (i < InvestigationIdValueParentfArray.length) {
							dtIds.add(Long.parseLong(InvestigationIdValueParentfArray[i].toString()));

						}

					}
				}

			}
			resultEntrydtList = labDao.getAllResultEntDtId(resultEntryHdId, parentInvIdsArrayValues);
			List<Long> dgResultId = new ArrayList<>();
			for (DgResultEntryDt dt : resultEntrydtList) {
				dgResultId.add(dt.getResultEntryDetailId());
			}
			boolean flag = labDao.submitResultValidationDetails(resultEntDtIdsArrayValues, remarksArrayValues,
					resultsValueArray, rangeStatusArrayValues, normalRangeArrayValues, subInvestigationIdArray,
					parentInvIdsArrayValues);

			List<DgResultEntryDt> dgResultEntryDtIds = labDao.getResultEntDtIds(resultEntryHdId);

			if (flag == true) {
				Integer count = 0;
				for (int i = 0; i < dgResultEntryDtIds.size(); i++) {
					if (dgResultEntryDtIds.get(i).getResultDetailStatus().equalsIgnoreCase("E")) {
						count += 1;
					}
				}
				if (count == 0) {
					boolean upResulEntHd = labDao.updateResultEntryHd(resultEntryHdId, userIdVal);
					if (upResulEntHd == true) {
						json.put("msg", "ResulEntryHd Status has been updated");
						json.put("status", "uDgOrderHdStatus");
					}
				}
			} else {

				json.put("status", 0);
				json.put("msg", "Not Updated");
				json.put("error", "Error in Updated");
			}
		} else {
			json.put("msg", "Invalid Input");
			json.put("status", 0);
		}

		return json.toString();
	}

	/**********************************
	 * LabHistory
	 *********************************************/

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public String getPatientList(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List list = new ArrayList();
		List<Patient> pList = labDao.getPatientList(jsonObject);
		if (pList != null && pList.size() > 0) {
			for (Patient p : pList) {
				Map<String, Object> map = new HashMap<String, Object>();
				if (p.getPatientId() != null) {
					map.put("patientId", p.getPatientId());
				} else {
					map.put("patientId", "");
				}

				if (p.getPatientName() != null) {
					map.put("patientName", p.getPatientName());
				} else {
					map.put("patientName", "");
				}

				list.add(map);
			}
			json.put("msg", "Record Fetch Successfully");
			json.put("status", 1);
			json.put("data", list);
		} else {
			json.put("status", 0);
			json.put("msg", "No Record Found");
			json.put("data", list);

		}

		return json.toString();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public String getLabHistory(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List list = new ArrayList();
		Map<String, Object> map = labDao.getLabHistory(jsonObject);
		String status = (String) map.get("status");

		if (status.equalsIgnoreCase("success")) {
			if (map.get("dtsList") != null) {
				List<DgResultEntryDt> dtList = (List<DgResultEntryDt>) map.get("dtsList");
				int count = (int) map.get("count");
				if (dtList != null && dtList.size() > 0) {

					dtList.forEach(dt -> {

						if (dt.getResult() != null && StringUtils.isNotEmpty(dt.getResult())) {

							Map<String, Object> mapobj = new HashMap<String, Object>();

							if (dt.getDgResultEntryHd().getResultEntryId() != null) {
								mapobj.put("resultEntHdId", dt.getDgResultEntryHd().getResultEntryId());
							} else {
								mapobj.put("resultEntHdId", "");
							}

							if (dt.getResultEntryDetailId() != null) {
								mapobj.put("resultEntDtId", dt.getResultEntryDetailId());
							} else {
								mapobj.put("resultEntDtId", "");
							}

							if (dt.getDgResultEntryHd() != null && dt.getDgResultEntryHd().getOrderHdId() != null) {
								mapobj.put("orderHdId", dt.getDgResultEntryHd().getOrderHdId());
							} else {
								mapobj.put("orderHdId", "");
							}

							if (dt.getOrderDtId() != null) {
								mapobj.put("orderDtId", dt.getOrderDtId());
							} else {
								mapobj.put("orderDtId", "");
							}

							if (dt.getDgResultEntryHd() != null && dt.getDgResultEntryHd().getPatientId() != null) {
								mapobj.put("patientId", dt.getDgResultEntryHd().getPatientId());
							} else {
								mapobj.put("patientId", "");
							}

							if (dt.getDgResultEntryHd() != null && dt.getDgResultEntryHd().getPatient() != null
									&& dt.getDgResultEntryHd().getPatient().getPatientName() != null) {
								mapobj.put("patientName", dt.getDgResultEntryHd().getPatient().getPatientName());
							} else {
								mapobj.put("patientName", "");
							}

							/*
							 * if (dt.getDgResultEntryHd() != null && dt.getDgResultEntryHd().getPatient()
							 * != null && dt.getDgResultEntryHd().getPatient().getRelationId() != null) {
							 * mapobj.put("relationId",
							 * dt.getDgResultEntryHd().getPatient().getRelationId()); } else {
							 * mapobj.put("relationId", ""); }
							 */

							/*
							 * if (dt.getDgResultEntryHd() != null && dt.getDgResultEntryHd().getPatient()
							 * != null && dt.getDgResultEntryHd().getPatient().getMasRelation() != null &&
							 * dt.getDgResultEntryHd().getPatient().getMasRelation() .getRelationName() !=
							 * null) { mapobj.put("relationName",
							 * dt.getDgResultEntryHd().getPatient().getMasRelation().getRelationName()); }
							 * else { mapobj.put("relationName", ""); }
							 */

							if (dt.getDgResultEntryHd() != null && dt.getDgResultEntryHd().getPatient() != null
									&& dt.getDgResultEntryHd().getPatient().getDateOfBirth() != null) {
								// mapobj.put("age",JavaUtils.calculateAgefromDob(dt.getDgResultEntryHd().getPatient().getDateOfBirth()));
								Date date = dt.getDgResultEntryHd().getPatient().getDateOfBirth();
								Date visitDate = null;
								if (dt.getDgResultEntryHd().getVisit() != null
										&& dt.getDgResultEntryHd().getVisit().getVisitDate() != null) {
									visitDate = dt.getDgResultEntryHd().getVisit().getVisitDate();
								}
								if (visitDate != null && date != null) {
									mapobj.put("age", HMSUtil.getDateBetweenTwoDate(visitDate, date));
								} else {
									mapobj.put("age", "");
								}

							} else {
								mapobj.put("age", "");
							}

							if (dt.getDgResultEntryHd() != null && dt.getDgResultEntryHd().getPatient() != null
									&& dt.getDgResultEntryHd().getPatient().getAdministrativeSexId() != null) {
								mapobj.put("genderId", dt.getDgResultEntryHd().getPatient().getAdministrativeSexId());
							} else {
								mapobj.put("genderId", "");
							}

							if (dt.getDgResultEntryHd() != null && dt.getDgResultEntryHd().getPatient() != null
									&& dt.getDgResultEntryHd().getPatient().getMasAdministrativeSex() != null
									&& dt.getDgResultEntryHd().getPatient().getMasAdministrativeSex()
											.getAdministrativeSexName() != null) {
								mapobj.put("genderName", dt.getDgResultEntryHd().getPatient().getMasAdministrativeSex()
										.getAdministrativeSexName());
							} else {
								mapobj.put("genderName", "");
							}

							if (dt.getDgResultEntryHd() != null && dt.getDgResultEntryHd().getPatient() != null
									&& dt.getDgResultEntryHd().getPatient().getMasAdministrativeSex() != null
									&& dt.getDgResultEntryHd().getPatient().getMasAdministrativeSex()
											.getAdministrativeSexCode() != null) {
								mapobj.put("genderCode", dt.getDgResultEntryHd().getPatient().getMasAdministrativeSex()
										.getAdministrativeSexCode());
							} else {
								mapobj.put("genderCode", "");
							}

							if (dt.getDgResultEntryHd() != null && dt.getDgResultEntryHd().getMasMmu() != null) {
								mapobj.put("hospitalId", dt.getDgResultEntryHd().getMasMmu().getMmuId());
							} else {
								mapobj.put("hospitalId", "");
							}

							if (dt.getDgResultEntryHd() != null && dt.getDgResultEntryHd().getMasMmu() != null
									&& dt.getDgResultEntryHd().getMasMmu() != null) {
								mapobj.put("hospitalName", dt.getDgResultEntryHd().getMasMmu().getMmuName());
							} else {
								mapobj.put("hospitalName", "");
							}

							if (dt.getDgResultEntryHd() != null && dt.getDgResultEntryHd().getResultDate() != null) {
								mapobj.put("investigationDate", HMSUtil.convertDateToStringFormat(
										dt.getDgResultEntryHd().getResultDate(), "dd/MM/yyyy"));
							} else {
								mapobj.put("investigationDate", "");
							}

							if (dt.getInvestigationId() != null) {
								mapobj.put("investigationId", dt.getInvestigationId());
							} else {
								mapobj.put("investigationId", "");
							}

							if (dt.getDgMasInvestigation() != null
									&& dt.getDgMasInvestigation().getInvestigationName() != null) {
								mapobj.put("investigationName", dt.getDgMasInvestigation().getInvestigationName());
							} else {
								mapobj.put("investigationName", "");
							}

							if (dt.getDgSubMasInvestigation() != null
									&& dt.getDgSubMasInvestigation().getSubInvestigationName() != null) {
								mapobj.put("subInvestigationName",
										dt.getDgSubMasInvestigation().getSubInvestigationName());
							} else {
								mapobj.put("subInvestigationName", "");
							}

							if (dt.getMasUOM() != null && dt.getMasUOM().getUOMName() != null) {
								mapobj.put("unit", dt.getMasUOM().getUOMName());
							} else {
								mapobj.put("unit", "");
							}

							if (dt.getResult() != null && dt.getResult() != "") {
								mapobj.put("investigationResult", dt.getResult());
							} else {
								mapobj.put("investigationResult", "");
							}

							if (dt.getLastChgDate() != null) {
								mapobj.put("lastChgDate",
										HMSUtil.convertDateToStringFormat(dt.getLastChgDate(), "dd/MM/yyyy"));
							} else {
								mapobj.put("lastChgDate", "");
							}

							if (dt.getRangeValue() != null && dt.getRangeValue() != "") {
								mapobj.put("range", dt.getRangeValue());
							} else {
								mapobj.put("range", "");
							}

							/*
							 * if (dt.getDgResultEntryHd() != null && dt.getDgResultEntryHd().getPatient()
							 * != null && dt.getDgResultEntryHd().getPatient().getServiceNo() != null) {
							 * mapobj.put("serviceNo", dt.getDgResultEntryHd().getPatient().getServiceNo());
							 * } else { mapobj.put("serviceNo", ""); }
							 */

							if (dt.getRidcId() != null) {
								mapobj.put("ridcId", dt.getRidcId());
							} else {
								mapobj.put("ridcId", "");
							}

							if (dt.getDgResultEntryHd() != null && dt.getDgResultEntryHd().getResultVerified() != null
									&& dt.getDgResultEntryHd().getResultVerified().getUserName() != null) {
								mapobj.put("resultVerifiedBy",
										dt.getDgResultEntryHd().getResultVerified().getUserName());
							} else {
								mapobj.put("resultVerifiedBy", "");
							}

							if (dt.getDgResultEntryHd() != null && dt.getDgResultEntryHd().getUsers() != null
									&& dt.getDgResultEntryHd().getUsers().getUserName() != null) {
								mapobj.put("createdBy", dt.getDgResultEntryHd().getUsers().getUserName());
							} else {
								mapobj.put("createdBy", "");
							}

							if (dt.getResultDetailStatus() != null) {
								mapobj.put("resultDetailStatus", dt.getResultDetailStatus());
							} else {
								mapobj.put("resultDetailStatus", "");
							}

							if (dt.getValidated() != null) {
								mapobj.put("validated", dt.getValidated());
							} else {
								mapobj.put("validated", "");
							}

							if (dt.getResultType() != null) {
								mapobj.put("resultType", dt.getResultType());
							} else {
								mapobj.put("resultType", "");
							}

							if (dt.getRangeStatus() != null) {
								mapobj.put("rangeStatus", dt.getRangeStatus());
							} else {
								mapobj.put("rangeStatus", "");
							}
							list.add(mapobj);

						}

					});

					json.put("msg", "Record Fetch Successfully");
					json.put("data", list);
					json.put("count", count);
					json.put("status", "success");
				} else {
					json.put("msg", "No Record Found");
					json.put("data", list);
					json.put("count", 0);

				}

			} else {
				json.put("status", "error");
				json.put("msg", "No Record Found");
				json.put("data", list);
				json.put("count", 0);
			}
		} else if (status.equalsIgnoreCase("errorInServiceNo")) {
			json.put("status", "errorInServiceNo");
			json.put("msg", "No Record Found");
			json.put("data", list);
			json.put("count", 0);
		} else if (status.equalsIgnoreCase("failure")) {
			json.put("status", "failure");
			json.put("data", list);
			json.put("count", 0);
		}

		return json.toString();
	}

	/***********************************************
	 * OutSide Patient Waiting List
	 ********************************************/
	@Override
	public String getOutSidePatientWaitingList(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<DgOrderhd> dgorhdList = null;
		List list = new ArrayList();
		String patientId = "";
		String serviceno = "";
		String patientname = "";
		Long relationId = null;
		String labMark = "";
		String headerId = "";
		Date orderdate;
		String orderdate1 = "";
		String empname = "";
		String departmentName = "";
		String relationName = "";
		String mobileNumber = "";
		Date dateOfBirth;
		String age = "";
		String gender = "";
		Long departmentId = null;
		Long orderNo = null;
		int count = 0;
		try {

			Map<String, Object> map = labDao.getOutSidePatientWaitingList(jsonObject);
			if (map.get("dgOrderhdsList") != null) {
				dgorhdList = (List<DgOrderhd>) map.get("dgOrderhdsList");
				count = (int) map.get("count");
			}

			if (CollectionUtils.isNotEmpty(dgorhdList)) {
				for (Object row : dgorhdList) {
					Map<String, Object> mapdata = new LinkedHashMap<String, Object>();

					Object[] rows = (Object[]) row;
					if (rows[0] != null) {
						patientId = rows[0].toString();
					}
					if (rows[1] != null) {
						serviceno = rows[1].toString();
					}
					if (rows[2] != null) {
						patientname = rows[2].toString();
					}
					if (rows[3] != null) {
						relationId = Long.parseLong(rows[3].toString());
					}
					if (rows[4] != null) {
						labMark = rows[4].toString();
					}
					if (rows[5] != null) {
						headerId = rows[5].toString();
					}
					if (rows[6] != null) {
						orderdate = (Date) rows[6];
						orderdate1 = HMSUtil.convertDateToStringFormat(orderdate, "dd/MM/yyyy");
					}
					if (rows[7] != null) {
						empname = rows[7].toString();
					}
					if (rows[8] != null) {
						departmentName = rows[8].toString();
					}

					if (rows[9] != null) {
						mobileNumber = rows[9].toString();
					}
					if (rows[10] != null) {
						dateOfBirth = (Date) rows[10];
						age = JavaUtils.calculateAgefromDob(dateOfBirth);
					}
					if (rows[11] != null) {
						gender = rows[11].toString();
					}

					if (rows[12] != null) {
						orderNo = Long.parseLong(rows[12].toString());
					}
					if (rows[13] != null) {
						relationName = rows[13].toString();
					}

					mapdata.put("patientId", patientId);
					mapdata.put("serviceno", serviceno);
					mapdata.put("patientname", patientname);
					mapdata.put("labMark", labMark);
					mapdata.put("headerId", headerId);
					mapdata.put("orderdate", orderdate1);
					mapdata.put("empname", empname);
					mapdata.put("departmentName", departmentName);
					mapdata.put("departmentId", departmentId);
					mapdata.put("relationName", relationName);
					mapdata.put("mobileNumber", mobileNumber);
					mapdata.put("age", age);
					mapdata.put("gender", gender);
					mapdata.put("orderNo", orderNo);

					list.add(mapdata);

				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("status", 1);
					json.put("count", count);
					json.put("msg", "Record Found Successfully");
				} else {
					json.put("data", list);
					json.put("status", "0");
					json.put("count", 0);
					json.put("msg", "Record Not Found");
				}
			} else if (CollectionUtils.isEmpty(dgorhdList)) {
				json.put("status", 0);
				json.put("count", list.size());
				json.put("msg", "No Record Found");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return json.toString();
	}

	/**
	 * @Description: get the out side patient details
	 */
	@Override
	public String getOutSidePatientDetails(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List list = new ArrayList();
		List<DgOrderhd> hdList = labDao.getOutSidePatientDetails(jsonObject);
		Map<String, Object> map = labDao.getOutSidePatientInvestigationDetails(jsonObject);

		if (CollectionUtils.isNotEmpty(hdList)) {
			hdList.forEach(hd -> {
				Map<String, Object> mData = new HashMap<String, Object>();
				mData.put("orderHdId", hd.getOrderhdId() != null ? hd.getOrderhdId() : "");
				mData.put("patientId", hd.getPatientId() != null ? hd.getPatientId() : "");
				mData.put("patientName",
						hd.getPatient() != null && hd.getPatient().getPatientName() != null
								? hd.getPatient().getPatientName()
								: "");
				mData.put("hospitalId", hd.getMmuId() != null ? hd.getMmuId() : "");
				/*
				 * mData.put("serviceNo", hd.getPatient() != null &&
				 * hd.getPatient().getServiceNo() != null ? hd.getPatient().getServiceNo() :
				 * "");
				 */
				mData.put("patientId", hd.getPatientId() != null ? hd.getPatientId() : "");
				mData.put("patientId", hd.getPatientId() != null ? hd.getPatientId() : "");

				list.add(mData);
			});
			json.put("status", 0);
			json.put("msg", "Record found successfully");
			json.put("data", list);
		} else {
			json.put("status", 0);
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}

		return json.toString();
	}

	/**
	 * @Description: Result Entry Update
	 */
	@Override
	public String getResultUpdateWaitingList(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		String resulttime = "";
		List listObj = new ArrayList();
		Map<String, Object> responseData = labDao.getResultUpdateWaitingList(jsonObject);
		List<DgResultEntryHd> dgResultEntryHdList = (List<DgResultEntryHd>) responseData.get("list");
		int count = (int) responseData.get("count");

		if (dgResultEntryHdList != null && !dgResultEntryHdList.isEmpty()) {
			for (DgResultEntryHd hd : dgResultEntryHdList) {
				Map<String, Object> map = new HashMap<String, Object>();

				String resultDate = HMSUtil.convertDateToStringFormat(hd.getResultDate(), "dd/MM/yyyy");
				if (resultDate != null) {
					map.put("resultDate", resultDate);
				} else {
					map.put("resultDate", "");
				}
				if (hd.getResultDate() != null) {
					resulttime = JavaUtils.getTimeFromDateAndTime(hd.getResultDate().toString());
				}

				if (resulttime != null) {
					map.put("resulttime", resulttime);
				} else {
					map.put("resulttime", "");
				}

				if (hd.getPatient() != null && hd.getPatient().getPatientName() != null) {
					map.put("patientName", hd.getPatient().getPatientName());
				} else {
					map.put("patient", "");
				}

				/*
				 * if (hd.getPatient() != null && hd.getPatient().getMasRelation() != null &&
				 * hd.getPatient().getMasRelation().getRelationId() != null) {
				 * map.put("relationId", hd.getPatient().getMasRelation().getRelationId()); }
				 * else { map.put("relationId", ""); }
				 * 
				 * if (hd.getPatient() != null && hd.getPatient().getMasRelation() != null &&
				 * hd.getPatient().getMasRelation().getRelationName() != null) {
				 * map.put("relationName", hd.getPatient().getMasRelation().getRelationName());
				 * } else { map.put("relationName", ""); }
				 * 
				 * if (hd.getPatient().getServiceNo() != null) { map.put("serviceNo",
				 * hd.getPatient().getServiceNo()); } else { map.put("serviceNo", ""); }
				 */

				if (hd.getVisitId() != null) {
					map.put("visitId", hd.getVisitId());
				} else {
					map.put("visitId", "");
				}

				if (hd.getDepartmentId() != null) {
					map.put("departmentId", hd.getDepartmentId());
				} else {
					map.put("departmentId", "");
				}

				if (hd.getDgSampleCollectionHd() != null && hd.getDgSampleCollectionHd().getDgOrderHd() != null
						&& hd.getDgSampleCollectionHd().getDgOrderHd().getMasDepartment() != null
						&& hd.getDgSampleCollectionHd().getDgOrderHd().getMasDepartment().getDepartmentName() != null) {
					map.put("departmentName",
							hd.getDgSampleCollectionHd().getDgOrderHd().getMasDepartment().getDepartmentName());
				} else {
					map.put("departmentName", "");
				}

				if (hd.getResultEntryId() != null) {
					map.put("resultEntHdId", hd.getResultEntryId());
				} else {
					map.put("resultEntHdId", "");
				}

				if (hd.getSampleCollectionHeaderId() != null) {
					map.put("sampleCollectionHdId", hd.getSampleCollectionHeaderId());
				} else {
					map.put("sampleCollectionHdId", "");
				}

				if (hd.getPatientId() != null) {
					map.put("patientId", hd.getPatientId());
				} else {
					map.put("patientId", "");
				}
				if (hd.getPatient() != null && hd.getPatient().getMobileNumber() != null) {
					map.put("mobileNumber", hd.getPatient().getMobileNumber());
				} else {
					map.put("mobileNumber", "");
				}
				if (hd.getSubChargecodeId() != null) {
					map.put("chargeCodeId", hd.getSubChargecodeId());
				} else {
					map.put("chargeCodeId", "");
				}
				if (hd.getMasSubChargecode().getSubChargecodeName() != null) {
					map.put("subChargecodeName", hd.getMasSubChargecode().getSubChargecodeName());
				} else {
					map.put("subChargecodeName", "");
				}

				/*
				 * if(dt.getResultEntryDetailId()!=null) { map.put("resultEntryDetailId",
				 * dt.getResultEntryDetailId()); }else { map.put("resultEntryDetailId", ""); }
				 */

				if (hd.getResultStatus() != null) {
					map.put("resultDetailStatus", hd.getResultStatus());
				} else {
					map.put("resultDetailStatus", "");
				}

				if (hd.getDgSampleCollectionHd().getOrderId() != null) {
					map.put("diagNo", hd.getDgSampleCollectionHd().getOrderId());
				} else {
					map.put("diagNo", "");
				}

				if (hd.getDgSampleCollectionHd().getOrderId() != null) {
					map.put("diagNo", hd.getDgSampleCollectionHd().getOrderId());
				} else {
					map.put("diagNo", "");
				}

				listObj.add(map);
			}
			if (listObj != null && !listObj.isEmpty()) {
				json.put("data", listObj);
				json.put("count", count);
				json.put("status", 1);
				json.put("msg", "Record Fetch Successfully");
			} else {
				json.put("data", listObj);
				json.put("count", 0);
				json.put("status", 0);
				json.put("msg", "Record Fetch Successfully");
			}
		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
			json.put("count", 0);
		}
		return json.toString();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public String getResultEntryUpdateDetails(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List list = new ArrayList();
		List globalListResultValidation = new ArrayList();

		if (jsonObject != null) {
			Long resultEntryHdId = Long.parseLong(jsonObject.get("resultEntryHdId").toString());
			// Map<String, Object> mapObject =
			// labDao.getResultValidationDetails(resultEntryHdId);
			Map<String, Object> mapObject = labDao.getResultEntryUpdateDetails(resultEntryHdId);

			if (mapObject != null && mapObject.size() > 0) {
				List<DgResultEntryHd> dgResultEntryHds = (List<DgResultEntryHd>) mapObject.get("dgResultEntryHdList");
				List<DgResultEntryDt> dgResultEntryDts = (List<DgResultEntryDt>) mapObject
						.get("dgResultEntryDtInvList");
				List<DgMasInvestigation> parInvestigationList = (List<DgMasInvestigation>) mapObject
						.get("parentInvestigationList");
				List<DgResultEntryDt> singleParameterList = (List<DgResultEntryDt>) mapObject
						.get("singleParameterList");
				List<DgResultEntryDt> templateParameterList = (List<DgResultEntryDt>) mapObject
						.get("templateParameterList");

				if (CollectionUtils.isNotEmpty(dgResultEntryHds) && dgResultEntryHds != null) {
					for (DgResultEntryHd hd : dgResultEntryHds) {
						Map<String, Object> mapDetails = new HashMap<String, Object>();
						if (hd.getResultEntryId() != null) {
							mapDetails.put("resultEntryHdId", hd.getResultEntryId());
						} else {
							mapDetails.put("resultEntryHdId", "");
						}

						if (hd.getDepartmentId() != null) {
							mapDetails.put("departmentId", hd.getDepartmentId());
						} else {
							mapDetails.put("departmentId", "");
						}

						if (hd.getVisit() != null && hd.getVisit().getMasDepartment() != null
								&& hd.getVisit().getMasDepartment().getDepartmentName() != null) {
							mapDetails.put("departmentName", hd.getVisit().getMasDepartment().getDepartmentName());
						} else {
							mapDetails.put("departmentName", "");
						}

						if (hd.getResultDate() != null) {
							mapDetails.put("resultDate",
									HMSUtil.convertDateToStringFormat(hd.getResultDate(), "dd/MM/yyyy"));
						} else {
							mapDetails.put("resultDate", "");
						}

						if (hd.getResultDate() != null) {
							mapDetails.put("resultTime",
									JavaUtils.getTimeFromDateAndTime(hd.getResultDate().toString()));
						} else {
							mapDetails.put("resultTime", "");
						}

						if (hd.getPatientId() != null) {
							mapDetails.put("patientId", hd.getPatientId());
						} else {
							mapDetails.put("patientId", "");
						}
						if (hd.getPatient() != null && hd.getPatient().getPatientName() != null) {
							mapDetails.put("patientName", hd.getPatient().getPatientName());
						} else {
							mapDetails.put("patientName", "");
						}
						/*
						 * if (hd.getPatient() != null && hd.getPatient().getMasrelation() != null &&
						 * hd.getPatient().getMasrelation().getRelationId() != null) {
						 * mapDetails.put("relationId",
						 * hd.getPatient().getMasrelation().getRelationId()); } else {
						 * mapDetails.put("relationId", ""); }
						 * 
						 * if (hd.getPatient() != null && hd.getPatient().getMasrelation() != null &&
						 * hd.getPatient().getMasrelation().getRelationName() != null) {
						 * mapDetails.put("relationName",
						 * hd.getPatient().getMasrelation().getRelationName()); } else {
						 * mapDetails.put("relationName", ""); }
						 * 
						 * if (hd.getPatient() != null && hd.getPatient().getEmployeeName() != null) {
						 * mapDetails.put("employeeName", hd.getPatient().getEmployeeName()); } else {
						 * mapDetails.put("employeeName", ""); }
						 */

						if (hd.getPatient().getDateOfBirth() != null) {
							Date dateOfBirth = hd.getPatient().getDateOfBirth();
							// String age = JavaUtils.calculateAgefromDob(dateOfBirth);
							// mapDetails.put("age", age);

							Date date = hd.getPatient().getDateOfBirth();
							Date visitDate = null;
							if (hd.getVisit() != null && hd.getVisit().getVisitDate() != null) {
								visitDate = hd.getVisit().getVisitDate();
							}
							if (visitDate != null && date != null) {
								mapDetails.put("age", HMSUtil.getDateBetweenTwoDate(visitDate, date));
							} else {
								mapDetails.put("age", "");
							}

						} else {
							mapDetails.put("age", "");
						}

						if (hd.getPatient() != null && hd.getPatient().getMasAdministrativeSex() != null
								&& hd.getPatient().getMasAdministrativeSex().getAdministrativeSexName() != null) {
							mapDetails.put("gender",
									hd.getPatient().getMasAdministrativeSex().getAdministrativeSexName());
						} else {
							mapDetails.put("gender", "");
						}

						/*
						 * if (hd.getPatient() != null && hd.getPatient().getServiceNo() != null) {
						 * mapDetails.put("serviceNo", hd.getPatient().getServiceNo()); } else {
						 * mapDetails.put("serviceNo", ""); }
						 */

						if (hd.getVisitId() != null) {
							mapDetails.put("visitId", hd.getVisitId());
						} else {
							mapDetails.put("visitId", "");
						}

						if (hd.getDgSampleCollectionHd() != null && hd.getDgSampleCollectionHd().getDgOrderHd() != null
								&& hd.getDgSampleCollectionHd().getDgOrderHd().getUsers().getUserName() != null) {
							mapDetails.put("validatedBy",
									hd.getDgSampleCollectionHd().getDgOrderHd().getUsers().getUserName());
						} else {
							mapDetails.put("validatedBy", "");
						}

						if (hd.getDgSampleCollectionHd() != null && hd.getDgSampleCollectionHd().getDgOrderHd() != null
								&& hd.getDgSampleCollectionHd().getDgOrderHd().getUsers().getUserName() != null) {
							mapDetails.put("resultEnteredBy",
									hd.getDgSampleCollectionHd().getDgOrderHd().getUsers().getUserName());
						} else {
							mapDetails.put("resultEnteredBy", "");
						}

						if (hd.getSampleCollectionHeaderId() != null) {
							mapDetails.put("sampleCollectionHdId", hd.getSampleCollectionHeaderId());
						} else {
							mapDetails.put("sampleCollectionHdId", "");
						}

						if (hd.getOrderHdId() != null) {
							mapDetails.put("orderHdId", hd.getOrderHdId());
						} else {
							mapDetails.put("orderHdId", "");
						}

						/* SAMPLE DETAILS */
						LocalDate currentDate1 = LocalDate.now();
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
						String currentDate = formatter.format(currentDate1);
						String currTime = LocalTime.now().toString();
						String[] currTime1 = currTime.split("\\.");
						String currentTime = currTime1[0];

						mapDetails.put("currentDate", currentDate);
						mapDetails.put("currentTime", currentTime);

						if (hd.getPatient() != null && hd.getPatient().getMobileNumber() != null) {
							mapDetails.put("mobileNoT", hd.getPatient().getMobileNumber());
						} else {
							mapDetails.put("mobileNoT", "");
						}
						
						list.add(mapDetails);
					}
				}

				/*
				 * List investigationDetailList = new ArrayList();
				 * if(CollectionUtils.isNotEmpty(dgResultEntryDts) && dgResultEntryDts!=null) {
				 * for(DgResultEntryDt dgResultEntryDt: dgResultEntryDts) { Map<String, Object>
				 * mapInvestigationDetails = new HashMap<String, Object>();
				 * if(dgResultEntryDt.getInvestigationId()!=null) {
				 * mapInvestigationDetails.put("investigationId",
				 * dgResultEntryDt.getInvestigationId()); }else {
				 * mapInvestigationDetails.put("investigationId", ""); }
				 * 
				 * if(dgResultEntryDt.getSubInvestigationId()!=null) {
				 * mapInvestigationDetails.put("subInvestigationId",
				 * dgResultEntryDt.getSubInvestigationId()); }else {
				 * mapInvestigationDetails.put("subInvestigationId", ""); }
				 * 
				 * if(dgResultEntryDt.getDgMasInvestigation()!=null &&
				 * dgResultEntryDt.getDgMasInvestigation().getInvestigationName()!=null) {
				 * mapInvestigationDetails.put("investigationName",
				 * dgResultEntryDt.getDgMasInvestigation().getInvestigationName()); }else {
				 * mapInvestigationDetails.put("investigationName", ""); }
				 * 
				 * if(dgResultEntryDt.getDgSubMasInvestigation()!=null &&
				 * dgResultEntryDt.getDgSubMasInvestigation().getSubInvestigationName()!=null) {
				 * mapInvestigationDetails.put("subInvestigationName",
				 * dgResultEntryDt.getDgSubMasInvestigation().getSubInvestigationName()); }else
				 * { mapInvestigationDetails.put("subInvestigationName", ""); }
				 * 
				 * if(dgResultEntryDt.getDgMasInvestigation()!=null &&
				 * dgResultEntryDt.getDgMasInvestigation().getMasSample()!=null &&
				 * dgResultEntryDt.getDgMasInvestigation().getMasSample().getSampleId()!=null) {
				 * mapInvestigationDetails.put("sampleId",
				 * dgResultEntryDt.getDgMasInvestigation().getMasSample().getSampleId()); }else
				 * { mapInvestigationDetails.put("sampleId", ""); }
				 * 
				 * if(dgResultEntryDt.getDgMasInvestigation()!=null &&
				 * dgResultEntryDt.getDgMasInvestigation().getMasSample()!=null &&
				 * dgResultEntryDt.getDgMasInvestigation().getMasSample().getSampleDescription()
				 * !=null) { mapInvestigationDetails.put("sampleDescription",
				 * dgResultEntryDt.getDgMasInvestigation().getMasSample().getSampleDescription()
				 * ); }else { mapInvestigationDetails.put("sampleDescription", ""); }
				 * 
				 * if(dgResultEntryDt.getDgMasInvestigation()!=null &&
				 * dgResultEntryDt.getDgMasInvestigation().getMasUOM()!=null &&
				 * dgResultEntryDt.getDgMasInvestigation().getMasUOM().getUOMId()!=null) {
				 * mapInvestigationDetails.put("uomId",
				 * dgResultEntryDt.getDgMasInvestigation().getMasUOM().getUOMId()); }else {
				 * mapInvestigationDetails.put("uomId", ""); }
				 * 
				 * if(dgResultEntryDt.getDgMasInvestigation()!=null &&
				 * dgResultEntryDt.getDgMasInvestigation().getMasUOM()!=null &&
				 * dgResultEntryDt.getDgMasInvestigation().getMasUOM().getUOMName()!=null) {
				 * mapInvestigationDetails.put("uomName",
				 * dgResultEntryDt.getDgMasInvestigation().getMasUOM().getUOMName()); }else {
				 * mapInvestigationDetails.put("uomName", ""); }
				 * 
				 * if(dgResultEntryDt.getResult()!=null) { mapInvestigationDetails.put("result",
				 * dgResultEntryDt.getResult()); }else { mapInvestigationDetails.put("result",
				 * ""); }
				 * 
				 * if(dgResultEntryDt.getResult()!=null) { mapInvestigationDetails.put("range",
				 * dgResultEntryDt.getRangeValue()); }else {
				 * mapInvestigationDetails.put("range", ""); }
				 * 
				 * if(dgResultEntryDt.getResult()!=null) {
				 * mapInvestigationDetails.put("rangeStatus", dgResultEntryDt.getRangeStatus());
				 * }else { mapInvestigationDetails.put("rangeStatus", ""); }
				 * if(dgResultEntryDt.getResultEntryId()!=null) {
				 * mapInvestigationDetails.put("resultEntryHdId",
				 * dgResultEntryDt.getResultEntryId()); }else {
				 * mapInvestigationDetails.put("resultEntryHdId", ""); }
				 * 
				 * if(dgResultEntryDt.getResultEntryDetailId()!=null) {
				 * mapInvestigationDetails.put("resultEntryDtId",
				 * dgResultEntryDt.getResultEntryDetailId()); }else {
				 * mapInvestigationDetails.put("resultEntryDtId", ""); }
				 * if(dgResultEntryDt.getResultType()!=null) {
				 * mapInvestigationDetails.put("resultType", dgResultEntryDt.getResultType());
				 * }else { mapInvestigationDetails.put("resultType", ""); }
				 * 
				 * investigationDetailList.add(mapInvestigationDetails); }
				 * 
				 * }
				 */

				List subInvestigationList = new ArrayList();
				if (CollectionUtils.isNotEmpty(dgResultEntryDts) && dgResultEntryDts != null) {
					for (DgResultEntryDt dgResultEntryDt : dgResultEntryDts) {
						Map<String, Object> mapSubInvestigationDetails = new HashMap<String, Object>();
						if (dgResultEntryDt.getResultType().equalsIgnoreCase("m")) {

							mapSubInvestigationDetails.put("subInvestigationId",
									dgResultEntryDt.getDgSubMasInvestigation().getSubInvestigationId());
							mapSubInvestigationDetails.put("subInvestigationName",
									dgResultEntryDt.getDgSubMasInvestigation().getSubInvestigationName());
							mapSubInvestigationDetails.put("investigtionType",
									dgResultEntryDt.getDgMasInvestigation().getInvestigationType());
							mapSubInvestigationDetails.put("investigatonId",
									dgResultEntryDt.getDgMasInvestigation().getInvestigationId());
							mapSubInvestigationDetails.put("investigatonName",
									dgResultEntryDt.getDgMasInvestigation().getInvestigationName());
							mapSubInvestigationDetails.put("subchargeCodeId",
									dgResultEntryDt.getDgSubMasInvestigation().getSubChargecodeId());

							mapSubInvestigationDetails.put("result",
									dgResultEntryDt.getResult() != null ? dgResultEntryDt.getResult() : "");
							mapSubInvestigationDetails.put("resultType",
									dgResultEntryDt.getResultType() != null ? dgResultEntryDt.getResultType() : "");

							mapSubInvestigationDetails.put("rangeValue",
									dgResultEntryDt.getRangeValue() != null ? dgResultEntryDt.getRangeValue() : "");
							mapSubInvestigationDetails.put("rangeStatus",
									dgResultEntryDt.getRangeStatus() != null ? dgResultEntryDt.getRangeStatus() : "");
							mapSubInvestigationDetails.put("uomId",
									dgResultEntryDt.getDgSubMasInvestigation() != null
											&& dgResultEntryDt.getDgSubMasInvestigation().getMasUOM() != null
											&& dgResultEntryDt.getDgSubMasInvestigation().getMasUOM().getUOMId() != null
													? dgResultEntryDt.getDgSubMasInvestigation().getMasUOM().getUOMId()
													: "");
							mapSubInvestigationDetails.put("uomName",
									dgResultEntryDt.getDgSubMasInvestigation().getMasUOM() != null && dgResultEntryDt
											.getDgSubMasInvestigation().getMasUOM().getUOMName() != null
													? dgResultEntryDt.getDgSubMasInvestigation().getMasUOM()
															.getUOMName()
													: "");

							mapSubInvestigationDetails.put("remarks",
									dgResultEntryDt.getRemarks() != null ? dgResultEntryDt.getRemarks() : "");
							mapSubInvestigationDetails.put("sampleDescription",
									dgResultEntryDt.getDgMasInvestigation() != null
											&& dgResultEntryDt.getDgMasInvestigation().getMasSample() != null
											&& dgResultEntryDt.getDgMasInvestigation().getMasSample()
													.getSampleDescription() != null
															? dgResultEntryDt.getDgMasInvestigation().getMasSample()
																	.getSampleDescription()
															: "");
							mapSubInvestigationDetails.put("sampleId", dgResultEntryDt.getDgMasInvestigation() != null
									&& dgResultEntryDt.getDgMasInvestigation().getMasSample() != null
									&& dgResultEntryDt.getDgMasInvestigation().getMasSample().getSampleId() != null
											? dgResultEntryDt.getDgMasInvestigation().getMasSample().getSampleId()
											: "");

							mapSubInvestigationDetails.put("resultEntryDtId",
									dgResultEntryDt.getResultEntryDetailId() != null
											? dgResultEntryDt.getResultEntryDetailId()
											: "");
							mapSubInvestigationDetails.put("resultEntryHdId",
									dgResultEntryDt.getResultEntryId() != null ? dgResultEntryDt.getResultEntryId()
											: "");

							mapSubInvestigationDetails.put("comparisonType",
									dgResultEntryDt.getDgSubMasInvestigation() != null
											&& dgResultEntryDt.getDgSubMasInvestigation().getComparisonType() != null
													? dgResultEntryDt.getDgSubMasInvestigation().getComparisonType()
													: "");
							mapSubInvestigationDetails.put("fixedId",
									dgResultEntryDt.getFixedId() != null ? dgResultEntryDt.getFixedId() : "");

							if (dgResultEntryDt.getDgSubMasInvestigation() != null
									&& dgResultEntryDt.getDgSubMasInvestigation().getDgFixedValues() != null) {
								List<DgFixedValue> dgFixedValues = dgResultEntryDt.getDgSubMasInvestigation()
										.getDgFixedValues();
								Map<Object, Object> dgfixedValueMap = new HashMap<Object, Object>();
								for (DgFixedValue dgfixedValue : dgFixedValues) {
									dgfixedValueMap.put(dgfixedValue.getFixedValue().trim(),
											dgfixedValue.getFixedId() + "@@" + dgfixedValue.getFixedValue().trim());
								}
								mapSubInvestigationDetails.put("dgFixedValue", dgfixedValueMap);
							} else {
								mapSubInvestigationDetails.put("dgFixedValue", "");
							}

							subInvestigationList.add(mapSubInvestigationDetails);
						}
					}
				}
				List parentInvestigationList = new ArrayList();
				if (CollectionUtils.isNotEmpty(parInvestigationList) && parInvestigationList != null) {
					for (DgMasInvestigation dgMasInvestigation : parInvestigationList) {
						Map<Object, Object> mapSubInvestigationParent = new HashMap<Object, Object>();

						mapSubInvestigationParent.put("investigtionId",
								dgMasInvestigation.getInvestigationId() != null
										? dgMasInvestigation.getInvestigationId()
										: "");
						mapSubInvestigationParent.put("investigtionName",
								dgMasInvestigation.getInvestigationName() != null
										? dgMasInvestigation.getInvestigationName()
										: "");
						mapSubInvestigationParent.put("investigtionType",
								dgMasInvestigation.getInvestigationType() != null
										? dgMasInvestigation.getInvestigationType()
										: "");

						parentInvestigationList.add(mapSubInvestigationParent);
					}
				}
				// single parameter
				List templateParaList = new ArrayList();
				List singleParaList = new ArrayList();
				if (CollectionUtils.isNotEmpty(singleParameterList) && singleParameterList != null) {
					for (DgResultEntryDt dgResultEntryDt : singleParameterList) {
						Map<String, Object> mapSinglePara = new HashMap<String, Object>();
						mapSinglePara.put("investigationId",
								dgResultEntryDt.getInvestigationId() != null ? dgResultEntryDt.getInvestigationId()
										: "");
						mapSinglePara.put("subInvestigationId",
								dgResultEntryDt.getSubInvestigationId() != null
										? dgResultEntryDt.getSubInvestigationId()
										: "");
						mapSinglePara.put("investigationName",
								dgResultEntryDt.getDgMasInvestigation() != null
										&& dgResultEntryDt.getDgMasInvestigation().getInvestigationName() != null
												? dgResultEntryDt.getDgMasInvestigation().getInvestigationName()
												: "");
						mapSinglePara.put("subInvestigationName",
								dgResultEntryDt.getDgSubMasInvestigation() != null
										&& dgResultEntryDt.getDgSubMasInvestigation().getSubInvestigationName() != null
												? dgResultEntryDt.getDgSubMasInvestigation().getSubInvestigationName()
												: "");
						mapSinglePara.put("sampleId",
								dgResultEntryDt.getDgMasInvestigation() != null
										&& dgResultEntryDt.getDgMasInvestigation().getMasSample() != null
										&& dgResultEntryDt.getDgMasInvestigation().getMasSample().getSampleId() != null
												? dgResultEntryDt.getDgMasInvestigation().getMasSample().getSampleId()
												: "");

						mapSinglePara.put("sampleDescription", dgResultEntryDt.getDgMasInvestigation() != null
								&& dgResultEntryDt.getDgMasInvestigation().getMasSample() != null
								&& dgResultEntryDt.getDgMasInvestigation().getMasSample().getSampleDescription() != null
										? dgResultEntryDt.getDgMasInvestigation().getMasSample().getSampleDescription()
										: "");

						mapSinglePara.put("uomId",
								dgResultEntryDt.getDgMasInvestigation() != null
										&& dgResultEntryDt.getDgMasInvestigation().getMasUOM() != null
										&& dgResultEntryDt.getDgMasInvestigation().getMasUOM().getUOMId() != null
												? dgResultEntryDt.getDgMasInvestigation().getMasUOM().getUOMId()
												: "");

						mapSinglePara.put("uomName",
								dgResultEntryDt.getDgMasInvestigation() != null
										&& dgResultEntryDt.getDgMasInvestigation().getMasUOM() != null
										&& dgResultEntryDt.getDgMasInvestigation().getMasUOM().getUOMName() != null
												? dgResultEntryDt.getDgMasInvestigation().getMasUOM().getUOMName()
												: "");
						mapSinglePara.put("result",
								dgResultEntryDt.getResult() != null ? dgResultEntryDt.getResult() : "");
						mapSinglePara.put("rangeValue",
								dgResultEntryDt.getRangeValue() != null ? dgResultEntryDt.getRangeValue() : "");
						mapSinglePara.put("range",
								dgResultEntryDt.getResult() != null ? dgResultEntryDt.getRangeValue() : "");
						mapSinglePara.put("rangeStatus",
								dgResultEntryDt.getRangeStatus() != null ? dgResultEntryDt.getRangeStatus() : "");
						mapSinglePara.put("resultType",
								dgResultEntryDt.getResultType() != null ? dgResultEntryDt.getResultType() : "");
						mapSinglePara.put("remarks",
								dgResultEntryDt.getRemarks() != null ? dgResultEntryDt.getRemarks() : "");

						mapSinglePara.put("resultEntryHdId",
								dgResultEntryDt.getResultEntryId() != null ? dgResultEntryDt.getResultEntryId() : "");
						mapSinglePara.put("resultEntryDtId",
								dgResultEntryDt.getResultEntryDetailId() != null
										? dgResultEntryDt.getResultEntryDetailId()
										: "");

						singleParaList.add(mapSinglePara);
					}
				}

				if (CollectionUtils.isNotEmpty(templateParameterList) && templateParameterList != null) {
					for (DgResultEntryDt dgResultEntryDt : templateParameterList) {
						Map<String, Object> mapTemplatePara = new HashMap<String, Object>();

						mapTemplatePara.put("investigationId",
								dgResultEntryDt.getInvestigationId() != null ? dgResultEntryDt.getInvestigationId()
										: "");
						mapTemplatePara.put("subInvestigationId",
								dgResultEntryDt.getSubInvestigationId() != null
										? dgResultEntryDt.getSubInvestigationId()
										: "");
						mapTemplatePara.put("investigationName",
								dgResultEntryDt.getDgMasInvestigation() != null
										&& dgResultEntryDt.getDgMasInvestigation().getInvestigationName() != null
												? dgResultEntryDt.getDgMasInvestigation().getInvestigationName()
												: "");
						mapTemplatePara.put("subInvestigationName",
								dgResultEntryDt.getDgSubMasInvestigation() != null
										&& dgResultEntryDt.getDgSubMasInvestigation().getSubInvestigationName() != null
												? dgResultEntryDt.getDgSubMasInvestigation().getSubInvestigationName()
												: "");
						mapTemplatePara.put("sampleId",
								dgResultEntryDt.getDgMasInvestigation() != null
										&& dgResultEntryDt.getDgMasInvestigation().getMasSample() != null
										&& dgResultEntryDt.getDgMasInvestigation().getMasSample().getSampleId() != null
												? dgResultEntryDt.getDgMasInvestigation().getMasSample().getSampleId()
												: "");

						mapTemplatePara.put("sampleDescription", dgResultEntryDt.getDgMasInvestigation() != null
								&& dgResultEntryDt.getDgMasInvestigation().getMasSample() != null
								&& dgResultEntryDt.getDgMasInvestigation().getMasSample().getSampleDescription() != null
										? dgResultEntryDt.getDgMasInvestigation().getMasSample().getSampleDescription()
										: "");

						mapTemplatePara.put("uomId",
								dgResultEntryDt.getDgMasInvestigation() != null
										&& dgResultEntryDt.getDgMasInvestigation().getMasUOM() != null
										&& dgResultEntryDt.getDgMasInvestigation().getMasUOM().getUOMId() != null
												? dgResultEntryDt.getDgMasInvestigation().getMasUOM().getUOMId()
												: "");

						mapTemplatePara.put("uomName",
								dgResultEntryDt.getDgMasInvestigation() != null
										&& dgResultEntryDt.getDgMasInvestigation().getMasUOM() != null
										&& dgResultEntryDt.getDgMasInvestigation().getMasUOM().getUOMName() != null
												? dgResultEntryDt.getDgMasInvestigation().getMasUOM().getUOMName()
												: "");
						mapTemplatePara.put("result",
								dgResultEntryDt.getResult() != null ? dgResultEntryDt.getResult() : "");
						mapTemplatePara.put("range",
								dgResultEntryDt.getResult() != null ? dgResultEntryDt.getRangeValue() : "");
						mapTemplatePara.put("rangeStatus",
								dgResultEntryDt.getResult() != null ? dgResultEntryDt.getRangeStatus() : "");

						mapTemplatePara.put("resultType",
								dgResultEntryDt.getResultType() != null ? dgResultEntryDt.getResultType() : "");
						mapTemplatePara.put("rangeValue",
								dgResultEntryDt.getRangeValue() != null ? dgResultEntryDt.getRangeValue() : "");
						mapTemplatePara.put("remarks",
								dgResultEntryDt.getRemarks() != null ? dgResultEntryDt.getRemarks() : "");

						mapTemplatePara.put("resultEntryHdId",
								dgResultEntryDt.getResultEntryId() != null ? dgResultEntryDt.getResultEntryId() : "");
						mapTemplatePara.put("resultEntryDtId",
								dgResultEntryDt.getResultEntryDetailId() != null
										? dgResultEntryDt.getResultEntryDetailId()
										: "");

						templateParaList.add(mapTemplatePara);
					}
				}
				// template parameter

				Map<String, Object> globalMap = new HashMap<String, Object>();
				// globalListResultValidation

				globalMap.put("data", list);
				// globalMap.put("investigationDetailList", investigationDetailList);
				globalMap.put("subInvestigationList", subInvestigationList);
				globalMap.put("parentInvestigationList", parentInvestigationList);
				globalMap.put("singleParameter", singleParaList);
				globalMap.put("templateParameter", templateParaList);
				globalMap.put("count", list.size());
				globalMap.put("msg", "Record Fetch Successfully");
				globalMap.put("status", 1);

				globalListResultValidation.add(globalMap);

				if (globalListResultValidation != null && !globalListResultValidation.isEmpty()) {
					json.put("globalListResultValidation", globalListResultValidation);
				}

			} else {
				json.put("msg", "No Record Found");
				json.put("status", 0);
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}
		return json.toString();

	}

	/*
	 * @Override public String getResultEntryUpdateDetails(JSONObject jsonObject,
	 * HttpServletRequest request, HttpServletResponse response) { JSONObject json =
	 * new JSONObject(); List list = new ArrayList(); List<DgSampleCollectionDt>
	 * dgSampCollDtsInvList = null; List<DgSubMasInvestigation> subInvestigationList
	 * = null; List<DgFixedValue> fixedValueList = null; Map<String, Object>
	 * mapFixedValue = null; List<DgNormalValue> normalValList = null;
	 * 
	 * if(jsonObject!=null) { Long resultEntryHdId =
	 * Long.parseLong(jsonObject.get("resultEntryHdId").toString());
	 * 
	 * Map<String, Object> mapobj =
	 * labDao.getResultEntryUpdateDetails(resultEntryHdId);
	 * 
	 * Map<String, Object> mapdata =
	 * labDao.getInvestigationDtFromDgResultEntryDt(resultEntryHdId);
	 * 
	 * dgSampCollDtsInvList = (List<DgSampleCollectionDt>)
	 * mapdata.get("dgSampCollDtsInvList");
	 * 
	 * subInvestigationList =
	 * (List<DgSubMasInvestigation>)mapdata.get("subInvestigationList");
	 * 
	 * fixedValueList = (List<DgFixedValue>) mapdata.get("fixedValueList");
	 * 
	 * mapFixedValue = (Map<String, Object>)mapdata.get("mapFixedValue");
	 * 
	 * normalValList = (List<DgNormalValue>) mapdata.get("normalValList");
	 * 
	 * if(mapobj!=null && mapobj.size()>0) { List<DgResultEntryHd> dgResultEntryHds
	 * = (List<DgResultEntryHd>) mapobj.get("dgResultEntryHdList");
	 * List<DgResultEntryDt> dgResultEntryDts = (List<DgResultEntryDt>)
	 * mapobj.get("dgResultEntryDtInvList");
	 * 
	 * if(CollectionUtils.isNotEmpty(dgResultEntryHds) && dgResultEntryHds!=null) {
	 * for(DgResultEntryHd hd:dgResultEntryHds) { Map<String, Object> mapDetails =
	 * new HashMap<String, Object>(); if(hd.getResultEntryId()!=null) {
	 * mapDetails.put("resultEntryHdId", hd.getResultEntryId()); }else {
	 * mapDetails.put("resultEntryHdId", ""); }
	 * 
	 * if(hd.getDepartmentId()!=null) { mapDetails.put("departmentId",
	 * hd.getDepartmentId()); }else { mapDetails.put("departmentId", ""); }
	 * 
	 * if(hd.getVisit()!=null && hd.getVisit().getMasDepartment()!=null &&
	 * hd.getVisit().getMasDepartment().getDepartmentName()!=null) {
	 * mapDetails.put("departmentName",
	 * hd.getVisit().getMasDepartment().getDepartmentName()); }else {
	 * mapDetails.put("departmentName", ""); }
	 * 
	 * if(hd.getResultDate()!=null) { mapDetails.put("resultDate",
	 * HMSUtil.convertDateToStringFormat(hd.getResultDate(), "dd/MM/yyyy")); }else {
	 * mapDetails.put("resultDate", ""); }
	 * 
	 * if(hd.getResultDate()!=null) { mapDetails.put("resultTime",
	 * JavaUtils.getTimeFromDateAndTime(hd.getResultDate().toString())); }else {
	 * mapDetails.put("resultTime", ""); }
	 * 
	 * if(hd.getPatientId()!=null) { mapDetails.put("patientId", hd.getPatientId());
	 * }else { mapDetails.put("patientId", ""); } if(hd.getPatient()!=null &&
	 * hd.getPatient().getPatientName()!=null) { mapDetails.put("patientName",
	 * hd.getPatient().getPatientName()); }else { mapDetails.put("patientName", "");
	 * } if(hd.getPatient()!=null && hd.getPatient().getMasrelation()!=null &&
	 * hd.getPatient().getMasrelation().getRelationId()!=null) {
	 * mapDetails.put("relationId",
	 * hd.getPatient().getMasrelation().getRelationId()); }else {
	 * mapDetails.put("relationId", ""); }
	 * 
	 * if(hd.getPatient()!=null && hd.getPatient().getMasrelation()!=null &&
	 * hd.getPatient().getMasrelation().getRelationName()!=null) {
	 * mapDetails.put("relationName",
	 * hd.getPatient().getMasrelation().getRelationName()); }else {
	 * mapDetails.put("relationName", ""); }
	 * 
	 * if(hd.getPatient()!=null && hd.getPatient().getEmployeeName()!=null) {
	 * mapDetails.put("employeeName", hd.getPatient().getEmployeeName()); }else {
	 * mapDetails.put("employeeName", ""); }
	 * 
	 * if(hd.getPatient().getDateOfBirth()!=null) { Date dateOfBirth =
	 * hd.getPatient().getDateOfBirth(); String age =
	 * JavaUtils.calculateAgefromDob(dateOfBirth); mapDetails.put("age", age); }else
	 * { mapDetails.put("age", ""); }
	 * 
	 * if(hd.getPatient()!=null && hd.getPatient().getMasAdministrativeSex()!=null
	 * &&
	 * hd.getPatient().getMasAdministrativeSex().getAdministrativeSexName()!=null) {
	 * mapDetails.put("gender",
	 * hd.getPatient().getMasAdministrativeSex().getAdministrativeSexName()); }else
	 * { mapDetails.put("gender", ""); }
	 * 
	 * if(hd.getPatient()!=null && hd.getPatient().getServiceNo()!=null) {
	 * mapDetails.put("serviceNo", hd.getPatient().getServiceNo()); }else {
	 * mapDetails.put("serviceNo", ""); }
	 * 
	 * if(hd.getVisitId()!=null) { mapDetails.put("visitId", hd.getVisitId()); }else
	 * { mapDetails.put("visitId", ""); }
	 * 
	 * if(hd.getDgSampleCollectionHd()!=null &&
	 * hd.getDgSampleCollectionHd().getDgOrderHd()!=null &&
	 * hd.getDgSampleCollectionHd().getDgOrderHd().getUsers().getFirstName()!=null)
	 * { mapDetails.put("validatedBy",
	 * hd.getDgSampleCollectionHd().getDgOrderHd().getUsers().getFirstName()); }else
	 * { mapDetails.put("validatedBy", ""); }
	 * 
	 * if(hd.getOrderHdId()!=null) { mapDetails.put("orderHdId", hd.getOrderHdId());
	 * }else { mapDetails.put("orderHdId", ""); }
	 * 
	 * if(hd.getDgSampleCollectionHd()!=null &&
	 * hd.getDgSampleCollectionHd().getDgOrderHd()!=null &&
	 * hd.getDgSampleCollectionHd().getDgOrderHd().getUsers().getFirstName()!=null)
	 * { mapDetails.put("resultEnteredBy",
	 * hd.getDgSampleCollectionHd().getDgOrderHd().getUsers().getFirstName()); }else
	 * { mapDetails.put("resultEnteredBy", ""); }
	 * 
	 * 
	 * if(hd.getSampleCollectionHeaderId()!=null) {
	 * mapDetails.put("sampleCollectionHdId", hd.getSampleCollectionHeaderId());
	 * }else { mapDetails.put("sampleCollectionHdId", ""); }
	 * 
	 * 
	 * SAMPLE DETAILS LocalDate currentDate1 = LocalDate.now(); DateTimeFormatter
	 * formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy"); String currentDate =
	 * formatter.format(currentDate1); String currTime = LocalTime.now().toString();
	 * String[] currTime1 = currTime.split("\\."); String currentTime =currTime1[0];
	 * 
	 * mapDetails.put("currentDate", currentDate); mapDetails.put("currentTime",
	 * currentTime);
	 * 
	 * list.add(mapDetails); } } Long parentInvestigationId = null; Long
	 * childInvestigationId = null; String parentInvName=""; List listsubMasInv =
	 * new ArrayList(); List listSubInvId = new ArrayList(); String
	 * minNormalValue=""; String maxNormalValue=""; List resultDtlsList = new
	 * ArrayList(); if(CollectionUtils.isNotEmpty(dgResultEntryDts) &&
	 * dgResultEntryDts!=null) { for(DgResultEntryDt dt:dgResultEntryDts) {
	 * Map<String, Object> resultDetailsMap = new HashMap<String, Object>();
	 * if(dt.getResultEntryDetailId()!=null) { resultDetailsMap.put("resultEntDtId",
	 * dt.getResultEntryDetailId()); }else { resultDetailsMap.put("resultEntDtId",
	 * ""); }
	 * 
	 * if(dt.getResultEntryId()!=null) { resultDetailsMap.put("resulEntHdId",
	 * dt.getResultEntryId()); }else { resultDetailsMap.put("resulEntHdId", ""); }
	 * 
	 * if(dt.getChargeCodeId()!=null) { resultDetailsMap.put("chargeCodeId",
	 * dt.getChargeCodeId()); }else { resultDetailsMap.put("chargeCodeId", ""); }
	 * 
	 * if(dt.getResultType()!=null) { resultDetailsMap.put("resultType",
	 * dt.getResultType()); }else { resultDetailsMap.put("resultType", ""); }
	 * 
	 * if(dt.getSubInvestigationId()!=null) {
	 * resultDetailsMap.put("subInvestigationId", dt.getSubInvestigationId()); }else
	 * { resultDetailsMap.put("subInvestigationId", ""); }
	 * 
	 * if(dt.getResultDetailStatus()!=null) {
	 * resultDetailsMap.put("resultDetailStatus", dt.getResultDetailStatus()); }else
	 * { resultDetailsMap.put("resultDetailStatus", ""); }
	 * 
	 * if(dt.getSampleCollectionDetailsId()!=null) {
	 * resultDetailsMap.put("sampleCollectionDetailId",
	 * dt.getSampleCollectionDetailsId()); }else {
	 * resultDetailsMap.put("sampleCollectionDetailId", ""); }
	 * 
	 * if(dt.getNprmalId()!=null) { resultDetailsMap.put("normalId",
	 * dt.getNprmalId()); }else { resultDetailsMap.put("normalId", ""); }
	 * 
	 * if(dt.getSampleId()!=null) { resultDetailsMap.put("sampleId",
	 * dt.getSampleId()); }else { resultDetailsMap.put("sampleId", ""); }
	 * 
	 * if(dt.getDgMasInvestigation().getMasSample().getSampleDescription()!=null) {
	 * resultDetailsMap.put("sampleDescription",
	 * dt.getDgMasInvestigation().getMasSample().getSampleDescription()); }else {
	 * resultDetailsMap.put("sampleDescription", ""); }
	 * 
	 * 
	 * if(dt.getResult()!=null) { resultDetailsMap.put("result", dt.getResult());
	 * }else { resultDetailsMap.put("result", ""); }
	 * 
	 * if(dt.getInvestigationId()!=null) { parentInvestigationId =
	 * dt.getInvestigationId(); resultDetailsMap.put("investigationId",
	 * dt.getInvestigationId()); }else { resultDetailsMap.put("investigationId",
	 * ""); }
	 * 
	 * if(dt.getDgMasInvestigation().getInvestigationName()!=null) { parentInvName =
	 * dt.getDgMasInvestigation().getInvestigationName();
	 * resultDetailsMap.put("investigationName",
	 * dt.getDgMasInvestigation().getInvestigationName()); }else {
	 * resultDetailsMap.put("investigationName", ""); }
	 * 
	 * if(dt.getDgSubMasInvestigation()!=null &&
	 * dt.getDgSubMasInvestigation().getSubInvestigationName()!=null) {
	 * resultDetailsMap.put("subInvestigationName",
	 * dt.getDgSubMasInvestigation().getSubInvestigationName()); }else {
	 * resultDetailsMap.put("subInvestigationName", ""); }
	 * 
	 * if(dt.getDgNormalValue()!=null &&
	 * dt.getDgNormalValue().getMinNormalValue()!=null) { minNormalValue =
	 * dt.getDgNormalValue().getMinNormalValue();
	 * resultDetailsMap.put("minNormalValue",
	 * dt.getDgNormalValue().getMinNormalValue()); }else {
	 * resultDetailsMap.put("minNormalValue", ""); }
	 * 
	 * if(dt.getDgNormalValue()!=null &&
	 * dt.getDgNormalValue().getMaxNormalValue()!=null) { maxNormalValue
	 * =dt.getDgNormalValue().getMaxNormalValue();
	 * resultDetailsMap.put("maxNormalValue",
	 * dt.getDgNormalValue().getMaxNormalValue()); }else {
	 * resultDetailsMap.put("maxNormalValue", ""); }
	 * 
	 * if(dt.getUomId()!=null) { resultDetailsMap.put("uomId", dt.getUomId()); }else
	 * { resultDetailsMap.put("uomId", ""); }
	 * 
	 * if(dt.getMasUOM()!=null && dt.getMasUOM().getUOMName()!=null) {
	 * resultDetailsMap.put("uomName", dt.getMasUOM().getUOMName()); }else {
	 * resultDetailsMap.put("uomName", ""); }
	 * 
	 * if(dt.getRangeValue()!=null) { resultDetailsMap.put("rangeValue",
	 * dt.getRangeValue()); }else { resultDetailsMap.put("rangeValue", ""); }
	 * 
	 * if(dt.getRemarks()!=null) { resultDetailsMap.put("remarks", dt.getRemarks());
	 * }else { resultDetailsMap.put("remarks", ""); }
	 * 
	 * String normalRange = minNormalValue +"-"+maxNormalValue;
	 * resultDetailsMap.put("normalRange", normalRange);
	 * listSubInvId.add(dt.getSubInvestigationId());
	 * 
	 * if(dt.getRangeStatus()!=null) { resultDetailsMap.put("rangeStatus",
	 * dt.getRangeStatus()); }else { resultDetailsMap.put("rangeStatus", ""); }
	 * 
	 * resultDtlsList.add(resultDetailsMap); }
	 * 
	 * List<DgSubMasInvestigation> listsubInv =
	 * labDao.getSubInvestigationDetails(listSubInvId);
	 * if(CollectionUtils.isNotEmpty(listsubInv)) { for(DgSubMasInvestigation
	 * dgsubInv:listsubInv) { Map<String, Object> mobj = new HashMap<String,
	 * Object>(); if(dgsubInv.getSubInvestigationId()!=null) {
	 * mobj.put("subInvestigationId", dgsubInv.getSubInvestigationId()); }else {
	 * mobj.put("subInvestigationId", ""); }
	 * 
	 * if(dgsubInv.getSubInvestigationName()!=null) {
	 * mobj.put("subInvestigationName", dgsubInv.getSubInvestigationName()); }else {
	 * mobj.put("subInvestigationName", ""); }
	 * 
	 * 
	 * if(dgsubInv.getInvestigationId()!=null) { childInvestigationId =
	 * dgsubInv.getInvestigationId(); mobj.put("investigationId",
	 * dgsubInv.getInvestigationId()); }else { mobj.put("investigationId", ""); }
	 * 
	 * if(dgsubInv.getDgMasInvestigation()!=null &&
	 * dgsubInv.getDgMasInvestigation().getInvestigationName()!=null) {
	 * mobj.put("investigationName",
	 * dgsubInv.getDgMasInvestigation().getInvestigationName()); }else {
	 * mobj.put("investigationName", ""); }
	 * 
	 * if(dgsubInv.getResultType()!=null) { mobj.put("resultType",
	 * dgsubInv.getResultType()); }else { mobj.put("resultType", ""); }
	 * 
	 * if(dgsubInv.getComparisonType()!=null) { mobj.put("comparisonType",
	 * dgsubInv.getComparisonType()); }else { mobj.put("comparisonType", ""); }
	 * 
	 * if(dgsubInv.getMainChargecodeId()!=null) { mobj.put("mainChargeCode",
	 * dgsubInv.getMainChargecodeId()); }else { mobj.put("mainChargeCode", ""); }
	 * 
	 * if(dgsubInv.getSubChargecodeId()!=null) { mobj.put("subChargeCodeId",
	 * dgsubInv.getSubChargecodeId()); }else { mobj.put("subChargeCodeId", ""); }
	 * 
	 * if(dgsubInv.getUomId()!=null) { mobj.put("uomId", dgsubInv.getUomId()); }else
	 * { mobj.put("uomId", ""); }
	 * 
	 * if(parentInvestigationId.equals(childInvestigationId)) {
	 * mobj.put("parentInvName", parentInvName); }else { mobj.put("parentInvName",
	 * ""); } if(dgsubInv.getDgMasInvestigation()!=null &&
	 * dgsubInv.getDgMasInvestigation().getInvestigationType()!=null) {
	 * mobj.put("investigationType",
	 * dgsubInv.getDgMasInvestigation().getInvestigationType()); }else {
	 * mobj.put("investigationType", ""); }
	 * 
	 * listsubMasInv.add(mobj); } } }
	 * 
	 * List norValList = new ArrayList(); List fixedValues = new ArrayList();
	 * if(CollectionUtils.isNotEmpty(fixedValueList)) { for(DgFixedValue fixedValue
	 * :fixedValueList) { Map<String, Object> map3 = new HashMap<String, Object>();
	 * if(fixedValue.getFixedId()!=null) { map3.put("fixedId",
	 * fixedValue.getFixedId()); }else { map3.put("fixedId", ""); }
	 * if(fixedValue.getFixedValue()!=null) { map3.put("fixedValue",
	 * fixedValue.getFixedValue()); }else { map3.put("fixedValue", ""); }
	 * 
	 * if(fixedValue.getSubInvestigationId()!=null) { map3.put("subInvestigationId",
	 * fixedValue.getSubInvestigationId()); }else { map3.put("subInvestigationId",
	 * ""); } fixedValues.add(map3); }
	 * 
	 * }
	 * 
	 * 
	 * if(!CollectionUtils.isEmpty(normalValList)) { for(DgNormalValue normalValue :
	 * normalValList) { Map<String, Object> mapNormalVal = new HashMap<String,
	 * Object>();
	 * 
	 * if(normalValue.getSex()!=null) { mapNormalVal.put("gender",
	 * normalValue.getSex()); }else { mapNormalVal.put("gender", ""); }
	 * 
	 * if(normalValue.getNormalId()!=null) { mapNormalVal.put("normalId",
	 * normalValue.getNormalId()); }else { mapNormalVal.put("normalId", ""); }
	 * 
	 * 
	 * if(normalValue.getFromAge()!=null) { mapNormalVal.put("fromAge",
	 * normalValue.getFromAge()); }else { mapNormalVal.put("fromAge", ""); }
	 * 
	 * if(normalValue.getToAge()!=null) { mapNormalVal.put("toAge",
	 * normalValue.getToAge()); }else { mapNormalVal.put("toAge", ""); }
	 * 
	 * if(normalValue.getMinNormalValue()!=null) {
	 * mapNormalVal.put("minNormalValue", normalValue.getMinNormalValue()); }else {
	 * mapNormalVal.put("minNormalValue", ""); }
	 * 
	 * if(normalValue.getMaxNormalValue()!=null) {
	 * mapNormalVal.put("maxNormalValue", normalValue.getMaxNormalValue()); }else {
	 * mapNormalVal.put("maxNormalValue", ""); }
	 * 
	 * if(normalValue.getNormalValue()!=null) { mapNormalVal.put("normalValue",
	 * normalValue.getNormalValue()); }else { mapNormalVal.put("normalValue", ""); }
	 * 
	 * if(normalValue.getSubInvestigationId()!=null) {
	 * mapNormalVal.put("subInvestigationId", normalValue.getSubInvestigationId());
	 * }else { mapNormalVal.put("subInvestigationId", ""); }
	 * 
	 * String range =
	 * normalValue.getMinNormalValue()+"-"+normalValue.getMaxNormalValue();
	 * mapNormalVal.put("range", range); norValList.add(mapNormalVal); } }
	 * 
	 * if(list!=null && !list.isEmpty()) { json.put("msg",
	 * "Record Fetch Successfully"); json.put("data", list); json.put("resultDtls",
	 * resultDtlsList); json.put("subInvestigationList", listsubMasInv);
	 * json.put("norValList", norValList); json.put("fixedValues", fixedValues);
	 * json.put("count", list.size()); json.put("resultDtlsCount",
	 * resultDtlsList.size()); json.put("status", 1); }else { json.put("msg",
	 * "No Record Found"); json.put("data", list); json.put("resultDtls",
	 * resultDtlsList); json.put("listsubMasInv", listsubMasInv);
	 * json.put("norValList", norValList); json.put("fixedValues", fixedValues);
	 * json.put("count", list.size()); json.put("resultDtlsCount",
	 * resultDtlsList.size()); json.put("status", 0); } }else { json.put("msg",
	 * "No Record Found"); json.put("status", 0); }
	 * 
	 * }else { json.put("msg", "No Record Found"); json.put("status", 0); } return
	 * json.toString(); }
	 */
	@Override
	public String updateResultEntryDetails(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {

		/*
		 * Session session = null; Transaction tx = null;
		 */
		JSONObject json = new JSONObject();
		if (jsonObject != null) {

			String[] subInvestigationIdArray = null;
			String[] resultValues = null;
			String[] userIdValues = null;

			/*
			 * session = getHibernateUtils.getHibernateUtlis().OpenSession(); tx =
			 * session.beginTransaction();
			 */

			if (jsonObject.has("subInvestigationId")) {
				String subInvestigationIds = jsonObject.get("subInvestigationId").toString();
				subInvestigationIds = JavaUtils.replaceStringWithDoubleQuotes(subInvestigationIds);
				subInvestigationIdArray = subInvestigationIds.split(",");
			}
			Long userId = null;
			if (jsonObject.has("userId")) {
				String userIds = jsonObject.get("userId").toString();
				userIds = JavaUtils.replaceStringWithDoubleQuotes(userIds);
				userIdValues = userIds.split(",");
				userId = Long.parseLong(userIdValues[0]);
			}

			if (jsonObject.has("result")) {
				String results = jsonObject.get("result").toString();
				results = JavaUtils.replaceStringWithDoubleQuotes(results);
				resultValues = results.split(",");
			}

			String remarksValue[] = null;
			if (jsonObject.has("remarks")) {
				String remarks = jsonObject.get("remarks").toString();
				remarks = JavaUtils.replaceStringWithDoubleQuotes(remarks);
				remarksValue = remarks.split(",");
			}

			String normalRangeValue[] = null;
			if (jsonObject.has("normalRange")) {
				String normalRanges = jsonObject.get("normalRange").toString();
				normalRanges = JavaUtils.replaceStringWithDoubleQuotes(normalRanges);
				normalRangeValue = normalRanges.split(",");
			}

			Long resultEntryHdId = null;
			String[] headerIdArray = null;
			if (jsonObject.has("resulEntHdId")) {
				String resulEntHdId = jsonObject.get("resulEntHdId").toString();
				resulEntHdId = JavaUtils.replaceStringWithDoubleQuotes(resulEntHdId);
				headerIdArray = resulEntHdId.split(",");
				resultEntryHdId = Long.parseLong(headerIdArray[0].toString());
			}

			String[] parentInvestigationIdArray = null;
			if (jsonObject.has("parentInvestigationId")) {
				String parentInvestigationId = jsonObject.get("parentInvestigationId").toString();
				parentInvestigationId = JavaUtils.replaceStringWithDoubleQuotes(parentInvestigationId);
				parentInvestigationIdArray = parentInvestigationId.split(",");
			}

			String[] resulEntDtIdArray = null;
			if (jsonObject.has("resulEntDtId")) {
				String resultEntDtId = jsonObject.get("resulEntDtId").toString();
				resultEntDtId = JavaUtils.replaceStringWithDoubleQuotes(resultEntDtId);
				resulEntDtIdArray = resultEntDtId.split(",");
			}

			String[] rangeStatusArray = null;
			if (jsonObject.has("rangestatus")) {
				String rangestatuss = jsonObject.get("rangestatus").toString();
				rangestatuss = JavaUtils.replaceStringWithDoubleQuotes(rangestatuss);
				rangeStatusArray = rangestatuss.split(",");
			}

			
			 String mobileNo="";
			    if (jsonObject.has("mobileNumber")) {
			    	mobileNo = jsonObject.get("mobileNumber").toString();
			    	mobileNo = JavaUtils.replaceStringWithDoubleQuotes(mobileNo);
			    	 
				}
			
			 String patientName="";
			    if (jsonObject.has("patientName")) {
			    	patientName = jsonObject.get("patientName").toString();
			    	patientName = JavaUtils.replaceStringWithDoubleQuotes(patientName);
			    	 
				}
			
		    String orderHdIdR="";
		    String []orderHdIdRA=null;
		    if (jsonObject.has("orderHdId")) {
		    	orderHdIdR = jsonObject.get("orderHdId").toString();
		    	orderHdIdR = JavaUtils.replaceStringWithDoubleQuotes(orderHdIdR);
		    	  orderHdIdRA=orderHdIdR.split(","); 
			}
		    String mmuName ="";
		    if (jsonObject.has("mmuName")) {
		    	mmuName = jsonObject.get("mmuName").toString();
		    	mmuName = JavaUtils.replaceStringWithDoubleQuotes(mmuName);
		    	 
			}
		
		    String cityName ="";
		    if (jsonObject.has("cityName")) {
		    	cityName = jsonObject.get("cityName").toString();
		    	cityName = JavaUtils.replaceStringWithDoubleQuotes(cityName);
		    	 
			}
		    
		    try {
				//final String uri = "https://2factor.in/API/R1/?module=TRANS_SMS&apikey=5cdc6365-22b5-11ec-a13b-0200cd936042&to="+mobileNo+"&from=CGMSSY&templatename=Apartment&var1="+followUpLocationForSend+"&var2="+followUpLandmarksForSend+"&var3="+followUpDateForSend+"";
				//final String url="https://www.cgmmssy.in/report/generateLabHistoryReport?oderHdId="+orderHdIdR+"";
		    	String urlForMessage = HMSUtil.getProperties("adt.properties", "urlForMessage").trim();
		    	
		    	String url=urlForMessage+"?orderHdId="+orderHdIdRA[0].trim();
		    	StringBuilder sb = new StringBuilder();
				 String sEmailURL = url;
				// sb.append(getFormatTemp(sEmailURL));
			     
				sb.append("Click "+url);
				//final String uri = "https://2factor.in/API/R1/?module=TRANS_SMS&apikey=5cdc6365-22b5-11ec-a13b-0200cd936042&to="+mobileNo+"&from=CGMSSY&templatename=LinkMMU&var1="+URLEncoder.encode(patientName,"UTF8")+"&var2="+URLEncoder.encode(mmuName,"UTF8")+""+URLEncoder.encode(cityName,"UTF8")+"&var3="+URLEncoder.encode(sb.toString(),"UTF8")+"";
			    
		    	final String uri = "https://2factor.in/API/R1/?module=TRANS_SMS&apikey=5cdc6365-22b5-11ec-a13b-0200cd936042&to="+mobileNo+"&from=CGMMSY&templatename=MMUSAMPLEF-New&var1="+patientName+"&var2="+mmuName+"&var3="+sEmailURL+"";
			     // final String uri = "https://2factor.in/API/R1/?module=TRANS_SMS&apikey=5cdc6365-22b5-11ec-a13b-0200cd936042&to="+mobileNo+"&from=CGMSSY&templatename=Report6&var1="+patientName+"&var2="+mmuName+""+cityName+"&var3="+sEmailURL+"";
					HttpResponse<String> resp = Unirest.post(uri).asString();
				//System.out.println(resp.getBody());
				System.out.println("Lab Message send successfully");
				if(resp.getBody()!=null)
				{
					boolean status=resp.getBody().contains("Status");
					if(status==true)
					{
						System.out.println("Message delivered succssfully");
					}
				    
				}
				//return resp.getBody();
				} catch (Exception e) {
					e.printStackTrace();
				}
		    
			
			
			
			List<Long> subInvIds = null;
			List<String> remarksList = new ArrayList<String>();

			subInvIds = new ArrayList<Long>();
			if (jsonObject.has("subInvestigationId") && subInvestigationIdArray.length > 0) {
				for (int i = 0; i < subInvestigationIdArray.length; i++) {
					if (i < subInvestigationIdArray.length) {
						subInvIds.add(Long.parseLong(subInvestigationIdArray[i].toString()));
						if (i < remarksValue.length && remarksValue[i] != null) {
							if (remarksValue[i] != null && !remarksValue[i].toString().equalsIgnoreCase("")) {
								remarksList.add(remarksValue[i].toString());
							} else {
								remarksList.add("");
							}
						} else {
							remarksList.add("");
						}

					}

				}
			} else if (jsonObject.has("parentInvestigationId") && parentInvestigationIdArray.length > 0) {
				for (int i = 0; i < parentInvestigationIdArray.length; i++) {
					if (i < parentInvestigationIdArray.length) {
						subInvIds.add(Long.parseLong(parentInvestigationIdArray[i].toString()));
						if (i < remarksValue[i].length() && remarksValue[i] != null) {
							if (remarksValue[i] != null && !remarksValue[i].toString().equalsIgnoreCase("")) {
								remarksList.add(remarksValue[i].toString());
							} else {
								remarksList.add("");
							}
						} else {
							remarksList.add("");
						}
					}
				}
			}

			List<Long> dtIds = null;
			List<DgResultEntryDt> dtList = null;
			/*
			 * if(subInvIds==null) { dtIds=new ArrayList<Long>();
			 * if(jsonObject.has("InvestigationIdValueParent")) { String
			 * InvestigationIdValueParentf =
			 * jsonObject.get("InvestigationIdValueParent").toString();
			 * InvestigationIdValueParentf =
			 * JavaUtils.replaceStringWithDoubleQuotes(InvestigationIdValueParentf); String
			 * []InvestigationIdValueParentfArray = InvestigationIdValueParentf.split(",");
			 * for(int i=0;i<InvestigationIdValueParentfArray.length;i++) {
			 * 
			 * if(i<InvestigationIdValueParentfArray.length) { dtIds
			 * .add(Long.parseLong(InvestigationIdValueParentfArray[i].toString()));
			 * 
			 * }
			 * 
			 * } }
			 * 
			 * }
			 */
			// dtList = labDao.getAllResultEntDtId(resultEntryHdId,
			// parentInvestigationIdArray);	
			/*
			 * List<Long> dgResultId=new ArrayList<>(); for(DgResultEntryDt dt:dtList) {
			 * dgResultId.add(dt.getResultEntryDetailId()); }
			 */

			boolean flag = labDao.updateResultEntryDetails(resulEntDtIdArray, remarksValue, resultValues, userId,
					subInvestigationIdArray, normalRangeValue, parentInvestigationIdArray);

			List<DgResultEntryDt> dgResultEntryDtIds = labDao.getResultEntDtIds(resultEntryHdId);

			if (flag == true) {
				Integer count = 0;
				for (int i = 0; i < dgResultEntryDtIds.size(); i++) {
					if (dgResultEntryDtIds.get(i).getResultDetailStatus().equalsIgnoreCase("E")) {
						count += 1;
					}
				}
				if (count == 0) {
					boolean upResulEntHd = labDao.updateResultEntryHd(resultEntryHdId, userId);
					if (upResulEntHd == true) {
						json.put("msg", "ResulEntryHd Status has been updated");
						json.put("status", "uDgOrderHdStatus");
					}
				}
			} else {

				json.put("status", 0);
				json.put("msg", "Not Updated");
				json.put("error", "Error in Updated");
			}
		} else {
			json.put("msg", "Invalid Input");
			json.put("status", 0);
		}

		return json.toString();

	}

	@Override
	public String getSampleRejectedWaitingList(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {

		List list = new ArrayList();
		JSONObject json = new JSONObject();

		Map<String, Object> responseData = labDao.getSampleRejectedWaitingList(jsonObject);
		List<DgSampleCollectionDt> dtList = (List<DgSampleCollectionDt>) responseData.get("list");
		int count = (int) responseData.get("count");
		if (CollectionUtils.isNotEmpty(dtList)) {
			for (DgSampleCollectionDt dt : dtList) {
				Map<String, Object> map = new HashMap<String, Object>();
				long subChargeCodeId = dt.getSubcharge();
				long hdId = dt.getSampleCollectionHdId();
				DgSampleCollectionDt dgSampleCollectionDt = labDao.getDgSampleCollectionDt(hdId);
				String collectedDate = HMSUtil.convertDateToStringFormat(
						dgSampleCollectionDt.getDgSampleCollectionHd().getCollectionDate(), "dd/MM/yyyy");
				if (collectedDate != null) {
					map.put("collectedDate", collectedDate);
				} else {
					map.put("collectedDate", "");
				}
				String collectedTime = JavaUtils.getTimeFromDateAndTime(
						dgSampleCollectionDt.getDgSampleCollectionHd().getLastChgDate().toString());
				/*
				 * if (collectedTime != null) { map.put("collectedTime", collectedTime); } else
				 * { map.put("collectedTime" , ""); }
				 */
				/*
				 * if (dgSampleCollectionDt.getDgSampleCollectionHd() != null &&
				 * dgSampleCollectionDt.getDgSampleCollectionHd().getPatient() != null &&
				 * dgSampleCollectionDt.getDgSampleCollectionHd().getPatient().getServiceNo() !=
				 * null) { map.put("serviceNo",
				 * dgSampleCollectionDt.getDgSampleCollectionHd().getPatient().getServiceNo());
				 * } else { map.put("serviceNo", ""); }
				 */
				if (dgSampleCollectionDt.getDgSampleCollectionHd() != null
						&& dgSampleCollectionDt.getDgSampleCollectionHd().getVisitId() != null) {
					map.put("visitId", dgSampleCollectionDt.getDgSampleCollectionHd().getVisitId());
				} else {
					map.put("visitId", "");
				}
				if (dgSampleCollectionDt.getDgSampleCollectionHd() != null
						&& dgSampleCollectionDt.getDgSampleCollectionHd().getDepartmentId() != null) {
					map.put("departmentId", dgSampleCollectionDt.getDgSampleCollectionHd().getDepartmentId());
				} else {
					map.put("departmentId", "");
				}
				if (dgSampleCollectionDt.getDgSampleCollectionHd() != null
						&& dgSampleCollectionDt.getDgSampleCollectionHd().getDgOrderHd() != null
						&& dgSampleCollectionDt.getDgSampleCollectionHd().getDgOrderHd().getMasDepartment() != null
						&& dgSampleCollectionDt.getDgSampleCollectionHd().getDgOrderHd().getMasDepartment()
								.getDepartmentName() != null) {
					map.put("departmentName", dgSampleCollectionDt.getDgSampleCollectionHd().getDgOrderHd()
							.getMasDepartment().getDepartmentName());
				} else {
					map.put("departmentName", "");
				}

				if (dgSampleCollectionDt.getDgSampleCollectionHd() != null
						&& dgSampleCollectionDt.getDgSampleCollectionHd().getSampleCollectionHdId() != null) {
					map.put("sampleCollectionHdId",
							dgSampleCollectionDt.getDgSampleCollectionHd().getSampleCollectionHdId());
				} else {
					map.put("sampleCollectionHdId", "");
				}

				if (dgSampleCollectionDt.getDgSampleCollectionHd() != null
						&& dgSampleCollectionDt.getDgSampleCollectionHd().getPatientId() != null) {
					map.put("patientId", dgSampleCollectionDt.getDgSampleCollectionHd().getPatientId());
				} else {
					map.put("patientId", "");
				}
				if (dgSampleCollectionDt.getDgSampleCollectionHd() != null
						&& dgSampleCollectionDt.getDgSampleCollectionHd().getPatient() != null
						&& dgSampleCollectionDt.getDgSampleCollectionHd().getPatientId() != null) {
					map.put("patientName",
							dgSampleCollectionDt.getDgSampleCollectionHd().getPatient().getPatientName());
				} else {
					map.put("patientName", "");
				}

				/*
				 * if (dgSampleCollectionDt.getDgSampleCollectionHd() != null &&
				 * dgSampleCollectionDt.getDgSampleCollectionHd().getPatient() != null &&
				 * dgSampleCollectionDt.getDgSampleCollectionHd().getPatient().getMasRelation()
				 * != null &&
				 * dgSampleCollectionDt.getDgSampleCollectionHd().getPatient().getMasRelation()
				 * .getRelationName() != null) { map.put("relationName",
				 * dgSampleCollectionDt.getDgSampleCollectionHd().getPatient().getMasRelation()
				 * .getRelationName()); } else { map.put("relationName", ""); }
				 */

				map.put("subchargeCodeId",
						dgSampleCollectionDt.getSubcharge() != null ? dgSampleCollectionDt.getSubcharge() : "");
				map.put("subChargecodeName",
						dgSampleCollectionDt.getMasSubChargecode().getSubChargecodeName() != null
								? dgSampleCollectionDt.getMasSubChargecode().getSubChargecodeName()
								: "");
				map.put("sampleCollectionDtId",
						dgSampleCollectionDt.getSampleCollectionDtId() != null
								? dgSampleCollectionDt.getSampleCollectionDtId()
								: "");
				map.put("diagNo",
						dgSampleCollectionDt.getDgSampleCollectionHd() != null
								&& dgSampleCollectionDt.getDgSampleCollectionHd().getDgOrderHd() != null
								&& dgSampleCollectionDt.getDgSampleCollectionHd().getDgOrderHd().getOrderNo() != null
										? dgSampleCollectionDt.getDgSampleCollectionHd().getDgOrderHd().getOrderNo()
										: "");

				if (dgSampleCollectionDt.getDgMasInvestigation() != null
						&& dgSampleCollectionDt.getDgMasInvestigation().getInvestigationType() != null) {
					map.put("investigationType", dgSampleCollectionDt.getDgMasInvestigation().getInvestigationType());
				} else {
					map.put("investigationType", "");
				}

				list.add(map);
			}
			if (list != null && !list.isEmpty()) {
				json.put("data", list);
				json.put("msg", "Record Fetch Successfully");
				json.put("count", count);
				json.put("status", 1);
			} else {
				json.put("data", list);
				json.put("count", 0);
				json.put("msg", "No Record Found");
				json.put("status", 0);
			}
		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}

		return json.toString();

	}

	@Override
	public String getSampleRejectedDetails(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {

		JSONObject json = new JSONObject();
		List<DgSampleCollectionHd> dgSampleCollectionHds = null;
		List<DgSampleCollectionDt> dgSampleCollectionDts = null;
		Map<String, Object> map = new HashMap<String, Object>();

		List list = new ArrayList();
		List list1 = new ArrayList();
		// List list2 = new ArrayList();

		if (jsonObject != null) {
			Map<String, Object> mapobject = labDao.getSampleRejectedDetails(jsonObject);
			List<DgSampleCollectionDt> dgSampleCollectionDtsValidateSampleList = labDao
					.getAllRejectedInvestigations(jsonObject);

			if (mapobject.get("dgSampleCollectionDtsList") != null) {
				dgSampleCollectionDts = (List<DgSampleCollectionDt>) mapobject.get("dgSampleCollectionDtsList");
				for (Iterator<?> iterator = dgSampleCollectionDts.iterator(); iterator.hasNext();) {
					DgSampleCollectionDt dt = new DgSampleCollectionDt();
					dt = (DgSampleCollectionDt) iterator.next();

					if (dt.getSampleCollectionHdId() != null) {
						map.put("sampleCollectionHdId", dt.getSampleCollectionHdId());
					} else {
						map.put("sampleCollectionHdId", "");
					}
					if (dt.getDgSampleCollectionHd().getCollectionDate() != null) {
						String sampleDateTime = HMSUtil.convertDateToStringFormat(
								dt.getDgSampleCollectionHd().getCollectionDate(), "dd/MM/yyyy");
						map.put("sampleDateTime", sampleDateTime);
					} else {
						map.put("sampleDateTime", "");
					}

					if (dt.getDgSampleCollectionHd().getCollectionDate() != null) {
						map.put("orderTime", JavaUtils
								.getTimeFromDateAndTime(dt.getDgSampleCollectionHd().getCollectionDate().toString()));
						// map.put("orderTime",
						// HMSUtil.convertStringTypeDateToDateType(dt.getDgSampleCollectionHd().getCollectionDate().toString()));

					} else {
						map.put("orderTime", "");
					}

					if (dt.getDgSampleCollectionHd() != null && dt.getDgSampleCollectionHd().getDgOrderHd() != null
							&& dt.getDgSampleCollectionHd().getDgOrderHd().getOrderNo() != null) {
						map.put("orderNumber", dt.getDgSampleCollectionHd().getDgOrderHd().getOrderNo());
					} else {
						map.put("orderNumber", "");
					}
					/*
					 * if (dt.getDgSampleCollectionHd().getDgOrderHd() != null &&
					 * dt.getDgSampleCollectionHd().getDgOrderHd().getVisit() != null &&
					 * dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getPatient() != null
					 * && dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getPatient()
					 * .getServiceNo() != null) { map.put("serviceNo",
					 * dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getPatient().
					 * getServiceNo()); } else { map.put("serviceNo", ""); }
					 */
					/*
					 * if (dt.getDgSampleCollectionHd().getDgOrderHd() != null &&
					 * dt.getDgSampleCollectionHd().getDgOrderHd().getVisit() != null &&
					 * dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getPatient() != null
					 * && dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getPatient()
					 * .getEmployeeName() != null) { map.put("employeeName",
					 * dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getPatient().
					 * getEmployeeName()); } else { map.put("employeeName", ""); }
					 */
					if (dt.getDgSampleCollectionHd().getDgOrderHd() != null
							&& dt.getDgSampleCollectionHd().getDgOrderHd().getVisit() != null
							&& dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getPatient() != null
							&& dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getPatient()
									.getPatientName() != null) {
						map.put("patientName",
								dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getPatient().getPatientName());
					} else {
						map.put("patientName", "");
					}

					/*
					 * if (dt.getDgSampleCollectionHd().getDgOrderHd() != null &&
					 * dt.getDgSampleCollectionHd().getDgOrderHd().getVisit() != null &&
					 * dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getPatient() != null
					 * && dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getPatient()
					 * .getMasRelation() != null &&
					 * dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getPatient().
					 * getMasRelation() .getRelationId() != null) { map.put("relationName",
					 * dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getPatient()
					 * .getMasRelation().getRelationName()); } else { map.put("relationName", ""); }
					 */

					if (dt.getDgSampleCollectionHd().getDgOrderHd() != null
							&& dt.getDgSampleCollectionHd().getDgOrderHd().getVisit() != null
							&& dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getPatient() != null
							&& dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getPatient()
									.getDateOfBirth() != null) {
						Date dateOfBirth = dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getPatient()
								.getDateOfBirth();
						// String age = JavaUtils.calculateAgefromDob(dateOfBirth);
						// map.put("age", age);
						Date visitDate = null;
						if (dt.getDgSampleCollectionHd().getDgOrderHd() != null
								&& dt.getDgSampleCollectionHd().getDgOrderHd().getVisit() != null
								&& dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getVisitDate() != null) {
							visitDate = dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getVisitDate();
						}
						if (visitDate != null && dateOfBirth != null) {
							map.put("age", HMSUtil.getDateBetweenTwoDate(visitDate, dateOfBirth));
						} else {
							map.put("age", "");
						}

					} else {
						map.put("age", "");
					}
					if (dt.getDgSampleCollectionHd().getDgOrderHd() != null
							&& dt.getDgSampleCollectionHd().getDgOrderHd().getVisit() != null
							&& dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getPatient() != null
							&& dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getPatient()
									.getMasAdministrativeSex() != null
							&& dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getPatient()
									.getMasAdministrativeSex().getAdministrativeSexName() != null) {
						map.put("gender", dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getPatient()
								.getMasAdministrativeSex().getAdministrativeSexName());
					} else {
						map.put("gender", "");
					}

					if (dt.getDgSampleCollectionHd().getDgOrderHd() != null
							&& dt.getDgSampleCollectionHd().getDgOrderHd().getVisit() != null
							&& dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getMasDepartment() != null
							&& dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getMasDepartment()
									.getDepartmentName() != null) {
						map.put("departmentName", dt.getDgSampleCollectionHd().getDgOrderHd().getVisit()
								.getMasDepartment().getDepartmentName());
					} else {
						map.put("departmentName", "");
					}

					if (dt.getDgSampleCollectionHd().getPatient() != null
							&& dt.getDgSampleCollectionHd().getPatient().getPatientId() != null) {
						map.put("patientId", dt.getDgSampleCollectionHd().getPatient().getPatientId());
					} else {
						map.put("patientId", null);
					}

					if (dt.getDgSampleCollectionHd().getDgOrderHd() != null
							&& dt.getDgSampleCollectionHd().getDgOrderHd().getVisit() != null
							&& dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getMasDepartment() != null
							&& dt.getDgSampleCollectionHd().getDgOrderHd().getVisit().getMasDepartment()
									.getDepartmentId() != 0) {
						map.put("departmentId", dt.getDgSampleCollectionHd().getDgOrderHd().getVisit()
								.getMasDepartment().getDepartmentId());
					} else {
						map.put("departmentId", null);
					}
					if (dt.getDgSampleCollectionHd().getDgOrderHd().getOrderDate() != null) {
						map.put("orderDate", HMSUtil.convertDateToStringFormat(
								dt.getDgSampleCollectionHd().getDgOrderHd().getOrderDate(), "dd/MM/yyyy"));
					} else {
						map.put("orderDate", "");
					}

					if (dt.getDgSampleCollectionHd().getVisitId() != 0) {
						map.put("visitId", dt.getDgSampleCollectionHd().getVisitId());
					} else {
						map.put("visitId", null);
					}

					if (dt.getSubcharge() != null) {
						map.put("subchargeCodeId", dt.getSubcharge());
					} else {
						map.put("subchargeCodeId", null);
					}

					if (dt.getMasSubChargecode().getSubChargecodeName() != null) {
						map.put("subchargeCodeName", dt.getMasSubChargecode().getSubChargecodeName());
					} else {
						map.put("subchargeCodeName", null);
					}

					/*
					 * if (dt.getDgSampleCollectionHd() != null &&
					 * dt.getDgSampleCollectionHd().getPatient() != null &&
					 * dt.getDgSampleCollectionHd().getPatient().getMasRank() != null &&
					 * dt.getDgSampleCollectionHd().getPatient().getMasRank().getRankName() != null)
					 * { map.put("rankName",
					 * dt.getDgSampleCollectionHd().getPatient().getMasRank().getRankName()); } else
					 * { map.put("rankName", null); }
					 */
					if (dt.getDgSampleCollectionHd() != null && dt.getDgSampleCollectionHd().getPatient() != null
							&& dt.getDgSampleCollectionHd().getPatient().getUhidNo() != null) {
						map.put("regNo", dt.getDgSampleCollectionHd().getPatient().getUhidNo());
					} else {
						map.put("regNo", "");
					}
					if (dt.getDgSampleCollectionHd() != null && dt.getDgSampleCollectionHd().getDgOrderHd() != null
							&& dt.getDgSampleCollectionHd().getDgOrderHd().getUsers().getLoginName() != null) {
						map.put("validatedBy", dt.getDgSampleCollectionHd().getDgOrderHd().getUsers().getLoginName());
					} else {
						map.put("validatedBy", "");
					}

					/* SAMPLE DETAILS */
					LocalDate currentDate1 = LocalDate.now();
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
					String currentDate = formatter.format(currentDate1);
					String currTime = LocalTime.now().toString();
					String[] currTime1 = currTime.split("\\.");
					String currentTime = currTime1[0];

					map.put("currentDate", currentDate);
					map.put("currentTime", currentTime);

					if (dt.getAdditionalRemarks() != null) {
						map.put("additionalRemarks", dt.getAdditionalRemarks());
					} else {
						map.put("additionalRemarks", "");
					}
				}

				list.add(map);
			}

			/******************************************
			 * Investigation List of Rejected Sample Validation
			 **************************************/

			/**
			 * @Description : Investigation List of Pending Sample Validation Details
			 * @param:jsonObject
			 * @return
			 */
			if (dgSampleCollectionDtsValidateSampleList != null && dgSampleCollectionDtsValidateSampleList.size() > 0) {

				for (DgSampleCollectionDt dt : dgSampleCollectionDtsValidateSampleList) {
					Map<String, Object> map1 = new HashMap<String, Object>();

					if (dt.getSampleCollectionDtId() != 0) {
						map1.put("sampleCollectionDtId", dt.getSampleCollectionDtId());
					} else {
						map1.put("sampleCollectionDtId", "");
					}

					if (dt.getDgSampleCollectionHd() != null && dt.getDgSampleCollectionHd().getDgOrderHd() != null
							&& dt.getDgSampleCollectionHd().getDgOrderHd().getOrderNo() != null) {
						map1.put("digNo", dt.getDgSampleCollectionHd().getDgOrderHd().getOrderNo());
					} else {
						map1.put("digNo", "");
					}

					if (dt.getInvestigationId() != null) {
						map1.put("investigationId", dt.getInvestigationId());
					} else {
						map1.put("investigationId", "");
					}

					if (dt.getDgMasInvestigation() != null
							&& dt.getDgMasInvestigation().getInvestigationName() != null) {
						map1.put("investigationName", dt.getDgMasInvestigation().getInvestigationName());
					} else {
						map1.put("investigationName", "");
					}

					if (dt.getSampleId() != null) {
						map1.put("sampleId", dt.getSampleId());
					} else {
						map1.put("sampleId", "");
					}

					if (dt.getMasSample() != null && dt.getMasSample().getSampleDescription() != null) {
						map1.put("sampleDesc", dt.getMasSample().getSampleDescription());
					} else {
						map1.put("sampleDesc", "");
					}

					if (dt.getSampleCollDatetime() != null) {
						map1.put("sampleCollDate",
								HMSUtil.convertDateToStringFormat(dt.getSampleCollDatetime(), "dd/MM/yyyy"));
					} else {
						map1.put("sampleCollDate", "");
					}

					if (dt.getSubcharge() != null) {
						map1.put("subchargeCodeId", dt.getSubcharge());
					} else {
						map1.put("subchargeCodeId", null);
					}

					if (dt.getMasSubChargecode() != null && dt.getMasSubChargecode().getSubChargecodeName() != null) {
						map1.put("subchargeCodeName", dt.getMasSubChargecode().getSubChargecodeName());
					} else {
						map1.put("subchargeCodeName", null);
					}

					if (dt.getReason() != null) {
						map1.put("reason", dt.getReason());
					} else {
						map1.put("reason", null);
					}

					if (dt.getAdditionalRemarks() != null) {
						map1.put("additionalRemarks", dt.getAdditionalRemarks());
					} else {
						map1.put("additionalRemarks", "");
					}
					list1.add(map1);
				}

			}

			json.put("data", list);
			json.put("data1", list1);
			json.put("count", list.size());
			json.put("count1", list1.size());
			json.put("msg", "Record get Successfully");

		} else {
			json.put("status", 0);
			json.put("msg", "Invalid Input");
		}

		return json.toString();
	}

	@Override
	public String submitSampleRejectedDetails(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {

		JSONObject json = new JSONObject();
		String[] reasonArr = null;
		String[] additionalRemarksArr = null;
		if (jsonObject != null) {

			Long subchargeCodeId = null;
			Long sampleCollectionHdId = null;
			String acceptedChecks = "";
			String[] acceptedChecksArray = null;
			String[] a_subchargeCodeIdArr = null;

			if (jsonObject.has("acceptedCheckBox")) {
				if (jsonObject.get("acceptedCheckBox").toString() != null) {
					acceptedChecks = JavaUtils.getReplaceString1(jsonObject.get("acceptedCheckBox").toString());
					acceptedChecks = acceptedChecks.replaceAll("\"", "");
					acceptedChecksArray = acceptedChecks.split(",");
				}
			}

			if (jsonObject.get("sampleCollectionHdId").toString() != null) {
				String sampleCollectionHdIds = JavaUtils
						.getReplaceString1(jsonObject.get("sampleCollectionHdId").toString());
				sampleCollectionHdIds = sampleCollectionHdIds.replaceAll("\"", "");
				sampleCollectionHdId = Long.parseLong(sampleCollectionHdIds);
			}

			if (jsonObject.has("subchargeCodeIdArray")) {
				if (jsonObject.get("subchargeCodeIdArray").toString() != null) {
					String subchargeCodeIds = jsonObject.get("subchargeCodeIdArray").toString();
					subchargeCodeIds = JavaUtils.getReplaceString1(subchargeCodeIds);
					subchargeCodeIds = subchargeCodeIds.replaceAll("\"", "");
					a_subchargeCodeIdArr = subchargeCodeIds.split(",");
				}
			}

			if (jsonObject.has("reason")) {
				if (jsonObject.get("reason").toString() != null) {
					String reason = jsonObject.get("reason").toString();
					reason = JavaUtils.getReplaceString1(reason);
					reason = reason.replaceAll("\"", "");
					reasonArr = reason.split(",");
				}
			}

			if (jsonObject.has("additionalRemarks")) {
				if (jsonObject.get("additionalRemarks").toString() != null) {
					String additionalRemarks = jsonObject.get("additionalRemarks").toString();
					additionalRemarks = JavaUtils.getReplaceString1(additionalRemarks);
					additionalRemarks = additionalRemarks.replaceAll("\"", "");
					additionalRemarksArr = additionalRemarks.split(",");

				}
			}

			String[] acceptedInvestigations = null;
			if (jsonObject.has("investigationsArray")) {
				if (jsonObject.get("investigationsArray").toString() != null) {
					String investIDs = jsonObject.get("investigationsArray").toString();
					investIDs = JavaUtils.getReplaceString1(investIDs);
					investIDs = investIDs.replaceAll("\"", "");
					acceptedInvestigations = investIDs.split(",");

				}
			}

			String acceptflag = "";
			if (acceptedChecksArray.length > 0 && !acceptedChecksArray[0].equalsIgnoreCase("")) {
				for (int i = 0; i < acceptedChecksArray.length; i++) {
					if (acceptedChecksArray[i].equalsIgnoreCase("y")) {
						if (sampleCollectionHdId != null) {
							//System.out.println("ainvestIDArray :: " + acceptedInvestigations[i]);
							acceptflag = "A";
							// update
							List<DgSampleCollectionDt> sampleCollectionDtIds = labDao.getSampleCollectionDtsIdForReject(
									sampleCollectionHdId, Long.parseLong(a_subchargeCodeIdArr[i]),
									Long.parseLong(acceptedInvestigations[i]));
							if (!sampleCollectionDtIds.isEmpty()) {
								// boolean status =
								// labDao.updateDgSampleCollectionDtForReject(sampleCollectionDtIds, reasonArr,
								// additionalRemarksArr, acceptflag);
								boolean status = labDao.updateDgSampleCollectionDt(sampleCollectionDtIds, reasonArr,
										additionalRemarksArr, acceptflag);
								if (status) {
									json.put("msg", "Record Updated Successfully");
								}

							}
						}
					}
				}
			}

		} else {
			json.put("msg", "Invalid Input");
			json.put("status", 0);
		}

		return json.toString();

	}

	@SuppressWarnings("unchecked")
	@Override
	public String getLabHistoryDetails(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject jsonObj = new JSONObject();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, Object> mapData = labDao.getLabHistoryDetails(jsondata);

			if (mapData.get("count") != null) {

				List<DgResultEntryDt> detailsList = (List<DgResultEntryDt>) mapData.get("dtsList");
				Integer count = (Integer) mapData.get("count");

				//System.out.println("size=" + detailsList.size());

				if (detailsList != null && detailsList.size() > 0) {
					for (DgResultEntryDt dt : detailsList) {

						// Map<String, Object> mapobj = new HashMap<String, Object>();

						if (dt.getResult() != null && StringUtils.isNotEmpty(dt.getResult())) {

							Map<String, Object> mapobj = new HashMap<String, Object>();
							if (dt.getDgResultEntryHd().getResultEntryId() != null) {
								mapobj.put("resultEntHdId", dt.getDgResultEntryHd().getResultEntryId());
							} else {
								mapobj.put("resultEntHdId", "");
							}

							
							  if (dt.getResultEntryDetailId() != null) { mapobj.put("resultEntDtId",
							  dt.getResultEntryDetailId()); } else { mapobj.put("resultEntDtId", ""); }
							  
							  if (dt.getDgResultEntryHd() != null && dt.getDgResultEntryHd().getOrderHdId()
							  != null) { mapobj.put("orderHdId", dt.getDgResultEntryHd().getOrderHdId()); }
							  else { mapobj.put("orderHdId", ""); }
							  
							  if (dt.getOrderDtId() != null) { mapobj.put("orderDtId", dt.getOrderDtId());
							  } else { mapobj.put("orderDtId", ""); }
							  
							  if (dt.getDgResultEntryHd() != null && dt.getDgResultEntryHd().getPatientId()
							  != null) { mapobj.put("patientId", dt.getDgResultEntryHd().getPatientId()); }
							  else { mapobj.put("patientId", ""); }
							  
							  if (dt.getDgResultEntryHd() != null && dt.getDgResultEntryHd().getPatient()
							  != null && dt.getDgResultEntryHd().getPatient().getPatientName() != null) {
							  mapobj.put("patientName",
							  dt.getDgResultEntryHd().getPatient().getPatientName()); } else {
							  mapobj.put("patientName", ""); }
							  
							  
						/*	  if (dt.getDgResultEntryHd() != null && dt.getDgResultEntryHd().getPatient()
							  != null && dt.getDgResultEntryHd().getPatient().getRelationId() != null) {
							  mapobj.put("relationId",
							  dt.getDgResultEntryHd().getPatient().getRelationId()); } else {
							  mapobj.put("relationId", ""); }*/
							  
							  
							  
							/*  if (dt.getDgResultEntryHd() != null && dt.getDgResultEntryHd().getPatient()
							  != null && dt.getDgResultEntryHd().getPatient().getMasRelation() != null &&
							  dt.getDgResultEntryHd().getPatient().getMasRelation() .getRelationName() !=
							  null) { mapobj.put("relationName",
							  dt.getDgResultEntryHd().getPatient().getMasRelation().getRelationName()); }
							  else { mapobj.put("relationName", ""); }*/
							  
							  
							  if (dt.getDgResultEntryHd() != null && dt.getDgResultEntryHd().getPatient()
							  != null && dt.getDgResultEntryHd().getPatient().getDateOfBirth() != null) {
							  // mapobj.put("age",JavaUtils.calculateAgefromDob(dt.getDgResultEntryHd().							  getPatient().getDateOfBirth())); 
								  Date date =
							  dt.getDgResultEntryHd().getPatient().getDateOfBirth(); Date visitDate = null;
							  if (dt.getDgResultEntryHd().getVisit() != null &&
							  dt.getDgResultEntryHd().getVisit().getVisitDate() != null) { visitDate =
							  dt.getDgResultEntryHd().getVisit().getVisitDate(); } if (visitDate != null &&
							  date != null) { mapobj.put("age", HMSUtil.getDateBetweenTwoDate(visitDate,
							  date)); } else { mapobj.put("age", ""); }
							  
							  } else { mapobj.put("age", ""); }
							  
							  if (dt.getDgResultEntryHd() != null && dt.getDgResultEntryHd().getPatient()
							  != null && dt.getDgResultEntryHd().getPatient().getAdministrativeSexId() !=
							  null) { mapobj.put("genderId",
							  dt.getDgResultEntryHd().getPatient().getAdministrativeSexId()); } else {
							  mapobj.put("genderId", ""); }
							  
							  if (dt.getDgResultEntryHd() != null && dt.getDgResultEntryHd().getPatient()
							  != null && dt.getDgResultEntryHd().getPatient().getMasAdministrativeSex() !=
							  null && dt.getDgResultEntryHd().getPatient().getMasAdministrativeSex()
							  .getAdministrativeSexName() != null) { mapobj.put("genderName",
							  dt.getDgResultEntryHd().getPatient().getMasAdministrativeSex()
							  .getAdministrativeSexName()); } else { mapobj.put("genderName", ""); }
							  
							  if (dt.getDgResultEntryHd() != null && dt.getDgResultEntryHd().getPatient()
							  != null && dt.getDgResultEntryHd().getPatient().getMasAdministrativeSex() !=
							  null && dt.getDgResultEntryHd().getPatient().getMasAdministrativeSex()
							  .getAdministrativeSexCode() != null) { mapobj.put("genderCode",
							  dt.getDgResultEntryHd().getPatient().getMasAdministrativeSex()
							  .getAdministrativeSexCode()); } else { mapobj.put("genderCode", ""); }
							  
							  if (dt.getDgResultEntryHd() != null && dt.getDgResultEntryHd().getMmuId() !=
							  null) { mapobj.put("hospitalId", dt.getDgResultEntryHd().getMmuId()); } else
							  { mapobj.put("hospitalId", ""); }
							  
							  if (dt.getDgResultEntryHd() != null && dt.getDgResultEntryHd().getMasMmu() !=
							  null && dt.getDgResultEntryHd().getMasMmu()!= null) {
							  mapobj.put("hospitalName", dt.getDgResultEntryHd().getMasMmu().getMmuName());
							  } 
							  else { mapobj.put("hospitalName", ""); }
							  
							  if (dt.getDgResultEntryHd() != null &&
							  dt.getDgResultEntryHd().getResultDate() != null) {
							  mapobj.put("investigationDate", HMSUtil.convertDateToStringFormat(
							  dt.getDgResultEntryHd().getResultDate(), "dd/MM/yyyy")); } else {
							  mapobj.put("investigationDate", ""); }
							  
							  if (dt.getInvestigationId() != null) { mapobj.put("investigationId",
							  dt.getInvestigationId()); } else { mapobj.put("investigationId", ""); }
							  
							  if (dt.getDgMasInvestigation() != null &&
							  dt.getDgMasInvestigation().getInvestigationName() != null) {
							  mapobj.put("investigationName",
							  dt.getDgMasInvestigation().getInvestigationName()); } else {
							  mapobj.put("investigationName", ""); }
							  
							  if (dt.getDgSubMasInvestigation() != null &&
							  dt.getDgSubMasInvestigation().getSubInvestigationName() != null) {
							  mapobj.put("subInvestigationName",
							  dt.getDgSubMasInvestigation().getSubInvestigationName()); } else {
							  mapobj.put("subInvestigationName", ""); }
							  
							  if (dt.getDgMasInvestigation() != null &&
							  dt.getDgMasInvestigation().getMasUOM() != null &&
							  dt.getDgMasInvestigation().getMasUOM().getUOMName() != null) {
							  mapobj.put("unit", dt.getDgMasInvestigation().getMasUOM().getUOMName()); }
							  else if (dt.getDgSubMasInvestigation() != null &&
							  dt.getDgSubMasInvestigation().getMasUOM() != null &&
							  dt.getDgSubMasInvestigation().getMasUOM().getUOMName() != null) {
							  mapobj.put("unit", dt.getDgSubMasInvestigation().getMasUOM().getUOMName()); }
							  else { mapobj.put("unit", ""); }
							  
							  if (dt.getResult() != null && dt.getResult() != "") {
							  mapobj.put("investigationResult", dt.getResult()); } else {
							  mapobj.put("investigationResult", ""); }
							  
							  if (dt.getLastChgDate() != null) { mapobj.put("lastChgDate",
							  HMSUtil.convertDateToStringFormat(dt.getLastChgDate(), "dd/MM/yyyy")); } else
							  { mapobj.put("lastChgDate", ""); }
							  
							  if (dt.getRangeValue() != null && dt.getRangeValue() != "") {
							  mapobj.put("range", dt.getRangeValue()); } else { mapobj.put("range", ""); }
							  
							  
							/*
							 * if (dt.getDgResultEntryHd() != null && dt.getDgResultEntryHd().getPatient()
							 * != null && dt.getDgResultEntryHd().getPatient().getServiceNo() != null) {
							 * mapobj.put("serviceNo", dt.getDgResultEntryHd().getPatient().getServiceNo());
							 * } else { mapobj.put("serviceNo", ""); }
							 */
							  
							  
							  if (dt.getDgResultEntryHd().getPatient().getMobileNumber() != null) {
							  mapobj.put("mobileNo",
							  dt.getDgResultEntryHd().getPatient().getMobileNumber()); } else {
							  mapobj.put("mobileNo", ""); }
							  
							  if (dt.getDgResultEntryHd() != null &&
							  dt.getDgResultEntryHd().getResultVerified() != null &&
							  dt.getDgResultEntryHd().getResultVerified().getUserName() != null) {
							  mapobj.put("resultVerifiedBy",
							  dt.getDgResultEntryHd().getResultVerified().getUserName()); } else {
							  mapobj.put("resultVerifiedBy", ""); }
							  
							  if (dt.getDgResultEntryHd() != null && dt.getDgResultEntryHd().getUsers() !=
							  null && dt.getDgResultEntryHd().getUsers().getUserName() != null) {
							  mapobj.put("createdBy", dt.getDgResultEntryHd().getUsers().getUserName()); }
							  else { mapobj.put("createdBy", ""); }
							  
							  if (dt.getResultDetailStatus() != null) { mapobj.put("resultDetailStatus",
							  dt.getResultDetailStatus()); } else { mapobj.put("resultDetailStatus", ""); }
							  
							  if (dt.getValidated() != null) { mapobj.put("validated", dt.getValidated());
							  } else { mapobj.put("validated", ""); }
							  
							  if (dt.getResultType() != null) { mapobj.put("resultType",
							  dt.getResultType()); } else { mapobj.put("resultType", ""); } if
							  (dt.getRangeStatus() != null) { mapobj.put("rangeStatus",
							  dt.getRangeStatus()); } else { mapobj.put("rangeStatus", ""); }
							 
							list.add(mapobj);
						}

					}

					jsonObj.put("msg", "Record Fetch Successfully");
					jsonObj.put("data", list);
					jsonObj.put("count", count);

				}
			}

			else {
				jsonObj.put("status", 0);
				jsonObj.put("msg", "No Record Found");
			}
		}

		else {
			jsonObj.put("status", 0);
			jsonObj.put("msg", "Error occured");
		}

		/*
		 * if (mapData != null && mapData.get("status") != null) { String status =
		 * (String) mapData.get("status"); if (status.equalsIgnoreCase("success")) {
		 * jsonObj.put("successStatus", status); jsonObj.put("status", 1);
		 * jsonObj.put("msg", "Authenticate user"); } else { jsonObj.put("status", 0);
		 * jsonObj.put("msg", "Error occured"); }
		 * 
		 * } else { jsonObj.put("status", 0); jsonObj.put("msg", "Error occured"); } }
		 */
		return jsonObj.toString();
	}

	@Override
	public String checkAuthenticateUser(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject jsonObj = new JSONObject();

		if (jsondata != null) {

			String data = labDao.checkAuthenticateUser(jsondata);
			if (data != null && data.equalsIgnoreCase("success")) {
				jsonObj.put("data", data);
				jsonObj.put("status", 1);
				jsonObj.put("msg", "Authenticate user");

			} else {
				jsonObj.put("data", data);
				jsonObj.put("status", 0);
				jsonObj.put("msg", "Error occured");
			}
		}
		return jsonObj.toString();
	}

	@Override
	public String checkAuthenticateServiceNo(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject jsonObj = new JSONObject();
		if (jsondata != null) {

			String data = labDao.checkAuthenticateServiceNo(jsondata);
			if (data != null && data.equalsIgnoreCase("success")) {
				jsonObj.put("data", data);
				jsonObj.put("status", 1);
				jsonObj.put("msg", "Authenticate Service No");

			} else {
				jsonObj.put("data", data);
				jsonObj.put("status", 0);
				jsonObj.put("msg", "Service No is not valid");
			}
		}
		return jsonObj.toString();
	}

	@Override
	public String autheniticateUHID(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getInvestigationList(Map<String, Object> requestData) {

		Map<String, Object> map = new HashMap<>();
		Map<String, Object> response = new HashMap<String, Object>();
		List<Map<String, Object>> invList = new ArrayList<Map<String, Object>>();
		try {

			List<DgMasInvestigation> list = labDao.getInvestigationList(requestData);
			if (!list.isEmpty()) {
				for (DgMasInvestigation inv : list) {
					Map<String, Object> map2 = new HashMap<>();
					map2.put("invId", inv.getInvestigationId());
					map2.put("invName", inv.getInvestigationName());
					invList.add(map2);
				}
				response.put("status", true);
				response.put("list", invList);
			} else {
				response.put("status", true);
				response.put("list", invList);
			}
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.put("status", false);
		response.put("list", invList);
		return response;
	
	}
public String getFormatTemp(String sEmailURL) {
	
	 StringBuilder sb = new StringBuilder();
    sb.append("<table>");
    sb.append("<tr>");
    sb.append("<td style=font-family: Century Gothic,Open Sans, sans-serif;font-size: 16px;>");
    sb.append("<a href='" + sEmailURL + "'>Click Here to Resend the Email Offer Ready</a><br/><br/>");
    sb.append("</td>");
    sb.append("</tr>");
    sb.append("</table>");
return sb.toString();
  
	
}
	
}
