# Assignment 2 Report

**Student:** Adilet Zhakubay  
**Group:** SE2517  
**Course:** Design and Analysis of Algorithms

## 1. Complexity Analysis

The project implements three data structures: `DynamicArray`, `MyLinkedList`, and `MinHeap`. The first two structures are compared using random access, search, and insertion/removal workloads, while `MinHeap` is evaluated using repeated insertion and minimum extraction. The complexity analysis below refers to the actual implementations in the project.

### 1.1 DynamicArray

| Operation | Best Case | Average Case | Worst Case | Auxiliary Space | Justification |
|---|---:|---:|---:|---:|---|
| `size()` | Θ(1) | Θ(1) | Θ(1) | Θ(1) | The current size is stored in a field. |
| `add(x)` | Θ(1) | Θ(1) amortized | O(n) | O(n) during resize | Normally the value is written at the end; resizing copies all existing elements. |
| `add(index, x)` | Θ(1) | Θ(n) | O(n) | O(n) during resize | Inserting near the end moves few elements, while inserting near the beginning shifts many elements. |
| `get(index)` | Θ(1) | Θ(1) | Θ(1) | Θ(1) | Array indexing directly calculates the required memory location. |
| `remove(index)` | Θ(1) | Θ(n) | O(n) | Θ(1) | Removing the last element needs no shifting, while earlier removal shifts following elements. |
| `contains(x)` | Θ(1) | Θ(n) | O(n) | Θ(1) | Linear search stops immediately for the first element but may inspect the whole array. |

For `add(x)`, the average bound is stated as Θ(1) amortized because occasional resizing costs Θ(n), but the cost is distributed over many insertions.

### 1.2 MyLinkedList

| Operation | Best Case | Average Case | Worst Case | Auxiliary Space | Justification |
|---|---:|---:|---:|---:|---|
| `size()` | Θ(1) | Θ(1) | Θ(1) | Θ(1) | The list stores its current size directly. |
| `add(x)` | Θ(1) | Θ(1) | Θ(1) | Θ(1) | A tail pointer allows insertion at the end without traversal. |
| `add(index, x)` | Θ(1) | Θ(n) | O(n) | Θ(1) | Head/end insertion is immediate, but other positions require traversal to the predecessor. |
| `get(index)` | Θ(1) | Θ(n) | O(n) | Θ(1) | The implementation follows `next` pointers from the head until the requested index. |
| `remove(index)` | Θ(1) | Θ(n) | O(n) | Θ(1) | Removing the head is immediate; other indices require traversal to the previous node. |
| `contains(x)` | Θ(1) | Θ(n) | O(n) | Θ(1) | Nodes are inspected sequentially until a matching value is found or the list ends. |

The linked list does not need an auxiliary array for these operations. A new node is created for insertion, but this is part of the data structure itself rather than an additional algorithmic working array.

### 1.3 MinHeap

| Operation | Best Case | Average Case | Worst Case | Auxiliary Space | Justification |
|---|---:|---:|---:|---:|---|
| `size()` | Θ(1) | Θ(1) | Θ(1) | Θ(1) | The heap size is stored directly. |
| `insert(x)` | Θ(1) | O(log n) | Θ(log n) | Θ(1) | The new element is placed at the end and may move up the heap; resizing is occasional. |
| `peekMin()` | Θ(1) | Θ(1) | Θ(1) | Θ(1) | The minimum is always stored at index 0. |
| `extractMin()` | Θ(1) | O(log n) | Θ(log n) | Θ(1) | After replacing the root, `siftDown` may move the element through the heap height. |

The heap is stored in an array, so parent and child positions are calculated arithmetically. The heap height is logarithmic in the number of elements, which gives `siftUp` and `siftDown` logarithmic worst-case time.

## 2. Loop Invariant Proofs

Two operations containing meaningful loops were selected from the implementation: `contains` in `MyLinkedList` and `siftDown` in `MinHeap`.

### 2.1 Loop Invariant for `MyLinkedList.contains(x)`

**Operation:** `contains(x)`

**Invariant:** Before every iteration of the `while` loop, every node that has already been visited contains a value different from `x`, and `curr` points to the first node that has not yet been checked.

**Initialization:** Before the first iteration, `curr` points to `head` and no nodes have been visited. Therefore, the statement that all visited nodes do not contain `x` is true vacuously.

**Maintenance:** Assume the invariant is true before an iteration. The algorithm compares `curr.val` with `x`. If they are equal, the method immediately returns `true`, so the operation is correct. Otherwise, the current node does not contain `x`, and the algorithm moves `curr` to `curr.next`. Thus, the visited part still contains no occurrence of `x`, and `curr` is again the first unchecked node. Therefore, the invariant remains true.

**Termination:** The loop terminates either when a node containing `x` is found or when `curr == null`. In the first case the method returns `true`. In the second case all nodes have been checked and none contains `x`, so returning `false` is correct.

**Conclusion:** The invariant proves that every node skipped by the loop has been checked and does not contain the target. Therefore, `contains(x)` returns the correct result.

### 2.2 Loop Invariant for `MinHeap.siftDown(index)`

**Operation:** `siftDown(index)`

**Invariant:** Before every iteration of the `while` loop, the subtree below `index` is a valid min-heap except possibly at `index`, and every node outside this affected path already satisfies the min-heap property.

**Initialization:** At the beginning, the element at `index` may violate the heap property with its children, but its child subtrees are already valid heaps. This is because `siftDown` is called after replacing the root with the last element, while the remaining subtrees have not been changed.

