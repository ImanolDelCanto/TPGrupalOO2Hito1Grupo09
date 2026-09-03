package negocio;

import dao.PedidoDao;
import datos.Pedido;

public class PedidoABM {

    private PedidoDao dao = new PedidoDao();

    // guarda el pedido: los items se persisten en cascada (ver Pedido.hbm.xml)
    public long agregar(Pedido p) {
        if (p.getItems().isEmpty())
            throw new IllegalArgumentException("Un pedido debe tener al menos un item");
        return dao.agregar(p);
    }
}