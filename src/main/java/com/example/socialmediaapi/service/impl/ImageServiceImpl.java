package com.example.socialmediaapi.service.impl;

import com.example.socialmediaapi.dto.ImageDto;
import com.example.socialmediaapi.exceptions.InternalServerErrorException;
import com.example.socialmediaapi.mapper.ImageMapper;
import com.example.socialmediaapi.model.Image;
import com.example.socialmediaapi.model.Post;
import com.example.socialmediaapi.repository.ImageRepository;
import com.example.socialmediaapi.service.ImageService;
import com.example.socialmediaapi.utils.PhotoValidator;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@AllArgsConstructor
@Slf4j
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;
    private final PostServiceImpl postService;

    private final ImageMapper imageMapper;


    @Override
    @Transactional
    public ImageDto addPicture(MultipartFile image, Long postId) {
        checkInputValues(image,postId);
        Post postById = postService.findById(postId);
        Image imageSaved = saveImage(image, postById);

        return imageMapper.toDto(imageSaved);
    }

    @Override
    @Transactional
    public ImageDto updatePicture(MultipartFile image, Long imageId) {
        checkInputValues(image,imageId);

        Image imageById = imageRepository.findById(imageId)
                        .orElseThrow(() -> new EntityNotFoundException("Image with id " + imageId + " not found"));
        try {
            imageById.setImageUrl(image.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageMapper.toDto(imageById);
    }


    private void checkInputValues(MultipartFile image, Long id){
        if (id == null){
            throw new IllegalArgumentException(id + "id is null");
        }
        if (image == null){
            throw new IllegalArgumentException(image + " image is null");
        }

    }

    private Image saveImage(MultipartFile image, Post post) {
        PhotoValidator.checkPhoto(image);
        final String fileName = image.getOriginalFilename();

        Image imageEntity = new Image();
        imageEntity.setPost(post);

        try {
            imageEntity.setImageUrl(image.getBytes());
        } catch (IOException e) {
            log.error("Error occurred when uploading image with name {}", fileName);
            throw InternalServerErrorException.failedToSaveImage();
        }

        return imageRepository.save(imageEntity);
    }
}