**Maintenance:** During an iteration, the algorithm determines the smaller child of the current node. If the current node is already less than or equal to that child, the min-heap property is restored and the loop terminates. Otherwise, the current node is swapped with the smaller child. The possible violation is then moved down to that child position, while the subtree above it becomes a valid min-heap. Thus, the invariant is preserved for the next iteration.

**Termination:** The loop stops when there is no left child, or when the current node is less than or equal to its smallest child. In either case, the affected subtree satisfies the min-heap property.

**Conclusion:** Each iteration moves the only possible violation down one level while preserving valid heap subtrees. When the loop terminates, the whole affected subtree is a valid min-heap, so `extractMin()` preserves the heap invariant.

## 3. Experimental Results and Plots

The benchmark uses input sizes `n = 100, 1000, 10000, 100000` and five timing repetitions, taking the median time. The workloads implemented in `WorkloadExecutor` are:

- **W1 — Random Access:** 10,000 random `get(i)` operations.
- **W2 — Search:** 1,000 `contains` queries, with 500 present and 500 absent values.
- **W3 — Insert/Remove:** 1,000 indexed insertions followed by 1,000 removals, tested at the head and middle.
- **W4 — Priority Processing:** `MinHeap` insertion of `n` elements followed by `n` `extractMin()` operations.

### 3.1 W1 — Random Access

**Figure 1. Time vs n — DynamicArray and MyLinkedList**

*[Insert your W1 Time vs n chart here. Put both structures on the same chart.]*

**Figure 2. Steps vs n — DynamicArray and MyLinkedList**

*[Insert your W1 Steps vs n chart here.]*

The benchmark shows that `DynamicArray.get(i)` remains effectively constant in the number of counted steps, while `MyLinkedList.get(i)` grows with the requested index. At `n = 100000`, the measured time is approximately 1.704 ms for `DynamicArray` versus 1507.083 ms for `MyLinkedList`, illustrating the large practical effect of sequential pointer traversal.

### 3.2 W2 — Search

**Figure 3. Time vs n — DynamicArray and MyLinkedList**

*[Insert your W2 Time vs n chart here.]*

**Figure 4. Comparisons vs n — DynamicArray and MyLinkedList**

*[Insert your W2 Comparisons vs n chart here.]*

Both structures perform linear search, so the number of comparisons is the same for the same query sequence. Nevertheless, the measured times differ because the linked list has to follow pointers between separately allocated nodes, while the dynamic array scans contiguous memory.

### 3.3 W3 — Insert and Remove

**Figure 5. Time vs n — Head insertion/removal**

*[Insert your W3-head Time vs n chart here.]*

**Figure 6. Moves vs n — Head insertion/removal**

*[Insert your W3-head Moves vs n chart here.]*

**Figure 7. Time vs n — Middle insertion/removal**

*[Insert your W3-middle Time vs n chart here.]*

**Figure 8. Moves vs n — Middle insertion/removal**

*[Insert your W3-middle Moves vs n chart here.]*

The head workload demonstrates the strength of a linked list because inserting and removing at the beginning require only a few pointer updates. The middle workload is different because the linked list must first traverse to the target position, while the dynamic array can shift a contiguous block efficiently.

### 3.4 W4 — MinHeap

**Figure 9. Time vs n — MinHeap**

*[Insert your W4 Time vs n chart here.]*

**Figure 10. Steps / moves / comparisons vs n — MinHeap**

*[Insert your W4 Steps/Moves/Comparisons chart here.]*

The heap workload grows approximately as expected for repeated insertion and extraction. Each individual operation can traverse the logarithmic height of the heap, so the total work for processing `n` inserted elements is approximately O(n log n).

## 4. Discussion

`DynamicArray` is faster for `get(i)` because an array provides direct indexing, while `MyLinkedList` must follow links from the head until it reaches the requested node. DynamicArray also benefits from CPU cache lines because its elements are stored in contiguous memory, so loading one part of the array often brings nearby elements into the cache. This spatial locality makes simple iteration and sequential access very efficient. In contrast, linked-list nodes can be located in different memory areas, so following `next` references can cause more cache misses. The linked list can therefore be slower even when both structures perform the same number of counted steps, because each pointer dereference may require another memory access. In addition, Java linked-list nodes have object headers and reference fields, which increase memory overhead compared with primitive values stored in an `int[]`. The creation of many node objects can also increase pressure on the garbage collector. `MyLinkedList` is still a better choice when frequent insertions or removals occur at known positions such as the head, where pointer changes can be performed in constant time. It can also be useful when avoiding large contiguous-array shifts is more important than fast indexed access. `DynamicArray` is preferable when the workload contains many `get(i)` operations or sequential iteration. `MinHeap` is the best choice when the application repeatedly needs the smallest element rather than arbitrary indexed access. Its `peekMin()` operation is constant time, while `insert()` and `extractMin()` are logarithmic in the worst case. Therefore, the appropriate data structure depends not only on asymptotic complexity but also on memory layout and the actual workload.

## 5. Conclusion

The experiments demonstrate that theoretical complexity and practical performance are related but are not identical. `DynamicArray` provides constant-time indexed access and strong cache locality, while `MyLinkedList` provides efficient local insertion and removal when traversal is not required. `MinHeap` is specialized for priority-based processing and provides efficient access to the minimum element. The benchmark results support these theoretical expectations and also show why memory layout, pointer chasing, object overhead, and cache behavior matter in real Java programs.
