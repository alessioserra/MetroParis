package it.polito.tdp.metroparis.model;

import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.event.ConnectedComponentTraversalEvent;
import org.jgrapht.event.EdgeTraversalEvent;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.event.VertexTraversalEvent;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;

//Classe per intercettare gli eventi del grafo quando � scandito da un iteratore (LISTENER)
public class EdgeTraversedListener implements TraversalListener<Fermata, DefaultWeightedEdge> {

	Graph<Fermata, DefaultWeightedEdge> grafo;
	Map<Fermata,Fermata> back;
	
	public EdgeTraversedListener(Map<Fermata, Fermata> back,Graph<Fermata, DefaultWeightedEdge> grafo) {
		super();
		this.back = back;
		this.grafo=grafo;
	}

	@Override
	public void connectedComponentFinished(ConnectedComponentTraversalEvent arg0) {		
	}

	@Override
	public void connectedComponentStarted(ConnectedComponentTraversalEvent arg0) {		
	}

	public void edgeTraversed(EdgeTraversalEvent<DefaultWeightedEdge> ev) {	
 		 
		/*Back codifica le relazioni si tipo CHILD -> PARENT
		 * 
		 * Per un nuovo vertice 'CHILD' scoperto devo avere che:
		 * 
		 * -CHILD � ancora sconosciuto (non ancora scoperto)
		 * -PARENT � gia stato visitato
		 */
		
		//Estraggo gli estremi dell'arco
		Fermata sourceVertex = grafo.getEdgeSource(ev.getEdge());
		Fermata targetVertex = grafo.getEdgeTarget(ev.getEdge());
		
		/*
		 * Se il grafo e' orientato, allora SOURCE==PARENT , TARGET==CHILD
		 * Se il grafo NON � orientato potrebbe essere il contrario..
		 */
		
		//Codice da riutilizzare
		if( !back.containsKey(targetVertex) && back.containsKey(sourceVertex)) {
			back.put(targetVertex, sourceVertex);
		} else if(!back.containsKey(sourceVertex) && back.containsKey(targetVertex)) {
			back.put(sourceVertex, targetVertex);
		}
	}

	@Override
	public void vertexFinished(VertexTraversalEvent<Fermata> arg0) {		
	}

	@Override
	public void vertexTraversed(VertexTraversalEvent<Fermata> arg0) {		
	}

}
