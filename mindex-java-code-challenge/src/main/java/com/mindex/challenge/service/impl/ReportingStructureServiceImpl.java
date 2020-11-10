package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;


/**
 * This class implements ReportingStructureService Interface to create reporting structure for an
 * employee on the fly.
 */
@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {

    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    /**
     * This method uses the employee id to get the direct reports under it and all of their distinct reports
     *
     * @param id employee id whose reporting structure needs to be found
     * @return reporting structure object having employee details and total number of reports under them.
     */
    @Override
    public ReportingStructure getReportingStructure(String id) {
        LOG.debug("Reading employee with id [{}]", id);

        // Get the employee from repository and check if it exists
        Employee employee = employeeRepository.findByEmployeeId(id);
        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        //Get the total number of reports for this employee
        //Assuming one subordinate employee is considered as one count
        int totalNoOfReports = getNumberOfReports(employee);

        // Create and return the reporting structure object
        ReportingStructure reportingStructure = new ReportingStructure();
        reportingStructure.setEmployee(employee);
        reportingStructure.setNumberOfReports(totalNoOfReports);

        return reportingStructure;
    }


    /**
     * This method uses breadth first search using queue implementation to get all the direct and indirect reports
     * under an employee. It considers a direct or indirect report only once to return distinct reports.
     *
     * @param employee : employee object whose reporting structure needs to be found
     * @return total number of reports
     */
    private int getNumberOfReports(Employee employee){
        LOG.debug("Calculating total number of reports for id [{}]", employee.getEmployeeId());

        Set<String> visitedEmployeeIds = new HashSet<>(); // distinct employees set

        //BFS implementation using queue data structure
        Queue<Employee> queue = new ArrayDeque<>();
        queue.add(employee);

        while(queue.size() > 0){
            Employee currentEmployee = queue.poll();
            List<Employee> directReports = currentEmployee.getDirectReports();

            //Continue if no direct reports for current employee
            if(directReports == null)
                continue;

            //add all unique reports for current employee to queue
            for(Employee subordinate : directReports){
                String subordinateId = subordinate.getEmployeeId();
                if( !visitedEmployeeIds.contains(subordinateId) ){
                    visitedEmployeeIds.add(subordinateId);
                    queue.add(employeeRepository.findByEmployeeId(subordinateId));
                }
            }
        }

        return visitedEmployeeIds.size(); // return total number of distinct reports as our set size
    }

}
