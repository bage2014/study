package com.bage.study.algorithm.tree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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


    /**
     * 分层遍历二叉树
     * @param root
     */
    public void printNodeByLevel(TreeNode root){
        //方法一 从根节点开始，打印二叉树的每一层，缺点：每次都是从根节点开始遍历
        for(int i=0;;++i){
            if(printNodeAtLevel(root,i) == 0){
                break;
            }
            System.out.println();
        }

        //方法二 广度优先的算法
        List<TreeNode> list = new ArrayList<TreeNode>();  //用于按层存放每个节点
        list.add(root);
        int cur = 0, last;    //cur代表已经遍历的节点，last代表当前层的最后一个节点

        while (cur < list.size()){
            last = list.size();  //每次都要重新初始化last

            //打印某一层
            while (cur < last){

                TreeNode node = list.get(cur);

                System.out.print(node.val);
                System.out.print(' ');

                if(node.left!=null){
                    list.add(node.left);
                }

                if(node.right!=null){
                    list.add(node.right);
                }

                cur++;

            }

            System.out.println();
        }
    }

    /**
     * 打印二叉树的某一层
     * @param root
     * @param level
     */
    private int printNodeAtLevel(TreeNode root, int level){
        if(root == null){
            return 0;
        }

        if(level == 0){
            System.out.print(root.val);
            System.out.print(' ');
            return 1;
        }else{
            //打印二叉树的第n层，相当于打印两个孩子节点的第n-1层，这样就可以递归
            return printNodeAtLevel(root.left, level-1)+printNodeAtLevel(root.right, level-1);
        }
    }

}
