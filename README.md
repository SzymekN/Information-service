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