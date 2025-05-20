package com.mmu.services.dao.impl;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.OrderBy;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mmu.services.entity.*;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.jdbc.Work;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mmu.services.dao.MasterDao;
import com.mmu.services.hibernateutils.GetHibernateUtils;
import com.mmu.services.utils.HMSUtil;
import com.mmu.services.utils.ProjectUtils;

@Repository
public class MasterDaoImpl implements MasterDao {

	@Autowired
	GetHibernateUtils getHibernateUtils;

	@Autowired
	SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<MasDepartment> getDepartmentList() {
		List<MasDepartment> departmentList = new ArrayList<MasDepartment>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria cr = session.createCriteria(MasDepartment.class);
			// r.add(Restrictions.eq("user_name", string));
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("DEPARTMENT_CODE").as("DEPARTMENT_CODE"));
			projectionList.add(Projections.property("DEPARTMENT_NAME").as("DEPARTMENT_NAME"));
			projectionList.add(Projections.property("DEPARTMENT_ID").as("DEPARTMENT_ID"));
			cr.setProjection(projectionList);

			departmentList = cr.setResultTransformer(new AliasToBeanResultTransformer(MasDepartment.class)).add(Restrictions.eq("status", 'Y').ignoreCase()).addOrder(Order.asc("departmentName")).list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return departmentList;
	}

	@Override
	public MasEmployee checkEmp(Long i) {

		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria cr = session.createCriteria(MasEmployee.class);
		cr.add(Restrictions.eq("employeeId", i));
		MasEmployee list = (MasEmployee) cr.uniqueResult();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return list;
	}

	@Override
	public List<MasIcd> getIcd() {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria cr = session.createCriteria(MasIcd.class);

		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("icdCode").as("icdCode"));
		projectionList.add(Projections.property("icdName").as("icdName"));
		cr.setProjection(projectionList);
		List<MasIcd> list = cr.setResultTransformer(new AliasToBeanResultTransformer(MasIcd.class)).list();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return list;
	}

	/**
	 * @author rajdeo.kumar
	 */
	@Override
	public MasState checkMasState(String stateCode) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria criteria = session.createCriteria(MasState.class);
		criteria.add(Restrictions.eq("stateCode", stateCode));
		MasState masStateObj = (MasState) criteria.uniqueResult();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return masStateObj;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, List<MasState>> getAllStates(JSONObject jsonObject) {
		Map<String, List<MasState>> map = new HashMap<String, List<MasState>>();
		List<MasState> stateList = new ArrayList<MasState>();
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();

		Criteria criteria = session.createCriteria(MasState.class, "masstate");
		criteria.createAlias("masstate.country", "country").add(Restrictions.eq("country.countryId", new Long(1)));

		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("stateCode").as("stateCode"));
		projectionList.add(Projections.property("stateName").as("stateName"));
		projectionList.add(Projections.property("status").as("status"));
		criteria.setProjection(projectionList);
		stateList = criteria.setResultTransformer(new AliasToBeanResultTransformer(MasState.class)).list();
		getHibernateUtils.getHibernateUtlis().CloseConnection();

		map.put("stateList", stateList);
		return map;
	}

	@Override
	public List<MasCommand> validateMasCommand(String commandCode, String commandName) {
		List<MasCommand> cmdList = null;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasCommand.class);
			criteria.add(Restrictions.or(Restrictions.eq("commandCode", commandCode),
					Restrictions.eq("commandName", commandName)));
			cmdList = criteria.list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return cmdList;
	}

	@Override
	public String addMasCommand(MasCommand masCommand) {
		String result = "";
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			Serializable savedObj = session.save(masCommand);
			tx.commit();
			
			if (savedObj != null) {
				result = "200";
			} else {
				result = "500";
			}
			session.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public MasCommand chkCommand(String commandCode) {

		MasCommand masCmd = new MasCommand();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasCommand.class);
			criteria.add(Restrictions.eq("commandCode", commandCode));
			masCmd = (MasCommand) criteria.uniqueResult();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return masCmd;
	}

	@Override
	public Map<String, List<MasCommand>> getAllCommand(JSONObject jsonObj) {
		Map<String, List<MasCommand>> mapObj = new HashMap<String, List<MasCommand>>();
		int pageSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
		int pageNo= 0;

		List totalMatches = new ArrayList();
		List<MasCommand> masCmdList = new ArrayList<MasCommand>();
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria criteria = session.createCriteria(MasCommand.class);

		if (jsonObj.get("PN") != null)
			pageNo = Integer.parseInt(jsonObj.get("PN").toString());

		String cName = "";
		if (jsonObj.has("commandName")) {
			cName = "%"+jsonObj.get("commandName") + "%";
			if (jsonObj.get("commandName").toString().length() > 0
					&& !jsonObj.get("commandName").toString().trim().equalsIgnoreCase("")) {
				criteria.add(Restrictions.ilike("commandName", cName));

			}
		}
		criteria.addOrder(Order.asc("commandName"));

		totalMatches = criteria.list();
		criteria.setFirstResult((pageSize) * (pageNo - 1));
		criteria.setMaxResults(pageSize);

		masCmdList = criteria.list();
		
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		mapObj.put("masCmdList", masCmdList);
		mapObj.put("totalMatches", totalMatches);
		return mapObj;
	}

	@Override
	public List<MasCommand> getCommand(String commandName) {
		Object[] status = new Object[] { "y" };

		List<MasCommand> masCmdList = new ArrayList<MasCommand>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasCommand.class);
			if (commandName.length() > 0 && !commandName.trim().equalsIgnoreCase("")) {

				criteria.add(Restrictions.ilike("commandName", commandName));
			}
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("commandId").as("commandId"));
			projectionList.add(Projections.property("commandCode").as("commandCode"));
			projectionList.add(Projections.property("commandName").as("commandName"));
			projectionList.add(Projections.property("status").as("status"));

			criteria.setProjection(projectionList);

			masCmdList = criteria.setResultTransformer(new AliasToBeanResultTransformer(MasCommand.class)).list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return masCmdList;
	}

	@Override
	public String updateCommand(Long commandId, String commandCode, String commandName, Long commandtypeId, Long userId) {
		String result = "";
		try {
			List<MasCommand> masCmdList = new ArrayList<MasCommand>();
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();

			if (commandId != 0) {
				Object cmdObject = session.load(MasCommand.class, commandId);
				MasCommand masCommand = (MasCommand) cmdObject;

				Transaction transaction = session.beginTransaction();
				masCommand.setCommandCode(commandCode.toUpperCase());
				masCommand.setCommandName(commandName.toUpperCase());

				MasCommandType commandType = new MasCommandType();
				commandType.setCommandtypeId(commandtypeId);
				//masCommand.setMasCommandType(commandType);

				//masCommand.setStatus("Y");
				Users users = new Users();
				users.setUserId(userId);
				//masCommand.setUser(users);

				long d = System.currentTimeMillis();
				Timestamp date = new Timestamp(d);

				//masCommand.setLastChgDate(date);
				session.update(masCommand);
				transaction.commit();
				result = "200";
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return result;
	}

	@Override
	public String updateCommandStatus(Long commandId, String commandCode, String status, Long userId) {
		// Object[] status = new Object[] {"y"};
		String result = "";
		try {

			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();

			Object cmdObject = session.load(MasCommand.class, commandId);
			MasCommand masCommand = (MasCommand) cmdObject;

			Transaction transaction = session.beginTransaction();
			
			session.update(masCommand);
			transaction.commit();
			result = "200";
			// }
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	/*****************************
	 * MAS UNIT
	 ********************************************/
	@Override
	public Map<String, List<MasUnit>> getAllUnit(JSONObject jsondata) {
		Map<String, List<MasUnit>> map = new HashMap<String, List<MasUnit>>();
		List<MasUnit> masUnitList = new ArrayList<MasUnit>();
		List<String[]> masObject = null;
		
		int pageSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
		int pageNo=0;

		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasUnit.class)/*.createAlias("masCommand", "masCommand")*/
					.createAlias("masUnitType", "masUnitType");

			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("unitId").as("unitId"));
			projectionList.add(Projections.property("unitName").as("unitName"));
			
			projectionList.add(Projections.property("masUnitType.unitTypeName"));
			projectionList.add(Projections.property("masUnitType.unitTypeId"));
			criteria.setProjection(projectionList);

			if (jsondata.get("PN") != null)
				pageNo = Integer.parseInt(jsondata.get("PN").toString());

			String uName = "";
			if (jsondata.has("unitName")) {
				uName = "%"+jsondata.get("unitName") + "%";
				if (jsondata.get("unitName").toString().length() > 0
						&& !jsondata.get("unitName").toString().trim().equalsIgnoreCase("")) {
					criteria.add(Restrictions.ilike("unitName", uName));
				}
			}

			criteria.addOrder(Order.asc("unitName"));
			List totalMatches = criteria.list();
			criteria.setFirstResult((pageSize) * (pageNo - 1));
			criteria.setMaxResults(pageSize);
			masObject = criteria.list();

			MasUnit masUnit = null;
			MasUnitType masUnittype = null;
			//MasCommand masCommand = null;

			if (CollectionUtils.isNotEmpty(masObject)) {
				for (Iterator<?> it = masObject.iterator(); it.hasNext();) {
					Object[] row = (Object[]) it.next();

					masUnit = new MasUnit();
					//masCommand = new MasCommand();
					masUnittype = new MasUnitType();
					if (row[0] != null) {
						masUnit.setUnitId(Long.parseLong(row[0].toString()));
					}

					if (row[1] != null) {
						masUnit.setUnitName(row[1].toString());
					}
					
					if (row[2] != null) {

						masUnittype.setUnitTypeName(row[2].toString());
						masUnit.setMasUnittype(masUnittype);
					}
					
					if (row[3] != null) {
						masUnittype.setUnitTypeId(Long.parseLong(row[3].toString()));
						masUnit.setMasUnittype(masUnittype);
					}
					masUnitList.add(masUnit);
				}
			}
			map.put("masUnitList", masUnitList);
			map.put("totalMatches", totalMatches);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@Override
	public List<MasCommand> getCommandList() {
		List<MasCommand> cList = new ArrayList<MasCommand>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasCommand.class);

			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("commandId").as("commandId"));
			projectionList.add(Projections.property("commandName").as("commandName"));
			criteria.setProjection(projectionList);
			criteria.addOrder(Order.asc("commandName"));
			cList = criteria.setResultTransformer(new AliasToBeanResultTransformer(MasCommand.class)).list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return cList;
	}

	@Override
	public List<MasUnit> validateUnit(String unitName) {
		List<MasUnit> masUnitList = new ArrayList<MasUnit>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasUnit.class);
			criteria.add(Restrictions.eq("unitName", unitName));
			masUnitList = criteria.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return masUnitList;
	}

	@Override
	public String addMasUnit(MasUnit masUnit) {
		String result = "";
		Transaction tx=null;
		Session session=null;

		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			Serializable savedObj = session.save(masUnit);
			tx.commit();
			if (savedObj != null) {
				result = "200";
			} else {
				result = "500";
			}
			session.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return result;

	}

	@Override
	public List<MasUnitType> getUnitTypeList() {
		List<MasUnitType> unitTypeList = new ArrayList<MasUnitType>();

		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasUnitType.class);

			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("unitTypeId").as("unitTypeId"));
			projectionList.add(Projections.property("unitTypeName").as("unitTypeName"));
			criteria.setProjection(projectionList);
			criteria.addOrder(Order.asc("unitTypeName"));
			unitTypeList = criteria.setResultTransformer(new AliasToBeanResultTransformer(MasUnitType.class)).list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return unitTypeList;
	}

	@Override
	public String updateUnit(Long uId, String uName, Long commandId, String unitAddress, Long uTypId,Long userId) {
		String result = "";
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			List<MasUnit> masUnitList = new ArrayList<MasUnit>();
			Criteria criteria = session.createCriteria(MasUnit.class);
			criteria.add(Restrictions.eq("unitId", uId));
			masUnitList = criteria.list();

			if (masUnitList != null && masUnitList.size() > 0) {
				for (int i = 0; i < masUnitList.size(); i++) {
					Long Id = masUnitList.get(i).getUnitId();

					Object unitObject = session.load(MasUnit.class, Id);
					MasUnit masUnit = (MasUnit) unitObject;
					long d = System.currentTimeMillis();
					Timestamp date = new Timestamp(d);

					Transaction tx = session.beginTransaction();
					masUnit.setUnitName(uName.toUpperCase());
					//masUnit.setUnitAddress(unitAddress.toUpperCase());

					MasCommand command = new MasCommand();
					command.setCommandId(commandId);
				//	masUnit.setMasCommand(command);

					MasUnitType masUnitType = new MasUnitType();
					masUnitType.setUnitTypeId(uTypId);
					masUnit.setMasUnittype(masUnitType);
					//masUnit.setUnitParentId(uTypId);
					
					Users users = new Users();
					users.setUserId(userId);
					//masUnit.setUser(users);
					
				//masUnit.setLastChgDate(date);
					session.update(masUnit);
					tx.commit();
					result += "200";
				}
			} else {
				result += "500";
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public String updateUnitStatus(Long unitId, String uName, String status,Long userId) {
		String result = "";
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();

			Object object = session.load(MasUnit.class, unitId);
			MasUnit masUnit = (MasUnit) object;
			Transaction transaction = session.beginTransaction();

			
			session.update(masUnit);
			transaction.commit();
			result = "200";

		} catch (Exception e) {

		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	
	@Override
	public List<MasCommandType> getCommandTypeList() {
		List<MasCommandType> cmdTypList = new ArrayList<MasCommandType>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasCommandType.class);

			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("commandtypeId").as("commandtypeId"));
			projectionList.add(Projections.property("commandtypeName").as("commandtypeName"));
			criteria.setProjection(projectionList);
			criteria.addOrder(Order.asc("commandtypeName"));
			cmdTypList = criteria.setResultTransformer(new AliasToBeanResultTransformer(MasCommandType.class)).list();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return cmdTypList;
	}

	/****************************************
	 * MAS HOSPITAL
	 **********************************************************/
	@Override
	public List<MasUnit> getUnitNameList() {
		List<MasUnit> mUnitList = new ArrayList<MasUnit>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasUnit.class);

			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("unitId").as("unitId"));
			projectionList.add(Projections.property("unitName").as("unitName"));
			criteria.setProjection(projectionList);
			criteria.addOrder(Order.asc("unitName"));
			mUnitList = criteria.setResultTransformer(new AliasToBeanResultTransformer(MasUnit.class)).add(Restrictions.eq("status", 'Y').ignoreCase()).list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mUnitList;
	}

	@Override
	public Map<String, Object> getAllHospital(JSONObject jsonObj) {
		int pageSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
		int pageNo=0;

		Map<String, Object> mapObj = new HashMap<String, Object>();
		List<MasHospital> mHospitalList = new ArrayList<MasHospital>();
		List<MasUnit> mUnitList = new ArrayList<MasUnit>();
		List <Object[]>objectList= new ArrayList<Object[]>();
		List <Object[]>objectList1= new ArrayList<Object[]>();
		List totalMatches = new ArrayList();
		String sql="";
		String unitName = "";
		Query query=null;
		try {
			/*
			 * 
			 * Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			 * Criteria criteria = session.createCriteria(MasUnit.class); if
			 * (jsonObj.get("PN") != null && !jsonObj.get("PN").toString().equals("0"))
			 * pageNo = Integer.parseInt(jsonObj.get("PN").toString());
			 * 
			 * if (jsonObj.has("unitName")) { unitName =
			 * "%"+jsonObj.get("unitName").toString() + "%"; if
			 * (jsonObj.get("unitName").toString().length() > 0 &&
			 * !jsonObj.get("unitName").toString().trim().equalsIgnoreCase("")) {
			 * criteria.add(Restrictions.ilike("unitName", unitName)); } }
			 * if(jsonObj.has("unitName")) { if(jsonObj.get("unitName")== "") {
			 * sql="select mu.unit_id, mu.unit_name, mh.hospital_id, mh.status from ship.vu_mas_unit mu left outer join ship.mas_hospital mh on mh.unit_id=mu.unit_id order by mu.unit_name"
			 * ; }else { unitName = "%"+jsonObj.get("unitName").toString() + "%"; sql =
			 * "select mu.unit_id, mu.unit_name, mh.hospital_id, mh.status from ship.vu_mas_unit mu left outer join ship.mas_hospital mh on mh.unit_id=mu.unit_id where UPPER (mu.unit_name) LIKE  UPPER ('%"
			 * +unitName+"%') order by mu.unit_name"; } query = (Query)
			 * session.createSQLQuery(sql); query.setFirstResult((pageSize) * (pageNo - 1));
			 * query.setMaxResults(pageSize); objectList = query.list(); }
			 * if(jsonObj.has("hospitalId")) { Long
			 * hospitalId=Long.parseLong(jsonObj.get("hospitalId").toString());
			 * if(jsonObj.get("hospitalId") !=null) {
			 * 
			 * sql="WITH RECURSIVE HOSPITAL (unitId ,unitName,hospitalId,status ) AS (" +
			 * "        SELECT  e.UNIT_ID, e.HOSPITAL_NAME,e.HOSPITAL_ID,e.status" +
			 * "        FROM ship.MAS_HOSPITAL e" + "         UNION" +
			 * "        SELECT p.UNIT_ID, p.HOSPITAL_NAME,p.HOSPITAL_ID,p.status " +
			 * "        FROM ship.MAS_HOSPITAL p" + "        JOIN ship.VU_MAS_PUNIT e" +
			 * "        ON e.centity = e.pentity " + ")" +
			 * "SELECT * FROM HOSPITAL where hospitalId="+hospitalId; } query = (Query)
			 * session.createSQLQuery(sql); objectList1 = query.list();
			 * 
			 * }
			 * 
			 * criteria.addOrder(Order.asc("unitName")); totalMatches = criteria.list();
			 * mUnitList = criteria.list(); mapObj.put("totalMatches", totalMatches);
			 * mapObj.put("mUnitList", mUnitList); mapObj.put("objectList", objectList);
			 * mapObj.put("objectList1", objectList1);
			 * 
			 */} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mapObj;
	}	

	
	@Override
	public String updateHospitalMasterStatus(Long hospitalId,String status, Long userId) {
		String result = "";
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();

			Object object = session.load(MasHospital.class, hospitalId);
			MasHospital masHospital = (MasHospital) object;
			Transaction transaction = session.beginTransaction();

			if (masHospital.getStatus().equalsIgnoreCase("Y") || masHospital.getStatus().equalsIgnoreCase("y")) {
				masHospital.setStatus("N");
			} else if (masHospital.getStatus().equalsIgnoreCase("N") || masHospital.getStatus().equalsIgnoreCase("n")) {
				masHospital.setStatus("Y");
			} else {
				masHospital.setStatus("Y");
			}
			session.update(masHospital);
			transaction.commit();
			result = "200";
			// }

		} catch (Exception e) {

		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public String updateHospitalDetails(Long hospitalId, String hospitalName, Long unitId, Long userId,String status) {
		String result = "";
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();

			Object object = session.load(MasHospital.class, hospitalId);
			MasHospital masHospital = (MasHospital) object;
			Transaction transaction = session.beginTransaction();
			masHospital.setHospitalName(hospitalName.toUpperCase());
			masHospital.setStatus(status);

			MasUnit masUnit = new MasUnit();
			masUnit.setUnitId(unitId);
			masHospital.setMasUnit(masUnit);

			Users user = new Users();
			user.setUserId(userId);
			masHospital.setUser(user);

			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);
			masHospital.setLastChgDate(date);
			session.update(masHospital);
			transaction.commit();

			result = "200";
			// }
			// }

		} catch (Exception e) {

		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public List<MasHospital> validateMasHospital(String hospitalCode, String hospitalName) {
		List<MasHospital> hospitalList = new ArrayList<MasHospital>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasHospital.class);
			criteria.add(Restrictions.or(Restrictions.eq("hospitalCode", hospitalCode),
					Restrictions.eq("hospitalName", hospitalName)));
			hospitalList = criteria.list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return hospitalList;
	}

	@Override
	public String addMasHospital(MasHospital hospital) {
		String result = "";
		Transaction transaction=null;
		Session session=null;

		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			transaction = session.beginTransaction();
			Serializable savedObj = session.save(hospital);
			transaction.commit();
			
			if (savedObj != null) {

				result = "200";
			} else {
				result = "500";
			}
			session.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return result;
	}

	@Override
	public MasHospital checkMiRoom(Long unitId,String unitName,String status,Long userId) {
		MasHospital miHospital = new MasHospital();
		MasUnit masUnit = new MasUnit();
		
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Transaction transaction = session.beginTransaction();
			List<MasHospital> hospList = new ArrayList<MasHospital>();
			
			Criteria criteria = session.createCriteria(MasHospital.class);			
			criteria.createAlias("masUnit", "mu");			
			criteria.add(Restrictions.eq("mu.unitId", unitId));			
			hospList = criteria.list();
			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);			
			if(hospList.size()>0)
			{
				
				if(!status.equalsIgnoreCase("y"))
				{
					
					Long hospitalId = hospList.get(0).getHospitalId();	
					//System.out.println("hospitalId :: "+hospitalId);
					MasHospital masHospital =  (MasHospital) session.load(MasHospital.class, hospitalId);
					
					Users user = new Users();
					user.setUserId(userId);
					masHospital.setUser(user);
					masHospital.setLastChgDate(date);
					masHospital.setStatus(status);
					
					
					session.update(masHospital);
				}
				if(!status.equalsIgnoreCase("n"))
				{
					
					Long hospitalId = hospList.get(0).getHospitalId();	
					////System.out.println("hospitalId :: "+hospitalId);
					MasHospital masHospital =  (MasHospital) session.load(MasHospital.class, hospitalId);
					
					Users user = new Users();
					user.setUserId(userId);
					masHospital.setUser(user);
					masHospital.setLastChgDate(date);
					masHospital.setStatus(status);
					
					
					session.update(masHospital);
				}
				
			}
			else
			{
				if(status.equalsIgnoreCase("y"))
				{
					Users user = new Users();
					user.setUserId(userId);
					miHospital.setUser(user);
					
					MasUnit masunit = new MasUnit();
					masunit.setUnitId(unitId);
					miHospital.setMasUnit(masunit);
					
					miHospital.setHospitalName(unitName);
					miHospital.setLastChgDate(date);
					miHospital.setStatus(status);
					session.save(miHospital);
				}
			}
			
			transaction.commit();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return miHospital;
	}
	
	/******************************************
	 * MAS RELATION
	 ********************************************************************/
	@Override
	public Map<String, List<MasRelation>> getAllRelation(JSONObject jsonObj) {
		int pageSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
		int pageNo=0;

		Map<String, List<MasRelation>> mapObj = new HashMap<String, List<MasRelation>>();
		List<MasRelation> mRelationList = new ArrayList<MasRelation>();
		List totalMatches = new ArrayList();

		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasRelation.class);

			if (jsonObj.get("PN") != null)
				pageNo = Integer.parseInt(jsonObj.get("PN").toString());

			String rName = "";
			String rCode = "";
			if (jsonObj.has("relationName")) {
				rName = "%"+jsonObj.get("relationName") + "%";
				if (jsonObj.get("relationName").toString().length() > 0
						&& !jsonObj.get("relationName").toString().trim().equalsIgnoreCase("")) {
					criteria.add(Restrictions.ilike("relationName", rName));
				}
			}

			criteria.addOrder(Order.asc("relationCode"));
			totalMatches = criteria.list();

			criteria.setFirstResult((pageSize) * (pageNo - 1));
			criteria.setMaxResults(pageSize);
			mRelationList = criteria.list();

			mapObj.put("totalMatches", totalMatches);
			mapObj.put("mRelationList", mRelationList);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mapObj;
	}

	@Override
	public String updateRelationDetails(Long relationId, String relationCode, String relationName, Long userId) {
		String result = "";
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();

			Object object = session.load(MasRelation.class, relationId);
			MasRelation masRelation = (MasRelation) object;
			Transaction transaction = session.beginTransaction();
			masRelation.setRelationCode(relationCode);
			masRelation.setRelationName(relationName.toUpperCase());
			

			Users user = new Users();
			user.setUserId(userId);

			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);
			session.update(masRelation);
			transaction.commit();

			result = "200";

		} catch (Exception e) {

		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public MasRelation checkMasRelation(Long relationCode) {
		MasRelation mRelation = new MasRelation();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasRelation.class);
			criteria.add(Restrictions.eq("relationCode", relationCode));
			mRelation = (MasRelation) criteria.uniqueResult();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mRelation;
	}

	@Override
	public String updateRelationStatus(Long realtionId, Long relationCode, String status,Long userId) {
		String result = "";
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();

			Object object = session.load(MasRelation.class, realtionId);
			MasRelation masRelation = (MasRelation) object;
			Transaction transaction = session.beginTransaction();

			
			session.update(masRelation);
			transaction.commit();
			result = "200";
			
		} catch (Exception e) {

		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public List<MasRelation> validateRelation(Long relationCode, String relationName) {
		List<MasRelation> relList = new ArrayList<MasRelation>();

		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasRelation.class);
			criteria.add(Restrictions.or(Restrictions.eq("relationCode", relationCode),
					Restrictions.eq("relationName", relationName)));
			relList = criteria.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return relList;
	}

	
	@Override
	public String addRelation(MasRelation masRelation) {
		String result = "";
		Transaction transaction=null;
		Session session=null;

		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			transaction = session.beginTransaction();
			Serializable savedObj = session.save(masRelation);
			transaction.commit();
			
			if (savedObj != null) {

				result = "200";
			} else {
				result = "500";
			}
			session.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return result;
	}
	

	/**************************************
	 * MAS APPOINTMENT TYPE
	 ************************************************************************/
	@Override
	public List<MasAppointmentType> validateAppointmentType(String appointmentTypeCode, String appointmentTypeName) {
		List<MasAppointmentType> appointmentTypesList = new ArrayList<MasAppointmentType>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasAppointmentType.class);
			criteria.add(Restrictions.or(Restrictions.eq("appointmentTypeCode", appointmentTypeCode),
					Restrictions.eq("appointmentTypeName", appointmentTypeName)));
			appointmentTypesList = criteria.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return appointmentTypesList;
	}
	
	@Override
	public List<MasAppointmentType> validateAppointmentTypeUpdate(String appointmentTypeName) {
		List<MasAppointmentType> appointmentTypesList = new ArrayList<MasAppointmentType>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasAppointmentType.class);
			criteria.add(Restrictions.eq("appointmentTypeName", appointmentTypeName));
			appointmentTypesList = criteria.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return appointmentTypesList;
	}
	@Override
	public String addAppointmentType(MasAppointmentType masAppointmentType) {
		String result = "";
		Transaction transaction=null;
		Session session=null;

		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			transaction = session.beginTransaction();
			Serializable savedObj = session.save(masAppointmentType);
			transaction.commit();
			
			if (savedObj != null) {

				result = "200";
			} else {
				result = "500";
			}
			session.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return result;
	}


	@Override
	public Map<String, List<MasAppointmentType>> getAllAppointmentType(JSONObject jsonObj) {
		int pageSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
		int pageNo=0;

		Map<String, List<MasAppointmentType>> mapObj = new HashMap<String, List<MasAppointmentType>>();
		List<MasAppointmentType> mAppointmentTypeList = new ArrayList<MasAppointmentType>();
		List totalMatches = new ArrayList();

		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasAppointmentType.class);

			if (jsonObj.get("PN") != null)
				pageNo = Integer.parseInt(jsonObj.get("PN").toString());

			String aName = "";
			String rCode = "";
			if (jsonObj.has("appointmentTypeName")) {
				aName = "%"+jsonObj.get("appointmentTypeName") + "%";
				if (jsonObj.get("appointmentTypeName").toString().length() > 0
						&& !jsonObj.get("appointmentTypeName").toString().trim().equalsIgnoreCase("")) {
					criteria.add(Restrictions.ilike("appointmentTypeName", aName));
				}
			}

			criteria.addOrder(Order.asc("appointmentTypeName"));
			totalMatches = criteria.list();

			criteria.setFirstResult((pageSize) * (pageNo - 1));
			criteria.setMaxResults(pageSize);
			mAppointmentTypeList = criteria.list();

			mapObj.put("totalMatches", totalMatches);
			mapObj.put("mAppointmentTypeList", mAppointmentTypeList);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mapObj;
	}

	@Override
	public String updateAppointmentTypeDetails(Long appointmentTypeId, String appointmentTypeCode,
			String appointmentTypeName, Long userId) {
		String result = "";
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();

			Object object = session.load(MasAppointmentType.class, appointmentTypeId);
			MasAppointmentType masAppointmentType = (MasAppointmentType) object;

			Transaction transaction = session.beginTransaction();
			masAppointmentType.setAppointmentTypeCode(appointmentTypeCode.toUpperCase());
			masAppointmentType.setAppointmentTypeName(appointmentTypeName.toUpperCase());
			Users users = new Users();
			users.setUserId(userId);
			masAppointmentType.setUser(users);

			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);
			masAppointmentType.setLastChgDate(date);
			session.update(masAppointmentType);
			transaction.commit();

			result = "200";

		} catch (Exception e) {

		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public MasAppointmentType checkMasAppointmentType(String appointmentTypeCode) {
		MasAppointmentType mAppointmentType = new MasAppointmentType();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasAppointmentType.class);
			criteria.add(Restrictions.eq("appointmentTypeCode", appointmentTypeCode));
			mAppointmentType = (MasAppointmentType) criteria.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mAppointmentType;
	}

	@Override
	public String updateAppointmentTypeStatus(Long appointmentTypeId, String appointmentTypeCode, String status, Long userId) {
		String result = "";
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();

			Object object = session.load(MasAppointmentType.class, appointmentTypeId);

			MasAppointmentType masAppointmentType = (MasAppointmentType) object;
			Transaction transaction = session.beginTransaction();

			if (masAppointmentType.getStatus().equalsIgnoreCase("Y")
					|| masAppointmentType.getStatus().equalsIgnoreCase("y")) {
				masAppointmentType.setStatus("N");
			} else if (masAppointmentType.getStatus().equalsIgnoreCase("N")
					|| masAppointmentType.getStatus().equalsIgnoreCase("n")) {
				masAppointmentType.setStatus("Y");
			} else {
				masAppointmentType.setStatus("Y");
			}
			session.update(masAppointmentType);
			transaction.commit();
			result = "200";

		} catch (Exception e) {

		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	/****************************
	 * MAS DEPARTMENT
	 *****************************************************/
	@SuppressWarnings("unchecked")
	@Override
	public List<MasDepartmentType> getDepartmentTypeList() {
		List<MasDepartmentType> deptTypeList = new ArrayList<MasDepartmentType>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasDepartmentType.class);

			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("departmentTypeId").as("departmentTypeId"));
			projectionList.add(Projections.property("departmentTypeName").as("departmentTypeName"));
			criteria.setProjection(projectionList);

			deptTypeList = criteria.setResultTransformer(new AliasToBeanResultTransformer
					(MasDepartmentType.class)).add(Restrictions.eq("status", 'Y').ignoreCase()).addOrder(Order.asc("departmentTypeName")).list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return deptTypeList;
	}

	@Override
	public Map<String, List<MasDepartment>> getAllDepartment(JSONObject jsondata) {
		Map<String, List<MasDepartment>> map = new HashMap<String, List<MasDepartment>>();
		List<MasDepartment> departmentList = new ArrayList<MasDepartment>();
		int pageSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
		int pageNo=0;
		List totalMatches=new ArrayList();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasDepartment.class);

			if (jsondata.get("PN") != null && ! jsondata.get("PN").toString().equals("0")) {
				pageNo = Integer.parseInt(jsondata.get("PN").toString());

			String dName = "";
			if (jsondata.has("departmentName")) {
				dName = "%"+jsondata.get("departmentName") + "%";
				if (jsondata.get("departmentName").toString().length() > 0
						&& !jsondata.get("departmentName").toString().trim().equalsIgnoreCase("")) {
					criteria.add(Restrictions.ilike("departmentName", dName));

				}
			}
			
			totalMatches = criteria.list();
			criteria.addOrder(Order.asc("departmentName"));
			criteria.setFirstResult((pageSize) * (pageNo - 1));
			criteria.setMaxResults(pageSize);
			departmentList = criteria.list();
			}
			
			
			if(jsondata.get("PN").toString().equals("0"))
			{
				 criteria.add(Restrictions.eq("status", "y").ignoreCase());
				 criteria.addOrder(Order.asc("departmentName"));
				 totalMatches = criteria.list();				 
				 departmentList = criteria.list();	
				
			
			}
			
			

			map.put("departmentList", departmentList);
			map.put("totalMatches", totalMatches);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@Override
	public String addDepartment(MasDepartment masDepartment) {
		String result = "";
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			Serializable savedObj = session.save(masDepartment);
			tx.commit();
			
			
			if (savedObj != null) {
				result = "200";
			} else {
				result = "500";
			}
			session.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return result;
	}

	@Override
	public List<MasDepartment> validateDepartment(String departmentCode, String departmentName,Long departmentTypeId) {
		List<MasDepartment> departmentList = new ArrayList<MasDepartment>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasDepartment.class);
			criteria.add(Restrictions.eq("departmentCode", departmentTypeId))
			.add(Restrictions.or(Restrictions.eq("masDepartmentType.departmentTypeId", departmentCode),
					Restrictions.eq("departmentName", departmentName)));
			departmentList = criteria.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return departmentList;
	}
	
	@Override
	public List<MasDepartment> validateDepartmentUpdate(Long departmentTypeId, String departmentName) {
		List<MasDepartment> departmentList = new ArrayList<MasDepartment>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasDepartment.class);
			criteria.add(Restrictions.and(Restrictions.eq("masDepartmentType.departmentTypeId", departmentTypeId),
					Restrictions.eq("departmentName", departmentName)));
			departmentList = criteria.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return departmentList;
	}

	@Override
	public MasDepartment checkDepartment(String departmentCode) {
		MasDepartment mdepart = new MasDepartment();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasDepartment.class);
			criteria.add(Restrictions.eq("departmentCode", departmentCode));
			mdepart = (MasDepartment) criteria.uniqueResult();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mdepart;
	}

	@Override
	public String updateDepartmentStatus(Long departmentId, String departmentCode, String status, Long userId) {
		String result = "";
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();

			Object object = session.load(MasDepartment.class, departmentId);

			MasDepartment masDepartment = (MasDepartment) object;
			Transaction transaction = session.beginTransaction();
			Users user = new Users();
			user.setUserId(userId);
			masDepartment.setUser(user);
			if (masDepartment.getStatus().equalsIgnoreCase("Y") || masDepartment.getStatus().equalsIgnoreCase("y")) {
				masDepartment.setStatus("N");
			} else if (masDepartment.getStatus().equalsIgnoreCase("N")
					|| masDepartment.getStatus().equalsIgnoreCase("n")) {
				masDepartment.setStatus("Y");
			} else {
				masDepartment.setStatus("Y");
			}
			session.update(masDepartment);
			transaction.commit();
			result = "200";
			// }

		} catch (Exception e) {

		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public String updateDepartmentDetails(Long departmentId, String departmentCode, String departmentName,
			Long departmentTypeId, Long userId) {
		String result = "";
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();

			Object object = session.load(MasDepartment.class, departmentId);
			MasDepartment masDepartment = (MasDepartment) object;

			Transaction transaction = session.beginTransaction();
			masDepartment.setDepartmentCode(departmentCode.toUpperCase());
			masDepartment.setDepartmentName(departmentName.toUpperCase());

			MasDepartmentType masDepartmentType = new MasDepartmentType();
			masDepartmentType.setDepartmentTypeId(departmentTypeId);
			masDepartment.setMasDepartmentType(masDepartmentType);
			Users users = new Users();
			users.setUserId(userId);
			masDepartment.setUser(users);
			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);
			masDepartment.setLastChgDate(date);
			session.update(masDepartment);
			transaction.commit();

			result = "200";
			// }
			// }

		} catch (Exception e) {

		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	/****************************
	 * MAS FREQUENCY
	 *********************************************************/
	@Override
	public Map<String, List<MasFrequency>> getAllOpdFrequency(JSONObject jsondata) {
		Map<String, List<MasFrequency>> map = new HashMap<String, List<MasFrequency>>();
		List<MasFrequency> frequencyList = new ArrayList<MasFrequency>();
		
		int pageSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
		int pageNo=0;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasFrequency.class);

			if (jsondata.get("PN") != null)
				pageNo = Integer.parseInt(jsondata.get("PN").toString());

			String fName = "";
			if (jsondata.has("frequencyName")) {
				fName = "%"+jsondata.get("frequencyName") + "%";
				if (jsondata.get("frequencyName").toString().length() > 0
						&& !jsondata.get("frequencyName").toString().trim().equalsIgnoreCase("")) {
					criteria.add(Restrictions.ilike("frequencyName", fName));

				}
			}
			criteria.addOrder(Order.asc("orderNo"));
			List totalMatches = criteria.list();

			criteria.setFirstResult((pageSize) * (pageNo - 1));
			criteria.setMaxResults(pageSize);
			frequencyList = criteria.list();

			map.put("frequencyList", frequencyList);
			map.put("totalMatches", totalMatches);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@Override
	public List<MasFrequency> validateFrequency(String frequencyCode, String frequencyName) {
		List<MasFrequency> freqList = new ArrayList<MasFrequency>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasFrequency.class);
			criteria.add(Restrictions.or(Restrictions.eq("frequencyCode", frequencyCode),
					Restrictions.eq("frequencyName", frequencyName)));
			freqList = criteria.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return freqList;
	}
	
	@Override
	public List<MasFrequency> validateFrequencyUpdate(String frequencyName,Double feq) {
		List<MasFrequency> freqList = new ArrayList<MasFrequency>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasFrequency.class);
			criteria.add(Restrictions.and(Restrictions.eq("frequencyName", frequencyName),Restrictions.eq("feq", feq)));
			freqList = criteria.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return freqList;
	}

	@Override
	public String addOpdFrequency(MasFrequency masFrequency) {
		String result = "";
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			Serializable savedObj = session.save(masFrequency);
			
			
			if (savedObj != null) {
				result = "200";
			} else {
				result = "500";
			}
			tx.commit();
			session.flush();
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return result;
	}

	@Override
	public String updateFrequencyDetails(Long frequencyId, String frequencyCode, String frequencyName,String frequencyHinName,Double feq,Long userId) {
		String result = "";
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();

			Object object = session.load(MasFrequency.class, frequencyId);
			MasFrequency masOpdFrequency = (MasFrequency) object;

			Transaction transaction = session.beginTransaction();
			masOpdFrequency.setFrequencyCode(frequencyCode.toUpperCase());
			masOpdFrequency.setFrequencyName(frequencyName.toUpperCase());
			masOpdFrequency.setFrequencyHinName(frequencyHinName);
			masOpdFrequency.setFeq(Double.parseDouble(feq.toString()));

			Users user = new Users();
			user.setUserId(userId);
			masOpdFrequency.setUser(user);
			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(new Date().getTime());
			masOpdFrequency.setLastChgDate(date);
			session.update(masOpdFrequency);
			transaction.commit();

			result = "200";

		} catch (Exception e) {

		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public MasFrequency checkFrequency(String frequencyCode) {
		MasFrequency mOpdfreq = new MasFrequency();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasFrequency.class);
			criteria.add(Restrictions.eq("frequencyCode", frequencyCode));
			mOpdfreq = (MasFrequency) criteria.uniqueResult();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mOpdfreq;
	}

	@Override
	public String updateOpdFrequencyStatus(Long frequencyId, String frequencyCode, String status,Long userId) {
		String result = "";
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();

			Object object = session.load(MasFrequency.class, frequencyId);

			MasFrequency masOpdFrequency = (MasFrequency) object;
			Transaction transaction = session.beginTransaction();
			if (masOpdFrequency.getStatus().equalsIgnoreCase("Y")
					|| masOpdFrequency.getStatus().equalsIgnoreCase("y")) {
				masOpdFrequency.setStatus("N");
			} else if (masOpdFrequency.getStatus().equalsIgnoreCase("N")
					|| masOpdFrequency.getStatus().equalsIgnoreCase("n")) {
				masOpdFrequency.setStatus("Y");
			} else {
				masOpdFrequency.setStatus("Y");
			}
			session.update(masOpdFrequency);
			transaction.commit();
			result = "200";

		} catch (Exception e) {

		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public MasUnit checkUnit(String unitName) {
		MasUnit mUnit = new MasUnit();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasUnit.class);
			criteria.add(Restrictions.eq("unitName", unitName));
			mUnit = (MasUnit) criteria.uniqueResult();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mUnit;
	}

	

	/*************************************
	 * MAS IMPANNELED HOSPITAL
	 **************************************************************/
	@Override
	public Map<String, List<MasEmpanelledHospital>> getAllEmpanelledHospital(JSONObject jsonObj) {
		int pageSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
		int pageNo=0;

		Map<String, List<MasEmpanelledHospital>> mapObj = new HashMap<String, List<MasEmpanelledHospital>>();
		List<MasEmpanelledHospital> mImpanneledHospitalList = new ArrayList<MasEmpanelledHospital>();
		List totalMatches = new ArrayList();

		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasEmpanelledHospital.class);

			if (jsonObj.get("PN") != null)
				pageNo = Integer.parseInt(jsonObj.get("PN").toString());

			String empanelledHospName = "";
			String hCode = "";
			if (jsonObj.has("empanelledHospitalName")) {
				empanelledHospName = "%"+jsonObj.get("empanelledHospitalName") + "%";
				if (jsonObj.get("empanelledHospitalName").toString().length() > 0
						&& !jsonObj.get("empanelledHospitalName").toString().trim().equalsIgnoreCase("")) {
					criteria.add(Restrictions.ilike("empanelledHospitalName", empanelledHospName));
				}
			}

			criteria.addOrder(Order.asc("empanelledHospitalName"));
			totalMatches = criteria.list();

			criteria.setFirstResult((pageSize) * (pageNo - 1));
			criteria.setMaxResults(pageSize);
			mImpanneledHospitalList = criteria.list();

			mapObj.put("totalMatches", totalMatches);
			mapObj.put("mImpanneledHospitalList", mImpanneledHospitalList);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mapObj;
	}

	@Override
	public List<MasEmpanelledHospital> validateEmpanelledHospital(String empanelledHospitalName, String empanelledHospitalCode) {

		List<MasEmpanelledHospital> impannelHospitalList = new ArrayList<MasEmpanelledHospital>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasEmpanelledHospital.class);
			criteria.add(Restrictions.or(Restrictions.eq("empanelledHospitalName", empanelledHospitalName),Restrictions.eq("empanelledHospitalCode", empanelledHospitalCode)));

			impannelHospitalList = criteria.list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return impannelHospitalList;
	}

	@Override
	public String addEmpanelledHospital(MasEmpanelledHospital masEmpanelledHospital) {
		String result = "";
		Transaction transaction = null;
		Session session = null;
		
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			transaction = session.beginTransaction();
			Serializable savedObj = session.save(masEmpanelledHospital);
			
			transaction.commit();
			if (savedObj != null) {
				result = "200";
			} else {
				result = "500";
			}
			session.flush();
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		} catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		} finally {
			
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public String updateEmpanelledHospital(JSONObject jsonObject) {
		
		String result = "";
		long d = System.currentTimeMillis();
		Timestamp lastChgDate = new Timestamp(d);
		try {
			if (jsonObject != null) {
		List<MasEmpanelledHospital> validate= validateEmpanelledHospital(jsonObject.get("empanelledHospitalName").toString(), jsonObject.get("empanelledHospitalCode").toString());  
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		String hStatus = "";
		Long empHospId;
		if (jsonObject.has("empanelledHospitalId")) {
			empHospId = Long.parseLong(jsonObject.get("empanelledHospitalId").toString());	
				Object object = session.load(MasEmpanelledHospital.class, empHospId);
				MasEmpanelledHospital masEmpanelledHospital = (MasEmpanelledHospital) object;
				Transaction transaction = session.beginTransaction();
 
				if (jsonObject.has("status")) {
					//System.out.println("status :: " + jsonObject.has("status"));
					hStatus = jsonObject.get("status").toString();
					//System.out.println("status :: " + hStatus);
					if (hStatus.equalsIgnoreCase("active") || hStatus.equalsIgnoreCase("inactive")) {
						if (hStatus.equalsIgnoreCase("active"))
							masEmpanelledHospital.setStatus("Y");
						else
							masEmpanelledHospital.setStatus("N");
						session.update(masEmpanelledHospital);
						transaction.commit();
						result = "200";
					} else {
						masEmpanelledHospital.setEmpanelledHospitalCode(jsonObject.get("empanelledHospitalCode").toString());
						if(validate !=null && validate.get(0).getEmpanelledHospitalName().equalsIgnoreCase(jsonObject.get("empanelledHospitalName").toString())) {
							
						masEmpanelledHospital.setEmpanelledHospitalName(masEmpanelledHospital.getEmpanelledHospitalName());						
						}else {
							masEmpanelledHospital.setEmpanelledHospitalName(jsonObject.get("empanelledHospitalName").toString());	
						}
						masEmpanelledHospital.setEmpanelledHospitalAddress(jsonObject.get("empanelledHospitalAddress").toString());
						masEmpanelledHospital.setCityId(Long.parseLong(jsonObject.get("cityId").toString()));
						masEmpanelledHospital.setPhoneNo(jsonObject.get("phoneNo").toString());

						Users user = new Users();
						user.setUserId(new Long(1));
						masEmpanelledHospital.setUser(user);

						masEmpanelledHospital.setLastChgDate(lastChgDate);
						session.update(masEmpanelledHospital);
						transaction.commit();
						result = "200";
					}

				}

			
		}
	}

} catch (Exception e) {
	e.printStackTrace();
} finally {
	getHibernateUtils.getHibernateUtlis().CloseConnection();
}
return result;
}

	/*****************************
	 * MAS IDEAL WEIGHT
	 ******************************************************/
	@Override
	public Map<String, List<MasIdealWeight>> getAllIdealWeight(JSONObject jsonObj) {
		int pageSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
		int pageNo=0;
		
		Map<String, List<MasIdealWeight>> mapObj = new HashMap<String, List<MasIdealWeight>>();
		List<MasIdealWeight> idealWeightsList = new ArrayList<MasIdealWeight>();
		List totalMatches = new ArrayList();

		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasIdealWeight.class);

			if (jsonObj.get("PN") != null)
				pageNo = Integer.parseInt(jsonObj.get("PN").toString());

			Long administrativeSexId;
			if (jsonObj.get("administrativeSexId") != null && !(jsonObj.get("administrativeSexId")).equals("")) {
				administrativeSexId = Long.parseLong(jsonObj.get("administrativeSexId").toString());
				if (jsonObj.get("administrativeSexId").toString().length() > 0
						&& !jsonObj.get("administrativeSexId").toString().equalsIgnoreCase("0")) {
						
						criteria.createAlias("masAdministrativeSex", "masadminsex");
						criteria.add(Restrictions.eq("masadminsex.administrativeSexId", administrativeSexId));
				}
			}

			totalMatches = criteria.list();

			criteria.setFirstResult((pageSize) * (pageNo - 1));
			criteria.setMaxResults(pageSize);
			idealWeightsList = criteria.list();

			mapObj.put("totalMatches", totalMatches);
			mapObj.put("idealWeightsList", idealWeightsList);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mapObj;
	}

	@Override
	public List<MasIdealWeight> validateIdealWeight(Long genderId, Long ageid, Long heightId) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria criteria = null;
		criteria = session.createCriteria(MasIdealWeight.class)
				.createAlias("masAdministrativeSex", "massex")
				.createAlias("masRange1", "hid")
				.createAlias("masRange2", "aid")
				.add(Restrictions.eq("massex.administrativeSexId", genderId))
				.add(Restrictions.eq("hid.rangeId", ageid))
				.add(Restrictions.eq("aid.rangeId", heightId));		
				//add restriction for ideal weight
		
		@SuppressWarnings("unchecked")
		List<MasIdealWeight> idealWeightList = criteria.list();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return idealWeightList;
	}
	

	@Override
	public String addIdealWeight (MasIdealWeight idealWeightObj) {
		String result="";
		Transaction tx=null;
		Session session=null;
		//System.out.println("idealWeightObj"+idealWeightObj);
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();		
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(idealWeightObj);
			tx.commit();
			
			if(savedObj!=null) {
				result = "200";
			}else {
				result = "500";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	@Override
	public String updateIdealWeight(JSONObject jsonObject) {
	 String result="";
	 Transaction transaction=null;
	 List<MasIdealWeight> validIdealWeight = validateIdealWeight(Long.parseLong(jsonObject.get("genderId").toString()),
				Long.parseLong(jsonObject.get("masRange1").toString()), Long.parseLong(jsonObject.get("masRange2").toString()));	
	 long d = System.currentTimeMillis();
	 Date lastChgDate = new Date(d);
	 
	 try {
	  if (jsonObject != null) {
	   
		  Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	   
	   String hStatus = "";
	   Long idealWtId;
	   //System.out.println("jsonObject+jsonObject+jsonObject ::"+jsonObject);
	   if (jsonObject.has("idealWeightId")) {
		   
	    idealWtId = Long.parseLong(jsonObject.get("idealWeightId").toString());
	    long masRange1 = Long.parseLong(jsonObject.get("masRange1").toString());
	    long masRange2 = Long.parseLong(jsonObject.get("masRange2").toString());
	    Double weight = Double.parseDouble(jsonObject.get("weight").toString());
	    long genderId = Long.parseLong(jsonObject.get("genderId").toString());
	     Object object = session.load(MasIdealWeight.class, idealWtId);
	     MasIdealWeight idealWeight = (MasIdealWeight) object;

	     hStatus = jsonObject.get("status").toString();       
	      if (hStatus.equalsIgnoreCase("active") || hStatus.equalsIgnoreCase("inactive")) {
	       if (hStatus.equalsIgnoreCase("active")) {
	        idealWeight.setStatus("Y");
	       }
	       else {
	        idealWeight.setStatus("N");
	       }
	       transaction = session.beginTransaction();
	       session.update(idealWeight);
	       transaction.commit();
	       
	       result = "statusUpdated";
	      
	      
	     }
	      else {
	    	  
	    	  
	       MasAdministrativeSex masAdministrativeSex = new MasAdministrativeSex();
	       MasRange masRange = new MasRange();
	       MasRange masRangee = new MasRange();
	       if(validIdealWeight !=null && validIdealWeight.size()>0) {
	    	   
	    	   masAdministrativeSex.setAdministrativeSexId(idealWeight.getMasAdministrativeSex().getAdministrativeSexId());	       
		       idealWeight.setMasAdministrativeSex(masAdministrativeSex);	       
		       //idealWeight.setWeight(idealWeight.getWeight());		       
		       masRange.setRangeId(idealWeight.getMasRange1().getRangeId());		       
		       masRangee.setRangeId(idealWeight.getMasRange2().getRangeId());
		       idealWeight.setMasRange2(masRangee);
	    	   
	    	   
	       }
	       else {
	    	   masAdministrativeSex.setAdministrativeSexId(genderId);	       
		       idealWeight.setMasAdministrativeSex(masAdministrativeSex);	       	      
		       masRange.setRangeId(masRange1);
		       idealWeight.setMasRange1(masRange);		      
		       masRangee.setRangeId(masRange2);
		       idealWeight.setMasRange2(masRangee);
	       }
	       
	       idealWeight.setWeight(weight);	
	       Users user = new Users();
	       user.setUserId(Long.parseLong(jsonObject.get("userId").toString()));
	       idealWeight.setUser(user);	       
	       idealWeight.setLastChgDate(lastChgDate);
	       transaction = session.beginTransaction();
	       session.update(idealWeight);
	       transaction.commit();
	      
	       result = "recordUpdated";
	      }

	     }

	    
	   }
	  

	 } catch (Exception e) {
	  e.printStackTrace();
	 } finally {
	  getHibernateUtils.getHibernateUtlis().CloseConnection();
	 }
	 return result;
	}
	
	
	/*********************Nursing Care**************************/
	@Override
	public Map<String, List<MasNursingCare>> getAllmNursingData(JSONObject jsonObj) {
		int pageSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
		int pageNo=0;

		Map<String, List<MasNursingCare>> mapObj = new HashMap<String, List<MasNursingCare>>();
		List<MasNursingCare> mHospitalList = new ArrayList<MasNursingCare>();
		List totalMatches = new ArrayList();

		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasNursingCare.class);

			if (jsonObj.get("PN") != null)
				pageNo = Integer.parseInt(jsonObj.get("PN").toString());

			String hName = "";
			String hCode = "";
			if (jsonObj.has("nursingName")) {
				hName = "%"+jsonObj.get("nursingName") + "%";
				if (jsonObj.get("nursingName").toString().length() > 0
						&& !jsonObj.get("nursingName").toString().trim().equalsIgnoreCase("")) {
					criteria.add(Restrictions.ilike("nursingName", hName));
				}
			}
			if (jsonObj.has("nursingCode")) {

				hCode = jsonObj.get("nursingCode") + "%";
				if (jsonObj.get("nursingCode").toString().length() > 0
						&& !jsonObj.get("nursingCode").toString().trim().equalsIgnoreCase("")) {
					criteria.add(Restrictions.ilike("nursingCode", hCode));
				}
			}
			criteria.addOrder(Order.asc("nursingName"));
			totalMatches = criteria.list();

			criteria.setFirstResult((pageSize) * (pageNo - 1));
			criteria.setMaxResults(pageSize);
			mHospitalList = criteria.list();

			mapObj.put("totalMatches", totalMatches);
			mapObj.put("mHospitalList", mHospitalList);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mapObj;
	}

	@Override
	public List<MasNursingCare> validateMasNursing(String nursingCode, String nursingName, String nursingType) {
		// TODO Auto-generated method stub
		List<MasNursingCare> nursingList = new ArrayList<MasNursingCare>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasNursingCare.class);
			criteria.add(Restrictions.eq("nursingCode", nursingCode))
			.add(Restrictions.and(Restrictions.eq("nursingName", nursingName), Restrictions.eq("nursingType", nursingType)));
			nursingList = criteria.list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return nursingList;
	}
	
	@Override
	public List<MasNursingCare> validateMasNursingUpdate(String nursingName, String nursingType) {
		// TODO Auto-generated method stub
		List<MasNursingCare> nursingList = new ArrayList<MasNursingCare>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasNursingCare.class);
			criteria.add(Restrictions.and(Restrictions.eq("nursingName", nursingName), Restrictions.eq("nursingType", nursingType)));
			nursingList = criteria.list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return nursingList;
	}
	@Override
	public String addMasNursing(MasNursingCare nursingObj) {		
		String result = "";
		Transaction transaction=null;
		Session session=null;
		try {

			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			transaction = session.beginTransaction();
			Serializable savedObj = session.save(nursingObj);
			
			transaction.commit();
			if (savedObj != null) {

				result = "200";
			} else {
				result = "500";
			}
			session.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return result;
	}

	@Override
	public String updateMasNursing(JSONObject jsonObject) {
		// TODO Auto-generated method stub
		String result = "";
		long d = System.currentTimeMillis();
		Timestamp lastChgDate = new Timestamp(d);
		try {
			if (jsonObject != null) {

				List<MasNursingCare> masNursingCarelList = new ArrayList<MasNursingCare>();
				Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria = session.createCriteria(MasNursingCare.class);
				String hStatus = "";
				Long nursingId;
				if (jsonObject.has("nursingId")) {
					nursingId = Long.parseLong(jsonObject.get("nursingId").toString());

					criteria.add(Restrictions.eq("nursingId", nursingId));

					masNursingCarelList = criteria.list();

					for (int i = 0; i < masNursingCarelList.size(); i++) {
						Long masNursingId = masNursingCarelList.get(i).getNursingId();

						Object object = session.load(MasNursingCare.class, masNursingId);
						MasNursingCare masNursingCare = (MasNursingCare) object;

						Transaction transaction = session.beginTransaction();

						if (jsonObject.has("status")) {
							//System.out.println("status :: " + jsonObject.has("status"));
							hStatus = jsonObject.get("status").toString();
							//System.out.println("status :: " + hStatus);
							if (hStatus.equalsIgnoreCase("active") || hStatus.equalsIgnoreCase("inactive")) {
								if (hStatus.equalsIgnoreCase("active"))
									masNursingCare.setStatus("Y");
								else
									masNursingCare.setStatus("N");
								session.update(masNursingCare);
								transaction.commit();
								result = "200";
							} else {
								masNursingCare.setNursingCode(jsonObject.get("nursingCode").toString().toUpperCase());
								masNursingCare.setNursingName(jsonObject.get("nursingName").toString().toUpperCase());
								masNursingCare.setNursingType(jsonObject.get("nursingType").toString().toUpperCase());

								Users user = new Users();
								user.setUserId(new Long(1));
								masNursingCare.setUser(user);

								// masNursingCare.setLastChgBy(new Long(1));
								masNursingCare.setLastChgDate(lastChgDate);
								session.update(masNursingCare);
								transaction.commit();
								result = "200";
							}

						}

					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public List<MasIdealWeight> getAge(JSONObject jsonObject) {
		List<MasIdealWeight> weightList = new ArrayList<MasIdealWeight>();
		StringBuffer buffer = new StringBuffer();
		List listObject = new ArrayList();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasIdealWeight.class);
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("idealWeightId").as("idealWeightId"));
			
			criteria.setProjection(projectionList);

			weightList = criteria.setResultTransformer(new AliasToBeanResultTransformer(MasIdealWeight.class)).list();
			Iterator iterator = weightList.iterator();
			while (iterator.hasNext()) {
				MasIdealWeight object = (MasIdealWeight) iterator.next();
				Map<Object, Object> map = new HashMap<Object, Object>();
				long idealWeightId = object.getIdealWeightId();
				map.put("idealWeightId", idealWeightId);
				listObject.add(map);

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return listObject;
	}

		/****************************Service Type*********************************************************/

	@Override
	public Map<String, List<MasServiceType>> getAllServiceType(JSONObject jsonObj) {
		Map<String, List<MasServiceType>> mapObj = new HashMap<String, List<MasServiceType>>();
		int pageSize = 5;
		int pageNo = 1;

		List totalMatches = new ArrayList();
		List<MasServiceType> masServiceTypeList = new ArrayList<MasServiceType>();
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria criteria = session.createCriteria(MasServiceType.class);

		if (jsonObj.get("PN") != null)
			pageNo = Integer.parseInt(jsonObj.get("PN").toString());

		String stName = "";
		if (jsonObj.has("serviceTypeName")) {
			stName = "%"+jsonObj.get("serviceTypeName") + "%";
			if (jsonObj.get("serviceTypeName").toString().length() > 0
					&& !jsonObj.get("serviceTypeName").toString().trim().equalsIgnoreCase("")) {
				criteria.add(Restrictions.ilike("serviceTypeName", stName));

			}
		}
		criteria.addOrder(Order.asc("serviceTypeName"));

		totalMatches = criteria.list();
		criteria.setFirstResult((pageSize) * (pageNo - 1));
		criteria.setMaxResults(pageSize);

		masServiceTypeList = criteria.list();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		mapObj.put("masServiceTypeList", masServiceTypeList);
		mapObj.put("totalMatches", totalMatches);
		return mapObj;
	}

	@Override
	public String updateServiceType(Long serviceTypeId, String serviceTypeName,Long userId) {
		String result = "";
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria cr=session.createCriteria(MasServiceType.class).add(Restrictions.eq("serviceTypeName", serviceTypeName));
			@SuppressWarnings("unchecked")
			List<MasServiceType> masList=cr.list();
			
			if(masList !=null && !masList.isEmpty()){
				result="serviceTypeNameExist";
			}
			else {
			
			if (serviceTypeId != 0) {
				Object stObject = session.load(MasServiceType.class, serviceTypeId);
				MasServiceType masServiceType = (MasServiceType) stObject;					
				Transaction transaction = session.beginTransaction();				
				masServiceType.setServiceTypeName(serviceTypeName.toUpperCase());
				masServiceType.setStatus("Y");			
				Users users = new Users(); 
				users.setUserId(userId);
				masServiceType.setUser(users);				
				long d = System.currentTimeMillis();
				Timestamp date = new Timestamp(d);
				masServiceType.setLastChgDate(date);
				session.merge(masServiceType);
				transaction.commit();
				session.flush();
				result = "200";
			}
		 }
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return result;
	}



	@Override
	public String addServiceType(MasServiceType masServiceType) {
		String result = "";
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			Criteria cr1=session.createCriteria(MasServiceType.class).add(Restrictions.eq("serviceTypeCode", masServiceType.getServiceTypeCode()));
			Criteria cr2=session.createCriteria(MasServiceType.class).add(Restrictions.eq("serviceTypeName", masServiceType.getServiceTypeName()));
			List<MasServiceType> masList1=cr1.list();
			List<MasServiceType> masList2=cr2.list();
			if(masList1 !=null && !masList1.isEmpty()){
				result="serviceTypeCodeExist";
			}
			else if(masList2 !=null && !masList2.isEmpty()) {
				result="serviceTypeNameExist";
			}else {
					Serializable savedObj = session.save(masServiceType);
					tx.commit();
					
					if (savedObj != null) {
						result = "200";
					} else {
						result = "500";
					}
				}
			session.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		finally {
			
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		
		return result;
	}

	@Override
	public String updateServiceTypeStatus(Long serviceTypeId, String serviceTypeCode, String status) {
		String result = "";
		try {

			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();

			Object stObject = session.load(MasServiceType.class, serviceTypeId);
			MasServiceType masServiceType = (MasServiceType) stObject;

			Transaction transaction = session.beginTransaction();
			if (masServiceType.getStatus().equalsIgnoreCase("Y")) {
				masServiceType.setStatus("n");

			} else if (masServiceType.getStatus().equalsIgnoreCase("N")) {
				masServiceType.setStatus("y");

			} else {
				masServiceType.setStatus("y");	
			}

			session.update(masServiceType);
			transaction.commit();
			session.flush();
			result = "200";
			// }
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	
	
	/**----------------MAS RANK--------------------*/

	@Override
	public List<MasRank> validateMasRank(String rankCode, String rankName) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		//List<MasRank> rankList = new ArrayList<MasRank>();
		//List<MasRank> rankList = null;
		Criteria criteria = null;
		criteria = session.createCriteria(MasRank.class)
							.add(Restrictions.or(Restrictions.eq("rankCode", rankCode), Restrictions.eq("rankName", rankName)));
		List<MasRank> rankList = criteria.list();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return rankList;
	}
	
	
	@Override
	public List<MasRank> validateMasRankUpdate(Long employeeCategoryId, String rankName){
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		List<MasRank> rankList = new ArrayList<MasRank>();
		Criteria criteria = session.createCriteria(MasRank.class);
		criteria.createAlias( "masEmployeeCategory", "mec" );
		criteria.add(Restrictions.and(Restrictions.eq("mec.employeeCategoryId",employeeCategoryId), Restrictions.eq("rankName", rankName)));
		rankList = criteria.list();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return rankList;
	}

	@Override
	public String addMasRank(MasRank masRank) {
		String result="";
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();		
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(masRank);
			tx.commit();
			
			if(savedObj!=null) {
				result = "200";
			}else {
				result = "500";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public MasRank chkRank(String rankName) {
		
		MasRank masRank = new MasRank();	
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria criteria =  session.createCriteria(MasRank.class);
		criteria.add(Restrictions.eq("rankName", rankName));
		masRank = (MasRank)criteria.uniqueResult();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		
		return masRank;
	}

	@Override
	public Map<String, List<MasRank>>  getAllRank(JSONObject jsonObj){
		Map<String, List<MasRank>> mapObj = new HashMap<String, List<MasRank>>();
		int pageSize = 5;
		int pageNo=1;
		
		List totalMatches = new ArrayList();
		 
		List<MasRank> masRankList = new ArrayList<MasRank>();
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasRank.class);		
			//System.out.println("jsonObj dao  :: " +jsonObj);		
			if( jsonObj.get("PN")!=null)
			 pageNo = Integer.parseInt(jsonObj.get("PN").toString());
			//System.out.println("pageNo  :: " +pageNo);		
			String rankName="";
				 if (jsonObj.has("rankName"))
				 {
					  rankName = "%"+jsonObj.get("rankName")+"%";
					  if(jsonObj.get("rankName").toString().length()>0 && !jsonObj.get("rankName").toString().trim().equalsIgnoreCase("")) {
							criteria.add(Restrictions.ilike("rankName", rankName));
						}
				 }	
				 
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("rankId").as("rankId"));
			projectionList.add(Projections.property("rankCode").as("rankCode"));
			projectionList.add(Projections.property("rankName").as("rankName"));
			projectionList.add(Projections.property("status").as("status"));
			projectionList.add(Projections.property("masEmployeeCategory").as("masEmployeeCategory"));
			criteria.addOrder(Order.asc("rankName"));
			
			totalMatches = criteria.list();
			//System.out.println("totalMatches :: "+totalMatches.size());
					
			criteria.setFirstResult((pageSize) * (pageNo - 1));
			criteria.setProjection(projectionList).setMaxResults(pageSize);
			masRankList = criteria.setResultTransformer(new AliasToBeanResultTransformer(MasRank.class)).list();
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			mapObj.put("masRankList", masRankList);
			mapObj.put("totalMatches", totalMatches);
			return mapObj;
		}
		
		
		


	@Override
	public List<MasRank> getRank(String rankName){
		Object[] status = new Object[] {"y"};
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		List<MasRank> masRankList = new ArrayList<MasRank>();
		Criteria criteria = session.createCriteria(MasRank.class);
		if(rankName.length()>0 && !rankName.trim().equalsIgnoreCase("")) {
			
			criteria.add(Restrictions.ilike("rankName", rankName));
		}
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("rankId").as("rankId"));
		projectionList.add(Projections.property("rankCode").as("rankCode"));
		projectionList.add(Projections.property("rankName").as("rankName"));
		projectionList.add(Projections.property("status").as("status"));
		
		criteria.setProjection(projectionList);
		
		masRankList = criteria.setResultTransformer(new AliasToBeanResultTransformer(MasRank.class)).list();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return masRankList;
	}

	@Override
	public String updateRank(Long rankId, String rankCode, String rankName, Long employeeCategoryId,Long userId) {	
		String result="";
		try {
			
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				MasRank masRank =  (MasRank)session.get(MasRank.class, rankId);
				
			
				
				if(masRank != null)
				{
				
				Transaction transaction = session.beginTransaction();
				masRank.setRankCode(rankCode);
				masRank.setRankName(rankName);
				//masRank.setStatus("y");
				masRank.getMasEmployeeCategory().getEmployeeCategoryName();
				Users usr = new Users();
				usr.setUserId(userId); // userId will be fetch from session
				//masRank.setLastChgBy(new Long(1));
				MasEmployeeCategory employeeCategory = new MasEmployeeCategory();
				employeeCategory.setEmployeeCategoryId(employeeCategoryId);
				
				masRank.setMasEmployeeCategory(employeeCategory);
				
				long d = System.currentTimeMillis();
				Timestamp date = new Timestamp(d);			
				
				session.update(masRank);
				
				transaction.commit();
				
				result="200";	
				}	
				else {
					session.update("msg","RankId dose not found");
				}
				
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
		
		
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		
		
		return result;
	}

	@Override
	public String updateRankStatus(Long rankId,String rankCode,String status,Long userId) {
		//Object[] status = new Object[] {"y"};
		String result = "";
		try {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		List<MasRank> masRankStatusList = new ArrayList<MasRank>();
		Criteria criteria = session.createCriteria(MasRank.class,"masrank");
		criteria.add(Restrictions.and(Restrictions.eq("rankId", rankId),Restrictions.eq("rankCode", rankCode),Restrictions.eq("masrank.status", status)));
		masRankStatusList = criteria.list();
			for(int i=0;i<masRankStatusList.size();i++) {
				Long rankId1 = masRankStatusList.get(i).getRankId();
				
				Object rankObject =  session.load(MasRank.class, rankId1);
				MasRank masRank = (MasRank)rankObject;
				
				Transaction transaction = session.beginTransaction();
								
				session.update(masRank);
				transaction.commit();
				result="200";
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public List<MasEmployeeCategory> getEmployeeCategoryList() {
		List<MasEmployeeCategory> employeeCategoryList = new ArrayList<MasEmployeeCategory>();
		try {	 
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria criteria =  session.createCriteria(MasEmployeeCategory.class);
		
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("employeeCategoryId").as("employeeCategoryId"));		
		projectionList.add(Projections.property("employeeCategoryName").as("employeeCategoryName"));
		criteria.setProjection(projectionList);
		
		employeeCategoryList = criteria.setResultTransformer(new AliasToBeanResultTransformer(MasEmployeeCategory.class)).add(Restrictions.eq("status", 'Y').ignoreCase()).list();
		
		}catch(Exception e) {
			e.printStackTrace();
		}
		return employeeCategoryList;
	}

	/***************************************MAS TRADE*************************************************************/

	@Override
	public List<MasTrade> validateMasTrade(String tradeName) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		List<MasTrade> tradeList = new ArrayList<MasTrade>();
		Criteria criteria = session.createCriteria(MasTrade.class);
		criteria.add(Restrictions.or(Restrictions.eq("tradeName", tradeName)));
		tradeList = criteria.list();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return tradeList;
	}

	@Override
	public List<MasTrade> validateMasTradeUpdate(String tradeName) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		List<MasTrade> tradeList = new ArrayList<MasTrade>();
		Criteria criteria = session.createCriteria(MasTrade.class);
		criteria.add(Restrictions.and(Restrictions.eq("tradeName", tradeName)));
		tradeList = criteria.list();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return tradeList;
	}

	@Override
	public String addMasTrade(MasTrade masTrade) {
		String result="";
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();		
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(masTrade);
			tx.commit();
			if(savedObj!=null) {
				result = "200";
			}else {
				result = "500";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public MasTrade checkTrade(String tradeName) {
		
		MasTrade masTrade = new MasTrade();	
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria criteria =  session.createCriteria(MasTrade.class);
		criteria.add(Restrictions.eq("tradeName", tradeName));
		masTrade = (MasTrade)criteria.uniqueResult();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		
		return masTrade;
	}

	@Override
	public Map<String, List<MasTrade>>  getAllTrade(JSONObject jsonObj){
		Map<String, List<MasTrade>> mapObj = new HashMap<String, List<MasTrade>>();
		int pageSize = 5;
		int pageNo=1;
		
		List totalMatches = new ArrayList();
		 
		List<MasTrade> masTradeList = new ArrayList<MasTrade>();
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasTrade.class);		
			//System.out.println("jsonObj dao  :: " +jsonObj);		
			if( jsonObj.get("PN")!=null)
			 pageNo = Integer.parseInt(jsonObj.get("PN").toString());
			//System.out.println("pageNo  :: " +pageNo);		
			String tradeName="";
				 if (jsonObj.has("tradeName"))
				 {
					 tradeName = "%"+jsonObj.get("tradeName")+"%";
					  if(jsonObj.get("tradeName").toString().length()>0 && !jsonObj.get("tradeName").toString().trim().equalsIgnoreCase("")) {
							criteria.add(Restrictions.ilike("tradeName", tradeName));
						}
				 }	
				 
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("tradeId").as("tradeId"));
			projectionList.add(Projections.property("tradeName").as("tradeName"));
			projectionList.add(Projections.property("status").as("status"));
			projectionList.add(Projections.property("masServiceType").as("masServiceType"));
			criteria.addOrder(Order.asc("tradeName"));
			
			totalMatches = criteria.list();
			//System.out.println("totalMatches :: "+totalMatches.size());
					
			criteria.setFirstResult((pageSize) * (pageNo - 1));
			criteria.setProjection(projectionList).setMaxResults(pageSize);
			masTradeList = criteria.setResultTransformer(new AliasToBeanResultTransformer(MasTrade.class)).list();
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			mapObj.put("masTradeList", masTradeList);
			mapObj.put("totalMatches", totalMatches);
			return mapObj;
		}
		
	@Override
	public List<MasTrade> getTrade(String tradeName){
		Object[] status = new Object[] {"y"};
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		List<MasTrade> masTradeList = new ArrayList<MasTrade>();
		Criteria criteria = session.createCriteria(MasTrade.class);
		if(tradeName.length()>0 && !tradeName.trim().equalsIgnoreCase("")) {
			
			criteria.add(Restrictions.ilike("tradeName", tradeName));
		}
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("tradeId").as("tradeId"));
		projectionList.add(Projections.property("tradeName").as("tradeName"));
		projectionList.add(Projections.property("status").as("status"));
		
		criteria.setProjection(projectionList);
		
		masTradeList = criteria.setResultTransformer(new AliasToBeanResultTransformer(MasTrade.class)).list();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return masTradeList;
	}

	@Override
	public String updateTrade(Long tradeId, String tradeName, Long serviceTypeId,Long userId) {	
		String result="";
		try {
			
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				MasTrade masTrade =  (MasTrade)session.get(MasTrade.class, tradeId);
				
			
				
				if(masTrade != null)
				{
				
				Transaction transaction = session.beginTransaction();
				masTrade.setTradeName(tradeName);
				Users usr = new Users();
				usr.setUserId(userId); // userId will be fetch from session
				MasServiceType masServiceType = new MasServiceType();
				masServiceType.setServiceTypeId(serviceTypeId);
				
				
				long d = System.currentTimeMillis();
				Timestamp date = new Timestamp(d);
				
				session.update(masTrade);
				
				transaction.commit();
				
				result="200";	
				}	
				else {
					session.update("msg","TradeId dose not found");
				}
				
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
		
		
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		
		
		return result;
	}

	@Override
	public String updateTradeStatus(Long tradeId,String tradeName,String status,Long userId) {
		//Object[] status = new Object[] {"y"};
		String result = "";
		try {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		List<MasTrade> masTradeStatusList = new ArrayList<MasTrade>();
		Criteria criteria = session.createCriteria(MasTrade.class,"masTrade");
		criteria.add(Restrictions.and(Restrictions.eq("tradeId", tradeId),Restrictions.eq("tradeName", tradeName),Restrictions.eq("masTrade.status", status)));
		//criteria.add(Restrictions.in("mascommand.status", status));
		masTradeStatusList = criteria.list();
			for(int i=0;i<masTradeStatusList.size();i++) {
				Long tradeId1 = masTradeStatusList.get(i).getTradeId();
				
				Object tradeObject =  session.load(MasTrade.class, tradeId1);
				MasTrade masTrade = (MasTrade)tradeObject;
				
				Transaction transaction = session.beginTransaction();
								
				session.update(masTrade);
				transaction.commit();
				result="200";
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public List<MasServiceType> getServiceTypeList() {
		List<MasServiceType> serviceTypeList = new ArrayList<MasServiceType>();
		try {	 
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria criteria =  session.createCriteria(MasServiceType.class);
		
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("serviceTypeId").as("serviceTypeId"));		
		projectionList.add(Projections.property("serviceTypeName").as("serviceTypeName"));
		criteria.setProjection(projectionList);
		
		serviceTypeList = criteria.setResultTransformer(new AliasToBeanResultTransformer(MasServiceType.class)).add(Restrictions.eq("status", 'Y').ignoreCase()).addOrder(Order.asc("serviceTypeName")).list();
		
		}catch(Exception e) {
			e.printStackTrace();
		}
		return serviceTypeList;
	}

	/****************************MAS RELIGION*********************************************************/
	@Override
	public Map<String, List<MasReligion>> getAllReligion(JSONObject jsondata) {
		Map<String, List<MasReligion>> map = new HashMap<String, List<MasReligion>>();
		List<MasReligion> religionList = new ArrayList<MasReligion>();
		int pageNo=1;
		int pageSize = 5;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasReligion.class);
			
					
			if( jsondata.get("PN")!=null)
			 pageNo = Integer.parseInt(jsondata.get("PN").toString());
					
			String rgName="";
				 if (jsondata.has("religionName"))
				 {
					  rgName = "%"+jsondata.get("religionName")+"%";
					  if(jsondata.get("religionName").toString().length()>0 && !jsondata.get("religionName").toString().trim().equalsIgnoreCase("")) {
							criteria.add(Restrictions.ilike("religionName", rgName));
							//criteria.addOrder(Order.asc(jsondata.get("religionName").toString()));
						}
				 }
				 List totalMatches = criteria.list();
				 
				 criteria.setFirstResult((pageSize) * (pageNo - 1));
				 criteria.setMaxResults(pageSize);
				 religionList = criteria.list();
			
			
		map.put("religionList", religionList);
		map.put("totalMatches", totalMatches);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@Override
	public List<MasReligion> validateReligion(String religionCode, String religionName) {
		List<MasReligion> reliList =  new ArrayList<MasReligion>();	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria = session.createCriteria(MasReligion.class);
				criteria.add(Restrictions.or(Restrictions.eq("religionCode", religionCode), Restrictions.eq("religionName", religionName)));
				reliList = criteria.list();
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		return reliList;
	}

	@Override
	public List<MasReligion> validateReligionUpdate(String religionCode, String religionName) {
		List<MasReligion> reliList =  new ArrayList<MasReligion>();	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria = session.createCriteria(MasReligion.class);
				criteria.add(Restrictions.and(Restrictions.eq("religionCode", religionCode), Restrictions.eq("religionName", religionName)));
				reliList = criteria.list();
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		return reliList;
	}

	@Override
	public String addReligion(MasReligion masReligion) {
		String result="";	
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(masReligion);
			tx.commit();
			if(savedObj!=null) {
				result="200";
			}else {
				result = "500";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		
		return result;
	}

	@Override
	public String updateReligionDetails(Long religionId, String religionCode, String religionName, Long userId) {
		String result="";
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
					Object object = session.load(MasReligion.class, religionId);
					MasReligion masReligion = (MasReligion)object;
					
					Transaction transaction = session.beginTransaction();
					masReligion.setReligionCode(religionCode.toUpperCase());
					masReligion.setReligionName(religionName.toUpperCase());			
									
					Users users = new Users();
					users.setUserId(userId);
					masReligion.setUser(users);				
					long d = System.currentTimeMillis();
					Timestamp date = new Timestamp(d);
					masReligion.setLastChgDate(date);
					session.update(masReligion);
					transaction.commit();
					
					result = "200";
				//}
			//}
											
		}catch(Exception e) {
			
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public MasReligion checkReligion(String religionCode) {
		MasReligion mReligion = new MasReligion();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasReligion.class);		
			criteria.add(Restrictions.eq("religionCode", religionCode));
			mReligion = (MasReligion)criteria.uniqueResult();
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mReligion;
	}

	@Override
	public String updateReligionStatus(Long religionId, String religionCode, String status, Long userId) {
		String result = "";	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Object object =  session.load(MasReligion.class, religionId);
				
				MasReligion masReligion = (MasReligion)object;
				Transaction transaction = session.beginTransaction();
				
				
				if(masReligion.getStatus().equalsIgnoreCase("Y") || masReligion.getStatus().equalsIgnoreCase("y")) {
					masReligion.setStatus("N");
				}else if(masReligion.getStatus().equalsIgnoreCase("N") || masReligion.getStatus().equalsIgnoreCase("n")) {
					masReligion.setStatus("Y");
				}else {
					masReligion.setStatus("Y");
				}
				session.update(masReligion);
				transaction.commit();
				result = "200";
			//}
			
		}catch(Exception e) {
			
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	/****************************MAS Identification*********************************************************/
	@Override
	public Map<String, List<MasIdentificationType>> getAllIdentification(JSONObject jsonObject) {
		Map<String, List<MasIdentificationType>> map = new HashMap<String, List<MasIdentificationType>>();
		List<MasIdentificationType> identificationList = new ArrayList<MasIdentificationType>();
		int pageNo=1;
		int pageSize = 5;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasIdentificationType.class);
			
					
			if( jsonObject.get("PN")!=null)
			 pageNo = Integer.parseInt(jsonObject.get("PN").toString());
					
			String msName="";
				 if (jsonObject.has("identificationName"))
				 {
					  msName = "%"+jsonObject.get("identificationName")+"%";
					  if(jsonObject.get("identificationName").toString().length()>0 && !jsonObject.get("identificationName").toString().trim().equalsIgnoreCase("")) {
							criteria.add(Restrictions.ilike("identificationName", msName));
							criteria.addOrder(Order.asc("identificationName"));
						}
				 }
				 List totalMatches = criteria.list();
				 
				 criteria.setFirstResult((pageSize) * (pageNo - 1));
				 criteria.setMaxResults(pageSize);
				 identificationList = criteria.list();
			
			
		map.put("identificationList", identificationList);
		map.put("totalMatches", totalMatches);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@Override
	public List<MasIdentificationType> validateIdentification(String identificationCode, String identificationName) {
		List<MasIdentificationType> marsList =  new ArrayList<MasIdentificationType>();	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria = session.createCriteria(MasIdentificationType.class);
				criteria.add(Restrictions.or(Restrictions.eq("identificationCode", identificationCode), Restrictions.eq("identificationName", identificationName)));
				marsList = criteria.list();
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		return marsList;
	}

	@Override
	public List<MasIdentificationType> validateIdentificationUpdate(String identificationName) {
		List<MasIdentificationType> marsList =  new ArrayList<MasIdentificationType>();	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria = session.createCriteria(MasIdentificationType.class);
				criteria.add(Restrictions.eq("identificationName", identificationName));
				marsList = criteria.list();
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		return marsList;
	}

	@Override
	public String addIdentification(MasIdentificationType masIdentification) {
		String result="";	
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(masIdentification);
			tx.commit();
			
			if(savedObj!=null) {
				result="200";
			}else {
				result = "500";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		
		return result;
	}

	@Override
	public String updateIdentification(Long identificationTypeId, String identificationCode, String identificationName,Long userId) {
		String result="";
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
					Object object = session.load(MasIdentificationType.class, identificationTypeId);
					MasIdentificationType masIdentification = (MasIdentificationType)object;
					
					Transaction transaction = session.beginTransaction();
					masIdentification.setIdentificationCode(identificationCode.toUpperCase());
					masIdentification.setIdentificationName(identificationName.toUpperCase());			
									
					Users users = new Users();
					users.setUserId(userId);
					masIdentification.setLastChgBy(userId);				
					long d = System.currentTimeMillis();
					Timestamp date = new Timestamp(d);
					masIdentification.setLastChgDate(date);
					session.update(masIdentification);
					transaction.commit();
					
					result = "200";
				//}
			//}
											
		}catch(Exception e) {
			
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public MasIdentificationType checkIdentification(String identificationCode) {
		MasIdentificationType mIdentification = new MasIdentificationType();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasIdentificationType.class);		
			criteria.add(Restrictions.eq("identificationCode", identificationCode));
			mIdentification = (MasIdentificationType)criteria.uniqueResult();
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mIdentification;
	}

	@Override
	public String updateIdentificationStatus(Long identificationTypeId, String identificationCode, String status,Long userId) {
		String result = "";	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Object object =  session.load(MasIdentificationType.class, identificationTypeId);
				
				MasIdentificationType masIdentification = (MasIdentificationType)object;
				Transaction transaction = session.beginTransaction();
				
				
				if(masIdentification.getStatus().equalsIgnoreCase("Y") || masIdentification.getStatus().equalsIgnoreCase("y")) {
					masIdentification.setStatus("N");
				}else if(masIdentification.getStatus().equalsIgnoreCase("N") || masIdentification.getStatus().equalsIgnoreCase("n")) {
					masIdentification.setStatus("Y");
				}else {
					masIdentification.setStatus("Y");
				}
				session.update(masIdentification);
				transaction.commit();
				result = "200";
			//}
			
		}catch(Exception e) {
			
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	/****************************MAS EMPLOYEE CATEGORY*********************************************************/
	@Override
	public Map<String, List<MasEmployeeCategory>> getAllEmployeeCategory(JSONObject jsondata) {
		Map<String, List<MasEmployeeCategory>> map = new HashMap<String, List<MasEmployeeCategory>>();
		List<MasEmployeeCategory> employeeCategoryList = new ArrayList<MasEmployeeCategory>();
		int pageNo=1;
		int pageSize = 5;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasEmployeeCategory.class);
			
					
			if( jsondata.get("PN")!=null)
			 pageNo = Integer.parseInt(jsondata.get("PN").toString());
					
			String ecName="";
				 if (jsondata.has("employeeCategoryName"))
				 {
					  ecName = "%"+jsondata.get("employeeCategoryName")+"%";
					  if(jsondata.get("employeeCategoryName").toString().length()>0 && !jsondata.get("employeeCategoryName").toString().trim().equalsIgnoreCase("")) {
							criteria.add(Restrictions.ilike("employeeCategoryName", ecName));
							//criteria.addOrder(Order.asc(jsondata.get("employeeCategoryName").toString()));
						}
				 }
				 List totalMatches = criteria.list();
				 
				 criteria.setFirstResult((pageSize) * (pageNo - 1));
				 criteria.setMaxResults(pageSize);
				 employeeCategoryList = criteria.list();
			
			
		map.put("employeeCategoryList", employeeCategoryList);
		map.put("totalMatches", totalMatches);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@Override
	public List<MasEmployeeCategory> validateEmployeeCategory(String employeeCategoryCode, String employeeCategoryName) {
		List<MasEmployeeCategory> ecList =  new ArrayList<MasEmployeeCategory>();	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria = session.createCriteria(MasEmployeeCategory.class);
				criteria.add(Restrictions.or(Restrictions.eq("employeeCategoryCode", employeeCategoryCode), Restrictions.eq("employeeCategoryName", employeeCategoryName)));
				ecList = criteria.list();
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		return ecList;
	}

	@Override
	public List<MasEmployeeCategory> validateEmployeeCategoryUpdate(String employeeCategoryCode, String employeeCategoryName) {
		List<MasEmployeeCategory> ecList =  new ArrayList<MasEmployeeCategory>();	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria = session.createCriteria(MasEmployeeCategory.class);
				criteria.add(Restrictions.and(Restrictions.eq("employeeCategoryCode", employeeCategoryCode), Restrictions.eq("employeeCategoryName", employeeCategoryName)));
				ecList = criteria.list();
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		return ecList;
	}

	@Override
	public String addEmployeeCategory(MasEmployeeCategory masEmployeeCategory) {
		String result="";
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(masEmployeeCategory);
			tx.commit();
			if(savedObj!=null) {
				result="200";
			}else {
				result = "500";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		
		return result;
	}

	@Override
	public String updateEmployeeCategoryDetails(Long employeeCategoryId, String employeeCategoryCode, String employeeCategoryName,Long userId) {
		String result="";
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
					
					Object object = session.load(MasEmployeeCategory.class, employeeCategoryId);
					MasEmployeeCategory masEmployeeCategory = (MasEmployeeCategory)object;
					
					Transaction transaction = session.beginTransaction();
					masEmployeeCategory.setEmployeeCategoryCode(employeeCategoryCode.toString().toUpperCase());
					masEmployeeCategory.setEmployeeCategoryName(employeeCategoryName.toUpperCase());			
									
					Users users = new Users();
					users.setUserId(userId);				
					long d = System.currentTimeMillis();
					Timestamp date = new Timestamp(d);
					//masEmployeeCategory.setLastChgDate(date);
					session.update(masEmployeeCategory);
					transaction.commit();
					
					result = "200";
				//}
			//}
											
		}catch(Exception e) {
			
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public MasEmployeeCategory checkEmployeeCategory(String employeeCategoryCode) {
		MasEmployeeCategory mEmployeeCategory = new MasEmployeeCategory();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasEmployeeCategory.class);		
			criteria.add(Restrictions.eq("employeeCategoryCode", employeeCategoryCode));
			mEmployeeCategory = (MasEmployeeCategory)criteria.uniqueResult();
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mEmployeeCategory;
	}

	@Override
	public String updateEmployeeCategoryStatus(Long employeeCategoryId, String employeeCategoryCode, String status,Long userId) {
		String result = "";	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Object object =  session.load(MasEmployeeCategory.class, employeeCategoryId);
				
				MasEmployeeCategory masEmployeeCategory = (MasEmployeeCategory)object;
				Transaction transaction = session.beginTransaction();
				session.update(masEmployeeCategory);
				transaction.commit();
				result = "200";
			
			
		}catch(Exception e) {
			
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	/****************************MAS Administrative Sex*********************************************************/
	@Override
	public Map<String, List<MasAdministrativeSex>> getAllAdministrativeSex(JSONObject jsondata) {
		Map<String, List<MasAdministrativeSex>> map = new HashMap<String, List<MasAdministrativeSex>>();
		List<MasAdministrativeSex> administrativeSexList = new ArrayList<MasAdministrativeSex>();
		int pageNo=1;
		int pageSize = 5;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasAdministrativeSex.class);
			
					
			if( jsondata.get("PN")!=null)
			 pageNo = Integer.parseInt(jsondata.get("PN").toString());
					
			String asName="";
				 if (jsondata.has("administrativeSexName"))
				 {
					  asName = "%"+jsondata.get("administrativeSexName")+"%";
					  if(jsondata.get("administrativeSexName").toString().length()>0 && !jsondata.get("administrativeSexName").toString().trim().equalsIgnoreCase("")) {
							criteria.add(Restrictions.ilike("administrativeSexName", asName));
						}
				 }
				 
				 List totalMatches = criteria.list();
				 
				 criteria.setFirstResult((pageSize) * (pageNo - 1));
				 criteria.setMaxResults(pageSize);
				 administrativeSexList = criteria.list();
			
			
		map.put("administrativeSexList", administrativeSexList);
		map.put("totalMatches", totalMatches);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@Override
	public List<MasAdministrativeSex> validateAdministrativeSex(String administrativeSexCode, String administrativeSexName) {
		List<MasAdministrativeSex> ecList =  new ArrayList<MasAdministrativeSex>();	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria = session.createCriteria(MasAdministrativeSex.class);
				criteria.add(Restrictions.or(Restrictions.eq("administrativeSexCode", administrativeSexCode), Restrictions.eq("administrativeSexName", administrativeSexName)));
				ecList = criteria.list();
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		return ecList;
	}

	@Override
	public List<MasAdministrativeSex> validateAdministrativeSexUpdate(String administrativeSexCode, String administrativeSexName) {
		List<MasAdministrativeSex> ecList =  new ArrayList<MasAdministrativeSex>();	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria = session.createCriteria(MasAdministrativeSex.class);
				criteria.add(Restrictions.and(Restrictions.eq("administrativeSexCode", administrativeSexCode), Restrictions.eq("administrativeSexName", administrativeSexName)));
				ecList = criteria.list();
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		return ecList;
	}

	@Override
	public String addAdministrativeSex(MasAdministrativeSex masAdministrativeSex) {
		String result="";	
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(masAdministrativeSex);
			tx.commit();
			if(savedObj!=null) {
				result="200";
			}else {
				result = "500";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		
		return result;
	}

	@Override
	public String updateAdministrativeSexDetails(Long administrativeSexId, String administrativeSexCode, String administrativeSexName, Long userId) {
		String result="";
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
					Object object = session.load(MasAdministrativeSex.class, administrativeSexId);
					MasAdministrativeSex masAdministrativeSex = (MasAdministrativeSex)object;
					
					Transaction transaction = session.beginTransaction();
					masAdministrativeSex.setAdministrativeSexCode(administrativeSexCode.toUpperCase());
					masAdministrativeSex.setAdministrativeSexName(administrativeSexName.toUpperCase());			
									
					Users user = new Users();
					user.setUserId(userId);
					masAdministrativeSex.setUser(user);				
					long d = System.currentTimeMillis();
					Timestamp date = new Timestamp(d);
					masAdministrativeSex.setLastChgDate(date);
					session.update(masAdministrativeSex);
					transaction.commit();
					
					result = "200";
				//}
			//}
											
		}catch(Exception e) {
			
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public MasAdministrativeSex checkAdministrativeSex(String administrativeSexCode) {
		MasAdministrativeSex mAdministrativeSex = new MasAdministrativeSex();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasAdministrativeSex.class);		
			criteria.add(Restrictions.eq("administrativeSexCode", administrativeSexCode));
			mAdministrativeSex = (MasAdministrativeSex)criteria.uniqueResult();
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mAdministrativeSex;
	}

	@Override
	public String updateAdministrativeSexStatus(Long administrativeSexId, String administrativeSexCode, String status, Long userId) {
		String result = "";	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				
				Object object =  session.load(MasAdministrativeSex.class, administrativeSexId);
				
				MasAdministrativeSex masAdministrativeSex = (MasAdministrativeSex)object;
				Transaction transaction = session.beginTransaction();
			
			  if(masAdministrativeSex.getStatus().equalsIgnoreCase("Y") || masAdministrativeSex.getStatus().equalsIgnoreCase("y")) {
				  masAdministrativeSex.setStatus("N"); 
			  }else if(masAdministrativeSex.getStatus().equalsIgnoreCase("N") || masAdministrativeSex.getStatus().equalsIgnoreCase("n")) {
				  masAdministrativeSex.setStatus("Y"); 
			  }else {
				  masAdministrativeSex.setStatus("Y"); 
			  }
			 
				session.update(masAdministrativeSex);
				transaction.commit();
				result = "200";
			//}
			
		}catch(Exception e) {
			
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	/****************************MAS MedicalCategory*********************************************************/
	@Override
	public Map<String, List<MasMedicalCategory>> getAllMedicalCategory(JSONObject jsondata) {
		Map<String, List<MasMedicalCategory>> map = new HashMap<String, List<MasMedicalCategory>>();
		List<MasMedicalCategory> medicalCategoryList = new ArrayList<MasMedicalCategory>();
		int pageNo=1;
		int pageSize = 5;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasMedicalCategory.class);
			List totalMatches = new ArrayList<MasMedicalCategory>();
					
			if( !jsondata.get("PN").equals("0")) {
			 pageNo = Integer.parseInt(jsondata.get("PN").toString());
					
			String mCatName="";
				 if (jsondata.has("medicalCategoryName"))
				 {
					  mCatName = "%"+jsondata.get("medicalCategoryName")+"%";
					  if(jsondata.get("medicalCategoryName").toString().length()>0 && !jsondata.get("medicalCategoryName").toString().trim().equalsIgnoreCase("")) {
							criteria.add(Restrictions.ilike("medicalCategoryName", mCatName));
							
						}
				 }
				criteria.addOrder(Order.asc("medicalCategoryName"));
				 totalMatches = criteria.list();
				 
				 criteria.setFirstResult((pageSize) * (pageNo - 1));
				 criteria.setMaxResults(pageSize);
				 medicalCategoryList = criteria.list();
			
			}
			if(jsondata.get("PN").equals("0"))
			{
				medicalCategoryList = criteria.list();
				totalMatches = criteria.list();
			}
		map.put("medicalCategoryList", medicalCategoryList);
		map.put("totalMatches", totalMatches);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@Override
	public List<MasMedicalCategory> validateMedicalCategory(Long medicalCategoryCode, String medicalCategoryName) {
		List<MasMedicalCategory> mCatList =  new ArrayList<MasMedicalCategory>();	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria = session.createCriteria(MasMedicalCategory.class);
				criteria.add(Restrictions.or(Restrictions.eq("medicalCategoryCode", medicalCategoryCode), Restrictions.eq("medicalCategoryName", medicalCategoryName)));
				mCatList = criteria.list();
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		return mCatList;
	}
	
	
	@Override
	public List<MasMedicalCategory> validateFitFlag() {
		List<MasMedicalCategory> fitFlagList =  new ArrayList<MasMedicalCategory>();	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria = session.createCriteria(MasMedicalCategory.class);
				criteria.add(Restrictions.eq("fitFlag", "F"));
				fitFlagList = criteria.list();
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		return fitFlagList;
	}

	@Override
	public List<MasMedicalCategory> validateMedicalCategoryUpdate(Long medicalCategoryCode, String medicalCategoryName) {
		List<MasMedicalCategory> mCatList =  new ArrayList<MasMedicalCategory>();	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria = session.createCriteria(MasMedicalCategory.class);
				criteria.add(Restrictions.and(Restrictions.eq("medicalCategoryCode", medicalCategoryCode), Restrictions.eq("medicalCategoryName", medicalCategoryName)));
				mCatList = criteria.list();
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		return mCatList;
	}

	@Override
	public String addMedicalCategory(MasMedicalCategory masMedicalCategory) {
		String result="";
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(masMedicalCategory);
			tx.commit();         
			if(savedObj!=null) {
				result="200";
			}else {
				result = "500";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		
		return result;
	}

	@Override
	public String updateMedicalCategoryDetails(Long medicalCategoryId, Long medicalCategoryCode, String medicalCategoryName,Long userId,String fitFlag) {
		String result="";
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
					
					Object object = session.load(MasMedicalCategory.class, medicalCategoryId);
					MasMedicalCategory masMedicalCategory = (MasMedicalCategory)object;
					
					Transaction transaction = session.beginTransaction();
					masMedicalCategory.setMedicalCategoryCode(medicalCategoryCode);
					masMedicalCategory.setMedicalCategoryName(medicalCategoryName.toUpperCase());			
					masMedicalCategory.setFitFlag(fitFlag);				
					Users user = new Users();
					user.setUserId(userId);
					masMedicalCategory.setUser(user);				
					long d = System.currentTimeMillis();
					Timestamp date = new Timestamp(d);
					masMedicalCategory.setLastChgDate(date);
					session.update(masMedicalCategory);
					transaction.commit();
					
					result = "200";
				//}
			//}
											
		}catch(Exception e) {
			
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public MasMedicalCategory checkMedicalCategory(Long medicalCategoryCode) {
		MasMedicalCategory mMedicalCategory = new MasMedicalCategory();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasMedicalCategory.class);		
			criteria.add(Restrictions.eq("medicalCategoryCode", medicalCategoryCode));
			mMedicalCategory = (MasMedicalCategory)criteria.uniqueResult();
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mMedicalCategory;
	}

	@Override
	public String updateMedicalCategoryStatus(Long medicalCategoryId, Long medicalCategoryCode, String status,Long userId) {
		String result = "";	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Object object =  session.load(MasMedicalCategory.class, medicalCategoryId);
				
				MasMedicalCategory masMedicalCategory = (MasMedicalCategory)object;
				Transaction transaction = session.beginTransaction();
				
				
				if(masMedicalCategory.getStatus().equalsIgnoreCase("Y") || masMedicalCategory.getStatus().equalsIgnoreCase("y")) {
					masMedicalCategory.setStatus("N");
				}else if(masMedicalCategory.getStatus().equalsIgnoreCase("N") || masMedicalCategory.getStatus().equalsIgnoreCase("n")) {
					masMedicalCategory.setStatus("Y");
				}else {
					masMedicalCategory.setStatus("Y");
				}
				session.update(masMedicalCategory);
				transaction.commit();
				result = "200";
			//}
			
		}catch(Exception e) {
			
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	/****************************MAS Blood Group*********************************************************/
	@Override
	public Map<String, List<MasBloodGroup>> getAllBloodGroup(JSONObject jsondata) {
		Map<String, List<MasBloodGroup>> map = new HashMap<String, List<MasBloodGroup>>();
		List<MasBloodGroup> bloodGroupList = new ArrayList<MasBloodGroup>();
		int pageNo=1;
		int pageSize = 5;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasBloodGroup.class);
			
					
			if( jsondata.get("PN")!=null)
			 pageNo = Integer.parseInt(jsondata.get("PN").toString());
					
			String bgName="";
				 if (jsondata.has("bloodGroupName"))
				 {
					  bgName = "%"+jsondata.get("bloodGroupName")+"%";
					  if(jsondata.get("bloodGroupName").toString().length()>0 && !jsondata.get("bloodGroupName").toString().trim().equalsIgnoreCase("")) {
							criteria.add(Restrictions.ilike("bloodGroupName", bgName));
							
						}
				 }
				criteria.addOrder(Order.asc("bloodGroupName"));
				 List totalMatches = criteria.list();
				 
				 criteria.setFirstResult((pageSize) * (pageNo - 1));
				 criteria.setMaxResults(pageSize);
				 bloodGroupList = criteria.list();
			
			
		map.put("bloodGroupList", bloodGroupList);
		map.put("totalMatches", totalMatches);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@Override
	public List<MasBloodGroup> validateBloodGroup(Long bloodGroupCode, String bloodGroupName) {
		List<MasBloodGroup> ecList =  new ArrayList<MasBloodGroup>();	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria = session.createCriteria(MasBloodGroup.class);
				criteria.add(Restrictions.or(Restrictions.eq("bloodGroupCode", bloodGroupCode), Restrictions.eq("bloodGroupName", bloodGroupName)));
				ecList = criteria.list();
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		return ecList;
	}

	@Override
	public List<MasBloodGroup> validateBloodGroupUpdate(Long bloodGroupCode, String bloodGroupName) {
		List<MasBloodGroup> ecList =  new ArrayList<MasBloodGroup>();	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria = session.createCriteria(MasBloodGroup.class);
				criteria.add(Restrictions.eq("bloodGroupName", bloodGroupName));
				ecList = criteria.list();
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		return ecList;
	}

	@Override
	public String addBloodGroup(MasBloodGroup masBloodGroup) {
		String result="";
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(masBloodGroup);
			tx.commit();
			if(savedObj!=null) {
				result="200";
			}else {
				result = "500";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		
		return result;
	}

	@Override
	public String updateBloodGroupDetails(Long bloodGroupId, Long bloodGroupCode, String bloodGroupName,Long userId) {
		String result="";
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
					Object object = session.load(MasBloodGroup.class, bloodGroupId);
					MasBloodGroup masBloodGroup = (MasBloodGroup)object;
					
					Transaction transaction = session.beginTransaction();
					masBloodGroup.setBloodGroupCode(bloodGroupCode);
					masBloodGroup.setBloodGroupName(bloodGroupName.toUpperCase());			
									
					Users user = new Users();
					user.setUserId(userId);
					masBloodGroup.setUser(user);				
					long d = System.currentTimeMillis();
					Timestamp date = new Timestamp(d);
					masBloodGroup.setLastChgDate(date);
					session.update(masBloodGroup);
					transaction.commit();
					
					result = "200";
				//}
			//}
											
		}catch(Exception e) {
			
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public MasBloodGroup checkBloodGroup(String bloodGroupCode) {
		MasBloodGroup mBloodGroup = new MasBloodGroup();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasBloodGroup.class);		
			criteria.add(Restrictions.eq("bloodGroupCode", bloodGroupCode));
			mBloodGroup = (MasBloodGroup)criteria.uniqueResult();
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mBloodGroup;
	}

	@Override
	public String updateBloodGroupStatus(Long bloodGroupId, Long bloodGroupCode, String status,Long userId) {
		String result = "";	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Object object =  session.load(MasBloodGroup.class, bloodGroupId);
				
				MasBloodGroup masBloodGroup = (MasBloodGroup)object;
				Transaction transaction = session.beginTransaction();
				
				
				if(masBloodGroup.getStatus().equalsIgnoreCase("Y") || masBloodGroup.getStatus().equalsIgnoreCase("y")) {
					masBloodGroup.setStatus("N");
				}else if(masBloodGroup.getStatus().equalsIgnoreCase("N") || masBloodGroup.getStatus().equalsIgnoreCase("n")) {
					masBloodGroup.setStatus("Y");
				}else {
					masBloodGroup.setStatus("Y");
				}
				session.update(masBloodGroup);
				transaction.commit();
				result = "200";
			//}
			
		}catch(Exception e) {
			
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}


	/****************************MAS Sample*********************************************************/
	@Override
	public Map<String, List<MasSample>> getAllSample(JSONObject jsondata) {
		Map<String, List<MasSample>> map = new HashMap<String, List<MasSample>>();
		List<MasSample> sampleList = new ArrayList<MasSample>();
		int pageNo=1;
		int pageSize = 5;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasSample.class);
			
					
			if( jsondata.get("PN")!=null)
			 pageNo = Integer.parseInt(jsondata.get("PN").toString());
					
			String sName="";
				 if (jsondata.has("sampleDescription"))
				 {
					  sName = "%"+jsondata.get("sampleDescription")+"%";
					  if(jsondata.get("sampleDescription").toString().length()>0 && !jsondata.get("sampleDescription").toString().trim().equalsIgnoreCase("")) {
							criteria.add(Restrictions.ilike("sampleDescription", sName));
							
						}
				 }
				criteria.addOrder(Order.asc("sampleDescription"));
				 List totalMatches = criteria.list();
				 
				 criteria.setFirstResult((pageSize) * (pageNo - 1));
				 criteria.setMaxResults(pageSize);
				 sampleList = criteria.list();
			
			
		map.put("sampleList", sampleList);
		map.put("totalMatches", totalMatches);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@Override
	public List<MasSample> validateSample(String sampleCode, String sampleDescription) {
		List<MasSample> ecList =  new ArrayList<MasSample>();	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria = session.createCriteria(MasSample.class);
				criteria.add(Restrictions.or(Restrictions.eq("sampleCode", sampleCode), Restrictions.eq("sampleDescription", sampleDescription)));
				ecList = criteria.list();
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		return ecList;
	}

	@Override
	public List<MasSample> validateSampleUpdate(String sampleDescription) {
		List<MasSample> ecList =  new ArrayList<MasSample>();	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria = session.createCriteria(MasSample.class);
				criteria.add(Restrictions.eq("sampleDescription", sampleDescription));
				ecList = criteria.list();
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		return ecList;
	}

	@Override
	public String addSample(MasSample masSample) {
		String result="";
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(masSample);
			tx.commit();
			
			if(savedObj!=null) {
				result="200";
			}else {
				result = "500";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		
		return result;
	}

	@Override
	public String updateSampleDetails(Long sampleId, String sampleCode, String sampleDescription,Long userId) {
		String result="";
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
					Object object = session.load(MasSample.class, sampleId);
					MasSample masSample = (MasSample)object;
					
					Transaction transaction = session.beginTransaction();
					masSample.setSampleCode(sampleCode.toUpperCase());
					masSample.setSampleDescription(sampleDescription.toUpperCase());			
									
					Users user = new Users();
					user.setUserId(userId);
					masSample.setUser(user);			
					long d = System.currentTimeMillis();
					Timestamp date = new Timestamp(d);
					masSample.setLastChgDate(date);
					session.update(masSample);
					transaction.commit();
					
					result = "200";
				//}
			//}
											
		}catch(Exception e) {
			
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public MasSample checkSample(String sampleCode) {
		MasSample mSample = new MasSample();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasSample.class);		
			criteria.add(Restrictions.eq("sampleCode", sampleCode));
			mSample = (MasSample)criteria.uniqueResult();
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mSample;
	}

	@Override
	public String updateSampleStatus(Long sampleId, String sampleCode, String status,Long userId) {
		String result = "";	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				
				Object object =  session.load(MasSample.class, sampleId);
				
				MasSample masSample = (MasSample)object;
				Transaction transaction = session.beginTransaction();
				
				
				if(masSample.getStatus().equalsIgnoreCase("Y")) {
					masSample.setStatus("n");
				}else if(masSample.getStatus().equalsIgnoreCase("N")) {
					masSample.setStatus("y");
				}else {
					masSample.setStatus("y");
				}
				session.update(masSample);
				transaction.commit();
				session.flush();
				result = "200";
			//}
			
		}catch(Exception e) {
			
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	/****************************MAS UOM*********************************************************/
	@Override
	public Map<String, List<MasUOM>> getAllUOM(JSONObject jsondata) {
		Map<String, List<MasUOM>> map = new HashMap<String, List<MasUOM>>();
		List<MasUOM> UOMList = new ArrayList<MasUOM>();
		int pageNo=1;
		int pageSize = 5;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasUOM.class);
			
					
			if( jsondata.get("PN")!=null)
			 pageNo = Integer.parseInt(jsondata.get("PN").toString());
					
			String uName="";
				 if (jsondata.has("UOMName"))
				 {
					  uName = "%"+jsondata.get("UOMName")+"%";
					  if(jsondata.get("UOMName").toString().length()>0 && !jsondata.get("UOMName").toString().trim().equalsIgnoreCase("")) {
							criteria.add(Restrictions.ilike("UOMName", uName));
							
						}
				 }
				criteria.addOrder(Order.asc("UOMName"));
				 List totalMatches = criteria.list();
				 
				 criteria.setFirstResult((pageSize) * (pageNo - 1));
				 criteria.setMaxResults(pageSize);
				 UOMList = criteria.list();
			
			
		map.put("UOMList", UOMList);
		map.put("totalMatches", totalMatches);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@Override
	public List<MasUOM> validateUOM(String UOMCode, String UOMName) {
		List<MasUOM> UOMList =  new ArrayList<MasUOM>();	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria = session.createCriteria(MasUOM.class);
				criteria.add(Restrictions.or(Restrictions.eq("UOMCode", UOMCode), Restrictions.eq("UOMName", UOMName)));
				UOMList = criteria.list();
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		return UOMList;
	}

	@Override
	public List<MasUOM> validateUOMUpdate(String UOMCode, String UOMName) {
		List<MasUOM> UOMList =  new ArrayList<MasUOM>();	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria = session.createCriteria(MasUOM.class);
				criteria.add(Restrictions.and(Restrictions.eq("UOMCode", UOMCode), Restrictions.eq("UOMName", UOMName)));
				UOMList = criteria.list();
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		return UOMList;
	}

	@Override
	public String addUOM(MasUOM masUOM) {
		String result="";	
		Transaction tx=null;
		Session session =null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(masUOM);
			tx.commit();
			
			if(savedObj!=null) {
				result="200";
			}else {
				result = "500";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		
		return result;
	}

	@Override
	public String updateUOMDetails(Long UOMId, String UOMCode, String UOMName,Long userId) {
		String result="";
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Transaction transaction = session.beginTransaction();		
					Object object = session.load(MasUOM.class, UOMId);
					MasUOM masUOM = (MasUOM)object;
					
					
					masUOM.setUOMCode(UOMCode.toUpperCase());
					masUOM.setUOMName(UOMName.toUpperCase());			
					//System.out.println(masUOM.getUOMCode());
					//System.out.println(masUOM.getUOMName());			
					Users user = new Users();
					user.setUserId(userId);
					masUOM.setUser(user);				
				
				 long d = System.currentTimeMillis(); 
				  Timestamp date = new Timestamp(d);
				
					masUOM.setLastChgDate(date);
					session.update(masUOM);
					transaction.commit();				
					result = "200";
				//}
			//}
											
		}catch(Exception e) {
			
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public MasUOM checkUOM(String UOMCode) {
		MasUOM mUOM = new MasUOM();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasUOM.class);		
			criteria.add(Restrictions.eq("UOMCode", UOMCode));
			mUOM = (MasUOM)criteria.uniqueResult();
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mUOM;
	}

	@Override
	public String updateUOMStatus(Long UOMId, String UOMCode, String status,Long userId) {
		String result = "";	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				
				Object object =  session.load(MasUOM.class, UOMId);
				
				MasUOM masUOM = (MasUOM)object;
				Transaction transaction = session.beginTransaction();
				
				
				if(masUOM.getUOMStatus().equalsIgnoreCase("Y") || masUOM.getUOMStatus().equalsIgnoreCase("y")) {
					masUOM.setUOMStatus("n");
				}else if(masUOM.getUOMStatus().equalsIgnoreCase("N") || masUOM.getUOMStatus().equalsIgnoreCase("n")) {
					masUOM.setUOMStatus("y");
				}else {
					masUOM.setUOMStatus("Y");
				}
				session.update(masUOM);
				transaction.commit();
				result = "200";
			//}
			
		}catch(Exception e) {
			
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	/****************************MAS Item Unit*********************************************************/
	@Override
	public Map<String, List<MasStoreUnit>> getAllItemUnit(JSONObject jsondata) {
		Map<String, List<MasStoreUnit>> map = new HashMap<String, List<MasStoreUnit>>();
		List<MasStoreUnit> ItemUnitList = new ArrayList<MasStoreUnit>();
		int pageNo=1;
		int pageSize = 5;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasStoreUnit.class);
			
					
			if( jsondata.get("PN")!=null)
			 pageNo = Integer.parseInt(jsondata.get("PN").toString());
					
			String uName="";
				 if (jsondata.has("storeUnitName"))
				 {
					  uName = "%"+jsondata.get("storeUnitName")+"%";
					  if(jsondata.get("storeUnitName").toString().length()>0 && !jsondata.get("storeUnitName").toString().trim().equalsIgnoreCase("")) {
							criteria.add(Restrictions.ilike("storeUnitName", uName));
							
						}
				 }
				criteria.addOrder(Order.asc("storeUnitName"));
				 List totalMatches = criteria.list();
				 
				 if(!jsondata.get("PN").equals("0"))
					{
					 criteria.setFirstResult((pageSize) * (pageNo - 1));
					 criteria.setMaxResults(pageSize);
					 ItemUnitList = criteria.list();
					}
				 if( jsondata.get("PN").equals("0")) {	
					 criteria.add(Restrictions.eq("status","Y").ignoreCase());
					 ItemUnitList = criteria.list();
					}
			
		map.put("ItemUnitList", ItemUnitList);
		map.put("totalMatches", totalMatches);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@Override
	public List<MasStoreUnit> validateItemUnit( String storeUnitName) {
		List<MasStoreUnit> itemUnitList =  new ArrayList<MasStoreUnit>();	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria = session.createCriteria(MasStoreUnit.class);
				criteria.add(Restrictions.eq("storeUnitName", storeUnitName));
				itemUnitList = criteria.list();
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		return itemUnitList;
	}

	@Override
	public List<MasStoreUnit> validateItemUnitUpdate( String storeUnitName) {
		List<MasStoreUnit> itemUnitList =  new ArrayList<MasStoreUnit>();	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria = session.createCriteria(MasStoreUnit.class);
				criteria.add(Restrictions.eq("storeUnitName", storeUnitName));
				itemUnitList = criteria.list();
				//System.out.println("itemUnitList"+itemUnitList);
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		return itemUnitList;
	}

	@Override
	public String addItemUnit(MasStoreUnit masStoreUnit) {
		String result="";
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(masStoreUnit);
			tx.commit();
			if(savedObj!=null) {
				result="200";
			}else {
				result = "500";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		
		return result;
	}

	@Override
	public String updateItemUnitDetails(Long storeUnitId, String storeUnitName,Long userId) {
		String result="";
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
					
					Object object = session.load(MasStoreUnit.class, storeUnitId);
					MasStoreUnit masStoreUnit = (MasStoreUnit)object;
					
					Transaction transaction = session.beginTransaction();
					masStoreUnit.setStoreUnitName(storeUnitName.toUpperCase());			
									
					Users user = new Users();
					user.setUserId(userId);
					masStoreUnit.setUser(user);				
					long d = System.currentTimeMillis();
					Timestamp date = new Timestamp(d);
					masStoreUnit.setLastChgDate(date);
					session.update(masStoreUnit);
					transaction.commit();
					
					result = "200";
				//}
			//}
											
		}catch(Exception e) {
			
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public MasStoreUnit checkItemUnit(String storeUnitName) {
		MasStoreUnit mItemUnit = new MasStoreUnit();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasStoreUnit.class);		
			criteria.add(Restrictions.eq("storeUnitName", storeUnitName));
			mItemUnit = (MasStoreUnit)criteria.uniqueResult();
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mItemUnit;
	}

	@Override
	public String updateItemUnitStatus(Long storeUnitId, String storeUnitName, String status,Long userId) {
		String result = "";	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			
				Object object =  session.load(MasStoreUnit.class, storeUnitId);
				
				MasStoreUnit masStoreUnit = (MasStoreUnit)object;
				Transaction transaction = session.beginTransaction();				
				if(masStoreUnit.getStatus().equalsIgnoreCase("y")) {
					masStoreUnit.setStatus("n");
				}else if(masStoreUnit.getStatus().equalsIgnoreCase("n")) {
					masStoreUnit.setStatus("y");
				}else {
					masStoreUnit.setStatus("y");
				}
				session.update(masStoreUnit);
				transaction.commit();
				session.flush();
				result = "200";
			//}
			
		}catch(Exception e) {
			
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	/**----------------Users--------------------*/

	@Override
	public List<Users> validateUsers(String loginName, String firstName) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		List<Users> usersList = new ArrayList<Users>();
		Criteria criteria = session.createCriteria(Users.class);
		criteria.add(Restrictions.or(Restrictions.eq("loginName", loginName), Restrictions.eq("firstName", firstName)));
		usersList = criteria.list();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return usersList;
	}

	@Override
	public List<Users> validateUsersUpdate(String loginName, Long hospitalId) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		List<Users> usersList = new ArrayList<Users>();
		Criteria criteria = session.createCriteria(Users.class);
		criteria.createAlias( "masHospitals", "mh" );
		criteria.add(Restrictions.and(Restrictions.eq("loginName", loginName), Restrictions.eq("mh.hospitalId", hospitalId)));
		usersList = criteria.list();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return usersList;
	}

	@Override
	public String addUsers(Users users) {
		String result="";
		Transaction tx=null;
		Session session=null;
		try {
			  session = getHibernateUtils.getHibernateUtlis().OpenSession();	
			
			  tx = session.beginTransaction();
			Serializable savedObj =  session.save(users);
			tx.commit();
			
			if(savedObj!=null) {
				result = "200";
			}else {
				result = "500";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public Users checkUsers(String loginName) {
		
		Users users = new Users();	
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria criteria =  session.createCriteria(Users.class);
		criteria.add(Restrictions.eq("loginName", loginName));
		users = (Users)criteria.uniqueResult();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		
		return users;
	}

	@Override
	public Map<String, List<Users>>  getAllUsers(JSONObject jsonObj){
		Map<String, List<Users>> mapObj = new HashMap<String, List<Users>>();
		int pageSize = 5;
		int pageNo=1;
		Long hospitalId= Long.parseLong(jsonObj.get("hospitalId").toString());
		List totalMatches = new ArrayList();
		 
		List<Users> usersList = new ArrayList<Users>();
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(Users.class).createAlias("masHospital", "mh");
			if(hospitalId !=null) {
				criteria.add(Restrictions.eq("mh.hospitalId", hospitalId));
			}
			if( jsonObj.get("PN")!=null)
			 pageNo = Integer.parseInt(jsonObj.get("PN").toString());		
			String loginName="";
				 if (jsonObj.has("loginName"))
				 {
					  loginName = "%"+jsonObj.get("loginName")+"%";
					  if(jsonObj.get("loginName").toString().length()>0 && !jsonObj.get("loginName").toString().trim().equalsIgnoreCase("")) {
							criteria.add(Restrictions.ilike("loginName", loginName));
						}
				 }	
				 
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("userId").as("userId"));
			projectionList.add(Projections.property("loginName").as("loginName"));
			projectionList.add(Projections.property("firstName").as("firstName"));
			projectionList.add(Projections.property("status").as("status"));
			projectionList.add(Projections.property("masHospital").as("masHospital"));
			criteria.addOrder(Order.asc("firstName"));
			
			totalMatches = criteria.list();
			if(!jsonObj.get("PN").toString().equals("0"))
			{
			criteria.setFirstResult((pageSize) * (pageNo - 1));
			criteria.setProjection(projectionList).setMaxResults(pageSize);
			}
			usersList = criteria.list();
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			mapObj.put("usersList", usersList);
			mapObj.put("totalMatches", totalMatches);
			return mapObj;
		}
		
		
		


	@Override
	public List<Users> getUsers(String loginName){
		Object[] status = new Object[] {"y"};
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		List<Users> usersList = new ArrayList<Users>();
		Criteria criteria = session.createCriteria(Users.class);
		if(loginName.length()>0 && !loginName.trim().equalsIgnoreCase("")) {
			
			criteria.add(Restrictions.ilike("loginName", loginName));
		}
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("userId").as("userId"));
		projectionList.add(Projections.property("loginName").as("loginName"));
		projectionList.add(Projections.property("firstName").as("firstName"));
		projectionList.add(Projections.property("status").as("status"));
		
		criteria.setProjection(projectionList);
		
		usersList = criteria.setResultTransformer(new AliasToBeanResultTransformer(Users.class)).list();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return usersList;
	}

	@Override
	public String updateUsers(Long userId, String loginName, String firstName, Long hospitalId) {	
		String result="";
		try {
			
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Users users =  (Users)session.get(Users.class, userId);
				
			
				
				if(users != null)
				{
				
				Transaction transaction = session.beginTransaction();
				
				MasHospital hospital = new MasHospital();
				hospital.setHospitalId(hospitalId);
				
				//users.setMasHospital(hospital);
				
				long d = System.currentTimeMillis();
				Timestamp date = new Timestamp(d);
				
				users.setLastChgDate(date);
				session.update(users);
				
				transaction.commit();
				
				result="200";	
				}	
				else {
					session.update("msg","UserId dose not found");
				}
				
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
		
		
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		
		
		return result;
	}

	@Override
	public String updateUsersStatus(Long userId,String loginName,String status) {
		//Object[] status = new Object[] {"y"};
		String result = "";
		try {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		List<Users> userstatusList = new ArrayList<Users>();
		Criteria criteria = session.createCriteria(Users.class,"users");
		criteria.add(Restrictions.and(Restrictions.eq("userId", userId),Restrictions.eq("loginName", loginName),Restrictions.eq("users.status", status)));
		userstatusList = criteria.list();
			for(int i=0;i<userstatusList.size();i++) {
				Long userId1 = userstatusList.get(i).getUserId();
				
				Object usersObject =  session.load(Users.class, userId1);
				Users users = (Users)usersObject;
				
				Transaction transaction = session.beginTransaction();
							
				session.update(users);
				transaction.commit();
				result="200";
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public List<MasHospital> getHospitalList() {
		List<MasHospital> hospitalList = new ArrayList<MasHospital>();
		try {	 
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria criteria =  session.createCriteria(MasHospital.class);
		
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("hospitalId").as("hospitalId"));		
		projectionList.add(Projections.property("hospitalName").as("hospitalName"));
		criteria.setProjection(projectionList);
		
		hospitalList = criteria.setResultTransformer(new AliasToBeanResultTransformer(MasHospital.class)).list();
		
		}catch(Exception e) {
			e.printStackTrace();
		}
		return hospitalList;
	}

	/**----------------Mas MainChargecode--------------------*/

	@Override
	public List<MasMainChargecode> validateMainChargecode(String mainChargecodeCode, String mainChargecodeName) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		List<MasMainChargecode> mainChargecodeList = new ArrayList<MasMainChargecode>();
		Criteria criteria = session.createCriteria(MasMainChargecode.class);
		criteria.add(Restrictions.or(Restrictions.eq("mainChargecodeCode", mainChargecodeCode), Restrictions.eq("mainChargecodeName", mainChargecodeName)));
		mainChargecodeList = criteria.list();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return mainChargecodeList;
	}

	@Override
	public List<MasMainChargecode> validateMainChargecodeUpdate(String mainChargecodeName, Long departmentId) {
		List<MasMainChargecode> mainChargecodeList = new ArrayList<MasMainChargecode>();
		try {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();		
		Criteria criteria = session.createCriteria(MasMainChargecode.class);
		criteria = session.createCriteria(MasMainChargecode.class)
				.createAlias("masDepartment", "md")
				.add(Restrictions.and(Restrictions.eq("mainChargecodeName", mainChargecodeName),Restrictions.eq("md.departmentId", departmentId)));
		mainChargecodeList = criteria.list();
		}
		catch (Exception e) {
			
			e.printStackTrace();
		} finally {
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mainChargecodeList;
	}

	@Override
	public String addMainChargecode(MasMainChargecode mainChargecode) {
		String result="";
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();	
			
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(mainChargecode);
			tx.commit();
			if(savedObj!=null) {
				result = "200";
			}else {
				result = "500";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public MasMainChargecode checkMainChargecode(String mainChargecodeCode) {
		
		MasMainChargecode mainChargecode = new MasMainChargecode();	
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria criteria =  session.createCriteria(MasMainChargecode.class);
		criteria.add(Restrictions.eq("mainChargecodeCode", mainChargecodeCode));
		mainChargecode = (MasMainChargecode)criteria.uniqueResult();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		
		return mainChargecode;
	}

	@Override
	public Map<String, List<MasMainChargecode>>  getAllMainChargecode(JSONObject jsonObj){
		Map<String, List<MasMainChargecode>> mapObj = new HashMap<String, List<MasMainChargecode>>();
		int pageSize = 5;
		int pageNo=1;
		
		List totalMatches = new ArrayList();
		 
		List<MasMainChargecode> mainChargecodeList = new ArrayList<MasMainChargecode>();
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasMainChargecode.class,"mas");				
			if( jsonObj.get("PN")!=null)
			 pageNo = Integer.parseInt(jsonObj.get("PN").toString());		
			String mainChargecodeName="";
				 if (jsonObj.has("mainChargecodeName"))
				 {
					 mainChargecodeName = "%"+jsonObj.get("mainChargecodeName")+"%";
					  if(jsonObj.get("mainChargecodeName").toString().length()>0 && !jsonObj.get("mainChargecodeName").toString().trim().equalsIgnoreCase("")) {
							criteria.add(Restrictions.ilike("mainChargecodeName", mainChargecodeName));
						}
				 }	
				 
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("mainChargecodeId").as("mainChargecodeId"));
			projectionList.add(Projections.property("mainChargecodeCode").as("mainChargecodeCode"));
			projectionList.add(Projections.property("mainChargecodeName").as("mainChargecodeName"));
			projectionList.add(Projections.property("status").as("status"));
			projectionList.add(Projections.property("mas.masDepartment").as("masDepartment"));
			criteria.addOrder(Order.asc("mainChargecodeName"));
			
			totalMatches = criteria.list();
			criteria.setFirstResult((pageSize) * (pageNo - 1));
			criteria.setProjection(projectionList).setMaxResults(pageSize);
			mainChargecodeList = criteria.setResultTransformer(new AliasToBeanResultTransformer(MasMainChargecode.class)).list();
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			mapObj.put("mainChargecodeList", mainChargecodeList);
			mapObj.put("totalMatches", totalMatches);
			return mapObj;
		}
		
	@Override
	public List<MasMainChargecode> getMainChargecode(String mainChargecodeCode){
		Object[] status = new Object[] {"y"};
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		List<MasMainChargecode> mainChargecodeList = new ArrayList<MasMainChargecode>();
		Criteria criteria = session.createCriteria(MasMainChargecode.class);
		if(mainChargecodeCode.length()>0 && !mainChargecodeCode.trim().equalsIgnoreCase("")) {
			
			criteria.add(Restrictions.ilike("mainChargecodeCode", mainChargecodeCode));
		}
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("mainChargecodeId").as("mainChargecodeId"));
		projectionList.add(Projections.property("MainChargecodeCode").as("mainChargecodeCode"));
		projectionList.add(Projections.property("mainChargecodeName").as("mainChargecodeName"));
		projectionList.add(Projections.property("status").as("status"));
		
		criteria.setProjection(projectionList);
		
		mainChargecodeList = criteria.setResultTransformer(new AliasToBeanResultTransformer(MasMainChargecode.class)).list();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return mainChargecodeList;
	}

	@Override
	public String updateMainChargecode(Long mainChargecodeId, String mainChargecodeCode, String mainChargecodeName, Long departmentId,Long userId) {	
		String result="";
		try {
			
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			MasMainChargecode mainChargecode =  (MasMainChargecode)session.get(MasMainChargecode.class, mainChargecodeId);	
				
				if(mainChargecode != null)
				{
				
				Transaction transaction = session.beginTransaction();
				mainChargecode.setMainChargecodeCode(mainChargecodeCode);
				mainChargecode.setMainChargecodeName(mainChargecodeName);
				mainChargecode.setStatus(mainChargecode.getStatus());
				//mainChargecode.getMasDepartment().getDepartmentName();
				Users user = new Users();
				user.setUserId(userId);
				mainChargecode.setUser(user);
				MasDepartment department = new MasDepartment();
				department.setDepartmentId(departmentId);				
				mainChargecode.setMasDepartment(department);				
				long d = System.currentTimeMillis();
				Timestamp date = new Timestamp(d);				
				mainChargecode.setLastChgDate(date);				
				session.update(mainChargecode);				
				transaction.commit();
				session.flush();
				result="200";	
				}					
				
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
		
		
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		
		
		return result;
	}

	@Override
	public String updateMainChargecodeStatus(Long mainChargecodeId,String status) {
		
		String result = "";
		try {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		List<MasMainChargecode> mainChargecodeStatusList = new ArrayList<MasMainChargecode>();
		Criteria criteria = session.createCriteria(MasMainChargecode.class,"mainChargecode");
		criteria.add(Restrictions.and(Restrictions.eq("mainChargecodeId", mainChargecodeId)));
			
				Object mainChargecodeCodeObject =  session.load(MasMainChargecode.class, mainChargecodeId);
				MasMainChargecode mainChargecode = (MasMainChargecode)mainChargecodeCodeObject;
				
				Transaction transaction = session.beginTransaction();
				if(mainChargecode.getStatus().equalsIgnoreCase("y")){
					mainChargecode.setStatus("n");
					result="200";
				}else if(mainChargecode.getStatus().equalsIgnoreCase("n")) {
					mainChargecode.setStatus("y");
					result="200";
				}else {
					mainChargecode.setStatus("y");
				}
				session.update(mainChargecode);
				transaction.commit();
				session.flush();
				
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public List<MasDepartment> getDepartmentsList() {
		List<MasDepartment> departmentList = new ArrayList<MasDepartment>();
		try {	 
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria criteria =  session.createCriteria(MasDepartment.class);
		criteria.add(Restrictions.eq("status", "Y").ignoreCase());
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("departmentId").as("departmentId"));		
		projectionList.add(Projections.property("departmentName").as("departmentName"));
		criteria.setProjection(projectionList);
		
		departmentList = criteria.setResultTransformer(new AliasToBeanResultTransformer(MasDepartment.class)).list();
		
		}catch(Exception e) {
			e.printStackTrace();
		}
		return departmentList;
	}
	
	/****************************MAS ROLE*********************************************************/
	@Override
	public Map<String, List<MasRole>> getAllRole(JSONObject jsondata) {
		Map<String, List<MasRole>> map = new HashMap<String, List<MasRole>>();
		List<MasRole> roleList = new ArrayList<MasRole>();
		int pageNo=0;
		int pageSize=5;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasRole.class);
			
					
			if( jsondata.get("PN")!=null)
			 pageNo = Integer.parseInt(jsondata.get("PN").toString());
					
			String rName="";
				 if (jsondata.has("roleName"))
				 {
					  rName = "%"+jsondata.get("roleName")+"%";
					  if(jsondata.get("roleName").toString().length()>0 && !jsondata.get("roleName").toString().trim().equalsIgnoreCase("")) {
							criteria.add(Restrictions.ilike("roleName", rName));
							//criteria.addOrder(Order.asc(jsondata.get("roleName").toString()));
						}
				 }
				 List totalMatches = criteria.list();
				 
				 criteria.setFirstResult((pageSize) * (pageNo - 1));
				 criteria.setMaxResults(pageSize);
				 roleList = criteria.list();
			
			
		map.put("roleList", roleList);
		map.put("totalMatches", totalMatches);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@Override
	public List<MasRole> validateRole(String roleCode, String roleName) {
		List<MasRole> rList =  new ArrayList<MasRole>();	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria = session.createCriteria(MasRole.class);
				criteria.add(Restrictions.or(Restrictions.eq("roleCode", roleCode), Restrictions.eq("roleName", roleName)));
				rList = criteria.list();
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		return rList;
	}

	@Override
	public List<MasRole> validateRoleUpdate(String roleName) {
		List<MasRole> rList =  new ArrayList<MasRole>();	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria = session.createCriteria(MasRole.class);
				criteria.add( Restrictions.eq("roleName", roleName));
				rList = criteria.list();
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		return rList;
	}

	@Override
	public String addRole(MasRole masRole) {
		String result="";		
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Transaction tx = session.beginTransaction();
			Serializable savedObj =  session.save(masRole);
			tx.commit();
			if(savedObj!=null) {
				result="200";
			}else {
				result = "500";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		
		return result;
	}

	@Override
	public String updateRoleDetails(Long roleId, String roleCode, String roleName,Long userId) {
		String result="";
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
					
					Object object = session.load(MasRole.class, roleId);
					MasRole masRole = (MasRole)object;
					
					Transaction transaction = session.beginTransaction();
					masRole.setRoleCode(roleCode.toString().toUpperCase());
					masRole.setRoleName(roleName.toUpperCase());			
									
					Users user = new Users();
					user.setUserId(userId);
					masRole.setUser(user);				
					long d = System.currentTimeMillis();
					Timestamp date = new Timestamp(d);
					masRole.setLastChgDate(date);
					session.update(masRole);
					transaction.commit();
					
					result = "200";
				//}
			//}
											
		}catch(Exception e) {
			
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public MasRole checkRole(String roleCode) {
		MasRole mRole = new MasRole();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasRole.class);		
			criteria.add(Restrictions.eq("roleCode", roleCode));
			mRole = (MasRole)criteria.uniqueResult();
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mRole;
	}

	@Override
	public String updateRoleStatus(Long roleId, String roleCode, String status,Long userId) {
		String result = "";	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Object object =  session.load(MasRole.class, roleId);
				
				MasRole masRole = (MasRole)object;
				Transaction transaction = session.beginTransaction();
				
				
				if(masRole.getStatus().equalsIgnoreCase("Y") || masRole.getStatus().equalsIgnoreCase("y")) {
					masRole.setStatus("N");
				}else if(masRole.getStatus().equalsIgnoreCase("N") || masRole.getStatus().equalsIgnoreCase("n")) {
					masRole.setStatus("Y");
				}else {
					masRole.setStatus("Y");
				}
				session.update(masRole);
				transaction.commit();
				result = "200";
			//}
			
		}catch(Exception e) {
			
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	/******************************************RANGE************************************************/
	@Override
	public List<MasRange> validateRange(Long fromRange, Long toRange,String rangeFlag) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		List<MasRange> rangeList = new ArrayList<MasRange>();
		Criteria criteria = session.createCriteria(MasRange.class);
		criteria.add(Restrictions.and(Restrictions.eq("fromRange", fromRange), Restrictions.eq("toRange", toRange),Restrictions.eq("rangeFlag", rangeFlag)));
		rangeList = criteria.list();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return rangeList;
	}

	@Override
	public MasRange checkRange(Long fromRange) {
		
		MasRange range = new MasRange();	
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria criteria =  session.createCriteria(MasRange.class);
		criteria.add(Restrictions.eq("fromRange", fromRange));
		range = (MasRange)criteria.uniqueResult();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		
		return range;
	}
	
	@Override
	public String addRange(MasRange range) {
		String result="";
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();	
			
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(range);
			tx.commit();
			if(savedObj!=null) {
				result = "200";
			}else {
				result = "500";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	@Override
	public Map<String, List<MasRange>>  getAllRange(JSONObject jsonObj){
		Map<String, List<MasRange>> mapObj = new HashMap<String, List<MasRange>>();
		int pageSize = 5;
		int pageNo=1;
		
		List totalMatches = new ArrayList();
		 
		List<MasRange> rangeList = new ArrayList<MasRange>();
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasRange.class);				
			if( jsonObj.get("PN")!=null)
			 pageNo = Integer.parseInt(jsonObj.get("PN").toString());		
			String fromRange="";
				 if (jsonObj.has("fromRange"))
				 {
					 fromRange = "%"+jsonObj.get("fromRange")+"%";
					  if(jsonObj.get("fromRange").toString().length()>0 && !jsonObj.get("fromRange").toString().trim().equalsIgnoreCase("")) {
							criteria.add(Restrictions.ilike("fromRange", fromRange));
						}
				 }	
				 
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("masAdministrativeSex").as("masAdministrativeSex"));
			projectionList.add(Projections.property("rangeId").as("rangeId"));
			projectionList.add(Projections.property("fromRange").as("fromRange"));
			projectionList.add(Projections.property("toRange").as("toRange"));
			projectionList.add(Projections.property("status").as("status"));
			projectionList.add(Projections.property("rangeFlag").as("rangeFlag"));
			
			if(jsonObj.get("administrativeSexId") != null && !(jsonObj.get("administrativeSexId")).equals("")) {
				Criteria criteria1 = session.createCriteria(MasRange.class);
				criteria.createAlias( "masAdministrativeSex", "mas" );
				criteria.add(Restrictions.eq("mas.administrativeSexId", Long.parseLong(jsonObj.get("administrativeSexId")+"")));
			}
			
			totalMatches = criteria.list();
			criteria.setFirstResult((pageSize) * (pageNo - 1));
			criteria.setProjection(projectionList).setMaxResults(pageSize);
			rangeList = criteria.setResultTransformer(new AliasToBeanResultTransformer(MasRange.class)).list();
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			mapObj.put("rangeList", rangeList);
			mapObj.put("totalMatches", totalMatches);
			
			return mapObj;
		}

	
	@Override
	public String updateRange(Long rangeId,Long fromRange, Long toRange,Long userId) {	
		String result="";
		try {
			
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			MasRange range =  (MasRange)session.get(MasRange.class, rangeId);
				
			
				
				if(range != null)
				{
				
				Transaction transaction = session.beginTransaction();
				range.setFromRange(fromRange);
				range.setToRange(toRange);
				range.setStatus("y");
				
				Users user = new Users();
				user.setUserId(userId);
				range.setUser(user);
				long d = System.currentTimeMillis();
				Timestamp date = new Timestamp(d);
				
				range.setLastChgDate(date);
				session.update(range);
				
				transaction.commit();
				
				result="200";	
				}	
				else {
					session.update("msg","RangeId dose not found");
				}
				
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
		
		
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		
		
		return result;
	}
	
	@Override
	public List<MasAdministrativeSex> getGenderList() {
		List<MasAdministrativeSex> genderList = new ArrayList<MasAdministrativeSex>();
		try {	 
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria criteria =  session.createCriteria(MasAdministrativeSex.class);
		
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("administrativeSexId").as("administrativeSexId"));		
		projectionList.add(Projections.property("administrativeSexName").as("administrativeSexName"));
		criteria.setProjection(projectionList);
		
		genderList = criteria.setResultTransformer(new AliasToBeanResultTransformer(MasAdministrativeSex.class)).add(Restrictions.eq("status", 'Y').ignoreCase()).list();
		
		}catch(Exception e) {
			e.printStackTrace();
		}
		return genderList;
	}
	
	@Override
	public String updateRangeStatus(Long rangeId,Long fromRange,String status,Long userId) {
		//Object[] status = new Object[] {"y"};
		String result = "";
		try {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		List<MasRange> rangeStatusList = new ArrayList<MasRange>();
		Criteria criteria = session.createCriteria(MasRange.class,"users");
		criteria.add(Restrictions.and(Restrictions.eq("rangeId", rangeId),Restrictions.eq("fromRange", fromRange),Restrictions.eq("status", status)));
		rangeStatusList = criteria.list();
			for(int i=0;i<rangeStatusList.size();i++) {
				Long rangeId1 = rangeStatusList.get(i).getRangeId();
				
				Object rangeObject =  session.load(MasRange.class, rangeId1);
				MasRange range = (MasRange)rangeObject;
				
				Transaction transaction = session.beginTransaction();
				if(range.getStatus().equalsIgnoreCase("y")){
					range.setStatus("n");
				}else if(range.getStatus().equalsIgnoreCase("n")) {
					range.setStatus("y");
				}else {
					range.setStatus("y");
				}
				
				session.update(range);
				transaction.commit();
				result="200";
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	@Override
	public List<MasRange> getMasRange() {
		List<MasRange> listMasRange = new ArrayList<MasRange>();
		try {
			
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasRange.class);
			listMasRange = criteria.list();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return listMasRange;
	}
	
	
	/****************************MAS Store Group*********************************************************/
	@Override
	public Map<String, List<MasStoreGroup>> getAllStoreGroup(JSONObject jsondata) {
		Map<String, List<MasStoreGroup>> map = new HashMap<String, List<MasStoreGroup>>();
		List<MasStoreGroup> storeGroupList = new ArrayList<MasStoreGroup>();
		int pageNo=1;
		int pageSize = 5;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasStoreGroup.class);
			
					
			if( jsondata.get("PN")!=null)
			 pageNo = Integer.parseInt(jsondata.get("PN").toString());
					
			String sgName="";
				 if (jsondata.has("groupName"))
				 {
					  sgName = "%"+jsondata.get("groupName")+"%";
					  if(jsondata.get("groupName").toString().length()>0 && !jsondata.get("groupName").toString().trim().equalsIgnoreCase("")) {
							criteria.add(Restrictions.ilike("groupName", sgName));
						}
				 }
				 criteria.addOrder(Order.asc("groupName"));
				 List totalMatches = criteria.list();
				 
			
				 if( !jsondata.get("PN").equals("0"))
					{
					 criteria.setFirstResult((pageSize) * (pageNo - 1));
					 criteria.setMaxResults(pageSize);
					 storeGroupList = criteria.list();
					}
				 if( jsondata.get("PN").equals("0")) {
				 criteria.add(Restrictions.eq("status", "Y").ignoreCase());	 
				 storeGroupList = criteria.list();
				 }
		map.put("storeGroupList", storeGroupList);
		map.put("totalMatches", totalMatches);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@Override
	public List<MasStoreGroup> validateStoreGroup(String groupCode, String groupName) {
		List<MasStoreGroup> sgList =  new ArrayList<MasStoreGroup>();	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria = session.createCriteria(MasStoreGroup.class);
				criteria.add(Restrictions.or(Restrictions.eq("groupCode", groupCode), Restrictions.eq("groupName", groupName)));
				sgList = criteria.list();
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		return sgList;
	}

	@Override
	public List<MasStoreGroup> validateStoreGroupUpdate(String groupName) {
		List<MasStoreGroup> ecList =  new ArrayList<MasStoreGroup>();	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria = session.createCriteria(MasStoreGroup.class);
				criteria.add(Restrictions.eq("groupName", groupName));
				ecList = criteria.list();
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		return ecList;
	}

	@Override
	public String addStoreGroup(MasStoreGroup masStoreGroup) {
		String result="";
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(masStoreGroup);
			tx.commit();
			if(savedObj!=null) {
				result="200";
			}else {
				result = "500";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		
		return result;
	}

	@Override
	public String updateStoreGroup(Long groupId, String groupCode, String groupName, Long userId) {
		String result="";
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
					
					Object object = session.load(MasStoreGroup.class, groupId);
					MasStoreGroup masStoreGroup = (MasStoreGroup)object;
					
					Transaction transaction = session.beginTransaction();
					masStoreGroup.setGroupCode(groupCode.toString().toUpperCase());
					masStoreGroup.setGroupName(groupName.toUpperCase());			
					masStoreGroup.setLastChgBy(userId);										
					long d = System.currentTimeMillis();
					Timestamp date = new Timestamp(d);
					masStoreGroup.setLastChgDate(date);
					session.update(masStoreGroup);
					transaction.commit();					
					result = "200";
				//}
			//}
											
		}catch(Exception e) {
			
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public MasStoreGroup checkStoreGroup(String groupCode) {
		MasStoreGroup mStoreGroup = new MasStoreGroup();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasStoreGroup.class);		
			criteria.add(Restrictions.eq("groupCode", groupCode));
			mStoreGroup = (MasStoreGroup)criteria.uniqueResult();
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mStoreGroup;
	}

	@Override
	public String updateStoreGroupStatus(Long groupId, String groupCode, String status,Long userId) {
		String result = "";	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Object object =  session.load(MasStoreGroup.class, groupId);
				
				MasStoreGroup masStoreGroup = (MasStoreGroup)object;
				Transaction transaction = session.beginTransaction();
				
				
				if(masStoreGroup.getStatus().equalsIgnoreCase("Y") || masStoreGroup.getStatus().equalsIgnoreCase("y")) {
					masStoreGroup.setStatus("N");
				}else if(masStoreGroup.getStatus().equalsIgnoreCase("N") || masStoreGroup.getStatus().equalsIgnoreCase("n")) {
					masStoreGroup.setStatus("Y");
				}else {
					masStoreGroup.setStatus("Y");
				}
				session.update(masStoreGroup);
				transaction.commit();
				result = "200";
			//}
			
		}catch(Exception e) {
			
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	/****************************MAS ItemType*********************************************************/
	@Override
	public Map<String, List<MasItemType>> getAllItemType(JSONObject jsondata) {
		Map<String, List<MasItemType>> map = new HashMap<String, List<MasItemType>>();
		List<MasItemType> itemTypeList = new ArrayList<MasItemType>();
		int pageNo=1;
		int pageSize = 5;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasItemType.class);
			
					
			if( jsondata.get("PN")!=null)
			 pageNo = Integer.parseInt(jsondata.get("PN").toString());
					
			String itName="";
				 if (jsondata.has("itemTypeName"))
				 {
					  itName = "%"+jsondata.get("itemTypeName")+"%";
					  if(jsondata.get("itemTypeName").toString().length()>0 && !jsondata.get("itemTypeName").toString().trim().equalsIgnoreCase("")) {
							criteria.add(Restrictions.ilike("itemTypeName", itName));
						}
				 }
				 criteria.addOrder(Order.asc("itemTypeName"));
				 List totalMatches = criteria.list();
				 
				 if( !jsondata.get("PN").equals("0"))
					{
					 criteria.setFirstResult((pageSize) * (pageNo - 1));
					 criteria.setMaxResults(pageSize);
					 itemTypeList = criteria.list();
					}
				 if( jsondata.get("PN").equals("0")) {
					criteria.add(Restrictions.eq("status", "y").ignoreCase());
					criteria.setMaxResults(pageSize);
					itemTypeList = criteria.list();
					}
				 
			
			
		map.put("itemTypeList", itemTypeList);
		map.put("totalMatches", totalMatches);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@Override
	public List<MasItemType> validateItemType(String itemTypeCode, String itemTypeName) {
		List<MasItemType> itList =  new ArrayList<MasItemType>();	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria = session.createCriteria(MasItemType.class);
				criteria.add(Restrictions.or(Restrictions.eq("itemTypeCode", itemTypeCode), Restrictions.eq("itemTypeName", itemTypeName)));
				itList = criteria.list();
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		return itList;
	}

	@Override
	public List<MasItemType> validateItemTypeUpdate(String itemTypeName) {
		List<MasItemType> itList =  new ArrayList<MasItemType>();	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria = session.createCriteria(MasItemType.class);
				criteria.add(Restrictions.eq("itemTypeName", itemTypeName));
				itList = criteria.list();
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		return itList;
	}

	@Override
	public String addItemType(MasItemType masItemType) {
		String result="";
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(masItemType);
			tx.commit();
			if(savedObj!=null) {
				result="200";
			}else {
				result = "500";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		
		return result;
	}

	@Override
	public String updateItemType(Long itemTypeId, String itemTypeCode, String itemTypeName,Long userId) {
		String result="";
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
					
					Object object = session.load(MasItemType.class, itemTypeId);
					MasItemType masItemType = (MasItemType)object;
					
					Transaction transaction = session.beginTransaction();
					masItemType.setItemTypeCode(itemTypeCode.toString().toUpperCase());
					masItemType.setItemTypeName(itemTypeName.toUpperCase());					
					masItemType.setLastChgBy(userId);									
					long d = System.currentTimeMillis();
					Timestamp date = new Timestamp(d);
					masItemType.setLastChgDate(date);
					session.update(masItemType);
					transaction.commit();					
					result = "200";
				//}
			//}
											
		}catch(Exception e) {
			
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public MasItemType checkItemType(String itemTypeCode) {
		MasItemType mItemType = new MasItemType();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasItemType.class);		
			criteria.add(Restrictions.eq("itemTypeCode", itemTypeCode));
			mItemType = (MasItemType)criteria.uniqueResult();
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mItemType;
	}

	@Override
	public String updateItemTypeStatus(Long itemTypeId, String itemTypeCode, String status,Long userId) {
		String result = "";	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Object object =  session.load(MasItemType.class, itemTypeId);
				
				MasItemType masItemType = (MasItemType)object;
				Transaction transaction = session.beginTransaction();
				
				
				if(masItemType.getStatus().equalsIgnoreCase("Y") || masItemType.getStatus().equalsIgnoreCase("y")) {
					masItemType.setStatus("N");
				}else if(masItemType.getStatus().equalsIgnoreCase("N") || masItemType.getStatus().equalsIgnoreCase("n")) {
					masItemType.setStatus("Y");
				}else {
					masItemType.setStatus("Y");
				}
				session.update(masItemType);
				transaction.commit();
				result = "200";
			//}
			
		}catch(Exception e) {
			
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	/**----------------MAS StoreSection--------------------*/

	@Override
	public List<MasStoreSection> validateMasStoreSection(String sectionCode,String sectionName){
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria criteria = null;
		criteria = session.createCriteria(MasStoreSection.class)
							.add(Restrictions.or(Restrictions.eq("sectionCode", sectionCode),
									Restrictions.eq("sectionName", sectionName)));
		List<MasStoreSection> storeSectionList = criteria.list();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return storeSectionList;
	}

	@Override
	public List<MasStoreSection> validateMasStoreSectionUpdate(String sectionName){
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		List<MasStoreSection> storeSectionList = new ArrayList<MasStoreSection>();
		Criteria criteria = session.createCriteria(MasStoreSection.class);
		criteria.add(Restrictions.eq("sectionName", sectionName));
		storeSectionList = criteria.list();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return storeSectionList;
	}

	@Override
	public String addMasStoreSection(MasStoreSection masStoreSection) {
		String result="";
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();		
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(masStoreSection);
			tx.commit();
			
			if(savedObj!=null) {
				result = "200";
			}else {
				result = "500";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public MasStoreSection chkStoreSection(String sectionName) {
		
		MasStoreSection masStoreSection = new MasStoreSection();	
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria criteria =  session.createCriteria(MasStoreSection.class);
		criteria.add(Restrictions.eq("sectionName", sectionName));
		masStoreSection = (MasStoreSection)criteria.uniqueResult();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		
		return masStoreSection;
	}

	@Override
	public Map<String, List<MasStoreSection>>  getAllStoreSection(JSONObject jsonObj){
		Map<String, List<MasStoreSection>> mapObj = new HashMap<String, List<MasStoreSection>>();
		int pageSize = 5;
		int pageNo=1;
		
		List totalMatches = new ArrayList();
		 
		List<MasStoreSection> masStoreSectionList = new ArrayList<MasStoreSection>();
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasStoreSection.class);			
			if( jsonObj.get("PN")!=null)
			 pageNo = Integer.parseInt(jsonObj.get("PN").toString());
			String sectionName="";
				 if (jsonObj.has("sectionName"))
				 {
					  sectionName = "%"+jsonObj.get("sectionName")+"%";
					  if(jsonObj.get("sectionName").toString().length()>0 && !jsonObj.get("sectionName").toString().trim().equalsIgnoreCase("")) {
							criteria.add(Restrictions.ilike("sectionName", sectionName));
						}
				 }	
				 
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("sectionId").as("sectionId"));			
			projectionList.add(Projections.property("sectionName").as("sectionName"));
			projectionList.add(Projections.property("sectionCode").as("sectionCode"));
			projectionList.add(Projections.property("status").as("status"));			
			criteria.addOrder(Order.asc("sectionName"));
			
			totalMatches = criteria.list();
			//System.out.println("totalMatches :: "+totalMatches.size());
					
			if(! jsonObj.get("PN").equals("0"))
			{
			criteria.setFirstResult((pageSize) * (pageNo - 1));
			criteria.setProjection(projectionList).setMaxResults(pageSize);
			masStoreSectionList = criteria.setResultTransformer(new AliasToBeanResultTransformer(MasStoreSection.class)).list();
			}
			if(jsonObj.get("PN").equals("0")) {
			criteria.setProjection(projectionList);
			criteria.add(Restrictions.eq("status", "Y").ignoreCase());
			masStoreSectionList = criteria.setResultTransformer(new AliasToBeanResultTransformer(MasStoreSection.class)).list();
			}
			
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			mapObj.put("masStoreSectionList", masStoreSectionList);
			mapObj.put("totalMatches", totalMatches);
			return mapObj;
		}
		
	@Override
	public List<MasStoreSection> getStoreSection(String sectionName){
		Object[] status = new Object[] {"y"};
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		List<MasStoreSection> masStoreSectionList = new ArrayList<MasStoreSection>();
		Criteria criteria = session.createCriteria(MasStoreSection.class);
		if(sectionName.length()>0 && !sectionName.trim().equalsIgnoreCase("")) {
			
			criteria.add(Restrictions.ilike("sectionName", sectionName));
		}
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("sectionId").as("sectionId"));
		projectionList.add(Projections.property("sectionCode").as("sectionCode"));
		projectionList.add(Projections.property("sectionName").as("sectionName"));
		projectionList.add(Projections.property("status").as("status"));
		
		criteria.setProjection(projectionList);
		
		masStoreSectionList = criteria.setResultTransformer(new AliasToBeanResultTransformer(MasStoreSection.class)).list();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return masStoreSectionList;
	}

	@Override
	public String updateStoreSection(Long sectionId, String sectionName, Long userId) {	
		String result="";
		try {
			
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				MasStoreSection masStoreSection =  (MasStoreSection)session.get(MasStoreSection.class, sectionId);		
				
				if(masStoreSection != null)
				{
				
				Transaction transaction = session.beginTransaction();				
				masStoreSection.setSectionName(sectionName);
				masStoreSection.setStatus("y");				
				Users usr = new Users();
				usr.setUserId(userId);
				masStoreSection.setUsers(usr);
				long d = System.currentTimeMillis();
				Timestamp date = new Timestamp(d);				
				masStoreSection.setLastChgDate(date);				
				session.update(masStoreSection);				
				transaction.commit();
				session.flush();
				result="200";	
				}	
				else {
					session.update("msg","Section Id dose not found");
				}
				
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
		
		
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		
		
		return result;
	}

	@Override
	public String updateStoreSectionStatus(Long sectionId,String status,Long userId) {
		
		String result = "";
		try {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		List<MasStoreSection> masStoreSectionStatusList = new ArrayList<MasStoreSection>();
				Object storeSectionObject =  session.load(MasStoreSection.class, sectionId);
				MasStoreSection masStoreSection = (MasStoreSection)storeSectionObject;				
				Transaction transaction = session.beginTransaction();
				if(masStoreSection.getStatus().equalsIgnoreCase("y")){
					masStoreSection.setStatus("n");
					//result="400";
				}else if(masStoreSection.getStatus().equalsIgnoreCase("n")) {
					masStoreSection.setStatus("y");
					//result="200";
				}else {
					masStoreSection.setStatus("y");
				}
				
				session.update(masStoreSection);
				transaction.commit();
				session.flush();
				result="200";
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	
	/**----------------MAS ItemClass--------------------*/

	@Override
	public List<MasItemClass> validateMasItemClass(String itemClassCode, String itemClassName) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria criteria = null;
		criteria = session.createCriteria(MasItemClass.class)
							.add(Restrictions.or(Restrictions.eq("itemClassCode", itemClassCode), Restrictions.eq("itemClassName", itemClassName)));
		List<MasItemClass> itemClassList = criteria.list();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return itemClassList;
	}

	@Override
	public List<MasItemClass> validateMasItemClassUpdate(Long sectionId, String itemClassName){
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		List<MasItemClass> itemClassList = new ArrayList<MasItemClass>();
		Criteria criteria = session.createCriteria(MasItemClass.class);
		criteria.createAlias( "masStoreSection", "mss" );
		criteria.add(Restrictions.and(Restrictions.eq("mss.sectionId", sectionId),Restrictions.eq("itemClassName", itemClassName)));
		itemClassList = criteria.list();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return itemClassList;
	}
	
	@Override
	public String addMasItemClass(MasItemClass masItemClass) {
		String result="";
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();		
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(masItemClass);
			tx.commit();
			
			if(savedObj!=null) {
				result = "200";
			}else {
				result = "500";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public MasItemClass chkItemClass(String itemClassName) {
		
		MasItemClass masItemClass = new MasItemClass();	
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria criteria =  session.createCriteria(MasItemClass.class);
		criteria.add(Restrictions.eq("itemClassName", itemClassName));
		masItemClass = (MasItemClass)criteria.uniqueResult();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		
		return masItemClass;
	}

	@Override
	public Map<String, List<MasItemClass>>  getAllItemClass(JSONObject jsonObj){
		Map<String, List<MasItemClass>> mapObj = new HashMap<String, List<MasItemClass>>();
		int pageSize = 5;
		int pageNo=1;
		
		List totalMatches = new ArrayList();
		 
		List<MasItemClass> masItemClassList = new ArrayList<MasItemClass>();
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasItemClass.class);			
			if( jsonObj.get("PN")!=null)
			 pageNo = Integer.parseInt(jsonObj.get("PN").toString());
			String itemClassName="";
				 if (jsonObj.has("itemClassName"))
				 {
					  itemClassName = "%"+jsonObj.get("itemClassName")+"%";
					  if(jsonObj.get("itemClassName").toString().length()>0 && !jsonObj.get("itemClassName").toString().trim().equalsIgnoreCase("")) {
							criteria.add(Restrictions.ilike("itemClassName", itemClassName));
						}
				 }	
				 
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("itemClassId").as("itemClassId"));
			projectionList.add(Projections.property("itemClassCode").as("itemClassCode"));
			projectionList.add(Projections.property("itemClassName").as("itemClassName"));
			projectionList.add(Projections.property("status").as("status"));
			projectionList.add(Projections.property("masStoreSection").as("masStoreSection"));
			criteria.addOrder(Order.asc("itemClassName"));
			
			totalMatches = criteria.list();			
			
			if(! jsonObj.get("PN").equals("0"))
			{
			criteria.setFirstResult((pageSize) * (pageNo - 1));
			criteria.setProjection(projectionList).setMaxResults(pageSize);
			masItemClassList = criteria.setResultTransformer(new AliasToBeanResultTransformer(MasItemClass.class)).list();
			}
			if(jsonObj.get("PN").equals("0")){
				
				criteria.add(Restrictions.eq("status","y").ignoreCase());
				criteria.setProjection(projectionList);
				masItemClassList = criteria.setResultTransformer(new AliasToBeanResultTransformer(MasItemClass.class)).list();
			}
			
			
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			mapObj.put("masItemClassList", masItemClassList);
			mapObj.put("totalMatches", totalMatches);
			return mapObj;
		}
		
	@Override
	public List<MasItemClass> getItemClass(String itemClassName){
		Object[] status = new Object[] {"y"};
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		List<MasItemClass> masItemClassList = new ArrayList<MasItemClass>();
		Criteria criteria = session.createCriteria(MasItemClass.class);
		if(itemClassName.length()>0 && !itemClassName.trim().equalsIgnoreCase("")) {
			
			criteria.add(Restrictions.ilike("itemClassName", itemClassName));
		}
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("itemClassId").as("itemClassId"));
		projectionList.add(Projections.property("itemClassCode").as("itemClassCode"));
		projectionList.add(Projections.property("itemClassName").as("itemClassName"));
		projectionList.add(Projections.property("status").as("status"));
		
		criteria.setProjection(projectionList);
		
		masItemClassList = criteria.setResultTransformer(new AliasToBeanResultTransformer(MasItemClass.class)).list();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return masItemClassList;
	}

	@Override
	public String updateItemClass(Long itemClassId, String itemClassName, Long sectionId,Long userId) {	
		String result="";
		
		List<MasItemClass> msItemClassList=validateMasItemClassUpdate(sectionId,itemClassName);
		
			try {
				Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				MasItemClass masItemClass =  (MasItemClass)session.get(MasItemClass.class, itemClassId);
				
				if(masItemClass != null)
				{
				
				Transaction transaction = session.beginTransaction();
				if(msItemClassList !=null && msItemClassList.size()>0)
				{
				masItemClass.setItemClassName(masItemClass.getItemClassName());
				}
				else {
					masItemClass.setItemClassName(itemClassName);
				}
				masItemClass.setStatus(masItemClass.getStatus());				
				Users usr = new Users();
				usr.setUserId(userId);
				masItemClass.setUsers(usr);
				MasStoreSection storeSection = new MasStoreSection();
				storeSection.setSectionId(sectionId);				
				masItemClass.setMasStoreSection(storeSection);				
				long d = System.currentTimeMillis();
				Timestamp date = new Timestamp(d);				
				masItemClass.setLastChgDate(date);				
				session.update(masItemClass);				
				transaction.commit();
				session.flush();
				result="200";	
				}	
								
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
		
		
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		
		
		return result;
	}

	@Override
	public String updateItemClassStatus(Long itemClassId,String status) {
		
		String result = "";
		try {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();	
				
				Object itemClassObject =  session.load(MasItemClass.class, itemClassId);
				MasItemClass masItemClass = (MasItemClass)itemClassObject;				
				Transaction transaction = session.beginTransaction();
				if(masItemClass.getStatus().equalsIgnoreCase("y")){
					masItemClass.setStatus("n");
					//result="400";
				}else if(masItemClass.getStatus().equalsIgnoreCase("n")) {
					masItemClass.setStatus("y");
					//result="200";
				}else {
					masItemClass.setStatus("y");
				}
				session.update(masItemClass);
				transaction.commit();
				session.flush();
				result="200";
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	@Override
	public List<MasStoreSection> getStoreSectionList() {
		List<MasStoreSection> storeSectionList = new ArrayList<MasStoreSection>();
		try {	 
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria criteria =  session.createCriteria(MasStoreSection.class);
		
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("storeSectionId").as("storeSectionId"));		
		projectionList.add(Projections.property("storeSectionName").as("storeSectionName"));
		criteria.setProjection(projectionList);
		
		storeSectionList = criteria.setResultTransformer(new AliasToBeanResultTransformer(MasStoreSection.class)).add(Restrictions.eq("status", 'Y').ignoreCase()).list();
		
		}catch(Exception e) {
			e.printStackTrace();
		}
		return storeSectionList;
	}
	
	/**----------------MAS Section--------------------*/

	@Override
	public List<MasSection> validateMasSection(String sectionCode, String sectionName) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria criteria = null;
		criteria = session.createCriteria(MasSection.class)
							.add(Restrictions.or(Restrictions.eq("sectionCode", sectionCode), Restrictions.eq("sectionName", sectionName)));
		List<MasSection> sectionList = criteria.list();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return sectionList;
	}

	@Override
	public List<MasSection> validateMasSectionUpdate(String sectionName){
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		List<MasSection> sectionList = new ArrayList<MasSection>();
		Criteria criteria = session.createCriteria(MasSection.class);
		//criteria.createAlias( "masHospital", "mh" );
		criteria.add(Restrictions.eq("sectionName", sectionName));
		sectionList = criteria.list();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return sectionList;
	}

	@Override
	public String addMasSection(MasSection masSection) {
		String result="";
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();		
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(masSection);
			tx.commit();
			
			if(savedObj!=null) {
				result = "200";
			}else {
				result = "500";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public MasSection chkSection(String sectionName) {
		
		MasSection masSection = new MasSection();	
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria criteria =  session.createCriteria(MasSection.class);
		criteria.add(Restrictions.eq("sectionName", sectionName));
		masSection = (MasSection)criteria.uniqueResult();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		
		return masSection;
	}

	@Override
	public Map<String, List<MasSection>>  getAllSection(JSONObject jsonObj){
		Map<String, List<MasSection>> mapObj = new HashMap<String, List<MasSection>>();
		int pageSize = 5;
		int pageNo=1;
		
		List totalMatches = new ArrayList();
		 
		List<MasSection> masSectionList = new ArrayList<MasSection>();
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasSection.class);			
			if( jsonObj.get("PN")!=null)
			 pageNo = Integer.parseInt(jsonObj.get("PN").toString());
			String sectionName="";
				 if (jsonObj.has("sectionName"))
				 {
					  sectionName = "%"+jsonObj.get("sectionName")+"%";
					  if(jsonObj.get("sectionName").toString().length()>0 && !jsonObj.get("sectionName").toString().trim().equalsIgnoreCase("")) {
							criteria.add(Restrictions.ilike("sectionName", sectionName));
						}
				 }	
				 
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("sectionId").as("sectionId"));
			projectionList.add(Projections.property("sectionCode").as("sectionCode"));
			projectionList.add(Projections.property("sectionName").as("sectionName"));
			projectionList.add(Projections.property("status").as("status"));
			projectionList.add(Projections.property("masHospital").as("masHospital"));
			criteria.addOrder(Order.asc("sectionName"));
			
			totalMatches = criteria.list();
			//System.out.println("totalMatches :: "+totalMatches.size());
					
			criteria.setFirstResult((pageSize) * (pageNo - 1));
			criteria.setProjection(projectionList).setMaxResults(pageSize);
			masSectionList = criteria.setResultTransformer(new AliasToBeanResultTransformer(MasSection.class)).list();
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			mapObj.put("masSectionList", masSectionList);
			mapObj.put("totalMatches", totalMatches);
			return mapObj;
		}
		
	@Override
	public List<MasSection> getSection(String sectionName){
		Object[] status = new Object[] {"y"};
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		List<MasSection> masSectionList = new ArrayList<MasSection>();
		Criteria criteria = session.createCriteria(MasSection.class);
		if(sectionName.length()>0 && !sectionName.trim().equalsIgnoreCase("")) {
			
			criteria.add(Restrictions.ilike("sectionName", sectionName));
		}
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("sectionId").as("sectionId"));
		projectionList.add(Projections.property("sectionCode").as("sectionCode"));
		projectionList.add(Projections.property("sectionName").as("sectionName"));
		projectionList.add(Projections.property("status").as("status"));
		
		criteria.setProjection(projectionList);
		
		masSectionList = criteria.setResultTransformer(new AliasToBeanResultTransformer(MasSection.class)).list();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return masSectionList;
	}

	@Override
	public String updateSection(Long sectionId, String sectionCode, String sectionName, Long hospitalId,Long userId) {	
		String result="";
		try {
			
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				MasSection masSection =  (MasSection)session.get(MasSection.class, sectionId);
				
			
				
				if(masSection != null)
				{
				
				Transaction transaction = session.beginTransaction();
				masSection.setSectionCode(sectionCode);
				masSection.setSectionName(sectionName);
				masSection.setStatus("y");
				masSection.getMasHospital().getHospitalName();
				Users usr = new Users();
				usr.setUserId(userId);
				MasHospital hospital = new MasHospital();
				hospital.setHospitalId(hospitalId);
				
				masSection.setMasHospital(hospital);
				
				long d = System.currentTimeMillis();
				Timestamp date = new Timestamp(d);
				
				masSection.setLastChgDate(date);
				//String lastChgTime = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
				session.update(masSection);
				
				transaction.commit();
				
				result="200";	
				}	
				else {
					session.update("msg","Section Id dose not found");
				}
				
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
		
		
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		
		
		return result;
	}

	@Override
	public String updateSectionStatus(Long sectionId,String sectionCode,String status,Long userId) {
		//Object[] status = new Object[] {"y"};
		String result = "";
		try {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		List<MasSection> masSectionStatusList = new ArrayList<MasSection>();
		Criteria criteria = session.createCriteria(MasSection.class,"masSection");
		criteria.add(Restrictions.and(Restrictions.eq("sectionId", sectionId),Restrictions.eq("sectionCode", sectionCode),Restrictions.eq("masSection.status", status)));
		//criteria.add(Restrictions.in("mascommand.status", status));
		masSectionStatusList = criteria.list();
			for(int i=0;i<masSectionStatusList.size();i++) {
				Long sectionId1 = masSectionStatusList.get(i).getSectionId();
				
				Object sectionObject =  session.load(MasSection.class, sectionId1);
				MasSection masSection = (MasSection)sectionObject;
				
				Transaction transaction = session.beginTransaction();
				if(masSection.getStatus().equalsIgnoreCase("y")){
					masSection.setStatus("n");
					//result="400";
				}else if(masSection.getStatus().equalsIgnoreCase("n")) {
					masSection.setStatus("y");
					//result="200";
				}else {
					masSection.setStatus("y");
				}
				
				session.update(masSection);
				transaction.commit();
				result="200";
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	
/************************
 * MAS STORE ITEM
 * ***************************/
	
	
	@Override
	public List<MasStoreItem> validateMasStoreItem(String pvmsNo, String nomenclature) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria criteria = null;
		criteria = session.createCriteria(MasStoreItem.class)
							.add(Restrictions.eq("nomenclature", nomenclature));
		List<MasStoreItem> itemList = criteria.list();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return itemList;
	}

	@Override
	public List<MasStoreItem> validateMasStoreItemUpdate(String pvmsNo,String nomenclature) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		List<MasStoreItem> itemList = new ArrayList<MasStoreItem>();
		Criteria criteria = session.createCriteria(MasStoreItem.class)
				.add(Restrictions.or(Restrictions.eq("pvmsNo", pvmsNo), Restrictions.eq("nomenclature", nomenclature)));
		itemList = criteria.list();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return itemList;
	}

	@Override
	public String addMasStoreItem(MasStoreItem masStoreItem) {
		String result="";
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();		
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(masStoreItem);
			//System.out.println("ID"+savedObj);
			tx.commit();
			
			if(savedObj!=null) {
				result = "200";
			}else {
				result = "403";
			}
			session.flush();
		}catch(Exception e) {
			result = "403";
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public MasStoreItem chkStoreItem(String pvmsNo, String nomenclature) {
		MasStoreItem masStoreItem = new MasStoreItem();	
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria criteria =  session.createCriteria(MasStoreItem.class);
		criteria.add(Restrictions.eq("pvmsNo", pvmsNo));
		criteria.add(Restrictions.eq("nomenclature", nomenclature));
		masStoreItem = (MasStoreItem)criteria.uniqueResult();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		
		return masStoreItem;
	}

	@Override
	public Map<String, List<MasStoreItem>> getAllStoreItem(JSONObject jsonObj) {
		Map<String, List<MasStoreItem>> mapObj = new HashMap<String, List<MasStoreItem>>();
		int pageSize = 5;
		int pageNo=1;
		String drugsCode = HMSUtil.getProperties("adt.properties", "drugsCode").trim();
		String vaccineCode = HMSUtil.getProperties("adt.properties", "vaccineCode").trim();
		List totalMatches = new ArrayList();
		 
		List<MasStoreItem> masStoreItemList = new ArrayList<MasStoreItem>();
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasStoreItem.class);
			
			if( jsonObj.get("PN")!=null)
			 pageNo = Integer.parseInt(jsonObj.get("PN").toString());
				
			String nomenclature="";
				 if (jsonObj.has("nomenclature"))
				 {
					 nomenclature = "%"+jsonObj.get("nomenclature")+"%";
					  if(jsonObj.get("nomenclature").toString().length()>0 && !jsonObj.get("nomenclature").toString().trim().equalsIgnoreCase("")) {
							criteria.add(Restrictions.ilike("nomenclature", nomenclature));
						}
				 }	
				 
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("itemId").as("itemId"));
			projectionList.add(Projections.property("pvmsNo").as("pvmsNo"));
			projectionList.add(Projections.property("nomenclature").as("nomenclature"));
			projectionList.add(Projections.property("status").as("status"));
			projectionList.add(Projections.property("masStoreGroup").as("masStoreGroup"));
			projectionList.add(Projections.property("masStoreUnit").as("masStoreUnit"));
			projectionList.add(Projections.property("masStoreSection").as("masStoreSection"));
			projectionList.add(Projections.property("masItemClass").as("masItemClass"));
			projectionList.add(Projections.property("masItemType").as("masItemType"));
			//projectionList.add(Projections.property("masHospital").as("masHospital"));
			projectionList.add(Projections.property("dispUnitQty").as("dispUnitQty"));
			projectionList.add(Projections.property("rolD").as("rolD"));
			projectionList.add(Projections.property("rolS").as("rolS"));
			projectionList.add(Projections.property("dangerousDrug").as("dangerousDrug"));
			projectionList.add(Projections.property("edl").as("edl"));
			projectionList.add(Projections.property("sastiDawai").as("sastiDawai"));
			projectionList.add(Projections.property("facilityCode").as("facilityCode"));
			projectionList.add(Projections.property("itemUnitId").as("itemUnitId"));
			projectionList.add(Projections.property("dosage").as("dosage"));
			projectionList.add(Projections.property("noOfDays").as("noOfDays"));
			projectionList.add(Projections.property("masFrequency").as("masFrequency"));
			criteria.addOrder(Order.asc("nomenclature"));
			
			totalMatches = criteria.list();			
					
			criteria.setFirstResult((pageSize) * (pageNo - 1));
			criteria.setProjection(projectionList).setMaxResults(pageSize);
			masStoreItemList = criteria.setResultTransformer(new AliasToBeanResultTransformer(MasStoreItem.class)).list();
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			mapObj.put("masStoreItemList", masStoreItemList);
			mapObj.put("totalMatches", totalMatches);
			return mapObj;
	}

	
	public List<MasStoreItem> getStoreItem(String pvmsNo) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public MasStoreItem getMasStoreItemById(Long masid) {
		
		MasStoreItem masStoreItem=new MasStoreItem();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			if(masid !=0) {
			 masStoreItem =  (MasStoreItem)session.get(MasStoreItem.class, masid);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		
		return masStoreItem;
	}
	
	@Override
	public String updateStoreItem(MasStoreItem objMaStoreItem) {
		String result="";
		try {
			
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();			
				
				if(objMaStoreItem != null)
				{
				
				Transaction transaction = session.beginTransaction();							
				session.update(objMaStoreItem);				
				transaction.commit();
				session.flush();
				result="200";	
			}	
	
		}catch(Exception e) {
			e.printStackTrace();
			//System.out.println("error ="+e);
		}finally {
		
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		
		
		return result;
	}

	
	
	@Override
	public String updateStoreItemStatus(Long itemId, String status,Long userId) {
		String result = "";
		try {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		List<MasStoreItem> masItemStatusList = new ArrayList<MasStoreItem>();	
				
				Object itemObject =  session.load(MasStoreItem.class, itemId);
				MasStoreItem masStoreItem = (MasStoreItem)itemObject;				
				Transaction transaction = session.beginTransaction();
				if(masStoreItem.getStatus().equalsIgnoreCase("y")){
					masStoreItem.setStatus("n");
					
				}else if(masStoreItem.getStatus().equalsIgnoreCase("n")) {
					masStoreItem.setStatus("y");
					//result="200";
				}else {
					masStoreItem.setStatus("y");
				}
				
				session.update(masStoreItem);
				transaction.commit();
				session.flush();
				result="200";
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public String addMEMBMaster(MasMedExam masMedExam) {
		String result = "";
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			Serializable savedObj = session.save(masMedExam);
			tx.commit();
			
			if (savedObj != null) {
				result = "200";
			} else {
				result = "500";
			}
			session.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	@Override
	public List<MasMedExam> validateMEMBMaster(String examName,String examCode) {
		List<MasMedExam> masMedExamList = null;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasMedExam.class);
			criteria.add(Restrictions.or(Restrictions.eq("medicalExamName",examName),Restrictions.eq("medicalExamCode", examCode)));
					masMedExamList = criteria.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return masMedExamList;
	}
	
	@Override
	public List<MasMedExam> validateMEMBMasterUpdate(String examName) {
		List<MasMedExam> masMedExamList = null;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasMedExam.class);
			criteria.add(Restrictions.eq("medicalExamName",examName));
					masMedExamList = criteria.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return masMedExamList;
	}
	
	@Override
	public Map<String, List<MasMedExam>> getAllMEMBMaster(JSONObject jsonObj) {
		Map<String, List<MasMedExam>> mapObj = new HashMap<String, List<MasMedExam>>();
		int pageSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
		int pageNo= 0;
		try {	
		List totalMatches = new ArrayList();
		List<MasMedExam> masMEMBList = new ArrayList<MasMedExam>();
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria criteria = session.createCriteria(MasMedExam.class);

		if (jsonObj.get("PN") != null)
			pageNo = Integer.parseInt(jsonObj.get("PN").toString());

		String eName = "";
		if (jsonObj.has("examName")) {
			eName = "%"+jsonObj.get("examName")+"%";
			if (jsonObj.get("examName").toString().length() > 0
					&& !jsonObj.get("examName").toString().trim().equalsIgnoreCase("")) {
				criteria.add(Restrictions.ilike("medicalExamName", eName));
				

			}
		}
		criteria.addOrder(Order.asc("medicalExamName"));
		totalMatches = criteria.list();
		criteria.setFirstResult((pageSize) * (pageNo - 1));
		criteria.setMaxResults(pageSize);

		masMEMBList = criteria.list();		
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		mapObj.put("masMEMBList", masMEMBList);
		mapObj.put("totalMatches", totalMatches);
		}
		catch (Exception e) {
			e.printStackTrace();
		} finally {

			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mapObj;
	}
	
	@Override
	public String updateMEMBMaster(Long membId, String examName,String examCode,Long userid, String onlineOffline) {
		String result = "";
		try {
			List<MasMedExam> membExamList = validateMEMBMasterUpdate(examName);	
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			
			if (membId != 0) {
				Object cmdObject = session.load(MasMedExam.class, membId);
				MasMedExam membExam = (MasMedExam) cmdObject;
				Transaction transaction = session.beginTransaction();				
				
				membExam.setMedicalExamCode(examCode);
				if(membExamList !=null && membExamList.size()>0) {
					membExam.setMedicalExamName(membExam.getMedicalExamName());	
				}
				else {
					membExam.setMedicalExamName(examName);
				}
				membExam.setStatus(membExam.getStatus());
				membExam.setOnlineOffline(onlineOffline);
				Users users = new Users();
				users.setUserId(userid);
				membExam.setUser(users);
				long d = System.currentTimeMillis();
				Date date = new Date(d);
				Timestamp timestamp=new Timestamp(d);
				membExam.setLastChgDate(timestamp);
				session.update(membExam);
				transaction.commit();
				session.flush();
				result = "200";
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return result;
	}
	
	@Override
	public String updateMEMBStatus(Long examId, String status) {
		
		String result = "";
		try {

			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Object membObject = session.get(MasMedExam.class, examId);
			MasMedExam masmed = (MasMedExam) membObject;
			Transaction transaction = session.beginTransaction();
			if (masmed.getStatus().equalsIgnoreCase("y")) {
				masmed.setStatus("n");

			} else if (masmed.getStatus().equalsIgnoreCase("n")) {
				masmed.setStatus("y");

			} else {
				masmed.setStatus("y");
			}

			session.update(masmed);
			transaction.commit();
			session.flush();
	        session.clear();
			result = "200";
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	@Override
	public List<DgMasInvestigation> getInvestigationNameList() {
		List<DgMasInvestigation> invList = new ArrayList<DgMasInvestigation>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(DgMasInvestigation.class);			
			criteria.add(Restrictions.eq("status", "Y").ignoreCase());			
			criteria.addOrder(Order.asc("investigationName"));
			invList = criteria.list();
			

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		getHibernateUtils.getHibernateUtlis().CloseConnection();
	}
		return invList;
	}
	
	@Override
	public String saveMEInvestigation(MasInvestignationMapping masInvestignationMapping) {
		String result="";
		Session session=null;
		Long investigationId=null;
		Transaction tx=null;
		try{
			session= getHibernateUtils.getHibernateUtlis().OpenSession();
			tx=session.beginTransaction();
			Serializable savedObj=session.save(masInvestignationMapping);
			if(savedObj !=null) {
				result="success";
			}
			tx.commit();
			session.flush();
	        session.clear();
	        
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			 
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			
			}
		return result;
	}
	
	@Override
	public Map<String, List<MasInvestignationMapping>> getAllInvestigationMapping(JSONObject jsonObj) {
		Map<String, List<MasInvestignationMapping>> mapObj = new HashMap<String, List<MasInvestignationMapping>>();
		int pageSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
		int pageNo= 0;
		List<MasInvestignationMapping> totalMatches = new ArrayList();
		List<MasInvestignationMapping> meInvestigationList = new ArrayList<MasInvestignationMapping>();
	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria criteria = session.createCriteria(MasInvestignationMapping.class);

		if (jsonObj.get("PN") != null)
			pageNo = Integer.parseInt(jsonObj.get("PN").toString());

		String age = "";
		if (jsonObj.has("age")) {
			age = "%"+jsonObj.get("age")+"%";
			if (jsonObj.get("age").toString().length() > 0
					&& !jsonObj.get("age").toString().trim().equalsIgnoreCase("")) {
				criteria.add(Restrictions.ilike("age", age));
				

			}
		}
		criteria.addOrder(Order.asc("investignationId"));
		totalMatches = criteria.list();
		criteria.setFirstResult((pageSize) * (pageNo - 1));
		criteria.setMaxResults(pageSize);

		meInvestigationList = criteria.list();		
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		mapObj.put("meInvestigationList", meInvestigationList);
		mapObj.put("totalMatches", totalMatches);
		return mapObj;
	}
	
	
	
	@Override
	public String getMasDesignationByDesignationId(String masDesigtionId) {
		List<DgMasInvestigation> listMasDesignation = null;
		String designationName="";
		String designationId="";
		
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			String [] massDesignationArray=masDesigtionId.split(",");
			List<Long>listMasssDesi=new ArrayList<>();
			for(String ss:massDesignationArray) {
				listMasssDesi.add(Long.parseLong(ss.trim()));
			}
			listMasDesignation = session.createCriteria(DgMasInvestigation.class) .add(Restrictions.in("investigationId",listMasssDesi)).
					 list();
			if(CollectionUtils.isNotEmpty(listMasDesignation)) {
				int count=0;
				for(DgMasInvestigation masDesignation:listMasDesignation) {
					if(count==0) {
						designationName=masDesignation.getInvestigationName();
						designationId=""+masDesignation.getInvestigationId();
					}
					else {
						designationName+=","+masDesignation.getInvestigationName();
						designationId+=","+masDesignation.getInvestigationId();
					}
					count++;
				}
			}
			 
		}catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return designationName+"##"+designationId;
	}

	
	
	@Override
	public String updateMEInvestStatus(Long invId, String status) {
		
		String result = "";
		try {

			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Object meinvObject = session.load(MasInvestignationMapping.class, invId);
			MasInvestignationMapping masInv = (MasInvestignationMapping) meinvObject;
			Transaction transaction = session.beginTransaction();
			if (masInv.getStatus().equalsIgnoreCase("y")) {
				masInv.setStatus("n");

			} else if (masInv.getStatus().equalsIgnoreCase("n")) {
				masInv.setStatus("y");

			} else {
				masInv.setStatus("y");
			}

			session.update(masInv);
			transaction.commit();
			session.flush();
			result = "200";
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	@Override
	public String updateInvestigationMapping(Long invMapId, String invId, String age, Long userid) {
		String result = "";
		try {
			List<MasInvestignationMapping> meinvList = new ArrayList<MasInvestignationMapping>();
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();

			if (invMapId != 0) {
				Object meObject = session.load(MasInvestignationMapping.class, invMapId);
				MasInvestignationMapping memap = (MasInvestignationMapping) meObject;
				Transaction transaction = session.beginTransaction();	
				memap.setAge(age);
				memap.setInvestignationId(invId);
				memap.setStatus(memap.getStatus());							
				memap.setLastChgBy(userid);
				long d = System.currentTimeMillis();
				Date date = new Date(d);
				Timestamp timestamp=new Timestamp(d);
				memap.setLastChgDate(timestamp);
				session.update(memap);
				transaction.commit();
				session.flush();
				result = "200";
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return result;
	}
	
		
	@Override
	public List<MasInvestignationMapping> getAllMEMappingList() {
		List<MasInvestignationMapping> masInvList = null;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasInvestignationMapping.class);						
			masInvList = criteria.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return masInvList;
	}
	
	@Override
	public List<MasInvestignationMapping> getAllMEMappingById(Long masInv) {
		List<MasInvestignationMapping> masInvList = null;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasInvestignationMapping.class).add(Restrictions.eq("id",masInv));						
			masInvList = criteria.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return masInvList;
	}
	
	
	
	@Override
	public List<MasMainChargecode> getMainTypeList() {
		List<MasMainChargecode> subTypeList = new ArrayList<MasMainChargecode>();
		try {	 
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria criteria =  session.createCriteria(MasMainChargecode.class);
		criteria.add(Restrictions.eq("status", "Y").ignoreCase());
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("mainChargecodeId").as("mainChargecodeId"));		
		projectionList.add(Projections.property("mainChargecodeName").as("mainChargecodeName"));
		criteria.setProjection(projectionList);
		
		subTypeList = criteria.setResultTransformer(new AliasToBeanResultTransformer(MasMainChargecode.class)).add(Restrictions.eq("status", 'Y').ignoreCase()).addOrder(Order.asc("mainChargecodeName")).list();
		
		}catch(Exception e) {
			e.printStackTrace();
		}
		return subTypeList;
	}
	
	
	@Override
	public List<MasSubChargecode> validateSubType(String masSubChargecodeCode, String masSubChargecodeName) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		List<MasSubChargecode> subtypeList = new ArrayList<MasSubChargecode>();
		Criteria criteria = session.createCriteria(MasSubChargecode.class);
		criteria.add(Restrictions.or(Restrictions.eq("subChargecodeCode", masSubChargecodeCode), Restrictions.eq("subChargecodeName", masSubChargecodeName)));
		subtypeList = criteria.list();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return subtypeList;
	}
	
	@Override
	public String addSubType(MasSubChargecode masSubChargecode) {
		String result="";
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();	
			
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(masSubChargecode);
			tx.commit();
			session.flush();
			if(savedObj!=null) {
				result = "200";
			}else {
				result = "500";
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	@Override
	public Map<String, List<MasSubChargecode>>  getAllSubTypeDetails(JSONObject jsonObj){
		Map<String, List<MasSubChargecode>> mapObj = new HashMap<String, List<MasSubChargecode>>();
		int pageSize = 5;
		int pageNo=1;
		
		List totalMatches = new ArrayList();
		 
		List<MasSubChargecode> subChargecodeList = new ArrayList<MasSubChargecode>();
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasSubChargecode.class);				
			if( jsonObj.get("PN")!=null)
			 pageNo = Integer.parseInt(jsonObj.get("PN").toString());		
			String subtypeName="";
				 if (jsonObj.has("subTypeName"))
				 {
					 subtypeName = "%"+jsonObj.get("subTypeName")+"%";
					  if(jsonObj.get("subTypeName").toString().length()>0 && !jsonObj.get("subTypeName").toString().trim().equalsIgnoreCase("")) {
							criteria.add(Restrictions.ilike("subChargecodeName", subtypeName));
						}
				 }	
				 
			criteria.addOrder(Order.asc("subChargecodeName"));
			totalMatches = criteria.list();
			criteria.setFirstResult((pageSize) * (pageNo - 1));			
			criteria.setMaxResults(pageSize);			
			subChargecodeList=criteria.list();
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			mapObj.put("subChargecodeList", subChargecodeList);
			mapObj.put("totalMatches", totalMatches);
			return mapObj;
		}
	
	@Override
	public String updateSubTypeStatus(Long subtypeId, String status) {
		
		String result = "";
		try {

			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Object subObject = session.load(MasSubChargecode.class, subtypeId);
			MasSubChargecode massub = (MasSubChargecode) subObject;
			Transaction transaction = session.beginTransaction();
			if (massub.getStatus().equalsIgnoreCase("y")) {
				massub.setStatus("n");

			} else if (massub.getStatus().equalsIgnoreCase("n")) {
				massub.setStatus("y");

			} else {
				massub.setStatus("y");
			}

			session.update(massub);
			transaction.commit();
			session.flush();
			result = "200";
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	@Override
	public List<MasSubChargecode> validateSubTypeUpdate(String subTypeCode, String subTypeName) {
		List<MasSubChargecode> subTypeList = new ArrayList<MasSubChargecode>();
		try {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();		
		Criteria criteria = session.createCriteria(MasSubChargecode.class);
		criteria.add(Restrictions.eq("subChargecodeName", subTypeName));
		subTypeList = criteria.list();
		}
		catch (Exception e) {
			
			e.printStackTrace();
		} finally {
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return subTypeList;
	}
	
	
	@Override
	public String updateSubTypeDetails(Long subChargecodeId, String subChargecodeCode, String subChargecodeName, Long mainTypeId,Long userId) {	
		String result="";
		try {
			
			List<MasSubChargecode> mainChargecodeList = validateSubTypeUpdate(subChargecodeCode,subChargecodeName);
					
			
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			MasSubChargecode masSubChargecode =  (MasSubChargecode)session.get(MasSubChargecode.class, subChargecodeId);	
				
				if(masSubChargecode != null)
				{
				
				Transaction transaction = session.beginTransaction();
				masSubChargecode.setSubChargecodeCode(subChargecodeCode);
				if(mainChargecodeList !=null && mainChargecodeList.size()>0) {
				masSubChargecode.setSubChargecodeName(masSubChargecode.getSubChargecodeName());
				}
				else {
					masSubChargecode.setSubChargecodeName(subChargecodeName);	
				}
				masSubChargecode.setStatus(masSubChargecode.getStatus());				
				masSubChargecode.setLastChgBy(userId);
				masSubChargecode.setMainChargecodeId(mainTypeId);								
				long d = System.currentTimeMillis();
				Timestamp date = new Timestamp(d);				
				masSubChargecode.setLastChgDate(date);				
				session.update(masSubChargecode);				
				transaction.commit();
				session.flush();
				result="200";	
				}					
				
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
		
		
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		
		
		return result;
	}
	
	/****************************Vendor Type*********************************************************/
	@Override
	public Map<String, List<MasMmuType>> getAllVendorType(JSONObject jsondata) {
		Map<String, List<MasMmuType>> map = new HashMap<String, List<MasMmuType>>();
		List<MasMmuType> vendorTypeList = new ArrayList<MasMmuType>();
		int pageNo=1;
		int pageSize = 5;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasMmuType.class);
		
			if( jsondata.get("PN")!=null)
			 pageNo = Integer.parseInt(jsondata.get("PN").toString());
					
			String vtName="";
				 if (jsondata.has("supplierTypeName"))
				 {
					 vtName = "%"+jsondata.get("supplierTypeName")+"%";
					  if(jsondata.get("supplierTypeName").toString().length()>0 && !jsondata.get("supplierTypeName").toString().trim().equalsIgnoreCase("")) {
							criteria.add(Restrictions.ilike("mmuTypeName", vtName));
						}
				 }
				 criteria.addOrder(Order.asc("mmuTypeName"));
				 List totalMatches = criteria.list();
				 
				 if( !jsondata.get("PN").equals("0"))
					{
					 criteria.setFirstResult((pageSize) * (pageNo - 1));
					 criteria.setMaxResults(pageSize);
					 vendorTypeList = criteria.list();
					}
				 if( jsondata.get("PN").equals("0")) {
					criteria.add(Restrictions.eq("status", "y").ignoreCase());
					criteria.setMaxResults(pageSize);
					vendorTypeList = criteria.list();
					}
			
		map.put("vendorTypeList", vendorTypeList);
		map.put("totalMatches", totalMatches);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@Override
	public List<MasMmuType> validateVendorType(String supplierTypeCode, String supplierTypeName) {
		List<MasMmuType> vendorTypeList =  new ArrayList<MasMmuType>();	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria = session.createCriteria(MasMmuType.class);
				criteria.add(Restrictions.or(Restrictions.eq("mmuTypeCode", supplierTypeCode), 
						Restrictions.eq("mmuTypeName", supplierTypeName)));
				vendorTypeList = criteria.list();
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		return vendorTypeList;
	}

	@Override
	public List<MasMmuType> validateVendorTypeUpdate(String supplierTypeCode, String supplierTypeName) {
		List<MasMmuType> vendorTypeList =  new ArrayList<MasMmuType>();	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria = session.createCriteria(MasMmuType.class);
				criteria.add(Restrictions.or(Restrictions.eq("mmuTypeCode", supplierTypeCode), Restrictions.eq("mmuTypeName", supplierTypeName)));
				vendorTypeList = criteria.list();
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		return vendorTypeList;
	}

	@Override
	public String addVendorType(MasMmuType masStoreSupplierType) {
		String result="";	
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(masStoreSupplierType);
			tx.commit();
			if(savedObj!=null) {
				result="200";
			}else {
				result = "500";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		
		return result;
	}

	@Override
	public String updateVendorTypeDetails(Long supplierTypeId, String supplierTypeCode, String supplierTypeName, Long userId) {
		String result="";
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
					Object object = session.load(MasMmuType.class, supplierTypeId);
					MasMmuType masStoreSupplierType = (MasMmuType)object;
					
					Transaction transaction = session.beginTransaction();
					masStoreSupplierType.setMmuTypeCode(supplierTypeCode.toUpperCase());
					masStoreSupplierType.setMmuTypeName(supplierTypeName.toUpperCase());					
					masStoreSupplierType.setLastChgBy(userId);				
					long d = System.currentTimeMillis();
					Timestamp date = new Timestamp(d);
					masStoreSupplierType.setLastChgDate(date);
					session.update(masStoreSupplierType);
					transaction.commit();
					
					result = "200";
				//}
			//}
											
		}catch(Exception e) {
			
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public MasMmuType checkVendorType(String supplierTypeCode) {
		MasMmuType mStoreSupplierType = new MasMmuType();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasMmuType.class);		
			criteria.add(Restrictions.eq("mmuTypeCode", supplierTypeCode));
			mStoreSupplierType = (MasMmuType)criteria.uniqueResult();
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mStoreSupplierType;
	}

	@Override
	public String updateVendorTypeStatus(Long supplierTypeId, String supplierTypeCode, String status, Long userId) {
		String result = "";	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Object object =  session.load(MasMmuType.class, supplierTypeId);
				
				MasMmuType masStoreSupplierType = (MasMmuType)object;
				Transaction transaction = session.beginTransaction();
				
				
				if(masStoreSupplierType.getStatus().equalsIgnoreCase("Y") || masStoreSupplierType.getStatus().equalsIgnoreCase("y")) {
					masStoreSupplierType.setStatus("N");
				}else if(masStoreSupplierType.getStatus().equalsIgnoreCase("N") || masStoreSupplierType.getStatus().equalsIgnoreCase("n")) {
					masStoreSupplierType.setStatus("Y");
				}else {
					masStoreSupplierType.setStatus("Y");
				}
				session.update(masStoreSupplierType);
				transaction.commit();
				result = "200";
			//}
			
		}catch(Exception e) {
			
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	/****************************MAS Sample Container*********************************************************/
	
	
	@Override
	public String validateSampleContainer(String collectionCode, String collectionName) {
		List<DgMasCollection> collectionList1 =  new ArrayList<DgMasCollection>();
		List<DgMasCollection> collectionList2 =  new ArrayList<DgMasCollection>();
		String result="";
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria1 = session.createCriteria(DgMasCollection.class);
				Criteria criteria2 = session.createCriteria(DgMasCollection.class);
				criteria1.add(Restrictions.eq("collectionCode", collectionCode));
				criteria2.add(Restrictions.eq("collectionName", collectionName));	
				
				collectionList1 = criteria1.list();
				collectionList2 = criteria2.list();
				if(collectionList1.size()>0 && collectionList1 !=null) {
					result="codeExists";
				}
				else if(collectionList2.size()>0 && collectionList2 !=null) {
					result="nameExists";
				}
				
				
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		return result;
	}
	
	@Override
	public String addSampleContainer(DgMasCollection masCollection) {
		String result="";
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(masCollection);
			tx.commit();
			
			if(savedObj!=null) {
				result="200";
			}else {
				result = "500";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		
		return result;
	}
	
	
	
	@Override
	public Map<String, List<DgMasCollection>> getAllSampleContainer(JSONObject jsondata) {
		Map<String, List<DgMasCollection>> map = new HashMap<String, List<DgMasCollection>>();
		List<DgMasCollection> sampleContainerList = new ArrayList<DgMasCollection>();
		int pageNo=1;
		int pageSize = 5;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(DgMasCollection.class);
			List totalMatches=new ArrayList();
					
			if( jsondata.get("PN")!=null)
			 pageNo = Integer.parseInt(jsondata.get("PN").toString());
					
			String scName="";
				 if (jsondata.has("collectionName"))
				 {
					  scName = "%"+jsondata.get("collectionName")+"%";
					  if(jsondata.get("collectionName").toString().length()>0 && !jsondata.get("collectionName").toString().trim().equalsIgnoreCase("")) {
							criteria.add(Restrictions.ilike("collectionName", scName));
							
						}
				 }
				criteria.addOrder(Order.asc("collectionName"));
				 totalMatches = criteria.list();				 
				 criteria.setFirstResult((pageSize) * (pageNo - 1));
				 criteria.setMaxResults(pageSize);
				 sampleContainerList = criteria.list();
			
			
		map.put("sampleContainerList", sampleContainerList);
		map.put("totalMatches", totalMatches);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}
	
	@Override
	public String updateSampleContainerStatus(Long collectionId, String status) {
		
		String result = "";
		try {

			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Object dgObject = session.load(DgMasCollection.class, collectionId);
			DgMasCollection dgcollection = (DgMasCollection) dgObject;
			Transaction transaction = session.beginTransaction();
			if (dgcollection.getStatus().equalsIgnoreCase("y")) {
				dgcollection.setStatus("n");

			} else if (dgcollection.getStatus().equalsIgnoreCase("n")) {
				dgcollection.setStatus("y");

			} else {
				dgcollection.setStatus("y");
			}

			session.update(dgcollection);
			transaction.commit();
			session.flush();
			result = "200";
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	
	@Override
	public String updateSampleContainer(Long collectionId, String collectionCode, String collectionName,Long userId) {
		String result="";
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
					Object object = session.load(DgMasCollection.class, collectionId);
					DgMasCollection dgmas = (DgMasCollection)object;					
					Transaction transaction = session.beginTransaction();
					dgmas.setCollectionCode(collectionCode.toUpperCase());
					dgmas.setCollectionName(collectionName.toUpperCase());
					dgmas.setLastChgBy(userId);			
					long d = System.currentTimeMillis();
					Timestamp date = new Timestamp(d);
					dgmas.setLastChgDate(date);
					dgmas.setStatus(dgmas.getStatus());
					session.update(dgmas);
					transaction.commit();
					session.flush();
					result="success";
				
											
		}catch(Exception e) {
			
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	
	@Override
	public String validateSampleContainerName(String collectionName) {
		List<DgMasCollection> collectionList =  new ArrayList<DgMasCollection>();		
		String result="";
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria = session.createCriteria(DgMasCollection.class);					
				criteria.add(Restrictions.eq("collectionName", collectionName));
				collectionList = criteria.list();				
				if(collectionList.size()>0 && collectionList !=null) {
					result="nameExists";
				}
			
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		return result;
	}

    /****************************Investigation UOM  Master***********************************************/
	
	
	@Override
	public List<MasUOM> validateInvestigationUOM(String uomCode, String uomName) {
		List<MasUOM> uomList =  new ArrayList<MasUOM>();	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria = session.createCriteria(MasUOM.class);
				criteria.add(Restrictions.or(Restrictions.eq("UOMCode", uomCode), Restrictions.eq("UOMName", uomName)));
				uomList = criteria.list();
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		return uomList;
	}
	
	@Override
	public String addInvestigationUOM(MasUOM dguom) {
		String result="";
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(dguom);
			tx.commit();
			
			if(savedObj!=null) {
				result="200";
			}else {
				result = "500";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
	
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
		
	}
	/****************************Department Type*********************************************************/
	@Override
	public Map<String, List<MasDepartmentType>> getAllDepartmentType(JSONObject jsondata) {
		Map<String, List<MasDepartmentType>> map = new HashMap<String, List<MasDepartmentType>>();
		List<MasDepartmentType> departmentTypeList = new ArrayList<MasDepartmentType>();
		int pageNo=1;
		int pageSize = 5;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasDepartmentType.class);
			
					
			if( jsondata.get("PN")!=null)
			 pageNo = Integer.parseInt(jsondata.get("PN").toString());
					
			String vtName="";
				 if (jsondata.has("departmentTypeName"))
				 {
					  vtName = "%"+jsondata.get("departmentTypeName")+"%";
					  if(jsondata.get("departmentTypeName").toString().length()>0 && !jsondata.get("departmentTypeName").toString().trim().equalsIgnoreCase("")) {
							criteria.add(Restrictions.ilike("departmentTypeName", vtName));
							//criteria.addOrder(Order.asc(jsondata.get("religionName").toString()));
						}
				 }
				 List totalMatches = criteria.list();
				 
				 criteria.setFirstResult((pageSize) * (pageNo - 1));
				 criteria.setMaxResults(pageSize);
				 departmentTypeList = criteria.list();
			
		map.put("departmentTypeList", departmentTypeList);
		map.put("totalMatches", totalMatches);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@Override
	public List<MasDepartmentType> validateDepartmentType(String departmentTypeCode, String departmentTypeName) {
		List<MasDepartmentType> departmentTypeList =  new ArrayList<MasDepartmentType>();	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria = session.createCriteria(MasDepartmentType.class);
				criteria.add(Restrictions.or(Restrictions.eq("departmentTypeCode", departmentTypeCode), 
						Restrictions.eq("departmentTypeName", departmentTypeName)));
				departmentTypeList = criteria.list();
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		return departmentTypeList;
	}

	@Override
	public List<MasDepartmentType> validateDepartmentTypeUpdate(String departmentTypeCode, String departmentTypeName) {
		List<MasDepartmentType> departmentTypeList =  new ArrayList<MasDepartmentType>();	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria = session.createCriteria(MasDepartmentType.class);
				criteria.add(Restrictions.and(Restrictions.eq("departmentTypeCode", departmentTypeCode), Restrictions.eq("departmentTypeName", departmentTypeName)));
				departmentTypeList = criteria.list();
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		return departmentTypeList;
	}

	@Override
	public String addDepartmentType(MasDepartmentType masDepartmentType) {
		String result="";	
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(masDepartmentType);
			tx.commit();
			if(savedObj!=null) {
				result="200";
			}else {
				result = "500";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		
		return result;
	}

	@Override
	public String updateDepartmentTypeDetails(Long departmentTypeId, String departmentTypeCode, String departmentTypeName, Long userId) {
		String result="";
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
					Object object = session.load(MasDepartmentType.class, departmentTypeId);
					MasDepartmentType masDepartmentType = (MasDepartmentType)object;
					
					Transaction transaction = session.beginTransaction();
					masDepartmentType.setDepartmentTypeCode(departmentTypeCode.toUpperCase());
					masDepartmentType.setDepartmentTypeName(departmentTypeName.toUpperCase());			
									
					Users users = new Users();
					users.setUserId(userId);
					masDepartmentType.setUser(users);				
					long d = System.currentTimeMillis();
					Timestamp date = new Timestamp(d);
					masDepartmentType.setLastChgDate(date);
					session.update(masDepartmentType);
					transaction.commit();
					
					result = "200";
				//}
			//}
											
		}catch(Exception e) {
			
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public MasDepartmentType checkDepartmentType(String departmentTypeCode) {
		MasDepartmentType mDepartmentType = new MasDepartmentType();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasDepartmentType.class);		
			criteria.add(Restrictions.eq("departmentTypeCode", departmentTypeCode));
			mDepartmentType = (MasDepartmentType)criteria.uniqueResult();
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mDepartmentType;
	}

	@Override
	public String updateDepartmentTypeStatus(Long departmentTypeId, String departmentTypeCode, String status, Long userId) {
		String result = "";	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Object object =  session.load(MasDepartmentType.class, departmentTypeId);
				
				MasDepartmentType masDepartmentType = (MasDepartmentType)object;
				Transaction transaction = session.beginTransaction();
				
				
				if(masDepartmentType.getStatus().equalsIgnoreCase("Y") || masDepartmentType.getStatus().equalsIgnoreCase("y")) {
					masDepartmentType.setStatus("N");
				}else if(masDepartmentType.getStatus().equalsIgnoreCase("N") || masDepartmentType.getStatus().equalsIgnoreCase("n")) {
					masDepartmentType.setStatus("Y");
				}else {
					masDepartmentType.setStatus("Y");
				}
				session.update(masDepartmentType);
				transaction.commit();
				result = "200";
			//}
			
		}catch(Exception e) {
			
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	/***************************Vendor Master****************************/
	@Override
	public List<MasStoreSupplierNew> validateMasStoreSupplier(String supplierCode,String supplierName) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria criteria = null;
		criteria = session.createCriteria(MasStoreSupplierNew.class)
							.add(Restrictions.or(Restrictions.eq("supplierCode", supplierCode).ignoreCase(),Restrictions.eq("supplierName", supplierName).ignoreCase()));
		
		List<MasStoreSupplierNew> storeSupplierist = criteria.list();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return storeSupplierist;
	}

	@Override
	public List<MasStoreSupplierNew> validateMasStoreSupplierUpdate(String supplierCode,String pinNo, String tinNo,String licenceNo, String mobileno) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria criteria = null;
		criteria = session.createCriteria(MasStoreSupplierNew.class)
							.add(Restrictions.and(Restrictions.eq("supplierCode", supplierCode).ignoreCase(),Restrictions.eq("pinNo", pinNo), Restrictions.eq("tinNo", tinNo),
									Restrictions.eq("licenceNo", licenceNo),Restrictions.eq("mobileno", mobileno)));
		List<MasStoreSupplierNew> storeSupplierist = criteria.list();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return storeSupplierist;
	}

	@Override
	public String addMasStoreSupplier(MasStoreSupplierNew masStoreSupplier) {
		
		String result="";
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(masStoreSupplier); 
			tx.commit();
			if(savedObj!=null) {
				result="200";
			}else {
				result = "500";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		
		return result;
	}

	@Override
	public MasStoreSupplier chkStoreSupplier(String pinNo, String tinNo,String licenceNo, String mobileno) {
		MasStoreSupplier mStoreSupplier = new MasStoreSupplier();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasStoreSupplier.class);		
			criteria.add(Restrictions.eq("pinNo", pinNo));
			criteria.add(Restrictions.eq("tinNo", tinNo));
			criteria.add(Restrictions.eq("licenceNo", licenceNo));
			criteria.add(Restrictions.eq("mobileno", mobileno));
			mStoreSupplier = (MasStoreSupplier)criteria.uniqueResult();
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mStoreSupplier;
	}

	@Override
	public Map<String, List<MasStoreSupplierNew>> getAllVendor(JSONObject jsonObj) {
		Map<String, List<MasStoreSupplierNew>> mapObj = new HashMap<String, List<MasStoreSupplierNew>>();
		int pageSize = 5;
		int pageNo=0;
		
		List totalMatches = new ArrayList();
		 
		List<MasStoreSupplierNew> mmuVendorList = new ArrayList<MasStoreSupplierNew>();
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasStoreSupplierNew.class);
			if( jsonObj.get("PN")!=null)
			 pageNo = Integer.parseInt(jsonObj.get("PN").toString());	
			String supplierName="";
				 if (jsonObj.has("supplierName"))
				 {
					 supplierName = "%"+jsonObj.get("supplierName")+"%";
					  if(jsonObj.get("supplierName").toString().length()>0 && !jsonObj.get("supplierName").toString().trim().equalsIgnoreCase("")) {
							criteria.add(Restrictions.ilike("supplierName", supplierName));
						}
				 }	
				 
			
			criteria.addOrder(Order.asc("supplierName"));
			
			totalMatches = criteria.list();
					
			criteria.setFirstResult((pageSize) * (pageNo - 1));
			criteria.setMaxResults(pageSize);
			mmuVendorList = criteria.list();
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			mapObj.put("mmuVendorList", mmuVendorList);
			mapObj.put("totalMatches", totalMatches);
			return mapObj;
	}

	@Override
	public String updateStoreSupplier(JSONObject jsondata) {
		
		String result="";
		
		List<MasStoreSupplierNew> msVendorList = validateMasStoreSupplier(jsondata.get("supplierCode").toString(),
				jsondata.get("supplierName").toString());		
		
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
					MasState masState=new MasState();
					MasState localmasState=new MasState();
					MasDistrict masDistrict=new MasDistrict();
					MasDistrict localmasDistrict=new MasDistrict();
					Long supplierId=Long.parseLong(jsondata.get("supplierId").toString());
					Object object = session.load(MasStoreSupplierNew.class, supplierId);
					MasStoreSupplierNew masStoreSupplier = (MasStoreSupplierNew)object;					
					
					Transaction transaction = session.beginTransaction();
					
					masStoreSupplier.setSupplierTypeId(Long.parseLong(jsondata.get("supplierTypeId").toString()));
					if(msVendorList !=null && !msVendorList.isEmpty()) {
					    masStoreSupplier.setSupplierName(masStoreSupplier.getSupplierName());	
					}
					else {
						masStoreSupplier.setSupplierName(jsondata.get("supplierName").toString());	
					}
					masStoreSupplier.setSupplierCode(jsondata.get("supplierCode").toString());
					
					if(jsondata.get("pinNo").toString() !=null && !jsondata.get("pinNo").toString().isEmpty()) {
					masStoreSupplier.setPinNo(jsondata.get("pinNo").toString());
					}
					masStoreSupplier.setTinNo(jsondata.get("tinNo").toString());
					masStoreSupplier.setLicenceNo(jsondata.get("licenceNo").toString());
					masStoreSupplier.setPanNumber(jsondata.get("panNo").toString());
										
					if(jsondata.get("address1").toString() !=null && !jsondata.get("address1").toString().isEmpty())
					{
						masStoreSupplier.setAddress1(jsondata.get("address1").toString());	
					}
					
					if(jsondata.get("address2").toString() !=null && !jsondata.get("address2").toString().isEmpty()) {
						masStoreSupplier.setAddress2(jsondata.get("address2").toString());
					}
					
					if(jsondata.has("stateId")) {
					if(jsondata.get("stateId").toString() !=null && !jsondata.get("stateId").toString().equals("")) {
						masState.setStateId(Long.parseLong(jsondata.get("stateId").toString()));
						masStoreSupplier.setState(masState);
					}
					}
					if(jsondata.has("districtId")) {
					if(jsondata.get("districtId").toString() !=null && !jsondata.get("districtId").toString().equals(""))
					{
						masDistrict.setDistrictId(Long.parseLong(jsondata.get("districtId").toString()));
						masStoreSupplier.setCity(masDistrict);
					}
					}
					
					if(jsondata.get("phoneno").toString() !=null &&  !jsondata.get("phoneno").toString().isEmpty()) {
						masStoreSupplier.setPhoneno(jsondata.get("phoneno").toString());	
					}
										
					if(jsondata.get("mobileno").toString() !=null && !jsondata.get("mobileno").toString().equals("")) {
					masStoreSupplier.setMobileno(jsondata.get("mobileno").toString());
					}
					
					if(jsondata.get("pinCode").toString() !=null  && !jsondata.get("pinCode").toString().equals("")) {
						masStoreSupplier.setPinCode(Long.parseLong(jsondata.get("pinCode").toString()));
					}
										
					if(jsondata.get("emailid").toString() !=null && !jsondata.get("emailid").toString().isEmpty() ) {
						masStoreSupplier.setEmailid(jsondata.get("emailid").toString() );
					}
					
					if(jsondata.get("faxnumber").toString() !=null && !jsondata.get("faxnumber").toString().isEmpty() ) {
						masStoreSupplier.setFaxnumber(jsondata.get("faxnumber").toString());
					}
					
					//masStoreSupplier.setUrl(url);
					if(jsondata.get("localAddress1").toString() !=null  && !jsondata.get("localAddress1").toString().isEmpty()) {
						masStoreSupplier.setLocalAddress1(jsondata.get("localAddress1").toString());
					}
					
					if(jsondata.get("localAddress2").toString() !=null && !jsondata.get("localAddress2").toString().isEmpty() ) {
						masStoreSupplier.setLocalAddress2(jsondata.get("localAddress2").toString());
					}
										
					if(jsondata.get("localPhoneNo").toString() !=null && !jsondata.get("localPhoneNo").toString().isEmpty()) {
						masStoreSupplier.setLocalPhoneNo(jsondata.get("localPhoneNo").toString());
					}
					
					if(jsondata.has("localstateId")) {
					if(jsondata.get("localstateId").toString() !=null && !jsondata.get("localstateId").toString().equals("")) {
						
						localmasState.setStateId(Long.parseLong(jsondata.get("localstateId").toString()));	
						masStoreSupplier.setMasState(localmasState);
						
					}
					}
					if(jsondata.has("localdistrictId")) {
					if(jsondata.get("localdistrictId").toString() !=null && !jsondata.get("localdistrictId").toString().equals(""))
					{
						localmasDistrict.setDistrictId(Long.parseLong(jsondata.get("localdistrictId").toString()));
						masStoreSupplier.setLocalCity(localmasDistrict);
					}
					
					}
					if(jsondata.get("localPinCode").toString() !=null  && !jsondata.get("localPinCode").toString().equals("")) {
						masStoreSupplier.setLocalPinCode(Long.parseLong(jsondata.get("localPinCode").toString()));
					}
										
					masStoreSupplier.setStatus(jsondata.get("status").toString());					
					Users user = new Users();
					user.setUserId(Long.parseLong(jsondata.get("userId").toString()));
					masStoreSupplier.setLastChgBy(user);				
					long d = System.currentTimeMillis();
					Timestamp date = new Timestamp(d);
					masStoreSupplier.setLastChgDate(date);
					session.update(masStoreSupplier);
					transaction.commit();
					session.clear();
					session.flush();
					result = "200";
			
											
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public String updateStoreSupplierStatus(Long supplierId, String supplierCode, String status,Long userId) {
		String result = "";
		try {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Object vendorObject =  session.load(MasStoreSupplierNew.class, supplierId);
				MasStoreSupplierNew masStoreSupplier = (MasStoreSupplierNew)vendorObject;				
				Transaction transaction = session.beginTransaction();
				if(masStoreSupplier.getStatus().equalsIgnoreCase("y")){
					masStoreSupplier.setStatus("n");
					
				}else if(masStoreSupplier.getStatus().equalsIgnoreCase("n")) {
					masStoreSupplier.setStatus("y");
					
				}else {
					masStoreSupplier.setStatus("y");
				}
				
				session.update(masStoreSupplier);
				transaction.commit();
				session.flush();
				session.clear();
				result="200";
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	@Override
	public List<MasState> getStateList() {
		List<MasState> stateList = new ArrayList<MasState>();
		try {	 
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria criteria =  session.createCriteria(MasState.class);
		criteria.addOrder(Order.asc("stateName"));		
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("stateId").as("stateId"));		
		projectionList.add(Projections.property("stateName").as("stateName"));
		criteria.setProjection(projectionList);		
		stateList = criteria.setResultTransformer(new AliasToBeanResultTransformer(MasState.class)).list();
		
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return stateList;
	}
	
	@Override
	public Map<String,List<MasDistrict>> getDistrictList(JSONObject jsondata) {
		Map<String,List<MasDistrict>> map = new HashMap<String,List<MasDistrict>>();
		List<MasDistrict> cityList=new ArrayList<MasDistrict>();
		List<MasDistrict> localcityList=new ArrayList<MasDistrict>();
		try {	 
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();		                     
		Criteria criteria1 = session.createCriteria(MasDistrict.class); 
		Criteria criteria2 = session.createCriteria(MasDistrict.class); 
		
		
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("districtId").as("districtId"));
		projectionList.add(Projections.property("districtName").as("districtName"));
		criteria1.setProjection(projectionList);	
		criteria2.setProjection(projectionList);
		if(jsondata.has("stateId")) {
		if(!jsondata.get("stateId").toString().equals("") && jsondata.get("stateId").toString() !=null ) {
			Long stateId=Long.parseLong(jsondata.get("stateId").toString());
			criteria1.createAlias("masState", "ms");
			criteria1.addOrder(Order.asc("districtName"));
			criteria1.add(Restrictions.eq("ms.stateId", stateId));
			cityList = criteria1.setResultTransformer(new AliasToBeanResultTransformer(MasDistrict.class)).list();
		}
		}
		if(jsondata.has("localStateId")) {
		if(!jsondata.get("localStateId").toString().equals("") && jsondata.get("localStateId").toString() !=null ) {
			Long localstateId=Long.parseLong(jsondata.get("localStateId").toString());
			criteria2.createAlias("masState", "ms");
			criteria1.addOrder(Order.asc("districtName"));
			criteria2.add(Restrictions.eq("ms.stateId", localstateId));
			localcityList = criteria2.setResultTransformer(new AliasToBeanResultTransformer(MasDistrict.class)).list();
		}
		}		
		
		map.put("cityList",cityList);
		map.put("localcityList",localcityList);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}
	
	
	@Override
	public List<MasDistrict> getDistrictListById(JSONObject jsondata) {
		List<MasDistrict> cityList=new ArrayList<MasDistrict>();
		
		try {	 
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();		                     
		Criteria criteria = session.createCriteria(MasDistrict.class); 		
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("districtId").as("districtId"));
		projectionList.add(Projections.property("districtName").as("districtName"));
		criteria.setProjection(projectionList);	
		
		if(jsondata.has("stateId")) {
		if(!jsondata.get("stateId").toString().equals("") && jsondata.get("stateId").toString() !=null ) {
			Long stateId=Long.parseLong(jsondata.get("stateId").toString());
			criteria.createAlias("masState", "ms");
			criteria.addOrder(Order.asc("districtName"));
			criteria.add(Restrictions.eq("ms.stateId", stateId));
			criteria.add(Restrictions.eq("status", "y").ignoreCase());
			cityList = criteria.setResultTransformer(new AliasToBeanResultTransformer(MasDistrict.class)).list();
		}
		}
		
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return cityList;
	}
	
	/******************************* END ********************************/
	@Override
	public List<MasHospital> getHospitalListByRegion() {
		List<MasHospital> hosptList = new ArrayList<MasHospital>();
		try {	 
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria criteria =  session.createCriteria(MasHospital.class);
		criteria.add(Restrictions.eq("status", "Y").ignoreCase().ignoreCase());
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("hospitalId").as("hospitalId"));		
		projectionList.add(Projections.property("hospitalName").as("hospitalName"));
		criteria.setProjection(projectionList);	
		criteria.addOrder(Order.asc("hospitalName"));
		hosptList = criteria.setResultTransformer(new AliasToBeanResultTransformer(MasHospital.class)).list();
		
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return hosptList;
	}

	@Override
	public String getMasHospitalById(String hosptId) {
		List<MasHospital> listMasDesignation = null;
		String miRoomName="";
		String miRoomId="";
		
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			String [] masHospitaArray=hosptId.split(",");
			List<Long>listMasssDesi=new ArrayList<>();
			for(String ss:masHospitaArray) {
				listMasssDesi.add(Long.parseLong(ss.trim()));
			}
			listMasDesignation = session.createCriteria(MasHospital.class) .add(Restrictions.in("hospitalId",listMasssDesi)).list();
			if(CollectionUtils.isNotEmpty(listMasDesignation)) {
				int count=0;
				for(MasHospital masHospital:listMasDesignation) {
					if(count==0) {
						
						miRoomName=masHospital.getHospitalName();
						miRoomId=""+masHospital.getHospitalId();
					}
					else {
						miRoomName+=","+masHospital.getHospitalName();
						miRoomId+=","+masHospital.getHospitalId();
					}
					count++;
				}
			}
			 
		}catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return miRoomName+"##"+miRoomId;
	}

	@Override
	public Map<String, List<MasUOM>> getAllInvestigationUOM(JSONObject jsondata) {
		Map<String, List<MasUOM>> map = new HashMap<String, List<MasUOM>>();
		List<MasUOM> uomList = new ArrayList<MasUOM>();
		int pageNo=1;
		int pageSize = 5;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasUOM.class);
			List totalMatches=new ArrayList();
					
			if( jsondata.get("PN")!=null)
			 pageNo = Integer.parseInt(jsondata.get("PN").toString());
					
			String uomName="";
				 if (jsondata.has("uomName"))
				 {
					  uomName = "%"+jsondata.get("uomName")+"%";
					  if(jsondata.get("uomName").toString().length()>0 && !jsondata.get("uomName").toString().trim().equalsIgnoreCase("")) {
							criteria.add(Restrictions.ilike("UOMName", uomName));
							
						}
				 }
				 criteria.addOrder(Order.asc("UOMName"));
				 totalMatches = criteria.list();				 
				 criteria.setFirstResult((pageSize) * (pageNo - 1));
				 criteria.setMaxResults(pageSize);
				 uomList = criteria.list();			
			
		map.put("uomList", uomList);
		map.put("totalMatches", totalMatches);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@Override
	public String updateInvestigationUOMStatus(Long uomId, String status) {
		
		String result = "";
		try {

			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Object dgObject = session.load(MasUOM.class, uomId);
			MasUOM dguom = (MasUOM) dgObject;
			Transaction transaction = session.beginTransaction();
			if (dguom.getUOMStatus().equalsIgnoreCase("y")) {
				dguom.setUOMStatus("n");

			} else if (dguom.getUOMStatus().equalsIgnoreCase("n")) {
				dguom.setUOMStatus("y");

			} else {
				dguom.setUOMStatus("y");
			}

			session.update(dguom);
			transaction.commit();
			session.flush();
			result = "200";
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public String updateInvestigationUOM(Long uomId, String uomCode, String uomName,Long userId) {
		String result="";
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
					Object object = session.load(MasUOM.class, uomId);
					MasUOM dgUom = (MasUOM)object;					
					Transaction transaction = session.beginTransaction();
					dgUom.setUOMCode(uomCode);
					dgUom.setUOMName(uomName);
					Users users=new Users();
					users.setUserId(userId);
					dgUom.setUser(users);			
					long d = System.currentTimeMillis();
					Timestamp date = new Timestamp(d);
					dgUom.setLastChgDate(date);
					dgUom.setUOMStatus(dgUom.getUOMStatus());
					session.update(dgUom);
					transaction.commit();
					session.flush();
					result="success";
				
											
		}catch(Exception e) {
			
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	@Override
	public List<MasUOM> validateInvestigationUOMName(String uomName) {
		List<MasUOM> uomList =  new ArrayList<MasUOM>();	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria = session.createCriteria(MasUOM.class);
				criteria.add(Restrictions.eq("UOMName", uomName));
				uomList = criteria.list();
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		return uomList;
	}

	
/****************************Investigation Master ***********************************************/
	
	@Override
	public List<MasMainChargecode> getAllMainChargeList() {
		List<MasMainChargecode> mcList = new ArrayList<MasMainChargecode>();
		try {	 
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria criteria =  session.createCriteria(MasMainChargecode.class);
		criteria.add(Restrictions.eq("status", "Y").ignoreCase());
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("mainChargecodeId").as("mainChargecodeId"));		
		projectionList.add(Projections.property("mainChargecodeCode").as("mainChargecodeCode"));
		projectionList.add(Projections.property("mainChargecodeName").as("mainChargecodeName"));
		criteria.setProjection(projectionList);		
		mcList = criteria.setResultTransformer(new AliasToBeanResultTransformer(MasMainChargecode.class)).list();
		
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}		
		
		return mcList;
	}
	
	@Override
	public List<MasSubChargecode> getAllModalityList(JSONObject json) {
		List<MasSubChargecode> scList = new ArrayList<MasSubChargecode>();
		try {	 
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria criteria =  session.createCriteria(MasSubChargecode.class);
		criteria.createAlias("masMainChargecode", "mmc");
		criteria.add(Restrictions.eq("mmc.mainChargecodeCode", json.get("mainChargecode").toString()));
		criteria.add(Restrictions.eq("status", "Y").ignoreCase());
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("subChargecodeId").as("subChargecodeId"));		
		projectionList.add(Projections.property("subChargecodeName").as("subChargecodeName"));
		criteria.setProjection(projectionList);		
		scList = criteria.setResultTransformer(new AliasToBeanResultTransformer(MasSubChargecode.class)).list();
		
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return scList;
	}
	
	@Override
	public List<MasSample> getAllSampleList() {
		List<MasSample> sampleList = new ArrayList<MasSample>();
		try {	 
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria criteria =  session.createCriteria(MasSample.class);
		criteria.add(Restrictions.eq("status", "Y").ignoreCase());
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("sampleId").as("sampleId"));		
		projectionList.add(Projections.property("sampleDescription").as("sampleDescription"));
		criteria.setProjection(projectionList);		
		sampleList = criteria.setResultTransformer(new AliasToBeanResultTransformer(MasSample.class)).list();
		
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return sampleList;
	}
	
	@Override
	public List<DgMasCollection> getAllCollectionList() {
		List<DgMasCollection> collectionList = new ArrayList<DgMasCollection>();
		try {	 
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria criteria =  session.createCriteria(DgMasCollection.class);
		criteria.add(Restrictions.eq("status", "Y").ignoreCase());
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("collectionId").as("collectionId"));		
		projectionList.add(Projections.property("collectionName").as("collectionName"));
		criteria.setProjection(projectionList);		
		collectionList = criteria.setResultTransformer(new AliasToBeanResultTransformer(DgMasCollection.class)).list();
		
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return collectionList;
	}
	
	@Override
	public List<MasUOM> getAllUOMList() {
		List<MasUOM> uomList = new ArrayList<MasUOM>();
		try {	 
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria criteria =  session.createCriteria(MasUOM.class);
		criteria.add(Restrictions.eq("UOMStatus", "Y").ignoreCase());
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("UOMId").as("UOMId"));		
		projectionList.add(Projections.property("UOMName").as("UOMName"));
		criteria.setProjection(projectionList);		
		uomList = criteria.setResultTransformer(new AliasToBeanResultTransformer(MasUOM.class)).list();
		
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return uomList;
	}
	
	
	@Override
	public String addInvestigation(DgMasInvestigation dgmas) {
		String result="";
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(dgmas);
			session.flush();
	        session.clear();
			tx.commit();
			
			if(savedObj!=null) {
				result="200";
			}else {
				result = "500";
			}
			//session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		
		return result;
	}
	
	@Override
	public String validateInvestigationName(String invName) {
		List<DgMasInvestigation> invList =  new ArrayList<DgMasInvestigation>();		
		String result="";
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria = session.createCriteria(DgMasInvestigation.class);				
				criteria.add(Restrictions.eq("investigationName", invName));				
				invList = criteria.list();				
				if(invList.size()>0 && invList !=null) {
					result="nameExists";
				}
				
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		return result;
	}
	
	
	@Override
	public Map<String, List<DgMasInvestigation>> getAllInvestigationDetails(JSONObject jsondata) {
		Map<String, List<DgMasInvestigation>> map = new HashMap<String, List<DgMasInvestigation>>();
		List<DgMasInvestigation> invList = new ArrayList<DgMasInvestigation>();
		int pageNo=1;
		int pageSize = 5;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(DgMasInvestigation.class);
			criteria.createAlias("masMainChargecode", "mmc");
			if(jsondata.has("investigationType")) {
			criteria.add(Restrictions.eq("mmc.mainChargecodeCode", jsondata.get("investigationType").toString()));
			}
			
			
			List totalMatches=new ArrayList();
					
			if( jsondata.get("PN")!=null)
			 pageNo = Integer.parseInt(jsondata.get("PN").toString());
					
			String invName="";
				 if (jsondata.has("investigationName"))
				 {
					  invName = "%"+jsondata.get("investigationName")+"%";
					  if(jsondata.get("investigationName").toString().length()>0 && !jsondata.get("investigationName").toString().trim().equalsIgnoreCase("")) {
							criteria.add(Restrictions.ilike("investigationName", invName));
							
						}
				 }
				 criteria.addOrder(Order.asc("investigationName"));
				 totalMatches = criteria.list();				 
				 criteria.setFirstResult((pageSize) * (pageNo - 1));
				 criteria.setMaxResults(pageSize);
				 invList = criteria.list();			
			
		map.put("invList", invList);
		map.put("totalMatches", totalMatches);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}
	
	@Override
	public String updateInvestigationStatus(Long invId, String status) {
		
		String result = "";
		try {

			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Object dgmasObject = session.load(DgMasInvestigation.class, invId);
			DgMasInvestigation dgmas = (DgMasInvestigation) dgmasObject;
			Transaction transaction = session.beginTransaction();
			if (dgmas.getStatus().equalsIgnoreCase("y")) {
				dgmas.setStatus("n");

			} else if (dgmas.getStatus().equalsIgnoreCase("n")) {
				dgmas.setStatus("y");

			} else {
				dgmas.setStatus("y");
			}

			session.update(dgmas);
			transaction.commit();
			session.flush();
			result = "200";
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	@Override
	public String updateInvestigation(JSONObject jsondata) {
		String result="";
		Transaction transaction=null;
		Long invId=Long.parseLong(jsondata.get("investigationId").toString());
		long d = System.currentTimeMillis();
		Timestamp date = new Timestamp(d);
		String validList = validateInvestigationName(jsondata.get("investigationName").toString().trim());
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			if(jsondata !=null) {
				if(validList !=null && !validList.equals("")) {		
				
				if(invId > 0) {
					Object object = session.load(DgMasInvestigation.class, invId);
					DgMasInvestigation dgmas = (DgMasInvestigation)object;					
					transaction = session.beginTransaction();
					dgmas.setInvestigationName(dgmas.getInvestigationName());
					dgmas.setMainChargecodeId(Long.parseLong(jsondata.get("departmentId").toString()));
					dgmas.setSubChargecodeId(Long.parseLong(jsondata.get("modalityId").toString()));
					
					if(jsondata.get("investigationType").toString().equalsIgnoreCase("Lab")) {
					 dgmas.setSampleId(Long.parseLong(jsondata.get("sampleId").toString()));
					 dgmas.setCollectionId(Long.parseLong(jsondata.get("collectionId").toString()));
					 dgmas.setUomId(Long.parseLong(jsondata.get("uomId").toString()));
					 dgmas.setMinNormalValue(jsondata.get("minValue").toString());
					 dgmas.setMaxNormalValue(jsondata.get("maxValue").toString());
					 dgmas.setLoincCode(jsondata.get("loincCode").toString());
					 if(null!=jsondata.get("pandemicFlag")) 
					 {
						 dgmas.setPandemicFlag("Y");
						 dgmas.setPandemicCases(Long.parseLong(jsondata.get("pandemicCases").toString()));
					 }
					}
					
					dgmas.setInvestigationType(jsondata.get("resultType").toString());
					if(jsondata.has("flag")) {
					dgmas.setFlag(jsondata.get("flag").toString());
					}
					dgmas.setConfidential(jsondata.get("confidential").toString());
					dgmas.setLastChgBy(Long.parseLong(jsondata.get("userId").toString()));					
					dgmas.setLastChgDate(date);
					dgmas.setStatus(dgmas.getStatus());
					session.update(dgmas);
					transaction.commit();
					session.flush();
					result="success";
					transaction=null;
				}
			}
				else {				
					
					if(invId > 0) {
						Object object = session.load(DgMasInvestigation.class, invId);
						DgMasInvestigation dgmas = (DgMasInvestigation)object;					
						transaction = session.beginTransaction();
						dgmas.setInvestigationName(jsondata.get("investigationName").toString());
						dgmas.setMainChargecodeId(Long.parseLong(jsondata.get("departmentId").toString()));
						dgmas.setSubChargecodeId(Long.parseLong(jsondata.get("modalityId").toString()));
						
						if(jsondata.get("investigationType").toString().equalsIgnoreCase("Lab")) {
						 dgmas.setSampleId(Long.parseLong(jsondata.get("sampleId").toString()));
						 dgmas.setCollectionId(Long.parseLong(jsondata.get("collectionId").toString()));
						 dgmas.setUomId(Long.parseLong(jsondata.get("uomId").toString()));
						 dgmas.setMinNormalValue(jsondata.get("minValue").toString());
						 dgmas.setMaxNormalValue(jsondata.get("maxValue").toString());
						 dgmas.setLoincCode(jsondata.get("loincCode").toString());
						}
						
						dgmas.setInvestigationType(jsondata.get("resultType").toString());	
						dgmas.setFlag(jsondata.get("flag").toString());
						dgmas.setConfidential(jsondata.get("confidential").toString());
						dgmas.setLastChgBy(Long.parseLong(jsondata.get("userId").toString()));						
						dgmas.setLastChgDate(date);
						dgmas.setStatus(dgmas.getStatus());
						session.update(dgmas);
						transaction.commit();
						session.flush();
						result="success";
						
					}
				
					
				}
				
		}									
		}catch(Exception e) {
			
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	/**************************************
	 * Sub Investigation Master
	 **************************************************/
	
		
	@Override
	public Map<String, List<DgSubMasInvestigation>> getAllSubInvestigationDetails(JSONObject jsondata) {
		Map<String, List<DgSubMasInvestigation>> map = new HashMap<String, List<DgSubMasInvestigation>>();
		List<DgSubMasInvestigation> subinvList = new ArrayList<DgSubMasInvestigation>();
		Long investigationId=Long.parseLong(jsondata.get("investigationId").toString());
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(DgSubMasInvestigation.class);	
			     criteria.add(Restrictions.eq("investigationId", investigationId));
				 criteria.addOrder(Order.asc("subInvestigationName"));				 
				 subinvList = criteria.list();			
			
		map.put("subinvList", subinvList);
		
		
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}
	
	@Override
	public String validateLoincCode(String loincCode) {
		List<DgSubMasInvestigation> invList =  new ArrayList<DgSubMasInvestigation>();		
		String result="";
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria = session.createCriteria(DgSubMasInvestigation.class);				
				criteria.add(Restrictions.eq("loincCode", loincCode));				
				invList = criteria.list();				
				if(invList.size()>0 && invList !=null) {
					result="nameExists";
				}
				
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		return result;
	}
	
		
	@Override
	public String updateSubInvestigation(HashMap<String, Object> jsondata) {
	    Long d = System.currentTimeMillis();
		Timestamp date = new Timestamp(d);	
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Transaction tx=session.beginTransaction();
		String result = "";
		try {
			
			
			if (jsondata.get("subInvlist") != null) {
				@SuppressWarnings("unchecked")
				List<HashMap<String, Object>> listSubInvestigation = (List<HashMap<String, Object>>) jsondata.get("subInvlist");
               
				if (listSubInvestigation != null) {
					for (HashMap<String, Object> map : listSubInvestigation) {
						DgSubMasInvestigation dgsubObj=null;
						
						if(map.get("subInvId").toString() !=null && !map.get("subInvId").toString().isEmpty())
						{
							Long subInvId =Long.parseLong(map.get("subInvId").toString());						
					        if(subInvId !=null && !subInvId.equals("")) {						   
					        	Object dgmasObject = session.load(DgSubMasInvestigation.class, subInvId);
					        	dgsubObj = (DgSubMasInvestigation) dgmasObject;
					   }
					   }
						
					   else {
							dgsubObj=new DgSubMasInvestigation();
						}					
						
					Long investigationId=Long.parseLong(map.get("investigationId").toString());		
						if(investigationId !=null) {
							dgsubObj.setInvestigationId(investigationId);
						}
					Long modalityId=Long.parseLong(map.get("modalityId").toString());	
					if(modalityId !=null) {
						dgsubObj.setSubChargecodeId(modalityId);
					}
					Long deparmentId=Long.parseLong(map.get("departmentId").toString());
					if(deparmentId !=null) {
						dgsubObj.setMainChargecodeId(deparmentId);
					}
					Long printOrder=Long.parseLong(map.get("printOrder").toString());
					if(printOrder!=null) {
						dgsubObj.setOrderNo(printOrder);
					}					
					
					String subtestName=map.get("subtestName").toString();
					if(subtestName !=null) {
						dgsubObj.setSubInvestigationName(subtestName);
					}
					
					String loincCode=map.get("loincCode").toString();
					if(loincCode !=null) {
						dgsubObj.setLoincCode(loincCode);
					}
					
					Long mainInvestigationId= !map.get("mainInvestigationId").toString().isEmpty() ? Long.parseLong(map.get("mainInvestigationId").toString()):null;
					if(mainInvestigationId !=null && !mainInvestigationId.equals("")) {
						dgsubObj.setMainInvestigationId(mainInvestigationId);
					}
					
					Long uomId=Long.parseLong(map.get("uomId").toString());
					if(uomId !=null) {
						dgsubObj.setUomId(uomId);
					}
					String resultType=map.get("resultType").toString();
					if(resultType !=null) {
						dgsubObj.setResultType(resultType);
					}
					
					String cmpType=map.get("cmpTypeId").toString();
					if(cmpType !=null )
					{
						dgsubObj.setComparisonType(cmpType);
					}
					dgsubObj.setLastChgDate(date);
					dgsubObj.setStatus("y");
					dgsubObj.setLastChgBy(Long.parseLong(map.get("userId").toString()));
					session.saveOrUpdate(dgsubObj);
					session.flush();
					session.clear();
					
					
					}
					tx.commit();
				}
				
				
				result = "success";
				
				}
			

		} catch (Exception ex) {
			tx.rollback();
			ex.printStackTrace();
			
			
		} finally {

			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return result;
	}
	
	
	@Override
	public String deleteSunbInvestigationById(Long subInvid) {
	    
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Transaction tx=session.beginTransaction();
		
		String result = "";
		try {
			List<DgFixedValue> listFixedValue=session.createCriteria(DgFixedValue.class).add(Restrictions.eq("subInvestigationId", subInvid)).list();
			if(listFixedValue !=null && listFixedValue.size()>0) {
				Query query = session.createQuery("delete DgFixedValue where subInvestigationId ="+subInvid);
				query.executeUpdate();
			}
			
			List<DgNormalValue> listNormalValue=session.createCriteria(DgNormalValue.class).add(Restrictions.eq("subInvestigationId", subInvid)).list();
			if(listNormalValue !=null && listNormalValue.size()>0) {
				Query query = session.createQuery("delete DgNormalValue where subInvestigationId ="+subInvid);
				query.executeUpdate();
			}
			Query query = session.createQuery("delete DgSubMasInvestigation where subInvestigationId ="+subInvid);
			query.executeUpdate();
			tx.commit();
			result="success";
			
	      }
		
		 catch (Exception ex) {
			tx.rollback();
			ex.printStackTrace();
			
			
		} finally {

			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return result;
	}
	
	
	@Override
	public String deleteFixedValueById(Long fixedValueid) {
	    
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Transaction tx=session.beginTransaction();
		
		String result = "";
		try {
			List<DgFixedValue> listFixedValue=session.createCriteria(DgFixedValue.class).add(Restrictions.eq("fixedId", fixedValueid)).list();
			if(listFixedValue !=null && listFixedValue.size()>0) {
				Query query = session.createQuery("delete DgFixedValue where fixedId ="+fixedValueid);
				query.executeUpdate();
			}
			
			tx.commit();
			result="success";
			
	      }
		
		 catch (Exception ex) {
			tx.rollback();
			ex.printStackTrace();
			
			
		} finally {

			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return result;
	}

	/*************************DoctorMapping Master************************************/
	
	@Override
	public String addMasDoctorMapping(MasDoctorMapping masDoctorMapping) {
		String result="";
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(masDoctorMapping);
			
			tx.commit();	
			
			//System.out.println("savedObj"+savedObj);
			if(savedObj!=null) {
				result="200";
			}else {
				result = "500";
			}
			//session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		
		return result;
	}
	
	@Override
	public List<MasDoctorMapping> validateMasDoctorMapping(Long doctorId, Long departmentId) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria criteria = null;
		criteria = session.createCriteria(MasDoctorMapping.class)
				.createAlias("masDepartment", "md")
				.createAlias("doctorId", "us")
				.add(Restrictions.and(Restrictions.eq("us.userId", doctorId),Restrictions.eq("md.departmentId", departmentId)));	
		@SuppressWarnings("unchecked")
		List<MasDoctorMapping> doctorMappingList = criteria.list();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return doctorMappingList;
	}
	
	@Override
	public List<MasDoctorMapping> validateMasDoctorMappingUpdate(Long doctorId, Long departmentId) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria criteria = null;
		criteria = session.createCriteria(MasDoctorMapping.class)
				.createAlias("masDepartment", "md")
				//.createAlias("users", "us")
				.add(Restrictions.or(
						Restrictions.and(Restrictions.eq("doctorId.userId", doctorId),Restrictions.eq("md.departmentId", departmentId))));	
		@SuppressWarnings("unchecked")
		List<MasDoctorMapping> doctorMappingList = criteria.list();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return doctorMappingList;
	}

	@Override
	public Map<String, List<MasDoctorMapping>> getAllDoctorMapping(JSONObject jsondata) {

		int pageSize = 5;
		int pageNo=Integer.parseInt(String.valueOf(jsondata.get("PN")));
		Long hospitalId= Long.parseLong(jsondata.get("hospitalId").toString());
		Map<String, List<MasDoctorMapping>> mapObj = new HashMap<String, List<MasDoctorMapping>>();
		List<MasDoctorMapping> dndList = new ArrayList<MasDoctorMapping>();
		List totalMatches = new ArrayList();

		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasDoctorMapping.class);
			if(hospitalId !=null) {
				criteria.add(Restrictions.eq("locationId", hospitalId));
			}
		long departmentId;
		if (jsondata.get("departmentId") != null && !(jsondata.get("departmentId")).equals("")) {
			departmentId = Long.parseLong(jsondata.get("departmentId").toString());
			if (jsondata.get("departmentId").toString().length() > 0
					&& !jsondata.get("departmentId").toString().equalsIgnoreCase("0")) {
					
					criteria.createAlias("masDepartment", "md");
					criteria.add(Restrictions.eq("md.departmentId", departmentId));
			}
		}
		totalMatches = criteria.list();

		criteria.setFirstResult((pageSize) * (pageNo - 1));
		criteria.setMaxResults(pageSize);
		dndList = criteria.list();

		mapObj.put("totalMatches", totalMatches);
		mapObj.put("dndList", dndList);

	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		getHibernateUtils.getHibernateUtlis().CloseConnection();
	}
	return mapObj;

	}

	@Override
	public String updateMasDoctorMappingStatus(Long doctorMapId, String status,Long userId,Long hospitalId) {
		String result = "";
		try {

			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Object dndObject = session.load(MasDoctorMapping.class, doctorMapId);
			MasDoctorMapping masDoctorMapping = (MasDoctorMapping) dndObject;
			Transaction transaction = session.beginTransaction();
			if (masDoctorMapping.getStatus().equalsIgnoreCase("y")) {
				masDoctorMapping.setStatus("n");

			} else if (masDoctorMapping.getStatus().equalsIgnoreCase("n")) {
				masDoctorMapping.setStatus("y");

			} else {
				masDoctorMapping.setStatus("y");
			}

			session.update(masDoctorMapping);
			transaction.commit();
			session.flush();
			result = "200";
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public String updateMasDoctorMapping(Long doctorMapId, Long departmentId, Long doctorId,Long userId,Long hospitalId) {
		String result = "";
		try {
			List<MasDoctorMapping> dndList = new ArrayList<MasDoctorMapping>();
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();

			if (doctorMapId != 0) {
				Object dndObject = session.load(MasDoctorMapping.class, doctorMapId);
				MasDoctorMapping masDoctorMapping = (MasDoctorMapping) dndObject;

				Transaction transaction = session.beginTransaction();
				
				Users user1 = new Users();
				user1.setUserId(doctorId);
				masDoctorMapping.setDoctorId(user1);
				
				MasDepartment masDepartment = new MasDepartment();
				masDepartment.setDepartmentId(departmentId);
				masDoctorMapping.setMasDepartment(masDepartment);

				Users users = new Users();
				users.setUserId(userId);
				masDoctorMapping.setLastChgBy(users);
				
				MasHospital location = new MasHospital();
				location.setHospitalId(hospitalId);
				masDoctorMapping.setLocationId(hospitalId);
				
				long d = System.currentTimeMillis();
				Timestamp date = new Timestamp(d);

				session.update(masDoctorMapping);
				transaction.commit();
				result = "200";
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return result;
	}
/*****************************Employee Master*********************************/
	
	@Override
	public MasRank getRankByRankCode(String rankCode) {
		 List<MasRank>listMasRank   = null;
		 MasRank masRank=null;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria cri =   session.createCriteria(MasRank.class)
					 .add(Restrictions.eq("rankCode",rankCode));
			listMasRank	= cri.list();
			if(CollectionUtils.isNotEmpty(listMasRank)) {
				masRank	=listMasRank.get(0);
			}
			
		}catch (Exception e) {
			//getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		}finally {
			//getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return masRank;
	}
	
	@Override
	public MasUnit getMasUnitByUnitCode(String unitCode) {
		List<MasUnit>listMasUnit   = null;
		MasUnit masUnit=null;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria cri =   session.createCriteria(MasUnit.class)
					 .add(Restrictions.eq("unitCode",unitCode));
			listMasUnit	= cri.list();
			if(CollectionUtils.isNotEmpty(listMasUnit)) {
				masUnit	=listMasUnit.get(0);
			}
			
		}catch (Exception e) {
			//getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		}finally {
			//getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return masUnit;
	}
	
	@Override
	public Map<String, List<MasEmployee>> getAllEmployee(JSONObject jsondata) {
		int pageSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
		int pageNo=Integer.parseInt(String.valueOf(jsondata.get("PN")));
		
		Map<String, List<MasEmployee>> mapObj = new HashMap<String, List<MasEmployee>>();
		List<MasEmployee> empList = new ArrayList<MasEmployee>();
		List<MasEmployee> totalMatches = new ArrayList();

		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasEmployee.class);
			
		String serviceNo="";
			if (jsondata.get("serviceNo") != null && !(jsondata.get("serviceNo")).equals("")) {
				serviceNo = jsondata.get("serviceNo").toString();
				 if (jsondata.has("serviceNo"))
				 {
					 serviceNo = "%"+jsondata.get("serviceNo")+"%";
					  if(jsondata.get("serviceNo").toString().length()>0 && !jsondata.get("serviceNo").toString().trim().equalsIgnoreCase("")) {
							criteria.add(Restrictions.ilike("serviceNo", serviceNo));
							
						}
				 }
			}

		
		if (jsondata.get("unitId") != null && !(jsondata.get("unitId")).equals("")) {
			if (jsondata.get("unitId").toString().length() > 0
					&& !jsondata.get("unitId").toString().equalsIgnoreCase("0")) {
				long unitId = Long.parseLong(String.valueOf(jsondata.get("unitId")));
				MasUnit masUnit1 = (MasUnit) session.createCriteria(MasUnit.class)
						.add(Restrictions.eq("unitId", unitId)).uniqueResult();
				String unitCode = masUnit1.getUnitCode();
				criteria = criteria.add(Restrictions.eq("masUnit", unitCode));
			}

		}
		criteria.addOrder(Order.asc("serviceNo"));
		totalMatches = criteria.list();

		criteria.setFirstResult((pageSize) * (pageNo - 1));
		criteria.setMaxResults(pageSize);
		empList = criteria.list();

		mapObj.put("totalMatches", totalMatches);
		mapObj.put("empList", empList);

	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		getHibernateUtils.getHibernateUtlis().CloseConnection();
	}
	return mapObj;

	}
	
	@Override
	public List<MasUnit> getAllUnitList() {
		List<MasUnit> unitList = new ArrayList<MasUnit>();
		try {	 
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria criteria =  session.createCriteria(MasUnit.class);		
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("unitId").as("unitId"));		
		projectionList.add(Projections.property("unitName").as("unitName"));
		criteria.setProjection(projectionList);		
		unitList = criteria.setResultTransformer(new AliasToBeanResultTransformer(MasUnit.class)).addOrder(Order.asc("unitName")).list();
		
		//criteria.addOrder(Order.asc("unitName"));
		
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return unitList;
	}
	
	
	/*****************************Fixed Value Master*********************************/
	
	@Override
	public String updateFixedValue(HashMap<String, Object> jsondata) {
	    Long d = System.currentTimeMillis();
		Timestamp date = new Timestamp(d);	
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Transaction tx=session.beginTransaction();
		
		
		String result = "";
		try {
			 
			if (jsondata.get("fixedValuelist") != null) {
				@SuppressWarnings("unchecked")
				List<HashMap<String, Object>> listFixedValue = (List<HashMap<String, Object>>) jsondata.get("fixedValuelist");
               
				if (listFixedValue != null) {
					for (HashMap<String, Object> map : listFixedValue) {
						DgFixedValue dgfvObj=null;
						Long userId=Long.parseLong(map.get("userId").toString());
						if(map.get("fixedValueId").toString() !=null && !map.get("fixedValueId").toString().equals(""))
						{
							Long fixedValueId =Long.parseLong(map.get("fixedValueId").toString());						
					        if(fixedValueId !=null && !fixedValueId.equals("")) {						   
					        	Object dgfvObject = session.load(DgFixedValue.class, fixedValueId);
					        	dgfvObj = (DgFixedValue) dgfvObject;
					   }
					       
					   }
						
					   else {
						   dgfvObj=new DgFixedValue();
						}					
						
					 Long subInvId=Long.parseLong(map.get("subInvId").toString());
					 if(subInvId !=null && !subInvId.equals(""))
					 {
						 dgfvObj.setSubInvestigationId(subInvId); 
					 }
					 
					 String fixedValue=map.get("fixedValue").toString();
					 if(fixedValue !=null && !fixedValue.equals("")) {
						 dgfvObj.setFixedValue(fixedValue);
					 }
					 dgfvObj.setLastChgDate(date);
					 dgfvObj.setLastChgBy(userId);
					session.saveOrUpdate(dgfvObj);
					session.flush();
					session.clear();
					
					
					
					}
					tx.commit();
				}
				
				
				result = "success";
				
				}

		} catch (Exception ex) {
			tx.rollback();
			ex.printStackTrace();
			
			
		} finally {

			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return result;
	}
	
	@Override
	public Map<String, List<DgFixedValue>> getAllFixeValueById(JSONObject jsondata) {
		Map<String, List<DgFixedValue>> map = new HashMap<String, List<DgFixedValue>>();
		List<DgFixedValue> fvList = new ArrayList<DgFixedValue>();
		Long subInvId=Long.parseLong(jsondata.get("subInvId").toString());
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(DgFixedValue.class);	
			     criteria.add(Restrictions.eq("subInvestigationId", subInvId));			 
			     fvList = criteria.list();			
			
		map.put("fixedValueList", fvList);
		
		
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}
	
	@Override
	public String validateFixedValue(JSONObject jsondata) {
		String result="";
		List<DgFixedValue> fvList=new ArrayList<DgFixedValue>();
		Long subInvId=Long.parseLong(jsondata.get("subInvId").toString());
		String fixedValue=jsondata.get("fixedValue").toString();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(DgFixedValue.class);	
			     criteria.add(Restrictions.eq("subInvestigationId", subInvId));	
			     criteria.add(Restrictions.eq("fixedValue", fixedValue));	
			     fvList = criteria.list();			
		if(!fvList.isEmpty() && fvList !=null) {
			result="fvFound";
		}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
/*****************************Normal Value Master*********************************/
	
	@Override
	public String updateNormalValue(HashMap<String, Object> jsondata) {
	    Long d = System.currentTimeMillis();
		Timestamp date = new Timestamp(d);	
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Transaction tx=session.beginTransaction();
		
		
		String result = "";
		try {
			
			
			if (jsondata.get("normalValuelist") != null) {
				@SuppressWarnings("unchecked")
				List<HashMap<String, Object>> listNormalValue = (List<HashMap<String, Object>>) jsondata.get("normalValuelist");
               
				if (listNormalValue != null) {
					for (HashMap<String, Object> map : listNormalValue) {
						DgNormalValue dgnvObj=null;
						Long userId=Long.parseLong(map.get("userId").toString());
						if(map.get("normalValueId").toString() !=null && !map.get("normalValueId").toString().equals(""))
						{
							Long normalValueId =Long.parseLong(map.get("normalValueId").toString());						
					        if(normalValueId !=null && !normalValueId.equals("")) {						   
					        	Object dgnvObject = session.load(DgNormalValue.class, normalValueId);
					        	dgnvObj = (DgNormalValue) dgnvObject;
					   }
					       
					   }
						
					   else {
						   dgnvObj=new DgNormalValue();
						}					
						String sexValue=map.get("sexId").toString();
					       if(sexValue!=null && !sexValue.equals("")) {
					    	   dgnvObj.setSex(sexValue);
					       }
					  Long fromAge=Long.parseLong(map.get("fromAge").toString());
					if(fromAge !=null && !fromAge.equals("")) {
						dgnvObj.setFromAge(fromAge);
					}
					
					Long toAge=Long.parseLong(map.get("toAge").toString());
					if(toAge !=null && !toAge.equals("")) {
						dgnvObj.setToAge(toAge);
					}
					
					String minNormalValue=map.get("minNormalValue").toString();
					if(minNormalValue !=null && !minNormalValue.equals("")) {
						dgnvObj.setMinNormalValue(minNormalValue);
					}
					
					String maxNormalValue=map.get("maxNormalValue").toString();
					if(maxNormalValue !=null && !maxNormalValue.equals("")) {
						dgnvObj.setMaxNormalValue(maxNormalValue);
					}
					
					String normalValue=map.get("normalValue").toString();
					if(normalValue !=null && !normalValue.equals("")) {
						dgnvObj.setNormalValue(normalValue);
					}
					 Long subInvId=Long.parseLong(map.get("subInvId").toString());
					 if(subInvId !=null && !subInvId.equals(""))
					 {
						 dgnvObj.setSubInvestigationId(subInvId); 
					 }
					 dgnvObj.setLastChgDate(date);
					 dgnvObj.setLastChgBy(userId);
					session.saveOrUpdate(dgnvObj);
					session.flush();
					session.clear();
				
					}
					tx.commit();
				}
				
				
				result = "success";
				
				}
			
		

		} catch (Exception ex) {
			tx.rollback();
			ex.printStackTrace();
			
			
		} finally {

			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return result;
	}
	
	@Override
	public Map<String, List<DgNormalValue>> getAllNormalValueById(JSONObject jsondata) {
		Map<String, List<DgNormalValue>> map = new HashMap<String, List<DgNormalValue>>();
		List<DgNormalValue> nvList = new ArrayList<DgNormalValue>();
		Long subInvId=Long.parseLong(jsondata.get("subInvId").toString());
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(DgNormalValue.class);	
			     criteria.add(Restrictions.eq("subInvestigationId", subInvId));			 
			     nvList = criteria.list();			
			
		map.put("normalValueList", nvList);
		
		
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}
	@Override
	public String validateServiceNo(JSONObject jsondata) {

		String result="";
		String sql="";
		
		List<Object[]> listObject=null;
		String unitId=jsondata.get("unitId").toString();
		
		String serviceNo=jsondata.get("serviceNo").toString();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();						
			// sql="select VE.Service_no from vu_mas_employee VE,  VU_Mas_punit VU where VE.Unit_id= VU.CENTITY  and VU.Unit_id='"+unitId+"'"+ "and VE.Service_no='"+serviceNo+"'";
			sql="select VE.Service_no from vu_mas_employee VE where VE.Service_no='"+serviceNo+"'";
			Query query = session.createSQLQuery(sql);
			listObject = query.list();
		if(CollectionUtils.isNotEmpty(listObject)) {
			result="svFound";
		}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	/****************************MAS DischargeStatus*********************************************************/
	@Override
	public Map<String, List<MasDischargeStatus>> getAllDischargeStatus(JSONObject jsondata) {
		Map<String, List<MasDischargeStatus>> map = new HashMap<String, List<MasDischargeStatus>>();
		List<MasDischargeStatus> dischargeStatusList = new ArrayList<MasDischargeStatus>();
		int pageNo=1;
		int pageSize = 5;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasDischargeStatus.class);
			
					
			if( jsondata.get("PN")!=null)
			 pageNo = Integer.parseInt(jsondata.get("PN").toString());
					
			String dsName="";
				 if (jsondata.has("dischargeStatusName"))
				 {
					  dsName = "%"+jsondata.get("dischargeStatusName")+"%";
					  if(jsondata.get("dischargeStatusName").toString().length()>0 && !jsondata.get("dischargeStatusName").toString().trim().equalsIgnoreCase("")) {
							criteria.add(Restrictions.ilike("dischargeStatusName", dsName));
							//criteria.addOrder(Order.asc(jsondata.get("administrativeSexName").toString()));
						}
				 }
				 //criteria.add(Restrictions.eq("status", "Y").ignoreCase());
				 List totalMatches = criteria.list();
				 
				 criteria.setFirstResult((pageSize) * (pageNo - 1));
				 criteria.setMaxResults(pageSize);
				 dischargeStatusList = criteria.list();
			
			
		map.put("dischargeStatusList", dischargeStatusList);
		map.put("totalMatches", totalMatches);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@Override
	public List<MasDischargeStatus> validateDischargeStatus(String dischargeStatusCode, String dischargeStatusName) {
		List<MasDischargeStatus> dsList =  new ArrayList<MasDischargeStatus>();	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria = session.createCriteria(MasDischargeStatus.class);
				criteria.add(Restrictions.or(Restrictions.eq("dischargeStatusCode", dischargeStatusCode), Restrictions.eq("dischargeStatusName", dischargeStatusName)));
				dsList = criteria.list();
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		return dsList;
	}

	@Override
	public List<MasDischargeStatus> validateDischargeStatusUpdate(String dischargeStatusCode, String dischargeStatusName) {
		List<MasDischargeStatus> dsList =  new ArrayList<MasDischargeStatus>();	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria = session.createCriteria(MasDischargeStatus.class);
				criteria.add(Restrictions.and(Restrictions.eq("dischargeStatusCode", dischargeStatusCode), Restrictions.eq("dischargeStatusName", dischargeStatusName)));
				dsList = criteria.list();
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		return dsList;
	}

	@Override
	public String addDischargeStatus(MasDischargeStatus masDischargeStatus) {
		String result="";	
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(masDischargeStatus);
			tx.commit();
			if(savedObj!=null) {
				result="200";
			}else {
				result = "500";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		
		return result;
	}

	@Override
	public String updateDischargeStatusDetails(Long dischargeStatusId, String dischargeStatusCode, String dischargeStatusName, Long userId) {
		String result="";
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
					Object object = session.load(MasDischargeStatus.class, dischargeStatusId);
					MasDischargeStatus masDischargeStatus = (MasDischargeStatus)object;
					
					Transaction transaction = session.beginTransaction();
					masDischargeStatus.setDischargeStatusCode(dischargeStatusCode.toUpperCase());
					masDischargeStatus.setDischargeStatusName(dischargeStatusName.toUpperCase());			
									
					Users user = new Users();
					user.setUserId(userId);
					masDischargeStatus.setUser(user);				
					long d = System.currentTimeMillis();
					Timestamp date = new Timestamp(d);
					masDischargeStatus.setLastChgDate(date);
					session.update(masDischargeStatus);
					transaction.commit();
					
					result = "200";
				//}
			//}
											
		}catch(Exception e) {
			
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public MasDischargeStatus checkDischargeStatus(String dischargeStatusCode) {
		MasDischargeStatus mDischargeStatus = new MasDischargeStatus();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasDischargeStatus.class);		
			criteria.add(Restrictions.eq("dischargeStatusCode", dischargeStatusCode));
			mDischargeStatus = (MasDischargeStatus)criteria.uniqueResult();
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mDischargeStatus;
	}

	@Override
	public String updateDischargeStatusStatus(Long dischargeStatusId, String dischargeStatusCode, String status, Long userId) {
		String result = "";	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				
				Object object =  session.load(MasDischargeStatus.class, dischargeStatusId);
				
				MasDischargeStatus masDischargeStatus = (MasDischargeStatus)object;
				Transaction transaction = session.beginTransaction();
			
			  if(masDischargeStatus.getStatus().equalsIgnoreCase("Y") || masDischargeStatus.getStatus().equalsIgnoreCase("y")) {
				  masDischargeStatus.setStatus("N"); 
			  }else if(masDischargeStatus.getStatus().equalsIgnoreCase("N") || masDischargeStatus.getStatus().equalsIgnoreCase("n")) {
				  masDischargeStatus.setStatus("Y"); 
			  }else {
				  masDischargeStatus.setStatus("Y"); 
			  }
			 
				session.update(masDischargeStatus);
				transaction.commit();
				result = "200";
			//}
			
		}catch(Exception e) {
			
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	/****************************MAS BedStatus*********************************************************/
	@Override
	public Map<String, List<MasBedStatus>> getAllBedStatus(JSONObject jsondata) {
		Map<String, List<MasBedStatus>> map = new HashMap<String, List<MasBedStatus>>();
		List<MasBedStatus> bedStatusList = new ArrayList<MasBedStatus>();
		int pageNo=1;
		int pageSize = 5;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasBedStatus.class);
			
					
			if( jsondata.get("PN")!=null)
			 pageNo = Integer.parseInt(jsondata.get("PN").toString());
					
			String bsName="";
				 if (jsondata.has("bedStatusName"))
				 {
					  bsName = "%"+jsondata.get("bedStatusName")+"%";
					  if(jsondata.get("bedStatusName").toString().length()>0 && !jsondata.get("bedStatusName").toString().trim().equalsIgnoreCase("")) {
							criteria.add(Restrictions.ilike("bedStatusName", bsName));
							//criteria.addOrder(Order.asc(jsondata.get("administrativeSexName").toString()));
						}
				 }
				 //criteria.add(Restrictions.eq("status", "Y").ignoreCase());
				 List totalMatches = criteria.list();
				 
				 if(! jsondata.get("PN").toString().equals("0"))
					{
					criteria.setFirstResult((pageSize) * (pageNo - 1));
					criteria.setMaxResults(pageSize);
					}
				 bedStatusList = criteria.list();
			
			
		map.put("bedStatusList", bedStatusList);
		map.put("totalMatches", totalMatches);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@Override
	public List<MasBedStatus> validateBedStatus(String bedStatusCode, String bedStatusName) {
		List<MasBedStatus> bsList =  new ArrayList<MasBedStatus>();	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria = session.createCriteria(MasBedStatus.class);
				criteria.add(Restrictions.or(Restrictions.eq("bedStatusCode", bedStatusCode), Restrictions.eq("bedStatusName", bedStatusName)));
				bsList = criteria.list();
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		return bsList;
	}

	@Override
	public List<MasBedStatus> validateBedStatusUpdate(String bedStatusCode, String bedStatusName) {
		List<MasBedStatus> bsList =  new ArrayList<MasBedStatus>();	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria = session.createCriteria(MasBedStatus.class);
				criteria.add(Restrictions.and(Restrictions.eq("bedStatusCode", bedStatusCode), Restrictions.eq("bedStatusName", bedStatusName)));
				bsList = criteria.list();
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		return bsList;
	}

	@Override
	public String addBedStatus(MasBedStatus masBedStatus) {
		String result="";	
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(masBedStatus);
			tx.commit();
			if(savedObj!=null) {
				result="200";
			}else {
				result = "500";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		
		return result;
	}

	@Override
	public String updateBedStatusDetails(Long bedStatusId, String bedStatusCode, String bedStatusName, Long userId) {
		String result="";
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
					Object object = session.load(MasBedStatus.class, bedStatusId);
					MasBedStatus masBedStatus = (MasBedStatus)object;
					
					Transaction transaction = session.beginTransaction();
					masBedStatus.setBedStatusCode(bedStatusCode.toUpperCase());
					masBedStatus.setBedStatusName(bedStatusName.toUpperCase());			
									
					Users user = new Users();
					user.setUserId(userId);
					masBedStatus.setUser(user);				
					long d = System.currentTimeMillis();
					Timestamp date = new Timestamp(d);
					masBedStatus.setLastChgDate(date);
					session.update(masBedStatus);
					transaction.commit();
					
					result = "200";
				//}
			//}
											
		}catch(Exception e) {
			
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public MasBedStatus checkBedStatus(String bedStatusCode) {
		MasBedStatus mBedStatus = new MasBedStatus();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasBedStatus.class);		
			criteria.add(Restrictions.eq("bedStatusCode", bedStatusCode));
			mBedStatus = (MasBedStatus)criteria.uniqueResult();
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mBedStatus;
	}

	@Override
	public String updateBedStatusStatus(Long bedStatusId, String bedStatusCode, String status, Long userId) {
		String result = "";	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				
				Object object =  session.load(MasBedStatus.class, bedStatusId);
				
				MasBedStatus masBedStatus = (MasBedStatus)object;
				Transaction transaction = session.beginTransaction();
			
			  if(masBedStatus.getStatus().equalsIgnoreCase("Y") || masBedStatus.getStatus().equalsIgnoreCase("y")) {
				  masBedStatus.setStatus("N"); 
			  }else if(masBedStatus.getStatus().equalsIgnoreCase("N") || masBedStatus.getStatus().equalsIgnoreCase("n")) {
				  masBedStatus.setStatus("Y"); 
			  }else {
				  masBedStatus.setStatus("Y"); 
			  }
			 
				session.update(masBedStatus);
				transaction.commit();
				result = "200";
			//}
			
		}catch(Exception e) {
			
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	/****************************MAS Bed*********************************************************/

	@Override
	public Map<String, List<MasBed>> getAllBed(JSONObject jsondata) {

		int pageSize = 5;
		int pageNo=Integer.parseInt(String.valueOf(jsondata.get("PN")));
		
		Map<String, List<MasBed>> mapObj = new HashMap<String, List<MasBed>>();
		List<MasBed> bedList = new ArrayList<MasBed>();
		List totalMatches = new ArrayList();

		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasBed.class);
		long departmentId;
		if (jsondata.get("departmentId") != null && !(jsondata.get("departmentId")).equals("")) {
			departmentId = Long.parseLong(jsondata.get("departmentId").toString());
			if (jsondata.get("departmentId").toString().length() > 0
					&& !jsondata.get("departmentId").toString().equalsIgnoreCase("0")) {
					
					criteria.createAlias("masDepartment", "md");
					criteria.add(Restrictions.eq("md.departmentId", departmentId));
			}
		}
		totalMatches = criteria.list();

		criteria.setFirstResult((pageSize) * (pageNo - 1));
		criteria.setMaxResults(pageSize);
		bedList = criteria.list();

		mapObj.put("totalMatches", totalMatches);
		mapObj.put("bedList", bedList);

	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		getHibernateUtils.getHibernateUtlis().CloseConnection();
	}
	return mapObj;

	}

	@Override
	public List<MasBed> validateBed(String bedNo, Long departmentId, Long hospitalId) {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = null;
			criteria = session.createCriteria(MasBed.class).createAlias("masHospital", "mh")
								.createAlias("masDepartment", "md")
								.add(Restrictions.and(Restrictions.eq("bedNo", bedNo),Restrictions.eq("mh.hospitalId", hospitalId),
										Restrictions.eq("md.departmentId", departmentId)));
			
			List<MasBed> bedList = criteria.list();
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			return bedList;
		}

	@Override
	public List<MasBed> validateBedUpdate(String bedNo, Long departmentId, Long hospitalId) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria criteria = null;
		criteria = session.createCriteria(MasBed.class).createAlias("masHospital", "mh")
							.createAlias("masDepartment", "md")
							.add(Restrictions.and(Restrictions.eq("bedNo", bedNo),Restrictions.eq("mh.hospitalId", hospitalId),
									Restrictions.eq("md.departmentId", departmentId)));
		
		List<MasBed> bedList = criteria.list();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return bedList;
	}

	@Override
	public String addBed(MasBed masBed) {
		String result="";	
		Transaction tx=null;
		Session session=null;
		Criteria criteria=null;
		Long bedStatusId=null;
		MasBedStatus masBedStatusObj=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			criteria=session.createCriteria(MasBedStatus.class);
			criteria.add(Restrictions.eq("status", "Y").ignoreCase());
			List<MasBedStatus> masBedStatuslist=criteria.add(Restrictions.eq("bedStatusCode", "U")).list();
			if(masBedStatuslist !=null && masBedStatuslist.size()>0) {
				bedStatusId=masBedStatuslist.get(0).getBedStatusId();
			
			masBed.setBedStatusId(bedStatusId);
			Serializable savedObj =  session.save(masBed);
			tx.commit();
			if(savedObj!=null) {
				result="200";
			}else {
				result = "500";
			}
		  }
			else {
				result="400";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		
		return result;
	}

	@Override
	public String updateBed(Long bedId, String bedNo, Long departmentId, Long userId,
			Long hospitalId) {
		String result = "";
		try {
			List<MasBed> bedList = new ArrayList<MasBed>();
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();

			if (bedId != 0) {
				Object bedObject = session.load(MasBed.class, bedId);
				MasBed masBed = (MasBed) bedObject;

				Transaction transaction = session.beginTransaction();
				
				masBed.setBedNo(bedNo);
				MasDepartment masDepartment = new MasDepartment();
				masDepartment.setDepartmentId(departmentId);
				masBed.setMasDepartment(masDepartment);

				Users users = new Users();
				users.setUserId(userId);
				masBed.setUser(users);
				
				MasHospital masHospital = new MasHospital();
				masHospital.setHospitalId(hospitalId);
				masBed.setMasHospital(masHospital);
				
				long d = System.currentTimeMillis();
				Timestamp date = new Timestamp(d);

				session.update(masBed);
				transaction.commit();
				result = "200";
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return result;
	}

	@Override
	public MasBed checkBed(String bedNo) {
		MasBed mBed = new MasBed();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasBed.class);		
			criteria.add(Restrictions.eq("bedNo", bedNo));
			mBed = (MasBed)criteria.uniqueResult();
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mBed;
	}

	@Override
	public String updateBedStatus(Long bedId, String status, Long userId, Long hospitalId) {
		String result = "";
		try {

			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Object bedObject = session.load(MasBed.class, bedId);
			MasBed masBed = (MasBed) bedObject;
			Transaction transaction = session.beginTransaction();
			if (masBed.getStatus().equalsIgnoreCase("y")) {
				masBed.setStatus("n");

			} else if (masBed.getStatus().equalsIgnoreCase("n")) {
				masBed.setStatus("y");

			} else {
				masBed.setStatus("y");
			}

			session.update(masBed);
			transaction.commit();
			session.flush();
			result = "200";
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	/****************************MAS Speciality*********************************************************/
	@Override
	public Map<String, List<MasSpeciality>> getAllSpeciality(JSONObject jsondata) {
		Map<String, List<MasSpeciality>> map = new HashMap<String, List<MasSpeciality>>();
		List<MasSpeciality> specialityList = new ArrayList<MasSpeciality>();
		int pageNo=1;
		int pageSize = 5;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasSpeciality.class);
			
					
			if( jsondata.get("PN")!=null)
			 pageNo = Integer.parseInt(jsondata.get("PN").toString());
					
			String sName="";
				 if (jsondata.has("specialityName"))
				 {
					  sName = "%"+jsondata.get("specialityName")+"%";
					  if(jsondata.get("specialityName").toString().length()>0 && !jsondata.get("specialityName").toString().trim().equalsIgnoreCase("")) {
							criteria.add(Restrictions.ilike("specialityName", sName));
							//criteria.addOrder(Order.asc(jsondata.get("administrativeSexName").toString()));
						}
				 }
				 //criteria.add(Restrictions.eq("status", "Y").ignoreCase());
				 List totalMatches = criteria.list();
				 
				 criteria.setFirstResult((pageSize) * (pageNo - 1));
				 criteria.setMaxResults(pageSize);
				 specialityList = criteria.list();
			
			
		map.put("specialityList", specialityList);
		map.put("totalMatches", totalMatches);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@Override
	public List<MasSpeciality> validateSpeciality(String specialityCode, String specialityName) {
		List<MasSpeciality> sList =  new ArrayList<MasSpeciality>();	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria = session.createCriteria(MasSpeciality.class);
				criteria.add(Restrictions.or(Restrictions.eq("specialityCode", specialityCode), Restrictions.eq("specialityName", specialityName)));
				sList = criteria.list();
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		return sList;
	}

	@Override
	public List<MasSpeciality> validateSpecialityUpdate(String specialityCode, String specialityName) {
		List<MasSpeciality> sList =  new ArrayList<MasSpeciality>();	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria = session.createCriteria(MasSpeciality.class);
				criteria.add(Restrictions.and(Restrictions.eq("specialityCode", specialityCode), Restrictions.eq("specialityName", specialityName)));
				sList = criteria.list();
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		return sList;
	}

	@Override
	public String addSpeciality(MasSpeciality masSpeciality) {
		String result="";	
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(masSpeciality);
			tx.commit();
			if(savedObj!=null) {
				result="200";
			}else {
				result = "500";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		
		return result;
	}

	@Override
	public String updateSpecialityDetails(Long specialityId, String specialityCode, String specialityName, Long userId) {
		String result="";
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
					Object object = session.load(MasSpeciality.class, specialityId);
					MasSpeciality masSpeciality = (MasSpeciality)object;
					
					Transaction transaction = session.beginTransaction();
					masSpeciality.setSpecialityCode(specialityCode.toUpperCase());
					masSpeciality.setSpecialityName(specialityName.toUpperCase());			
									
					Users user = new Users();
					user.setUserId(userId);
					masSpeciality.setUser(user);				
					long d = System.currentTimeMillis();
					Timestamp date = new Timestamp(d);
					masSpeciality.setLastChgDate(date);
					session.update(masSpeciality);
					transaction.commit();
					
					result = "200";
				//}
			//}
											
		}catch(Exception e) {
			
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public MasSpeciality checkSpeciality(String specialityCode) {
		MasSpeciality mSpeciality = new MasSpeciality();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasSpeciality.class);		
			criteria.add(Restrictions.eq("specialityCode", specialityCode));
			mSpeciality = (MasSpeciality)criteria.uniqueResult();
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mSpeciality;
	}

	@Override
	public String updateSpecialityStatus(Long specialityId, String specialityCode, String status, Long userId) {
		String result = "";	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				
				Object object =  session.load(MasSpeciality.class, specialityId);
				
				MasSpeciality masSpeciality = (MasSpeciality)object;
				Transaction transaction = session.beginTransaction();
			
			  if(masSpeciality.getStatus().equalsIgnoreCase("Y") || masSpeciality.getStatus().equalsIgnoreCase("y")) {
				  masSpeciality.setStatus("N"); 
			  }else if(masSpeciality.getStatus().equalsIgnoreCase("N") || masSpeciality.getStatus().equalsIgnoreCase("n")) {
				  masSpeciality.setStatus("Y"); 
			  }else {
				  masSpeciality.setStatus("Y"); 
			  }
			 
				session.update(masSpeciality);
				transaction.commit();
				result = "200";
			//}
			
		}catch(Exception e) {
			
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	/****************************MAS AdmissionType*********************************************************/
	@Override
	public Map<String, List<MasAdmissionType>> getAllAdmissionType(JSONObject jsondata) {
		Map<String, List<MasAdmissionType>> map = new HashMap<String, List<MasAdmissionType>>();
		List<MasAdmissionType> admissionTypeList = new ArrayList<MasAdmissionType>();
		int pageNo=1;
		int pageSize = 5;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasAdmissionType.class);
			
					
			if( jsondata.get("PN")!=null)
			 pageNo = Integer.parseInt(jsondata.get("PN").toString());
					
			String atName="";
				 if (jsondata.has("admissionTypeName"))
				 {
					  atName = "%"+jsondata.get("admissionTypeName")+"%";
					  if(jsondata.get("admissionTypeName").toString().length()>0 && !jsondata.get("admissionTypeName").toString().trim().equalsIgnoreCase("")) {
							criteria.add(Restrictions.ilike("admissionTypeName", atName));
							//criteria.addOrder(Order.asc(jsondata.get("administrativeSexName").toString()));
						}
				 }
				 //criteria.add(Restrictions.eq("status", "Y").ignoreCase());
				 List totalMatches = criteria.list();
				 
				 criteria.setFirstResult((pageSize) * (pageNo - 1));
				 criteria.setMaxResults(pageSize);
				 admissionTypeList = criteria.list();
			
			
		map.put("admissionTypeList", admissionTypeList);
		map.put("totalMatches", totalMatches);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@Override
	public List<MasAdmissionType> validateAdmissionType(String admissionTypeCode, String admissionTypeName) {
		List<MasAdmissionType> atList =  new ArrayList<MasAdmissionType>();	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria = session.createCriteria(MasSpeciality.class);
				criteria.add(Restrictions.or(Restrictions.eq("admissionTypeCode", admissionTypeCode), Restrictions.eq("admissionTypeName", admissionTypeName)));
				atList = criteria.list();
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		return atList;
	}

	@Override
	public List<MasAdmissionType> validateAdmissionTypeUpdate(String admissionTypeCode, String admissionTypeName) {
		List<MasAdmissionType> atList =  new ArrayList<MasAdmissionType>();	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria = session.createCriteria(MasAdmissionType.class);
				criteria.add(Restrictions.and(Restrictions.eq("admissionTypeCode", admissionTypeCode), Restrictions.eq("admissionTypeName", admissionTypeName)));
				atList = criteria.list();
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		return atList;
	}

	@Override
	public String addAdmissionType(MasAdmissionType masAdmissionType) {
		String result="";	
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(masAdmissionType);
			tx.commit();
			if(savedObj!=null) {
				result="200";
			}else {
				result = "500";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		
		return result;
	}

	@Override
	public String updateAdmissionTypeDetails(Long admissionTypeId, String admissionTypeCode, String admissionTypeName, Long userId) {
		String result="";
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
					Object object = session.load(MasAdmissionType.class, admissionTypeId);
					MasAdmissionType masAdmissionType = (MasAdmissionType)object;
					
					Transaction transaction = session.beginTransaction();
					masAdmissionType.setAdmissionTypeCode(admissionTypeCode.toUpperCase());
					masAdmissionType.setAdmissionTypeName(admissionTypeName.toUpperCase());			
									
					Users user = new Users();
					user.setUserId(userId);
					masAdmissionType.setUser(user);				
					long d = System.currentTimeMillis();
					Timestamp date = new Timestamp(d);
					masAdmissionType.setLastChgDate(date);
					session.update(masAdmissionType);
					transaction.commit();
					
					result = "200";
				//}
			//}
											
		}catch(Exception e) {
			
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public MasAdmissionType checkAdmissionType(String admissionTypeCode) {
		MasAdmissionType mAdmissionType = new MasAdmissionType();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasAdmissionType.class);		
			criteria.add(Restrictions.eq("admissionTypeCode", admissionTypeCode));
			mAdmissionType = (MasAdmissionType)criteria.uniqueResult();
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mAdmissionType;
	}

	@Override
	public String updateAdmissionTypeStatus(Long admissionTypeId, String admissionTypeCode, String status, Long userId) {
		String result = "";	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				
				Object object =  session.load(MasAdmissionType.class, admissionTypeId);
				
				MasAdmissionType masAdmissionType = (MasAdmissionType)object;
				Transaction transaction = session.beginTransaction();
			
			  if(masAdmissionType.getStatus().equalsIgnoreCase("Y") || masAdmissionType.getStatus().equalsIgnoreCase("y")) {
				  masAdmissionType.setStatus("N"); 
			  }else if(masAdmissionType.getStatus().equalsIgnoreCase("N") || masAdmissionType.getStatus().equalsIgnoreCase("n")) {
				  masAdmissionType.setStatus("Y"); 
			  }else {
				  masAdmissionType.setStatus("Y"); 
			  }
			 
				session.update(masAdmissionType);
				transaction.commit();
				result = "200";
			//}
			
		}catch(Exception e) {
			
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	/****************************MAS DisposedTo*********************************************************/
	@Override
	public Map<String, List<MasDisposedTo>> getAllDisposedTo(JSONObject jsondata) {
		Map<String, List<MasDisposedTo>> map = new HashMap<String, List<MasDisposedTo>>();
		List<MasDisposedTo> disposedToList = new ArrayList<MasDisposedTo>();
		int pageNo=1;
		int pageSize = 5;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasDisposedTo.class);
			
					
			if( jsondata.get("PN")!=null)
			 pageNo = Integer.parseInt(jsondata.get("PN").toString());
					
			String dtName="";
				 if (jsondata.has("disposedToName"))
				 {
					  dtName = "%"+jsondata.get("disposedToName")+"%";
					  if(jsondata.get("disposedToName").toString().length()>0 && !jsondata.get("disposedToName").toString().trim().equalsIgnoreCase("")) {
							criteria.add(Restrictions.ilike("disposedToName", dtName));
							//criteria.addOrder(Order.asc(jsondata.get("administrativeSexName").toString()));
						}
				 }
				 //criteria.add(Restrictions.eq("status", "Y").ignoreCase());
				 List totalMatches = criteria.list();
				 
				 criteria.setFirstResult((pageSize) * (pageNo - 1));
				 criteria.setMaxResults(pageSize);
				 disposedToList = criteria.list();
			
			
		map.put("disposedToList", disposedToList);
		map.put("totalMatches", totalMatches);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@Override
	public List<MasDisposedTo> validateDisposedTo(String disposedToCode, String disposedToName) {
		List<MasDisposedTo> dtList =  new ArrayList<MasDisposedTo>();	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria = session.createCriteria(MasDisposedTo.class);
				criteria.add(Restrictions.or(Restrictions.eq("disposedToCode", disposedToCode), Restrictions.eq("disposedToName", disposedToName)));
				dtList = criteria.list();
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		return dtList;
	}

	@Override
	public List<MasDisposedTo> validateDisposedToUpdate(String disposedToCode, String disposedToName) {
		List<MasDisposedTo> dtList =  new ArrayList<MasDisposedTo>();	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria = session.createCriteria(MasDisposedTo.class);
				criteria.add(Restrictions.and(Restrictions.eq("disposedToCode", disposedToCode), Restrictions.eq("disposedToName", disposedToName)));
			dtList = criteria.list();
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		return dtList;
	}

	@Override
	public String addDisposedTo(MasDisposedTo masDisposedTo) {
		String result="";	
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(masDisposedTo);
			tx.commit();
			if(savedObj!=null) {
				result="200";
			}else {
				result = "500";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		
		return result;
	}

	@Override
	public String updateDisposedToDetails(Long disposedToId, String disposedToCode, String disposedToName, Long userId) {
		String result="";
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
					Object object = session.load(MasDisposedTo.class, disposedToId);
					MasDisposedTo masDisposedTo = (MasDisposedTo)object;
					
					Transaction transaction = session.beginTransaction();
					masDisposedTo.setDisposedToCode(disposedToCode.toUpperCase());
					masDisposedTo.setDisposedToName(disposedToName.toUpperCase());			
									
					Users user = new Users();
					user.setUserId(userId);
					masDisposedTo.setUser(user);				
					long d = System.currentTimeMillis();
					Timestamp date = new Timestamp(d);
					masDisposedTo.setLastChgDate(date);
					session.update(masDisposedTo);
					transaction.commit();
					
					result = "200";
				//}
			//}
											
		}catch(Exception e) {
			
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public MasDisposedTo checkDisposedTo(String disposedToCode) {
		MasDisposedTo mDisposedTo = new MasDisposedTo();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasDisposedTo.class);		
			criteria.add(Restrictions.eq("disposedToCode", disposedToCode));
			mDisposedTo = (MasDisposedTo)criteria.uniqueResult();
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mDisposedTo;
	}

	@Override
	public String updateDisposedToStatus(Long disposedToId, String disposedToCode, String status, Long userId) {
		String result = "";	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				
				Object object =  session.load(MasDisposedTo.class, disposedToId);
				
				MasDisposedTo masDisposedTo = (MasDisposedTo)object;
				Transaction transaction = session.beginTransaction();
			
			  if(masDisposedTo.getStatus().equalsIgnoreCase("Y") || masDisposedTo.getStatus().equalsIgnoreCase("y")) {
				  masDisposedTo.setStatus("N"); 
			  }else if(masDisposedTo.getStatus().equalsIgnoreCase("N") || masDisposedTo.getStatus().equalsIgnoreCase("n")) {
				  masDisposedTo.setStatus("Y"); 
			  }else {
				  masDisposedTo.setStatus("Y"); 
			  }
			 
				session.update(masDisposedTo);
				transaction.commit();
				result = "200";
			//}
			
		}catch(Exception e) {
			
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	
/******************************Patient Condition Master********************************/	
	@Override
	public Map<String, List<MasPatientCondition>> getAllCondition(JSONObject jsondata) {
		Map<String, List<MasPatientCondition>> map = new HashMap<String, List<MasPatientCondition>>();
		List<MasPatientCondition> conditionList = new ArrayList<MasPatientCondition>();
		int pageNo=1;
		int pageSize = 5;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasPatientCondition.class);
			
					
			if( jsondata.get("PN")!=null)
			 pageNo = Integer.parseInt(jsondata.get("PN").toString());
					
			String cName="";
				 if (jsondata.has("conditionName"))
				 {
					  cName = "%"+jsondata.get("conditionName")+"%";
					  if(jsondata.get("conditionName").toString().length()>0 && !jsondata.get("conditionName").toString().trim().equalsIgnoreCase("")) {
							criteria.add(Restrictions.ilike("conditionName", cName));
							//criteria.addOrder(Order.asc(jsondata.get("administrativeSexName").toString()));
						}
				 }
				 //criteria.add(Restrictions.eq("status", "Y").ignoreCase());
				 List totalMatches = criteria.list();
				 
				 criteria.setFirstResult((pageSize) * (pageNo - 1));
				 criteria.setMaxResults(pageSize);
				 conditionList = criteria.list();
			
			
		map.put("conditionList", conditionList);
		map.put("totalMatches", totalMatches);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@Override
	public List<MasPatientCondition> validateCondition(String conditionName) {
		List<MasPatientCondition> cList =  new ArrayList<MasPatientCondition>();	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria = session.createCriteria(MasPatientCondition.class);
				criteria.add(Restrictions.eq("conditionName", conditionName));
				cList = criteria.list();
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		return cList;
	}

	@Override
	public List<MasPatientCondition> validateConditionUpdate(String conditionName) {
		List<MasPatientCondition> cList =  new ArrayList<MasPatientCondition>();	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria = session.createCriteria(MasPatientCondition.class);
				criteria.add(Restrictions.eq("conditionName", conditionName));
				cList = criteria.list();
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		return cList;
	}

	@Override
	public String addCondition(MasPatientCondition masPatientCondition) {
		String result="";	
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(masPatientCondition);
			tx.commit();
			if(savedObj!=null) {
				result="200";
			}else {
				result = "500";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		
		return result;
	}

	@Override
	public String updateConditionDetails(Long conditionId,String conditionName, Long userId) {
		String result="";
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
					Object object = session.load(MasPatientCondition.class, conditionId);
					MasPatientCondition masPatientCondition = (MasPatientCondition)object;
					
					Transaction transaction = session.beginTransaction();
					//masPatientCondition.setConditionCode(conditionCode.toUpperCase());
					masPatientCondition.setConditionName(conditionName.toUpperCase());			
									
					Users user = new Users();
					user.setUserId(userId);
					masPatientCondition.setUser(user);				
					long d = System.currentTimeMillis();
					Timestamp date = new Timestamp(d);
					masPatientCondition.setLastChgDate(date);
					session.update(masPatientCondition);
					transaction.commit();
					
					result = "200";
				//}
			//}
											
		}catch(Exception e) {
			
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public MasPatientCondition checkCondition(String conditionName) {
		MasPatientCondition mCondition = new MasPatientCondition();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasPatientCondition.class);		
			criteria.add(Restrictions.eq("conditionName", conditionName));
			mCondition = (MasPatientCondition)criteria.uniqueResult();
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mCondition;
	}

	@Override
	public String updateConditionStatus(Long conditionId, String status, Long userId) {
		String result = "";	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				
				Object object =  session.load(MasPatientCondition.class, conditionId);
				
				MasPatientCondition masPatientCondition = (MasPatientCondition)object;
				Transaction transaction = session.beginTransaction();
			
			  if(masPatientCondition.getStatus().equalsIgnoreCase("Y") || masPatientCondition.getStatus().equalsIgnoreCase("y")) {
				  masPatientCondition.setStatus("N"); 
			  }else if(masPatientCondition.getStatus().equalsIgnoreCase("N") || masPatientCondition.getStatus().equalsIgnoreCase("n")) {
				  masPatientCondition.setStatus("Y"); 
			  }else {
				  masPatientCondition.setStatus("Y"); 
			  }
			 
				session.update(masPatientCondition);
				transaction.commit();
				result = "200";
			//}
			
		}catch(Exception e) {
			
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	
	/****************************MAS Diet*********************************************************/
	@Override
	public Map<String, List<MasDiet>> getAllDiet(JSONObject jsondata) {
		Map<String, List<MasDiet>> map = new HashMap<String, List<MasDiet>>();
		List<MasDiet> dietList = new ArrayList<MasDiet>();
		int pageNo=1;
		int pageSize = 5;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasDiet.class);
			
					
			if( jsondata.get("PN")!=null)
			 pageNo = Integer.parseInt(jsondata.get("PN").toString());
					
			String dtName="";
				 if (jsondata.has("dietName"))
				 {
					  dtName = "%"+jsondata.get("dietName")+"%";
					  if(jsondata.get("dietName").toString().length()>0 && !jsondata.get("dietName").toString().trim().equalsIgnoreCase("")) {
							criteria.add(Restrictions.ilike("dietName", dtName));
							//criteria.addOrder(Order.asc(jsondata.get("administrativeSexName").toString()));
						}
				 }
				 //criteria.add(Restrictions.eq("status", "Y").ignoreCase());
				 List totalMatches = criteria.list();
				 
				 criteria.setFirstResult((pageSize) * (pageNo - 1));
				 criteria.setMaxResults(pageSize);
				 dietList = criteria.list();
			
			
		map.put("dietList", dietList);
		map.put("totalMatches", totalMatches);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@Override
	public List<MasDiet> validateDiet(String dietCode, String dietName) {
		List<MasDiet> dtList =  new ArrayList<MasDiet>();	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria = session.createCriteria(MasDiet.class);
				criteria.add(Restrictions.or(Restrictions.eq("dietCode", dietCode), Restrictions.eq("dietName", dietName)));
				dtList = criteria.list();
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		return dtList;
	}

	@Override
	public List<MasDiet> validateDietUpdate(String dietCode, String dietName) {
		List<MasDiet> dtList =  new ArrayList<MasDiet>();	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria = session.createCriteria(MasDiet.class);
				criteria.add(Restrictions.and(Restrictions.eq("dietCode", dietCode), Restrictions.eq("dietName", dietName)));
			dtList = criteria.list();
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		return dtList;
	}

	@Override
	public String addDiet(MasDiet masDiet) {
		String result="";	
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(masDiet);
			tx.commit();
			if(savedObj!=null) {
				result="200";
			}else {
				result = "500";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		
		return result;
	}

	@Override
	public String updateDietDetails(Long dietId, String dietCode, String dietName, Long userId) {
		String result="";
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
					Object object = session.load(MasDiet.class, dietId);
					MasDiet masDiet = (MasDiet)object;
					
					Transaction transaction = session.beginTransaction();
					masDiet.setDietCode(dietCode.toUpperCase());
					masDiet.setDietName(dietName.toUpperCase());			
									
					Users user = new Users();
					user.setUserId(userId);
					masDiet.setUser(user);				
					long d = System.currentTimeMillis();
					Timestamp date = new Timestamp(d);
					masDiet.setLastChgDate(date);
					session.update(masDiet);
					transaction.commit();
					
					result = "200";
				//}
			//}
											
		}catch(Exception e) {
			
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public MasDiet checkDiet(String dietCode) {
		MasDiet mDiet = new MasDiet();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasDiet.class);		
			criteria.add(Restrictions.eq("dietCode", dietCode));
			mDiet = (MasDiet)criteria.uniqueResult();
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mDiet;
	}

	@Override
	public String updateDietStatus(Long dietId, String dietCode, String status, Long userId) {
		String result = "";	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				
				Object object =  session.load(MasDiet.class, dietId);
				
				MasDiet masDiet = (MasDiet)object;
				Transaction transaction = session.beginTransaction();
			
			  if(masDiet.getStatus().equalsIgnoreCase("Y") || masDiet.getStatus().equalsIgnoreCase("y")) {
				  masDiet.setStatus("N"); 
			  }else if(masDiet.getStatus().equalsIgnoreCase("N") || masDiet.getStatus().equalsIgnoreCase("n")) {
				  masDiet.setStatus("Y"); 
			  }else {
				  masDiet.setStatus("Y"); 
			  }
			 
				session.update(masDiet);
				transaction.commit();
				result = "200";
			//}
			
		}catch(Exception e) {
			
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public Map<String, List<MasStoreItem>> getAllStoreNiv(JSONObject jsonObj) {
		Map<String, List<MasStoreItem>> mapObj = new HashMap<String, List<MasStoreItem>>();
		int pageSize = 5;
		int pageNo=1;
		String drugsCode = HMSUtil.getProperties("adt.properties", "drugsCode").trim();
		String vaccineCode = HMSUtil.getProperties("adt.properties", "vaccineCode").trim();
		List totalMatches = new ArrayList();
		 
		List<MasStoreItem> masStoreItemList = new ArrayList<MasStoreItem>();
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasStoreItem.class);
			if(jsonObj.has("drugType") && jsonObj.has("itemTypeId") ) {
				
				criteria.add(Restrictions.and(Restrictions.eq("typeOfItem",jsonObj.get("drugType").toString()),
						Restrictions.eq("itemTypeId",jsonObj.get("itemTypeId").toString())));
				
				if(jsonObj.get("drugType").toString().equalsIgnoreCase("E") && jsonObj.get("itemTypeId").toString().equalsIgnoreCase("13")) {
				if(drugsCode !=null && vaccineCode !=null) {
					criteria.createAlias("masStoreSection", "ms");
					criteria.add(Restrictions.in("ms.sectionCode", new String[] {drugsCode, vaccineCode}));
					
				}		
				}
				
				}
		
			if( jsonObj.get("PN")!=null)
			 pageNo = Integer.parseInt(jsonObj.get("PN").toString());
				
			String nomenclature="";
				 if (jsonObj.has("nomenclature"))
				 {
					 nomenclature = "%"+jsonObj.get("nomenclature")+"%";
					  if(jsonObj.get("nomenclature").toString().length()>0 && !jsonObj.get("nomenclature").toString().trim().equalsIgnoreCase("")) {
							criteria.add(Restrictions.ilike("nomenclature", nomenclature));
						}
				 }	
				 
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("itemId").as("itemId"));
			projectionList.add(Projections.property("pvmsNo").as("pvmsNo"));
			projectionList.add(Projections.property("nomenclature").as("nomenclature"));
			projectionList.add(Projections.property("status").as("status"));
			projectionList.add(Projections.property("masStoreGroup").as("masStoreGroup"));
			projectionList.add(Projections.property("masStoreUnit").as("masStoreUnit"));
			projectionList.add(Projections.property("masStoreSection").as("masStoreSection"));
			projectionList.add(Projections.property("masItemClass").as("masItemClass"));
			projectionList.add(Projections.property("masItemType").as("masItemType"));
			//projectionList.add(Projections.property("masHospital").as("masHospital"));
			projectionList.add(Projections.property("dispUnitQty").as("dispUnitQty"));
			projectionList.add(Projections.property("rolD").as("rolD"));
			projectionList.add(Projections.property("rolS").as("rolS"));
			criteria.addOrder(Order.asc("nomenclature"));
			
			totalMatches = criteria.list();			
					
			criteria.setFirstResult((pageSize) * (pageNo - 1));
			criteria.setProjection(projectionList).setMaxResults(pageSize);
			masStoreItemList = criteria.setResultTransformer(new AliasToBeanResultTransformer(MasStoreItem.class)).list();
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			mapObj.put("masStoreItemList", masStoreItemList);
			mapObj.put("totalMatches", totalMatches);
			return mapObj;
	}
	
	
	@Override
	public String addDisease(MasDisease masDisease) {
		String result="";	
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(masDisease);
			tx.commit();
			if(savedObj!=null) {
				result="200";
			}else {
				result = "500";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		
		return result;
	}
	
	
	@Override
	public List<MasDisease> validateDisease(String diseaseCode, String diseaseName) {
		List<MasDisease> diseaseList =  new ArrayList<MasDisease>();	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria = session.createCriteria(MasDisease.class);
				criteria.add(Restrictions.or(Restrictions.eq("diseaseCode", diseaseCode).ignoreCase(), Restrictions.eq("diseaseName", diseaseName).ignoreCase()));
				diseaseList = criteria.list();
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		return diseaseList;
	}
	
	
	@Override
	public Map<String, List<MasDisease>> getAllDisease(JSONObject jsondata) {
		Map<String, List<MasDisease>> map = new HashMap<String, List<MasDisease>>();
		List<MasDisease> diseaseList = new ArrayList<MasDisease>();
		List totalMatches  =new ArrayList<>();
		int pageNo=1;
		int pageSize = 5;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasDisease.class);					
			if( !jsondata.get("PN").toString().equals("0"))
			{
			 pageNo = Integer.parseInt(jsondata.get("PN").toString());
					
			String diseaseName="";
				 if (jsondata.has("diseaseName"))
				 {
					 diseaseName = "%"+jsondata.get("diseaseName")+"%";
					  if(jsondata.get("diseaseName").toString().length()>0 && !jsondata.get("diseaseName").toString().trim().equalsIgnoreCase("")) {
							criteria.add(Restrictions.ilike("diseaseName", diseaseName));
							
						}
				 }
				 criteria.addOrder(Order.asc("diseaseName"));
				 totalMatches = criteria.list();				 
				 criteria.setFirstResult((pageSize) * (pageNo - 1));
				 criteria.setMaxResults(pageSize);
				 diseaseList = criteria.list();
			}
			else if(jsondata.get("PN").toString().equals("0")) {
				if(jsondata.has("diseaseType")) {
					criteria.createAlias("masDiseaseType", "mdt");
					criteria.add(Restrictions.eq("mdt.diseaseTypeName", "Preventable Disease").ignoreCase());
					criteria.add(Restrictions.eq("status", "Y").ignoreCase());
					totalMatches = criteria.list();
					 diseaseList = criteria.list();
				}
				else {
				criteria.add(Restrictions.eq("status", "Y").ignoreCase());
				totalMatches = criteria.list();
				 diseaseList = criteria.list();
				}
			}
			
			
		map.put("diseaseList", diseaseList);
		map.put("totalMatches", totalMatches);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}
	
	
	@Override
	public String updateDiseaseStatus(Long id, String status) {
		
		String result = "";
		try {

			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Object dgmasObject = session.load(MasDisease.class, id);
			MasDisease masdis = (MasDisease) dgmasObject;
			Transaction transaction = session.beginTransaction();
			if (masdis.getStatus().equalsIgnoreCase("y")) {
				masdis.setStatus("n");

			} else if (masdis.getStatus().equalsIgnoreCase("n")) {
				masdis.setStatus("y");

			} else {
				masdis.setStatus("y");
			}

			session.update(masdis);
			transaction.commit();
			session.flush();
			result = "200";
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	@Override
	public String updateDisease(MasDisease masDisease) {
		String result="";
		Session session = null;
		try {
			List<MasDisease> masDiseaseList = validateDisease(masDisease.getDiseaseCode(),masDisease.getDiseaseName());
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Transaction transaction = session.beginTransaction();
					Object object = session.load(MasDisease.class, masDisease.getDiseaseId());
					MasDisease masDiseaseObj = (MasDisease)object;					
					if(masDiseaseList.get(0).getDiseaseName().equalsIgnoreCase(masDisease.getDiseaseName())) {
						masDiseaseObj.setDiseaseName(masDiseaseObj.getDiseaseName());
					}
					else {
						masDiseaseObj.setDiseaseName(masDisease.getDiseaseName());
					}
					masDiseaseObj.setDiseaseTypeId(masDisease.getDiseaseTypeId());
					masDiseaseObj.setLastChgBy(masDisease.getLastChgBy());			
					masDiseaseObj.setLastChgDate(masDisease.getLastChgDate());
					session.update(masDiseaseObj);
					session.flush();
					transaction.commit();					
					result = "success";
			
											
		}catch(Exception e) {
			
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	@Override
	public List<MasDocument> validateDocument(String documentCode, String documentName) {
		List<MasDocument> documentList =  new ArrayList<MasDocument>();	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria = session.createCriteria(MasDocument.class);
				criteria.add(Restrictions.or(Restrictions.eq("documentCode", documentCode), Restrictions.eq("documentName", documentName)));
				documentList = criteria.list();
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		return documentList;
	}
	
	@Override
	public String addDocument(MasDocument masDocument) {
		String result="";	
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(masDocument);
			tx.commit();
			if(savedObj!=null) {
				result="200";
			}else {
				result = "500";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		
		return result;
	}
	
	
	@Override
	public Map<String, List<MasDocument>> getAllDocument(JSONObject jsondata) {
		Map<String, List<MasDocument>> map = new HashMap<String, List<MasDocument>>();
		List<MasDocument> docList = new ArrayList<MasDocument>();
		int pageNo=1;
		int pageSize = 5;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasDocument.class);					
			if( jsondata.get("PN")!=null)
			{
			 pageNo = Integer.parseInt(jsondata.get("PN").toString());
			}		
			String documentName="";
				 if (jsondata.has("documentName"))
				 {
					 documentName = "%"+jsondata.get("documentName")+"%";
					  if(jsondata.get("documentName").toString().length()>0 && !jsondata.get("documentName").toString().trim().equalsIgnoreCase("")) {
							criteria.add(Restrictions.ilike("documentName", documentName));
							
						}
				 }
				 criteria.addOrder(Order.asc("documentName"));
				 List totalMatches = criteria.list();				 
				 criteria.setFirstResult((pageSize) * (pageNo - 1));
				 criteria.setMaxResults(pageSize);
				 docList = criteria.list();
			
			
		map.put("docList", docList);
		map.put("totalMatches", totalMatches);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}
	
	@Override
	public String updateDocumentStatus(Long id, String status) {
		
		String result = "";
		try {

			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Object docObject = session.load(MasDocument.class, id);
			MasDocument docObj = (MasDocument) docObject;
			Transaction transaction = session.beginTransaction();
			if (docObj.getStatus().equalsIgnoreCase("y")) {
				docObj.setStatus("n");

			} else if (docObj.getStatus().equalsIgnoreCase("n")) {
				docObj.setStatus("y");

			} else {
				docObj.setStatus("y");
			}

			session.update(docObj);
			transaction.commit();
			session.flush();
			result = "200";
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	@Override
	public String updateDocument(MasDocument masDocument) {
		String result="";
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
					Object object = session.load(MasDocument.class, masDocument.getDocumentId());
					MasDocument masDocumentObj = (MasDocument)object;					
					Transaction transaction = session.beginTransaction();					
					masDocumentObj.setDocumentName(masDocument.getDocumentName());
					masDocumentObj.setLastChgBy(masDocument.getLastChgBy());			
					masDocumentObj.setLastChgDate(masDocument.getLastChgDate());					
					session.update(masDocumentObj);
					transaction.commit();					
					result = "success";
			
											
		}catch(Exception e) {
			
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	@Override
	public List<MasBank> validateBankDetails(String bankCode, String bankName) {
		List<MasBank> bankList =  new ArrayList<MasBank>();	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria = session.createCriteria(MasBank.class);
				criteria.add(Restrictions.or(Restrictions.eq("bankCode", bankCode), Restrictions.eq("bankName", bankName)));
				bankList = criteria.list();
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		return bankList;
	}
	
	@Override
	public String addBank(MasBank masBank) {
		String result="";	
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(masBank);
			tx.commit();
			if(savedObj!=null) {
				result="200";
			}else {
				result = "500";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		
		return result;
	}
	
	@Override
	public String updateBankStatus(Long id, String status) {
		
		String result = "";
		try {

			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Object bankObject = session.load(MasBank.class, id);
			MasBank bankObj = (MasBank) bankObject;
			Transaction transaction = session.beginTransaction();
			if (bankObj.getStatus().equalsIgnoreCase("y")) {
				bankObj.setStatus("n");

			} else if (bankObj.getStatus().equalsIgnoreCase("n")) {
				bankObj.setStatus("y");

			} else {
				bankObj.setStatus("y");
			}

			session.update(bankObj);
			transaction.commit();
			session.flush();
			result = "200";
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	
	@Override
	public Map<String, List<MasBank>> getAllBank(JSONObject jsondata) {
		Map<String, List<MasBank>> map = new HashMap<String, List<MasBank>>();
		List<MasBank> bankList = new ArrayList<MasBank>();
		List totalMatches=new ArrayList<>();
		int pageNo=1;
		int pageSize = 5;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasBank.class);					
			if( jsondata.get("PN")!=null &&  !jsondata.get("PN").equals("0"))
			{
			 pageNo = Integer.parseInt(jsondata.get("PN").toString());
					
			String bankName="";
				 if (jsondata.has("bankName"))
				 {
					 bankName = "%"+jsondata.get("bankName")+"%";
					  if(jsondata.get("bankName").toString().length()>0 && !jsondata.get("bankName").toString().trim().equalsIgnoreCase("")) {
							criteria.add(Restrictions.ilike("bankName", bankName));
							
						}
				 }
				 criteria.addOrder(Order.asc("bankName"));
				 totalMatches = criteria.list();				 
				 criteria.setFirstResult((pageSize) * (pageNo - 1));
				 criteria.setMaxResults(pageSize);
				 bankList = criteria.list();
		  }	
		else if(jsondata.get("PN").equals("0")) {
			criteria.add(Restrictions.eq("status", "Y").ignoreCase());
			criteria.addOrder(Order.asc("bankName"));
			bankList = criteria.list();	
			totalMatches = criteria.list();
			}
		map.put("bankList", bankList);
		map.put("totalMatches", totalMatches);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}
	
	
	@Override
	public String updateBankDetails(MasBank masBank) {
		String result="";
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
					Object object = session.load(MasBank.class, masBank.getBankId());
					MasBank masBankObj = (MasBank)object;					
					Transaction transaction = session.beginTransaction();
					masBankObj.setBankName(masBank.getBankName().toUpperCase());					
					masBankObj.setLastChgBy(masBankObj.getLastChgBy());			
					masBankObj.setLastChgDate(masBankObj.getLastChgDate());					
					session.update(masBankObj);
					transaction.commit();					
					result = "success";
			
											
		}catch(Exception e) {
			
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	@Override
	public List<MasAccountType> validateAccountType(String accountTypeCode, String accountTypeName) {
		List<MasAccountType> accountTypeList =  new ArrayList<MasAccountType>();	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria = session.createCriteria(MasAccountType.class);
				criteria.add(Restrictions.or(Restrictions.eq("accountTypeCode", accountTypeCode), Restrictions.eq("accountTypeName", accountTypeName)));
				accountTypeList = criteria.list();
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		return accountTypeList;
	}
	
	@Override
	public List<MasAccountType> validateAccountTypeUpdate(String accountTypeName) {
		List<MasAccountType> accountTypeList =  new ArrayList<MasAccountType>();	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria = session.createCriteria(MasAccountType.class);
				criteria.add(Restrictions.eq("accountTypeName", accountTypeName));
				accountTypeList = criteria.list();
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		return accountTypeList;
	}
	
	@Override
	public String addAccountType(MasAccountType masAccountType) {
		String result="";	
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(masAccountType);
			tx.commit();
			if(savedObj!=null) {
				result="200";
			}else {
				result = "500";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		
		return result;
	}
	
	
	@Override
	public String updateAccountTypeStatus(Long id, String status) {
		
		String result = "";
		try {

			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Object actObject = session.load(MasAccountType.class, id);
			MasAccountType accountTypeObj = (MasAccountType) actObject;
			Transaction transaction = session.beginTransaction();
			if (accountTypeObj.getStatus().equalsIgnoreCase("y")) {
				accountTypeObj.setStatus("n");

			} else if (accountTypeObj.getStatus().equalsIgnoreCase("n")) {
				accountTypeObj.setStatus("y");

			} else {
				accountTypeObj.setStatus("y");
			}

			session.update(accountTypeObj);
			transaction.commit();
			session.flush();
			result = "200";
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	@Override
	public Map<String, List<MasAccountType>> getAllAccountType(JSONObject jsondata) {
		Map<String, List<MasAccountType>> map = new HashMap<String, List<MasAccountType>>();
		List<MasAccountType> accountTypeNameList = new ArrayList<MasAccountType>();
		List totalMatches=new ArrayList<>();
		int pageNo=1;
		int pageSize = 5;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasAccountType.class);					
			if( jsondata.get("PN")!=null && !jsondata.get("PN").equals("0"))
			{
			 pageNo = Integer.parseInt(jsondata.get("PN").toString());
					
			String accountTypeName="";
				 if (jsondata.has("accountTypeName"))
				 {
					 accountTypeName = "%"+jsondata.get("accountTypeName")+"%";
					  if(jsondata.get("accountTypeName").toString().length()>0 && !jsondata.get("accountTypeName").toString().trim().equalsIgnoreCase("")) {
							criteria.add(Restrictions.ilike("accountTypeName", accountTypeName));
							
						}
				 }
				 criteria.addOrder(Order.asc("accountTypeName"));
				  totalMatches = criteria.list();				 
				 criteria.setFirstResult((pageSize) * (pageNo - 1));
				 criteria.setMaxResults(pageSize);
				 accountTypeNameList = criteria.list();
			
		}
		else if(jsondata.get("PN").equals("0")) {
			criteria.add(Restrictions.eq("status", "Y").ignoreCase());
			 criteria.addOrder(Order.asc("accountTypeName"));
			accountTypeNameList = criteria.list();
			totalMatches = criteria.list();
		}
			
		map.put("accountTypeNameList", accountTypeNameList);
		map.put("totalMatches", totalMatches);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}
	
	@Override
	public String updateAccountType(MasAccountType masAccountType) {
		String result="";
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
					Object object = session.load(MasAccountType.class, masAccountType.getAccountId());
					MasAccountType masAccountTypeObj = (MasAccountType)object;					
					Transaction transaction = session.beginTransaction();
					masAccountTypeObj.setAccountTypeName(masAccountType.getAccountTypeName().toUpperCase());					
					masAccountTypeObj.setLastChgBy(masAccountType.getLastChgBy());			
					masAccountTypeObj.setLastChgDate(masAccountType.getLastChgDate());					
					session.update(masAccountTypeObj);
					transaction.commit();					
					result = "success";
			
											
		}catch(Exception e) {
			
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	/****************************MAS ICD_Diagnosis*********************************************************/
	@Override
	public Map<String, List<MasIcd>> getAllDiagnosis(JSONObject jsondata) {
		Map<String, List<MasIcd>> map = new HashMap<String, List<MasIcd>>();
		List<MasIcd> icdList = new ArrayList<MasIcd>();
		int pageNo=1;
		int pageSize = 5;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasIcd.class);
			
					
			if( jsondata.get("PN")!=null)
			 pageNo = Integer.parseInt(jsondata.get("PN").toString());
					
			String iName="";
				 if (jsondata.has("icdName"))
				 {
					  iName = "%"+jsondata.get("icdName")+"%";
					  if(jsondata.get("icdName").toString().length()>0 && !jsondata.get("icdName").toString().trim().equalsIgnoreCase("")) {
							criteria.add(Restrictions.ilike("icdName", iName));
							
						}
				 }
				criteria.addOrder(Order.asc("icdName"));
				 List totalMatches = criteria.list();
				 
				 criteria.setFirstResult((pageSize) * (pageNo - 1));
				 criteria.setMaxResults(pageSize);
				 icdList = criteria.list();
			
			
		map.put("icdList", icdList);
		map.put("totalMatches", totalMatches);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@Override
	public List<MasIcd> validateDiagnosis(String icdCode, String icdName) {
		List<MasIcd> diaList =  new ArrayList<MasIcd>();	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria = session.createCriteria(MasIcd.class);
				criteria.add(Restrictions.or(Restrictions.eq("icdCode", icdCode), Restrictions.eq("icdName", icdName)));
				diaList = criteria.list();
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		return diaList;
	}

	@Override
	public List<MasIcd> validateDiagnosisUpdate(String icdName) {
		List<MasIcd> ecList =  new ArrayList<MasIcd>();	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria = session.createCriteria(MasIcd.class);
				criteria.add(Restrictions.eq("icdName", icdName).ignoreCase());
				ecList = criteria.list();
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		return ecList;
	}

	@Override
	public String addDiagnosis(MasIcd masIcd) {
		String result="";
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(masIcd);
			tx.commit();
			if(savedObj!=null) {
				result="200";
			}else {
				result = "500";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		
		return result;
	}

	@Override
	public String updateDiagnosis(Long icdId, String icdCode, String icdName,Long userId, String communicable, String infectious,String mfDiagnosis) {
		String result="";
		try {
			List<MasIcd> validmsIcdList = validateDiagnosisUpdate(icdName);			
			
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
					Object object = session.load(MasIcd.class, icdId);
					MasIcd masIcd = (MasIcd)object;					
					Transaction transaction = session.beginTransaction();
					// masIcd.setIcdCode(icdCode.toUpperCase());
					if(validmsIcdList !=null && validmsIcdList.size() >0) {
						if(validmsIcdList.get(0).getIcdName().equalsIgnoreCase(icdName)) {
							masIcd.setIcdName(masIcd.getIcdName());	
						}
						else {
							masIcd.setIcdName(icdName.toUpperCase());
						}
						
					}
					else {
						masIcd.setIcdName(icdName.toUpperCase());
					}
					masIcd.setCommunicableFlag(communicable);	
					masIcd.setInfectionsFlag(infectious);
					Users user = new Users();
					user.setUserId(userId);
					masIcd.setLastChgBy(userId);				
					long d = System.currentTimeMillis();
					Timestamp date = new Timestamp(d);
					masIcd.setLastChgDate(date);
					masIcd.setMostCommonUser(mfDiagnosis);
					session.update(masIcd);
					transaction.commit();
					
					result = "200";
				//}
			//}
											
		}catch(Exception e) {
			
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public MasIcd checkDiagnosis(String icdCode) {
		MasIcd mIcd = new MasIcd();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasIcd.class);		
			criteria.add(Restrictions.eq("icdCode", icdCode));
			mIcd = (MasIcd)criteria.uniqueResult();
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mIcd;
	}

	@Override
	public String updateDiagnosisStatus(Long icdId, String icdCode, String status,Long userId) {
		String result = "";	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Object object =  session.load(MasIcd.class, icdId);
				
				MasIcd masIcd = (MasIcd)object;
				Transaction transaction = session.beginTransaction();
				
				
				if(masIcd.getStatus().equalsIgnoreCase("Y") || masIcd.getStatus().equalsIgnoreCase("y")) {
					masIcd.setStatus("N");
				}else if(masIcd.getStatus().equalsIgnoreCase("N") || masIcd.getStatus().equalsIgnoreCase("n")) {
					masIcd.setStatus("Y");
				}else {
					masIcd.setStatus("Y");
				}
				session.update(masIcd);
				transaction.commit();
				result = "200";
			//}
			
		}catch(Exception e) {
			
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	/**----------------Medical Exam Schedule Master--------------------*/

	@Override
	public List<CategoryDue> validateMedicalExamSchedule(Long employeeCategoryId) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria criteria = null;
		criteria = session.createCriteria(CategoryDue.class)
				.createAlias("masEmployeeCategory", "mec")
				.add(Restrictions.eq("mec.employeeCategoryId", employeeCategoryId));
		List<CategoryDue> medicalExamScheduleList = criteria.list();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return medicalExamScheduleList;
	}

	@Override
	public List<CategoryDue> validateMedicalExamScheduleUpdate(Long categoryId){
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		List<CategoryDue> medicalExamScheduleList = new ArrayList<CategoryDue>();
		Criteria criteria = session.createCriteria(CategoryDue.class);
		criteria.add(Restrictions.eq("categoryId", categoryId));
		medicalExamScheduleList = criteria.list();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return medicalExamScheduleList;
	}
	
	@Override
	public String addMedicalExamSchedule(CategoryDue categoryDue) {
		String result="";
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();		
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(categoryDue);
			tx.commit();
			
			if(savedObj!=null) {
				result = "200";
			}else {
				result = "500";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override	
	public Map<String, Object> getAllMedicalExamSchedule(JSONObject jsonObj){
	int pageSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
	int pageNo=0;

	Map<String, Object> mapObj = new HashMap<String, Object>();
	List<CategoryDue> medicalExamScheduleList = new ArrayList<CategoryDue>();
	List<MasEmployeeCategory> mEmployeeCategoryList = new ArrayList<MasEmployeeCategory>();
	List <Object[]>objectList= new ArrayList<Object[]>();
	List <Object[]>objectList1= new ArrayList<Object[]>();
	List totalMatches = new ArrayList();
	String sql="";
	String employeeCategoryName = "";
	Query query=null;
	try {
		
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria criteria = session.createCriteria(CategoryDue.class);
		if (jsonObj.get("PN") != null && !jsonObj.get("PN").toString().equals("0"))
			pageNo = Integer.parseInt(jsonObj.get("PN").toString());
					
		if (jsonObj.has("employeeCategoryName")) {
			employeeCategoryName = "%"+jsonObj.get("employeeCategoryName").toString() + "%";
			if (jsonObj.get("employeeCategoryName").toString().length() > 0	&& !jsonObj.get("employeeCategoryName").toString().trim().equalsIgnoreCase("")) {
				criteria.add(Restrictions.ilike("employeeCategoryName", employeeCategoryName));
			}
		}
		if(jsonObj.has("employeeCategoryName")) {
		if(jsonObj.get("employeeCategoryName")== "") {
			sql="select vec.employee_category_id, vec.employee_category_name, ct.category_due_id,ct.category_id,ct.from_month,ct.to_month,ct.status from category_due ct left outer join vu_mas_employee_category vec on ct.category_id=vec.employee_category_id order by vec.employee_category_name";
		}else {
			employeeCategoryName = "%"+jsonObj.get("employeeCategoryName").toString() + "%";
			sql = "select vec.employee_category_id, vec.employee_category_name, ct.category_due_id,ct.category_id,ct.from_month,ct.to_month.ct.status from category_due ct left outer join vu_mas_employee_category vec on ct.category_id=vec.employee_category_id where UPPER (vec.employee_category_name) LIKE  UPPER ('%"+employeeCategoryName+"%') order by vec.employee_category_name";
		}
		query = (Query) session.createSQLQuery(sql);
		query.setFirstResult((pageSize) * (pageNo - 1));
		query.setMaxResults(pageSize);
		objectList = query.list();
		}
		if(jsonObj.has("categoryDueId")) {
			Long categoryDueId=Long.parseLong(jsonObj.get("categoryDueId").toString());
			if(jsonObj.get("categoryDueId") !=null) {
				
				sql="select CT.CATEGORY_DUE_ID,VEC.EMPLOYEE_CATEGORY_NAME as employee_category_name, CT.CATEGORY_ID, CT.FROM_MONTH, CT.TO_MONTH,CT.STATUS from CATEGORY_DUE CT,VU_MAS_EMPLOYEE_CATEGORY VEC WHERE CT.category_id=VEC.employee_category_id start with CT.category_due_id="+categoryDueId+" ORDER BY VEC.EMPLOYEE_CATEGORY_NAME ASC";
			}
			query = (Query) session.createSQLQuery(sql);				
			objectList1 = query.list();
			
		}
	
		//criteria.addOrder(Order.asc("employeeCategoryName"));
		totalMatches = criteria.list();			
		medicalExamScheduleList = criteria.list();			
		mapObj.put("totalMatches", totalMatches);
		mapObj.put("medicalExamScheduleList", medicalExamScheduleList);			
		mapObj.put("objectList", objectList);
		mapObj.put("objectList1", objectList1);

	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		getHibernateUtils.getHibernateUtlis().CloseConnection();
	}
	return mapObj;
}
	
		
	@Override
	public String updateMedicalExamSchedule(Long categoryDueId,Long employeeCategoryId,Long fromMonth,Long toMonth,Long userId) {	
		String result="";
		try {
			List<CategoryDue> medicalExamScheduleList = new ArrayList<CategoryDue>();
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();

			if (categoryDueId != 0) {
				Object medicalExamScheduleObject = session.load(CategoryDue.class, categoryDueId);
				CategoryDue categoryDue = (CategoryDue) medicalExamScheduleObject;

				Transaction transaction = session.beginTransaction();
				
				categoryDue.setFromMonth(fromMonth);
				categoryDue.setToMonth(toMonth);
				
				MasEmployeeCategory employeeCategory = new MasEmployeeCategory();
				employeeCategory.setEmployeeCategoryId(employeeCategoryId);				
				categoryDue.setMasEmployeeCategory(employeeCategory);

				Users users = new Users();
				users.setUserId(userId);
				categoryDue.setUser(users);
				
				long d = System.currentTimeMillis();
				Timestamp date = new Timestamp(d);

				session.update(categoryDue);
				transaction.commit();
				result = "200";
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return result;
	}

	@Override
	public String updateMedicalExamScheduleStatus(Long categoryDueId,String status) {
		
		String result = "";
		try {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();	
				
				Object medicalExamScheduleObject =  session.load(CategoryDue.class, categoryDueId);
				CategoryDue categoryDue = (CategoryDue)medicalExamScheduleObject;				
				Transaction transaction = session.beginTransaction();
				if(categoryDue.getStatus().equalsIgnoreCase("y")){
					categoryDue.setStatus("n");
					//result="400";
				}else if(categoryDue.getStatus().equalsIgnoreCase("n")) {
					categoryDue.setStatus("y");
					//result="200";
				}else {
					categoryDue.setStatus("y");
				}
				session.update(categoryDue);
				transaction.commit();
				session.flush();
				result="200";
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	@Override
	public List<MasEmployeeCategory> getRankCategoryList() {
		List<MasEmployeeCategory> mEmpCategory = new ArrayList<MasEmployeeCategory>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasEmployeeCategory.class);

			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("employeeCategoryId").as("employeeCategoryId"));
			projectionList.add(Projections.property("employeeCategoryName").as("employeeCategoryName"));
			criteria.setProjection(projectionList);
			criteria.addOrder(Order.asc("employeeCategoryName"));
			mEmpCategory = criteria.setResultTransformer(new AliasToBeanResultTransformer(MasEmployeeCategory.class)).list();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mEmpCategory;
	}
	
	/**----------------FWc Master--------------------*/

	@Override
	public List<MasHospital> validateFWC(String hospitalName) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria criteria = null;
		criteria = session.createCriteria(MasHospital.class)
							.add(Restrictions.eq("hospitalName", hospitalName));
		List<MasHospital> FWCList = criteria.list();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return FWCList;
	}
	
	@Override
	public List<MasHospital> validateFWCUpdate(Long hospitalId1, String hospitalName){
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		List<MasHospital> FWCList = new ArrayList<MasHospital>();
		Criteria criteria = session.createCriteria(MasHospital.class);
		criteria.add(Restrictions.and(Restrictions.eq("hospitalName", hospitalName)
				,Restrictions.eq("hospitalParentId", hospitalId1)));
		FWCList = criteria.list();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return FWCList;
	}
	

	@Override
	public String addFWC(MasHospital masHospital) {
		String result="";
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();		
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(masHospital);
			tx.commit();
			
			if(savedObj!=null) {
				result = "200";
			}else {
				result = "500";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public MasHospital chkFWC(String hospitalName) {
		
		MasHospital masHospital = new MasHospital();	
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria criteria =  session.createCriteria(MasHospital.class);
		criteria.add(Restrictions.eq("hospitalName", hospitalName));
		masHospital = (MasHospital)criteria.uniqueResult();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		
		return masHospital;
	}

	@Override
	public Map<String, List<MasHospital>>  getAllFWC(JSONObject jsonObj){
		Map<String, List<MasHospital>> mapObj = new HashMap<String, List<MasHospital>>();
		int pageSize = 5;
		int pageNo=1;
		if(jsonObj.get("hospitalFlag")!=null) {
			String hospitalFlag = "";
		List totalMatches = new ArrayList();
		 
		List<MasHospital> FWCList = new ArrayList<MasHospital>();
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasHospital.class);	
			if (jsonObj.has("hospitalFlag"))
			 {
				hospitalFlag = jsonObj.get("hospitalFlag").toString();
				  if(jsonObj.get("hospitalFlag").toString().length()>0 && !jsonObj.get("hospitalFlag").toString().trim().equalsIgnoreCase("")) {
						criteria.add(Restrictions.eq("hospitalFlag", hospitalFlag));
					}
			 }
			if( jsonObj.get("PN")!=null )
			 pageNo = Integer.parseInt(jsonObj.get("PN").toString());
			String hospitalName="";
				 if (jsonObj.has("hospitalName"))
				 {
					  hospitalName = "%"+jsonObj.get("hospitalName")+"%";
					  if(jsonObj.get("hospitalName").toString().length()>0 && !jsonObj.get("hospitalName").toString().trim().equalsIgnoreCase("")) {
							criteria.add(Restrictions.ilike("hospitalName", hospitalName));
						}
				 }	
				 
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("hospitalId").as("hospitalId"));
			projectionList.add(Projections.property("hospitalName").as("hospitalName"));
			projectionList.add(Projections.property("status").as("status"));
			projectionList.add(Projections.property("hospitalParent").as("hospitalParent"));
			projectionList.add(Projections.property("hospitalFlag").as("hospitalFlag"));
			criteria.addOrder(Order.asc("hospitalName"));
			
			totalMatches = criteria.list();			
			
			if(! jsonObj.get("PN").equals("0"))
			{
			criteria.setFirstResult((pageSize) * (pageNo - 1));
			criteria.setProjection(projectionList).setMaxResults(pageSize);
			FWCList = criteria.setResultTransformer(new AliasToBeanResultTransformer(MasHospital.class)).list();
			}
			if(jsonObj.get("PN").equals("0")){
				if(!jsonObj.get("hospitalParentId").toString().equalsIgnoreCase("All")) {
					criteria.createAlias("hospitalParent", "hp");
					criteria.add(Restrictions.eq("hp.hospitalParentId",Long.parseLong(jsonObj.get("hospitalParentId").toString())));
					}
				criteria.add(Restrictions.eq("status","y").ignoreCase());
				criteria.setProjection(projectionList);
				FWCList = criteria.setResultTransformer(new AliasToBeanResultTransformer(MasHospital.class)).list();
			}
			
		
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			mapObj.put("FWCList", FWCList);
			mapObj.put("totalMatches", totalMatches);
		}
			return mapObj;
		}	

	

	@Override
	public String updateFWC(Long hospitalId, String hospitalName, Long hospitalParentId,Long userId) {	
		String result="";
		
		List<MasHospital> msFWCList=validateFWCUpdate(hospitalParentId,hospitalName);
		
			try {
				Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				MasHospital masHospital =  (MasHospital)session.get(MasHospital.class, hospitalId);
				
				if(masHospital != null)
				{
				
				Transaction transaction = session.beginTransaction();
				if(msFWCList !=null && msFWCList.size()>0)
				{
					masHospital.setHospitalName(masHospital.getHospitalName());
				}
				else {
					masHospital.setHospitalName(hospitalName);
				}
				masHospital.setStatus(masHospital.getStatus());				
				Users usr = new Users();
				usr.setUserId(userId);
				masHospital.setUser(usr);
				
				masHospital.setHospitalParentId(hospitalParentId);				
				long d = System.currentTimeMillis();
				Timestamp date = new Timestamp(d);				
				masHospital.setLastChgDate(date);				
				session.update(masHospital);				
				transaction.commit();
				session.flush();
				result="200";	
				}	
								
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
		
		
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		
		
		return result;
	}

	@Override
	public String updateFWCStatus(Long hospitalId, String status) {
		
		String result = "";
		try {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();	
				
				Object FWCObject =  session.load(MasHospital.class, hospitalId);
				MasHospital masHospital = (MasHospital)FWCObject;				
				Transaction transaction = session.beginTransaction();
				if(masHospital.getStatus().equalsIgnoreCase("y")){
					masHospital.setStatus("n");
					//result="400";
				}else if(masHospital.getStatus().equalsIgnoreCase("n")) {
					masHospital.setStatus("y");
					//result="200";
				}else {
					masHospital.setStatus("y");
				}
				session.update(masHospital);
				transaction.commit();
				session.flush();
				result="200";
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	@Override
	public List<MasHospital> getMIRoomList() {
		List<MasHospital> miRoomList = new ArrayList<MasHospital>();
		try {	 
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria criteria =  session.createCriteria(MasHospital.class);
		
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("hospitalId").as("hospitalId"));
		projectionList.add(Projections.property("hospitalParentId").as("hospitalParentId"));
		projectionList.add(Projections.property("hospitalName").as("hospitalName"));
		criteria.setProjection(projectionList);
		miRoomList = criteria.setResultTransformer(new AliasToBeanResultTransformer(MasHospital.class)).add(Restrictions.isNull("hospitalParentId")).add(Restrictions.eq("status", 'Y').ignoreCase()).addOrder(Order.asc("hospitalName")).list();
		
		}catch(Exception e) {
			e.printStackTrace();
		}
		return miRoomList;
	}
   
	@Override
	public List<MasDiseaseType> validateDiseaseType(String diseaseTypeCode, String diseaseTypeName) {
		List<MasDiseaseType> diseaseTypeList =  new ArrayList<MasDiseaseType>();	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria = session.createCriteria(MasDiseaseType.class);
				criteria.add(Restrictions.or(Restrictions.eq("diseaseTypeCode", diseaseTypeCode).ignoreCase(), Restrictions.eq("diseaseTypeName", diseaseTypeName).ignoreCase()));
				diseaseTypeList = criteria.list();
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		return diseaseTypeList;
	}
	
	@Override
	public String addDiseaseType(MasDiseaseType masDiseaseType) {
		String result="";	
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(masDiseaseType);
			tx.commit();
			if(savedObj!=null) {
				result="200";
			}else {
				result = "500";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		
		return result;
	}
	
	
	@Override
	public Map<String, List<MasDiseaseType>> getAllDiseaseType(JSONObject jsondata) {
		Map<String, List<MasDiseaseType>> map = new HashMap<String, List<MasDiseaseType>>();
		List<MasDiseaseType> diseaseTypeList = new ArrayList<MasDiseaseType>();
		List totalMatches  =new ArrayList<>();
		int pageNo=1;
		int pageSize = 5;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasDiseaseType.class);					
			if(!jsondata.get("PN").toString().equals("0")) {
			 pageNo = Integer.parseInt(jsondata.get("PN").toString());
					
			String diseaseTypeName="";
				 if (jsondata.has("diseaseTypeName"))
				 {
					 diseaseTypeName = "%"+jsondata.get("diseaseTypeName")+"%";
					  if(jsondata.get("diseaseTypeName").toString().length()>0 && !jsondata.get("diseaseTypeName").toString().trim().equalsIgnoreCase("")) {
							criteria.add(Restrictions.ilike("diseaseTypeName", diseaseTypeName));
							
						}
				 }
				 criteria.addOrder(Order.asc("diseaseTypeName"));
				 totalMatches = criteria.list();				 
				 criteria.setFirstResult((pageSize) * (pageNo - 1));
				 criteria.setMaxResults(pageSize);
				 diseaseTypeList = criteria.list();
			}
		 if(jsondata.get("PN").toString().equals("0")) {
			 criteria.addOrder(Order.asc("diseaseTypeName"));
			 criteria.add(Restrictions.eq("status", "y").ignoreCase());
			 totalMatches = criteria.list();				 
			 diseaseTypeList = criteria.list();
		 }
	
		map.put("diseaseTypeList", diseaseTypeList);
		map.put("totalMatches", totalMatches);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}
	
	@Override
	public String updateDiseaseTypeStatus(Long id, String status) {
		
		String result = "";
		try {

			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Object dgmasObject = session.load(MasDiseaseType.class, id);
			MasDiseaseType masdis = (MasDiseaseType) dgmasObject;
			Transaction transaction = session.beginTransaction();
			if (masdis.getStatus().equalsIgnoreCase("y")) {
				masdis.setStatus("n");

			} else if (masdis.getStatus().equalsIgnoreCase("n")) {
				masdis.setStatus("y");

			} else {
				masdis.setStatus("y");
			}

			session.update(masdis);
			transaction.commit();
			session.flush();
			result = "200";
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	@Override
	public String updateDiseaseType(MasDiseaseType masDiseaseType) {
		String result="";
		Session session = null;
		try {
			List<MasDiseaseType> masDiseaseTypeList = validateDiseaseType(masDiseaseType.getDiseaseTypeCode(),masDiseaseType.getDiseaseTypeName());
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Transaction transaction = session.beginTransaction();
					Object object = session.load(MasDiseaseType.class, masDiseaseType.getDiseaseTypeId());
					MasDiseaseType masDiseaseObj = (MasDiseaseType)object;	
					
						if(masDiseaseTypeList.get(0).getDiseaseTypeName().equalsIgnoreCase(masDiseaseType.getDiseaseTypeName())) {
						masDiseaseObj.setDiseaseTypeName(masDiseaseObj.getDiseaseTypeName());
					 }
					else {
							masDiseaseObj.setDiseaseTypeName(masDiseaseType.getDiseaseTypeName());	
						}
					
					masDiseaseObj.setLastChgBy(masDiseaseType.getLastChgBy());			
					masDiseaseObj.setLastChgDate(masDiseaseType.getLastChgDate());
					session.update(masDiseaseObj);
					session.flush();
					transaction.commit();					
					result = "success";
			
											
		}catch(Exception e) {
			
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	@Override
	public String addDiseaseMapping(MasDiseaseMapping masDiseaseMapping) {
		String result="";	
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(masDiseaseMapping);
			tx.commit();
			if(savedObj!=null) {
				result="200";
			}else {
				result = "500";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		
		return result;
	}
	
	@Override
	public boolean validateDiseaseById(Long diseaseId) {
		boolean flag=false;
		List<MasDiseaseMapping> validList =  new ArrayList<MasDiseaseMapping>();	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria = session.createCriteria(MasDiseaseMapping.class);
				criteria.add(Restrictions.eq("diseaseId", diseaseId));
				validList = criteria.list();
				if(validList !=null && validList.size()>0) {
					flag=true;
				}
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		return flag;
	}
	
	@Override
	public Map<String, List<MasDiseaseMapping>> getAllDiseaseMapping(JSONObject jsondata) {
		Map<String, List<MasDiseaseMapping>> map = new HashMap<String, List<MasDiseaseMapping>>();
		List<MasDiseaseMapping> diseaseMappingList = new ArrayList<MasDiseaseMapping>();
		List totalMatches  =new ArrayList<>();
		int pageNo=1;
		int pageSize = 5;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasDiseaseMapping.class);					
			criteria.createAlias("masDisease", "md");
			 pageNo = Integer.parseInt(jsondata.get("PN").toString());
					
			String diseaseName="";
				 if (jsondata.has("diseaseName"))
				 {
					 diseaseName = "%"+jsondata.get("diseaseName")+"%";
					  if(jsondata.get("diseaseName").toString().length()>0 && !jsondata.get("diseaseName").toString().trim().equalsIgnoreCase("")) {
							criteria.add(Restrictions.ilike("md.diseaseName", diseaseName));
							
						}
				 }
				 criteria.addOrder(Order.asc("md.diseaseName"));
				 totalMatches = criteria.list();				 
				 criteria.setFirstResult((pageSize) * (pageNo - 1));
				 criteria.setMaxResults(pageSize);
				 diseaseMappingList = criteria.list();
		
		map.put("diseaseMappingList", diseaseMappingList);
		map.put("totalMatches", totalMatches);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}
	
	@Override
	public String updateDiseaseMappingStatus(Long id, String status) {
		
		String result = "";
		try {

			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Object dgmasObject = session.load(MasDiseaseMapping.class, id);
			MasDiseaseMapping masdismap = (MasDiseaseMapping) dgmasObject;
			Transaction transaction = session.beginTransaction();
			if (masdismap.getStatus().equalsIgnoreCase("y")) {
				masdismap.setStatus("n");

			} else if (masdismap.getStatus().equalsIgnoreCase("n")) {
				masdismap.setStatus("y");

			} else {
				masdismap.setStatus("y");
			}

			session.update(masdismap);
			transaction.commit();
			session.flush();
			result = "200";
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	@Override
	public String updateDiseaseMapping(MasDiseaseMapping masDiseaseMapping) {
		String result="";
		Session session = null;
		try {
			boolean flag = validateDiseaseById(masDiseaseMapping.getDiseaseId());
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Transaction transaction = session.beginTransaction();
					Object object = session.load(MasDiseaseMapping.class, masDiseaseMapping.getDiseaseMappingId());
					MasDiseaseMapping masDiseaseMapObj = (MasDiseaseMapping)object;	
					if(flag) {
							masDiseaseMapObj.setDiseaseId(masDiseaseMapObj.getMasDisease().getDiseaseId());
					 }
					else {
						masDiseaseMapObj.setDiseaseId(masDiseaseMapping.getDiseaseId());	
						}
					masDiseaseMapObj.setIcdId(masDiseaseMapping.getIcdId());
					masDiseaseMapObj.setLastChgBy(masDiseaseMapping.getLastChgBy());			
					masDiseaseMapObj.setLastChgDate(masDiseaseMapping.getLastChgDate());
					session.update(masDiseaseMapObj);					
					session.flush();
					session.clear();
					transaction.commit();					
					result = "success";
			
											
		}catch(Exception e) {
			
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public String getMasIcdById(String masicdId) {
		List<MasIcd> masicdList = null;
		String masicdName = "";
		String masicdNameId = "";

		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			String[] masicdArray = masicdId.split(",");
			List<Long> listMasssIcd = new ArrayList<>();
			for (String ss : masicdArray) {
				listMasssIcd.add(Long.parseLong(ss.trim()));
			}
			masicdList = session.createCriteria(MasIcd.class).add(Restrictions.in("icdId", listMasssIcd)).list();
			if (CollectionUtils.isNotEmpty(masicdList)) {
				int count = 0;
				for (MasIcd masicd : masicdList) {
					if (count == 0) {
						masicdName = masicd.getIcdName();
						masicdNameId = "" + masicd.getIcdId();
					} else {
						masicdName += "," + masicd.getIcdName();
						masicdNameId += "," + masicd.getIcdId();
					}
					count++;
				}
			}

		} catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return masicdName + "##" + masicdNameId;
	}
	
	/**************************************
	 *MMU Master   dated 21-08-2021
	 **************************************************/
	
	@Override
	public String addMMU(MasMMU massMMU) {
		String result="";	
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(massMMU);
			tx.commit();
			if(savedObj!=null) {
				result="200";
			}else {
				result = "500";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		
		return result;
	}
	
	@Override
	public String updateMMUStatus(Long id, String status) {
		
		String result = "";
		try {

			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Object mmuObject = session.load(MasMMU.class, id);
			MasMMU mmu = (MasMMU) mmuObject;
			Transaction transaction = session.beginTransaction();
			if (mmu.getStatus().equalsIgnoreCase("Y")) {
				mmu.setStatus("n");

			} else if (mmu.getStatus().equalsIgnoreCase("n")) {
				mmu.setStatus("y");

			} else {
				mmu.setStatus("y");
			}

			session.update(mmu);
			transaction.commit();
			session.flush();
			result = "200";
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	@Override
	public Map<String, List<MasMMU>> getAllMMU(JSONObject jsondata) {
		Map<String, List<MasMMU>> map = new HashMap<String, List<MasMMU>>();
		List<MasMMU> mmuList = new ArrayList<MasMMU>();
		List totalMatches  =new ArrayList<>();
		int pageNo=1;
		int pageSize = 5;
		Long vendorId=null;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasMMU.class).createAlias("masMmuVendor", "vid");					
			if(jsondata.get("PN") !=null && !jsondata.get("PN").equals("0") && !jsondata.get("PN").equals("")) {
			 pageNo = Integer.parseInt(jsondata.get("PN").toString());
					
			
				 /*if (jsondata.has("cityName"))
				 {
					 
					 //cityName = "%"+jsondata.get("mmuName")+"%";
					 
					  if(!jsondata.get("cityName").toString().trim().equalsIgnoreCase("")) {
						  Long cityId=jsondata.getLong("cityName");
							criteria.add(Restrictions.eq("cityId", cityId));
							
						}
				 }*/
			 if (jsondata.has("mmuSearch"))
			 {
				 
				 //cityName = "%"+jsondata.get("mmuName")+"%";
				 
				  if(!jsondata.get("mmuSearch").toString().trim().equalsIgnoreCase("")) {
					 // Long cityId=jsondata.getLong("cityName");
					 String mmuName =jsondata.get("mmuSearch").toString().trim();
						criteria.add(Restrictions.ilike("mmuName", "%"+mmuName+"%"));
						
					}
			 }
				 criteria.addOrder(Order.asc("mmuName"));
				 totalMatches = criteria.list();
				if (pageNo > 0) {
					criteria.setFirstResult((pageSize) * (pageNo - 1));
					criteria.setMaxResults(pageSize);
				}
				 mmuList = criteria.list();
			}
			
		if(jsondata.get("PN").equals("0")) {
			criteria.addOrder(Order.asc("mmuName"));
			criteria.add(Restrictions.eq("status", "y").ignoreCase());
			 totalMatches = criteria.list();
			 mmuList = criteria.list();
		}
		
		if(jsondata.has("vendorId")) {
			vendorId= Long.parseLong(jsondata.get("vendorId").toString());
			criteria.addOrder(Order.asc("mmuName"));
			criteria.add(Restrictions.eq("status", "y").ignoreCase());
			criteria.add(Restrictions.eq("vid.mmuVendorId", vendorId));
			 totalMatches = criteria.list();
			 mmuList = criteria.list();
		}
		
		
		map.put("mmuList", mmuList);
		map.put("totalMatches", totalMatches);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}
	
	@Override
	public String updateMMU(MasMMU masmmu) {
		String result="";
		Session session = null;
		try {
			 
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Transaction transaction = session.beginTransaction();
					Object object = session.load(MasMMU.class, masmmu.getMmuId());
					MasMMU masMMUObj = (MasMMU)object;	
					
								
					masMMUObj.setMmuName(masmmu.getMmuName());
					masMMUObj.setStatus(masmmu.getStatus());
					masMMUObj.setLastChgBy(masmmu.getLastChgBy());
					masMMUObj.setLastChgDate(masmmu.getLastChgDate());
					
					if(!masMMUObj.getMmuNo().equalsIgnoreCase(masmmu.getMmuNo())) {
						masMMUObj.setMmuNo(masmmu.getMmuNo());	
					}					
				 	//masMMUObj.setCityId(masmmu.getCityId());
					masMMUObj.setOprationalDate(masmmu.getOprationalDate());
					masMMUObj.setMmuTypeId(masmmu.getMmuTypeId());
					masMMUObj.setMmuVendorId(masmmu.getMmuVendorId());
					masMMUObj.setOfficeId(masmmu.getOfficeId());
					masMMUObj.setPollNo(masmmu.getPollNo());
					masMMUObj.setChassisNo(masmmu.getChassisNo());
					masMMUObj.setDistrictId(masmmu.getDistrictId());
					session.update(masMMUObj);					
					session.flush();
					session.clear();
					transaction.commit();					
					result = "success";
			
											
		}catch(Exception e) {
			
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	@Override
	public Map<String, List<MasCity>> getAllCity(JSONObject jsondata) {
		Map<String, List<MasCity>> map = new HashMap<String, List<MasCity>>();
		List<MasCity> mascityList = new ArrayList<MasCity>();
		List totalMatches  =new ArrayList<>();
		int pageNo=1;
		int pageSize = 5;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasCity.class).createAlias("masDistrict", "did");					
			if(!jsondata.get("PN").toString().equals("0")) {
			 pageNo = Integer.parseInt(jsondata.get("PN").toString());
					
			String cityName="";
				 if (jsondata.has("cityName"))
				 {
					 cityName = "%"+jsondata.get("cityName")+"%";
					  if(jsondata.get("cityName").toString().length()>0 && !jsondata.get("cityName").toString().trim().equalsIgnoreCase("")) {
							criteria.add(Restrictions.ilike("cityName", cityName));
							
						}
				 }
				 criteria.addOrder(Order.asc("cityName"));
				 totalMatches = criteria.list();				 
				 criteria.setFirstResult((pageSize) * (pageNo - 1));
				 criteria.setMaxResults(pageSize);
				 mascityList = criteria.list();
			}
			
			
		 
			if(jsondata.has("districtId") && jsondata.get("PN").toString().equals("0") && !jsondata.get("districtId").equals("") ) {
				 criteria.addOrder(Order.asc("cityName"));
				 if(!jsondata.get("districtId").toString().equals("0")) {
				 criteria.add(Restrictions.eq("did.districtId", Long.parseLong(jsondata.get("districtId").toString())));
				 }
				 criteria.add(Restrictions.eq("status", "y").ignoreCase());
				 totalMatches = criteria.list();				 
				 mascityList = criteria.list();
			 }	
			
		 if(jsondata.get("PN").toString().equals("0")) {
			 criteria.addOrder(Order.asc("cityName"));
			 criteria.add(Restrictions.eq("status", "y").ignoreCase());
			 totalMatches = criteria.list();				 
			 mascityList = criteria.list();
		 }
		 
	
		map.put("mascityList", mascityList);
		map.put("totalMatches", totalMatches);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}
	
	@Override
	public Map<String, List<MasMmuVendor>> getAllMMUVendor(JSONObject jsondata) {
		Map<String, List<MasMmuVendor>> map = new HashMap<String, List<MasMmuVendor>>();
		List<MasMmuVendor> masvendorList = new ArrayList<MasMmuVendor>();
		List totalMatches  =new ArrayList<>();
		int pageNo=1;
		int pageSize = 5;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasMmuVendor.class);					
			if(!jsondata.get("PN").toString().equals("0")) {
			 pageNo = Integer.parseInt(jsondata.get("PN").toString());
					
			String vendorName="";
				 if (jsondata.has("vendorName"))
				 {
					 vendorName = "%"+jsondata.get("vendorName")+"%";
					  if(jsondata.get("vendorName").toString().length()>0 && !jsondata.get("vendorName").toString().trim().equalsIgnoreCase("")) {
							criteria.add(Restrictions.ilike("mmuVendorName", vendorName));
							
						}
				 }
				 criteria.addOrder(Order.asc("mmuVendorName"));
				 totalMatches = criteria.list();				 
				 criteria.setFirstResult((pageSize) * (pageNo - 1));
				 criteria.setMaxResults(pageSize);
				 masvendorList = criteria.list();
			}
		 if(jsondata.get("PN").toString().equals("0")) {
			 criteria.addOrder(Order.asc("mmuVendorName"));
			 criteria.add(Restrictions.eq("status", "y").ignoreCase());
			 totalMatches = criteria.list();				 
			 masvendorList = criteria.list();
		 }
	
		map.put("masvendorList", masvendorList);
		map.put("totalMatches", totalMatches);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}
	
	@Override
	public Map<String, List<MasMmuType>> getAllMMUType(JSONObject jsondata) {
		Map<String, List<MasMmuType>> map = new HashMap<String, List<MasMmuType>>();
		List<MasMmuType> masmmutypeList = new ArrayList<MasMmuType>();
		List totalMatches  =new ArrayList<>();
		int pageNo=1;
		int pageSize = 5;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasMmuType.class);					
			if(!jsondata.get("PN").toString().equals("0")) {
			 pageNo = Integer.parseInt(jsondata.get("PN").toString());
					
			String typeName="";
				 if (jsondata.has("typeName"))
				 {
					 typeName = "%"+jsondata.get("typeName")+"%";
					  if(jsondata.get("typeName").toString().length()>0 && !jsondata.get("typeName").toString().trim().equalsIgnoreCase("")) {
							criteria.add(Restrictions.ilike("mmuVendorName", typeName));
							
						}
				 }
				 criteria.addOrder(Order.asc("mmuTypeName"));
				 totalMatches = criteria.list();				 
				 criteria.setFirstResult((pageSize) * (pageNo - 1));
				 criteria.setMaxResults(pageSize);
				 masmmutypeList = criteria.list();
			}
		 if(jsondata.get("PN").toString().equals("0")) {
			 criteria.addOrder(Order.asc("mmuTypeName"));
			 criteria.add(Restrictions.eq("status", "y").ignoreCase());
			 totalMatches = criteria.list();				 
			 masmmutypeList = criteria.list();
		 }
	
		map.put("masmmutypeList", masmmutypeList);
		map.put("totalMatches", totalMatches);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}
	
	@Override
	public String validateRegNo(JSONObject jsondata) {
		
		String result="";
		String regNo="";
		List<MasMMU> list=new ArrayList<MasMMU>();
		try {
			regNo=jsondata.getString("regNo").toString();
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasMMU.class);					
			list=criteria.add(Restrictions.eq("mmuNo", regNo).ignoreCase()).list();
		 if(list !=null && list.size()>0 ) {
			 result="exists";
		 }
	
		
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	@Override
	public String addUserType(MasUserType masUserType) {
		String result="";	
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(masUserType);
			tx.commit();
			if(savedObj!=null) {
				result="200";
			}else {
				result = "500";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		
		return result;
	}
	
	@Override
	public Map<String, List<MasUserType>> getAllUserType(JSONObject jsondata) {
		Map<String, List<MasUserType>> map = new HashMap<String, List<MasUserType>>();
		List<MasUserType> userTypeList = new ArrayList<MasUserType>();
		List totalMatches  =new ArrayList<>();
		int pageNo=1;
		int pageSize = 5;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasUserType.class);					
			if(!jsondata.get("PN").toString().equals("0")) {
			 pageNo = Integer.parseInt(jsondata.get("PN").toString());
					
			String userTypeName="";
				 if (jsondata.has("userTypeName"))
				 {
					 userTypeName = "%"+jsondata.get("userTypeName")+"%";
					  if(jsondata.get("userTypeName").toString().length()>0 && !jsondata.get("userTypeName").toString().trim().equalsIgnoreCase("")) {
							criteria.add(Restrictions.ilike("userTypeName", userTypeName));
							
						}
				 }
				 criteria.addOrder(Order.asc("userTypeName"));
				 totalMatches = criteria.list();				 
				 criteria.setFirstResult((pageSize) * (pageNo - 1));
				 criteria.setMaxResults(pageSize);
				 userTypeList = criteria.list();
			}
		 if(jsondata.get("PN").toString().equals("0")) {
			 criteria.addOrder(Order.asc("userTypeName"));
			 criteria.add(Restrictions.eq("status", "y").ignoreCase());
			 totalMatches = criteria.list();				 
			 userTypeList = criteria.list();
		 }
	
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
	public String updateUserTypeStatus(Long id, String status) {
		
		String result = "";
		try {

			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Object mmuObject = session.load(MasUserType.class, id);
			MasUserType mmu = (MasUserType) mmuObject;
			Transaction transaction = session.beginTransaction();
			if (mmu.getStatus().equalsIgnoreCase("Y")) {
				mmu.setStatus("n");

			} else if (mmu.getStatus().equalsIgnoreCase("n")) {
				mmu.setStatus("y");

			} else {
				mmu.setStatus("y");
			}

			session.update(mmu);
			transaction.commit();
			session.flush();
			result = "200";
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	
	@Override
	public String updateUserType(MasUserType masUserType) {
		String result="";
		Session session = null;
		
		try {
			List<MasUserType> validate= validateMasUserType(masUserType.getUserTypeCode(), masUserType.getUserTypeName());
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Transaction transaction = session.beginTransaction();
					Object object = session.load(MasUserType.class, masUserType.getUserTypeId());
					MasUserType userTypeObj = (MasUserType)object;
					userTypeObj.setUserTypeCode(masUserType.getUserTypeCode());				
					
					if(validate.get(0).getUserTypeName().equalsIgnoreCase(masUserType.getUserTypeName())) {
							userTypeObj.setUserTypeName(userTypeObj.getUserTypeName());	
						}					
					else {
						userTypeObj.setUserTypeName(masUserType.getUserTypeName());
					}
					userTypeObj.setStatus(masUserType.getStatus());
					userTypeObj.setMmuStaff(masUserType.getMmuStaff());
					userTypeObj.setLastChgBy(masUserType.getLastChgBy());
					userTypeObj.setLastChgDate(masUserType.getLastChgDate());					
					session.update(userTypeObj);					
					session.flush();
					session.clear();
					transaction.commit();					
					result = "success";
			
											
		}catch(Exception e) {
			
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	@Override
	public List<MasUserType> validateMasUserType(String userTypeCode, String userTypeName) {
		List<MasUserType> userTypeList = new ArrayList<MasUserType>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasUserType.class);
			criteria.add(Restrictions.or(Restrictions.eq("userTypeCode", userTypeCode),
					Restrictions.eq("userTypeName", userTypeName)));
			userTypeList = criteria.list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return userTypeList;
	}
	
	
	@Override
	public String addCity(MasCity masCity) {
		String result="";	
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(masCity);
			tx.commit();
			if(savedObj!=null) {
				result="200";
			}else {
				result = "500";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		
		return result;
	}
	
	@Override
	public List<MasCity> validateMasCity(String cityCode, String cityName) {
		List<MasCity> validCityList = new ArrayList<MasCity>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasCity.class);
			criteria.add(Restrictions.or(Restrictions.eq("cityCode", cityCode),
					Restrictions.eq("cityName", cityName)));
			validCityList = criteria.list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return validCityList;
	}
	
	@Override
	public String updateCityStatus(Long id, String status,String indentCity) {
		
		String result = "";
		try {

			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Object mmuObject = session.load(MasCity.class, id);
			MasCity mmu = (MasCity) mmuObject;
			Transaction transaction = session.beginTransaction();
			if (mmu.getStatus().equalsIgnoreCase("Y")) {
				mmu.setStatus("n");

			} else if (mmu.getStatus().equalsIgnoreCase("n")) {
				mmu.setStatus("y");

			} else {
				mmu.setStatus("y");
			}
			mmu.setIndentCity(indentCity);
			session.update(mmu);
			transaction.commit();
			session.flush();
			result = "200";
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	@Override
	public String updateCity(MasCity masCity) {
		String result="";
		Session session = null;
		
		try {
			List<MasCity> validate= validateMasCity(masCity.getCityCode(), masCity.getCityName());
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Transaction transaction = session.beginTransaction();
					Object object = session.load(MasCity.class, masCity.getCityId());
					MasCity masCityObj = (MasCity)object;
					masCityObj.setCityCode(masCity.getCityCode());				
					
					if(validate.get(0).getCityName().equalsIgnoreCase(masCity.getCityName())) {
						masCityObj.setCityName(masCityObj.getCityName());	
						}					
					else {
						masCityObj.setCityName(masCity.getCityName());
					}
					masCityObj.setMasDistrict(masCity.getMasDistrict());
					masCityObj.setMmuOperational(masCity.getMmuOperational());
					masCityObj.setStatus(masCity.getStatus());
					masCityObj.setLastChangeBy(masCity.getLastChangeBy());
					masCityObj.setLastChangeDate(masCity.getLastChangeDate());	
					masCityObj.setOrderNo(masCity.getOrderNo());
					masCityObj.setIndentCity(masCity.getIndentCity());
					session.update(masCityObj);					
					session.flush();
					session.clear();
					transaction.commit();					
					result = "success";
			
											
		}catch(Exception e) {
			
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	@Override
	public Map<String, List<MasDistrict>> getAllDistrict(JSONObject jsondata) {
		Map<String, List<MasDistrict>> map = new HashMap<String, List<MasDistrict>>();
		List<MasDistrict> districtList = new ArrayList<MasDistrict>();
		List totalMatches  =new ArrayList<>();
		int pageNo=1;
		int pageSize = 5;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasDistrict.class);					
			if(!jsondata.get("PN").toString().equals("0")) {
			 pageNo = Integer.parseInt(jsondata.get("PN").toString());
					
			String districtName="";
				 if (jsondata.has("districtName"))
				 {
					 districtName = "%"+jsondata.get("districtName")+"%";
					  if(jsondata.get("districtName").toString().length()>0 && !jsondata.get("districtName").toString().trim().equalsIgnoreCase("")) {
							criteria.add(Restrictions.ilike("districtName", districtName));
							
						}
				 }
							
				 criteria.addOrder(Order.asc("districtName"));
				 totalMatches = criteria.list();				 
				 criteria.setFirstResult((pageSize) * (pageNo - 1));
				 criteria.setMaxResults(pageSize);
				 districtList = criteria.list();
			}
		 if(jsondata.get("PN").toString().equals("0")) {
			 criteria.addOrder(Order.asc("districtName"));
			 criteria.add(Restrictions.eq("status", "y").ignoreCase());
			 if (jsondata.has("vendorId"))
			 {
				Long vendorId=Long.parseLong(jsondata.getString("vendorId").toString()); 
				Criterion vendorIdEq = Restrictions.eq("vendorId", vendorId);
			    Criterion vendorIdNull = Restrictions.isNull("vendorId");
			    criteria.add(Restrictions.or(vendorIdEq, vendorIdNull));
			 }	
			 totalMatches = criteria.list();				 
			 districtList = criteria.list();
		 }
	
		map.put("districtList", districtList);
		map.put("totalMatches", totalMatches);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}
	
	@Override
	public List<MasZone> validateMasZone(String zoneCode, String zoneName) {
		List<MasZone> zoneList = new ArrayList<MasZone>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasZone.class);
			criteria.add(Restrictions.or(Restrictions.eq("zoneCode", zoneCode),
					Restrictions.eq("zoneName", zoneName)));
			zoneList = criteria.list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return zoneList;
	}
	
	@Override
	public String addZone(MasZone masZone) {
		String result="";	
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(masZone);
			tx.commit();
			if(savedObj!=null) {
				result="200";
			}else {
				result = "500";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		
		return result;
	}
	
	@Override
	public Map<String, List<MasZone>> getAllZone(JSONObject jsondata) {
		Map<String, List<MasZone>> map = new HashMap<String, List<MasZone>>();
		List<MasZone> zoneList = new ArrayList<MasZone>();
		List totalMatches  =new ArrayList<>();
		int pageNo=1;
		int pageSize = 5;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasZone.class).createAlias("masCity", "mc");					
			if(!jsondata.get("PN").toString().equals("0")) {
			 pageNo = Integer.parseInt(jsondata.get("PN").toString());
					
			String zoneName="";
				 if (jsondata.has("zoneName"))
				 {
					 zoneName = "%"+jsondata.get("zoneName")+"%";
					  if(jsondata.get("zoneName").toString().length()>0 && !jsondata.get("zoneName").toString().trim().equalsIgnoreCase("")) {
							criteria.add(Restrictions.ilike("zoneName", zoneName));
							
						}
				 }
				 criteria.addOrder(Order.asc("zoneName"));
				 totalMatches = criteria.list();				 
				 criteria.setFirstResult((pageSize) * (pageNo - 1));
				 criteria.setMaxResults(pageSize);
				 zoneList = criteria.list();
			}
		if(!jsondata.has("cityId")) {	
		 if(jsondata.get("PN").toString().equals("0")) {
			 criteria.addOrder(Order.asc("zoneName"));
			 criteria.add(Restrictions.eq("status", "y").ignoreCase());
			 totalMatches = criteria.list();				 
			 zoneList = criteria.list();
		 }
		}
	     if(jsondata.has("cityId")) {
		 if(jsondata.get("PN").toString().equals("0")) {
			 criteria.addOrder(Order.asc("zoneName"));
			 criteria.add(Restrictions.eq("mc.cityId",Long.parseLong(jsondata.get("cityId").toString())));
			 criteria.add(Restrictions.eq("status", "y").ignoreCase());
			 totalMatches = criteria.list();				 
			 zoneList = criteria.list();
		 }
	     }
		map.put("zoneList", zoneList);
		map.put("totalMatches", totalMatches);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}
	
	@Override
	public String updateZoneStatus(Long id, String status) {
		
		String result = "";
		try {

			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Object mmuObject = session.load(MasZone.class, id);
			MasZone zone = (MasZone) mmuObject;
			Transaction transaction = session.beginTransaction();
			if (zone.getStatus().equalsIgnoreCase("Y")) {
				zone.setStatus("n");

			} else if (zone.getStatus().equalsIgnoreCase("n")) {
				zone.setStatus("y");

			} else {
				zone.setStatus("y");
			}

			session.update(zone);
			transaction.commit();
			session.flush();
			result = "200";
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	@Override
	public String updateZone(MasZone masZone) {
		String result="";
		Session session = null;
		
		try {
			List<MasZone> validate= validateMasZone(masZone.getZoneCode(), masZone.getZoneName());
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Transaction transaction = session.beginTransaction();
					Object object = session.load(MasZone.class, masZone.getZoneId());
					MasZone masZoneObj = (MasZone)object;
					masZoneObj.setZoneCode(masZone.getZoneCode());				
					
					if(validate.get(0).getZoneName().equalsIgnoreCase(masZone.getZoneName())) {
						masZoneObj.setZoneName(masZoneObj.getZoneName());	
						}					
					else {
						masZoneObj.setZoneName(masZone.getZoneName());	
					}
					masZoneObj.setMasCity(masZone.getMasCity());
					masZoneObj.setStatus(masZone.getStatus());
					masZoneObj.setLastChangeBy(masZone.getLastChangeBy());
					masZoneObj.setLastChangeDate(masZone.getLastChangeDate());					
					session.update(masZoneObj);					
					session.flush();
					session.clear();
					transaction.commit();					
					result = "success";
			
											
		}catch(Exception e) {
			
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	@Override
	public List<MasSymptoms> getAllSymptoms(JSONObject jsonObject) {
		List<MasSymptoms> list = null;
		Criteria criteria=null;
		try {
			
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			if(jsonObject.get("opdFlag").equals("opd"))
			{
				criteria = session.createCriteria(MasSymptoms.class)
						.add(Restrictions.eq("status", "Y").ignoreCase());	
			}
			else
			{	
				      criteria = session.createCriteria(MasSymptoms.class)
						.add(Restrictions.eq("status", "Y").ignoreCase())
						.add(Restrictions.ne("mostFrequentSymptoms", "Y").ignoreCase());
			}
			if(jsonObject.get("name") != null && !String.valueOf(jsonObject.get("name")).equals("") && !String.valueOf(jsonObject.get("name")).equals("null")) {
				String name = "%"+String.valueOf(jsonObject.get("name"))+"%";
				criteria = criteria.add(Restrictions.like("name", name).ignoreCase());
			}
			list = criteria.list();
			return list;
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return null;
	}
	
	@Override
	public String addWard(MasWard masWard) {
		String result="";	
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(masWard);
			tx.commit();
			if(savedObj!=null) {
				result="200";
			}else {
				result = "500";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		
		return result;
	}
	
	@Override
	public List<MasWard> validateWardCode(String wardCode) {
		List<MasWard> wardList = new ArrayList<MasWard>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasWard.class);
			criteria.add(Restrictions.eq("wardCode", wardCode));
			wardList = criteria.list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return wardList;
	}
	
	@Override
	public List<MasWard> validateWardName(String wardName, Long cityId) {
		List<MasWard> wardList = new ArrayList<MasWard>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasWard.class).createAlias("masCity", "mc");
			criteria.add(Restrictions.and(Restrictions.eq("wardName", wardName),
					Restrictions.eq("mc.cityId", cityId)));
			wardList = criteria.list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return wardList;
	}
	
	@Override
	public Map<String, List<MasWard>> getAllWard(JSONObject jsondata) {
		Map<String, List<MasWard>> map = new HashMap<String, List<MasWard>>();
		List<MasWard> wardList = new ArrayList<MasWard>();
		List totalMatches  =new ArrayList<>();
		int pageNo=1;
		int pageSize = 5;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasWard.class);					
			if(jsondata.get("PN").toString() !=null) 
			 pageNo = Integer.parseInt(jsondata.get("PN").toString());
					
			String wardName="";
			String searchCityName="";
				 if (jsondata.has("wardName"))
				 {
					 wardName = "%"+jsondata.get("wardName")+"%";
					 //wardName =  jsondata.get("wardName").toString(); 
					 if(jsondata.get("wardName").toString().length()>0 && !jsondata.get("wardName").toString().trim().equalsIgnoreCase("")) {
							criteria.add(Restrictions.ilike("wardName", wardName));
							
						}
				 }
				 
				 if (jsondata.has("searchCityName"))
				 {
					 searchCityName =  (String) jsondata.get("searchCityName");
				 	 if( !jsondata.get("searchCityName").toString().trim().equalsIgnoreCase("") && !jsondata.get("searchCityName").toString().trim().equalsIgnoreCase("0")) {
							criteria.add(Restrictions.eq("cityId", Long.parseLong(searchCityName)));
							
						}
				 }
				 criteria.addOrder(Order.asc("wardName"));
				 totalMatches = criteria.list();				 
				 criteria.setFirstResult((pageSize) * (pageNo - 1));
				 criteria.setMaxResults(pageSize);
				 wardList = criteria.list();
			
		 
		map.put("wardList", wardList);
		map.put("totalMatches", totalMatches);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
	
		return map;
	}
	
	@Override
	public String updateWardStatus(Long id, String status) {
		
		String result = "";
		try {

			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Object mmuObject = session.load(MasWard.class, id);
			MasWard ward = (MasWard) mmuObject;
			Transaction transaction = session.beginTransaction();
			if (ward.getStatus().equalsIgnoreCase("Y")) {
				ward.setStatus("n");

			} else if (ward.getStatus().equalsIgnoreCase("n")) {
				ward.setStatus("y");

			} else {
				ward.setStatus("y");
			}

			session.update(ward);
			transaction.commit();
			session.flush();
			result = "200";
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	@Override
	public String updateWard(MasWard masWard) {
		String result="";
		Session session = null;
		
		try {
			List<MasWard> validateWname= validateWardName(masWard.getWardName(),masWard.getMasCity().getCityId());
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Transaction transaction = session.beginTransaction();
					Object object = session.load(MasWard.class, masWard.getWardId());
					MasWard masWardObj = (MasWard)object;
					if(validateWname !=null && validateWname.size() >0)	{				
					if(validateWname.get(0).getWardName().equalsIgnoreCase(masWard.getWardName())) {
						masWardObj.setWardName(masWardObj.getWardName());	
						//masWardObj.setMasCity(masWardObj.getMasCity());
						masWardObj.setCityId(masWardObj.getMasCity().getCityId());	
					}					
					}
					else {
						masWardObj.setWardName(masWard.getWardName());	
						//masWardObj.setMasCity(masWard.getMasCity());
						masWardObj.setCityId(masWard.getMasCity().getCityId());	
					}
					if(masWard.getMasZone() !=null) {
					masWardObj.setMasZone(masWard.getMasZone());
					}
									
					masWardObj.setWardNo(masWard.getWardNo());
					masWardObj.setStatus(masWard.getStatus());
					masWardObj.setLastChangeBy(masWard.getLastChangeBy());
					masWardObj.setLastChangeDate(masWard.getLastChangeDate());					
					session.update(masWardObj);					
					session.flush();
					session.clear();
					transaction.commit();					
					result = "success";
			
											
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	@Override
	public List<MasDistrict> validateMasDistrict(String districtCode, String districtName) {
		List<MasDistrict> validDistrictList = new ArrayList<MasDistrict>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasDistrict.class);
			criteria.add(Restrictions.or(Restrictions.eq("districtCode", districtCode),
					Restrictions.eq("districtName", districtName)));
			validDistrictList = criteria.list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return validDistrictList;
	}
	
	@Override
	public String addDistrict(MasDistrict masDistrict) {
		String result="";	
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(masDistrict);
			tx.commit();
			if(savedObj!=null) {
				result="200";
			}else {
				result = "500";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		
		return result;
	}
	
	
	@Override
	public String updateDistrictStatus(Long id, String status) {
		
		String result = "";
		try {

			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Object mmuObject = session.load(MasDistrict.class, id);
			MasDistrict dist = (MasDistrict) mmuObject;
			Transaction transaction = session.beginTransaction();
			if (dist.getStatus().equalsIgnoreCase("Y")) {
				dist.setStatus("n");

			} else if (dist.getStatus().equalsIgnoreCase("n")) {
				dist.setStatus("y");

			} else {
				dist.setStatus("y");
			}

			session.update(dist);
			transaction.commit();
			session.flush();
			result = "200";
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	@Override
	public String updateDistrict(MasDistrict masDistrict) {
		String result="";
	
		
		try {
			List<MasDistrict> validate= validateMasDistrict(masDistrict.getDistrictCode(), masDistrict.getDistrictName());
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Transaction tx = session.beginTransaction();
					Object objectMasDist = session.load(MasDistrict.class, masDistrict.getDistrictId());
					MasDistrict masDistrictObj = (MasDistrict)objectMasDist;
					
					
					masDistrictObj.setDistrictCode(masDistrict.getDistrictCode());
					if(validate.get(0).getDistrictName().equalsIgnoreCase(masDistrict.getDistrictName())) {
						masDistrictObj.setDistrictName(masDistrictObj.getDistrictName());
						}				
					else {
						masDistrictObj.setDistrictName(masDistrict.getDistrictName());	
					}
					masDistrictObj.setMasState(masDistrict.getMasState());
					masDistrictObj.setStatus(masDistrict.getStatus());
					masDistrictObj.setLastChangeBy(masDistrict.getLastChangeBy());
					masDistrictObj.setLastChangeDate(masDistrict.getLastChangeDate());	
					masDistrictObj.setPopulation(masDistrict.getPopulation());
					masDistrictObj.setOrderNo(masDistrict.getOrderNo());
					masDistrictObj.setUpss(masDistrict.getUpss());
					if(masDistrict.getVendorId()!=null)
					{
					  masDistrictObj.setVendorId(masDistrict.getVendorId());
					}
					if(masDistrict.getStartDate()!=null)
					{	
				    //Date startDate=HMSUtil.dateFormatteryyyymmdd(masDistrict.getStartDate().toString());
					System.out.println("startDate "+masDistrict.getStartDate());
					masDistrictObj.setStartDate(masDistrict.getStartDate());
					
					}
					if(masDistrict.getLongitude()!=null)
					{	
					masDistrictObj.setLongitude(masDistrict.getLongitude());
					}
					if(masDistrict.getLattitude()!=null)
					{	
					masDistrictObj.setLattitude(masDistrict.getLattitude());
					}
					session.update(masDistrictObj);					
					tx.commit();
					session.flush();
					result = "success";
			
											
		}catch(Exception e) {
			e.printStackTrace();
			
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	@Override
	public List<MasTreatmentInstruction> validateMasTreatmentInstruction(String instructionsCode, String instructionsName) {
		List<MasTreatmentInstruction> mtiList = new ArrayList<MasTreatmentInstruction>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasTreatmentInstruction.class);
			criteria.add(Restrictions.or(Restrictions.eq("instructionsCode", instructionsCode),
					Restrictions.eq("instructionsName", instructionsName)));
			mtiList = criteria.list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mtiList;
	}
	
	@Override
	public String addTreatmentInstructions(MasTreatmentInstruction masTreatmentInstruction) {
		String result="";	
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(masTreatmentInstruction);
			tx.commit();
			if(savedObj!=null) {
				result="200";
			}else {
				result = "500";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		
		return result;
	}
	
	@Override
	public Map<String, List<MasTreatmentInstruction>> getAllTreatmentInstructions(JSONObject jsondata) {
		Map<String, List<MasTreatmentInstruction>> map = new HashMap<String, List<MasTreatmentInstruction>>();
		List<MasTreatmentInstruction> mtiList = new ArrayList<MasTreatmentInstruction>();
		List totalMatches  =new ArrayList<>();
		int pageNo=1;
		int pageSize = 5;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasTreatmentInstruction.class);					
			if(jsondata.get("PN").toString() !=null) 
			 pageNo = Integer.parseInt(jsondata.get("PN").toString());
					
			String instructionsName="";
				 if (jsondata.has("instructionsName"))
				 {
					 instructionsName = "%"+jsondata.get("instructionsName")+"%";
					  if(jsondata.get("instructionsName").toString().length()>0 && !jsondata.get("instructionsName").toString().trim().equalsIgnoreCase("")) {
							criteria.add(Restrictions.ilike("instructionsName", instructionsName));
							
						}
				 }
				 criteria.addOrder(Order.asc("instructionsName"));
				 totalMatches = criteria.list();				 
				 criteria.setFirstResult((pageSize) * (pageNo - 1));
				 criteria.setMaxResults(pageSize);
				 mtiList = criteria.list();
			
		 
		map.put("mtiList", mtiList);
		map.put("totalMatches", totalMatches);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
	
		return map;
	}
	
	@Override
	public String updateTreatmentInstructionsStatus(Long id, String status) {
		
		String result = "";
		try {

			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Object mmuObject = session.load(MasTreatmentInstruction.class, id);
			MasTreatmentInstruction inst = (MasTreatmentInstruction) mmuObject;
			Transaction transaction = session.beginTransaction();
			if (inst.getStatus().equalsIgnoreCase("Y")) {
				inst.setStatus("n");

			} else if (inst.getStatus().equalsIgnoreCase("n")) {
				inst.setStatus("y");

			} else {
				inst.setStatus("y");
			}

			session.update(inst);
			transaction.commit();
			session.flush();
			result = "200";
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	@Override
	public String updateTreatmentInstructions(MasTreatmentInstruction masTreatmentInstruction) {
		String result="";
		Session session = null;
		
		try {
			List<MasTreatmentInstruction> validate= validateMasTreatmentInstruction(masTreatmentInstruction.getInstructionsCode(), masTreatmentInstruction.getInstructionsName());
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Transaction transaction = session.beginTransaction();
					Object object = session.load(MasTreatmentInstruction.class, masTreatmentInstruction.getInstructionsId());
					MasTreatmentInstruction masTrtObj = (MasTreatmentInstruction)object;
					masTrtObj.setInstructionsCode(masTreatmentInstruction.getInstructionsCode());
					if(validate.get(0).getInstructionsName().equalsIgnoreCase(masTreatmentInstruction.getInstructionsName())) {
						masTrtObj.setInstructionsName(masTrtObj.getInstructionsName());
						}					
					else {
						masTrtObj.setInstructionsName(masTreatmentInstruction.getInstructionsName());
					}
					masTrtObj.setStatus(masTreatmentInstruction.getStatus());
					masTrtObj.setLastChgBy(masTreatmentInstruction.getLastChgBy());
					masTrtObj.setLastChgDate(masTreatmentInstruction.getLastChgDate());					
					session.update(masTrtObj);					
					session.flush();
					session.clear();
					transaction.commit();					
					result = "success";
			
											
		}catch(Exception e) {
			
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	
	@Override
	public List<MasSymptoms> validateMasSymptoms(String code, String name) {
		List<MasSymptoms> symptomsList = new ArrayList<MasSymptoms>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasSymptoms.class);
			criteria.add(Restrictions.or(Restrictions.eq("code", code),
					Restrictions.eq("name", name)));
			symptomsList = criteria.list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return symptomsList;
	}
	
	@Override
	public String addSignSymtoms(MasSymptoms masSymptoms) {
		String result="";	
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(masSymptoms);
			tx.commit();
			if(savedObj!=null) {
				result="200";
			}else {
				result = "500";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		
		return result;
	}
	
	@Override
	public Map<String, List<MasSymptoms>> getAllSignSymtoms(JSONObject jsondata) {
		Map<String, List<MasSymptoms>> map = new HashMap<String, List<MasSymptoms>>();
		List<MasSymptoms> symptomsList = new ArrayList<MasSymptoms>();
		List totalMatches  =new ArrayList<>();
		int pageNo=1;
		int pageSize = 5;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasSymptoms.class);					
			if(jsondata.get("PN").toString() !=null) 
			 pageNo = Integer.parseInt(jsondata.get("PN").toString());
					
			String symptomsName="";
				 if (jsondata.has("symptomsName"))
				 {
					 symptomsName = "%"+jsondata.get("symptomsName")+"%";
					  if(jsondata.get("symptomsName").toString().length()>0 && !jsondata.get("symptomsName").toString().trim().equalsIgnoreCase("")) {
							criteria.add(Restrictions.ilike("name", symptomsName));
							
						}
				 }
				 criteria.addOrder(Order.asc("name"));
				 totalMatches = criteria.list();				 
				 criteria.setFirstResult((pageSize) * (pageNo - 1));
				 criteria.setMaxResults(pageSize);
				 symptomsList = criteria.list();
			
		 
		map.put("symptomsList", symptomsList);
		map.put("totalMatches", totalMatches);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
	
		return map;
	}
	
	@Override
	public String updateSignSymtomsStatus(Long id, String status) {
		
		String result = "";
		try {

			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Object mmuObject = session.load(MasSymptoms.class, id);
			MasSymptoms symptoms = (MasSymptoms) mmuObject;
			Transaction transaction = session.beginTransaction();
			if (symptoms.getStatus().equalsIgnoreCase("Y")) {
				symptoms.setStatus("n");

			} else if (symptoms.getStatus().equalsIgnoreCase("n")) {
				symptoms.setStatus("y");

			} else {
				symptoms.setStatus("y");
			}

			session.update(symptoms);
			transaction.commit();
			session.flush();
			result = "200";
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	@Override
	public String updateSignSymtoms(MasSymptoms masSymptoms) {
		String result="";
		Session session = null;
		
		try {
			List<MasSymptoms> validate= validateMasSymptoms(masSymptoms.getCode(), masSymptoms.getName());
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Transaction transaction = session.beginTransaction();
					Object object = session.load(MasSymptoms.class, masSymptoms.getId());
					MasSymptoms symptomsObj = (MasSymptoms)object;
					symptomsObj.setCode(masSymptoms.getCode());
					if(validate.get(0).getName().equalsIgnoreCase(masSymptoms.getName())) {
						symptomsObj.setName(symptomsObj.getName());
						}					
					else {
						symptomsObj.setName(masSymptoms.getName());
					}
					symptomsObj.setMostFrequentSymptoms(masSymptoms.getMostFrequentSymptoms());
					symptomsObj.setStatus(masSymptoms.getStatus());
					symptomsObj.setLastChgBy(masSymptoms.getLastChgBy());
					symptomsObj.setLastChgDate(masSymptoms.getLastChgDate());					
					session.update(symptomsObj);					
					session.flush();
					session.clear();
					transaction.commit();					
					result = "success";
			
											
		}catch(Exception e) {
			
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	@Override
	public List<MasLabor> validateMasLabor(String laborCode, String laborName) {
		List<MasLabor> laborList = new ArrayList<MasLabor>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasLabor.class);
			criteria.add(Restrictions.or(Restrictions.eq("laborCode", laborCode),
					Restrictions.eq("laborName", laborName)));
			laborList = criteria.list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return laborList;
	}
	
	@Override
	public String addLabour(MasLabor masLabor) {
		String result="";	
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(masLabor);
			tx.commit();
			if(savedObj!=null) {
				result="200";
			}else {
				result = "500";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		
		return result;
	}
	
	@Override
	public Map<String, List<MasLabor>> getAllLabour(JSONObject jsondata) {
		Map<String, List<MasLabor>> map = new HashMap<String, List<MasLabor>>();
		List<MasLabor> laborList = new ArrayList<MasLabor>();
		List totalMatches  =new ArrayList<>();
		int pageNo=1;
		int pageSize = 5;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasLabor.class);					
			if(jsondata.get("PN").toString() !=null) 
			 pageNo = Integer.parseInt(jsondata.get("PN").toString());
					
			String laborName="";
				 if (jsondata.has("laborName"))
				 {
					 laborName = "%"+jsondata.get("laborName")+"%";
					  if(jsondata.get("laborName").toString().length()>0 && !jsondata.get("laborName").toString().trim().equalsIgnoreCase("")) {
							criteria.add(Restrictions.ilike("laborName", laborName));
							
						}
				 }
				 criteria.addOrder(Order.asc("laborName"));
				 totalMatches = criteria.list();				 
				 criteria.setFirstResult((pageSize) * (pageNo - 1));
				 criteria.setMaxResults(pageSize);
				 laborList = criteria.list();
			
		 
		map.put("laborList", laborList);
		map.put("totalMatches", totalMatches);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
	
		return map;
	}
	
	@Override
	public String updateLabourStatus(Long id, String status) {
		
		String result = "";
		try {

			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Object mmuObject = session.load(MasLabor.class, id);
			MasLabor labor = (MasLabor) mmuObject;
			Transaction transaction = session.beginTransaction();
			if (labor.getStatus().equalsIgnoreCase("Y")) {
				labor.setStatus("n");

			} else if (labor.getStatus().equalsIgnoreCase("n")) {
				labor.setStatus("y");

			} else {
				labor.setStatus("y");
			}

			session.update(labor);
			transaction.commit();
			session.flush();
			result = "200";
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	@Override
	public String updateLabour(MasLabor masLabor) {
		String result="";
		Session session = null;
		
		try {
			List<MasLabor> validate= validateMasLabor(masLabor.getLaborCode(), masLabor.getLaborName());
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Transaction transaction = session.beginTransaction();
					Object object = session.load(MasLabor.class, masLabor.getLaborId());
					MasLabor masLaborObj = (MasLabor)object;
					masLaborObj.setLaborCode(masLabor.getLaborCode());
					if(validate.get(0).getLaborName().equalsIgnoreCase(masLabor.getLaborName())) {
						masLaborObj.setLaborName(masLaborObj.getLaborName());
						}					
					else {
						masLaborObj.setLaborName(masLabor.getLaborName());
					}
					masLaborObj.setStatus(masLabor.getStatus());
					masLaborObj.setLastChgBy(masLabor.getLastChgBy());
					masLaborObj.setLastChgDate(masLabor.getLastChgDate());					
					session.update(masLaborObj);					
					session.flush();
					session.clear();
					transaction.commit();					
					result = "success";
			
											
		}catch(Exception e) {
			
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public Map<String, List<MasPenalty>> getAllPenalty(JSONObject jsondata) {
		Map<String, List<MasPenalty>> map = new HashMap<String, List<MasPenalty>>();
		List<MasPenalty> penaltyList = new ArrayList<MasPenalty>();
		List totalMatches  =new ArrayList<>();
		int pageNo=0;
		int pageSize = 5;

		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasPenalty.class);

			if(jsondata.has("withExpiredRecords") && !jsondata.getString("withExpiredRecords").trim().isEmpty()) {
				criteria.add(Restrictions.ge("endDate", new Date()));
			}
			if(jsondata.get("PN").toString() !=null)
				pageNo = Integer.parseInt(jsondata.get("PN").toString());

			if(jsondata.has("penaltyCode") && !jsondata.get("penaltyCode").toString().trim().isEmpty()) {
				criteria.add(Restrictions.ilike("penaltyCode", "%"+jsondata.get("penaltyCode")+"%"));
			}
			if(jsondata.has("status") && !jsondata.get("status").toString().trim().isEmpty()) {
				criteria.add(Restrictions.ilike("status", "%"+jsondata.get("status")+"%"));
			}
			criteria.addOrder(Order.asc("penaltyCode"));
			totalMatches = criteria.list();
			if (pageNo > 0) {
				criteria.setFirstResult((pageSize) * (pageNo - 1));
				criteria.setMaxResults(pageSize);
			}
			penaltyList = criteria.list();


			map.put("penaltyList", penaltyList);
			map.put("totalMatches", totalMatches);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return map;
	}

	@Override
	public String updatePenaltyStatus(Long id, String status){
		String result = "";
		try {

			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Object mmuObject = session.load(MasPenalty.class, id);
			MasPenalty penalty = (MasPenalty) mmuObject;
			Transaction transaction = session.beginTransaction();
			if (penalty.getStatus().equalsIgnoreCase("Y")) {
				penalty.setStatus("N");

			} else if (penalty.getStatus().equalsIgnoreCase("N")) {
				penalty.setStatus("Y");

			} else {
				penalty.setStatus("Y");
			}

			session.update(penalty);
			transaction.commit();
			session.flush();
			result = "200";

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public String updatePenalty(MasPenalty masPenalty){
		String result="";
		Session session = null;

		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Transaction transaction = session.beginTransaction();
			Object object = session.load(MasPenalty.class, masPenalty.getPenaltyId());
			MasPenalty masPenaltyObj = (MasPenalty) object;
			masPenaltyObj.setPenaltyDescription(masPenalty.getPenaltyDescription());
			masPenaltyObj.setPenaltyAmount(masPenalty.getPenaltyAmount());
			masPenaltyObj.setStartDate(masPenalty.getStartDate());
			masPenaltyObj.setEndDate(masPenalty.getEndDate());

			session.update(masPenaltyObj);
			session.flush();
			session.clear();
			transaction.commit();
			result = "success";


		}catch(Exception e) {

		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public Map<String, List<MasEquipmentChecklist>> getAllEquipmentChecklist(JSONObject jsondata) {
		Map<String, List<MasEquipmentChecklist>> map = new HashMap<String, List<MasEquipmentChecklist>>();
		List<MasEquipmentChecklist> equipmentChecklists = new ArrayList<MasEquipmentChecklist>();
		List totalMatches  =new ArrayList<>();
		int pageNo=0;
		int pageSize = 5;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasEquipmentChecklist.class);
			if(jsondata.get("PN").toString() !=null)
				pageNo = Integer.parseInt(jsondata.get("PN").toString());


			if(jsondata.has("instrumentName") && !jsondata.get("instrumentName").toString().trim().isEmpty()) {
				criteria.add(Restrictions.ilike("instrumentName", "%"+jsondata.get("instrumentName")+"%"));
			}
			if(jsondata.has("status") && !jsondata.get("status").toString().trim().isEmpty()) {
				criteria.add(Restrictions.ilike("status", "%"+jsondata.get("status")+"%"));
			}

			criteria.addOrder(Order.asc("instrumentName"));
			totalMatches = criteria.list();
			if (pageNo > 0) {
				criteria.setFirstResult((pageSize) * (pageNo - 1));
				criteria.setMaxResults(pageSize);
			}
			equipmentChecklists = criteria.list();


			map.put("equipmentChecklists", equipmentChecklists);
			map.put("totalMatches", totalMatches);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return map;
	}

	@Override
	public String updateEquipmentChecklistStatus(Long id, String status) {
		String result = "";
		try {

			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Object mmuObject = session.load(MasEquipmentChecklist.class, id);
			MasEquipmentChecklist masEquipmentChecklist = (MasEquipmentChecklist) mmuObject;
			Transaction transaction = session.beginTransaction();
			if (masEquipmentChecklist.getStatus().equalsIgnoreCase("Y")) {
				masEquipmentChecklist.setStatus("N");
			} else if (masEquipmentChecklist.getStatus().equalsIgnoreCase("N")) {
				masEquipmentChecklist.setStatus("Y");
			} else {
				masEquipmentChecklist.setStatus("Y");
			}

			session.update(masEquipmentChecklist);
			transaction.commit();
			session.flush();
			result = "200";

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public String updateEquipmentChecklist(MasEquipmentChecklist masEquipmentChecklist) {
		String result="";
		Session session = null;

		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Transaction transaction = session.beginTransaction();
			Object object = session.load(MasEquipmentChecklist.class, masEquipmentChecklist.getChecklistId());
			MasEquipmentChecklist masEquipmentChecklistObj = (MasEquipmentChecklist) object;
			masEquipmentChecklistObj.setQuantity(masEquipmentChecklist.getQuantity());
			masEquipmentChecklistObj.setSequenceNo(masEquipmentChecklist.getSequenceNo());
			masEquipmentChecklistObj.setInstrumentName(masEquipmentChecklist.getInstrumentName());
			masEquipmentChecklistObj.setPenaltyId(masEquipmentChecklist.getPenaltyId());

			session.update(masEquipmentChecklistObj);
			session.flush();
			session.clear();
			transaction.commit();
			result = "success";

		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public Map<String, List<MasInspectionChecklist>> getAllInspectionChecklist(JSONObject jsondata) {
		Map<String, List<MasInspectionChecklist>> map = new HashMap<String, List<MasInspectionChecklist>>();
		List<MasInspectionChecklist> inspectionChecklists = new ArrayList<MasInspectionChecklist>();
		List totalMatches  =new ArrayList<>();
		int pageNo=0;
		int pageSize = 5;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasInspectionChecklist.class);
			if(jsondata.get("PN").toString() !=null)
				pageNo = Integer.parseInt(jsondata.get("PN").toString());


			if(jsondata.has("checklistName") && !jsondata.get("checklistName").toString().trim().isEmpty()) {
				criteria.add(Restrictions.ilike("checklistName", "%"+jsondata.get("checklistName")+"%"));
			}

			if(jsondata.has("status") && !jsondata.get("status").toString().trim().isEmpty()) {
				criteria.add(Restrictions.ilike("status", "%"+jsondata.get("status")+"%"));
			}

			if(jsondata.has("sequenceNo") && jsondata.getInt("sequenceNo") > 0) {
				criteria.add(Restrictions.eq("sequenceNo", jsondata.getInt("sequenceNo")));
			}

			if(jsondata.has("subsequence") && jsondata.getInt("subsequence") > 0) {
				criteria.add(Restrictions.eq("subsequence", jsondata.getInt("subsequence")));
			}

			criteria.addOrder(Order.asc("sequenceNo"));
			totalMatches = criteria.list();

			if (pageNo > 0) {
				criteria.setFirstResult((pageSize) * (pageNo - 1));
				criteria.setMaxResults(pageSize);
			}
			inspectionChecklists = criteria.list();
			inspectionChecklists.sort(Comparator.comparing(e -> {
				int ss = 0;
				if(e.getSubsequence() != null)
					ss = e.getSubsequence();

				String sq = e.getSequenceNo()+""+ss;
				return Integer.parseInt(sq);
			}));

			map.put("inspectionChecklists", inspectionChecklists);
			map.put("totalMatches", totalMatches);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return map;
	}

	@Override
	public String updateInspectionChecklistStatus(Long id, String status) {
		String result = "";
		try {

			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Object mmuObject = session.load(MasInspectionChecklist.class, id);
			MasInspectionChecklist masInspectionChecklist = (MasInspectionChecklist) mmuObject;
			Transaction transaction = session.beginTransaction();
			if (masInspectionChecklist.getStatus().equalsIgnoreCase("Y")) {
				masInspectionChecklist.setStatus("N");

			} else if (masInspectionChecklist.getStatus().equalsIgnoreCase("N")) {
				masInspectionChecklist.setStatus("Y");
			} else {
				masInspectionChecklist.setStatus("Y");
			}

			session.update(masInspectionChecklist);
			transaction.commit();
			session.flush();
			result = "200";

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public String updateInspectionChecklist(MasInspectionChecklist masInspectionChecklist) {
		String result="";
		Session session = null;

		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Transaction transaction = session.beginTransaction();
			Object object = session.load(MasInspectionChecklist.class, masInspectionChecklist.getChecklistId());
			MasInspectionChecklist masInspectionChecklistObj = (MasInspectionChecklist) object;
			masInspectionChecklistObj.setSequenceNo(masInspectionChecklist.getSequenceNo());
			masInspectionChecklistObj.setTypeOfInput(masInspectionChecklist.getTypeOfInput());
			masInspectionChecklistObj.setSubsequence(masInspectionChecklist.getSubsequence());
			masInspectionChecklistObj.setPenaltyId(masInspectionChecklist.getPenaltyId());
			masInspectionChecklistObj.setChecklistName(masInspectionChecklist.getChecklistName());

			session.update(masInspectionChecklistObj);
			session.flush();
			session.clear();
			transaction.commit();
			result = "success";

		}catch(Exception e) {

		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public boolean isRecordAlreadyExists(String keyColumn, String columnValue, Class entityClass){
		boolean isAlreadyExist = false;

		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(entityClass);
			criteria.add(Restrictions.or(Restrictions.eq(keyColumn, columnValue)));
			isAlreadyExist = criteria.list().size() > 0;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return isAlreadyExist;
	}

	@Override
	public String createRecord(Serializable entity) {
		String result="";
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(entity);
			tx.commit();
			if(savedObj!=null) {
				result="200";
			}else {
				result = "500";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return result;
	}

	public Object read(Class entityClass, Serializable id) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Object entity = session.load(entityClass, id);
		session.flush();
		session.close();
		return entity;
	}

	@Override
	public Map<String, Object> getMMUHierarchicalList(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> map = new HashMap<>();
		
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();	
		JSONObject jsonObject = new JSONObject();
		session.doWork(new Work() {
			@Override
			public void execute(java.sql.Connection connection) throws SQLException {
				int userId =Integer.parseInt(jsondata.get("userId").toString());
				String levelOfUser = jsondata.get("levelOfUser").toString();
				JSONArray jsonArray1 = new JSONArray();				
			
				String queryString = "SELECT asp_hierarchical_mmu(?,?)";				
				PreparedStatement    stmt = connection.prepareCall(queryString);
				
				stmt.setString(1, levelOfUser);			
				stmt.setInt(2, userId);
				connection.setAutoCommit(false);
				ResultSet rs = stmt.executeQuery();
				
				if (rs.next()) {
					Object o = rs.getObject(1);
					if (o instanceof ResultSet) {
						ResultSet rs1 = (ResultSet) o;
						while (rs1.next()) {
							int columnCount = rs1.getMetaData().getColumnCount();
							JSONObject jsonObj = new JSONObject();
							for (int i = 0; i < columnCount; i++) {
								jsonObj.put(rs1.getMetaData().getColumnLabel(i + 1), HMSUtil.convertNullToEmptyString(rs1.getObject(i + 1)));
							}
							jsonArray1.put(jsonObj);
						}
						jsonObject.put("mmuList", jsonArray1);
					}
				}
				
			
		
			}
		});
						
		map.put("mmuListdata", jsonObject);
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return map;
	}

	@Override
	public List<MasSymptoms> getFrequentlyUsedSymptomsList(JSONObject jsonObject) {
		List<MasSymptoms> list = null;
		try {
			
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasSymptoms.class)
					.add(Restrictions.eq("status", "Y").ignoreCase())
					.add(Restrictions.eq("mostFrequentSymptoms", "Y").ignoreCase());
			
			criteria =criteria.addOrder(Order.asc("name"));
			
			/*
			 * if(jsonObject.get("name") != null &&
			 * !String.valueOf(jsonObject.get("name")).equals("") &&
			 * !String.valueOf(jsonObject.get("name")).equals("null")) { String name =
			 * "%"+String.valueOf(jsonObject.get("name"))+"%"; criteria =
			 * criteria.add(Restrictions.like("name", name).ignoreCase()); }
			 */
			//criteria = criteria.setMaxResults(10);
			list = criteria.list();
			return list;
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return null;
	}

	
	@Override
	public String addDeptMapping(MMUDepartment mmuDepartment) {
		String result="";	
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(mmuDepartment);
			tx.commit();
			if(savedObj!=null) {
				result="200";
			}else {
				result = "500";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		
		return result;
	}
	
	@Override
	public Map<String, List<MMUDepartment>> getAllDeptMapping(JSONObject jsondata) {
		Map<String, List<MMUDepartment>> map = new HashMap<String, List<MMUDepartment>>();
		List<MMUDepartment> mmuDeptList = new ArrayList<MMUDepartment>();
		List totalMatches  =new ArrayList<>();
		int pageNo=1;
		int pageSize = 5;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MMUDepartment.class).createAlias("masMMU", "mmu");					
			if(jsondata.get("PN").toString() !=null) 
			 pageNo = Integer.parseInt(jsondata.get("PN").toString());
					
			String mmuName="";
				 if (jsondata.has("mmuName"))
				 {
					 mmuName = "%"+jsondata.get("mmuName")+"%";
					  if(jsondata.get("mmuName").toString().length()>0 && !jsondata.get("mmuName").toString().trim().equalsIgnoreCase("")) {
							criteria.add(Restrictions.ilike("mmu.mmuName", mmuName));
							
						}
				 }
				 criteria.addOrder(Order.asc("mmu.mmuName"));
				 totalMatches = criteria.list();				 
				 criteria.setFirstResult((pageSize) * (pageNo - 1));
				 criteria.setMaxResults(pageSize);
				 mmuDeptList = criteria.list();
			
		 
		map.put("mmuDeptList", mmuDeptList);
		map.put("totalMatches", totalMatches);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
	
		return map;
	}
		
	@Override
	public String updateDeptMappingStatus(Long id, String status) {
		
		String result = "";
		try {

			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Object mmuObject = session.load(MMUDepartment.class, id);
			MMUDepartment mmudept = (MMUDepartment) mmuObject;
			Transaction transaction = session.beginTransaction();
			if (mmudept.getStatus().equalsIgnoreCase("Y")) {
				mmudept.setStatus("n");

			} else if (mmudept.getStatus().equalsIgnoreCase("n")) {
				mmudept.setStatus("y");

			} else {
				mmudept.setStatus("y");
			}

			session.update(mmudept);
			transaction.commit();
			session.flush();
			result = "200";
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
		
	@Override
	public List<MMUDepartment> validateMMUDepartment(Long mmuId, Long deptId) {
		List<MMUDepartment> mmuDepartmentList = new ArrayList<MMUDepartment>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MMUDepartment.class);
			criteria.add(Restrictions.and(Restrictions.eq("mmuId", mmuId),
					Restrictions.eq("departmentId", deptId)));
			mmuDepartmentList = criteria.list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mmuDepartmentList;
	}
	
	@Override
	public String updateDeptMapping(MMUDepartment mmuDepartment) {
		String result="";
		Session session = null;
		
		try {
			Long mmuId=mmuDepartment.getMmuId();
           
			
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Transaction transaction = session.beginTransaction();
					Object object = session.load(MMUDepartment.class, mmuDepartment.getMmuDepartmentId());
					MMUDepartment mmuDepartmentrObj = (MMUDepartment)object;
					
					mmuDepartmentrObj.setMmuId(mmuDepartment.getMmuId());
					mmuDepartmentrObj.setDepartmentId(mmuDepartment.getDepartmentId());
										
					mmuDepartmentrObj.setStatus(mmuDepartment.getStatus());
					mmuDepartmentrObj.setLastChangeBy(mmuDepartment.getLastChangeBy());
					mmuDepartmentrObj.setLastChangeDate(mmuDepartment.getLastChangeDate());					
					session.update(mmuDepartmentrObj);					
					session.flush();
					session.clear();
					transaction.commit();					
					result = "success";
			
											
		}catch(Exception e) {
			
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	/****************************Supplier Type Master*********************************************************/
	@Override
	public Map<String, List<MasStoreSupplierType>> getAllSupplierType(JSONObject jsondata) {
		Map<String, List<MasStoreSupplierType>> map = new HashMap<String, List<MasStoreSupplierType>>();
		List<MasStoreSupplierType> supplierTypeList = new ArrayList<MasStoreSupplierType>();
		int pageNo=1;
		int pageSize = 5;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasStoreSupplierType.class);
		
			if( jsondata.get("PN")!=null)
			 pageNo = Integer.parseInt(jsondata.get("PN").toString());
					
			String vtName="";
				 if (jsondata.has("supplierTypeName"))
				 {
					 vtName = "%"+jsondata.get("supplierTypeName")+"%";
					  if(jsondata.get("supplierTypeName").toString().length()>0 && !jsondata.get("supplierTypeName").toString().trim().equalsIgnoreCase("")) {
							criteria.add(Restrictions.ilike("supplierTypeName", vtName));
						}
				 }
				 criteria.addOrder(Order.asc("supplierTypeName"));
				 List totalMatches = criteria.list();
				 
				 if( !jsondata.get("PN").equals("0"))
					{
					 criteria.setFirstResult((pageSize) * (pageNo - 1));
					 criteria.setMaxResults(pageSize);
					 supplierTypeList = criteria.list();
					}
				 if( jsondata.get("PN").equals("0")) {
					criteria.add(Restrictions.eq("status", "y").ignoreCase());
					criteria.setMaxResults(pageSize);
					supplierTypeList = criteria.list();
					}
			
		map.put("supplierTypeList", supplierTypeList);
		map.put("totalMatches", totalMatches);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@Override
	public List<MasStoreSupplierType> validateSupplierType(String supplierTypeCode, String supplierTypeName) {
		List<MasStoreSupplierType> supplierTypeList =  new ArrayList<MasStoreSupplierType>();	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria = session.createCriteria(MasMmuType.class);
				criteria.add(Restrictions.or(Restrictions.eq("mmuTypeCode", supplierTypeCode), 
						Restrictions.eq("mmuTypeName", supplierTypeName)));
				supplierTypeList = criteria.list();
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		return supplierTypeList;
	}

	@Override
	public List<MasStoreSupplierType> validateSupplierTypeUpdate(String supplierTypeCode, String supplierTypeName) {
		List<MasStoreSupplierType> supplierTypeList =  new ArrayList<MasStoreSupplierType>();	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria = session.createCriteria(MasStoreSupplierType.class);
				criteria.add(Restrictions.or(Restrictions.eq("supplierTypeCode", supplierTypeCode), Restrictions.eq("supplierTypeName", supplierTypeName)));
				supplierTypeList = criteria.list();
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		return supplierTypeList;
	}

	@Override
	public String addSupplierType(MasStoreSupplierType masStoreSupplierType) {
		String result="";	
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(masStoreSupplierType);
			tx.commit();
			if(savedObj!=null) {
				result="200";
			}else {
				result = "500";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		
		return result;
	}

	@Override
	public String updateSupplierTypeDetails(Long supplierTypeId, String supplierTypeCode, String supplierTypeName, Long userId) {
		String result="";
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
					Object object = session.load(MasStoreSupplierType.class, supplierTypeId);
					MasStoreSupplierType masStoreSupplierType = (MasStoreSupplierType)object;
					Users user=new Users();
					user.setUserId(userId);
					Transaction transaction = session.beginTransaction();
					masStoreSupplierType.setSupplierTypeCode(supplierTypeCode.toUpperCase());
					masStoreSupplierType.setSupplierTypeName(supplierTypeName.toUpperCase());					
					masStoreSupplierType.setLastChgBy(user);				
					long d = System.currentTimeMillis();
					Timestamp date = new Timestamp(d);
					masStoreSupplierType.setLastChgDate(date);
					session.update(masStoreSupplierType);
					transaction.commit();
					
					result = "200";
				//}
			//}
											
		}catch(Exception e) {
			
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public MasStoreSupplierType checkSupplierType(String supplierTypeCode) {
		MasStoreSupplierType mStoreSupplierType = new MasStoreSupplierType();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasStoreSupplierType.class);		
			criteria.add(Restrictions.eq("supplierTypeCode", supplierTypeCode));
			mStoreSupplierType = (MasStoreSupplierType)criteria.uniqueResult();
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mStoreSupplierType;
	}

	@Override
	public String updateSupplierTypeStatus(Long supplierTypeId, String supplierTypeCode, String status, Long userId) {
		String result = "";	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Object object =  session.load(MasStoreSupplierType.class, supplierTypeId);
				
				MasStoreSupplierType masStoreSupplierType = (MasStoreSupplierType)object;
				Transaction transaction = session.beginTransaction();
				
				
				if(masStoreSupplierType.getStatus().equalsIgnoreCase("Y") || masStoreSupplierType.getStatus().equalsIgnoreCase("y")) {
					masStoreSupplierType.setStatus("N");
				}else if(masStoreSupplierType.getStatus().equalsIgnoreCase("N") || masStoreSupplierType.getStatus().equalsIgnoreCase("n")) {
					masStoreSupplierType.setStatus("Y");
				}else {
					masStoreSupplierType.setStatus("Y");
				}
				session.update(masStoreSupplierType);
				transaction.commit();
				result = "200";
			//}
			
		}catch(Exception e) {
			
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	
	@Override
	public Map<String, List<Users>> getAllAuditorName(JSONObject jsondata) {
		Map<String, List<Users>> map = new HashMap<String, List<Users>>();
		List<Users> auditorNameList = new ArrayList<Users>();
		List totalMatches  =new ArrayList<>();
		Long userFlag=2l;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(Users.class).createAlias("masUserType", "masut");
			criteria.add(Restrictions.eq("masut.userTypeName", "Auditor").ignoreCase());
			criteria.add(Restrictions.ne("userFlag", userFlag));
		 	 criteria.addOrder(Order.asc("userName"));
			 totalMatches = criteria.list();				 
			 auditorNameList = criteria.list();
			 map.put("auditorNameList", auditorNameList);
			 map.put("totalMatches", totalMatches);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}
	
	
	/****************************Treatment Advice Master*********************************************************/
	@Override
	public Map<String, List<OpdTemplateMedicalAdvice>> getAllTreatmentAdvice(JSONObject jsondata) {
		Map<String, List<OpdTemplateMedicalAdvice>> map = new HashMap<String, List<OpdTemplateMedicalAdvice>>();
		List<OpdTemplateMedicalAdvice> treatmentAdviceList = new ArrayList<OpdTemplateMedicalAdvice>();
		int pageNo=1;
		int pageSize = 5;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(OpdTemplateMedicalAdvice.class);
			
					
			if( jsondata.get("PN")!=null)
			 pageNo = Integer.parseInt(jsondata.get("PN").toString());
					
			String adviceName="";
				 if (jsondata.has("adviceName"))
				 {
					 adviceName = "%"+jsondata.get("adviceName")+"%";
					  if(jsondata.get("adviceName").toString().length()>0 && !jsondata.get("adviceName").toString().trim().equalsIgnoreCase("")) {
							criteria.add(Restrictions.ilike("medicalAdvice", adviceName));
							
						}
				 }
				criteria.addOrder(Order.asc("medicalAdvice"));
				 List totalMatches = criteria.list();
				 
				 criteria.setFirstResult((pageSize) * (pageNo - 1));
				 criteria.setMaxResults(pageSize);
				 treatmentAdviceList = criteria.list();
			
			
		map.put("treatmentAdviceList", treatmentAdviceList);
		map.put("totalMatches", totalMatches);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@Override
	public List<OpdTemplateMedicalAdvice> validateAdviceName(String adviceName) {
		List<OpdTemplateMedicalAdvice> vanList =  new ArrayList<OpdTemplateMedicalAdvice>();	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria = session.createCriteria(OpdTemplateMedicalAdvice.class);
				criteria.add(Restrictions.eq("medicalAdvice", adviceName));
				vanList = criteria.list();
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		return vanList;
	}

	
	@Override
	public String addTreatmentAdvice(OpdTemplateMedicalAdvice madvice) {
		String result="";
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(madvice);
			tx.commit();
			
			if(savedObj!=null) {
				result="200";
			}else {
				result = "500";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		
		return result;
	}

	@Override
	public String updateTreatmentAdvice(Long Id, String adviceName, Long userId) {
		String result="";
		
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
					
			Object object = session.load(OpdTemplateMedicalAdvice.class, Id);
			OpdTemplateMedicalAdvice advice = (OpdTemplateMedicalAdvice)object;
					
					Transaction transaction = session.beginTransaction();
					advice.setMedicalAdvice(adviceName);		
					long d = System.currentTimeMillis();
					Timestamp date = new Timestamp(d);
					advice.setLastChgDate(date);
					advice.setLastChgBy(userId);
					session.update(advice);
					transaction.commit();					
					result = "200";
			
											
		}catch(Exception e) {
			
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	@Override
	public String updateTreatmentAdviceStatus(Long adviceId) {
		String result = "";	
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				
				Object object =  session.load(OpdTemplateMedicalAdvice.class, adviceId);
				
				OpdTemplateMedicalAdvice advice = (OpdTemplateMedicalAdvice)object;
				Transaction transaction = session.beginTransaction();
			
				if(advice.getStatus().equalsIgnoreCase("Y")) {
					advice.setStatus("n");
				}else if(advice.getStatus().equalsIgnoreCase("N")) {
					advice.setStatus("y");
				}else {
					advice.setStatus("y");
				}
				session.update(advice);
				transaction.commit();
				session.flush();
				result = "200";
						
		}catch(Exception e) {
			
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	/**************************************
	 * Manufacturer Master
	 **************************************************/
	
	@Override
	public String addManufacturer(MasManufacturer masManufacturer) {
		String result="";	
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(masManufacturer);
			tx.commit();
			if(savedObj!=null) {
				result="200";
			}else {
				result = "500";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		
		return result;
	}
	
	@Override
	public Map<String, List<MasManufacturer>> getAllManufacturer(JSONObject jsondata) {
		Map<String, List<MasManufacturer>> map = new HashMap<String, List<MasManufacturer>>();
		List<MasManufacturer> manufacturerList = new ArrayList<MasManufacturer>();
		List totalMatches  =new ArrayList<>();
		int pageNo=1;
		int pageSize = 5;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasManufacturer.class);					
			if(jsondata.get("PN").toString() !=null) 
			 pageNo = Integer.parseInt(jsondata.get("PN").toString());
					
			String manufacturerName="";
				 if (jsondata.has("manufacturerName"))
				 {
					 manufacturerName = "%"+jsondata.get("manufacturerName")+"%";
					  if(jsondata.get("manufacturerName").toString().length()>0 && !jsondata.get("manufacturerName").toString().trim().equalsIgnoreCase("")) {
							criteria.add(Restrictions.ilike("manufacturerName", manufacturerName));
							
						}
				 }
				 criteria.addOrder(Order.asc("manufacturerName"));
				 totalMatches = criteria.list();				 
				 criteria.setFirstResult((pageSize) * (pageNo - 1));
				 criteria.setMaxResults(pageSize);
				 manufacturerList = criteria.list();
			
		 
		map.put("manufacturerList", manufacturerList);
		map.put("totalMatches", totalMatches);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
	
		return map;
	}
		
	@Override
	public String updateManufacturerStatus(Long id, String status) {
		
		String result = "";
		try {

			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Object mmuObject = session.load(MasManufacturer.class, id);
			MasManufacturer man = (MasManufacturer) mmuObject;
			Transaction transaction = session.beginTransaction();
			if (man.getStatus().equalsIgnoreCase("Y")) {
				man.setStatus("n");

			} else if (man.getStatus().equalsIgnoreCase("n")) {
				man.setStatus("y");

			} else {
				man.setStatus("y");
			}

			session.update(man);
			transaction.commit();
			session.flush();
			result = "200";
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
		
	@Override
	public List<MasManufacturer> validateMasManufacturer(String manufacturerName, Long suplyId) {
		List<MasManufacturer> validList = new ArrayList<MasManufacturer>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasManufacturer.class).createAlias("masStoreSupplierType", "st");
			criteria.add(Restrictions.and(Restrictions.eq("manufacturerName", manufacturerName).ignoreCase(),
					Restrictions.eq("st.supplierTypeId", suplyId)));
			validList = criteria.list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return validList;
	}
	
	@Override
	public String updateManufacturer(MasManufacturer masManufacturer) {
		String result="";
		Session session = null;
		
		try {
			Long manufacturerId=masManufacturer.getManufacturerId();
			List<MasManufacturer> validate = validateMasManufacturer(masManufacturer.getManufacturerName(),masManufacturer.getSupplierTypeId());
			
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Transaction transaction = session.beginTransaction();
					Object object = session.load(MasManufacturer.class, masManufacturer.getManufacturerId());
					MasManufacturer masManufacturerObj = (MasManufacturer)object;
					if(validate !=null && validate.size()>0) {
					masManufacturerObj.setManufacturerName(masManufacturerObj.getManufacturerName());
					masManufacturerObj.setSupplierTypeId(masManufacturerObj.getSupplierTypeId());					
					}
					else {
						masManufacturerObj.setManufacturerName(masManufacturer.getManufacturerName());
						masManufacturerObj.setSupplierTypeId(masManufacturer.getSupplierTypeId());	
					}
					masManufacturerObj.setStatus(masManufacturerObj.getStatus());
					masManufacturerObj.setLastChangeBy(masManufacturer.getLastChangeBy());
					masManufacturerObj.setLastChgDate(masManufacturer.getLastChgDate());			
					
					session.update(masManufacturerObj);					
					session.flush();
					session.clear();
					transaction.commit();					
					result = "success";
			
											
		}catch(Exception e) {

		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	
	@Override
	public List<LagecyData> getLegaCityMasterData(Integer cityId) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria cr = session.createCriteria(LagecyData.class);
		cr.add(Restrictions.eq("cityId", cityId));
		List<LagecyData> list = cr.list();
		//System.out.println(list.size());
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return list;
	}

	@Override
	public String saveOrUpdateLgacyData(HashMap<String, Object> jsondata) {
		Date currentDate = ProjectUtils.getCurrentDate();
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria cr = session.createCriteria(LagecyData.class);
		Transaction tx = session.beginTransaction();
		Integer cityId = Integer.parseInt((String) jsondata.get("cityId"));
		Long patientId;
		Long hospitalId;
		Long userId;
		Long legacyId;
		Long itemTypeNivId=null;
		Long headerNivId = null;
		Calendar calendar = Calendar.getInstance();
		java.sql.Timestamp ourJavaTimestampObject = new java.sql.Timestamp(calendar.getTime().getTime());
		String procedureType = null;
		cr.add(Restrictions.eq("cityId", cityId));
		LagecyData legacylist = (LagecyData) cr.uniqueResult();
		try {

			if (legacylist != null) {
				if(!jsondata.get("noOfCamp").equals(""))
				{	
				legacylist.setDaiCampCount(Integer.parseInt(String.valueOf(jsondata.get("noOfCamp"))));
				}
				if(!jsondata.get("totalPatient").equals(""))
				{
				legacylist.setDaiTotPatient(Integer.parseInt(String.valueOf(jsondata.get("totalPatient"))));
				}
				if(!jsondata.get("averagePatientCount").equals(""))
				{
				legacylist.setDaiAvgPatient(Integer.parseInt(String.valueOf(jsondata.get("averagePatientCount"))));
				}
				if(!jsondata.get("countOfPatientLabTest").equals(""))
				{
				legacylist.setDaiLabPatient(Integer.parseInt(String.valueOf(jsondata.get("countOfPatientLabTest"))));
				}
				if(!jsondata.get("countOPatientMdicineGiven").equals(""))
				{
				legacylist.setDaiMedPatient(Integer.parseInt(String.valueOf(jsondata.get("countOPatientMdicineGiven"))));
				}
				if(!jsondata.get("countOPatientLabourDepartment").equals(""))
				{
					legacylist.setDaiDepRegPatient(Integer.parseInt(String.valueOf(jsondata.get("countOPatientLabourDepartment"))));
				}
				if(!jsondata.get("countOPatientLabourRegistration").equals(""))
				{
					legacylist.setDaiLabRegPatient(Integer.parseInt(String.valueOf(jsondata.get("countOPatientLabourRegistration"))));
				}
				//legacylist.setDaiLabRegPatient(Integer.parseInt(String.valueOf(jsondata.get("countOPatientLabourRegistration"))));
				if(!jsondata.get("countOfMMUCamp").equals(""))
				{
				legacylist.setMmssyCampCount(Integer.parseInt(String.valueOf(jsondata.get("countOfMMUCamp"))));
				}
				if(!jsondata.get("totalNoOfPatient").equals(""))
				{
				legacylist.setMmssyTotPatient(Integer.parseInt(String.valueOf(jsondata.get("totalNoOfPatient"))));
				}
				if(!jsondata.get("averagePatientMMU").equals(""))
				{
				legacylist.setMmssyAvgPatient(Integer.parseInt(String.valueOf(jsondata.get("averagePatientMMU"))));
				}
				if(!jsondata.get("noOfPatientLabTest").equals(""))
				{
				legacylist.setMmssyLabPatient(Integer.parseInt(String.valueOf(jsondata.get("noOfPatientLabTest"))));
				}
				if(!jsondata.get("noOfPatientMedicineDispensed").equals(""))
				{
				legacylist.setMmssyMedPatient(Integer.parseInt(String.valueOf(jsondata.get("noOfPatientMedicineDispensed"))));
				}
				if(!jsondata.get("noOfPatientLabourDepartment").equals(""))
				{
				legacylist.setMmssyDepRegPatient(Integer.parseInt(String.valueOf(jsondata.get("noOfPatientLabourDepartment"))));
				}
				if(!jsondata.get("noOfPatientLabourRegistration").equals(""))
				{
				legacylist.setMmssyLabRegPatient(Integer.parseInt(String.valueOf(jsondata.get("noOfPatientLabourRegistration"))));
				}
				if(!jsondata.get("noOfLabourMale").equals(""))
				{
				legacylist.setLabourMale(Integer.parseInt(String.valueOf(jsondata.get("noOfLabourMale"))));
				}
				if(!jsondata.get("noOfLabourFemale").equals(""))
				{
				legacylist.setLabourFemale(Integer.parseInt(String.valueOf(jsondata.get("noOfLabourFemale"))));
				}
				if(!jsondata.get("noOfLabourChild").equals(""))
				{
				legacylist.setLabourChild(Integer.parseInt(String.valueOf(jsondata.get("noOfLabourChild"))));
				}
				if(!jsondata.get("noOfLabourTransgender").equals(""))
				{
				legacylist.setLabourO(Integer.parseInt(String.valueOf(jsondata.get("noOfLabourTransgender"))));
				}
				/*if(!jsondata.get("totalBeneficiary").equals(""))
				{
				legacylist.setLabourTot(Integer.parseInt(String.valueOf(jsondata.get("totalBeneficiary"))));
				}*/
				if(!jsondata.get("noOfPatientAppliedLabourBoc").equals(""))
				{
				legacylist.setLabourRegBoc(Integer.parseInt(String.valueOf(jsondata.get("noOfPatientAppliedLabourBoc"))));
				}
				if(!jsondata.get("noOfPatientAppliedLabour").equals(""))
				{
				legacylist.setLabourRegOther(Integer.parseInt(String.valueOf(jsondata.get("noOfPatientAppliedLabour"))));
				}
				if(!jsondata.get("noOfPatientAppliedBoc").equals(""))
				{
				legacylist.setLabourAppBoc(Integer.parseInt(String.valueOf(jsondata.get("noOfPatientAppliedBoc"))));
				}
				if(!jsondata.get("noOfPatientAppliedOthers").equals(""))
				{
				legacylist.setLabourOthBoc(Integer.parseInt(String.valueOf(jsondata.get("noOfPatientAppliedOthers"))));
				}
				if(!jsondata.get("noOfUnrgistredWorkersTeated").equals(""))
				{
				legacylist.setLabourUnregBoc(Integer.parseInt(String.valueOf(jsondata.get("noOfUnrgistredWorkersTeated"))));
				}
				if(!jsondata.get("noOfUnrgistredWorkers").equals(""))
				{
				legacylist.setLabourUnregOther(Integer.parseInt(String.valueOf(jsondata.get("noOfUnrgistredWorkers"))));
				}
				if(!jsondata.get("noOfNonLabourGeneral").equals(""))
				{
				legacylist.setLabourRegGc(Integer.parseInt(String.valueOf(jsondata.get("noOfNonLabourGeneral"))));
				}
				
				
				session.update(legacylist);
				legacyId = legacylist.getLagecyDataId();
				
				
			   }

			else {

				LagecyData lgacydetails = new LagecyData();
				lgacydetails.setCityId(Integer.parseInt(String.valueOf(jsondata.get("cityId"))));
				if(!jsondata.get("noOfCamp").equals(""))
				{	
				lgacydetails.setDaiCampCount(Integer.parseInt(String.valueOf(jsondata.get("noOfCamp"))));
				}
				if(!jsondata.get("totalPatient").equals(""))
				{
				lgacydetails.setDaiTotPatient(Integer.parseInt(String.valueOf(jsondata.get("totalPatient"))));
				}
				if(!jsondata.get("averagePatientCount").equals(""))
				{
				lgacydetails.setDaiAvgPatient(Integer.parseInt(String.valueOf(jsondata.get("averagePatientCount"))));
				}
				if(!jsondata.get("countOfPatientLabTest").equals(""))
				{
				lgacydetails.setDaiLabPatient(Integer.parseInt(String.valueOf(jsondata.get("countOfPatientLabTest"))));
				}
				if(!jsondata.get("countOPatientMdicineGiven").equals(""))
				{
				lgacydetails.setDaiMedPatient(Integer.parseInt(String.valueOf(jsondata.get("countOPatientMdicineGiven"))));
				}
				if(!jsondata.get("countOPatientLabourDepartment").equals(""))
				{
				lgacydetails.setDaiDepRegPatient(Integer.parseInt(String.valueOf(jsondata.get("countOPatientLabourDepartment"))));
				}
				if(!jsondata.get("countOPatientLabourRegistration").equals(""))
				{
				lgacydetails.setDaiLabRegPatient(Integer.parseInt(String.valueOf(jsondata.get("countOPatientLabourRegistration"))));
				}
				//lgacydetails.setDaiLabRegPatient(Integer.parseInt(String.valueOf(jsondata.get("countOPatientLabourRegistration"))));
				
				
				if(!jsondata.get("countOfMMUCamp").equals(""))
				{
				lgacydetails.setMmssyCampCount(Integer.parseInt(String.valueOf(jsondata.get("countOfMMUCamp"))));
				}
				if(!jsondata.get("totalNoOfPatient").equals(""))
				{
				lgacydetails.setMmssyTotPatient(Integer.parseInt(String.valueOf(jsondata.get("totalNoOfPatient"))));
				}
				if(!jsondata.get("averagePatientMMU").equals(""))
				{
				lgacydetails.setMmssyAvgPatient(Integer.parseInt(String.valueOf(jsondata.get("averagePatientMMU"))));
				}
				if(!jsondata.get("noOfPatientLabTest").equals(""))
				{
					lgacydetails.setMmssyLabPatient(Integer.parseInt(String.valueOf(jsondata.get("noOfPatientLabTest"))));
				}
				if(!jsondata.get("noOfPatientMedicineDispensed").equals(""))
				{
					lgacydetails.setMmssyMedPatient(Integer.parseInt(String.valueOf(jsondata.get("noOfPatientMedicineDispensed"))));
				}
				if(!jsondata.get("noOfPatientLabourDepartment").equals(""))
				{
					lgacydetails.setMmssyDepRegPatient(Integer.parseInt(String.valueOf(jsondata.get("noOfPatientLabourDepartment"))));
				}
				if(!jsondata.get("noOfPatientLabourRegistration").equals(""))
				{
					lgacydetails.setMmssyLabRegPatient(Integer.parseInt(String.valueOf(jsondata.get("noOfPatientLabourRegistration"))));
				}
				if(!jsondata.get("noOfLabourMale").equals(""))
				{
				lgacydetails.setLabourMale(Integer.parseInt(String.valueOf(jsondata.get("noOfLabourMale"))));
				}
				if(!jsondata.get("noOfLabourFemale").equals(""))
				{
				lgacydetails.setLabourFemale(Integer.parseInt(String.valueOf(jsondata.get("noOfLabourFemale"))));
				}
				if(!jsondata.get("noOfLabourChild").equals(""))
				{
				lgacydetails.setLabourChild(Integer.parseInt(String.valueOf(jsondata.get("noOfLabourChild"))));
				}
				if(!jsondata.get("noOfLabourTransgender").equals(""))
				{
				lgacydetails.setLabourO(Integer.parseInt(String.valueOf(jsondata.get("noOfLabourTransgender"))));
				}
				/*if(!jsondata.get("totalBeneficiary").equals(""))
				{
				lgacydetails.setLabourTot(Integer.parseInt(String.valueOf(jsondata.get("totalBeneficiary"))));
				}*/
				if(!jsondata.get("noOfPatientAppliedLabourBoc").equals(""))
				{
				lgacydetails.setLabourRegBoc(Integer.parseInt(String.valueOf(jsondata.get("noOfPatientAppliedLabourBoc"))));
				}
				if(!jsondata.get("noOfPatientAppliedLabour").equals(""))
				{
				lgacydetails.setLabourRegOther(Integer.parseInt(String.valueOf(jsondata.get("noOfPatientAppliedLabour"))));
				}
				if(!jsondata.get("noOfPatientAppliedBoc").equals(""))
				{
				lgacydetails.setLabourAppBoc(Integer.parseInt(String.valueOf(jsondata.get("noOfPatientAppliedBoc"))));
				}
				if(!jsondata.get("noOfPatientAppliedOthers").equals(""))
				{
				lgacydetails.setLabourOthBoc(Integer.parseInt(String.valueOf(jsondata.get("noOfPatientAppliedOthers"))));
				}
				if(!jsondata.get("noOfUnrgistredWorkersTeated").equals(""))
				{
				lgacydetails.setLabourUnregBoc(Integer.parseInt(String.valueOf(jsondata.get("noOfUnrgistredWorkersTeated"))));
				}
				if(!jsondata.get("noOfUnrgistredWorkers").equals(""))
				{
				lgacydetails.setLabourUnregOther(Integer.parseInt(String.valueOf(jsondata.get("noOfUnrgistredWorkers"))));
				}
				if(!jsondata.get("noOfNonLabourGeneral").equals(""))
				{
				lgacydetails.setLabourRegGc(Integer.parseInt(String.valueOf(jsondata.get("noOfNonLabourGeneral"))));
				}
				
				legacyId = Long.parseLong(session.save(lgacydetails).toString());
				
			}
			tx.commit();

		} catch (Exception ex) {

			// System.out.println("Exception e="+ex.);
			ex.printStackTrace();
			tx.rollback();
			//System.out.println("Exception Message Print ::" + ex.toString());
			return ex.toString();
		} finally {

			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return "Successfully saved";
	}
	@Override
	public String addCluster(MasCluster masCluster) {
		String result="";
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(masCluster);
			tx.commit();
			if(savedObj!=null) {
				result="200";
			}else {
				result = "500";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return result;
	}

	@Override
	public Map<String, List<MasCluster>> getAllCluster(JSONObject jsondata) {
		Map<String, List<MasCluster>> map = new HashMap<String, List<MasCluster>>();
		List<MasCluster> clusterList = new ArrayList<MasCluster>();
		List totalMatches  =new ArrayList<>();
		int pageNo=1;
		int pageSize = 5;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasCluster.class);
			if(jsondata.get("PN").toString() !=null)
				pageNo = Integer.parseInt(jsondata.get("PN").toString());

			String clusterName="";
			if (jsondata.has("clusterName"))
			{
				clusterName = "%"+jsondata.get("clusterName")+"%";
				if(jsondata.get("clusterName").toString().length()>0 && !jsondata.get("clusterName").toString().trim().equalsIgnoreCase("")) {
					criteria.add(Restrictions.ilike("clusterName", clusterName));

				}
			}

			criteria.addOrder(Order.asc("clusterName"));
			totalMatches = criteria.list();
			if(!jsondata.has("comboVal"))
			{
				criteria.setFirstResult((pageSize) * (pageNo - 1));
				criteria.setMaxResults(pageSize);
			}
			clusterList = criteria.list();


			map.put("clusterList", clusterList);
			map.put("totalMatches", totalMatches);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return map;
	}

	@Override
	public String updateMasCluster(Long Id, String clusterName,String status, Long userId) {
		String result="";

		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();

			Object object = session.load(MasCluster.class, Id);
			MasCluster masClustr = (MasCluster)object;

			Transaction transaction = session.beginTransaction();
			masClustr.setClusterName(clusterName);
			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);
			masClustr.setLastChgDate(date);
			masClustr.setLastChgBy(userId);
			masClustr.setStatus(status);
			session.update(masClustr);
			transaction.commit();
			result = "200";


		}catch(Exception e) {

		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	
	public Map<String, List<Object[]>> getAllCityMmuMapping(JSONObject jsondata) {
		Map<String, List<Object[]>> map = new HashMap<>();
		List<CityMmuMapping> cityMmuList = new ArrayList<>();
		List totalMatches  =new ArrayList<>();
		int pageNo=1;
		int pageSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());;

		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(CityMmuMapping.class).createAlias("masCity", "mc").createAlias("masMmu", "mm");

			if(jsondata.has("cityId") && !jsondata.getString("cityId").trim().isEmpty() && !jsondata.getString("cityId").trim().equalsIgnoreCase("0")) {
				criteria.add(Restrictions.eq("cityId", jsondata.getLong("cityId")));
			}
			if(jsondata.has("mmuId") && !jsondata.getString("mmuId").trim().isEmpty() && !jsondata.getString("mmuId").trim().equalsIgnoreCase("0")) {
				criteria.add(Restrictions.eq("mmuId", jsondata.getLong("mmuId")));
			}
			if(jsondata.get("PN").toString() !=null)
				pageNo = Integer.parseInt(jsondata.get("PN").toString());

			if(jsondata.has("status") && !jsondata.get("status").toString().trim().isEmpty()) {
				criteria.add(Restrictions.eq("status", jsondata.get("status")));
			}

			criteria.addOrder(Order.asc("cityMmuMappingId"));
			
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("cityMmuMappingId").as("cityMmuMappingId"));//0
			projectionList.add(Projections.property("lastChangeBy").as("lastChangeBy"));//1
			projectionList.add(Projections.property("lastChangeDate").as("lastChangeDate"));//2
			projectionList.add(Projections.property("mc.cityId").as("cityId"));//3
			projectionList.add(Projections.property("mc.cityName").as("cityName"));//4
			projectionList.add(Projections.property("mm.mmuId").as("mmuId"));//5
			projectionList.add(Projections.property("mm.mmuName").as("mmuName"));//6
			projectionList.add(Projections.property("status").as("status"));//7
			criteria.setProjection(projectionList);
			List<Object[]> listObject=null;
			//listObject=criteria.list();
			//totalMatches = criteria.list();
			 if(CollectionUtils.isNotEmpty(listObject));
			 	totalMatches =criteria.list();
			if (pageNo > 0) {
				criteria.setFirstResult((pageSize) * (pageNo - 1));
				criteria.setMaxResults(pageSize);
			}
			
		//	cityMmuList = criteria.list();
			 listObject=criteria.list();

			 
			 
			 
			 
			map.put("cityMmuMappingList", listObject);
			map.put("totalMatches", totalMatches);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
        
		return map;
	}

	@Override
	public String addCityCluster(ClusterCityMapping clusterCityMapping) {
		String result="";
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(clusterCityMapping);
			tx.commit();
			if(savedObj!=null) {
				result="200";
			}else {
				result = "500";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return result;
	}

	/*************************************
	 * MAS IMPANNELED HOSPITAL
	 **************************************************************/
	@Override
	public Map<String, List<ClusterCityMapping>> getAllCityCluster(JSONObject jsonObj) {
		int pageSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
		int pageNo=0;

		Map<String, List<ClusterCityMapping>> mapObj = new HashMap<String, List<ClusterCityMapping>>();
		List<ClusterCityMapping> cityClusterList = new ArrayList<ClusterCityMapping>();
		List totalMatches = new ArrayList();

		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(ClusterCityMapping.class);

			if (jsonObj.get("PN") != null)
				pageNo = Integer.parseInt(jsonObj.get("PN").toString());

			Long clusterId = null;
			String hCode = "";
			if (jsonObj.has("clusterId")) {
				if (!jsonObj.getString("clusterId").toString().equals("")) {
					clusterId=Long.parseLong(jsonObj.get("clusterId").toString());
					criteria.add(Restrictions.eq("clusterId",clusterId));
				}
			}

			criteria.addOrder(Order.asc("clusterId"));
			totalMatches = criteria.list();

			criteria.setFirstResult((pageSize) * (pageNo - 1));
			criteria.setMaxResults(pageSize);
			cityClusterList = criteria.list();

			mapObj.put("totalMatches", totalMatches);
			mapObj.put("cityClusterList", cityClusterList);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mapObj;
	}

	@Override
	public String addDistrictCluster(ClusterDistrictMapping clusterDistrictMapping) {
		String result="";
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(clusterDistrictMapping);
			tx.commit();
			if(savedObj!=null) {
				result="200";
			}else {
				result = "500";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return result;
	}

	/*************************************
	 * MAS IMPANNELED HOSPITAL
	 **************************************************************/
	@Override
	public Map<String, List<ClusterDistrictMapping>> getAllDistrictCluster(JSONObject jsonObj) {
		int pageSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
		int pageNo=0;

		Map<String, List<ClusterDistrictMapping>> mapObj = new HashMap<String, List<ClusterDistrictMapping>>();
		List<ClusterDistrictMapping> districtClusterList = new ArrayList<ClusterDistrictMapping>();
		List totalMatches = new ArrayList();

		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(ClusterDistrictMapping.class);

			if (jsonObj.get("PN") != null)
				pageNo = Integer.parseInt(jsonObj.get("PN").toString());

			Long clusterId = null;
			String hCode = "";
			if (jsonObj.has("clusterId")) {
				if (!jsonObj.getString("clusterId").toString().equals("")) {
					clusterId=Long.parseLong(jsonObj.get("clusterId").toString());
					criteria.add(Restrictions.eq("clusterId",clusterId));
				}
			}

			criteria.addOrder(Order.asc("clusterId"));
			totalMatches = criteria.list();

			criteria.setFirstResult((pageSize) * (pageNo - 1));
			criteria.setMaxResults(pageSize);
			districtClusterList = criteria.list();

			mapObj.put("totalMatches", totalMatches);
			mapObj.put("districtClusterList", districtClusterList);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mapObj;
	}

	@Override
	public Map<String, List<MasCity>> getClusterByDistrict(JSONObject jsonObj) {
		Map<String, List<MasCity>> mapObj = new HashMap<String, List<MasCity>>();
		List<MasCity> districtClusterList = new ArrayList<MasCity>();
		List totalMatches = new ArrayList();

		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasCity.class);

			String clusterId = "";
			String hCode = "";
			if (jsonObj.has("clusterId")) {
				clusterId = "%"+jsonObj.get("clusterId") + "%";
				if (jsonObj.get("clusterId").toString().length() > 0
						&& !jsonObj.get("clusterId").toString().trim().equalsIgnoreCase("")) {
					criteria.add(Restrictions.ilike("clusterId", clusterId));
				}
			}

			if (jsonObj.has("districtId")) {
				Long districtId =Long.parseLong(jsonObj.get("districtId").toString());
				if (!jsonObj.get("districtId").toString().trim().equalsIgnoreCase("")) {
					MasDistrict masDistrict=new MasDistrict();
					masDistrict.setDistrictId(districtId);
					criteria.add(Restrictions.eq("masDistrict", masDistrict));
				}
			}

			//criteria.addOrder(Order.asc("clusterId"));
			totalMatches = criteria.list();

			districtClusterList = criteria.list();

			mapObj.put("totalMatches", totalMatches);
			mapObj.put("districtClusterList", districtClusterList);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mapObj;
	}

	@Override
	public Map<String, List<MasCity>> getClusterByCity(JSONObject jsonObj) {
		//Map<String, List<ClusterCityMapping>> mapObj = new HashMap<String, List<ClusterCityMapping>>();
		//List<ClusterCityMapping> cityClusterList = new ArrayList<ClusterCityMapping>();
		Map<String, List<MasCity>> mapObj = new HashMap<String, List<MasCity>>();
		List<MasCity> cityList = new ArrayList<MasCity>();
		
		List totalMatches = new ArrayList();

		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasCity.class);

			Long clusterId = null;
			String hCode = "";
			/*if (jsonObj.has("clusterId")) {
				clusterId = Long.parseLong(jsonObj.get("clusterId").toString());
				if (!jsonObj.get("clusterId").toString().trim().equalsIgnoreCase("")) {
					criteria.add(Restrictions.eq("clusterId", clusterId));
				}
			}*/
			if (jsonObj.has("districtId")) {
				clusterId = Long.parseLong(jsonObj.get("districtId").toString());
				if (!jsonObj.get("districtId").toString().trim().equalsIgnoreCase("")) {
					MasDistrict masDistrict=new MasDistrict();
					masDistrict.setDistrictId(clusterId);
					criteria.add(Restrictions.eq("masDistrict", masDistrict));
				}
			}


			if (jsonObj.has("cityId")) {
				Long cityId =Long.parseLong(jsonObj.get("cityId").toString());
				if (!jsonObj.get("cityId").toString().trim().equalsIgnoreCase("")) {
					criteria.add(Restrictions.eq("cityId", cityId));
				}
			}

			//criteria.addOrder(Order.asc("clusterId"));
			totalMatches = criteria.list();

			cityList = criteria.list();

			mapObj.put("totalMatches", totalMatches);
			mapObj.put("cityClusterList", cityList);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mapObj;
	}

	@Override
	public String updateClusterStatus(Long id, String status) {

		String result = "";
		try {

			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Object mmuObject = session.load(MasCluster.class, id);
			MasCluster mmu = (MasCluster) mmuObject;
			Transaction transaction = session.beginTransaction();
			if (mmu.getStatus().equalsIgnoreCase("Y")) {
				mmu.setStatus("n");

			} else if (mmu.getStatus().equalsIgnoreCase("n")) {
				mmu.setStatus("y");

			} else {
				mmu.setStatus("y");
			}

			session.update(mmu);
			transaction.commit();
			session.flush();
			result = "200";

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public String updateMasDistrictClusterMap(Long Id,Long clusterId, Long districtId,String status, Long userId) {
		String result="";

		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();

			Object object = session.load(ClusterDistrictMapping.class, Id);
			ClusterDistrictMapping masClustr = (ClusterDistrictMapping)object;

			Transaction transaction = session.beginTransaction();
			masClustr.setDistrictId(districtId);
			masClustr.setClusterId(clusterId);
			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);
			masClustr.setLastChgDate(date);
			masClustr.setLastChgBy(userId);
			masClustr.setStatus(status);
			session.update(masClustr);
			transaction.commit();
			result = "200";


		}catch(Exception e) {

		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public String updateDistrictClusterStatus(Long id, String status) {

		String result = "";
		try {

			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Object mmuObject = session.load(ClusterDistrictMapping.class, id);
			ClusterDistrictMapping mmu = (ClusterDistrictMapping) mmuObject;
			Transaction transaction = session.beginTransaction();
			if (mmu.getStatus().equalsIgnoreCase("Y")) {
				mmu.setStatus("n");

			} else if (mmu.getStatus().equalsIgnoreCase("n")) {
				mmu.setStatus("y");

			} else {
				mmu.setStatus("y");
			}

			session.update(mmu);
			transaction.commit();
			session.flush();
			result = "200";

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public String updateCityClusterStatus(Long id, String status) {

		String result = "";
		try {

			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Object mmuObject = session.load(ClusterCityMapping.class, id);
			ClusterCityMapping mmu = (ClusterCityMapping) mmuObject;
			Transaction transaction = session.beginTransaction();
			if (mmu.getStatus().equalsIgnoreCase("Y")) {
				mmu.setStatus("n");

			} else if (mmu.getStatus().equalsIgnoreCase("n")) {
				mmu.setStatus("y");

			} else {
				mmu.setStatus("y");
			}

			session.update(mmu);
			transaction.commit();
			session.flush();
			result = "200";

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public String updateMasCityClusterMap(Long cityClusterId, Long clusterId, Long cityId, String status,
										  Long userIdUpdate) {
		String result="";

		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();

			Object object = session.load(ClusterCityMapping.class, cityClusterId);
			ClusterCityMapping masClustr = (ClusterCityMapping)object;

			Transaction transaction = session.beginTransaction();
			masClustr.setCityId(cityId);
			masClustr.setClusterId(clusterId);
			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);
			masClustr.setLastChgDate(date);
			masClustr.setLastChgBy(userIdUpdate);
			masClustr.setStatus(status);
			session.update(masClustr);
			transaction.commit();
			result = "200";


		}catch(Exception e) {

		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}


	@Override
	public List<MasSociety> validateMasSociety(String societyCode, String societyName) {
		List<MasSociety> validSocietyList = new ArrayList<MasSociety>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasSociety.class);
			criteria.add(Restrictions.or(Restrictions.eq("societyCode", societyCode),
					Restrictions.eq("societyName", societyName)));
			validSocietyList = criteria.list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return validSocietyList;
	}

	@Override
	public String addSociety(MasSociety masSociety) {

		String result="";
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(masSociety);
			tx.commit();
			if(savedObj!=null) {
				result="200";
			}else {
				result = "500";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return result;
	}

	@Override
	public Map<String, List<MasSociety>> getAllSociety(JSONObject jsondata) {
		Map<String, List<MasSociety>> map = new HashMap<String, List<MasSociety>>();
		List<MasSociety> societyList = new ArrayList<MasSociety>();
		List totalMatches  =new ArrayList<>();
		int pageNo=1;
		int pageSize = 5;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasSociety.class);
			if(!jsondata.get("PN").toString().equals("0")) {
				pageNo = Integer.parseInt(jsondata.get("PN").toString());

				String societyName="";
				if (jsondata.has("societyName"))
				{
					societyName = "%"+jsondata.get("societyName")+"%";
					if(jsondata.get("societyName").toString().length()>0 && !jsondata.get("societyName").toString().trim().equalsIgnoreCase("")) {
						criteria.add(Restrictions.ilike("societyName", societyName));

					}
				}
				criteria.addOrder(Order.asc("societyName"));
				totalMatches = criteria.list();
				criteria.setFirstResult((pageSize) * (pageNo - 1));
				criteria.setMaxResults(pageSize);
				societyList = criteria.list();
			}
			if(jsondata.get("PN").toString().equals("0")) {
				criteria.addOrder(Order.asc("societyName"));
				criteria.add(Restrictions.eq("status", "y").ignoreCase());
				totalMatches = criteria.list();
				societyList = criteria.list();
			}

			map.put("societyList", societyList);
			map.put("totalMatches", totalMatches);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@Override
	public String updateSociety(MasSociety masSociety) {
		String result="";
		Session session = null;

		try {
			List<MasSociety> validate= validateMasSociety(masSociety.getSocietyCode(), masSociety.getSocietyName());
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Transaction transaction = session.beginTransaction();
			Object object = session.load(MasSociety.class, masSociety.getSocietyId());
			MasSociety masSocietyObj = (MasSociety)object;
			masSocietyObj.setSocietyCode(masSociety.getSocietyCode());
			if(validate.get(0).getSocietyName().equalsIgnoreCase(masSociety.getSocietyName())) {
				masSocietyObj.setSocietyName(masSocietyObj.getSocietyName());
			}
			else {
				masSocietyObj.setSocietyName(masSociety.getSocietyName());
			}
			masSocietyObj.setStatus(masSociety.getStatus());
			masSocietyObj.setLastChgBy(masSociety.getLastChgBy());
			masSocietyObj.setLastChgDate(masSociety.getLastChgDate());
			session.update(masSocietyObj);
			session.flush();
			session.clear();
			transaction.commit();
			result = "success";


		}catch(Exception e) {

		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public String updateSocietyStatus(long societyId, String status) {
		String result = "";
		try {

			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Object societyObj = session.load(MasSociety.class, societyId);
			MasSociety society = (MasSociety) societyObj;
			Transaction transaction = session.beginTransaction();
			if (society.getStatus().equalsIgnoreCase("Y")) {
				society.setStatus("n");

			} else if (society.getStatus().equalsIgnoreCase("n")) {
				society.setStatus("y");

			} else {
				society.setStatus("y");
			}

			session.update(society);
			transaction.commit();
			session.flush();
			result = "200";

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public Map<String, List<MasCity>> getCityList(JSONObject jsondata) {

		Map<String, List<MasCity>> map = new HashMap<String, List<MasCity>>();
		List<MasCity> cityList = new ArrayList<MasCity>();
		List totalMatches  =new ArrayList<>();
		int pageNo=1;
		int pageSize = 5;
		String cityName="";
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasCity.class);
			if(!jsondata.get("PN").toString().equals("0")) {
				pageNo = Integer.parseInt(jsondata.get("PN").toString());
				if (jsondata.has("cityName")) {
					cityName = "%"+jsondata.get("cityName")+"%";
					if(jsondata.get("cityName").toString().length()>0 && !jsondata.get("cityName").toString().trim().equalsIgnoreCase("")) {
						criteria.add(Restrictions.ilike("cityName", cityName));

					}
				}
				criteria.addOrder(Order.asc("cityName"));
				totalMatches = criteria.list();
				criteria.setFirstResult((pageSize) * (pageNo - 1));
				criteria.setMaxResults(pageSize);
				cityList = criteria.list();
			}
			if(jsondata.get("PN").toString().equals("0")) {
				criteria.addOrder(Order.asc("cityName"));
				criteria.add(Restrictions.eq("status", "y").ignoreCase());
				totalMatches = criteria.list();
				cityList = criteria.list();
			}

			map.put("cityList", cityList);
			map.put("totalMatches", totalMatches);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;

	}

	@Override
	public Map<String, List<MasSociety>> getSocietyList(JSONObject jsondata) {
		Map<String,List<MasSociety>> map = new HashMap<String,List<MasSociety>>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasSociety.class);

			List<MasSociety> societyList= criteria.list();
			map.put("societyList",societyList);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@Override
	public List<SocietyCityMapping> validateSocietyCitymapping(long cityId, long societyId) {
		List<SocietyCityMapping> validSocietyCityList = new ArrayList<SocietyCityMapping>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(SocietyCityMapping.class);
			criteria.add(Restrictions.or(Restrictions.eq("cityId", cityId),
					Restrictions.eq("societyId", societyId)));
			validSocietyCityList = criteria.list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return validSocietyCityList;
	}

	@Override
	public String addSocietyCity(SocietyCityMapping societyCityMap) {
		String result="";
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(societyCityMap);
			tx.commit();
			if(savedObj!=null) {
				result="200";
			}else {
				result = "500";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return result;
	}

	@Override
	public Map<String, List<SocietyCityMapping>> getAllCitySociety(JSONObject jsonData) {
		int pageSize =5;
		int pageNo=1;

		Map<String, List<SocietyCityMapping>> mapObj = new HashMap<String, List<SocietyCityMapping>>();
		List<SocietyCityMapping> societyCityList = new ArrayList<SocietyCityMapping>();
		List totalMatches = new ArrayList();

		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(SocietyCityMapping.class);

			if (jsonData.get("PN") != null)
				pageNo = Integer.parseInt(jsonData.get("PN").toString());

			String hCode = "";
			if (jsonData.has("cityId") && jsonData.get("cityId") != "") {
				Long cityId=Long.parseLong(jsonData.get("cityId").toString());
				criteria.add(Restrictions.eq("cityId",cityId));
			}

			criteria.addOrder(Order.asc("cityId"));
			totalMatches = criteria.list();

			criteria.setFirstResult((pageSize) * (pageNo - 1));
			criteria.setMaxResults(pageSize);
			societyCityList = criteria.list();

			mapObj.put("totalMatches", totalMatches);
			mapObj.put("societyCityList", societyCityList);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mapObj;
	}

	@Override
	public String updateSocietyCityStatus(long societyCityId, String status) {
		String result = "";
		try {

			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Object societyCityObj = session.load(SocietyCityMapping.class, societyCityId);
			SocietyCityMapping societyCity = (SocietyCityMapping) societyCityObj;
			Transaction transaction = session.beginTransaction();
			if (societyCity.getStatus().equalsIgnoreCase("Y")) {
				societyCity.setStatus("N");

			} else if (societyCity.getStatus().equalsIgnoreCase("N")) {
				societyCity.setStatus("Y");

			} else {
				societyCity.setStatus("Y");
			}

			session.update(societyCity);
			transaction.commit();
			session.flush();
			result = "200";

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public String updateSocietyCity(Long societyCityId, Long societyId, Long cityId, String status, Long userId) {
		String result="";
		long d = System.currentTimeMillis();
		Timestamp date = new Timestamp(d);
		try {
			List<SocietyCityMapping> validate= validateSocietyCitymapping(cityId, societyId);
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Transaction transaction = session.beginTransaction();

			Object object = session.load(SocietyCityMapping.class, societyCityId);

			SocietyCityMapping societyCityMapping = (SocietyCityMapping)object;
			societyCityMapping.setCityId(cityId);
			if(validate.get(0).getCityId() == cityId && validate.get(0).getSocietyId() == societyId) {
				societyCityMapping.setSocietyId(societyCityMapping.getSocietyId());;
			}else {
				societyCityMapping.setSocietyId(societyId);
			}

			societyCityMapping.setLastChgDate(date);
			societyCityMapping.setLastChgBy(userId);
			societyCityMapping.setStatus(status);
			session.update(societyCityMapping);
			transaction.commit();
			result = "200";

		}catch(Exception e) {

		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public List<FundSchemeMaster> validateFundSchemeMaster(String fundSchemeCode, String fundSchemeName) {
		List<FundSchemeMaster> validFundSchemeMaster = new ArrayList<FundSchemeMaster>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(FundSchemeMaster.class);
			criteria.add(Restrictions.or(Restrictions.eq("fundSchemeCode", fundSchemeCode),
					Restrictions.eq("fundSchemeName", fundSchemeName)));
			validFundSchemeMaster = criteria.list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return validFundSchemeMaster;
	}

	@Override
	public String addFundSchemeMaster(FundSchemeMaster fundSchemeMaster) {
		String result="";
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(fundSchemeMaster);
			tx.commit();
			if(savedObj!=null) {
				result="200";
			}else {
				result = "500";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return result;
	}

	@Override
	public Map<String, List<FundSchemeMaster>> getAllFundScheme(JSONObject jsonData) {
		int pageSize =5;
		int pageNo=1;

		Map<String, List<FundSchemeMaster>> mapObj = new HashMap<String, List<FundSchemeMaster>>();
		List<FundSchemeMaster> fundSchemeMasterList = new ArrayList<FundSchemeMaster>();
		List totalMatches = new ArrayList();

		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(FundSchemeMaster.class);

			if (jsonData.get("PN") != null)
				pageNo = Integer.parseInt(jsonData.get("PN").toString());

			String hCode = "";
			String fundSchemeName="";
			if (jsonData.has("fundSchemeName")) {
				fundSchemeName = "%"+jsonData.get("fundSchemeName")+"%";
				if(jsonData.get("fundSchemeName").toString().length()>0 && !jsonData.get("fundSchemeName").toString().trim().equalsIgnoreCase("")) {
					criteria.add(Restrictions.ilike("fundSchemeName", fundSchemeName));

				}
			}

			criteria.addOrder(Order.asc("fundSchemeName"));
			totalMatches = criteria.list();

			criteria.setFirstResult((pageSize) * (pageNo - 1));
			criteria.setMaxResults(pageSize);
			fundSchemeMasterList = criteria.list();

			mapObj.put("totalMatches", totalMatches);
			mapObj.put("fundSchemeMasterList", fundSchemeMasterList);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mapObj;
	}

	@Override
	public String upateFundSchemeMaster(Long fundSchemeId, String fundSchemeCode, String fundSchemeName, String status, long userId) {
		String result="";
		long d = System.currentTimeMillis();
		Timestamp date = new Timestamp(d);
		try {

			List<FundSchemeMaster> validate= validateFundSchemeMaster(fundSchemeCode, fundSchemeName);
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Object object = session.load(FundSchemeMaster.class, fundSchemeId);

			FundSchemeMaster fundSchemeMaster = (FundSchemeMaster)object;

			Transaction transaction = session.beginTransaction();
			fundSchemeMaster.setFundSchemeCode(fundSchemeCode);
			if(validate.get(0).getFundSchemeName().equalsIgnoreCase(fundSchemeName)) {
				fundSchemeMaster.setFundSchemeName(fundSchemeMaster.getFundSchemeName());
			}else {
				fundSchemeMaster.setFundSchemeName(fundSchemeName);
			}

			fundSchemeMaster.setLastChgDate(date);
			fundSchemeMaster.setLastChgBy(userId);
			fundSchemeMaster.setStatus(status);
			session.update(fundSchemeMaster);
			transaction.commit();
			result = "200";

		}catch(Exception e) {

		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public String updateFundSchemeStatus(long fundSchemeId, String status) {
		String result = "";
		try {

			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Object fundSchemeMasterObj = session.load(FundSchemeMaster.class, fundSchemeId);
			FundSchemeMaster fundSchemeMaster = (FundSchemeMaster) fundSchemeMasterObj;
			Transaction transaction = session.beginTransaction();

			if (fundSchemeMaster.getStatus().equalsIgnoreCase("Y")) {
				fundSchemeMaster.setStatus("N");

			} else if (fundSchemeMaster.getStatus().equalsIgnoreCase("N")) {
				fundSchemeMaster.setStatus("Y");

			} else {
				fundSchemeMaster.setStatus("Y");
			}

			session.update(fundSchemeMaster);
			transaction.commit();
			session.flush();
			result = "200";

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public Map<String, List<CityMmuMapping>> getMMUByCityCluster(JSONObject jsonObj) {
		Map<String, List<CityMmuMapping>> mapObj = new HashMap<String, List<CityMmuMapping>>();
		List<CityMmuMapping> cityMMUList = new ArrayList<CityMmuMapping>();
		List totalMatches = new ArrayList();

		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(CityMmuMapping.class);

			Long mmuId = null;
			String hCode = "";
			if (jsonObj.has("mmuId")) {
				mmuId = Long.parseLong(jsonObj.get("mmuId").toString());
				if (!jsonObj.get("mmuId").toString().trim().equalsIgnoreCase("")) {
					criteria.add(Restrictions.eq("mmuId", mmuId));
				}
			}

			if (jsonObj.has("cityId")) {
				Long cityId =Long.parseLong(jsonObj.get("cityId").toString());
				if (!jsonObj.get("cityId").toString().trim().equalsIgnoreCase("")) {
					criteria.add(Restrictions.eq("cityId", cityId));
				}
			}
			criteria.add(Restrictions.eq("status", "A").ignoreCase());
			//criteria.addOrder(Order.asc("clusterId"));
			//totalMatches = criteria.list();

			cityMMUList = criteria.list();

			//mapObj.put("totalMatches", totalMatches);
			mapObj.put("cityMMUList", cityMMUList);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mapObj;
	}

	@Override
	public Map<String, List<CityMmuMapping>> getMmuByCityMapping(JSONObject jsonData) {
		Map<String, List<CityMmuMapping>> mapObj = new HashMap<String, List<CityMmuMapping>>();
		List<CityMmuMapping> cityMmuList = new ArrayList<CityMmuMapping>();
		List totalMatches = new ArrayList();

		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(CityMmuMapping.class).createAlias("masCity", "masCity").createAlias("masMmu", "masMmu");

			if (jsonData.has("cityId") && !jsonData.get("cityId").toString().equalsIgnoreCase("")) {
				Long cityId =Long.parseLong(jsonData.get("cityId").toString());
				if (!jsonData.get("cityId").toString().trim().equalsIgnoreCase("")) {
					criteria.add(Restrictions.eq("cityId", cityId));
				}
			}
			if (jsonData.has("districtId") && !jsonData.get("districtId").toString().equalsIgnoreCase("")) {
				
				if (!jsonData.get("districtId").toString().trim().equalsIgnoreCase("")) {
					Long districtId =Long.parseLong(jsonData.get("districtId").toString());
					MasDistrict masDistrict=new MasDistrict();
					masDistrict.setDistrictId(districtId);
					criteria.add(Restrictions.eq("masCity.masDistrict", masDistrict));
				}
			}
			criteria.add(Restrictions.eq("status", "A").ignoreCase()).addOrder(Order.asc("masMmu.mmuName"));
			totalMatches = criteria.list();

			cityMmuList = criteria.list();

			mapObj.put("totalMatches", totalMatches);
			mapObj.put("cityMmuList", cityMmuList);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mapObj;
	}

	@Override
	public Map<String, List<MasCity>> getIndendeCityList(JSONObject jsondata) {
		Map<String, List<MasCity>> map = new HashMap<String, List<MasCity>>();
		List<MasCity> mascityList = new ArrayList<MasCity>();
		List totalMatches  =new ArrayList<>();
		int pageNo=1;
		int pageSize = 5;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasCity.class).createAlias("masDistrict", "did");					
			if(!jsondata.get("PN").toString().equals("0")) {
			 pageNo = Integer.parseInt(jsondata.get("PN").toString());
					
			String cityName="";
				 if (jsondata.has("cityName"))
				 {
					 cityName = "%"+jsondata.get("cityName")+"%";
					  if(jsondata.get("cityName").toString().length()>0 && !jsondata.get("cityName").toString().trim().equalsIgnoreCase("")) {
							criteria.add(Restrictions.ilike("cityName", cityName));
							
						}
				 }
				 criteria.add(Restrictions.eq("indentCity", "y"));
				 criteria.addOrder(Order.asc("cityName"));
				 totalMatches = criteria.list();				 
				 criteria.setFirstResult((pageSize) * (pageNo - 1));
				 criteria.setMaxResults(pageSize);
				 mascityList = criteria.list();
			}
		 
			if(jsondata.has("districtId") && jsondata.get("PN").toString().equals("0") ) {
				 criteria.addOrder(Order.asc("cityName"));
				 if(!jsondata.get("districtId").toString().equals("0")) {
				 criteria.add(Restrictions.eq("did.districtId", Long.parseLong(jsondata.get("districtId").toString())));
				 }
				 criteria.add(Restrictions.eq("status", "y").ignoreCase());
				 criteria.add(Restrictions.eq("indentCity", "y"));
				 totalMatches = criteria.list();				 
				 mascityList = criteria.list();
			 }	
			
		 if(jsondata.get("PN").toString().equals("0")) {
			 criteria.addOrder(Order.asc("cityName"));
			 criteria.add(Restrictions.eq("status", "y").ignoreCase());
			 criteria.add(Restrictions.eq("indentCity", "y"));
			 totalMatches = criteria.list();				 
			 mascityList = criteria.list();
		 }
		 
	
		map.put("mascityList", mascityList);
		map.put("totalMatches", totalMatches);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@Override
	public List<MasAuthority> getMasAuthorityList() {
		List<MasAuthority> masAuthorityData = new ArrayList<MasAuthority>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria cr = session.createCriteria(MasAuthority.class);
			// r.add(Restrictions.eq("user_name", string));
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("authorityId").as("authorityId"));
			projectionList.add(Projections.property("authorityName").as("authorityName"));
			projectionList.add(Projections.property("orderNo").as("orderNo"));
			cr.setProjection(projectionList);

			masAuthorityData = cr.setResultTransformer(new AliasToBeanResultTransformer(MasAuthority.class)).add(Restrictions.eq("status", 'Y').ignoreCase()).addOrder(Order.desc("orderNo")).list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return masAuthorityData;
	}

	 
	@Override
	public String updateCityMMUStatus(Long id, String status,Long mmuId,Long cityId) {
		
		String result = "";
		try {

			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Object mmuObject = session.load(CityMmuMapping.class, id);
			CityMmuMapping dist = (CityMmuMapping) mmuObject;
			Transaction transaction = session.beginTransaction();
			if (status.equalsIgnoreCase("A")) {
				dist.setStatus("I");

			} else if (status.equalsIgnoreCase("I")) {
				dist.setStatus("A");

			} else {
				dist.setStatus("A");
			}

			if(mmuId!=null) {
				dist.setMmuId(mmuId);
			}
			if(cityId!=null) {
				dist.setCityId(cityId);
			}
			session.update(dist);
			transaction.commit();
			session.flush();
			result = "200";
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	
	@Override
	public List<CityMmuMapping> validateCityMmuMapping(String cityId, String mmuId) {
		List<CityMmuMapping> validCityList = new ArrayList<CityMmuMapping>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(CityMmuMapping.class);
			criteria.add(Restrictions.and(Restrictions.eq("cityId", Long.parseLong(cityId)),
					Restrictions.eq("mmuId", Long.parseLong(mmuId))));
			validCityList = criteria.list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return validCityList;
	}

	
	@Override
	public List<MasAudit> getAuditList() {
		Session session = null;
		List<MasAudit> masAuditsList = new ArrayList<>();
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria cr = (Criteria) session.createCriteria(MasAudit.class).add(Restrictions.eq("status", 'Y').ignoreCase());
			masAuditsList = cr.addOrder(Order.asc("auditCode")).list();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return masAuditsList;
	}

	@Override
	public Map<String, List<MasMMU>> getMmuByDistrictId(JSONObject jsonData) {
		Map<String, List<MasMMU>> mapObj = new HashMap<String, List<MasMMU>>();
		List<MasMMU> mmuMmuList = new ArrayList<MasMMU>();
		List totalMatches = new ArrayList();

		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasMMU.class) ;

			if (jsonData.has("districtId") && !jsonData.get("districtId").toString().equalsIgnoreCase("")) {
				
				if (!jsonData.get("districtId").toString().trim().equalsIgnoreCase("")) {
					Long districtId =Long.parseLong(jsonData.get("districtId").toString());
					criteria.add(Restrictions.eq("districtId", districtId));
				}
			}
			 
			criteria.add(Restrictions.eq("status", "y").ignoreCase()).addOrder(Order.asc("mmuName"));
			totalMatches = criteria.list();

			mmuMmuList = criteria.list();

			mapObj.put("totalMatches", totalMatches);
			mapObj.put("mmuMmuList", mmuMmuList);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mapObj;
	}

	
	@Override
	public MasMMU getMasMMU(Long mmuId){
		Session session = null;
		MasMMU masMMU = null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			masMMU = (MasMMU) session.createCriteria(MasMMU.class).add(Restrictions.eq("mmuId", mmuId)).uniqueResult();
			 
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return masMMU;
	}

	@Override
	public List<MasStoreFinancial> getMasStoreFinancial() {
		List<MasStoreFinancial> masStoreFinancial = new ArrayList<MasStoreFinancial>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria cr = session.createCriteria(MasStoreFinancial.class);
			// r.add(Restrictions.eq("user_name", string));
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("financialId").as("financialId"));
			projectionList.add(Projections.property("startDate").as("startDate"));
			projectionList.add(Projections.property("endDate").as("endDate"));
			projectionList.add(Projections.property("yearDescription").as("yearDescription"));
			projectionList.add(Projections.property("markFinancialYear").as("markFinancialYear"));
			cr.setProjection(projectionList);

			masStoreFinancial = cr.setResultTransformer(new AliasToBeanResultTransformer(MasStoreFinancial.class)).add(Restrictions.eq("status", 'Y').ignoreCase()).addOrder(Order.asc("orderYear")).list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return masStoreFinancial;
	}

	@Override
	public List<MasHeadType> getMasHadType() {
		List<MasHeadType> masStoreFinancial = new ArrayList<MasHeadType>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria cr = session.createCriteria(MasHeadType.class);
			// r.add(Restrictions.eq("user_name", string));
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("headTypeId").as("headTypeId"));
			projectionList.add(Projections.property("headTypeCode").as("headTypeCode"));
			projectionList.add(Projections.property("headTypeName").as("headTypeName"));
			cr.setProjection(projectionList);

			masStoreFinancial = cr.setResultTransformer(new AliasToBeanResultTransformer(MasHeadType.class)).add(Restrictions.eq("status", 'Y').ignoreCase()).addOrder(Order.asc("headTypeId")).list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return masStoreFinancial;
	}


@Override
	public Map<String, List<MasHead>> getAllMasHead(JSONObject jsondata) {
		Map<String, List<MasHead>> map = new HashMap<String, List<MasHead>>();
		List<MasHead> masHeadList = new ArrayList<MasHead>();
		List totalMatches  =new ArrayList<>();
		int pageNo=1;
		int pageSize = 5;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasHead.class) ;					
			if(!jsondata.get("PN").toString().equals("0")) {
			 pageNo = Integer.parseInt(jsondata.get("PN").toString());
					
			String headTypeName="";
				 if (jsondata.has("headTypeName"))
				 {
					 headTypeName = "%"+jsondata.get("headTypeName")+"%";
					  if(jsondata.get("headTypeName").toString().length()>0 && !jsondata.get("headTypeName").toString().trim().equalsIgnoreCase("")) {
							criteria.add(Restrictions.ilike("headTypeName", headTypeName));
							
						}
				 }
				 criteria.addOrder(Order.asc("headTypeName"));
				 totalMatches = criteria.list();				 
				 criteria.setFirstResult((pageSize) * (pageNo - 1));
				 criteria.setMaxResults(pageSize);
				 masHeadList = criteria.list();
			}
		 
		 
			
		 if(jsondata.get("PN").toString().equals("0")) {
			 criteria.addOrder(Order.asc("headTypeName"));
			 criteria.add(Restrictions.eq("status", "y").ignoreCase());
			 totalMatches = criteria.list();				 
			 masHeadList = criteria.list();
		 }
		 
	
		map.put("masHeadList", masHeadList);
		map.put("totalMatches", totalMatches);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}
	 
	@Override
	public List<MasHead> validateMasHead(String headCode, String headName) {
		List<MasHead> validMasHeadList = new ArrayList<MasHead>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasHead.class);
			criteria.add(Restrictions.or(Restrictions.eq("headTypeCode", headCode),
					Restrictions.eq("headTypeName", headName)));
			validMasHeadList = criteria.list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return validMasHeadList;
	}
	 
	@Override
	public String addHead(MasHead masHead) {
		String result="";	
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(masHead);
			tx.commit();
			if(savedObj!=null) {
				result="200";
			}else {
				result = "500";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		
		return result;
	}
	
	 
	@Override
	public String updateHead(MasHead masHead) {
		String result="";
		Session session = null;
		
		try {
			List<MasHead> validate= validateMasHead(masHead.getHeadTypeCode(), masHead.getHeadTypeName());
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Transaction transaction = session.beginTransaction();
					Object object = session.load(MasHead.class, masHead.getHeadTypeId());
					MasHead masCityObj = (MasHead)object;
					masCityObj.setHeadTypeCode(masHead.getHeadTypeCode());				
					
					if(validate.get(0).getHeadTypeName().equalsIgnoreCase(masHead.getHeadTypeName()) 
							&& !validate.get(0).getHeadTypeId().toString().equalsIgnoreCase(masHead.getHeadTypeId().toString())) {
						masCityObj.setHeadTypeName(masCityObj.getHeadTypeName());	
						}					
					else {
						masCityObj.setHeadTypeName(masHead.getHeadTypeName());
		
					 
					masCityObj.setStatus(masHead.getStatus());
					masCityObj.setLastChangeBy(masHead.getLastChangeBy());
					masCityObj.setLastChangeDate(masHead.getLastChangeDate());	
					 
					session.update(masCityObj);					
					session.flush();
					session.clear();
					transaction.commit();					
					result = "success";
					}
											
		}catch(Exception e) {
			
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	
	 
	@Override
	public String updateHeadStatus(Long id, String status) {
		
		String result = "";
		try {

			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Object mmuObject = session.load(MasHead.class, id);
			MasHead masHead = (MasHead) mmuObject;
			Transaction transaction = session.beginTransaction();
			if (masHead.getStatus().equalsIgnoreCase("Y")) {
				masHead.setStatus("n");

			} else if (masHead.getStatus().equalsIgnoreCase("n")) {
				masHead.setStatus("y");

			} else {
				masHead.setStatus("y");
			}
			 
			session.update(masHead);
			transaction.commit();
			session.flush();
			result = "200";
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	@Override
	public Map<String, List<ApprovingAuthority>> getAllApprovalAuthority(JSONObject jsondata) {
		Map<String, List<ApprovingAuthority>> map = new HashMap<String, List<ApprovingAuthority>>();
		List<ApprovingAuthority> masHeadList = new ArrayList<ApprovingAuthority>();
		List totalMatches  =new ArrayList<>();
		int pageNo=1;
		int pageSize = 5;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(ApprovingAuthority.class) ;					
			if(!jsondata.get("PN").toString().equals("0")) {
			 pageNo = Integer.parseInt(jsondata.get("PN").toString());
					
			String searchApprovingAuthorityName="";
				 if (jsondata.has("searchApprovingAuthorityName"))
				 {
					 searchApprovingAuthorityName = "%"+jsondata.get("searchApprovingAuthorityName")+"%";
					  if(jsondata.get("searchApprovingAuthorityName").toString().length()>0 &&
							  !jsondata.get("searchApprovingAuthorityName").toString().trim().equalsIgnoreCase("")) {
							criteria.add(Restrictions.ilike("approvingAuthorityName", searchApprovingAuthorityName));
							
						}
				 }
				
				 
				 
				 criteria.addOrder(Order.asc("approvingAuthorityName"));
				 totalMatches = criteria.list();				 
				 criteria.setFirstResult((pageSize) * (pageNo - 1));
				 criteria.setMaxResults(pageSize);
				 masHeadList = criteria.list();
			}
			 if(jsondata.get("PN").toString().equals("0")) {
				 criteria.addOrder(Order.asc("approvingAuthorityName"));
				 criteria.add(Restrictions.eq("status", "y").ignoreCase());
				 totalMatches = criteria.list();				 
				 masHeadList = criteria.list();
			 }
		 
		map.put("masApprovingAuthorityList", masHeadList);
		map.put("totalMatches", totalMatches);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}
	 
	@Override
	public Map<String,  Object> getAllOrderNumber(JSONObject jsondata) {
		Map<String,  Object> map = new HashMap<String, Object>();
		List<ApprovingAuthority> approvingAuthorityList = new ArrayList<ApprovingAuthority>();
		List<MasUserType> masUserTypeList = new ArrayList<MasUserType>();
		 
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(ApprovingAuthority.class);					
			 	 criteria.addOrder(Order.asc("approvingAuthorityName"));
			 criteria.add(Restrictions.eq("status", "y").ignoreCase());
			 			 
			 approvingAuthorityList = criteria.list();
		 
				  criteria = session.createCriteria(MasUserType.class);					
			 	 criteria.addOrder(Order.asc("userTypeName"));
			 criteria.add(Restrictions.eq("status", "y").ignoreCase());
			 			 
			 masUserTypeList = criteria.list();
	
		map.put("approvingAuthorityList", approvingAuthorityList);
		map.put("masUserTypeList", masUserTypeList);
		 
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}
	
	 
	@Override
	public List<ApprovingAuthority> validateApprovingAuthority(String authorityCode, String authorityName) {
		List<ApprovingAuthority> validApprovingAuthorityList = new ArrayList<ApprovingAuthority>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(ApprovingAuthority.class);
			criteria.add(Restrictions.or(Restrictions.eq("approvingAuthorityCode", authorityCode),
					Restrictions.eq("approvingAuthorityName", authorityName)));
			validApprovingAuthorityList = criteria.list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return validApprovingAuthorityList;
	}
	 
	@Override
	public String addApprovalAuthority(ApprovingAuthority approvingAuthority) {
		String result="";	
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(approvingAuthority);
			tx.commit();
			if(savedObj!=null) {
				result="200";
			}else {
				result = "500";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		
		return result;
	}
	
	 
	@Override
	public String updateApprovingAuthority(ApprovingAuthority approvingAuthority) {
		String result="";
		Session session = null;
		
		try {
			List<ApprovingAuthority> validate= validateApprovingAuthority(approvingAuthority.getApprovingAuthorityCode(), approvingAuthority.getApprovingAuthorityName());
			List<ApprovingAuthority> validateOrder=null;
			if(approvingAuthority.getOrderNumber()!=0) {
				validateOrder=validateOrderNumber(approvingAuthority.getOrderNumber().toString());
				 }
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Transaction transaction = session.beginTransaction();
					Object object = session.load(ApprovingAuthority.class, approvingAuthority.getAuthorityId());
					ApprovingAuthority masCityObj = (ApprovingAuthority)object;
					masCityObj.setApprovingAuthorityCode(approvingAuthority.getApprovingAuthorityCode());				
					
					if(validate.get(0).getApprovingAuthorityName().equalsIgnoreCase(approvingAuthority.getApprovingAuthorityName()) && !validate.get(0).getAuthorityId().toString().equalsIgnoreCase(approvingAuthority.getAuthorityId().toString())) {
						masCityObj.setApprovingAuthorityName(masCityObj.getApprovingAuthorityName());	
						}					
					else {
						masCityObj.setApprovingAuthorityName(approvingAuthority.getApprovingAuthorityName());
							
					 
					masCityObj.setStatus(approvingAuthority.getStatus());
					masCityObj.setLastChangeBy(approvingAuthority.getLastChangeBy());
					masCityObj.setLastChangeDate(approvingAuthority.getLastChangeDate());	
					masCityObj.setFinalApprovingAuthority(approvingAuthority.getFinalApprovingAuthority());	
					 
					if(approvingAuthority.getOrderNumber()!=0) {
					 
						if(validateOrder!=null && validateOrder.size()!=0 && !validateOrder.get(0).getOrderNumber().toString().equalsIgnoreCase(masCityObj.getOrderNumber().toString()))
								masCityObj.setOrderNumber(approvingAuthority.getOrderNumber());
						else {
							if(validateOrder.size()==0 && approvingAuthority.getOrderNumber()!=0) {
								masCityObj.setOrderNumber(approvingAuthority.getOrderNumber());
							}
						}
					}
					if(!approvingAuthority.getLevelOfUser().equalsIgnoreCase("0"))
					masCityObj.setLevelOfUser(approvingAuthority.getLevelOfUser());
					session.update(masCityObj);					
					session.flush();
					session.clear();
					transaction.commit();					
					result = "success";
					}
											
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	 
	@Override
	public String updateApprovalAuthorityStatus(Long id, String status) {
		
		String result = "";
		try {

			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Object mmuObject = session.load(ApprovingAuthority.class, id);
			ApprovingAuthority approvingAuthority = (ApprovingAuthority) mmuObject;
			Transaction transaction = session.beginTransaction();
			if (approvingAuthority.getStatus().equalsIgnoreCase("Y")) {
				approvingAuthority.setStatus("n");

			} else if (approvingAuthority.getStatus().equalsIgnoreCase("n")) {
				approvingAuthority.setStatus("y");

			} else {
				approvingAuthority.setStatus("y");
			}
			 
			session.update(approvingAuthority);
			transaction.commit();
			session.flush();
			result = "200";
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public String checkFinalApproval(JSONObject jsondata) {
		
		String result="";
		 
		List<ApprovingAuthority> list=new ArrayList<ApprovingAuthority>();
		try {
			 
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(ApprovingAuthority.class);					
			list=criteria.add(Restrictions.eq("finalApprovingAuthority", 'y').ignoreCase()).list();
		 if(list !=null && list.size()>0 ) {
			 result="exists";
		 }
	
		
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	@Override
	public List<ApprovingAuthority> validateOrderNumber(String orderNumber) {
		List<ApprovingAuthority> validApprovingAuthorityList = new ArrayList<ApprovingAuthority>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(ApprovingAuthority.class);
			criteria.add(Restrictions.or(Restrictions.eq("orderNumber", Long.parseLong(orderNumber.trim()))));
					 
			validApprovingAuthorityList = criteria.list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return validApprovingAuthorityList;
	}

	@Override
	public String addApprovalAuthorityMapping(ApprovingMapping approvingMapping) {
		String result="";	
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(approvingMapping);
			tx.commit();
			if(savedObj!=null) {
				result="200";
			}else {
				result = "500";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		
		return result;
	}
	
	 
	@Override
	public List<ApprovingMapping> validateApprovingMapping(Long authorityId, Long userTypeId) {
		List<ApprovingMapping> validApprovingMappingList = new ArrayList<ApprovingMapping>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(ApprovingMapping.class);
			criteria.add(Restrictions.and(Restrictions.eq("authorityId", authorityId),
					Restrictions.eq("userTypeId", userTypeId)));
			validApprovingMappingList = criteria.list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return validApprovingMappingList;
	}

	 
	 
	@Override
	public Map<String, List<ApprovingMapping>> getAllApprovalAuthorityMapping(JSONObject jsondata) {
		Map<String, List<ApprovingMapping>> map = new HashMap<String, List<ApprovingMapping>>();
		List<ApprovingMapping> masHeadList = new ArrayList<ApprovingMapping>();
		List totalMatches  =new ArrayList<>();
		int pageNo=1;
		int pageSize = 5;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(ApprovingMapping.class) ;					
			if(!jsondata.get("PN").toString().equals("0")) {
			 pageNo = Integer.parseInt(jsondata.get("PN").toString());
					
			String searchApprovingAuthorityName="";
				 if (jsondata.has("searchApprovingAuthorityName"))
				 {
					 searchApprovingAuthorityName =  jsondata.get("searchApprovingAuthorityName").toString();
					  if(jsondata.get("searchApprovingAuthorityName").toString().length()>0 &&
							  !jsondata.get("searchApprovingAuthorityName").toString().trim().equalsIgnoreCase("")) {
							criteria.add(Restrictions.eq("authorityId", Long.parseLong(searchApprovingAuthorityName)));
							
						}
				 }
				  criteria.addOrder(Order.asc("authorityMappingId"));
					/* criteria.add(Restrictions.eq("status", "y").ignoreCase()); */
				 totalMatches = criteria.list();				 
				 criteria.setFirstResult((pageSize) * (pageNo - 1));
				 criteria.setMaxResults(pageSize);
				 masHeadList = criteria.list();
			}
			 if(jsondata.get("PN").toString().equals("0")) {
				 criteria.addOrder(Order.asc("authorityMappingId"));
				 criteria.add(Restrictions.eq("status", "y").ignoreCase());
				 totalMatches = criteria.list();				 
				 masHeadList = criteria.list();
			 }
		 
		map.put("masApprovingAuthorityList", masHeadList);
		map.put("totalMatches", totalMatches);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	 
	@Override
	public String updateApprovingAuthorityMapping(ApprovingMapping approvingAuthority) {
		String result="";
		Session session = null;
		
		try {
			List<ApprovingMapping> validate= validateApprovingMapping(approvingAuthority.getAuthorityId(), approvingAuthority.getUserTypeId());
		 	session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Transaction transaction = session.beginTransaction();
					Object object = session.load(ApprovingMapping.class, approvingAuthority.getAuthorityMappingId());
					ApprovingMapping masCityObj = (ApprovingMapping)object;
				 	if(validate!=null && validate.size()>0 && !validate.get(0).getAuthorityId().toString().equalsIgnoreCase(masCityObj.getAuthorityId().toString())) {
						masCityObj.setAuthorityId(masCityObj.getAuthorityId());	
						}					
					else {
						masCityObj.setAuthorityId(approvingAuthority.getAuthorityId());
				 	masCityObj.setStatus(approvingAuthority.getStatus());
				 	if(approvingAuthority.getUserTypeId()!=0) {
				 	masCityObj.setUserTypeId(approvingAuthority.getUserTypeId());
					}
				 
					session.update(masCityObj);					
					session.flush();
					session.clear();
					transaction.commit();					
					result = "success";
					}
											
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public String updateApprovalAuthorityMappingStatus(Long id, String status) {
		
		String result = "";
		try {

			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Object mmuObject = session.load(ApprovingMapping.class, id);
			ApprovingMapping approvingAuthority = (ApprovingMapping) mmuObject;
			Transaction transaction = session.beginTransaction();
			if (approvingAuthority.getStatus().equalsIgnoreCase("Y")) {
				approvingAuthority.setStatus("n");

			} else if (approvingAuthority.getStatus().equalsIgnoreCase("n")) {
				approvingAuthority.setStatus("y");

			} else {
				approvingAuthority.setStatus("y");
			}
			 
			session.update(approvingAuthority);
			transaction.commit();
			session.flush();
			result = "200";
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	 
	@Override
	public Map<String, List<MasStoreFinancial>> getAllFinancialYear(JSONObject jsondata) {
		Map<String, List<MasStoreFinancial>> map = new HashMap<String, List<MasStoreFinancial>>();
		List<MasStoreFinancial> masHeadList = new ArrayList<MasStoreFinancial>();
		List totalMatches  =new ArrayList<>();
		int pageNo=1;
		int pageSize = 5;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasStoreFinancial.class) ;					
			if(!jsondata.get("PN").toString().equals("0")) {
			 pageNo = Integer.parseInt(jsondata.get("PN").toString());
					
			String searchApprovingAuthorityName="";
				 if (jsondata.has("searchFinancialYear"))
				 {
					 searchApprovingAuthorityName =  "%"+jsondata.get("searchFinancialYear").toString()+"%";
					  if( 
							  !jsondata.get("searchFinancialYear").toString().trim().equalsIgnoreCase("")) {
							criteria.add(Restrictions.ilike("financialYear",  searchApprovingAuthorityName));
							
						}
				 }
				  criteria.addOrder(Order.asc("financialId"));
					/* criteria.add(Restrictions.eq("status", "y").ignoreCase()); */
				 totalMatches = criteria.list();				 
				 criteria.setFirstResult((pageSize) * (pageNo - 1));
				 criteria.setMaxResults(pageSize);
				 criteria.addOrder(Order.asc("orderYear"));
				 masHeadList = criteria.list();
			}
			 if(jsondata.get("PN").toString().equals("0")) {
				 criteria.addOrder(Order.asc("orderYear"));
				 criteria.add(Restrictions.eq("status", "y").ignoreCase());
				 totalMatches = criteria.list();				 
				 masHeadList = criteria.list();
			 }
		 
		map.put("masFinancialYearList", masHeadList);
		map.put("totalMatches", totalMatches);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}
	@Override
	public List<MasStoreFinancial> validateFinancialYear(String  financialYear) {
		List<MasStoreFinancial> validMasStoreFinancialList = new ArrayList<MasStoreFinancial>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasStoreFinancial.class);
			criteria.add( Restrictions.ilike("financialYear", financialYear)
					 );
			validMasStoreFinancialList = criteria.list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return validMasStoreFinancialList;
	}

	@Override
	public String addFinancialYear(MasStoreFinancial masStoreFinancial) {
		String result="";	
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(masStoreFinancial);
			tx.commit();
			if(savedObj!=null) {
				result="200";
			}else {
				result = "500";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		
		return result;
	}
	 
	@Override
	public String updateFinancialYear(MasStoreFinancial masStoreFinancial) {
		String result="";
		Session session = null;
		
		try {
			List<MasStoreFinancial> validate= validateFinancialYear(masStoreFinancial.getFinancialYear());
		 	session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Transaction transaction = session.beginTransaction();
					Object object = session.load(MasStoreFinancial.class, masStoreFinancial.getFinancialId());
					MasStoreFinancial masCityObj = (MasStoreFinancial)object;
				 	if(validate!=null && validate.size()>0 && validate.get(0).getFinancialId()!=masCityObj.getFinancialId()) {
						masCityObj.setFinancialId(masCityObj.getFinancialId());	
						}					
					else {
						masCityObj.setFinancialId(masStoreFinancial.getFinancialId());
						masCityObj.setStatus(masStoreFinancial.getStatus());
						masCityObj.setFinancialYear(masStoreFinancial.getFinancialYear());
						masCityObj.setYearDescription(masStoreFinancial.getFinancialYear());
						masCityObj.setStartDate(masStoreFinancial.getStartDate());
						masCityObj.setEndDate(masStoreFinancial.getEndDate());
						masCityObj.setLastChgBy(masStoreFinancial.getLastChgBy());
						masCityObj.setLastChgDate(masStoreFinancial.getLastChgDate());
						masCityObj.setMarkFinancialYear(masStoreFinancial.getMarkFinancialYear());
					session.update(masCityObj);					
					session.flush();
					session.clear();
					transaction.commit();					
					result = "success";
					}
											
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	

	@Override
	public String updateFinancialYearStatus(Long id, String status) {
		
		String result = "";
		try {

			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Object mmuObject = session.load(MasStoreFinancial.class, id);
			MasStoreFinancial approvingAuthority = (MasStoreFinancial) mmuObject;
			Transaction transaction = session.beginTransaction();
			if (approvingAuthority.getStatus().equalsIgnoreCase("Y")) {
				approvingAuthority.setStatus("n");

			} else if (approvingAuthority.getStatus().equalsIgnoreCase("n")) {
				approvingAuthority.setStatus("y");

			} else {
				approvingAuthority.setStatus("y");
			}
			 
			session.update(approvingAuthority);
			transaction.commit();
			session.flush();
			result = "200";
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public String checkFinancialYear(JSONObject jsondata) {
		
		String result="";
		 
		List<MasStoreFinancial> list=new ArrayList<MasStoreFinancial>();
		try {
			 
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasStoreFinancial.class);					
			list=criteria.add(Restrictions.eq("markFinancialYear", 'y').ignoreCase()).list();
		 if(list !=null && list.size()>0 ) {
			 result="exists"+"##"+list.get(0).getFinancialYear();
		 }
	
		
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}


	@Override
	public List<PenaltyAuthorityConfig> validatePenaltyAuthorityConfig(String uppsId, String authorityName) {
		List<PenaltyAuthorityConfig> penaltyAuthorityConfigList = new ArrayList<PenaltyAuthorityConfig>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(PenaltyAuthorityConfig.class);
			criteria.add(Restrictions.eq("uppsId", Long.parseLong(uppsId))).add(Restrictions.eq("authorityId", Long.parseLong(authorityName)));
					
			penaltyAuthorityConfigList = criteria.list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return penaltyAuthorityConfigList;
	}
	 

	@Override
	public String addPenalityApprovalAuthority(PenaltyAuthorityConfig penaltyAuthorityConfig) {
		String result="";	
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(penaltyAuthorityConfig);
			tx.commit();
			if(savedObj!=null) {
				result="200";
			}else {
				result = "500";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		
		return result;
	}

	@Override
	public Map<String, List<PenaltyAuthorityConfig>> getAllPenalityApprovalAuthority(JSONObject jsondata) {
		Map<String, List<PenaltyAuthorityConfig>> map = new HashMap<String, List<PenaltyAuthorityConfig>>();
		List<PenaltyAuthorityConfig> masHeadList = new ArrayList<PenaltyAuthorityConfig>();
		List totalMatches  =new ArrayList<>();
		int pageNo=1;
		int pageSize = 5;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(PenaltyAuthorityConfig.class) ;					
			if(!jsondata.get("PN").toString().equals("0")) {
			 pageNo = Integer.parseInt(jsondata.get("PN").toString());
					
			String searchApprovingAuthorityName="";
				 if (jsondata.has("searchUPPSName"))
				 {
					 searchApprovingAuthorityName =  jsondata.get("searchUPPSName").toString();
					  if(jsondata.get("searchUPPSName").toString().length()>0 &&
							  !jsondata.get("searchUPPSName").toString().trim().equalsIgnoreCase("") &&  !jsondata.get("searchUPPSName").toString().trim().equalsIgnoreCase("0")) {
							criteria.add(Restrictions.eq("uppsId", Long.parseLong(searchApprovingAuthorityName)));
							
						}
				 }
				
				 
				 
				 criteria.addOrder(Order.asc("authorityConfigId"));
				 totalMatches = criteria.list();				 
				 criteria.setFirstResult((pageSize) * (pageNo - 1));
				 criteria.setMaxResults(pageSize);
				 masHeadList = criteria.list();
			}
			 if(jsondata.get("PN").toString().equals("0")) {
				 criteria.addOrder(Order.asc("authorityConfigId"));
				 criteria.add(Restrictions.eq("status", "y").ignoreCase());
				 totalMatches = criteria.list();				 
				 masHeadList = criteria.list();
			 }
		 
		map.put("masPenalityApprovingAuthorityList", masHeadList);
		map.put("totalMatches", totalMatches);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}
		
	
	@Override
	public String updatePenalityApprovingAuthority(PenaltyAuthorityConfig penaltyAuthorityConfig) {
		String result="";
		Session session = null;
		
		try {
			/*List<ApprovingAuthority> validate= validateApprovingAuthority(approvingAuthority.getApprovingAuthorityCode(), approvingAuthority.getApprovingAuthorityName());
			List<ApprovingAuthority> validateOrder=null;
			if(approvingAuthority.getOrderNumber()!=0) {
				validateOrder=validateOrderNumber(approvingAuthority.getOrderNumber().toString());
				 }*/
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Transaction transaction = session.beginTransaction();
					Object object = session.load(PenaltyAuthorityConfig.class, penaltyAuthorityConfig.getAuthorityConfigId());
					PenaltyAuthorityConfig masCityObj = (PenaltyAuthorityConfig)object;
					//masCityObj.setApprovingAuthorityCode(approvingAuthority.getApprovingAuthorityCode());				
					
					if(masCityObj.getMasDistrict().getDistrictId()!=penaltyAuthorityConfig.getUppsId() ) {
						masCityObj.setUppsId(penaltyAuthorityConfig.getUppsId());
					}
						if( masCityObj.getApprovingAuthority().getAuthorityId()!= penaltyAuthorityConfig.getAuthorityId())
						masCityObj.setAuthorityId(penaltyAuthorityConfig.getAuthorityId());
			
										
			 				
					 
					masCityObj.setStatus(penaltyAuthorityConfig.getStatus());
					masCityObj.setLastChangeBy(penaltyAuthorityConfig.getLastChangeBy());
					masCityObj.setLastChangeDate(penaltyAuthorityConfig.getLastChangeDate());	
					 
					 
			 		session.update(masCityObj);					
					session.flush();
					session.clear();
					transaction.commit();					
					result = "success";
					}
	 		catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	
	 
	@Override
	public String updatePenalityApprovalAuthorityStatus(Long id, String status) {
		
		String result = "";
		try {

			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Object mmuObject = session.load(PenaltyAuthorityConfig.class, id);
			PenaltyAuthorityConfig approvingAuthority = (PenaltyAuthorityConfig) mmuObject;
			Transaction transaction = session.beginTransaction();
			if (approvingAuthority.getStatus().equalsIgnoreCase("Y")) {
				approvingAuthority.setStatus("n");

			} else if (approvingAuthority.getStatus().equalsIgnoreCase("n")) {
				approvingAuthority.setStatus("y");

			} else {
				approvingAuthority.setStatus("y");
			}
			 
			session.update(approvingAuthority);
			transaction.commit();
			session.flush();
			result = "200";
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public Map<String, List<MasStoreSupplier>> getAllMasStoreSupplier(JSONObject jsondata) {
		Map<String, List<MasStoreSupplier>> map = new HashMap<String, List<MasStoreSupplier>>();
		List<MasStoreSupplier> MasStoreSupplierList = new ArrayList<MasStoreSupplier>();
		List totalMatches  =new ArrayList<>();
		int pageNo=1;
		int pageSize = 5;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasStoreSupplier.class);					
			if(!jsondata.get("PN").toString().equals("0")) {
			 pageNo = Integer.parseInt(jsondata.get("PN").toString());
					
			String typeName="";
				 if (jsondata.has("typeName"))
				 {
					 typeName = "%"+jsondata.get("typeName")+"%";
					  if(jsondata.get("supplierName").toString().length()>0 && !jsondata.get("supplierName").toString().trim().equalsIgnoreCase("")) {
							criteria.add(Restrictions.ilike("supplierName", typeName));
							
						}
				 }
				 criteria.addOrder(Order.asc("supplierName"));
				 totalMatches = criteria.list();				 
				 criteria.setFirstResult((pageSize) * (pageNo - 1));
				 criteria.setMaxResults(pageSize);
				 MasStoreSupplierList = criteria.list();
			}
		 if(jsondata.get("PN").toString().equals("0")) {
			 criteria.addOrder(Order.asc("supplierName"));
			 criteria.add(Restrictions.eq("status", "y").ignoreCase());
			 totalMatches = criteria.list();				 
			 MasStoreSupplierList = criteria.list();
		 }
	
		map.put("masStoreSupplierList", MasStoreSupplierList);
		map.put("totalMatches", totalMatches);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	 
	@Override
	public String addMMUManufac(UpssManufacturerMapping massMMU) {
		String result="";	
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(massMMU);
			tx.commit();
			if(savedObj!=null) {
				result="200";
			}else {
				result = "500";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		
		return result;
	}

	 
	 
	@Override
	public List<UpssManufacturerMapping> validateUpssManufacturerMapping(Long itemId, Long districtId) {
		List<UpssManufacturerMapping> cmdList = null;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(UpssManufacturerMapping.class);
			criteria.add(Restrictions.and(Restrictions.eq("supplierId", itemId),
					Restrictions.eq("districtId", districtId)));
			cmdList = criteria.list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return cmdList;
	}


	 
	@Override
	public String updateUppsManu(UpssManufacturerMapping masmmu) {
		String result="";
		Session session = null;
		try {
			List<UpssManufacturerMapping> validate= validateUpssManufacturerMapping(masmmu.getSupplierId(), masmmu.getDistrictId());
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Transaction transaction = session.beginTransaction();
					Object object = session.load(UpssManufacturerMapping.class, masmmu.getUpssManuId());
					UpssManufacturerMapping masMMUObj = (UpssManufacturerMapping)object;	
					 if(validate!=null && validate.size()>0  ) {
						masMMUObj.setSupplierId(masmmu.getSupplierId());
					}
					else {	 	 	//masMMUObj.setCityId(masmmu.getCityId());
						masMMUObj.setStatus(masmmu.getStatus());
						masMMUObj.setLastChgBy(masmmu.getLastChgBy());
						masMMUObj.setLastChgDate(masmmu.getLastChgDate());
						masMMUObj.setSupplierId(masmmu.getSupplierId());
						masMMUObj.setDistrictId(masmmu.getDistrictId());
						session.update(masMMUObj);					
					session.flush();
					session.clear();
					transaction.commit();					
					result = "success";
					}
											
		}catch(Exception e) {
			
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	 
	@Override
	public String updateUpssManuStatus(Long id, String status) {
		
		String result = "";
		try {

			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Object mmuObject = session.load(UpssManufacturerMapping.class, id);
			UpssManufacturerMapping mmu = (UpssManufacturerMapping) mmuObject;
			Transaction transaction = session.beginTransaction();
			if (mmu.getStatus().equalsIgnoreCase("Y")) {
				mmu.setStatus("n");

			} else if (mmu.getStatus().equalsIgnoreCase("n")) {
				mmu.setStatus("y");

			} else {
				mmu.setStatus("y");
			}

			session.update(mmu);
			transaction.commit();
			session.flush();
			result = "200";
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	 
	@Override
	public Map<String, List<UpssManufacturerMapping>> getAllUpssManu(JSONObject jsondata) {
		Map<String, List<UpssManufacturerMapping>> map = new HashMap<String, List<UpssManufacturerMapping>>();
		List<UpssManufacturerMapping> mmuList = new ArrayList<UpssManufacturerMapping>();
		List totalMatches  =new ArrayList<>();
		int pageNo=1;
		int pageSize = 5;
		Long vendorId=null;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(UpssManufacturerMapping.class);					
			if(jsondata.get("PN") !=null && !jsondata.get("PN").equals("0") && !jsondata.get("PN").equals("")) {
			 pageNo = Integer.parseInt(jsondata.get("PN").toString());
					
			 
			 if (jsondata.has("mmuSearch"))
			 {
				  
				  if(!jsondata.get("mmuSearch").toString().trim().equalsIgnoreCase("")) {
					  Long districtId=jsondata.getLong("mmuSearch");
				 		criteria.add(Restrictions.eq("districtId", districtId));
						
					}
			 }
				 criteria.addOrder(Order.asc("upssManuId"));
				 totalMatches = criteria.list();
				if (pageNo > 0) {
					criteria.setFirstResult((pageSize) * (pageNo - 1));
					criteria.setMaxResults(pageSize);
				}
				 mmuList = criteria.list();
			}
			
		if(jsondata.get("PN").equals("0")) {
			criteria.addOrder(Order.asc("upssManuId"));
			criteria.add(Restrictions.eq("status", "y").ignoreCase());
			 totalMatches = criteria.list();
			 mmuList = criteria.list();
		}
		 
		
		map.put("mmuList", mmuList);
		map.put("totalMatches", totalMatches);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}
	
	@Override
	public Map<String, List<MasDistrict>> getFilterDistrict(JSONObject jsondata) {
		Map<String, List<MasDistrict>> map = new HashMap<String, List<MasDistrict>>();
		List<MasDistrict> districtList = new ArrayList<MasDistrict>();
		List totalMatches  =new ArrayList<>();
		int pageNo=1;
		int pageSize = 5;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasDistrict.class);					
			if(jsondata.get("PN").toString().equals("0")) {
			 pageNo = Integer.parseInt(jsondata.get("PN").toString());
			 Long [] districtIdVal=null;
				String [] districtIdValString= jsondata.get("districtUser").toString().split(",");
				districtIdVal = HMSUtil.convertFromStringToLongArray(districtIdValString);
		 	
			String districtName="";
				 if (districtIdVal!=null)
				 {
					
							criteria.add(Restrictions.in("districtId", districtIdVal));
				
				 }
				 criteria.addOrder(Order.asc("districtName"));
				 totalMatches = criteria.list();				 
				 criteria.setFirstResult((pageSize) * (pageNo - 1));
				 criteria.setMaxResults(pageSize);
				 districtList = criteria.list();
			}
		
		map.put("districtList", districtList);
		map.put("totalMatches", totalMatches);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}
	
	private Long getDistrictIdVal(Long cityIdVal) {
		MasCity masCity = null;
		long districtId = 0;
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		try {
			masCity = (MasCity) session.createCriteria(MasCity.class).add(Restrictions.eq("cityId",cityIdVal))
					.uniqueResult();
			if (masCity != null) {
				districtId = masCity.getDistrictId();
			}

		} catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		}
		return districtId;
	}

	@Override
	public Map<String, List<MasPhase>> getMasPhase(JSONObject jsonObj) {
		Map<String, List<MasPhase>> mapObj = new HashMap<String, List<MasPhase>>();
		int pageSize = 5;
		int pageNo=1;
		
		List totalMatches = new ArrayList();
		 
		List<MasPhase> masPhaseList = new ArrayList<MasPhase>();
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasPhase.class);		
			//System.out.println("jsonObj dao  :: " +jsonObj);		
			if( jsonObj.get("PN")!=null)
			 pageNo = Integer.parseInt(jsonObj.get("PN").toString());
			//System.out.println("pageNo  :: " +pageNo);		
			String rankName="";
				 if (jsonObj.has("phaseName"))
				 {
					  rankName = "%"+jsonObj.get("phaseName")+"%";
					  if(jsonObj.get("phaseName").toString().length()>0 && !jsonObj.get("phaseName").toString().trim().equalsIgnoreCase("")) {
							criteria.add(Restrictions.ilike("phaseName", rankName));
						}
				 }	
				 
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("phaseId").as("phaseId"));
			projectionList.add(Projections.property("phaseValue").as("phaseValue"));
			projectionList.add(Projections.property("phaseName").as("phaseName"));
			projectionList.add(Projections.property("status").as("status"));
			criteria.addOrder(Order.asc("phaseId"));
			
			totalMatches = criteria.list();
			//System.out.println("totalMatches :: "+totalMatches.size());
					
			criteria.setFirstResult((pageSize) * (pageNo - 1));
			criteria.setProjection(projectionList).setMaxResults(pageSize);
			masPhaseList = criteria.setResultTransformer(new AliasToBeanResultTransformer(MasPhase.class)).list();
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			mapObj.put("masPhaseList", masPhaseList);
			mapObj.put("totalMatches", totalMatches);
			return mapObj;
		}
	
	@Override
	public List<UpssPhaseMapping> validateUpssPhaseMapping(Long phaseId, Long upssId) {
		List<UpssPhaseMapping> cmdList = null;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(UpssPhaseMapping.class);
			criteria.add(Restrictions.and(Restrictions.eq("phaseId", phaseId),
					Restrictions.eq("upssId", upssId)));
			cmdList = criteria.list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return cmdList;
	}
	
	@Override
	public String addUpssPhaseMapping(UpssPhaseMapping massMMU) {
		String result="";	
		Transaction tx=null;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			Serializable savedObj =  session.save(massMMU);
			tx.commit();
			if(savedObj!=null) {
				result="200";
			}else {
				result = "500";
			}
			session.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		
		return result;
	}
	
	@Override
	public Map<String, List<UpssPhaseMapping>> getAllUpssPhaseMapping(JSONObject jsondata) {
		Map<String, List<UpssPhaseMapping>> map = new HashMap<String, List<UpssPhaseMapping>>();
		List<UpssPhaseMapping> mmuList = new ArrayList<UpssPhaseMapping>();
		List totalMatches  =new ArrayList<>();
		int pageNo=1;
		int pageSize = 5;
		Long vendorId=null;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(UpssPhaseMapping.class);					
			if(jsondata.get("PN") !=null && !jsondata.get("PN").equals("0") && !jsondata.get("PN").equals("")) {
			 pageNo = Integer.parseInt(jsondata.get("PN").toString());
					
			 
			 if (jsondata.has("mmuSearch"))
			 {
				  
				  if(!jsondata.get("mmuSearch").toString().trim().equalsIgnoreCase("")) {
					  //Long districtId=jsondata.getLong("mmuSearch");
					  Long [] districtIdList=null;
					  String []districtIdString = jsondata.getString("mmuSearch").trim().split(",");
					  districtIdList=HMSUtil.convertFromStringToLongArray(districtIdString);
				 		criteria.add(Restrictions.in("upssId", districtIdList));
				 		criteria.addOrder(Order.asc("phaseId"));
				 		if(!jsondata.has("masterPage"))
				 		{	
				 		criteria.add(Restrictions.eq("status", "y").ignoreCase());
				 		}
						
					}
			 }
			
				 criteria.addOrder(Order.asc("phaseId"));
				 totalMatches = criteria.list();
				 pageNo=Integer.parseInt(jsondata.get("PN").toString());
				if (pageNo > 0) {
					criteria.setFirstResult((pageSize) * (pageNo - 1));
					criteria.setMaxResults(pageSize);
				}
				 mmuList = criteria.list();
			}
			
		if(jsondata.get("PN").equals("0") &&!jsondata.has("phaseValue")) {
			 criteria.addOrder(Order.asc("phaseId"));
			if(!jsondata.get("mmuSearch").equals("") && !jsondata.get("mmuSearch").equals("0"))
			 {	
			 criteria.add(Restrictions.eq("upssId", Long.parseLong(jsondata.get("mmuSearch").toString())));
			 }
		 	 criteria.add(Restrictions.eq("status", "y").ignoreCase());
		 	
			 totalMatches = criteria.list();
			 mmuList = criteria.list();
			
		}
		 if (jsondata.has("phaseValue"))
		 {
			  
			 if(!jsondata.get("phaseValue").toString().trim().equalsIgnoreCase("")) {
				  //Long districtId=jsondata.getLong("mmuSearch");
				 String phaseVal=jsondata.get("phaseValue").toString();
				 Long phaseId=getPhaseId(phaseVal);
				 criteria.add(Restrictions.eq("phaseId", phaseId));
			 	 criteria.add(Restrictions.eq("status", "y").ignoreCase());
			 	 criteria.addOrder(Order.asc("phaseId"));
				 totalMatches = criteria.list();
				 pageNo=Integer.parseInt(jsondata.get("PN").toString());
				if (pageNo > 0) {
					criteria.setFirstResult((pageSize) * (pageNo - 1));
					criteria.setMaxResults(pageSize);
				}
				 mmuList = criteria.list();
				}
		 }
		 
		if(jsondata.get("PN").equals("0")&&!jsondata.has("phaseValue"))
		{
			Set<String> nameSet = new HashSet<>();
			mmuList = mmuList.stream()
		            .filter(e -> nameSet.add(e.getMasPhase().getPhaseValue()))
		            .collect(Collectors.toList());
			
		}

		map.put("mmuList", mmuList);
		map.put("totalMatches", totalMatches);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}
	
	@Override
	public String updateUpssPhaseStatus(Long id, String status) {
		
		String result = "";
		try {

			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Object mmuObject = session.load(UpssPhaseMapping.class, id);
			UpssPhaseMapping mmu = (UpssPhaseMapping) mmuObject;
			Transaction transaction = session.beginTransaction();
			if (mmu.getStatus().equalsIgnoreCase("Y")) {
				mmu.setStatus("n");

			} else if (mmu.getStatus().equalsIgnoreCase("n")) {
				mmu.setStatus("y");

			} else {
				mmu.setStatus("y");
			}

			session.update(mmu);
			transaction.commit();
			session.flush();
			result = "200";
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	@Override
	public String updateUppsPhase(UpssPhaseMapping masmmu) {
		String result="";
		Session session = null;
		try {
			List<UpssPhaseMapping> validate= validateUpssPhaseMapping(masmmu.getPhaseId(), masmmu.getUpssId());
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Transaction transaction = session.beginTransaction();
					Object object = session.load(UpssPhaseMapping.class, masmmu.getUpssPhaseId());
					UpssPhaseMapping masMMUObj = (UpssPhaseMapping)object;	
					 if(validate!=null && validate.size()>0  ) {
						masMMUObj.setPhaseId(masmmu.getPhaseId());
					}
					else {	 	 	//masMMUObj.setCityId(masmmu.getCityId());
						masMMUObj.setStatus(masmmu.getStatus());
						masMMUObj.setLastChgBy(masmmu.getLastChgBy());
						masMMUObj.setLastChgDate(masmmu.getLastChgDate());
						masMMUObj.setPhaseId(masmmu.getPhaseId());
						masMMUObj.setUpssId(masmmu.getUpssId());
						session.update(masMMUObj);					
					session.flush();
					session.clear();
					transaction.commit();					
					result = "success";
					}
											
		}catch(Exception e) {
			
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	public Long getPhaseId(String phaseValue) {
		MasPhase masPhase = null;
		long phaseId = 0;
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		try {
			masPhase = (MasPhase) session.createCriteria(MasPhase.class).add(Restrictions.eq("phaseValue", phaseValue)).uniqueResult();
			if (masPhase != null) {
				phaseId = masPhase.getPhaseId();
			}

		} catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		}
		return phaseId;
	}

	@Override
	public List<MasStoreFinancial> getMasStoreFutureFinancial() {
		List<MasStoreFinancial> masStoreFinancial = new ArrayList<MasStoreFinancial>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria cr = session.createCriteria(MasStoreFinancial.class);
			// r.add(Restrictions.eq("user_name", string));
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("financialId").as("financialId"));
			projectionList.add(Projections.property("startDate").as("startDate"));
			projectionList.add(Projections.property("endDate").as("endDate"));
			projectionList.add(Projections.property("yearDescription").as("yearDescription"));
			projectionList.add(Projections.property("markFinancialYear").as("markFinancialYear"));
			cr.setProjection(projectionList);

			masStoreFinancial = cr.setResultTransformer(new AliasToBeanResultTransformer(MasStoreFinancial.class)).add(Restrictions.eq("status", 'Y').ignoreCase()).add(Restrictions.eq("futureYear", 'Y').ignoreCase()).addOrder(Order.asc("orderYear")).list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return masStoreFinancial;
	}

	@Override
	public Map<String, List<UpssManufacturerMapping>> getAllUpssManufactureMapping(JSONObject jsondata,Long districtId) {
		Map<String, List<UpssManufacturerMapping>> map = new HashMap<String, List<UpssManufacturerMapping>>();
		List<UpssManufacturerMapping> mmuList = new ArrayList<UpssManufacturerMapping>();
		List totalMatches  =new ArrayList<>();
		int pageNo=1;
		int pageSize = 5;
		Long vendorId=null;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(UpssManufacturerMapping.class);					
			
		 if (districtId!=null)
		 {
				 criteria.add(Restrictions.eq("districtId", districtId));
			 	 criteria.add(Restrictions.eq("status", "y").ignoreCase());
			 	 criteria.addOrder(Order.asc("districtId"));
				 totalMatches = criteria.list();
				 mmuList = criteria.list();
				
		 }
		 
		if(jsondata.get("PN").equals("0")&&!jsondata.has("phaseValue"))
		{
			Set<String> nameSet = new HashSet<>();
			mmuList = mmuList.stream()
		            .filter(e -> nameSet.add(e.getMasStoreSupplier().getSupplierName()))
		            .collect(Collectors.toList());
			
		}

		map.put("mmuList", mmuList);
		map.put("totalMatches", totalMatches);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}
	
	@Override
	public Long getMMUDistrictId(Long mmuId) {
		MasMMU masMmu = null;
		long districtId = 0;
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		try {
			masMmu = (MasMMU) session.createCriteria(MasMMU.class).add(Restrictions.eq("mmuId", mmuId)).uniqueResult();
			if (masMmu != null) {
				districtId = masMmu.getDistrictId();
			}

		} catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		}
		return districtId;
	}
	
	@Override
	public Long getCityDistrictId(Long cityId) {
		MasCity masCity = null;
		long districtId = 0;
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		try {
			masCity = (MasCity) session.createCriteria(MasCity.class).add(Restrictions.eq("cityId", cityId)).uniqueResult();
			if (masCity != null) {
				districtId = masCity.getDistrictId();
			}

		} catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		}
		return districtId;
	}
	
	@Override
	public List getFinancialYearFilter(String startDate) {
		Date sDate=HMSUtil.convertStringDateToUtilDateForDatabase(startDate);
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		//List<MasCamp> list = null;
		List<Object[]> searchList = null;
		try {	
			String Query="select * from MAS_STORE_FINANCIAL where order_year >= (SELECT max(order_year) FROM MAS_STORE_FINANCIAL where ( '"+sDate+"' BETWEEN start_date AND end_date)) order by order_year asc";
			
			if (Query != null) 
			{
				searchList = session.createSQLQuery(Query).list();
			} 
			else
			{
				System.out.println("No Record Found");
			}
			return searchList;
			}catch(Exception ex) {
				ex.printStackTrace();
			}finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
			return searchList;
		}
	
	@Override
	public List<CityMmuInvoiceMapping> validateCityMmuInvoiceMapping(String cityId, String mmuId,String phaseId) {
		List<CityMmuInvoiceMapping> validCityList = new ArrayList<CityMmuInvoiceMapping>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(CityMmuInvoiceMapping.class);
			criteria.add(Restrictions.and(Restrictions.eq("cityId", Long.parseLong(cityId)),
					Restrictions.eq("mmuId", Long.parseLong(mmuId)),
					Restrictions.eq("phaseId", Long.parseLong(phaseId))));
			validCityList = criteria.list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return validCityList;
	}

	@Override
	public Map<String, List<Object[]>> getAllCityMmuPhaseMapping(JSONObject jsondata) {
		
		System.out.println("MMU Phase ma call");
		Map<String, List<Object[]>> map = new HashMap<>();
		List<CityMmuInvoiceMapping> cityMmuList = new ArrayList<>();
		List totalMatches  =new ArrayList<>();
		int pageNo=1;
		int pageSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());;

		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(CityMmuInvoiceMapping.class).createAlias("masCity", "mc").createAlias("masMmu", "mm").createAlias("masPhase", "mp");

			if(jsondata.has("cityId") && !jsondata.getString("cityId").trim().isEmpty() && !jsondata.getString("cityId").trim().equalsIgnoreCase("0")) {
				criteria.add(Restrictions.eq("cityId", jsondata.getLong("cityId")));
			}
			if(jsondata.has("mmuId") && !jsondata.getString("mmuId").trim().isEmpty() && !jsondata.getString("mmuId").trim().equalsIgnoreCase("0")) {
				criteria.add(Restrictions.eq("mmuId", jsondata.getLong("mmuId")));
			}
			if(jsondata.has("phaseId") && !jsondata.getString("phaseId").trim().isEmpty() && !jsondata.getString("phaseId").trim().equalsIgnoreCase("0")) {
				criteria.add(Restrictions.eq("phaseId", jsondata.getLong("phaseId")));
			}
			if(jsondata.get("PN").toString() !=null)
				pageNo = Integer.parseInt(jsondata.get("PN").toString());

			if(jsondata.has("status") && !jsondata.get("status").toString().trim().isEmpty()) {
				criteria.add(Restrictions.eq("status", jsondata.get("status")));
			}

			criteria.addOrder(Order.asc("cityMmuInvoiceMappingId"));
			
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("cityMmuInvoiceMappingId").as("cityMmuInvoiceMappingId"));//0
			projectionList.add(Projections.property("lastChangeBy").as("lastChangeBy"));//1
			projectionList.add(Projections.property("lastChangeDate").as("lastChangeDate"));//2
			projectionList.add(Projections.property("mc.cityId").as("cityId"));//3
			projectionList.add(Projections.property("mc.cityName").as("cityName"));//4
			projectionList.add(Projections.property("mm.mmuId").as("mmuId"));//5
			projectionList.add(Projections.property("mm.mmuName").as("mmuName"));//6
			projectionList.add(Projections.property("status").as("status"));//7
			projectionList.add(Projections.property("mp.phaseId").as("phaseId"));//8
			projectionList.add(Projections.property("mp.phaseName").as("phaseName"));//9
			criteria.setProjection(projectionList);
			List<Object[]> listObject=null;
			//listObject=criteria.list();
			//totalMatches = criteria.list();
			 if(CollectionUtils.isNotEmpty(listObject));
			 	totalMatches =criteria.list();
			if (pageNo > 0) {
				criteria.setFirstResult((pageSize) * (pageNo - 1));
				criteria.setMaxResults(pageSize);
			}
			
		//	cityMmuList = criteria.list();
			 listObject=criteria.list();

			 
			 
			 
			 
			map.put("cityMmuMappingList", listObject);
			map.put("totalMatches", totalMatches);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
        
		return map;
	}
	
	@Override
	public String updateCityMMUPhaseStatus(Long id, String status,Long mmuId,Long cityId,Long phaseId) {
		
		String result = "";
		try {

			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Object mmuObject = session.load(CityMmuInvoiceMapping.class, id);
			CityMmuInvoiceMapping dist = (CityMmuInvoiceMapping) mmuObject;
			Transaction transaction = session.beginTransaction();
			if (status.equalsIgnoreCase("A")) {
				dist.setStatus("I");

			} else if (status.equalsIgnoreCase("I")) {
				dist.setStatus("A");

			} else {
				dist.setStatus("A");
			}

			if(mmuId!=null) {
				dist.setMmuId(mmuId);
			}
			if(cityId!=null) {
				dist.setCityId(cityId);
			}
			if(phaseId!=null) {
				dist.setPhaseId(phaseId);
			}
			session.update(dist);
			transaction.commit();
			session.flush();
			result = "200";
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	@Override
	public Map<String, List<CityMmuInvoiceMapping>> getMmuByCityMMUPhaseMapping(JSONObject jsonData) {
		Map<String, List<CityMmuInvoiceMapping>> mapObj = new HashMap<String, List<CityMmuInvoiceMapping>>();
		List<CityMmuInvoiceMapping> cityMmuList = new ArrayList<CityMmuInvoiceMapping>();
		List totalMatches = new ArrayList();

		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(CityMmuInvoiceMapping.class).createAlias("masCity", "masCity").createAlias("masMmu", "masMmu");

			if (jsonData.has("cityId") && !jsonData.get("cityId").toString().equalsIgnoreCase("")) {
				Long cityId =Long.parseLong(jsonData.get("cityId").toString());
				if (!jsonData.get("cityId").toString().trim().equalsIgnoreCase("")) {
					criteria.add(Restrictions.eq("cityId", cityId));
				}
			}
			if (jsonData.has("phaseId") && !jsonData.get("phaseId").toString().equalsIgnoreCase("")) {
				Long cityId =Long.parseLong(jsonData.get("phaseId").toString());
				if (!jsonData.get("phaseId").toString().trim().equalsIgnoreCase("")) {
					criteria.add(Restrictions.eq("phaseId", cityId));
				}
			}
			if (jsonData.has("districtId") && !jsonData.get("districtId").toString().equalsIgnoreCase("")) {
				
				if (!jsonData.get("districtId").toString().trim().equalsIgnoreCase("")) {
					Long districtId =Long.parseLong(jsonData.get("districtId").toString());
					MasDistrict masDistrict=new MasDistrict();
					masDistrict.setDistrictId(districtId);
					criteria.add(Restrictions.eq("masCity.masDistrict", masDistrict));
				}
			}
			criteria.add(Restrictions.eq("status", "A").ignoreCase()).addOrder(Order.asc("masMmu.mmuName"));
			totalMatches = criteria.list();

			cityMmuList = criteria.list();

			mapObj.put("totalMatches", totalMatches);
			mapObj.put("cityMmuList", cityMmuList);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mapObj;
	}
	
}

