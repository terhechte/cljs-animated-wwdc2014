(defproject project "0.1.0-SNAPSHOT"
    :description "FIXME: write description"
    :url "http://example.com/FIXME"
    :license {:name "Eclipse Public License"
              :url "http://www.eclipse.org/legal/epl-v10.html"}
    :dependencies [[org.clojure/clojure "1.5.1"][org.clojure/clojurescript "0.0-2202"]]
    :plugins [[lein-cljsbuild "1.0.3"]]
    :cljsbuild {
                :builds [{
                          ; The path to the top-level ClojureScript source directory:
                          :source-paths ["src-cljs"]
                          ; The standard ClojureScript compiler options:
                          ; (See the ClojureScript compiler documentation for details.)
                          :compiler {
                                     :output-to "assets/javascripts/main.js"  ; default: target/cljsbuild-main.js
                                     :optimizations :whitespace
                                     :pretty-print true}}]})
