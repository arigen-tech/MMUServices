package com.mmu.services.dao;

import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;
@Repository
public interface GenericDao<E,K> {
    public void add(E entity) ;
    public void saveOrUpdate(E entity) ;
    public void update(E entity) ;
    public void remove(E entity);
    public E find(K key);
    public List<E> getAll() ;
    public List<E> findByCriteria(Criterion... criterion);
	List<E> findWithLimit(Order order, int startPos, int limit, Criterion... criterion);
	List<E> findByLeftJoinCriteria(String alias, String aliasName, Order order, int startPos, int limit,
			Criterion... criterion);
	List<E> findByLeftJoinCriteria2(String alias, String aliasName, Order order, int startPos, int limit,
			Criterion... criterion);
 
	List<E> findByLeftJoinCriteriaWithMultipleAlias(String alias, String aliasn, Order order, int startPos, int limit,
			Criterion[] criterion);
}