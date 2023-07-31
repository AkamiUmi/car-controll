package uz.asbt.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.asbt.model.ContractModel;
import uz.asbt.model.Response;
import uz.asbt.service.ContractService;

import java.util.List;

@RestController
@RequestMapping("/contracts")
@Slf4j
public class ContractController {

    private final ContractService contractService;

    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    @GetMapping("/list")
    public List<ContractModel> list() {
        return contractService.getAllContracts();

    }

    @PostMapping( "/add-contracts")
    public List<ContractModel> AddContract(@RequestBody List<ContractModel> contracts) {
        log.info("LogInfo: {}", contracts.toString());
        for (ContractModel contract: contracts) {
            contractService.addContract(contract);
        }
        return contracts;
    }
    @PostMapping("/compare-entry")
    public ResponseEntity<Integer> compareContractsEntry(@RequestBody List<ContractModel> contracts) {
        log.info("LogInfo: {}", contracts.toString());
        return ResponseEntity.ok(contractService.compareContractsEntry(contracts));
    }

    @PostMapping("/base-contracts")
    public ResponseEntity<List<ContractModel>> contractsInBase(@RequestBody List<ContractModel> contracts) {
        log.info("LogInfo: {}", contracts.toString());
        return ResponseEntity.ok(contractService.uniqueFromDatabase(contracts));
    }

    @PostMapping("/json-contracts")
    public ResponseEntity<List<ContractModel>> uniqueFromJson(@RequestBody List<ContractModel> contracts) {
        log.info("LogInfo: {}", contracts.toString());
        return ResponseEntity.ok(contractService.uniqueFromJson(contracts));
    }

    @PostMapping("/compare")
    public ResponseEntity<Response> compareData(@RequestBody List<ContractModel> contracts) {
        log.info("LogInfo: {}", contracts.toString());
        return ResponseEntity.ok(contractService.compareData(contracts));
    }

}
