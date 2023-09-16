package duke;

import duke.exceptions.DukeException;
import duke.exceptions.NullDescriptionInputException;
import duke.exceptions.NullInputException;
import duke.exceptions.UndefinedTaskException;
import duke.tasks.Deadline;
import duke.tasks.Event;
import duke.tasks.Task;
import duke.tasks.Todo;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Duke {
    public static final String NULL_INPUT_EXCEPTION = "At least say something! :D";

    public static final String UNDEFINED_TASKS = "____________________________________________________________" + System.lineSeparator()
        + "     ☹ OOPS!!! I'm sorry, but I don't know what that means :-(" + System.lineSeparator()
        + "     You should tell me which kind of Tasks (todo, deadline, event) you would like to add" + System.lineSeparator()
        + "____________________________________________________________" + System.lineSeparator();

    public static final String NULL_DESCRIPTION_EXCEPTION_FOR_TODO = "____________________________________________________________" + System.lineSeparator()
        + "     ☹ OOPS!!! The description of a todo cannot be empty." + System.lineSeparator()
        + "____________________________________________________________" + System.lineSeparator();

    public static final String NULL_DESCRIPTION_EXCEPTION_FOR_DEADLINE = "____________________________________________________________" + System.lineSeparator()
        + "     ☹ OOPS!!! The description of a deadline cannot be empty." + System.lineSeparator()
        + "____________________________________________________________" + System.lineSeparator();

    public static final String NULL_DESCRIPTION_EXCEPTION_FOR_EVENT = "____________________________________________________________" + System.lineSeparator()
        + "     ☹ OOPS!!! The description of an event cannot be empty." + System.lineSeparator()
        + "____________________________________________________________" + System.lineSeparator();

    public static void main(String[] args) {
        List<Task> taskList = new LinkedList<>();
        greetToUsers();

        Scanner in = new Scanner(System.in);
        String line = in.nextLine();

        try {
            if (line.length() == 0) {
                throw new NullInputException(NULL_INPUT_EXCEPTION);
            }
        } catch (DukeException e) {
            System.out.println(e.getMessage());
            line = in.nextLine();
        }

        while (!line.equals("bye")) {

            if (line.equals("list")) {

                scanTheList(taskList);

            } else if (line.startsWith("mark") || line.startsWith("unmark")) {

                markTheTask(line, taskList);

            } else {

                try {

                    handleTheUserInput(taskList, line);
                    feedbackOfTheExecution(taskList);


                } catch (DukeException e) {

                    System.out.println(e.getMessage());

                }

            }

            line = in.nextLine();

        }

        byeToUsers();
    }

    private static void handleTheUserInput(List<Task> taskList, String line) throws DukeException {

        String[] words = line.split(" ");
        String firstWord = words[0];
        boolean nullDescription = words.length == 1;

        switch (firstWord) {

        case "deadline":
            if (nullDescription) {
                throw new NullDescriptionInputException(NULL_DESCRIPTION_EXCEPTION_FOR_DEADLINE);
            }
            handleDeadline(taskList, line);
            break;
        case "todo":
            if (nullDescription) {
                throw new NullDescriptionInputException(NULL_DESCRIPTION_EXCEPTION_FOR_TODO);
            }
            handleTodo(taskList, words);
            break;
        case "event":
            if (nullDescription) {
                throw new NullDescriptionInputException(NULL_DESCRIPTION_EXCEPTION_FOR_EVENT);
            }
            handleEvent(taskList, line);
            break;
        default: {
            throw new UndefinedTaskException(UNDEFINED_TASKS);
        }
        }
    }

    private static void feedbackOfTheExecution(List<Task> taskList) {
        System.out.println("____________________________________________________________");
        System.out.println("     Got it. I've added this task:");
        System.out.println("       " + taskList.get(taskList.size()-1).toString());
        System.out.println("     Now you have " + taskList.size() + " tasks in the list.");
        System.out.println("____________________________________________________________");
    }

    private static void handleDefault() throws DukeException {
        String errorMessage = "____________________________________________________________" + System.lineSeparator()
            + "     ☹ OOPS!!! I'm sorry, but I don't know what that means :-(" + System.lineSeparator()
            + "     You should tell me which kind of Tasks (todo, deadline, event) you would like to add" + System.lineSeparator()
            + "____________________________________________________________" + System.lineSeparator();

        throw new DukeException(errorMessage);
    }

    private static void handleEvent(List<Task> taskList, String line) {
        String[] userfulInfo = Event.handleInputForEvent(line);
        taskList.add(new Event(userfulInfo[0], userfulInfo[1]));
    }

    private static void handleTodo(List<Task> taskList, String[] words) {
        String task = "";
        for (int i = 1; i < words.length; i++) {
            task += words[i] + " ";
        }
        taskList.add(new Todo(task.trim()));
    }

    private static void handleDeadline(List<Task> taskList, String line) {

        String by = line.split("/")[1];
        String description = line.split("/")[0].replace("deadline", "").trim();
        taskList.add(new Deadline(description, by.replace("by", "").trim()));
    }

    public static void greetToUsers() {
        System.out.println("____________________________________________________________");
        System.out.println(" Hello! I'm TUM");
        System.out.println(" What can I do for you?");
        System.out.println("____________________________________________________________");
    }
    public static void byeToUsers() {
        System.out.println("____________________________________________________________");
        System.out.println(" Bye. Hope to see you again soon!");
        System.out.println("____________________________________________________________");
    }

    public static void scanTheList(List taskList) {
        System.out.println("____________________________________________________________");
        System.out.println("     Here are the tasks in your list:");
        for(int i = 0; i < taskList.size(); i++) {
            System.out.println("     " + (i+1)  + "."+ taskList.get(i).toString());
        }
        System.out.println("____________________________________________________________");
    }

    public static void markTheTask(String line, List<Task> taskList) {
        System.out.println("____________________________________________________________");
        String[] words = line.split(" ");
        int index = Integer.parseInt(words[1]) - 1;
        if (line.startsWith("mark")) {
            taskList.get(index).setDone(true);
            System.out.println("     Nice! I've marked this task as done:");
        } else {
            taskList.get(index).setDone(false);
            System.out.println("     OK, I've marked this task as not done yet:");
        }
        System.out.println("       " + taskList.get(index).toString());
        System.out.println("____________________________________________________________");

    }
}
