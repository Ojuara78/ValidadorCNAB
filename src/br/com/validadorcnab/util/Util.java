package br.com.validadorcnab.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Date;

public final class Util {

	public static String[] dividirString(String texto, int caracteres) {
		
		ArrayList<String> lista = new ArrayList<>();
		
		while (texto.length() > caracteres) {
			
			char caracter = texto.charAt(caracteres);
			int novaPosicao = caracteres;
			
			while (caracter != ' ' && caracter != '.' && novaPosicao+1 != texto.length()) {
				novaPosicao = novaPosicao + 1;
				caracter = texto.charAt(novaPosicao);
			}
			
			lista.add(texto.substring(0, novaPosicao));
			texto = texto.substring(novaPosicao);
		}
		
		if (texto.length() > 0) {
			lista.add(texto);
		}
		
		String[] arrayRetorno = new String[lista.size()];
		lista.toArray(arrayRetorno);
		
		return arrayRetorno;
	}	

	public static int random(int offset, int tamanho) {
		int value = offset + (int) ( Math.random() * tamanho );
		
		return value;
	}
	
	public static String retiraMascara(String string) {	
		String retorno = "";
		if (string != null) {
			retorno = string.replaceAll("-", "");
			retorno = retorno.replaceAll("\\.", "");
			retorno = retorno.replaceAll("/", "");
		}
		return retorno;
	}

	public static String retirarZerosEsquerda(String valor) {
		int posicao = 0;
		for(int i = 0; i < valor.length(); i++) {
			if(valor.charAt(i) != '0') {
				break;
			} else {
				posicao++;
			}
		}
		return valor.substring(posicao);
	}
		
	public static char getQuebraDeLinha() {
		/*String separadorLinha = System.getProperty("line.separator");
		char cara = separadorLinha.charAt(0);*/
		
		return '\n';
	}
	
	public static DecimalFormat getValorDecimalFormatado(int qtdeDigitos) {

		DecimalFormat decimalFormat = formataValorDecimal(qtdeDigitos);
		DecimalFormatSymbols dfs = configuraDecimalFormatSymbols();

		decimalFormat.setDecimalFormatSymbols(dfs);
		return decimalFormat;
	}
	
	public static String getValorFormatadoComMascra(double valorDouble) {
		DecimalFormat decimalFormat = new DecimalFormat("R$ ###,###,##0.00");
		return decimalFormat.format(valorDouble);
	}
	
	public static DecimalFormat formataValorDecimal(int qtdeDigitos) {
		DecimalFormat decimalFormat = new DecimalFormat();
		decimalFormat.setMinimumFractionDigits(qtdeDigitos);
		decimalFormat.setMaximumFractionDigits(qtdeDigitos);
		return decimalFormat;
	}

	public static DecimalFormatSymbols configuraDecimalFormatSymbols() {
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator(',');
		dfs.setGroupingSeparator('.');
		return dfs;
	}
	
	/**
	 * Retira a mascara
	 * @author antonio.lopes
	 * Método criado em 11/08/2011
	 */
	public static String retiraMascaraSemNull(String string) {	
		String retorno = "";
		if (!string.equals("")) {
			retorno = string.replaceAll("-", "");
			retorno = retorno.replaceAll("\\.", "");
			retorno = retorno.replaceAll("/", "");
		}
		return retorno;
	}
	
	/**
	 * Calcula a diferença de duas datas em dias
	 * <br>
	 * <b>Importante:</b> Quando realiza a diferença em dias entre duas datas, este método considera as horas restantes e as converte em fração de dias.
	 * @return quantidade de dias existentes entre a dataInicial e dataFinal.
	 */
	public static double diferencaEmDias(Date dataInicial, Date dataFinal){
		double result = 0;
		long diferenca = dataFinal.getTime() - dataInicial.getTime();
		double diferencaEmDias = (diferenca /1000) / 60 / 60 /24; //resultado é diferença entre as datas em dias
		long horasRestantes = (diferenca /1000) / 60 / 60 %24; //calcula as horas restantes
		result = diferencaEmDias + (horasRestantes /24d); //transforma as horas restantes em fração de dias

		return result;
	}

	/**
	 * Calcula a diferença de duas datas em horas
	 * <br>
	 * <b>Importante:</b> Quando realiza a diferença em horas entre duas datas, este método considera os minutos restantes e os converte em fração de horas.
	 * @param dataInicial
	 * @param dataFinal
	 * @return quantidade de horas existentes entre a dataInicial e dataFinal.
	 */
	public static double diferencaEmHoras(Date dataInicial, Date dataFinal){
		double result = 0;
		long diferenca = dataFinal.getTime() - dataInicial.getTime();
		long diferencaEmHoras = (diferenca /1000) / 60 / 60;
		long minutosRestantes = (diferenca / 1000)/60 %60;
		double horasRestantes = minutosRestantes / 60d;
		result = diferencaEmHoras + (horasRestantes);

		return result;
	}

	/**
	 * Calcula a diferença de duas datas em minutos
	 * <br>
	 * <b>Importante:</b> Quando realiza a diferença em minutos entre duas datas, este método considera os segundos restantes e os converte em fração de minutos.
	 * @param dataInicial
	 * @param dataFinal
	 * @return quantidade de minutos existentes entre a dataInicial e dataFinal.
	 */
	public static double diferencaEmMinutos(Date dataInicial, Date dataFinal){
		double result = 0;
		long diferenca = dataFinal.getTime() - dataInicial.getTime();
		double diferencaEmMinutos = (diferenca /1000) / 60; //resultado é diferença entre as datas em minutos
		long segundosRestantes = (diferenca / 1000)%60; //calcula os segundos restantes
		result = diferencaEmMinutos + (segundosRestantes /60d); //transforma os segundos restantes em minutos

		return result;
	}	

}
