package uz.asbt.model;

import lombok.Getter;
import lombok.Setter;
import java.util.List;
@Getter
@Setter

public class Response {
    private List<ContractModel> contractsInDB;
    private List<ContractModel> contractsInJson;
    private Long count;
}
