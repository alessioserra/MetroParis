package it.polito.tdp.metroparis.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.javadocmd.simplelatlng.LatLng;

import it.polito.tdp.metroparis.model.Fermata;
import it.polito.tdp.metroparis.model.Linea;

public class MetroDAO {

	public List<Fermata> getAllFermate() {

		final String sql = "SELECT id_fermata, nome, coordx, coordy FROM fermata ORDER BY nome ASC";
		List<Fermata> fermate = new ArrayList<Fermata>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Fermata f = new Fermata(rs.getInt("id_Fermata"), rs.getString("nome"),
						new LatLng(rs.getDouble("coordx"), rs.getDouble("coordy")));
				fermate.add(f);
			}

			st.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore di connessione al Database.");
		}

		return fermate;
	}

	public List<Linea> getAllLinee() {
		final String sql = "SELECT id_linea, nome, velocita, intervallo FROM linea ORDER BY nome ASC";

		List<Linea> linee = new ArrayList<Linea>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Linea f = new Linea(rs.getInt("id_linea"), rs.getString("nome"), rs.getDouble("velocita"),
						rs.getDouble("intervallo"));
				linee.add(f);
			}

			st.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore di connessione al Database.");
		}

		return linee;
	}
	
	/**
	 * Metodo per verificare l'esistenza della connessione
	 * @param partenza
	 * @param arrivo
	 * @return true se esiste la connessione, false altrimenti
	 */
	public boolean esisteConnessione(Fermata partenza,Fermata arrivo) {
		
		final String sql = "SELECT COUNT(*) AS cnt FROM connessione WHERE id_stazP=? AND id_stazA=?";
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			//Setto i parametri della query
			st.setInt(1, partenza.getIdFermata());
			st.setInt(2, arrivo.getIdFermata());
			
			//Eseguo query
			ResultSet rs = st.executeQuery();

			//Mi posiziono sulla prima e unica riga
			rs.next();
			
			int numero = rs.getInt("cnt");

			conn.close();
			
			return (numero>0);

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore",e);
		}

	}

	public List<Fermata> stazioniArrivo(Fermata partenza, Map<Integer,Fermata> idMap) {

		final String sql ="SELECT id_stazA FROM connessione WHERE id_stazP=?";
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			//Setto i parametri della query
			st.setInt(1, partenza.getIdFermata());
			
			//Eseguo query
			ResultSet rs = st.executeQuery();

			//Creo la lista da riempire e poi restituire
			List<Fermata> result = new ArrayList<Fermata>();
			
			while(rs.next()) { //sfrutto l'hashcode creando un oggetto effimero
				result.add(idMap.get( rs.getInt("id_stazA")));	
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore",e);
		}
	}
	
}
