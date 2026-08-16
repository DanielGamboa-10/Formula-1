package com.formula1;

import com.formula1.data.DataLoader;
import com.formula1.gui.MainFrame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.Color;

/**
 * Punto de entrada principal de la aplicación.
 * Inicializa la persistencia en memoria y lanza la interfaz gráfica de escritorio.
 */
public class Main {
    public static void main(String[] args) {
        // 1. Precarga de datos oficiales en memoria (Pilotos, Equipos, Circuitos, Monoplazas)
        DataLoader.cargarDatosIniciales();

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
