package com.bdp.demo.datastructure;

import java.util.*;

/**
 * 堆栈
 */
public class StockTest {
    //我们来看一个基于用 栈 来完成的 算法题（来源leetcode）：
    //算法题：给定一个只包括 '('，')'，'{'，'}'，'['，']' 的字符串，判断字符串是否有效。
    //有效字符串需满足：左括号必须用相同类型的右括号闭合。左括号必须以正确的顺序闭合。
    //举例：字符串 "()"有效、"()[]{}"有效、"(]"无效、"([)]"无效、"{[]}"有效。
    //解题思路：
    //使用1个堆栈即可解决，依次遍历这个字符串，如果遇到是左括号就入栈到堆栈中，如果遇到的是右括号，则从堆栈中取出栈顶的第一个左括号，
    //比对一下这个左括号和当前遇到的右括号是否匹配，如果不匹配就认为这整个字符串无效。如果能匹配，则OK，删除这个左括号和右括号，
    //继续往后走，继续遍历字符串中剩下的字符，只要遇到左括号就入栈，只要遇到右括号就与将栈顶的左括号出栈与之比较。
    //一直走到字符串结束，再来检查堆栈中是否还有元素，如果还有元素，则这个字符串同样无效，如果堆栈为空，则字符串有效。
    //就以这个思路实现一个初版代码：
    public boolean isValid(String s) {
        Deque deque = new Deque() {
            @Override
            public void addFirst(Object o) {

            }

            @Override
            public void addLast(Object o) {

            }

            @Override
            public boolean offerFirst(Object o) {
                return false;
            }

            @Override
            public boolean offerLast(Object o) {
                return false;
            }

            @Override
            public Object removeFirst() {
                return null;
            }

            @Override
            public Object removeLast() {
                return null;
            }

            @Override
            public Object pollFirst() {
                return null;
            }

            @Override
            public Object pollLast() {
                return null;
            }

            @Override
            public Object getFirst() {
                return null;
            }

            @Override
            public Object getLast() {
                return null;
            }

            @Override
            public Object peekFirst() {
                return null;
            }

            @Override
            public Object peekLast() {
                return null;
            }

            @Override
            public boolean removeFirstOccurrence(Object o) {
                return false;
            }

            @Override
            public boolean removeLastOccurrence(Object o) {
                return false;
            }

            @Override
            public boolean add(Object o) {
                return false;
            }

            @Override
            public boolean offer(Object o) {
                return false;
            }

            @Override
            public Object remove() {
                return null;
            }

            @Override
            public Object poll() {
                return null;
            }

            @Override
            public Object element() {
                return null;
            }

            @Override
            public Object peek() {
                return null;
            }

            @Override
            public void push(Object o) {

            }

            @Override
            public Object pop() {
                return null;
            }

            @Override
            public boolean remove(Object o) {
                return false;
            }

            @Override
            public boolean contains(Object o) {
                return false;
            }

            @Override
            public int size() {
                return 0;
            }

            @Override
            public Iterator iterator() {
                return null;
            }

            @Override
            public Iterator descendingIterator() {
                return null;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public Object[] toArray() {
                return new Object[0];
            }

            @Override
            public Object[] toArray(Object[] a) {
                return new Object[0];
            }

            @Override
            public boolean containsAll(Collection c) {
                return false;
            }

            @Override
            public boolean addAll(Collection c) {
                return false;
            }

            @Override
            public boolean removeAll(Collection c) {
                return false;
            }

            @Override
            public boolean retainAll(Collection c) {
                return false;
            }

            @Override
            public void clear() {

            }
        };// TODO: 2020/8/6 学习

        Stack<Character> satck = new Stack<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '(' || c == '{' || c == '[') {
                satck.push(c);
            } else {
                if (satck.isEmpty()) return false;
                char temp = satck.pop();
                if ((temp == '(' && c == ')') || (temp == '{' && c == '}') || (temp == '[' && c == ']')) {
                    continue;
                } else {
                    return false;
                }
            }
        }
        return satck.isEmpty();
    }

    //这个代码的时间复杂度o(n),空间复杂度o(n)搞定。
    //但是想了想，好像代码不是很优雅，写了一个优化版，提前将左右括号放入到MAP中，这个方法的时间和空间复杂度跟上面的一样。
    public boolean isValid2(String s) {
        Stack<Character> stack = new Stack<>();
        HashMap<Character, Character> map = new HashMap<>();
        map.put('(', ')');
        map.put('{', '}');
        map.put('[', ']');
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (map.containsKey(c)) {
                stack.push(c);
            } else {
                if (stack.isEmpty()) return false;
                char temp = stack.pop();
                if (map.get(temp) != c) return false;
            }
        }
        return stack.isEmpty();
    }

    //继续思考有没有更简洁的方法，竟然在leetcode上找到了一个：
    //但是这个方法并没有用到堆栈哦，它的思路是不断的遍历这个字符串，将字符串中的(){}[]全部调换成空字符串，
    //如果最后全部替换完成了，并且字符串为空了，就说明字符串是有效的，否者就是无效的字符串。
    public boolean isValid3(String s) {
        int length = s.length();
        do {
            length = s.length();
            s = s.replaceAll("\\(\\)", "").replaceAll("\\{\\}", "").replaceAll("\\[\\]", "");
        } while (s.length() != length);
        return s.length() == 0;
    }

    public static void main(String[] args) {
        String s="(){}[]";
        boolean valid3 = new StockTest().isValid3(s);
    }

}
