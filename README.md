# dir-diff
An utility which compares two directories and creates new directory with a difference.

This repo has two main working branches:
- maser
- multithreaded

**master** implements main functionality of the application: generating a two-directories
difference in the third directory.

**multithreaded** branch contains a simple thread pool implementation in order to
make copying of the files faster. It actually works (a bit), (39,... milliseconds on 
single-threaded version against the 34,... milliseconds on multithreaded version on
500 directories in each _first_ and _second_ directories, resulting into the 500
directories in the _result_ dir).

# build

`mvn clean install` or `mvn clean package`

taking  `dirdiff-1.0.0-shaded.jar`


