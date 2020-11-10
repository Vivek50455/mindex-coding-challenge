package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;

/**
 * This class inserts and retrieves compensation details for an employee from the compensation repository
 */
@Service
public class CompensationServiceImpl implements CompensationService {

    private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImpl.class);

    @Autowired
    private CompensationRepository compensationRepository;


    /**
     * Inserts a compensation object containing employee details, salary and effective date in compensation
     * repository. This method assumes that employee id is provided in employee object.
     * @param compensation : compensation
     * @return compensation object created
     */
    @Override
    public Compensation create(Compensation compensation) {
        LOG.debug("Creating Compensation [{}]", compensation);

        compensation.setCompensationId(UUID.randomUUID().toString()); // set compensation id (for future use)
        compensationRepository.insert(compensation); // insert compensation details in repository

        return compensation;
    }


    /**
     * Returns the compensation details of an employee using its id
     *
     * @param id : employee id whose compensation details needs to be checked
     * @return compensation object containing employee details, salary and effective date
     */
    @Override
    public Compensation read(String id) {
        LOG.debug("Reading compensation with employee id [{}]", id);

        // Retrieve the compensation for an employee from repository
        Compensation compensation = compensationRepository.findByEmployeeEmployeeId(id);

        // Raise exception if no compensation details found
        if (compensation == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        return compensation;
    }


}
