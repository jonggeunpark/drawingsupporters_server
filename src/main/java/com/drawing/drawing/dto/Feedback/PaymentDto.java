package com.drawing.drawing.dto.Feedback;

import com.drawing.drawing.entity.Feedback;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentDto {
    private int start;
    private int end;

    public static PaymentDto of(int start, int end) {
        return new PaymentDto(start, end);
    }
}
