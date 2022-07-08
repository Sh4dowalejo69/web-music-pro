package cl.pro.music.web.service;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.pro.music.web.delegate.IMusicProDelegate;
import cl.pro.music.web.utils.Constantes;
import cl.pro.music.web.utils.FuncionesUtils;
import cl.pro.music.web.viewmodel.dto.CategoriaDTO;
import cl.pro.music.web.viewmodel.dto.DatosCompradorDTO;
import cl.pro.music.web.viewmodel.dto.DetalleVentaVO;
import cl.pro.music.web.viewmodel.dto.MonedaDTO;
import cl.pro.music.web.viewmodel.dto.PagoFormularioTransDTO;
import cl.pro.music.web.viewmodel.dto.ProdDTO;
import cl.pro.music.web.viewmodel.dto.ProductoDTO;
import cl.pro.music.web.viewmodel.dto.TotalMontoProductoVO;
import cl.pro.music.web.viewmodel.dto.VoucherTransaccionDTO;
import cl.pro.music.web.viewmodel.model.PagoFormulario;

@Service
public class MusicProServiceImpl implements IMusicProService{
	
	@Autowired
	private IMusicProDelegate iMusicProDelegate;

	@Override
	public List<ProductoDTO> getListaDeProductos() {
		return iMusicProDelegate.getListaProductos();
	}

	@Override
	public List<CategoriaDTO> getListaCategorias() {
		return iMusicProDelegate.getListaCategoria();
	}

	@Override
	public List<ProductoDTO> getListaDeProductosPorCategoria(Integer idCategoria) {
		return iMusicProDelegate.getListaProductosPorCategoria(idCategoria);
	}

	@Override
	public ProductoDTO getProductoPorId(Integer idProducto) {
		return iMusicProDelegate.getProductoPorId(idProducto);
	}

	@Override
	public DetalleVentaVO agregarCarrito(List<ProductoDTO> listaCarrito, ProductoDTO producto,DetalleVentaVO detalleVenta,MonedaDTO moneda) {
		listaCarrito.add(producto);	
		detalleVenta.setCarritoSession(listaCarrito);
		detalleVenta.setProducto(obtenerProductoEnMap(listaCarrito));
		detalleVenta.setMontoPorProducto(obtenerTotalDetalleVenta(detalleVenta,listaCarrito));
		sumasDetalleVenta(detalleVenta,moneda);
		return detalleVenta;
	}
	
	private Map<Integer, ProductoDTO> obtenerProductoEnMap(List<ProductoDTO> listaCarrito){
		return listaCarrito.stream().collect(Collectors.toMap(ProductoDTO::getIdProducto, data-> data, (data1, data2)-> {return data1;}));
	}
	
	private Map<Integer, TotalMontoProductoVO> obtenerTotalDetalleVenta(DetalleVentaVO detalleVenta, List<ProductoDTO> listaCarrito){
		Map<Integer, TotalMontoProductoVO> detalleProductos = new TreeMap<>();
		for(Map.Entry<Integer,ProductoDTO> entry : detalleVenta.getProducto().entrySet()) {
			TotalMontoProductoVO montoProducto = new TotalMontoProductoVO();
			Integer totalProductos = obtenerCantidadProducto(listaCarrito, entry.getValue().getIdProducto()).intValue();
			montoProducto.setCantidadProducto(totalProductos);
			montoProducto.setSubTotal(entry.getValue().getValorProducto()*totalProductos);
			montoProducto.setDescuento((entry.getValue().getValorProducto()*totalProductos) * entry.getValue().getDescuentoProductoPorc()/100);
			montoProducto.setTotalConDescuento(montoProducto.getSubTotal() - montoProducto.getDescuento());
			detalleProductos.put(entry.getKey(), montoProducto);
		}
		return detalleProductos;
	};


	private Long obtenerCantidadProducto(List<ProductoDTO> listaCarrito, Integer idProducto) {
		return listaCarrito.stream().filter(producto->producto.getIdProducto().equals(idProducto)).count();		
	}
	
