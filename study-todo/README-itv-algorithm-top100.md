# LeetCode Top 100 常见题目实现

本文档包含 LeetCode 常见的 100 道算法题目，涵盖了面试中经常出现的各种类型的问题。每个题目都包含详细的题目描述、原题链接、解决思路、伪代码实现和完整的 Java 代码实现。

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

**题目描述**：给你一个整数数组 nums，判断是否存在三元组 [nums[i], nums[j], nums[k]] 满足 i != j、i != k 且 j != k，同时还满足 nums[i] + nums[j] + nums[k] == 0。请你返回所有和为 0 且不重复的三元组。注意：答案中不可以包含重复的三元组。

**解决思路**：
1. **排序 + 双指针**：先排序数组，然后固定一个数，使用双指针找另外两个数
2. 注意跳过重复元素

**伪代码实现**：
```
function threeSum(nums):
    result = []
    sort(nums)
    
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

### 3. 四数之和

**题目链接**：[18. 四数之和](https://leetcode.cn/problems/4sum/)

**题目描述**：给你一个由 n 个整数组成的数组 nums，和一个目标值 target。请你找出并返回满足下述全部条件且不重复的四元组 [nums[a], nums[b], nums[c], nums[d]]（若两个四元组元素一一对应，则认为两个四元组重复）。

**解决思路**：
1. **排序 + 双指针**：在三数之和的基础上再套一层循环
2. 注意剪枝和去重

**伪代码实现**：
```
function fourSum(nums, target):
    result = []
    sort(nums)
    n = nums.length
    
    for i from 0 to n - 4:
        if i > 0 and nums[i] == nums[i - 1]: continue
        if (long)nums[i] + nums[i + 1] + nums[i + 2] + nums[i + 3] > target: break
        if (long)nums[i] + nums[n - 3] + nums[n - 2] + nums[n - 1] < target: continue
        
        for j from i + 1 to n - 3:
            if j > i + 1 and nums[j] == nums[j - 1]: continue
            if (long)nums[i] + nums[j] + nums[j + 1] + nums[j + 2] > target: break
            if (long)nums[i] + nums[j] + nums[n - 2] + nums[n - 1] < target: continue
            
            left = j + 1
            right = n - 1
            
            while left < right:
                sum = (long)nums[i] + nums[j] + nums[left] + nums[right]
                
                if sum == target:
                    result.add([nums[i], nums[j], nums[left], nums[right]])
                    while left < right and nums[left] == nums[left + 1]: left++
                    while left < right and nums[right] == nums[right - 1]: right--
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
        int n = nums.length;
        
        for (int i = 0; i < n - 3; i++) {
            if (i > 0 && nums[i] == nums[i - 1]) continue;
            if ((long)nums[i] + nums[i + 1] + nums[i + 2] + nums[i + 3] > target) break;
            if ((long)nums[i] + nums[n - 3] + nums[n - 2] + nums[n - 1] < target) continue;
            
            for (int j = i + 1; j < n - 2; j++) {
                if (j > i + 1 && nums[j] == nums[j - 1]) continue;
                if ((long)nums[i] + nums[j] + nums[j + 1] + nums[j + 2] > target) break;
                if ((long)nums[i] + nums[j] + nums[n - 2] + nums[n - 1] < target) continue;
                
                int left = j + 1;
                int right = n - 1;
                
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
1. **双指针**：使用左右两个指针向中间移动
2. 每次移动高度较小的指针，因为容器的容量受限于较小的高度

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

### 5. 接雨水

**题目链接**：[42. 接雨水](https://leetcode.cn/problems/trapping-rain-water/)

**题目描述**：给定 n 个非负整数表示每个宽度为 1 的柱子的高度图，计算按此排列的柱子，下雨之后能接多少雨水。

**解决思路**：
1. **双指针**：使用左右两个指针，维护左右最大高度
2. 对于当前位置，能接的雨水量取决于左右最大高度的较小值减去当前高度

**伪代码实现**：
```
function trap(height):
    if height == null or height.length == 0: return 0
    
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
        if (height == null || height.length == 0) return 0;
        
        int left = 0;
        int right = height.length - 1;
        int leftMax = 0;
        int rightMax = 0;
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

### 6. 下一个排列

**题目链接**：[31. 下一个排列](https://leetcode.cn/problems/next-permutation/)

**题目描述**：整数数组的一个排列就是将其所有成员以序列或线性顺序排列。例如，arr = [1,2,3]，以下这些都可以视作 arr 的排列：[1,2,3]、[1,3,2]、[3,1,2]、[2,3,1]。整数数组的下一个排列是指其整数的下一个字典序更大的排列。如果不存在下一个更大的排列，那么这个数组必须重排为字典序最小的排列（即，其元素按升序排列）。例如，arr = [1,2,3] 的下一个排列是 [1,3,2]。类似地，arr = [2,3,1] 的下一个排列是 [3,1,2]。而 arr = [3,2,1] 的下一个排列是 [1,2,3]，因为 [3,2,1] 不存在一个字典序更大的排列。给你一个整数数组 nums，找出 nums 的下一个排列。

**解决思路**：
1. 从后向前找到第一个升序对 (i, i+1)，即 nums[i] < nums[i+1]
2. 从后向前找到第一个大于 nums[i] 的元素 nums[j]
3. 交换 nums[i] 和 nums[j]
4. 反转 i+1 到末尾的元素

**伪代码实现**：
```
function nextPermutation(nums):
    n = nums.length
    i = n - 2
    
    // 从后向前找第一个升序对
    while i >= 0 and nums[i] >= nums[i + 1]:
        i--
    
    if i >= 0:
        j = n - 1
        // 从后向前找第一个大于 nums[i] 的元素
        while j >= 0 and nums[j] <= nums[i]:
            j--
        swap(nums, i, j)
    
    // 反转 i+1 到末尾
    reverse(nums, i + 1, n - 1)
```

**Java代码实现**：
```java
class Solution {
    public void nextPermutation(int[] nums) {
        int n = nums.length;
        int i = n - 2;
        
        // 从后向前找第一个升序对
        while (i >= 0 && nums[i] >= nums[i + 1]) {
            i--;
        }
        
        if (i >= 0) {
            int j = n - 1;
            // 从后向前找第一个大于 nums[i] 的元素
            while (j >= 0 && nums[j] <= nums[i]) {
                j--;
            }
            swap(nums, i, j);
        }
        
        // 反转 i+1 到末尾
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

### 7. 搜索旋转排序数组

**题目链接**：[33. 搜索旋转排序数组](https://leetcode.cn/problems/search-in-rotated-sorted-array/)

**题目描述**：整数数组 nums 按升序排列，数组中的值互不相同。在传递给函数之前，nums 在预先未知的某个下标 k（0 <= k < nums.length）上进行了旋转，使数组变为 [nums[k], nums[k+1], ..., nums[n-1], nums[0], nums[1], ..., nums[k-1]]（下标从 0 开始计数）。给你旋转后的数组 nums 和一个整数 target，如果 nums 中存在这个目标值 target，则返回它的下标，否则返回 -1。

**解决思路**：
1. **二分查找**：虽然数组被旋转，但部分仍然有序
2. 判断哪一半是有序的，然后决定搜索范围

**伪代码实现**：
```
function search(nums, target):
    left = 0
    right = nums.length - 1
    
    while left <= right:
        mid = left + (right - left) / 2
        
        if nums[mid] == target:
            return mid
        
        // 左半部分有序
        if nums[left] <= nums[mid]:
            if nums[left] <= target and target < nums[mid]:
                right = mid - 1
            else:
                left = mid + 1
        // 右半部分有序
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
            
            // 左半部分有序
            if (nums[left] <= nums[mid]) {
                if (nums[left] <= target && target < nums[mid]) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            } else {
                // 右半部分有序
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

### 8. 在排序数组中查找元素的第一个和最后一个位置

**题目链接**：[34. 在排序数组中查找元素的第一个和最后一个位置](https://leetcode.cn/problems/find-first-and-last-position-of-element-in-sorted-array/)

**题目描述**：给你一个按照非递减顺序排列的整数数组 nums，和一个目标值 target。请你找出给定目标值在数组中的开始位置和结束位置。如果数组中不存在目标值 target，返回 [-1, -1]。你必须设计并实现时间复杂度为 O(log n) 的算法解决此问题。

**解决思路**：
1. **二分查找**：分别查找第一个和最后一个位置
2. 查找第一个位置时，找到 target 后继续向左搜索
3. 查找最后一个位置时，找到 target 后继续向右搜索

**伪代码实现**：
```
function searchRange(nums, target):
    result = [-1, -1]
    result[0] = findFirst(nums, target)
    result[1] = findLast(nums, target)
    return result

function findFirst(nums, target):
    left = 0
    right = nums.length - 1
    index = -1
    
    while left <= right:
        mid = left + (right - left) / 2
        if nums[mid] >= target:
            right = mid - 1
        else:
            left = mid + 1
        if nums[mid] == target:
            index = mid
    
    return index

function findLast(nums, target):
    left = 0
    right = nums.length - 1
    index = -1
    
    while left <= right:
        mid = left + (right - left) / 2
        if nums[mid] <= target:
            left = mid + 1
        else:
            right = mid - 1
        if nums[mid] == target:
            index = mid
    
    return index
```

**Java代码实现**：
```java
class Solution {
    public int[] searchRange(int[] nums, int target) {
        int[] result = {-1, -1};
        result[0] = findFirst(nums, target);
        result[1] = findLast(nums, target);
        return result;
    }
    
    private int findFirst(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        int index = -1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] >= target) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
            if (nums[mid] == target) {
                index = mid;
            }
        }
        
        return index;
    }
    
    private int findLast(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        int index = -1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] <= target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
            if (nums[mid] == target) {
                index = mid;
            }
        }
        
        return index;
    }
}
```

---

### 9. 组合总和

**题目链接**：[39. 组合总和](https://leetcode.cn/problems/combination-sum/)

**题目描述**：给你一个无重复元素的整数数组 candidates 和一个目标整数 target，找出 candidates 中可以使数字和为目标数 target 的所有不同组合，并以列表形式返回。你可以按任意顺序返回这些组合。candidates 中的同一个数字可以无限制重复被选取。如果至少一个数字的被选数量不同，则两种组合是不同的。

**解决思路**：
1. **回溯法**：递归尝试每个数字
2. 由于可以重复使用，递归时从当前位置开始
3. 注意剪枝优化

**伪代码实现**：
```
function combinationSum(candidates, target):
    result = []
    sort(candidates)
    backtrack(candidates, target, 0, [], result)
    return result

function backtrack(candidates, target, start, path, result):
    if target == 0:
        result.add(new ArrayList(path))
        return
    
    if target < 0:
        return
    
    for i from start to candidates.length - 1:
        if candidates[i] > target: break
        path.add(candidates[i])
        backtrack(candidates, target - candidates[i], i, path, result)
        path.remove(path.size() - 1)
```

**Java代码实现**：
```java
class Solution {
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
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
            if (candidates[i] > target) break;
            path.add(candidates[i]);
            backtrack(candidates, target - candidates[i], i, path, result);
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
1. **回溯法**：递归生成所有排列
2. 使用一个数组标记哪些元素已经被使用
3. 每次选择一个未使用的元素加入当前排列

**伪代码实现**：
```
function permute(nums):
    result = []
    used = new boolean[nums.length]
    backtrack(nums, used, [], result)
    return result

function backtrack(nums, used, path, result):
    if path.size() == nums.length:
        result.add(new ArrayList(path))
        return
    
    for i from 0 to nums.length - 1:
        if used[i]: continue
        
        used[i] = true
        path.add(nums[i])
        backtrack(nums, used, path, result)
        path.remove(path.size() - 1)
        used[i] = false
```

**Java代码实现**：
```java
class Solution {
    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        boolean[] used = new boolean[nums.length];
        backtrack(nums, used, new ArrayList<>(), result);
        return result;
    }
    
    private void backtrack(int[] nums, boolean[] used, List<Integer> path, List<List<Integer>> result) {
        if (path.size() == nums.length) {
            result.add(new ArrayList<>(path));
            return;
        }
        
        for (int i = 0; i < nums.length; i++) {
            if (used[i]) continue;
            
            used[i] = true;
            path.add(nums[i]);
            backtrack(nums, used, path, result);
            path.remove(path.size() - 1);
            used[i] = false;
        }
    }
}
```

---

### 11. 子集

**题目链接**：[78. 子集](https://leetcode.cn/problems/subsets/)

**题目描述**：给你一个整数数组 nums，数组中的元素互不相同。返回该数组所有可能的子集（幂集）。解集不能包含重复的子集。你可以按任意顺序返回解集。

**解决思路**：
1. **回溯法**：对于每个元素，选择包含或不包含
2. 递归生成所有可能的子集

**伪代码实现**：
```
function subsets(nums):
    result = []
    backtrack(nums, 0, [], result)
    return result

function backtrack(nums, start, path, result):
    result.add(new ArrayList(path))
    
    for i from start to nums.length - 1:
        path.add(nums[i])
        backtrack(nums, i + 1, path, result)
        path.remove(path.size() - 1)
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

### 12. 电话号码的字母组合

**题目链接**：[17. 电话号码的字母组合](https://leetcode.cn/problems/letter-combinations-of-a-phone-number/)

**题目描述**：给定一个仅包含数字 2-9 的字符串，返回所有它能表示的字母组合。答案可以按任意顺序返回。给出数字到字母的映射如下（与电话按键相同）。注意 1 不对应任何字母。

**解决思路**：
1. **回溯法**：对每个数字，尝试所有可能的字母
2. 建立数字到字母的映射表
3. 递归生成所有组合

**伪代码实现**：
```
function letterCombinations(digits):
    if digits == null or digits.length == 0: return []
    
    map = ["", "", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"]
    result = []
    backtrack(digits, map, 0, "", result)
    return result

function backtrack(digits, map, index, current, result):
    if index == digits.length:
        result.add(current)
        return
    
    digit = digits.charAt(index) - '0'
    letters = map[digit]
    
    for i from 0 to letters.length - 1:
        backtrack(digits, map, index + 1, current + letters.charAt(i), result)
```

**Java代码实现**：
```java
class Solution {
    public List<String> letterCombinations(String digits) {
        if (digits == null || digits.length() == 0) return new ArrayList<>();
        
        String[] map = {"", "", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"};
        List<String> result = new ArrayList<>();
        backtrack(digits, map, 0, "", result);
        return result;
    }
    
    private void backtrack(String digits, String[] map, int index, String current, List<String> result) {
        if (index == digits.length()) {
            result.add(current);
            return;
        }
        
        int digit = digits.charAt(index) - '0';
        String letters = map[digit];
        
        for (int i = 0; i < letters.length(); i++) {
            backtrack(digits, map, index + 1, current + letters.charAt(i), result);
        }
    }
}
```

---

### 13. 岛屿数量

**题目链接**：[200. 岛屿数量](https://leetcode.cn/problems/number-of-islands/)

**题目描述**：给你一个由 '1'（陆地）和 '0'（水）组成的的二维网格，请你计算网格中岛屿的数量。岛屿总是被水包围，并且每座岛屿只能由水平方向和/或竖直方向上相邻的陆地连接形成。此外，你可以假设该网格的四条边均被水包围。

**解决思路**：
1. **DFS/BFS**：遍历网格，遇到 '1' 时进行深度优先搜索或广度优先搜索
2. 将访问过的 '1' 标记为 '0'，避免重复计数
3. 每次遇到未访问的 '1'，岛屿数量加1

**伪代码实现（DFS）**：
```
function numIslands(grid):
    if grid == null or grid.length == 0: return 0
    
    count = 0
    for i from 0 to grid.length - 1:
        for j from 0 to grid[0].length - 1:
            if grid[i][j] == '1':
                count++
                dfs(grid, i, j)
    
    return count

function dfs(grid, i, j):
    if i < 0 or i >= grid.length or j < 0 or j >= grid[0].length or grid[i][j] == '0':
        return
    
    grid[i][j] = '0'
    dfs(grid, i + 1, j)
    dfs(grid, i - 1, j)
    dfs(grid, i, j + 1)
    dfs(grid, i, j - 1)
```

**Java代码实现（DFS）**：
```java
class Solution {
    public int numIslands(char[][] grid) {
        if (grid == null || grid.length == 0) return 0;
        
        int count = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == '1') {
                    count++;
                    dfs(grid, i, j);
                }
            }
        }
        
        return count;
    }
    
    private void dfs(char[][] grid, int i, int j) {
        if (i < 0 || i >= grid.length || j < 0 || j >= grid[0].length || grid[i][j] == '0') {
            return;
        }
        
        grid[i][j] = '0';
        dfs(grid, i + 1, j);
        dfs(grid, i - 1, j);
        dfs(grid, i, j + 1);
        dfs(grid, i, j - 1);
    }
}
```

---

### 14. 课程表

**题目链接**：[207. 课程表](https://leetcode.cn/problems/course-schedule/)

**题目描述**：你这个学期必须选修 numCourses 门课程，记为 0 到 numCourses - 1 。在选修某些课程之前需要一些先修课程。先修课程按数组 prerequisites 给出，其中 prerequisites[i] = [ai, bi] ，表示如果要学习课程 ai 则必须先学习课程 bi。请你判断是否可能完成所有课程的学习？如果可以，返回 true；否则，返回 false。

**解决思路**：
1. **拓扑排序**：判断有向图是否存在环
2. **Kahn算法（BFS）**：计算每个节点的入度，从入度为0的节点开始
3. 如果最终访问的节点数等于课程数，则可以完成

**伪代码实现（Kahn算法）**：
```
function canFinish(numCourses, prerequisites):
    // 构建图和入度数组
    graph = new ArrayList[numCourses]
    inDegree = new int[numCourses]
    
    for each [course, pre] in prerequisites:
        graph[pre].add(course)
        inDegree[course]++
    
    // 将所有入度为0的课程加入队列
    queue = new LinkedList()
    for i from 0 to numCourses - 1:
        if inDegree[i] == 0:
            queue.offer(i)
    
    count = 0
    while not queue.isEmpty():
        course = queue.poll()
        count++
        
        for next in graph[course]:
            inDegree[next]--
            if inDegree[next] == 0:
                queue.offer(next)
    
    return count == numCourses
```

**Java代码实现（Kahn算法）**：
```java
class Solution {
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        // 构建图和入度数组
        List<Integer>[] graph = new ArrayList[numCourses];
        for (int i = 0; i < numCourses; i++) {
            graph[i] = new ArrayList<>();
        }
        
        int[] inDegree = new int[numCourses];
        for (int[] pre : prerequisites) {
            graph[pre[1]].add(pre[0]);
            inDegree[pre[0]]++;
        }
        
        // 将所有入度为0的课程加入队列
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < numCourses; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
            }
        }
        
        int count = 0;
        while (!queue.isEmpty()) {
            int course = queue.poll();
            count++;
            
            for (int next : graph[course]) {
                inDegree[next]--;
                if (inDegree[next] == 0) {
                    queue.offer(next);
                }
            }
        }
        
        return count == numCourses;
    }
}
```

---

### 15. 实现 Trie (前缀树)

**题目链接**：[208. 实现 Trie (前缀树)](https://leetcode.cn/problems/implement-trie-prefix-tree/)

**题目描述**：Trie（发音类似 "try"）或者说前缀树是一种树形数据结构，用于高效地存储和检索字符串数据集中的键。这一数据结构有相当多的应用情景，例如自动补完和拼写检查。请你实现 Trie 类：
- Trie() 初始化前缀树对象。
- void insert(String word) 向前缀树中插入字符串 word 。
- boolean search(String word) 如果字符串 word 在前缀树中，返回 true（即，在检索之前已经插入）；否则，返回 false 。
- boolean startsWith(String prefix) 如果之前已经插入的字符串 word 的前缀之一为 prefix ，返回 true ；否则，返回 false 。

**解决思路**：
1. **Trie树**：每个节点包含26个子节点（对应26个字母）和一个标记表示是否为单词结尾
2. **插入**：逐字符插入，不存在则创建新节点
3. **搜索**：逐字符查找，最后检查是否为单词结尾
4. **前缀搜索**：逐字符查找，只要能找到所有字符即可

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
    
    class TrieNode {
        TrieNode[] children;
        boolean isEnd;
        
        public TrieNode() {
            children = new TrieNode[26];
            isEnd = false;
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
1. **迭代**：使用三个指针（prev, current, next）
2. 每次将当前节点的 next 指向 prev
3. 逐步移动指针

**伪代码实现**：
```
function reverseList(head):
    prev = null
    current = head
    
    while current != null:
        next = current.next
        current.next = prev
        prev = current
        current = next
    
    return prev
