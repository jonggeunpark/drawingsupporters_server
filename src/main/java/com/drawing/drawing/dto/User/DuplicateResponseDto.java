package com.drawing.drawing.dto.User;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DuplicateResponseDto {

    public boolean is_duplicate;

    public static DuplicateResponseDto of(boolean isDuplicate) {
        return new DuplicateResponseDto(isDuplicate);
    }
}
