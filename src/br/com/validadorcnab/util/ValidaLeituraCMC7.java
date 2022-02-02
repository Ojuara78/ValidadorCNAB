package br.com.validadorcnab.util;

import java.util.HashMap;
import java.util.Map;

public class ValidaLeituraCMC7 {
	
	public static boolean validaLeituraCodigo(String codigoChequeDesformatado) {
		if (codigoChequeDesformatado.trim().equals("000000000000000000000000000000")) {
			return false;
		}
		
		if (codigoChequeDesformatado.trim().length()==0) {
			return false;
		}
		
		if (codigoChequeDesformatado.trim().length() != 30) {
			return false;
		}
		return 
			validaDV(codigoChequeDesformatado.substring(0,7),codigoChequeDesformatado.substring(18,19),1) && 
			validaDV(codigoChequeDesformatado.substring(8,18),codigoChequeDesformatado.substring(7,8),2) && 
			validaDV(codigoChequeDesformatado.substring(19,29),codigoChequeDesformatado.substring(29,30),3);
	}
	
	private static boolean validaDV(String numero, String digito, int dv) {
	    int tamanho = numero.length();
	    int soma = 0;
	    for (int i = 0; i < tamanho; i++) {
	    	String num = numero.substring(tamanho - (i+1),tamanho - i);
	    		soma += retornaSoma(Integer.parseInt(num), 2-(i%2));
	    }
	    if (((10 - (soma % 10))%10) == Integer.parseInt(digito)) {
	    	return true;
	    } else {
	    	Map<String,Integer> map = new HashMap<>();
	    	map.put("dv", dv);
	    	return false;
	    }
	}
	
	public static int retornaSoma(int valorIndice, int fator) {
		int valor =  valorIndice * fator;
		if (valor >= 10 ){
			valor -= 9;
		}
		return valor;
	}

}
