package hr.rba.creditcardservice.common.mapper;

import hr.rba.creditcardservice.jpa.entity.user.*;
import hr.rba.creditcardservice.openapi.model.*;
import org.mapstruct.*;
import org.mapstruct.factory.*;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "password", ignore = true)
    UserEntity mapTo(RegisterRequest registerRequest);

    User mapTo(UserEntity userEntity);
}
