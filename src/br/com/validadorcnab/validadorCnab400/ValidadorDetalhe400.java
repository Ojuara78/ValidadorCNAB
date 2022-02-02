package br.com.validadorcnab.validadorCnab400;
  
import java.util.List;
import java.util.regex.Pattern;

import br.com.validadorcnab.enuns.UfEnum;
import br.com.validadorcnab.util.DateUtil;
import br.com.validadorcnab.util.ValidaTipoDados;
import br.com.validadorcnab.validadorCnab400.Validador400.LinhaArquivoModel;


public class ValidadorDetalhe400 {
	
	public static ValidadorDetalhe400 instance;	
	public static ValidadorDetalhe400 getInstance() {
		if(instance==null){
			return new ValidadorDetalhe400();
		}
		return instance;
	}
	
	private ValidadorDetalhe400() {}
	
	public Detalhe400Model d = null;
	
	
	private ValidacaoCNAB400  vc = ValidacaoCNAB400.getInstance();
	
	public void ValidadorDetalhe(LinhaArquivoModel linha) {
		
		d = new Detalhe400Model(linha);
		
		validaIDRegistro();
		validaTipoInscCliente();
		validaInscricaoCliente();
		validaCooperativaCliente();
		validaDvCooperativaCliente();
		validaContaCorrenteCliente();
		validaNumConvenio();
		//validaNossoNumero();
		validaNossoNumeroDuplicadoArquivo();
		validaNumeroParcela();
		validaValorNominal();
//		validaTituloSemRegistroValor250();
//		validaEmissaoSemRegistro();//Valida emissão sem registro (planejamento de extrinção da carteira pela Febraban)
//		validaEmissaoSemRegistroVencimento();Só é chamando se validaEmissaoSemRegistro não gerou ocorrência. 
		validaDataVencimento(); //Valida se é maior que a data de emissao
		validaBancoDepositario();
		validaAceite();
		validaDataEmissao();
		validaDataEmissaoMariorDataVencimento();
		validaCodPrimeiraInstrucao();
		validaCodSegundaInstrucao();
		validaDataPrimeiroDesconto();
		validaValorPrimeiroDesconto();		
		validaCodMoeda();//Valida se é numerico e se o cod moeda está entre 1 e 37		
		validaValorIOF_Qtdmonetaria();//Valida se é numerico e se o cod moeda está entre 1 e 37		
		validaValorAbatimentoMaiorQueValorTitulo();
		validaValorAbatimento();//Se é numerico
		validaCarteiraModalidade();
		validaCodMovimento();
		validaAgenciaCobradora();
		validaDVAgenciaCobradora();
		validaEspecieTitulo();
		validaValorMora();
		validaValorMulta();
		validaTipoDeDistribuicao();
		validaTipoInscricaoSacado();
		validaInscricaoSacado();
		validarCPF_CNPJ_duplicados_com_nomes_diferentes();
		validaNomeSacado();
		validaEnderecoSacado();
		validaCEPScado();
		validaCidadeSacado();
		validaUfSacado();
		validaSacadorAvalista();
		validaPrazoParaProtesto();
		validaSequencialRegistro();
		validaTipoEmissao();
		
	}
	
	private void validarCPF_CNPJ_duplicados_com_nomes_diferentes() {
		
		if( !( ValidaTipoDados.isCPFValido(d.getCPF_CGC_Sacado().substring(3, 14)) || ValidaTipoDados.isCNPJValido(d.getCPF_CGC_Sacado()))  ){
			return;
		}
		
		List<LinhaArquivoModel> listaLinhas = ValidacaoCNAB400.getInstance().listaLinhasArquivo;
			
			for(LinhaArquivoModel linhaArquivo : listaLinhas){
				if(linhaArquivo.getTipoLinha() == ValidacaoCNAB400.Detalhe_Titulo_CNAB400 ){
					if(d.getCPF_CGC_Sacado().equals(linhaArquivo.getParte(220, 234)) ){
						if(! d.getNomeSacado().equals(linhaArquivo.getParte(234, 274))){
							vc.criarRegistroInvalido(d.getLinha(), 220, 234, vc.getMapaOcorrencia().get("46E"));
					}
				}
			}
		}
	}
	
	private void validaSequencialRegistro() {
		if( !vc.isNumeric(d.getSeqRegistro())){
			vc.criarRegistroInvalido(d.getLinha(), 394, 400, vc.getMapaOcorrencia().get("AH"));	
		}
	}

	private void validaPrazoParaProtesto() {
		if(!vc.isNumeric(d.getProtesto().trim())){
			vc.criarRegistroInvalido(d.getLinha(), 391, 393, vc.getMapaOcorrencia().get("38"));	
		}
		
	}

	private void validaSacadorAvalista() {
		if(d.getIndSacador().equalsIgnoreCase("A") &&  d.getObservacoes().trim().length() == 0){
			vc.criarRegistroInvalido(d.getLinha(), 351, 391, vc.getMapaOcorrencia().get("54A"));
		}		
	}

	private void validaUfSacado() {
		
		if(d.getUFSacado().trim().equals("")){
			vc.criarRegistroInvalido(d.getLinha(), 349, 351, vc.getMapaOcorrencia().get("52A"));
			return;
		}		
		
		if(!UfEnum.contains(d.getUFSacado())) {
			vc.criarRegistroInvalido(d.getLinha(), 349, 351, vc.getMapaOcorrencia().get("52B"));
		}
	}

