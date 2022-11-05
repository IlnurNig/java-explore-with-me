package ru.practicum.comment.service;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.query.AuditEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.repository.CommentRepository;
import ru.practicum.exception.exceptionClass.ExceptionForbidden;
import ru.practicum.exception.exceptionClass.ExceptionNotFound;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@Validated
public class CommentService {

    private final CommentRepository commentRepository;

    private final AuditReader auditReader;

    @Autowired
    public CommentService(CommentRepository commentRepository, AuditReader auditReader) {
        this.commentRepository = commentRepository;
        this.auditReader = auditReader;
    }

    public Comment create(@Valid Comment comment) {
        return commentRepository.save(comment);
    }

    public Comment getById(@Positive Long id) {
        return commentRepository.findById(id).orElseThrow(
                () -> new ExceptionNotFound(String.format("comment with Id=%d is missing", id)));
    }

    public Comment update(@Positive Long commentId,
                          @Positive Long userId,
                          @NotBlank String description) {
        Comment comment = getById(commentId);
        if (comment.getCommentator().getId() != userId) {
            throw new ExceptionForbidden("access to the changes is available only to the authors of the comment");
        }
        comment.setDescription(description);
        comment.setUpdateOn(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    public void delete(@Positive Long commentId,
                       @Positive Long userId) {
        log.info("delete comment with id: {}", commentId);
        Comment comment = getById(commentId);
        if (comment.getCommentator().getId() != userId) {
            throw new ExceptionForbidden("access to deletion is available only to the authors of the comment");
        }
        commentRepository.deleteById(commentId);
    }

    public List<Comment> getAllByEventId(@Positive Long eventId,
                                         @PositiveOrZero Integer from,
                                         @Positive Integer size) {
        Pageable pageable = PageRequest.of(
                from / size,
                size);
        return commentRepository.findAllByEventId(eventId, pageable);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List getAuditById(@Positive Long id,
                             @PositiveOrZero Integer from,
                             @Positive Integer size) {
        return auditReader
                .createQuery()
                .forRevisionsOfEntity(Comment.class, false, true)
                .add(AuditEntity.id().eq(id))
                .setFirstResult(from)
                .setMaxResults(size)
                .getResultList();
    }

}
