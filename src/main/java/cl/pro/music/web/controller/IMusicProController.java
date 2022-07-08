package cl.pro.music.web.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

public interface IMusicProController {

	String login();
	
	String categoriaFiltro(@PathVariable Integer idCategoria,Model model); 
	
	
	
}
