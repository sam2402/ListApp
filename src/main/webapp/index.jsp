<%@ page import="uk.ac.ucl.model.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="uk.ac.ucl.model.ListItem" %>
<%@ page import="uk.ac.ucl.model.ListLinkItem" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<html lang="en">
<head>
  <jsp:include page="/meta.jsp"/>
  <title>List App</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
</head>
<body>
<section>
  <div class="topLevel" id="lists">
    <h1>Lists</h1>

    <div class="search">
      <form method="POST" action="/searchLists.html">
        <% String searchListsText = (String) request.getSession().getAttribute("searchListsText");
        searchListsText = (searchListsText == null) ? "" : searchListsText;
        %>
        <input style="display: inline" class="searchBox" type="text" name="searchString" value="<%=searchListsText%>" placeholder="Search list names...">
        <input style="display: inline" type="submit" value="Search">
      </form>
      <% if (request.getSession().getAttribute("searchListsText")!=null) { %>
      <form method="GET" action="/searchLists.html">
        <input type="submit" value="Cancel Search">
      </form>
      <%}%>
      <hr>
    </div>

    <div>
      <div id="ListThumbnails">
        <% ArrayList<List> lists = (ArrayList<List>) request.getAttribute("lists");
        for (List list: lists) {
          String name = list.getName();
          String id = list.getId().toString();
        %>
          <h3 class="listThumbnailTitle "><%=name%></h3>
          <form class="editList" method="POST" action="/deleteList.html">
            <input type="hidden" value="<%=id%>" name="listId">
            <input type="submit" value="Delete">
          </form>
          <form class="editList" method="POST" action="/renameList.html">
            <input type="text" placeholder="Change this list's name" name="newName">
            <input type="hidden" value="<%=id%>" name="listId">
            <input type="submit" value="Rename">
          </form>

          <form method="GET" action="/viewList.html">
            <input type="hidden" value="<%=id%>" name="listId">
            <input type="submit" value="View List">
          </form>
          <hr>
        <%}%>
      </div>

      <form method="POST" action="/addList.html">
        <input id="listNameTextBox" type="text" name="listNameString" placeholder="New list name..."/>
        <input type="submit" value="Add"/>
      </form>
    </div>
  </div>

  <div class = "topLevel" id="list">
    <% List list = (List) request.getSession().getAttribute("currentList");
      if (list!=null) {
      String listId = list.getId().toString();
    %>

      <h1><%=list.getName()%></h1>

    <form method="POST" action="/searchList.html">
      <% String searchListText = (String) request.getSession().getAttribute("searchListText");
        searchListText = (searchListText == null) ? "" : searchListText;
      %>
      <input class="searchBox" type="text" name="searchString" value="<%=searchListText%>" placeholder="Search items in <%=list.getName()%>...">
      <input type="submit" value="Search">
    </form>
    <% if (request.getSession().getAttribute("searchListText")!=null) { %>
    <form method="GET" action="/searchList.html">
      <input type="submit" value="Cancel Search">
    </form>
    <%}%>
    <hr>


      <ul>
        <% for (ListItem listItem: list.getChildren()) {
          String itemId = listItem.getId().toString();
        %>
        <li class="listItem">
          <%=listItem.getHTMLRepresentation()%>
          <form method="POST" action="/deleteItem.html">
            <input type="hidden" value="<%=listId%>" name="listId">
            <input type="hidden" value="<%=itemId%>" name="itemId">
            <input type="submit" value="Delete item">
          </form>
          <% if (!(listItem instanceof ListLinkItem)) { %>
          <form method="POST" action="/editItem.html">
            <input type="hidden" value="<%=itemId%>" name="itemId">
            <label for="editItem">Edit Item</label>
              <input id="editItem" type="text" placeholder="Change this list's name" value="<%=listItem.getText()%>" name="newText">
            <input type="submit" value="Edit item">
          </form>
          <%}%>
          <hr>
        </li>
        <%}%>
      </ul>


      <form id="itemTypeForm" method="POST" action="/addItem.html">
        <label for="itemType">
            <input type="text" placeholder="Add item..." name="text">
        </label>
        <select name="itemType" id="itemType">
          <option value="text">Text</option>
          <option value="imageURL">Image URL</option>
          <option value="URL">URL</option>
        </select>
        <input type="hidden" value="<%=listId%>" name="listId">
        <input type="submit" value="Add item">
      </form>

      <% if (((ArrayList<List>) request.getAttribute("lists")).size() > 1) { %>
      <form method="POST" action="/addListLink.html">
        <label>Add link to list</label>
        <select name="chosenListId">
          <% for (List listOption: ((ArrayList<List>) request.getAttribute("lists"))) {
            String listOptionName = listOption.getName();
            String listOptionId = String.valueOf(listOption.getId());
            if (!listOptionId.equals(listId)) {
          %>
          <option value="<%=listOptionId%>"><%=listOptionName%></option>
          <%}
          }%>
        </select>
        <input type="hidden" value="<%=listId%>" name="listId">
        <input type="submit" value="Add item">
      </form>
    <%}%>

    <%}%>
  </div>
</section>

</body>
</html>