package com.andmhn;

import java.util.Arrays;

public class Main {
    private static final String[] operandArgs = {"do", "add"};
    private static final String[] singleArgs = {"help", "ls", "sort", "archive"};

    private static final Todo todo = new Todo();

    private static void printUsage() {
        String msg = """
                Welcome to Todo.txt!
                Usage:
                    todo ls             List tasks
                    todo sort           Sort tasks and list it
                    todo archive        moves completed task to archive.txt in TODO_DIR
                    todo help           show this help text
                    todo do <Number>    Mark complete the selected task
                    todo add <args..>   Adds new task to todo.txt
                """;
        System.err.println(msg);
    }

    private static void handleUnknownArg(String arg){
        System.err.println("Unknown arg: " + arg);
        printUsage();
    }

    private static void handleSingleArgs(String arg){
        switch (arg){
            case "ls":
                todo.listLines();
                break;
            case "sort":
                todo.sortTasks();
                break;
            case "help":
                printUsage();
                break;
            default:
                handleUnknownArg(arg);
        }
    }

    private static void handleOperandArgs(String[] args){
        if (args.length == 1) {
            System.err.println("arg: \"" + args[0] + "\" requires value!");
            printUsage();
            return;
        }

        StringBuilder s = new StringBuilder();
        for (int i = 1; i < args.length - 1; i++) {
            s.append(args[i]).append(" ");
        }
        s.append(args[args.length - 1]);

        switch (args[0]){
            case "add":
                todo.addTask(s.toString());
                break;
            case "do":
                todo.markDone(Integer.parseInt(args[1]));
                break;
            default:
                handleUnknownArg(args[0]);
        }
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            printUsage();
        } else if (Arrays.stream(operandArgs).toList().contains(args[0])) {
            handleOperandArgs(args);
        } else if (Arrays.stream(singleArgs).toList().contains(args[0])) {
            handleSingleArgs(args[0]);
        } else {
            handleUnknownArg(args[0]);
        }
    }
}