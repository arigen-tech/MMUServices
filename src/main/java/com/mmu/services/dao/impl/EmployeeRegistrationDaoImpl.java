package com.mmu.services.dao.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.jdbc.Work;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.mmu.services.dao.EmployeeRegistrationDao;
import com.mmu.services.entity.Attendance;
import com.mmu.services.entity.AuditAttendance;
import com.mmu.services.entity.AuditAttendanceData;
import com.mmu.services.entity.EmployeeDocument;
import com.mmu.services.entity.EmployeeQualification;
import com.mmu.services.entity.EmployeeRegistration;
import com.mmu.services.entity.MasAdministrativeSex;
import com.mmu.services.entity.MasCamp;
import com.mmu.services.entity.MasIdentificationType;
import com.mmu.services.entity.MasMMU;
import com.mmu.services.entity.MasUserType;
import com.mmu.services.entity.Users;
import com.mmu.services.hibernateutils.GetHibernateUtils;
import com.mmu.services.utils.HMSUtil;
import com.mmu.services.utils.ProjectUtils;

import sun.misc.BASE64Decoder;

@Repository
@Transactional
public class EmployeeRegistrationDaoImpl implements EmployeeRegistrationDao {

	String databaseScema = HMSUtil.getProperties("adt.properties", "currentSchema").trim();
	String entitUrl = HMSUtil.getProperties("adt.properties", "entitUrl").trim();

	@Autowired
	GetHibernateUtils getHibernateUtils;

