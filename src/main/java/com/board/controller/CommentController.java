package com.board.controller;

import com.spring.annotation.component.RestController;
import com.spring.annotation.mapping.GetMapping;
import com.spring.annotation.mapping.RequestMapping;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @GetMapping
    public String getCommentList() {
        return null;
    }

}
