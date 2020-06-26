package com.codegun.kakaopay.application.service;

import com.codegun.kakaopay.application.exception.*;
import com.codegun.kakaopay.domain.seed.Seed;
import com.codegun.kakaopay.infrastructure.jpa.seed.SeedRepository;
import com.codegun.kakaopay.interfaces.dto.req.ReceiveReqDTO;
import com.codegun.kakaopay.interfaces.dto.req.SeedingReqDTO;
import com.codegun.kakaopay.util.ErrorCodes;
import com.codegun.kakaopay.util.TokenGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SeedingService {
    private final SeedRepository seedRepository;
    private final TokenGenerator tokenGenerator;

    public SeedingService(SeedRepository seedRepository, TokenGenerator tokenGenerator) {
        this.seedRepository = seedRepository;
        this.tokenGenerator = tokenGenerator;
    }

    @Transactional
    public Seed seedingMoney(SeedingReqDTO seedingReqDTO, String userId, String roomId) {
        Seed seed = seedingReqDTO.toEntity(Long.parseLong(userId),roomId,tokenGenerator.pub());
        seed.initReceivedCompletedInfos();
        return seedRepository.save(seed);
    }


    @Transactional
    public long receiveMoney(ReceiveReqDTO receiveReqDTO, String userId, String roomId) {
        Seed seed = seedRepository.findByToken(receiveReqDTO.getToken());
        if (seed == null) {
            throw new NotExistTokenException(ErrorCodes.NOT_EXIST_TOKEN,receiveReqDTO.getToken());
        }
        tokenCheck(seed,userId,roomId);
        return seed.receiveMoney(userId);
    }

    private void tokenCheck(Seed seed, String userId, String roomId) {
        if (seed.isDuplicateReceivedUserId(userId)) {
            throw new AlreadySeedingException(ErrorCodes.ALREADY_TOKEN);
        }

        if (seed.isSelfRequest(userId)) {
            throw new SelfSeedingException(ErrorCodes.SELF_SEEDING);
        }

        if (seed.isNotSameRoom(roomId)) {
            throw new NotSameRoomException(ErrorCodes.NOT_SAME_ROOM);
        }

        if (seed.isNotSeedingEffectiveTime()) {
            throw new NotEffectiveTimeException(ErrorCodes.NOT_EFFECTIVE_TIME);
        }
    }

    @Transactional(readOnly = true)
    public Seed findSeed(String userId, String receiveToken) {
        Seed seed = seedRepository.findByToken(receiveToken);
        if (seed == null) {
            throw new NotExistTokenException(ErrorCodes.NOT_EXIST_TOKEN,receiveToken);
        }
        authorityCheck(seed,userId);
        return seed;
    }

    private void authorityCheck(Seed seed, String userId) {
        if (seed.getUserId() != Integer.parseInt(userId)) {
            throw new NotReadOnlySelfException(ErrorCodes.NOT_READ_SELF);
        }

        if (seed.getCreatedAt().isAfter(seed.getCreatedAt().plusDays(7))) {
            throw new NotPeriodException(ErrorCodes.NOT_READ_PERIOD);
        }
    }
}
