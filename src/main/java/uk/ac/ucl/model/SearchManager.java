package uk.ac.ucl.model;

import java.util.ArrayList;
import java.util.UUID;

// This class is used by model to search the lists and list items in a given list
// The parent holds the references to its children that can be searched
public class SearchManager<P extends Parent<C>, C extends Child> {
    private P parentCollection;
    private ArrayList<UUID> viewableIds; // The ids of children that match the search query. It is all children of the parent if there is no current search

    public SearchManager() {
        viewableIds = new ArrayList<>();
    }

    public void setParentCollection(P parentCollection) {
        this.parentCollection = parentCollection;
    }

    // Return the children that match the current search. If there is no current search then all children are returned.
    public ArrayList<C> getViewableChildren() {
        ArrayList<C> viewableCs = new ArrayList<>();
        for (C child : parentCollection.getChildren()) {
            if (viewableIds.contains(child.getId())) {
                viewableCs.add(child);
            }
        }
        return viewableCs;
    }

    public void search(String searchText) {
        viewableIds = parentCollection.getMatchingChildrenIds(searchText);
    }

    // Set all children of the parent as viewable
    public void cancelSearch() {
        viewableIds.clear();
        for (C child : parentCollection.getChildren()) {
            viewableIds.add(child.getId());
        }
    }

}
