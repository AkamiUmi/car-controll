package uz.asbt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import uz.asbt.model.CarData;
import uz.asbt.model.CarInfo;
import uz.asbt.service.CarViolationsService;

import java.util.List;

@RestController
public class CarViolationsController {
    private final CarViolationsService carViolationsService;
    @Autowired
    public CarViolationsController(CarViolationsService carViolationsService) {
        this.carViolationsService = carViolationsService;
    }

    @PostMapping("/checkViolations")
    public ResponseEntity<List<CarInfo>> checkViolations(@RequestBody CarData carDataList) {
        List<CarInfo> result = carViolationsService.checkViolations(carDataList);
        return ResponseEntity.ok(result);
    }




}
