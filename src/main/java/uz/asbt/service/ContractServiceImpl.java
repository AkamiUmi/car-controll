package uz.asbt.service;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Service;
import uz.asbt.model.Contract;
import uz.asbt.model.ContractDB;
import uz.asbt.model.Response;
import uz.asbt.repository.ContractRepository;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContractServiceImpl implements ContractService {

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
        return contractRepository.findContractInDB(keys);
    }

    @Override
    public List<Contract> uniqueFromJson(List<Contract> contracts) {
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
        short formatDate = workbook.createDataFormat().getFormat("dd.mm.yyyy");

        CellStyle dateStyle = workbook.createCellStyle();
        dateStyle.setDataFormat(formatDate);
        HSSFRow row = sheet.createRow(0);

        int entryCountRowIndex = 0;
        HSSFRow entryCountRow = sheet.createRow(entryCountRowIndex);
        entryCountRow.createCell(0).setCellValue("Entry Count");
        entryCountRow.createCell(1).setCellValue(compareContractsEntry(contracts));

        int mergedRowIndex = 1;
        row = sheet.createRow(mergedRowIndex);
        row.createCell(0).setCellValue("ContractsInJson");
        CellRangeAddress contractsInJsonCellRange = new CellRangeAddress(mergedRowIndex, mergedRowIndex, 0, 5);
        sheet.addMergedRegion(contractsInJsonCellRange);

        row.createCell(7).setCellValue("ContractDB");
        CellRangeAddress contractDBCellRange = new CellRangeAddress(mergedRowIndex, mergedRowIndex, 7, 12); // Adjust the column range
        sheet.addMergedRegion(contractDBCellRange);

        int nameRowIndex = 2;
        HSSFRow nameRow = sheet.createRow(nameRowIndex);

        nameRow.createCell(0).setCellValue("Id");
        nameRow.createCell(1).setCellValue("Phone");
        nameRow.createCell(2).setCellValue("Passport Series");
        nameRow.createCell(3).setCellValue("Plate Number");
        nameRow.createCell(4).setCellValue("Date Begin");
        nameRow.createCell(5).setCellValue("Date End");

        nameRow.createCell(7).setCellValue("Id");
        nameRow.createCell(8).setCellValue("Phone");
        nameRow.createCell(9).setCellValue("Passport Series");
        nameRow.createCell(10).setCellValue("Plate Number");
        nameRow.createCell(11).setCellValue("Date Begin");
        nameRow.createCell(12).setCellValue("Date End");

        int dataRowIndex = 3;
        for (Contract contract : uniqueFromJson(contracts)) {
            HSSFRow dataRow = sheet.createRow(dataRowIndex);
            dataRow.createCell(0).setCellValue(contract.getId());
            dataRow.createCell(1).setCellValue(contract.getPhone());
            dataRow.createCell(2).setCellValue(contract.getPassportSeries());
            dataRow.createCell(3).setCellValue(contract.getPlateNumber());
            HSSFCell dateBeginCell = dataRow.createCell(4);
            dateBeginCell.setCellValue(contract.getDateBegin());
            dateBeginCell.setCellStyle(dateStyle);
            HSSFCell dateEndCell = dataRow.createCell(5);
            dateEndCell.setCellValue(contract.getDateEnd());
            dateEndCell.setCellStyle(dateStyle);
            dataRowIndex++;
        }

        dataRowIndex = 3;
        for (ContractDB contractDB : uniqueFromDatabase(contracts)) {
            HSSFRow dataRow = sheet.getRow(dataRowIndex);
            if (dataRow == null) {
                dataRow = sheet.createRow(dataRowIndex);
            }
            dataRow.createCell(7).setCellValue(contractDB.getId());
            dataRow.createCell(8).setCellValue(contractDB.getPhone());
            dataRow.createCell(9).setCellValue(contractDB.getPassportSeries());
            dataRow.createCell(10).setCellValue(contractDB.getPlateNumber());
            HSSFCell dateBeginCellDB = dataRow.createCell(11);
            dateBeginCellDB.setCellValue(contractDB.getDateBegin());
            dateBeginCellDB.setCellStyle(dateStyle);
            HSSFCell dateEndCellDB = dataRow.createCell(12);
            dateEndCellDB.setCellValue(contractDB.getDateEnd());
            dateEndCellDB.setCellStyle(dateStyle);
            dataRowIndex++;
        }

        ServletOutputStream ops = res.getOutputStream();
        workbook.write(ops);
        workbook.close();
        ops.close();
    }

}
