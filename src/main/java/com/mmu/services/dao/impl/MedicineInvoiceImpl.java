package com.mmu.services.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mmu.services.dao.MedicineInvoiceDao;
import com.mmu.services.dto.MedicineInvoiceDetails;
import com.mmu.services.dto.MedicineInvoiceList;
import com.mmu.services.entity.CaptureMedicineInvoiceDetails;
import com.mmu.services.hibernateutils.GetHibernateUtils;
import com.mmu.services.utils.HMSUtil;

@Repository
@Transactional
public class MedicineInvoiceImpl implements MedicineInvoiceDao{

	@Autowired
	GetHibernateUtils getHibernateUtils;
	
	@Override
	public long saveMedicineInvoice(CaptureMedicineInvoiceDetails medicineInvoiceDetails) {
		    Long result=null;
	        Transaction tx=null;
	        Session session=null;
	        try {
	            session = getHibernateUtils.getHibernateUtlis().OpenSession();
	            tx = session.beginTransaction();
	            result =  (Long) session.save(medicineInvoiceDetails);
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
	public List<MedicineInvoiceList> findAllInvoiceList() {
		List<MedicineInvoiceList>  list = new ArrayList<>();
        try {
        	StringBuilder sb = new StringBuilder();
        	sb.append("select mi.invoice_date,mi.invoice_no,mi.invoice_amount,mi.bill_year ,mi.bill_month, ")
        	.append("md.district_name,mc.city_name,msst.supplier_type_name  FROM medicine_invoice mi ")
        	.append("left join mas_district md on mi.district_id=md.district_id ")
        	.append("left join mas_city mc on mi.city_id =mc.city_id ")
        	.append("left join mas_store_supplier_type msst on mi.supplier_type_id = msst.supplier_type_id");
       
        	
        	 Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
        	 Transaction tx = session.beginTransaction();
        	 
        	
        	 Query query = session.createSQLQuery(sb.toString())
        			 .setResultTransformer(Transformers.aliasToBean(MedicineInvoiceList.class));
        	 List<MedicineInvoiceList> medicineInvoiceList = query.list();
        	 return medicineInvoiceList;
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            getHibernateUtils.getHibernateUtlis().CloseConnection();
        }
        return list;
	}
	


	@Override
	public Map<String,Object> findAllInvoiceList(Map<String, Object> params) {
		List<MedicineInvoiceList>  list = new ArrayList<>();
		Map<String, Object> map = new HashMap<String,Object>();
		int pageNo=1;
		int pageSize = 5;
        try {
        	StringBuilder sb = new StringBuilder();
        	sb.append("select mi.invoice_date,mi.invoice_no,mi.invoice_amount,mi.bill_year ,mi.bill_month, mi.medicine_invoice_id ,mi.batch_no ,mi.action as status ,")
        	.append("md.district_name,mc.city_name,msst.supplier_type_name  FROM medicine_invoice mi ")
        	.append("left join mas_district md on mi.district_id=md.district_id ")
        	.append("left join mas_city mc on mi.city_id =mc.city_id ")
        	.append("left join mas_store_supplier_type msst on mi.supplier_type_id = msst.supplier_type_id");
        	
        	if(params.containsKey("PN")) {
        		//pageNo = (int) params.get("PN");
        		pageNo = Integer.parseInt(params.get("PN").toString());
        	}
        
        	
        	if(params.size()>0) {
        		StringBuilder sbBuilder = new StringBuilder();
        		if(params.containsKey("cityIdUser") &&  !params.get("cityIdUser").equals("")) {
        			sbBuilder.append("mi.user_city_id='"+params.get("cityIdUser").toString()+"'");
            	}
        		if(params.containsKey("vendorName") && !params.get("vendorName").equals("")){
        			if(sbBuilder.toString().length()>0)
        			{
        				sbBuilder.append(" and ");
        			}
        			sbBuilder.append("mi.supplier_type_id="+params.get("vendorName").toString());
        		}
        		if(params.containsKey("toDate") && !params.get("toDate").equals("")) {
        			if(sbBuilder.toString().length()>0)
        			{
        				sbBuilder.append(" and ");
        			}
        			sbBuilder.append(" mi.invoice_date <= '"+HMSUtil.convertStringFormat(params.get("toDate").toString()) +"'");
        		}
        		if(params.containsKey("fromDate") && !params.get("fromDate").equals("")) {
        			if(sbBuilder.toString().length()>0)
        			{
        				sbBuilder.append(" and ");
        			}
        			sbBuilder.append("mi.invoice_date   >= '"+HMSUtil.convertStringFormat(params.get("fromDate").toString())+"'");        			
        		}
        		if(sbBuilder.toString().trim().length()>0) {
        			sb.append(" where ").append(sbBuilder.toString());
        		}
        	}
        	
        	sb.append(" order by mi.invoice_date desc");
        	
        	 
        	
        	 Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
        	 Transaction tx = session.beginTransaction();
        	 
        	
        	 Query query = session.createSQLQuery(sb.toString())
        			 .setResultTransformer(Transformers.aliasToBean(MedicineInvoiceList.class));
        	 List<MedicineInvoiceList> totalList = query.list();
        	 query.setFirstResult((pageSize) * (pageNo - 1));
        	 query.setMaxResults(pageSize);
        	 List<MedicineInvoiceList> medicineInvoiceList = query.list();
        	 
        	 
        	map.put("medicineList", medicineInvoiceList);
     		map.put("totalMatches", totalList.size());
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            getHibernateUtils.getHibernateUtlis().CloseConnection();
        }
        return map;
	}

	@Override
	public Integer lastBatch() {
		Integer result = null;
		Session session = null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session
				    .createCriteria(CaptureMedicineInvoiceDetails.class)
				    .setProjection(Projections.max("batchNo"));
				result = (Integer)criteria.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(session!=null)
				session.close();
		}
		return result;
	}

	@Override
	public List<CaptureMedicineInvoiceDetails> findCaptureInvoiceList(Long Id) {
		  List<CaptureMedicineInvoiceDetails>  list = new ArrayList<>();
     
        	Integer batchNo = null;
    		Session session = null;
    		try {
    			session = getHibernateUtils.getHibernateUtlis().OpenSession();
    			list = session.createCriteria(CaptureMedicineInvoiceDetails.class).add(Restrictions.eq("medicineInvoiceId", Id)).list();
    			batchNo = list.get(0).getBatchNo();
    			list = session.createCriteria(CaptureMedicineInvoiceDetails.class).add(Restrictions.eq("batchNo", batchNo)).list();

    		} catch (Exception e) {
    			e.printStackTrace();
    		} finally {
    			getHibernateUtils.getHibernateUtlis().CloseConnection();
    		}

        return list;
	}
	
	@Override
	public List<CaptureMedicineInvoiceDetails> findCaptureInvoiceByBatch(Integer batchNo) {
		List<CaptureMedicineInvoiceDetails>  list = new ArrayList<>();
		Session session = null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			list = session.createCriteria(CaptureMedicineInvoiceDetails.class).add(Restrictions.eq("batchNo", batchNo)).list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

    return list;
	}
	
	public List<Long> getInvoiceId(Integer batchNo){
		
		
		Session session = null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria crit = session.createCriteria(CaptureMedicineInvoiceDetails.class);
			crit.add(Restrictions.eq("batchNo", batchNo));
			ProjectionList projList = Projections.projectionList();
			projList.add(Projections.property("medicineInvoiceId"));
			crit.setProjection(projList);
			List<Long> results = crit.list();
			return results;
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		getHibernateUtils.getHibernateUtlis().CloseConnection();
	}
		return null;
		
	}

