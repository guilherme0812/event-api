package api.controller;

import api.domain.event.Event;
import api.domain.event.EventRequestDTO;
import api.domain.event.EventResponseDTO;
import api.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "events", description = "Events operations")
public class EventController {
    private final EventService eventService;

    @Operation(summary = "Create a new event")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Event created successfully",
                    content = @Content(schema = @Schema(implementation = Event.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content)
    })
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

    @Operation(summary = "List upcoming events", description = "Return upcoming events in a pagination list")
    @ApiResponse(responseCode = "200", description = "List of events", content = @Content(array = @ArraySchema(schema = @Schema(implementation = EventResponseDTO.class))))
    @GetMapping
    public ResponseEntity<List<EventResponseDTO>> getEvents(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        List<EventResponseDTO> allEvents = this.eventService.getUpcomingEvents(page, size);

        return ResponseEntity.ok(allEvents);
    }

    @Operation(summary = "Get specic event by id")
    @GetMapping("/{eventId}")
    public ResponseEntity<Event> getEventDetail(@PathVariable UUID eventId) {
        Event event = this.eventService.findEventById(eventId);
        return ResponseEntity.status(200).body(event);
    }

    @Operation(summary = "Get an event by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Event founded",
                    content = @Content(schema = @Schema(implementation = Event.class))),
            @ApiResponse(responseCode = "404", description = "Event not found", content = @Content)
    })
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

    @Operation(summary = "Delete an evrnt by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Event deleted", content = @Content),
            @ApiResponse(responseCode = "404", description = "Event not found", content = @Content)
    })
    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable UUID eventId) {
        this.eventService.deleteEvent(eventId);
        return ResponseEntity.noContent().build();
    }
}
