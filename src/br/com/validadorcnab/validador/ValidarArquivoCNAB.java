package br.com.validadorcnab.validador;

import java.awt.Cursor;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import br.com.validadorcnab.interfaces.FrontendInterface;
import br.com.validadorcnab.interfaces.Validador;
import br.com.validadorcnab.model.RegistroInvalidoArquivo;
import br.com.validadorcnab.validadorCnab400.ValidacaoCNAB400;
import br.com.validadorcnab.validadorcnab240.ValidacaoCNAB240;

public class ValidarArquivoCNAB extends SwingWorker<Object, Object> {

	private static final String ERRO_DE_ESTRUTURA_NA_LINHA = "Erro de estrutura na linha: ";

	private static final String ARQUIVO_SEM_INCONSISTENCIAS = "Sem Inconsistencia";

	private static final String VAZIO = "Arquivo vazio. ";
	private static final String ERROR = "Error interno";
	private static final String A_INSCRICAO_INCORRETA = "A inscrição do cliente descrita no arquivo não corresponde à configurada no sistema.";
	private static final String DIFERENTE_DE_400_E_240 = "A quantidade de caracteres presente no header do arquivo é diferente de 400 e 240.";
	private static final String O_NUMERO_DO_CLIENTE_NAO_CORRESPONDE = "O número do cliente descrito no header do arquivo está no formato incorreto.";
	private static final String A_COOPERATIVA_DIFERENTE_DO_SISTEMA = "A Cooperativa descrita no header contém informação incorreta.";
	private static final String TIPO_DE_SERVICO_DIFERENTE_DE_01 = "A posição referente ao tipo de serviço no header do arquivo é um valor diferente de \"01\".";
	private static final String PRIMEIRA_LINHA_NAO_CORRESPONDE = "A posição referente ao tipo de registro na primeira linha não corresponde a um header de arquivo CNAB400.";
	private static final int TAM_MAXIMO_CNAB400 = 400;
	private static final int TAM_MAXIMO_CNAB240 = 240;

	public JProgressBar jProgressBar;
	private final FrontendInterface frontend;
	private final File arquivo;

	private HashMap<String, List<RegistroInvalidoArquivo>> mapaDeArquivos;

	public ValidarArquivoCNAB(FrontendInterface instance, File file, JProgressBar jProgressBar) {
		this.jProgressBar = jProgressBar;
		this.frontend = instance;
		this.arquivo = file;
	}

	@Override
	protected Object doInBackground() {
		frontend.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		setStatusText(0);	
		inicioOperacao(arquivo);		
		return "Finished";
	}

	private void setStatusText(final int status) {
		SwingUtilities.invokeLater(() -> jProgressBar.setValue(status));
	}

	@Override
	protected void done() { 
		frontend.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		Toolkit.getDefaultToolkit().beep();
		setStatusText(100);
	}

	private List<RegistroInvalidoArquivo> gravaRegistroInvalido(String mensagemError) {

		List<RegistroInvalidoArquivo> listaInvalida = new ArrayList<>();

		RegistroInvalidoArquivo reg = new RegistroInvalidoArquivo();
		reg.setNumLinha(0);
		reg.setPosicaoFinal(0);
		reg.setPosicaoInical(0);
		reg.setOcorrencia(mensagemError);		
		listaInvalida.add(reg);

		return listaInvalida;

	}

	private void gravarMapaRegistroInvalido(String nomeArquivo,List<RegistroInvalidoArquivo> lista) {
		mapaDeArquivos.put(nomeArquivo, lista);
	}	
	
	private String getFileExtension(File file) {
	    String name = file.getName();
	    int lastIndexOf = name.lastIndexOf(".");
	    if (lastIndexOf == -1) {
	        return "";
	    }
	    return name.substring(lastIndexOf);
	}	


