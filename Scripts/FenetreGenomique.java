/*
 * FenetreGenomique est la classe qui construit une interface graphique à partir d'un objet Genome.
 * Cette classe est caractérisée par les informations suivantes :
 * <ul>
 * <li>regionList : La liste des regions contenues dans tout le genome. </li>
 * <li>subRegionList : La liste des subregions contenues dans tout le genome. </li>
 * <li>subSubRegionList : La liste des subsubregions contenues dans tout le genome. </li>
 * <li>genomeLength : Variable qui contient la taille du genome en bases azotées. </li>
 * <li>infoGenome : Hastable qui contient toutes les informations relatives a tout le genome. </li>
 * </ul>
 * @param Prend en argument un objet Genome
 * Cette classe va permettre d'organiser les données recuperer d'un ficher .gff3 en interface graphique.
 * </p>
 * @author YassJeanRaph
*/




////////////////IMPORTS/////////////////

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

////////////////////////////////////////


public class FenetreGenomique extends JFrame  // la classe FenetreGenomique herite de la classe JFrame
{

////////////////VARIABLES STATIQUES//////////////////

	private static int PANELWIDTH = 900;  //Largeur de la fenetre scrollable
	private static int PANELHEIGTH = 380; //Hauteur de la fenetre scrollable

/////////////////////////////////////////////////////

///////////VARIABLES DE NOS OBJETS GENOMES/////////////

	private int genomeLength; // Variable qui contiendra la taille du genome associé
	private ArrayList<Region> regionList; // Arraylist qui contiendra la liste des Regions du genome
	private ArrayList<SubRegion> subRegionList; // Arraylist qui contiendra la liste des Sous-Regions du genome
	private ArrayList<SubSubRegion> subSubRegionList;// Arraylist qui contiendra la liste des Sous-Sous-Regions du genome
	private Hashtable<String, String> infoGenome; // Hastable qui contiendra les informations associé au genome en generale (colonne 9 du fichier gff)

///////////////////////////////////////////////////////

	private String currentElement = "Region"; // Variables dont depend l'affichage des types representé sur l'interface (Regions, Sous-Regions, Exons)

////////////////COULEURS DE L'INTERFACE////////////////////

	private Color cadreColor = new Color(234, 236, 238); // Couleur 1 du panel scrollable
	private Color cadreColorBis = new Color(213, 216, 220); // Couleur 2 du panel scrollable
	private Color panelColor = new Color(213, 216, 220); // Couleur du panel principale
	private Color cartoucheColor = new Color(128, 139, 150); // Couleur de la cartouche de selection et de la cartouche

////////////////////////////////////////////////////////////


/////////////INITIALISATIONS / DECLARATIONS VARIABLES LIEES A L'INTERFACE///////////////

	private JTextField debut = new JTextField("0");// Zone de texte "debut" initialisée par defaut a 0
	private JTextField fin = new JTextField("5000"); // Zone de texte "fin" initialisée par defaut a 5000
	private JTextField id = new JTextField(); // Zone de texte de recherche id d'un element present dans le genome 
	private JComboBox<Object> chooseViewedElement = new JComboBox<Object>(); // Liste de choix des types d'elements affichés sur l'interface
	private JScrollPane genomeScroller = new JScrollPane(); // Fenetre scrollable
	private int debutFrame = 0; 
	private int finFrame = 5000 ;
	private double fittedGenomeLength; // Taille du genome ajustée a l'intervalle choisie
	private JLabel DebutDeLaFenetre; // Label "debut" de l'intervalle par defaut
	private JLabel FinDeLaFenetre; // Label "fin" de l'intervalle par defaut
	private double intervalle; // Taille de l'intervalle debut - Fin
	private double scalepb; // Echelle paires de bases par pixels

//////////////////////////////////////////////////////////////////////////////////////////


////////////////////////////CONSTRUCTEUR////////////////////////////	
	
	public FenetreGenomique(Genome currentGenome)       
	{
		super(); // Recuperation des variables et les methode de JFRAME
		regionList = currentGenome.getRegionList();
		subRegionList = currentGenome.getSubRegionList();
		subSubRegionList = currentGenome.getSubSubRegionList();
		genomeLength = currentGenome.getLength();
		infoGenome = currentGenome.getInfo();
		setTitle("Genome Explorer"); // TITRE DE LA FENETRE
		setSize(900,500);            // TAILLE DE LA FENETRE PRINCIPALE
		setLocationRelativeTo(null); // LA FENETRE APPARAIT AU MILLIEUR DE L'ECRAN
		setResizable(false); // IMPOSSIBILITE DE LA REDIMENSSIONER
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		build(); //Initialisation de la fenetre
		
	}

//////////////////////////////////////////////////////////////////////


/////////////////////////////BUILDER/////////////////////////////////
	private void build() 
	{
		DebutDeLaFenetre = new JLabel(String.valueOf(debutFrame)); // Label "debut" de l'intervalle qui reprend le nombre entrer par l'utilisateur
		FinDeLaFenetre = new JLabel(String.valueOf(finFrame)); // Label "fin" de l'intervalle qui reprend le nombre entrer par l'utilisateur

		intervalle = finFrame-debutFrame; //Calcul de l'intervalle
		scalepb = intervalle/PANELWIDTH; // Calcul de l'echelle
		fittedGenomeLength = genomeLength/intervalle*PANELWIDTH;  //Calcul de la taille du genome ajustée

		setContentPane(buildContentPane()); // Panel de "contenance", Panel primaire qui contient tous les elements de l'interface
		JScrollBar horizontalScrollBar = genomeScroller.getHorizontalScrollBar(); // Recupereration de la scrollbar
		horizontalScrollBar.setValue((int)(debutFrame/intervalle*PANELWIDTH)); // Positonnement de la scrollbar
		this.pack(); //Dimensionne le cadre de sorte que tout son contenu soit égal ou supérieur à sa taille préférée
	}


	//@return le panel de contenance (primaire)
	private JPanel buildContentPane() // PANEL PRIMAIRE
	{
		JPanel mainPanel = new JPanel(new BorderLayout()); // ORGANISATION EN BORDERLAYOUT
		mainPanel.add(setLocationBar(), BorderLayout.NORTH); // MENU DU HAUT ("CARTOUCHE DE SELECTION")
		mainPanel.add(setIntervalBar(), BorderLayout.SOUTH); // MENU DU BAS("CARTOUCHE D'INFORMATION")
		mainPanel.add(setGenomePanel(genomeLength), BorderLayout.CENTER);// ZONE D'AFFICHAGE DU GENOME EN PANEL SCROLLABLE
		mainPanel.add(setLabelPanel(), BorderLayout.WEST); // ZONE D'INDICATION DES DIFFERENTS CADRES DE LECTURES.
		return mainPanel;

	}
	
