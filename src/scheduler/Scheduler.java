package scheduler;
import java.util.*;

/*
 * Objective: Create a weekly scheduling application.
 * 
 * You may create any additional enums, classes, methods or variables needed
 * to accomplish the requirements below:
 * 
 * - You should use an array filled with enums for the days of the week and each
 *   enum should contain a LinkedList of events that includes a time and what is 
 *   happening at the event.
 * 
 * - The user should be able to to interact with your application through the
 *   console and have the option to add events, view events or remove events by
 *   day.
 *   
 * - Each day's events should be sorted by chronological order.
 *  
 * - If the user tries to add an event on the same day and time as another event
 *   throw a SchedulingConflictException(created by you) that tells the user
 *   they tried to double book a time slot.
 *   
 * - Make sure any enums or classes you create have properly encapsulated member
 *   variables.
 */

class SchedulingConflictException extends RuntimeException {
    public SchedulingConflictException(String message) {
        super(message);
    }
}

enum DayOfWeek {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
}

class Event {
    private String eventName;
    private int hour;
    private int minute;

    public Event(String eventName, int hour, int minute) {
        this.eventName = eventName;
        this.hour = hour;
        this.minute = minute;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    @Override
    public String toString() {
        return String.format("%02d:%02d - %s", hour, minute, eventName);
    }
}

class DaySchedule {
    private List<Event> events;

    public DaySchedule() {
        events = new ArrayList<>();
    }

    public void addEvent(Event event) {
        for (Event existingEvent : events) {
            if (existingEvent.getHour() == event.getHour() && existingEvent.getMinute() == event.getMinute()) {
                throw new SchedulingConflictException("There is already another event at this time.");
            }
        }
        events.add(event);
        events.sort(Comparator.comparingInt(event1 -> event1.getHour() * 60 + event1.getMinute()));
    }

    public List<Event> getEvents() {
        return events;
    }
}

public class Scheduler {
    private DaySchedule[] weeklySchedule;

    public Scheduler() {
        weeklySchedule = new DaySchedule[DayOfWeek.values().length];
        for (int i = 0; i < weeklySchedule.length; i++) {
            weeklySchedule[i] = new DaySchedule();
        }
    }

    public void addEvent(DayOfWeek day, Event event) {
        weeklySchedule[day.ordinal()].addEvent(event);
    }

    public List<Event> viewEvents(DayOfWeek day) {
        return weeklySchedule[day.ordinal()].getEvents();
    }

    public void removeEvent(DayOfWeek day, int index) {
        List<Event> events = weeklySchedule[day.ordinal()].getEvents();
        if (index >= 0 && index < events.size()) {
            events.remove(index);
        } else {
            System.out.println("That index doesn't exist.");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Scheduler scheduler = new Scheduler();

        while (true) {
            System.out.println("1. Add Event\n2. View Events\n3. Remove Event\n4. Exit");
            int choice = scanner.nextInt();

            if (choice == 4) {
                break;
            }

            System.out.println("Enter day (MONDAY, TUESDAY, ..., SUNDAY): ");
            DayOfWeek day = DayOfWeek.valueOf(scanner.next());

            if (choice == 1) {
                System.out.println("Enter event name: ");
                String eventName = scanner.next();
                System.out.println("Enter hour and minute (separated by space): ");
                int hour = scanner.nextInt();
                int minute = scanner.nextInt();
                Event event = new Event(eventName, hour, minute);
                scheduler.addEvent(day, event);
                System.out.println("Event added.");
            } else if (choice == 2) {
                List<Event> events = scheduler.viewEvents(day);
                if (events.isEmpty()) {
                    System.out.println("No events for " + day);
                } else {
                    System.out.println("Events for " + day + ":");
                    for (int i = 0; i < events.size(); i++) {
                        System.out.println(i + ". " + events.get(i));
                    }
                }
            } else if (choice == 3) {
                System.out.println("Enter index of event to remove: ");
                int index = scanner.nextInt();
                scheduler.removeEvent(day, index);
                System.out.println("Event removed.");
            }
        }
    }
}


