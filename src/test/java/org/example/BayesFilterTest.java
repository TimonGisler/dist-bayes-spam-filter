package org.example;

import org.example.model.MailRepository;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class BayesFilterTest {

    @Test
    void shoulBeHam() throws IOException {
        double spamProbaility = new BayesFilter().isSpam("src/main/resources/ham-anlern/00001hamaaaaaaatgstest.ea7e79d3153e7469e7a9c3e0af6a357e");
        System.out.println("Spam probability: " + spamProbaility);
        // probability of spam must be below 0.5
        assertTrue(spamProbaility < 0.5);
    }

    @Test
    void shouldBeSpam() throws IOException {
        double spamProbaility = new BayesFilter().isSpam("src/main/resources/spam-anlern/00001spamaaaaaaatgstest.ea7e79d3153e7469e7a9c3e0af6a357e");
        System.out.println("Spam probability: " + spamProbaility);
        // probability of spam must be above 0.5
        assertTrue(spamProbaility > 0.5);
    }

    @Test
    void kalibrierungsTest() throws IOException {

        // get all mails of the test set
        List<String> hamMailsUri = new MailRepository().getAllHamKallibrierungMails();
        List<String> spamMailsUri = new MailRepository().getAllSpamKallibrierungMails();

        // count how many ham mails are correctly classified as ham
        int noOfCorrectlyClassifiedHamMails = 0;
        for (String hamMailUri : hamMailsUri) {
            double spamProbability = new BayesFilter().isSpam(hamMailUri);
            if (spamProbability < 0.5) {
                noOfCorrectlyClassifiedHamMails++;
            }
        }

        System.out.println("Ham mails: " + hamMailsUri.size());
    }

}