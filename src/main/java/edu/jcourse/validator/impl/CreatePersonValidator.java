package edu.jcourse.validator.impl;

import edu.jcourse.dao.DaoProvider;
import edu.jcourse.dao.PersonDao;
import edu.jcourse.dto.CreatePersonDto;
import edu.jcourse.entity.Person;
import edu.jcourse.exception.DAOException;
import edu.jcourse.exception.ServiceException;
import edu.jcourse.util.CodeUtil;
import edu.jcourse.util.LocalDateFormatter;
import edu.jcourse.util.MessageUtil;
import edu.jcourse.validator.Error;
import edu.jcourse.validator.ValidationResult;
import edu.jcourse.validator.Validator;

import java.util.Optional;

public class CreatePersonValidator implements Validator<CreatePersonDto> {

    private final PersonDao personDAO;

    public CreatePersonValidator() {
        personDAO = DaoProvider.getInstance().getPersonDao();
    }

    public CreatePersonValidator(PersonDao personDAO) {
        this.personDAO = personDAO;
    }

    @Override
    public ValidationResult validate(CreatePersonDto createPersonDTO) throws ServiceException {
        ValidationResult validationResult = new ValidationResult();
        CommonValidator.nameValidation(validationResult, createPersonDTO.name());
        CommonValidator.birthdayValidation(validationResult, createPersonDTO.birthDate());

        if (validationResult.isValid()) {
            checkForDuplicate(validationResult, createPersonDTO.name(), createPersonDTO.birthDate());
        }
        return validationResult;
    }

    private void checkForDuplicate(ValidationResult validationResult, String name, String birthDate) throws ServiceException {
        try {
            Optional<Person> person = personDAO.findByNameAndBirthDate(name, LocalDateFormatter.parse(birthDate));
            person.ifPresent(it -> validationResult.add(Error.of(CodeUtil.EXIST_PERSON_CODE, MessageUtil.PERSON_EXISTS_MESSAGE)));
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }
}