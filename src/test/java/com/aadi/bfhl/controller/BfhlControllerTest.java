package com.aadi.bfhl.controller;

import com.aadi.bfhl.dto.BfhlRequestDTO;
import com.aadi.bfhl.dto.BfhlResponseDTO;
import com.aadi.bfhl.service.BfhlService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for BfhlController using MockMvc.
 * Tests HTTP layer: routes, status codes, response structure, and error handling.
 */
@WebMvcTest(BfhlController.class)
class BfhlControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BfhlService bfhlService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /bfhl returns 200 with correct response structure")
    void testPostBfhlSuccess() throws Exception {
        BfhlResponseDTO mockResponse = BfhlResponseDTO.builder()
                .isSuccess(true)
                .userId("aadi_attrey_27082005")
                .email("aadi1753.be23@chitkara.edu.in")
                .rollNumber("2310991753")
                .oddNumbers(List.of("1"))
                .evenNumbers(List.of("334", "4"))
                .alphabets(List.of("A", "R"))
                .specialCharacters(List.of("$"))
                .sum("339")
                .concatString("Ra")
                .build();

        Mockito.when(bfhlService.processData(any(BfhlRequestDTO.class)))
                .thenReturn(mockResponse);

        BfhlRequestDTO request = new BfhlRequestDTO(
                Arrays.asList("a", "1", "334", "4", "R", "$"));

        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.is_success", is(true)))
                .andExpect(jsonPath("$.user_id", is("aadi_attrey_27082005")))
                .andExpect(jsonPath("$.email", is("aadi1753.be23@chitkara.edu.in")))
                .andExpect(jsonPath("$.roll_number", is("2310991753")))
                .andExpect(jsonPath("$.odd_numbers", hasItem("1")))
                .andExpect(jsonPath("$.even_numbers", hasItems("334", "4")))
                .andExpect(jsonPath("$.alphabets", hasItems("A", "R")))
                .andExpect(jsonPath("$.special_characters", hasItem("$")))
                .andExpect(jsonPath("$.sum", is("339")))
                .andExpect(jsonPath("$.concat_string", is("Ra")));
    }

    @Test
    @DisplayName("POST /bfhl with null data returns 400 BAD REQUEST")
    void testPostBfhlNullData() throws Exception {
        String body = "{\"data\": null}";

        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.is_success", is(false)))
                .andExpect(jsonPath("$.error", is("VALIDATION_ERROR")));
    }

    @Test
    @DisplayName("POST /bfhl with missing body returns 400 BAD REQUEST")
    void testPostBfhlMissingBody() throws Exception {
        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.is_success", is(false)));
    }

    @Test
    @DisplayName("GET /health returns 200 with status UP")
    void testHealthCheck() throws Exception {
        mockMvc.perform(get("/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("UP")));
    }

    @Test
    @DisplayName("POST /bfhl with malformed JSON returns 400")
    void testPostBfhlMalformedJson() throws Exception {
        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ this is not valid json }"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.is_success", is(false)));
    }
}
