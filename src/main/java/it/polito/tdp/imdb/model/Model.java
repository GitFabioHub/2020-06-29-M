package it.polito.tdp.imdb.model;

import java.util.HashMap;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	
	private ImdbDAO dao;
	private Graph<Director,DefaultWeightedEdge> grafo;
	private Map<Integer,Director>idMap;
	
	public Model() {
		this.dao=new ImdbDAO();
	}
	
	public void creaGrafo(int anno) {
		
		this.idMap=new HashMap<Integer,Director>();
		grafo=new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		this.dao.getDirettori(anno,idMap);
		Graphs.addAllVertices(grafo, idMap.values());
		for(Adiacenza a:dao.getAdiacenze(idMap)) {
			if(!grafo.containsEdge(grafo.getEdge(a.getD1(), a.getD2()))) {
				
				Graphs.addEdge(grafo, a.getD1(), a.getD2(), a.getPeso());
				
			}
			
		}
	}
		
	public int getVertici() {
		
			return this.grafo.vertexSet().size();
		}
	public int getArchi() {
		
		return this.grafo.edgeSet().size();
	}
		
	public Graph<Director,DefaultWeightedEdge> getGrafo(){
		return this.grafo;
	}

}
