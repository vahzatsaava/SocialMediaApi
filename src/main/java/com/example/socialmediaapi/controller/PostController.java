package com.example.socialmediaapi.controller;

import com.example.socialmediaapi.dto.PostDto;
import com.example.socialmediaapi.dto.general.ResponseDto;
import com.example.socialmediaapi.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/posts")
@AllArgsConstructor
@Slf4j
@SecurityRequirement(name = "bearerAuth")
public class PostController {

    private final PostService postService;

    @GetMapping("/by-title")
    @Operation(summary = "[US 2.1] Get post information by title",
            description = "This API is used to get Post info.")
    public ResponseEntity<ResponseDto<PostDto>> getByTittle(@RequestParam String tittle) {
        PostDto postDto = postService.getPostByTitle(tittle);
        ResponseDto<PostDto> responseDto = new ResponseDto<>(HttpStatus.OK.value(),
                postDto);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/create")
    @Operation(summary = "[US 2.2] Create post ",
            description = "This API is used to create Post.")
    public ResponseEntity<ResponseDto<PostDto>> createPost(@RequestBody PostDto dto, @RequestParam String email) {
        PostDto postDto = postService.createPost(dto, email);
        ResponseDto<PostDto> responseDto = new ResponseDto<>(HttpStatus.OK.value(),
                postDto);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/update")
    @Operation(summary = "[US 2.3] Update post ",
            description = "This API is used to update Post.")
    public ResponseEntity<ResponseDto<PostDto>> updatePost(@RequestBody PostDto dto, @RequestParam Long id) {
        PostDto postDto = postService.updatePost(dto, id);
        ResponseDto<PostDto> responseDto = new ResponseDto<>(HttpStatus.OK.value(),
                postDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/delete-post/{id}")
    @Operation(summary = "[US 2.4] Delete post by id",
            description = "This API is used to delete Post.")
    public ResponseEntity<ResponseDto<String>> updatePost(@PathVariable Long id) {
        postService.delete(id);
        ResponseDto<String> responseDto = new ResponseDto<>(HttpStatus.OK.value(),
                String.format("Post with ID %d was deleted", id));
        return ResponseEntity.ok(responseDto);
    }

}
