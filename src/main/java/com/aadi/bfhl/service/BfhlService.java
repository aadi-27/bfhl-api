package com.aadi.bfhl.service;

import com.aadi.bfhl.dto.BfhlRequestDTO;
import com.aadi.bfhl.dto.BfhlResponseDTO;

/**
 * Service interface defining the contract for BFHL business logic.
 * All processing of the input data array is defined here.
 */
public interface BfhlService {

    /**
     * Processes the input data array and returns a structured response
     * containing categorized elements and computed values.
     *
     * @param requestDTO the request containing the input data array
     * @return BfhlResponseDTO with all processed fields
     */
    BfhlResponseDTO processData(BfhlRequestDTO requestDTO);
}
