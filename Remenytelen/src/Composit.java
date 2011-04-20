import java.util.ArrayList;
import java.util.List;

public class Composit extends DigitalObject {
	/**
	 * Leiras: Ez az attributum tarolja az összes kaput, kimenetet, bemenetet
	 * hierarchikus sorrendben Ez nem mas, mint egy listabol szervezett tömb. A
	 * tömb indexe azonositja a hierarchia szintet (0-Forrasok, 1-a forrasokhoz
	 * csatlakozo elemek, stb) az egyes szinteken pedig egy lista van az
	 * elemekröl
	 */
	private ArrayList<List<DigitalObject>> ComponentList;

	/**
	 * Egyszeru lista a Wire objektumokbol
	 */
	private List<Wire> WireList;

	public List<PIN> pins_in;
	public List<PIN> pins_out;

	/* KONSTRUKTOR */
	public Composit(String strOwnerCompositName, String CompositName) {
		final String strClassName = this.getClass().getName();
		ID = strOwnerCompositName + strIDDelimiter + strClassName
				+ strIDDelimiter + CompositName;
		WireList = new ArrayList<Wire>();

		pins_in = new ArrayList<PIN>();
		pins_out = new ArrayList<PIN>();

		ComponentList = new ArrayList<List<DigitalObject>>();
		ComponentList.add(new ArrayList<DigitalObject>());
	}

	/* METÓDUSOK */
	public void SetFrequency(int Frequency, String ElementID) {
		// Leiras: A parameterben megadott azonositoju GENERATOR objektum
		// frekvenciajat modositja
		GENERATOR tmp;
		tmp = (GENERATOR) GetElementByID(ElementID);
		tmp.SetFrequency(Frequency);
	};

	public void SetSequence(String Sequence, String ElementID) {
		// Leiras: A parameterben megadott azonositoju GENERATOR objektum
		// szekvenciajat modositja
		GENERATOR GEN_to_setsequence; /* Temporalis valtozo */
		GEN_to_setsequence = (GENERATOR) GetElementByID(ElementID); /*
																	 * GetElemetByIDvel
																	 * megkapjuk
																	 * , az
																	 * objektumot
																	 */
		GEN_to_setsequence.SetSequence(Sequence); /*
												 * az generator objektum
												 * SetSequence(...) metodusat
												 * meghivjuk
												 */

	};

	public void SetSwitch(int Value, String ElementID) {
		SWITCH SW_to_set; /* Temporalis valtozo */
		SW_to_set = (SWITCH) GetElementByID(ElementID);
		SW_to_set.Value = Value;

	};

	public void Toggle(String ElementID) {
		/*
		 * Leiras: A parameterben megadott azonositoju SWITCH objektum erteket
		 * az ellenkezöre allitja azaltal, hogy meghivja az objektum hasonlo
		 * nevu parameteret
		 */
		SWITCH SWITCH_to_toggle; /* Temporalis valtozo */
		SWITCH_to_toggle = (SWITCH) GetElementByID(ElementID); /*
																 * GetElemetByIDvel
																 * megkapjuk,
																 * majd egyet
																 */
		SWITCH_to_toggle.Toggle(); /*
									 * az switch objektum Toggle() metodusat
									 * meghivjuk
									 */

	};

	@Override
	public void AddToFeedbacks(DigitalObject feedback) {
		// Leiras: Hozzaadja a parameterkent kapott DigitalObjectet a Feedbacks
		// tömbjehez.
		if (Feedbacks == null)
			Feedbacks  = new ArrayList<DigitalObject>();
		
		Feedbacks.add(feedback); // Hozzaadjuk a feedbackshez
	};

	public DigitalObject GetElementByID(String ElementID) {
		// Leiras: Megkeres egy adott elemet a ComponentList listakban

		/**
		 * Teljes ID alapjan
		 */
		if (ComponentList != null && !ComponentList.isEmpty()) {
			for (List<DigitalObject> sublist : ComponentList) {
				for (DigitalObject o : sublist) {
					if (o.ID.equalsIgnoreCase(ElementID))
						return o;
				}
			}
		}
		return null;
	}

	public DigitalObject GetElementByName(String ElementName) {
		/**
		 * Nev alapjan
		 */
		if (ComponentList != null && !ComponentList.isEmpty()) {
			for (List<DigitalObject> sublist : ComponentList) {
				for (DigitalObject o : sublist) {
					if (o.GetName().equalsIgnoreCase(ElementName))
						return o;
				}
			}
		}
		return null;
	}

	public Wire GetWireByID(String WireID) {
		// Leiras: Megkeres egy adott drotot a WireList listakban
		if (WireList != null && !WireList.isEmpty()) {
			for (Wire w : WireList) {
				if (w.GetID() == WireID)
					return w;
			}
		}
		return null;
	}

