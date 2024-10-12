package org.example;

import org.example.model.Mail;
import org.example.model.MailRepository;
import org.example.model.Word;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) throws Exception {
        // Mails einlesn ("Die Emails aus ham-anlern.zip bzw. spam-anlern.zip werden nacheinander eingelesen und als Ham bzw. Spam markier")
        List<Mail> hamMails = new MailRepository().getAllHamAnlernMails();
        List<Mail> spamMails = new MailRepository().getAllSpamAnlernMails();

        // "Dabei wird fur jedes Wort in einer Ham- bzw. Spam-Mail gezahlt, in wie vielen Ham- bzw. Spam-Mails das Wort vorkommt"
        // get unique words from ham and spam mails
        List<Mail> allMails =  Stream.concat(hamMails.stream(), spamMails.stream()).toList();
        List<Word> allUniqueWordsWhichAppearedInAllMails = allMails.stream().flatMap(mail -> mail.getWords().stream()).collect(Collectors.toSet()).stream().map(Word::new).toList();

        // TODO TGS, for testing now, remove later
        // only take the first 1k of all the words, because it takes too long to calculate the probabilities for all words
        allUniqueWordsWhichAppearedInAllMails = allUniqueWordsWhichAppearedInAllMails.subList(0, 1000);

        // count how often each word appears in ham and spam mails
        AtomicInteger i = new AtomicInteger(0);
        allUniqueWordsWhichAppearedInAllMails.parallelStream().forEach(word -> {
            word.setNoOfOccurencesInHam(getNoOfOccurencesInMails(hamMails, word));
            word.setNoOfOccurencesInSpam(getNoOfOccurencesInMails(spamMails, word));
            System.out.println("Counting occurrences of word: " + word.getWord() + " it is the " + i.getAndIncrement() + "th word");
        });

        // set the number of spam and ham mails (so that probability for a word in a mail can be calculated)
        int noOfHamMails = hamMails.size();
        int noOfSpamMails = spamMails.size();
        allUniqueWordsWhichAppearedInAllMails.forEach(word -> {
            word.setNoOfHamMails(noOfHamMails);
            word.setNoOfSpamMails(noOfSpamMails);
        });


    }

    public static int getNoOfOccurencesInMails(List<Mail> mails, Word word){
        return (int) mails.stream().filter(mail -> mail.getWords().contains(word.getWord())).count();
    }
}