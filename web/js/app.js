/**
 * UI for the ADP470S web view. One panel per assignment topic; all logic lives in algorithms.js.
 * @author Rorisang Makgana
 */
import * as A from './algorithms.js';

/* ------------------------------------------------------------------ tiny helpers */

const h = (tag, attrs = {}, ...kids) => {
  const el = document.createElement(tag);
  for (const [k, v] of Object.entries(attrs || {})) {
    if (k === 'class') el.className = v;
    else if (k.startsWith('on')) el.addEventListener(k.slice(2), v);
    else if (v !== false && v != null) el.setAttribute(k, v === true ? '' : v);
  }
  for (const kid of kids.flat()) if (kid != null) el.append(kid.nodeType ? kid : document.createTextNode(kid));
  return el;
};
const svgEl = (tag, attrs = {}, ...kids) => {
  const el = document.createElementNS('http://www.w3.org/2000/svg', tag);
  for (const [k, v] of Object.entries(attrs)) el.setAttribute(k, v);
  for (const kid of kids.flat()) if (kid != null) el.append(kid.nodeType ? kid : document.createTextNode(kid));
  return el;
};
const css = (name) => getComputedStyle(document.documentElement).getPropertyValue(name).trim();
const btn = (label, onclick, primary = false) => h('button', { class: 'btn' + (primary ? ' primary' : ''), type: 'button', onclick }, label);
const field = (label, input) => h('label', {}, label, input);
const numInput = (value, attrs = {}) => h('input', { type: 'number', value, ...attrs });
const stat = (label, value) => h('div', {}, h('b', {}, value), h('span', {}, label));
const fmtNs = (ns) => (ns >= 1e6 ? (ns / 1e6).toFixed(2) + ' ms' : ns >= 1e3 ? (ns / 1e3).toFixed(1) + ' µs' : Math.round(ns) + ' ns');
const sleep0 = () => new Promise((r) => setTimeout(r, 0));

/** Shows an error from a thrown exception in a message line. */
const guarded = (msg, fn) => () => {
  try { msg.className = 'msg'; fn(); } catch (e) { msg.className = 'msg err'; msg.textContent = e.message; }
};

/** Replays a list of steps with play / pause / step / reset and a speed slider. */
class Player {
  constructor(apply, reset, onEnd) { this.apply = apply; this.reset = reset; this.onEnd = onEnd; this.steps = []; this.i = 0; this.timer = null; this.delay = 60; }
  load(steps) { this.stop(); this.steps = steps; this.i = 0; this.reset(); }
  step() { if (this.i >= this.steps.length) { this.stop(); this.onEnd?.(); return false; } this.apply(this.steps[this.i++]); return true; }
  play() { if (this.timer || !this.steps.length) return; this.timer = setInterval(() => this.step(), this.delay); }
  stop() { clearInterval(this.timer); this.timer = null; }
  restart() { this.stop(); this.i = 0; this.reset(); }
  controls() {
    const speed = h('input', { type: 'range', min: 5, max: 400, value: 405 - this.delay, 'aria-label': 'Speed', oninput: (e) => { this.delay = 405 - Number(e.target.value); if (this.timer) { this.stop(); this.play(); } } });
    return h('div', { class: 'row' },
      btn('Play', () => this.play(), true), btn('Pause', () => this.stop()), btn('Step', () => { this.stop(); this.step(); }),
      btn('Reset', () => this.restart()), field('Speed', speed));
  }
}

/* ------------------------------------------------------------------ panels */

const panels = [];
const panel = (id, title, build) => panels.push({ id, title, build });

