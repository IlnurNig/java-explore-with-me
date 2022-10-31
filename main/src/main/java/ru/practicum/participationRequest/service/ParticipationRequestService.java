package ru.practicum.participationRequest.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.event.model.EventState;
import ru.practicum.event.service.EventService;
import ru.practicum.exception.exceptionClass.ExceptionBadRequest;
import ru.practicum.exception.exceptionClass.ExceptionConflict;
import ru.practicum.exception.exceptionClass.ExceptionNotFound;
import ru.practicum.participationRequest.model.ParticipationRequest;
import ru.practicum.participationRequest.model.RequestState;
import ru.practicum.participationRequest.repository.ParticipationRequestRepository;

import java.util.List;

@Service
@Slf4j
public class ParticipationRequestService {

    private final ParticipationRequestRepository requestRepository;

    private final EventService eventService;

    @Autowired
    public ParticipationRequestService(ParticipationRequestRepository requestRepository, EventService eventService) {
        this.requestRepository = requestRepository;
        this.eventService = eventService;
    }

    public ParticipationRequest create(ParticipationRequest request) throws ExceptionConflict {
        log.info("create ParticipationRequest: {}", request);

        if (request.getRequester().getId() == request.getEvent().getInitiator().getId()) {
            log.debug("the initiator of the event cannot add a request to participate in his event");
            throw new ExceptionConflict("the initiator of the event cannot add a request to participate in his event");
        }

        if (requestRepository.findByRequesterIdAndEventId(request.getRequester().getId(), request.getEvent().getId())
                .isPresent()) {
            log.debug("Participation request already exist");
            throw new ExceptionConflict("Participation request already exist");
        }

        if (eventService.isExceededLimitRequests(request.getEvent())) {
            log.debug("The application limit has been exceeded");
            throw new ExceptionConflict("The application limit has been exceeded");
        }

        if (request.getEvent().getState() != EventState.PUBLISHED) {
            log.debug("you cannot participate in an unpublished event");
            throw new ExceptionConflict("you cannot participate in an unpublished event");
        }

        if (!request.getEvent().getRequestModeration()) {
            request.setState(RequestState.CONFIRMED);
        } else {
            request.setState(RequestState.PENDING);
        }

        return requestRepository.save(request);
    }

    public ParticipationRequest getById(Long id) throws ExceptionNotFound {
        log.info("getById ParticipationRequest with id: {}", id);
        return requestRepository.findById(id).orElseThrow(() ->
                new ExceptionNotFound(String.format("Participation Request with id=%d exist", id)));
    }

    public List<ParticipationRequest> getAllByRequesterId(Long userId) {
        log.info("get all by RequesterId: {}", userId);
        return requestRepository.findAllByRequesterId(userId);
    }

    public ParticipationRequest cancel(Long requestId, Long userId) throws ExceptionNotFound, ExceptionBadRequest {
        log.info("cancel ParticipationRequest with requestId: {}, userId: {}", userId, requestId);

        ParticipationRequest request = getById(requestId);

        if (request.getRequester().getId() != userId) {
            log.debug("The API is available only to the authors of the request");
            throw new ExceptionBadRequest("The API is available only to the authors of the request");
        }

        requestRepository.delete(request);
        request.setState(RequestState.CANCELED);
        return request;
    }


}
