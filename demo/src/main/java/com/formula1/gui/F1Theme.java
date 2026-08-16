package com.formula1.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class F1Theme {
    // Paleta de Colores F1 Premium Dark
    public static final Color BG_DARK = new Color(21, 21, 30);        // #15151e - Fondo principal
    public static final Color BG_CARD = new Color(30, 30, 45);        // #1e1e2d - Tarjetas y paneles
    public static final Color BG_CARD_HOVER = new Color(42, 42, 62);  // Hover en tarjetas
    public static final Color BG_SIDEBAR = new Color(15, 15, 23);     // #0f0f17 - Barra lateral
    public static final Color RED_PRIMARY = new Color(225, 6, 0);     // #e10600 - Rojo oficial F1
    public static final Color RED_HOVER = new Color(255, 24, 1);      // Rojo brillante
    public static final Color ACCENT_GOLD = new Color(255, 184, 0);   // #ffb800 - Pole Position
    public static final Color ACCENT_GREEN = new Color(0, 210, 106);  // Verde telemetría
    public static final Color ACCENT_BLUE = new Color(0, 144, 255);   // Azul widgets
    public static final Color TEXT_PRIMARY = new Color(255, 255, 255);
    public static final Color TEXT_MUTED = new Color(160, 160, 185);
    public static final Color BORDER_COLOR = new Color(48, 48, 70);

    // Tipografías
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_REGULAR = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_TIMER = new Font("Consolas", Font.BOLD, 14);

    public static JButton crearBotonPrimario(String texto, String emoji) {
        JButton btn = new JButton(emoji + " " + texto);
        btn.setFont(FONT_BOLD);
        btn.setForeground(TEXT_PRIMARY);
        btn.setBackground(RED_PRIMARY);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(10, 18, 10, 18));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(RED_HOVER);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(RED_PRIMARY);
            }
        });
        return btn;
    }

    public static JButton crearBotonSecundario(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(FONT_BOLD);
        btn.setForeground(TEXT_PRIMARY);
        btn.setBackground(BG_CARD);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                new EmptyBorder(8, 16, 8, 16)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(BG_CARD_HOVER);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(BG_CARD);
            }
        });
        return btn;
    }

    public static JPanel crearTarjeta() {
        JPanel panel = new JPanel();
        panel.setBackground(BG_CARD);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                new EmptyBorder(15, 15, 15, 15)
        ));
        return panel;
    }

    public static void estilizarTabla(JTable tabla) {
        tabla.setBackground(BG_CARD);
        tabla.setForeground(TEXT_PRIMARY);
        tabla.setFont(FONT_REGULAR);
        tabla.setRowHeight(32);
        tabla.setShowGrid(true);
        tabla.setGridColor(BORDER_COLOR);
        tabla.setSelectionBackground(new Color(60, 60, 90));
        tabla.setSelectionForeground(TEXT_PRIMARY);

        JTableHeader header = tabla.getTableHeader();
        header.setBackground(BG_SIDEBAR);
        header.setForeground(TEXT_PRIMARY);
        header.setFont(FONT_BOLD);
        header.setPreferredSize(new Dimension(header.getWidth(), 36));
        header.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tabla.getColumnCount(); i++) {
            if (tabla.getColumnClass(i) == Integer.class || tabla.getColumnClass(i) == Double.class) {
                tabla.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }
    }
}
