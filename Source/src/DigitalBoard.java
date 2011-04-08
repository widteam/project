/*
* Név: 			DigitalBoard
* Típus: 		Class
* Interfacek:	---
* Szülõk		---
* 
*********** Leírás **********
* A digitális áramkört nyilvántartó és a vezérlést  biztosító objektum.
* Az áramkör összes elemét és * a köztük lévõ kapcsolatokat létrehozza,
* kezeli és tárolja. Új áramkör megnyitásakor betölti az áramkört egy
* fájlból – eközben ellenõrzi a szintaktikai helyességét, 
* létrehoz minden digitális elemet, hierarchia szerint sorrendezi,
* felfedezi a visszacsatolásokat. 
* További feladata az áramkör számításait vezérelni, 
* így ha a felhasználó léptetést kér, a generátorokat lépteti, 
* és újraszámolja az áramkör komponenseinek állapotát. 
* Ha a felhasználó a futtatást választja, változtatható idõközönként 
* lépteti a jelgenerátort, és a komponensek értékeit újraszámolja.
*/
/**
*
* @author Jégh Tamás
*/

/*  IMPORTOK  */
import java.io.*;
import java.util.*;	// List, ArrayList-hez

public class DigitalBoard {
	/*	ATTRIBÚTUMOK	*/
	@SuppressWarnings("unused")
	private Status SimStatus; 

	// Leírás: Háromállapotú változó, amely a szimuláció aktuális állapotát tárolja 

	private ArrayList< List<DigitalObject> > ComponentList;
	/* Leírás: Ez az attribútum tárolja az összes kaput, kimenetet, bemenetet
	 * hierarchikus sorrendben
	 * Ez nem más, mint egy listából szervezett tömb. A tömb indexe
	 * azonosítja a hierarchia szintet 
	 * (0-Források, 1-a forrásokhoz csatlakozó elemek, stb)
	 * az egyes szinteken pedig egy lista van az elemekrõl
	*/	
	
	@SuppressWarnings("unused")
	private List<Wire> WireList;
	// Leírás: Egyszerû lista a Wire objektumokból

	/*  KONSTRUKTOR  */ 
	public DigitalBoard(){		
		SimStatus = Status.STOPPED;
		ComponentList = new ArrayList<List<DigitalObject>>();
		WireList = new ArrayList<Wire>();	
	}
	/*	METÓDUSOK	*/
	public DigitalObject GetElementByID(String ElementID){
	// Leírás: Megkeres egy adott elemet a 	ComponentList illetve a WireList listákban
		if(ComponentList != null && !ComponentList.isEmpty()){
			for(List<DigitalObject> sublist: ComponentList){
				for(DigitalObject o : sublist){
					if(o.ID == ElementID)
						return (DigitalObject) o;
				}
			}
		}
		return null;		
	};

	public void LoadBoard(String strFilePath){
	// Leírás: A megfelelõ paraméterrel meghívja a ParseFile(String strFilePath) metódust.
	    boolean exists = (new File(strFilePath)).exists();
        if (!exists) {
        	// throw FileDoesNotExistException;
        } else {
            ParseFile(strFilePath);
        }
    };

	public void ParseFile(String strFilePath){
	// Leírás: A megadott útvonalon található fájlt olvassa be és soronként értelmezi az állományt
		File file = new File(strFilePath);
        BufferedInputStream bin = null;


		try
		{
			//create FileInputStream object
			FileInputStream fin = new FileInputStream(file);
			//create object of BufferedInputStream
			bin = new BufferedInputStream(fin);
			//create a byte array
			byte[] contents = new byte[1024];
			int bytesRead=0;
			String strFileContents;
			while( (bytesRead = bin.read(contents)) != -1){
				strFileContents = new String(contents, 0, bytesRead);
				System.out.print(strFileContents);
			}
		}
		catch(FileNotFoundException e){
			System.out.println("File not found" + e);
		}
		catch(IOException ioe){
			System.out.println("Exception while reading the file "+ ioe);
		}
    };

	public void Run(){
	// Leírás: metódus meghívja a SetStatus metódust RUNING paraméterrel	
		SetStatus(Status.RUNNING);	
	};
	public void Pause(){
	// Leírás: A metódus meghívja a SetStatus metódust PAUSED paraméterrel		
		SetStatus(Status.PAUSED);
	};	
	public void Stop(){
	// Leírás:  A metódus meghívja a SetStatus metódust STOPPED paraméterrel
		SetStatus(Status.RUNNING);
	};		
	private void SetStatus(Status NewStatus){
	// Leírás: Átállítja a SimStatus attribútumot a paraméterben megadott értékre
		SimStatus = NewStatus;
	};	
	public void SetFrequency(int Frequency, String ElementID){
	// Leírás: A paraméterben megadott azonosítójú GENERATOR objektum frekvenciáját módosítja
		GENERATOR tmp;
		tmp = (GENERATOR) GetElementByID(ElementID);
		tmp.SetFrequency(Frequency);
	};
	public void SetSequence(int Sequence, String ElementID){
	// Leírás: A paraméterben megadott azonosítójú GENERATOR objektum szekvenciáját módosítja
		GENERATOR GEN_to_setsequence;	/* Temporális változó */
		GEN_to_setsequence = (GENERATOR) GetElementByID(ElementID);	/* GetElemetByIDvel megkapjuk, az objektumot	*/		
		GEN_to_setsequence.SetSequence(Sequence); 				 /* az generátor objektum SetSequence(...) metódusát meghívjuk */
	
	};	
	public void Toggle(String ElementID){
	/* Leírás: A paraméterben megadott azonosítójú SWITCH objektum értékét az ellenkezõre 
	 * állítja azáltal, hogy meghívja az objektum hasonló nevû paraméterét
	*/
		SWITCH SWITCH_to_toggle;								/* Temporális változó */
		SWITCH_to_toggle = (SWITCH) GetElementByID(ElementID);	/* GetElemetByIDvel megkapjuk, majd  egyet	*/		
		SWITCH_to_toggle.Toggle();								/* az switch objektum Toggle() metódusát meghívjuk */

	};	
	public void StepComponents(){
	// Leírás: Meghívja az összes iComponent interfészt megvalósító objektum Step() metódusát.

		/* Elvileg már fel van épülve a hierarchia így nekem elég megkapnom a ComponentListet */
		DigitalObject obj;
		for(List<DigitalObject> sublist: ComponentList){
			for(DigitalObject o : sublist){
				obj = (DigitalObject) o;
				obj.Step();
			}
		}
	};
}
