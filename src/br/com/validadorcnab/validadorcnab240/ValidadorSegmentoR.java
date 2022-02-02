package br.com.validadorcnab.validadorcnab240;

import java.util.Arrays;
import java.util.regex.Pattern;

import br.com.validadorcnab.model.SegmentoPModel;
import br.com.validadorcnab.model.SegmentoRModel;
import br.com.validadorcnab.util.DateUtil;
import br.com.validadorcnab.validadorcnab240.Validador240.LinhaArquivoModel;


class ValidadorSegmentoR {
	
	public static ValidadorSegmentoR instance;
	
	public static ValidadorSegmentoR getInstance() {
		if(instance==null){
			return new ValidadorSegmentoR();
		}
		return instance;
	}
	
	private ValidadorSegmentoR() {}
	
	private final SegmentoPModel segP = ValidadorSegmentoP.getInstance().getSegP();
	private SegmentoRModel segR;
	private ValidacaoCNAB240  vc = ValidacaoCNAB240.getInstance();
	
	public void validaSegmentoR(LinhaArquivoModel linha) {

			segR = new SegmentoRModel(linha.getConteudoLinha());

			validarCodigoBanco(linha);
			
			validarCodigoMovimento(linha);
			
			validarCodigoDesconto2(linha);
			
			validarCodigoDesconto2ComDataInvalidada(linha);
			
			validarValorDesconto2(linha);
			
			validarCodigoDesconto3(linha);
			
			validarCodigoDesconto3ComDataInvalidada(linha);
			
			validarValorDesconto3(linha);
			
			validaCodMulta(linha);
			
			validaDataMulta(linha);
			
			validaDataMultaMenorQueDataVencimento(linha);
			
			validaValorMulta(linha);
			
			validaValorMultaParaModalidade(linha);
			
			validaAgenciaDebitoMantenedora(linha);
			
			validaContaCorrenteDebitoExiste(linha);
		
	}

	private void validaContaCorrenteDebitoExiste(LinhaArquivoModel linha) {
		if( !vc.isNumeric(segR.getContaCorrentaDebito()) || Integer.parseInt(segR.getContaCorrentaDebito()) > 0 && !vc.isContaExiste(segR.getContaCorrentaDebito()) ){
			 vc.criarRegistroInvalido(linha,216,228,"Seg(R) Pos(217) Tam(13) - Conta corrente de débito inexistente");
		}
	}

	private void validaAgenciaDebitoMantenedora(LinhaArquivoModel linha) {
		if(!vc.isNumeric(segR.getAgenciaDebitoMantenedora()) ){
			 vc.criarRegistroInvalido(linha,210,215,"Seg(R) Pos(211) Tam(5) - Agência mantenedora da conta é diferente do número da cooperativa");
		}
	}

	private void validaValorMultaParaModalidade(LinhaArquivoModel linha) {
		
		if(segP.getVlrTitulo().equals("000000000000000"))
			return;
		
		if( vc.isNumeric(segR.getCodMulta()) && vc.isNumeric(segP.getVlrTitulo()) && vc.isNumeric(segR.getValorMulta()) ){
			
			int codMulta =	Integer.parseInt(segR.getCodMulta());
			double vlrTitulo = Double.parseDouble(segP.getVlrTitulo())/100;
			
			double percMaxMulta = 99.900;
			if(segP.getCodModalidade().equals("09") || segP.getCodModalidade().equals("08")){ //Cheque e conta capital
				percMaxMulta = 2.0000;	
			}
			
			double perMinMulta =  0.0000;
			double vlrMulta = Double.parseDouble(segR.getValorMulta())/100;			
			
			if(codMulta == 1 ){
				 if((vlrMulta < (vlrTitulo * (perMinMulta / 100))) || (vlrMulta > (vlrTitulo * (percMaxMulta / 100)) )){
					 vc.criarRegistroInvalido(linha,74,89,"Seg(R) Pos(75) Tam(13) - Valor da multa é inválido para o Produto/Modalidade informado");
				 }
			}else if(codMulta == 2){
				if((vlrMulta < perMinMulta) || (vlrMulta > percMaxMulta)){
					 vc.criarRegistroInvalido(linha,74,89,"Seg(R) Pos(75) Tam(13) - Valor da multa é inválido para o Produto/Modalidade informado");
				 }
			}
		}else if( !vc.isNumeric(segR.getValorMulta())){
			vc.criarRegistroInvalido(linha,74,89,"Seg(R) Pos(75) Tam(13) - Valor da multa é inválido");
		}
		
	}
	
	private void validaCodMulta(LinhaArquivoModel linha) {
		if(!Pattern.matches("0|1|2|3", segR.getCodMulta()) ){
			vc.criarRegistroInvalido(linha,65,66,"Seg(R) Pos(66) Tam(1) - Código da multa é diferente de '0','1','2' e '3'");
		}
	}

