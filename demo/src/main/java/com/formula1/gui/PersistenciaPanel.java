package com.formula1.gui;

import com.formula1.data.DataPersistenceManager;
import com.formula1.data.DataStore;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Map;

/**
 * Panel interactivo para la gestión y monitoreo de la persistencia de datos en disco.
 */
public class PersistenciaPanel extends JPanel {
    private final JLabel lblEstadoArchivo;
    private final JLabel lblRutaArchivo;
    private final JLabel lblTamanoArchivo;
    private final JLabel lblFechaGuardado;

    private final JLabel lblTotalPilotos;
    private final JLabel lblTotalEquipos;
    private final JLabel lblTotalCircuitos;
    private final JLabel lblTotalVehiculos;
    private final JLabel lblTotalConfiguraciones;
    private final JLabel lblTotalHistorial;

    private final JCheckBox chkAutoSave;

    public PersistenciaPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(F1Theme.BG_DARK);
        setBorder(new EmptyBorder(25, 25, 25, 25));

        // 1. Encabezado
        JPanel headerPanel = new JPanel(new BorderLayout(5, 5));
        headerPanel.setOpaque(false);

        JLabel lblTitle = new JLabel("💾 GESTIÓN DE PERSISTENCIA DE DATOS");
        lblTitle.setFont(F1Theme.FONT_TITLE);
        lblTitle.setForeground(F1Theme.RED_PRIMARY);

        JLabel lblSubtitle = new JLabel("Monitoreo del almacenamiento local en disco, sincronización de estado y copias de respaldo.");
        lblSubtitle.setFont(F1Theme.FONT_REGULAR);
        lblSubtitle.setForeground(F1Theme.TEXT_MUTED);

        headerPanel.add(lblTitle, BorderLayout.NORTH);
        headerPanel.add(lblSubtitle, BorderLayout.SOUTH);
        add(headerPanel, BorderLayout.NORTH);

        // 2. Contenido Central (Grid con Tarjetas)
        JPanel centralPanel = new JPanel();
        centralPanel.setLayout(new BoxLayout(centralPanel, BoxLayout.Y_AXIS));
        centralPanel.setOpaque(false);

        // Tarjeta 1: Estado del Archivo Físico
        JPanel cardArchivo = F1Theme.crearTarjeta();
        cardArchivo.setLayout(new BorderLayout(10, 10));

        JLabel lblCard1Titulo = new JLabel("📁 INFORMACIÓN DEL ALMACENAMIENTO EN DISCO");
        lblCard1Titulo.setFont(F1Theme.FONT_SUBTITLE);
        lblCard1Titulo.setForeground(F1Theme.ACCENT_CYAN);
        cardArchivo.add(lblCard1Titulo, BorderLayout.NORTH);

        JPanel gridArchivo = new JPanel(new GridLayout(4, 2, 10, 10));
        gridArchivo.setOpaque(false);

        gridArchivo.add(crearEtiquetaClave("Estado del Archivo:"));
        lblEstadoArchivo = crearEtiquetaValor("Comprobando...");
        gridArchivo.add(lblEstadoArchivo);

        gridArchivo.add(crearEtiquetaClave("Ruta de Almacenamiento:"));
        lblRutaArchivo = crearEtiquetaValor("-");
        gridArchivo.add(lblRutaArchivo);

        gridArchivo.add(crearEtiquetaClave("Tamaño en Disco:"));
        lblTamanoArchivo = crearEtiquetaValor("-");
        gridArchivo.add(lblTamanoArchivo);

        gridArchivo.add(crearEtiquetaClave("Última Sincronización:"));
        lblFechaGuardado = crearEtiquetaValor("-");
        gridArchivo.add(lblFechaGuardado);

        cardArchivo.add(gridArchivo, BorderLayout.CENTER);
        centralPanel.add(cardArchivo);
        centralPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Tarjeta 2: Resumen de Entidades Persistidas
        JPanel cardEntidades = F1Theme.crearTarjeta();
        cardEntidades.setLayout(new BorderLayout(10, 10));

