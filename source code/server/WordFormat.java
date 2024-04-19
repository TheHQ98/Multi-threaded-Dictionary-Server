/**
 * The data unit structure
 * Each word must include its term and its meaning(s)
 *
 * @author Josh Feng, 1266669, chenhaof@student.unimelb.edu.au
 * @date 4 April 2024
 */
import java.util.List;

public class WordFormat {
    private String word;
    private List<String> meanings;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public List<String> getMeanings() {
        return meanings;
    }

    public void setMeanings(List<String> meanings) {
        this.meanings = meanings;
    }
}
