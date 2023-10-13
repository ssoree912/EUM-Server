package eum.backed.server.domain.community.user;

import eum.backed.server.enums.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Boolean existsByEmail(String email);

    Boolean existsByEmailAndRole(String email, Role role);

    Boolean existsByRole(Role role);
    Optional<Users> findByEmail(String username);

    Optional<Users> findByEmailAndPassword(String email, String password);

}
