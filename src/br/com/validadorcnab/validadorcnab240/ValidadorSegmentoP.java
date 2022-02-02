package br.com.validadorcnab.validadorcnab240;

import java.util.Arrays;
import java.util.Date;
import java.util.regex.Pattern;

import br.com.validadorcnab.model.SegmentoPModel;
import br.com.validadorcnab.util.DateUtil;
import br.com.validadorcnab.util.Util;
import br.com.validadorcnab.validadorcnab240.Validador240.LinhaArquivoModel;


class ValidadorSegmentoP {
	
	private static ValidadorSegmentoP instance;
	
	public static ValidadorSegmentoP getInstance() {
		if (instance == null) {
			instance = new ValidadorSegmentoP();
		}
		return instance;
	}
	private ValidadorSegmentoP() {}	
	
	private SegmentoPModel segP;	
	private ValidacaoCNAB240 vc = ValidacaoCNAB240.getInstance();
	
	public void validaSegmentoP(LinhaArquivoModel linha) throws Exception {

		segP = new SegmentoPModel(linha.getConteudoLinha());
		
		validarCodigoBanco(linha);
		validarCodigoMovimento(linha);
		validaAgenciaMantenedoraConta(linha);
		validarContaCorrenteExiste(linha);
		validarModalidade(linha);
		validarNossoNumeroValido(linha);
		ValidarNossoNumeroRepetido(linha);
		validaCarteiraDeCobranca(linha);
		validaTipoEmissaoBloqueto(linha);
		validaDataVencimentoZeradasParaCarne(linha);
		validaDataVencimentoAnteriorDataEmissao(linha);
		validaAceite(linha);
		validaDataEmissaoMaiorQueDataMovimento(linha);
		validaCodMora(linha);
		validaCodDesc1(linha);
		validaCodDesc1DataDesconto1(linha);
		validaDataDesc1Invalido(linha);
		validaDataDesc1MaiorQueDataVencimento(linha);
		validaValorDesc1MaiorOuIgualValorTitulo(linha);
		validaCodigoMoedaRealValorIOFMaiorIgualValorTitulo(linha);
		validaCodigoMoedaRealEValorAbatimentoMaiorigualValortitulo(linha);
		validaCodigoProtesto(linha);
		validaDiasProtesto(linha);
		validaCodigoMoedaRealEValorAbatimentoMaisValorDesconto1MaiorigualValortitulo(linha);
		validaContratoCarteiraInvalido(linha);
		validaContratoCarteira(linha);
		validaCodigoMoeda(linha);
		validaValorTitulo(linha);
		validaEspecieDocumento(linha);
		
	}

	private void validaEspecieDocumento(LinhaArquivoModel linha) {
		if(!vc.isNumeric(segP.getEspecieDocumento()) || segP.getEspecieDocumento().equals("00") ){
			vc.criarRegistroInvalido(linha,106,108,"Seg(P) Pos(107) Tam(2) - Código da espécie de documento é inválido (erro crítico)");
		}		
	}
	
	private void validaCodigoMoeda(LinhaArquivoModel linha) {
		if(!Pattern.matches("02|09", segP.getCodMoeda())){
			vc.criarRegistroInvalido(linha,227,229,"Seg(P) Pos(228) Tam(2) - Código de moeda é diferente de '02' - Dólar Comercial e '09' - Real");
		}
	}
	
	private void validaCodDesc1(LinhaArquivoModel linha) {
		if(!Pattern.matches("0|1|2|3|4|5|6|7", segP.getCodDesconto1())){
			vc.criarRegistroInvalido(linha,141,142,"Seg(P) Pos(142) Tam(1) - Código do desconto 1 é diferente de '0','1','2','3','4','5','6' e '7'");
		}
	}
	
	private void validaContratoCarteiraInvalido(LinhaArquivoModel linha) {
		if(segP.getContratoGarantia() != null && !segP.getContratoGarantia().equals("") && !vc.isNumeric(segP.getContratoGarantia()) ) {
			vc.criarRegistroInvalido(linha,229, 239,"Seg(P) Pos(230) Tam(10) - Contrato garantia com formato invalido");
		}
	}	
	
	private void validaContratoCarteira(LinhaArquivoModel linha) {
		if(Pattern.matches("2|3", segP.getCodCarteira())){
			if(!vc.isNumeric(segP.getContratoGarantia()) || Integer.parseInt(segP.getContratoGarantia().trim()) <= 0){
				vc.criarRegistroInvalido(linha,229, 239,"Seg(P) Pos(230) Tam(10) - Contrato garantia é inválido para a modalidade 'Caucionada' ou 'Vinculada'");
			}
		}
	}
	
