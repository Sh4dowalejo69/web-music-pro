package cl.pro.music.web.viewmodel.dto;

import java.util.List;

import lombok.Data;

@Data
public class PagoFormularioTransDTO {
	
	private Integer codMoneda;
	private Long codNumeroTarjeta;
	private Integer codPago;
	private DatosCompradorDTO datosComprador;
	private List<ProdDTO> listaProductos;
	private Integer montoPagadoComprador;
}