	private void validaCidadeSacado() {
		if(d.getCidadeSacado().trim().length()==0){
			vc.criarRegistroInvalido(d.getLinha(), 334, 349, vc.getMapaOcorrencia().get("PK"));	
		}
	}

	private void validaCEPScado() {
		if(d.getCEPSacado().trim().equals("")){
			vc.criarRegistroInvalido(d.getLinha(), 326, 334, vc.getMapaOcorrencia().get("48A"));
			return;
		}
		if(!vc.isNumeric(d.getCEPSacado())){
			vc.criarRegistroInvalido(d.getLinha(), 326, 334, vc.getMapaOcorrencia().get("48B"));	
		}
		if(Pattern.matches(d.getCEPSacado().substring(0, 5), "00000")){
			vc.criarRegistroInvalido(d.getLinha(), 326, 334, vc.getMapaOcorrencia().get("48B"));
		}
	}

	private void validaEnderecoSacado() {
		if(d.getEnderecoSacado().trim().length()==0){
			vc.criarRegistroInvalido(d.getLinha(), 274, 311, vc.getMapaOcorrencia().get("47"));	
		}
	}

	private void validaNomeSacado() {
		if(d.getNomeSacado().trim().length()==0){
			vc.criarRegistroInvalido(d.getLinha(), 234, 271, vc.getMapaOcorrencia().get("45"));	
		}
	}

	private void validaInscricaoSacado() {
		
		/* Existem inscrições CPF que coincidem com CNPJ quando se completa com zeros à esquerda conforme recomenda o padrão CNAB240/400 
		*  Exemplo: CNPJ =00000868180823 ou CPF: 00868180823 (se validarmos com 11 ou 14 dígitos ambos serão válidos)
		*/		
		if(ValidaTipoDados.isCNPJValido(d.getCPF_CGC_Sacado()) && ValidaTipoDados.isCPFValido(d.getCPF_CGC_Sacado().substring(3, 14)) && !isDiferente("01|02",d.getTipoInscSacado())){
			return;//Desconsidera a validação.
		}
				
		if(d.getTipoInscSacado().equals("01") && ValidaTipoDados.isCNPJValido(d.getCPF_CGC_Sacado())){
			vc.criarRegistroInvalido(d.getLinha(), 220, 234, vc.getMapaOcorrencia().get("46B"));
			return;
		}else if(d.getTipoInscSacado().equals("02") && ValidaTipoDados.isCPFValido(d.getCPF_CGC_Sacado().substring(3, 14)) ){
			vc.criarRegistroInvalido(d.getLinha(), 220, 234, vc.getMapaOcorrencia().get("46C"));
			return;
		}
		
		if( (d.getTipoInscSacado().equals("01") &&  !ValidaTipoDados.isCPFValido(d.getCPF_CGC_Sacado().substring(3, 14)))||
			(d.getTipoInscSacado().equals("02") &&  !ValidaTipoDados.isCNPJValido(d.getCPF_CGC_Sacado()))	){ 
			vc.criarRegistroInvalido(d.getLinha(), 220, 234, vc.getMapaOcorrencia().get("46D"));
		}
	}

/*	private boolean isCPF_CNPJ_Valido() {
		if(d.getTipoInscSacado().equals("01")){//PF
			return ValidaTipoDados.isCPFValido(d.getCPF_CGC_Sacado().substring(3, 14));
		}else if(d.getTipoInscSacado().equals("02")){//PJ
			return ValidaTipoDados.isCNPJValido(d.getCPF_CGC_Sacado());
		}else{
			return false;
		}
	} */

	private void validaTipoInscricaoSacado() {
		if( isDiferente("01|02", d.getTipoInscSacado()) ){
			vc.criarRegistroInvalido(d.getLinha(), 218, 220, vc.getMapaOcorrencia().get("46"));
		}
	}

	private void validaEspecieTitulo(){
	/*	CodListaItem DescListaItem                                                                                                                                                                                            
		------------ -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- 
		01           DUPLICATA MERCANTIL
		02           NOTA PROMISSORIA
		03           NOTA DE SEGURO
		05           RECIBO
		06           DUPLICATA RURAL
		08           LETRA DE CAMBIO
		09           WARRANT
		10           CHEQUE
		12           DUPLICATA DE SERVICO
		13           NOTA DE DEBITO
		14           TRPLICATA MERCANTIL
		15           TRIPLICATA DE SERVICO
		18           FATURA
		20           APOLICE DE SEGURO
		21           MENSALIDADE ESCOLAR
		22           PARCELA DE CONSORCIO
		99           OUTROS */
		
		if( !vc.isNumeric(d.getEspecieTitulo()) || !Pattern.matches("01|02|03|05|06|08|09|10|12|13|14|15|18|20|21|22|99", d.getEspecieTitulo()) ){
			vc.criarRegistroInvalido(d.getLinha(), 147, 149, vc.getMapaOcorrencia().get("21"));
		}
	}
	
	private void validaAgenciaCobradora() {
		if( !vc.isNumeric(d.getNumAgCobradora())) {
			vc.criarRegistroInvalido(d.getLinha(), 142, 146, vc.getMapaOcorrencia().get("BA"));
		}
	}
	
	private void validaDVAgenciaCobradora() {
		if( !vc.isNumeric(d.getDVAgCobradora())) {
			vc.criarRegistroInvalido(d.getLinha(), 146, 147, vc.getMapaOcorrencia().get("BA1"));
		}
	}

