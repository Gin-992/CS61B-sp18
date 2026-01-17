/** An Integer tester created by Flik Enterprises. */
public class Flik {
    // int：是基本数据类型，直接存数值
    // Integer：是一个对象，可以看作是一个装着数字的盒子。
    // -128 到 127：对于这 256 个数字，Java 专门建了一个“共享仓库”，不用单独开辟内存
    // 超出范围的部分则需要开辟新内存。
    public static boolean isSameNumber(Integer a, Integer b) {
        // a.equals(b);
        return a == b;
    }
}
