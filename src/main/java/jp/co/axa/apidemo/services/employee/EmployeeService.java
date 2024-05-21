package jp.co.axa.apidemo.services.employee;

import jp.co.axa.apidemo.entities.Employee;

import java.util.List;

import org.springframework.http.ResponseEntity;

public interface EmployeeService {

    public List<Employee> retrieveEmployees();

    public Employee getEmployee(Long employeeId);

    public void saveEmployee(Employee employee);

    public ResponseEntity<String> deleteEmployee(Long employeeId);

    public ResponseEntity<String> updateEmployee(Employee employee,Long employeeId);
    
    public ResponseEntity<String> updatePass(Long employeeId,String password);
}