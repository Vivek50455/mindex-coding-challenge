package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportingStructureServiceImplTest {
    private String reportStructEmployeeIdUrl;
    private String employeeUrl;


    @Autowired
    private ReportingStructureService reportingStructureService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        reportStructEmployeeIdUrl = "http://localhost:" + port + "/reportingStructure/employee/{id}";
        employeeUrl = "http://localhost:" + port + "/employee";
    }

    /**
     * This method does the sanity checks for employee details, number of reports calculated in reporting structure.
     */
    @Test
    public void testCreateReadUpdate() {
        // Setting test employee object details
        Employee testEmployee = new Employee();
        testEmployee.setEmployeeId("abc123");
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");

        //Creating test employee direct report
        List<Employee> directReports = new ArrayList<>();
        Employee testDirectEmployee = new Employee();
        Employee directReport = restTemplate.postForEntity(employeeUrl, testDirectEmployee, Employee.class).getBody();
        directReports.add(directReport);
        testEmployee.setDirectReports(directReports);

        // Creating test employee object
        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();

        // Creating reporting structure test object
        ReportingStructure testReportStruct = new ReportingStructure();
        testReportStruct.setEmployee(createdEmployee);
        testReportStruct.setNumberOfReports(1);


        // Read checks for reporting structure
        ReportingStructure readReportStruct = restTemplate.getForEntity(reportStructEmployeeIdUrl, ReportingStructure.class,
                testReportStruct.getEmployee().getEmployeeId()).getBody();
        assertNotNull(readReportStruct.getEmployee().getEmployeeId());
        assertEquals(testReportStruct.getEmployee().getEmployeeId(), readReportStruct.getEmployee().getEmployeeId());
        assertReportStructEquivalence(testReportStruct, readReportStruct);
    }

    /**
     * This method checks whether test object and generated reporting structure object has same details
     * @param expected test reporting structure object to compare with
     * @param actual reporting structure object generated to test
     */
    private static void assertReportStructEquivalence(ReportingStructure expected, ReportingStructure actual) {
        assertEquals(expected.getNumberOfReports(), actual.getNumberOfReports());
        assertEquals(expected.getEmployee().getFirstName(), actual.getEmployee().getFirstName());
        assertEquals(expected.getEmployee().getLastName(), actual.getEmployee().getLastName());
        assertEquals(expected.getEmployee().getPosition(), actual.getEmployee().getPosition());
        assertEquals(expected.getEmployee().getDepartment(), actual.getEmployee().getDepartment());
    }
}
