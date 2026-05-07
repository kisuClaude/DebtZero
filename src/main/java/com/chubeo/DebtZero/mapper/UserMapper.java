package com.chubeo.DebtZero.mapper;

import com.chubeo.DebtZero.dto.request.UserCreationRequest;
import com.chubeo.DebtZero.dto.response.UserCreationResponse;
import com.chubeo.DebtZero.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser (UserCreationRequest request);
    UserCreationResponse toUserResponse(User user);
}
