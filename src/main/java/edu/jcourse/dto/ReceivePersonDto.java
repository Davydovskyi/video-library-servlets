package edu.jcourse.dto;

import lombok.Builder;

@Builder
public record ReceivePersonDto(Long id,
                               String personData) {
}