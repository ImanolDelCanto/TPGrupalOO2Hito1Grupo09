package test;

import org.hibernate.Session;
import dao.HibernateUtil;

/**
 * Prueba que Java pueda conectarse a MySQL a traves de Hibernate.
 * No toca ninguna tabla: solo le pregunta al servidor quien es.
 */
public class TestConexion {

	public static void main(String[] args) {

		System.out.println("Abriendo conexion...");

		Session session = HibernateUtil.getSessionFactory().openSession();

		String version = session.createNativeQuery("SELECT VERSION()")
				.getSingleResult().toString();
		String base = session.createNativeQuery("SELECT DATABASE()")
				.getSingleResult().toString();
		String usuario = session.createNativeQuery("SELECT USER()")
				.getSingleResult().toString();

		session.close();
		HibernateUtil.getSessionFactory().close();

		System.out.println();
		System.out.println("=========================================");
		System.out.println("            CONEXION OK");
		System.out.println("=========================================");
		System.out.println("  Servidor MySQL : " + version);
		System.out.println("  Base de datos  : " + base);
		System.out.println("  Usuario        : " + usuario);
		System.out.println("=========================================");

		System.exit(0);
	}
}
