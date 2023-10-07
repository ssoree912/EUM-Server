package eum.backed.server.service.community;

import com.google.firebase.auth.FirebaseToken;
import eum.backed.server.controller.community.dto.request.UsersRequestDTO;
import eum.backed.server.domain.community.user.Users;
import eum.backed.server.domain.community.user.UsersRepository;
import eum.backed.server.enums.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;


@Service
public class CustomUserService implements UserDetailsService {
    @Autowired
    UsersRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElse(Users.builder().build());
    }
    }

    @Transactional
    public Users create(UsersRequestDTO.SignUp signUp, FirebaseToken decode) {
        Users users = Users.builder()
                .email(decode.getEmail())
                .introduction(signUp.getIntroduction())
                .name(signUp.getName())
                .sex(signUp.getSex())
                .birth(signUp.getBirth())
                .nickname(signUp.getNickname())
                .address(signUp.getAddress())
                .phone(signUp.getPhone())
                .isBanned(false)
                .authorities(Collections.singletonList(Authority.ROLE_USER.name()))
                .totalVolunteerTime(0).build();
        userRepository.save(users);
        return users;
    }
    private UserDetails createUserDetails(Users users) {
        return new User(users.getUsername(), "", users.getAuthorities());
    }
    public UsernamePasswordAuthenticationToken toAuthentication(String email){
        return new UsernamePasswordAuthenticationToken(email, "");
    }
}