	private void validaDataMulta(LinhaArquivoModel linha) {
		if(Pattern.matches("0|1|2|3", segR.getDataMulta())){
			if(!vc.isNumeric(segR.getDataMulta()) || !DateUtil.isDataCnabValidaDDMMYYYY(segR.getDataMulta())){
				vc.criarRegistroInvalido(linha,68,74,"Seg(R) Pos(67) Tam(8) - Código da multa é diferente de '0','1','2' e '3'");
			}
		}
	}
	
	private void validaDataMultaMenorQueDataVencimento(LinhaArquivoModel linha) {
		if(Pattern.matches("0|1|2|3", segR.getDataMulta()) && 
				DateUtil.isDataCnabValidaDDMMYYYY(segR.getDataMulta()) &&
				DateUtil.isDataCnabValidaDDMMYYYY(segP.getDataVencimento()) &&
				DateUtil.isDataUmMenorQueDataDois(DateUtil.parseDataCnab(segR.getDataMulta()),
												  DateUtil.parseDataCnab(segP.getDataVencimento())) ==  -1){//código desconto
			
			vc.criarRegistroInvalido(linha,142,150,"Seg(P) Pos(68) Tam(8) - Data do multa menor ou igual que a data de vencimento do título");
		}
	}	
	
	
	private void validaValorMulta(LinhaArquivoModel linha) {
		if(Pattern.matches("0|1|2|3", segR.getCodMulta()) ){
			if(!vc.isNumeric(segR.getValorMulta()) || segR.getValorMulta().equals("000000000000000")) {
				vc.criarRegistroInvalido(linha,65,66,"Seg(R) Pos(75) Tam(13) - valor da multa é inválido");	
			}			
		}
	}	
	
	
	private void validarCodigoDesconto3(LinhaArquivoModel linha) {
		if(!Pattern.matches("0|1|2|3|4|5|6|7", segR.getCodDesconto3()) ){
			vc.criarRegistroInvalido(linha,41,42,"Seg(R) Pos(42) Tam(1) - Código do desconto 3 é diferente de '0','1','2','3','4','5','6' e '7'");
		}
	}
	
	private void validarCodigoDesconto2(LinhaArquivoModel linha) {
		if(!Pattern.matches("0|1|2|3|4|5|6|7", segR.getCodDesconto2()) ){
			vc.criarRegistroInvalido(linha,17,18,"Seg(R) Pos(18) Tam(1) - Código do desconto 2 é diferente de '0','1','2','3','4','5','6' e '7' ");
		}
	}
	
	private void validarCodigoDesconto2ComDataInvalidada(LinhaArquivoModel linha) {
		if(Pattern.matches("1|2|3|4|5|6|7", segR.getCodDesconto2()) && !DateUtil.isDataCnabValidaDDMMYYYY(segR.getDataDeconto2())){
			vc.criarRegistroInvalido(linha,18,26,"Seg(R) Pos(19) Tam(8) - Data do desconto 2 é inválida");
		}
	}
	
	private void validarValorDesconto2(LinhaArquivoModel linha) {
		if(!vc.isNumeric(segR.getValorDesconto2())){
			vc.criarRegistroInvalido(linha,28,43,"Seg(R) Pos(27) Tam(41) - Valor/Percentual Desconto 2 é inválido");
		}
	}
	
	private void validarCodigoDesconto3ComDataInvalidada(LinhaArquivoModel linha) {
		if(Pattern.matches("1|2|3|4|5|6|7", segR.getCodDesconto3()) && !DateUtil.isDataCnabValidaDDMMYYYY(segR.getDataDeconto3())){
			vc.criarRegistroInvalido(linha,18,26,"Seg(R) Pos(19) Tam(8) - Data do desconto 3 é inválida");
		}
	}
	
	private void validarValorDesconto3(LinhaArquivoModel linha) {
		if(!vc.isNumeric(segR.getValorDesconto3())){
			vc.criarRegistroInvalido(linha,52,67,"Seg(R) Pos(51) Tam(13) - Valor/Percentual Desconto 3 é inválido");
		}
	}

	private void validarCodigoMovimento(LinhaArquivoModel linha) {
		//C004	Código de Movimento Remessa manual febraban pagina 146
		Integer[] arrayMovimento = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,30,31,33,34,35,40,41,42,43,44,45,46};
		try {
			if(!Arrays.asList(arrayMovimento).contains(Integer.valueOf(segR.getCodoMovimento()))){
				vc.criarRegistroInvalido(linha,15,17,"Seg(R) Pos(16) Tam(2) - Código do movimento é inválido'");
			}else if(Integer.valueOf(segP.getCodoMovimento()) != Integer.valueOf(segR.getCodoMovimento())) {
				vc.criarRegistroInvalido(linha,15,17,"Seg(R) Pos(16) Tam(2) - Código do movimento diferente do segmento P");
			}

		} catch (Exception e) {
			vc.criarRegistroInvalido(linha,15,17,"Seg(R) Pos(16) Tam(2) -  Código do movimento é inválido'");
		}		
	}
	
	private void validarCodigoBanco(LinhaArquivoModel linha){
		if(! segR.getCodBanco().equals("756")){
			vc.criarRegistroInvalido(linha, 0, 3, "Seg(R) Pos(1) Tam(3) - Código do banco é diferente de '756'");
		}
	}
}

