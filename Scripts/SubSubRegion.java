/*
 * Subrégion est la classe héritant de Abstract_region qui définit une sous sous région génomique
 * Cette classe est caractérisée par les informations suivantes :
 * <ul>
 * <li>_phase : Indique la pb (0, 1 ou 2) à partir de laquelle commence réellement le premier codon
 * </ul>
 * Cette classe va permettre d'organiser (via le constructeur) et de manipuler (via les méthodes) les données relatives aux subsubrégions génomiques du fichier .gff3 .
 * </p>
 * @author YassJeanRaph
*/


public class SubSubRegion extends Abstract_region
{
	private char _phase;

	//Constructeur de la classe SubSubregion
	public SubSubRegion(String type, int start, int end, String eval, char strand, char phase)
	{
		super(type, start, end, eval, strand);
		this._phase = phase;
	}


//-----------------Méthodes----------------------
	//Méthodes retournant des attributs

	//@return	L'attribut _phase d'une subsubrégion
	public char getPhase()
	{
		return this._phase;
	}

	//Méthodes modifiant des attributs


	//@param 	Un caractère
	// Cette méthode permet de créer/modifier l'attribut _phase manuellement
	public void setPhase(char new_phase)
	{
		this._phase = new_phase;
	}
}
