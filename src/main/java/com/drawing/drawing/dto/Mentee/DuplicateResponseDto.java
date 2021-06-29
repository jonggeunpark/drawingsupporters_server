package com.drawing.drawing.dto.Mentee;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
public class DuplicateResponseDto {

    public boolean isDuplicate;

    public static DuplicateResponseDto of(boolean isDuplicate) {
        return new DuplicateResponseDto(isDuplicate);
    }
}
