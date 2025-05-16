package com.mmu.services.dao.impl;

import com.mmu.services.dao.AuditDao;
import com.mmu.services.dao.FundHcbDao;
import com.mmu.services.entity.*;
import com.mmu.services.hibernateutils.GetHibernateUtils;
import com.mmu.services.utils.HMSUtil;
import com.mmu.services.utils.ProjectUtils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.hibernate.*;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.jdbc.Work;
import org.json.JSONArray;
import org.json.JSONObject;
import org.postgresql.core.NativeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.registry.infomodel.User;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class AuditDaoImpl implements AuditDao {

    @Autowired
    GetHibernateUtils getHibernateUtils;
    
    @Autowired
    FundHcbDao fundHcbDao;

  
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
    public Long createRecord(Serializable entity) {
        Long result=null;
        Transaction tx=null;
        Session session=null;
        try {
            session = getHibernateUtils.getHibernateUtlis().OpenSession();
            tx = session.beginTransaction();
            result =  (Long) session.save(entity);
            tx.commit();
            session.flush();
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            getHibernateUtils.getHibernateUtlis().CloseConnection();
        }

        return result;
    }
    
    @Override
    public Long createOrUpdateMMURecord(Serializable entity, Long mmuId, Long captureVendorBillDetailId) {
        Long result = null;
        Transaction tx = null;
        Session session = null;

        try {
            session = getHibernateUtils.getHibernateUtlis().OpenSession();
            tx = session.beginTransaction();

            // Check if the record exists
            String hql = "from CaptureVendorBillMMUDetail where mmuId = :mmuId and captureVendorBillDetailId = :captureVendorBillDetailId";
            Query query = session.createQuery(hql);
            query.setParameter("mmuId", mmuId);
            query.setParameter("captureVendorBillDetailId", captureVendorBillDetailId);

            CaptureVendorBillMMUDetail existingEntity = (CaptureVendorBillMMUDetail) query.uniqueResult();
            if (existingEntity != null) {
                // Update existing entity
                existingEntity.setAuditorsRemarks(((CaptureVendorBillMMUDetail) entity).getAuditorsRemarks());
                existingEntity.setPenaltyAmount(((CaptureVendorBillMMUDetail) entity).getPenaltyAmount());
                existingEntity.setManualPenaltyAmount(((CaptureVendorBillMMUDetail) entity).getManualPenaltyAmount());
                existingEntity.setManualPenaltyFile(((CaptureVendorBillMMUDetail) entity).getManualPenaltyFile());
                // Set other fields as necessary
                session.merge(existingEntity);
                result = existingEntity.getCaptureVendorBillMMUDetailId();
            } else {
                // Create new entity
                result = (Long) session.save(entity);
            }

            tx.commit();
            session.flush();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }

        return result;
    }
    
    @Override
    public Long createRecordForUpdate(Serializable entity) {
        Long result=1l;
        Transaction tx=null;
        Session session=null;
        try {
        
            session = getHibernateUtils.getHibernateUtlis().OpenSession();
            tx = session.beginTransaction();
            session.saveOrUpdate(entity);
            tx.commit();
            session.flush();
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            getHibernateUtils.getHibernateUtlis().CloseConnection();
        }

        return result;
    }

    @Override
    public Integer updateRecord(Serializable entity) {
        Long result=null;
        Transaction tx=null;
        Session session=null;
        try {
            session = getHibernateUtils.getHibernateUtlis().OpenSession();
            tx = session.beginTransaction();
            session.update(entity);
            tx.commit();
            session.flush();
        }catch(Exception e) {
            e.printStackTrace();
            return -1;
        }finally {
            getHibernateUtils.getHibernateUtlis().CloseConnection();
        }
        return 0;
    }

    @Override
    public Integer updateMultipleRecords(List<Serializable> entities) {
        Long result=null;
        Transaction tx=null;
        Session session=null;
        try {
            session = getHibernateUtils.getHibernateUtlis().OpenSession();
            tx = session.beginTransaction();
            int count = 0;
            for(Serializable entity: entities){
                session.saveOrUpdate(entity);
                if(++count == entities.size()){
                    session.flush();
                    session.clear();
                }
            }
            tx.commit();
        }catch(Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
            return -1;
        }finally {
            getHibernateUtils.getHibernateUtlis().CloseConnection();
        }
        return 0;
    }

    /*public Object read(Class entityClass, Serializable id) {
        Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
        Object entity = session.load(entityClass, id);
        session.flush();
        getHibernateUtils.getHibernateUtlis().CloseConnection();
        return entity;
    }*/
    
    public Object read(Class entityClass, Serializable id) {
        Session session = null;
        Transaction transaction = null;
        Object entity = null;
        try {
            session = getHibernateUtils.getHibernateUtlis().OpenSession();
            transaction = session.beginTransaction();
            entity = session.get(entityClass, id);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return entity;
    }

    @Override
    public Map<String, List<MasCamp>> getCampDetail(JSONObject jsondata) {
        Map<String, List<MasCamp>> map = new HashMap<String, List<MasCamp>>();
        List<MasCamp> campList = new ArrayList<MasCamp>();
        List totalMatches = new ArrayList<>();
        int pageNo = 0;
        int pageSize = 5;

        try {
            Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
            
            Criteria criteria = session.createCriteria(MasCamp.class);
            criteria.add(Restrictions.eq("weeklyOff", "camp").ignoreCase());
            if (jsondata.get("PN").toString() != null)
                pageNo = Integer.parseInt(jsondata.get("PN").toString());

          //  MasMMU masMMU = (MasMMU)session.load(MasMMU.class, jsondata.getLong("mmuId"));
            //Users users = masMMU.getUser();
            //MasUserType masUserType = users.getMasUserType();
            if (jsondata.has("mmuId") && !jsondata.get("mmuId").toString().trim().isEmpty()) {
                criteria.add(Restrictions.eq("mmuId", jsondata.getLong("mmuId")));
               // criteria.add(Restrictions.eq("cityId", cityMmuMappingList.get(0).getCityId()));
            }

            totalMatches = criteria.list();
            if (pageNo > 0) {
                criteria.setFirstResult((pageSize) * (pageNo - 1));
                criteria.setMaxResults(pageSize);
            }
            campList = criteria.list();
            if (jsondata.has("campDate") && !jsondata.get("campDate").toString().trim().isEmpty()) {
                String campDate = jsondata.getString("campDate");
                campList = campList.stream().filter(c -> {
                    if(c.getCampDate().toString() != null){
                        String[] campDateDB = c.getCampDate().toString().split(" ")[0].split("-");
                        String cmpDate = campDateDB[2]+"/"+campDateDB[1]+"/"+campDateDB[0];

                        return cmpDate.equals(campDate);
                    }
                    return false;
                }).collect(Collectors.toList());
            }

            map.put("campList", campList);
            map.put("totalMatches", totalMatches);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            getHibernateUtils.getHibernateUtlis().CloseConnection();
        }

        return map;
    }

    @Override
    public Map<String, List<CaptureEquipmentChecklistDetails>> getAllCapturedEquipmentChecklist(JSONObject jsondata) {
        Map<String, List<CaptureEquipmentChecklistDetails>> map = new HashMap<String, List<CaptureEquipmentChecklistDetails>>();
        List<CaptureEquipmentChecklistDetails> capturedEquipmentList = new ArrayList<CaptureEquipmentChecklistDetails>();
        List totalMatches  =new ArrayList<>();
        int pageNo=0;
        int pageSize = 5;

        try {
            Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
            Criteria criteria = session.createCriteria(CaptureEquipmentChecklistDetails.class);
            if(jsondata.get("PN").toString() !=null)
                pageNo = Integer.parseInt(jsondata.get("PN").toString());

            if (jsondata.has("cityId") && !jsondata.get("cityId").toString().trim().isEmpty()) {
                criteria.add(Restrictions.eq("cityId", jsondata.getLong("cityId")));
            }
            if (jsondata.has("mmuId") && !jsondata.get("mmuId").toString().trim().isEmpty()) {
                criteria.add(Restrictions.eq("mmuId", jsondata.getLong("mmuId")));
            }
            if (jsondata.has("auditStatus") && !jsondata.get("auditStatus").toString().trim().isEmpty()) {
                String []status = jsondata.getString("auditStatus").trim().split(",");
                criteria.add(Restrictions.in("auditStatus", status));
            }
            if (jsondata.has("validatedBy") && !jsondata.get("validatedBy").toString().trim().isEmpty()) {
                criteria.add(Restrictions.eq("validatedBy", jsondata.getLong("validatedBy")));
            }
            if (jsondata.has("inspectedBy") && !jsondata.get("inspectedBy").toString().trim().isEmpty()) {
                criteria.add(Restrictions.eq("inspectedBy", jsondata.getLong("inspectedBy")));
            }

            if (jsondata.has("fromDate")
                    && !jsondata.get("fromDate").toString().trim().isEmpty()
                    && jsondata.has("toDate")
                    && !jsondata.get("toDate").toString().trim().isEmpty()
            ) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                Date fromDate = formatter.parse(jsondata.getString("fromDate"));
                Date toDate = formatter.parse(jsondata.getString("toDate"));
                criteria.add(Restrictions.ge("inspectionDate", fromDate));
                criteria.add(Restrictions.le("inspectionDate", toDate));
            }

            if (jsondata.has("inspectionDate") && !jsondata.get("inspectionDate").toString().trim().isEmpty()) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                Date insDate = formatter.parse(jsondata.getString("inspectionDate"));
                criteria.add(Restrictions.eq("inspectionDate", insDate));
            }
            if (jsondata.has("mmuLocation") && !jsondata.get("mmuLocation").toString().trim().isEmpty()) {
                criteria.add(Restrictions.eq("mmuLocation", jsondata.getString("mmuLocation")));
            }

            criteria.addOrder(Order.asc("equipmentChecklistDetailId"));
            totalMatches = criteria.list();
            if (pageNo > 0) {
                criteria.setFirstResult((pageSize) * (pageNo - 1));
                criteria.setMaxResults(pageSize);
            }
            capturedEquipmentList = criteria.list();


            map.put("capturedEquipmentList", capturedEquipmentList);
            map.put("totalMatches", totalMatches);
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            getHibernateUtils.getHibernateUtlis().CloseConnection();
        }

        return map;
    }

    @Override
    public Map<String, List<CaptureInspectionDetails>> getAllCapturedInspectionChecklist(JSONObject jsondata) {
        Map<String, List<CaptureInspectionDetails>> map = new HashMap<String, List<CaptureInspectionDetails>>();
        List<CaptureInspectionDetails> capturedInspectionList = new ArrayList<CaptureInspectionDetails>();
        List totalMatches  =new ArrayList<>();
        int pageNo=0;
        int pageSize = 5;

        try {
            Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
            Criteria criteria = session.createCriteria(CaptureInspectionDetails.class);
            if(jsondata.get("PN").toString() !=null)
                pageNo = Integer.parseInt(jsondata.get("PN").toString());

            if (jsondata.has("cityId") && !jsondata.get("cityId").toString().trim().isEmpty()) {
                criteria.add(Restrictions.eq("cityId", jsondata.getLong("cityId")));
            }
            if (jsondata.has("mmuId") && !jsondata.get("mmuId").toString().trim().isEmpty()) {
                criteria.add(Restrictions.eq("mmuId", jsondata.getLong("mmuId")));
            }
            if (jsondata.has("auditStatus") && !jsondata.getString("auditStatus").trim().isEmpty()) {
                String []status = jsondata.getString("auditStatus").trim().split(",");
                criteria.add(Restrictions.in("auditStatus", status));
            }
            if (jsondata.has("validatedBy") && !jsondata.get("validatedBy").toString().trim().isEmpty()) {
                criteria.add(Restrictions.eq("validatedBy", jsondata.getLong("validatedBy")));
            }
            if (jsondata.has("inspectedBy") && !jsondata.get("inspectedBy").toString().trim().isEmpty()) {
                criteria.add(Restrictions.eq("inspectedBy", jsondata.getLong("inspectedBy")));
            }
            if (jsondata.has("inspectionDate") && !jsondata.get("inspectionDate").toString().trim().isEmpty()) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                Date insDate = formatter.parse(jsondata.getString("inspectionDate"));
                criteria.add(Restrictions.eq("inspectionDate", insDate));
            }
            if (jsondata.has("mmuLocation") && !jsondata.get("mmuLocation").toString().trim().isEmpty()) {
                criteria.add(Restrictions.eq("mmuLocation", jsondata.getString("mmuLocation")));
            }

            if (jsondata.has("fromDate")
                    && !jsondata.get("fromDate").toString().trim().isEmpty()
                    && jsondata.has("toDate")
                    && !jsondata.get("toDate").toString().trim().isEmpty()
            ) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                Date fromDate = formatter.parse(jsondata.getString("fromDate"));
                Date toDate = formatter.parse(jsondata.getString("toDate"));
                criteria.add(Restrictions.ge("inspectionDate", fromDate));
                criteria.add(Restrictions.le("inspectionDate", toDate));
            }

            criteria.addOrder(Order.asc("inspectionDetailId"));
            totalMatches = criteria.list();
            if (pageNo > 0) {
                criteria.setFirstResult((pageSize) * (pageNo - 1));
                criteria.setMaxResults(pageSize);
            }
            capturedInspectionList = criteria.list();


            map.put("capturedInspectionList", capturedInspectionList);
            map.put("totalMatches", totalMatches);
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            getHibernateUtils.getHibernateUtlis().CloseConnection();
        }

        return map;
    }

    public Map<String, List<CaptureInspectionChecklist>> getCapturedInspectionChecklistByDetailId(JSONObject jsondata){
        Map<String, List<CaptureInspectionChecklist>> map = new HashMap<String, List<CaptureInspectionChecklist>>();
        List<CaptureInspectionChecklist> capturedInspectionList = new ArrayList<CaptureInspectionChecklist>();
        List totalMatches  =new ArrayList<>();
        int pageNo=0;
        int pageSize = 5;

        try {
            Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
            Criteria criteria = session.createCriteria(CaptureInspectionChecklist.class).createAlias("masInspectionChecklist", "masInspectionChecklist");
            if(jsondata.get("PN").toString() !=null)
                pageNo = Integer.parseInt(jsondata.get("PN").toString());

            if (jsondata.has("inspectionDetailId") && !jsondata.get("inspectionDetailId").toString().trim().isEmpty()) {
                criteria.add(Restrictions.eq("inspectionDetailId", jsondata.getLong("inspectionDetailId")));
            }

            //criteria.addOrder(Order.asc("inspectionChecklistId"));
            criteria.addOrder(Order.asc("masInspectionChecklist.sequenceNo"));
            totalMatches = criteria.list();
            if (pageNo > 0) {
                criteria.setFirstResult((pageSize) * (pageNo - 1));
                criteria.setMaxResults(pageSize);
            }
            capturedInspectionList = criteria.list();


            map.put("capturedInspectionChecklist", capturedInspectionList);
            map.put("totalMatches", totalMatches);
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            getHibernateUtils.getHibernateUtlis().CloseConnection();
        }

        return map;
    }

    public Map<String, List<InspectionChecklistValidationHistory>> getAllInspectionChecklistValidationHistory(JSONObject jsondata){
        Map<String, List<InspectionChecklistValidationHistory>> map = new HashMap<>();
        List<InspectionChecklistValidationHistory> historyList = new ArrayList<>();
        List totalMatches  =new ArrayList<>();
        int pageNo=0;
        int pageSize = 5;

        try {
            Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
            Criteria criteria = session.createCriteria(InspectionChecklistValidationHistory.class);
            if(jsondata.get("PN").toString() !=null)
                pageNo = Integer.parseInt(jsondata.get("PN").toString());

            if (jsondata.has("inspectionDetailId") && !jsondata.get("inspectionDetailId").toString().trim().isEmpty()) {
                criteria.add(Restrictions.eq("inspectionDetailId", jsondata.getLong("inspectionDetailId")));
            }
            if (jsondata.has("captureInspectionChecklistId") && !jsondata.get("captureInspectionChecklistId").toString().trim().isEmpty()) {
                criteria.add(Restrictions.eq("captureInspectionChecklistId", jsondata.getLong("captureInspectionChecklistId")));
            }
            if (jsondata.has("inspectionChecklistId") && !jsondata.get("inspectionChecklistId").toString().trim().isEmpty()) {
                criteria.add(Restrictions.eq("inspectionChecklistId", jsondata.getLong("inspectionChecklistId")));
            }

            criteria.addOrder(Order.asc("historyId"));
            totalMatches = criteria.list();
            if (pageNo > 0) {
                criteria.setFirstResult((pageSize) * (pageNo - 1));
                criteria.setMaxResults(pageSize);
            }
            historyList = criteria.list();


            map.put("inspectionHistoryList", historyList);
            map.put("totalMatches", totalMatches);
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            getHibernateUtils.getHibernateUtlis().CloseConnection();
        }
        return map;
    }

    public Map<String, List<CaptureEquipmentChecklist>> getCapturedEquipmentChecklistByDetailId(JSONObject jsondata){
        Map<String, List<CaptureEquipmentChecklist>> map = new HashMap<>();
        List<CaptureEquipmentChecklist> capturedEquipmentChecklists = new ArrayList<>();
        List totalMatches  =new ArrayList<>();
        int pageNo=0;
        int pageSize = 5;

        try {
            Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
            Criteria criteria = session.createCriteria(CaptureEquipmentChecklist.class);
            if(jsondata.get("PN").toString() !=null)
                pageNo = Integer.parseInt(jsondata.get("PN").toString());

            if (jsondata.has("equipmentChecklistDetailId") && !jsondata.get("equipmentChecklistDetailId").toString().trim().isEmpty()) {
                criteria.add(Restrictions.eq("equipmentChecklistDetailId", jsondata.getLong("equipmentChecklistDetailId")));
            }

            criteria.addOrder(Order.asc("equipmentChecklistId"));
            totalMatches = criteria.list();
            if (pageNo > 0) {
                criteria.setFirstResult((pageSize) * (pageNo - 1));
                criteria.setMaxResults(pageSize);
            }
            capturedEquipmentChecklists = criteria.list();


            map.put("capturedEquipmentChecklists", capturedEquipmentChecklists);
            map.put("totalMatches", totalMatches);
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            getHibernateUtils.getHibernateUtlis().CloseConnection();
        }

        return map;
    }

    public Map<String, List<EquipmentChecklistValidationHistory>> getAllEquipmentChecklistValidationHistory(JSONObject jsondata){
        Map<String, List<EquipmentChecklistValidationHistory>> map = new HashMap<>();
        List<EquipmentChecklistValidationHistory> historyList = new ArrayList<>();
        List totalMatches  =new ArrayList<>();
        int pageNo=0;
        int pageSize = 5;

        try {
            Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
            Criteria criteria = session.createCriteria(EquipmentChecklistValidationHistory.class);
            if(jsondata.get("PN").toString() !=null)
                pageNo = Integer.parseInt(jsondata.get("PN").toString());

            if (jsondata.has("equipmentDetailId") && !jsondata.get("equipmentDetailId").toString().trim().isEmpty()) {
                criteria.add(Restrictions.eq("equipmentDetailId", jsondata.getLong("equipmentDetailId")));
            }
            if (jsondata.has("captureEquipmentChecklistId") && !jsondata.get("captureEquipmentChecklistId").toString().trim().isEmpty()) {
                criteria.add(Restrictions.eq("captureEquipmentChecklistId", jsondata.getLong("captureEquipmentChecklistId")));
            }
            if (jsondata.has("equipmentChecklistId") && !jsondata.get("equipmentChecklistId").toString().trim().isEmpty()) {
                criteria.add(Restrictions.eq("equipmentChecklistId", jsondata.getLong("equipmentChecklistId")));
            }

            criteria.addOrder(Order.asc("historyId"));
            totalMatches = criteria.list();
            if (pageNo > 0) {
                criteria.setFirstResult((pageSize) * (pageNo - 1));
                criteria.setMaxResults(pageSize);
            }
            historyList = criteria.list();


            map.put("equipmentHistoryList", historyList);
            map.put("totalMatches", totalMatches);
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            getHibernateUtils.getHibernateUtlis().CloseConnection();
        }
        return map;
    }

    public Map<String, List<Object[]>> getEquipmentPenaltyList(JSONObject jsondata){
        Map<String, List<Object[]>> map = new HashMap<>();
        List<Map<String, Object>> valueList = new LinkedList<>();
        List totalMatches  =new ArrayList<>();
        int pageNo=0;
        int pageSize = 5;

        try {
            List<CaptureEquipmentChecklistDetails> detailList = new ArrayList<>();
            Session session = getHibernateUtils.getHibernateUtlis().OpenSession();

            if(jsondata.get("PN").toString() !=null)
                pageNo = Integer.parseInt(jsondata.get("PN").toString());

            String hql = "Select to_char(a.inspectionDate,'dd/MM/yyyy'),c.instrumentName,(b.penaltyQuantity * d.penaltyAmount),b.penaltyQuantity,b.assignedQuantity,b.operationalQuantity,b.availableQuantity " +
                    "from CaptureEquipmentChecklistDetails a, CaptureEquipmentChecklist b, " +
                    "MasEquipmentChecklist c, MasPenalty d where a.equipmentChecklistDetailId=b.equipmentChecklistDetailId" +
                    " and b.equipmentChecklistId=c.checklistId and c.penaltyId=d.penaltyId and b.createIncident='Y'" +
                    " and a.mmuId=:mmuId";

            if (jsondata.has("inspectionYear") && !jsondata.get("inspectionYear").toString().trim().isEmpty()) {
                hql = hql+" and to_char(a.inspectionDate, 'yyyy')=:inspectionYear";
            }
            if (jsondata.has("inspectionMonth") && !jsondata.get("inspectionMonth").toString().trim().isEmpty()) {
                hql = hql+" and to_char(a.inspectionDate, 'MM')=:inspectionMonth";
            }

            Query query = session.createQuery(hql);
            query.setParameter("mmuId", jsondata.getLong("mmuId"));
            if (jsondata.has("inspectionYear") && !jsondata.get("inspectionYear").toString().trim().isEmpty()) {
                query.setParameter("inspectionYear", jsondata.getString("inspectionYear"));
            }
            if (jsondata.has("inspectionMonth") && !jsondata.get("inspectionMonth").toString().trim().isEmpty()) {
                query.setParameter("inspectionMonth", jsondata.getString("inspectionMonth"));
            }
            totalMatches = query.list();
            if (pageNo > 0) {
                query.setFirstResult((pageSize) * (pageNo - 1));
                query.setMaxResults(pageSize);
            }
            List rec = query.list();

            map.put("equipmentPenaltyList", rec);
            map.put("totalMatches", totalMatches);
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            getHibernateUtils.getHibernateUtlis().CloseConnection();
        }
        return map;
    }

    public Map<String, List<Object[]>> getInspectionPenaltyList(JSONObject jsondata){
        Map<String, List<Object[]>> map = new HashMap<>();
        List<Map<String, Object>> valueList = new LinkedList<>();
        List totalMatches  =new ArrayList<>();
        int pageNo=0;
        int pageSize = 5;

        try {
            List<CaptureEquipmentChecklistDetails> detailList = new ArrayList<>();
            Session session = getHibernateUtils.getHibernateUtlis().OpenSession();

            if(jsondata.get("PN").toString() !=null)
                pageNo = Integer.parseInt(jsondata.get("PN").toString());

            String hql = "Select to_char(a.inspectionDate,'dd/MM/yyyy'),c.checklistName,d.penaltyAmount,b.remarks " +
                    "from CaptureInspectionDetails a, CaptureInspectionChecklist b, " +
                    "MasInspectionChecklist c, MasPenalty d where a.inspectionDetailId=b.inspectionDetailId" +
                    " and b.inspectionChecklistId=c.checklistId and c.penaltyId=d.penaltyId and b.createIncident='Y'" +
                    " and a.mmuId=:mmuId";

            if (jsondata.has("inspectionYear") && !jsondata.get("inspectionYear").toString().trim().isEmpty()) {
                hql = hql+" and to_char(a.inspectionDate, 'yyyy')=:inspectionYear";
            }
            if (jsondata.has("inspectionMonth") && !jsondata.get("inspectionMonth").toString().trim().isEmpty()) {
                hql = hql+" and to_char(a.inspectionDate, 'MM')=:inspectionMonth";
            }

            Query query = session.createQuery(hql);
            query.setParameter("mmuId", jsondata.getLong("mmuId"));
            if (jsondata.has("inspectionYear") && !jsondata.get("inspectionYear").toString().trim().isEmpty()) {
                query.setParameter("inspectionYear", jsondata.getString("inspectionYear"));
            }
            if (jsondata.has("inspectionMonth") && !jsondata.get("inspectionMonth").toString().trim().isEmpty()) {
                query.setParameter("inspectionMonth", jsondata.getString("inspectionMonth"));
            }
            totalMatches = query.list();
            if (pageNo > 0) {
                query.setFirstResult((pageSize) * (pageNo - 1));
                query.setMaxResults(pageSize);
            }
            List rec = query.list();

            map.put("inspectionPenaltyList", rec);
            map.put("totalMatches", totalMatches);
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            getHibernateUtils.getHibernateUtlis().CloseConnection();
        }
        return map;
    }

    public Map<String, List<MasMmuVendor>> getVendors(JSONObject jsondata){
        Map<String, List<MasMmuVendor>> map = new HashMap<>();
        List<MasMmuVendor> masMmuVendors = new ArrayList<>();
        List totalMatches  =new ArrayList<>();
        int pageNo=0;
        int pageSize = 5;

        try {
            Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
            Criteria criteria = session.createCriteria(MasMmuVendor.class);
            if(jsondata.get("PN").toString() !=null)
                pageNo = Integer.parseInt(jsondata.get("PN").toString());

            if (jsondata.has("status") && !jsondata.getString("status").trim().isEmpty()) {
                criteria.add(Restrictions.eq("status", jsondata.getString("status")));
            }

            criteria.addOrder(Order.asc("mmuVendorId"));
            totalMatches = criteria.list();
            if (pageNo > 0) {
                criteria.setFirstResult((pageSize) * (pageNo - 1));
                criteria.setMaxResults(pageSize);
            }
            masMmuVendors = criteria.list();

            map.put("vendors", masMmuVendors);
            map.put("totalMatches", totalMatches);
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            getHibernateUtils.getHibernateUtlis().CloseConnection();
        }

        return map;
    }

    @Override
    public Map<String, List<CaptureVendorBillDetail>> getCapturedVendorBillDetail(JSONObject jsondata){
        Map<String, List<CaptureVendorBillDetail>> map = new HashMap<>();
        List<CaptureVendorBillDetail> billDetails = new ArrayList<>();
        List totalMatches  =new ArrayList<>();
        int pageNo=0;
        int pageSize = 5;
        String fromDate = "";
        String toDate = "";
        Criterion c1 = null;
        if(jsondata.has("fromDate"))
        {	
         fromDate = (String) jsondata.get("fromDate");
		 toDate = (String) jsondata.get("toDate");
        }
        try {
            Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
            Criteria criteria = session.createCriteria(CaptureVendorBillDetail.class);
            if(jsondata.get("PN").toString() !=null)
                pageNo = Integer.parseInt(jsondata.get("PN").toString());

            if (jsondata.has("captureVendorBillDetailId") && !jsondata.get("captureVendorBillDetailId").toString().trim().isEmpty()) {
                criteria.add(Restrictions.eq("captureVendorBillDetailId", jsondata.getLong("captureVendorBillDetailId")));
            }
            if (jsondata.has("vendorId") && !jsondata.get("vendorId").toString().trim().isEmpty()) {
                criteria.add(Restrictions.eq("vendorId", jsondata.getLong("vendorId")));
            }
            if (jsondata.has("cityId") && !jsondata.get("cityId").toString().trim().isEmpty()) {
            	long cityId = jsondata.getLong("cityId");
                criteria.add(Restrictions.eq("cityId", cityId));
                //criteria.add(Restrictions.eq("status", "C"));
            }
            if (jsondata.has("district") && !jsondata.get("district").toString().trim().isEmpty()) {
                criteria.add(Restrictions.eq("districtId", jsondata.getLong("district")));
            }
            if (jsondata.has("statusSearch") && !jsondata.get("statusSearch").toString().trim().isEmpty()) {
                criteria.add(Restrictions.eq("paymentStatus", jsondata.getString("statusSearch")));
            }
            if (jsondata.has("invoiceNo") && !jsondata.get("invoiceNo").toString().trim().isEmpty()) {
                criteria.add(Restrictions.ilike("invoiceNo", jsondata.getString("invoiceNo"), MatchMode.ANYWHERE));
            }
            if (jsondata.has("billMonth") && !jsondata.get("billMonth").toString().trim().isEmpty()) {
                criteria.add(Restrictions.eq("billMonth", jsondata.getInt("billMonth")));
            }
            if (jsondata.has("billYear") && !jsondata.get("billYear").toString().trim().isEmpty()) {
                criteria.add(Restrictions.eq("billYear", jsondata.getInt("billYear")));
            }
           if (jsondata.has("type") && !jsondata.get("type").toString().trim().isEmpty() && jsondata.get("type").equals("C")) {
        	   criteria.add(Restrictions.or(Restrictions.eq("nextAuthorityId", Long.parseLong(jsondata.getString("authorityId"))), Restrictions.eq("vendorStatus", jsondata.getString("type"))));
        	   criteria.add(Restrictions.eq("paymentStatus", "P")); 
           }
            if (jsondata.has("creatdBy") && !jsondata.get("creatdBy").toString().trim().isEmpty()&&!jsondata.has("vendorId")) {
                criteria.add(Restrictions.eq("createdBy", jsondata.getLong("creatdBy")));
            }
            if (jsondata.has("status") && !jsondata.get("status").toString().trim().isEmpty()) {
                criteria.add(Restrictions.eq("status", "C"));
            }
            if (jsondata.has("paymentstatus") && !jsondata.get("paymentstatus").toString().trim().isEmpty()) {
                criteria.add(Restrictions.eq("status", "C"));
                criteria.add(Restrictions.eq("paymentStatus", "P"));
            }
            if (jsondata.has("authorityId")  &&!jsondata.get("type").equals("C")) {
            	if(!jsondata.get("authorityId").toString().trim().isEmpty())
            	{	
                 
                    criteria.add(Restrictions.and(Restrictions.eq("nextAuthorityId", Long.parseLong(jsondata.getString("authorityId"))), Restrictions.isNull("status")));
                    Criterion c2= null;
                    Criterion c3= null;
                    if(!jsondata.get("cityIdVal").equals(""))
                    {	
                    c2=Restrictions.and(Restrictions.eq("cityId", jsondata.getLong("cityIdVal")));
                    }
                    else {
                    	c2=Restrictions.and(Restrictions.eq("districtId", jsondata.getLong("distIdVal")));
                    }
                   criteria.add(c2);
            	}
            }
            if (fromDate != null && !fromDate.equals("") && !fromDate.equals("null") &&
					toDate != null && !toDate.equals("") && !toDate.equals("null")) {
            	criteria.add(Restrictions.ge("invoiceDate", formatDate(fromDate)));
            	criteria.add(Restrictions.le("invoiceDate", formatDate(toDate)));
			}
           
            criteria.addOrder(Order.desc("captureVendorBillDetailId"));
            totalMatches = criteria.list();
            if (pageNo > 0) {
                criteria.setFirstResult((pageSize) * (pageNo - 1));
                criteria.setMaxResults(pageSize);
            }
            billDetails = criteria.list();


            map.put("capturedVendorBillDetails", billDetails);
            map.put("totalMatches", totalMatches);
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            getHibernateUtils.getHibernateUtlis().CloseConnection();
        }
        return map;
    }
    
    @Override
    public Map<String, List<CaptureVendorBillDetail>> getCapturedVendorBillNodalDetail(JSONObject jsondata){
        Map<String, List<CaptureVendorBillDetail>> map = new HashMap<>();
        List<CaptureVendorBillDetail> billDetails = new ArrayList<>();
        List totalMatches  =new ArrayList<>();
        int pageNo=0;
        int pageSize = 5;
        String fromDate = "";
        String toDate = "";
        Criterion c1 = null;
        if(jsondata.has("fromDate"))
        {	
         fromDate = (String) jsondata.get("fromDate");
		 toDate = (String) jsondata.get("toDate");
        }
        try {
            Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
            Criteria criteria = session.createCriteria(CaptureVendorBillDetail.class);
            if(jsondata.get("PN").toString() !=null)
                pageNo = Integer.parseInt(jsondata.get("PN").toString());

            
            if (jsondata.has("cityId") && !jsondata.get("cityId").toString().trim().isEmpty()&&jsondata.get("type").equals("NodalWaitingData")) {
            	long cityId = jsondata.getLong("cityId");
                criteria.add(Restrictions.eq("cityId", cityId));
                criteria.add(Restrictions.eq("status", "C"));
                //criteria.add(Restrictions.eq("paymentStatus", "P"));
                criteria.add(Restrictions.or(Restrictions.eq("paymentStatus", "P"), Restrictions.eq("paymentStatus", "C")));
            }else {
            	long cityId = jsondata.getLong("cityId");
                criteria.add(Restrictions.eq("cityId", cityId));
                criteria.add(Restrictions.eq("status", "C"));
                criteria.add(Restrictions.eq("paymentStatus", "C"));
            }
            
            if (jsondata.has("paymentstatus") && !jsondata.get("paymentstatus").toString().trim().isEmpty()) {
                criteria.add(Restrictions.eq("status", "C"));
                criteria.add(Restrictions.eq("paymentStatus", "P"));
            }
           
            if (fromDate != null && !fromDate.equals("") && !fromDate.equals("null") &&
					toDate != null && !toDate.equals("") && !toDate.equals("null")) {
            	criteria.add(Restrictions.ge("invoiceDate", formatDate(fromDate)));
            	criteria.add(Restrictions.le("invoiceDate", formatDate(toDate)));
			}
           
            criteria.addOrder(Order.desc("captureVendorBillDetailId"));
            totalMatches = criteria.list();
            if (pageNo > 0) {
                criteria.setFirstResult((pageSize) * (pageNo - 1));
                criteria.setMaxResults(pageSize);
            }
            billDetails = criteria.list();


            map.put("capturedVendorBillDetails", billDetails);
            map.put("totalMatches", totalMatches);
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            getHibernateUtils.getHibernateUtlis().CloseConnection();
        }
        return map;
    }

    public Map<String, Map<MasCity, List<MasMMU>>> getVendorsMMUAndCity(JSONObject jsondata){
        Map<String, Map<MasCity, List<MasMMU>>> map = new HashMap<>();
        int pageNo=0;
        int pageSize = 5;

        try {
            Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
            Criteria criteria = session.createCriteria(MasMMU.class);
            if(jsondata.get("PN").toString() !=null)
                pageNo = Integer.parseInt(jsondata.get("PN").toString());

            if (jsondata.has("mmuVendorId") && !jsondata.get("mmuVendorId").toString().trim().isEmpty()) {
                criteria.add(Restrictions.eq("mmuVendorId", jsondata.getLong("mmuVendorId")));
            }

            criteria.addOrder(Order.asc("mmuId"));
            if (pageNo > 0) {
                criteria.setFirstResult((pageSize) * (pageNo - 1));
                criteria.setMaxResults(pageSize);
            }
            List<MasMMU> masMMUS = criteria.list();

            List<MasCity> cities = masMMUS
                    .stream()
                    .map(MasMMU::getCityId)
                    .distinct()
                    .map(id -> (MasCity)session.load(MasCity.class, id))
                    .collect(Collectors.toList());

            Map<MasCity, List<MasMMU>> data = new HashMap<>();
            cities.forEach(c -> {
                data.put(c, masMMUS.stream().filter(m -> m.getCityId() == c.getCityId()).collect(Collectors.toList()));
            });

            map.put("vendorsMMUS", data);
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            getHibernateUtils.getHibernateUtlis().CloseConnection();
        }
        return map;
    }

    public Map<String, List<Map<String, Object>>> getVendorsPenalty(JSONObject jsondata){
        Map<String, List<Map<String, Object>>> map = new HashMap<>();

        try {
            List<CaptureEquipmentChecklistDetails> detailList = new ArrayList<>();
            Session session = getHibernateUtils.getHibernateUtlis().OpenSession();

            String hqlIns = "Select to_char(b.incidentDate,'dd/MM/yyyy'),c.checklistName,d.penaltyAmount,a.mmuId,e.mmuName,b.inputValue " +
                    "from CaptureInspectionDetails a, CaptureInspectionChecklist b, " +
                    "MasInspectionChecklist c, MasPenalty d, MasMMU e where a.inspectionDetailId=b.inspectionDetailId" +
                    " and b.inspectionChecklistId=c.checklistId and c.penaltyId=d.penaltyId and a.mmuId=e.mmuId and b.createIncident='Y'" +
                    " and a.mmuId in(:mmuIds)";

            String hqlEquip = "Select to_char(b.incidentDate,'dd/MM/yyyy'),c.instrumentName,(b.penaltyQuantity * d.penaltyAmount),b.penaltyQuantity,a.mmuId,e.mmuName,b.assignedQuantity,b.availableQuantity,b.operationalQuantity " +
                    "from CaptureEquipmentChecklistDetails a, CaptureEquipmentChecklist b, " +
                    "MasEquipmentChecklist c, MasPenalty d, MasMMU e where a.equipmentChecklistDetailId=b.equipmentChecklistDetailId" +
                    " and b.equipmentChecklistId=c.checklistId and c.penaltyId=d.penaltyId and a.mmuId=e.mmuId and b.createIncident='Y'" +
                    " and a.mmuId in(:mmuIds)";

            String hqlAtten = "Select to_char(a.attendanceDate,'dd/MM/yyyy'),b.userName,c.userTypeName,d.mmuName,d.mmuId " +
                    "from AuditAttendanceData a, Users b, MasUserType c, MasMMU d" +
                    " where a.userId=b.userId and b.userTypeId=c.userTypeId and a.mmuId.mmuId=d.mmuId and a.status='A'" +
                    " and a.mmuId.mmuId in(:mmuIds)";

            if (jsondata.has("inspectionYear") && !jsondata.get("inspectionYear").toString().trim().isEmpty()) {
                hqlIns = hqlIns+" and year(a.inspectionDate)=:inspectionYear";
                hqlEquip = hqlEquip+" and year(a.inspectionDate)=:inspectionYear";
                hqlAtten = hqlAtten+" and a.attendanceYear=:inspectionYear";
            }
            if (jsondata.has("inspectionMonth") && !jsondata.get("inspectionMonth").toString().trim().isEmpty()) {
                hqlIns = hqlIns+" and month(a.inspectionDate)=:inspectionMonth";
                hqlEquip = hqlEquip+" and month(a.inspectionDate)=:inspectionMonth";
                hqlAtten = hqlAtten+" and a.attendanceMonth=:inspectionMonth";
            }

            Query queryIns = session.createQuery(hqlIns);
            Query queryEquip = session.createQuery(hqlEquip);
            Query queryAtten = session.createQuery(hqlAtten);
            List<Long> mmuids = Arrays
                    .stream(jsondata.getString("mmuIds").split(","))
                    .map(s -> Long.parseLong(s))
                    .collect(Collectors.toList());

            queryIns.setParameterList("mmuIds", mmuids);
            queryEquip.setParameterList("mmuIds", mmuids);
            queryAtten.setParameterList("mmuIds", mmuids);
            if (jsondata.has("inspectionYear") && !jsondata.get("inspectionYear").toString().trim().isEmpty()) {
                queryIns.setParameter("inspectionYear", jsondata.getInt("inspectionYear"));
                queryEquip.setParameter("inspectionYear", jsondata.getInt("inspectionYear"));
                queryAtten.setParameter("inspectionYear", jsondata.getInt("inspectionYear"));
            }
            if (jsondata.has("inspectionMonth") && !jsondata.get("inspectionMonth").toString().trim().isEmpty()) {
                queryIns.setParameter("inspectionMonth", jsondata.getInt("inspectionMonth")+1);
                queryEquip.setParameter("inspectionMonth", jsondata.getInt("inspectionMonth")+1);
                queryAtten.setParameter("inspectionMonth", jsondata.getInt("inspectionMonth")+1);
            }

            List<Object[]> inspectionList = queryIns.list();
            List<Object[]> equipmentList = queryEquip.list();
            List<Object[]> attendanceList = queryAtten.list();
            List<Map<String, Object>> result = new ArrayList();
            inspectionList.stream().forEach(a -> {
                Map<String, Object> record = new HashMap<>();
                record.put("mmuId", a[3]);
                record.put("mmuName", a[4]);
                record.put("incidentDate", a[0]);
                record.put("incidentDescription", a[1]+"- \n"+a[5]);
                record.put("penaltyAmount", a[2]);
                record.put("penaltyType", "INSPECTION");
                result.add(record);
            });

            equipmentList.stream().forEach(a -> {
                int assigned_quantity = Integer.parseInt(a[6].toString());
                int available_quantity = Integer.parseInt(a[7].toString());
                int operational_quantity = Integer.parseInt(a[8].toString());
                String incidentDescription = a[1].toString();
                if(available_quantity < assigned_quantity)
                    incidentDescription += "- "+(assigned_quantity - available_quantity)+" quantity is Not Available";

                if(operational_quantity < assigned_quantity)
                    incidentDescription += "- "+(assigned_quantity - operational_quantity)+" quantity is Not Operational";
                
                Map<String, Object> record = new HashMap<>();
                record.put("mmuId", a[4]);
                record.put("mmuName", a[5]);
                record.put("incidentDate", a[0]);
                record.put("incidentDescription", incidentDescription);
                record.put("penaltyAmount", a[2]);
                record.put("penaltyType", "EQUIPMENT");
                result.add(record);
            });

            attendanceList.stream().forEach(a -> {
                Map<String, Object> record = new HashMap<>();
                String amount = HMSUtil.getProperties("adt.properties", "penaltyForOther").trim();
                if (a[2] != null && a[2].toString().equalsIgnoreCase("doctor"))
                    amount = HMSUtil.getProperties("adt.properties", "penaltyForDoctor").trim();
                record.put("mmuId", a[4]);
                record.put("mmuName", a[3]);
                record.put("incidentDate", a[0]);
                record.put("incidentDescription", a[2] + "- "+a[1] + " was Absent");
                record.put("penaltyAmount", Double.parseDouble(amount));
                record.put("penaltyType", "ATTENDANCE");
                result.add(record);
            });
            map.put("vendorInsEquipPenaltyList", result);
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            getHibernateUtils.getHibernateUtlis().CloseConnection();
        }
        return map;
    }

    public Map<Long, CaptureVendorBillMMUDetail> getCapturedMMUDetails(List<Long> ids){
        Map<Long, CaptureVendorBillMMUDetail> equipMap = new HashMap<>();
        if (ids != null && !ids.isEmpty()) {
            String hql = "Select c from CaptureVendorBillMMUDetail c where c.captureVendorBillDetailId in(:ids)";
            Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
            Query query = session.createQuery(hql);
            query.setParameterList("ids", ids);
            List<CaptureVendorBillMMUDetail> li = query.list();
            equipMap = li.stream().collect(Collectors.toMap(CaptureVendorBillMMUDetail::getCaptureVendorBillMMUDetailId, Function.identity()));
        }
        return equipMap;
    }
    
    public Map<Long, VendorInvoicSupportingDocs> getVendorSupportingDocs(List<Long> ids){
        Map<Long, VendorInvoicSupportingDocs> equipMap = new HashMap<>();
        if (ids != null && !ids.isEmpty()) {
            String hql = "SELECT doc FROM VendorInvoicSupportingDocs doc WHERE doc.captureVendorBillDetailId IN (:ids)";
            Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
            Query query = session.createQuery(hql);
            query.setParameterList("ids", ids);
            List<VendorInvoicSupportingDocs> li = query.list();
            equipMap = li.stream().collect(Collectors.toMap(VendorInvoicSupportingDocs::getCaptureVendorSupportingDocsId, Function.identity()));
        }
        return equipMap;
    }
    

    public Map<Long, MasEquipmentChecklist> getEquipmentMaster(List<Long> ids){
        Map<Long, MasEquipmentChecklist> equipMap = new HashMap<>();
        if (ids != null && !ids.isEmpty()) {
            String hql = "Select c from MasEquipmentChecklist c where c.checklistId in(:ids)";
            Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
            Query query = session.createQuery(hql);
            query.setParameterList("ids", ids);
            List<MasEquipmentChecklist> li = query.list();
            equipMap = li.stream().collect(Collectors.toMap(MasEquipmentChecklist::getChecklistId, Function.identity()));
        }
        return equipMap;
    }

    public Map<Long, MasInspectionChecklist> getInspectionMaster(List<Long> ids){
        Map<Long, MasInspectionChecklist> insMap = new HashMap<>();
        if (ids != null && !ids.isEmpty()) {
            String hql = "Select c from MasInspectionChecklist c where c.checklistId in(:ids)";
            Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
            Query query = session.createQuery(hql);
            query.setParameterList("ids", ids);
            List<MasInspectionChecklist> li = query.list();
            insMap = li.stream().collect(Collectors.toMap(MasInspectionChecklist::getChecklistId, Function.identity()));
        }
        return insMap;
    }

    @Override
    public String[] getAPMDoctor(Long campId, Long mmuId){
        int pageNo=1;
        int pageSize = 1;
        String apmDoctorDetail[] = new String[3];
        try {
            Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
            Criteria criteria = session.createCriteria(Visit.class);

            if (campId != null) {
                criteria.add(Restrictions.eq("campId", campId));
            }
            if (pageNo > 0) {
                criteria.setFirstResult((pageSize) * (pageNo - 1));
                criteria.setMaxResults(pageSize);
            }
            List<Visit> list = criteria.list();
            if(list != null && list.size() > 0) {
                Visit visit = list.get(0);
                if (visit.getDoctorId() != null) {
                    Users users = (Users) session.load(Users.class, visit.getDoctorId());
                    if (users.getEmployeeId() != null) {
                        EmployeeRegistration employeeRegistration = (EmployeeRegistration) session.load(EmployeeRegistration.class, users.getEmployeeId().getEmployeeId());
                        apmDoctorDetail[0] = users.getUserName();
                        apmDoctorDetail[1] = employeeRegistration.getRegNo();
                    }
                }
            }

            Criteria criteriaRole = session.createCriteria(MasRole.class);
            criteriaRole.add(Restrictions.eq("roleName", "APM"));
            List<MasRole> roleLi = criteriaRole.list();
            if(roleLi != null && !roleLi.isEmpty()){
                MasRole masRole = roleLi.get(0);
                Criteria criteriaUsers = session.createCriteria(Users.class);
                criteriaUsers.add(Restrictions.like("roleId", Long.toString(masRole.getRoleId()), MatchMode.ANYWHERE));
                criteriaUsers.add(Restrictions.like("mmuId", Long.toString(mmuId), MatchMode.ANYWHERE));
                List<Users> usersList = criteriaUsers.list();
                if (usersList != null && !usersList.isEmpty()){
                    Users apmUser = usersList.get(0);
                    apmDoctorDetail[2] = apmUser.getUserName();
                }
            }

        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            getHibernateUtils.getHibernateUtlis().CloseConnection();
        }
        return apmDoctorDetail;
    }
    
    private Date formatDate(String inDate) {

		String[] dateString = inDate.split("/");
		ZoneId defaultZoneId = ZoneId.systemDefault();
		LocalDate localDate = LocalDate.of(Integer.parseInt(dateString[2]), Integer.parseInt(dateString[1]),
				Integer.parseInt(dateString[0]));
		Date date = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
		return date;
	}
    
    
    @Override
	public String updateCaptureVendorBillDetail(HashMap<String, Object> jsonObject) {
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Transaction t = session.beginTransaction();
			String captureVendorBillDetailId = jsonObject.get("captureVendorBillDetailId").toString();
			String lastApprovalMsg = jsonObject.get("lastApprovalMsg").toString();
			String auditorAuthorityId = jsonObject.get("auditorAuthorityId").toString();
			Long forwordTo = null;
			if (jsonObject.get("forwordTo") != null) {
				forwordTo = Long.parseLong(jsonObject.get("forwordTo").toString());
			}
			String action = jsonObject.get("action").toString();
			if (!action.equals("A")) {
				action = "S";
			}
			if (captureVendorBillDetailId != null && lastApprovalMsg.startsWith("Acknowledged")) {
				String qryStringHd = "update CaptureVendorBillDetail u set u.lastApprovalStatus='" + lastApprovalMsg
						+ "', u.currentAuthorityId='" + auditorAuthorityId + "',u.nextAuthorityId=" + forwordTo
						+ ",u.vendorStatus='" + action + "' where u.captureVendorBillDetailId='"
						+ captureVendorBillDetailId + "' ";
				Query queryHd = session.createQuery(qryStringHd);
				int countHd = queryHd.executeUpdate();
				System.out.println(countHd + "Users Records MMU Updated.");
				t.commit();
				session.flush();

			}else {

				String auditorFileName = jsonObject.get("auditUploadedFileName").toString();
				String auditorRemarks = jsonObject.get("auditorRemarks").toString();
				Long penaltyAmountImposed = null;
				if (jsonObject.get("penaltyAmountImposed") != null) {
					penaltyAmountImposed = Long.parseLong(jsonObject.get("penaltyAmountImposed").toString());
				} else {
					penaltyAmountImposed = 0l;
				}

				if (captureVendorBillDetailId != null) {
					String qryStringHd = "update CaptureVendorBillDetail u set u.lastApprovalStatus='" + lastApprovalMsg
							+ "',u.auditorFileName='" + auditorFileName + "',u.finalAuditorsRemarks='" + auditorRemarks
							+ "', u.currentAuthorityId='" + auditorAuthorityId + "',u.nextAuthorityId=" + forwordTo
							+ ",u.vendorStatus='" + action + "',u.initialPenaltyAmount='" + penaltyAmountImposed
							+ "' where u.captureVendorBillDetailId='" + captureVendorBillDetailId + "' ";
					Query queryHd = session.createQuery(qryStringHd);
					int countHd = queryHd.executeUpdate();
					System.out.println(countHd + "Users Records MMU Updated.");
					t.commit();
					session.flush();
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			return "500";
			// TODO: handle exception
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return "statusUpdated";
	}
    
    @Override
    public Map<String, List<VendorInvoiceApproval>> getVendorInvoiceApprovalDetail(JSONObject jsondata){
        Map<String, List<VendorInvoiceApproval>> map = new HashMap<>();
        List<VendorInvoiceApproval> billDetails = new ArrayList<>();
        List totalMatches  =new ArrayList<>();
       
        try {
            Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
            Criteria criteria = session.createCriteria(VendorInvoiceApproval.class);
           
            if (jsondata.has("captureVendorBillDetailId") && !jsondata.get("captureVendorBillDetailId").toString().trim().isEmpty()) {
                criteria.add(Restrictions.eq("captureVendorBillDetailId", jsondata.getLong("captureVendorBillDetailId")));
            }
            
            billDetails = criteria.list();
            criteria.addOrder(Order.asc("vendorInvoiceApprovalId"));
            totalMatches = criteria.list();
            
            billDetails = criteria.list();

            map.put("capturedVendorBillDetails", billDetails);
            map.put("totalMatches", totalMatches);
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            getHibernateUtils.getHibernateUtlis().CloseConnection();
        }
        return map;
    }

	@Override
	public String saveOrUpdateAuthorityVendorBillDetails(HashMap<String, Object> jsondata) {
		 Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		 Transaction t = session.beginTransaction();
		 //String authType=jsondata.get("authorityRoleName").toString();
		 String finalApproval=jsondata.get("finalApproval").toString();
		 String authAction=jsondata.get("actionId").toString();
		 String lastApprovalMsg=jsondata.get("lastApprovalMsg").toString();
		 String[] forward = null;
		 try {
		if(authAction.equals("A"))
		{	
				 if(jsondata.get("authorityId")!=null && !jsondata.get("authorityId").equals(""))
		         {
					 VendorInvoiceApproval via=new VendorInvoiceApproval();
			         via.setCaptureVendorBillDetailId(Long.parseLong(jsondata.get("captureVendorBillDetailId").toString()));
			         via.setAuthorityId(Long.parseLong(jsondata.get("authorityId").toString()));
			         if(null!=jsondata.get("actionDate")) {
		                    String actionDate=jsondata.get("actionDate").toString();
		                    Date dd1 = HMSUtil.convertStringDateToUtilDateForDatabase(actionDate);
		                    via.setAuthorityDate(dd1);
	                    }
			         //via.setAuthorityDate(new Date());
			         via.setAuthorityName(jsondata.get("authorityName").toString());
			         via.setAuthorityRole(jsondata.get("authorityRoleName").toString());
			         via.setAuthorityAction(jsondata.get("actionId").toString());
			         via.setAuthorityRemarks(jsondata.get("remarks").toString());
			         via.setOrderNo(Long.parseLong(jsondata.get("authorityOrderNo").toString()));
			         via.setUserId(Long.parseLong(jsondata.get("userId").toString()));
			         if(!finalApproval.equals("y"))
			         {	 
				         String str=jsondata.get("forwordTo").toString();
				         if(str!=null && str!="")
				         {
				         forward=str.split("@@");
				         via.setForwardAuthorityId(Long.parseLong(forward[0]));
				         via.setForwardOrderNo(Long.parseLong(forward[1]));
			          }
			         }
			         else
			         {
			        	 via.setForwardAuthorityId(Long.parseLong(jsondata.get("authorityId").toString()));
			        	 via.setForwardOrderNo(Long.parseLong(jsondata.get("authorityOrderNo").toString()));
			         }
			         session.save(via);
		            }
					 
						String captureVendorBillDetailId=jsondata.get("captureVendorBillDetailId").toString();
						String currentAuthId=jsondata.get("authorityId").toString();
						Double penaltyAmountImposed=null;
						if(jsondata.get("penaltyAmountImposedAuth")!=null && !jsondata.get("penaltyAmountImposedAuth").equals(""))
						{	
							penaltyAmountImposed=Double.parseDouble(jsondata.get("penaltyAmountImposedAuth").toString());
						}
						else
						{
							penaltyAmountImposed=(double) 0;
						}
						if(captureVendorBillDetailId!=null && !finalApproval.equals("y"))
						{	
						    String forwordTo=forward[0];
						    String penaltyAmountAuditor=jsondata.get("penaltyAmountImposed").toString();
							String qryStringHd  ="update CaptureVendorBillDetail u set u.lastApprovalStatus='"+lastApprovalMsg+"',u.currentAuthorityId='"+currentAuthId+"',u.nextAuthorityId="+forwordTo+",u.initialPenaltyAmount='"+penaltyAmountImposed+"',u.auditorPenaltyAmount='"+penaltyAmountAuditor+"'  where u.captureVendorBillDetailId='"+captureVendorBillDetailId+"' " ;
							Query queryHd = session.createQuery(qryStringHd);
					        int countHd = queryHd.executeUpdate();
							System.out.println(countHd + "Records CaptureVendorBillDetail Updated.");
							t.commit();
						}
						else
						{
							String penaltyAmount=jsondata.get("penaltyAmount").toString();
							String penaltyAmountAuditor=jsondata.get("penaltyAmountImposed").toString();
							String qryStringHd  ="update CaptureVendorBillDetail u set u.lastApprovalStatus='"+lastApprovalMsg+"',u.penaltyAmount='"+penaltyAmount+"',u.auditorPenaltyAmount='"+penaltyAmountAuditor+"',u.status='C',u.currentAuthorityId='"+currentAuthId+"',u.nextAuthorityId="+currentAuthId+"  where u.captureVendorBillDetailId='"+captureVendorBillDetailId+"' " ;
							Query queryHd = session.createQuery(qryStringHd);
					        int countHd = queryHd.executeUpdate();
							System.out.println(countHd + "Records CaptureVendorBillDetail Updated.");
							t.commit();	
						}
		  }
		else if(authAction.equals("D"))
		{
			 Map<String, Object> nextAuthorityDetails=getAuthorityAndOrderNo(Long.parseLong(jsondata.get("authorityId").toString()),Long.parseLong(jsondata.get("captureVendorBillDetailId").toString()));
			 Long nextAuthId = null;
			 Long nextOrderNo = null;
			 Long mobileNo = null;
			 String userName = null;
			 List<Object[]> listCamp = (List<Object[]>) nextAuthorityDetails.get("list");
				for (Object[] row : listCamp) {
					if (row[0]!= null) {
						nextAuthId=Long.parseLong(row[0].toString());
					
						
					}
					if (row[1]!= null) {
						nextOrderNo=Long.parseLong(row[1].toString());
						
					}
					if (row[2]!= null) {
						mobileNo=Long.parseLong(row[2].toString());
						
					}
					if (row[3]!= null) {
						userName=row[3].toString();
						
					}
				 }
				
				 if(jsondata.get("authorityId")!=null && !jsondata.get("authorityId").equals(""))
		         {
					 VendorInvoiceApproval via=new VendorInvoiceApproval();
			         via.setCaptureVendorBillDetailId(Long.parseLong(jsondata.get("captureVendorBillDetailId").toString()));
			         via.setAuthorityId(Long.parseLong(jsondata.get("authorityId").toString()));
			         //via.setAuthorityDate(new Date());
			         if(null!=jsondata.get("actionDate")) {
		                    String actionDate=jsondata.get("actionDate").toString();
		                    Date dd1 = HMSUtil.convertStringDateToUtilDateForDatabase(actionDate);
		                    via.setAuthorityDate(dd1);
	                    }
			         via.setAuthorityName(jsondata.get("authorityName").toString());
			         via.setAuthorityRole(jsondata.get("authorityRoleName").toString());
			         via.setAuthorityAction(jsondata.get("actionId").toString());
			         via.setAuthorityRemarks(jsondata.get("remarks").toString());
			         via.setOrderNo(Long.parseLong(jsondata.get("authorityOrderNo").toString()));
			         via.setForwardAuthorityId(nextAuthId);
			         via.setForwardOrderNo(nextOrderNo);
			         via.setUserId(Long.parseLong(jsondata.get("userId").toString()));
			         session.save(via);
			         //t.commit();	
		         }
				 String captureVendorBillDetailId=jsondata.get("captureVendorBillDetailId").toString();
					Long currentAuthId=Long.parseLong(jsondata.get("authorityId").toString());
					if(captureVendorBillDetailId!=null && !finalApproval.equals("y"))
					{	
					    
						String qryStringHd  ="update CaptureVendorBillDetail u set u.lastApprovalStatus='"+lastApprovalMsg+"',u.currentAuthorityId='"+currentAuthId+"',u.nextAuthorityId="+nextAuthId+"  where u.captureVendorBillDetailId='"+captureVendorBillDetailId+"' " ;
						Query queryHd = session.createQuery(qryStringHd);
				        int countHd = queryHd.executeUpdate();
						System.out.println(countHd + "Records CaptureVendorBillDetail Updated.");
						t.commit();
					}
					else
					{
						
						String penaltyAmount=jsondata.get("penaltyAmount").toString();
						String penaltyAmountAuditor=jsondata.get("penaltyAmountImposed").toString();
						String qryStringHd  ="update CaptureVendorBillDetail u set u.lastApprovalStatus='"+lastApprovalMsg+"',u.penaltyAmount='"+penaltyAmount+"',u.auditorPenaltyAmount='"+penaltyAmountAuditor+"',u.currentAuthorityId='"+currentAuthId+"',u.nextAuthorityId="+nextAuthId+" where u.captureVendorBillDetailId='"+captureVendorBillDetailId+"' " ;
						Query queryHd = session.createQuery(qryStringHd);
				        int countHd = queryHd.executeUpdate();
						System.out.println(countHd + "Records CaptureVendorBillDetail Updated.");
						t.commit();	
					}	
				 try {
						final String uri ="https://2factor.in/API/R1/?module=TRANS_SMS&apikey=5cdc6365-22b5-11ec-a13b-0200cd936042&to="+mobileNo+
								"&from=CGMMSY&templatename=Username-New&var1="+userName+"&var2="+mobileNo+"&var3="+"for discussion";

						MultiValueMap<String, String> requestHeaders = new LinkedMultiValueMap<String, String>();
						RestTemplate restTemplate = new RestTemplate();
						String responseObject = restTemplate.postForObject(uri, requestHeaders, String.class);

						System.out.println(responseObject.toString());
						System.out.println("SMS send succefully");
						//return responseObject;
					} catch (Exception e) {

						return ProjectUtils.getReturnMsg("0", "We are unable to process your request").toString();
					}
	  	 }
		 else
		 {
			 Map<String, Object> nextAuthorityDetails=getAuthorityAndOrderNo(Long.parseLong(jsondata.get("authorityId").toString()),Long.parseLong(jsondata.get("captureVendorBillDetailId").toString()));
			 Long nextAuthId = null;
			 Long nextOrderNo = null;
			 Long mobileNo = null;
			 String userName = null;
			 List<Object[]> listCamp = (List<Object[]>) nextAuthorityDetails.get("list");
				for (Object[] row : listCamp) {
					if (row[0]!= null) {
						nextAuthId=Long.parseLong(row[0].toString());
					
						
					}
					if (row[1]!= null) {
						nextOrderNo=Long.parseLong(row[1].toString());
						
					}
					if (row[2]!= null) {
						mobileNo=Long.parseLong(row[2].toString());
						
					}
					if (row[3]!= null) {
						userName=row[3].toString();
						
					}
				 }
				
							
			 if(jsondata.get("authorityId")!=null && !jsondata.get("authorityId").equals(""))
	         {
				 VendorInvoiceApproval via=new VendorInvoiceApproval();
		         via.setCaptureVendorBillDetailId(Long.parseLong(jsondata.get("captureVendorBillDetailId").toString()));
		         via.setAuthorityId(Long.parseLong(jsondata.get("authorityId").toString()));
		         //via.setAuthorityDate(new Date());
		         if(null!=jsondata.get("actionDate")) {
	                    String actionDate=jsondata.get("actionDate").toString();
	                    Date dd1 = HMSUtil.convertStringDateToUtilDateForDatabase(actionDate);
	                    via.setAuthorityDate(dd1);
                 }
		         via.setAuthorityName(jsondata.get("authorityName").toString());
		         via.setAuthorityRole(jsondata.get("authorityRoleName").toString());
		         via.setAuthorityAction(jsondata.get("actionId").toString());
		         via.setAuthorityRemarks(jsondata.get("remarks").toString());
		         via.setOrderNo(Long.parseLong(jsondata.get("authorityOrderNo").toString()));
		         via.setForwardAuthorityId(nextAuthId);
		         via.setForwardOrderNo(nextOrderNo);
		         via.setUserId(Long.parseLong(jsondata.get("userId").toString()));
		         session.save(via);
	            }
				 
					String captureVendorBillDetailId=jsondata.get("captureVendorBillDetailId").toString();
					Long currentAuthId=Long.parseLong(jsondata.get("authorityId").toString());
					if(captureVendorBillDetailId!=null && !finalApproval.equals("y"))
					{	
					    
						String qryStringHd  ="update CaptureVendorBillDetail u set u.lastApprovalStatus='"+lastApprovalMsg+"',u.currentAuthorityId='"+currentAuthId+"',u.nextAuthorityId="+nextAuthId+"  where u.captureVendorBillDetailId='"+captureVendorBillDetailId+"' " ;
						Query queryHd = session.createQuery(qryStringHd);
				        int countHd = queryHd.executeUpdate();
						System.out.println(countHd + "Records CaptureVendorBillDetail Updated.");
						t.commit();
					}
					else
					{
						
						String qryStringHd  ="update CaptureVendorBillDetail u set u.lastApprovalStatus='"+lastApprovalMsg+"',u.currentAuthorityId='"+currentAuthId+"',u.nextAuthorityId="+nextAuthId+"  where u.captureVendorBillDetailId='"+captureVendorBillDetailId+"' " ;
						Query queryHd = session.createQuery(qryStringHd);
				        int countHd = queryHd.executeUpdate();
						System.out.println(countHd + "Records CaptureVendorBillDetail Updated.");
						t.commit();	
					}
					
					
		 }
		 }catch (Exception e) {
				t.rollback();
				e.printStackTrace();
				return "500";
				// TODO: handle exception
			}finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		 return "Successfully saved";
	}
	
	@Override
	public String savePaymentVendorBillDetails(HashMap<String, Object> jsondata) {
		 Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		 Transaction t = session.beginTransaction();
		 try {
		
			 if(jsondata.get("penaltyAmount")!=null && !jsondata.get("penaltyAmount").equals("") && jsondata.get("finalAmount")!=null && !jsondata.get("finalAmount").equals(""))
	         {
				 VendorInvoicePayment vip=new VendorInvoicePayment();
				 vip.setCaptureVendorBillDetailId(Long.parseLong(jsondata.get("captureVendorBillDetailId").toString()));
				 Date dateOfPayment=HMSUtil.convertStringTypeDateToDateType(jsondata.get("finalPaymentDate").toString());
				 vip.setPaymentDate(dateOfPayment);
		         vip.setInvoiceAmount(Long.parseLong(jsondata.get("invoiceAmount").toString()));
		         vip.setPenaltyAmount(Long.parseLong(jsondata.get("penaltyAmount").toString()));
		         vip.setClearAmount(Long.parseLong(jsondata.get("finalAmount").toString()));
		         vip.setTdsDeduction(Long.parseLong(jsondata.get("tdsDeduction").toString()));
		         vip.setModeOfPayment(jsondata.get("modeOfPayment").toString());
		         vip.setTransactionNumber(jsondata.get("transNo").toString());
		         vip.setPhase(jsondata.get("phase").toString());
		         vip.setAdvancedPayment(Long.parseLong(jsondata.get("advancedAmount").toString()));
		         vip.setAmountPaid(Long.parseLong(jsondata.get("calculateUtilzedAmount").toString()));
		         session.save(vip);
	          }
		
				String captureVendorBillDetailId=jsondata.get("captureVendorBillDetailId").toString();
			
				String penaltyAmount=jsondata.get("penaltyAmount").toString();
				String finalAmount=jsondata.get("finalAmount").toString();
				String calculateUtilzedAmount=jsondata.get("calculateUtilzedAmount").toString();
				String advancedPayment=jsondata.get("advancedAmount").toString();
				String lastApprovalMsg=jsondata.get("lastApprovalMsg").toString();
				String phase=jsondata.get("phase").toString();
				String qryStringHd  ="update CaptureVendorBillDetail u set u.lastApprovalStatus='"+lastApprovalMsg+"',u.penaltyAmount='"+penaltyAmount+"',u.clearAmount='"+finalAmount+"',u.finalAmount='"+calculateUtilzedAmount+"',u.advancedPayment='"+advancedPayment+"',u.status='C',u.paymentStatus='C',u.phase='"+phase+"'  where u.captureVendorBillDetailId='"+captureVendorBillDetailId+"' " ;
				Query queryHd = session.createQuery(qryStringHd);
		        int countHd = queryHd.executeUpdate();
				System.out.println(countHd + "Records CaptureVendorBillDetail Updated.");
				t.commit();	

			
		 }catch (Exception e) {
				t.rollback();
				e.printStackTrace();
				return "500";
				// TODO: handle exception
			}finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		 return "Successfully saved";
	}
	
	 @Override
	    public Map<String, List<VendorInvoicePayment>> getVendorInvoicePaymentDetail(JSONObject jsondata){
	        Map<String, List<VendorInvoicePayment>> map = new HashMap<>();
	        List<VendorInvoicePayment> billDetails = new ArrayList<>();
	       
	        try {
	            Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	            Criteria criteria = session.createCriteria(VendorInvoicePayment.class);
	           
	            if (jsondata.has("captureVendorBillDetailId") && !jsondata.get("captureVendorBillDetailId").toString().trim().isEmpty()) {
	                criteria.add(Restrictions.eq("captureVendorBillDetailId", jsondata.getLong("captureVendorBillDetailId")));
	            }
	            
	            billDetails = criteria.list();
	            criteria.addOrder(Order.asc("captureVendorBillDetailId"));
	      
	            billDetails = criteria.list();

	            map.put("capturedVendorBillDetails", billDetails);
	       
	        }catch(Exception e) {
	            e.printStackTrace();
	        }finally {
	            getHibernateUtils.getHibernateUtlis().CloseConnection();
	        }
	        return map;
	    }
	 
	 @Override
		public List<MasCamp> getMasCampFromMMUIdAndDate(Long mmuId,String date){
		        
		        List<MasCamp> capturedInspectionList = new ArrayList<MasCamp>();
		         

		        try {
		        	 
		        	String campDate=date;//jsondata.getString("inspectionDate");
		        	Date campDateVal=HMSUtil.convertStringTypeDateToDateType(campDate.trim());
		            Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		            Criteria criteria = session.createCriteria(MasCamp.class);
		              criteria.add(Restrictions.eq("mmuId",mmuId));
		              criteria.add(Restrictions.eq("campDate", campDateVal));
		          
		           
		            capturedInspectionList = criteria.list();


		             
		        }catch(Exception e) {
		            e.printStackTrace();
		        }finally {
		            getHibernateUtils.getHibernateUtlis().CloseConnection();
		        }

		        return capturedInspectionList;
		    }
	 
	 public Map<String, Object> getAuthorityAndOrderNo(Long authorityId,Long captureVendorBillDetailId) {
			Session session1 = getHibernateUtils.getHibernateUtlis().OpenSession();
			Map<String, Object> map = new HashMap<>();
			List<String> searchList = null;
			try {
				String Query = "SELECT  via.authority_id,via.order_no,u.mobile_no,u.user_name" + 
						" FROM vendor_invoice_approval via" + 
						" left outer join users u on u.user_id=via.user_id" +
						" WHERE via.capture_vendor_bill_detail_id ="+captureVendorBillDetailId+" and" + 
						" via.authority_id < "+authorityId+"" + 
						" ORDER BY via.authority_id DESC" + 
						" LIMIT 1";
				System.out.println(Query);
				if (Query != null) {
					searchList = session1.createSQLQuery(Query).list();
				} else {
					System.out.println("No Record Found");
				}
				map.put("list", searchList);
			} catch (Exception ex) {
				ex.printStackTrace();
			} 
			return map;
		}
	 
	 @Override
	 public Map<String, Object> getBillMonthandYear(Long month,Long year,Long district,Long cityId,String mmuIds) {
			Session session1 = getHibernateUtils.getHibernateUtlis().OpenSession();
			Map<String, Object> map = new HashMap<>();
			List<String> searchList = null;
			try {
				String Query = "select cd.bill_month,cd.bill_year from capture_vendor_bill_detail cd" + 
						" left outer join capture_vendor_bill_mmu_detail cm on cm.capture_vendor_bill_detail_id=cd.capture_vendor_bill_detail_id" + 
						" where cd.bill_month="+month+" and cd.bill_year="+year+" and cd.district_id="+district+" and cd.city_id="+cityId+"" + 
						" and cm.mmu_id in("+mmuIds+")";
				System.out.println(Query);
				if (Query != null) {
					searchList = session1.createSQLQuery(Query).list();
				} else {
					System.out.println("No Record Found");
				}
				map.put("list", searchList);
			} catch (Exception ex) {
				ex.printStackTrace();
			} 
			return map;
		}

	@Override
	public Long saveFundAllocationDetails(HashMap<String, Object> getJsondata,String letterName) {
		Long headerFundId = null;
		JSONArray newForm=new JSONArray(getJsondata.get("headMainData").toString());
		JSONObject jsondata = (JSONObject) newForm.get(0);
		
		Date currentDate = ProjectUtils.getCurrentDate();
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria cr = session.createCriteria(OpdPatientDetail.class);
		Transaction tx = session.beginTransaction();
		Calendar calendar = Calendar.getInstance();
		java.sql.Timestamp ourJavaTimestampObject = new java.sql.Timestamp(calendar.getTime().getTime());
		try {

			//////////////////////////////////////// Fund HD and Fund DT	//////////////////////////////////////// ////////////////////////////////////////
				if (jsondata!= null) {
				
							FundAllocationHd fundorderhd = null;
							Long fundAllocationHdId=null;
							if(!jsondata.get("fundAllocationHdId").equals("") && jsondata.get("fundAllocationHdId")!=null)
							{	
								fundAllocationHdId=Long.parseLong(String.valueOf(jsondata.get("fundAllocationHdId")));
							}
							Date orderDate = HMSUtil.getTodayFormattedDate();
							System.out.println("today"+orderDate);//Today's date
						
							fundorderhd = new FundAllocationHd();
							Date dateOfFund=HMSUtil.convertStringTypeDateToDateType(jsondata.get("dateOfUpload").toString());
							fundorderhd.setFundAllocationDate(dateOfFund);
							fundorderhd.setScheme("MMSSY");
							fundorderhd.setStatus(String.valueOf(jsondata.get("statusFlag")));
							if(jsondata.has("remarks"))
							{	
							fundorderhd.setRemarks(String.valueOf(jsondata.get("remarks")));
							}
							if(!jsondata.get("createdUserId").equals(""))
							{	
								fundorderhd.setCreatedBy(Long.parseLong(String.valueOf(jsondata.get("createdUserId"))));
								Date dateOfEntry=HMSUtil.convertStringTypeDateToDateType(jsondata.get("createdOn").toString());
								fundorderhd.setCreatedOn(dateOfEntry);
								if(letterName!=null &&letterName!="")
								{	
								fundorderhd.setFileName(letterName);
								}
								else
								{
									fundorderhd.setFileName(jsondata.get("fundLetterNameDuplicate").toString());	
								}
							}
							else
							{	
								fundorderhd.setCreatedBy(Long.parseLong(String.valueOf(jsondata.get("userId"))));
								fundorderhd.setCreatedOn(orderDate);
								if(letterName!=null &&letterName!="")
								{	
								fundorderhd.setFileName(letterName);
								}
							}
							fundorderhd.setTotalAmount(Long.parseLong(String.valueOf(jsondata.get("totalAllocatedAmount"))));
							fundorderhd.setLetterNo(String.valueOf(jsondata.get("letterNo")));
							fundorderhd.setFinancialId(Long.parseLong(String.valueOf(jsondata.get("financialYear"))));
									//////////////////Phase/////////////////////////////////
											fundorderhd.setPhase(String.valueOf(jsondata.get("phaseVal")));
											//////////////////////////////////////////
											
							
							if(!jsondata.get("fundAllocationHdId").equals("") && jsondata.get("fundAllocationHdId")!=null)
							{
								fundorderhd.setFundAllocationHdId(Long.parseLong(String.valueOf(jsondata.get("fundAllocationHdId"))));
								session.update(fundorderhd);
								headerFundId=fundAllocationHdId;
							}
							else
							{	
							headerFundId = Long.parseLong(session.save(fundorderhd).toString());
							}
						
						
							JSONArray invList = new JSONArray(jsondata.get("listofHeader").toString());
						
							for (int K = 0; K < invList.length(); K++) {
								JSONObject invMap = invList.getJSONObject(K);
								FundAllocationDt ob1 = new FundAllocationDt();
								if (invMap.get("upssId") != null && invMap.get("upssId") != "") {
									ob1.setDistrictId(Long.valueOf(invMap.get("upssId").toString()));
								}
								if (invMap.get("cityId") != null && invMap.get("cityId") != "") {
									ob1.setCityId(Long.valueOf(invMap.get("cityId").toString()));
								}
								if (invMap.get("headTypeId") != null && invMap.get("headTypeId") != "") {
									ob1.setHeadTypeId(Long.valueOf(invMap.get("headTypeId").toString()));
								}
								if (invMap.get("amount") != null && invMap.get("amount") != "") {
									ob1.setAllocatedAmount(Long.valueOf(invMap.get("amount").toString()));
								}
								if (!invMap.get("fundAllocationDtId").equals("") && invMap.get("fundAllocationDtId") != null) {
									ob1.setFundAllocationDtId(Long.valueOf(invMap.get("fundAllocationDtId").toString()));
								}
																	
								ob1.setFundAllocationHdId(headerFundId);
								if (!invMap.get("fundAllocationDtId").equals("") && invMap.get("fundAllocationDtId") != null) {
									session.update(ob1);	
								}
								else
								{	
								session.save(ob1);
								}

							}

				}
				
			tx.commit();

		} catch (Exception ex) {

			// System.out.println("Exception e="+ex.);
			ex.printStackTrace();
			tx.rollback();
			System.out.println("Exception Message Print ::" + ex.toString());
			return 0L;
		} finally {

			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return headerFundId;
	}
	
	@Override
    public Map<String, List<FundAllocationHd>> getFundAllocationHdDetails(JSONObject jsondata){
        Map<String, List<FundAllocationHd>> map = new HashMap<>();
        List<FundAllocationHd> billDetails = new ArrayList<>();
        List totalMatches  =new ArrayList<>();
        int pageNo=0;
        int pageSize = 5;
        String fromDate = "";
        String toDate = "";
        Criterion c1 = null;
        /*if(jsondata.has("fromDate"))
        {	
         fromDate = (String) jsondata.get("fromDate");
		 toDate = (String) jsondata.get("toDate");
        }*/
        try {
            Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
            Criteria criteria = session.createCriteria(FundAllocationHd.class);
            if(jsondata.get("PN").toString() !=null)
                pageNo = Integer.parseInt(jsondata.get("PN").toString());

            if (jsondata.has("statusPending") && !jsondata.get("statusPending").toString().trim().isEmpty()) {
                //criteria.add(Restrictions.eq("status","S"));
            	criteria.add(Restrictions.or(Restrictions.eq("status", "S"), Restrictions.eq("status", "C"),Restrictions.eq("status", "A")));
                
            }
            if (jsondata.has("status") && !jsondata.get("status").toString().trim().isEmpty()) {
                criteria.add(Restrictions.eq("status","C"));
            	//criteria.add(Restrictions.or(Restrictions.eq("status", "C"), Restrictions.eq("status", "A")));
                
            }
            if (jsondata.has("financialYear") && !jsondata.get("financialYear").toString().trim().isEmpty()) {
                criteria.add(Restrictions.eq("financialId",Long.parseLong(jsondata.get("financialYear").toString())));
            	//criteria.add(Restrictions.or(Restrictions.eq("status", "C"), Restrictions.eq("status", "A")));
                
            }
            if (jsondata.has("phaseVal") && !jsondata.get("phaseVal").toString().trim().isEmpty()) {
                criteria.add(Restrictions.eq("phase", jsondata.get("phaseVal").toString().trim()));
            	//criteria.add(Restrictions.or(Restrictions.eq("status", "C"), Restrictions.eq("status", "A")));
                
            }
          
           
            criteria.addOrder(Order.desc("fundAllocationHdId"));
            totalMatches = criteria.list();
            if (pageNo > 0) {
                criteria.setFirstResult((pageSize) * (pageNo - 1));
                criteria.setMaxResults(pageSize);
            }
            billDetails = criteria.list();


            map.put("fundAllocationDetails", billDetails);
            map.put("totalMatches", totalMatches);
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            getHibernateUtils.getHibernateUtlis().CloseConnection();
        }
        return map;
    }
	
	@Override
	public Map<Long, FundAllocationDt> getFundAllocationDtDetails(List<Long> ids){
        Map<Long, FundAllocationDt> equipMap = new HashMap<>();
        if (ids != null && !ids.isEmpty()) {
            String hql = "Select c from FundAllocationDt c where c.fundAllocationHdId in(:ids)";
            Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
            Query query = session.createQuery(hql);
            query.setParameterList("ids", ids);
            List<FundAllocationDt> li = query.list();
            equipMap = li.stream().collect(Collectors.toMap(FundAllocationDt::getFundAllocationDtId, Function.identity()));
        }
        return equipMap;
    }
	
	 @Override
		public List<Object[]> getDgFundAllcationHdDt(Long fundAllocationHdId) {
			//List<DgMasInvestigation>  listDgMasInvestigation =null;
			String databaseScema = HMSUtil.getProperties("adt.properties", "currentSchema").trim();
			final String fund_allocation_dt=databaseScema+"."+"fund_allocation_dt";
			final String fund_allocation_hd=databaseScema+"."+"fund_allocation_hd";
			Transaction transation=null;
			 List<Object[]> listObject=null;
			try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			transation=session.beginTransaction();
			 StringBuilder sbQuery = new StringBuilder();
			 
			    sbQuery.append(" select fah.fund_allocation_hd_id,fah.financial_id,fah.fund_allocation_date,fah.letter_no,fah.total_amount,fah.file_name,");
			    sbQuery.append( "fad.fund_allocation_dt_id,fad.district_id,fad.head_type_id,fad.allocated_amount,");
			    sbQuery.append(" fad.allocation_flag,fah.status,fah.created_on,fah.created_by,fad.city_id,fah.phase from " +fund_allocation_hd+ " fah "); 
			    sbQuery.append(" left outer join " +fund_allocation_dt+ " fad on fah.fund_allocation_hd_id=fad.fund_allocation_hd_id"); 
			    sbQuery.append(" where fah.fund_allocation_hd_id =:fundAllocationHdId");
			    Query query = session.createSQLQuery(sbQuery.toString());

			    query.setParameter("fundAllocationHdId", fundAllocationHdId);
			    
			     listObject = query.list();
			transation.commit();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			
			}
			return listObject;
		}
		
		@Override
		public String deleteFundAllocationDtDetails(HashMap<String, Object> jsondata) {
			try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Transaction t = session.beginTransaction();
			String fundAllocationDtId=jsondata.get("fundAllocationDtId").toString();
			
			if(fundAllocationDtId!=null)
			{	
			String qryStringHd  ="delete from FundAllocationDt v where v.fundAllocationDtId='"+fundAllocationDtId+"'" ;
			Query queryHd = session.createQuery(qryStringHd);
	        int countHd = queryHd.executeUpdate();
			System.out.println(countHd + " Record(s) fundAllocationDtId delete Records Updated.");
			t.commit();
			}
			
			}catch (Exception e) {
				e.printStackTrace();
				return "500";
				// TODO: handle exception
			}
			return "statusUpdated";
		}
		
		@Override
		public Long saveFundAllocationApprovalDetails(HashMap<String, Object> getJsondata,String letterName) {
			Long headerFundId = null;
			JSONArray newForm=new JSONArray(getJsondata.get("headMainData").toString());
			JSONObject jsondata = (JSONObject) newForm.get(0);
			
			Date currentDate = ProjectUtils.getCurrentDate();
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria cr = session.createCriteria(OpdPatientDetail.class);
			Transaction tx = session.beginTransaction();
			Calendar calendar = Calendar.getInstance();
			java.sql.Timestamp ourJavaTimestampObject = new java.sql.Timestamp(calendar.getTime().getTime());
			try {

				//////////////////////////////////////// Fund HD and Fund DT	//////////////////////////////////////// ////////////////////////////////////////
					if (jsondata!= null) {
					
								FundAllocationHd fundorderhd = null;
								Long fundAllocationHdId=null;
								if(jsondata.get("fundAllocationHdId")!="" && jsondata.get("fundAllocationHdId")!=null)
								{	
									fundAllocationHdId=Long.parseLong(String.valueOf(jsondata.get("fundAllocationHdId")));
								}
								Date orderDate = HMSUtil.getTodayFormattedDate();
								System.out.println("today"+orderDate);//Today's date
							
								fundorderhd = new FundAllocationHd();
								Date dateOfFund=HMSUtil.convertStringTypeDateToDateType(jsondata.get("dateOfUpload").toString());
								fundorderhd.setFundAllocationDate(dateOfFund);
								fundorderhd.setScheme("MMSSY");
								if(!jsondata.get("createdUserId").equals(""))
								{	
									fundorderhd.setCreatedBy(Long.parseLong(String.valueOf(jsondata.get("createdUserId"))));
									Date dateOfEntry=HMSUtil.convertStringTypeDateToDateType(jsondata.get("createdOn").toString());
									fundorderhd.setCreatedOn(dateOfEntry);
									if(letterName!=null &&letterName!="")
									{	
									fundorderhd.setFileName(letterName);
									}
									else
									{
										fundorderhd.setFileName(jsondata.get("fundLetterNameDuplicate").toString());	
									}
								}
								
								fundorderhd.setStatus(String.valueOf(jsondata.get("statusFlag")));
								fundorderhd.setRemarks(String.valueOf(jsondata.get("approvalRemarks")));
								fundorderhd.setApprovedDate(orderDate);
								fundorderhd.setApprovedBy(Long.parseLong(String.valueOf(jsondata.get("userId"))));
								fundorderhd.setTotalAmount(Long.parseLong(String.valueOf(jsondata.get("totalAllocatedAmount"))));
								fundorderhd.setLetterNo(String.valueOf(jsondata.get("letterNo")));
								fundorderhd.setFinancialId(Long.parseLong(String.valueOf(jsondata.get("financialYear"))));
								//////////////////Phase/////////////////////////////////
								fundorderhd.setPhase(String.valueOf(jsondata.get("phaseVal")));
								//////////////////////////////////////////
								
								if(jsondata.get("fundAllocationHdId")!="" && jsondata.get("fundAllocationHdId")!=null)
								{
									fundorderhd.setFundAllocationHdId(Long.parseLong(String.valueOf(jsondata.get("fundAllocationHdId"))));
									session.update(fundorderhd);
									headerFundId=fundAllocationHdId;
								}
								else
								{	
								headerFundId = Long.parseLong(session.save(fundorderhd).toString());
								}
							
							
								JSONArray invList = new JSONArray(jsondata.get("listofHeader").toString());
							
								for (int K = 0; K < invList.length(); K++) {
									JSONObject invMap = invList.getJSONObject(K);
									FundAllocationDt ob1 = new FundAllocationDt();
									if (invMap.get("upssId") != null && invMap.get("upssId") != "") {
										ob1.setDistrictId(Long.valueOf(invMap.get("upssId").toString()));
									}
									if (invMap.get("cityId") != null && invMap.get("cityId") != "") {
										ob1.setCityId(Long.valueOf(invMap.get("cityId").toString()));
									}
									if (invMap.get("headTypeId") != null && invMap.get("headTypeId") != "") {
										ob1.setHeadTypeId(Long.valueOf(invMap.get("headTypeId").toString()));
									}
									if (invMap.get("amount") != null && invMap.get("amount") != "") {
										ob1.setAllocatedAmount(Long.valueOf(invMap.get("amount").toString()));
									}
									ob1.setFundAllocationHdId(headerFundId);
									if (!invMap.get("fundAllocationDtId").equals("") && invMap.get("fundAllocationDtId") != null) {
										ob1.setFundAllocationDtId(Long.valueOf(invMap.get("fundAllocationDtId").toString()));
									}
																		
									ob1.setFundAllocationHdId(headerFundId);
									if (!invMap.get("fundAllocationDtId").equals("") && invMap.get("fundAllocationDtId") != null) {
										session.update(ob1);	
									}
									else
									{	
									session.save(ob1);
									}

								}
							JSONArray invListFundHcb = new JSONArray(jsondata.get("listofHeaderFundHcb").toString());
							for (int K = 0; K < invListFundHcb.length(); K++) {
								JSONObject invMapFund = invListFundHcb.getJSONObject(K);
								Long upssId=Long.valueOf(invMapFund.get("upssId").toString());
								Long cityId=Long.valueOf(invMapFund.get("cityId").toString());
								Long headTypeId=Long.valueOf(invMapFund.get("headTypeId").toString());
								Long amount=Long.valueOf(invMapFund.get("amount").toString());
								FundHcb fundDetails =getFundHcb(upssId,cityId, headTypeId, HMSUtil.getTodayFormattedDate(),jsondata.get("phaseVal").toString());
								
								Map<String, Object> nextAuthorityDetails=fundHcbDao.getFundOpernationalUpdatedBalance(upssId,cityId,headTypeId,jsondata.get("phaseVal").toString());
								 Long availableBalance = null;
								 List<Object[]> listCamp = (List<Object[]>) nextAuthorityDetails.get("list");
									for (Object[] row : listCamp) {
										if (row[0]!= null) {
											availableBalance=Long.parseLong(row[0].toString());
										
											
										}
									
									 }
								if(fundDetails!=null) {
									fundDetails.setOpeningBalance(fundDetails.getHcbBalance());
									fundDetails.setDebitCredit(amount);
									fundDetails.setHcbBalance(fundDetails.getHcbBalance()+amount);
									session.update(fundDetails);
								}else {
									FundHcb fundHcb = new FundHcb();
									fundHcb.setUpssId(upssId);
									fundHcb.setCityId(cityId);
									fundHcb.setFundFlag("U");
									fundHcb.setHeadTypeId(headTypeId);
									fundHcb.setHcbDate(HMSUtil.getTodayFormattedDate());
									if(jsondata.has("phaseVal"))
									fundHcb.setPhase(jsondata.get("phaseVal").toString());
									if(jsondata.has("phase"))
										fundHcb.setPhase(jsondata.get("phase").toString());
										
									if(availableBalance!=null)
									{	
									fundHcb.setOpeningBalance(availableBalance);
									fundHcb.setDebitCredit(amount);
									fundHcb.setHcbBalance(availableBalance+amount);
									}
									else
									{
										fundHcb.setOpeningBalance(0L);
										fundHcb.setDebitCredit(amount);
										fundHcb.setHcbBalance(amount);	
									}
									session.save(fundHcb);
								}
							}

					}
					
				tx.commit();

			} catch (Exception ex) {

				// System.out.println("Exception e="+ex.);
				ex.printStackTrace();
				tx.rollback();
				System.out.println("Exception Message Print ::" + ex.toString());
				return 0L;
			} finally {

				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}

			return headerFundId;
		}
		
		@Override
		public FundHcb getFundHcb(Long upss_id,Long cityId,Long head_type_id,Date currentDate,String phase) {
			try {
				Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria = session.createCriteria(FundHcb.class);
				Criterion upssIDExcpectaion= Restrictions.eq("upssId", upss_id);
				Criterion cityIdException= Restrictions.eq("cityId", cityId);
				Criterion headTypeExpectation= Restrictions.eq("headTypeId", head_type_id);
				Criterion currentDateExpectation= Restrictions.eq("hcbDate", currentDate);
				Criterion phaseVal= Restrictions.eq("hcbDate", phase);
				Conjunction logicalAndExpression
	            = Restrictions.and(upssIDExcpectaion,cityIdException,
	            		headTypeExpectation,currentDateExpectation,phaseVal);
				criteria.add(logicalAndExpression);
				List<FundHcb> list = criteria.list();
				if(!list.isEmpty())
					return list.get(0);
		}catch(Exception e) {
			
			e.printStackTrace();
		}finally {
			//getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
			return null;
		}

		@Override
		public Map<String, Object> getFundAllocationAmount(HashMap<String, Object> jsondata, HttpServletRequest request,
				HttpServletResponse response) {
			Map<String,Object> map = new HashMap<>();
			
			//int userId = 2;
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();	
			JSONObject jsonObject = new JSONObject();
			session.doWork(new Work() {
				@Override
				public void execute(java.sql.Connection connection) throws SQLException {
					
					int mmuId=1;
					String phaseVal="";
					JSONArray jsonArray1 = new JSONArray();				
					JSONArray jsonArray2 = new JSONArray();	
					
					
					
					if(jsondata.containsKey("financialId")) {
						mmuId = Integer.parseInt(jsondata.get("financialId").toString());
						}
					if(jsondata.containsKey("phaseVal")) {
						phaseVal =  jsondata.get("phaseVal").toString();
						}
					String queryString =null;
					if(StringUtils.isNotEmpty(phaseVal)) {
					  queryString = "SELECT asp_fund_allocation_financial_phase(?,?)";				
					}else {
						queryString = "SELECT asp_fund_allocation_financial(?)";
					}
					PreparedStatement    stmt = connection.prepareCall(queryString);
					if(StringUtils.isNotEmpty(phaseVal)) {
					stmt.setInt(1, mmuId);
					stmt.setString(2, phaseVal);
					}
					else {
						stmt.setInt(1, mmuId);
					}
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
							jsonObject.put("asp_fund", jsonArray1);
						}
					  }
					
					if (rs.next()) {
						Object o = rs.getObject(1);
						if (o instanceof ResultSet) {
							ResultSet rs2 = (ResultSet) o;
							while (rs2.next()) {
								int columnCount = rs2.getMetaData().getColumnCount();
								JSONObject jsonObj = new JSONObject();
								for (int i = 0; i < columnCount; i++) {
									jsonObj.put(rs2.getMetaData().getColumnLabel(i + 1), HMSUtil.convertNullToEmptyString(rs2.getObject(i + 1)));
								}
								jsonArray2.put(jsonObj);
							}
							jsonObject.put("total_data", jsonArray2);
						}
					}
					
				    }
			   });
				
					map.put("asp_fund_allocation", jsonObject);
					return map;
			}

		@Override
		public Map<String, Object> getPenaltyAuthId(Long upss_id) {
			Session session1 = getHibernateUtils.getHibernateUtlis().OpenSession();
			Map<String, Object> map = new HashMap<>();
			List<String> searchList = null;
			try {
				String Query = "select authority_id,upps_id from Penalty_authority_config where upps_id="+upss_id+"";
				System.out.println(Query);
				if (Query != null) {
					searchList = session1.createSQLQuery(Query).list();
				} else {
					System.out.println("No Record Found");
				}
				map.put("list", searchList);
			} catch (Exception ex) {
				ex.printStackTrace();
			} 
			return map;
		}

	 
		@Override
		public Long saveCaptureInterestDetails(HashMap<String, Object> getJsondata ) {
			Long captureInteHdId = null;
			JSONArray newForm=new JSONArray(getJsondata.get("headMainData").toString());
			JSONObject jsondata = (JSONObject) newForm.get(0);
			List<Long>listCaptureInterest=new ArrayList<>();
			Date currentDate = ProjectUtils.getCurrentDate();
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria cr = session.createCriteria(OpdPatientDetail.class);
			Transaction tx = session.beginTransaction();
			Calendar calendar = Calendar.getInstance();
			java.sql.Timestamp ourJavaTimestampObject = new java.sql.Timestamp(calendar.getTime().getTime());
			Long financialId=null;
			Long userId=null;
			String statusFlag="";
			if (jsondata.get("financialYear") != null && jsondata.get("financialYear") != "") {
				financialId= Long.valueOf( jsondata.get("financialYear").toString());
			}
			if (jsondata.get("userId") != null && jsondata.get("userId") != "") {
				userId= Long.valueOf( jsondata.get("userId").toString());
			}
			if (jsondata.get("statusFlag") != null && jsondata.get("statusFlag") != "") {
				statusFlag=   jsondata.get("statusFlag").toString();
			}
			try {

				//////////////////////////////////////// Fund HD and Fund DT	//////////////////////////////////////// ////////////////////////////////////////
						if (jsondata!= null) {
					 		
							
							CaptureInterestHd captureInterestHd = null;
							Long captureIntHdId=null;
							if(!jsondata.get("captureInterestHdId").toString().equalsIgnoreCase("")&& jsondata.get("captureInterestHdId")!="" && jsondata.get("captureInterestHdId")!=null )
							{	
								captureIntHdId=Long.parseLong(String.valueOf(jsondata.get("captureInterestHdId")));
							}
							Date orderDate = HMSUtil.getTodayFormattedDate();
							System.out.println("today"+orderDate);//Today's date
						
							captureInterestHd = new CaptureInterestHd();
							captureInterestHd.setCreatedBy(userId);
							captureInterestHd.setCreatedOn(new Date());
							captureInterestHd.setStatus(statusFlag);
			//////////////////Phase/////////////////////////////////
							captureInterestHd.setPhase(String.valueOf(jsondata.get("phaseVal")));
														//////////////////////////////////////////
														
							if(jsondata.has("actionId"))
							{
								captureInterestHd.setRemarks(String.valueOf(jsondata.get("approvalRemarks")));
								JSONArray invListFundHcb = new JSONArray(jsondata.get("listofHeader").toString());
								for (int K = 0; K < invListFundHcb.length(); K++) {
									JSONObject invMapFund = invListFundHcb.getJSONObject(K);
									Long upssId=Long.valueOf(invMapFund.get("upssId").toString());
									Long cityId=Long.valueOf(invMapFund.get("cityId").toString());
									Long headTypeId=Long.valueOf(invMapFund.get("headTypeId").toString());
									Long amount=Long.valueOf(invMapFund.get("amount").toString());
									FundHcb fundDetails =getFundHcb(upssId,cityId, headTypeId, HMSUtil.getTodayFormattedDate(),jsondata.get("phaseVal").toString());
									
									Map<String, Object> nextAuthorityDetails=fundHcbDao.getFundOpernationalUpdatedBalance(upssId,cityId,headTypeId,jsondata.get("phaseVal").toString());
									 Long availableBalance = null;
									 List<Object[]> listCamp = (List<Object[]>) nextAuthorityDetails.get("list");
										for (Object[] row : listCamp) {
											if (row[0]!= null) {
												availableBalance=Long.parseLong(row[0].toString());
											
												
											}
										
										 }
									if(fundDetails!=null) {
										fundDetails.setOpeningBalance(fundDetails.getHcbBalance());
										fundDetails.setDebitCredit(amount);
										fundDetails.setHcbBalance(fundDetails.getHcbBalance()+amount);
										session.update(fundDetails);
									}else {
										FundHcb fundHcb = new FundHcb();
										fundHcb.setUpssId(upssId);
										fundHcb.setCityId(cityId);
										fundHcb.setFundFlag("U");
										fundHcb.setHeadTypeId(headTypeId);
										fundHcb.setHcbDate(HMSUtil.getTodayFormattedDate());
										if(jsondata.has("phaseVal"))
										fundHcb.setPhase(jsondata.get("phaseVal").toString());
										if(jsondata.has("phase"))
											fundHcb.setPhase(jsondata.get("phase").toString());
											
										if(availableBalance!=null)
										{	
										fundHcb.setOpeningBalance(availableBalance);
										fundHcb.setDebitCredit(amount);
										fundHcb.setHcbBalance(availableBalance+amount);
										}
										else
										{
											fundHcb.setOpeningBalance(0L);
											fundHcb.setDebitCredit(amount);
											fundHcb.setHcbBalance(amount);	
										}
										session.save(fundHcb);
									}
								}
							}
							 
							
							captureInterestHd.setFinancialId(financialId);
							 MasStoreFinancial masStoreFinancial = (MasStoreFinancial) session.load(MasStoreFinancial.class,financialId);
							captureInterestHd.setFinancialYear(masStoreFinancial.getFinancialYear());
							 
							if(!jsondata.get("captureInterestHdId").toString().equalsIgnoreCase("") && jsondata.get("captureInterestHdId")!="" && jsondata.get("captureInterestHdId")!=null)
							{
								captureInterestHd.setCaptureInterestHdId(Long.parseLong(String.valueOf(jsondata.get("captureInterestHdId"))));
								session.update(captureInterestHd);
								captureInteHdId=captureIntHdId;
							}
							else
							{	
								captureInteHdId = Long.parseLong(session.save(captureInterestHd).toString());
							}
							
							
							
								JSONArray invList = new JSONArray(jsondata.get("listofHeader").toString());
							
								for (int K = 0; K < invList.length(); K++) {
									JSONObject invMap = invList.getJSONObject(K);
									CaptureInterestDt ob1 = new CaptureInterestDt();
									if (invMap.get("upssId") != null && invMap.get("upssId") != "") {
										ob1.setDistrictId(Long.valueOf(invMap.get("upssId").toString()));
									}
									if (invMap.get("cityId") != null && invMap.get("cityId") != "") {
										ob1.setCityId(Long.valueOf(invMap.get("cityId").toString()));
									}
									if (invMap.get("headTypeId") != null && invMap.get("headTypeId") != "") {
										ob1.setHeadTypeId(Long.valueOf(invMap.get("headTypeId").toString()));
									}
									if (invMap.get("amount") != null && invMap.get("amount") != "") {
										ob1.setInterest(Long.valueOf(invMap.get("amount").toString()));
									}
									
									ob1.setStatus(statusFlag);
									 //ob1.setLastChangeBy(userId);
									ob1.setCreateBy(userId);
								 	ob1.setAllocationFlag(statusFlag);								
								 	ob1.setFinancialId(financialId);
								 	ob1.setCreatedOn(currentDate);
								 	ob1.setCaptureInterestHdId(captureInteHdId);
								 	
								 	if (invMap.has("captureInterestDtId")&&!invMap.get("captureInterestDtId").equals("") && invMap.get("captureInterestDtId") != null) {
								 		Long captureInteDtId = Long.parseLong(invMap.get("captureInterestDtId").toString());
								 		ob1.setCaptureInterestDtId(captureInteDtId);
								 		session.update(ob1);
										//listCaptureInterest.add(headerFundId);
									}
									else
									{	
										 session.save(ob1);//session.save(ob1);
										//listCaptureInterest.add(captureInterest);
									}
								 		
								}
							 

					}
					
				tx.commit();

			} catch (Exception ex) {

				// System.out.println("Exception e="+ex.);
				ex.printStackTrace();
				tx.rollback();
				System.out.println("Exception Message Print ::" + ex.toString());
				return captureInteHdId;
			} finally {

				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}

			return captureInteHdId;
		}
		
		@Override
	    public Map<String, List<CaptureInterestHd>> getAllCaptureInterestDetails(JSONObject jsondata){
	        Map<String, List<CaptureInterestHd>> map = new HashMap<>();
	        List<CaptureInterestHd> captureInterestDetails = new ArrayList<>();
	        List totalMatches  =new ArrayList<>();
	        int pageNo=0;
	        int pageSize = 5;
	        String fromDate = "";
	        String toDate = "";
	        Criterion c1 = null;
	        /*if(jsondata.has("fromDate"))
	        {	
	         fromDate = (String) jsondata.get("fromDate");
			 toDate = (String) jsondata.get("toDate");
	        }*/
	        try {
	            Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	            Criteria criteria = session.createCriteria(CaptureInterestHd.class);
	            if(jsondata.get("PN").toString() !=null)
	                pageNo = Integer.parseInt(jsondata.get("PN").toString());

	            if (jsondata.has("statusPending") && !jsondata.get("statusPending").toString().trim().isEmpty()) {
	                //criteria.add(Restrictions.eq("status","S"));
	            	criteria.add(Restrictions.or(Restrictions.eq("status", "S"), Restrictions.eq("status", "C"),Restrictions.eq("status", "A")));
	                
	            }
	            if (jsondata.has("status") && !jsondata.get("status").toString().trim().isEmpty()) {
	                criteria.add(Restrictions.eq("status","C"));
	            	//criteria.add(Restrictions.or(Restrictions.eq("status", "C"), Restrictions.eq("status", "A")));
	             //	criteria.add(Restrictions.or(Restrictions.eq("status", "S"), Restrictions.eq("status", "C")));
	 	              
	            }
	            if (jsondata.has("financialYear") && !jsondata.get("financialYear").toString().trim().isEmpty()) {
	                criteria.add(Restrictions.eq("financialId",Long.parseLong(jsondata.get("financialYear").toString())));
	            	//criteria.add(Restrictions.or(Restrictions.eq("status", "C"), Restrictions.eq("status", "A")));
	                
	            }
	            
	            if (jsondata.has("captureInterestHdId") && !jsondata.get("captureInterestHdId").toString().trim().isEmpty()) {
	                criteria.add(Restrictions.eq("captureInterestHdId",Long.parseLong(jsondata.get("captureInterestHdId").toString())));
	                 
	            }
	           
	            criteria.addOrder(Order.desc("captureInterestHdId"));
	            totalMatches = criteria.list();
	            if (pageNo > 0) {
	                criteria.setFirstResult((pageSize) * (pageNo - 1));
	                criteria.setMaxResults(pageSize);
	            }
	            captureInterestDetails = criteria.list();


	            map.put("captureInterestDetails", captureInterestDetails);
	            map.put("totalMatches", totalMatches);
	        }catch(Exception e) {
	            e.printStackTrace();
	        }finally {
	            getHibernateUtils.getHibernateUtlis().CloseConnection();
	        }
	        return map;
	    }
		 
		@Override
		public Map<Long, CaptureInterestDt> getCaptureInterestDtDetails(List<Long> ids){
	        Map<Long, CaptureInterestDt> equipMap = new HashMap<>();
	        if (ids != null && !ids.isEmpty()) {
	            String hql = "Select c from CaptureInterestDt c where c.captureInterestHdId in(:ids)";
	            Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	            Query query = session.createQuery(hql);
	            query.setParameterList("ids", ids);
	            List<CaptureInterestDt> li = query.list();
	            equipMap = li.stream().collect(Collectors.toMap(CaptureInterestDt::getCaptureInterestDtId, Function.identity()));
	        }
	        return equipMap;
	    }
		
		@Override
		public List<Object[]> getCaptureInterestHdDt(Long captureInterestHdId) {
			//List<DgMasInvestigation>  listDgMasInvestigation =null;
			String databaseScema = HMSUtil.getProperties("adt.properties", "currentSchema").trim();
			final String capture_interest_dt=databaseScema+"."+"capture_interest_dt";
			final String capture_interest_hd=databaseScema+"."+"capture_interest_hd";
			Transaction transation=null;
			 List<Object[]> listObject=null;
			try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			transation=session.beginTransaction();
			 StringBuilder sbQuery = new StringBuilder();
			 
			    sbQuery.append(" select fah.capture_interest_hd_id,fah.financial_id,fah.created_on as captureHd_createdOn, ");
			    sbQuery.append( "fad.capture_interest_dt_id,fad.district_id,fad.head_type_id,fad.interest,");
			    sbQuery.append(" fad.allocation_flag,fah.status,fah.created_on,fah.created_by,fad.city_id,fah.phase  from " +capture_interest_hd+ " fah "); 
			    sbQuery.append(" left outer join " +capture_interest_dt+ " fad on fah.capture_interest_hd_id=fad.capture_interest_hd_id"); 
			    sbQuery.append(" where fah.capture_interest_hd_id =:captureInterestHdId");
			    Query query = session.createSQLQuery(sbQuery.toString());

			    query.setParameter("captureInterestHdId", captureInterestHdId);
			    
			     listObject = query.list();
			transation.commit();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			
			}
			return listObject;
		}
		@Override
		public String deleteCaptureInterestDtDetails(HashMap<String, Object> jsondata) {
			try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Transaction t = session.beginTransaction();
			String fundAllocationDtId=jsondata.get("captureInterestDtId").toString();
			
			if(fundAllocationDtId!=null)
			{	
			String qryStringHd  ="delete from CaptureInterestDt v where v.captureInterestDtId='"+fundAllocationDtId+"'" ;
			Query queryHd = session.createQuery(qryStringHd);
	        int countHd = queryHd.executeUpdate();
			System.out.println(countHd + " Record(s) CaptureInterestDtID delete Records Updated.");
			t.commit();
			}
			
			}catch (Exception e) {
				e.printStackTrace();
				return "500";
				// TODO: handle exception
			}
			return "statusUpdated";
		}

		@Override
		public Long saveUCDocumentUploadDetails(HashMap<String, Object> getJsondata, String letterName) {
			Long headerFundId = null;
			JSONArray newForm=new JSONArray(getJsondata.get("headMainData").toString());
			JSONObject jsondata = (JSONObject) newForm.get(0);
			Date currentDate = ProjectUtils.getCurrentDate();
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Transaction tx = session.beginTransaction();
			Calendar calendar = Calendar.getInstance();
			java.sql.Timestamp ourJavaTimestampObject = new java.sql.Timestamp(calendar.getTime().getTime());
			try {

				//////////////////////////////////////// Fund HD and Fund DT	//////////////////////////////////////// ////////////////////////////////////////
					if (jsondata!= null) {
					
								UcUploadHd ucUploadhd = null;
								Long ucUploadhdId=null;
								Long fundAllocationHdId=null;
								if(!jsondata.get("ucUploadHdId").equals("") && jsondata.get("ucUploadHdId")!=null)
								{	
									ucUploadhdId=Long.parseLong(String.valueOf(jsondata.get("ucUploadHdId")));
								}
								Date orderDate = HMSUtil.getTodayFormattedDate();
								System.out.println("today"+orderDate);//Today's date
							
								ucUploadhd = new UcUploadHd();
								ucUploadhd.setUpssId(Long.parseLong(jsondata.get("upssId").toString()));
								Date dateOfFund=HMSUtil.convertStringTypeDateToDateType(jsondata.get("dateOfUpload").toString());
								ucUploadhd.setUploadDate(dateOfFund);
								ucUploadhd.setRemarks(String.valueOf(jsondata.get("remarks")));
								ucUploadhd.setCertificateNo(String.valueOf(jsondata.get("certificateNo")));
								//fundorderhd.setScheme("MMSSY");
								ucUploadhd.setUploadFlag(String.valueOf(jsondata.get("statusFlag")));
								ucUploadhd.setEntryDate(currentDate);	
								ucUploadhd.setCreatedBy(Long.parseLong(String.valueOf(jsondata.get("userId"))));
								//ucUploadhd.setCreatedOn(orderDate);
									if(letterName!=null &&letterName!="")
									{	
										ucUploadhd.setFileName(letterName);
									}
									else
									{
										ucUploadhd.setFileName(jsondata.get("fundLetterNameDuplicate").toString());	
									}
								
									ucUploadhd.setFinancialId(Long.parseLong(String.valueOf(jsondata.get("financialYear"))));
										//////////////////Phase/////////////////////////////////
									ucUploadhd.setPhase(String.valueOf(jsondata.get("phaseVal")));
												//////////////////////////////////////////
												
								
								if(!jsondata.get("ucUploadHdId").equals("") && jsondata.get("ucUploadHdId")!=null)
								{
									ucUploadhd.setUcUploadHdId(Long.parseLong(String.valueOf(jsondata.get("ucUploadHdId"))));
									session.update(ucUploadhd);
									headerFundId=ucUploadhdId;
								}
								else
								{	
								headerFundId = Long.parseLong(session.save(ucUploadhd).toString());
								}
							
							
								JSONArray invList = new JSONArray(jsondata.get("listofHeader").toString());
							
								for (int K = 0; K < invList.length(); K++) {
									JSONObject invMap = invList.getJSONObject(K);
									UcUploadDt ob1 = new UcUploadDt();
									
									if (invMap.get("headTypeId") != null && invMap.get("headTypeId") != "") {
										ob1.setHeadTypeId(Long.valueOf(invMap.get("headTypeId").toString()));
									}
									if (invMap.get("availableBalance") != null && invMap.get("availableBalance") != "") {
										ob1.setAvailableBalance(Long.valueOf(invMap.get("availableBalance").toString()));
									}
									if (!invMap.get("availableUtilization").equals("") && invMap.get("availableUtilization") != null) {
										ob1.setAvailableUtilization(Long.valueOf(invMap.get("availableUtilization").toString()));
									}
									if (!invMap.get("ucUploadDtId").equals("") && invMap.get("ucUploadDtId") != null) {
										ob1.setUcUploadDtId(Long.valueOf(invMap.get("ucUploadDtId").toString()));
									}
																		
									ob1.setUcUploadHdId(headerFundId);
										
									if (!invMap.get("ucUploadDtId").equals("") && invMap.get("ucUploadDtId") != null) {
										session.update(ob1);	
									}
									else
									{	
									session.save(ob1);
									}
							
								}

					}
					
				tx.commit();

			} catch (Exception ex) {

				// System.out.println("Exception e="+ex.);
				ex.printStackTrace();
				tx.rollback();
				System.out.println("Exception Message Print ::" + ex.toString());
				return 0L;
			} finally {

				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}

			return headerFundId;
		}

		@Override
		public Map<String, List<UcUploadHd>> getUcUploadDocumentDetail(JSONObject jsondata) {
	        Map<String, List<UcUploadHd>> map = new HashMap<>();
	        List<UcUploadHd> billDetails = new ArrayList<>();
	        List totalMatches  =new ArrayList<>();
	        int pageNo=0;
	        int pageSize = 5;
	        String fromDate = "";
	        String toDate = "";
	        Criterion c1 = null;
	       if(jsondata.has("fromDate"))
	        {	
	         fromDate = (String) jsondata.get("fromDate");
			 toDate = (String) jsondata.get("toDate");
	        }
	        try {
	            Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	            Criteria criteria = session.createCriteria(UcUploadHd.class);
	            if(jsondata.get("PN").toString() !=null)
	                pageNo = Integer.parseInt(jsondata.get("PN").toString());

	           
	            if (jsondata.has("status") && !jsondata.get("status").toString().trim().isEmpty()) {
	                criteria.add(Restrictions.eq("status","C"));
	            	//criteria.add(Restrictions.or(Restrictions.eq("status", "C"), Restrictions.eq("status", "A")));
	                
	            }
	            if (jsondata.has("districtUser") && !jsondata.get("districtUser").toString().trim().isEmpty()) {
	                criteria.add(Restrictions.eq("upssId",Long.parseLong(jsondata.get("districtUser").toString())));
	            	//criteria.add(Restrictions.or(Restrictions.eq("status", "C"), Restrictions.eq("status", "A")));
	                
	            }
	            if (jsondata.has("financialYear") && !jsondata.get("financialYear").toString().trim().isEmpty()) {
	                criteria.add(Restrictions.eq("financialId",Long.parseLong(jsondata.get("financialYear").toString())));
	            	//criteria.add(Restrictions.or(Restrictions.eq("status", "C"), Restrictions.eq("status", "A")));
	                
	            }
	            if (jsondata.has("phaseVal") && !jsondata.get("phaseVal").toString().trim().isEmpty()) {
	                criteria.add(Restrictions.eq("phase", jsondata.get("phaseVal").toString().trim()));
	            	//criteria.add(Restrictions.or(Restrictions.eq("status", "C"), Restrictions.eq("status", "A")));
	                
	            }
	            if (fromDate != null && !fromDate.equals("") && !fromDate.equals("null") &&
						toDate != null && !toDate.equals("") && !toDate.equals("null")) {
	            	criteria.add(Restrictions.ge("entryDate", formatDate(fromDate)));
	            	criteria.add(Restrictions.le("entryDate", formatDate(toDate)));
				}
	           
	            criteria.addOrder(Order.desc("ucUploadHdId"));
	            totalMatches = criteria.list();
	            if (pageNo > 0) {
	                criteria.setFirstResult((pageSize) * (pageNo - 1));
	                criteria.setMaxResults(pageSize);
	            }
	            billDetails = criteria.list();


	            map.put("fundAllocationDetails", billDetails);
	            map.put("totalMatches", totalMatches);
	        }catch(Exception e) {
	            e.printStackTrace();
	        }finally {
	            getHibernateUtils.getHibernateUtlis().CloseConnection();
	        }
	        return map;
	    }
		
		@Override
		public Map<Long, UcUploadDt> getUcUploadDtDetails(List<Long> ids){
	        Map<Long, UcUploadDt> equipMap = new HashMap<>();
	        if (ids != null && !ids.isEmpty()) {
	            String hql = "Select c from UcUploadDt c where c.ucUploadHdId in(:ids)";
	            Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	            Query query = session.createQuery(hql);
	            query.setParameterList("ids", ids);
	            List<UcUploadDt> li = query.list();
	            equipMap = li.stream().collect(Collectors.toMap(UcUploadDt::getUcUploadDtId, Function.identity()));
	        }
	        return equipMap;
	    }
		
		
		 @Override
			public List<Object[]> getUcUploadDocumentHdDt(Long ucUploadHdId) {
				//List<DgMasInvestigation>  listDgMasInvestigation =null;
				String databaseScema = HMSUtil.getProperties("adt.properties", "currentSchema").trim();
				final String uc_upload_dt=databaseScema+"."+"uc_upload_dt";
				final String uc_upload_hd=databaseScema+"."+"uc_upload_hd";
				Transaction transation=null;
				 List<Object[]> listObject=null;
				try {
				Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				transation=session.beginTransaction();
				 StringBuilder sbQuery = new StringBuilder();
				 
				    sbQuery.append(" select ucHd.uc_upload_hd_id,ucHd.upss_id,ucHd.phase,ucHd.upload_date,ucHd.file_name,ucHd.created_by,ucHd.remarks,ucHd.financial_id,ucHd.entry_date,");
				    sbQuery.append(" ucDt.uc_upload_dt_id,ucDt.head_type_id,ucDt.available_balance,ucDt.available_utilization,ucHd.certificate_no,ucHd.upload_flag from " +uc_upload_hd+ " ucHd "); 
				    sbQuery.append(" left outer join " +uc_upload_dt+ " ucDt on ucHd.uc_upload_hd_id=ucDt.uc_upload_hd_id"); 
				    sbQuery.append(" where ucHd.uc_upload_hd_id =:ucUploadHdId");
				    Query query = session.createSQLQuery(sbQuery.toString());

				    query.setParameter("ucUploadHdId", ucUploadHdId);
				    
				     listObject = query.list();
				transation.commit();
				}
				catch(Exception e) {
					e.printStackTrace();
				}
				finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
				
				}
				return listObject;
			}
			
			@Override
			public Long getCountUCUploadHd(Long upssId,Long financialId,String phaseVal) {
				Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				//List<MasCamp> list = null;
				List searchList = null;
				Long countVal=null;
				try {	
					String Query="select * from uc_upload_hd where upss_id="+upssId+" and phase='"+phaseVal+"' and financial_id="+financialId+"";
					
					if (Query != null) 
					{
						searchList = session.createSQLQuery(Query).list();
						countVal=(long) searchList.size();
						System.out.println("count value : "+countVal);
					} 
					else
					{
						System.out.println("No Record Found");
					}
					return countVal;
					}catch(Exception ex) {
						ex.printStackTrace();
					}finally {
						getHibernateUtils.getHibernateUtlis().CloseConnection();
					}
					return countVal;
				}

			@Override
			public String deleteUCUploadDtDetails(HashMap<String, Object> jsondata) {
				try {
					Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
					Transaction t = session.beginTransaction();
					String ucUploadDtId=jsondata.get("ucUploadDtId").toString();
					
					if(ucUploadDtId!=null)
					{	
					String qryStringHd  ="delete from UcUploadDt v where v.ucUploadDtId='"+ucUploadDtId+"'" ;
					Query queryHd = session.createQuery(qryStringHd);
			        int countHd = queryHd.executeUpdate();
					System.out.println(countHd + " Record(s) ucUploadDtId delete Records Updated.");
					t.commit();
					}
					
					}catch (Exception e) {
						e.printStackTrace();
						return "500";
						// TODO: handle exception
					}
					return "statusUpdated";
				}
			
			@Override
			public boolean isRecordExists(String invoiceNo) {
		        String hql = "SELECT count(*) FROM CaptureVendorBillDetail WHERE invoiceNo = :invoiceNo";
		        Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	            Query query = session.createQuery(hql);
	            query.setParameter("invoiceNo", invoiceNo);
		        Long count = (Long) query.uniqueResult();
		        return count != null && count > 0;
		    }
			
			@Override
			 public Map<Long, Users> readUsers(List<Long> userIds) {
		        Map<Long, Users> usersMap = new HashMap<>();
		        for (Long userId : userIds) {
		            Users user = (Users) read(Users.class, userId);
		            if (user != null) {
		                usersMap.put(userId, user);
		            }
		        }
		        return usersMap;
		    }
			
			@Override
			 public Map<Long, MasMmuVendor> readVendors(List<Long> vendorIds) {
			        Map<Long, MasMmuVendor> vendorsMap = new HashMap<>();
			        for (Long vendorId : vendorIds) {
			            MasMmuVendor vendor = (MasMmuVendor) read(MasMmuVendor.class, vendorId);
			            if (vendor != null) {
			                vendorsMap.put(vendorId, vendor);
			            }
			        }
			        return vendorsMap;
			    }

			@Override
			    // Method to fetch Cities in batch
			    public Map<Long, MasCity> readCities(List<Long> cityIds) {
			        Map<Long, MasCity> citiesMap = new HashMap<>();
			        for (Long cityId : cityIds) {
			            MasCity city = (MasCity) read(MasCity.class, cityId);
			            if (city != null) {
			                citiesMap.put(cityId, city);
			            }
			        }
			        return citiesMap;
			    }
			@Override
			    // Method to fetch Districts in batch
			    public Map<Long, MasDistrict> readDistricts(List<Long> districtIds) {
			        Map<Long, MasDistrict> districtsMap = new HashMap<>();
			        for (Long districtId : districtIds) {
			            MasDistrict district = (MasDistrict) read(MasDistrict.class, districtId);
			            if (district != null) {
			                districtsMap.put(districtId, district);
			            }
			        }
			        return districtsMap;
			    }

			@Override
			public String documentDelete(HashMap<String, Object> jsondata) {
				try {
					Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
					Transaction t = session.beginTransaction();
					String captureVendorSupportingDocsId=jsondata.get("captureVendorSupportingDocsId").toString();
					
					if(captureVendorSupportingDocsId!=null)
					{	
					String qryStringHd  ="delete from VendorInvoicSupportingDocs v where v.captureVendorSupportingDocsId='"+captureVendorSupportingDocsId+"'" ;
					Query queryHd = session.createQuery(qryStringHd);
			        int countHd = queryHd.executeUpdate();
					System.out.println(countHd + " Record(s) ucUploadDtId delete Records Updated.");
					t.commit();
					}
					
					}catch (Exception e) {
						e.printStackTrace();
						return "500";
						// TODO: handle exception
					}
					return "statusUpdated";
				}
