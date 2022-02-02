package br.com.validadorcnab.model;

public class SegmentoQModel {
	
	private String codBanco;
	private String codigoMovimento;
	private String tipoInscricao;
	private String inscricao;
	private String nomeSacado;
	private String endereco;
	private String bairro;
	private String cep;
	private String sufixoCep;
	private String uf;
	private String tipoInscricaoSacador;
	private String inscricaoSacador;
	private String nomeSacadorAvalista;	

	private final String linhaSegmentoQ;

	
	public SegmentoQModel(String linhaSegmentoQ){
		this.linhaSegmentoQ = linhaSegmentoQ;
		
		setCodBanco(getParte(0, 3));
		setCodMovimento(getParte(15, 17));
		setTipoInscricao(getParte(17, 18));
		setInscricao(getParte(18, 33));
		setNomeSacado(getParte(33, 73));
		setEndereco(getParte(73, 113));
		setBairro(getParte(113, 128));
		setCep(getParte(128, 133));
		setSufixoCep(getParte(133,136 ));
		setUf(getParte(151,153));
		setTipoInscricaoSacador(getParte(153,154 ));
		setInscricaoSacador(getParte(154,169 ));
		setNomeSacadorAvalista(getParte(169,209));		
	}

	public String getNomeSacadorAvalista() {
		return nomeSacadorAvalista;
	}

	public void setNomeSacadorAvalista(String nomeSacadorAvalista) {
		this.nomeSacadorAvalista = nomeSacadorAvalista;
	}

	public String getTipoInscricaoSacador() {
		return tipoInscricaoSacador;
	}
	public int getTipoInscricaoSacadorInt() {
		return Integer.parseInt(tipoInscricaoSacador);
	}

	public void setTipoInscricaoSacador(String tipoInscricaoSacador) {
		this.tipoInscricaoSacador = tipoInscricaoSacador;
	}

	public String getInscricaoSacador() {
		return inscricaoSacador;
	}
	
	public String getInscricaoSacadorCPF_CNPJ(String tipo, String inscricao) {		
		if(tipo.equals("1")){
			return inscricao.substring(4, 15);//Return 11 dígitos do cpf
		}else if(tipo.equals("2")){
			return inscricao.substring(1,15);//Return 14 dígitos do cpf
		}
		return null;
	}

	public void setInscricaoSacador(String inscricaoSacador) {
		this.inscricaoSacador = inscricaoSacador;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getSufixoCep() {
		return sufixoCep;
	}

	public void setSufixoCep(String sufixoCep) {
		this.sufixoCep = sufixoCep;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getNomeSacado() {
		return nomeSacado;
	}

	public void setNomeSacado(String nomeSacado) {
		this.nomeSacado = nomeSacado;
	}

	public String getInscricaoPF_PJ() {
		if(getTipoInscricao().equals("1")){
			return inscricao.substring(4, 15);//Return 11 dígitos do cpf
		}else if(getTipoInscricao().equals("2")){
			return inscricao.substring(1,15);//Return 14 dígitos do cpf
		}
		return inscricao;
	}
	public String getInscricao15Digitos() {
		return inscricao;
	}

	public void setInscricao(String inscricao) {
		this.inscricao = inscricao;
	}

	public String getTipoInscricao() {
		return tipoInscricao;
	}
	public String getTipoInscricaoIntr() {
		return tipoInscricao;
	}
	public int getTipoInscricaoInt() {
		return Integer.parseInt(tipoInscricao);
	}	
	
	public void setTipoInscricao(String tipoInscricao) {
		this.tipoInscricao = tipoInscricao;
	}

	public String getParte(int de, int ate ){
		return this.linhaSegmentoQ.substring(de, ate);
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
