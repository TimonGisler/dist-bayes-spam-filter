package org.example.model;

public class Word {
    private String word;

    public static final double alpha = 0.1;

    private double noOfOccurencesInSpam;
    private int noOfSpamMails;

    private double noOfOccurencesInHam;
    private int noOfHamMails;

    public Word(String word) {
        this.word = word;
    }

    public int getNoOfSpamMails() {
        return noOfSpamMails;
    }


    public double getNoOfOccurencesInSpam() {
        //alpha if word is not in spam mails
        if (noOfOccurencesInSpam == 0) {
            return alpha;
        }

        return noOfOccurencesInSpam;
    }

    public double getNoOfOccurencesInHam() {
        // alpha if word is not in ham mails
        if (noOfOccurencesInHam == 0) {
            return alpha;
        }

        return noOfOccurencesInHam;
    }

    public String getWord() {
        return word;
    }

    public void setNoOfSpamMails(int noOfSpamMails) {
        if (noOfSpamMails == 0) {
            System.out.println("NO SPAM MAILS SHOULD NEVER BE POSSIBLE!!!!!!");
        }

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

    public double getSpamRatio() {
        return this.getNoOfOccurencesInSpam() / (double) this.noOfSpamMails;
    }

    public double getHamRatio() {
        return this.getNoOfOccurencesInHam() / (double) this.noOfHamMails;
    }

    public double getBayesSpam() {
        return getSpamRatio() / (getSpamRatio() + getHamRatio());
    }

}
