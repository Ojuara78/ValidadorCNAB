package br.com.validadorcnab.model;

public class SegmentoRModel {
	
	private String codBanco;
	private String codigoMovimento;
	private String codDesconto2;
	private String codDesconto3;
	private String codMulta;
	private String dataMulta;
	private String valorMulta;
	private String codModalidade;
	private String agenciaDebitoMantenedora;
	private String contaCorrentaDebito;
	private String dataDesconto2;
	private String valorDesconto2;
	private String dataDesconto3;
	private String valorDesconto3;
	
	private final String linhaSegmentoR;

	
	public SegmentoRModel(String linhaSegmentoR){
		
		this.linhaSegmentoR = linhaSegmentoR;
		
		setCodBanco(getParte(0, 3));
		setCodMovimento(getParte(15, 17));
		setCodDesconto2(getParte(17, 18));
		setCodDesconto3(getParte(41, 42));
		setCodMulta(getParte(65, 66));
		setDataMulta(getParte(66, 74));
		setValorMulta(getParte(74, 89));
		setAgenciaDebitoMantenedora(getParte(210, 215));
		setContaCorrentaDebito(getParte(216, 228));
		setDataDesconto2(getParte(18	, 26));
		setValorDesconto2(getParte(26,41));
		setDataDesconto3(getParte(42,50));
		setValorDesconto3(getParte(50,65));
		
	}
	
	public String getDataDeconto2() {
		return dataDesconto2;
	}

	public void setDataDesconto2(String dataDeconto2) {
		this.dataDesconto2 = dataDeconto2;
	}

	public String getContaCorrentaDebito() {
		return contaCorrentaDebito;
	}

	public void setContaCorrentaDebito(String contaCorrentaDebito) {
		this.contaCorrentaDebito = contaCorrentaDebito;
	}

	public String getAgenciaDebitoMantenedora() {
		return agenciaDebitoMantenedora;
	}
	public int getAgenciaDebitoMantenedoraInt() {
		return Integer.parseInt(agenciaDebitoMantenedora);
	}

	public void setAgenciaDebitoMantenedora(String agenciaDebitoMantenedora) {
		this.agenciaDebitoMantenedora = agenciaDebitoMantenedora;
	}

	public String getValorMulta() {
		return valorMulta;
	}

	public void setValorMulta(String valorMulta) {
		this.valorMulta = valorMulta;
	}

	public String getCodModalidade() {
		return codModalidade;
	}

	public void setCodModalidade(String codModalidade) {
		this.codModalidade = codModalidade;
	}

	public String getCodMulta() {
		return codMulta;
	}

	public void setCodMulta(String codMulta) {
		this.codMulta = codMulta;
	}

	public String getDataMulta() {
		return dataMulta;
	}

	public void setDataMulta(String dataMulta) {
		this.dataMulta = dataMulta;
	}

	public String getCodDesconto3() {
		return codDesconto3;
	}

	public void setCodDesconto3(String codDesconto3) {
		this.codDesconto3 = codDesconto3;
	}

	public String getCodDesconto2() {
		return codDesconto2;
	}

	public void setCodDesconto2(String codDesconto2) {
		this.codDesconto2 = codDesconto2;
	}

	public String getParte(int de, int ate ){
		return this.linhaSegmentoR.substring(de, ate);
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

	public String getValorDesconto2() {
		return valorDesconto2;
	}

	public void setValorDesconto2(String valorDesconto2) {
		this.valorDesconto2 = valorDesconto2;
	}

	public String getDataDeconto3() {
		return dataDesconto3;
	}

	public void setDataDesconto3(String dataDeconto3) {
		this.dataDesconto3 = dataDeconto3;
	}

	public String getValorDesconto3() {
		return valorDesconto3;
	}

	public void setValorDesconto3(String valorDesconto3) {
		this.valorDesconto3 = valorDesconto3;
	}

}
