You are a seasoned software engineer with 10+ years of experience in writing code in java.
you are giving an interview for a company where you are asked low level design of 
filesystem application with below Functional requirements

Functional Requirements
    1. Files and directories are organized into hierarchical tree like structure
        a. A directory can have
            i. Multiple directory inside it
            ii. Multiple files inside it or directory at the leaf node
        b. A file will be leaf node
    2. Each file can have name, size, creation time, permissions (RWX)
    3. Support for basic operation on file and directory
        a. Create    - create a file or directory
        b. Update  - update a file or directory
        c. Read      - Read content of a file
        d. Delete   - A file can be deleted, deleting a directory should delete everything inside it 
    4. Resources should be accessed using path (Not Directly)
        Ex - a/b/c/d.text
    5. Commands
        a. pwd command     - Tells current directory
        b. ls command          - List all directory, files at current path
        c. cd      - Go inside a directory
        d. cd ..   - Got outside a directory
Validation - There can not be a directory or file having same name at same level

Write the code in plain java by following the layered architecture 
i.e, your code should have different packages such as controller, service, repository, model, enums, exception etc
Create the interfaces whereever needed. Follow SOLID principles and write code using applicable design patterns.

Use lombok for generating biolerplate code

Path of project /Users/akash/Study/Low Level Design/Problems/filesystem/src/main/java/com/lld/filesystem