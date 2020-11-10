package com.mindex.challenge;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.CompensationService;
import com.mindex.challenge.service.ReportingStructureService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ChallengeApplicationTests {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private CompensationRepository compensationRepository;

	@Autowired
	CompensationService compensationService;

	@Autowired
	private ReportingStructureService reportStructService;

	@Test
	public void contextLoads() throws ParseException {
		//TASK 1
		Employee employee  = employeeRepository.findByEmployeeId("16a596ae-edd3-4847-99fe-c4518e82c86f");
		ReportingStructure employeeReport = reportStructService.getReportingStructure(employee.getEmployeeId());

		assertNotNull(employeeReport);
		assertEquals(4, employeeReport.getNumberOfReports().intValue());

		//TASK 2
		Compensation compensation = new Compensation();
		compensation.setSalary(10000.00);
		compensation.setEffectiveDate("11/10/2020");

		Compensation createdCompensation = compensationService.create(compensation);

		assertNotNull(createdCompensation);
		assertEquals(compensation.getSalary(), createdCompensation.getSalary());
	}

}
