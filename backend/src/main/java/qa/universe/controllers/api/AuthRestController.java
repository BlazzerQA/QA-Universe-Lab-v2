package qa.universe.controllers.api;

import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import qa.universe.dto.LoginRequest;
import qa.universe.dto.ProfileRequest;
import qa.universe.dto.RegistrationRequest;
import qa.universe.models.User;
import qa.universe.repositories.UserRepository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class AuthRestController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final Map<String, Long> tokenStore = new ConcurrentHashMap<>();

    @PostConstruct
    public void initTestUser() {
        if (userRepository.findUserByPhone("+79991234567").isEmpty()) {
            String encodedPassword = passwordEncoder.encode("password123");
            userRepository.save(new User(null, "+79991234567", encodedPassword));
            System.out.println("Тестовый пользователь создан!");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegistrationRequest request) {
        Optional<User> existingUser = userRepository.findUserByPhone(request.getPhone());
        if (existingUser.isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Номер уже зарегистрирован"));
        }

        User newUser = new User();
        newUser.setPhone(request.getPhone());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        if (request.getFullName() != null) {
            newUser.setFullName(request.getFullName().trim());
        }

        userRepository.save(newUser);
        String randomToken = UUID.randomUUID().toString();
        tokenStore.put(randomToken, newUser.getId());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of("message", "Пользователь успешно зарегистрирован",
                        "phone", request.getPhone(),
                        "token", randomToken));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        var userOpt = userRepository.findUserByPhone(request.getPhone());
        if (userOpt.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Неверный логин или пароль"));
        }

        var user = userOpt.get();
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Неверный логин или пароль"));
        }

        String randomToken = UUID.randomUUID().toString();
        tokenStore.put(randomToken, user.getId());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Map.of("token", randomToken));
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        Long userId = extractUserId(authHeader);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Не авторизован"));
        }

        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Пользователь не найден"));
        }

        ProfileRequest dto = new ProfileRequest();
        dto.setPhone(user.getPhone());
        dto.setFullName(user.getFullName());

        return ResponseEntity.ok(dto);
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestBody ProfileRequest profileDto,
                                           @RequestHeader(value = "Authorization", required = false) String authHeader) {
        Long userId = extractUserId(authHeader);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Не авторизован"));
        }

        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Пользователь не найден"));
        }

        String fullName = profileDto.getFullName();
        if (fullName != null) {
            fullName = fullName.trim();
        }

        if (fullName == null || fullName.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Имя не должно быть пустым!"));
        }

        if (fullName.length() > 50) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Имя не должно превышать 50 символов!"));
        }

        user.setFullName(fullName);
        userRepository.save(user);

        return ResponseEntity.ok(Map.of("message", "Данные обновлены", "fullName", user.getFullName()));
    }

    private Long extractUserId(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        String token = authHeader.substring(7);
        return tokenStore.get(token);
    }
}
