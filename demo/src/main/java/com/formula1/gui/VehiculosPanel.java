package com.formula1.gui;

import com.formula1.model.*;
import com.formula1.service.VehiculoService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VehiculosPanel extends JPanel {
    private final VehiculoService vehiculoService;
    private JTable tablaVehiculos;
    private DefaultTableModel tableModel;

    // Comparador visual
    private JComboBox<String> cbAuto1;
    private JComboBox<String> cbAuto2;
    private JTextArea txtComparacion;

    public VehiculosPanel(VehiculoService vehiculoService) {
        this.vehiculoService = vehiculoService;
        setLayout(new BorderLayout(15, 15));
        setBackground(F1Theme.BG_DARK);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        initUI();
        cargarDatos(vehiculoService.listarTodos());
    }

    private void initUI() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel lblTitulo = new JLabel("🚗 Monoplazas y Telemetría de Rendimiento");
        lblTitulo.setFont(F1Theme.FONT_TITLE);
        lblTitulo.setForeground(F1Theme.TEXT_PRIMARY);
        headerPanel.add(lblTitulo, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        // Panel Principal Dividido: Arriba Tabla, Abajo Comparador Visual
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setOpaque(false);
        splitPane.setBorder(null);
        splitPane.setDividerLocation(260);

        // Tabla superior
        String[] columnas = {"Modelo", "Escudería", "Motor", "Velocidad Máxima", "Aceleración 0-100 km/h", "Pilotos Asignados"};
        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        tablaVehiculos = new JTable(tableModel);
        F1Theme.estilizarTabla(tablaVehiculos);

        JScrollPane scrollTabla = new JScrollPane(tablaVehiculos);
        scrollTabla.getViewport().setBackground(F1Theme.BG_CARD);
        scrollTabla.setBorder(BorderFactory.createLineBorder(F1Theme.BORDER_COLOR));
        splitPane.setTopComponent(scrollTabla);

        // Comparador Visual Inferior
        JPanel panelComparador = F1Theme.crearTarjeta();
        panelComparador.setLayout(new BorderLayout(10, 10));

        JPanel compHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        compHeader.setOpaque(false);

        JLabel lblCompTitle = new JLabel("⚖️ Comparador Directo de Rendimiento:");
        lblCompTitle.setFont(F1Theme.FONT_SUBTITLE);
        lblCompTitle.setForeground(F1Theme.RED_PRIMARY);

        cbAuto1 = new JComboBox<>();
        cbAuto2 = new JComboBox<>();
        cbAuto1.setBackground(F1Theme.BG_SIDEBAR);
        cbAuto1.setForeground(F1Theme.TEXT_PRIMARY);
        cbAuto2.setBackground(F1Theme.BG_SIDEBAR);
        cbAuto2.setForeground(F1Theme.TEXT_PRIMARY);

        cbAuto1.addActionListener(e -> actualizarComparacion());
        cbAuto2.addActionListener(e -> actualizarComparacion());

        JButton btnComparar = F1Theme.crearBotonPrimario("Comparar", "⚡");
        btnComparar.addActionListener(e -> actualizarComparacion());

        compHeader.add(lblCompTitle);
        compHeader.add(new JLabel("Auto 1:"));
        compHeader.getComponent(compHeader.getComponentCount() - 1).setForeground(F1Theme.TEXT_MUTED);
        compHeader.add(cbAuto1);
        compHeader.add(new JLabel("Auto 2:"));
        compHeader.getComponent(compHeader.getComponentCount() - 1).setForeground(F1Theme.TEXT_MUTED);
        compHeader.add(cbAuto2);
        compHeader.add(btnComparar);

        panelComparador.add(compHeader, BorderLayout.NORTH);

        txtComparacion = new JTextArea();
        txtComparacion.setBackground(F1Theme.BG_SIDEBAR);
        txtComparacion.setForeground(F1Theme.TEXT_PRIMARY);
        txtComparacion.setFont(F1Theme.FONT_TIMER);
        txtComparacion.setEditable(false);
        txtComparacion.setBorder(new EmptyBorder(10, 15, 10, 15));

        JScrollPane scrollComp = new JScrollPane(txtComparacion);
        scrollComp.setBorder(BorderFactory.createLineBorder(F1Theme.BORDER_COLOR));
        panelComparador.add(scrollComp, BorderLayout.CENTER);

        splitPane.setBottomComponent(panelComparador);
        add(splitPane, BorderLayout.CENTER);

        // Barra inferior
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        actionsPanel.setOpaque(false);

        JButton btnNuevo = F1Theme.crearBotonPrimario("Nuevo Monoplaza", "➕");
        btnNuevo.addActionListener(e -> agregarVehiculoModal());

        JButton btnEliminar = F1Theme.crearBotonSecundario("🗑️ Eliminar Monoplaza");
        btnEliminar.addActionListener(e -> eliminarVehiculoModal());

        actionsPanel.add(btnNuevo);
        actionsPanel.add(btnEliminar);
        add(actionsPanel, BorderLayout.SOUTH);
    }

    public void cargarDatos(List<Vehiculo> vehiculos) {
        tableModel.setRowCount(0);
        cbAuto1.removeAllItems();
        cbAuto2.removeAllItems();

        for (Vehiculo v : vehiculos) {
            tableModel.addRow(new Object[]{
                    v.getModelo(),
                    v.getEquipo(),
                    v.getMotor(),
                    v.getVelocidadMaximaKmh() + " km/h",
                    v.getAceleracion0a100() + " s",
                    v.getPilotosIds().toString()
            });
            cbAuto1.addItem(v.getModelo());
            cbAuto2.addItem(v.getModelo());
        }

        if (cbAuto2.getItemCount() > 1) {
            cbAuto2.setSelectedIndex(1);
        }
        actualizarComparacion();
    }

    private void actualizarComparacion() {
        if (cbAuto1.getSelectedItem() == null || cbAuto2.getSelectedItem() == null) return;
        String m1 = cbAuto1.getSelectedItem().toString();
        String m2 = cbAuto2.getSelectedItem().toString();

        Vehiculo v1 = vehiculoService.buscarPorModelo(m1).orElse(null);
        Vehiculo v2 = vehiculoService.buscarPorModelo(m2).orElse(null);
        if (v1 == null || v2 == null) return;

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("========================================================================================\n"));
        sb.append(String.format("  TELEMETRÍA COMPARATIVA           | %-25s | %-25s\n", v1.getModelo() + " (" + v1.getEquipo() + ")", v2.getModelo() + " (" + v2.getEquipo() + ")"));
        sb.append(String.format("========================================================================================\n"));
        sb.append(String.format("  Unidad de Potencia (Motor)       | %-25s | %-25s\n", v1.getMotor(), v2.getMotor()));
        sb.append(String.format("  Velocidad Punta Máxima           | %3d km/h                  | %3d km/h\n", v1.getVelocidadMaximaKmh(), v2.getVelocidadMaximaKmh()));
        sb.append(String.format("  Aceleración 0 a 100 km/h         | %4.2f segundos             | %4.2f segundos\n", v1.getAceleracion0a100(), v2.getAceleracion0a100()));

        if (v1.getRendimiento() != null && v2.getRendimiento() != null) {
            sb.append("----------------------------------------------------------------------------------------\n");
            sb.append("  [MODO CONDUCCIÓN NORMAL - CLIMA SECO]\n");
            sb.append(String.format("  Velocidad Media Estimada         | %3.0f km/h                  | %3.0f km/h\n",
                    v1.getRendimiento().getConduccionNormal().getVelocidadPromedioKmh(), v2.getRendimiento().getConduccionNormal().getVelocidadPromedioKmh()));
            sb.append(String.format("  Consumo de Combustible           | %4.2f L / vuelta           | %4.2f L / vuelta\n",
                    v1.getRendimiento().getConduccionNormal().getConsumo(Clima.SECO), v2.getRendimiento().getConduccionNormal().getConsumo(Clima.SECO)));
            sb.append(String.format("  Desgaste de Neumáticos           | %4.2f %% / vuelta          | %4.2f %% / vuelta\n",
                    v1.getRendimiento().getConduccionNormal().getDesgaste(Clima.SECO), v2.getRendimiento().getConduccionNormal().getDesgaste(Clima.SECO)));

            sb.append("----------------------------------------------------------------------------------------\n");
            sb.append("  [MODO AGRESIVO - MÁXIMO ATAQUE]\n");
            sb.append(String.format("  Velocidad Media Estimada         | %3.0f km/h                  | %3.0f km/h\n",
                    v1.getRendimiento().getConduccionAgresiva().getVelocidadPromedioKmh(), v2.getRendimiento().getConduccionAgresiva().getVelocidadPromedioKmh()));
            sb.append(String.format("  Consumo Combustible Agresivo     | %4.2f L / vuelta           | %4.2f L / vuelta\n",
                    v1.getRendimiento().getConduccionAgresiva().getConsumo(Clima.SECO), v2.getRendimiento().getConduccionAgresiva().getConsumo(Clima.SECO)));
            sb.append(String.format("  Desgaste Neumáticos Agresivo     | %4.2f %% / vuelta          | %4.2f %% / vuelta\n",
                    v1.getRendimiento().getConduccionAgresiva().getDesgaste(Clima.SECO), v2.getRendimiento().getConduccionAgresiva().getDesgaste(Clima.SECO)));
        }
        sb.append(String.format("========================================================================================\n"));

        txtComparacion.setText(sb.toString());
        txtComparacion.setCaretPosition(0);
    }

    private void agregarVehiculoModal() {
        JTextField txtMod = new JTextField();
        JTextField txtEq = new JTextField();
        JTextField txtMot = new JTextField();
        JTextField txtVMax = new JTextField("355");
        JTextField txtAcel = new JTextField("2.6");

        Object[] form = {
                "Modelo (ej. RB20, W15):", txtMod,
                "Escudería:", txtEq,
                "Motor:", txtMot,
                "Velocidad Máxima (km/h):", txtVMax,
                "Aceleración 0-100 (segundos):", txtAcel
        };

        int res = JOptionPane.showConfirmDialog(this, form, "Nuevo Monoplaza", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            try {
                String mod = txtMod.getText().trim();
                if (mod.isEmpty()) return;
                int vmax = Integer.parseInt(txtVMax.getText().trim());
                double acel = Double.parseDouble(txtAcel.getText().trim());
                RendimientoVehiculo rend = new RendimientoVehiculo(
                        new RendimientoConduccion(vmax - 40, 2.0, 2.2, 2.5, 1.6, 0.9, 2.6),
                        new RendimientoConduccion(vmax - 20, 2.5, 2.7, 3.1, 2.2, 1.3, 3.6),
                        new RendimientoConduccion(vmax - 60, 1.7, 1.9, 2.2, 1.1, 0.6, 1.9)
                );
                vehiculoService.agregarVehiculo(txtEq.getText().trim(), mod, txtMot.getText().trim(), vmax, acel, rend, "");
                cargarDatos(vehiculoService.listarTodos());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Datos numéricos inválidos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void eliminarVehiculoModal() {
        int row = tablaVehiculos.getSelectedRow();
        if (row == -1) return;
        String mod = tableModel.getValueAt(row, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar vehículo " + mod + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            vehiculoService.eliminarVehiculo(mod);
            cargarDatos(vehiculoService.listarTodos());
        }
    }
}
