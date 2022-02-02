package br.com.validadorcnab.validadorcnab240;

import java.util.regex.Pattern;

import br.com.validadorcnab.validadorcnab240.Validador240.LinhaArquivoModel;

class ValidadorTrailerArquivo {
	
	private static ValidadorTrailerArquivo instance;
	
	public static ValidadorTrailerArquivo getInstance() {
		if (instance == null) {
			instance = new ValidadorTrailerArquivo();
		}
		return instance;
	}
	
	private ValidadorTrailerArquivo() {}
	
	
	private TrailerArquivo ta;	
	private final ValidacaoCNAB240 vc = ValidacaoCNAB240.getInstance();
	
	public void validaTrailerArquivo(LinhaArquivoModel linha) throws Exception {

		ta = new TrailerArquivo(linha.getConteudoLinha());
		
		validarCodigoBanco(linha);
		validarLoteServico(linha);

	}
	
	private void validarLoteServico(LinhaArquivoModel linha) {		
		if(! Pattern.matches("9999", ta.getLoteServico())){
		    vc.criarRegistroInvalido(linha, 3, 7, "Trailer de Arquivo Pos(4) Tam(4) - Lote é diferente de '9999'");
		}		 
	}
	
	private void validarCodigoBanco(LinhaArquivoModel linha){
		if(! ta.getCodBanco().equals("756")){
			vc.criarRegistroInvalido(linha, 0, 3, "Trailer de arquivo Pos(1) Tam(3) - Código do banco é diferente de '756'");
		}
	}	
}

class TrailerArquivo{
	
	
	private String codBanco;
	private String loteServico;
	private final String trailerArquivo;
	
	public TrailerArquivo(String trailerArquivo){
		this.trailerArquivo = trailerArquivo;
		
		setCodBanco(getParte(0, 3));
		setLoteServico(getParte(3, 7));
	}
	
	public String getLoteServico() {
		return loteServico;
	}

	public void setLoteServico(String loteServico) {
		this.loteServico = loteServico;
	}

	public String getParte(int de, int ate ){
		return this.trailerArquivo.substring(de, ate);
	}
	public String getCodBanco() {
		return codBanco;
	}
	public void setCodBanco(String codigoBanco) {
		this.codBanco = codigoBanco;
	}
	
	
	
}
