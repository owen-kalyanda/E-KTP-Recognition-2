import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Database {

	static String latKarakter;
	static double latStdHori;
	static double latMeanHori;
	static double latStdVert;
	static double latMeanVert;
	static double latStdDiag;
	static double latMeanDiag;
	static ArrayList<String> stringLatih = new ArrayList<>(); 
	static ArrayList<Integer> idString = new ArrayList<>(); 
	
	public static Connection getConnection() throws ClassNotFoundException, SQLException {
		String driver = "org.gjt.mm.mysql.Driver";
		String url = "jdbc:mysql://localhost/ktprecog";
		String username = "root";
		String password = "";
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, username, password);
		return conn;
	}
	
	public static void insert(Connection conn, String karakter, double horizontalSD, double horizontalMean, double vertikalSD, double vertikalMean, double diagonalSD, double diagonalMean) throws SQLException{
		String sql = "INSERT INTO tb_globalhistogram (karakter, horizontalSD, horizontalMean, vertikalSD, vertikalMean, diagonalSD, diagonalMean) values (?,?,?,?,?,?,?)";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, karakter);
		stmt.setDouble(2, horizontalSD);
		stmt.setDouble(3, horizontalMean);
		stmt.setDouble(4, vertikalSD);
		stmt.setDouble(5, vertikalMean);
		stmt.setDouble(6, diagonalSD);
		stmt.setDouble(7, diagonalMean);
		stmt.executeUpdate();
	}
	
	public static int selectCount(Connection conn) throws SQLException{
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT COUNT(*) from tb_globalhistogram");
		while (rs.next()) {
			return rs.getInt(1);
		}
		rs.close();
		conn.close();
		return 0;
	}
	
	public static ArrayList<Integer> selectId(Connection conn) throws SQLException{
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT id_karakter FROM tb_globalhistogram");
		ArrayList<Integer> idKarakter = new ArrayList<>(); 
		while (rs.next()) {
			idKarakter.add(rs.getInt(1));
		}
		rs.close();
		return idKarakter;
	}
	
	public static void selectAll(Connection conn, int id) throws SQLException{
		String sql = "SELECT * FROM tb_globalhistogram WHERE id_karakter = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, id);
		ResultSet rs = stmt.executeQuery();
		if(rs.next()){
			latKarakter = rs.getString(1);
			latStdHori = rs.getDouble(2);
			latMeanHori= rs.getDouble(3);
			latStdVert = rs.getDouble(4);
			latMeanVert= rs.getDouble(5);
			latStdDiag = rs.getDouble(6);
			latMeanDiag = rs.getDouble(7);
		}
		rs.close();
		conn.close();
	}
	
	public static double selectHoriSD(Connection conn, int id) throws SQLException{
		String sql = "SELECT horizontalSD FROM tb_globalhistogram WHERE id_karakter = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, id);
		ResultSet rs = stmt.executeQuery();
		double latHoriSD = 0;
		if(rs.next()) {
			latHoriSD = rs.getDouble(1);
		}
		rs.close();
		conn.close();
		return latHoriSD;
	}
	
	public static double selectHoriM(Connection conn, int id) throws SQLException{
		String sql = "SELECT horizontalMean FROM tb_globalhistogram WHERE id_karakter = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, id);
		ResultSet rs = stmt.executeQuery();
		double latHoriM = 0;
		if(rs.next()) {
			latHoriM = rs.getDouble(1);
		}
		rs.close();
		conn.close();
		return latHoriM;
	}

	public static double selectVertiSD(Connection conn, int id) throws SQLException{
		String sql = "SELECT vertikalSD FROM tb_globalhistogram WHERE id_karakter = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, id);
		ResultSet rs = stmt.executeQuery();
		double latVertiSD = 0;
		if(rs.next()) {
			latVertiSD = rs.getDouble(1);
		}
		rs.close();
		conn.close();
		return latVertiSD;
	}
	
	public static double selectVertiM(Connection conn, int id) throws SQLException{
		String sql = "SELECT vertikalMean FROM tb_globalhistogram WHERE id_karakter = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, id);
		ResultSet rs = stmt.executeQuery();
		double latVertiM = 0;
		if(rs.next()) {
			latVertiM = rs.getDouble(1);
		}
		rs.close();
		conn.close();
		return latVertiM;
	}
	
	public static double selectDiagSD(Connection conn, int id) throws SQLException{
		String sql = "SELECT diagonalSD FROM tb_globalhistogram WHERE id_karakter = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, id);
		ResultSet rs = stmt.executeQuery();
		double latDiagSD = 0;
		if(rs.next()) {
			latDiagSD = rs.getDouble(1);
		}
		rs.close();
		conn.close();
		return latDiagSD;
	}
	
	public static double selectDiagM(Connection conn, int id) throws SQLException{
		String sql = "SELECT diagonalMean FROM tb_globalhistogram WHERE id_karakter = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, id);
		ResultSet rs = stmt.executeQuery();
		double latDiagM = 0;
		if(rs.next()) {
			latDiagM = rs.getDouble(1);
		}
		rs.close();
		conn.close();
		return latDiagM;
	}
	
	public static void selectIdLeven(Connection conn) throws SQLException{
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT id FROM tb_levenshtein");
		while (rs.next()) {
			idString.add(rs.getInt(1));
		}
		rs.close();
	}
	
	public static void selectString(Connection conn) throws SQLException{
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT stringLatih FROM tb_levenshtein");		
		while (rs.next()) {
			stringLatih.add(rs.getString(1));
		}
		rs.close();
	}
}
