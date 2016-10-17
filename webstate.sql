-- CREATE SCHEMA therapy;
USE therapy;
drop TABLE IF EXISTS UserForm;
drop TABLE IF EXISTS User;
drop TABLE IF EXISTS FormTemplate;

-- Brayden Gramse
CREATE TABLE User (
	UserID VARCHAR(10) NOT NULL, -- weber W #
    UserFirst VARCHAR(45) NOT NULL,
    UserLast VARCHAR(45) NOT NULL,
    password varchar(45) NOT NULL,
    UserAdmin BIT(1) NOT NULL,
    UserPassReset BIT(1) NOT NULL,
    UserActive BIT(1) NOT NULL,
    UserEmail VARCHAR(45) NULL,
    UserYear DATE NOT NULL
);

ALTER TABLE User
ADD PRIMARY KEY (UserID);
grant all on therapy.user to adminFxASVVU;

-- Haydn Slade
CREATE TABLE FormTemplate (
  FormTemplateId INT(11) NOT NULL AUTO_INCREMENT,
  FormTemplateName VARCHAR(45) NOT NULL,
  FormTemplateHTML BLOB,
  FormTemplateType VARCHAR(15),
  FormTemplateActive BIT(1),
  FormTemplateVersionNum INT(11),
  PRIMARY KEY (FormTemplateId)
);
grant all on therapy.FormTemplate to adminFxASVVU;

-- Steve Aguirre
CREATE TABLE UserForm (
	UserFormID INT(11) NOT NULL AUTO_INCREMENT,
	UserID VARCHAR(10) NOT NULL,
	FormTemplateHTML BLOB,
	UserFormName varchar(45) NOT NULL,
	UserFormLastEdit TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (UserFormID),
	FOREIGN KEY fk_user(UserID)
   	REFERENCES User(UserID)
);
grant all on therapy.UserForm to adminFxASVVU;

commit;

-- 
--
--  data population
--
-- Users
INSERT INTO User
(UserID,UserFirst,UserLast,password,UserAdmin,UserPassReset,UserActive,UserEmail,UserYear)
VALUES
('admin','System','Administrator', 'admin', true, false, true, 's@w.com', CURDATE());
INSERT INTO User
(UserID,UserFirst,UserLast,password,UserAdmin,UserPassReset,UserActive,UserEmail,UserYear)
VALUES
('nonadmin','Generic','User', 'nonadmin', false, false, true, 'ns@w.com', CURDATE());

commit;

-- User forms
INSERT INTO UserForm
(UserID,FormTemplateHTML, UserFormName)
VALUES
('nonadmin', '<form method="POST" action="" class="form-horizontal">
  <fieldset id="content_form_name">
    <legend>begin1</legend>
  </fieldset>
  <input type="hidden" id="formType" value="beginner">
  <div class="form-group">
    <label class="control-label col-sm-4" for="select_multiple">Select - Multiple</label>
    <div class="controls col-sm-8">
      <select name="" class="form-control" id="select_multiple" multiple="multiple" size="3">
        <option value="1">Option 1</option>
        <option value="2">Option 2</option>
        <option value="3">Option 3</option>
      </select>
    </div>
  </div>
  <div class="form-group">
    <label class="control-label col-sm-4" for="textarea">Textarea</label>
    <div class="controls col-sm-8">
      <textarea name="" class="form-control" id="textarea" placeholder="placeholder"></textarea>
    </div>
  </div>
</form>','begin1');

commit;

INSERT INTO FormTemplate
(FormTemplateName, FormTemplateHTML, FormTemplateType, FormTemplateActive, FormTemplateVersionNum)
VALUES
('Template1', '<form method="POST" action="" class="form-horizontal">
  <fieldset id="content_form_name">
    <legend>begin1</legend>
  </fieldset>
  <input type="hidden" id="formType" value="beginner">
  <div class="form-group">
    <label class="control-label col-sm-4" for="select_multiple">Select - Multiple</label>
    <div class="controls col-sm-8">
      <select name="" class="form-control" id="select_multiple" multiple="multiple" size="3">
        <option value="1">Option 1</option>
        <option value="2">Option 2</option>
        <option value="3">Option 3</option>
      </select>
    </div>
  </div>
  <div class="form-group">
    <label class="control-label col-sm-4" for="textarea">Textarea</label>
    <div class="controls col-sm-8">
      <textarea name="" class="form-control" id="textarea" placeholder="placeholder"></textarea>
    </div>
  </div>
</form>', 'beginner',1,1 );
INSERT INTO FormTemplate
(FormTemplateName, FormTemplateHTML, FormTemplateType, FormTemplateActive, FormTemplateVersionNum)
VALUES
('Template2', '<form method="POST" action="" class="form-horizontal">
  <fieldset id="content_form_name">
    <legend>begin1</legend>
  </fieldset>
  <input type="hidden" id="formType" value="beginner">
  <div class="form-group">
    <label class="control-label col-sm-4" for="select_multiple">Select - Multiple</label>
    <div class="controls col-sm-8">
      <select name="" class="form-control" id="select_multiple" multiple="multiple" size="3">
        <option value="1">Advanced 1</option>
        <option value="2">Option 2</option>
        <option value="3">Option 3</option>
      </select>
    </div>
  </div>
  <div class="form-group">
    <label class="control-label col-sm-4" for="textarea">Advanced</label>
    <div class="controls col-sm-8">
      <textarea name="" class="form-control" id="textarea" placeholder="placeholder"></textarea>
    </div>
  </div>
</form>', 'advanced',1,1 );

INSERT INTO FormTemplate
(FormTemplateName, FormTemplateHTML, FormTemplateType, FormTemplateActive, FormTemplateVersionNum)
VALUES
('Template3', '<form method="POST" action="" class="form-horizontal">
  <fieldset id="content_form_name">
    <legend>begin1</legend>
  </fieldset>
  <input type="hidden" id="formType" value="beginner">
  <div class="form-group">
    <label class="control-label col-sm-4" for="select_multiple">Select - Multiple</label>
    <div class="controls col-sm-8">
      <select name="" class="form-control" id="select_multiple" multiple="multiple" size="3">
        <option value="1">Draft 1</option>
        <option value="2">Option 2</option>
        <option value="3">Option 3</option>
      </select>
    </div>
  </div>
  <div class="form-group">
    <label class="control-label col-sm-4" for="textarea">Draft</label>
    <div class="controls col-sm-8">
      <textarea name="" class="form-control" id="textarea" placeholder="placeholder"></textarea>
    </div>
  </div>
</form>', 'draft',1,1 );

commit;
  