/* Part A */
panel('recursion', 'A · Recursion', (root) => {
  const algo = h('select', {}, h('option', { value: 'fact' }, 'Factorial'), h('option', { value: 'fib' }, 'Fibonacci'));
  const n = numInput(5, { min: 0, max: 40 });
  const msg = h('div', { class: 'msg' });
  const stats = h('div', { class: 'stats' });
  const tree = h('div', { class: 'tree' });
  const run = guarded(msg, () => {
    const isFib = algo.value === 'fib', v = Number(n.value);
    if (!Number.isInteger(v) || v < 0) throw new Error('Enter a whole number, 0 or more.');
    if (isFib && v > 35) throw new Error('Recursive Fibonacci is exponential. Keep n at 35 or below.');
    if (!isFib && v > 20) throw new Error('A 64-bit integer holds factorials up to 20!, so keep n at 20 or below.');
    const rec = new A.CallTreeRecorder();
    const recursive = isFib ? A.fibonacciRecursive : A.factorialRecursive;
    const iterative = isFib ? A.fibonacciIterative : A.factorialIterative;
    const t0 = performance.now(); const result = recursive(v, rec); const tRec = (performance.now() - t0) * 1e6;
    const t1 = performance.now(); iterative(v); const tIter = (performance.now() - t1) * 1e6;
    stats.replaceChildren(stat('result', result.toLocaleString()), stat('recursive calls', rec.calls.toLocaleString()),
      stat('max depth (stack frames)', rec.maxDepth), stat('recursive time', fmtNs(tRec)), stat('iterative time', fmtNs(tIter)));
    const limit = isFib ? 6 : 12;
    if (v > limit) { tree.replaceChildren(h('p', { class: 'lead' }, `The call tree is drawn for n up to ${limit}. Try a smaller n to see it.`)); return; }
    const draw = (node) => h('li', {}, `${isFib ? 'fib' : 'fact'}(${node.n}) → `, h('span', { class: 'res' }, node.result),
      node.children.length ? h('ul', {}, node.children.map(draw)) : null);
    tree.replaceChildren(h('ul', {}, draw(rec.root)));
  });
  root.append(h('div', { class: 'card' },
    h('h2', {}, 'Recursive algorithms'),
    h('p', { class: 'lead' }, 'Factorial needs n calls and n stack frames: O(n) time and O(n) space. Fibonacci recomputes subproblems, so calls grow as 2F(n+1) − 1: O(2ⁿ) time, O(n) space. The iterative versions are O(n) time and O(1) space.'),
    h('div', { class: 'row' }, field('Algorithm', algo), field('n', n), btn('Run', run, true)), msg, stats),
    h('div', { class: 'card' }, h('h2', {}, 'Call tree'), tree));
  run();
});

/* Part B: sorting */
panel('sorting', 'B · Sorting', (root, cleanup) => {
  const algo = h('select', {}, A.SORTERS.map((s, i) => h('option', { value: i }, s.name)));
  const size = h('input', { type: 'range', min: 8, max: 120, value: 40, 'aria-label': 'Array size' });
  const canvas = h('canvas', { width: 1000, height: 320, role: 'img', 'aria-label': 'Bar chart animation of the selected sorting algorithm' });
  const stats = h('div', { class: 'stats' });
  const big = h('p', { class: 'lead' });
  let array = [], display = [], marks = {}, sortedSet = new Set(), compares = 0, swaps = 0, total = 0;
  const draw = () => {
    const g = canvas.getContext('2d'), W = canvas.width, H = canvas.height, n = display.length, bw = W / n;
    g.clearRect(0, 0, W, H);
    const max = Math.max(...display, 1);
    display.forEach((v, i) => {
      g.fillStyle = marks.swap?.includes(i) ? css('--swap') : marks.compare?.includes(i) ? css('--compare') : sortedSet.has(i) ? css('--done') : css('--bar');
      const bh = (v / max) * (H - 8);
      g.fillRect(i * bw + 1, H - bh, Math.max(bw - 2, 1), bh);
    });
    stats.replaceChildren(stat('comparisons', compares.toLocaleString()), stat('swaps', swaps.toLocaleString()), stat('steps', `${player.i} / ${total}`));
  };
  const player = new Player((s) => {
    marks = {};
    if (s.t === 'compare') { compares++; marks.compare = [s.i, s.j]; }
    else if (s.t === 'swap') { swaps++; [display[s.i], display[s.j]] = [display[s.j], display[s.i]]; marks.swap = [s.i, s.j]; }
    else sortedSet.add(s.i);
    draw();
  }, () => { display = array.slice(); marks = {}; sortedSet = new Set(); compares = swaps = 0; draw(); }, () => { marks = {}; draw(); });
  const load = () => {
    const sorter = A.SORTERS[algo.value];
    array = A.randomArray(Number(size.value), Date.now() & 0xffff, 100).map((v) => v + 5);
    const steps = A.recordSort(sorter, array);
    total = steps.length; big.textContent = `${sorter.name}: ${sorter.big}`;
    player.load(steps);
  };
  algo.addEventListener('change', load); size.addEventListener('input', load);
  cleanup(() => player.stop());
  root.append(h('div', { class: 'card' },
    h('h2', {}, 'Sorting visualiser'), big,
    h('div', { class: 'row' }, field('Algorithm', algo), field('Array size', size), btn('New random array', load)),
    player.controls(), stats, canvas,
    h('div', { class: 'legend' }, h('span', {}, h('i', { style: 'background:var(--compare)' }), 'comparing'),
      h('span', {}, h('i', { style: 'background:var(--swap)' }), 'swapping'), h('span', {}, h('i', { style: 'background:var(--done)' }), 'final position'))));
  load();
});

