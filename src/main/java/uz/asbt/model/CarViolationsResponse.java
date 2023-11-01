package uz.asbt.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarViolationsResponse {
    private int ResponseCode;
    private String ResponseMessage;
    private Violation[] ViolationsList;
    private String Violations;
    private String Pdf;

    public boolean hasViolations() {
        return ViolationsList != null && ViolationsList.length > 0;
    }
}
