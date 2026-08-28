	package test;

import java.util.List;
import org.hibernate.Session;
import dao.HibernateUtil;


public class TestCrearTablas {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {

		System.out.println("Generando el esquema a partir de los mapeos...\n");

		Session session = HibernateUtil.getSessionFactory().openSession();

		List<Object[]> filas = session.createNativeQuery(
				"SELECT TABLE_NAME, TABLE_ROWS FROM information_schema.TABLES "
				+ "WHERE TABLE_SCHEMA = DATABASE() ORDER BY TABLE_NAME").getResultList();

		System.out.println("=========================================");
		System.out.println("  TABLAS EN LA BASE: " + filas.size());
		System.out.println("=========================================");
		for (Object[] f : filas) {
			System.out.println("  - " + f[0]);
		}
		System.out.println("=========================================");

		session.close();
		HibernateUtil.getSessionFactory().close();
		System.exit(0);
	}
}
