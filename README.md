# dir-diff
An utility which compares two directories and creates new directory with a difference.

This repo has two main working branches:
- maser
- multithreaded

**master** implements main functionality of the application: generating a two-directories
difference in the third directory.

**multithreaded** branch contains a simple thread pool implementation in order to
make copying of the files faster. It actually works, (43,... milliseconds on 
single-threaded version agains the 25,... milliseconds on multithreaded version on
500 directories in each _first_ and _second_ directories, resulting into the 500
directories in the _result_ dir).
