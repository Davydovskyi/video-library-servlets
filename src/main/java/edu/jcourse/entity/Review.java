package edu.jcourse.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {
    private Long id;
    private Movie movie;
    private User user;
    private String text;
    private Short rate;
}
