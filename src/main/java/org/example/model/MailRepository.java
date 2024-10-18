package org.example.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads in the mails from the files
 */
public class MailRepository {
    // path to resource folder
    String reosurcePath = "src/main/resources/";

    public List<Mail> getAllHamAnlernMails() throws IOException {
        String pathToHamAnlern = reosurcePath + "ham-anlern";

        List<String> fileContents = readMails(pathToHamAnlern);
        List<Mail> mails = mapStringToMailObject(fileContents);

        return mails;
    }

    public List<Mail> getAllSpamAnlernMails() throws IOException {
        String pathToSpamAnlern = reosurcePath + "spam-anlern";

        List<String> fileContents = readMails(pathToSpamAnlern);
        List<Mail> mails = mapStringToMailObject(fileContents);

        return mails;
    }

    private static List<Mail> mapStringToMailObject(List<String> fileContents) {
        // create a list of Mail objects
        List<Mail> mails = new ArrayList<>();
        for (String content : fileContents) {
            mails.add(new Mail(content));
        }
        return mails;
    }

    private List<String> readMails(String pathOfMailsToRead) throws IOException {
        List<String> fileContents = new ArrayList<>();

        File folder = new File(pathOfMailsToRead);
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles == null) throw new IllegalArgumentException("No files found in the given path: " + pathOfMailsToRead);

        for (File file : listOfFiles) {
            if (file.isFile()) {
                    String content = new String(Files.readAllBytes(Paths.get(file.getPath())));
                    fileContents.add(content);
            }
        }


        return fileContents;
    }

    public Mail getMail(String path) throws IOException {
        try {
            String content = new String(Files.readAllBytes(Paths.get(path)));
            return new Mail(content);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String content = new String(Files.readAllBytes(Paths.get(path)));
        return new Mail(content);
    }


    public List<String> getAllHamKallibrierungMails() {
        String pathToHamKallibrierung = reosurcePath + "ham-kallibrierung";

        File folder = new File(pathToHamKallibrierung);
        File[] listOfFiles = folder.listFiles();

        // convert to list of uris
        List<String> uris = new ArrayList<>();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                file.getName();
                uris.add(file.getAbsoluteFile().getAbsolutePath());
            }
        }
        return uris;
    }

    public List<String> getAllSpamKallibrierungMails() {
        String pathToSpamKallibrierung = reosurcePath + "spam-kallibrierung";

        File folder = new File(pathToSpamKallibrierung);
        File[] listOfFiles = folder.listFiles();

        // convert to list of uris
        List<String> uris = new ArrayList<>();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                file.getName();
                uris.add(file.getAbsoluteFile().getAbsolutePath());
            }
        }
        return uris;
    }

    public List<String> getAllHamTestMails() {
        String pathToHamTest = reosurcePath + "ham-test";

        File folder = new File(pathToHamTest);
        File[] listOfFiles = folder.listFiles();

        // convert to list of uris
        List<String> uris = new ArrayList<>();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                uris.add(file.getAbsoluteFile().getAbsolutePath());
            }
        }
        return uris;
    }

    public List<String> getAllSpamTestMails() {
        String pathToSpamTest = reosurcePath + "spam-test";

        File folder = new File(pathToSpamTest);
        File[] listOfFiles = folder.listFiles();

        // convert to list of uris
        List<String> uris = new ArrayList<>();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                uris.add(file.getAbsoluteFile().getAbsolutePath());
            }
        }
        return uris;

    }
}
