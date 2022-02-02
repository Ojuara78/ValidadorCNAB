package br.com.validadorcnab.validadorcnab240;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import br.com.validadorcnab.enuns.UfEnum;
import br.com.validadorcnab.model.SegmentoPModel;
import br.com.validadorcnab.model.SegmentoQModel;
import br.com.validadorcnab.util.ValidaTipoDados;
import br.com.validadorcnab.validadorcnab240.Validador240.LinhaArquivoModel;

class ValidadorSegmentoQ {
	
	public static ValidadorSegmentoQ instance;
	
	public static ValidadorSegmentoQ getInstance() {
		
		if(instance==null){
			return new ValidadorSegmentoQ();
		}
		return instance;
	}
	private ValidadorSegmentoQ() {}
	
	private SegmentoPModel segP = ValidadorSegmentoP.getInstance().getSegP();
	private SegmentoQModel segQ;	
	
	private ValidacaoCNAB240  vc = ValidacaoCNAB240.getInstance();		
	
	public void validaSegmentoQ(LinhaArquivoModel linha) throws Exception {
		
		segQ = new SegmentoQModel(linha.getConteudoLinha());

		validarCodigoBanco(linha);

		validarCodigoMovimento(linha);

		validarTipoInscricaoSacado(linha);

		validarCPF_CNPJ(linha);

		validarCPF_CNPJ_duplicados_com_nomes_diferentes(linha);

		validarNomeSacadoInformado(linha); 

		validarEnderecoInformado(linha);

		validarBairroInformado(linha);

		validarCEP(linha);

		validarSufixoCEP(linha);

		validarUfSacado(linha);

		validarTipoInscricaoSacadorAvalista(linha);

		validarInscricaoSacadorAvalista(linha);

		validarNomeSacadorAvalista(linha);

		validarNomeSacadorSemCPFCNPJ(linha);

		validarCPF_CNPJ_duplicados_com_nomes_diferentes(linha);
		
	}
	
	private void validarCPF_CNPJ_duplicados_com_nomes_diferentes(LinhaArquivoModel linha) {

		List<LinhaArquivoModel> listaLinhas = ValidacaoCNAB240.getInstance().listaLinhasArquivo;
			
			for(LinhaArquivoModel linhaArquivo : listaLinhas){
				if(linhaArquivo.getTipoLinha() == ValidacaoCNAB240.Detalhe_Titulo_Segmento_Q ){
					if( segQ.getInscricao15Digitos().equals(linhaArquivo.getParte(18, 33)) ){
						if(! segQ.getNomeSacado().equals(linhaArquivo.getParte(33, 73))){
							vc.criarRegistroInvalido(linha, 18, 33, "Seg(Q) Pos(19) Tam(15) - CPF/CNPJ do sacado está duplicado com nomes diferentes");
						}
					}
				}
			}
	}
	
	private void validarNomeSacadorSemCPFCNPJ(LinhaArquivoModel linha) {
		if(!segQ.getNomeSacadorAvalista().trim().equals("")){
			if(segQ.getInscricaoSacador().equals("00000000000000")){
				vc.criarRegistroInvalido(linha,154,169,"Seg(Q) Pos(155) Tam(15) - CPF/CNPJ do sacador avalista não informado");
			}
		}
	}
	
	private void validarNomeSacadorAvalista(LinhaArquivoModel linha) {
		if( Pattern.matches("1|2", segQ.getTipoInscricaoSacador()) && 
				( ValidaTipoDados.isCPFValido(segQ.getInscricaoSacadorCPF_CNPJ(segQ.getTipoInscricao(),segQ.getInscricaoSacador())) || 
				ValidaTipoDados.isCNPJValido(segQ.getInscricaoSacadorCPF_CNPJ(segQ.getTipoInscricao(),segQ.getInscricaoSacador())) ) && segQ.getNomeSacadorAvalista().trim().equals("")){
			vc.criarRegistroInvalido(linha,169,209,"Seg(Q) Pos(170) Tam(40) - Nome do sacador avalista não informado");
		}
	}
	
	private void validarInscricaoSacadorAvalista(LinhaArquivoModel linha) {
	
		if( Pattern.matches("1|2", segQ.getTipoInscricaoSacador()) ){
			String inscricao =  segQ.getInscricaoSacadorCPF_CNPJ(segQ.getTipoInscricaoSacador(),segQ.getInscricaoSacador());
			switch (segQ.getTipoInscricaoSacadorInt()) {
			case 1:
				if( ! ValidaTipoDados.isCPFValido(inscricao)){
					vc.criarRegistroInvalido(linha,154,169,"Seg(Q) Pos(155) Tam(15) - CPF/CNPJ do sacador avalista é inválido para o tipo de inscrição (1-PF)");
				}
				break;
			case 2:
				if( ! ValidaTipoDados.isCNPJValido(inscricao)){
					vc.criarRegistroInvalido(linha,154,169,"Seg(Q) Pos(155) Tam(15) - CPF/CNPJ do sacador avalista é inválido para o tipo de inscrição (2-PJ)");
				}
				break;
			default:
				break;
			}
		}
	}
	
	private void validarTipoInscricaoSacadorAvalista(LinhaArquivoModel linha) {
		if(!Pattern.matches("1|2|0", segQ.getTipoInscricaoSacador() )){
			vc.criarRegistroInvalido(linha,153,154,"Seg(Q) Pos(154) Tam(1) - Tipo de inscrição do sacador avalista é diferente de (1-PF) e (2-PJ)");
		}
	}
	
