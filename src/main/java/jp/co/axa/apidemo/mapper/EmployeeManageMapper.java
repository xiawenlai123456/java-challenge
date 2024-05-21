package jp.co.axa.apidemo.mapper;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.entities.LoginUser;

@Mapper
public interface EmployeeManageMapper {

	@Select("SELECT * FROM LOGINUSER WHERE name = #{name}")
	LoginUser select(@Param("name")String name);
	
	@Select("SELECT department FROM EMPLOYEE WHERE id = #{id}")
	String selectDepartment(@Param("id") Long id);
	
	@Select("SELECT * FROM EMPLOYEE WHERE department=#{department}")
	List<Employee> selEmployees(@Param("department") String department);
	
	@Update("UPDATE LOGINUSER SET password = #{password} WHERE id = #{id}")
    void setPass(@Param("id")Long id,@Param("password") String pass);
	
	@Update("UPDATE EMPLOYEE SET department = #{department} , employee_name =#{name} ,employee_salary =#{salary} WHERE id = #{id}")
    void setEmployeeInfo(@Param("id") Long id, @Param("department") String department, @Param("name") String name, @Param("salary")Integer salary);
	
	@Update("UPDATE LOGINUSER SET name =#{name} WHERE id = #{id}")
    void setLoginUserInfo(@Param("id") Long id,  @Param("name") String name);
	
	
}
