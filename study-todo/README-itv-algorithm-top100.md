# LeetCode Top 100 常见题目实现

本文档包含 LeetCode 常见的 100 道算法题目，涵盖了面试中经常出现的各种类型的问题。每个题目都包含详细的题目描述、原题链接、解决思路、伪代码实现和完整的 Java 代码实现。

## 目录

- [一、数组（15题）](#一数组15题)
- [二、链表（15题）](#二链表15题)
- [三、二叉树（15题）](#三二叉树15题)
- [四、动态规划（15题）](#四动态规划15题)
- [五、字符串（15题）](#五字符串15题)
- [六、其他类型（25题）](#六其他类型25题)

---

## 一、数组（15题）

### 1. 两数之和

**题目链接**：[1. 两数之和](https://leetcode.cn/problems/two-sum/)

**题目描述**：给定一个整数数组 nums 和一个整数目标值 target，请你在该数组中找出和为目标值 target 的那两个整数，并返回它们的数组下标。你可以假设每种输入只会对应一个答案。但是，数组中同一个元素在答案里不能重复出现。你可以按任意顺序返回答案。

**解决思路**：
1. **哈希表**：使用哈希表存储已遍历的数值及其索引
2. 对于当前数值，检查 target - nums[i] 是否在哈希表中

**伪代码实现**：
```
function twoSum(nums, target):
    map = new HashMap()
    
    for i from 0 to nums.length - 1:
        complement = target - nums[i]
        if map.containsKey(complement):
            return [map.get(complement), i]
        map.put(nums[i], i)
    
    return []
```

**Java代码实现**：
```java
class Solution {
    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (map.containsKey(complement)) {
                return new int[]{map.get(complement), i};
            }
            map.put(nums[i], i);
        }
        
        return new int[0];
    }
}
```

---

### 2. 三数之和

**题目链接**：[15. 三数之和](https://leetcode.cn/problems/3sum/)

**题目描述**：给你一个整数数组 nums，判断是否存在三元组 [nums[i], nums[j], nums[k]] 满足 i != j、i != k 且 j != k，同时还满足 nums[i] + nums[j] + nums[k] == 0。请你返回所有和为 0 且不重复的三元组。

**解决思路**：
1. **排序 + 双指针**：先对数组排序
2. 固定一个数，然后用双指针找另外两个数
3. 注意去重

**伪代码实现**：
```
function threeSum(nums):
    result = new List()
    sort(nums)
    
    for i from 0 to nums.length - 3:
        if i > 0 and nums[i] == nums[i-1]: continue
        
        left = i + 1
        right = nums.length - 1
        
        while left < right:
            sum = nums[i] + nums[left] + nums[right]
            
            if sum == 0:
                result.add([nums[i], nums[left], nums[right]])
                while left < right and nums[left] == nums[left+1]: left++
                while left < right and nums[right] == nums[right-1]: right--
                left++
                right--
            else if sum < 0:
                left++
            else:
                right--
    
    return result
```

**Java代码实现**：
```java
class Solution {
    public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        Arrays.sort(nums);
        
        for (int i = 0; i < nums.length - 2; i++) {
            if (i > 0 && nums[i] == nums[i - 1]) continue;
            
            int left = i + 1;
            int right = nums.length - 1;
            
            while (left < right) {
                int sum = nums[i] + nums[left] + nums[right];
                
                if (sum == 0) {
                    result.add(Arrays.asList(nums[i], nums[left], nums[right]));
                    while (left < right && nums[left] == nums[left + 1]) left++;
                    while (left < right && nums[right] == nums[right - 1]) right--;
                    left++;
                    right--;
                } else if (sum < 0) {
                    left++;
                } else {
                    right--;
                }
            }
        }
        
        return result;
    }
}
```

---

### 3. 四数之和

**题目链接**：[18. 四数之和](https://leetcode.cn/problems/4sum/)

**题目描述**：给你一个由 n 个整数组成的数组 nums，和一个目标值 target。请你找出并返回满足下述全部条件且不重复的四元组 [nums[a], nums[b], nums[c], nums[d]]。

**解决思路**：
1. **排序 + 双指针**：类似三数之和，多一层循环
2. 注意剪枝和去重

**伪代码实现**：
```
function fourSum(nums, target):
    result = new List()
    sort(nums)
    
    for i from 0 to nums.length - 4:
        if i > 0 and nums[i] == nums[i-1]: continue
        
        for j from i + 1 to nums.length - 3:
            if j > i + 1 and nums[j] == nums[j-1]: continue
            
            left = j + 1
            right = nums.length - 1
            
            while left < right:
                sum = nums[i] + nums[j] + nums[left] + nums[right]
                
                if sum == target:
                    result.add([nums[i], nums[j], nums[left], nums[right]])
                    while left < right and nums[left] == nums[left+1]: left++
                    while left < right and nums[right] == nums[right-1]: right--
                    left++
                    right--
                else if sum < target:
                    left++
                else:
                    right--
    
    return result
```

**Java代码实现**：
```java
class Solution {
    public List<List<Integer>> fourSum(int[] nums, int target) {
        List<List<Integer>> result = new ArrayList<>();
        Arrays.sort(nums);
        
        for (int i = 0; i < nums.length - 3; i++) {
            if (i > 0 && nums[i] == nums[i - 1]) continue;
            
            for (int j = i + 1; j < nums.length - 2; j++) {
                if (j > i + 1 && nums[j] == nums[j - 1]) continue;
                
                int left = j + 1;
                int right = nums.length - 1;
                
                while (left < right) {
                    long sum = (long)nums[i] + nums[j] + nums[left] + nums[right];
                    
                    if (sum == target) {
                        result.add(Arrays.asList(nums[i], nums[j], nums[left], nums[right]));
                        while (left < right && nums[left] == nums[left + 1]) left++;
                        while (left < right && nums[right] == nums[right - 1]) right--;
                        left++;
                        right--;
                    } else if (sum < target) {
                        left++;
                    } else {
                        right--;
                    }
                }
            }
        }
        
        return result;
    }
}
```

---

### 4. 盛最多水的容器

**题目链接**：[11. 盛最多水的容器](https://leetcode.cn/problems/container-with-most-water/)

**题目描述**：给定一个长度为 n 的整数数组 height。有 n 条垂线，第 i 条线的两个端点是 (i, 0) 和 (i, height[i])。找出其中的两条线，使得它们与 x 轴共同构成的容器可以容纳最多的水。返回容器可以储存的最大水量。

**解决思路**：
1. **双指针**：从两端向中间移动
2. 每次移动较短的指针

**伪代码实现**：
```
function maxArea(height):
    left = 0
    right = height.length - 1
    maxArea = 0
    
    while left < right:
        area = min(height[left], height[right]) * (right - left)
        maxArea = max(maxArea, area)
        
        if height[left] < height[right]:
            left++
        else:
            right--
    
    return maxArea
```

**Java代码实现**：
```java
class Solution {
    public int maxArea(int[] height) {
        int left = 0;
        int right = height.length - 1;
        int maxArea = 0;
        
        while (left < right) {
            int area = Math.min(height[left], height[right]) * (right - left);
            maxArea = Math.max(maxArea, area);
            
            if (height[left] < height[right]) {
                left++;
            } else {
                right--;
            }
        }
        
        return maxArea;
    }
}
```

---

### 5. 下一个排列

**题目链接**：[31. 下一个排列](https://leetcode.cn/problems/next-permutation/)

**题目描述**：整数数组的一个排列就是将其所有成员以序列或线性顺序排列。例如，arr = [1,2,3]，以下这些都可以视作 arr 的排列：[1,2,3]、[1,3,2]、[3,1,2]、[2,3,1]。整数数组的下一个排列是指其整数的下一个字典序更大的排列。

**解决思路**：
1. 从后往前找第一个降序的数
2. 从后往前找第一个比它大的数
3. 交换并反转后面的序列

**伪代码实现**：
```
function nextPermutation(nums):
    i = nums.length - 2
    
    while i >= 0 and nums[i] >= nums[i+1]:
        i--
    
    if i >= 0:
        j = nums.length - 1
        while j >= 0 and nums[j] <= nums[i]:
            j--
        swap(nums, i, j)
    
    reverse(nums, i + 1, nums.length - 1)
```

**Java代码实现**：
```java
class Solution {
    public void nextPermutation(int[] nums) {
        int i = nums.length - 2;
        
        while (i >= 0 && nums[i] >= nums[i + 1]) {
            i--;
        }
        
        if (i >= 0) {
            int j = nums.length - 1;
            while (j >= 0 && nums[j] <= nums[i]) {
                j--;
            }
            swap(nums, i, j);
        }
        
        reverse(nums, i + 1, nums.length - 1);
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

### 6. 搜索旋转排序数组

**题目链接**：[33. 搜索旋转排序数组](https://leetcode.cn/problems/search-in-rotated-sorted-array/)

**题目描述**：整数数组 nums 按升序排列，数组中的值互不相同。在传递给函数之前，nums 在预先未知的某个下标 k（0 <= k < nums.length）上进行了旋转，使数组变为 [nums[k], nums[k+1], ..., nums[n-1], nums[0], nums[1], ..., nums[k-1]]。

**解决思路**：
1. **二分查找**：判断哪一半是有序的
2. 判断target在哪个区间

**伪代码实现**：
```
function search(nums, target):
    left = 0
    right = nums.length - 1
    
    while left <= right:
        mid = left + (right - left) / 2
        
        if nums[mid] == target:
            return mid
        
        if nums[left] <= nums[mid]:
            if nums[left] <= target and target < nums[mid]:
                right = mid - 1
            else:
                left = mid + 1
        else:
            if nums[mid] < target and target <= nums[right]:
                left = mid + 1
            else:
                right = mid - 1
    
    return -1
```

**Java代码实现**：
```java
class Solution {
    public int search(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            if (nums[mid] == target) {
                return mid;
            }
            
            if (nums[left] <= nums[mid]) {
                if (nums[left] <= target && target < nums[mid]) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            } else {
                if (nums[mid] < target && target <= nums[right]) {
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

### 7. 在排序数组中查找元素的第一个和最后一个位置

**题目链接**：[34. 在排序数组中查找元素的第一个和最后一个位置](https://leetcode.cn/problems/find-first-and-last-position-of-element-in-sorted-array/)

**题目描述**：给你一个按照非递减顺序排列的整数数组 nums，和一个目标值 target。请你找出给定目标值在数组中的开始位置和结束位置。

**解决思路**：
1. **二分查找**：分别查找左边界和右边界

**伪代码实现**：
```
function searchRange(nums, target):
    if nums.length == 0: return [-1, -1]
    
    left = findLeft(nums, target)
    if left == -1: return [-1, -1]
    
    right = findRight(nums, target)
    return [left, right]

function findLeft(nums, target):
    left = 0
    right = nums.length - 1
    
    while left <= right:
        mid = left + (right - left) / 2
        
        if nums[mid] == target:
            right = mid - 1
        else if nums[mid] < target:
            left = mid + 1
        else:
            right = mid - 1
    
    if left >= nums.length or nums[left] != target:
        return -1
    return left

function findRight(nums, target):
    left = 0
    right = nums.length - 1
    
    while left <= right:
        mid = left + (right - left) / 2
        
        if nums[mid] == target:
            left = mid + 1
        else if nums[mid] < target:
            left = mid + 1
        else:
            right = mid - 1
    
    if right < 0 or nums[right] != target:
        return -1
    return right
```

**Java代码实现**：
```java
class Solution {
    public int[] searchRange(int[] nums, int target) {
        if (nums.length == 0) return new int[]{-1, -1};
        
        int left = findLeft(nums, target);
        if (left == -1) return new int[]{-1, -1};
        
        int right = findRight(nums, target);
        return new int[]{left, right};
    }
    
    private int findLeft(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            if (nums[mid] == target) {
                right = mid - 1;
            } else if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        if (left >= nums.length || nums[left] != target) {
            return -1;
        }
        return left;
    }
    
    private int findRight(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            if (nums[mid] == target) {
                left = mid + 1;
            } else if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        if (right < 0 || nums[right] != target) {
            return -1;
        }
        return right;
    }
}
```

---

### 8. 组合总和

**题目链接**：[39. 组合总和](https://leetcode.cn/problems/combination-sum/)

**题目描述**：给你一个无重复元素的整数数组 candidates 和一个目标整数 target，找出 candidates 中可以使数字和为目标数 target 的所有不同组合。

**解决思路**：
1. **回溯**：使用回溯法枚举所有组合
2. 注意剪枝和去重

**伪代码实现**：
```
function combinationSum(candidates, target):
    result = new List()
    backtrack(candidates, target, 0, new List(), result)
    return result

function backtrack(candidates, target, start, path, result):
    if target == 0:
        result.add(new List(path))
        return
    
    if target < 0:
        return
    
    for i from start to candidates.length - 1:
        path.add(candidates[i])
        backtrack(candidates, target - candidates[i], i, path, result)
        path.removeLast()
```

**Java代码实现**：
```java
class Solution {
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> result = new ArrayList<>();
        backtrack(candidates, target, 0, new ArrayList<>(), result);
        return result;
    }
    
    private void backtrack(int[] candidates, int target, int start, List<Integer> path, List<List<Integer>> result) {
        if (target == 0) {
            result.add(new ArrayList<>(path));
            return;
        }
        
        if (target < 0) {
            return;
        }
        
        for (int i = start; i < candidates.length; i++) {
            path.add(candidates[i]);
            backtrack(candidates, target - candidates[i], i, path, result);
            path.remove(path.size() - 1);
        }
    }
}
```

---

### 9. 组合总和 II

**题目链接**：[40. 组合总和 II](https://leetcode.cn/problems/combination-sum-ii/)

**题目描述**：给定一个候选人编号的集合 candidates 和一个目标数 target，找出 candidates 中所有可以使数字和为 target 的组合。candidates 中的每个数字在每个组合中只能使用一次。

**解决思路**：
1. **回溯**：类似组合总和，但需要去重
2. 先排序，然后跳过重复元素

**伪代码实现**：
```
function combinationSum2(candidates, target):
    result = new List()
    sort(candidates)
    backtrack(candidates, target, 0, new List(), result)
    return result

function backtrack(candidates, target, start, path, result):
    if target == 0:
        result.add(new List(path))
        return
    
    if target < 0:
        return
    
    for i from start to candidates.length - 1:
        if i > start and candidates[i] == candidates[i-1]: continue
        
        path.add(candidates[i])
        backtrack(candidates, target - candidates[i], i + 1, path, result)
        path.removeLast()
```

**Java代码实现**：
```java
class Solution {
    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
        List<List<Integer>> result = new ArrayList<>();
        Arrays.sort(candidates);
        backtrack(candidates, target, 0, new ArrayList<>(), result);
        return result;
    }
    
    private void backtrack(int[] candidates, int target, int start, List<Integer> path, List<List<Integer>> result) {
        if (target == 0) {
            result.add(new ArrayList<>(path));
            return;
        }
        
        if (target < 0) {
            return;
        }
        
        for (int i = start; i < candidates.length; i++) {
            if (i > start && candidates[i] == candidates[i - 1]) continue;
            
            path.add(candidates[i]);
            backtrack(candidates, target - candidates[i], i + 1, path, result);
            path.remove(path.size() - 1);
        }
    }
}
```

---

### 10. 全排列

**题目链接**：[46. 全排列](https://leetcode.cn/problems/permutations/)

**题目描述**：给定一个不含重复数字的数组 nums，返回其所有可能的全排列。你可以按任意顺序返回答案。

**解决思路**：
1. **回溯**：使用回溯法枚举所有排列
2. 使用visited数组标记已访问的元素

**伪代码实现**：
```
function permute(nums):
    result = new List()
    visited = new boolean[nums.length]
    backtrack(nums, new List(), visited, result)
    return result

function backtrack(nums, path, visited, result):
    if path.length == nums.length:
        result.add(new List(path))
        return
    
    for i from 0 to nums.length - 1:
        if visited[i]: continue
        
        visited[i] = true
        path.add(nums[i])
        backtrack(nums, path, visited, result)
        path.removeLast()
        visited[i] = false
```

**Java代码实现**：
```java
class Solution {
    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        boolean[] visited = new boolean[nums.length];
        backtrack(nums, new ArrayList<>(), visited, result);
        return result;
    }
    
    private void backtrack(int[] nums, List<Integer> path, boolean[] visited, List<List<Integer>> result) {
        if (path.size() == nums.length) {
            result.add(new ArrayList<>(path));
            return;
        }
        
        for (int i = 0; i < nums.length; i++) {
            if (visited[i]) continue;
            
            visited[i] = true;
            path.add(nums[i]);
            backtrack(nums, path, visited, result);
            path.remove(path.size() - 1);
            visited[i] = false;
        }
    }
}
```

---

### 11. 全排列 II

**题目链接**：[47. 全排列 II](https://leetcode.cn/problems/permutations-ii/)

**题目描述**：给定一个可包含重复数字的序列 nums，按任意顺序返回所有不重复的全排列。

**解决思路**：
1. **回溯**：类似全排列，但需要去重
2. 先排序，然后跳过重复元素

**伪代码实现**：
```
function permuteUnique(nums):
    result = new List()
    sort(nums)
    visited = new boolean[nums.length]
    backtrack(nums, new List(), visited, result)
    return result

function backtrack(nums, path, visited, result):
    if path.length == nums.length:
        result.add(new List(path))
        return
    
    for i from 0 to nums.length - 1:
        if visited[i]: continue
        if i > 0 and nums[i] == nums[i-1] and not visited[i-1]: continue
        
        visited[i] = true
        path.add(nums[i])
        backtrack(nums, path, visited, result)
        path.removeLast()
        visited[i] = false
```

**Java代码实现**：
```java
class Solution {
    public List<List<Integer>> permuteUnique(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        Arrays.sort(nums);
        boolean[] visited = new boolean[nums.length];
        backtrack(nums, new ArrayList<>(), visited, result);
        return result;
    }
    
    private void backtrack(int[] nums, List<Integer> path, boolean[] visited, List<List<Integer>> result) {
        if (path.size() == nums.length) {
            result.add(new ArrayList<>(path));
            return;
        }
        
        for (int i = 0; i < nums.length; i++) {
            if (visited[i]) continue;
            if (i > 0 && nums[i] == nums[i - 1] && !visited[i - 1]) continue;
            
            visited[i] = true;
            path.add(nums[i]);
            backtrack(nums, path, visited, result);
            path.remove(path.size() - 1);
            visited[i] = false;
        }
    }
}
```

---

### 12. 子集

**题目链接**：[78. 子集](https://leetcode.cn/problems/subsets/)

**题目描述**：给你一个整数数组 nums，数组中的元素互不相同。返回该数组所有可能的子集（幂集）。解集不能包含重复的子集。

**解决思路**：
1. **回溯**：每个元素都有选或不选两种情况

**伪代码实现**：
```
function subsets(nums):
    result = new List()
    backtrack(nums, 0, new List(), result)
    return result

function backtrack(nums, start, path, result):
    result.add(new List(path))
    
    for i from start to nums.length - 1:
        path.add(nums[i])
        backtrack(nums, i + 1, path, result)
        path.removeLast()
```

**Java代码实现**：
```java
class Solution {
    public List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        backtrack(nums, 0, new ArrayList<>(), result);
        return result;
    }
    
    private void backtrack(int[] nums, int start, List<Integer> path, List<List<Integer>> result) {
        result.add(new ArrayList<>(path));
        
        for (int i = start; i < nums.length; i++) {
            path.add(nums[i]);
            backtrack(nums, i + 1, path, result);
            path.remove(path.size() - 1);
        }
    }
}
```

---

### 13. 子集 II

**题目链接**：[90. 子集 II](https://leetcode.cn/problems/subsets-ii/)

**题目描述**：给你一个整数数组 nums，其中可能包含重复元素，请你返回该数组所有可能的子集（幂集）。解集不能包含重复的子集。

**解决思路**：
1. **回溯**：类似子集，但需要去重
2. 先排序，然后跳过重复元素

**伪代码实现**：
```
function subsetsWithDup(nums):
    result = new List()
    sort(nums)
    backtrack(nums, 0, new List(), result)
    return result

function backtrack(nums, start, path, result):
    result.add(new List(path))
    
    for i from start to nums.length - 1:
        if i > start and nums[i] == nums[i-1]: continue
        
        path.add(nums[i])
        backtrack(nums, i + 1, path, result)
        path.removeLast()
```

**Java代码实现**：
```java
class Solution {
    public List<List<Integer>> subsetsWithDup(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        Arrays.sort(nums);
        backtrack(nums, 0, new ArrayList<>(), result);
        return result;
    }
    
    private void backtrack(int[] nums, int start, List<Integer> path, List<List<Integer>> result) {
        result.add(new ArrayList<>(path));
        
        for (int i = start; i < nums.length; i++) {
            if (i > start && nums[i] == nums[i - 1]) continue;
            
            path.add(nums[i]);
            backtrack(nums, i + 1, path, result);
            path.remove(path.size() - 1);
        }
    }
}
```

---

### 14. 接雨水

**题目链接**：[42. 接雨水](https://leetcode.cn/problems/trapping-rain-water/)

**题目描述**：给定 n 个非负整数表示每个宽度为 1 的柱子的高度图，计算按此排列的柱子，下雨之后能接多少雨水。

**解决思路**：
1. **双指针**：左右两个指针向中间移动
2. 记录左右两边的最大高度

**伪代码实现**：
```
function trap(height):
    if height.length == 0: return 0
    
    left = 0
    right = height.length - 1
    leftMax = 0
    rightMax = 0
    water = 0
    
    while left < right:
        if height[left] < height[right]:
            if height[left] >= leftMax:
                leftMax = height[left]
            else:
                water += leftMax - height[left]
            left++
        else:
            if height[right] >= rightMax:
                rightMax = height[right]
            else:
                water += rightMax - height[right]
            right--
    
    return water
```

**Java代码实现**：
```java
class Solution {
    public int trap(int[] height) {
        if (height.length == 0) return 0;
        
        int left = 0;
        int right = height.length - 1;
        int leftMax = 0;
        int rightMax = 0;
        int water = 0;
        
        while (left < right) {
            if (height[left] < height[right]) {
                if (height[left] >= leftMax) {
                    leftMax = height[left];
                } else {
                    water += leftMax - height[left];
                }
                left++;
            } else {
                if (height[right] >= rightMax) {
                    rightMax = height[right];
                } else {
                    water += rightMax - height[right];
                }
                right--;
            }
        }
        
        return water;
    }
}
```

---

### 15. 移动零

**题目链接**：[283. 移动零](https://leetcode.cn/problems/move-zeroes/)

**题目描述**：给定一个数组 nums，编写一个函数将所有 0 移动到数组的末尾，同时保持非零元素的相对顺序。

**解决思路**：
1. **双指针**：一个指针记录非零元素的位置
2. 遍历数组，将非零元素移到前面

**伪代码实现**：
```
function moveZeroes(nums):
    j = 0
    
    for i from 0 to nums.length - 1:
        if nums[i] != 0:
            nums[j] = nums[i]
            j++
    
    for i from j to nums.length - 1:
        nums[i] = 0
```

**Java代码实现**：
```java
class Solution {
    public void moveZeroes(int[] nums) {
        int j = 0;
        
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != 0) {
                nums[j] = nums[i];
                j++;
            }
        }
        
        for (int i = j; i < nums.length; i++) {
            nums[i] = 0;
        }
    }
}
```

---

## 二、链表（15题）

### 16. 反转链表

**题目链接**：[206. 反转链表](https://leetcode.cn/problems/reverse-linked-list/)

**题目描述**：给你单链表的头节点 head，请你反转链表，并返回反转后的链表。

**解决思路**：
1. **迭代**：使用三个指针prev、curr、next
2. **递归**：递归到链表末尾，然后逐个反转

**伪代码实现（迭代）**：
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

---

### 17. 合并两个有序链表

**题目链接**：[21. 合并两个有序链表](https://leetcode.cn/problems/merge-two-sorted-lists/)

**题目描述**：将两个升序链表合并为一个新的升序链表并返回。新链表是通过拼接给定的两个链表的所有节点组成的。

**解决思路**：
1. **迭代**：使用虚拟头节点，逐个比较节点值
2. **递归**：比较头节点，递归合并剩余部分

**伪代码实现（迭代）**：
```
function mergeTwoLists(l1, l2):
    dummy = new ListNode(0)
    curr = dummy
    
    while l1 != null and l2 != null:
        if l1.val <= l2.val:
            curr.next = l1
            l1 = l1.next
        else:
            curr.next = l2
            l2 = l2.next
        curr = curr.next
    
    curr.next = l1 != null ? l1 : l2
    
    return dummy.next
```

**Java代码实现（迭代）**：
```java
class Solution {
    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(0);
        ListNode curr = dummy;
        
        while (l1 != null && l2 != null) {
            if (l1.val <= l2.val) {
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

---

### 18. 合并K个升序链表

**题目链接**：[23. 合并K个升序链表](https://leetcode.cn/problems/merge-k-sorted-lists/)

**题目描述**：给你一个链表数组，每个链表都已经按升序排列。请你将所有链表合并到一个升序链表中，返回合并后的链表。

**解决思路**：
1. **优先队列**：使用最小堆维护所有链表的头节点
2. **分治**：两两合并链表

**伪代码实现（优先队列）**：
```
function mergeKLists(lists):
    pq = new PriorityQueue((a, b) -> a.val - b.val)
    
    for list in lists:
        if list != null:
            pq.offer(list)
    
    dummy = new ListNode(0)
    curr = dummy
    
    while not pq.isEmpty():
        node = pq.poll()
        curr.next = node
        curr = curr.next
        
        if node.next != null:
            pq.offer(node.next)
    
    return dummy.next
```

**Java代码实现（优先队列）**：
```java
class Solution {
    public ListNode mergeKLists(ListNode[] lists) {
        PriorityQueue<ListNode> pq = new PriorityQueue<>((a, b) -> a.val - b.val);
        
        for (ListNode list : lists) {
            if (list != null) {
                pq.offer(list);
            }
        }
        
        ListNode dummy = new ListNode(0);
        ListNode curr = dummy;
        
        while (!pq.isEmpty()) {
            ListNode node = pq.poll();
            curr.next = node;
            curr = curr.next;
            
            if (node.next != null) {
                pq.offer(node.next);
            }
        }
        
        return dummy.next;
    }
}
```

---

### 19. 环形链表

**题目链接**：[141. 环形链表](https://leetcode.cn/problems/linked-list-cycle/)

**题目描述**：给你一个链表的头节点 head，判断链表中是否有环。

**解决思路**：
1. **快慢指针**：快指针每次走两步，慢指针每次走一步
2. 如果有环，快慢指针会相遇

**伪代码实现**：
```
function hasCycle(head):
    if head == null or head.next == null: return false
    
    slow = head
    fast = head
    
    while fast != null and fast.next != null:
        slow = slow.next
        fast = fast.next.next
        
        if slow == fast:
            return true
    
    return false
```

**Java代码实现**：
```java
public class Solution {
    public boolean hasCycle(ListNode head) {
        if (head == null || head.next == null) return false;
        
        ListNode slow = head;
        ListNode fast = head;
        
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            
            if (slow == fast) {
                return true;
            }
        }
        
        return false;
    }
}
```

---

### 20. 环形链表 II

**题目链接**：[142. 环形链表 II](https://leetcode.cn/problems/linked-list-cycle-ii/)

**题目描述**：给定一个链表的头节点 head，返回链表开始入环的第一个节点。如果链表无环，则返回 null。

**解决思路**：
1. **快慢指针**：先用快慢指针找到相遇点
2. 然后一个指针从头开始，另一个从相遇点开始，再次相遇即为环入口

**伪代码实现**：
```
function detectCycle(head):
    if head == null or head.next == null: return null
    
    slow = head
    fast = head
    
    while fast != null and fast.next != null:
        slow = slow.next
        fast = fast.next.next
        
        if slow == fast:
            slow = head
            while slow != fast:
                slow = slow.next
                fast = fast.next
            return slow
    
    return null
```

**Java代码实现**：
```java
public class Solution {
    public ListNode detectCycle(ListNode head) {
        if (head == null || head.next == null) return null;
        
        ListNode slow = head;
        ListNode fast = head;
        
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            
            if (slow == fast) {
                slow = head;
                while (slow != fast) {
                    slow = slow.next;
                    fast = fast.next;
                }
                return slow;
            }
        }
        
        return null;
    }
}
```

---

### 21. 相交链表

**题目链接**：[160. 相交链表](https://leetcode.cn/problems/intersection-of-two-linked-lists/)

**题目描述**：给你两个单链表的头节点 headA 和 headB，请你找出并返回两个单链表相交的起始节点。如果两个链表不存在相交节点，返回 null。

**解决思路**：
1. **双指针**：两个指针分别遍历两个链表
2. 遍历完一个链表后，从另一个链表头部重新开始

**伪代码实现**：
```
function getIntersectionNode(headA, headB):
    if headA == null or headB == null: return null
    
    pA = headA
    pB = headB
    
    while pA != pB:
        pA = pA == null ? headB : pA.next
        pB = pB == null ? headA : pB.next
    
    return pA
```

**Java代码实现**：
```java
public class Solution {
    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        if (headA == null || headB == null) return null;
        
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

### 22. 删除链表的倒数第N个节点

**题目链接**：[19. 删除链表的倒数第N个节点](https://leetcode.cn/problems/remove-nth-node-from-end-of-list/)

**题目描述**：给你一个链表，删除链表的倒数第 n 个节点，并且返回链表的头节点。

**解决思路**：
1. **双指针**：快指针先走n步，然后快慢指针一起走
2. 当快指针到达末尾时，慢指针指向倒数第n+1个节点

**伪代码实现**：
```
function removeNthFromEnd(head, n):
    dummy = new ListNode(0)
    dummy.next = head
    fast = dummy
    slow = dummy
    
    for i from 0 to n:
        fast = fast.next
    
    while fast.next != null:
        fast = fast.next
        slow = slow.next
    
    slow.next = slow.next.next
    
    return dummy.next
```

**Java代码实现**：
```java
class Solution {
    public ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode fast = dummy;
        ListNode slow = dummy;
        
        for (int i = 0; i <= n; i++) {
            fast = fast.next;
        }
        
        while (fast != null) {
            fast = fast.next;
            slow = slow.next;
        }
        
        slow.next = slow.next.next;
        
        return dummy.next;
    }
}
```

---

### 23. 删除排序链表中的重复元素

**题目链接**：[83. 删除排序链表中的重复元素](https://leetcode.cn/problems/remove-duplicates-from-sorted-list/)

**题目描述**：给定一个已排序的链表的头 head，删除所有重复的元素，使每个元素只出现一次。返回已排序的链表。

**解决思路**：
1. **遍历**：如果当前节点值等于下一个节点值，删除下一个节点

**伪代码实现**：
```
function deleteDuplicates(head):
    curr = head
    
    while curr != null and curr.next != null:
        if curr.val == curr.next.val:
            curr.next = curr.next.next
        else:
            curr = curr.next
    
    return head
```

**Java代码实现**：
```java
class Solution {
    public ListNode deleteDuplicates(ListNode head) {
        ListNode curr = head;
        
        while (curr != null && curr.next != null) {
            if (curr.val == curr.next.val) {
                curr.next = curr.next.next;
            } else {
                curr = curr.next;
            }
        }
        
        return head;
    }
}
```

---

### 24. 删除排序链表中的重复元素 II

**题目链接**：[82. 删除排序链表中的重复元素 II](https://leetcode.cn/problems/remove-duplicates-from-sorted-list-ii/)

**题目描述**：给定一个已排序的链表的头 head，删除原始链表中所有重复数字的节点，只留下不同的数字。返回已排序的链表。

**解决思路**：
1. **遍历**：使用虚拟头节点，跳过所有重复的节点

**伪代码实现**：
```
function deleteDuplicates(head):
    dummy = new ListNode(0)
    dummy.next = head
    curr = dummy
    
    while curr.next != null and curr.next.next != null:
        if curr.next.val == curr.next.next.val:
            val = curr.next.val
            while curr.next != null and curr.next.val == val:
                curr.next = curr.next.next
        else:
            curr = curr.next
    
    return dummy.next
```

**Java代码实现**：
```java
class Solution {
    public ListNode deleteDuplicates(ListNode head) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode curr = dummy;
        
        while (curr.next != null && curr.next.next != null) {
            if (curr.next.val == curr.next.next.val) {
                int val = curr.next.val;
                while (curr.next != null && curr.next.val == val) {
                    curr.next = curr.next.next;
                }
            } else {
                curr = curr.next;
            }
        }
        
        return dummy.next;
    }
}
```

---

### 25. 反转链表 II

**题目链接**：[92. 反转链表 II](https://leetcode.cn/problems/reverse-linked-list-ii/)

**题目描述**：给你单链表的头指针 head 和两个整数 left 和 right，其中 left <= right。请你反转从位置 left 到位置 right 的链表节点，返回反转后的链表。

**解决思路**：
1. **遍历**：找到left位置的前一个节点
2. 反转left到right之间的节点

**伪代码实现**：
```
function reverseBetween(head, left, right):
    dummy = new ListNode(0)
    dummy.next = head
    prev = dummy
    
    for i from 0 to left - 2:
        prev = prev.next
    
    curr = prev.next
    
    for i from 0 to right - left:
        next = curr.next
        curr.next = next.next
        next.next = prev.next
        prev.next = next
    
    return dummy.next
```

**Java代码实现**：
```java
class Solution {
    public ListNode reverseBetween(ListNode head, int left, int right) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode prev = dummy;
        
        for (int i = 0; i < left - 1; i++) {
            prev = prev.next;
        }
        
        ListNode curr = prev.next;
        
        for (int i = 0; i < right - left; i++) {
            ListNode next = curr.next;
            curr.next = next.next;
            next.next = prev.next;
            prev.next = next;
        }
        
        return dummy.next;
    }
}
```

---

### 26. 两两交换链表中的节点

**题目链接**：[24. 两两交换链表中的节点](https://leetcode.cn/problems/swap-nodes-in-pairs/)

**题目描述**：给你一个链表，两两交换其中相邻的节点，并返回交换后链表的头节点。你必须在不修改节点内部的值的情况下完成本题（即，只能进行节点交换）。

**解决思路**：
1. **迭代**：使用虚拟头节点，每次交换两个节点
2. **递归**：递归交换后续节点

**伪代码实现（迭代）**：
```
function swapPairs(head):
    dummy = new ListNode(0)
    dummy.next = head
    prev = dummy
    
    while prev.next != null and prev.next.next != null:
        first = prev.next
        second = prev.next.next
        
        first.next = second.next
        second.next = first
        prev.next = second
        
        prev = first
    
    return dummy.next
```

**Java代码实现（迭代）**：
```java
class Solution {
    public ListNode swapPairs(ListNode head) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode prev = dummy;
        
        while (prev.next != null && prev.next.next != null) {
            ListNode first = prev.next;
            ListNode second = prev.next.next;
            
            first.next = second.next;
            second.next = first;
            prev.next = second;
            
            prev = first;
        }
        
        return dummy.next;
    }
}
```

---

### 27. K个一组翻转链表

**题目链接**：[25. K个一组翻转链表](https://leetcode.cn/problems/reverse-nodes-in-k-group/)

**题目描述**：给你链表的头节点 head，每 k 个节点一组进行翻转，请你返回修改后的链表。

**解决思路**：
1. **迭代**：先判断是否有k个节点，然后反转这k个节点
2. 连接反转后的链表

**伪代码实现**：
```
function reverseKGroup(head, k):
    dummy = new ListNode(0)
    dummy.next = head
    prev = dummy
    
    while true:
        last = prev
        for i from 0 to k:
            last = last.next
            if last == null: return dummy.next
        
        curr = prev.next
        next = curr.next
        
        for i from 0 to k - 1:
            curr.next = next.next
            next.next = prev.next
            prev.next = next
            next = curr.next
        
        prev = curr
    
    return dummy.next
```

**Java代码实现**：
```java
class Solution {
    public ListNode reverseKGroup(ListNode head, int k) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode prev = dummy;
        
        while (true) {
            ListNode last = prev;
            for (int i = 0; i < k; i++) {
                last = last.next;
                if (last == null) return dummy.next;
            }
            
            ListNode curr = prev.next;
            ListNode next = curr.next;
            
            for (int i = 0; i < k - 1; i++) {
                curr.next = next.next;
                next.next = prev.next;
                prev.next = next;
                next = curr.next;
            }
            
            prev = curr;
        }
    }
}
```

---

### 28. 分隔链表

**题目链接**：[86. 分隔链表](https://leetcode.cn/problems/partition-list/)

**题目描述**：给你一个链表的头节点 head 和一个特定值 x，请你对链表进行分隔，使得所有小于 x 的节点都出现在大于或等于 x 的节点之前。

**解决思路**：
1. **双指针**：维护两个链表，一个存小于x的节点，一个存大于等于x的节点
2. 最后连接两个链表

**伪代码实现**：
```
function partition(head, x):
    lessDummy = new ListNode(0)
    greaterDummy = new ListNode(0)
    less = lessDummy
    greater = greaterDummy
    
    while head != null:
        if head.val < x:
            less.next = head
            less = less.next
        else:
            greater.next = head
            greater = greater.next
        head = head.next
    
    greater.next = null
    less.next = greaterDummy.next
    
    return lessDummy.next
```

**Java代码实现**：
```java
class Solution {
    public ListNode partition(ListNode head, int x) {
        ListNode lessDummy = new ListNode(0);
        ListNode greaterDummy = new ListNode(0);
        ListNode less = lessDummy;
        ListNode greater = greaterDummy;
        
        while (head != null) {
            if (head.val < x) {
                less.next = head;
                less = less.next;
            } else {
                greater.next = head;
                greater = greater.next;
            }
            head = head.next;
        }
        
        greater.next = null;
        less.next = greaterDummy.next;
        
        return lessDummy.next;
    }
}
```

---

### 29. 排序链表

**题目链接**：[148. 排序链表](https://leetcode.cn/problems/sort-list/)

**题目描述**：给你链表的头结点 head，请将其按升序排列并返回排序后的链表。

**解决思路**：
1. **归并排序**：找到链表中点，递归排序左右两部分，然后合并

**伪代码实现**：
```
function sortList(head):
    if head == null or head.next == null: return head
    
    slow = head
    fast = head.next
    
    while fast != null and fast.next != null:
        slow = slow.next
        fast = fast.next.next
    
    right = slow.next
    slow.next = null
    
    left = sortList(head)
    right = sortList(right)
    
    return merge(left, right)

function merge(l1, l2):
    dummy = new ListNode(0)
    curr = dummy
    
    while l1 != null and l2 != null:
        if l1.val <= l2.val:
            curr.next = l1
            l1 = l1.next
        else:
            curr.next = l2
            l2 = l2.next
        curr = curr.next
    
    curr.next = l1 != null ? l1 : l2
    
    return dummy.next
```

**Java代码实现**：
```java
class Solution {
    public ListNode sortList(ListNode head) {
        if (head == null || head.next == null) return head;
        
        ListNode slow = head;
        ListNode fast = head.next;
        
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        
        ListNode right = slow.next;
        slow.next = null;
        
        ListNode left = sortList(head);
        right = sortList(right);
        
        return merge(left, right);
    }
    
    private ListNode merge(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(0);
        ListNode curr = dummy;
        
        while (l1 != null && l2 != null) {
            if (l1.val <= l2.val) {
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

---

### 30. LRU缓存机制

**题目链接**：[146. LRU缓存机制](https://leetcode.cn/problems/lru-cache/)

**题目描述**：请你设计并实现一个满足 LRU (最近最少使用) 缓存约束的数据结构。

**解决思路**：
1. **哈希表 + 双向链表**：哈希表实现O(1)查找，双向链表维护访问顺序

**伪代码实现**：
```
class LRUCache:
    capacity
    map = new HashMap()
    head = new Node()
    tail = new Node()
    
    constructor(capacity):
        this.capacity = capacity
        head.next = tail
        tail.prev = head
    
    function get(key):
        if not map.containsKey(key): return -1
        
        node = map.get(key)
        moveToHead(node)
        return node.value
    
    function put(key, value):
        if map.containsKey(key):
            node = map.get(key)
            node.value = value
            moveToHead(node)
        else:
            node = new Node(key, value)
            map.put(key, node)
            addToHead(node)
            
            if map.size() > capacity:
                removed = removeTail()
                map.remove(removed.key)
    
    function addToHead(node):
        node.prev = head
        node.next = head.next
        head.next.prev = node
        head.next = node
    
    function removeNode(node):
        node.prev.next = node.next
        node.next.prev = node.prev
    
    function moveToHead(node):
        removeNode(node)
        addToHead(node)
    
    function removeTail():
        node = tail.prev
        removeNode(node)
        return node
```

**Java代码实现**：
```java
class LRUCache {
    class DLinkedNode {
        int key;
        int value;
        DLinkedNode prev;
        DLinkedNode next;
    }
    
    private void addNode(DLinkedNode node) {
        node.prev = head;
        node.next = head.next;
        head.next.prev = node;
        head.next = node;
    }
    
    private void removeNode(DLinkedNode node) {
        DLinkedNode prev = node.prev;
        DLinkedNode next = node.next;
        prev.next = next;
        next.prev = prev;
    }
    
    private void moveToHead(DLinkedNode node) {
        removeNode(node);
        addNode(node);
    }
    
    private DLinkedNode popTail() {
        DLinkedNode res = tail.prev;
        removeNode(res);
        return res;
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
            DLinkedNode newNode = new DLinkedNode();
            newNode.key = key;
            newNode.value = value;
            cache.put(key, newNode);
            addNode(newNode);
            ++size;
            if (size > capacity) {
                DLinkedNode tail = popTail();
                cache.remove(tail.key);
                --size;
            }
        } else {
            node.value = value;
            moveToHead(node);
        }
    }
}
```

---

## 三、二叉树（15题）

### 31. 二叉树的前序遍历

**题目链接**：[144. 二叉树的前序遍历](https://leetcode.cn/problems/binary-tree-preorder-traversal/)

**题目描述**：给你二叉树的根节点 root，返回它节点值的前序遍历。

**解决思路**：
1. **递归**：根 -> 左 -> 右
2. **迭代**：使用栈模拟递归过程

**伪代码实现（递归）**：
```
function preorderTraversal(root):
    result = new List()
    preorder(root, result)
    return result

function preorder(node, result):
    if node == null: return
    
    result.add(node.val)
    preorder(node.left, result)
    preorder(node.right, result)
```

**Java代码实现（递归）**：
```java
class Solution {
    public List<Integer> preorderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        preorder(root, result);
        return result;
    }
    
    private void preorder(TreeNode node, List<Integer> result) {
        if (node == null) return;
        
        result.add(node.val);
        preorder(node.left, result);
        preorder(node.right, result);
    }
}
```

---

### 32. 二叉树的中序遍历

**题目链接**：[94. 二叉树的中序遍历](https://leetcode.cn/problems/binary-tree-inorder-traversal/)

**题目描述**：给定一个二叉树的根节点 root，返回它的中序遍历。

**解决思路**：
1. **递归**：左 -> 根 -> 右
2. **迭代**：使用栈模拟递归过程

**伪代码实现（递归）**：
```
function inorderTraversal(root):
    result = new List()
    inorder(root, result)
    return result

function inorder(node, result):
    if node == null: return
    
    inorder(node.left, result)
    result.add(node.val)
    inorder(node.right, result)
```

**Java代码实现（递归）**：
```java
class Solution {
    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        inorder(root, result);
        return result;
    }
    
    private void inorder(TreeNode node, List<Integer> result) {
        if (node == null) return;
        
        inorder(node.left, result);
        result.add(node.val);
        inorder(node.right, result);
    }
}
```

---

### 33. 二叉树的后序遍历

**题目链接**：[145. 二叉树的后序遍历](https://leetcode.cn/problems/binary-tree-postorder-traversal/)

**题目描述**：给定一个二叉树，返回它的后序遍历。

**解决思路**：
1. **递归**：左 -> 右 -> 根
2. **迭代**：使用栈模拟递归过程

**伪代码实现（递归）**：
```
function postorderTraversal(root):
    result = new List()
    postorder(root, result)
    return result

function postorder(node, result):
    if node == null: return
    
    postorder(node.left, result)
    postorder(node.right, result)
    result.add(node.val)
```

**Java代码实现（递归）**：
```java
class Solution {
    public List<Integer> postorderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        postorder(root, result);
        return result;
    }
    
    private void postorder(TreeNode node, List<Integer> result) {
        if (node == null) return;
        
        postorder(node.left, result);
        postorder(node.right, result);
        result.add(node.val);
    }
}
```

---

### 34. 二叉树的层序遍历

**题目链接**：[102. 二叉树的层序遍历](https://leetcode.cn/problems/binary-tree-level-order-traversal/)

**题目描述**：给你二叉树的根节点 root，返回其节点值的层序遍历。（即逐层地，从左到右访问所有节点）。

**解决思路**：
1. **BFS**：使用队列进行广度优先搜索

**伪代码实现**：
```
function levelOrder(root):
    if root == null: return new List()
    
    result = new List()
    queue = new Queue()
    queue.offer(root)
    
    while not queue.isEmpty():
        level = new List()
        size = queue.size()
        
        for i from 0 to size - 1:
            node = queue.poll()
            level.add(node.val)
            
            if node.left != null: queue.offer(node.left)
            if node.right != null: queue.offer(node.right)
        
        result.add(level)
    
    return result
```

**Java代码实现**：
```java
class Solution {
    public List<List<Integer>> levelOrder(TreeNode root) {
        if (root == null) return new ArrayList<>();
        
        List<List<Integer>> result = new ArrayList<>();
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        
        while (!queue.isEmpty()) {
            List<Integer> level = new ArrayList<>();
            int size = queue.size();
            
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                level.add(node.val);
                
                if (node.left != null) queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }
            
            result.add(level);
        }
        
        return result;
    }
}
```

---

### 35. 二叉树的最大深度

**题目链接**：[104. 二叉树的最大深度](https://leetcode.cn/problems/maximum-depth-of-binary-tree/)

**题目描述**：给定一个二叉树，找出其最大深度。二叉树的深度为根节点到最远叶子节点的最长路径上的节点数。

**解决思路**：
1. **递归**：最大深度 = max(左子树深度, 右子树深度) + 1
2. **BFS**：层序遍历的层数

**伪代码实现（递归）**：
```
function maxDepth(root):
    if root == null: return 0
    
    leftDepth = maxDepth(root.left)
    rightDepth = maxDepth(root.right)
    
    return max(leftDepth, rightDepth) + 1
```

**Java代码实现（递归）**：
```java
class Solution {
    public int maxDepth(TreeNode root) {
        if (root == null) return 0;
        
        int leftDepth = maxDepth(root.left);
        int rightDepth = maxDepth(root.right);
        
        return Math.max(leftDepth, rightDepth) + 1;
    }
}
```

---

### 36. 二叉树的最小深度

**题目链接**：[111. 二叉树的最小深度](https://leetcode.cn/problems/minimum-depth-of-binary-tree/)

**题目描述**：给定一个二叉树，找出其最小深度。最小深度是从根节点到最近叶子节点的最短路径上的节点数量。

**解决思路**：
1. **递归**：注意如果左子树或右子树为空的情况
2. **BFS**：层序遍历，遇到第一个叶子节点返回

**伪代码实现（递归）**：
```
function minDepth(root):
    if root == null: return 0
    
    if root.left == null: return minDepth(root.right) + 1
    if root.right == null: return minDepth(root.left) + 1
    
    return min(minDepth(root.left), minDepth(root.right)) + 1
```

**Java代码实现（递归）**：
```java
class Solution {
    public int minDepth(TreeNode root) {
        if (root == null) return 0;
        
        if (root.left == null) return minDepth(root.right) + 1;
        if (root.right == null) return minDepth(root.left) + 1;
        
        return Math.min(minDepth(root.left), minDepth(root.right)) + 1;
    }
}
```

---

### 37. 翻转二叉树

**题目链接**：[226. 翻转二叉树](https://leetcode.cn/problems/invert-binary-tree/)

**题目描述**：给你一棵二叉树的根节点 root，翻转这棵二叉树，并返回其根节点。

**解决思路**：
1. **递归**：交换左右子树，然后递归处理左右子树

**伪代码实现**：
```
function invertTree(root):
    if root == null: return null
    
    temp = root.left
    root.left = root.right
    root.right = temp
    
    invertTree(root.left)
    invertTree(root.right)
    
    return root
```

**Java代码实现**：
```java
class Solution {
    public TreeNode invertTree(TreeNode root) {
        if (root == null) return null;
        
        TreeNode temp = root.left;
        root.left = root.right;
        root.right = temp;
        
        invertTree(root.left);
        invertTree(root.right);
        
        return root;
    }
}
```

---

### 38. 对称二叉树

**题目链接**：[101. 对称二叉树](https://leetcode.cn/problems/symmetric-tree/)

**题目描述**：给你一个二叉树的根节点 root，检查它是否轴对称。

**解决思路**：
1. **递归**：比较左子树的左节点和右子树的右节点，左子树的右节点和右子树的左节点

**伪代码实现**：
```
function isSymmetric(root):
    if root == null: return true
    return isMirror(root.left, root.right)

function isMirror(left, right):
    if left == null and right == null: return true
    if left == null or right == null: return false
    
    return left.val == right.val
        and isMirror(left.left, right.right)
        and isMirror(left.right, right.left)
```

**Java代码实现**：
```java
class Solution {
    public boolean isSymmetric(TreeNode root) {
        if (root == null) return true;
        return isMirror(root.left, root.right);
    }
    
    private boolean isMirror(TreeNode left, TreeNode right) {
        if (left == null && right == null) return true;
        if (left == null || right == null) return false;
        
        return left.val == right.val
            && isMirror(left.left, right.right)
            && isMirror(left.right, right.left);
    }
}
```

---

### 39. 路径总和

**题目链接**：[112. 路径总和](https://leetcode.cn/problems/path-sum/)

**题目描述**：给你二叉树的根节点 root 和一个表示目标和的整数 targetSum。判断该树中是否存在根节点到叶子节点的路径，这条路径上所有节点值相加等于目标和 targetSum。

**解决思路**：
1. **递归**：从根节点开始，每次减去当前节点值，到达叶子节点时判断是否为0

**伪代码实现**：
```
function hasPathSum(root, targetSum):
    if root == null: return false
    
    if root.left == null and root.right == null:
        return root.val == targetSum
    
    return hasPathSum(root.left, targetSum - root.val)
        or hasPathSum(root.right, targetSum - root.val)
```

**Java代码实现**：
```java
class Solution {
    public boolean hasPathSum(TreeNode root, int targetSum) {
        if (root == null) return false;
        
        if (root.left == null && root.right == null) {
            return root.val == targetSum;
        }
        
        return hasPathSum(root.left, targetSum - root.val)
            || hasPathSum(root.right, targetSum - root.val);
    }
}
```

---

### 40. 路径总和 II

**题目链接**：[113. 路径总和 II](https://leetcode.cn/problems/path-sum-ii/)

**题目描述**：给你二叉树的根节点 root 和一个整数目标和 targetSum，找出所有从根节点到叶子节点路径总和等于给定目标和的路径。

**解决思路**：
1. **回溯**：记录路径，到达叶子节点时判断是否满足条件

**伪代码实现**：
```
function pathSum(root, targetSum):
    result = new List()
    pathSum(root, targetSum, new List(), result)
    return result

function pathSum(node, targetSum, path, result):
    if node == null: return
    
    path.add(node.val)
    
    if node.left == null and node.right == null and node.val == targetSum:
        result.add(new List(path))
    else:
        pathSum(node.left, targetSum - node.val, path, result)
        pathSum(node.right, targetSum - node.val, path, result)
    
    path.removeLast()
```

**Java代码实现**：
```java
class Solution {
    public List<List<Integer>> pathSum(TreeNode root, int targetSum) {
        List<List<Integer>> result = new ArrayList<>();
        pathSum(root, targetSum, new ArrayList<>(), result);
        return result;
    }
    
    private void pathSum(TreeNode node, int targetSum, List<Integer> path, List<List<Integer>> result) {
        if (node == null) return;
        
        path.add(node.val);
        
        if (node.left == null && node.right == null && node.val == targetSum) {
            result.add(new ArrayList<>(path));
        } else {
            pathSum(node.left, targetSum - node.val, path, result);
            pathSum(node.right, targetSum - node.val, path, result);
        }
        
        path.remove(path.size() - 1);
    }
}
```

---

### 41. 二叉树的最近公共祖先

**题目链接**：[236. 二叉树的最近公共祖先](https://leetcode.cn/problems/lowest-common-ancestor-of-a-binary-tree/)

**题目描述**：给定一个二叉树，找到该树中两个指定节点的最近公共祖先。

**解决思路**：
1. **递归**：如果在左右子树分别找到p和q，则当前节点为LCA
2. 如果只在一边找到，则返回找到的节点

**伪代码实现**：
```
function lowestCommonAncestor(root, p, q):
    if root == null or root == p or root == q: return root
    
    left = lowestCommonAncestor(root.left, p, q)
    right = lowestCommonAncestor(root.right, p, q)
    
    if left != null and right != null: return root
    if left != null: return left
    return right
```

**Java代码实现**：
```java
class Solution {
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null || root == p || root == q) return root;
        
        TreeNode left = lowestCommonAncestor(root.left, p, q);
        TreeNode right = lowestCommonAncestor(root.right, p, q);
        
        if (left != null && right != null) return root;
        if (left != null) return left;
        return right;
    }
}
```

---

### 42. 二叉树的右视图

**题目链接**：[199. 二叉树的右视图](https://leetcode.cn/problems/binary-tree-right-side-view/)

**题目描述**：给定一个二叉树的根节点 root，想象自己站在它的右侧，按照从顶部到底部的顺序，返回从右侧所能看到的节点值。

**解决思路**：
1. **BFS**：层序遍历，每层的最后一个节点
2. **DFS**：先遍历右子树，记录每层第一个访问的节点

**伪代码实现（BFS）**：
```
function rightSideView(root):
    if root == null: return new List()
    
    result = new List()
    queue = new Queue()
    queue.offer(root)
    
    while not queue.isEmpty():
        size = queue.size()
        
        for i from 0 to size - 1:
            node = queue.poll()
            
            if i == size - 1:
                result.add(node.val)
            
            if node.left != null: queue.offer(node.left)
            if node.right != null: queue.offer(node.right)
    
    return result
```

**Java代码实现（BFS）**：
```java
class Solution {
    public List<Integer> rightSideView(TreeNode root) {
        if (root == null) return new ArrayList<>();
        
        List<Integer> result = new ArrayList<>();
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        
        while (!queue.isEmpty()) {
            int size = queue.size();
            
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                
                if (i == size - 1) {
                    result.add(node.val);
                }
                
                if (node.left != null) queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }
        }
        
        return result;
    }
}
```

---

### 43. 从前序与中序遍历序列构造二叉树

**题目链接**：[105. 从前序与中序遍历序列构造二叉树](https://leetcode.cn/problems/construct-binary-tree-from-preorder-and-inorder-traversal/)

**题目描述**：给定两个整数数组 preorder 和 inorder，其中 preorder 是二叉树的先序遍历，inorder 是同一棵树的中序遍历，请构造二叉树并返回其根节点。

**解决思路**：
1. **递归**：前序遍历的第一个元素为根节点
2. 在中序遍历中找到根节点位置，左边为左子树，右边为右子树

**伪代码实现**：
```
function buildTree(preorder, inorder):
    map = new HashMap()
    for i from 0 to inorder.length - 1:
        map.put(inorder[i], i)
    
    return build(preorder, 0, preorder.length - 1, 0, inorder.length - 1, map)

function build(preorder, preStart, preEnd, inStart, inEnd, map):
    if preStart > preEnd: return null
    
    rootVal = preorder[preStart]
    root = new TreeNode(rootVal)
    
    inRoot = map.get(rootVal)
    leftSize = inRoot - inStart
    
    root.left = build(preorder, preStart + 1, preStart + leftSize, inStart, inRoot - 1, map)
    root.right = build(preorder, preStart + leftSize + 1, preEnd, inRoot + 1, inEnd, map)
    
    return root
```

**Java代码实现**：
```java
class Solution {
    public TreeNode buildTree(int[] preorder, int[] inorder) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < inorder.length; i++) {
            map.put(inorder[i], i);
        }
        
        return build(preorder, 0, preorder.length - 1, 0, inorder.length - 1, map);
    }
    
    private TreeNode build(int[] preorder, int preStart, int preEnd, int inStart, int inEnd, Map<Integer, Integer> map) {
        if (preStart > preEnd) return null;
        
        int rootVal = preorder[preStart];
        TreeNode root = new TreeNode(rootVal);
        
        int inRoot = map.get(rootVal);
        int leftSize = inRoot - inStart;
        
        root.left = build(preorder, preStart + 1, preStart + leftSize, inStart, inRoot - 1, map);
        root.right = build(preorder, preStart + leftSize + 1, preEnd, inRoot + 1, inEnd, map);
        
        return root;
    }
}
```

---

### 44. 从中序与后序遍历序列构造二叉树

**题目链接**：[106. 从中序与后序遍历序列构造二叉树](https://leetcode.cn/problems/construct-binary-tree-from-inorder-and-postorder-traversal/)

**题目描述**：给定两个整数数组 inorder 和 postorder，其中 inorder 是二叉树的中序遍历，postorder 是同一棵树的后序遍历，请你构造并返回这棵二叉树。

**解决思路**：
1. **递归**：后序遍历的最后一个元素为根节点
2. 在中序遍历中找到根节点位置，左边为左子树，右边为右子树

**伪代码实现**：
```
function buildTree(inorder, postorder):
    map = new HashMap()
    for i from 0 to inorder.length - 1:
        map.put(inorder[i], i)
    
    return build(inorder, 0, inorder.length - 1, postorder, 0, postorder.length - 1, map)

function build(inorder, inStart, inEnd, postorder, postStart, postEnd, map):
    if inStart > inEnd: return null
    
    rootVal = postorder[postEnd]
    root = new TreeNode(rootVal)
    
    inRoot = map.get(rootVal)
    leftSize = inRoot - inStart
    
    root.left = build(inorder, inStart, inRoot - 1, postorder, postStart, postStart + leftSize - 1, map)
    root.right = build(inorder, inRoot + 1, inEnd, postorder, postStart + leftSize, postEnd - 1, map)
    
    return root
```

**Java代码实现**：
```java
class Solution {
    public TreeNode buildTree(int[] inorder, int[] postorder) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < inorder.length; i++) {
            map.put(inorder[i], i);
        }
        
        return build(inorder, 0, inorder.length - 1, postorder, 0, postorder.length - 1, map);
    }
    
    private TreeNode build(int[] inorder, int inStart, int inEnd, int[] postorder, int postStart, int postEnd, Map<Integer, Integer> map) {
        if (inStart > inEnd) return null;
        
        int rootVal = postorder[postEnd];
        TreeNode root = new TreeNode(rootVal);
        
        int inRoot = map.get(rootVal);
        int leftSize = inRoot - inStart;
        
        root.left = build(inorder, inStart, inRoot - 1, postorder, postStart, postStart + leftSize - 1, map);
        root.right = build(inorder, inRoot + 1, inEnd, postorder, postStart + leftSize, postEnd - 1, map);
        
        return root;
    }
}
```

---

### 45. 验证二叉搜索树

**题目链接**：[98. 验证二叉搜索树](https://leetcode.cn/problems/validate-binary-search-tree/)

**题目描述**：给你一个二叉树的根节点 root，判断其是否是一个有效的二叉搜索树。

**解决思路**：
1. **递归**：中序遍历是升序的
2. **递归**：每个节点的值必须在(min, max)范围内

**伪代码实现（递归）**：
```
function isValidBST(root):
    return validate(root, null, null)

function validate(node, min, max):
    if node == null: return true
    
    if min != null and node.val <= min: return false
    if max != null and node.val >= max: return false
    
    return validate(node.left, min, node.val)
        and validate(node.right, node.val, max)
```

**Java代码实现（递归）**：
```java
class Solution {
    public boolean isValidBST(TreeNode root) {
        return validate(root, null, null);
    }
    
    private boolean validate(TreeNode node, Integer min, Integer max) {
        if (node == null) return true;
        
        if (min != null && node.val <= min) return false;
        if (max != null && node.val >= max) return false;
        
        return validate(node.left, min, node.val)
            && validate(node.right, node.val, max);
    }
}
```

---

## 四、动态规划（15题）

### 46. 爬楼梯

**题目链接**：[70. 爬楼梯](https://leetcode.cn/problems/climbing-stairs/)

**题目描述**：假设你正在爬楼梯。需要 n 阶你才能到达楼顶。每次你可以爬 1 或 2 个台阶。你有多少种不同的方法可以爬到楼顶？

**解决思路**：
1. **动态规划**：dp[i] = dp[i-1] + dp[i-2]
2. 可以优化空间复杂度为O(1)

**伪代码实现**：
```
function climbStairs(n):
    if n <= 2: return n
    
    prev1 = 1
    prev2 = 2
    
    for i from 3 to n:
        current = prev1 + prev2
        prev1 = prev2
        prev2 = current
    
    return prev2
```

**Java代码实现**：
```java
class Solution {
    public int climbStairs(int n) {
        if (n <= 2) return n;
        
        int prev1 = 1;
        int prev2 = 2;
        
        for (int i = 3; i <= n; i++) {
            int current = prev1 + prev2;
            prev1 = prev2;
            prev2 = current;
        }
        
        return prev2;
    }
}
```

---

### 47. 斐波那契数

**题目链接**：[509. 斐波那契数](https://leetcode.cn/problems/fibonacci-number/)

**题目描述**：斐波那契数（通常用 F(n) 表示）形成的序列称为斐波那契数列。该数列由 0 和 1 开始，后面的每一项数字都是前面两项数字的和。

**解决思路**：
1. **动态规划**：F(n) = F(n-1) + F(n-2)
2. 可以优化空间复杂度

**伪代码实现**：
```
function fib(n):
    if n <= 1: return n
    
    prev1 = 0
    prev2 = 1
    
    for i from 2 to n:
        current = prev1 + prev2
        prev1 = prev2
        prev2 = current
    
    return prev2
```

**Java代码实现**：
```java
class Solution {
    public int fib(int n) {
        if (n <= 1) return n;
        
        int prev1 = 0;
        int prev2 = 1;
        
        for (int i = 2; i <= n; i++) {
            int current = prev1 + prev2;
            prev1 = prev2;
            prev2 = current;
        }
        
        return prev2;
    }
}
```

---

### 48. 最大子数组和

**题目链接**：[53. 最大子数组和](https://leetcode.cn/problems/maximum-subarray/)

**题目描述**：给你一个整数数组 nums，请你找出一个具有最大和的连续子数组（子数组最少包含一个元素），返回其最大和。

**解决思路**：
1. **动态规划（Kadane算法）**：dp[i]表示以i结尾的最大子数组和
2. dp[i] = max(nums[i], dp[i-1] + nums[i])

**伪代码实现**：
```
function maxSubArray(nums):
    maxSum = nums[0]
    currentSum = nums[0]
    
    for i from 1 to nums.length - 1:
        currentSum = max(nums[i], currentSum + nums[i])
        maxSum = max(maxSum, currentSum)
    
    return maxSum
```

**Java代码实现**：
```java
class Solution {
    public int maxSubArray(int[] nums) {
        int maxSum = nums[0];
        int currentSum = nums[0];
        
        for (int i = 1; i < nums.length; i++) {
            currentSum = Math.max(nums[i], currentSum + nums[i]);
            maxSum = Math.max(maxSum, currentSum);
        }
        
        return maxSum;
    }
}
```

---

### 49. 最长递增子序列

**题目链接**：[300. 最长递增子序列](https://leetcode.cn/problems/longest-increasing-subsequence/)

**题目描述**：给你一个整数数组 nums，找到其中最长严格递增子序列的长度。

**解决思路**：
1. **动态规划**：dp[i]表示以i结尾的最长递增子序列长度
2. dp[i] = max(dp[j] + 1) for all j < i and nums[j] < nums[i]

**伪代码实现**：
```
function lengthOfLIS(nums):
    if nums == null or nums.length == 0: return 0
    
    dp = new int[nums.length]
    Arrays.fill(dp, 1)
    maxLength = 1
    
    for i from 1 to nums.length - 1:
        for j from 0 to i - 1:
            if nums[j] < nums[i]:
                dp[i] = max(dp[i], dp[j] + 1)
        maxLength = max(maxLength, dp[i])
    
    return maxLength
```

**Java代码实现**：
```java
class Solution {
    public int lengthOfLIS(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        
        int[] dp = new int[nums.length];
        Arrays.fill(dp, 1);
        int maxLength = 1;
        
        for (int i = 1; i < nums.length; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[j] < nums[i]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            maxLength = Math.max(maxLength, dp[i]);
        }
        
        return maxLength;
    }
}
```

---

### 50. 零钱兑换

**题目链接**：[322. 零钱兑换](https://leetcode.cn/problems/coin-change/)

**题目描述**：给你一个整数数组 coins，表示不同面额的硬币；以及一个整数 amount，表示总金额。计算并返回可以凑成总金额所需的最少的硬币个数。如果没有任何一种硬币组合能组成总金额，返回 -1。

**解决思路**：
1. **动态规划**：dp[i]表示凑成金额i所需的最少硬币数
2. dp[i] = min(dp[i - coin] + 1) for all coin in coins

**伪代码实现**：
```
function coinChange(coins, amount):
    dp = new int[amount + 1]
    Arrays.fill(dp, amount + 1)
    dp[0] = 0
    
    for i from 1 to amount:
        for coin in coins:
            if coin <= i:
                dp[i] = min(dp[i], dp[i - coin] + 1)
    
    return dp[amount] > amount ? -1 : dp[amount]
```

**Java代码实现**：
```java
class Solution {
    public int coinChange(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, amount + 1);
        dp[0] = 0;
        
        for (int i = 1; i <= amount; i++) {
            for (int coin : coins) {
                if (coin <= i) {
                    dp[i] = Math.min(dp[i], dp[i - coin] + 1);
                }
            }
        }
        
        return dp[amount] > amount ? -1 : dp[amount];
    }
}
```

---

### 51. 编辑距离

**题目链接**：[72. 编辑距离](https://leetcode.cn/problems/edit-distance/)

**题目描述**：给你两个单词 word1 和 word2，请你计算出将 word1 转换成 word2 所使用的最少操作数。你可以对一个单词进行如下三种操作：插入一个字符、删除一个字符、替换一个字符。

**解决思路**：
1. **动态规划**：dp[i][j]表示word1前i个字符转换成word2前j个字符的最少操作数
2. 如果word1[i-1] == word2[j-1]，dp[i][j] = dp[i-1][j-1]
3. 否则，dp[i][j] = min(dp[i-1][j], dp[i][j-1], dp[i-1][j-1]) + 1

**伪代码实现**：
```
function minDistance(word1, word2):
    m = word1.length()
    n = word2.length()
    dp = new int[m + 1][n + 1]
    
    for i from 0 to m:
        dp[i][0] = i
    for j from 0 to n:
        dp[0][j] = j
    
    for i from 1 to m:
        for j from 1 to n:
            if word1[i-1] == word2[j-1]:
                dp[i][j] = dp[i-1][j-1]
            else:
                dp[i][j] = min(dp[i-1][j], dp[i][j-1], dp[i-1][j-1]) + 1
    
    return dp[m][n]
```

**Java代码实现**：
```java
class Solution {
    public int minDistance(String word1, String word2) {
        int m = word1.length();
        int n = word2.length();
        int[][] dp = new int[m + 1][n + 1];
        
        for (int i = 0; i <= m; i++) dp[i][0] = i;
        for (int j = 0; j <= n; j++) dp[0][j] = j;
        
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = Math.min(dp[i - 1][j], Math.min(dp[i][j - 1], dp[i - 1][j - 1])) + 1;
                }
            }
        }
        
        return dp[m][n];
    }
}
```

---

### 52. 最长公共子序列

**题目链接**：[1143. 最长公共子序列](https://leetcode.cn/problems/longest-common-subsequence/)

**题目描述**：给定两个字符串 text1 和 text2，返回这两个字符串的最长公共子序列的长度。

**解决思路**：
1. **动态规划**：dp[i][j]表示text1前i个字符和text2前j个字符的最长公共子序列长度
2. 如果text1[i-1] == text2[j-1]，dp[i][j] = dp[i-1][j-1] + 1
3. 否则，dp[i][j] = max(dp[i-1][j], dp[i][j-1])

**伪代码实现**：
```
function longestCommonSubsequence(text1, text2):
    m = text1.length()
    n = text2.length()
    dp = new int[m + 1][n + 1]
    
    for i from 1 to m:
        for j from 1 to n:
            if text1[i-1] == text2[j-1]:
                dp[i][j] = dp[i-1][j-1] + 1
            else:
                dp[i][j] = max(dp[i-1][j], dp[i][j-1])
    
    return dp[m][n]
```

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

### 53. 不同路径

**题目链接**：[62. 不同路径](https://leetcode.cn/problems/unique-paths/)

**题目描述**：一个机器人位于一个 m x n 网格的左上角。机器人每次只能向下或者向右移动一步。机器人试图达到网格的右下角。问总共有多少条不同的路径？

**解决思路**：
1. **动态规划**：dp[i][j]表示到达位置(i,j)的路径数
2. dp[i][j] = dp[i-1][j] + dp[i][j-1]

**伪代码实现**：
```
function uniquePaths(m, n):
    dp = new int[m][n]
    
    for i from 0 to m - 1:
        dp[i][0] = 1
    for j from 0 to n - 1:
        dp[0][j] = 1
    
    for i from 1 to m - 1:
        for j from 1 to n - 1:
            dp[i][j] = dp[i-1][j] + dp[i][j-1]
    
    return dp[m-1][n-1]
```

**Java代码实现**：
```java
class Solution {
    public int uniquePaths(int m, int n) {
        int[][] dp = new int[m][n];
        
        for (int i = 0; i < m; i++) dp[i][0] = 1;
        for (int j = 0; j < n; j++) dp[0][j] = 1;
        
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
            }
        }
        
        return dp[m - 1][n - 1];
    }
}
```

---

### 54. 不同路径 II

**题目链接**：[63. 不同路径 II](https://leetcode.cn/problems/unique-paths-ii/)

**题目描述**：一个机器人位于一个 m x n 网格的左上角。机器人每次只能向下或者向右移动一步。机器人试图达到网格的右下角。现在考虑网格中有障碍物。那么从左上角到右下角将会有多少条不同的路径？

**解决思路**：
1. **动态规划**：如果有障碍物，该位置路径数为0
2. 否则，dp[i][j] = dp[i-1][j] + dp[i][j-1]

**伪代码实现**：
```
function uniquePathsWithObstacles(obstacleGrid):
    m = obstacleGrid.length
    n = obstacleGrid[0].length
    dp = new int[m][n]
    
    if obstacleGrid[0][0] == 1: return 0
    dp[0][0] = 1
    
    for i from 1 to m - 1:
        dp[i][0] = obstacleGrid[i][0] == 1 ? 0 : dp[i-1][0]
    
    for j from 1 to n - 1:
        dp[0][j] = obstacleGrid[0][j] == 1 ? 0 : dp[0][j-1]
    
    for i from 1 to m - 1:
        for j from 1 to n - 1:
            if obstacleGrid[i][j] == 1:
                dp[i][j] = 0
            else:
                dp[i][j] = dp[i-1][j] + dp[i][j-1]
    
    return dp[m-1][n-1]
```

**Java代码实现**：
```java
class Solution {
    public int uniquePathsWithObstacles(int[][] obstacleGrid) {
        int m = obstacleGrid.length;
        int n = obstacleGrid[0].length;
        int[][] dp = new int[m][n];
        
        if (obstacleGrid[0][0] == 1) return 0;
        dp[0][0] = 1;
        
        for (int i = 1; i < m; i++) {
            dp[i][0] = obstacleGrid[i][0] == 1 ? 0 : dp[i - 1][0];
        }
        
        for (int j = 1; j < n; j++) {
            dp[0][j] = obstacleGrid[0][j] == 1 ? 0 : dp[0][j - 1];
        }
        
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                if (obstacleGrid[i][j] == 1) {
                    dp[i][j] = 0;
                } else {
                    dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
                }
            }
        }
        
        return dp[m - 1][n - 1];
    }
}
```

---

### 55. 最小路径和

**题目链接**：[64. 最小路径和](https://leetcode.cn/problems/minimum-path-sum/)

**题目描述**：给定一个包含非负整数的 m x n 网格 grid，请找出一条从左上角到右下角的路径，使得路径上的数字总和为最小。

**解决思路**：
1. **动态规划**：dp[i][j]表示到达位置(i,j)的最小路径和
2. dp[i][j] = grid[i][j] + min(dp[i-1][j], dp[i][j-1])

**伪代码实现**：
```
function minPathSum(grid):
    m = grid.length
    n = grid[0].length
    dp = new int[m][n]
    
    dp[0][0] = grid[0][0]
    
    for i from 1 to m - 1:
        dp[i][0] = dp[i-1][0] + grid[i][0]
    
    for j from 1 to n - 1:
        dp[0][j] = dp[0][j-1] + grid[0][j]
    
    for i from 1 to m - 1:
        for j from 1 to n - 1:
            dp[i][j] = grid[i][j] + min(dp[i-1][j], dp[i][j-1])
    
    return dp[m-1][n-1]
```

**Java代码实现**：
```java
class Solution {
    public int minPathSum(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        int[][] dp = new int[m][n];
        
        dp[0][0] = grid[0][0];
        
        for (int i = 1; i < m; i++) {
            dp[i][0] = dp[i - 1][0] + grid[i][0];
        }
        
        for (int j = 1; j < n; j++) {
            dp[0][j] = dp[0][j - 1] + grid[0][j];
        }
        
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                dp[i][j] = grid[i][j] + Math.min(dp[i - 1][j], dp[i][j - 1]);
            }
        }
        
        return dp[m - 1][n - 1];
    }
}
```

---

### 56. 打家劫舍

**题目链接**：[198. 打家劫舍](https://leetcode.cn/problems/house-robber/)

**题目描述**：你是一个专业的小偷，计划偷窃沿街的房屋。每间房内都藏有一定的现金，影响你偷窃的唯一制约因素就是相邻的房屋装有相互连通的防盗系统，如果两间相邻的房屋在同一晚上被小偷闯入，系统会自动报警。给定一个代表每个房屋存放金额的非负整数数组，计算你不触动警报装置的情况下，一夜之内能够偷窃到的最高金额。

**解决思路**：
1. **动态规划**：dp[i]表示前i个房屋能偷窃到的最高金额
2. dp[i] = max(dp[i-1], dp[i-2] + nums[i])

**伪代码实现**：
```
function rob(nums):
    if nums == null or nums.length == 0: return 0
    if nums.length == 1: return nums[0]
    
    prev2 = 0
    prev1 = 0
    
    for num in nums:
        current = max(prev1, prev2 + num)
        prev2 = prev1
        prev1 = current
    
    return prev1
```

**Java代码实现**：
```java
class Solution {
    public int rob(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        
        int prev2 = 0;
        int prev1 = 0;
        
        for (int num : nums) {
            int current = Math.max(prev1, prev2 + num);
            prev2 = prev1;
            prev1 = current;
        }
        
        return prev1;
    }
}
```

---

### 57. 打家劫舍 II

**题目链接**：[213. 打家劫舍 II](https://leetcode.cn/problems/house-robber-ii/)

**题目描述**：你是一个专业的小偷，计划偷窃沿街的房屋，每间房内都藏有一定的现金。这个地方所有的房屋都围成一圈，这意味着第一个房屋和最后一个房屋是紧挨着的。同时，相邻的房屋装有相互连通的防盗系统，如果两间相邻的房屋在同一晚上被小偷闯入，系统会自动报警。

**解决思路**：
1. **动态规划**：分两种情况，偷第一个房屋或不偷第一个房屋
2. 取两种情况的最大值

**伪代码实现**：
```
function rob(nums):
    if nums == null or nums.length == 0: return 0
    if nums.length == 1: return nums[0]
    
    return max(robRange(nums, 0, nums.length - 2), robRange(nums, 1, nums.length - 1))

function robRange(nums, start, end):
    prev2 = 0
    prev1 = 0
    
    for i from start to end:
        current = max(prev1, prev2 + nums[i])
        prev2 = prev1
        prev1 = current
    
    return prev1
```

**Java代码实现**：
```java
class Solution {
    public int rob(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        if (nums.length == 1) return nums[0];
        
        return Math.max(robRange(nums, 0, nums.length - 2), robRange(nums, 1, nums.length - 1));
    }
    
    private int robRange(int[] nums, int start, int end) {
        int prev2 = 0;
        int prev1 = 0;
        
        for (int i = start; i <= end; i++) {
            int current = Math.max(prev1, prev2 + nums[i]);
            prev2 = prev1;
            prev1 = current;
        }
        
        return prev1;
    }
}
```

---

### 58. 完全平方数

**题目链接**：[279. 完全平方数](https://leetcode.cn/problems/perfect-squares/)

**题目描述**：给你一个整数 n，返回和为 n 的完全平方数的最少数量。

**解决思路**：
1. **动态规划**：dp[i]表示和为i的完全平方数的最少数量
2. dp[i] = min(dp[i - j*j] + 1) for all j*j <= i

**伪代码实现**：
```
function numSquares(n):
    dp = new int[n + 1]
    Arrays.fill(dp, Integer.MAX_VALUE)
    dp[0] = 0
    
    for i from 1 to n:
        for j from 1 while j * j <= i:
            dp[i] = min(dp[i], dp[i - j * j] + 1)
    
    return dp[n]
```

**Java代码实现**：
```java
class Solution {
    public int numSquares(int n) {
        int[] dp = new int[n + 1];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;
        
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j * j <= i; j++) {
                dp[i] = Math.min(dp[i], dp[i - j * j] + 1);
            }
        }
        
        return dp[n];
    }
}
```

---

### 59. 分割等和子集

**题目链接**：[416. 分割等和子集](https://leetcode.cn/problems/partition-equal-subset-sum/)

**题目描述**：给你一个只包含正整数的非空数组 nums。请你判断是否可以将这个数组分割成两个子集，使得两个子集的元素和相等。

**解决思路**：
1. **动态规划（0-1背包）**：dp[i]表示是否可以组成和为i
2. 目标和为数组总和的一半

**伪代码实现**：
```
function canPartition(nums):
    sum = 0
    for num in nums: sum += num
    
    if sum % 2 != 0: return false
    
    target = sum / 2
    dp = new boolean[target + 1]
    dp[0] = true
    
    for num in nums:
        for i from target down to num:
            dp[i] = dp[i] or dp[i - num]
    
    return dp[target]
```

**Java代码实现**：
```java
class Solution {
    public boolean canPartition(int[] nums) {
        int sum = 0;
        for (int num : nums) sum += num;
        
        if (sum % 2 != 0) return false;
        
        int target = sum / 2;
        boolean[] dp = new boolean[target + 1];
        dp[0] = true;
        
        for (int num : nums) {
            for (int i = target; i >= num; i--) {
                dp[i] = dp[i] || dp[i - num];
            }
        }
        
        return dp[target];
    }
}
```

---

## 五、字符串（15题）

### 61. 无重复字符的最长子串

**题目链接**：[3. 无重复字符的最长子串](https://leetcode.cn/problems/longest-substring-without-repeating-characters/)

**题目描述**：给定一个字符串 s，请你找出其中不含有重复字符的最长子串的长度。

**解决思路**：
1. **滑动窗口**：使用双指针维护一个不含重复字符的窗口
2. 使用哈希表记录字符最后出现的位置

**伪代码实现**：
```
function lengthOfLongestSubstring(s):
    map = new HashMap()
    maxLen = 0
    left = 0
    
    for right from 0 to s.length - 1:
        char c = s.charAt(right)
        if map.containsKey(c) and map.get(c) >= left:
            left = map.get(c) + 1
        
        map.put(c, right)
        maxLen = max(maxLen, right - left + 1)
    
    return maxLen
```

**Java代码实现**：
```java
class Solution {
    public int lengthOfLongestSubstring(String s) {
        Map<Character, Integer> map = new HashMap<>();
        int maxLen = 0;
        int left = 0;
        
        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);
            if (map.containsKey(c) && map.get(c) >= left) {
                left = map.get(c) + 1;
            }
            
            map.put(c, right);
            maxLen = Math.max(maxLen, right - left + 1);
        }
        
        return maxLen;
    }
}
```

---

### 62. 最长回文子串

**题目链接**：[5. 最长回文子串](https://leetcode.cn/problems/longest-palindromic-substring/)

**题目描述**：给你一个字符串 s，找到 s 中最长的回文子串。

**解决思路**：
1. **中心扩展**：以每个字符为中心，向两边扩展寻找回文
2. 需要考虑奇数长度和偶数长度两种情况

**伪代码实现**：
```
function longestPalindrome(s):
    if s == null or s.length() == 0: return ""
    
    start = 0
    maxLen = 1
    
    for i from 0 to s.length - 1:
        len1 = expandAroundCenter(s, i, i)
        len2 = expandAroundCenter(s, i, i + 1)
        len = max(len1, len2)
        
        if len > maxLen:
            maxLen = len
            start = i - (len - 1) / 2
    
    return s.substring(start, start + maxLen)

function expandAroundCenter(s, left, right):
    while left >= 0 and right < s.length() and s.charAt(left) == s.charAt(right):
        left--
        right++
    
    return right - left - 1
```

**Java代码实现**：
```java
class Solution {
    public String longestPalindrome(String s) {
        if (s == null || s.length() == 0) return "";
        
        int start = 0;
        int maxLen = 1;
        
        for (int i = 0; i < s.length(); i++) {
            int len1 = expandAroundCenter(s, i, i);
            int len2 = expandAroundCenter(s, i, i + 1);
            int len = Math.max(len1, len2);
            
            if (len > maxLen) {
                maxLen = len;
                start = i - (len - 1) / 2;
            }
        }
        
        return s.substring(start, start + maxLen);
    }
    
    private int expandAroundCenter(String s, int left, int right) {
        while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
            left--;
            right++;
        }
        
        return right - left - 1;
    }
}
```

---

### 63. 最长回文子序列

**题目链接**：[516. 最长回文子序列](https://leetcode.cn/problems/longest-palindromic-subsequence/)

**题目描述**：给你一个字符串 s，找出其中最长的回文子序列，并返回该序列的长度。

**解决思路**：
1. **动态规划**：dp[i][j]表示s[i...j]的最长回文子序列长度
2. 如果s[i] == s[j]，dp[i][j] = dp[i+1][j-1] + 2
3. 否则，dp[i][j] = max(dp[i+1][j], dp[i][j-1])

**伪代码实现**：
```
function longestPalindromeSubseq(s):
    n = s.length()
    dp = new int[n][n]
    
    for i from n - 1 down to 0:
        dp[i][i] = 1
        for j from i + 1 to n - 1:
            if s.charAt(i) == s.charAt(j):
                dp[i][j] = dp[i + 1][j - 1] + 2
            else:
                dp[i][j] = max(dp[i + 1][j], dp[i][j - 1])
    
    return dp[0][n - 1]
```

**Java代码实现**：
```java
class Solution {
    public int longestPalindromeSubseq(String s) {
        int n = s.length();
        int[][] dp = new int[n][n];
        
        for (int i = n - 1; i >= 0; i--) {
            dp[i][i] = 1;
            for (int j = i + 1; j < n; j++) {
                if (s.charAt(i) == s.charAt(j)) {
                    dp[i][j] = dp[i + 1][j - 1] + 2;
                } else {
                    dp[i][j] = Math.max(dp[i + 1][j], dp[i][j - 1]);
                }
            }
        }
        
        return dp[0][n - 1];
    }
}
```

---

### 64. 最小覆盖子串

**题目链接**：[76. 最小覆盖子串](https://leetcode.cn/problems/minimum-window-substring/)

**题目描述**：给你一个字符串 s、一个字符串 t。返回 s 中涵盖 t 所有字符的最小子串。如果 s 中不存在涵盖 t 所有字符的子串，则返回空字符串 ""。

**解决思路**：
1. **滑动窗口**：使用双指针维护窗口
2. 使用哈希表记录字符出现次数
3. 当窗口包含所有字符时，尝试缩小窗口

**伪代码实现**：
```
function minWindow(s, t):
    if s == null or t == null or s.length() < t.length(): return ""
    
    map = new HashMap()
    for char c in t:
        map.put(c, map.getOrDefault(c, 0) + 1)
    
    left = 0
    right = 0
    count = t.length()
    minLen = Integer.MAX_VALUE
    start = 0
    
    while right < s.length():
        char c = s.charAt(right)
        if map.containsKey(c):
            if map.get(c) > 0: count--
            map.put(c, map.get(c) - 1)
        
        right++
        
        while count == 0:
            if right - left < minLen:
                minLen = right - left
                start = left
            
            char leftChar = s.charAt(left)
            if map.containsKey(leftChar):
                map.put(leftChar, map.get(leftChar) + 1)
                if map.get(leftChar) > 0: count++
            
            left++
    
    return minLen == Integer.MAX_VALUE ? "" : s.substring(start, start + minLen)
```

**Java代码实现**：
```java
class Solution {
    public String minWindow(String s, String t) {
        if (s == null || t == null || s.length() < t.length()) return "";
        
        Map<Character, Integer> map = new HashMap<>();
        for (char c : t.toCharArray()) {
            map.put(c, map.getOrDefault(c, 0) + 1);
        }
        
        int left = 0, right = 0;
        int count = t.length();
        int minLen = Integer.MAX_VALUE;
        int start = 0;
        
        while (right < s.length()) {
            char c = s.charAt(right);
            if (map.containsKey(c)) {
                if (map.get(c) > 0) count--;
                map.put(c, map.get(c) - 1);
            }
            
            right++;
            
            while (count == 0) {
                if (right - left < minLen) {
                    minLen = right - left;
                    start = left;
                }
                
                char leftChar = s.charAt(left);
                if (map.containsKey(leftChar)) {
                    map.put(leftChar, map.get(leftChar) + 1);
                    if (map.get(leftChar) > 0) count++;
                }
                
                left++;
            }
        }
        
        return minLen == Integer.MAX_VALUE ? "" : s.substring(start, start + minLen);
    }
}
```

---

### 65. 字符串相加

**题目链接**：[415. 字符串相加](https://leetcode.cn/problems/add-strings/)

**题目描述**：给定两个字符串形式的非负整数 num1 和 num2，计算它们的和并同样以字符串形式返回。

**解决思路**：
1. **模拟加法**：从最低位开始逐位相加
2. 处理进位

**伪代码实现**：
```
function addStrings(num1, num2):
    result = new StringBuilder()
    i = num1.length() - 1
    j = num2.length() - 1
    carry = 0
    
    while i >= 0 or j >= 0 or carry > 0:
        sum = carry
        if i >= 0:
            sum += num1.charAt(i) - '0'
            i--
        if j >= 0:
            sum += num2.charAt(j) - '0'
            j--
        
        result.append(sum % 10)
        carry = sum / 10
    
    return result.reverse().toString()
```

**Java代码实现**：
```java
class Solution {
    public String addStrings(String num1, String num2) {
        StringBuilder result = new StringBuilder();
        int i = num1.length() - 1;
        int j = num2.length() - 1;
        int carry = 0;
        
        while (i >= 0 || j >= 0 || carry > 0) {
            int sum = carry;
            if (i >= 0) {
                sum += num1.charAt(i) - '0';
                i--;
            }
            if (j >= 0) {
                sum += num2.charAt(j) - '0';
                j--;
            }
            
            result.append(sum % 10);
            carry = sum / 10;
        }
        
        return result.reverse().toString();
    }
}
```

---

### 66. 验证回文串

**题目链接**：[125. 验证回文串](https://leetcode.cn/problems/valid-palindrome/)

**题目描述**：给定一个字符串，验证它是否是回文串，只考虑字母和数字字符，可以忽略字母的大小写。

**解决思路**：
1. **双指针**：使用左右两个指针向中间移动
2. 跳过非字母数字字符

**伪代码实现**：
```
function isPalindrome(s):
    left = 0
    right = s.length() - 1
    
    while left < right:
        while left < right and not isAlphanumeric(s.charAt(left)):
            left++
        while left < right and not isAlphanumeric(s.charAt(right)):
            right--
        
        if toLowerCase(s.charAt(left)) != toLowerCase(s.charAt(right)):
            return false
        
        left++
        right--
    
    return true
```

**Java代码实现**：
```java
class Solution {
    public boolean isPalindrome(String s) {
        int left = 0;
        int right = s.length() - 1;
        
        while (left < right) {
            while (left < right && !Character.isLetterOrDigit(s.charAt(left))) {
                left++;
            }
            while (left < right && !Character.isLetterOrDigit(s.charAt(right))) {
                right--;
            }
            
            if (Character.toLowerCase(s.charAt(left)) != Character.toLowerCase(s.charAt(right))) {
                return false;
            }
            
            left++;
            right--;
        }
        
        return true;
    }
}
```

---

### 67. 回文数

**题目链接**：[9. 回文数](https://leetcode.cn/problems/palindrome-number/)

**题目描述**：给你一个整数 x，如果 x 是一个回文整数，返回 true；否则，返回 false。

**解决思路**：
1. **数学方法**：反转数字的后半部分
2. 比较前半部分和反转后的后半部分

**伪代码实现**：
```
function isPalindrome(x):
    if x < 0 or (x % 10 == 0 and x != 0): return false
    
    revertedNumber = 0
    while x > revertedNumber:
        revertedNumber = revertedNumber * 10 + x % 10
        x /= 10
    
    return x == revertedNumber or x == revertedNumber / 10
```

**Java代码实现**：
```java
class Solution {
    public boolean isPalindrome(int x) {
        if (x < 0 || (x % 10 == 0 && x != 0)) return false;
        
        int revertedNumber = 0;
        while (x > revertedNumber) {
            revertedNumber = revertedNumber * 10 + x % 10;
            x /= 10;
        }
        
        return x == revertedNumber || x == revertedNumber / 10;
    }
}
```

---

### 68. 字符串解码

**题目链接**：[394. 字符串解码](https://leetcode.cn/problems/decode-string/)

**题目描述**：给定一个经过编码的字符串，返回它解码后的字符串。编码规则为: k[encoded_string]，表示其中方括号内部的 encoded_string 正好重复 k 次。注意 k 保证为正整数。

**解决思路**：
1. **栈**：使用两个栈分别存储数字和字符串
2. 遇到 '[' 时，将当前字符串和数字入栈
3. 遇到 ']' 时，出栈并拼接字符串

**伪代码实现**：
```
function decodeString(s):
    countStack = new Stack()
    stringStack = new Stack()
    currentString = ""
    currentNum = 0
    
    for char c in s:
        if Character.isDigit(c):
            currentNum = currentNum * 10 + (c - '0')
        else if c == '[':
            countStack.push(currentNum)
            stringStack.push(currentString)
            currentNum = 0
            currentString = ""
        else if c == ']':
            count = countStack.pop()
            prevString = stringStack.pop()
            currentString = prevString + currentString.repeat(count)
        else:
            currentString += c
    
    return currentString
```

**Java代码实现**：
```java
class Solution {
    public String decodeString(String s) {
        Stack<Integer> countStack = new Stack<>();
        Stack<String> stringStack = new Stack<>();
        StringBuilder currentString = new StringBuilder();
        int currentNum = 0;
        
        for (char c : s.toCharArray()) {
            if (Character.isDigit(c)) {
                currentNum = currentNum * 10 + (c - '0');
            } else if (c == '[') {
                countStack.push(currentNum);
                stringStack.push(currentString.toString());
                currentNum = 0;
                currentString = new StringBuilder();
            } else if (c == ']') {
                int count = countStack.pop();
                StringBuilder prevString = new StringBuilder(stringStack.pop());
                for (int i = 0; i < count; i++) {
                    prevString.append(currentString);
                }
                currentString = prevString;
            } else {
                currentString.append(c);
            }
        }
        
        return currentString.toString();
    }
}
```

---

### 69. 有效的括号

**题目链接**：[20. 有效的括号](https://leetcode.cn/problems/valid-parentheses/)

**题目描述**：给定一个只包括 '('，')'，'{'，'}'，'['，']'的字符串 s，判断字符串是否有效。

**解决思路**：
1. **栈**：遇到左括号入栈，遇到右括号出栈并匹配

**伪代码实现**：
```
function isValid(s):
    stack = new Stack()
    map = new HashMap()
    map.put(')', '(')
    map.put('}', '{')
    map.put(']', '[')
    
    for char c in s:
        if map.containsKey(c):
            if stack.isEmpty() or stack.pop() != map.get(c):
                return false
        else:
            stack.push(c)
    
    return stack.isEmpty()
```

**Java代码实现**：
```java
class Solution {
    public boolean isValid(String s) {
        Stack<Character> stack = new Stack<>();
        Map<Character, Character> map = new HashMap<>();
        map.put(')', '(');
        map.put('}', '{');
        map.put(']', '[');
        
        for (char c : s.toCharArray()) {
            if (map.containsKey(c)) {
                if (stack.isEmpty() || stack.pop() != map.get(c)) {
                    return false;
                }
            } else {
                stack.push(c);
            }
        }
        
        return stack.isEmpty();
    }
}
```

---

### 70. 最长有效括号

**题目链接**：[32. 最长有效括号](https://leetcode.cn/problems/longest-valid-parentheses/)

**题目描述**：给你一个只包含 '(' 和 ')' 的字符串，找出最长有效（格式正确且连续）括号子串的长度。

**解决思路**：
1. **栈**：栈中存储下标，遇到匹配时计算长度
2. 栈底存储最后一个未匹配的右括号下标

**伪代码实现**：
```
function longestValidParentheses(s):
    maxLen = 0
    stack = new Stack()
    stack.push(-1)
    
    for i from 0 to s.length - 1:
        if s.charAt(i) == '(':
            stack.push(i)
        else:
            stack.pop()
            if stack.isEmpty():
                stack.push(i)
            else:
                maxLen = max(maxLen, i - stack.peek())
    
    return maxLen
```

**Java代码实现**：
```java
class Solution {
    public int longestValidParentheses(String s) {
        int maxLen = 0;
        Stack<Integer> stack = new Stack<>();
        stack.push(-1);
        
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') {
                stack.push(i);
            } else {
                stack.pop();
                if (stack.isEmpty()) {
                    stack.push(i);
                } else {
                    maxLen = Math.max(maxLen, i - stack.peek());
                }
            }
        }
        
        return maxLen;
    }
}
```

---

### 71. 实现 strStr()

**题目链接**：[28. 实现 strStr()](https://leetcode.cn/problems/implement-strstr/)

**题目描述**：给你两个字符串 haystack 和 needle，请你在 haystack 字符串中找出 needle 字符串出现的第一个位置（下标从 0 开始）。如果不存在，则返回 -1。

**解决思路**：
1. **KMP算法**：使用next数组优化匹配过程
2. **暴力匹配**：逐个字符比较

**伪代码实现（KMP）**：
```
function strStr(haystack, needle):
    if needle.length() == 0: return 0
    
    next = buildNext(needle)
    j = 0
    
    for i from 0 to haystack.length - 1:
        while j > 0 and haystack.charAt(i) != needle.charAt(j):
            j = next[j - 1]
        
        if haystack.charAt(i) == needle.charAt(j):
            j++
        
        if j == needle.length():
            return i - j + 1
    
    return -1

function buildNext(needle):
    next = new int[needle.length()]
    j = 0
    
    for i from 1 to needle.length - 1:
        while j > 0 and needle.charAt(i) != needle.charAt(j):
            j = next[j - 1]
        
        if needle.charAt(i) == needle.charAt(j):
            j++
        
        next[i] = j
    
    return next
```

**Java代码实现（KMP）**：
```java
class Solution {
    public int strStr(String haystack, String needle) {
        if (needle.length() == 0) return 0;
        
        int[] next = buildNext(needle);
        int j = 0;
        
        for (int i = 0; i < haystack.length(); i++) {
            while (j > 0 && haystack.charAt(i) != needle.charAt(j)) {
                j = next[j - 1];
            }
            
            if (haystack.charAt(i) == needle.charAt(j)) {
                j++;
            }
            
            if (j == needle.length()) {
                return i - j + 1;
            }
        }
        
        return -1;
    }
    
    private int[] buildNext(String needle) {
        int[] next = new int[needle.length()];
        int j = 0;
        
        for (int i = 1; i < needle.length(); i++) {
            while (j > 0 && needle.charAt(i) != needle.charAt(j)) {
                j = next[j - 1];
            }
            
            if (needle.charAt(i) == needle.charAt(j)) {
                j++;
            }
            
            next[i] = j;
        }
        
        return next;
    }
}
```

---

### 72. 重复的子字符串

**题目链接**：[459. 重复的子字符串](https://leetcode.cn/problems/repeated-substring-pattern/)

**题目描述**：给定一个非空的字符串 s，检查它是否可以由它的一个子串重复多次构成。

**解决思路**：
1. **字符串拼接**：如果s可以由子串重复构成，则s在(s+s)中第二次出现的位置小于s.length()

**伪代码实现**：
```
function repeatedSubstringPattern(s):
    return (s + s).indexOf(s, 1) != s.length()
```

**Java代码实现**：
```java
class Solution {
    public boolean repeatedSubstringPattern(String s) {
        return (s + s).indexOf(s, 1) != s.length();
    }
}
```

---

### 73. 赎金信

**题目链接**：[383. 赎金信](https://leetcode.cn/problems/ransom-note/)

**题目描述**：给你两个字符串：ransomNote 和 magazine，判断 ransomNote 能不能由 magazine 里面的字符构成。

**解决思路**：
1. **哈希表**：统计magazine中每个字符的出现次数
2. 遍历ransomNote，检查字符是否足够

**伪代码实现**：
```
function canConstruct(ransomNote, magazine):
    count = new int[26]
    
    for char c in magazine:
        count[c - 'a']++
    
    for char c in ransomNote:
        if count[c - 'a'] == 0:
            return false
        count[c - 'a']--
    
    return true
```

**Java代码实现**：
```java
class Solution {
    public boolean canConstruct(String ransomNote, String magazine) {
        int[] count = new int[26];
        
        for (char c : magazine.toCharArray()) {
            count[c - 'a']++;
        }
        
        for (char c : ransomNote.toCharArray()) {
            if (count[c - 'a'] == 0) {
                return false;
            }
            count[c - 'a']--;
        }
        
        return true;
    }
}
```

---

### 74. 字母异位词分组

**题目链接**：[49. 字母异位词分组](https://leetcode.cn/problems/group-anagrams/)

**题目描述**：给你一个字符串数组，请你将字母异位词组合在一起。可以按任意顺序返回结果列表。字母异位词是由重新排列源单词的所有字母得到的一个新单词。

**解决思路**：
1. **哈希表**：将排序后的字符串作为key
2. 异位词排序后相同

**伪代码实现**：
```
function groupAnagrams(strs):
    map = new HashMap()
    
    for str in strs:
        char[] chars = str.toCharArray()
        Arrays.sort(chars)
        key = new String(chars)
        
        if not map.containsKey(key):
            map.put(key, new ArrayList())
        map.get(key).add(str)
    
    return new ArrayList(map.values())
```

**Java代码实现**：
```java
class Solution {
    public List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> map = new HashMap<>();
        
        for (String str : strs) {
            char[] chars = str.toCharArray();
            Arrays.sort(chars);
            String key = new String(chars);
            
            if (!map.containsKey(key)) {
                map.put(key, new ArrayList<>());
            }
            map.get(key).add(str);
        }
        
        return new ArrayList<>(map.values());
    }
}
```

---

### 75. 找到字符串中所有字母异位词

**题目链接**：[438. 找到字符串中所有字母异位词](https://leetcode.cn/problems/find-all-anagrams-in-a-string/)

**题目描述**：给定两个字符串 s 和 p，找到 s 中所有 p 的异位词的子串，返回这些子串的起始索引。

**解决思路**：
1. **滑动窗口**：维护一个长度为p.length()的窗口
2. 比较窗口内字符与p的字符是否相同

**伪代码实现**：
```
function findAnagrams(s, p):
    result = new List()
    if s.length() < p.length(): return result
    
    pCount = new int[26]
    sCount = new int[26]
    
    for char c in p:
        pCount[c - 'a']++
    
    for i from 0 to s.length() - 1:
        sCount[s.charAt(i) - 'a']++
        
        if i >= p.length():
            sCount[s.charAt(i - p.length()) - 'a']--
        
        if i >= p.length() - 1 and Arrays.equals(pCount, sCount):
            result.add(i - p.length() + 1)
    
    return result
```

**Java代码实现**：
```java
class Solution {
    public List<Integer> findAnagrams(String s, String p) {
        List<Integer> result = new ArrayList<>();
        if (s.length() < p.length()) return result;
        
        int[] pCount = new int[26];
        int[] sCount = new int[26];
        
        for (char c : p.toCharArray()) {
            pCount[c - 'a']++;
        }
        
        for (int i = 0; i < s.length(); i++) {
            sCount[s.charAt(i) - 'a']++;
            
            if (i >= p.length()) {
                sCount[s.charAt(i - p.length()) - 'a']--;
            }
            
            if (i >= p.length() - 1 && Arrays.equals(pCount, sCount)) {
                result.add(i - p.length() + 1);
            }
        }
        
        return result;
    }
}
```

---

### 60. 目标和

**题目链接**：[494. 目标和](https://leetcode.cn/problems/target-sum/)

**题目描述**：给你一个整数数组 nums 和一个整数 target。向数组中的每个整数前添加 '+' 或 '-'，然后串联起所有整数，可以构造一个表达式。返回可以通过上述方法构造的、运算结果等于 target 的不同表达式的数目。

**解决思路**：
1. **动态规划（0-1背包）**：转化为子集和问题
2. 设正数和为P，负数和为N，则P - N = target，P + N = sum
3. 所以P = (target + sum) / 2

**伪代码实现**：
```
function findTargetSumWays(nums, target):
    sum = 0
    for num in nums: sum += num
    
    if (target + sum) % 2 != 0 or target + sum < 0: return 0
    
    target = (target + sum) / 2
    dp = new int[target + 1]
    dp[0] = 1
    
    for num in nums:
        for i from target down to num:
            dp[i] += dp[i - num]
    
    return dp[target]
```

**Java代码实现**：
```java
class Solution {
    public int findTargetSumWays(int[] nums, int target) {
        int sum = 0;
        for (int num : nums) sum += num;
        
        if ((target + sum) % 2 != 0 || target + sum < 0) return 0;
        
        target = (target + sum) / 2;
        int[] dp = new int[target + 1];
        dp[0] = 1;
        
        for (int num : nums) {
            for (int i = target; i >= num; i--) {
                dp[i] += dp[i - num];
            }
        }
        
        return dp[target];
    }
}
```

---

## 六、其他类型（25题）

### 76. 两数之和

**题目链接**：[1. 两数之和](https://leetcode.cn/problems/two-sum/)

**题目描述**：给定一个整数数组 nums 和一个整数目标值 target，请你在该数组中找出和为目标值 target 的那两个整数，并返回它们的数组下标。

**解决思路**：
1. **哈希表**：使用HashMap存储已遍历过的数字及其下标
2. 对于当前数字num，检查target - num是否在map中

**伪代码实现**：
```
function twoSum(nums, target):
    map = new HashMap()
    
    for i from 0 to nums.length - 1:
        complement = target - nums[i]
        if map.containsKey(complement):
            return [map.get(complement), i]
        map.put(nums[i], i)
    
    return null
```

**Java代码实现**：
```java
class Solution {
    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (map.containsKey(complement)) {
                return new int[]{map.get(complement), i};
            }
            map.put(nums[i], i);
        }
        
        return null;
    }
}
```

---

### 77. 字母异位词分组

**题目链接**：[49. 字母异位词分组](https://leetcode.cn/problems/group-anagrams/)

**题目描述**：给你一个字符串数组，请你将字母异位词组合在一起。可以按任意顺序返回结果列表。

**解决思路**：
1. **哈希表**：将排序后的字符串作为key
2. 异位词排序后相同

**伪代码实现**：
```
function groupAnagrams(strs):
    map = new HashMap()
    
    for str in strs:
        char[] chars = str.toCharArray()
        Arrays.sort(chars)
        key = new String(chars)
        
        if not map.containsKey(key):
            map.put(key, new ArrayList())
        map.get(key).add(str)
    
    return new ArrayList(map.values())
```

**Java代码实现**：
```java
class Solution {
    public List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> map = new HashMap<>();
        
        for (String str : strs) {
            char[] chars = str.toCharArray();
            Arrays.sort(chars);
            String key = new String(chars);
            
            if (!map.containsKey(key)) {
                map.put(key, new ArrayList<>());
            }
            map.get(key).add(str);
        }
        
        return new ArrayList<>(map.values());
    }
}
```

---

### 78. 最长连续序列

**题目链接**：[128. 最长连续序列](https://leetcode.cn/problems/longest-consecutive-sequence/)

**题目描述**：给定一个未排序的整数数组 nums，找出数字连续的最长序列（不要求序列元素在原数组中连续）的长度。

**解决思路**：
1. **哈希集合**：将所有数字存入HashSet
2. 对于每个数字，如果num-1不在集合中，则从num开始向后寻找连续序列

**伪代码实现**：
```
function longestConsecutive(nums):
    if nums.length == 0: return 0
    
    set = new HashSet()
    for num in nums:
        set.add(num)
    
    maxLen = 0
    for num in set:
        if not set.contains(num - 1):
            currentNum = num
            currentLen = 1
            
            while set.contains(currentNum + 1):
                currentNum++
                currentLen++
            
            maxLen = max(maxLen, currentLen)
    
    return maxLen
