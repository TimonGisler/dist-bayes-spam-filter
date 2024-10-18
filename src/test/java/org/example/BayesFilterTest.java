package org.example;

import org.example.model.MailRepository;
import org.example.model.Word;
import org.junit.jupiter.api.Test;

import java.io.IOException;
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
        BayesFilter bayesFilter = new BayesFilter(BayesTrainer.train(null));

        double optimalThreshold = bayesFilter.findOptimalThreshold();
    }

}