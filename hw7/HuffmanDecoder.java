import java.util.ArrayList;
import java.util.List;

public class HuffmanDecoder {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java HuffmanDecoder <input file> <output file>");
            return;
        }
        String inputFileName = args[0];
        String outputFileName = args[1];

        ObjectReader or = new ObjectReader(inputFileName);
        Object x = or.readObject();
        BinaryTrie trie = (BinaryTrie) x;

        Object y = or.readObject();
        int totalSymbols = (Integer) y;

        Object z = or.readObject();
        BitSequence hugeBitSequence = (BitSequence) z;

        List<Character> decodedChars = new ArrayList<>();
        for (int i = 0; i < totalSymbols; i++) {
            Match match = trie.longestPrefixMatch(hugeBitSequence);
            decodedChars.add(match.getSymbol());
            BitSequence matchedSeq = match.getSequence();
            int bitsConsumed = matchedSeq.length();
            hugeBitSequence = hugeBitSequence.allButFirstNBits(bitsConsumed);
        }

        char[] result = new char[decodedChars.size()];
        for (int i = 0; i < decodedChars.size(); i++) {
            result[i] = decodedChars.get(i);
        }

        FileUtils.writeCharArray(outputFileName, result);
    }
}