```

**Java代码实现**：
```java
class Solution {
    public int longestConsecutive(int[] nums) {
        if (nums.length == 0) return 0;
        
        Set<Integer> set = new HashSet<>();
        for (int num : nums) {
            set.add(num);
        }
        
        int maxLen = 0;
        for (int num : set) {
            if (!set.contains(num - 1)) {
                int currentNum = num;
                int currentLen = 1;
                
                while (set.contains(currentNum + 1)) {
                    currentNum++;
                    currentLen++;
                }
                
                maxLen = Math.max(maxLen, currentLen);
            }
        }
        
        return maxLen;
    }
}
```

---

### 79. 盛最多水的容器

**题目链接**：[11. 盛最多水的容器](https://leetcode.cn/problems/container-with-most-water/)

**题目描述**：给定一个长度为 n 的整数数组 height。有 n 条垂线，第 i 条线的两个端点是 (i, 0) 和 (i, height[i])。找出其中的两条线，使得它们与 x 轴共同构成的容器可以容纳最多的水。

**解决思路**：
1. **双指针**：使用左右两个指针向中间移动
2. 每次移动高度较小的一边，因为移动高度较大的一边不可能得到更大的面积

**伪代码实现**：
```
function maxArea(height):
    left = 0
    right = height.length - 1
    maxArea = 0
    
    while left < right:
        width = right - left
        h = min(height[left], height[right])
        maxArea = max(maxArea, width * h)
        
        if height[left] < height[right]:
            left++
        else:
            right--
    
    return maxArea
