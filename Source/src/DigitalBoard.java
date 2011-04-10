/*  IMPORTOK  */
import java.io.*;
import java.util.*;	// List, ArrayList-hez
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
	/*	ATTRIBUTUMOK	*/
	
	/**
	 * Haromallapotu valtozo, amely a szimulacio aktualis allapotat tarolja 
	 */
	private Status SimStatus; 

	/**
	 * Ez az attributum tarolja az osszes kaput, kimenetet, bemenetet
	 * hierarchikus sorrendben
	 * Ez nem mas, mint egy listabol szervezett tomb. A tomb indexe
	 * azonositja a hierarchia szintet 
	 * (0-Forrasok, 1-a forrasokhoz csatlakozo elemek, stb)
	 * az egyes szinteken pedig egy lista van az elemekrol
	 */
	private ArrayList< List<DigitalObject> > ComponentList;

	/**
	 * Egyszeru lista a Wire objektumokbol
	 */
	private List<Wire> WireList;


	/**  KONSTRUKTOR  */ 
	public DigitalBoard(){		
		SimStatus = Status.STOPPED;
		ComponentList = new ArrayList<List<DigitalObject>>();
		WireList = new ArrayList<Wire>();	
		//Composit MainComposit = new Composit("main");
	}
	
	
	/*	METODUSOK	*/	
	/**
	 * Megkeres egy adott elemet egy Composit ComponentList listajaban
	 */
	public DigitalObject GetElementByID(String ElementID){
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

	/**
	 * A megfelelo parameterrel meghivja a ParseFile(String strFilePath) metodust.
	 * @param strFilePath Az aramkort leiro dokumentum (*.bhdl) eleresi utvonala
	 * @throws FileDoesNotExistException ha a fajl nem letezik vagy nem olvashato
	 */
	public void LoadBoard(String strFilePath){
		
		/////////////////csomák teszt részéhez//////////////////////////
		if(strFilePath.equals("0")){
			//osszekoto wire letrehozasa		
			Wire sw0_or0 = new Wire("v0");	
			Wire gen0_or0 = new Wire("v1");	
			Wire or0_led0 = new Wire("v2");	
			// egy switch , generator egy Or kapu ill egy led letrehozasa, konstruktorukban a wire-rel
			SWITCH sw0 = new SWITCH("sw0",sw0_or0);
			GENERATOR gen0 = new GENERATOR("gen0",3,5,gen0_or0);
			ORGate or0 = new ORGate("or0",sw0_or0,gen0_or0);
			LED led0 = new LED("led0",or0_led0);
			or0.AddOutput(or0_led0);
			// Hozzaadjuk a hierarchiahoz
			ComponentList.add(new ArrayList<DigitalObject>());	//0. szint letrehozasa
			ComponentList.get(0).add(sw0);
			ComponentList.get(0).add(gen0);
			ComponentList.add(new ArrayList<DigitalObject>()); //1 szint letrehozasa
			ComponentList.get(0).add(or0);
			ComponentList.add(new ArrayList<DigitalObject>()); // 2. szint letrehozasa
			ComponentList.get(0).add(led0);
			// hozzaadjuk a Wireket
			WireList.add(sw0_or0);
			WireList.add(gen0_or0);
			WireList.add(or0_led0);
			//összeköttetések:
			sw0_or0.objectsOut.add(or0);
			gen0_or0.objectsOut.add(or0);
			or0_led0.objectsOut.add(led0);
			System.out.println("now calling hierbuilder");//csomák teszt
			HierarchyCounter cntr=new HierarchyCounter();
			cntr.CountHierarchy(WireList, ComponentList);

		}
		///////////////////////csomák end/////////////////////////////////////////
		
	    boolean exists = (new File(strFilePath)).exists();
        if (!exists) {
        	// throw FileDoesNotExistException;
        } else {
            ParseFile(strFilePath);
        }
    };

    /**
     * A megadott utvonalon talalhato fajlt olvassa be es soronkent ertelmezi az allomanyt
     * @param strFilePath Az aramkort leiro dokumentum (*.bhdl) eleresi utvonala
     */
	public void ParseFile(String strFilePath){
		File file = new File(strFilePath);
        BufferedInputStream bin = null;


		try
		{
			//FileInputStream object letrehozasa
			FileInputStream fin = new FileInputStream(file);
			//BufferedInputStream obkejtum letrehozasa a beolvasashoz
			bin = new BufferedInputStream(fin);
			// byte tomb letrehozasa, ebbe olvasunk be majd.
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

    /**
     * metodus meghivja a SetStatus metodust RUNING parameterrel
     */
	public void Run(){
		SetStatus(Status.RUNNING);	
	};
	
	/**
	 * A metodus meghivja a SetStatus metodust PAUSED parameterrel		
	 */
	public void Pause(){
		SetStatus(Status.PAUSED);
	};	
	
	/**
	 * A metodus meghivja a SetStatus metodust STOPPED parameterrel
	 */
	public void Stop(){  
		SetStatus(Status.RUNNING);
	};
	
	/**
	 * Atallitja a SimStatus attributumot a parameterben megadott ertekre
	 * @param NewStatus Az uj allapot
	 */
	private void SetStatus(Status NewStatus){
		SimStatus = NewStatus;
	};	
	
	/**
	 * A parameterben megadott azonositoju GENERATOR objektum frekvenciajat modositja
	 * @param Frequency Az uj frekvencia
	 * @param ElementID A modositani kivant GENERATOR IDja
	 */
	public void SetFrequency(int Frequency, String ElementID){
		GENERATOR tmp;
		tmp = (GENERATOR) GetElementByID(ElementID);
		tmp.SetFrequency(Frequency);
	};
	
	/**
	 * A parameterben megadott azonositoju GENERATOR objektum szekvenciajat modositja
	 * @param Sequence az uj szekvencia, minta
	 * @param ElementID A modositani kivant GENERATOR IDja
	 */
	public void SetSequence(int Sequence, String ElementID){
		GENERATOR GEN_to_setsequence;	/* Temporalis valtozo */
		GEN_to_setsequence = (GENERATOR) GetElementByID(ElementID);	/* GetElemetByIDvel megkapjuk, az objektumot	*/		
		GEN_to_setsequence.SetSequence(Sequence); 				 /* az generator objektum SetSequence(...) metodusat meghivjuk */
	
	};	
	
	/**
	 * A parameterben megadott azonositoju SWITCH objektum erteket az ellenkezore 
	 * allitja azaltal, hogy meghivja az objektum hasonlo nevu parameteret
	 * @param ElementID A SWITCH ID-ja
	 */
	public void Toggle(String ElementID){
		SWITCH SWITCH_to_toggle;								/* Temporalis valtozo */
		SWITCH_to_toggle = (SWITCH) GetElementByID(ElementID);	/* GetElemetByIDvel megkapjuk, majd  egyet	*/		
		SWITCH_to_toggle.Toggle();								/* az switch objektum Toggle() metodusat meghivjuk */

	};	
	
	/**
	 * Meghivja az osszes iComponent interfeszt megvalosito objektum Step() metodusat.
	 */
	public void StepComponents(){
		/* Elvileg mar fel van epulve a hierarchia igy nekem eleg megkapnom a ComponentListet */
		DigitalObject obj;
		for(List<DigitalObject> sublist: ComponentList){
			for(DigitalObject o : sublist){
				obj = (DigitalObject) o;
				obj.Step();
			}
		}
	};
}
