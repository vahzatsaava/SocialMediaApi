package com.example.socialmediaapi.mapper;

import com.example.socialmediaapi.dto.PostDto;
import com.example.socialmediaapi.model.Post;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface PostMapper {

    PostDto toDto(Post post);

    @InheritInverseConfiguration
    Post toEntity(PostDto postDto);

}
