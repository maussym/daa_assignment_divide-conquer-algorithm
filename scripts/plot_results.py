from pathlib import Path
import csv
import matplotlib.pyplot as plt

ROOT = Path(__file__).resolve().parents[1]
RESULTS = ROOT / "results" / "results.csv"
PLOTS = ROOT / "docs" / "plots"
PLOTS.mkdir(parents=True, exist_ok=True)

with RESULTS.open(newline="", encoding="utf-8") as f:
    rows = list(csv.DictReader(f))

# Use random/random_points rows so the algorithms can be compared on one uncluttered plot.
selected = [r for r in rows if r["input_type"] in {"random", "random_points"}]
algorithms = sorted({r["algorithm"] for r in selected})

plt.figure(figsize=(8, 5))
for algorithm in algorithms:
    data = sorted((r for r in selected if r["algorithm"] == algorithm), key=lambda r: int(r["n"]))
    plt.plot([int(r["n"]) for r in data], [int(r["time_ns"]) / 1_000_000 for r in data], marker="o", label=algorithm)
plt.xlabel("Input size n")
plt.ylabel("Execution time (ms)")
plt.title("Execution Time vs Input Size")
plt.legend()
plt.grid(True, alpha=0.25)
plt.tight_layout()
plt.savefig(PLOTS / "time_vs_n.png", dpi=180)
plt.close()

plt.figure(figsize=(8, 5))
for algorithm in algorithms:
    data = sorted((r for r in selected if r["algorithm"] == algorithm), key=lambda r: int(r["n"]))
    plt.plot([int(r["n"]) for r in data], [int(r["max_recursion_depth"]) for r in data], marker="o", label=algorithm)
plt.xlabel("Input size n")
plt.ylabel("Maximum recursion depth")
plt.title("Recursion Depth vs Input Size")
plt.legend()
plt.grid(True, alpha=0.25)
plt.tight_layout()
plt.savefig(PLOTS / "recursion_depth_vs_n.png", dpi=180)
plt.close()

print("Saved plots to", PLOTS)
