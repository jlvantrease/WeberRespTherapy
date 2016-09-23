<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="edu.weber.resptherapy.charting.Dashboard"%>
<%@ page import="edu.weber.resptherapy.charting.User"%>
<%@ page import="edu.weber.resptherapy.charting.TherapyTemplate"%>
<%@ page import="edu.weber.resptherapy.charting.Form"%>
<%@ page import="java.io.BufferedReader"%>
<%@ page import="java.io.StringReader"%>

<!-- Global Java Variables -->
<%
	Dashboard dashboard = new Dashboard();
%>
<%
	ArrayList<String> leftPaneList = new ArrayList<String>();
%>
<%
	User loggedInUser = (User) session.getAttribute("user");
%>
<%
	System.out.println(loggedInUser);
%>
<%
	Map<String, User> allUsers = new HashMap<String, User>();
%>
<%
	Map<Integer, TherapyTemplate> allTemplates = new HashMap<Integer, TherapyTemplate>();
%>
<%
	boolean isEditingTemplate = false;
%>
<%
	boolean isEditingUser = false;
%>
<%
	boolean isEditingForm = false;
%>
<%
	User userBeingEdited = (User) session.getAttribute("userToBeEdited");
%>
<%
	TherapyTemplate getTemplateToFillOut = (TherapyTemplate) session.getAttribute("getTemplateToFillOut");
%>
<%
	Map<Integer, Form> userForms = (Map<Integer, Form>) session.getAttribute("userForms");
%>
<%
	Form formToFillOut =  (Form) session.getAttribute("formToFillOut");
%>

<!DOCTYPE html>
<html>
<head>
<title>Respiratory Therapy Charting</title>
<link rel="stylesheet" href="../css/styles.css">
<link rel="stylesheet" href="../css/bootstrap.min.css">
<link rel="stylesheet" href="../css/bootstrap-theme.min.css">
<link rel="stylesheet" href="../css/font-awesome.min.css">
<link rel="stylesheet" href="../css/dashboard.css" type="text/css" />
<link href="../css/codemirror.css" rel="stylesheet">
<link href="../css/form_builder.css" rel="stylesheet">

<script src="../js/jquery-2.1.1.min.js"></script>
<script src="../js/jquery-ui.min.js"></script>
<script src="../js/bootstrap.min.js"></script>
<script src="../js/dashboard.js"></script>
<script src="../js/form_builder.js"></script>
<script src="../js/codemirror.min.js"></script>
<script src="../js/formatting.js"></script>

<meta name="viewport" content="initial-scale = 1.0,maximum-scale = 1.0">
<script>
        
     $(document).ready(function() {
        	
        	populateLoggedInUserInfo(); 
        	
        	//Sets the functionality to all of the buttons and links on the page
        	setFunctionality(); 
        	
        	//Displays correct content for either Admin or Student role
        	showStudentOrAdminDashboard();
        	
        });
                	
        
		</script>