```

**Java代码实现**：
```java
class Solution {
    public ListNode reverseList(ListNode head) {
        ListNode prev = null;
        ListNode current = head;
        
        while (current != null) {
            ListNode next = current.next;
            current.next = prev;
            prev = current;
            current = next;
        }
        
        return prev;
    }
}
```

---

### 17. 合并两个有序链表

**题目链接**：[21. 合并两个有序链表](https://leetcode.cn/problems/merge-two-sorted-lists/)

**题目描述**：将两个升序链表合并为一个新的 升序 链表并返回。新链表是通过拼接给定的两个链表的所有节点组成的。

**解决思路**：
1. **递归**：比较两个链表的头节点，选择较小的节点作为合并后的头节点
2. **迭代**：使用虚拟头节点，依次比较并拼接

**伪代码实现（递归）**：
```
function mergeTwoLists(l1, l2):
    if l1 == null: return l2
    if l2 == null: return l1
    
    if l1.val < l2.val:
        l1.next = mergeTwoLists(l1.next, l2)
        return l1
    else:
        l2.next = mergeTwoLists(l1, l2.next)
        return l2
```

**Java代码实现（递归）**：
```java
class Solution {
    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        if (l1 == null) return l2;
        if (l2 == null) return l1;
        
        if (l1.val < l2.val) {
            l1.next = mergeTwoLists(l1.next, l2);
            return l1;
        } else {
            l2.next = mergeTwoLists(l1, l2.next);
            return l2;
        }
    }
}
```

---

### 18. 相交链表

**题目链接**：[160. 相交链表](https://leetcode.cn/problems/intersection-of-two-linked-lists/)

**题目描述**：给你两个单链表的头节点 headA 和 headB，请你找出并返回两个单链表相交的起始节点。如果两个链表不存在相交节点，返回 null。

**解决思路**：
1. **双指针**：两个指针分别从两个链表头出发，当到达链表尾部时，切换到另一个链表头
2. 当两个指针相遇时，即为相交节点

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
class Solution {
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

### 19. 环形链表

**题目链接**：[141. 环形链表](https://leetcode.cn/problems/linked-list-cycle/)

**题目描述**：给你一个链表的头节点 head，判断链表中是否有环。如果链表中有某个节点，可以通过连续跟踪 next 指针再次到达，则链表中存在环。为了表示给定链表中的环，评测系统内部使用整数 pos 来表示链表尾连接到链表中的位置（索引从 0 开始）。注意：pos 不作为参数进行传递。仅仅是为了标识链表的实际情况。如果链表中存在环，则返回 true。 否则，返回 false。

**解决思路**：
1. **快慢指针**：使用两个指针，快指针每次移动两步，慢指针每次移动一步
2. 如果存在环，快慢指针最终会相遇

**伪代码实现**：
```
function hasCycle(head):
    if head == null or head.next == null: return false
    
    slow = head
    fast = head.next
    
    while slow != fast:
        if fast == null or fast.next == null: return false
        slow = slow.next
        fast = fast.next.next
    
    return true
