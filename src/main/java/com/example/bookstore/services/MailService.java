package com.example.bookstore.services;

import com.example.bookstore.domain.Book;
import com.example.bookstore.domain.BrowsingHistory;
import com.example.bookstore.domain.OrderItem;
import com.example.bookstore.domain.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MailService {

    private final Logger log = LoggerFactory.getLogger(MailService.class);
    private final SpringTemplateEngine templateEngine;
    private final JavaMailSender javaMailSender;
    private final BrowsingHistoryService browsingHistoryService;
    private final UserService userService;
    private  final BookService bookService;
    private final String BASE_URL = "http://localhost:8080";
    private  final OrderService orderService;


    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultiPart, boolean isHtml) {
        log.debug("Sending Email");

        MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
            message.setTo(to);
            message.setFrom("noreply@springit.com");
            message.setSubject(subject);
            message.setText(content,isHtml);
            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            log.warn("Email could not be sent to user '{}': {}", to, e.getMessage());
        }
    }

    @Async
    public void sendLowStockAlert(Book book){
        String userEmail = "Admin@example.com"; // Replace with the user's email
        String subject = "Low stock alert";
        String text = "You are running out of  "+ book.getName() +" Quantity is : "+book.getQuantity() ;

        sendEmail(userEmail, subject, text,false,true);
    }

    @Async
    public void sendEmailFromTemplate(UserDetails user, String templateName, String subject) {
        Locale locale = Locale.ENGLISH;
        Context context = new Context(locale);
        context.setVariable("user", user);
        context.setVariable("baseURL",BASE_URL);
        String content = templateEngine.process(templateName,context);
        sendEmail(user.getUsername(),subject,content,false,true);
    }
    @Scheduled(fixedRate = 2 * 60 * 100000) // 2 minutes in milliseconds
    public void sendMostCommonOrderItemEmail() {
        List<Map<String, Object>> mostCommonOrderItem = orderService.findMostCommonOrderItem(1);

        if (!mostCommonOrderItem.isEmpty()) {
            String userEmail = "user@example.com"; // Replace with the user's email
            String subject = "Your Most Common Order Item";
            String text = "Your most common order item is: " + mostCommonOrderItem.get(0).toString();

            sendEmail(userEmail, subject, text,false,true);
        }
    }

    @Scheduled(fixedRate = 2*60*100000)
    public void SendBooksBasedOnUserBrowsing(){
        List<User> users = userService.getAllUsers();
        for(User user: users) {
            BrowsingHistory browsingHistory = browsingHistoryService.getBrowsingHistoryByUserId(user);
            if(browsingHistory != null) {
                String category = browsingHistory.getMostFrequentCategory();
                List<Book> books = bookService.getBooksByGenre(category);

                String userEmail = user.getEmail(); // Replace with the user's email
                String subject = "Recommended Books";
                String text = "Since you seem to like  "+category+" we recommend you to read these books" + books.toString();

                sendEmail(userEmail, subject, text, false, true);
            }
        }

    }

    @Async
    public void sendEmailFromTemplate(User user, String templateName, String subject) {
        Locale locale = Locale.ENGLISH;
        Context context = new Context(locale);
        context.setVariable("user", user);
        context.setVariable("baseURL",BASE_URL);
        String content = templateEngine.process(templateName,context);
        sendEmail(user.getUsername(),subject,content,false,true);
    }

    @Async
    public void sendActivationEmail(User user) {
        log.debug("Sending activation email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "email/activation", "Springit User Activation");
    }

    @Async
    public void sendWelcomeEmail(UserDetails user) {
        log.debug("Sending activation email to '{}'", user.getUsername());
        sendEmailFromTemplate(user, "email/welcome", "Welcome new Bookstore User");
    }


}