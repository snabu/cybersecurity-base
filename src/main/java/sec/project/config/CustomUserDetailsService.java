package sec.project.config;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sec.project.domain.Account;
import sec.project.repository.AccountRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {


        @Autowired
        private AccountRepository accountRepository;


        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            Account serviceAccount = accountRepository.findByUsername(username);
            if (serviceAccount == null) {
                throw new UsernameNotFoundException("No such user: " + username);
            }

            return new org.springframework.security.core.userdetails.User(
                    serviceAccount.getUsername(),
                    serviceAccount.getPassword(),
                    true,
                    true,
                    true,
                    true,
                    Arrays.asList(new SimpleGrantedAuthority("USER")));
        }
}
