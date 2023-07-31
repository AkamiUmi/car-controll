package uz.asbt.model;

import lombok.Getter;
import lombok.Setter;
import java.util.List;
@Getter
@Setter

public class Response {
    private List<ContractDB> contractsInDB;
    private List<Contract> contractsInJson;
    private Long count;
}
