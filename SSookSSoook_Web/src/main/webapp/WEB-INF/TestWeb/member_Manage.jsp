<%@page import="kr.smhrd.domain.Board"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html lang="en">
<head>
  <title>Bootstrap Example</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"/>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
<style type="text/css">

  .pageInfo{
      list-style : none;
      display: inline-block;
    margin: 50px 50px 0 100px;      
  }
  .pageInfo li{
      float: left;
    font-size: 15px;
    margin-left: 20px;
    padding: 7px;
    font-weight: 500;
  }
  
  .active{
      background-color: #cdd5ec;
  }
  
  
  .search_area{
    display: inline-block;
    margin-top: 30px;
    margin-left: 260px;
  }
  .search_area input{
      height: 30px;
    width: 250px;
  }
  .search_area button{
     width: 100px;
    height: 36px;
  }
  
 
 a:link {color:black; text-decoration: none;}
 a:visited {color:black; text-decoration: none;}
 a:hover {color:black; text-decoration: underline;}
 
</style>
</head>
<body>
 
<div class="container">
  <h2>회원관리</h2>    
  <div class="panel panel-default">
    <div class="panel-heading">
				
	<button class="btn btn-default btn-sm" onclick="location.href='main'">뒤로가기</button>
					
    </div>
 
    <div class="panel-body">
    
	<table class = "table table-bordered table-hover">
   <tr>
      <th>번호</th>
      <th>아이디</th>
      <th>이메일</th>
      <th>이름</th>
      <th>주소</th>
      <th>핸드폰 번호</th>
      <th>가입날짜</th>
   </tr>
   
   <c:set var="i" value='0'/>
   
   <c:forEach var="m_vo" items="${Mem_list}">
   <tr>
   	  <td>${m_vo.rn}</td>
      <td>${m_vo.id}</a></td>
      <td>${m_vo.email}</td>
      <td>${m_vo.name}</td>
      <td>${m_vo.addr}</td>
      <td>${m_vo.phone}</td>
      <td>${m_vo.joindate}</td>
   </tr>
   </c:forEach>
  </table>
  
  <div class="search_wrap">
        <div class="search_area">
            <input type="text" name="keyword" value="${pageMaker.cri.keyword }">
            <button>Search</button>
        </div>
    </div>  
  
  <div class="pageInfo_wrap" >
        <div class="pageInfo_area">
			<ul id="pageInfo" class="pageInfo">		
			
                <!-- 이전페이지 버튼 -->
                <c:if test="${pageMaker.cri.pageNum > 10}">
                    <li class="pageInfo_btn previous"><a href="${pageMaker.startPage-1}">Previous</a></li>
                </c:if>
                
               <!-- 각 번호 페이지 버튼 -->
               <c:forEach var="num" begin="${pageMaker.startPage}" end="${pageMaker.endPage}">
                   <li class="pageInfo_btn ${pageMaker.cri.pageNum == num ? "active":"" }"><a href="${num}">${num}</a></li>
               </c:forEach>
               
                <!-- 다음페이지 버튼 -->
                <c:if test="${pageMaker.cri.pageNum < pageMaker.realEnd}">
                    <li class="pageInfo_btn next"><a href="${pageMaker.endPage + 1 }">Next</a></li>
                </c:if>
                
 			</ul>
        </div>
    </div>
   
   <form id="moveForm" method="get">
   	<input type="hidden" name="pageNum" value="${pageMaker.cri.pageNum }">
   	<input type="hidden" name="amount" value="${pageMaker.cri.amount }">
   	<input type="hidden" name="keyword" value="${pageMaker.cri.keyword }">
  </form>  
</div>
    <div class="panel-footer">SSook SSook</div>
  </div>
</div>


<script>

/* 페이지 넘기기 js */
let moveForm = $("#moveForm");

   $(".pageInfo a").on("click", function(e){
 
        e.preventDefault();
        moveForm.find("input[name='pageNum']").val($(this).attr("href"));
        moveForm.attr("action", "member_Manage");
        moveForm.submit();
        
    });
   
  
/* 검색 기능 */
   $(".search_area button").on("click", function(e){
       e.preventDefault();
       let val = $("input[name='keyword']").val();
       moveForm.find("input[name='keyword']").val(val);
       moveForm.find("input[name='pageNum']").val(1);
       moveForm.submit();
   });


 
</script>

</body>
</html>