	private void validaTipoDeDistribuicao(){
		if(d.getFiller3().equals(" "))//Se for vazio não gerar crítica, pois o cobrança irá considerar como cliente tipo 1 
			return;
		if(!Pattern.matches("1|2", d.getFiller3())){
			vc.criarRegistroInvalido(d.getLinha(), 172, 173, vc.getMapaOcorrencia().get("67A"));
		}else if(d.getFiller3().equals("1")){//Id da funcionalidade Tipo de distribuição
			vc.criarRegistroInvalido(d.getLinha(), 172, 173, vc.getMapaOcorrencia().get("67B"));
		}
	}

	private void validaTipoEmissao() {
        if (d.getFiller3().equals("2")) {// Se for igual a 2-Cedente Distribui, o sistema deve cobrar o preenchimento do tipo de emissÃ£o.
            if (d.getTipoEmissao().equals(" ")) {
                vc.criarRegistroInvalido(d.getLinha(), 105, 106, vc.getMapaOcorrencia().get("TE"));
            }
        }
    }

	private void validaValorMulta() {
		if(!vc.isNumeric(d.getTaxaMulta())){
			vc.criarRegistroInvalido(d.getLinha(), 166, 172, vc.getMapaOcorrencia().get("CJ"));
		}
	}

	private void validaValorMora() {
		if(!vc.isNumeric(d.getTaxaMoraMes())){
			vc.criarRegistroInvalido(d.getLinha(), 160, 166, vc.getMapaOcorrencia().get("CI"));
		}
	}

	private void validaValorAbatimentoMaiorQueValorTitulo() {
		if(	(vc.isNumeric(d.getValorAbatimento()) && vc.isNumeric(d.getValorTitulo()) && Long.parseLong(d.getValorTitulo()) > 0) && 
				( Long.parseLong(d.getValorAbatimento()) >= Long.parseLong(d.getValorTitulo()) )){
			vc.criarRegistroInvalido(d.getLinha(), 205, 218, vc.getMapaOcorrencia().get("34"));
		}
	}

	private void validaValorAbatimento() {
		if(!vc.isNumeric(d.getValorAbatimento())){
			vc.criarRegistroInvalido(d.getLinha(), 205, 218, vc.getMapaOcorrencia().get("33"));
		}
	}

	private void validaValorIOF_Qtdmonetaria(){
		if(d.getCodMoeda().equals("9") &&  !vc.isNumeric(d.getValorIOF_QtdMonetaria())){
			vc.criarRegistroInvalido(d.getLinha(), 193, 205, vc.getMapaOcorrencia().get("68"));
		}else if( !isDiferente("1|2|3|4|5|6|7|8|",d.getCodMoeda()) && !vc.isNumeric(d.getValorIOF_QtdMonetaria())){
			vc.criarRegistroInvalido(d.getLinha(), 193, 205, vc.getMapaOcorrencia().get("69"));
		}
	}
	private void validaCodMoeda() {
		/*
		 select IDIndice , DescIndice  from Indice order by IDIndice
		IDIndice	  DescIndice                               
		-------- ---------------------------------------- 
		1        REAL
		2        DÓLAR COMERCIAL
		3        UNIDADE PADRÃO DE CAPITAL
		4        UNIDADE FISCAL DE REFERÊNCIA
		5        CUSTO UNITÁRIO BÁSICO DA CONSTR. CIVIL
		6        ÍNDICE DE PREÇOS AO CONSUMIDOR AMPLIADO
		7        TAXA DE JUROS DE LONGO PRAZO
		8        TAXA REFERENCIAL
		9        TAXA BÁSICA FINANCEIRA
		10       CERTIFICADO DEPÓSITO INTERBANCÁRIO
		11       INDICE GERAL DE PRECOS MERCADO
		12       ÍNDICE NACIONAL DE PRECOS AO CONSUMIDOR
		13       UNIDADE DE REFERENCIA DA TJLP
		14       TAXA SELIC
		15       POUPANCA COOPERADA
		16       FATOR PARA CÁLCULO DE ATRASO
		17       FATOR PARA CÁLCULO DE ATRASO
		18       FATOR PARA CÁLCULO DE ATRASO
		19       FATOR PARA CÁLCULO DE ATRASO
		20       FATOR PARA CÁLCULO DE ATRASO
		21       FATOR PARA CÁLCULO DE ATRASO
		22       FATOR PARA CÁLCULO DE ATRASO
		23       FATOR PARA CÁLCULO DE ATRASO
		24       FATOR PARA CÁLCULO DE ATRASO
		25       FATOR PARA CÁLCULO DE ATRASO
		26       FATOR PARA CÁLCULO DE ATRASO
		27       FATOR PARA CÁLCULO DE ATRASO
		28       FATOR PARA CÁLCULO DE ATRASO
		29       FATOR PARA CÁLCULO DE ATRASO
		30       FATOR PARA CÁLCULO DE ATRASO
		31       FATOR PARA CÁLCULO DE ATRASO
		32       FATOR PARA CÁLCULO DE ATRASO
		33       FATOR PARA CÁLCULO DE ATRASO
		34       FATOR PARA CÁLCULO DE ATRASO
		35       FATOR PARA CÁLCULO DE ATRASO
		36       FATOR PARA CÁLCULO DE ATRASO
		37       TAXA SELIC - FUNCAFE
		 */
		/*OBS: 		No processo de importação o código da moeda está fixado como 9,
				ou seja, independente do valor informado não haverá rejeição desde que esteja entra 1 e 37 (select * from sp_cob_canab400_precons).
					Nesta pré-consistência estamos considerando apenas numérico de 1 a 9, uma vez que o layout CNAB400 FEBRABAN disponibiliza apenas um dígito para esta informação.
		 */
		
		if( isDiferente("1|2|3|4|5|6|7|8|9", d.getCodMoeda()) ){
			vc.criarRegistroInvalido(d.getLinha(), 192, 193, vc.getMapaOcorrencia().get("44"));
		}
		
	}

