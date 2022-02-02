package br.com.validadorcnab.validadorCnab400;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import br.com.validadorcnab.enuns.ModalidadeProdutoEnum;
import br.com.validadorcnab.interfaces.Validador;
import br.com.validadorcnab.model.RegistroInvalidoArquivo;


public abstract class Validador400 implements Validador{

	public static final int Header_de_Arquivo400 = 14;
	public static final int Detalhe_Titulo_CNAB400 = 15;
	public static final int Trailer_de_Arquivo400 = 16;
	
	public static final int Layout_Desconhecido = 18;

	private Date dataMovimentoAtual;
	private List<String>  listaNossoNumero;
	public int segmentoAnterior;
	public boolean isErroEstrutura;
	public String msgErroEstrutura;
	public int numeroLinha;	
	private int sequencialAnterior=1;
	
	private HashMap<String, String> mapaOcorrencia;

	public List<LinhaArquivoModel> listaLinhasArquivo;

	public List<RegistroInvalidoArquivo> listaRegistroInvalido;

	public void lerArquivoValidandoSequencia(BufferedReader arquivo) throws IOException{

		inicializarVariaveis();

		while (arquivo.ready()) {
			String  textoLinha = (arquivo.readLine());	

			if(!validaEstruturaLinha(textoLinha) )
				break;

			String registro = textoLinha.substring(0,1);
				
			int codSegmento = 0;

			if(registro.equals("0")){
				validaSequencia(Header_de_Arquivo400);
				codSegmento = Header_de_Arquivo400;

			}else if(registro.equals("1")){

				validaSequencia(Detalhe_Titulo_CNAB400);
				codSegmento = Detalhe_Titulo_CNAB400; 
				registrarNossoNumerosArquivo(textoLinha); //registra o nosso número para validar duplicidade

			}else if( registro.equals("9")){
				validaSequencia(Trailer_de_Arquivo400);
				codSegmento = Trailer_de_Arquivo400;
			}else{
				validaSequencia(Layout_Desconhecido);
				codSegmento = Layout_Desconhecido;
			}
			
			validaSequencialRegistro(textoLinha.substring(394,400));
			
			if(isErroEstrutura){
				break;
			}
			listaLinhasArquivo.add(getLinha(textoLinha,numeroLinha,codSegmento));
			numeroLinha++;
		}//Fim while
		existeTrailerArquivo();		
		populaMapaOcorrencia();		
	}


	private void validaSequencialRegistro(String novoSequencial) {
		
		if(!Pattern.matches("\\d*", novoSequencial)){
			isErroEstrutura = true;
			msgErroEstrutura = "Sequencial de registro inválido";
			return;
		}else if( sequencialAnterior == 0 && Integer.parseInt(novoSequencial) > 1 || novoSequencial.equals("000000")){
			isErroEstrutura = true;
			msgErroEstrutura = "Sequencial de registro não está iniciado em '000001'";
		}else if( Integer.parseInt(novoSequencial) != sequencialAnterior+1  ){//se são dados numéricos
			isErroEstrutura = true;
			msgErroEstrutura = "Sequencial de registro fora da sequência";
		}
		sequencialAnterior = Integer.parseInt(novoSequencial);
	}


	private void existeTrailerArquivo() {
		if(isErroEstrutura!= true && segmentoAnterior != Trailer_de_Arquivo400 ){
			isErroEstrutura = true;
			msgErroEstrutura = " Trailer de arquivo não encontrado";
		}
	}

