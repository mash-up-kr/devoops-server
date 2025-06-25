package com.devoops.jpa.repository.github;

import com.devoops.jpa.entity.github.QuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionJpaRepository extends JpaRepository<QuestionEntity, Integer> {

}
