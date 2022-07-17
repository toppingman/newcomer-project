package bbs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class BbsDAO {

	private Connection conn; // DB에 접근하는 객체

	private ResultSet rs; // DB data를 담을 수 있는 객체  (Ctrl + shift + 'o') -> auto import

	// 기본 생성자
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
	
	//작성일자 메소드
	public String getDate() { // 현재시간
		  	String SQL = "SELECT NOW()";  // 현재시간을 나타내는 mysql
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

		// 게시글 번호 부여 메소드
		public int getNext() { 
		  	String SQL = "SELECT board_ID FROM board ORDER BY board_ID DESC";  // 내림차순으로 가장 마지막에 쓰인 것을 가져온다
		  	try {
		  		PreparedStatement pstmt = conn.prepareStatement(SQL);
		  		rs = pstmt.executeQuery();
		  		if (rs.next()) {
		  			return rs.getInt(1) + 1; // 그 다음 게시글의 번호
		  		}
		  		return 1; // 첫 번째 게시물인 경우
		  	} catch(Exception e) {
		  		e.printStackTrace();
		  	}
		  	return -1; 
		  }
	   
		//글쓰기 메소드
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
		  
		//게시글 리스트 메소드
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

		  	// 페이징 처리 메소드
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
			
			// 하나의 게시글을 보는 메소드
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
			
		// 게시글 수정 메소드
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
		
		// 게시글 삭제 메소드
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

