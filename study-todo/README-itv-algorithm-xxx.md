# 大厂算法面试高频题目汇总

> 本文档汇总了字节跳动、阿里巴巴、快手、百度等互联网大厂的LeetCode高频面试题目，包含题目链接、解题思路、伪代码和Java代码实现。

---

## 目录

1. [数据概览](#一数据概览)
2. [字节跳动高频题](#二字节跳动高频题)
3. [阿里巴巴高频题](#三阿里巴巴高频题)
4. [快手高频题](#四快手高频题)
5. [百度高频题](#五百度高频题)
6. [通用高频Top 200](#六通用高频top-200)
7. [按题型分类汇总](#七按题型分类汇总)

---

## 一、数据概览

### 各公司题目统计

| 公司 | 总题目数 | 高频题(≥20次) | 中频题(10-19次) | 低频题(<10次) |
|------|----------|---------------|-----------------|---------------|
| **字节跳动** | 346道 | 26道 | 36道 | 284道 |
| **阿里巴巴** | 18道 | - | - | 18道 |
| **快手** | 30+道 | - | - | 30+道 |
| **百度** | 19道 | - | - | 19道 |

### 超高频题目（出现次数≥40）

| 排名 | 题目 | 出现次数 | 难度 | LeetCode链接 |
|------|------|----------|------|--------------|
| 1 | K个一组翻转链表 | 60次 | 困难 | [链接](https://leetcode.cn/problems/reverse-nodes-in-k-group/) |
| 2 | 无重复字符的最长子串 | 57次 | 中等 | [链接](https://leetcode.cn/problems/longest-substring-without-repeating-characters/) |
| 3 | LRU缓存机制 | 53次 | 中等 | [链接](https://leetcode.cn/problems/lru-cache/) |
| 4 | 数组中的第K个最大元素 | 52次 | 中等 | [链接](https://leetcode.cn/problems/kth-largest-element-in-an-array/) |
| 5 | 反转链表 | 51次 | 简单 | [链接](https://leetcode.cn/problems/reverse-linked-list/) |
| 6 | 二叉树的锯齿形层次遍历 | 47次 | 中等 | [链接](https://leetcode.cn/problems/binary-tree-zigzag-level-order-traversal/) |
| 7 | 三数之和 | 42次 | 中等 | [链接](https://leetcode.cn/problems/3sum/) |
| 8 | 买卖股票的最佳时机 | 41次 | 简单 | [链接](https://leetcode.cn/problems/best-time-to-buy-and-sell-stock/) |

---

## 二、字节跳动高频题

### 🔥 超高频题目（≥40次）

#### 1. K个一组翻转链表（60次）

**题目链接**：[25. K个一组翻转链表](https://leetcode.cn/problems/reverse-nodes-in-k-group/)

**题目描述**：给你链表的头节点 head ，每 k 个节点一组进行翻转，请你返回修改后的链表。

**解题思路**：
1. 使用递归或迭代方式，每k个节点为一组进行翻转
2. 需要记录每组翻转后的头节点和尾节点
3. 连接上一组的尾节点和当前组的头节点

**伪代码**：
```
function reverseKGroup(head, k):
    // 检查剩余节点是否足够k个
    count = 0
    temp = head
    while temp and count < k:
        temp = temp.next
        count++
    
    if count < k:
        return head  // 不足k个，不翻转
    
    // 翻转当前k个节点
    prev = null
    curr = head
    for i from 1 to k:
        next = curr.next
        curr.next = prev
        prev = curr
        curr = next
    
    // 递归处理后续节点
    head.next = reverseKGroup(curr, k)
    
    return prev  // 返回新的头节点
```

**Java代码实现**：
```java
class Solution {
    public ListNode reverseKGroup(ListNode head, int k) {
        // 检查剩余节点是否足够k个
        ListNode temp = head;
        int count = 0;
        while (temp != null && count < k) {
            temp = temp.next;
            count++;
        }
        
        // 不足k个，直接返回
        if (count < k) {
            return head;
        }
        
        // 翻转当前k个节点
        ListNode prev = null;
        ListNode curr = head;
        for (int i = 0; i < k; i++) {
            ListNode next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
        }
        
        // 递归处理后续节点
        head.next = reverseKGroup(curr, k);
        
        return prev;
    }
}
```

---

#### 2. 无重复字符的最长子串（57次）

**题目链接**：[3. 无重复字符的最长子串](https://leetcode.cn/problems/longest-substring-without-repeating-characters/)

**题目描述**：给定一个字符串 s ，请你找出其中不含有重复字符的 最长子串 的长度。

**解题思路**：
1. 使用滑动窗口技术
2. 用HashSet或HashMap记录窗口内的字符
3. 右指针扩展窗口，遇到重复字符时左指针收缩

**伪代码**：
```
function lengthOfLongestSubstring(s):
    left = 0
    maxLen = 0
    charSet = new HashSet()
    
    for right from 0 to s.length - 1:
        while charSet.contains(s[right]):
            charSet.remove(s[left])
            left++
        
        charSet.add(s[right])
        maxLen = max(maxLen, right - left + 1)
    
    return maxLen
```

**Java代码实现**：
```java
class Solution {
    public int lengthOfLongestSubstring(String s) {
        int left = 0;
        int maxLen = 0;
        Set<Character> charSet = new HashSet<>();
        
        for (int right = 0; right < s.length(); right++) {
            // 遇到重复字符，收缩左边界
            while (charSet.contains(s.charAt(right))) {
                charSet.remove(s.charAt(left));
                left++;
            }
            
            charSet.add(s.charAt(right));
            maxLen = Math.max(maxLen, right - left + 1);
        }
        
        return maxLen;
    }
}
```

---

#### 3. LRU缓存机制（53次）

**题目链接**：[146. LRU缓存机制](https://leetcode.cn/problems/lru-cache/)

**题目描述**：请你设计并实现一个满足 LRU (最近最少使用) 缓存约束的数据结构。

**解题思路**：
1. 使用HashMap + 双向链表实现O(1)的get和put操作
2. HashMap存储key到链表节点的映射
3. 双向链表按访问时间排序，最近访问的在头部

**伪代码**：
```
class LRUCache:
    capacity
    map = new HashMap()
    head, tail = new DLinkedNode()  // 伪头部和伪尾部
    
    function get(key):
        if map.containsKey(key):
            node = map.get(key)
            moveToHead(node)
            return node.value
        return -1
    
    function put(key, value):
        if map.containsKey(key):
            node = map.get(key)
            node.value = value
            moveToHead(node)
        else:
            node = new DLinkedNode(key, value)
            map.put(key, node)
            addToHead(node)
            
            if map.size() > capacity:
                removed = removeTail()
                map.remove(removed.key)
    
    function moveToHead(node):
        removeNode(node)
        addToHead(node)
```

**Java代码实现**：
```java
class LRUCache {
    class DLinkedNode {
        int key;
        int value;
        DLinkedNode prev;
        DLinkedNode next;
        
        public DLinkedNode() {}
        
        public DLinkedNode(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }
    
    private Map<Integer, DLinkedNode> cache = new HashMap<>();
    private int size;
    private int capacity;
    private DLinkedNode head, tail;
    
    public LRUCache(int capacity) {
        this.size = 0;
        this.capacity = capacity;
        head = new DLinkedNode();
        tail = new DLinkedNode();
        head.next = tail;
        tail.prev = head;
    }
    
    public int get(int key) {
        DLinkedNode node = cache.get(key);
        if (node == null) {
            return -1;
        }
        moveToHead(node);
        return node.value;
    }
    
    public void put(int key, int value) {
        DLinkedNode node = cache.get(key);
        if (node == null) {
            DLinkedNode newNode = new DLinkedNode(key, value);
            cache.put(key, newNode);
            addToHead(newNode);
            size++;
            
            if (size > capacity) {
                DLinkedNode tail = removeTail();
                cache.remove(tail.key);
                size--;
            }
        } else {
            node.value = value;
            moveToHead(node);
        }
    }
    
    private void addToHead(DLinkedNode node) {
        node.prev = head;
        node.next = head.next;
        head.next.prev = node;
        head.next = node;
    }
    
    private void removeNode(DLinkedNode node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }
    
    private void moveToHead(DLinkedNode node) {
        removeNode(node);
        addToHead(node);
    }
    
    private DLinkedNode removeTail() {
        DLinkedNode res = tail.prev;
        removeNode(res);
        return res;
    }
}
```

---

#### 4. 数组中的第K个最大元素（52次）

**题目链接**：[215. 数组中的第K个最大元素](https://leetcode.cn/problems/kth-largest-element-in-an-array/)

**题目描述**：给定整数数组 nums 和整数 k，请返回数组中第 k 个最大的元素。

**解题思路**：
1. **快速选择算法**：基于快速排序的partition，平均O(n)
2. **最小堆**：维护大小为k的最小堆，O(nlogk)
3. **排序**：直接排序后取第k个，O(nlogn)

**伪代码（快速选择）**：
```
function findKthLargest(nums, k):
    return quickSelect(nums, 0, nums.length - 1, k)

function quickSelect(nums, left, right, k):
    if left == right:
        return nums[left]
    
    pivotIndex = partition(nums, left, right)
    
    // 第k大元素在数组中的索引是nums.length - k
    targetIndex = nums.length - k
    
    if pivotIndex == targetIndex:
        return nums[pivotIndex]
    else if pivotIndex < targetIndex:
        return quickSelect(nums, pivotIndex + 1, right, k)
    else:
        return quickSelect(nums, left, pivotIndex - 1, k)

function partition(nums, left, right):
    pivot = nums[right]
    i = left
    
    for j from left to right - 1:
        if nums[j] <= pivot:
            swap(nums, i, j)
            i++
    
    swap(nums, i, right)
    return i
```

**Java代码实现（快速选择）**：
```java
class Solution {
    public int findKthLargest(int[] nums, int k) {
        return quickSelect(nums, 0, nums.length - 1, k);
    }
    
    private int quickSelect(int[] nums, int left, int right, int k) {
        if (left == right) {
            return nums[left];
        }
        
        int pivotIndex = partition(nums, left, right);
        int targetIndex = nums.length - k;
        
        if (pivotIndex == targetIndex) {
            return nums[pivotIndex];
        } else if (pivotIndex < targetIndex) {
            return quickSelect(nums, pivotIndex + 1, right, k);
        } else {
            return quickSelect(nums, left, pivotIndex - 1, k);
        }
    }
    
    private int partition(int[] nums, int left, int right) {
        int pivot = nums[right];
        int i = left;
        
        for (int j = left; j < right; j++) {
            if (nums[j] <= pivot) {
                swap(nums, i, j);
                i++;
            }
        }
        
        swap(nums, i, right);
        return i;
    }
    
    private void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }
}
```

**Java代码实现（最小堆）**：
```java
class Solution {
    public int findKthLargest(int[] nums, int k) {
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        
        for (int num : nums) {
            minHeap.offer(num);
            if (minHeap.size() > k) {
                minHeap.poll();
            }
        }
        
        return minHeap.peek();
    }
}
```

---

#### 5. 反转链表（51次）

**题目链接**：[206. 反转链表](https://leetcode.cn/problems/reverse-linked-list/)

**题目描述**：给你单链表的头节点 head ，请你反转链表，并返回反转后的链表。

**解题思路**：
1. 迭代法：使用三个指针逐个反转
2. 递归法：递归反转后续节点，再处理当前节点

**伪代码（迭代）**：
```
function reverseList(head):
    prev = null
    curr = head
    
    while curr != null:
        next = curr.next
        curr.next = prev
        prev = curr
        curr = next
    
    return prev
```

**Java代码实现（迭代）**：
```java
class Solution {
    public ListNode reverseList(ListNode head) {
        ListNode prev = null;
        ListNode curr = head;
        
        while (curr != null) {
            ListNode next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
        }
        
        return prev;
    }
}
```

**Java代码实现（递归）**：
```java
class Solution {
    public ListNode reverseList(ListNode head) {
        // 递归终止条件
        if (head == null || head.next == null) {
            return head;
        }
        
        // 递归反转后续链表
        ListNode newHead = reverseList(head.next);
        
        // 反转当前节点
        head.next.next = head;
        head.next = null;
        
        return newHead;
    }
}
```

---

### ⭐ 高频题目（20-39次）

| 排名 | 题目 | 出现次数 | 难度 | LeetCode链接 |
|------|------|----------|------|--------------|
| 9 | 相交链表 | 32次 | 简单 | [链接](https://leetcode.cn/problems/intersection-of-two-linked-lists/) |
| 10 | 二叉树的最近公共祖先 | 32次 | 中等 | [链接](https://leetcode.cn/problems/lowest-common-ancestor-of-a-binary-tree/) |
| 11 | 接雨水 | 31次 | 困难 | [链接](https://leetcode.cn/problems/trapping-rain-water/) |
| 12 | 搜索旋转排序数组 | 27次 | 中等 | [链接](https://leetcode.cn/problems/search-in-rotated-sorted-array/) |
| 13 | 下一个排列 | 27次 | 中等 | [链接](https://leetcode.cn/problems/next-permutation/) |
| 14 | 二叉树的右视图 | 24次 | 中等 | [链接](https://leetcode.cn/problems/binary-tree-right-side-view/) |
| 15 | 螺旋矩阵 | 24次 | 中等 | [链接](https://leetcode.cn/problems/spiral-matrix/) |
| 16 | 重排链表 | 24次 | 中等 | [链接](https://leetcode.cn/problems/reorder-list/) |
| 17 | 合并K个排序链表 | 23次 | 困难 | [链接](https://leetcode.cn/problems/merge-k-sorted-lists/) |
| 18 | 合并两个有序链表 | 22次 | 简单 | [链接](https://leetcode.cn/problems/merge-two-sorted-lists/) |
| 19 | 最长上升子序列 | 21次 | 中等 | [链接](https://leetcode.cn/problems/longest-increasing-subsequence/) |
| 20 | x的平方根 | 20次 | 简单 | [链接](https://leetcode.cn/problems/sqrtx/) |
| 21 | 反转链表 II | 20次 | 中等 | [链接](https://leetcode.cn/problems/reverse-linked-list-ii/) |

---

#### 9. 相交链表（32次）

**题目链接**：[160. 相交链表](https://leetcode.cn/problems/intersection-of-two-linked-lists/)

**题目描述**：给你两个单链表的头节点 headA 和 headB ，请你找出并返回两个单链表相交的起始节点。如果两个链表不存在相交节点，返回 null 。

**解题思路**：
1. **双指针法**：pA 从 headA 出发，pB 从 headB 出发
2. 当 pA 到达链表末尾时，转向 headB；当 pB 到达链表末尾时，转向 headA
3. 两个指针相遇的地方就是相交点

**Java代码实现**：
```java
public class Solution {
    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        if (headA == null || headB == null) {
            return null;
        }
        
        ListNode pA = headA;
        ListNode pB = headB;
        
        while (pA != pB) {
            pA = pA == null ? headB : pA.next;
            pB = pB == null ? headA : pB.next;
        }
        
        return pA;
    }
}
```

---

#### 10. 二叉树的最近公共祖先（32次）

**题目链接**：[236. 二叉树的最近公共祖先](https://leetcode.cn/problems/lowest-common-ancestor-of-a-binary-tree/)

**题目描述**：给定一个二叉树, 找到该树中两个指定节点的最近公共祖先。

**解题思路**：
1. **递归法**：后序遍历
2. 如果当前节点是 p 或 q，返回当前节点
3. 递归左子树和右子树
4. 如果左右子树都返回非空，则当前节点是最近公共祖先
5. 如果只有一边返回非空，返回那边的结果

**Java代码实现**：
```java
class Solution {
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        // 递归终止条件
        if (root == null || root == p || root == q) {
            return root;
        }
        
        // 递归左子树
        TreeNode left = lowestCommonAncestor(root.left, p, q);
        // 递归右子树
        TreeNode right = lowestCommonAncestor(root.right, p, q);
        
        // 如果左右都找到，当前节点就是最近公共祖先
        if (left != null && right != null) {
            return root;
        }
        // 如果只有一边找到，返回找到的那边
        return left != null ? left : right;
    }
}
```

---

#### 11. 接雨水（31次）

**题目链接**：[42. 接雨水](https://leetcode.cn/problems/trapping-rain-water/)

**题目描述**：给定 n 个非负整数表示每个宽度为 1 的柱子的高度图，计算按此排列的柱子，下雨之后能接多少雨水。

**解题思路**：
1. **双指针法**：左右指针分别从两端向中间移动
2. 维护左最大高度和右最大高度
3. 较低的一侧向中间移动，计算当前位置能接的雨水量

**Java代码实现**：
```java
class Solution {
    public int trap(int[] height) {
        int left = 0, right = height.length - 1;
        int leftMax = 0, rightMax = 0;
        int result = 0;
        
        while (left < right) {
            if (height[left] < height[right]) {
                if (height[left] >= leftMax) {
                    leftMax = height[left];
                } else {
                    result += leftMax - height[left];
                }
                left++;
            } else {
                if (height[right] >= rightMax) {
                    rightMax = height[right];
                } else {
                    result += rightMax - height[right];
                }
                right--;
            }
        }
        
        return result;
    }
}
```

---

#### 12. 搜索旋转排序数组（27次）

**题目链接**：[33. 搜索旋转排序数组](https://leetcode.cn/problems/search-in-rotated-sorted-array/)

**题目描述**：整数数组 nums 按升序排列，数组中的值互不相同 。在传递给函数之前，nums 在预先未知的某个下标 k（0 <= k < nums.length）上进行了旋转，使数组变为 [nums[k], nums[k+1], ..., nums[n-1], nums[0], nums[1], ..., nums[k-1]]（下标 从 0 开始计数）。给定旋转后的数组 nums 和一个整数 target ，如果 nums 中存在这个目标值 target ，则返回它的下标，否则返回 -1 。

**解题思路**：
1. **二分查找**：虽然数组旋转了，但其中一半仍然是有序的
2. 每次二分后，判断哪一半是有序的
3. 在有序的那一半中判断 target 是否存在

**Java代码实现**：
```java
class Solution {
    public int search(int[] nums, int target) {
        int left = 0, right = nums.length - 1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            if (nums[mid] == target) {
                return mid;
            }
            
            // 左半部分有序
            if (nums[left] <= nums[mid]) {
                if (target >= nums[left] && target < nums[mid]) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            } else {
                // 右半部分有序
                if (target > nums[mid] && target <= nums[right]) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
        }
        
        return -1;
    }
}
```

---

#### 13. 下一个排列（27次）

**题目链接**：[31. 下一个排列](https://leetcode.cn/problems/next-permutation/)

**题目描述**：实现获取 下一个排列 的函数，算法需要将给定数字序列重新排列成字典序中下一个更大的排列。如果不存在下一个更大的排列，则将数字重新排列成最小的排列（即升序排列）。

**解题思路**：
1. **从后向前**找到第一个降序的位置 i
2. **从后向前**找到第一个大于 nums[i] 的位置 j
3. **交换** nums[i] 和 nums[j]
4. **反转** i+1 到末尾的元素

**Java代码实现**：
```java
class Solution {
    public void nextPermutation(int[] nums) {
        int n = nums.length;
        int i = n - 2;
        
        // 从后向前找到第一个降序的位置
        while (i >= 0 && nums[i] >= nums[i + 1]) {
            i--;
        }
        
        if (i >= 0) {
            // 从后向前找到第一个大于nums[i]的位置
            int j = n - 1;
            while (j > i && nums[j] <= nums[i]) {
                j--;
            }
            // 交换
            swap(nums, i, j);
        }
        
        // 反转i+1到末尾
        reverse(nums, i + 1, n - 1);
    }
    
    private void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }
    
    private void reverse(int[] nums, int start, int end) {
        while (start < end) {
            swap(nums, start, end);
            start++;
            end--;
        }
    }
}
```

---

#### 14. 二叉树的右视图（24次）

**题目链接**：[199. 二叉树的右视图](https://leetcode.cn/problems/binary-tree-right-side-view/)

**题目描述**：给定一个二叉树的 根节点 root，想象自己站在它的右侧，按照从顶部到底部的顺序，返回从右侧所能看到的节点值。

**解题思路**：
1. **层次遍历（BFS）**：按层遍历二叉树
2. 记录每层的最后一个节点

**Java代码实现**：
```java
class Solution {
    public List<Integer> rightSideView(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) {
            return result;
        }
        
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                // 记录每层的最后一个节点
                if (i == size - 1) {
                    result.add(node.val);
                }
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
        }
        
        return result;
    }
}
```

---

#### 15. 螺旋矩阵（24次）

**题目链接**：[54. 螺旋矩阵](https://leetcode.cn/problems/spiral-matrix/)

**题目描述**：给你一个 m 行 n 列的矩阵 matrix ，请按照 顺时针螺旋顺序 ，返回矩阵中的所有元素。

**解题思路**：
1. **模拟法**：按顺时针方向遍历矩阵
2. 维护四个边界：上、下、左、右
3. 按顺序遍历：右 → 下 → 左 → 上

**Java代码实现**：
```java
class Solution {
    public List<Integer> spiralOrder(int[][] matrix) {
        List<Integer> result = new ArrayList<>();
        if (matrix == null || matrix.length == 0) {
            return result;
        }
        
        int top = 0, bottom = matrix.length - 1;
        int left = 0, right = matrix[0].length - 1;
        
        while (top <= bottom && left <= right) {
            // 从左到右
            for (int i = left; i <= right; i++) {
                result.add(matrix[top][i]);
            }
            top++;
            
            // 从上到下
            for (int i = top; i <= bottom; i++) {
                result.add(matrix[i][right]);
            }
            right--;
            
            // 从右到左
            if (top <= bottom) {
                for (int i = right; i >= left; i--) {
                    result.add(matrix[bottom][i]);
                }
                bottom--;
            }
            
            // 从下到上
            if (left <= right) {
                for (int i = bottom; i >= top; i--) {
                    result.add(matrix[i][left]);
                }
                left++;
            }
        }
        
        return result;
    }
}
```

---

#### 16. 重排链表（24次）

**题目链接**：[143. 重排链表](https://leetcode.cn/problems/reorder-list/)

**题目描述**：给定一个单链表 L 的头节点 head ，单链表 L 表示为： L0 → L1 → … → Ln-1 → Ln 。请将其重新排列后变为： L0 → Ln → L1 → Ln-1 → L2 → Ln-2 → …

**解题思路**：
1. **快慢指针**找到链表中点
2. **反转**后半部分链表
3. **合并**前半部分和反转后的后半部分

**Java代码实现**：
```java
class Solution {
    public void reorderList(ListNode head) {
        if (head == null || head.next == null) {
            return;
        }
        
        // 找到中点
        ListNode slow = head, fast = head;
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        
        // 反转后半部分
        ListNode second = slow.next;
        slow.next = null; // 断开
        second = reverse(second);
        
        // 合并两部分
        ListNode first = head;
        while (second != null) {
            ListNode temp1 = first.next;
            ListNode temp2 = second.next;
            
            first.next = second;
            second.next = temp1;
            
            first = temp1;
            second = temp2;
        }
    }
    
    private ListNode reverse(ListNode head) {
        ListNode prev = null;
        ListNode curr = head;
        while (curr != null) {
            ListNode next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
        }
        return prev;
    }
}
```

---

#### 17. 合并K个排序链表（23次）

**题目链接**：[23. 合并K个排序链表](https://leetcode.cn/problems/merge-k-sorted-lists/)

**题目描述**：给你一个链表数组，每个链表都已经按升序排列。请你将所有链表合并到一个升序链表中，返回合并后的链表。

**解题思路**：
1. **分治法**：将链表数组分成两部分，递归合并
2. **优先队列**：维护一个最小堆，每次取出最小的节点

**Java代码实现（分治法）**：
```java
class Solution {
    public ListNode mergeKLists(ListNode[] lists) {
        if (lists == null || lists.length == 0) {
            return null;
        }
        return mergeKLists(lists, 0, lists.length - 1);
    }
    
    private ListNode mergeKLists(ListNode[] lists, int start, int end) {
        if (start == end) {
            return lists[start];
        }
        int mid = start + (end - start) / 2;
        ListNode left = mergeKLists(lists, start, mid);
        ListNode right = mergeKLists(lists, mid + 1, end);
        return mergeTwoLists(left, right);
    }
    
    private ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(0);
        ListNode curr = dummy;
        
        while (l1 != null && l2 != null) {
            if (l1.val < l2.val) {
                curr.next = l1;
                l1 = l1.next;
            } else {
                curr.next = l2;
                l2 = l2.next;
            }
            curr = curr.next;
        }
        
        curr.next = l1 != null ? l1 : l2;
        return dummy.next;
    }
}
```

**Java代码实现（优先队列）**：
```java
class Solution {
    public ListNode mergeKLists(ListNode[] lists) {
        if (lists == null || lists.length == 0) {
            return null;
        }
        
        PriorityQueue<ListNode> minHeap = new PriorityQueue<>((a, b) -> a.val - b.val);
        
        // 将所有链表的头节点加入堆
        for (ListNode list : lists) {
            if (list != null) {
                minHeap.offer(list);
            }
        }
        
        ListNode dummy = new ListNode(0);
        ListNode curr = dummy;
        
        while (!minHeap.isEmpty()) {
            ListNode node = minHeap.poll();
            curr.next = node;
            curr = curr.next;
            
            if (node.next != null) {
                minHeap.offer(node.next);
            }
        }
        
        return dummy.next;
    }
}
```

---

#### 18. 合并两个有序链表（22次）

**题目链接**：[21. 合并两个有序链表](https://leetcode.cn/problems/merge-two-sorted-lists/)

**题目描述**：将两个升序链表合并为一个新的 升序 链表并返回。新链表是通过拼接给定的两个链表的所有节点组成的。

**解题思路**：
1. **迭代法**：使用哑节点，比较两个链表的节点值
2. **递归法**：递归比较两个链表的节点值

**Java代码实现（迭代法）**：
```java
class Solution {
    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        ListNode dummy = new ListNode(0);
        ListNode curr = dummy;
        
        while (list1 != null && list2 != null) {
            if (list1.val < list2.val) {
                curr.next = list1;
                list1 = list1.next;
            } else {
                curr.next = list2;
                list2 = list2.next;
            }
            curr = curr.next;
        }
        
        curr.next = list1 != null ? list1 : list2;
        return dummy.next;
    }
}
```

**Java代码实现（递归法）**：
```java
class Solution {
    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        if (list1 == null) {
            return list2;
        }
        if (list2 == null) {
            return list1;
        }
        
        if (list1.val < list2.val) {
            list1.next = mergeTwoLists(list1.next, list2);
            return list1;
        } else {
            list2.next = mergeTwoLists(list1, list2.next);
            return list2;
        }
    }
}
```

---

#### 19. 最长上升子序列（21次）

**题目链接**：[300. 最长上升子序列](https://leetcode.cn/problems/longest-increasing-subsequence/)

**题目描述**：给你一个整数数组 nums ，找到其中最长严格递增子序列的长度。

**解题思路**：
1. **动态规划**：dp[i] 表示以 nums[i] 结尾的最长上升子序列长度
2. **贪心 + 二分查找**：维护一个递增数组，替换第一个大于等于当前元素的位置

**Java代码实现（动态规划）**：
```java
class Solution {
    public int lengthOfLIS(int[] nums) {
        int n = nums.length;
        int[] dp = new int[n];
        int maxLen = 0;
        
        for (int i = 0; i < n; i++) {
            dp[i] = 1; // 至少包含自己
            for (int j = 0; j < i; j++) {
                if (nums[i] > nums[j]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            maxLen = Math.max(maxLen, dp[i]);
        }
        
        return maxLen;
    }
}
```

**Java代码实现（贪心 + 二分查找）**：
```java
class Solution {
    public int lengthOfLIS(int[] nums) {
        List<Integer> list = new ArrayList<>();
        
        for (int num : nums) {
            int index = binarySearch(list, num);
            if (index == list.size()) {
                list.add(num);
            } else {
                list.set(index, num);
            }
        }
        
        return list.size();
    }
    
    private int binarySearch(List<Integer> list, int target) {
        int left = 0, right = list.size() - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (list.get(mid) < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return left;
    }
}
```

---

#### 20. x的平方根（20次）

**题目链接**：[69. x 的平方根](https://leetcode.cn/problems/sqrtx/)

**题目描述**：给你一个非负整数 x ，计算并返回 x 的 算术平方根 。由于返回类型是整数，结果只保留 整数部分 ，小数部分将被 舍去 。

**解题思路**：
1. **二分查找**：在 [0, x] 范围内查找平方根
2. **牛顿迭代法**：通过迭代逼近平方根

**Java代码实现（二分查找）**：
```java
class Solution {
    public int mySqrt(int x) {
        if (x == 0) {
            return 0;
        }
        
        int left = 1, right = x;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            // 防止溢出
            if (mid > x / mid) {
                right = mid - 1;
            } else {
                if (mid + 1 > x / (mid + 1)) {
                    return mid;
                }
                left = mid + 1;
            }
        }
        
        return -1;
    }
}
```

**Java代码实现（牛顿迭代法）**：
```java
class Solution {
    public int mySqrt(int x) {
        if (x == 0) {
            return 0;
        }
        
        long guess = x;
        while (guess * guess > x) {
            guess = (guess + x / guess) / 2;
        }
        
        return (int) guess;
    }
}
```

---

#### 21. 反转链表 II（20次）

**题目链接**：[92. 反转链表 II](https://leetcode.cn/problems/reverse-linked-list-ii/)

**题目描述**：给你单链表的头指针 head 和两个整数 left 和 right ，其中 left <= right 。请你反转从位置 left 到位置 right 的链表节点，返回 反转后的链表 。

**解题思路**：
1. **找到反转的起点和终点**
2. **反转指定区间的链表**
3. **重新连接链表**

**Java代码实现**：
```java
class Solution {
    public ListNode reverseBetween(ListNode head, int left, int right) {
        if (head == null || left == right) {
            return head;
        }
        
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode prev = dummy;
        
        // 找到反转的前一个节点
        for (int i = 1; i < left; i++) {
            prev = prev.next;
        }
        
        // 反转区间内的链表
        ListNode curr = prev.next;
        ListNode next;
        for (int i = 0; i < right - left; i++) {
            next = curr.next;
            curr.next = next.next;
            next.next = prev.next;
            prev.next = next;
        }
        
        return dummy.next;
    }
}
```

---

## 三、阿里巴巴高频题

### 数据概览

阿里算法岗题目数量相对较少（18道），但动态规划占比高达28%，是绝对重点。

| 指标 | 数据 |
|------|------|
| 总题目数 | 18题 |
| 动态规划 | 5题（28%） |
| 树相关 | 4题（22%） |
| 链表 | 3题（17%） |
| 简单题 | 7题（39%） |
| 中等题 | 7题（39%） |
| 困难题 | 4题（22%） |

### 💎 动态规划（5题，占比28%）

#### 1. 最大子序和

**题目链接**：[53. 最大子序和](https://leetcode.cn/problems/maximum-subarray/)

**题目描述**：给你一个整数数组 nums ，请你找出一个具有最大和的连续子数组（子数组最少包含一个元素），返回其最大和。

**解题思路**：
1. 使用Kadane算法
2. dp[i]表示以第i个元素结尾的最大子数组和
3. dp[i] = max(nums[i], dp[i-1] + nums[i])

**Java代码实现**：
```java
class Solution {
    public int maxSubArray(int[] nums) {
        int maxSum = nums[0];
        int currSum = nums[0];
        
        for (int i = 1; i < nums.length; i++) {
            // 要么从当前元素开始，要么延续之前的子数组
            currSum = Math.max(nums[i], currSum + nums[i]);
            maxSum = Math.max(maxSum, currSum);
        }
        
        return maxSum;
    }
}
```

---

#### 2. 最长公共子序列

**题目链接**：[1143. 最长公共子序列](https://leetcode.cn/problems/longest-common-subsequence/)

**题目描述**：给定两个字符串 text1 和 text2，返回这两个字符串的最长公共子序列的长度。

**解题思路**：
1. 使用二维动态规划
2. dp[i][j]表示text1[0..i-1]和text2[0..j-1]的最长公共子序列长度
3. 如果text1[i-1] == text2[j-1]，dp[i][j] = dp[i-1][j-1] + 1
4. 否则，dp[i][j] = max(dp[i-1][j], dp[i][j-1])

**Java代码实现**：
```java
class Solution {
    public int longestCommonSubsequence(String text1, String text2) {
        int m = text1.length();
        int n = text2.length();
        int[][] dp = new int[m + 1][n + 1];
        
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        
        return dp[m][n];
    }
}
```

---

#### 3. 编辑距离

**题目链接**：[72. 编辑距离](https://leetcode.cn/problems/edit-distance/)

**题目描述**：给你两个单词 word1 和 word2，请返回将 word1 转换成 word2 所使用的最少操作数。

**解题思路**：
1. 使用二维动态规划
2. dp[i][j]表示word1[0..i-1]转换成word2[0..j-1]的最小操作数
3. 如果word1[i-1] == word2[j-1]，dp[i][j] = dp[i-1][j-1]
4. 否则，dp[i][j] = min(dp[i-1][j], dp[i][j-1], dp[i-1][j-1]) + 1

**Java代码实现**：
```java
class Solution {
    public int minDistance(String word1, String word2) {
        int m = word1.length();
        int n = word2.length();
        int[][] dp = new int[m + 1][n + 1];
        
        // 初始化边界
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;
        }
        
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = Math.min(Math.min(
                        dp[i - 1][j],      // 删除
                        dp[i][j - 1]),     // 插入
                        dp[i - 1][j - 1]   // 替换
                    ) + 1;
                }
            }
        }
        
        return dp[m][n];
    }
}
```

---

### 🔗 链表相关（3题）

| 题号 | 题目 | 难度 | LeetCode链接 |
|------|------|------|--------------|
| 剑指Offer 24 | 反转链表 | 简单 | [链接](https://leetcode.cn/problems/fan-zhuan-lian-biao-lcof/) |
| 92 | 反转链表 II | 中等 | [链接](https://leetcode.cn/problems/reverse-linked-list-ii/) |
| 143 | 重排链表 | 中等 | [链接](https://leetcode.cn/problems/reorder-list/) |

### 🌳 树相关（4题）

| 题号 | 题目 | 难度 | LeetCode链接 |
|------|------|------|--------------|
| 110 | 平衡二叉树 | 简单 | [链接](https://leetcode.cn/problems/balanced-binary-tree/) |
| 94 | 二叉树的中序遍历 | 简单 | [链接](https://leetcode.cn/problems/binary-tree-inorder-traversal/) |
| 617 | 合并二叉树 | 简单 | [链接](https://leetcode.cn/problems/merge-two-binary-trees/) |
| 102 | 二叉树的层序遍历 | 中等 | [链接](https://leetcode.cn/problems/binary-tree-level-order-traversal/) |

---

## 四、快手高频题

### 数据概览

快手后端面试中，链表操作是绝对重点，反转链表出现15次，是绝对的王者。

### 🔥 超高频题目（出现次数≥10）

| 出现次数 | 题号 | 题目 | 考察点 |
|----------|------|------|--------|
| 15 | 206 | 反转链表 | 链表基础、指针操作 |
| 8 | 160 | 相交链表 | 链表、双指针 |
| 7 | - | 手撕快速排序 | 排序算法、分治 |
| 7 | 146 | LRU缓存机制 | 设计、哈希表+双向链表 |
| 7 | 21 | 合并两个有序链表 | 链表、递归/迭代 |
| 7 | 215 | 数组中的第K个最大元素 | 快速选择、堆 |
| 6 | 88 | 合并两个有序数组 | 双指针、从后往前 |
| 5 | 141 | 环形链表 | 快慢指针 |
| 5 | 92 | 反转链表 II | 链表、区间反转 |

### 重点题目详解

#### 1. 手撕快速排序

**题目描述**：实现快速排序算法。

**解题思路**：
1. 选择基准元素（pivot）
2. 将数组分为两部分，左边小于pivot，右边大于pivot
3. 递归对左右两部分进行排序

**Java代码实现**：
```java
public class QuickSort {
    public void quickSort(int[] nums, int left, int right) {
        if (left < right) {
            int pivotIndex = partition(nums, left, right);
            quickSort(nums, left, pivotIndex - 1);
            quickSort(nums, pivotIndex + 1, right);
        }
    }
    
    private int partition(int[] nums, int left, int right) {
        // 选择最右边元素作为基准
        int pivot = nums[right];
        int i = left;
        
        for (int j = left; j < right; j++) {
            if (nums[j] <= pivot) {
                swap(nums, i, j);
                i++;
            }
        }
        
        swap(nums, i, right);
        return i;
    }
    
    private void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }
}
```

---

## 五、百度高频题

### 数据概览

百度算法岗面试中，数组中的第K个最大元素出现频率最高（7次），是绝对的高频题。

### 🔥 高频题目（频度≥2）

| 排名 | 题目 | 题号 | 频度 | 难度 | LeetCode链接 |
|------|------|------|------|------|--------------|
| 1 | 数组中的第K个最大元素 | 215 | 7次 | 中等 | [链接](https://leetcode.cn/problems/kth-largest-element-in-an-array/) |
| 2 | 环形链表 II | 142 | 2次 | 中等 | [链接](https://leetcode.cn/problems/linked-list-cycle-ii/) |
| 3 | 二叉树的锯齿形层次遍历 | 103 | 2次 | 中等 | [链接](https://leetcode.cn/problems/binary-tree-zigzag-level-order-traversal/) |

### 题型分布统计

| 题型 | 数量 | 占比 |
|------|------|------|
| 树相关 | 5题 | 23.8% |
| 链表 | 3题 | 14.3% |
| 动态规划 | 3题 | 14.3% |
| 数组/排序 | 2题 | 9.5% |
| 其他 | 8题 | 38.1% |

### 难度分布

| 难度 | 数量 | 占比 |
|------|------|------|
| 简单 | 6题 | 28.6% |
| 中等 | 13题 | 61.9% |
| 困难 | 2题 | 9.5% |

---

## 六、通用高频Top 200

### 1-50题精选

| 题号 | 题目 | 难度 | 题型 | LeetCode链接 |
|------|------|------|------|--------------|
| 1 | 两数之和 | 简单 | 数组、哈希 | [链接](https://leetcode.cn/problems/two-sum/) |
| 2 | 两数相加 | 中等 | 链表 | [链接](https://leetcode.cn/problems/add-two-numbers/) |
| 3 | 无重复字符的最长子串 | 中等 | 字符串、滑动窗口 | [链接](https://leetcode.cn/problems/longest-substring-without-repeating-characters/) |
| 4 | 寻找两个正序数组的中位数 | 困难 | 数组、二分查找 | [链接](https://leetcode.cn/problems/median-of-two-sorted-arrays/) |
| 5 | 最长回文子串 | 中等 | 字符串、动态规划 | [链接](https://leetcode.cn/problems/longest-palindromic-substring/) |
| 11 | 盛最多水的容器 | 中等 | 数组、双指针 | [链接](https://leetcode.cn/problems/container-with-most-water/) |
| 15 | 三数之和 | 中等 | 数组、双指针 | [链接](https://leetcode.cn/problems/3sum/) |
| 19 | 删除链表的倒数第N个节点 | 中等 | 链表、双指针 | [链接](https://leetcode.cn/problems/remove-nth-node-from-end-of-list/) |
| 20 | 有效的括号 | 简单 | 栈 | [链接](https://leetcode.cn/problems/valid-parentheses/) |
| 21 | 合并两个有序链表 | 简单 | 链表 | [链接](https://leetcode.cn/problems/merge-two-sorted-lists/) |
| 23 | 合并K个升序链表 | 困难 | 链表、分治 | [链接](https://leetcode.cn/problems/merge-k-sorted-lists/) |
| 25 | K个一组翻转链表 | 困难 | 链表 | [链接](https://leetcode.cn/problems/reverse-nodes-in-k-group/) |
| 33 | 搜索旋转排序数组 | 中等 | 数组、二分查找 | [链接](https://leetcode.cn/problems/search-in-rotated-sorted-array/) |
| 42 | 接雨水 | 困难 | 数组、双指针、动态规划 | [链接](https://leetcode.cn/problems/trapping-rain-water/) |
| 46 | 全排列 | 中等 | 回溯 | [链接](https://leetcode.cn/problems/permutations/) |
| 53 | 最大子序和 | 简单 | 数组、动态规划 | [链接](https://leetcode.cn/problems/maximum-subarray/) |
| 54 | 螺旋矩阵 | 中等 | 数组、模拟 | [链接](https://leetcode.cn/problems/spiral-matrix/) |
| 56 | 合并区间 | 中等 | 数组、排序 | [链接](https://leetcode.cn/problems/merge-intervals/) |
| 70 | 爬楼梯 | 简单 | 动态规划 | [链接](https://leetcode.cn/problems/climbing-stairs/) |
| 72 | 编辑距离 | 困难 | 字符串、动态规划 | [链接](https://leetcode.cn/problems/edit-distance/) |
| 76 | 最小覆盖子串 | 困难 | 字符串、滑动窗口 | [链接](https://leetcode.cn/problems/minimum-window-substring/) |
| 78 | 子集 | 中等 | 回溯 | [链接](https://leetcode.cn/problems/subsets/) |
| 79 | 单词搜索 | 中等 | 回溯 | [链接](https://leetcode.cn/problems/word-search/) |
| 84 | 柱状图中最大的矩形 | 困难 | 栈 | [链接](https://leetcode.cn/problems/largest-rectangle-in-histogram/) |
| 94 | 二叉树的中序遍历 | 简单 | 树 | [链接](https://leetcode.cn/problems/binary-tree-inorder-traversal/) |
| 98 | 验证二叉搜索树 | 中等 | 树 | [链接](https://leetcode.cn/problems/validate-binary-search-tree/) |
| 101 | 对称二叉树 | 简单 | 树 | [链接](https://leetcode.cn/problems/symmetric-tree/) |
| 102 | 二叉树的层序遍历 | 中等 | 树、BFS | [链接](https://leetcode.cn/problems/binary-tree-level-order-traversal/) |
| 103 | 二叉树的锯齿形层次遍历 | 中等 | 树、BFS | [链接](https://leetcode.cn/problems/binary-tree-zigzag-level-order-traversal/) |
| 104 | 二叉树的最大深度 | 简单 | 树 | [链接](https://leetcode.cn/problems/maximum-depth-of-binary-tree/) |
| 105 | 从前序与中序遍历序列构造二叉树 | 中等 | 树 | [链接](https://leetcode.cn/problems/construct-binary-tree-from-preorder-and-inorder-traversal/) |
| 121 | 买卖股票的最佳时机 | 简单 | 数组、动态规划 | [链接](https://leetcode.cn/problems/best-time-to-buy-and-sell-stock/) |
| 124 | 二叉树中的最大路径和 | 困难 | 树 | [链接](https://leetcode.cn/problems/binary-tree-maximum-path-sum/) |
| 128 | 最长连续序列 | 中等 | 数组、并查集 | [链接](https://leetcode.cn/problems/longest-consecutive-sequence/) |
| 136 | 只出现一次的数字 | 简单 | 位运算 | [链接](https://leetcode.cn/problems/single-number/) |
| 141 | 环形链表 | 简单 | 链表、双指针 | [链接](https://leetcode.cn/problems/linked-list-cycle/) |
| 142 | 环形链表 II | 中等 | 链表、双指针 | [链接](https://leetcode.cn/problems/linked-list-cycle-ii/) |
| 146 | LRU缓存机制 | 中等 | 设计 | [链接](https://leetcode.cn/problems/lru-cache/) |
| 148 | 排序链表 | 中等 | 链表、排序 | [链接](https://leetcode.cn/problems/sort-list/) |
| 152 | 乘积最大子数组 | 中等 | 数组、动态规划 | [链接](https://leetcode.cn/problems/maximum-product-subarray/) |
| 155 | 最小栈 | 简单 | 栈、设计 | [链接](https://leetcode.cn/problems/min-stack/) |
| 160 | 相交链表 | 简单 | 链表、双指针 | [链接](https://leetcode.cn/problems/intersection-of-two-linked-lists/) |
| 169 | 多数元素 | 简单 | 数组 | [链接](https://leetcode.cn/problems/majority-element/) |
| 198 | 打家劫舍 | 中等 | 动态规划 | [链接](https://leetcode.cn/problems/house-robber/) |
| 200 | 岛屿数量 | 中等 | 图、DFS/BFS | [链接](https://leetcode.cn/problems/number-of-islands/) |
| 206 | 反转链表 | 简单 | 链表 | [链接](https://leetcode.cn/problems/reverse-linked-list/) |
| 208 | 实现 Trie (前缀树) | 中等 | 设计、树 | [链接](https://leetcode.cn/problems/implement-trie-prefix-tree/) |
| 215 | 数组中的第K个最大元素 | 中等 | 数组、堆、快速选择 | [链接](https://leetcode.cn/problems/kth-largest-element-in-an-array/) |

---

## 七、按题型分类汇总

### 链表（绝对重点）

| 题目 | 出现次数 | 难度 | 核心技巧 |
|------|----------|------|----------|
| K个一组翻转链表 | 60次 | 困难 | 递归/迭代翻转 |
| 反转链表 | 51次 | 简单 | 三指针迭代 |
| 相交链表 | 32次 | 简单 | 双指针 |
| 重排链表 | 24次 | 中等 | 快慢指针+翻转+合并 |
| 合并K个排序链表 | 23次 | 困难 | 优先队列/分治 |
| 合并两个有序链表 | 22次 | 简单 | 递归/迭代 |
| 反转链表 II | 20次 | 中等 | 区间翻转 |
| 环形链表 | 18次 | 简单 | 快慢指针 |
| 环形链表 II | 13次 | 中等 | 快慢指针+数学 |

### 二叉树（高频）

| 题目 | 出现次数 | 难度 | 核心技巧 |
|------|----------|------|----------|
| 二叉树的锯齿形层次遍历 | 47次 | 中等 | BFS+奇偶层判断 |
| 二叉树的最近公共祖先 | 32次 | 中等 | 递归 |
| 二叉树的右视图 | 24次 | 中等 | BFS/DFS |
| 从前序与中序遍历序列构造二叉树 | 19次 | 中等 | 递归+分治 |
| 二叉树中的最大路径和 | 18次 | 困难 | 递归 |
| 对称二叉树 | 16次 | 简单 | 递归/迭代 |
| 验证二叉搜索树 | 14次 | 中等 | 中序遍历 |
| 二叉树的层序遍历 | 13次 | 中等 | BFS |

### 数组/字符串（高频）

| 题目 | 出现次数 | 难度 | 核心技巧 |
|------|----------|------|----------|
| 无重复字符的最长子串 | 57次 | 中等 | 滑动窗口 |
| 数组中的第K个最大元素 | 52次 | 中等 | 快速选择/堆 |
| 三数之和 | 42次 | 中等 | 双指针 |
| 接雨水 | 31次 | 困难 | 双指针/动态规划 |
| 搜索旋转排序数组 | 27次 | 中等 | 二分查找 |
| 下一个排列 | 27次 | 中等 | 双指针 |
| 螺旋矩阵 | 24次 | 中等 | 模拟 |
| 合并区间 | 16次 | 中等 | 排序+贪心 |

### 动态规划（高频）

| 题目 | 出现次数 | 难度 | 核心技巧 |
|------|----------|------|----------|
| 买卖股票的最佳时机 | 41次 | 简单 | 一次遍历 |
| 最长上升子序列 | 21次 | 中等 | 动态规划+二分 |
| 最大子序和 | 13次 | 简单 | Kadane算法 |
| 零钱兑换 | 13次 | 中等 | 完全背包 |
| 最长有效括号 | 12次 | 困难 | 动态规划/栈 |
| 打家劫舍 | 12次 | 中等 | 动态规划 |
| 编辑距离 | 10次 | 困难 | 二维动态规划 |

### 回溯（高频）

| 题目 | 出现次数 | 难度 | 核心技巧 |
|------|----------|------|----------|
| 全排列 | 16次 | 中等 | 回溯+去重 |
| 子集 | 12次 | 中等 | 回溯 |
| 组合总和 | 14次 | 中等 | 回溯+剪枝 |
| N皇后 | 10次 | 困难 | 回溯+剪枝 |
| 解数独 | 10次 | 困难 | 回溯+剪枝 |

### 设计题（高频）

| 题目 | 出现次数 | 难度 | 核心技巧 |
|------|----------|------|----------|
| LRU缓存机制 | 53次 | 中等 | 哈希表+双向链表 |
| 最小栈 | 16次 | 简单 | 辅助栈 |
| 用栈实现队列 | 12次 | 简单 | 双栈 |
| 实现Trie | 10次 | 中等 | 前缀树 |

---

## 八、备考建议

### 1. 优先级策略

**第一优先级（≥40次）**：必须熟练掌握，这8道题是面试重点
- K个一组翻转链表
- 无重复字符的最长子串
- LRU缓存机制
- 数组中的第K个最大元素
- 反转链表
- 二叉树的锯齿形层次遍历
- 三数之和
- 买卖股票的最佳时机

**第二优先级（20-39次）**：重点准备，共18道题
- 相交链表、二叉树的最近公共祖先、接雨水等

**第三优先级（10-19次）**：理解思路，共36道题
- 全排列、子集、组合总和等

**第四优先级（<10次）**：时间充裕时练习

### 2. 题型重点

- **链表操作**：反转、合并、相交等（字节最爱考）
- **二叉树遍历**：各种遍历方式及变形
- **数组算法**：双指针、滑动窗口、二分查找
- **动态规划**：经典DP问题
- **系统设计**：LRU、LFU等缓存设计

### 3. 刷题顺序建议

1. 先刷完前26道高频题（≥20次）
2. 再刷中频题（10-19次）
3. 按题型分类刷题，形成知识体系
4. 定期复习已做过的题目

### 4. 面试技巧

- 重点关注时间复杂度和空间复杂度优化
- 准备多种解法，从暴力到最优
- 注意边界条件和特殊情况处理
- 代码要规范，变量命名要清晰

---

## 九、总结

本文档汇总了字节跳动、阿里巴巴、快手、百度等互联网大厂的LeetCode高频面试题目，包含：

1. **数据概览**：各公司题目统计和超高频题目
2. **字节跳动高频题**：346道题目中的高频题详解
3. **阿里巴巴高频题**：18道题目，动态规划占比28%
4. **快手高频题**：30+道题目，链表是绝对重点
5. **百度高频题**：19道题目，数组中的第K个最大元素频度最高
6. **通用高频Top 200**：精选1-50题
7. **按题型分类汇总**：链表、二叉树、数组、动态规划、回溯、设计题
8. **备考建议**：优先级策略、题型重点、刷题顺序、面试技巧

掌握这些高频题目，你就抓住了大厂面试的核心！祝你面试顺利！
