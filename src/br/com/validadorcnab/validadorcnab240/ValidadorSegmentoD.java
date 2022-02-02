package br.com.validadorcnab.validadorcnab240;

import java.util.Arrays;
import java.util.regex.Pattern;

import br.com.validadorcnab.model.SegmentoDModel;
import br.com.validadorcnab.util.DateUtil;
import br.com.validadorcnab.util.ValidaLeituraCMC7;
import br.com.validadorcnab.util.ValidaTipoDados;
import br.com.validadorcnab.validadorcnab240.Validador240.LinhaArquivoModel;


class ValidadorSegmentoD {
	
	public static ValidadorSegmentoD instance;	
	public static ValidadorSegmentoD getInstance() {
		if(instance==null){
			return new ValidadorSegmentoD();
		}
		return instance;
	}
	
	private ValidadorSegmentoD() {}
	
	private SegmentoDModel segD;
	private ValidacaoCNAB240  vc = ValidacaoCNAB240.getInstance();
	
	public void validaSegmentoD(LinhaArquivoModel linha) throws Exception {
		
		segD = new SegmentoDModel(linha.getConteudoLinha());

		validarCodigoBanco(linha);

		validarCodigoMovimento(linha);

		validarCodigoFinalidade(linha);

		validarFormaEntrada(linha);

		validaCMC7(linha);

		validaCMC7RepetidoNoArquivo(linha);

		validaTiptoInscricao(linha);

		validaCPFCNPJ(linha);
		
		validaDataDepositoCheque(linha);

		validaDataPrevistaDebito(linha);
	}
	
	
	private void validaDataPrevistaDebito(LinhaArquivoModel linha) {
		if(! segD.getDataPrevistaDebito().equals("00000000") && !DateUtil.isDataCnabValidaDDMMYYYY(segD.getDataPrevistaDebito())){
			vc.criarRegistroInvalido(linha, 100, 108, "Seg(D) Pos(101) Tam(8) - Data prevista para débito/crédito é inválida" );
		}
	}

	private void validaDataDepositoCheque(LinhaArquivoModel linha) {
		if(! DateUtil.isDataCnabValidaDDMMYYYY(segD.getDataDepositoCheque())){
			vc.criarRegistroInvalido(linha, 92, 100, "Seg(D) Pos(93) Tam(8) - Data para depósito do cheque é inválida" );
		}
	}

	private void validaCPFCNPJ(LinhaArquivoModel linha) {
		
		/* Existem inscrições CPF que coincidem com CNPJ quando se completa com zeros à esquerda conforme recomenda o padrão CNAB240/400 
		*  Exemplo: CNPJ =00000868180823 ou CPF: 00868180823 (se validarmos com 11 ou 14 dígitos ambos serão válidos)
		*/
		if(ValidaTipoDados.isCNPJValido(segD.getInscricao(2)) && ValidaTipoDados.isCPFValido(segD.getInscricao(1)) && Pattern.matches("1|2",segD.getTipoInscricao())){
			return;//Desconsiderar a validação.
		}
		
		//Se o CPF/CNPJ for incompativel com o tipo de inscrição
		if(segD.getTipoInscricao().equals("1") && ValidaTipoDados.isCNPJValido(segD.getInscricao(2))){
			vc.criarRegistroInvalido(linha,55,69,"Seg(D) Pos(56) Tam(14) - CPF/CNPJ do emitente é inválido para o tipo de inscrição (1-PF)");
			return;
		}else if(segD.getTipoInscricao().equals("2") && ValidaTipoDados.isCPFValido(segD.getInscricao(1))){
			vc.criarRegistroInvalido(linha,55,69,"Seg(D) Pos(56) Tam(14) - CPF/CNPJ do emitente é inválido para o tipo de inscrição (2-PJ)");
			return;
		}
		
		if(Pattern.matches("1|2", segD.getTipoInscricao())){
			switch (segD.getTipoInscricaoInt()) {
			case 1:
				if( ! ValidaTipoDados.isCPFValido(segD.getInscricao())){
					vc.criarRegistroInvalido(linha,55,69,"Seg(D) Pos(56) Tam(14) - CPF/CNPJ do emitente é inválido");
				}
				break;
			case 2:
				if( ! ValidaTipoDados.isCNPJValido(segD.getInscricao())){
					vc.criarRegistroInvalido(linha,55,69,"Seg(D) Pos(56) Tam(14) - CPF/CNPJ do emitente é inválido");
				}
				break;
			default:
				break;
			}
		}
	}

	private void validaTiptoInscricao(LinhaArquivoModel linha) {
		if(! Pattern.matches("1|2", segD.getTipoInscricao())){
			vc.criarRegistroInvalido(linha, 54, 55, "Seg(D) Pos(55) Tam(1) - Tipo de inscrição é diferente de (1-PF) e (2-PJ)" );
		}
	}

	private void validaCMC7RepetidoNoArquivo(LinhaArquivoModel linha) {

		int coutRepeticao = 0;
		for(String cmc7 : vc.listaCMC7  ){
			if(cmc7.equals(segD.getCodCmc7())){
				coutRepeticao++;
				if(coutRepeticao > 1){
					vc.criarRegistroInvalido(linha, 20, 54, "Seg(D) Pos(21) Tam(34) - A identificação do cheque (CMC7) está repetida no arquivo");
					break;
				}					
			}
		}
	}

	private void validaCMC7(LinhaArquivoModel linha) {
		if( ! vc.isNumeric(segD.getCodCmc7().trim()) || !ValidaLeituraCMC7.validaLeituraCodigo( segD.getCodCmc7())){
			vc.criarRegistroInvalido(linha,20,54,"Seg(D) Pos(21) Tam(34) - Identificação do cheque (CMC7) é inválida");
		}
	}

	private void validarFormaEntrada(LinhaArquivoModel linha) {
		if( ! Pattern.matches("1", segD.getFormaEntrada()) ){
			vc.criarRegistroInvalido(linha,19,20,"Seg(D) Pos(20) Tam(1) - Forma de entrada de dados é diferente de (1)");
		}
	}

	private void validarCodigoFinalidade(LinhaArquivoModel linha) {
		if( ! Pattern.matches("00|01|02|03|04", segD.getCodFinalidade()) ){
			vc.criarRegistroInvalido(linha,17,19,"Seg(D) Pos(18) Tam(2) - Código da finalidade é diferente de (00,01,02,03,04)");
		}		
	}

	private void validarCodigoMovimento(LinhaArquivoModel linha) {
		//C004	Código de Movimento Remessa manual febraban pagina 146
		Integer[] arrayMovimento = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,30,31,33,34,35,40,41,42,43,44,45,46};
		try {
			if(!Arrays.asList(arrayMovimento).contains(Integer.valueOf(segD.getCodoMovimento()))){
				vc.criarRegistroInvalido(linha,15,17,"Seg(Q) Pos(16) Tam(2) - Código do movimento é inválido'");
			}

		} catch (Exception e) {
			vc.criarRegistroInvalido(linha,15,17,"Seg(Q) Pos(16) Tam(2) -  Código do movimento é inválido'");
		}		
	}
	
	private void validarCodigoBanco(LinhaArquivoModel linha){
		if(! segD.getCodBanco().equals("756")){
			vc.criarRegistroInvalido(linha, 0, 3, "Seg(D) Pos(1) Tam(3) - Código do banco é diferente de '756'");
		}
	}	
}