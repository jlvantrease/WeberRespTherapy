//Add initial functionality to links and buttons when the page first loads
function setFunctionality() {
	addFunctionToTabs();
	setFunctionToUserIcon();
	setNewPasswordFunction();
	addCreateUserFunctionality();
	resetPasswordFunctionality();
	updateUserFunctionality();
	setUserFormsFunctionality();
	loadFunctionalityForDashboardButtons();
}


//Add users or therapies to the links on the left with 
function setFunctionToLeftLinks() {
	
	//Select element of list, de-select all other elements	
	$("li.leftLinkItem").each(function() {
			$(this).click(function() {
				$(".leftLinkItem").each(function() {
					$(this).removeClass("selectedLeftLink");
					$(this).next().css("display", "none");
				});
				$(this).toggleClass("selectedLeftLink");
				$(this).next().toggle();
			});
		});
}

//Add functionality to log out, change password in top right hand corner
function setFunctionToUserIcon() {
	$(".userIcon").mouseenter(function() {
		$("#accountDropDown").show();
	});
	
	$("#accountDropDown").mouseleave(function() {
		$("#accountDropDown").hide();
	});
	
	$("#logOut").click(function(event) {
		event.preventDefault();
		
		$.ajax({
			url: "../ServletUser",
			type: "POST",
			data: {type: 'logout'},
			success: function data()
			{
				$(location).attr('href',"http://localhost:8080/WeberRespiratoryTherapy/login.jsp");
			}
		});
	});
	
	$("#changePassword").click(function() {
		$('#contentHolder').children().hide();
		$("#newPasswordDiv").show();
	});
}


//Make the top left button a button to add a new user
function setAddUserFunctionToButton() {
	$("#leftListButton").click(function() {
		$('#contentHolder').children().hide();
		showNewUser();
	});
}

//Show the dashboard an Admin sees with tabs to filter and edit users, create and edit forms
function showAdminDashboard() {
	$("#usersTab").show();
	$("#formsTab").hide();
	$("#therapyDraftsButton").show();
	$("#inactiveTherapiesButton").show();
	showMainDashboard();
}

//Show the dashboard a non-admin user sees without any ability to see/edit users or create forms
function showUserDashboard() {
	$("#tabMenu").hide();
	$(".menuFilters").hide();
 	showMainDashboard();
 	setLeftButtonToShowPersonalForms();
}

function setLeftButtonToShowPersonalForms() {
	$("#leftListButton").text("Start New Form");
	$("#leftListButton").show();
	
	$("#leftListButton").click(function() {
		$("#therapyFilters").show();
		$("#therapyDraftsButton").hide();
		setLeftButtonToStartNewForm();
	});
	
	showFormsOfUserLinks();
}

function setLeftButtonToStartNewForm() {
	$("#leftListButton").text("Forms List");
	$("#leftListButton").show();
	
	$("#leftListButton").click(function() {
		$("#therapyFilters").show();
		$(".menuFilters").hide();
		setLeftButtonToShowPersonalForms();
	});
}

function showFormsOfUser() {	 	 
	$.ajax({
			url: "../ServletForms",
			type: "POST",
			data: {type: 'getAllForms',
				userID: $("#wNumberDisplay").html()},
			success: function(data)
			{  				
				window.location.reload();
			}
		});
}

//Set functionality to the Admin tabs that hold the filtering buttons
function addFunctionToTabs() {
	$("#therapiesTab").click(function() {
		showAddTherapyButton();
		$("#usersTab").removeClass("active");
		$("#therapiesTab").addClass("active");
		$("#therapyFilters").removeClass("hideFilters");
		$("#userFilters").addClass("hideFilters");
	});
	
	$("#usersTab").click(function() {
		showAddUserButton();
		$("#therapiesTab").removeClass("active");
		$("#usersTab").addClass("active");
		$("#therapyFilters").addClass("hideFilters");
		$("#userFilters").removeClass("hideFilters");
	});
	
	$(".filterButton").click(function() {
		$(".filterButton").removeClass("active");
		$(this).addClass("active");
	});
}


