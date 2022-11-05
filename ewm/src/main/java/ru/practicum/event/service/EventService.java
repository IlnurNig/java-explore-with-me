package ru.practicum.event.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.event.dto.EventMapper;
import ru.practicum.event.model.Criteria;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.repository.EventCriteriaRepository;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.exceptionClass.ExceptionBadRequest;
import ru.practicum.exception.exceptionClass.ExceptionConflict;
import ru.practicum.exception.exceptionClass.ExceptionForbidden;
import ru.practicum.exception.exceptionClass.ExceptionNotFound;
import ru.practicum.participationRequest.model.ParticipationRequest;
import ru.practicum.participationRequest.model.RequestState;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class EventService {

    private final EventRepository eventRepository;

    private final EventCriteriaRepository eventCriteriaRepository;

    private final EventMapper eventMapper;


    @Autowired
    public EventService(EventRepository eventRepository,
                        EventCriteriaRepository eventCriteriaRepository,
                        EventMapper mapper) {
        this.eventRepository = eventRepository;
        this.eventCriteriaRepository = eventCriteriaRepository;
        this.eventMapper = mapper;
    }


    public Event create(Event event) {
        log.info("create event {}", event);
        event.setState(EventState.PENDING);
        return eventRepository.save(event);
    }

    public Event getById(Long eventId) {
        log.info("get event with eventId: {}", eventId);
        return eventRepository.findById(eventId).orElseThrow(() -> new ExceptionNotFound(
                String.format("Event with eventId=%d is missing", eventId)));
    }


    public Set<Event> getByIdx(List<Long> idx) {
        log.info("get event in idx: {}", idx);
        if (idx == null) {
            return new HashSet<>();
        }
        return eventRepository.findAllByIdIn(idx);
    }

    public Event getByIdAndInitiatorId(Long userId, Long eventId) {
        log.info("getByIdAndInitiatorId with userId: {}, eventId:{}", userId, eventId);
        return eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new ExceptionNotFound(
                        String.format("Event with eventId=%d and userId=%d is missing", eventId, userId)));
    }

    public List<Event> getAllByInitiatorId(Long userId, Integer from, Integer size) {
        log.info("get event with userId: {}, from: {}, size: {}", userId, from, size);
        Pageable pageable = PageRequest.of(
                from / size,
                size);
        return eventRepository.findAllByInitiatorId(userId, pageable);
    }

    public List<Event> getAllByCriteria(Criteria criteria) {
        log.info("get all event with criteria: {}", criteria);
        return eventCriteriaRepository.findAllByCriteria(criteria);
    }

    public Event update(Event event) {
        log.info("update event {}", event);
        Event oldEvent = getById(event.getId());
        eventMapper.updateEventIgnoreNull(event, oldEvent);
        return eventRepository.save(oldEvent);
    }

    public Event cancelEvent(Long userId, Long eventId) {
        log.info("cancel event with userId: {}, eventId: {}", userId, eventId);
        Event event = getByIdAndInitiatorId(userId, eventId);
        event.setState(EventState.CANCELED);
        return eventRepository.save(event);
    }

    public Event setStateEvent(Long eventId, EventState state) {
        log.info("set event {} state {}", eventId, state);
        Event event = getById(eventId);
        event.setState(state);
        if (state == EventState.PUBLISHED) {
            event.setPublishedOn(LocalDateTime.now());
        }
        return eventRepository.save(event);
    }

    public List<ParticipationRequest> getRequestsByUserIdAndEventId(Long userId, Long eventId) {
        log.info("get request by userId: {}, eventId: {}", userId, eventId);
        Event event = getById(eventId);
        return new ArrayList<>(event.getRequests());
    }


    public ParticipationRequest confirmRequest(Long reqId, Long userId, Long eventId) {

        log.info("confirm request with reqId: {}, userId: {}, eventId: {}", reqId, userId, eventId);

        Event event = getById(eventId);

        if (!Objects.equals(event.getInitiator().getId(), userId)) {
            throw new ExceptionForbidden("only the author of the event can confirm the application");
        }

        if (isExceededLimitRequests(event)) {
            throw new ExceptionConflict("the limit of applications for the event has been exhausted");
        }

        var request = event.getRequests().stream()
                .filter(r -> Objects.equals(r.getId(), reqId))
                .findFirst()
                .orElseThrow(() -> new ExceptionBadRequest("the request is missing in the event"));

        request.setState(RequestState.CONFIRMED);
        if (isExceededLimitRequests(event)) {
            event.getRequests().removeIf(r -> r.getState() != RequestState.CONFIRMED);
        }

        eventRepository.save(event);
        return request;
    }

    public ParticipationRequest rejectRequest(Long reqId, Long userId, Long eventId) {

        log.info("reject request with reqId: {}, userId: {}, eventId: {}", reqId, userId, eventId);

        Event event = getById(eventId);

        if (!Objects.equals(event.getInitiator().getId(), userId)) {
            throw new ExceptionBadRequest("the application is confirmed only by the author of the event");
        }

        var request = event.getRequests().stream()
                .filter(r -> Objects.equals(r.getId(), reqId))
                .findFirst()
                .orElseThrow(() -> new ExceptionBadRequest("the request is missing in the event"));

        event.getRequests().remove(request);
        eventRepository.save(event);
        request.setState(RequestState.REJECTED);
        return request;
    }

    public Event getByIdAndState(Long eventId, EventState eventState) {
        log.info("getByIdAndState with eventId: {}, eventState: {}", eventId, eventState);

        return eventRepository.findByIdAndState(eventId, eventState)
                .orElseThrow(() -> new ExceptionNotFound(String.format("Event with id=%d and state=%s is not found",
                        eventId, eventState.toString())));
    }


    public boolean isExceededLimitRequests(Event event) {
        log.info("isExceededLimitRequests event {}", event);

        if (event.getParticipantLimit() == null || event.getParticipantLimit() == 0) {
            return false;
        }

        return event.getRequests().stream().filter(r -> r.getState() == RequestState.CONFIRMED).count()
                >= event.getParticipantLimit();
    }

}
