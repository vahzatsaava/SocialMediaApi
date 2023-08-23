package com.example.socialmediaapi.service;

import com.example.socialmediaapi.model.Image;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    Image saveImage(MultipartFile image);
}