</head>
<body>
	<div class="header">
		<img src="../img/chp_horiz_reverse.png" class="logo" id="logo"
			alt="logo" />
		<div class="userContainer verticalcenter">
			<div class="userName">
				<span id="userNameDisplay">Zane Fairbanks</span> <br /> <span
					id="wNumberDisplay">W#12345678</span>
			</div>
			<i id="userImage" class="userIcon fa fa-user-md fa-inverse fa-2x"></i>
			<div id="accountDropDown">
				<a id="changePassword">Change Password</a> <br> <a id="logOut">Log
					Out</a>
			</div>
		</div>
	</div>

	<div class="leftSideBackground"></div>
	<div class="leftListDiv">
		<button id="leftListButton" style="display: none;"></button>
		<hr class="leftListHR">
		<div class="leftListHeading">&nbsp;</div>
		<hr class="leftListHR">
		<ul id="leftListItems">
		</ul>
	</div>

	<div class="menu" id="tabMenu">
		<div class="menuTabs">
			<div class="menuTab btn btn-default active" id="formsTab">My
				Forms</div>
			<div class="menuTab btn btn-default active" id="therapiesTab">Therapies</div>
			<div class="menuTab btn btn-default" id="usersTab">Users</div>
		</div>
	</div>
	<div class="menuFilters" id="therapyFilters">
		<button class="filterButton btn btn-xs" id="beginningTherapiesButton">Beginning
			Therapies</button>
		<button class="filterButton btn btn-xs" id="advancedTherapiesButton">Advanced
			Therapies</button>
		<button class="filterButton btn btn-xs" id="therapyDraftsButton">Therapy
			Drafts</button>
	</div>
	<div class="menuFilters hideFilters" id="userFilters">
		<button class="filterButton btn btn-xs" id="firstYearsButton">Current
			First Years</button>
		<button class="filterButton btn btn-xs" id="secondYearsButton">Current
			Second Years</button>
		<button class="filterButton btn btn-xs" id="allStudentsButton">All
			Students</button>
		<button class="filterButton btn btn-xs" id="inactiveStudentsButton">Inactive
			Students</button>
		<button class="filterButton btn btn-xs" id="adminsButton">All
			Admins</button>
	</div>

	<div class="contentHolder" id="contentHolder">
		<div id="makeASelectionDiv">
			<h3 class="verticalcenter" id="pleaseSelect"
				style="text-align: center;">Please make a selection</h3>
		</div>

		<div id="formBuilder"><jsp:include page="formBuilder.html" /></div>

		<div id="templateForm" style="display: none;">
			Form Name: <input type="text" id="formName" />
			<div id="templateFormHtml"></div>
			<div
				style="margin-left: auto; margin-right: auto; text-align: center;">
				<button id="saveFilledOutFormButton">Save</button>
				<button id="saveEditedFormButton">Update</button>
				<button id="generatePDFButton">Generate PDF</button>
			</div>
		</div>

		<div id="newUserDiv" style="display: none;">
			<form id="userForm" accept-charset="UTF-8">
				<div class="formDiv">
					<h2 style="text-align: center;">User Information</h2>
				</div>
				<div class="formDiv">
					First: <input id="firstNameInput" type="text" class="userFormInput">
					Last: <input id="lastNameInput" type="text" class="userFormInput">
				</div>
				<hr class="formHR">
				<div class="formDiv">
					W#:&nbsp;&nbsp;&nbsp;&nbsp;<input id="wNumberInput" type="text"
						class="userFormInput">
				</div>
				<div class="formDiv">
					Year:&nbsp;&nbsp;<input type="text" id="yearInput"
						class="userFormInput" id="userFormYear">
				</div>
				<div class="formDiv">
					Email: <input type="text" id="emailInput" class="userFormInput"
						id="userFormEmail">
				</div>
				<div class="formDiv">
					<input type="radio" name="studentStatus" class="userFormInput"
						id="userStatusStudent" value="Student">&nbsp;Student&nbsp;&nbsp;
					<input type="radio" name="studentStatus" class="userFormInput"
						id="userStatusAdmin" value="Admin">&nbsp;Administrator&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<input type="checkbox" name="inactiveCheckbox"
						id="inactiveCheckbox">&nbsp;Inactive
				</div>
				<br>
				<hr class="formHR">
				<div class="formFooterWithButtonsDiv">
					<span id="buttonsToTheLeftSpan">
						<button type="button" id="userFormClearButton"
							class="userDivButton">Clear</button>
						<button type="button" id="backToDashboard" class="userDivButton">Back</button>
						<button type="button" id="resetPasswordButton"
							class="userDivButton">Reset User Password</button>
					</span>
					<button type="button" class="userDivButton" id="userFormSaveButton">Save
						New User</button>
					<button type="button" class="userDivButton"
						id="userFormUpdateButton">Update User</button>
				</div>
			</form>
		</div>

		<div id="newPasswordDiv" style="display: none;">
			<div>
				Enter New Password: <input class="passInput" type="text"
					class="userFormInput" id="password1">
			</div>
			<div>
				Verify New Password: <input class="passInput" type="text"
					class="userFormInput" id="password2">
			</div>
			<div>
				<input class="passInput" type="button" id="submitNewPass"
					name="submitPass" value="Change Password" />
			</div>
		</div>
	</div>







	<div class="footer">
		<p id="text">
			&copy;2014 Weber State University<br />
		</p>
	</div>