@Override			
public List<Object[]> getIncidentReport(String fromDate, String toDate, int mmuId, int vendorId, String levelOfUser, int userId) {
				Transaction transation=null;
				Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				transation=session.beginTransaction();
				Date fd= new Date();
				Date td= new Date();
				try {
					fd = HMSUtil.convertStringTypeDateToDateType(fromDate);
					td = HMSUtil.convertStringTypeDateToDateType(toDate);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        String sql = 
		                "SELECT city.city_name, mmu.mmu_name, CONCAT(" +
		                "        CASE WHEN CEC.penalty_quantity > 0 AND CEC.available_quantity < CEC.assigned_quantity THEN " +
		                "            CONCAT(MEQ.instrument_name, '- ', (CEC.assigned_quantity - CEC.available_quantity), ' quantity is not available. ') " +
		                "        ELSE '' END, " +
		                "        CASE WHEN CEC.penalty_quantity > 0 AND CEC.operational_quantity < CEC.available_quantity THEN " +
		                "            CONCAT(MEQ.instrument_name, '- ', (CEC.available_quantity - CEC.operational_quantity), ' quantity is not Operational.') " +
		                "        ELSE '' END) AS description, " +
		                "    MP.penalty_description, CEC.incident_date, MP.penalty_amount, " +
		                "    (SELECT MMU_NAME FROM MAS_MMU WHERE MMU_ID = :mmuId) AS hospital_name " +
		                "FROM public.capture_equipment_checklist CEC " +
		                "INNER JOIN mas_equipment_checklist MEQ ON MEQ.checklist_id = CEC.equipment_checklist_id " +
		                "INNER JOIN mas_penalty MP ON MP.penalty_id = MEQ.penalty_id " +
		                "INNER JOIN capture_equipment_checklist_details CED ON CEC.capture_equipment_checklist_detail_id = CED.capture_equipment_checklist_detail_id " +
		                "INNER JOIN mas_mmu mmu ON CED.mmu_id = mmu.mmu_id " +
		                "INNER JOIN mas_city city ON city.city_id = mmu.city_id " +
		                "INNER JOIN mas_mmu_vendor mmv ON mmu.mmu_vendor_id = mmv.mmu_vendor_id " +
		                "WHERE CED.inspection_date BETWEEN :fromDate AND :toDate " +
		                "  AND UPPER(CEC.create_incident) = 'Y' " +
		                "  AND (CED.MMU_ID = :mmuId OR :mmuId = 0) " +
		                "  AND (CED.city_id = :cityId OR :cityId = 0) " +
		                "  AND (mmu.mmu_vendor_id = :vendorId OR :vendorId = 0) " +
		                "  AND MMU.MMU_ID IN (" +
		                "      SELECT MMU.MMU_ID " +
		                "      FROM MAS_MMU MMU, (" +
		                "          SELECT to_number(foo, '99999999') IDS " +
		                "          FROM regexp_split_to_table((" +
		                "              SELECT CASE " +
		                "                  WHEN 'M' = :levelOfUser THEN substring(M.MMU_ID, 1, length(M.MMU_ID) - 1) " +
		                "                  WHEN 'C' = :levelOfUser THEN substring(M.CITY_ID, 1, length(M.CITY_ID) - 1) " +
		                "                  WHEN 'D' = :levelOfUser THEN substring(M.DISTRICT_ID, 1, length(M.DISTRICT_ID) - 1) " +
		                "                  ELSE '1' END " +
		                "              FROM USERS M WHERE M.USER_ID = :userId), " +
		                "          E',') AS foo) ALL_ID " +
		                "      WHERE (" +
		                "          (MMU.MMU_ID = ALL_ID.IDS AND 'M' = :levelOfUser) OR " +
		                "          (MMU.CITY_ID = ALL_ID.IDS AND 'C' = :levelOfUser) OR " +
		                "          (MMU.STATE_ID = ALL_ID.IDS AND 'S' = :levelOfUser) OR " +
		                "          (MMU.DISTRICT_ID = ALL_ID.IDS AND 'D' = :levelOfUser))) " +
		                "UNION ALL " +
		                "SELECT city.city_name, mmu.mmu_name, CONCAT(MEQ.checklist_name, '- ', CEC.remarks) AS description, MP.penalty_description, " +
		                "    CEC.incident_date, MP.penalty_amount, " +
		                "    (SELECT MMU_NAME FROM MAS_MMU WHERE MMU_ID = :mmuId) AS hospital_name " +
		                "FROM public.capture_inspection_checklist CEC " +
		                "INNER JOIN mas_inspection_checklist MEQ ON MEQ.checklist_id = CEC.inspection_checklist_id " +
		                "INNER JOIN mas_penalty MP ON MP.penalty_id = MEQ.penalty_id " +
		                "INNER JOIN capture_inspection_details CED ON CEC.capture_inspection_detail_id = CED.capture_inspection_detail_id " +
		                "INNER JOIN mas_mmu mmu ON CED.mmu_id = mmu.mmu_id " +
		                "INNER JOIN mas_city city ON city.city_id = mmu.city_id " +
		                "INNER JOIN mas_mmu_vendor mmv ON mmu.mmu_vendor_id = mmv.mmu_vendor_id " +
		                "WHERE CED.inspection_date BETWEEN :fromDate AND :toDate " +
		                "  AND UPPER(CEC.create_incident) = 'Y' " +
		                "  AND (CED.MMU_ID = :mmuId OR :mmuId = 0) " +
		                "  AND (CED.city_id = :cityId OR :cityId = 0) " +
		                "  AND (mmu.mmu_vendor_id = :vendorId OR :vendorId = 0) " +
		                "  AND MMU.MMU_ID IN (" +
		                "      SELECT MMU.MMU_ID " +
		                "      FROM MAS_MMU MMU, (" +
		                "          SELECT to_number(foo, '99999999') IDS " +
		                "          FROM regexp_split_to_table((" +
		                "              SELECT CASE " +
		                "                  WHEN 'M' = :levelOfUser THEN substring(M.MMU_ID, 1, length(M.MMU_ID) - 1) " +
		                "                  WHEN 'C' = :levelOfUser THEN substring(M.CITY_ID, 1, length(M.CITY_ID) - 1) " +
		                "                  WHEN 'D' = :levelOfUser THEN substring(M.DISTRICT_ID, 1, length(M.DISTRICT_ID) - 1) " +
		                "                  ELSE '1' END " +
		                "              FROM USERS M WHERE M.USER_ID = :userId), " +
		                "          E',') AS foo) ALL_ID " +
		                "      WHERE (" +
		                "          (MMU.MMU_ID = ALL_ID.IDS AND 'M' = :levelOfUser) OR " +
		                "          (MMU.CITY_ID = ALL_ID.IDS AND 'C' = :levelOfUser) OR " +
		                "          (MMU.STATE_ID = ALL_ID.IDS AND 'S' = :levelOfUser) OR " +
		                "          (MMU.DISTRICT_ID = ALL_ID.IDS AND 'D' = :levelOfUser)))";

		        Query query = session.createSQLQuery(sql.toString());
		        query.setParameter("fromDate", fd);
		        query.setParameter("toDate", td);
		        query.setParameter("mmuId", mmuId);
		        query.setParameter("cityId", 0);
		        query.setParameter("vendorId", vendorId);
		        query.setParameter("levelOfUser", levelOfUser);
		        query.setParameter("userId", userId);
		        transation.commit();
		        return query.list();
		    }

			
				
}