```

**Java代码实现**：
```java
class Solution {
    public int maxArea(int[] height) {
        int left = 0;
        int right = height.length - 1;
        int maxArea = 0;
        
        while (left < right) {
            int width = right - left;
            int h = Math.min(height[left], height[right]);
            maxArea = Math.max(maxArea, width * h);
            
            if (height[left] < height[right]) {
                left++;
            } else {
                right--;
            }
        }
        
        return maxArea;
    }
}
```

---

### 80. 三数之和

**题目链接**：[15. 三数之和](https://leetcode.cn/problems/3sum/)

**题目描述**：给你一个整数数组 nums，判断是否存在三元组 [nums[i], nums[j], nums[k]] 满足 i != j、i != k 且 j != k，同时还满足 nums[i] + nums[j] + nums[k] == 0。请你返回所有和为 0 且不重复的三元组。

**解决思路**：
1. **排序 + 双指针**：先排序，然后固定一个数，使用双指针找另外两个数
2. 注意去重

**伪代码实现**：
```
function threeSum(nums):
    result = new ArrayList()
    if nums.length < 3: return result
    
    Arrays.sort(nums)
    
    for i from 0 to nums.length - 3:
        if i > 0 and nums[i] == nums[i - 1]: continue
        if nums[i] > 0: break
        
        left = i + 1
        right = nums.length - 1
        
        while left < right:
            sum = nums[i] + nums[left] + nums[right]
            
            if sum == 0:
                result.add([nums[i], nums[left], nums[right]])
                while left < right and nums[left] == nums[left + 1]: left++
                while left < right and nums[right] == nums[right - 1]: right--
                left++
                right--
            else if sum < 0:
                left++
            else:
                right--
    
    return result