	public Wire GetWireByName(String WireName) {
		// Leiras: Megkeres egy adott drotot a WireList listakban
		if (WireList != null && !WireList.isEmpty()) {
			for (Wire w : WireList) {
				if (w.GetName().equalsIgnoreCase(WireName))
					return w;
			}
		}
		return null;
	}

	public Wire ReplaceWire(Wire Mit, Wire Mivel) {
		// Leiras: Megkeres egy adott drotot a WireList listakban
		if (WireList != null && !WireList.isEmpty()) {
			for (Wire w : WireList) {
				if (w == Mit) {
					return WireList.set(WireList.indexOf(w), Mivel);
				}
			}
		}
		return null;
	}

	/*
	 * Itt van egy kisebb problema. Kivulröl kell stepet biztositani, de blulröl
	 * meg step components van. Step hivjon stepcomponentset es mondja meg hogy
	 * a reszobjektumok stabilak-e?
	 * 
	 * @see DigitalObject#Step()
	 */
	public boolean Step() {
		StepComponents();
		return true;
	};

	public int Count() {
		DigitalObject obj;
		for (List<DigitalObject> sublist : ComponentList) {
			for (DigitalObject o : sublist) {
				obj = (DigitalObject) o;
				obj.Count();
				//ha main
				if(ID.startsWith("null")){
					//ha output
					if(obj.wireOut.size()==0){
						System.out.println("<"+obj.GetName()+"> value is "+obj.wireIn.get(0).GetValue());
						
					}
					//ha wire
					for(int i=0; i<obj.wireOut.size();i++){
						System.out.println("<"+obj.wireOut.get(i).GetName()+"> value is "+obj.wireOut.get(0).GetValue() );
					}//for i
				}//if
			}//sublist
		}//list digitalo
		return 0;
	}

	public void StepComponents() {
		/*
		 * Elvileg mar fel van epulve a hierarchia igy nekem eleg megkapnom a
		 * ComponentListet
		 */
		System.out.println("<"+this.GetName()+" step componnets>");
	
		for (List<DigitalObject> sublist : ComponentList) {
			for (DigitalObject obj : sublist) {
				
				obj.Step();
			}
		}
	}

	/**
	 * Hozzaad a WireList-hez egy elemet
	 * 
	 * @param wire
	 */
	public void AddToWireList(Wire wire) {
		WireList.add(wire);
	}

	/**
	 * Elvesz a WireList-bol egy elemet
	 * 
	 * @param wire
	 */
	public boolean RemoveFromWireList(Wire w) {
		return WireList.remove(w);
	}

