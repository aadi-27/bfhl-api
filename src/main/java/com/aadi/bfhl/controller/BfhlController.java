package com.aadi.bfhl.controller;

import com.aadi.bfhl.dto.BfhlRequestDTO;
import com.aadi.bfhl.dto.BfhlResponseDTO;
import com.aadi.bfhl.service.BfhlService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST Controller exposing the BFHL API endpoints.
 *
 * Endpoints:
 *   POST /bfhl  — main processing endpoint
 *   GET  /health — health check
 */
@RestController
@RequestMapping
public class BfhlController {

    private final BfhlService bfhlService;

    @Autowired
    public BfhlController(BfhlService bfhlService) {
        this.bfhlService = bfhlService;
    }

    /**
     * POST /bfhl
     * Accepts a JSON body with a "data" array.
     * Returns a structured response with categorized elements and computed values.
     *
     * @param requestDTO the request body
     * @return 200 OK with BfhlResponseDTO on success
     */
    @PostMapping("/bfhl")
    public ResponseEntity<BfhlResponseDTO> processBfhl(
            @Valid @RequestBody BfhlRequestDTO requestDTO) {

        BfhlResponseDTO response = bfhlService.processData(requestDTO);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /health
     * Simple health check endpoint to verify the service is running.
     *
     * @return 200 OK with status message
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "message", "BFHL API is running"
        ));
    }
}
