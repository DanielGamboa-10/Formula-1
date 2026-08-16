package com.formula1.gui;

import com.formula1.model.ResultadoVuelta;
import com.formula1.model.SesionClasificacion;
import com.formula1.service.EstadisticaService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class HistorialPanel extends JPanel {
    private final EstadisticaService estadisticaService;
    private JTable tablaHistorial;
    private DefaultTableModel tableModel;
    private JTextArea txtDetalle;

    public HistorialPanel(EstadisticaService estadisticaService) {
        this.estadisticaService = estadisticaService;
        setLayout(new BorderLayout(15, 15));
        setBackground(F1Theme.BG_DARK);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        initUI();
    }

    private void initUI() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel lblTitulo = new JLabel("📊 Historial de Sesiones de Clasificación y Estadísticas");
        lblTitulo.setFont(F1Theme.FONT_TITLE);
        lblTitulo.setForeground(F1Theme.TEXT_PRIMARY);
        headerPanel.add(lblTitulo, BorderLayout.WEST);

        JButton btnRefrescar = F1Theme.crearBotonSecundario("🔄 Actualizar Historial");
        btnRefrescar.addActionListener(e -> cargarDatos());
        headerPanel.add(btnRefrescar, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setOpaque(false);
        splitPane.setBorder(null);
        splitPane.setDividerLocation(520);

        String[] columnas = {"ID Sesión", "Fecha / Hora", "Circuito", "Clima", "Pole Position"};
        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        tablaHistorial = new JTable(tableModel);
        F1Theme.estilizarTabla(tablaHistorial);
        tablaHistorial.getSelectionModel().addListSelectionListener(e -> actualizarDetalle());

        JScrollPane scrollTabla = new JScrollPane(tablaHistorial);
        scrollTabla.getViewport().setBackground(F1Theme.BG_CARD);
        scrollTabla.setBorder(BorderFactory.createLineBorder(F1Theme.BORDER_COLOR));
        splitPane.setLeftComponent(scrollTabla);

        JPanel panelDetalle = F1Theme.crearTarjeta();
        panelDetalle.setLayout(new BorderLayout(10, 10));

        JLabel lblInfo = new JLabel("🏁 Telemetría y Top 5 de la Sesión");
        lblInfo.setFont(F1Theme.FONT_SUBTITLE);
        lblInfo.setForeground(F1Theme.ACCENT_GOLD);
        panelDetalle.add(lblInfo, BorderLayout.NORTH);

        txtDetalle = new JTextArea();
        txtDetalle.setBackground(F1Theme.BG_SIDEBAR);
        txtDetalle.setForeground(F1Theme.TEXT_PRIMARY);
        txtDetalle.setFont(F1Theme.FONT_REGULAR);
        txtDetalle.setEditable(false);
        txtDetalle.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane scrollDetalle = new JScrollPane(txtDetalle);
        scrollDetalle.setBorder(BorderFactory.createLineBorder(F1Theme.BORDER_COLOR));
        panelDetalle.add(scrollDetalle, BorderLayout.CENTER);

        splitPane.setRightComponent(panelDetalle);
        add(splitPane, BorderLayout.CENTER);

        cargarDatos();
    }

    public void cargarDatos() {
        tableModel.setRowCount(0);
        List<SesionClasificacion> lista = estadisticaService.obtenerHistorialCompleto();
        for (SesionClasificacion s : lista) {
            String pole = (s.getPolePosition() != null) ? s.getPolePosition().getNombre() : "N/A";
            tableModel.addRow(new Object[]{
                    s.getId(),
                    s.getFechaHoraFormateada(),
                    s.getCircuito().getNombre(),
                    s.getClima().getNombre(),
                    pole
            });
        }
        if (tablaHistorial.getRowCount() > 0) {
            tablaHistorial.setRowSelectionInterval(0, 0);
            actualizarDetalle();
        } else {
            txtDetalle.setText("Aún no se han ejecutado sesiones de clasificación.\nVe a la pestaña '🚦 Simulación de Clasificación' para correr tu primera sesión.");
        }
    }

    private void actualizarDetalle() {
        int row = tablaHistorial.getSelectedRow();
        if (row == -1) return;

        List<SesionClasificacion> lista = estadisticaService.obtenerHistorialCompleto();
        if (row >= lista.size()) return;

        SesionClasificacion s = lista.get(row);
        StringBuilder sb = new StringBuilder();
        sb.append("📍 CIRCUITO: ").append(s.getCircuito().getNombre()).append(" (").append(s.getCircuito().getPais()).append(")\n");
        sb.append("📅 Fecha/Hora: ").append(s.getFechaHoraFormateada()).append(" | 🌦️ Clima: ").append(s.getClima().getNombre()).append("\n");
        sb.append("🏎️ Tu Monoplaza: ").append(s.getVehiculoUsuario()).append(" | Tu Piloto: ").append(s.getPilotoUsuario()).append("\n");
        if (s.getConfiguracionUsuario() != null) {
            sb.append("⚙️ Reglajes: ").append(s.getConfiguracionUsuario().toString()).append("\n");
        }
        sb.append("\n========================================================\n");
        sb.append("  TOP 5 CLASIFICACIÓN FINAL:\n");
        sb.append("========================================================\n");

        int top = Math.min(5, s.getResultados().size());
        for (int i = 0; i < top; i++) {
            ResultadoVuelta r = s.getResultados().get(i);
            String delta = (i == 0) ? "POLE" : String.format("+%.3fs", r.getDiferenciaConLiderSegundos());
            sb.append(String.format("  P%d. %-20s (%-18s) - %s (%s)\n",
                    r.getPosicion(), r.getPiloto().getNombre(), r.getPiloto().getEquipo(), r.getTiempoFormateado(), delta));
        }

        ResultadoVuelta user = s.getResultadoUsuario();
        if (user != null) {
            sb.append("\n--------------------------------------------------------\n");
            sb.append(String.format("🎯 TU POSICIÓN DE PARTIDA: P%d con tiempo %s (Vel: %.1f km/h)\n",
                    user.getPosicion(), user.getTiempoFormateado(), user.getVelocidadMediaKmh()));
        }

        txtDetalle.setText(sb.toString());
        txtDetalle.setCaretPosition(0);
    }
}
