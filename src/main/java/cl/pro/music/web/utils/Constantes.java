package cl.pro.music.web.utils;

public class Constantes {

	//Metodos Para Api externas
	public static final String GET_LISTA_PRODUCTOS ="/listaProductos";
	public static final String GET_LISTA_CATEGORIA ="/listaCategoria";
	public static final String GET_LISTA_PRODUCTOS_POR_CATE = "/listaProductosPorCategoria/";
	public static final String GET_PRODUCTO_POR_ID = "/productoFindById/";
	public static final String GET_MONEDA_USD_ACTUAL = "/getDolarActual";
	public static final String POST_PAGO_COMPRAS = "/pagoProductos";	
	//Atributos para templates
	public static final String TEMPLATE_LISTA_PRODUCTOS ="listaProductos";
	public static final String TEMPLATE_LISTA_CATEGORIA ="listaCategoria";
	public static final String TEMPLATE_LISTA_CARRITO ="listaCarrito";
	public static final String TEMPLATE_DETALLE_VENTA ="detalleVentaCarrito";
	public static final String TEMPLATE_VOUCHER ="voucher";
	
	//Variables en session
	public static final String SESSION_LISTA_PRODUCT_CARRITO = "carrito";
	public static final String SESSION_DETALLE_VENTA = "detalleVenta";
	public static final String SESSION_MONEDA = "moneda";
	
	public static final Integer PESOS_CLP = 2;
	
}
