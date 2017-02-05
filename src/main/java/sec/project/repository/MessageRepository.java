package sec.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sec.project.domain.Account;
import sec.project.domain.Message;
import sec.project.domain.Signup;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByRecipient(String recipient);
    List<Message> findByAuthor(String author);

}
