package it.polito.tdp.metroparis.model;

import java.util.*;

import org.jgrapht.*;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

import it.polito.tdp.metroparis.db.MetroDAO;

public class Model {
	
	//Dichiaro solamente variabili, le CREO(new) dopo
	private Graph<Fermata,DefaultEdge> grafo;
	private List<Fermata> fermate;
	private Map<Integer,Fermata> fermateIdMap;
	Map<Fermata,Fermata> backVisit;
	
	//GETTERS and SETTERS
	public Graph<Fermata, DefaultEdge> getGrafo() {
		return grafo;
	}
	public void setGrafo(Graph<Fermata, DefaultEdge> grafo) {
		this.grafo = grafo;
	}
	public List<Fermata> getFermate() {
		return fermate;
	}
	public void setFermate(List<Fermata> fermate) {
		this.fermate = fermate;
	}
	/**
	 * Metodo per creare il Grafo
	 */
	public void creaGrafo() {
		
		//Creo l'oggetto GRAFO
		this.grafo = new SimpleDirectedGraph<>(DefaultEdge.class);
		
		//Devo aggiungere i VERTICI
		MetroDAO dao = new MetroDAO(); //interrodo DB per ottenere tutte le fermate
		this.fermate= dao.getAllFermate();
		Graphs.addAllVertices(this.grafo, this.fermate);
		
		//Creo IdMap
		this.fermateIdMap = new HashMap<>();
		for (Fermata f : fermate)
			fermateIdMap.put(f.getIdFermata(), f);
		
		//Devo aggiungere gli ARCHI, ho diverse opzioni:
		
		/*1° opzione: doppio ciclo for,
		for( Fermata partenza : this.grafo.vertexSet()) {
			for (Fermata arrivo : this.grafo.vertexSet()) {
				
				if(dao.esisteConnessione(partenza,arrivo)) {
					this.grafo.addEdge(partenza, arrivo);
				}
			}
		}
		*/
		
		//2°opzione
		for ( Fermata partenza : this.grafo.vertexSet()) {
			List<Fermata> arrivi = dao.stazioniArrivo(partenza, fermateIdMap);
			
			for(Fermata arrivo : arrivi)
				this.grafo.addEdge(partenza, arrivo); 
		}
		
		//3° opzione
		
		
		System.out.println("Grafo creato\n#Vertici: "+grafo.vertexSet().size()+"\n#Archi: "+grafo.edgeSet().size());
	}

	//Iterazione con la quale attraversiamo il grafo e ci aggiungiamo via via i vertici che troviamo (N.B. NON E' UN CAMMINO!)
	public List<Fermata> fermateRaggiungibili(Fermata source){
		
		List<Fermata> risultato = new ArrayList<Fermata>();
		backVisit = new HashMap<>();
		
		//Creo iteratore e lo associo al grafo       
		//GraphIterator<Fermata, DefaultEdge> it = new BreadthFirstIterator<>(this.grafo,source); //in ampiezza
		GraphIterator<Fermata, DefaultEdge> it = new DepthFirstIterator<>(this.grafo,source); //in profondita'
		
		it.addTraversalListener(new EdgeTraversedListener(backVisit, grafo));
		//A fine iterazione mi ritroverò la mappa back riempita
		
		//Devo popolare la mappa almeno col nodo sorgente
		backVisit.put(source, null);
		
		while(it.hasNext()) {
			risultato.add(it.next());
		}
		
		return risultato;
	}
	
	public List<Fermata> percorsoFinoA(Fermata target){
		
		if(!backVisit.containsKey(target)) {
			return null;
		}
		
		List<Fermata> result = new LinkedList<Fermata>();
		
		//Aggiungo la "fine" della lista come prima cosa
		Fermata f = target;
		
		while(f != null) {
		result.add(0, f); //Aggiungo sempre in prima posizione
		f = backVisit.get(f);
		}
		//In questo modo si ripercorre indietro l'albero costruendo la lista

		return result;
	}
	


}