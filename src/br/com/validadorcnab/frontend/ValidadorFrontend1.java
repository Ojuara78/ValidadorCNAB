package br.com.validadorcnab.frontend;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import br.com.validadorcnab.interfaces.FrontendInterface;
import br.com.validadorcnab.model.RegistroInvalidoArquivo;
import br.com.validadorcnab.validador.ValidarArquivoCNAB;

/**
 * ValidadorFrontend1 é responsável por
 * 
 * @author Wesley.Araujo
 */
public class ValidadorFrontend1 extends JFrame implements ActionListener, FrontendInterface {

    private static final long serialVersionUID = -1013814024554465067L;
    private URL diskette;
    private static final String VALIDADOR = "Validador de Arquivo CNAB240/CNAB400 - v5.0.1";

    private final JPanel contentPane;
    private JProgressBar jProgressBar;
    private JFileChooser jfc;

    private JButton jbValidarPasta;
    private JButton jbPesquisar;

    protected File anexo;

    private JTable jTableResultado;
    private JTable jTableArquivos;

    private String localTemp = ("C:\\CNAB240");

    private HashMap<String, List<RegistroInvalidoArquivo>> listaRegistro;

    private final Object selectedColumn = null;

    private final String[] text_header_resultado = { "L", "Ini", "Fim", "Valor Encontrado", "Ocorrência" };
    private final String[] text_header_arquivo = { "E", "Nome do Arquivo" };

    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    ValidadorFrontend1 frame = new ValidadorFrontend1();
                    ImageIcon img = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("resources/logo.png")));
                    frame.setIconImage(img.getImage());

                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                    SwingUtilities.updateComponentTreeUI(frame);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public ValidadorFrontend1() {
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1010, 460);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);
        initIcons();
        montarTela();
    }

    private void initIcons() {
        try {
            diskette = getClass().getClassLoader().getResource("resources/logo.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void montarTela() {
        setTitle(VALIDADOR);
        panelArquivo();
        panelResultado();
        panelInferior();

    }

    private void panelArquivo() {

        JScrollPane scrollPaneArquivos = new JScrollPane();
        // scrollPaneArquivos.setViewportBorder(null);
        scrollPaneArquivos.setBounds(10, 52, 381, 358);
        contentPane.add(scrollPaneArquivos);
        List<String[]> values = new ArrayList<>();
        TableModel tableModel = new DefaultTableModel(values.toArray(new Object[][] {}), text_header_arquivo);

        jTableArquivos = new JTable(tableModel);
        headerArquivo(jTableArquivos);

        jTableArquivos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPaneArquivos.setViewportView(jTableArquivos);

        setTableArquivo(jTableArquivos);

        jTableArquivos.getSelectionModel().addListSelectionListener(event -> {
            if (jTableArquivos.getSelectedRow() > -1) {
                setUpTableDataResultado(jTableArquivos.getValueAt(jTableArquivos.getSelectedRow(), 1).toString());
            }
        });
    }

    private void headerArquivo(JTable jtable) {
        JTableHeader header_arquivo = jtable.getTableHeader();
        header_arquivo.setReorderingAllowed(false);
        final TableCellRenderer hr = jtable.getTableHeader().getDefaultRenderer();

        header_arquivo.setDefaultRenderer(new TableCellRenderer() {
            private JLabel lbl;

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (selectedColumn == value) {
                    lbl = (JLabel) hr.getTableCellRendererComponent(table, value, true, true, row, column);
                    lbl.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1), BorderFactory.createLineBorder(Color.red, 1)));
                    lbl.setHorizontalAlignment(SwingConstants.LEFT);
                    lbl.setBackground(lbl.getBackground());
                } else {
                    lbl = (JLabel) hr.getTableCellRendererComponent(table, value, false, false, row, column);
                    lbl.setBorder(BorderFactory.createCompoundBorder(lbl.getBorder(), BorderFactory.createEmptyBorder(1, 1, 1, 1)));
                    lbl.setHorizontalAlignment(SwingConstants.CENTER);
                    lbl.setBackground(new Color(184, 197, 198));
                }
                return lbl;
            }
        });

    }

    private void panelResultado() {

        JScrollPane scrollPaneResultado = new JScrollPane();

        // scrollPaneResultado.setViewportBorder(null);
        scrollPaneResultado.setBounds(401, 52, 583, 358);
        contentPane.add(scrollPaneResultado);
        List<String[]> values = new ArrayList<>();
        TableModel tableModel = new DefaultTableModel(values.toArray(new Object[][] {}), text_header_resultado);

        jTableResultado = new JTable(tableModel);
        jTableResultado.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        headerArquivo(jTableResultado);

        scrollPaneResultado.setViewportView(jTableResultado);
        setTableResultado(jTableResultado);

        jbPesquisar = new JButton("Pesquisar");
        jbPesquisar.setToolTipText("Pesquisa por uma pasta onde contenha arquivos cnab240/400 v\u00E1lidos");
        jbPesquisar.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
        jbPesquisar.addActionListener(this);
        jbPesquisar.setBounds(228, 6, 115, 38);
        contentPane.add(jbPesquisar);

        jbValidarPasta = new JButton("Validar PastaCNAB240");
        jbValidarPasta.setToolTipText("Valida todos os arquivos que est\u00E3o na pasta CNAB240 na raiz do C:");
        jbValidarPasta.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
        jbValidarPasta.setBounds(10, 6, 208, 38);
        jbValidarPasta.addActionListener(this);
        contentPane.add(jbValidarPasta);

        JButton btnLimpar = new JButton("Limpar");
        btnLimpar.setToolTipText("Limpar Campos");
        btnLimpar.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
        btnLimpar.addActionListener(e -> limparCampos());
        btnLimpar.setBounds(895, 6, 89, 38);
        contentPane.add(btnLimpar);
    }

    private void panelInferior() {
        jProgressBar = new JProgressBar();
        jProgressBar.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
        jProgressBar.setBounds(353, 6, 532, 38);
        contentPane.add(jProgressBar);
        jProgressBar.setStringPainted(true);
        jProgressBar.setIndeterminate(false);
        jProgressBar.setForeground(Color.GREEN);

    }

    private void setTableArquivo(JTable jTableArquivos) {
        jTableArquivos.getColumnModel().getColumn(0).setCellRenderer(new RowIconTableCellRenderer());
        jTableArquivos.getColumnModel().getColumn(0).setMinWidth(20);
        jTableArquivos.getColumnModel().getColumn(0).setMaxWidth(20);
    }

    private void setTableResultado(JTable jTableResultado) {

        jTableResultado.getColumnModel().getColumn(0).setMinWidth(20);
        jTableResultado.getColumnModel().getColumn(0).setMaxWidth(20);

        jTableResultado.getColumnModel().getColumn(1).setMinWidth(30);
        jTableResultado.getColumnModel().getColumn(1).setMaxWidth(30);

        jTableResultado.getColumnModel().getColumn(2).setMinWidth(30);
        jTableResultado.getColumnModel().getColumn(2).setMaxWidth(30);

        jTableResultado.getColumnModel().getColumn(3).setMinWidth(120);
        jTableResultado.getColumnModel().getColumn(3).setMaxWidth(150);

    }

    @Override
    public void setupTableDataArquivo(HashMap<String, List<RegistroInvalidoArquivo>> listaRegistroInvalido) {

        listaRegistro = listaRegistroInvalido;
        DefaultTableModel tableModel = (DefaultTableModel) jTableArquivos.getModel();

        if (listaRegistroInvalido == null)
            tableModel.setRowCount(0);

        if (listaRegistroInvalido != null && listaRegistroInvalido.size() > 0) {

            for (Entry<String, List<RegistroInvalidoArquivo>> e : listaRegistroInvalido.entrySet()) {
                String[] data = new String[2];

                if (!e.getValue().get(0).getOcorrencia().equals("Sem Inconsistencia")) {
                    data[0] = "error";
                    data[1] = "" + e.getKey();
                    tableModel.addRow(data);
                }
            }
        }

        jTableArquivos.setDefaultRenderer(Object.class, new StripedRowTableCellRenderer());
        tableModel.fireTableDataChanged();
    }

    private void setUpTableDataResultado(String chave) {
        List<RegistroInvalidoArquivo> listaRegistroInvalido = new ArrayList<>();

        if (chave != null) {
            listaRegistroInvalido = listaRegistro.get(chave);
        }

        DefaultTableModel tableModel = (DefaultTableModel) jTableResultado.getModel();

        tableModel.setRowCount(0);

        if (listaRegistroInvalido != null && listaRegistroInvalido.size() > 0) {

            for (RegistroInvalidoArquivo invalidos : listaRegistroInvalido) {
                String[] data = new String[5];

                data[0] = "" + invalidos.getNumLinha();
                data[1] = "" + invalidos.getPosicaoInicial();
                data[2] = "" + invalidos.getPosicaoFinal();
                data[3] = invalidos.getValorEncontrado();
                data[4] = invalidos.getOcorrencia();

                tableModel.addRow(data);
            }
        }
        jTableResultado.setModel(tableModel);
        jTableResultado.setDefaultRenderer(Object.class, new StripedRowTableCellRenderer());
        tableModel.fireTableDataChanged();
    }

    private void jbLocalizarArquivoAnexo() {
        limparCampos();
        try {
            jfc = new JFileChooser(localTemp) {

                private static final long serialVersionUID = -6636386619058848494L;

                @Override
                protected JDialog createDialog(Component parent) throws HeadlessException {
                    JDialog dialog = super.createDialog(parent);
                    dialog.setIconImage(new ImageIcon(diskette).getImage());
                    return dialog;
                }
            };

            int retorno = jfc.showOpenDialog(this);

            if (retorno == JFileChooser.APPROVE_OPTION) {
                localTemp = jfc.getCurrentDirectory().getAbsolutePath();
                validaArquivoCnab();
            } else {
                limparCampos();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void validaArquivoCnab() {
        ValidarArquivoCNAB actionPerform = new ValidarArquivoCNAB(this, jfc.getCurrentDirectory(), jProgressBar);
        actionPerform.execute();
    }

    private void limparCampos() {
        setupTableDataArquivo(null);
        setUpTableDataResultado(null);
        jProgressBar.setValue(0);
    }

    private void processar_pastaCnab240() {

        limparCampos();
        File pastaCnab240 = new File(localTemp);
        if (!pastaCnab240.exists()) {
            pastaCnab240.mkdir();
        }

        ValidarArquivoCNAB actionPerform = new ValidarArquivoCNAB(this, pastaCnab240, jProgressBar);
        actionPerform.execute();
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        // if(evt.getSource().equals(jtInputCooperativa)) {
        // processar_cooperativa(jtInputCooperativa.getText());
        // }
        if (evt.getSource().equals(jbPesquisar)) {
            jbLocalizarArquivoAnexo();
        }
        if (evt.getSource().equals(jbValidarPasta)) {
            processar_pastaCnab240();
        }
    }
}
