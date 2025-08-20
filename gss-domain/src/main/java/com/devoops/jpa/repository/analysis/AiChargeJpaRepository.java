package com.devoops.jpa.repository.analysis;

import com.devoops.jpa.entity.analysis.AiChargeEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AiChargeJpaRepository extends JpaRepository<AiChargeEntity, Long> {


    Optional<AiChargeEntity> findByYearAndMonth(int todayYear, int todayMonth);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            update AiChargeEntity ai_charge
            set ai_charge.charge = ai_charge.charge + :charge
            where ai_charge.year = :year
                        and ai_charge.month = :month
                        and ai_charge.charge >= 0
            """)
    void updateChargeById(
            @Param("year") int year,
            @Param("month") int month,
            @Param("charge") double charge
    );
}
