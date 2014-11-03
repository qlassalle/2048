import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;


public class Plateau implements ActionListener{
    JFrame fen = new JFrame();// instance de la fenêtre
    int i = 0 , j = 0, nbColTab = 4, nbLigneTab = 4, tabInt[][] = new int[nbLigneTab][nbColTab];
    int espacementG = 20, espacementH = 5, largeur = 80, hauteurNom = 45, hauteurChiffres = 30;
    int placementGScore = 250, placementH = 5, placementGMeilleur = 400, PlacementHChiffres = 50;
    int hauteurLogo = (hauteurNom*2+hauteurChiffres*2)/2; // Toutes ces valeurs sont pour les setBounds, cela permet
    // de les modifier bien plus facilement
	int extremite = 0, generAleatoire = 0, cpt = 0, score =0, fin = 0;
    boolean estVide = (true), verif = (false), detruit = (false), deplacePossible =(false);
	long colAleatoire, ligneAleatoire, nbAleatoire1, nbAleatoire2;
	private JLabel[][] tab = new JLabel[nbColTab][nbLigneTab];// instance du tableau
    JPanel plateau = new JPanel(); // On crée un JPanel pour le plateau et ensuite pour l'entête
    JPanel entete = new JPanel();
    JLabel[] tabEntete = new JLabel[5];
    Plateau p;
    
    public Plateau(){
    	initFenetre();
       	initEntete();
       	initPlateau();
    }

    public static void main(String[] args) {
		Plateau p = new Plateau();
		p.init();
		p.couleurTuile();
		p.toJLabel();
	}
	public JLabel creerJLabel(){
        JLabel jl = new JLabel("", JLabel.CENTER);// On crée noter JLabel et on centre le texte
        jl.setFont(new Font("Arial", Font.PLAIN, 50)); // On choisit sa police et on la met à 30
        jl.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // On affiche des bordures que l'on utilise comme des grilles
        jl.setOpaque(true); // On set opaque pour pouvoir mettre un background
        jl.setBackground(new Color(204,204,169));
        return jl;
    }
	public void initFenetre()
	{
    	fen.setTitle("2048");
        fen.setSize(600,600);
        fen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fen.setResizable(false);// Pas de redimensionnement
        fen.setLocationRelativeTo(null);// Pour centrer
        fen.setLayout(null);// 2 lignes 1 colonne
        fen.setVisible(true);
        fen.addKeyListener(new ClavierListener(this)); // On ajoute le KeyListener à la fenêtre et on transmet l'objet p
	}
	
