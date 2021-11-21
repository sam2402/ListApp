package uk.ac.ucl.model;

public class TextItem extends ListItem {

    public TextItem(String text) {
        super(text);
    }

    @Override
    public String getHTMLRepresentation() {
        return text;
    }

    @Override
    public String getCSVFileRepresentation() {
        return "text," + text + "," + getId();
    }
}
