package de.hawlandshut.sgheldd.scheduleplaner;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by s-gheldd on 10/27/15.
 */
public class MapSchedule implements Schedule {

    private final HashMap<Event, Set<LocalDateTime>> eventMap = new HashMap<>();

    MapSchedule(String... eventStrings) throws Exception {
        for (String eventString : eventStrings) {

            final String[] splitString = eventString.split("/");
            if (splitString.length != 7)
                throw new IllegalArgumentException("wrong format fot input string, needs to be: #/#/#/#/#/#/#\ngot: "
                        + eventString);

            final LocalDateTime eventTime = parseLocalDateTime(splitString);

            final Event event = parseEvent(splitString);

            addEvent(event, eventTime);
        }
    }

    private void addEvent(Event event, LocalDateTime eventTime) {
        if (!eventMap.containsKey(event)) {
            final Set<LocalDateTime> dateList = new HashSet<>();
            dateList.add(eventTime);
            eventMap.put(event, dateList);
        } else {
            eventMap.get(event).add(eventTime);
        }
    }

    private Event parseEvent(String[] splitString) {
        final String eventName = splitString[5];
        final int duration = Integer.parseInt(splitString[6]);
        return new Event(eventName, duration);
    }

    private LocalDateTime parseLocalDateTime(String[] splitString) {
        final int year = Integer.parseInt(splitString[0]);
        final int month = Integer.parseInt(splitString[1]);
        final int day = Integer.parseInt(splitString[2]);
        final int hour = Integer.parseInt(splitString[3]);
        final int minute = Integer.parseInt(splitString[4]);
        return LocalDateTime.of(year, month, day, hour, minute);
    }


    @Override
    public Schedule expire() {
        LocalDateTime now = LocalDateTime.now();


        Iterator<Map.Entry<Event, Set<LocalDateTime>>> entryIterator =  this.eventMap.entrySet().iterator();
        while (entryIterator.hasNext()){

            Map.Entry<Event,Set<LocalDateTime>> entry = entryIterator.next();
            Iterator<LocalDateTime> timeIterator = entry.getValue().iterator();

            while (timeIterator.hasNext()){

                LocalDateTime dateTime = timeIterator.next();
                if (dateTime.isBefore(now)){
                    timeIterator.remove();
                }
            }

            if (entry.getValue().size()==0){
                entryIterator.remove();
            }
        }
        return this;
    }

    @Override
    public Schedule repeatWeekly(Event event, int times) {
        LocalDateTime lasttime = this.getLastTime(event);

        for (int i = 1; i<=times; i++){
            lasttime= lasttime.plusWeeks(1);
            addEvent(event, lasttime);
        }
        return this;
    }

    private LocalDateTime getLastTime(Event event) {
        if(this.eventMap.containsKey(event)){
            Iterator<LocalDateTime> timeIterator = eventMap.get(event).iterator();
            LocalDateTime lastTime = timeIterator.next();
            while (timeIterator.hasNext()){
                LocalDateTime next = timeIterator.next();
                if (next.isAfter(lastTime))
                    lastTime=next;
            }
            return lastTime;
        } else {
            return LocalDateTime.now();
        }

    }

    @Override
    public Optional<Event> find(String key) {
        Iterator<Event> eventIterator = this.eventMap.keySet().iterator();
        while (eventIterator.hasNext()){
            Event event = eventIterator.next();
            if (event.getTitle().trim().toLowerCase().contains(key.toLowerCase().trim())){
                return Optional.of(event);
            }
        }
        return Optional.empty();
    }

    @Override
    public String toString() {
        return "MapSchedule{" +
                "eventMap=" + eventMap +
                '}';
    }
}
