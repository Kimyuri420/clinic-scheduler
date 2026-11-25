package com.yuri.clinic;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SlotController {

    @GetMapping("/api/demo/slots")
    public List<Slot> getSlots() {
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

        return slots;
    }

    public record Slot(String start, String end) {}
}