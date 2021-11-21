package uk.ac.ucl.model;

import java.util.ArrayList;
import java.util.UUID;

public class ListStore implements Parent<List> { // Is a parent of List objects

    private final ArrayList<List> lists;

    public ListStore(ArrayList<List> lists) {
        this.lists = lists;
    }

    public ArrayList<List> getChildren() {
        return lists;
    }

    public UUID addList(String listName) {
        List list = new List(listName);
        lists.add(list);
        return list.getId();
    }

    public void deleteList(UUID id) {
        // This for loops deletes list links to the list about to be deleted
        for (List list : lists) {
            list.removeListLink(id);
        }
        lists.remove(getChildIndex(id));
    }

    public void renameList(UUID id, String newName) {
        lists.get(getChildIndex(id)).setName(newName);
        // This for loops renames list links to the list that has been renamed
        for (List list : lists) {
            list.renameLinkedListItems(id, newName);
        }
    }

    // Returns the ids of the lists that match the search query in searchText
    // Needed to implement Parent
    public ArrayList<UUID> getMatchingChildrenIds(String searchString) {
        ArrayList<UUID> matchingIds = new ArrayList<>();
        for (List list : getChildren()) {
            if (list.getName().contains(searchString)) {
                matchingIds.add(list.getId());
            }
        }
        return matchingIds;
    }

    // Gets the index in lists of the list with the given index
    public int getChildIndex(UUID id) {
        for (int i = 0; i < lists.size(); i++) {
            if (lists.get(i).getId().equals(id)) {
                return i;
            }
        }
        throw new RuntimeException("No such list with id: " + id.toString());
    }

    public List getList(UUID id) {
        return lists.get(getChildIndex(id));
    }

    public ListItem addItem(UUID listId, String text, String itemTypeText) {
        return lists.get(getChildIndex(listId)).addItem(text, itemTypeText);
    }

    public void deleteItem(UUID listId, UUID itemId) {
        lists.get(getChildIndex(listId)).deleteItem(itemId);
    }

    public ListItem addListLinkToList(UUID listId, UUID linkingListId) {
        List list = getList(listId);
        List linkingList = getList(linkingListId);
        return list.addLinkList(linkingList);
    }

    public void editItem(UUID listId, UUID itemId, String newItemText) {
        List list = lists.get(getChildIndex(listId));
        list.editItem(itemId, newItemText);
    }
}
