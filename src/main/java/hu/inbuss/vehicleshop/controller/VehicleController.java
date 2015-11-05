package hu.inbuss.vehicleshop.controller;

import hu.inbuss.vehicleshop.model.Customer;
import hu.inbuss.vehicleshop.model.Vehicle;
import hu.inbuss.vehicleshop.model.Vehicle.Type;
import hu.inbuss.vehicleshop.repository.CustomerRepository;
import hu.inbuss.vehicleshop.repository.VehicleRepository;
import hu.inbuss.vehicleshop.util.Relatives;
import hu.inbuss.vehicleshop.util.Views;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
@RequestMapping(value = Relatives.VEHICLE)
public class VehicleController {

    @Autowired
    private VehicleRepository vehicleRepo;
    @Autowired
    private CustomerRepository customerRepo;
    private static final Logger LOGGER = LoggerFactory.getLogger(VehicleController.class);

    @RequestMapping(value = Relatives.VEHICLE_CREATE, method = RequestMethod.POST)
    public String createVehicle(@ModelAttribute @Valid final Vehicle vehicle, final Errors errors) {
        LOGGER.info("createVehicle()");
        if (errors.hasErrors()) {
            LOGGER.info("createVehicle(): Count of field's errors: {}", errors.getErrorCount());
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("createVehicle(): {}", errors);
            }
            return Views.VEHICLE_CREATOR;
        }
        vehicleRepo.save(vehicle);
        LOGGER.info("createVehicle(): vehicle is saved");
        LOGGER.debug("createVehicle(): {}", vehicle);
        return Views.REDIRECT_TO_VEHICLE_LIST;
    }

    @RequestMapping(value = Relatives.VEHICLE_CREATOR_FORM, method = RequestMethod.GET)
    public String showVehicleCreator(final Model model) {
        LOGGER.info("showVehicleCreator()");
        model.addAttribute(new Vehicle());
        return Views.VEHICLE_CREATOR;
    }

    @RequestMapping(value = Relatives.VEHICLE_LIST, method = RequestMethod.GET)
    public ModelAndView listVehicles(final ModelAndView modelAndView) {
        LOGGER.info("listVehicles()");
        final List<Vehicle> vehicles = vehicleRepo.findByStatus(Vehicle.Status.FOR_SALE);
        modelAndView.addObject("vehicles", vehicles);
        modelAndView.setViewName(Views.VEHICLE_LIST);
        return modelAndView;
    }

    @RequestMapping(value = Relatives.VEHICLE_DELETE, method = RequestMethod.GET)
    public String deleteVehicle(@PathVariable("vehicleId") final long id) {
        LOGGER.info("deleteVehicle()");
        LOGGER.debug("deleteVehicle(): vehicleId = {}", id);
        vehicleRepo.delete(id);
        return Views.REDIRECT_TO_VEHICLE_LIST;
    }

    @RequestMapping(value = Relatives.VEHICLE_MODIFY, method = RequestMethod.GET)
    public ModelAndView modifyVehicle(@PathVariable("vehicleId") final long vehicleId, final ModelAndView modelAndView) {
        LOGGER.info("modifyVehicle()");
        final Vehicle vehicle = vehicleRepo.findOne(vehicleId);
        LOGGER.debug("modifyVehicle(): vehicle = {}", vehicle);
        modelAndView.addObject("vehicle", vehicle);
        modelAndView.setViewName(Views.VEHICLE_CREATOR);
        return modelAndView;
    }

    @RequestMapping(value = Relatives.VEHICLE_SELL, method = RequestMethod.GET)
    public ModelAndView sellVehicle(@PathVariable("vehicleId") final long vehicleId, final ModelAndView modelAndView) {
        LOGGER.info("sellVehicle()");
        final Vehicle vehicle = vehicleRepo.findOne(vehicleId);
        final List<Customer> customers = customerRepo.findAll();
        LOGGER.debug("sellVehicle(): vehicleId = {}", vehicleId);
        LOGGER.debug("sellVehicle(): vehicle = {}", vehicle);
        modelAndView.addObject("vehicle", vehicle);
        modelAndView.addObject("customers", customers);
        modelAndView.setViewName(Views.VEHICLE_SELLER);
        return modelAndView;
    }

    @ModelAttribute("allTypes")
    public List<Type> allTypes() {
        return Arrays.asList(Type.values());
    }

    @PostConstruct
    public void initializeVehicles() {
        vehicleRepo.save(new Vehicle("Ford", 4, "Red", 500000, Vehicle.Type.CAR));
        vehicleRepo.save(new Vehicle("BMW", 4, "Blue", 3500000, Vehicle.Type.CAR));
        vehicleRepo.save(new Vehicle("KTM", 2, "Orange", 1000000, Vehicle.Type.BIKE));
        vehicleRepo.save(new Vehicle("Hyundai", 4, "White", 1500000, Vehicle.Type.CAR));

        customerRepo.save(new Customer("Teszt Elek", "huzolee89@gmail.com"));
        customerRepo.save(new Customer("Teszt Elek1", "wkrix89@gmail.com"));
    }
}