	private boolean validaEstruturaLinha(String textoLinha){

		if(textoLinha.length() < 400){
			isErroEstrutura = true;
			msgErroEstrutura = "Linha "+numeroLinha+" possue menos que 400 caracteres";
			return false;
		}else if(textoLinha.length() > 400){
			isErroEstrutura = true;
			msgErroEstrutura = "Linha "+numeroLinha+" possue mais que 400 caracteres";
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
			msgErroEstrutura = "A linha n° "+numeroLinha+" não foi reconhecida como um layout CNAB400";
			break;
		case Header_de_Arquivo400:
			if(segmentoAnterior == Header_de_Arquivo400){
				isErroEstrutura = true;
				msgErroEstrutura = "Header de arquivo está repetido";				 
			}else if( segmentoAnterior != 0){
				isErroEstrutura = true;
				msgErroEstrutura = "Header de arquivo está fora da sequência prevista no padrão CNAB400";
			}
			break;		
		case Detalhe_Titulo_CNAB400:
			if( (segmentoAnterior != Header_de_Arquivo400) && (segmentoAnterior != Detalhe_Titulo_CNAB400) ){
				isErroEstrutura = true;
				msgErroEstrutura = "Detalhe de arquivo não está precedido por um header de arquivo";
			}
			break;
		case Trailer_de_Arquivo400:
			if( segmentoAnterior != Detalhe_Titulo_CNAB400 ){
				isErroEstrutura = true;
				msgErroEstrutura = "Trailer de arquivo não está precedido por um detalhe de arquivo";
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
		sequencialAnterior=0;
		segmentoAnterior = 0;
		isErroEstrutura = false;
		msgErroEstrutura = "";
		listaNossoNumero = new ArrayList<String>();
		listaLinhasArquivo = new ArrayList<LinhaArquivoModel>();
		listaRegistroInvalido = new ArrayList<RegistroInvalidoArquivo>();
		mapaOcorrencia = new HashMap<String, String>();
	}


	public boolean getModalidadePorId(String modalidade) {
		if(!isNumeric(modalidade)) {
			return false;
		}

		return ModalidadeProdutoEnum.contains(modalidade);
	}

	public Date getDataMovimentoAtual() {
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
		listaNossoNumero.add(linha.substring(62, 74));
	}
	
	public boolean isNossoNumeroRepetido(String nossoNumero){
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

	
	public HashMap<String, String> getMapaOcorrencia() {
		return mapaOcorrencia;
	}


	private void populaMapaOcorrencia(){

		//Mesagens com base na consulta select * from listaitem where idlista = 247 em produção
		mapaOcorrencia.put("01","Banco de destino inválido");
		mapaOcorrencia.put("01A","Identificação do banco diferente de '756BANCOOBCED'");
		mapaOcorrencia.put("02","Código de registro inválido");
		mapaOcorrencia.put("05","Código de movimento inválido");
		mapaOcorrencia.put("06","Tipo/Código do cliente inválido");
		mapaOcorrencia.put("07","Agência inválida");
		mapaOcorrencia.put("07A","DV cooperativa do cliente inválido");		
		mapaOcorrencia.put("08","Nosso número/DV inválido");
		mapaOcorrencia.put("08A","Nosso número repetido no arquivo");
		mapaOcorrencia.put("09","Título já existe");
		mapaOcorrencia.put("10","Carteira/modalidade inválida");
		mapaOcorrencia.put("12","Tipo de documento inválido");
		mapaOcorrencia.put("16","Data de vencimento inválida");
		mapaOcorrencia.put("17","Data de vencimento menor que a data de emissão");
		mapaOcorrencia.put("20","Valor nominal do título inválido");
		mapaOcorrencia.put("21","Número da espécie do título inválido");
		mapaOcorrencia.put("23","Número do aceite do título inválido");
		mapaOcorrencia.put("24","Data de emissão do título inválida");
		mapaOcorrencia.put("25","Data de emissão posterior a data de vencimento");
		mapaOcorrencia.put("27","Não é permitido o envio de título na modalidade SIMPLES SEM REGISTRO para valores maiores ou iguais a R$");
		mapaOcorrencia.put("29","Valor do primeiro desconto inválido");
		mapaOcorrencia.put("29A","Valor de desconto maior ou igual ao valor do título");
		mapaOcorrencia.put("29B","Data de desconto informada e valor de desconto não informado");
		mapaOcorrencia.put("29C","Valor de desconto informado e data de desconto não informada");
		mapaOcorrencia.put("30A","Data do primeiro desconto inválida");
		mapaOcorrencia.put("30B","Desconto a conceder inválido");
		mapaOcorrencia.put("33","Valor do abatimento inválido");
		mapaOcorrencia.put("34","Valor do abatimento maior ou igual ao valor do título");
		mapaOcorrencia.put("38","Prazo para protesto inválido");
		mapaOcorrencia.put("42","Código de baixa/devolução inválido");
		mapaOcorrencia.put("44","Código da moeda inválido");
		mapaOcorrencia.put("45","Nome do sacado não informado");
		mapaOcorrencia.put("46","Tipo de inscrição do sacado inválido");
		mapaOcorrencia.put("46B","Número da inscrição do sacado inválido para o tipo 1-PF");
		mapaOcorrencia.put("46C","Número da inscrição do sacado inválido para o tipo 2-PJ");
		mapaOcorrencia.put("46D","Número da inscrição do sacado inválido");
		mapaOcorrencia.put("46E","Número da inscrição do sacado repetido para nomes diferentes");
		mapaOcorrencia.put("47","Endereço do sacado não informado");
		mapaOcorrencia.put("48A","CEP do sacado não informado");
		mapaOcorrencia.put("48B","CEP do sacado inválido");
		mapaOcorrencia.put("52A","UF do sacado não informada");
		mapaOcorrencia.put("52B","UF do sacado inválida");
		mapaOcorrencia.put("54A","Nome do sacador/avalista não informado");
		mapaOcorrencia.put("54B","Segunda instrução de recebimento não informada");
		mapaOcorrencia.put("54C","Indicador de sacador/avalista inválido");
		mapaOcorrencia.put("65","Banco do remetente inválido");
		mapaOcorrencia.put("66","Agência do remetente inválida");
		mapaOcorrencia.put("67A","Tipo de distribuição inválido");
		mapaOcorrencia.put("67B","Tipo de distribuição inválido para o perfil do cliente");
		mapaOcorrencia.put("68","Valor do IOF inválido");
		mapaOcorrencia.put("69","Quantidade monetária inválida");
		mapaOcorrencia.put("AB","Tipo de operação inválida");
		mapaOcorrencia.put("AC","Nome do serviço inválido");
		mapaOcorrencia.put("AG","Recebido via agendamento");
		mapaOcorrencia.put("AH","Sequencial do registro inválido");
		mapaOcorrencia.put("AI","Código do segmento inválido");
		mapaOcorrencia.put("AJ","Tipo de movimento");
		mapaOcorrencia.put("AK","Código compe inválido");
		mapaOcorrencia.put("AL","Banco do depositário inválido");
		mapaOcorrencia.put("AN","Conta Corrente/DV do cliente inválida ou inexistente");
		mapaOcorrencia.put("AP","Data da gravação inválida");
		mapaOcorrencia.put("BA","Número da agência cobradora inválido");
		mapaOcorrencia.put("BA1","Número do DV da agência cobradora inválido");
		mapaOcorrencia.put("BD","Inclusão efetuada com sucesso");
		mapaOcorrencia.put("BE","Alteração efetuada com sucesso");
		mapaOcorrencia.put("BF","Exclusão (ou baixa) efetuada com sucesso");
		mapaOcorrencia.put("BL","Valor da parcela inválido");
		mapaOcorrencia.put("BM","Identificação do contrato inválida");
		mapaOcorrencia.put("CA","Recebido via caixa");
		mapaOcorrencia.put("CI","Valor da taxa de mora mês inválido");
		mapaOcorrencia.put("CJ","Valor da multa inválido");
		mapaOcorrencia.put("CM","Valor do IOF inválido");
		mapaOcorrencia.put("FO","Pagamento fora de ordem");
		mapaOcorrencia.put("H1","Arquivo sem trailer");
		mapaOcorrencia.put("H3","Não descontado – outros motivos");
		mapaOcorrencia.put("HA","Lote não aceito");
		mapaOcorrencia.put("HB","Número da inscrição da empresa inválido para o contrato");
		mapaOcorrencia.put("HC","Convênio/Contrato inválido");
		mapaOcorrencia.put("HE","Tipo de serviço inválido");
		mapaOcorrencia.put("HH","Lote de serviço inválido");
		mapaOcorrencia.put("HI","Arquivo não aceito");
		mapaOcorrencia.put("HJ","Tipo de registro inválido");
		mapaOcorrencia.put("HK","Código de remessa/retorno inválido");
		mapaOcorrencia.put("HL","Versão de layout inválida");
		mapaOcorrencia.put("HM","Mutuário não identificado");
		mapaOcorrencia.put("HN","Tipo de benefício não permite empréstimo");
		mapaOcorrencia.put("HO","Benefício cessado ou suspenso");
		mapaOcorrencia.put("HP","Benefício possui representante legal");
		mapaOcorrencia.put("HQ","Benefício é do tipo PA");
		mapaOcorrencia.put("HR","Quantidade de contratos permitida excedida");
		mapaOcorrencia.put("HS","Benefício não pertence ao banco");
		mapaOcorrencia.put("HT","Início do desconto informado já ultrapassado");
		mapaOcorrencia.put("HV","Quantidade de parcelas inválida");
		mapaOcorrencia.put("HW","Margem consignável excedida");
		mapaOcorrencia.put("HX","Empréstimo já cadastrado");
		mapaOcorrencia.put("HY","Empréstimo inexistente");
		mapaOcorrencia.put("HZ","Empréstimo já encerrado");
		mapaOcorrencia.put("NE","Devolução de título não encontrada");
		mapaOcorrencia.put("PA","Arquivo vazio");
		mapaOcorrencia.put("PB","Tamanho do registro diferente 400 posições");
		mapaOcorrencia.put("PC","Nome do arquivo inválido");
		mapaOcorrencia.put("PD","Identificação do arquivo banco inválido");
		mapaOcorrencia.put("PE","DV do nosso número inválido");
		mapaOcorrencia.put("PF","Número da parcela inválido");
		mapaOcorrencia.put("PG1","Código da primeira instrução inválido");
		mapaOcorrencia.put("PG2","Código da segunda instrução inválido");
		mapaOcorrencia.put("PH","Quantidade de header´s/trailer´s inválido");
		mapaOcorrencia.put("PI","Cliente inexistente na cooperativa");
		mapaOcorrencia.put("PJ","Sequencial de remessa inválido");
		mapaOcorrencia.put("PK","Cidade do sacado não informada");
		mapaOcorrencia.put("PL","Título já liquidado");
		mapaOcorrencia.put("PM","Título já protestado");
		mapaOcorrencia.put("PN","Título inexistente");
		mapaOcorrencia.put("PO","Título já prorrogado na data");
		mapaOcorrencia.put("PQ","Título já alterado na data");
		mapaOcorrencia.put("PR","Data importação maior que data movimento produto");
		mapaOcorrencia.put("PS","Título liquidado/em liquidação");
		mapaOcorrencia.put("PT","Título duplicado mov.compe");
		mapaOcorrencia.put("PU","Conta corrente bloqueada");
		mapaOcorrencia.put("PV","Conta corrente encerrada");
		mapaOcorrencia.put("TA","Lote não aceito – totais do lote com diferença");
		mapaOcorrencia.put("TE", "Tipo de emissão deve ser informado");
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
		regInv.setPosicaoInical(inicio+1);
		regInv.setPosicaoFinal(fim);
		regInv.setValorEncontrado(linha.getParte(inicio, fim));
		regInv.setOcorrencia(ocorrencia);

		if(!listaRegistroInvalido.contains(regInv)) {
			listaRegistroInvalido.add(regInv);       
		}
	}
	
	public static class LinhaArquivoModel{
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
	public boolean isErroEstrutura() {
		return isErroEstrutura;
	}


	@Override
	public Integer getNumeroLinha() {
		return numeroLinha;
	}


	@Override
	public String getMsgErroEstrutura() {
		return msgErroEstrutura;
	}


	@Override
	public List<RegistroInvalidoArquivo> getListaRegistroInvalido() {
		return listaRegistroInvalido;
	}

}

