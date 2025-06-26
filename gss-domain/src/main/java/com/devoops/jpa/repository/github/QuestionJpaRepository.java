package com.devoops.jpa.repository.github;

import com.devoops.jpa.entity.github.QuestionEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface QuestionJpaRepository extends JpaRepository<QuestionEntity, Integer> {

    Optional<QuestionEntity> findById(long id);
}
