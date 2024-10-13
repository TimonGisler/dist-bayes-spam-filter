package org.example;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class BayesFilterTest {

    @Test
    void shoulBeHam() throws IOException {
        double spamProbaility = BayesFilter.isSpam("src/main/resources/ham-anlern/00001hamaaaaaaatgstest.ea7e79d3153e7469e7a9c3e0af6a357e");
        System.out.println("Spam probability: " + spamProbaility);
    }

    @Test
    void shouldBeSpam() throws IOException {
        double spamProbaility = BayesFilter.isSpam("src/main/resources/spam-anlern/00001spamaaaaaaatgstest.ea7e79d3153e7469e7a9c3e0af6a357e");
        System.out.println("Spam probability: " + spamProbaility);
    }

}