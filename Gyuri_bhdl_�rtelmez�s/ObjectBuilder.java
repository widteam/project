package bhdlparser;

import java.util.ArrayList;
import java.util.Scanner;

public class ObjectBuilder {
	
	public static Composit buildComposit(ArrayList<String> foundComposites){
		Composit temp = new Composit("main");
		for(String s : foundComposites)
    	{
			temp = new Composit("main"); //Ide még akartam volna egy initializeComposite metódust írni, de akkor ezt már nem írom meg

    		s=s.replaceAll(";", "");
    		System.out.println(s);
    		if(ObjectBuilder.isObject(s)) //feltöltjük a kompozitünket DigitalObjectekkel
    		{
    			temp.AddToComponentList(ObjectBuilder.buildObject(s));
    			System.out.println(ObjectBuilder.buildObject(s).ID + " létrehozva.\n");
    		}
    		else if(ObjectBuilder.isWire(s)) //feltöltjük a kompozitünket Wirekel
    		{
    			temp.AddToWireList(ObjectBuilder.buildWire(s));
    			System.out.println(ObjectBuilder.buildWire(s).GetID() + " létrehozva.\n");
    		}
    		else if(ObjectBuilder.isAssign(s)) //összekötjük az elemeket
    		{
    			ObjectBuilder.assign(s, temp);
    			System.out.println();
    		}
    		else System.out.println("Nem tudom mi ez.\n");    		
    	}
		return temp;
	}
	
	//Ellenõrizzük, hogy egy DigitalObjectet hozunk-e létre (true: igen azt, false: valami mást csinálunk)
	public static boolean isObject(String s){
		
		Scanner scanner = new Scanner(s);
				
		//ha kompozit akkor oké
		if(scanner.hasNext("composit")
		|| scanner.hasNext("comp[0-9][0-9]")){
        	System.out.println("Ez egy composit.");
        	return true;
        }
		
		//ha led akkor oké
        else if(scanner.hasNext("led")
    	|| scanner.hasNext("led[0-9][0-9]")){
        	System.out.println("Ez egy led.");
        	return true;
        }
		
		//ha switch akkor oké
        else if(scanner.hasNext("switch")
    	|| scanner.hasNext("sw[0-9][0-9]")){
        	System.out.println("Ez egy switch.");
        	return true;
        }
		
		//ha generator akkor true
        else if(scanner.hasNext("generator")
        || scanner.hasNext("gen[0-9][0-9]")){
        	System.out.println("Ez egy generator.");
        	return true;
        }
		
		//ha oscilloscope akkor true
        else if(scanner.hasNext("oscilloscope")
        || scanner.hasNext("osc[0-9][0-9]")){
        	System.out.println("Ez egy oscilloscope.");
        	return true;
        }
		
		//ha andgate akkor true
        else if(scanner.hasNext("andgate")
        || scanner.hasNext("and[0-9][0-9]")){
        	System.out.println("Ez egy andgate.");
        	return true;
        }
		
		//ha orgate akkor true
        else if(scanner.hasNext("orgate")
        || scanner.hasNext("or[0-9][0-9]")){
        	System.out.println("Ez egy orgate.");
        	return true;
        }
		
		//ha inverter akkor go
        else if(scanner.hasNext("inverter")
        || scanner.hasNext("inv[0-9][0-9]")){
           	System.out.println("Ez egy inverter.");
           	return true;
        }
		
		//különben false
        else return false; //nem DigitalObject
	}
	
	//Ellenõrizzük, hogy Wire-e
	public static boolean isWire(String s){
		Scanner scanner = new Scanner(s);
		
		//ha wire akkor ture
		if(scanner.hasNext("wire")
		|| scanner.hasNext("w[0-9][0-9]")){
			System.out.println("Ez egy wire.");
			return true;
		}
		else return false;
	}
	
	//ellenõrizzük, hogy összekötést kell létrehoznunk
	public static boolean isAssign(String s){
		Scanner scanner = new Scanner(s);
		
		if(scanner.hasNext("assign")){
			System.out.println("Ez egy assign.");
			return true;
		}
		else return false;
	}
	
	//Visszaad csinál egy olyan DigitalObjectet, amilyet a paraméter leír 
	public static DigitalObject buildObject(String s){

        Scanner scanner = new Scanner(s);
        //kompozit létrehozása
        if(scanner.hasNext("composit")){
        	scanner.skip("composit");
        	return new Composit(scanner.next());
        }
        //led létrehozása
        else if(scanner.hasNext("led")){
        	scanner.skip("led");
        	return new LED(scanner.next());
        }
        //switch létrehozása
        else if(scanner.hasNext("switch")){
        	scanner.skip("switch");
        	return new SWITCH(scanner.next());
        }
        //generator létrehozása
        else if(scanner.hasNext("generator")){
        	scanner.skip("generator");
        	return new GENERATOR(scanner.next(), 0, 0);
        }
        //oscilloscope létrehozása
        else if(scanner.hasNext("oscilloscope")){
        	scanner.skip("oscilloscope");
        	return new Oscilloscope(scanner.next(), 10);
        }
        //andgate létrehozása
        else if(scanner.hasNext("andgate")){
        	scanner.skip("andgate");
        	return new ANDGate(scanner.next());
        }
        //orgate létrehozása
        else if(scanner.hasNext("orgate")){
        	scanner.skip("orgate");
        	return new ORGate(scanner.next());
        }
        //inverter létrehozása
        else if(scanner.hasNext("inverter")){
        	scanner.skip("inverter");
        	return new INVERTER(scanner.next());
        }
        else{
        	return null;
        }
	}
	
	//Paraméterben megadott nevû Wire-t ad vissza
	public static Wire buildWire(String s){
		Scanner scanner = new Scanner(s);
		scanner.skip("wire");
		return new Wire(scanner.next());
	}
	
	//összekapcsolást végzi el
	public static void assign(String s, Composit comp){
		Scanner scanner = new Scanner(s);
		scanner.skip("assign");
		String temp1 = scanner.next();
		String temp2 = scanner.next();
		if(ObjectBuilder.isWire(temp1))
		{
			//a DigitalObject WireOut listájába helyezi a Wire-t
			comp.GetElementByID(temp2).addToWireOut(comp.GetWireByID(temp1));
			System.out.println("A " + temp2 + " kimenete " + temp1 + ".");
		}
		else if(ObjectBuilder.isObject(temp1))
		{
			//a DigitalObject WireIn listájába helyezi a Wire-t
			comp.GetElementByID(temp1).addToWireIn(comp.GetWireByID(temp2));
			System.out.println("A " + temp1 + " bemenete " + temp2 + ".");
		}
	}
}