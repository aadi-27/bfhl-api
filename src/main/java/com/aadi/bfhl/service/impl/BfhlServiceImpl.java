package com.aadi.bfhl.service.impl;

import com.aadi.bfhl.dto.BfhlRequestDTO;
import com.aadi.bfhl.dto.BfhlResponseDTO;
import com.aadi.bfhl.service.BfhlService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of BfhlService.
 * Contains all core logic for processing the input data array.
 */
@Service
public class BfhlServiceImpl implements BfhlService {

    @Value("${app.user.full-name}")
    private String fullName;

    @Value("${app.user.dob}")
    private String dob;

    @Value("${app.user.email}")
    private String email;

    @Value("${app.user.roll-number}")
    private String rollNumber;

    /**
     * Processes each element in the input array:
     * - Numeric strings  → classified as odd or even; summed
     * - Alpha strings    → uppercased; used for concat logic
     * - Other strings    → classified as special characters
     *
     * concat_string logic:
     *   1. Collect all alphabetical characters across all elements (maintaining order)
     *   2. Reverse the collected sequence
     *   3. Apply alternating caps: index 0 → uppercase, index 1 → lowercase, etc.
     */
    @Override
    public BfhlResponseDTO processData(BfhlRequestDTO requestDTO) {

        List<String> data = requestDTO.getData();

        List<String> oddNumbers       = new ArrayList<>();
        List<String> evenNumbers      = new ArrayList<>();
        List<String> alphabets        = new ArrayList<>();
        List<String> specialChars     = new ArrayList<>();
        List<Character> allAlphaChars = new ArrayList<>(); // raw chars for concat logic
        long sum = 0;

        for (String item : data) {
            if (isNumeric(item)) {
                // Numeric element
                long num = Long.parseLong(item);
                sum += num;
                if (num % 2 == 0) {
                    evenNumbers.add(item);
                } else {
                    oddNumbers.add(item);
                }
            } else if (isAllAlphabetic(item)) {
                // Pure alphabetic element — uppercase the whole token
                alphabets.add(item.toUpperCase());
                // Collect individual chars for concat
                for (char c : item.toCharArray()) {
                    allAlphaChars.add(c);
                }
            } else {
                // Special character element
                specialChars.add(item);
            }
        }

        // Build concat_string: reverse chars → alternating caps
        String concatString = buildConcatString(allAlphaChars);

        // Build user_id: full_name_ddmmyyyy
        String userId = fullName + "_" + dob;

        return BfhlResponseDTO.builder()
                .isSuccess(true)
                .userId(userId)
                .email(email)
                .rollNumber(rollNumber)
                .oddNumbers(oddNumbers)
                .evenNumbers(evenNumbers)
                .alphabets(alphabets)
                .specialCharacters(specialChars)
                .sum(String.valueOf(sum))
                .concatString(concatString)
                .build();
    }

    // ─── Helpers ─────────────────────────────────────────────────────────────

    /**
     * Returns true if the string represents a non-negative integer.
     * Handles multi-digit numbers like "334".
     */
    private boolean isNumeric(String s) {
        if (s == null || s.isEmpty()) return false;
        for (char c : s.toCharArray()) {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }

    /**
     * Returns true if every character in the string is an ASCII letter.
     */
    private boolean isAllAlphabetic(String s) {
        if (s == null || s.isEmpty()) return false;
        for (char c : s.toCharArray()) {
            if (!Character.isLetter(c)) return false;
        }
        return true;
    }

    /**
     * Builds the concat_string from collected alphabetical characters:
     * 1. Reverse the list
     * 2. Apply alternating caps (index 0 → upper, 1 → lower, 2 → upper, ...)
     *
     * Example: chars = [a, b, c, d] → reversed = [d, c, b, a]
     *          → alternating caps = "DcBa"
     */
    private String buildConcatString(List<Character> chars) {
        if (chars.isEmpty()) return "";

        // Reverse
        List<Character> reversed = new ArrayList<>();
        for (int i = chars.size() - 1; i >= 0; i--) {
            reversed.add(chars.get(i));
        }

        // Alternating caps
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < reversed.size(); i++) {
            char c = reversed.get(i);
            if (i % 2 == 0) {
                sb.append(Character.toUpperCase(c));
            } else {
                sb.append(Character.toLowerCase(c));
            }
        }
        return sb.toString();
    }
}