```

**Java代码实现**：
```java
class Solution {
    public boolean hasCycle(ListNode head) {
        if (head == null || head.next == null) return false;
        
        ListNode slow = head;
        ListNode fast = head.next;
        
        while (slow != fast) {
            if (fast == null || fast.next == null) return false;
            slow = slow.next;
            fast = fast.next.next;
        }
        
        return true;
    }
}
```

---

### 20. 环形链表 II

**题目链接**：[142. 环形链表 II](https://leetcode.cn/problems/linked-list-cycle-ii/)

**题目描述**：给定一个链表的头节点 head，返回链表开始入环的第一个节点。 如果链表无环，则返回 null。

**解决思路**：
1. **快慢指针**：先找到快慢指针相遇点
2. 然后从相遇点和链表头同时出发，相遇处即为环的入口

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
class Solution {
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

### 21. 合并K个升序链表

**题目链接**：[23. 合并K个升序链表](https://leetcode.cn/problems/merge-k-sorted-lists/)

**题目描述**：给你一个链表数组，每个链表都已经按升序排列。请你将所有链表合并到一个升序链表中，返回合并后的链表。

**解决思路**：
1. **分治**：将链表数组分成两部分，分别合并后再合并
2. **优先队列**：使用小顶堆，每次取出最小的节点

**伪代码实现（优先队列）**：
```
function mergeKLists(lists):
    if lists == null or lists.length == 0: return null
    
    priorityQueue = new PriorityQueue((a, b) -> a.val - b.val)
    
    for list in lists:
        if list != null:
            priorityQueue.offer(list)
    
    dummy = new ListNode(0)
    current = dummy
    
    while not priorityQueue.isEmpty():
        node = priorityQueue.poll()
        current.next = node
        current = current.next
        
        if node.next != null:
            priorityQueue.offer(node.next)
    
    return dummy.next
