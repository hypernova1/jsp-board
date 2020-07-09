package sam.board.controller;

import sam.spring.annotation.component.RestController;
import sam.spring.annotation.mapping.GetMapping;
import sam.spring.annotation.mapping.RequestMapping;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @GetMapping
    public String getCommentList() {
        return "aaaaa";
    }

}
