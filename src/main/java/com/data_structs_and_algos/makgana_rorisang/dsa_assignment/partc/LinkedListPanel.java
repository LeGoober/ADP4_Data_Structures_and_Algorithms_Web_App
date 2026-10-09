package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.partc;

import com.data_structs_and_algos.makgana_rorisang.dsa_assignment.app.DemoPanel;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

/** Boxes joined by arrows; the node a search lands on is highlighted. */
@Component
@Lazy
@Order(50)
public class LinkedListPanel extends DemoPanel {

    private static final int BOX_W = 70;
    private static final int BOX_H = 36;
    private static final int GAP = 36;

    private final SinglyLinkedList<String> list = new SinglyLinkedList<>();
    private JTextField valueField;
    private JTextField indexField;
    private int highlightIndex = -1;

    @Override
    public String getTitle() {
        return "Singly Linked List";
    }

    @Override
    public String getPartLabel() {
        return "Part C — Lists, Stacks & Queues";
    }

    @Override
    protected JPanel buildControls() {
        valueField = new JTextField(6);
        indexField = new JTextField(3);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        controls.add(new JLabel("Value"));
        controls.add(valueField);
        controls.add(new JLabel("Index"));
        controls.add(indexField);
        controls.add(button("Insert first", () -> list.insertFirst(value())));
        controls.add(button("Insert last", () -> list.insertLast(value())));
        controls.add(button("Insert at", () -> list.insertAt(index(), value())));
        controls.add(button("Delete value", () -> showStatus("delete: " + list.delete(value()))));
        controls.add(button("Delete at", () -> showStatus("deleteAt removed: " + list.deleteAt(index()))));
        controls.add(button("Search", () -> {
            highlightIndex = list.search(value());
            showStatus(highlightIndex >= 0 ? "Found at index " + highlightIndex : "Not found");
        }));
        return controls;
    }

    private JButton button(String label, Runnable action) {
        JButton b = new JButton(label);
        b.addActionListener(e -> {
            highlightIndex = -1;
            attempt(label, action);
            repaint();
        });
        return b;
    }

    private String value() {
        String v = valueField.getText().trim();
        if (v.isEmpty()) throw new IllegalArgumentException("enter a value");
        return v;
    }

    private int index() {
        try {
            return Integer.parseInt(indexField.getText().trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("enter an integer index");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintSafely(g, g2 -> {
            Object[] items = list.toArray();
            FontMetrics fm = g2.getFontMetrics();
            int perRow = Math.max(1, (getWidth() - 80) / (BOX_W + GAP));
            int x0 = 40, y0 = 60;

            g2.setColor(Color.GRAY);
            g2.drawString("head", x0, y0 - 14);
            g2.drawString("size = " + list.size(), x0, 24);

            for (int i = 0; i <= items.length; i++) {
                int x = x0 + (i % perRow) * (BOX_W + GAP);
                int y = y0 + (i / perRow) * (BOX_H + 40);
                if (i == items.length) {
                    g2.setColor(Color.GRAY);
                    g2.drawString("null", x, y + BOX_H / 2 + 5);
                    break;
                }
                g2.setColor(i == highlightIndex ? new Color(0xF8D58C) : new Color(0xE8EEF8));
                g2.fillRoundRect(x, y, BOX_W, BOX_H, 8, 8);
                g2.setColor(new Color(0x3A6FC4));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(x, y, BOX_W, BOX_H, 8, 8);
                String text = String.valueOf(items[i]);
                g2.setColor(Color.DARK_GRAY);
                g2.drawString(text, x + (BOX_W - fm.stringWidth(text)) / 2, y + BOX_H / 2 + 5);
                g2.setColor(Color.GRAY);
                g2.drawString(String.valueOf(i), x + 2, y + BOX_H + 14);

                // arrow to the next box (or wrap to the next row)
                if ((i + 1) % perRow != 0) {
                    drawArrow(g2, x + BOX_W, y + BOX_H / 2, x + BOX_W + GAP - 4, y + BOX_H / 2);
                } else {
                    drawArrow(g2, x + BOX_W / 2, y + BOX_H, x0 + BOX_W / 2, y + BOX_H + 36);
                }
            }
        });
    }

    private void drawArrow(Graphics2D g, int x1, int y1, int x2, int y2) {
        g.setColor(Color.GRAY);
        g.drawLine(x1, y1, x2, y2);
        double angle = Math.atan2(y2 - y1, x2 - x1);
        int len = 8;
        g.drawLine(x2, y2, (int) (x2 - len * Math.cos(angle - 0.4)), (int) (y2 - len * Math.sin(angle - 0.4)));
        g.drawLine(x2, y2, (int) (x2 - len * Math.cos(angle + 0.4)), (int) (y2 - len * Math.sin(angle + 0.4)));
    }
}
