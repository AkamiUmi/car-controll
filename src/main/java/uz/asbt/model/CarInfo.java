package uz.asbt.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarInfo {
    private String carNumber;
    private String techPassport;


    @Override
    public String toString() {
        return "CarInfo{" +
                "carNumber='" + carNumber + '\'' +
                ", techPassport='" + techPassport + '\'' +
                '}';
    }
}
