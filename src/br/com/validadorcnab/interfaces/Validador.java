package br.com.validadorcnab.interfaces;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import br.com.validadorcnab.model.RegistroInvalidoArquivo;

public interface Validador {
	
	boolean isErroEstrutura();
	Integer getNumeroLinha();
	String getMsgErroEstrutura();
	List<RegistroInvalidoArquivo> getListaRegistroInvalido();
	void validarConteudoLinhas() throws Exception;
	void lerArquivoValidandoSequencia( BufferedReader arquivo) throws IOException;
	
}
