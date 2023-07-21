package com.company;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.BufferedReader;

public class DbMethods {



    public static final Scanner scanner = new Scanner(System.in);
    public static final File database = new File("database.txt");
    public static final File backup = new File("backup.txt");
    public static final File temp = new File ("temp.txt");



    // Se muestra un prompt solicitando input del usuario.
    public static String getUserInput(String userInputMessage) {
        outputMessage(userInputMessage);
        return scanner.next();
    }



    // Se muestran mensajes predefinidos. De esta manera se evita emplear System.out.println("") de forma excesiva.
    public static void outputMessage(String message) {
        System.out.println(message);
    }



    // Se obtiene el input del usuario a través de un prompt, pero este input solo puede ser un valor numérico.
    static int getUserIntInput(String message) {
        outputMessage(message);
        return scanner.nextInt();
    }



    // Se obtiene y almacena la información de los usuarios dentro de la base de datos.
    public static void addUser() {
            HashMap<Integer, String> userMap = new HashMap<>();
        userMap.put(1, getUserInput("\nIngrese el nombre:"));
        userMap.put(2, getUserInput("\nIngrese el apellido:"));
        userMap.put(3, getUserInput("\nIngrese la edad:"));
        userMap.put(4, getUserInput("\nIngrese el correo:"));
        userMap.put(5, getUserInput("\nIngrese el teléfono:"));
            try (
                    FileWriter writer = new FileWriter(database.getAbsoluteFile(), true)
            ) {
                String userString = "Nombre: " + userMap.get(1) +
                        ", Apellido: " + userMap.get(2) +
                        ", Edad: " + userMap.get(3) +
                        ", E-Mail: " + userMap.get(4) +
                        ", N° Telefónico: " + userMap.get(5) +
                        "\n";

                writer.write(userString);
            } catch (IOException e) {
                outputMessage("Error al agregar al usuario: " + e.getMessage());
            }
        outputMessage("\nUsuario agregado correctamente.");
    }



    // Se definen estas variables para evitar el uso de "Magic Numbers".
    static final int EDIT_NAME = 1;
    static final int EDIT_LAST_NAME = 2;
    static final int EDIT_AGE = 3;
    static final int EDIT_EMAIL = 4;
    static final int EDIT_PHONE = 5;
    static final int GO_BACK = 6;



    // Se le muestra al usuario un prompt para que ingrese cuál es el dato en específico que quiere modificar del
    // usuario obtenido. Luego se traslada esa información a otra función que realiza el proceso.
    public static void editUser(String user) {
        if (!user.equals("0")) {
            outputMessage("\n" + user);
            int selection;
            try {
                selection = getUserIntInput("\nSeleccione la información que desea editar:\n[1] Nombre \n[2] Apellido \n[3] Edad \n[4] Correo \n[5] Teléfono \n[6] Volver atrás \n");
                switch (selection) {
                    case EDIT_NAME -> modifyUser(EDIT_NAME, user);
                    case EDIT_LAST_NAME -> modifyUser(EDIT_LAST_NAME, user);
                    case EDIT_AGE -> modifyUser(EDIT_AGE, user);
                    case EDIT_EMAIL -> modifyUser(EDIT_EMAIL, user);
                    case EDIT_PHONE -> modifyUser(EDIT_PHONE, user);
                    case GO_BACK -> Options.mainOptions();
                }
            } catch (InputMismatchException e) {
                Options.onlyNumbers();
                editUser(user);
            }
        }
    }



    // Se modifica la información del usuario obtenido, basándose en la selección de la función editUser().
    public static void modifyUser(int editOption, String user) {
        try (
                FileReader fr = new FileReader(database.getAbsoluteFile());
                BufferedReader br = new BufferedReader(fr);
                FileWriter writer = new FileWriter(temp.getAbsoluteFile(), false)
        ) {
            String currentLine;
            while ((currentLine = br.readLine()) != null) {

                String trimmedLine = currentLine.trim();

                if (trimmedLine.equals(user) && !trimmedLine.contains("\n")) {
                    switch (editOption) {
                        case EDIT_NAME -> {
                            modifyUserInfo("nombre",user,"Nombre: ",", Apelli",writer);
                            continue;
                        }
                        case EDIT_LAST_NAME -> {
                            modifyUserInfo("apellido",user,"Apellido: ",", Edad: ",writer);
                            continue;
                        }
                        case EDIT_AGE -> {
                            modifyUserInfo("valor de edad",user,"Edad: ",", E-Mail:",writer);
                            continue;
                        }
                        case EDIT_EMAIL -> {
                            modifyUserInfo("correo",user,"E-Mail: ",", N° Tel",writer);
                            continue;
                        }
                        case EDIT_PHONE -> {
                            modifyUserInfo("teléfono",user,"N° Telefónico: ","",writer);
                            continue;
                        }
                    }
                }
                writer.write(currentLine + System.getProperty("line.separator"));
            }
        } catch (IOException e){
            outputMessage("Error al modificar el usuario: " + e.getMessage());
        }
        database.delete();
        temp.renameTo(database);
    }



