package uz.asbt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.asbt.model.ContractModel;

import java.util.List;

@Repository
public interface ContractRepository extends JpaRepository<ContractModel, Long> {
    @Query(value = "SELECT * FROM contracts c WHERE phone IN :phones", nativeQuery = true)
    List<ContractModel> findContractsEntrySize(@Param("phones") List<String> phones);

    @Query(value = "SELECT * FROM contracts c WHERE phone NOT IN :phones", nativeQuery = true)
    List<ContractModel> findContractInDB(@Param("phones") List<String>  phones);
    boolean existsByPhoneAndPassportSeriesAndPlateNumber(String phone, String passportSeries, String plateNumber);

    boolean existsByPhone(String phone);
}
