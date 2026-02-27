/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra, Alexander Hwang
 *
 */
public class RadixSort {
    /**
     * Does LSD radix sort on the passed in array with the following restrictions:
     * The array can only have ASCII Strings (sequence of 1 byte characters)
     * The sorting is stable and non-destructive
     * The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     *
     * @return String[] the sorted array
     */
    public static String[] sort(String[] asciis) {
        String[] clone = new String[asciis.length];
        int cur = 0;
        for (String s : asciis) {
            clone[cur] = s;
            cur += 1;
        }

        int maxLen = Integer.MIN_VALUE;
        for (String s : clone) {
            if (s.length() > maxLen) {
                maxLen = s.length();
            }
        }

        // 全空
        if (maxLen == Integer.MIN_VALUE) {
            return clone;
        }

        for (int i = 0; i < maxLen; i++) {
            sortHelperLSD(clone, maxLen - 1 - i);
        }

        return clone;
    }

    /**
     * LSD helper method that performs a destructive counting sort the array of
     * Strings based off characters at a specific index.
     * @param asciis Input array of Strings
     * @param index The position to sort the Strings on.
     */
    private static void sortHelperLSD(String[] asciis, int index) {
        // 字符串排序右补零
        int[] count = new int[257];
        for (String s : asciis) {
            int code;
            if (s.length() > index) {
                code = (int) s.charAt(index);
            } else {
                code = -1;
            }

            count[code + 1]++;
        }

        int[] startIndex = new int[257];
        startIndex[0] = 0;
        for (int i = 1; i < 257; i++) {
            startIndex[i] = startIndex[i - 1] + count[i - 1];
        }

        String[] sorted = new String[asciis.length];
        for (String s : asciis) {
            int code;
            if (s.length() > index) {
                code = (int) s.charAt(index);
            } else {
                code = -1;
            }

            sorted[startIndex[code + 1]] = s;
            startIndex[code + 1]++;
        }

        // 深拷贝
        int cur = 0;
        for (String s : sorted) {
            asciis[cur] = s;
            cur += 1;
        }
    }

    /**
     * MSD radix sort helper function that recursively calls itself to achieve the sorted array.
     * Destructive method that changes the passed in array, asciis.
     *
     * @param asciis String[] to be sorted
     * @param start int for where to start sorting in this method (includes String at start)
     * @param end int for where to end sorting in this method (does not include String at end)
     * @param index the index of the character the method is currently sorting on
     *
     **/
    private static void sortHelperMSD(String[] asciis, int start, int end, int index) {
        // Optional MSD helper method for optional MSD radix sort
        return;
    }
}
