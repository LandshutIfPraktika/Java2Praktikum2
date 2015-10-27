package de.hawlandshut.sgheldd.scheduleplaner;

/**
 * Ein Ereignis zum Eintrag in einen Terminplaner.
 * Aufgabenrunde 2: Aufgabe2 Praktikum WS 2015-16
 *
 * @author tschucky
 */
public class Event {
    /**
     * Titel des Ereignisses.
     */
    private final String title;
    /**
     * Dauer des Ereignisses.
     */
    private final int duration;

    /**
     * Konstruktor zuer Erzeugung eines Events.
     *
     * @param title    textuelle Beschreibung eines Ereignisses.
     * @param duration Dauer des Ereignisses in Minuten.
     */
    public Event(String title, int duration) {
        if (title == null)
            throw new NullPointerException();
        if (title.isEmpty())
            throw new IllegalArgumentException();
        if (duration < 1)
            throw new IllegalArgumentException();
        this.title = title;
        this.duration = duration;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + duration;
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Event other = (Event) obj;
        if (duration != other.duration)
            return false;
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
            return false;
        return true;
    }

    public String getTitle() {
        return title;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return "Event [title=" + title + ", duration=" + duration + "]";
    }

}
