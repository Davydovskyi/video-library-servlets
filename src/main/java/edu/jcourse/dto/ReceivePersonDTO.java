package edu.jcourse.dto;

import lombok.Builder;

@Builder
public record ReceivePersonDTO(Long id,
                               String personData) {
}