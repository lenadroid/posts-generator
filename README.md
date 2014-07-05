Generator of HTML content for xml data
======================================

USAGE:

Output file, before (Posts.html):

```html
<!DOCTYPE html>
<html>
<head>
    <title>Posts</title> 
</head>
<html>
   <body>
  <!-- generated content starts -->
  <!-- generated content ends -->

  <!-- Yandex metrica starts -->
  <!-- Yandex metrica ends -->

  <!-- Twitter Section starts -->  
  <!-- Twitter Section ends -->
  </body>
</html>
```

Configuration xml file (config.xml):
```xml
<config>
	<item>
	    <inputfile>settings\\posts.xml</inputfile> <!-- path to template file-->
		<outputfile>Posts.html</outputfile> <!-- path to output file -->
		<startcommenttext>generated content starts</startcommenttext> <!--text of start template-comment in output file-->
		<endcommenttext>generated content ends</endcommenttext> <!-- text of end template-comment in output file-->
	</item>
	<item>
		<inputfile>settings\\twitter.html</inputfile>
		<outputfile>Posts.html</outputfile>
		<startcommenttext>Twitter Section starts</startcommenttext>
		<endcommenttext>Twitter Section ends</endcommenttext>
	</item>
	<item>
		<inputfile>settings\\metrica.html</inputfile>
		<outputfile>Posts.html</outputfile>
		<startcommenttext>Yandex metrica starts</startcommenttext>
		<endcommenttext>Yandex metrica ends</endcommenttext>
	</item>
</config>
```

Sample directory structure:

\projectroot
------------\settings
             --------config.xml
             --------twitter.html
             --------posts.xml
             --------metrica.html
-------------Posts.html

After running the project: scala generator.scala
As a result there is such content in our output file (Posts.html):
```html
<!DOCTYPE html>
<html>
<head>
    <title>Posts</title> 
</head>
<html>
   <body>
  <!-- generated content starts -->
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
         <article class='post' >
               <header class='title' >
                  Hello World!2
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
               <a class='link' href='hello2.html' >
                  hello-world
               </a>

         </article>
         <article class='post' >
               <header class='title' >
                  Hello World!3
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
               <a class='link' href='hello3.html' >
                  hello-world
               </a>

         </article>

      </section>
  <!-- generated content ends -->

  <!-- Yandex metrica starts -->

        <a href="https://metrica.yandex.com/stat/?id=25344614&amp;from=informer"
        target="_blank" rel="nofollow"><img src="//bs.yandex.ru/informer/25344614/3_1_FFFFFFFF_EFEFEFFF_0_pageviews"
        style="display:none; width:88px; height:31px; border:0;" alt="Yandex.Metrica" title="Yandex.Metrica: data for today (page views, visits and unique visitors)" onclick="try{Ya.Metrika.informer({i:this,id:25344614,lang:'en'});return false}catch(e){}"/></a>

        <script type="text/javascript">
        (function (d, w, c) {
            (w[c] = w[c] || []).push(function() {
                try {
                    w.yaCounter25344614 = new Ya.Metrika({id:25344614,
                            webvisor:true,
                            clickmap:true,
                            trackLinks:true,
                            accurateTrackBounce:true});
                } catch(e) { }
            });

            var n = d.getElementsByTagName("script")[0],
                s = d.createElement("script"),
                f = function () { n.parentNode.insertBefore(s, n); };
            s.type = "text/javascript";
            s.async = true;
            s.src = (d.location.protocol == "https:" ? "https:" : "http:") + "//mc.yandex.ru/metrika/watch.js";

            if (w.opera == "[object Opera]") {
                d.addEventListener("DOMContentLoaded", f, false);
            } else { f(); }
        })(document, window, "yandex_metrika_callbacks");
        </script>
        <noscript><div><img src="//mc.yandex.ru/watch/25344614" style="position:absolute; left:-9999px;" alt="" /></div></noscript>
  <!-- Yandex metrica ends -->

  <!-- Twitter Section starts -->  
     
        <a class="twitter-timeline" href="https://twitter.com/lenadroid" data-widget-id="479959426271432705">
            Tweets by @lenadroid
        </a>
        <script>

        !function(d,s,id){var js,fjs=d.getElementsByTagName(s)[0],p=/^http:/.test(d.location)?'http':'https';if(!d.getElementById(id)){js=d.createElement(s);js.id=id;js.src=p+"://platform.twitter.com/widgets.js";fjs.parentNode.insertBefore(js,fjs);}}(document,"script","twitter-wjs");
        </script> 
  <!-- Twitter Section ends -->
  </body>
</html>
```
