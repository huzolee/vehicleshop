package hu.inbuss.vehicleshop.service;

import hu.inbuss.vehicleshop.service.EmailService;
import hu.inbuss.vehicleshop.controller.TestClass;
import hu.inbuss.vehicleshop.model.Customer;
import hu.inbuss.vehicleshop.model.Vehicle;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.jvnet.mock_javamail.Mailbox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 *
 * @author Szecskó Zoltán <zoltan.szecsko@gmail.com>
 */
public class EmailServiceTest extends TestClass {

    @Autowired
    private EmailService emailService;
    @Autowired
    private ReloadableResourceBundleMessageSource messageSource;

    @Before
    public void setUp() {
        Mailbox.clearAll();
    }

    @Test
    public void testSendEmail() throws AddressException, MessagingException, IOException {
        final Vehicle vehicle = new Vehicle("Ford", 4, "red", 500000, Vehicle.Type.CAR);
        final Customer customer = new Customer("mailTestUser", "testuser@cardealertest.com");
        vehicle.setCustomer(customer);

        Assume.assumeTrue(emailService.isMailSenderConfigured());

        emailService.sendNotificationAboutCarBought(vehicle);
        final List<Message> inbox = Mailbox.get(vehicle.getCustomer().getEmail());
        assertTrue(inbox.size() == 1);
        assertEquals(messageSource.getMessage("mail.subject", null, Locale.ENGLISH), inbox.get(0).getSubject());
    }
}
