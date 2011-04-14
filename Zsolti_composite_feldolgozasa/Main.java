/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bhdlparser;

import java.util.ArrayList;

/**
 *
 * @author Daneee
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // A fájlunk betöltve
        String file = "composit main (in kaki, out pisi)\nled led1;\ngenerator generator1;\nswitch switch1;\nled1 =  generator1 | switch1;\nendcompositcomposit mn (in, out)\nled led1;\ngenerator generator1;\nswitch switch1;\nled1 =  generator1 | switch1;\nendcomposit";
        ArrayList<String> foundComposites = new ArrayList<String>();

        //System.out.println(file);
        //System.out.println();

        //Kivesszük entereket
        file = RegularParser.remove_CR_LF(file);
        //System.out.println(file);


        //Amig nem üres a file

        while (!file.equals("")) {

            //Megkeressük composite-t
            String composit = RegularParser.find_next_Composite(file);
            //System.out.println(composit);
            //System.out.println();

            //Kivesszük régiből
            file = file.replace(composit, "");
            //System.out.println(file);
            //System.out.println();

            //Hozzáadjuk a tömbhöz
            foundComposites.add(composit);
        }

        for (String found : foundComposites) {
            System.out.println(found);
            //System.out.println(asd);
        }

        //elso composit-ot nezem csak meg
        ArrayList<String> elements = new ArrayList<String>();
        elements = RegularParser.getElementsOfComposite(foundComposites.get(0));

        for (String i : elements)
            System.out.println(i);

        int j = 2;
    }
}