//Make the top left button a button to create a new therapy
function showAddTherapyButton() {
	templateOperation='createTemplate';
	$("#leftListButton").text("Create New Therapy");
	$("#leftListButton").show();
	setAddTherapyFunctionToButton();
}

//Display the "Add New User" button
function showAddUserButton() {
	$("#leftListButton").text("Add New User");
	$("#leftListButton").show();
	setAddUserFunctionToButton();
	
}

//Show the User Information display with blank fields (no existing user info)
function showEditUser() {
	$('#contentHolder').children().hide();
	$("#newUserDiv").show();
	$('#userFormSaveButton').hide();
	$('#userFormUpdateButton').show();
	setUserFormFunctionality(); 
}

//Show the User Information display with the information of an existing user
function showEditUserWithLoadedInfo() {
	$('#contentHolder').children().hide();
	$("#newUserDiv").show();
	$('#userFormSaveButton').hide();
	$('#userFormUpdateButton').show();
	$('#wNumberInput').prop('disabled', true);
}

//Show the User Information display with blank fields (no existing user info)
function showNewUser() {
	$("#userFormClearButton").click();
	$("#userFormSaveButton").show();
	$("#userFormUpdateButton").hide();
	$('#contentHolder').children().hide();
	$("#newUserDiv").show();
	$('#wNumberInput').prop('disabled', false);
	$("#userStatusAdmin").prop('checked', false);
	setUserFormFunctionality(); 
}

//Check that the value in the email field is a valid email address
function isEmail(email) {
	var regex = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
	return regex.test(email);
}

//Adds functionality to the Edit link that drops down when a user name from the left panel is clicked
function addEditUserFunctionalityToListItems() {
	//Click "edit" to edit an element of the list	
	$("a.editLink").each(function() {
		$(this).click(function() {
			var wNumber = $(this).parent().next().text();
			$("#wNumberInput").val(wNumber);
			$("#userFormSaveButton").hide();
			$("#userFormUpdateButton").show();
			populateUserFields(wNumber);
		});
	});	
}

//Adds functionality to the Edit link that drops down when a user name from the left panel is clicked
function addEditTemplateFunctionalityToListItems() {
	//Click "edit" to edit an element of the list	
	$("a.editBelowLink").each(function() {
		$(this).click(function() {
			var templateNumber = $(this).parent().next().text();
			showTemplateInFormGenerator(templateNumber);
		});
	});	
}

//Add functionality to the Add to Form link that drops down when a user clicks on the template name on the left panel
function setAddToFormFunctionalityToListItems() {
	$("a.addToTherapyLink").each(function() {
		$(this).click(function() {
			var therapyId = $(this).parent().next().text();
			showTemplate(therapyId);
		});
	});	
}

//Add functionality to the Edit Form link that drops down when a user clicks on the template name on the left panel
function setFormFunctionToEditLinks() {
	$("a.addToFormLinkBelow").each(function() {
		$(this).click(function() {
			var FormId = $(this).parent().next().text();
			showForm(FormId);
		});
	});	
}

//Adds functionality to show the Form Generator when the "Add New Therapy" button on the top left
function setAddTherapyFunctionToButton() {
	$("#leftListButton").click(function() {
		$('#contentHolder').children().hide();
		$('#formBuilder').show();
		$('#content').show();

	});
}

