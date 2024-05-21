package jp.co.axa.apidemo.services.employee;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.entities.LoginUser;
import jp.co.axa.apidemo.mapper.EmployeeManageMapper;
import jp.co.axa.apidemo.repositories.EmployeeRepository;
import jp.co.axa.apidemo.repositories.LoginUserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService{
    // default password
	private String defaultPass = "1234567";
	
    @Autowired
    private LoginUserRepository loginUserRepository;
	
	@Autowired
	private EmployeeManageMapper userMapper;

    @Autowired
    private EmployeeRepository employeeRepository;

    private static final Logger logger =LoggerFactory.getLogger(EmployeeServiceImpl.class);
	/**
	 * find all employees
	 * 
	 * @return employees
	 */
    public List<Employee> retrieveEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        // employees would never be null, null process can an ignored
        logger.info(employees.toString());
        return employees;
 
    }

	/**
	 * get the employee by id
	 * 
	 * @param employeeId
	 * @return employee
	 */
    @Cacheable("employees")
    public Employee getEmployee(Long employeeId) {
    	// if the optEmp is null, the exception will be throw automatically 
        Optional<Employee> optEmp = employeeRepository.findById(employeeId);
        logger.info("find it");
        return optEmp.get();
    }

	/**
	 * add employee
	 * 
	 * @param employee
	 */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @CachePut(value="employees", key="#employee.id")
    public void saveEmployee(Employee employee){
        employeeRepository.save(employee);
        LoginUser user = new LoginUser(employee.getId(), employee.getName(), defaultPass); 
        //add the info into user table
        loginUserRepository.save(user);
        logger.info("Employee Saved Successfully");
    }

	/**
	 * delete employee
	 * 
	 * @param employee
	 * @return ResponseEntity
	 */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @CacheEvict("employees")
    public ResponseEntity<String> deleteEmployee(Long employeeId){    	
    	String department ="";
    	department = userMapper.selectDepartment(employeeId);
    	// Count the man in the Director department when deleting the man`s info in the Director,
    	// If the count is 1, delete operation is not allow.
    	if("Director".equals(department)) {
    		List<Employee> employees=userMapper.selEmployees(department);
    		Integer sum= (int) employees.stream().filter((e)->"Director".equals(e.getDepartment())).count();
    		if(sum ==1) {
    			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("At least one director must be present.");
    		}
    	}
    	// delete the info both in the employ table and loginUser table
    	employeeRepository.deleteById(employeeId);
    	loginUserRepository.deleteById(employeeId);
    	return ResponseEntity.status(HttpStatus.OK).body("updtate successfully");
    }
    
	/**
	 * update employee
	 * 
	 * @param employee
	 * @param employeeId
	 * @return ResponseEntity
	 */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @CachePut(value = "employees", key="#employeeId")
    public ResponseEntity<String> updateEmployee(Employee employee,Long employeeId) {
        Employee emp = employeeRepository.findById(employeeId).get();
        // the id of employee to be updated should be same as parameter employeeId  
        if(!employee.getId().equals(employeeId)) {
        	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("id can not be modified");
        }
        
    	// Count the man in the Director department when updating the man`s info in the Director,
    	// If the count is 1 and Director is going to be changed, updating operation is not allow.
        if(emp.getDepartment().equals("Director") && !employee.getDepartment().equals("Director")) {
    		List<Employee> employees=userMapper.selEmployees(emp.getDepartment());
    		Integer sum= (int) employees.stream().filter((e)->"Director".equals(e.getDepartment())).count();
    		if(sum ==1) {
    			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("At least one director must be present.");
    		}
        }
        userMapper.setEmployeeInfo(employee.getId(), employee.getDepartment(), employee.getName(), employee.getSalary());
        userMapper.setLoginUserInfo(employee.getId(),employee.getName());
        return ResponseEntity.status(HttpStatus.OK).body("updtate successfully");
    }
    
	/**
	 * update the password
	 * 
	 * @param employeeId
	 * @param password
	 * @return ResponseEntity
	 */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
	@Override
	public ResponseEntity<String> updatePass(Long employeeId, String password) {
        LoginUser user= loginUserRepository.findById(employeeId).get();
        // if the user is null, default exception will be thrown
        if(user ==null) {
        	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("update failure");
        }
        userMapper.setPass(employeeId, password);
        return ResponseEntity.status(HttpStatus.OK).body("updtate successfully");
    }
}