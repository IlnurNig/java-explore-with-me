package ru.practicum.compilation.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.service.EventService;
import ru.practicum.exception.exceptionClass.ExceptionNotFound;

import java.util.List;

@Service
@Slf4j
public class CompilationService {

    private final CompilationRepository compilationRepository;

    private final EventService eventService;

    @Autowired
    public CompilationService(CompilationRepository compilationRepository, EventService eventService) {
        this.compilationRepository = compilationRepository;
        this.eventService = eventService;
    }

    public Compilation create(Compilation compilation) {
        log.info("Create compilation: {}", compilation);
        return compilationRepository.save(compilation);
    }

    public Compilation getById(Long id) throws ExceptionNotFound {
        log.info("get compilation with id: {}", id);
        return compilationRepository.findById(id)
                .orElseThrow(() -> new ExceptionNotFound(String.format("Compilation with id=%d is not found", id)));
    }

    public List<Compilation> getAll(Boolean pinned, Integer from, Integer size) {
        log.info("get all compilation with pinned: {}, from: {}, size: {}", pinned, from, size);
        Pageable pageable = PageRequest.of(
                from / size,
                size);
        if (pinned == null) {
            return compilationRepository.findAll(pageable).toList();
        }
        return compilationRepository.findAllByPinned(pinned, pageable);
    }

    public void delete(Long id) {
        log.info("delete compilation with id: {}", id);
        compilationRepository.deleteById(id);
    }

    public void addEvent(Long eventId, Long compId) throws ExceptionNotFound {
        log.info("add event id {} compilation with id: {}", eventId, compId);
        Compilation compilation = getById(compId);
        Event event = eventService.getById(eventId);
        compilation.getEvents().add(event);
        compilationRepository.save(compilation);
    }

    public void deleteEvent(Long eventId, Long compId) throws ExceptionNotFound {
        log.info("delete event id {} compilation with id: {}", eventId, compId);
        Compilation compilation = getById(compId);
        Event event = eventService.getById(eventId);
        compilation.getEvents().remove(event);
        compilationRepository.save(compilation);
    }

    public void pinCompilation(Long compId, boolean pinned) throws ExceptionNotFound {
        log.info("pin compilation with id: {} and pinned: {}", compId, pinned);
        Compilation compilation = getById(compId);
        compilation.setPinned(pinned);
        compilationRepository.save(compilation);
    }

}