    // Se modifica la información específica del usuario obtenido, pero separado en otra función para evitar duplicar código.
    public static void modifyUserInfo(String editingInfo, String user, String prefix, String suffix, FileWriter writer) throws IOException {
        String value = getUserInput("\nIngrese el nuevo " + editingInfo + ":");
        int prefixIndex = user.indexOf(prefix);
        int suffixIndex;
        if (!suffix.isEmpty()) {
            suffixIndex = user.indexOf(suffix, prefixIndex);
        } else {
            suffixIndex = user.length();
        }
        if (prefixIndex != -1 && suffixIndex != -1) {
            String modifiedUser = user.substring(0, prefixIndex + prefix.length()) +
                    value +
                    user.substring(suffixIndex);
            writer.write(modifiedUser + System.getProperty("line.separator"));
        } else {
            writer.write(user + System.getProperty("line.separator"));
        }
    }



    // Se utiliza esta función para simplificar el llamado a searchUser() y editUser() desde mainOptions(), además de que
    // de esta manera se evita llamar a editUser() con un usuario inexistente y provocar errores en la ejecución.
    public static void editUserOption(){
        String user = searchUser(2);
        if (!user.equals("0")) {
            editUser(user);
        }
    }



    // Se obtiene un usuario específico para proceder con su eliminación.
    // Luego se le pregunta al usuario si está seguro de proceder con la acción seleccionada.
    public static void deleteUser(String userToDelete) {
        outputMessage("\nUsuario a eliminar:" +"\n" + userToDelete);
        String opcion = getUserInput("\nIngrese [S] para confirmar la acción o [N] para volver atrás:");
        if (opcion.equalsIgnoreCase("s")) {
            if (userToDelete.equals("0")) {
                outputMessage("\nNo se encontró ningún usuario para eliminar.");
            } else {
                deleteUserProcess(userToDelete);
            }
        } else if (!opcion.equalsIgnoreCase("n")) {
            Options.validOption();
            deleteUser(userToDelete);
        }
    }



    // Se procede con la eliminación del usuario obtenido.
    public static void deleteUserProcess(String userToDelete) {
        File temp = new File("temp.txt");
        try (
                FileReader fr = new FileReader(database.getAbsoluteFile());
                BufferedReader br = new BufferedReader(fr);
                FileWriter writer = new FileWriter(temp, false)
        ) {
            String currentLine;
            boolean deleted = false;
            while ((currentLine = br.readLine()) != null) {
                if (currentLine.equals(userToDelete) && !currentLine.contains("\n")) {
                    deleted = true;
                    continue;
                }
                writer.write(currentLine + System.getProperty("line.separator"));
            } if (deleted) {
                outputMessage("\nUsuario eliminado correctamente.");
            } else {
                outputMessage("\nUsuario no eliminado.");
            }
        } catch (IOException e) {
            outputMessage("Error al eliminar el usuario: " + e.getMessage());
        }
        database.delete();
        temp.renameTo(database);
    }



    // Esta función posee el mismo propósito que editUserOption(), el cual es el de simplificar el llamado desde mainOptions()
    // y evitar llamar a una función (deleteUser()) proporcionando un usuario inexistente.
    public static void deleteUserOption() throws IOException {
        String user = searchUser(2);
        if (!user.equals("0")) {
            deleteUser(user);
        }
    }



