import java.security.acl.Owner;
import java.util.Stack;
import java.util.regex.*;

public class bhdlParser {
	/**
	 * Fuggveny mely ellenori, talalhato-e egy adott szovegben a regularis
	 * kifejezesnek megfeleltetheto reszlet
	 * 
	 * @param expr
	 *            egy reguláris kifejezes
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
		// Kivesszük a Carrige Return és Line Feed elemeket
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
		// Kivesszük a Spaceket
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
	 * find_next_Composite:visszaadja a következõ Composite-ot a fájlból
	 * 
	 * @param source
	 *            :A fájl,ami Composite-kat tartalmaz
	 * @return A kivett Comopsite
	 * @author Peti
	 */
	public static String FindComposite(String source, String compname) {
		// Reguláris Kifejezés
		Pattern regexp = Pattern.compile("composit[ ]?" + compname + ".*?endcomposit;");
		// Megkeressük a találatokat
		Matcher match = regexp.matcher(source);
		// Elsõ kell
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
		// Reguláris Kifejezés
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
		// a composit letrehozasa
		Composit myComposit = new Composit("null", CompositName);
		return myComposit;
	}

	public static Composit ReadComposit(Composit ThisComposit, String source, String composit_name) {
		String[] CompositCommands = null; // Egy kompozitvban levo utasitasok
		CompositCommands = getCommands(FindComposite(source, composit_name));
		CommandParser(ThisComposit, source, CompositCommands);
		ThisComposit.buildHierarchy();
		ThisComposit.getFeedbacks();
		return ThisComposit;
	}

