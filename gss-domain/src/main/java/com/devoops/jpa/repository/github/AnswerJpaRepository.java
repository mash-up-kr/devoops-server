package com.devoops.jpa.repository.github;

import com.devoops.jpa.entity.github.AnswerEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerJpaRepository extends JpaRepository<AnswerEntity, Long> {

}
