package sec.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.PlaintextPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import sec.project.domain.Account;
import java.security.Principal;
import sec.project.domain.User;
import sec.project.repository.AccountRepository;

import javax.annotation.PostConstruct;

@Controller
public class SignupController {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PlaintextPasswordEncoder passwordEncoder;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public  String defaultMapping(Principal principal){
       if (principal == null)
           return "login";
       else
        return "homepage";
    }

    @PostConstruct
    public void init() {
        Account acc = new Account();
        Account acc2 = new Account();
        acc.setPassword(passwordEncoder.encodePassword("12345", ""));
        acc.setEmail("example1@example.com");
        acc.setUsername("testuser1");
        accountRepository.save(acc);

        acc2.setPassword(passwordEncoder.encodePassword("12345", ""));
        acc2.setEmail("example2@example.com");
        acc2.setUsername("testuser2");
        accountRepository.save(acc2);

    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String register(@ModelAttribute User user) {
        if (accountRepository.findByUsername(user.getUsername()) != null) {
            return "signup";
        }
        Account acc = new Account();
        acc.setPassword(passwordEncoder.encodePassword(user.getPassword(), ""));
        acc.setEmail(user.getEmail());
        acc.setUsername(user.getUsername());
        accountRepository.save(acc);
        return "login";
    }
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String signupForm() {
        return "signup";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginForm() {
        return "login";
    }
}
