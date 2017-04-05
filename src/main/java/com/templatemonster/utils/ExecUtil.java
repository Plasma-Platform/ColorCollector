package com.templatemonster.utils;

import java.io.IOException;
import java.util.Arrays;

public class ExecUtil {

    /**
     * Tries to exec the command, waits for it to finish, logs errors if exit
     * status is nonzero, and returns true if exit status is 0 (success).
     *
     * @param command Description of the Parameter
     * @return Description of the Return Value
     */
    public static boolean exec(String[] command) {

        System.out.println("exec(): command=" + Arrays.toString(command));

        Process proc;

        try {
            proc = Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            System.out.println("IOException while trying to execute " + Arrays.toString(command));
            return false;
        }

        int exitStatus;

        while (true) {
            try {
                exitStatus = proc.waitFor();
                break;
            } catch (java.lang.InterruptedException e) {
                System.out.println("Interrupted: Ignoring and waiting");
            }
        }
        if (exitStatus != 0) {
            System.out.println("Error executing command: " + exitStatus);
        }
        return (exitStatus == 0);
    }
}
