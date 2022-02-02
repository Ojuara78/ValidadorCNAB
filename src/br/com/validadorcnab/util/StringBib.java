package br.com.validadorcnab.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringBib {

	public static String substituiPontoPorVirgula(String valorParcela) {
		String retorno = "";
		retorno = valorParcela.replaceAll(",", ".");
		return retorno;
	}

	public static String formataInt(int valor, int tamanho) {
		StringBuilder sb = new StringBuilder(Integer.toString(valor));
		while (sb.length() < tamanho) {
			sb.insert(0, '0');
		}
		return sb.toString();
	}


	public static String formataLong(long valor, int tamanho) {
		StringBuilder sb = new StringBuilder(Long.toString(valor));
		while (sb.length() < tamanho) {
			sb.insert(0, '0');
		}
		return sb.toString();
	}
	

	public static String retiraCasaDecimal(double nmr, int casasDecimais) {
		
		String num = Double.valueOf(nmr).toString();
		int inteiro = (int) nmr;
		int posicao = num.indexOf(".");
		String decimais = num.substring(posicao + 1);
		String parte1 = Integer.valueOf(inteiro).toString();
		String parte2 = null;

		if (casasDecimais > decimais.length()) {
			parte2 = preencherComZeros(decimais, casasDecimais, true);
		} else {
			parte2 = decimais.substring(0, casasDecimais);
		}

		return parte1 + parte2;
	}
	
	public static String completaComBrancos(String valor, int tamanho) {
		
		if (valor != null && valor.length() > tamanho) {
			String completo = valor;
			valor = completo.substring(0,tamanho);
			return valor;
		}
		
		StringBuffer sb = new StringBuffer(valor == null ? "" : valor);
		while (sb.length() < tamanho) {
			sb.append(' ');
		}
		return sb.toString();
	}
	
	public static String completaComBrancos(String valor, int tamanho, boolean esquerda) {
		
		if (!esquerda) {
			return completaComBrancos(valor, tamanho);
		}
		
		StringBuffer sb = new StringBuffer(valor == null ? "" : valor);
		while (sb.length() < tamanho) {
			sb.insert(0, ' ');
		}
		
		return sb.toString();
	}
	

	public static String completaComBrancos(int valor, int tamanho) {
		return completaComBrancos(Integer.valueOf(valor).toString(), tamanho);
	}
	

	public static String completaComBrancos(char valor, int tamanho) {
		return completaComBrancos(Character.toString(valor), tamanho);
	}
	
	public static String completaComBrancos(long valor, int tamanho) {
		if (valor > Integer.MAX_VALUE) {
			return completaComBrancos(String.valueOf(valor), tamanho);
		} else {
			return completaComBrancos(Integer.valueOf((int)valor).toString(), tamanho);
		}
		
	}
	

	public static String completaComBrancos(double valor, int tamanho) {
		if (valor > Float.MAX_VALUE) {
			return completaComBrancos(Double.valueOf(valor).toString(), tamanho);
		} else {
			return completaComBrancos(Float.valueOf((float) valor).toString(), tamanho);
		}
	}


	public static String completaComZeros(String valor, int tamanho) {
		
		if (valor != null && valor.length() > tamanho) {
			String completo = valor;
			valor = completo.substring(0,tamanho);
			return valor;
		}
		
		StringBuilder sb = new StringBuilder(valor == null ? "" : valor);
		while (sb.length() < tamanho) {
			sb.insert(0, '0');
		}
		return sb.toString();
	}
	

	public static String completaComZeros(int valor, int tamanho) {
		String val = Integer.valueOf(valor).toString();
		StringBuilder sb = new StringBuilder(val);
		while (sb.length() < tamanho) {
			sb.insert(0, '0');
		}
		return sb.toString();
	}
	
	public static String completaComZeros(double valor, int tamanho) {
		
		String val = Integer.valueOf((int)valor).toString();
		StringBuilder sb = new StringBuilder(val);
		while (sb.length() < tamanho) {
			sb.insert(0, '0');
		}
		return sb.toString();
	}
	

	public static String completaComZeros(char valor, int tamanho) {
		return completaComZeros(Character.toString(valor), tamanho);
	}

	public static String preencherComZeros(String valor, int casas) {
		StringBuilder sb = new StringBuilder(valor);
		while (sb.length() < casas) {
			sb.insert(0, "0");
		}
		return sb.toString();

	}
	
	public static String preencherComZeros(String valor, int casas, boolean direita) {
		
		if (!direita) {
			return preencherComZeros(valor, casas);
		}
		
		StringBuilder sb = new StringBuilder(valor);
		while (sb.length() < casas) {
			sb.append("0");
		}
		return sb.toString();
	}
	
	public static String preencherComZeros(int valor, int casas, boolean direita) {
		String val = String.valueOf(valor);
		return preencherComZeros(val, casas, direita);
	}
	
	public static String preencherComZeros(double valor, int casas, boolean direita) {
		String val = String.valueOf(valor);
		
		return preencherComZeros(val, casas, direita);
	}
	
	/**
	 * Repete o caracter fornecido o numero de vezes indicado
	 * 
	 * @param pChar - caracter a ser repetido
	 * @param pNumVezes - numero de vezes a repetir o caracter
	 */
	public static String repetir(char pChar, int pNumVezes) {
		if (pNumVezes > 0) {
			char[] modelo = new char[pNumVezes];

			for (int i = 0; i < pNumVezes; i++) {
				modelo[i] = pChar;
			}

			return new String(modelo);
		}
		return "";
	}

	public static boolean ehEmailValido(String email) {
		Pattern pat = Pattern.compile(".+@.+\\.[A-z]+");
		Matcher match = pat.matcher(email);
		return match.matches();
	}

	public static String formatarData(Date data) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", new Locale("pt", "BR"));
		if (data != null)
			return sdf.format(data);
		else
			return "";
	}

	/**
	 * Retorna o string fornecido, formatado de acordo com a mascara
	 * especificada, seguindo o alinhamento da esquerda para a direita.
	 * 
	 * Na formacao da mascara, os seguintes caracteres tem significado especial:
	 * 9 - aceita apenas digitos numericos; A - aceita apenas caracteres
	 * alfabeticos ('a' ate 'z' e espaco); X - aceita qualquer caracter;
	 * 
	 * @param pString - string a ser formatado
	 * @param pMascara - mascara de formatacao
	 */
	public static String formatar(String pString, String pMascara) {
		return formatar(pString, pMascara, false);
	}

	/**
	 * Retorna um String a partir do inteiro fornecido, formatado de acordo com
	 * a mascara especificada, seguindo o alinhamento da esquerda para a
	 * direita.
	 * 
	 * Na formacao da mascara, os seguintes caracteres tem significado especial:
	 * 9 - aceita apenas digitos numericos; A - aceita apenas caracteres
	 * alfabeticos ('a' ate 'z' e espaco); X - aceita qualquer caracter;
	 * 
	 * @param pInteiro - o inteiro a ser formatado
	 * @param pMascara - mascara de formatacao
	 * 
	 * @return o String formatado
	 */
	public static String formatar(int pInteiro, String pMascara) {
		return formatar(Integer.toString(pInteiro), pMascara, false);
	}

	/**
	 * Retorna um String a partir do inteiro fornecido, formatado de acordo com
	 * a mascara especificada, seguindo o alinhamento da esquerda para a
	 * direita.
	 * 
	 * Na formacao da mascara, os seguintes caracteres tem significado especial:
	 * 9 - aceita apenas digitos numericos; A - aceita apenas caracteres
	 * alfabeticos ('a' ate 'z' e espaco); X - aceita qualquer caracter;
	 *
	 * @return o String formatado
	 */
	public static String formatar(long pLong, String pMascara, boolean pAlinhadoDireita) {
		return formatar(Long.toString(pLong), pMascara, pAlinhadoDireita);
	}
	
	public static String formatar(double pDouble, String pMascara,
			boolean pAlinhadoDireita) {

		int casasdecimais = (pMascara.length() - pMascara.indexOf(',')) - 1;
		String texto = Long.toString((long) Math.round(pDouble * Math.pow(10, casasdecimais)));
		texto = StringBib.preencherComZeros(texto, casasdecimais + 1);
		if (pDouble != 0) {
			return StringBib.formatar(texto, pMascara, pAlinhadoDireita);
		} else {
			return "0,00";
		}
	}

	/**
	 * Retorna o string fornecido, formatado de acordo com a mascara
	 * especificada.
	 * 
	 * Na formacao da mascara, os seguintes caracteres tem significado especial:
	 * 9 - aceita apenas digitos numericos; A - aceita apenas caracteres
	 * alfabeticos ('a' ate 'z' e espaco); X - aceita qualquer caracter;
	 * 
	 * @param pString - string a ser formatado
	 * @param pMascara -  mascara de formatacao
	 * @param pAlinhadoDireita -
	 *            true caso o string deva ser formatado a partir da direita,
	 *            caso contrario, a formatacao se dara a partir da esquerda
	 */
	public static String formatar(String pString, String pMascara, boolean pAlinhadoDireita) {
		if (pString == null || pString.equals("") || pString.charAt(0) == '-') {
			return "";
		}
		if (pMascara == null || pMascara.equals("")) {
			pMascara = "X";
		}

		StringBuilder temp = new StringBuilder("");
		int contMasc = pAlinhadoDireita ? pMascara.length() - 1 : 0;
		int cont = pAlinhadoDireita ? pString.length() - 1 : 0;
		char charMasc = pMascara.charAt(contMasc);
		char c;

		while (cont < pString.length() && cont >= 0) {
			if (charMasc != '9' && charMasc != 'X' && charMasc != 'A') {
				if (pAlinhadoDireita) {
					temp.insert(0, charMasc);
				} else {
					temp.append(charMasc);
				}
			} else {
				c = pString.charAt(cont);
				if (charMasc == '9') {
					if (c >= '0' && c <= '9') {
						if (pAlinhadoDireita) {
							temp.insert(0, c);
						} else {
							temp.append(c);
						}
					} else {
						if (pAlinhadoDireita) {
							contMasc++;
						} else {
							contMasc--;
						}
					}
				} else if (charMasc == 'A') {
					if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')
							|| c == ' ') {
						if (pAlinhadoDireita) {
							temp.insert(0, c);
						} else {
							temp.append(c);
						}
					} else {
						if (pAlinhadoDireita) {
							contMasc++;
						} else {
							contMasc--;
						}
					}
				} else { 
					if (pAlinhadoDireita) {
						temp.insert(0, c);
					} else {
						temp.append(c);
					}
				}
				if (pAlinhadoDireita) {
					cont--;
				} else {
					cont++;
				}
			}

			if (pAlinhadoDireita) {
				contMasc--;
			} else {
				contMasc++;
			}
			if (contMasc < 0 || contMasc >= pMascara.length()) {
				charMasc = 'X';
			} else {
				charMasc = pMascara.charAt(contMasc);
			}
		}

		return temp.toString();

	}

	/**
	 * Desformata o string fornecido, de acordo com a mascara indicada.
	 * 
	 * Na formacao da mascara, os seguintes caracteres tem significado especial:
	 * 9 - aceita apenas digitos numericos; A - aceita apenas caracteres
	 * alfabeticos ('a' ate 'z' e espaco); X - aceita qualquer caracter;
	 * 
	 * @param pString - string a ser desformatado
	 * @param pMascara - mascara a ser utilizada para a desformatacao
	 * @return string desformatado
	 */
	public static String desformatar(String pString, String pMascara) {
		if (pString.length() != 0) {
			if (pMascara.length() == 0) {
				return pString;
			}
			StringBuffer temp = new StringBuffer();
			for (int i = 0; i < pString.length(); i++) {
				if (pMascara.charAt(i) == '9' || pMascara.charAt(i) == 'X'
						|| pMascara.charAt(i) == 'A') {
					temp.append(pString.charAt(i));
				}
			}
			return temp.toString();
		} else {
			return "";
		}
	}

	public static String desformatarEsquerda(String pString, String pMascara) {
        // alinhado a esquerda
        return StringBib.desformatar(pString, pMascara.substring(0, pString.length()));
        
	}

	public static String desformatarDireita(String pString, String pMascara) {
        // alinhado a direita
        return StringBib.desformatar(pString, pMascara.substring(pMascara.length() - pString.length()));
	}
	
	/*
	 * Traduz cadeia nï¿½o instanciada em uma cadeia vazia @param pCadeia objeto
	 * String a ser avaliado @return String objeto instanciado caso o argumento
	 * n&atilde;o o seja.
	 */
	public static String desnulificar(String pCadeia) {
		return pCadeia == null ? CADEIA_VAZIA : pCadeia.trim();
	}

	/**
	 * Substitui espaços em branco no nome do servidor por '_' para que o mesmo
	 * possa integrar o nome do arquivo de destino a ser gerado por este
	 * componente.
	 * 
	 * @param origem - String da qual serio retirados os espaços em branco.
	 * @return Destino - String com os espaços em branco substituídos por '_'.
	 */
	public static String trocarEspacoEmBrancoPorSubscrito(String origem) {
		StringTokenizer st = new StringTokenizer(origem);
		StringBuilder destino = new StringBuilder();
		while (st.hasMoreTokens()) {
			destino.append("_").append(st.nextToken());
		}
		return destino.toString();
	}

	/**
	 * Remove os acentos e as cedilhas do string fornecido
	 * 
	 * @param pStr -
	 *            o string a ter seus acentos e cedilhas removidos
	 * @return o string sem os acentos e cedilhas
	 */
	public static String removerAcentos(String pStr) {
		char c;
		StringBuilder str = new StringBuilder(pStr);
		for (int i = 0; i < pStr.length(); i++) {
			c = pStr.charAt(i);
			for (int j = 0; j < CARACTERES_ESPECIAIS.length; j++) {
				if (c == CARACTERES_ESPECIAIS[j]) { 
					// é um caracter especial, substituir
					str.setCharAt(i, CARACTERES_SUBSTITUTOS[j]);
					break;
				}
			}
		}
		return str.toString();
	}

	/**
	 * criptografa uma String
	 * 
	 * @param pSt -
	 *            String a ser criptografada return String - sequencia de
	 *            códigos da String criptografada, separados por ";"
	 */
	public static String transf(String pSt) {
		return exportar(xTransf(pSt));
	}

	/**
	 * descriptografa uma String constituída de seus caracteres separados por
	 * ";"
	 * 
	 * @param pCodigos -   String constituiçãa de seus caracteres separados por ";" return
	 *            String - String descriptografada
	 */
	public static String detransf(String pCodigos) {
		return xTransf(importar(pCodigos));
	}

	/**
	 * retorna uma String a partir dos códigos de seus caracteres, separados por
	 * ";"
	 * 
	 * @param pCodigos -
	 *            String com os códigos de caracteres separados por ";"
	 * @return String - String correspondente aos caracteres de entrada
	 */
	private static String importar(String pCodigos) {
		StringTokenizer st = new StringTokenizer(pCodigos, ";");
		StringBuilder sb = new StringBuilder();

		while (st.hasMoreTokens()) {
			sb.append((char) Integer.parseInt(st.nextToken(), 16));
		}
		return sb.toString();
	}

	/**
	 * retorna os códigos hexadecimais dos caracteres de uma String, separados
	 * por ";"
	 * 
	 * @param pSt - String para obtenï¿½ï¿½o dos códigos de seus caracteres
	 * @return String - códigos dos caracteres separados por ";"
	 */
	private static String exportar(String pSt) {
		char[] ca = pSt.toCharArray();
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < ca.length; i++) {
			sb.append(Integer.toHexString(ca[i]));
			if (i < ca.length - 1)
				sb.append(';');
		}

		return sb.toString();
	}

	/**
	 * criptografa uma String aplicando um algoritmo simï¿½trico, ou seja, para
	 * descriptografar basta aplicar o método novamente
	 * 
	 * @param pSt - String a ser criptografada/descriptografada
	 * @return String - String criptografada/descriptografada
	 */
	private static String xTransf(String pSt) {
		byte[] x = new byte[20];
		x = importar("1;6d;71;c7;0;d6;dc;5b;4a;13;201a;1b;e0;28;45;55;65;50;4c;18").getBytes();
		char[] st = pSt.toCharArray();

		for (int i = 0; i < st.length; i++) {
			st[i] ^= (~x[i % x.length]);
		}
		return new String(st);
	}

	// ********** CONSTANTES ************

	private static final String CADEIA_VAZIA = "";

	//array com os caracteres acentuados e cedilhados
	private static final char[] CARACTERES_ESPECIAIS = { 'á', 'Á', 'ã', 'Ã',
			'â', 'Â', 'à', 'À', 'é', 'É', 'ê', 'Ê', 'í', 'Í', 'ó', 'Ó', 'õ',
			'Õ', 'ô', 'Ô', 'ú', 'Ú', 'ü', 'Ü', 'ç', 'Ç' };

	// array com os caracteres sem acentos e sem cedilhas correspondentes ao
	// array anterior
	private static final char[] CARACTERES_SUBSTITUTOS = { 'a', 'A', 'a', 'A',
			'a', 'A', 'a', 'A', 'e', 'E', 'e', 'E', 'i', 'I', 'o', 'O', 'o',
			'O', 'o', 'O', 'u', 'U', 'u', 'U', 'c', 'C' };

}