/* Part B: searching */
panel('search', 'B · Binary Search', (root, cleanup) => {
  const data = Array.from({ length: 31 }, (_, i) => (i + 1) * 3);
  const key = numInput(42, { min: 0 });
  const mode = h('select', {}, h('option', { value: 'it' }, 'Iterative'), h('option', { value: 'rec' }, 'Recursive'));
  const boxes = h('div', { class: 'boxes' });
  const log = h('div', { class: 'msg' });
  let probes = [], found = -1, shown = 0, timer = null;
  const render = () => {
    const p = probes[shown - 1];
    boxes.replaceChildren(...data.map((v, i) => {
      const inRange = !p || (i >= p[0] && i <= p[2]);
      const el = h('div', { class: 'box' + (p && i === p[1] ? ' hot' : ''), style: inRange ? '' : 'opacity:.25' }, v);
      if (shown === probes.length && found === i) el.style.outline = '3px solid var(--done)';
      return el;
    }));
    log.textContent = !shown ? 'Press Search.' : `Probe ${shown}: lo=${p[0]}, mid=${p[1]} (value ${data[p[1]]}), hi=${p[2]}` +
      (shown === probes.length ? (found >= 0 ? ` → found at index ${found}.` : ' → not in the array.') : '');
  };
  const search = () => {
    clearInterval(timer); probes = [];
    const fn = mode.value === 'it' ? A.binarySearchIterative : A.binarySearchRecursive;
    found = fn(data, Number(key.value), (lo, mid, hi) => probes.push([lo, mid, hi]));
    shown = 0; render();
    timer = setInterval(() => { if (shown < probes.length) { shown++; render(); } else clearInterval(timer); }, 600);
  };
  cleanup(() => clearInterval(timer));
  root.append(h('div', { class: 'card' },
    h('h2', {}, 'Binary Search'),
    h('p', { class: 'lead' }, 'Needs a sorted array. Each probe halves the range: O(log n) time. The iterative version uses O(1) space, the recursive one O(log n) for the call stack. 31 values need at most 5 probes.'),
    h('div', { class: 'row' }, field('Find', key), field('Version', mode), btn('Search', search, true)), boxes, log));
  render();
});

/* Part B: benchmark */
panel('benchmark', 'B · Benchmark', (root) => {
  const n = numInput(1000, { min: 10, max: 5000 }), runs = numInput(100, { min: 5, max: 500 });
  const out = h('div'), msg = h('div', { class: 'msg' });
  const run = async () => {
    msg.className = 'msg'; msg.textContent = 'Running…'; out.replaceChildren(); await sleep0();
    const data = A.randomArray(Number(n.value), 470), rows = [];
    for (const s of A.SORTERS) { await sleep0(); rows.push({ s, ...A.benchmark(s, data, 10, Number(runs.value)), ...A.countOps(s, data) }); }
    const maxMed = Math.max(...rows.map((r) => r.median));
    const bars = svgEl('svg', { class: 'fig', viewBox: '0 0 640 170', role: 'img', 'aria-label': 'Median time per algorithm' },
      rows.map((r, i) => svgEl('g', {},
        svgEl('text', { x: 0, y: 28 + i * 48, fill: css('--text'), 'font-size': 13 }, r.s.name),
        svgEl('rect', { x: 120, y: 12 + i * 48, width: Math.max((r.median / maxMed) * 400, 2), height: 24, rx: 4, fill: css('--accent') }),
        svgEl('text', { x: 126 + (r.median / maxMed) * 400, y: 29 + i * 48, fill: css('--text'), 'font-size': 12 }, fmtNs(r.median)))));
    out.replaceChildren(bars, h('table', {},
      h('thead', {}, h('tr', {}, ['Algorithm', 'Median', 'Mean', 'Best', 'Comparisons', 'Swaps'].map((t) => h('th', {}, t)))),
      h('tbody', {}, rows.map((r) => h('tr', {}, h('td', {}, r.s.name), h('td', {}, fmtNs(r.median)), h('td', {}, fmtNs(r.mean)), h('td', {}, fmtNs(r.best)),
        h('td', {}, r.comparisons.toLocaleString()), h('td', {}, r.swaps.toLocaleString()))))));
    msg.textContent = `Same ${n.value} random integers for every algorithm, ${runs.value} timed runs each after warm-up. Browser timers are coarse, so use the medians and the comparison counts, which are exact.`;
  };
  root.append(h('div', { class: 'card' },
    h('h2', {}, 'Experimental comparison'),
    h('p', { class: 'lead' }, 'The assignment benchmark: the three sorts on 1,000 random integers. Selection Sort makes exactly n(n−1)/2 = 499,500 comparisons; Quick Sort and Heap Sort stay near n log n. The Java run (1,000 samples, charted with pyplot) is in the report.'),
    h('div', { class: 'row' }, field('Array size', n), field('Timed runs', runs), btn('Run benchmark', run, true)), msg, out));
});

