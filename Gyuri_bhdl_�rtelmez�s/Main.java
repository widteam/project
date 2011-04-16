/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bhdlparser;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
    	
    	ArrayList<String> foundComposites = new ArrayList<String>();
    	foundComposites.add("composit main");
    	foundComposites.add("(in,");
    	foundComposites.add("out)");
    	foundComposites.add("switch sw01;");
    	foundComposites.add("generator gen01;");
    	foundComposites.add("led led01;");
    	foundComposites.add("assign led01 = sw01 | gen01;");    	
    	foundComposites.add("endcomposit");
    	
    	
    	
    	ArrayList<DigitalObject> objectList = new ArrayList<DigitalObject>();
    	ArrayList<Wire> wireList = new ArrayList<Wire>();
    	
    	
    	    	
    	
    	for(String s : foundComposites)
    	{
    		System.out.println(s);
    		if(ObjectBuilder.isNewObject(s))
    		{
    			objectList.add(ObjectBuilder.buildObject(s));
    			System.out.println(ObjectBuilder.buildObject(s).ID + " létrehozva.\n");
    		}
    		else if(ObjectBuilder.isNewWire(s))
    		{
    			wireList.add(ObjectBuilder.buildWire(s));
    			System.out.println(ObjectBuilder.buildWire(s).GetID() + " létrehozva.\n");
    		}
    		else if(ObjectBuilder.isAssign(s))
    		{
    			ObjectBuilder.assign(s, objectList);
    			System.out.println();
    		}
    		else System.out.println("Nem tudom mi ez.\n");    		
    	}
    	System.out.println("Az objectList tartalma:");
    	for(DigitalObject o : objectList)
    		System.out.println(o.ID);
    }
}

