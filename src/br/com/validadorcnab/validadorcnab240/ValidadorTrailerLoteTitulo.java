package br.com.validadorcnab.validadorcnab240;

import br.com.validadorcnab.validadorcnab240.Validador240.LinhaArquivoModel;

class ValidadorTrailerLoteTitulo {
	
	private static ValidadorTrailerLoteTitulo instance;
	public static ValidadorTrailerLoteTitulo getInstance() {
		if (instance == null) {
			instance = new ValidadorTrailerLoteTitulo();
		}
		return instance;
	}
	private ValidadorTrailerLoteTitulo() {}
	
	public TraillerLoteTitulo tt = null;
	ValidacaoCNAB240  vc = ValidacaoCNAB240.getInstance();
	
	public void validaTrailerLoteTitulo(LinhaArquivoModel linha) throws Exception {
		tt = new TraillerLoteTitulo(linha.getConteudoLinha());
		validarCodigoBanco(linha);
	}
	
	private void validarCodigoBanco(LinhaArquivoModel linha){
		if(! tt.getCodBanco().equals("756")){
			vc.criarRegistroInvalido(linha, 0, 3, "Trailer Lote Título Pos(1) Tam(3) - Código do banco é diferente de '756'");
		}
	}
	
}

class TraillerLoteTitulo{	
	
	private String codBanco;
	private final String traillerLoteTitulo;
	
	public TraillerLoteTitulo(String linhaSegmentoP){
		this.traillerLoteTitulo = linhaSegmentoP;		
		setCodBanco(getParte(0, 3));
	}
	public String getParte(int de, int ate ){
		return this.traillerLoteTitulo.substring(de, ate);
	}
	public String getCodBanco() {
		return codBanco;
	}
	public void setCodBanco(String codigoBanco) {
		this.codBanco = codigoBanco;
	}
	
}