/* Part C: linked list */
panel('list', 'C · Linked List', (root) => {
  const list = new A.SinglyLinkedList();
  [3, 7, 12].forEach((v) => list.insertLast(v));
  const val = numInput(5), idx = numInput(1, { min: 0 });
  const view = h('div', { class: 'boxes' }), msg = h('div', { class: 'msg' });
  let hot = -1;
  const render = () => {
    const items = list.toArray();
    view.replaceChildren(h('span', { class: 'arrow' }, 'head →'),
      ...(items.length ? items.flatMap((v, i) => [h('div', { class: 'box' + (i === hot ? ' hot' : '') }, v), h('span', { class: 'arrow' }, '→')]) : []),
      h('div', { class: 'box empty' }, 'null'));
    msg.className = 'msg'; msg.textContent = `size = ${list.length}`; hot = -1;
  };
  const act = (fn) => () => { try { fn(); render(); } catch (e) { render(); msg.className = 'msg err'; msg.textContent = e.message; } };
  root.append(h('div', { class: 'card' },
    h('h2', {}, 'Singly linked list'),
    h('p', { class: 'lead' }, 'insertFirst is O(1). insertLast, insertAt, delete and search walk the chain: O(n).'),
    h('div', { class: 'row' }, field('Value', val), field('Index', idx),
      btn('Insert first', act(() => list.insertFirst(Number(val.value)))), btn('Insert last', act(() => list.insertLast(Number(val.value)))),
      btn('Insert at index', act(() => list.insertAt(Number(idx.value), Number(val.value)))),
      btn('Delete value', act(() => { if (!list.delete(Number(val.value))) throw new Error('Value not found.'); })),
      btn('Delete at index', act(() => list.deleteAt(Number(idx.value)))),
      btn('Search', () => { const i = list.search(Number(val.value)); hot = i; render(); hot = -1; msg.textContent = i < 0 ? 'Not found (−1).' : `Found at index ${i}.`; view.children[1 + i * 2]?.classList.add('hot'); })),
    view, msg));
  render();
});

/* Part C: stack */
panel('stack', 'C · Stack', (root) => {
  const stack = new A.ArrayStack(8);
  const val = numInput(10), view = h('div', { class: 'stack' }), msg = h('div', { class: 'msg' });
  const text = h('input', { type: 'text', value: 'javascript', style: 'width:200px' }), rev = h('div', { class: 'msg mono' });
  const render = () => {
    const items = stack.toArray();
    view.replaceChildren(...Array.from({ length: 8 }, (_, i) => h('div', { class: 'box' + (i < items.length ? '' : ' empty') + (i === items.length - 1 ? ' hot' : '') }, i < items.length ? items[i] : '')));
    msg.className = 'msg'; msg.textContent = `size ${stack.size} of 8, top = ${stack.size ? stack.peek() : '–'}`;
  };
  const act = (fn) => () => { try { fn(); render(); } catch (e) { render(); msg.className = 'msg err'; msg.textContent = e.message; } };
  const reverse = () => { rev.textContent = `reverse("${text.value}") = "${A.reverseString(text.value)}"`; };
  root.append(h('div', { class: 'two' },
    h('div', { class: 'card' }, h('h2', {}, 'Array-based stack'),
      h('p', { class: 'lead' }, 'Last in, first out. push, pop and peek are O(1). The top of the stack is the highlighted box.'),
      h('div', { class: 'row' }, field('Value', val), btn('Push', act(() => stack.push(Number(val.value))), true), btn('Pop', act(() => { msg.textContent = `popped ${stack.pop()}`; })), btn('Peek', act(() => stack.peek()))),
      view, msg),
    h('div', { class: 'card' }, h('h2', {}, 'Application: reverse a string'),
      h('p', { class: 'lead' }, 'Push every character, then pop them all: the last one in comes out first.'),
      h('div', { class: 'row' }, field('Text', text), btn('Reverse', reverse, true)), rev)));
  render(); reverse();
});

