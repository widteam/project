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
/*  IMPORTOK  */
import java.util.*;	// List, ArrayList-hez

public class DigitalBoard {
	/*	ATTRIBÚTUMOK	*/
	private Status SimStatus; 
	private String ID;
	private static int DigitalBoardCounts;

	// Leírás: Háromállapotú változó, amely a szimuláció aktuális állapotát tárolja 

	private ArrayList< List<iComponent> > ComponentList;
	/* Leírás: Ez az attribútum tárolja az összes kaput, kimenetet, bemenetet
	 * hierarchikus sorrendben
	 * Ez nem más, mint egy listából szervezett tömb. A tömb indexe
	 * azonosítja a hierarchia szintet 
	 * (0-Források, 1-a forrásokhoz csatlakozó elemek, stb)
	 * az egyes szinteken pedig egy lista van az elemekrõl
	*/	
	
	private List<Wire> WireList;
	// Leírás: Egyszerû lista a Wire objektumokból

	/*  KONSTRUKTOR  */ 
	public DigitalBoard(){		
		SimStatus = Status.STOPPED;
		ComponentList = new ArrayList<List<iComponent>>();
		WireList = new ArrayList<Wire>();	
		ID = "DB" + String.valueOf(DigitalBoardCounts++);
	}
	/*	METÓDUSOK	*/
	public iComponent GetElementByID(String ElementID){
	// Leírás: Megkeres egy adott elemet a 	ComponentList illetve a WireList listákban
		_TEST stack = new _TEST();	
		/* TEST */
		stack.PrintHeader(ID,"ElementID:String", ElementID+":iComponent");	/* TEST */
		stack.PrintTail(ID,"ElementID:String", ElementID+":iComponent"); 	/* TEST */
		return null;		
	};
	
	public void LoadBoard(String strFilePath){
	// Leírás: A megfelelõ paraméterrel meghívja a ParseFile(String strFilePath) metódust.
		_TEST stack = new _TEST();						/* TEST */
		stack.PrintHeader(ID,"strFilePath:String", "");	/* TEST */
		ParseFile(strFilePath);
		stack.PrintTail(ID,"strFilePath:String", ""); 		/* TEST */

	};

