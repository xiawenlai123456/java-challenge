package jp.co.axa.apidemo.controllers;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.services.employee.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Api(value="User Operation",tags="User Operation Interface")
public class EmployeeController {
	
    @Autowired
    private EmployeeService employeeService;


    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

	/**
	 * 1.get the info of all employees
	 * 2.only people in the Director or HR can do this operation
	 * 
	 * @return List<Employee>
	 */
    @ApiOperation(value = "DISPLAY ALL USERS")
    @GetMapping("/employees")
    @Secured({"ROLE_Director","ROLE_HR"})
    public List<Employee> getEmployees() {
        List<Employee> employees = employeeService.retrieveEmployees();
        return employees;
    }

	/**
	 * 1.get the employee with particular id
	 * 2.only people in the Director or HR can do this operation
	 * 
	 * @return Employee
	 */
    @ApiOperation(value = "SEARCH")
    @GetMapping("/employees/{employeeId}")
    @Secured({"ROLE_Director","ROLE_HR"})
    public Employee getEmployee(@PathVariable(name="employeeId")Long employeeId) {
        return employeeService.getEmployee(employeeId);
    }

	/**
	 * 1.add employee
	 * 2.only people in the Director can do this operation
	 * 
	 * @param employee
	 */
    @ApiOperation(value = "ADD THE USER")
    @PostMapping("/employees")
    @Secured({"ROLE_Director"})
    public void saveEmployee(Employee employee){
        employeeService.saveEmployee(employee);
    }

	/**
	 * 1.delete employee
	 * 2.only people in the Director can do this operation
	 * 
	 * @param employeeId
	 * @return ResponseEntity
	 */
    @ApiOperation(value = "DELETE THE USER")
    @DeleteMapping("/employees/{employeeId}")
    @Secured({"ROLE_Director"})
    public ResponseEntity<String> deleteEmployee(@PathVariable(name="employeeId")Long employeeId){
    	return employeeService.deleteEmployee(employeeId);
    }

	/**
	 * 1.update employee
	 * 2.only people in the Director can do this operation
	 * 
	 * @param employee
	 * @param employeeId
	 * @return ResponseEntity
	 */
    @ApiOperation(value = "UPDATE THE USER")
    @PutMapping("/employees/{employeeId}")
    @Secured({"ROLE_Director"})
    public ResponseEntity<String> updateEmployee(@RequestBody Employee employee,
                               @PathVariable(name="employeeId")Long employeeId){
    	return employeeService.updateEmployee(employee, employeeId);
    }
    
	/**
	 * 1.update the password
	 * 2.only people in the Director can do this operation
	 * 
	 * @param employeeId
	 * @param password
	 * @return ResponseEntity
	 */
    @ApiOperation(value = "UPDATE PASSWORD")
    @GetMapping("/employees/{employeeId}/{password}")
    @Secured({"ROLE_Director"})
    public ResponseEntity<String> setPassword(@PathVariable(name="employeeId")Long employeeId, @PathVariable(name="password")String password) {
        return employeeService.updatePass(employeeId, password);
    }
}
