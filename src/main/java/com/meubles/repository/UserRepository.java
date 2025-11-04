package com.meubles.repository;

import com.meubles.Entity.UserEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    List<UserEntity> findByLastname(String lastname);

    Optional<UserEntity> findByEmail(String email);

    boolean existsByEmail(String email);
}