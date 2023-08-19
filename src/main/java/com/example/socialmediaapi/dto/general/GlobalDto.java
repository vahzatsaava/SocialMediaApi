package com.example.socialmediaapi.dto.general;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public abstract class GlobalDto implements Serializable {
    protected final int code;
}