	private JPanel setLocationBar() // PANEL "CARTOUCHE DE SELECTION"
	{
		JPanel menuPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,10,10)); //Initialisation du panel "CARTOUCHE DE SELECTION"
		menuPanel.setBackground(cartoucheColor); //Y ajoute de la couleur
		
		
		JLabel MARGE = new JLabel("          "); //mise en forme de la marge entre le bord et écriture
		menuPanel.add(MARGE);	//ajout
		
		JLabel Debutlabel = new JLabel("Début :");
		
		
		debut.setHorizontalAlignment(JTextField.CENTER); //place l'écriture rentrée dans la zone de texte de manière centrée
		debut.setPreferredSize(new Dimension(50,30)); //Dimmensionne la zone de texte
		debut.setColumns(4); //la taille de la zone de texte
	
		menuPanel.add(Debutlabel); //ajout label + zone de texte au label début
		menuPanel.add(debut);
		
		JLabel Finlabel = new JLabel("Fin :");
	
		
		fin.setHorizontalAlignment(JTextField.CENTER);//place l'écriture rentrée dans la zone de texte de manière centrée
		fin.setPreferredSize(new Dimension(50,30));//Dimmensionne la zone de texte
		fin.setColumns(4);//la taille de la zone de texte
		menuPanel.add(Finlabel);//ajout label + zone de texte au label fin
		menuPanel.add(fin);
		JButton boutonOK_1 = new JButton("OK"); //initialisation du bouton "OK"
		
		menuPanel.add(boutonOK_1); //ajout du bouton OK au panel "CARTOUCHE DE SELECTION"
		boutonOK_1.addActionListener(new TraitementBut1()); //ajout d'un "effet" au bouton "OK"
		
		
		JLabel ESPACE = new JLabel("                              "); //Initialisation du panel espace
		menuPanel.add(ESPACE); //ajout au panel "CARTOUCHE DE SELECTION"
		
		//Ordonne la combobox de façon à ce que l'élement choisie soit en tete de liste
		if(currentElement.equals("Region")) //Cas choisie  = région
		{
			Object[] elements = new Object[]{"Region", "Sous-Region", "Exon"}; //liste éléments
			chooseViewedElement= new JComboBox<Object>(elements); //accorde la combobox
			menuPanel.add(chooseViewedElement); //ajout de la combo box dans le panel "CARTOUCHE DE SELECTION"
		}
		else if(currentElement.equals("Sous-Region"))//Cas choisie  = subrégion
		{
			Object[] elements = new Object[]{"Sous-Region", "Exon", "Region"}; //liste éléments
			chooseViewedElement= new JComboBox<Object>(elements); //accorde la combobox
			menuPanel.add(chooseViewedElement); //ajout de la combo box dans le panel "CARTOUCHE DE SELECTION"
		}
		else if(currentElement.equals("Exon"))//Cas choisie  = subsubrégion
		{
			Object[] elements = new Object[]{"Exon", "Region", "Sous-Region"}; //liste éléments
			chooseViewedElement= new JComboBox<Object>(elements); //accorde la combobox
			menuPanel.add(chooseViewedElement); //ajout de la combo box dans le panel "CARTOUCHE DE SELECTION"
		}

		
		JButton boutonOK_3 = new JButton("OK");//initialisation du bouton "OK"
		menuPanel.add(boutonOK_3); // ajout bouton dans le panel "CARTOUCHE DE SELECTION"
		boutonOK_3.addActionListener(new TraitementBut3());//ajout d'un "effet" au bouton "OK"
		
		JLabel ESPACE2 = new JLabel("    "); //initialisation label ESPACE2
		menuPanel.add(ESPACE2); //ajoute le panel au panel "CARTOUCHE DE SELECTION"
		
		
		JLabel idlabel = new JLabel("Identifiant :"); //initialisation du label idlabel
		id.setPreferredSize(new Dimension(70,30)); //dimmmensionne le panel
		id.setColumns(10); //def taille zone de texte
		
		menuPanel.add(idlabel); //ajout du label au label "CARTOUCHE DE SELECTION"
		menuPanel.add(id);//ajout de la zone de texte au label "CARTOUCHE DE SELECTION"
		
		JButton boutonOK_2 = new JButton("OK");
		
		menuPanel.add(boutonOK_2);
		boutonOK_2.addActionListener(new TraitementBut2());
		

		return menuPanel;
	}
	
	private JPanel setIntervalBar() // PANEL "CARTOUCHE D'INFORMATION"
	{
		JPanel menuPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,5,10)); //Initialisation du panel menuPanel
		menuPanel.setBackground(cartoucheColor); //ajout de couleur au panel
		
		JLabel espece = new JLabel("Espece visualisée : "+ String.valueOf(infoGenome.get("old-name") + " (" + String.valueOf(genomeLength) + " pbs)")); //Initialisation du label espece + recup de l'argument génome ID dans infoGenome
		menuPanel.add(espece); //ajout du label dans le panel "CARTOUCHE D'INFORMATION"


		JLabel ESPACE = new JLabel("                                                         "); // CREATION LABEL + AJOUT dans "CARTOUCHE D'INFORMATION"
		menuPanel.add(ESPACE);																	 //

		JLabel label = new JLabel("Intervalle représenté:");									 // CREATION LABEL + AJOUT dans "CARTOUCHE D'INFORMATION"
		menuPanel.add(label);																     //

		JLabel crochetG = new JLabel("[");														 //
		JLabel crochetD = new JLabel("] bps");													 //CREATION LABEL
		JLabel milieu = new JLabel(",");													     //
		

		
		menuPanel.add(crochetG);																//
		menuPanel.add(DebutDeLaFenetre);														//
		menuPanel.add(milieu);																	//AJOUT dans "CARTOUCHE D'INFORMATION"
		menuPanel.add(FinDeLaFenetre);															//
		menuPanel.add(crochetD);																//



		return menuPanel;
	}
	
	private JPanel setLabelPanel() // PANEL CADRES DE LECTURES
	{
		JPanel labelPanel = new JPanel();
		labelPanel.setBackground(panelColor);
		labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.PAGE_AXIS)); // BOXLAYOUT ORGANISER EN PAGE_AXIS (LES COMPOSANT SERONT RANGES LES UNS EN DESSOUS DE L'AUTRES)
		
		
		JLabel plus_1 = new JLabel(" +1");	//
		JLabel plus_2 = new JLabel(" +2");	//
		JLabel plus_3 = new JLabel(" +3");	//INITIALISATION + CREATION LABEL
		JLabel moin_1 = new JLabel(" -1");	//
		JLabel moin_2 = new JLabel(" -2");	//
		JLabel moin_3 = new JLabel(" -3");	//

		labelPanel.add(Box.createRigidArea((new Dimension(0,30)))); // 

		labelPanel.add(plus_1);										//
		labelPanel.add(Box.createRigidArea((new Dimension(0,40)))); //
	
		labelPanel.add(plus_2);
		labelPanel.add(Box.createRigidArea((new Dimension(0,45)))); //

		labelPanel.add(plus_3);										// AJOUT DE RIGIDES AREAS ENTRE CHAQUE LABEL = ESPACE VERTICAUX
		labelPanel.add(Box.createRigidArea((new Dimension(0,45)))); //

		labelPanel.add(moin_1);										//
		labelPanel.add(Box.createRigidArea((new Dimension(0,42)))); //

		labelPanel.add(moin_2);										//
		labelPanel.add(Box.createRigidArea((new Dimension(0,48)))); //

		labelPanel.add(moin_3);
		
		return labelPanel;
	}
	
	//@ return le panel genomePanel modifier
	private JPanel setGenomePanel(int length) //PANEL QUI CONTIENT LE PANEL SCROLLABLE
	{
		JPanel genomePanel = new JPanel(); //Initialisation panel
		genomePanel.setBackground(panelColor);//ajoute une couleur
		genomeScroller = setScrollingPanel((int)(fittedGenomeLength)); //Créer panel scrollable dont la taille correspond à la taille ajusté à l'échelle du génome (cast en int car méthode en dépend)
		genomePanel.add(genomeScroller); //ajout du panel scrollable au panel "MERE"
		
	genomeScroller.getHorizontalScrollBar().addAdjustmentListener(new AdjustmentListener() { //Donne l'animation de la scrollbar
		      public void adjustmentValueChanged(AdjustmentEvent e) { //Ajuste les valeurs des labels début et fin en fonction de la position de la scrollbar
		    		DebutDeLaFenetre.setText(String.valueOf( (int)(genomeScroller.getHorizontalScrollBar().getValue()*scalepb)));
		    		FinDeLaFenetre.setText(String.valueOf((finFrame-debutFrame)+(int)(genomeScroller.getHorizontalScrollBar().getValue()*scalepb)));
		      }
		    });
		return genomePanel;
	}
	
	private JScrollPane setScrollingPanel(int length) //PANEL SCROLLABLE
	{
		JPanel mainPanel = new JPanel(new GridBagLayout()); //ORGANISE EN GRIDBAGLAYOUT POUR AVOIR UN MEILLEURE CONTROL SUR LES PLACEMENT DES DIFFERENTS COMPOSANT
		JScrollPane genomeScroller = new JScrollPane(mainPanel); //Initialisation
		genomeScroller.setPreferredSize(new Dimension(PANELWIDTH, PANELHEIGTH)); // DIMMENSION DU PANEL
		genomeScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS); // LA SCROLLBAR HORIZONTAL SERA TOUJOURS VISIBILES (POSSIBILITE DE LA METTRE EN "AS_NEEDED3)

		GridBagConstraints gbc = new GridBagConstraints(); // OBJET PERMETTANT DE PLACER LES COMPOSANT SUR LE PANEL

		gbc.weightx = 1; // donne un poid de 1 en x à chaque panel ajouter 
		gbc.weighty = 1; // donne un poid de 1 en y à chaque panel ajouter 

		gbc.gridy = 0;
		mainPanel.add(Cadre_1(length), gbc); // PANEL QUI CONTIENDRA LES BOUTONS CORRESPONDANT AU CADRE LECTURE 1 DIRECT

		gbc.gridy = 1;
		mainPanel.add(Cadre_2(length),gbc); //PANEL QUI CONTIENDRA LES BOUTONS CORRESPONDANT AU CADRE LECTURE 2 DIRECT

		gbc.gridy = 2;
		mainPanel.add(Cadre_3(length),gbc); //PANEL QUI CONTIENDRA LES BOUTONS CORRESPONDANT AU CADRE LECTURE 3 DIRECT

		gbc.gridy = 3;
		mainPanel.add(Cadre_4(length),gbc); //PANEL QUI CONTIENDRA LES BOUTONS CORRESPONDANT AU CADRE LECTURE 1 INDIRECT

		gbc.gridy = 4;
		mainPanel.add(Cadre_5(length),gbc); //PANEL QUI CONTIENDRA LES BOUTONS CORRESPONDANT AU CADRE LECTURE 2 INDIRECT

		gbc.gridy = 5;
		mainPanel.add(Cadre_6(length),gbc); //PANEL QUI CONTIENDRA LES BOUTONS CORRESPONDANT AU CADRE LECTURE 3 INDIRECT

		return genomeScroller;
	}


	// LES JPANEL QUI SUIVENT CONTIENDRONT L'INFORMATION DU GENOME POUR CHAQUES CADRES DE LECTURES, IL Y EN A EN TOUT 6 COMME CA ON POURRA LES GERER INDEPENDEMENT DES AUTRES
	private JPanel Cadre_1(int length)
	{
		JPanel panel = new JPanel(); //Initialisation panel
		panel.setPreferredSize(new Dimension(length,60)); //Intialise la taille de la bande qui contiendra les boutons
		panel.setBackground(cadreColor); //couleur
		panel.setLayout(null); 
		
		if(currentElement.equals("Region") ) //Si element choisie  est égale à  région
		{ //if1
			double start = 0;
			double len =0;
			for(Region currentRegion:regionList) //Pour chaque region de la liste region
			{ 
				if(currentRegion.getStrand() == '+' && currentRegion.getFrame() == 1) //Si la region est dans le sens direct et dans le cadre de lecture 1
				{ //if2
					JButton regionButton = new JButton(String.valueOf(currentRegion.getType())); //Initialisation d'un bouton qui aura comme noms le type de la region, et comme taille la longueur de cette région mise à l'échelle
					start = (currentRegion.getStart()/intervalle*PANELWIDTH); //Recupération du start (x) avec une mise à l'échelle
					len = (currentRegion.getLength()/intervalle*PANELWIDTH); //Recupérature de la taille du bouton mise à l'échelle
					regionButton.setBounds((int)(start), 10, (int)(len), 30); //Positionnement et application du bouton

					if(currentRegion.getType().equals("gene")) //Si la région est un gène
					{
						regionButton.setBackground(new Color(169, 139, 183)); //coloration de type "gène"
						regionButton.setForeground(Color.WHITE); //coloration écriture de type "gène"
					}
					else if(currentRegion.getType().equals("repeat_region"))
					{
						regionButton.setBackground(new Color(166, 210, 99));
						regionButton.setForeground(Color.WHITE);
					}
					else if(currentRegion.getType().equals("mobile_genetic_element"))
					{
						regionButton.setBackground(new Color(10, 180, 148));
						regionButton.setForeground(Color.WHITE);
					}
					else if(currentRegion.getType().equals("pseudogene"))
					{
						regionButton.setBackground(new Color(197, 157, 8));
						regionButton.setForeground(Color.WHITE);
					}
					else if(currentRegion.getType().equals("origin_of_replication"))
					{
						regionButton.setBackground(new Color(58, 98, 247));
						regionButton.setForeground(Color.WHITE);
					}
					panel.add(regionButton); //ajout du bouton au panel "CADRE_1"

					String	info = new String("Information sur : " + currentRegion.getInfo().get("Name") + "\n" ); //Initialisation de la variable qui contiendra toute l'info de la région dans une chaine de caractère
					Iterator<String> it; //Initialisation d'un itérateur
					it = currentRegion.getInfo().keySet().iterator(); //itérateur, qui récupère les clés du tableau de hash de l'argument info de la région

					String inf = new String(String.valueOf(currentRegion.getStart())); //récupère le debut de la région caster en string
					String sup = new String(String.valueOf(currentRegion.getEnd())); //récupère la fin de la région caster en string
					String borne = new String("Borne : [" +inf+"-"+sup+"] bps\n" ); //Mise en forme de la ligne borne qui sera contenue dans info
					info = info.concat(borne); //concaténation dans le hastable

					while(it.hasNext()) //Tant que l'itérateur à un élément suivant (n'a pas parcouru toutes les clés)
					{
						String key = it.next(); //recupère la clé
						String value = currentRegion.getInfo().get(key); //recupère la valeur associée à cette clé

						String line = new String(key +" : " + value +"\n"); //Mise en forme de la line qui sera contennu dans info
						info = info.concat(line); //concaténation dans le hashtable
					}
					regionButton.addActionListener(new TraitementBut4(info)); //ajoute de l'effet aux boutons régions
				} //fin if2
			} //fin du for
		} //fin if1

		//MEME RAISONNEMENT POUR "SOUS REGION" CF LIGNE 357-422
		else if(currentElement.equals("Sous-Region"))
		{
			double start = 0;
			double len =0;
			for(SubRegion currentSubRegion:subRegionList)
			{
				if(currentSubRegion.getStrand() == '+' && currentSubRegion.getFrame() == 1)
				{
					JButton subRegionButton = new JButton(String.valueOf(currentSubRegion.getType()));
					start = (currentSubRegion.getStart()/intervalle*PANELWIDTH);
					len = (currentSubRegion.getLength()/intervalle*PANELWIDTH);
					subRegionButton.setBounds((int)(start), 10, (int)(len), 30);

					if(currentSubRegion.getType().equals("CDS"))
					{
						subRegionButton.setBackground(new Color(219, 82, 61));
						subRegionButton.setForeground(Color.WHITE);
					}
					else if(currentSubRegion.getType().equals("antisense_RNA"))
					{
						subRegionButton.setBackground(new Color(123, 144, 197));
						subRegionButton.setForeground(Color.WHITE);
					}
					else if(currentSubRegion.getType().equals("ncRNA"))
					{
						subRegionButton.setBackground(new Color(119, 218, 133));
						subRegionButton.setForeground(Color.WHITE);
					}
					else if(currentSubRegion.getType().equals("rRNA"))
					{
						subRegionButton.setBackground(new Color(226, 152, 59));
						subRegionButton.setForeground(Color.WHITE);
					}
					else if(currentSubRegion.getType().equals("tRNA"))
					{
						subRegionButton.setBackground(new Color(98, 99, 150));
						subRegionButton.setForeground(Color.WHITE);
					}
					else if(currentSubRegion.getType().equals("pseudogenic_tRNA"))
					{
						subRegionButton.setBackground(new Color(109, 21, 13));
						subRegionButton.setForeground(Color.WHITE);
					}
					else if(currentSubRegion.getType().equals("SRP_RNA"))
					{
						subRegionButton.setBackground(new Color(206, 76, 185));
						subRegionButton.setForeground(Color.WHITE);
					}
					else if(currentSubRegion.getType().equals("tmRNA"))
					{
						subRegionButton.setBackground(new Color(168, 131, 67));
						subRegionButton.setForeground(Color.WHITE);
					}
					else if(currentSubRegion.getType().equals("RNase_P_RNA"))
					{
						subRegionButton.setBackground(new Color(22, 48, 112));
						subRegionButton.setForeground(Color.WHITE);
					}
					panel.add(subRegionButton);


					String	info = new String("Information sur : " + currentSubRegion.getInfo().get("ID") + "\n" );
					Iterator<String> it;
					it = currentSubRegion.getInfo().keySet().iterator(); 

					String inf = new String(String.valueOf(currentSubRegion.getStart()));
					String sup = new String(String.valueOf(currentSubRegion.getEnd()));
					String borne = new String("Borne : [" +inf+"-"+sup+"] bps\n" );
					info = info.concat(borne);

					while(it.hasNext())
					{
						String key = it.next();
						String value = currentSubRegion.getInfo().get(key);

						String line = new String(key +" : " + value +"\n");
						info = info.concat(line);
					}


					subRegionButton.addActionListener(new TraitementBut4(info));

				}
			}
		}

		//MEME RAISONNEMENT POUR "EXON" CF LIGNE 357-422
		else if(currentElement.equals("Exon"))
		{
			double start = 0;
			double len =0;
			for(SubSubRegion currentSubSubRegion:subSubRegionList)
			{
				if(currentSubSubRegion.getStrand() == '+' && currentSubSubRegion.getFrame() == 1)
				{
					JButton subSubRegionButton = new JButton(String.valueOf(currentSubSubRegion.getType()));
					start = (currentSubSubRegion.getStart()/intervalle*PANELWIDTH);
					len = (currentSubSubRegion.getLength()/intervalle*PANELWIDTH);
					subSubRegionButton.setBounds((int)(start), 10, (int)(len), 30);
					subSubRegionButton.setBackground(new Color(80, 170, 87));
					subSubRegionButton.setForeground(Color.WHITE);
					panel.add(subSubRegionButton);

					String	info = new String("Information sur : " + currentSubSubRegion.getInfo().get("ID") + "\n" );
					Iterator<String> it;
					it = currentSubSubRegion.getInfo().keySet().iterator(); 

					String inf = new String(String.valueOf(currentSubSubRegion.getStart()));
					String sup = new String(String.valueOf(currentSubSubRegion.getEnd()));
					String borne = new String("Borne : [" +inf+"-"+sup+"] bps\n" );
					info = info.concat(borne);

					while(it.hasNext())
					{
						String key = it.next();
						String value = currentSubSubRegion.getInfo().get(key);

						String line = new String(key +" : " + value +"\n");
						info = info.concat(line);
					}

					subSubRegionButton.addActionListener(new TraitementBut4(info));

				}
			}	
		}
		
		return panel;
	}

