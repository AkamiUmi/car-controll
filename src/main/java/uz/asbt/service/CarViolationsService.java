package uz.asbt.service;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uz.asbt.model.CarData;
import uz.asbt.model.CarInfo;
import uz.asbt.model.CarViolationsResponse;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CarViolationsService {
    private final String baseUrl = "http://192.168.0.73/";
    private final RestTemplate restTemplate;
    @Autowired
    public CarViolationsService() {
        this.restTemplate = new RestTemplate();
    }

    public List<CarInfo> checkViolations(CarData carData) {
        List<CarInfo> carsWithViolations = new ArrayList<>();

        for (CarInfo car : carData.getCars()) {
            String carNumber = car.getCarNumber();
            String techPassport = car.getTechPassport();
            StringBuilder urlBuilder = new StringBuilder(baseUrl);
            urlBuilder.append(carNumber).append("/").append(techPassport).append("/");
            String finalUrl = urlBuilder.toString();
            log.info("Request URL: {}", finalUrl);
            String response = restTemplate.getForObject(finalUrl, String.class);
            log.info("Response: {}", response);
            if (responseContainsViolations(response)) {
                carsWithViolations.add(car);
            }
        }

        return carsWithViolations;
    }



    private boolean responseContainsViolations(String response) {
        try {
            CarViolationsResponse violationsResponse = new Gson().fromJson(response, CarViolationsResponse.class);
            return violationsResponse != null && violationsResponse.getResponseCode() == 1 && violationsResponse.hasViolations();
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return false;
        }
    }

}
