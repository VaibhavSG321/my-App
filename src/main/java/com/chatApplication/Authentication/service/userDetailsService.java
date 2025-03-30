package com.chatApplication.Authentication.service;
import com.chatApplication.Authentication.model.Users;
import com.chatApplication.Authentication.model.userPrincipal;
import com.chatApplication.Authentication.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class userDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepo.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("user not found");
        }

        return new userPrincipal(user);
    }


    public UserDetails loadUserByUniqueID(String uniqueID) throws UsernameNotFoundException {
        Users user = userRepo.findByUniqueID(uniqueID);
        if (user == null) {
            throw new UsernameNotFoundException("user not found");
        }

        return new userPrincipal(user);
    }
}
