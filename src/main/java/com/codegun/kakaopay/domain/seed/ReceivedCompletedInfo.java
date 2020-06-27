package com.codegun.kakaopay.domain.seed;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "RECEIVED_COMPLETED_INFO")
@EqualsAndHashCode(callSuper = false)
@Getter
@NoArgsConstructor
public class ReceivedCompletedInfo{
    @Id
    @GeneratedValue
    private Long id;
    private String receivedUserId;
    private BigDecimal receivedAmount;

    ReceivedCompletedInfo(String receivedUserId, BigDecimal receivedAmount) {
        this.receivedUserId = receivedUserId;
        this.receivedAmount = receivedAmount;
    }

    void updateReceivedUserId(String userId) {
        this.receivedUserId = userId;
    }
}
