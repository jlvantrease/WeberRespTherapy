<%--
  Created by IntelliJ IDEA.
  User: Vantrease
  Date: 3/11/2017
  Time: 12:36 PM
  To change this template use File | Settings | File Templates.
--%>


<div id="newPatientDiv"  >
    <form id="newPatientForm" accept-charset="UTF-8">
        <div class="formDiv">
            <h2 style="text-align: center;">New Patient</h2>
        </div>
        <div class="formDiv" style="width: 30%; text-align: right">
            First: <input  id="firstNamePatient" type="text" maxlength="45" class="userFormInput"><br/><br/>
            Middle: <input  id="middleNamePatient" type="text" maxlength="45" class="userFormInput"><br/><br/>
            Last: <input id="lastNamePatient" type="text" maxlength="45" class="userFormInput"><br/><br/>
            Date Of Birth: <input  id="dobPatient" type="text" maxlength="45" class="userFormInput"><br/><br/>
            Gender: <input  id="genderPatient" type="text" maxlength="45" class="userFormInput"><br/><br/>
            Phone: <input  id="phonePatient" type="text" maxlength="45" class="userFormInput"><br/><br/>
        </div>

        <hr class="formHR">
        <div class="formFooterWithButtonsDiv">
					<span id="buttonsToTheLeftSpan">
						<button type="button" id="backToDashboard" class="userDivButton">Back</button>
                    </span>
            <button type="button" class="userDivButton" id="newPatientSaveButton">Save
                New Patient</button>
            <button type="button" class="userDivButton"
                    id="userFormUpdateButton">Update User</button>
        </div>
    </form>
</div>


