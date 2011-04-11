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
	    boolean exists = (new File(strFilePath)).exists();
        if (!exists) {
        	//ParseFile(strFilePath);
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
		//System.out.print(file.getAbsolutePath());
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
				System.out.println("A Fajl tartalma\n");
				System.out.print(strFileContents);
			}
		}
		catch(FileNotFoundException e){
			System.out.println("File not found" + e);
		}
		catch(IOException ioe){
			System.out.println("Exception while reading the file "+ ioe);
		}
		
		/* ComponentList feltoltese
		 * 
		 * Feltetlezve, hogy a mintaillesztes mar megvolt
		 * tovabba az aritmetikai kifejezes is at lett alakitva 
		 * RPN alakra, sot meg jobb ha van egy tombunk a componensekkel
		 * es a wirelist
		 *
		 */
		
		/* Elkeszitem a WIre objektumokat, es a WireListet */
		List<DigitalObject> tmp_components = new ArrayList<DigitalObject>();
/*****************************************************************
 * ************** TESZTARAMKOROK leirasa manualisan **************	
******************************************************************/	

		if(file.getName() == "teszt1.bhdl"){
			// SWITCH letrehozasa
			SWITCH sw01 = new SWITCH("main", null);	
			tmp_components.add(sw01);
			
			// GENERATOR letrehozasa
			GENERATOR gen01 = new GENERATOR("main", 1000, 11001100, null);
			tmp_components.add(gen01);
			
			// LED letrehozasa
			LED led01 = new LED("main", null);
			tmp_components.add(led01);
			
			/* a kifejezes:  led01 = sw01 gen01 |
			 * olvassuk be; elerunk a "|"-ig. Tudjuk hogy az egy orkapu
			 * tehat letrehozunk ket wire-t a bemenetnek, egyet a kimenetnek
			 */
			Wire main_w01 = new Wire("main");
			WireList.add(main_w01);
			gen01.wireOut.add(main_w01);
			main_w01.SetConnection(null, gen01);	
			
			Wire main_w02 = new Wire("main");
			WireList.add(main_w02);
			sw01.wireOut.add(main_w02);
			main_w02.SetConnection(null, sw01);			
			
			// letrehozzuk a kaput
			ORGate main_ORGate_0= new ORGate("main", main_w01, main_w02);
			tmp_components.add(main_ORGate_0);			
			//beallitjuk a drotokat
			main_w01.SetConnection(main_ORGate_0,null);			
			main_w02.SetConnection(main_ORGate_0,null);
			
			// a kimentet meg sehova nem kotjuk
			Wire main_w03 = new Wire("main");
			main_ORGate_0.AddOutput(main_w03);
			main_w03.SetConnection(null, main_ORGate_0);
			WireList.add(main_w03);		
			
			main_w03.SetConnection(led01, null);
			led01.wireIn.add(main_w03);	
		}
		if(file.getName() == "teszt2.bhdl"){
			// SWITCH letrehozasa
			SWITCH sw01 = new SWITCH("main", null);	
			tmp_components.add(sw01);

			// Oscilloscope letrehozasa
			Oscilloscope osc01 = new Oscilloscope("main", null, 10);
			tmp_components.add(osc01);
			
			Wire main_w02 = new Wire("main");
			WireList.add(main_w02);	
			
			
			Wire main_w01 = new Wire("main");
			WireList.add(main_w01);
			sw01.wireOut.add(main_w01);
			main_w01.SetConnection(null, sw01);
			
			// letrehozzuk a kaput
			ANDGate main_ANDGate_0= new ANDGate("main", main_w01, main_w02);
			tmp_components.add(main_ANDGate_0);				
			
			//beallitjuk a drotokat
			main_w01.SetConnection(main_ANDGate_0, sw01);
			main_w02.SetConnection(main_ANDGate_0, main_ANDGate_0);			
			// a kimentet meg sehova nem kotjuk
			//main_w02.SetConnection(null, main_ANDGate_0);
			main_ANDGate_0.AddOutput(main_w02);			
			main_w02.SetConnection(osc01, null);
			osc01.wireIn.add(main_w02);				
		}	
		/*****************************************************************
		 * **************    TESZTARAMKOROK leirasanak vege  *************	
		******************************************************************/			
			/* Elso lepesben az osszes Input objektumot hozzaadjuk
			 * a Hierarchia 0. szintjehez
			 */			
			// 0. szint letrehozasa 
			ComponentList.add(new ArrayList<DigitalObject>());
			for(DigitalObject obj:tmp_components){	
				if(obj.GetType().equalsIgnoreCase("SWITCH") || obj.GetType().equalsIgnoreCase("GENERATOR")){
					/* Hozzadjuk a szinthez az elemet */
					ComponentList.get(0).add(obj);											
				}
			}
			/* Eltavolitjuk a listabol a mar hierarchikus elemeket */
			tmp_components.removeAll(ComponentList.get(0));
			
			/* Amig van elem a listaban */
		
			int iHierarchy = 0;
			while(!tmp_components.isEmpty()){
				List<DigitalObject> components_next = new ArrayList<DigitalObject>();
				// Vegigmegyunk az aktualis szint osszes elemen
				for(DigitalObject obj:ComponentList.get(iHierarchy)){
					//az aktualis szint aktualis elemenek kimenetein
					for(Wire out:obj.wireOut){
						/* es az aktualis szint aktualis elemenek 
						 * kimeno drotjai altal meghatarozott elemet hozzaadjuk 
						 * a kovetkezo elemekhez
						 */
						for(DigitalObject wire_obj:out.objectsOut){
							/* Itt meg kell nezni hogy korabi szinten szerepelt-e mar */
							boolean contain = true;
							for(int i=0; i<=iHierarchy;i++){
								contain =ComponentList.get(i).contains(wire_obj) ;
							}
							// ha nem volt meg korabbi szinten akkor most uj szinthez adjuk
							if(!contain && !components_next.contains(wire_obj))
								components_next.add(wire_obj);								
						}
					}													
				}
				tmp_components.removeAll(components_next);
				ComponentList.add(new ArrayList<DigitalObject>());
				ComponentList.get(++iHierarchy).addAll(components_next);
			}		
	
		/* KIIRATAS, DEBUG */
		int szint=0;
		
		for(List<DigitalObject> sublist: ComponentList){
			System.out.println();
			System.out.print("Szint: ");
			System.out.print(szint++);
			System.out.print("\t");
			for(DigitalObject o : sublist){					
				System.out.print(o.GetID()+ ", ");
			}
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
