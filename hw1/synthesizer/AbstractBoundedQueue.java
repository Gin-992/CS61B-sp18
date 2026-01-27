package synthesizer;

public abstract class AbstractBoundedQueue<T> implements BoundedQueue<T> {
    // 抽象类无法预测子类会用什么数据结构来存数据
    // 没有存储结构，就无法编写遍历逻辑
    protected int fillCount;
    protected int capacity;

    public int capacity() {
        return capacity;
    }

    public int fillCount() {
        return fillCount;
    }
}
