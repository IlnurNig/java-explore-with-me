package ru.practicum.comment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.CommentMapper;
import ru.practicum.comment.dto.NewCommentDto;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.service.CommentService;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@RestController
@Validated
@Slf4j
@RequestMapping("/users/{userId}/events/comments")
public class CommentPrivateController {

    private final CommentService commentService;

    private final CommentMapper commentMapper;

    @Autowired
    public CommentPrivateController(CommentService commentService, CommentMapper commentMapper) {
        this.commentService = commentService;
        this.commentMapper = commentMapper;
    }

    @PostMapping
    public CommentDto create(@PathVariable @NotNull Long userId,
                             @RequestBody NewCommentDto commentDto) {
        log.info("create comment with userId: {}, commentDto: {}", userId, commentDto);
        Comment comment = commentMapper.toEntity(commentDto, userId);
        return commentMapper.toDto(commentService.create(comment));
    }

    @PatchMapping("{commentId}/{description}")
    public CommentDto update(@PathVariable @NotNull Long commentId,
                             @PathVariable @NotNull Long userId,
                             @PathVariable @NotBlank String description) {
        log.info("update comment with commentId: {}, userId: {}, description: {}", commentId, userId, description);
        return commentMapper.toDto(commentService.update(commentId, userId, description));
    }

    @DeleteMapping("{commentId}")
    public void delete(@PathVariable @NotNull Long commentId,
                       @PathVariable @NotNull Long userId) {
        log.info("delete comment with commentId: {}, userId: {}", commentId, userId);
        commentService.delete(commentId, userId);
    }

}
