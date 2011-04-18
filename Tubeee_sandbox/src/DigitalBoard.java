/*  IMPORTOK  */
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.*;
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
		ComponentList.add(new ArrayList<DigitalObject>());
		// Composit MainComposit = new Composit("main");
	}

	/* METODUSOK */
	/**
	 * Megkeres egy adott elemet egy Composit ComponentList listajaban
	 */
//	public DigitalObject GetElementByID(String ElementID) {
//		if (ComponentList != null && !ComponentList.isEmpty()) {
//			for (List<DigitalObject> sublist : ComponentList) {
//				for (DigitalObject o : sublist) {
//					if (o.ID.equalsIgnoreCase(ElementID))
//						return (DigitalObject) o;
//				}
//			}
//		}
//		return null;
//	};

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
		ParseFile(strFilePath);
	};

	/**
	 * A megadott utvonalon talalhato fajlt olvassa be es soronkent ertelmezi az
	 * allomanyt
	 * 
	 * @param strFilePath
	 *            Az aramkort leiro dokumentum (*.bhdl) eleresi utvonala
	 */
	public void ParseFile(String strFilePath) {
        ArrayList<ArrayList<String>> foundComposites = new ArrayList<ArrayList<String>>();//Ebben t�roljuk a parszolt strukt�r�t
        String strFileContents = "";//Beolvasott f�jl tartalma
        ///////////////////////////F�JLB�L olvas�s//////////////////////////////
        StringBuilder text = new StringBuilder();
        String NL = System.getProperty("line.separator");
        Scanner scanner = null;
        try {
            scanner = new Scanner(new FileInputStream(strFilePath));
            while (scanner.hasNextLine()) {
                text.append(scanner.nextLine());
            }
            strFileContents = text.toString();
        } catch (Exception e) {
            System.err.print(e.toString());
        }

        // Tisztogatas
        strFileContents = bhdlParser.remove("\n", strFileContents); //nincs sortores
        strFileContents = bhdlParser.remove("\t", strFileContents); //nincs tab

        //Amig nem �res a file
        int i = 0; //Ebben van elt�rolva,hogy h�nyadik t�mb�t t�rol� t�mb�t �rj�k el. A t�rol� t�mb a composit, egy eleme is t�mb,ebben a parancsok vannak.
        while (!strFileContents.equals("")) {

            //Megkeress�k composite-t
            String composit = bhdlParser.find_next_Composite(strFileContents.toString());

            //Kivessz�k r�gib�l
            strFileContents = strFileContents.replace(composit, "");

            //Hozz�adjuk a t�mbh�z, egy t�mb els� elemek�nt
            ArrayList<String> tmp = new ArrayList<String>();
            tmp.add(composit);
            foundComposites.add(tmp);
        }

        System.out.println("\n Composit �rtelemz�se \n");

        //Vannak m�r compositjaink egy t�mbben
        i = 0; //i ujrahazsn�l�sa
        while (i < foundComposites.size()) {//V�gigmegy�nk az �sszes compositon
            String current_composit = foundComposites.get(i).get(0);//Kivessz�k a vizsg�lt elemet
            current_composit = current_composit.replace("composit ", "");//nem kell eleje
            current_composit = current_composit.replace("endcomposit;", "");//Sem v�ge

            //Header legyen az in,out
            String header = bhdlParser.find_header(current_composit);//pl. main( in, out) kiszed�se
            current_composit = current_composit.replace(header, "");
            foundComposites.get(i).remove(0);//Kivessz�k a r�gi header
            foundComposites.get(i).add(header);//Berakjuk headert

            while (!current_composit.equals("")) {//R�gi teljes composittal dolgozunk
                String command = bhdlParser.find_next_Command(current_composit);//Keress�k meg ebben a k�v parancsot
                current_composit = current_composit.replace(command, "");//Kivessz�k a cmpositb�l
                foundComposites.get(i).add(command);
            }
            i++;
        }
        //KI�ratjuk a dolgokat,v�gig megy�nk a t�mb�n,amiben t�mb�k vannak,�s azok minden elem�t ki�rjuk
        i = 0;

        while (i < foundComposites.size()) {
            int j = 0;
            while (j < foundComposites.get(i).size()) {
                System.out.print((j == 0 ? "Fejl�c: " : "Parancs: ") + foundComposites.get(i).get(j));
                j++;
                System.out.println();
            }
            i++;
        }

        System.out.println("\n Parancs �rtelemz�se \n");

        //Most m�r megvan �sszes parancs, componensekhez rendelve, illetve a komponensek
        //Menj�nk v�gig rajtuk �s hozzuk l�tre az elemeket
        //Header alapj�n kompozit l�trehoz�sa
        i = 0;

        while (i < foundComposites.size()) {

            String header = foundComposites.get(i).get(0);//Szedj�k, ki a headert, az adott komponens, els� elemek�nt
            String name = "";
            Pattern regxp = Pattern.compile("^[a-zA-Z0-9]+$");//Kiszedj�k a nevet
            Matcher match = regxp.matcher(header);
            if (match.find()) {
                name = match.group();
            }
            header = header.replace(name, "");

            //Egyel�re csak kompozit j�n l�tre
            Composit comp = new Composit("", name);

            int j = 1;
            while (j < foundComposites.get(i).size()) {//Menj�nk a composit t�bbi r�sz�n is v�gig
                String command = "";
                String current_com = foundComposites.get(i).get(j);
                regxp = Pattern.compile(".+? ");//Kiszedj�k a parancot
                match = regxp.matcher(current_com);
                if (match.find()) {
                    command = match.group();
                    command.replace(" ", "");
                }
                current_com = current_com.replace(command, "");//Kiszedj�k a parancot eredeti sztringb�l
                System.out.println("Parancs: " + command);

                ArrayList<String> params = new ArrayList<String>();//Adott param�tereit t�rol� t�mb
                String param = "";
                
                while (!current_com.equals("")) {
                    regxp = Pattern.compile(".+?[ |&;=|]");//Param�terv�laszt�s
                    match = regxp.matcher(current_com);
                    if (match.find()) {
                        param = match.group();
                        param.replace(" ", "");
                        param.replace(";", "");
                        
                        if (!param.equals("")) {
                        	String par=param.replace(" ", "").replace(";", "");
                            params.add(par);//Hozz�adjuk a param�terhez
                        }
                        if(params.get(params.size()-1).endsWith(";")){
                        	String a=params.get(params.size()-1);
                        	a.replace(";", "");
                        	params.set(params.size()-1,a);
                        }
                        System.out.println("Param�ter: " + param);
                    }
                    current_com = current_com.replace(param, "");//Kiszedj�k parszolt param�tert
                }
                j++;
                if(command.startsWith("switch")){
                	ComponentList.get(0).add(new SWITCH(header, params.get(0)));
                }
                if(command.startsWith("generator")){
                	ComponentList.get(0).add(new GENERATOR(header, params.get(0),1,0));
                }
                if(command.startsWith("led")){
                	ComponentList.get(0).add(new LED(header, params.get(0)));
                }
                if(command.startsWith("assign")){
                	DigitalObject a = getItemByID(params.get(0));
                	DigitalObject b = getItemByID(params.get(2));
                	DigitalObject c = getItemByID(params.get(4));
                	if(params.get(3).equals("|")){
                		Wire wirein1=new Wire(header), wirein2=new Wire(header), wireOut=new Wire(header);
                		ORGate or=new ORGate(header, wirein1, wirein2);
                		b.wireOut.add(wirein1);
                		c.wireOut.add(wirein2);
                		wirein1.SetConnection(or, b);//azt is bassza meg, aki ezt forditva tal�lta ki
                		wirein2.SetConnection(or, c);
                		or.wireOut.add(wireOut);
                		a.wireIn.add(wireOut);
                		wireOut.SetConnection(a, or);
                		ComponentList.get(0).add(or);
                	}
                	if(params.get(3).equals("&")){
                		Wire wirein1=new Wire(header), wirein2=new Wire(header), wireOut=new Wire(header);
                		ANDGate or=new ANDGate(header, wirein1, wirein2);
                		b.wireOut.add(wirein1);
                		c.wireOut.add(wirein2);
                		wirein1.SetConnection(or, b);//azt is bassza meg, aki ezt forditva tal�lta ki
                		wirein2.SetConnection(or, c);
                		or.wireOut.add(wireOut);
                		a.wireIn.add(wireOut);
                		wireOut.SetConnection(a, or);
                		ComponentList.get(0).add(or);
                	}
                }
            }//while
            i++;
        }

        //Elemek l�trehoz�sa , bek�t�se

        /* Elkeszitem a WIre objektumokat, es a WireListet */
       // List<DigitalObject> tmp_components = new ArrayList<DigitalObject>();

        //ComponentList.add(foundComposites);
		//System.out.println(foundComposites);
        HierarchyCounter cntr=new HierarchyCounter();
		cntr.CountHierarchy(WireList, ComponentList);
		//Debug();
		System.out.println(strFilePath+ "'s Board is loaded");
		CountComponents();
		runProto();

    }
	
	public DigitalObject getItemByID(String eztkeresem){
		for(int i=0; i<ComponentList.size();i++){//minden szint
			for(int j=0; j<ComponentList.get(i).size();j++){//minden eleme
				if(ComponentList.get(i).get(j).ID.equalsIgnoreCase(eztkeresem)) 
					return ComponentList.get(i).get(j);//ha megtal�ltuk, visszaadjuk
				String id=ComponentList.get(i).get(j).ID;
				id=id.split("#")[2];
				if(id.equalsIgnoreCase(eztkeresem)) return ComponentList.get(i).get(j); //ha az id csak a nevet takarja, akkor is visszaadjuk
					
			}
		}
		return null;
	}
		
	/**
	 * Kezeli a file bet�lt�se ut�n �rkez� parancsokat.
	 * 
	 */
	public void runProto(){
		InputStreamReader input = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(input);
		String line,command = "",param1 = "",param2 = "";

        Pattern regxp;
        while(true){ 
        	try { //  a bevitt szoveget
    			line = reader.readLine();
                           
                //Regularis Kifejezes
                regxp = Pattern.compile(".*? "); // Megkeressuk parancsot

                //Megkeressuk a talalatokat
                Matcher match = regxp.matcher(line);
                if (match.find()){
                	command = match.group();//Kimentjuk parancsot
                } else {
                    regxp = Pattern.compile(".*");//Egyszavas parancs
                    match = regxp.matcher(line);
                    if (match.find()){
                    	command = match.group();//Kimentjuk parancsot
                    }
                }

                line = line.replace(command,"");//Kivesszuk talalatot,reszre keresunk cska
                command = command.replace(" ",""); //Kivesszuk spacet

                match = regxp.matcher(line);
                if (match.find()){
                	param1 = match.group();//Kimentjuk elso parametert
                } else {
                    regxp = Pattern.compile(".*");//Egyszavas parancs
                    match = regxp.matcher(line);
                    if (match.find()){
                    	param1 = match.group();//Kimentjuk parancsot
                    }
                }

                line = line.replace(param1,"");//Kivesszuk talalatot,reszre keresunk cska
                param1 = param1.replace(" ",""); //Kivesszuk spacet

                regxp = Pattern.compile(".*");
                match = regxp.matcher(line);
                match.find();
                param2 = match.group();//Kimentjuk masodik parametert
                param2 = param2.replace(" ",""); //Kivesszuk spacet
                           
                //Koszonjuk JAVA, hogy nem lehet stringet switchbe rakni
                //ha a kommand setFrequency gener_id freq
                if (command.equalsIgnoreCase("setFrequency")){
                	DigitalObject elem=getItemByID(param1);//ellenorzes, hogy jo e a nev
                	if(elem==null) System.out.println("x Error: Wrong Parameter: No object with id "+param1);
                    int freq = Integer.parseInt(param2);//jo e a param
                    if(freq<=0) System.out.println("x Error: Frequency must be positive");
                    try {
                    	//jo e az obj
                    	((GENERATOR)(elem)).SetFrequency(freq);
                    } catch(Exception ex)  {
                    	System.out.println("x Error: Wrong Parameter: Object is not Generator");
                    }
                    System.out.println(param1+"'s frequency is set to " + freq);
                    CountComponents();
                    runProto();
                 //ha stepComponents a kommand
                } else if(command.equalsIgnoreCase("stepComponents")){
                	StepComponents();//ujraszamol
                	System.out.println("Board circuit has stepped");
                	runProto();
                	//ha setoutput ??
                } else if(command.equalsIgnoreCase("setOutput")){
                    System.out.println("Output is set to " + param1);
                    //TODO?	
                    //ha setInterval
                } else if(command.equalsIgnoreCase("setInterval")){
                    System.out.println("Boards interval is set to " + param1);
                    //TODO?
                    //ha toggleswitch sw0 a kommand
                } else if(command.equalsIgnoreCase("toggleSwitch")){
                	DigitalObject elem=getItemByID(param1);
                	if(elem==null) System.out.println("x Error: Wrong Parameter: No object with id "+param1);
                     try {
                    	((SWITCH)(elem)).Toggle();
                    	System.out.println(param1+"'s value is set to " + ((SWITCH)(elem)).Value);
                    } catch(Exception ex)  {
                    	System.out.println("x Error: Wrong Parameter: Object is not Generator");
                    }
                    CountComponents();
                    runProto();
                    //ha setSample osc 3 a kommand
                } else if(command.equalsIgnoreCase("setSample")){
                    System.out.println("For each Digitalobject in List");
                    System.out.println("Search for the name " + param1);
                    System.out.println("If it is OSCILLOSCOPE, call its SetSample with param " + param2);
                    int sample = Integer.parseInt(param2);
                    //TODO? int meg binary problem
                    CountComponents();
                    //ha exit, kil�p
                } else if(command.equalsIgnoreCase("exit")){
                    System.exit(0);
                } else {
                    System.out.print("x Error: CommandNotFound\n");
                    runProto();
                }
    		} catch (Exception e) {
    			System.out.println(e.toString());
    		} 
        }//while(true)
	
	}//runproto
	