```

**Java代码实现（优先队列）**：
```java
class Solution {
    public ListNode mergeKLists(ListNode[] lists) {
        if (lists == null || lists.length == 0) return null;
        
        PriorityQueue<ListNode> priorityQueue = new PriorityQueue<>((a, b) -> a.val - b.val);
        
        for (ListNode list : lists) {
            if (list != null) {
                priorityQueue.offer(list);
            }
        }
        
        ListNode dummy = new ListNode(0);
        ListNode current = dummy;
        
        while (!priorityQueue.isEmpty()) {
            ListNode node = priorityQueue.poll();
            current.next = node;
            current = current.next;
            
            if (node.next != null) {
                priorityQueue.offer(node.next);
            }
        }
        
        return dummy.next;
    }
}
```

---

### 22. 两两交换链表中的节点

**题目链接**：[24. 两两交换链表中的节点](https://leetcode.cn/problems/swap-nodes-in-pairs/)

**题目描述**：给你一个链表，两两交换其中相邻的节点，并返回交换后链表的头节点。你必须在不修改节点内部值的情况下完成本题（即，只能进行节点交换）。

**解决思路**：
1. **递归**：交换前两个节点，然后递归处理剩余部分
2. **迭代**：使用虚拟头节点，依次交换相邻节点

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

### 23. K 个一组翻转链表

**题目链接**：[25. K 个一组翻转链表](https://leetcode.cn/problems/reverse-nodes-in-k-group/)

**题目描述**：给你链表的头节点 head，每 k 个节点一组进行翻转，请你返回修改后的链表。k 是一个正整数，它的值小于或等于链表的长度。如果节点总数不是 k 的整数倍，那么请将最后剩余的节点保持原有顺序。

**解决思路**：
1. **迭代**：使用虚拟头节点，每次处理 k 个节点
2. 先检查是否有 k 个节点，然后翻转这 k 个节点
3. 连接翻转后的部分

**伪代码实现**：
```
function reverseKGroup(head, k):
    dummy = new ListNode(0)
    dummy.next = head
    prev = dummy
    
    while head != null:
        tail = prev
        
        // 检查是否有 k 个节点
        for i from 0 to k - 1:
            tail = tail.next
            if tail == null:
                return dummy.next
        
        next = tail.next
        // 翻转 k 个节点
        head, tail = reverse(head, tail)
        
        // 连接
        prev.next = head
        tail.next = next
        prev = tail
        head = next
    
    return dummy.next