//MEME RAISONNEMENT POUR "CADRE 1" CF LIGNE 355-553
	private JPanel Cadre_2(int length)
	{
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(length,60));
		panel.setBackground(cadreColorBis);
		panel.setLayout(null);
		
		if(currentElement.equals("Region") )
		{
			double start = 0;
			double len =0;
			for(Region currentRegion:regionList)
			{
				if(currentRegion.getStrand() == '+' && currentRegion.getFrame() == 2)
				{
					JButton regionButton = new JButton(String.valueOf(currentRegion.getType()));
					start = (currentRegion.getStart()/intervalle*PANELWIDTH);
					len = (currentRegion.getLength()/intervalle*PANELWIDTH);
					regionButton.setBounds((int)(start), 10, (int)(len), 30);

					if(currentRegion.getType().equals("gene"))
					{
						regionButton.setBackground(new Color(169, 139, 183));
						regionButton.setForeground(Color.WHITE);
					}
					else if(currentRegion.getType().equals("repeat_region"))
					{
						regionButton.setBackground(new Color(166, 210, 99));
						regionButton.setForeground(Color.WHITE);
					}
					else if(currentRegion.getType().equals("mobile_genetic_element"))
					{
						regionButton.setBackground(new Color(10, 180, 148));
						regionButton.setForeground(Color.WHITE);
					}
					else if(currentRegion.getType().equals("pseudogene"))
					{
						regionButton.setBackground(new Color(197, 157, 8));
						regionButton.setForeground(Color.WHITE);
					}
					else if(currentRegion.getType().equals("origin_of_replication"))
					{
						regionButton.setBackground(new Color(58, 98, 247));
						regionButton.setForeground(Color.WHITE);
					}
					panel.add(regionButton);

					String	info = new String("Information sur : " + currentRegion.getInfo().get("Name") + "\n" );
					Iterator<String> it;
					it = currentRegion.getInfo().keySet().iterator(); 

					String inf = new String(String.valueOf(currentRegion.getStart()));
					String sup = new String(String.valueOf(currentRegion.getEnd()));
					String borne = new String("Borne : [" +inf+"-"+sup+"] bps\n" );
					info = info.concat(borne);

					while(it.hasNext())
					{
						String key = it.next();
						String value = currentRegion.getInfo().get(key);

						String line = new String(key +" : " + value +"\n");
						info = info.concat(line);
					}

					regionButton.addActionListener(new TraitementBut4(info));

				}
			}
		}
		else if(currentElement.equals("Sous-Region"))
		{
			double start = 0;
			double len =0;
			for(SubRegion currentSubRegion:subRegionList)
			{
				if(currentSubRegion.getStrand() == '+' && currentSubRegion.getFrame() == 2)
				{
					JButton subRegionButton = new JButton(String.valueOf(currentSubRegion.getType()));
					start = (currentSubRegion.getStart()/intervalle*PANELWIDTH);
					len = (currentSubRegion.getLength()/intervalle*PANELWIDTH);
					subRegionButton.setBounds((int)(start), 10, (int)(len), 30);

					if(currentSubRegion.getType().equals("CDS"))
					{
						subRegionButton.setBackground(new Color(219, 82, 61));
						subRegionButton.setForeground(Color.WHITE);
					}
					else if(currentSubRegion.getType().equals("antisense_RNA"))
					{
						subRegionButton.setBackground(new Color(123, 144, 197));
						subRegionButton.setForeground(Color.WHITE);
					}
					else if(currentSubRegion.getType().equals("ncRNA"))
					{
						subRegionButton.setBackground(new Color(119, 218, 133));
						subRegionButton.setForeground(Color.WHITE);
					}
					else if(currentSubRegion.getType().equals("rRNA"))
					{
						subRegionButton.setBackground(new Color(226, 152, 59));
						subRegionButton.setForeground(Color.WHITE);
					}
					else if(currentSubRegion.getType().equals("tRNA"))
					{
						subRegionButton.setBackground(new Color(98, 99, 150));
						subRegionButton.setForeground(Color.WHITE);
					}
					else if(currentSubRegion.getType().equals("pseudogenic_tRNA"))
					{
						subRegionButton.setBackground(new Color(109, 21, 13));
						subRegionButton.setForeground(Color.WHITE);
					}
					else if(currentSubRegion.getType().equals("SRP_RNA"))
					{
						subRegionButton.setBackground(new Color(206, 76, 185));
						subRegionButton.setForeground(Color.WHITE);
					}
					else if(currentSubRegion.getType().equals("tmRNA"))
					{
						subRegionButton.setBackground(new Color(168, 131, 67));
						subRegionButton.setForeground(Color.WHITE);
					}
					else if(currentSubRegion.getType().equals("RNase_P_RNA"))
					{
						subRegionButton.setBackground(new Color(22, 48, 112));
						subRegionButton.setForeground(Color.WHITE);
					}
					panel.add(subRegionButton);


					String	info = new String("Information sur : " + currentSubRegion.getInfo().get("ID") + "\n" );
					Iterator<String> it;
					it = currentSubRegion.getInfo().keySet().iterator(); 


					String inf = new String(String.valueOf(currentSubRegion.getStart()));
					String sup = new String(String.valueOf(currentSubRegion.getEnd()));
					String borne = new String("Borne : [" +inf+"-"+sup+"] bps\n" );
					info = info.concat(borne);

					while(it.hasNext())
					{
						String key = it.next();
						String value = currentSubRegion.getInfo().get(key);

						String line = new String(key +" : " + value +"\n");
						info = info.concat(line);
					}


					subRegionButton.addActionListener(new TraitementBut4(info));

				}
			}
		}
		else if(currentElement.equals("Exon"))
		{
			double start = 0;
			double len =0;
			for(SubSubRegion currentSubSubRegion:subSubRegionList)
			{
				if(currentSubSubRegion.getStrand() == '+' && currentSubSubRegion.getFrame() == 2)
				{
					JButton subSubRegionButton = new JButton(String.valueOf(currentSubSubRegion.getType()));
					start = (currentSubSubRegion.getStart()/intervalle*PANELWIDTH);
					len = (currentSubSubRegion.getLength()/intervalle*PANELWIDTH);
					subSubRegionButton.setBounds((int)(start), 10, (int)(len), 30);
					subSubRegionButton.setBackground(new Color(80, 170, 87));
					subSubRegionButton.setForeground(Color.WHITE);
					panel.add(subSubRegionButton);

					String	info = new String("Information sur : " + currentSubSubRegion.getInfo().get("ID") + "\n" );
					Iterator<String> it;
					it = currentSubSubRegion.getInfo().keySet().iterator(); 

					String inf = new String(String.valueOf(currentSubSubRegion.getStart()));
					String sup = new String(String.valueOf(currentSubSubRegion.getEnd()));
					String borne = new String("Borne : [" +inf+"-"+sup+"] bps\n" );
					info = info.concat(borne);

					while(it.hasNext())
					{
						String key = it.next();
						String value = currentSubSubRegion.getInfo().get(key);

						String line = new String(key +" : " + value +"\n");
						info = info.concat(line);
					}


					subSubRegionButton.addActionListener(new TraitementBut4(info));

				}
			}	
		}
		
		return panel;
	}

	//MEME RAISONNEMENT POUR "CADRE 1" CF LIGNE 355-553
	private JPanel Cadre_3(int length)
	{
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(length,60));
		panel.setBackground(cadreColor);
		panel.setLayout(null);
		
		if(currentElement.equals("Region") )
		{
			double start = 0;
			double len =0;
			for(Region currentRegion:regionList)
			{
				if(currentRegion.getStrand() == '+' && currentRegion.getFrame() == 3)
				{
					JButton regionButton = new JButton(String.valueOf(currentRegion.getType()));
					start = (currentRegion.getStart()/intervalle*PANELWIDTH);
					len = (currentRegion.getLength()/intervalle*PANELWIDTH);
					regionButton.setBounds((int)(start), 10, (int)(len), 30);

					if(currentRegion.getType().equals("gene"))
					{
						regionButton.setBackground(new Color(169, 139, 183));
						regionButton.setForeground(Color.WHITE);
					}
					else if(currentRegion.getType().equals("repeat_region"))
					{
						regionButton.setBackground(new Color(166, 210, 99));
						regionButton.setForeground(Color.WHITE);
					}
					else if(currentRegion.getType().equals("mobile_genetic_element"))
					{
						regionButton.setBackground(new Color(10, 180, 148));
						regionButton.setForeground(Color.WHITE);
					}
					else if(currentRegion.getType().equals("pseudogene"))
					{
						regionButton.setBackground(new Color(197, 157, 8));
						regionButton.setForeground(Color.WHITE);
					}
					else if(currentRegion.getType().equals("origin_of_replication"))
					{
						regionButton.setBackground(new Color(58, 98, 247));
						regionButton.setForeground(Color.WHITE);
					}
					panel.add(regionButton);

					String	info = new String("Information sur : " + currentRegion.getInfo().get("Name") + "\n" );
					Iterator<String> it;
					it = currentRegion.getInfo().keySet().iterator(); 

					String inf = new String(String.valueOf(currentRegion.getStart()));
					String sup = new String(String.valueOf(currentRegion.getEnd()));
					String borne = new String("Borne : [" +inf+"-"+sup+"] bps\n" );
					info = info.concat(borne);


					while(it.hasNext())
					{
						String key = it.next();
						String value = currentRegion.getInfo().get(key);

						String line = new String(key +" : " + value +"\n");
						info = info.concat(line);
					}

					regionButton.addActionListener(new TraitementBut4(info));

				}
			}
		}
		else if(currentElement.equals("Sous-Region"))
		{
			double start = 0;
			double len =0;
			for(SubRegion currentSubRegion:subRegionList)
			{
				if(currentSubRegion.getStrand() == '+' && currentSubRegion.getFrame() == 3)
				{
					JButton subRegionButton = new JButton(String.valueOf(currentSubRegion.getType()));
					start = (currentSubRegion.getStart()/intervalle*PANELWIDTH);
					len = (currentSubRegion.getLength()/intervalle*PANELWIDTH);
					subRegionButton.setBounds((int)(start), 10, (int)(len), 30);

					if(currentSubRegion.getType().equals("CDS"))
					{
						subRegionButton.setBackground(new Color(219, 82, 61));
						subRegionButton.setForeground(Color.WHITE);
					}
					else if(currentSubRegion.getType().equals("antisense_RNA"))
					{
						subRegionButton.setBackground(new Color(123, 144, 197));
						subRegionButton.setForeground(Color.WHITE);
					}
					else if(currentSubRegion.getType().equals("ncRNA"))
					{
						subRegionButton.setBackground(new Color(119, 218, 133));
						subRegionButton.setForeground(Color.WHITE);
					}
					else if(currentSubRegion.getType().equals("rRNA"))
					{
						subRegionButton.setBackground(new Color(226, 152, 59));
						subRegionButton.setForeground(Color.WHITE);
					}
					else if(currentSubRegion.getType().equals("tRNA"))
					{
						subRegionButton.setBackground(new Color(98, 99, 150));
						subRegionButton.setForeground(Color.WHITE);
					}
					else if(currentSubRegion.getType().equals("pseudogenic_tRNA"))
					{
						subRegionButton.setBackground(new Color(109, 21, 13));
						subRegionButton.setForeground(Color.WHITE);
					}
					else if(currentSubRegion.getType().equals("SRP_RNA"))
					{
						subRegionButton.setBackground(new Color(206, 76, 185));
						subRegionButton.setForeground(Color.WHITE);
					}
					else if(currentSubRegion.getType().equals("tmRNA"))
					{
						subRegionButton.setBackground(new Color(168, 131, 67));
						subRegionButton.setForeground(Color.WHITE);
					}
					else if(currentSubRegion.getType().equals("RNase_P_RNA"))
					{
						subRegionButton.setBackground(new Color(22, 48, 112));
						subRegionButton.setForeground(Color.WHITE);
					}
					panel.add(subRegionButton);


					String	info = new String("Information sur : " + currentSubRegion.getInfo().get("ID") + "\n" );
					Iterator<String> it;
					it = currentSubRegion.getInfo().keySet().iterator(); 

					String inf = new String(String.valueOf(currentSubRegion.getStart()));
					String sup = new String(String.valueOf(currentSubRegion.getEnd()));
					String borne = new String("Borne : [" +inf+"-"+sup+"] bps\n" );
					info = info.concat(borne);

					while(it.hasNext())
					{
						String key = it.next();
						String value = currentSubRegion.getInfo().get(key);

						String line = new String(key +" : " + value +"\n");
						info = info.concat(line);
					}

					subRegionButton.addActionListener(new TraitementBut4(info));

				}
			}
		}
		else if(currentElement.equals("Exon"))
		{
			double start = 0;
			double len =0;
			for(SubSubRegion currentSubSubRegion:subSubRegionList)
			{
				if(currentSubSubRegion.getStrand() == '+' && currentSubSubRegion.getFrame() == 3)
				{
					JButton subSubRegionButton = new JButton(String.valueOf(currentSubSubRegion.getType()));
					start = (currentSubSubRegion.getStart()/intervalle*PANELWIDTH);
					len = (currentSubSubRegion.getLength()/intervalle*PANELWIDTH);
					subSubRegionButton.setBounds((int)(start), 10, (int)(len), 30);
					subSubRegionButton.setBackground(new Color(80, 170, 87));
					subSubRegionButton.setForeground(Color.WHITE);
					panel.add(subSubRegionButton);

					String	info = new String("Information sur : " + currentSubSubRegion.getInfo().get("ID") + "\n" );
					Iterator<String> it;
					it = currentSubSubRegion.getInfo().keySet().iterator(); 

					String inf = new String(String.valueOf(currentSubSubRegion.getStart()));
					String sup = new String(String.valueOf(currentSubSubRegion.getEnd()));
					String borne = new String("Borne : [" +inf+"-"+sup+"] bps\n" );

					while(it.hasNext())
					{
						String key = it.next();
						String value = currentSubSubRegion.getInfo().get(key);

						String line = new String(key +" : " + value +"\n");
						info = info.concat(line);
					}

					info = info.concat(borne);

					subSubRegionButton.addActionListener(new TraitementBut4(info));

				}
			}	
		}
		
		return panel;
	}

	//MEME RAISONNEMENT POUR "CADRE 1" CF LIGNE 355-553
	private JPanel Cadre_4(int length)
	{
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(length,60));
		panel.setBackground(cadreColorBis);
		panel.setLayout(null);
		
		if(currentElement.equals("Region") )
		{
			double start = 0;
			double len =0;
			for(Region currentRegion:regionList)
			{
				if(currentRegion.getStrand() == '-' && currentRegion.getFrame() == 1)
				{
					JButton regionButton = new JButton(String.valueOf(currentRegion.getType()));
					start = (currentRegion.getStart()/intervalle*PANELWIDTH);
					len = (currentRegion.getLength()/intervalle*PANELWIDTH);
					regionButton.setBounds((int)(start), 10, (int)(len), 30);

					if(currentRegion.getType().equals("gene"))
					{
						regionButton.setBackground(new Color(169, 139, 183));
						regionButton.setForeground(Color.WHITE);
					}
					else if(currentRegion.getType().equals("repeat_region"))
					{
						regionButton.setBackground(new Color(166, 210, 99));
						regionButton.setForeground(Color.WHITE);
					}
					else if(currentRegion.getType().equals("mobile_genetic_element"))
					{
						regionButton.setBackground(new Color(10, 180, 148));
						regionButton.setForeground(Color.WHITE);
					}
					else if(currentRegion.getType().equals("pseudogene"))
					{
						regionButton.setBackground(new Color(197, 157, 8));
						regionButton.setForeground(Color.WHITE);
					}
					else if(currentRegion.getType().equals("origin_of_replication"))
					{
						regionButton.setBackground(new Color(58, 98, 247));
						regionButton.setForeground(Color.WHITE);
					}
					panel.add(regionButton);

					String	info = new String("Information sur : " + currentRegion.getInfo().get("Name") + "\n" );
					Iterator<String> it;
					it = currentRegion.getInfo().keySet().iterator(); 

					String inf = new String(String.valueOf(currentRegion.getStart()));
					String sup = new String(String.valueOf(currentRegion.getEnd()));
					String borne = new String("Borne : [" +inf+"-"+sup+"] bps\n" );
					info = info.concat(borne);


					while(it.hasNext())
					{
						String key = it.next();
						String value = currentRegion.getInfo().get(key);

						String line = new String(key +" : " + value +"\n");
						info = info.concat(line);
					}

					regionButton.addActionListener(new TraitementBut4(info));

				}
			}
		}
		else if(currentElement.equals("Sous-Region"))
		{
			double start = 0;
			double len =0;
			for(SubRegion currentSubRegion:subRegionList)
			{
				if(currentSubRegion.getStrand() == '-' && currentSubRegion.getFrame() == 1)
				{
					JButton subRegionButton = new JButton(String.valueOf(currentSubRegion.getType()));
					start = (currentSubRegion.getStart()/intervalle*PANELWIDTH);
					len = (currentSubRegion.getLength()/intervalle*PANELWIDTH);
					subRegionButton.setBounds((int)(start), 10, (int)(len), 30);

					if(currentSubRegion.getType().equals("CDS"))
					{
						subRegionButton.setBackground(new Color(219, 82, 61));
						subRegionButton.setForeground(Color.WHITE);
					}
					else if(currentSubRegion.getType().equals("antisense_RNA"))
					{
						subRegionButton.setBackground(new Color(123, 144, 197));
						subRegionButton.setForeground(Color.WHITE);
					}
					else if(currentSubRegion.getType().equals("ncRNA"))
					{
						subRegionButton.setBackground(new Color(119, 218, 133));
						subRegionButton.setForeground(Color.WHITE);
					}
					else if(currentSubRegion.getType().equals("rRNA"))
					{
						subRegionButton.setBackground(new Color(226, 152, 59));
						subRegionButton.setForeground(Color.WHITE);
					}
					else if(currentSubRegion.getType().equals("tRNA"))
					{
						subRegionButton.setBackground(new Color(98, 99, 150));
						subRegionButton.setForeground(Color.WHITE);
					}
					else if(currentSubRegion.getType().equals("pseudogenic_tRNA"))
					{
						subRegionButton.setBackground(new Color(109, 21, 13));
						subRegionButton.setForeground(Color.WHITE);
					}
					else if(currentSubRegion.getType().equals("SRP_RNA"))
					{
						subRegionButton.setBackground(new Color(206, 76, 185));
						subRegionButton.setForeground(Color.WHITE);
					}
					else if(currentSubRegion.getType().equals("tmRNA"))
					{
						subRegionButton.setBackground(new Color(168, 131, 67));
						subRegionButton.setForeground(Color.WHITE);
					}
					else if(currentSubRegion.getType().equals("RNase_P_RNA"))
					{
						subRegionButton.setBackground(new Color(22, 48, 112));
						subRegionButton.setForeground(Color.WHITE);
					}
					panel.add(subRegionButton);


					String	info = new String("Information sur : " + currentSubRegion.getInfo().get("ID") + "\n" );
					Iterator<String> it;
					it = currentSubRegion.getInfo().keySet().iterator(); 


					String inf = new String(String.valueOf(currentSubRegion.getStart()));
					String sup = new String(String.valueOf(currentSubRegion.getEnd()));
					String borne = new String("Borne : [" +inf+"-"+sup+"] bps\n" );
					info = info.concat(borne);


					while(it.hasNext())
					{
						String key = it.next();
						String value = currentSubRegion.getInfo().get(key);

						String line = new String(key +" : " + value +"\n");
						info = info.concat(line);
					}

					subRegionButton.addActionListener(new TraitementBut4(info));

				}
			}
		}
		else if(currentElement.equals("Exon"))
		{
			double start = 0;
			double len =0;
			for(SubSubRegion currentSubSubRegion:subSubRegionList)
			{
				if(currentSubSubRegion.getStrand() == '-' && currentSubSubRegion.getFrame() == 1)
				{
					JButton subSubRegionButton = new JButton(String.valueOf(currentSubSubRegion.getType()));
					start = (currentSubSubRegion.getStart()/intervalle*PANELWIDTH);
					len = (currentSubSubRegion.getLength()/intervalle*PANELWIDTH);
					subSubRegionButton.setBounds((int)(start), 10, (int)(len), 30);
					subSubRegionButton.setBackground(new Color(80, 170, 87));
					subSubRegionButton.setForeground(Color.WHITE);
					panel.add(subSubRegionButton);

					String	info = new String("Information sur : " + currentSubSubRegion.getInfo().get("ID") + "\n" );
					Iterator<String> it;
					it = currentSubSubRegion.getInfo().keySet().iterator(); 


					String inf = new String(String.valueOf(currentSubSubRegion.getStart()));
					String sup = new String(String.valueOf(currentSubSubRegion.getEnd()));
					String borne = new String("Borne : [" +inf+"-"+sup+"] bps\n" );
					info = info.concat(borne);

					while(it.hasNext())
					{
						String key = it.next();
						String value = currentSubSubRegion.getInfo().get(key);

						String line = new String(key +" : " + value +"\n");
						info = info.concat(line);
					}

					subSubRegionButton.addActionListener(new TraitementBut4(info));

				}
			}	
		}
		return panel;
	}

