package org.example;

import org.example.model.MailRepository;
import org.example.model.Word;

import java.util.List;

public class Main {

    /**
     * Erwartet wird eine Abgabe, bei der nach dem Ausf¨uhren des Programms eine Zusammen- fassung
     * aller Werte (Schwellenwert, α, Erkennungsraten) angezeigt wird.
     * Senden Sie einen Screenshot dieser Ausgabe in der Mail mit ihrer Abgabe mit.
     */
    public static void main(String[] args) throws Exception {
        List<Word> wordList = BayesTrainer.train(null);
        BayesFilter bayesFilter = new BayesFilter(wordList);

        // find correct "Schwellenwert" (most correct) using the calibration mails
        double optimalThreshold = bayesFilter.findOptimalThreshold();

        // test the filter with the test mails
        List<String> hamMailsUri = new MailRepository().getAllHamTestMails();
        List<String> spamMailsUri = new MailRepository().getAllSpamTestMails();

        int noOfCorrectlyClassifiedHamMails = 0;
        int noOfCorrectlyClassifiedSpamMails = 0;

        for (String hamMailUri : hamMailsUri) {
            double spamProbability = bayesFilter.isSpam(hamMailUri);
            if (spamProbability < optimalThreshold) {
                noOfCorrectlyClassifiedHamMails++;
            }
        }

        for (String spamMailUri : spamMailsUri) {
            double spamProbability = bayesFilter.isSpam(spamMailUri);
            if (spamProbability >= optimalThreshold) {
                noOfCorrectlyClassifiedSpamMails++;
            }
        }


        System.out.println("Optimaler Schwellenwert: " + optimalThreshold);
        System.out.println("α: " + Word.alpha);
        System.out.println("Erkennungsrate Ham-Mails: " + (double) noOfCorrectlyClassifiedHamMails / hamMailsUri.size());
        System.out.println("Erkennungsrate Spam-Mails: " + (double) noOfCorrectlyClassifiedSpamMails / spamMailsUri.size());
        System.out.println("Erkennungsrate gesamt: " + (double) (noOfCorrectlyClassifiedHamMails + noOfCorrectlyClassifiedSpamMails) / (hamMailsUri.size() + spamMailsUri.size()));
    }


}