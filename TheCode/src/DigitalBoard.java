/*
* Nev: 			DigitalBoard
* Tipus: 		Class
* Interfacek:	---
* Szulok		---
* 
*********** Leiras **********
* A digitalis aramkort nyilvantarto es a vezerlest  biztosito objektum.
* Az aramkor osszes elemet es * a koztuk levo kapcsolatokat letrehozza,
* kezeli es tarolja. uj aramkor megnyitasakor betolti az aramkort egy
* fajlbol – ekozben ellenorzi a szintaktikai helyesseget, 
* letrehoz minden digitalis elemet, hierarchia szerint sorrendezi,
* felfedezi a visszacsatolasokat. 
* Tovabbi feladata az aramkor szamitasait vezerelni, 
* igy ha a felhasznalo leptetest ker, a generatorokat lepteti, 
* es ujraszamolja az aramkor komponenseinek allapotat. 
* Ha a felhasznalo a futtatast valasztja, valtoztathato idokozonkent 
* lepteti a jelgeneratort, es a komponensek ertekeit ujraszamolja.

*/
/*  IMPORTOK  */
import java.util.*;	// List, ArrayList-hez

public class DigitalBoard {
	/*	ATTRIBuTUMOK	*/
	private Status SimStatus; 
	private String ID;
	private static int DigitalBoardCounts;

	// Leiras: Haromallapotu valtozo, amely a szimulacio aktualis allapotat tarolja 

	private ArrayList< List<iComponent> > ComponentList;
	/* Leiras: Ez az attributum tarolja az osszes kaput, kimenetet, bemenetet
	 * hierarchikus sorrendben
	 * Ez nem mas, mint egy listabol szervezett tomb. A tomb indexe
	 * azonositja a hierarchia szintet 
	 * (0-Forrasok, 1-a forrasokhoz csatlakozo elemek, stb)
	 * az egyes szinteken pedig egy lista van az elemekrol
	*/	
	
	private List<Wire> WireList;
	// Leiras: Egyszeru lista a Wire objektumokbol

	/*  KONSTRUKTOR  */ 
	public DigitalBoard(){		
		SimStatus = Status.STOPPED;
		ComponentList = new ArrayList<List<iComponent>>();
		WireList = new ArrayList<Wire>();	
		ID = "DB" + String.valueOf(DigitalBoardCounts++);
	}
	/*	METoDUSOK	*/
	public iComponent GetElementByID(String ElementID){
	// Leiras: Megkeres egy adott elemet a 	ComponentList illetve a WireList listakban
		_TEST stack = new _TEST();	
		/* TEST */
		stack.PrintHeader(ID,"ElementID:String", ElementID+":iComponent");	/* TEST */
		stack.PrintTail(ID,"ElementID:String", ElementID+":iComponent"); 	/* TEST */
		return null;		
	};
	
	public void LoadBoard(String strFilePath){
	// Leiras: A megfelelo parameterrel meghivja a ParseFile(String strFilePath) metodust.
		_TEST stack = new _TEST();						/* TEST */
		stack.PrintHeader(ID,"strFilePath:String", "");	/* TEST */
		ParseFile(strFilePath);
		stack.PrintTail(ID,"strFilePath:String", ""); 		/* TEST */

	};

