import java.util.Stack;
import java.util.regex.*;

public class bhdlParser {
	/**
	 * Fuggveny mely ellenori, talalhato-e egy adott szovegben a regularis
	 * kifejezesnek megfeleltetheto reszlet
	 * 
	 * @param expr
	 *            egy regul�ris kifejezes
	 * @param source
	 *            egy String, amiben keresni akarunk
	 * @return a legelso megtalalt elem, ha ilyen nincs, akkor false
	 */
	public static String matching(String expr, String source) {
		Pattern regexp = Pattern.compile(expr);
		Matcher match = regexp.matcher(source);
		if (match.find()) {
			String foundElement = match.group();
			return foundElement;
		} else {
			return null;
		}
	}

	/**
	 * Eltavolit minden sortorest a megadott forras szovegbol
	 * 
	 * @param source
	 *            a karakterlanc, amibol kivagjuk a sortoreseket
	 * @return a megtisztitott karakterlanc
	 */
	public static String remove_CR_LF(String source) {
		// Kivessz�k a Carrige Return �s Line Feed elemeket
		source = source.replaceAll("\n", "");
		source = source.replaceAll("\r", "");

		return source;
	}

	/**
	 * Eltavolitja a felesleges Whitespace karaktereket a megadott forras
	 * szovegbol
	 * 
	 * @param source
	 *            a megtisztitani kivant karaktersorozat
	 * @return szavankent maximalisan egy whitespace-t tartalmazo szoveg
	 */
	public static String remove_Spaces(String source) {
		// Kivessz�k a Spaceket
		source = source.replaceAll("( |\t)+", " ");
		source = source.replaceAll("\\( ", "\\(");
		source = source.replaceAll("\\) ", "\\)");
		source = source.replaceAll("= ", "=");
		source = source.replaceAll(" =", "=");
		source = source.replaceAll(" ;", ";");
		source = source.replaceAll("; ", ";");

		return source;
	}

	/**
	 * find_next_Composite:visszaadja a k�vetkez� Composite-ot a f�jlb�l
	 * 
	 * @param source
	 *            :A f�jl,ami Composite-kat tartalmaz
	 * @return A kivett Comopsite
	 * @author Peti
	 */
	public static String FindComposite(String source, String compname) {
		// Regul�ris Kifejez�s
		Pattern regexp = Pattern.compile("composit[ ]?"+compname+".*?endcomposit;");
		// Megkeress�k a tal�latokat
		Matcher match = regexp.matcher(source);
		// Els� kell
		match.find();
		String foundComposit = match.group();
		return foundComposit;
	}
	/**
	 * Megkeresi a main(in,out) szignoval lellatott compositot
	 * @param source
	 * @return a composit BHDL leirasa
	 */
	public static String FindMainComposit(String source) {
		// Regul�ris Kifejez�s
		String regexp_ = "composit[ ]?(main)\\(in(( |[\\w]*,?)+),out(( |[\\w]*,?)+)\\)(.*)?endcomposit;";
		Pattern regexp = Pattern.compile(regexp_);
		Matcher match = regexp.matcher(source);
		match.find();
		String foundComposit = match.group();
		return foundComposit;
	}
	/**
	 * Letrehozza a main(in,out) compositot
	 * @return a main composit
	 */
	public static Composit CreateMain(String composit) {
		// Egy regularis kifejezes ami illeszkedik egy composit elemre.
		String rege = "composit[ ]?(main)\\(in(( |[\\w]*,?)+),out(( |[\\w]*,?)+)\\)(.*)?endcomposit;";
		Pattern regexp = Pattern.compile(rege);
		// Megkeressuk a talalatokat
		Matcher match = regexp.matcher(composit);
		String CompositName = "";
		// keresunk
		match.find();
		// ha volt talalat
		if (match.matches() && match.groupCount() > 1) {
			CompositName = match.group(1);
		}
		//  a composit letrehozasa
		Composit myComposit = new Composit("null",CompositName);
		return myComposit;
	}
	
	
	
	public static Composit ReadComposit(Composit ThisComposit, String source, String composit_name) {
		String[] CompositCommands = null;	// Egy kompozitvban levo utasitasok
		CompositCommands = getCommands(FindComposite(source,composit_name));		
		CommandParser(ThisComposit, source,CompositCommands);
		ThisComposit.buildHierarchy();
		//ThisComposit.getFeedbacks();
		return ThisComposit;
	}
	
