Generator of HTML content for xml data
======================================

Sample configuration xml file (posts.xml):
```xml
<posts tag="section">
    <post tag="article">
        <title tag="header">Hello World!</title>
        <description tag="div">Yay! This is a first test post in my software engineering blog!</description>
        <date tag="div">23.06.2014</date>
        <keywords tag="div">
            <keyword tag="span">hello world</keyword>
            <keyword tag="span">functional programming</keyword>
            <keyword tag="span">software engineering</keyword>
        </keywords>
        <link tag="a" href="hello1.html">hello-world</link>
    </post>
</posts>
```
Sample output (for now, just in cosole and in file Posts.html):
```html
<section class='posts' >
   <article class='post' >
      <header class='title' >
         Hello World!1
      </header>
      <div class='description' >
         Yay! This is a first test post in my software engineering blog!
      </div>
      <div class='date' >
         23.06.2014
      </div>
      <div class='keywords' >
         <span class='keyword' >
            hello world
         </span>
         <span class='keyword' >
            functional programming
         </span>
         <span class='keyword' >
            software engineering
         </span>
      </div>
      <a class='link' href='hello1.html' >
         hello-world
      </a>
   </article>
```
