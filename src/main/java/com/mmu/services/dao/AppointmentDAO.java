package com.mmu.services.dao;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import com.mmu.services.entity.AppSetup;
import com.mmu.services.entity.MasAppointmentSession;
import com.mmu.services.entity.MasAppointmentType;
import com.mmu.services.entity.MasDoctorMapping;
import com.mmu.services.entity.MasHospital;



@Repository
public interface AppointmentDAO {

	Map<String, Object> getDataForDoctorAppointment(long hospitalId);
	Map<String, Object> getappointmentSetupDetails(long deptId, long appointmentTypeId, long hospitalId);
	List<MasAppointmentSession> getlocationWiseAppointmentType(long deptId, long hospitalId);
	String saveAppointmentSetup(AppSetup appSetup);
	AppSetup getAppSetupObject(long appointmentId);
	List<MasHospital> getHospitalList();
	List<MasAppointmentType> getAppointmentTypeList();
	String submitAppointmentSession(MasAppointmentSession appointmentSession);
	Map<String, List<MasAppointmentSession>> getAllAppointmentSession(JSONObject jsonObject);
	boolean validateAppSession(long departmentId, long appointmentTypeId, long hospitalId);
	String updateAppointmentSession(long rowId, long departmentId, long appointmentType, String stratTime,
			String endTime, String status);
	List<MasAppointmentSession> getDepartmentListByHospital(long hospitalId);
	List<MasDoctorMapping> getDoctorListFromMapping(long departmentId, long hospitalId);
	List<MasAppointmentSession> getHospitalFromAppSetup();
	
}
