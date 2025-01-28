package enefit.rasmushaug.enefitpower.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import enefit.rasmushaug.enefitpower.model.EleringData;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface EleringDataRepository extends JpaRepository<EleringData, LocalDate> {
    List<EleringData> findByDate(LocalDate date);

    List<EleringData> findByDateBetween(LocalDate startDate, LocalDate endDate);
}
