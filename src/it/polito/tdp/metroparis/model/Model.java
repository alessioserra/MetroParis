package it.polito.tdp.metroparis.model;

import java.util.*;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import it.polito.tdp.metroparis.db.MetroDAO;

public class Model {
	
	//Dichiaro solamente variabili, le CREO(new) dopo
	private Graph<Fermata,DefaultEdge> grafo;
	private List<Fermata> fermate;
	private Map<Integer,Fermata> fermateIdMap;
	
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
		this.fermate=dao.getAllFermate();
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

}