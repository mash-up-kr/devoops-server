package com.devoops.jpa.entity.analysis;

import com.devoops.domain.entity.analysis.AiCharge;
import com.devoops.domain.entity.github.answer.Answer;
import com.devoops.jpa.entity.github.answer.AnswerEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "ai_charge")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AiChargeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int year;

    private int month;

    private double charge;

    public static AiChargeEntity from(AiCharge aiCharge) {
        return new AiChargeEntity(
                null,
                aiCharge.getYear(),
                aiCharge.getMonth(),
                aiCharge.getCharge()
        );
    }

    public AiCharge toDomainEntity() {
        return new AiCharge(
                year,
                month,
                charge
        );
    }
}
