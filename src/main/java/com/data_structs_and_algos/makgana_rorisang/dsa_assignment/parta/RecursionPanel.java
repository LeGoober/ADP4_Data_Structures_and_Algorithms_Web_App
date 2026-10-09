package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.parta;

import com.data_structs_and_algos.makgana_rorisang.dsa_assignment.app.DemoPanel;
import com.data_structs_and_algos.makgana_rorisang.dsa_assignment.app.StepPlayer;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Draws the recursion call tree growing as calls enter and filling in as they return. */
@Component
@Lazy
@Order(10)
public class RecursionPanel extends DemoPanel {

    /** Fibonacci(7) makes 41 calls; beyond this only the counts are shown. */
    private static final int MAX_DRAWN_CALLS = 63;
    private static final int NODE_HEIGHT = 26;

    private final Map<String, RecursiveAlgorithm> algorithms = new LinkedHashMap<>();

    private JComboBox<String> algorithmBox;
    private JSpinner nSpinner;
    private CallTreeRecorder recorder;
    private final StepPlayer<CallEvent> player;
    private JLabel statsLabel;

    private CallNode treeRoot;
    private final Set<CallNode> entered = Collections.newSetFromMap(new IdentityHashMap<>());
    private final Set<CallNode> returned = Collections.newSetFromMap(new IdentityHashMap<>());
    private final Map<CallNode, Integer> leafCounts = new IdentityHashMap<>();
    private int levelGap = 60;

    public RecursionPanel(List<RecursiveAlgorithm> algorithmBeans) {
        algorithmBeans.forEach(a -> algorithms.put(a.getClass().getSimpleName(), a));
        player = new StepPlayer<>(this::applyEvent, () -> showStatus("Replay finished"));
    }

    @Override
    public String getTitle() {
        return "Recursion (Factorial / Fibonacci)";
    }

    @Override
    public String getPartLabel() {
        return "Part A — Recursion";
    }

    @Override
    protected StepPlayer<?> getPlayer() {
        return player;
    }

    @Override
    protected JPanel buildControls() {
        algorithmBox = new JComboBox<>(algorithms.keySet().toArray(new String[0]));
        nSpinner = new JSpinner(new SpinnerNumberModel(5, 0, 20, 1));
        statsLabel = new JLabel("calls: –   max depth: –");

        JButton runRecursive = new JButton("Run recursive");
        runRecursive.addActionListener(e -> runRecursive());
        JButton runIterative = new JButton("Run iterative");
        runIterative.addActionListener(e -> runIterative());

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        controls.add(new JLabel("Algorithm"));
        controls.add(algorithmBox);
        controls.add(new JLabel("n"));
        controls.add(nSpinner);
        controls.add(runRecursive);
        controls.add(runIterative);
        controls.add(statsLabel);
        return controls;
    }

    private RecursiveAlgorithm selected() {
        return algorithms.get((String) algorithmBox.getSelectedItem());
    }

    private void runRecursive() {
        RecursiveAlgorithm algorithm = selected();
        int n = (Integer) nSpinner.getValue();
        String name = algorithmBox.getSelectedItem() + ".recursive(" + n + ")";
        attempt(name, () -> {
            recorder = new CallTreeRecorder();
            algorithm.resetCount();
            algorithm.setListener(recorder);
            long result = algorithm.recursive(n);

            statsLabel.setText("result: " + result + "   calls: " + algorithm.getCallCount()
                    + "   max depth: " + recorder.getMaxDepth());
            entered.clear();
            returned.clear();
            leafCounts.clear();

            List<CallEvent> events = recorder.getEvents();
            if (events.size() / 2 > MAX_DRAWN_CALLS) {
                treeRoot = null;
                player.load(List.of());
                showStatus("Too many calls to draw (" + events.size() / 2 + "); showing counts only");
            } else {
                treeRoot = recorder.getRoot();
                player.load(events);
                player.play();
                showStatus(name + " = " + result);
            }
            repaint();
        });
    }

    private void runIterative() {
        RecursiveAlgorithm algorithm = selected();
        int n = (Integer) nSpinner.getValue();
        String name = algorithmBox.getSelectedItem() + ".iterative(" + n + ")";
        attempt(name, () -> {
            long result = algorithm.iterative(n);
            statsLabel.setText("iterative result: " + result);
            showStatus(name + " = " + result);
        });
    }

    private void applyEvent(CallEvent e) {
        if (e.type == EventType.ENTER) {
            entered.add(e.node);
        } else {
            returned.add(e.node);
        }
        repaint();
    }

    @Override
    public void reset() {
        player.reset();
        entered.clear();
        returned.clear();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintSafely(g, g2 -> {
            if (treeRoot == null) {
                paintMessage(g2, "Pick an algorithm and n, then Run recursive.");
                return;
            }
            int depth = depthOf(treeRoot);
            levelGap = Math.max(NODE_HEIGHT + 4, Math.min(70, (getHeight() - 60) / Math.max(1, depth)));
            drawNode(g2, treeRoot, getWidth() / 2, 30, getWidth() - 40);
        });
    }

    /** Lays children out across {@code spread} pixels in proportion to their leaf counts. */
    private void drawNode(Graphics2D g, CallNode node, int x, int y, int spread) {
        if (!entered.contains(node)) {
            return;
        }
        int total = Math.max(1, leafCount(node));
        int left = x - spread / 2;
        for (CallNode child : node.children) {
            int childSpread = spread * leafCount(child) / total;
            int cx = left + childSpread / 2;
            if (entered.contains(child)) {
                g.setColor(uiColor("Component.borderColor", Color.GRAY));
                g.setStroke(new BasicStroke(1.5f));
                g.drawLine(x, y + NODE_HEIGHT / 2, cx, y + levelGap - NODE_HEIGHT / 2);
            }
            drawNode(g, child, cx, y + levelGap, childSpread);
            left += childSpread;
        }

        String label = node.label != null ? node.label : "f(" + node.n + ")";
        if (returned.contains(node)) {
            label += " = " + node.result;
        }
        FontMetrics fm = g.getFontMetrics();
        int w = Math.max(34, Math.min(fm.stringWidth(label) + 14, Math.max(34, spread - 4)));
        int h = NODE_HEIGHT;

        boolean done = returned.contains(node);
        g.setColor(done ? new Color(0xD7F0DD) : new Color(0xDCE8FA));
        g.fillRoundRect(x - w / 2, y - h / 2, w, h, 10, 10);
        g.setColor(done ? new Color(0x2E8B57) : new Color(0x3A6FC4));
        g.setStroke(new BasicStroke(1.5f));
        g.drawRoundRect(x - w / 2, y - h / 2, w, h, 10, 10);
        g.setColor(Color.DARK_GRAY);
        String shown = fm.stringWidth(label) > w - 6 ? (node.label != null ? node.label : "" + node.n) : label;
        g.drawString(shown, x - fm.stringWidth(shown) / 2, y + fm.getAscent() / 2 - 2);
    }

    private int leafCount(CallNode node) {
        Integer cached = leafCounts.get(node);
        if (cached != null) return cached;
        int count = 0;
        for (CallNode child : node.children) count += leafCount(child);
        count = Math.max(1, count);
        leafCounts.put(node, count);
        return count;
    }

    private int depthOf(CallNode node) {
        int max = 0;
        for (CallNode child : node.children) max = Math.max(max, depthOf(child));
        return max + 1;
    }
}
