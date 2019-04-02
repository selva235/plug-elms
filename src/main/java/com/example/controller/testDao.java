//package com.example.controller;
//
//import javax.persistence.EntityManager;
//
//import org.hibernate.HibernateException;
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Repository;
//
//@Repository("testDao")
//public class testDao {
//	
//
//	
//	@Autowired
//	protected SessionFactory sessionFactory;
//
//	public SessionFactory getSessionFactory() {
//		return sessionFactory;
//	}
//
//	public void setSessionFactory(SessionFactory sessionFactory) {
//		this.sessionFactory = sessionFactory;
//	}
//
//	
//	protected Object saveOrUpdate(test obj) {
//		
//		System.out.println("<<<<<<<???????????????????");
//		
//		Session session =sessionFactory.openSession();
//		try {
//			session.saveOrUpdate(obj);
//			session.flush();
//		}catch(HibernateException e) {
//			e.printStackTrace();
//		}finally {
//			session.close();
//		}
//		return obj;
//		
//	}
//
//	
//	
//}
