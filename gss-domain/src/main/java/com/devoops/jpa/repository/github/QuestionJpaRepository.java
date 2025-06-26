package com.devoops.jpa.repository.github;

import com.devoops.jpa.entity.github.QuestionEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionJpaRepository extends JpaRepository<QuestionEntity, Integer> {

    QuestionEntity findByIdAndUser_Id(long questionId, long userId);

    Optional<QuestionEntity> findById(long id);
}
