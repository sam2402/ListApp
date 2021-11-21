package uk.ac.ucl.model;

public class URLItem extends ListItem {

    public URLItem(String text) {
        super(text);
    }

    @Override
    public String getHTMLRepresentation() {
        return "<a href=\"" + text + "\" target=\"_blank\">" + text + "</a>";
    }

    @Override
    public String getCSVFileRepresentation() {
        return "URL," + text + "," + getId();
    }

}
