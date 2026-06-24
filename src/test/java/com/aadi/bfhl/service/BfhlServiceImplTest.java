package com.aadi.bfhl.service;

import com.aadi.bfhl.dto.BfhlRequestDTO;
import com.aadi.bfhl.dto.BfhlResponseDTO;
import com.aadi.bfhl.service.impl.BfhlServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for BfhlServiceImpl.
 * Tests all examples from the spec plus edge cases.
 */
class BfhlServiceImplTest {

    private BfhlServiceImpl bfhlService;

    @BeforeEach
    void setUp() {
        bfhlService = new BfhlServiceImpl();
        // Inject properties manually since there's no Spring context
        ReflectionTestUtils.setField(bfhlService, "fullName", "aadi_attrey");
        ReflectionTestUtils.setField(bfhlService, "dob", "27082055");
        ReflectionTestUtils.setField(bfhlService, "email", "aadi1753.be23@chitkara.edu.in");
        ReflectionTestUtils.setField(bfhlService, "rollNumber", "2310991753");
    }

    // ─── Example A from spec ──────────────────────────────────────────────────

    @Test
    @DisplayName("Example A: mixed input with numbers, letters, special char")
    void testExampleA() {
        BfhlRequestDTO req = new BfhlRequestDTO(Arrays.asList("a", "1", "334", "4", "R", "$"));
        BfhlResponseDTO res = bfhlService.processData(req);

        assertTrue(res.isSuccess());
        assertEquals("aadi_attrey_27082055", res.getUserId());
        assertEquals("aadi1753.be23@chitkara.edu.in", res.getEmail());
        assertEquals("2310991753", res.getRollNumber());

        assertEquals(List.of("1"), res.getOddNumbers());
        assertEquals(List.of("334", "4"), res.getEvenNumbers());
        assertEquals(List.of("A", "R"), res.getAlphabets());
        assertEquals(List.of("$"), res.getSpecialCharacters());
        assertEquals("339", res.getSum());
        // chars in order: a, R → reversed: R, a → alternating caps: R, a → "Ra"
        assertEquals("Ra", res.getConcatString());
    }

    // ─── Example B from spec ──────────────────────────────────────────────────

    @Test
    @DisplayName("Example B: multiple special chars, multiple numbers and letters")
    void testExampleB() {
        BfhlRequestDTO req = new BfhlRequestDTO(
                Arrays.asList("2", "a", "y", "4", "&", "-", "*", "5", "92", "b"));
        BfhlResponseDTO res = bfhlService.processData(req);

        assertTrue(res.isSuccess());
        assertEquals(List.of("5"), res.getOddNumbers());
        assertEquals(List.of("2", "4", "92"), res.getEvenNumbers());
        assertEquals(List.of("A", "Y", "B"), res.getAlphabets());
        assertEquals(List.of("&", "-", "*"), res.getSpecialCharacters());
        assertEquals("103", res.getSum());
        // chars in order: a, y, b → reversed: b, y, a → alternating caps: B, y, A → "ByA"
        assertEquals("ByA", res.getConcatString());
    }

    // ─── Example C from spec ──────────────────────────────────────────────────

    @Test
    @DisplayName("Example C: only multi-char alphabetic tokens")
    void testExampleC() {
        BfhlRequestDTO req = new BfhlRequestDTO(Arrays.asList("A", "ABCD", "DOE"));
        BfhlResponseDTO res = bfhlService.processData(req);

        assertTrue(res.isSuccess());
        assertEquals(List.of(), res.getOddNumbers());
        assertEquals(List.of(), res.getEvenNumbers());
        assertEquals(List.of("A", "ABCD", "DOE"), res.getAlphabets());
        assertEquals(List.of(), res.getSpecialCharacters());
        assertEquals("0", res.getSum());
        // chars in order: A, A,B,C,D, D,O,E → reversed: E,O,D,D,C,B,A,A
        // alternating caps: E,o,D,d,C,b,A,a → "EoDdCbAa"
        assertEquals("EoDdCbAa", res.getConcatString());
    }

    // ─── Edge cases ───────────────────────────────────────────────────────────

    @Test
    @DisplayName("Empty data array returns zeroed response")
    void testEmptyArray() {
        BfhlRequestDTO req = new BfhlRequestDTO(Collections.emptyList());
        BfhlResponseDTO res = bfhlService.processData(req);

        assertTrue(res.isSuccess());
        assertEquals(List.of(), res.getOddNumbers());
        assertEquals(List.of(), res.getEvenNumbers());
        assertEquals(List.of(), res.getAlphabets());
        assertEquals(List.of(), res.getSpecialCharacters());
        assertEquals("0", res.getSum());
        assertEquals("", res.getConcatString());
    }

    @Test
    @DisplayName("Only numbers — no alphabets or special chars")
    void testOnlyNumbers() {
        BfhlRequestDTO req = new BfhlRequestDTO(Arrays.asList("3", "6", "11", "100"));
        BfhlResponseDTO res = bfhlService.processData(req);

        assertTrue(res.isSuccess());
        assertEquals(List.of("3", "11"), res.getOddNumbers());
        assertEquals(List.of("6", "100"), res.getEvenNumbers());
        assertEquals(List.of(), res.getAlphabets());
        assertEquals(List.of(), res.getSpecialCharacters());
        assertEquals("120", res.getSum());
        assertEquals("", res.getConcatString());
    }

    @Test
    @DisplayName("Only special characters — no numbers or alphabets")
    void testOnlySpecialChars() {
        BfhlRequestDTO req = new BfhlRequestDTO(Arrays.asList("@", "#", "!"));
        BfhlResponseDTO res = bfhlService.processData(req);

        assertTrue(res.isSuccess());
        assertEquals(List.of(), res.getOddNumbers());
        assertEquals(List.of(), res.getEvenNumbers());
        assertEquals(List.of(), res.getAlphabets());
        assertEquals(List.of("@", "#", "!"), res.getSpecialCharacters());
        assertEquals("0", res.getSum());
        assertEquals("", res.getConcatString());
    }

    @Test
    @DisplayName("user_id format is correct: fullname_ddmmyyyy")
    void testUserId() {
        BfhlRequestDTO req = new BfhlRequestDTO(List.of("1"));
        BfhlResponseDTO res = bfhlService.processData(req);
        assertEquals("aadi_attrey_27082055", res.getUserId());
    }

    @Test
    @DisplayName("Sum is returned as a string, not a number")
    void testSumIsString() {
        BfhlRequestDTO req = new BfhlRequestDTO(Arrays.asList("5", "10"));
        BfhlResponseDTO res = bfhlService.processData(req);
        assertInstanceOf(String.class, res.getSum());
        assertEquals("15", res.getSum());
    }

    @Test
    @DisplayName("Numbers are returned as strings in odd/even arrays")
    void testNumbersReturnedAsStrings() {
        BfhlRequestDTO req = new BfhlRequestDTO(Arrays.asList("1", "2"));
        BfhlResponseDTO res = bfhlService.processData(req);
        assertEquals("1", res.getOddNumbers().get(0));
        assertEquals("2", res.getEvenNumbers().get(0));
    }

    @Test
    @DisplayName("Single alphabet character concat is just uppercase of it")
    void testSingleAlphabetConcat() {
        BfhlRequestDTO req = new BfhlRequestDTO(List.of("z"));
        BfhlResponseDTO res = bfhlService.processData(req);
        // chars: [z] → reversed: [z] → index 0 → uppercase → "Z"
        assertEquals("Z", res.getConcatString());
    }
}
