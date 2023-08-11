package com.grimpan.emodiary.repository;

import com.grimpan.emodiary.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import com.grimpan.emodiary.type.EUserRole;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u.id AS id, u.role AS role FROM User u WHERE u.id = :userId AND u.isLogin = true AND u.refreshToken = :refreshToken")
    Optional<UserLoginForm> findByIdAndRefreshToken(@Param("userId") Long userId, @Param("refreshToken") String refreshToken);

    Boolean existsByPhoneNumber(String phoneNumber);

    Optional<User> findByIdAndIsLoginAndRefreshTokenIsNotNull(Long userId, Boolean isLogin);

    Optional<User> findByPhoneNumber(String phoneNumber);

    public interface UserLoginForm {
        Long getId();
        EUserRole getRole();
    }
}
