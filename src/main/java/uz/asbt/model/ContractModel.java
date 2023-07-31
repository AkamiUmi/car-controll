package uz.asbt.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "contracts")
public class ContractModel {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

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
