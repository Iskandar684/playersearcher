package ru.iskandar.playersearcher.model;

public class Player {

    private String _login;

    private String _password;

    private String name;

    private Gender  _gender;

    private PlayerLevel _level;

    public Player(String aName, Gender aGender, PlayerLevel aLevel) {
        this.name = aName;
        this._gender = aGender;
        this._level = aLevel;
    }

    public String getName() {
        return name;
    }

    public void setName(String aName) {
        this.name = aName;
    }

    public Gender getGender() {
        return _gender;
    }

    public void setGender(Gender aGender) {
        _gender = aGender;
    }

    public PlayerLevel getLevel() {
        return _level;
    }

    public void setLevel(PlayerLevel aLevel) {
        _level = aLevel;
    }

    public String getLogin() {
        return _login;
    }

    public void setLogin(String aLogin) {
        this._login = aLogin;
    }

    public String getPassword() {
        return _password;
    }

    public void setPassword(String aPassword) {
        this._password = aPassword;
    }
}
