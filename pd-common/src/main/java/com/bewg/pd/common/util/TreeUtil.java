package com.bewg.pd.common.util;

import java.util.*;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;

import com.bewg.pd.common.util.model.TreeNode;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TreeUtil {

    /**
     * 使用递归方法建树(包含根节点)
     * 
     * @param treeNodes
     * @return
     */
    public <T extends TreeNode> T buildByRecursiveWithRoot(List<T> treeNodes, @NotNull Object root) {
        List<T> trees = new ArrayList<T>();
        // 根节点
        T head = null;
        for (T treeNode : treeNodes) {
            if (root.equals(treeNode.getId())) {
                head = treeNode;
            }
            if (root.equals(treeNode.getParentId())) {
                trees.add(findChildren(treeNode, treeNodes));
            }
        }
        if (head != null) {
            if (trees.size() != 0) {
                Iterator<T> iterator = trees.iterator();
                while (iterator.hasNext()) {
                    head.add(iterator.next());
                    iterator.remove();
                }
                trees.add(head);
            } else {
                trees.add(head);
            }
        }
        return head;
    }

    /**
     * 使用递归方法建树(不包含根节点)
     * 
     * @param treeNodes
     * @return
     */
    public <T extends TreeNode> List<T> buildByRecursiveWithoutRoot(List<T> treeNodes, String root) {
        List<T> trees = new ArrayList<T>();
        for (T treeNode : treeNodes) {
            if (root == null) {
                if (StringUtils.isBlank(treeNode.getParentId())) {
                    trees.add(findChildren(treeNode, treeNodes));
                }
            } else {
                if (root.equals(treeNode.getParentId())) {
                    trees.add(findChildren(treeNode, treeNodes));
                }
            }
        }
        return trees;
    }

    /**
     * 递归查找子节点
     * 
     * @param treeNodes
     * @return
     */
    public <T extends TreeNode> T findChildren(T treeNode, List<T> treeNodes) {
        for (T it : treeNodes) {
            if (treeNode.getId().equals(it.getParentId())) {
                if (treeNode.getChildren() == null) {
                    treeNode.setChildren(new ArrayList<>());
                }
                treeNode.add(findChildren(it, treeNodes));
            }
        }
        return treeNode;
    }

    /**
     * 深度遍历树
     * 
     * @param tree
     * @param rootId
     * @return
     */
    public <T extends TreeNode> List<String> dfsNotRecursive(T tree, String rootId) {
        List<String> list = new ArrayList<>();
        T rootNode = findTreeNode(tree, rootId);
        if (rootNode != null) {
            Stack<T> stack = new Stack<>();
            stack.push(rootNode);
            while (!stack.isEmpty()) {
                T node = stack.pop();
                list.add(node.getId());
                if (node.getChildren() != null && !node.getChildren().isEmpty()) {
                    for (int i = node.getChildren().size() - 1; i >= 0; i--) {
                        stack.push((T)node.getChildren().get(i));
                    }
                }
            }
        }
        return list;
    }

    /**
     * 层次遍历查找对应的节点
     * 
     * @param tree
     * @param rootId
     * @return
     */
    public <T extends TreeNode> T findTreeNode(T tree, String rootId) {
        T rootNode = null;
        if (tree != null) {
            if (StringUtils.isBlank(rootId) || tree.getId().equals(rootId)) {
                rootNode = tree;
                return rootNode;
            }
            Queue<T> queue = new ArrayDeque<>();
            queue.offer(tree);
            while (!queue.isEmpty()) {
                T node = queue.poll();
                if (tree.getId().equals(rootId)) {
                    rootNode = node;
                    return rootNode;
                }
                if (node.getChildren() != null && !node.getChildren().isEmpty()) {
                    for (int i = node.getChildren().size() - 1; i >= 0; i--) {
                        queue.offer((T)node.getChildren().get(i));
                    }
                }
            }
        }
        return null;
    }

}
