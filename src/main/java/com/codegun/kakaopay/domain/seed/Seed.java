package com.codegun.kakaopay.domain.seed;

import com.codegun.kakaopay.application.exception.NotExistReceiveException;
import com.codegun.kakaopay.application.exception.NotExistTokenException;
import com.codegun.kakaopay.util.ErrorCodes;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Entity
@Table(name = "seed",indexes = {
        @Index(columnList = "token", unique = true)
})
@NoArgsConstructor
@Getter
public class Seed {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "SEED_ID")
    private long id;

    @Column(length = 10)
    private String token;

    private BigDecimal seedingAmount;

    private BigDecimal receivedCompletedAmount = BigDecimal.ZERO;

    private int receivedPersonCount;

    @Column(length = 40)
    private long userId;

    @Column(length = 40)
    private String roomId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true) // Aggregate Root 를 위한 일관성 설정
    @JoinColumn(name = "SEED_ID")
    private Set<ReceivedCompletedInfo> receivedCompletedInfos = new HashSet<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Builder
    public Seed(String token, BigDecimal seedingAmount, BigDecimal receivedCompletedAmount, int receivedPersonCount, long userId, String roomId) {
        this.token = token;
        this.seedingAmount = seedingAmount;
        this.receivedCompletedAmount = receivedCompletedAmount;
        this.receivedPersonCount = receivedPersonCount;
        this.userId = userId;
        this.roomId = roomId;
    }
    @Builder
    public Seed(String token, BigDecimal seedingAmount, BigDecimal receivedCompletedAmount, int receivedPersonCount, long userId, String roomId
            , Set<ReceivedCompletedInfo> receivedCompletedInfos, LocalDateTime createdAt) {
        this.token = token;
        this.seedingAmount = seedingAmount;
        this.receivedCompletedAmount = receivedCompletedAmount;
        this.receivedPersonCount = receivedPersonCount;
        this.userId = userId;
        this.roomId = roomId;
        this.receivedCompletedInfos = receivedCompletedInfos;
        this.createdAt = createdAt;
    }

    public void initReceivedCompletedInfos() {
        Set<ReceivedCompletedInfo> infoSet = new HashSet<>();
        int divideCount = this.receivedPersonCount;
        BigDecimal seedingAmount = this.seedingAmount;
        for (int i = 0; i < this.receivedPersonCount; i++) {
            BigDecimal distributionAmount =  distributionSeedingMoney(divideCount,seedingAmount);
            infoSet.add(new ReceivedCompletedInfo(null,distributionAmount));
            seedingAmount = seedingAmount.subtract(distributionAmount);
            divideCount = divideCount -1;
        }
        this.receivedCompletedInfos = infoSet;
    }

    private BigDecimal distributionSeedingMoney(int divideCount,BigDecimal remainingAmount) {
        if (divideCount != 1) {
            BigDecimal maxAmount = remainingAmount.subtract(BigDecimal.valueOf(divideCount));
            int distributionValue = (int) (Math.random() * maxAmount.longValue() + 1);
            return BigDecimal.valueOf(distributionValue);
        }
        return remainingAmount;
    }

    private Optional<ReceivedCompletedInfo> getUndistributedSeed() {
        return this.receivedCompletedInfos.stream()
                .filter(receivedCompletedInfo -> StringUtils.isEmpty(receivedCompletedInfo.getReceivedUserId()))
                .findFirst();
    }

    private void plusReceivedCompletedAmount(BigDecimal receivedAmount) {
        this.receivedCompletedAmount = this.receivedCompletedAmount.add(receivedAmount);
    }

    public long receiveMoney(String userId) {
        Optional<ReceivedCompletedInfo> info =  getUndistributedSeed();
        if (info.isEmpty()) {
            throw new NotExistReceiveException(ErrorCodes.NOT_EXIST_RECEIVE);
        }
        info.ifPresent(receivedCompletedInfo -> {
            plusReceivedCompletedAmount(receivedCompletedInfo.getReceivedAmount());
            receivedCompletedInfo.updateReceivedUserId(userId);
        });
        return info.get().getReceivedAmount().longValue();
    }

    public boolean isDuplicateReceivedUserId(String userId) {
        long count = this.getReceivedCompletedInfos().stream()
                .filter(receivedCompletedInfo -> StringUtils.isNotEmpty(receivedCompletedInfo.getReceivedUserId()))
                .filter(receivedCompletedInfo -> receivedCompletedInfo.getReceivedUserId().equals(userId))
                .count();
        return count > 0;
    }

    public boolean isSelfRequest(String userId) {
        return this.userId == Integer.parseInt(userId);
    }

    public boolean isNotSameRoom(String roomId) {
        return !this.roomId.equals(roomId);
    }

    public boolean isNotSeedingEffectiveTime() {
        return this.createdAt.isAfter(LocalDateTime.now().plusMinutes(10));
    }
}
