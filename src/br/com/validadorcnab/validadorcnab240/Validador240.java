package br.com.validadorcnab.validadorcnab240;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import br.com.validadorcnab.enuns.ModalidadeProdutoEnum;
import br.com.validadorcnab.interfaces.Validador;
import br.com.validadorcnab.model.RegistroInvalidoArquivo;

public abstract class Validador240  implements Validador{
	
	public static final int Header_de_Arquivo = 1;
	public static final int Header_de_Lote_Cheque = 2;
	public static final int Detalhe_Cheque_Segmento_D = 3;	
	public static final int Trailer_de_Lote_Cheque = 4;	
	public static final int Header_de_Lote_Titulo = 5;	
	public static final int Detalhe_Titulo_Segmento_P = 6;
	public static final int Detalhe_Titulo_Segmento_Q = 7;
	public static final int Detalhe_Titulo_Segmento_R = 8;
	public static final int Detalhe_Titulo_Segmento_S = 9;
	public static final int Trailer_de_Lote_Titulo = 10;	
	public static final int Trailer_de_Arquivo = 11;
	public static final int Layout_Desconhecido = 18;
		
	private Date dataMovimentoAtual;
	private List<String>  listaNossoNumero;
	public  List<String> listaCMC7;
	private int segmentoAnterior;
	private boolean isErroEstrutura;
	private String msgErroEstrutura;
	private int numeroLinha; 
	
	public List<LinhaArquivoModel> listaLinhasArquivo;	
	public List<RegistroInvalidoArquivo> listaRegistroInvalido;
		
	public void lerArquivoValidandoSequencia(BufferedReader arquivo) throws IOException{
		
		inicializarVariaveis();

		int headerAtual = 0;
		while (arquivo.ready()) {
			String  textoLinha = (arquivo.readLine());	

			if(!validaEstruturaLinha(textoLinha))
				break;

				String registro = textoLinha.substring(7,8);
				int codSegmento = 0;

				if(registro.equals("0") && textoLinha.charAt(13) == ' '){
					
					validaSequencia(Header_de_Arquivo);
					codSegmento = Header_de_Arquivo;// "HA";
					headerAtual = Header_de_Arquivo;
					
				}else if((registro+textoLinha.substring(8,11)).equals("1R06") || (registro+textoLinha.substring(7,11)).equals("1T06")){
					validaSequencia(Header_de_Lote_Cheque);
					codSegmento = Header_de_Lote_Cheque; // "HC";
					headerAtual = Header_de_Lote_Cheque;
				}else if(registro.equals("3") && textoLinha.charAt(13) == 'D'){
					validaSequencia(Detalhe_Cheque_Segmento_D);
					codSegmento = Detalhe_Cheque_Segmento_D;// "D";
					registrarCMC7Arquivo(textoLinha);//Registra o CMC7 para facilitar a validação de repetição
					
				}else if(registro.equals("5") && headerAtual == Header_de_Lote_Cheque ) {
					validaSequencia(Trailer_de_Lote_Cheque);
					codSegmento = Trailer_de_Lote_Cheque;// "TC";
					
				}else if(registro.equals("1") && textoLinha.substring(9,11).equals("01")){
					validaSequencia(Header_de_Lote_Titulo);
					codSegmento = Header_de_Lote_Titulo;//"HT";
					headerAtual = Header_de_Lote_Titulo;
					
				}else if(registro.equals("3") && textoLinha.charAt(13) == 'P'){
					validaSequencia(Detalhe_Titulo_Segmento_P);
					codSegmento = Detalhe_Titulo_Segmento_P;//"P";
					registrarNossoNumerosArquivo(textoLinha);//Registra o nosso numero para facilitar a validação de repetição.
				}else if(registro.equals("3") && textoLinha.substring(13,15).trim().equals("Q") ){
					validaSequencia(Detalhe_Titulo_Segmento_Q);
					codSegmento = Detalhe_Titulo_Segmento_Q;//"Q";	
					
				}else if(registro.equals("3") && textoLinha.charAt(13) == 'R'){
					validaSequencia(Detalhe_Titulo_Segmento_R);
					codSegmento = Detalhe_Titulo_Segmento_R;//"R";
					
				}else if(registro.equals("3") && textoLinha.charAt(13) == 'S'){
					validaSequencia(Detalhe_Titulo_Segmento_S);
					codSegmento = Detalhe_Titulo_Segmento_S;//"S";
					
				}else if(registro.equals("5") && headerAtual == Header_de_Lote_Titulo){
					validaSequencia(Trailer_de_Lote_Titulo);
					codSegmento = Trailer_de_Lote_Titulo;//"TT";
					
				}else if(registro.equals("9")){
					validaSequencia(Trailer_de_Arquivo);
					codSegmento = Trailer_de_Arquivo;//"TA";
					
				}else{
					validaSequencia(Layout_Desconhecido);
					codSegmento = Layout_Desconhecido;
					
				}

				if(isErroEstrutura){
					break;
				}

				listaLinhasArquivo.add(getLinha(textoLinha,numeroLinha,codSegmento));
				numeroLinha++;
			}//Fim while
		existeTrailerArquivo();
	}