```

**Java代码实现**：
```java
class Solution {
    public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        if (nums.length < 3) return result;
        
        Arrays.sort(nums);
        
        for (int i = 0; i < nums.length - 2; i++) {
            if (i > 0 && nums[i] == nums[i - 1]) continue;
            if (nums[i] > 0) break;
            
            int left = i + 1;
            int right = nums.length - 1;
            
            while (left < right) {
                int sum = nums[i] + nums[left] + nums[right];
                
                if (sum == 0) {
                    result.add(Arrays.asList(nums[i], nums[left], nums[right]));
                    while (left < right && nums[left] == nums[left + 1]) left++;
                    while (left < right && nums[right] == nums[right - 1]) right--;
                    left++;
                    right--;
                } else if (sum < 0) {
                    left++;
                } else {
                    right--;
                }
            }
        }
        
        return result;
    }
}
```

---

### 81. 接雨水

**题目链接**：[42. 接雨水](https://leetcode.cn/problems/trapping-rain-water/)

**题目描述**：给定 n 个非负整数表示每个宽度为 1 的柱子的高度图，计算按此排列的柱子，下雨之后能接多少雨水。

**解决思路**：
1. **双指针**：使用左右两个指针，维护左右最大高度
2. 每次移动高度较小的一边

**伪代码实现**：
```
function trap(height):
    if height.length == 0: return 0
    
    left = 0
    right = height.length - 1
    leftMax = 0
    rightMax = 0
    result = 0
    
    while left < right:
        if height[left] < height[right]:
            if height[left] >= leftMax:
                leftMax = height[left]
            else:
                result += leftMax - height[left]
            left++
        else:
            if height[right] >= rightMax:
                rightMax = height[right]
            else:
                result += rightMax - height[right]
            right--
    
    return result
