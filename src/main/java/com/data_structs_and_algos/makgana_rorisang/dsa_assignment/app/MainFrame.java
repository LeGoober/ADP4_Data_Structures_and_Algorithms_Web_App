package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.app;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Application window: header bar, grouped navigation list, the active demo, and a shared
 * control strip driving that demo's StepPlayer.
 *
 * Spring injects every DemoPanel bean (ordered by {@code @Order}), so a new demo only needs to
 * be a {@code @Component} subclass of DemoPanel to appear in the menu.
 */
@Component
@Lazy
public class MainFrame extends JFrame {

    private static final int NAV_MIN_WIDTH = 190;

    private final CardLayout cards = new CardLayout();
    private final JPanel cardHost = new JPanel(cards);
    private final JList<String> menu;
    private final Map<String, DemoPanel> demos = new LinkedHashMap<>();
    private DemoPanel current;

    // Navigation grouping: part labels appear as non-selectable header rows.
    private final DefaultListModel<String> menuModel = new DefaultListModel<>();
    private final Set<String> headerRows = new HashSet<>();
    private String lastPartLabel;

    // Shared control strip
    private final CardLayout controlCards = new CardLayout();
    private final JPanel controlHost = new JPanel(controlCards);
    private final JButton playButton = new JButton("Play");
    private final JButton pauseButton = new JButton("Pause");
    private final JButton stepButton = new JButton("Step");
    private final JButton resetButton = new JButton("Reset");
    private final JSlider speedSlider = new JSlider(1, 100, 70);
    private final JLabel statusLabel = new JLabel(" ");