	private void validaValorPrimeiroDesconto() {
		if(vc.isNumeric(d.getValorPrimDesconto()) && !d.getValorPrimDesconto().equals("0000000000000") &&   d.getDataPrimDesconto().equals("000000") ){
			vc.criarRegistroInvalido(d.getLinha(), 173, 179, vc.getMapaOcorrencia().get("29C"));
			return;
		}
		if( DateUtil.validarDataDDMMYY(d.getDataPrimDesconto()) && 
				vc.isNumeric(d.getValorPrimDesconto()) &&
				vc.isNumeric(d.getValorTitulo())&&  
				(Long.parseLong(d.getValorPrimDesconto()) >= Long.parseLong(d.getValorTitulo()))){
			vc.criarRegistroInvalido(d.getLinha(), 179, 192, vc.getMapaOcorrencia().get("29A"));
		}else if(!vc.isNumeric(d.getValorPrimDesconto())){
			vc.criarRegistroInvalido(d.getLinha(), 179, 192, vc.getMapaOcorrencia().get("29"));
		}
	}
	private void validaDataPrimeiroDesconto() {
		if(DateUtil.validarDataDDMMYY(d.getDataPrimDesconto()) && d.getValorPrimDesconto().equals("0000000000000")){
			vc.criarRegistroInvalido(d.getLinha(), 179, 192, vc.getMapaOcorrencia().get("29B"));
			return;
		}
		if(isDiferente("000000", d.getDataPrimDesconto()) && (!vc.isNumeric(d.getDataPrimDesconto()) || !DateUtil.validarDataDDMMYY(d.getDataPrimDesconto()) )){
			vc.criarRegistroInvalido(d.getLinha(), 173, 179, vc.getMapaOcorrencia().get("30A"));
		} 
	}

	private void validaCodSegundaInstrucao() {
		if(!vc.isNumeric(d.getSegundaInstrucao())){
			vc.criarRegistroInvalido(d.getLinha(), 158, 160, vc.getMapaOcorrencia().get("PG2"));
		}
	}

	private void validaCodPrimeiraInstrucao() {
		if(!vc.isNumeric(d.getPrimeiraInstrucao())){
			vc.criarRegistroInvalido(d.getLinha(), 156, 158, vc.getMapaOcorrencia().get("PG1"));
		}
	}

	private void validaDataEmissaoMariorDataVencimento() {
		
		if(vc.isNumeric(d.getDataEmissaoTitulo())
				&& vc.isNumeric(d.getDataVencTitulo())
				&& DateUtil.validarDataDDMMYY(d.getDataEmissaoTitulo())
				&& DateUtil.validarDataDDMMYY(d.getDataVencTitulo())
				&& DateUtil.isDataUmMenorQueDataDois( DateUtil.parseDataCanb_ddmmaa(d.getDataEmissaoTitulo()), DateUtil.parseDataCanb_ddmmaa(d.getDataVencTitulo())) == 1	)// fim if
			vc.criarRegistroInvalido(d.getLinha(), 150, 156, vc.getMapaOcorrencia().get("25"));
	}

	private void validaDataEmissao() {
		if(!vc.isNumeric(d.getDataEmissaoTitulo()) || !DateUtil.validarDataDDMMYY(d.getDataEmissaoTitulo())){
			vc.criarRegistroInvalido(d.getLinha(), 150, 156, vc.getMapaOcorrencia().get("24"));
		}
	}

	private void validaAceite() {
		if(!Pattern.matches("0|1", d.getAceiteTitulo())){
			vc.criarRegistroInvalido(d.getLinha(), 149, 150, vc.getMapaOcorrencia().get("23"));
		}
	}

	private void validaBancoDepositario() {
		if( isDiferente("756", d.getNumBanco()) ){
			vc.criarRegistroInvalido(d.getLinha(), 139, 142, vc.getMapaOcorrencia().get("AL"));
		}
	}

	private void validaValorNominal() {
		if( !vc.isNumeric(d.getValorTitulo()) || Long.parseLong(d.getValorTitulo()) <= 0 ){
			vc.criarRegistroInvalido(d.getLinha(), 126, 139, vc.getMapaOcorrencia().get("20"));
		}
	}
	
	private void validaDataVencimento() {
		if( isDiferente("888888|999999", d.getDataVencTitulo()) && (!vc.isNumeric(d.getDataVencTitulo()) || !DateUtil.validarDataDDMMYY(d.getDataVencTitulo()))	){
			vc.criarRegistroInvalido(d.getLinha(), 120, 126, vc.getMapaOcorrencia().get("16"));
		}
	}

	private void validaNumeroParcela() {
		if(!vc.isNumeric(d.getNumParcela()) ){/* Removida a validadação de parcela = 100, pois este campo não é utilizado no cobrança */
			vc.criarRegistroInvalido(d.getLinha(), 74, 76, vc.getMapaOcorrencia().get("PF"));
		}
	}

