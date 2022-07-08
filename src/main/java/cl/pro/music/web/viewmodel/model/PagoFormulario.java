package cl.pro.music.web.viewmodel.model;

import lombok.Data;

@Data
public class PagoFormulario {

	private String rut;
	private String dvRut;
	private String nombre;
	private String segundoNombre;
	private String apellidoPaterno;
	private String apellidoMaterno;
	private String email;
	private String direccion;
	private String ciudad;
	private String metodoPago;
	private String nombreTitular;
	private String numeroTarjeta;
	private String fechaExpiracion;
	private String codigoCvv;
	private String montoDinero;
	
}
