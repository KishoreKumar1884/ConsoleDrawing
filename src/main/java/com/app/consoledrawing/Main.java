package com.app.consoledrawing;

import com.app.consoledrawing.command.*;
import com.app.consoledrawing.model.Canvas;
import com.app.consoledrawing.model.CanvasImpl;
import com.app.consoledrawing.model.EntityFactory;
import com.app.consoledrawing.model.InvalidEntityException;

import java.util.Scanner;

public class Main {
    private static Canvas         canvas;
    private static Scanner        scanner;
    private static CommandFactory commandFactory;
    private static EntityFactory  entityFactory;

    public static void main(String[] args) throws NumberFormatException, InterruptedException {
        scanner = new Scanner(System.in);
        commandFactory = new CommandFactory();
        entityFactory = new EntityFactory();

        System.out.println("Enter command:");

        while (true) {
            process(scanner.nextLine());
            System.out.println("Enter command:");
        }
    }

    private static void process(String commandLine) {
        Command command = null;

        try {
            command = commandFactory.getCommand(commandLine);
        } catch (InvalidCommandException e) {
            System.out.println("Please enter a valid command.");
        } catch (InvalidCommandParams invalidCommandParams) {
            System.out.println("Command syntax is not correct: " + invalidCommandParams.getMessage());
            System.out.println("Refer to following correct syntax: \n" + invalidCommandParams.getHelpMessage());
        }

        if (command instanceof QuitCommand) {
            quit();
        }

        if (command instanceof CreateCommand) {
            createNewCanvas((CreateCommand) command);
            return;
        }

        if (command instanceof DrawEntityCommand) {
            draw((DrawEntityCommand) command);
        }
    }

    private static void draw(DrawEntityCommand command) {
        if (canvas == null) {
            System.out.println("You need to create a canvas first");
            return;
        }
        try {
            canvas.addEntity(entityFactory.getEntity(command));
            System.out.println(canvas.render());
        } catch (InvalidEntityException e) {
            System.out.println("Can not add the model to canvas: " + e.getMessage());
        }
    }

    private static void createNewCanvas(CreateCommand command) {
        canvas = new CanvasImpl(command.getWidth(), command.getHeight());
        System.out.println(canvas.render());
    }

    private static void quit() {
        scanner.close();
        System.out.println("Exiting...");
        System.exit(0);
    }
}
