package cl.pro.music.web.delegate;

import java.util.List;

import cl.pro.music.web.viewmodel.dto.CategoriaDTO;
import cl.pro.music.web.viewmodel.dto.MonedaDTO;
import cl.pro.music.web.viewmodel.dto.PagoFormularioTransDTO;
import cl.pro.music.web.viewmodel.dto.ProductoDTO;
import cl.pro.music.web.viewmodel.dto.VoucherTransaccionDTO;


public interface IMusicProDelegate {

	List<ProductoDTO> getListaProductos();
	List<ProductoDTO> getListaProductosPorCategoria(Integer idCategoria);
	ProductoDTO getProductoPorId(Integer idProducto);
	List<CategoriaDTO> getListaCategoria();
	MonedaDTO getMonedaActualDolar();
	VoucherTransaccionDTO postPagoCompra(PagoFormularioTransDTO pagoCompra);
	


	
}
