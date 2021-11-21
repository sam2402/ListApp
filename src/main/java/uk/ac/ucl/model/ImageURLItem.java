package uk.ac.ucl.model;

public class ImageURLItem extends ListItem {

    public ImageURLItem(String text) {
        super(text);
    }

    @Override
    public String getHTMLRepresentation() {
        return "<img src=\"" + text + "\">";
    }

    @Override
    public String getCSVFileRepresentation() {
        return "imageURL," + text + "," + getId();
    }
}
