package com.example.demo.validator;

import com.example.demo.annotations.TitleCase;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.*;
import java.util.regex.Pattern;

public class TitleCaseValidator implements ConstraintValidator<TitleCase, String> {

    private final String commonValidator = "([(\",',,|\\p{L})&&[^\r\t\n]]+\\s)*[(\",',,|\\p{L})&&[^\r\t\n]]+";
    private final String enLowerCaseValidator = "^[a-z][a-z]*";
    private final String enUpperCaseValidator = "^[A-Z][a-z]*";
    private final String ruLowerCaseValidator = "^[а-яё][а-яё]*";
    private final String ruUpperCaseValidator = "^[А-ЯЁ][а-яё]*";
    private String language;

    @Override
    public void initialize(TitleCase constraintAnnotation) {
        this.language = constraintAnnotation.language();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return true;
        }
        switch (this.language) {
            case "RU":
                return commonValidator(value) && ruValidator(value);
            case "EN":
                return commonValidator(value) && enValidator(value);
            case "ANY":
                return commonValidator(value) && ((ruValidator(value) || enValidator(value)));
            default:
                return false;
        }
    }

    private boolean commonValidator(String title) {
        return title.matches(commonValidator);
    }

    private boolean ruValidator(String title) {
        List<String> words = createListFromTitle(title);
        boolean firstWord = words.remove(0).matches(ruUpperCaseValidator);
        for (String word : words) {
            if (!word.matches(ruLowerCaseValidator)) {
                return false;
            }
        }
        return firstWord;
    }

    private boolean enValidator(String title) {
        List<String> words = createListFromTitle(title);
        if (words.size() > 2) {
            return enFirstAndLastWordsValidator(words) && enMainValidator(words);
        } else {
            return enShortWordValidator(words);
        }
    }

    private List<String> createListFromTitle(String string) {
        Pattern pattern = Pattern.compile("[^\\p{L}]");
        ArrayList<String> arrayFromString = new ArrayList<>(Arrays.asList(pattern.split(string)));
        arrayFromString.remove("");
        return arrayFromString;
    }

    private boolean enFirstAndLastWordsValidator(List<String> array) {
        boolean firstWord = array.remove(0).matches(enUpperCaseValidator);
        boolean lastWord = array.remove(array.size() - 1).matches(enUpperCaseValidator);
        return firstWord && lastWord;
    }

    private boolean enMainValidator(List<String> array) {
        Set<String> conjunctions = new HashSet<>();
        Collections.addAll(conjunctions, "a", "but", "for", "or", "not", "the", "an");
        for (String word : array) {
            if (conjunctions.contains(word.toLowerCase())) {
                if (!word.matches(enLowerCaseValidator)) {
                    return false;
                }
            } else {
                if (!word.matches(enUpperCaseValidator)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean enShortWordValidator(List<String> array) {
        for (String word : array) {
            if (!word.matches(enUpperCaseValidator)) {
                return false;
            }
        }
        return true;
    }
}