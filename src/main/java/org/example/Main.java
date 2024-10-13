package org.example;

public class Main {
    public static void main(String[] args) throws Exception {
        double spamProbaility = BayesFilter.isSpam("src/main/resources/ham-test/aaaaaaatgstest.ea7e79d3153e7469e7a9c3e0af6a357e");
        System.out.println("Spam probability: " + spamProbaility);
    }


}