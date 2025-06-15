package com.lld.inventorymanagement.model.response;

import com.lld.inventorymanagement.model.enums.ResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class ApiResponse<T> {
    private ResponseStatus status;
    private ErrorResponse errorResponse;
    private T data;
}