package edu.jcourse.service;

import edu.jcourse.dto.CreatePersonDTO;
import edu.jcourse.dto.ReceivePersonDTO;
import edu.jcourse.entity.Person;
import edu.jcourse.entity.User;
import edu.jcourse.exception.ServiceException;
import edu.jcourse.exception.ValidationException;

import java.util.List;

public interface PersonService {

    Person create(CreatePersonDTO createPersonDTO) throws ServiceException, ValidationException;

    List<ReceivePersonDTO> findAll() throws ServiceException;
}
