import java.util.List;
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
	public static String find_next_Composite(String source) {
		// Reguláris Kifejezés
		Pattern regexp = Pattern.compile("composit.*?endcomposit;");
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
	 * @return
	 */
	public static String FindMainComposite(String source) {
		// Reguláris Kifejezés
		String regexp_ = "composit[ ]?(main)\\(in(( |[\\w]*,?)+),out(( |[\\w]*,?)+)\\)(.*)?endcomposit;";
		Pattern regexp = Pattern.compile(regexp_);
		Matcher match = regexp.matcher(source);
		match.find();
		String foundComposit = match.group();
		return foundComposit;
	}
	/**
	 * egy BHDL leiras alapjan letrehoz egy Composit elemet
	 * NEM tolti fel a belso listakat!!! Csak konstrual egy compositot
	 * @param composit
	 *            Egy composit BHDL leirasa
	 * @return
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
		Composit myComposit = new Composit("",CompositName);
		return myComposit;
	}
	public static Composit ReadComposit(Composit owner, String composit) {
		// Egy regularis kifejezes ami illeszkedik egy composit elemre.
		String rege = "composit[ ]?(main)\\(in(( |[\\w]*,?)+),out(( |[\\w]*,?)+)\\)(.*)?endcomposit;";
		Pattern regexp = Pattern.compile(rege);
		// Megkeressuk a talalatokat
		Matcher match = regexp.matcher(composit);
		String CompositName = "";
		String CompositeContent="";
		Composit ThisComposit =null;
		String[] CompositCommands = null;	// Egy kompozitvban levo utasitasok
		// keresunk
		match.find();
		// ha volt talalat
		if (match.matches() && match.groupCount() > 1) {
			CompositName = match.group(1);
			ThisComposit = (Composit) owner.GetElementByName(CompositName);
			CompositeContent = match.group(6);
			
		}
		CompositCommands = getCommands(CompositeContent);
		CommandParser(ThisComposit, CompositCommands,ThisComposit.getFirstLevelOfComponentList());
		return ThisComposit;
	}
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
	 * 
	 * @param myComposit a composit, melynek a parancsait ertelmezzuk
	 * @param commands egy lista a parancsokbol
	 * @param elementlist egy DigitalObjectekbol szervezett lista, melyet a parancsok alapjan epit fel s majd ezen vegzi a hierarchiat
	 */
	public static void CommandParser(Composit Owner, String[] commands,List<DigitalObject> elementlist) {
		/*
		 * Kulonbozo regularis kifejezesek mely segitsegevel megkaphatoak a reszletek is
		 */
		String reg_wire = "wire[ ]+([\\w]+)?;";
		String reg_led = "led[ ]+([\\w]+)?";
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
				assign(Owner,commands[i],elementlist);
				System.out.println("Assign expression.");
			}
			if (matching(reg_comp, commands[i]) != null) {
				CreateComposit(Owner,commands[i]);
				System.out.println("Composit  has been embedded.");
			}
		}
	}
	
	private static Composit CreateComposit(Composit owner, String command) {
		String OwnerName = owner.ID;
		String reg_comp = "([\\w, ]+)\\(in(( |[\\w]*,?)+)out(( |[\\w]*,?)+)\\)(.*)?;";
		Pattern regexp = Pattern.compile(reg_comp);
		Matcher match = regexp.matcher(command);

		match.find();
		if(match.matches()){
			String comp_name = match.group(1).trim();
			String[] WiresIn = match.group(2).split(",");
				for(int i=0; i<WiresIn.length;i++)WiresIn[i]=WiresIn[i].trim();
			String[] WiresOut= match.group(4).split(",");
				for(int i=0; i<WiresOut.length;i++)WiresOut[i]=WiresOut[i].trim();
			
			/*
			 * A bemeno parameterekkel valo osszekotese
			 */
			Composit myComposit = new Composit(OwnerName,comp_name);
			
			for(String wire_in:WiresIn){
				Wire w=null;
				if(owner.GetWireByName(wire_in)!=null){
					w = owner.GetWireByName(wire_in);
				}else if(owner.GetElementByName(wire_in)!=null){
					w = owner.GetElementByName(wire_in).wireOut.get(0);
				}else{
					// throw new UnknownException(",,,,");
				}
				if(w!=null){
					w.SetConnection(myComposit, null);
					myComposit.wireIn.add(w);	
					myComposit.AddToWireList(w);
				}
			}
			for(String wire_out:WiresOut){
				Wire w=null;
				if(owner.GetWireByName(wire_out)!=null){
					w = owner.GetWireByName(wire_out);
				}else if(owner.GetElementByName(wire_out)!=null){
					w = owner.GetElementByName(wire_out).wireOut.get(0);
				}else{
					// throw new UnknownException(",,,,");
				}
				if(w!=null){
					w.SetConnection(myComposit, null);
					myComposit.wireIn.add(w);	
					myComposit.AddToWireList(w);
				}
			}
			
			ReadComposit(null,comp_name);
			return myComposit;
		}
		return null;
	}
	/**
	 * Letrehoz egy Wire-t, es hozza is adja a parameterben megkapott Composit
	 * WireList-jahoz
	 * @param myComposit Kompozit, melyen belul dolgozunk
	 * @param command AZ aktualis parancs
	 * @return a letrehozott Wire
	 */
	private static Wire CreateWire(Composit myComposit, String command) {
		String CompositName = myComposit.ID;
		String reg_wire = "wire[ ]+([\\w]+)?;";
		Pattern regexp = Pattern.compile(reg_wire);
		Matcher match = regexp.matcher(command);

		match.find();
		if(match.matches()){
			String wirename = match.group(1).trim();
			Wire myWire = new Wire(CompositName, wirename);
			myComposit.AddToWireList(myWire);
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
	private static LED CreateLed(Composit myComposit, String command) {
		String CompositName = myComposit.ID;
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
	private static Oscilloscope CreateOscilloscope(Composit myComposit, String command) {
		String CompositName = myComposit.ID;
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
	private static GENERATOR CreateGenerator(Composit myComposit, String command) {
		String CompositName = myComposit.ID;
		String reg_generator = "generator[ ]+([\\w]+)?;";
		Pattern regexp = Pattern.compile(reg_generator);
		Matcher match = regexp.matcher(command);

		match.find();

		if(match.matches()){
			String genname = match.group(1).trim();
			GENERATOR myGenerator = new GENERATOR(CompositName, genname, 1000, 0);
			Wire genout = new Wire(CompositName);
			genout.SetConnection(null, myGenerator);
			myGenerator.wireOut.add(genout);
			myComposit.AddToWireList(genout);			
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
	private static SWITCH CreateSwitch(Composit myComposit, String command) {
		String CompositName = myComposit.ID;
		String reg_switch = "switch[ ]+([\\w]+);";
		Pattern regexp = Pattern.compile(reg_switch);
		Matcher match = regexp.matcher(command);

		match.find();

		if(match.matches()){
			String switchname = match.group(1).trim();
			SWITCH mySwitch = new SWITCH(CompositName, switchname);
			Wire swout = new Wire(CompositName);
			swout.SetConnection(null, mySwitch);
			mySwitch.wireOut.add(swout);
			myComposit.AddToWireList(swout);
			return mySwitch;
		}
		return null;
	}

	
	private static boolean SettingElement(Composit comp, String command) {
		String reg_set = "set[ ]+([\\w]+)=([\\d]+);";
		Pattern regexp = Pattern.compile(reg_set);
		Matcher match = regexp.matcher(command);

		match.find();
		String elementname = match.group(1).trim();
		int value = Integer.parseInt(match.group(3).trim());
		String elementtype = comp.GetElementByID(elementname).GetType();
		if (elementtype.equalsIgnoreCase("SWITCH")) {
			comp.SetSwitch(value, elementname);
			return true;
		} else if (elementtype.equalsIgnoreCase("GENERATOR")) {
			comp.SetSequence(value, elementname);
			return true;
		}

		return false;
	}
	
	private static  Wire assign(Composit myComposit, String command,List<DigitalObject> elementlist){
		//String CompositName = myComposit.ID;
		String reg_assign = "assign[ ]+([\\w]+)=(.*?);";
		Pattern regexp = Pattern.compile(reg_assign);
		Matcher match = regexp.matcher(command);
		Wire assigned_wire =null;
		match.find();

		if(match.matches()){
			String rvalue = match.group(2).trim();
			String lvalue = match.group(1).trim();
			String postfix = Infix2Postfix(rvalue);
			
			String[] items = postfix.split(" ");
			Stack<Wire> WireStack = new Stack<Wire>();
			if(WireStack.size()>1 || true){
				for(String item:items){
					
					if(!isOperator(item)){
						// Hozzadjuk a veremhez
						if(myComposit.GetWireByName(item)!=null){
							WireStack.add(myComposit.GetWireByName(item));
						}else if(myComposit.GetElementByName(item)!=null){
								WireStack.add(myComposit.GetElementByName(item).wireOut.get(0));
						}
					}else if(isOperator(item)){
						if(NumOfOperand(item)==1){
							if(item.equalsIgnoreCase("!"))
							{
								Wire inv_in1 = WireStack.pop();								
								INVERTER myInverter = new INVERTER(myComposit.ID, inv_in1);
								inv_in1.SetConnection(myInverter, null);
								Wire inv_out = new Wire(myComposit.ID);
								inv_out.SetConnection(null, myInverter);
								myComposit.AddToWireList(inv_out);
								myInverter.AddOutput(inv_out);
								WireStack.push(inv_out);
								elementlist.add(myInverter);
							}
						}
						if(NumOfOperand(item)==2){
							if(item.equalsIgnoreCase("&"))
							{
								Wire and_in1 = WireStack.pop();	
								Wire and_in2 = WireStack.pop();	
								ANDGate myAnd = new ANDGate(myComposit.ID, and_in1,and_in2);
								and_in1.SetConnection(myAnd, null);
								and_in2.SetConnection(myAnd, null);
								Wire and_out = new Wire(myComposit.ID);
								and_out.SetConnection(null, myAnd);
								myComposit.AddToWireList(and_out);
								myAnd.AddOutput(and_out);
								WireStack.push(and_out);
								elementlist.add(myAnd);
							}
							if(item.equalsIgnoreCase("|"))
							{
								Wire or_in1 = WireStack.pop();	
								Wire or_in2 = WireStack.pop();	
								ORGate myOr = new ORGate(myComposit.ID, or_in1,or_in2);
								or_in1.SetConnection(myOr, null);
								or_in2.SetConnection(myOr, null);
								Wire or_out = new Wire(myComposit.ID);
								or_out.SetConnection(null, myOr);
								myComposit.AddToWireList(or_out);
								myOr.AddOutput(or_out);
								WireStack.push(or_out);
								elementlist.add(myOr);
							}
						}//end: operandusok szama					
					}//end if: ha operator				
				}//end for: lista bejarasa, postfix kiertekel		
			}//Vege: ha tobb elem van a listaban mint 1

			assigned_wire = WireStack.pop();
			if(myComposit.GetWireByName(lvalue)!=null){
				myComposit.GetWireByName(lvalue).objectsIn=assigned_wire.objectsIn;				
			}else if(myComposit.GetElementByName(lvalue)!=null){
					myComposit.GetElementByName(lvalue).wireIn.add(assigned_wire);
					assigned_wire.SetConnection(myComposit.GetElementByName(lvalue), null);
					myComposit.AddToWireList(assigned_wire);
			}
		}
		
		return assigned_wire;
	}

	/**
	 * Infix2Postfix
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

// composite[ ]?([\w, ]+)\(in(( |[\w]+,?)+)out(( |[\w]+,?)+)\).*?endcomposite;