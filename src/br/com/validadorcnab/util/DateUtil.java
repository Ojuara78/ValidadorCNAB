package br.com.validadorcnab.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Pattern;

public class DateUtil {

	public static final int ANO_MAXIMO = 2078;
	public static final int ANO_MINIMO = 1900;

	public static boolean validarDataDDMMYYYY(String data) {
		if (data.length() < 10) {
			return false;
		}
		int dd = Integer.parseInt(data.substring(0,2));
		int mm = Integer.parseInt(data.substring(3,5));
		int yyyy = Integer.parseInt(data.substring(6));
		return isDataValida(dd, mm, yyyy);
	}
	
	public static boolean isDataCnabValidaDDMMYYYY(String data) {
		if (data.length() < 8 || !Pattern.matches("[0-9]{8}",data)) {
			return false;
		}
		if (data.equals("00000000")) {
			return false;
		}
		int dd = Integer.parseInt(data.substring(0,2));
		int mm = Integer.parseInt(data.substring(2,4));
		int yyyy = Integer.parseInt(data.substring(4,8));
		return isDataValida(dd, mm, yyyy);
	}

	public static boolean validarDataDDMMYY(String data) {
		if (data.length() < 6 || !Pattern.matches("[0-9]{6}",data)) {
			return false;
		}
		if (data.equals("000000")) {
			return false;
		}
		int dd = Integer.parseInt(data.substring(0,2));
		int mm = Integer.parseInt(data.substring(2,4));
		int yy = Integer.parseInt(data.substring(4,6));
		return isDataValida(dd, mm, yy);
	}

	private static boolean isDataValida(int dd, int mm, int yy) {
		if (dd == 0) return false;
		switch (mm) {
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:
				return (dd < 32);
			case 4:
			case 6:
			case 9:
			case 11:
				return (dd < 31);
			case 2:
				if ((yy % 400)==0 || ((yy % 4)==0 && (yy % 100)>0)) {
					return dd < 30;
				} else {
					return dd < 29;
				}
			default: return false;
		}
	}

	/**
	 * Compara a data 1 com a a data 2.  
	 * @return
	 * 0 caso a data 1 seja igual a data 2 
	 * -1 caso a data 1 seja menor que a data 2 
	 * retorna 1 caso a data 1 seja maior que a data 2 
	 */
	public static int isDataUmMenorQueDataDois(Date data1, Date data2){
		return data1.compareTo(data2);
	}
	
	public static Date parseDataCnab(String txtData){
		Date data = null;

		String dia = ""; 
		String mes  = "";
		String ano = "";
		String  d = "";
		try {
			if(txtData.length()== 8){
				dia = txtData.substring(0, 2); 
				mes  = txtData.substring(2, 4);
				ano = txtData.substring(4, 8);
			}else if(txtData.length()==6){
				dia = txtData.substring(0, 2); 
				mes  = txtData.substring(2, 4);
				ano = txtData.substring(4, 6);
			}
			d = ano+"/"+ mes +"/"+dia;
			data = DateFormat.getDateInstance().parse(d);
		} catch (Exception e) {
			return null;
		}
		return data;
	}
	public static Date parseDataCanb_ddmmaa(String txtData){
		Date data = null;
		try {
			String dia = txtData.substring(0, 2); 
			String mes  = txtData.substring(2, 4);
			String ano = txtData.substring(4, 6);
			
			String  d = dia+"/"+mes+"/"+ano;
			data =  txtData.length()>6? parse("dd/MM/yyyy", d):parse("dd/MM/yy", d);
		} catch (Exception e) {
			return null;
		}
		return data;
	}
	public static boolean isDataMenor(String data) {
		int year = Integer.parseInt(data.substring(6));

		return year < ANO_MINIMO;
	}
	
	public static boolean isDataMaior(String data) {
		int year = Integer.parseInt(data.substring(6));

		return year > ANO_MAXIMO;
	}
	
	public static Date parse(String formato, String txtData) {
		SimpleDateFormat sdf = new SimpleDateFormat(formato);
		Date data = null;
		try {
			data = sdf.parse(txtData);
		} catch (ParseException ignored) {}
		return data;
	}

	public static Calendar obterCalendar(Date data) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(data);
		return cal;
	}

	public static String formatarData(String formato, Date data) {
		SimpleDateFormat sdf = new SimpleDateFormat(formato);
		String txtData = null;
		txtData = sdf.format(data);
		return txtData;
	}
	
	public static long calculaDiferencaDias(Date data1, Date data2) {
		Calendar cal1 = new GregorianCalendar();
		cal1.setTime(data1);
		Calendar cal2 = new GregorianCalendar();
		cal2.setTime(data2);
		long dif = cal1.getTimeInMillis() - cal2.getTimeInMillis();		
		return dif/(24*60*60*1000);
	}
	
	public static Date somaDias(Date data, int quantidadeDeDias) {
		return somaCampo(Calendar.DAY_OF_MONTH, data, quantidadeDeDias);
	}
	
	public static Date somaCampo(int campo, Date data, int quantidade) {
		Calendar calendar = getCalendarDaData(data);
		calendar.add(campo, quantidade);
		return calendar.getTime();
	}
	
	protected static Calendar getCalendarDaData(Date data) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(data);
		return calendar;
	}
	
	public static Date subtraiDias(Date date, int quantidadeDeDias) {
		return somaDias(date, -quantidadeDeDias);
	}	
	
}
