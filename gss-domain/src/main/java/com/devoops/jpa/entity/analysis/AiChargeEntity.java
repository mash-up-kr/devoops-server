package com.devoops.jpa.entity.analysis;

import com.devoops.domain.entity.analysis.AiCharge;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "ai_charge",
        uniqueConstraints = {@UniqueConstraint(name= "uk_year_month", columnNames = {"charge_year", "charge_month"})}
)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AiChargeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "charge_year")
    private int year;

    @Column(name = "charge_month")
    private int month;

    @Column(precision = 10, scale = 2)
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
