package ru.iskandar.playersearcher.form;


import ru.iskandar.playersearcher.model.Gender;
import ru.iskandar.playersearcher.model.HourInterval;
import ru.iskandar.playersearcher.model.PlayerLevel;
import ru.iskandar.playersearcher.model.Schedule;

public class SuggestionForm {

    private String firstName;

    private Gender gender;

    private PlayerLevel level;

    private Schedule schedule = new Schedule();


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

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }
}