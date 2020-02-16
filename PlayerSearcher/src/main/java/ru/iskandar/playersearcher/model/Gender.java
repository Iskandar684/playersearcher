package ru.iskandar.playersearcher.model;

/**
 * Пол.
 */
public class Gender {

    /**   М  */
    public static Gender MALE = new Gender("M");

    /** Ж  */
    public static Gender FEMALE = new Gender("Ж");

    private String _text;


    public Gender(String aText) {
        //Нужен публичный конструктор. Поэтому enum не подходит.
        _text = aText;
    }

    @Override
    public String toString() {
        return _text;
    }
}
