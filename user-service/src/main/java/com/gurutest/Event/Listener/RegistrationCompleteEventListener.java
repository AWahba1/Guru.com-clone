package com.gurutest.Event.Listener;

import com.gurutest.Event.RegistrationCompleteEvent;
import com.gurutest.Users.UserService;
import com.gurutest.Users.Users;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor

public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {
  private final UserService userService;
  private final JavaMailSender mailSender;
  private Users theUser;
    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        // 1 Get the newly registered user
         theUser=event.getUser();
        // 2 Create a verification token for the user
        String verToken= UUID.randomUUID().toString();
        // 3 save the verification token for the user
        userService.saveUserVerificationToken(theUser,verToken);

        // 4 build the verification url to be sent for the user
        String url=event.getApplicationURL()+"/signup/verifyEmail?token="+verToken;
//        // 5 send the email
        try {
            sendVerificationEmail(url);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        log.info("Click to verify your email : {}",url);
    }
    public void sendVerificationEmail(String url) throws MessagingException, UnsupportedEncodingException {
        String Subject="Verification Email";
        String SenderName="Guru.com";
        String mailContent="<p> Hi, "+ theUser.getFullname()+ ", </p>"+
                "<p>Thank you for registering with us,"+ " " +
                "Please, follow the link below to complete your registration. </p>"+
                "<a href=\"" +url+ "\">Verify your email to activate your account</a>"+
                "<p> Thank you "+" "+"‹br> Users Registration Portal Service";

        emailMessage(Subject, SenderName, mailContent);
    }

    private void emailMessage(String subject, String senderName, String mailContent) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender .createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper. setFrom("basmala.mahmoud498@gmail.com", senderName) ;
        messageHelper .setTo(theUser.getEmail());
        messageHelper.setSubject(subject);
        messageHelper. setText (mailContent, true);
        mailSender. send (message);
    }

    public void sendVerificationPassword(String url, Optional<Users> theUser) throws MessagingException, UnsupportedEncodingException {
       System.out.println(theUser+"><<<<<<<<<");
        String Subject="Password Reset Link";
        String SenderName="Guru.com";
        String mailContent="<p> Hi, "+ theUser.get().getFullname()+ ", </p>"+
                "Please, follow the link below to reset your password. </p>"+
                "<a href=\"" +url+ "\">Reset Password</a>"+
                "<p> Thank you "+" "+"‹br> Users Registration Portal Service";

      //  emailMessage(Subject, SenderName, mailContent);
        MimeMessage message = mailSender .createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper. setFrom("basmala.mahmoud498@gmail.com", SenderName) ;
        messageHelper .setTo(theUser.get().getEmail());
        messageHelper.setSubject(Subject);
        messageHelper. setText (mailContent, true);
        mailSender. send (message);
    }
}
