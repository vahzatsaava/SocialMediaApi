package com.example.socialmediaapi.controller;

import com.example.socialmediaapi.dto.ImageDto;
import com.example.socialmediaapi.dto.general.ResponseDto;
import com.example.socialmediaapi.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ImageController {

    private final ImageService imageService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload and save a new image",
            description = "Upload an image file and associate it with a specific post."
    )
    public ResponseEntity<ResponseDto<ImageDto>> saveImage(@RequestParam MultipartFile multipartFile,
                                                           @RequestParam Long postId) {
        ImageDto imageDto = imageService.addPicture(multipartFile, postId);
        ResponseDto<ImageDto> imageDtoResponseDto = new ResponseDto<>(HttpStatus.CREATED.value(), imageDto);
        return ResponseEntity.ok(imageDtoResponseDto);
    }

    @PatchMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Update an existing image",
            description = "Update an image file associated with a specific post."
    )
    public ResponseEntity<ResponseDto<ImageDto>> updateImage(@RequestParam MultipartFile multipartFile,
                                                             @RequestParam Long imageId) {
        ImageDto imageDto = imageService.updatePicture(multipartFile, imageId);
        ResponseDto<ImageDto> imageDtoResponseDto = new ResponseDto<>(HttpStatus.OK.value(), imageDto);
        return ResponseEntity.ok(imageDtoResponseDto);

    }

}
