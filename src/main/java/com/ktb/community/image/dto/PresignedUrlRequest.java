package com.ktb.community.image.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PresignedUrlRequest {
    @NotNull
    private ImageType type;

    @NotBlank
    private String fileName;

    @NotBlank
    private String contentType;
}
