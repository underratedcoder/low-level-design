You are a seasoned software engineer with 10+ years of experiece in writing code in java. you are giving an interview for a company where you are asked low level design of splitwise application with below Functional and Non Functional Requirements requirements

Functional Requirements -
    1. Users can create a group
    2. Users can be added to a group or they can join a group
    3. Users can add expenses between some members or all members of the group and the payer
    4. Users can split expenses equally or in some percentage
       (percentage should be equal to 100%)
    5. A balance sheet should be maintained where its tracked which member has to pay how much amput to which other members and which member has to take money for which other members
    6. Users should be able to see whom they have to pay or from whom they have to take after optimisation
    7. Users can settle the expenses
    8. Optimal Settlement Plan - Minimum transactions are required
    
Non Functional Requirements -
    1. Scalable Entities (Should adapt any new requirements)
    2. ACID
    3. Handle high concurrency

Below is the list of entities and services used in the application

Entity
Group               - A group with some name 
User                  - Users can be added to a group
Expense           - It keep track of the money spent by the member and how many participants were there and what is the split strategy
BalanceSheet  - It keep tracks with which member will pay money to which other members and how much
SettleUp        - It keeps track of how much money the member will send to or receive from other member(s) after optimization
SplitType         - Equal, Percentage

Service
GroupManager      
UserService          
ExpenseManager        
BalanceSheetManager   
TransactionManager     

Repository
GroupRepository.               - Manages all the group on splitwise
UserRepository                 - Manages all the members
ExpenseRepository              - Manages all the expenses done on a groups and balance sheet
BalanceRepository              - Manages balance sheet and SettleUp
TransactionRepository          - Manages all the transaction done

Below is the list of Design Patterns you should use, you can also use other patterns if you want to.
    1. Singleton
    2. Builder
    3. Strategy
Split equally, Split by %
    4. Factory
        Create strategy object based on SplitType
    5. Observer
        - Notify members when they are added in any expense
        - Notify members when their transaction is settled by someone

Write the code in plain java by following the layered architecture i.e, your code should have differenct packages such as controller, service, repository, model, enums, exception etc. Create the interfaces whereever needed. Follow SOLID principles and applicable design patterns.

Create packages at this path /Users/akash/Study/Low Level Design/Problems/splitwise/src/main/java/com/lld/splitwise

User lombok framwork and do not write boilerplate code