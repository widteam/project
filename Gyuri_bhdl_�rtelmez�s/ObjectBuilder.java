package bhdlparser;

import java.util.ArrayList;
import java.util.Scanner;

public class ObjectBuilder {
	
	public static Composit buildComposit(ArrayList<String> foundComposites){
		Composit temp = new Composit("main");
		for(String s : foundComposites)
    	{
			temp = new Composit("main"); //Ide m�g akartam volna egy initializeComposite met�dust �rni, de akkor ezt m�r nem �rom meg

    		s=s.replaceAll(";", "");
    		System.out.println(s);
    		if(ObjectBuilder.isObject(s)) //felt�ltj�k a kompozit�nket DigitalObjectekkel
    		{
    			temp.AddToComponentList(ObjectBuilder.buildObject(s));
    			System.out.println(ObjectBuilder.buildObject(s).ID + " l�trehozva.\n");
    		}
    		else if(ObjectBuilder.isWire(s)) //felt�ltj�k a kompozit�nket Wirekel
    		{
    			temp.AddToWireList(ObjectBuilder.buildWire(s));
    			System.out.println(ObjectBuilder.buildWire(s).GetID() + " l�trehozva.\n");
    		}
    		else if(ObjectBuilder.isAssign(s)) //�sszek�tj�k az elemeket
    		{
    			ObjectBuilder.assign(s, temp);
    			System.out.println();
    		}
    		else System.out.println("Nem tudom mi ez.\n");    		
    	}
		return temp;
	}
	
	//Ellen�rizz�k, hogy egy DigitalObjectet hozunk-e l�tre (true: igen azt, false: valami m�st csin�lunk)
	public static boolean isObject(String s){
		
		Scanner scanner = new Scanner(s);
				
		//ha kompozit akkor ok�
		if(scanner.hasNext("composit")
		|| scanner.hasNext("comp[0-9][0-9]")){
        	System.out.println("Ez egy composit.");
        	return true;
        }
		
		//ha led akkor ok�
        else if(scanner.hasNext("led")
    	|| scanner.hasNext("led[0-9][0-9]")){
        	System.out.println("Ez egy led.");
        	return true;
        }
		
		//ha switch akkor ok�
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
		
		//k�l�nben false
        else return false; //nem DigitalObject
	}
	
	//Ellen�rizz�k, hogy Wire-e
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
	
	//ellen�rizz�k, hogy �sszek�t�st kell l�trehoznunk
	public static boolean isAssign(String s){
		Scanner scanner = new Scanner(s);
		
		if(scanner.hasNext("assign")){
			System.out.println("Ez egy assign.");
			return true;
		}
		else return false;
	}
	
	//Visszaad csin�l egy olyan DigitalObjectet, amilyet a param�ter le�r 
	public static DigitalObject buildObject(String s){

        Scanner scanner = new Scanner(s);
        //kompozit l�trehoz�sa
        if(scanner.hasNext("composit")){
        	scanner.skip("composit");
        	return new Composit(scanner.next());
        }
        //led l�trehoz�sa
        else if(scanner.hasNext("led")){
        	scanner.skip("led");
        	return new LED(scanner.next());
        }
        //switch l�trehoz�sa
        else if(scanner.hasNext("switch")){
        	scanner.skip("switch");
        	return new SWITCH(scanner.next());
        }
        //generator l�trehoz�sa
        else if(scanner.hasNext("generator")){
        	scanner.skip("generator");
        	return new GENERATOR(scanner.next(), 0, 0);
        }
        //oscilloscope l�trehoz�sa
        else if(scanner.hasNext("oscilloscope")){
        	scanner.skip("oscilloscope");
        	return new Oscilloscope(scanner.next(), 10);
        }
        //andgate l�trehoz�sa
        else if(scanner.hasNext("andgate")){
        	scanner.skip("andgate");
        	return new ANDGate(scanner.next());
        }
        //orgate l�trehoz�sa
        else if(scanner.hasNext("orgate")){
        	scanner.skip("orgate");
        	return new ORGate(scanner.next());
        }
        //inverter l�trehoz�sa
        else if(scanner.hasNext("inverter")){
        	scanner.skip("inverter");
        	return new INVERTER(scanner.next());
        }
        else{
        	return null;
        }
	}
	
	//Param�terben megadott nev� Wire-t ad vissza
	public static Wire buildWire(String s){
		Scanner scanner = new Scanner(s);
		scanner.skip("wire");
		return new Wire(scanner.next());
	}
	
	//�sszekapcsol�st v�gzi el
	public static void assign(String s, Composit comp){
		Scanner scanner = new Scanner(s);
		scanner.skip("assign");
		String temp1 = scanner.next();
		String temp2 = scanner.next();
		if(ObjectBuilder.isWire(temp1))
		{
			//a DigitalObject WireOut list�j�ba helyezi a Wire-t
			comp.GetElementByID(temp2).addToWireOut(comp.GetWireByID(temp1));
			System.out.println("A " + temp2 + " kimenete " + temp1 + ".");
		}
		else if(ObjectBuilder.isObject(temp1))
		{
			//a DigitalObject WireIn list�j�ba helyezi a Wire-t
			comp.GetElementByID(temp1).addToWireIn(comp.GetWireByID(temp2));
			System.out.println("A " + temp1 + " bemenete " + temp2 + ".");
		}
	}
}