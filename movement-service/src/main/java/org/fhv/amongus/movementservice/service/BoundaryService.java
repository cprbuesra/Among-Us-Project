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

    private List<List<Integer>> mapBounds;
    private final ResourceLoader resourceLoader;

    @PostConstruct
    public void init() throws IOException {
        //Resource resource = resourceLoader.getResource("classpath:mapBoundsGhostMode.json");
        Resource resource = resourceLoader.getResource("classpath:mapBounds.json");
        ObjectMapper objectMapper = new ObjectMapper();
        mapBounds = objectMapper.readValue(resource.getInputStream(), new TypeReference<>() {
        });
    }

    public boolean isWithinMovementBoundaries(int x, int y) {
        return y >= 0 && y < mapBounds.size() && !mapBounds.get(y).contains(x);
    }
}

