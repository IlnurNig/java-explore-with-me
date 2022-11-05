package ru.practicum.comment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.service.CommentService;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Validated
@Slf4j
@RequestMapping("/admin/events/comments")
public class CommentAdminController {

    private final CommentService commentService;

    @Autowired
    public CommentAdminController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/audit/{commentId}")
    public List getAuditById(@PathVariable @NotNull Long commentId,
                             @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                             @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("get audit event by id {}", commentId);
        return commentService.getAuditById(commentId, from, size);
    }

}