	private void validaCodigoProtesto(LinhaArquivoModel linha) {
		if(!Pattern.matches("1|2|3|8|9", segP.getCodProtesto())){
			vc.criarRegistroInvalido(linha,220,221,"Seg(P) Pos(221) Tam(1) - Código para protesto é diferente de '1','2','3' '8' e '9'");
		}
	}

	private void validaDiasProtesto(LinhaArquivoModel linha) {
		if(!vc.isNumeric(segP.getDiasProtesto())){
			vc.criarRegistroInvalido(linha,222,223,"Seg(P) Pos(222) Tam(2) - Dias de protesto inválido");
		}
	}	
	
	private void validaCodigoMoedaRealValorIOFMaiorIgualValorTitulo(LinhaArquivoModel linha) {
		if(segP.getVlrTitulo().equals("000000000000000"))
			return;
		
		if( vc.isNumeric(segP.getVlrIof()) && 
			vc.isNumeric(segP.getVlrTitulo()) && 
			segP.getCodMoeda().equals("09")&&			
			Long.parseLong(segP.getVlrIof()) >= Long.parseLong(segP.getVlrTitulo())){
			
			vc.criarRegistroInvalido(linha,165,180,"Seg(P) Pos(166) Tam(15) - Valor do IOF é maior ou igual ao valor do título");
		}else if(!vc.isNumeric(segP.getVlrIof())){
			vc.criarRegistroInvalido(linha,165,180,"Seg(P) Pos(166) Tam(15) - Valor do IOF é inválido");
		}	     
	}
	
	private void validaValorTitulo(LinhaArquivoModel linha){
		if(!vc.isNumeric(segP.getVlrTitulo())){
			vc.criarRegistroInvalido(linha,85,100,"Seg(P) Pos(86) Tam(15) - Valor do título é inválido");
		}else if(segP.getVlrTitulo().equals("000000000000000")){
			vc.criarRegistroInvalido(linha,85,100,"Seg(P) Pos(86) Tam(15) - Valor do título não informado");
		}

	}
	
	private void validaDataDesc1MaiorQueDataVencimento(LinhaArquivoModel linha) {
		if(Pattern.matches("1|2|3|4|5|6|7", segP.getCodDesconto1()) && 
				DateUtil.isDataCnabValidaDDMMYYYY(segP.getDataDesconto1()) &&
				DateUtil.isDataCnabValidaDDMMYYYY(segP.getDataVencimento()) &&
				DateUtil.isDataUmMenorQueDataDois(DateUtil.parseDataCnab(segP.getDataVencimento()), DateUtil.parseDataCnab(segP.getDataDesconto1()) ) ==  -1
			){//código desconto
			
			vc.criarRegistroInvalido(linha,142,150,"Seg(P) Pos(143) Tam(8) - Data do desconto 1 é maior que a data de vencimento do título");
		}
	}
	private void validaCodDesc1DataDesconto1(LinhaArquivoModel linha) {
		if(Pattern.matches("1|2|3|4|5|6|7", segP.getCodDesconto1())){
			if(!vc.isNumeric(segP.getDataDesconto1()) || !DateUtil.isDataCnabValidaDDMMYYYY(segP.getDataDesconto1())){
				vc.criarRegistroInvalido(linha,142,150,"Seg(P) Pos(143) Tam(8) - Data do desconto 1 é inválida");
			}
		}
	}
	
	private void validaDataDesc1Invalido(LinhaArquivoModel linha) {		
		if(!vc.isNumeric(segP.getDataDesconto1())){
			vc.criarRegistroInvalido(linha,142,150,"Seg(P) Pos(143) Tam(8) - Valor do desconto 1 inválido");
		}
		
	}
	