	@Override
	public Long updateMedicineInvoice(CaptureMedicineInvoiceDetails medicineInvoiceDetails,Long id) {
		   Long result=(long) 1;
	        Transaction tx=null;
	        Session session=null;
	        try {
	            session = getHibernateUtils.getHibernateUtlis().OpenSession();
	            tx = session.beginTransaction();
	            
	            CaptureMedicineInvoiceDetails invoiceDetails = (CaptureMedicineInvoiceDetails) session.get(CaptureMedicineInvoiceDetails.class, id);
	           
	            invoiceDetails.setDistrictId(medicineInvoiceDetails.getDistrictId());
				invoiceDetails.setCityId(medicineInvoiceDetails.getCityId());
				invoiceDetails.setBillYear(medicineInvoiceDetails.getBillYear());
				invoiceDetails.setBillMonth(medicineInvoiceDetails.getBillMonth());
				
				invoiceDetails.setSupplierTypeId(medicineInvoiceDetails.getSupplierTypeId());
				invoiceDetails.setSupplierId(medicineInvoiceDetails.getSupplierId());
				invoiceDetails.setInvoiceDate(medicineInvoiceDetails.getInvoiceDate());
				invoiceDetails.setInvoiceNumber(medicineInvoiceDetails.getInvoiceNumber());
				invoiceDetails.setInvoiceAmount(medicineInvoiceDetails.getInvoiceAmount());
				
				invoiceDetails.setAction(medicineInvoiceDetails.getAction());
				invoiceDetails.setLastChgBy(medicineInvoiceDetails.getLastChgBy());
				invoiceDetails.setLastChangeDate(medicineInvoiceDetails.getLastChangeDate());
				invoiceDetails.setBatchNo(medicineInvoiceDetails.getBatchNo());
				if(medicineInvoiceDetails.getInvoiceDoc()!= null && !medicineInvoiceDetails.getInvoiceDoc().isEmpty()) {
					invoiceDetails.setInvoiceDoc(medicineInvoiceDetails.getInvoiceDoc());
				}
				invoiceDetails.setHeadTypeId(medicineInvoiceDetails.getHeadTypeId());
				invoiceDetails.setPhase(medicineInvoiceDetails.getPhase());
				 session.update(invoiceDetails);
	            tx.commit();
	            session.flush();
	        }catch(Exception e) {
	            e.printStackTrace();
	            result = (long) 0;
	        }finally {
	            getHibernateUtils.getHibernateUtlis().CloseConnection();
	        }

	        return result;

	}

