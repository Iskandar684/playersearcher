package ru.iskandar.playersearcher.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Objects;

@Getter
@Accessors(prefix = "_")
@Setter
public class NewUser {

    private String _login;

    private String _password;

    private String _passwordConfirm;

    public boolean isEmpty() {
        boolean isEmpty = _login == null || _login.isEmpty();
        isEmpty = isEmpty || _password == null || _password.isEmpty();
        isEmpty = isEmpty || _passwordConfirm == null || _passwordConfirm.isEmpty();
        return isEmpty;
    }

    public boolean passwordsIsMatch() {
        return Objects.equals(_password, _passwordConfirm);
    }

}
