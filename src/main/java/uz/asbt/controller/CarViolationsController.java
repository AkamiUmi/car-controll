package uz.asbt.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import uz.asbt.model.CarData;
import uz.asbt.model.CarInfo;
import uz.asbt.service.CarViolationsService;

import java.util.List;
@Slf4j
@RestController
public class CarViolationsController {
    private final CarViolationsService carViolationsService;
    @Autowired
    public CarViolationsController(CarViolationsService carViolationsService) {
        this.carViolationsService = carViolationsService;
    }

    @PostMapping("/checkViolations")
    public ResponseEntity<List<CarInfo>> checkViolations(@RequestBody CarData carDataList) {
        log.info("Received request: {}", carDataList.toString());
        List<CarInfo> result = carViolationsService.checkViolations(carDataList);
        log.debug("Response: {}", result);
        return ResponseEntity.ok(result);
    }
    
}
