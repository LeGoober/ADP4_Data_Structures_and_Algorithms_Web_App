package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.partd;

import com.data_structs_and_algos.makgana_rorisang.dsa_assignment.app.DemoPanel;
import com.data_structs_and_algos.makgana_rorisang.dsa_assignment.app.StepPlayer;
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
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.Supplier;

/** Draws the BST via TreeLayout and lights up one key per tick for searches and traversals. */
@Component
@Lazy
@Order(80)
public class BSTPanel extends DemoPanel {

    private static final int H_GAP = 36;
    private static final int V_GAP = 60;
    private static final int R = 16;

    private BinarySearchTree tree = new BinarySearchTree();
    private final TreeLayout layout = new TreeLayout();
    private final StepPlayer<Integer> player;
    private final Set<Integer> highlighted = new HashSet<>();
    private JTextField keyField;
    private JLabel statsLabel;

    private Integer currentKey;
    private final List<Integer> visitOrder = new ArrayList<>();
    private final Random random = new Random();

    public BSTPanel() {
        player = new StepPlayer<>(key -> {
            highlighted.add(key);
            currentKey = key;
            visitOrder.add(key);
            repaint();
        }, () -> showStatus("Replay finished"));
    }

    @Override
    public String getTitle() {
        return "Binary Search Tree";
    }

    @Override
    public String getPartLabel() {
        return "Part D — Binary Search Tree";
    }

    @Override
    protected StepPlayer<?> getPlayer() {
        return player;
    }

    @Override
    protected JPanel buildControls() {
        keyField = new JTextField(4);
        statsLabel = new JLabel("size: –   height: –");

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        controls.add(new JLabel("Key"));
        controls.add(keyField);
        controls.add(button("Insert", () -> showStatus("insert: " + tree.insert(key()))));
        controls.add(button("Delete", () -> showStatus("delete: " + tree.delete(key()))));
        controls.add(button("Search", () -> {
            int k = key();
            animate(tree.searchPath(k));
            showStatus("search(" + k + ") = " + tree.search(k));
        }));
        controls.add(button("In-order", () -> animate(tree.inOrder())));
        controls.add(button("Pre-order", () -> animate(tree.preOrder())));
        controls.add(button("Post-order", () -> animate(tree.postOrder())));
        controls.add(button("Insert 15 random", () -> insertRandom(15)));
        controls.add(button("Insert 1–15 in order", () -> insertSorted(15)));
        controls.add(button("Clear", () -> tree.clear()));
        controls.add(statsLabel);
        return controls;
    }

    private JButton button(String label, Runnable action) {
        JButton b = new JButton(label);
        b.addActionListener(e -> {
            attempt(label, action);
            updateStats();
            repaint();
        });
        return b;
    }

    private int key() {
        try {
            return Integer.parseInt(keyField.getText().trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("enter an integer key");
        }
    }

    private void insertRandom(int count) {
        tree = new BinarySearchTree();
        List<Integer> keys = new ArrayList<>();
        for (int i = 1; i <= 99; i++) keys.add(i);
        Collections.shuffle(keys, random);
        for (int i = 0; i < count; i++) tree.insert(keys.get(i));
    }

    private void insertSorted(int count) {
        tree = new BinarySearchTree();
        for (int i = 1; i <= count; i++) tree.insert(i);
    }

    private void animate(List<Integer> keys) {
        highlighted.clear();
        visitOrder.clear();
        currentKey = null;
        player.load(keys);
        player.play();
    }

    private void updateStats() {
        statsLabel.setText("size: " + safe(tree::size) + "   height: " + safe(tree::height));
    }

    private String safe(Supplier<Object> s) {
        try {
            return String.valueOf(s.get());
        } catch (RuntimeException e) {
            return "TODO";
        }
    }

    @Override
    public void reset() {
        player.reset();
        highlighted.clear();
        visitOrder.clear();
        currentKey = null;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintSafely(g, g2 -> {
            BSTNode root = tree.getRoot();
            if (root == null) {
                paintMessage(g2, "Empty tree. Insert some keys.");
                return;
            }
            layout.compute(root, H_GAP, V_GAP);
            drawEdges(g2, root);
            drawNodes(g2, root, g2.getFontMetrics());

            if (!visitOrder.isEmpty()) {
                StringJoiner j = new StringJoiner(", ", "Visited: ", "");
                visitOrder.forEach(k -> j.add(String.valueOf(k)));
                g2.setColor(Color.DARK_GRAY);
                g2.drawString(j.toString(), 20, getHeight() - 16);
            }
        });
    }

    private Point at(BSTNode node) {
        Point p = layout.positionOf(node);
        return new Point(p.x + 30, p.y + 30);
    }

    private void drawEdges(Graphics2D g, BSTNode node) {
        Point p = at(node);
        g.setColor(Color.GRAY);
        g.setStroke(new BasicStroke(1.5f));
        for (BSTNode child : new BSTNode[]{node.left, node.right}) {
            if (child != null) {
                Point c = at(child);
                g.drawLine(p.x, p.y, c.x, c.y);
                drawEdges(g, child);
            }
        }
    }

    private void drawNodes(Graphics2D g, BSTNode node, FontMetrics fm) {
        if (node == null) return;
        Point p = at(node);
        boolean current = currentKey != null && currentKey == node.key;
        g.setColor(current ? new Color(0xF8D58C) : highlighted.contains(node.key) ? new Color(0xD7F0DD) : new Color(0xDCE8FA));
        g.fillOval(p.x - R, p.y - R, 2 * R, 2 * R);
        g.setColor(new Color(0x3A6FC4));
        g.drawOval(p.x - R, p.y - R, 2 * R, 2 * R);
        String s = String.valueOf(node.key);
        g.setColor(Color.DARK_GRAY);
        g.drawString(s, p.x - fm.stringWidth(s) / 2, p.y + 5);
        drawNodes(g, node.left, fm);
        drawNodes(g, node.right, fm);
    }
}
