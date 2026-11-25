package com.yuri.clinic;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/demo")
public class SlotController {

    private static final List<Slot> DAY_SLOTS = makeSlots();
    private final ConcurrentMap<String, Slot> reservations = new ConcurrentHashMap<>();

    @GetMapping("/slots")
    public List<Slot> getSlots() {
        return DAY_SLOTS.stream()
                .filter(slot -> !reservations.containsKey(slot.start()))
                .toList();
    }

    @PostMapping("/reservations")
    @ResponseStatus(HttpStatus.CREATED)
    public Slot createReservation(@RequestBody ReservationRequest request) {
        Slot slot = DAY_SLOTS.stream()
                .filter(item -> item.start().equals(request.start()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "예약 가능 시간대가 아닙니다."));

        if (reservations.putIfAbsent(slot.start(), slot) != null) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "이미 예약된 시간입니다.");
        }

        return slot;
    }

    @GetMapping("/reservations")
    public List<Slot> getReservations() {
        return reservations.values().stream()
                .sorted(Comparator.comparing(Slot::start))
                .toList();
    }

    private static List<Slot> makeSlots() {
        LocalTime close = LocalTime.of(18, 0);
        LocalTime lunchStart = LocalTime.of(12, 0);
        LocalTime lunchEnd = LocalTime.of(13, 0);
        List<Slot> slots = new ArrayList<>();

        for (LocalTime start = LocalTime.of(9, 0);
             !start.plusMinutes(30).isAfter(close);
             start = start.plusMinutes(30)) {
            LocalTime end = start.plusMinutes(30);
            if (start.isBefore(lunchEnd) && end.isAfter(lunchStart)) {
                continue;
            }
            slots.add(new Slot(start.toString(), end.toString()));
        }

        return List.copyOf(slots);
    }

    public record Slot(String start, String end) {}
    public record ReservationRequest(String start) {}
}