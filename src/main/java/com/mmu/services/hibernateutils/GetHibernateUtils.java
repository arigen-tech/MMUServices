package com.mmu.services.hibernateutils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GetHibernateUtils {

	private HibernateUtlis hibernateUtlis;

	@Autowired
	public GetHibernateUtils(HibernateUtlis hibernateUtlis) {
		this.hibernateUtlis = hibernateUtlis;

	}

	public GetHibernateUtils() {

	}

	public HibernateUtlis getHibernateUtlis() {
		return hibernateUtlis;
	}

}
