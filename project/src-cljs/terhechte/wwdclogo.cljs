(ns terhechte.wwdclogo
  ; So it seems clojure.core/format is not available in CLJSFiddle. Or at least all my attempts of importing
  ; It or cljs.core/format failed. Then I stumbled upon this: http://dev.clojure.org/jira/browse/CLJS-324
  ; And followed the provided sample code, importing goog.string.format. This, at least, works. 
  (:require [goog.string :as gstring]
            [goog.string.format]
            [goog.net.XhrIo :as jsonp]
            [goog.style]
            [goog.dom :as dom]
            [clojure.string :as string]
            [goog.dom.query :as query]))

(enable-console-print!)

(def grid-size {:w 40 :h 20}) ; how many boxes in x / y
(def box-size 18) ; size of a box in x / y
(def box-margin 1)

(def anim-speed (atom 0.35)) ; how many seconds per frame
(def fps (atom 8))

(def contents {:apple {:fps 2 :speed 1.05}
               :bird {:fps 5 :speed 0.60}
               :wwdc {:fps 8 :speed 0.35}
               :stars {:fps 5 :speed 0.25}})

;; Set the container size
(goog.style.setWidth (first (goog.dom.query "#content")) (* (+ (* 2 box-margin) box-size) (:w grid-size)))

(defn box-name [x y]
  "Simple addressing of containers by x/y position"
  (gstring/format "box%i-%i" x y))

(defn create-boxes [g b m]
  "create g amount of boxes of b size"
  (doseq [iy (range 0 (:h g))
          ix (range 0 (:w g))]
    (let [n (box-name ix iy)
          ; Generate Rainbow Colors
          f (/ 5 (:w g))
          red (+ 148 (* 127 (.sin js/Math (+ (* ix f) 4))))
          green (+ 148 (* 127 (.sin js/Math (+ (* ix f) 0))))
          blue (+ 148 (* 127 (.sin js/Math (+ (* ix f) 2))))
          
          ; Generate Style Part
          s (gstring/format "background-color: rgb(%i, %i, %i); 
                            width: %ipx; height: %ipx; 
                            float: left;
                            -webkit-transition: -webkit-transform %fs;
                            transition: transform %fs;
                            transition-timing-function: linear;
                            -webkit-transition-timing-function: linear;
                            border-radius: 3px;
                            margin: %ipx;"                     
                            red green blue b b @anim-speed @anim-speed m)
          p {:id n :style s}
          
          ; Create Element 
          e (dom/createDom "div" (clj->js p))
          ; Append to parent element
          o (goog.dom.query "#content")]
      (dom/append (first o) e))))

(defn remove-boxes []
  (goog.dom.removeChildren (first (goog.dom.query "#content"))))

(defn animate-boxes [g b m frame]
    (doseq [iy (range 0 (:h g))
          ix (range 0 (:w g))]
      (let [v (get frame (+ (* iy (:w g)) ix))
            n (box-name ix iy)
            e (first (goog.dom.query (str "#" n)))]
        (set! (-> e .-style .-webkitTransform) (gstring/format "scale(%f)" v)))))

;; We need a bit of state for the animation.
;; The frames
(def org-frames (atom []))
(def fcounter (atom 0))

;; The framerate calculation
(def fps-now (atom 0))
(def fps-then (atom (.now js/Date)))
(def fps-delta (atom 0))
(def animationframe (atom nil))

(defn render []
  ;; If we're through all frames, start again
  (if (> (+ 2 @fcounter) (count @org-frames))
    (reset! fcounter 0)
    (swap! fcounter inc))
  (animate-boxes grid-size box-size box-margin (nth @org-frames @fcounter)))

(defn animate []
  (reset! animationframe (.requestAnimationFrame js/window animate))
  ;; Only render again if enough time has passed / fps
  (reset! fps-now (.now js/Date))
  (reset! fps-delta (- @fps-now @fps-then))
  (when (> @fps-delta (/ 1000 @fps))
    (render)
    (reset! fps-then (- @fps-now (mod @fps-delta (/ 1000 @fps))))))

(defn parse-frames [json]
  "convert json to clj data"
  (let [data (js->clj (.getResponseJson (.-target json)) :keywordize-keys true)]
    ;; Set the frames into our internal cache
    (reset! org-frames data)
    (reset! fcounter 0)
    ;; We're done, start the animation
    (render)
    (animate)
    (set! (-> (goog.dom.query "#tagline") first .-style .-display) "none")))

(defn ^:export loadfile [ckey]
  "load the frames"
  (.cancelAnimationFrame js/window @animationframe)
  (set! (-> (goog.dom.query "#tagline") first .-style .-display) "block")
  (let [d ((keyword ckey) contents)]
    (when d
      ; Load the data
      (.send goog.net.XhrIo (str "javascripts/" ckey ".json")
             parse-frames)

      ; remove the old boxes
      (remove-boxes)
      (reset! fps (:fps d))
      (reset! anim-speed (:speed d))

      ; And create the Boxes
      (create-boxes grid-size box-size box-margin))))
   
(loadfile "apple") 
