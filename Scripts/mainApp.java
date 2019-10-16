/*
 * MainApp est une classe qui va servir d'interface primaire avec l'utilisateur en récupérant l'adresse du fichier gff3 à traiter et exécuter le parseur puis l'interface graphique dessus.
 * </p>
 * @author YassJeanRaph
*/

import javax.swing.SwingUtilities;
import java.util.Scanner;
import java.lang.String;
import java.io.File;


public class mainApp
{
	//@param 	Prend en argument 
	//Main du programme servant à demander l'adresse du fichier gff3 et excécuter le parseur puis l'interface graphique
	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				Scanner sc = new Scanner(System.in);
				System.out.println("");
				System.out.println("Bienvenue sur Genome Explorer");
				System.out.println("-------------------------------------------------");
			    System.out.println("Choisir le fichier GFF3 d'input:");
			    
			    String input = sc.nextLine();
			    boolean erreur = false;
					File f = null;
				    do
				    {
						f = new File(input);
				       	if ((input.substring(input.lastIndexOf(".") + 1).equals("gff3")) && (f.canRead()))//test si le fichier est bien au format gff3 et la possibilité de lecture du fichier
				       	{
				          	erreur = false;
				       	}//fin du if
				       	else//en cas d'erreur: un message d'erreur s'affiche et le programme demande une nouvelle fois l'adresse du fichier gff3
				       	{
				        	erreur = true;
							System.out.println("");
				         	System.out.println("Le chemin du fichier est incorrect ou le fichier est illisible, vérifier que le format du fichier est bien .gff3.");
							System.out.println("-------------------------------------------------");
							System.out.println("Choisir le fichier GFF3 d'input:");
							input = "";
				         	input = sc.nextLine();
				        }//fin du else
				    } while(erreur);
				    sc.close();
				    //instanciation du parseur et du génome pour extraire et stocker les informations du fichier gff3
					GFF_file current_gff = new GFF_file();
					Genome current_genome = new Genome();
					
					current_genome = current_gff.GFFparseur(input);
					FenetreGenomique fenetre = new FenetreGenomique(current_genome);
					fenetre.setVisible(true);
			}
		});
	}
}
