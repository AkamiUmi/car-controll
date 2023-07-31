package uz.asbt.service;

import uz.asbt.model.Contract;
import uz.asbt.model.ContractDB;
import uz.asbt.model.Response;

import java.util.List;

public interface ContractService {
     ContractDB addContract(ContractDB contracts);
     List<ContractDB> getAllContracts();
     ContractDB getContractById(long id);
     Long compareContractsEntry(List<Contract> contracts);
     List<Contract> uniqueFromJson(List<Contract> contracts);
     //Long fullyComparedCount(List<ContractModel> contracts);
     List<ContractDB> uniqueFromDatabase(List<Contract> contracts);
     Response compareData(List<Contract> contracts);
}
