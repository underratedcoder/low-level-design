com.lld.filesystem
├── controller
│   └── FileSystemController.java
├── service
│   ├── FileSystemService.java (interface)
│   └── FileSystemServiceImpl.java
├── repository
│   ├── FileSystemRepository.java (interface)
│   └── InMemoryFileSystemRepository.java
├── model
│   ├── FileSystemNode.java (abstract)
│   ├── Directory.java
│   └── File.java
├── enums
│   └── Permission.java
├── exception
│   ├── FileSystemException.java
│   ├── FileAlreadyExistsException.java
│   ├── FileNotFoundException.java
│   └── InvalidOperationException.java
└── util
└── PathUtil.java


Key Components
model
FileSystemNode (abstract): Common attributes (name, creation time, permissions, parent)
Directory: Extends FileSystemNode, contains children (Map<String, FileSystemNode>)
File: Extends FileSystemNode, contains content, size
enums
Permission: Enum for READ, WRITE, EXECUTE
exception
Custom exceptions for various error cases
repository
FileSystemRepository: Interface for CRUD operations on nodes
InMemoryFileSystemRepository: In-memory implementation (Singleton pattern)
service
FileSystemService: Interface for business logic (create, read, update, delete, navigation)
FileSystemServiceImpl: Implements the logic, uses repository
controller
FileSystemController: Exposes APIs for commands (pwd, ls, cd, etc.)
util
PathUtil: Utility methods for path parsing and validation
3. Design Patterns Used
   Composite Pattern: For Directory/File hierarchy
   Singleton Pattern: For InMemoryFileSystemRepository
   Command Pattern: For extensible command execution (optional, for CLI)
   Factory Pattern: For node creation (optional)