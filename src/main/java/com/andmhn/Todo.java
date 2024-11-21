package com.andmhn;

import static java.nio.file.StandardOpenOption.*;

import java.nio.file.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Todo {
    private final Path todoFilePath;
    private final List<String> tasks = new ArrayList<>();

    Todo() {
        String todoDir = System.getenv("TODO_DIR");
        if (todoDir != null) {
            todoFilePath = Paths.get(todoDir).resolve("todo.txt");
        } else {
            throw new RuntimeException("Couldn't Get Environment Variable: 'TODO_DIR'");
        }
        readFileIntoMemory();
    }

    Todo(String todoPathStr) {
        this.todoFilePath = Paths.get(todoPathStr);
        readFileIntoMemory();
    }

    public void listLines() {
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(i + 1 + " " + tasks.get(i));
        }
    }

    public void addTask(String newTask) {
        if (newTask.contains("\n")) {
            throw new RuntimeException("Task contains new line character '\\n' aborting...");
        }
        tasks.add(0, newTask);
        System.out.print("New Task: " + newTask + "\n");
        writeTasksToFile();
    }

    public void markDone(int taskNo) {
        int taskArrayPos = taskNo - 1;
        StringBuilder editor = new StringBuilder(tasks.get(taskArrayPos));
        editor.insert(0, "x ");
        tasks.set(taskArrayPos, editor.toString());
        writeTasksToFile();
        System.out.println(editor);
    }

    public void sortTasks(){
        tasks.sort(String::compareTo);
        listLines();
        writeTasksToFile();
    }

    private void readFileIntoMemory() {
        try (BufferedReader reader = Files.newBufferedReader(todoFilePath)) {
            String currLine;
            while ((currLine = reader.readLine()) != null) {
                if (currLine.trim().isEmpty()) {
                    continue;
                }
                tasks.add(currLine);
            }
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
    }

    private void writeTasksToFile(){
        // accumulate
        String reducedTasks = tasks.stream().reduce((a, b) -> a + "\n" + b).get();

        try (OutputStream outputStream = new BufferedOutputStream(
                Files.newOutputStream(todoFilePath, CREATE, WRITE))) {
            outputStream.write(reducedTasks.getBytes(), 0, reducedTasks.length());
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
    }

}
