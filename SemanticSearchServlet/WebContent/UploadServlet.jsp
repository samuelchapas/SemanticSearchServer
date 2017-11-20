<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="latin1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Search or Upload</title>
		<!-- Tomcat v9.0 Server at localhost (5) this is the servlet -->
	</head>
	<body>
		
		<h1 style="color: #5e9ca0;">&nbsp;</h1>
		<table class="editorDemoTable" style="width: 663px;">
		
			<tbody>
		
			<tr>
				<td style="width: 50%;">
					<center>
		
					    <h1>File Upload</h1>
					    
						    <form method="post" action="UploadServlet" enctype="multipart/form-data">
						        Select file to upload: <input type="file" name="file" id="file" /><br />
						        Description: <textarea name="paragraph_text" cols="50" rows="10"></textarea>
						        <br /> <input type="submit" value="Upload" />
						    </form>
				    
					</center>
				</td>
				
				<td style="width: 50%;">
					<center>
						<FORM METHOD=GET ACTION="SearchServlet">
							Search Image for the phrase:
								<INPUT TYPE=TEXT NAME="query"><P>
								<INPUT TYPE=SUBMIT>
						</FORM>
					</center>
				</td>
			</tr>
		
			</tbody>
		</table>
		
	</body>
</html>