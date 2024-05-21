package jp.co.axa.apidemo.services.authentication;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.entities.LoginUser;
import jp.co.axa.apidemo.mapper.EmployeeManageMapper;
import jp.co.axa.apidemo.repositories.EmployeeRepository;

@Service("userDetailsService")
public class MyUserDetailsService implements UserDetailsService{
    @Autowired
    private EmployeeRepository employeeRepository;
	
	@Autowired
	private EmployeeManageMapper userMapper;
	/**
	 * find user by user`s name, set the authority and get the password from database
	 * 
	 * @param username
	 * throws UsernameNotFoundException
	 * @return UserDetails
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Long id;
		String role = "";
		//get the user from database according to the name
		LoginUser user = (LoginUser) userMapper.select(username); 
		if(user==null) {
			throw new UsernameNotFoundException("invaild username");
		}
		id = user.getId();
		Optional<Employee> employee = employeeRepository.findById(id);
		if(employee.isPresent()) role= "ROLE_" + employee.get().getDepartment();
		// set the authority based on the department
		List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList("admins,"+role);
		
		return new User(user.getName(),new BCryptPasswordEncoder().encode(user.getPassword()),auths);
	}

}
