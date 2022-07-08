package cl.pro.music.web.viewmodel.dto;

import lombok.Data;

@Data
public class VoucherTransaccionDTO {

	private String idTransaVenta;
	private Integer idVenta;
	private Integer idMoneda;
	private String monedaDescripcion;
	private String tipoPago;
	private String estadoTransaccion;
	private Integer montoVentaDCompra;
	private Integer montoCanceladoVentaDCompra;
	private Integer montoVueltoDineroCompr;
	private PagoFormularioTransDTO datos;
	private String fechaSistemaVenta;
	
	
}
