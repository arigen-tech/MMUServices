package com.mmu.services.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mmu.services.dao.OpdPatientDetailDao;
import com.mmu.services.entity.MasAdministrativeSex;
import com.mmu.services.entity.MasEmployee;
import com.mmu.services.entity.MasMaritalStatus;
import com.mmu.services.entity.MasMedicalCategory;
import com.mmu.services.entity.MasReligion;
import com.mmu.services.entity.MasState;
import com.mmu.services.entity.MasTrade;
import com.mmu.services.entity.MasUnit;
import com.mmu.services.entity.OpdPatientDetail;
import com.mmu.services.entity.Patient;
import com.mmu.services.hibernateutils.GetHibernateUtils;

@Repository
@Transactional
public class OpdPatientDetailsImpl implements OpdPatientDetailDao{
	
	@Autowired
	GetHibernateUtils getHibernateUtils;

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public OpdPatientDetail getOpdPatientDetails(Long visitId) {
		OpdPatientDetail opdPatientDetail=null;
		try {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		opdPatientDetail = (OpdPatientDetail) session.createCriteria(OpdPatientDetail.class).add(Restrictions.eq("visitId", visitId)).uniqueResult();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return opdPatientDetail;
	}

	@Override
	public OpdPatientDetail getOpdPatientDetailsByOpdPatientDetailId(Long opdPatientDetailId) {
		OpdPatientDetail opdPatientDetail = null;
	 		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			opdPatientDetail = (OpdPatientDetail) session.createCriteria(OpdPatientDetail.class)
					.add(Restrictions.eq("opdPatientDetailsId", opdPatientDetailId)).uniqueResult();
	 	return opdPatientDetail;
	}

	@Override
	public Patient getPatientByPatientId(Long patientId) {
		Patient patient=null;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			patient = (Patient) session.createCriteria(Patient.class).add(Restrictions.eq("patientId", patientId)).uniqueResult();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
			return patient;
	}

	@Override
	public MasMaritalStatus getMasMaritalStatusByMaritalId(Long maritalId,String mritalStatus) {
		MasMaritalStatus masMaritalStatus=null;
		Criterion criterion=null;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			if(maritalId!=null) {
				criterion=Restrictions.eq("maritalStatusId", maritalId);
			}
			if(StringUtils.isNotEmpty(mritalStatus)) {
				criterion=Restrictions.eq("maritalStatusName", mritalStatus);
			}
			masMaritalStatus = (MasMaritalStatus) session.createCriteria(MasMaritalStatus.class).add(criterion).uniqueResult();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
			return masMaritalStatus;
	}

	@Override
	public MasAdministrativeSex getMasAdministrativeSexByGender(String gender) {
		MasAdministrativeSex masAdministrativeSex=null;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			gender=gender.toUpperCase();
			masAdministrativeSex = (MasAdministrativeSex) session.createCriteria(MasAdministrativeSex.class)
					.add(Restrictions.eq("administrativeSexName", gender)).uniqueResult();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		return masAdministrativeSex;
	}

	@Override
	public MasMedicalCategory getMasMedicalCategoryByCategory(String category) {
		MasMedicalCategory masMedicalCategory=null;
		List<MasMedicalCategory>listMasMedicalCategory=null;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			category=category.toUpperCase();
			listMasMedicalCategory = (List<MasMedicalCategory>) session.createCriteria(MasMedicalCategory.class).add(Restrictions.eq("medicalCategoryName", category)).list();
		if(CollectionUtils.isNotEmpty(listMasMedicalCategory)) {
			masMedicalCategory=listMasMedicalCategory.get(0);
		}	
		}
			catch(Exception e) {
				e.printStackTrace();
			}
			finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		return masMedicalCategory;
	}

	/*@Override
	public OpdPatientHistory getOpdPatientHistoryByVisitId(Long visitId) {
		OpdPatientHistory opdPatientHistory=null;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			opdPatientHistory = (OpdPatientHistory) session.createCriteria(OpdPatientHistory.class)
					.add(Restrictions.eq("visitId", visitId)).uniqueResult();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			finally {
			//getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		return opdPatientHistory;
	}*/

	@Override
	public MasEmployee getMasEmployeeByFirstName(String firstName) {
		MasEmployee masEmployee=null;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			masEmployee = (MasEmployee) session.createCriteria(MasEmployee.class)
					 .add(Restrictions.eq("firstName", firstName)).uniqueResult();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		return masEmployee;
	}

	@Override
	public MasReligion getMasReligionByReligion(String religion) {
		MasReligion masReligion=null;
		try {
			religion=religion.toUpperCase();
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			masReligion = (MasReligion) session.createCriteria(MasReligion.class)
					 .add(Restrictions.eq("religionName", religion)).uniqueResult();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		return masReligion;
	}

	@Override
	public MasUnit getMasUnitByUnitId(String unitName) {
		MasUnit masUnit=null;
		try {
			unitName=unitName.toUpperCase();
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			masUnit = (MasUnit) session.createCriteria(MasUnit.class)
					 .add(Restrictions.eq("unitName", unitName)).uniqueResult();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		return masUnit;
	}

	@Override
	public MasState getMasStateByStateName(String stateName) {
		MasState masState=null;
		try {
			stateName=stateName.toUpperCase();
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			masState = (MasState) session.createCriteria(MasState.class)
					 .add(Restrictions.eq("stateName", stateName)).uniqueResult();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		return masState;
	}

	@Override
	public MasTrade getMasTradeByTradeName(String masTradeName) {
		MasTrade masTrade=null;
		try {
			masTradeName=masTradeName.toUpperCase();
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			masTrade = (MasTrade) session.createCriteria(MasTrade.class)
					 .add(Restrictions.eq("tradeName", masTradeName)).uniqueResult();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		return masTrade;
	}
	
 
}











