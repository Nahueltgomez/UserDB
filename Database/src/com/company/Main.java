package com.company;

public class Main {



    // Se muestra un mensaje de bienvenida, se llama a la función de opciones principales y luego de que el usuario
    // finalice el programa se cierra el scanner utilizado en todos los procesos para evitar el derroche de recursos.
    public static void main(String[] args) {
        DbMethods.outputMessage("¡Bienvenido a la Base de Datos de usuarios!\n");
        Options.mainOptions();
        DbMethods.scanner.close();
    }



}
