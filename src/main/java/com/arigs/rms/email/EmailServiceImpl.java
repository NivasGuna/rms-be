package com.arigs.rms.email;

import jakarta.mail.internet.MimeMessage;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Spring Mail implementation for HTML email delivery.
 */
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendHtmlEmail(String recipientEmail, String subject, String htmlTemplate, Map<String, String> variables) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
            helper.setTo(recipientEmail);
            helper.setSubject(subject);
            helper.setText(render(htmlTemplate, variables), true);
            mailSender.send(message);
        } catch (Exception ex) {
            throw new IllegalStateException("Email delivery failed", ex);
        }
    }

    private String render(String template, Map<String, String> variables) {
        String rendered = template;
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            rendered = rendered.replace("{{" + entry.getKey() + "}}",
                    StringUtils.hasText(entry.getValue()) ? entry.getValue() : "");
        }
        return rendered;
    }
}
