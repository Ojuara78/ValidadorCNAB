package br.com.validadorcnab.validadorCnab400;

import java.util.regex.Pattern;
import br.com.validadorcnab.validadorCnab400.Validador400.LinhaArquivoModel;


public class ValidadorTrailerArquivo400 {
	
	private static ValidadorTrailerArquivo400 instance;
	public static ValidadorTrailerArquivo400 getInstance() {
		if (instance == null) {
			instance = new ValidadorTrailerArquivo400();
		}
		return instance;
	}
	private ValidadorTrailerArquivo400() {}
	
	
	public TrailerArquivo400 t = null;
	
	ValidacaoCNAB400  vc = ValidacaoCNAB400.getInstance();
	
	public void  ValidaTrailerArquivo(LinhaArquivoModel linha) {

		t = new TrailerArquivo400(linha);
		
		validaIDRegistro();
		validaSequencialRegistro();
		

	}
	
	private void validaSequencialRegistro() {
		if(!vc.isNumeric(t.getSeqRegistro()))
			vc.criarRegistroInvalido(t.getLinha(), 394, 400, vc.getMapaOcorrencia().get("AH"));
	}
	private void validaIDRegistro() {
		if(isDiferente("9",t.getIdentRegistro()))
			vc.criarRegistroInvalido(t.getLinha(), 1, 1, vc.getMapaOcorrencia().get("02"));
	}
	
	private boolean isDiferente(String regexp, String valor){
		return !Pattern.matches(regexp, valor);
	}
}
class TrailerArquivo400{
	
	private String identRegistro ;
	private String Filler1;
	private String SeqRegistro;

	private final LinhaArquivoModel trailerArquivo400;
	
	public TrailerArquivo400(LinhaArquivoModel trailerArquivo400){
		this.trailerArquivo400 = trailerArquivo400;
		
		setIdentRegistro(getParte(0, 1));
		setFiller1(getParte(1, 393));
		setSeqRegistro(getParte(394, 400));
		
	}

	public String getIdentRegistro() {
		return identRegistro;
	}

	public void setIdentRegistro(String identRegistro) {
		this.identRegistro = identRegistro;
	}

	public String getFiller1() {
		return Filler1;
	}

	public void setFiller1(String filler1) {
		Filler1 = filler1;
	}

	public String getSeqRegistro() {
		return SeqRegistro;
	}

	public void setSeqRegistro(String seqRegistro) {
		SeqRegistro = seqRegistro;
	}
	
	public String getParte(int de, int ate ){
		return this.trailerArquivo400.getConteudoLinha().substring(de, ate);
	}
	public LinhaArquivoModel getLinha() {
		return trailerArquivo400;
	}
	
	
	
}
