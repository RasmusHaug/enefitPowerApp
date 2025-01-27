package enefit.rasmushaug.enefitpower.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import enefit.rasmushaug.enefitpower.model.MeteringPoints;

import java.util.List;

public interface MeteringPointsRepository extends JpaRepository<MeteringPoints, Long> {
    List<MeteringPoints> findByCustomerCustomerId(Long customerId);
}
