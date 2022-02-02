package br.com.validadorcnab.model;

public class SegmentoDModel {
	
	private String codBanco;
	private String codigoMovimento;
	private String codFinalidade;
	private String codCmc7;
	private String tipoInscricao;
	private String inscricao;
	private String dataDepositoCheque;
	private String dataPrevistaDebito;
	private String formaEntrada;
	private final String linhaSegmentoD;

	
	public SegmentoDModel(String linhaSegmentoD){
		this.linhaSegmentoD = linhaSegmentoD;
		
		setCodBanco(getParte(0, 3));
		setCodMovimento(getParte(15, 17));
		setCodFinalidade(getParte(17, 19));
		setFormaEntrada(getParte(19, 20));
		setCodCmc7(getParte(20, 54));
		setTipoInscricao(getParte(54, 55));
		setInscricao(getParte(55, 69));
		setDataDepositoCheque(getParte(92, 100));
		setDataPrevistaDebito(getParte(100, 108));		
	}
	
	public String getDataPrevistaDebito() {
		return dataPrevistaDebito;
	}
	public void setDataPrevistaDebito(String dataPrevistaDebito) {
		this.dataPrevistaDebito = dataPrevistaDebito;
	}
	public String getFormaEntrada() {
		return formaEntrada;
	}
	public void setFormaEntrada(String formaEntrada) {
		this.formaEntrada = formaEntrada;
	}
	public String getDataDepositoCheque() {
		return dataDepositoCheque;
	}
	public void setDataDepositoCheque(String dataDepositoCheque) {
		this.dataDepositoCheque = dataDepositoCheque;
	}
	public String getTipoInscricao() {
		return tipoInscricao;
	}
	public int getTipoInscricaoInt() {
		return Integer.parseInt(tipoInscricao);
	}
	
	public String getInscricao(int tipo) {
		if(tipo == 1){
			return inscricao.substring(3, 14);//Return 11 dígitos do cpf
		}else if(tipo==2){
			return inscricao.substring(0,14);//Return 14 dígitos do cpf
		}
		return inscricao;
	}

	public void setTipoInscricao(String tipoInscricao) {
		this.tipoInscricao = tipoInscricao;
	}

	public String getInscricao() {
		if(getTipoInscricao().equals("1")){
			return inscricao.substring(3, 14);//Return 11 dígitos do cpf
		}else if(getTipoInscricao().equals("2")){
			return inscricao.substring(0,14);//Return 14 dígitos do cpf
		}
		return inscricao;
	}

	public void setInscricao(String inscricao) {
		this.inscricao = inscricao;
	}
	public String getCodCmc7() {
		return codCmc7;
	}
	public void setCodCmc7(String codCmc7) {
		this.codCmc7 = codCmc7;
	}
	public String getCodFinalidade() {
		return codFinalidade;
	}
	public void setCodFinalidade(String codFinalidade) {
		this.codFinalidade = codFinalidade;
	}
	public String getParte(int de, int ate ){
		return this.linhaSegmentoD.substring(de, ate);
	}
	public String getCodBanco() {
		return codBanco;
	}
	public void setCodBanco(String codigoBanco) {
		this.codBanco = codigoBanco;
	}
	public String getCodoMovimento() {
		return codigoMovimento;
	}
	public void setCodMovimento(String codigoMovimento) {
		this.codigoMovimento = codigoMovimento;
	}

}
