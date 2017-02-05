package sec.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sec.project.domain.Message;

import java.security.Principal;
import java.sql.*;
import java.util.ArrayList;

@Controller
public class SearchController {


    @RequestMapping(value = "/searchform", method = RequestMethod.GET)
    public String searchForm() {
        return "searchform";
    }


    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String search(Principal principal, Model model, @RequestParam("searchterm") String searchString) {
        ArrayList msgList = new ArrayList();
        try {

            Class.forName("org.h2.Driver");
            Connection conn = DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "");
            Statement stmnt = conn.createStatement();
            //String sql = "SELECT subject,message FROM message WHERE subject='" + searchString + "'";
            String sql = "SELECT subject,message FROM message WHERE (author= '" + principal.getName() + "' OR  recipient= '" + principal.getName() + "') AND  message LIKE '%" + searchString + "%'";
            System.out.println(sql);

            ResultSet rs = stmnt.executeQuery(sql);
            while (rs.next()) {
                String subject = rs.getString("subject");
                String message = rs.getString("message");
                Message msg = new Message();
                msg.setSubject(subject);
                msg.setMessage(message);
                msgList.add(msg);
            }
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("messages", msgList);
        return "searchresult";
    }

}
