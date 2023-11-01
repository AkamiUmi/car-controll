package uz.asbt.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Violation {
    private String DRB;
    private String QarorSery;
    private String QarorNumber;
    private String ViolationTime;
    private String ViolationType;
    private String Sum;
    private String ViolationLocation;
}
