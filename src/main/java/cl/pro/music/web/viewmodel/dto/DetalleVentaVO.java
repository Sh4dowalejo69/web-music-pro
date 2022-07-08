package cl.pro.music.web.viewmodel.dto;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class DetalleVentaVO {

	private List<ProductoDTO> carritoSession;
	private Map<Integer, ProductoDTO> producto;
	private Map<Integer, TotalMontoProductoVO> montoPorProducto;
	private Integer totalCompraProductos;
	private Integer subTotalProductos;
	private Integer descuentos;
	private Double valorFinalDolares;
}
