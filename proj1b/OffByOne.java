public class OffByOne implements CharacterComparator {
    /* returns true for characters that are different by exactly one. */
    // interface 中的方法都是 public，子类重写父类方法时，访问权限不能变小。
    @Override
    public boolean equalChars(char x, char y) {
        return Math.abs(x - y) == 1;
    }
}