package com.mmu.services.hibernateutils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GetHibernateUtilsMis {

	private HibernateUtlisMis hibernateUtlisMis;

	@Autowired
	public GetHibernateUtilsMis(HibernateUtlisMis hibernateUtlisMis) {
		this.hibernateUtlisMis = hibernateUtlisMis;

	}

	public GetHibernateUtilsMis() {

	}

	public HibernateUtlisMis getHibernateUtlisMiss() {
		return hibernateUtlisMis;
	}

}