	private void validaNossoNumeroDuplicadoArquivo() {
		if(isDiferente("000000000000", d.getNossoNumeroComDv()) && vc.isNossoNumeroRepetido(d.getNossoNumeroComDv())){
			vc.criarRegistroInvalido(d.getLinha(), 62, 74, vc.getMapaOcorrencia().get("08A"));
		}
	}

//	private void validaNossoNumero() {		
//		//Se o commando for 01 o nosso número pode ser informado ou não.
//		if(Pattern.matches("01|02|03|04|05|06|07|08|09|10|11|12|30|31|34", d.getComando_Movimento())){
//			if( d.getComando_Movimento().equals("01") && isDiferente("000000000000", d.getNossoNumeroComDv()) ){
//				if( !vc.isNumeric(d.getNossoNumeroComDv()) || Long.parseLong(d.getNossoNumeroComDv()) > 99999999 || !NossoNumeroValidacao.getInstance().isNossoNumeroInformadoValido(d.getNossoNumeroComDv(),vc.dto.getNumCooperativa() ,vc.dto.getNumCliente())){
//					vc.criarRegistroInvalido(d.getLinha(), 62, 74, vc.getMapaOcorrencia().get("08"));
//				}
//			}else if( isDiferente("01", d.getComando_Movimento()) && ( d.getNossoNumeroComDv().equals("000000000000") || !vc.isNumeric(d.getNossoNumeroComDv()) || Long.parseLong(d.getNossoNumeroComDv()) > 99999999 || !NossoNumeroValidacao.getInstance().isNossoNumeroInformadoValido(d.getNossoNumeroComDv(),vc.dto.getNumCooperativa() ,vc.dto.getNumCliente())) ){
//				vc.criarRegistroInvalido(d.getLinha(), 62, 74, vc.getMapaOcorrencia().get("08"));
//			}
//		}
//	}

	private void validaCodMovimento() {
	/* select * from listaItem where IdLista = 254
		IdLista     CodListaItem DescListaItem                                                                                                                                                                                            
		----------- ------------ ----------------------- 
		254         01           REGISTRO DE TÍTULOS
		254         02           SOLICITAÇÃO DE BAIXA
		254         03           PEDIDO DE DÉBITO EM CONTA
		254         04           CONCESSÃO DE ABATIMENTO
		254         05           CANCELAMENTO DE ABATIMENTO
		254         06           ALTERAÇÃO DE VENCIMENTO
		254         07           ALTERAÇÃO DO NÚMERO DE CONTROLE
		254         08           ALTERAÇÃO DE SEU NÚMERO
		254         09           INSTRUÇÃO PARA PROTESTAR
		254         10           INSTRUÇÃO PARA SUSTAR PROTESTO
		254         11           INSTRUÇÃO PARA DISPENSAR JUROS
		254         12           ALTERAÇÃO DE SACADO
		254         30           RECUSA DE ALEGAÇÃO DO SACADO
		254         31           ALTERAÇÃO DE OUTROS DADOS
		254         34           BAIXA - PAGAMENTO DIRETO AO CEDENTE*/
		
		if(!Pattern.matches("01|02|03|04|05|06|07|08|09|10|11|12|30|31|34", d.getComando_Movimento())){
			vc.criarRegistroInvalido(d.getLinha(), 108, 110, vc.getMapaOcorrencia().get("05"));	
		}
		
	}

	private void validaCarteiraModalidade() {
		if(!vc.getModalidadePorId(d.getCarteira_Modalidade())){
			vc.criarRegistroInvalido(d.getLinha(), 106, 108, vc.getMapaOcorrencia().get("10"));
		}
	}

	private void validaNumConvenio() {
		if(!d.getNumConvenio().equals("000000") ){
			vc.criarRegistroInvalido(d.getLinha(), 31, 37, vc.getMapaOcorrencia().get("HC"));			
		}
	}

	private void validaContaCorrenteCliente() {//conta + DV
		
		String contaDv = d.getContaCorCliente()+ d.getDVContaCorCliente();
		if(!vc.isNumeric(contaDv)){
			vc.criarRegistroInvalido(d.getLinha(), 22, 31, vc.getMapaOcorrencia().get("AN"));
		}		
	}

	private void validaCooperativaCliente() {
		if( !vc.isNumeric(d.getAgenciaCliente())){
			vc.criarRegistroInvalido(d.getLinha(), 17, 21, vc.getMapaOcorrencia().get("07"));
		}
	}
	
	private void validaDvCooperativaCliente() {
		if( !vc.isNumeric(d.getDVAgenciaCliente())){
			vc.criarRegistroInvalido(d.getLinha(), 21, 22, vc.getMapaOcorrencia().get("07A"));
		}
	}

	private void validaInscricaoCliente() {
		
		if(!isDiferente("01|02", d.getTipoInscCliente()) && vc.isNumeric(d.getTipoInscCliente())){
			
			int tipo= Integer.parseInt(d.getTipoInscCliente().trim());
				
			switch (tipo) {
			case 1:
				if( ! ValidaTipoDados.isCPFValido(d.getCPF_CGC_Cliente().substring(3, 14))){
					vc.criarRegistroInvalido(d.getLinha(), 3, 17, vc.getMapaOcorrencia().get("06"));
				}
				break;
			case 2:
				if( ! ValidaTipoDados.isCNPJValido(d.getCPF_CGC_Cliente().substring(0, 14))){
					vc.criarRegistroInvalido(d.getLinha(), 3, 17, vc.getMapaOcorrencia().get("06"));
				}
				break;
			default:
				break;
			}
		}		
	}

