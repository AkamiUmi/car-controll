package uz.asbt.service;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;
import uz.asbt.model.Contract;
import uz.asbt.model.Response;
import uz.asbt.repository.ContractRepository;
import uz.asbt.model.ContractDB;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContractServiceImpl implements ContractService{

    private final ContractRepository contractRepository;

    public ContractServiceImpl(ContractRepository contractRepository) {
        this.contractRepository = contractRepository;
    }

    @Override
    public ContractDB addContract(ContractDB contract) {
        return contractRepository.save(contract);
    }

    @Override
    public List<ContractDB> getAllContracts() {
        return contractRepository.findAll();
    }

    @Override
    public ContractDB getContractById(long id) {
        return contractRepository.getReferenceById(id);
    }

    @Override
    public Long compareContractsEntry(List<Contract> contracts) {
        List<String> keys = new ArrayList<>();
        for (Contract contract : contracts) {
            String key = contract.getPhone() + contract.getPassportSeries() + contract.getPlateNumber();
            keys.add(key);
        }
        List<ContractDB> contain = contractRepository.findContractsEntrySize(keys);
        return (long) contain.size();
    }

    @Override
    public List<ContractDB> uniqueFromDatabase(List<Contract> contracts) {
        var keys = contracts
                .stream()
                .map(contract -> contract.getPhone() + contract.getPassportSeries() + contract.getPlateNumber())
                .collect(Collectors.toList());
        return  contractRepository.findContractInDB(keys);
    }


/*    @Override
    public Long fullyComparedCount(List<ContractModel> contracts) {
        return contracts
                .stream()
                .filter(model -> contractRepository.existsByPhoneAndPassportSeriesAndPlateNumber(model.getPhone(), model.getPassportSeries(), model.getPlateNumber()))
                .count();
    }*/

    @Override
    public List<Contract> uniqueFromJson(List<Contract> contracts) {
//        var keys = contracts
//                .stream()
//                .map(contract -> contract.getPhone() + contract.getPassportSeries() + contract.getPlateNumber())
//                .collect(Collectors.toList());
//        return contractRepository.findContractsEntrySize(keys);
        return contracts
                .stream()
                .filter(model -> !contractRepository.existsByPhoneAndPassportSeriesAndPlateNumber(model.getPhone(), model.getPassportSeries(), model.getPlateNumber()))
                .collect(Collectors.toList());
    }

    @Override
    public Response compareData(List<Contract> contracts) {
        Response response = new Response();
        response.setContractsInDB(uniqueFromDatabase(contracts));
        response.setContractsInJson(uniqueFromJson(contracts));
        response.setCount(compareContractsEntry(contracts));
        return response;
    }

    @Override
    public void excel(HttpServletResponse res, List<Contract> contracts) throws Exception {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Contracts Info");
        HSSFRow row = sheet.createRow(0);
        row.createCell(0).setCellValue("ContractsInJson");
        row.createCell(1).setCellValue("Id");
        row.createCell(2).setCellValue("Phone");
        row.createCell(3).setCellValue("Passport Series");
        row.createCell(4).setCellValue("Plate Number");
        row.createCell(5).setCellValue("Date Begin");
        row.createCell(6).setCellValue("Date End");
        row.createCell(8).setCellValue("ContractDB");
        row.createCell(9).setCellValue("Id");
        row.createCell(10).setCellValue("Phone");
        row.createCell(11).setCellValue("Passport Series");
        row.createCell(12).setCellValue("Plate Number");
        row.createCell(13).setCellValue("Date Begin");
        row.createCell(14).setCellValue("Date End");
        int dataRowIndex = 1;

        Response response = new Response();
        response.setContractsInDB(uniqueFromDatabase(contracts));
        response.setContractsInJson(uniqueFromJson(contracts));
        response.setCount(compareContractsEntry(contracts));

        for (Contract contract : response.getContractsInJson()) {
            HSSFRow dataRow = sheet.createRow(dataRowIndex);
            dataRow.createCell(1).setCellValue(contract.getId());
            dataRow.createCell(2).setCellValue(contract.getPhone());
            dataRow.createCell(3).setCellValue(contract.getPassportSeries());
            dataRow.createCell(4).setCellValue(contract.getPlateNumber());
            dataRow.createCell(5).setCellValue(contract.getDateBegin());
            dataRow.createCell(6).setCellValue(contract.getDateEnd());
            dataRowIndex++;
        }
        dataRowIndex = 1;
        for (ContractDB contractDB : response.getContractsInDB()) {
            HSSFRow responseRow = sheet.getRow(dataRowIndex);
            if (responseRow == null) {
                responseRow = sheet.createRow(dataRowIndex);
            }
            responseRow.createCell(9).setCellValue(contractDB.getId());
            responseRow.createCell(10).setCellValue(contractDB.getPhone());
            responseRow.createCell(11).setCellValue(contractDB.getPassportSeries());
            responseRow.createCell(12).setCellValue(contractDB.getPlateNumber());
            responseRow.createCell(13).setCellValue(contractDB.getDateBegin());
            responseRow.createCell(14).setCellValue(contractDB.getDateEnd());
            dataRowIndex++;
        }

        /*responseRow.createCell(2).setCellValue("Contracts in JSON");
        responseRow.createCell(3).setCellValue(response.getContractsInJson().toString());
        responseRow.createCell(4).setCellValue("Number of Contracts");
        responseRow.createCell(5).setCellValue(response.getCount());*/

        ServletOutputStream ops = res.getOutputStream();
        workbook.write(ops);
        workbook.close();
        ops.close();
    }


}
