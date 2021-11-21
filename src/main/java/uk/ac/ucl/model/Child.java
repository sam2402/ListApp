package uk.ac.ucl.model;

import java.util.UUID;

// Classes that implement Child are capable of acting like an item in
// the collection of items a Class which implements Parent has
public interface Child {
    UUID getId();
}
