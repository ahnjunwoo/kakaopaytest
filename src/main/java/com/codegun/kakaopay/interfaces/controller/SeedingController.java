package com.codegun.kakaopay.interfaces.controller;

import com.codegun.kakaopay.application.exception.*;
import com.codegun.kakaopay.application.service.SeedingService;
import com.codegun.kakaopay.interfaces.dto.assembler.FindSeedingDTOAssembler;
import com.codegun.kakaopay.interfaces.dto.assembler.SeedingDTOAssembler;
import com.codegun.kakaopay.interfaces.dto.req.ReceiveReqDTO;
import com.codegun.kakaopay.interfaces.dto.req.SeedingReqDTO;
import com.codegun.kakaopay.interfaces.dto.res.*;
import com.codegun.kakaopay.util.ErrorCodes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.codegun.kakaopay.interfaces.dto.req.CommonHeader.X_USER_ID;
import static com.codegun.kakaopay.interfaces.dto.req.CommonHeader.X_ROOM_ID;

@Slf4j
@Api("돈 뿌리기/받기/조회 API")
@RestController
public class SeedingController {
    private final SeedingService seedingService;

    public SeedingController(SeedingService seedingService) {
        this.seedingService = seedingService;
    }

    @ApiOperation(value = "돈 뿌리기 API")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = SeedingRes.class)
    })
    @PostMapping(path = "/v1/seeds")
    public ResponseEntity seedingMoney(@RequestBody @Valid SeedingReqDTO seedingReqDTO
            , @RequestHeader(X_USER_ID) String userId
            ,@RequestHeader(X_ROOM_ID) String roomId) {
        SeedingRes seedingRes = new SeedingRes();
        try {
            seedingRes.setData(SeedingDTOAssembler.toDTO(
                    seedingService.seedingMoney(seedingReqDTO,userId,roomId)));
        }catch (Exception e) {
            log.error("seedingMoney Failed..", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new KakaoResultRes<>(ResponseCodes.SERVER_ERROR, e.getLocalizedMessage(),
                            ErrorCodes.INTERNAL_SERVER_ERROR));
        }
        return ResponseEntity.ok(seedingRes);
    }

    @ApiOperation(value = "받기 API")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = ReceiveRes.class)
    })
    @PutMapping(path = "/v1/seeds")
    public ResponseEntity receiveMoney(@RequestBody @Valid ReceiveReqDTO receiveReqDTO
            , @RequestHeader(X_USER_ID) String userId
            ,@RequestHeader(X_ROOM_ID) String roomId) {
        ReceiveRes receiveRes = new ReceiveRes();
        try {
            receiveRes.setData(ReceiveResDTO.builder()
                    .receivedAmount(seedingService.receiveMoney(receiveReqDTO,userId,roomId))
                    .build());
        }catch (NotEffectiveTimeException | NotExistTokenException | NotSameRoomException | SelfSeedingException | UnUsableTokenTimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new KakaoResultRes<>(e.errorCode(), e.getMessage()));
        }catch (Exception e) {
            log.error("receiveMoney Failed..", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new KakaoResultRes<>(ResponseCodes.SERVER_ERROR, e.getLocalizedMessage(),
                            ErrorCodes.INTERNAL_SERVER_ERROR));
        }
        return ResponseEntity.ok(receiveRes);
    }

    @ApiOperation(value = "조회 API")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = FindReceiveRes.class)
    })
    @GetMapping(path = "/v1/seeds")
    public ResponseEntity findSeed(@RequestParam("token") String token
            , @RequestHeader(X_USER_ID) String userId) {
        FindReceiveRes findReceiveRes = new FindReceiveRes();
        try {
            findReceiveRes.setData(FindSeedingDTOAssembler.toDTO(seedingService.findSeed(userId, token)));
        }catch (NotPeriodException | NotReadOnlySelfException | NotExistTokenException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new KakaoResultRes<>(e.errorCode(), e.getMessage()));
        }catch (Exception e) {
            log.error("findSeed Failed..", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new KakaoResultRes<>(ResponseCodes.SERVER_ERROR, e.getLocalizedMessage(),
                            ErrorCodes.INTERNAL_SERVER_ERROR));
        }
        return ResponseEntity.ok(findReceiveRes);
    }



}
