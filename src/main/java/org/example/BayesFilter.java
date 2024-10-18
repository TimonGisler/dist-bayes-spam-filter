package org.example;

import org.example.model.Mail;
import org.example.model.MailRepository;
import org.example.model.Word;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;

public class BayesFilter {
    List<Word> allUniqueWordsWhichAppearedInAllMails = new ArrayList<>();

    public BayesFilter(List<Word> allUniqueWordsWhichAppearedInAllMails) {
        this.allUniqueWordsWhichAppearedInAllMails = allUniqueWordsWhichAppearedInAllMails;
    }

    /**
     * Takes a path to a mail and returns the probability that the mail is spam
     */
    public double isSpam(String pathToMailToTest) throws IOException {
        // trains the model with training mails
        Mail mailToTestIfSpamOrHam = new MailRepository().getMail(pathToMailToTest);
        double probability = calculateNaiveBayesProbability(mailToTestIfSpamOrHam, allUniqueWordsWhichAppearedInAllMails);

        return probability;
    }



    private double calculateNaiveBayesProbability(Mail mailToTestIfSpamOrHam, List<Word> allUniqueWordsWhichAppearedInAllMails) {
        BigDecimal pSpam = new BigDecimal("0.5");
        BigDecimal pHam = new BigDecimal("0.5");

        BigDecimal productPWordGivenSpam = BigDecimal.ONE;
        BigDecimal productPWordGivenHam = BigDecimal.ONE;

        for (String word : mailToTestIfSpamOrHam.getWords()) {
            Word wordStats = allUniqueWordsWhichAppearedInAllMails.stream()
                    .filter(w -> w.getWord().equals(word))
                    .findFirst()
                    .orElse(null);

            if (wordStats == null) {
                continue; // Word not found in training data, skip it
            }

            productPWordGivenSpam = productPWordGivenSpam.multiply(new BigDecimal(Double.toString(wordStats.getSpamRatio())));
            productPWordGivenHam = productPWordGivenHam.multiply(new BigDecimal(Double.toString(wordStats.getHamRatio())));
        }

        BigDecimal numerator = pSpam.multiply(productPWordGivenSpam);
        BigDecimal denominator = numerator.add(pHam.multiply(productPWordGivenHam));

        BigDecimal result = numerator.divide(denominator, new MathContext(10)); // 10 digits of precision

        return result.doubleValue(); // Convert BigDecimal to double
    }

    public double findOptimalThreshold() throws IOException {
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

        return bestThreshold;
    }


}
