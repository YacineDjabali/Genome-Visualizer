/*
 * GFF_file est une classe qui va parser un fichier gff3 et construire les objets : génome, région, subrégion, susubrégion.
 * Cette classe va permettre d'extraire toutes les informations d'un fichier .gff3 et d'organiser ces données dans les objets adéquats.
 * </p>
 * @author YassJeanRaph
*/

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.lang.String;
import java.util.Arrays;


public class GFF_file
{
	//@param String fileGFF, prend en paramètre l'adresse d'un fichier au format .gff3.
	//@return Un objet Génome contenant toutes les informations parsées.
	public Genome GFFparseur(String fileGFF)
	{
		//variables servant pour la lecture du fichier gff3
		ArrayList<String[]> listlines = new ArrayList<>();
		String[] line_splited = null;

		//Chaque ligne correspond à un type de région qui sont subdivisées en trois niveau hiérarchiques : region, subregion, subsubregion
		ArrayList<String> region_type = new ArrayList<String>(Arrays.asList("gene", "repeat_region", "mobile_genetic_element", "pseudogene", "origin_of_replication"));
		ArrayList<String> subregion_type = new ArrayList<String>(Arrays.asList("CDS", "antisense_RNA", "ncRNA", "rRNA", "tRNA", "pseudogenic_tRNA", "SRP_RNA", "tmRNA", "RNase_P_RNA"));
		ArrayList<String> subsubregion_type = new ArrayList<String>(Arrays.asList("exon"));
		//l'arrayList 'unknown-type" récupère les types de lignes non-prévues
		ArrayList<String> unknown_type = new ArrayList<String>();
		//NB : dans le fichier test la région de type "sequence_feature" est volontairement ignorée car non-informative

		//Arraylists servant à stocker les subregions et subsubregions non-raccrochées respectivement à une region ou une subregion en vu d'un traitement post-lecture du fichier
		ArrayList<SubRegion> unhooked_subregionList = new ArrayList<SubRegion>();
		ArrayList<SubSubRegion> unhooked_subsubregionList = new ArrayList<SubSubRegion>();

		//Arraylists servant à stocker les subregions et subsubregions non-raccrochées respectivement à une region ou une subregion après le traitement des ArrayLists ci-dessus
		ArrayList<SubRegion> processed_unhooked_subregionList = new ArrayList<SubRegion>();
		ArrayList<SubSubRegion> processed_unhooked_subsubregionList = new ArrayList<SubSubRegion>();

		boolean flag_hooked = false;

		//Objet génome renvoyé contenant toutes les informations du fichier gff3
		Genome output_genome = new Genome();
		try
		{
			BufferedReader buf = new BufferedReader(new FileReader(fileGFF));

			String lineJustFetched = null;
			Genome current_genome = new Genome();

			while(true)
			{
				lineJustFetched = buf.readLine();
				if(lineJustFetched.equals("")) //Si fin du file est atteinte, on sort de la boucle while
				{
					break;
				}
				else
				{ //else1
					//lignes de commentaires dont les informations sont répétées sur la région de type "region" et donc ignorées
					if (lineJustFetched.startsWith("##"))
					{ //if1
						//à l'exception de l'information de l'url à garder car présente seulement dans les lignes commentaires
						if (lineJustFetched.matches("https"))
						{//if2
							current_genome.setUrl(lineJustFetched.split("\t")[1]);
						} //fin if2
						continue;
					} //fin if1
					else
					{ //else2
						line_splited = lineJustFetched.split("\t");

						//Traitement des informations liées à la ligne de type "region" contenant les informations du génome
						if (line_splited[2].equals("region"))
						{ //if1
							current_genome.setID(line_splited[0]);
							current_genome.setLength(Integer.parseInt(line_splited[4]) - Integer.parseInt(line_splited[3]) + 1);
							for (String couple : line_splited[8].split(";"))
							{
								current_genome.addInfo(couple.split("=")[0], couple.split("=")[1]);
							} //fin du for
						}//fin du if1

						//Construction des objets de type region, subregion ou subsubregion

						//Traitement des informations liées à une "region"
						else if(region_type.contains(line_splited[2]))
						{
							current_genome.addRegion(new Region(line_splited[2], Integer.parseInt(line_splited[3]), Integer.parseInt(line_splited[4]), line_splited[5], line_splited[6].charAt(0)));

							for (String couple : line_splited[8].split(";"))
							{
								current_genome.getLastRegionList().addInfo(couple.split("=")[0], couple.split("=")[1]);
							} //fin du for
						} //fin du else if
						//Traitement des informations liées à une "subregion"
						else if (subregion_type.contains(line_splited[2]))
						{
							current_genome.addSubRegion(new SubRegion(line_splited[2], Integer.parseInt(line_splited[3]), Integer.parseInt(line_splited[4]), line_splited[5], line_splited[6].charAt(0), line_splited[7].charAt(0)));

							for (String couple : line_splited[8].split(";"))
							{
								current_genome.getLastSubRegionList().addInfo(couple.split("=")[0], couple.split("=")[1]);
							} //fin du for

							if((current_genome.getLastRegionList().getStart() >= Integer.parseInt(line_splited[3]))&& (current_genome.getLastRegionList().getEnd() <= Integer.parseInt(line_splited[4])))
							{
								current_genome.getLastRegionList().addSubRegion(current_genome.getLastSubRegionList());
							} //fin du if
							else
							{
								unhooked_subregionList.add(current_genome.getLastSubRegionList());
							} //fin else

						} //fin du else if
						//Traitement des informations liées à une "subsubregion"
						else if (subsubregion_type.contains(line_splited[2]))
						{
							current_genome.addSubSubRegion(new SubSubRegion(line_splited[2], Integer.parseInt(line_splited[3]), Integer.parseInt(line_splited[4]), line_splited[5], line_splited[6].charAt(0), line_splited[7].charAt(0)));

							for (String couple : line_splited[8].split(";"))
							{
							current_genome.getLastSubSubRegionList().addInfo(couple.split("=")[0], couple.split("=")[1]);
						} //fin du for

							if((current_genome.getLastSubRegionList().getStart() >= Integer.parseInt(line_splited[3]))&& (current_genome.getLastSubRegionList().getEnd() <= Integer.parseInt(line_splited[4])))
							{
								current_genome.getLastSubRegionList().addSubSubRegion(current_genome.getLastSubSubRegionList());
							} //fin du if
							else
							{
								unhooked_subsubregionList.add(current_genome.getLastSubSubRegionList());
							}// fin du else
						} //fin du else if
						else //si le type n'est présent dans aucune des 3 listes de types prévus, alors il est stocké dans la liste "unknown_type"
						{
							unknown_type.add(line_splited[2]);
						} //fin du else

						listlines.add(line_splited);
					} //fin du else2
				} //fin du else1
			} //fin du while
			buf.close();

			//Traitement des subregion non-raccrochées en parcourant l'ensemble des regions
			if(unhooked_subregionList.size()>0)
			{ //if1
				for(SubRegion unhooked_subregion : unhooked_subregionList)
				{ //for1
					flag_hooked = false;
					for(Region region : current_genome.getRegionList())
					{ //for2
						if((unhooked_subregion.getStart() >= region.getStart())&&(unhooked_subregion.getEnd() <= region.getEnd()))
						{
							region.addSubRegion(unhooked_subregion);
							flag_hooked = true;
						} //fin du if
					} //fin du for2

					if(flag_hooked == false)
					{
						processed_unhooked_subregionList.add(unhooked_subregion);
					}
				} //fin du for1
			} //fin du if1

			//Traitement des subsubregion non-raccrochées en parcourant l'ensemble des subregions
			if(unhooked_subsubregionList.size()>0)
			{ //if1
				for(SubSubRegion unhooked_subsubregion : unhooked_subsubregionList)
				{ //for1
					flag_hooked = false;
					for(SubRegion subregion : current_genome.getSubRegionList())
					{ //for2
						if((unhooked_subsubregion.getStart() >= subregion.getStart())&&(unhooked_subsubregion.getEnd() <= subregion.getEnd()))
						{
							subregion.addSubSubRegion(unhooked_subsubregion);
							flag_hooked = true;
						} //fin du if
					} //fin du for2

					if(flag_hooked == false)
					{
						processed_unhooked_subsubregionList.add(unhooked_subsubregion);
					}
				} //fin du for1
			} //fin if1

			output_genome = current_genome;
		} //fin try

		//Il s'agit d'un gestionnaire d'exception générique, ce qui signifie qu'il peut gérer les exceptions. Il s'exécutera si l'exception n'est pas gérée par les précédents blocs catch.
		catch(Exception e)
		{
			e.printStackTrace(); //Affiche les emplacements où l'exception s'est produite dans le code source, permettant ainsi à l'auteur du programme de voir ce qui ne va pas.
		} //fin du catch
		// @return l'objet Genome
		return(output_genome);
	} //fin GFF parceur

} //fin classe GFF_file
