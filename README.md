This application uses an API to gather weather data based off a zip input given from the user. After valid zip is entered and weather data is recieved from the api, it is displayed
in the console and logged into a file. 

The API being used is OpenWeatherMap.
OpenWeatherMap is an online service owned by OpenWeather Ltd, providing global weather data accessible through an API. This API offers access to current weather data,
forecasts (including minute-by-minute hyperlocal precipitation), and historical weather data for any geographical location. However for this program, it only uses the 
5 Day / 3 Hour Forecast API

Threat Modeling:
A threat that wouldn't be handled would be because of my dependency on T=the external API. If anything were to happen to their service the program would no longer work.

1. If my API Key was leaked, people would be able to freely use it without my knowledge. By storing it in a secret location its not freely given in the code.
2. Input Validation is another threat that was fixed, by making sure that a zip is only accepted and sent out if it has valid looking zip code
3. The third threat would be error handling. Instead of printing a stack trace that would expose alot of sensitive info, a string is sent back saying something along the lines of
   "Zip doesn't look right, try something like 17701" or "Error: Could not retrieve weather data. Please check the zip code or try again later."
