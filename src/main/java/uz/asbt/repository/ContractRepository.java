package uz.asbt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.asbt.model.ContractDB;

import java.util.List;

@Repository
public interface ContractRepository extends JpaRepository<ContractDB, Long> {
    @Query(value = "SELECT * FROM contracts " +
            "WHERE phone || passport_series || plate_number IN :keys", nativeQuery = true)
    List<ContractDB> findContractsEntrySize(@Param("keys") List<String> keys);

    @Query(value = "SELECT * FROM contracts " +
            "WHERE phone || passport_series || plate_number NOT IN :keys", nativeQuery = true)
    List<ContractDB> findContractInDB(@Param("keys") List<String>  keys);
    boolean existsByPhoneAndPassportSeriesAndPlateNumber(String phone, String passportSeries, String plateNumber);
}
