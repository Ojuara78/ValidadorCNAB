package br.com.validadorcnab.validadorcnab240;

import java.util.regex.Pattern;

import br.com.validadorcnab.model.HeaderArquivoModel;
import br.com.validadorcnab.util.ValidaTipoDados;
import br.com.validadorcnab.validadorcnab240.Validador240.LinhaArquivoModel;


class ValidadorHeaderArquivo {
	
	private static ValidadorHeaderArquivo instance;
	
	public static ValidadorHeaderArquivo getInstance() {
		if (instance == null) {
			instance = new ValidadorHeaderArquivo();
		}
		return instance;
	}
	
	private ValidadorHeaderArquivo() {}	
	
	public HeaderArquivoModel headerLote = null;
	ValidacaoCNAB240 validador = ValidacaoCNAB240.getInstance();
	
	public void validaHeaderArquivo(LinhaArquivoModel linha) {
			
			headerLote = new HeaderArquivoModel(linha.getConteudoLinha());

			validarCodigoBanco(linha);
			validarLoteServico(linha);
			validarTipoInscricao(linha);
			validarCPFCNPJ(linha);
			validaCPFCNPJcliente(linha);
			validaCodigoConvenio(linha);
			validaContaCorrenteExiste(linha);
			validaCodigoRemessa(linha);
	}	
	
	private void validarTipoInscricao(LinhaArquivoModel linha) {
		if(!headerLote.getTipoInscricao().matches("1|2")){
			validador.criarRegistroInvalido(linha,17,18,"Header Arquivo Pos(18) Tam(1) - Tipo de inscrição do cedente é diferente de 1-CPF e 2-CNPJ");
		}
	}
	
	private void validaCodigoRemessa(LinhaArquivoModel linha) {		
		if(!Pattern.matches("1", headerLote.getCodRemessa())){
			validador.criarRegistroInvalido(linha,142,143,"Header Arquivo Pos(143) Tam(1) - Código da remessa/retorno é diferente de '1'");
		}
	}
	
	private void validaContaCorrenteExiste(LinhaArquivoModel linha) {
		if(!validador.isNumeric(headerLote.getContaCorrente())) {
			validador.criarRegistroInvalido(linha,58,71,"Header Arquivo Pos(59) Tam(13) - Conta corrente inexistente");//conta + dv
		}
	}
	
	private void validaCodigoConvenio(LinhaArquivoModel linha) {
		//Campo alfa
		if( !headerLote.getCodConvenio().trim().equals("")){
			validador.criarRegistroInvalido(linha,32,52,"Header Arquivo Pos(33) Tam(20) - Código do convênio é inválido");
		}
	}
	private void validaCPFCNPJcliente(LinhaArquivoModel linha) {
		if( (!ValidaTipoDados.isCNPJValido(headerLote.getInscricao(2)) && !ValidaTipoDados.isCPFValido(headerLote.getInscricao(1) ))) {
			validador.criarRegistroInvalido(linha,18,33,"Header Arquivo Pos(19) Tam(14) - CPF/CNPJ diferente do cedente informado");
		}
	}
	private void validarCPFCNPJ(LinhaArquivoModel linha) {
		//Se o CPF/CNPJ for incompativel com o tipo de inscrição
		if(headerLote.getTipoInscricao().equals("1") && ValidaTipoDados.isCNPJValido(headerLote.getInscricao(2))){
			validador.criarRegistroInvalido(linha,18,33,"Header Arquivo Pos(19) Tam(14) - CPF/CNPJ do cedente é inválido para o tipo de inscrição (1-PF)");
			return;
		}else if(headerLote.getTipoInscricao().equals("2") && ValidaTipoDados.isCPFValido(headerLote.getInscricao(1))){
			validador.criarRegistroInvalido(linha,18,33,"Header Arquivo Pos(19) Tam(14) - CPF/CNPJ do cedente é inválido para o tipo de inscrição (2-PJ)");
			return;
		}
		
		if(headerLote.getTipoInscricao().matches("1|2")){
			switch (headerLote.getTipoInscricaoInt()) {
			case 1:
				if(!ValidaTipoDados.isCPFValido(headerLote.getInscricao(1))){
					validador.criarRegistroInvalido(linha,18,33,"Header Arquivo Pos(19) Tam(14) - CPF/CNPJ do cedente é inválido");
				}
				break;
			case 2:
				if(!ValidaTipoDados.isCNPJValido(headerLote.getInscricao(2))){
					validador.criarRegistroInvalido(linha,18,33,"Header Arquivo Pos(19) Tam(14) - CPF/CNPJ do cedente é inválido");
				}
				break;
			default:					
				break;
			}
		}
	}
	
	private void validarLoteServico(LinhaArquivoModel linha) {
		 if(! Pattern.matches("0000", headerLote.getLoteServico())){
			 validador.criarRegistroInvalido(linha, 3, 7, "Header Arquivo Pos(4) Tam(4) - Lote de serviço é diferente de '0000'");
		 }
	}
	private void validarCodigoBanco(LinhaArquivoModel linha){
		if(! headerLote.getCodBanco().equals("756")){
			validador.criarRegistroInvalido(linha, 0, 3, "Header Arquivo Pos(1) Tam(3) - Código do banco é diferente de '756'");
		}
	}
}

