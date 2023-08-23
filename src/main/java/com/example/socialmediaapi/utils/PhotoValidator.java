package com.example.socialmediaapi.utils;

import com.example.socialmediaapi.exceptions.BadRequestException;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.FilenameUtils;

import java.util.Objects;

public class PhotoValidator {
    private static final long MAX_FILE_SIZE = 20_971_520; // 20MB

    public static void checkPhoto(MultipartFile file) {
        String fileName = file.getName();

        if (file.isEmpty()) {
            throw BadRequestException.fileIsEmpty(fileName);
        }

        String fileExtension = Objects.requireNonNull(FilenameUtils
                        .getExtension(file.getOriginalFilename()))
                .toLowerCase();

        boolean isMatches = false;
        for (FileExtension extension : FileExtension.values()) {
            if (extension.value.equals(fileExtension)) {
                isMatches = true;
                break;
            }
        }

        if (!isMatches) {
            throw BadRequestException.fileIsNotImage(fileName);
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw BadRequestException.fileIsTooLarge(fileName);
        }
    }

    enum FileExtension {
        JPG("jpg"),
        JPEG("jpeg"),
        PNG("png");

        final String value;

        FileExtension(String value) {
            this.value = value;
        }
    }
}
