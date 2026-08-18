package com.formula1;

import com.formula1.data.DataLoader;
import com.formula1.data.DataPersistenceManager;
import com.formula1.data.DataStore;
import com.formula1.gui.MainFrame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.Color;

/**
 * Punto de entrada principal de la aplicación.
 * Inicializa la persistencia de datos (disco/memoria) y lanza la interfaz gráfica de escritorio.
 */
public class Main {
    public static void main(String[] args) {
        DataStore store = DataStore.getInstance();

        // 1. Carga inteligente de persistencia: recuperar datos guardados o inicializar baseline
        if (DataPersistenceManager.existeArchivoPersistencia()) {
            boolean cargado = DataPersistenceManager.cargar(store);
            if (!cargado || store.getPilotos().isEmpty()) {
                System.out.println("⚠️ Archivo de persistencia vacío o corrupto. Cargando datos de fábrica...");
                DataLoader.cargarDatosIniciales();
                DataPersistenceManager.guardar(store);
            } else {
                System.out.println("✅ Datos cargados exitosamente desde el almacenamiento persistente en disco.");
            }
        } else {
            System.out.println("ℹ️ Primera ejecución: Inicializando datos de fábrica y creando archivo de persistencia...");
            DataLoader.cargarDatosIniciales();
            DataPersistenceManager.guardar(store);
        }

        // 2. Registro de ShutdownHook para garantizar guardado automático al cerrar la aplicación
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                System.out.println("💾 Guardando estado persistente antes de cerrar la aplicación...");
                DataPersistenceManager.guardar(DataStore.getInstance());
            } catch (Exception e) {
                System.err.println("❌ Error en ShutdownHook de persistencia: " + e.getMessage());
            }
        }));

        // 2. Ejecución segura de la interfaz gráfica en el hilo de eventos de Swing (EDT)
        SwingUtilities.invokeLater(() -> {
            try {
                // Configuración de propiedades visuales para soporte de tema oscuro F1
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                UIManager.put("Panel.background", new Color(15, 17, 26));
                UIManager.put("OptionPane.background", new Color(25, 28, 42));
                UIManager.put("OptionPane.messageForeground", Color.WHITE);
            } catch (Exception ignored) {}

            // Instancia y visibilidad de la ventana principal
            MainFrame ventanaPrincipal = new MainFrame();
            ventanaPrincipal.setVisible(true);
        });
    }
}
