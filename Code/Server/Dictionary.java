/**
 * Operations for dictionary include:
 *                                  print all data
 *                                  query a word
 *                                  add a word and meanings
 *                                  remove a word and meanings
 *                                  update a word and meanings
 *
 * @author Josh Feng, 1266669, chenhaof@student.unimelb.edu.au
 * @date 4 April 2024
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Dictionary extends Thread {
    private static DictionaryFormat dictionaryFormat;
    private static ObjectMapper mapper;

    // initial setup
    public Dictionary(String fileAddress){
        mapper = new ObjectMapper();
        try {
            dictionaryFormat = mapper.readValue(new File(fileAddress), DictionaryFormat.class);
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
        } catch (IOException e) {
            System.out.println("File data structure not correct");
            System.exit(0);
        }
    }

    // print all exist data include words and its meanings
    public String printAll() {
        String context = "";
        for (WordFormat word : dictionaryFormat.getDictionary()) {
            context += "Word: " + word.getWord();
            context += "\nMeanings: " +
                    String.join("\n", word.getMeanings()) + "\n\n";
        }
        return context;
    }

    // query the word and return word and meanings
    public synchronized String queryWord(String queryWord) {
        String result = null;
        for (WordFormat word : dictionaryFormat.getDictionary()) {
            if (Objects.equals(word.getWord(), queryWord)) {
                result = "Word: " + word.getWord() +
                        "\nMeanings: " + String.join("\n", word.getMeanings());
            }
        }
        return result;
    }

    // add a word and its meanings into the dictionary
    public synchronized String addWord(String addWord, String addMeanings) {
        // Check is exist
        for (WordFormat word : dictionaryFormat.getDictionary()) {
            if (word.getWord().equals(addWord)) {
                return addWord + " already exist\nAdded fail";
            }
        }

        WordFormat temp = new WordFormat();
        temp.setWord(addWord);
        temp.setMeanings(Arrays.asList(addMeanings.split(Params.MEANINGS_SPLIT_FORMAT)));
        dictionaryFormat.getDictionary().add(temp);

        return addWord + " added success";
    }

    // remove a word and its meanings
    public synchronized String removeWord(String removeWord) {
        if (dictionaryFormat.removeWord(removeWord)) {
            return removeWord + " remove success";
        }
        return removeWord + " not in dictionary\nRemove fails";
    }

    // update an exist word and its meanings
    public synchronized String updateWord(String updateWord, String updateMeanings) {
        for (WordFormat word : dictionaryFormat.getDictionary()) {
            if (word.getWord().equals(updateWord)) {
                List<String> currMeaning = word.getMeanings();
                List<String> newMeaning = Arrays.asList(updateMeanings.split("\n"));
                Set<String> set = new HashSet<>();
                set.addAll(currMeaning);
                set.addAll(newMeaning);
                List<String> mergeMeanings = new ArrayList<>(set);
                word.setMeanings(mergeMeanings);
                return updateWord + " update success";
            }
        }
        return updateWord + " not exist";
    }

    // save data into the file
    public synchronized void saveFile(String fileAddress) {
        try {
            mapper.writeValue(new File(fileAddress), dictionaryFormat);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

