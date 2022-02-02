package br.com.validadorcnab.enuns;

public enum ModalidadeProdutoEnum {

	REGISTRADO("01","SIMPLES COM REGISTRO"),
	SEM_REGISTRO("02","SIMPLES SEM REGISTRO"),
	CAUCIONADA("03","CAUCIONADA"),
	VINCULADA("04","VINCULADA"),
	CARNE("05","CARNÊ DE PAGAMENTOS"),
	INDEXADA("06","INDEXADA"),
	RAPIDA("07","RÁPIDA"),
	CONTACAPITAL("08","COBRANÇA CONTA CAPITAL"),
	CHEQUE("09","CHEQUE EM CUSTODIA"),
	BC("11","BANCO CORRESPONDENTE -  BANCOOB"),
	RETORNADA("12","COBRANÇA RETORNADA PARA SIMPLES"),
	EMPRESTIMO("13","EMPRÉSTIMO"),
	CARTAOCREDITO("14","CARTÃO DE CRÉDITO"),
	DESCONTADO("15","TÍTULO DESCONTADO"),
	CONTADIGITAL("16","CONTA DIGITAL"),
	DEPOSITOVIABOLETO("17","DEPÓSITO VIA BOLETO");	
	
	private final String idmodalidadeProduto;
	private final String descricao;
	
	private ModalidadeProdutoEnum(String modalidade,String descricao) {
		
		this.idmodalidadeProduto = modalidade;
		this.descricao = descricao;		
	}	
	
	public String getDescricao() {
		return descricao;
	}	
	public String getIdModalidadeProduto() {
		return idmodalidadeProduto;
	}
	
	public static boolean contains(String idmodalidade) {
	    for (ModalidadeProdutoEnum c : ModalidadeProdutoEnum.values()) {
	        if (c.getIdModalidadeProduto().equals(idmodalidade)) {
	            return true;
	        }
	    }
	    return false;
	}
}
