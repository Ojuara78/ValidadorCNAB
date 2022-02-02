package br.com.validadorcnab.validadorcnab240;

public class ValidacaoCNAB240 extends Validador240 {
	
	private static ValidacaoCNAB240 instance;
	
	public static ValidacaoCNAB240 getInstance(){
		if (instance == null) {
			instance = new ValidacaoCNAB240();
		}
		return instance;
	}
	
	private ValidacaoCNAB240(){	}
	
	public void validarConteudoLinhas() throws Exception {
		
		for (LinhaArquivoModel linha : listaLinhasArquivo) {
			switch (linha.getTipoLinha()) {
			case Header_de_Arquivo:
				ValidadorHeaderArquivo.getInstance().validaHeaderArquivo(linha);
				break;
			case Header_de_Lote_Cheque:
				ValidadorHeaderLoteCheque.getInstance().validaHeaderLoteCheque(linha);
				break;
			case Detalhe_Cheque_Segmento_D:
				ValidadorSegmentoD.getInstance().validaSegmentoD(linha);
				break;
			case Trailer_de_Lote_Cheque:
				ValidadorTrailerLoteCheque.getInstance().validaTrailerLoteCheque(linha);
				break;
			case Header_de_Lote_Titulo:
				ValidadorHeaderLoteTitulo.getInstance().validaHeaderLoteTitulo(linha);
				break;
			case Detalhe_Titulo_Segmento_P:
				ValidadorSegmentoP.getInstance().validaSegmentoP(linha);
				break;
			case Detalhe_Titulo_Segmento_Q:
				ValidadorSegmentoQ.getInstance().validaSegmentoQ(linha);
				break;
			case Detalhe_Titulo_Segmento_R:
				ValidadorSegmentoR.getInstance().validaSegmentoR(linha);
				break;
			case Detalhe_Titulo_Segmento_S:
				ValidadorSegmentoS.getInstance().validaSegmentoS(linha);
				break;
			case Trailer_de_Lote_Titulo:
				ValidadorTrailerLoteTitulo.getInstance().validaTrailerLoteTitulo(linha);
				break;
			case Trailer_de_Arquivo:
				ValidadorTrailerArquivo.getInstance().validaTrailerArquivo(linha);
				break;
			default:
				System.out.println("Tipo de linha não tratada");
				break;
			}
		}
	}
}

