package hr.rba.creditcardservice.common.mapper;

import hr.rba.creditcardservice.jpa.entity.user.UserEntity;
import hr.rba.creditcardservice.openapi.model.RegisterRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    RegisterRequest mapTo(UserEntity userEntity);
}
