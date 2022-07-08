package cl.pro.music.web.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cl.pro.music.web.viewmodel.dto.DolarDTO;
import cl.pro.music.web.viewmodel.dto.MonedaDTO;

public class FuncionesUtils {
	
	public MonedaDTO pruebaMoneda() {
		
		return new MonedaDTO(new DolarDTO(972.23)) ;
	}
	
	public String fechaString() {
		 Date date = Calendar.getInstance().getTime();  
	     DateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy hh:mm:ss");  
	     String strDate = dateFormat.format(date);  
	     return strDate;
	}
}
