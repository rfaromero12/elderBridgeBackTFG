package es.uco.tfg.elderBridge.infrastructure.security;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import es.uco.tfg.elderBridge.infrastructure.database.entity.UserEntity;
import es.uco.tfg.elderBridge.infrastructure.database.repository.UserRepositoryJPA;

public class UserDetailsServiceImpl implements UserDetailsService{
	
	private UserRepositoryJPA userRepositoryJPA;
	
	public UserDetailsServiceImpl(UserRepositoryJPA userRepositoryJPA) {
		this.userRepositoryJPA = userRepositoryJPA;
	}
	
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity user = userRepositoryJPA.findUserByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("No existe usuario"));

        Set<GrantedAuthority> grantList = new HashSet<GrantedAuthority>();

        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("USER");
        grantList.add(grantedAuthority);

        return (UserDetails) new User(username, user.getPassword(), grantList);
	   
    }
}
