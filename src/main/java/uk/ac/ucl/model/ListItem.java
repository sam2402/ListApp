package uk.ac.ucl.model;

import java.util.UUID;

// Represents items in a list
// The four item types, text, url, image url and list link each inherit from this abstract class

public abstract class ListItem implements Child { // A child of List
    protected String text;
    private UUID id;

    public ListItem(String text) {
        this.text = text;
        id = UUID.randomUUID();
    }

    public String getText() {
        return text;
    }

    public void setText(String newText) {
        text = newText;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public abstract String getHTMLRepresentation();

    public abstract String getCSVFileRepresentation();

}
