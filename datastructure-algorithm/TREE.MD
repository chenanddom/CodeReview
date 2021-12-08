# 树
```text
树是我们计算机中非常重要的数据结构，同时使用树这种数据结构，可以描述实现显示生活中很多事务，例如家谱
单位的组织架构，等等，
树是由n(n>=1)个有限结点组成一个具有层次关系的集合，把他们叫做"树"是因为它看起来像以可倒挂的树，也就是说
它是跟朝上的，而叶子朝下的。

* 树具有的特点:
1. 每个节点由零个或者多个子结点;
2. 没有父结点的结点为根结点;
3. 每个非根结点只有一个父结点;
4.每一个结点极其后代整体上可以看作是一棵树，称为前结点的父结点的一个子树;

```
## 树的相关术语

* 结点的度
一个结点含有的子树的个数称为该结点的度;

* 叶节点
度为0的结点称为叶结点，也可以叫做终端结点;

* 分支结点
度不为零的结点称为分支结点，也可以叫做非终端结点

* 结点的层次
从根结点开始，根系结点的层次为1，根的直接后继层次为2，以此类推;

* 结点的层次编号
树中的结点按照从上倒下，同层次的从左到右的次序拍成一个线性的序列，把他们编成连续的自然数;

* 树的度:
树中的所有结点的度的最大值

* 树的高度
树中结点的最大层次

* 森林
m(m>=0)个互不相交的树的集合，将一颗非空的树的根结点删去，树就变成了一片森林;给森林增加一个统一
的根结点，森林就变成了一棵树。

* 孩子结点
一个结点的直接后继结点称为改结点的孩子结点

* 双亲结点
一个结点的直接前驱结点称为该结点的双亲结点

* 兄弟结点:
同一双亲结点的孩子结点间互称为兄弟结点

## 二叉树
```text
二叉树就是度不超过2的树(每个结点最多有两个子结点)
```

* 满二叉树
```text
一个二叉树，如果每一个层的结点树都达到最大值，则这个二叉树就是满二叉树。
```

* 完全二叉树
```text
叶子结点只能出现再最下层和次下层，并且最下面的一层的叶子结点都集中在该层嘴左边若干位置的二叉树
```

* 二叉树put的方法思想
1. 如果当前树中没有任何一个结点，则直接把新结点当作根节点使用。
2. 如果当前树不为空则从根节点开始
    2.1. 如果新结点的key小于当前结点的key，则继续找当前结点的左子结点
    2.2. 如果新结点的key大于当前结点的key，则继续找当前结点的右子结点
    2.3. 如果新结点的key等于当前结点的key，则树中已经存在这样的结点，替换该结点的value值即可.
详细的代码如下：     
```java
  public void put(K key, V value) {
        this.root = put(root, key, value);
    }

    private Node put(Node node, K k, V v) {
        if (node == null) {
            this.N++;
            node = new Node(k, v, null, null);
            return node;
        }
        int cmp = k.compareTo(node.k);
        if (cmp > 0) {
            node.right = put(node.right, k, v);
        } else if (cmp < 0) {
            node.left = put(node.left, k, v);
        } else {
            node.v = v;
        }
        return node;
    }
```

* 查找结点get()方法的实现:
从根节点开始
1. 如果查找的key小于当前结点的key,则继续查找当前结点的左子结点。
2. 如果查找的key大于当前结点的key，则继续找当前结点的右子结点.
3. 如果要查询的key等于当前结点的key,则树中返回当前结点的va
详细的代码实现如下:
```java
 public V get(K key) {
        return get(root, key);
    }

    private V get(Node node, K key) {
        if (node == null) {
            return null;
        }
        int cmp = key.compareTo(node.k);
        if (cmp > 0) {
            return get(node.right, key);
        } else if (cmp < 0) {
            return get(node.left, key);
        } else {
            return node.v;
        }
    }
```
* 删除delete实现的思想
1. 找到要杉树的结点。
2. 找到要删除的结点的右子树涨的最小结点minNode(遍历右子树的左子结点就是它的最小值)
3. 删除右子树中的最小结点
4. 让被删除的结点的左子树成为最小结点minNode的左子树，让被删除结点的右子树成为最小结点minNode的右子树.
5. 让被删除结点的父结点指向最小结点minNode。
代码实现如下:
```java
  public void delete(K key) {
        this.root = delete(root, key);
    }

    private Node delete(Node node, K key) {
        if (node == null) {
            return null;
        }
        int cmp = key.compareTo(node.k);
        if (cmp > 0) {
            node.right = delete(node.right, key);
        } else if (cmp < 0) {
            node.left = delete(node.left, key);
        } else {
            this.N--;
            //如果当前结点没有右子树
            if (node.right == null) {
                return node.left;
            }
            //如果当前结点没有左子树
            if (node.left == null) {
                return node.right;
            }
            //如果左右子树都存在
            Node minNode = node.right;
            // 找到右子树中最小的结点
            while (minNode.left != null) {
                minNode = minNode.left;
            }
            //删除（右子树中的）左子树最小的结点
            Node rightNode = node.right;
            while (rightNode.left != null) {
                if (rightNode.left.left == null) {
                    rightNode.left = null;
                } else {
                    rightNode = rightNode.left;
                }
            }
            //让删除结点的左子树称为最小结点的左子树，让删除结点的右子树称为minNode的右子树
            minNode.right = node.right;
            minNode.left = node.left;
            //让被删除结点的父结点指向的最小的结点
            node = minNode;
        }
        return node;
    }

```

