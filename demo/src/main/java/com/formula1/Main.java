package com.formula1;

import com.formula1.data.DataLoader;
import com.formula1.gui.MainFrame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        // 1. Cargar datos iniciales en memoria (HashMap / Map)
        DataLoader.cargarDatosIniciales();

        // 2. Iniciar Interfaz Gráfica de Usuario (GUI) en el hilo de eventos de Swing
        SwingUtilities.invokeLater(() -> {
            try {
                // Look and Feel nativo del sistema
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}

            MainFrame ventanaPrincipal = new MainFrame();
            ventanaPrincipal.setVisible(true);
        });
    }
}