	private void validaTipoInscCliente() {
		if(isDiferente("01|02", d.getTipoInscCliente()) ){
			vc.criarRegistroInvalido(d.getLinha(), 1, 3, vc.getMapaOcorrencia().get("06"));
		}
	}

	private void validaIDRegistro() {
		if(isDiferente("1",d.getIdentRegistro()))
			vc.criarRegistroInvalido(d.getLinha(), 0, 1, vc.getMapaOcorrencia().get("02"));
	}
	
	private boolean isDiferente(String regexp, String valor){
		if(Pattern.matches(regexp, valor)){
			return false;
		}
		return true;
	}
}
class Detalhe400Model{
		
	private String	identRegistro	;
	private String	tipoInscCliente	;
	private String	CPF_CGC_Cliente	;
	private String	agenciaCliente	;
	private String	DVAgenciaCliente	;
	private String	contaCorCliente	;
	private String	DVContaCorCliente	;
	private String	numConvenio	;
	private String	numControleCliente	;
	private String	nossoNumero	;
	private String	numParcela	;
	private String	indValorGrupo	;
	private String	filler1	;
	private String	indSacador	;
	private String	prefixoTitulo	;
	private String	variacao	;
	private String	contaCaucao	;
	private String	codResponsabilidade	;
	private String	DVContrato	;
	private String	numBordero	;
	private String	filler2	;
	private String tipoEmissao;
	private String	carteira_Modalidade	;
	private String	comando_Movimento	;
	private String	seuNumero	;
	private String	dataVencTitulo	;
	private String	valorTitulo	;
	private String	numBanco	;
	private String	numAgCobradora	;
	private String	DVAgCobradora	;
	private String	especieTitulo	;
	private String	aceiteTitulo	;
	private String	dataEmissaoTitulo	;
	private String	primeiraInstrucao	;
	private String	segundaInstrucao	;
	private String	taxaMoraMes	;
	private String	taxaMulta	;
	private String	filler3	;
	private String	dataPrimDesconto	;
	private String	valorPrimDesconto	;
	private String	valorIOF_QtdMonetaria	;
	private String	valorAbatimento	;
	private String	tipoInscSacado	;
	private String	CPF_CGC_Sacado	;
	private String	nomeSacado	;
	private String	enderecoSacado	;
	private String	bairroSacado	;
	private String	CEPSacado	;
	private String	cidadeSacado	;
	private String	UFSacado	;
	private String	observacoes	;
	private String	protesto	;
	private String	filler4	;
	private String	CPF_CGC_Cliente_Valido	;
	private String	CPF_CGC_Sacado_Valido	;
	private String	seqRegistro	;
	private String 	codMoeda;

	private LinhaArquivoModel linhaDetalhe400;

	
	public Detalhe400Model(LinhaArquivoModel linhaDetalhe400){
		this.linhaDetalhe400 = linhaDetalhe400;
		  
		setIdentRegistro(getParte(0, 1));
		
		setTipoInscCliente(getParte(1, 3));
		
		setCPF_CGC_Cliente(getParte(3, 17));
		
		setAgenciaCliente(getParte(17, 21));
		
		setDVAgenciaCliente(getParte(21, 22));
		
		setContaCorCliente(getParte(22, 30));
		
		setDVContaCorCliente(getParte(30, 31));
		
		setNumConvenio(getParte(31, 37));
		
		setNumControleCliente(getParte(37, 62));
		
		setNossoNumeroComDv(getParte(62, 73)+ getParte(73, 74));
		
		setNumParcela(getParte(74, 76));
		
		setIndValorGrupo(getParte(76, 78));
		
		setFiller1(getParte(78, 81));
		
		setIndSacador(getParte(81, 82));
		
		setPrefixoTitulo(getParte(82, 85));
		
		setVariacao(getParte(85, 88));
		
		setContaCaucao(getParte(88, 89));
		
		setCodResponsabilidade(getParte(89, 94));
		
		setDVContrato(getParte(94, 95));
		
		setNumBordero(getParte(95, 101));
		
		setFiller2(getParte(101, 105));
		
		setTipoEmissao(getParte(105, 106));
		
		setCarteira_Modalidade(getParte(106, 108));
		
		setComando_Movimento(getParte(108, 110));
		
		setSeuNumero(getParte(110, 120));
		
		setDataVencTitulo(getParte(120, 126));
		
		setValorTitulo(getParte(126, 139));
		
		setNumBanco(getParte(139, 142));
		
		setNumAgCobradora(getParte(142, 146));
		
		setDVAgCobradora(getParte(146, 147));
		
		setEspecieTitulo(getParte(147, 149));
		
		setAceiteTitulo(getParte(149, 150));
		
		setDataEmissaoTitulo(getParte(150, 156));
		
		setPrimeiraInstrucao(getParte(156, 158));
		
		setSegundaInstrucao(getParte(158, 160));
		
		setTaxaMoraMes(getParte(160, 166));
		
		setTaxaMulta(getParte(166, 172));
		
		setFiller3(getParte(172, 173));
		
		setDataPrimDesconto(getParte(173, 179));
		
		setValorPrimDesconto(getParte(179, 192));
		
		setCodMoeda(getParte(192, 193));
		
		setValorIOF_QtdMonetaria(getParte(192, 205));
		
		setValorAbatimento(getParte(205, 218));
		
		setTipoInscSacado(getParte(218, 220));
		
		setCPF_CGC_Sacado(getParte(220, 234));
		
		setNomeSacado(getParte(234, 274));
		
		setEnderecoSacado(getParte(274, 311));
		
		setBairroSacado(getParte(311, 326));
		
		setCEPSacado(getParte(326, 334));
		
		setCidadeSacado(getParte(334, 349));
		
		setUFSacado(getParte(349, 351));
		
		setObservacoes(getParte(351, 391));
		
		setProtesto(getParte(391, 393));
		
		setFiller4(getParte(393, 394));
		
		setSeqRegistro(getParte(394, 400));
			
		
	}
	
