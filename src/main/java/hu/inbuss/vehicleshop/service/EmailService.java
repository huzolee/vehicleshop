package hu.inbuss.vehicleshop.service;

import hu.inbuss.vehicleshop.model.Vehicle;
import java.util.Date;
import java.util.Locale;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring4.SpringTemplateEngine;

/**
 *
 * @author Szecskó Zoltán <zoltan.szecsko@gmail.com>
 */
@Service
@PropertySource("classpath:/mail.properties")
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private SpringTemplateEngine templateEngine;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;
    @Autowired
    private ReloadableResourceBundleMessageSource messageSource;
    private static final String LOCATION = "mail/";
    private static final String MAIL_TEMPLATE = LOCATION + "mail";
    @Value("${mail.from}")
    private String from;
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    public void sendNotificationAboutCarBought(final Vehicle vehicle) throws MessagingException {
        if (mailSender == null) {
            LOGGER.info("");
        } else {
            final Locale locale = RequestContextUtils.getLocale(request);
            final WebContext ctx = new WebContext(request, response, request.getServletContext(), locale);
            final Date dateOfBuy = new Date();

            ctx.setVariable("date", dateOfBuy);
            ctx.setVariable("vehicleName", vehicle.getVehicleName());
            ctx.setVariable("type", vehicle.getType());
            ctx.setVariable("color", vehicle.getColor());
            ctx.setVariable("price", vehicle.getPrice());

            final MimeMessage mimeMessage = mailSender.createMimeMessage();
            final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
            message.setSubject(messageSource.getMessage("mail.subject", null, locale));
            message.setSentDate(dateOfBuy);
            message.setFrom(from);
            message.setTo(vehicle.getCustomer().getEmail());

            final String htmlContent = templateEngine.process(MAIL_TEMPLATE, ctx);
            message.setText(htmlContent, true);

            mailSender.send(mimeMessage);
        }
    }

    public boolean isMailSenderConfigured() {
        return mailSender != null;
    }
}