	private void existeTrailerArquivo() {
		if(!isErroEstrutura &&  segmentoAnterior != Trailer_de_Arquivo ){
			isErroEstrutura = true;
			msgErroEstrutura = "A última linha do arquivo não corresponde a um trailer de arquivo";
		}
	}
	
	public boolean validaEstruturaLinha(String textoLinha){
		if(textoLinha.length() < 240){
			isErroEstrutura = true;
			msgErroEstrutura = "Linha "+numeroLinha+" possue menos que 240 caracteres";
			return false;
		}else if(textoLinha.length() > 240){
			isErroEstrutura = true;
			msgErroEstrutura = "Linha "+numeroLinha+" possue mais que 240 caracteres";
			return false;
		}else if(textoLinha.trim().equals("")){
			isErroEstrutura = true;
			msgErroEstrutura = "Linha "+numeroLinha+" não possue informação";
			return false;
		}
		return true;
	}
	
	public void validaSequencia(int segmentoNovo){
		switch (segmentoNovo) {
		case Layout_Desconhecido:
			isErroEstrutura = true;
			msgErroEstrutura = "A linha n° "+numeroLinha+" não foi reconhecida como um layout CNAB240";
			break;
		case Header_de_Arquivo:
			if(segmentoAnterior == Header_de_Arquivo || segmentoAnterior != 0  ){
				isErroEstrutura = true;
				msgErroEstrutura = "Header de arquivo está repetido";				 
			}
			break;
		case Header_de_Lote_Cheque:
			if( (segmentoAnterior != Header_de_Arquivo) && (segmentoAnterior != Trailer_de_Lote_Cheque)){
				isErroEstrutura = true;
				msgErroEstrutura = "Header de lote de cheque não está precedido por um header de arquivo ou trailer de lote de cheque";
			}
			break;
		case Detalhe_Cheque_Segmento_D:
			if( (segmentoAnterior != Header_de_Lote_Cheque ) && (segmentoAnterior != Detalhe_Cheque_Segmento_D) ){
				isErroEstrutura = true;
				msgErroEstrutura = "Segmento (D) não está precedido por um header de lote de cheque ou segmento(D)";
			}
			break;
		case Trailer_de_Lote_Cheque: /*Cheques de conta corrente diferentes são colocados em diferentes lotes*/
			if( segmentoAnterior != Detalhe_Cheque_Segmento_D && segmentoAnterior != Header_de_Lote_Cheque){
				isErroEstrutura = true;
				msgErroEstrutura = "Trailer de lote de cheque não está precedido por um header de lote de cheque ou segmento (D)";
			}
			break;
		case Header_de_Lote_Titulo:
			if( (segmentoAnterior != Trailer_de_Lote_Cheque ) && (segmentoAnterior != Header_de_Arquivo )){
				isErroEstrutura = true;
				msgErroEstrutura = "Header de lote de título não está precedido por header de arquivo ou trailer de lote de cheque";
			}
			break;
		case Detalhe_Titulo_Segmento_P:
			if( (segmentoAnterior != Header_de_Lote_Titulo ) && (segmentoAnterior != Detalhe_Titulo_Segmento_S ) && (segmentoAnterior != Detalhe_Titulo_Segmento_R ) && (segmentoAnterior != Detalhe_Titulo_Segmento_Q ) ){
				isErroEstrutura = true;
				msgErroEstrutura = "Segmento (P) não está precedido por header de lote de título ou segmento (R, S, Q ou Q2)";
			}
			break;
		case Detalhe_Titulo_Segmento_Q:
			if( segmentoAnterior != Detalhe_Titulo_Segmento_P ){
				isErroEstrutura = true;
				msgErroEstrutura = "Segmento (Q) não está precedido por um segmento (P)";
			}
			break;

		case Detalhe_Titulo_Segmento_R:
			if(segmentoAnterior != Detalhe_Titulo_Segmento_Q){
				isErroEstrutura = true;
				msgErroEstrutura = "Segmento (R) não está precedido por um segmento (Q)";
			}
			break;
			
		case Detalhe_Titulo_Segmento_S:
			if( (segmentoAnterior != Detalhe_Titulo_Segmento_R) && (segmentoAnterior != Detalhe_Titulo_Segmento_Q)){
				isErroEstrutura = true;
				msgErroEstrutura = "Segmento (S) não está precedido por um segmento (R, Q)";
			}
			break;
			
		case Trailer_de_Lote_Titulo:
			if( (segmentoAnterior != Detalhe_Titulo_Segmento_S) && 
					( segmentoAnterior != Detalhe_Titulo_Segmento_R) && 
					( segmentoAnterior != Detalhe_Titulo_Segmento_Q)){
				isErroEstrutura = true;
				msgErroEstrutura = "Trailer de lote título não está precedido por um segmento (S, R, Q)";
			}
			break;
			
		case Trailer_de_Arquivo:
			if( segmentoAnterior != Trailer_de_Lote_Titulo && segmentoAnterior != Trailer_de_Lote_Cheque){
				isErroEstrutura = true;
				msgErroEstrutura = "Trailer de arquivo não está precedido por trailer de lote de título ou trailer de lote de cheque";
			}
			break;
		default:
			isErroEstrutura = true;
			msgErroEstrutura = "Layout desconhecido";
			break;
		}
		segmentoAnterior = segmentoNovo;
	}
	
