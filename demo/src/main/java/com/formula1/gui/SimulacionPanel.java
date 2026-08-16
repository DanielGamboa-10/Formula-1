package com.formula1.gui;

import com.formula1.model.*;
import com.formula1.service.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class SimulacionPanel extends JPanel {
    private final SimulacionService simulacionService;
    private final CircuitoService circuitoService;
    private final PilotoService pilotoService;
    private final VehiculoService vehiculoService;
    private final ConfiguracionService configuracionService;

    private JComboBox<String> cbCircuitos;
    private JComboBox<String> cbPilotos;
    private JComboBox<String> cbVehiculos;
    private JComboBox<String> cbClima;

    private JTable tablaResultados;
    private DefaultTableModel tableModel;
    private JLabel lblPoleBanner;
    private JLabel lblUserPosBanner;
    private JProgressBar progressBar;
    private JButton btnSimular;

    public SimulacionPanel(SimulacionService simulacionService, CircuitoService circuitoService,
                           PilotoService pilotoService, VehiculoService vehiculoService,
                           ConfiguracionService configuracionService) {
        this.simulacionService = simulacionService;
        this.circuitoService = circuitoService;
        this.pilotoService = pilotoService;
        this.vehiculoService = vehiculoService;
        this.configuracionService = configuracionService;

        setLayout(new BorderLayout(15, 15));
        setBackground(F1Theme.BG_DARK);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        initUI();
    }

    private void initUI() {
        // Encabezado con selectores de Carrera
        JPanel topContainer = new JPanel(new BorderLayout(10, 10));
        topContainer.setOpaque(false);

        JLabel lblTitulo = new JLabel("🚦 Simulador Oficial de Sesión de Clasificación");
        lblTitulo.setFont(F1Theme.FONT_TITLE);
        lblTitulo.setForeground(F1Theme.TEXT_PRIMARY);
        topContainer.add(lblTitulo, BorderLayout.NORTH);

        // Barra de Controles de Simulación
        JPanel setupBar = F1Theme.crearTarjeta();
        setupBar.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));

        cbCircuitos = new JComboBox<>();
        cbPilotos = new JComboBox<>();
        cbVehiculos = new JComboBox<>();
        cbClima = new JComboBox<>(new String[]{"🎲 Clima Aleatorio (Radar)", "☀️ Pista Seca", "🌧️ Lluvia Moderada", "⛈️ Lluvia Extrema"});

        poblarCombos();

        setupBar.add(crearItemControl("Circuito:", cbCircuitos));
        setupBar.add(crearItemControl("Tu Piloto:", cbPilotos));
        setupBar.add(crearItemControl("Tu Monoplaza:", cbVehiculos));
        setupBar.add(crearItemControl("Clima:", cbClima));

        btnSimular = F1Theme.crearBotonPrimario("INICIAR CLASIFICACIÓN", "🚦");
        btnSimular.setFont(F1Theme.FONT_TITLE);
        btnSimular.addActionListener(e -> ejecutarSimulacionAnimada());
        setupBar.add(btnSimular);

        topContainer.add(setupBar, BorderLayout.CENTER);

        // Banners de Estado (Pole Position y Resultado Usuario)
        JPanel bannerContainer = new JPanel(new GridLayout(2, 1, 5, 5));
        bannerContainer.setOpaque(false);

        lblPoleBanner = new JLabel("🏆 Seleccione los parámetros y presione INICIAR CLASIFICACIÓN para abrir la pista.");
        lblPoleBanner.setFont(F1Theme.FONT_SUBTITLE);
        lblPoleBanner.setForeground(F1Theme.ACCENT_GOLD);

        lblUserPosBanner = new JLabel("");
        lblUserPosBanner.setFont(F1Theme.FONT_BOLD);
        lblUserPosBanner.setForeground(F1Theme.ACCENT_GREEN);

        bannerContainer.add(lblPoleBanner);
        bannerContainer.add(lblUserPosBanner);

        topContainer.add(bannerContainer, BorderLayout.SOUTH);
        add(topContainer, BorderLayout.NORTH);

        // Tabla Central de Resultados de Clasificación
        String[] columnas = {"POS", "Piloto", "Escudería", "Auto", "Tiempo de Vuelta", "Diferencia Líder", "Vel. Media", "Desgaste"};
        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        tablaResultados = new JTable(tableModel);
        F1Theme.estilizarTabla(tablaResultados);

        // Custom Renderer para colorear la Pole Position en Amarillo/Dorado y el auto del usuario en Verde
        tablaResultados.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    if (row == 0) {
                        c.setBackground(new Color(50, 45, 10)); // Dorado tenue
                        c.setForeground(F1Theme.ACCENT_GOLD);
                        setFont(F1Theme.FONT_BOLD);
                    } else if (tableModel.getValueAt(row, 1) != null &&
                            cbPilotos.getSelectedItem() != null &&
                            tableModel.getValueAt(row, 1).toString().contains(cbPilotos.getSelectedItem().toString())) {
                        c.setBackground(new Color(15, 45, 25)); // Verde tenue para tu piloto
                        c.setForeground(F1Theme.ACCENT_GREEN);
                        setFont(F1Theme.FONT_BOLD);
                    } else {
                        c.setBackground(F1Theme.BG_CARD);
                        c.setForeground(F1Theme.TEXT_PRIMARY);
                        setFont(F1Theme.FONT_REGULAR);
                    }
                }
                return c;
            }
        });

        JScrollPane scrollTabla = new JScrollPane(tablaResultados);
        scrollTabla.getViewport().setBackground(F1Theme.BG_CARD);
        scrollTabla.setBorder(BorderFactory.createLineBorder(F1Theme.BORDER_COLOR));
        add(scrollTabla, BorderLayout.CENTER);

        // Barra de progreso inferior
        progressBar = new JProgressBar();
        progressBar.setVisible(false);
        progressBar.setStringPainted(true);
        progressBar.setForeground(F1Theme.RED_PRIMARY);
        progressBar.setBackground(F1Theme.BG_SIDEBAR);
        add(progressBar, BorderLayout.SOUTH);
    }

    private JPanel crearItemControl(String label, JComboBox<?> combo) {
        JPanel p = new JPanel(new BorderLayout(3, 3));
        p.setOpaque(false);
        JLabel l = new JLabel(label);
        l.setFont(F1Theme.FONT_SMALL);
        l.setForeground(F1Theme.TEXT_MUTED);
        p.add(l, BorderLayout.NORTH);

        combo.setBackground(F1Theme.BG_SIDEBAR);
        combo.setForeground(F1Theme.TEXT_PRIMARY);
        p.add(combo, BorderLayout.CENTER);
        return p;
    }

    public void poblarCombos() {
        cbCircuitos.removeAllItems();
        for (Circuito c : circuitoService.listarTodos()) {
            cbCircuitos.addItem(c.getNombre());
        }

        cbPilotos.removeAllItems();
        for (Piloto p : pilotoService.listarTodos()) {
            cbPilotos.addItem(p.getNombre());
        }

        cbVehiculos.removeAllItems();
        for (Vehiculo v : vehiculoService.listarTodos()) {
            cbVehiculos.addItem(v.getModelo());
        }
    }

    private void ejecutarSimulacionAnimada() {
        if (cbCircuitos.getSelectedItem() == null || cbPilotos.getSelectedItem() == null || cbVehiculos.getSelectedItem() == null) return;

        Circuito circuito = circuitoService.buscarPorNombre(cbCircuitos.getSelectedItem().toString()).orElse(null);
        Piloto piloto = pilotoService.buscarPorNombre(cbPilotos.getSelectedItem().toString()).stream().findFirst().orElse(null);
        Vehiculo vehiculo = vehiculoService.buscarPorModelo(cbVehiculos.getSelectedItem().toString()).orElse(null);
        ConfiguracionVehiculo config = configuracionService.getConfiguracionActual();

        if (circuito == null || piloto == null || vehiculo == null) return;

        Clima clima;
        int selClima = cbClima.getSelectedIndex();
        if (selClima == 1) clima = Clima.SECO;
        else if (selClima == 2) clima = Clima.LLUVIOSO;
        else if (selClima == 3) clima = Clima.EXTREMO;
        else clima = simulacionService.generarClimaAleatorio();

        btnSimular.setEnabled(false);
        progressBar.setVisible(true);
        progressBar.setValue(0);
        progressBar.setString("🟢 Bandera Verde: Monoplazas en vuelta de calentamiento...");

        // Timer animado en hilo Swing Worker
        SwingWorker<SesionClasificacion, Integer> worker = new SwingWorker<>() {
            @Override
            protected SesionClasificacion doInBackground() throws Exception {
                for (int i = 1; i <= 100; i += 10) {
                    Thread.sleep(40);
                    publish(i);
                }
                return simulacionService.simularClasificacion(circuito, clima, piloto, vehiculo, config);
            }

            @Override
            protected void process(List<Integer> chunks) {
                int val = chunks.get(chunks.size() - 1);
                progressBar.setValue(val);
                if (val < 50) {
                    progressBar.setString("🏎️ Sector 1 & 2: Marcando récords en sectores intermedios (" + val + "%)...");
                } else {
                    progressBar.setString("🏁 Sector 3: Recta principal y bandera a cuadros (" + val + "%)...");
                }
            }

            @Override
            protected void done() {
                try {
                    SesionClasificacion sesion = get();
                    mostrarResultados(sesion);
                } catch (Exception ignored) {}
                progressBar.setVisible(false);
                btnSimular.setEnabled(true);
            }
        };
        worker.execute();
    }

    private void mostrarResultados(SesionClasificacion sesion) {
        tableModel.setRowCount(0);
        for (ResultadoVuelta r : sesion.getResultados()) {
            String delta = (r.getPosicion() == 1) ? "POLE" : String.format("+%6.3f s", r.getDiferenciaConLiderSegundos());
            tableModel.addRow(new Object[]{
                    "P" + r.getPosicion(),
                    r.getPiloto().getNombre(),
                    r.getPiloto().getEquipo(),
                    (r.getVehiculo() != null ? r.getVehiculo().getModelo() : "F1"),
                    r.getTiempoFormateado(),
                    delta,
                    String.format("%.1f km/h", r.getVelocidadMediaKmh()),
                    String.format("%.1f %%", r.getDesgasteNeumaticosEstimado())
            });
        }

        if (sesion.getPolePosition() != null && !sesion.getResultados().isEmpty()) {
            lblPoleBanner.setText("🏆 POLE POSITION: " + sesion.getPolePosition().getNombre() +
                    " (" + sesion.getPolePosition().getEquipo() + ") - Tiempo: " + sesion.getResultados().get(0).getTiempoFormateado() +
                    " | Clima: " + sesion.getClima().getNombre());
        }

        ResultadoVuelta user = sesion.getResultadoUsuario();
        if (user != null) {
            lblUserPosBanner.setText("🎯 Tu Resultado: Posición P" + user.getPosicion() + " con tiempo " + user.getTiempoFormateado() +
                    " (Dif: +" + String.format("%.3fs", user.getDiferenciaConLiderSegundos()) + ")");
        }
    }
}
