package br.com.validadorcnab.validadorcnab240;

import br.com.validadorcnab.validadorcnab240.Validador240.LinhaArquivoModel;

class ValidadorTrailerLoteCheque {
	
	private static ValidadorTrailerLoteCheque instance;
	public static ValidadorTrailerLoteCheque getInstance() {
		if (instance == null) {
			instance = new ValidadorTrailerLoteCheque();
		}
		return instance;
	}
	private ValidadorTrailerLoteCheque() {}

	public TraillerLoteCheque tc = null;
	ValidacaoCNAB240  vc = ValidacaoCNAB240.getInstance();
	
	public void validaTrailerLoteCheque(LinhaArquivoModel linha) throws Exception {

		tc = new TraillerLoteCheque(linha.getConteudoLinha());
		validarCodigoBanco(linha);

	}
	
	private void validarCodigoBanco(LinhaArquivoModel linha){
		if(! tc.getCodBanco().equals("756")){
			vc.criarRegistroInvalido(linha, 0, 3, "Trailer Lote Cheque Pos(1) Tam(3) - Código do banco é diferente de '756'");
		}
	}	
}

class TraillerLoteCheque{
	
	private String codBanco;
	private final String linhaSegmentoP;
	
	public TraillerLoteCheque(String linhaSegmentoP){
		this.linhaSegmentoP = linhaSegmentoP;		
		setCodBanco(getParte(0, 3));
	}

	public String getParte(int de, int ate ){
		return this.linhaSegmentoP.substring(de, ate);
	}
	public String getCodBanco() {
		return codBanco;
	}
	public void setCodBanco(String codigoBanco) {
		this.codBanco = codigoBanco;
	}
}