	/**
	 * Felepeiti a Composit belso hierarchiajat
	 * 
	 * 
	 */
	public void buildHierarchy() {
		List<DigitalObject> tmp_components = this
				.getFirstLevelOfComponentList();
		
		//ComponentList tisztitasa
		ComponentList = new ArrayList<List<DigitalObject>>();
		
		/*
		 * Elso lepesben az osszes Input objektumot hozzaadjuk a Hierarchia 0.
		 * szintjehez
		 */
		
		// 0. szint letrehozasa	
		ComponentList.add(new ArrayList<DigitalObject>());
		
		/*
		 * Ha van a compositban forras
		 */
		for (DigitalObject obj : tmp_components) {
			if (obj.GetType().equalsIgnoreCase("SWITCH")
					|| obj.GetType().equalsIgnoreCase("GENERATOR")) {
				/* Hozzadjuk a szinthez az elemet */
				ComponentList.get(0).add(obj);
			}
		}
		
		/*
		 * Ha a nulladik szintnel tartunk, szamolni kell a bemeno drotokkal is
		 * Mivel a Composit hataran PIN-ek vannak azokon kell vegigmenni
		 */
		for (PIN pin : pins_in) {
			if (pin.wireOut != null) {
				for (DigitalObject o : pin.wireOut.objectsOut) {
					if (!ComponentList.get(0).contains(o))
						ComponentList.get(0).add(o);
				}
			}
		}

		
		/* Eltavolitjuk a listabol a mar hierarchikus elemeket */
		tmp_components.removeAll(ComponentList.get(0));

		
		/* Amig van elem a rendezetlen elemekbol allo listaban */
		int iHierarchy = 0;
		while (!tmp_components.isEmpty()) {
			List<DigitalObject> components_next = new ArrayList<DigitalObject>();
			// Vegigmegyunk az aktualis szint osszes elemen
			for (DigitalObject obj : ComponentList.get(iHierarchy)) {
				
				
				// az aktualis szint aktualis elemenek kimenetein
				/*
				 * Ha ez az obj egy Composit, nem mehetunk bele. Egybol a kimeno
				 * drotokat kell megkapnunk.
				 */
				List<Wire> WIRESOUT = null;
				if (obj.GetType().equalsIgnoreCase("Composit")) {
					WIRESOUT = new ArrayList<Wire>();
					Composit renegade = (Composit) obj;
					for (PIN pin : renegade.pins_out) {
						WIRESOUT.add(pin.wireOut);
					}
				} else {
					WIRESOUT = obj.wireOut;
				}
				for (Wire out : WIRESOUT) {
					/*
					 * es az aktualis szint aktualis elemenek kimeno drotjai
					 * altal meghatarozott elemet hozzaadjuk a kovetkezo
					 * elemekhez
					 */					
					for (DigitalObject wire_obj : out.objectsOut) {
						boolean contain = false;
						// Ha elertunk egy  Composit szelehez
						if (wire_obj.GetType().equalsIgnoreCase("PIN")) {
							PIN tmp = (PIN) wire_obj;
							/*
							 * HA a PIN-t tartalmazo Composit neve az aktualis
							 * Composit neve, akkor elertunk a jelenlegiComposit
							 * szelehez, ki kell lepnunk a ciklusbol, nincs
							 * tobb eleme ezen a droton; 
							 * ha egy masik Composithoz akkor azt hozzadhatjuk
							 * a hierarchiahoz
							 * (a PIN referenciat tarol a tartalmazo Compositra)
							 */
							if (tmp.ContainerComposit == this) {
								continue;
							}
							// ha PIN de nem ez a Composit
							else {
								wire_obj = tmp.ContainerComposit;
							}
						}
						
						/*
						 * Itt meg kell nezni hogy korabi szinten szerepelt-e
						 * mar a jelenlegi objektum
						 */
						

						for (int i = 0; i < iHierarchy + 1; i++) {
							if (ComponentList.get(i).contains(wire_obj)) {
								contain = true;
							}
						}

						// ha nem volt meg korabbi szinten akkor most uj
						// szinthez adjuk
						if (!contain)
							if (!components_next.contains(wire_obj))
								components_next.add(wire_obj);
					}
				}
			}
			tmp_components.removeAll(components_next);
			ComponentList.add(new ArrayList<DigitalObject>());
			ComponentList.get(++iHierarchy).addAll(components_next);
		}
	}

	
	/**
	 * Egy hierarchikusan felepitett Compositban megkeresi a visszacsatolasokat.
	 */
	public void getFeedbacks() {
		List<DigitalObject> toCheck = new ArrayList<DigitalObject>();
		//List<DigitalObject> new_toCheck = new ArrayList<DigitalObject>();
		List<DigitalObject> from_feedback_start = new ArrayList<DigitalObject>();
		List<DigitalObject> from_feedback_end = new ArrayList<DigitalObject>();
		
		DigitalObject feedback_start = null;
		DigitalObject feedback_end = null;
		int iHierarchy = 0;

		// vegig az osszes hierarchia szinten
		for (List<DigitalObject> sublist : ComponentList) {
			toCheck = sublist;			
			for (DigitalObject obj : toCheck) {
				
				List<Wire> WIRESOUT = null;
				if (obj.GetType().equalsIgnoreCase("Composit")) {
					WIRESOUT = new ArrayList<Wire>();
					Composit renegade = (Composit) obj;
					for (PIN pin : renegade.pins_out) {
						WIRESOUT.add(pin.wireOut);
					}
				} else {
					WIRESOUT = obj.wireOut;
				}
				// az aktualis elem kimeneteit nezzuk
				for (Wire out : WIRESOUT) {
					/*
					 * Ellenoritzni kell, hogy a kimenet objektumai
					 * szerepeltek-e mar korabbi szinten
					 */
					for (DigitalObject wire_obj : out.objectsOut) {
						// Ha elertunk a Composit szelehez
						if (wire_obj.GetType().equalsIgnoreCase("PIN")) {
							PIN tmp = (PIN) wire_obj;
							/*
							 * HA a PIN-ttartalmazo Composit neve az aktualis
							 * Composit neve, akkor elertunk a szelehez, ki kell
							 * lepnunk; 
							 */
							if (tmp.ContainerComposit == this) {
								continue;
							}
							/*
							 *  ha PIN de nem eze a Composit
							 */
							else {
								wire_obj = tmp.ContainerComposit;
							}
						}
						// most nezzuk meg korabbi szintekre
						for (int i = 0; i < iHierarchy + 1; i++) {
							// Talaltunk egy feedbacket
							if (ComponentList.get(i).contains(wire_obj)) {
								
								feedback_start  =obj;
								feedback_end  =wire_obj;
								from_feedback_start.add(feedback_start);
								/**
								 * RÉSZGRÁF a feedbackbõl
								 */
								while(!from_feedback_start.contains(feedback_end)){
									for(DigitalObject fb:from_feedback_start){
										List<Wire> FBWIRESOUT = null;
										if (fb.GetType().equalsIgnoreCase("Composit")) {
											FBWIRESOUT = new ArrayList<Wire>();
											Composit renegade = (Composit) fb;
											for (PIN pin : renegade.pins_out) {
												FBWIRESOUT.add(pin.wireOut);
											}
										} else {
											FBWIRESOUT = fb.wireOut;
										}
										for(Wire fbw:FBWIRESOUT){
											for(DigitalObject fbwo:fbw.objectsOut){
												if (fbwo.GetType().equalsIgnoreCase("PIN")) {
													PIN tmp = (PIN) fbwo;
													/*
													 * HA a PIN-ttartalmazo Composit neve az aktualis
													 * Composit neve, akkor elertunk a szelehez, ki kell
													 * lepnunk; 
													 */
													if (tmp.ContainerComposit == this) {
														continue;
													}
													/*
													 *  ha PIN de nem eze a Composit
													 */
													else {
														fbwo = tmp.ContainerComposit;
													}
												}
												
												from_feedback_start.add(fbwo);
												
											}
										}
									}
								}
								
								/*
								 * Feedback
								 * 
								 */	
								from_feedback_end.add(feedback_end);
								while(!from_feedback_end.contains(feedback_start)){
									for(DigitalObject fb:from_feedback_end){
										List<Wire> FBWIRESIN = null;
										if (fb.GetType().equalsIgnoreCase("Composit")) {
											FBWIRESIN = new ArrayList<Wire>();
											Composit renegade = (Composit) fb;
											for (PIN pin : renegade.pins_in) {
												FBWIRESIN.add(pin.wireIn);
											}
										} else {
											FBWIRESIN = fb.wireIn;
										}
										for(Wire fbw:FBWIRESIN){
											for(DigitalObject fbwi:fbw.objectsIn){
												if (fbwi.GetType().equalsIgnoreCase("PIN")) {
													PIN tmp = (PIN) fbwi;
													/*
													 * HA a PIN-ttartalmazo Composit neve az aktualis
													 * Composit neve, akkor elertunk a szelehez, ki kell
													 * lepnunk; 
													 */
													if (tmp.ContainerComposit == this) {
														continue;
													}
													/*
													 *  ha PIN de nem eze a Composit
													 */
													else {
														fbwi = tmp.ContainerComposit;
													}
												}												
												from_feedback_end.add(fbwi);
												
											}
										}
									}
								}
								for(DigitalObject d_o:from_feedback_end){
									if(from_feedback_start.contains(d_o)){
										feedback_start.AddToFeedbacks(d_o);
									}
								}
								
							}//Vege:ellenorizzuk, szerepelt-e korabban
						}// vege: eddigi hierarchia bejarasa						
					}//vege: kimeno drootok kimeno objektumai
				}//vege: kimeno drotok
			}//vege: allista elemei
			iHierarchy++;
		}//vege: hierarchia szintek
	}//vege: funkcio

