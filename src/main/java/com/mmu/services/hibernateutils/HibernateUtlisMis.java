package com.mmu.services.hibernateutils;

import java.sql.Connection;
import java.sql.SQLException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class HibernateUtlisMis {

	@Autowired
	@Qualifier("sessionFactory2")
	protected SessionFactory factory;

	ThreadLocal<Session> threadlocal = new ThreadLocal<Session>();

	public HibernateUtlisMis() {
		System.out.println("HibernateUtlisMis intilize time");
	}

	public Session OpenSession() {
		Session session = null;
		if (threadlocal.get() == null) {
			session = factory.openSession();
			threadlocal.set(session);
		} else {
			session = threadlocal.get();
			if (!session.isOpen()) {
				threadlocal.remove();
				session = factory.openSession();
				threadlocal.set(session);
			}
		}

		return session;

	}

	public void CloseConnection() {
		if (threadlocal.get() != null) {
			threadlocal.get().clear();
			threadlocal.get().close();
			threadlocal.remove();
		}
	}

	public Session getCurrentSession() {
		return factory.getCurrentSession();

	}

	public Connection getConnection() {
		Connection conn = null;
		try {
			conn = factory.getSessionFactoryOptions().getServiceRegistry().getService(ConnectionProvider.class)
					.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}

}
