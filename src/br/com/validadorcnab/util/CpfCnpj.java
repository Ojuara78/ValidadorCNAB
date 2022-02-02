package br.com.validadorcnab.util;

import java.util.ArrayList;
import java.util.List;

public class CpfCnpj {

    private static String message;
    public static final String MASC_CPF = "999.999.999-99";
    public static final String MASC_CNPJ = "99.999.999/9999-99";
    public static boolean isValid(String number, String mascara) {
        return false;
    }

    public static String getMessage() {
        return message;
    }

    public static boolean isCpf(String cpfCnpj) {
        if (cpfCnpj.indexOf('/') != -1) {//se possui barra possivelmente é um CNPJ formatado
            return false;
        } else // possui traço, então está formatado
            // se tem 14 caracteres é um cpf formatado
            if (cpfCnpj.indexOf('-') == -1 && cpfCnpj.length() == 11) {
                // não possui traço, a inscrição não esta formatada
                // se tem 11 caracteres é um cpf desformatado
                return true;
            } else return cpfCnpj.indexOf('-') != -1 && cpfCnpj.length() == 14;
    }

    public static boolean isCnpj(String cpfCnpj) {
        return !isCpf(cpfCnpj);
    }

    public static String formatar(String cpfCnpj) {
        if (cpfCnpj == null) {
            throw new IllegalArgumentException("O CPF ou CNPJ não dever ser nulo!");
        }
        return StringBib.formatar(cpfCnpj, cpfCnpj.length() < 12 ? MASC_CPF : MASC_CNPJ);
    }

    public static boolean isCPFValido(String number) {
        if (!"".equals(number)) {
            message = "";
            try {
                Long.parseLong(number);
            } catch (Exception e) {
                message = "Somente numeros são permitidos";
                return false;
            }

            if (number.length() == 11 && !isCPFsIgnorados(number)) {
                int soma = 0;
                for (int i = 0; i < 9; i++)

                    soma += (10 - i) * (number.charAt(i) - '0');
                soma = 11 - (soma % 11);
                if (soma > 9)
                    soma = 0;
                if (soma == (number.charAt(9) - '0')) {
                    soma = 0;
                    for (int i = 0; i < 10; i++)
                        soma += (11 - i) * (number.charAt(i) - '0');
                    soma = 11 - (soma % 11);
                    if (soma > 9)
                        soma = 0;
                    return soma == (number.charAt(10) - '0');
                }
            }
            //System.out.println("CPF inválido");
            return false;
        } else {
            return true;
        }
    }

    public static boolean isCPFsIgnorados(String cpf) {
        return getListaDeCPFsIgnorados().contains(cpf);
    }

    public static List<String> getListaDeCPFsIgnorados() {
        List<String> listaCpfCnpj = new ArrayList<>();
        listaCpfCnpj.add("00000000000");
        listaCpfCnpj.add("11111111111");
        listaCpfCnpj.add("22222222222");
        listaCpfCnpj.add("33333333333");
        listaCpfCnpj.add("44444444444");
        listaCpfCnpj.add("55555555555");
        listaCpfCnpj.add("66666666666");
        listaCpfCnpj.add("77777777777");
        listaCpfCnpj.add("88888888888");
        listaCpfCnpj.add("99999999999");
        return listaCpfCnpj;
    }

    public static boolean isCNPJValido(String number) {
        if (!"".equals(number)) {
            message = "";
            try {
                Long.parseLong(number);
            } catch (Exception e) {
                message = "Somente numeros são permitidos";
                return false;
            }

            if (number.length() == 14 && !number.equals("00000000000000")) {
                int soma = 0;
                for (int i = 0, j = 5; i < 12; i++) {
                    soma += j-- * (number.charAt(i) - '0');
                    if (j < 2)
                        j = 9;
                }
                soma = 11 - (soma % 11);
                if (soma > 9)
                    soma = 0;
                if (soma == (number.charAt(12) - '0')) {
                    soma = 0;
                    for (int i = 0, j = 6; i < 13; i++) {
                        soma += j-- * (number.charAt(i) - '0');
                        if (j < 2)
                            j = 9;
                    }
                    soma = 11 - (soma % 11);
                    if (soma > 9)
                        soma = 0;
                    return soma == (number.charAt(13) - '0');
                }
            }
            return false;
        } else {
            return true;
        }
    }
}