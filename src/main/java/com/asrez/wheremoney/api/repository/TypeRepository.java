package com.asrez.wheremoney.api.repository;

import com.asrez.wheremoney.api.entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeRepository extends JpaRepository<Type, Long> {
}
