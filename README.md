# dir-diff
An utility which compares two directories and creates new directory with a difference.

**master** implements main functionality of the application: generating a two-directories
difference in the third directory.

**multithreaded** branch contains a simple thread pool implementation in order to
make copying of the files faster. It actually works (a bit), (39,... milliseconds on 
single-threaded version against the 34,... milliseconds on multithreaded version on
500 directories in each _first_ and _second_ directories, resulting into the 500
directories in the _result_ dir).

# build

`mvn clean install` or `mvn clean package`

taking`dirdiff-1.0.0-shaded.jar`

# run

`dirdiff-1.0.0.-shaded.jar firstDirectory secondDirectory resultDirectory`, where the `firstDirectory` and `secondDirectory` are directoriws, which are compared to each other, and the `resultDirectory` is the directory with the result (difference between two directories).
