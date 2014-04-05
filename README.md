
ClojureScript Project that creates an animated WWDC2014 Logo

You can see it in action here:



This was mostly a first time clojurescript experiment for me. I've worked a bit with Clojure in the past, but I'd never worked with ClojureScript yet. Also, my Javascript knowledge is a bit rusty. Therefore, the code is probably awful. Instead of functional paradigms, it utilizes lots of atoms to catch state. It is also fairly verbose and not very fast. I suppose a native javascript implementation would be far faster. Also, maybe I should have worked with Canvas instead of creating and modifying divs.

However, I mostly just wanted an easy experiment so that I could play around with ClojureScript, and that worked fine.

The animations were rendered out with Blender3D and then converted into the frame-vector format with a simple Python script. There's a lot of room for optimization here, especially since the JSON files are rather verbose.

## License

Copyright © 2014 FIXME

Distributed under the Eclipse Public License, the same as Clojure.