/* Part C: queue + bank */
panel('queue', 'C · Queue', (root, cleanup) => {
  const q = new A.CircularQueue(8), val = numInput(1);
  const view = h('div', { class: 'boxes' }), msg = h('div', { class: 'msg' });
  const render = () => {
    view.replaceChildren(...q.items.map((v, i) => h('div', { style: 'text-align:center' },
      h('div', { class: 'box' + (v === undefined ? ' empty' : '') }, v === undefined ? '' : v),
      h('small', { class: 'mono' }, `${i}${i === q.front ? ' F' : ''}${i === q.rear ? ' R' : ''}`))));
    msg.className = 'msg'; msg.textContent = `count ${q.count} of ${q.capacity}, front = ${q.front}, rear = ${q.rear}`;
  };
  const act = (fn) => () => { try { fn(); render(); } catch (e) { render(); msg.className = 'msg err'; msg.textContent = e.message; } };
  let next = 1;
  /* bank */
  let bank = new A.BankSimulation(8, 0.55, 42), timer = null;
  const bankView = h('div'), bankStats = h('div', { class: 'stats' });
  const renderBank = () => {
    const line = [];
    for (let i = 0; i < bank.line.count; i++) line.push(bank.line.items[(bank.line.front + i) % bank.line.capacity]);
    bankView.replaceChildren(h('div', { class: 'row' }, h('b', {}, 'Teller: '), bank.atTeller ? h('div', { class: 'box hot' }, `#${bank.atTeller.id}`) : h('div', { class: 'box empty' }, 'idle')),
      h('div', { class: 'row' }, h('b', {}, 'Line: '), line.length ? line.map((c) => h('div', { class: 'box' }, `#${c.id}`)) : h('div', { class: 'box empty' }, 'empty')));
    bankStats.replaceChildren(stat('tick', bank.clock), stat('served', bank.served), stat('average wait (ticks)', bank.averageWait.toFixed(2)));
  };
  const tick = () => { bank.tick(); renderBank(); };
  cleanup(() => clearInterval(timer));
  root.append(h('div', { class: 'card' }, h('h2', {}, 'Circular queue'),
    h('p', { class: 'lead' }, 'First in, first out in a fixed array. F and R mark front and rear; both wrap round with % capacity, so enqueue and dequeue are O(1). Enqueue 8 values, dequeue a few, then enqueue again to watch R wrap.'),
    h('div', { class: 'row' }, field('Value', val), btn('Enqueue', act(() => q.enqueue(Number(val.value))), true), btn('Dequeue', act(() => { msg.textContent = `dequeued ${q.dequeue()}`; })), btn('Auto value', () => { val.value = ++next; })),
    view, msg),
    h('div', { class: 'card' }, h('h2', {}, 'Application: a queue at a bank'),
      h('p', { class: 'lead' }, 'One teller, one line (a circular queue of 8). Customers arrive at random and are served in order.'),
      h('div', { class: 'row' }, btn('Tick', tick, true), btn('Auto run', () => { clearInterval(timer); timer = setInterval(tick, 500); }), btn('Stop', () => clearInterval(timer)),
        btn('Reset', () => { clearInterval(timer); bank = new A.BankSimulation(8, 0.55, 42); renderBank(); })), bankStats, bankView));
  render(); renderBank();
});

