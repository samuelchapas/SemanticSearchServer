<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!-- I found this trick here https://stackoverflow.com/questions/8400301/cout-unknown-tag/8400733#8400733 -->
    
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Search Results</title>
</head>
	<body>
		 <div align="center">
		 <center>
	        <table border="1" cellpadding="5">
	        
	        <caption>Results for the search</caption>
	        	<thead>
					<tr>
		            	<td>Id on Database</td>
						<td>Name</td>
						<td>Description</td>
		            	<td style="width: 50%;">Path</td>
		            </tr>
	            </thead>
	            
	            <!-- I found this way to retrieve results and show it on jsp 
	            http://www.codejava.net/java-ee/jsp/how-to-list-records-in-a-database-table-using-jsp-and-jstl -->
	            
	            <!-- This is the way to retrieve the values from a list array of objects 
	            https://stackoverflow.com/questions/8577545/javax-el-propertynotfoundexception-property-foo-not-found-on-type-com-example -->
	            
				<tbody>
					
						<c:forEach var="image" items="${requestScope.imageList}">
			                <tr>
			                    <td><c:out value="${image.imageId}" /></td>
			                    <td><c:out value="${image.name}" /></td>
			                    <td><c:out value="${image.description}" /></td>
			                    <td style="width: 50%;"><c:out value="${image.path}" /></td>
			                </tr>
			            </c:forEach>
		
		            </tbody>
	        	</table>
	        	<form METHOD=GET action="UploadServlet.jsp">
		    					<input type="submit" value = "New Search?"/>
		    	</form>
	        </center>
	    </div>
	</body>
</html>