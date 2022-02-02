package br.com.validadorcnab.validadorcnab240;

import java.util.Arrays;
import java.util.regex.Pattern;

import br.com.validadorcnab.model.SegmentoSModel;
import br.com.validadorcnab.validadorcnab240.Validador240.LinhaArquivoModel;


class ValidadorSegmentoS {
	
	public static ValidadorSegmentoS instance;
	public static ValidadorSegmentoS getInstance() {
		if(instance==null){
			return new ValidadorSegmentoS();
		}
		return instance;
	}
	
	private ValidadorSegmentoS() {}
	
	public SegmentoSModel segmentoS = null;
	
	
	private final ValidacaoCNAB240 validador = ValidacaoCNAB240.getInstance();
	
	public void validaSegmentoS(LinhaArquivoModel linha) throws Exception {
		
		segmentoS = new SegmentoSModel(linha.getConteudoLinha());
		validarCodigoBanco(linha);
		validarCodigoMovimento(linha);
		validaTipoImpressao(linha);
	}
	
	private void validaTipoImpressao(LinhaArquivoModel linha) {
		
		if(!Pattern.matches("1|2|3", segmentoS.getTipoImpressao() )){
			validador.criarRegistroInvalido(linha,17,18,"Seg(S) Pos(18) Tam(1) - Tipo de impressão é diferente de '1','2' e '3'");
		}
	}

	private void validarCodigoMovimento(LinhaArquivoModel linha) {
		//C004	Código de Movimento Remessa manual febraban pagina 146
		Integer[] arrayMovimento = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,30,31,33,34,35,40,41,42,43,44,45,46};
		try {
			if(!Arrays.asList(arrayMovimento).contains(Integer.valueOf(segmentoS.getCodoMovimento()))){
				validador.criarRegistroInvalido(linha,15,17,"Seg(Q) Pos(16) Tam(2) - Código do movimento é inválido'");
			}

		} catch (Exception e) {
			validador.criarRegistroInvalido(linha,15,17,"Seg(Q) Pos(16) Tam(2) -  Código do movimento é inválido'");
		}		
	}
	
	private void validarCodigoBanco(LinhaArquivoModel linha){
		if(! segmentoS.getCodBanco().equals("756")){
			validador.criarRegistroInvalido(linha, 0, 3, "Seg(S) Pos(1) Tam(3) - Código do banco é diferente de '756'");
		}
	}	
}


