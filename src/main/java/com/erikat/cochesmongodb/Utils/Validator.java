package com.erikat.cochesmongodb.Utils;

public class Validator {
    public static boolean validatePlate(String plate){
        return plate.matches("[0-9]{4}[BCDFGHJKLMNPRSTVWXYZ]{3}") || plate.matches("[A-Z]{1,2}[0-9]{4}[A-Z]{2}");
        /*
        Traducción del regEx:
            1. 4 números del 0 al 9
            2. 3 letras mayúsculas de la B a la Z, quitando las vocales E I O y U (En españa, las letras antiguas
         */
    }
}
