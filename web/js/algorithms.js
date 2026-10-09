/**
 * Algorithms for the ADP470S web view: a JavaScript port of the Java implementations in
 * src/main/java/.../dsa_assignment (parts A to E). No dependencies, runs in the browser and in Node.
 *
 * @author Rorisang Makgana
 */

/* ------------------------------------------------------------------ Part A: recursion */

/** Records the call tree while a recursive function runs. */
export class CallTreeRecorder {
  constructor() { this.root = null; this.stack = []; this.maxDepth = 0; this.calls = 0; }
  enter(n) {
    const node = { n, result: null, children: [] };
    if (this.stack.length) this.stack[this.stack.length - 1].children.push(node); else this.root = node;
    this.stack.push(node);
    this.calls++;
    this.maxDepth = Math.max(this.maxDepth, this.stack.length);
  }
  exit(result) { const node = this.stack.pop(); node.result = result; return result; }
}

export function factorialRecursive(n, rec = null) {
  if (n < 0) throw new RangeError('n must be >= 0');
  rec?.enter(n);
  // Base case: 0! = 1! = 1
  if (n <= 1) return rec ? rec.exit(1) : 1;
  const result = n * factorialRecursive(n - 1, rec);
  return rec ? rec.exit(result) : result;
}

export function factorialIterative(n) {
  if (n < 0) throw new RangeError('n must be >= 0');
  let result = 1;
  for (let i = 2; i <= n; i++) result *= i;
  return result;
}

export function fibonacciRecursive(n, rec = null) {
  if (n < 0) throw new RangeError('n must be >= 0');
  rec?.enter(n);
  // Base cases: F(0) = 0, F(1) = 1
  if (n <= 1) return rec ? rec.exit(n) : n;
  const result = fibonacciRecursive(n - 1, rec) + fibonacciRecursive(n - 2, rec);
  return rec ? rec.exit(result) : result;
}

export function fibonacciIterative(n) {
  if (n < 0) throw new RangeError('n must be >= 0');
  let previous = 0, current = 1;
  for (let i = 0; i < n; i++) [previous, current] = [current, previous + current];
  return previous;
}

/* ------------------------------------------------------------------ Part B: sorting */

/**
 * Every sorter takes (array, listener). The listener has compare(i, j), swap(i, j) and sorted(i),
 * exactly like the Java SortListener. Pass NO_OP when timing.
 */
export const NO_OP = { compare() {}, swap() {}, sorted() {} };

const less = (a, i, j, l) => { l.compare(i, j); return a[i] < a[j]; };
const swap = (a, i, j, l) => { [a[i], a[j]] = [a[j], a[i]]; l.swap(i, j); };

export function selectionSort(a, l = NO_OP) {
  const n = a.length;
  for (let i = 0; i < n; i++) {
    let minimum = i;
    for (let j = i + 1; j < n; j++) if (less(a, j, minimum, l)) minimum = j;
    if (minimum !== i) swap(a, i, minimum, l);
    l.sorted(i);
  }
}

export function quickSort(a, l = NO_OP, random = Math.random) {
  const partition = (lo, hi) => {
    // A random pivot is moved to the end of the range (Lomuto scheme)
    const r = lo + Math.floor(random() * (hi - lo + 1));
    if (r !== hi) swap(a, r, hi, l);
    let boundary = lo - 1;
    for (let j = lo; j < hi; j++) {
      if (less(a, j, hi, l)) { boundary++; if (boundary !== j) swap(a, boundary, j, l); }
    }
    if (boundary + 1 !== hi) swap(a, boundary + 1, hi, l);
    return boundary + 1;
  };
  const sort = (lo, hi) => {
    if (lo > hi) return;                         // base case: empty range
    if (lo === hi) { l.sorted(lo); return; }     // base case: single element
    const p = partition(lo, hi);
    l.sorted(p);
    sort(lo, p - 1);
    sort(p + 1, hi);
  };
  sort(0, a.length - 1);
}

export function heapSort(a, l = NO_OP) {
  const siftDown = (i, size) => {
    for (;;) {
      let largest = i;
      const left = 2 * i + 1, right = 2 * i + 2;
      if (left < size && less(a, largest, left, l)) largest = left;
      if (right < size && less(a, largest, right, l)) largest = right;
      if (largest === i) return;
      swap(a, i, largest, l);
      i = largest;
    }
  };
  const n = a.length;
  for (let i = Math.floor(n / 2) - 1; i >= 0; i--) siftDown(i, n);   // build the max-heap
  for (let end = n - 1; end > 0; end--) {
    swap(a, 0, end, l);       // the root is the largest remaining value
    l.sorted(end);
    siftDown(0, end);
  }
  if (n > 0) l.sorted(0);
}

