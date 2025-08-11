package com.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {

    private final JavaMailSender emailSender;

    @Autowired
    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendResetPasswordEmail(String to, String resetLink) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setTo(to);
            helper.setSubject("NestMart: Password Reset Request");
            helper.setText(createPasswordResetEmailTemplate(resetLink), true);
            
            emailSender.send(message);
        } catch (MessagingException e) {
            SimpleMailMessage fallbackMessage = new SimpleMailMessage();
            fallbackMessage.setTo(to);
            fallbackMessage.setSubject("NestMart: Password Reset Request");
            fallbackMessage.setText("To reset your password, click the following link: " + resetLink);
            emailSender.send(fallbackMessage);
        }
    }
    
    private String createPasswordResetEmailTemplate(String resetLink) {
        return "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
            "    <title>Password Reset - NestMart</title>\n" +
            "    <style>\n" +
            "        body {\n" +
            "            margin: 0;\n" +
            "            padding: 0;\n" +
            "            font-family: 'Cairo', 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;\n" +
            "            line-height: 1.6;\n" +
            "            color: #333333;\n" +
            "            background-color: #f4f4f4;\n" +
            "        }\n" +
            "        .container {\n" +
            "            max-width: 600px;\n" +
            "            margin: 0 auto;\n" +
            "            background-color: #ffffff;\n" +
            "            border-radius: 10px;\n" +
            "            overflow: hidden;\n" +
            "            box-shadow: 0 0 20px rgba(0,0,0,0.1);\n" +
            "        }\n" +
            "        .header {\n" +
            "            background: linear-gradient(135deg, #ff9702 0%, #ff8a00 100%);\n" +
            "            padding: 30px 40px;\n" +
            "            text-align: center;\n" +
            "        }\n" +
            "        .header h1 {\n" +
            "            color: #ffffff;\n" +
            "            margin: 0;\n" +
            "            font-size: 28px;\n" +
            "            font-weight: 700;\n" +
            "            text-shadow: 0 2px 4px rgba(0,0,0,0.2);\n" +
            "        }\n" +
            "        .logo {\n" +
            "            font-size: 32px;\n" +
            "            font-weight: bold;\n" +
            "            color: #ffffff;\n" +
            "            margin-bottom: 10px;\n" +
            "            text-decoration: none;\n" +
            "        }\n" +
            "        .content {\n" +
            "            padding: 40px;\n" +
            "        }\n" +
            "        .greeting {\n" +
            "            font-size: 18px;\n" +
            "            color: #333333;\n" +
            "            margin-bottom: 20px;\n" +
            "        }\n" +
            "        .message {\n" +
            "            font-size: 16px;\n" +
            "            color: #555555;\n" +
            "            margin-bottom: 30px;\n" +
            "            line-height: 1.8;\n" +
            "        }\n" +
            "        .cta-button {\n" +
            "            display: inline-block;\n" +
            "            background: linear-gradient(135deg, #ff9702 0%, #ff8a00 100%);\n" +
            "            color: #ffffff !important;\n" +
            "            text-decoration: none;\n" +
            "            padding: 15px 30px;\n" +
            "            border-radius: 8px;\n" +
            "            font-size: 18px;\n" +
            "            font-weight: 600;\n" +
            "            text-align: center;\n" +
            "            margin: 20px 0;\n" +
            "            transition: all 0.3s ease;\n" +
            "            box-shadow: 0 4px 15px rgba(255, 151, 2, 0.3);\n" +
            "        }\n" +
            "        .cta-button:hover {\n" +
            "            background: linear-gradient(135deg, #e6870d 0%, #d47500 100%);\n" +
            "            transform: translateY(-2px);\n" +
            "            box-shadow: 0 6px 20px rgba(255, 151, 2, 0.4);\n" +
            "        }\n" +
            "        .security-note {\n" +
            "            background-color: #f8f9fa;\n" +
            "            border-left: 4px solid #ff9702;\n" +
            "            padding: 15px 20px;\n" +
            "            margin: 25px 0;\n" +
            "            border-radius: 0 8px 8px 0;\n" +
            "        }\n" +
            "        .security-note p {\n" +
            "            margin: 0;\n" +
            "            font-size: 14px;\n" +
            "            color: #666666;\n" +
            "        }\n" +
            "        .footer {\n" +
            "            background-color: #2c3e50;\n" +
            "            color: #ffffff;\n" +
            "            padding: 30px 40px;\n" +
            "            text-align: center;\n" +
            "        }\n" +
            "        .footer p {\n" +
            "            margin: 0 0 10px 0;\n" +
            "            font-size: 14px;\n" +
            "        }\n" +
            "        .footer .contact-info {\n" +
            "            margin-top: 15px;\n" +
            "            font-size: 12px;\n" +
            "            color: #bdc3c7;\n" +
            "        }\n" +
            "        .divider {\n" +
            "            height: 1px;\n" +
            "            background: linear-gradient(90deg, transparent 0%, #ff9702 50%, transparent 100%);\n" +
            "            margin: 30px 0;\n" +
            "        }\n" +
            "        @media screen and (max-width: 600px) {\n" +
            "            .container {\n" +
            "                margin: 10px;\n" +
            "                width: auto;\n" +
            "            }\n" +
            "            .content, .header, .footer {\n" +
            "                padding: 20px;\n" +
            "            }\n" +
            "            .header h1 {\n" +
            "                font-size: 24px;\n" +
            "            }\n" +
            "            .cta-button {\n" +
            "                display: block;\n" +
            "                width: 100%;\n" +
            "                padding: 12px;\n" +
            "                font-size: 16px;\n" +
            "            }\n" +
            "        }\n" +
            "    </style>\n" +
            "</head>\n" +
            "<body>\n" +
            "    <div class=\"container\">\n" +
            "        <div class=\"header\">\n" +
            "            <div class=\"logo\">NestMart</div>\n" +
            "            <h1>Password Reset Request</h1>\n" +
            "        </div>\n" +
            "        \n" +
            "        <div class=\"content\">\n" +
            "            <div class=\"greeting\">\n" +
            "                Hello there! 👋\n" +
            "            </div>\n" +
            "            \n" +
            "            <div class=\"message\">\n" +
            "                We received a request to reset your password for your NestMart account. \n" +
            "                If you made this request, please click the button below to create a new password.\n" +
            "            </div>\n" +
            "            \n" +
            "            <div style=\"text-align: center; margin: 30px 0;\">\n" +
            "                <a href=\"" + resetLink + "\" class=\"cta-button\">\n" +
            "                    🔒 Reset My Password\n" +
            "                </a>\n" +
            "            </div>\n" +
            "            \n" +
            "            <div class=\"security-note\">\n" +
            "                <p><strong>🛡️ Security Notice:</strong></p>\n" +
            "                <p>If you didn't request this password reset, please ignore this email. \n" +
            "                Your password will remain unchanged and your account stays secure.</p>\n" +
            "                <p style=\"margin-top: 10px;\">This link will expire in 24 hours for your security.</p>\n" +
            "            </div>\n" +
            "            \n" +
            "            <div class=\"divider\"></div>\n" +
            "            \n" +
            "            <p style=\"font-size: 14px; color: #777; text-align: center;\">\n" +
            "                If the button doesn't work, you can copy and paste this link into your browser:\n" +
            "                <br><br>\n" +
            "                <span style=\"word-break: break-all; color: #ff9702;\">" + resetLink + "</span>\n" +
            "            </p>\n" +
            "        </div>\n" +
            "        \n" +
            "        <div class=\"footer\">\n" +
            "            <p><strong>NestMart - Quality Organic Products</strong></p>\n" +
            "            <p>\"Quality is not just our promise, it's what makes our brand stand out.\"</p>\n" +
            "            \n" +
            "            <div class=\"contact-info\">\n" +
            "                📧 nestmart@gmail.com | 📞 (+84) 123 456 789\n" +
            "                <br>\n" +
            "                🕒 Mon-Sun: 8:00am-10:00pm\n" +
            "                <br>\n" +
            "                📍 7563 St. Vicent Place, Glasgow, Greater Newyork NH7689, UK\n" +
            "            </div>\n" +
            "        </div>\n" +
            "    </div>\n" +
            "</body>\n" +
            "</html>";
    }

    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }
}