	private void validaValorDesc1MaiorOuIgualValorTitulo(LinhaArquivoModel linha){
		/* Legenda dos códigos de desconto 
		 * 1 - Valor fixo até a data informada
		 * 2 - Percentual até a data informada
		 * 3 - Valor por antecipação dia corrido
		 * 4 - Valor por antecipação dia útil
		 * 5 - Percentual sobre o valor nominal dia corrido
		 * 6 - Percentual sobre o valor nominal dia útil 
		 */
		if(segP.getVlrTitulo().equals("000000000000000"))
			return;
		if( 	Pattern.matches("1|3|4", segP.getCodDesconto1()) && 
				vc.isNumeric(segP.getVlrDesconto1()) &&
				vc.isNumeric(segP.getVlrTitulo()) &&
				Long.parseLong(segP.getVlrDesconto1()) >= Long.parseLong(segP.getVlrTitulo())){
			
				vc.criarRegistroInvalido(linha,150,165,"Seg(P) Pos(151) Tam(13) - Valor do desconto 1 é maior ou igual ao valor do título");
			
		}else if(Pattern.matches("2|5|6", segP.getCodDesconto1()) && 
				vc.isNumeric(segP.getVlrDesconto1()) &&
				vc.isNumeric(segP.getVlrTitulo()) && 
				Integer.parseInt(segP.getVlrDesconto1()) > 9999 	)  {//Percentual
				
				vc.criarRegistroInvalido(linha,150,165,"Seg(P) Pos(150) Tam(13) - Valor do desconto 1 é maior ou igual ao valor do título");
		}
		
	}
	private void validaCodigoMoedaRealEValorAbatimentoMaiorigualValortitulo( LinhaArquivoModel linha) {
		if(segP.getVlrTitulo().equals("000000000000000"))
			return;

		if( vc.isNumeric(segP.getVlrAbatimento()) && 
			vc.isNumeric(segP.getVlrTitulo()) && 
			segP.getCodMoeda().equals("09") &&
			Long.parseLong(segP.getVlrAbatimento()) >= Long.parseLong(segP.getVlrTitulo())){
							
			vc.criarRegistroInvalido(linha,180,195,"Seg(P) Pos(181) Tam(15) - Valor do abatimento é maior ou igual ao valor do título");
		}else if(!vc.isNumeric(segP.getVlrAbatimento())){
			vc.criarRegistroInvalido(linha,180,195,"Seg(P) Pos(181) Tam(15) - Valor do abatimento é inválido");
		}
	}
	
	private void validaCodigoMoedaRealEValorAbatimentoMaisValorDesconto1MaiorigualValortitulo(LinhaArquivoModel linha) {
		if(segP.getVlrTitulo().equals("000000000000000"))
			return;
		
		if(segP.getCodMoeda().equals("09") && 
				vc.isNumeric(segP.getCodDesconto1()) && 
				!segP.getCodDesconto1().equals("0") && 
				vc.isNumeric(segP.getVlrDesconto1()) && 
				vc.isNumeric(segP.getVlrTitulo()) && 
				vc.isNumeric(segP.getVlrAbatimento()) && 
				Integer.parseInt(segP.getVlrAbatimento()) > 0){
			
			float valorAbatimento =  Float.parseFloat( segP.getVlrAbatimento())  /100;
			float valorTitulo = Float.parseFloat(segP.getVlrTitulo()) /100;
			float valorDesconto1 = 0; 
				
				if(Pattern.matches("1|3|4", segP.getCodDesconto1())){
					valorDesconto1 = Float.parseFloat(segP.getVlrDesconto1()) / 100;
				}else{
					valorDesconto1 = ( Float.parseFloat(segP.getVlrDesconto1()) / 10000) *  valorTitulo ;
				}
				
				if((valorAbatimento + valorDesconto1) >= valorTitulo){

					vc.criarRegistroInvalido(linha,180,195,"Seg(P) Pos(151) Tam(13) - Valor do desconto 1 + valor do abatimento ("+ Util.getValorFormatadoComMascra( valorAbatimento )+") é maior ou igual ao valor do título ("+ 
							Util.getValorFormatadoComMascra(valorTitulo)+")");				
					vc.criarRegistroInvalido(linha,150,165,"Seg(P) Pos(181) Tam(13) - Valor do abatimento + valor do desconto 1 ("+ Util.getValorFormatadoComMascra( valorDesconto1 )+") é maior ou igual ao valor do título ("+ 
							Util.getValorFormatadoComMascra(valorTitulo)+")");
				}
		}
		
	}
	private void validaCodMora(LinhaArquivoModel linha) {
		if(!Pattern.matches("0|1|2|3", segP.getCodMora() )){
			vc.criarRegistroInvalido(linha,117,118,"Seg(P) Pos(118) Tam(1)- Código da mora é diferente de '0','1','2' e '3'");
		}
	}
	
	private void validaDataEmissaoMaiorQueDataMovimento(LinhaArquivoModel linha) {

		Date dtEmissao = DateUtil.parseDataCnab( segP.getDataEmissao());
		if(dtEmissao!=null &&  DateUtil.isDataUmMenorQueDataDois(dtEmissao, vc.getDataMovimentoAtual()) == 1){
			vc.criarRegistroInvalido(linha, 109, 117, "Seg(P) Pos(110) Tam(8) - Data de emissão é maior que a data de movimento");
		}
	}
	
	private void validaAceite(LinhaArquivoModel linha) {
		if(! Pattern.matches("A|N",segP.getAceite())){
			vc.criarRegistroInvalido(linha, 108, 109, "Seg(P) Pos(109) Tam(1) - Aceite é diferente de ('A','N')");
		}
	}
	
