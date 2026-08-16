package com.formula1;

import com.formula1.data.DataLoader;
import com.formula1.gui.MainFrame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.Color;

public class Main {
    public static void main(String[] args) {
        // 1. Cargar datos iniciales en memoria (HashMap / Map)
        DataLoader.cargarDatosIniciales();

        // 2. Iniciar Interfaz Gráfica de Usuario (GUI)
        SwingUtilities.invokeLater(() -> {
            try {
                // Configurar Look and Feel estándar compatible con tema oscuro
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                UIManager.put("Panel.background", new Color(15, 17, 26));
                UIManager.put("OptionPane.background", new Color(25, 28, 42));
                UIManager.put("OptionPane.messageForeground", Color.WHITE);
            } catch (Exception ignored) {}

            MainFrame ventanaPrincipal = new MainFrame();
            ventanaPrincipal.setVisible(true);
        });
    }
}
