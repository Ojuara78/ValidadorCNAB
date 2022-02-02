package br.com.validadorcnab.model;

public class SegmentoSModel{

    private String codBanco;
    private String codigoMovimento;
    private String tipoImpressao;
    private final String linhaSegmentoS;

    public SegmentoSModel(String linhaSegmentoR){
        this.linhaSegmentoS = linhaSegmentoR;
        setCodBanco(getParte(0, 3));
        setCodMovimento(getParte(15, 17));
        setTipoImpressao(getParte(17, 18));
    }
    public String getTipoImpressao() {
        return tipoImpressao;
    }
    public void setTipoImpressao(String tipoImpressao) {
        this.tipoImpressao = tipoImpressao;
    }
    public String getParte(int de, int ate ){
        return this.linhaSegmentoS.substring(de, ate);
    }
    public String getCodBanco() {
        return codBanco;
    }
    public void setCodBanco(String codigoBanco) {
        this.codBanco = codigoBanco;
    }
    public String getCodoMovimento() {
        return codigoMovimento;
    }
    public void setCodMovimento(String codigoMovimento) {
        this.codigoMovimento = codigoMovimento;
    }




}