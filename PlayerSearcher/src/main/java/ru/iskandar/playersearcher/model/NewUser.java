package ru.iskandar.playersearcher.model;

import java.util.Objects;

public class NewUser {

    private String _login;

    private String _password;

    private String _passwordConfirm;

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

    public String getPasswordConfirm() {
        return _passwordConfirm;
    }

    public void setPasswordConfirm(String aPassword) {
        this._passwordConfirm = aPassword;
    }

    public boolean isEmpty (){
        boolean isEmpty = _login == null || _login.isEmpty();
        isEmpty = isEmpty || _password == null || _password.isEmpty();
        isEmpty = isEmpty || _passwordConfirm == null || _passwordConfirm.isEmpty();
        return isEmpty;
    }

    public boolean passwordsIsMatch(){
        return Objects.equals(_password, _passwordConfirm);
    }

}
