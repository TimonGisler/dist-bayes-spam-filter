package org.example;

import org.example.model.Mail;
import org.example.model.MailRepository;
import org.example.model.Word;

import java.util.List;
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
        List<Word> allWords = allMails.stream().flatMap(mail -> mail.getWords().stream()).collect(Collectors.toSet()).stream().map(Word::new).toList();

        // count how often each word appears in ham and spam mails
        hamMails.forEach(mail -> mail.getWords().forEach(word -> allWords.stream().filter(w -> w.getWord().equals(word)).findFirst().ifPresent(Word::increaseHamCount)));
        spamMails.forEach(mail -> mail.getWords().forEach(word -> allWords.stream().filter(w -> w.getWord().equals(word)).findFirst().ifPresent(Word::increaseSpamCount)));


    }
}