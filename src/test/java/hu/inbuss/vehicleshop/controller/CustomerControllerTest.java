package hu.inbuss.vehicleshop.controller;

import hu.inbuss.vehicleshop.model.Customer;
import hu.inbuss.vehicleshop.model.Vehicle;
import hu.inbuss.vehicleshop.repository.CustomerRepository;
import hu.inbuss.vehicleshop.repository.VehicleRepository;
import hu.inbuss.vehicleshop.service.UserService;
import hu.inbuss.vehicleshop.util.Relatives;
import static hu.inbuss.vehicleshop.util.Relatives.CUSTOMER;
import static hu.inbuss.vehicleshop.util.Relatives.CUSTOMER_CREATE;
import static hu.inbuss.vehicleshop.util.Relatives.CUSTOMER_CREATOR_FORM;
import static hu.inbuss.vehicleshop.util.Relatives.CUSTOMER_DELETE;
import static hu.inbuss.vehicleshop.util.Relatives.CUSTOMER_DETAILS;
import static hu.inbuss.vehicleshop.util.Relatives.CUSTOMER_LIST;
import static hu.inbuss.vehicleshop.util.Relatives.CUSTOMER_MODIFY;
import hu.inbuss.vehicleshop.util.Roles;
import hu.inbuss.vehicleshop.util.Util;
import hu.inbuss.vehicleshop.util.Views;
import java.util.List;
import junit.framework.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Szecskó Zoltán <zoltan.szecsko@gmail.com>
 */
public class CustomerControllerTest extends TestClass {

    @Autowired
    private UserService userService;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private VehicleRepository vehicleRepository;

    @Test
    public void testShowCustomerCreatorPage() throws Exception {
        final MockHttpSession session = Util.createSessionForSeller("testuser", "test", Roles.SELLER, userService, mockMVC);
        final MockHttpServletRequestBuilder rb = MockMvcRequestBuilders.get(CUSTOMER + CUSTOMER_CREATOR_FORM).session(session);
        final ModelAndView mav = mockMVC.perform(rb).andReturn().getModelAndView();
        final String customerView = (String) mav.getModelMap().get("view");
        Assert.assertTrue(Views.CUSTOMER_CREATOR.equals(customerView));
    }

    @Test
    public void testCreateCustomer() throws Exception {
        final MockHttpSession session = Util.createSessionForSeller("testuser2", "test", Roles.SELLER, userService, mockMVC);
        final MockHttpServletRequestBuilder rb = MockMvcRequestBuilders.post(CUSTOMER + CUSTOMER_CREATE)
                .param("name", "customer1")
                .param("email", "test123456789@email.com")
                .session(session);

        mockMVC.perform(rb);
        Assert.assertNotNull(customerRepository.findByName("customer1"));
        Assert.assertNotNull(customerRepository.findByEmail("test123456789@email.com"));
    }

    @Test
    public void testMissingCustomerName() throws Exception {
        final MockHttpSession session = Util.createSessionForSeller("testuser3", "test", Roles.SELLER, userService, mockMVC);
        final MockHttpServletRequestBuilder rb = MockMvcRequestBuilders.post(CUSTOMER + CUSTOMER_CREATE)
                .param("name", "")
                .param("email", "test123456789@email.com")
                .session(session);

        mockMVC.perform(rb).andExpect(model().hasErrors());
    }

    @Test
    public void testMissingCustomerEmail() throws Exception {
        final MockHttpSession session = Util.createSessionForSeller("testuser4", "test", Roles.SELLER, userService, mockMVC);
        final MockHttpServletRequestBuilder rb = MockMvcRequestBuilders.post(CUSTOMER + CUSTOMER_CREATE)
                .param("name", "customer2")
                .param("email", "")
                .session(session);

        mockMVC.perform(rb).andExpect(model().hasErrors());
    }

    @Test
    public void testNotValidEmail() throws Exception {
        final MockHttpSession session = Util.createSessionForSeller("testuser5", "test", Roles.SELLER, userService, mockMVC);
        final MockHttpServletRequestBuilder rb = MockMvcRequestBuilders.post(CUSTOMER + CUSTOMER_CREATE)
                .param("name", "customer2")
                .param("email", "asdf")
                .session(session);

        mockMVC.perform(rb).andExpect(model().hasErrors());
    }

