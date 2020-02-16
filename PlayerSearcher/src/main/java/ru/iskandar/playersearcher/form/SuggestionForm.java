package ru.iskandar.playersearcher.form;


import ru.iskandar.playersearcher.model.Gender;
import ru.iskandar.playersearcher.model.PlayerLevel;

public class SuggestionForm {

    private String firstName;

    private Gender gender;

    private PlayerLevel level;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender aGender) {
        this.gender = aGender;
    }

    public PlayerLevel getLevel() {
        return level;
    }

    public void setLevel(PlayerLevel level) {
        this.level = level;
    }
}