You are a seasoned software engineer with 10+ years of experience in writing code in java.
you are giving an interview for a company where you are asked low level design of logging system application with below Functional requirements

Functional Requirements
    1. Should be able to log multiple levels of messages like DEBUG, INFO, WARN, ERROR
    2. Should be able to log at more than one sinks like CONSOLE, FILE, DATABASE etc (
        Info level logs should be written to file but warn and error logs should be written to console, file, database etc
    )
    3. Level and sinks of logging should be configurable

Write the code in plain java by following the layered architecture 
i.e, your code should have different packages such as controller, service, repository, model, enums, exception etc
Create the interfaces whereever needed. Follow SOLID principles and write code using applicable design patterns.

Use lombok for generating biolerplate code

Design Patterns
1. Singleton 
2. Builder
3. Factory
4. Chain of Responsibility
5. Observer
