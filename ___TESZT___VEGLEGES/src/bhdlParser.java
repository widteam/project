import java.util.Stack;
import java.util.regex.*;

/**
 * a BHDL fajl beolvasasara szolgalo Class. Beolvas egy fajlt, ertelmezi azt,
 * felepiti a leirt aramkort
 * 
 * @author Tamas
 * 
 */
public class bhdlParser {
	/**
	 * DEBUG-hoz szukseges, osztalyra jellemzo valtozo. {@code true} eseten
	 * kulonbozo fuggvenyek hivasakor informaciot szolgaltat.
	 */
	// public static boolean DebugMode = false;

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
		// Kivesszuk a Carrige Return és Line Feed elemeket
		Logger.Log(Logger.log_type.ADDITIONAL, "Parser: cleaning source (CRLF)");
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

		Logger.Log(Logger.log_type.ADDITIONAL, "Parser: cleaning source (whitespaces)");
		
		source = source.replaceAll("([ \t]+)+", " ");
		source = source.replaceAll("\\( ", "\\(");
		source = source.replaceAll("\\) ", "\\)");
		source = source.replaceAll("= ", "=");
		source = source.replaceAll(" =", "=");
		source = source.replaceAll(" ;", ";");
		source = source.replaceAll("; ", ";");

		return source;
	}

	/**
	 * Egy megadott nevvel ellatott Composit kodjat nyeri ki a forrasbol
	 * 
	 * @param source
	 *            :A fajl,ami Composite-kat tartalmaz
	 * @return A kivett Comopsite ha talalt, vagy egy ures string
	 * @author Peti
	 */
	public static String FindComposite(String source, String compname) {
		Logger.Log(Logger.log_type.ADDITIONAL, "Parser: Searching for a composit ("+compname+")");
		
		// Regularis Kifejezes
		Pattern regexp = Pattern.compile("composit[ ]?" + compname
				+ ".*?endcomposit;");
		// Megkeressük a talalatokat
		Matcher match = regexp.matcher(source);
		String foundComposit = "";
		if (match.find())
			foundComposit = match.group();
		return foundComposit;
	}

	/**
	 * Megkeresi a main(in,out) szignoval ellatott compositot
	 * 
	 * @param source
	 * @return a composit BHDL leirasa ha talalt, ha nem ures String
	 */
	public static String FindMainComposit(String source) {
		Logger.Log(Logger.log_type.ADDITIONAL, "Parser: locate main composit");
		// Reguláris Kifejezés
		String regexp_ = "composit[ ]?(main)\\(in(( |[\\w]*,?)+),out(( |[\\w]*,?)+)\\)(.*)?endcomposit;";
		Pattern regexp = Pattern.compile(regexp_);
		Matcher match = regexp.matcher(source);
		String foundComposit = "";
		if (match.find())
			foundComposit = match.group();
		return foundComposit;
	}

	/**
	 * Letrehozza a main(in,out) compositot
	 * 
	 * @return a main composit
	 */
	public static Composit CreateMain(String composit) {
		// Egy regularis kifejezes ami illeszkedik egy composit elemre.
		String rege = "composit[ ]?(main)\\(in(( |[\\w]*,?)+),out(( |[\\w]*,?)+)\\)(.*)?endcomposit;";
		Pattern regexp = Pattern.compile(rege);
		// Megkeressuk a talalatokat
		Matcher match = regexp.matcher(composit);
		String CompositName = "";

		Composit myComposit = null;

		// ha volt talalat
		if (match.matches() && match.groupCount() > 1) {
			CompositName = match.group(1);
			// a composit letrehozasa
			myComposit = new Composit("null", CompositName);
		}
		return myComposit;
	}

	/**
	 * Egy Composit tartalmat olvassa be es ertelmezi.
	 * 
	 * @param ThisComposit
	 *            Referencia, melyik Composit-bol hivtuk a fuggvenyt
	 * @param source
	 *            A teljes BHDL fajl (tisztitva)
	 * @param composit_name
	 *            a Composit, melyiket be akarjuk olvasni
	 * @return A beolvasott, elemekkel feltoltott, rendezett Composit
	 * @throws ExceptionWrongBoard
	 *             Rosszul formazott BHDL fajl
	 */
	public static Composit ReadComposit(Composit ThisComposit, String source,
			String composit_name) throws ExceptionWrongBoard {
		Logger.Log(Logger.log_type.ADDITIONAL, "Parser: Reading composit's content ("+ThisComposit.GetID()+")");
		
		String[] CompositCommands = null; // Egy kompozitvban levo utasitasok
		CompositCommands = getCommands(FindComposite(source, composit_name));
		try {
			CommandParser(ThisComposit, source, CompositCommands);
		} catch (Exception e) {
			throw new ExceptionWrongBoard(ThisComposit);
		}
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
		String rege = "composit[ ]?([\\w ]+)\\(in(( |[\\w]*,?)+),[ ]?out(( |[\\w]*,?)+)\\)(.*)?endcomposit;";
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
	 * 
	 * @param Owner
	 *            a composit, melynek a parancsait ertelmezzuk.
	 * @param source
	 *            A teljes BHDL fajl
	 * @param commands
	 *            egy lista a parancsokbol
	 * @throws ExceptionWrongBoard
	 */
	public static void CommandParser(Composit Owner, String source,
			String[] commands) throws ExceptionWrongBoard {
		/*
		 * Kulonbozo regularis kifejezesek mely segitsegevel megkaphatoak a
		 * reszletek is
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
			}
			if (matching(reg_led, commands[i]) != null) {
				Owner.getFirstLevelOfComponentList().add(
						CreateLed(Owner, commands[i]));
			}
			if (matching(reg_oscilloscope, commands[i]) != null) {
				try {
					Owner.getFirstLevelOfComponentList().add(
							CreateOscilloscope(Owner, commands[i]));
				} catch (ExceptionWrongParameter e) {
					throw new ExceptionWrongBoard(Owner);
				}				
			}
			if (matching(reg_generator, commands[i]) != null) {
				try {
					Owner.getFirstLevelOfComponentList().add(
							CreateGenerator(Owner, commands[i]));
				} catch (ExceptionWrongParameter e) {
					throw new ExceptionWrongBoard(Owner);
				}
			}
			if (matching(reg_switch, commands[i]) != null) {
				Owner.getFirstLevelOfComponentList().add(
						CreateSwitch(Owner, commands[i]));
			}
			if (matching(reg_set, commands[i]) != null) {
				try {
					SettingElement(Owner, commands[i]);
				} catch (Exception e) {
					throw new ExceptionWrongBoard(Owner);
				}
			}
			if (matching(reg_assign, commands[i]) != null) {
				try {
					assign(Owner, commands[i]);
				} catch (ExceptionWireHasMultipleInputs e) {
					throw new ExceptionWrongBoard(Owner);
				}
			}
			if (matching(reg_comp, commands[i]) != null) {
				try {
					Owner.getFirstLevelOfComponentList().add(
							CreateComposit(Owner, source, commands[i]));
				} catch (ExceptionWireHasMultipleInputs e) {
					throw new ExceptionWrongBoard(Owner);
				}

			}
		}
	}

	/**
	 * Letrehoz egy Wire-t
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
		Pattern regexp = Pattern.compile(reg_wire);
		Matcher match = regexp.matcher(command);

		match.find();
		if (match.matches()) {
			String wirename = match.group(1).trim();
			Wire myWire = new Wire(CompositName, wirename);
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
	 * @throws ExceptionWrongParameter 
	 */
	private static Oscilloscope CreateOscilloscope(Composit owner,
			String command) throws ExceptionWrongParameter {
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
	 * Letrehoz egy GENERATOR-t
	 * 
	 * @param myComposit
	 *            Kompozit, melyen belul dolgozunk
	 * @param command
	 *            AZ aktualis parancs
	 * @return a letrehozott elem
	 * @throws ExceptionWrongParameter 
	 */
	private static GENERATOR CreateGenerator(Composit owner, String command) throws ExceptionWrongParameter {
		String CompositName = owner.GetName();
		String reg_generator = "generator[ ]+([\\w]+)?;";
		Pattern regexp = Pattern.compile(reg_generator);
		Matcher match = regexp.matcher(command);

		match.find();

		if (match.matches()) {
			String genname = match.group(1).trim();
			GENERATOR myGenerator = new GENERATOR(CompositName, genname, 1,
					"11001100");
			return myGenerator;
		}
		return null;
	}

	/**
	 * Letrehoz egy SWITCH-et
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

	private static Wire assign(Composit owner, String command)
			throws ExceptionWireHasMultipleInputs {
		// Ahhoz, hogy az Assign reszeit be tujuk olvasni, reszletes
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
			Logger.Log(Logger.log_type.ADDITIONAL, "Parser: Assign expression founded. (Postix: "+lvalue+"="+postfix+")");
			
			String[] items = postfix.split(" ");
			Stack<Wire> WireStack = new Stack<Wire>();

			// Kiertekeljuk a PostFixes alakot
			for (String item : items) {
				// ha nem operator
				if (!(isOperator(item) || isInverter(item))) {
					// megnezzuk, hogy Wire-e
					if (owner.GetWireByName(item) != null) {
						// ekkor egyszeruen csak hozzadjuk a veremhez
						WireStack.add(owner.GetWireByName(item));
					}
					// ha ez egy DigiObject
					else if (owner.GetElementByName(item) != null) {
						/*
						 * Ha ez egy DigiObject, akkor ez csak egy kapu lehet;
						 * Compositnak ugyanis tobb kimenete lehetne, igy nem
						 * lenn egyertelmu, hogy melyiekt keri ezert az nem
						 * szerpelhet assignban
						 * 
						 * De ez sem eleg am; meg kell nezni, hogy a kapunak
						 * szerpelt-e mar kimenete a WireListben, ha nem,
						 * letrehozunk egy Wire-t, ha igen akkor csak hozzadjuk
						 * a veremhez (kapunakl egy kimeno Wire-je van)
						 */
						boolean WasIt = false; // volt-e mar kimente a kapunak

						Wire GateOut = null; // Ez lesz a kpau kimente,
												// referencia
						// Csekkoljuk a kapu osszes kimenetet
						for (Wire w : owner.GetElementByName(item).wireOut) {
							// ha mar volt a WireListben
							if (owner.GetWireByID(w.GetID()) != null) {
								GateOut = owner.GetWireByID(w.GetID());
								WasIt = true;
							}
						}
						// ha meg nem volt, letre kell hozni
						if (!WasIt) {
							GateOut = new Wire(owner.GetName());
							owner.AddToWireList(GateOut);
							owner.GetElementByName(item).wireOut.add(GateOut);
						}
						// A kapcsolatokat mindenfelekelppen be kell allitani
						GateOut.SetConnection(null,
								owner.GetElementByName(item));
						WireStack.add(GateOut);
					}
				}
				// ha operator (& | !)
				else {
					// meg kell neznunk, hogy hagy operandust igenyel
					if (NumOfOperand(item) == 1) {
						if (item.equalsIgnoreCase("!")) {
							Wire inv_in1 = WireStack.pop();

							INVERTER myInverter = null;
							Wire inv_out = null;

							/*
							 * Meg kell nezni, hogy volt-e mar olyan kapu,
							 * aminek a mostani operandus a bemenete (es csak ez
							 * a bemenete) Ha van ilyen kapu akkor nem szabad
							 * ujat letrehozni
							 */
							boolean WasIt = false;
							// Kapukat vegignezzuk
							for (DigitalObject o : owner
									.getFirstLevelOfComponentList()) {
								// Van-e ezek kozott Iverter, aminek van
								// bemenete...
								if (o.GetType().equalsIgnoreCase("Inverter")
										&& o.wireIn != null) {
									// ...es a mostani operandust
									// tartalmazza
									if (o.wireIn.contains(inv_in1)) {
										myInverter = (INVERTER) o;
										// kapuknak csak egy kimenete van..
										inv_out = myInverter.wireOut.get(0);
										WasIt = true;
									}
								}
							}
							// ha nem volt ilyen kapu, akkor letrehozhatjuk
							if (!WasIt) {
								myInverter = new INVERTER(owner.GetName(),
										inv_in1);
								inv_out = new Wire(owner.GetName());
								inv_in1.SetConnection(myInverter, null);
								inv_out.SetConnection(null, myInverter);

								myInverter.AddOutput(inv_out);
								owner.AddToWireList(inv_out);
								owner.getFirstLevelOfComponentList().add(
										myInverter);
							}
							// a kimenetet mindenfelekeppen hozzaadjuk
							WireStack.push(inv_out);

						}
					}
					if (NumOfOperand(item) == 2) {
						if (item.equalsIgnoreCase("&")) {
							Wire and_in1 = WireStack.pop();
							Wire and_in2 = WireStack.pop();

							ANDGate myAnd = null;
							Wire and_out = null;

							/*
							 * Meg kell nezni, hogy volt-e mar olyan ESkapu,
							 * aminek a mostani ket operandus a bemenetei
							 * lennenek Ha van ilyen kapu akkor nem szabad ujat
							 * letrehozni
							 */
							boolean WasIt = false;
							// olyan ES kapu,amihez ez a ket elem bemegy
							for (DigitalObject o : owner
									.getFirstLevelOfComponentList()) {
								// Van-e ezek kozott Iverter, aminek van
								// bemenete...
								if (o.GetType().equalsIgnoreCase("ANDGate")
										&& o.wireIn != null) {
									// ...es a mostani operandusokat
									// tartalmazza
									if (o.wireIn.contains(and_in1)
											&& o.wireIn.contains(and_in2)) {
										myAnd = (ANDGate) o;
										// kapuknak csak egy kimenete van..
										and_out = myAnd.wireOut.get(0);
										WasIt = true;
									}
								}
							}

							if (!WasIt) {
								myAnd = new ANDGate(owner.GetName(), and_in1,
										and_in2);
								and_out = new Wire(owner.GetName());
								and_in1.SetConnection(myAnd, null);
								and_in2.SetConnection(myAnd, null);

								and_out.SetConnection(null, myAnd);
								myAnd.AddOutput(and_out);
								owner.AddToWireList(and_out);
								owner.getFirstLevelOfComponentList().add(myAnd);
							}

							WireStack.push(and_out);
						}
						if (item.equalsIgnoreCase("|")) {
							Wire or_in1 = WireStack.pop();
							Wire or_in2 = WireStack.pop();

							ORGate myOr = null;
							Wire or_out = null;
							/*
							 * Meg kell nezni, hogy volt-e mar olyan VAGYkapu,
							 * aminek a mostani ket operandus a bemenetei
							 * lennenek Ha van ilyen kapu akkor nem szabad ujat
							 * letrehozni
							 */
							boolean WasIt = false;
							// olyan ES kapu,amihez ez a ket elem bemegy
							for (DigitalObject o : owner
									.getFirstLevelOfComponentList()) {
								// Van-e ezek kozott Iverter, aminek van
								// bemenete...
								if (o.GetType().equalsIgnoreCase("ORGate")
										&& o.wireIn != null) {
									// ...es a mostani operandusokat
									// tartalmazza
									if (o.wireIn.contains(or_in1)
											&& o.wireIn.contains(or_in2)) {
										myOr = (ORGate) o;
										// kapuknak csak egy kimenete van..
										or_out = myOr.wireOut.get(0);
										WasIt = true;
									}
								}
							}
							if (!WasIt) {
								myOr = new ORGate(owner.GetName(), or_in1,
										or_in2);
								or_out = new Wire(owner.GetName());

								or_in1.SetConnection(myOr, null);
								or_in2.SetConnection(myOr, null);

								or_out.SetConnection(null, myOr);
								myOr.AddOutput(or_out);
								owner.AddToWireList(or_out);
								owner.getFirstLevelOfComponentList().add(myOr);
							}
							WireStack.push(or_out);
						}
					}// end: operandusok szama
				}// end if: ha operator
			}// end for: lista bejarasa, postfix kiertekel

			Logger.Log(Logger.log_type.ADDITIONAL, "Parser: Assigning the result...");
			// ez a "vegeredmeny" ez a drot fog csatlakozni az lvaluehoz
			assigned_wire = WireStack.pop();
			// ez pedig a legutolso kapu ami adja az erteket
			DigitalObject LastObject = assigned_wire.objectsIn.get(0);

			/*
			 * Ez itt a legnehezebb es legbonyolultabb resz. Majdnem minden
			 * esetet feltuntettem, mar csak azert is, hogy magam kevesse
			 * zavarodjak bele...
			 * 
			 * - drotnak csak egy bemenete lehet. - kapunak lehetoseg szerint
			 * egy kimeno drotja legyen, de lehet tobb is - ha elertun ka
			 * Composit szelehez azt kulon neznunk kell
			 */
			/* HA PIN-hez akarjuk csatlakoztatni a legutolso objektumot */
			if (LastObject.GetType().equalsIgnoreCase("PIN")) {
				PIN pin = (PIN) LastObject;
				/* Ha ez egy Compositba bemeno PIN */
				if (pin.ContainerComposit.pins_in.contains(pin)) {
					if (owner.GetWireByName(lvalue) != null) {
						Wire LValueWire = owner.GetWireByName(lvalue);
						if (pin.wireOut.objectsOut != null
								&& !pin.wireOut.objectsOut.isEmpty()) {
							// Ilyen elvileg nem lehet, hiszen minden bemeno
							// drot kap PIN-t
						} else {// ha meg a PIN droja nincs csatlakoztatva
							owner.RemoveFromWireList(pin.wireOut); // eltavolitjuk
																	// a
																	// csonkot
							pin.wireOut = LValueWire; // PIN-t hozzakotjuk a
														// drothoz
							LValueWire.SetConnection(pin, null); // a Drotot
																	// a
																	// PINhez
						}
					} else if (owner.GetElementByName(lvalue) != null) {
						DigitalObject LValueObject = owner
								.GetElementByName(lvalue);
						if (pin.wireOut == null) {
							// Ilyen elvileg nem lehet, hiszen minden PIN
							// kap
							// drotot
						} else {
							pin.wireOut.SetConnection(LValueObject, null);
							LValueObject.wireIn.add(pin.wireOut);
						}
					}
					/* Ha ez egy Compositbol kimeno PIN */
				} else if (pin.ContainerComposit.pins_out.contains(pin)) {
					if (owner.GetWireByName(lvalue) != null) {
						Wire LValueWire = owner.GetWireByName(lvalue);
						if (pin.wireOut != null) {
							if (pin.wireOut.objectsOut != null
									&& !pin.wireOut.objectsOut.isEmpty()) {
								// elvileg ilyet nem kene kapnunk
							} else {
								owner.RemoveFromWireList(pin.wireOut); // eltavolitjuk
																		// a
																		// csonkot
								pin.wireOut = LValueWire; // PIN-t
															// hozzakotjuk a
															// drothoz
								LValueWire.SetConnection(pin, null); // a
																		// Drotot
																		// a
																		// PINhez
							}

						} else {
							pin.wireOut = LValueWire;
						}
					} else if (owner.GetElementByName(lvalue) != null) {
						DigitalObject LValueObject = owner
								.GetElementByName(lvalue);
						if (pin.wireOut != null) {
							pin.wireOut.SetConnection(LValueObject, null);
							LValueObject.wireIn.add(pin.wireOut);
						} else {
							// elvileg ilyet nem kene kapnunk
						}
					}
				} else {
					// throw HIBAValamit
				}

			} else {
				if (owner.GetWireByName(lvalue) != null) {
					Wire LValueWire = owner.GetWireByName(lvalue);
					if (LValueWire.objectsIn != null
							&& !LValueWire.objectsIn.isEmpty()) {
						LValueWire.GetID();
						// HIBA
					} else if (LValueWire.objectsIn != null
							&& LValueWire.objectsIn.isEmpty()) {

						boolean connected_to_pin_out = false;
						PIN ThePin = null;
						for (PIN p : owner.pins_out) {
							if (p.wireIn == LValueWire) {
								connected_to_pin_out = true;
								ThePin = p;
								break;
							}
						}
						if (connected_to_pin_out) {
							ThePin.wireIn = assigned_wire;
							assigned_wire.SetConnection(ThePin, null);
							owner.RemoveFromWireList(LValueWire);
						} else {
							LastObject.wireOut.add(LValueWire);
							LValueWire.SetConnection(null, LastObject);
						}
					}

				} else if (owner.GetElementByName(lvalue) != null) {
					DigitalObject LValueObject = owner.GetElementByName(lvalue);
					assigned_wire.SetConnection(LValueObject, null);
					LValueObject.wireIn.add(assigned_wire);

				}
			}
		}

		return assigned_wire;
	}

	/**
	 * Infix2Postfix. Reverse Poland Notation Ez a szakasz biztosan mukodik, NE
	 * nyulj hozza Mivel a !(Inverter) aritmetikaja egy picit mas, modositasokat
	 * igenyelt az ismert algoritmus.
	 * 
	 * @param InfixExpression
	 *            Egy infix alaku kifejezes
	 * @return A kifejezes Postfixes alakja
	 */
	private static String Infix2Postfix(String InfixExpression) {
		InfixExpression = InfixExpression.replaceAll("[!]", " ! ");
		InfixExpression = InfixExpression.replaceAll("[|]", " | ");
		InfixExpression = InfixExpression.replaceAll("[&]", " & ");

		InfixExpression = InfixExpression.replaceAll("\\(", " ( ");
		InfixExpression = InfixExpression.replaceAll("\\)", " ) ");
		InfixExpression = InfixExpression.replaceAll("[ ]+", " ");
		String[] Source = InfixExpression.trim().split(" ");
		Stack<String> stack = new Stack<String>();
		Stack<String> stack_inv = new Stack<String>();

		StringBuffer buffer = new StringBuffer();

		for (String tag : Source) {
			if (isOperandChar(tag)) {
				buffer.append(tag + " ");
				while (!stack_inv.empty() && !isLeftParental(stack_inv.peek())) {
					buffer.append(stack_inv.pop() + " ");
				}
			} else if (isInverter(tag)) {
				stack_inv.push(tag);
			} else if (isLeftParental(tag)) {
				stack_inv.push(tag);
				stack.push(tag);
			} else if (isOperator(tag)) {
				if (stack.empty())
					stack.push(tag);
				else if (isLeftParental(stack.peek()))
					stack.push(tag);
				else {
					while (!stack.empty() && !isLeftParental(stack.peek())
							&& Precedence(stack.peek()) >= Precedence(tag)) {
						buffer.append(stack.pop() + " ");
					}
					stack.push(tag);

				}
			} else if (isRightParental(tag)) {
				while (!stack_inv.empty() && !isLeftParental(stack_inv.peek())) {
					buffer.append(stack_inv.pop() + " ");
				}
				if (isLeftParental(stack_inv.peek())) {
					stack_inv.pop();
				}
				while (!stack.empty() && !isLeftParental(stack.peek())) {
					buffer.append(stack.pop() + " ");
				}
				if (isLeftParental(stack.peek())) {
					stack.pop();
				}
			}
		}
		while (!stack.empty() || !stack_inv.empty()) {
			if (!stack_inv.empty() && !isLeftParental(stack_inv.peek())) {
				buffer.append(stack_inv.pop());
			}
			if (!stack.empty() && !isLeftParental(stack.peek())) {
				buffer.append(stack.pop());
			}
		}

		return buffer.toString().trim().replaceAll("[ ]+", " ");
	}

	private static int Precedence(String ch) {
		int result = 0;
		if (ch.equalsIgnoreCase("&") || ch.equalsIgnoreCase("|"))
			result = 2;
		else if (ch.equalsIgnoreCase("!"))
			result = 3;
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

	private static boolean isOperandChar(String ch) {
		return !(isOperator(ch) || isLeftParental(ch) || isRightParental(ch) || isInverter(ch));
	}

	private static boolean isOperator(String ch) {
		return (ch.equalsIgnoreCase("&") || ch.equalsIgnoreCase("|"));
	}

	private static boolean isInverter(String ch) {
		return (ch.equalsIgnoreCase("!"));
	}

	private static boolean isLeftParental(String ch) {
		return ch.equalsIgnoreCase("(");
	}

	private static boolean isRightParental(String ch) {
		return ch.equalsIgnoreCase(")");
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
	 * @throws ExceptionWireHasMultipleInputs
	 * @throws ExceptionWrongBoard
	 *             Rosszul formazott BHDL
	 */
	private static Composit CreateComposit(Composit owner, String source,
			String command) throws ExceptionWireHasMultipleInputs,
			ExceptionWrongBoard {
		// Lekerdezzuk a szulo nevet
		String OwnerName = owner.GetName();
		// Minta amely illeszkedik a composit hivasra
		String reg_comp = "([\\w, ]+)\\(in(( |[\\w]*,?)+)out(( |[\\w]*,?)+)\\)(.*?);";
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

				Wire PinWire = null;
				;

				// Bemeno drotok
				for (String wire_in : HeaderWiresIn) {
					// Letrehozzuk a PIN-t
					PIN pin = new PIN(myComposit, "PIN" + wire_in);
					myComposit.pins_in.add(pin);

					PinWire = new Wire(myComposit.GetName(), wire_in);
					PinWire.SetConnection(null, pin);
					pin.wireOut = PinWire;
					myComposit.AddToWireList(PinWire);

				}
				// Kimeno drotok

				for (String wire_out : HeaderWiresOut) {
					// Letrehozzuk a PIN-t
					PIN pin = new PIN(myComposit, "PIN" + wire_out);
					myComposit.pins_out.add(pin);

					PinWire = new Wire(myComposit.GetName(), wire_out);
					PinWire.SetConnection(pin, null);
					pin.wireIn = PinWire;
					myComposit.AddToWireList(PinWire);
				}

				/**
				 * REKURZIV FELDERITESE A TOBBI KOMPOZITNAK
				 * ReadComposit(myComposit,source,comp_name); *
				 */
				ReadComposit(myComposit, source, comp_name);

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
						// ezt a drotot a szulo listajahoz adjuk, hiszen a szulo
						// composit alapjan keszullt el
						owner.AddToWireList(w);
					}
					// viszont a pin listahoz, megpedig a bemeno pin listahoz
					// hozzadjuk
					myComposit.pins_in.get(i).wireIn = w;
					// megadjuk melyik PINhez csatlakozzek
					w.SetConnection(myComposit.pins_in.get(i), null);

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
						// ezt a drotot a szulo listajahoz adjuk, hiszen a szulo
						// composit alapjan keszullt el
						owner.AddToWireList(w);
					}
					// viszont a pin listahoz, megpedig a bemeno pin listahoz
					// hozzadjuk
					myComposit.pins_out.get(i).wireOut = w;
					// megadjuk melyik PINhez csatlakozzek
					w.SetConnection(null, myComposit.pins_out.get(i));

				}
			}
			Logger.Log(Logger.log_type.ADDITIONAL, "Composit ("+myComposit.GetID()+") was created.");
			return myComposit;
		}
		return null;
	}

	/**
	 * Elvileg lehetoseg van a BHDL fajlban is megadni az Inputok kezdo
	 * ertekeit...
	 * 
	 * @param owner
	 *            a Composit, melyben a parancsot talaltuk
	 * @param command
	 *            maga a prancs
	 * @return true, ha allitott az elemn, false egyebkent
	 * @throws ExceptionObjectNotFound
	 * @throws NumberFormatException
	 */
	private static boolean SettingElement(Composit owner, String command)
			throws NumberFormatException, ExceptionObjectNotFound {
		String reg_set = "set[ ]+([\\w]+)=([\\d]+);";
		Pattern regexp = Pattern.compile(reg_set);
		Matcher match = regexp.matcher(command);

		match.find();
		String elementname = match.group(1).trim();
		String value = match.group(2).trim();
		String elementtype = owner.GetElementByName(elementname).GetType();
		if (elementtype.equalsIgnoreCase("SWITCH")) {
			owner.SetSwitch(Integer.parseInt(value),
					owner.GetElementByName(elementname).GetID());
			return true;
		} else if (elementtype.equalsIgnoreCase("GENERATOR")) {
			owner.SetSequence(value, owner.GetElementByName(elementname)
					.GetID());
			return true;
		}

		return false;
	}
}