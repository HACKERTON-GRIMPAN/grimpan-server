package com.grimpan.emodiary.repository;

import com.grimpan.emodiary.domain.LoginProvider;
import com.grimpan.emodiary.domain.type.AuthenticationProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoginProviderRepository extends JpaRepository<LoginProvider, Long> {
    Optional<LoginProvider> findBySocialIdAndProvider(String socialId, AuthenticationProvider provider);
}
