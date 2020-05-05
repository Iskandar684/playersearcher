package ru.iskandar.playersearcher.model;

import org.springframework.security.core.GrantedAuthority;

public class Role implements GrantedAuthority {

    private String _role;

    public Role (String aRole){
        _role = aRole;
    }

    @Override
    public String getAuthority() {
        return _role;
    }
}
