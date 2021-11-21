# List App
#### Program Features
- Store one or more lists of items. Each list has a name, and items are one of text, an URL, an image URL or a link to another list
- Item lists can be created, deleted, or renamed
- Items in a list can be added, removed, or edited
- An item list can be searched for list items, and lists can be located by searching for
the name of the list.
- A list is automatically saved to a csv so that the user does not have to explicitly load
or save to a file. When a list or items are modified, the change(s) are immediately written to the file.

#### How to run
Run the following commands from the rot of this project
`mvn clean package`
`mvn exec:exec`
Then navigate to
http://localhost:8080

#### UML Diagram
![UML Diagram](https://user-images.githubusercontent.com/10476170/142769685-7867be06-9e7b-436d-a34f-1a44309d0902.png)
