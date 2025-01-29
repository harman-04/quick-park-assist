package com.quickparkassist.service;


import java.time.LocalDateTime;
import java.util.UUID;

import com.quickparkassist.model.PasswordResetToken;
import com.quickparkassist.model.User;
import com.quickparkassist.repository.TokenRepository;
import com.quickparkassist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
public class PasswordResetService {


    @Autowired
    UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    private PasswordResetService selfProxy;


    @Transactional(propagation = Propagation.REQUIRED)
    public String sendEmail(User user) {
        try {
            String resetLink = generateResetToken(user);

            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom("harmandeep02315@gmail.com");// input the senders email ID
            msg.setTo(user.getEmail());

            msg.setSubject("Welcome To Quick Parking Spot");
            msg.setText("Hello \n\n" + "Please click on this link to Reset your Password :" + resetLink + ". \n\n"
                    + "Regards \n" + "Harman");

            javaMailSender.send(msg);

            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }

    }


    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteExistingToken(User user) {
        tokenRepository.deleteByUser(user);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public String generateResetToken(User user) {

        deleteExistingToken(user);

        UUID uuid = UUID.randomUUID();
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime expiryDateTime = currentDateTime.plusMinutes(30);

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setUser(user);
        resetToken.setToken(uuid.toString());
        resetToken.setExpiryDateTime(expiryDateTime);
        resetToken.setUser(user);
        PasswordResetToken token = tokenRepository.save(resetToken);
        if (token != null) {
            String endpointUrl = "http://localhost:8082/resetPassword";
            return endpointUrl + "/" + resetToken.getToken();
        }
        return "";
    }


    public boolean hasExipred(LocalDateTime expiryDateTime) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        return expiryDateTime.isBefore(currentDateTime);
    }
}
