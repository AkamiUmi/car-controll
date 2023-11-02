package uz.asbt.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

public class CarData {
    private List<CarInfo> cars;


    @Override
    public String toString() {
        return "CarData{" +
                "cars=" + cars +
                '}';
    }
}