export const SORTERS = [
  { name: 'Selection Sort', run: selectionSort, big: 'O(n²) always' },
  { name: 'Quick Sort', run: quickSort, big: 'O(n log n) avg, O(n²) worst' },
  { name: 'Heap Sort', run: heapSort, big: 'O(n log n) always' },
];

/** Records a sort as a list of steps (the replay data the animation uses). */
export function recordSort(sorter, array) {
  const steps = [];
  sorter.run(array.slice(), {
    compare: (i, j) => steps.push({ t: 'compare', i, j }),
    swap: (i, j) => steps.push({ t: 'swap', i, j }),
    sorted: (i) => steps.push({ t: 'sorted', i }),
  });
  return steps;
}

/** Counts comparisons and swaps in an untimed run. */
export function countOps(sorter, array) {
  let comparisons = 0, swaps = 0;
  sorter.run(array.slice(), { compare: () => comparisons++, swap: () => swaps++, sorted() {} });
  return { comparisons, swaps };
}

/** Seeded random integers so a run is repeatable (mulberry32). */
export function seededRandom(seed) {
  let s = seed >>> 0;
  return () => {
    s = (s + 0x6D2B79F5) >>> 0;
    let t = s;
    t = Math.imul(t ^ (t >>> 15), t | 1);
    t ^= t + Math.imul(t ^ (t >>> 7), t | 61);
    return ((t ^ (t >>> 14)) >>> 0) / 4294967296;
  };
}

export function randomArray(n, seed = 470, max = 10000) {
  const r = seededRandom(seed);
  return Array.from({ length: n }, () => Math.floor(r() * max));
}

/** Times a sorter: warm-ups, then `runs` timed runs on clones of the same data. Returns ns per run. */
export function benchmark(sorter, data, warmups = 20, runs = 200) {
  for (let i = 0; i < warmups; i++) sorter.run(data.slice(), NO_OP);
  const times = [];
  for (let i = 0; i < runs; i++) {
    const copy = data.slice();
    const t0 = performance.now();
    sorter.run(copy, NO_OP);
    times.push((performance.now() - t0) * 1e6);
  }
  times.sort((x, y) => x - y);
  const mean = times.reduce((s, v) => s + v, 0) / times.length;
  return { mean, median: times[Math.floor(times.length / 2)], best: times[0], times };
}

/* ------------------------------------------------------------------ Part B: binary search */

export function binarySearchIterative(a, key, onProbe = () => {}) {
  let lo = 0, hi = a.length - 1;
  while (lo <= hi) {
    const mid = lo + Math.floor((hi - lo) / 2);
    onProbe(lo, mid, hi);
    if (a[mid] === key) return mid;
    if (a[mid] < key) lo = mid + 1; else hi = mid - 1;
  }
  return -1;
}

export function binarySearchRecursive(a, key, onProbe = () => {}, lo = 0, hi = a.length - 1) {
  if (lo > hi) return -1;                        // base case: empty range
  const mid = lo + Math.floor((hi - lo) / 2);
  onProbe(lo, mid, hi);
  if (a[mid] === key) return mid;
  return a[mid] < key
    ? binarySearchRecursive(a, key, onProbe, mid + 1, hi)
    : binarySearchRecursive(a, key, onProbe, lo, mid - 1);
}

/* ------------------------------------------------------------------ Part C: data structures */

export class SinglyLinkedList {
  constructor() { this.head = null; this.length = 0; }
  insertFirst(value) { this.head = { data: value, next: this.head }; this.length++; }
  insertAt(index, value) {
    if (index < 0 || index > this.length) throw new RangeError(`Index ${index} out of range`);
    if (index === 0) return this.insertFirst(value);
    let prev = this.head;
    for (let i = 0; i < index - 1; i++) prev = prev.next;
    prev.next = { data: value, next: prev.next };
    this.length++;
  }
  insertLast(value) { this.insertAt(this.length, value); }
  deleteAt(index) {
    if (index < 0 || index >= this.length) throw new RangeError(`Index ${index} out of range`);
    let removed;
    if (index === 0) { removed = this.head.data; this.head = this.head.next; }
    else {
      let prev = this.head;
      for (let i = 0; i < index - 1; i++) prev = prev.next;
      removed = prev.next.data;
      prev.next = prev.next.next;
    }
    this.length--;
    return removed;
  }
  search(value) {
    let cur = this.head, i = 0;
    while (cur) { if (cur.data === value) return i; cur = cur.next; i++; }
    return -1;
  }
  delete(value) { const i = this.search(value); if (i < 0) return false; this.deleteAt(i); return true; }
  toArray() { const out = []; for (let c = this.head; c; c = c.next) out.push(c.data); return out; }
}

