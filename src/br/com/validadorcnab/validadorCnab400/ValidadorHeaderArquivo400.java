package br.com.validadorcnab.validadorCnab400;

import java.util.regex.Pattern;

import br.com.validadorcnab.util.DateUtil;
import br.com.validadorcnab.util.StringBib;
import br.com.validadorcnab.validadorCnab400.Validador400.LinhaArquivoModel;

public class ValidadorHeaderArquivo400 {
	
	private static ValidadorHeaderArquivo400 instance;
	
	public static ValidadorHeaderArquivo400 getInstance() {
		if (instance == null) {
			instance = new ValidadorHeaderArquivo400();
		}
		return instance;
	}
	private ValidadorHeaderArquivo400() {
		
	}	
	
	public HeaderArquivo400 h = null;
	
	ValidacaoCNAB400  vc = ValidacaoCNAB400.getInstance();
	
	public void  ValidaHeaderArquivo400( LinhaArquivoModel linha ) {

		h = new HeaderArquivo400(linha);
		
		validaIDRegistro();
		validaNomeArquivo();
		validaNomeServico();
		validaAgencia();
		validaTipoCodCedente();
		validaCodIdentficadaoBanco();
		validaDataGravacao();
		validaSequencialRegistro();
	
	}
	
	private void validaSequencialRegistro() {
		if(! vc.isNumeric( h.getSeqRegistro()) ){
			vc.criarRegistroInvalido(h.getLinha(), 394, 400, vc.getMapaOcorrencia().get("AH"));
		}
	}

	private void validaDataGravacao() {
		if( ! DateUtil.validarDataDDMMYY(h.getDataGravacao()) ){
			vc.criarRegistroInvalido(h.getLinha(), 94, 100, vc.getMapaOcorrencia().get("AP"));
		}
	}
	private void validaCodIdentficadaoBanco() {
		if(isDiferente("756BANCOOBCED", h.getIDentBanco().trim()))
			vc.criarRegistroInvalido(h.getLinha(), 76, 94, vc.getMapaOcorrencia().get("01A"));
	}
	private void validaTipoCodCedente() {
		if(!vc.isNumeric(h.getCodCedente()+h.getDVCedente()))
			vc.criarRegistroInvalido(h.getLinha(), 31, 40, vc.getMapaOcorrencia().get("06"));
	}
	
	private void validaAgencia() {
		if(!vc.isNumeric(h.getPrefixoAgencia()))
			vc.criarRegistroInvalido(h.getLinha(), 26, 30, vc.getMapaOcorrencia().get("07"));
	}
	private void validaNomeServico() {
		if(isDiferente("COBRANÇA", h.getNomeServico()))
			vc.criarRegistroInvalido(h.getLinha(), 11, 19, vc.getMapaOcorrencia().get("AC"));
	}
	private void validaNomeArquivo() {
		if(isDiferente("REMESSA", h.getNomeArq()))
			vc.criarRegistroInvalido(h.getLinha(), 2, 9, vc.getMapaOcorrencia().get("PC"));
	}
	private void validaIDRegistro() {
		if(isDiferente("0",h.getIDEntRegistro()))
			vc.criarRegistroInvalido(h.getLinha(), 0, 1, vc.getMapaOcorrencia().get("02"));
	}

	private boolean isDiferente(String regexp, String valor){
		return !Pattern.matches(regexp, valor);
	}
}

class HeaderArquivo400{
	
	private String	IDEntRegistro	;
	private String	tipoArquivo	;
	private String	nomeArq	;
	private String	tipoServico	;
	private String	nomeServico	;
	private String	brancos	;
	private String	prefixoAgencia	;
	private String	dVCooperativa	;
	private String	codCedente	;
	private String	DVCedente	;
	private String	numConvenenteLider	;
	private String	nomeCedente	;
	private String	IDentBanco	;
	private String	dataGravacao	;
	private String	seqRemessa	;
	private String	filler1	;
	private String	seqRegistro	;

	private final LinhaArquivoModel linhaHeader400;
	
