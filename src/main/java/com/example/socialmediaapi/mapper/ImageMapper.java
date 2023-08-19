package com.example.socialmediaapi.mapper;

import com.example.socialmediaapi.dto.ImageDto;
import com.example.socialmediaapi.model.Image;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ImageMapper {

    List<Image> toEntity(List<ImageDto> imageDtos);

    @InheritInverseConfiguration
    List<ImageDto> toDto(List<Image> image);


}
