package com.itdom.linear;

/**
 * 括号匹配问题
 */
public class BrackersMatch {
    public static void main(String[] args) {
        String str = "(dadasd(adasdsa()))";
        System.out.println(isMatch(str));
    }

    public static boolean isMatch(String s) {
        Stack<String> stack = new Stack<>();
        for (int i = 0; i < s.length(); i++) {

            String c = s.charAt(i) + "";
            //如果遍历到的括号是左括号就将括号加入栈中
            if (c.equals("(")) {
                stack.push(c);
            } else if (c.equals(")")) {
                //如果栈弹出的左括号为null就说明不匹配
                String s1 = stack.pop();
                if (s1 == null) {
                    return false;
                }
            }
        }
        //遍历完了之后如果栈内没有元素就是为匹配，如果有元素就是不匹配.
        return stack.size() == 0 ? true : false;
    }
}
