package hu.inbuss.vehicleshop.configuration;

import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 *
 * @author Szecskó Zoltán <zoltan.szecsko@gmail.com>
 */
@Configuration
@PropertySource("classpath:mail.properties")
public class MailConfig {

    @Value("${mail.protocol}")
    private String protocol;
    @Value("${mail.host}")
    private String host;
    @Value("${mail.port}")
    private String port;
    @Value("${mail.smtp.auth}")
    private boolean auth;
    @Value("${mail.smtp.starttls.enable}")
    private boolean starttls;
    @Value("${mail.username}")
    private String username;
    @Value("${mail.password}")
    private String password;
    private static final Logger LOGGER = LoggerFactory.getLogger(MailConfig.class);

    @Bean
    public JavaMailSender javaMailSender() {
        if (host == null || host.isEmpty() || port == null || port.isEmpty()) {
            LOGGER.info("JavaMailSender is not configured properly.");
            return null;
        }

        final JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        final Properties mailProperties = new Properties();
        mailProperties.put("mail.smtp.auth", auth);
        mailProperties.put("mail.smtp.starttls.enable", starttls);
        mailSender.setJavaMailProperties(mailProperties);
        mailSender.setHost(host);
        try {
            mailSender.setPort(Integer.parseInt(port));
        } catch (NumberFormatException nfex) {
            LOGGER.info("Bad config. Port: " + port, nfex);
            return null;
        }
        mailSender.setProtocol(protocol);
        mailSender.setUsername(username);
        mailSender.setPassword(password);
        return mailSender;
    }
}