	public void ParseFile(String strFilePath){
	// Leírás: A megadott útvonalon található fájlt olvassa be és soronként értelmezi az állományt
		_TEST stack = new _TEST();						/* TEST */
		stack.PrintHeader(ID,"strFilePath:String", "");	/* TEST */
		switch(Integer.decode(strFilePath)){
		case 0:		
			/*
			* ********************************
			* *******  Egyszerû áramkör ******
			* ********************************
			*  led0 = sw0 + gen0
			*/		
			//Összekötõ wire létrehozása		
			Wire sw0_or0 = new Wire();	
			Wire gen0_or0 = new Wire();	
			Wire or0_led0 = new Wire();	
			// egy switch , generátor egy Or kapu ill egy led létrehozása, konstruktorukban a wire-rel
			SWITCH sw0 = new SWITCH(sw0_or0);
			GENERATOR gen0 = new GENERATOR(3,5,gen0_or0);
			ORGate or0 = new ORGate(sw0_or0,gen0_or0);
			LED led0 = new LED(or0_led0);
			or0.AddOutput(or0_led0);
			// Hozzáadjuk a hierarchiához
			ComponentList.add(new ArrayList<iComponent>());	//0. szint létrehozása
			ComponentList.get(0).add(sw0);
			ComponentList.get(0).add(gen0);
			ComponentList.add(new ArrayList<iComponent>()); //1 szint létrehozása
			ComponentList.get(1).add(or0);
			ComponentList.add(new ArrayList<iComponent>()); // 2. szint létrehozása
			ComponentList.get(2).add(led0);
			// hozzáadjuk a Wireket
			WireList.add(sw0_or0);
			WireList.add(gen0_or0);
			WireList.add(or0_led0);
			break;
			
		case 1:
			/*
			* *****************************************
			* *******  áramkör visszacsatolással ******
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
			// Hozzáadjuk a hierarchiához
			ComponentList.add(new ArrayList<iComponent>());
			ComponentList.get(0).add(sw01);
			ComponentList.add(new ArrayList<iComponent>());
			ComponentList.get(1).add(and0);
			ComponentList.add(new ArrayList<iComponent>());
			ComponentList.get(2).add(led01);

			// hozzáadjuk a Wireket
			WireList.add(sw01_and0);
			WireList.add(and0_and0);
			WireList.add(and0_led01);
			break;
		case 2:
			/* ********************************
			* *******  Instabil áramkör ******
			* ********************************
			* inv0_and2 = !sw2 & inv0_and2
			* led3 = inv0_and2
			*/		
			//Összekötõ wire létrehozása		
			Wire sw2_and2 = new Wire();	
			Wire and2_inv0 = new Wire();	
			Wire inv0_and2 = new Wire();
			Wire inv0_led3 = new Wire();
			// egy switch , inverter egy AND kapu ill egy led létrehozása, konstruktorukban a wire-rel
			SWITCH sw2 = new SWITCH(sw2_and2);
			INVERTER inv0 = new INVERTER(and2_inv0);
			ANDGate and2 = new ANDGate(sw2_and2,inv0_and2);
			LED led3 = new LED(inv0_led3);
			
			and2.AddOutput(and2_inv0);
			inv0.AddOutput(inv0_and2);
			// Feedbacks
			and2.AddToFeedbacks(and2);
			and2.AddToFeedbacks(inv0);
			// Hozzáadjuk a hierarchiához
			ComponentList.add(new ArrayList<iComponent>());	//0. szint létrehozása
			ComponentList.get(0).add(sw2);
			ComponentList.add(new ArrayList<iComponent>()); //1 szint létrehozása
			ComponentList.get(1).add(and2);
			ComponentList.add(new ArrayList<iComponent>()); // 2. szint létrehozása
			ComponentList.get(2).add(inv0);
			ComponentList.add(new ArrayList<iComponent>()); // 3. szint létrehozása
			ComponentList.get(2).add(led3);
			// hozzáadjuk a Wireket
			WireList.add(sw2_and2);
			WireList.add(and2_inv0);
			WireList.add(inv0_and2);
			WireList.add(inv0_led3);
			break;
		}
		stack.PrintTail(ID,"strFilePAth:String","");
	};	
	public void Run(){
	// Leírás: metódus meghívja a SetStatus metódust RUNING paraméterrel	
		_TEST stack = new _TEST();		/* TEST */
		stack.PrintHeader(ID,"", "");	/* TEST */
		
		SetStatus(Status.RUNNING);
		
		stack.PrintTail(ID,"", ""); 	/* TEST */
		
	};
	public void Pause(){
	// Leírás: A metódus meghívja a SetStatus metódust PAUSED paraméterrel		
		_TEST stack = new _TEST();		/* TEST */
		stack.PrintHeader(ID,"", "");	/* TEST */
		
		SetStatus(Status.PAUSED);
		
		stack.PrintTail(ID,"", ""); 	/* TEST */
	};	
	public void Stop(){
	// Leírás:  A metódus meghívja a SetStatus metódust STOPPED paraméterrel
		_TEST stack = new _TEST();		/* TEST */
		stack.PrintHeader(ID,"", "");	/* TEST */

		SetStatus(Status.RUNNING);
		
		stack.PrintTail(ID,"", ""); 	/* TEST */
	};		
	private void SetStatus(Status NewStatus){
	// Leírás: Átállítja a SimStatus attribútumot a paraméterben megadott értékre
		_TEST stack = new _TEST();		// A Stackbõl kinyert adatokat tartalmazza
		stack.PrintHeader(ID,NewStatus+":Status","");
		// TODO: Ha vannak függvényhívások, el kell helyezni ide õket!
		// ...
		stack.PrintTail(ID,"","");
		// TODO: Ha van visszatérési érték, ide kell írni!
		//return null;
	};	
	public void SetFrequency(int Frequency, String ElementID){
	// Leírás: A paraméterben megadott azonosítójú GENERATOR objektum frekvenciáját módosítja
		_TEST stack = new _TEST();		// A Stackbõl kinyert adatokat tartalmazza
		stack.PrintHeader(ID,"","");
		// TODO: Ha vannak függvényhívások, el kell helyezni ide õket!
		// ...
		stack.PrintTail(ID,"","");
		// TODO: Ha van visszatérési érték, ide kell írni!
		//return null;
	};
	public void SetSequence(int Sequence, String ElementID){
	// Leírás: A paraméterben megadott azonosítójú GENERATOR objektum szekvenciáját módosítja
		_TEST stack = new _TEST();								 /* TEST */
		stack.PrintHeader(ID,"Sequence:int, ElementID:String","");	 /* TEST */
		GENERATOR GEN_to_setsequence;	
		GEN_to_setsequence = new GENERATOR(0,0,null);			/* Temporális változó */
		GetElementByID(GEN_to_setsequence.ID);					/* GetElemetByIDvel megkapjuk, az objektumot	*/		
		GEN_to_setsequence.SetSequence(Sequence); 				 /* az generátor objektum SetSequence(...) metódusát meghívjuk */
		stack.PrintTail(ID,"Sequence:int, ElementID:String","");									 /* TEST */
	
	};	
	public void Toggle(String ElementID){
	/* Leírás: A paraméterben megadott azonosítójú SWITCH objektum értékét az ellenkezõre 
	 * állítja azáltal, hogy meghívja az objektum hasonló nevû paraméterét
	*/
		_TEST stack = new _TEST();								/* TEST */
		stack.PrintHeader(ID,"ElementID:String","");			/* TEST */
		SWITCH SWITCH_to_toggle = new SWITCH(null);				/* Temporális változó */
		GetElementByID(SWITCH_to_toggle.ID);					/* GetElemetByIDvel megkapjuk, majd  egyet	*/		
		SWITCH_to_toggle.Toggle();								/* az switch objektum Toggle() metódusát meghívjuk */
		stack.PrintTail(ID,"","");								/* TEST */
	};	
	public void StepComponents(int TestArg){
	// Leírás: Meghívja az összes iComponent interfészt megvalósító objektum Step() metódusát.
		_TEST stack = new _TEST();		/*TEST*/
		stack.PrintHeader(ID,"","");	/*TEST*/
		/* Elvileg már fel van épülve a hierarchia így nekem elég megkapnom a ComponentListet */
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
