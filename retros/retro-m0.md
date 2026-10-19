# Milestone 0 Retrospective

Each week, you'll be asked to reflect on the current milestone.  You may be asked to reflect on the technical tasks, AI use, durable skills, or other relevant topics appropriate for the milestone.  For Milestone 0, you'll reflect on using AI (or not) to find bugs in CoffeeMaker.


## AI Reflection

Copies of the AI starter prompts from the Debugging Workshop are below for you to adapt.
For each item, do the following:

  * Add any edits you made to the prompt
  * Summarize the results
  * Describe if the results were useful and how you would proceed given the results

### Compare implementation against a requirement

```
Here is Use Case 2 (Add Recipe) from our requirements:
<paste the use case, including its alternative flows>

Here are the relevant classes: RecipeController, RecipeService, Recipe.

List every place the implementation appears NOT to match this requirement.
For each one, quote the specific requirement text and the specific line of
code, and tell me what you would do in the UI to observe the difference.
```

**Results Summary:**
n/a

**How would you proceed?**
I didn't use AI to find difference between the implmentation and requirement. To find my bugs, I directly went to frontend and manually tested the requirements to write the bug reports. I can see how AI would be very usefull to fixing the bugs when needing to find the exact location of the bug.

**Identify tests to exercise static analysis notifications**

```
For the ESLint notification listed below, suggest a flow of events that would uncover the fault as a failure.

[Notification text here]
```

**Results Summary:**
n/a

**How would you proceed?**
I didn't use AI to check the notificaions and there didn't seem to be any notifications in the terminal. In other assignments I have used AI to help me find solutions to notifications, but this lab didn't require it.

### Hunt for unhandled alternative flows

```
This use case lists the following alternative flows:
<paste the alternative flows>

For each one, tell me whether the code appears to handle it, and which
method would be responsible. Point out any that appear unhandled.
```


**Results Summary:**
n/a

**How would you proceed?**
I didn't use AI, so I tested alternate flows through the website frontend. 

### Generate boundary inputs to try

```
This endpoint accepts <describe the fields and their types>.
Give me a list of input values that would be worth trying, grouped into
equivalence classes, including boundary values and invalid inputs.
Do not write tests -- I just want the list of values to try by hand.
```


**Results Summary:**
n/a

**How would you proceed?**
I didn't use AI, but I can see how AI would save a lot of time with very repetitive work.

### Explain, then verify

```
Explain step by step what this method does, including what happens when
<field> is empty, zero, or negative.
```


**Results Summary:**
n/a

**How would you proceed?**
I didn't use AI because the lab didn't require us to understand the methods in such a high level of detail. We only need to find bugs against the requirements.

### Your own prompts

**Prompt**
n/a
**Results Summary:**
no use of ai

**How would you proceed?**