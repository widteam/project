import java.util.ArrayList;
import java.util.List;

/**
 * A Composit aramkori elem tobb elem elrejtesere szolgal a kulvilag fele es
 * lehetove teszi, hogy egyetlen elemkent legyenek kezelve.
 * 
 * @author Tamas
 * 
 */
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

	/**
	 * PIN elemekbol allo lista, melybe a kivulrol erkezo drotok futnak
	 */
	public List<PIN> pins_in;
	/**
	 * PIN elemekbol allo lista, melybol a Compositon kivuli drotok indulnak
	 */
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

	/**
	 * A parameterben megadott azonositoju GENERATOR objektum frekvenciajat
	 * modositja
	 * 
	 * @param Frequency
	 *            Az uj frekvencia
	 * @param ElementID
	 *            Az Objektum ID-je
	 */
	public void SetFrequency(int Frequency, String ElementID) {
		GENERATOR tmp; // temporalis valtozo
		// megprobaljuk megtalalni az objektumot
		tmp = (GENERATOR) GetElementByID(ElementID);
		tmp.SetFrequency(Frequency);
	};

	/**
	 * Beallitja egy generator mintajat
	 * 
	 * @param Sequence
	 *            A beallitani kivant szekvencia (pl. 110110010)
	 * @param ElementID
	 *            Az objektum ID-je
	 */
	public void SetSequence(String Sequence, String ElementID) {
		// Leiras: A parameterben megadott azonositoju GENERATOR objektum
		// szekvenciajat modositja
		GENERATOR GEN_to_setsequence; /* Temporalis valtozo */
		/* GetElementByID-vel megkapjuk az objektumot */
		GEN_to_setsequence = (GENERATOR) GetElementByID(ElementID);
		GEN_to_setsequence.SetSequence(Sequence);

	};

	/**
	 * Beallitja egy kapcsolo erteket
	 * 
	 * @param Value
	 *            0,vagy 1 az Switch uj erteke
	 * @param ElementID
	 *            A beallitani kivant Switch ID-je
	 */
	public void SetSwitch(int Value, String ElementID) {
		SWITCH SW_to_set; /* Temporalis valtozo */
		SW_to_set = (SWITCH) GetElementByID(ElementID);
		SW_to_set.SetValue(Value);

	};

	/**
	 * A parameterben megadott azonositoju SWITCH objektum erteket az
	 * ellenkezöre allitja azaltal, hogy meghivja az objektum hasonlo nevu
	 * parameteret
	 * 
	 * @param ElementID
	 * @return
	 */
	public DigitalObject Toggle(String ElementID) {
		SWITCH SWITCH_to_toggle; /* Temporalis valtozo */
		SWITCH_to_toggle = (SWITCH) GetElementByID(ElementID);
		SWITCH_to_toggle.Toggle();
		return SWITCH_to_toggle;

	};

	/**
	 * Beallitja egy megadott Oscilloscope tarolt mintajanak nagysagat
	 * 
	 * @param SampleSize
	 *            Egy szam, ennyi mintat tarol
	 * @param ElementID
	 *            A beallitani kivant elem ID-je
	 */
	public void SetSample(int SampleSize, String ElementID) {
		Oscilloscope tmp;
		tmp = (Oscilloscope) GetElementByID(ElementID);
		if (SampleSize < 0) {
			System.out.println("x Error: SampleSize must be positive");
		} else {
			tmp.SetSample(SampleSize);
		}
	}

	/**
	 * Hozzaadja a parameterkent kapott DigitalObjectet a Feedbacks tömbjehez.
	 * 
	 * @param feedback
	 *            Az elem amit hozza kivanunk adni a Feedbacks listahoz
	 */
	public void AddToFeedbacks(DigitalObject feedback) {
		if (Feedbacks == null)
			Feedbacks = new ArrayList<DigitalObject>();
		Feedbacks.add(feedback); // Hozzaadjuk a feedbackshez
	};

	/**
	 * A Composit sajat elemei kozul kikeresi a megadott ID-vel rendelkezo
	 * objektumot es visszater a referenciajaval. Ha nem talalja, null a
	 * visszateresei ertek.
	 * 
	 * @param ElementID
	 * @return A kert objektum, ha nincs meg, null
	 */
	public DigitalObject GetElementByID(String ElementID) {
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

	/**
	 * A Composit sajat elemei kozul kikeresi a megadott nevvel rendelkezo
	 * objektumot es visszater a referenciajaval. Ha nem talalja, null a
	 * visszateresei ertek.
	 * 
	 * @param ElementID
	 * @return A kert objektum, ha nincs meg, null
	 */
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

	/**
	 * A Composit WireList listajabol kikeres egy elemet
	 * 
	 * @param WireID
	 *            a keresett Wire ID-je
	 * @return ha nincs talalt null, egyebkent a Wire
	 */
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

	/**
	 * A Composit WireList listajabol kikeres egy elemet
	 * 
	 * @param WireName
	 *            a keresett Wire neve
	 * @return ha nincs talalt null, egyebkent a Wire
	 */
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

	/**
	 * Composit Step Meghivja a Composit Count() metodusat, illetve ellenorzi,
	 * hogy stabil-e a Composit
	 * 
	 * @return {@code true} ha a Composit stabil az adott bemeneti ertekekre,
	 *         kulonben {@code false}
	 * @throws ConnectionsException A Composit elemeinek kapcsoltaibol szarmazo hibak
	 * @throws UnstableCircuitException  AZ aramkori elem nem stabil
	 */
	public boolean Step() throws ConnectionsException, UnstableCircuitException {
		/*
		 * Elozo ertekek tarolasara szolgalo lista (a COmpositnak tobb kimenete
		 * is lehet)
		 */
		List<Integer> PreviousValues = new ArrayList<Integer>();

		boolean Result = true;

		/* Elso lepesben hivunk egy Count() metodust */
		Count();

		/* Ezutan beolvassuk a legutolso futas eredmenyet */
		for (PIN p_out : pins_out) {
			PreviousValues.add(p_out.wireOut.GetValue());
		}

		/* Ha nem ures a feedbacks , stabilitast vizsgalunk */
		if (Feedbacks != null && !Feedbacks.isEmpty()) {

			if (DebugMode) {
				System.out
						.print("FEEDBACK founded. First running result were [");
				for (int i : PreviousValues)
					System.out.print(i + ",");
				System.out.print("]\n");
			}

			List<Integer> NewValues = new ArrayList<Integer>();

			for (DigitalObject obj : Feedbacks) { // Feedback osses elemen vegig
				obj.Count();
			}			
			// Most megnezzuk a kimenetet
			for (PIN p_out : pins_out) {
				NewValues.add(p_out.wireOut.GetValue());
			}
			// most pedig osszehasonlitunk minden erteket a listaban
			for (int i = 0; i < PreviousValues.size(); i++)
				if (PreviousValues.get(i) != NewValues.get(i))
					Result = false;

			PreviousValues = NewValues;
			NewValues.clear();
			// mostani)
			if (DebugMode)
				System.out
						.println("Second running result is equal with the previous? "
								+ Result);

			for (DigitalObject obj : Feedbacks) {
				obj.Count();
			}			
			// Most megnezzuk a kimenetet
			for (PIN p_out : pins_out) {
				NewValues.add(p_out.wireOut.GetValue());
			}
			// most pedig osszehasonlitunk minden erteket a listaban
			for (int i = 0; i < PreviousValues.size(); i++)
				if (PreviousValues.get(i) != NewValues.get(i))
					Result = false;

			PreviousValues = NewValues;
			NewValues.clear();
			if (DebugMode)
				System.out
						.println("Third running result is equal with the previous? "
								+ Result);

			for (DigitalObject obj : Feedbacks) {
				obj.Count();
			}			
			// Most megnezzuk a kimenetet
			for (PIN p_out : pins_out) {
				NewValues.add(p_out.wireOut.GetValue());
			}
			// most pedig osszehasonlitunk minden erteket a listaban
			for (int i = 0; i < PreviousValues.size(); i++)
				if (PreviousValues.get(i) != NewValues.get(i))
					Result = false;

			PreviousValues = NewValues;
			NewValues.clear();
			if(!Result) throw new UnstableCircuitException();
			if (DebugMode)
				System.out.println("Last running result. Gate is stable? "
						+ Result);
		}
		return Result;
	};

	/**
	 * Lekerdezi a bemeneteinek (PIN-ek) ertekeit, majd az osszes belso elemet
	 * Step()-eli
	 * 
	 * @return Mindig 0-val ter vissza.
	 * @throws ConnectionsException 
	 * @throws UnstableCircuitException 
	 */
	public int Count() throws UnstableCircuitException, ConnectionsException {
		for (PIN p_in : pins_in) {
			p_in.Step();
		}
		StepComponents();
		
		for (PIN p_out : pins_out) {			
			p_out.Step();
		}
		return 0;
	}

	/**
	 * A Composit osszes elemere meghivja a Step() metodust
	 * @throws ConnectionsException 
	 * @throws UnstableCircuitException 
	 */
	public void StepComponents() throws UnstableCircuitException, ConnectionsException {
		if (DebugMode)
			System.out.println("<" + this.GetName() + " step componnets>");

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
		if (null == GetWireByID(wire.GetID()))
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
	 * Felepeiti a Composit belso hierarchiajat Elso lepeskent lekerdez minden
	 * forrasnak minosulo elemet( Input osztaly leszarmazottjai, illetve bejovo
	 * PIN-ek) ezutan minddig mig vvan rendezetlen elem: az elozo hierarchia
	 * szinten levo elemeket vegignezi es a kovetkezo hierarchia szintre pakolja
	 * azokat, amik meg nem szerepelnek a reendezett listaban.
	 */
	public void buildHierarchy() {
		List<DigitalObject> tmp_components = this
				.getFirstLevelOfComponentList();
		// ComponentList tisztitasa
		ComponentList = new ArrayList<List<DigitalObject>>();

		/*
		 * Eltavolitunk minden olyan Wire-t ami nem csatlakozik sehova. Ez a
		 * felepitesnel sajnos eloallhat.
		 */
		for (DigitalObject obj : tmp_components) {
			if (obj.wireOut == null)
				continue;
			for (Wire obj_wire : obj.wireOut) {
				if (obj_wire.objectsOut == null
						|| obj_wire.objectsOut.isEmpty()) {
					obj.wireOut.remove(obj.wireOut.indexOf(obj_wire));
					this.RemoveFromWireList(obj_wire);
					obj_wire = null;
				}
			}
		}

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
						// Ha elertunk egy Composit szelehez
						if (wire_obj.GetType().equalsIgnoreCase("PIN")) {
							PIN tmp = (PIN) wire_obj;
							/*
							 * HA a PIN-t tartalmazo Composit neve az aktualis
							 * Composit neve, akkor elertunk a jelenlegiComposit
							 * szelehez, ki kell lepnunk a ciklusbol, nincs tobb
							 * eleme ezen a droton; ha egy masik Composithoz
							 * akkor azt hozzadhatjuk a hierarchiahoz (a PIN
							 * referenciat tarol a tartalmazo Compositra)
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
							 * ha PIN de nem eze a Composit
							 */
							else {
								wire_obj = tmp.ContainerComposit;
							}
						}
						// most nezzuk meg korabbi szintekre
						for (int i = 0; i < iHierarchy + 1; i++) {
							// Talaltunk egy feedbacket
							if (ComponentList.get(i).contains(wire_obj)) {

								feedback_start = wire_obj;
								feedback_end = obj;
								from_feedback_start.add(feedback_start);
								/**
								 * RESZGRAF a feedbackbol
								 */
								int count = 0;
								while (!from_feedback_start
										.contains(feedback_end)) {
									DigitalObject fb = from_feedback_start
											.get(count);

									List<Wire> FBWIRESOUT = null;
									if (fb.GetType().equalsIgnoreCase(
											"Composit")) {
										FBWIRESOUT = new ArrayList<Wire>();
										Composit renegade = (Composit) fb;
										for (PIN pin : renegade.pins_out) {
											FBWIRESOUT.add(pin.wireOut);
										}
									} else {
										FBWIRESOUT = fb.wireOut;
									}
									for (Wire fbw : FBWIRESOUT) {
										for (DigitalObject fbwo : fbw.objectsOut) {
											if (fbwo.GetType()
													.equalsIgnoreCase("PIN")) {
												PIN tmp = (PIN) fbwo;
												/*
												 * HA a PIN-ttartalmazo Composit
												 * neve az aktualis Composit
												 * neve, akkor elertunk a
												 * szelehez, ki kell lepnunk;
												 */
												if (tmp.ContainerComposit == this) {
													continue;
												}
												/*
												 * ha PIN de nem eze a Composit
												 */
												else {
													fbwo = tmp.ContainerComposit;
												}
											}

											from_feedback_start.add(fbwo);
											count++;
										}
									}
								}

								/*
								 * Feedback
								 */
								from_feedback_end.add(feedback_end);
								count = 0;
								while (!from_feedback_end
										.contains(feedback_start)) {
									DigitalObject fb = from_feedback_end
											.get(count);

									List<Wire> FBWIRESIN = null;
									if (fb.GetType().equalsIgnoreCase(
											"Composit")) {
										FBWIRESIN = new ArrayList<Wire>();
										Composit renegade = (Composit) fb;
										for (PIN pin : renegade.pins_in) {
											FBWIRESIN.add(pin.wireIn);
										}
									} else {
										FBWIRESIN = fb.wireIn;
									}
									for (Wire fbw : FBWIRESIN) {
										for (DigitalObject fbwi : fbw.objectsIn) {
											if (fbwi.GetType()
													.equalsIgnoreCase("PIN")) {
												PIN tmp = (PIN) fbwi;
												/*
												 * HA a PIN-ttartalmazo Composit
												 * neve az aktualis Composit
												 * neve, akkor elertunk a
												 * szelehez, ki kell lepnunk;
												 */
												if (tmp.ContainerComposit == this) {
													continue;
												}
												/*
												 * ha PIN de nem eze a Composit
												 */
												else {
													fbwi = tmp.ContainerComposit;
												}
											}
											from_feedback_end.add(fbwi);
											count++;

										}
									}
								}

								for (int index = 0; index < from_feedback_end
										.size(); index++) {
									DigitalObject d_o = from_feedback_end
											.get(index);
									if (from_feedback_start.contains(d_o)) {
										feedback_start.AddToFeedbacks(d_o);
									}
								}

							}// Vege:ellenorizzuk, szerepelt-e korabban
						}// vege: eddigi hierarchia bejarasa
					}// vege: kimeno drootok kimeno objektumai
				}// vege: kimeno drotok
			}// vege: allista elemei
			iHierarchy++;
		}// vege: hierarchia szintek
	}// vege: funkcio

	/**
	 * DEBUG fuggveny. Kiiratja a Composit tartalmat
	 * 
	 * @param AllComponent
	 *            ha {@code true} a parameter erteke, vizsgalja a belso
	 *            Compositokat is.
	 */
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
	 *            Ebbe a verembe gyujtjuk a Composit referenciakt
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

	/**
	 * A Composit felepitesenel a Compositot alkoto elemeket A ComponentList 0.
	 * szintjere gyujtjuk mindaddig mig nincs megszervezve a hierarchia
	 * 
	 * @return a ComponentList elso szintje
	 */
	public List<DigitalObject> getFirstLevelOfComponentList() {
		return ComponentList.get(0);
	}
}