	/**
	 * Egy composit BHDL leirasabol tombbe rendezi a benne levo parancsokat
	 * 
	 * @param bhdlcomposit
	 *            a composit BHDL leirasa
	 * @return Egy String tomb, mely tartalmazza a parancsokat, soronkent, a
	 *         sorvei ";" jelet is beleertve
	 */
	public static String[] getCommands(String bhdlcomposit) {
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
	 * Ertelmez egy BHDL parancsot. A sorokra mintaillesztest vegez es ha talal
	 * egy mintat, a parancsnak megfeleloen jar el, hivj a fuggvenyeket
	 * @param Owner a composit, melynek a parancsait ertelmezzuk.
	 * @param source a teljes BHDL fajl
	 * @param commands egy lista a parancsokbol
	 */
	public static void CommandParser(Composit Owner, String source,
			String[] commands) {
		/*
		 * Kulonbozo regularis kifejezesek mely segitsegevel megkaphatoak a
		 * reszletek is
		 */
		String reg_wire = "wire[ ]+([\\w]+)?;";
		String reg_led = "led[ ]+([\\w]+)?;";
		String reg_oscilloscope = "oscilloscope[ ]+([\\w]+)(\\(([\\d]*)\\))*?;";
		String reg_generator = "generator[ ]+([\\w]+)?;";
		String reg_switch = "switch[ ]+([\\w]+);";
		String reg_andgate = "andgate[ ]+([\\w]+);";
		String reg_orgate = "orgate[ ]+([\\w]+);";
		String reg_inverter = "inverter[ ]+([\\w]+);";
		String reg_pin = "pin[ ]+([\\w]+);";
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
			}
			if (matching(reg_led, commands[i]) != null) {
				LED l=CreateLed(Owner, commands[i]);
				Owner.getFirstLevelOfComponentList().add(l);
				if(l.ID.startsWith("main"))
					System.out.println("create "+l.GetName()+" (LED)");
				else System.out.println("create "+l.ID.split("#")[1].trim()+"_"+l.GetName()+" (LED)");
			}
			if (matching(reg_oscilloscope, commands[i]) != null) {
				Oscilloscope o=CreateOscilloscope(Owner, commands[i]);
				Owner.getFirstLevelOfComponentList().add(o);
				if(o.ID.startsWith("main"))
					System.out.println("create "+o.GetName()+" (OSCILLOSCOPE)");
				else System.out.println("create "+o.ID.split("#")[1].trim()+"_"+o.GetName()+" (OSCILLOSCOPE)");
			}
			if (matching(reg_generator, commands[i]) != null) {
				GENERATOR g=CreateGenerator(Owner, commands[i]);
				Owner.getFirstLevelOfComponentList().add(g);
				if(g.ID.startsWith("main"))
					System.out.println("create "+g.GetName()+" (GENERATOR)");
				else System.out.println("create "+g.ID.split("#")[1].trim()+"_"+g.GetName()+" (GENERATOR)");
			}
			if (matching(reg_switch, commands[i]) != null) {
				SWITCH s=CreateSwitch(Owner, commands[i]);
				Owner.getFirstLevelOfComponentList().add(s);
				if(s.ID.startsWith("main"))
					System.out.println("create "+s.GetName()+" (SWITCH)");
				else System.out.println("create "+s.ID.split("#")[1].trim()+"_"+s.GetName()+" (SWITCH)");
			}
			if (matching(reg_andgate, commands[i]) != null) {
				ANDGate g=CreateAndGate(Owner, commands[i]);
				Owner.getFirstLevelOfComponentList().add(g);
				if(g.ID.startsWith("main"))
					System.out.println("create "+g.GetName()+" (ANDGate)");
				else System.out.println("create "+g.ID.split("#")[1].trim()+"_"+g.GetName()+" (ANDGate)");
			}
			if (matching(reg_orgate, commands[i]) != null) {
				ORGate g=CreateOrGate(Owner, commands[i]);
				Owner.getFirstLevelOfComponentList().add(g);
				if(g.ID.startsWith("main"))
					System.out.println("create "+g.GetName()+" (ORgate)");
				else System.out.println("create "+g.ID.split("#")[1].trim()+"_"+g.GetName()+" (ORGate)");
			}
			if (matching(reg_inverter, commands[i]) != null) {
				INVERTER inv=CreateInverter(Owner, commands[i]);
				Owner.getFirstLevelOfComponentList().add(inv);
				if(inv.ID.startsWith("main"))
					System.out.println("create "+inv.GetName()+" (INVERTER)");
				else System.out.println("create "+inv.ID.split("#")[1].trim()+"_"+inv.GetName()+" (INVERTER)");
			}
			if (matching(reg_pin, commands[i]) != null) {
				Owner.getFirstLevelOfComponentList().add(
						CreatePin(Owner, commands[i]));
				//System.out.println("PIN has been created.");
			}
			if (matching(reg_set, commands[i]) != null) {
				SettingElement(Owner, commands[i]);
				System.out.println("Setting element");
			}
			if (matching(reg_assign, commands[i]) != null) {
				assign(Owner, commands[i]);
				//System.out.println("Assign expression.");
			}
			if (matching(reg_comp, commands[i]) != null) {
				CreateComposit(Owner, source, commands[i]);
				System.out.println("Composit has been embedded.");
			}
		}
	}

	/**
	 * Letrehoz egy Wire-t, es hozza is adja a parameterben megkapott Composit
	 * WireList-jahoz
	 * 
	 * @param myComposit
	 *            Kompozit, melyen belul dolgozunk
	 * @param command
	 *            AZ aktualis parancs
	 * @return a letrehozott Wire
	 */
	private static Wire CreateWire(Composit owner, String command) {
		String CompositName = owner.GetName();
		String reg_wire = "wire[ ]+([\\w]+)?;";
		//String reg_wire = "w[ ]+([\\w]+)?;";
		Pattern regexp = Pattern.compile(reg_wire);
		Matcher match = regexp.matcher(command);

		match.find();
		if (match.matches()) {
			String wirename = match.group(1).trim();
			Wire myWire = new Wire(CompositName, wirename);
			owner.AddToWireList(myWire);
			return myWire;
		}
		return null;
	}

	/**
	 * Letrehoz egy LEDet.
	 * 
	 * @param myComposit
	 *            Kompozit, melyen belul dolgozunk
	 * @param command
	 *            AZ aktualis parancs
	 * @return a letrehozott LED
	 */
	private static LED CreateLed(Composit owner, String command) {
		String CompositName = owner.GetName();
		String reg_led = "led[ ]+([\\w]+)?;";
		Pattern regexp = Pattern.compile(reg_led);
		Matcher match = regexp.matcher(command);

		match.find();

		if (match.matches()) {
			String ledname = match.group(1).trim();
			LED myLED = new LED(CompositName, ledname);
			return myLED;
		}
		return null;
	}

	/**
	 * Letrehoz egy Oscilloscope-ot
	 * 
	 * @param myComposit
	 *            Kompozit, melyen belul dolgozunk
	 * @param command
	 *            AZ aktualis parancs
	 * @return a letrehozott elem
	 */
	private static Oscilloscope CreateOscilloscope(Composit owner,
			String command) {
		String CompositName = owner.GetName();
		String reg_oscilloscope = "oscilloscope[ ]+([\\w]+)(\\(([\\d]*)\\))*?;";
		Pattern regexp = Pattern.compile(reg_oscilloscope);
		Matcher match = regexp.matcher(command);

		match.find();

		if (match.matches()) {
			String oscname = match.group(1).trim();
			int samplesize = Integer.parseInt(match.group(3).trim());
			Oscilloscope myOscilloscope = new Oscilloscope(CompositName,
					oscname, samplesize);
			return myOscilloscope;
		}
		return null;
	}

	/**
	 * Letrehoz egy GENERATOR-t es a hozza tartozo kimeno drotot
	 * 
	 * @param myComposit
	 *            Kompozit, melyen belul dolgozunk
	 * @param command
	 *            AZ aktualis parancs
	 * @return a letrehozott elem
	 */
	private static GENERATOR CreateGenerator(Composit owner, String command) {
		String CompositName = owner.GetName();
		String reg_generator = "generator[ ]+([\\w]+)?;";
		Pattern regexp = Pattern.compile(reg_generator);
		Matcher match = regexp.matcher(command);

		match.find();

		if (match.matches()) {
			String genname = match.group(1).trim();
			GENERATOR myGenerator = new GENERATOR(CompositName, genname, 1, "00");
			return myGenerator;
		}
		return null;
	}

	/**
	 * Letrehoz egy SWITCH-et es a kimeno drotjat
	 * 
	 * @param myComposit
	 *            Kompozit, melyen belul dolgozunk
	 * @param command
	 *            AZ aktualis parancs
	 * @return a letrehozott elem
	 */
	private static SWITCH CreateSwitch(Composit owner, String command) {
		String CompositName = owner.GetName();
		String reg_switch = "switch[ ]+([\\w]+);";
		Pattern regexp = Pattern.compile(reg_switch);
		Matcher match = regexp.matcher(command);

		match.find();

		if (match.matches()) {
			String switchname = match.group(1).trim();
			SWITCH mySwitch = new SWITCH(CompositName, switchname);
			return mySwitch;
		}
		return null;
	}

	/**
	 * Letrehoz egy ANDGate-et es a kimeno drotjat
	 * 
	 * @param myComposit
	 *            Kompozit, melyen belul dolgozunk
	 * @param command
	 *            AZ aktualis parancs
	 * @return a letrehozott elem
	 */
	private static ANDGate CreateAndGate(Composit owner, String command) {
		String CompositName = owner.GetName();
		String reg_andgate = "andgate[ ]+([\\w]+);";
		Pattern regexp = Pattern.compile(reg_andgate);
		Matcher match = regexp.matcher(command);

		match.find();

		if (match.matches()) {
			String andgatename = match.group(1).trim();
			ANDGate myAndGate = new ANDGate(CompositName, andgatename);
			return myAndGate;
		}
		return null;
	}

	/**
	 * Letrehoz egy ORGate-et es a kimeno drotjat
	 * 
	 * @param myComposit
	 *            Kompozit, melyen belul dolgozunk
	 * @param command
	 *            AZ aktualis parancs
	 * @return a letrehozott elem
	 */
	private static ORGate CreateOrGate(Composit owner, String command) {
		String CompositName = owner.GetName();
		String reg_orgate = "orgate[ ]+([\\w]+);";
		Pattern regexp = Pattern.compile(reg_orgate);
		Matcher match = regexp.matcher(command);

		match.find();

		if (match.matches()) {
			String orgatename = match.group(1).trim();
			ORGate myOrGate = new ORGate(CompositName, orgatename);
			return myOrGate;
		}
		return null;
	}

	/**
	 * Letrehoz egy INVERTER-t es a kimeno drotjat
	 * 
	 * @param myComposit
	 *            Kompozit, melyen belul dolgozunk
	 * @param command
	 *            AZ aktualis parancs
	 * @return a letrehozott elem
	 */
	private static INVERTER CreateInverter(Composit owner, String command) {
		String CompositName = owner.GetName();
		String reg_inverter = "inverter[ ]+([\\w]+);";
		Pattern regexp = Pattern.compile(reg_inverter);
		Matcher match = regexp.matcher(command);

		match.find();

		if (match.matches()) {
			String invertername = match.group(1).trim();
			INVERTER myInverter = new INVERTER(CompositName, invertername);
			return myInverter;
		}
		return null;
	}
	
	/**
	 * Letrehoz egy INVERTER-t es a kimeno drotjat
	 * 
	 * @param myComposit
	 *            Kompozit, melyen belul dolgozunk
	 * @param command
	 *            AZ aktualis parancs
	 * @return a letrehozott elem
	 */
	private static PIN CreatePin(Composit owner, String command) {
		String CompositName = owner.GetName();
		String reg_pin = "pin[ ]+([\\w]+);";
		Pattern regexp = Pattern.compile(reg_pin);
		Matcher match = regexp.matcher(command);

		match.find();

		if (match.matches()) {
			String pinname = match.group(1).trim();
			PIN myPin = new PIN(CompositName, pinname);
			return myPin;
		}
		return null;
	}
	/**
	 * Letrehoz egy Compositot, es ertelmezi a tartalmat. REKURZIV eljaras!
	 * 
	 * @param owner
	 *            Egy composit, melybol a parancs meg lett hivva
	 * @param source
	 *            BHDL fajl
	 * @param command
	 *            egy composit hivasos eljaras (pl. : composit(in sw1,gen1 out
	 *            led1); )
	 * @return a letrehozott composit
	 */
	private static Composit CreateComposit(Composit owner, String source,
			String command) {
		// Lekerdezzuk a szulo nevet
		String OwnerName = owner.GetName();
		// Minta amely illeszkedik a composit hivasra
		String reg_comp = "([\\w, ]+)\\(in(( |[\\w]*,?)+)out(( |[\\w]*,?)+)\\)(.*)?";
		Pattern regexp = Pattern.compile(reg_comp);
		Matcher match = regexp.matcher(command);

		match.find();
		if (match.matches()) {
			// a letrehozni kivant composit neve BHDL szerint
			String comp_name = match.group(1).trim();
			// letrehozzuk a Compositot
			Composit myComposit = new Composit(OwnerName, comp_name);

			/*
			 * itt azt fogjuk vizsgalni, , hogy a szulo compositbol hogy nez ki
			 * a most letrehozando composit, azaz a szulo milyen drotokat kot be
			 * ill ki a letrehozando compositba/bol Ezekbol egy listat epitunk,
			 * eltaroljuk. A lista tartalma tehat lehet: egy szulo beli elem
			 * neve egy szulo beli wire neve
			 */

			String[] WiresIn = match.group(2).split(",");
			for (int i = 0; i < WiresIn.length; i++)
				WiresIn[i] = WiresIn[i].trim();
			String[] WiresOut = match.group(4).split(",");
			for (int i = 0; i < WiresOut.length; i++)
				WiresOut[i] = WiresOut[i].trim();

			/*
			 * Mivel azonban a most letrejott Compositban sajat nevek vannak,
			 * azokat meg kell keresni. Ehhez megvizsgaljuk a Composit
			 * leirasanak fejlecet. Mintaillesztest hasznalok, a kapott elemeket
			 * (kimeno, bemeno) egy listaba szervezem
			 */
			String[] HeaderWiresOut = null;
			String[] HeaderWiresIn = null;

			// a Composit headerjenek megkeresesehez hasznalt mintaillesztes
			String CompositeHeader = FindComposite(source, comp_name);
			String strHederRegexp = "([\\w, ]+)\\(in(( |[\\w]*,?)+)out(( |[\\w]*,?)+)\\)(.*)?;";
			Pattern HeaderRegexp = Pattern.compile(strHederRegexp);
			Matcher MatchingHeader = HeaderRegexp.matcher(CompositeHeader);

			MatchingHeader.find();
			// Ha talat megfelelo fejreszt
			if (MatchingHeader.matches()) {
				// Megvizsgaljuk az aszerinti bemeno drotokat...
				HeaderWiresIn = MatchingHeader.group(2).split(",");
				// Tisztogatunk
				for (int i = 0; i < HeaderWiresIn.length; i++)
					HeaderWiresIn[i] = HeaderWiresIn[i].trim();
				// .. es a kimenoket is
				HeaderWiresOut = MatchingHeader.group(4).split(",");
				// Tisztigatunk
				for (int i = 0; i < HeaderWiresOut.length; i++)
					HeaderWiresOut[i] = HeaderWiresOut[i].trim();

				/*
				 * A Composit fejlece alapjan megtalalt drotokat most letre is
				 * hozzuk. Mindegyiket hozzakotjuk a Composithoz es hozzadjuk a
				 * Composit listajahoz ezek a drotok a Compositon belul futnak.
				 */
				// Bemeno drotok
				for (String wire_in : HeaderWiresIn) {
					Wire w = new Wire(comp_name, wire_in);
					w.SetConnection(null, myComposit);
					myComposit.wireIn.add(w);
					//myComposit.AddToWireList(w);
				}
				// Kimeno drotok
				for (String wire_out : HeaderWiresOut) {
					Wire w = new Wire(comp_name, wire_out);
					w.SetConnection(myComposit, null);
					myComposit.wireOut.add(w);
					myComposit.AddToWireList(w);
				}

				/**
				 * REKURZIV FELDERITESE A TOBBI KOMPOZITNAK
				 * ReadComposit(myComposit,source,comp_name); *
				 */
				ReadComposit(myComposit, source, comp_name);
				owner.getFirstLevelOfComponentList().add(myComposit);

				/*
				 * Ez pedig ahogy kivulrol nez ki az osszekottetes A drotok a
				 * Compositig futnak csak, Composit beli elemhez nem
				 * csatlakoznak (null).
				 */
				// Bemeno drotok
				Wire w = null;
				for (int i = 0; i < WiresIn.length; i++) {
					if (owner.GetWireByName(WiresIn[i]) != null) {
						w = owner.GetWireByName(WiresIn[i]);
					} else if (owner.GetElementByName(WiresIn[i]) != null) {
						w = new Wire(owner.GetName());
						owner.GetElementByName(WiresIn[i]).wireOut.add(w);
						w.SetConnection(null,
								owner.GetElementByName(WiresIn[i]));
					}
					myComposit.wireIn.add(w);
					w.SetConnection(myComposit, null);
					owner.AddToWireList(w);
				}
				// Kimeno drotok
				for (int i = 0; i < WiresOut.length; i++) {
					if (owner.GetWireByName(WiresOut[i]) != null) {
						w = owner.GetWireByName(WiresOut[i]);
					} else if (owner.GetElementByName(WiresOut[i]) != null) {
						w = new Wire(owner.GetName());
						owner.GetElementByName(WiresOut[i]).wireIn.add(w);
						w.SetConnection(owner.GetElementByName(WiresOut[i]),
								null);
						owner.AddToWireList(w);
					}
					w.SetConnection(null, myComposit);
					myComposit.wireOut.add(w);
					owner.AddToWireList(w);
				}
			}

			// ReadComposit(myComposit,source,comp_name);
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
		String value = match.group(3).trim();
		String elementtype = owner.GetElementByName(elementname).GetType();
		if (elementtype.equalsIgnoreCase("SWITCH")) {
			owner.SetSwitch(Integer.parseInt(value), elementname);
			//TODO ezitt mia fene, emberek????!!!!!
			return true;
		} else if (elementtype.equalsIgnoreCase("GENERATOR")) {
			owner.SetSequence(value, elementname);
			return true;
		}

		return false;
	}

	private static Wire assign(Composit owner, String command) {
		// Ahooz, hogy az Assign reszeit be tujuk olvasni, reszletes
		// mintaillesztes
		String reg_assign = "assign[ ]+([\\w]+)=(.*?);";
		Pattern regexp = Pattern.compile(reg_assign);
		Matcher match = regexp.matcher(command);
		Wire assigned_wire = null;
		match.find();

		if (match.matches()) {
			String rvalue = match.group(2).trim();			
			String lvalue = match.group(1).trim();
			String postfix = Infix2Postfix(rvalue);
			/**
			 * DEBUGG KEZDETE
			 */
			//System.out.println("Assign expression. ("+lvalue+"="+postfix+")");

			String[] items = postfix.split(" ");
			Stack<Wire> WireStack = new Stack<Wire>();
						
			// Kiertekeljuk a PostFixes alakot
			for(String item:items){
				// ha nem operator
				if(!isOperator(item)){
					// megnezzuk, hogy Wire-e
					if(owner.GetWireByName(item)!=null){
						// ekkor egyszeruen csak hozzadjuk a veremhez
						WireStack.add(owner.GetWireByName(item));
					}
					// h ez egy DigiObject
					else if(owner.GetElementByName(item)!=null){
						/*
						 * Ha ez egy DigiObject, akkor ez csak egy 
						 * kapu lehet; Compositnak ugyanis tobb kimenete
						 * lehetne, igy nem lenn egyertelmu, hogy melyiekt keri
						 * ezert az nem szerpelhet assignban
						 * 
						 * De ez sem eleg am; meg kell nezni, hogy 
						 * a kapunak szerpelt-e mar kimenete a WireListben,
						 * ha nem, letrehozunk egy Wire-t, ha igen akkor csak hozzadjuk
						 * a veremhez (kapunakl egy kimeno Wire-je van)
						 */
						boolean WasIt = false;	// volt-e mar kimente a kapunak
						
						Wire GateOut = null;	// Ez lesz a kpau kimente, referencia
						// Csekkoljuk a kapu osszes kimenetet
						for(Wire w:owner.GetElementByName(item).wireOut){
							// ha mar volt a WireListben
							if(owner.GetWireByID(w.GetID())!=null){
								GateOut = owner.GetWireByID(w.GetID());
								WasIt=true;
							}
						}
						// ha meg nem volt, letre kell hozni
						if(!WasIt){
							GateOut= new Wire(owner.GetName());
							owner.AddToWireList(GateOut);
							owner.GetElementByName(item).wireOut.add(GateOut);	
						}
						// A kapcsolatokat mindenfelekelppen be kell allitani
						GateOut.SetConnection(null, owner.GetElementByName(item));
						WireStack.add(GateOut);
					}
				}
				// ha operator (& | !)
				else if(isOperator(item)){
					// meg kell neznunk, hogy hagy operandust igenyel
					if(NumOfOperand(item)==1){
						if(item.equalsIgnoreCase("!"))
						{
							Wire inv_in1 = WireStack.pop();	
						
							INVERTER myInverter = null;
							Wire inv_out = null;
							
							/* Meg kell nezni, hogy volt-e mar olyan
							 * kapu, aminek a mostani operandus a bemenete
							 * (es csak ez a bemenete)
							 * Ha van ilyen kapu akkor nem szabad ujat letrehozni
							 */
							boolean WasIt = false;
							// Kapukat vegignezzuk
							for(DigitalObject o:owner.getFirstLevelOfComponentList()){
								// Van-e ezek kozott Iverter, aminek van bemenete...
								if(o.GetType().equalsIgnoreCase("Inverter") && o.wireIn!=null){
									// ...es a mostani operandust tartalmazza
									if( o.wireIn.contains(inv_in1)){
										myInverter = (INVERTER) o;
										// kapuknak csak egy kimenete van..
										inv_out = myInverter.wireOut.get(0);
										WasIt = true;
									}
								}
							}	
							// ha nem volt ilyen kapu, akkor letrehozhatjuk
							if(!WasIt){
								myInverter = new INVERTER(owner.GetName(), inv_in1);
								inv_out = new Wire(owner.GetName());
								inv_in1.SetConnection(myInverter, null);							
								inv_out.SetConnection(null, myInverter);
								
								myInverter.AddOutput(inv_out);
								owner.AddToWireList(inv_out);
								owner.getFirstLevelOfComponentList().add(myInverter);
							}	
							// a kimenetet mindenfelekeppen hozzaadjuk
							WireStack.push(inv_out);
							/**
							 * DEBUG
							 */
							//System.out.println(myInverter.GetName()+" has been assigned to "+inv_in1.GetName()+" output is "+inv_out.GetName());
						}						
					}
					if(NumOfOperand(item)==2){
						if(item.equalsIgnoreCase("&"))
						{
							Wire and_in1 = WireStack.pop();	
							Wire and_in2 = WireStack.pop();	
							
							ANDGate myAnd=  null;
							Wire and_out =null;
							
							/* Meg kell nezni, hogy volt-e mar olyan
							 * ESkapu, aminek a mostani ket operandus a bemenetei lennenek
							 * Ha van ilyen kapu akkor nem szabad ujat letrehozni
							 */
							boolean WasIt = false;
							// olyan ES kapu,amihez ez a ket elem bemegy
							for(DigitalObject o:owner.getFirstLevelOfComponentList()){
								// Van-e ezek kozott Iverter, aminek van bemenete...
								if(o.GetType().equalsIgnoreCase("ANDGate") && o.wireIn!=null){
									// ...es a mostani operandusokat tartalmazza
									if( o.wireIn.contains(and_in1) && o.wireIn.contains(and_in2)){
										myAnd = (ANDGate) o;
										// kapuknak csak egy kimenete van..
										and_out = myAnd.wireOut.get(0);
										WasIt = true;
									}
								}
							}						

							if(!WasIt){
								myAnd=  new ANDGate(owner.GetName(), and_in1,and_in2);
								and_out = new Wire(owner.GetName());
								and_in1.SetConnection(myAnd, null);
								and_in2.SetConnection(myAnd, null);
								
								and_out.SetConnection(null, myAnd);							
								myAnd.AddOutput(and_out);
								owner.AddToWireList(and_out);
								owner.getFirstLevelOfComponentList().add(myAnd);
							}
							
							WireStack.push(and_out);
							/**
							 * DEBUG
							 */
							//System.out.println(myAnd.GetName()+" has been assigned to "+and_in1.GetName()+","+and_in2.GetName()+" output is "+and_out.GetName());
							
						}
						if(item.equalsIgnoreCase("|"))
						{
							Wire  or_in1 = WireStack.pop();	
							Wire  or_in2 = WireStack.pop();	
							
							ORGate myOr=  null;
							Wire or_out = null;
							/* Meg kell nezni, hogy volt-e mar olyan
							 * VAGYkapu, aminek a mostani ket operandus a bemenetei lennenek
							 * Ha van ilyen kapu akkor nem szabad ujat letrehozni
							 */
							boolean WasIt = false;
							// olyan ES kapu,amihez ez a ket elem bemegy
							for(DigitalObject o:owner.getFirstLevelOfComponentList()){
								// Van-e ezek kozott Iverter, aminek van bemenete...
								if(o.GetType().equalsIgnoreCase("ORGate") && o.wireIn!=null){
									// ...es a mostani operandusokat tartalmazza
									if( o.wireIn.contains(or_in1) && o.wireIn.contains(or_in2)){
										myOr = (ORGate) o;
										// kapuknak csak egy kimenete van..
										or_out = myOr.wireOut.get(0);
										WasIt = true;
									}
								}
							}						
							if(!WasIt){
								myOr=  new ORGate(owner.GetName(), or_in1,or_in2);
								or_out = new Wire(owner.GetName());
								
								or_in1.SetConnection(myOr, null);
								or_in2.SetConnection(myOr, null);
								
								or_out.SetConnection(null, myOr);							
								myOr.AddOutput(or_out);
								owner.AddToWireList(or_out);
								owner.getFirstLevelOfComponentList().add(myOr);
							}
							WireStack.push(or_out);
							/**
							 * DEBUG
							 */
							//System.out.println(myOr.GetName()+" has been assigned to "+or_in1.GetName()+","+or_in2.GetName()+" output is "+or_out.GetName());
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

				/**
				 * DEBUG
				 */
				//System.out.println("Finally, "+owner.GetWireByName(lvalue).GetName()+" has been assigned to "+assigned_wire.GetName());
				//owner.RemoveFromWireList(assigned_wire); EZ NEM KELL MERT SOHA NEM LETT HOZZAADVA a WIRELISThez
			}
			/*
			 *  Ha ez egy elem a Composit ComponentListbol, akkor elvileg neki nem lesz 
			 *  nicns wire-je amit fel kell szabaditani, egyszeruen csak hozzakotjuk
			 */
			else if(owner.GetElementByName(lvalue)!=null){
				owner.GetElementByName(lvalue).wireIn.add(assigned_wire);
				assigned_wire.SetConnection(owner.GetElementByName(lvalue), null);
				owner.AddToWireList(assigned_wire);	
				/**
				 * DEBUG
				 */
				//System.out.println("Finally, "+owner.GetElementByName(lvalue).GetName()+" has been assigned to "+assigned_wire.GetName());
			}
		}		
		return assigned_wire;
	}

	/**
	 * Infix2Postfix. Reverse Poland Notation Ez a szakasz biztosan mukodik, NE
	 * nyulj hozza
	 * 
	 * @param InfixExpression
	 *            Egy infix alaku kifejezes
	 * @return A kifejezes Postfixes alakja
	 */
	private static String Infix2Postfix(String InfixExpression) {
		Stack<String> stack = new Stack<String>();
		// String PostfixExpression ="";
		StringBuffer buffer = new StringBuffer();

		String operandName = "";
		InfixExpression = InfixExpression.replaceAll(" ", "");
		for (int i = 0; i < InfixExpression.length(); i++) {
			String ch = InfixExpression.substring(i, i + 1);
			if (!(isOperator(ch) || isLeftParental(ch) || isRightParental(ch))) {
				operandName += ch;
			} else if (isLeftParental(ch)) {
				stack.push(ch);
			} else if (isOperator(ch)) {
				buffer.append(operandName + " ");
				operandName = "";
				if (stack.empty())
					stack.push(ch + " ");
				else if (isLeftParental(stack.peek()))
					stack.push(ch + " ");
				else {
					while (!stack.empty() && !isLeftParental(stack.peek())
							&& precedence(stack.peek()) >= precedence(ch))
						buffer.append(stack.pop());
					stack.push(ch + " ");
				}
			} else if (isRightParental(ch)) {
				buffer.append(operandName + " ");
				operandName = "";
				while (!stack.empty() && !isLeftParental(stack.peek()))
					buffer.append(stack.pop());
				if (isLeftParental(stack.peek()))
					stack.pop();
			}
		}
		buffer.append(operandName + " ");
		while (!stack.empty())
			if (!isLeftParental(stack.peek()))
				buffer.append(stack.pop());
		return buffer.toString().trim().replaceAll("  ", " ");
	}

	private static int precedence(String ch) {
		int result = 0;
		if (ch.equalsIgnoreCase("&") || ch.equalsIgnoreCase("|")
				|| ch.equalsIgnoreCase("!"))
			result = 1;
		return result;
	}

	private static int NumOfOperand(String ch) {
		int result = 0;
		if (ch.equalsIgnoreCase("&") || ch.equalsIgnoreCase("|"))
			result = 2;
		else if (ch.equalsIgnoreCase("!"))
			result = 1;
		return result;
	}

	private static boolean isOperator(String ch) {
		return (ch.equalsIgnoreCase("&") || ch.equalsIgnoreCase("|") || ch
				.equalsIgnoreCase("!"));
	}

	private static boolean isLeftParental(String ch) {
		return ch.equalsIgnoreCase("(");
	}

	private static boolean isRightParental(String ch) {
		return ch.equalsIgnoreCase(")");
	}
}
