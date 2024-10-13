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
                    .orElse(new Word(word));

            productPWordGivenSpam = productPWordGivenSpam.multiply(new BigDecimal(Double.toString(wordStats.getSpamRatio())));
            productPWordGivenHam = productPWordGivenHam.multiply(new BigDecimal(Double.toString(wordStats.getHamRatio())));
        }

        BigDecimal numerator = pSpam.multiply(productPWordGivenSpam);
        BigDecimal denominator = numerator.add(pHam.multiply(productPWordGivenHam));

        BigDecimal result = numerator.divide(denominator, new MathContext(10)); // 10 digits of precision

        return result.doubleValue(); // Convert BigDecimal to double
    }



}
