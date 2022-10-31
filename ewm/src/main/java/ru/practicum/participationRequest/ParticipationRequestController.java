package ru.practicum.participationRequest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exception.exceptionClass.ExceptionBadRequest;
import ru.practicum.exception.exceptionClass.ExceptionConflict;
import ru.practicum.exception.exceptionClass.ExceptionNotFound;
import ru.practicum.participationRequest.dto.ParticipationRequestDto;
import ru.practicum.participationRequest.dto.ParticipationRequestMapper;
import ru.practicum.participationRequest.service.ParticipationRequestService;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users/{userId}/requests")
public class ParticipationRequestController {

    private final ParticipationRequestService requestService;

    private final ParticipationRequestMapper requestMapper;

    @Autowired
    public ParticipationRequestController(ParticipationRequestService requestService, ParticipationRequestMapper requestMapper) {
        this.requestService = requestService;
        this.requestMapper = requestMapper;
    }

    @PostMapping
    public ParticipationRequestDto create(@RequestParam Long eventId,
                                          @PathVariable @NotNull Long userId)
            throws ExceptionNotFound, ExceptionConflict {
        log.info("create ParticipationRequest with eventId: {}, userId: {}", eventId, userId);
        var request = requestMapper.toEntity(eventId, userId);
        return requestMapper.toDto(requestService.create(request));
    }

    @PatchMapping("{requestId}/cancel")
    public ParticipationRequestDto cancel(@PathVariable @NotNull Long requestId,
                                          @PathVariable @NotNull Long userId)
            throws ExceptionBadRequest, ExceptionNotFound {
        log.info("cancel ParticipationRequest with requestId: {}, userId: {}", userId, requestId);
        return requestMapper.toDto(requestService.cancel(requestId, userId));
    }

    @GetMapping
    public List<ParticipationRequestDto> getAll(@PathVariable @NotNull Long userId) {
        log.info("getAll ParticipationRequest with userId: {}", userId);
        return requestMapper.toDto(requestService.getAllByRequesterId(userId));
    }
}
