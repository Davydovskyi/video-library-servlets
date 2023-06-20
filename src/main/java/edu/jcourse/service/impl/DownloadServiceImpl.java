package edu.jcourse.service.impl;

import edu.jcourse.dto.ReceiveMovieDto;
import edu.jcourse.mapper.MapperProvider;
import edu.jcourse.mapper.impl.CSVMovieMapper;
import edu.jcourse.service.DownloadService;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

public class DownloadServiceImpl implements DownloadService {

    private final CSVMovieMapper csvMovieMapper;

    public DownloadServiceImpl() {
        this(MapperProvider.getInstance().getCsvMovieMapper());
    }

    public DownloadServiceImpl(CSVMovieMapper csvMovieMapper) {
        this.csvMovieMapper = csvMovieMapper;
    }

    @Override
    public InputStream get(List<ReceiveMovieDto> movies) {
        if (movies == null) {
            return new ByteArrayInputStream(new byte[0]);
        }

        byte[] bytes = movies.stream()
                .map(csvMovieMapper::mapFrom)
                .collect(Collectors.joining("\n"))
                .getBytes();
        return new ByteArrayInputStream(bytes);
    }
}