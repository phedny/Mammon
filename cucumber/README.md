Mammon Cucumber
===============

Mammon is a distributed IOU management system, capable of running in
daemon mode on a server and also able to run on a mobile phone. A
peer-to-peer network between servers allows (circular) debts to be
resolved, while the mobile application allows users to issue and
transfer IOUs without requiring an active internet connection.

See the [homepage](http://phedny.github.com/Mammon/ "Mammon Homepage")
for a more thorough introduction to Mammon.

Cucumber
--------

[Cucumber](http://cukes.info/ "Cucumber Homepage") make Behaviour
Driven Development fun. We use it to verify that our code delivers our
intended use cases.

Environment
-----------

We use [maven](http://maven.apache.org/ "Maven homepage") to build our
software. See the 
[documentation](http://maven.apache.org/run-maven/index.html "Running Maven")
for more information about running maven.

### Eclipse

To generate project files for Eclipse run

    > mvn eclipse:eclipse

### Test

To run the test suite execute

    > mvn test
