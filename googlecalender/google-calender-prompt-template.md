You are a seasoned senior software engineer with 10+ years of experience in writing code in java.
you are giving an interview for a company where you are asked low level design of google calendar application with below Functional requirements

Functional Requirements
  1. Each member have their own calendar
  2. Users can create event with tile, start time, end time, description and other participants
  3. Events can be one-time or recurring (Daily, weekly, monthly, every Monday, 1st tuesday of every month)
  4. Participants can accept, reject, maybe a single instance of event or all instance of event invites
  5. User can edit a single instance or all instance of an event (time, description etc)
  6. For each member, we should be able to see the events day-wise (including recurring events)
  7. Notify member about event via one or more channel (Email, In-App)


Write the code in plain java by following the layered architecture 
i.e, your code should have different packages such as controller, service, repository, model, enums, exception etc
Create the interfaces whereever needed. Follow SOLID principles and write code using applicable design patterns.

Use lombok for generating biolerplate code