```

**Java代码实现**：
```java
class Solution {
    public int trap(int[] height) {
        if (height.length == 0) return 0;
        
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

### 82. 旋转图像

**题目链接**：[48. 旋转图像](https://leetcode.cn/problems/rotate-image/)

**题目描述**：给定一个 n × n 的二维矩阵 matrix 表示一个图像。请你将图像顺时针旋转 90 度。你必须在原地旋转图像，这意味着你需要直接修改输入的二维矩阵。请不要使用另一个矩阵来旋转图像。

**解决思路**：
1. **先转置，再反转每一行**：顺时针旋转90度 = 转置 + 水平翻转

**伪代码实现**：
```
function rotate(matrix):
    n = matrix.length
    
    // 转置
    for i from 0 to n - 1:
        for j from i + 1 to n - 1:
            swap(matrix[i][j], matrix[j][i])
    
    // 反转每一行
    for i from 0 to n - 1:
        reverse(matrix[i])
```

**Java代码实现**：
```java
class Solution {
    public void rotate(int[][] matrix) {
        int n = matrix.length;
        
        // 转置
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int temp = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = temp;
            }
        }
        
        // 反转每一行
        for (int i = 0; i < n; i++) {
            reverse(matrix[i]);
        }
    }
    
    private void reverse(int[] row) {
        int left = 0, right = row.length - 1;
        while (left < right) {
            int temp = row[left];
            row[left] = row[right];
            row[right] = temp;
            left++;
            right--;
        }
    }
}
```

---

### 83. 矩阵置零

**题目链接**：[73. 矩阵置零](https://leetcode.cn/problems/set-matrix-zeroes/)

**题目描述**：给定一个 m x n 的矩阵，如果一个元素为 0，则将其所在行和列的所有元素都设为 0。请使用原地算法。

**解决思路**：
1. **使用第一行和第一列作为标记**：节省空间
2. 需要额外的变量记录第一行和第一列是否需要置零

**伪代码实现**：
```
function setZeroes(matrix):
    m = matrix.length
    n = matrix[0].length
    firstRowZero = false
    firstColZero = false
    
    // 检查第一行是否需要置零
    for j from 0 to n - 1:
        if matrix[0][j] == 0:
            firstRowZero = true
            break
    
    // 检查第一列是否需要置零
    for i from 0 to m - 1:
        if matrix[i][0] == 0:
            firstColZero = true
            break
    
    // 使用第一行和第一列做标记
    for i from 1 to m - 1:
        for j from 1 to n - 1:
            if matrix[i][j] == 0:
                matrix[i][0] = 0
                matrix[0][j] = 0
    
    // 根据标记置零
    for i from 1 to m - 1:
        for j from 1 to n - 1:
            if matrix[i][0] == 0 or matrix[0][j] == 0:
                matrix[i][j] = 0
    
    // 处理第一行
    if firstRowZero:
        for j from 0 to n - 1:
            matrix[0][j] = 0
    
    // 处理第一列
    if firstColZero:
        for i from 0 to m - 1:
            matrix[i][0] = 0
```

**Java代码实现**：
```java
class Solution {
    public void setZeroes(int[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;
        boolean firstRowZero = false;
        boolean firstColZero = false;
        
        // 检查第一行是否需要置零
        for (int j = 0; j < n; j++) {
            if (matrix[0][j] == 0) {
                firstRowZero = true;
                break;
            }
        }
        
        // 检查第一列是否需要置零
        for (int i = 0; i < m; i++) {
            if (matrix[i][0] == 0) {
                firstColZero = true;
                break;
            }
        }
        
        // 使用第一行和第一列做标记
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                if (matrix[i][j] == 0) {
                    matrix[i][0] = 0;
                    matrix[0][j] = 0;
                }
            }
        }
        
        // 根据标记置零
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                if (matrix[i][0] == 0 || matrix[0][j] == 0) {
                    matrix[i][j] = 0;
                }
            }
        }
        
        // 处理第一行
        if (firstRowZero) {
            for (int j = 0; j < n; j++) {
                matrix[0][j] = 0;
            }
        }
        
        // 处理第一列
        if (firstColZero) {
            for (int i = 0; i < m; i++) {
                matrix[i][0] = 0;
            }
        }
    }
}
```

---

### 84. 螺旋矩阵

**题目链接**：[54. 螺旋矩阵](https://leetcode.cn/problems/spiral-matrix/)

**题目描述**：给你一个 m 行 n 列的矩阵 matrix，请按照顺时针螺旋顺序，返回矩阵中的所有元素。

**解决思路**：
1. **按层模拟**：按照顺时针方向逐层遍历
2. 维护四个边界：top, bottom, left, right

**伪代码实现**：
```
function spiralOrder(matrix):
    if matrix.length == 0: return new ArrayList()
    
    result = new ArrayList()
    top = 0
    bottom = matrix.length - 1
    left = 0
    right = matrix[0].length - 1
    
    while top <= bottom and left <= right:
        // 从左到右
        for j from left to right:
            result.add(matrix[top][j])
        top++
        
        // 从上到下
        for i from top to bottom:
            result.add(matrix[i][right])
        right--
        
        // 从右到左
        if top <= bottom:
            for j from right down to left:
                result.add(matrix[bottom][j])
            bottom--
        
        // 从下到上
        if left <= right:
            for i from bottom down to top:
                result.add(matrix[i][left])
            left++
    
    return result
```

**Java代码实现**：
```java
class Solution {
    public List<Integer> spiralOrder(int[][] matrix) {
        if (matrix.length == 0) return new ArrayList<>();
        
        List<Integer> result = new ArrayList<>();
        int top = 0, bottom = matrix.length - 1;
        int left = 0, right = matrix[0].length - 1;
        
        while (top <= bottom && left <= right) {
            // 从左到右
            for (int j = left; j <= right; j++) {
                result.add(matrix[top][j]);
            }
            top++;
            
            // 从上到下
            for (int i = top; i <= bottom; i++) {
                result.add(matrix[i][right]);
            }
            right--;
            
            // 从右到左
            if (top <= bottom) {
                for (int j = right; j >= left; j--) {
                    result.add(matrix[bottom][j]);
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

### 85. 合并区间

**题目链接**：[56. 合并区间](https://leetcode.cn/problems/merge-intervals/)

**题目描述**：以数组 intervals 表示若干个区间的集合，其中单个区间为 intervals[i] = [starti, endi]。请你合并所有重叠的区间，并返回一个不重叠的区间数组，该数组需恰好覆盖输入中的所有区间。

**解决思路**：
1. **排序**：按照区间起点排序
2. 遍历区间，如果当前区间与上一个区间重叠，则合并

**伪代码实现**：
```
function merge(intervals):
    if intervals.length <= 1: return intervals
    
    Arrays.sort(intervals, (a, b) -> a[0] - b[0])
    result = new ArrayList()
    current = intervals[0]
    
    for i from 1 to intervals.length - 1:
        if intervals[i][0] <= current[1]:
            current[1] = max(current[1], intervals[i][1])
        else:
            result.add(current)
            current = intervals[i]
    
    result.add(current)
    return result.toArray(new int[result.size()][])
```

**Java代码实现**：
```java
class Solution {
    public int[][] merge(int[][] intervals) {
        if (intervals.length <= 1) return intervals;
        
        Arrays.sort(intervals, (a, b) -> a[0] - b[0]);
        List<int[]> result = new ArrayList<>();
        int[] current = intervals[0];
        
        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i][0] <= current[1]) {
                current[1] = Math.max(current[1], intervals[i][1]);
            } else {
                result.add(current);
                current = intervals[i];
            }
        }
        
        result.add(current);
        return result.toArray(new int[result.size()][]);
    }
}
```

---

### 86. 插入区间

**题目链接**：[57. 插入区间](https://leetcode.cn/problems/insert-interval/)

**题目描述**：给你一个无重叠的，按照区间起始端点排序的区间列表。在列表中插入一个新的区间，你需要确保列表中的区间仍然有序且不重叠（如果有必要的话，可以合并区间）。

**解决思路**：
1. **分类讨论**：新区间在当前区间之前、重叠、之后

**伪代码实现**：
```
function insert(intervals, newInterval):
    result = new ArrayList()
    i = 0
    n = intervals.length
    
    // 添加所有在newInterval之前的区间
    while i < n and intervals[i][1] < newInterval[0]:
        result.add(intervals[i])
        i++
    
    // 合并所有与newInterval重叠的区间
    while i < n and intervals[i][0] <= newInterval[1]:
        newInterval[0] = min(newInterval[0], intervals[i][0])
        newInterval[1] = max(newInterval[1], intervals[i][1])
        i++
    
    result.add(newInterval)
    
    // 添加剩余的区间
    while i < n:
        result.add(intervals[i])
        i++
    
    return result.toArray(new int[result.size()][])
```

**Java代码实现**：
```java
class Solution {
    public int[][] insert(int[][] intervals, int[] newInterval) {
        List<int[]> result = new ArrayList<>();
        int i = 0;
        int n = intervals.length;
        
        // 添加所有在newInterval之前的区间
        while (i < n && intervals[i][1] < newInterval[0]) {
            result.add(intervals[i]);
            i++;
        }
        
        // 合并所有与newInterval重叠的区间
        while (i < n && intervals[i][0] <= newInterval[1]) {
            newInterval[0] = Math.min(newInterval[0], intervals[i][0]);
            newInterval[1] = Math.max(newInterval[1], intervals[i][1]);
            i++;
        }
        
        result.add(newInterval);
        
        // 添加剩余的区间
        while (i < n) {
            result.add(intervals[i]);
            i++;
        }
        
        return result.toArray(new int[result.size()][]);
    }
}
```

---

### 87. 跳跃游戏

**题目链接**：[55. 跳跃游戏](https://leetcode.cn/problems/jump-game/)

**题目描述**：给定一个非负整数数组 nums，你最初位于数组的第一个下标。数组中的每个元素代表你在该位置可以跳跃的最大长度。判断你是否能够到达最后一个下标。

**解决思路**：
1. **贪心算法**：维护能到达的最远位置
2. 如果当前位置超过最远位置，则无法到达

**伪代码实现**：
```
function canJump(nums):
    maxReach = 0
    
    for i from 0 to nums.length - 1:
        if i > maxReach: return false
        maxReach = max(maxReach, i + nums[i])
    
    return true
```

**Java代码实现**：
```java
class Solution {
    public boolean canJump(int[] nums) {
        int maxReach = 0;
        
        for (int i = 0; i < nums.length; i++) {
            if (i > maxReach) return false;
            maxReach = Math.max(maxReach, i + nums[i]);
        }
        
        return true;
    }
}
```

---

### 88. 跳跃游戏 II

**题目链接**：[45. 跳跃游戏 II](https://leetcode.cn/problems/jump-game-ii/)

**题目描述**：给定一个长度为 n 的 0 索引整数数组 nums。初始位置为 nums[0]。每个元素 nums[i] 表示从索引 i 向前跳转的最大长度。返回到达 nums[n - 1] 的最小跳跃次数。

**解决思路**：
1. **贪心算法**：维护当前能到达的最远位置和下一步能到达的最远位置
2. 当到达当前边界时，更新边界并增加跳跃次数

**伪代码实现**：
```
function jump(nums):
    if nums.length <= 1: return 0
    
    jumps = 0
    currentEnd = 0
    farthest = 0
    
    for i from 0 to nums.length - 2:
        farthest = max(farthest, i + nums[i])
        
        if i == currentEnd:
            jumps++
            currentEnd = farthest
    
    return jumps
```

**Java代码实现**：
```java
class Solution {
    public int jump(int[] nums) {
        if (nums.length <= 1) return 0;
        
        int jumps = 0;
        int currentEnd = 0;
        int farthest = 0;
        
        for (int i = 0; i < nums.length - 1; i++) {
            farthest = Math.max(farthest, i + nums[i]);
            
            if (i == currentEnd) {
                jumps++;
                currentEnd = farthest;
            }
        }
        
        return jumps;
    }
}
```

---

### 89. 买卖股票的最佳时机

**题目链接**：[121. 买卖股票的最佳时机](https://leetcode.cn/problems/best-time-to-buy-and-sell-stock/)

**题目描述**：给定一个数组 prices，它的第 i 个元素 prices[i] 表示一支给定股票第 i 天的价格。你只能选择某一天买入这只股票，并选择在未来的某一个不同的日子卖出该股票。设计一个算法来计算你所能获取的最大利润。

**解决思路**：
1. **一次遍历**：维护最低价格和最大利润

**伪代码实现**：
```
function maxProfit(prices):
    if prices.length == 0: return 0
    
    minPrice = prices[0]
    maxProfit = 0
    
    for price in prices:
        if price < minPrice:
            minPrice = price
        else if price - minPrice > maxProfit:
            maxProfit = price - minPrice
    
    return maxProfit
```

**Java代码实现**：
```java
class Solution {
    public int maxProfit(int[] prices) {
        if (prices.length == 0) return 0;
        
        int minPrice = prices[0];
        int maxProfit = 0;
        
        for (int price : prices) {
            if (price < minPrice) {
                minPrice = price;
            } else if (price - minPrice > maxProfit) {
                maxProfit = price - minPrice;
            }
        }
        
        return maxProfit;
    }
}
```

---

### 90. 买卖股票的最佳时机 II

**题目链接**：[122. 买卖股票的最佳时机 II](https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-ii/)

**题目描述**：给定一个数组 prices，其中 prices[i] 是一支给定股票第 i 天的价格。设计一个算法来计算你所能获取的最大利润。你可以尽可能地完成更多的交易（多次买卖一支股票）。

**解决思路**：
1. **贪心算法**：只要今天的价格比昨天高，就进行买卖
2. 累加所有正利润

**伪代码实现**：
```
function maxProfit(prices):
    profit = 0
    
    for i from 1 to prices.length - 1:
        if prices[i] > prices[i - 1]:
            profit += prices[i] - prices[i - 1]
    
    return profit
```

**Java代码实现**：
```java
class Solution {
    public int maxProfit(int[] prices) {
        int profit = 0;
        
        for (int i = 1; i < prices.length; i++) {
            if (prices[i] > prices[i - 1]) {
                profit += prices[i] - prices[i - 1];
            }
        }
        
        return profit;
    }
}
```

---

### 91. 分发糖果

**题目链接**：[135. 分发糖果](https://leetcode.cn/problems/candy/)

**题目描述**：n 个孩子站成一排。给你一个整数数组 ratings 表示每个孩子的评分。你需要按照以下要求，给这些孩子分发糖果：每个孩子至少分配到 1 个糖果。相邻两个孩子评分更高的孩子会获得更多的糖果。请你给每个孩子分发糖果，计算并返回需要准备的 最少糖果数目。

**解决思路**：
1. **两次遍历**：从左到右和从右到左各遍历一次
2. 取两次遍历的最大值

**伪代码实现**：
```
function candy(ratings):
    n = ratings.length
    candies = new int[n]
    Arrays.fill(candies, 1)
    
    // 从左到右
    for i from 1 to n - 1:
        if ratings[i] > ratings[i - 1]:
            candies[i] = candies[i - 1] + 1
    
    // 从右到左
    for i from n - 2 down to 0:
        if ratings[i] > ratings[i + 1]:
            candies[i] = max(candies[i], candies[i + 1] + 1)
    
    sum = 0
    for c in candies:
        sum += c
    
    return sum
```

**Java代码实现**：
```java
class Solution {
    public int candy(int[] ratings) {
        int n = ratings.length;
        int[] candies = new int[n];
        Arrays.fill(candies, 1);
        
        // 从左到右
        for (int i = 1; i < n; i++) {
            if (ratings[i] > ratings[i - 1]) {
                candies[i] = candies[i - 1] + 1;
            }
        }
        
        // 从右到左
        for (int i = n - 2; i >= 0; i--) {
            if (ratings[i] > ratings[i + 1]) {
                candies[i] = Math.max(candies[i], candies[i + 1] + 1);
            }
        }
        
        int sum = 0;
        for (int c : candies) {
            sum += c;
        }
        
        return sum;
    }
}
```

---

### 92. 根据身高重建队列

**题目链接**：[406. 根据身高重建队列](https://leetcode.cn/problems/queue-reconstruction-by-height/)

**题目描述**：假设有打乱顺序的一群人站成一个队列，数组 people 表示队列中一些人的属性（不一定按顺序）。每个 people[i] = [hi, ki] 表示第 i 个人的身高为 hi，前面正好有 ki 个身高大于或等于 hi 的人。请你重新构造并返回输入数组 people 所表示的队列。

**解决思路**：
1. **排序 + 插入**：先按身高降序、ki升序排序
2. 然后按ki值插入到结果列表中

**伪代码实现**：
```
function reconstructQueue(people):
    Arrays.sort(people, (a, b) -> {
        if a[0] != b[0]: return b[0] - a[0]
        return a[1] - b[1]
    })
    
    result = new ArrayList()
    for p in people:
        result.add(p[1], p)
    
    return result.toArray(new int[result.size()][])
```

**Java代码实现**：
```java
class Solution {
    public int[][] reconstructQueue(int[][] people) {
        Arrays.sort(people, (a, b) -> {
            if (a[0] != b[0]) return b[0] - a[0];
            return a[1] - b[1];
        });
        
        List<int[]> result = new ArrayList<>();
        for (int[] p : people) {
            result.add(p[1], p);
        }
        
        return result.toArray(new int[result.size()][]);
    }
}
```

---

### 93. 任务调度器

**题目链接**：[621. 任务调度器](https://leetcode.cn/problems/task-scheduler/)

**题目描述**：给你一个用字符数组 tasks 表示的 CPU 需要执行的任务列表。其中每个字母表示一种不同种类的任务。任务可以以任意顺序执行，并且每个任务都可以在 1 个单位时间内执行完。在任何一个单位时间，CPU 可以完成一个任务，或者处于待命状态。然而，两个相同种类的任务之间必须有长度为整数 n 的冷却时间，因此至少有连续 n 个单位时间内 CPU 在执行不同的任务，或者在待命状态。你需要计算完成所有任务所需要的最短时间。

**解决思路**：
1. **数学公式**：计算最大频率任务所需时间
2. 公式：max(tasks.length, (maxFreq - 1) * (n + 1) + maxCount)

**伪代码实现**：
```
function leastInterval(tasks, n):
    count = new int[26]
    for task in tasks:
        count[task - 'A']++
    
    maxFreq = 0
    for c in count:
        maxFreq = max(maxFreq, c)
    
    maxCount = 0
    for c in count:
        if c == maxFreq:
            maxCount++
    
    partCount = maxFreq - 1
    partLength = n - (maxCount - 1)
    emptySlots = partCount * partLength
    availableTasks = tasks.length - maxFreq * maxCount
    idles = max(0, emptySlots - availableTasks)
    
    return tasks.length + idles
```

**Java代码实现**：
```java
class Solution {
    public int leastInterval(char[] tasks, int n) {
        int[] count = new int[26];
        for (char task : tasks) {
            count[task - 'A']++;
        }
        
        int maxFreq = 0;
        for (int c : count) {
            maxFreq = Math.max(maxFreq, c);
        }
        
        int maxCount = 0;
        for (int c : count) {
            if (c == maxFreq) {
                maxCount++;
            }
        }
        
        int partCount = maxFreq - 1;
        int partLength = n - (maxCount - 1);
        int emptySlots = partCount * partLength;
        int availableTasks = tasks.length - maxFreq * maxCount;
        int idles = Math.max(0, emptySlots - availableTasks);
        
        return tasks.length + idles;
    }
}
```

---

### 94. 划分字母区间

**题目链接**：[763. 划分字母区间](https://leetcode.cn/problems/partition-labels/)

**题目描述**：给你一个字符串 s。我们要把这个字符串划分为尽可能多的片段，同一字母最多出现在一个片段中。返回一个表示每个字符串片段的长度的列表。

**解决思路**：
1. **贪心算法**：先记录每个字符最后出现的位置
2. 遍历字符串，维护当前片段的结束位置

**伪代码实现**：
```
function partitionLabels(s):
    last = new int[26]
    for i from 0 to s.length - 1:
        last[s.charAt(i) - 'a'] = i
    
    result = new ArrayList()
    start = 0
    end = 0
    
    for i from 0 to s.length - 1:
        end = max(end, last[s.charAt(i) - 'a'])
        
        if i == end:
            result.add(end - start + 1)
            start = end + 1
    
    return result
```

**Java代码实现**：
```java
class Solution {
    public List<Integer> partitionLabels(String s) {
        int[] last = new int[26];
        for (int i = 0; i < s.length(); i++) {
            last[s.charAt(i) - 'a'] = i;
        }
        
        List<Integer> result = new ArrayList<>();
        int start = 0;
        int end = 0;
        
        for (int i = 0; i < s.length(); i++) {
            end = Math.max(end, last[s.charAt(i) - 'a']);
            
            if (i == end) {
                result.add(end - start + 1);
                start = end + 1;
            }
        }
        
        return result;
    }
}
```

---

### 95. 移掉 K 位数字

**题目链接**：[402. 移掉 K 位数字](https://leetcode.cn/problems/remove-k-digits/)

**题目描述**：给你一个以字符串表示的非负整数 num 和一个整数 k，移除这个数中的 k 位数字，使得剩下的数字最小。请你以字符串形式返回这个最小的数字。

**解决思路**：
1. **单调栈**：维护一个递增的栈
2. 当当前数字小于栈顶时，弹出栈顶（移除数字）

**伪代码实现**：
```
function removeKdigits(num, k):
    if k >= num.length(): return "0"
    
    stack = new Stack()
    
    for char c in num:
        while k > 0 and not stack.isEmpty() and stack.peek() > c:
            stack.pop()
            k--
        stack.push(c)
    
    // 如果k还有剩余，从栈顶移除
    while k > 0:
        stack.pop()
        k--
    
    // 构建结果，去除前导零
    result = ""
    while not stack.isEmpty():
        result = stack.pop() + result
    
    // 去除前导零
    while result.length() > 1 and result.charAt(0) == '0':
        result = result.substring(1)
    
    return result
```

**Java代码实现**：
```java
class Solution {
    public String removeKdigits(String num, int k) {
        if (k >= num.length()) return "0";
        
        Stack<Character> stack = new Stack<>();
        
        for (char c : num.toCharArray()) {
            while (k > 0 && !stack.isEmpty() && stack.peek() > c) {
                stack.pop();
                k--;
            }
            stack.push(c);
        }
        
        // 如果k还有剩余，从栈顶移除
        while (k > 0) {
            stack.pop();
            k--;
        }
        
        // 构建结果，去除前导零
        StringBuilder result = new StringBuilder();
        while (!stack.isEmpty()) {
            result.insert(0, stack.pop());
        }
        
        // 去除前导零
        while (result.length() > 1 && result.charAt(0) == '0') {
            result.deleteCharAt(0);
        }
        
        return result.toString();
    }
}
```

---

### 96. 数据流的中位数

**题目链接**：[295. 数据流的中位数](https://leetcode.cn/problems/find-median-from-data-stream/)

**题目描述**：中位数是有序整数列表中的中间值。如果列表的大小是偶数，则没有中间值，中位数是两个中间值的平均值。设计一个支持以下两种操作的数据结构：void addNum(int num) - 从数据流中添加一个整数到数据结构中。double findMedian() - 返回目前所有元素的中位数。

**解决思路**：
1. **双堆**：使用最大堆存储较小的一半，最小堆存储较大的一半
2. 保持两个堆的大小差不超过1

**伪代码实现**：
```
class MedianFinder:
    maxHeap = new PriorityQueue(Collections.reverseOrder())
    minHeap = new PriorityQueue()
    
    function addNum(num):
        maxHeap.offer(num)
        minHeap.offer(maxHeap.poll())
        
        if maxHeap.size() < minHeap.size():
            maxHeap.offer(minHeap.poll())
    
    function findMedian():
        if maxHeap.size() > minHeap.size():
            return maxHeap.peek()
        return (maxHeap.peek() + minHeap.peek()) / 2.0
```

**Java代码实现**：
```java
class MedianFinder {
    private PriorityQueue<Integer> maxHeap;
    private PriorityQueue<Integer> minHeap;
    
    public MedianFinder() {
        maxHeap = new PriorityQueue<>(Collections.reverseOrder());
        minHeap = new PriorityQueue<>();
    }
    
    public void addNum(int num) {
        maxHeap.offer(num);
        minHeap.offer(maxHeap.poll());
        
        if (maxHeap.size() < minHeap.size()) {
            maxHeap.offer(minHeap.poll());
        }
    }
    
    public double findMedian() {
        if (maxHeap.size() > minHeap.size()) {
            return maxHeap.peek();
        }
        return (maxHeap.peek() + minHeap.peek()) / 2.0;
    }
}
```

---

### 97. 前 K 个高频元素

**题目链接**：[347. 前 K 个高频元素](https://leetcode.cn/problems/top-k-frequent-elements/)

**题目描述**：给你一个整数数组 nums 和一个整数 k，请你返回其中出现频率前 k 高的元素。你可以按任意顺序返回答案。

**解决思路**：
1. **哈希表 + 最小堆**：统计频率，使用大小为k的最小堆

**伪代码实现**：
```
function topKFrequent(nums, k):
    map = new HashMap()
    for num in nums:
        map.put(num, map.getOrDefault(num, 0) + 1)
    
    minHeap = new PriorityQueue((a, b) -> a[1] - b[1])
    
    for entry in map.entrySet():
        minHeap.offer(new int[]{entry.getKey(), entry.getValue()})
        if minHeap.size() > k:
            minHeap.poll()
    
    result = new int[k]
    for i from k - 1 down to 0:
        result[i] = minHeap.poll()[0]
    
    return result
```

**Java代码实现**：
```java
class Solution {
    public int[] topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int num : nums) {
            map.put(num, map.getOrDefault(num, 0) + 1);
        }
        
        PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> a[1] - b[1]);
        
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            minHeap.offer(new int[]{entry.getKey(), entry.getValue()});
            if (minHeap.size() > k) {
                minHeap.poll();
            }
        }
        
        int[] result = new int[k];
        for (int i = k - 1; i >= 0; i--) {
            result[i] = minHeap.poll()[0];
        }
        
        return result;
    }
}
```

---

### 98. 滑动窗口最大值

**题目链接**：[239. 滑动窗口最大值](https://leetcode.cn/problems/sliding-window-maximum/)

**题目描述**：给你一个整数数组 nums，有一个大小为 k 的滑动窗口从数组的最左侧移动到数组的最右侧。你只可以看到在滑动窗口内的 k 个数字。滑动窗口每次只向右移动一位。返回滑动窗口中的最大值。

**解决思路**：
1. **单调队列**：使用双端队列维护单调递减序列
2. 队列中存储下标，队首为当前窗口最大值

**伪代码实现**：
```
function maxSlidingWindow(nums, k):
    result = new ArrayList()
    deque = new ArrayDeque()
    
    for i from 0 to nums.length - 1:
        // 移除不在窗口内的元素
        while not deque.isEmpty() and deque.peekFirst() < i - k + 1:
            deque.pollFirst()
        
        // 移除小于当前元素的元素
        while not deque.isEmpty() and nums[deque.peekLast()] < nums[i]:
            deque.pollLast()
        
        deque.offerLast(i)
        
        // 记录结果
        if i >= k - 1:
            result.add(nums[deque.peekFirst()])
    
    return result.stream().mapToInt(Integer::intValue).toArray()
