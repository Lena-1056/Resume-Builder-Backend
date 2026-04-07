package com.project.airesumebuilder;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.project.airesumebuilder.service.EmailService;

@SpringBootTest
class AiResumebuilderApplicationTests {

    @Autowired
    private EmailService emailService;

    @Test
    void contextLoads() {
    }

    @Test
    void testEmail() {
        try {
            System.out.println("Attempting to send email...");
            emailService.sendHtmlEmail("test@example.com", "Test Subject", "<h1>Test Content</h1>");
            System.out.println("Email sent successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
