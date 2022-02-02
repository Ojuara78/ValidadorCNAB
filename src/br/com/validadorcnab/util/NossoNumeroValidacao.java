package br.com.validadorcnab.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Long.parseLong;

public class NossoNumeroValidacao {
	
	private static NossoNumeroValidacao nossoNumeroValidacao;
	
	public static NossoNumeroValidacao getInstance() {
		
		if(nossoNumeroValidacao == null) {
			nossoNumeroValidacao = new NossoNumeroValidacao();
		}
		return nossoNumeroValidacao;
	}
	
	public boolean isNossoNumeroInformadoValido(String nossoNumeroInformado, String string, String string2) {
		if (nossoNumeroInformado == null || nossoNumeroInformado.trim().length() == 0) {
			return false;
		} else {
			nossoNumeroInformado = nossoNumeroInformado.replaceAll("\\.", "");
			nossoNumeroInformado = nossoNumeroInformado.replaceAll("-", "");

			Pattern p = Pattern.compile("\\d+");
			Matcher m = p.matcher(nossoNumeroInformado);

			if ( !m.matches() ) {
				throw new IllegalArgumentException("O nosso numero " + nossoNumeroInformado + " é inválido.");
			}else{

				String numero = null;
				int digitoVerificador = -1;

				if (nossoNumeroInformado.length() > 1) {
					String nmr = nossoNumeroInformado.substring(0, nossoNumeroInformado.length()-1);
					numero = nmr;
					String dv = nossoNumeroInformado.substring(nossoNumeroInformado.length()-1, nossoNumeroInformado.length());
					digitoVerificador = Integer.parseInt(dv);
				} else {
					String nmr = nossoNumeroInformado.substring(0, nossoNumeroInformado.length());
					numero = nmr;
				}
				String novoDv = calculaDV(Integer.parseInt(string), Integer.parseInt(string2), numero);

				return novoDv.equals(Integer.toString(digitoVerificador));
			}
		}
	}
	
	private String calculaDV(int numCooperativa, int numCliente, String numTitulo) {
		String somado = StringBib.formataInt(numCooperativa, 4)
				+ StringBib.formataInt(numCliente, 10)
				+ StringBib.formataLong(parseLong(numTitulo), 7);
		return inserirDV11(somado);
	}
	
	private String inserirDV11(String sComposicao) {
		String sAux = "3791";
		int iAcumulador = 0;
		int i = sComposicao.length();
		int j = 0;
		
		while (i >= 1) {
			int iMultiplicador = Integer.parseInt(sAux.substring(j, j+1));
			iAcumulador = iAcumulador + (Integer.parseInt(sComposicao.substring(i-1, i)) * iMultiplicador);
			
			j = j + 1;
			
			if (j == 4) {
				j = 0;
			}
			i = i - 1;
		}
		int iResto = iAcumulador % 11;
		int iDV = 0;
		
		if (iResto == 0 || iResto == 1) {
			iDV = 0;
		} else {
			iDV = 11 - iResto;
		}
		return Integer.toString(iDV);
	}

}
