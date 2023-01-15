package ru.iskandar.playersearcher.model;

import org.springframework.security.core.GrantedAuthority;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Role implements GrantedAuthority {

    /** Идентификатор для сериализации */
    private static final long serialVersionUID = -7141093196439940958L;

    private final String _role;

    @Override
    public String getAuthority() {
        return _role;
    }
}
