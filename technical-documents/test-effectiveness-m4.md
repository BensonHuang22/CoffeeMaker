| # | What was changed | Where | Caught? | Test that caught it, or test you added |
|---|---|---|--------|----------------------------------------|
| 1 | changed >= to > | service ln 65 | Caught | MakeRecipeControllerTest |
| 2 | moved the inventory removal to the inventory search logic | Service lns 78-89 | Escaped | MakeRecipeControllerTest |
| 3 | changed <= to < | controller ln 99 | Escaped | MakeRecipeControllerTest |
| 4 | removed the update to calculate the change | controller ln 100/101 | Caught | MakeRecipeControllerTest |
| 5 | swapped conflict types bad request <-> conflict | controller lns 62/63 | Caught | MakeRecipeControllerTest               |

Coverage for MakeRecipeServiceImpl/MakeRecipeController

Statement Coverage Before 100%/95%

Branch Coverage Before 100%/88%

Statement Coverage After 90%/95%

Branch Coverage After 72%/88%

The escaped bugs tended to lean towards boundaries that I didn't think to explicitly check. 
For example #3 where I needed to check if the amount paid for a recipe was the exact amount.
