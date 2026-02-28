import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HuffmanEncoder {
    public static Map<Character, Integer> buildFrequencyTable(char[] inputSymbols) {
        Map<Character, Integer> frequency = new HashMap<>();
        for (char c : inputSymbols) {
            if (frequency.containsKey(c)) {
                frequency.put(c, frequency.get(c) + 1);
            } else {
                frequency.put(c, 1);
            }
        }

        return frequency;
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please provide a file name.");
            return;
        }

        char[] inputSymbols = FileUtils.readFile(args[0]);
        Map<Character, Integer> fre = buildFrequencyTable(inputSymbols);
        BinaryTrie trie = new BinaryTrie(fre);
        ObjectWriter ow = new ObjectWriter(args[0] + ".huf");

        // Write the binary decoding trie to the .huf file
        ow.writeObject(trie);

        // Write the number of symbols to the .huf file
        ow.writeObject((Integer) inputSymbols.length);

        Map<Character, BitSequence> lookupTable = trie.buildLookupTable();

        List<BitSequence> bitsList = new ArrayList<>();

        for (char c : inputSymbols) {
            BitSequence bs = lookupTable.get(c);
            bitsList.add(bs);
        }

        BitSequence hugeBitSequence = BitSequence.assemble(bitsList);

        ow.writeObject(hugeBitSequence);
    }
}
