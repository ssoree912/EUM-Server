package eum.backed.server.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Boolean existsByEmail(String email);

    Optional<Users> findByEmail(String username);
}