	public void initEntete()
	{
        entete.setBackground(new Color(253,252,245));
        entete.setLayout(null);// null pour placer ensuite librement
        tabEntete[0] = new JLabel("2048", JLabel.CENTER); // On crée le logo
        tabEntete[0].setFont(new Font("Arial", Font.PLAIN, 30)); // On définit police et taille
        tabEntete[0].setForeground(Color.WHITE); // Police de couleur blanche
        tabEntete[0].setOpaque(true); // Pour pouvoir définir un background à la police
        tabEntete[0].setBackground(new Color(235,205,82)); // Background de fond jaune
        
        tabEntete[1] = new JLabel("SCORE", JLabel.CENTER);
        tabEntete[2] = new JLabel("MEILLEUR", JLabel.CENTER);
        tabEntete[3] = new JLabel("0", JLabel.CENTER);
        tabEntete[4] = new JLabel("0", JLabel.CENTER);
        // Création des JLabel
        for(i=1; i < 5 ; i++)
        {
        	tabEntete[i].setOpaque(true);
        	tabEntete[i].setBackground(new Color(199,188,188));
        	// On leur attribue un fond marron dans cette boucle
        }
        for (i=0; i < 5 ; i++)
        {
	        entete.add(tabEntete[i]);
	        // On ajoute chaque JLabel au JPanel entete
        }
        tabEntete[0].setBounds(espacementG, espacementH, largeur, hauteurLogo);
       	tabEntete[1].setBounds(placementGScore, espacementH, largeur, hauteurNom);
       	tabEntete[2].setBounds(placementGMeilleur, placementH, largeur, hauteurNom);
       	tabEntete[3].setBounds(placementGScore, PlacementHChiffres, largeur, hauteurChiffres);
       	tabEntete[4].setBounds(placementGMeilleur, PlacementHChiffres, largeur, hauteurChiffres);
       	// On positionne les JLabel de manière totalement libre
       	entete.setBounds(0, 0, 600, 100);
       	// On lui attribue la taille que l'on souhaite
        fen.add(entete);
        // Et enfin on l'ajoute a notre JFrame !
	}
	public void initPlateau()
	{
        plateau.setBackground(new Color(204,204,169));
        plateau.setLayout(new GridLayout(4,4,1,1));// col/ligne/espacement vertical/horizontal
        plateau.setBorder(BorderFactory.createEmptyBorder(2,2,2,2)); // On crée une bordure pour faire notre grille
       	plateau.setBounds(0, 100, 595, 470);
        for(i = 0; i < nbLigneTab;i++)
        {
        	for(j=0; j < nbColTab ; j++)
        	{
	            tab[i][j]= creerJLabel();// on crée les JLabel et on les met dans tab
        		plateau.add(tab[i][j]);// On ajoute chaque JLabel au plateau
        	}
        }
        fen.add(plateau);// on ajoute notre plateau à la fenêtre
        plateau.addKeyListener(new ClavierListener(p));// On ajoute le KeyListener au plateau et on transmet l'objet p
	}
	public void couleurTuile()
	{
		for (i = 0; i < nbLigneTab; i++)
		{
			for(j=0; j < nbColTab; j++)
			{
				int couleur = tabInt[i][j];
				// En fonction du contenu des cases du tableau d'entier, on attribue la couleur dans le tableau de JLabel
				// Autrement dit, le plateau dans la fenêtre
				switch(couleur)
				{
				case 0: tab[i][j].setBackground(new Color(204,204,169));
				break;
				case 2 : tab[i][j].setBackground(new Color(224,224,224));
				break;
				case 4 : tab[i][j].setBackground(new Color(202,202,202));
				break;
				case 8 : tab[i][j].setBackground(new Color(247,208,119));
				break;
				case 16 : tab[i][j].setBackground(new Color(242,150,119));
				break;
				case 32 : tab[i][j].setBackground(new Color(238,117,77));
				break;
				case 64 : tab[i][j].setBackground(new Color(255,72,72));
				break;
				case 128 : tab[i][j].setBackground(new Color(250,245,165));
				break;
				case 256 : tab[i][j].setBackground(new Color(248,242,135));
				break;
				case 512 : tab[i][j].setBackground(new Color(244,237,91));
				break;
				case 1024 : tab[i][j].setBackground(new Color(242,233,53));
				break;
				case 2048 : tab[i][j].setBackground(new Color(227,217,15));
				break;
				default : tab[i][j].setBackground(new Color(48,182,10));
				break;
				}
				// On définit une couleur de tuile à chaque case en fonction de sa valeur
			}
		}
	}
	public void toInt()
	{
		detruit = (false);
		for(i = 0; i < nbLigneTab; i++)
		{
			for (j = 0; j < nbColTab; j++)
			{
				try
				{
					tabInt[i][j] = Integer.parseInt(tab[i][j].getText());
					// On attribue à la case de notre tableau d'entier la valeur contenue dans notre tableau de JLabel, 
					// on la parse en int car un tableau de JLabel contient des valeurs de type String
				}
				catch(NumberFormatException e)
				{
					tabInt[i][j] = 0;
					// Si on essaye de parser quelque chose de vide, on a un NumberFormatException, on catch l'exception et on met alors le 0 dans tabInt
				}
			}
		}
	}
	public void toJLabel()
	{
		for(i = 0; i < nbLigneTab; i++)
		{
			for (j = 0; j < nbColTab; j++)
			{
				if (tabInt[i][j] == 0)
				{
					tab[i][j].setText("");
					// Cette fois on transmet au tableau de JLabel le contenu de tabInt
				}
				else
				{
					tab[i][j].setText(Integer.toString(tabInt[i][j]));
					// Et ici on parse dans l'autre sens, le tableau de JLabel doit contenir des valeurs de type String
					// On utilise donc la méthode toString de la classe Integer
				}
			}
		}
		tabEntete[3].setText(Integer.toString(score));
		// On affiche la valeur du score que l'on passe ici aussi de int à String pour répéter le format des JLabel
	}
	public void init()
	{
		for(i = 0; i < nbLigneTab; i++)
		{
			for(j = 0; j < nbColTab; j++)
			{
				tabInt[i][j] = 0;
			}
		}
		// on initialise de base toutes les cases à 0
		for(generAleatoire = 0; generAleatoire < 2; generAleatoire++)
		{
			colAleatoire = Math.round(Math.random()*3);
			ligneAleatoire = Math.round(Math.random()*3);
			int col = (int)colAleatoire;
			int ligne = (int)ligneAleatoire;
			// on détermine une ligne et une colonne aléatoire, Math.random sort un nombre de type float
			// On les parse en int pour le tableau (ici on fait *3 a cause du décalage d'indice)
			nbAleatoire1 = Math.round(Math.random()*4);
			// On choisit alors deux nombres aléatoires (*4 car Math random sort un nombre entre 0 et 1
			while(!verif)
			{
				if(nbAleatoire1 == 0 || nbAleatoire1 == 1 || nbAleatoire1 == 3)
				{
						nbAleatoire1 = Math.round(Math.random()*4);
						i = (int)nbAleatoire1;
						// Le nombre n'est pas un 2 ou un 4 on rechoisit alors aléatoirement
				}
				else
					verif = (true);
					i = (int)nbAleatoire1;
				//Si c'est un 2 ou 4 on en a un premier de bon => verif passe à true
			}
			if (tabInt[ligne][col] != 0)
				generAleatoire -= 1;
			// Si la case choisie aléatoirement contient déjà un nombre il faut recommencer
			else
				tabInt[ligne][col] = i;
				verif = (false);
				// Si elle est vide on lui attribue le nombre choisi aléatoirement
		}
	}
	public void generAleatoire()
	{
		verif = (false);
		do
		{
		colAleatoire = Math.round(Math.random()*3);
		ligneAleatoire = Math.round(Math.random()*3);
		int col = (int)colAleatoire;
		int ligne = (int)ligneAleatoire;
		nbAleatoire1 = Math.round(Math.random()*4);
		i = (int)nbAleatoire1;
		while(!verif)
		{
			if(nbAleatoire1 == 0 || nbAleatoire1 == 1 || nbAleatoire1 == 3)
			{
					nbAleatoire1 = Math.round(Math.random()*4);
					i = (int)nbAleatoire1;
			}
			else
				verif = (true);
		}
		if (tabInt[ligne][col] != 0)
		{
			verif = (false);
		}
		else
		{
			tabInt[ligne][col] = i;
			verif = (true);
		}
		}while(!verif && cpt != 16);
		// Exactement le même raisonnement que pour init, on vérifie que cpt n'est pas à 16 pour éviter la boucle infinie
	}
	public void finJeu()
	{
		cpt = 0;
		for(i = 0; i < nbLigneTab; i++)
		{
			for (j= 0; j < nbColTab; j++)
			{
				// On vérifie case par case qu'aucune ne vaut 0
				if (tabInt[i][j] != 0)
					cpt +=1;
			}
		}
		if (cpt == 16)
		{
			// Aucune case ne valant 0, on va vérifier si il est encore possible de faire des déplacements
			for(i = 0; i < nbLigneTab; i++)
			{
				for(j = 0; j < nbColTab - 1; j++)
				{
					//Ici on fait les tests horizontaux, la tuile de gauche comparée à celle de droite
					if (tabInt[i][j] == tabInt[i][j+1])
						return;
						//Si un mouvement est possible, on quitte la méthode
				}
			}
			for(j = 0; j< nbColTab; j++)
			{
				for(i = 0; i < nbLigneTab - 1; i++)
				{
					// Ici ce sont les tests verticaux, on compare la tuile du haut avec celle d'en dessous
					if (tabInt[i+1][j] == tabInt[i][j])
						return;
						//Si un mouvement est possible, on quitte la méthode
				}
			}
			if (fin == 0)
			{
				// On crée une fenêtre qui indique à l'utilisateur qu'il ne peut plus faire de mouvement
				JFrame finJeu = new JFrame("Fin du jeu");
				finJeu.setLayout(new FlowLayout());
				finJeu.setVisible(true);
		        finJeu.setSize(500,50);
		        finJeu.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Dispose_on_close libère les ressources sans fermer tout
		        finJeu.setResizable(false);
		        finJeu.setLocationRelativeTo(null);
		        JLabel fin = new JLabel("Plus aucun mouvement n'est possible ! ");
				finJeu.add(fin);
				// Aucun mouvement n'est possible, fin de la partie ! 
			}
			fin = 1;
		}
	}
	public void gagne()
	{
		for(i = 0; i < nbLigneTab; i++)
		{
			for (j= 0; j < nbColTab; j++)
			{
				if(tabInt[i][j] == 2048)
				{
					// Excatement la même chose que pour finJeu() sauf que cette fois on vérifie s'il n'a pas atteint 2048
					JFrame finJeu = new JFrame("Victoire !");
					finJeu.setLayout(new FlowLayout());
					finJeu.setVisible(true);
			        finJeu.setSize(500,50);
			        finJeu.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Dispose_on_close libère les ressources sans fermer tout
			        finJeu.setResizable(false);
			        finJeu.setLocationRelativeTo(null);
			        JLabel fin = new JLabel("Félicitation, vous avez atteint le carré 2048 !");
					finJeu.add(fin);
				}
			}
		}
	}
	public void deplaceDroit()
	{
		if (!deplacePossible)
			verifDeplaceDroit();
			// De base deplacePossible est à false pour vérifier avant tout que l'on puisse effectuer ce déplacement
		if (deplacePossible)
			// Si le déplacement est possible on effectue la méthode normalement
		{
			for(i=0; i < nbLigneTab; i++)
			{
				extremite = 3;
				// Case la plus à droite
				for(j=3; j > -1; j--)
				{
					//On parcourt les cases de droite à gauche
					if(tabInt[i][extremite] == 0)
					{
						//Si l'extrêmité est vide
						if(tabInt[i][j] != 0)
						{
							//Si la case testée actuellement contient un nombre
							// on décale celui ci jusqu'à l'extrêmité
							tabInt[i][extremite] = tabInt[i][j];
							if (j != extremite)
							{
								tabInt[i][j] = 0;
								// j est la colonne de la case que l'on déplace, si elle est 
								// différente de l'extremité il faut la détruire 
							}
							extremite -=1;
							// l'extrêmité n'est plus la case la plus à droite mais celle avant
						}	
					}
					else
						extremite -=1;
					// l'extrêmité la plus à droite est déjà prise, on se décale donc de un
				}
			}
			if(!detruit)
				detruitDroit();
				// Si detruit est à false c'est que l'on a pas encore effectué les "destructions" causées par
				// le déplacement
			deplacePossible =(false);
			// On remet deplacePossible à false pour la prochaine vérification
		}
	}
	public void detruitDroit()
	{
		detruit = (true);
		// Dès le début on passe détruit à true pour ne pas le refaire dans deplace
		// Après avoir placé toutes les tuiles d'un côté, on cherche celles qui sont semblables
		for (i= 0; i < nbLigneTab; i ++)
		{
			for( j = 3; j > 0; j--)
			{
				if (tabInt[i][j-1] == tabInt[i][j])
				{
					//Si celle avant est la même que celle à l'extrêmité, on addittione
					tabInt[i][j] = tabInt[i][j-1] + tabInt[i][j];
					tabInt[i][j-1] = 0;
					score = score + tabInt[i][j];
					//et on détruit celle avant l'extrêmité
					// Enfin on actualise le score
				}
			}
		}
		deplaceDroit();
		// On redéplace tout à droite pour combler les vides
		generAleatoire();
		// On génére un nouveau nombre aléatoire
	}
	public void verifDeplaceDroit()
	{
		deplacePossible = (false);
		// Utilisation d'un booléen permettant de savoir si l'on peut effectuer ou non un déplacement
		for(i= 0; i < nbLigneTab; i++)
		{
			for (j= 0; j < nbColTab-1; j++)
			{
				// On parcourt toutes les cases du tableau
				try
				{
					if (tabInt[i][j] != 0)
					{
						// Il faut que la case soit différente de 0
						if(tabInt[i][j] == tabInt[i][j + 1])
						{
							// Si la case située directement à côté est pareille, le déplacement est possible
							deplacePossible = true;
							return;
						}
						else
						{
							if(tabInt[i][j+1] == 0)
							{
								// Si la case suivant une tuile n'étant pas un 0 est un 0 ou un même nombre
								// Le déplacement est possible
								deplacePossible = (true);
								return;
								// On quitte alors la méthode
							}
							else if(tabInt[i][j+2] == 0)
							{
								// Même principe mais pour 2 cases plus loin
								deplacePossible = (true);
								return;
							}
							else if(tabInt[i][j+3] == 0)
							{
								// Même principe mais pour 3 cases plus loin
								deplacePossible = (true);
								return;
							}
						}
					}
				}
				catch(IndexOutOfBoundsException e)
				{} // Enfin on catch une sortie de tableau, un dépassement d'indice qui serait dû au j+1 / j+2 / j+3
			}
		}
	}
	public void deplaceGauche()
	{
		if (!deplacePossible)
			verifDeplaceGauche();
		if (deplacePossible)
		{
			//La méthode est principalement celle détaillée dans deplaceDroit()
			for(i=0; i < nbLigneTab; i++)
			{
				extremite = 0;
				// on déplace à gauche, l'extrêmité est donc désormais 0 (décalage d'indice)
				for(j=0; j < nbColTab; j++)
				{
					if(tabInt[i][extremite] == 0)
					{
						if(tabInt[i][j] != 0)
						{
							tabInt[i][extremite] = tabInt[i][j];
							if (j != extremite)
							{
								tabInt[i][j] = 0;
							}
							extremite +=1;
						}
					}
					else
						extremite +=1;
				}
			}		
			if (!detruit)
				detruitGauche();
			deplacePossible =(false);
		}
	}
		public void detruitGauche()
		{
			detruit = (true);
			for (i= 0; i < nbLigneTab; i ++)
			{
				for( j = 0; j < nbColTab - 2; j++)
				{
					if (tabInt[i][j+1] == tabInt[i][j])
					{
						tabInt[i][j] = tabInt[i][j+1] + tabInt[i][j];
						tabInt[i][j+1] = 0;
						score = score + (tabInt[i][j+1] + tabInt[i][j]);
					}
				}
			}
			deplaceGauche();
			generAleatoire();
		}
		public void verifDeplaceGauche()
		{
			//cf deplace droit pour comprendre le procédé
			deplacePossible = (false);
			for(i = 0; i < nbLigneTab; i++)
			{
				for(j= 3 ; j > 0 ; j--)
				{
					try
					{
						if (tabInt[i][j] != 0)
						{
							if(tabInt[i][j] == tabInt[i][j - 1])
							{
								deplacePossible = true;
								return;
							}
							else
							{
								if(tabInt[i][j-1] == 0)
								{
									deplacePossible = (true);
									return;
								}
								else if(tabInt[i][j-2] == 0)
								{
									deplacePossible = (true);
									return;
								}
								else if(tabInt[i][j-3] == 0)
								{
									deplacePossible = (true);
									return;
								}
							}
						}
					}
					catch(IndexOutOfBoundsException e)
					{}
				}
			}
			
		}
		public void deplaceHaut()
		{
			if (!deplacePossible)
				verifDeplaceHaut();
			if (deplacePossible)
			{
				//La méthode est principalement celle détaillée dans deplaceDroit()		
				for(j=0; j < nbColTab; j++)
				{
					extremite = 0;
					for(i=0; i < nbLigneTab; i++)
					{
						if(tabInt[extremite][j] == 0)
						{
							if(tabInt[i][j] != 0)
							{
								tabInt[extremite][j] = tabInt[i][j];
								if (i != extremite)
								{
									tabInt[i][j] = 0;
								}
								extremite +=1;
							}
						}
						else
							extremite +=1;
					}
				}	
				if (!detruit)
					detruitHaut();
				deplacePossible = (false);
			}
		}
		public void detruitHaut()
		{
			detruit = (true);
			for (j= 0; j < nbColTab; j++)
			{
				for( i = 0; i < nbLigneTab -1; i++)
				{
					if (tabInt[i+1][j] == tabInt[i][j])
					{
						tabInt[i][j] = tabInt[i+1][j] + tabInt[i][j];
						tabInt[i+1][j] = 0;
						score = score + (tabInt[i+1][j] + tabInt[i][j]);
					}
				}
			}
			deplaceHaut();
			generAleatoire();
		}
		public void verifDeplaceHaut()
		{
			//cf deplace droit pour comprendre le procédé		
			deplacePossible = (false);
			for(j = 0; j < nbColTab; j++)
			{
				for(i= 3 ; i > 0 ; i--)
				{
					try
					{
						if (tabInt[i][j] != 0)
						{
							if(tabInt[i][j] == tabInt[i-1][j])
							{
								deplacePossible = true;
								return;
							}
							else
							{
								if(tabInt[i-1][j] == 0)
								{
									deplacePossible = (true);
									return;
								}
								else if(tabInt[i-2][j] == 0)
								{
									deplacePossible = (true);
									return;
								}
								else if(tabInt[i-3][j] == 0)
								{
									deplacePossible = (true);
									return;
								}
							}
						}
					}
					catch(IndexOutOfBoundsException e)
					{}
				}
			}
		}
		public void deplaceBas()
		{
			if (!deplacePossible)
				verifDeplaceBas();
			if (deplacePossible)
			{
				//La méthode est principalement celle détaillée dans deplaceDroit()
				for(j=0; j < nbColTab; j++)
				{
					extremite = 3;
					for(i=3; i > -1 ; i--)
					{
						if(tabInt[extremite][j] == 0)
						{
							if(tabInt[i][j] != 0)
							{
								tabInt[extremite][j] = tabInt[i][j];
								if (i != extremite)
								{
									tabInt[i][j] = 0;
								}
								extremite -=1;
							}
						}
						else
							extremite -=1;
					}
				}	
				if (!detruit)
					detruitBas();
				deplacePossible = (false);
			}
		}
		public void detruitBas()
		{
			detruit = (true);
			for (j= 0; j < nbColTab; j++)
			{
				for( i = 3; i > 0; i--)
				{
					if (tabInt[i-1][j] == tabInt[i][j])
					{
						tabInt[i][j] = tabInt[i-1][j] + tabInt[i][j];
						tabInt[i-1][j] = 0;
						score = score + (tabInt[i-1][j] + tabInt[i][j]);
					}
				}
			}
			deplaceBas();
			generAleatoire();
		}
		public void verifDeplaceBas()
		{
			//cf deplace droit pour comprendre le procédé		
			deplacePossible = (false);
			for(j = 0; j < nbColTab; j++)
			{
				for(i= 0 ; i < nbLigneTab - 1 ; i++)
				{
					try
					{
						if (tabInt[i][j] != 0)
						{
							if(tabInt[i][j] == tabInt[i+1][j])
							{
								deplacePossible = true;
								return;
							}
							else
							{
								if(tabInt[i+1][j] == 0)
								{
									deplacePossible = (true);
									return;
								}
								else if(tabInt[i+2][j] == 0)
								{
									deplacePossible = (true);
									return;
								}
								else if(tabInt[i+3][j] == 0)
								{
									deplacePossible = (true);
									return;
								}
							}
						}
					}
					catch(IndexOutOfBoundsException e)
					{}
				}
			}	
		}
		public void actionPerformed(ActionEvent arg0) {}
}
class ClavierListener implements KeyListener {

	Plateau p;
	ClavierListener(Plateau p)
	// On transmet l'objet p en paramètre
	{
		this.p = p;
	}
    public void keyPressed(KeyEvent e) {
		p.toInt();
		// A chaque début de boucle, on crée l'image du tableau de JLabel dans tabInt qui est un tableau d'entier
		switch(e.getKeyCode())
		{
		// En fonction du code clavier retourné lorsque l'on clique sur une touche, on effectue une des méthodes
		case 40 : p.deplaceBas();
      	break;      
		case 37 : p.deplaceGauche();
		break;
		case 38 : p.deplaceHaut();
      	break;
		case 39 : p.deplaceDroit();
      	break;
		}
		p.couleurTuile();
		// On colore chaque tuile
		p.toJLabel();
		// On transpose tabInt (le tableau d'entier) dans notre plateau (le tableau de JLabel)
		p.finJeu();
		p.gagne();
		// Après chaque déplacement on vérifie que le joueur a gagné ou perdu
    }
    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}    
}