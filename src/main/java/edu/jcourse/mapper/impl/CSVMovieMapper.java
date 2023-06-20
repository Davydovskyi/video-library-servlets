package edu.jcourse.mapper.impl;

import edu.jcourse.dto.ReceiveMovieDto;
import edu.jcourse.mapper.Mapper;

import java.util.StringJoiner;

public class CSVMovieMapper implements Mapper<ReceiveMovieDto, String> {

    private static final String COLUMN_SEPARATOR = ",";

    @Override
    public String mapFrom(ReceiveMovieDto receiveMovieDto) {
        StringJoiner stringJoiner = new StringJoiner(COLUMN_SEPARATOR);
        stringJoiner.add(receiveMovieDto.title());
        stringJoiner.add(String.valueOf(receiveMovieDto.releaseYear()));
        stringJoiner.add(receiveMovieDto.genre());
        stringJoiner.add(receiveMovieDto.country());
        stringJoiner.add(receiveMovieDto.description());
        return stringJoiner.toString();
    }
}