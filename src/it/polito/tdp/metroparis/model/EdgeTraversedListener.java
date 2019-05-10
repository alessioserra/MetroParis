package it.polito.tdp.metroparis.model;

import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.event.ConnectedComponentTraversalEvent;
import org.jgrapht.event.EdgeTraversalEvent;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.event.VertexTraversalEvent;
import org.jgrapht.graph.DefaultEdge;

//Classe per intercettare gli eventi del grafo quando è scandito da un iteratore (LISTENER)
public class EdgeTraversedListener implements TraversalListener<Fermata, DefaultEdge> {

	Graph<Fermata, DefaultEdge> grafo;
	Map<Fermata,Fermata> back;
	
	public EdgeTraversedListener(Map<Fermata, Fermata> back,Graph<Fermata, DefaultEdge> grafo) {
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

	@Override
	public void edgeTraversed(EdgeTraversalEvent<DefaultEdge> ev) {	
 		 
		/*Back codifica le relazioni si tipo CHILD -> PARENT
		 * 
		 * Per un nuovo vertice 'CHILD' scoperto devo avere che:
		 * 
		 * -CHILD è ancora sconosciuto (non ancora scoperto)
		 * -PARENT è gia stato visitato
		 */
		
		//Estraggo gli estremi dell'arco
		Fermata sourceVertex = grafo.getEdgeSource(ev.getEdge());
		Fermata targetVertex = grafo.getEdgeTarget(ev.getEdge());
		
		/*
		 * Se il grafo e' orientato, allora SOURCE==PARENT , TARGET==CHILD
		 * Se il grafo NON è orientato potrebbe essere il contrario..
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
