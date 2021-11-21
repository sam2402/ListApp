package uk.ac.ucl.model;

import java.io.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.UUID;

// This class receives instructions from the model to create, read, update and delete lists and list items on file.
// Files are stored in the data directory and have the following structure:
//
// Each file in ./data is a csv file whose name is the UUID of the list
// The first row of the file is the list name
// Each subsequent row contains an item in the list. Each row has the following structure:
// <item type>,<item data>,<item UUID>
// So for example the file called "f05a6945-f481-4eec-92b4-0f67bd9b7942.csv" is below
//
// list 1
// text,some text,f1fb1b86-812f-4fef-86d5-f022d2bab834
// imageURL,https://miro.medium.com/max/1200/1*mk1-6aYaf_Bes1E3Imhc0A.jpeg,4f4c72f2-b32a-4f7e-92a1-aae4beb1f380
// URL,http://www.youtube.com,52362c76-e479-4730-9b74-010891f9d786
// listLink,90e2830e-fae5-4b9c-9505-7ba2317a7de7,4a7e3a02-e15a-4bd1-a099-e55fd46c0aba
//
// It is called "list 1" and has four items.
// The listLink item's data is the the UUID of the linked list

public class FileHandler {

    private final String dataFolder = "." + File.separator + "data";

    public ArrayList<List> getLists() {
        ArrayList<List> lists = new ArrayList<>();
        File dir = new File(dataFolder);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                lists.add(getListFromFile(child));
            }
            return lists;
        } else {
            // Handle the case where dir is not really a directory.
            throw new RuntimeException("List directory not found");
        }
    }

    public void deleteList(UUID id) {
        File file = new File(dataFolder + File.separator + id + ".csv");
        ArrayList<List> lists = getLists();

        // This for loop goes through each list and removes listLinks to the list being deleted
        for (List list : lists) {
            for (ListItem item : list.getChildren()) {
                if (item instanceof ListLinkItem && ((ListLinkItem) item).getLinkedListId().equals(id)) {
                    deleteItem(list.getId(), item.getId());
                }
            }
        }
        if (!file.delete()) {
            throw new RuntimeException("File deletion error");
        }
    }

    public void addList(List list) {
        createListFile(list);
        try {
            FileWriter myWriter = new FileWriter(dataFolder + File.separator + list.getId() + ".csv");
            myWriter.write(list.getName());
            myWriter.close();
        } catch (IOException e) {
            throw new RuntimeException("Writing to file error");
        }
    }

    public void renameList(UUID id, String newName) {
        try {
            String[] newText = generateRenamedFileText(id, newName);
            writeTextToFile(id, newText);
        } catch (IOException e) {
            throw new RuntimeException("Renaming list on file error");
        }
    }

    public void addItem(UUID listId, ListItem newItem) {
        try {
            String[] splitFileText = getFileText(listId);
            ArrayList<String> variableSizeSplitFileText = new ArrayList<>(Arrays.asList(splitFileText));
            variableSizeSplitFileText.add(newItem.getCSVFileRepresentation());
            String[] updatedFileText = variableSizeSplitFileText.toArray(new String[0]);
            writeTextToFile(listId, updatedFileText);
        } catch (IOException e) {
            throw new RuntimeException("Adding item to list on file error");
        }
    }

    public void deleteItem(UUID listId, UUID itemId) {
        try {
            // If the newText argument passed to fileTextEdit is null, then the item is deleted
            String[] updatedFileText = fileTextEdit(listId, itemId, null);
            writeTextToFile(listId, updatedFileText);
        } catch (IOException e) {
            throw new RuntimeException("Adding item to list on file error");
        }
    }

    public void editItem(UUID listId, UUID itemId, String newText) {
        try {
            String[] updatedFileText = fileTextEdit(listId, itemId, newText);
            writeTextToFile(listId, updatedFileText);
        } catch (IOException e) {
            throw new RuntimeException("Adding item to list on file error");
        }
    }

    private List getListFromFile(File file) {
        try {
            List list = new List(getListName(file));
            list.setId(UUID.fromString(removeExtension(file.getName())));
            ArrayList<ListItem> items = getListItems(file);
            list.setItems(items);
            return list;
        } catch (IOException e) {
            throw new RuntimeException("IO error");
        }
    }

    private String getListName(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        return reader.readLine(); // Returns the first line in the file
    }

    private ArrayList<ListItem> getListItems(File file) {
        try {
            ArrayList<ListItem> items = new ArrayList<>();
            Scanner scanner = new Scanner(file);
            boolean isFirstLine = true; // The first line should be skipped because it contains the list name
            while (scanner.hasNextLine()) {
                if (isFirstLine) {
                    scanner.nextLine();
                    isFirstLine = false;
                    continue;
                }
                String line = scanner.nextLine();
                items.add(getListItem(line));
            }
            return items;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found");
        }
    }

    private ListItem getListItem(String line) {
        String[] splitLine = line.split(",");
        // splitLine[0] is the item type
        // splitLine[1] is the item data
        ListItem item = switch (splitLine[0]) {
            case "text" -> new TextItem(splitLine[1]);
            case "imageURL" -> new ImageURLItem(splitLine[1]);
            case "URL" -> new URLItem(splitLine[1]);
            case "listLink" -> new ListLinkItem(getListFromLinkingListItem(UUID.fromString(splitLine[1])));
            default -> throw new RuntimeException("No such item type " + splitLine[0]);
        };
        item.setId(UUID.fromString(splitLine[2]));
        return item;
    }

    // This method is needed to avoid infinite recursion in the case where two lists both link to each other
    // If the getListFromFile method were instead called by the getListItem method above,
    // then both the lists' items would need to be computed before they could be returned.
    // This is not possible if both lists refer to each other because they both depend of the other being computed before they can be returned
    private List getListFromLinkingListItem(UUID id) { // id is the id of the list getting retrieved
        try {
            File listFile = new File(dataFolder + File.separator + id.toString() + ".csv");
            List list = new List(getListName(listFile));
            list.setId(id);
            return list;
        } catch (IOException e) {
            throw new RuntimeException("Error getting list link from file with id: " + id);
        }
    }

    private void createListFile(List list) {
        File file = new File(dataFolder + File.separator + list.getId() + ".csv");
        try {
            if (!file.createNewFile()) {
                throw new RuntimeException("File creation error");
            }
        } catch (IOException e) {
            throw new RuntimeException("File creation error");
        }
    }

    // This method has been copied from:
    // https://stackoverflow.com/a/990492/4934861
    // It removes a file extension from the file name
    private String removeExtension(String fileName) {
        String separator = System.getProperty("file.separator");
        String filename;
        // Remove the path upto the filename.
        int lastSeparatorIndex = fileName.lastIndexOf(separator);
        if (lastSeparatorIndex == -1) {
            filename = fileName;
        } else {
            filename = fileName.substring(lastSeparatorIndex + 1);
        }
        // Remove the extension.
        int extensionIndex = filename.lastIndexOf(".");
        if (extensionIndex == -1) {
            return filename;
        }
        return filename.substring(0, extensionIndex);
    }

    private String[] getFileText(UUID id) throws IOException {
        String fileName = dataFolder + File.separator + id + ".csv";
        String fileContents = Files.readString(Path.of(fileName));
        return fileContents.split("\n");
    }

    private String[] generateRenamedFileText(UUID id, String newName) throws IOException {
        String[] splitFileContents = getFileText(id);
        splitFileContents[0] = newName;
        return splitFileContents;
    }

    private void writeTextToFile(UUID id, String[] newText) throws IOException {
        String fileName = dataFolder + File.separator + id + ".csv";
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        for (String line : newText) {
            writer.write(line + "\n");
        }
        writer.close();
    }

    // Edits the item data of an item
    // Takes a listId, itemId and new text and returns an array of strings which is the files new text
    // If the newData is null then that item is deleted, this means this method can be used to both edit and delete items
    private String[] fileTextEdit(UUID listId, UUID itemId, String newText) throws IOException {
        String[] splitFileText = getFileText(listId);
        ArrayList<String> variableSizeSplitFileText = new ArrayList<>(Arrays.asList(splitFileText)); // Number of lines in the file may change so an ArrayList is needed
        // For each item in the list, check if its id is that of the item to be edited
        for (int i = 1; i < variableSizeSplitFileText.size(); i++) {
            String line = variableSizeSplitFileText.get(i);
            String[] splitLine = line.split(",");
            if (splitLine[2].equals(itemId.toString())) { // splitLine[2] is the item's id
                if (newText == null) {
                    variableSizeSplitFileText.remove(i); // Delete the item
                } else {
                    // Edit the item's data
                    splitLine[1] = newText;
                    String editedLine = String.join(",", splitLine);
                    variableSizeSplitFileText.set(i, editedLine);
                }
                break;
            }
        }
        return variableSizeSplitFileText.toArray(new String[0]);
    }

}

