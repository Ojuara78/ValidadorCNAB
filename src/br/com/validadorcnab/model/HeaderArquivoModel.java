package br.com.validadorcnab.model;

public class HeaderArquivoModel {

    private String codBanco;
    private String loteServico;
    private String tipoInscricao;
    private String inscricao;
    private String codConvenio;
    private String contaCorrente;
    private String codRemessa;

    private final String linhaHeaderArquivo;

    public HeaderArquivoModel(String linhaSegmentoP){
        this.linhaHeaderArquivo = linhaSegmentoP;

        setCodBanco(getParte(0, 3));
        setLoteServico(getParte(3, 7));
        setTipoInscricao(getParte(17, 18));
        setInscricao(getParte(18, 32));
        setCodConvenio(getParte(32, 52));
        setContaCorrente(getParte(58, 71));//+ 71 DV da conta
        setCodRemessa(getParte(142,143 ));
    }

    public String getCodRemessa() {
        return codRemessa;
    }
    public void setCodRemessa(String codRemessa) {
        this.codRemessa = codRemessa;
    }
    public String getContaCorrente() {
        return contaCorrente;
    }
    public void setContaCorrente(String contaCorrente) {
        this.contaCorrente = contaCorrente;
    }
    public String getCodConvenio() {
        return codConvenio;
    }
    public void setCodConvenio(String codConvenio) {
        this.codConvenio = codConvenio;
    }

    public String getInscricao(int tipo) {
        if(tipo == 1){
            return inscricao.substring(3, 14);//Return 11 dígitos do cpf
        }else if(tipo==2){
            return inscricao.substring(0,14);//Return 14 dígitos do cpf
        }
        return inscricao;
    }

    public void setInscricao(String inscricao) {
        this.inscricao = inscricao;
    }
    public String getTipoInscricao() {
        return tipoInscricao;
    }
    public int getTipoInscricaoInt() {
        return Integer.parseInt(tipoInscricao);
    }
    public void setTipoInscricao(String tipoInscricao) {
        this.tipoInscricao = tipoInscricao;
    }
    public String getLoteServico() {
        return loteServico;
    }
    public void setLoteServico(String loteServico) {
        this.loteServico = loteServico;
    }
    public String getParte(int de, int ate ){
        return this.linhaHeaderArquivo.substring(de, ate);
    }
    public String getCodBanco() {
        return codBanco;
    }
    public void setCodBanco(String codigoBanco) {
        this.codBanco = codigoBanco;
    }
}