//������ ���̽� ������ �ҷ��ö� ��� 

package user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {

	private Connection conn; // DB�� �����ϴ� ��ü
	private PreparedStatement pstmt;
	private ResultSet rs;     // DB data�� ���� �� �ִ� ��ü 

	public UserDAO() {
		try {
			String dbURL = "jdbc:mysql://localhost:3306/BBS?serverTimezone=UTC";
			String dbID = "root";
			String dbPassword = "1234";
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int login(String userID, String userPassword) {
		String SQL = "SELECT userPassword FROM USER WHERE userID = ?";
		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1,  userID);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				if (rs.getString(1).equals(userPassword)) {
					return 1; // �α��� ����
				} else {
					return 0; // ��й�ȣ ����ġ
				}
			}
			return -1; // ���̵� ����
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -2; // �����ͺ��̽� ����
	}
	
	public int join(User user) {
		String SQL = "INSERT INTO USER VALUES (?, ?, ?, ?, ?, now(), now())";
		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, user.getUserID());
			pstmt.setString(2, user.getUserPassword());
			pstmt.setString(3, user.getUserName());
			pstmt.setString(4, user.getUserGender());
			pstmt.setString(5, user.getUserEmail());
			

			return pstmt.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
		
	public int modifi(String userID, String userPassword, String userName, String userGender, String userEmail ) {
		String SQL = "UPDATE user SET userID = ?, userPassword = ?, userName = ?, userGender = ?, userEmail = ? ,modifiedDate = now() WHERE board_ID = ? ";

			try {
	              System.out.println(userID);
	              PreparedStatement pstmt = conn.prepareStatement(SQL);
	              pstmt.setString(1, userID);
	              pstmt.setString(2, userPassword);
	              pstmt.setString(3, userName);
	              pstmt.setString(4, userGender);
	              pstmt.setString(5, userEmail);
	              pstmt.setString(6, userID);
	              
	              return pstmt.executeUpdate();
	        	 

	          } catch (Exception e) {
	              e.printStackTrace();
	          }
	          return -1; 
	}
}