	/**
	 * Egy composit BHDL leirasabol tombbe rendezi a benne levo parancsokat
	 * @param bhdlcomposit a composit BHDL leirasa
	 * @return Egy String tomb, mely tartalmazza  a parancsokat, soronkent, a sorvei ";" jelet is beleertve
	 */
	public static String[] getCommands(String bhdlcomposit){
		// Egy regularis kifejezes ami illeszkedik egy composit elemre.
		String rege = "composit[ ]?([\\w, ]+)\\(in(( |[\\w]*,?)+),out(( |[\\w]*,?)+)\\)(.*)?endcomposit;";
		Pattern regexp = Pattern.compile(rege);
		// Megkeressuk a talalatokat
		Matcher match = regexp.matcher(bhdlcomposit);
		String[] Commands = null;
		// keresunk
		match.find();
		// ha volt talalat
		if (match.matches()) {
			// belso parancsok kiynerese
			Commands = match.group(6).split(";");
			for (int i = 0; i < Commands.length; i++) {
				Commands[i] = Commands[i].trim() + ";";
				Commands[i] = Commands[i].toLowerCase();
			}
		}
		return Commands;	
	}
	/**
	 * Ertelmez egy BHDL parancsot. 
	 * A sorokra mintaillesztest vegez es ha talal egy mintat, a parancsnak 
	 * megfeleloen jar el, hivj a fuggvenyeket
	 * @param Owner a composit, melynek a parancsait ertelmezzuk.
	 * @param source A teljes BHDL fajl
	 * @param commands egy lista a parancsokbol
	 */
	public static void CommandParser(Composit Owner,String source, String[] commands) {
		/*
		 * Kulonbozo regularis kifejezesek mely segitsegevel megkaphatoak a reszletek is
		 */
		String reg_wire = "wire[ ]+([\\w]+)?;";
		String reg_led = "led[ ]+([\\w]+)?;";
		String reg_oscilloscope = "oscilloscope[ ]+([\\w]+)(\\(([\\d]*)\\))*?;";
		String reg_generator = "generator[ ]+([\\w]+)?;";
		String reg_switch = "switch[ ]+([\\w]+);";
		String reg_set = "set[ ]+([\\w]+)=([\\d]+);";
		String reg_comp = "([\\w, ]+)\\(in(( |[\\w]*,?)+)out(( |[\\w]*,?)+)\\)(.*)?;";
		String reg_assign = "assign[ ]+([\\w]+)=(.*?);";
		/*
		 * Vegignezzuk az osszes parancsot, illeszkedik-e valamelyik kifejezesre
		 * ha illeszkedik, aszerint hivjuk meg a fuggvenyeket
		 */
		for (int i = 0; i < commands.length; i++) {
			if (matching(reg_wire, commands[i]) != null) {				
				Owner.AddToWireList(CreateWire(Owner, commands[i]));	
				System.out.println("Wire has been created.");
			}
			if (matching(reg_led, commands[i]) != null) {
				Owner.getFirstLevelOfComponentList().add(CreateLed(Owner, commands[i]));
				System.out.println("LED has been  created.");
			}
			if (matching(reg_oscilloscope, commands[i]) != null) {
				Owner.getFirstLevelOfComponentList().add(CreateOscilloscope(Owner,
						commands[i]));
				System.out.println("Oscilloscope  has been created.");
			}
			if (matching(reg_generator, commands[i]) != null) {
				Owner.getFirstLevelOfComponentList().add(CreateGenerator(Owner,
						commands[i]));
				System.out.println("GENERATOR has been  created.");
			}
			if (matching(reg_switch, commands[i]) != null) {
				Owner.getFirstLevelOfComponentList().add(CreateSwitch(Owner,
						commands[i]));
				System.out.println("SWITCh  has been created.");
			}
			if (matching(reg_set, commands[i]) != null) {
				SettingElement(Owner,commands[i]);
				System.out.println("Setting element");
			}
			if (matching(reg_assign, commands[i]) != null) {
				assign(Owner,commands[i]);
				System.out.println("Assign expression.");
			}
			if (matching(reg_comp, commands[i]) != null) {
				CreateComposit(Owner,source,commands[i]);
				System.out.println("Composit  has been embedded.");
			}
		}
	}
	
