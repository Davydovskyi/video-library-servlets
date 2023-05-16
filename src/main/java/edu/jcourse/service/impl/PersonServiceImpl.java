package edu.jcourse.service.impl;

import edu.jcourse.dao.DAOProvider;
import edu.jcourse.dao.PersonDAO;
import edu.jcourse.dto.CreatePersonDTO;
import edu.jcourse.entity.Person;
import edu.jcourse.exception.DAOException;
import edu.jcourse.exception.ServiceException;
import edu.jcourse.exception.ValidationException;
import edu.jcourse.mapper.MapperProvider;
import edu.jcourse.mapper.impl.CreatePersonMapper;
import edu.jcourse.service.PersonService;
import edu.jcourse.validator.ValidationResult;
import edu.jcourse.validator.ValidatorProvider;
import edu.jcourse.validator.impl.CreatePersonValidator;

public class PersonServiceImpl implements PersonService {

    private final PersonDAO personDAO = DAOProvider.getInstance().getPersonDAO();
    private final CreatePersonValidator createPersonValidator = ValidatorProvider.getInstance().getCreatePersonValidator();
    private final CreatePersonMapper createPersonMapper = MapperProvider.getInstance().getCreatePersonMapper();

    @Override
    public Long create(CreatePersonDTO createPersonDTO) throws ServiceException, ValidationException {
        ValidationResult validationResult = createPersonValidator.isValid(createPersonDTO);
        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getErrors());
        }
        Person person = createPersonMapper.mapFrom(createPersonDTO);

        try {
            return personDAO.save(person).getId();
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
    }
}
