package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.parte;

import com.data_structs_and_algos.makgana_rorisang.dsa_assignment.app.DemoPanel;
import com.data_structs_and_algos.makgana_rorisang.dsa_assignment.app.StepPlayer;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Map of the transport network (left) and its adjacency matrix (right); replays DFS / BFS / shortest path. */
@Component
@Lazy
@Order(90)
public class GraphPanel extends DemoPanel {

    private static final int R = 9;

    private final TransportNetwork network;
    private final GraphTraversal traversal;
    private final ShortestPathFinder finder;
    private final StepPlayer<GraphStep> player;
    private JComboBox<String> fromBox;
    private JComboBox<String> toBox;
    private final JTable matrixTable;
    private final JScrollPane matrixScroll;
    private final Set<Integer> visited = new HashSet<>();
    private List<Integer> pathToHighlight = new ArrayList<>();

    private final Set<Long> exploredEdges = new HashSet<>();
    private final List<Integer> queueView = new ArrayList<>();
    private List<Integer> pendingPath = new ArrayList<>();
    private int currentVertex = -1;

    public GraphPanel() {
        network = new TransportNetwork();
        traversal = new GraphTraversal(network.getGraph());
        finder = new ShortestPathFinder(network.getGraph());
        player = new StepPlayer<>(this::applyStep, () -> {
            pathToHighlight = pendingPath;
            currentVertex = -1;
            showStatus("Replay finished");
            repaint();
        });

        matrixTable = new JTable();
        matrixTable.setEnabled(false);
        matrixTable.setRowHeight(22);
        matrixScroll = new JScrollPane(matrixTable);
        matrixScroll.setPreferredSize(new Dimension(420, 0));
        matrixScroll.setBorder(BorderFactory.createTitledBorder("Adjacency matrix"));
        add(matrixScroll, BorderLayout.EAST);
    }

    @Override
    public String getTitle() {
        return "Transport Network";
    }

    @Override
    public String getPartLabel() {
        return "Part E — Graphs";
    }

    @Override
    protected StepPlayer<?> getPlayer() {
        return player;
    }

    @Override
    protected JPanel buildControls() {
        fromBox = new JComboBox<>();
        toBox = new JComboBox<>();

        JButton dfs = new JButton("DFS");
        dfs.addActionListener(e -> runTraversal("DFS", false));
        JButton bfs = new JButton("BFS");
        bfs.addActionListener(e -> runTraversal("BFS", true));
        JButton path = new JButton("Shortest path");
        path.addActionListener(e -> runShortestPath());

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        controls.add(new JLabel("From"));
        controls.add(fromBox);
        controls.add(new JLabel("To"));
        controls.add(toBox);
        controls.add(dfs);
        controls.add(bfs);
        controls.add(path);
        return controls;
    }

    /** City names and the matrix come from Graph, so they appear once Graph is implemented. */
    @Override
    public void onShow() {
        Graph graph = network.getGraph();
        attempt("Graph", () -> {
            int n = graph.vertexCount();
            if (fromBox.getItemCount() != n) {
                fromBox.removeAllItems();
                toBox.removeAllItems();
                for (int v = 0; v < n; v++) {
                    fromBox.addItem(graph.nameOf(v));
                    toBox.addItem(graph.nameOf(v));
                }
                if (n > 1) toBox.setSelectedIndex(n - 1);
            }
            int[][] m = graph.matrixCopy();
            String[] headers = new String[n + 1];
            headers[0] = "";
            Object[][] rows = new Object[n][n + 1];
            for (int i = 0; i < n; i++) {
                String name = graph.nameOf(i);
                headers[i + 1] = name.length() > 3 ? name.substring(0, 3) : name;
                rows[i][0] = name;
                for (int j = 0; j < n; j++) rows[i][j + 1] = m[i][j];
            }
            matrixTable.setModel(new DefaultTableModel(rows, headers));
            matrixTable.getColumnModel().getColumn(0).setPreferredWidth(110);
        });
    }

    private void clearTrace() {
        visited.clear();
        exploredEdges.clear();
        queueView.clear();
        pathToHighlight = new ArrayList<>();
        pendingPath = new ArrayList<>();
        currentVertex = -1;
    }

    private void runTraversal(String name, boolean breadthFirst) {
        int start = fromBox.getSelectedIndex();
        if (start < 0) {
            showStatus("No cities yet: implement Graph and add the routes in TransportNetwork");
            return;
        }
        clearTrace();
        attempt(name, () -> {
            GraphTraceRecorder recorder = new GraphTraceRecorder();
            List<Integer> order = breadthFirst ? traversal.bfs(start, recorder) : traversal.dfs(start, recorder);
            player.load(recorder.getSteps());
            showStatus(name + " order: " + names(order));
            player.play();
        });
        repaint();
    }

