package com.fungisearch.fudriver.common;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import java.util.Collections;
import java.util.List;

/**
 * Created by marcin on 22.01.17.
 */
public class CreateFakeAuthentication {
    public static void authenticatate() {
        List<GrantedAuthority> grantedAuthorities = Collections.emptyList();
        User user = new User("admin", "", grantedAuthorities);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, "");
        SecurityContextHolder.getContext().setAuthentication(authentication);

    }
}