export class ArrayStack {
  constructor(capacity) { this.items = new Array(capacity).fill(undefined); this.top = 0; }
  push(item) { if (this.top === this.items.length) throw new Error('Stack is full'); this.items[this.top++] = item; }
  pop() { if (!this.top) throw new Error('Stack is empty'); const v = this.items[--this.top]; this.items[this.top] = undefined; return v; }
  peek() { if (!this.top) throw new Error('Stack is empty'); return this.items[this.top - 1]; }
  get size() { return this.top; }
  toArray() { return this.items.slice(0, this.top); }
}

export class CircularQueue {
  // fill() so empty slots are real `undefined` values, not holes that map() would skip
  constructor(capacity) { this.items = new Array(capacity).fill(undefined); this.front = 0; this.rear = 0; this.count = 0; }
  get capacity() { return this.items.length; }
  enqueue(item) {
    if (this.count === this.items.length) throw new Error('Queue is full');
    this.items[this.rear] = item;
    this.rear = (this.rear + 1) % this.items.length;     // wrap round
    this.count++;
  }
  dequeue() {
    if (!this.count) throw new Error('Queue is empty');
    const v = this.items[this.front];
    this.items[this.front] = undefined;
    this.front = (this.front + 1) % this.items.length;
    this.count--;
    return v;
  }
  isEmpty() { return this.count === 0; }
}

export function reverseString(text) {
  const stack = new ArrayStack(text.length);
  for (const ch of text) stack.push(ch);
  let out = '';
  while (stack.size) out += stack.pop();
  return out;
}

/** One teller, one line (a CircularQueue), customers arriving at random. */
export class BankSimulation {
  constructor(lineCapacity = 8, arrivalChance = 0.5, seed = 42) {
    this.line = new CircularQueue(lineCapacity);
    this.arrivalChance = arrivalChance;
    this.random = seededRandom(seed);
    this.clock = 0; this.nextId = 1; this.atTeller = null; this.remaining = 0;
    this.served = 0; this.started = 0; this.totalWait = 0;
  }
  tick() {
    this.clock++;
    if (this.random() < this.arrivalChance && this.line.count < this.line.capacity) {
      this.line.enqueue({ id: this.nextId++, arrival: this.clock, service: 2 + Math.floor(this.random() * 4) });
    }
    if (this.atTeller && --this.remaining <= 0) { this.served++; this.atTeller = null; }
    if (!this.atTeller && !this.line.isEmpty()) {
      this.atTeller = this.line.dequeue();
      this.remaining = this.atTeller.service;
      this.totalWait += this.clock - this.atTeller.arrival;
      this.started++;
    }
  }
  get averageWait() { return this.started ? this.totalWait / this.started : 0; }
}

/* ------------------------------------------------------------------ Part D: binary search tree */

export class BinarySearchTree {
  constructor() { this.root = null; this.size = 0; }
  insert(key) {
    if (this.search(key)) return false;           // duplicates are rejected
    const add = (node) => {
      if (!node) return { key, left: null, right: null };
      if (key < node.key) node.left = add(node.left); else node.right = add(node.right);
      return node;
    };
    this.root = add(this.root);
    this.size++;
    return true;
  }
  search(key) {
    let cur = this.root;
    while (cur && cur.key !== key) cur = key < cur.key ? cur.left : cur.right;
    return !!cur;
  }
  searchPath(key) {
    const path = [];
    let cur = this.root;
    while (cur) { path.push(cur.key); if (cur.key === key) break; cur = key < cur.key ? cur.left : cur.right; }
    return path;
  }
  delete(key) {
    if (!this.search(key)) return false;
    const findMin = (n) => { while (n.left) n = n.left; return n; };
    const remove = (node, k) => {
      if (!node) return null;
      if (k < node.key) node.left = remove(node.left, k);
      else if (k > node.key) node.right = remove(node.right, k);
      else {
        // leaf or one child: replace by that child; two children: copy the in-order successor
        if (!node.left) return node.right;
        if (!node.right) return node.left;
        const successor = findMin(node.right);
        node.key = successor.key;
        node.right = remove(node.right, successor.key);
      }
      return node;
    };
    this.root = remove(this.root, key);
    this.size--;
    return true;
  }
  inOrder() { const out = []; const go = (n) => { if (n) { go(n.left); out.push(n.key); go(n.right); } }; go(this.root); return out; }
  preOrder() { const out = []; const go = (n) => { if (n) { out.push(n.key); go(n.left); go(n.right); } }; go(this.root); return out; }
  postOrder() { const out = []; const go = (n) => { if (n) { go(n.left); go(n.right); out.push(n.key); } }; go(this.root); return out; }
  /** Height in edges: -1 for an empty tree, 0 for one node. */
  height() { const h = (n) => (n ? 1 + Math.max(h(n.left), h(n.right)) : -1); return h(this.root); }
  clear() { this.root = null; this.size = 0; }
}

