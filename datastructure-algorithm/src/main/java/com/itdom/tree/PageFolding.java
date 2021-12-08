package com.itdom.tree;

import com.itdom.linear.Queue;

/**
 * 折纸问题
 * 1.定义结点类
 * 2.构建深度为N的折痕树；
 * 3.使用中序遍历，打印出树中所有结点的内容；
 * 构建深度为N的折痕树：
 * 1.第一次对折，只有一条折痕，创建根结点；
 * 2.如果不是第一次对折，则使用队列保存根结点；
 * 3.循环遍历队列：
 * 3.1从队列中拿出一个结点；
 * 3.2如果这个结点的左子结点不为空，则把这个左子结点添加到队列中；
 * 3.3如果这个结点的右子结点不为空，则把这个右子结点添加到队列中；
 * 3.4判断当前结点的左子结点和右子结点都不为空，如果是，则需要为当前结点创建一个值为down的左子结点，一
 * 个值为up的右子结点。
 */
public class PageFolding {


    //3.使用中序遍历，打印出树中所有结点的内容；
    private static void printTree(Node tree) {
        if (tree == null) {
            return;
        }
        printTree(tree.left);
        System.out.println(tree.item);
        printTree(tree.right);
    }


    //2.构建深度为N的折痕树；
    private static Node createTree(int N) {
        Node root = null;
        for (int i = 0; i < N; i++) {
            if (i == 0) {
                //第一次对折只有一条折痕，创建的是一个根结点
                root = new Node("down", null, null);
            } else {
                //2.如果不是第一次对折，则使用队列保存根结点；
                Queue<Node> queue = new Queue<>();
                queue.enqueue(root);
                //3.循环遍历队列：
                while (!queue.isEmpty()) {
                    //3.1从队列中拿出一个结点；
                    Node tmpNode = queue.dequeue();
                    //3.2如果这个结点的左子结点不为空，则把这个左子结点添加到队列中；
                    if (tmpNode.left != null) {
                        queue.enqueue(tmpNode.left);
                    }
                    //3.3如果这个结点的右子结点不为空，则把这个右子结点添加到队列中；
                    if (tmpNode.right != null) {
                        queue.enqueue(tmpNode.right);
                    }
                    //3.4判断当前结点的左子结点和右子结点都不为空，如果是，则需要为当前结点创建一个
                    //值为down的左子结点，一个值为up的右子结点。
                    if (tmpNode.left == null && tmpNode.right == null) {
                        tmpNode.left = new Node("down", null, null);
                        tmpNode.right = new Node("up", null, null);
                    }
                }
            }
        }
        return root;
    }


    private static class Node {
        //存储结点元素
        String item;
        //左子结点
        Node left;
        //右子节点
        Node right;

        public Node(String item, Node left, Node right) {
            this.item = item;
            this.left = left;
            this.right = right;
        }

    }


    public static void main(String[] args) {
        printTree(createTree(2));

    }
}
