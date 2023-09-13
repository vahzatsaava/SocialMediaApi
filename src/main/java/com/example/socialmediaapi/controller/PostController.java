package com.example.socialmediaapi.controller;

import com.example.socialmediaapi.dto.PostDto;
import com.example.socialmediaapi.dto.general.ResponseDto;
import com.example.socialmediaapi.dto.post.PostInputDto;
import com.example.socialmediaapi.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@RestController
@RequestMapping("/api/v1/posts")
@AllArgsConstructor
@Slf4j
@SecurityRequirement(name = "bearerAuth")
public class PostController {

    private final PostService postService;


    @PostMapping("/create-post")
    @Operation(summary = "[US 2.1] Create a New Post",
            description = "Create a new Post with the provided title, text, optional image, and user's email. " +
                    "Images should be uploaded as multipart/form-data."
    )
    public ResponseEntity<ResponseDto<PostDto>> createPost(@Valid @RequestBody PostInputDto postInputDto,
                                                           Principal principal) {
        PostDto postDto = postService.createPost(postInputDto, principal);
        ResponseDto<PostDto> responseDto = new ResponseDto<>(HttpStatus.OK.value(),
                postDto);
        return ResponseEntity.ok(responseDto);
    }


    @PutMapping("/update-post")
    @Operation(summary = "[US 2.2] Update an Existing Post",
            description = "Update an existing Post with new information provided in the request body. +" +
                    "Images should be uploaded as multipart/form-data.")

    public ResponseEntity<ResponseDto<PostDto>> updatePost( @RequestBody  PostInputDto postInputDto,
                                                           Principal principal) {
        PostDto postDto = postService.updatePost(postInputDto,principal);
        ResponseDto<PostDto> responseDto = new ResponseDto<>(HttpStatus.OK.value(),
                postDto);
        return ResponseEntity.ok(responseDto);
    }
    @GetMapping("/by-title")
    @Operation(summary = "[US 2.3] Get Post by Title",
            description = "Retrieve information about a Post using its title.")
    public ResponseEntity<ResponseDto<PostDto>> getByTittle(@RequestParam @Size(min = 3) String title) {
        PostDto postDto = postService.getPostByTitle(title);
        ResponseDto<PostDto> responseDto = new ResponseDto<>(HttpStatus.OK.value(),
                postDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/delete-post/{id}")
    @Operation(summary = "[US 2.4] Delete Post by ID",
            description = "Delete a Post using its unique ID.")
    public ResponseEntity<ResponseDto<String>> deletePost(@PathVariable Long id, Principal principal) {
        postService.delete(id,principal);
        ResponseDto<String> responseDto = new ResponseDto<>(HttpStatus.OK.value(),
                String.format("Post with ID %d was deleted", id));
        return ResponseEntity.ok(responseDto);
    }

}
