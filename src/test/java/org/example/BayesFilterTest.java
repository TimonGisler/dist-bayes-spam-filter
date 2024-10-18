package org.example;

import org.example.model.MailRepository;
import org.example.model.Word;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class BayesFilterTest {

    @Test
    void shoulBeHam() throws IOException {
        String mailToTestForSpam = "src/main/resources/ham-anlern/00001hamaaaaaaatgstest.ea7e79d3153e7469e7a9c3e0af6a357e";
        List<Word> wordList = BayesTrainer.train(mailToTestForSpam);

        double spamProbaility = new BayesFilter(wordList).isSpam(mailToTestForSpam);
        System.out.println("Spam probability: " + spamProbaility);
        // probability of spam must be below 0.5
        assertTrue(spamProbaility < 0.5);
    }

    @Test
    void shouldBeSpam() throws IOException {
        String mailToTestForSpam = "src/main/resources/spam-anlern/00001spamaaaaaaatgstest.ea7e79d3153e7469e7a9c3e0af6a357e";
        List<Word> wordList = BayesTrainer.train(mailToTestForSpam);

        double spamProbaility = new BayesFilter(wordList).isSpam(mailToTestForSpam);
        System.out.println("Spam probability: " + spamProbaility);
        // probability of spam must be above 0.5
        assertTrue(spamProbaility > 0.5);
    }

    @Test
    void kalibrierungsTest() throws IOException {

        List<Word> wordList = BayesTrainer.train(null);

        // get all mails of the test set
        List<String> hamMailsUri = new MailRepository().getAllHamKallibrierungMails();
        List<String> spamMailsUri = new MailRepository().getAllSpamKallibrierungMails();

        // count how many ham mails are correctly classified as ham
        int noOfCorrectlyClassifiedHamMails = 0;
        for (String hamMailUri : hamMailsUri) {
            double spamProbability = 0;
            try {
                spamProbability = new BayesFilter(wordList).isSpam(hamMailUri);
            } catch (Exception e) {
                System.out.println("Error while classifying mail: " + hamMailUri);
                spamProbability = new BayesFilter(wordList).isSpam(hamMailUri);
            }

            double schwellenWert = 0.5;
            if (spamProbability < schwellenWert) {
                noOfCorrectlyClassifiedHamMails++;
            }
        }

        // count how many spam mails are correctly classified as spam
        int noOfCorrectlyClassifiedSpamMails = 0;
        for (String spamMailUri : spamMailsUri) {
            double spamProbability = 0;
            try {
                spamProbability = new BayesFilter(wordList).isSpam(spamMailUri);
            } catch (Exception e) {
                System.out.println("Error while classifying mail: " + spamMailUri);
                spamProbability = new BayesFilter(wordList).isSpam(spamMailUri);
            }

            double schwellenWert = 0.5;
            if (spamProbability >= schwellenWert) {
                noOfCorrectlyClassifiedSpamMails++;
            }
        }

        System.out.println("Ham mails: " + hamMailsUri.size() + " correctly classified: " + noOfCorrectlyClassifiedHamMails);
        System.out.println("Spam mails: " + spamMailsUri.size() + " correctly classified: " + noOfCorrectlyClassifiedSpamMails);
    }


    @Test
    void findOptimalThreshold() throws IOException {
        List<Word> wordList = BayesTrainer.train(null);

        List<String> hamMailsUri = new MailRepository().getAllHamKallibrierungMails();
        List<String> spamMailsUri = new MailRepository().getAllSpamKallibrierungMails();

        double bestThreshold = 0;
        double bestAccuracy = 0;

        // Calculate spam probabilities for all mails once
        List<Double> hamProbabilities = new ArrayList<>();
        List<Double> spamProbabilities = new ArrayList<>();

        for (String hamMailUri : hamMailsUri) {
            try {
                hamProbabilities.add(new BayesFilter(wordList).isSpam(hamMailUri));
            } catch (Exception e) {
                System.out.println("Error while classifying ham mail: " + hamMailUri);
            }
        }

        for (String spamMailUri : spamMailsUri) {
            try {
                spamProbabilities.add(new BayesFilter(wordList).isSpam(spamMailUri));
            } catch (Exception e) {
                System.out.println("Error while classifying spam mail: " + spamMailUri);
            }
        }

        // Iterate through possible threshold values
        for (double threshold = 0; threshold <= 1; threshold += 0.01) {
            int correctlyClassifiedHam = 0;
            int correctlyClassifiedSpam = 0;

            for (double probability : hamProbabilities) {
                if (probability < threshold) {
                    correctlyClassifiedHam++;
                }
            }

            for (double probability : spamProbabilities) {
                if (probability >= threshold) {
                    correctlyClassifiedSpam++;
                }
            }

            double accuracy = (double) (correctlyClassifiedHam + correctlyClassifiedSpam) /
                    (hamProbabilities.size() + spamProbabilities.size());

            if (accuracy > bestAccuracy) {
                bestAccuracy = accuracy;
                bestThreshold = threshold;
            }
        }

        System.out.printf("Best threshold: %.2f%n", bestThreshold);
        System.out.printf("Best accuracy: %.2f%%%n", bestAccuracy * 100);

        // Print results for the best threshold
        int finalCorrectHam = 0;
        int finalCorrectSpam = 0;

        for (double probability : hamProbabilities) {
            if (probability < bestThreshold) {
                finalCorrectHam++;
            }
        }

        for (double probability : spamProbabilities) {
            if (probability >= bestThreshold) {
                finalCorrectSpam++;
            }
        }

        System.out.println("Ham mails: " + hamProbabilities.size() +
                " correctly classified: " + finalCorrectHam);
        System.out.println("Spam mails: " + spamProbabilities.size() +
                " correctly classified: " + finalCorrectSpam);
    }

}