</body>
<script>
    
    //Called when a User is clicked on in the left pane to edit
    function populateUserFields(wNumber) {
    	$.ajax({
			url: "../ServletUser",
			type: "POST",
			data: {userId: wNumber, type: 'getUserToEdit'},
			success: function data() {
					<%isEditingUser = true;%>
					window.location.reload();
			}
			
		});
    }
  
  //Fill the main content pane with the HTML of a stored template
    function showTemplate(therapyId) {
    	$('#contentHolder').children().hide();
    	var templateHtml;
    	
    	$.ajax({
    		url: "../ServletTherapy",
    		type: "POST",
    		data: {templateId: therapyId, type: 'getTemplateToFillOut'},
    		success: function data() {
    			<%isEditingUser = true;%>
    			window.location.reload();
    		}	
    	});
    }
  
  function saveFilledOutForm() {
	  var theFormHtml = $("#templateFormHtml").html();
	  var userNumber = '<%=loggedInUser.getUserId()%>';
	  if($("#formName").val() != null) {
		 var nameOfForm =  $("#formName").val();
	  }
	  else {
		  alert("Please enter a form name");
	  }
	  
	  $.ajax({
 			url: "../ServletForms",
 			type: "POST",
 			data: {type: 'createForm',
 				formHTML: theFormHtml,
 				formName: nameOfForm,
 				userID: userNumber},
 			success: function(data)
 			{  				
 				alert("Form saved!");
 				window.location.reload();
 			}
 		});
  }
  
  function saveEditedFilledOutForm() {
	  $("#saveEditedFormButton").click(function() {
		  var formHtml = $("#templateFormHtml").html();
		  var wNumber = '<%=loggedInUser.getUserId()%>';
		  var formName = '<%=loggedInUser.getUserId()%>';
		  
		  <%if(formToFillOut != null) {%>
		  	var formId = '<%=formToFillOut.getId()%>';
		  <%}%>
			  
		  if($("#formName").val() != null) {
			 var nameOfForm =  $("#formName").val();
		  }
		  else {
			  alert("Please enter a form name");
		  }
		  
		  $.ajax({
  			url: "../ServletForms",
  			type: "POST",
  			data: {type: 'updateForm',
  				theFormHTML: formHtml,
  				theFormName: nameOfForm,
  				userId: wNumber, 
  				theFormId: formId},
  			success: function(data)
  			{  				
  				alert("Form updated!");
  				window.location.reload();
  			}
  		});

	  });
  }
  
  
  function showFormToEdit(formId) {
		  $.ajax({
  			url: "../ServletForms",
  			type: "POST",
  			data: {type: 'getFormToEdit',
  				userID: '<%=loggedInUser.getUserId()%>',
  				formID: formId},
  			success: function(data)
  			{  				
  				<%isEditingForm = true;%>
  				window.location.reload();
  			}
  		});
  }
    
 //Fill outs the user name and w# in the top righthand corner of the header  
  function populateLoggedInUserInfo() {
	//Fill outs the user name and w# in the top righthand corner of the header
  	$("#userNameDisplay").html('<%=loggedInUser.getFirstName() + " " + loggedInUser.getLastName()%>');
  	$("#wNumberDisplay").html("<%=loggedInUser.getUserId()%>"); 
  }
 
 function showStudentOrAdminDashboard() {
	//Admin sees tabs to get users
 	<%if(loggedInUser.isAdmin()) { %>
        	showAdminDashboard();
        	setEditTemplateFunctionality();
        	<%allUsers = (HashMap<String, User>) session.getAttribute("allUsers");%>
        	
        	//If the admin is editing a user the userToBeEdited is retrieved from the session and the user info is loaded into the user edit form
        		<%if(isEditingUser && userBeingEdited != null) {%>
        		showUserToEditInfo('<%=userBeingEdited.getUserId()%>', '<%=userBeingEdited.getFirstName()%>', '<%=userBeingEdited.getLastName()%>', '<%=userBeingEdited.getEmail()%>', '<%=userBeingEdited.getYear()%>', '<%=userBeingEdited.isActive()%>', '<%=userBeingEdited.isAdmin()%>');
				showEditUserWithLoadedInfo();
				<%}
        	
        		else if (isEditingTemplate && getTemplateToFillOut != null && loggedInUser.isAdmin()) { %>
        			$('#contentHolder').children().hide();
        			$('#formBuilder').show();	
					$('#generatedForm').html(<%=getTemplateToFillOut%>);
				<%}
		}
 	
 	
 	    //If the logged in user is a student and they are filling out a new form - show the original template
		else if(!loggedInUser.isAdmin()) {%>	
			showUserDashboard();
			
			<%if(isEditingTemplate && getTemplateToFillOut != null) {%>
				<%String templateHtml = ""; 
				BufferedReader bufReader = new BufferedReader(new StringReader(getTemplateToFillOut.getTheHtml()));
				String line=null;
				while( (line=bufReader.readLine()) != null )
				{
					templateHtml += line;
				}%>
				
				var originalFormHtml = $("#templateFormHtml").html();
				
				$('#contentHolder').children().hide();
				$("#templateFormHtml").html(originalFormHtml + '<%=templateHtml%>');
				$("#saveFilledOutFormButton").show();
				$("#saveEditedFormButton").hide();
				$("#templateForm").show();
			<%} 
			
			//If the logged in user is a student and they are editing one of their previous forms
			else if(isEditingForm && formToFillOut != null) {%>
				<%String formHtml = ""; 
				BufferedReader bufReader = new BufferedReader(new StringReader(formToFillOut.getTheFormHtml()));
				String line=null;
				while( (line=bufReader.readLine()) != null ) {
					formHtml += line;
				}%>
				$('#contentHolder').children().hide();
				$("#templateFormHtml").html($("#templateFormHtml").html() + '<%=formHtml%>');
				$("#saveFilledOutFormButton").hide();
				$("#saveEditedFormButton").show();
				$("#templateForm").show();
			<%} 
		}

		//All templates are loaded from the session to display on the left side
	    allTemplates = (HashMap<Integer, TherapyTemplate>) session.getAttribute("allTemplates"); %>
	}

	function showFormsOfUserLinks() {
		<% if(userForms == null) { %>
			showFormsOfUser();
		<%}%>
		
		<%if(userForms != null) {
			Map<String, String> personalForms = new HashMap<String, String>();
			for(Integer key : userForms.keySet()) {
				personalForms.put("" + userForms.get(key).getId(), userForms.get(key).getName());
			}
					
			String userFormsHtml = dashboard.updateLeftLinks(personalForms, "forms", loggedInUser.isAdmin());%>
			
			$("#leftListItems").replaceWith(<%=userFormsHtml%>);
		<%}%>
		setFunctionToLeftLinks();
		setEditFormFunctionality()
	}

	function loadFunctionalityForDashboardButtons() {
		//"First Year Students" button
		$("#firstYearsButton").click(function() {
				$(".leftListHeading").text("First Year Students");
				showAddUserButton();
				<%Map<String, User> currentFirstYears = dashboard.getCurrentFirstYears(allUsers); 
        		Map<String, String> firstYearStudents = new HashMap<String, String>();
        		
        		for(String key : currentFirstYears.keySet()) {
        			firstYearStudents.put(currentFirstYears.get(key).getUserId(), currentFirstYears.get(key).getLastName() + ", " + currentFirstYears.get(key).getFirstName());
        		}
        		String newFirstYearsHtml = dashboard.updateLeftLinks(firstYearStudents, "users", true);%>
				$("#leftListItems").replaceWith(<%=newFirstYearsHtml%>);
					setFunctionToLeftLinks();
					addEditUserFunctionalityToListItems();
				});

		//"Second Year Students" button
		$("#secondYearsButton").click(function() {
			$(".leftListHeading").text("Second Year Students");
			showAddUserButton();
			<%Map<String, User> currentSecondYears = dashboard.getCurrentSecondYears(allUsers); 
        	Map<String, String> secondYearStudents = new HashMap<String, String>();
        	for(String key : currentSecondYears.keySet()) {
        		secondYearStudents.put(currentSecondYears.get(key).getUserId(), currentSecondYears.get(key).getLastName() + ", " + currentSecondYears.get(key).getFirstName());
        	}
        		
        	String newSecondYearsHtml = dashboard.updateLeftLinks(secondYearStudents, "users", true);%>
			$("#leftListItems").replaceWith(<%=newSecondYearsHtml%>);
			setFunctionToLeftLinks();
			addEditUserFunctionalityToListItems();
		});

		//"All Students" button
		$("#allStudentsButton").click(function() {
			$(".leftListHeading").text("All Students");
			showAddUserButton();
			<%Map<String, User> allStudentsFirstYear = dashboard.getCurrentFirstYears(allUsers); 
        	Map<String, String> allStudents = new HashMap<String, String>();
        	for(String key : allStudentsFirstYear.keySet()) {
        		allStudents.put(allStudentsFirstYear.get(key).getUserId(), allStudentsFirstYear.get(key).getLastName() + ", " + allStudentsFirstYear.get(key).getFirstName());
        	}
        		
        	Map<String, User> allStudentsSecondYear = dashboard.getCurrentSecondYears(allUsers); 
        	for(String key : allStudentsSecondYear.keySet()) {
        		allStudents.put(allStudentsSecondYear.get(key).getUserId(), allStudentsSecondYear.get(key).getLastName() + ", " + allStudentsSecondYear.get(key).getFirstName());
        	}

        	String newAllStudentsHtml = dashboard.updateLeftLinks(allStudents, "users", true);%>
			$("#leftListItems").replaceWith(<%=newAllStudentsHtml%>);
				setFunctionToLeftLinks();
				addEditUserFunctionalityToListItems();
		});

		//"Inactive Students" button
		$("#inactiveStudentsButton").click(function() {
			$(".leftListHeading").text("Inactive Students");
			showAddUserButton();
			<%Map<String, User> inactiveUserMap = dashboard.getInactiveUsers(allUsers);
        	Map<String, String> inactiveUsers = new HashMap<String, String>();
        	for(String key : inactiveUserMap.keySet()) {
        		inactiveUsers.put(inactiveUserMap.get(key).getUserId(), inactiveUserMap.get(key).getLastName() + ", " + inactiveUserMap.get(key).getFirstName());
        	}
        	String newInactiveStudentsHtml = dashboard.updateLeftLinks(inactiveUsers, "users", true);%>
			$("#leftListItems").replaceWith(<%=newInactiveStudentsHtml%>);
				setFunctionToLeftLinks();
				addEditUserFunctionalityToListItems();
		});

		//"Beginning Therapies" button
		$("#beginningTherapiesButton").click(function() {
			$(".leftListHeading").text("Beginning Therapies");
			<%if(loggedInUser.isAdmin()) {%>
				showAddTherapyButton();
			<%}%>
	
			<%Map<Integer, TherapyTemplate> beginningTherapiesMap = dashboard.getBeginningTherapies(allTemplates);
        	Map<String, String> beginningTherapies = new HashMap<String, String>();
 			for(Integer key : beginningTherapiesMap.keySet()) {
 				beginningTherapies.put(Integer.toString(beginningTherapiesMap.get(key).getId()), beginningTherapiesMap.get(key).getName());
 			}
        		
        		
        		String newBeginningTherapyHtml = dashboard.updateLeftLinks(beginningTherapies, "therapies", loggedInUser.isAdmin());%>
					$("#leftListItems").replaceWith(<%=newBeginningTherapyHtml%>);
				<%if(loggedInUser.isAdmin()) {%>
					setAddTherapyFunctionToButton();
				<%}%>
			
				setFunctionToLeftLinks();
				
				<%if(loggedInUser.isAdmin()) {%>
					setEditTemplateFunctionality();
				<%}

				else {%>
					setAddToFormFunctionalityToListItems();
				<%}%>
		 });

		//"Advanced Therapies" button
		$("#advancedTherapiesButton").click(function() {
			$(".leftListHeading").text("Advanced Therapies");
			<%if(loggedInUser.isAdmin()) {%>
				showAddTherapyButton();
			<%}%>
	
			<%Map<Integer, TherapyTemplate> advancedTherapiesMap = dashboard.getAdvancedTherapies(allTemplates);
        	Map<String, String> advancedTherapies = new HashMap<String, String>();
 			for(Integer key : advancedTherapiesMap.keySet()) {
 				advancedTherapies.put(Integer.toString(advancedTherapiesMap.get(key).getId()), advancedTherapiesMap.get(key).getName());
 			}
        	String newAdvancedTherapiesHtml = dashboard.updateLeftLinks(advancedTherapies, "therapies", loggedInUser.isAdmin());%>
			$("#leftListItems").replaceWith(<%=newAdvancedTherapiesHtml%>);
			
			<%if(loggedInUser.isAdmin()) {%>
				setAddTherapyFunctionToButton();
			<%}%>	
			
			setFunctionToLeftLinks();
			
			<%if(loggedInUser.isAdmin()) {%>
				setEditTemplateFunctionality();
			<%}
			else {%>
				setAddToFormFunctionalityToListItems();
			<%}%>
		});

		//"Therapy Drafts" button
		$("#therapyDraftsButton").click(function() {
			$(".leftListHeading").text("Therapy Drafts");
			<%if(loggedInUser.isAdmin()) {%>
				showAddTherapyButton();
			<%}%>
	
			<%Map<Integer, TherapyTemplate> draftTherapiesMap = dashboard.getTherapyDrafts(allTemplates);
        	Map<String, String> draftTherapies = new HashMap<String, String>();
 			for(Integer key : draftTherapiesMap.keySet()) {
 				draftTherapies.put(Integer.toString(draftTherapiesMap.get(key).getId()), draftTherapiesMap.get(key).getName());
 			}
        			
        	String newTherapyDraftsHtml = dashboard.updateLeftLinks(draftTherapies, "therapies", loggedInUser.isAdmin());%>
			$("#leftListItems").replaceWith(<%=newTherapyDraftsHtml%>);
			
			<%if(loggedInUser.isAdmin()) {%>
				setAddTherapyFunctionToButton();
			<%}%>
			setFunctionToLeftLinks();
			setEditTemplateFunctionality();
		});

		//"All Admins" button
		$("#adminsButton").click(function() {
			$(".leftListHeading").text("Administrators");
			showAddUserButton();
			<%Map<String, User> allAdmins = dashboard.getAdmins(allUsers);
        	Map<String, String> admins = new HashMap<String, String>();
        	for(String key : allAdmins.keySet()) {
        		admins.put(allAdmins.get(key).getUserId(), allAdmins.get(key).getLastName() + ", " + allAdmins.get(key).getFirstName());
        	}
        	String newAdminsHtml = dashboard.updateLeftLinks(admins, "users", true);%>
        	
			$("#leftListItems").replaceWith(<%= newAdminsHtml %>);
			setFunctionToLeftLinks();
			addEditUserFunctionalityToListItems();
		});
	}

	$('#saveForm').click(function(event) {
		event.preventDefault();
		console.log("saveForm clicked");
		var formHTML = source.getValue();

		$.ajax({
			url : "../ServletTherapy",
			type : "POST",
			data : {
				type : 'createTemplate',
				templateHTML : formHTML,
				templateName : $("#content_form_name > legend").text().trim(),
				templateType : $("#formType").val()
			},
			success : function(data) {
				$(location).attr('href', "/jsp/dashboard.jsp");
				location.reload();
			}
		});
		return false;

	});

	$('#content_form_name').click(function() {
		var formName = $('#content_form_name > legend').text();
		if (formName == "Form Name") {
			$('#content_form_name > legend').text('');
		}
	});
	
	//TODO:  Fill this out so that the stored html shows up within the form generator
	function showTemplateInFormGenerator(therapyId) {
		$('#contentHolder').children().hide();
		var templateHtml;
		
		$.ajax({
			url: "../ServletTherapy",
			type: "POST",
			data: {templateId: therapyId, type: 'getTemplateToFillOut'},
			success: function data() {
				<% isEditingTemplate = true; %>
				window.location.reload();
			}	
		});
		
	}
</script>
</html>