public class LinkedListDeque<T> {
    private class Node {
        T item;
        Node prev;
        Node next;

        // 构造函数不能写返回值类型
        public Node(T i, Node p, Node n) {
            this.item = i;
            this.prev = p;
            this.next = n;
        }
    }

    private Node sentinel;
    private int size;

    /*
    public LinkedListDeque(T t) {
        sentinel.next = new Node(t, null, null);
        sentinel.next.prev = sentinel;
        sentinel.next.next = sentinel;
        sentinel.prev = sentinel.next;
        size += 1;
    }
    */

    // 构造函数不能写返回值类型
    public LinkedListDeque() {
        sentinel = new Node(null, null, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        size = 0;
    }

    public void addFirst(T item) {
        Node p = new Node(item, null, null);
        p.next = sentinel.next;
        sentinel.next.prev = p;
        p.prev = sentinel;
        sentinel.next = p;
        size += 1;
    }

    public void addLast(T item) {
        Node p = new Node(item, null, null);
        p.prev = sentinel.prev;
        p.next = sentinel;
        sentinel.prev.next = p;
        sentinel.prev = p;
        size += 1;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        if (size == 0) {
            System.out.print("The deque is empty!");
        } else {
            Node p = sentinel.next;
            while (p != sentinel) {
                System.out.print(p.item + " ");
                p = p.next;
            }
        }
        System.out.println();
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        T returnItem = sentinel.next.item;
        sentinel.next.next.prev = sentinel;
        sentinel.next = sentinel.next.next;
        size -= 1;
        return returnItem;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        T returnItem = sentinel.prev.item;
        sentinel.prev.prev.next = sentinel;
        sentinel.prev = sentinel.prev.prev;
        size -= 1;
        return returnItem;
    }

    public T get(int index) {
        if (index >= size || index < 0) {
            return null;
        }

        Node p = sentinel;
        for (int i = 0; i <= index; i++) {
            p = p.next;
        }
        return p.item;
    }

    private T helper(Node p, int index) {
        if (index == 0) {
            return p.item;
        }
        return helper(p.next, index - 1);
    }

    public T getRecursive(int index) {
        if (index >= size || index < 0) {
            return null;
        }

        return helper(sentinel.next, index);
    }
}
