package test;

import java.util.List;

import org.hibernate.Session;

import dao.HibernateUtil;
import datos.Cajero;
import datos.Cocinero;
import datos.Personal;
import datos.UnidadDeVenta;

// Verifica la conexion con MySQL y los mapeos. Al levantar la SessionFactory,
// hbm2ddl.auto=update genera el esquema a partir de los .hbm.xml; despues
// consulta cada entidad: si alguna tabla no se creo bien, la consulta falla.
public class TestConexion {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {

		System.out.println("Abriendo conexion y generando el esquema...\n");

		Session session = HibernateUtil.getSessionFactory().openSession();

		List<UnidadDeVenta> unidades = session.createQuery("from UnidadDeVenta").getResultList();
		List<Personal> personal = session.createQuery("from Personal").getResultList();
		List<Cocinero> cocineros = session.createQuery("from Cocinero").getResultList();
		List<Cajero> cajeros = session.createQuery("from Cajero").getResultList();

		session.close();
		HibernateUtil.getSessionFactory().close();

		System.out.println("=========================================");
		System.out.println("            CONEXION OK");
		System.out.println("=========================================");
		System.out.println("  UnidadDeVenta : " + unidades.size());
		System.out.println("  Personal      : " + personal.size());
		System.out.println("  Cocinero      : " + cocineros.size());
		System.out.println("  Cajero        : " + cajeros.size());
		System.out.println("=========================================");

		System.exit(0);
	}
}
