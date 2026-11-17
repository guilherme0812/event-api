package api.exception;

import java.util.UUID;

public class EventNotFoundException extends BusinessException{
    public EventNotFoundException(UUID id) {
        super("Event not found: " + id);
    }
}