//MEME RAISONNEMENT POUR "CADRE 1" CF LIGNE 355-553
	private JPanel Cadre_5(int length)
	{
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(length,60));
		panel.setBackground(cadreColor);
		panel.setLayout(null);
		
		
		
		if(currentElement.equals("Region") )
		{
			double start = 0;
			double len =0;
			for(Region currentRegion:regionList)
			{
				if(currentRegion.getStrand() == '-' && currentRegion.getFrame() == 2)
				{
					JButton regionButton = new JButton(String.valueOf(currentRegion.getType()));
					start = (currentRegion.getStart()/intervalle*PANELWIDTH);
					len = (currentRegion.getLength()/intervalle*PANELWIDTH);
					regionButton.setBounds((int)(start), 10, (int)(len), 30);

					if(currentRegion.getType().equals("gene"))
					{
						regionButton.setBackground(new Color(169, 139, 183));
						regionButton.setForeground(Color.WHITE);
					}
					else if(currentRegion.getType().equals("repeat_region"))
					{
						regionButton.setBackground(new Color(166, 210, 99));
						regionButton.setForeground(Color.WHITE);
					}
					else if(currentRegion.getType().equals("mobile_genetic_element"))
					{
						regionButton.setBackground(new Color(10, 180, 148));
						regionButton.setForeground(Color.WHITE);
					}
					else if(currentRegion.getType().equals("pseudogene"))
					{
						regionButton.setBackground(new Color(197, 157, 8));
						regionButton.setForeground(Color.WHITE);
					}
					else if(currentRegion.getType().equals("origin_of_replication"))
					{
						regionButton.setBackground(new Color(58, 98, 247));
						regionButton.setForeground(Color.WHITE);
					}
					panel.add(regionButton);

					String	info = new String("Information sur : " + currentRegion.getInfo().get("Name") + "\n" );
					Iterator<String> it;
					it = currentRegion.getInfo().keySet().iterator(); 


					String inf = new String(String.valueOf(currentRegion.getStart()));
					String sup = new String(String.valueOf(currentRegion.getEnd()));
					String borne = new String("Borne : [" +inf+"-"+sup+"] bps\n" );
					info = info.concat(borne);

					while(it.hasNext())
					{
						String key = it.next();
						String value = currentRegion.getInfo().get(key);

						String line = new String(key +" : " + value +"\n");
						info = info.concat(line);
					}

					regionButton.addActionListener(new TraitementBut4(info));

				}
			}
		}
		else if(currentElement.equals("Sous-Region"))
		{
			double start = 0;
			double len =0;
			for(SubRegion currentSubRegion:subRegionList)
			{
				if(currentSubRegion.getStrand() == '-' && currentSubRegion.getFrame() == 2)
				{
					JButton subRegionButton = new JButton(String.valueOf(currentSubRegion.getType()));
					start = (currentSubRegion.getStart()/intervalle*PANELWIDTH);
					len = (currentSubRegion.getLength()/intervalle*PANELWIDTH);
					subRegionButton.setBounds((int)(start), 10, (int)(len), 30);

					if(currentSubRegion.getType().equals("CDS"))
					{
						subRegionButton.setBackground(new Color(219, 82, 61));
						subRegionButton.setForeground(Color.WHITE);
					}
					else if(currentSubRegion.getType().equals("antisense_RNA"))
					{
						subRegionButton.setBackground(new Color(123, 144, 197));
						subRegionButton.setForeground(Color.WHITE);
					}
					else if(currentSubRegion.getType().equals("ncRNA"))
					{
						subRegionButton.setBackground(new Color(119, 218, 133));
						subRegionButton.setForeground(Color.WHITE);
					}
					else if(currentSubRegion.getType().equals("rRNA"))
					{
						subRegionButton.setBackground(new Color(226, 152, 59));
						subRegionButton.setForeground(Color.WHITE);
					}
					else if(currentSubRegion.getType().equals("tRNA"))
					{
						subRegionButton.setBackground(new Color(98, 99, 150));
						subRegionButton.setForeground(Color.WHITE);
					}
					else if(currentSubRegion.getType().equals("pseudogenic_tRNA"))
					{
						subRegionButton.setBackground(new Color(109, 21, 13));
						subRegionButton.setForeground(Color.WHITE);
					}
					else if(currentSubRegion.getType().equals("SRP_RNA"))
					{
						subRegionButton.setBackground(new Color(206, 76, 185));
						subRegionButton.setForeground(Color.WHITE);
					}
					else if(currentSubRegion.getType().equals("tmRNA"))
					{
						subRegionButton.setBackground(new Color(168, 131, 67));
						subRegionButton.setForeground(Color.WHITE);
					}
					else if(currentSubRegion.getType().equals("RNase_P_RNA"))
					{
						subRegionButton.setBackground(new Color(22, 48, 112));
						subRegionButton.setForeground(Color.WHITE);
					}
					panel.add(subRegionButton);


					String	info = new String("Information sur : " + currentSubRegion.getInfo().get("ID") + "\n" );
					Iterator<String> it;
					it = currentSubRegion.getInfo().keySet().iterator(); 

					String inf = new String(String.valueOf(currentSubRegion.getStart()));
					String sup = new String(String.valueOf(currentSubRegion.getEnd()));
					String borne = new String("Borne : [" +inf+"-"+sup+"] bps\n" );
					info = info.concat(borne);

					while(it.hasNext())
					{
						String key = it.next();
						String value = currentSubRegion.getInfo().get(key);

						String line = new String(key +" : " + value +"\n");
						info = info.concat(line);
					}

					subRegionButton.addActionListener(new TraitementBut4(info));

				}
			}
		}
		else if(currentElement.equals("Exon"))
		{
			double start = 0;
			double len =0;
			for(SubSubRegion currentSubSubRegion:subSubRegionList)
			{
				if(currentSubSubRegion.getStrand() == '-' && currentSubSubRegion.getFrame() == 2)
				{
					JButton subSubRegionButton = new JButton(String.valueOf(currentSubSubRegion.getType()));
					start = (currentSubSubRegion.getStart()/intervalle*PANELWIDTH);
					len = (currentSubSubRegion.getLength()/intervalle*PANELWIDTH);
					subSubRegionButton.setBounds((int)(start), 10, (int)(len), 30);
					subSubRegionButton.setBackground(new Color(80, 170, 87));
					subSubRegionButton.setForeground(Color.WHITE);
					panel.add(subSubRegionButton);

					String	info = new String("Information sur : " + currentSubSubRegion.getInfo().get("ID") + "\n" );
					Iterator<String> it;
					it = currentSubSubRegion.getInfo().keySet().iterator(); 

					String inf = new String(String.valueOf(currentSubSubRegion.getStart()));
					String sup = new String(String.valueOf(currentSubSubRegion.getEnd()));
					String borne = new String("Borne : [" +inf+"-"+sup+"] bps\n" );
					info = info.concat(borne);

					while(it.hasNext())
					{
						String key = it.next();
						String value = currentSubSubRegion.getInfo().get(key);

						String line = new String(key +" : " + value +"\n");
						info = info.concat(line);
					}

					subSubRegionButton.addActionListener(new TraitementBut4(info));

				}
			}	
		}
		
		return panel;
	}

	//MEME RAISONNEMENT POUR "CADRE 1" CF LIGNE 355-553
	private JPanel Cadre_6(int length)
	{
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(length,60));
		panel.setBackground(cadreColorBis);
		panel.setLayout(null);
		
		if(currentElement.equals("Region") )
		{
			double start = 0;
			double len =0;
			for(Region currentRegion:regionList)
			{
				if(currentRegion.getStrand() == '-' && currentRegion.getFrame() == 3)
				{
					JButton regionButton = new JButton(String.valueOf(currentRegion.getType()));
					start = (currentRegion.getStart()/intervalle*PANELWIDTH);
					len = (currentRegion.getLength()/intervalle*PANELWIDTH);
					regionButton.setBounds((int)(start), 10, (int)(len), 30);

					if(currentRegion.getType().equals("gene"))
					{
						regionButton.setBackground(new Color(169, 139, 183));
						regionButton.setForeground(Color.WHITE);
					}
					else if(currentRegion.getType().equals("repeat_region"))
					{
						regionButton.setBackground(new Color(166, 210, 99));
						regionButton.setForeground(Color.WHITE);
					}
					else if(currentRegion.getType().equals("mobile_genetic_element"))
					{
						regionButton.setBackground(new Color(10, 180, 148));
						regionButton.setForeground(Color.WHITE);
					}
					else if(currentRegion.getType().equals("pseudogene"))
					{
						regionButton.setBackground(new Color(197, 157, 8));
						regionButton.setForeground(Color.WHITE);
					}
					else if(currentRegion.getType().equals("origin_of_replication"))
					{
						regionButton.setBackground(new Color(58, 98, 247));
						regionButton.setForeground(Color.WHITE);
					}
					panel.add(regionButton);

					String	info = new String("Information sur : " + currentRegion.getInfo().get("Name") + "\n" );
					Iterator<String> it;
					it = currentRegion.getInfo().keySet().iterator(); 

					String inf = new String(String.valueOf(currentRegion.getStart()));
					String sup = new String(String.valueOf(currentRegion.getEnd()));
					String borne = new String("Borne : [" +inf+"-"+sup+"] bps\n" );
					info = info.concat(borne);

					while(it.hasNext())
					{
						String key = it.next();
						String value = currentRegion.getInfo().get(key);

						String line = new String(key +" : " + value +"\n");
						info = info.concat(line);
					}

					regionButton.addActionListener(new TraitementBut4(info));

				}
			}
		}
		else if(currentElement.equals("Sous-Region"))
		{
			double start = 0;
			double len =0;
			for(SubRegion currentSubRegion:subRegionList)
			{
				if(currentSubRegion.getStrand() == '-' && currentSubRegion.getFrame() == 3)
				{
					JButton subRegionButton = new JButton(String.valueOf(currentSubRegion.getType()));
					start = (currentSubRegion.getStart()/intervalle*PANELWIDTH);
					len = (currentSubRegion.getLength()/intervalle*PANELWIDTH);
					subRegionButton.setBounds((int)(start), 10, (int)(len), 30);

					if(currentSubRegion.getType().equals("CDS"))
					{
						subRegionButton.setBackground(new Color(219, 82, 61));
						subRegionButton.setForeground(Color.WHITE);
					}
					else if(currentSubRegion.getType().equals("antisense_RNA"))
					{
						subRegionButton.setBackground(new Color(123, 144, 197));
						subRegionButton.setForeground(Color.WHITE);
					}
					else if(currentSubRegion.getType().equals("ncRNA"))
					{
						subRegionButton.setBackground(new Color(119, 218, 133));
						subRegionButton.setForeground(Color.WHITE);
					}
					else if(currentSubRegion.getType().equals("rRNA"))
					{
						subRegionButton.setBackground(new Color(226, 152, 59));
						subRegionButton.setForeground(Color.WHITE);
					}
					else if(currentSubRegion.getType().equals("tRNA"))
					{
						subRegionButton.setBackground(new Color(98, 99, 150));
						subRegionButton.setForeground(Color.WHITE);
					}
					else if(currentSubRegion.getType().equals("pseudogenic_tRNA"))
					{
						subRegionButton.setBackground(new Color(109, 21, 13));
						subRegionButton.setForeground(Color.WHITE);
					}
					else if(currentSubRegion.getType().equals("SRP_RNA"))
					{
						subRegionButton.setBackground(new Color(206, 76, 185));
						subRegionButton.setForeground(Color.WHITE);
					}
					else if(currentSubRegion.getType().equals("tmRNA"))
					{
						subRegionButton.setBackground(new Color(168, 131, 67));
						subRegionButton.setForeground(Color.WHITE);
					}
					else if(currentSubRegion.getType().equals("RNase_P_RNA"))
					{
						subRegionButton.setBackground(new Color(22, 48, 112));
						subRegionButton.setForeground(Color.WHITE);
					}
					panel.add(subRegionButton);


					String	info = new String("Information sur : " + currentSubRegion.getInfo().get("ID") + "\n" );
					Iterator<String> it;
					it = currentSubRegion.getInfo().keySet().iterator(); 

					String inf = new String(String.valueOf(currentSubRegion.getStart()));
					String sup = new String(String.valueOf(currentSubRegion.getEnd()));
					String borne = new String("Borne : [" +inf+"-"+sup+"] bps\n" );
					info = info.concat(borne);

					while(it.hasNext())
					{
						String key = it.next();
						String value = currentSubRegion.getInfo().get(key);

						String line = new String(key +" : " + value +"\n");
						info = info.concat(line);
					}

					subRegionButton.addActionListener(new TraitementBut4(info));

				}
			}
		}
		else if(currentElement.equals("Exon"))
		{
			double start = 0;
			double len =0;
			for(SubSubRegion currentSubSubRegion:subSubRegionList)
			{
				if(currentSubSubRegion.getStrand() == '-' && currentSubSubRegion.getFrame() == 3)
				{
					JButton subSubRegionButton = new JButton(String.valueOf(currentSubSubRegion.getType()));
					start = (currentSubSubRegion.getStart()/intervalle*PANELWIDTH);
					len = (currentSubSubRegion.getLength()/intervalle*PANELWIDTH);
					subSubRegionButton.setBounds((int)(start), 10, (int)(len), 30);
					subSubRegionButton.setBackground(new Color(80, 170, 87));
					subSubRegionButton.setForeground(Color.WHITE);
					panel.add(subSubRegionButton);

					String	info = new String("Information sur : " + currentSubSubRegion.getInfo().get("ID") + "\n" );
					Iterator<String> it;
					it = currentSubSubRegion.getInfo().keySet().iterator(); 


					String inf = new String(String.valueOf(currentSubSubRegion.getStart()));
					String sup = new String(String.valueOf(currentSubSubRegion.getEnd()));
					String borne = new String("Borne : [" +inf+"-"+sup+"] bps\n" );
					info = info.concat(borne);


					while(it.hasNext())
					{
						String key = it.next();
						String value = currentSubSubRegion.getInfo().get(key);

						String line = new String(key +" : " + value +"\n");
						info = info.concat(line);
					}

					subSubRegionButton.addActionListener(new TraitementBut4(info));

				}
			}	
		}
		
		return panel;
	}

	// CLASSE INTERNE TRAITEMENT DU BOUTON 1 (CHOIX DE L'INTERVALLE)
	public class TraitementBut1 implements ActionListener
	{
		public void actionPerformed(ActionEvent arg0)
	  {
			try
			{
				debutFrame = Integer.parseInt(debut.getText());  // Recuperation du nombre entré par l'utilisateur
				finFrame = Integer.parseInt(fin.getText());		// Recuperation du nombre entré par l'utilisateur
				if(debutFrame>finFrame) // Si la fin est plus petite que le debut
				{
					debutFrame = 0;
					finFrame = 5000; // Remise de l'intervale initiale
					build(); // Raffraichissement de l'interface
					fin.setBorder(BorderFactory.createLineBorder(Color.RED)); //Coloration des bordures des zones de textes en rouge
					debut.setBorder(BorderFactory.createLineBorder(Color.RED));
					debut.setText("inf"); // Insertion de "inf" et "sup" dans les zone de texte debut et fin respectivement
					fin.setText("sup");
					JOptionPane.showMessageDialog(null,"La borne \"Début\" doit etre inferieur à la borne \"Fin\" !","Erreur",JOptionPane.ERROR_MESSAGE); // Affichage d'une fenetre de dialogue de type "ERROR_MESSAGE"

				}
				else // Sinon
				{
					build(); //Raffraichissment de l'interface avec l'intervalle donné par l'utilsateur
					fin.setBorder(BorderFactory.createLineBorder(Color.WHITE)); // Remet les bordure des zones de textes en blanc
					debut.setBorder(BorderFactory.createLineBorder(Color.WHITE));
				}
				if(finFrame-debutFrame < 1) // Si l'ecart entre le debut et fin < 1
				{
					debutFrame = 0;  
					finFrame = 5000; //Remise a l'intervale initiale
					build();	// Raffraichissement de l'interface
					fin.setBorder(BorderFactory.createLineBorder(Color.RED)); //Coloration des bordures des zones de textes en rouge
					debut.setBorder(BorderFactory.createLineBorder(Color.RED));
					JOptionPane.showMessageDialog(null,"L'intervale limite supporté (debut-fin) est de 2 paires de bases !","Erreur",JOptionPane.ERROR_MESSAGE);// Affichage d'une fenetre de dialogue de type "ERROR_MESSAGE"
				}
				if(finFrame < 0 || debutFrame < 0 || (finFrame < 0 && debutFrame < 0)) // Si l'utilisateur entre des nombres negatifs dans les zones de textes "debut" et "fin"
				{
					debutFrame = 0;
					finFrame = 5000;
					build();
					fin.setBorder(BorderFactory.createLineBorder(Color.RED));
					debut.setBorder(BorderFactory.createLineBorder(Color.RED));
					JOptionPane.showMessageDialog(null,"L'intervale doit etre compris entre 0 et "+String.valueOf(genomeLength)+" paires de bases","Erreur",JOptionPane.ERROR_MESSAGE);
				}
				if(finFrame > genomeLength || debutFrame > genomeLength)
				{
					debutFrame = 0;
					finFrame = 5000; //Remise a l'intervale initiale
					build();		// Raffraichissement de l'interface
					fin.setBorder(BorderFactory.createLineBorder(Color.RED)); //Coloration des bordures des zones de textes en rouge
					debut.setBorder(BorderFactory.createLineBorder(Color.RED));
					JOptionPane.showMessageDialog(null,"Vous avez depassé la limite maximal du genome qui est de "+String.valueOf(genomeLength)+" paires de bases","Erreur",JOptionPane.ERROR_MESSAGE); // Affichage d'une fenetre de dialogue de type "ERROR_MESSAGE"
				}
			}
			catch (NumberFormatException e) // Si ce n'est pas des nombres qui sont entrés dans les zones de textes "debut" et "fin"
			{
				fin.setBorder(BorderFactory.createLineBorder(Color.RED)); //Coloration des bordures des zones de textes en rouge
				debut.setBorder(BorderFactory.createLineBorder(Color.RED));
				debut.setText("0"); //Remise a l'intervale initiale
				fin.setText("5000");
				JOptionPane.showMessageDialog(null,"Vous devez rentrer des nombre dans le champ \"Début\" et dans le champ \"Fin\" !","Erreur",JOptionPane.ERROR_MESSAGE);// Affichage d'une fenetre de dialogue de type "ERROR_MESSAGE"
			}
     
	  }
	}
	
	// CLASSE INTERNE TRAITEMENT DU BOUTON 2 (Recherche d'un element)
	public class TraitementBut2 implements ActionListener
	{
		public void actionPerformed(ActionEvent arg0)
	  {
			String queryGene = id.getText(); // Recuperation de l'entrée de l'utilisateur dans une chaine de caracetere
			boolean find = false; // booleen qui permet de savoir si le gene recherché est bien present dans le genome
			for(Region currentRegion:regionList) // Pour chaques regions presentes dans regionList
			{
				String researchedGene = currentRegion.getInfo().get("Name"); // On recupere le nom de l'element
				if(queryGene.equals(researchedGene)) // Si le nom de l'element est equivalente a la requete
				{
					id.setBorder(BorderFactory.createLineBorder(Color.WHITE)); // Remet les bordures de la zones de texte en blanc
					JScrollBar horizontalScrollBar = genomeScroller.getHorizontalScrollBar(); // Recuperation de la scrollbar du panel scrollable
					horizontalScrollBar.setValue((int)(currentRegion.getStart()/intervalle*PANELWIDTH));// Positionne la scrollbar a l'emplacement de l'element recherché
					find = true; // Booleen mis sur vrai
				}
			}
			if(!find) // Si le booleen est resté faux
			{
				id.setBorder(BorderFactory.createLineBorder(Color.RED)); //Coloration des bordures des zones de textes en rouge
				JOptionPane.showMessageDialog(null,"Le gene rentré est inconnu !","Erreur",JOptionPane.ERROR_MESSAGE); // Affichage d'une fenetre de dialogue de type "ERROR_MESSAGE"
			}
		}
	}

	//CLASSE INTERNE TRAITEMENT DU BOUTON 3 (Choix du type d'element à afficher)
	public class TraitementBut3 implements ActionListener
	{
		public void actionPerformed(ActionEvent arg0)
	  {
			currentElement = String.valueOf(chooseViewedElement.getSelectedItem()); // Recuperation de la selection de l'utilisateur
			build(); // Raffraichissement de l'interface
		}
	}

	//CLASSE INTERNE TRAITEMENT DU BOUTON 4 (Obtentions d'informations sur l'element selectionné)
	public class TraitementBut4 implements ActionListener
	{
		String Informations; // Necéssite la chaine de caractere possedant toutes les informations sur l'element selectionné

		public TraitementBut4(String Informations)
		{
			this.Informations = Informations;
		}
		public void actionPerformed(ActionEvent arg0)
		{
			JOptionPane.showMessageDialog(null, Informations ,"Informations sur l'element",JOptionPane.INFORMATION_MESSAGE); // Affichage des informations de l'element dans une fenetre dialogue de type "INFORMATION_MESSAGE"
		}

	}

}




