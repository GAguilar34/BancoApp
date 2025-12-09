package com.bankApp;

import com.bankApp.customer.model.Customer;
import com.bankApp.customer.service.CustomerService;
import com.bankApp.transaction.controller.TransactionController;
import com.bankApp.transaction.dto.TransactionDTO;
import com.bankApp.transaction.repository.TransactionRepositoryImpl;
import com.bankApp.transaction.service.TransactionService;
import com.bankApp.connection.ConnectionDB;
import com.bankApp.login.view.LoginFrame;
import com.bankApp.login.service.LoginService;
import com.bankApp.customer.controller.CustomerController;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

public class bankAppFrame extends JFrame {

    private Customer currentCustomer;
    private CustomerService customerService;
    private TransactionController transactionController;
    private ConnectionDB connectionDB;
    private CustomerController customerController;
    private LoginService loginService;


    // Componentes principales
    private JPanel mainPanel, sidebarPanel, contentPanel, balancePanel, buttonsPanel, transactionsPanel;
    private JLabel lblWelcome, lblBalance, lblAccountNumber, lblBankName;
    private JButton btnMiCuenta, btnDepositos, btnTransferencias, btnPagos, btnHistorial, btnCerrarSesion;
    private JButton btnDepositar, btnTransferir, btnPagar, btnVerTodo, btnEliminarCuenta;
    private JTable tableTransactions;
    private DefaultTableModel tableModel;