	private void validaDataVencimentoAnteriorDataEmissao(LinhaArquivoModel linha) {
		if(!segP.getDataVencimento().equals("00000000")){// diferente de contra apresentação

			if(!DateUtil.isDataCnabValidaDDMMYYYY(segP.getDataVencimento())){
	
				vc.criarRegistroInvalido(linha, 77, 85, "Seg(P) Pos(78) Tam(8) - Data de vencimento é inválida");
	
			}else if(!DateUtil.isDataCnabValidaDDMMYYYY(segP.getDataEmissao())){
	
				vc.criarRegistroInvalido(linha, 109, 117, "Seg(P) Pos(110) Tam(8) - Data de emissão é inválida");
	
			}else if(DateUtil.isDataUmMenorQueDataDois( DateUtil.parseDataCnab(segP.getDataVencimento()) ,  DateUtil.parseDataCnab(segP.getDataEmissao())  ) == -1){
	
				vc.criarRegistroInvalido(linha, 77, 85, "Seg(P) Pos(78) Tam(8) - Data de vencimento é menor que a data de emissão do título");
			}
		}
	}
	
	private void validaDataVencimentoZeradasParaCarne(LinhaArquivoModel linha) {
		if(Pattern.matches("00000000", segP.getDataVencimento())){
			if( Pattern.matches("5", segP.getCodModalidade())){
				vc.criarRegistroInvalido(linha, 77, 84, "Seg(P) Pos(78) Tam(8) - Data de vencimento é inválida para modalidade carnê de pagamentos");
			}
		}
	}
	private void validaCarteiraDeCobranca(LinhaArquivoModel linha) {
		if(! Pattern.matches("1|2|3", segP.getCodCarteira())){
			vc.criarRegistroInvalido(linha, 57, 58, "Seg(P) Pos(58) Tam(1) - Código da carteira de cobrança é diferente de '1','2' e '3'");
		}
	}
	private void validaTipoEmissaoBloqueto(LinhaArquivoModel linha) {
		if(! Pattern.matches("1|2|3",segP.getTipoBloqueto())){
			vc.criarRegistroInvalido(linha, 60, 61, "Seg(P) Pos(61) Tam(1) - Tipo de emissão do bloqueto é diferente de '1' , '2' e '3'");
		}
	}
	private void ValidarNossoNumeroRepetido(LinhaArquivoModel linha) {

		if(vc.isNossoNumeroRepetido(segP.getNossoNumero())){
			vc.criarRegistroInvalido(linha, 37, 47, "Seg(P) Pos(38) Tam(10) - Nosso número está repetido no arquivo");
		}
	}
	private void validarNossoNumeroValido(LinhaArquivoModel linha) {
		if(!segP.getNossoNumero().equals("0000000000") &&  (!vc.isNumeric(segP.getNossoNumero()))){
			vc.criarRegistroInvalido(linha, 37	, 47, "Seg(P) Pos(38) Tam(10) - Nosso número é inválido" );
		}
	}
	private void validarContaCorrenteExiste(LinhaArquivoModel linha) {
		if(!vc.isNumeric(segP.getContaCorrenteDv())){
			vc.criarRegistroInvalido(linha, 23, 36, "Seg(P) Pos(24) Tam(13) - Conta corrente inexistente");				
		}
	}
	
	private void validarModalidade(LinhaArquivoModel linha) {
		if(!vc.isNumeric(segP.getCodModalidade())){
			vc.criarRegistroInvalido(linha, 49, 51,"Seg(P) Pos(50) Tam(2) - Código de modalidade é inválida");
		}	
	}
	
	
	private void validaAgenciaMantenedoraConta(LinhaArquivoModel linha) {
		if(!vc.isNumeric(segP.getAgenciaMantenedoraConta())){
			vc.criarRegistroInvalido(linha, 17, 22, "Seg(P) Pos(18) Tam(5) - Agência mantenedora da conta é diferente da cooperativa configurada no sistema");
		}
	}
	
	private void validarCodigoMovimento(LinhaArquivoModel linha) {
		//C004	Código de Movimento Remessa manual febraban pagina 146
		Integer[] arrayMovimento = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,30,31,33,34,35,40,41,42,43,44,45,46};
		try {
			if(!Arrays.asList(arrayMovimento).contains(Integer.valueOf(segP.getCodoMovimento()))){
				vc.criarRegistroInvalido(linha,15,17,"Seg(Q) Pos(16) Tam(2) - Código do movimento é inválido'");
			}

		} catch (Exception e) {
			vc.criarRegistroInvalido(linha,15,17,"Seg(Q) Pos(16) Tam(2) -  Código do movimento é inválido'");
		}		
	}
	
	private void validarCodigoBanco(LinhaArquivoModel linha){
		if(! segP.getCodBanco().equals("756")){
			vc.criarRegistroInvalido(linha, 0, 3, "Seg(P) Pos(1) Tam(3) - Código do banco é diferente de '756'");
		}
	}
	
	public SegmentoPModel getSegP() {
		return segP;
	}
	
}
