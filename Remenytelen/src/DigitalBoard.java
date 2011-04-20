/*  IMPORTOK  */
/*  IMPORTOK  */
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** 
 * <table border=0>
 * 	<tr align=left>
 * 		<th>Nev:</th>
 * 		<td width=30>&nbsp;&nbsp;DigitalBoard</td>
 * 	</tr>
 * 	<tr align=left>
 * 		<th>Tipus:</th>
 * 		<td>&nbsp;&nbsp;Class</td>
 * 	</tr>
 * 	<tr align=left>
 * 		<th>Interface: </th>
 * 		<td>&nbsp;&nbsp;---</td>
 * 	</tr>
 * 	<tr align=left>
 * 		<th>Szulok:</th>
 * 		<td>&nbsp;&nbsp;---</td>
 * </table> 
*<br>
* A digitalis aramkort nyilvantarto es a vezerlest  biztosito objektum.
* Az aramkor osszes elemet es a koztuk levo kapcsolatokat letrehozza,
* kezeli es tarolja. Uj aramkor megnyitasakor betolti az aramkort egy
* fajlbol-ekozben ellenorzi a szintaktikai helyesseget, 
* letrehoz minden digitalis elemet, hierarchia szerint sorrendezi,
* felfedezi a visszacsatolasokat. 
* Tovabbi feladata az aramkor szamitasait vezerelni, 
* igy ha a felhasznalo leptetest ker, a generatorokat lepteti, 
* es ujraszamolja az aramkor komponenseinek allapotat. 
* Ha a felhasznalo a futtatast valasztja, valtoztathato idokozonkent 
* lepteti a jelgeneratort, es a komponensek ertekeit ujraszamolja.
*/

public class DigitalBoard {
	/* ATTRIBUTUMOK */

	/**
	 * Haromallapotu valtozo, amely a szimulacio aktualis allapotat tarolja
	 */
	@SuppressWarnings("unused")
	private Status SimStatus;
	/**
	 * Egy fo Composit, ez tartalmaz minden tovabbi elemet, Compositot
	 */
	private Composit MainComposit = null;
	/** KONSTRUKTOR */
	public DigitalBoard() {
		SimStatus = Status.STOPPED;
	}

	/* METODUSOK */
	/**
	 * Megkeres egy adott elemet egy Composit ComponentList listajaban
	 */
	public DigitalObject GetElementByID(String ElementID) {
		return MainComposit.GetElementByID(ElementID);
	};

	/**
	 * A megfelelo parameterrel meghivja a ParseFile(String strFilePath)
	 * metodust.
	 * 
	 * @param strFilePath
	 *            Az aramkort leiro dokumentum (*.bhdl) eleresi utvonala
	 * @throws FileDoesNotExistException
	 *             ha a fajl nem letezik vagy nem olvashato
	 */
	public void LoadBoard(String strFilePath) {
		boolean exists = (new File(strFilePath)).exists();
		if (!exists) {
			// ParseFile(strFilePath);
			// throw FileDoesNotExistException;
		} else {
			ParseFile(strFilePath);
		}
	};

