package com.meubles.Repository;

import com.meubles.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    boolean existsByEmail(String email);

    // Vous pouvez garder celle-ci si vous en avez besoin
    List<UserEntity> findByLastname(String lastname);
}