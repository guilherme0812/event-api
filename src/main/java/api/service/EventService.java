package api.service;

import api.domain.event.Event;
import api.domain.event.EventAddressProjection;
import api.domain.event.EventRequestDTO;
import api.domain.event.EventResponseDTO;
import api.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

        Date date = new Date(data.date() * 1000);

        Event newEvent = new Event();
        newEvent.setTitle(data.title());
        newEvent.setDescription(data.description());
        newEvent.setRemote(data.remote());
        newEvent.setDate(date);
        newEvent.setImgUrl("http://localhost:3000");
        newEvent.setEventUrl("http://localhost:3000");

        IO.println("test: " + date);

        repository.save(newEvent);

        return newEvent;
    }

    public List<EventResponseDTO> getUpcomingEvents(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<EventAddressProjection> eventsPage = this.repository.findUpcomingEvents(new Date(), pageable);

        IO.println("Event page: " + pageable);

        return eventsPage.map(event -> new EventResponseDTO(
                        event.getId(),
                        event.getTitle(),
                        event.getDescription(),
                        event.getDate(),
                        event.getCity() != null ? event.getCity() : "",
                        event.getUf() != null ? event.getUf() : "",
                        event.getRemote(),
                        event.getEventUrl(),
                        event.getImgUrl())
                )
                .stream().toList();
    }

}
