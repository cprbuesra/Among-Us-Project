package org.fhv.amongus.amongus.user;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User moveUser(Long userId, String direction) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        int movementSpeed = 10;
        switch (direction) {
            case "UP": user.setY(user.getY() - movementSpeed); break;
            case "DOWN": user.setY(user.getY() + movementSpeed); break;
            case "LEFT": user.setX(user.getX() - movementSpeed); break;
            case "RIGHT": user.setX(user.getX() + movementSpeed); break;
        }
        return userRepository.save(user);
    }
}
