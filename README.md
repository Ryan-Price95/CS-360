# CS-360

I had issues getting the zip file to upload, so I just uploaded the individual files that were the most necessary in my opinion. 

### Briefly summarize the requirements and goals of the app you developed. What user needs was this app designed to address?
The goal was to build an application that would store user log in information and allow users to track inventory for their business, including receiving SMS notifications when an inventory item is reduced to zero on hand. 

### What screens and features were necessary to support user needs and produce a user-centered UI for the app? How did your UI designs keep users in mind? Why were your designs successful?
First I had a log in screen where users would input their username and password. This screen had a button for creating a new account and one for logging in. Next was the main inventory page that would show all items that the user had created, as well as the location in the store, quantity, name, and a tracking code (such as a UPC or SKU code). On this screen, users were able to add inventory via a button at the bottom of the screen, and they were able to move to the Notification Settings page by another button at the bottom. Additionally, users could long press on an individual item and bring up a menu to either edit an item (the name, location, quantity) or they were able to completely delete it. 

By ensuring that the design was minimal but inclusive of all main needs for a user, I was able to keep the user experience at the forefront of the designing process. I think there are a couple of things that I could add (such as the ability to scan items into the system by using the back camera of the phone) but those were outside the scope of this project. 

### How did you approach the process of coding your app? What techniques or strategies did you use? How could those techniques or strategies be applied in the future?
My first step is always creating either pseudocode or a class diagram, often both, so that I could work through the functions that needed to be created before I even started actually coding. This helps me by ensuring that I am focusing on the base requirements before I start adding additional stuff. 

### How did you test to ensure your code was functional? Why is this process important, and what did it reveal?
I did a lot of manual testing for this, at multiple stages throughout development. I manually went through each individual function to ensure that it was working. This is important because releasing a buggy, not fully functional app, will frustrate users and ultimately turn them away from your product. Through testing I found multiple issues that I needed to address, like when I found that I did not have any check for whether or not an item was already added to the inventory, allowing users to add multiple items with the same ID. 

### Consider the full app design and development process from initial planning to finalization. Where did you have to innovate to overcome a challenge?
I consistently had issues with the layout of the inventory screen, where users would spend most of their time.

### In what specific component of your mobile app were you particularly successful in demonstrating your knowledge, skills, and experience?
I think the main inventory screen was fairly well laid out in the end and had all of the functionality that I was needing to add. Additionally, the functionality of the system on that screen did everything that I was expecting it to do in a clean, efficient way. 
