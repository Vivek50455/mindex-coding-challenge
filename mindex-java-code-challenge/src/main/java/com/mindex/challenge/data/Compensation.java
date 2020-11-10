package com.mindex.challenge.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Compensation {
    @Id
    private String compensationId;
    private Employee employee;
    private Double salary;
    private Date effectiveDate;

    @Transient
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");

    public Compensation() {
    }
    public String getCompensationId() {
        return compensationId;
    }

    public void setCompensationId(String compensationId) {
        this.compensationId = compensationId;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public String getEffectiveDate() {
        return simpleDateFormat.format(effectiveDate);
    }

    public void setEffectiveDate(String effectiveDate) throws ParseException {

        this.effectiveDate = simpleDateFormat.parse(effectiveDate);
    }
}
