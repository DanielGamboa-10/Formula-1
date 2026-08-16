package com.formula1.gui;

import com.formula1.service.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainFrame extends JFrame {
    private final CardLayout cardLayout;
    private final JPanel contentPanel;

    private final PilotosPanel pilotosPanel;
    private final EquiposPanel equiposPanel;
    private final CircuitosPanel circuitosPanel;
    private final VehiculosPanel vehiculosPanel;
    private final ConfiguracionPanel configuracionPanel;
    private final SimulacionPanel simulacionPanel;
    private final HistorialPanel historialPanel;

    private JButton btnNavActivo = null;

    public MainFrame() {
        super("🏎️ Fórmula 1 - Sistema Integral de Simulación y Gestión");

        // Inicializar Servicios
        PilotoService pilotoService = new PilotoService();
        EquipoService equipoService = new EquipoService();
        CircuitoService circuitoService = new CircuitoService();
        VehiculoService vehiculoService = new VehiculoService();
        ConfiguracionService configuracionService = new ConfiguracionService();
        SimulacionService simulacionService = new SimulacionService();
        EstadisticaService estadisticaService = new EstadisticaService();

        // Inicializar Paneles
        this.pilotosPanel = new PilotosPanel(pilotoService);
        this.equiposPanel = new EquiposPanel(equipoService);
        this.circuitosPanel = new CircuitosPanel(circuitoService);
        this.vehiculosPanel = new VehiculosPanel(vehiculoService);
        this.configuracionPanel = new ConfiguracionPanel(configuracionService);
        this.simulacionPanel = new SimulacionPanel(simulacionService, circuitoService, pilotoService, vehiculoService, configuracionService);
        this.historialPanel = new HistorialPanel(estadisticaService);

        // Layout de Contenido
        this.cardLayout = new CardLayout();
        this.contentPanel = new JPanel(cardLayout);
        this.contentPanel.setBackground(F1Theme.BG_DARK);

        contentPanel.add(simulacionPanel, "SIMULACION");
        contentPanel.add(vehiculosPanel, "VEHICULOS");
        contentPanel.add(configuracionPanel, "CONFIGURACION");
        contentPanel.add(pilotosPanel, "PILOTOS");
        contentPanel.add(equiposPanel, "EQUIPOS");
        contentPanel.add(circuitosPanel, "CIRCUITOS");
        contentPanel.add(historialPanel, "HISTORIAL");

        initFrame();
    }

    private void initFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 780);
        setMinimumSize(new Dimension(1000, 680));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Barra Lateral (Sidebar) de Navegación F1
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(F1Theme.BG_SIDEBAR);
        sidebar.setPreferredSize(new Dimension(240, getHeight()));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, F1Theme.BORDER_COLOR));

        // Logo / Título en la barra lateral
        JPanel brandPanel = new JPanel(new BorderLayout(5, 5));
        brandPanel.setOpaque(false);
        brandPanel.setBorder(new EmptyBorder(25, 20, 25, 20));

        JLabel lblLogo = new JLabel("🏎️ FÓRMULA 1");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblLogo.setForeground(F1Theme.RED_PRIMARY);

        JLabel lblSub = new JLabel("SIMULATION SUITE");
        lblSub.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblSub.setForeground(F1Theme.TEXT_MUTED);

        brandPanel.add(lblLogo, BorderLayout.NORTH);
        brandPanel.add(lblSub, BorderLayout.SOUTH);
        sidebar.add(brandPanel);

        // Botones de Navegación
        sidebar.add(crearBotonNav("🚦 Clasificación F1", "SIMULACION", true));
        sidebar.add(crearBotonNav("🚗 Monoplazas & Telemetría", "VEHICULOS", false));
        sidebar.add(crearBotonNav("⚙️ Reglajes / Setup", "CONFIGURACION", false));
        sidebar.add(crearBotonNav("🏎️ Pilotos FIA", "PILOTOS", false));
        sidebar.add(crearBotonNav("🏁 Escuderías", "EQUIPOS", false));
        sidebar.add(crearBotonNav("🗺️ Circuitos", "CIRCUITOS", false));
        sidebar.add(crearBotonNav("📊 Historial & Stats", "HISTORIAL", false));

        sidebar.add(Box.createVerticalGlue());

        JLabel lblVersion = new JLabel("v1.0.0 • Java SE Puro");
        lblVersion.setFont(F1Theme.FONT_SMALL);
        lblVersion.setForeground(F1Theme.TEXT_MUTED);
        lblVersion.setBorder(new EmptyBorder(15, 20, 15, 20));
        sidebar.add(lblVersion);

        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }

    private JButton crearBotonNav(String texto, String cardName, boolean inicial) {
        JButton btn = new JButton(texto);
        btn.setFont(F1Theme.FONT_BOLD);
        btn.setMaximumSize(new Dimension(240, 48));
        btn.setPreferredSize(new Dimension(240, 48));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        if (inicial) {
            btn.setBackground(F1Theme.RED_PRIMARY);
            btn.setForeground(F1Theme.TEXT_PRIMARY);
            btnNavActivo = btn;
        } else {
            btn.setBackground(F1Theme.BG_SIDEBAR);
            btn.setForeground(F1Theme.TEXT_MUTED);
        }

        btn.addActionListener(e -> {
            if (btnNavActivo != null) {
                btnNavActivo.setBackground(F1Theme.BG_SIDEBAR);
                btnNavActivo.setForeground(F1Theme.TEXT_MUTED);
            }
            btn.setBackground(F1Theme.RED_PRIMARY);
            btn.setForeground(F1Theme.TEXT_PRIMARY);
            btnNavActivo = btn;

            // Actualizar datos si es necesario
            if ("SIMULACION".equals(cardName)) {
                simulacionPanel.poblarCombos();
            } else if ("HISTORIAL".equals(cardName)) {
                historialPanel.cargarDatos();
            }

            cardLayout.show(contentPanel, cardName);
        });

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (btn != btnNavActivo) {
                    btn.setBackground(F1Theme.BG_CARD);
                    btn.setForeground(F1Theme.TEXT_PRIMARY);
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (btn != btnNavActivo) {
                    btn.setBackground(F1Theme.BG_SIDEBAR);
                    btn.setForeground(F1Theme.TEXT_MUTED);
                }
            }
        });

        return btn;
    }
}
