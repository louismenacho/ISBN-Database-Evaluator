import gui.LoginUI;
import paths.InFile;
import paths.OutFile;

import java.awt.*;

/**
 *Main class from which the program initiates from
 */
public class Main {

    public static void main(String[] args) {
        //start program with no arguments
        if(args.length == 0) {
            start();
        } else {
            //if argument specified set values
            if (args.length == 1) {
                InFile.filePath = args[0];
                InFile.fileSpecified = true;

            } else {
                //if multiple arguments specified set values
                InFile.filePath = args[0];
                System.out.println(args[0]);
                InFile.fileSpecified = true;

                OutFile.filePath = args[1];
                OutFile.fileSpecified = true;
            }
            //start program
            start();
        }
    }

    //Launches the log in interface
    public static void start() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                LoginUI login = new LoginUI();
            }
        });
    }
}
