package dao;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import datos.ItemPedido;
import datos.Pedido;
import datos.UnidadDeVenta;

public class UnidadDeVentaDao {

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

	public long agregar(UnidadDeVenta objeto) {
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

	public void actualizar(UnidadDeVenta objeto) {
		try {
			iniciaOperacion();
			session.update(objeto);
			tx.commit();
		} catch (HibernateException he) {
			manejaExcepcion(he);
			throw he;
		} finally {
			session.close();
		}
	}

	// el <set> es lazy: hay que inicializarlo antes de cerrar la sesion
	public UnidadDeVenta traerUnidadYStaff(long idUnidad) {
		UnidadDeVenta objeto = null;
		try {
			iniciaOperacion();
			String hql = "from UnidadDeVenta u where u.idUnidad = :idUnidad";
			objeto = (UnidadDeVenta) session.createQuery(hql)
					.setParameter("idUnidad", idUnidad).uniqueResult();
			Hibernate.initialize(objeto.getStaff());
		} finally {
			session.close();
		}
		return objeto;
	}
	

	@SuppressWarnings("unchecked")
	public List<UnidadDeVenta> traerTodasConStaff() {
		List<UnidadDeVenta> lista = null;
		try {
			iniciaOperacion();
			String hql = "from UnidadDeVenta u order by u.nombreComercial";
			lista = session.createQuery(hql).getResultList();
			for (UnidadDeVenta u : lista)
				Hibernate.initialize(u.getStaff());
		} finally {
			session.close();
		
		}
		return lista;
	}
	
	@SuppressWarnings("unchecked")
	public List<UnidadDeVenta> traerTodasConPedidos() {
	    List<UnidadDeVenta> lista = null;
	    try {
	        iniciaOperacion();
	        String hql = "from UnidadDeVenta u order by u.nombreComercial";
	        lista = session.createQuery(hql).getResultList();
	        for (UnidadDeVenta u : lista) {
	            Hibernate.initialize(u.getPedidos());
	            for (Pedido p : u.getPedidos()) {
	                Hibernate.initialize(p.getItems());
	                for (ItemPedido i : p.getItems())
	                    Hibernate.initialize(i.getPlato());
	            }
	        }
	    } finally {
	        session.close();
	    }
	    return lista;
	}
	
}
