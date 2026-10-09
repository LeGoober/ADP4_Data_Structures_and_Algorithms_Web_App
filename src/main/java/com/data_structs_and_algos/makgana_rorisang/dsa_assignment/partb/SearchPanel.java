package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.partb;

import com.data_structs_and_algos.makgana_rorisang.dsa_assignment.app.DemoPanel;
import com.data_structs_and_algos.makgana_rorisang.dsa_assignment.app.StepPlayer;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** Draws the sorted array as boxes with lo / mid / hi markers, replaying each probe. */
@Component
@Lazy
@Order(30)
public class SearchPanel extends DemoPanel {

    private static final int SIZE = 31;

    private final BinarySearch binarySearch;
    private final List<Sorter> sorters;

    private int[] sortedData;
    private JTextField keyField;
    private JRadioButton iterativeButton;
    private JRadioButton recursiveButton;
    private final List<Probe> probes = new ArrayList<>();
    private final StepPlayer<Probe> player;

    private Probe currentProbe;
    private int resultIndex = Integer.MIN_VALUE;
    private int pendingResult;

    public SearchPanel(BinarySearch binarySearch, List<Sorter> sorters) {
        this.binarySearch = binarySearch;
        this.sorters = sorters;
        this.player = new StepPlayer<>(p -> {
            currentProbe = p;
            repaint();
        }, () -> {
            resultIndex = pendingResult;
            showStatus(resultIndex >= 0 ? "Found at index " + resultIndex : "Not found (" + resultIndex + ")");
            repaint();
        });
    }

    @Override
    public String getTitle() {
        return "Binary Search";
    }

    @Override
    public String getPartLabel() {
        return "Part B — Sorting & Searching";
    }

    @Override
    protected StepPlayer<?> getPlayer() {
        return player;
    }

    @Override
    public void onShow() {
        if (sortedData == null) newData();
    }

    @Override
    protected JPanel buildControls() {
        keyField = new JTextField(6);
        iterativeButton = new JRadioButton("Iterative", true);
        recursiveButton = new JRadioButton("Recursive");
        ButtonGroup group = new ButtonGroup();
        group.add(iterativeButton);
        group.add(recursiveButton);

        JButton newDataButton = new JButton("New sorted data");
        newDataButton.addActionListener(e -> newData());
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> search());
        keyField.addActionListener(e -> search());

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        controls.add(newDataButton);
        controls.add(new JLabel("Key"));
        controls.add(keyField);
        controls.add(iterativeButton);
        controls.add(recursiveButton);
        controls.add(searchButton);
        return controls;
    }

    /** Random data sorted with the first of your sorters that is implemented. */
    private void newData() {
        int[] data = new Random().ints(SIZE, 1, 100).toArray();
        player.load(List.of());
        currentProbe = null;
        resultIndex = Integer.MIN_VALUE;
        for (Sorter sorter : sorters) {
            try {
                int[] copy = data.clone();
                sorter.sort(copy, SortListener.NO_OP);
                sortedData = copy;
                showStatus("Data sorted with " + sorter.getName());
                repaint();
                return;
            } catch (UnsupportedOperationException ignored) {
                // try the next sorter
            }
        }
        sortedData = null;
        showStatus("Binary search needs sorted data: implement at least one Sorter first");
        repaint();
    }

    private void search() {
        if (sortedData == null) {
            newData();
            if (sortedData == null) return;
        }
        int key;
        try {
            key = Integer.parseInt(keyField.getText().trim());
        } catch (NumberFormatException e) {
            showStatus("Enter an integer key");
            return;
        }
        boolean recursive = recursiveButton.isSelected();
        attempt(recursive ? "BinarySearch.recursive" : "BinarySearch.iterative", () -> {
            probes.clear();
            currentProbe = null;
            resultIndex = Integer.MIN_VALUE;
            SearchListener listener = (lo, mid, hi) -> probes.add(new Probe(lo, mid, hi));
            pendingResult = recursive
                    ? binarySearch.recursive(sortedData, key, listener)
                    : binarySearch.iterative(sortedData, key, listener);
            player.load(probes);
            showStatus(probes.size() + " probes recorded");
            player.play();
        });
    }

    @Override
    public void reset() {
        player.reset();
        currentProbe = null;
        resultIndex = Integer.MIN_VALUE;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintSafely(g, g2 -> {
            if (sortedData == null) {
                paintMessage(g2, "No sorted data yet.");
                return;
            }
            int n = sortedData.length;
            int margin = 20;
            int box = Math.max(18, Math.min(48, (getWidth() - 2 * margin) / n));
            int x0 = (getWidth() - box * n) / 2;
            int y = getHeight() / 2 - box;
            FontMetrics fm = g2.getFontMetrics();

            for (int i = 0; i < n; i++) {
                int x = x0 + i * box;
                boolean inRange = currentProbe == null || (i >= currentProbe.lo && i <= currentProbe.hi);
                Color fill = inRange ? new Color(0xE8EEF8) : new Color(0xF2F2F2);
                if (currentProbe != null && i == currentProbe.mid) fill = new Color(0xF8D58C);
                if (i == resultIndex) fill = new Color(0x9FDCB0);
                g2.setColor(fill);
                g2.fillRect(x, y, box, box);
                g2.setColor(inRange ? Color.GRAY : new Color(0xD0D0D0));
                g2.drawRect(x, y, box, box);
                String v = String.valueOf(sortedData[i]);
                g2.setColor(inRange ? Color.DARK_GRAY : Color.LIGHT_GRAY);
                g2.drawString(v, x + (box - fm.stringWidth(v)) / 2, y + box / 2 + fm.getAscent() / 2 - 2);
                g2.setColor(Color.GRAY);
                String idx = String.valueOf(i);
                g2.drawString(idx, x + (box - fm.stringWidth(idx)) / 2, y - 6);
            }

            if (currentProbe != null) {
                drawMarker(g2, "lo", currentProbe.lo, x0, box, y + box, new Color(0x3A6FC4), 0);
                drawMarker(g2, "mid", currentProbe.mid, x0, box, y + box, new Color(0xD08A10), 1);
                drawMarker(g2, "hi", currentProbe.hi, x0, box, y + box, new Color(0x3A6FC4), 2);
            }
        });
    }

    private void drawMarker(Graphics2D g, String label, int index, int x0, int box, int top, Color c, int row) {
        if (index < 0 || index >= sortedData.length) return;
        int cx = x0 + index * box + box / 2;
        int y = top + 14 + row * 18;
        g.setColor(c);
        g.setStroke(new BasicStroke(1.5f));
        g.drawLine(cx, top + 2, cx, y - 10);
        g.drawString(label, cx - g.getFontMetrics().stringWidth(label) / 2, y + 2);
    }
}
