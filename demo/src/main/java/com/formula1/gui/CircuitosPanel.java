package com.formula1.gui;

import com.formula1.data.DataStore;
import com.formula1.model.Circuito;
import com.formula1.model.GanadorHistorico;
import com.formula1.model.Piloto;
import com.formula1.service.CircuitoService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CircuitosPanel extends JPanel {
    private final CircuitoService circuitoService;
    private JTable tablaCircuitos;
    private DefaultTableModel tableModel;
    private JTextArea txtDetalle;

    public CircuitosPanel(CircuitoService circuitoService) {
        this.circuitoService = circuitoService;
        setLayout(new BorderLayout(15, 15));
        setBackground(F1Theme.BG_DARK);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        initUI();
        cargarDatos(circuitoService.listarTodos());
    }

    private void initUI() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel lblTitulo = new JLabel("🗺️ Circuitos Oficiales del Campeonato");
        lblTitulo.setFont(F1Theme.FONT_TITLE);
        lblTitulo.setForeground(F1Theme.TEXT_PRIMARY);
        headerPanel.add(lblTitulo, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        // Centro dividido: Tabla a la izquierda, detalles / telemetría de pista a la derecha
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setOpaque(false);
        splitPane.setBorder(null);
        splitPane.setDividerLocation(540);

        String[] columnas = {"Nombre del Gran Premio", "País", "Longitud", "Vueltas", "Clima Típico"};
        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        tablaCircuitos = new JTable(tableModel);
        F1Theme.estilizarTabla(tablaCircuitos);
        tablaCircuitos.getSelectionModel().addListSelectionListener(e -> actualizarDetalle());

        JScrollPane scrollTabla = new JScrollPane(tablaCircuitos);
        scrollTabla.getViewport().setBackground(F1Theme.BG_CARD);
        scrollTabla.setBorder(BorderFactory.createLineBorder(F1Theme.BORDER_COLOR));
        splitPane.setLeftComponent(scrollTabla);

        // Panel de Detalle Derecho
        JPanel panelDetalle = F1Theme.crearTarjeta();
        panelDetalle.setLayout(new BorderLayout(10, 10));

        JLabel lblInfo = new JLabel("📊 Ficha Técnica y Ganadores");
        lblInfo.setFont(F1Theme.FONT_SUBTITLE);
        lblInfo.setForeground(F1Theme.RED_PRIMARY);
        panelDetalle.add(lblInfo, BorderLayout.NORTH);

        txtDetalle = new JTextArea();
        txtDetalle.setBackground(F1Theme.BG_SIDEBAR);
        txtDetalle.setForeground(Color.WHITE);
        txtDetalle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        txtDetalle.setEditable(false);
        txtDetalle.setLineWrap(true);
        txtDetalle.setWrapStyleWord(true);
        txtDetalle.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane scrollDetalle = new JScrollPane(txtDetalle);
        scrollDetalle.setBorder(BorderFactory.createLineBorder(F1Theme.BORDER_COLOR));
        panelDetalle.add(scrollDetalle, BorderLayout.CENTER);

        splitPane.setRightComponent(panelDetalle);
        add(splitPane, BorderLayout.CENTER);

        // Botones inferiores
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        actionsPanel.setOpaque(false);

        JButton btnNuevo = F1Theme.crearBotonPrimario("Agregar Circuito", "➕");
        btnNuevo.addActionListener(e -> agregarCircuitoModal());

        JButton btnEliminar = F1Theme.crearBotonSecundario("🗑️ Eliminar Circuito");
        btnEliminar.addActionListener(e -> eliminarCircuitoModal());

        actionsPanel.add(btnNuevo);
        actionsPanel.add(btnEliminar);
        add(actionsPanel, BorderLayout.SOUTH);
    }

    public void cargarDatos(List<Circuito> circuitos) {
        tableModel.setRowCount(0);
        for (Circuito c : circuitos) {
            tableModel.addRow(new Object[]{
                    c.getNombre(),
                    c.getPais(),
                    c.getLongitudKm() + " km",
                    c.getVueltas() + " vtas",
                    c.getClimaHabitual()
            });
        }
        if (tablaCircuitos.getRowCount() > 0) {
            tablaCircuitos.setRowSelectionInterval(0, 0);
            actualizarDetalle();
        }
    }

    private void actualizarDetalle() {
        int row = tablaCircuitos.getSelectedRow();
        if (row == -1) {
            txtDetalle.setText("Seleccione un circuito para ver sus características y estadísticas.");
            return;
        }

        String nombre = tableModel.getValueAt(row, 0).toString();
        Circuito c = circuitoService.buscarPorNombre(nombre).orElse(null);
        if (c == null) return;

        StringBuilder sb = new StringBuilder();
        sb.append("🏁 ").append(c.getNombre().toUpperCase()).append("\n");
        sb.append("📍 País: ").append(c.getPais()).append("\n");
        sb.append("📏 Longitud: ").append(c.getLongitudKm()).append(" km (").append(c.getVueltas()).append(" vueltas de GP)\n");
        sb.append("🌦️ Clima típico: ").append(c.getClimaHabitual()).append("\n\n");
        sb.append("📝 DESCRIPCIÓN:\n").append(c.getDescripcion()).append("\n\n");

        if (c.getRecordVuelta() != null) {
            sb.append("⏱️ RÉCORD OFICIAL DE VUELTA:\n");
            sb.append("   ").append(c.getRecordVuelta().getTiempo())
              .append(" - ").append(c.getRecordVuelta().getPiloto())
              .append(" (").append(c.getRecordVuelta().getAnio()).append(")\n\n");
        }

        sb.append("📊 IMPACTO DE TRAZADO EN EL MONOPLAZA:\n");
        sb.append("   • Desgaste de Neumáticos: x").append(String.format("%.2f", c.getFactorDesgasteNeumaticos())).append("\n");
        sb.append("   • Consumo de Combustible: x").append(String.format("%.2f", c.getFactorConsumoCombustible())).append("\n\n");

        sb.append("🏆 HISTORIAL DE GANADORES:\n");
        if (c.getGanadores().isEmpty()) {
            sb.append("   Sin registros de ganadores previos.\n");
        } else {
            for (GanadorHistorico g : c.getGanadores()) {
                Piloto p = DataStore.getInstance().getPilotos().get(g.getPilotoId());
                String pNombre = (p != null) ? p.getNombre() + " (" + p.getEquipo() + ")" : "Piloto #" + g.getPilotoId();
                sb.append("   • Temp. ").append(g.getTemporada()).append(": ").append(pNombre).append("\n");
            }
        }

        txtDetalle.setText(sb.toString());
        txtDetalle.setCaretPosition(0);
    }

    private void agregarCircuitoModal() {
        JTextField txtNombre = new JTextField();
        JTextField txtPais = new JTextField();
        JTextField txtLong = new JTextField("5.0");
        JTextField txtVueltas = new JTextField("55");
        JTextField txtDesc = new JTextField();

        Object[] form = {
                "Nombre del Circuito:", txtNombre,
                "País:", txtPais,
                "Longitud en km:", txtLong,
                "Vueltas:", txtVueltas,
                "Descripción:", txtDesc
        };

        int res = JOptionPane.showConfirmDialog(this, form, "Nuevo Circuito", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            try {
                String n = txtNombre.getText().trim();
                if (n.isEmpty()) return;
                double l = Double.parseDouble(txtLong.getText().trim());
                int v = Integer.parseInt(txtVueltas.getText().trim());
                circuitoService.agregarCircuito(n, txtPais.getText().trim(), l, v, txtDesc.getText().trim(), "1:25.000", "Recordman", 2024, "");
                cargarDatos(circuitoService.listarTodos());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Datos numéricos inválidos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void eliminarCircuitoModal() {
        int row = tablaCircuitos.getSelectedRow();
        if (row == -1) return;
        String nombre = tableModel.getValueAt(row, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar circuito " + nombre + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            circuitoService.eliminarCircuito(nombre);
            cargarDatos(circuitoService.listarTodos());
        }
    }
}