/* Part D: BST */
panel('bst', 'D · Binary Search Tree', (root) => {
  const tree = new A.BinarySearchTree();
  [50, 30, 70, 20, 40, 60, 80].forEach((k) => tree.insert(k));
  const key = numInput(65), msg = h('div', { class: 'msg' }), fig = svgEl('svg', { class: 'fig', role: 'img', 'aria-label': 'Binary search tree diagram' }), info = h('div');
  let hot = new Set();
  const render = () => {
    const pos = new Map(); let col = 0;
    const place = (n, d) => { if (!n) return; place(n.left, d + 1); pos.set(n, { x: col++, y: d }); place(n.right, d + 1); };
    place(tree.root, 0);
    const cols = Math.max(col, 1), depth = Math.max(tree.height() + 1, 1);
    const gx = Math.min(44, 960 / cols), gy = 56, W = Math.max(cols * gx + 40, 300), H = depth * gy + 30;
    fig.setAttribute('viewBox', `0 0 ${W} ${H}`); fig.replaceChildren();
    const X = (n) => 20 + pos.get(n).x * gx + gx / 2, Y = (n) => 24 + pos.get(n).y * gy;
    pos.forEach((_, n) => [n.left, n.right].forEach((c) => c && fig.append(svgEl('line', { x1: X(n), y1: Y(n), x2: X(c), y2: Y(c), stroke: css('--line'), 'stroke-width': 2 }))));
    pos.forEach((_, n) => fig.append(svgEl('circle', { cx: X(n), cy: Y(n), r: Math.min(17, gx / 2 - 1), fill: hot.has(n.key) ? css('--hot') : css('--node'), stroke: css('--accent'), 'stroke-width': 2 }),
      svgEl('text', { x: X(n), y: Y(n) + 4, 'text-anchor': 'middle', 'font-size': 11, fill: css('--text') }, n.key)));
    info.replaceChildren(h('p', {}, h('b', {}, 'In-order: '), tree.inOrder().map((k) => h('span', { class: 'chip' }, k))),
      h('p', {}, h('b', {}, 'Pre-order: '), tree.preOrder().map((k) => h('span', { class: 'chip' }, k))),
      h('p', {}, h('b', {}, 'Post-order: '), tree.postOrder().map((k) => h('span', { class: 'chip' }, k))),
      h('div', { class: 'stats' }, stat('nodes', tree.size), stat('height (edges)', tree.height()),
        stat('balanced height', tree.size ? Math.floor(Math.log2(tree.size)) : '–')));
    hot = new Set();
  };
  const set = (m) => { msg.className = 'msg'; msg.textContent = m; };
  root.append(h('div', { class: 'card' }, h('h2', {}, 'Binary search tree'),
    h('p', { class: 'lead' }, 'Every operation follows one path from the root: O(h). Random insertion keeps h near log n (average O(log n)). Inserting sorted keys builds a chain with h = n − 1 (worst case O(n)). Duplicates are rejected.'),
    h('div', { class: 'row' }, field('Key', key),
      btn('Insert', () => { set(tree.insert(Number(key.value)) ? `Inserted ${key.value}.` : 'Duplicate key rejected.'); render(); }, true),
      btn('Search', () => { const p = tree.searchPath(Number(key.value)); hot = new Set(p); const f = tree.search(Number(key.value)); render(); hot = new Set(p); render(); set(`Path: ${p.join(' → ') || '(empty tree)'} — ${f ? 'found' : 'not found'}.`); }),
      btn('Delete', () => { set(tree.delete(Number(key.value)) ? `Deleted ${key.value}.` : 'Key not found.'); render(); }),
      btn('Fill 30 random', () => { tree.clear(); const r = A.seededRandom(Date.now() & 0xffff); while (tree.size < 30) tree.insert(Math.floor(r() * 99) + 1); set('Random keys: the tree stays fairly bushy.'); render(); }),
      btn('Fill 30 sorted', () => { tree.clear(); for (let i = 1; i <= 30; i++) tree.insert(i); set('Sorted keys: the tree is a chain, so every operation is O(n).'); render(); }),
      btn('Clear', () => { tree.clear(); set('Cleared.'); render(); })), msg, h('div', { style: 'overflow-x:auto' }, fig), info));
  render();
});