	@Override
	public Integer deleteCaptureInvoice(Long Id) {
		   Session session=null;
		   Transaction tx=null;
		   Integer result=1;
	        try {
	            session = getHibernateUtils.getHibernateUtlis().OpenSession();
	            tx = session.beginTransaction();
	            CaptureMedicineInvoiceDetails invoiceDetails = (CaptureMedicineInvoiceDetails) session.get(CaptureMedicineInvoiceDetails.class, Id);
	            session.delete(invoiceDetails);   
	            tx.commit();
	            session.flush();
	        }catch(Exception e) {
	            e.printStackTrace();
	            result =0;
	        }finally {
	            getHibernateUtils.getHibernateUtlis().CloseConnection();
	        }
	        return result;
	}

	@Override
	public Object getInvoiceReports(Map<String, Object> params) {

		   Session session=null;
		   Transaction tx=null;
		   List<Object[]> result=null;
	        try {
	            session = getHibernateUtils.getHibernateUtlis().OpenSession();
	            tx = session.beginTransaction();
	            Query query = session.createSQLQuery(
	            		"CALL asp_invoice_dashboard(:fromDate,:toDate)")
	            		
	            		.setParameter("fromDate", "7277");
	        }catch(Exception e) {
	            e.printStackTrace();
	           
	        }finally {
	            getHibernateUtils.getHibernateUtlis().CloseConnection();
	        }
	        return result;
	
	}

