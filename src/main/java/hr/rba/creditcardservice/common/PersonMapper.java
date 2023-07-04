package hr.rba.creditcardservice.common;

import hr.rba.creditcardservice.openapi.model.Person;
import hr.rba.creditcardservice.jpa.entity.PersonEntity;
import hr.rba.creditcardservice.jpa.entity.PersonStatus;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PersonMapper {

    PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);

    PersonStatus mapTo(Person.StatusEnum statusEnum);

    PersonEntity mapTo(Person person);

    Person mapTo(PersonEntity personEntity);
}
