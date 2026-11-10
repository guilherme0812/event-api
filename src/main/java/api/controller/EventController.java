package api.controller;

import api.domain.event.Event;
import api.domain.event.EventRequestDTO;
import api.domain.event.EventResponseDTO;
import api.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/event")
public class EventController {
    private final EventService eventService;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Event> createEvent(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("date") Long date,
            @RequestParam("city") String city,
            @RequestParam("state") String state,
            @RequestParam("remote") Boolean remote,
            @RequestParam("eventUrl") String eventUrl,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) {
        EventRequestDTO eventRequestDTO = new EventRequestDTO(title, description, date, city, state, remote, eventUrl, image);
        Event newEvent = this.eventService.createEvent(eventRequestDTO);
        return ResponseEntity.ok(newEvent);
    }

    @GetMapping
    public ResponseEntity<List<EventResponseDTO>> getEvents(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        List<EventResponseDTO> allEvents = this.eventService.getUpcomingEvents(page, size);

        return ResponseEntity.ok(allEvents);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<Object> getEventDetail(@PathVariable UUID eventId) {
        return ResponseEntity.status(200).body(eventId);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<EventResponseDTO>> getFilteredEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam String city,
            @RequestParam String uf,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate
    ) {
        List<EventResponseDTO> events = eventService.getFilteredEvents(page, size, city, uf, startDate, endDate);
        return ResponseEntity.ok(events);
    }

//    @GetMapping("/search")
//    public ResponseEntity<List<EventResponseDTO>> getSearchEvents(@RequestParam String title) {
//        List<EventResponseDTO> events = eventService.searchEvents(title);
//        return ResponseEntity.ok(events);
//    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable UUID eventId) {
        this.eventService.deleteEvent(eventId);
        return ResponseEntity.noContent().build();
    }
}
