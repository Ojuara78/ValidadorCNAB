package br.com.validadorcnab.model;

public class HeaderLoteTitulo {
	
	private String codBanco;
	private String tipoOperacao;
	private String tipoInscricao;
	private String inscricao;
	private String codConvenio;
	private String agenciaMantenedoraConta;
	private String contaCorrenteDv;	
	private final String linhaHeaderLoteTitulo;
	
	public HeaderLoteTitulo(String linhaSegmentoP){
		this.linhaHeaderLoteTitulo = linhaSegmentoP;
		
		setCodBanco(getParte(0, 3));
		setTipoOperacao(getParte(8, 9));
		setTipoInscricao(getParte(17,18));
		setInscricao(getParte(19,33));
		setCodConvenio(getParte(33, 53)); 
		setAgenciaMantenedoraConta(getParte(53, 58));
		setContaCorrenteDv(getParte(59,72));
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
	public String getInscricao() {
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
		return this.linhaHeaderLoteTitulo.substring(de, ate);
	}
	public String getCodBanco() {
		return codBanco;
	}
	public void setCodBanco(String codigoBanco) {
		this.codBanco = codigoBanco;
	}
	
	public String getInscricao(int tipo) {
		if(tipo == 1){
			return inscricao.substring(3, 14);//Return 11 dígitos do cpf
		}else if(tipo==2){
			return inscricao.substring(0,14);//Return 14 dígitos do cpf
		}
		return inscricao;
	}
}