	public void ParseFile(String strFilePath){
	// Leiras: A megadott utvonalon talalhato fajlt olvassa be es soronkent ertelmezi az allomanyt
		_TEST stack = new _TEST();						/* TEST */
		stack.PrintHeader(ID,"strFilePath:String", "");	/* TEST */
		switch(Integer.decode(strFilePath)){
		case 0:		
			/*
			* ********************************
			* *******  Egyszeru aramkor ******
			* ********************************
			*  led0 = sw0 + gen0
			*/		
			//osszekoto wire letrehozasa		
			Wire sw0_or0 = new Wire();	
			Wire gen0_or0 = new Wire();	
			Wire or0_led0 = new Wire();	
			// egy switch , generator egy Or kapu ill egy led letrehozasa, konstruktorukban a wire-rel
			SWITCH sw0 = new SWITCH(sw0_or0);
			GENERATOR gen0 = new GENERATOR(3,5,gen0_or0);
			ORGate or0 = new ORGate(sw0_or0,gen0_or0);
			LED led0 = new LED(or0_led0);
			or0.AddOutput(or0_led0);
			// Hozzaadjuk a hierarchiahoz
			ComponentList.add(new ArrayList<iComponent>());	//0. szint letrehozasa
			ComponentList.get(0).add(sw0);
			ComponentList.get(0).add(gen0);
			ComponentList.add(new ArrayList<iComponent>()); //1 szint letrehozasa
			ComponentList.get(1).add(or0);
			ComponentList.add(new ArrayList<iComponent>()); // 2. szint letrehozasa
			ComponentList.get(2).add(led0);
			// hozzaadjuk a Wireket
			WireList.add(sw0_or0);
			WireList.add(gen0_or0);
			WireList.add(or0_led0);
			break;
			
		case 1:
			/*
			* *****************************************
			* *******  aramkor visszacsatolassal ******
			* *****************************************
			* and0_and0 = sw01 & and0_and0
			* led01 = and0_and0
			*/	
			//sw0:Switch, and0:andGate, led0:LED, sw0_and0:Wire, and0_and0:Wire,and0_led0:Wire
			Wire sw01_and0 = new Wire();	
			Wire and0_and0 = new Wire();
			Wire and0_led01 = new Wire();
			
			SWITCH sw01 = new SWITCH(sw01_and0);
			ANDGate and0 = new ANDGate(and0_and0,sw01_and0);
			LED led01 = new LED(and0_led01);
			
			and0.AddOutput(and0_and0);
			and0.AddOutput(and0_led01);
			
			and0.AddToFeedbacks(and0);
			// Hozzaadjuk a hierarchiahoz
			ComponentList.add(new ArrayList<iComponent>());
			ComponentList.get(0).add(sw01);
			ComponentList.add(new ArrayList<iComponent>());
			ComponentList.get(1).add(and0);
			ComponentList.add(new ArrayList<iComponent>());
			ComponentList.get(2).add(led01);

			// hozzaadjuk a Wireket
			WireList.add(sw01_and0);
			WireList.add(and0_and0);
			WireList.add(and0_led01);
			break;
		case 2:
			/* ********************************
			* *******  Instabil aramkor ******
			* ********************************
			* inv0_and2 = !sw2 & inv0_and2
			* led3 = inv0_and2
			*/		
			//osszekoto wire letrehozasa		
			Wire sw2_and2 = new Wire();	
			Wire and2_inv0 = new Wire();	
			Wire inv0_and2 = new Wire();
			Wire inv0_led3 = new Wire();
			// egy switch , inverter egy AND kapu ill egy led letrehozasa, konstruktorukban a wire-rel
			SWITCH sw2 = new SWITCH(sw2_and2);
			INVERTER inv0 = new INVERTER(and2_inv0);
			ANDGate and2 = new ANDGate(sw2_and2,inv0_and2);
			LED led3 = new LED(inv0_led3);
			
			and2.AddOutput(and2_inv0);
			inv0.AddOutput(inv0_and2);
			// Feedbacks
			and2.AddToFeedbacks(and2);
			and2.AddToFeedbacks(inv0);
			// Hozzaadjuk a hierarchiahoz
			ComponentList.add(new ArrayList<iComponent>());	//0. szint letrehozasa
			ComponentList.get(0).add(sw2);
			ComponentList.add(new ArrayList<iComponent>()); //1 szint letrehozasa
			ComponentList.get(1).add(and2);
			ComponentList.add(new ArrayList<iComponent>()); // 2. szint letrehozasa
			ComponentList.get(2).add(inv0);
			ComponentList.add(new ArrayList<iComponent>()); // 3. szint letrehozasa
			ComponentList.get(2).add(led3);
			// hozzaadjuk a Wireket
			WireList.add(sw2_and2);
			WireList.add(and2_inv0);
			WireList.add(inv0_and2);
			WireList.add(inv0_led3);
			break;
		}
		stack.PrintTail(ID,"strFilePAth:String","");
	};	
	public void Run(){
	// Leiras: metodus meghivja a SetStatus metodust RUNING parameterrel	
		_TEST stack = new _TEST();		/* TEST */
		stack.PrintHeader(ID,"", "");	/* TEST */
		
		SetStatus(Status.RUNNING);
		
		stack.PrintTail(ID,"", ""); 	/* TEST */
		
	};
	public void Pause(){
	// Leiras: A metodus meghivja a SetStatus metodust PAUSED parameterrel		
		_TEST stack = new _TEST();		/* TEST */
		stack.PrintHeader(ID,"", "");	/* TEST */
		
		SetStatus(Status.PAUSED);
		
		stack.PrintTail(ID,"", ""); 	/* TEST */
	};	
	public void Stop(){
	// Leiras:  A metodus meghivja a SetStatus metodust STOPPED parameterrel
		_TEST stack = new _TEST();		/* TEST */
		stack.PrintHeader(ID,"", "");	/* TEST */

		SetStatus(Status.RUNNING);
		
		stack.PrintTail(ID,"", ""); 	/* TEST */
	};		
	private void SetStatus(Status NewStatus){
	// Leiras: atallitja a SimStatus attributumot a parameterben megadott ertekre
		_TEST stack = new _TEST();		// A Stackbol kinyert adatokat tartalmazza
		stack.PrintHeader(ID,NewStatus+":Status","");
		// TODO: Ha vannak fuggvenyhivasok, el kell helyezni ide oket!
		// ...
		stack.PrintTail(ID,"","");
		// TODO: Ha van visszateresi ertek, ide kell irni!
		//return null;
	};	
	public void SetFrequency(int Frequency, String ElementID){
	// Leiras: A parameterben megadott azonositoju GENERATOR objektum frekvenciajat modositja
		_TEST stack = new _TEST();		// A Stackbol kinyert adatokat tartalmazza
		stack.PrintHeader(ID,"","");
		// TODO: Ha vannak fuggvenyhivasok, el kell helyezni ide oket!
		// ...
		stack.PrintTail(ID,"","");
		// TODO: Ha van visszateresi ertek, ide kell irni!
		//return null;
	};
	public void SetSequence(int Sequence, String ElementID){
	// Leiras: A parameterben megadott azonositoju GENERATOR objektum szekvenciajat modositja
		_TEST stack = new _TEST();								 /* TEST */
		stack.PrintHeader(ID,"Sequence:int, ElementID:String","");	 /* TEST */
		GENERATOR GEN_to_setsequence;	
		GEN_to_setsequence = new GENERATOR(0,0,null);			/* Temporalis valtozo */
		GetElementByID(GEN_to_setsequence.ID);					/* GetElemetByIDvel megkapjuk, az objektumot	*/		
		GEN_to_setsequence.SetSequence(Sequence); 				 /* az generator objektum SetSequence(...) metodusat meghivjuk */
		stack.PrintTail(ID,"Sequence:int, ElementID:String","");									 /* TEST */
	
	};	
	public void Toggle(String ElementID){
	/* Leiras: A parameterben megadott azonositoju SWITCH objektum erteket az ellenkezore 
	 * allitja azaltal, hogy meghivja az objektum hasonlo nevu parameteret
	*/
		_TEST stack = new _TEST();								/* TEST */
		stack.PrintHeader(ID,"ElementID:String","");			/* TEST */
		SWITCH SWITCH_to_toggle = new SWITCH(null);				/* Temporalis valtozo */
		GetElementByID(SWITCH_to_toggle.ID);					/* GetElemetByIDvel megkapjuk, majd  egyet	*/		
		SWITCH_to_toggle.Toggle();								/* az switch objektum Toggle() metodusat meghivjuk */
		stack.PrintTail(ID,"","");								/* TEST */
	};	
	public void StepComponents(int TestArg){
	// Leiras: Meghivja az osszes iComponent interfeszt megvalosito objektum Step() metodusat.
		_TEST stack = new _TEST();		/*TEST*/
		stack.PrintHeader(ID,"","");	/*TEST*/
		/* Elvileg mar fel van epulve a hierarchia igy nekem eleg megkapnom a ComponentListet */
		switch(TestArg){
		case 0:
			/*DigitalObject obj;
			List<iComponent> sublist;
			for(List<iComponent> sublist: ComponentList)
				for(iComponent o : sublist){
					obj = (DigitalObject) o;
					//System.out.println(obj.ID); // DEBUG
					obj.Step();
				}*/
			ComponentList.get(0).get(0).Step();
			ComponentList.get(0).get(1).Step();
			ComponentList.get(1).get(0).Step();
			ComponentList.get(2).get(0).Step();
			break;
		case 1:
			ComponentList.get(0).get(0).Step();
			ComponentList.get(1).get(0).Step();
			ComponentList.get(2).get(0).Step();
			break;
		case 2:
			ComponentList.get(0).get(0).Step();
			ComponentList.get(1).get(0).Step();
			ComponentList.get(2).get(0).Step();
			ComponentList.get(2).get(1).Step();
			break;
		}
		stack.PrintTail(ID,"","");		/*TEST*/
	};
}
