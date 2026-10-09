# ADP470S DSA Visualiser

**Rorisang Makgana · ADP470S Programming Assignment (Java 21)**

Recursion, sorting and searching, linked lists, stacks, queues, binary search trees and graphs, each implemented from scratch and paired with an animated demo. The algorithms run in a Java desktop app, and the same algorithms are also available as a web page.

## Run it

| I want to… | Do this |
|---|---|
| **Open the desktop app** (Java 21+ installed) | Double-click [`dist/DSA-Visualiser.jar`](dist/DSA-Visualiser.jar), or double-click [`dist/Run-DSA-Visualiser.bat`](dist/Run-DSA-Visualiser.bat) (it tells you if Java is missing) |
| **Use it with no Java at all** | Open the web version: **[`<ADP Assignment Web App`](https://adp4datastructuresandalgorithmsweba.vercel.app/)**. To try it offline, open a terminal in [`web/`](web/) and run `python -m http.server 5173`, then browse to <http://localhost:5173> |
| **Run from a terminal** | `java -jar dist/DSA-Visualiser.jar` |
| **Build and run from source** | `mvn spring-boot:run` (JDK 21 and Maven, or use the bundled `mvnw`) |
| **Run all tests** | `mvn test` (114 JUnit tests) |

If double-clicking the JAR opens an archive tool instead of the app, use the `.bat` file or the terminal command above.

## Where to find each algorithm

All Java code is in [`src/main/java/com/data_structs_and_algos/makgana_rorisang/dsa_assignment/`](src/main/java/com/data_structs_and_algos/makgana_rorisang/dsa_assignment/). Every class has Javadoc with its complexity. The last column is the matching test class.

### Part A: Recursive algorithms

| Topic | Code | Time / space | Tests |
|---|---|---|---|
| Factorial (recursive + iterative) | [`Factorial.java`](src/main/java/com/data_structs_and_algos/makgana_rorisang/dsa_assignment/parta/Factorial.java) | O(n) / O(n) recursive, O(1) iterative | [`FactorialTest`](src/test/java/com/data_structs_and_algos/makgana_rorisang/dsa_assignment/parta/FactorialTest.java) |
| nth Fibonacci (recursive + iterative) | [`Fibonacci.java`](src/main/java/com/data_structs_and_algos/makgana_rorisang/dsa_assignment/parta/Fibonacci.java) | O(2ⁿ) / O(n) recursive, O(n) / O(1) iterative | [`FibonacciTest`](src/test/java/com/data_structs_and_algos/makgana_rorisang/dsa_assignment/parta/FibonacciTest.java) |
| Call counting and call tree | [`RecursiveAlgorithm.java`](src/main/java/com/data_structs_and_algos/makgana_rorisang/dsa_assignment/parta/RecursiveAlgorithm.java), [`CallTreeRecorder.java`](src/main/java/com/data_structs_and_algos/makgana_rorisang/dsa_assignment/parta/CallTreeRecorder.java) | | [`CallTreeRecorderTest`](src/test/java/com/data_structs_and_algos/makgana_rorisang/dsa_assignment/parta/CallTreeRecorderTest.java) |

### Part B: Searching and sorting

| Topic | Code | Time / space | Tests |
|---|---|---|---|
| Selection Sort (iterative) | [`SelectionSort.java`](src/main/java/com/data_structs_and_algos/makgana_rorisang/dsa_assignment/partb/SelectionSort.java) | O(n²) / O(1) | [`SelectionSortTest`](src/test/java/com/data_structs_and_algos/makgana_rorisang/dsa_assignment/partb/SelectionSortTest.java) |
| Quick Sort (recursive) | [`QuickSort.java`](src/main/java/com/data_structs_and_algos/makgana_rorisang/dsa_assignment/partb/QuickSort.java) | O(n log n) avg, O(n²) worst / O(log n) | [`QuickSortTest`](src/test/java/com/data_structs_and_algos/makgana_rorisang/dsa_assignment/partb/QuickSortTest.java) |
| Heap Sort | [`HeapSort.java`](src/main/java/com/data_structs_and_algos/makgana_rorisang/dsa_assignment/partb/HeapSort.java) | O(n log n) / O(1) | [`HeapSortTest`](src/test/java/com/data_structs_and_algos/makgana_rorisang/dsa_assignment/partb/HeapSortTest.java) |
| Binary Search (recursive + iterative) | [`BinarySearch.java`](src/main/java/com/data_structs_and_algos/makgana_rorisang/dsa_assignment/partb/BinarySearch.java) | O(log n) / O(1) iterative, O(log n) recursive | [`BinarySearchTest`](src/test/java/com/data_structs_and_algos/makgana_rorisang/dsa_assignment/partb/BinarySearchTest.java) |
| Shared sorting plumbing (compare / swap hooks) | [`AbstractSorter.java`](src/main/java/com/data_structs_and_algos/makgana_rorisang/dsa_assignment/partb/AbstractSorter.java), [`OperationCounter.java`](src/main/java/com/data_structs_and_algos/makgana_rorisang/dsa_assignment/partb/OperationCounter.java) | | [`OperationCounterTest`](src/test/java/com/data_structs_and_algos/makgana_rorisang/dsa_assignment/partb/OperationCounterTest.java) |
| 1,000-integer experiment | [`Benchmark.java`](src/main/java/com/data_structs_and_algos/makgana_rorisang/dsa_assignment/partb/Benchmark.java), [`BenchmarkCli.java`](src/main/java/com/data_structs_and_algos/makgana_rorisang/dsa_assignment/partb/BenchmarkCli.java), [`DatasetLoader.java`](src/main/java/com/data_structs_and_algos/makgana_rorisang/dsa_assignment/partb/DatasetLoader.java) | | [`BenchmarkTest`](src/test/java/com/data_structs_and_algos/makgana_rorisang/dsa_assignment/partb/BenchmarkTest.java) |

**Experiment results and graphs.** [`benchmarks/`](benchmarks/) holds the CSV output of the Java benchmark (1,000 random integers, 1,000 timed samples per algorithm) and [`plot_sorts.py`](benchmarks/plot_sorts.py), which draws the graphs with matplotlib. To reproduce them:

```bash
mvn -q compile
java -cp target/classes com.data_structs_and_algos.makgana_rorisang.dsa_assignment.partb.BenchmarkCli benchmarks
pip install matplotlib pandas
python benchmarks/plot_sorts.py
```

### Part C: Elementary data structures

| Topic | Code | Time | Tests |
|---|---|---|---|
| Singly linked list (insert, delete, search) | [`SinglyLinkedList.java`](src/main/java/com/data_structs_and_algos/makgana_rorisang/dsa_assignment/partc/SinglyLinkedList.java) | insertFirst O(1), others O(n) | [`SinglyLinkedListTest`](src/test/java/com/data_structs_and_algos/makgana_rorisang/dsa_assignment/partc/SinglyLinkedListTest.java) |
| Stack (array-based) | [`ArrayStack.java`](src/main/java/com/data_structs_and_algos/makgana_rorisang/dsa_assignment/partc/ArrayStack.java) | push / pop / peek O(1) | [`ArrayStackTest`](src/test/java/com/data_structs_and_algos/makgana_rorisang/dsa_assignment/partc/ArrayStackTest.java) |
| Queue (circular array) | [`CircularQueue.java`](src/main/java/com/data_structs_and_algos/makgana_rorisang/dsa_assignment/partc/CircularQueue.java) | enqueue / dequeue O(1) | [`CircularQueueTest`](src/test/java/com/data_structs_and_algos/makgana_rorisang/dsa_assignment/partc/CircularQueueTest.java) |
| Demo: reverse a string with a stack | [`StringReverser.java`](src/main/java/com/data_structs_and_algos/makgana_rorisang/dsa_assignment/partc/StringReverser.java) | O(n) | [`StringReverserTest`](src/test/java/com/data_structs_and_algos/makgana_rorisang/dsa_assignment/partc/StringReverserTest.java) |
| Demo: a queue at a bank | [`BankSimulation.java`](src/main/java/com/data_structs_and_algos/makgana_rorisang/dsa_assignment/partc/BankSimulation.java) | O(1) per tick | [`BankSimulationTest`](src/test/java/com/data_structs_and_algos/makgana_rorisang/dsa_assignment/partc/BankSimulationTest.java) |

### Part D: Trees

| Topic | Code | Time | Tests |
|---|---|---|---|
| Binary search tree: insert, search, delete, in / pre / post-order | [`BinarySearchTree.java`](src/main/java/com/data_structs_and_algos/makgana_rorisang/dsa_assignment/partd/BinarySearchTree.java) | O(log n) average, O(n) worst (sorted inserts) | [`BinarySearchTreeTest`](src/test/java/com/data_structs_and_algos/makgana_rorisang/dsa_assignment/partd/BinarySearchTreeTest.java) |
| Screen layout for the drawing | [`TreeLayout.java`](src/main/java/com/data_structs_and_algos/makgana_rorisang/dsa_assignment/partd/TreeLayout.java) | O(n) | [`TreeLayoutTest`](src/test/java/com/data_structs_and_algos/makgana_rorisang/dsa_assignment/partd/TreeLayoutTest.java) |

### Part E: Graphs

| Topic | Code | Time | Tests |
|---|---|---|---|
| Adjacency matrix graph | [`Graph.java`](src/main/java/com/data_structs_and_algos/makgana_rorisang/dsa_assignment/parte/Graph.java) | edge test O(1), space O(V²) | [`GraphTest`](src/test/java/com/data_structs_and_algos/makgana_rorisang/dsa_assignment/parte/GraphTest.java) |
| DFS (recursive) and BFS (queue) | [`GraphTraversal.java`](src/main/java/com/data_structs_and_algos/makgana_rorisang/dsa_assignment/parte/GraphTraversal.java) | O(V²) | [`GraphTraversalTest`](src/test/java/com/data_structs_and_algos/makgana_rorisang/dsa_assignment/parte/GraphTraversalTest.java) |
| Real-world problem: fewest-edge route between two cities | [`ShortestPathFinder.java`](src/main/java/com/data_structs_and_algos/makgana_rorisang/dsa_assignment/parte/ShortestPathFinder.java), [`TransportNetwork.java`](src/main/java/com/data_structs_and_algos/makgana_rorisang/dsa_assignment/parte/TransportNetwork.java) | O(V²) | [`ShortestPathFinderTest`](src/test/java/com/data_structs_and_algos/makgana_rorisang/dsa_assignment/parte/ShortestPathFinderTest.java), [`TransportNetworkTest`](src/test/java/com/data_structs_and_algos/makgana_rorisang/dsa_assignment/parte/TransportNetworkTest.java) |

## How the project fits together

```
src/main/java/.../dsa_assignment/
  parta/ … parte/   algorithms and data structures (one package per assignment part),
                    plus the Swing panel that animates each one
  app/              Main, window shell, and the shared Play / Pause / Step / Reset controls
web/                the browser version (HTML, CSS and JavaScript, no build step)
benchmarks/         benchmark CSVs and the Python script that draws the graphs
dist/               the runnable JAR and the Windows launcher
```

- **Recording, then replaying:** each demo runs the real algorithm once with a listener attached, records every step (a comparison, a swap, a recursive call, a visited vertex), and then replays those steps on screen. That is why Play, Pause, Step and Speed work the same way across every part.
- **Adding a sorting algorithm:** write a class that extends [`AbstractSorter`](src/main/java/com/data_structs_and_algos/makgana_rorisang/dsa_assignment/partb/AbstractSorter.java) and mark it `@Component`; it appears in the Sorting and Benchmark panels automatically.
- **Desktop UI:** Java Swing with the FlatLaf look and feel, started through Spring Boot ([`Main.java`](src/main/java/com/data_structs_and_algos/makgana_rorisang/dsa_assignment/app/Main.java)).

## The web version

[`web/`](web/) is plain HTML, CSS and JavaScript with no build step and no dependencies.

- [`web/js/algorithms.js`](web/js/algorithms.js) is a line-by-line port of the Java algorithms (same names, same listener design, same results).
- [`web/js/app.js`](web/js/app.js) holds the nine demo panels.
- Check the port with `node --test web/test/algorithms.test.mjs`.
