package com.company;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;

public class Options {



    // Las opciones principales del programa, que abarcan a los usuarios, así como el acceso a submenús.
    public static void mainOptions() {

        // Se definen estas variables para evitar el uso de "Magic Numbers".
        final int ADD_USER = 1;
        final int SEARCH_USER = 2;
        final int EDIT_USER = 3;
        final int DELETE_USER = 4;
        final int LIST_OPTIONS = 5;
        final int BACKUP_OPTIONS = 6;
        final int END_PROGRAM = 7;

        try {
            int opcion = DbMethods.getUserIntInput("\nSeleccione una opción:\n[1] Agregar user \n[2] Buscar user \n[3] Editar user \n[4] Eliminar user \n[5] Opciones de lista \n[6] Opciones de Backup \n[7] Finalizar el programa");
            switch (opcion) {
                case ADD_USER -> DbMethods.addUser();
                case SEARCH_USER -> DbMethods.searchUser(1);
                case EDIT_USER -> DbMethods.editUserOption();
                case DELETE_USER -> DbMethods.deleteUserOption();
                case LIST_OPTIONS -> Options.listOptions();
                case BACKUP_OPTIONS -> Options.backupOptions();
                case END_PROGRAM -> {
                    DbMethods.outputMessage("\nFin del programa.");
                    System.exit(0);
                }
                default -> {
                    validOption();
                    mainOptions();
                }
            }
            optionsContinue();
        } catch (InputMismatchException | IOException e) {
            onlyNumbers();
            mainOptions();
        }
    }



    static final int CREATE_FILE = 1;
    static final int VIEW_FILE = 2;
    static final int DELETE_FILE = 3;



    // Opciones en torno a la Lista de usuarios o el archivo "database.txt".
    public static void listOptions() {
        final int OVERWRITE_LIST = 4;
        final int GO_BACK = 5;
        int opcion;
        try {
            opcion = DbMethods.getUserIntInput("\nSeleccione una opción: \n[1] Crear lista \n[2] Ver lista \n[3] Eliminar lista \n[4] Sobreescribir lista con Backup \n[5] Volver atrás");
            switch (opcion) {
                case CREATE_FILE -> DbMethods.createFile(1);
                case VIEW_FILE -> DbMethods.viewFile(DbMethods.database);
                case DELETE_FILE -> DbMethods.deleteFile(DbMethods.database);
                case OVERWRITE_LIST -> DbMethods.overwriteList();
                case GO_BACK -> Options.mainOptions();
                default -> {
                    validOption();
                    listOptions();
                }
            }
        } catch (InputMismatchException | IOException e) {
            onlyNumbers();
            listOptions();
        }
    }



    // Opciones para la Copia de Seguridad de la lista o el archivo "backup.txt"
        public static void backupOptions() {
        final int GO_BACK = 4;
        int opcion;
            try {
                opcion = DbMethods.getUserIntInput("\n[1] Crear Backup \n[2] Ver Backup \n[3] Eliminar Backup \n[4] Volver atrás");
                switch (opcion) {
                    case CREATE_FILE -> DbMethods.createFile(2);
                    case VIEW_FILE -> DbMethods.viewFile(DbMethods.backup);
                    case DELETE_FILE -> DbMethods.deleteFile(DbMethods.backup);
                    case GO_BACK -> Options.mainOptions();
                    default -> {
                        validOption();
                        Options.backupOptions();
                    }
                }
            } catch (InputMismatchException | IOException e) {
                onlyNumbers();
                backupOptions();
            }
        }



        // Opción de continuar utilizando el programa una vez terminado un proceso. En caso positivo se vuelve al menú principal.
        static void optionsContinue() {
            try {
                String opcion = DbMethods.getUserInput("\n¿Desea realizar otra operación?\n[S] Si   [N] No");
                switch (opcion.toLowerCase()) {
                    case "s" -> mainOptions();
                    case "n" -> {
                        DbMethods.outputMessage("\nFin del programa.");
                        System.exit(0);
                    }
                    default -> {
                        validOption();
                        optionsContinue();
                    }
                }
            } catch (NoSuchElementException e) {
                validOption();
                DbMethods.scanner.nextLine();
                optionsContinue();
            }
        }



        // Esta función imprime el siguiente texto: "Por favor, ingrese una opción válida", cada vez que se necesite el input
        // del usuario y este ingrese una opción inexistente.
        static void validOption() {
            DbMethods.outputMessage("\nPor favor, ingrese una opción válida.");
        }



        // Esta función cumple un propósito similar al de validOption(), pero toma en cuenta las opciones en las que
        // se deben introducir únicamente números, además de que aquí se vuelve a solicitar el input del usuario.
        static void onlyNumbers(){
            DbMethods.outputMessage("\nPor favor, ingrese sólo números.");
            DbMethods.scanner.nextLine();
        }



}