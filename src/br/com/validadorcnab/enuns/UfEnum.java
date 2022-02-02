package br.com.validadorcnab.enuns;

public enum UfEnum {
	
	AC("AC","ACRE"),
	AL("AL","ALAGOAS"),
	AM("AM","AMAZONAS"),
	AP("AP","AMAPÁ"),
	BA("BA","BAHIA"),
	CE("CE","CEARÁ"),
	DF("DF","DISTRITO FEDERAL"),
	ES("ES","ESPÍRITO SANTO"),
	GO("GO","GOIÁS"),
	MA("MA","MARANHÃO"),
	MG("MG","MINAS GERAIS"),
	MS("MS","MATO GROSSO DO SUL"),
	MT("MT","MATO GROSSO"),
	PA("PA","PARÁ"),
	PB("PB","PARAÍBA"),
	PE("PE","PERNAMBUCO"),
	PI("PI","PIAUÍ"),
	PR("PR","PARANÁ"),
	RJ("RJ","RIO DE JANEIRO"),
	RN("RN","RIO GRANDE DO NORTE"),
	RO("RO","RONDÔNIA"),
	RR("RR","RORAIMA"),
	RS("RS","RIO GRANDE DO SUL"),
	SC("SC","SANTA CATARINA"),
	SE("SE","SERGIPE"),
	SP("SP","SÃO PAULO"),
	TO("TO","TOCANTINS");	
	
	private final String uf;
	private final String descUf;
	
	private UfEnum(String uf,String descUF) {
		this.uf = uf;
		this.descUf = descUF;
	}
	
	public String getUF() {
		return uf;
	}
	
	public String getDescUF() {
		return descUf;
	}
	
	public static boolean contains(String uf) {
	    for (UfEnum c : UfEnum.values()) {
	        if (c.getUF().equals(uf)) {
	            return true;
	        }
	    }
	    return false;
	}

	

}