```

**Java代码实现**：
```java
class Solution {
    public int[] maxSlidingWindow(int[] nums, int k) {
        List<Integer> result = new ArrayList<>();
        Deque<Integer> deque = new ArrayDeque<>();
        
        for (int i = 0; i < nums.length; i++) {
            // 移除不在窗口内的元素
            while (!deque.isEmpty() && deque.peekFirst() < i - k + 1) {
                deque.pollFirst();
            }
            
            // 移除小于当前元素的元素
            while (!deque.isEmpty() && nums[deque.peekLast()] < nums[i]) {
                deque.pollLast();
            }
            
            deque.offerLast(i);
            
            // 记录结果
            if (i >= k - 1) {
                result.add(nums[deque.peekFirst()]);
            }
        }
        
        return result.stream().mapToInt(Integer::intValue).toArray();
    }
}
```

---

### 99. 数组中的第K个最大元素

**题目链接**：[215. 数组中的第K个最大元素](https://leetcode.cn/problems/kth-largest-element-in-an-array/)

**题目描述**：给定整数数组 nums 和整数 k，请返回数组中第 k 个最大的元素。请注意，你需要找的是数组排序后的第 k 个最大的元素，而不是第 k 个不同的元素。

**解决思路**：
1. **快速选择**：基于快速排序的选择算法
2. **最小堆**：维护大小为k的最小堆

**伪代码实现（快速选择）**：
```
function findKthLargest(nums, k):
    return quickSelect(nums, 0, nums.length - 1, nums.length - k)

