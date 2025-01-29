package enefit.rasmushaug.enefitpower.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import enefit.rasmushaug.enefitpower.model.MeteringPoints;
import enefit.rasmushaug.enefitpower.repository.MeteringPointsRepository;

import java.util.List;

@Service
public class MeteringPointsService {

    @Autowired
    private MeteringPointsRepository meteringPointsRepository;

    public MeteringPoints saveMeteringPoint(MeteringPoints meteringPoint) {
        return meteringPointsRepository.save(meteringPoint);
    }

    public MeteringPoints getMeteringPointsById(Long meteringPointId) {
        return meteringPointsRepository.findByMeteringPointId(meteringPointId);
    }

    public List<MeteringPoints> getMeteringPointsByCustomerId(Long customerId) {
        return meteringPointsRepository.findByCustomerId(customerId);
    }
}