function reverse(head, tail):
    prev = tail.next
    current = head
    
    while prev != tail:
        next = current.next
        current.next = prev
        prev = current
        current = next
    
    return tail, head
```

**Java代码实现**：
```java
class Solution {
    public ListNode reverseKGroup(ListNode head, int k) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode prev = dummy;
        
        while (head != null) {
            ListNode tail = prev;
            
            // 检查是否有 k 个节点
            for (int i = 0; i < k; i++) {
                tail = tail.next;
                if (tail == null) {
                    return dummy.next;
                }
            }
            
            ListNode next = tail.next;
            // 翻转 k 个节点
            ListNode[] reversed = reverse(head, tail);
            head = reversed[0];
            tail = reversed[1];
            
            // 连接
            prev.next = head;
            tail.next = next;
            prev = tail;
            head = next;
        }
        
        return dummy.next;
    }
    
    private ListNode[] reverse(ListNode head, ListNode tail) {
        ListNode prev = tail.next;
        ListNode current = head;
        
        while (prev != tail) {
            ListNode next = current.next;
            current.next = prev;
            prev = current;
            current = next;
        }
        
        return new ListNode[]{tail, head};
    }
}
```

---

### 24. 删除链表的倒数第 N 个结点

**题目链接**：[19. 删除链表的倒数第 N 个结点](https://leetcode.cn/problems/remove-nth-node-from-end-of-list/)

**题目描述**：给你一个链表，删除链表的倒数第 n 个结点，并且返回链表的头结点。

**解决思路**：
1. **快慢指针**：快指针先移动 n 步，然后快慢指针同时移动
2. 当快指针到达链表末尾时，慢指针指向的就是倒数第 n+1 个节点
3. 删除慢指针的下一个节点

**伪代码实现**：
```
function removeNthFromEnd(head, n):
    dummy = new ListNode(0)
    dummy.next = head
    fast = dummy
    slow = dummy
    
    // 快指针先移动 n+1 步
    for i from 0 to n:
        fast = fast.next
    
    // 快慢指针同时移动
    while fast != null:
        fast = fast.next
        slow = slow.next
    
    // 删除节点
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
        
        // 快指针先移动 n+1 步
        for (int i = 0; i <= n; i++) {
            fast = fast.next;
        }
        
        // 快慢指针同时移动
        while (fast != null) {
            fast = fast.next;
            slow = slow.next;
        }
        
        // 删除节点
        slow.next = slow.next.next;
        
        return dummy.next;
    }
}
```

---

### 25. 旋转链表

**题目链接**：[61. 旋转链表](https://leetcode.cn/problems/rotate-list/)

**题目描述**：给你一个链表的头节点 head，旋转链表，将链表每个节点向右移动 k 个位置。

**解决思路**：
1. 计算链表长度
2. 将链表闭合为环
3. 找到新的头节点和尾节点
4. 断开环

**伪代码实现**：
```
function rotateRight(head, k):
    if head == null or head.next == null or k == 0: return head
    
    // 计算长度
    length = 1
    tail = head
    while tail.next != null:
        tail = tail.next
        length++
    
    // 闭合为环
    tail.next = head
    
    // 找到新的头节点位置
    steps = length - k % length
    for i from 0 to steps - 1:
        tail = tail.next
    
    // 断开环
    head = tail.next
    tail.next = null
    
    return head
