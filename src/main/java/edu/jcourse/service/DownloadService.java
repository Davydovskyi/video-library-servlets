package edu.jcourse.service;

import edu.jcourse.dto.ReceiveMovieDTO;

import java.io.InputStream;
import java.util.List;

public interface DownloadService {

    InputStream get(List<ReceiveMovieDTO> movies);

}