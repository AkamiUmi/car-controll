package uz.asbt.service;

import org.springframework.stereotype.Service;
import uz.asbt.model.Contract;
import uz.asbt.model.Response;
import uz.asbt.repository.ContractRepository;
import uz.asbt.model.ContractDB;

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
        List<String> keys = new ArrayList<>();
        for (Contract contract : contracts) {
            String key = contract.getPhone() + contract.getPassportSeries() + contract.getPlateNumber();
            keys.add(key);
        }
        List<ContractDB> contain = contractRepository.findContractInDB(keys);
        return contain;
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


}
