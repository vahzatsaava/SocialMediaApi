package com.example.socialmediaapi.service;

import com.example.socialmediaapi.dto.ImageDto;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    ImageDto addPicture(MultipartFile image, Long postId);
    ImageDto updatePicture(MultipartFile image,Long imageId);
}
