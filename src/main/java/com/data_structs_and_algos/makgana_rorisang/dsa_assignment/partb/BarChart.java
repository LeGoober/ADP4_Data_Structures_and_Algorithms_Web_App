package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.partb;

import javax.swing.JPanel;
import javax.swing.UIManager;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;

/** Horizontal-axis bar chart of average sort time per algorithm (milliseconds). */
public class BarChart extends JPanel {

    private static final Color[] PALETTE = {new Color(0x3A6FC4), new Color(0xE07B39), new Color(0x4CAF6E), new Color(0x9B59B6)};

    private List<BenchmarkResult> results = new ArrayList<>();

    public void setResults(List<BenchmarkResult> r) {
        results = r == null ? new ArrayList<>() : new ArrayList<>(r);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        FontMetrics fm = g2.getFontMetrics();

        if (results.isEmpty()) {
            String msg = "Run the benchmark to see average times here.";
            g2.setColor(UIManager.getColor("Label.disabledForeground"));
            g2.drawString(msg, (getWidth() - fm.stringWidth(msg)) / 2, getHeight() / 2);
            g2.dispose();
            return;
        }

        int left = 40, right = 20, top = 30, bottom = 40;
        int plotW = getWidth() - left - right;
        int plotH = getHeight() - top - bottom;
        double max = results.stream().mapToDouble(r -> r.averageNanos).max().orElse(1);
        max = Math.max(max, 1);

        g2.setColor(Color.DARK_GRAY);
        g2.drawString("Average time (ms)", left, top - 12);
        g2.setColor(UIManager.getColor("Component.borderColor"));
        g2.drawLine(left, top + plotH, left + plotW, top + plotH);

        int slot = plotW / results.size();
        int barW = Math.min(90, (int) (slot * 0.6));
        for (int i = 0; i < results.size(); i++) {
            BenchmarkResult r = results.get(i);
            int h = (int) (plotH * r.averageNanos / max);
            int x = left + i * slot + (slot - barW) / 2;
            g2.setColor(PALETTE[i % PALETTE.length]);
            g2.fillRect(x, top + plotH - h, barW, h);

            g2.setColor(Color.DARK_GRAY);
            String value = String.format("%.3f", r.averageNanos / 1_000_000.0);
            g2.drawString(value, x + (barW - fm.stringWidth(value)) / 2, top + plotH - h - 4);
            String name = r.algorithm;
            g2.drawString(name, left + i * slot + (slot - fm.stringWidth(name)) / 2, top + plotH + fm.getHeight() + 2);
        }
        g2.dispose();
    }
}