	@Autowired
	private Environment environment;
	@Override
	public Map<String, Object> saveEmployee(Map<String, Object> requestData) {
		Map<String, Object> response = new HashMap<>();
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Transaction tx = session.beginTransaction();
		EmployeeRegistration employee = null;
		try {

			Users user = new Users();
			if (requestData.get("userId") != null && !requestData.get("userId").toString().equals("")) {

				Long userId = Long.parseLong(requestData.get("userId").toString());

				user.setUserId(userId);
			}

			if (requestData.get("empRegId") != null && !requestData.get("empRegId").toString().equals("")) {

				Long empRegId = Long.parseLong(requestData.get("empRegId").toString());
				employee = (EmployeeRegistration) session.load(EmployeeRegistration.class, empRegId);

				/*Query deleteQualification = session
						.createSQLQuery("delete from employee_qualification  where employee_id =" + empRegId);
				deleteQualification.executeUpdate();
				Query deleteDocument = session
						.createSQLQuery("delete from employee_document  where employee_id =" + empRegId);
				deleteDocument.executeUpdate();
*/
			} else {
				employee = new EmployeeRegistration();
			}

			String empName = String.valueOf(requestData.get("empName"));
			Long genderId = Long.parseLong(String.valueOf(requestData.get("genderId")));
			Date dob = formatDate(String.valueOf(requestData.get("dob")));
			String empAddress = String.valueOf(requestData.get("empAddress"));
			String empState = String.valueOf(requestData.get("empState"));
			String empDist = String.valueOf(requestData.get("empDist"));
			String empCity = String.valueOf(requestData.get("empCity"));
			Long pinCode = Long.parseLong(String.valueOf(requestData.get("empPincode")));
			String mobileNo = String.valueOf(requestData.get("empMobile"));
			String empIMEI = String.valueOf(requestData.get("empIMEI"));
			Long mmuId = Long.parseLong(String.valueOf(requestData.get("mmuId")));
			Long empTypeId = Long.parseLong(String.valueOf(requestData.get("empTypeId")));
			String employmentType = String.valueOf(requestData.get("employmentTypeId"));
			String recordAction = String.valueOf(requestData.get("action"));
			Long empIdType = Long.parseLong(String.valueOf(requestData.get("empIdType")));
			String empIdNumber = String.valueOf(requestData.get("empIdNumber"));
			String mimeTypeId = "";
			if(requestData.get("idMimeType") !=null) {
				mimeTypeId = String.valueOf(requestData.get("idMimeType"));
			}
			if(requestData.get("base64ForProfileStr") != null && !requestData.get("base64ForProfileStr").equals("")) {
				String base64ForProfileTmp= String.valueOf(requestData.get("base64ForProfileStr"));			
				String base64ForProfileStr = base64ForProfileTmp.substring(base64ForProfileTmp.indexOf(",")+1, base64ForProfileTmp.length());
				byte[] decodedProfileImage = Base64.decodeBase64(base64ForProfileStr);
				employee.setProfileImage(decodedProfileImage);
			}
			if(requestData.get("base64ForIdStr") != null && !requestData.get("base64ForIdStr").equals("")) {
				String base64ForIdStr = String.valueOf(requestData.get("base64ForIdStr"));
				byte[] decodedIdImage = Base64.decodeBase64(base64ForIdStr);
				if(decodedIdImage!=null)
				employee.setIdentificationTypeImage(decodedIdImage);
			}
			
			Date fromDate = formatDate(String.valueOf(requestData.get("fromDate")));
			Date toDate = null;
			if (requestData.get("toDate") != null && !requestData.get("toDate").toString().equals("")) {
				toDate = formatDate(String.valueOf(requestData.get("toDate")));
			}

			String empIdentity = String.valueOf(requestData.get("empIdentity"));

			employee.setEmployeeName(empName);
			if(mimeTypeId.length() < 10) {
			employee.setIdMimeType(mimeTypeId);
			}
			
			MasIdentificationType idType = new MasIdentificationType();
			idType.setIdentificationTypeId(empIdType);
			
			employee.setIdentificationTypeId(idType);
			employee.setIdentificationTypeNo(empIdNumber);

			MasAdministrativeSex m = new MasAdministrativeSex();
			m.setAdministrativeSexId(genderId);

			employee.setMasAdministrativeSex(m);
			employee.setAddress(empAddress);
			employee.setState(empState);
			employee.setDistrict(empDist);
			employee.setCity(empCity);
			employee.setPinCode(pinCode);
			employee.setMobileNo(mobileNo);
			employee.setImeiNumber(empIMEI);
			employee.setDob(dob);
			employee.setStartDate(fromDate);
			if (requestData.get("toDate") != null && !requestData.get("toDate").toString().equals("")) {
				employee.setEndDate(toDate);
			}
			employee.setRegNo(empIdentity);

			MasMMU mmu = new MasMMU();
			mmu.setMmuId(mmuId);
			employee.setMmuId(mmu);

			MasUserType userType = new MasUserType();
			userType.setUserTypeId(empTypeId);
			employee.setUserTypeId(userType);

			employee.setEmploymentType(employmentType);

			employee.setLastChgDate(HMSUtil.getCurrentTimeStamp());
			employee.setRecordStatus(recordAction);
			session.saveOrUpdate(employee);

			if (requestData.get("qualificationList") != null) {
				EmployeeQualification empQualification = null;

				@SuppressWarnings("unchecked")
				List<HashMap<String, Object>> qualificationList = (List<HashMap<String, Object>>) (Object) requestData
						.get("qualificationList");
				for (HashMap<String, Object> qualification : qualificationList) {

					if (qualification.get("qualId") != null && qualification.get("qualId") != "") {
						long qId = Long.parseLong(qualification.get("qualId").toString());
						if (qId > 0) {
							empQualification = (EmployeeQualification) session.load(EmployeeQualification.class, qId);
							;
						}
					} else {
						empQualification = new EmployeeQualification();
					}
					if(empQualification==null)
					empQualification = new EmployeeQualification();
					if (qualification.get("degree") != null && qualification.get("degree") != "") {
						empQualification.setQualificationName(String.valueOf(qualification.get("degree").toString()));
					}
					if (qualification.get("instName") != null && qualification.get("instName") != "") {
						empQualification.setInstitutionName(String.valueOf(qualification.get("instName").toString()));
					}
					if (qualification.get("completeionYear") != null && qualification.get("completeionYear") != "") {
						empQualification
								.setCompletionYear(Integer.valueOf(qualification.get("completeionYear").toString()));
					}
					if (qualification.get("completeionYear") != null && qualification.get("completeionYear") != "") {
						empQualification
								.setCompletionYear(Integer.valueOf(qualification.get("completeionYear").toString()));
					}
					String mimeTypeEdu = "";
					if (qualification.get("mimeTypeEduDoc") != null && qualification.get("mimeTypeEduDoc") != "") {
						mimeTypeEdu = String.valueOf(qualification.get("mimeTypeEduDoc").toString());
						if(mimeTypeEdu.length() < 15) {
						empQualification.setMimeType(mimeTypeEdu);
						}
					}
					if (qualification.get("base64ForEduDoc") != null && !qualification.get("base64ForEduDoc").equals("")) {
						String base64ForEduDocTmp= String.valueOf(qualification.get("base64ForEduDoc"));	
						//System.out.println("base64ForEduDocTmp string---> "+base64ForEduDocTmp);
						//String [] s = splitBase64String(base64ForEduDocTmp,",");
						//String mimeTypeQual = getMimeType(s);
						byte[] base64ForEduDoc = Base64.decodeBase64(base64ForEduDocTmp);
						if(mimeTypeEdu.length() < 15) {
						empQualification.setDocument(base64ForEduDoc);
						}
					}
					
					empQualification.setEmployeeId(employee);

					if (user != null) {
						empQualification.setLastChgBy(user);
					}

					empQualification.setLastChgDate(HMSUtil.getCurrentTimeStamp());
					if (qualification.get("degree") != null && qualification.get("degree") != ""
							&& qualification.get("instName") != null && qualification.get("instName") != ""
							&& qualification.get("completeionYear") != null
							&& qualification.get("completeionYear") != "") {
						session.saveOrUpdate(empQualification);
					}

				}
			}

			if (requestData.get("documentList") != null) {
				List<HashMap<String, Object>> documentList = (List<HashMap<String, Object>>) (Object) requestData
						.get("documentList");
				EmployeeDocument emplDocument = null;
				for (HashMap<String, Object> doc : documentList) {

					if (doc.get("docId") != null && doc.get("docId") != "") {
						long docId = Long.parseLong(doc.get("docId").toString());
						if (docId > 0) {
							emplDocument = (EmployeeDocument) session.load(EmployeeDocument.class, docId);
							;
						}
					} else {
						emplDocument = new EmployeeDocument();
					}
					if(emplDocument==null)
					emplDocument = new EmployeeDocument();
					if (doc.get("docName") != null && doc.get("docName") != "") {
						emplDocument.setDocumentName(String.valueOf(doc.get("docName").toString()));
					}

					emplDocument.setEmployeeId(employee);

					if (user != null) {
						emplDocument.setLastChgBy(user);
					}

					emplDocument.setLastChgDate(HMSUtil.getCurrentTimeStamp());
					String docMime = "";
					if (doc.get("mimeTypeRequireDoc") != null && !doc.get("mimeTypeRequireDoc").equals("")) {
						docMime = String.valueOf(doc.get("mimeTypeRequireDoc").toString());
						if(docMime.length() < 15) {
						emplDocument.setMimeType(docMime);
						}
					}
					if (doc.get("base64ForRequireDoc") != null && !doc.get("base64ForRequireDoc").equals("")) {
						String base64ForEduDocTmp= String.valueOf(doc.get("base64ForRequireDoc"));	
						//System.out.println("base64ForEduDocTmp string---> "+base64ForEduDocTmp);
						//String [] s = splitBase64String(base64ForEduDocTmp,",");
						//String mimeTypeDoc = getMimeType(s);
						//System.out.println("mimeTypeDoc---> "+mimeTypeDoc);
						byte[] base64ForEduDoc = Base64.decodeBase64(base64ForEduDocTmp);
						emplDocument.setDocument(base64ForEduDoc);
						}
					
					
				//	if (doc.get("docName") != null && !doc.get("docName").equals("") && doc.get("base64ForRequireDoc") != null && !doc.get("base64ForRequireDoc").equals("")
						//	&& docMime.length() < 15) {
					if (doc.get("docName") != null && !doc.get("docName").equals("")) {
						
						session.saveOrUpdate(emplDocument);
					}

				}
			}

			response.put("status", true);
			if (recordAction.equalsIgnoreCase("saved")) {
				response.put("msg", "The employee details are saved");
			} else {
				response.put("msg", "The employee is registered and record is forwarded for validation");
			}
			tx.commit();
			return response;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		response.put("status", false);
		response.put("msg", "Something went wrong");
		return response;
	}


	private String getMimeType(String[] stringToSplit) {
		String mimeTemp = stringToSplit[0];
		String[] splitMimeType = mimeTemp.split("/");
		String[] mimeType = splitMimeType[1].split(";");
		System.out.println("mime type0000:- "+mimeType[0]);
		return mimeType[0];
	}
	 

	/*
	 * @Override public Map<String, Object> saveEmployee(Map<String, Object>
	 * getjsondata) {
	 * 
	 * Map<String, Object> response = new HashMap<>(); Session session =
	 * getHibernateUtils.getHibernateUtlis().OpenSession(); Transaction tx =
	 * session.beginTransaction(); EmployeeRegistration employee = null;
	 * 
	 * JSONArray newForm=new
	 * JSONArray(getjsondata.get("registrationForm").toString());
	 * 
	 * JSONObject requestData = (JSONObject) newForm.get(0);
	 * 
	 * 
	 * String qList = String.valueOf(requestData.get("qualificationList")); String
	 * dList = String.valueOf(requestData.get("documentList"));
	 * 
	 * String profileImagePath = ""; if(getjsondata.get("profileImagePath") != null
	 * && !getjsondata.get("profileImagePath").equals("")) { profileImagePath =
	 * getjsondata.get("profileImagePath").toString(); }
	 * 
	 * String qualificationDocPath =
	 * getjsondata.get("qualificationDocPath").toString(); String requiredDocPath =
	 * getjsondata.get("requiredDocPath").toString();
	 * 
	 * 
	 * try {
	 * 
	 * Users user = new Users(); if (requestData.get("userId") != null &&
	 * !requestData.get("userId").toString().equals("")) {
	 * 
	 * Long userId = Long.parseLong(requestData.get("userId").toString());
	 * 
	 * user.setUserId(userId); }
	 * 
	 * 
	 * if (requestData.get("empRegId") != null &&
	 * !requestData.get("empRegId").toString().equals("")) {
	 * 
	 * Long empRegId = Long.parseLong(requestData.get("empRegId").toString());
	 * employee = (EmployeeRegistration) session.load(EmployeeRegistration.class,
	 * empRegId);
	 * 
	 * Query deleteQualification = session.
	 * createSQLQuery("delete from employee_qualification  where employee_id ="
	 * +empRegId ); deleteQualification.executeUpdate(); Query deleteDocument =
	 * session.createSQLQuery("delete from employee_document  where employee_id ="
	 * +empRegId ); deleteDocument.executeUpdate();
	 * 
	 * }else { employee = new EmployeeRegistration(); }
	 * 
	 * 
	 * 
	 * String empName = String.valueOf(requestData.get("empName")); Long genderId =
	 * Long.parseLong(String.valueOf(requestData.get("genderId"))); Date dob =
	 * formatDate(String.valueOf(requestData.get("dob"))); String empAddress =
	 * String.valueOf(requestData.get("empAddress")); String empState =
	 * String.valueOf(requestData.get("empState")); String empDist =
	 * String.valueOf(requestData.get("empDist")); String empCity =
	 * String.valueOf(requestData.get("empCity")); int pinCode =
	 * Integer.parseInt(String.valueOf(requestData.get("empPincode"))); String
	 * mobileNo = String.valueOf(requestData.get("empMobile")); String empIMEI =
	 * String.valueOf(requestData.get("empIMEI")); Long mmuId =
	 * Long.parseLong(String.valueOf(requestData.get("mmuId"))); Long empTypeId =
	 * Long.parseLong(String.valueOf(requestData.get("empTypeId"))); String
	 * employmentType = String.valueOf(requestData.get("employmentTypeId")); String
	 * recordAction = String.valueOf(requestData.get("action"));
	 * 
	 * Date fromDate = formatDate(String.valueOf(requestData.get("fromDate"))); Date
	 * toDate = null; if(requestData.get("toDate") !=null &&
	 * !requestData.get("toDate").toString().equals("")) { toDate =
	 * formatDate(String.valueOf(requestData.get("toDate"))); }
	 * 
	 * String empIdentity = String.valueOf(requestData.get("empIdentity"));
	 * 
	 * employee.setEmployeeName(empName);
	 * 
	 * MasAdministrativeSex m = new MasAdministrativeSex();
	 * m.setAdministrativeSexId(genderId);
	 * 
	 * employee.setMasAdministrativeSex(m); employee.setAddress(empAddress);
	 * employee.setState(empState); employee.setDistrict(empDist);
	 * employee.setCity(empCity); employee.setPinCode(pinCode);
	 * employee.setMobileNo(mobileNo); employee.setImeiNumber(empIMEI);
	 * employee.setDob(dob); employee.setStartDate(fromDate);
	 * if(requestData.get("toDate") !=null &&
	 * !requestData.get("toDate").toString().equals("")) {
	 * employee.setEndDate(toDate); } employee.setRegNo(empIdentity);
	 * 
	 * MasMMU mmu = new MasMMU(); mmu.setMmuId(mmuId); employee.setMmuId(mmu);
	 * 
	 * MasUserType userType = new MasUserType(); userType.setUserTypeId(empTypeId);
	 * employee.setUserTypeId(userType);
	 * 
	 * employee.setEmploymentType(employmentType);
	 * 
	 * employee.setLastChgDate(HMSUtil.getCurrentTimeStamp());
	 * employee.setRecordStatus(recordAction); if(!profileImagePath.equals("")) {
	 * employee.setFilePath(profileImagePath); }
	 * 
	 * 
	 * session.saveOrUpdate(employee);
	 * 
	 * 
	 * JSONArray qualificationArray = new JSONArray(qList); JSONArray docArray = new
	 * JSONArray(dList);
	 * System.out.println("qualificationDocPath---> "+qualificationDocPath);
	 * if(qualificationArray.length() > 0) { if (qualificationDocPath.endsWith(","))
	 * { qualificationDocPath = qualificationDocPath.substring(0,
	 * qualificationDocPath.length() - 1); } String [] qualificationDocPaths =
	 * qualificationDocPath.split(","); for(int i=0; i <
	 * qualificationArray.length(); i++) { JSONObject object =
	 * qualificationArray.getJSONObject(i);
	 * System.out.println(object.getString("degree"));
	 * System.out.println(object.getString("completeionYear"));
	 * 
	 * EmployeeQualification empQualification = new EmployeeQualification(); if
	 * (object.get("degree") != null && object.get("degree") != "") {
	 * empQualification.setQualificationName(String.valueOf(object.get("degree").
	 * toString())); } if (object.get("instName") != null && object.get("instName")
	 * != "") {
	 * empQualification.setInstitutionName(String.valueOf(object.get("instName").
	 * toString())); } if (object.get("completeionYear") != null &&
	 * object.get("completeionYear") != "") {
	 * empQualification.setCompletionYear(Integer.valueOf(object.get(
	 * "completeionYear").toString())); }
	 * 
	 * empQualification.setEmployeeId(employee);
	 * 
	 * if(user !=null) { empQualification.setLastChgBy(user); }
	 * 
	 * empQualification.setLastChgDate(HMSUtil.getCurrentTimeStamp()); if(i <=
	 * qualificationDocPaths.length && qualificationDocPaths[i] != null &&
	 * !qualificationDocPaths[i].equals("")) {
	 * empQualification.setFilePath(qualificationDocPaths[i]); }
	 * if(object.get("degree") != null && object.get("degree") != "" &&
	 * object.get("instName") != null && object.get("instName") != "" &&
	 * object.get("completeionYear") != null && object.get("completeionYear") != "")
	 * { session.saveOrUpdate(empQualification); }
	 * 
	 * }
	 * 
	 * }
	 * 
	 * 
	 * 
	 * if (docArray.length() > 0) { if (requiredDocPath.endsWith(",")) {
	 * requiredDocPath = requiredDocPath.substring(0, requiredDocPath.length() - 1);
	 * } String [] requiredDocPaths = requiredDocPath.split(",");
	 * 
	 * for(int i=0; i < docArray.length(); i++) {
	 * 
	 * JSONObject object = docArray.getJSONObject(i); EmployeeDocument emplDocument
	 * = new EmployeeDocument(); if (object.get("docName") != null &&
	 * object.get("docName") != "") {
	 * emplDocument.setDocumentName(String.valueOf(object.get("docName").toString())
	 * ); }
	 * 
	 * emplDocument.setEmployeeId(employee);
	 * 
	 * if(user !=null) { emplDocument.setLastChgBy(user); }
	 * 
	 * emplDocument.setLastChgDate(HMSUtil.getCurrentTimeStamp()); if(i <=
	 * requiredDocPaths.length && requiredDocPaths[i] != null &&
	 * !requiredDocPaths[i].equals("")) {
	 * emplDocument.setFilePath(requiredDocPaths[i]); }
	 * 
	 * if(object.get("docName") != null && !object.get("docName").equals("")) {
	 * session.saveOrUpdate(emplDocument); } }
	 * 
	 * }
	 * 
	 * 
	 * response.put("status", true); response.put("msg", "Employee is registered");
	 * tx.commit(); return response;
	 * 
	 * 
	 * }catch (Exception e) { e.printStackTrace(); }finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); }
	 * response.put("status", false); response.put("msg", "Something went wrong");
	 * return response; }
	 */
	
	
	private Date formatDate(String inDate) {

		String[] dateString = inDate.split("/");
		ZoneId defaultZoneId = ZoneId.systemDefault();
		LocalDate localDate = LocalDate.of(Integer.parseInt(dateString[2]), Integer.parseInt(dateString[1]),
				Integer.parseInt(dateString[0]));
		Date date = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
		return date;
	}

	private Date formatDateYYYYMMDD(String inDate) {

		String[] dateString = inDate.split("-");
		ZoneId defaultZoneId = ZoneId.systemDefault();
		LocalDate localDate = LocalDate.of(Integer.parseInt(dateString[0]), Integer.parseInt(dateString[1]),
				Integer.parseInt(dateString[2]));
		Date date = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
		return date;
	}
	
	@Override
	public Map<String, Object> getEmpList(HashMap<String, Object> jsonData) {

		List<EmployeeRegistration> list = null;
		Map<String, Object> map = new HashMap<String, Object>();

		try {

			Criteria cr = null;
			int pageNo = Integer.parseInt(jsonData.get("pageNo") + "");

			String mobileNo = (String) jsonData.get("mobileNo");
			String empName = (String) jsonData.get("empName");
			String calledFor = (String) jsonData.get("calledFor");
			Long userId = Long.parseLong(jsonData.get("userId") + "");
			
			String hqlQery="";
			int pagingSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
			int count = 0;

			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			
			Criteria c =  session.createCriteria(Users.class)
					            .add(Restrictions.eq("userId", userId));
			
			List<Users> userList = c.list();
			Users user = null;
			if(userList.size() > 0) {
				 user = userList.get(0);
			}
			
			String mmu = user.getMmuId();
			
			List<Long> mmuId = null;
			if(mmu != null && !mmu.equals("")) {
			 mmuId = Stream.of(mmu.split(","))
	                .map(Long::parseLong)
	                .collect(Collectors.toList());
			}

			cr = session.createCriteria(EmployeeRegistration.class).addOrder(Order.asc("employeeId"));
			hqlQery="select employee_id from mas_employee";
			if (calledFor.equalsIgnoreCase("apmApproval")) {
				cr.add(Restrictions.like("recordStatus", "submitted"));
				cr.add(Restrictions.isNull("apmFlag"));
				hqlQery="select employee_id from mas_employee where record_status='submitted' and apm_flag is null ";
				if(mmu != null && !mmu.equals("")) {
					cr.createAlias("mmuId", "mmuId");
					cr.add(Restrictions.in("mmuId.mmuId", mmuId));
					String lastMmu = mmu.substring(0, mmu.lastIndexOf(","));
					hqlQery="select employee_id from mas_employee where record_status='submitted' and apm_flag is null and mmu_id in("+lastMmu+")";
				}
             
			} else if (calledFor.equalsIgnoreCase("audApproval")) {
				cr.add(Restrictions.like("recordStatus", "submitted"));
				cr.add(Restrictions.isNull("auditorFlag"));
			hqlQery="select employee_id from mas_employee where record_status='submitted' and apm_flag is null";
			} else if (calledFor.equalsIgnoreCase("chmoApproval")) {
				cr.add(Restrictions.isNull("chmoFlag"))
						.add(Restrictions.and(Restrictions.isNotNull("auditorFlag"),
								Restrictions.eq("auditorFlag", "A")))
						.add(Restrictions.and(Restrictions.isNotNull("apmFlag"), Restrictions.eq("apmFlag", "A")));
				hqlQery="select employee_id from mas_employee where chmo_flag is null and auditor_flag is not null and auditor_flag='A'";

			} else if (calledFor.equalsIgnoreCase("upssApproval")) {
				cr.add(Restrictions.isNull("upssFlag"))
						.add(Restrictions.and(Restrictions.isNotNull("chmoFlag"), Restrictions.eq("chmoFlag", "A")));
				hqlQery="select employee_id from mas_employee where upss_flag is null and chmo_flag is not null and chmo_flag='A'";

			}

			if (mobileNo != null && !mobileNo.equals("") && !mobileNo.equals("null")) {
				String mNumber = "%" + mobileNo + "%";
				cr.add(Restrictions.like("mobileNo", mNumber));
			}
			if (empName != null && !empName.equals("") && !empName.equals("null")) {
				String eName = "%" + empName + "%";
				cr.add(Restrictions.like("employeeName", eName).ignoreCase());
			}

			Query queryHiber = (Query) session.createSQLQuery(hqlQery);
			List<Object[]> objectList = (List<Object[]>) queryHiber.list();
			System.out.println("objectList="+objectList.size());
			//list = cr.list();
			//count = 115;
			map.put("count", objectList.size());
			cr = cr.setFirstResult(pagingSize * (pageNo - 1));
	    	cr = cr.setMaxResults(pagingSize);
			list = cr.list();

			map.put("list", list);
			getHibernateUtils.getHibernateUtlis().CloseConnection();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return map;

	}

	@Override
	public Map<String, Object> getEmployeeDetails(HashMap<String, String> jsondata) {
		Map<String, Object> map = new HashMap<String, Object>();

		try {

			Criteria cr = null;

			long empRecId = Long.parseLong((String) jsondata.get("empRecId"));

			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();

			cr = session.createCriteria(EmployeeRegistration.class).addOrder(Order.asc("employeeId"));
			cr.add(Restrictions.eq("employeeId", empRecId));

			EmployeeRegistration emp = (EmployeeRegistration) cr.list().get(0);

			map.put("emp", emp);
			getHibernateUtils.getHibernateUtlis().CloseConnection();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return map;
	}

	@Override
	public Map<String, Object> saveAPMAction(Map<String, Object> requestData) {
		System.out.println("saveapm");
		Map<String, Object> response = new HashMap<>();
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Transaction tx = session.beginTransaction();
		JSONObject jsonEmp = new JSONObject();
		boolean msgFlag=false;
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
		//String entitUrl = HMSUtil.getProperties("adt.properties", "entitUrl").trim();
		System.out.println("entitUrl ---> "+entitUrl);
		try {

			Users user = new Users();
			if (requestData.get("userId") != null && !requestData.get("userId").toString().equals("")) {

				Long userId = Long.parseLong(requestData.get("userId").toString());

				//user.setUserId(userId);
				user = (Users) session.load(Users.class, userId);
			}

			if (requestData.get("empRegId") != null && !requestData.get("empRegId").toString().equals("")) {

				Long empRegId = Long.parseLong(requestData.get("empRegId").toString());
				EmployeeRegistration employee = (EmployeeRegistration) session.load(EmployeeRegistration.class,
						empRegId);

				if (user != null) {
					employee.setApmId(user);
					
				}

				String apmAction = String.valueOf(requestData.get("apmAction"));
				String apmRemarks = String.valueOf(requestData.get("apmRemarks"));

				employee.setApmFlag(apmAction);
				employee.setApmRemarks(apmRemarks);

				employee.setApmDate(HMSUtil.getCurrentTimeStamp());
				session.saveOrUpdate(employee);
				String passwordGenerate = generatePassword(6);
				Users userRecord = null;
				
				SQLQuery  query  = session.createSQLQuery("select role_id from mas_role_usertype_mapping where user_type_id ="+employee.getUserTypeId().getUserTypeId() ); 
				
				List<String> rolesRows = query .list();
				System.out.println("rolesRows size--> "+rolesRows.size());
				
				StringBuilder role = new StringBuilder();
				
				for(String row : rolesRows){
					role.append(row).append(",");
				}
				
				

				/*
				 * Criteria cr = session.createCriteria(Users.class).createAlias("employeeId",
				 * "emp") //.add(Restrictions.eq("emp.employeeId", empRegId))
				 * .add(Restrictions.or(Restrictions.eq("emp.employeeId", empRegId),
				 * Restrictions.eq("mobileNo", employee.getMobileNo())))
				 * .add(Restrictions.or(Restrictions.eq("userFlag", new Long(0)),
				 * Restrictions.eq("userFlag", new Long(1))));
				 */

				List<Users> list = new ArrayList<Users>();
				if (list.size() > 0) {
					userRecord = list.get(0);
				}
				
				System.out.println("existingusser="+list.size());

				Timestamp lastChgDate = new Timestamp(new Date().getTime());
				if (userRecord != null) {
					if (apmAction.equalsIgnoreCase("R")) {
						JSONObject jsonEmpInactive = new JSONObject();
						jsonEmpInactive.put("mobile_number", employee.getMobileNo());
						jsonEmpInactive.put("validate_by", user.getMobileNo());
						jsonEmpInactive.put("validate_status", "0");
						jsonEmpInactive.put("emp_flag", "3");
						
						HttpEntity request = new HttpEntity(jsonEmpInactive.toString(), headers);
						ResponseEntity<String> apiResponse = new RestTemplate().exchange(entitUrl, HttpMethod.POST, request,
								String.class);
						
						 String entitResponse = apiResponse.getBody();
						 System.out.println("entitResponse for inactive Employee---> "+entitResponse);
						
						
						userRecord.setUserFlag(new Long(2));
					}
					if(userRecord.getPassword() != null && !userRecord.getPassword().equals("")) {
						msgFlag = true;
					}

				} else {
					
					System.out.println("sending data to Nistha");
					userRecord = new Users();
					userRecord.setLastChgDate(lastChgDate);
					userRecord.setAdminFlag("U");
					userRecord.setLoginName(employee.getMobileNo());
					userRecord.setUserName(employee.getEmployeeName());
					if(!msgFlag) {
						userRecord.setPassword(passwordGenerate);
					}
					if(rolesRows.size() > 0) {
						userRecord.setRoleId(role.toString());
					}
					userRecord.setMobileNo(employee.getMobileNo());
					userRecord.setEmployeeIdValue(empRegId);
					userRecord.setUserTypeId(employee.getUserTypeId().getUserTypeId());
					userRecord.setMmuId(employee.getMmuId().getMmuId().toString().concat(","));
					userRecord.setLevelOfUser("M");
					String validateStatus = "";
					if (apmAction.equalsIgnoreCase("A")) {
						userRecord.setUserFlag(new Long(0));
						validateStatus = "1";
					} else {
						userRecord.setUserFlag(new Long(2));
						validateStatus = "0";
					}
					
					// data to send to Nishtha
					
					String[] empName = employee.getEmployeeName().split(" ");
					String firstName = "";
					String lastName = "";
					if (empName.length == 1) {
						firstName = empName[0];
						lastName = "";
					} else if (empName.length == 2) {
						firstName = empName[0];
						lastName = empName[1];
					} else if (empName.length == 3) {
						firstName = empName[0] + " " + empName[1];
						lastName = empName[2];
					}

					jsonEmp.put("fname", firstName);
					jsonEmp.put("lname", lastName);
					if (employee.getProfileImage() != null && !employee.getProfileImage().equals("")) {
						byte[] profileImage = employee.getProfileImage();
						String profilePhoto = encodeByteToBase64Binary(profileImage);

						jsonEmp.put("photo", profilePhoto);
					}
					jsonEmp.put("mobile_number", employee.getMobileNo());
					jsonEmp.put("designation", employee.getUserTypeId().getDesignation().toString());
					jsonEmp.put("department", "220");
					jsonEmp.put("ofc_id", employee.getMmuId().getOfficeId().toString());
					jsonEmp.put("idType", employee.getIdentificationTypeId().getMappedId().toString());
					jsonEmp.put("otherIdNumber", employee.getIdentificationTypeNo().toString());
					if (employee.getIdentificationTypeImage() != null && !employee.getIdentificationTypeImage().equals("")) {

						byte[] idImage = employee.getIdentificationTypeImage();
						String otherIdImage = encodeByteToBase64Binary(idImage);

						jsonEmp.put("otherIdImage", otherIdImage);
					}
					jsonEmp.put("validate_by", user.getMobileNo());
					jsonEmp.put("validate_status", validateStatus);
					jsonEmp.put("emp_flag", "1");
					System.out.println("json for new --> "+jsonEmp.toString());
					
					HttpEntity request = new HttpEntity(jsonEmp.toString(), headers);
					ResponseEntity<String> apiResponse = new RestTemplate().exchange(entitUrl, HttpMethod.POST, request,
							String.class);
					
					 String entitResponse = apiResponse.getBody();
					 System.out.println("entitResponse ---> "+entitResponse);

				}
				session.saveOrUpdate(userRecord);
				Long id = userRecord.getUserId();

				if (id != 0 && !apmAction.equalsIgnoreCase("R") && !msgFlag) {
					
					
					sendSMSNew(employee.getMobileNo(),employee.getEmployeeName(),passwordGenerate);
					
				}	

				
				response.put("status", true);
				response.put("msg", "Employee status is updated");
				
				tx.commit();
				return response;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		response.put("status", false);
		response.put("msg", "Some error occurred");
		return response;

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

	@Override
	public Map<String, Object> saveAUDAction(Map<String, Object> requestData) {

		Map<String, Object> response = new HashMap<>();
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Transaction tx = session.beginTransaction();
		boolean msgFlag=false;
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
		//String entitUrl = HMSUtil.getProperties("adt.properties", "entitUrl").trim();
		System.out.println("entitUrl ---> "+entitUrl);
		try {

			Users user = new Users();
			if (requestData.get("userId") != null && !requestData.get("userId").toString().equals("")) {

				Long userId = Long.parseLong(requestData.get("userId").toString());

				//user.setUserId(userId);
				user = (Users) session.load(Users.class, userId);
			}

			if (requestData.get("empRegId") != null && !requestData.get("empRegId").toString().equals("")) {

				Long empRegId = Long.parseLong(requestData.get("empRegId").toString());
				EmployeeRegistration employee = (EmployeeRegistration) session.load(EmployeeRegistration.class,
						empRegId);

				if (user != null) {
					employee.setAuditorId(user);
				}

				String audAction = String.valueOf(requestData.get("audAction"));
				String audRemarks = String.valueOf(requestData.get("audRemarks"));

				employee.setAuditorFlag(audAction);
				employee.setAuditorRemarks(audRemarks);

				employee.setAuditorDate(HMSUtil.getCurrentTimeStamp());
				session.saveOrUpdate(employee);

				String passwordGenerate = generatePassword(6);
				Users userRecord = null;
				List<Users> list = null;
				
				SQLQuery  query  = session.createSQLQuery("select role_id from mas_role_usertype_mapping where user_type_id ="+employee.getUserTypeId().getUserTypeId() ); 
				
				List<String> rolesRows = query .list();
				System.out.println("rolesRows size--> "+rolesRows.size());
				
				StringBuilder role = new StringBuilder();
				
				for(String row : rolesRows){
					role.append(row).append(",");
				}
				
				

				Criteria cr = session.createCriteria(Users.class).createAlias("employeeId", "emp")
						//.add(Restrictions.eq("emp.employeeId", empRegId));
						.add(Restrictions.or(Restrictions.eq("emp.employeeId", empRegId), Restrictions.eq("mobileNo", employee.getMobileNo())))
						.add(Restrictions.or(Restrictions.eq("userFlag", new Long(0)), Restrictions.eq("userFlag", new Long(1))));

				list = cr.list();
				if (list.size() > 0) {
					userRecord = list.get(0);
				}
				System.out.println("existing="+list.size());
				Timestamp lastChgDate = new Timestamp(new Date().getTime());
				if (userRecord != null) {
					if (audAction.equalsIgnoreCase("R")) {
						
						userRecord.setUserFlag(new Long(2));
						
						JSONObject jsonEmpInactive = new JSONObject();
						jsonEmpInactive.put("mobile_number", employee.getMobileNo());
						jsonEmpInactive.put("validate_by", user.getMobileNo());
						jsonEmpInactive.put("validate_status", "0");
						jsonEmpInactive.put("emp_flag", "3");
						System.out.println("jsonEmpInactive--> "+jsonEmpInactive.toString());
						HttpEntity request = new HttpEntity(jsonEmpInactive.toString(), headers);
						ResponseEntity<String> apiResponse = new RestTemplate().exchange(entitUrl, HttpMethod.POST, request,
								String.class);
						
						 String entitResponse = apiResponse.getBody();
						 System.out.println("entitResponse for inactive Employee---> "+entitResponse);

					}
					if(userRecord.getPassword() != null && !userRecord.getPassword().equals("")) {
						msgFlag = true;
					}

					
					
				} else {
					System.out.println("sending to nis");
					userRecord = new Users();
					userRecord.setLastChgDate(lastChgDate);
					userRecord.setAdminFlag("U");
					userRecord.setLoginName(employee.getMobileNo());
					userRecord.setUserName(employee.getEmployeeName());
					if(!msgFlag) {
						userRecord.setPassword(passwordGenerate);
					}
					if(rolesRows.size() > 0) {
						userRecord.setRoleId(role.toString());
					}
					userRecord.setMobileNo(employee.getMobileNo());
					userRecord.setEmployeeIdValue(empRegId);
					userRecord.setUserTypeId(employee.getUserTypeId().getUserTypeId());
					userRecord.setMmuId(employee.getMmuId().getMmuId().toString());
					userRecord.setLevelOfUser("M");
					String validateStatus = "";
					if (audAction.equalsIgnoreCase("A")) {
						userRecord.setUserFlag(new Long(0));
						validateStatus = "1";

					} else {
						userRecord.setUserFlag(new Long(2));
						validateStatus = "0";
					}
					
					// data to send to Nishtha
					JSONObject jsonEmp = new JSONObject();
					String[] empName = employee.getEmployeeName().split(" ");
					String firstName = "";
					String lastName = "";
					if (empName.length == 1) {
						firstName = empName[0];
						lastName = "";
					} else if (empName.length == 2) {
						firstName = empName[0];
						lastName = empName[1];
					} else if (empName.length == 3) {
						firstName = empName[0] + " " + empName[1];
						lastName = empName[2];
					}

					jsonEmp.put("fname", firstName);
					jsonEmp.put("lname", lastName);
					if (employee.getProfileImage() != null && !employee.getProfileImage().equals("")) {
						byte[] profileImage = employee.getProfileImage();
						String profilePhoto = encodeByteToBase64Binary(profileImage);

						jsonEmp.put("photo", profilePhoto);
					}
					jsonEmp.put("mobile_number", employee.getMobileNo());
					jsonEmp.put("designation", employee.getUserTypeId().getDesignation().toString());
					jsonEmp.put("department", "220");
					jsonEmp.put("ofc_id", employee.getMmuId().getOfficeId().toString());
					jsonEmp.put("idType", employee.getIdentificationTypeId().getMappedId().toString());
					jsonEmp.put("otherIdNumber", employee.getIdentificationTypeNo().toString());
					if (employee.getIdentificationTypeImage() != null && !employee.getIdentificationTypeImage().equals("")) {

						byte[] idImage = employee.getIdentificationTypeImage();
						String otherIdImage = encodeByteToBase64Binary(idImage);

						jsonEmp.put("otherIdImage", otherIdImage);
					}
					jsonEmp.put("validate_by", user.getMobileNo());
					jsonEmp.put("validate_status", validateStatus);
					jsonEmp.put("emp_flag", "1");
					//System.out.println("json for new --> "+jsonEmp.toString());
					
					HttpEntity request = new HttpEntity(jsonEmp.toString(), headers);
					ResponseEntity<String> apiResponse = new RestTemplate().exchange(entitUrl, HttpMethod.POST, request,
							String.class);
					
					 String entitResponse = apiResponse.getBody();
					 System.out.println("entitResponse ---> "+entitResponse);

				}
				session.saveOrUpdate(userRecord);
				Long id = userRecord.getUserId();

				if (id != 0 && !audAction.equalsIgnoreCase("R") && !msgFlag) {
					
					sendSMSNew(employee.getMobileNo(),employee.getEmployeeName(),passwordGenerate);
				}

				response.put("status", true);
				response.put("msg", "Employee status is updated");
				tx.commit();
				return response;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		response.put("status", false);
		response.put("msg", "Some error occurred");
		return response;

	}

	@Override
	public Map<String, Object> saveCHMOAction(Map<String, Object> requestData) {

		Map<String, Object> response = new HashMap<>();
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Transaction tx = session.beginTransaction();
		try {

			Users user = new Users();
			if (requestData.get("userId") != null && !requestData.get("userId").toString().equals("")) {

				Long userId = Long.parseLong(requestData.get("userId").toString());

				//user.setUserId(userId);
				user = (Users) session.load(Users.class, userId);
			}

			if (requestData.get("empRegId") != null && !requestData.get("empRegId").toString().equals("")) {

				Long empRegId = Long.parseLong(requestData.get("empRegId").toString());
				EmployeeRegistration employee = (EmployeeRegistration) session.load(EmployeeRegistration.class,
						empRegId);

				if (user != null) {
					employee.setChmoId(user);
				}

				String chmoAction = String.valueOf(requestData.get("chmoAction"));
				String chmoRemarks = String.valueOf(requestData.get("chmoRemarks"));

				employee.setChmoFlag(chmoAction);
				employee.setChmoRemarks(chmoRemarks);

				employee.setChmoDate(HMSUtil.getCurrentTimeStamp());
				session.saveOrUpdate(employee);

				Users userRecord = null;
				List<Users> list = null;

				Criteria cr = session.createCriteria(Users.class).createAlias("employeeId", "emp")
						.add(Restrictions.eq("emp.employeeId", empRegId));

				list = cr.list();
				if (list.size() > 0) {
					userRecord = list.get(0);
				}

				Timestamp lastChgDate = new Timestamp(new Date().getTime());
				if (userRecord != null) {
					if (chmoAction.equalsIgnoreCase("R")) {
						userRecord.setUserFlag(new Long(2));
						userRecord.setLastChgDate(lastChgDate);
						session.saveOrUpdate(userRecord);

					}

				}
				String validateStatus = "";
				if (chmoAction.equalsIgnoreCase("A")) {
					validateStatus = "1";
				} else {
					validateStatus = "0";
				}
				HttpHeaders headers = new HttpHeaders();
				headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
				//String entitUrl = HMSUtil.getProperties("adt.properties", "entitUrl").trim();
				
				JSONObject jsonEmpInactive = new JSONObject();
				jsonEmpInactive.put("mobile_number", employee.getMobileNo());
				jsonEmpInactive.put("validate_by", user.getMobileNo());
				jsonEmpInactive.put("validate_status", validateStatus);
				jsonEmpInactive.put("emp_flag", "3");
				
				HttpEntity request = new HttpEntity(jsonEmpInactive.toString(), headers);
				ResponseEntity<String> apiResponse = new RestTemplate().exchange(entitUrl, HttpMethod.POST, request,
						String.class);
				
				 String entitResponse = apiResponse.getBody();
				 System.out.println("entitResponse ---> "+entitResponse);
				
				response.put("status", true);
				response.put("msg", "Employee status is updated");
				tx.commit();
				return response;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		response.put("status", false);
		response.put("msg", "Some error occurred");
		return response;

	}

	@Override
	public Map<String, Object> saveUPSSAction(Map<String, Object> requestData) {

		Map<String, Object> response = new HashMap<>();
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Transaction tx = session.beginTransaction();
		try {

			Users user = new Users();
			if (requestData.get("userId") != null && !requestData.get("userId").toString().equals("")) {

				Long userId = Long.parseLong(requestData.get("userId").toString());

				//user.setUserId(userId);
				user = (Users) session.load(Users.class, userId);
			}

			if (requestData.get("empRegId") != null && !requestData.get("empRegId").toString().equals("")) {

				Long empRegId = Long.parseLong(requestData.get("empRegId").toString());
				EmployeeRegistration employee = (EmployeeRegistration) session.load(EmployeeRegistration.class,
						empRegId);

				if (user != null) {
					employee.setUpssId(user);
				}

				String upssAction = String.valueOf(requestData.get("upssAction"));
				String upssRemarks = String.valueOf(requestData.get("upssRemarks"));

				employee.setUpssFlag(upssAction);
				employee.setUpssRemarks(upssRemarks);

				employee.setUpssDate(HMSUtil.getCurrentTimeStamp());
				session.saveOrUpdate(employee);

				Users userRecord = null;
				List<Users> list = null;

				Criteria cr = session.createCriteria(Users.class).createAlias("employeeId", "emp")
						.add(Restrictions.eq("emp.employeeId", empRegId));

				list = cr.list();
				if (list.size() > 0) {
					userRecord = list.get(0);
				}

				Timestamp lastChgDate = new Timestamp(new Date().getTime());
				if (userRecord != null) {
					if (upssAction.equalsIgnoreCase("R")) {
						userRecord.setUserFlag(new Long(2));
						userRecord.setLastChgDate(lastChgDate);
						session.saveOrUpdate(userRecord);

					}

				}

				String validateStatus = "";
				if (upssAction.equalsIgnoreCase("A")) {
					validateStatus = "1";
				} else {
					validateStatus = "0";
				}
				HttpHeaders headers = new HttpHeaders();
				headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
				//String entitUrl = HMSUtil.getProperties("adt.properties", "entitUrl").trim();
				
				JSONObject jsonEmpInactive = new JSONObject();
				jsonEmpInactive.put("mobile_number", employee.getMobileNo());
				jsonEmpInactive.put("validate_by", user.getMobileNo());
				jsonEmpInactive.put("validate_status", validateStatus);
				jsonEmpInactive.put("emp_flag", "3");
				
				HttpEntity request = new HttpEntity(jsonEmpInactive.toString(), headers);
				ResponseEntity<String> apiResponse = new RestTemplate().exchange(entitUrl, HttpMethod.POST, request,
						String.class);
				
				 String entitResponse = apiResponse.getBody();
				 System.out.println("entitResponse ---> "+entitResponse);
				response.put("status", true);
				response.put("msg", "Employee status is updated");
				tx.commit();
				return response;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		response.put("status", false);
		response.put("msg", "Some error occurred");
		return response;

	}

	private static String generatePassword(int length) { // ASCII range – alphanumeric (0-9, a-z, A-Z)
		final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

		SecureRandom random = new SecureRandom();
		StringBuilder sb = new StringBuilder();

		// each iteration of the loop randomly chooses a character from the given
		// ASCII range and appends it to the `StringBuilder` instance

		for (int i = 0; i < length; i++) {
			int randomIndex = random.nextInt(chars.length());
			sb.append(chars.charAt(randomIndex));
		}
		System.out.println("Password : " + sb.toString());

		return sb.toString();

	}

	public String sendSMS(String mobile, String messageVal) {

		try {
			String getMobile = mobile;
			String message = messageVal;
			final String uri = "https://sms.weblinto.com/smsapi/index?key=2614C35C9E9BDF&campaign=689&routeid=6&type=text&contacts="
					+ getMobile + "&senderid=VESIPL&msg=Dear " + message + " -VESIPL";

			MultiValueMap<String, String> requestHeaders = new LinkedMultiValueMap<String, String>();
			RestTemplate restTemplate = new RestTemplate();
			String responseObject = restTemplate.postForObject(uri, requestHeaders, String.class);

			System.out.println(responseObject.toString());
			System.out.println("SMS send succefully");
			return responseObject;
		} catch (Exception e) {

			return ProjectUtils.getReturnMsg("0", "We are unable to process your request").toString();
		}
	}
	
	public String sendSMSNew(String mobile, String name,String password) {

		try {
			
			final String uri ="https://2factor.in/API/R1/?module=TRANS_SMS&apikey=5cdc6365-22b5-11ec-a13b-0200cd936042&to="+mobile+
					"&from=CGMMSY&templatename=Username-New&var1="+name+"&var2="+mobile+"&var3="+password;

			MultiValueMap<String, String> requestHeaders = new LinkedMultiValueMap<String, String>();
			RestTemplate restTemplate = new RestTemplate();
			String responseObject = restTemplate.postForObject(uri, requestHeaders, String.class);

			System.out.println(responseObject.toString());
			System.out.println("SMS send succefully");
			return responseObject;
		} catch (Exception e) {

			return ProjectUtils.getReturnMsg("0", "We are unable to process your request").toString();
		}
	}

	@Override
	public Map<String, Object> getEmployeeQualoificationDetails(HashMap<String, String> jsondata) {

		Map<String, Object> map = new HashMap<String, Object>();

		try {

			Criteria cr = null;

			long empRecId = Long.parseLong((String) jsondata.get("empRecId"));

			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();

			cr = session.createCriteria(EmployeeQualification.class).createAlias("employeeId", "emp")
					.add(Restrictions.eq("emp.employeeId", empRecId)).addOrder(Order.asc("employeeQualificationId"));

			List<EmployeeQualification> qualList = cr.list();

			map.put("qualList", qualList);
			getHibernateUtils.getHibernateUtlis().CloseConnection();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return map;

	}

	@Override
	public Map<String, Object> getEmployeeDocumentDetails(HashMap<String, String> jsondata) {

		Map<String, Object> map = new HashMap<String, Object>();

		try {

			Criteria cr = null;

			long empRecId = Long.parseLong((String) jsondata.get("empRecId"));

			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();

			cr = session.createCriteria(EmployeeDocument.class).createAlias("employeeId", "emp")
					.add(Restrictions.eq("emp.employeeId", empRecId)).addOrder(Order.asc("employeeDocumentId"));

			List<EmployeeDocument> docList = cr.list();

			map.put("docList", docList);
			getHibernateUtils.getHibernateUtlis().CloseConnection();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return map;

	}


	@Override
	public Map<String, Object> checkDuplicateMobile(Map<String, Object> requestData) {
		Map<String, Object> map = new HashMap<String, Object>();

		try {

			Criteria cr = null;
			Criteria cr1 = null;
			Criteria cr2 = null;
			boolean ifMobileExists = false;

			String mobileNo = String.valueOf(requestData.get("empMobile"));

			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();

			cr = session.createCriteria(Users.class)
					.add(Restrictions.eq("mobileNo", mobileNo))
					.add(Restrictions.or(Restrictions.eq("userFlag", new Long(0)), Restrictions.eq("userFlag", new Long(1))));
			
			List<Users> userList = cr.list();	
			
			
			
			
			cr1 = session.createCriteria(EmployeeRegistration.class)
					.add(Restrictions.eq("mobileNo", mobileNo));

			List<EmployeeRegistration> empList = cr1.list();
			
			cr2 = session.createCriteria(Users.class)
					.add(Restrictions.isNotNull("employeeIdValue"))	
					
					.add(Restrictions.eq("mobileNo", mobileNo));
					
			
			List<Users> userList1 = cr2.list();	
			
			if(userList.size() > 0 || empList.size() > 0 || userList1.size() > 0) {
				ifMobileExists = true;
			}

			map.put("ifMobileExists", ifMobileExists);
			getHibernateUtils.getHibernateUtlis().CloseConnection();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return map;
	}


	@Override
	public List<MasIdentificationType> getIdTypeList(Map<String, Object> requestData) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<MasIdentificationType> list = new ArrayList<>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasIdentificationType.class).add(Restrictions.eq("status", "Y").ignoreCase());

		
			list = criteria.addOrder(Order.asc("identificationTypeId")).list();
			return list;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return list;
	}

	private String[] splitBase64String(String toSplit, String delimiter) {
	     
	      int offset = toSplit.indexOf(delimiter);
	      if (offset < 0) {
	          return null;
	      }
	      String beforeDelimiter = toSplit.substring(0, offset);
	      String afterDelimiter = toSplit.substring(offset
	              + delimiter.length());
	      return new String[] { beforeDelimiter, afterDelimiter };
	  }


	@Override
	public Map<String, List<MasUserType>> getAllUserTypeForEmpReg(JSONObject jsondata) {

		Map<String, List<MasUserType>> map = new HashMap<String, List<MasUserType>>();
		List<MasUserType> userTypeList = new ArrayList<MasUserType>();
		List totalMatches  =new ArrayList<>();
		String mmuStaff = jsondata.getString("mmuStaff");
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasUserType.class);	
			if(!mmuStaff.equals("") && mmuStaff.equalsIgnoreCase("y")) {
				criteria.add(Restrictions.eq("mmuStaff", mmuStaff).ignoreCase());
			}
						
				 criteria.addOrder(Order.asc("userTypeName"));
				 totalMatches = criteria.list();				 
				 userTypeList = criteria.list();
			
		map.put("userTypeList", userTypeList);
		map.put("totalMatches", totalMatches);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	
	}


	@Override
	public Map<String, Object> getPendingAttendanceList(HashMap<String, Object> jsonData) {


		List<EmployeeRegistration> list = null;
		Map<String, Object> map = new HashMap<String, Object>();

		try {

			Criteria cr = null;
			//int pageNo = Integer.parseInt(jsonData.get("pageNo") + "");

			String mobileNo = (String) jsonData.get("mobileNo");
			String mmu =  String.valueOf(jsonData.get("mmu"));
			String fromDate = (String) jsonData.get("fromDate");
			String toDate = (String) jsonData.get("toDate");
			String statusSearch = (String) jsonData.get("statusSearch");

			//int pagingSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
			int count = 0;

			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();

			Date date = new Date();
			String tDate= new SimpleDateFormat("yyyy-MM-dd").format(date);
			cr =  session.createCriteria(AuditAttendanceData.class)            		
					.add(Restrictions.isNotNull("attendancePhoto"))
					.add(Restrictions.isNull("status"))
					.createAlias("userId", "user");
					
					if (mobileNo != null && !mobileNo.equals("") && !mobileNo.equals("null")) {
						String mNumber = "%" + mobileNo + "%";
						cr.add(Restrictions.like("user.mobileNo", mNumber));
					}
					if (mmu != null &&  !mmu.equals("")) {
						Long mmuId = Long.parseLong(mmu);
						cr.createAlias("mmuId", "mmu");
	            		cr.add(Restrictions.eq("mmu.mmuId", mmuId));
					}
					if (fromDate != null && !fromDate.equals("") && !fromDate.equals("null") &&
							toDate != null && !toDate.equals("") && !toDate.equals("null")) {
						cr.add(Restrictions.ge("attendanceDate", formatDate(fromDate)));
						cr.add(Restrictions.le("attendanceDate", formatDate(toDate)));
					}  else {
						cr.add(Restrictions.eq("attendanceDate", formatDateYYYYMMDD(tDate)));
					}
			
					
            		cr.addOrder(Order.asc("user.userId"));
            		cr.setMaxResults(300);
			

		
			list = cr.list();
			count = list.size();
			map.put("count", count);
			//cr = cr.setFirstResult(pagingSize * (pageNo - 1));
			//cr = cr.setMaxResults(pagingSize);
			list = cr.list();

			map.put("attendanceList", list);
			map.put("statusSearch", statusSearch);
			getHibernateUtils.getHibernateUtlis().CloseConnection();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return map;

	
	}


	@Override
	public Map<String, Object> saveAttendance(Map<String, Object> requestData) {

		Map<String, Object> response = new HashMap<>();
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Transaction tx = session.beginTransaction();
		Criteria cr = null;
		List<Attendance> list = new ArrayList<>();
		try {

			
			Long attendanceId = Long.parseLong(String.valueOf(requestData.get("attendanceId")));
			Long employeeId = Long.parseLong(String.valueOf(requestData.get("employeeId")));			
			String mobileNo = String.valueOf(requestData.get("mobileNo"));
			Double latitude = Double.parseDouble(String.valueOf(requestData.get("latitude")));
			Double longitude = Double.parseDouble(String.valueOf(requestData.get("longitude")));
			String attendanceDate = String.valueOf(requestData.get("attendanceDate"));
			String base64ForAttnImage= String.valueOf(requestData.get("attendanceImage"));	
			String [] attenDate = attendanceDate.split(" ");
			//System.out.println("---attenDate-->"+attenDate[0]);
			String attenTimes = attenDate[1].replaceAll(":", "");
			
			String [] splitYear = attenDate[0].split("-");
			int attendanceYear = Integer.parseInt(splitYear[0]);
			int attendanceMonth = Integer.parseInt(splitYear[1]);
			

			Date atnDate = formatDateYYYYMMDD(attenDate[0]);
			//System.out.println("---atnDate-->"+atnDate);
			cr = session.createCriteria(Users.class);
			cr.add(Restrictions.eq("mobileNo", mobileNo))
			  .add(Restrictions.or(Restrictions.eq("userFlag", new Long(0)), Restrictions.eq("userFlag", new Long(1))));

			Long mmuId = null;
			Long userId = null;
			Users user = null;
			if(cr.list().size() > 0 ) {
			 user = (Users) cr.list().get(0);
			
			 String mmuIdString = user.getMmuId();
			 String [] mmuIdSplit = mmuIdString.split(",");
			 mmuId = Long.parseLong(mmuIdSplit[0]);
			
			userId = user.getUserId();
			
			BufferedImage image = null;
			byte[] imageByte;

			BASE64Decoder decoder = new BASE64Decoder();
			imageByte = decoder.decodeBuffer(base64ForAttnImage);
			ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
			image = ImageIO.read(bis);
			bis.close();

			//mmu.service.files.basePath=C:/Program Files/Apache Software Foundation/Tomcat 8.5/webapps/MMU/uploaded_files/AttendanceImages
			String rootPath = environment.getProperty("mmu.service.attendancefiles.basePath");
			//String rootPath = System.getProperty("catalina.home");
			 String imageFolderPathDirctory = rootPath +File.separator+mobileNo+File.separator+attenDate[0]+File.separator;
			 String completePath = imageFolderPathDirctory+attendanceId+"_"+attenDate[0]+" "+attenTimes+".jpg";
			 String fileName = ""+attendanceId+"_"+attenDate[0]+" "+attenTimes+".jpg";
			 File imageDir = new File(imageFolderPathDirctory);
			 if (!imageDir.exists())
			 { 
				 imageDir.mkdirs();
			}
			// write the image to a file
			File outputfile = new File(completePath);
			ImageIO.write(image, "jpg", outputfile);
			
			
			Query insertSql = session.createSQLQuery("insert into attendance(attendance_id,employee_id,mobile_no,latitude,longitude,attendance_date,file_name) " + 
					"values("+attendanceId+","+employeeId+",'"+mobileNo+"','"+latitude+"','"+longitude+"','"+attendanceDate+"','"+fileName+"'); ");
			insertSql.executeUpdate();
			
			AuditAttendanceData audData = null;
			
			Criteria cr1 = session.createCriteria(AuditAttendanceData.class)
					              .createAlias("userId", "user") 
					              .add(Restrictions.eq("user.userId", userId))
					              .add(Restrictions.eq("attendanceDate",atnDate));
			if(cr1.list().size() > 0)
				audData =  (AuditAttendanceData) cr1.list().get(0);
			if(audData != null) {
					audData.setLastOutPhoto(fileName);
					audData.setLastLat(latitude);
					audData.setLastLong(longitude);
					audData.setOutTime(attenDate[1]);
					audData.setLastAttId(attendanceId);
			} else {
					audData = new AuditAttendanceData();
					audData.setUserId(user);
					MasMMU mmu = new MasMMU();
					mmu.setMmuId(mmuId);
					audData.setMmuId(mmu);
					audData.setFirstInPhoto(fileName);
					audData.setFirstLat(latitude);
					audData.setFirstLong(longitude);
					audData.setInTime(attenDate[1]);
					audData.setFirstAttId(attendanceId);
					audData.setAttendanceDate(formatDateYYYYMMDD(attenDate[0]));
					audData.setAttendanceMonth(attendanceMonth);
					audData.setAttendanceYear(attendanceYear);
			}
			session.saveOrUpdate(audData);
			
			response.put("status", true);
			response.put("msg", "Success");		
			
			} else {
				response.put("status", false);
				response.put("msg", "User does not exist");	
			}
			
			
			tx.commit();
			return response;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		response.put("status", false);
		response.put("msg", "Failed");
		return response;
	
	}


	@Override
	public Map<String, Object> pendingListPhoto(HashMap<String, Object> jsonData) {

		List<AuditAttendanceData> attendanceList = null;
		//List<Object[]> attendanceList = null;
		Map<String, Object> map = new HashMap<String, Object>();

		try {

			Criteria cr = null;
			//int pageNo = Integer.parseInt(jsonData.get("pageNo") + "");

			String mobileNo = (String) jsonData.get("mobileNo");
			String mmu =  String.valueOf(jsonData.get("mmu"));
			String fromDate = (String) jsonData.get("fromDate");
			String toDate = (String) jsonData.get("toDate");

			//int pagingSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
			int count = 0;
			
			Date date = new Date();
			String tDate= new SimpleDateFormat("yyyy-MM-dd").format(date);
			Date todayDate = formatDateYYYYMMDD(tDate);
			//System.out.println("today Date--> "+tDate);
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
					

            cr =  session.createCriteria(AuditAttendanceData.class)
            		.add(Restrictions.isNull("attendancePhoto"))
            		.createAlias("userId", "user")
            		.createAlias("user.masUserType", "userType")
            		.add(Restrictions.isNotNull("userType.mmuStaff"))
            		.addOrder(Order.asc("user.userId"));
		
            if (mobileNo != null && !mobileNo.equals("") && !mobileNo.equals("null")) {
				String mNumber = "%" + mobileNo + "%";
				cr.add(Restrictions.like("user.mobileNo", mNumber));
			}
			if (mmu != null &&  !mmu.equals("")) {
				Long mmuId = Long.parseLong(mmu);
				cr.createAlias("mmuId", "mmu");
        		cr.add(Restrictions.eq("mmu.mmuId", mmuId));
			}
			if (fromDate != null && !fromDate.equals("") && !fromDate.equals("null") &&
					toDate != null && !toDate.equals("") && !toDate.equals("null")) {
				cr.add(Restrictions.ge("attendanceDate", formatDate(fromDate)));
				cr.add(Restrictions.le("attendanceDate", formatDate(toDate)));
			}  else {
				cr.add(Restrictions.eq("attendanceDate", formatDateYYYYMMDD(tDate)));
			}
	
			
    		cr.addOrder(Order.asc("user.userId"));
    		cr.setMaxResults(300);
            
			attendanceList =  cr.list();
			//System.out.println("attendanceList size--> "+attendanceList.size());
			count = attendanceList.size();
			map.put("count", count);
			//cr = cr.setFirstResult(pagingSize * (pageNo - 1));
			//cr = cr.setMaxResults(pagingSize);

			map.put("attendanceList", attendanceList);
			
			for(AuditAttendanceData temp: attendanceList)
			{
				System.out.println(temp.getUserId().getEmployeeId().getMasAdministrativeSex().getAdministrativeSexName());
			}
			getHibernateUtils.getHibernateUtlis().CloseConnection();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return map;
	}


	@Override
	public Map<String, Object> savePhotoValidation(Map<String, Object> requestData) {

		Map<String, Object> response = new HashMap<>();
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Transaction tx = session.beginTransaction();
		try {

			
			if (requestData.get("photoValidationData") != null) {
				List<HashMap<String, Object>> photoValidationList = (List<HashMap<String, Object>>) (Object) requestData.get("photoValidationData");
				Long auditAttnId = null;
				for (HashMap<String, Object> photo : photoValidationList) {
					if (photo.get("auditAttnId") != null && !photo.get("auditAttnId").equals("")) {
						 auditAttnId= Long.parseLong(String.valueOf(photo.get("auditAttnId")));
						
					} 					
					AuditAttendanceData auditAttendance = (AuditAttendanceData) session.load(AuditAttendanceData.class, auditAttnId);
					
					
					if (photo.get("photoMatched") != null && !photo.get("photoMatched").equals("")) {
						String photoMatched= String.valueOf(photo.get("photoMatched"));
						auditAttendance.setAttendancePhoto(photoMatched);
						
					} 
					Users user = new Users();
					if (photo.get("userId") != null && !photo.get("userId").toString().equals("")) {

						Long userId = Long.parseLong(photo.get("userId").toString());
						//System.out.println("userId--> "+userId);

						user.setUserId(userId);
						auditAttendance.setLastChangeBy(user);
					}

					auditAttendance.setLastChgDate(HMSUtil.getCurrentTimeStamp());
					session.saveOrUpdate(auditAttendance);
					
				}
			}

			
			response.put("status", true);
			response.put("msg", "Photo Validation saved successfully");
			
			tx.commit();
			return response;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		response.put("status", false);
		response.put("msg", "Something went wrong");
		return response;
	
	}


	@Override
	public Map<String, Object> getAttendanceInfo(Long empId, String attenDate) {

		List<AuditAttendance> attendanceList = null;
		Map<String, Object> map = new HashMap<String, Object>();

		try {

			Criteria cr = null;
			Date date = new Date();
			String tDate= new SimpleDateFormat("yyyy-MM-dd").format(date);
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			String [] DateString = attenDate.split("-");
			LocalDate atnDate = LocalDate.of(Integer.parseInt(DateString[0]), Integer.parseInt(DateString[1]), Integer.parseInt(DateString[2]));
			Date aDate = Date.from(atnDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
				//System.out.println("----date---> "+aDate);
			 cr =  session.createCriteria(AuditAttendance.class)
					      .createAlias("employeeId", "emp")
					      .add(Restrictions.eq("emp.employeeId", empId))
	            		  .add(Restrictions.eq("attendanceDate", aDate));
			 
				attendanceList =  cr.list();
				//System.out.println("attendanceList size--> "+attendanceList.size());
		

			map.put("attendanceList", attendanceList);
			getHibernateUtils.getHibernateUtlis().CloseConnection();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return map;
	}


	@Override
	public Map<String, Object> getCampInfo(Long mmuId, String attenDate) {


		Map<String, Object> map = new HashMap<String, Object>();

		try {

			Criteria cr = null;
			Date date = new Date();
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			String [] DateString = attenDate.split("-");
			LocalDate atnDate = LocalDate.of(Integer.parseInt(DateString[0]), Integer.parseInt(DateString[1]), Integer.parseInt(DateString[2]));
			Date aDate = Date.from(atnDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
			 cr =  session.createCriteria(MasCamp.class)
					      .createAlias("masMMU", "mmu")
					      .add(Restrictions.eq("mmu.mmuId", mmuId))
	            		  .add(Restrictions.eq("campDate", aDate))
	            		  .add(Restrictions.eq("weeklyOff", "Camp"));
			 
			 List<MasCamp> campList =  cr.list();
		

			map.put("campList", campList);
			getHibernateUtils.getHibernateUtlis().CloseConnection();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return map;
	}


	@Override
	public Map<String, Object> saveAttendanceAudit(Map<String, Object> requestData) {


		Map<String, Object> response = new HashMap<>();
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Transaction tx = session.beginTransaction();
		try {

			
			if (requestData.get("auditAttendanceData") != null) {
				List<HashMap<String, Object>> auditAttendanceDataList = (List<HashMap<String, Object>>) (Object) requestData.get("auditAttendanceData");
				Long auditAttnId = null;
				for (HashMap<String, Object> attendance : auditAttendanceDataList) {
					if (attendance.get("attnId") != null && !attendance.get("attnId").equals("")) {
						 auditAttnId= Long.parseLong(String.valueOf(attendance.get("attnId")));
						
					} 					
					AuditAttendanceData auditAttendance = (AuditAttendanceData) session.load(AuditAttendanceData.class, auditAttnId);
					
					
					if (attendance.get("distance") != null && !attendance.get("distance").equals("")) {
						String locationMatched= String.valueOf(attendance.get("distance"));
						if(locationMatched.equals("Matched")) {
							locationMatched = "Y";
						} else {
							locationMatched = "N";
						}
						auditAttendance.setAttendanceLocation(locationMatched);
						
					} 
					
					if (attendance.get("time") != null && !attendance.get("time").equals("")) {
						String timeMatched= String.valueOf(attendance.get("time"));
						if(timeMatched.equals("Matched")) {
							timeMatched = "Y";
						} else {
							timeMatched = "N";
						}
						auditAttendance.setAttendanceTime(timeMatched);
						
					} 
					
					if (attendance.get("status") != null && !attendance.get("status").equals("")) {
						String status= String.valueOf(attendance.get("status"));
						auditAttendance.setStatus(status);
						
					} 
					
					if (attendance.get("remarks") != null && !attendance.get("remarks").equals("")) {
						String remarks= String.valueOf(attendance.get("remarks"));
						auditAttendance.setRemarks(remarks);
						
					} 
					
					Users user = new Users();
					if (attendance.get("userId") != null && !attendance.get("userId").toString().equals("")) {

						Long userId = Long.parseLong(attendance.get("userId").toString());

						user.setUserId(userId);
						auditAttendance.setLastChangeBy(user);
					}

					auditAttendance.setLastChgDate(HMSUtil.getCurrentTimeStamp());
					session.saveOrUpdate(auditAttendance);
					
					
				}
			}

			
			response.put("status", true);
			response.put("msg", "Attendance audit data saved successfully");
			
			tx.commit();
			return response;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		response.put("status", false);
		response.put("msg", "Something went wrong");
		return response;
	}


	@Override
	public List<Integer> getAttendanceYears(Map<String, Object> requestData) {

		List<Integer> yearsList = new ArrayList<>();
		try {
			//Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			//SQLQuery  query  = session.createSQLQuery("select distinct attendance_year from audit_attendance_data" ); 
			
			 //yearsList = query.list();
			
			 	int cur = 2021;
			    int stop = 2031;
			    while (cur <= stop) {
			    	yearsList.add(cur++);
			    }
		
			return yearsList;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			//getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return yearsList;
	
	}


	@Override
	public List<Integer> getAttendanceMonths(Map<String, Object> jsonData) {

		List<Integer> monthList = new ArrayList<>();
		//String year = (String) jsonData.get("year");
		//System.out.println("year---jk---> "+year);
		try {
			//Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			//SQLQuery  query = null;
			/*if(year != null && !year.equals("")) {
				int selectedYear = Integer.parseInt(year);
			    query  = session.createSQLQuery("select distinct attendance_month from audit_attendance_data where attendance_year="+selectedYear ); 
			} else {*/
				//query  = session.createSQLQuery("select distinct attendance_month from audit_attendance_data" ); 
			//}
			
			//monthList = query.list();
				monthList.add(1);
				monthList.add(2);
				monthList.add(3);
				monthList.add(4);
				monthList.add(5);
				monthList.add(6);
				monthList.add(7);
				monthList.add(8);
				monthList.add(9);
				monthList.add(10);
				monthList.add(11);
				monthList.add(12);
				
		
			return monthList;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			//getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return monthList;
	
	}


	@Override
	public Map<String, Object> getPenaltyList(HashMap<String, Object> jsonData) {


		List<AuditAttendanceData> penaltyList = null;
		Map<String, Object> map = new HashMap<String, Object>();

		try {

			Criteria cr = null;
			int pageNo = Integer.parseInt(jsonData.get("pageNo") + "");

			Integer attnMonth = 0;
			Integer attnYear = 0;
			String mmu =  String.valueOf(jsonData.get("mmu"));
			String mmuId =  String.valueOf(jsonData.get("mmuId"));
			if(jsonData.get("attnMonth") != null && !jsonData.get("attnMonth").equals("")) {
			 attnMonth = Integer.parseInt(String.valueOf(jsonData.get("attnMonth")));
			}
			if(jsonData.get("attnYear") != null && !jsonData.get("attnYear").equals("")) {
			 attnYear = Integer.parseInt(String.valueOf(jsonData.get("attnYear")));
			}

			int pagingSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
			int count = 0;
			
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
					

            cr =  session.createCriteria(AuditAttendanceData.class)
            		.add(Restrictions.eq("status", "A").ignoreCase());
		
           
			if (mmu != null &&  !mmu.equals("") &&  !mmu.equals("null")) {
				Long mmuId_l = Long.parseLong(mmu);
				cr.createAlias("mmuId", "mmu");
        		cr.add(Restrictions.eq("mmu.mmuId", mmuId_l));
			}
			
			if (mmuId != null &&  !mmuId.equals("") &&  !mmuId.equals("null")) {
				Long mmuId_l1 = Long.parseLong(mmuId);
				cr.createAlias("mmuId", "mmu");
        		cr.add(Restrictions.eq("mmu.mmuId", mmuId_l1));
			}
			if (attnMonth > 0) {
				cr.add(Restrictions.eq("attendanceMonth", attnMonth));
			}  
			if (attnYear > 0) {
				cr.add(Restrictions.eq("attendanceYear", attnYear));
			} 
	
			
            
    		penaltyList =  cr.list();
			//System.out.println("penaltyList size--> "+penaltyList.size());
			count = penaltyList.size();
			map.put("count", count);
			cr = cr.setFirstResult(pagingSize * (pageNo - 1));
			cr = cr.setMaxResults(pagingSize);

			map.put("penaltyList", penaltyList);
			getHibernateUtils.getHibernateUtlis().CloseConnection();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return map;
	
	}


	@Override
	public Map<String, Object> auditAttendanceHistory(Map<String, Object> jsonData) {



		List<EmployeeRegistration> list = null;
		Map<String, Object> map = new HashMap<String, Object>();

		try {

			Criteria cr = null;
			int pageNo = Integer.parseInt(jsonData.get("pageNo") + "");

			String mobileNo = (String) jsonData.get("mobileNo");
			String mmu =  String.valueOf(jsonData.get("mmu"));
			String fromDate = (String) jsonData.get("fromDate");
			String toDate = (String) jsonData.get("toDate");
			String statusSearch = (String) jsonData.get("statusSearch");

			int pagingSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
			int count = 0;

			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();

			cr =  session.createCriteria(AuditAttendanceData.class)            		
					.add(Restrictions.isNotNull("attendancePhoto"))
					.add(Restrictions.isNotNull("status"))
					.createAlias("userId", "user");
					
					if (mobileNo != null && !mobileNo.equals("") && !mobileNo.equals("null")) {
						String mNumber = "%" + mobileNo + "%";
						cr.add(Restrictions.like("user.mobileNo", mNumber));
					}
					if (mmu != null &&  !mmu.equals("")) {
						Long mmuId = Long.parseLong(mmu);
						cr.createAlias("mmuId", "mmu");
	            		cr.add(Restrictions.eq("mmu.mmuId", mmuId));
					}
					if (fromDate != null && !fromDate.equals("") && !fromDate.equals("null") &&
							toDate != null && !toDate.equals("") && !toDate.equals("null")) {
						cr.add(Restrictions.ge("attendanceDate", formatDate(fromDate)));
						cr.add(Restrictions.le("attendanceDate", formatDate(toDate)));
					}  
			
					
            		cr.addOrder(Order.asc("user.userId"));
            		cr.setMaxResults(300);
			

		
			list = cr.list();
			count = list.size();
			map.put("count", count);
			cr = cr.setFirstResult(pagingSize * (pageNo - 1));
			cr = cr.setMaxResults(pagingSize);
			list = cr.list();

			map.put("attendanceList", list);
			map.put("statusSearch", statusSearch);
			getHibernateUtils.getHibernateUtlis().CloseConnection();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return map;

	
	
	}


	@Override
	public Map<String, Object> checkDuplicateIMEI(Map<String, Object> requestData) {

		Map<String, Object> map = new HashMap<String, Object>();

		try {

			Criteria cr = null;
			boolean ifimeiNoExists = false;

			String imeiNo = String.valueOf(requestData.get("imeiNo"));

			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();

			cr = session.createCriteria(EmployeeRegistration.class)
					.add(Restrictions.eq("imeiNumber", imeiNo))
					.add(Restrictions.ne("recordStatus", "saved"));

			List<EmployeeRegistration> empList = cr.list();
			if(empList.size() > 0) {
				ifimeiNoExists = true;
			}

			map.put("ifimeiNoExists", ifimeiNoExists);
			getHibernateUtils.getHibernateUtlis().CloseConnection();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return map;
	
	}


	@Override
	public Map<String, Object> getEmpListForEdit(HashMap<String, Object> jsonData) {


		List<EmployeeRegistration> list = null;
		Map<String, Object> map = new HashMap<String, Object>();

		try {

			Criteria cr = null;
			int pageNo = Integer.parseInt(jsonData.get("pageNo") + "");

			String mobileNo = (String) jsonData.get("mobileNo");
			String calledFor = (String) jsonData.get("calledFor");
			Long userId = Long.parseLong(jsonData.get("userId") + "");
			

			int pagingSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
			int count = 0;

			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			
			Criteria c =  session.createCriteria(Users.class)
					            .add(Restrictions.eq("userId", userId));
			
			List<Users> userList = c.list();
			Users user = null;
			if(userList.size() > 0) {
				 user = userList.get(0);
			}
			String levelOfUser = user.getLevelOfUser();
			List<Long> mmuId = new ArrayList<Long>();

			cr = session.createCriteria(EmployeeRegistration.class).addOrder(Order.asc("employeeId"));

			
			session.doWork(new Work() {
				@Override
				public void execute(java.sql.Connection connection) throws SQLException {
				
					String queryString = "SELECT asp_hierarchical_mmu(?,?)";				
					PreparedStatement    stmt = connection.prepareCall(queryString);
					
					stmt.setString(1, levelOfUser);			
					stmt.setInt(2, userId.intValue());
					connection.setAutoCommit(false);
					ResultSet rs = stmt.executeQuery();
					
					if (rs.next()) {
						Object o = rs.getObject(1);
						if (o instanceof ResultSet) {
							ResultSet rs1 = (ResultSet) o;
							while (rs1.next()) {
								int columnCount = rs1.getMetaData().getColumnCount();
								for (int i = 0; i < columnCount; i++) {
									if(rs1.getMetaData().getColumnLabel(i + 1).equals("mmu_id")) {
										mmuId.add(Long.parseLong( rs1.getObject(i + 1).toString()));
									}
								}
							}
						}
					}
					
				
			
				}
			});
				 //System.out.println("MMU id:- "+mmuId.toString());
				 if(mmuId != null && mmuId.size() > 0) { 
					 cr.createAlias("mmuId", "mmuId");
					 cr.add(Restrictions.in("mmuId.mmuId", mmuId));
				  }

			if (mobileNo != null && !mobileNo.equals("") && !mobileNo.equals("null")) {
				String mNumber = "%" + mobileNo + "%";
				cr.add(Restrictions.like("mobileNo", mNumber));
			}
			

			list = cr.list();
			count = list.size();
			map.put("count", count);
			cr = cr.setFirstResult(pagingSize * (pageNo - 1));
			cr = cr.setMaxResults(pagingSize);
			list = cr.list();

			map.put("list", list);
			getHibernateUtils.getHibernateUtlis().CloseConnection();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return map;

	
	}


	@Override
	public Map<String, Object> getEmployeeRecordForUpdate(HashMap<String, String> jsondata) {

		Map<String, Object> map = new HashMap<String, Object>();

		try {

			Criteria cr = null;

			long empRecId = Long.parseLong((String) jsondata.get("empRecId"));

			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();

			cr = session.createCriteria(EmployeeRegistration.class).addOrder(Order.asc("employeeId"));
			cr.add(Restrictions.eq("employeeId", empRecId));

			EmployeeRegistration emp = (EmployeeRegistration) cr.list().get(0);

			map.put("emp", emp);
			getHibernateUtils.getHibernateUtlis().CloseConnection();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return map;
	
	}


	@Override
	public Map<String, Object> updateEmployee(Map<String, Object> requestData) {

		Map<String, Object> response = new HashMap<>();
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Transaction tx = session.beginTransaction();
		Long empRegId = 0l;
		try {

			Users user = new Users();
			if (requestData.get("userId") != null && !requestData.get("userId").toString().equals("")) {

				Long userId = Long.parseLong(requestData.get("userId").toString());

				//user.setUserId(userId);
				user = (Users) session.load(Users.class, userId);
			}

			if (requestData.get("empRegId") != null && !requestData.get("empRegId").toString().equals("")) {

				 empRegId = Long.parseLong(requestData.get("empRegId").toString());
			}
			EmployeeRegistration employee = (EmployeeRegistration) session.load(EmployeeRegistration.class, empRegId);			


			String empName = String.valueOf(requestData.get("empName"));
			Long genderId = Long.parseLong(String.valueOf(requestData.get("genderId")));
			Date dob = formatDate(String.valueOf(requestData.get("dob")));
			String empAddress = String.valueOf(requestData.get("empAddress"));
			String empState = String.valueOf(requestData.get("empState"));
			String empDist = String.valueOf(requestData.get("empDist"));
			String empCity = String.valueOf(requestData.get("empCity"));
			Long pinCode = Long.parseLong(String.valueOf(requestData.get("empPincode")));
			String mobileNo = String.valueOf(requestData.get("empMobile"));
			String empIMEI = String.valueOf(requestData.get("empIMEI"));
			Long mmuId = Long.parseLong(String.valueOf(requestData.get("mmuId")));
			Long empTypeId = Long.parseLong(String.valueOf(requestData.get("empTypeId")));
			String employmentType = String.valueOf(requestData.get("employmentTypeId"));
			String recordAction = String.valueOf(requestData.get("action"));
			Long empIdType = Long.parseLong(String.valueOf(requestData.get("empIdType")));
			String empIdNumber = String.valueOf(requestData.get("empIdNumber"));
			String mimeTypeId = "";
			if(requestData.get("idMimeType") !=null) {
				mimeTypeId = String.valueOf(requestData.get("idMimeType"));
			}
			if(requestData.get("base64ForProfileStr") != null && !requestData.get("base64ForProfileStr").equals("")) {
				String base64ForProfileTmp= String.valueOf(requestData.get("base64ForProfileStr"));			
				String base64ForProfileStr = base64ForProfileTmp.substring(base64ForProfileTmp.indexOf(",")+1, base64ForProfileTmp.length());
				byte[] decodedProfileImage = Base64.decodeBase64(base64ForProfileStr);
				
				System.out.println("base64ForProfileTmp:"+base64ForProfileTmp);
				System.out.println("base64ForProfileStr:"+base64ForProfileStr);
				System.out.println("decodedProfileImage:"+decodedProfileImage);
				employee.setProfileImage(decodedProfileImage);
			}
			if(requestData.get("base64ForIdStr") != null && !requestData.get("base64ForIdStr").equals("")) {
				String base64ForIdStr = String.valueOf(requestData.get("base64ForIdStr"));
				byte[] decodedIdImage = Base64.decodeBase64(base64ForIdStr);
				employee.setIdentificationTypeImage(decodedIdImage);
			}
			
			Date fromDate = formatDate(String.valueOf(requestData.get("fromDate")));
			Date toDate = null;
			if (requestData.get("toDate") != null && !requestData.get("toDate").toString().equals("")) {
				toDate = formatDate(String.valueOf(requestData.get("toDate")));
			}

			String empIdentity = String.valueOf(requestData.get("empIdentity"));

			employee.setEmployeeName(empName);
			if(mimeTypeId.length() < 10) {
			employee.setIdMimeType(mimeTypeId);
			}
			
			MasIdentificationType idType = new MasIdentificationType();
			//idType.setIdentificationTypeId(empIdType);
			idType = (MasIdentificationType) session.load(MasIdentificationType.class, empIdType);
			
			employee.setIdentificationTypeId(idType);
			employee.setIdentificationTypeNo(empIdNumber);

			MasAdministrativeSex m = new MasAdministrativeSex();
			m.setAdministrativeSexId(genderId);

			employee.setMasAdministrativeSex(m);
			employee.setAddress(empAddress);
			employee.setState(empState);
			employee.setDistrict(empDist);
			employee.setCity(empCity);
			employee.setPinCode(pinCode);
			employee.setMobileNo(mobileNo);
			employee.setImeiNumber(empIMEI);
			employee.setDob(dob);
			employee.setStartDate(fromDate);
			if (requestData.get("toDate") != null && !requestData.get("toDate").toString().equals("")) {
				employee.setEndDate(toDate);
			}
			employee.setRegNo(empIdentity);

			MasMMU mmu = new MasMMU();
			//mmu.setMmuId(mmuId);
			mmu = (MasMMU) session.load(MasMMU.class, mmuId);
			employee.setMmuId(mmu);

			MasUserType userType = new MasUserType();
			
			userType = (MasUserType) session.load(MasUserType.class, empTypeId);
			//userType.setUserTypeId(empTypeId);
			employee.setUserTypeId(userType);

			employee.setEmploymentType(employmentType);

			employee.setLastChgDate(HMSUtil.getCurrentTimeStamp());
			employee.setRecordStatus(recordAction);
			//////////////////////////////////////////////////////////////////////////////////////////////
			if(employee!=null && StringUtils.isNotEmpty(employee.getApmFlag()) && employee.getApmFlag().equalsIgnoreCase("R")) {
				employee.setApmFlag(null);
			}
			///////////////////////////////////////
			session.saveOrUpdate(employee);

			///////////////////////////////////////////////////////////////////////////////////////////////////
			

			if (requestData.get("qualificationList") != null) {
				EmployeeQualification empQualification = null;

				@SuppressWarnings("unchecked")
				List<HashMap<String, Object>> qualificationList = (List<HashMap<String, Object>>) (Object) requestData
						.get("qualificationList");
				for (HashMap<String, Object> qualification : qualificationList) {

					if (qualification.get("qualId") != null && qualification.get("qualId") != "") {
						long qId = Long.parseLong(qualification.get("qualId").toString());
						if (qId > 0) {
							empQualification = (EmployeeQualification) session.load(EmployeeQualification.class, qId);
							;
						}
					} else {
						empQualification = new EmployeeQualification();
					}
					if(empQualification==null)
					empQualification = new EmployeeQualification();
					if (qualification.get("degree") != null && qualification.get("degree") != "") {
						empQualification.setQualificationName(String.valueOf(qualification.get("degree").toString()));
					}
					if (qualification.get("instName") != null && qualification.get("instName") != "") {
						empQualification.setInstitutionName(String.valueOf(qualification.get("instName").toString()));
					}
					if (qualification.get("completeionYear") != null && qualification.get("completeionYear") != "") {
						empQualification
								.setCompletionYear(Integer.valueOf(qualification.get("completeionYear").toString()));
					}
					if (qualification.get("completeionYear") != null && qualification.get("completeionYear") != "") {
						empQualification
								.setCompletionYear(Integer.valueOf(qualification.get("completeionYear").toString()));
					}
					String mimeTypeEdu = "";
					if (qualification.get("mimeTypeEduDoc") != null && qualification.get("mimeTypeEduDoc") != "") {
						mimeTypeEdu = String.valueOf(qualification.get("mimeTypeEduDoc").toString());
						if(mimeTypeEdu.length() < 15) {
						empQualification.setMimeType(mimeTypeEdu);
						}
					}
					if (qualification.get("base64ForEduDoc") != null && !qualification.get("base64ForEduDoc").equals("")) {
						String base64ForEduDocTmp= String.valueOf(qualification.get("base64ForEduDoc"));	
						//System.out.println("base64ForEduDocTmp string---> "+base64ForEduDocTmp);
						//String [] s = splitBase64String(base64ForEduDocTmp,",");
						//String mimeTypeQual = getMimeType(s);
						byte[] base64ForEduDoc = Base64.decodeBase64(base64ForEduDocTmp);
						if(mimeTypeEdu.length() < 15) {
						empQualification.setDocument(base64ForEduDoc);
						}
					}
					
					empQualification.setEmployeeId(employee);

					if (user != null) {
						empQualification.setLastChgBy(user);
					}

					empQualification.setLastChgDate(HMSUtil.getCurrentTimeStamp());
					if (qualification.get("degree") != null && qualification.get("degree") != ""
							&& qualification.get("instName") != null && qualification.get("instName") != ""
							&& qualification.get("completeionYear") != null
							&& qualification.get("completeionYear") != "") {
						session.saveOrUpdate(empQualification);
					}

				}
			}

			if (requestData.get("documentList") != null) {
				List<HashMap<String, Object>> documentList = (List<HashMap<String, Object>>) (Object) requestData
						.get("documentList");
				EmployeeDocument emplDocument = null;
				for (HashMap<String, Object> doc : documentList) {

					if (doc.get("docId") != null && doc.get("docId") != "") {
						long docId = Long.parseLong(doc.get("docId").toString());
						if (docId > 0) {
							emplDocument = (EmployeeDocument) session.load(EmployeeDocument.class, docId);
							;
						}
					} else {
						emplDocument = new EmployeeDocument();
					}
					if(emplDocument==null)
					emplDocument = new EmployeeDocument();
					if (doc.get("docName") != null && doc.get("docName") != "") {
						emplDocument.setDocumentName(String.valueOf(doc.get("docName").toString()));
					}

					emplDocument.setEmployeeId(employee);

					if (user != null) {
						emplDocument.setLastChgBy(user);
					}

					emplDocument.setLastChgDate(HMSUtil.getCurrentTimeStamp());
					String docMime = "";
					if (doc.get("mimeTypeRequireDoc") != null && !doc.get("mimeTypeRequireDoc").equals("")) {
						docMime = String.valueOf(doc.get("mimeTypeRequireDoc").toString());
						if(docMime.length() < 15) {
						emplDocument.setMimeType(docMime);
						}
					}
					if (doc.get("base64ForRequireDoc") != null && !doc.get("base64ForRequireDoc").equals("")) {
						String base64ForEduDocTmp= String.valueOf(doc.get("base64ForRequireDoc"));	
						//System.out.println("base64ForEduDocTmp string---> "+base64ForEduDocTmp);
						//String [] s = splitBase64String(base64ForEduDocTmp,",");
						//String mimeTypeDoc = getMimeType(s);
						//System.out.println("mimeTypeDoc---> "+mimeTypeDoc);
						byte[] base64ForEduDoc = Base64.decodeBase64(base64ForEduDocTmp);
						emplDocument.setDocument(base64ForEduDoc);
						}
					
					
				//	if (doc.get("docName") != null && !doc.get("docName").equals("") && doc.get("base64ForRequireDoc") != null && !doc.get("base64ForRequireDoc").equals("")
						//	&& docMime.length() < 15) {
					if (doc.get("docName") != null && !doc.get("docName").equals("")) {
						
						session.saveOrUpdate(emplDocument);
					}

				}
			}

			/////////////////////////////////////////////////////////////////////////////////////////////////////
			
			//user update if record exists
			
			Users userRecord = null;
			
			

			Criteria cr = session.createCriteria(Users.class).createAlias("employeeId", "emp")
					.add(Restrictions.eq("emp.employeeId", empRegId))
					.add(Restrictions.or(Restrictions.eq("userFlag", new Long(0)), Restrictions.eq("userFlag", new Long(1))));

			List<Users> list = cr.list();
			if (list.size() > 0) {
				userRecord = list.get(0);
			}

			
			if (userRecord != null) {
				SQLQuery  query  = session.createSQLQuery("select role_id from mas_role_usertype_mapping where user_type_id ="+employee.getUserTypeId().getUserTypeId() ); 
				
				List<String> rolesRows = query .list();
				//System.out.println("rolesRows size--> "+rolesRows.size());
				
				StringBuilder role = new StringBuilder();
				
				for(String row : rolesRows){
					role.append(row).append(",");
				}
				Timestamp lastChgDate = new Timestamp(new Date().getTime());
				userRecord.setLastChgDate(lastChgDate);
				userRecord.setLoginName(employee.getMobileNo());
				userRecord.setUserName(employee.getEmployeeName());
				
				if(rolesRows.size() > 0) {
					userRecord.setRoleId(role.toString());
				}
				userRecord.setMobileNo(employee.getMobileNo());
				userRecord.setEmployeeIdValue(empRegId);
				userRecord.setUserTypeId(employee.getUserTypeId().getUserTypeId());
				userRecord.setMmuId(employee.getMmuId().getMmuId().toString().concat(","));
				session.update(userRecord);
				
				HttpHeaders headers = new HttpHeaders();
				headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
				//String entitUrl = HMSUtil.getProperties("adt.properties", "entitUrl").trim();
				// data to send to Nishtha
				JSONObject jsonEmp = new JSONObject();
				String[] eName = employee.getEmployeeName().split(" ");
				String firstName = "";
				String lastName = "";
				if (eName.length == 1) {
					firstName = eName[0];
					lastName = "";
				} else if (eName.length == 2) {
					firstName = eName[0];
					lastName = eName[1];
				} else if (eName.length == 3) {
					firstName = eName[0] + " " + eName[1];
					lastName = eName[2];
				}

				jsonEmp.put("fname", firstName);
				jsonEmp.put("lname", lastName);
				String profilePhoto = "";
				if (employee.getProfileImage() != null && !employee.getProfileImage().equals("")) {
					byte[] profileImage = employee.getProfileImage();
					 profilePhoto = encodeByteToBase64Binary(profileImage);
				}
				jsonEmp.put("photo", profilePhoto);
				jsonEmp.put("mobile_number", employee.getMobileNo());
				jsonEmp.put("designation", employee.getUserTypeId().getDesignation().toString());
				jsonEmp.put("department", "220");
				jsonEmp.put("ofc_id", employee.getMmuId().getOfficeId().toString());
				jsonEmp.put("idType", employee.getIdentificationTypeId().getMappedId().toString());
				jsonEmp.put("otherIdNumber", employee.getIdentificationTypeNo().toString());
				String otherIdImage = "";
				if (employee.getIdentificationTypeImage() != null && !employee.getIdentificationTypeImage().equals("")) {

					byte[] idImage = employee.getIdentificationTypeImage();
					otherIdImage = encodeByteToBase64Binary(idImage);
				}
				jsonEmp.put("otherIdImage", otherIdImage);
				jsonEmp.put("validate_by", user.getMobileNo());
				jsonEmp.put("emp_flag", "2");
				//System.out.println("json --> "+jsonEmp.toString());
				
				HttpEntity request = new HttpEntity(jsonEmp.toString(), headers);
				ResponseEntity<String> apiResponse = new RestTemplate().exchange(entitUrl, HttpMethod.POST, request,
						String.class);
				
				 String entitResponse = apiResponse.getBody();
				 System.out.println("entitResponse ---> "+entitResponse);
				
				
				}
			// end

				response.put("status", true);
				response.put("msg", "The employee details are updated");
			
			tx.commit();
			return response;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		response.put("status", false);
		response.put("msg", "Something went wrong");
		return response;
	
	}

	
}
