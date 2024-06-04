package org.fhv.amongus.movementservice.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoundaryService {

    private List<List<Integer>> mapBoundsPlayerAlive;
    private List<List<Integer>> mapBoundsGhostMode;
    private final ResourceLoader resourceLoader;

    @PostConstruct
    public void init() throws IOException {
        Resource resourceGhost = resourceLoader.getResource("classpath:mapBoundsGhostMode.json");
        Resource resourceAlive = resourceLoader.getResource("classpath:mapBounds.json");
        ObjectMapper objectMapper = new ObjectMapper();
        mapBoundsPlayerAlive = objectMapper.readValue(resourceAlive.getInputStream(), new TypeReference<>() {
        });
        mapBoundsGhostMode = objectMapper.readValue(resourceGhost.getInputStream(), new TypeReference<>() {
        });
    }

    public boolean isWithinMovementBoundaries(int x, int y, String status) {
        if (status.equals("ALIVE")) {
            return y >= 0 && y < mapBoundsPlayerAlive.size() && !mapBoundsPlayerAlive.get(y).contains(x);
        } else {
            return y >= 0 && y < mapBoundsGhostMode.size() && !mapBoundsGhostMode.get(y).contains(x);
        }
    }
}