        JLabel lblCard2Titulo = new JLabel("📊 REGISTROS CENTRALIZADOS EN EL DATASTORE");
        lblCard2Titulo.setFont(F1Theme.FONT_SUBTITLE);
        lblCard2Titulo.setForeground(F1Theme.ACCENT_GOLD);
        cardEntidades.add(lblCard2Titulo, BorderLayout.NORTH);

        JPanel gridEntidades = new JPanel(new GridLayout(3, 4, 15, 12));
        gridEntidades.setOpaque(false);

        gridEntidades.add(crearEtiquetaClave("🏎️ Pilotos FIA:"));
        lblTotalPilotos = crearEtiquetaValor("0");
        gridEntidades.add(lblTotalPilotos);

        gridEntidades.add(crearEtiquetaClave("🏁 Escuderías F1:"));
        lblTotalEquipos = crearEtiquetaValor("0");
        gridEntidades.add(lblTotalEquipos);

        gridEntidades.add(crearEtiquetaClave("🗺️ Circuitos Oficiales:"));
        lblTotalCircuitos = crearEtiquetaValor("0");
        gridEntidades.add(lblTotalCircuitos);

        gridEntidades.add(crearEtiquetaClave("🚗 Monoplazas:"));
        lblTotalVehiculos = crearEtiquetaValor("0");
        gridEntidades.add(lblTotalVehiculos);

        gridEntidades.add(crearEtiquetaClave("⚙️ Setups Guardados:"));
        lblTotalConfiguraciones = crearEtiquetaValor("0");
        gridEntidades.add(lblTotalConfiguraciones);

        gridEntidades.add(crearEtiquetaClave("📈 Sesiones Clasificación:"));
        lblTotalHistorial = crearEtiquetaValor("0");
        gridEntidades.add(lblTotalHistorial);

