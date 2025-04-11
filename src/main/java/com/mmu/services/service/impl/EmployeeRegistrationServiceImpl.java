package com.mmu.services.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.mmu.services.dao.EmployeeRegistrationDao;
import com.mmu.services.entity.Attendance;
import com.mmu.services.entity.AuditAttendance;
import com.mmu.services.entity.AuditAttendanceData;
import com.mmu.services.entity.EmployeeDocument;
import com.mmu.services.entity.EmployeeQualification;
import com.mmu.services.entity.EmployeeRegistration;
import com.mmu.services.entity.MasCamp;
import com.mmu.services.entity.MasIdentificationType;
import com.mmu.services.entity.MasUserType;
import com.mmu.services.service.EmployeeRegistrationService;
import com.mmu.services.utils.HMSUtil;

import java.time.*;
import java.time.Month;
import java.time.temporal.ChronoField;

@Service("EmployeeRegistrationService")
public class EmployeeRegistrationServiceImpl implements EmployeeRegistrationService {

	@Autowired
	EmployeeRegistrationDao empRegistrationDao;
	
	@Autowired
	private Environment environment;

	@Override
	public Map<String, Object> saveEmployee(Map<String, Object> requestData) {

		Map<String, Object> map = new HashMap<>();
		try {
			map = empRegistrationDao.saveEmployee(requestData);

			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	@Override
	public String savedEmployeeList(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		Map<String, Object> map = empRegistrationDao.getEmpList(jsondata);
		int count = (int) map.get("count");
		List<EmployeeRegistration> getList = (List<EmployeeRegistration>) map.get("list");

		if (getList.size() == 0) {
			return "{\"status\":\"0\",\"msg\":\"Data not found\"}";
		} else {

			List<HashMap<String, Object>> c = new ArrayList<HashMap<String, Object>>();

			try {
				for (EmployeeRegistration emp : getList) {

					HashMap<String, Object> empRec = new HashMap<String, Object>();

					empRec.put("empId", emp.getEmployeeId());
					empRec.put("empName", emp.getEmployeeName());
					empRec.put("gender", emp.getMasAdministrativeSex().getAdministrativeSexName());
					empRec.put("dob", formatDateYYYYMMDD(emp.getDob().toString()));

					empRec.put("mobileNo", emp.getMobileNo());
					empRec.put("mmu", emp.getMmuId().getMmuName());
					empRec.put("empType", emp.getEmploymentType());

					empRec.put("empType", emp.getUserTypeId().getUserTypeName());

					String recordStatus = "";
					boolean apmApproved = false;
					boolean apmRejected = false;
					boolean audApproved = false;
					boolean audRejected = false;

					/*
					 * if(emp.getRecordStatus() != null && !emp.getRecordStatus().equals("")) {
					 * recordStatus = emp.getRecordStatus(); } else
					 */
					if (emp.getRecordStatus().equalsIgnoreCase("submitted")) {
						
						if (emp.getApmFlag() != null && !emp.getApmFlag().equals("")){
							if (emp.getApmFlag().equals("A")){
								apmApproved = true;
							} else {
								apmRejected = true;
							}
						}

						if (emp.getAuditorFlag() != null && !emp.getAuditorFlag().equals("")){
							if (emp.getAuditorFlag().equals("A")){
								audApproved = true;
							} else {
								audRejected = true;
							}
						}
						
						if(apmApproved && audApproved) {
							recordStatus = "Approved By APM/Auditor";
						} else if(apmApproved && audRejected) {
							recordStatus = "Rejected By Auditor";
						} else if(audApproved && apmRejected) {
							recordStatus = "Rejected By APM";
						} else if(apmApproved && !audApproved) {
							recordStatus = "Approved By APM";
						}else if(audApproved && !apmApproved) {
							recordStatus = "Approved By Auditor";
						} else if(!audApproved && !apmApproved) {
							recordStatus = "Pending at APM/Auditor";
						} else if(apmRejected && audRejected) {
							recordStatus = "Rejected By APM/Auditor";
						} 
						
						/*
						 * if (emp.getApmFlag() != null && !emp.getApmFlag().equals("") &&
						 * emp.getAuditorFlag() != null && !emp.getAuditorFlag().equals("")) { if
						 * (emp.getApmFlag().equals("R") && emp.getAuditorFlag().equalsIgnoreCase("A"))
						 * { recordStatus = "Rejected By APM"; } else if (emp.getApmFlag().equals("A")
						 * && emp.getAuditorFlag().equalsIgnoreCase("R")) { recordStatus =
						 * "Rejected By Auditor"; } else if (emp.getApmFlag().equals("A") &&
						 * emp.getAuditorFlag().equalsIgnoreCase("A")) { recordStatus =
						 * "Approved By APM/Auditor"; }
						 * 
						 * } else { recordStatus = "Pending at APM/Auditor"; }
						 */

						if (emp.getChmoFlag() != null && !emp.getChmoFlag().equals("")) {
							if (emp.getChmoFlag().equals("A")) {
								recordStatus = "Approved By CHMO";
							} else {
								recordStatus = "Rejected By CHMO";
							}
						}

						if (emp.getUpssFlag() != null && !emp.getUpssFlag().equals("")) {
							if (emp.getUpssFlag().equals("A")) {
								recordStatus = "Approved By UPSS";
							} else {
								recordStatus = "Rejected By UPSS";
							}
						}

					} else if (emp.getRecordStatus() != null && !emp.getRecordStatus().equals("")
							&& emp.getRecordStatus().equalsIgnoreCase("saved")) {
						recordStatus = "Pending for submission";
					}
					empRec.put("recordStatus", recordStatus);

					int age = 0;
					if (emp.getDob() != null && !emp.getDob().equals("")) {
						String[] dateString = emp.getDob().toString().split("-");

						LocalDate today = LocalDate.now();
						LocalDate birthday = LocalDate.of(Integer.parseInt(dateString[0]),
								Integer.parseInt(dateString[1]), Integer.parseInt(dateString[2]));

						Period p = Period.between(birthday, today);
						age = p.getYears();
					}

					empRec.put("age", age + " Years");

					if (emp.getApmId() != null) {
						empRec.put("apmName", emp.getApmId().getUserName());
					}

					if (emp.getApmFlag() != null && !emp.getApmFlag().equals("")) {
						empRec.put("apmAction", emp.getApmFlag());
					}

					if (emp.getAuditorId() != null) {
						empRec.put("auditorName", emp.getAuditorId().getUserName());
					}

					if (emp.getAuditorFlag() != null && !emp.getAuditorFlag().equals("")) {
						empRec.put("auditorAction", emp.getAuditorFlag());
					}

					if (emp.getChmoId() != null) {
						empRec.put("chmoName", emp.getChmoId().getUserName());
					}

					if (emp.getChmoFlag() != null && !emp.getChmoFlag().equals("")) {
						empRec.put("chmoAction", emp.getChmoFlag());
					}

					c.add(empRec);
					// }
				}
				if (c != null && c.size() > 0) {
					json.put("data", c);
					json.put("count", count);
					json.put("msg", "Employee List  get  sucessfull... ");
					json.put("status", "1");
				}

			}

			catch (Exception e) {
				e.printStackTrace();
				return "{\"status\":\"0\",\"msg\":\"Somting went wrong}";
			}

		}
		return json.toString();
	}

	@Override
	public String getEmployeeDetails(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		Map<String, Object> map = empRegistrationDao.getEmployeeDetails(jsondata);

		Map<String, Object> qulificationMap = empRegistrationDao.getEmployeeQualoificationDetails(jsondata);

		Map<String, Object> documentMap = empRegistrationDao.getEmployeeDocumentDetails(jsondata);

		EmployeeRegistration emp = (EmployeeRegistration) map.get("emp");

		if (emp == null) {
			return "{\"status\":\"0\",\"msg\":\"Data not found\"}";
		} else {

			HashMap<String, Object> c = new HashMap<String, Object>();

			try {

				HashMap<String, Object> empRec = new HashMap<String, Object>();

				empRec.put("empId", emp.getEmployeeId());
				empRec.put("empName", emp.getEmployeeName());
				empRec.put("gender", emp.getMasAdministrativeSex().getAdministrativeSexId());
				empRec.put("genderName", emp.getMasAdministrativeSex().getAdministrativeSexName());
				empRec.put("dob", formatDateYYYYMMDD(emp.getDob().toString()));
				empRec.put("address", emp.getAddress());
				empRec.put("state", emp.getState());
				empRec.put("district", emp.getDistrict());
				empRec.put("city", emp.getCity());
				empRec.put("pincode", emp.getPinCode());
				empRec.put("imei", emp.getImeiNumber());

				empRec.put("mobileNo", emp.getMobileNo());
				empRec.put("mmu", emp.getMmuId().getMmuId());
				empRec.put("mmuName", emp.getMmuId().getMmuName());
				empRec.put("empType", emp.getUserTypeId().getUserTypeId());
				empRec.put("empTypeName", emp.getUserTypeId().getUserTypeName());
				empRec.put("emplyMentType", emp.getEmploymentType());
				empRec.put("recordState", emp.getRecordStatus());
				if (emp.getStartDate() != null && !emp.getStartDate().equals("")) {
					empRec.put("fromDate", formatDateYYYYMMDD(emp.getStartDate().toString()));
				}
				if (emp.getEndDate() != null && !emp.getEndDate().equals("")) {
					empRec.put("toDate", formatDateYYYYMMDD(emp.getEndDate().toString()));
				}
				empRec.put("identityNo", emp.getRegNo());

				int age = 0;
				if (emp.getDob() != null && !emp.getDob().equals("")) {
					String[] dateString = emp.getDob().toString().split("-");

					LocalDate today = LocalDate.now();
					LocalDate birthday = LocalDate.of(Integer.parseInt(dateString[0]), Integer.parseInt(dateString[1]),
							Integer.parseInt(dateString[2]));

					Period p = Period.between(birthday, today);
					age = p.getYears();
				}

				empRec.put("age", age);

				if (emp.getApmDate() != null && !emp.getApmDate().equals("")) {
					empRec.put("apmDate", formatDateYYYYMMDD(emp.getApmDate().toString()));
				}

				if (emp.getApmId() != null) {
					empRec.put("apmName", emp.getApmId().getUserName());
				}

				if (emp.getApmFlag() != null && !emp.getApmFlag().equals("")) {
					String apmAction = "";
					if (emp.getApmFlag().equalsIgnoreCase("A"))
						apmAction = "Approved";
					else
						apmAction = "Rejected";
					empRec.put("apmAction", apmAction);
				}

				if (emp.getApmRemarks() != null && !emp.getApmRemarks().equals("")) {
					empRec.put("apmRemarks", emp.getApmRemarks());
				}

				if (emp.getAuditorDate() != null && !emp.getAuditorDate().equals("")) {
					empRec.put("auditorDate", formatDateYYYYMMDD(emp.getAuditorDate().toString()));
				}

				if (emp.getAuditorId() != null) {
					empRec.put("auditorName", emp.getAuditorId().getUserName());
				}

				if (emp.getAuditorFlag() != null && !emp.getAuditorFlag().equals("")) {
					String audAction = "";
					if (emp.getAuditorFlag().equalsIgnoreCase("A"))
						audAction = "Approved";
					else
						audAction = "Rejected";
					empRec.put("auditorAction", audAction);
				}
				if (emp.getAuditorRemarks() != null && !emp.getAuditorRemarks().equals("")) {
					empRec.put("auditorRemarks", emp.getAuditorRemarks());
				}

				if (emp.getChmoDate() != null && !emp.getChmoDate().equals("")) {
					empRec.put("chmoDate", formatDateYYYYMMDD(emp.getChmoDate().toString()));
				}

				if (emp.getChmoId() != null) {
					empRec.put("chmoName", emp.getChmoId().getUserName());
				}

				if (emp.getChmoFlag() != null && !emp.getChmoFlag().equals("")) {
					String chmoAction = "";
					if (emp.getChmoFlag().equalsIgnoreCase("A"))
						chmoAction = "Approved";
					else
						chmoAction = "Rejected";
					empRec.put("chmoAction", chmoAction);
				}
				if (emp.getChmoRemarks() != null && !emp.getChmoRemarks().equals("")) {
					empRec.put("chmoRemarks", emp.getChmoRemarks());
				}
				if (emp.getIdentificationTypeId() != null) {
					empRec.put("idType", emp.getIdentificationTypeId().getIdentificationTypeId());
				}
				if (emp.getIdentificationTypeNo() != null && !emp.getIdentificationTypeNo().equals("")) {
					empRec.put("idTypeNo", emp.getIdentificationTypeNo());
					empRec.put("idTypeName", emp.getIdentificationTypeId().getIdentificationName());
				}

				if (emp.getIdentificationTypeImage() != null && !emp.getIdentificationTypeImage().equals("")) {
					byte[] idImage = emp.getIdentificationTypeImage();
					String idImageString = encodeByteToBase64Binary(idImage);

					empRec.put("idImageString", idImageString);
				}

				String encodedStringImage = "";
				String mimeType = "";
				/*
				 * if(emp.getFilePath() !=null && !emp.getFilePath().equals("")) {
				 * 
				 * File f = new File(emp.getFilePath()); encodedStringImage =
				 * encodeFileToBase64Binary(f);
				 * 
				 * }
				 */
				if (emp.getProfileImage() != null && !emp.getProfileImage().equals("")) {

					byte[] f = emp.getProfileImage();
					encodedStringImage = encodeByteToBase64Binary(f);

					empRec.put("encodedStringImage", encodedStringImage);
				}

				if (emp.getIdMimeType() != null && !emp.getIdMimeType().equals("")) {

					empRec.put("idMimeType", emp.getIdMimeType());

				}

				List<EmployeeQualification> empQualificationList = (List<EmployeeQualification>) qulificationMap
						.get("qualList");
				List<Object> qList = new ArrayList<Object>();

				long qualId = 0;
				String qualName = "";
				String instName = "";
				int completionYear = 0;
				String qualFilePath = "";
				String qualFileString = "";

				if (empQualificationList != null && empQualificationList.size() > 0) {
					for (Iterator<?> iterator = empQualificationList.iterator(); iterator.hasNext();) {
						Map<String, Object> qMap = new HashMap<String, Object>();

						EmployeeQualification qualification = (EmployeeQualification) iterator.next();

						qualId = qualification.getEmployeeQualificationId();

						if (qualification.getQualificationName() != null) {
							qualName = qualification.getQualificationName();
						}
						if (qualification.getInstitutionName() != null) {
							instName = qualification.getInstitutionName();
						}

						completionYear = qualification.getCompletionYear();

						if (qualification.getFilePath() != null) {
							qualFilePath = changePathFormat(qualification.getFilePath());
						}

						if (qualification.getDocument() != null) {
							byte[] doc = qualification.getDocument();
							qualFileString = encodeByteToBase64Binary(doc);
						}

						if (qualification.getMimeType() != null && !qualification.getMimeType().equals("")) {

							mimeType = qualification.getMimeType();

						}

						qMap.put("qualId", qualId);
						qMap.put("qualName", qualName);
						qMap.put("instName", instName);
						qMap.put("completionYear", completionYear);
						qMap.put("uploadedFile", qualFilePath);
						qMap.put("qualFileString", qualFileString);
						qMap.put("mimeType", mimeType);

						qList.add(qMap);

					}

				}

				List<EmployeeDocument> docList = (List<EmployeeDocument>) documentMap.get("docList");
				List<Object> dList = new ArrayList<Object>();

				long docId = 0;
				String docName = "";
				// String uploadedDocPath = "" ;
				String docFileString = "";

				if (docList != null && docList.size() > 0) {
					for (Iterator<?> iterator = docList.iterator(); iterator.hasNext();) {
						Map<String, Object> dMap = new HashMap<String, Object>();

						EmployeeDocument document = (EmployeeDocument) iterator.next();

						docId = document.getEmployeeDocumentId();
						if (document.getDocumentName() != null) {
							docName = document.getDocumentName();
						}

						/*
						 * if (document.getFilePath() != null) { uploadedDocPath =
						 * changePathFormat(document.getFilePath()); }
						 */

						if (document.getDocument() != null) {
							byte[] doc = document.getDocument();
							docFileString = encodeByteToBase64Binary(doc);
						}

						if (document.getMimeType() != null && !document.getMimeType().equals("")) {

							mimeType = document.getMimeType();

						}
						dMap.put("docId", docId);
						dMap.put("docName", docName);
						dMap.put("docFileString", docFileString);
						dMap.put("mimeType", mimeType);

						dList.add(dMap);

					}

				}

				json.put("data", empRec);
				json.put("qList", qList);
				json.put("dList", dList);
				json.put("status", "1");

			}

			catch (Exception e) {
				e.printStackTrace();
				return "{\"status\":\"0\",\"msg\":\"Somting went wrong}";
			}

		}
		return json.toString();
	}

	private String changePathFormat(String filePath) {

		StringBuilder resStr = new StringBuilder();
		String[] tmpStr = filePath.split("\\\\");
		for (int i = 0; i < tmpStr.length; i++) {
			if (i == 0)
				resStr.append(tmpStr[i]);
			else
				resStr.append("@").append(tmpStr[i]);
		}

		return resStr.toString();

	}

	private String encodeFileToBase64Binary(File file) {
		String encodedfile = null;
		try {
			@SuppressWarnings("resource")
			FileInputStream fileInputStreamReader = new FileInputStream(file);
			byte[] bytes = new byte[(int) file.length()];
			fileInputStreamReader.read(bytes);
			encodedfile = new String(Base64.encodeBase64(bytes), "UTF-8");
			fileInputStreamReader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return encodedfile;
	}

	private String encodeByteToBase64Binary(byte[] byteStr) {
		String encodedString = null;

		try {
			encodedString = new String(Base64.encodeBase64(byteStr), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return encodedString;
	}

	private String formatDateYYYYMMDD(String inDate) {
		String[] dateString = inDate.split("-");
		String outDate = dateString[2] + "/" + dateString[1] + "/" + dateString[0];
		return outDate;
	}

	@Override
	public Map<String, Object> saveAPMAction(Map<String, Object> requestData) {
		Map<String, Object> map = new HashMap<>();
		try {
			map = empRegistrationDao.saveAPMAction(requestData);

			// data to send to Nishtha
			/*
			 * JSONObject jsonEmp = new JSONObject(); if (map.get("employee") != null) {
			 * EmployeeRegistration emp = (EmployeeRegistration) map.get("employee");
			 * String[] empName = emp.getEmployeeName().split(" "); String firstName = "";
			 * String lastName = ""; if (empName.length == 2) { firstName = empName[0];
			 * lastName = empName[1]; } else if (empName.length == 3) { firstName =
			 * empName[0] + " " + empName[1]; lastName = empName[2]; }
			 * 
			 * jsonEmp.put("fname", firstName); jsonEmp.put("lname", lastName); if
			 * (emp.getProfileImage() != null && !emp.getProfileImage().equals("")) { byte[]
			 * profileImage = emp.getProfileImage(); String profilePhoto =
			 * encodeByteToBase64Binary(profileImage);
			 * 
			 * jsonEmp.put("photo", profilePhoto); } jsonEmp.put("mobile_number",
			 * emp.getMobileNo()); jsonEmp.put("designation",
			 * emp.getUserTypeId().getDesignation()); jsonEmp.put("department", "220");
			 * jsonEmp.put("ofc_id", emp.getMmuId().getOfficeId()); jsonEmp.put("idType",
			 * emp.getIdentificationTypeId().getMappedId());// modify me
			 * jsonEmp.put("otherIdNumber", emp.getIdentificationTypeNo()); if
			 * (emp.getIdentificationTypeImage() != null &&
			 * !emp.getIdentificationTypeImage().equals("")) {
			 * 
			 * byte[] idImage = emp.getIdentificationTypeImage(); String otherIdImage =
			 * encodeByteToBase64Binary(idImage);
			 * 
			 * jsonEmp.put("otherIdImage", otherIdImage); } jsonEmp.put("validate_by", "1");
			 * jsonEmp.put("validate_status", "1"); jsonEmp.put("emp_flag", "1"); //
			 * System.out.println("jkjk---> "+jsonEmp.toString()); // map.put("jsonEmp",
			 * jsonEmp);
			 * 
			 * }
			 */
			// newEntryForEmp
			// end

			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	@Override
	public Map<String, Object> saveAUDAction(Map<String, Object> requestData) {
		Map<String, Object> map = new HashMap<>();
		try {
			map = empRegistrationDao.saveAUDAction(requestData);

			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	@Override
	public Map<String, Object> saveCHMOAction(Map<String, Object> requestData) {
		Map<String, Object> map = new HashMap<>();
		try {
			map = empRegistrationDao.saveCHMOAction(requestData);

			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	@Override
	public Map<String, Object> saveUPSSAction(Map<String, Object> requestData) {
		Map<String, Object> map = new HashMap<>();
		try {
			map = empRegistrationDao.saveUPSSAction(requestData);

			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	@Override
	public Map<String, Object> checkDuplicateMobile(Map<String, Object> requestData) {
		Map<String, Object> map = new HashMap<>();
		try {
			map = empRegistrationDao.checkDuplicateMobile(requestData);

			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	@Override
	public Map<String, Object> getIdTypeList(Map<String, Object> requestData) {
		Map<String, Object> map = new HashMap<>();
		Map<String, Object> response = new HashMap<String, Object>();
		List<Map<String, Object>> idTypeList = new ArrayList<Map<String, Object>>();
		try {

			List<MasIdentificationType> list = empRegistrationDao.getIdTypeList(requestData);
			if (!list.isEmpty()) {
				for (MasIdentificationType idType : list) {
					Map<String, Object> map2 = new HashMap<>();
					map2.put("id", idType.getIdentificationTypeId());
					map2.put("IdName", idType.getIdentificationName());
					idTypeList.add(map2);
				}
				response.put("status", true);
				response.put("list", idTypeList);
			} else {
				response.put("status", true);
				response.put("list", idTypeList);
			}
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.put("status", false);
		response.put("list", idTypeList);
		return response;
	}

	@Override
	public String getAllUserTypeForEmpReg(JSONObject jsondata, HttpServletRequest request,
			HttpServletResponse response) {

		JSONObject json = new JSONObject();
		List<MasUserType> userTypeList = new ArrayList<MasUserType>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<MasUserType>> mapUserType = empRegistrationDao.getAllUserTypeForEmpReg(jsondata);
			List totalMatches = new ArrayList();
			if (mapUserType.get("userTypeList") != null) {
				userTypeList = mapUserType.get("userTypeList");
				totalMatches = mapUserType.get("totalMatches");
				{
					userTypeList.forEach(ut -> {

						HashMap<String, Object> mapObj = new HashMap<String, Object>();
						if (ut != null) {
							mapObj.put("userTypeId", ut.getUserTypeId());
							mapObj.put("userTypeCode", ut.getUserTypeCode());
							mapObj.put("userTypeName", ut.getUserTypeName());
							mapObj.put("status", ut.getStatus());
							list.add(mapObj);
						}
					});

				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "Get Record successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", 0);
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			}

		}
		return json.toString();

	}

	@Override
	public String pendingAttendanceList(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {

		JSONObject json = new JSONObject();
		Map<String, Object> map = empRegistrationDao.getPendingAttendanceList(jsondata);
		int count = (int) map.get("count");
		String statusSearch = (String) map.get("statusSearch");
		List<AuditAttendanceData> attendanceList = (List<AuditAttendanceData>) map.get("attendanceList");
		String attendanceStatus = "";
		if (attendanceList != null && attendanceList.size() == 0) {
			return "{\"status\":\"0\",\"msg\":\"Data not found\"}";
		} else {

			List<HashMap<String, Object>> c = new ArrayList<HashMap<String, Object>>();
			List<HashMap<String, Object>> presentStatus = new ArrayList<HashMap<String, Object>>();
			List<HashMap<String, Object>> absentStatus = new ArrayList<HashMap<String, Object>>();
			
			int acceptedDistanceForAttendance = Integer.parseInt(HMSUtil.getProperties("adt.properties", "acceptedDistanceForAttendance").trim());
			int acceptedInTimeDiffForAttendance = Integer.parseInt(HMSUtil.getProperties("adt.properties", "acceptedInTimeDiffForAttendance").trim());
			int acceptedOutTimeDiffForAttendance = Integer.parseInt(HMSUtil.getProperties("adt.properties", "acceptedOutTimeDiffForAttendance").trim());

			try {
				for (AuditAttendanceData attendance : attendanceList) {

					HashMap<String, Object> attn = new HashMap<String, Object>();
					Long empId = 0l;
					Long attenId = 0l;
					if(attendance.getUserId() != null && attendance.getUserId().getEmployeeId() != null) {
						empId = attendance.getUserId().getEmployeeId().getEmployeeId();
					}
					if (attendance.getAttendanceDataId() != null) {
						attenId = attendance.getAttendanceDataId();
					}
					attn.put("attenId", attenId);
					String attenDate = "";
					if (attendance.getAttendanceDate() != null) {
						attenDate = attendance.getAttendanceDate().toString();
					}
					attn.put("attenDate", formatDateYYYYMMDD(attenDate));

					String mobileNo = "";
					if (attendance.getUserId() != null) {
						mobileNo = attendance.getUserId().getMobileNo();
					}
					attn.put("mobileNo", mobileNo);

					String empName = "";
					if (attendance.getUserId().getEmployeeId() != null) {
						empName = attendance.getUserId().getEmployeeId().getEmployeeName();
					}
					attn.put("empName", empName);
					String mmuName = "";

					if (attendance.getMmuId() != null) {
						mmuName = attendance.getMmuId().getMmuName();
					}
					attn.put("mmuName", mmuName);

					String gender = "";
					if (attendance.getUserId().getEmployeeId() != null) {
						gender = attendance.getUserId().getEmployeeId().getMasAdministrativeSex()
								.getAdministrativeSexName();
					}
					attn.put("gender", gender);

					String empType = "";
					if (attendance.getUserId().getEmployeeId() != null) {
						empType = attendance.getUserId().getEmployeeId().getUserTypeId().getUserTypeName();
					}
					attn.put("empType", empType);

					String photoMatched = "";
						if(attendance.getAttendancePhoto().equals("N")) {
						photoMatched = "Not Matched";
						
					} else if(attendance.getAttendancePhoto().equals("Y")) {
						photoMatched = "Matched";
					}
					attn.put("photoMatched", photoMatched);
					
					String encodedStringImage = "";
					if (attendance.getUserId().getEmployeeId() != null && attendance.getUserId().getEmployeeId().getProfileImage() != null
							&& !attendance.getUserId().getEmployeeId().getProfileImage().equals("")) {

						byte[] f = attendance.getUserId().getEmployeeId().getProfileImage();
						 encodedStringImage = encodeByteToBase64Binary(f);

						
					}
					//System.out.println("encodedStringImage--> "+encodedStringImage);
					attn.put("profileImage", encodedStringImage);
					//String rootPath = System.getProperty("catalina.home");
					String rootPath = environment.getProperty("mmu.service.attendancefiles.basePath");
					String outPhotoBase64String = "";
					if (attendance.getLastOutPhoto() != null && !attendance.getLastOutPhoto().equals("")) {

						String imageFolderPathDirctory = rootPath + File.separator
								+ attendance.getUserId().getMobileNo() + File.separator + attenDate + File.separator;
						String completePath = imageFolderPathDirctory + attendance.getLastOutPhoto();
						File f = new File(completePath);
						if(f.exists()) {
							outPhotoBase64String = encodeFileToBase64Binary(f);
						}
					}
					attn.put("outPhotoBase64String", outPhotoBase64String);
					String inPhotoBase64String = "";
					if (attendance.getFirstInPhoto() != null && !attendance.getFirstInPhoto().equals("")) {
						String imageFolderPathDirctory = rootPath  + File.separator
								+ attendance.getUserId().getMobileNo() + File.separator + attenDate + File.separator;
						String completePath = imageFolderPathDirctory + attendance.getFirstInPhoto();
						File f = new File(completePath);
						if(f.exists()) {
							inPhotoBase64String = encodeFileToBase64Binary(f);
						}
					}
					attn.put("inPhotoBase64String", inPhotoBase64String);
					
					
					Map<String, Object> campInfoMap = empRegistrationDao.getCampInfo(attendance.getMmuId().getMmuId(),attenDate);
					List<MasCamp> camp = (List<MasCamp>) campInfoMap.get("campList");
					
					
					Double campLattitude = 0.0; 
					Double campLongitude = 0.0; 
					Double attendanceInLattitude = 0.0; 
					Double attendanceInLongitude = 0.0; 
					
					Double attendanceOutLattitude = 0.0; 
					Double attendanceOutLongitude = 0.0; 
					
					String campStartTime = "";
					String campEndTime = "";
					String timeMatched = "";
					double inTimeDistance = 0.0; 
					double outTimeDistance = 0.0;
					
					if(camp != null && camp.size() > 0) {
						campLattitude = camp.get(0).getLattitude();
						campLongitude = camp.get(0).getLongitude();
						campStartTime = camp.get(0).getStartTime().toString();
						campEndTime = camp.get(0).getEndTime().toString();
					}
					
					if(attendance.getFirstLat() != null) {
						attendanceInLattitude = attendance.getFirstLat();
					} 
					if(attendance.getFirstLong() != null) {
						attendanceInLongitude = attendance.getFirstLong();
					} 
					
					if(attendance.getLastLat() != null) {
						attendanceOutLattitude = attendance.getLastLat();
					} 
					if(attendance.getLastLong() != null) {
						attendanceOutLongitude = attendance.getLastLong();
					} 
					String distMatched = "";
					inTimeDistance = calculateDistanceInMeters(campLattitude,campLongitude,attendanceInLattitude,attendanceInLongitude);
					if(attendanceOutLattitude > 0 && attendanceOutLongitude > 0) {
						outTimeDistance = calculateDistanceInMeters(campLattitude,campLongitude,attendanceOutLattitude,attendanceOutLongitude);
					}
					if(attendance.getLastLat() != null && attendance.getLastLong() != null) {						
						if(inTimeDistance > acceptedDistanceForAttendance || outTimeDistance > acceptedDistanceForAttendance ) {
							distMatched = "Not Matched";
						} else {
							distMatched = "Matched";
						}
					} else {
						distMatched = "Not Matched";
					}
					attn.put("campLattitude", campLattitude);
					attn.put("campLongitude", campLongitude);
					attn.put("attendanceInLattitude", attendanceInLattitude);
					attn.put("attendanceInLongitude", attendanceInLongitude);
					
					attn.put("attendanceOutLattitude", attendanceOutLattitude);
					attn.put("attendanceOutLongitude", attendanceOutLongitude);
					
					attn.put("distMatched", distMatched);
					attn.put("inTimeDistance", String.format("%.2f", inTimeDistance));
					attn.put("outTimeDistance", String.format("%.2f", outTimeDistance));
					
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
					String attnInTime = "";
					String attnOutTime = "";
					Date attnIn = null;
					Date attnOut = null;
					if(campStartTime != null && !campStartTime.equals("") && campEndTime != null && !campEndTime.equals("") ) {
					Date campStTime = simpleDateFormat.parse(campStartTime);
					Date campStopTime = simpleDateFormat.parse(campEndTime);
					long differenceOutMinutes = 0l;
					long differenceInMinutes = 0l;
					if(attendance.getInTime() != null && attendance.getInTime() != "") {
					 attnInTime = attendance.getInTime();
					 attnIn = simpleDateFormat.parse(attnInTime);
					  long diffIntimeMiliSeconds =  attnIn.getTime() - campStTime.getTime()  ;
					   differenceInMinutes = (diffIntimeMiliSeconds / (60 * 1000));
					}
					if(attendance.getOutTime()!= null && attendance.getOutTime() != "") {
					 attnOutTime = attendance.getOutTime();
					  attnOut = simpleDateFormat.parse(attnOutTime);
					  long diffOuttimeMiliSeconds =  campStopTime.getTime() - attnOut.getTime() ;
					  differenceOutMinutes = (diffOuttimeMiliSeconds / (60 * 1000));
					}
					if(attendance.getOutTime()!= null && attendance.getOutTime() != ""){
						if(differenceInMinutes > acceptedInTimeDiffForAttendance || differenceOutMinutes > acceptedOutTimeDiffForAttendance ) {
						
							 timeMatched = "Not Matched";
						} else {
							timeMatched = "Matched";
						}
					} else {
						 timeMatched = "Not Matched";
					}
				}
					attn.put("timeMatched", timeMatched);
					attn.put("campStartTime", campStartTime);
					attn.put("campEndTime", campEndTime);
					attn.put("attnOutTime", attnOutTime);
					attn.put("attnInTime", attnInTime);
					
					
					if(photoMatched.equals("Matched") && distMatched.equals("Matched") && timeMatched.equals("Matched")) {
						attendanceStatus = "P";
					} else {
						attendanceStatus = "A";
					}
					attn.put("attendanceStatus", attendanceStatus);
						c.add(attn);
				}
				if (c != null && c.size() > 0) {
					
					for(HashMap<String, Object> record : c) {
						if(record.get("attendanceStatus").equals("A")) {
							absentStatus.add(record);
						} else {
							presentStatus.add(record);
						}
					}
				} if(statusSearch!= null && !statusSearch.equals("") ) {
					if(statusSearch.equals("P") && presentStatus != null && presentStatus.size() > 0 ){
					json.put("data", presentStatus);
					json.put("count", count);
					json.put("msg", "Employee List  get  sucessfull... ");
					json.put("status", "1");
				} else if(statusSearch.equals("A") && absentStatus != null && absentStatus.size() > 0) {
					json.put("data", absentStatus);
					json.put("count", count);
					json.put("msg", "Employee List  get  sucessfull... ");
					json.put("status", "1");
				}
				}else if (c != null && c.size() > 0) {
					json.put("data", c);
					json.put("count", count);
					json.put("msg", "Employee List  get  sucessfull... ");
					json.put("status", "1");
				}

			}

			catch (Exception e) {
				e.printStackTrace();
				return "{\"status\":\"0\",\"msg\":\"Somting went wrong}";
			}

		}
		return json.toString();

	}

	private Double calculateDistanceInMeters(Double campLattitude, Double campLongitude, Double attendanceLattitude,
			Double attendanceLongitude) {
		double dist = org.apache.lucene.util.SloppyMath.haversinMeters(campLattitude, campLongitude, attendanceLattitude, attendanceLongitude);
	    return dist;
	}

	@Override
	public Map<String, Object> saveAttendance(Map<String, Object> requestData) {
		Map<String, Object> map = new HashMap<>();
		try {
			map = empRegistrationDao.saveAttendance(requestData);

			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	@Override
	public String pendingListPhoto(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {

		JSONObject json = new JSONObject();
		Map<String, Object> map = empRegistrationDao.pendingListPhoto(jsondata);
		int count = (int) map.get("count");
		List<AuditAttendanceData> attendanceList = (List<AuditAttendanceData>) map.get("attendanceList");

		if (attendanceList != null && attendanceList.size() == 0) {
			return "{\"status\":\"0\",\"msg\":\"Data not found\"}";
		}

		System.out.println("attendanceList size on impl--> " + attendanceList.size());
		try {

			
			List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
			for (AuditAttendanceData attendance : attendanceList) {
				HashMap<String, Object> attnRecord = new HashMap<String, Object>();
				String empName = "";
				Long empId = 0l;
				String gender = "";
				String empType = "";
				

				attnRecord.put("auditAttendanceId", attendance.getAttendanceDataId());
				if(attendance.getUserId().getEmployeeId() != null ) {
				 empName = attendance.getUserId().getEmployeeId().getEmployeeName();
				}else {
					empName = attendance.getUserId().getUserName();
				}
				attnRecord.put("empName", empName);
				if(attendance.getUserId().getEmployeeId() != null) {
				 empId = attendance.getUserId().getEmployeeId().getEmployeeId();
				}
				attnRecord.put("empId", empId);
				Long mmuId = attendance.getMmuId().getMmuId();
				attnRecord.put("mmuId", mmuId);
				String mmuName = attendance.getMmuId().getMmuName();
				attnRecord.put("mmuName", mmuName);
				if(attendance.getUserId().getEmployeeId() != null) {
				 gender = attendance.getUserId().getEmployeeId().getMasAdministrativeSex().getAdministrativeSexCode();
				} 
						
				attnRecord.put("gender", gender);
				if(attendance.getUserId().getEmployeeId() != null) {
				 empType = attendance.getUserId().getEmployeeId().getUserTypeId().getUserTypeName();
				} else {
					empType = attendance.getUserId().getMasUserType().getUserTypeName();
				}
				attnRecord.put("empType", empType);
				Date attnDate = attendance.getAttendanceDate();
				//System.out.println("attnDate-> " + formatDateYYYYMMDD(attnDate.toString()));
				attnRecord.put("attnDate", formatDateYYYYMMDD(attnDate.toString()));
				String mobileNo = attendance.getUserId().getMobileNo();
				attnRecord.put("mobileNo", mobileNo);

				String age = "";
				if (attendance.getUserId().getEmployeeId()!= null && attendance.getUserId().getEmployeeId().getDob() != null
						&& !attendance.getUserId().getEmployeeId().getDob().equals("")) {
					String[] dateString = attendance.getUserId().getEmployeeId().getDob().toString().split("-");

					LocalDate today = LocalDate.now();
					LocalDate birthday = LocalDate.of(Integer.parseInt(dateString[0]), Integer.parseInt(dateString[1]),
							Integer.parseInt(dateString[2]));

					Period p = Period.between(birthday, today);
					 age = "" + p.getYears() + " Years";
					
				}
				attnRecord.put("age", age);
				String encodedStringImage = "";

				if (attendance.getUserId().getEmployeeId()!= null && attendance.getUserId().getEmployeeId().getProfileImage() != null
						&& !attendance.getUserId().getEmployeeId().getProfileImage().equals("")) {

					byte[] f = attendance.getUserId().getEmployeeId().getProfileImage();
					 encodedStringImage = encodeByteToBase64Binary(f);

				}
				attnRecord.put("profileImage", encodedStringImage);
				//String rootPath = System.getProperty("catalina.home");
				String rootPath = environment.getProperty("mmu.service.attendancefiles.basePath");
				String outPhotoBase64String = "";
				if (attendance.getLastOutPhoto() != null && !attendance.getLastOutPhoto().equals("")) {

					String imageFolderPathDirctory = rootPath + File.separator +
							attendance.getUserId().getMobileNo() + File.separator + attnDate + File.separator;
					String completePath = imageFolderPathDirctory + attendance.getLastOutPhoto();
					File f = new File(completePath);
					if(f.exists()) {
						outPhotoBase64String = encodeFileToBase64Binary(f);
					}
				}
				attnRecord.put("outPhotoBase64String", outPhotoBase64String);
				String inPhotoBase64String = "";
				if (attendance.getFirstInPhoto() != null && !attendance.getFirstInPhoto().equals("")) {
					String imageFolderPathDirctory = rootPath + File.separator 
							+ attendance.getUserId().getMobileNo() + File.separator + attnDate + File.separator;
					String completePath = imageFolderPathDirctory + attendance.getFirstInPhoto();
					File f = new File(completePath);
					if(f.exists()) {
					inPhotoBase64String = encodeFileToBase64Binary(f);
					}
				}
				attnRecord.put("inPhotoBase64String", inPhotoBase64String);
				data.add(attnRecord);
			}

			//if (data != null && data.size() > 0) {
				json.put("data", data);
				json.put("count", data.size());
				json.put("msg", "Attendance List  get  sucessfull... ");
				json.put("status", "1");
			//}

		} catch (Exception e) {
			e.printStackTrace();
			return "{\"status\":\"0\",\"msg\":\"Somting went wrong}";
		}
		return json.toString();
	}

	@Override
	public Map<String, Object> savePhotoValidation(Map<String, Object> requestData) {
		Map<String, Object> map = new HashMap<>();
		try {
			map = empRegistrationDao.savePhotoValidation(requestData);

			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	@Override
	public Map<String, Object> saveAttendanceAudit(Map<String, Object> requestData) {

		Map<String, Object> map = new HashMap<>();
		try {
			map = empRegistrationDao.saveAttendanceAudit(requestData);

			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	
	}

	@Override
	public Map<String, Object> getAttendanceYears(Map<String, Object> requestData) {

		Map<String, Object> response = new HashMap<String, Object>();
		List<Map<String, Object>> attnYearsList = new ArrayList<Map<String, Object>>();
		try {

			List<Integer> yearsList = empRegistrationDao.getAttendanceYears(requestData);
			if (!yearsList.isEmpty()) {
				 for(Integer year : yearsList) {
					 Map<String, Object> map = new HashMap<>();
					 map.put("years", year);
					 attnYearsList.add(map);
				 }
				response.put("status", true);
				response.put("list", attnYearsList);
			} else {
				response.put("status", true);
				response.put("list", attnYearsList);
			}
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.put("status", false);
		response.put("list", attnYearsList);
		return response;
	
	}

	@Override
	public Map<String, Object> getAttendanceMonths(Map<String, Object> requestData) {


		Map<String, Object> response = new HashMap<String, Object>();
		List<Integer> attnMonthsList = new ArrayList<Integer>();
		List<Map<String, Object>> attnMonthList = new ArrayList<Map<String, Object>>();
		try {

			attnMonthsList = empRegistrationDao.getAttendanceMonths(requestData);
			if (!attnMonthsList.isEmpty()) {
				 for(Integer monthVal : attnMonthsList) {
					 Map<String, Object> map = new HashMap<>();
					 map.put("monthVal", monthVal);
					 Month months = Month.of(monthVal);
					String month = months.toString();
					map.put("month", month);
					 attnMonthList.add(map);
				 }
				response.put("status", true);
				response.put("list", attnMonthList);
			} else {
				response.put("status", true);
				response.put("list", attnMonthList);
			}
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.put("status", false);
		response.put("list", attnMonthList);
		return response;
	
	
	}

	@Override
	public String getPenaltyList(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {


		JSONObject json = new JSONObject();
		Map<String, Object> map = empRegistrationDao.getPenaltyList(jsondata);
		int count = (int) map.get("count");
		List<AuditAttendanceData> penaltyList = (List<AuditAttendanceData>) map.get("penaltyList");
		
		int doctorPenalty = Integer.parseInt(HMSUtil.getProperties("adt.properties", "penaltyForDoctor").trim());
		int penaltyForOther = Integer.parseInt(HMSUtil.getProperties("adt.properties", "penaltyForOther").trim());

		if (penaltyList != null && penaltyList.size() == 0) {
			return "{\"status\":\"0\",\"msg\":\"Data not found\"}";
		}

		System.out.println("penaltyList size on impl--> " + penaltyList.size());
		try {

			int penaltyAmount = 0;
			List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
			for (AuditAttendanceData penalty : penaltyList) {
				HashMap<String, Object> penaltyRecord = new HashMap<String, Object>();
				penaltyRecord.put("incidentDate", formatDateYYYYMMDD(penalty.getAttendanceDate().toString()));
				String desc = "";
				if(penalty.getUserId().getEmployeeId() != null ) {
				 desc = penalty.getUserId().getEmployeeId().getEmployeeName()+", "+penalty.getUserId().getEmployeeId().getUserTypeId().getUserTypeName()+", Absent";
				} else {
					desc = penalty.getUserId().getUserName()+", "+penalty.getUserId().getMasUserType().getUserTypeName()+", Absent";
				}
				penaltyRecord.put("description", desc);
				if(penalty.getUserId().getEmployeeId() != null) {
				if(penalty.getUserId().getEmployeeId().getUserTypeId().getUserTypeName().equalsIgnoreCase("Doctor")) {
					penaltyAmount = doctorPenalty;
				} else {
					penaltyAmount = penaltyForOther;
				}
				} else {
					if(penalty.getUserId().getMasUserType().getUserTypeName().equalsIgnoreCase("Doctor")) {
						penaltyAmount = doctorPenalty;
					} else {
						penaltyAmount = penaltyForOther;
					}
				}
				penaltyRecord.put("penaltyAmount", penaltyAmount);
				
				data.add(penaltyRecord);
			}

			if (data != null && data.size() > 0) {
				json.put("data", data);
				json.put("count", data.size());
				json.put("msg", "Penality List  get  sucessfull... ");
				json.put("status", "1");
			}

		} catch (Exception e) {
			e.printStackTrace();
			return "{\"status\":\"0\",\"msg\":\"Somting went wrong}";
		}
		return json.toString();
	
	}

	@Override
	public String auditAttendanceHistory(Map<String, Object> requestData) {


		JSONObject json = new JSONObject();
		Map<String, Object> map = empRegistrationDao.auditAttendanceHistory(requestData);
		int count = (int) map.get("count");
		String statusSearch = (String) map.get("statusSearch");
		List<AuditAttendanceData> attendanceList = (List<AuditAttendanceData>) map.get("attendanceList");
		String attendanceStatus = "";
		if (attendanceList != null && attendanceList.size() == 0) {
			return "{\"status\":\"0\",\"msg\":\"Data not found\"}";
		} else {

			List<HashMap<String, Object>> c = new ArrayList<HashMap<String, Object>>();
			List<HashMap<String, Object>> presentStatus = new ArrayList<HashMap<String, Object>>();
			List<HashMap<String, Object>> absentStatus = new ArrayList<HashMap<String, Object>>();
			
			int acceptedDistanceForAttendance = Integer.parseInt(HMSUtil.getProperties("adt.properties", "acceptedDistanceForAttendance").trim());
			int acceptedInTimeDiffForAttendance = Integer.parseInt(HMSUtil.getProperties("adt.properties", "acceptedInTimeDiffForAttendance").trim());
			int acceptedOutTimeDiffForAttendance = Integer.parseInt(HMSUtil.getProperties("adt.properties", "acceptedOutTimeDiffForAttendance").trim());

			try {
				for (AuditAttendanceData attendance : attendanceList) {

					HashMap<String, Object> attn = new HashMap<String, Object>();
					Long empId = 0l;
					Long attenId = 0l;
					if(attendance.getUserId() != null) {
						empId = attendance.getUserId().getEmployeeId().getEmployeeId();
					}
					if (attendance.getAttendanceDataId() != null) {
						attenId = attendance.getAttendanceDataId();
					}
					attn.put("attenId", attenId);
					String attenDate = "";
					if (attendance.getAttendanceDate() != null) {
						attenDate = attendance.getAttendanceDate().toString();
					}
					attn.put("attenDate", formatDateYYYYMMDD(attenDate));

					String mobileNo = "";
					if (attendance.getUserId() != null) {
						mobileNo = attendance.getUserId().getMobileNo();
					}
					attn.put("mobileNo", mobileNo);

					String empName = "";
					if (attendance.getUserId().getEmployeeId() != null) {
						empName = attendance.getUserId().getEmployeeId().getEmployeeName();
					}
					attn.put("empName", empName);
					String mmuName = "";

					if (attendance.getMmuId() != null) {
						mmuName = attendance.getMmuId().getMmuName();
					}
					attn.put("mmuName", mmuName);

					String gender = "";
					if (attendance.getUserId().getEmployeeId() != null) {
						gender = attendance.getUserId().getEmployeeId().getMasAdministrativeSex()
								.getAdministrativeSexName();
					}
					attn.put("gender", gender);

					String empType = "";
					if (attendance.getUserId().getEmployeeId() != null) {
						empType = attendance.getUserId().getEmployeeId().getUserTypeId().getUserTypeName();
					}
					attn.put("empType", empType);

					String photoMatched = "";
						if(attendance.getAttendancePhoto().equals("N")) {
						photoMatched = "Not Matched";
						
					} else if(attendance.getAttendancePhoto().equals("Y")) {
						photoMatched = "Matched";
					}
					attn.put("photoMatched", photoMatched);
					
					String encodedStringImage = "";
					if (attendance.getUserId().getEmployeeId().getProfileImage() != null
							&& !attendance.getUserId().getEmployeeId().getProfileImage().equals("")) {

						byte[] f = attendance.getUserId().getEmployeeId().getProfileImage();
						 encodedStringImage = encodeByteToBase64Binary(f);

						
					}
					attn.put("profileImage", encodedStringImage);
					//String rootPath = System.getProperty("catalina.home");
					String rootPath = environment.getProperty("mmu.service.attendancefiles.basePath");
					
					String outPhotoBase64String = "";
					if (attendance.getLastOutPhoto() != null && !attendance.getLastOutPhoto().equals("")) {

						String imageFolderPathDirctory = rootPath + File.separator
								+ attendance.getUserId().getMobileNo() + File.separator + attenDate + File.separator;
						String completePath = imageFolderPathDirctory + attendance.getLastOutPhoto();
						File f = new File(completePath);
						if(f.exists()) {
							outPhotoBase64String = encodeFileToBase64Binary(f);
						}
					}
					attn.put("outPhotoBase64String", outPhotoBase64String);
					String inPhotoBase64String = "";
					if (attendance.getFirstInPhoto() != null && !attendance.getFirstInPhoto().equals("")) {
						String imageFolderPathDirctory = rootPath + File.separator
								+ attendance.getUserId().getMobileNo() + File.separator + attenDate + File.separator;
						String completePath = imageFolderPathDirctory + attendance.getFirstInPhoto();
						File f = new File(completePath);
						if(f.exists()) {
							inPhotoBase64String = encodeFileToBase64Binary(f);
					}
					}
					attn.put("inPhotoBase64String", inPhotoBase64String);
					
					
					Map<String, Object> campInfoMap = empRegistrationDao.getCampInfo(attendance.getMmuId().getMmuId(),attenDate);
					List<MasCamp> camp = (List<MasCamp>) campInfoMap.get("campList");
					
					
					Double campLattitude = 0.0; 
					Double campLongitude = 0.0; 
					Double attendanceInLattitude = 0.0; 
					Double attendanceInLongitude = 0.0; 
					
					Double attendanceOutLattitude = 0.0; 
					Double attendanceOutLongitude = 0.0; 
					
					String campStartTime = "";
					String campEndTime = "";
					String timeMatched = "";
					double inTimeDistance = 0.0; 
					double outTimeDistance = 0.0;
					
					if(camp != null && camp.size() > 0) {
						campLattitude = camp.get(0).getLattitude();
						campLongitude = camp.get(0).getLongitude();
						campStartTime = camp.get(0).getStartTime().toString();
						campEndTime = camp.get(0).getEndTime().toString();
					}
					
					if(attendance.getFirstLat() != null) {
						attendanceInLattitude = attendance.getFirstLat();
					} 
					if(attendance.getFirstLong() != null) {
						attendanceInLongitude = attendance.getFirstLong();
					} 
					
					if(attendance.getLastLat() != null) {
						attendanceOutLattitude = attendance.getLastLat();
					} 
					if(attendance.getLastLong() != null) {
						attendanceOutLongitude = attendance.getLastLong();
					} 
					String distMatched = "";
					inTimeDistance = calculateDistanceInMeters(campLattitude,campLongitude,attendanceInLattitude,attendanceInLongitude);
					outTimeDistance = calculateDistanceInMeters(campLattitude,campLongitude,attendanceOutLattitude,attendanceOutLongitude);
					if(attendance.getLastLat() != null && attendance.getLastLat() != null) {
						
						if(inTimeDistance > acceptedDistanceForAttendance || outTimeDistance > acceptedDistanceForAttendance ) {
							distMatched = "Not Matched";
						} else {
							distMatched = "Matched";
						}
					} else {
						distMatched = "Not Matched";
					}
					
					attn.put("attRemarks", attendance.getRemarks());
					attn.put("campLattitude", campLattitude);
					attn.put("campLongitude", campLongitude);
					attn.put("attendanceInLattitude", attendanceInLattitude);
					attn.put("attendanceInLongitude", attendanceInLongitude);
					
					attn.put("attendanceOutLattitude", attendanceOutLattitude);
					attn.put("attendanceOutLongitude", attendanceOutLongitude);
					
					attn.put("distMatched", distMatched);
					attn.put("inTimeDistance", String.format("%.2f", inTimeDistance));
					attn.put("outTimeDistance", String.format("%.2f", outTimeDistance));
					
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
					String attnInTime = "";
					String attnOutTime = "";
					Date attnIn = null;
					Date attnOut = null;
					if(campStartTime != null && !campStartTime.equals("") && campEndTime != null && !campEndTime.equals("") ) {
					Date campStTime = simpleDateFormat.parse(campStartTime);
					Date campStopTime = simpleDateFormat.parse(campEndTime);
					long differenceOutMinutes = 0l;
					long differenceInMinutes = 0l;
					if(attendance.getInTime() != null && attendance.getInTime() != "") {
					 attnInTime = attendance.getInTime();
					 attnIn = simpleDateFormat.parse(attnInTime);
					  long diffIntimeMiliSeconds =  attnIn.getTime() - campStTime.getTime()  ;
					   differenceInMinutes = (diffIntimeMiliSeconds / (60 * 1000));
					}
					if(attendance.getOutTime()!= null && attendance.getOutTime() != "") {
					 attnOutTime = attendance.getOutTime();
					  attnOut = simpleDateFormat.parse(attnOutTime);
					  long diffOuttimeMiliSeconds =  campStopTime.getTime() - attnOut.getTime() ;
					  differenceOutMinutes = (diffOuttimeMiliSeconds / (60 * 1000));
					}
					if(attendance.getOutTime()!= null && attendance.getOutTime() != ""){
						if(differenceInMinutes > acceptedInTimeDiffForAttendance || differenceOutMinutes > acceptedOutTimeDiffForAttendance ) {
						
							 timeMatched = "Not Matched";
						} else {
							timeMatched = "Matched";
						}
					} else {
						 timeMatched = "Not Matched";
					}
				}
					attn.put("timeMatched", timeMatched);
					attn.put("campStartTime", campStartTime);
					attn.put("campEndTime", campEndTime);
					System.out.println("attnOutTime--> "+attnOutTime);
					attn.put("attnOutTime", attnOutTime);
					attn.put("attnInTime", attnInTime);
					
					
					if(photoMatched.equals("Matched") && distMatched.equals("Matched") && timeMatched.equals("Matched")) {
						attendanceStatus = "P";
					} else {
						attendanceStatus = "A";
					}
					attn.put("attendanceStatus", attendanceStatus);
						c.add(attn);
				}
				if (c != null && c.size() > 0) {
					
					for(HashMap<String, Object> record : c) {
						if(record.get("attendanceStatus").equals("A")) {
							absentStatus.add(record);
						} else {
							presentStatus.add(record);
						}
					}
				} if(statusSearch!= null && !statusSearch.equals("") ) {
					if(statusSearch.equals("P") && presentStatus != null && presentStatus.size() > 0 ){
					json.put("data", presentStatus);
					json.put("count", count);
					json.put("msg", "Attendance List  get  sucessfull... ");
					json.put("status", "1");
				} else if(statusSearch.equals("A") && absentStatus != null && absentStatus.size() > 0) {
					json.put("data", absentStatus);
					json.put("count", count);
					json.put("msg", "Attendance List  get  sucessfull... ");
					json.put("status", "1");
				}
				}else if (c != null && c.size() > 0) {
					json.put("data", c);
					json.put("count", count);
					json.put("msg", "Attendance List  get  sucessfull... ");
					json.put("status", "1");
				}

			}

			catch (Exception e) {
				e.printStackTrace();
				return "{\"status\":\"0\",\"msg\":\"Somting went wrong}";
			}

		}
		return json.toString();

	
	}

	@Override
	public Map<String, Object> checkDuplicateIMEI(Map<String, Object> requestData) {

		Map<String, Object> map = new HashMap<>();
		try {
			map = empRegistrationDao.checkDuplicateIMEI(requestData);

			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	
	}

	@Override
	public String getEmpListForEdit(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {

		JSONObject json = new JSONObject();
		Map<String, Object> map = empRegistrationDao.getEmpListForEdit(jsondata);
		int count = (int) map.get("count");
		List<EmployeeRegistration> getList = (List<EmployeeRegistration>) map.get("list");

		if (getList.size() == 0) {
			return "{\"status\":\"0\",\"msg\":\"Data not found\"}";
		} else {

			List<HashMap<String, Object>> c = new ArrayList<HashMap<String, Object>>();

			try {
				for (EmployeeRegistration emp : getList) {

					HashMap<String, Object> empRec = new HashMap<String, Object>();

					empRec.put("empId", emp.getEmployeeId());
					empRec.put("empName", emp.getEmployeeName());
					empRec.put("gender", emp.getMasAdministrativeSex().getAdministrativeSexName());
					empRec.put("dob", formatDateYYYYMMDD(emp.getDob().toString()));

					empRec.put("mobileNo", emp.getMobileNo());
					empRec.put("mmu", emp.getMmuId().getMmuName());
					empRec.put("empType", emp.getEmploymentType());

					empRec.put("empType", emp.getUserTypeId().getUserTypeName());

					String recordStatus = "";

					
					if (emp.getRecordStatus().equalsIgnoreCase("submitted")) {

						if (emp.getApmFlag() != null && !emp.getApmFlag().equals("") && emp.getAuditorFlag() != null
								&& !emp.getAuditorFlag().equals("")) {
							if (emp.getApmFlag().equals("R") && emp.getAuditorFlag().equalsIgnoreCase("A")) {
								recordStatus = "Rejected By APM";
							} else if (emp.getApmFlag().equals("A") && emp.getAuditorFlag().equalsIgnoreCase("R")) {
								recordStatus = "Rejected By Auditor";
							} else if (emp.getApmFlag().equals("A") && emp.getAuditorFlag().equalsIgnoreCase("A")) {
								recordStatus = "Approved By APM/Auditor";
							}

						} else {
							recordStatus = "Pending at APM/Auditor";
						}

						if (emp.getChmoFlag() != null && !emp.getChmoFlag().equals("")) {
							if (emp.getChmoFlag().equals("A")) {
								recordStatus = "Approved By CHMO";
							} else {
								recordStatus = "Rejected By CHMO";
							}
						}

						if (emp.getUpssFlag() != null && !emp.getUpssFlag().equals("")) {
							if (emp.getUpssFlag().equals("A")) {
								recordStatus = "Approved By UPSS";
							} else {
								recordStatus = "Rejected By UPSS";
							}
						}

					} else if (emp.getRecordStatus() != null && !emp.getRecordStatus().equals("")
							&& emp.getRecordStatus().equalsIgnoreCase("saved")) {
						recordStatus = "Pending for submission";
					}
					empRec.put("recordStatus", recordStatus);

					int age = 0;
					if (emp.getDob() != null && !emp.getDob().equals("")) {
						String[] dateString = emp.getDob().toString().split("-");

						LocalDate today = LocalDate.now();
						LocalDate birthday = LocalDate.of(Integer.parseInt(dateString[0]),
								Integer.parseInt(dateString[1]), Integer.parseInt(dateString[2]));

						Period p = Period.between(birthday, today);
						age = p.getYears();
					}

					empRec.put("age", age + " Years");

					if (emp.getApmId() != null) {
						empRec.put("apmName", emp.getApmId().getUserName());
					}

					if (emp.getApmFlag() != null && !emp.getApmFlag().equals("")) {
						empRec.put("apmAction", emp.getApmFlag());
					}

					if (emp.getAuditorId() != null) {
						empRec.put("auditorName", emp.getAuditorId().getUserName());
					}

					if (emp.getAuditorFlag() != null && !emp.getAuditorFlag().equals("")) {
						empRec.put("auditorAction", emp.getAuditorFlag());
					}

					if (emp.getChmoId() != null) {
						empRec.put("chmoName", emp.getChmoId().getUserName());
					}

					if (emp.getChmoFlag() != null && !emp.getChmoFlag().equals("")) {
						empRec.put("chmoAction", emp.getChmoFlag());
					}

					c.add(empRec);
					// }
				}
				if (c != null && c.size() > 0) {
					json.put("data", c);
					System.out.println("Emp list: "+c);
					json.put("count", count);
					json.put("msg", "Employee List  get  sucessfull... ");
					json.put("status", "1");
				}

			}

			catch (Exception e) {
				e.printStackTrace();
				return "{\"status\":\"0\",\"msg\":\"Somting went wrong}";
			}

		}
		return json.toString();
	
	}

	@Override
	public String getEmployeeRecordForUpdate(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {

		JSONObject json = new JSONObject();
		Map<String, Object> map = empRegistrationDao.getEmployeeRecordForUpdate(jsondata);

		Map<String, Object> qulificationMap = empRegistrationDao.getEmployeeQualoificationDetails(jsondata);

		Map<String, Object> documentMap = empRegistrationDao.getEmployeeDocumentDetails(jsondata);

		EmployeeRegistration emp = (EmployeeRegistration) map.get("emp");
		
		

		if (emp == null) {
			return "{\"status\":\"0\",\"msg\":\"Data not found\"}";
		} else {

			HashMap<String, Object> c = new HashMap<String, Object>();

			try {

				HashMap<String, Object> empRec = new HashMap<String, Object>();

				empRec.put("empId", emp.getEmployeeId());
				empRec.put("empName", emp.getEmployeeName());
				empRec.put("gender", emp.getMasAdministrativeSex().getAdministrativeSexId());
				empRec.put("genderName", emp.getMasAdministrativeSex().getAdministrativeSexName());
				empRec.put("dob", formatDateYYYYMMDD(emp.getDob().toString()));
				empRec.put("address", emp.getAddress());
				empRec.put("state", emp.getState());
				empRec.put("district", emp.getDistrict());
				empRec.put("city", emp.getCity());
				empRec.put("pincode", emp.getPinCode());
				empRec.put("imei", emp.getImeiNumber());

				empRec.put("mobileNo", emp.getMobileNo());
				empRec.put("mmu", emp.getMmuId().getMmuId());
				empRec.put("mmuName", emp.getMmuId().getMmuName());
				empRec.put("empType", emp.getUserTypeId().getUserTypeId());
				empRec.put("empTypeName", emp.getUserTypeId().getUserTypeName());
				empRec.put("emplyMentType", emp.getEmploymentType());
				empRec.put("recordState", emp.getRecordStatus());
				if (emp.getStartDate() != null && !emp.getStartDate().equals("")) {
					empRec.put("fromDate", formatDateYYYYMMDD(emp.getStartDate().toString()));
				}
				if (emp.getEndDate() != null && !emp.getEndDate().equals("")) {
					empRec.put("toDate", formatDateYYYYMMDD(emp.getEndDate().toString()));
				}
				empRec.put("identityNo", emp.getRegNo());

				int age = 0;
				if (emp.getDob() != null && !emp.getDob().equals("")) {
					String[] dateString = emp.getDob().toString().split("-");

					LocalDate today = LocalDate.now();
					LocalDate birthday = LocalDate.of(Integer.parseInt(dateString[0]), Integer.parseInt(dateString[1]),
							Integer.parseInt(dateString[2]));

					Period p = Period.between(birthday, today);
					age = p.getYears();
				}

				empRec.put("age", age);

				
				if (emp.getIdentificationTypeId() != null) {
					empRec.put("idType", emp.getIdentificationTypeId().getIdentificationTypeId());
				}
				if (emp.getIdentificationTypeNo() != null && !emp.getIdentificationTypeNo().equals("")) {
					empRec.put("idTypeNo", emp.getIdentificationTypeNo());
					empRec.put("idTypeName", emp.getIdentificationTypeId().getIdentificationName());
				}

				if (emp.getIdentificationTypeImage() != null && !emp.getIdentificationTypeImage().equals("")) {
					byte[] idImage = emp.getIdentificationTypeImage();
					String idImageString = encodeByteToBase64Binary(idImage);

					empRec.put("idImageString", idImageString);
				}

				String encodedStringImage = "";
				String mimeType = "";
				
				if (emp.getProfileImage() != null && !emp.getProfileImage().equals("")) {

					byte[] f = emp.getProfileImage();
					encodedStringImage = encodeByteToBase64Binary(f);

					empRec.put("encodedStringImage", encodedStringImage);
				}

				if (emp.getIdMimeType() != null && !emp.getIdMimeType().equals("")) {

					empRec.put("idMimeType", emp.getIdMimeType());

				}
				
				List<EmployeeQualification> empQualificationList = (List<EmployeeQualification>) qulificationMap
						.get("qualList");
				List<Object> qList = new ArrayList<Object>();

				long qualId = 0;
				String qualName = "";
				String instName = "";
				int completionYear = 0;
				String qualFilePath = "";
				String qualFileString = "";

				if (empQualificationList != null && empQualificationList.size() > 0) {
					for (Iterator<?> iterator = empQualificationList.iterator(); iterator.hasNext();) {
						Map<String, Object> qMap = new HashMap<String, Object>();

						EmployeeQualification qualification = (EmployeeQualification) iterator.next();

						qualId = qualification.getEmployeeQualificationId();

						if (qualification.getQualificationName() != null) {
							qualName = qualification.getQualificationName();
						}
						if (qualification.getInstitutionName() != null) {
							instName = qualification.getInstitutionName();
						}

						completionYear = qualification.getCompletionYear();

						if (qualification.getFilePath() != null) {
							qualFilePath = changePathFormat(qualification.getFilePath());
						}

						if (qualification.getDocument() != null) {
							byte[] doc = qualification.getDocument();
							qualFileString = encodeByteToBase64Binary(doc);
						}

						if (qualification.getMimeType() != null && !qualification.getMimeType().equals("")) {

							mimeType = qualification.getMimeType();

						}

						qMap.put("qualId", qualId);
						qMap.put("qualName", qualName);
						qMap.put("instName", instName);
						qMap.put("completionYear", completionYear);
						qMap.put("uploadedFile", qualFilePath);
						qMap.put("qualFileString", qualFileString);
						qMap.put("mimeType", mimeType);

						qList.add(qMap);

					}

				}

				List<EmployeeDocument> docList = (List<EmployeeDocument>) documentMap.get("docList");
				List<Object> dList = new ArrayList<Object>();

				long docId = 0;
				String docName = "";
				// String uploadedDocPath = "" ;
				String docFileString = "";

				if (docList != null && docList.size() > 0) {
					for (Iterator<?> iterator = docList.iterator(); iterator.hasNext();) {
						Map<String, Object> dMap = new HashMap<String, Object>();

						EmployeeDocument document = (EmployeeDocument) iterator.next();

						docId = document.getEmployeeDocumentId();
						if (document.getDocumentName() != null) {
							docName = document.getDocumentName();
						}

						/*
						 * if (document.getFilePath() != null) { uploadedDocPath =
						 * changePathFormat(document.getFilePath()); }
						 */

						if (document.getDocument() != null) {
							byte[] doc = document.getDocument();
							docFileString = encodeByteToBase64Binary(doc);
						}

						if (document.getMimeType() != null && !document.getMimeType().equals("")) {

							mimeType = document.getMimeType();

						}
						dMap.put("docId", docId);
						dMap.put("docName", docName);
						dMap.put("docFileString", docFileString);
						dMap.put("mimeType", mimeType);

						dList.add(dMap);

					}

				}

				
				
				json.put("data", empRec);
				json.put("qList", qList);
				json.put("dList", dList);
				
				json.put("status", "1");

			}

			catch (Exception e) {
				e.printStackTrace();
				return "{\"status\":\"0\",\"msg\":\"Somting went wrong}";
			}

		}
		return json.toString();
	
	}

	@Override
	public Map<String, Object> updateEmployee(Map<String, Object> requestData) {


		Map<String, Object> map = new HashMap<>();
		try {
			map = empRegistrationDao.updateEmployee(requestData);

			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	
	}
}