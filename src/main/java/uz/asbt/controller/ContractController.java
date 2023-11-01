package uz.asbt.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.asbt.model.Contract;
import uz.asbt.model.ContractDB;
import uz.asbt.model.Response;
import uz.asbt.service.ContractService;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/contracts")
@Slf4j
@Api(value = "Test")
public class ContractController {

    private final ContractService contractService;

    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    @GetMapping("/list")
    public List<ContractDB> list() {
        return contractService.getAllContracts();

    }

    @PostMapping( "/add-contracts")
    public List<ContractDB> AddContract(@RequestBody List<ContractDB> contracts) {
        log.info("LogInfo: {}", contracts.toString());
        for (ContractDB contract: contracts) {
            contractService.addContract(contract);
        }
        return contracts;
    }
    @PostMapping("/compare-entry")
    public ResponseEntity<Long> compareContractsEntry(@RequestBody List<Contract> contracts) {
        log.info("LogInfo: {}", contracts.toString());
        return ResponseEntity.ok(contractService.compareContractsEntry(contracts));
    }

    @PostMapping("/base-contracts")
    public ResponseEntity<List<ContractDB>> contractsInBase(@RequestBody List<Contract> contracts) {
        log.info("LogInfo: {}", contracts.toString());
        return ResponseEntity.ok(contractService.uniqueFromDatabase(contracts));
    }

    @PostMapping("/json-contracts")
    public ResponseEntity<List<Contract>> uniqueFromJson(@RequestBody List<Contract> contracts) {
        log.info("LogInfo: {}", contracts.toString());
        return ResponseEntity.ok(contractService.uniqueFromJson(contracts));
    }


    @PostMapping("/compare")
    public ResponseEntity<Response> compareData(@RequestBody List<Contract> contracts,
                                                @RequestParam(defaultValue = "false") boolean onlyByPhone) {
        log.info("LogInfo: {}", contracts.toString());
        return ResponseEntity.ok(contractService.compareData(contracts, onlyByPhone));
    }

    @PostMapping("excel")
    public void excel(@RequestBody List<Contract> contracts, HttpServletResponse res) throws Exception{
        res.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment;filename=contracts.xls";
        res.setHeader(headerKey, headerValue);

        contractService.excel(res, contracts);
    }

    @PostMapping("/parse-contracts")
    public List<Contract> parseContracts(@RequestBody String jsonData) {
        log.info("LogInfo: {}", jsonData);
        return contractService.parseContracts(jsonData);
    }

    @PostMapping("/test")
    public void test(@RequestBody String date) {
        try {

            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");

            log.info("Date: {}", date);
            LocalDate currentDate = LocalDate.now();
            LocalDate lastDayOfMonthDate = currentDate.with(TemporalAdjusters.lastDayOfMonth());
            LocalDate firstDayOfNextMonth = currentDate.with(TemporalAdjusters.firstDayOfNextMonth());
            LocalDate firstDayOfCurrentMonth = currentDate.with(TemporalAdjusters.firstDayOfMonth());

            log.info("FirstDayOfNextMonth: {}", firstDayOfNextMonth);

            Date parseDate = format.parse(date);
            LocalDate cardExpiry = parseDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate nextMonth = currentDate.plusMonths(1);
            if (cardExpiry.isBefore(lastDayOfMonthDate)) {
                //if (!cardExpiry.isEqual(lastDayOfMonthDate))
                    log.info("DATE {} EXPIRED", parseDate.toString());
            }
            if (cardExpiry.isBefore(firstDayOfNextMonth.withDayOfMonth(1)) && cardExpiry.isBefore(currentDate)) {

                    log.info("DATE {} EXPIRED2", parseDate.toString());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getContractById(@PathVariable long id) {
        ContractDB contract = contractService.getContractById(id);
        if (contract != null) {
            return ResponseEntity.ok(contract);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Contract not found with ID: " + id);
        }
    }


}