    public MainFrame(List<DemoPanel> panels) {
        super("ADP470S DSA Visualiser");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        menu = new JList<>(menuModel);
        menu.setSelectionModel(new HeaderSkippingSelectionModel());
        menu.setCellRenderer(new MenuRenderer());
        menu.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && menu.getSelectedValue() != null) {
                showDemo(menu.getSelectedValue());
            }
        });

        // Group by part label, keeping the injected (@Order) order within and across parts.
        Map<String, List<DemoPanel>> byPart = new LinkedHashMap<>();
        for (DemoPanel panel : panels) {
            byPart.computeIfAbsent(panel.getPartLabel(), k -> new ArrayList<>()).add(panel);
        }
        byPart.values().forEach(group -> group.forEach(this::registerDemo));

        setContentPane(buildContent());
        wireControls();

        setSize(1280, 820);
        setMinimumSize(new Dimension(960, 620));
        setLocationRelativeTo(null);

        if (!demos.isEmpty()) {
            showDemo(demos.keySet().iterator().next());
        }
    }

    public void registerDemo(DemoPanel panel) {
        String title = panel.getTitle();
        if (!panel.getPartLabel().equals(lastPartLabel)) {
            lastPartLabel = panel.getPartLabel();
            headerRows.add(lastPartLabel);
            menuModel.addElement(lastPartLabel);
        }
        menuModel.addElement(title);
        demos.put(title, panel);
        cardHost.add(panel, title);

        JPanel controls = panel.buildControls();
        controlHost.add(controls != null ? controls : new JPanel(), title);

        panel.addPropertyChangeListener(DemoPanel.STATUS_PROPERTY, e -> {
            if (panel == current) {
                statusLabel.setText(String.valueOf(e.getNewValue()));
            }
        });
    }

    public void showDemo(String title) {
        DemoPanel next = demos.get(title);
        if (next == null || next == current) {
            return;
        }
        if (current != null) {
            current.onHide();
        }
        current = next;
        cards.show(cardHost, title);
        controlCards.show(controlHost, title);
        menu.setSelectedValue(title, true);

        boolean playback = next.hasPlayback();
        playButton.setEnabled(playback);
        pauseButton.setEnabled(playback);
        stepButton.setEnabled(playback);
        resetButton.setEnabled(playback);
        speedSlider.setEnabled(playback);
        next.setDelayMs(currentDelayMs());
        statusLabel.setText(" ");

        next.onShow();
    }

    // ---- Layout ------------------------------------------------------------------------------

    private JPanel buildContent() {
        JScrollPane nav = new JScrollPane(menu);
        nav.setBorder(BorderFactory.createEmptyBorder());
        nav.setMinimumSize(new Dimension(NAV_MIN_WIDTH, 0));
        nav.setPreferredSize(new Dimension(230, 0));

        JSplitPane right = new JSplitPane(JSplitPane.VERTICAL_SPLIT, cardHost, buildControlStrip());
        right.setResizeWeight(0.8);
        right.setContinuousLayout(true);
        right.setBorder(BorderFactory.createEmptyBorder());

        JSplitPane main = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, nav, right);
        main.setResizeWeight(0.1);
        main.setContinuousLayout(true);
        main.setBorder(BorderFactory.createEmptyBorder());

        JPanel content = new JPanel(new BorderLayout());
        content.add(buildHeader(), BorderLayout.NORTH);
        content.add(main, BorderLayout.CENTER);
        return content;
    }

    private JPanel buildHeader() {
        JLabel title = new JLabel("ADP470S — [Rorisang Makgana]");
        title.putClientProperty("FlatLaf.styleClass", "h2");
        JLabel subtitle = new JLabel("Data Structures & Algorithms Visualiser");
        subtitle.putClientProperty("FlatLaf.styleClass", "medium");
        subtitle.setForeground(UIManager.getColor("Label.disabledForeground"));

        JPanel header = new JPanel(new GridBagLayout());
        header.setPreferredSize(new Dimension(0, 56));
        header.setBackground(UIManager.getColor("List.background"));
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, UIManager.getColor("Component.borderColor")),
                BorderFactory.createEmptyBorder(0, 20, 0, 20)));

        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(0, 0, 0, 16);
        header.add(title, c);
        c.weightx = 1;
        c.insets = new Insets(4, 0, 0, 0);
        header.add(subtitle, c);
        return header;
    }

    private JPanel buildControlStrip() {
        JPanel playback = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        playButton.putClientProperty("JButton.buttonType", "default");
        playback.add(playButton);
        playback.add(pauseButton);
        playback.add(stepButton);
        playback.add(resetButton);
        playback.add(new JLabel("   Speed"));
        speedSlider.setPreferredSize(new Dimension(180, speedSlider.getPreferredSize().height));
        playback.add(speedSlider);

        statusLabel.setForeground(UIManager.getColor("Label.disabledForeground"));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));

        JPanel bottomRow = new JPanel(new BorderLayout());
        bottomRow.add(playback, BorderLayout.WEST);
        bottomRow.add(statusLabel, BorderLayout.CENTER);
        bottomRow.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, UIManager.getColor("Component.borderColor")));

        JPanel strip = new JPanel(new BorderLayout());
        strip.setBorder(BorderFactory.createEmptyBorder(6, 8, 4, 8));
        // No scroll pane: each panel's FlowLayout wraps onto extra rows when the strip is narrow.
        controlHost.setMinimumSize(new Dimension(0, 0));
        strip.add(controlHost, BorderLayout.CENTER);
        strip.add(bottomRow, BorderLayout.SOUTH);
        strip.setMinimumSize(new Dimension(0, 110));
        return strip;
    }

    private void wireControls() {
        playButton.addActionListener(e -> { if (current != null) current.play(); });
        pauseButton.addActionListener(e -> { if (current != null) current.pause(); });
        stepButton.addActionListener(e -> { if (current != null) current.stepForward(); });
        resetButton.addActionListener(e -> { if (current != null) current.reset(); });
        speedSlider.addChangeListener(e -> { if (current != null) current.setDelayMs(currentDelayMs()); });
    }

    /** Slider 1 (slow) .. 100 (fast) mapped to a 1000 ms .. 10 ms tick. */
    private int currentDelayMs() {
        return 1010 - speedSlider.getValue() * 10;
    }

    // ---- Navigation list helpers -------------------------------------------------------------

    private boolean isHeader(int index) {
        return index >= 0 && index < menuModel.size() && headerRows.contains(menuModel.get(index));
    }

    /** Single selection that steps over part-label header rows (mouse and keyboard). */
    private class HeaderSkippingSelectionModel extends DefaultListSelectionModel {
        HeaderSkippingSelectionModel() {
            setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        }

        @Override
        public void setSelectionInterval(int anchor, int lead) {
            int target = lead;
            if (isHeader(target)) {
                int direction = target >= getLeadSelectionIndex() ? 1 : -1;
                target = skipHeaders(target, direction);
                if (target < 0) target = skipHeaders(lead, -direction);
                if (target < 0) return;
            }
            super.setSelectionInterval(target, target);
        }

        private int skipHeaders(int index, int direction) {
            while (index >= 0 && index < menuModel.size() && isHeader(index)) {
                index += direction;
            }
            return index >= 0 && index < menuModel.size() ? index : -1;
        }
    }

    private class MenuRenderer extends DefaultListCellRenderer {
        @Override
        public java.awt.Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            boolean header = isHeader(index);
            JLabel label = (JLabel) super.getListCellRendererComponent(
                    list, value, index, isSelected && !header, cellHasFocus && !header);
            if (header) {
                label.setFont(label.getFont().deriveFont(Font.BOLD, label.getFont().getSize2D() - 1f));
                label.setForeground(UIManager.getColor("Label.disabledForeground"));
                label.setBorder(BorderFactory.createEmptyBorder(index == 0 ? 8 : 14, 10, 4, 8));
            } else {
                label.setBorder(BorderFactory.createEmptyBorder(5, 22, 5, 8));
            }
            return label;
        }
    }
}
