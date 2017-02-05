package sec.project.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.PlaintextPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sec.project.domain.Account;
import sec.project.repository.AccountRepository;

import java.security.Principal;

@Controller
public class PasswordChangeController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PlaintextPasswordEncoder passwordEncoder;
    @RequestMapping(value = "/changepassword", method = RequestMethod.GET)
    String changePsswordForm()
    {
        return "changepassword";
    }

    @RequestMapping(value = "/changepassword", method = RequestMethod.POST)
    String changePssword(Principal principal, @RequestParam String newPassword) {
        Account acc = accountRepository.findByUsername(principal.getName());
        acc.setPassword(passwordEncoder.encodePassword(newPassword, ""));
        accountRepository.save(acc);
        return "passwordchanged";
    }

}
