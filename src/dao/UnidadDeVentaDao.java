package dao;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import datos.ItemPedido;
import datos.Pedido;
import datos.PuestoDesarmable;
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
	

	// Cruza puestoDesarmable + unidadDeVenta + festival + personal en un solo HQL.
	// El filtro por festival y por tiempo de montaje lo resuelve la base no Java.
	@SuppressWarnings("unchecked")
	public List<PuestoDesarmable> traerPuestosPorTiempoDeMontaje(String festival, int minutos) {
		List<PuestoDesarmable> lista = null;
		try {
			iniciaOperacion();
			String hql = "select distinct p from PuestoDesarmable p "
					+ "inner join fetch p.festival f "
					+ "left join fetch p.staff "
					+ "where f.nombre = :festival "
					+ "and p.tiempoMontaje <= :minutos "
					+ "order by p.tiempoMontaje";
			lista = session.createQuery(hql)
					.setParameter("festival", festival)
					.setParameter("minutos", minutos)
					.getResultList();
		} finally {
			session.close();
		}
		return lista;
	}

	@SuppressWarnings("unchecked")
	public List<UnidadDeVenta> traerTodasConStaff() {
		List<UnidadDeVenta> lista = null;
		try {
			iniciaOperacion();
			// left join fetch: trae unidades y staff en UNA consulta. Sin esto
			// Hibernate hace una consulta extra por cada unidad (problema N+1).
			// Es left y no inner para no perder las unidades sin staff.
			String hql = "select distinct u from UnidadDeVenta u "
					+ "left join fetch u.staff "
					+ "order by u.nombreComercial";
			lista = session.createQuery(hql).getResultList();
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
