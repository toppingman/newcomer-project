<!-- 글 삭제 처리 페이지 -->

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="bbs.BbsDAO" %>
<%@ page import="bbs.Bbs" %>
<%@ page import="java.io.PrintWriter" %> <!-- 자바스크립트 문장사용 -->
<% request.setCharacterEncoding("UTF-8"); %> 

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JSP게시판 웹사이트</title>
</head>
<body>

    <%
	
    
        String userID = null;
        // 로그인 된 사람은 회원가입페이지에 들어갈수 없다
        if(session.getAttribute("userID") != null )
        {
            userID = (String) session.getAttribute("userID");
        }
        if(userID == null){
            PrintWriter script = response.getWriter();
            script.println("<script>");
            script.println("alert('로그인을 하세요')");
            script.println("location.href = 'login.jsp'");
            script.println("</script>");
        } 
        int board_ID = 0;
        if (request.getParameter("board_ID") != null)
        {
        	board_ID = Integer.parseInt(request.getParameter("board_ID"));
        }
        if (board_ID == 0)
        {
            PrintWriter script = response.getWriter();
            script.println("<script>");
            script.println("alert('유효하지 않은 글입니다')");
            script.println("location.href = 'bbs.jsp'");
            script.println("</script>");
        }
        Bbs bbs = new BbsDAO().getBbs(board_ID);
        if (!userID.equals(bbs.getUserID()))
        {
            PrintWriter script = response.getWriter();
            script.println("<script>");
            script.println("alert('권한이 없습니다')");
            script.println("location.href = 'bbs.jsp'");
            script.println("</script>");
        }

        
        else {

            BbsDAO bbsDAO = new BbsDAO();
            int result = bbsDAO.delete(board_ID);            
                if(result == -1){ 
                    PrintWriter script = response.getWriter(); //하나의 스크립트 문장을 넣을 수 있도록.
				    script.println("<script>");
                    script.println("alert('글 삭제에 실패했습니다.')");
                    script.println("history.back()");
                    script.println("</script>");
                    System.out.println(result);

                }
                else { // 글쓰기에 성공했을 경우
                    PrintWriter script = response.getWriter();
                    script.println("<script>");
                    script.println("alert('삭제 성공')");
                    script.println("location.href= 'bbs.jsp'");
                    script.println("</script>");
                    }
            }
    %>
 
      
      
</body>
</html> 
