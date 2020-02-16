package ru.iskandar.playersearcher.form;


import ru.iskandar.playersearcher.model.Gender;

public class SuggestionForm {

    private String firstName;
    private String lastName;

    private Gender gender;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender aGender) {
        this.gender = aGender;
    }


}