	public void inicializarVariaveis(){
		numeroLinha=1;
		segmentoAnterior = 0;
		isErroEstrutura = false;
		msgErroEstrutura = "";
		listaCMC7 = new ArrayList<String>();
		listaNossoNumero = new ArrayList<String>();
		listaLinhasArquivo = new ArrayList<LinhaArquivoModel>();
		listaRegistroInvalido = new ArrayList<RegistroInvalidoArquivo>();
	}
	
	public boolean getModalidadePorId(String modalidade) {
		if(!isNumeric(modalidade)) {
			return false;
		}
		
		if(!ModalidadeProdutoEnum.contains(modalidade)) {
			return false;
		}
		return true;
	}
	
	public Date getDataMovimentoAtual() {
		if(dataMovimentoAtual == null) {
			dataMovimentoAtual = new Date();
		}
		return dataMovimentoAtual;	
	}
	
	public void setDataMovimentoAtual(Date dataMovimentoAtual) {
		this.dataMovimentoAtual = dataMovimentoAtual;
	}

	public boolean isContaExiste(String contaCorrente) {
		return isNumeric(contaCorrente);
	}
	
	public void adicionaRegistroInvalido(RegistroInvalidoArquivo registroInvalidoArquivo){
		listaRegistroInvalido.add(registroInvalidoArquivo);
	}
	
