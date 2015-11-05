package hu.inbuss.vehicleshop.controller;

import hu.inbuss.vehicleshop.exception.CustomerEMailException;
import hu.inbuss.vehicleshop.model.Customer;
import hu.inbuss.vehicleshop.model.Vehicle;
import hu.inbuss.vehicleshop.repository.CustomerRepository;
import hu.inbuss.vehicleshop.repository.VehicleRepository;
import hu.inbuss.vehicleshop.service.EmailService;
import hu.inbuss.vehicleshop.util.Relatives;
import hu.inbuss.vehicleshop.util.Views;
import java.util.List;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Szecskó Zoltán <zoltan.szecsko@gmail.com>
 */
@Controller
@RequestMapping(value = Relatives.CUSTOMER)
public class CustomerController {

    @Autowired
    private VehicleRepository vehicleRepo;
    @Autowired
    private CustomerRepository customerRepo;
    @Autowired
    private EmailService emailService;
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);

    @RequestMapping(value = Relatives.CUSTOMER_CREATOR_FORM, method = RequestMethod.GET)
    public String showCustomerCreator(final Model model) {
        LOGGER.info("showCustomerCreator()");
        model.addAttribute(new Customer());
        return Views.CUSTOMER_CREATOR;
    }

    @RequestMapping(value = Relatives.CUSTOMER_CREATE, method = RequestMethod.POST)
    public String createCustomer(@ModelAttribute @Valid final Customer customer, final Errors errors) {
        LOGGER.info("createCustomer()");
        if (errors.hasErrors()) {
            LOGGER.info("createCustomer(): Count of field's errors: {}", errors.getErrorCount());
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("createCustomer(): {}", errors);
            }
            return Views.CUSTOMER_CREATOR;
        }

        if (customerRepo.findByEmail(customer.getEmail()) != null) {
            LOGGER.info("createCustomer(): email address is already used");
            throw new CustomerEMailException("email address is already used");
        }

        customerRepo.save(customer);

        LOGGER.info("createCustomer(): customer is saved");
        LOGGER.debug("createCustomer(): {}", customer);
        return Views.REDIRECT_TO_CUSTOMER_LIST;
    }

    @RequestMapping(value = Relatives.CUSTOMER_LIST, method = RequestMethod.GET)
    public ModelAndView listCustomers(final ModelAndView modelAndView) {
        LOGGER.info("listCustomers()");
        final List<Customer> customers = customerRepo.findByStatus(Customer.Status.ACTIVE);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("listCustomers(): customers = {}", customers);
        }
        modelAndView.addObject("customers", customers);
        modelAndView.setViewName(Views.CUSTOMER_LIST);

        return modelAndView;
    }

    @RequestMapping(value = Relatives.CUSTOMER_DELETE, method = RequestMethod.GET)
    public String deleteCustomer(@PathVariable("customerId") final long customerId) {
        LOGGER.info("deleteCustomer()");
        LOGGER.debug("deleteCustomer(): customerId = {}", customerId);
        final Customer customer = customerRepo.findOne(customerId);
        customer.setStatus(Customer.Status.INACTIVE);
        customerRepo.save(customer);
        return Views.REDIRECT_TO_CUSTOMER_LIST;
    }

    @RequestMapping(value = Relatives.CUSTOMER_MODIFY, method = RequestMethod.GET)
    public ModelAndView modifyCustomer(@PathVariable("customerId") final long customerId, final ModelAndView modelAndView) {
        LOGGER.info("modifyCustomer()");
        final Customer customer = customerRepo.findOne(customerId);
        LOGGER.debug("modifyCustomer(): customer = {}", customer);
        modelAndView.addObject("customer", customer);
        modelAndView.setViewName(Views.CUSTOMER_CREATOR);
        return modelAndView;
    }

    @RequestMapping(value = Relatives.CUSTOMER_BUY, method = RequestMethod.POST)
    public String buyVehicle(@ModelAttribute final Vehicle vehicle,
            final BindingResult result, final HttpServletRequest request) throws MessagingException {
        LOGGER.info("buyVehicle()");
        LOGGER.debug("buyVehicle(): customer = {}, vehicle = {}", vehicle.getCustomer(), vehicle);
        vehicle.setStatus(Vehicle.Status.SOLD);
        vehicleRepo.save(vehicle);
        if (emailService.isMailSenderConfigured()) {
            emailService.sendNotificationAboutCarBought(vehicle);
        }
        return Views.REDIRECT_TO_CUSTOMER_LIST;
    }

    @RequestMapping(value = Relatives.CUSTOMER_DETAILS, method = RequestMethod.GET)
    public ModelAndView listOwnerById(@PathVariable final long customerId, final ModelAndView modelAndView) {
        LOGGER.info("listOwnerById()");
        final List<Vehicle> vehicles = vehicleRepo.findByCustomerId(customerId);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("listOwnerById(): customerId = {},vehicles = {}", customerId, vehicles);
        }
        modelAndView.addObject("vehicles", vehicles);
        modelAndView.setViewName(Views.CUSTOMER_DETAILED_LIST);
        return modelAndView;
    }
}
