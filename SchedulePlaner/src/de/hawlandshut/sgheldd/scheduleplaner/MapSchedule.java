package de.hawlandshut.sgheldd.scheduleplaner;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;

/**
 * Created by s-gheldd on 10/27/15.
 */
public class MapSchedule implements Schedule {

    /**
     * private field, none of your business.
     */
    private static final int SPLITNUMBER = 7;

    /**
     * Dito.
     */
    private static final int NAMEINT = 5;

    /**
     * Dito.
     */
    private static final int DURATIONINT = 6;

    /**
     * Dito.
     */
    private static final int HOURINT = 3;

    /**
     * Dito.
     */
    private static final int MINUTEINT = 4;

    /**
     * Dito.
     */
    private final Map<Event, Set<LocalDateTime>> eventMap = new HashMap<>();


    /**
     * Constructor.
     * @param eventStrings  Expects string of form yyyy/mm/dd/hh/minmin/name/min.
     * @throws IllegalArgumentException Throws illegal argument exception if string is in the wrong format.
     */
    MapSchedule(final String... eventStrings) throws IllegalArgumentException {
        for (final String eventString : eventStrings) {

            final String[] splitString = eventString.split("/");
            if (splitString.length != SPLITNUMBER)
                throw new IllegalArgumentException("wrong format fot input string, needs to be: #/#/#/#/#/#/#\ngot: "
                        + eventString);

            final LocalDateTime eventTime = parseLocalDateTime(splitString);

            final Event event = parseEvent(splitString);

            addEvent(event, eventTime);
        }
    }

    /**
     * Adds an event to the schedule. Avoids duplicates.
     * @param event an @see Event
     * @param eventTime event time in form of @see LocalDateTime
     */
    private void addEvent(Event event, LocalDateTime eventTime) {
        if (!eventMap.containsKey(event)) {
            final Set<LocalDateTime> dateList = new HashSet<>();
            dateList.add(eventTime);
            eventMap.put(event, dateList);
        } else {
            eventMap.get(event).add(eventTime);
        }
    }

    /**
     * Parses an event from the input string.
     * @param splitString to be parsed string
     * @return Event
     */
    private Event parseEvent(String... splitString) {

        final String eventName = splitString[NAMEINT];
        final int duration = Integer.parseInt(splitString[DURATIONINT]);
        return new Event(eventName, duration);
    }

    /**
     * Parses a LocalDateTime from the input string.
     * @param splitString to be parsed string
     * @return LocalDateTime
     */
    private LocalDateTime parseLocalDateTime(String... splitString) {
        final int year = Integer.parseInt(splitString[0]);
        final int month = Integer.parseInt(splitString[1]);
        final int day = Integer.parseInt(splitString[2]);
        final int hour = Integer.parseInt(splitString[HOURINT]);
        final int minute = Integer.parseInt(splitString[MINUTEINT]);
        return LocalDateTime.of(year, month, day, hour, minute);
    }


    @Override
    public Schedule expire() {
        final LocalDateTime now = LocalDateTime.now();


        final Iterator<Map.Entry<Event, Set<LocalDateTime>>> entryIterator = this.eventMap.entrySet().iterator();
        while (entryIterator.hasNext()) {

            final Map.Entry<Event, Set<LocalDateTime>> entry = entryIterator.next();
            final Iterator<LocalDateTime> timeIterator = entry.getValue().iterator();

            while (timeIterator.hasNext()) {

                final LocalDateTime dateTime = timeIterator.next();
                if (dateTime.isBefore(now)) {
                    timeIterator.remove();
                }
            }

            if (entry.getValue().size() == 0) {
                entryIterator.remove();
            }
        }
        return this;
    }

    @Override
    public Schedule repeatWeekly(Event event, int times) {
        LocalDateTime lasttime = this.getLastTime(event);

        for (int iterationInt = 1; iterationInt <= times; iterationInt++) {
            lasttime = lasttime.plusWeeks(1);
            addEvent(event, lasttime);
        }
        return this;
    }

    /**
     * Finds the last time an Event is scheduled.
     * @param event to be sought Event
     * @return last time
     */
    private LocalDateTime getLastTime(Event event) {
        if (this.eventMap.containsKey(event)) {
            final Iterator<LocalDateTime> timeIterator = eventMap.get(event).iterator();
            LocalDateTime lastTime = timeIterator.next();
            while (timeIterator.hasNext()) {
                final LocalDateTime next = timeIterator.next();
                if (next.isAfter(lastTime))
                    lastTime = next;
            }
            return lastTime;
        } else {
            return LocalDateTime.now();
        }

    }

    @Override
    public Optional<Event> find(String key) {
        final Iterator<Event> eventIterator = this.eventMap.keySet().iterator();
        while (eventIterator.hasNext()) {
            final Event event = eventIterator.next();
            if (event.getTitle().trim().toLowerCase().contains(key.toLowerCase().trim())) {
                return Optional.of(event);
            }
        }
        return Optional.empty();
    }

    @Override
    public String toString() {
        return "MapSchedule{"
                +"eventMap=" + eventMap
                +'}';
    }
}
