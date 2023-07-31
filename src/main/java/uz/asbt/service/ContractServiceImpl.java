package uz.asbt.service;

import org.springframework.stereotype.Service;
import uz.asbt.model.Response;
import uz.asbt.repository.ContractRepository;
import uz.asbt.model.ContractModel;

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
    public ContractModel addContract(ContractModel contract) {
        return contractRepository.save(contract);
    }

    @Override
    public List<ContractModel> getAllContracts() {
        return contractRepository.findAll();
    }

    @Override
    public ContractModel getContractById(long id) {
        return contractRepository.getReferenceById(id);
    }

    @Override
    public int compareContractsEntry(List<ContractModel> contracts) {
        List<String> phones = new ArrayList<>();
        for (ContractModel contract : contracts) {
            String phone = contract.getPhone();
            phones.add(phone);
        }
        List<ContractModel> contain = contractRepository.findContractsEntrySize(phones);
        return contain.size();
    }

    @Override
    public List<ContractModel> uniqueFromDatabase(List<ContractModel> contracts) {
        List<String> phones = new ArrayList<>();
        for (ContractModel contract : contracts) {
            String phone = contract.getPhone();
            phones.add(phone);
        }
        List<ContractModel> contain = contractRepository.findContractInDB(phones);
        return contain;
    }

    @Override
    public Long fullyComparedCount(List<ContractModel> contracts) {
        return contracts
                .stream()
                .filter(model -> contractRepository.existsByPhoneAndPassportSeriesAndPlateNumber(model.getPhone(), model.getPassportSeries(), model.getPlateNumber()))
                .count();
    }
    @Override
    public List<ContractModel> uniqueFromJson(List<ContractModel> contracts) {
        return contracts
                .stream()
                .filter(model -> !contractRepository.existsByPhone(model.getPhone()))
                .collect(Collectors.toList());
    }

    @Override
    public Response compareData(List<ContractModel> contracts) {
        Response response = new Response();
        response.setContractsInDB(uniqueFromDatabase(contracts));
        response.setContractsInJson(uniqueFromJson(contracts));
        response.setCount(fullyComparedCount(contracts));
        return response;
    }


}
