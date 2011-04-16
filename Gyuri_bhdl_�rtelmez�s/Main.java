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
    	foundComposites.add("oscilloscope osc01;");
    	foundComposites.add("andgate and01;");
    	foundComposites.add("wire w01;");
    	foundComposites.add("wire w02;");
    	foundComposites.add("assign w01 sw01;");
    	foundComposites.add("assign w02 and01;");
    	foundComposites.add("assign and01 w02;");
    	foundComposites.add("assign osc01 w02;");
    	foundComposites.add("endcomposit;");
    	
    	Composit mainComp = new Composit("main");
    	
    	mainComp = ObjectBuilder.buildComposit(foundComposites);
    }
}

