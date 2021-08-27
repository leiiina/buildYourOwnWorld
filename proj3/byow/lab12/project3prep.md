# Project 3 Prep

**For tessellating hexagons, one of the hardest parts is figuring out where to place each hexagon/how to easily place hexagons on screen in an algorithmic way.
After looking at your own implementation, consider the implementation provided near the end of the lab.
How did your implementation differ from the given one? What lessons can be learned from it?**

Answer:
The implementation provided in lab had a mathematical solution based off the positions of each hexagon. It also showed very high-level thinking. This demonstrates how we need to problem-solve for project 3.

-----
**Can you think of an analogy between the process of tessellating hexagons and randomly generating a world using rooms and hallways?
What is the hexagon and what is the tesselation on the Project 3 side?**

Answer:
Rooms would be the "hexagons" for project 3, and the tesselation would involve connecting rooms via hallways in a logical way.

-----
**If you were to start working on world generation, what kind of method would you think of writing first? 
Think back to the lab and the process used to eventually get to tessellating hexagons.**

Answer:
I would start with a "Room" class that generated a room with walls. Then I would create a "Hallway" class that would create an opening in a room wall and connect itself to a room. Then I would create a class similar to "Tessellation" whose job it would be to connect hallways to each other.
-----
**What distinguishes a hallway from a room? How are they similar?**

Answer Both hallways and rooms hold open space that is connected to other open spaces. However, hallways have a fixed width, while rooms can be any size. Hallways are meant to connect rooms to each other.
