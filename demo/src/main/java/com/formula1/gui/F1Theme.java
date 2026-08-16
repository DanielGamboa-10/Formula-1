package com.formula1.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class F1Theme {
    // Paleta de Colores de Alto Contraste
    public static final Color BG_DARK = new Color(13, 14, 20);        // #0d0e14 - Fondo principal profundo
    public static final Color BG_CARD = new Color(24, 26, 38);        // #181a26 - Tarjetas
    public static final Color BG_CARD_HOVER = new Color(38, 42, 60);  // Hover
    public static final Color BG_SIDEBAR = new Color(18, 20, 30);     // #12141e - Barra lateral
    public static final Color BG_INPUT = new Color(32, 35, 52);       // Fondo de inputs y combos

    public static final Color RED_PRIMARY = new Color(225, 6, 0);     // #e10600 - Rojo oficial F1
    public static final Color RED_HOVER = new Color(255, 36, 15);     // Rojo brillante
    public static final Color ACCENT_GOLD = new Color(255, 215, 0);   // #ffd700 - Dorado brillante (Pole)
    public static final Color ACCENT_GREEN = new Color(35, 235, 130); // Verde brillante
    public static final Color ACCENT_CYAN = new Color(0, 210, 255);   // Cyan eléctrico

    // Textos de Máxima Visibilidad
    public static final Color TEXT_PRIMARY = new Color(255, 255, 255); // Blanco puro
    public static final Color TEXT_MUTED = new Color(220, 228, 245);   // Blanco suave / Plateado de alto contraste
    public static final Color TEXT_LABEL = new Color(190, 205, 230);   // Etiquetas visibles
    public static final Color BORDER_COLOR = new Color(70, 78, 105);   // Bordes nítidos

    // Tipografías
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_REGULAR = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.BOLD, 12);
    public static final Font FONT_TIMER = new Font("Consolas", Font.BOLD, 15);

    public static JButton crearBotonPrimario(String texto, String emoji) {
        JButton btn = new JButton(emoji + " " + texto);
        btn.setFont(FONT_BOLD);
        btn.setForeground(TEXT_PRIMARY);
        btn.setBackground(RED_PRIMARY);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 80, 70), 1),
                new EmptyBorder(10, 18, 10, 18)
        ));
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
                btn.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(ACCENT_CYAN, 1),
                        new EmptyBorder(8, 16, 8, 16)
                ));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(BG_CARD);
                btn.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER_COLOR, 1),
                        new EmptyBorder(8, 16, 8, 16)
                ));
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
        tabla.setRowHeight(34);
        tabla.setShowGrid(true);
        tabla.setGridColor(BORDER_COLOR);
        tabla.setSelectionBackground(new Color(45, 60, 95));
        tabla.setSelectionForeground(Color.WHITE);

        JTableHeader header = tabla.getTableHeader();
        header.setBackground(new Color(20, 22, 32));
        header.setForeground(ACCENT_CYAN);
        header.setFont(FONT_BOLD);
        header.setPreferredSize(new Dimension(header.getWidth(), 38));
        header.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        centerRenderer.setForeground(TEXT_PRIMARY);
        for (int i = 0; i < tabla.getColumnCount(); i++) {
            if (tabla.getColumnClass(i) == Integer.class || tabla.getColumnClass(i) == Double.class) {
                tabla.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }
    }

    public static <T> void estilizarCombo(JComboBox<T> combo) {
        combo.setBackground(BG_INPUT);
        combo.setForeground(TEXT_PRIMARY);
        combo.setFont(FONT_BOLD);
        combo.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel lbl = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                lbl.setFont(FONT_BOLD);
                if (isSelected) {
                    lbl.setBackground(RED_PRIMARY);
                    lbl.setForeground(Color.WHITE);
                } else {
                    lbl.setBackground(BG_INPUT);
                    lbl.setForeground(Color.WHITE);
                }
                lbl.setBorder(new EmptyBorder(6, 10, 6, 10));
                return lbl;
            }
        });
    }
}