	public String getCodMoeda() {
		return codMoeda;
	}

	public void setCodMoeda(String codMoeda) {
		this.codMoeda = codMoeda;
	}

	public String getIdentRegistro() {
		return identRegistro;
	}


	public void setIdentRegistro(String identRegistro) {
		this.identRegistro = identRegistro;
	}


	public String getTipoInscCliente() {
		return tipoInscCliente;
	}


	public void setTipoInscCliente(String tipoInscCliente) {
		this.tipoInscCliente = tipoInscCliente;
	}


	public String getCPF_CGC_Cliente() {
		return CPF_CGC_Cliente;
	}


	public void setCPF_CGC_Cliente(String cliente) {
		CPF_CGC_Cliente = cliente;
	}


	public String getAgenciaCliente() {
		return agenciaCliente;
	}


	public void setAgenciaCliente(String agenciaCliente) {
		this.agenciaCliente = agenciaCliente;
	}


	public String getDVAgenciaCliente() {
		return DVAgenciaCliente;
	}


	public void setDVAgenciaCliente(String agenciaCliente) {
		DVAgenciaCliente = agenciaCliente;
	}


	public String getContaCorCliente() {
		return contaCorCliente;
	}


	public void setContaCorCliente(String contaCorCliente) {
		this.contaCorCliente = contaCorCliente;
	}


	public String getDVContaCorCliente() {
		return DVContaCorCliente;
	}


	public void setDVContaCorCliente(String contaCorCliente) {
		DVContaCorCliente = contaCorCliente;
	}


	public String getNumConvenio() {
		return numConvenio;
	}


	public void setNumConvenio(String numConvenio) {
		this.numConvenio = numConvenio;
	}


	public String getNumControleCliente() {
		return numControleCliente;
	}


	public void setNumControleCliente(String numControleCliente) {
		this.numControleCliente = numControleCliente;
	}


	public String getNossoNumeroComDv() {
		return nossoNumero;
	}


	public void setNossoNumeroComDv(String nossoNumero) {
		this.nossoNumero = nossoNumero;
	}


	public String getNumParcela() {
		return numParcela;
	}


	public void setNumParcela(String numParcela) {
		this.numParcela = numParcela;
	}


	public String getIndValorGrupo() {
		return indValorGrupo;
	}


	public void setIndValorGrupo(String indValorGrupo) {
		this.indValorGrupo = indValorGrupo;
	}


	public String getFiller1() {
		return filler1;
	}


	public void setFiller1(String filler1) {
		this.filler1 = filler1;
	}


	public String getIndSacador() {
		return indSacador;
	}


	public void setIndSacador(String indSacador) {
		this.indSacador = indSacador;
	}


	public String getPrefixoTitulo() {
		return prefixoTitulo;
	}


	public void setPrefixoTitulo(String prefixoTitulo) {
		this.prefixoTitulo = prefixoTitulo;
	}


	public String getVariacao() {
		return variacao;
	}


	public void setVariacao(String variacao) {
		this.variacao = variacao;
	}


	public String getContaCaucao() {
		return contaCaucao;
	}


	public void setContaCaucao(String contaCaucao) {
		this.contaCaucao = contaCaucao;
	}


	public String getCodResponsabilidade() {
		return codResponsabilidade;
	}


	public void setCodResponsabilidade(String codResponsabilidade) {
		this.codResponsabilidade = codResponsabilidade;
	}


	public String getDVContrato() {
		return DVContrato;
	}


	public void setDVContrato(String contrato) {
		DVContrato = contrato;
	}


	public String getNumBordero() {
		return numBordero;
	}


	public void setNumBordero(String numBordero) {
		this.numBordero = numBordero;
	}


	public String getFiller2() {
		return filler2;
	}
 


	public void setFiller2(String filler2) {
		this.filler2 = filler2;
	}

    public String getTipoEmissao() {
        return tipoEmissao;
    }

    public void setTipoEmissao(String tipoEmissao) {
        this.tipoEmissao = tipoEmissao;
    }

	public String getCarteira_Modalidade() {
		return carteira_Modalidade;
	}


	public void setCarteira_Modalidade(String carteira_Modalidade) {
		this.carteira_Modalidade = carteira_Modalidade;
	}


	public String getComando_Movimento() {
		return comando_Movimento;
	}


	public void setComando_Movimento(String comando_Movimento) {
		this.comando_Movimento = comando_Movimento;
	}


	public String getSeuNumero() {
		return seuNumero;
	}


	public void setSeuNumero(String seuNumero) {
		this.seuNumero = seuNumero;
	}


	public String getDataVencTitulo() {
		return dataVencTitulo;
	}


	public void setDataVencTitulo(String dataVencTitulo) {
		this.dataVencTitulo = dataVencTitulo;
	}


	public String getValorTitulo() {
		return valorTitulo;
	}


	public void setValorTitulo(String valorTitulo) {
		this.valorTitulo = valorTitulo;
	}


	public String getNumBanco() {
		return numBanco;
	}


	public void setNumBanco(String numBanco) {
		this.numBanco = numBanco;
	}


	public String getNumAgCobradora() {
		return numAgCobradora;
	}


