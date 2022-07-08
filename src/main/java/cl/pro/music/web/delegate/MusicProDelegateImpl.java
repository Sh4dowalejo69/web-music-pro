package cl.pro.music.web.delegate;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import cl.pro.music.web.utils.Constantes;
import cl.pro.music.web.viewmodel.dto.CategoriaDTO;
import cl.pro.music.web.viewmodel.dto.MonedaDTO;
import cl.pro.music.web.viewmodel.dto.PagoFormularioTransDTO;
import cl.pro.music.web.viewmodel.dto.ProductoDTO;
import cl.pro.music.web.viewmodel.dto.VoucherTransaccionDTO;

@Service
public class MusicProDelegateImpl implements IMusicProDelegate {
	private Logger log = LoggerFactory.getLogger(MusicProDelegateImpl.class);

	@Value("${url-api-rest-music-pro}")
	private String urlApiRestMusicPro;

	@Value("${url-api-mi-indicador-dolar}")
	private String urlApiRestMiIndicador;

	@Autowired
	private RestTemplate restTemplate;

	@Override
	public List<ProductoDTO> getListaProductos() {
		String ruta = new StringBuilder().append(urlApiRestMusicPro).append(Constantes.GET_LISTA_PRODUCTOS).toString();
		System.out.println(ruta);
		ResponseEntity<List<ProductoDTO>> productos = null;
		try {
			productos = restTemplate.exchange(ruta, HttpMethod.GET, null,
					new ParameterizedTypeReference<List<ProductoDTO>>() {
					});

			if (productos.getStatusCode() == HttpStatus.OK) {
				log.info("Obtiene de lista de productos exitosamente");
				return productos.getBody();
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<CategoriaDTO> getListaCategoria() {
		String ruta = new StringBuilder().append(urlApiRestMusicPro).append(Constantes.GET_LISTA_CATEGORIA).toString();
		ResponseEntity<List<CategoriaDTO>> categorias = null;
		try {
			categorias = restTemplate.exchange(ruta, HttpMethod.GET, null,
					new ParameterizedTypeReference<List<CategoriaDTO>>() {
					});

			if (categorias.getStatusCode() == HttpStatus.OK) {
				log.info("Obtiene categorias de productos");
				return categorias.getBody();
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<ProductoDTO> getListaProductosPorCategoria(Integer idCategoria) {
		String ruta = new StringBuilder().append(urlApiRestMusicPro).append(Constantes.GET_LISTA_PRODUCTOS_POR_CATE)
				.append(idCategoria).toString();
		ResponseEntity<List<ProductoDTO>> productos = null;
		try {
			productos = restTemplate.exchange(ruta, HttpMethod.GET, null,
					new ParameterizedTypeReference<List<ProductoDTO>>() {
					});

			if (productos.getStatusCode() == HttpStatus.OK) {
				return productos.getBody();
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ProductoDTO getProductoPorId(Integer idProducto) {
		String ruta = new StringBuilder().append(urlApiRestMusicPro).append(Constantes.GET_PRODUCTO_POR_ID)
				.append(idProducto).toString();
		ResponseEntity<ProductoDTO> producto = null;
		try {
			producto = restTemplate.getForEntity(ruta, ProductoDTO.class);

			if (producto.getStatusCode() == HttpStatus.OK) {
				log.info("Obtene producto por id Exitosamente");
				return producto.getBody();
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public MonedaDTO getMonedaActualDolar() {
		// TODO Auto-generated method stub
		String ruta = new StringBuilder().append(urlApiRestMusicPro).append(Constantes.GET_MONEDA_USD_ACTUAL).toString();
		ResponseEntity<MonedaDTO> moneda = null;
		try {
			moneda = restTemplate.getForEntity(ruta, MonedaDTO.class);

			if (moneda.getStatusCode() == HttpStatus.OK) {
				log.info("Obtiene el dolar actual exitosamente");
				return moneda.getBody();
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public VoucherTransaccionDTO postPagoCompra(PagoFormularioTransDTO pagoCompra) {
		String ruta = new StringBuilder().append(urlApiRestMusicPro).append(Constantes.POST_PAGO_COMPRAS).toString();
		ResponseEntity<VoucherTransaccionDTO> transaccion = null;
		try {
			transaccion = restTemplate.postForEntity(ruta,pagoCompra, VoucherTransaccionDTO.class);

			if (transaccion.getStatusCode() == HttpStatus.OK) {
				System.out.println("Fue exitoso");
				System.out.println(transaccion.getBody());
				return transaccion.getBody();
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;
	}
}
