package uz.asbt.service;

import uz.asbt.model.Contract;
import uz.asbt.model.ContractDB;
import uz.asbt.model.Response;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface ContractService {
     ContractDB addContract(ContractDB contracts);
     List<ContractDB> getAllContracts();
     ContractDB getContractById(long id);
     Long compareContractsEntry(List<Contract> contracts);
     Long compareContractsEntryByPhone(List<Contract> contracts);
     List<Contract> uniqueFromJson(List<Contract> contracts);
     List<Contract> uniqueFromJsonByPhone(List<Contract> contracts);
     List<ContractDB> uniqueFromDatabase(List<Contract> contracts);
     List<ContractDB> uniqueFromPhone(List<Contract> contracts);
     Response compareData(List<Contract> contracts, boolean onlyByPhone);
     void excel(HttpServletResponse res, List<Contract> contracts) throws Exception;
     List<Contract> parseContracts(String jsonData);
}
