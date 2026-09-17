package dao;

import java.time.LocalDate;
import java.util.List;
import org.hibernate.Hibernate;
import datos.Cajero;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import datos.Cocinero;
import datos.Personal;

public class PersonalDao {

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

	public long agregar(Personal objeto) {
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

	public Personal traer(long idPersonal) {
		Personal objeto = null;
		try {
			iniciaOperacion();
			objeto = session.get(Personal.class, idPersonal);
		} finally {
			session.close();
		}
		return objeto;
	}

	@SuppressWarnings("unchecked")
	public List<Personal> traerTodos() {
		List<Personal> lista = null;
		try {
			iniciaOperacion();
			lista = session.createQuery("from Personal p order by p.apellido").getResultList();
		} finally {
			session.close();
		}
		return lista;
	}

	@SuppressWarnings("unchecked")
	public List<Cocinero> traerCocinerosPorEspecialidad(String especialidad) {
		List<Cocinero> lista = null;
		try {
			iniciaOperacion();
			String hql = "from Cocinero c where c.especialidad = :esp order by c.apellido";
			lista = session.createQuery(hql).setParameter("esp", especialidad).getResultList();
		} finally {
			session.close();
		}
		return lista;
	}
	
	@SuppressWarnings("unchecked")
	public List<Cajero> traerCajerosPorTurnoUnidadYFestival(String turno, int antiguedadMinima, String temporada) {
		List<Cajero> lista = null;
		try {
			iniciaOperacion();
			// la antiguedad no es una columna, pero si es una resta de fechas:
			// calculamos el limite y lo pasamos como parametro, asi filtra la base
			LocalDate limite = LocalDate.now().minusYears(antiguedadMinima);
			// cruza cajero + unidadDeVenta + foodTruck + festival en una sola consulta.
			// type(u) pregunta por la subclase real, que es lo que antes se hacia
			// con un instanceof despues de traer todo
			String hql = "select c from Cajero c "
					+ "inner join fetch c.unidad u "
					+ "inner join fetch u.festival f "
					+ "where c.turno = :turno "
					+ "and type(u) = FoodTruck "
					+ "and c.fechaIngreso <= :limite "
					+ "and f.temporada = :temporada "
					+ "order by c.apellido";
			lista = session.createQuery(hql)
					.setParameter("turno", turno)
					.setParameter("limite", limite)
					.setParameter("temporada", temporada)
					.getResultList();
		} finally {
			session.close();
		}
		return lista;
	}
}