	/**
	 * Letrehoz egy Wire-t, es hozza is adja a parameterben megkapott Composit
	 * WireList-jahoz
	 * @param myComposit Kompozit, melyen belul dolgozunk
	 * @param command AZ aktualis parancs
	 * @return a letrehozott Wire
	 */
	private static Wire CreateWire(Composit owner, String command) {
		String CompositName = owner.GetName();
		String reg_wire = "wire[ ]+([\\w]+)?;";
		Pattern regexp = Pattern.compile(reg_wire);
		Matcher match = regexp.matcher(command);

		match.find();
		if(match.matches()){
			String wirename = match.group(1).trim();
			Wire myWire = new Wire(CompositName, wirename);
			owner.AddToWireList(myWire);
			return myWire;
		}
		return null;
	}
	
	/**
	 * Letrehoz egy LEDet.
	 * @param myComposit Kompozit, melyen belul dolgozunk
	 * @param command AZ aktualis parancs
	 * @return a letrehozott LED
	 */
	private static LED CreateLed(Composit owner, String command) {
		String CompositName = owner.GetName();
		String reg_led = "led[ ]+([\\w]+)?;";
		Pattern regexp = Pattern.compile(reg_led);
		Matcher match = regexp.matcher(command);

		match.find();

		if(match.matches()){
			String ledname = match.group(1).trim();
			LED myLED = new LED(CompositName, ledname);
			return myLED;
		}
		return null;
	}
	/**
	 * Letrehoz egy Oscilloscope-ot
	 * @param myComposit Kompozit, melyen belul dolgozunk
	 * @param command AZ aktualis parancs
	 * @return a letrehozott elem
	 */
	private static Oscilloscope CreateOscilloscope(Composit owner, String command) {
		String CompositName = owner.GetName();
		String reg_oscilloscope = "oscilloscope[ ]+([\\w]+)(\\(([\\d]*)\\))*?;";
		Pattern regexp = Pattern.compile(reg_oscilloscope);
		Matcher match = regexp.matcher(command);

		match.find();

		if(match.matches()){
			String oscname = match.group(1).trim();
			int samplesize = Integer.parseInt(match.group(3).trim());
			Oscilloscope myOscilloscope = new Oscilloscope(CompositName, oscname,
					samplesize);
			return myOscilloscope;
		}
		return null;
	}
	/**
	 * Letrehoz egy GENERATOR-t es a hozza tartozo kimeno drotot
	 * @param myComposit Kompozit, melyen belul dolgozunk
	 * @param command AZ aktualis parancs
	 * @return a letrehozott elem
	 */
	private static GENERATOR CreateGenerator(Composit owner, String command) {
		String CompositName = owner.GetName();
		String reg_generator = "generator[ ]+([\\w]+)?;";
		Pattern regexp = Pattern.compile(reg_generator);
		Matcher match = regexp.matcher(command);

		match.find();

		if(match.matches()){
			String genname = match.group(1).trim();
			GENERATOR myGenerator = new GENERATOR(CompositName, genname, 1000, 0);		
			return myGenerator;
		}
		return null;
	}
	/**
	 * Letrehoz egy SWITCH-et es a kimeno drotjat
	 * @param myComposit Kompozit, melyen belul dolgozunk
	 * @param command AZ aktualis parancs
	 * @return a letrehozott elem
	 */
	private static SWITCH CreateSwitch(Composit owner, String command) {
		String CompositName = owner.GetName();
		String reg_switch = "switch[ ]+([\\w]+);";
		Pattern regexp = Pattern.compile(reg_switch);
		Matcher match = regexp.matcher(command);

		match.find();

		if(match.matches()){
			String switchname = match.group(1).trim();
			SWITCH mySwitch = new SWITCH(CompositName, switchname);
			return mySwitch;
		}
		return null;
	}	
	
	
	
	
	
	
	
	
	/**
	 * Letrehoz egy Compositot, es ertelmezi a tartalmat.
	 * REKURZIV eljaras!
	 * 
	 * @param owner Egy composit, melybol a parancs meg lett hivva
	 * @param source BHDL fajl
	 * @param command egy composit hivasos eljaras (pl. : composit(in sw1,gen1 out led1); )
	 * @return a letrehozott composit
	 */
	private static Composit CreateComposit(Composit owner, String source, String command) {
		// Lekerdezzuk a szulo nevet
		String OwnerName = owner.GetName();
		// Minta amely illeszkedik a composit hivasra
		String reg_comp = "([\\w, ]+)\\(in(( |[\\w]*,?)+)out(( |[\\w]*,?)+)\\)(.*?);";
		Pattern regexp = Pattern.compile(reg_comp);
		Matcher match = regexp.matcher(command);

		match.find();
		if(match.matches()){
			// a letrehozni kivant composit neve BHDL szerint
			String comp_name = match.group(1).trim();
			// letrehozzuk a Compositot
			Composit myComposit = new Composit(OwnerName,comp_name);
			
			/*
			 * itt  azt fogjuk vizsgalni, , hogy a szulo compositbol
			 * hogy nez ki a most letrehozando composit, azaz a szulo
			 * milyen drotokat kot be ill ki a letrehozando compositba/bol
			 * Ezekbol egy listat epitunk, eltaroljuk.
			 * A lista tartalma tehat lehet: 
			 * 		egy szulo beli elem neve
			 * 		egy szulo beli wire neve
			 */
			
			String[] WiresIn = match.group(2).split(",");
				for(int i=0; i<WiresIn.length;i++)WiresIn[i]=WiresIn[i].trim();
			String[] WiresOut= match.group(4).split(",");
				for(int i=0; i<WiresOut.length;i++)WiresOut[i]=WiresOut[i].trim();
			
			
			/*
			 * Mivel azonban a most letrejott Compositban sajat nevek vannak, 
			 * azokat meg kell keresni.
			 * Ehhez megvizsgaljuk a Composit leirasanak fejlecet.
			 * Mintaillesztest hasznalok, a kapott elemeket
			 * (kimeno, bemeno) egy listaba szervezem 
			 */
			String[] HeaderWiresOut=null;
			String[] HeaderWiresIn=null;
			
			// a Composit headerjenek megkeresesehez hasznalt mintaillesztes
			String CompositeHeader = FindComposite(source,comp_name);
			String strHederRegexp = "([\\w, ]+)\\(in(( |[\\w]*,?)+)out(( |[\\w]*,?)+)\\)(.*)?;";
			Pattern HeaderRegexp = Pattern.compile(strHederRegexp);
			Matcher MatchingHeader = HeaderRegexp.matcher(CompositeHeader);

			MatchingHeader.find();
			// Ha talat megfelelo fejreszt
			if(MatchingHeader.matches()){
				// Megvizsgaljuk az aszerinti bemeno drotokat...
				HeaderWiresIn = MatchingHeader.group(2).split(",");
				// Tisztogatunk
				for(int i=0; i<HeaderWiresIn.length;i++)HeaderWiresIn[i]=HeaderWiresIn[i].trim();
				//.. es a kimenoket is
				HeaderWiresOut= MatchingHeader.group(4).split(",");
				// Tisztigatunk
				for(int i=0; i<HeaderWiresOut.length;i++)HeaderWiresOut[i]=HeaderWiresOut[i].trim();
				
				/* A Composit fejlece alapjan megtalalt drotokat most letre is hozzuk.
				 * Mindegyiket hozzakotjuk a Composithoz es hozzadjuk a Composit listajahoz
				 */	
				//Bemeno drotok
				for(String wire_in:HeaderWiresIn){
					Wire w=new Wire(comp_name,wire_in);
					w.SetConnection(null,null);
					myComposit.wireIn.add(w);	
					myComposit.AddToWireList(w);
				}
				//Kimeno drotok
				for(String wire_out:HeaderWiresOut){
					Wire w=new Wire(comp_name,wire_out);
					w.SetConnection(null,null);
					myComposit.wireOut.add(w);
					myComposit.AddToWireList(w);
				}

			/**
			 * REKURZIV FELDERITESE A TOBBI KOMPOZITNAK
			 * ReadComposit(myComposit,source,comp_name);			 * 
			 */
			Composit compi = ReadComposit(myComposit,source,comp_name);
			owner.getFirstLevelOfComponentList().add(compi);

			/* Ez pedig ahogy kivulrol nez ki az osszekottetes
			 */	
			//Bemeno drotok
			Wire w =null;
			for(int i=0;i<WiresIn.length;i++){
				if(owner.GetWireByName(WiresIn[i])!=null){
					w = owner.GetWireByName(WiresIn[i]);
				}
				else if(owner.GetElementByName(WiresIn[i])!=null){
					w=new Wire(comp_name);
					owner.GetElementByName(WiresIn[i]).wireOut.add(w);
					w.SetConnection(null, owner.GetElementByName(WiresIn[i]));
				}
				
				w.SetConnection(myComposit,null);
				myComposit.wireIn.add(w);	
				owner.AddToWireList(w);
			}
			//Kimeno drotok
			for(int i=0;i<WiresOut.length;i++){
				if(owner.GetWireByName(WiresOut[i])!=null){
					w = owner.GetWireByName(WiresOut[i]);
				}
				else if(owner.GetElementByName(WiresOut[i])!=null){
					w=new Wire(comp_name);
					owner.GetElementByName(WiresOut[i]).wireIn.add(w);
					w.SetConnection(owner.GetElementByName(WiresOut[i]),null);
					owner.AddToWireList(w);
				}					
				w.SetConnection(null,myComposit);
				myComposit.wireOut.add(w);	
				owner.AddToWireList(w);
			}
		}
		
			
			/*
			 * Most jon a legmacerasabb resz.
			 * Egy masik Compositbol a most letrehozott
			 * Composit mashogy nez ki, masok a bemeno drotjai.
			 * Ezt kell valahogy athidalni. Egy biztos pont: 
			 * Ugyanannak a Compositnak minden komponensbol
			 *  - ugyanannyi osszekottetese van
			 *  - ugyanabban a sorendben
			 */
			
			// Temporalis valtozo
			Wire w=null;	
			/*
			 * Vegignezem, hogy a szulo mit kot a Composithoz.
			 * A Compositon beluli drotokat lecserelem a Compositon kivulire
			 */
			for(int i=0;i<WiresIn.length;i++){
				// Ha letezik a szuloben az adott WIRE
				if(owner.GetWireByName(WiresIn[i])!=null){
					// owner.GetWireByName(Wires[i])--> Szulo composit drotja ami belemegy a Compositba
					w= owner.GetWireByName(WiresIn[i]);
					
					// Hozzakotom a kulso drothoz a belso droton csucsulo elemeket
					//myComposit.wireIn.get(i) --> Composit bemeno drotjai
					w.objectsOut = myComposit.wireIn.get(i).objectsOut;
					
					/*
					 * most ertesiteni kell ezeket az elemeket, hogy valtozott a
					 * listajuk. (ha nem ures az elemek halmaza)
					 */	
					if(w.objectsOut!=null){
						for(int o=0;o<w.objectsOut.size();o++){
							/*
							 * Kinyerjuk a valtoztatando Wire-k indexet
							 */
							int WireToChangeIndex = w.objectsOut.get(o).wireIn.indexOf(myComposit.wireIn.get(i));
							//Kicsereljuk a drotot ezeken az elemeken
							w.objectsOut.get(o).wireIn.set(WireToChangeIndex, w);							
						}
					}		
				}
				// Ha ez a bemeno ize egy elem
				else if(owner.GetElementByName(WiresIn[i])!=null){
					/*
					 *  owner.GetElementByName(WiresIn[i]).wireOut.get(0)--> 
					 *  Szulo composit elemenek drotja ami belemegy a Compositba
					 *  
					 */
					for(int index=0;index<owner.GetElementByName(WiresIn[i]).wireOut.size();index++){
						if(owner.GetElementByName(WiresIn[i]).wireOut.get(index).objectsOut.contains(myComposit)){
							w= owner.GetElementByName(WiresIn[i]).wireOut.get(index);
							w.objectsOut.remove(w.objectsOut.indexOf(myComposit));
						}
					}
					// a kivulrol jovo drothoz csatlakoztatjuk a composit elmeit
					w.objectsOut.addAll(myComposit.wireIn.get(i).objectsOut);
					/*
					 * most ertesiteni kell ezeket az elemeket, hogy valtozott a
					 * listajuk. (ha nem ures az elemek halmaza)
					 */
					if(w.objectsOut!=null){
						for(int o=0;o<w.objectsOut.size();o++){
							/*
							 * Kinyerjuk a valtoztatando Wire-k indexet
							 */
							int WireToChangeIndex = w.objectsOut.get(o).wireIn.indexOf(myComposit.wireIn.get(i));
							//Kicsereljuk a drotot ezeken az elemeken
							w.objectsOut.get(o).wireIn.set(WireToChangeIndex, w);							
						}
					}									
				}
				myComposit.wireIn.set(i, w);
				myComposit.ReplaceWire(myComposit.wireIn.get(i), w);
			}
			
			/*
			 * UGYANZET el is kell jatszani csak most a kimeno elemeket csatlakoztatjuk
			 */
			w=null;	
			for(int i=0;i<WiresOut.length;i++){
				// Ha letezik a szuloben az adott WIRE...
				if(owner.GetWireByName(WiresOut[i])!=null){
					// ...akkor w az a wire lesz
					w= owner.GetWireByName(WiresOut[i]);
					
					// Hozzakotom a kulso drothoz a belso droton csucsulo elemeket
			
					w.objectsIn = myComposit.wireOut.get(i).objectsIn;
					
					/*
					 * most ertesiteni kell ezeket az elemeket, hogy valtozott a
					 * listajuk. (ha nem ures az elemek halmaza)
					 */	
					if(w.objectsIn!=null){
						for(int o=0;o<w.objectsIn.size();o++){
							/*
							 * Kinyerjuk a valtoztatando Wire-k indexet
							 */
							int WireToChangeIndex = w.objectsIn.get(o).wireOut.indexOf(myComposit.wireOut.get(i));
							//Kicsereljuk a drotot ezeken az elemeken
							w.objectsIn.get(o).wireOut.set(WireToChangeIndex, w);							
						}
					}		
				}
				// Ha ez a bemeno ize egy elem
				else if(owner.GetElementByName(WiresOut[i])!=null){
					for(int index=0;index<owner.GetElementByName(WiresOut[i]).wireIn.size();index++){
						if(owner.GetElementByName(WiresOut[i]).wireIn.get(index).objectsIn.contains(myComposit)){
							w= owner.GetElementByName(WiresOut[i]).wireIn.get(index);
							w.objectsIn.remove(w.objectsIn.indexOf(myComposit));
						}
					}
					// a kivulrol jovo drothoz csatlakoztatjuk a composit elmeit
					w.objectsIn.addAll(myComposit.wireOut.get(i).objectsIn);
					/*
					 * most ertesiteni kell ezeket az elemeket, hogy valtozott a
					 * listajuk. (ha nem ures az elemek halmaza)
					 */
					if(w.objectsIn!=null){
						for(int o=0;o<w.objectsIn.size();o++){
							/*
							 * Kinyerjuk a valtoztatando Wire-k indexet
							 */
							int WireToChangeIndex = w.objectsIn.get(o).wireOut.indexOf(myComposit.wireOut.get(i));
							//Kicsereljuk a drotot ezeken az elemeken
							w.objectsIn.get(o).wireOut.set(WireToChangeIndex, w);							
						}
					}									
				}
				myComposit.wireOut.set(i, w);
				myComposit.ReplaceWire(myComposit.wireOut.get(i), w);
			}
//			ReadComposit(myComposit,source,comp_name);
			return myComposit;
		}
		return null;
	}


	
	private static boolean SettingElement(Composit owner, String command) {
		String reg_set = "set[ ]+([\\w]+)=([\\d]+);";
		Pattern regexp = Pattern.compile(reg_set);
		Matcher match = regexp.matcher(command);

		match.find();
		String elementname = match.group(1).trim();
		int value = Integer.parseInt(match.group(3).trim());
		String elementtype = owner.GetElementByName(elementname).GetType();
		if (elementtype.equalsIgnoreCase("SWITCH")) {
			owner.SetSwitch(value, elementname);
			return true;
		} else if (elementtype.equalsIgnoreCase("GENERATOR")) {
			owner.SetSequence(value, elementname);
			return true;
		}

		return false;
	}
	
