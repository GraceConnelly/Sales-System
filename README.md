#Sales-System
Your task is to write a web-based sales system that is designed to be used by multiple people *simultaneously*. Hint: This means sessions will play a role in the system. Remember sessions? They're important.

But first, enjoy this gif that basically summarizes everything you need to know about kids:

![kid](http://i.imgur.com/8xlbAb1.gif)

Okay, let's do this.

## Requirements
This system should:

- Login users
   - You can have a predefined set of users beforehand.
- Allow a user to build a "orders" of items.
   - You can just have three boxes here which comprise an item (quantity, name, and amount)
   - The user gets to pick (not like in real life) the item, its cost, and its quantity before it gets added to the orders.
- Allow a user to "checkout" the orders.
   - "Checking Out" is basically showing them an order summary page and clearing their orders.
   - This summary page should show the user data, and the details of their order (the quantities, amount, and total amount)

All of this information (the users, the items in their orders, and the orders) should be stored in a SQL database.

There should be 3 SQL tables that store this data:
1. `users`
2. `items`
3. `orders`

There is a 1-many relationship between users-orders, and a 1-many relationship between orders-items.

## HARD MODE
- Create a user registration page for custom users
- Create a predefined list of items that the user can choose from (like in real life, instead of them being able to arbitrarily set an item to purchase and its cost)
- Use semantic HTML, and make the site look nice with CSS
- Include a 7% sales tax with the purchase

## LUDICROUS MODE
- Add interactivity to your webpage with AJAX.
- Make the sales tax dependent on where the user made the order
