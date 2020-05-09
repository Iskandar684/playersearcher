package ru.iskandar.playersearcher.model;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.Objects;

@Getter
@Accessors(prefix = "_")
@Setter
public class NewUser {

    private String _login;

    private String _password;

    private String _passwordConfirm;

    private String _name;

    private Gender _gender;

    private PlayerLevel _level;

    public boolean isEmpty() {
        boolean isEmpty = _login == null || _login.isEmpty();
        isEmpty = isEmpty || _password == null || _password.isEmpty();
        isEmpty = isEmpty || _passwordConfirm == null || _passwordConfirm.isEmpty();
        isEmpty = isEmpty || _name == null || _name.isEmpty();
        isEmpty = isEmpty || _gender == null ;
        isEmpty = isEmpty || _level == null ;
        return isEmpty;
    }

    public boolean passwordsIsMatch() {
        return Objects.equals(_password, _passwordConfirm);
    }

}
