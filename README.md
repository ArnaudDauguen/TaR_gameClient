# I. Presentation
Try and Redie is the 'Fil Rouge' project for Ingésup B3a.
Goals are to create a game and an AI to play in. This project game is, for the player, to design maze as complex as possible. The AI will someday solve the maze by dying inside and founding the best path. Once completed, the player will gain score based on the number of the AI fails.

This part is the application used for sending AI in the designed dungeons.

This part cannot work properly without having the first part, API *(see part 3.)*

# 2. How to use
## Start the .jar file
You can start the `TaR_gameClient.jar` file by :
* using java when double-clic on it
* use console and type `java -jar <path-to-'TaR_gameClient.jar'>`
## Import in Eclipse
* in `File` choose `Import`
* in `Maven` folder choose `Existing Maven Repository`
* `Browse` and select the `TaR_gameClient` folder
* wait for dependencies download and installation

You can found sources in the `src` folder and main fil is `src/UI/window.java`, from this file you can launch the app.

## Playing
* Choose a dungeon using arrows int thr bottom of the window
* Press the `Send AI`
* Enjoy

# 3. Pickup API
Please visit my [first repo](https://github.com/ArnaudDauguen/TaR-api)


# 4. Pickup level editor
Please visit my [third repo](https://github.com/ArnaudDauguen/TaR_levelEditor)
