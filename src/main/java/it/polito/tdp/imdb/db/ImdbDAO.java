package it.polito.tdp.imdb.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Adiacenza;
import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Movie;

public class ImdbDAO {
	
	public List<Actor> listAllActors(){
		String sql = "SELECT * FROM actors";
		List<Actor> result = new ArrayList<Actor>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Actor actor = new Actor(res.getInt("id"), res.getString("first_name"), res.getString("last_name"),
						res.getString("gender"));
				
				result.add(actor);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Movie> listAllMovies(){
		String sql = "SELECT * FROM movies";
		List<Movie> result = new ArrayList<Movie>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Movie movie = new Movie(res.getInt("id"), res.getString("name"), 
						res.getInt("year"), res.getDouble("rank"));
				
				result.add(movie);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<Director> listAllDirectors(){
		String sql = "SELECT * FROM directors";
		List<Director> result = new ArrayList<Director>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Director director = new Director(res.getInt("id"), res.getString("first_name"), res.getString("last_name"));
				
				result.add(director);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void getDirettori(int anno, Map<Integer, Director> idMap) {
		
	
		String sql = "SELECT DISTINCT  d.id id,d.first_name fname,d.last_name lname " + 
				"FROM directors d,movies_directors md,movies m " + 
				"WHERE d.id=md.director_id AND md.movie_id=m.id AND m.year= ? ";
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Director director = new Director(res.getInt("id"), res.getString("fname"), res.getString("lname"));
				if(!idMap.containsKey(res.getInt("id")))
				    idMap.put(res.getInt("id"), director);
			}
			conn.close();
			return ;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return ;
		}
		
	}

	public Collection<Adiacenza> getAdiacenze(Map<Integer, Director> idMap) {

	
		String sql = "SELECT d1.director_id id1,d2.director_id id2 ,COUNT(DISTINCT(r1.actor_id)) as peso " + 
				"FROM roles r1,roles r2,movies m1,movies m2,movies_directors d1,movies_directors d2 " + 
				"WHERE r1.actor_id= r2.actor_id AND r1.movie_id>= r2.movie_id AND r1.movie_id=m1.id AND r2.movie_id=m2.id AND d1.movie_id=m1.id AND  d2.movie_id=m2.id AND d1.director_id> d2.director_id " + 
				"GROUP BY d1.director_id,d2.director_id " + 
				"";
		List<Adiacenza> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				if(idMap.containsKey(res.getInt("id1"))&& idMap.containsKey(res.getInt("id2"))) {
				
				
				Adiacenza adiacenza = new Adiacenza(idMap.get(res.getInt("id1")),idMap.get(res.getInt("id2")),res.getDouble("peso"));
				result.add(adiacenza);
				}
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	
	
	}
	
	
	
	
	
	
}
