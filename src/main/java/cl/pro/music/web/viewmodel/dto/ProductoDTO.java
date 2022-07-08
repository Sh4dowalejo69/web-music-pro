package cl.pro.music.web.viewmodel.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class ProductoDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer idProducto;
	private String tituloProducto;
	private String descripcionProducto;
	private Integer descuentoProductoPorc;
	private Integer cantidadProducto;
	private Integer valorProducto;
	private String urlImagen;
	private Integer idCategoria;
	private Integer idMarca;
}
