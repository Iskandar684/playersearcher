package ru.iskandar.playersearcher.model;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;

@Getter
@Builder
@Accessors(prefix = "_")
@EqualsAndHashCode(of = "_login")
public class Player implements UserDetails {

    /** Идентификатор для сериализации */
    private static final long serialVersionUID = -2041348565147666109L;

    @NonNull
    private final String _login;

    @NonNull
    private final String _password;

    @NonNull
    private final String _name;

    @NonNull
    private final Gender _gender;

    @NonNull
    private final PlayerLevel _level;

    private final String _email;

    private final List<Role> _roles = Collections.singletonList(new Role("USER"));

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return _roles;
    }

    @Override
    public String getPassword() {
        return _password;
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
