package uk.ac.ucl.model;

import java.util.ArrayList;
import java.util.UUID;

public class List implements Parent<ListItem>, Child { // List is both the parent of ListItems the child of ListStore
    private String name;
    private UUID id;
    private ArrayList<ListItem> items;

    public List(String name) {
        this.name = name;
        this.id = UUID.randomUUID();
        items = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setItems(ArrayList<ListItem> items) {
        this.items = items;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ArrayList<ListItem> getChildren() {
        return items;
    }

    // Returns the ids of the items that match the search query in newText
    // Needed to implement Parent
    @Override
    public ArrayList<UUID> getMatchingChildrenIds(String searchText) {
        ArrayList<UUID> matchingIds = new ArrayList<>();
        for (ListItem listItem : getChildren()) {
            if (listItem.getText().toLowerCase().contains(searchText.toLowerCase())) {
                matchingIds.add(listItem.getId());
            }
        }
        return matchingIds;
    }

    public void setName(String newName) {
        name = newName;
    }

    public ListItem addItem(String text, String itemTypeText) {
        ListItem item = switch (itemTypeText) {
            case "text" -> new TextItem(text);
            case "imageURL" -> new ImageURLItem(text);
            case "URL" -> new URLItem(text);
            default -> throw new RuntimeException("No such item type " + itemTypeText);
        };
        items.add(item);
        return item;
    }

    public void deleteItem(UUID itemId) {
        items.remove(getChildIndex(itemId));
    }

    // Gets the index in items of the item with the given index
    public int getChildIndex(UUID itemId) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getId().equals(itemId)) {
                return i;
            }
        }
        throw new RuntimeException("No such item with id: " + itemId);
    }

    public void editItem(UUID itemId, String newText) {
        ListItem item = items.get(getChildIndex(itemId));
        item.setText(newText);
    }

    public ListItem addLinkList(List linkingList) {
        ListLinkItem listLink = new ListLinkItem(linkingList);
        items.add(listLink);
        return listLink;
    }

    public void removeListLink(UUID id) {
        // removeIf is used instead of a normal for loop with a condition to avoid a ConcurrentModificationException
        items.removeIf(item -> (item instanceof ListLinkItem) && (((ListLinkItem) item).getLinkedListId().equals(id)));
    }

    // When another list is renamed, the buttons that display the name of that list in list links need their text to change
    // This needs to be reflected in the model so a new list link is made with the old id, but new name
    public void renameLinkedListItems(UUID renamedListId, String newName) {
        for (int i = 0; i < items.size(); i++) {
            ListItem item = items.get(i);
            if (item instanceof ListLinkItem && ((ListLinkItem) item).getLinkedListId().equals(renamedListId)) {
                ListItem updatedListLink = new ListLinkItem(((ListLinkItem) item).getLinkedListId(), newName);
                items.set(i, updatedListLink);
            }
        }
    }
}
