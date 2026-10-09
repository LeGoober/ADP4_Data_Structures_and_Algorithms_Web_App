package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.partb;

import com.data_structs_and_algos.makgana_rorisang.dsa_assignment.app.DemoPanel;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/** Runs every Sorter on the same 1,000-integer dataset in a SwingWorker; table + bar chart. */
@Component
@Lazy
@Order(40)
public class BenchmarkPanel extends DemoPanel {

    private static final String RANDOM = "Random (1,000)";
    private static final String SORTED = "Already sorted (1,000)";
    private static final String REVERSED = "Reversed (1,000)";
    private static final String CSV = "Load CSV…";
    private static final int N = 1000;

    private final List<Sorter> sorters;
    private final Benchmark benchmark = new Benchmark(3, 5);
    private final JTable resultsTable;
    private final BarChart chart = new BarChart();
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"Algorithm", "Best (ms)", "Average (ms)", "Comparisons", "Swaps"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private JComboBox<String> datasetBox;
    private JButton runButton;
    private JButton exportButton;

    private List<BenchmarkResult> lastResults = new ArrayList<>();

    public BenchmarkPanel(List<Sorter> sorters) {
        this.sorters = sorters;
        resultsTable = new JTable(tableModel);
        resultsTable.setFillsViewportHeight(true);

        JScrollPane tableScroll = new JScrollPane(resultsTable);
        tableScroll.setPreferredSize(new Dimension(0, 140));
        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, chart, tableScroll);
        split.setResizeWeight(0.7);
        split.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        add(split, BorderLayout.CENTER);
    }

    @Override
    public String getTitle() {
        return "Benchmark";
    }

    @Override
    public String getPartLabel() {
        return "Part B — Sorting & Searching";
    }

    @Override
    protected JPanel buildControls() {
        datasetBox = new JComboBox<>(new String[]{RANDOM, SORTED, REVERSED, CSV});
        runButton = new JButton("Run benchmark");
        runButton.addActionListener(e -> runInBackground());
        exportButton = new JButton("Export CSV…");
        exportButton.setEnabled(false);
        exportButton.addActionListener(e -> export());

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        controls.add(new JLabel("Dataset"));
        controls.add(datasetBox);
        controls.add(runButton);
        controls.add(exportButton);
        controls.add(new JLabel("  " + sorters.size() + " sorters registered"));
        return controls;
    }

    private void runInBackground() {
        String choice = (String) datasetBox.getSelectedItem();
        Path csv = null;
        if (CSV.equals(choice)) {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;
            csv = chooser.getSelectedFile().toPath();
        }
        Path csvFile = csv;

        runButton.setEnabled(false);
        showStatus("Running benchmark…");
        new SwingWorker<List<BenchmarkResult>, Void>() {
            @Override
            protected List<BenchmarkResult> doInBackground() {
                int[] data = switch (choice) {
                    case SORTED -> DatasetLoader.sorted(N);
                    case REVERSED -> DatasetLoader.reversed(N);
                    case CSV -> DatasetLoader.loadCsv(csvFile);
                    default -> DatasetLoader.random(N, 42L);
                };
                return benchmark.runAll(sorters, data);
            }

            @Override
            protected void done() {
                runButton.setEnabled(true);
                try {
                    showResults(get());
                    showStatus("Benchmark finished");
                } catch (ExecutionException e) {
                    Throwable cause = e.getCause();
                    showStatus(cause instanceof UnsupportedOperationException
                            ? "Benchmark: not implemented yet (" + cause.getMessage() + ")"
                            : "Benchmark failed: " + cause);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }.execute();
    }

    private void showResults(List<BenchmarkResult> results) {
        lastResults = results == null ? new ArrayList<>() : results;
        tableModel.setRowCount(0);
        for (BenchmarkResult r : lastResults) {
            tableModel.addRow(new Object[]{
                    r.algorithm,
                    String.format("%.3f", r.bestNanos / 1_000_000.0),
                    String.format("%.3f", r.averageNanos / 1_000_000.0),
                    r.comparisons,
                    r.swaps});
        }
        chart.setResults(lastResults);
        exportButton.setEnabled(!lastResults.isEmpty());
    }

    private void export() {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new java.io.File("benchmark-results.csv"));
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
        Path file = chooser.getSelectedFile().toPath();
        attempt("Export CSV", () -> {
            Benchmark.exportCsv(lastResults, file);
            showStatus("Exported to " + file);
        });
    }
}
