package br.com.validadorcnab.interfaces;

import br.com.validadorcnab.model.RegistroInvalidoArquivo;

import java.awt.*;
import java.util.HashMap;
import java.util.List;

public interface FrontendInterface {
	
	void setCursor(Cursor predefinedCursor);
	void setupTableDataArquivo(HashMap<String, List<RegistroInvalidoArquivo>> mapaDeArquivos);
	
}
