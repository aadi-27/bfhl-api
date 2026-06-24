package com.aadi.bfhl.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Request DTO for POST /bfhl endpoint.
 * Accepts an array of strings to be processed.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BfhlRequestDTO {

    @NotNull(message = "data field is required and cannot be null")
    @JsonProperty("data")
    private List<String> data;
}
