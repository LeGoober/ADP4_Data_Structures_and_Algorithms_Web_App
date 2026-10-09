package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.partc;

import com.data_structs_and_algos.makgana_rorisang.dsa_assignment.app.DemoPanel;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * Animates string reversal: one push per timer tick, then one pop per tick.
 * Uses its own Timer (per the UML) instead of a StepPlayer, so it overrides the playback hooks.
 */
@Component
@Lazy
@Order(60)
public class StackPanel extends DemoPanel {

    private static final int MAX_LENGTH = 20;

    private final StringReverser reverser;

    private ArrayStack<Character> stack;
    private final Timer animation;
    private String input = "";
    private StringBuilder output = new StringBuilder();

    private int pushIndex;
    private JTextField inputField;

    public StackPanel(StringReverser reverser) {
        this.reverser = reverser;
        this.animation = new Timer(300, e -> tick());
        load("STACK");
    }

    @Override
    public String getTitle() {
        return "Stack: String Reversal";
    }

    @Override
    public String getPartLabel() {
        return "Part C — Lists, Stacks & Queues";
    }

    @Override
    protected JPanel buildControls() {
        inputField = new JTextField(input, 16);
        JButton loadButton = new JButton("Load");
        loadButton.addActionListener(e -> load(inputField.getText()));
        inputField.addActionListener(e -> load(inputField.getText()));
        JButton reverseButton = new JButton("StringReverser.reverse()");
        reverseButton.addActionListener(e -> attempt("StringReverser.reverse",
                () -> showStatus("reverse(\"" + inputField.getText() + "\") = \"" + reverser.reverse(inputField.getText()) + "\"")));

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        controls.add(new JLabel("Text (max " + MAX_LENGTH + ")"));
        controls.add(inputField);
        controls.add(loadButton);
        controls.add(reverseButton);
        controls.add(new JLabel("  then press Play / Step"));
        return controls;
    }

    private void load(String text) {
        animation.stop();
        input = text.length() > MAX_LENGTH ? text.substring(0, MAX_LENGTH) : text;
        output = new StringBuilder();
        pushIndex = 0;
        stack = new ArrayStack<>(Math.max(1, input.length()));
        repaint();
    }

    private void tick() {
        boolean ok = attempt("Stack animation", () -> {
            if (pushIndex < input.length()) {
                stack.push(input.charAt(pushIndex++));
            } else if (!stack.isEmpty()) {
                output.append(stack.pop());
            } else {
                animation.stop();
                showStatus("Reversed: \"" + output + "\"");
            }
        });
        if (!ok) animation.stop();
        repaint();
    }

    // ---- shared control strip ----

    @Override
    public boolean hasPlayback() {
        return true;
    }

    @Override
    public void play() {
        animation.start();
    }

    @Override
    public void pause() {
        animation.stop();
    }

    @Override
    public void stepForward() {
        animation.stop();
        tick();
    }

    @Override
    public void reset() {
        load(input);
    }

    @Override
    public void setDelayMs(int ms) {
        animation.setDelay(ms);
        animation.setInitialDelay(ms);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintSafely(g, g2 -> {
            FontMetrics fm = g2.getFontMetrics();
            int cell = 34;

            g2.setColor(Color.GRAY);
            g2.drawString("Input", 30, 34);
            drawRow(g2, input, pushIndex, 30, 44, cell, fm);
            g2.setColor(Color.GRAY);
            g2.drawString("Output", 30, 114);
            drawRow(g2, output.toString(), output.length(), 30, 124, cell, fm);

            // Stack drawn bottom-up; toArray() is expected to return bottom first.
            Object[] items = stack.toArray();
            int sx = getWidth() - 160;
            int bottom = getHeight() - 30;
            g2.setColor(Color.GRAY);
            g2.drawString("Stack (top ↑)", sx, bottom + 20);
            g2.drawLine(sx - 6, bottom, sx + 66, bottom);
            for (int i = 0; i < items.length; i++) {
                int y = bottom - (i + 1) * (cell - 4);
                g2.setColor(i == items.length - 1 ? new Color(0xF8D58C) : new Color(0xE8EEF8));
                g2.fillRect(sx, y, 60, cell - 6);
                g2.setColor(new Color(0x3A6FC4));
                g2.drawRect(sx, y, 60, cell - 6);
                String s = String.valueOf(items[i]);
                g2.setColor(Color.DARK_GRAY);
                g2.drawString(s, sx + (60 - fm.stringWidth(s)) / 2, y + (cell - 6) / 2 + 5);
            }
        });
    }

    private void drawRow(Graphics2D g, String text, int processed, int x, int y, int cell, FontMetrics fm) {
        for (int i = 0; i < text.length(); i++) {
            int cx = x + i * cell;
            boolean done = i < processed;
            g.setColor(done ? new Color(0xF2F2F2) : new Color(0xE8EEF8));
            g.fillRect(cx, y, cell - 4, cell - 4);
            g.setColor(Color.GRAY);
            g.drawRect(cx, y, cell - 4, cell - 4);
            String s = String.valueOf(text.charAt(i));
            g.setColor(done && y < 100 ? Color.LIGHT_GRAY : Color.DARK_GRAY);
            g.drawString(s, cx + (cell - 4 - fm.stringWidth(s)) / 2, y + (cell - 4) / 2 + 5);
        }
    }
}