	private static  Wire assign(Composit owner, String command){
		// Ahooz, hogy az Assign reszeit be tujuk olvasni, reszletes mintaillesztes
		String reg_assign = "assign[ ]+([\\w]+)=(.*?);";
		Pattern regexp = Pattern.compile(reg_assign);
		Matcher match = regexp.matcher(command);
		Wire assigned_wire =null;
		match.find();

		if(match.matches()){
			String rvalue = match.group(2).trim();
			String lvalue = match.group(1).trim();
			String postfix = Infix2Postfix(rvalue);
			/*********************************************************************/
			/**********     AZ ASSIGNBAN CSAK A VEGE LEHET HIBAS      ************/
			/*********************************************************************/
			String[] items = postfix.split(" ");
			Stack<Wire> WireStack = new Stack<Wire>();

			// Kiertekeljuk a PostFixes alakot
			for(String item:items){
				// ha nem operator
				if(!isOperator(item)){
					// Hozzadjuk a veremhez
					if(owner.GetWireByName(item)!=null){
						WireStack.add(owner.GetWireByName(item));
					}else if(owner.GetElementByName(item)!=null){
						// ha meg nincs kesz a wire, gyorsan letrehozzuk
						Wire w = new Wire(owner.GetName());
						w.SetConnection(null, owner.GetElementByName(item));
						owner.GetElementByName(item).wireOut.add(w);
						owner.AddToWireList(w);
						
						WireStack.add(w);
					}
				}
				// ha operator (& | !)
				else if(isOperator(item)){
					// meg kell neznunk, hogy hagy operandust igenyel
					if(NumOfOperand(item)==1){
						if(item.equalsIgnoreCase("!"))
						{
							Wire inv_in1 = WireStack.pop();								
							INVERTER myInverter = new INVERTER(owner.GetName(), inv_in1);
							inv_in1.SetConnection(myInverter, null);
							Wire inv_out = new Wire(owner.GetName());
							inv_out.SetConnection(null, myInverter);
							owner.AddToWireList(inv_out);
							myInverter.AddOutput(inv_out);
							WireStack.push(inv_out);
							owner.getFirstLevelOfComponentList().add(myInverter);
						}
					}
					if(NumOfOperand(item)==2){
						if(item.equalsIgnoreCase("&"))
						{
							Wire and_in1 = WireStack.pop();	
							Wire and_in2 = WireStack.pop();	
							ANDGate myAnd = new ANDGate(owner.GetName(), and_in1,and_in2);
							and_in1.SetConnection(myAnd, null);
							and_in2.SetConnection(myAnd, null);
							Wire and_out = new Wire(owner.GetName());
							and_out.SetConnection(null, myAnd);
							owner.AddToWireList(and_out);
							myAnd.AddOutput(and_out);
							WireStack.push(and_out);
							owner.getFirstLevelOfComponentList().add(myAnd);
						}
						if(item.equalsIgnoreCase("|"))
						{
							Wire or_in1 = WireStack.pop();	
							Wire or_in2 = WireStack.pop();	
							ORGate myOr = new ORGate(owner.GetName(), or_in1,or_in2);
							or_in1.SetConnection(myOr, null);
							or_in2.SetConnection(myOr, null);
							Wire or_out = new Wire(owner.GetName());
							or_out.SetConnection(null, myOr);
							owner.AddToWireList(or_out);
							myOr.AddOutput(or_out);
							WireStack.push(or_out);
							owner.getFirstLevelOfComponentList().add(myOr);
						}
					}//end: operandusok szama					
				}//end if: ha operator				
			}//end for: lista bejarasa, postfix kiertekel		

			// ez a "vegeredmeny" ez a drot fog csatlakozni az lvaluehoz
			assigned_wire = WireStack.pop();
			/*
			 *  most megnezzuk, lvlaue hol s mikent letezik.
			 */
			
			// Ha ez egy wire a composit WireList-jeben
			if(owner.GetWireByName(lvalue)!=null){
				/* 
				 * ha wire, akkor mar letezik. Hozzaadjuk a bemeno elemlistajahoz 
				 * a vegeredmeny drot elemlistajat
				 */				
				owner.GetWireByName(lvalue).objectsIn.addAll(assigned_wire.objectsIn);
				/*
				 *  most ertesitek minden olyan objektumot, amely az assigned_wire-hez csatlakozik
				 *  ugyanis az torlesre fog kerulni.
				 *  az objektumok kimenetenek a Composit drotjat adom meg
				 */
				for(int i=0;i<assigned_wire.objectsIn.size();i++){
					DigitalObject o = assigned_wire.objectsIn.get(i);
					/*
					 * ha talalok az objektumnal egy olyan drotot ami 
					 * az assigned_wire-hez csatlakozna veletlen, 
					 * azt lecserelem a Composit Wire elemere
					 */
					for(Wire w: o.wireOut){
						if(w==assigned_wire){
							o.wireOut.set(o.wireOut.indexOf(w), owner.GetWireByName(lvalue)) ;
						}
						
					}
				}
				owner.RemoveFromWireList(assigned_wire);
			}
			/*
			 *  Ha ez egy elem a Composit ComponentList, akkor elvileg neki nem lesz 
			 *  nicns wire-je amit fel kell szabaditani, egyszeruen csak hozzakotjuk
			 */
			else if(owner.GetElementByName(lvalue)!=null){
				owner.GetElementByName(lvalue).wireIn.add(assigned_wire);
				assigned_wire.SetConnection(owner.GetElementByName(lvalue), null);
				owner.AddToWireList(assigned_wire);	
			}
		}		
		return assigned_wire;
	}