        cardEntidades.add(gridEntidades, BorderLayout.CENTER);
        centralPanel.add(cardEntidades);
        centralPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Tarjeta 3: Opciones y Ajustes
        JPanel cardAjustes = F1Theme.crearTarjeta();
        cardAjustes.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 5));

        chkAutoSave = new JCheckBox("Guardado automático activado tras cada cambio (Auto-Save)");
        chkAutoSave.setFont(F1Theme.FONT_BOLD);
        chkAutoSave.setForeground(Color.WHITE);
        chkAutoSave.setOpaque(false);
        chkAutoSave.setSelected(DataStore.getInstance().isAutoSave());
        chkAutoSave.addActionListener(e -> {
            DataStore.getInstance().setAutoSave(chkAutoSave.isSelected());
            actualizarMetadatos();
        });
        cardAjustes.add(chkAutoSave);

        centralPanel.add(cardAjustes);

        add(new JScrollPane(centralPanel) {{
            setOpaque(false);
            getViewport().setOpaque(false);
            setBorder(null);
        }}, BorderLayout.CENTER);

        // 3. Barra Inferior con Botones de Acción
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        actionsPanel.setOpaque(false);

        JButton btnActualizar = F1Theme.crearBotonSecundario("🔄 Refrescar Estado");
        btnActualizar.addActionListener(e -> actualizarMetadatos());

        JButton btnCargar = F1Theme.crearBotonSecundario("📂 Recargar de Disco");
        btnCargar.addActionListener(e -> ejecutarRecarga());

        JButton btnRestaurar = F1Theme.crearBotonSecundario("⚠️ Restaurar de Fábrica");
        btnRestaurar.setForeground(new Color(255, 120, 120));
        btnRestaurar.addActionListener(e -> ejecutarRestauracionFabrica());

        JButton btnGuardar = F1Theme.crearBotonPrimario("Guardar en Disco", "💾");
        btnGuardar.addActionListener(e -> ejecutarGuardado());

        actionsPanel.add(btnActualizar);
        actionsPanel.add(btnCargar);
        actionsPanel.add(btnRestaurar);
        actionsPanel.add(btnGuardar);

        add(actionsPanel, BorderLayout.SOUTH);

        actualizarMetadatos();
    }

    private JLabel crearEtiquetaClave(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(F1Theme.FONT_BOLD);
        lbl.setForeground(F1Theme.TEXT_MUTED);
        return lbl;
    }

    private JLabel crearEtiquetaValor(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(F1Theme.FONT_BOLD);
        lbl.setForeground(Color.WHITE);
        return lbl;
    }

    public void actualizarMetadatos() {
        Map<String, String> meta = DataPersistenceManager.obtenerMetadatos();

        boolean existe = "true".equalsIgnoreCase(meta.get("existe"));
        if (existe) {
            lblEstadoArchivo.setText("● ALMACENADO Y SINCRONIZADO EN DISCO");
            lblEstadoArchivo.setForeground(F1Theme.ACCENT_GREEN);
        } else {
            lblEstadoArchivo.setText("○ PENDIENTE DE PRIMER GUARDADO EN DISCO");
            lblEstadoArchivo.setForeground(F1Theme.ACCENT_GOLD);
        }

        lblRutaArchivo.setText(meta.getOrDefault("ruta", "-"));
        lblTamanoArchivo.setText(meta.getOrDefault("tamano", "0 B"));
        lblFechaGuardado.setText(meta.getOrDefault("ultimaModificacion", "-"));

        lblTotalPilotos.setText(meta.getOrDefault("pilotos", "0") + " pilotos");
        lblTotalEquipos.setText(meta.getOrDefault("equipos", "0") + " escuderías");
        lblTotalCircuitos.setText(meta.getOrDefault("circuitos", "0") + " circuitos");
        lblTotalVehiculos.setText(meta.getOrDefault("vehiculos", "0") + " monoplazas");
        lblTotalConfiguraciones.setText(meta.getOrDefault("configuraciones", "0") + " setups");
        lblTotalHistorial.setText(meta.getOrDefault("historial", "0") + " simulaciones");

        chkAutoSave.setSelected(DataStore.getInstance().isAutoSave());
    }

    private void ejecutarGuardado() {
        boolean ok = DataStore.getInstance().guardar();
        if (ok) {
            actualizarMetadatos();
            JOptionPane.showMessageDialog(this,
                    "¡Datos guardados exitosamente en disco!\nArchivo: " + DataPersistenceManager.getRutaArchivo(),
                    "Guardado Exitoso",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "No se pudieron guardar los datos en disco.",
                    "Error de Guardado",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ejecutarRecarga() {
        int resp = JOptionPane.showConfirmDialog(this,
                "¿Desea recargar los datos desde el archivo en disco?\nSe reemplazarán las modificaciones que no hayan sido guardadas.",
                "Confirmar Recarga",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (resp == JOptionPane.YES_OPTION) {
            boolean ok = DataStore.getInstance().cargar();
            if (ok) {
                actualizarMetadatos();
                JOptionPane.showMessageDialog(this,
                        "¡Datos recargados exitosamente desde disco!",
                        "Recarga Exitosa",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se encontró un archivo válido para recargar.",
                        "Aviso",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void ejecutarRestauracionFabrica() {
        int resp = JOptionPane.showConfirmDialog(this,
                "⚠️ ¿Está seguro de que desea restaurar los datos de fábrica originales?\n" +
                "Esto borrará pilotos, circuitos y simulaciones personalizadas.",
                "Confirmar Restauración de Fábrica",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (resp == JOptionPane.YES_OPTION) {
            boolean ok = DataStore.getInstance().restaurarPorDefecto();
            if (ok) {
                actualizarMetadatos();
                JOptionPane.showMessageDialog(this,
                        "¡Se han restaurado exitosamente todos los datos originales de Fórmula 1!",
                        "Restauración Completada",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Hubo un problema al restaurar los datos iniciales.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
