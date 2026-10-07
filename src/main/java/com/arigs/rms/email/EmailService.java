package com.arigs.rms.email;

import java.util.Map;

/**
 * Sends HTML emails with variable replacement.
 */
public interface EmailService {

    void sendHtmlEmail(String recipientEmail, String subject, String htmlTemplate, Map<String, String> variables);
}
