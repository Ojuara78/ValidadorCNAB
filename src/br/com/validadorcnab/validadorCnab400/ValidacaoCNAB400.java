package br.com.validadorcnab.validadorCnab400;

import java.io.IOException;


public class ValidacaoCNAB400 extends Validador400 {
	
	private static ValidacaoCNAB400 instance;
	
	public static ValidacaoCNAB400 getInstance(){
		if (instance == null) {
			instance = new ValidacaoCNAB400();
		}
		return instance;
	}
	
	private ValidacaoCNAB400(){
		
	}

	@Override
	public void validarConteudoLinhas() throws IOException {
		
		for (LinhaArquivoModel linha : listaLinhasArquivo) {
			switch (linha.getTipoLinha()) {
			case Header_de_Arquivo400:
				ValidadorHeaderArquivo400.getInstance().ValidaHeaderArquivo400(linha);
				break;
			case Detalhe_Titulo_CNAB400:
				ValidadorDetalhe400.getInstance().ValidadorDetalhe(linha);
				break;
			case Trailer_de_Arquivo400:
				ValidadorTrailerArquivo400.getInstance().ValidaTrailerArquivo(linha);
				break;
			default:
				System.out.println("Tipo de linha não tratada");
				break;
			}
		}
	}
}