	/**
	 * Infix2Postfix. Reverse Poland Notation
	 * Ez a szakasz biztosan mukodik, NE nyulj hozza
	 * @param InfixExpression Egy infix alaku kifejezes
	 * @return A kifejezes Postfixes alakja
	 */
	private static String Infix2Postfix(String InfixExpression){
		Stack<String> stack = new Stack<String>();
		//String PostfixExpression ="";
		StringBuffer buffer = new StringBuffer();
		
		String operandName ="";
		InfixExpression = InfixExpression.replaceAll(" ", "");
		for(int i = 0; i<InfixExpression.length();i++){
			String ch = InfixExpression.substring(i,i+1);
			if(!(isOperator(ch) || isLeftParental(ch) || isRightParental(ch))){
				operandName+=ch;
			}
			else if (isLeftParental(ch)) {
	                stack.push(ch);
			}
			else if (isOperator(ch)) {
				buffer.append(operandName+" ");
				operandName ="";
                if (stack.empty() )
                    stack.push(ch+" ");
                else if (isLeftParental(stack.peek()) )
                    stack.push(ch+" ");
                else {
                    while (!stack.empty() && !isLeftParental(stack.peek())
                        && precedence(stack.peek()) >= precedence(ch))
                        buffer.append(stack.pop());
                    stack.push(ch+" ");
                }
            }
            else if (isRightParental(ch)) {
            	buffer.append(operandName+" ");
            	operandName ="";
                while (!stack.empty() && !isLeftParental(stack.peek()))
                    buffer.append(stack.pop());
                if (isLeftParental(stack.peek()))
                    stack.pop();                
            }	
		}
		buffer.append(operandName+" ");
		 while (!stack.empty())
	            if (!isLeftParental(stack.peek()))
	                 buffer.append(stack.pop());
		 return buffer.toString().trim().replaceAll("  ", " ");
	}
    private static int precedence(String ch) {
    	int result = 0;
    	if(ch.equalsIgnoreCase("&") || ch.equalsIgnoreCase("|") ||ch.equalsIgnoreCase("!") )
    		result = 1;
    	return result;
    }
    private static int NumOfOperand(String ch) {
    	int result = 0;
    	if(ch.equalsIgnoreCase("&") || ch.equalsIgnoreCase("|") )
    		result = 2;
    	else if(ch.equalsIgnoreCase("!"))
    		result=1;
    	return result;
    }

    private static  boolean isOperator(String ch) {
       return (ch.equalsIgnoreCase("&") || ch.equalsIgnoreCase("|") || ch.equalsIgnoreCase("!"));  
    }
    private static boolean isLeftParental(String ch) {
        return ch.equalsIgnoreCase("(");
    }
    private static boolean isRightParental(String ch) {
    	return ch.equalsIgnoreCase(")");
    }
}
