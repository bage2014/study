package com.bage.study.algorithm.tree;

public class Solution {
    public static void main(String[] args){

        Solution solution = new Solution();

        TreeNode s = new TreeNode(3);
        s.left = new TreeNode(4);
        s.right = new TreeNode(5);

        s.left.left = new TreeNode(1);

        s.right.left = new TreeNode(2);

        TreeNode t = new TreeNode(3);

        t.left = new TreeNode(1);

        t.right = new TreeNode(2);

        System.out.println(solution.isSubtree(s, t));

    }


    /**
     * 给定两个非空二叉树 s 和 t，检验 s 中是否包含和 t 具有相同结构和节点值的子树。s 的一个子树包括 s 的一个节点和这个节点的所有子孙。s 也可以看做它自身的一棵子树。
     * @param s
     * @param t
     * @return
     */
    public boolean isSubtree(TreeNode s, TreeNode t) {

        //方法一 暴力解法，判断s的每个子树是不是和t相等

        //两者相等，直接返回true
        if(s == t){
            return true;
        }

        //有一个为null，返回false
        if(s == null || t == null){
            return false;
        }

        //如果当前值相等，则判断两树是否相同，如果不等，则判断子树是否包含当前树
        if(s.val == t.val){
            return (treeEquals(s.left, t.left) && treeEquals(s.right, t.right)) || isSubtree(s.left,t) || isSubtree(s.right,t);
        }else{
            return isSubtree(s.left,t) || isSubtree(s.right,t);
        }

        //方法二 树哈希，标准答案都没看懂- -#

    }

    private boolean treeEquals(TreeNode s, TreeNode t){
        if(s == t){
            return true;
        }

        if(s == null || t == null){
            return false;
        }

        return s.val == t.val && treeEquals(s.left, t.left) && treeEquals(s.right, t.right);

    }

}
