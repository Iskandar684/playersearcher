package ru.iskandar.playersearcher.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Player implements UserDetails {

    private String _login;

    private String _password;

    private String name;

    private Gender _gender;

    private PlayerLevel _level;

    private List<Role> _roles = Collections.singletonList(new Role("USER"));

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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return _roles;
    }

    @Override
    public String getPassword() {
        return _password;
    }

    public void setPassword(String aPassword) {
        this._password = aPassword;
    }

    @Override
    public String getUsername() {
        return _login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}