	private void sumasDetalleVenta(DetalleVentaVO detalleVentaVO,MonedaDTO moneda) {
		Integer subTotalProductos = detalleVentaVO.getMontoPorProducto().entrySet().stream().collect(Collectors.summingInt(producto->producto.getValue().getSubTotal()));
		Integer descuentoTotalProductos = detalleVentaVO.getMontoPorProducto().entrySet().stream().collect(Collectors.summingInt(producto->producto.getValue().getDescuento()));
		detalleVentaVO.setSubTotalProductos(subTotalProductos);
		detalleVentaVO.setDescuentos(descuentoTotalProductos);
		detalleVentaVO.setTotalCompraProductos(subTotalProductos-descuentoTotalProductos); 
		if(moneda == null) {
			moneda =  new FuncionesUtils().pruebaMoneda();//getMonedaActual();
		}
		detalleVentaVO.setValorFinalDolares(detalleVentaVO.getTotalCompraProductos() / moneda.getDolar().getValor());
		Double valorEnDolares = (double) Math.round(detalleVentaVO.getValorFinalDolares() * 100) / 100;
		detalleVentaVO.setValorFinalDolares(valorEnDolares);
	}

	@Override
	public void removerProducto(DetalleVentaVO detalleVenta, Integer idProducto, MonedaDTO moneda) {
		List<ProductoDTO> lista = detalleVenta.getCarritoSession().stream().filter(producto-> !producto.getIdProducto().equals(idProducto)).collect(Collectors.toList());
		detalleVenta.setCarritoSession(lista);
		detalleVenta.getProducto().remove(idProducto);
		detalleVenta.getMontoPorProducto().remove(idProducto);
		sumasDetalleVenta(detalleVenta,moneda);
	}

	@Override
	public MonedaDTO getMonedaActual() {
		
		return iMusicProDelegate.getMonedaActualDolar();
	}

	@Override
	public VoucherTransaccionDTO pagoProducto(PagoFormulario pagoFormulario, DetalleVentaVO detalleVentaVO) {
		PagoFormularioTransDTO productoDTO = convertObj(pagoFormulario, detalleVentaVO);
		VoucherTransaccionDTO voucher = iMusicProDelegate.postPagoCompra(productoDTO);
		voucher.setDatos(productoDTO);
		voucher.setFechaSistemaVenta(new FuncionesUtils().fechaString());
		return voucher;
	};
	
	private PagoFormularioTransDTO convertObj(PagoFormulario pagoFormulario,DetalleVentaVO detalleVentaVO){
		
		PagoFormularioTransDTO producto = new PagoFormularioTransDTO();
		producto.setCodMoneda(Constantes.PESOS_CLP);
		producto.setCodPago(Integer.parseInt(pagoFormulario.getMetodoPago()));
		producto.setCodNumeroTarjeta(pagoFormulario.getNumeroTarjeta() == null  || pagoFormulario.getNumeroTarjeta().equalsIgnoreCase("") ? 0:Long.parseLong(pagoFormulario.getNumeroTarjeta()));
		producto.setDatosComprador(convertDatosComprador(pagoFormulario));
		producto.setMontoPagadoComprador(detalleVentaVO.getTotalCompraProductos());
		List<ProdDTO> listaProdDTO =  detalleVentaVO.getMontoPorProducto().entrySet().stream().map(produ->new ProdDTO(produ.getKey(),produ.getValue().getCantidadProducto())).collect(Collectors.toList());
		producto.setListaProductos(listaProdDTO);
		return producto;
	}
	
	private DatosCompradorDTO convertDatosComprador(PagoFormulario pagoFormulario) {
		DatosCompradorDTO comprador = new DatosCompradorDTO();
		comprador.setRut(Integer.parseInt(pagoFormulario.getRut()));
		comprador.setDvRut(pagoFormulario.getDvRut());
		comprador.setNombreComp(pagoFormulario.getNombre());
		comprador.setSegNombreComp(pagoFormulario.getSegundoNombre());
		comprador.setApellidoPComp(pagoFormulario.getApellidoPaterno());
		comprador.setApellidoMComp(pagoFormulario.getApellidoMaterno());
		comprador.setDireccionComp(pagoFormulario.getDireccion() +"-"+pagoFormulario.getCiudad());
		return comprador;
	}
	
}
