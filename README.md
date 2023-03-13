# BaseDeDados
Used to clean/merge Excel Files for the 'Simulador' program.
Merges excel files containing Stock Market Candles Data while evaluating it's content.
Removes cloned candles, import daily indicators into intra-day data.

## Importing Data:
 The Excel file containing the candle information should have the following format:
### Collums:
6 collums in the following order:
 - Date
 - opening
 - maximum
 - minimun
 - closing
 - indicator
### 1st row
 - Titles only. The last collum title, relating to the 'indicator' will be imported into the code
 ### every other row:
 - Candle data.
