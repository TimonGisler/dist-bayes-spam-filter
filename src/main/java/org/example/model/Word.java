package org.example.model;

public class Word {
    private String word;


    private int noOfOccurencesInSpam;
    private int noOfSpamMails;

    private int noOfOccurencesInHam;
    private int noOfHamMails;

    public Word(String word) {
        this.word = word;
    }

    public String getWord() {
        return word;
    }

    public void setNoOfSpamMails(int noOfSpamMails) {
        this.noOfSpamMails = noOfSpamMails;
    }

    public void setNoOfHamMails(int noOfHamMails) {
        this.noOfHamMails = noOfHamMails;
    }

    public void setNoOfOccurencesInHam(int noOfOccurencesInMails) {
        this.noOfOccurencesInHam = noOfOccurencesInMails;
    }

    public void setNoOfOccurencesInSpam(int noOfOccurencesInMails) {
        this.noOfOccurencesInSpam = noOfOccurencesInMails;
    }

    public double getSpamRatio(){
        return this.noOfOccurencesInSpam /(double) this.noOfSpamMails;
    }

    public double getHamRatio(){
        return this.noOfOccurencesInHam /(double) this.noOfHamMails;
    }

    public double getBayesSpam(){
        return getSpamRatio() / (getSpamRatio() + getHamRatio());
    }
}
