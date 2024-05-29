package org.fhv.amongus.movementservice.controller;

import lombok.RequiredArgsConstructor;
import org.fhv.amongus.movementservice.DTO.PlayerStatusUpdateRequest;
import org.fhv.amongus.movementservice.service.MovementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/movement")
public class MovementController {


    private final MovementService movementService;
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @PutMapping("/status")
    public ResponseEntity<String> updatePlayerStatus(@RequestBody PlayerStatusUpdateRequest request) {
        logger.info("Updating player status for movement with this request: {}", request);
        //movementService.updatePlayerStatus(request.getPlayerId(), request.getStatus());
        return ResponseEntity.ok("Player status updated for movement");
    }
}
