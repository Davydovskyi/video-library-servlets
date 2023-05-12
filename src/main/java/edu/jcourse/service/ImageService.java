package edu.jcourse.service;

import edu.jcourse.exception.ServiceException;

import java.io.InputStream;
import java.util.Optional;

public interface ImageService {

    void upload(String imagePath, InputStream imageContent) throws ServiceException;

    Optional<InputStream> get(String imagePath) throws ServiceException;
}
