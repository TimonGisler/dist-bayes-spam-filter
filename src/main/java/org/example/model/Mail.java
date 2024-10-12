package org.example.model;

import java.util.Arrays;
import java.util.List;

public class Mail {

    private String text;

    public Mail(String text) {
        this.text = text;
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getWords() {
        // "und ein Leerzeichen als Worttrennungssymbol verwenden."
        return Arrays.asList(text.split(" "));
    }
}
