package ru.practicum.comment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.CommentMapper;
import ru.practicum.comment.service.CommentService;

import java.util.List;

@RestController
@Validated
@Slf4j
@RequestMapping("/events/comments")
public class CommentPublicController {

    private final CommentService commentService;

    private final CommentMapper commentMapper;

    @Autowired
    public CommentPublicController(CommentService commentService, CommentMapper commentMapper) {
        this.commentService = commentService;
        this.commentMapper = commentMapper;
    }

    @GetMapping("{eventId}")
    public List<CommentDto> getAllByEventId(@PathVariable Long eventId,
                                            @RequestParam(name = "from", defaultValue = "0") Integer from,
                                            @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("getAllByEventId  with eventId: {}", eventId);
        return commentMapper.toDto(commentService.getAllByEventId(eventId, from, size));
    }

    @GetMapping("comment/{commentId}")
    public CommentDto getById(@PathVariable Long commentId) {
        log.info("getById  with commentId: {}", commentId);
        return commentMapper.toDto(commentService.getById(commentId));
    }
}