/* Part E: graph */
panel('graph', 'E · Graph', (root, cleanup) => {
  const g = A.transportNetwork();
  const sel = () => h('select', {}, A.CITIES.map((c, i) => h('option', { value: i }, c)));
  const from = sel(), to = sel(); to.value = 10;
  const msg = h('div', { class: 'msg' }), out = h('div'), order = h('div', { class: 'msg' });
  const fig = svgEl('svg', { class: 'fig', viewBox: '-40 -30 900 720', role: 'img', 'aria-label': 'Map of the transport network' });
  let visited = new Set(), pathEdges = new Set(), pathNodes = new Set(), edgeHot = new Set(), timer = null;
  const ek = (a, b) => `${Math.min(a, b)}-${Math.max(a, b)}`;
  const render = () => {
    fig.replaceChildren();
    A.ROUTES.forEach(([a, b]) => {
      const u = g.indexOf(a), v = g.indexOf(b), k = ek(u, v);
      fig.append(svgEl('line', { x1: A.COORDS[u][0], y1: A.COORDS[u][1], x2: A.COORDS[v][0], y2: A.COORDS[v][1],
        stroke: pathEdges.has(k) ? css('--done') : edgeHot.has(k) ? css('--swap') : css('--line'), 'stroke-width': pathEdges.has(k) ? 6 : 3 }));
    });
    A.CITIES.forEach((c, i) => {
      const [x, y] = A.COORDS[i];
      fig.append(svgEl('circle', { cx: x, cy: y, r: 15, fill: pathNodes.has(i) ? css('--done') : visited.has(i) ? css('--hot') : css('--node'), stroke: css('--accent'), 'stroke-width': 2 }),
        svgEl('text', { x, y: y - 22, 'text-anchor': i === 0 ? 'start' : 'middle', 'font-size': 17, fill: css('--text') }, c));
    });
  };
  const reset = () => { clearInterval(timer); visited = new Set(); pathEdges = new Set(); pathNodes = new Set(); edgeHot = new Set(); };
  const route = () => {
    reset(); const p = A.shortestPath(g, Number(from.value), Number(to.value));
    p.forEach((v, i) => { pathNodes.add(v); if (i) pathEdges.add(ek(p[i - 1], v)); });
    msg.textContent = p.length ? `Fewest edges: ${p.length - 1}` : 'No route.'; order.textContent = '';
    out.replaceChildren(p.map((v, i) => [i ? h('span', { class: 'arrow' }, ' → ') : null, h('span', { class: 'chip' }, g.names[v])]));
    render();
  };
  const traverse = (kind) => () => {
    reset(); out.replaceChildren(); const steps = [], seq = [];
    (kind === 'dfs' ? A.dfs : A.bfs)(g, Number(from.value), steps);
    let i = 0;
    timer = setInterval(() => {
      if (i >= steps.length) { clearInterval(timer); msg.textContent = `${kind.toUpperCase()} visited ${seq.length} cities.`; return; }
      const s = steps[i++];
      if (s.t === 'visit') { visited.add(s.v); seq.push(g.names[s.v]); order.textContent = `${kind.toUpperCase()} order: ${seq.join(' → ')}`; }
      else if (s.t === 'edge') edgeHot.add(ek(s.from, s.to));
      render();
    }, 350);
  };
  cleanup(() => clearInterval(timer));
  root.append(h('div', { class: 'card' }, h('h2', {}, 'Transport network'),
    h('p', { class: 'lead' }, 'Cities are vertices in an adjacency matrix (11 × 11); roads are two-way edges. BFS uses a queue and reaches cities in order of edge count, so the first path it finds to the destination has the fewest edges. DFS and BFS are O(V²) on a matrix.'),
    h('div', { class: 'row' }, field('From', from), field('To', to), btn('Shortest path (BFS)', route, true), btn('Animate DFS', traverse('dfs')), btn('Animate BFS', traverse('bfs')),
      btn('Clear', () => { reset(); order.textContent = ''; out.replaceChildren(); msg.textContent = ''; render(); })), msg, out, order, fig));
  render(); route();
});

/* ------------------------------------------------------------------ shell */

const tabs = document.getElementById('tabs'), view = document.getElementById('view');
let cleanups = [];
const show = (id) => {
  cleanups.forEach((fn) => fn()); cleanups = [];
  const p = panels.find((x) => x.id === id) || panels[0];
  view.replaceChildren();
  p.build(view, (fn) => cleanups.push(fn));
  [...tabs.children].forEach((t) => t.setAttribute('aria-selected', String(t.dataset.id === p.id)));
  history.replaceState(null, '', '#' + p.id);
};
panels.forEach((p) => tabs.append(h('button', { type: 'button', 'data-id': p.id, role: 'tab', onclick: () => show(p.id) }, p.title)));
tabs.setAttribute('role', 'tablist');
show(location.hash.slice(1));