function quickSelect(nums, left, right, k):
    if left == right: return nums[left]
    
    pivotIndex = partition(nums, left, right)
    
    if pivotIndex == k:
        return nums[k]
    else if pivotIndex < k:
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
        return quickSelect(nums, 0, nums.length - 1, nums.length - k);
    }
    
    private int quickSelect(int[] nums, int left, int right, int k) {
        if (left == right) return nums[left];
        
        int pivotIndex = partition(nums, left, right);
        
        if (pivotIndex == k) {
            return nums[k];
        } else if (pivotIndex < k) {
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

---

### 100. 实现 Trie（前缀树）

**题目链接**：[208. 实现 Trie（前缀树）](https://leetcode.cn/problems/implement-trie-prefix-tree/)

**题目描述**：Trie（发音类似 "try"）或者说前缀树是一种树形数据结构，用于高效地存储和检索字符串数据集中的键。这一数据结构有相当多的应用情景，例如自动补完和拼写检查。请你实现 Trie 类。

**解决思路**：
1. **树形结构**：每个节点包含子节点数组和是否为单词结尾的标记

**伪代码实现**：
```
class TrieNode:
    children = new TrieNode[26]
    isEnd = false

class Trie:
    root = new TrieNode()
    
    function insert(word):
        node = root
        for char c in word:
            index = c - 'a'
            if node.children[index] == null:
                node.children[index] = new TrieNode()
            node = node.children[index]
        node.isEnd = true
    
    function search(word):
        node = searchPrefix(word)
        return node != null and node.isEnd
    
    function startsWith(prefix):
        return searchPrefix(prefix) != null
    
    function searchPrefix(prefix):
        node = root
        for char c in prefix:
            index = c - 'a'
            if node.children[index] == null:
                return null
            node = node.children[index]
        return node
```

**Java代码实现**：
```java
class Trie {
    private TrieNode root;
    
    class TrieNode {
        TrieNode[] children;
        boolean isEnd;
        
        public TrieNode() {
            children = new TrieNode[26];
            isEnd = false;
        }
    }
    
    public Trie() {
        root = new TrieNode();
    }
    
    public void insert(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            int index = c - 'a';
            if (node.children[index] == null) {
                node.children[index] = new TrieNode();
            }
            node = node.children[index];
        }
        node.isEnd = true;
    }
    
    public boolean search(String word) {
        TrieNode node = searchPrefix(word);
        return node != null && node.isEnd;
    }
    
    public boolean startsWith(String prefix) {
        return searchPrefix(prefix) != null;
    }
    
    private TrieNode searchPrefix(String prefix) {
        TrieNode node = root;
        for (char c : prefix.toCharArray()) {
            int index = c - 'a';
            if (node.children[index] == null) {
                return null;
            }
            node = node.children[index];
        }
        return node;
    }
}
```

---

## 总结

本文档包含了LeetCode Top 100常见题目的完整实现，涵盖了以下六大类算法问题：

### 1. 数组（15题）
- 双指针、二分查找、滑动窗口、前缀和等经典技巧
- 重点题目：两数之和、三数之和、接雨水、盛最多水的容器

### 2. 链表（15题）
- 链表的基本操作：反转、合并、删除、检测环等
- 重点题目：反转链表、合并K个升序链表、LRU缓存、环形链表

### 3. 二叉树（15题）
- 树的遍历：前序、中序、后序、层序
- 重点题目：二叉树的中序遍历、二叉树的层序遍历、二叉树的最大深度、路径总和

### 4. 动态规划（15题）
- 经典DP问题：斐波那契、背包、路径、子序列等
- 重点题目：爬楼梯、最长递增子序列、零钱兑换、编辑距离

### 5. 字符串（15题）
- 字符串处理技巧：滑动窗口、KMP、双指针、栈
- 重点题目：无重复字符的最长子串、最长回文子串、最小覆盖子串、有效的括号

### 6. 其他类型（25题）
- 贪心算法、哈希表、堆、单调栈/队列、并查集、Trie树等
- 重点题目：两数之和、跳跃游戏、买卖股票的最佳时机、数据流的中位数、前K个高频元素

### 学习建议

1. **循序渐进**：按照分类顺序学习，先掌握基础算法，再攻克复杂问题
2. **理解思想**：不仅要会写代码，更要理解算法的设计思想和适用场景
3. **举一反三**：每道题都要思考是否有其他解法，以及能否应用到其他问题
4. **定期复习**：算法需要反复练习，建议定期回顾已学过的题目
5. **动手实践**：光看是不够的，一定要自己动手写代码，调试和优化

希望这份文档能帮助你系统地学习和掌握LeetCode常见算法题目！


---
