package hr.rba.creditcardservice.common;

import hr.rba.creditcardservice.jpa.entity.user.UserEntity;
import hr.rba.creditcardservice.openapi.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User mapTo(UserEntity userEntity);
}
