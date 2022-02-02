package br.com.validadorcnab.validadorcnab240;

import java.util.regex.Pattern;

import br.com.validadorcnab.model.HeaderLoteTitulo;
import br.com.validadorcnab.util.ValidaTipoDados;
import br.com.validadorcnab.validadorcnab240.Validador240.LinhaArquivoModel;

class ValidadorHeaderLoteTitulo {
	
	private static ValidadorHeaderLoteTitulo instance;
	public static ValidadorHeaderLoteTitulo getInstance() {
		if (instance == null) {
			instance = new ValidadorHeaderLoteTitulo();
		}
		return instance;
	}
	private ValidadorHeaderLoteTitulo() {}
	
	
	private HeaderLoteTitulo ht;
	private ValidacaoCNAB240 vc = ValidacaoCNAB240.getInstance();
	
	public void  validaHeaderLoteTitulo(LinhaArquivoModel linha) throws Exception  {

		ht = new HeaderLoteTitulo(linha.getConteudoLinha());

		validarCodigoBanco(linha);

		validarTipoOperacao(linha);
		
		validaTipoInscricao(linha);
		
		validaCPF_CNPJ(linha);
		
		validaCPF_CNPJ_cliente(linha);
		
		validaCodigoConvenio(linha);
		
		validaAgenciaMantenedora(linha);
		
		validarContaCorrenteExiste(linha);
		
	}
	
	private void validarContaCorrenteExiste(LinhaArquivoModel linha) {
		if(!vc.isNumeric(ht.getContaCorrenteDv())){
			vc.criarRegistroInvalido(linha, 59, 72, "Header Lote Título Pos(60) Tam(13) - Conta corrente inexistente");
		}
	}
	
	private void validaAgenciaMantenedora(LinhaArquivoModel linha) {
		if(!vc.isNumeric(ht.getAgenciaMantenedoraConta())){
			vc.criarRegistroInvalido(linha, 53, 58, "Header Lote Título Pos(54) Tam(5) - Agência mantenedora da conta é diferente da cooperativa configurada no sistema");
		}
	}
	
	private void validaCodigoConvenio(LinhaArquivoModel linha) {
		if(!ht.getCodConvenio().trim().equals("")){
			vc.criarRegistroInvalido(linha,33,53,"Header Lote Título Pos(34) Tam(20) - Código de convênio não é válido.");
		}
	}
	
	private void validaCPF_CNPJ_cliente(LinhaArquivoModel linha) {
		if( ValidaTipoDados.isCPFValido( ht.getInscricao(ht.getTipoInscricaoInt())) || ValidaTipoDados.isCNPJValido( ht.getInscricao(ht.getTipoInscricaoInt())) ){
			if(Pattern.matches("1|2", ht.getTipoInscricao())){
				if(!vc.isNumeric(ht.getInscricao(ht.getTipoInscricaoInt()))) {
					vc.criarRegistroInvalido(linha,18,33,"Header Lote Título Pos(19) Tam(15) - CPF/CNPJ informado é diferente do CPF/CNPJ do cliente.");
				}
			}
		}
	}
	
	private void validaCPF_CNPJ(LinhaArquivoModel linha) {
		/* Existem inscrições CPF que coincidem com CNPJ quando se completa com zeros à esquerda conforme recomenda o padrão CNAB240/400 
		*  Exemplo: CNPJ =00000868180823 ou CPF: 00868180823 (se validarmos com 11 ou 14 dígitos ambos serão válidos)
		*/
		if(ValidaTipoDados.isCNPJValido(ht.getInscricao(2)) && ValidaTipoDados.isCPFValido(ht.getInscricao(1)) && Pattern.matches("1|2",ht.getTipoInscricao())){
			return;//Desconsidera a validação.
		}
		
		//Se o CPF/CNPJ for incompativel com o tipo de inscrição
		if(ht.getTipoInscricao().equals("1") && ValidaTipoDados.isCNPJValido(ht.getInscricao(2))){
			vc.criarRegistroInvalido(linha,18,33,"Header Lote Título Pos(19) Tam(15) - CPF/CNPJ do cedente é inválido para o tipo de inscrição (1-PF)");
			return;
		}else if(ht.getTipoInscricao().equals("2") && ValidaTipoDados.isCPFValido(ht.getInscricao(1))){
			vc.criarRegistroInvalido(linha,18,33,"Header Lote Título Pos(19) Tam(15) - CPF/CNPJ do cedente é inválido para o tipo de inscrição (2-PJ)");
			return;
		}
		
		
		if(Pattern.matches("1|2", ht.getTipoInscricao() )){
			switch (ht.getTipoInscricaoInt()) {
			case 1:
				if( ! ValidaTipoDados.isCPFValido(ht.getInscricao(ht.getTipoInscricaoInt()))){
					vc.criarRegistroInvalido(linha,18,33,"Header Lote Título Pos(19) Tam(15) - CPF/CNPJ inválido.");
				}
				break;
			case 2:
				if( ! ValidaTipoDados.isCNPJValido(ht.getInscricao(ht.getTipoInscricaoInt()))){
					vc.criarRegistroInvalido(linha,18,33,"Header Lote Título Pos(19) Tam(15) - CPF/CNPJ inválido.");
				}
				break;
			default:
				break;
			}
		}

	}
	
	private void validaTipoInscricao(LinhaArquivoModel linha) {
		if(!Pattern.matches("1|2", ht.getTipoInscricao() )){
			vc.criarRegistroInvalido(linha,17,18,"Header Lote Título Pos(18) Tam(1) - Tipo de inscrição é diferente de (1-PF) e (2-PJ)");
		}
	}
	
	private void validarTipoOperacao(LinhaArquivoModel linha) {
		if(! Pattern.matches("C|D|E|I|R|T", ht.getTipoOperacao())){
			vc.criarRegistroInvalido(linha, 8, 9, "Header Lote Título Pos(9) Tam(1) - Tipo de operação é diferente de  'C','D','E','I','R' e 'T'");
		}
	}
	
	private void validarCodigoBanco(LinhaArquivoModel linha){
		if(! ht.getCodBanco().equals("756")){
			vc.criarRegistroInvalido(linha, 0, 3, "Header Lote Título Pos(1) Tam(3) - Código do banco é diferente de '756'");
		}
	}	
}

