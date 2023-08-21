package com.example.socialmediaapi.mapper.message;

import com.example.socialmediaapi.dto.message.MessageDto;
import com.example.socialmediaapi.model.Message;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    MessageDto toDto(Message message);

    @InheritInverseConfiguration
    Message toEntity(MessageDto messageDto);
}
