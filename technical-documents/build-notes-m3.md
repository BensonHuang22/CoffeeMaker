Prompt Given to AI:
I am building a page in a React + Vite frontend that uses Bootstrap 5 classes and 
react-router-dom. Components live in src/components/ and export a single function component. Here is the wire frame for the page: Write the JSX for the static version of this page: layout and form elements only, with realistic placeholder data. No API calls and no state management yet. 

can you write this as a static html page 



Fields that are not in your wire frame, and fields of yours that are missing
The AI reproduced the static page with exactly all the correct fields albeit with slightly different placement of UI elements. There were no differences in names of fields. 
controls that changed type, like a dropdown you designed appearing as a text field, or the reverse
The control that changed type was the one used for input in the starting ​​amount for an ingredient. The control changed from a textbox on our original static page, which assumed any starting amount would be an integer, to a Numeric Up Down control where users can only enter a number and increase or decrease it by clicking the up or down arrows. 


error handling that was invented
The ai was not able to handle any of the errors that we had in our original wireframe. Whenever a invalid input was given the system accepted it no matter what
labels and wording that differ from the rest of CoffeeMaker
There was one additional “Home” label at the top of the screen, which could be useful, but it might not be necessary since clicking the Coffee Maker logo would return to the home screen. 
usability and accessibility concerns
The forum control was very similar to our original design and all buttons were clearly labeled, it was easy to see how to input data and add ingredients
placeholder data, like does it demonstrate arbitrary ingredients, or did it default to coffee, milk, sugar, and chocolate?
The AI chose “Tea” as the temporary ingredient with a quantity of 10, likely because we used those values for some of the testing so it used those values instead of the default values for the course such as coffee and milk. 

Accepted Changes
The Numeric Up/Down control. 
