package com.formula1.gui;

import com.formula1.model.*;
import com.formula1.service.ConfiguracionService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Map;

public class ConfiguracionPanel extends JPanel {
    private final ConfiguracionService configuracionService;

    private JComboBox<ModoConduccion> cbModo;
    private JComboBox<CargaAerodinamica> cbAero;
    private JComboBox<PresionNeumaticos> cbPresion;
    private JComboBox<EstrategiaCombustible> cbCombustible;

    private JLabel lblDeltaTiempo;
    private JLabel lblDeltaConsumo;
    private JLabel lblDeltaDesgaste;
    private JComboBox<String> cbPresets;

    public ConfiguracionPanel(ConfiguracionService configuracionService) {
        this.configuracionService = configuracionService;
        setLayout(new BorderLayout(20, 20));
        setBackground(F1Theme.BG_DARK);
        setBorder(new EmptyBorder(25, 30, 25, 30));

        initUI();
        actualizarTelemetria();
    }

    private void initUI() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel lblTitulo = new JLabel("⚙️ Reglajes de Ingeniería y Configuración del Monoplaza");
        lblTitulo.setFont(F1Theme.FONT_TITLE);
        lblTitulo.setForeground(F1Theme.TEXT_PRIMARY);
        headerPanel.add(lblTitulo, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        // Panel central con 2 columnas: Izquierda Reglajes, Derecha Telemetría en Vivo
        JPanel mainGrid = new JPanel(new GridLayout(1, 2, 25, 0));
        mainGrid.setOpaque(false);

        // --- COLUMNA 1: CONTROLES DE REGLAJE ---
        JPanel panelReglajes = F1Theme.crearTarjeta();
        panelReglajes.setLayout(new GridLayout(5, 1, 10, 15));

        JLabel lblSec1 = new JLabel("🔧 PARÁMETROS DE CONFIGURACIÓN");
        lblSec1.setFont(F1Theme.FONT_SUBTITLE);
        lblSec1.setForeground(F1Theme.RED_PRIMARY);
        panelReglajes.add(lblSec1);

        // 1. Modo
        JPanel rowModo = crearFilaControl("Modo de Conducción:", cbModo = new JComboBox<>(ModoConduccion.values()));
        cbModo.setSelectedItem(configuracionService.getConfiguracionActual().getModoConduccion());
        cbModo.addActionListener(e -> {
            configuracionService.getConfiguracionActual().setModoConduccion((ModoConduccion) cbModo.getSelectedItem());
            actualizarTelemetria();
        });
        panelReglajes.add(rowModo);

        // 2. Carga Aero
        JPanel rowAero = crearFilaControl("Carga Aerodinámica (Alerones):", cbAero = new JComboBox<>(CargaAerodinamica.values()));
        cbAero.setSelectedItem(configuracionService.getConfiguracionActual().getCargaAerodinamica());
        cbAero.addActionListener(e -> {
            configuracionService.getConfiguracionActual().setCargaAerodinamica((CargaAerodinamica) cbAero.getSelectedItem());
            actualizarTelemetria();
        });
        panelReglajes.add(rowAero);

        // 3. Presión Neumáticos
        JPanel rowPresion = crearFilaControl("Presión de Neumáticos:", cbPresion = new JComboBox<>(PresionNeumaticos.values()));
        cbPresion.setSelectedItem(configuracionService.getConfiguracionActual().getPresionNeumaticos());
        cbPresion.addActionListener(e -> {
            configuracionService.getConfiguracionActual().setPresionNeumaticos((PresionNeumaticos) cbPresion.getSelectedItem());
            actualizarTelemetria();
        });
        panelReglajes.add(rowPresion);

        // 4. Combustible
        JPanel rowComb = crearFilaControl("Estrategia de Combustible / Motor:", cbCombustible = new JComboBox<>(EstrategiaCombustible.values()));
        cbCombustible.setSelectedItem(configuracionService.getConfiguracionActual().getEstrategiaCombustible());
        cbCombustible.addActionListener(e -> {
            configuracionService.getConfiguracionActual().setEstrategiaCombustible((EstrategiaCombustible) cbCombustible.getSelectedItem());
            actualizarTelemetria();
        });
        panelReglajes.add(rowComb);

        mainGrid.add(panelReglajes);

        // --- COLUMNA 2: TELEMETRÍA EN VIVO Y PRESETS ---
        JPanel panelTelemetria = F1Theme.crearTarjeta();
        panelTelemetria.setLayout(new BorderLayout(15, 15));

        JLabel lblSec2 = new JLabel("📊 TELEMETRÍA ESTIMADA EN PISTA");
        lblSec2.setFont(F1Theme.FONT_SUBTITLE);
        lblSec2.setForeground(F1Theme.ACCENT_GREEN);
        panelTelemetria.add(lblSec2, BorderLayout.NORTH);

        JPanel metricsPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        metricsPanel.setOpaque(false);

        lblDeltaTiempo = new JLabel("Impacto en Tiempo: +0.00s");
        lblDeltaTiempo.setFont(F1Theme.FONT_SUBTITLE);
        lblDeltaTiempo.setForeground(F1Theme.ACCENT_GOLD);

        lblDeltaConsumo = new JLabel("Multiplicador Consumo: x1.00");
        lblDeltaConsumo.setFont(F1Theme.FONT_SUBTITLE);
        lblDeltaConsumo.setForeground(F1Theme.TEXT_PRIMARY);

        lblDeltaDesgaste = new JLabel("Multiplicador Desgaste: x1.00");
        lblDeltaDesgaste.setFont(F1Theme.FONT_SUBTITLE);
        lblDeltaDesgaste.setForeground(F1Theme.TEXT_PRIMARY);

        metricsPanel.add(lblDeltaTiempo);
        metricsPanel.add(lblDeltaConsumo);
        metricsPanel.add(lblDeltaDesgaste);
        panelTelemetria.add(metricsPanel, BorderLayout.CENTER);

        // Guardar y Cargar Presets
        JPanel presetsPanel = new JPanel(new BorderLayout(10, 10));
        presetsPanel.setOpaque(false);
        presetsPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(F1Theme.BORDER_COLOR),
                "💾 Presets Guardados", 0, 0, F1Theme.FONT_BOLD, F1Theme.TEXT_MUTED
        ));

        cbPresets = new JComboBox<>();
        actualizarListaPresets();

        JPanel btnPresets = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        btnPresets.setOpaque(false);

        JButton btnGuardar = F1Theme.crearBotonPrimario("Guardar Actual", "💾");
        btnGuardar.addActionListener(e -> guardarPresetModal());

        JButton btnCargar = F1Theme.crearBotonSecundario("📂 Cargar");
        btnCargar.addActionListener(e -> cargarPresetSeleccionado());

        btnPresets.add(btnGuardar);
        btnPresets.add(btnCargar);

        presetsPanel.add(cbPresets, BorderLayout.CENTER);
        presetsPanel.add(btnPresets, BorderLayout.SOUTH);

        panelTelemetria.add(presetsPanel, BorderLayout.SOUTH);
        mainGrid.add(panelTelemetria);

        add(mainGrid, BorderLayout.CENTER);
    }

    private JPanel crearFilaControl(String labelText, JComboBox<?> combo) {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        p.setOpaque(false);
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(F1Theme.FONT_BOLD);
        lbl.setForeground(F1Theme.TEXT_PRIMARY);
        p.add(lbl, BorderLayout.NORTH);

        combo.setBackground(F1Theme.BG_SIDEBAR);
        combo.setForeground(F1Theme.TEXT_PRIMARY);
        combo.setFont(F1Theme.FONT_REGULAR);
        p.add(combo, BorderLayout.CENTER);
        return p;
    }

    private void actualizarTelemetria() {
        ConfiguracionVehiculo c = configuracionService.getConfiguracionActual();
        double dt = c.getImpactoTotalTiempoSegundos();
        lblDeltaTiempo.setText(String.format("⏱️ Impacto en Tiempo de Vuelta: %+5.2f s / vta", dt));
        lblDeltaTiempo.setForeground(dt <= 0 ? F1Theme.ACCENT_GREEN : F1Theme.RED_PRIMARY);

        lblDeltaConsumo.setText(String.format("⛽ Factor Consumo Combustible: x%.2f", c.getImpactoTotalConsumo()));
        lblDeltaDesgaste.setText(String.format("🛞 Factor Desgaste Neumáticos: x%.2f", c.getImpactoTotalDesgaste()));
    }

    private void actualizarListaPresets() {
        cbPresets.removeAllItems();
        for (String k : configuracionService.listarConfiguracionesGuardadas().keySet()) {
            cbPresets.addItem(k);
        }
    }

    private void guardarPresetModal() {
        String nombre = JOptionPane.showInputDialog(this, "Nombre para guardar este reglaje (ej. Setup Mónaco Agresivo):", "Guardar Preset", JOptionPane.PLAIN_MESSAGE);
        if (nombre != null && !nombre.trim().isEmpty()) {
            configuracionService.guardarConfiguracion(nombre.trim(), configuracionService.getConfiguracionActual());
            actualizarListaPresets();
            cbPresets.setSelectedItem(nombre.trim());
            JOptionPane.showMessageDialog(this, "Preset guardado con éxito.", "Guardado", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void cargarPresetSeleccionado() {
        if (cbPresets.getSelectedItem() == null) return;
        String nombre = cbPresets.getSelectedItem().toString();
        configuracionService.cargarConfiguracion(nombre).ifPresent(cfg -> {
            configuracionService.setConfiguracionActual(cfg);
            cbModo.setSelectedItem(cfg.getModoConduccion());
            cbAero.setSelectedItem(cfg.getCargaAerodinamica());
            cbPresion.setSelectedItem(cfg.getPresionNeumaticos());
            cbCombustible.setSelectedItem(cfg.getEstrategiaCombustible());
            actualizarTelemetria();
            JOptionPane.showMessageDialog(this, "Reglaje '" + nombre + "' aplicado.", "Cargado", JOptionPane.INFORMATION_MESSAGE);
        });
    }
}
