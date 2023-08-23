package com.example.socialmediaapi.service.impl;

import com.example.socialmediaapi.exceptions.BadRequestException;
import com.example.socialmediaapi.exceptions.InternalServerErrorException;
import com.example.socialmediaapi.model.Image;
import com.example.socialmediaapi.repository.ImageRepository;
import com.example.socialmediaapi.service.ImageService;
import com.example.socialmediaapi.utils.PhotoValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@AllArgsConstructor
@Slf4j
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;


    @Override
    public Image saveImage(MultipartFile image) {
        PhotoValidator.checkPhoto(image);
        final String fileName = image.getOriginalFilename();

        Image imageEntity = new Image();

        try {
            imageEntity.setImageUrl(image.getBytes());
        } catch (IOException e) {
            log.error("Error occurred when uploading image with name {}", fileName);
            throw InternalServerErrorException.failedToSaveImage();
        }

        return imageRepository.save(imageEntity);
    }
}
