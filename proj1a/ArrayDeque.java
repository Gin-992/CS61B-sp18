public class ArrayDeque<T> {
    private int size;
    private T[] items;
    int nextFirst;
    int nextLast;

    public ArrayDeque() {
        size = 0;
        // 先创建 Object 数组再强转
        items = (T[]) new Object[8];
        nextFirst = 0;
        nextLast = 1;
    }

    // 拉直
    private void resize(int capacity) {
        T[] a = (T[]) new Object[capacity];

        int head = (nextFirst + 1) % items.length;

        if (head + size > items.length) {
            // 前半段长度
            int lengthOfFirstChunk = items.length - head;
            // 后半段长度
            int lengthOfSecondChunk = size - lengthOfFirstChunk;

            System.arraycopy(items, head, a, 0, lengthOfFirstChunk);
            System.arraycopy(items, 0, a, lengthOfFirstChunk, lengthOfSecondChunk);
        }else{
            System.arraycopy(items, head, a, 0, size);
        }

        items = a;
        nextFirst = items.length - 1;
        nextLast = size;
    }

    public void addFirst(T item) {
        if (items.length == size) {
            resize(size * 2);
        }

        size += 1;
        items[nextFirst] = item;
        if (nextFirst - 1 < 0) {
            nextFirst = items.length - 1;
        }else{
            nextFirst -= 1;
        }
    }

    public void addLast(T item) {
        if (items.length == size) {
            resize(size * 2);
        }

        size += 1;
        items[nextLast] = item;
        if (nextLast + 1 == items.length) {
            nextLast = 0;
        }else{
            nextLast += 1;
        }
    }

    public boolean isEmpty() {
        return (size == 0);
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        for (int i = 0; i < size; i++) {
            System.out.print(get(i) + " ");
        }
        System.out.println();
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }else{
            size -= 1;
        }

        if (items.length >= 16 && (double) size / items.length < 0.25) {
            resize(items.length / 2);
        }

        T returnItem;
        if (nextFirst + 1 == items.length) {
            returnItem = items[0];
            items[0] = null;
            nextFirst = 0;
        }else {
            returnItem = items[nextFirst + 1];
            items[nextFirst + 1] = null;
            nextFirst += 1;
        }
        return returnItem;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }else{
            size -= 1;
        }

        if (items.length >= 16 && (double) size / items.length < 0.25) {
            resize(items.length / 2);
        }

        T returnItem;
        if (nextLast == 0) {
            returnItem = items[items.length - 1];
            items[items.length - 1] = null;
            nextLast = items.length - 1;
        }else {
            returnItem = items[nextLast - 1];
            items[nextLast - 1] = null;
            nextLast -= 1;
        }
        return returnItem;
    }

    public T get(int index) {
        if (index >= size || index < 0) {
            return null;
        }

        if (nextFirst + 1 + index >= items.length) {
            return items[nextFirst + 1 + index - items.length];
        }else{
            return items[index + nextFirst + 1];
        }
    }
}