```

**Java代码实现**：
```java
class Solution {
    public ListNode rotateRight(ListNode head, int k) {
        if (head == null || head.next == null || k == 0) return head;
        
        // 计算长度
        int length = 1;
        ListNode tail = head;
        while (tail.next != null) {
            tail = tail.next;
            length++;
        }
        
        // 闭合为环
        tail.next = head;
        
        // 找到新的头节点位置
        int steps = length - k % length;
        for (int i = 0; i < steps; i++) {
            tail = tail.next;
        }
        
        // 断开环
        head = tail.next;
        tail.next = null;
        
        return head;
    }
}
```

---

### 26. 排序链表

**题目链接**：[148. 排序链表](https://leetcode.cn/problems/sort-list/)

**题目描述**：给你链表的头结点 head，请将其按升序排列并返回排序后的链表。

**解决思路**：
1. **归并排序**：找到链表中点，递归排序左右两部分，然后合并
2. **找到中点**：使用快慢指针
3. **合并**：合并两个有序链表

**伪代码实现**：
```
function sortList(head):
    if head == null or head.next == null: return head
    
    // 找到中点
    mid = findMiddle(head)
    next = mid.next
    mid.next = null
    
    // 递归排序左右两部分
    left = sortList(head)
    right = sortList(next)
    
    // 合并
    return merge(left, right)

