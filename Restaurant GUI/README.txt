How to Run Program:
Make sure you have:
    phase1:
        src:
            dishes:
                Combo.java
                Food.java
                Recipe.java
            employees:
                Chef.java
                Employee.java
                Manager.java
                Server.java
            eventhandlers:
                ChefHandler.java
                EventHandler.java
                ManagerHandler.java
                OtherHandler.java
                ServerHandler.java
            restaurant:
                Inventory.java
                Menu.java
                Order.java
                Restaurant.java

        Ingredients.txt
        menu.txt
        README.txt

Open up restaurant.java with IntelliJ and run the Restaurant.java file.

-----------------------------------------------------------------------------------------------------------------------
Menu Format

For Individual Foods
Menu item number - Food Name - Food Price - [ingredient1:quantity, ingredient2:quantity, ...]
    ex:
1 - Cheeseburger - 6.99 - [cheese: 1, beef patty: 1, bun: 2]

For Combos
Combo + Menu item number - [Food1, Food2, ...] - Price
Note: Food1, Food2, ... have to be on the menu and there should be at least one food.
    ex:
Combo1 - [Fries, Coke, Cheeseburger] - 12.99

Don't have the same menu item number for two different menu items.
The menu items numbers should be in ascending order.
Note that names of foods and combos are case sensitive in later parts of this README
-----------------------------------------------------------------------------------------------------------------------
How To Use:

Identifiers:
Restaurant
Chef_<ID Number>
Manager_<ID Number>
Server_<ID Number>
Table_<Table Number>

Basics:
-------
Each line in the Event.txt should start off with an identifier (it is case sensitive).
Ex:
    Restaurant ...
    Chef_2 ...
    Manager_1 ...
    etc.
    (Ellipses are fillers, not actual commands, and etc.).

Also no line in the Events.txt should end with punctuation.
Orders will all be identified by Table_<Table Number>
Ex:
    Server_$ adds Table_3 ...
        This will refer to Server_3 and they will be adding a menu item to Table_3's order.

Commands:
Each identifier is followed up by a command, each identifier only works with their specific commands. All commands are in lowercase.

Restaurant commands:
--------------------
hires
    adds a new employee of a specified class into the program.
    Ex:
        Restaurant hires Chef
        Note: no id number is required in this field as the employee will be assigned on instantiation of the class. It is up to the user to keep track of available chefs and their ID numbers.
fires
    removes the employee from the program and stores their ID number to be used for hiring new employees of the same class.
    Ex:
        Restaurant fires Server_4

Server commands:
----------------
adds
    adds a menu item to the specified table.
    Ex:
        Server_5 adds Table_2 ...

order
    Creates a new order, no table or table number is specified as one will be assigned based off availability.
    Ex:
        Server_4 order

complaint
    Files a complaint with the menu item formatted as:
    <Server_ID> complaint <food> | <additions> | <subtractions> ~ string of complaint
    Ex:
        Server_4 complaint Burger | Onions:3 | Patty:1 ~ Tastes like trash

delivered:
    gets the chef to deliver the menu item to the specified table.
    Ex:
        Server_99 delivered Burger | Onion:2, Lettuce:3 | Patty:1

remove:
    removes the Order from the restaurant and has them pay the bill.
    Ex:
        Server_14 removes Table_3

Chef commands:
--------------
acknowledged
    prints out that the order has been acknowledged by a chef specified by the table number.
    Ex:
        Chef_3 acknowledged Table_2

cooking
    This changes the status of a food item in the table's order to cooking.
    Ex:
        Chef_5 cooking Table_3 Burger | none | none

cooked
    This changes the status of a food item in the table's order to cooked. Same way of writing as cooking

Manager commands:
-----------------
assign
    This makes the manager assign the designated employee into a receiver.
    Ex:
        Manager_9 assign Chef_4
            This allows Chef_4 to be a receiver

checks
    This allows the manager to check what is in the inventory and the quantity and threshold of all the foods.
    Ex:
        Manager_12 checks

Receiver commands:
------------------
receive
    get the new shipment of ingredients and adds it to the inventory.
    Ex:
        Chef_7 receives

Order formatting:
-----------------
Orders are separated in a very particular way. Ordering a combo and an individual food is slightly different but important to know the difference.
When ordering food it's important to know to separate using the "|" that's the or sign ||, or the " ", whatever terminology you want to use.
This division is used to separate the food name, its addition and its subtractions.
Commas, "," are used to separate multiple additions/subtractions.

Standard formatting: <Server_ID> [command] <Table Number> <Food name> | <Ingredient1 : quantity>, <ingredient2 : quantity>, ... <ingredientN :quantity> | <Ingredient5 : quantity>
Ex:
    Server_1 adds Table_3 Bacon Cheese Burger | Lettuce:3 | Bacon:1, Buns:1
        this will make Server_1 adds a Bacon Cheese Burger with 3 extra lettuce and 1 less bacon and no buns to the Table_1.

Combos are essentially the same except they also use semicolons as well to separate the multiple different additions and subtractions between the multiple food items available.
Standard formatting: <Combo Name> ; <Ingredient1 : quantity>, ... | <IngredientX : quantity>, ... ; <IngredientY : quantity>, ... | <IngredientF : quantity>, ... ; ...
Ex:
    Server_5 delivered Table_8 Combo3 ; Onion:2 | Cheese:1 ; none | none ; Salt:20 | none
        For reference Combo3: [Bacon Cheese Burger, Medium French Fries, Medium Soft Drink]
        This line would make Server_5 deliver Table_8 a Combo3 with additional onion in the Bacon Cheese Burger with 1 less cheese,
        no changes to the medium fries and adds salt to the medium soft drink.

NOTE: if one does not want additions or subtractions, none must be used to specify. in each respective field.
    Ex:
        Server_8 adds Burger | none | none

-----------------------------------------------------------------------------------------------------------------------
Notice:
- In the git repository, the pushes made by the user names Victor Huang and yomanpwnage are the same people.
    - Also --replace all, Thomas, and Thomas Leung are all the same people, hence why the email is the same.

- We are a group of three as our group member in the Markus, Hangbo, dropped 207.

-----------------------------------------------------------------------------------------------------------------------
Code Cited:
Melih Altintas (2013 Oct 8) removing words from a string in java (Unknown) [source code]. https://stackoverflow.com/questions/19257172/removing-words-from-a-string-in-java