package com.formula1.gui;

import com.formula1.model.Piloto;
import com.formula1.service.PilotoService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PilotosPanel extends JPanel {
    private final PilotoService pilotoService;
    private JTable tablaPilotos;
    private DefaultTableModel tableModel;
    private JTextField txtBuscar;

    public PilotosPanel(PilotoService pilotoService) {
        this.pilotoService = pilotoService;
        setLayout(new BorderLayout(15, 15));
        setBackground(F1Theme.BG_DARK);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        initUI();
        cargarDatos(pilotoService.listarTodos());
    }

    private void initUI() {
        // Encabezado
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel lblTitulo = new JLabel("🏎️ Gestión de Pilotos Oficiales FIA");
        lblTitulo.setFont(F1Theme.FONT_TITLE);
        lblTitulo.setForeground(F1Theme.TEXT_PRIMARY);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        searchPanel.setOpaque(false);

        txtBuscar = new JTextField(15);
        txtBuscar.setBackground(F1Theme.BG_CARD);
        txtBuscar.setForeground(F1Theme.TEXT_PRIMARY);
        txtBuscar.setCaretColor(F1Theme.TEXT_PRIMARY);
        txtBuscar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(F1Theme.BORDER_COLOR),
                new EmptyBorder(6, 10, 6, 10)
        ));

        JButton btnBuscar = F1Theme.crearBotonSecundario("🔍 Buscar");
        btnBuscar.addActionListener(e -> {
            String q = txtBuscar.getText().trim();
            if (q.isEmpty()) {
                cargarDatos(pilotoService.listarTodos());
            } else {
                cargarDatos(pilotoService.buscarPorNombre(q));
            }
        });

        searchPanel.add(new JLabel("Filtrar:"));
        searchPanel.getComponent(searchPanel.getComponentCount() - 1).setForeground(F1Theme.TEXT_MUTED);
        searchPanel.add(txtBuscar);
        searchPanel.add(btnBuscar);

        headerPanel.add(lblTitulo, BorderLayout.WEST);
        headerPanel.add(searchPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // Tabla de Pilotos
        String[] columnas = {"ID", "Nombre del Piloto", "Escudería", "Rol", "Habilidad", "Experiencia"};
        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaPilotos = new JTable(tableModel);
        F1Theme.estilizarTabla(tablaPilotos);
        JScrollPane scrollPane = new JScrollPane(tablaPilotos);
        scrollPane.getViewport().setBackground(F1Theme.BG_CARD);
        scrollPane.setBorder(BorderFactory.createLineBorder(F1Theme.BORDER_COLOR));
        add(scrollPane, BorderLayout.CENTER);

        // Barra de botones CRUD
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        actionsPanel.setOpaque(false);

        JButton btnNuevo = F1Theme.crearBotonPrimario("Nuevo Piloto", "➕");
        btnNuevo.addActionListener(e -> abrirModalNuevoPiloto());

        JButton btnEditar = F1Theme.crearBotonSecundario("✏️ Editar Seleccionado");
        btnEditar.addActionListener(e -> editarPilotoSeleccionado());

        JButton btnEliminar = F1Theme.crearBotonSecundario("🗑️ Eliminar");
        btnEliminar.addActionListener(e -> eliminarPilotoSeleccionado());

        JButton btnRefrescar = F1Theme.crearBotonSecundario("🔄 Recargar");
        btnRefrescar.addActionListener(e -> {
            txtBuscar.setText("");
            cargarDatos(pilotoService.listarTodos());
        });

        actionsPanel.add(btnNuevo);
        actionsPanel.add(btnEditar);
        actionsPanel.add(btnEliminar);
        actionsPanel.add(btnRefrescar);

        add(actionsPanel, BorderLayout.SOUTH);
    }

    public void cargarDatos(List<Piloto> pilotos) {
        tableModel.setRowCount(0);
        for (Piloto p : pilotos) {
            tableModel.addRow(new Object[]{
                    "#" + String.format("%02d", p.getId()),
                    p.getNombre(),
                    p.getEquipo(),
                    p.getRol(),
                    p.getHabilidad() + " %",
                    p.getExperiencia() + " %"
            });
        }
    }

    private void abrirModalNuevoPiloto() {
        JTextField txtNombre = new JTextField();
        JTextField txtEquipo = new JTextField();
        JComboBox<String> cbRol = new JComboBox<>(new String[]{"Líder", "Escudero"});
        JSpinner spHab = new JSpinner(new SpinnerNumberModel(90, 50, 100, 1));
        JSpinner spExp = new JSpinner(new SpinnerNumberModel(88, 50, 100, 1));

        Object[] formulario = {
                "Nombre:", txtNombre,
                "Escudería:", txtEquipo,
                "Rol en el Equipo:", cbRol,
                "Nivel de Habilidad (50-100%):", spHab,
                "Nivel de Experiencia (50-100%):", spExp
        };

        int res = JOptionPane.showConfirmDialog(this, formulario, "Registrar Nuevo Piloto", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            String nombre = txtNombre.getText().trim();
            String equipo = txtEquipo.getText().trim();
            if (nombre.isEmpty() || equipo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe completar nombre y escudería.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            pilotoService.agregarPiloto(nombre, equipo, (String) cbRol.getSelectedItem(), (int) spExp.getValue(), (int) spHab.getValue());
            cargarDatos(pilotoService.listarTodos());
            JOptionPane.showMessageDialog(this, "Piloto agregado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void editarPilotoSeleccionado() {
        int row = tablaPilotos.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un piloto de la tabla para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idStr = tableModel.getValueAt(row, 0).toString().replace("#", "").trim();
        int id = Integer.parseInt(idStr);
        Piloto p = pilotoService.buscarPorId(id).orElse(null);
        if (p == null) return;

        JTextField txtNombre = new JTextField(p.getNombre());
        JTextField txtEquipo = new JTextField(p.getEquipo());
        JComboBox<String> cbRol = new JComboBox<>(new String[]{"Líder", "Escudero"});
        cbRol.setSelectedItem(p.getRol());
        JSpinner spHab = new JSpinner(new SpinnerNumberModel(p.getHabilidad(), 50, 100, 1));
        JSpinner spExp = new JSpinner(new SpinnerNumberModel(p.getExperiencia(), 50, 100, 1));

        Object[] formulario = {
                "Nombre:", txtNombre,
                "Escudería:", txtEquipo,
                "Rol:", cbRol,
                "Habilidad (50-100%):", spHab,
                "Experiencia (50-100%):", spExp
        };

        int res = JOptionPane.showConfirmDialog(this, formulario, "Modificar Piloto #" + id, JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            pilotoService.actualizarPiloto(id, txtNombre.getText().trim(), txtEquipo.getText().trim(),
                    (String) cbRol.getSelectedItem(), (int) spExp.getValue(), (int) spHab.getValue());
            cargarDatos(pilotoService.listarTodos());
        }
    }

    private void eliminarPilotoSeleccionado() {
        int row = tablaPilotos.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un piloto de la tabla para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idStr = tableModel.getValueAt(row, 0).toString().replace("#", "").trim();
        int id = Integer.parseInt(idStr);
        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar al piloto #" + id + "?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            pilotoService.eliminarPiloto(id);
            cargarDatos(pilotoService.listarTodos());
        }
    }
}
