/*  IMPORTOK  */
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

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

public class DigitalBoard{
	/* ATTRIBUTUMOK */
	/** DEBUG-hoz szukseges, osztalyra jellemzo valtozo. {@code true} eseten 
	 * kulonbozo fuggvenyek hivasakor informaciot szolgaltat.
	 */
	public static boolean DebugMode = false;
	// RUNNING allapotban hanyszor fusson le az egesz
	public static int MaxNumOfRound = 3;
	
	/**
	 * Haromallapotu valtozo, amely a szimulacio aktualis allapotat tarolja
	 */
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
	 * @throws IOException 
	 */
	public void LoadBoard(String strFilePath) throws IOException {
		boolean exists = (new File(strFilePath)).exists();
		if (!exists) {
			 throw new FileNotFoundException();
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
	 * @throws IOException 
	 */
	public void ParseFile(String strFilePath) throws IOException {
		File file = new File(strFilePath);
		BufferedInputStream bin = null;
		String strFileContents = "";

		// FileInputStream object letrehozasa
		FileInputStream fin = new FileInputStream(file);
		// BufferedInputStream obkejtum letrehozasa a beolvasashoz
		bin = new BufferedInputStream(fin);
		// byte tomb letrehozasa, ebbe olvasunk be majd.
		byte[] contents = new byte[1024];
		int bytesRead = 0;

		while ((bytesRead = bin.read(contents)) != -1) {
			strFileContents = new String(contents, 0, bytesRead);
			if (DebugMode)
				System.out.println(strFileContents + "\n");
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
	 * @throws ConnectionsException 
	 * @throws UnstableCircuitException 
	 */
	public void Run() throws UnstableCircuitException, ConnectionsException {
		SetStatus(Status.RUNNING);
		
		boolean exit=false;
		int RunCounter=0;
		while(SimStatus == Status.RUNNING && !exit){
			if(DebugMode){
				System.out.println(RunCounter + ". Round.");
			}
			MainComposit.StepComponents();
			RunCounter++;
			if(RunCounter >= MaxNumOfRound)
				exit=true;
		}
	}

	/**
     * A metodus meghivja a SetStatus metodust PAUSED parameterrel
     */
    public void Pause() {
        SetStatus(Status.PAUSED);
    }


    /**
     * A metodus meghivja a SetStatus metodust STOPPED parameterrel
     */
    public void Stop() {
        SetStatus(Status.RUNNING);
    }


    /**
     * Atallitja a SimStatus attributumot a parameterben megadott ertekre
     *
     * @param NewStatus
     *            Az uj allapot
     */
    private void SetStatus(Status NewStatus) {
        SimStatus = NewStatus;
    }

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
    	MainComposit.SetFrequency(Frequency, ElementID);
    }


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
    	MainComposit.SetSequence(Sequence, ElementID);
    }

    /**
     * A parameterben megadott azonositoju SWITCH objektum erteket az
     * ellenkezore allitja azaltal, hogy meghivja az objektum hasonlo nevu
     * parameteret
     *
     * @param ElementID
     *            A SWITCH ID-ja
     */
    public void Toggle(String ElementID) {
        MainComposit.Toggle(ElementID);
    }

    /**
     * A parameterben megadott azonositoju OSCILLOSCOPE objektum frekvenciajat
     * modositja
     *
     * @param SampleSize
     *            A mintavetelezes nagysaga
     * @param ElementID
     *            A modositani kivant Oscilloscope IDja
     */
    public void SetSample(int SampleSize, String ElementID) {
    	MainComposit.SetSample(SampleSize, ElementID);
    }

    /**
     * Meghivja az osszes iComponent interfeszt megvalosito objektum Step()
     * metodusat.
     * @throws ConnectionsException 
     * @throws UnstableCircuitException 
     */
    public void StepComponents() throws UnstableCircuitException, ConnectionsException {
        MainComposit.StepComponents();
    }

   
}
