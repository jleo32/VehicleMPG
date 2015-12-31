package com.contrastsecurity.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

import com.contrastsecurity.models.Vehicle;
import com.contrastsecurity.services.VehicleService;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/vehicle", method = RequestMethod.GET)
public class VehicleController {

    @Autowired
    VehicleService vehicleService;


    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<List<Vehicle>> getAllVehicles() {

        List<Vehicle> vehicles = vehicleService.getAllVehicles();

        if (vehicles.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(vehicles, HttpStatus.OK);
    }

    @RequestMapping(value = "/make", method = RequestMethod.GET)
    public ResponseEntity<List<Vehicle>> getVehiclesByMake(@RequestParam(value = "make") String make) {
        List<Vehicle> vehicles = vehicleService.findByMake(make);

        if (vehicles.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(vehicles, HttpStatus.OK);
    }

    @RequestMapping(value = "/model", method = RequestMethod.GET)
    public ResponseEntity<List<Vehicle>> getVehiclesByModel(@RequestParam(value = "model") String model) {
        List<Vehicle> vehicles = vehicleService.findByModel(model);

        if (vehicles.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(vehicles, HttpStatus.OK);
    }

    @RequestMapping(value = "/year", method = RequestMethod.GET)
    public ResponseEntity<List<Vehicle>> getVehiclesByYear(@RequestParam(value = "year") int year) {
        List<Vehicle> vehicles = vehicleService.findByYear(year);

        if (vehicles.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(vehicles, HttpStatus.OK);
    }

    // /vehicle/years?from=1990&to=1995
    @RequestMapping(value = "/years", method = RequestMethod.GET)
    public ResponseEntity<List<Vehicle>> getVehiclesBetweenYears(@RequestParam(value = "from") int fromYear,
                                                                 @RequestParam(value = "to") int toYear) {
        List<Vehicle> vehicles = vehicleService.findByYearBetween(fromYear, toYear);

        if (vehicles.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(vehicles, HttpStatus.OK);
    }

    @RequestMapping(value = "/cylinders", method = RequestMethod.GET)
    public ResponseEntity<List<Vehicle>> getVehiclesByModel(@RequestParam(value = "cylinders") int cylinders) {
        List<Vehicle> vehicles = vehicleService.findByCylinders(cylinders);

        if (vehicles.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(vehicles, HttpStatus.OK);
    }

    @RequestMapping(value = "/fuel", method = RequestMethod.GET)
    public ResponseEntity<List<Vehicle>> getVehiclesByFuelType(@RequestParam(value = "type") String type) {
        List<Vehicle> vehicles = vehicleService.findByFuelType(type);

        if (vehicles.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(vehicles, HttpStatus.OK);
    }

}