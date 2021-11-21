package uk.ac.ucl.model;

import java.util.ArrayList;
import java.util.UUID;

// Classes that implement Parent are capable of holding collect of items of type C
public interface Parent<C extends Child> { // C is the class which classes of this interface are parent of
    int getChildIndex(UUID id);

    ArrayList<C> getChildren();

    ArrayList<UUID> getMatchingChildrenIds(String searchText);
}
