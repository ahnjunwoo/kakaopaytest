package com.codegun.kakaopay.infrastructure.jpa.seed;

import com.codegun.kakaopay.domain.seed.Seed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SeedRepository extends JpaRepository<Seed, Long> {
    Seed findByToken(String token);
}