//Set validation and disabled/enabledness to the fields of the User Information form
function setUserFormFunctionality() {
	//Uncheck inactive checkbox on user form
	$("#inactiveCheckbox").attr('checked', false);
	
	//Disable Year field if user is admin
	$("#userStatusAdmin").click(function() {
		$("#yearInput").prop('disabled', true);
		$("#yearInput").val('');
		$("#yearInput").css({'background-color' : 'lightgray'});
	});
	
	//Enable Year field if any role but admin is clicked
	$("#userStatusStudent").click(function() {
		$("#yearInput").prop('disabled', false);
		$("#yearInput").css({'background-color' : 'white'});
	}); 
	
	//Disable all user fields if inactive is checked
	$("#inactiveCheckbox").click(function() {
		if ($('input[id="inactiveCheckbox"]:checked').length > 0) {
			$(".userFormInput").each(function() {
				$(this).prop('disabled', true);
				$(this).css({'background-color' : 'lightgray'});
			});
		}
		else {
			$(".userFormInput").each(function() {
				$(this).prop('disabled', false);
				$(this).css({'background-color' : 'white'});
			});
			if ($('input[name=studentStatus]:checked').val() == 'Admin') {
				$("#yearInput").prop('disabled', true);
				$("#yearInput").css({'background-color' : 'lightgray'});
			}
		}
	});
	//Click "Clear" to clear the Student Edit Form
	$("#userFormClearButton").click(function() {
		$(".userFormInput").each(function() {
			$(this).val('');
		});
	});	
	
	//Click to return to the dashboard
	$("#backToDashboard").click(function() {
		$("#newUserDiv").hide();
		$("#content").show();

	});
	
	//Validate email entered in new user form 
	$("#userFormEmail").blur(function() {
		if(!isEmail($(this).val())) {
			alert("Please enter a valid email address.");
			$(this).val('');
			$(this).focus();
		}
	});

}

//Call the ServletUser to change the user's password
function setNewPasswordFunction() {
		$("#submitNewPass").click(function(){
		if(!($("#password1").val() == $("#password2").val())) {
			alert("The passwords do not match.  Please enter matching passwords");
			return;
		}
		
		$.ajax({
			url: "../ServletUser",
			type: "POST",
			data: {wNumber: $("#wNumberDisplay").html(), theNewPassword: $("#password1").val(), type: 'changePassword'},
			success: function data()
			{
				alert("Password successfully changed!");
				showMainDashboard();		
			}  			
		});
	});
}

//Call the ServletUser to update a user's information
function updateUserFunctionality() {
	$("#userFormUpdateButton").click(function(){
		if(checkThatAllUserFieldsAreFilledOut()) {
			var isAdminVariable = false;
			var isActiveVariable = true;
			var year;
			
			if ($('input[name=studentStatus]:checked').val() == 'Admin') {
				isAdminVariable = true;
			}
			
			if ($("#inactiveCheckbox").is(":checked")) {
				isActiveVariable = false;
			}
			
			if(isAdminVariable === true) {
				year = "2000";
			}
			else {
				year = $("#yearInput").val();
			}
			
			$.ajax({
				url: "../ServletUser",
				type: "POST",
				data: {firstName: $("#firstNameInput").val(), lastName: $("#lastNameInput").val(), wNumber: $("#wNumberInput").val(), email: $("#emailInput").val(), userYear: year, isAdmin: isAdminVariable,  isActive: isActiveVariable, needsResetPassword: 0, type: 'updateUser'},
				success: function data() {
					alert("User successfully updated!");
					showMainDashboard();
				}	
			});
		}
	});
}

//Calls ServletUser to reset a user's password
function resetPasswordFunctionality() {
	$("#resetPasswordButton").click(function(){
		if(checkThatAllUserFieldsAreFilledOut()) {
			var confirmReset = confirm("Do you really want to reset the password for " + $("#firstNameInput") + " " + $("lastNameInput") + "?");
			if(confirmReset) {
				var isAdminVariable = "0";
				var isActiveVariable = "1";
				var year;
				
				if ($('input[name=studentStatus]:checked').val() == 'Admin') {
					isAdminVariable = "1";
				}
				
				if ($("#inactiveCheckbox").is(":checked")) {
					isActiveVariable = "0";
				}
				
				if(isAdminVariable === "1") {
					year = "2000";
				}
				else {
					year = $("#yearInput").val();
				}
				
				$.ajax({
					url: "../ServletUser",
					type: "POST",
					data: {firstName: $("#firstNameInput").val(), lastName: $("#lastNameInput").val(), wNumber: $("#wNumberInput").val(), email: $("#emailInput").val(), userYear: year, isAdmin: isAdminVariable,  isActive: isActiveVariable, needsResetPassword: 1, type: 'updateUser'},
					success: function data() {
						alert("Password successfully reset!");
						showMainDashboard();
						$(location).attr('href',"http://localhost:8080/WeberRespiratoryTherapy/jsp/dashboard.jsp");
					}	
				});
			}
		}
	});
}


