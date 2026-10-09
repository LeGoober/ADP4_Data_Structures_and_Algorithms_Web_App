"""
Plots the Part B sorting benchmark produced by BenchmarkCli.

Usage:  python plot_sorts.py [output_directory_for_png_files]

Inputs (same folder as this script):
    sort_runs_n1000.csv     1,000 timed samples per algorithm on 1,000 random integers
    sort_results_n1000.csv  mean / stddev / best time, comparisons and swaps
    sort_scaling.csv        mean time against n (Big-O growth)
    sort_shapes_n1000.csv   random vs sorted vs reversed input
"""
import sys
from pathlib import Path

import matplotlib.pyplot as plt
import numpy as np
import pandas as pd

HERE = Path(__file__).resolve().parent
OUT = Path(sys.argv[1]) if len(sys.argv) > 1 else HERE
OUT.mkdir(parents=True, exist_ok=True)

COLOURS = {"Selection Sort": "#d95f02", "Quick Sort": "#1b9e77", "Heap Sort": "#7570b3"}
ORDER = ["Selection Sort", "Quick Sort", "Heap Sort"]

runs = pd.read_csv(HERE / "sort_runs_n1000.csv")
summary = pd.read_csv(HERE / "sort_results_n1000.csv").set_index("algorithm").loc[ORDER]
scaling = pd.read_csv(HERE / "sort_scaling.csv")
shapes = pd.read_csv(HERE / "sort_shapes_n1000.csv")

plt.rcParams.update({"font.size": 10, "axes.spines.top": False, "axes.spines.right": False})

# 1. The 1,000 timed samples per algorithm (microseconds), plus the median
fig, ax = plt.subplots(figsize=(8, 4.5))
for name in ORDER:
    t = runs[runs.algorithm == name].sort_values("sample")["nanos"] / 1000
    ax.plot(range(1, len(t) + 1), t, ".", ms=2, alpha=0.35, color=COLOURS[name])
    ax.axhline(t.median(), color=COLOURS[name], lw=1.6, label=f"{name} (median {t.median():.0f} µs)")
ax.set_yscale("log")
ax.set_xlabel("Sample number (1 sort of the same 1,000 random integers)")
ax.set_ylabel("Execution time (µs, log scale)")
ax.set_title("Sorting 1,000 random integers: 1,000 timed samples per algorithm")
ax.legend(loc="upper center", bbox_to_anchor=(0.5, -0.15), ncol=3, frameon=False, fontsize=8)
fig.tight_layout()
fig.savefig(OUT / "sort_samples_n1000.png", dpi=200)
plt.close(fig)

# 2. Median / mean time bar chart
fig, ax = plt.subplots(figsize=(6.5, 4.5))
medians = [runs[runs.algorithm == n]["nanos"].median() / 1000 for n in ORDER]
means = [summary.loc[n, "mean_ns"] / 1000 for n in ORDER]
x = np.arange(len(ORDER))
ax.bar(x - 0.2, medians, 0.4, label="Median", color=[COLOURS[n] for n in ORDER])
ax.bar(x + 0.2, means, 0.4, label="Mean", color=[COLOURS[n] for n in ORDER], alpha=0.5, hatch="//")
for xi, v in zip(x - 0.2, medians):
    ax.text(xi, v, f"{v:.0f}", ha="center", va="bottom", fontsize=9)
for xi, v in zip(x + 0.2, means):
    ax.text(xi, v, f"{v:.0f}", ha="center", va="bottom", fontsize=9)
ax.set_xticks(x, ORDER)
ax.set_ylabel("Execution time (µs)")
ax.set_title("Average execution time, n = 1,000")
ax.legend(frameon=False)
fig.tight_layout()
fig.savefig(OUT / "sort_times_n1000.png", dpi=200)
plt.close(fig)

# 3. Scaling with reference curves
fig, ax = plt.subplots(figsize=(7, 4.5))
for name in ORDER:
    d = scaling[scaling.algorithm == name]
    ax.plot(d.n, d.mean_ns / 1000, "o-", color=COLOURS[name], label=name)
n = scaling.n.unique()
ref = scaling[(scaling.algorithm == "Selection Sort") & (scaling.n == 1000)].mean_ns.iloc[0] / 1000
ax.plot(n, ref * (n / 1000) ** 2, "k--", lw=0.8, label="O(n²) reference")
q = scaling[(scaling.algorithm == "Quick Sort") & (scaling.n == 1000)].mean_ns.iloc[0] / 1000
ax.plot(n, q * (n * np.log2(n)) / (1000 * np.log2(1000)), "k:", lw=1, label="O(n log n) reference")
ax.set_xscale("log")
ax.set_yscale("log")
ax.set_xlabel("Input size n (log scale)")
ax.set_ylabel("Mean execution time (µs, log scale)")
ax.set_title("Growth of execution time with input size")
ax.legend(frameon=False)
fig.tight_layout()
fig.savefig(OUT / "sort_scaling.png", dpi=200)
plt.close(fig)

# 4. Comparisons and swaps (theory made visible)
fig, axes = plt.subplots(1, 2, figsize=(9, 4))
for ax, col, title in zip(axes, ["comparisons", "swaps"], ["Comparisons", "Swaps"]):
    vals = summary[col]
    ax.bar(ORDER, vals, color=[COLOURS[n] for n in ORDER])
    ax.set_yscale("log")
    ax.set_title(f"{title}, n = 1,000")
    for i, v in enumerate(vals):
        ax.text(i, v, f"{v:,}", ha="center", va="bottom", fontsize=9)
    ax.tick_params(axis="x", labelsize=8)
fig.tight_layout()
fig.savefig(OUT / "sort_ops.png", dpi=200)
plt.close(fig)

# 5. Input shape
fig, ax = plt.subplots(figsize=(7, 4.2))
w = 0.25
for k, name in enumerate(ORDER):
    d = shapes[shapes.algorithm == name].set_index("input").loc[["random", "sorted", "reversed"]]
    ax.bar(np.arange(3) + (k - 1) * w, d.mean_ns / 1000, w, label=name, color=COLOURS[name])
ax.set_xticks(range(3), ["random", "sorted", "reversed"])
ax.set_ylabel("Mean execution time (µs)")
ax.set_title("Effect of input order, n = 1,000")
ax.legend(frameon=False)
fig.tight_layout()
fig.savefig(OUT / "sort_shapes.png", dpi=200)
plt.close(fig)

print("Saved graphs to", OUT)
