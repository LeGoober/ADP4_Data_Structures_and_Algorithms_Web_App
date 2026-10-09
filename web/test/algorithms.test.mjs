// Run with: node --test web/test
import test from 'node:test';
import assert from 'node:assert/strict';
import * as A from '../js/algorithms.js';

test('factorial and fibonacci', () => {
  assert.equal(A.factorialRecursive(5), 120);
  assert.equal(A.factorialIterative(20), 2432902008176640000);
  const rec = new A.CallTreeRecorder();
  A.fibonacciRecursive(7, rec);
  assert.equal(rec.calls, 41);
  const f = new A.CallTreeRecorder();
  A.fibonacciRecursive(10, f);
  assert.equal(f.calls, 177);
  assert.equal(A.fibonacciIterative(30), A.fibonacciRecursive(30));
  assert.equal(A.fibonacciIterative(0), 0);
});

test('sorters sort and count like the Java version', () => {
  const data = A.randomArray(1000, 470);
  const expected = data.slice().sort((x, y) => x - y);
  for (const s of A.SORTERS) {
    const copy = data.slice();
    s.run(copy);
    assert.deepEqual(copy, expected, s.name);
  }
  assert.equal(A.countOps(A.SORTERS[0], data).comparisons, 499500);
  const steps = A.recordSort(A.SORTERS[2], [3, 1, 2]);
  assert.ok(steps.some((s) => s.t === 'swap'));
});

test('binary search', () => {
  const a = [2, 4, 6, 8, 10, 12];
  for (let k = 0; k <= 13; k++) assert.equal(A.binarySearchIterative(a, k), A.binarySearchRecursive(a, k));
  assert.equal(A.binarySearchIterative(a, 8), 3);
  assert.equal(A.binarySearchIterative([], 1), -1);
});

test('data structures', () => {
  const l = new A.SinglyLinkedList();
  l.insertLast(2); l.insertFirst(1); l.insertAt(2, 3);
  assert.deepEqual(l.toArray(), [1, 2, 3]);
  assert.ok(l.delete(2));
  assert.equal(l.search(3), 1);
  assert.throws(() => l.insertAt(9, 1));
  assert.equal(A.reverseString('javascript'), 'tpircsavaj');
  const q = new A.CircularQueue(3);
  q.enqueue(1); q.enqueue(2); q.enqueue(3); q.dequeue(); q.dequeue(); q.enqueue(4); q.enqueue(5);
  assert.deepEqual([q.dequeue(), q.dequeue(), q.dequeue()], [3, 4, 5]);
  assert.throws(() => q.dequeue());
  // empty slots must be real entries, so the UI's items.map() draws every slot
  assert.equal(new A.CircularQueue(8).items.map(() => 1).length, 8);
  assert.equal(Object.keys(new A.CircularQueue(8).items).length, 8);
  const bank = new A.BankSimulation(5, 0.9, 7);
  for (let i = 0; i < 200; i++) bank.tick();
  assert.ok(bank.served > 0);
});

test('BST', () => {
  const t = new A.BinarySearchTree();
  [50, 30, 70, 20, 40, 60, 80].forEach((k) => t.insert(k));
  assert.equal(t.insert(50), false);
  assert.deepEqual(t.inOrder(), [20, 30, 40, 50, 60, 70, 80]);
  assert.deepEqual(t.preOrder(), [50, 30, 20, 40, 70, 60, 80]);
  assert.deepEqual(t.postOrder(), [20, 40, 30, 60, 80, 70, 50]);
  assert.deepEqual(t.searchPath(60), [50, 70, 60]);
  t.delete(50);
  assert.equal(t.root.key, 60);
  assert.equal(new A.BinarySearchTree().height(), -1);
  const chain = new A.BinarySearchTree();
  for (let i = 0; i < 100; i++) chain.insert(i);
  assert.equal(chain.height(), 99);
});

test('graph', () => {
  const g = A.transportNetwork();
  const name = (p) => p.map((v) => g.names[v]);
  assert.deepEqual(name(A.shortestPath(g, g.indexOf('Cape Town'), g.indexOf('Mbombela'))),
    ['Cape Town', 'Kimberley', 'Johannesburg', 'Durban', 'Mbombela']);
  assert.deepEqual(name(A.shortestPath(g, g.indexOf('George'), g.indexOf('Durban'))),
    ['George', 'Gqeberha', 'East London', 'Durban']);
  assert.equal(A.bfs(g, 0).length, 11);
  assert.deepEqual(name(A.dfs(g, 0)).slice(0, 3), ['Cape Town', 'George', 'Gqeberha']);
});
