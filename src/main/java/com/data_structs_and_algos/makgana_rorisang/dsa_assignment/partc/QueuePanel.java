package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.partc;

import com.data_structs_and_algos.makgana_rorisang.dsa_assignment.app.DemoPanel;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.Timer;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * Bank queue simulation drawn as a ring of slots with front / rear markers.
 * A Timer (per the UML) calls simulation.tick(); the shared Play / Pause / Step / Reset drive it.
 */
@Component
@Lazy
@Order(70)
public class QueuePanel extends DemoPanel {

    private BankSimulation simulation;
    private final Timer clock;

    private JSpinner capacitySpinner;
    private JSpinner chanceSpinner;

    public QueuePanel() {
        clock = new Timer(300, e -> tick());
        simulation = new BankSimulation(8, 0.4, 42L);
    }

    @Override
    public String getTitle() {
        return "Queue: Bank Simulation";
    }

    @Override
    public String getPartLabel() {
        return "Part C — Lists, Stacks & Queues";
    }

    @Override
    protected JPanel buildControls() {
        capacitySpinner = new JSpinner(new SpinnerNumberModel(8, 2, 16, 1));
        chanceSpinner = new JSpinner(new SpinnerNumberModel(0.4, 0.05, 0.95, 0.05));
        JButton newSim = new JButton("New simulation");
        newSim.addActionListener(e -> reset());

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        controls.add(new JLabel("Line capacity"));
        controls.add(capacitySpinner);
        controls.add(new JLabel("Arrival chance"));
        controls.add(chanceSpinner);
        controls.add(newSim);
        return controls;
    }

    private void tick() {
        if (!attempt("BankSimulation.tick", simulation::tick)) clock.stop();
        repaint();
    }

    // ---- shared control strip ----

    @Override
    public boolean hasPlayback() {
        return true;
    }

    @Override
    public void play() {
        clock.start();
    }

    @Override
    public void pause() {
        clock.stop();
    }

    @Override
    public void stepForward() {
        clock.stop();
        tick();
    }

    @Override
    public void reset() {
        clock.stop();
        int capacity = capacitySpinner != null ? (Integer) capacitySpinner.getValue() : 8;
        double chance = chanceSpinner != null ? (Double) chanceSpinner.getValue() : 0.4;
        simulation = new BankSimulation(capacity, chance, System.nanoTime());
        showStatus("New simulation");
        repaint();
    }

    @Override
    public void setDelayMs(int ms) {
        clock.setDelay(ms);
        clock.setInitialDelay(ms);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintSafely(g, g2 -> {
            CircularQueue<Customer> line = simulation.getLine();
            Object[] slots = line.slotsSnapshot();
            int front = line.getFrontIndex();
            int rear = line.getRearIndex();
            FontMetrics fm = g2.getFontMetrics();

            int cx = getWidth() / 2 - 80;
            int cy = getHeight() / 2;
            int radius = Math.max(60, Math.min(getWidth(), getHeight()) / 2 - 70);
            int slot = 46;
            int n = slots.length;

            for (int i = 0; i < n; i++) {
                double angle = -Math.PI / 2 + 2 * Math.PI * i / n;
                int x = (int) (cx + radius * Math.cos(angle));
                int y = (int) (cy + radius * Math.sin(angle));
                g2.setColor(slots[i] != null ? new Color(0xDCE8FA) : new Color(0xF4F4F4));
                g2.fillRoundRect(x - slot / 2, y - slot / 2, slot, slot, 10, 10);
                g2.setColor(Color.GRAY);
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(x - slot / 2, y - slot / 2, slot, slot, 10, 10);
                if (slots[i] instanceof Customer c) {
                    String s = "C" + c.id;
                    g2.setColor(Color.DARK_GRAY);
                    g2.drawString(s, x - fm.stringWidth(s) / 2, y + 5);
                }
                g2.setColor(Color.LIGHT_GRAY);
                g2.drawString(String.valueOf(i), (int) (cx + (radius - 40) * Math.cos(angle)) - 4,
                        (int) (cy + (radius - 40) * Math.sin(angle)) + 5);

                String marker = (i == front && i == rear) ? "front/rear" : i == front ? "front" : i == rear ? "rear" : null;
                if (marker != null) {
                    int mx = (int) (cx + (radius + 46) * Math.cos(angle));
                    int my = (int) (cy + (radius + 46) * Math.sin(angle));
                    g2.setColor(i == front ? new Color(0x2E8B57) : new Color(0xC0392B));
                    g2.drawString(marker, mx - fm.stringWidth(marker) / 2, my + 5);
                }
            }

            // Teller and stats on the right
            int tx = getWidth() - 220;
            int ty = 40;
            Customer atTeller = simulation.getAtTeller();
            g2.setColor(Color.GRAY);
            g2.drawString("Teller", tx, ty);
            g2.setColor(atTeller != null ? new Color(0xD7F0DD) : new Color(0xF4F4F4));
            g2.fillRoundRect(tx, ty + 8, 120, 50, 10, 10);
            g2.setColor(Color.DARK_GRAY);
            g2.drawRoundRect(tx, ty + 8, 120, 50, 10, 10);
            g2.drawString(atTeller != null ? "Serving C" + atTeller.id : "idle", tx + 12, ty + 38);

            int sy = ty + 100;
            g2.drawString("Clock: " + simulation.getClock(), tx, sy);
            g2.drawString("In line: " + line.size() + " / " + line.capacity(), tx, sy + 20);
            g2.drawString("Served: " + simulation.getServedCount(), tx, sy + 40);
            g2.drawString(String.format("Average wait: %.2f ticks", simulation.getAverageWait()), tx, sy + 60);
        });
    }
}
