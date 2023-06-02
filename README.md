# Information-service

## Fronted configuration for local developement

First you need to install [Node.js](https://nodejs.org/en)

Once Node is ready you can now install project dependencies and run the server. Open website directory in terminal and then follow these steps:  
1. npm install - needed to install dependencies locally, you need to do this step only when it's your first time running frontend on your machine or if something changed in file website/package.json
2. npm run dev - this command spins up server hosting website, output of the command will have the address and port that you can use access the website

To shut down the server simply use CTRL+C hotkey or close terminal.

## Backend endpoints
### Login:
<ol>
    <li><b>GET /client/login (/editorial/login)</b></li>
        This endpoint allows the retrieval of a login page or a logout confirmation message depending on the presence of a query parameter named "logout".
    <br><br>
    <b>Query Parameters:</b>
    <br>
    <ul>
         <li>logout (optional): a string parameter used to indicate if the user has logged out.</li>
    </ul>
    <br>
    <b>Response:</b>
    <br>
    The response of this endpoint is a ResponseEntity object that wraps the response message and the HTTP status code. 
    If the "logout" parameter is present, the message "You have been successfully logged out." will be returned with a 200 OK status code.
    If the "logout" parameter is not present, the message "Login page." will be returned with a 200 OK status code.
    <br><br>
    <li><b>POST /client/login/v2 (/editorial/login/v2)</b></li>
        This endpoint allows a user to log in to the system. The login credentials must be provided in the request body in JSON format.
        If the login is successful, the client will be authorized.
    <br><br>
    <b>Request Body:</b>
    <br>
    The request body must contain a JSON object representing the user's login information. The object should contain the following fields:
    <ul>
        <li>username (required): a string representing the user's username.</li>
        <li>password (required): a string representing the user's password.</li>
    </ul>
    <br>
    <b>Response:</b>
    <br>
    The response is a status code indicating the outcome of the login operation. If the login was successful, a 200 OK status code is returned. 
    If the login credentials are incorrect, a 401 Unauthorized status code is returned. 
    If the user is trying to sign in with an account created by another provider, a 401 Unauthorized status code is returned with information "You cannot log in this way. You must log in via provider_name" (Only for /client/login/v2 endpoint).
    If the server encountered an error while processing the login request, a 500 Internal Server Error status code is returned.
    <br><br>
    <li><b>GET /client/login/google</b></li>
    This endpoint redirects the user to Google's authorization endpoint, allowing them to authenticate with their Google account. 
    The endpoint constructs an authorization URL with the required parameters, including the client ID and redirect URI.
    The user is redirected to this URL, where they can authenticate and grant access to the requested scopes.
    After successful authentication, the user is redirected back to the client application.
    <br><br>
    <li><b>GET /client/login/oauth2/code/google</b></li>
    This endpoint is used to authenticate users via Google OAuth 2.0 protocol. 
    The endpoint receives an authorization code from the Google authorization server and returns a response containing the user's information. It <b>SHOULD NOT BE DIRECTLY ACCESSED!!!</b>
    <br><br>
    <b>Query Parameters:</b>
    <br>
    <ul>
         <li>code (required): a string representing the authorization code returned by the Google authorization server.</li>
    </ul>
    <br>
    <b>Response:</b>
    <br>
    The endpoint returns a ResponseEntity object that contains a string representing the status of the authentication process.
    If the authentication is successful, the response body contains a message "Successful login via google account." indicating that the user has been logged in successfully.
    If the authentication fails, the response body contains a message "Unsuccessful authentication via google account." indicating that the user was not authenticated.
    Additionally, the endpoint uses a RestTemplate to communicate with the Google API and retrieve the user's information. 
    If the user does not exist in the system, the endpoint also registers the user with the system by creating a UserRegistrationDto object and calling the registerService.registerUser() method. 
    If the user is successfully registered, the endpoint also registers the user with the editorial service by calling the registerService.registerUserClientToEditorial() method.
</ol>

### Registration:
<ol>
    <li><b>POST /client/registration (/editorial/registration)</b></li>
        This endpoint allows users to register an account in the system. 
        The endpoint expects a POST request with a JSON object representing the user registration data in the request body.
    <br><br>
    <b>Request Body:</b>
    <br>
    The request body must contain a JSON object with the following fields:
    <ul>
        <li>username (required): a string representing the user's username.</li>
        <li>password (required): a string representing the user's password.</li>
        <li>email (required): a string representing the user's email address.</li>
        <li>name (required): a string representing the user's first name.</li>
        <li>surname (required): a string representing the user's last name.</li>
        <li>authorityName (required): a string representing the user's authority name (Only for /editorial/registration endpoint).</li>
    </ul>
    <br>
    <b>Response:</b>
    <br>
    This endpoint returns an HTTP response with a status code and an optional message. 
    If the registration process is successful, it returns a response with a 200 OK status code and the message "Correct registration process." 
    If the email or username already exists in the database, it returns a response with a 409 CONFLICT status code and an error message indicating that the email or username is already taken. 
    If there is any other error during the registration process, such as an error while trying to register the user with an editorial service, it returns a response with the appropriate HTTP status code and an error message explaining the issue.
    <br><br>
    <li><b>POST /client/registration/fe (/editorial/registration/fc)</b></li>
        This endpoint handles user registration from an editorial microservice. It receives a POST request with a JSON payload containing user information. 
        It checks if the email or username already exists in the database, and if not, it registers the user. 
        It also checks if the request header "X-Caller" contains the value "REGISTRATION_FROM_EDITORIAL". 
        If it does not contain this value, the endpoint returns a bad request response. If the value is present, the endpoint returns a success response.
        It can only be accessed by the caller with X-Caller header set to "REGISTRATION_FROM_EDITORIAL" ("REGISTRATION_FROM_CLIENT"). So it <b>SHOULD NOT BE DIRECTLY ACCESSED!!!</b>
    <br><br>
    <b>Request Body:</b>
    <br>
    The request body must contain a JSON object representing the user information. The object should contain the following fields:
    <ul>
        <li>username (required): a string representing the user's username.</li>
        <li>password (required): a string representing the user's password.</li>
        <li>email (required): a string representing the user's email address.</li>
        <li>name (required): a string representing the user's first name.</li>
        <li>surname (required): a string representing the user's last name.</li>
    </ul>
    <br>
    <b>Headers:</b>
    <br>
    <ul>
        <li>X-Caller (required): a string representing the caller's identity. Must be set to "REGISTRATION_FROM_EDITORIAL" ("REGISTRATION_FROM_CLIENT").</li>
    </ul>
    <br>
    <b>Response:</b>
    <br>
    If the email or username already exists in the database, the endpoint returns a conflict response with an error message. 
    If the "X-Caller" header does not contain the expected value, the endpoint returns a bad request response with an error message. 
    If the registration process is successful, the endpoint returns a success response with a message indicating the success of the registration process.
    <br><br>
    <li><b>GET /client/validate</b></li>
    This endpoint allows users to validate their account using a validation code. 
    The endpoint expects a GET request with the validation code as a query parameter.
    <br><br>
    <b>Request Parameters:</b>
    <ul>
        <li>code (required): a string representing the validation code associated with the user account.</li>
    </ul>
    <br>
    <b>Response:</b>
    <br>
    This endpoint returns an HTTP response with a status code and an optional message. 
    If the validation code is valid and the user account is successfully activated, it returns a response with a 200 OK status code and the message "User successfully enabled!"
    If there is any other error during the validation process, such as an error while trying to enable the user in the editorial system, it returns a response with the appropriate HTTP status code and an error message explaining the issue (400 - Bad Request).
    <br><br>
    <li><b>POST /editorial/validate</b></li>
    This endpoint allows enabling a user from the client application in the editorial microservice. 
    The endpoint expects a POST request with the user ID in the request body and the X-Caller header set to "ACCOUNT_ENABLE" to authorize the operation.
    <br><br>
    <b>Request Headers:</b>
    <ul>
        <li>X-Caller (required): a string representing the caller of the API. It must be set to "ACCOUNT_ENABLE" to authorize the account enabling process.</li>
    </ul>
    <br>
    <b>Request Body:</b>
    <br>
    The request body must contain a JSON object with the following fields:
    <ul>
        <li>userId (required): a string or number representing the ID of the user to be enabled in the editorial system.</li>
    </ul>
    <br>
    <b>Response:</b>
    <br>
    This endpoint returns an HTTP response with a status code and an optional message. <b>IT SHOULD NOT BE DIRECTLY ACCESSED!!!</b> <br>
    If the account enabling process is successful, it returns a response with a 200 OK status code and the message "User successfully enabled!" 
    If the X-Caller header is missing or set to an invalid value, it returns a response with a 400 BAD REQUEST status code and an error message indicating that the account enabling process was unsuccessful.
    If the user ID is not found in the editorial database, it returns a response with a 400 BAD REQUEST status code and an error message indicating that the user with the given ID was not found.
    If the user is already enabled in the editorial system, it returns a response with a 400 BAD REQUEST status code and an error message indicating that the user is already enabled.
    If there is any other error during the account enabling process, it returns a response with the appropriate HTTP status code and an error message explaining the issue.
</ol>

### Articles:
<ol>
    <li><b>GET /client/articles</b></li>
        This endpoint retrieves a list of articles. You can optionally filter the articles by category by providing a <b>category</b> query parameter.
    <br><br>
    <b>Query Parameters:</b>
    <br>
    <ul>
        <li>category (optional): a string representing the category to filter by.</li>
    </ul>
    <br>
    <b>Response:</b>
    <br>
    The response is a list of article objects represented in JSON format.
    If there are no articles found, <b>a 204 No Content</b> status code is returned.
    <br><br>
    <li><b>GET /client/articles/pages</b></li>
        This endpoint retrieves a paginated list of articles. You can optionally filter the articles by category by providing a category query
        parameter. You can also specify the <b>page</b> and <b>size</b> query parameters
        to control the pagination.
    <br><br>
    <b>Query Parameters:</b>
    <br>
    <ul>
        <li>page (required): an integer representing the page number (starting from 0).</li>
        <li>size (required): an integer representing the page size.</li>
        <li>category (optional): a string representing the category to filter by.</li>
    </ul>
    <br>
    <b>Response:</b>
    <br>
    The response is a paginated list of article objects represented in JSON format.
    If there are no articles found, <b>a 204 No Content</b> status code is returned.
    If there's an error with the query parameters, <b>a 400 Bad Request</b> status code is returned.
    <br><br>
    <li><b>POST /client/articles/fe</b></li>
    This endpoint is used to add an article from the editorial microservice to the client microservice.It can only be accessed by the caller with X-Caller header set to "ARTICLE_FROM_EDITORIAL". So it <b>SHOULD NOT BE DIRECTLY ACCESSED!!!</b>
    <br><br>
    <b>Request Headers:</b>
    <ul>
        <li>X-Caller (required): This header must be set to "ARTICLE_FROM_EDITORIAL" to ensure that the request is coming from the editorial microservice.</li>
    </ul>
    <br>
    <b>Request Body:</b>
    <br>
    The request body should be a JSON object representing the ArticleCorrectToClientDto. It contains the necessary information about the article, such as the title, content, date of correction, etc.
    <br><br>
    <b>Response:</b>
    <br>
    If the article is successfully saved in the client microservice, the status code will be 200 (OK), and the body will contain the message "Successful moved". If the request is rejected or unsuccessful in the client microservice, the status code will be 400 (Bad Request), and the body will contain the message "Unsuccessful transfer process in the client microservice".
    <br><br>  
</ol>

### Users:
<ol>
    <li><b>GET /client/actions/get/user/info (/editorial/actions/get/user/info)</b></li>
    This endpoint retrieves the user information for the logged-in user. It checks if the user is authenticated and authorized to access their own information.
    <br><br>
    <b>Query Parameters:</b>
    <br>
    <b>Response:</b>
    <br>
    If the user is not authenticated, a 401 Unauthorized status code is returned. In contrast, if the request is successful and the user information is retrieved, a 200 OK status code is returned along with the UserDto object representing the user's information.
    <br><br>
    <li><b>GET /client/actions/get/users (/editorial/actions/get/users)</b></li>
    This endpoint retrieves a list of users from the database based on optional query parameters. Only users with ADMIN privileges can get a list of users.
    <br><br>
    <b>Query Parameters:</b>
    <br>
    <ul>
    <li>pageable (optional): a pageable object specifying the page size (10 by default), page number (0 by default), sorting field, and sorting direction for pagination.</li>
    <li>role (optional): a string representing the role of the users to filter by.</li>
    <li>attributeName (optional): a string representing the column in the database to filter the users by.</li>
    <li>attributeValue (optional): a string representing the value to match in the specified field.</li>
    </ul>
    <br>
    <b>Response:</b>
    <br>
    The response includes a list of UserDto objects and appropriate status codes. Additionally, the response headers include the X-Total-Count field, representing the total number of records in the database that match the specified criteria. If the user is not authenticated, <b>a 401 Unauthorized</b> status code is returned. If the request is successful and there are users matching the criteria, <b>a 200 OK</b> status code is returned along with the list of UserDto objects. If there are no users matching the criteria, <b>a 204 No Content</b> status code is returned.
    <br><br>
    <li><b>DELETE /client/actions/delete (/editorial/actions/delete)</b></li>
    This endpoint deletes a user from the database. Only users with ADMIN privileges or the owner of the account can delete a user.
    <br><br>
    <b>Query Parameters:</b>
    <br>
    <ul>
    <li>id (required): an integer representing the ID of the user to be deleted.</li>
    </ul>
    <br>
    <b>Response:</b>
    <br>
    The response is a status code indicating the outcome of the deletion operation. If the deletion was successful, <b>a 200 OK</b> status code is returned. If the user is not authorized to perform the deletion operation, <b>a 403 Forbidden</b> status code is returned. If there's an error with the query parameters, <b>a 400 Bad Request</b> status code is returned. If the server encountered an error while performing the deletion operation, <b>a 500 Internal Server Error</b> status code is returned.
    <br><br>
    <li><b>DELETE /client/actions/delete/fe (/editorial/actions/delete/fc)</b></li>
    This endpoint deletes a user from the database. It can only be accessed by the caller with X-Caller header set to "DELETE_FROM_EDITORIAL". So it <b>SHOULD NOT BE DIRECTLY ACCESSED!!!</b>
    <br><br>
    <b>Query Parameters:</b>
    <br>
    <ul>
        <li>id (required): an integer representing the ID of the user to be deleted.</li>
    </ul>
    <br>
    <b>Headers:</b>
    <br>
    <ul>
        <li>X-Caller (required): a string representing the caller's identity. Must be set to "DELETE_FROM_EDITORIAL" ("DELETE_FROM_CLIENT").</li>
    </ul>
    <br>
    <b>Response:</b>
    <br>
    The response is a status code indicating the outcome of the deletion operation. If the deletion was successful, <b>a 200 OK</b> status code is returned. If there's an error with the query parameters or the caller is not authorized to perform the deletion operation, <b>a 400 Bad Request</b> status code is returned. If the server encountered an error while performing the deletion operation, <b>a 500 Internal Server Error</b> status code is returned.
    <br><br>
    <li><b>PUT /client/actions/edit (/editorial/actions/edit)</b></li>
    This endpoint edits an existing user in the database. Only users with ADMIN privileges or the owner of the account can edit a user.
    <br><br>
    <b>Query Parameters:</b>
    <br>
    <ul>
        <li>id (required): an integer representing the ID of the user to be edited.</li>
    </ul>
    <br>
    <b>Request Body:</b>
    <br>
    The request body must contain a JSON object representing the updated user information. The object should contain the following fields:
    <ul>
        <li>username (required): a string representing the user's username</li>
        <li>name (required): a string representing the user's first name.</li>
        <li>surname (required): a string representing the user's last name.</li>
        <li>authorityName (required): a string representing the user's role. Can be either: ADMIN, USER, JOURNALIST, CORRECTOR, REDACTOR.</li>
        <li>passwordToConfirm (optional): a string representing user's password. It is not required ONLY if user has an admin role.</li>
        <li>passwordToChange (optional): a string representing password to change into (new password).</li>
    </ul>
        <br>
        <b>Response:</b>
        <br>
        The response is a status code indicating the outcome of the update operation. If the update was successful, <b>a 200 OK</b> status code is returned. If the user is not authorized to perform the update operation, <b>a 403 Forbidden</b> status code is returned. If there's an error with the query parameters or the request body. <b>User cannot change his own role!</b>. Users with account created outside of application can only edit their username, name and surname.
    <br><br>
    <li><b>DELETE /client/actions/edit/fe (/editorial/actions/edit/fc)</b></li>
    This endpoint edits a user from the database. It can only be accessed by the caller with X-Caller header set to "EDIT_FROM_EDITORIAL". So it <b>SHOULD NOT BE DIRECTLY ACCESSED!!!</b>
    <br><br>
    <b>Query Parameters:</b>
    <br>
    <ul>
        <li>id (required): an integer representing the ID of the user to be edited.</li>
        <li>loggedId (required): an integer representing the ID of the logged user.</li>
    </ul>
    <br>
    <b>Headers:</b>
    <br>
    <ul>
        <li>X-Caller (required): a string representing the caller's identity. Must be set to "EDIT_FROM_EDITORIAL" ("EDIT_FROM_CLIENT").</li>
    </ul>
    <br>
    <b>Response:</b>
    <br>
    The response is a status code indicating the outcome of the edit operation. If the edit was successful, <b>a 200 OK</b> status code is returned. If there's an error with the query parameters or the caller is not authorized to perform the edit operation, <b>a 400 Bad Request</b> status code is returned. If the server encountered an error while performing the edit operation, <b>a 500 Internal Server Error</b> status code is returned.
</ol>

### Editorial:
<ol>
    <li><b>GET /editorial/correct</b></li>
    This endpoint retrieves a list of article corrects.
    <br><br>
    <b>Query Parameters:</b>
    <br>
    <ul>
        <li><b>pageable</b> (optional): A pageable object specifying the page size (10 by default), page number (0 by default), sorting field, and sorting direction for pagination.</li>
        <li><b>title</b> (optional): A string representing the article's title. If provided, only articles matching the title will be included in the response.</li>
        <li><b>isCorrected</b> (optional): A boolean value indicating whether the articles should be filtered based on their correction status.</li>
    </ul>
    <br>
    <b>Response:</b>
    <br>
    The response is a JSON object with the following properties:
    <ul>
        <li><b>status</b>: An integer representing the HTTP status code of the response.</li>
        <li><b>body</b>: A list of ArticleCorrectDto objects containing the article corrects.</li>
    </ul>
    If the articles are retrieved successfully, the status code will be 200 (OK), and the body will contain the list of ArticleCorrectDto objects. If there is an error while processing the request, the status code will be 400 (Bad Request), and an empty response will be returned. If the user is not authenticated, the status code will be 401 (Unauthorized), and an empty response will be returned.
    <br><br>
    <li><b>PUT /editorial/correct</b></li>
    This endpoint allows users to update an existing article correct.
    <br><br>
    <b>Request:</b>
    <br>
    The request body should contain a JSON object of type ArticleDraftDto with the following properties:
    <ul>
        <li><b>id</b> (required): A unique identifier for the article draft.</li>
        <li><b>title</b> (required): A string representing the updated title of the article draft.</li>
        <li><b>content</b> (required): A string representing the content of the article draft.</li>
        <li><b>isCorrected</b> (required): A boolean indicating whether the article has been corrected.</li>
    </ul>
    <br>
    <b>Response:</b>
    <br>
    The response is a JSON object with the following properties:
    <ul>
        <li><b>status</b>: An integer representing the HTTP status code of the response.</li>
        <li><b>body</b>: A string containing the response message.</li>
    </ul>
    If the article is updated successfully, the status code will be 200 (OK), and the body will contain the message "Successful update". If the request body is invalid or the ID is not provided, the status code will be 400 (Bad Request), and the body will contain the error message "Provide a body, including id!". If the user is not authorized or the username does not exist in the database, the status code will be 401 (Unauthorized), and the body will contain the error message "Username of requesting user does not exist in db!". If the article with the provided ID is not found, the status code will be 400 (Bad Request), and the body will contain the error message "Correct has not been found!".".
    <br><br>
    <li><b>DELETE /editorial/correct/reject</b></li>
    This endpoint deletes an article correct and performs a move operation.
    <br><br>
    <b>Query Parameters:</b>
    <ul>
        <li><b>id</b> (required): A long representing the ID of the article correct to delete and move.</li>
    </ul>
    <br>
    <b>Response:</b>
    <br>
    The response is a JSON object with the following properties:
    <ul>
        <li><b>status</b>: An integer representing the HTTP status code of the response.</li>
        <li><b>body</b>: A string containing the response message.</li>
    </ul>
    If the article correction is successfully deleted and the article is moved to the article draft, the status code will be 200 (OK), and the body will contain the message "Successful moved". If the ID is not provided or the article correction is not found, the status code will be 400 (Bad Request), and the body will contain the error message "Correct has not been found!". If the user is not authorized or the username does not exist in the database, the status code will be 401 (Unauthorized), and the body will contain the error message "Username of requesting user does not exist in db!".
    <br><br>
    <li><b>DELETE /editorial/correct/accept</b></li>
    This endpoint deletes an article correct from the editorial system and moves it to the client service.
    <br><br>
    <b>Query Parameters:</b>
    <ul>
        <li><b>id</b> (required): A long representing the ID of the article correct to delete and move.</li>
        <li><b>category</b> (required): The category of the article.</li>
    </ul>
    <br>
    <b>Response:</b>
    <br>
    The response is a JSON object with the following properties:
    <ul>
        <li><b>status</b>: An integer representing the HTTP status code of the response.</li>
        <li><b>body</b>: A string containing the response message.</li>
    </ul>
    If the article correction is successfully deleted and transferred to the client service, the status code will be 200 (OK), and the body will contain the message "Successful moved". If the user is not authorized or the username does not exist in the database, the status code will be 401 (Unauthorized), and the body will contain the message "Username of requesting user does not exist in the database". If the article correction is not found, the status code will be 400 (Bad Request), and the body will contain the message "Correct has not been found". If the request is rejected or unsuccessful in the client microservice, the status code will be 400 (Bad Request), and the body will contain the message "Unsuccessful transfer process in the client microservice".
    <br><br>    
    <li><b>POST /editorial/proposal </b></li>
    This endpoint allows users to add a new article proposal to the system.
    <br><br>
    <b>Request:</b>
    <br>
    The request body should contain a JSON object with the following properties:
    <ul>
        <li><b>title</b> (required): A string representing the title of the article. It must be between 3 and 200 characters long.</li>
    </ul>
    <br>
    <b>Response:</b>
    <br>
    The response is a JSON object with the following properties:
    <ul>
        <li><b>status</b>: An integer representing the HTTP status code of the response.</li>
        <li><b>body</b>: A string containing the response message.</li>
    </ul>
    If the article proposal is added successfully, the status code will be 200 (OK), and the body will be "Successfully added an article!". If the user is not authorized or the username does not exist in the database, the status code will be 401 (Unauthorized), and the body will be "Username of requesting user does not exist in the database!".
    <br><br>
    <li><b>PUT /editorial/proposal </b></li>
    This endpoint allows users to update an existing article proposal in the system.
    <br><br>
    <b>Request:</b>
    <br>
    The request body should contain a JSON object with the following properties:
    <ul>
        <li><b>id</b> (required): A unique identifier for the article proposal.</li>
        <li><b>title</b> (required): A string representing the title of the article. It must be between 3 and 200 characters long.</li>
        <li><b>acceptance</b> (required): An enumeration representing the acceptance status of the article proposal.</li>
    </ul>
    <br>
    <b>Response:</b>
    <br>
    The response is a JSON object with the following properties:
    <ul>
        <li><b>status</b>: An integer representing the HTTP status code of the response.</li>
        <li><b>body</b>: A string containing the response message.</li>
    </ul>
    If the article proposal is updated successfully, the status code will be 200 (OK), and the body will contain the response message returned by the articleProposalService. If the user is not authorized or the username does not exist in the database, the status code will be 401 (Unauthorized), and the body will be "Username of requesting user does not exist in the database!". If the request body is missing required parameters, the status code will be 400 (Bad Request), and the body will be "Provide a body,
    including id and status!". If an error occurs while processing the request, the status code will be 500 (Internal Server Error). <b> Redactor is allowed to edit every proposal, journalist only the one provided by himself. No matter which acceptance level is going to be provided by journalist, it is going to end on PENDING by default. </b>
    <br><br>
    <li><b>GET /editorial/proposal </b></li>
    This endpoint retrieves a list of article proposals.
    <br><br>
    <b>Query Parameters:</b>
    <br>
    <ul>
        <li><b>page</b> (optional): An integer representing the page number of the results to retrieve (starting from 0). If not provided it is equal to 0</li>
        <li><b>size</b> (optional): An integer representing the number of results per page. If not provided it is equal to 20</li>
        <li><b>title</b> (optional): A string representing the article's title</li>
        <li><b>sort</b> (optional): (In format sort=name,direction), where "name" is the Java attribute name and "direction" is either asc or desc</li>
        <li><b>acceptance</b> (optional): State of the article. Can be either PENDING or DECLINED, ACCEPTED</li>
    </ul>
    <br>
    <b>Response:</b>
    <br>
    The response is a JSON object with the following properties:
    <ul>
        <li><b>status</b>: An integer representing the HTTP status code of the response.</li>
        <li><b>body</b>: A list of ArticleProposalDto objects containing the article proposals.</li>
    </ul>
    If the article proposals are retrieved successfully, the status code will be 200 (OK), and the body will contain the list of ArticleProposalDto objects. If the user is not authorized or the username does not exist in the database, the status code will be 401 (Unauthorized), and the body will be an empty list. If an error occurs while processing the request, the status code will be 400 (Bad Request), and an empty response will be returned. <b>Redactor receives every proposal, journalist only related to him. Results are ordered by date in ascending order.</b>
    <br><br>
    <li><b>POST /editorial/draft</b></li>
    This endpoint allows users to initialize a new article draft.
    <br><br>
    <b>Request:</b>
    <br>
    The request body should contain a JSON object of type ArticleDraftDto with the following properties:
    <ul>
        <li><b>title</b> (required): A string representing the title of the article draft.</li>
        <li><b>content</b> (required): A string representing the content of the article draft.</li>
    </ul>
    <br>
    <b>Response:</b>
    <br>
    The response is a JSON object with the following properties:
    <ul>
        <li><b>status</b>: An integer representing the HTTP status code of the response.</li>
        <li><b>body</b>: A string containing the response message.</li>
    </ul>
    If the article draft is initialized successfully, the status code will be 200 (OK), and the body will contain the response message returned by the articleDraftService. If the user is not authorized or the username does not exist in the database, the status code will be 401 (Unauthorized), and the body will be "Username of requesting user does not exist in db!".
    <br><br>
    <li><b>PUT /editorial/draft</b></li>
    This endpoint allows users to update an existing article draft.
    <br><br>
    <b>Request:</b>
    <br>
    The request body should contain a JSON object of type ArticleDraftDto with the following properties:
    <ul>
        <li><b>id</b> (required): A unique identifier for the article draft.</li>
        <li><b>title</b> (required): A string representing the updated title of the article draft.</li>
        <li><b>content</b> (required): A string representing the content of the article draft.</li>
    </ul>
    <br>
    <b>Response:</b>
    <br>
    The response is a JSON object with the following properties:
    <ul>
        <li><b>status</b>: An integer representing the HTTP status code of the response.</li>
        <li><b>body</b>: A string containing the response message.</li>
    </ul>
    If the article draft is updated successfully, the status code will be 200 (OK), and the body will contain the response message returned by the articleDraftService. If the request body is missing the required parameters, the status code will be 400 (Bad Request), and the body will be "Provide a body, including id!". If the user is not authorized or the username does not exist in the database, the status code will be 401 (Unauthorized), and the body will be "Username of requesting user does not exist in db!".
    <br><br>
    <li><b>GET /editorial/draft</b></li>
    This endpoint retrieves a list of article drafts.
    <br><br>
    <b>Query Parameters:</b>
    <br>
    <ul>
        <li><b>page</b> (optional): An integer representing the page number of the results to retrieve (starting from 0). If not provided it is equal to 0</li>
        <li><b>size</b> (optional): An integer representing the number of results per page. If not provided it is equal to 20</li>
        <li><b>title</b> (optional): A string representing the article's title</li>
        <li><b>sort</b> (optional): (In format sort=name,direction), where "name" is the Java attribute name and "direction" is either asc or desc</li>
    </ul>
    <br>
    <b>Response:</b>
    <br>
    The response is a JSON object with the following properties:
    <ul>
        <li><b>status</b>: An integer representing the HTTP status code of the response.</li>
        <li><b>body</b>: A list of ArticleDraftDto objects containing the article drafts.</li>
    </ul>
    If the article drafts are retrieved successfully, the status code will be 200 (OK), and the body will contain the list of ArticleDraftDto objects returned by the articleDraftService. If there is an error during retrieval, the status code will be 400 (Bad Request), and the body will be empty. If the user is not authorized or the username does not exist in the database, the status code will be 401 (Unauthorized), and the body will be an empty list.
    <br><br>
    <li><b>DELETE /editorial/draft</b></li>
    This endpoint deletes an article draft and performs a move operation.
    <br><br>
    <b>Query Parameters:</b>
    <ul>
        <li><b>id</b> (required): A long integer representing the ID of the article draft to delete and move.</li>
    </ul>
    <br>
    <b>Response:</b>
    <br>
    The response is a JSON object with the following properties:
    <ul>
        <li><b>status</b>: An integer representing the HTTP status code of the response.</li>
        <li><b>body</b>: A string containing the response message.</li>
    </ul>
    If the article draft is deleted and moved successfully, the status code will be 200 (OK), and the body will contain the response message returned by the articleDraftService. If the user is not authorized or the username does not exist in the database, the status code will be 401 (Unauthorized), and the body will be "Username of requesting user does not exist in db!".
</ol>