# Keyword Counter
Given a stream of keywords with frequency, keyword counter finds thet top K words at any time using Max Fibonacci Heap with following amortized complexity.

## Amortized Complexity 
```
RemoveMax: O(logn) 
Remove: O(logn)
Insertion: O(1)
IncreaseKey: O(1)
meld: O(1)

n is the number of nodes in the heap
```


