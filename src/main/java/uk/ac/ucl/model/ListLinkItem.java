package uk.ac.ucl.model;

import java.util.UUID;

public class ListLinkItem extends ListItem {

    private final UUID linkingListId;

    // A list link only needs to store the name and id of the linked list.
    // But depending on where an object of this type is instantiated,
    // it may be easier to call it with the list to be linked to, or its id and name hence the two initializers
    public ListLinkItem(UUID linkingListId, String linkingListName) {
        super(linkingListName);
        this.linkingListId = linkingListId;
    }

    public ListLinkItem(List list) {
        super(list.getName());
        linkingListId = list.getId();
    }

    public UUID getLinkedListId() {
        return linkingListId;
    }

    @Override
    public String getHTMLRepresentation() {
        String id = linkingListId.toString();
        return "<form method=\"GET\" action=\"/viewList.html\"> <input type=\"hidden\" value=\"" + id + "\" name=\"listId\"> <input type=\"submit\" value=\"" + super.getText() + "\"> </form>";
    }

    @Override
    public String getCSVFileRepresentation() {
        return "listLink," + linkingListId + "," + getId();
    }

}