	public void Debug(boolean AllComponent) {
		/* KIIRATAS, DEBUG */
		if (AllComponent) {
			List<Composit> compi_list = new ArrayList<Composit>();
			compi_list.add(this);
			GetMyComposits(compi_list);

			for (Composit c : compi_list) {
				System.out.println();
				System.out.print(c.GetName());
				c.Debug(false);
				System.out.println();
			}
		} else {
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
		}
	}

	/**
	 * DEBUG fuggveny. A tartalmazott Compositokat kigyujti egy Verembe
	 * 
	 * @param compi_stack
	 */
	private void GetMyComposits(List<Composit> compi_list) {
		Composit CompositToReturn = null;
		// Bejarjuk a ComponentListet
		for (List<DigitalObject> sublist : ComponentList) {
			// Annak minden elemet
			for (DigitalObject o : sublist) {
				// Ha az adott eleme egy ujabb Composit
				if (o.GetType().equalsIgnoreCase("Composit")) {
					// Castolunk ra egyet, majd...
					CompositToReturn = (Composit) o;
					// Meghivjuk ra is rekurzivan ezt a fuggvenyt
					CompositToReturn.GetMyComposits(compi_list);
					// Majd ha az a Composit befejezte a felderitest, hozzadjuk
					// ezt
					if (!compi_list.contains(o))
						compi_list.add(CompositToReturn);
				}
			}
		}
	}

	public List<DigitalObject> getFirstLevelOfComponentList() {
		return ComponentList.get(0);
	}
}