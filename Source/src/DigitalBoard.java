/*
* N�v: 			DigitalBoard
* T�pus: 		Class
* Interfacek:	---
* Sz�l�k		---
* 
*********** Le�r�s **********
* A digit�lis �ramk�rt nyilv�ntart� �s a vez�rl�st  biztos�t� objektum.
* Az �ramk�r �sszes elem�t �s * a k�zt�k l�v� kapcsolatokat l�trehozza,
* kezeli �s t�rolja. �j �ramk�r megnyit�sakor bet�lti az �ramk�rt egy
* f�jlb�l � ek�zben ellen�rzi a szintaktikai helyess�g�t, 
* l�trehoz minden digit�lis elemet, hierarchia szerint sorrendezi,
* felfedezi a visszacsatol�sokat. 
* Tov�bbi feladata az �ramk�r sz�m�t�sait vez�relni, 
* �gy ha a felhaszn�l� l�ptet�st k�r, a gener�torokat l�pteti, 
* �s �jrasz�molja az �ramk�r komponenseinek �llapot�t. 
* Ha a felhaszn�l� a futtat�st v�lasztja, v�ltoztathat� id�k�z�nk�nt 
* l�pteti a jelgener�tort, �s a komponensek �rt�keit �jrasz�molja.
*/
/**
*
* @author J�gh Tam�s
*/

/*  IMPORTOK  */
import java.io.*;
import java.util.*;	// List, ArrayList-hez

public class DigitalBoard {
	/*	ATTRIB�TUMOK	*/
	@SuppressWarnings("unused")
	private Status SimStatus; 

	// Le�r�s: H�rom�llapot� v�ltoz�, amely a szimul�ci� aktu�lis �llapot�t t�rolja 

	private ArrayList< List<DigitalObject> > ComponentList;
	/* Le�r�s: Ez az attrib�tum t�rolja az �sszes kaput, kimenetet, bemenetet
	 * hierarchikus sorrendben
	 * Ez nem m�s, mint egy list�b�l szervezett t�mb. A t�mb indexe
	 * azonos�tja a hierarchia szintet 
	 * (0-Forr�sok, 1-a forr�sokhoz csatlakoz� elemek, stb)
	 * az egyes szinteken pedig egy lista van az elemekr�l
	*/	
	
	@SuppressWarnings("unused")
	private List<Wire> WireList;
	// Le�r�s: Egyszer� lista a Wire objektumokb�l

	/*  KONSTRUKTOR  */ 
	public DigitalBoard(){		
		SimStatus = Status.STOPPED;
		ComponentList = new ArrayList<List<DigitalObject>>();
		WireList = new ArrayList<Wire>();	
	}
	/*	MET�DUSOK	*/
	public DigitalObject GetElementByID(String ElementID){
	// Le�r�s: Megkeres egy adott elemet a 	ComponentList illetve a WireList list�kban
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
	// Le�r�s: A megfelel� param�terrel megh�vja a ParseFile(String strFilePath) met�dust.
	    boolean exists = (new File(strFilePath)).exists();
        if (!exists) {
        	// throw FileDoesNotExistException;
        } else {
            ParseFile(strFilePath);
        }
    };

	public void ParseFile(String strFilePath){
	// Le�r�s: A megadott �tvonalon tal�lhat� f�jlt olvassa be �s soronk�nt �rtelmezi az �llom�nyt
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
	// Le�r�s: met�dus megh�vja a SetStatus met�dust RUNING param�terrel	
		SetStatus(Status.RUNNING);	
	};
	public void Pause(){
	// Le�r�s: A met�dus megh�vja a SetStatus met�dust PAUSED param�terrel		
		SetStatus(Status.PAUSED);
	};	
	public void Stop(){
	// Le�r�s:  A met�dus megh�vja a SetStatus met�dust STOPPED param�terrel
		SetStatus(Status.RUNNING);
	};		
	private void SetStatus(Status NewStatus){
	// Le�r�s: �t�ll�tja a SimStatus attrib�tumot a param�terben megadott �rt�kre
		SimStatus = NewStatus;
	};	
	public void SetFrequency(int Frequency, String ElementID){
	// Le�r�s: A param�terben megadott azonos�t�j� GENERATOR objektum frekvenci�j�t m�dos�tja
		GENERATOR tmp;
		tmp = (GENERATOR) GetElementByID(ElementID);
		tmp.SetFrequency(Frequency);
	};
	public void SetSequence(int Sequence, String ElementID){
	// Le�r�s: A param�terben megadott azonos�t�j� GENERATOR objektum szekvenci�j�t m�dos�tja
		GENERATOR GEN_to_setsequence;	/* Tempor�lis v�ltoz� */
		GEN_to_setsequence = (GENERATOR) GetElementByID(ElementID);	/* GetElemetByIDvel megkapjuk, az objektumot	*/		
		GEN_to_setsequence.SetSequence(Sequence); 				 /* az gener�tor objektum SetSequence(...) met�dus�t megh�vjuk */
	
	};	
	public void Toggle(String ElementID){
	/* Le�r�s: A param�terben megadott azonos�t�j� SWITCH objektum �rt�k�t az ellenkez�re 
	 * �ll�tja az�ltal, hogy megh�vja az objektum hasonl� nev� param�ter�t
	*/
		SWITCH SWITCH_to_toggle;								/* Tempor�lis v�ltoz� */
		SWITCH_to_toggle = (SWITCH) GetElementByID(ElementID);	/* GetElemetByIDvel megkapjuk, majd  egyet	*/		
		SWITCH_to_toggle.Toggle();								/* az switch objektum Toggle() met�dus�t megh�vjuk */

	};	
	public void StepComponents(){
	// Le�r�s: Megh�vja az �sszes iComponent interf�szt megval�s�t� objektum Step() met�dus�t.

		/* Elvileg m�r fel van �p�lve a hierarchia �gy nekem el�g megkapnom a ComponentListet */
		DigitalObject obj;
		for(List<DigitalObject> sublist: ComponentList){
			for(DigitalObject o : sublist){
				obj = (DigitalObject) o;
				obj.Step();
			}
		}
	};
}
