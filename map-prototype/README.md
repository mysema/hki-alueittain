What is this?
=============

This is a simple HTML prototype written in ERB that is designed to be
viewed with Serve.

What is Serve? Serve is an open-source rapid prototyping framework for Web
applications. It makes it easy to prototype functionality without writing a
single line of backend code.

How do I install and run Serve?
-------------------------------

Serve is distributed as a Ruby gem to make it easy to get up and running. You
must have Ruby installed in order to download and use Serve. The Ruby download
page provides instructions for getting Ruby setup on different platforms:

<http://www.ruby-lang.org/en/downloads/>

After you have Ruby and the Ruby bundler installed, open up the command prompt and type:

    bundle install

This will install Ruby gems according to the contents of the Gemfile. It is probably
best not to install the gems in your global environment, instead use a tool like 
[rvm](https://rvm.io/).

After Serve is installed, you can start it up in a given directory like this:

    serve

This will start Serve on port 4000. You can now view the prototype in your
Web browser at this URL:

<http://localhost:4000>

Compass and Sass
----------------

This prototype uses Compass and Sass to generate CSS. Learn more about Sass:

<http://sass-lang.org>

Learn more about Compass:

<http://compass-style.org>

Exporting
---------

To export your project, use the new "export" command:

    serve export project output

Where "project" is the path to the project and "output" is the path to the
directory where you would like your HTML and CSS generated.

Learning More
-------------

You can learn more about Serve on the GitHub project page:

<http://github.com/jlong/serve>