//	public void Debug(){
//		/* KIIRATAS, DEBUG */
//		int szint = 0;
//		for (List<DigitalObject> sublist : ComponentList) {
//			System.out.println();
//			System.out.print("Szint: ");
//			System.out.print(szint++);
//			System.out.print("\t");
//			for (DigitalObject o : sublist) {
//				System.out.print(o.GetID() + ", ");
//				if (o.Feedbacks != null && !o.Feedbacks.isEmpty()) {
//					System.out.print("FEEDBACK [");
//					for (DigitalObject f_o : o.Feedbacks) {
//						System.out.print(" " + f_o.ID + ", ");
//					}
//					System.out.print("]");
//				}
//			}
//		}
//	};

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
		tmp = (GENERATOR) getItemByID(ElementID);
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
		GEN_to_setsequence = (GENERATOR) getItemByID(ElementID);
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
		SWITCH_to_toggle = (SWITCH) getItemByID(ElementID);
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
		System.out.println("<main count>");
		
		DigitalObject obj;
		for (List<DigitalObject> sublist : ComponentList) {
			for (DigitalObject o : sublist) {
				obj = (DigitalObject) o;
				obj.Step();
				for(int i=0;i<obj.wireOut.size();i++){
					if(obj.ID.split("#")[0].equalsIgnoreCase("main"))
						System.out.println("<"+obj.wireOut.get(i).GetName()+"> value is "+obj.wireOut.get(i).GetValue());
					
				}
				//System.out.println("xxx"+obj.getClass().toString());
				if(obj.getClass().toString().equals("class Oscilloscope") || 
						obj.getClass().toString().equals("class LED"))
							System.out.println("<"+obj.ID.split("#")[2]+"> value is "+ obj.wireIn.get(0).GetValue());
									
				
			}
		}
		
	}
	/**
	 * Meghivja az osszes iComponent interfeszt megvalosito objektum Count()
	 * metodusat.
	 */
	public void CountComponents() {
		/*
		 * Elvileg mar fel van epulve a hierarchia igy nekem eleg megkapnom a
		 * ComponentListet
		 */
		System.out.println("<main count>");
		DigitalObject obj;
		for (List<DigitalObject> sublist : ComponentList) {
			for (DigitalObject o : sublist) {
				obj = (DigitalObject) o;
				obj.Count();
				for(int i=0;i<obj.wireOut.size();i++){
					//System.out.print(obj.ID.split("#")[0].trim());
					if(obj.ID.split("#")[0].equalsIgnoreCase("main"))
						System.out.println("<"+obj.wireOut.get(i).GetName()+"> value is "+obj.wireOut.get(i).GetValue());
				}
				//System.out.println("xxx"+obj.getClass().toString());
				if(obj.getClass().toString().equals("class Oscilloscope") || 
						obj.getClass().toString().equals("class LED"))
							System.out.println("<"+obj.ID.split("#")[2]+"> value is "+ obj.wireIn.get(0).GetValue());
					
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