	@Override
	public Map<String,Object> findAllInvoiceListBasedOnGroup(Map<String, Object> params) {

		List<MedicineInvoiceDetails>  list = new ArrayList<>();
		Map<String, Object> map = new HashMap<String,Object>();
		int pageNo=1;
		int pageSize = 5;
        try {
        	StringBuilder sb = new StringBuilder();
        	sb.append("select mi.batch_no,mi.bill_month ,mi.bill_year,CAST(mi.last_chg_date AS DATE),mi.\"action\" as status ,md.upss as district_name,mc.city_name,mi.phase  from medicine_invoice mi ")
        	.append("left join mas_district md on mi.district_id=md.district_id ")
        	.append("left join mas_city mc on mi.city_id =mc.city_id ");
        
        	
        	if(params.containsKey("PN")) {
        		
        		pageNo = Integer.parseInt(params.get("PN").toString());
        	}
        
        	
        	if(params.size()>0) {
        		StringBuilder sbBuilder = new StringBuilder();
				/*
				 * if(params.containsKey("cityIdUser") && !params.get("cityIdUser").equals(""))
				 * {
				 * sbBuilder.append("mi.user_city_id='"+params.get("cityIdUser").toString()+"'")
				 * ; }
				 */
        		if(params.containsKey("vendorName") && !params.get("vendorName").equals("")){
        			if(sbBuilder.toString().length()>0)
        			{
        				sbBuilder.append(" and ");
        			}
        			sbBuilder.append("mi.district_id="+params.get("vendorName").toString());
        		}
        		if(params.containsKey("toDate") && !params.get("toDate").equals("")) {
        			if(sbBuilder.toString().length()>0)
        			{
        				sbBuilder.append(" and ");
        			}
        			sbBuilder.append(" mi.invoice_date <= '"+HMSUtil.convertStringFormat(params.get("toDate").toString()) +"'");
        		}
        		if(params.containsKey("fromDate") && !params.get("fromDate").equals("")) {
        			if(sbBuilder.toString().length()>0)
        			{
        				sbBuilder.append(" and ");
        			}
        			sbBuilder.append("mi.invoice_date   >= '"+HMSUtil.convertStringFormat(params.get("fromDate").toString())+"'");        			
        		}
        		if(params.containsKey("phase") && !params.get("phase").equals("")) {
        			if(sbBuilder.toString().length()>0)
        			{
        				sbBuilder.append(" and ");
        			}
        			sbBuilder.append("mi.phase   = '"+ params.get("phase").toString().trim()+"'");        			
        		}
        		
        		if(sbBuilder.toString().trim().length()>0) {
        			sb.append(" where ").append(sbBuilder.toString());
        			sb.append(" and mi.head_type_id =" +Long.parseLong(params.get("headTypeId").toString()));
        		}
        	
        		
        		
        		if(sbBuilder.toString().trim().length()==0) {
                	sb.append(" where  mi.head_type_id =" +Long.parseLong(params.get("headTypeId").toString()));
                	}
        	
        		
        	
        	}
        	
        	sb.append(" GROUP BY mi.batch_no,mi.bill_month,mi.bill_year,mi.last_chg_date,mi.\"action\" ,md.upss,mc.city_name ,mi.phase  order by mi.last_chg_date desc");
        	
        	 
        	
        	 Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
        	 Transaction tx = session.beginTransaction();
        	 
        	
        	 Query query = session.createSQLQuery(sb.toString())
        			 .setResultTransformer(Transformers.aliasToBean(MedicineInvoiceDetails.class));
        	 List<MedicineInvoiceList> totalList = query.list();
        	 query.setFirstResult((pageSize) * (pageNo - 1));
        	 query.setMaxResults(pageSize);
        	 List<MedicineInvoiceDetails> medicineInvoiceList = query.list();
        	 
        	 
        	map.put("medicineList", medicineInvoiceList);
     		map.put("totalMatches", totalList.size());
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            getHibernateUtils.getHibernateUtlis().CloseConnection();
        }
        return map;
	
	}

	@Override
	public Integer getInvoiceData(Integer districtId, Integer cityId, Integer month, Integer year,
			Integer supplierTypeId, String invoiceNumber) {

		
		
		Session session = null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria crit = session.createCriteria(CaptureMedicineInvoiceDetails.class);
			Conjunction andExp = Restrictions.and(
			Restrictions.eq("districtId", districtId),
			Restrictions.eq("cityId", cityId),
			Restrictions.eq("billMonth", month),
			Restrictions.eq("billYear", year),
			Restrictions.eq("supplierTypeId", supplierTypeId),
			Restrictions.eq("invoiceNumber", invoiceNumber));
		
			crit.add(andExp);
			List<Long> results = crit.list();
			return results.size();
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		getHibernateUtils.getHibernateUtlis().CloseConnection();
	}
		return 0;
		
	
	}

	

}
