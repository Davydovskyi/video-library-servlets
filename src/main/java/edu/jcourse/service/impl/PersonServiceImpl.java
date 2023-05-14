package edu.jcourse.service.impl;

import edu.jcourse.dao.DAOProvider;
import edu.jcourse.dao.PersonDAO;
import edu.jcourse.dto.CreatePersonDTO;
import edu.jcourse.service.PersonService;

public class PersonServiceImpl implements PersonService {

    private final PersonDAO personDAO = DAOProvider.getInstance().getPersonDAO();

    @Override
    public Long create(CreatePersonDTO createPersonDTO) {
        return null;
    }
}
