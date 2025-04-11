package com.mmu.services.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mmu.services.dao.GenericDao;

@SuppressWarnings("unchecked")
@Repository
public abstract class GenericDaoImpl<E, K extends Serializable> 
        implements GenericDao<E, K> {
	
    @Autowired
    private SessionFactory sessionFactory;
    protected Session session;
     
    protected Class<? extends E> daoType;
     
    /**
    * By defining this class as abstract, we prevent Spring from creating 
    * instance of this class If not defined as abstract, 
    * getClass().getGenericSuperClass() would return Object. There would be 
    * exception because Object class does not hava constructor with parameters.
    */
    public GenericDaoImpl() {
        Type t = getClass().getGenericSuperclass();
        ParameterizedType pt = (ParameterizedType) t;
        daoType = (Class) pt.getActualTypeArguments()[0];
    }
     
    protected Session currentSession() {
        return sessionFactory.getCurrentSession();
    }
     
    @Override
    @Transactional
    public void add(E entity) {
        currentSession().save(entity);
    }
     
    @Override
    @Transactional
    public void saveOrUpdate(E entity) {
        currentSession().saveOrUpdate(entity);
    }
     
    @Override
    @Transactional
    public void update(E entity) {
        currentSession().saveOrUpdate(entity);
    }
     
    @Override
    @Transactional
    public void remove(E entity) {
        currentSession().delete(entity);
    }
     
    @Override
    @Transactional
    public E find(K key) {
        return (E) currentSession().get(daoType, key);
    }
     
    @Override
    @Transactional
    public List<E> getAll() {
        return currentSession().createCriteria(daoType).list();
    }
  /*  @Override
    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")*/
    @Override
    @Transactional
    public List<E> findByCriteria(Criterion... criterion) {
    	Criteria crit =currentSession().createCriteria(daoType);
      //Criteria crit = getSession().createCriteria(getPersistentClass());
      for (Criterion c : criterion) {
        crit.add(c);
      }
      return crit.list();
    }
    @Override
    @Transactional
    public List<E> findWithLimit(Order order, int startPos, int limit, Criterion... criterion) {
    	Criteria crit =currentSession().createCriteria(daoType);
        for (Criterion c : criterion) {
          crit.add(c);
        }
        crit.setFirstResult(startPos);
        crit.setMaxResults(limit);

        if (order != null) {
          crit.addOrder(order);
        }

        return crit.list();
      }
    
    @Override
    @Transactional
	public List<E> findByLeftJoinCriteria(String alias,String aliasName,Order order, int startPos, int limit,  Criterion... criterion) {
    	Criteria crit =currentSession().createCriteria(daoType);
        crit.createAlias(alias, aliasName,  JoinType.LEFT_OUTER_JOIN);

        for (Criterion c : criterion) {
        	if(c!=null)
        		crit.add(c);
        }
        crit.setFirstResult(startPos);
        crit.setMaxResults(limit);

        if (order != null) {
          crit.addOrder(order);
        }
        return crit.list();
      }
    @Override
    @Transactional
   	public List<E> findByLeftJoinCriteria2(String alias,String aliasName,Order order, int startPos, int limit,  Criterion... criterion) {
       	Criteria crit =currentSession().createCriteria(daoType);
           crit.createAlias(alias, alias,  JoinType.LEFT_OUTER_JOIN);
           crit.createAlias(aliasName, aliasName,  JoinType.LEFT_OUTER_JOIN);
           for (Criterion c : criterion) {
             crit.add(c);
           }
           crit.setFirstResult(startPos);
           crit.setMaxResults(limit);

           if (order != null) {
             crit.addOrder(order);
           }
           return crit.list();
         }

    
    public Session getSession() {
        if (this.session == null || !this.session.isOpen() || this.session.isConnected()) {
          this.session = SessionFactoryUtils.getSession(sessionFactory, Boolean.FALSE);
        }
        this.applyFilter(session);
        return this.session;

      }
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	public void setSession(Session session) {
		this.session = session;
	}
	private String[] filterName;
	  private String[] paramName;
	  private String[] paramValue;

	  public void applyFilterOn(String[] filterName, String[] paramName, String[] paramValue) {
	    this.filterName = filterName;
	    this.paramName = paramName;
	    this.paramValue = paramValue;
	  }


	  public void applyFilter(Session session) {
	    Filter filter = null;
	    int i = 0;
	    if (filterName != null) {
	      for (String filterName : this.filterName) {
	        filter = session.enableFilter(filterName);
	        filter.setParameter(paramName[i], paramValue[i]);
	        i++;
	      }
	    }
	  }
	  
	  
	    @Override
	    @Transactional(readOnly=false)
	   	public List<E> findByLeftJoinCriteriaWithMultipleAlias(String alias,String aliasn,Order order, int startPos, int limit,  Criterion... criterion) {
	       	Criteria crit =currentSession().createCriteria(daoType);
	       	String [] aliasMul=alias.split(",");
	       	String [] aliasMulnb=aliasn.split(",");
	       	int count=0;
	       	for(String aliass:aliasMul) {
	       		
	       		crit.createAlias(aliass, aliasMulnb[count],  JoinType.LEFT_OUTER_JOIN);
	       		count++;
	       	}
	           for (Criterion c : criterion) {
	        	  if(c!=null)
	             crit.add(c);
	           }
	           if(limit!=0) {
	           crit.setFirstResult(startPos);
	           crit.setMaxResults(limit);
	           }
	           if (order != null) {
	             crit.addOrder(order);
	           }
	           return crit.list();
	         }

	  
	  
	  
	  
}