## MMDP - Multiple Machines Data Processing

### 多机数据处理

利用 10 台机器，在每台机器上保存着 10 亿个 64-bit 整数（不一定刚好 10 亿个，可能有上下几千万的浮动），一共约 100 亿个整数。编程求出：

0. 对这 100 亿个整数排序，结果顺序存放到这 10 台机器上。
1. 这些数的平均数。
2. 这些数的中位数。
3. 出现次数最多的 100 万个数。
4. （附加健壮性要求）能正确应对输入数据的各种分布（均匀、正态、Zipf）。
5. （附加伸缩性要求）能平滑扩展到更多的机器，支持更大的数据量。比如 20 台机器、一共 200 亿个整数，或者 50 台机器、一共 500 亿个整数。


### 进度安排

划线为已完成

- ~~实现测试数据的生成方法~~
- ~~实现外部排序~~
- 实现分区函数
- 实现多机交互


### 参考资料

- Online
	- [External_Sorting](https://en.wikipedia.org/wiki/External_sorting)
	- [Sort Benchmark](http://sortbenchmark.org)
	- [External Memory Sorting](http://lemire.me/blog/archives/2010/04/01/external-memory-sorting-in-java/)
	- [Selection algorithm](https://en.wikipedia.org/wiki/Selection_algorithm)
	- [parallel-computation-of-the-median-of-a-large-array](http://stackoverflow.com/questions/2932503/parallel-computation-of-the-median-of-a-large-array)

- Books
	- [数据结构与算法分析](https://book.douban.com/subject/21519616/)，Weiss，p.315