### 二叉树的遍历
* 前序遍历
1. 把当前结点的key放入队列当中。
2. 找到当前结点的左子树，递归遍历左子树
3. 找到当前结点的右子树，如果不为空，递归遍历右子树

代码实现如下：
```java
 //前序遍历
    public Queue<K> preErgodic() {
        Queue<K> keys = new Queue<>();
        preErgodic(this.root, keys);
        return keys;
    }

    public void preErgodic(Node node, Queue<K> keys) {
        if (node==null){
            return;
        }
        keys.enqueue(node.k);
        if (node.left!=null){
            preErgodic(node.left,keys);
        }
        if (node.right!=null){
            preErgodic(node.right,keys);
        }
    }
```

* 中序遍历
1. 先吧当前的结点的左子树，如果不为空，递归遍历左子树
2. 把当前结点的key放入到队列当中
3. 找到当前结点的右子树，如果不为空，递归遍历右子树

代码实现如下:
```java
    public Queue<K> midErgodic() {
        Queue<K> keys = new Queue<>();
        midErgodic(this.root, keys);
        return keys;
    }

    public void midErgodic(Node node, Queue<K> keys) {
        if (node==null){
            return;
        }
        if (node.left!=null){
            midErgodic(node.left,keys);
        }
        keys.enqueue(node.k);
        if (node.right!=null){
            midErgodic(node.right,keys);
        }
    }
```

* 后序遍历
1. 找到当前结点的左子树，如果不为空，递归遍历左子树
2．找到当前结点的右子树，如果不为空，递归遍历右子树  
3. 将当前结点的key添加到队列里面
代码实现如下：
```java
 public Queue<K> lastErgodic(){
        Queue<K> keys = new Queue<>();
        lastErgodic(this.root,keys);
        return keys;
    }
    public void lastErgodic(Node node,Queue<K> keys){
        if (node==null){
            return;
        }
        if (node.left!=null){
            lastErgodic(node.left,keys);
        }
        if (node.right!=null){
            lastErgodic(node.right,keys);
        }
        keys.enqueue(node.k);
    }
```
* 二叉树层的遍历
1.创建队列，存储每一层的结点；
2.使用循环从队列中弹出一个结点：
2.1获取当前结点的key；
2.2如果当前结点的左子结点不为空，则把左子结点放入到队列中
2.3如果当前结点的右子结点不为空，则把右子结点放入到队列中
![分层遍历的图解](./files\layerErgodic.png)
代码实现如下:
```java
public Queue<K> layerErgodic(){
        Queue<K> keys = new Queue<>();
        Queue<Node> nodes = new Queue<>();
         nodes.enqueue(this.root);
         while (!nodes.isEmpty()){
             Node node = nodes.dequeue();
             keys.enqueue(node.k);
             if (node.left!=null){
                 nodes.enqueue(node.left);
             }
             if (node.right!=null){
                 nodes.enqueue(node.right);
             }
         }
         return keys;
    }
```


* 折纸问题
```java
需求：
请把一段纸条竖着放在桌子上，然后从纸条的下边向上方对折1次，压出折痕后展开。此时 折痕是凹下去的，即折
痕突起的方向指向纸条的背面。如果从纸条的下边向上方连续对折2 次，压出折痕后展开，此时有三条折痕，从上
到下依次是下折痕、下折痕和上折痕。
给定一 个输入参数N，代表纸条都从下边向上方连续对折N次，请从上到下打印所有折痕的方向 例如：N=1时，打
印： down；N=2时，打印： down down up
```
![折纸问题](./files\page_fold.PNG)

**分析**
```text
由上图可知，第一次对折，让粉色朝下，此时产生的的折痕是根结点；第二次对折产生的是下折痕和上折痕，其中我们可以将第二次产生的上折痕
看作是右子结点，下折痕迹是左子结点，经过分析我们可以使用树的数据结构来进行表示。
这棵树具有一下的特点:
1. 根结点为下折痕
2. 每个结点的左子结点为下折痕
3. 每个结点的右子结点为上折痕
```
![树结构](./files\pagefold_tree_structure_characteristic.PNG)

* 实现步骤
1. 定义结点类
2. 构建深度为N的折痕
3. 使用中序遍历，打印处树中所有的结点内容.
* 构建深度为N的折痕的树:
1. 第一次i者恒，只有一条者恒没创建根结点
2. 如果不是第一次对折，则使用队列保存根结点
3. 循环遍历队列
3.1. 从队列中拿出一次结点
3.2. 如果这个结点的左子结点不为空，则把这个左子系欸但添加到队列中;
3.3 如果这个结点的右子结点不为空，则把这个右子结点添加到队列中
3.4 判断当前系欸但的左子系欸但那和右子结点都不为空，如果是，则需要为当前结点创建一个值为down的左子结点，一个值为up的右子结点

[下一节](./HEAD.MD)
