	private void inicioOperacao(File caminhoArquivoCnab) {
		try {
			mapaDeArquivos = new HashMap<>();
			File[] listOfFiles = caminhoArquivoCnab.listFiles(pathname -> {
				String name = pathname.getName().toLowerCase();
				return name.endsWith(".ced") && pathname.isFile();

			});
			
			if(listOfFiles == null && getFileExtension(caminhoArquivoCnab).equals(".CED")) {
				BufferedReader reader = new BufferedReader(new FileReader(caminhoArquivoCnab));
				String arqLeitura = reader.readLine();
				reader.close();

				int tipoArquivo = validaEstrutura(arqLeitura,caminhoArquivoCnab);

				if(tipoArquivo > 0) {    					
					setStatusText(40);
					validaNegocio(caminhoArquivoCnab, tipoArquivo);
				}
			}else {
				assert listOfFiles != null;
				if( listOfFiles.length > 0 ) {
					for (File file : listOfFiles) {
						BufferedReader reader = new BufferedReader(new FileReader(file));
						String arqLeitura = reader.readLine();
						reader.close();

						int tipoArquivo = validaEstrutura(arqLeitura,file);

						if(tipoArquivo > 0) {
							setStatusText(40);
							validaNegocio(file, tipoArquivo);
						}
					}
				}
			}
			
			setStatusText(80);
			frontend.setupTableDataArquivo(mapaDeArquivos);
			done();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	private int validaEstrutura(String arqLeitura,File file){
		try {

			String tipoDeServico = "01";
			String idRergistro = "0";
			String numCooperativa = null;
			String numCliente = null;

			setStatusText(20);

			if(arqLeitura == null) {
				gravarMapaRegistroInvalido(file.getName(), gravaRegistroInvalido(VAZIO));
				return 0;
			}

			if(arqLeitura.length() == TAM_MAXIMO_CNAB400) {
				
				setStatusText(30);

				numCooperativa = arqLeitura.substring(26, 30);
				numCliente = arqLeitura.substring(31, 40);

				// validando header
				if (!idRergistro.equals(arqLeitura.substring(0, 1))) {					
					gravarMapaRegistroInvalido(file.getName(), gravaRegistroInvalido(PRIMEIRA_LINHA_NAO_CORRESPONDE));
				} else if (!tipoDeServico.equals(arqLeitura.substring(9, 11))) {
					gravarMapaRegistroInvalido(file.getName(), gravaRegistroInvalido(TIPO_DE_SERVICO_DIFERENTE_DE_01));
				} else if(!isNumeric(numCooperativa)) {					
					gravarMapaRegistroInvalido(file.getName(), gravaRegistroInvalido(A_COOPERATIVA_DIFERENTE_DO_SISTEMA));
				} else if (!isNumeric(numCliente)) {
					gravarMapaRegistroInvalido(file.getName(), gravaRegistroInvalido(O_NUMERO_DO_CLIENTE_NAO_CORRESPONDE));
				}else {
					return 1;
				}

			}else if(arqLeitura.length() == TAM_MAXIMO_CNAB240) {		
				
				setStatusText(30);

				numCooperativa = arqLeitura.substring(53, 57);

				if(!isNumeric(numCooperativa)){
					gravarMapaRegistroInvalido(file.getName(), gravaRegistroInvalido(A_COOPERATIVA_DIFERENTE_DO_SISTEMA));
				}else if(!Pattern.matches("\\d*", arqLeitura.substring(18, 32).trim()) ||arqLeitura.substring(19, 33).trim().equals("")) {
					gravarMapaRegistroInvalido(file.getName(), gravaRegistroInvalido(A_INSCRICAO_INCORRETA));
				}else {
					return 2;
				}
			}else {
				gravarMapaRegistroInvalido(file.getName(), gravaRegistroInvalido(DIFERENTE_DE_400_E_240));
			}
		} catch (Exception e) {
			gravarMapaRegistroInvalido(file.getName(), gravaRegistroInvalido(ERROR));
			e.printStackTrace();
		}
		return 0;
	}	

	public void validaNegocio(File anexo, int tipoArquivo) throws Exception{
		Validador vCnab;
		if(tipoArquivo == 1) {
			vCnab = ValidacaoCNAB400.getInstance();
		}else {
			vCnab = ValidacaoCNAB240.getInstance();
		}	
		
		vCnab.lerArquivoValidandoSequencia(new BufferedReader(new FileReader(anexo)));

		setStatusText(50);

		if(vCnab.isErroEstrutura()){
			gravarMapaRegistroInvalido(anexo.getName(), gravaRegistroInvalido(ERRO_DE_ESTRUTURA_NA_LINHA + vCnab.getNumeroLinha() +" \nMensagem: "+ vCnab.getMsgErroEstrutura()));
		}else{ 
			setStatusText(60);
			vCnab.validarConteudoLinhas();
			if(vCnab.getListaRegistroInvalido() != null &&  vCnab.getListaRegistroInvalido().size() > 0){				
				gravarMapaRegistroInvalido(anexo.getName(),vCnab.getListaRegistroInvalido());
			}else {
				gravarMapaRegistroInvalido(anexo.getName(), gravaRegistroInvalido(ARQUIVO_SEM_INCONSISTENCIAS));
			}
			setStatusText(70);	
		}
	}

	private boolean isNumeric(String valor){
		if(valor.equals(""))
			return false;
		return  Pattern.matches("\\d*",valor);
	}

}