//Calls ServletUser to create a new user
function addCreateUserFunctionality() {	
	$("#userFormSaveButton").click(function(){
		
		if(checkThatAllUserFieldsAreFilledOut()) {
			var isAdminVariable = false;
			
			if ($('input[name=studentStatus]:checked').val() == 'Admin') {
				isAdminVariable = true;
			}
			
			if(isAdminVariable === true) {
				year = "2000";
			}
			else {
				year = $("#yearInput").val();
			}
	
				
			$.ajax({
				url: "../ServletUser",
				type: "POST",
				data: {firstName: $("#firstNameInput").val(), lastName: $("#lastNameInput").val(), wNumber: $("#wNumberInput").val(), email: $("#emailInput").val(), userYear: year, isAdmin: isAdminVariable,  type: 'createUser'},
				success: function data() {
					alert("User successfully added!");
					$(location).attr('href',"http://localhost:8080/WeberRespiratoryTherapy/jsp/dashboard.jsp");
				}	
			});
		}
	});
}


//Validation for all fields of the User Information form
function checkThatAllUserFieldsAreFilledOut() {
	if(!$("#firstNameInput").val() != "") {
		alert("Please enter a first name");
		return false;
	}
	if(!$("#lastNameInput").val() != "") {
		alert("Please enter a last name");
		return false;
	}
	if(!$("#emailInput").val() != "") {
		alert("Please enter an email");
		return false;
	}
	if(!$("#yearInput").val() != "") {
		if($("#userStatusStudent").prop('checked') == "true") {
			alert("Please enter a year");
			return false;
		}
	}
	if(!$("#userStatusAdmin").prop('checked') == "true" && !$("#userStatusStudent").prop('checked')== "true") {
		alert("Please indicate whether the user is an Admin or a Student");
		return false;
	} 
	
	return true;
}

//Shows the main dashboard (no Form Generator, Password Change Form, or User Information form)
function showMainDashboard() {
	$('#contentHolder').children().hide();
	$("#makeASelectionDiv").show();
}

//Fills out the fields of the User Information form with a user's information to edit
function showUserToEditInfo(wNumber, firstName, lastName, email, year, isActive, isAdmin){
	$("#firstNameInput").val(firstName);
	$("#lastNameInput").val(lastName);
	$("#emailInput").val(email);
	$("#yearInput").val(year.substring(0, 4));
	$("#wNumberInput").val(wNumber);
	if(isActive == "true" || isActive == true) { 
		$("#inactiveCheckbox").prop('checked', false);
	} else { 
		$("#inactiveCheckbox").prop('checked', true);
	} 
	
	if(isAdmin == "true" || isAdmin == true) {
		$("#userStatusAdmin").prop('checked', true);
	} else {
		$("#userStatusStudent").prop('checked', true);
	}
}

function setEditTemplateFunctionality() {
	$("a.editTherapyLink").each(function() {
		$(this).click(function() {
			debugger;
			var therapyId = $(this).parent().next().text();
			showTemplateInFormGenerator(therapyId);
		});
	});
}

function setEditFormFunctionality() {
	
	$("a.editFormLink").each(function() {
		$(this).click(function() {
			var formId = $(this).parent().next().text();
			showFormToEdit(formId);
		});
	});
}

function setAddTherapyFunctionToButton() {
	$("#leftListButton").click(function() {
		$('#contentHolder').children().hide();
		$('#formBuilder').show();
	});
}

function setUserFormsFunctionality() {
	$("#saveFilledOutFormButton").click(function() {
		saveFilledOutForm();
	});
	
	$("#saveEditedFormButton").click(function() {
		saveEditedFilledOutForm();
	});

	$("#generatePDFButton").click(function() {
        generatePdfForForm();
	})

}
