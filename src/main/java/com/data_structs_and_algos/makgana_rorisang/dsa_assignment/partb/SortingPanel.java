package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.partb;

import com.data_structs_and_algos.makgana_rorisang.dsa_assignment.app.DemoPanel;
import com.data_structs_and_algos.makgana_rorisang.dsa_assignment.app.StepPlayer;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.util.List;
import java.util.Random;

/** Sorts a copy with a SortTraceRecorder attached, then replays the steps onto the bars. */
@Component
@Lazy
@Order(20)
public class SortingPanel extends DemoPanel {

    private static final Color BAR = new Color(0x8FA8C8);
    private static final Color COMPARE = new Color(0xF2A541);
    private static final Color SWAP = new Color(0xE05252);
    private static final Color SORTED = new Color(0x4CAF6E);

    private final List<Sorter> sorters;
    private final Random random = new Random();

    private int[] displayArray;
    private int[] originalArray;
    private int highlightA = -1;
    private int highlightB = -1;
    private SortStepType lastType;
    private boolean[] sortedFlags;
    private JComboBox<Sorter> sorterBox;
    private JSpinner sizeSpinner;
    private final StepPlayer<SortStep> player;

    public SortingPanel(List<Sorter> sorters) {
        this.sorters = sorters;
        this.player = new StepPlayer<>(this::applyStep, () -> {
            highlightA = highlightB = -1;
            showStatus("Replay finished");
            repaint();
        });
        shuffle(50);
    }

    @Override
    public String getTitle() {
        return "Sorting";
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
    protected JPanel buildControls() {
        sorterBox = new JComboBox<>(sorters.toArray(new Sorter[0]));
        sorterBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                                   boolean isSelected, boolean cellHasFocus) {
                Object shown = value instanceof Sorter s ? s.getName() : value;
                return super.getListCellRendererComponent(list, shown, index, isSelected, cellHasFocus);
            }
        });
        sizeSpinner = new JSpinner(new SpinnerNumberModel(50, 5, 120, 5));

        JButton shuffleButton = new JButton("Shuffle");
        shuffleButton.addActionListener(e -> {
            player.load(List.of());
            shuffle((Integer) sizeSpinner.getValue());
            showStatus(" ");
        });
        JButton sortButton = new JButton("Sort");
        sortButton.addActionListener(e -> runSort());

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        controls.add(new JLabel("Algorithm"));
        controls.add(sorterBox);
        controls.add(new JLabel("Elements"));
        controls.add(sizeSpinner);
        controls.add(shuffleButton);
        controls.add(sortButton);
        return controls;
    }

    private void shuffle(int size) {
        originalArray = new int[size];
        for (int i = 0; i < size; i++) {
            originalArray[i] = 5 + random.nextInt(96);
        }
        restoreOriginal();
    }

    private void restoreOriginal() {
        displayArray = originalArray.clone();
        sortedFlags = new boolean[displayArray.length];
        highlightA = highlightB = -1;
        lastType = null;
        repaint();
    }

    private void runSort() {
        Sorter sorter = (Sorter) sorterBox.getSelectedItem();
        if (sorter == null) return;
        restoreOriginal();
        attempt(sorter.getName(), () -> {
            SortTraceRecorder recorder = new SortTraceRecorder();
            sorter.sort(originalArray.clone(), recorder);
            List<SortStep> steps = recorder.getSteps();
            player.load(steps);
            showStatus(sorter.getName() + ": " + steps.size() + " steps recorded");
            player.play();
        });
    }

    private void applyStep(SortStep s) {
        lastType = s.type;
        switch (s.type) {
            case COMPARE -> {
                highlightA = s.i;
                highlightB = s.j;
            }
            case SWAP -> {
                int tmp = displayArray[s.i];
                displayArray[s.i] = displayArray[s.j];
                displayArray[s.j] = tmp;
                highlightA = s.i;
                highlightB = s.j;
            }
            case SORTED -> {
                sortedFlags[s.i] = true;
                highlightA = highlightB = -1;
            }
        }
        repaint();
    }

    @Override
    public void reset() {
        player.reset();
        restoreOriginal();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintSafely(g, g2 -> {
            int n = displayArray.length;
            int margin = 20;
            int w = getWidth() - 2 * margin;
            int h = getHeight() - 2 * margin;
            double barWidth = (double) w / n;
            for (int i = 0; i < n; i++) {
                int barHeight = (int) (h * displayArray[i] / 100.0);
                int x = margin + (int) (i * barWidth);
                Color c = BAR;
                if (sortedFlags[i]) c = SORTED;
                if (i == highlightA || i == highlightB) c = lastType == SortStepType.SWAP ? SWAP : COMPARE;
                g2.setColor(c);
                g2.fillRect(x + 1, margin + h - barHeight, Math.max(1, (int) barWidth - 2), barHeight);
            }
        });
    }
}
