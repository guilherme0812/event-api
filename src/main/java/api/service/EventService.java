package api.service;

import api.domain.event.Event;
import api.domain.event.EventRequestDTO;
import api.domain.event.EventResponseDTO;
import api.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventService {
    private final EventRepository repository;

    public Event createEvent(EventRequestDTO data) {
        Event newEvent = new Event();
        newEvent.setTitle(data.title());
        newEvent.setDescription(data.description());
        newEvent.setRemote(data.remote());
        newEvent.setDate(new Date(data.date()));
        newEvent.setImgUrl("http://localhost:3000");
        newEvent.setEventUrl("http://localhost:3000");

        repository.save(newEvent);

        return newEvent;
    }

    public List<EventResponseDTO> getUpcomingEvents(int page, int size) {
        List<EventResponseDTO> events = new ArrayList<>();
        return events;
    }

}
