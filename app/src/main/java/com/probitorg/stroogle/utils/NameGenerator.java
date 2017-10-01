package com.probitorg.stroogle.utils;

import java.util.Random;

/**
 * Created by Mihai on 13/07/2016.
 */
public class NameGenerator {

    private static String[] Beginning = { "John", "Carlos", "Raul", "Mirok", "Cruella"};
    private static String[] Middle = { "Mc", "Da", "Of", "As", "Ma"};
    private static String[] End = { "Anzy", "Gama", "Artemis", "Vortec", "Militie"};

    private static Random rand = new Random();

    public static String generateName() {

        return Beginning[rand.nextInt(Beginning.length)] +
                Middle[rand.nextInt(Middle.length)]+
                End[rand.nextInt(End.length)];
    }

}
