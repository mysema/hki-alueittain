Helsinki alueittain
===================

Helsinki alueittain is a PDF publication containing statistical information
on different boroughs in Helsinki. Excel files are used for gathering all 
the statistics prior to publication. This is a prototype implementation
of a web version of the publication, specifically a prototype of the publication 
process.

The goal is to reuse the same Excel files as are used for the PDF publication.
In order to use the Excel data, we need a few mappings: one to the data model
used internally by the prototype and one to configure how the statistics are
presented to the end user (grouping, whether or not the city's average should
be shown and so on). All in all, we wanted to make a publication process that
is as flexible as possible in the hands of a normal user.

### Usage

The prototype is implemented using Clojure. You will need the 
[Leiningen](https://github.com/technomancy/leiningen) build tool. 
With Leiningen installed, you can start the web application at the 
command prompt:

```
$> lein ring server
```

Then, open up a browser and visit http://localhost:3000. 

At first, there are no statistics to show. Visit the "Ylläpito" page to 
upload statistics and configuration files. Examples that can be used are
found in the ```etc/``` directory. After publishing the data, you can see
statistics for different areas of Helsinki on the first page.

#### Why Clojure?

Clojure is an excellent tool for writing web applications. Its expressiveness
means that you have to write very little code to get a whole lot done. 
It is therefore particularily well suited for rapid prototying of web applications. 
Being on the JVM is also a good thing, since there are excellent Java libraries
for dealing with Excel spreadsheets.