	public void registrarNossoNumerosArquivo(String linha){
		listaNossoNumero.add(linha.substring(37, 47));
	}
	
	public boolean isNossoNumeroRepetido(String nossoNumero){
		if(nossoNumero.equals("0000000000")){//O usuário poderá informar NN zerados
				return false;
		}
		int coutnNN=0;
		for(String nn: listaNossoNumero){
			if(nn.equals(nossoNumero))
				coutnNN++;
			if(coutnNN > 1){
				return true;
			}
		}
		return false;
	}
	
	public void registrarCMC7Arquivo(String linha){
		listaCMC7.add(linha.substring(20, 54)); 
	}
	
	public void populaLinha(LinhaArquivoModel linha, String texto, int numeroLinha  , int tipoLinha){
		linha.setConteudoLinha(texto);
		linha.setNumero(numeroLinha);
		linha.setTipoLinha(tipoLinha);
	}
	
	public boolean isNumeric(String valor){
		if(valor.equals(""))
			return false;
		return  Pattern.matches("\\d*",valor);
	}
	
	@Override
	public boolean isErroEstrutura() {
		return isErroEstrutura;
	}

	public void setErroEstrutura(boolean isErroEstrutura) {
		this.isErroEstrutura = isErroEstrutura;
	}

	@Override
	public String getMsgErroEstrutura() {
		return msgErroEstrutura;
	}

	public void setMsgErroEstrutura(String msgErroEstrutura) {
		this.msgErroEstrutura = msgErroEstrutura;
	}

	public void setNumeroLinha(int numeroLinha) {
		this.numeroLinha = numeroLinha;
	}
	
	public LinhaArquivoModel getLinha(String texto, int numeroLinha  , int tipoLinha){
		
		LinhaArquivoModel linhaArquivoModel = new LinhaArquivoModel();
		
		linhaArquivoModel.setConteudoLinha(texto);
		linhaArquivoModel.setNumero(numeroLinha);
		linhaArquivoModel.setTipoLinha(tipoLinha);

		return linhaArquivoModel;
	}
	public void criarRegistroInvalido(LinhaArquivoModel linha, int inicio, int fim, String ocorrencia ) {

		RegistroInvalidoArquivo regInv = new RegistroInvalidoArquivo();

		regInv.setNumLinha(linha.getNumero());
		regInv.setPosicaoInical(inicio);
		regInv.setPosicaoFinal(fim);
		regInv.setValorEncontrado(linha.getParte(inicio, fim));
		regInv.setOcorrencia(ocorrencia);

		if(!listaRegistroInvalido.contains(regInv)) {
			listaRegistroInvalido.add(regInv);       
		}
	}
	
	public class LinhaArquivoModel{
		private	int numero;
		private	String conteudoLinha;
		private	int tipoLinha;
		
			public int getNumero() {
				return numero;
			}
			public void setNumero(int numero) {
				this.numero = numero;
			}
			public String getConteudoLinha() {
				return conteudoLinha;
			}
			public void setConteudoLinha(String conteudoLinha) {
				this.conteudoLinha = conteudoLinha;
			}
			
			public int getTipoLinha() {
				return tipoLinha;
			}
			public void setTipoLinha(int tipoLinha) {
				this.tipoLinha = tipoLinha;
			}
			public String getParte(int de, int ate ){
				return getConteudoLinha().substring(de, ate);
			}
		}

	@Override
	public Integer getNumeroLinha() {
		return numeroLinha;
	}

	@Override
	public List<RegistroInvalidoArquivo> getListaRegistroInvalido() {
		return listaRegistroInvalido;
	}
}

