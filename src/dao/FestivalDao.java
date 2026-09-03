package dao;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import datos.Festival;

public class FestivalDao {

	private static Session session;
	private Transaction tx;

	private void iniciaOperacion() throws HibernateException {
		session = HibernateUtil.getSessionFactory().openSession();
		tx = session.beginTransaction();
	}

	private void manejaExcepcion(HibernateException he) throws HibernateException {
		tx.rollback();
		throw new HibernateException("ERROR en la capa de acceso a datos", he);
	}

	public long agregar(Festival objeto) {
		long id = 0;
		try {
			iniciaOperacion();
			id = Long.parseLong(session.save(objeto).toString());
			tx.commit();
		} catch (HibernateException he) {
			manejaExcepcion(he);
			throw he;
		} finally {
			session.close();
		}
		return id;
	}

	public Festival traer(long idFestival) {
		Festival objeto = null;
		try {
			iniciaOperacion();
			objeto = session.get(Festival.class, idFestival);
		} finally {
			session.close();
		}
		return objeto;
	}

	@SuppressWarnings("unchecked")
	public List<Festival> traerTodos() {
		List<Festival> lista = null;
		try {
			iniciaOperacion();
			lista = session.createQuery("from Festival f order by f.fechaInicio").getResultList();
		} finally {
			session.close();
		}
		return lista;
	}

	// el <set> de unidades es lazy: hay que inicializarlo antes de cerrar la sesion
	@SuppressWarnings("unchecked")
	public List<Festival> traerTodosConUnidades() {
		List<Festival> lista = null;
		try {
			iniciaOperacion();
			String hql = "from Festival f order by f.fechaInicio";
			lista = session.createQuery(hql).getResultList();
			for (Festival f : lista)
				Hibernate.initialize(f.getUnidades());
		} finally {
			session.close();
		}
		return lista;
	}

	// consulta con parametros: los festivales de un rango de fechas
	@SuppressWarnings("unchecked")
	public List<Festival> traerPorRango(java.time.LocalDate desde, java.time.LocalDate hasta) {
		List<Festival> lista = null;
		try {
			iniciaOperacion();
			String hql = "from Festival f where f.fechaInicio >= :desde and f.fechaFin <= :hasta order by f.fechaInicio";
			lista = session.createQuery(hql).setParameter("desde", desde).setParameter("hasta", hasta).getResultList();
		} finally {
			session.close();
		}
		return lista;
	}
}
