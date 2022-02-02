package br.com.validadorcnab.validadorcnab240;

import java.util.regex.Pattern;

import br.com.validadorcnab.util.ValidaTipoDados;
import br.com.validadorcnab.validadorcnab240.Validador240.LinhaArquivoModel;


class ValidadorHeaderLoteCheque {
	
	private static ValidadorHeaderLoteCheque instance;
	public static ValidadorHeaderLoteCheque getInstance() {
		if (instance == null) {
			instance = new ValidadorHeaderLoteCheque();
		}
		return instance;
	}
	
	private ValidadorHeaderLoteCheque() {
		
	}
	
	
	public HeaderLoteCheque hc = null;
	ValidacaoCNAB240  vc = ValidacaoCNAB240.getInstance();
	
	public void  validaHeaderLoteCheque(LinhaArquivoModel linha) throws Exception {

		hc = new HeaderLoteCheque(linha.getConteudoLinha());

		validarCodigoBanco(linha);

		validarTipoOperacao(linha);

		validaTipoInscricao(linha);

		validaCPF_CNPJ(linha);

		validaCPF_CNPJ_cliente(linha);
		
		validaCodigoConvenio(linha);
		
		validaAgenciaMantenedora(linha);

		validarContaCorrenteExiste(linha);

		validaNomeEmpresaCedente(linha);
	}
	
	private void validaNomeEmpresaCedente(LinhaArquivoModel linha) {
		if(hc.getNomeCliente().trim().equals("")){
			vc.criarRegistroInvalido(linha, 72, 102, "Header Lote Cheque Pos(73) Tam(30) - Nome do cedente não informado");
		}
		
	}
	private void validarContaCorrenteExiste(LinhaArquivoModel linha) {
		if(! vc.isContaExiste(hc.getContaCorrenteDv())){
			vc.criarRegistroInvalido(linha, 58, 71, "Header Lote Cheque Pos(59) Tam(13) - Conta corrente inexistente");
		}
	}
	private void validaAgenciaMantenedora(LinhaArquivoModel linha) {
		if(!vc.isNumeric(hc.getAgenciaMantenedoraConta())){
			vc.criarRegistroInvalido(linha, 53, 58, "Header Lote Cheque Pos(53) Tam(5) - Agência mantenedora da conta é diferente da cooperativa configurada no sistema");
		}
	}
	private void validaCodigoConvenio(LinhaArquivoModel linha) {
		if(!hc.getCodConvenio().trim().equals("")){
			vc.criarRegistroInvalido(linha,32,52,"Header Lote Cheque Pos(33) Tam(20) - Código de convênio é inválido");
		}
	}
	private void validaCPF_CNPJ_cliente(LinhaArquivoModel linha) {
		if(hc.getTipoInscricao().matches("1|2") && ValidaTipoDados.isCNPJValido(hc.getInscricao(hc.getTipoInscricaoInt()))){
			vc.criarRegistroInvalido(linha,18,33,"Header Lote Cheque Pos(19) Tam(14) - CPF/CNPJ é diferente do CPF/CNPJ do cedente");
		}
	}
	
