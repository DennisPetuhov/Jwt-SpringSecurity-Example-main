package com.example.springsecurityexample.security.services;



import com.example.springsecurityexample.model.User;
import com.example.springsecurityexample.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService { //

    @Autowired
    UserRepository userRepository; //

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
    	
        User user = userRepository.findByUsername(username) // ищем пользователя
                	.orElseThrow(() -> // если нулевой
                        new UsernameNotFoundException("User Not Found with -> username or email : " + username) // выбрасываем исключение
        );
//        UserDetails userDetails = (UserDetails) user;

        return UserPrinciple.build(user); // обьект user перегоняем в класс оболочку UserPrinciple
    }
}