package de.hawlandshut.sgheldd.scheduleplaner;

import java.util.Optional;

/**
 * Schnittstelle f�r Eintr�ge von Events in einen Terminplaner.
 * Aufgabenrunde 2: Aufgabe2 Praktikum WS 2015-16
 *
 * @author Gudrun Schiedermeier
 */
public interface Schedule {
    /**
     * Loescht alle Eintraege, die vor dem aktuellen Termin liegen.
     *
     * @return dieser Terminplaner.
     */
    Schedule expire();

    /**
     * Traegt ein Ereignis woechentlich in den Terminplaner ein.
     *
     * @param event ein Ereignis.
     * @param times Anzahl wie oft dieses Ereignis eingetragen werden soll.
     * @return dieser Terminplaner
     */
    Schedule repeatWeekly(Event event, int times);

    /**
     * Findet ein Ereignis anhand eines Schluesselwortes .
     *
     * @param key ein Schluesselwort, das ein Ereignis beschreibt.
     * @return ein Ereignis, eventuell leer.
     */
    Optional<Event> find(String key);

    /**
     * Aufruf der Methoden des Terminplaners.
     *
     * @param args einige Ereignisse
     *             2015/11/3/12/50/Robotikvorlesung/90
     *             2015/7/31/12/00/Pruefung/60
     * @throws IllegalArgumentException
     */
    static void main(String... args) throws IllegalArgumentException{
        final Schedule schedule = new MapSchedule(args);

        System.out.println(schedule);
        //fruehere Veranstaltungen entfernen.
        schedule.expire();
        System.out.println(schedule);

        //finde eine Veranstaltung anhand eines Schlueeselwortes.
        final int repeat = 3;
        final Optional<Event> robotik = schedule.find("Robotik");

        //Woechentliche Veranstaltung eintragen.
        if (robotik.isPresent())
            schedule.repeatWeekly(robotik.get(), repeat);
        System.out.println(schedule);
    }
}