/* ------------------------------------------------------------------ Part E: graphs */

export const CITIES = ['Cape Town', 'George', 'Gqeberha', 'East London', 'Durban', 'Bloemfontein',
  'Kimberley', 'Johannesburg', 'Pretoria', 'Polokwane', 'Mbombela'];
export const COORDS = [[24, 624], [270, 630], [456, 628], [594, 570], [780, 384], [492, 336],
  [408, 312], [600, 170], [618, 118], [687, 24], [778, 118]];
export const ROUTES = [['Cape Town', 'George'], ['Cape Town', 'Kimberley'], ['George', 'Gqeberha'],
  ['Gqeberha', 'East London'], ['East London', 'Durban'], ['Gqeberha', 'Bloemfontein'],
  ['East London', 'Bloemfontein'], ['Kimberley', 'Bloemfontein'], ['Bloemfontein', 'Johannesburg'],
  ['Kimberley', 'Johannesburg'], ['Durban', 'Johannesburg'], ['Durban', 'Mbombela'],
  ['Johannesburg', 'Pretoria'], ['Pretoria', 'Polokwane'], ['Pretoria', 'Mbombela'], ['Polokwane', 'Mbombela']];

/** Undirected graph stored as an adjacency matrix. */
export class Graph {
  constructor(names) {
    this.names = names.slice();
    this.adj = names.map(() => names.map(() => 0));
  }
  indexOf(name) { const i = this.names.indexOf(name); if (i < 0) throw new Error(`Unknown city: ${name}`); return i; }
  addEdge(a, b) {
    const u = typeof a === 'string' ? this.indexOf(a) : a, v = typeof b === 'string' ? this.indexOf(b) : b;
    this.adj[u][v] = 1; this.adj[v][u] = 1;
  }
  neighbours(v) { return this.adj[v].map((x, i) => (x ? i : -1)).filter((i) => i >= 0); }
  get size() { return this.names.length; }
}

export function transportNetwork() {
  const g = new Graph(CITIES);
  ROUTES.forEach(([a, b]) => g.addEdge(a, b));
  return g;
}

/** Recursive DFS. Returns the visit order; `steps` receives {t:'visit'|'edge'} events. */
export function dfs(graph, start, steps = []) {
  const visited = new Array(graph.size).fill(false), order = [];
  const go = (v) => {
    visited[v] = true; order.push(v); steps.push({ t: 'visit', v });
    for (const next of graph.neighbours(v)) {
      if (!visited[next]) { steps.push({ t: 'edge', from: v, to: next }); go(next); }
    }
  };
  go(start);
  return order;
}

/** BFS using the CircularQueue (capacity = number of vertices). */
export function bfs(graph, start, steps = []) {
  const seen = new Array(graph.size).fill(false), order = [], queue = new CircularQueue(graph.size);
  seen[start] = true; queue.enqueue(start); steps.push({ t: 'enqueue', v: start });
  while (!queue.isEmpty()) {
    const v = queue.dequeue();
    steps.push({ t: 'dequeue', v }); order.push(v); steps.push({ t: 'visit', v });
    for (const next of graph.neighbours(v)) {
      if (!seen[next]) {
        seen[next] = true; steps.push({ t: 'edge', from: v, to: next });
        queue.enqueue(next); steps.push({ t: 'enqueue', v: next });
      }
    }
  }
  return order;
}

/** BFS plus a parent array: the path with the fewest edges, or [] when unreachable. */
export function shortestPath(graph, src, dst) {
  const parent = new Array(graph.size).fill(-1), seen = new Array(graph.size).fill(false);
  const queue = new CircularQueue(graph.size);
  seen[src] = true; queue.enqueue(src);
  while (!queue.isEmpty()) {
    const v = queue.dequeue();
    if (v === dst) {
      const path = [];
      for (let x = dst; x !== -1; x = parent[x]) path.push(x);
      return path.reverse();
    }
    for (const next of graph.neighbours(v)) {
      if (!seen[next]) { seen[next] = true; parent[next] = v; queue.enqueue(next); }
    }
  }
  return [];
}
