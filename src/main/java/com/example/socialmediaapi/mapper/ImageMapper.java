package com.example.socialmediaapi.mapper;

import com.example.socialmediaapi.dto.ImageDto;
import com.example.socialmediaapi.model.Image;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface ImageMapper {

    Image toEntity(ImageDto imageDtos);

    @InheritInverseConfiguration
    ImageDto toDto(Image image);


}
