package cl.pro.music.web.service;

import java.util.List;

import cl.pro.music.web.viewmodel.dto.CategoriaDTO;
import cl.pro.music.web.viewmodel.dto.DetalleVentaVO;
import cl.pro.music.web.viewmodel.dto.MonedaDTO;
import cl.pro.music.web.viewmodel.dto.ProductoDTO;
import cl.pro.music.web.viewmodel.dto.VoucherTransaccionDTO;
import cl.pro.music.web.viewmodel.model.PagoFormulario;


public interface IMusicProService {
	
	List<ProductoDTO> getListaDeProductos();
	List<ProductoDTO> getListaDeProductosPorCategoria(Integer idCategoria);
	List<CategoriaDTO> getListaCategorias();
	ProductoDTO getProductoPorId(Integer idProducto);
	DetalleVentaVO agregarCarrito(List<ProductoDTO> listaCarrito, ProductoDTO producto,DetalleVentaVO detalleVenta, MonedaDTO moneda);
	MonedaDTO getMonedaActual();
	void removerProducto(DetalleVentaVO detalleVenta, Integer idProducto, MonedaDTO moneda);
	VoucherTransaccionDTO pagoProducto(PagoFormulario pagoFormulario,DetalleVentaVO detalleVentaVO);
	
	
}
