package com.formula1.gui;

import com.formula1.model.Equipo;
import com.formula1.service.EquipoService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EquiposPanel extends JPanel {
    private final EquipoService equipoService;
    private JTable tablaEquipos;
    private DefaultTableModel tableModel;

    public EquiposPanel(EquipoService equipoService) {
        this.equipoService = equipoService;
        setLayout(new BorderLayout(15, 15));
        setBackground(F1Theme.BG_DARK);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        initUI();
        cargarDatos(equipoService.listarTodos());
    }

    private void initUI() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel lblTitulo = new JLabel("ESCUDERÍAS Y PROVEEDORES DE MOTOR");
        lblTitulo.setFont(F1Theme.FONT_TITLE);
        lblTitulo.setForeground(Color.WHITE);
        headerPanel.add(lblTitulo, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        String[] columnas = {"Nombre de Escudería", "País", "Unidad de Potencia (Motor)", "Pilotos Asignados (IDs)"};
        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        tablaEquipos = new JTable(tableModel);
        F1Theme.estilizarTabla(tablaEquipos);
        JScrollPane scrollPane = new JScrollPane(tablaEquipos);
        scrollPane.getViewport().setBackground(F1Theme.BG_CARD);
        scrollPane.setBorder(BorderFactory.createLineBorder(F1Theme.BORDER_COLOR));
        add(scrollPane, BorderLayout.CENTER);

        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        actionsPanel.setOpaque(false);

        JButton btnNuevo = F1Theme.crearBotonPrimario("NUEVA ESCUDERÍA", "+");
        btnNuevo.addActionListener(e -> agregarEquipoModal());

        JButton btnAsignar = F1Theme.crearBotonSecundario("ASIGNAR PILOTO");
        btnAsignar.addActionListener(e -> asignarPilotoModal());

        JButton btnEliminar = F1Theme.crearBotonSecundario("ELIMINAR ESCUDERÍA");
        btnEliminar.addActionListener(e -> eliminarEquipoSeleccionado());

        actionsPanel.add(btnNuevo);
        actionsPanel.add(btnAsignar);
        actionsPanel.add(btnEliminar);
        add(actionsPanel, BorderLayout.SOUTH);
    }

    public void cargarDatos(List<Equipo> equipos) {
        tableModel.setRowCount(0);
        for (Equipo eq : equipos) {
            tableModel.addRow(new Object[]{
                    eq.getNombre(),
                    eq.getPais(),
                    eq.getMotor(),
                    eq.getPilotosIds().toString()
            });
        }
    }

    private void agregarEquipoModal() {
        JTextField txtNombre = new JTextField();
        JTextField txtPais = new JTextField();
        JTextField txtMotor = new JTextField();

        Object[] form = {
                "Nombre Escudería:", txtNombre,
                "País:", txtPais,
                "Motor:", txtMotor
        };

        int res = JOptionPane.showConfirmDialog(this, form, "Registrar Escudería", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            String n = txtNombre.getText().trim();
            if (n.isEmpty()) return;
            equipoService.agregarEquipo(n, txtPais.getText().trim(), txtMotor.getText().trim(), "");
            cargarDatos(equipoService.listarTodos());
        }
    }

    private void asignarPilotoModal() {
        int row = tablaEquipos.getSelectedRow();
        String equipoDefault = (row != -1) ? tableModel.getValueAt(row, 0).toString() : "Red Bull Racing";

        JTextField txtPilotoId = new JTextField();
        JTextField txtEquipo = new JTextField(equipoDefault);

        Object[] form = {
                "ID del Piloto (1-20):", txtPilotoId,
                "Nombre del Equipo:", txtEquipo
        };

        int res = JOptionPane.showConfirmDialog(this, form, "Asignar Piloto", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            try {
                int pId = Integer.parseInt(txtPilotoId.getText().trim());
                boolean exito = equipoService.asignarPilotoAEquipo(pId, txtEquipo.getText().trim());
                if (exito) {
                    cargarDatos(equipoService.listarTodos());
                    JOptionPane.showMessageDialog(this, "Piloto asignado correctamente.");
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo asignar. Verifique los datos.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "ID inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void eliminarEquipoSeleccionado() {
        int row = tablaEquipos.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un equipo para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nombre = tableModel.getValueAt(row, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar " + nombre + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            equipoService.eliminarEquipo(nombre);
            cargarDatos(equipoService.listarTodos());
        }
    }
}
