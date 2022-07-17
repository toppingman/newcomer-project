package bbs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class BbsDAO {

	private Connection conn; // DB�� �����ϴ� ��ü

	private ResultSet rs; // DB data�� ���� �� �ִ� ��ü  (Ctrl + shift + 'o') -> auto import

	// �⺻ ������
	public BbsDAO() {
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
	
	//�ۼ����� �޼ҵ�
	public String getDate() { // ����ð�
		  	String SQL = "SELECT NOW()";  // ����ð��� ��Ÿ���� mysql
		  	try {
		  		PreparedStatement pstmt = conn.prepareStatement(SQL);
		  		rs = pstmt.executeQuery();
		  		if (rs.next()) {
		  			return rs.getString(1);
		  		}
		  	} catch(Exception e) {
		  		e.printStackTrace();
		  	}
		  	return "";
		  }

		// �Խñ� ��ȣ �ο� �޼ҵ�
		public int getNext() { 
		  	String SQL = "SELECT board_ID FROM board ORDER BY board_ID DESC";  // ������������ ���� �������� ���� ���� �����´�
		  	try {
		  		PreparedStatement pstmt = conn.prepareStatement(SQL);
		  		rs = pstmt.executeQuery();
		  		if (rs.next()) {
		  			return rs.getInt(1) + 1; // �� ���� �Խñ��� ��ȣ
		  		}
		  		return 1; // ù ��° �Խù��� ���
		  	} catch(Exception e) {
		  		e.printStackTrace();
		  	}
		  	return -1; 
		  }
	   
		//�۾��� �޼ҵ�
		public int write(String board_Title, String userID, String board_Contents) {
		  	String SQL = "INSERT INTO board VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		  	try {
		  		PreparedStatement pstmt = conn.prepareStatement(SQL);
		  		pstmt.setInt(1, getNext());
		  		pstmt.setString(2, board_Title);
		  		pstmt.setString(3, board_Contents);
		  		pstmt.setString(4, userID);
		  		pstmt.setString(5, getDate());
		  		pstmt.setString(6, userID);
		  		pstmt.setString(7, getDate());
		  		pstmt.setInt(8, 1);
		  		return pstmt.executeUpdate();  
		  		
		  	} catch(Exception e) {
		  		e.printStackTrace();
		  	}
		  	return -1; 
		  }
		  
		//�Խñ� ����Ʈ �޼ҵ�
		public ArrayList<Bbs> getList(int pageNumber) {
			  
				String SQL = "SELECT * FROM board WHERE board_ID < ? AND bbsAvailable = 1 ORDER BY board_ID DESC LIMIT 10";
				ArrayList<Bbs> list = new ArrayList<Bbs>();
				try {
					PreparedStatement pstmt = conn.prepareStatement(SQL);
					pstmt.setInt(1, getNext() - (pageNumber -1) * 10);
					rs = pstmt.executeQuery();
					while (rs.next()) {
						Bbs bbs = new Bbs();
						bbs.setBoard_ID(rs.getInt(1));
						bbs.setBoard_Title(rs.getString(2));
						bbs.setBoard_Contents(rs.getString(3));
						bbs.setUserID(rs.getString(4));
						bbs.setBoard_createdDate(rs.getString(5));
						bbs.setBoard_modifieID(rs.getString(6));
						bbs.setBoard_modifiedDate(rs.getString(7));
						bbs.setBbsAvailable(rs.getInt(8));
						list.add(bbs);
					}			
				} catch(Exception e) {
					e.printStackTrace();
				}
				return list;
			}

		  	// ����¡ ó�� �޼ҵ�
			public boolean nextPage(int pageNumber) {
				String SQL = "SELECT * FROM board WHERE board_ID < ? AND bbsAvailable = 1";

				try {
					PreparedStatement pstmt = conn.prepareStatement(SQL);
					pstmt.setInt(1, getNext() - (pageNumber -1) * 10);
					rs = pstmt.executeQuery();
					if (rs.next()) {
						return true;
					}			
				} catch(Exception e) {
					e.printStackTrace();
				}
				return false;
			}
			
			// �ϳ��� �Խñ��� ���� �޼ҵ�
			public Bbs getBbs(int board_ID) {
				String SQL = "SELECT * FROM board WHERE board_ID = ?";
				try {
					PreparedStatement pstmt = conn.prepareStatement(SQL);
					pstmt.setInt(1, board_ID);
					rs = pstmt.executeQuery();
					if (rs.next()) {
						Bbs bbs = new Bbs();
						bbs.setBoard_ID(rs.getInt(1));
						bbs.setBoard_Title(rs.getString(2));
						bbs.setBoard_Contents(rs.getString(3));
						bbs.setUserID(rs.getString(4));
						bbs.setBoard_createdDate(rs.getString(5));
						bbs.setBoard_modifieID(rs.getString(6));
						bbs.setBoard_modifiedDate(rs.getString(7));
						bbs.setBbsAvailable(rs.getInt(8));
						return bbs;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
			
		// �Խñ� ���� �޼ҵ�
		public int update(int board_ID, String board_Title, String board_Contents, String userID)
		{
		          String SQL = "UPDATE board SET board_Title = ?, board_Contents = ?, board_modifieID = ?, board_modifiedDate = now()  WHERE board_ID = ? ";
	
		          try {
		              System.out.println(userID);
		              PreparedStatement pstmt = conn.prepareStatement(SQL);
		              pstmt.setString(1, board_Title);
		              pstmt.setString(2, board_Contents);
		              pstmt.setString(3, userID);
		              pstmt.setInt(4, board_ID);
		              return pstmt.executeUpdate();
		        	 
	
		          } catch (Exception e) {
		              e.printStackTrace();
		          }
		          return -1; 
	
		  }
		
		// �Խñ� ���� �޼ҵ�
		public int delete(int board_ID)
	    {
	        String SQL = "UPDATE board SET bbsAvailable = 0  WHERE board_ID = ?";
	        try {
	            PreparedStatement pstmt = conn.prepareStatement(SQL);
	            pstmt.setInt(1, board_ID);
	            return pstmt.executeUpdate();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return -1; 
	    }
	}

