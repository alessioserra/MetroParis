package it.polito.tdp.metroparis.model;

import java.util.List;

public class TestModel {

	public static void main(String[] args) {

		Model model = new Model();
		
		model.creaGrafo();
		
		System.out.println(model.getGrafo());
		
		Fermata source = model.getFermate().get(0);
		List<Fermata> raggiungibili = model.fermateRaggiungibili(source);
        
		//Output
		System.out.println("Parto da: "+source);
		System.out.println("Fermate raggiunte: "+raggiungibili.size());
	
		Fermata target = model.getFermate().get(150);
		List<Fermata> percorso = model.percorsoFinoA(target);
		System.out.println(percorso);
	
	}

}
