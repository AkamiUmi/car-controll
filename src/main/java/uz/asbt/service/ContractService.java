package uz.asbt.service;

import uz.asbt.model.ContractModel;
import uz.asbt.model.Response;

import java.util.List;

public interface ContractService {
     ContractModel addContract(ContractModel contracts);
     List<ContractModel> getAllContracts();
     ContractModel getContractById(long id);
     int compareContractsEntry(List<ContractModel> contracts);
     List<ContractModel> uniqueFromJson(List<ContractModel> contracts);
     Long fullyComparedCount(List<ContractModel> contracts);
     List<ContractModel> uniqueFromDatabase(List<ContractModel> contracts);
     Response compareData(List<ContractModel> contracts);
}
