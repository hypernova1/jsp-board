<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Post List</title>
</head>
<body>
<table>
    <tr>
        <th>번호</th>
        <th>제목</th>
        <th>등록일</th>
    </tr>

    <c:forEach items="${postList}" var="item">
        <tr>
            <td>${item.content}</td>
            <td>${item.title}</td>
            <td></td>
        </tr>

    </c:forEach>

</table>

</body>
</html>
