/*  IMPORTOK  */
//import java.io.BufferedInputStream;
import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
	 * Ez az attributum tarolja az osszes kaput, kimenetet, bemenetet
	 * hierarchikus sorrendben Ez nem mas, mint egy listabol szervezett tomb. A
	 * tomb indexe azonositja a hierarchia szintet (0-Forrasok, 1-a forrasokhoz
	 * csatlakozo elemek, stb) az egyes szinteken pedig egy lista van az
	 * elemekrol
	 */
	private ArrayList<List<DigitalObject>> ComponentList;

	/**
	 * Egyszeru lista a Wire objektumokbol
	 */
	private List<Wire> WireList;

	/** KONSTRUKTOR */
	public DigitalBoard() {
		SimStatus = Status.STOPPED;
		ComponentList = new ArrayList<List<DigitalObject>>();
		WireList = new ArrayList<Wire>();
		// Composit MainComposit = new Composit("main");
	}

	/* METODUSOK */
	/**
	 * Megkeres egy adott elemet egy Composit ComponentList listajaban
	 */
	public DigitalObject GetElementByID(String ElementID) {
		if (ComponentList != null && !ComponentList.isEmpty()) {
			for (List<DigitalObject> sublist : ComponentList) {
				for (DigitalObject o : sublist) {
					if (o.ID == ElementID)
						return (DigitalObject) o;
				}
			}
		}
		return null;
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
		List<DigitalObject> elementlist = new ArrayList<DigitalObject>();
		// System.out.print(file.getAbsolutePath());
//		BufferedInputStream bin = null;
//		String strFileContents="";
//		try {
//			// FileInputStream object letrehozasa
//			FileInputStream fin = new FileInputStream(file);
//			// BufferedInputStream obkejtum letrehozasa a beolvasashoz
//			bin = new BufferedInputStream(fin);
//			// byte tomb letrehozasa, ebbe olvasunk be majd.
//			byte[] contents = new byte[1024];
//			int bytesRead = 0;
//			
//			while ((bytesRead = bin.read(contents)) != -1) {
//				strFileContents = new String(contents, 0, bytesRead);
//				System.out.print(strFileContents);
//			}
//		} catch (FileNotFoundException e) {
//			System.out.println("File not found" + e);
//		} catch (IOException ioe) {
//			System.out.println("Exception while reading the file " + ioe);
//		}
//		
//		// Tisztogatas
//		
//		strFileContents = bhdlParser.remove_Spaces(strFileContents);
//		strFileContents = bhdlParser.remove_CR_LF(strFileContents); //nincs sortores
//		
//		/*
//		 * MAIN composit megkeresese letrehozasa
//		 */
//		ComponentList.add(new ArrayList<DigitalObject>());
//		String main_composit = bhdlParser.FindMainComposite(strFileContents);
//		Composit main = bhdlParser.CreateMain(main_composit);
//		String[] main_commands = bhdlParser.getCommands(main_composit);
//		bhdlParser.CommandParser(main, main_commands, ComponentList.get(0));
//		strFileContents = strFileContents.replace(main_composit,"");
//		
		
		/*while(strFileContents!=null && !strFileContents.trim().isEmpty() ){
			String[] CompositCommands = null;	// Egy kompozitvban levo utasitasok
			Composit myComposit;	// az aktualis kompozit			
			String composit = bhdlParser.find_next_Composite(strFileContents);
			
			myComposit = bhdlParser.CreateComposit(composit);
			CompositCommands = bhdlParser.getCommands(composit);
			bhdlParser.CommandParser(myComposit, CompositCommands, elementlist);
			
			
			strFileContents = strFileContents.replace(composit,"");
		}
		*/

		
		/*
		 * ComponentList feltoltese
		 * 
		 * Feltetlezve, hogy a mintaillesztes mar megvolt tovabba az aritmetikai
		 * kifejezes is at lett alakitva RPN alakra, sot meg jobb ha van egy
		 * tombunk a componensekkel es a wirelist
		 */

		/* Elkeszitem a WIre objektumokat, es a WireListet */
		List<DigitalObject> tmp_components = new ArrayList<DigitalObject>();
		/*****************************************************************
		 * ************** TESZTARAMKOROK leirasa manualisan **************
		 ******************************************************************/

		if (file.getName() == "teszt1.bhdl") {
			tmp_components = elementlist;
		}
		if (file.getName() == "teszt2.bhdl") {
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
			ANDGate main_ANDGate_0 = new ANDGate("main", main_w01, main_w02);
			tmp_components.add(main_ANDGate_0);

			// beallitjuk a drotokat
			main_w01.SetConnection(main_ANDGate_0, sw01);
			main_w02.SetConnection(main_ANDGate_0, main_ANDGate_0);
			// a kimentet meg sehova nem kotjuk
			// main_w02.SetConnection(null, main_ANDGate_0);
			main_ANDGate_0.AddOutput(main_w02);
			main_w02.SetConnection(osc01, null);
			osc01.wireIn.add(main_w02);
		}
		if (file.getName() == "teszt4.bhdl") {
			// SWITCH letrehozasa
			SWITCH sw01 = new SWITCH("main", null);
			tmp_components.add(sw01);
			Wire main_w1 = new Wire("main");
			WireList.add(main_w1);
			main_w1.SetConnection(null, sw01);
			sw01.wireOut.add(main_w1);

			// GENERATOR
			GENERATOR gen01 = new GENERATOR("main", 1000, 11001100);
			tmp_components.add(gen01);
			Wire main_w2 = new Wire("main");
			WireList.add(main_w2);
			main_w2.SetConnection(null, gen01);
			gen01.wireOut.add(main_w2);

			// LED
			LED led01 = new LED("main", null);
			tmp_components.add(led01);

			// Oscilloscope letrehozasa
			Oscilloscope osc01 = new Oscilloscope("main", null, 10);
			tmp_components.add(osc01);

			Wire feedback = new Wire("comp01");
			WireList.add(feedback);

			// letrehozzuk az inverter kaput
			INVERTER comp1_inv1 = new INVERTER("comp01", feedback);
			tmp_components.add(comp1_inv1);
			feedback.SetConnection(comp1_inv1, null);
			Wire comp1_w1 = new Wire("comp01");
			WireList.add(comp1_w1);
			comp1_w1.SetConnection(null, comp1_inv1);
			comp1_inv1.AddOutput(comp1_w1);

			// AND1 (!feedback-es)
			ANDGate main_and1 = new ANDGate("comp01", comp1_w1, main_w1);
			tmp_components.add(main_and1);
			comp1_w1.SetConnection(main_and1, null);
			main_w1.SetConnection(main_and1, null);
			Wire comp1_w2 = new Wire("comp01");
			WireList.add(comp1_w2);
			comp1_w2.SetConnection(null, main_and1);
			main_and1.AddOutput(comp1_w2);

			// AND2 (feedback-es)
			ANDGate main_and2 = new ANDGate("comp01", feedback, main_w2);
			tmp_components.add(main_and2);
			feedback.SetConnection(main_and2, null);
			main_w2.SetConnection(main_and2, null);
			Wire comp1_w3 = new Wire("comp01");
			WireList.add(comp1_w3);
			comp1_w3.SetConnection(null, main_and2);
			main_and2.AddOutput(comp1_w3);

			// OR1 (a ket es kapus)
			ORGate main_or1 = new ORGate("comp01", comp1_w2, comp1_w3);
			tmp_components.add(main_or1);
			comp1_w2.SetConnection(main_or1, null);
			comp1_w3.SetConnection(main_or1, null);
			// Wire comp1_w4 = new Wire("comp01");
			// WireList.add(comp1_w4);
			// comp1_w4.SetConnection(null, main_or1);
			main_or1.AddOutput(feedback);
			feedback.SetConnection(null, main_or1);

			// oszcilloszkop
			osc01.wireIn.add(comp1_w3);
			comp1_w3.SetConnection(osc01, null);

			// LED
			led01.wireIn.add(feedback);
			feedback.SetConnection(led01, null);

		}
		/*****************************************************************
		 * ************** TESZTARAMKOROK leirasanak vege *************
		 ******************************************************************/

		//Csomák féle hierarchia start
		HierarchyCounter cntr=new HierarchyCounter();
		ComponentList=new ArrayList<List<DigitalObject>>();//tmp
		ComponentList.add(tmp_components);//tmp2
		cntr.CountHierarchy(WireList, ComponentList);
		//Csomák féle hierarchia end
		
		//Tubee féle hierarchia start
		//buildHierarchy(tmp_components);
		//getFeedbacks();
		//Tubee féle hierarchia start
		Debug();
	}
		

	public void Debug(){
		/* KIIRATAS, DEBUG */
		int szint = 0;
		for (List<DigitalObject> sublist : ComponentList) {
			System.out.println();
			System.out.print("Szint: ");
			System.out.print(szint++);
			System.out.print("\t");
			for (DigitalObject o : sublist) {
				System.out.print(o.GetID() + ", ");
				if (o.Feedbacks != null && !o.Feedbacks.isEmpty()) {
					System.out.print("FEEDBACK [");
					for (DigitalObject f_o : o.Feedbacks) {
						System.out.print(" " + f_o.ID + ", ");
					}
					System.out.print("]");
				}
			}
		}
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
	public void SetSequence(int Sequence, String ElementID) {
		GENERATOR GEN_to_setsequence; /* Temporalis valtozo */
		GEN_to_setsequence = (GENERATOR) GetElementByID(ElementID);
		GEN_to_setsequence.SetSequence(Sequence);
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
		SWITCH_to_toggle.Toggle();
	};

	/**
	 * Meghivja az osszes iComponent interfeszt megvalosito objektum Step()
	 * metodusat.
	 */
	public void StepComponents() {
		/*
		 * Elvileg mar fel van epulve a hierarchia igy nekem eleg megkapnom a
		 * ComponentListet
		 */
		DigitalObject obj;
		for (List<DigitalObject> sublist : ComponentList) {
			for (DigitalObject o : sublist) {
				obj = (DigitalObject) o;
				obj.Step();
			}
		}
	}
	public boolean isMyObject(String obj_name){
		boolean result=false;
		for (List<DigitalObject> sublist : ComponentList) {
			for (DigitalObject o : sublist) {
				if((o.ID.split("#")[2]).trim()==obj_name)
					result= true;
				else
					result= false;
			}	
		}
		return result;
	}
	public boolean isMyWire(String wire_name){
		boolean result=false;
		for (Wire w : WireList) {
			if((w.GetID().split("#")[2]).trim()==wire_name)
				result= true;
			else
				result= false;
		}	
		return result;
	}
}