	/**
	 * A megadott utvonalon talalhato fajlt olvassa be es soronkent ertelmezi az
	 * allomanyt
	 * 
	 * @param strFilePath
	 *            Az aramkort leiro dokumentum (*.bhdl) eleresi utvonala
	 */
	public void ParseFile(String strFilePath) {
		File file = new File(strFilePath);
		// System.out.print(file.getAbsolutePath());
		BufferedInputStream bin = null;
		String strFileContents="";
		try {
			// FileInputStream object letrehozasa
			FileInputStream fin = new FileInputStream(file);
			// BufferedInputStream obkejtum letrehozasa a beolvasashoz
			bin = new BufferedInputStream(fin);
			// byte tomb letrehozasa, ebbe olvasunk be majd.
			byte[] contents = new byte[1024];
			int bytesRead = 0;
			
			while ((bytesRead = bin.read(contents)) != -1) {
				strFileContents = new String(contents, 0, bytesRead);
				System.out.println(strFileContents+"\n");
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found" + e);
		} catch (IOException ioe) {
			System.out.println("Exception while reading the file " + ioe);
		}
		
		// Tisztogatas
		
		strFileContents = bhdlParser.remove_Spaces(strFileContents);
		strFileContents = bhdlParser.remove_CR_LF(strFileContents); //nincs sortores
		
		/*
		 * MAIN composit megkeresese letrehozasa
		 */
		String main_composit = bhdlParser.FindMainComposit(strFileContents);
		MainComposit = bhdlParser.CreateMain(main_composit);
		bhdlParser.ReadComposit(MainComposit, strFileContents, MainComposit.GetName());
		Debug(true);
	}
	public void Debug(boolean AllComponent){
		/* KIIRATAS, DEBUG */
		MainComposit.Debug(AllComponent);
	};
	public void Debug(){
		/* KIIRATAS, DEBUG */
		MainComposit.Debug(false);
	};
	/**
	 * metodus meghivja a SetStatus metodust RUNING parameterrel
	 */
	public void Run() {
		SetStatus(Status.RUNNING);
	};

	/**
	 * A metodus meghivja a SetStatus metodust PAUSED parameterrel
	 */
	public void Pause() {
		SetStatus(Status.PAUSED);
	};

	/**
	 * A metodus meghivja a SetStatus metodust STOPPED parameterrel
	 */
	public void Stop() {
		SetStatus(Status.RUNNING);
	};

	/**
	 * Atallitja a SimStatus attributumot a parameterben megadott ertekre
	 * 
	 * @param NewStatus
	 *            Az uj allapot
	 */
	private void SetStatus(Status NewStatus) {
		SimStatus = NewStatus;
	};

	/**
	 * A parameterben megadott azonositoju GENERATOR objektum frekvenciajat
	 * modositja
	 * 
	 * @param Frequency
	 *            Az uj frekvencia
	 * @param ElementID
	 *            A modositani kivant GENERATOR IDja
	 */
	public void SetFrequency(int Frequency, String ElementID) {
		GENERATOR tmp;
		tmp = (GENERATOR) GetElementByID(ElementID);
		tmp.SetFrequency(Frequency);
		

        if (tmp == null) {
            System.out.println("x Error: Wrong Parameter: No object with id " + ElementID);
        } else{
            if (Frequency <= 0) {
                System.out.println("x Error: Frequency must be positive");
        }

        try {
            tmp.SetFrequency(Frequency);
        } catch (Exception ex) {
            System.out.println("x Error: Wrong Parameter: Object is not Generator");
        }
        System.out.println(ElementID + "'s frequency is set to " + Frequency);
        }

		
	};

	/**
	 * A parameterben megadott azonositoju GENERATOR objektum szekvenciajat
	 * modositja
	 * 
	 * @param Sequence
	 *            az uj szekvencia, minta
	 * @param ElementID
	 *            A modositani kivant GENERATOR IDja
	 */
	public void SetSequence(String Sequence, String ElementID) {
		GENERATOR GEN_to_setsequence; /* Temporalis valtozo */
		GEN_to_setsequence = (GENERATOR) GetElementByID(ElementID);
		GEN_to_setsequence.SetSequence(Sequence);
		
		if (GEN_to_setsequence == null) {
              System.out.println("x Error: Wrong Parameter: No object with id " + ElementID);
        }
        try {
              ((GENERATOR) (GEN_to_setsequence)).SetSequence(Sequence);
        } catch (Exception ex) {
              System.out.println("x Error: Wrong Parameter: Object is not Generator");
        }
        System.out.println(ElementID + "'s sequence is set to " + Sequence);

	};

	/**
	 * A parameterben megadott azonositoju SWITCH objektum erteket az
	 * ellenkezore allitja azaltal, hogy meghivja az objektum hasonlo nevu
	 * parameteret
	 * 
	 * @param ElementID
	 *            A SWITCH ID-ja
	 */
	public void Toggle(String ElementID) {
		
		SWITCH SWITCH_to_toggle; /* Temporalis valtozo */
		SWITCH_to_toggle = (SWITCH) GetElementByID(ElementID);
		if (SWITCH_to_toggle == null) {
            System.out.println("x Error: Wrong Parameter: No object with id " + ElementID);
        } else {
	        try {
	            SWITCH_to_toggle.Toggle();
	            System.out.println(ElementID + "'s value is set to " + SWITCH_to_toggle.Value);
	        } catch (Exception ex) {
	            System.out.println("x Error: Wrong Parameter: Object is not Switch");
	        }
        }
	};

	/**
	 * Meghivja az osszes iComponent interfeszt megvalosito objektum Step()
	 * metodusat.
	 */
	public void StepComponents() {
		System.out.println("<main step>");
		MainComposit.StepComponents();
		System.out.println("");
	}	
	public void CountComponents() {
		System.out.println("<main count>");
		MainComposit.Count();
		System.out.println("");
	}	
	public String[] GetCmdParams(String cmdLine) {

        Pattern regxp;
        Matcher match;

        String command = "";
        String param1 = "";
        String param2 = "";
        
        //Regularis Kifejezes
        regxp = Pattern.compile(".*? "); // Megkeressuk parancsot
        //Megkeressuk a talalatokat
        match = regxp.matcher(cmdLine);

        if (match.find()) {
            command = match.group();//Kimentjuk parancsot
        } else {
            regxp = Pattern.compile(".*");//Egyszavas parancs
            match = regxp.matcher(cmdLine);
            if (match.find()) {
                command = match.group();//Kimentjuk parancsot
            }
        }

        cmdLine = cmdLine.replace(command, "");//Kivesszuk talalatot,reszre keresunk cska
        command = command.replace(" ", ""); //Kivesszuk spacet

        match = regxp.matcher(cmdLine);
        if (match.find()) {
            param1 = match.group();//Kimentjuk elso parametert
        } else {
            regxp = Pattern.compile(".*");//Egyszavas parancs
            match = regxp.matcher(cmdLine);
            if (match.find()) {
                param1 = match.group();//Kimentjuk parancsot
            }
        }

        cmdLine = cmdLine.replace(param1, "");//Kivesszuk talalatot,reszre keresunk cska
        param1 = param1.replace(" ", ""); //Kivesszuk spacet

        regxp = Pattern.compile(".*");
        match = regxp.matcher(cmdLine);
        match.find();
        param2 = match.group();//Kimentjuk masodik parametert
        param2 = param2.replace(" ", ""); //Kivesszuk spacet

        return new String[]{ command , param1 , param2 };

    }

    public void HandleUserCommand(BufferedReader reader) {

        //System.out.println("/*******Parancsok fogadasa*******/");

        String cmdLine = "";

        String command = "";
        String param1 = "";
        String param2 = "";

        while (!cmdLine.contentEquals("exit")) {
            try {
                //  a bevitt szoveget
                cmdLine = reader.readLine();

                // parancs es parameterek meghatarozasa
                command = GetCmdParams(cmdLine)[0];
                param1 = GetCmdParams(cmdLine)[1];
                param2 = GetCmdParams(cmdLine)[2];

                // setFrequency
                if (command.equalsIgnoreCase("setFrequency")) {
                    SetFrequency(Integer.parseInt(param2), param1);   
                    CountComponents();
                // stepComponents
                } else if (command.equalsIgnoreCase("step")
                		|| command.equalsIgnoreCase("stepComponents")) {
                    StepComponents();
                // toggleSwitch
                } else if (command.equalsIgnoreCase("toggleSwitch")) {
                	Toggle(param1);
                	CountComponents();
                // setSequence
                } else if (command.equalsIgnoreCase("setSequence")) {
                   SetSequence(param2, param1);
                   CountComponents();
                // exit
                } else if (command.equalsIgnoreCase("exit")) {
                    System.exit(0);
                } else {
                    System.out.println("Unknown command");
                }
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
    }

}