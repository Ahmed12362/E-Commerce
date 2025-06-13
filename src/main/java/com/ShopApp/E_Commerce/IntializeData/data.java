package com.ShopApp.E_Commerce.IntializeData;

import com.ShopApp.E_Commerce.model.Role;
import com.ShopApp.E_Commerce.model.User;
import com.ShopApp.E_Commerce.repository.RoleRepository;
import com.ShopApp.E_Commerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Component
@Transactional
@RequiredArgsConstructor
public class data implements ApplicationListener<ApplicationReadyEvent> {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Set<String> defaultRoles = Set.of("ROLE_ADMIN" , "ROLE_USER");
        createDefaultUserIfNotExist();
        createDefaultRoleIfNotExist(defaultRoles);
        createDefaultAdminIfNotExist();
    }

    private void createDefaultUserIfNotExist() {
        Role userRole = roleRepository.findByName("ROLE_USER");
        for (int i = 1 ; i<=5 ; i++){
            String defaultEmail = "user"+i+"@gmail.com";
            if(userRepository.existsByEmail(defaultEmail)){
                continue;
            }
            User user = new User();
            user.setPassword(passwordEncoder.encode("123456"));
            user.setEmail(defaultEmail);
            user.setFirstName("The User");
            user.setLastName("User"+i);
            user.setRoles(Set.of(userRole));
            userRepository.save(user);
            System.out.println("User "+i+" Create Successfully.");

        }
    }

    private void createDefaultAdminIfNotExist() {
        Role adminRole = roleRepository.findByName("ROLE_ADMIN");
        for (int i = 1 ; i<=2 ; i++){
            String defaultEmail = "admin"+i+"@gmail.com";
            if(userRepository.existsByEmail(defaultEmail)){
                continue;
            }
            User user = new User();
            user.setPassword(passwordEncoder.encode("123456"));
            user.setEmail(defaultEmail);
            user.setFirstName("The Admin");
            user.setLastName("Admin"+i);
            user.setRoles(Set.of(adminRole));
            userRepository.save(user);
            System.out.println("Admin "+i+" Created Successfully.");

        }
    }
    private void createDefaultRoleIfNotExist(Set<String> roles){
        roles.stream()
                .filter(role->roleRepository.findByName(role) == null)
                .map(Role::new)
                .forEach(roleRepository::save);
    }
}
