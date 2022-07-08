package cl.pro.music.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import cl.pro.music.web.delegate.MusicProDelegateImpl;
import cl.pro.music.web.service.IMusicProService;
import cl.pro.music.web.utils.Constantes;
import cl.pro.music.web.utils.FuncionesUtils;
import cl.pro.music.web.viewmodel.dto.CategoriaDTO;
import cl.pro.music.web.viewmodel.dto.DetalleVentaVO;
import cl.pro.music.web.viewmodel.dto.MonedaDTO;
import cl.pro.music.web.viewmodel.dto.ProductoDTO;
import cl.pro.music.web.viewmodel.dto.VoucherTransaccionDTO;
import cl.pro.music.web.viewmodel.model.PagoFormulario;

@Controller
public class MusicProControllerImpl implements IMusicProController{
	private Logger log = LoggerFactory.getLogger(MusicProControllerImpl.class);
	private IMusicProService iMusicProServices;
	
	MusicProControllerImpl(IMusicProService iMusicProServices) {
		this.iMusicProServices = iMusicProServices;	
	}
	
	@GetMapping("/")
	public String index(HttpServletRequest request) {
		HttpSession session = request.getSession();
		MonedaDTO moneda = (MonedaDTO) session.getAttribute(Constantes.SESSION_MONEDA);
		if(moneda == null) {
			moneda = iMusicProServices.getMonedaActual();
			session.setAttribute(Constantes.SESSION_MONEDA, moneda);
		}
		return "index";
	}

	@GetMapping("/catalogo")
	public String catalogo(HttpServletRequest request,Model model) {
		List<ProductoDTO> productos = null;
		List<CategoriaDTO> categorias = null;
		try {
			productos = iMusicProServices.getListaDeProductos();
			categorias = iMusicProServices.getListaCategorias();
		}catch(Exception e){
			e.printStackTrace();
		}
		model.addAttribute(Constantes.TEMPLATE_LISTA_PRODUCTOS, productos);
		model.addAttribute(Constantes.TEMPLATE_LISTA_CATEGORIA, categorias);
		return "catalogo";
	}

	@GetMapping("/carritoCompras")
	public String carrito(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession(); 
		if(session == null ) {
			session = request.getSession(true);
		}
		DetalleVentaVO detalleVenta = null;
		try {
			detalleVenta = (DetalleVentaVO) session.getAttribute(Constantes.SESSION_DETALLE_VENTA);
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute(Constantes.TEMPLATE_DETALLE_VENTA,detalleVenta);
		
		return "carrito";
	}

	@Override
	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@Override
	@GetMapping("/catalogo/categoriaFiltro/{idCategoria}")
	public String categoriaFiltro(@PathVariable Integer idCategoria,Model model) {
		List<ProductoDTO> productos = null;
		List<CategoriaDTO> categorias = null;
		try {
			productos = iMusicProServices.getListaDeProductosPorCategoria(idCategoria);
			categorias = iMusicProServices.getListaCategorias();
		}catch(Exception e){
			e.printStackTrace();
		}
		model.addAttribute(Constantes.TEMPLATE_LISTA_PRODUCTOS, productos);
		model.addAttribute(Constantes.TEMPLATE_LISTA_CATEGORIA, categorias);
		return "catalogo";
	}

	@SuppressWarnings("unchecked")
	@GetMapping("/agregarCarrito/{idProducto}")
	public String agregarCarrito(@PathVariable Integer idProducto, HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		
		if(session == null ) {
			log.info("entra aqui ya que la session es nula - añadir carrito");
			session = request.getSession(true);
		}
		
		ProductoDTO producto = null;
		List<ProductoDTO> listaCarrito = null;
		DetalleVentaVO detalleVenta = null;
		MonedaDTO moneda = null;
		try {
			producto = iMusicProServices.getProductoPorId(idProducto);
			listaCarrito = (List<ProductoDTO>) session.getAttribute(Constantes.SESSION_LISTA_PRODUCT_CARRITO);
			detalleVenta = (DetalleVentaVO) session.getAttribute(Constantes.SESSION_DETALLE_VENTA);
			
			if(listaCarrito == null || detalleVenta == null) {
				log.info("Son valores nulos");
				detalleVenta = iMusicProServices.agregarCarrito(new ArrayList<ProductoDTO>(), producto,new DetalleVentaVO(),moneda);
			}else {
				log.info("Son Hay valores en session ");
				detalleVenta = iMusicProServices.agregarCarrito(listaCarrito, producto,detalleVenta,moneda);
			}
			session.setAttribute(Constantes.SESSION_LISTA_PRODUCT_CARRITO, detalleVenta.getCarritoSession());
			session.setAttribute(Constantes.SESSION_DETALLE_VENTA, detalleVenta);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		model.addAttribute(Constantes.TEMPLATE_DETALLE_VENTA,detalleVenta);
		return "carrito";
	}

	@GetMapping("/removerCarrito/{idProducto}")
	public String removerCarrito(@PathVariable Integer idProducto, HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		DetalleVentaVO detalleVenta = null;
		MonedaDTO moneda = null;
		try {
			detalleVenta = (DetalleVentaVO) session.getAttribute(Constantes.SESSION_DETALLE_VENTA);
			moneda = (MonedaDTO) session.getAttribute(Constantes.SESSION_MONEDA);
			iMusicProServices.removerProducto(detalleVenta,idProducto,moneda);
			session.setAttribute(Constantes.SESSION_LISTA_PRODUCT_CARRITO, detalleVenta.getCarritoSession());
			session.setAttribute(Constantes.SESSION_DETALLE_VENTA, detalleVenta);
		}catch(Exception e) {
			e.printStackTrace();
		}
		model.addAttribute(Constantes.TEMPLATE_DETALLE_VENTA,detalleVenta);
		return "redirect:/carritoCompras";
	}
	
	@GetMapping("/aPagar")
	public String aPagar(HttpServletRequest request,Model model) {
		
		HttpSession session = request.getSession();
		DetalleVentaVO detalleVenta = null;
		try {
			detalleVenta = (DetalleVentaVO) session.getAttribute(Constantes.SESSION_DETALLE_VENTA);
		}catch(Exception e) {
			e.printStackTrace();
		}
		model.addAttribute(Constantes.TEMPLATE_DETALLE_VENTA,detalleVenta);
		return "formularioPago";
	}
	
	
	@PostMapping("/procesandoPago")
	public String procesandoPago(HttpServletRequest request,PagoFormulario pagoFormulario, Model model) {
		HttpSession session = request.getSession();
		DetalleVentaVO detalleVenta = null;
		VoucherTransaccionDTO voucher = null;
		try {
			detalleVenta = (DetalleVentaVO) session.getAttribute(Constantes.SESSION_DETALLE_VENTA);
			voucher = iMusicProServices.pagoProducto(pagoFormulario,detalleVenta);
		}catch(Exception e) {
			e.printStackTrace();
		}
		model.addAttribute(Constantes.TEMPLATE_DETALLE_VENTA,detalleVenta);
		model.addAttribute(Constantes.TEMPLATE_VOUCHER,voucher);
		session.setAttribute(Constantes.SESSION_DETALLE_VENTA, null);
		session.setAttribute(Constantes.SESSION_LISTA_PRODUCT_CARRITO,null);
		return "pagoAprobado";
	}

}
