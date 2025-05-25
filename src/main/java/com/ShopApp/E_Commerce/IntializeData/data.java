package com.ShopApp.E_Commerce.IntializeData;

import com.ShopApp.E_Commerce.model.User;
import com.ShopApp.E_Commerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class data implements ApplicationListener<ApplicationReadyEvent> {
    private final UserRepository userRepository;
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        createDefaultUserIfNotExist();
    }

    private void createDefaultUserIfNotExist() {
        for (int i = 1 ; i<=5 ; i++){
            String defaultEmail = "user"+i+"@gmail.com";
            if(userRepository.existsByEmail(defaultEmail)){
                continue;
            }
            User user = new User();
            user.setPassword("123456");
            user.setEmail(defaultEmail);
            user.setFirstName("The User");
            user.setLastName("User"+i);
            userRepository.save(user);
            System.out.println("User "+i+" Create Successfully.");

        }
    }
}
