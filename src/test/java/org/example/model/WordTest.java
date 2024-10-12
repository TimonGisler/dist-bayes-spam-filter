package org.example.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WordTest {

    @Test
    void testBayesSpamCalculation() {
        // von diesem tutorial kopiert: https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-api/5.11.2

        // Test für das Wort "haben"
        Word haben = new Word("haben");
        haben.setNoOfOccurencesInHam(30);
        haben.setNoOfHamMails(100);
        haben.setNoOfOccurencesInSpam(7);
        haben.setNoOfSpamMails(100);

        // Test für das Wort "online"
        Word online = new Word("online");
        online.setNoOfOccurencesInHam(3);
        online.setNoOfHamMails(100);
        online.setNoOfOccurencesInSpam(8);
        online.setNoOfSpamMails(100);

        // Berechnung der kombinierten Wahrscheinlichkeit
        double pSpamHaben = haben.getSpamRatio();
        double pSpamOnline = online.getSpamRatio();
        double pHamHaben = haben.getHamRatio();
        double pHamOnline = online.getHamRatio();

        double combinedProbability = (pSpamHaben * pSpamOnline) /
                ((pSpamHaben * pSpamOnline) + (pHamHaben * pHamOnline));

        // Überprüfung der Ergebnisse
        assertEquals(0.07, haben.getSpamRatio(), 0.001);
        assertEquals(0.30, haben.getHamRatio(), 0.001);
        assertEquals(0.08, online.getSpamRatio(), 0.001);
        assertEquals(0.03, online.getHamRatio(), 0.001);

        // Überprüfung der kombinierten Wahrscheinlichkeit
        assertEquals(0.38, combinedProbability, 0.01);

        // Überprüfung der getBayesSpam() Methode
        assertEquals(0.189, haben.getBayesSpam(), 0.001);
        assertEquals(0.727, online.getBayesSpam(), 0.001);
    }
}