package hu.inbuss.vehicleshop.controller;

import hu.inbuss.vehicleshop.model.Vehicle;
import hu.inbuss.vehicleshop.repository.VehicleRepository;
import hu.inbuss.vehicleshop.service.UserService;
import hu.inbuss.vehicleshop.util.Relatives;
import static hu.inbuss.vehicleshop.util.Relatives.VEHICLE;
import static hu.inbuss.vehicleshop.util.Relatives.VEHICLE_CREATE;
import static hu.inbuss.vehicleshop.util.Relatives.VEHICLE_CREATOR_FORM;
import static hu.inbuss.vehicleshop.util.Relatives.VEHICLE_DELETE;
import static hu.inbuss.vehicleshop.util.Relatives.VEHICLE_LIST;
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
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Szecskó Zoltán <zoltan.szecsko@gmail.com>
 */
public class VehicleControllerTest extends TestClass {

    @Autowired
    private UserService userService;
    @Autowired
    private VehicleRepository vehicleRepo;

    @Test
    public void testShowVehicleCreatorPage() throws Exception {
        final MockHttpSession session = Util.createSessionForSeller("vehicletestuser", "test", Roles.SELLER, userService, mockMVC);
        final MockHttpServletRequestBuilder rb = MockMvcRequestBuilders.get(VEHICLE + VEHICLE_CREATOR_FORM).session(session);
        final ModelAndView mav = mockMVC.perform(rb).andReturn().getModelAndView();
        final String customerView = (String) mav.getModelMap().get("view");
        Assert.assertTrue(Views.VEHICLE_CREATOR.equals(customerView));
    }

    @Test
    public void testCreateVehicle() throws Exception {
        final MockHttpSession session = Util.createSessionForSeller("vehicletestuser2", "test", Roles.SELLER, userService, mockMVC);
        final Vehicle car = new Vehicle("Ford", 4, "red", 1000000, Vehicle.Type.CAR);
        final MockHttpServletRequestBuilder rb = MockMvcRequestBuilders.post(VEHICLE + VEHICLE_CREATE)
                .param("vehicleName", car.getVehicleName())
                .param("numberOfWheels", String.valueOf(car.getNumberOfWheels()))
                .param("color", car.getColor())
                .param("price", String.valueOf(car.getPrice()))
                .param("type", car.getType().toString())
                .session(session);

        mockMVC.perform(rb);
        Assert.assertNotNull(vehicleRepo.findByVehicleName(car.getVehicleName()));
    }

    @Test
    public void testListVehicles() throws Exception {
        final MockHttpSession session = Util.createSessionForSeller("vehicletestuser3", "test", Roles.SELLER, userService, mockMVC);
        final MockHttpServletRequestBuilder rb = MockMvcRequestBuilders.get(VEHICLE + VEHICLE_LIST)
                .session(session);
        final ModelAndView mav = mockMVC.perform(rb).andReturn().getModelAndView();
        final List<Vehicle> vehicleListInView = (List<Vehicle>) mav.getModel().get("vehicles");
        final List<Vehicle> expectedList = vehicleRepo.findAll();
        Assert.assertTrue(vehicleListInView.equals(expectedList));
    }

    @Test
    public void testDeleteVehicle() throws Exception {
        final MockHttpSession session = Util.createSessionForSeller("vehicletestuser4", "test", Roles.SELLER, userService, mockMVC);
        final Vehicle car = new Vehicle("Ford4", 4, "red", 1000000, Vehicle.Type.CAR);
        vehicleRepo.save(car);

        final MockHttpServletRequestBuilder rb
                = MockMvcRequestBuilders.get(VEHICLE + Relatives.getRelativeWithParam(VEHICLE_DELETE, String.valueOf(car.getId())))
                .session(session);
        mockMVC.perform(rb);

        Assert.assertNull(vehicleRepo.findOne(car.getId()));
    }
}