    // Se solicita al usuario introducir un parámetro para buscar a un usuario o un grupo de usuarios.
    public static String searchUser(int from) {

        ArrayList<String> al = new ArrayList<>();
        String info = getUserInput("\nIngrese información para filtrar a el / los usuarios (Nombre, Apellido, Teléfono, Edad o Correo):");
        String currentLine;
        try (
                FileReader fr = new FileReader(database.getAbsoluteFile());
             BufferedReader br = new BufferedReader(fr)
             ) {
            boolean exists = false;
            String searchedLine = null;
            while ((currentLine = br.readLine()) != null) {
                if (currentLine.contains(info)) {
                    exists = true;
                    al.add(currentLine + System.getProperty("line.separator"));
                    searchedLine = currentLine;
                }
            }
            if (exists) {  // Luego, se imprime en pantalla la información obtenida, o se devuelve con un return, dependiendo de la función desde la que se proviene.
                switch (from) {
                    case 1 -> outputMessage("\n" + al);
                    case 2 -> {
                        return searchedLine;
                    }

                }
            } else {
                String eleccion = getUserInput("\nNo se pudieron encontrar usuarios con la información otorgada\n¿Desea volver a intentarlo?\n[S] Si   [N] No");
                if (eleccion.equalsIgnoreCase("s")) {
                    searchUser(from);
                }
            }
        } catch (IOException e) {
            outputMessage("Error al buscar el usuario: " + e.getMessage());
        }
        return "0";
    }



    // Se obtiene un archivo (backup o database) y se imprime en pantalla en caso de que exista.
    public static void viewFile(File file) throws IOException {
        if (file.exists()) {
            ArrayList<String> showFile = new ArrayList<>(); // Se emplea un ArrayList para así poder guardar todas las líneas de texto e imprimirlas con una variable.
            try (
                    FileReader fr = new FileReader(file);
                 BufferedReader br = new BufferedReader(fr)
                 ) {
                outputMessage("\nLista de usuarios:");
                String currentLine;
                while ((currentLine = br.readLine()) != null) {
                    showFile.add(currentLine + System.getProperty("line.separator"));
                }
                outputMessage("\n" + showFile);
                showFile.clear();  // Finalmente, se vacía el ArrayList en caso de que se vuelva a llamar a esta función.
            }
        } else {
            outputMessage("\nEl archivo no existe y debe ser creado para realizar esta acción.");
        }
    }



    // Se crea un archivo (backup o database) en caso de que este no exista previamente.
    public static void createFile(int from) throws IOException {

        switch (from) {
            case 1 ->
            {
                try {
                    if (!database.exists()) {
                        database.createNewFile();
                        outputMessage("\nLa lista se ha creado con éxito.");
                    } else {
                        outputMessage("\nLa lista ya existe.");
                    }
                } catch (Exception e) {
                    outputMessage("Error al crear la lista: " + e.getMessage());
                }
            }
            case 2 ->
            {
                if (database.exists()) {
                    File backup = new File("backup.txt"); // Para crear el Backup, se deberá crear un archivo nuevo y escribir toda la información de la database en él.
                    try (
                            FileReader fr = new FileReader(database);
                            BufferedReader br = new BufferedReader(fr);
                            FileWriter fw = new FileWriter(backup)
                    ) {
                        String currentLine;
                        while ((currentLine = br.readLine()) != null) {
                            fw.write(currentLine + System.getProperty("line.separator"));
                        }
                        outputMessage("\nEl Backup se ha creado con éxito.");
                    } catch (Exception e){
                        outputMessage("Error al crear el BackUp: " + e.getMessage());
                    }
                } else {
                    outputMessage("\nPara crear un Backup debe existir una lista.");
                }
            }
        }
    }



    // Se sobreescribe la database con la información del backup.
    public static void overwriteList() {
        if (backup.exists() && database.exists()) { // Si existen ambos archivos, se elimina la database y luego se renombra el backup.
            database.delete();
            backup.renameTo(database);
            outputMessage("\nOperación exitosa.");
        } else if (!backup.exists()) { // Si no existe el backup, se le informa al usuario.
            outputMessage("\nNo se ha creado un backup.");
        } else if (!database.exists()) { // Si no existe la database, únicamente se le cambia el nombre al backup.
            backup.renameTo(database);
            outputMessage("\nOperación exitosa.");
        } else {
            outputMessage("\nNo se han creado ni un Backup ni una lista.");
        }
    }



    // Se elimina el archivo proporcionado (database o backup) en caso de que este exista.
    public static void deleteFile(File file) {
        if (file.exists()) {
            file.delete();
            outputMessage("\nArchivo eliminado exitosamente.");
        } else {
            outputMessage("El Archivo no existe. Para realizar esta acción debe crearlo primero.");
            Options.mainOptions();
        }
    }



}