# Information-service

## Fronted configuration for local developement

First you need to install [Node.js](https://nodejs.org/en)

Once Node is ready you can now install project dependencies and run the server. Open website directory in terminal and then follow these steps:  
1. npm install - needed to install dependencies locally, you need to do this step only when it's your first time running frontend on your machine or if something changed in file website/package.json
2. npm run dev - this command spins up server hosting website, output of the command will have the address and port that you can use access the website

To shut down the server simply use CTRL+C hotkey or close terminal.

## Backend endpoints
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
        <li>page (optional): an integer representing the page number (starting from 0).</li>
        <li>size (optional): an integer representing the page size.</li>
        <li>category (optional): a string representing the category to filter by.</li>
    </ul>
    <br>
    <b>Response:</b>
    <br>
    The response is a paginated list of article objects represented in JSON format.
    If there are no articles found, <b>a 204 No Content</b> status code is returned.
    If there's an error with the query parameters, <b>a 400 Bad Request</b> status code is returned.
    <br><br>
</ol>

### Users:
<ol>
    <li><b>DELETE /client/delete (/editorial/delete)</b></li>
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
    <li><b>DELETE /client/delete/fe (/editorial/delete/fc)</b></li>
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
    <li><b>PUT /client/edit (/editorial/edit)</b></li>
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
        <li>email (required): a string representing the user's email address.</li>
        <li>password (required): a string representing the user's new password.</li>
    </ul>
        <br>
        <b>Response:</b>
        <br>
        The response is a status code indicating the outcome of the update operation. If the update was successful, <b>a 200 OK</b> status code is returned. If the user is not authorized to perform the update operation, <b>a 403 Forbidden</b> status code is returned. If there's an error with the query parameters or the request body
    </ol>