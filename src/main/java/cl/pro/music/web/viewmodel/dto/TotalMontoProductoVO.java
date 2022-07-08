package cl.pro.music.web.viewmodel.dto;

import lombok.Data;

@Data
public class TotalMontoProductoVO {

	private Integer cantidadProducto;
	private Integer subTotal;
	private Integer descuento;
	private Integer totalConDescuento;
	
}
