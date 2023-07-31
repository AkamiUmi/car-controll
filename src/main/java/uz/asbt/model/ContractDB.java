package uz.asbt.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "contracts")
public class ContractDB {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "phone")
    private String phone;

    @Column(name = "plate_number")
    private String plateNumber;
    @Column(name = "passport_series")
    private String passportSeries;

    @Column(name = "date_begin")
    @JsonFormat(pattern="dd.MM.yyyy")
    private LocalDate dateBegin;

    @Column(name = "date_end")
    @JsonFormat(pattern="dd.MM.yyyy")
    private LocalDate dateEnd;
}
