# Tingo

__Tingo__ is a web application built to manage teacher bingos. It uses technologies like AngularJS 
(JS is bad, I know, but this is supposed to be a simple and fast project), lesscss and the Spring
Framework. Also check out [sic](https://github.com/literalplus/sic), which is a citation management software.

## What is it?

Specifically, _Tingo_ allows anyone knowing a secret code to register, view and manage bingo fields. 
Teachers do currently have to be created by an administrator, but this might change in the future.
It also provides a convenient print view which allows people to print Tingo fields for playing.

## How to play

Print a grid of Tingo fields for a teacher. Each filed represents something (dumb?) they often do.
Once they do that, you mark that field using a pencil. If you have five marked fields in a vertical,
horizontal or diagonal row, you win. If you prefer, you can stand up and tell the rest of the class
by shouting "Bingo!". Then erase the marks you made and start over again.

Obviously, the game is most fun when played with friends. You can cooperate on creating the fields and
also play together. The fields are printed in random order to prevent everyone winning at the same time.

Tingo can obviously be used for things different than teachers, such as TV shows, politicians, university
professors, anything. Use your imagination.

## Installation

To install _Tingo_, you first need to build it: (assuming you're on GNU/Linux, other platforms work similarly)

````bash
git clone https://github.com/literalplus/tingo.git
cd sic
./gradlew build
cp application.yml-default application.yml
````

Then, you need to edit the `application.yml` config file with your favourite editor and insert database credentials.
_Tingo_ was built for use with MySQL or MariaDB. You do not need to perform any database setup except for creating the
database you named in your config file. (`tingo` by default)

You can then run the application either using Gradle directly:

````bash
./gradlew bootRun
````

or by running the `.jar` file created in the `build/libs/` directory.

It will start on port 8080 by default.

## License

This project is licensed under the Apache License, Version 2.0. You can find a copy in the `LICENSE` file.

## Support

Please, just open an issue at [GitHub](https://github.com/xxyy/tingo/issues). Pull Requests are obviously welcome. 