	private void validaCPF_CNPJ(LinhaArquivoModel linha) {
		/* Existem inscrições CPF que coincidem com CNPJ quando se completa com zeros à esquerda conforme recomenda o padrão CNAB240/400 
		*  Exemplo: CNPJ =00000868180823 ou CPF: 00868180823 (se validarmos com 11 ou 14 dígitos ambos serão válidos)
		*/
		if(ValidaTipoDados.isCNPJValido(hc.getInscricao(2)) && ValidaTipoDados.isCPFValido(hc.getInscricao(1)) && Pattern.matches("1|2",hc.getTipoInscricao())){
			return;//Desconsidera a validação.
		}
		
		//Se o CPF/CNPJ for incompativel com o tipo de inscrição
		if(hc.getTipoInscricao().equals("1") && ValidaTipoDados.isCNPJValido(hc.getInscricao(2))){
			vc.criarRegistroInvalido(linha,18,33,"Header Lote Cheque Pos(19) Tam(14) - CPF/CNPJ do cedente é inválido para o tipo de inscrição (1-PF)");
			return;
		}else if(hc.getTipoInscricao().equals("2") && ValidaTipoDados.isCPFValido(hc.getInscricao(1))){
			vc.criarRegistroInvalido(linha,18,33,"Header Lote Cheque Pos(19) Tam(14) - CPF/CNPJ do cedente é inválido para o tipo de inscrição (2-PJ)");
			return;
		}
		
		if(vc.isNumeric(hc.getTipoInscricao())){
			switch (hc.getTipoInscricaoInt()) {
			case 1:
				if( ! ValidaTipoDados.isCPFValido(hc.getInscricao(1))){
					vc.criarRegistroInvalido(linha,18,33,"Header Lote Cheque Pos(19) Tam(14) - CPF/CNPJ é inválido");
				}
				break;
			case 2:
				if( ! ValidaTipoDados.isCNPJValido(hc.getInscricao(2))){
					vc.criarRegistroInvalido(linha,18,33,"Header Lote Cheque Pos(19) Tam(14) - CPF/CNPJ é inválido");
				}
				break;
			default:
				break;
			}
		}
		
	}
	private void validaTipoInscricao(LinhaArquivoModel linha) {
		if(!Pattern.matches("1|2", hc.getTipoInscricao() )){
			vc.criarRegistroInvalido(linha,17,18,"Header Lote Cheque Pos(18) Tam(1) - Tipo de inscrição é diferente de 1-CPF e 2-CNPJ");
		}
	}
	private void validarTipoOperacao(LinhaArquivoModel linha) {
		if(! Pattern.matches("R|T", hc.getTipoOperacao())){
			vc.criarRegistroInvalido(linha, 8, 9, "Header Lote Cheque Pos(9) Tam(1) - Tipo de operação não é diferente de ('R','T')");
		}
	}
	private void validarCodigoBanco(LinhaArquivoModel linha){
		if(! hc.getCodBanco().equals("756")){
			vc.criarRegistroInvalido(linha, 0, 3, "Header Lote Cheque Pos(1) Tam(3) - Código do banco é diferente de '756'");
		}
	}
	
}
class HeaderLoteCheque{
	
	private String codBanco;
	private String tipoOperacao;
	private String tipoInscricao;
	private String inscricao;
	private String codConvenio;
	private String agenciaMantenedoraConta;
	private String contaCorrenteDv;
	private String nomeCliente;
	private String linhaHeaderLoteCheque;
	
	public HeaderLoteCheque(String linhaHeaderLoteCheque){
		this.linhaHeaderLoteCheque = linhaHeaderLoteCheque;
		
		setCodBanco(getParte(0, 3));
		setTipoOperacao(getParte(8, 9));
		setTipoInscricao(getParte(17,18));
		setInscricao(getParte(18,32));
		setCodConvenio(getParte(32, 52)); 
		setAgenciaMantenedoraConta(getParte(52, 57));
		setContaCorrenteDv(getParte(58,71));
		setNomeCliente(getParte(72, 102));
	}
	
	public String getNomeCliente() {
		return nomeCliente;
	}

	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}

	public String getContaCorrenteDv() {
		return contaCorrenteDv;
	}

	public void setContaCorrenteDv(String contaCorrenteDv) {
		this.contaCorrenteDv = contaCorrenteDv;
	}

	public String getAgenciaMantenedoraConta() {
		return agenciaMantenedoraConta;
	}
	public int getAgenciaMantenedoraContaInt() {
		return Integer.parseInt(agenciaMantenedoraConta);
	}

	public void setAgenciaMantenedoraConta(String agenciaMantenedoraConta) {
		this.agenciaMantenedoraConta = agenciaMantenedoraConta;
	}

	public String getCodConvenio() {
		return codConvenio;
	}

	public void setCodConvenio(String codConvenio) {
		this.codConvenio = codConvenio;
	}
	
	public String getInscricao(int tipo) {
		if(tipo == 1){
			return inscricao.substring(3, 14);//Return 11 dígitos do cpf
		}else if(tipo==2){
			return inscricao.substring(0,14);//Return 14 dígitos do cpf
		}
		return inscricao;
	}

	public void setInscricao(String inscricao) {
		this.inscricao = inscricao;
	}

	public String getTipoInscricao() {
		return tipoInscricao;
	}
	public int getTipoInscricaoInt() {
		return Integer.parseInt(tipoInscricao);
	}

	public void setTipoInscricao(String tipoInscricao) {
		this.tipoInscricao = tipoInscricao;
	}

	public String getTipoOperacao() {
		return tipoOperacao;
	}
	public void setTipoOperacao(String tipoOperacao) {
		this.tipoOperacao = tipoOperacao;
	}
	public String getParte(int de, int ate ){
		return this.linhaHeaderLoteCheque.substring(de, ate);
	}
	public String getCodBanco() {
		return codBanco;
	}
	public void setCodBanco(String codigoBanco) {
		this.codBanco = codigoBanco;
	}
	
	
	
}
