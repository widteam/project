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
		Logger.Log(Logger.log_type.ADDITIONAL, "called DigitalBoard's GetElementByID("+ElementID+")");
		return MainComposit.GetElementByID(ElementID);
	};

	/**
	 * A megfelelo parameterrel meghivja a ParseFile(String strFilePath)
	 * metodust.
	 * 
	 * @param strFilePath
	 *            Az aramkort leiro dokumentum (*.bhdl) eleresi utvonala
	 * @throws IOException 
	 * @throws ExceptionWrongBoard 
	 */
	public void LoadBoard(String strFilePath) throws IOException,  ExceptionWrongBoard {
		Logger.Log(Logger.log_type.ADDITIONAL, "called DigitalBoard's GetElementByID("+strFilePath+")");
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
	 * @throws ExceptionWireHasMultipleInputs 
	 * @throws ExceptionWrongBoard 
	 */
	public void ParseFile(String strFilePath) throws IOException, ExceptionWrongBoard {
		Logger.Log(Logger.log_type.ADDITIONAL, "called DigitalBoard's GetElementByID("+strFilePath+")");
		
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
			Logger.Log(Logger.log_type.DEBUG, strFileContents);
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
		
		Debug();
	}
	public void Debug(){
		/* KIIRATAS, DEBUG */
		MainComposit.Debug(true);
	};
	
	/**
	 * metodus meghivja a SetStatus metodust RUNING parameterrel 
	 */
	public void Run() {
		Logger.Log(Logger.log_type.ADDITIONAL, "called DigitalBoard's Run()");
		SetStatus(Status.RUNNING);		
	}

	/**
     * A metodus meghivja a SetStatus metodust PAUSED parameterrel
     */
    public void Pause() {
    	Logger.Log(Logger.log_type.ADDITIONAL, "called DigitalBoard's Pause()");
        SetStatus(Status.PAUSED);
    }


    /**
     * A metodus meghivja a SetStatus metodust STOPPED parameterrel
     */
    public void Stop() {
    	Logger.Log(Logger.log_type.ADDITIONAL, "called DigitalBoard's Stop()");
        SetStatus(Status.STOPPED);       
    }


    /**
     * Atallitja a SimStatus attributumot a parameterben megadott ertekre
     *
     * @param NewStatus
     *            Az uj allapot
     */
    private void SetStatus(Status NewStatus) {
        SimStatus = NewStatus;
        Logger.Log(Logger.log_type.DEBUG, "DigitalBoard's new status is "+SimStatus.toString());
    }

    /**
     * A parameterben megadott azonositoju GENERATOR objektum frekvenciajat
     * modositja
     *
     * @param Frequency
     *            Az uj frekvencia
     * @param ElementID
     *            A modositani kivant GENERATOR IDja
     * @throws ExceptionObjectNotFound Ha nem talalja a beallitai kivant objektumot
     * @throws ExceptionWrongParameter 
     */
    public void SetFrequency(int Frequency, String ElementID) throws ExceptionObjectNotFound, ExceptionWrongParameter {
    	Logger.Log(Logger.log_type.ADDITIONAL, "called DigitalBoard's SetFrequency("+Frequency+","+ElementID+")");
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
     * @throws ExceptionObjectNotFound 
     */
    public void SetSequence(String Sequence, String ElementID) throws ExceptionObjectNotFound {
    	Logger.Log(Logger.log_type.ADDITIONAL, "called DigitalBoard's SetSequence("+Sequence+","+ElementID+")");
    	MainComposit.SetSequence(Sequence, ElementID);
    }

    /**
     * A parameterben megadott azonositoju SWITCH objektum erteket az
     * ellenkezore allitja azaltal, hogy meghivja az objektum hasonlo nevu
     * parameteret
     *
     * @param ElementID
     *            A SWITCH ID-ja
     * @throws ExceptionObjectNotFound 
     */
    public void Toggle(String ElementID) throws ExceptionObjectNotFound {
    	Logger.Log(Logger.log_type.ADDITIONAL, "called DigitalBoard's Toggle("+ElementID+")");
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
     * @throws ExceptionObjectNotFound 
     * @throws ExceptionWrongParameter 
     */
    public void SetSample(int SampleSize, String ElementID) throws ExceptionObjectNotFound, ExceptionWrongParameter {
    	Logger.Log(Logger.log_type.ADDITIONAL, "called DigitalBoard's SetSample("+SampleSize+","+ElementID+")");
    	MainComposit.SetSample(SampleSize, ElementID);
    }

    /**
     * Meghivja az osszes DigitalObject tipusu objektum Step()
     * metodusat.
     * @throws ExceptionsWithConnection 
     * @throws ExceptionUnstableCircuit 
     */
    public void StepComponents() throws ExceptionUnstableCircuit, ExceptionsWithConnection {
    	Logger.Log(Logger.log_type.ADDITIONAL, "called DigitalBoard's StepComponents()");
    	if(SimStatus == Status.RUNNING)
    		MainComposit.StepComponents();
    	else if(SimStatus == Status.PAUSED){
    		MainComposit.StepComponents();
    	}
    }

   public Status GetStatus(){
	   Logger.Log(Logger.log_type.ADDITIONAL, "called DigitalBoard's GetStatus()");
	   return SimStatus;
   }
}
