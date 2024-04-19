/**
 * The dictionary file structure
 *
 * @author Josh Feng, 1266669, chenhaof@student.unimelb.edu.au
 * @date 4 April 2024
 */
import java.util.List;

public class DictionaryFormat {
    private List<WordFormat> dictionary;

    public synchronized List<WordFormat> getDictionary() {
        return dictionary;
    }

    public synchronized boolean removeWord(String removeWord) {
        return dictionary.removeIf(Word -> Word.getWord().equals(removeWord));
    }
}
