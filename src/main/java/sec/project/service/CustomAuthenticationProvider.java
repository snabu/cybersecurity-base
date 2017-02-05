package sec.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import sec.project.domain.Account;
import sec.project.repository.AccountRepository;

import java.util.ArrayList;


@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private AccountRepository accountRepository;
    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();
        Account serviceAccount = accountRepository.findByUsername(name);
        if (serviceAccount.getPassword().equals(password)) {

            return new UsernamePasswordAuthenticationToken(name, password, new ArrayList<>());
        }
        else {
            return null;
        }

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}