    private DecimalFormat moneyFormat = new DecimalFormat("$#,##0.00");
    private DateTimeFormatter dateFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);

    // Colores del tema moderno
    private final Color PRIMARY_BLUE = new Color(41, 128, 185);
    private final Color DARK_BLUE = new Color(31, 97, 141);
    private final Color BACKGROUND_WHITE = new Color(249, 250, 251);
    private final Color CARD_WHITE = Color.WHITE;
    private final Color TEXT_DARK = new Color(33, 37, 41);
    private final Color TEXT_LIGHT = new Color(108, 117, 125);
    private final Color SUCCESS_GREEN = new Color(46, 204, 113);
    private final Color DANGER_RED = new Color(231, 76, 60);
    private final Color WARNING_ORANGE = new Color(230, 126, 34);
    private final Color INFO_BLUE = new Color(52, 152, 219);
    private final Color BORDER_COLOR = new Color(229, 231, 235);

    public bankAppFrame(Customer customer) {
        this.customerController = new CustomerController();
        this.currentCustomer = customer;
        this.customerService = new CustomerService();
        this.connectionDB = new ConnectionDB();
        this.loginService = new LoginService(customerController);

        TransactionService transactionService = new TransactionService(
                new TransactionRepositoryImpl(connectionDB.getDatabase()),
                customerService
        );
        this.transactionController = new TransactionController(transactionService);

        setTitle("MiBanco - Dashboard");
        setSize(1200, 750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        initializeComponents();
        loadRecentTransactions();
    }

    private void initializeComponents() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_WHITE);
        this.add(mainPanel);

        createSidebar();
        createMainContent();
    }

    private void createSidebar() {
        sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(PRIMARY_BLUE);
        sidebarPanel.setPreferredSize(new Dimension(280, 750));
        sidebarPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        // Logo del banco
        JPanel logoPanel = new JPanel();
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));
        logoPanel.setBackground(DARK_BLUE);
        logoPanel.setMaximumSize(new Dimension(280, 100));
        logoPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        lblBankName = new JLabel("MiBanco");
        lblBankName.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblBankName.setForeground(Color.WHITE);
        lblBankName.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSubtitle = new JLabel("Banca Digital");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSubtitle.setForeground(new Color(200, 230, 255));
        lblSubtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        logoPanel.add(lblBankName);
        logoPanel.add(Box.createVerticalStrut(5));
        logoPanel.add(lblSubtitle);
        sidebarPanel.add(logoPanel);
        sidebarPanel.add(Box.createVerticalStrut(20));

        // Informacion del cliente
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
        userPanel.setBackground(PRIMARY_BLUE);
        userPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 20, 30));
        userPanel.setMaximumSize(new Dimension(280, 120));

        JLabel lblWelcome = new JLabel("Bienvenido");
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblWelcome.setForeground(new Color(220, 240, 255));
        lblWelcome.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblUserName = new JLabel(currentCustomer.getNombreCompleto());
        lblUserName.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblUserName.setForeground(Color.WHITE);
        lblUserName.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblUserEmail = new JLabel(currentCustomer.getEmail());
        lblUserEmail.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblUserEmail.setForeground(new Color(200, 230, 255));
        lblUserEmail.setAlignmentX(Component.LEFT_ALIGNMENT);

        userPanel.add(lblWelcome);
        userPanel.add(Box.createVerticalStrut(5));
        userPanel.add(lblUserName);
        userPanel.add(Box.createVerticalStrut(2));
        userPanel.add(lblUserEmail);

        sidebarPanel.add(userPanel);
        sidebarPanel.add(Box.createVerticalStrut(20));

        // Separador
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setForeground(new Color(70, 150, 220));
        separator.setMaximumSize(new Dimension(240, 1));
        sidebarPanel.add(separator);
        sidebarPanel.add(Box.createVerticalStrut(20));

        // Botones del menu
        btnMiCuenta = createMenuButton("Mi Cuenta", true);
        btnMiCuenta.addActionListener(e -> showMiCuenta());
        sidebarPanel.add(btnMiCuenta);

        btnDepositos = createMenuButton("Depositos", false);
        btnDepositos.addActionListener(e -> showDepositosHistory());
        sidebarPanel.add(btnDepositos);

        btnTransferencias = createMenuButton("Transferencias", false);
        btnTransferencias.addActionListener(e -> showTransferenciasHistory());
        sidebarPanel.add(btnTransferencias);

        btnPagos = createMenuButton("Pagos", false);
        btnPagos.addActionListener(e -> showPagosHistory());
        sidebarPanel.add(btnPagos);

        btnHistorial = createMenuButton("Historial", false);
        btnHistorial.addActionListener(e -> showFullHistory());
        sidebarPanel.add(btnHistorial);

        sidebarPanel.add(Box.createVerticalGlue());

        // Boton cerrar sesion
        btnCerrarSesion = createMenuButton("Cerrar Sesion", false);
        btnCerrarSesion.addActionListener(e -> logout());
        sidebarPanel.add(btnCerrarSesion);
        sidebarPanel.add(Box.createVerticalStrut(30));

        mainPanel.add(sidebarPanel, BorderLayout.WEST);

        sidebarPanel.add(Box.createVerticalStrut(10));

        // Botón Eliminar Cuenta (Rojo)
        btnEliminarCuenta = new JButton("Eliminar Cuenta");
        btnEliminarCuenta.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnEliminarCuenta.setForeground(Color.WHITE);
        btnEliminarCuenta.setBackground(DANGER_RED);
        btnEliminarCuenta.setBorderPainted(false);
        btnEliminarCuenta.setFocusPainted(false);
        btnEliminarCuenta.setContentAreaFilled(true);
        btnEliminarCuenta.setHorizontalAlignment(SwingConstants.LEFT);
        btnEliminarCuenta.setMaximumSize(new Dimension(280, 50));
        btnEliminarCuenta.setPreferredSize(new Dimension(280, 50));
        btnEliminarCuenta.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEliminarCuenta.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0));
        btnEliminarCuenta.addActionListener(e -> eliminarCuenta());

        // Efecto hover para el botón eliminar
        btnEliminarCuenta.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnEliminarCuenta.setBackground(new Color(210, 60, 45));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnEliminarCuenta.setBackground(DANGER_RED);
            }
        });

        sidebarPanel.add(btnEliminarCuenta);
        sidebarPanel.add(Box.createVerticalStrut(20));
    }

    private void eliminarCuenta() {
        // Primera confirmación
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "<html><b>¿ESTÁS SEGURO DE ELIMINAR TU CUENTA?</b><br><br>" +
                        "Esta acción es <b>IRREVERSIBLE</b>.<br>" +
                        "Se eliminarán todos tus datos personales y saldo.<br><br>" +
                        "Saldo actual: " + moneyFormat.format(currentCustomer.getSaldo()) + "</html>",
                "Confirmar Eliminación de Cuenta",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        // Segunda confirmación con password
        JPasswordField passwordField = new JPasswordField(20);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("Ingresa tu contraseña para confirmar:"), BorderLayout.NORTH);
        panel.add(passwordField, BorderLayout.CENTER);

        int passwordConfirm = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Verificar Identidad",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (passwordConfirm != JOptionPane.OK_OPTION) {
            return;
        }

        String password = new String(passwordField.getPassword());

        try {
            // Verificar contraseña
            if (!loginService.authenticate(currentCustomer.getEmail(), password)) {
                JOptionPane.showMessageDialog(
                        this,
                        "Contraseña incorrecta. La cuenta no se eliminó.",
                        "Error de Autenticación",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // Eliminar la cuenta
            boolean eliminado = customerService.deleteById(currentCustomer.getId());

            if (eliminado) {
                JOptionPane.showMessageDialog(
                        this,
                        "<html><b>Cuenta eliminada exitosamente</b><br>" +
                                "Gracias por usar MiBanco. Tus transacciones se mantendrán en nuestros registros.</html>",
                        "Cuenta Eliminada",
                        JOptionPane.INFORMATION_MESSAGE
                );

                // Volver al login
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
                dispose();

            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "No se pudo eliminar la cuenta. Intenta nuevamente.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error al eliminar la cuenta: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            e.printStackTrace();
        }
    }

    private JButton createMenuButton(String text, boolean selected) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(selected ? DARK_BLUE : PRIMARY_BLUE);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(true);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setMaximumSize(new Dimension(280, 50));
        button.setPreferredSize(new Dimension(280, 50));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!selected) {
                    button.setBackground(new Color(60, 140, 200));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!selected) {
                    button.setBackground(PRIMARY_BLUE);
                }
            }
        });

        return button;
    }

    private void createMainContent() {
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(BACKGROUND_WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Panel de Balance
        createBalancePanel();
        contentPanel.add(balancePanel);

        contentPanel.add(Box.createVerticalStrut(30));

        // Botones de acciones rapidas
        createQuickActionsPanel();
        contentPanel.add(buttonsPanel);

        contentPanel.add(Box.createVerticalStrut(30));

        // Panel de movimientos recientes
        createTransactionsPanel();
        contentPanel.add(transactionsPanel);

        mainPanel.add(contentPanel, BorderLayout.CENTER);
    }

    private void createBalancePanel() {
        balancePanel = new JPanel();
        balancePanel.setLayout(new BorderLayout());
        balancePanel.setBackground(CARD_WHITE);
        balancePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        balancePanel.setMaximumSize(new Dimension(900, 180));
        balancePanel.setPreferredSize(new Dimension(900, 180));

        // Panel izquierdo: Informacion de saldo
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(CARD_WHITE);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 40));

        JLabel lblBalanceTitle = new JLabel("Balance Total");
        lblBalanceTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblBalanceTitle.setForeground(TEXT_LIGHT);
        lblBalanceTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblBalance = new JLabel(moneyFormat.format(currentCustomer.getSaldo()));
        lblBalance.setFont(new Font("Segoe UI", Font.BOLD, 42));
        lblBalance.setForeground(TEXT_DARK);
        lblBalance.setAlignmentX(Component.LEFT_ALIGNMENT);

        leftPanel.add(lblBalanceTitle);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(lblBalance);

        // Panel derecho: Informacion de cuenta
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(CARD_WHITE);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 0));

        JLabel lblAccountTitle = new JLabel("Numero de Cuenta");
        lblAccountTitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblAccountTitle.setForeground(TEXT_LIGHT);
        lblAccountTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Formato de tarjeta con espacios
        String accountNumber = String.format("%04d", currentCustomer.getId());
        String formattedAccount = "**** **** **** " + accountNumber;
        lblAccountNumber = new JLabel(formattedAccount);
        lblAccountNumber.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblAccountNumber.setForeground(TEXT_DARK);
        lblAccountNumber.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblAccountType = new JLabel("Cuenta Corriente");
        lblAccountType.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblAccountType.setForeground(TEXT_LIGHT);
        lblAccountType.setAlignmentX(Component.LEFT_ALIGNMENT);

        rightPanel.add(lblAccountTitle);
        rightPanel.add(Box.createVerticalStrut(5));
        rightPanel.add(lblAccountNumber);
        rightPanel.add(Box.createVerticalStrut(5));
        rightPanel.add(lblAccountType);

        balancePanel.add(leftPanel, BorderLayout.WEST);
        balancePanel.add(rightPanel, BorderLayout.CENTER);

        // Fecha actual
        JLabel lblDate = new JLabel("Actualizado hoy");
        lblDate.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblDate.setForeground(TEXT_LIGHT);
        balancePanel.add(lblDate, BorderLayout.SOUTH);
    }

    private void createQuickActionsPanel() {
        buttonsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        buttonsPanel.setBackground(BACKGROUND_WHITE);
        buttonsPanel.setMaximumSize(new Dimension(900, 110));
        buttonsPanel.setPreferredSize(new Dimension(900, 110));

        btnDepositar = createActionCard("Depositar", SUCCESS_GREEN);
        btnDepositar.addActionListener(e -> openDepositDialog());

        btnTransferir = createActionCard("Transferir", INFO_BLUE);
        btnTransferir.addActionListener(e -> openTransferDialog());

        btnPagar = createActionCard("Pagar", WARNING_ORANGE);
        btnPagar.addActionListener(e -> openPaymentDialog());

        btnVerTodo = createActionCard("Ver Todo", PRIMARY_BLUE);
        btnVerTodo.addActionListener(e -> showFullHistory());

        buttonsPanel.add(btnDepositar);
        buttonsPanel.add(btnTransferir);
        buttonsPanel.add(btnPagar);
        buttonsPanel.add(btnVerTodo);
    }

    private JButton createActionCard(String text, Color color) {
        JButton button = new JButton();
        button.setLayout(new BorderLayout());
        button.setBackground(CARD_WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Efecto hover
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(250, 250, 250));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(CARD_WHITE);
            }
        });

        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        textLabel.setForeground(TEXT_DARK);
        textLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Indicador de color
        JPanel colorIndicator = new JPanel();
        colorIndicator.setBackground(color);
        colorIndicator.setPreferredSize(new Dimension(60, 4));
        colorIndicator.setMaximumSize(new Dimension(60, 4));

        button.add(colorIndicator, BorderLayout.NORTH);
        button.add(textLabel, BorderLayout.CENTER);

        return button;
    }

    private void createTransactionsPanel() {
        transactionsPanel = new JPanel();
        transactionsPanel.setLayout(new BorderLayout());
        transactionsPanel.setBackground(CARD_WHITE);
        transactionsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));
        transactionsPanel.setMaximumSize(new Dimension(900, 350));
        transactionsPanel.setPreferredSize(new Dimension(900, 350));

        // Cabecera del panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(CARD_WHITE);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR),
                BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));

        JLabel lblTitle = new JLabel("Movimientos Recientes");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(TEXT_DARK);

        JButton btnRefresh = new JButton("Actualizar");
        btnRefresh.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnRefresh.setBackground(INFO_BLUE);
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setBorderPainted(false);
        btnRefresh.setFocusPainted(false);
        btnRefresh.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefresh.addActionListener(e -> loadRecentTransactions());

        headerPanel.add(lblTitle, BorderLayout.WEST);
        headerPanel.add(btnRefresh, BorderLayout.EAST);

        transactionsPanel.add(headerPanel, BorderLayout.NORTH);

        // Tabla de transacciones
        String[] columns = {"Tipo", "Descripcion", "Fecha", "Monto", "Saldo"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return String.class;
            }
        };

        tableTransactions = new JTable(tableModel);
        tableTransactions.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tableTransactions.setRowHeight(45);
        tableTransactions.setShowGrid(false);
        tableTransactions.setIntercellSpacing(new Dimension(0, 0));
        tableTransactions.setBackground(CARD_WHITE);
        tableTransactions.setSelectionBackground(new Color(245, 245, 245));

        // Remover seleccion por defecto
        tableTransactions.setRowSelectionAllowed(false);

        // Estilo del header
        JTableHeader header = tableTransactions.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(250, 250, 250));
        header.setForeground(TEXT_DARK);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR));
        header.setReorderingAllowed(false);

        // Renderers personalizados
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);

        // Aplicar renderers
        tableTransactions.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tableTransactions.getColumnModel().getColumn(0).setPreferredWidth(100);

        tableTransactions.getColumnModel().getColumn(1).setPreferredWidth(300);

        tableTransactions.getColumnModel().getColumn(2).setPreferredWidth(120);

        tableTransactions.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
        tableTransactions.getColumnModel().getColumn(3).setPreferredWidth(120);

        tableTransactions.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
        tableTransactions.getColumnModel().getColumn(4).setPreferredWidth(120);

        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(tableTransactions);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(CARD_WHITE);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Estilo del scrollbar
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setPreferredSize(new Dimension(8, Integer.MAX_VALUE));
        verticalScrollBar.setBackground(CARD_WHITE);
        verticalScrollBar.setBorder(BorderFactory.createEmptyBorder());

        transactionsPanel.add(scrollPane, BorderLayout.CENTER);
    }

    private void loadRecentTransactions() {
        tableModel.setRowCount(0);

        try {
            List<TransactionDTO> transactions = transactionController.getRecentTransactions(currentCustomer.getId(), 10);

            if (transactions.isEmpty()) {
                // Mostrar mensaje si no hay transacciones
                Object[] emptyRow = {"No hay", "transacciones", "recientes", "", ""};
                tableModel.addRow(emptyRow);
            } else {
                for (TransactionDTO transaction : transactions) {
                    String tipo = getTransactionTypeSpanish(transaction.getType());
                    String descripcion = transaction.getDescription();
                    String fecha = transaction.getTransactionDate().format(dateFormat);

                    String monto;
                    Color montoColor = TEXT_DARK;

                    if (transaction.getType().contains("DEPOSIT") || transaction.getType().contains("TRANSFER_IN")) {
                        monto = "+" + moneyFormat.format(transaction.getAmount());
                    } else {
                        monto = "-" + moneyFormat.format(transaction.getAmount());
                    }

                    String saldo = moneyFormat.format(transaction.getNewBalance());

                    Object[] row = {tipo, descripcion, fecha, monto, saldo};
                    tableModel.addRow(row);
                }
            }

            // Actualizar balance
            double currentBalance = transactionController.getCurrentBalance(currentCustomer.getId());
            lblBalance.setText(moneyFormat.format(currentBalance));
            currentCustomer.setSaldo(currentBalance);

            System.out.println("Transacciones cargadas: " + transactions.size());

        } catch (Exception e) {
            System.out.println("Error al cargar transacciones: " + e.getMessage());
            e.printStackTrace();

            // Datos de ejemplo para debugging
            Object[][] sampleData = {
                    {"Deposito", "Transferencia recibida", "05/12/2025", "+$1,200.00", "$25,430.50"},
                    {"Transferencia", "Pago a amigo", "04/12/2025", "-$500.00", "$24,230.50"},
                    {"Pago", "Servicio de internet", "03/12/2025", "-$85.50", "$24,145.00"},
                    {"Deposito", "Salario mensual", "01/12/2025", "+$2,500.00", "$26,645.00"},
                    {"Pago", "Supermercado", "28/11/2025", "-$120.75", "$24,230.50"}
            };

            for (Object[] row : sampleData) {
                tableModel.addRow(row);
            }
        }
    }

    private String getTransactionTypeSpanish(String type) {
        if (type.contains("DEPOSIT")) return "Deposito";
        if (type.contains("WITHDRAWAL")) return "Retiro";
        if (type.contains("TRANSFER")) return "Transferencia";
        if (type.contains("PAYMENT")) return "Pago";
        if (type.contains("FEE")) return "Comision";
        if (type.contains("INTEREST")) return "Interes";
        return "Transaccion";
    }

    // Metodos para todas las transacciones

    private void openDepositDialog() {
        JDialog dialog = new JDialog(this, "Realizar Deposito", true);
        dialog.setSize(450, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(CARD_WHITE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        panel.setBackground(CARD_WHITE);

        JLabel lblTitle = new JLabel("Realizar Deposito");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(TEXT_DARK);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblTitle);

        panel.add(Box.createVerticalStrut(25));

        JLabel lblMonto = new JLabel("Monto a Depositar:");
        lblMonto.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblMonto.setForeground(TEXT_DARK);
        lblMonto.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblMonto);

        panel.add(Box.createVerticalStrut(8));

        JTextField txtMonto = new JTextField();
        txtMonto.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtMonto.setMaximumSize(new Dimension(400, 45));
        txtMonto.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtMonto.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panel.add(txtMonto);

        panel.add(Box.createVerticalStrut(20));

        JLabel lblDesc = new JLabel("Descripcion (opcional):");
        lblDesc.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblDesc.setForeground(TEXT_DARK);
        lblDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblDesc);

        panel.add(Box.createVerticalStrut(8));

        JTextField txtDesc = new JTextField();
        txtDesc.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtDesc.setMaximumSize(new Dimension(400, 45));
        txtDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtDesc.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panel.add(txtDesc);

        panel.add(Box.createVerticalStrut(30));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(CARD_WHITE);
        buttonPanel.setMaximumSize(new Dimension(400, 50));
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCancelar.setPreferredSize(new Dimension(110, 40));
        btnCancelar.setBackground(new Color(240, 240, 240));
        btnCancelar.setForeground(TEXT_DARK);
        btnCancelar.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        btnCancelar.setFocusPainted(false);
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.addActionListener(e -> dialog.dispose());

        JButton btnConfirmar = new JButton("Depositar");
        btnConfirmar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnConfirmar.setPreferredSize(new Dimension(110, 40));
        btnConfirmar.setBackground(SUCCESS_GREEN);
        btnConfirmar.setForeground(Color.WHITE);
        btnConfirmar.setBorderPainted(false);
        btnConfirmar.setFocusPainted(false);
        btnConfirmar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnConfirmar.addActionListener(e -> {
            try {
                double monto = Double.parseDouble(txtMonto.getText());
                if (monto <= 0) {
                    JOptionPane.showMessageDialog(dialog, "El monto debe ser mayor a cero", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String descripcion = txtDesc.getText().trim().isEmpty() ?
                        "Deposito en cuenta" : txtDesc.getText();

                TransactionDTO transaction = transactionController.deposit(currentCustomer.getId(), monto, descripcion);

                // Actualizar la interfaz
                currentCustomer = customerService.findById(currentCustomer.getId()).get();
                lblBalance.setText(moneyFormat.format(currentCustomer.getSaldo()));
                loadRecentTransactions();

                JOptionPane.showMessageDialog(dialog,
                        "<html><b>Deposito realizado exitosamente</b><br>" +
                                "Monto: " + moneyFormat.format(monto) + "<br>" +
                                "Nuevo saldo: " + moneyFormat.format(currentCustomer.getSaldo()) + "</html>",
                        "Exito",
                        JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Por favor ingrese un monto valido", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonPanel.add(btnCancelar);
        buttonPanel.add(btnConfirmar);
        panel.add(buttonPanel);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void openTransferDialog() {
        JDialog dialog = new JDialog(this, "Realizar Transferencia", true);
        dialog.setSize(500, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(CARD_WHITE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        panel.setBackground(CARD_WHITE);

        JLabel lblTitle = new JLabel("Realizar Transferencia");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(TEXT_DARK);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblTitle);

        panel.add(Box.createVerticalStrut(25));

        // ID del destinatario
        JLabel lblDestinatario = new JLabel("ID del Destinatario:");
        lblDestinatario.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblDestinatario.setForeground(TEXT_DARK);
        lblDestinatario.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblDestinatario);

        panel.add(Box.createVerticalStrut(8));

        JTextField txtDestinatario = new JTextField();
        txtDestinatario.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtDestinatario.setMaximumSize(new Dimension(400, 45));
        txtDestinatario.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtDestinatario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panel.add(txtDestinatario);

        panel.add(Box.createVerticalStrut(20));

        // Monto
        JLabel lblMonto = new JLabel("Monto a Transferir:");
        lblMonto.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblMonto.setForeground(TEXT_DARK);
        lblMonto.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblMonto);

        panel.add(Box.createVerticalStrut(8));

        JTextField txtMonto = new JTextField();
        txtMonto.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtMonto.setMaximumSize(new Dimension(400, 45));
        txtMonto.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtMonto.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panel.add(txtMonto);

        panel.add(Box.createVerticalStrut(20));

        // Descripcion
        JLabel lblDesc = new JLabel("Descripcion (opcional):");
        lblDesc.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblDesc.setForeground(TEXT_DARK);
        lblDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblDesc);

        panel.add(Box.createVerticalStrut(8));

        JTextField txtDesc = new JTextField();
        txtDesc.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtDesc.setMaximumSize(new Dimension(400, 45));
        txtDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtDesc.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panel.add(txtDesc);

        panel.add(Box.createVerticalStrut(30));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(CARD_WHITE);
        buttonPanel.setMaximumSize(new Dimension(400, 50));
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCancelar.setPreferredSize(new Dimension(110, 40));
        btnCancelar.setBackground(new Color(240, 240, 240));
        btnCancelar.setForeground(TEXT_DARK);
        btnCancelar.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        btnCancelar.setFocusPainted(false);
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.addActionListener(e -> dialog.dispose());

        JButton btnConfirmar = new JButton("Transferir");
        btnConfirmar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnConfirmar.setPreferredSize(new Dimension(110, 40));
        btnConfirmar.setBackground(INFO_BLUE);
        btnConfirmar.setForeground(Color.WHITE);
        btnConfirmar.setBorderPainted(false);
        btnConfirmar.setFocusPainted(false);
        btnConfirmar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnConfirmar.addActionListener(e -> {
            try {
                int destinatarioId = Integer.parseInt(txtDestinatario.getText());
                double monto = Double.parseDouble(txtMonto.getText());
                String descripcion = txtDesc.getText().trim().isEmpty() ?
                        "Transferencia a cuenta " + destinatarioId : txtDesc.getText();

                if (monto <= 0) {
                    JOptionPane.showMessageDialog(dialog, "El monto debe ser mayor a cero", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (destinatarioId == currentCustomer.getId()) {
                    JOptionPane.showMessageDialog(dialog, "No puedes transferirte a ti mismo", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                TransactionDTO transaction = transactionController.transfer(
                        currentCustomer.getId(),
                        destinatarioId,
                        monto,
                        descripcion
                );

                // Actualizar la interfaz
                currentCustomer = customerService.findById(currentCustomer.getId()).get();
                lblBalance.setText(moneyFormat.format(currentCustomer.getSaldo()));
                loadRecentTransactions();

                JOptionPane.showMessageDialog(dialog,
                        "<html><b>Transferencia realizada exitosamente</b><br>" +
                                "Monto: " + moneyFormat.format(monto) + "<br>" +
                                "Destinatario: " + destinatarioId + "<br>" +
                                "Nuevo saldo: " + moneyFormat.format(currentCustomer.getSaldo()) + "</html>",
                        "Exito",
                        JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Por favor ingrese valores validos", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonPanel.add(btnCancelar);
        buttonPanel.add(btnConfirmar);
        panel.add(buttonPanel);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void openPaymentDialog() {
        JDialog dialog = new JDialog(this, "Realizar Pago de Servicio", true);
        dialog.setSize(500, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(CARD_WHITE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        panel.setBackground(CARD_WHITE);

        JLabel lblTitle = new JLabel("Pago de Servicio");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(TEXT_DARK);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblTitle);

        panel.add(Box.createVerticalStrut(25));

        // Servicio
        JLabel lblServicio = new JLabel("Servicio:");
        lblServicio.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblServicio.setForeground(TEXT_DARK);
        lblServicio.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblServicio);

        panel.add(Box.createVerticalStrut(8));

        String[] servicios = {"Internet", "Telefono", "Luz", "Agua", "Gas", "TV Cable", "Universidad", "Otro"};
        JComboBox<String> cmbServicio = new JComboBox<>(servicios);
        cmbServicio.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        cmbServicio.setMaximumSize(new Dimension(400, 45));
        cmbServicio.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(cmbServicio);

        panel.add(Box.createVerticalStrut(20));

        // Monto
        JLabel lblMonto = new JLabel("Monto a Pagar:");
        lblMonto.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblMonto.setForeground(TEXT_DARK);
        lblMonto.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblMonto);

        panel.add(Box.createVerticalStrut(8));

        JTextField txtMonto = new JTextField();
        txtMonto.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtMonto.setMaximumSize(new Dimension(400, 45));
        txtMonto.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtMonto.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panel.add(txtMonto);

        panel.add(Box.createVerticalStrut(20));

        // Descripcion
        JLabel lblDesc = new JLabel("Descripcion (opcional):");
        lblDesc.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblDesc.setForeground(TEXT_DARK);
        lblDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblDesc);

        panel.add(Box.createVerticalStrut(8));

        JTextField txtDesc = new JTextField();
        txtDesc.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtDesc.setMaximumSize(new Dimension(400, 45));
        txtDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtDesc.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panel.add(txtDesc);

        panel.add(Box.createVerticalStrut(30));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(CARD_WHITE);
        buttonPanel.setMaximumSize(new Dimension(400, 50));
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCancelar.setPreferredSize(new Dimension(110, 40));
        btnCancelar.setBackground(new Color(240, 240, 240));
        btnCancelar.setForeground(TEXT_DARK);
        btnCancelar.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        btnCancelar.setFocusPainted(false);
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.addActionListener(e -> dialog.dispose());

        JButton btnConfirmar = new JButton("Pagar");
        btnConfirmar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnConfirmar.setPreferredSize(new Dimension(110, 40));
        btnConfirmar.setBackground(WARNING_ORANGE);
        btnConfirmar.setForeground(Color.WHITE);
        btnConfirmar.setBorderPainted(false);
        btnConfirmar.setFocusPainted(false);
        btnConfirmar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnConfirmar.addActionListener(e -> {
            try {
                String servicio = (String) cmbServicio.getSelectedItem();
                double monto = Double.parseDouble(txtMonto.getText());
                String descripcion = txtDesc.getText().trim().isEmpty() ?
                        "Pago de " + servicio : txtDesc.getText();

                if (monto <= 0) {
                    JOptionPane.showMessageDialog(dialog, "El monto debe ser mayor a cero", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                TransactionDTO transaction = transactionController.payService(
                        currentCustomer.getId(),
                        servicio,
                        monto,
                        descripcion
                );

                // Actualizar la interfaz
                currentCustomer = customerService.findById(currentCustomer.getId()).get();
                lblBalance.setText(moneyFormat.format(currentCustomer.getSaldo()));
                loadRecentTransactions();

                JOptionPane.showMessageDialog(dialog,
                        "<html><b>Pago realizado exitosamente</b><br>" +
                                "Servicio: " + servicio + "<br>" +
                                "Monto: " + moneyFormat.format(monto) + "<br>" +
                                "Nuevo saldo: " + moneyFormat.format(currentCustomer.getSaldo()) + "</html>",
                        "Exito",
                        JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Por favor ingrese un monto valido", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonPanel.add(btnCancelar);
        buttonPanel.add(btnConfirmar);
        panel.add(buttonPanel);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    // Metodos para las vistas del menu

    private void showMiCuenta() {
        JDialog dialog = new JDialog(this, "Mi Cuenta", true);
        dialog.setSize(600, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(CARD_WHITE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        panel.setBackground(CARD_WHITE);

        JLabel lblTitle = new JLabel("Informacion de Cuenta");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(TEXT_DARK);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblTitle);

        panel.add(Box.createVerticalStrut(30));

        // Informacion personal
        JPanel infoPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        infoPanel.setBackground(CARD_WHITE);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Datos del cliente
        infoPanel.add(createInfoLabel("ID de Cliente:"));
        infoPanel.add(createInfoValue(String.valueOf(currentCustomer.getId())));

        infoPanel.add(createInfoLabel("Nombre:"));
        infoPanel.add(createInfoValue(currentCustomer.getNombreCompleto()));

        infoPanel.add(createInfoLabel("Email:"));
        infoPanel.add(createInfoValue(currentCustomer.getEmail()));

        infoPanel.add(createInfoLabel("Direccion:"));
        infoPanel.add(createInfoValue(currentCustomer.getDireccion()));

        infoPanel.add(createInfoLabel("Edad:"));
        infoPanel.add(createInfoValue(String.valueOf(currentCustomer.getEdad())));

        infoPanel.add(createInfoLabel("Saldo Actual:"));
        infoPanel.add(createInfoValue(moneyFormat.format(currentCustomer.getSaldo())));

        panel.add(infoPanel);
        panel.add(Box.createVerticalStrut(30));

        // Boton cerrar
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCerrar.setPreferredSize(new Dimension(120, 40));
        btnCerrar.setBackground(PRIMARY_BLUE);
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCerrar.setBorderPainted(false);
        btnCerrar.setFocusPainted(false);
        btnCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrar.addActionListener(e -> dialog.dispose());

        panel.add(btnCerrar);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private JLabel createInfoLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(TEXT_DARK);
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        return label;
    }

    private JLabel createInfoValue(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(TEXT_LIGHT);
        label.setHorizontalAlignment(SwingConstants.LEFT);
        return label;
    }

    private void showDepositosHistory() {
        try {
            List<TransactionDTO> deposits = transactionController.getTransactionsByType(currentCustomer.getId(), "DEPOSIT");
            showTransactionHistoryDialog("Historial de Depositos", deposits);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar depositos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showTransferenciasHistory() {
        try {
            List<TransactionDTO> transfersOut = transactionController.getTransactionsByType(currentCustomer.getId(), "TRANSFER_OUT");
            List<TransactionDTO> transfersIn = transactionController.getTransactionsByType(currentCustomer.getId(), "TRANSFER_IN");

            // Combinar ambas listas
            transfersOut.addAll(transfersIn);
            showTransactionHistoryDialog("Historial de Transferencias", transfersOut);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar transferencias: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showPagosHistory() {
        try {
            List<TransactionDTO> payments = transactionController.getTransactionsByType(currentCustomer.getId(), "PAYMENT");
            showTransactionHistoryDialog("Historial de Pagos", payments);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar pagos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showFullHistory() {
        try {
            List<TransactionDTO> allTransactions = transactionController.getTransactionHistory(currentCustomer.getId());
            showTransactionHistoryDialog("Historial Completo", allTransactions);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar historial: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showTransactionHistoryDialog(String title, List<TransactionDTO> transactions) {
        JDialog dialog = new JDialog(this, title, true);
        dialog.setSize(800, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        String[] columns = {"ID", "Tipo", "Descripcion", "Fecha", "Monto", "Saldo"};
        DefaultTableModel historyModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable historyTable = new JTable(historyModel);
        historyTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        historyTable.setRowHeight(35);

        // Llenar la tabla
        for (TransactionDTO transaction : transactions) {
            String tipo = getTransactionTypeSpanish(transaction.getType());
            String monto = (transaction.getType().contains("DEPOSIT") || transaction.getType().contains("TRANSFER_IN")) ?
                    "+" + moneyFormat.format(transaction.getAmount()) :
                    "-" + moneyFormat.format(transaction.getAmount());

            historyModel.addRow(new Object[]{
                    transaction.getTransactionId(),
                    tipo,
                    transaction.getDescription(),
                    transaction.getTransactionDate().format(dateFormat),
                    monto,
                    moneyFormat.format(transaction.getNewBalance())
            });
        }

        JScrollPane scrollPane = new JScrollPane(historyTable);

        // Boton cerrar
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnCerrar);

        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "<html><b>Estas seguro de cerrar sesion?</b><br>Seras redirigido al inicio de sesion.</html>",
                "Confirmar Cierre de Sesion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
            dispose();
        }
    }
}