	public HeaderArquivo400(LinhaArquivoModel linhaHeader400){
		
		this.linhaHeader400 = linhaHeader400;
		
		setIDEntRegistro(getParte(0, 1));
		setTipoArquivo(getParte(1, 2));
		setNomeArq(getParte(2, 9));
		setTipoServico(getParte(9, 11));
		setNomeServico(getParte(11, 19));
		setBrancos(getParte(19, 26));
		setPrefixoAgencia(getParte(26, 30));
		setDVCooperativa(getParte(30, 31));
		setCodCedente(getParte(31, 39));
		setDVCedente(getParte(39, 40));
		setNumConvenenteLider(getParte(40, 46));
		setNomeCedente(getParte(46, 76));
		setIDentBanco(getParte(76, 94));
		setDataGravacao(getParte(94, 100));
		setSeqRemessa(getParte(100, 107));
		setFiller1(getParte(108, 394));
		setSeqRegistro(getParte(394, 400));
	}

	public String getIDEntRegistro() {
		return IDEntRegistro;
	}

	public void setIDEntRegistro(String entRegistro) {
		IDEntRegistro = entRegistro;
	}

	public String getTipoArquivo() {
		return tipoArquivo;
	}

	public void setTipoArquivo(String tipoArquivo) {
		this.tipoArquivo = tipoArquivo;
	}

	public String getNomeArq() {
		return nomeArq;
	}

	public void setNomeArq(String nomeArq) {
		this.nomeArq = nomeArq;
	}

	public String getTipoServico() {
		return tipoServico;
	}

	public void setTipoServico(String tipoServico) {
		this.tipoServico = tipoServico;
	}

	public String getNomeServico() {
		return nomeServico;
	}

	public void setNomeServico(String nomeServico) {
		this.nomeServico = nomeServico;
	}

	public String getBrancos() {
		return brancos;
	}

	public void setBrancos(String brancos) {
		this.brancos = brancos;
	}

	public String getPrefixoAgencia() {
		return prefixoAgencia;
	}

	public void setPrefixoAgencia(String prefixoAgencia) {
		this.prefixoAgencia = prefixoAgencia;
	}

	public String getDVCooperativa() {
		return dVCooperativa;
	}

	public void setDVCooperativa(String cooperativa) {
		dVCooperativa = cooperativa;
	}

	public String getCodCedente() {
		return codCedente;
	}

	public void setCodCedente(String codCedente) {
		this.codCedente = codCedente;
	}

	public String getDVCedente() {
		return DVCedente;
	}

	public void setDVCedente(String cedente) {
		DVCedente = cedente;
	}

	public String getNumConvenenteLider() {
		return numConvenenteLider;
	}

	public void setNumConvenenteLider(String numConvenenteLider) {
		this.numConvenenteLider = numConvenenteLider;
	}

	public String getNomeCedente() {
		return nomeCedente;
	}

	public void setNomeCedente(String nomeCedente) {
		this.nomeCedente = nomeCedente;
	}

	public String getIDentBanco() {
		return IDentBanco;
	}

	public void setIDentBanco(String dentBanco) {
		IDentBanco = dentBanco;
	}

	public String getDataGravacao() {
		return dataGravacao;
	}

	public void setDataGravacao(String dataGravacao) {
		this.dataGravacao = dataGravacao;
	}

	public String getSeqRemessa() {
		return seqRemessa;
	}

	public void setSeqRemessa(String seqRemessa) {
		this.seqRemessa = seqRemessa;
	}

	public String getFiller1() {
		return filler1;
	}

	public void setFiller1(String filler1) {
		this.filler1 = filler1;
	}

	public String getSeqRegistro() {
		return seqRegistro;
	}

	public void setSeqRegistro(String seqRegistro) {
		this.seqRegistro = seqRegistro;
	}

	public LinhaArquivoModel getLinha() {
		return linhaHeader400;
	}
	
	public String getParte(int de, int ate ){
		return this.linhaHeader400.getConteudoLinha().substring(de, ate);
	}
	
}
