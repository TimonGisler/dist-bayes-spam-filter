package org.example.model;

public class Word {
    private String word;

    private int noOfOccurencesInSpam;
    private int noOfOccurencesInHam;

    public Word(String word) {
        this.word = word;
    }

    public String getWord() {
        return word;
    }

    public void increaseSpamCount() {
        noOfOccurencesInSpam++;
    }

    public void increaseHamCount() {
        noOfOccurencesInHam++;
    }
}
