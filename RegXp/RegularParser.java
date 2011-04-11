/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bhdlparser;
import java.util.regex.*;

/**
 *
 * @author Daneee
 */
public class RegularParser {

    /**
     * remove_CR_LF: Kiszedi az entereket és egyéb karaktereket a kapott a sztringből
     * @param source:Felesleges spacektől mentes(mert kell elemek létrehozásától)
     * @return Az enter nélküli sztring
     */
    public static String remove_CR_LF(String source){

        //Kivesszük a Carrige Return és Line Fedd elemeket
        source = source.replaceAll("\n", "");
        source = source.replaceAll("\r", "");

        return source;
    }

    /**
     * find_next_Composite:visszaadja a következő Composite-ot a fájlból
     * @param source:A fájl,ami Composite-kat tartalmaz
     * @return A kivett Comopsite
     */
    public static String find_next_Composite(String source){

        //Reguláris Kifejezés
        Pattern regexp = Pattern.compile("composit.*?endcomposit");
        
        //Megkeressük a találatokat
        Matcher match = regexp.matcher(source);
        
        //Első kell
        match.find();
        String foundComposit = match.group();

        return foundComposit;
    }
}