function findMiddle(head):
    slow = head
    fast = head.next
    
    while fast != null and fast.next != null:
        slow = slow.next
        fast = fast.next.next
    
    return slow

function merge(l1, l2):
    dummy = new ListNode(0)
    current = dummy
    
    while l1 != null and l2 != null:
        if l1.val < l2.val:
            current.next = l1
            l1 = l1.next
        else:
            current.next = l2
            l2 = l2.next
        current = current.next
    
    current.next = l1 != null ? l1 : l2
    return dummy.next
```

**Java代码实现**：
```java
class Solution {
    public ListNode sortList(ListNode head) {
        if (head == null || head.next == null) return head;
        
        // 找到中点
        ListNode mid = findMiddle(head);
        ListNode next = mid.next;
        mid.next = null;
        
        // 递归排序左右两部分
        ListNode left = sortList(head);
        ListNode right = sortList(next);
        
        // 合并
        return merge(left, right);
    }
    
    private ListNode findMiddle(ListNode head) {
        ListNode slow = head;
        ListNode fast = head.next;
        
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        
        return slow;
    }
    
    private ListNode merge(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(0);
        ListNode current = dummy;
        
        while (l1 != null && l2 != null) {
            if (l1.val < l2.val) {
                current.next = l1;
                l1 = l1.next;
            } else {
                current.next = l2;
                l2 = l2.next;
            }
            current = current.next;
        }
        
        current.next = l1 != null ? l1 : l2;
        return dummy.next;
    }
}
```

---

### 27. 复制带随机指针的链表

**题目链接**：[138. 复制带随机指针的链表](https://leetcode.cn/problems/copy-list-with-random-pointer/)

**题目描述**：给你一个长度为 n 的链表，每个节点包含一个额外增加的随机指针 random，该指针可以指向链表中的任何节点或空节点。构造这个链表的 深拷贝。 深拷贝应该正好由 n 个 全新 节点组成，其中每个新节点的值都设为其对应的原节点的值。新节点的 next 指针和 random 指针也都应指向复制链表中的新节点，并使原链表和复制链表中的这些指针能够表示相同的链表状态。复制链表中的指针都不应指向原链表中的节点 。

**解决思路**：
1. **哈希表**：使用哈希表存储原节点到新节点的映射
2. 第一遍创建新节点并建立映射
3. 第二遍设置新节点的 next 和 random 指针

**伪代码实现**：
```
function copyRandomList(head):
    if head == null: return null
    
    map = new HashMap()
    current = head
    
    // 第一遍：创建新节点
    while current != null:
        map.put(current, new Node(current.val))
        current = current.next
    
    // 第二遍：设置指针
    current = head
    while current != null:
        map.get(current).next = map.get(current.next)
        map.get(current).random = map.get(current.random