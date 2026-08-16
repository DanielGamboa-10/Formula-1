package com.formula1.gui;

import com.formula1.service.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

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

    private final List<NavButton> botonesNav = new ArrayList<>();
    private NavButton botonActivo = null;

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
        setSize(1240, 800);
        setMinimumSize(new Dimension(1020, 700));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Barra Lateral (Sidebar)
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(16, 18, 28));
        sidebar.setPreferredSize(new Dimension(250, getHeight()));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, F1Theme.BORDER_COLOR));

        // Logo / Título
        JPanel brandPanel = new JPanel(new BorderLayout(5, 5));
        brandPanel.setOpaque(false);
        brandPanel.setBorder(new EmptyBorder(25, 20, 20, 20));

        JLabel lblLogo = new JLabel("🏎️ FÓRMULA 1");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblLogo.setForeground(F1Theme.RED_PRIMARY);

        JLabel lblSub = new JLabel("SIMULATION SUITE");
        lblSub.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblSub.setForeground(new Color(200, 210, 230));

        brandPanel.add(lblLogo, BorderLayout.NORTH);
        brandPanel.add(lblSub, BorderLayout.SOUTH);
        sidebar.add(brandPanel);

        // Separador sutil
        JSeparator sep = new JSeparator();
        sep.setForeground(F1Theme.BORDER_COLOR);
        sep.setMaximumSize(new Dimension(250, 1));
        sidebar.add(sep);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));

        // Botones de Navegación Personalizados
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
        lblVersion.setForeground(new Color(160, 175, 205));
        lblVersion.setBorder(new EmptyBorder(15, 20, 15, 20));
        sidebar.add(lblVersion);

        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }

    private NavButton crearBotonNav(String texto, String cardName, boolean inicial) {
        NavButton btn = new NavButton(texto);
        btn.setActive(inicial);
        if (inicial) {
            botonActivo = btn;
        }
        botonesNav.add(btn);

        btn.addActionListener(e -> {
            for (NavButton b : botonesNav) {
                b.setActive(false);
            }
            btn.setActive(true);
            botonActivo = btn;

            if ("SIMULACION".equals(cardName)) {
                simulacionPanel.poblarCombos();
            } else if ("HISTORIAL".equals(cardName)) {
                historialPanel.cargarDatos();
            }

            cardLayout.show(contentPanel, cardName);
        });

        return btn;
    }

    /**
     * Componente personalizado de botón lateral con pintura manual para evitar
     * que Windows Look & Feel pinte el botón blanco.
     */
    private static class NavButton extends JButton {
        private boolean isActive = false;
        private boolean isHovered = false;

        public NavButton(String text) {
            super(text);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setMaximumSize(new Dimension(250, 46));
            setPreferredSize(new Dimension(250, 46));
            setAlignmentX(Component.CENTER_ALIGNMENT);
            setHorizontalAlignment(SwingConstants.LEFT);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setUI(new BasicButtonUI()); // Evita estilos nativos de Windows

            addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    isHovered = true;
                    repaint();
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    isHovered = false;
                    repaint();
                }
            });
        }

        public void setActive(boolean active) {
            this.isActive = active;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            if (isActive) {
                // Fondo Activo: Rojo F1 vibrante
                g2.setColor(F1Theme.RED_PRIMARY);
                g2.fillRect(0, 0, w, h);
                // Borde izquierdo indicador blanco
                g2.setColor(Color.WHITE);
                g2.fillRect(0, 0, 5, h);
                setForeground(Color.WHITE);
            } else if (isHovered) {
                // Fondo Hover: Azul oscuro elegante
                g2.setColor(new Color(36, 40, 60));
                g2.fillRect(0, 0, w, h);
                g2.setColor(F1Theme.ACCENT_CYAN);
                g2.fillRect(0, 0, 4, h);
                setForeground(Color.WHITE);
            } else {
                // Fondo Inactivo: Sidebar oscuro
                g2.setColor(new Color(16, 18, 28));
                g2.fillRect(0, 0, w, h);
                // Texto en color plateado brillante de alto contraste
                setForeground(new Color(230, 238, 255));
            }

            // Dibujar texto con padding
            FontMetrics fm = g2.getFontMetrics();
            int x = 20;
            int y = (h - fm.getHeight()) / 2 + fm.getAscent();

            g2.setColor(getForeground());
            g2.setFont(getFont());
            g2.drawString(getText(), x, y);

            g2.dispose();
        }
    }
}