    private void runShortestPath() {
        int src = fromBox.getSelectedIndex();
        int dst = toBox.getSelectedIndex();
        if (src < 0 || dst < 0) {
            showStatus("No cities yet: implement Graph and add the routes in TransportNetwork");
            return;
        }
        clearTrace();
        attempt("Shortest path", () -> {
            GraphTraceRecorder recorder = new GraphTraceRecorder();
            pendingPath = finder.findPath(src, dst, recorder);
            player.load(recorder.getSteps());
            showStatus("Path: " + (pendingPath.isEmpty() ? "unreachable" : names(pendingPath)));
            player.play();
        });
        repaint();
    }

    private String names(List<Integer> vertices) {
        List<String> out = new ArrayList<>();
        for (int v : vertices) out.add(network.getGraph().nameOf(v));
        return String.join(" → ", out);
    }

    private void applyStep(GraphStep s) {
        switch (s.type) {
            case VISIT -> {
                visited.add(s.from);
                currentVertex = s.from;
            }
            case EDGE -> exploredEdges.add(edgeKey(s.from, s.to));
            case ENQUEUE -> queueView.add(s.from);
            case DEQUEUE -> queueView.remove(Integer.valueOf(s.from));
        }
        repaint();
    }

    private static long edgeKey(int a, int b) {
        return ((long) Math.min(a, b) << 32) | Math.max(a, b);
    }

    @Override
    public void reset() {
        player.reset();
        clearTrace();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintSafely(g, g2 -> {
            Graph graph = network.getGraph();
            int n = graph.vertexCount();
            if (n == 0) {
                paintMessage(g2, "No cities in the graph.");
                return;
            }

            // Fit the network's coordinates into the area left of the matrix table.
            int mapW = matrixScroll.getX() - 40;
            int mapH = getHeight() - 70;
            int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;
            for (int v = 0; v < n; v++) {
                Point p = network.positionOf(v);
                minX = Math.min(minX, p.x); maxX = Math.max(maxX, p.x);
                minY = Math.min(minY, p.y); maxY = Math.max(maxY, p.y);
            }
            double scale = Math.min((double) (mapW - 80) / Math.max(1, maxX - minX), (double) (mapH - 40) / Math.max(1, maxY - minY));
            Point[] screen = new Point[n];
            for (int v = 0; v < n; v++) {
                Point p = network.positionOf(v);
                screen[v] = new Point(40 + (int) ((p.x - minX) * scale), 30 + (int) ((p.y - minY) * scale));
            }

            // Edges
            for (int u = 0; u < n; u++) {
                for (int v = u + 1; v < n; v++) {
                    if (!graph.hasEdge(u, v)) continue;
                    boolean explored = exploredEdges.contains(edgeKey(u, v));
                    g2.setStroke(new BasicStroke(explored ? 2.5f : 1.2f));
                    g2.setColor(explored ? new Color(0xE07B39) : new Color(0xC8C8C8));
                    g2.drawLine(screen[u].x, screen[u].y, screen[v].x, screen[v].y);
                }
            }
            // Final path
            g2.setStroke(new BasicStroke(5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.setColor(new Color(0x2E8B57));
            for (int i = 0; i + 1 < pathToHighlight.size(); i++) {
                Point a = screen[pathToHighlight.get(i)];
                Point b = screen[pathToHighlight.get(i + 1)];
                g2.drawLine(a.x, a.y, b.x, b.y);
            }
            // Cities
            FontMetrics fm = g2.getFontMetrics();
            g2.setStroke(new BasicStroke(1.5f));
            for (int v = 0; v < n; v++) {
                Point p = screen[v];
                Color fill = v == currentVertex ? new Color(0xF8D58C)
                        : visited.contains(v) ? new Color(0x3A6FC4) : Color.WHITE;
                g2.setColor(fill);
                g2.fillOval(p.x - R, p.y - R, 2 * R, 2 * R);
                g2.setColor(new Color(0x3A6FC4));
                g2.drawOval(p.x - R, p.y - R, 2 * R, 2 * R);
                g2.setColor(Color.DARK_GRAY);
                g2.drawString(graph.nameOf(v), p.x + R + 4, p.y + fm.getAscent() / 2 - 2);
            }

            if (!queueView.isEmpty()) {
                g2.setColor(Color.DARK_GRAY);
                g2.drawString("Queue: " + names(queueView), 20, getHeight() - 16);
            }
        });
    }
}
