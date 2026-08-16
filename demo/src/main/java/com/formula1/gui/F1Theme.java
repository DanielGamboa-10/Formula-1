package com.formula1.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class F1Theme {
    // Paleta de Colores de Alto Contraste
    public static final Color BG_DARK = new Color(15, 17, 26);        // Fondo principal
    public static final Color BG_CARD = new Color(25, 28, 42);        // Tarjetas
    public static final Color BG_CARD_HOVER = new Color(40, 45, 68);  // Hover
    public static final Color BG_SIDEBAR = new Color(18, 20, 30);     // Barra lateral
    public static final Color BG_INPUT = new Color(35, 40, 60);       // Fondo de inputs y combos

    public static final Color RED_PRIMARY = new Color(225, 6, 0);     // Rojo oficial F1
    public static final Color RED_HOVER = new Color(255, 36, 15);     // Rojo brillante
    public static final Color ACCENT_GOLD = new Color(255, 215, 0);   // Dorado
    public static final Color ACCENT_GREEN = new Color(35, 235, 130); // Verde brillante
    public static final Color ACCENT_CYAN = new Color(0, 210, 255);   // Cyan

    public static final Color TEXT_PRIMARY = new Color(255, 255, 255); // Blanco puro
    public static final Color TEXT_MUTED = new Color(210, 220, 240);   // Plateado claro
    public static final Color BORDER_COLOR = new Color(75, 85, 115);   // Bordes nítidos

    // Tipografías estándar 100% compatibles en Windows
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_REGULAR = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.BOLD, 11);
    public static final Font FONT_TIMER = new Font("Consolas", Font.BOLD, 14);

    public static JButton crearBotonPrimario(String texto, String icono) {
        String label = (icono != null && !icono.isEmpty()) ? icono + " " + texto : texto;
        JButton btn = new JButton(label) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2.setColor(new Color(180, 0, 0));
                } else if (getModel().isRollover()) {
                    g2.setColor(RED_HOVER);
                } else {
                    g2.setColor(RED_PRIMARY);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);

                g2.setColor(new Color(255, 100, 90));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 6, 6);

                FontMetrics fm = g2.getFontMetrics(getFont());
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();

                g2.setFont(getFont());
                g2.setColor(Color.WHITE);
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        btn.setFont(FONT_BOLD);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setOpaque(false);
        btn.setPreferredSize(new Dimension(170, 38));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setUI(new BasicButtonUI());
        return btn;
    }

    public static JButton crearBotonSecundario(String texto) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2.setColor(new Color(20, 24, 38));
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(48, 55, 80));
                } else {
                    g2.setColor(new Color(32, 36, 54));
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);

                // Borde
                g2.setColor(getModel().isRollover() ? ACCENT_CYAN : BORDER_COLOR);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 6, 6);

                FontMetrics fm = g2.getFontMetrics(getFont());
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();

                g2.setFont(getFont());
                g2.setColor(Color.WHITE);
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        btn.setFont(FONT_BOLD);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setOpaque(false);
        btn.setPreferredSize(new Dimension(160, 36));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setUI(new BasicButtonUI());
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
        tabla.setForeground(Color.WHITE);
        tabla.setFont(FONT_REGULAR);
        tabla.setRowHeight(34);
        tabla.setShowGrid(true);
        tabla.setGridColor(BORDER_COLOR);
        tabla.setSelectionBackground(new Color(45, 65, 105));
        tabla.setSelectionForeground(Color.WHITE);

        JTableHeader header = tabla.getTableHeader();
        header.setBackground(new Color(18, 20, 30));
        header.setForeground(ACCENT_CYAN);
        header.setFont(FONT_BOLD);
        header.setPreferredSize(new Dimension(header.getWidth(), 38));
        header.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        centerRenderer.setForeground(Color.WHITE);
        for (int i = 0; i < tabla.getColumnCount(); i++) {
            if (tabla.getColumnClass(i) == Integer.class || tabla.getColumnClass(i) == Double.class) {
                tabla.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }
    }

    public static <T> void estilizarCombo(JComboBox<T> combo) {
        combo.setFont(FONT_BOLD);
        combo.setForeground(Color.WHITE);
        combo.setBackground(BG_INPUT);
        combo.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        combo.setFocusable(false);

        combo.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton btn = new JButton("▼");
                btn.setFont(new Font("Segoe UI", Font.BOLD, 10));
                btn.setForeground(Color.WHITE);
                btn.setBackground(BG_INPUT);
                btn.setBorder(new EmptyBorder(0, 4, 0, 8));
                btn.setContentAreaFilled(false);
                btn.setFocusPainted(false);
                return btn;
            }

            @Override
            public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
                g.setColor(BG_INPUT);
                g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
            }

            @Override
            protected ComboPopup createPopup() {
                BasicComboPopup popup = new BasicComboPopup(comboBox);
                popup.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
                return popup;
            }
        });

        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel lbl = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                lbl.setFont(FONT_BOLD);
                lbl.setBorder(new EmptyBorder(6, 12, 6, 12));
                if (isSelected) {
                    lbl.setBackground(RED_PRIMARY);
                    lbl.setForeground(Color.WHITE);
                } else {
                    lbl.setBackground(BG_INPUT);
                    lbl.setForeground(Color.WHITE);
                }
                return lbl;
            }
        });
    }
}
