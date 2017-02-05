package sec.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sec.project.domain.Message;
import sec.project.repository.AccountRepository;
import sec.project.repository.MessageRepository;

import javax.annotation.PostConstruct;
import java.security.Principal;




@Controller
public class MessageController {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private AccountRepository accountRepository;
    @PostConstruct
    public void init() {
        Message msg = new Message();
        msg.setAuthor("testuser1");
        msg.setSubject("test message 1");
        msg.setMessage("This is a test message from testuser to testuser2");
        msg.setRecipient("testuser2");
        messageRepository.save(msg);
        Message msg2 = new Message();
        msg2.setAuthor("testuser2");
        msg2.setSubject("test message 2");
        msg2.setMessage("This is a test message from testuser2 to testuser");
        msg2.setRecipient("testuser1");
        messageRepository.save(msg2);
    }


    @RequestMapping(value = "/createmessage", method = RequestMethod.GET)
    public String loadForm(Model model) {
        model.addAttribute("recipients", accountRepository.findAllProjectedBy());
        return "messageform";
    }

    @RequestMapping(value = "/createmessage", method = RequestMethod.POST)
    public String submitForm(@RequestParam String recipient, @RequestParam String subject, @RequestParam String message, Model model, Principal principal) {

        Message msg = new Message();
        msg.setMessage(message);
        msg.setSubject(subject);
        msg.setAuthor(principal.getName());
        msg.setRecipient(recipient);
        messageRepository.save(msg);
        return "redirect:/messages";
    }

    @RequestMapping(value = "/messages", method = RequestMethod.GET)
    public String messageList(Model model, Principal principal) {
        model.addAttribute("received", messageRepository.findByRecipient(principal.getName()));
        model.addAttribute("sent", messageRepository.findByAuthor(principal.getName()));
        return "messagelist";
    }

    @RequestMapping(value = "/messages/view/{id}", method = RequestMethod.GET)
    public String showMessage(Model model, @PathVariable(value = "id") Long id) {
        /*
        Check here authorized to view the message, must be either author or recipient
        -check author and recipient against principle.getName()
         */
        model.addAttribute("message", messageRepository.findOne(id));
        return "messageview";
    }

    @RequestMapping(value = "/messages/{id}/modify", method = RequestMethod.GET)
    public String modifyMessageView(Model model, @PathVariable(value = "id") Long id) {
        model.addAttribute("message", messageRepository.findOne(id));
        return "modifyView";
    }
    @RequestMapping(value = "/messages/{id}/modify", method = RequestMethod.POST)
    public String modifyMessage(@PathVariable(value = "id") Long id,  @RequestParam(value = "subject") String subject,
                                @RequestParam(value = "message") String message) {

        Message msg = messageRepository.findOne(id);
        msg.setSubject(subject);
        msg.setMessage(message);
        messageRepository.save(msg);
        return "redirect:/messages";
    }

}
