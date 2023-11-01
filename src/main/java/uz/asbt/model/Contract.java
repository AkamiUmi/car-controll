package uz.asbt.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
public class Contract {
    private String id;

    private String phone;

    private String plateNumber;

    private String passportSeries;

    @JsonFormat(pattern="dd.MM.yyyy")
    private LocalDate dateBegin;

    @JsonFormat(pattern="dd.MM.yyyy")
    private LocalDate dateEnd;
    @Override
    public String toString() {
        return "Contract{" +
                "id='" + id + '\'' +
                ", phone='" + phone + '\'' +
                ", plateNumber='" + plateNumber + '\'' +
                ", passportSeries='" + passportSeries + '\'' +
                ", dateBegin=" + dateBegin +
                ", dateEnd=" + dateEnd +
                '}';
    }
}