	private void validarUfSacado(LinhaArquivoModel linha) {
		if(!UfEnum.contains(segQ.getUf())) {
			vc.criarRegistroInvalido(linha,151,153,"Seg(Q) Pos(152) Tam(2) - UF do sacado é inválida");
		}
	}
	
	private void validarSufixoCEP(LinhaArquivoModel linha) {
		if(segQ.getSufixoCep().replace(" ", "").length() != 3){
			vc.criarRegistroInvalido(linha,133,136,"Seg(Q) Pos(134) Tam(3) - Sufixo do CEP do sacado com valor inválido");
			return;
		}
		if(!vc.isNumeric(segQ.getSufixoCep())){
			vc.criarRegistroInvalido(linha,133,136,"Seg(Q) Pos(134) Tam(3) - Sufixo do CEP do sacado não informado");
		}
	}
	
	private void validarCEP(LinhaArquivoModel linha) {
		if(segQ.getCep().replace(" ", "").length() == 0){
			vc.criarRegistroInvalido(linha,128,133,"Seg(Q) Pos(129) Tam(5) - CEP do sacado não informado");
			return;
		}	
		
		if(segQ.getCep().replace(" ", "").length() != 5){
			vc.criarRegistroInvalido(linha,128,133,"Seg(Q) Pos(129) Tam(5) - CEP do sacado com valor inválido");
			return;
		}		
		if(Pattern.matches(segQ.getCep().substring(0, 5), "00000")){
			vc.criarRegistroInvalido(linha,128,133,"Seg(Q) Pos(129) Tam(5) - CEP do sacado com valor inválido");
		}
	}
	
	private void validarBairroInformado(LinhaArquivoModel linha) {
		if(segQ.getBairro().trim().equals("")){
			vc.criarRegistroInvalido(linha, 113, 128, "Seg(Q) Pos(114) Tam(15) - Bairro do sacado não informado");
		}
	}
	
	private void validarEnderecoInformado(LinhaArquivoModel linha) {
		if(segQ.getEndereco().trim().equals("")){
			vc.criarRegistroInvalido(linha, 73, 113, "Seg(Q) Pos(74) Tam(40) - Endereço do sacado não informado");
		}
	}
	
	private void validarNomeSacadoInformado(LinhaArquivoModel linha) {
		if(segQ.getNomeSacado().trim().equals("")){
			vc.criarRegistroInvalido(linha, 33, 73, "Seg(Q) Pos(34) Tam(40) - Nome do sacado não informado");
		}
	}

	private void validarCPF_CNPJ(LinhaArquivoModel linha) {

		if(vc.isNumeric(segQ.getTipoInscricao()) && Pattern.matches("1|2", segQ.getTipoInscricao())){

			switch (segQ.getTipoInscricaoInt()) {
			case 1:
				if( ! ValidaTipoDados.isCPFValido(segQ.getInscricaoPF_PJ())){
					vc.criarRegistroInvalido(linha,18,33,"Seg(Q) Pos(19) Tam(15) - CPF/CNPJ do sacado é inválido para o tipo de inscrição (1-PF)");
				}
				break;
			case 2:
				if( ! ValidaTipoDados.isCNPJValido(segQ.getInscricaoPF_PJ())){
					vc.criarRegistroInvalido(linha,18,33,"Seg(Q) Pos(19) Tam(15) - CPF/CNPJ do sacado é inválido para o tipo de inscrição (2-PJ)");
				}
				break;
			default:
				break;
			}
		}
	}
	
	private void validarTipoInscricaoSacado(LinhaArquivoModel linha) {
		if(!Pattern.matches("1|2", segQ.getTipoInscricao() )){
			vc.criarRegistroInvalido(linha,17,18,"Seg(Q) Pos(18) Tam(1) - Tipo de inscrição do sacado é diferente de (1-PF) e (2-PJ)");
		}
	}

	private void validarCodigoMovimento(LinhaArquivoModel linha) {
		//C004	Código de Movimento Remessa manual febraban pagina 146
		Integer[] arrayMovimento = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,30,31,33,34,35,40,41,42,43,44,45,46};
		try {
			if(!Arrays.asList(arrayMovimento).contains(Integer.valueOf(segQ.getCodoMovimento()))){
				vc.criarRegistroInvalido(linha,15,17,"Seg(Q) Pos(16) Tam(2) - Código do movimento é inválido");
			}else if(Integer.valueOf(segP.getCodoMovimento()) != Integer.valueOf(segQ.getCodoMovimento())) {
				vc.criarRegistroInvalido(linha,15,17,"Seg(Q) Pos(16) Tam(2) - Código do movimento diferente do segmento P");
			}

		} catch (Exception e) {
			vc.criarRegistroInvalido(linha,15,17,"Seg(Q) Pos(16) Tam(2) -  Código do movimento é inválido'");
		}		
	}
	
	private void validarCodigoBanco(LinhaArquivoModel linha){
		if(!Pattern.matches("756",segQ.getCodBanco()) ){
			vc.criarRegistroInvalido(linha, 0, 3, "Seg(Q) Pos(1) Tam(3) - Código do banco é diferente de '756'");
		}
	}	
}

