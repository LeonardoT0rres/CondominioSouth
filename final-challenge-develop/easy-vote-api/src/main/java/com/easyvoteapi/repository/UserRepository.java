package com.easyvoteapi.repository;

import com.easyvoteapi.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByName(String name);

    Optional<User> findByEmail(String email);

    @Query(value = "SELECT * FROM tb_users " +
            "WHERE (name = :name OR email = :email) " +
            "AND status = :status", nativeQuery = true)
    Optional<User> findByNameOrEmailAndStatus(String name, String email, String status);

    @Query(value = "SELECT * FROM tb_users " +
            "WHERE (name = :name OR email = :email) " +
            "AND status = :status " +
            "AND id <> :id", nativeQuery = true)
    Optional<User> findByNameOrEmailAndStatusIdIsNot(String name, String email, String status, Long id);
}
