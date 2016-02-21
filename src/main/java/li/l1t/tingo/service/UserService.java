package li.l1t.tingo.service;

import li.l1t.tingo.config.TingoConfiguration;
import li.l1t.tingo.exception.JsonPropagatingException;
import li.l1t.tingo.model.User;
import li.l1t.tingo.model.UserAuthority;
import li.l1t.tingo.model.repo.AuthorityRepository;
import li.l1t.tingo.model.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service handling user registration.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-02-21
 */
@Service
public class UserService {
    private UserRepository userRepository;
    private TingoConfiguration tingoConfiguration;
    private AuthorityRepository authorityRepository;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public UserService(UserRepository userRepository, TingoConfiguration tingoConfiguration,
                       AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.tingoConfiguration = tingoConfiguration;
        this.authorityRepository = authorityRepository;
    }

    public User createUser(String username, String password, String registerSecret) {
        System.out.println("Secret: "+registerSecret);
        if(!tingoConfiguration.getRegisterSecret().equalsIgnoreCase(registerSecret)) {
            throw new JsonPropagatingException("Falscher Geheimcode!");
        }

        if(userRepository.findByName(username) != null) {
            throw new JsonPropagatingException("Benutzername schon vergeben!");
        }

        User user = userRepository.save(new User(username, passwordEncoder.encode(password), true));
        authorityRepository.save(new UserAuthority(user.getName(), "default"));
        return user;
    }
}