	public void setNumAgCobradora(String numAgCobradora) {
		this.numAgCobradora = numAgCobradora;
	}


	public String getDVAgCobradora() {
		return DVAgCobradora;
	}


	public void setDVAgCobradora(String agCobradora) {
		DVAgCobradora = agCobradora;
	}


	public String getEspecieTitulo() {
		return especieTitulo;
	}


	public void setEspecieTitulo(String especieTitulo) {
		this.especieTitulo = especieTitulo;
	}


	public String getAceiteTitulo() {
		return aceiteTitulo;
	}


	public void setAceiteTitulo(String aceiteTitulo) {
		this.aceiteTitulo = aceiteTitulo;
	}


	public String getDataEmissaoTitulo() {
		return dataEmissaoTitulo;
	}


	public void setDataEmissaoTitulo(String dataEmissaoTitulo) {
		this.dataEmissaoTitulo = dataEmissaoTitulo;
	}


	public String getPrimeiraInstrucao() {
		return primeiraInstrucao;
	}


	public void setPrimeiraInstrucao(String primeiraInstrucao) {
		this.primeiraInstrucao = primeiraInstrucao;
	}


	public String getSegundaInstrucao() {
		return segundaInstrucao;
	}


	public void setSegundaInstrucao(String segundaInstrucao) {
		this.segundaInstrucao = segundaInstrucao;
	}


	public String getTaxaMoraMes() {
		return taxaMoraMes;
	}


	public void setTaxaMoraMes(String taxaMoraMes) {
		this.taxaMoraMes = taxaMoraMes;
	}


	public String getTaxaMulta() {
		return taxaMulta;
	}


	public void setTaxaMulta(String taxaMulta) {
		this.taxaMulta = taxaMulta;
	}


	public String getFiller3() {
		return filler3;
	}


	public void setFiller3(String filler3) {
		this.filler3 = filler3;
	}


	public String getDataPrimDesconto() {
		return dataPrimDesconto;
	}


	public void setDataPrimDesconto(String dataPrimDesconto) {
		this.dataPrimDesconto = dataPrimDesconto;
	}


	public String getValorPrimDesconto() {
		return valorPrimDesconto;
	}


	public void setValorPrimDesconto(String valorPrimDesconto) {
		this.valorPrimDesconto = valorPrimDesconto;
	}


	public String getValorIOF_QtdMonetaria() {
		return valorIOF_QtdMonetaria;
	}


	public void setValorIOF_QtdMonetaria(String valorIOF_QtdMonetaria) {
		this.valorIOF_QtdMonetaria = valorIOF_QtdMonetaria;
	}


	public String getValorAbatimento() {
		return valorAbatimento;
	}


	public void setValorAbatimento(String valorAbatimento) {
		this.valorAbatimento = valorAbatimento;
	}


	public String getTipoInscSacado() {
		return tipoInscSacado;
	}


	public void setTipoInscSacado(String tipoInscSacado) {
		this.tipoInscSacado = tipoInscSacado;
	}


	public String getCPF_CGC_Sacado() {
		return CPF_CGC_Sacado;
	}


	public void setCPF_CGC_Sacado(String sacado) {
		CPF_CGC_Sacado = sacado;
	}


	public String getNomeSacado() {
		return nomeSacado;
	}


	public void setNomeSacado(String nomeSacado) {
		this.nomeSacado = nomeSacado;
	}


	public String getEnderecoSacado() {
		return enderecoSacado;
	}


	public void setEnderecoSacado(String enderecoSacado) {
		this.enderecoSacado = enderecoSacado;
	}


	public String getBairroSacado() {
		return bairroSacado;
	}


	public void setBairroSacado(String bairroSacado) {
		this.bairroSacado = bairroSacado;
	}


	public String getCEPSacado() {
		return CEPSacado;
	}


	public void setCEPSacado(String sacado) {
		CEPSacado = sacado;
	}


	public String getCidadeSacado() {
		return cidadeSacado;
	}


	public void setCidadeSacado(String cidadeSacado) {
		this.cidadeSacado = cidadeSacado;
	}


	public String getUFSacado() {
		return UFSacado;
	}


	public void setUFSacado(String sacado) {
		UFSacado = sacado;
	}


	public String getObservacoes() {
		return observacoes;
	}


	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}


	public String getProtesto() {
		return protesto;
	}


	public void setProtesto(String protesto) {
		this.protesto = protesto;
	}


	public String getFiller4() {
		return filler4;
	}


	public void setFiller4(String filler4) {
		this.filler4 = filler4;
	}


	public String getCPF_CGC_Cliente_Valido() {
		return CPF_CGC_Cliente_Valido;
	}


	public void setCPF_CGC_Cliente_Valido(String cliente_Valido) {
		CPF_CGC_Cliente_Valido = cliente_Valido;
	}


	public String getCPF_CGC_Sacado_Valido() {
		return CPF_CGC_Sacado_Valido;
	}


	public void setCPF_CGC_Sacado_Valido(String sacado_Valido) {
		CPF_CGC_Sacado_Valido = sacado_Valido;
	}


	public String getSeqRegistro() {
		return seqRegistro;
	}


	public void setSeqRegistro(String seqRegistro) {
		this.seqRegistro = seqRegistro;
	}

	public String getParte(int de, int ate ){
		return this.linhaDetalhe400.getConteudoLinha().substring(de, ate);
	}
	
	public LinhaArquivoModel getLinha() {
		return linhaDetalhe400;
	}
	
}
