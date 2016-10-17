<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
    
    <% int numberOfLoginAttempts = 0; %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<link href="css/login.css" rel="stylesheet" type="text/css"/>
	    <link href="css/styles.css" rel="stylesheet" type="text/css"/>
	    
	    <script src="js/jquery-2.1.1.min.js"></script>
	    
	    <meta name="viewport" content="initial-scale = 1.0,maximum-scale = 1.0">
		<title>Weber Respiratory Therapy Charting Login</title>
	</head>
	<body>
		<div id="primaryContainer" class="primaryContainer">
	        <div class="header" id="header">
	            <img id="logo" src="img/chp_horiz_reverse.png" class="logo" />
	        </div>
	        <img id="_logo2" src="img/RespTherapy.png" class="image" />
	        <p id="_title">Charting Sign In</p>
	        
	        <form id="login" name="login">
	            <div id="_login">
	                <input class="textinput" id="username" type="text" placeholder="Username"></input>
	                <input class="textinput" id="password" type="password" placeholder="Password"></input>
	                <input type="submit" id="signIn" name="submit" value="Log In"/>
	            </div>
	        </form>
	        
	        <div id="invalidLogin" style="color:red; text-align:center; font-weight:bold; font-size:20px; display:none;">Invalid Username or Password.  Please try again.</div>
	        
	        <div class="disclaimer">
	            <h4>Disclaimer:</h4>
	            <p>This product is in alpha-development stage.<br/>
	            Design, features, and content are subject to change.</p>
	        </div>
	            
	        <div class="footer">
	            <p id="text">&copy;2014 Weber State University<br /></p>
	        </div>
    	</div>
    <script>   
    
    $(document).ready(function() {
    	<%if("failed".equals((String) session.getAttribute("failed"))) { %>
			$("#invalidLogin").show();
		<%}
		else if(session.getAttribute("user") != null) { %>
			$(location).attr('href',"http://localhost:8080/WeberRespiratoryTherapy/jsp/dashboard.jsp");
		<%}%>
    });
    	$("#login").submit(function(event){
    		event.preventDefault();
    		
    		$.ajax({
    			url: "ServletUser",
    			type: "POST",
    			data: {username: $("#username").val(), password: $("#password").val(), type: 'login'},
    			success: function data()
    			{
    				//$(location).attr('href',"http://localhost:8080/WeberRespiratoryTherapy/jsp/dashboard.jsp");		
    				window.location.reload();
    			}
    			
    		});
    	});
    	
    </script>
	</body>
</html>