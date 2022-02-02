package br.com.validadorcnab.util;

public class ValidaTipoDados {
	
	private ValidaTipoDados() {
	    throw new IllegalStateException("ValidaTipoDados class");
	}
	
	public static boolean isCPFValido(String valorNumerico){
		return CpfCnpj.isCPFValido(valorNumerico); 
	}
	public static boolean isCNPJValido(String valorNumerico){
		return CpfCnpj.isCNPJValido(valorNumerico);
	}	

}