    @Test
    public void testModifyCustomer() throws Exception {
        final String name = "customerToEdit";
        final String email = "test123456789@mail.com";
        final MockHttpSession session = Util.createSessionForSeller("testuser6", "test", Roles.SELLER, userService, mockMVC);
        final MockHttpServletRequestBuilder rb = MockMvcRequestBuilders.post(CUSTOMER + CUSTOMER_CREATE)
                .param("name", name)
                .param("email", email)
                .session(session);

        mockMVC.perform(rb);

        final long customerId = customerRepository.findByName("customerToEdit").getId();
        final MockHttpServletRequestBuilder rb2 = MockMvcRequestBuilders
                .get(CUSTOMER + Relatives.getRelativeWithParam(CUSTOMER_MODIFY, String.valueOf(customerId)))
                .session(session);

        final ModelAndView mav = mockMVC.perform(rb2).andReturn().getModelAndView();
        final Customer customer = (Customer) mav.getModel().get("customer");
        final String customerView = (String) mav.getModelMap().get("view");

        Assert.assertTrue(Views.CUSTOMER_CREATOR.equals(customerView));
        Assert.assertEquals(customerId, customer.getId());
        Assert.assertEquals(name, customer.getName());
        Assert.assertEquals(email, customer.getEmail());
    }

    @Test
    public void testAlreadyExistEmail() throws Exception {
        final MockHttpSession session = Util.createSessionForSeller("testuser7", "test", Roles.SELLER, userService, mockMVC);
        final MockHttpServletRequestBuilder rb1 = MockMvcRequestBuilders.post(CUSTOMER + CUSTOMER_CREATE)
                .param("name", "customer3")
                .param("email", "test1@mail.com")
                .session(session);

        mockMVC.perform(rb1);

        final MockHttpServletRequestBuilder rb2 = MockMvcRequestBuilders.post(CUSTOMER + CUSTOMER_CREATE)
                .param("name", "customer4")
                .param("email", "test1@mail.com")
                .session(session);

        mockMVC.perform(rb2).andExpect(MockMvcResultMatchers.model().attributeExists("error"));
    }

    @Test
    public void testListCustomers() throws Exception {
        final MockHttpSession session = Util.createSessionForSeller("testuser10", "test", Roles.SELLER, userService, mockMVC);
        final MockHttpServletRequestBuilder rb = MockMvcRequestBuilders.get(CUSTOMER + CUSTOMER_LIST)
                .session(session);
        final ModelAndView mav = mockMVC.perform(rb).andReturn().getModelAndView();
        final List<Customer> customerListInView = (List<Customer>) mav.getModel().get("customers");
        final List<Customer> expectedList = customerRepository.findAll();
        Assert.assertTrue(customerListInView.equals(expectedList));
    }

    @Test
    public void testDeleteCustomer() throws Exception {
        final MockHttpSession session = Util.createSessionForSeller("testuser11", "test", Roles.SELLER, userService, mockMVC);
        final Customer customer = new Customer("delCust", "delCust@ex.rr");
        customerRepository.save(customer);

        final MockHttpServletRequestBuilder rb
                = MockMvcRequestBuilders.get(CUSTOMER + Relatives.getRelativeWithParam(CUSTOMER_DELETE, String.valueOf(customer.getId())))
                .session(session);
        mockMVC.perform(rb);

        final Customer c = customerRepository.findOne(customer.getId());
        Assert.assertTrue(c.getStatus().equals(Customer.Status.INACTIVE));
    }

    @Test
    public void testListOwnerById() throws Exception {
        final MockHttpSession session = Util.createSessionForSeller("testuser12", "test", Roles.SELLER, userService, mockMVC);
        final Customer customer = new Customer("customerX", "delCustX@ex.rr");
        customerRepository.save(customer);

        for (int i = 0; i <= 1; i++) {
            final Vehicle vehicle = new Vehicle("FordX" + i, 4, "red", 1000000, Vehicle.Type.CAR);
            vehicle.setCustomer(customer);
            vehicleRepository.save(vehicle);
        }

        final MockHttpServletRequestBuilder rb
                = MockMvcRequestBuilders.get(CUSTOMER + Relatives.getRelativeWithParam(CUSTOMER_DETAILS, String.valueOf(customer.getId())))
                .session(session);

        final ModelAndView mav = mockMVC.perform(rb).andReturn().getModelAndView();
        final List<Vehicle> vehicleListInView = (List<Vehicle>) mav.getModel().get("vehicles");
        final List<Vehicle> expectedList = vehicleRepository.findByCustomerId(customer.getId());
        Assert.assertTrue(vehicleListInView.equals(expectedList));
    }
}
