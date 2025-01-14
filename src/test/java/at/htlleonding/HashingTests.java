package at.htlleonding;

import at.htlleonding.services.HashingService;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.internal.common.assertion.Assertion;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class HashingTests {
    private final HashingService hashingService;

    public HashingTests(HashingService hashingService) {
        this.hashingService = hashingService;
    }

    @Test
    void testThatHashingYieldsADifferentStringToRawPassword() {
        var rawPassword = "MyGreatGreatPassword";
        var hash = this.hashingService.hash(rawPassword);
        Assertions.assertNotEquals(rawPassword, hash);
    }

    @Test
    void testThatHashesHaveTheSameLengthAndNotEmpty() {
        String[] passwords = {
                "",
                "a",
                "ab",
                ";4",
                "alk;sdh f8asdjlfil;asdj folisjd fnkauks;dasdfsdf",
                "lka;sdjfoasjdl",
                "as8dfoj49f0w",
        };

        var hashes = Arrays.stream(passwords).map(this.hashingService::hash).toArray(String[]::new);
        var allSameLengths = Arrays.stream(hashes).map(String::length).distinct().count() == 1;

        Assertions.assertTrue(allSameLengths);
        Assertions.assertFalse(hashes[0].isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "a",
            "ab",
            ";4",
            "alk;sdh f8asdjlfil;asdj folisjd fnkauks;dasdfsdf",
            "lka;sdjfoasjdl",
            "as8dfoj49f0w",
    })
    void testThatComparingARawPasswordToItsHashGivesCorrectAnswer(String password) {
        var hash = this.hashingService.hash(password);
        var comparisonResult = this.hashingService.comparePasswords(password, hash);
        Assertions.assertTrue(comparisonResult);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "a",
            "ab",
            ";4",
            "alk;sdh f8asdjlfil;asdj folisjd fnkauks;dasdfsdf",
            "lka;sdjfoasjdl",
            "as8dfoj49f0w",
    })
    void testThatComparingAHashWithTheWrongPasswordReturnsFalse(String otherPassword) {
        var originalPassword = "MyGreatGreatPassword";

        var hash = this.hashingService.hash(otherPassword);
        var comparisonResult = this.hashingService.comparePasswords(originalPassword, hash);
        Assertions.assertFalse(comparisonResult);
    }

    @Test
    void testThatTheHashIsAlwaysDifferent() {
        var password = "MyGreatGreatPassword";

        var hashes = new HashSet<String>(20);
        final int countIterations = 20;

        for (int i = 0; i < countIterations; i++) {
            hashes.add(
                this.hashingService.hash(password)
            );
        }

        Assertions.assertEquals(countIterations, hashes.size());
    }
}
