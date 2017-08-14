package commconsistency.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import commconsistency.dao.UserRepository;
import commconsistency.domain.User;
import commconsistency.security.UserSecurity;

public class CustomUserDetailsService implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		
		User user = userRepository.findByUserName(userName);
		if(user==null) {
			throw new UsernameNotFoundException("UserName:"+userName + "not found.");
		}
		
		// UserSecurity实现UserDetails并将SUser的Email映射为username
        UserSecurity userSecurity = new UserSecurity(user);
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        return userSecurity; 
	}

}
