package ru.iskandar.playersearcher.model;

import java.util.Arrays;
import java.util.List;

/**
 * Пол.
 */
public class Gender {

    /**
     * Мужчина
     */
    public static Gender MALE = new Gender("Мужчина");

    /**
     * Женщина
     */
    public static Gender FEMALE = new Gender("Женщина");

    private String _text;


    public Gender(String aText) {
        //Нужен публичный конструктор. Поэтому enum не подходит.
        _text = aText;
    }

    public static List<Gender> values() {
        return Arrays.asList(MALE, FEMALE);
    }

    @Override
    public String toString() {
        return _text;
    }
}
