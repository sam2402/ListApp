package uk.ac.ucl.model;

import java.util.ArrayList;
import java.util.UUID;

// This is what the controller interacts with
// It manages the state of the program in memory via listStore, and on file via fileHandler
public class Model {

    private final ListStore listStore;
    private final FileHandler fileHandler;
    private final SearchManager<ListStore, List> listStoreSearchManager; // pParent is a ListStore and children are Lists
    private final SearchManager<List, ListItem> listSearchManager; // Parent is a List and children is ListItems

    public Model() {
        fileHandler = new FileHandler();
        listStore = new ListStore(fileHandler.getLists()); // Gets all lists that are stored on file from the fileHandler
        listSearchManager = new SearchManager<>();
        listStoreSearchManager = new SearchManager<>();
        listStoreSearchManager.setParentCollection(listStore);
    }

    // Returns the lists that are viewable
    // This is all lists if the user is not searching and the lists that match th search query if they are
    public ArrayList<List> getLists() {
        return listStoreSearchManager.getViewableChildren();
    }

    public void deleteList(UUID id) {
        // Both the deleteList methods below handle the case that there is a list link to the deleted list in other lists
        // The list links to the deleted list are removed from the lists in which they are found
        fileHandler.deleteList(id);
        listStore.deleteList(id);
    }

    // Creates a new list with the given name and returns its id
    public UUID addList(String listName) {
        // User data can't contain commas in order to preserve the structure of the csv files
        UUID newId = listStore.addList(cleanUserInput(listName));
        List newList = listStore.getList(newId);
        fileHandler.addList(newList);
        resetViewableLists();
        return newId;
    }

    public void renameList(UUID id, String newName) {
        // The deleteList methods in listStore renames list links in other lists
        // where the id of the list is equal to the id of the deleted list
        String validNewName = cleanUserInput(newName);
        listStore.renameList(id, validNewName);
        fileHandler.renameList(id, validNewName);
    }

    public void searchLists(String searchString) {
        // This causes the return value of listStoreSearcher.getViewableChildren() to change which updates
        // the lists that are returned in the getLists() method
        listStoreSearchManager.search(searchString);
    }

    public void cancelListStoreSearch() {
        resetViewableLists();
    }

    public void addItem(UUID listId, String text, String itemTypeText) {
        ListItem newItem = listStore.addItem(listId, cleanUserInput(text), itemTypeText);
        fileHandler.addItem(listId, newItem);
    }

    public void addListLinkToList(UUID listId, UUID linkingListId) {
        ListItem newItem = listStore.addListLinkToList(listId, linkingListId);
        fileHandler.addItem(listId, newItem);
    }

    public List getList(UUID id) {
        return listStore.getList(id);
    }

    public void deleteItem(UUID listId, UUID itemId) {
        listStore.deleteItem(listId, itemId);
        fileHandler.deleteItem(listId, itemId);
    }

    public List searchList(UUID listId, String searchText) {
        List list = listStore.getList(listId);
        listSearchManager.setParentCollection(list);
        listSearchManager.search(searchText);
        ArrayList<ListItem> matchingItems = listSearchManager.getViewableChildren();
        List viewableList = new List(list.getName()); // Make new list with name of the list being searched
        viewableList.setItems(matchingItems); // Set the items of this new list to be the items that match the search query
        return viewableList;
    }

    public List cancelListSearch(UUID listId) {
        listSearchManager.cancelSearch();
        return getList(listId);
    }

    public void editItem(UUID listId, UUID itemId, String newItemText) {
        String validNewItemText = cleanUserInput(newItemText);
        listStore.editItem(listId, itemId, validNewItemText);
        fileHandler.editItem(listId, itemId, validNewItemText);
    }

    // Makes all lists viewable
    private void resetViewableLists() {
        listStoreSearchManager.cancelSearch();
    }

    private String cleanUserInput(String userInput) {
        // User data can't contain , or " in order to preserve the structure of the csv files
        return userInput.replace(",", "").replace("\"", "");
    }

}
