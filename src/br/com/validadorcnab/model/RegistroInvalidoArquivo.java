package br.com.validadorcnab.model;

public class RegistroInvalidoArquivo  {
	
	private int numLinha;
	private String valorEncontrado;
	private int posicaoInicial;
	private int posicaoFinal;
	private String ocorrencia;	
	
	public String getOcorrencia() {
		return ocorrencia;
	}
	public void setOcorrencia(String ocorrencia) {
		this.ocorrencia = ocorrencia;
	}
	public int getNumLinha() {
		return numLinha;
	}
	public void setNumLinha(int numLinha) {
		this.numLinha = numLinha;
	}
	public String getValorEncontrado() {
		return valorEncontrado;
	}
	public void setValorEncontrado(String valorEncontrado) {
		this.valorEncontrado = valorEncontrado;
	}
	public int getPosicaoInicial() {
		return posicaoInicial;
	}
	public void setPosicaoInical(int posicaoInical) {
		this.posicaoInicial = posicaoInical;
	}
	public int getPosicaoFinal() {
		return posicaoFinal;
	}
	public void setPosicaoFinal(int posicaoFinal) {
		this.posicaoFinal = posicaoFinal;
	}
	
	@Override
	public boolean equals(Object obj) {
		return (this.getNumLinha() == ((RegistroInvalidoArquivo)obj).getNumLinha()) && 
        (this.getOcorrencia().equals(((RegistroInvalidoArquivo)obj).getOcorrencia()));
	}
}
