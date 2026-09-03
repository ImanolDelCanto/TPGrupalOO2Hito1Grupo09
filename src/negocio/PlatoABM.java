package negocio;

import dao.PlatoDao;
import datos.Plato;

public class PlatoABM {

    private PlatoDao dao = new PlatoDao();

    public long agregar(Plato p) {
        if (p.getPrecioVenta() <= p.getCostoProduccion())
            throw new IllegalArgumentException(
                    "El precio de venta debe ser mayor al costo de produccion: " + p.getNombre());
        return dao.agregar(p);
    }

    public Plato traer(long idPlato) {
        return dao.traer(idPlato);
    }
}