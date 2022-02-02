package br.com.validadorcnab.model;

public class SegmentoPModel {
	
	private String codBanco;
	private String codigoMovimento;
	private String agenciaMantenedoraConta;
	private String contaCorrente;
	private String nossoNumero;
	private String codCarteira;
	private String tipoBloqueto;
	private String aceite;
	private String dataVencimento;
	private String dataEmissao;
	private String codMora;
	private String codDesconto1;
	private String dataDesconto1;
	private String codMoeda;
	private String codProtesto;
	private String diasProtesto;
	private String vlrAbatimento;
	private String vlrTitulo;	
	private String vlrDesconto1;
	private String vlrIof;
	private String contratoGarantia;
	private String codModalidade;
	private String tipoEmissao;
	private String especieDocumento;
	private String linhaSegmentoP;
	
	public SegmentoPModel(String linhaSegmentoP){
		
		this.linhaSegmentoP = linhaSegmentoP;
		
		setCodBanco(getParte(0, 3));
		setCodMovimento(getParte(15, 17));
		setAgenciaMantenedoraConta(getParte(17,22));
		setContaCorrenteDv(getParte(23, 36)); // Número da conta + DV 
		setNossoNumero(getParte(37,47));
		setCodModalidade(getParte(50, 51));
		setCodCarteira(getParte(57,58));
		setTipoBloqueto(getParte(60,61));
		setAceite(getParte(108, 109));
		
		setDataVencimento(getParte(77, 85));
		setDataEmissao(getParte(109, 117));
		setCodMora(getParte(117, 118));
		setCodDesconto1(getParte(141, 142));
		setDataDesconto1(getParte(142, 150));
		setCodMoeda(getParte(227, 229));
		setCodProtesto(getParte(220, 221));
		setDiasProtesto(getParte(221,223));
		setVlrAbatimento(getParte(180, 195));
		setVlrTitulo(getParte(85, 100));
		setVlrDesconto1(getParte(150, 165));
		setContratoGarantia(getParte(229, 239));
		setTipoEmissao(getParte(60, 61));
		setVlrIof(getParte(165, 180));
		setEspecieDocumento(getParte(106, 108));
	}
	
	public String getVlrIof() {
		return vlrIof;
	}

	public void setVlrIof(String vlrIof) {
		this.vlrIof = vlrIof;
	}

	public String getTipoEmissao() {
		return tipoEmissao;
	}
	public void setTipoEmissao(String criterioEmissao) {
		this.tipoEmissao = criterioEmissao;
	}
	public String getCodModalidade() {
		return codModalidade;
	}
	public void setCodModalidade(String codModalidade) {
		this.codModalidade = codModalidade;
	}
	public String getParte(int de, int ate ){
		return this.linhaSegmentoP.substring(de, ate);
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
	public String getAgenciaMantenedoraConta() {
		return agenciaMantenedoraConta;
	}
	public int getAgenciaMantenedoraContaInt() {
		return Integer.parseInt( agenciaMantenedoraConta);
	}
	public void setAgenciaMantenedoraConta(String agenciaMantenedoraConta) {
		this.agenciaMantenedoraConta = agenciaMantenedoraConta;
	}
	public String getContaCorrenteDv() {
		return contaCorrente;
	}
	public void setContaCorrenteDv(String contaCorrente) {
		this.contaCorrente = contaCorrente;
	}
	public String getNossoNumero() {
		return nossoNumero;
	}
	public void setNossoNumero(String nossoNumero) {
		this.nossoNumero = nossoNumero;
	}
	public String getCodCarteira() {
		return codCarteira;
	}
	public void setCodCarteira(String codCarteira) {
		this.codCarteira = codCarteira;
	}
	public String getTipoBloqueto() {
		return tipoBloqueto;
	}
	public void setTipoBloqueto(String tipoBloqueto) {
		this.tipoBloqueto = tipoBloqueto;
	}
	public String getAceite() {
		return aceite;
	}
	public void setAceite(String aceite) {
		this.aceite = aceite;
	}
	public String getDataVencimento() {
		return dataVencimento;
	}
	public void setDataVencimento(String dataVencimento) {
		this.dataVencimento = dataVencimento;
	}
	public String getDataEmissao() {
		return dataEmissao;
	}
	public void setDataEmissao(String dataEmissao) {
		this.dataEmissao = dataEmissao;
	}
	public String getCodMora() {
		return codMora;
	}
	public void setCodMora(String codMora) {
		this.codMora = codMora;
	}
	public String getCodDesconto1() {
		return codDesconto1;
	}
	public void setCodDesconto1(String codDesconto1) {
		this.codDesconto1 = codDesconto1;
	}
	public String getDataDesconto1() {
		return dataDesconto1;
	}
	public void setDataDesconto1(String dataDesconto1) {
		this.dataDesconto1 = dataDesconto1;
	}
	public String getCodMoeda() {
		return codMoeda;
	}
	public void setCodMoeda(String codMoeda) {
		this.codMoeda = codMoeda;
	}
	public String getCodProtesto() {
		return codProtesto;
	}
	public void setCodProtesto(String codProtesto) {
		this.codProtesto = codProtesto;
	}
	public String getDiasProtesto() {
		return diasProtesto;
	}
	public void setDiasProtesto(String diasProtesto) {
		this.diasProtesto = diasProtesto;
	}
	public String getVlrAbatimento() {
		return vlrAbatimento;
	}
	public void setVlrAbatimento(String vlrAbatimento) {
		this.vlrAbatimento = vlrAbatimento;
	}
	public String getVlrTitulo() {
		return vlrTitulo;
	}
	public void setVlrTitulo(String vlrTitulo) {
		this.vlrTitulo = vlrTitulo;
	}
	public String getVlrDesconto1() {
		return vlrDesconto1;
	}
	public void setVlrDesconto1(String vlrDesconto1) {
		this.vlrDesconto1 = vlrDesconto1;
	}
	public String getContratoGarantia() {
		return contratoGarantia;
	}
	public void setContratoGarantia(String contratoGarantia) {
		this.contratoGarantia = contratoGarantia;
	}
	public String getEspecieDocumento() {
		return especieDocumento;
	}
	public void setEspecieDocumento(String especieDocumento) {
		this.especieDocumento = especieDocumento;
	}

}
